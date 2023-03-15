package com.taobao.weex.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.common.WXThread;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.performance.WXInstanceApm;
import io.dcloud.common.util.StringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BoxShadowUtil {
    private static final String TAG = "BoxShadowUtil";
    private static boolean sBoxShadowEnabled = true;
    private static Pattern sColorPattern;

    public static void setBoxShadowEnabled(boolean z) {
        sBoxShadowEnabled = z;
        WXLogUtils.w(TAG, "Switch box-shadow status: " + z);
    }

    public static boolean isBoxShadowEnabled() {
        return sBoxShadowEnabled;
    }

    public static void setBoxShadow(View view, String str, float[] fArr, float f, float f2) {
        if (!sBoxShadowEnabled) {
            WXLogUtils.w(TAG, "box-shadow was disabled by config");
        } else if (view == null) {
            WXLogUtils.w(TAG, "Target view is null!");
        } else if (!TextUtils.isEmpty(str) || Build.VERSION.SDK_INT < 18) {
            BoxShadowOptions[] parseBoxShadows = parseBoxShadows(str, f);
            if (parseBoxShadows == null || parseBoxShadows.length == 0) {
                WXLogUtils.w(TAG, "Failed to parse box-shadow: " + str);
                return;
            }
            final ArrayList arrayList = new ArrayList();
            final ArrayList arrayList2 = new ArrayList();
            for (BoxShadowOptions boxShadowOptions : parseBoxShadows) {
                if (boxShadowOptions != null) {
                    if (boxShadowOptions.isInset) {
                        arrayList2.add(0, boxShadowOptions);
                    } else {
                        arrayList.add(0, boxShadowOptions);
                    }
                }
            }
            if (fArr != null) {
                if (fArr.length != 8) {
                    WXLogUtils.w(TAG, "Length of radii must be 8");
                } else {
                    for (int i = 0; i < fArr.length; i++) {
                        fArr[i] = WXViewUtils.getRealSubPxByWidth(fArr[i], f);
                    }
                }
            }
            final View view2 = view;
            final float f3 = f2;
            final float[] fArr2 = fArr;
            view.post(WXThread.secure((Runnable) new Runnable() {
                public void run() {
                    if (Build.VERSION.SDK_INT >= 18) {
                        view2.getOverlay().clear();
                        if (arrayList.size() > 0) {
                            BoxShadowUtil.setNormalBoxShadow(view2, arrayList, f3, fArr2);
                        }
                        if (arrayList2.size() > 0) {
                            BoxShadowUtil.setInsetBoxShadow(view2, arrayList2, f3, fArr2);
                        }
                    }
                }
            }));
        } else {
            view.getOverlay().clear();
            WXLogUtils.d(TAG, "Remove all box-shadow");
        }
    }

    private static void drawShadow(Canvas canvas, BoxShadowOptions boxShadowOptions) {
        RectF rectF = new RectF(0.0f, 0.0f, ((float) boxShadowOptions.viewWidth) + (boxShadowOptions.spread * 2.0f), ((float) boxShadowOptions.viewHeight) + (boxShadowOptions.spread * 2.0f));
        if (boxShadowOptions.topLeft != null) {
            rectF.offset(boxShadowOptions.topLeft.x, boxShadowOptions.topLeft.y);
        }
        float f = boxShadowOptions.blur;
        float f2 = boxShadowOptions.blur;
        if (boxShadowOptions.hShadow > 0.0f) {
            f += boxShadowOptions.hShadow * 2.0f;
        }
        if (boxShadowOptions.vShadow > 0.0f) {
            f2 += boxShadowOptions.vShadow * 2.0f;
        }
        rectF.offset(f, f2);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(boxShadowOptions.color);
        paint.setStyle(Paint.Style.FILL);
        if (boxShadowOptions.blur > 0.0f) {
            paint.setMaskFilter(new BlurMaskFilter(boxShadowOptions.blur, BlurMaskFilter.Blur.NORMAL));
        }
        Path path = new Path();
        float[] fArr = new float[8];
        for (int i = 0; i < boxShadowOptions.radii.length; i++) {
            if (boxShadowOptions.radii[i] == 0.0f) {
                fArr[i] = 0.0f;
            } else {
                fArr[i] = boxShadowOptions.radii[i] + boxShadowOptions.spread;
            }
        }
        path.addRoundRect(rectF, fArr, Path.Direction.CCW);
        canvas.drawPath(path, paint);
    }

    /* access modifiers changed from: private */
    public static void setNormalBoxShadow(View view, List<BoxShadowOptions> list, float f, float[] fArr) {
        float f2 = f;
        int height = view.getHeight();
        int width = view.getWidth();
        view.getLayoutParams();
        if (height == 0 || width == 0) {
            Log.w(TAG, "Target view is invisible, ignore set shadow.");
        } else if (Build.VERSION.SDK_INT >= 18) {
            int i = 0;
            int i2 = 0;
            for (BoxShadowOptions next : list) {
                next.viewWidth = width;
                next.viewHeight = height;
                next.radii = fArr;
                Rect targetCanvasRect = next.getTargetCanvasRect();
                if (i < targetCanvasRect.width()) {
                    i = targetCanvasRect.width();
                }
                if (i2 < targetCanvasRect.height()) {
                    i2 = targetCanvasRect.height();
                }
            }
            float[] fArr2 = fArr;
            Bitmap createBitmap = Bitmap.createBitmap((int) (((float) i) * f2), (int) (((float) i2) * f2), Bitmap.Config.ARGB_4444);
            if (Build.VERSION.SDK_INT >= 19) {
                WXLogUtils.d(TAG, "Allocation memory for box-shadow: " + (createBitmap.getAllocationByteCount() / 1024) + " KB");
            }
            Canvas canvas = new Canvas(createBitmap);
            for (BoxShadowOptions next2 : list) {
                Rect targetCanvasRect2 = next2.getTargetCanvasRect();
                next2.topLeft = new PointF(((float) (i - targetCanvasRect2.width())) / 2.0f, ((float) (i2 - targetCanvasRect2.height())) / 2.0f);
                drawShadow(canvas, next2.scale(f2));
            }
            OverflowBitmapDrawable overflowBitmapDrawable = new OverflowBitmapDrawable(view.getResources(), createBitmap, new Point((i - width) / 2, (i2 - height) / 2), new Rect(0, 0, width, height), fArr);
            view.getOverlay().add(overflowBitmapDrawable);
            ViewParent parent = view.getParent();
            if (parent != null) {
                parent.requestLayout();
                if (parent instanceof ViewGroup) {
                    ((ViewGroup) parent).invalidate(overflowBitmapDrawable.getBounds());
                }
            }
        } else {
            Log.w(TAG, "Call setNormalBoxShadow() requires API level 18 or higher.");
        }
    }

    /* access modifiers changed from: private */
    public static void setInsetBoxShadow(View view, List<BoxShadowOptions> list, float f, float[] fArr) {
        List<BoxShadowOptions> list2 = list;
        if (view == null || list2 == null) {
            WXLogUtils.w(TAG, "Illegal arguments");
        } else if (view.getWidth() == 0 || view.getHeight() == 0) {
            WXLogUtils.w(TAG, "Target view is invisible, ignore set shadow.");
        } else if (Build.VERSION.SDK_INT >= 18) {
            Drawable[] drawableArr = new Drawable[list.size()];
            for (int i = 0; i < list.size(); i++) {
                BoxShadowOptions boxShadowOptions = list2.get(i);
                drawableArr[i] = new InsetShadowDrawable(view.getWidth(), view.getHeight(), boxShadowOptions.hShadow, boxShadowOptions.vShadow, boxShadowOptions.blur, boxShadowOptions.spread, boxShadowOptions.color, fArr);
            }
            view.getOverlay().add(new LayerDrawable(drawableArr));
            view.invalidate();
        } else {
            Log.w(TAG, "Call setInsetBoxShadow() requires API level 18 or higher.");
        }
    }

    public static BoxShadowOptions[] parseBoxShadows(String str, float f) {
        int i;
        if (sColorPattern == null) {
            sColorPattern = Pattern.compile("([rR][gG][bB][aA]?)\\((\\d+\\s*),\\s*(\\d+\\s*),\\s*(\\d+\\s*)(?:,\\s*(\\d+(?:\\.\\d+)?))?\\)");
        }
        Matcher matcher = sColorPattern.matcher(str);
        while (true) {
            if (!matcher.find()) {
                break;
            }
            String group = matcher.group();
            str = str.replace(group, "#" + StringUtil.format("%8s", Integer.toHexString(WXResourceUtils.getColor(group, -16777216))).replaceAll("\\s", WXInstanceApm.VALUE_ERROR_CODE_DEFAULT));
        }
        String[] split = str.split(",");
        if (split == null || split.length <= 0) {
            return null;
        }
        BoxShadowOptions[] boxShadowOptionsArr = new BoxShadowOptions[split.length];
        for (i = 0; i < split.length; i++) {
            boxShadowOptionsArr[i] = parseBoxShadow(split[i], f);
        }
        return boxShadowOptionsArr;
    }

    private static BoxShadowOptions parseBoxShadow(String str, float f) {
        BoxShadowOptions boxShadowOptions = new BoxShadowOptions(f);
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String replaceAll = str.replaceAll("\\s*,\\s+", ",");
        if (replaceAll.contains("inset")) {
            boxShadowOptions.isInset = true;
            replaceAll = replaceAll.replace("inset", "");
        }
        ArrayList arrayList = new ArrayList(Arrays.asList(replaceAll.trim().split("\\s+")));
        String str2 = (String) arrayList.get(arrayList.size() - 1);
        if (!TextUtils.isEmpty(str2) && (str2.startsWith("#") || str2.startsWith("rgb") || WXResourceUtils.isNamedColor(str2))) {
            boxShadowOptions.color = WXResourceUtils.getColor(str2, -16777216);
            arrayList.remove(arrayList.size() - 1);
        }
        try {
            if (arrayList.size() < 2) {
                return null;
            }
            if (!TextUtils.isEmpty((CharSequence) arrayList.get(0))) {
                boxShadowOptions.hShadow = WXViewUtils.getRealSubPxByWidth(WXUtils.getFloat(((String) arrayList.get(0)).trim(), Float.valueOf(0.0f)).floatValue(), f);
            }
            if (!TextUtils.isEmpty((CharSequence) arrayList.get(1))) {
                boxShadowOptions.vShadow = WXViewUtils.getRealPxByWidth(WXUtils.getFloat(((String) arrayList.get(1)).trim(), Float.valueOf(0.0f)).floatValue(), f);
            }
            for (int i = 2; i < arrayList.size(); i++) {
                ((BoxShadowOptions.IParser) boxShadowOptions.optionParamParsers.get(i - 2)).parse((String) arrayList.get(i));
            }
            return boxShadowOptions;
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private static class OverflowBitmapDrawable extends BitmapDrawable {
        private int paddingX;
        private int paddingY;
        private float[] radii;
        private Rect viewRect;

        private OverflowBitmapDrawable(Resources resources, Bitmap bitmap, Point point, Rect rect, float[] fArr) {
            super(resources, bitmap);
            this.paddingX = point.x;
            int i = point.y;
            this.paddingY = i;
            this.viewRect = rect;
            this.radii = fArr;
            setBounds(-this.paddingX, -i, rect.width() + this.paddingX, rect.height() + this.paddingY);
        }

        public void draw(Canvas canvas) {
            Rect clipBounds = canvas.getClipBounds();
            Rect rect = new Rect(clipBounds);
            rect.inset((-this.paddingX) * 2, (-this.paddingY) * 2);
            try {
                if (WXEnvironment.sApplication.getApplicationInfo().targetSdkVersion > 26) {
                    canvas.clipRect(rect);
                } else {
                    canvas.clipRect(rect, Region.Op.REPLACE);
                }
            } catch (NullPointerException unused) {
                canvas.clipRect(rect);
            }
            Path path = new Path();
            path.addRoundRect(new RectF(clipBounds), this.radii, Path.Direction.CCW);
            canvas.clipPath(path, Region.Op.DIFFERENCE);
            canvas.translate((float) clipBounds.left, (float) clipBounds.top);
            super.draw(canvas);
        }
    }

    private static class InsetShadowDrawable extends Drawable {
        private static final int BOTTOM_TO_TOP = 3;
        private static final int LEFT_TO_RIGHT = 0;
        private static final int RIGHT_TO_LEFT = 2;
        private static final int TOP_TO_BOTTOM = 1;
        private float blurRadius;
        private float height;
        private Paint paint;
        private Path[] paths;
        private float[] radii;
        private Shader[] shades;
        private int shadowColor;
        private float shadowXSize;
        private float shadowYSize;
        private float width;

        public int getOpacity() {
            return -1;
        }

        public void setAlpha(int i) {
        }

        public void setColorFilter(ColorFilter colorFilter) {
        }

        private InsetShadowDrawable(int i, int i2, float f, float f2, float f3, float f4, int i3, float[] fArr) {
            this.shades = new Shader[4];
            this.paths = new Path[4];
            this.blurRadius = f3;
            this.shadowColor = i3;
            this.width = ((float) i) + (f * 2.0f);
            this.height = ((float) i2) + (2.0f * f2);
            this.shadowXSize = f + f4;
            this.shadowYSize = f2 + f4;
            this.radii = fArr;
            setBounds(0, 0, i, i2);
            prepare();
        }

        private void prepare() {
            PointF pointF = new PointF(0.0f, 0.0f);
            PointF pointF2 = new PointF(this.width, 0.0f);
            PointF pointF3 = new PointF(pointF2.x, this.height);
            PointF pointF4 = new PointF(pointF.x, pointF3.y);
            PointF pointF5 = new PointF(this.shadowXSize, this.shadowYSize);
            PointF pointF6 = new PointF(pointF2.x - this.shadowXSize, pointF5.y);
            PointF pointF7 = new PointF(pointF6.x, pointF3.y - this.shadowYSize);
            PointF pointF8 = new PointF(pointF5.x, pointF7.y);
            LinearGradient linearGradient = new LinearGradient(pointF5.x - this.blurRadius, pointF5.y, pointF5.x, pointF5.y, this.shadowColor, 0, Shader.TileMode.CLAMP);
            float f = pointF5.x;
            float f2 = pointF5.y - this.blurRadius;
            float f3 = pointF5.x;
            float f4 = pointF5.y;
            LinearGradient linearGradient2 = new LinearGradient(f, f2, f3, f4, this.shadowColor, 0, Shader.TileMode.CLAMP);
            float f5 = pointF7.x + this.blurRadius;
            float f6 = pointF7.y;
            float f7 = pointF7.x;
            float f8 = pointF7.y;
            LinearGradient linearGradient3 = new LinearGradient(f5, f6, f7, f8, this.shadowColor, 0, Shader.TileMode.CLAMP);
            float f9 = pointF7.x;
            float f10 = pointF7.y + this.blurRadius;
            float f11 = pointF7.x;
            float f12 = pointF7.y;
            PointF pointF9 = pointF7;
            LinearGradient linearGradient4 = new LinearGradient(f9, f10, f11, f12, this.shadowColor, 0, Shader.TileMode.CLAMP);
            Shader[] shaderArr = this.shades;
            shaderArr[0] = linearGradient;
            shaderArr[1] = linearGradient2;
            shaderArr[2] = linearGradient3;
            shaderArr[3] = linearGradient4;
            Path path = new Path();
            path.moveTo(pointF.x, pointF.y);
            path.lineTo(pointF5.x, pointF5.y);
            path.lineTo(pointF8.x, pointF8.y);
            path.lineTo(pointF4.x, pointF4.y);
            path.close();
            Path path2 = new Path();
            path2.moveTo(pointF.x, pointF.y);
            path2.lineTo(pointF2.x, pointF2.y);
            path2.lineTo(pointF6.x, pointF6.y);
            path2.lineTo(pointF5.x, pointF5.y);
            path2.close();
            Path path3 = new Path();
            path3.moveTo(pointF2.x, pointF2.y);
            path3.lineTo(pointF3.x, pointF3.y);
            PointF pointF10 = pointF9;
            path3.lineTo(pointF10.x, pointF10.y);
            path3.lineTo(pointF6.x, pointF6.y);
            path3.close();
            Path path4 = new Path();
            path4.moveTo(pointF4.x, pointF4.y);
            path4.lineTo(pointF3.x, pointF3.y);
            path4.lineTo(pointF10.x, pointF10.y);
            path4.lineTo(pointF8.x, pointF8.y);
            path4.close();
            Path[] pathArr = this.paths;
            pathArr[0] = path;
            pathArr[1] = path2;
            pathArr[2] = path3;
            pathArr[3] = path4;
            Paint paint2 = new Paint();
            this.paint = paint2;
            paint2.setAntiAlias(true);
            this.paint.setStyle(Paint.Style.FILL);
            this.paint.setColor(this.shadowColor);
        }

        public void draw(Canvas canvas) {
            Rect clipBounds = canvas.getClipBounds();
            Path path = new Path();
            path.addRoundRect(new RectF(clipBounds), this.radii, Path.Direction.CCW);
            canvas.clipPath(path);
            canvas.translate((float) clipBounds.left, (float) clipBounds.top);
            for (int i = 0; i < 4; i++) {
                Shader shader = this.shades[i];
                Path path2 = this.paths[i];
                this.paint.setShader(shader);
                canvas.drawPath(path2, this.paint);
            }
        }
    }

    public static class BoxShadowOptions {
        public float blur;
        public int color;
        public float hShadow;
        public boolean isInset;
        /* access modifiers changed from: private */
        public List<IParser> optionParamParsers;
        public float[] radii;
        public float spread;
        public PointF topLeft;
        public float vShadow;
        public int viewHeight;
        public int viewWidth;
        /* access modifiers changed from: private */
        public float viewport;

        private interface IParser {
            void parse(String str);
        }

        private BoxShadowOptions(float f) {
            this.viewport = 750.0f;
            this.blur = 0.0f;
            this.spread = 0.0f;
            this.radii = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
            this.color = -16777216;
            this.isInset = false;
            this.viewWidth = 0;
            this.viewHeight = 0;
            this.topLeft = null;
            if (750.0f != 0.0f) {
                this.viewport = f;
            }
            this.optionParamParsers = new ArrayList();
            AnonymousClass1 r4 = new IParser() {
                public void parse(String str) {
                    if (!TextUtils.isEmpty(str)) {
                        float floatValue = WXUtils.getFloat(str, Float.valueOf(0.0f)).floatValue();
                        BoxShadowOptions boxShadowOptions = BoxShadowOptions.this;
                        boxShadowOptions.spread = WXViewUtils.getRealSubPxByWidth(floatValue, boxShadowOptions.viewport);
                        WXLogUtils.w(BoxShadowUtil.TAG, "Experimental box-shadow attribute: spread");
                    }
                }
            };
            this.optionParamParsers.add(new IParser() {
                public void parse(String str) {
                    if (!TextUtils.isEmpty(str)) {
                        float floatValue = WXUtils.getFloat(str, Float.valueOf(0.0f)).floatValue();
                        BoxShadowOptions boxShadowOptions = BoxShadowOptions.this;
                        boxShadowOptions.blur = WXViewUtils.getRealSubPxByWidth(floatValue, boxShadowOptions.viewport);
                    }
                }
            });
            this.optionParamParsers.add(r4);
        }

        public BoxShadowOptions scale(float f) {
            if (f <= 0.0f || f > 1.0f) {
                return null;
            }
            BoxShadowOptions boxShadowOptions = new BoxShadowOptions(this.viewport);
            boxShadowOptions.hShadow = this.hShadow * f;
            boxShadowOptions.vShadow = this.vShadow * f;
            boxShadowOptions.blur = this.blur * f;
            boxShadowOptions.spread = this.spread * f;
            int i = 0;
            while (true) {
                float[] fArr = this.radii;
                if (i >= fArr.length) {
                    break;
                }
                boxShadowOptions.radii[i] = fArr[i] * f;
                i++;
            }
            boxShadowOptions.viewHeight = (int) (((float) this.viewHeight) * f);
            boxShadowOptions.viewWidth = (int) (((float) this.viewWidth) * f);
            if (this.topLeft != null) {
                PointF pointF = new PointF();
                boxShadowOptions.topLeft = pointF;
                pointF.x = this.topLeft.x * f;
                boxShadowOptions.topLeft.y = this.topLeft.y * f;
            }
            boxShadowOptions.color = this.color;
            boxShadowOptions.isInset = this.isInset;
            WXLogUtils.d(BoxShadowUtil.TAG, "Scaled BoxShadowOptions: [" + f + "] " + boxShadowOptions);
            return boxShadowOptions;
        }

        public Rect getTargetCanvasRect() {
            return new Rect(0, 0, this.viewWidth + (((int) (this.blur + this.spread + Math.abs(this.hShadow))) * 2), this.viewHeight + (((int) (this.blur + this.spread + Math.abs(this.vShadow))) * 2));
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer("BoxShadowOptions{");
            stringBuffer.append("h-shadow=");
            stringBuffer.append(this.hShadow);
            stringBuffer.append(", v-shadow=");
            stringBuffer.append(this.vShadow);
            stringBuffer.append(", blur=");
            stringBuffer.append(this.blur);
            stringBuffer.append(", spread=");
            stringBuffer.append(this.spread);
            stringBuffer.append(", corner-radius=");
            stringBuffer.append(Operators.ARRAY_START_STR + this.radii[0] + "," + this.radii[2] + "," + this.radii[4] + "," + this.radii[6] + Operators.ARRAY_END_STR);
            stringBuffer.append(", color=#");
            stringBuffer.append(Integer.toHexString(this.color));
            stringBuffer.append(", inset=");
            stringBuffer.append(this.isInset);
            stringBuffer.append(Operators.BLOCK_END);
            return stringBuffer.toString();
        }
    }
}
