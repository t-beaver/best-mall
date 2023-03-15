package io.dcloud.feature.uniapp.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXResourceUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.StringUtil;
import io.dcloud.feature.uniapp.ui.shadow.UniBoxShadowData;
import io.dcloud.feature.uniapp.ui.shadow.UniBoxShadowOptions;
import io.dcloud.feature.uniapp.ui.shadow.UniInsetBoxShadowDrawable;
import io.dcloud.feature.uniapp.ui.shadow.UniInsetBoxShadowLayer;
import io.dcloud.feature.uniapp.ui.shadow.UniNormalBoxShadowDrawable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UniBoxShadowUtil {
    private static final String TAG = "UniBoxShadowUtil";
    private static boolean sBoxShadowEnabled = true;
    private static Pattern sColorPattern;

    public static void setBoxShadowEnabled(boolean z) {
        sBoxShadowEnabled = z;
        WXLogUtils.w(TAG, "Switch box-shadow status: " + z);
    }

    public static boolean isBoxShadowEnabled() {
        return sBoxShadowEnabled;
    }

    public static UniBoxShadowData parseBoxShadow(int i, int i2, String str, float[] fArr, float f, float f2) {
        if (!sBoxShadowEnabled) {
            WXLogUtils.w(TAG, "box-shadow was disabled by config");
            return null;
        }
        UniBoxShadowOptions[] parseBoxShadows = parseBoxShadows(str, f);
        if (parseBoxShadows == null || parseBoxShadows.length == 0) {
            WXLogUtils.w(TAG, "Failed to parse box-shadow: " + str);
            return null;
        }
        ArrayList<UniBoxShadowOptions> arrayList = new ArrayList<>();
        ArrayList arrayList2 = new ArrayList();
        int i3 = 0;
        for (UniBoxShadowOptions uniBoxShadowOptions : parseBoxShadows) {
            if (uniBoxShadowOptions != null) {
                if (uniBoxShadowOptions.isInset) {
                    arrayList2.add(0, uniBoxShadowOptions);
                } else {
                    arrayList.add(0, uniBoxShadowOptions);
                }
            }
        }
        if (!(fArr == null || fArr.length == 8)) {
            WXLogUtils.w(TAG, "Length of radii must be 8");
        }
        UniBoxShadowData uniBoxShadowData = new UniBoxShadowData(arrayList, arrayList2, fArr, f2);
        uniBoxShadowData.setStyle(str);
        if (arrayList.size() > 0) {
            int i4 = 0;
            for (UniBoxShadowOptions uniBoxShadowOptions2 : arrayList) {
                uniBoxShadowOptions2.viewWidth = i;
                uniBoxShadowOptions2.viewHeight = i2;
                uniBoxShadowOptions2.radii = fArr;
                Rect targetCanvasRect = uniBoxShadowOptions2.getTargetCanvasRect();
                if (i3 < targetCanvasRect.width()) {
                    i3 = targetCanvasRect.width();
                }
                if (i4 < targetCanvasRect.height()) {
                    i4 = targetCanvasRect.height();
                }
            }
            uniBoxShadowData.setNormalMaxWidth(i3);
            uniBoxShadowData.setNormalMaxHeight(i4);
            uniBoxShadowData.setNormalLeft(i3 - i);
            uniBoxShadowData.setNormalTop(i4 - i2);
        }
        uniBoxShadowData.setContentHeight(i2);
        uniBoxShadowData.setContentWidth(i);
        return uniBoxShadowData;
    }

    public static UniBoxShadowOptions[] parseBoxShadows(String str, float f) {
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
        UniBoxShadowOptions[] uniBoxShadowOptionsArr = new UniBoxShadowOptions[split.length];
        for (i = 0; i < split.length; i++) {
            uniBoxShadowOptionsArr[i] = parseBoxShadow(split[i], f);
        }
        return uniBoxShadowOptionsArr;
    }

    private static UniBoxShadowOptions parseBoxShadow(String str, float f) {
        UniBoxShadowOptions uniBoxShadowOptions = new UniBoxShadowOptions(f);
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String replaceAll = str.replaceAll("\\s*,\\s+", ",");
        if (replaceAll.contains("inset")) {
            uniBoxShadowOptions.isInset = true;
            replaceAll = replaceAll.replace("inset", "");
        }
        ArrayList arrayList = new ArrayList(Arrays.asList(replaceAll.trim().split("\\s+")));
        String str2 = (String) arrayList.get(arrayList.size() - 1);
        if (!TextUtils.isEmpty(str2) && (str2.startsWith("#") || str2.startsWith("rgb") || WXResourceUtils.isNamedColor(str2))) {
            uniBoxShadowOptions.color = WXResourceUtils.getColor(str2, -16777216);
            arrayList.remove(arrayList.size() - 1);
        }
        try {
            if (arrayList.size() < 2) {
                return null;
            }
            if (!TextUtils.isEmpty((CharSequence) arrayList.get(0))) {
                uniBoxShadowOptions.hShadow = WXViewUtils.getRealSubPxByWidth(WXUtils.getFloat(((String) arrayList.get(0)).trim(), Float.valueOf(0.0f)).floatValue(), f);
            }
            if (!TextUtils.isEmpty((CharSequence) arrayList.get(1))) {
                uniBoxShadowOptions.vShadow = WXViewUtils.getRealPxByWidth(WXUtils.getFloat(((String) arrayList.get(1)).trim(), Float.valueOf(0.0f)).floatValue(), f);
            }
            for (int i = 2; i < arrayList.size(); i++) {
                uniBoxShadowOptions.optionParamParsers.get(i - 2).parse((String) arrayList.get(i));
            }
            return uniBoxShadowOptions;
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static UniNormalBoxShadowDrawable getNormalBoxShadowDrawable(UniBoxShadowData uniBoxShadowData, Resources resources) {
        if (uniBoxShadowData.getNormalShadows().size() <= 0) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(uniBoxShadowData.getCanvasWidth(), uniBoxShadowData.getCanvasHeight(), Bitmap.Config.ARGB_4444);
        if (Build.VERSION.SDK_INT >= 19) {
            Logger.e(TAG, "Allocation memory for box-shadow: " + (createBitmap.getAllocationByteCount() / 1024) + " KB");
        }
        Canvas canvas = new Canvas(createBitmap);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        for (UniBoxShadowOptions scale : uniBoxShadowData.getNormalShadows()) {
            drawShadow(canvas, scale.scale(uniBoxShadowData.getQuality()), (float) uniBoxShadowData.getNormalTop(), (float) uniBoxShadowData.getNormalLeft(), uniBoxShadowData.getQuality());
        }
        return new UniNormalBoxShadowDrawable(resources, createBitmap);
    }

    private static void drawShadow(Canvas canvas, UniBoxShadowOptions uniBoxShadowOptions, float f, float f2, float f3) {
        RectF rectF = new RectF(0.0f, 0.0f, ((float) uniBoxShadowOptions.viewWidth) + (uniBoxShadowOptions.spread * 2.0f), ((float) uniBoxShadowOptions.viewHeight) + (uniBoxShadowOptions.spread * 2.0f));
        rectF.offset(uniBoxShadowOptions.hShadow >= 0.0f ? uniBoxShadowOptions.blur + (uniBoxShadowOptions.hShadow * 2.0f) : 0.0f, uniBoxShadowOptions.vShadow >= 0.0f ? uniBoxShadowOptions.blur + (uniBoxShadowOptions.vShadow * 2.0f) : 0.0f);
        Path path = new Path();
        RectF rectF2 = new RectF(0.0f, 0.0f, (float) uniBoxShadowOptions.viewWidth, (float) uniBoxShadowOptions.viewHeight);
        rectF2.offset(f2 > 0.0f ? (f2 * f3) / 2.0f : 0.0f, f > 0.0f ? (f * f3) / 2.0f : 0.0f);
        rectF2.inset(1.0f, 1.0f);
        path.addRoundRect(rectF2, uniBoxShadowOptions.radii, Path.Direction.CCW);
        canvas.clipPath(path, Region.Op.DIFFERENCE);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(uniBoxShadowOptions.color);
        paint.setStyle(Paint.Style.FILL);
        if (uniBoxShadowOptions.blur > 0.0f) {
            paint.setMaskFilter(new BlurMaskFilter(uniBoxShadowOptions.blur, BlurMaskFilter.Blur.NORMAL));
        }
        float f4 = uniBoxShadowOptions.radii[0];
        if (f4 == uniBoxShadowOptions.radii[2] && f4 == uniBoxShadowOptions.radii[4] && f4 == uniBoxShadowOptions.radii[6]) {
            canvas.drawRoundRect(rectF, f4, f4, paint);
            return;
        }
        Path path2 = new Path();
        float[] fArr = new float[8];
        for (int i = 0; i < uniBoxShadowOptions.radii.length; i++) {
            if (uniBoxShadowOptions.radii[i] == 0.0f) {
                fArr[i] = 0.0f;
            } else {
                fArr[i] = uniBoxShadowOptions.radii[i] + uniBoxShadowOptions.spread;
            }
        }
        path2.addRoundRect(rectF, fArr, Path.Direction.CW);
        canvas.drawPath(path2, paint);
    }

    public static UniInsetBoxShadowLayer getInsetBoxShadowDrawable(UniBoxShadowData uniBoxShadowData) {
        if (uniBoxShadowData.getInsetShadows().size() <= 0) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= 18) {
            Drawable[] drawableArr = new Drawable[uniBoxShadowData.getInsetShadows().size()];
            for (int i = 0; i < uniBoxShadowData.getInsetShadows().size(); i++) {
                UniBoxShadowOptions uniBoxShadowOptions = uniBoxShadowData.getInsetShadows().get(i);
                drawableArr[i] = new UniInsetBoxShadowDrawable(uniBoxShadowData.getContentWidth(), uniBoxShadowData.getContentHeight(), uniBoxShadowOptions.hShadow, uniBoxShadowOptions.vShadow, uniBoxShadowOptions.blur, uniBoxShadowOptions.spread, uniBoxShadowOptions.color, uniBoxShadowData.getRadii());
            }
            return new UniInsetBoxShadowLayer(drawableArr);
        }
        WXLogUtils.w(TAG, "Call setInsetBoxShadow() requires API level 18 or higher.");
        return null;
    }
}
