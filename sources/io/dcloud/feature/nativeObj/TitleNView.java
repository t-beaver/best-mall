package io.dcloud.feature.nativeObj;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dcloud.android.widget.CapsuleLayout;
import com.dcloud.android.widget.DCProgressView;
import com.taobao.weex.common.Constants;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IEventCallback;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.ITitleNView;
import io.dcloud.common.DHInterface.IWebAppRootView;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.DHInterface.IWebviewStateListener;
import io.dcloud.common.DHInterface.IX5WebView;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.UniMPConfig;
import io.dcloud.common.ui.blur.DCBlurDraweeView;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import io.dcloud.common.util.TitleNViewUtil;
import io.dcloud.feature.internal.sdk.SDK;
import io.dcloud.feature.nativeObj.NativeView;
import io.dcloud.feature.nativeObj.data.ButtonDataItem;
import io.dcloud.feature.ui.nativeui.a;
import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TitleNView extends NativeView implements ITitleNView {
    private final String CLOSE = AbsoluteConst.EVENTS_CLOSE;
    private final String MENU = AbsoluteConst.EVENTS_MENU;
    private LinearLayout centerSearchLayout = null;
    private TextView closeBt = null;
    /* access modifiers changed from: private */
    public AtomicBoolean isSetText = new AtomicBoolean(false);
    private BadgeLinearLayout mBackButton = null;
    private BackGroundDrawable mBackGroundDrawable;
    /* access modifiers changed from: private */
    public View mBackgroudView;
    private DCBlurDraweeView mBlurDraweeView;
    public String mBlurEffect = "none";
    /* access modifiers changed from: private */
    public ArrayList<Object> mButtons = new ArrayList<>();
    private CapsuleLayout mCapsuleLayout;
    private TextView mHomeButton = null;
    protected IWebviewStateListenerImpl mIWebviewStateListenerImpl;
    private LinearLayout mLeftButtonLayout = null;
    private String mMenuButtonFontSize = "22px";
    /* access modifiers changed from: private */
    public String mMenuButtonFontWeight = "normal";
    /* access modifiers changed from: private */
    public int mMenuButtonTextColor = -16777216;
    /* access modifiers changed from: private */
    public ArrayList<ButtonDataItem> mMenuButtons;
    private Progress mProgress;
    private LinearLayout mRightButtonLayout = null;
    private View mSplitLine;
    /* access modifiers changed from: private */
    public RelativeLayout mTitleNViewLayout = null;
    private TextView mTitleView = null;
    private RelativeLayout mTitlelayout;
    public int maxButton = 2;
    private TextView menuBt = null;
    private int redDotColor;
    /* access modifiers changed from: private */
    public EditText searchInput;

    class BackGroundDrawable extends Drawable {
        private String bitmapPath = null;
        private Rect bound;
        private Paint colorPaint;
        private int height = 0;
        private Shader mBackgroundBitmap = null;
        private int mBackgroundColor = 0;
        private Paint mPaint;
        private int offset = 0;
        private String repeatType = "no-repeat";
        private int shadow5PX;
        private String shadowColor = "";
        private int shadowColorInt = 0;
        private Paint shadowPaint;

        public BackGroundDrawable() {
            this.shadow5PX = PdrUtil.convertToScreenInt("10px", TitleNView.this.mInnerWidth, 0, TitleNView.this.mCreateScale);
        }

        private Shader getShader(List<String> list, float f, float f2) {
            float[] parseGradientDirection = parseGradientDirection(list.get(0).trim(), f, f2);
            if (parseGradientDirection == null) {
                return null;
            }
            float f3 = parseGradientDirection[0];
            float f4 = parseGradientDirection[1];
            float f5 = parseGradientDirection[2];
            return new LinearGradient(f3, f4, f5, parseGradientDirection[3], PdrUtil.stringToColor(list.get(1).trim()), PdrUtil.stringToColor(list.get(2).trim()), Shader.TileMode.CLAMP);
        }

        private Paint getShadowPaint() {
            if (this.shadowPaint == null) {
                this.shadowPaint = new Paint();
            }
            return this.shadowPaint;
        }

        /* JADX WARNING: Can't fix incorrect switch cases order */
        /* JADX WARNING: Code restructure failed: missing block: B:10:0x003f, code lost:
            if (r9.equals("totop") == false) goto L_0x002c;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private float[] parseGradientDirection(java.lang.String r9, float r10, float r11) {
            /*
                r8 = this;
                r0 = 4
                float[] r1 = new float[r0]
                r1 = {0, 0, 0, 0} // fill-array
                boolean r2 = android.text.TextUtils.isEmpty(r9)
                if (r2 != 0) goto L_0x001a
                java.lang.String r2 = "\\s*"
                java.lang.String r3 = ""
                java.lang.String r9 = r9.replaceAll(r2, r3)
                java.util.Locale r2 = java.util.Locale.ENGLISH
                java.lang.String r9 = r9.toLowerCase(r2)
            L_0x001a:
                r9.hashCode()
                r9.hashCode()
                r2 = -1
                int r3 = r9.hashCode()
                r4 = 3
                r5 = 2
                r6 = 1
                r7 = 0
                switch(r3) {
                    case -1352032154: goto L_0x0063;
                    case -1137407871: goto L_0x0058;
                    case -868157182: goto L_0x004d;
                    case -172068863: goto L_0x0042;
                    case 110550266: goto L_0x0039;
                    case 1176531318: goto L_0x002e;
                    default: goto L_0x002c;
                }
            L_0x002c:
                r0 = -1
                goto L_0x006d
            L_0x002e:
                java.lang.String r0 = "tobottomright"
                boolean r9 = r9.equals(r0)
                if (r9 != 0) goto L_0x0037
                goto L_0x002c
            L_0x0037:
                r0 = 5
                goto L_0x006d
            L_0x0039:
                java.lang.String r3 = "totop"
                boolean r9 = r9.equals(r3)
                if (r9 != 0) goto L_0x006d
                goto L_0x002c
            L_0x0042:
                java.lang.String r0 = "totopleft"
                boolean r9 = r9.equals(r0)
                if (r9 != 0) goto L_0x004b
                goto L_0x002c
            L_0x004b:
                r0 = 3
                goto L_0x006d
            L_0x004d:
                java.lang.String r0 = "toleft"
                boolean r9 = r9.equals(r0)
                if (r9 != 0) goto L_0x0056
                goto L_0x002c
            L_0x0056:
                r0 = 2
                goto L_0x006d
            L_0x0058:
                java.lang.String r0 = "toright"
                boolean r9 = r9.equals(r0)
                if (r9 != 0) goto L_0x0061
                goto L_0x002c
            L_0x0061:
                r0 = 1
                goto L_0x006d
            L_0x0063:
                java.lang.String r0 = "tobottom"
                boolean r9 = r9.equals(r0)
                if (r9 != 0) goto L_0x006c
                goto L_0x002c
            L_0x006c:
                r0 = 0
            L_0x006d:
                switch(r0) {
                    case 0: goto L_0x0085;
                    case 1: goto L_0x0082;
                    case 2: goto L_0x007f;
                    case 3: goto L_0x007a;
                    case 4: goto L_0x0077;
                    case 5: goto L_0x0072;
                    default: goto L_0x0070;
                }
            L_0x0070:
                r9 = 0
                return r9
            L_0x0072:
                r1[r5] = r10
                r1[r4] = r11
                goto L_0x0087
            L_0x0077:
                r1[r6] = r11
                goto L_0x0087
            L_0x007a:
                r1[r7] = r10
                r1[r6] = r11
                goto L_0x0087
            L_0x007f:
                r1[r7] = r10
                goto L_0x0087
            L_0x0082:
                r1[r5] = r10
                goto L_0x0087
            L_0x0085:
                r1[r4] = r11
            L_0x0087:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.nativeObj.TitleNView.BackGroundDrawable.parseGradientDirection(java.lang.String, float, float):float[]");
        }

        private List<String> parseGradientValues(String str) {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            str.trim();
            try {
                if (str.startsWith("linear-gradient")) {
                    StringTokenizer stringTokenizer = new StringTokenizer(str.substring(str.indexOf(Operators.BRACKET_START_STR) + 1, str.lastIndexOf(Operators.BRACKET_END_STR)), ",");
                    ArrayList arrayList = new ArrayList();
                    while (true) {
                        String str2 = null;
                        while (stringTokenizer.hasMoreTokens()) {
                            String nextToken = stringTokenizer.nextToken();
                            if (nextToken.contains(Operators.BRACKET_START_STR)) {
                                str2 = nextToken + ",";
                            } else if (nextToken.contains(Operators.BRACKET_END_STR)) {
                                arrayList.add(str2 + nextToken);
                            } else if (str2 != null) {
                                str2 = str2 + nextToken + ",";
                            } else {
                                arrayList.add(nextToken);
                            }
                        }
                        return arrayList;
                    }
                }
            } catch (Exception unused) {
            }
            return null;
        }

        private Bitmap scaleBitmap(Bitmap bitmap, int i, int i2) {
            if (bitmap == null) {
                return null;
            }
            if (this.repeatType.equals("repeat")) {
                return bitmap;
            }
            int height2 = bitmap.getHeight();
            int width = bitmap.getWidth();
            float f = ((float) i) / ((float) width);
            float f2 = ((float) i2) / ((float) height2);
            Matrix matrix = new Matrix();
            if (this.repeatType.equals("repeat-x")) {
                matrix.preScale(1.0f, f2);
            } else if (this.repeatType.equals("repeat-y")) {
                matrix.preScale(f, 1.0f);
            } else {
                matrix.preScale(f, f2);
            }
            Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height2, matrix, false);
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            return createBitmap;
        }

        public void draw(Canvas canvas) {
            updatebound();
            if (this.mBackgroundBitmap != null) {
                getPaint().setShader(this.mBackgroundBitmap);
                getBackgroundColorPaint().setColor(Color.argb(getBackgroundColorPaint().getAlpha(), 255, 255, 255));
                canvas.drawRect(this.bound, getBackgroundColorPaint());
            } else {
                String str = this.bitmapPath;
                if (str != null) {
                    setBackgroundImage(str);
                    this.bitmapPath = null;
                    getPaint().setShader(this.mBackgroundBitmap);
                    getBackgroundColorPaint().setColor(Color.argb(getBackgroundColorPaint().getAlpha(), 255, 255, 255));
                    canvas.drawRect(this.bound, getBackgroundColorPaint());
                } else {
                    getPaint().setColor(this.mBackgroundColor);
                }
            }
            canvas.drawRect(this.bound, getPaint());
            if (!PdrUtil.isEmpty(this.shadowColor)) {
                Rect rect = this.bound;
                int i = rect.left;
                int i2 = rect.bottom;
                int i3 = rect.right;
                int i4 = this.height;
                if (i4 == 0) {
                    i4 = this.shadow5PX;
                }
                Rect rect2 = new Rect(i, i2, i3, i4 + i2 + this.offset);
                Paint shadowPaint2 = getShadowPaint();
                int i5 = rect2.top;
                int i6 = this.offset;
                shadowPaint2.setShader(new LinearGradient(0.0f, (float) (i5 - i6), 0.0f, (float) (rect2.bottom - i6), this.shadowColorInt, 0, Shader.TileMode.CLAMP));
                canvas.drawRect(rect2, getShadowPaint());
            }
            if (this.mBackgroundBitmap != null) {
                getPaint().setShader((Shader) null);
            }
        }

        public Paint getBackgroundColorPaint() {
            if (this.colorPaint == null) {
                this.colorPaint = new Paint();
            }
            return this.colorPaint;
        }

        public int getOpacity() {
            return 0;
        }

        public Paint getPaint() {
            if (this.mPaint == null) {
                this.mPaint = new Paint(1);
            }
            return this.mPaint;
        }

        public void setAlpha(int i) {
            if (this.mBackgroundBitmap != null || this.bitmapPath != null) {
                getPaint().setAlpha(i);
                getBackgroundColorPaint().setAlpha(i);
            }
        }

        public void setBackgroundColor(int i) {
            if (this.mBackgroundBitmap != null) {
                setAlpha(Color.alpha(i));
            }
            this.mBackgroundColor = i;
            getShadowPaint().setAlpha(Color.alpha(i));
            invalidateSelf();
        }

        public void setBackgroundImage(String str) {
            Bitmap bitmap;
            Bitmap bitmap2;
            Rect rect = this.bound;
            if (rect == null || rect.width() == 0) {
                this.bitmapPath = str;
            } else if (str != null) {
                List<String> parseGradientValues = parseGradientValues(str);
                if (parseGradientValues == null || parseGradientValues.size() != 3) {
                    String convert2AbsFullPath = TitleNView.this.mWebView.obtainApp().convert2AbsFullPath(TitleNView.this.mWebView.obtainFullUrl(), str);
                    if (!PdrUtil.isDeviceRootDir(convert2AbsFullPath)) {
                        try {
                            bitmap = BitmapFactory.decodeStream(TitleNView.this.mWebView.getContext().getAssets().open(convert2AbsFullPath));
                        } catch (Exception unused) {
                            bitmap = null;
                        }
                    } else {
                        bitmap = BitmapFactory.decodeFile(convert2AbsFullPath);
                    }
                    if (bitmap == null) {
                        this.mBackgroundBitmap = null;
                        this.bitmapPath = null;
                        invalidateSelf();
                        return;
                    }
                    byte[] ninePatchChunk = bitmap.getNinePatchChunk();
                    if (ninePatchChunk == null || !NinePatch.isNinePatchChunk(ninePatchChunk)) {
                        bitmap2 = scaleBitmap(bitmap, this.bound.width(), this.bound.height());
                    } else {
                        NinePatchDrawable ninePatchDrawable = new NinePatchDrawable(bitmap, ninePatchChunk, new Rect(), (String) null);
                        bitmap2 = Bitmap.createBitmap(this.bound.width(), this.bound.height(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap2);
                        ninePatchDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        ninePatchDrawable.draw(canvas);
                    }
                    if (bitmap2 == null) {
                        this.mBackgroundBitmap = null;
                    } else {
                        Shader.TileMode tileMode = Shader.TileMode.REPEAT;
                        this.mBackgroundBitmap = new BitmapShader(bitmap2, tileMode, tileMode);
                    }
                } else {
                    this.mBackgroundBitmap = getShader(parseGradientValues, (float) this.bound.width(), (float) this.bound.height());
                }
                invalidateSelf();
            }
        }

        public void setColorFilter(ColorFilter colorFilter) {
        }

        public void setRepeatType(String str, String str2) {
            if (!PdrUtil.isEmpty(str) && !this.repeatType.equals(str)) {
                this.repeatType = str;
                this.mBackgroundBitmap = null;
                updatebound();
                setBackgroundImage(str2);
            }
        }

        public void setShadowColor(JSONObject jSONObject) {
            String optString = jSONObject.has("color") ? jSONObject.optString("color") : "";
            String optString2 = jSONObject.has("height") ? jSONObject.optString("height") : "10px";
            String optString3 = jSONObject.has("offset") ? jSONObject.optString("offset") : "0px";
            TitleNView titleNView = TitleNView.this;
            this.height = PdrUtil.convertToScreenInt(optString2, titleNView.mInnerWidth, 0, titleNView.mCreateScale);
            TitleNView titleNView2 = TitleNView.this;
            this.offset = PdrUtil.convertToScreenInt(optString3, titleNView2.mInnerWidth, 0, titleNView2.mCreateScale);
            if (!optString.equals(this.shadowColor)) {
                this.shadowColor = optString;
                this.shadowColorInt = PdrUtil.stringToColor(optString);
                invalidateSelf();
            }
        }

        public void updatebound() {
            View view;
            if (this.bound == null) {
                this.bound = new Rect();
            }
            TitleNView titleNView = TitleNView.this;
            if (!titleNView.isImmersed || !titleNView.isStatusBarHas()) {
                this.bound.top = 0;
            } else {
                this.bound.top = DeviceInfo.sStatusBarHeight;
            }
            TitleNView titleNView2 = TitleNView.this;
            if (titleNView2.isStatusBar && (view = titleNView2.mStatusbarView) != null && view.getVisibility() == 0) {
                this.bound.top = DeviceInfo.sStatusBarHeight;
            }
            Rect rect = this.bound;
            TitleNView titleNView3 = TitleNView.this;
            int i = titleNView3.mInnerHeight;
            rect.bottom = rect.top + i;
            if (titleNView3.isStatusBar && titleNView3.isImmersed) {
                rect.bottom = i + DeviceInfo.sStatusBarHeight;
            }
            rect.left = 0;
            rect.right = getBounds().right;
        }
    }

    static class IWebviewStateListenerImpl implements IWebviewStateListener {
        private SoftReference<Progress> mProgress;

        public IWebviewStateListenerImpl(Progress progress) {
            this.mProgress = new SoftReference<>(progress);
        }

        public Object onCallBack(int i, Object obj) {
            if (this.mProgress.get() == null || this.mProgress.get().getParent() == null || this.mProgress.get().getVisibility() != 0) {
                return null;
            }
            if (i == 3) {
                this.mProgress.get().updateProgress(((Integer) obj).intValue());
                return null;
            } else if ((i != 1 && i != 5) || this.mProgress.get() == null || this.mProgress.get().isFinish()) {
                return null;
            } else {
                this.mProgress.get().finishProgress();
                return null;
            }
        }
    }

    static class Progress extends DCProgressView {
        Progress(Context context) {
            super(context);
        }
    }

    private class SearchInputDrawable extends Drawable {
        private Paint mPaint;
        private int radius;

        public SearchInputDrawable(int i, int i2) {
            Paint paint = new Paint();
            this.mPaint = paint;
            paint.setAntiAlias(true);
            this.mPaint.setColor(i);
            this.radius = i2;
        }

        public void draw(Canvas canvas) {
            RectF rectF = new RectF(getBounds());
            float f = (float) this.radius;
            canvas.drawRoundRect(rectF, f, f, this.mPaint);
        }

        public int getAlpha() {
            return this.mPaint.getAlpha();
        }

        public int getDrawableColor() {
            Paint paint = this.mPaint;
            if (paint != null) {
                return paint.getColor();
            }
            return 0;
        }

        public int getOpacity() {
            return -3;
        }

        public void setAlpha(int i) {
            this.mPaint.setAlpha(i);
            invalidateSelf();
        }

        public void setColorFilter(ColorFilter colorFilter) {
            this.mPaint.setColorFilter(colorFilter);
            invalidateSelf();
        }

        public void setDrawableColor(int i) {
            Paint paint = this.mPaint;
            if (paint != null) {
                paint.setColor(i);
            }
            invalidateSelf();
        }
    }

    public TitleNView(Context context, IWebview iWebview, String str, String str2, JSONObject jSONObject) {
        super(context, iWebview, str, str2, jSONObject);
        setTag("titleNView");
        if (SDK.isUniMPSDK()) {
            initCapsuleLayout();
        }
    }

    private void addButtonOnClickListener(final String str, final IWebview iWebview, View view) {
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TitleNView.this.buttonOnclick(str, iWebview);
            }
        });
    }

    private TextView addSelect(ViewGroup viewGroup, TextView textView, int i) {
        TextView textView2 = new TextView(getContext());
        textView2.setText("");
        textView2.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/dcloud_iconfont.ttf"));
        textView2.setSingleLine();
        textView2.setLines(1);
        textView2.setGravity(17);
        textView2.setIncludeFontPadding(false);
        textView2.getPaint().setTextSize((float) PdrUtil.convertToScreenInt("15px", this.mInnerWidth, i, this.mCreateScale));
        viewGroup.addView(textView2, new LinearLayout.LayoutParams(-2, -1));
        return textView2;
    }

    /* access modifiers changed from: private */
    public void buttonOnclick(String str, IWebview iWebview) {
        if (!TextUtils.isEmpty(str)) {
            if (str.toLowerCase(Locale.ENGLISH).startsWith(AbsoluteConst.PROTOCOL_JAVASCRIPT)) {
                IFrameView iFrameView = this.mFrameViewParent;
                if (!(iFrameView == null || iFrameView.obtainWindowMgr() == null || this.mFrameViewParent.obtainApp() == null)) {
                    Object processEvent = this.mFrameViewParent.obtainWindowMgr().processEvent(IMgr.MgrType.WindowMgr, 47, this.mFrameViewParent.obtainApp().obtainAppId());
                    if (processEvent instanceof IFrameView) {
                        IFrameView iFrameView2 = (IFrameView) processEvent;
                        if (!(iFrameView2.obtainWebView() == null || iFrameView2.obtainWebView().obtainWindowView() == null)) {
                            iFrameView2.obtainWebView().loadUrl(str);
                            return;
                        }
                    }
                }
                IWebview iWebview2 = this.mWebView;
                if (iWebview2 != null && iWebview2.obtainWindowView() != null) {
                    this.mWebView.loadUrl(str);
                    return;
                } else if (!(iWebview == null || iWebview.obtainWindowView() == null)) {
                    iWebview.loadUrl(str);
                    return;
                }
            }
            if (iWebview != null) {
                Deprecated_JSUtil.execCallback(iWebview, str, "", JSUtil.OK, false, true);
                if (iWebview.getOpener() != null) {
                    Deprecated_JSUtil.execCallback(iWebview.getOpener(), str, "", JSUtil.OK, false, true);
                }
            }
        }
    }

    private void caculateTitleMargin() {
        RelativeLayout relativeLayout = this.mTitlelayout;
        if (relativeLayout != null && relativeLayout.getVisibility() == 0) {
            int convertToScreenInt = PdrUtil.convertToScreenInt("10px", this.mInnerWidth, 0, this.mCreateScale);
            initLeftButtonLayout();
            initRightButtonLayout();
            int width = this.mLeftButtonLayout.getWidth();
            int width2 = this.mRightButtonLayout.getWidth();
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mTitlelayout.getLayoutParams();
            int i = convertToScreenInt / 2;
            int i2 = this.mInnerHeight - i;
            float f = 0.0f;
            TextView textView = this.mTitleView;
            if (textView != null) {
                f = textView.getPaint().measureText("张磊 ");
            }
            int i3 = layoutParams.rightMargin;
            int min = ((int) Math.min((((float) (this.mTitleNViewLayout.getWidth() - i2)) - f) / 2.0f, (float) Math.max(width, width2))) + i;
            if (i3 != min) {
                int[] rules = layoutParams.getRules();
                if (rules == null || rules.length <= 0 || rules[1] <= 0) {
                    layoutParams.rightMargin = min;
                    layoutParams.leftMargin = min;
                } else {
                    layoutParams.rightMargin = min;
                }
                this.mTitlelayout.setLayoutParams(layoutParams);
                this.mTitlelayout.bringToFront();
            }
        }
    }

    /* access modifiers changed from: private */
    public void capsuleButtonClick(String str) {
        IWebview iWebview = this.mWebView;
        if (iWebview != null && iWebview.obtainFrameView() != null) {
            str.hashCode();
            if (str.equals(AbsoluteConst.EVENTS_MENU)) {
                Bundle bundle = new Bundle();
                bundle.putString("appid", this.mWebView.obtainApp().obtainAppId());
                bundle.putString("type", AbsoluteConst.EVENTS_MENU);
                IWebview iWebview2 = this.mWebView;
                iWebview2.obtainFrameView().obtainWindowMgr().processEvent(IMgr.MgrType.WindowMgr, 80, new Object[]{iWebview2, bundle});
                if (!UniMPConfig.isCapsuleMenuIntercept) {
                    showCapsuleMenu(getMenuArray());
                }
            } else if (str.equals(AbsoluteConst.EVENTS_CLOSE)) {
                Bundle bundle2 = new Bundle();
                bundle2.putString("appid", this.mWebView.obtainApp().obtainAppId());
                bundle2.putString("type", AbsoluteConst.EVENTS_CLOSE);
                IWebview iWebview3 = this.mWebView;
                Object[] objArr = {iWebview3, bundle2};
                AbsMgr obtainWindowMgr = iWebview3.obtainFrameView().obtainWindowMgr();
                IMgr.MgrType mgrType = IMgr.MgrType.WindowMgr;
                obtainWindowMgr.processEvent(mgrType, 80, objArr);
                if (!UniMPConfig.isCapsuleCloseIntercept) {
                    this.mWebView.obtainFrameView().obtainWindowMgr().processEvent(mgrType, 20, this.mWebView.obtainApp().obtainAppId());
                }
            } else {
                throw new IllegalStateException("Unexpected value: " + str);
            }
        }
    }

    private View createButton(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, IWebview iWebview, String str9, String str10, boolean z, String str11, boolean z2, String str12, String str13) {
        int stringToColor;
        int stringToColor2;
        int i;
        int convertToScreenInt;
        String str14 = str9;
        BadgeRelateiveLayout badgeRelateiveLayout = new BadgeRelateiveLayout(this, getContext(), this.mCreateScale, this.redDotColor);
        int i2 = 0;
        badgeRelateiveLayout.setOrientation(0);
        badgeRelateiveLayout.setGravity(17);
        badgeRelateiveLayout.setPadding(0, 0, 0, 0);
        TextView textView = new TextView(getContext());
        textView.setGravity(17);
        StringBuilder sb = new StringBuilder();
        sb.append("TitleNView.Button.");
        sb.append(str == null ? "" : str);
        textView.setTag(sb.toString());
        textView.setId(View.generateViewId());
        textView.setSingleLine();
        textView.setLines(1);
        textView.setIncludeFontPadding(false);
        if (!PdrUtil.isEmpty(str12) && (convertToScreenInt = PdrUtil.convertToScreenInt(str12, this.mAppScreenWidth, 0, this.mCreateScale)) > 0) {
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setMaxWidth(convertToScreenInt);
        }
        try {
            stringToColor = Color.parseColor(str2);
        } catch (Exception unused) {
            stringToColor = PdrUtil.stringToColor(str2);
        }
        int i3 = stringToColor;
        try {
            stringToColor2 = Color.parseColor(str3);
        } catch (Exception unused2) {
            stringToColor2 = PdrUtil.stringToColor(str3);
        }
        int i4 = stringToColor2;
        textView.setTextColor(createColorStateList(i3, i4));
        if (Constants.Value.BOLD.equals(str4)) {
            textView.getPaint().setFakeBoldText(true);
        }
        setTextAndFont(str, str6, str7, textView, TextUtils.isEmpty(str7));
        int convertToScreenInt2 = PdrUtil.convertToScreenInt(TextUtils.isEmpty(str5) ? "transparent".equals(str14) ? "22px" : "27px" : "17px", this.mInnerWidth, 0, this.mCreateScale);
        textView.getPaint().setTextSize((float) PdrUtil.convertToScreenInt(str5, this.mInnerWidth, convertToScreenInt2, this.mCreateScale));
        try {
            i = Color.parseColor(str10);
        } catch (Exception unused3) {
            i = PdrUtil.stringToColor(str10);
        }
        if ("transparent".equals(str14)) {
            i2 = i;
        }
        NativeViewBackButtonDrawable nativeViewBackButtonDrawable = new NativeViewBackButtonDrawable(i2);
        nativeViewBackButtonDrawable.setWidth(str13);
        badgeRelateiveLayout.addView(textView, new LinearLayout.LayoutParams(-2, -1, 1.0f));
        if (z2) {
            addSelect(badgeRelateiveLayout, textView, convertToScreenInt2).setTextColor(createColorStateList(i3, i4));
            textView.setEllipsize(TextUtils.TruncateAt.END);
        }
        badgeRelateiveLayout.setBackground(nativeViewBackButtonDrawable);
        addButtonOnClickListener(str8, iWebview, badgeRelateiveLayout);
        badgeRelateiveLayout.setBadgeStr(str11);
        badgeRelateiveLayout.setDrawRedDot(z);
        this.mButtons.add(badgeRelateiveLayout);
        return badgeRelateiveLayout;
    }

    private TextView getCapsuleButton(String str) {
        TextView textView = new TextView(getContext());
        textView.setGravity(17);
        textView.setId(View.generateViewId());
        textView.setSingleLine();
        textView.setLines(1);
        textView.setIncludeFontPadding(false);
        textView.getPaint().setFakeBoldText(true);
        textView.getPaint().setTextSize((float) PdrUtil.convertToScreenInt("19px", this.mInnerWidth, 0, this.mCreateScale));
        Typeface createFromAsset = Typeface.createFromAsset(getContext().getAssets(), "fonts/dcloud_iconfont.ttf");
        if (createFromAsset != null) {
            textView.setTypeface(createFromAsset);
        }
        return textView;
    }

    private String getIconPath(String str) {
        if (PdrUtil.isNetPath(str)) {
            return str;
        }
        String convert2AbsFullPath = this.mApp.convert2AbsFullPath(this.mWebView.obtainFullUrl(), str);
        if (convert2AbsFullPath == null || !PdrUtil.isDeviceRootDir(convert2AbsFullPath)) {
            if (convert2AbsFullPath != null && convert2AbsFullPath.startsWith("/") && convert2AbsFullPath.length() > 1) {
                convert2AbsFullPath = convert2AbsFullPath.substring(1);
            }
            return SDK.ANDROID_ASSET + convert2AbsFullPath;
        }
        return DeviceInfo.FILE_PROTOCOL + convert2AbsFullPath;
    }

    private JSONArray getMenuArray() {
        JSONObject jSONObject;
        JSONArray jSONArray = new JSONArray();
        for (int i = 0; i < this.mMenuButtons.size(); i++) {
            ButtonDataItem buttonDataItem = this.mMenuButtons.get(i);
            JSONObject jSONObject2 = new JSONObject();
            try {
                jSONObject2.put(AbsoluteConst.JSON_KEY_TITLE, TextUtils.isEmpty(buttonDataItem.getTitle()) ? buttonDataItem.getText() : buttonDataItem.getTitle());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jSONArray.put(jSONObject2);
        }
        if (this.mMenuButtons.size() > 0) {
            JSONObject jSONObject3 = new JSONObject();
            try {
                jSONObject3.put(AbsoluteConst.JSON_KEY_TITLE, "");
                jSONObject3.put("type", "interval");
                jSONArray.put(jSONObject3);
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        if (SDK.sDefaultMenuButton != null) {
            try {
                JSONObject jSONObject4 = new JSONObject(SDK.sDefaultMenuButton);
                if (jSONObject4.has(AbsoluteConst.EVENTS_MENU) && (jSONObject = jSONObject4.getJSONObject(AbsoluteConst.EVENTS_MENU)) != null && jSONObject.has("buttons")) {
                    JSONArray jSONArray2 = jSONObject.getJSONArray("buttons");
                    for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                        jSONArray.put(jSONArray2.getJSONObject(i2));
                    }
                }
            } catch (JSONException e3) {
                e3.printStackTrace();
            }
        }
        return jSONArray;
    }

    private void initCapsuleLayout() {
        JSONObject jSONObject;
        if (this.mCapsuleLayout == null && SDK.isCapsule) {
            this.mMenuButtons = new ArrayList<>();
            int convertToScreenInt = this.mInnerHeight - PdrUtil.convertToScreenInt("12px", this.mAppScreenWidth, 0, this.mCreateScale);
            CapsuleLayout capsuleLayout = new CapsuleLayout(getContext(), (float) (convertToScreenInt / 2));
            this.mCapsuleLayout = capsuleLayout;
            capsuleLayout.setId(View.generateViewId());
            this.mCapsuleLayout.setGravity(16);
            this.mCapsuleLayout.setOrientation(0);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, convertToScreenInt);
            layoutParams.addRule(15);
            layoutParams.addRule(11);
            float f = this.mCreateScale;
            int i = (int) (13.0f * f);
            int i2 = (int) (f * 10.0f);
            layoutParams.rightMargin = i;
            initTitleNViewLayout();
            this.mTitleNViewLayout.addView(this.mCapsuleLayout, layoutParams);
            if (this.menuBt == null) {
                TextView capsuleButton = getCapsuleButton(AbsoluteConst.EVENTS_MENU);
                this.menuBt = capsuleButton;
                capsuleButton.setText("");
            }
            this.menuBt.setTextColor(-16777216);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
            layoutParams2.leftMargin = i;
            layoutParams2.gravity = 17;
            layoutParams2.rightMargin = i2;
            this.mCapsuleLayout.addButtonView(this.menuBt, layoutParams2, CapsuleLayout.ButtonType.LIFT, new View.OnClickListener() {
                public void onClick(View view) {
                    TitleNView.this.capsuleButtonClick(AbsoluteConst.EVENTS_MENU);
                }
            });
            this.mCapsuleLayout.addIntervalView(this.mCreateScale);
            if (this.closeBt == null) {
                TextView capsuleButton2 = getCapsuleButton(AbsoluteConst.EVENTS_CLOSE);
                this.closeBt = capsuleButton2;
                capsuleButton2.setText("");
            }
            this.closeBt.setTextColor(-16777216);
            LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-2, -2);
            layoutParams3.rightMargin = i;
            layoutParams3.gravity = 17;
            layoutParams3.leftMargin = i2;
            this.mCapsuleLayout.addButtonView(this.closeBt, layoutParams3, CapsuleLayout.ButtonType.RIGHT, new View.OnClickListener() {
                public void onClick(View view) {
                    TitleNView.this.capsuleButtonClick(AbsoluteConst.EVENTS_CLOSE);
                }
            });
            if (!TextUtils.isEmpty(SDK.sDefaultMenuButton)) {
                try {
                    JSONObject jSONObject2 = new JSONObject(SDK.sDefaultMenuButton);
                    if (jSONObject2.has(AbsoluteConst.EVENTS_MENU) && (jSONObject = jSONObject2.getJSONObject(AbsoluteConst.EVENTS_MENU)) != null) {
                        if (jSONObject.has("textColor")) {
                            this.mMenuButtonTextColor = PdrUtil.stringToColor(jSONObject.getString("textColor"));
                        }
                        if (jSONObject.has(Constants.Name.FONT_SIZE) && !TextUtils.isEmpty(jSONObject.getString(Constants.Name.FONT_SIZE))) {
                            this.mMenuButtonFontSize = jSONObject.getString(Constants.Name.FONT_SIZE);
                        }
                        if (jSONObject.has(Constants.Name.FONT_WEIGHT)) {
                            if (Constants.Value.BOLD.equals(jSONObject.getString(Constants.Name.FONT_WEIGHT))) {
                                this.mMenuButtonFontWeight = Constants.Value.BOLD;
                            } else {
                                this.mMenuButtonFontWeight = "normal";
                            }
                        }
                    }
                    if (jSONObject2.has("capsuleButtonStyle")) {
                        setCapsuleButtonStyle(jSONObject2.getJSONObject("capsuleButtonStyle"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initCenterSearchLayout() {
        if (this.centerSearchLayout == null) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            this.centerSearchLayout = linearLayout;
            linearLayout.setId(View.generateViewId());
            this.centerSearchLayout.setGravity(16);
            this.centerSearchLayout.setOrientation(0);
            this.centerSearchLayout.setLayoutParams(new ViewGroup.LayoutParams(-2, -1));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, this.mInnerHeight - PdrUtil.convertToScreenInt("12px", this.mAppScreenWidth, 0, this.mCreateScale));
            LinearLayout linearLayout2 = this.mRightButtonLayout;
            if (linearLayout2 != null) {
                layoutParams.addRule(0, linearLayout2.getId());
            } else {
                layoutParams.addRule(11);
            }
            LinearLayout linearLayout3 = this.mLeftButtonLayout;
            if (linearLayout3 != null) {
                layoutParams.addRule(1, linearLayout3.getId());
            } else {
                layoutParams.addRule(9);
            }
            layoutParams.addRule(15);
            layoutParams.rightMargin = PdrUtil.convertToScreenInt("5px", this.mAppScreenWidth, 0, this.mCreateScale);
            layoutParams.leftMargin = PdrUtil.convertToScreenInt("5px", this.mAppScreenWidth, 0, this.mCreateScale);
            this.mTitleNViewLayout.addView(this.centerSearchLayout, layoutParams);
        }
    }

    private void initLeftButtonLayout() {
        if (this.mLeftButtonLayout == null) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            this.mLeftButtonLayout = linearLayout;
            linearLayout.setId(View.generateViewId());
            this.mLeftButtonLayout.setGravity(16);
            this.mLeftButtonLayout.setOrientation(0);
            this.mLeftButtonLayout.setLayoutParams(new ViewGroup.LayoutParams(-2, -1));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -1);
            layoutParams.addRule(15);
            layoutParams.addRule(9);
            this.mTitleNViewLayout.addView(this.mLeftButtonLayout, layoutParams);
        }
    }

    private void initRightButtonLayout() {
        if (this.mRightButtonLayout == null) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            this.mRightButtonLayout = linearLayout;
            linearLayout.setId(View.generateViewId());
            this.mRightButtonLayout.setGravity(16);
            this.mRightButtonLayout.setOrientation(0);
            this.mRightButtonLayout.setLayoutParams(new ViewGroup.LayoutParams(-2, -1));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -1);
            layoutParams.addRule(15);
            CapsuleLayout capsuleLayout = this.mCapsuleLayout;
            if (capsuleLayout != null) {
                layoutParams.addRule(0, capsuleLayout.getId());
            } else {
                layoutParams.addRule(11);
            }
            this.mTitleNViewLayout.addView(this.mRightButtonLayout, layoutParams);
        }
    }

    private void initTitleNViewLayout() {
        boolean z;
        IFrameView iFrameView;
        if (this.mTitleNViewLayout == null) {
            this.mTitleNViewLayout = new RelativeLayout(getContext());
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, this.mInnerHeight);
            ViewParent parent = getParent();
            int i = 0;
            if (!TextUtils.equals(this.mStyle.optString("position", AbsoluteConst.JSON_VALUE_POSITION_ABSOLUTE), AbsoluteConst.JSON_VALUE_POSITION_ABSOLUTE) && parent != null && (parent instanceof ViewGroup)) {
                int i2 = 0;
                while (true) {
                    ViewGroup viewGroup = (ViewGroup) parent;
                    if (i2 >= viewGroup.getChildCount()) {
                        break;
                    }
                    Object tag = viewGroup.getChildAt(i2).getTag();
                    if (tag != null && "StatusBar".equalsIgnoreCase(tag.toString())) {
                        z = true;
                        break;
                    }
                    i2++;
                }
            }
            z = false;
            if (!z && (iFrameView = this.mFrameViewParent) != null && iFrameView.obtainApp().obtainStatusBarMgr().isImmersive) {
                i = 0 + DeviceInfo.sStatusBarHeight;
            }
            layoutParams.topMargin = i;
            addView(this.mTitleNViewLayout, layoutParams);
        }
    }

    public static boolean isBase64Image(String str) {
        return Pattern.compile("^data:image/.*;base64,").matcher(str).find();
    }

    /* access modifiers changed from: private */
    public boolean isStatusBarHas() {
        IWebview iWebview = this.mWebView;
        if (iWebview == null || iWebview.obtainFrameView() == null) {
            return false;
        }
        return ((AdaFrameItem) this.mWebView.obtainFrameView()).obtainFrameOptions().isStatusbar;
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void layoutSubtitleIcon(java.lang.String r18, java.lang.String r19, java.lang.String r20, java.lang.String r21, java.lang.String r22, java.lang.String r23, android.widget.ImageView r24, android.widget.TextView r25, int r26, java.lang.String r27, java.lang.String r28) {
        /*
            r17 = this;
            r0 = r17
            r1 = r18
            r2 = r24
            r3 = r25
            r4 = r27
            int r5 = r0.mInnerWidth
            float r6 = r0.mCreateScale
            java.lang.String r7 = "10px"
            r8 = 0
            int r5 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r7, r5, r8, r6)
            android.widget.RelativeLayout r6 = r0.mTitlelayout
            int r6 = r6.indexOfChild(r2)
            r7 = -1
            if (r7 != r6) goto L_0x0023
            android.widget.RelativeLayout r6 = r0.mTitlelayout
            r6.addView(r2)
        L_0x0023:
            boolean r6 = io.dcloud.common.util.PdrUtil.isEmpty(r18)
            r11 = 15
            if (r6 != 0) goto L_0x0124
            int r6 = r0.mInnerWidth
            float r13 = r0.mCreateScale
            r14 = r28
            int r6 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r14, r6, r8, r13)
            if (r6 > 0) goto L_0x003a
            int r6 = r0.mInnerHeight
            int r6 = r6 - r5
        L_0x003a:
            int r13 = r0.mInnerHeight
            if (r6 <= r13) goto L_0x003f
            r6 = r13
        L_0x003f:
            android.widget.RelativeLayout$LayoutParams r13 = new android.widget.RelativeLayout$LayoutParams
            r13.<init>(r6, r6)
            r14 = 9
            r13.addRule(r14)
            r13.addRule(r11)
            r14 = 2
            int r5 = r5 / r14
            r13.rightMargin = r5
            int r5 = r0.mInnerWidth
            float r15 = r0.mCreateScale
            r11 = r19
            int r5 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r11, r5, r8, r15)
            boolean r11 = isBase64Image(r18)
            java.lang.String r15 = ""
            java.lang.String r9 = "^data:image/.*;base64,"
            if (r11 != 0) goto L_0x007a
            pl.droidsonroids.gif.GifDrawable r10 = new pl.droidsonroids.gif.GifDrawable     // Catch:{ IOException -> 0x0088 }
            android.content.Context r16 = r17.getContext()     // Catch:{ IOException -> 0x0088 }
            android.content.ContentResolver r7 = r16.getContentResolver()     // Catch:{ IOException -> 0x0088 }
            java.lang.String r16 = r17.getIconPath(r18)     // Catch:{ IOException -> 0x0088 }
            android.net.Uri r12 = android.net.Uri.parse(r16)     // Catch:{ IOException -> 0x0088 }
            r10.<init>((android.content.ContentResolver) r7, (android.net.Uri) r12)     // Catch:{ IOException -> 0x0088 }
            goto L_0x0089
        L_0x007a:
            pl.droidsonroids.gif.GifDrawable r10 = new pl.droidsonroids.gif.GifDrawable     // Catch:{  }
            java.lang.String r7 = r1.replaceFirst(r9, r15)     // Catch:{  }
            byte[] r7 = android.util.Base64.decode(r7, r8)     // Catch:{  }
            r10.<init>((byte[]) r7)     // Catch:{  }
            goto L_0x0089
        L_0x0088:
            r10 = 0
        L_0x0089:
            if (r5 > 0) goto L_0x00c0
            android.content.Context r5 = r17.getContext()
            com.bumptech.glide.RequestManager r5 = com.bumptech.glide.Glide.with((android.content.Context) r5)
            if (r10 == 0) goto L_0x009e
            r2.setImageDrawable(r10)
            android.widget.ImageView$ScaleType r1 = android.widget.ImageView.ScaleType.CENTER_CROP
            r2.setScaleType(r1)
            goto L_0x010e
        L_0x009e:
            if (r11 == 0) goto L_0x00a9
            java.lang.String r1 = r1.replaceFirst(r9, r15)
            byte[] r1 = android.util.Base64.decode(r1, r8)
            goto L_0x00ad
        L_0x00a9:
            java.lang.String r1 = r17.getIconPath(r18)
        L_0x00ad:
            com.bumptech.glide.RequestBuilder r1 = r5.load((java.lang.Object) r1)
            com.bumptech.glide.load.resource.bitmap.CenterCrop r5 = new com.bumptech.glide.load.resource.bitmap.CenterCrop
            r5.<init>()
            com.bumptech.glide.request.BaseRequestOptions r1 = r1.transform((com.bumptech.glide.load.Transformation<android.graphics.Bitmap>) r5)
            com.bumptech.glide.RequestBuilder r1 = (com.bumptech.glide.RequestBuilder) r1
            r1.into((android.widget.ImageView) r2)
            goto L_0x010e
        L_0x00c0:
            android.content.Context r7 = r17.getContext()
            com.bumptech.glide.RequestManager r7 = com.bumptech.glide.Glide.with((android.content.Context) r7)
            if (r10 == 0) goto L_0x00dc
            io.dcloud.feature.nativeObj.GIFCornerRadiusTransform r1 = new io.dcloud.feature.nativeObj.GIFCornerRadiusTransform
            float r5 = (float) r5
            r1.<init>(r5, r6)
            r10.setTransform(r1)
            r2.setImageDrawable(r10)
            android.widget.ImageView$ScaleType r1 = android.widget.ImageView.ScaleType.CENTER_CROP
            r2.setScaleType(r1)
            goto L_0x010e
        L_0x00dc:
            if (r11 == 0) goto L_0x00e7
            java.lang.String r1 = r1.replaceFirst(r9, r15)
            byte[] r1 = android.util.Base64.decode(r1, r8)
            goto L_0x00eb
        L_0x00e7:
            java.lang.String r1 = r17.getIconPath(r18)
        L_0x00eb:
            com.bumptech.glide.RequestBuilder r1 = r7.load((java.lang.Object) r1)
            com.bumptech.glide.load.MultiTransformation r6 = new com.bumptech.glide.load.MultiTransformation
            com.bumptech.glide.load.Transformation[] r7 = new com.bumptech.glide.load.Transformation[r14]
            com.bumptech.glide.load.resource.bitmap.CenterCrop r9 = new com.bumptech.glide.load.resource.bitmap.CenterCrop
            r9.<init>()
            r7[r8] = r9
            com.bumptech.glide.load.resource.bitmap.RoundedCorners r9 = new com.bumptech.glide.load.resource.bitmap.RoundedCorners
            r9.<init>(r5)
            r5 = 1
            r7[r5] = r9
            r6.<init>((com.bumptech.glide.load.Transformation<T>[]) r7)
            com.bumptech.glide.request.BaseRequestOptions r1 = r1.transform((com.bumptech.glide.load.Transformation<android.graphics.Bitmap>) r6)
            com.bumptech.glide.RequestBuilder r1 = (com.bumptech.glide.RequestBuilder) r1
            r1.into((android.widget.ImageView) r2)
        L_0x010e:
            android.widget.RelativeLayout r1 = r0.mTitlelayout
            int r1 = r1.indexOfChild(r2)
            r5 = -1
            if (r5 != r1) goto L_0x011d
            android.widget.RelativeLayout r1 = r0.mTitlelayout
            r1.addView(r2, r13)
            goto L_0x0120
        L_0x011d:
            r2.setLayoutParams(r13)
        L_0x0120:
            r2.setVisibility(r8)
            goto L_0x0129
        L_0x0124:
            r1 = 8
            r2.setVisibility(r1)
        L_0x0129:
            android.widget.RelativeLayout$LayoutParams r1 = new android.widget.RelativeLayout$LayoutParams
            r5 = -2
            r1.<init>(r5, r5)
            r6 = 10
            r1.addRule(r6)
            int r7 = r24.getVisibility()
            r9 = 14
            java.lang.String r10 = "auto"
            java.lang.String r11 = "left"
            if (r7 == 0) goto L_0x0151
            boolean r7 = r4.equals(r11)
            if (r7 != 0) goto L_0x0151
            boolean r7 = r4.equals(r10)
            if (r7 == 0) goto L_0x014d
            goto L_0x0151
        L_0x014d:
            r1.addRule(r9)
            goto L_0x0159
        L_0x0151:
            int r7 = r24.getId()
            r12 = 1
            r1.addRule(r12, r7)
        L_0x0159:
            android.widget.RelativeLayout r7 = r0.mTitlelayout
            android.widget.TextView r12 = r0.mTitleView
            int r7 = r7.indexOfChild(r12)
            r12 = -1
            if (r12 != r7) goto L_0x016c
            android.widget.RelativeLayout r7 = r0.mTitlelayout
            android.widget.TextView r13 = r0.mTitleView
            r7.addView(r13, r1)
            goto L_0x0171
        L_0x016c:
            android.widget.TextView r7 = r0.mTitleView
            r7.setLayoutParams(r1)
        L_0x0171:
            android.widget.RelativeLayout r7 = r0.mTitlelayout
            int r7 = r7.indexOfChild(r3)
            if (r12 != r7) goto L_0x017e
            android.widget.RelativeLayout r7 = r0.mTitlelayout
            r7.addView(r3)
        L_0x017e:
            boolean r7 = io.dcloud.common.util.PdrUtil.isEmpty(r20)
            if (r7 != 0) goto L_0x0213
            android.widget.RelativeLayout$LayoutParams r7 = new android.widget.RelativeLayout$LayoutParams
            r7.<init>(r5, r5)
            android.widget.TextView r5 = r0.mTitleView
            int r5 = r5.getId()
            r12 = 3
            r7.addRule(r12, r5)
            int r5 = r24.getVisibility()
            if (r5 == 0) goto L_0x01aa
            boolean r5 = r4.equals(r11)
            if (r5 != 0) goto L_0x01aa
            boolean r4 = r4.equals(r10)
            if (r4 == 0) goto L_0x01a6
            goto L_0x01aa
        L_0x01a6:
            r7.addRule(r9)
            goto L_0x01b2
        L_0x01aa:
            int r2 = r24.getId()
            r4 = 1
            r7.addRule(r4, r2)
        L_0x01b2:
            r2 = r20
            r3.setText(r2)
            java.lang.String r2 = "clip"
            r4 = r23
            boolean r2 = r2.equals(r4)
            if (r2 == 0) goto L_0x01c6
            r2 = 0
            r3.setEllipsize(r2)
            goto L_0x01cb
        L_0x01c6:
            android.text.TextUtils$TruncateAt r2 = android.text.TextUtils.TruncateAt.END
            r3.setEllipsize(r2)
        L_0x01cb:
            r25.setSingleLine()
            r2 = 1
            r3.setLines(r2)
            boolean r2 = io.dcloud.common.util.PdrUtil.isEmpty(r21)
            if (r2 == 0) goto L_0x01db
            r2 = r26
            goto L_0x01df
        L_0x01db:
            int r2 = io.dcloud.common.util.PdrUtil.stringToColor(r21)
        L_0x01df:
            r3.setTextColor(r2)
            android.text.TextPaint r2 = r25.getPaint()
            boolean r4 = io.dcloud.common.util.PdrUtil.isEmpty(r22)
            if (r4 == 0) goto L_0x01ef
            java.lang.String r4 = "12px"
            goto L_0x01f1
        L_0x01ef:
            r4 = r22
        L_0x01f1:
            int r5 = r0.mInnerWidth
            float r9 = r0.mCreateScale
            int r4 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r4, r5, r8, r9)
            float r4 = (float) r4
            r2.setTextSize(r4)
            android.widget.RelativeLayout r2 = r0.mTitlelayout
            int r2 = r2.indexOfChild(r3)
            r4 = -1
            if (r4 != r2) goto L_0x020c
            android.widget.RelativeLayout r2 = r0.mTitlelayout
            r2.addView(r3, r7)
            goto L_0x020f
        L_0x020c:
            r3.setLayoutParams(r7)
        L_0x020f:
            r3.setVisibility(r8)
            goto L_0x0218
        L_0x0213:
            r2 = 8
            r3.setVisibility(r2)
        L_0x0218:
            int r2 = r25.getVisibility()
            if (r2 != 0) goto L_0x0234
            android.widget.TextView r2 = r0.mTitleView
            android.view.ViewGroup$LayoutParams r2 = r2.getLayoutParams()
            android.widget.RelativeLayout$LayoutParams r2 = (android.widget.RelativeLayout.LayoutParams) r2
            r2.addRule(r6)
            r3 = 15
            r2.removeRule(r3)
            android.widget.TextView r2 = r0.mTitleView
            r2.setLayoutParams(r1)
            goto L_0x0249
        L_0x0234:
            r3 = 15
            android.widget.TextView r2 = r0.mTitleView
            android.view.ViewGroup$LayoutParams r2 = r2.getLayoutParams()
            android.widget.RelativeLayout$LayoutParams r2 = (android.widget.RelativeLayout.LayoutParams) r2
            r2.removeRule(r6)
            r2.addRule(r3)
            android.widget.TextView r2 = r0.mTitleView
            r2.setLayoutParams(r1)
        L_0x0249:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.nativeObj.TitleNView.layoutSubtitleIcon(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, android.widget.ImageView, android.widget.TextView, int, java.lang.String, java.lang.String):void");
    }

    private void setButtonColor(View view, String str, String str2, String str3) {
        int i;
        int i2;
        int i3;
        if (view != null) {
            Drawable background = view.getBackground();
            if (background != null && (background instanceof NativeViewBackButtonDrawable)) {
                try {
                    i3 = Color.parseColor(str);
                } catch (Exception unused) {
                    i3 = PdrUtil.stringToColor(str);
                }
                NativeViewBackButtonDrawable nativeViewBackButtonDrawable = (NativeViewBackButtonDrawable) background;
                if (i3 != nativeViewBackButtonDrawable.getDrawableColor()) {
                    nativeViewBackButtonDrawable.setDrawableColor(i3);
                }
            }
            try {
                i = Color.parseColor(str2);
            } catch (Exception unused2) {
                i = PdrUtil.stringToColor(str2);
            }
            try {
                i2 = Color.parseColor(str3);
            } catch (Exception unused3) {
                i2 = PdrUtil.stringToColor(str3);
            }
            if ((view instanceof RelativeLayout) || (view instanceof BadgeRelateiveLayout)) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i4 = 0; i4 < viewGroup.getChildCount(); i4++) {
                    TextView textView = (TextView) viewGroup.getChildAt(i4);
                    if (i != textView.getTextColors().getDefaultColor()) {
                        textView.setTextColor(createColorStateList(i, i2));
                    }
                }
            } else if (view instanceof TextView) {
                TextView textView2 = (TextView) view;
                if (i != textView2.getTextColors().getDefaultColor()) {
                    textView2.setTextColor(createColorStateList(i, i2));
                }
            } else if (view instanceof LinearLayout) {
                LinearLayout linearLayout = (LinearLayout) view;
                for (int i5 = 0; i5 < linearLayout.getChildCount(); i5++) {
                    View childAt = linearLayout.getChildAt(i5);
                    if (childAt instanceof TextView) {
                        TextView textView3 = (TextView) childAt;
                        if (i != textView3.getTextColors().getDefaultColor()) {
                            textView3.setTextColor(createColorStateList(i, i2));
                        }
                    } else if (childAt instanceof RelativeLayout) {
                        TextView textView4 = (TextView) ((RelativeLayout) childAt).getChildAt(0);
                        if (i != textView4.getTextColors().getDefaultColor()) {
                            textView4.setTextColor(createColorStateList(i, i2));
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void setTextAndFont(String str, String str2, String str3, TextView textView, boolean z) {
        Typeface typeface;
        String str4;
        if (z || str3.equals("none")) {
            textView.setText(str);
            if (this.mApp == null || this.mWebView == null || TextUtils.isEmpty(str2)) {
                typeface = null;
            } else {
                if (str2.contains("__wap2app.ttf")) {
                    str4 = BaseInfo.sBaseWap2AppTemplatePath + "wap2app__template/__wap2app.ttf";
                    if (!new File(str4).exists()) {
                        str4 = this.mWebView.obtainApp().convert2AbsFullPath(this.mWebView.obtainFullUrl(), str2);
                    }
                } else {
                    str4 = this.mWebView.obtainApp().convert2AbsFullPath(this.mWebView.obtainFullUrl(), str2);
                }
                typeface = NativeTypefaceFactory.getTypeface(this.mWebView.obtainApp(), str4);
            }
        } else {
            typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/dcloud_iconfont.ttf");
            str3.hashCode();
            char c = 65535;
            switch (str3.hashCode()) {
                case -677145915:
                    if (str3.equals("forward")) {
                        c = 0;
                        break;
                    }
                    break;
                case 3015911:
                    if (str3.equals("back")) {
                        c = 1;
                        break;
                    }
                    break;
                case 3208415:
                    if (str3.equals("home")) {
                        c = 2;
                        break;
                    }
                    break;
                case 3347807:
                    if (str3.equals(AbsoluteConst.EVENTS_MENU)) {
                        c = 3;
                        break;
                    }
                    break;
                case 94756344:
                    if (str3.equals(AbsoluteConst.EVENTS_CLOSE)) {
                        c = 4;
                        break;
                    }
                    break;
                case 109400031:
                    if (str3.equals("share")) {
                        c = 5;
                        break;
                    }
                    break;
                case 1050790300:
                    if (str3.equals("favorite")) {
                        c = 6;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    str = "";
                    break;
                case 1:
                    str = "";
                    break;
                case 2:
                    str = "";
                    break;
                case 3:
                    str = "";
                    break;
                case 4:
                    str = "";
                    break;
                case 5:
                    str = "";
                    break;
                case 6:
                    str = "";
                    break;
            }
            textView.setText(str);
        }
        if (typeface != null) {
            textView.setTypeface(typeface);
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0036  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0074  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setTextGravity(java.lang.String r4, java.lang.String r5) {
        /*
            r3 = this;
            int r0 = r4.hashCode()
            r1 = 1
            r2 = -1364013995(0xffffffffaeb2cc55, float:-8.1307995E-11)
            if (r0 == r2) goto L_0x0029
            r2 = 3317767(0x32a007, float:4.649182E-39)
            if (r0 == r2) goto L_0x001f
            r2 = 108511772(0x677c21c, float:4.6598146E-35)
            if (r0 == r2) goto L_0x0015
            goto L_0x0033
        L_0x0015:
            java.lang.String r0 = "right"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0033
            r4 = 1
            goto L_0x0034
        L_0x001f:
            java.lang.String r0 = "left"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0033
            r4 = 0
            goto L_0x0034
        L_0x0029:
            java.lang.String r0 = "center"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0033
            r4 = 2
            goto L_0x0034
        L_0x0033:
            r4 = -1
        L_0x0034:
            if (r4 == 0) goto L_0x0074
            java.lang.String r0 = " "
            if (r4 == r1) goto L_0x0057
            android.widget.EditText r4 = r3.searchInput
            r1 = 17
            r4.setGravity(r1)
            android.widget.EditText r4 = r3.searchInput
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            r1.append(r5)
            java.lang.String r5 = r1.toString()
            r4.setHint(r5)
            goto L_0x0081
        L_0x0057:
            android.widget.EditText r4 = r3.searchInput
            r1 = 8388613(0x800005, float:1.175495E-38)
            r4.setGravity(r1)
            android.widget.EditText r4 = r3.searchInput
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            r1.append(r5)
            java.lang.String r5 = r1.toString()
            r4.setHint(r5)
            goto L_0x0081
        L_0x0074:
            android.widget.EditText r4 = r3.searchInput
            r0 = 8388611(0x800003, float:1.1754948E-38)
            r4.setGravity(r0)
            android.widget.EditText r4 = r3.searchInput
            r4.setHint(r5)
        L_0x0081:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.nativeObj.TitleNView.setTextGravity(java.lang.String, java.lang.String):void");
    }

    private void showCapsuleMenu(final JSONArray jSONArray) {
        a aVar;
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                aVar = new a(this.mWebView.getActivity());
            } else {
                aVar = new a(this.mWebView.getActivity(), 16973837);
            }
            aVar.a((a.b) new a.b() {
                public void initCancelText(TextView textView) {
                    TextPaint paint = textView.getPaint();
                    if (TitleNView.this.mMenuButtonFontWeight.equals(Constants.Value.BOLD)) {
                        paint.setFakeBoldText(true);
                    } else {
                        paint.setFakeBoldText(false);
                    }
                }

                public void initTextItem(int i, TextView textView, String str) {
                    if (TitleNView.this.mMenuButtons.size() > i) {
                        ButtonDataItem buttonDataItem = (ButtonDataItem) TitleNView.this.mMenuButtons.get(i);
                        TitleNView.this.setTextAndFont(str, buttonDataItem.getFontSrc(), buttonDataItem.getFontType(), textView, TextUtils.isEmpty(buttonDataItem.getFontType()));
                    } else if (jSONArray.length() > i) {
                        if (jSONArray.optJSONObject(i).optString("type", "").equals("interval")) {
                            textView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#e5e5e5")));
                            textView.getLayoutParams().height = 10;
                            return;
                        }
                        textView.setText(str);
                    }
                    TextPaint paint = textView.getPaint();
                    if (TitleNView.this.mMenuButtonFontWeight.equals(Constants.Value.BOLD)) {
                        paint.setFakeBoldText(true);
                    } else {
                        paint.setFakeBoldText(false);
                    }
                    textView.setTextColor(TitleNView.this.mMenuButtonTextColor);
                }

                public boolean onDismiss(int i) {
                    int i2 = i - 1;
                    if (i2 <= 0 || jSONArray.length() <= i2 || !jSONArray.optJSONObject(i2).optString("type", "").equals("interval")) {
                        return false;
                    }
                    return true;
                }

                /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v1, resolved type: java.lang.Object[]} */
                /* JADX WARNING: Multi-variable type inference failed */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void onItemClick(int r9) {
                    /*
                        r8 = this;
                        int r9 = r9 + -1
                        if (r9 >= 0) goto L_0x0005
                        return
                    L_0x0005:
                        io.dcloud.feature.nativeObj.TitleNView r0 = io.dcloud.feature.nativeObj.TitleNView.this
                        java.util.ArrayList r0 = r0.mMenuButtons
                        int r0 = r0.size()
                        r1 = 2
                        r2 = 1
                        r3 = 0
                        if (r0 <= r9) goto L_0x0072
                        io.dcloud.feature.nativeObj.TitleNView r0 = io.dcloud.feature.nativeObj.TitleNView.this
                        java.util.ArrayList r0 = r0.mMenuButtons
                        java.lang.Object r9 = r0.get(r9)
                        io.dcloud.feature.nativeObj.data.ButtonDataItem r9 = (io.dcloud.feature.nativeObj.data.ButtonDataItem) r9
                        if (r9 == 0) goto L_0x00cf
                        io.dcloud.feature.nativeObj.TitleNView r0 = io.dcloud.feature.nativeObj.TitleNView.this
                        io.dcloud.common.DHInterface.IWebview r0 = r0.mWebView
                        io.dcloud.common.DHInterface.IFrameView r0 = r0.obtainFrameView()
                        io.dcloud.common.DHInterface.AbsMgr r0 = r0.obtainWindowMgr()
                        io.dcloud.common.DHInterface.IMgr$MgrType r4 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
                        r5 = 4
                        java.lang.Object[] r5 = new java.lang.Object[r5]
                        io.dcloud.feature.nativeObj.TitleNView r6 = io.dcloud.feature.nativeObj.TitleNView.this
                        io.dcloud.common.DHInterface.IApp r6 = r6.mApp
                        r5[r3] = r6
                        java.lang.String r7 = "ui"
                        r5[r2] = r7
                        java.lang.String r7 = "findWebview"
                        r5[r1] = r7
                        java.lang.String[] r1 = new java.lang.String[r1]
                        java.lang.String r6 = r6.obtainAppId()
                        r1[r3] = r6
                        java.lang.String r3 = r9.getWebviewUuid()
                        r1[r2] = r3
                        r2 = 3
                        r5[r2] = r1
                        r1 = 10
                        java.lang.Object r0 = r0.processEvent(r4, r1, r5)
                        r1 = 0
                        if (r0 == 0) goto L_0x0062
                        boolean r2 = r0 instanceof io.dcloud.common.DHInterface.IWebview
                        if (r2 == 0) goto L_0x0062
                        r1 = r0
                        io.dcloud.common.DHInterface.IWebview r1 = (io.dcloud.common.DHInterface.IWebview) r1
                    L_0x0062:
                        if (r1 != 0) goto L_0x0068
                        io.dcloud.feature.nativeObj.TitleNView r0 = io.dcloud.feature.nativeObj.TitleNView.this
                        io.dcloud.common.DHInterface.IWebview r1 = r0.mWebView
                    L_0x0068:
                        io.dcloud.feature.nativeObj.TitleNView r0 = io.dcloud.feature.nativeObj.TitleNView.this
                        java.lang.String r9 = r9.getOnclick()
                        r0.buttonOnclick(r9, r1)
                        goto L_0x00cf
                    L_0x0072:
                        org.json.JSONArray r0 = r6
                        int r0 = r0.length()
                        if (r0 <= r9) goto L_0x00cf
                        org.json.JSONArray r0 = r6
                        org.json.JSONObject r9 = r0.optJSONObject(r9)
                        java.lang.String r0 = "type"
                        java.lang.String r4 = ""
                        java.lang.String r0 = r9.optString(r0, r4)
                        java.lang.String r4 = "interval"
                        boolean r0 = r0.equals(r4)
                        if (r0 == 0) goto L_0x0091
                        return
                    L_0x0091:
                        java.lang.String r0 = "id"
                        boolean r4 = r9.has(r0)
                        if (r4 == 0) goto L_0x00cf
                        java.lang.String r9 = r9.optString(r0)
                        android.os.Bundle r4 = new android.os.Bundle
                        r4.<init>()
                        r4.putString(r0, r9)
                        io.dcloud.feature.nativeObj.TitleNView r9 = io.dcloud.feature.nativeObj.TitleNView.this
                        io.dcloud.common.DHInterface.IWebview r9 = r9.mWebView
                        io.dcloud.common.DHInterface.IApp r9 = r9.obtainApp()
                        java.lang.String r9 = r9.obtainAppId()
                        java.lang.String r0 = "appid"
                        r4.putString(r0, r9)
                        java.lang.Object[] r9 = new java.lang.Object[r1]
                        io.dcloud.feature.nativeObj.TitleNView r0 = io.dcloud.feature.nativeObj.TitleNView.this
                        io.dcloud.common.DHInterface.IWebview r0 = r0.mWebView
                        r9[r3] = r0
                        r9[r2] = r4
                        io.dcloud.common.DHInterface.IFrameView r0 = r0.obtainFrameView()
                        io.dcloud.common.DHInterface.AbsMgr r0 = r0.obtainWindowMgr()
                        io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr
                        r2 = 77
                        r0.processEvent(r1, r2, r9)
                    L_0x00cf:
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.nativeObj.TitleNView.AnonymousClass15.onItemClick(int):void");
                }
            });
            aVar.b(getContext().getString(R.string.dcloud_common_cancel)).d(this.mMenuButtonTextColor).a(PdrUtil.parseFloat(this.mMenuButtonFontSize, 0.0f, 0.0f, 1.0f)).a(jSONArray).a(true).b(false).e(0);
            aVar.j();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCapsuleLayout() {
        CapsuleLayout capsuleLayout = this.mCapsuleLayout;
        if (capsuleLayout != null && !capsuleLayout.isDiy) {
            String backgroundColor = getBackgroundColor();
            if (!TextUtils.isEmpty(backgroundColor)) {
                if (this.mCapsuleLayout.checkColorToStyle(PdrUtil.stringToColor(backgroundColor)) == 1) {
                    this.closeBt.setTextColor(-16777216);
                    this.menuBt.setTextColor(-16777216);
                    return;
                }
                this.closeBt.setTextColor(-1);
                this.menuBt.setTextColor(-1);
            }
        }
    }

    /* JADX WARNING: type inference failed for: r8v43, types: [android.view.View] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addBackButton(java.lang.String r17, java.lang.String r18, java.lang.String r19, org.json.JSONObject r20) {
        /*
            r16 = this;
            r0 = r16
            r1 = r20
            r16.initTitleNViewLayout()
            r16.initLeftButtonLayout()
            io.dcloud.feature.nativeObj.TitleNView$BadgeLinearLayout r2 = r0.mBackButton
            r3 = 8
            r4 = -1
            r5 = 1
            r6 = -2
            r7 = 0
            if (r2 != 0) goto L_0x00b2
            io.dcloud.feature.nativeObj.TitleNView$BadgeLinearLayout r2 = new io.dcloud.feature.nativeObj.TitleNView$BadgeLinearLayout
            android.content.Context r8 = r16.getContext()
            float r9 = r0.mCreateScale
            int r10 = r0.redDotColor
            r2.<init>(r0, r8, r9, r10)
            r0.mBackButton = r2
            android.content.res.Resources r8 = r16.getResources()
            int r9 = io.dcloud.base.R.string.dcloud_titlenview_back_button_description
            java.lang.String r8 = r8.getString(r9)
            r2.setContentDescription(r8)
            io.dcloud.feature.nativeObj.TitleNView$BadgeLinearLayout r2 = r0.mBackButton
            java.lang.String r8 = "TitleNView.BackButton"
            r2.setTag(r8)
            io.dcloud.feature.nativeObj.TitleNView$BadgeLinearLayout r2 = r0.mBackButton
            r8 = 17
            r2.setGravity(r8)
            android.widget.TextView r2 = new android.widget.TextView
            android.content.Context r9 = r16.getContext()
            r2.<init>(r9)
            r2.setGravity(r8)
            int r9 = android.view.View.generateViewId()
            r2.setId(r9)
            r2.setIncludeFontPadding(r7)
            android.widget.TextView r9 = new android.widget.TextView
            android.content.Context r10 = r16.getContext()
            r9.<init>(r10)
            r9.setGravity(r8)
            r9.setIncludeFontPadding(r7)
            android.widget.TextView r10 = new android.widget.TextView
            android.content.Context r11 = r16.getContext()
            r10.<init>(r11)
            r10.setGravity(r8)
            r10.setIncludeFontPadding(r7)
            r10.setTextColor(r4)
            r10.setVisibility(r3)
            android.widget.RelativeLayout r8 = new android.widget.RelativeLayout
            android.content.Context r11 = r16.getContext()
            r8.<init>(r11)
            r11 = 16
            r8.setGravity(r11)
            io.dcloud.feature.nativeObj.TitleNView$BadgeLinearLayout r11 = r0.mBackButton
            android.widget.LinearLayout$LayoutParams r12 = new android.widget.LinearLayout$LayoutParams
            r13 = 500(0x1f4, float:7.0E-43)
            r12.<init>(r6, r13)
            r11.addView(r2, r12)
            io.dcloud.feature.nativeObj.TitleNView$BadgeLinearLayout r11 = r0.mBackButton
            android.widget.LinearLayout$LayoutParams r12 = new android.widget.LinearLayout$LayoutParams
            r12.<init>(r6, r4)
            r11.addView(r8, r12)
            android.widget.RelativeLayout$LayoutParams r11 = new android.widget.RelativeLayout$LayoutParams
            r11.<init>(r6, r4)
            r8.addView(r9, r11)
            android.widget.RelativeLayout$LayoutParams r11 = new android.widget.RelativeLayout$LayoutParams
            r11.<init>(r6, r6)
            r12 = 15
            r11.addRule(r12)
            r8.addView(r10, r11)
            goto L_0x00cd
        L_0x00b2:
            android.view.View r2 = r2.getChildAt(r7)
            android.widget.TextView r2 = (android.widget.TextView) r2
            io.dcloud.feature.nativeObj.TitleNView$BadgeLinearLayout r8 = r0.mBackButton
            android.view.View r8 = r8.getChildAt(r5)
            android.widget.RelativeLayout r8 = (android.widget.RelativeLayout) r8
            android.view.View r9 = r8.getChildAt(r7)
            android.widget.TextView r9 = (android.widget.TextView) r9
            android.view.View r8 = r8.getChildAt(r5)
            r10 = r8
            android.widget.TextView r10 = (android.widget.TextView) r10
        L_0x00cd:
            io.dcloud.common.DHInterface.IFrameView r8 = r0.mFrameViewParent
            r11 = 5
            java.lang.String r12 = "text"
            if (r8 == 0) goto L_0x00ee
            int r8 = r8.getFrameType()
            if (r11 != r8) goto L_0x00ee
            if (r1 == 0) goto L_0x00e7
            boolean r8 = r1.has(r12)
            if (r8 == 0) goto L_0x00e7
            java.lang.String r8 = r1.optString(r12)
            goto L_0x00ea
        L_0x00e7:
            java.lang.String r8 = ""
        L_0x00ea:
            r2.setText(r8)
            goto L_0x0101
        L_0x00ee:
            if (r1 == 0) goto L_0x00fb
            boolean r8 = r1.has(r12)
            if (r8 == 0) goto L_0x00fb
            java.lang.String r8 = r1.optString(r12)
            goto L_0x00fe
        L_0x00fb:
            java.lang.String r8 = ""
        L_0x00fe:
            r2.setText(r8)
        L_0x0101:
            android.content.Context r8 = r16.getContext()
            android.content.res.AssetManager r8 = r8.getAssets()
            java.lang.String r12 = "fonts/dcloud_iconfont.ttf"
            android.graphics.Typeface r8 = android.graphics.Typeface.createFromAsset(r8, r12)
            r2.setTypeface(r8)
            if (r1 == 0) goto L_0x0121
            java.lang.String r8 = "title"
            boolean r12 = r1.has(r8)
            if (r12 == 0) goto L_0x0121
            java.lang.String r8 = r1.optString(r8)
            goto L_0x0123
        L_0x0121:
            java.lang.String r8 = ""
        L_0x0123:
            r9.setText(r8)
            io.dcloud.feature.nativeObj.TitleNView$BadgeLinearLayout r8 = r0.mBackButton
            io.dcloud.feature.nativeObj.TitleNView$3 r12 = new io.dcloud.feature.nativeObj.TitleNView$3
            r12.<init>()
            r8.setOnClickListener(r12)
            java.lang.String r8 = "transparent"
            r12 = r19
            boolean r8 = r8.equals(r12)
            java.lang.String r12 = "2px"
            java.lang.String r13 = "16px"
            java.lang.String r14 = "titleSize"
            java.lang.String r15 = "fontSize"
            if (r8 == 0) goto L_0x01ca
            if (r1 == 0) goto L_0x014f
            boolean r8 = r1.has(r15)
            if (r8 == 0) goto L_0x014f
            java.lang.String r8 = r1.optString(r15)
            goto L_0x0151
        L_0x014f:
            java.lang.String r8 = "22px"
        L_0x0151:
            android.text.TextPaint r15 = r2.getPaint()
            int r6 = r0.mInnerWidth
            float r4 = r0.mCreateScale
            int r4 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r8, r6, r7, r4)
            float r4 = (float) r4
            r15.setTextSize(r4)
            if (r1 == 0) goto L_0x016e
            boolean r4 = r1.has(r14)
            if (r4 == 0) goto L_0x016e
            java.lang.String r4 = r1.optString(r14)
            goto L_0x016f
        L_0x016e:
            r4 = r13
        L_0x016f:
            android.text.TextPaint r6 = r9.getPaint()
            int r8 = r0.mInnerWidth
            float r14 = r0.mCreateScale
            int r4 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r4, r8, r7, r14)
            float r4 = (float) r4
            r6.setTextSize(r4)
            io.dcloud.common.DHInterface.IFrameView r4 = r0.mFrameViewParent
            if (r4 == 0) goto L_0x018f
            int r4 = r4.getFrameType()
            if (r11 != r4) goto L_0x018f
            io.dcloud.feature.nativeObj.TitleNView$BadgeLinearLayout r4 = r0.mBackButton
            r4.setPadding(r7, r7, r7, r7)
            goto L_0x019c
        L_0x018f:
            io.dcloud.feature.nativeObj.TitleNView$BadgeLinearLayout r4 = r0.mBackButton
            int r6 = r0.mInnerWidth
            float r8 = r0.mCreateScale
            int r6 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r12, r6, r7, r8)
            r4.setPadding(r7, r7, r6, r7)
        L_0x019c:
            if (r1 == 0) goto L_0x01ab
            java.lang.String r4 = "background"
            boolean r6 = r1.has(r4)
            if (r6 == 0) goto L_0x01ab
            java.lang.String r4 = r1.optString(r4)
            goto L_0x01ad
        L_0x01ab:
            java.lang.String r4 = "#7F333333"
        L_0x01ad:
            io.dcloud.feature.nativeObj.NativeViewBackButtonDrawable r6 = new io.dcloud.feature.nativeObj.NativeViewBackButtonDrawable
            int r4 = io.dcloud.common.util.PdrUtil.stringToColor(r4)
            r6.<init>(r4)
            java.lang.String r4 = "backButton"
            r6.setWidth(r4)
            io.dcloud.feature.nativeObj.TitleNView$BadgeLinearLayout r4 = r0.mBackButton
            r4.setBackground(r6)
            java.lang.String r4 = "#FFFFFF"
            r6 = 1050253722(0x3e99999a, float:0.3)
            java.lang.String r6 = io.dcloud.common.util.TitleNViewUtil.changeColorAlpha((java.lang.String) r4, (float) r6)
            goto L_0x020b
        L_0x01ca:
            if (r1 == 0) goto L_0x01d7
            boolean r4 = r1.has(r15)
            if (r4 == 0) goto L_0x01d7
            java.lang.String r4 = r1.optString(r15)
            goto L_0x01d9
        L_0x01d7:
            java.lang.String r4 = "27px"
        L_0x01d9:
            android.text.TextPaint r6 = r2.getPaint()
            int r8 = r0.mInnerWidth
            float r11 = r0.mCreateScale
            int r4 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r4, r8, r7, r11)
            float r4 = (float) r4
            r6.setTextSize(r4)
            if (r1 == 0) goto L_0x01f6
            boolean r4 = r1.has(r14)
            if (r4 == 0) goto L_0x01f6
            java.lang.String r4 = r1.optString(r14)
            goto L_0x01f7
        L_0x01f6:
            r4 = r13
        L_0x01f7:
            android.text.TextPaint r6 = r9.getPaint()
            int r8 = r0.mInnerWidth
            float r11 = r0.mCreateScale
            int r4 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r4, r8, r7, r11)
            float r4 = (float) r4
            r6.setTextSize(r4)
            r4 = r17
            r6 = r18
        L_0x020b:
            if (r1 == 0) goto L_0x0219
            java.lang.String r8 = "badgeSize"
            boolean r11 = r1.has(r8)
            if (r11 == 0) goto L_0x0219
            java.lang.String r13 = r1.optString(r8)
        L_0x0219:
            int r8 = r0.mInnerWidth
            float r11 = r0.mCreateScale
            int r8 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r13, r8, r7, r11)
            android.text.TextPaint r11 = r10.getPaint()
            float r13 = (float) r8
            r11.setTextSize(r13)
            android.view.ViewGroup$LayoutParams r11 = r10.getLayoutParams()
            int r13 = r0.mInnerWidth
            float r14 = r0.mCreateScale
            int r12 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r12, r13, r7, r14)
            int r12 = r12 + r8
            r11.height = r12
            r10.setLayoutParams(r11)
            int r12 = r11.height
            r10.setMinWidth(r12)
            int r8 = r8 / 4
            int r12 = r16.getPaddingTop()
            int r13 = r16.getPaddingBottom()
            r10.setPadding(r8, r12, r8, r13)
            java.lang.String r8 = "normal"
            if (r1 == 0) goto L_0x025e
            java.lang.String r12 = "fontWeight"
            boolean r13 = r1.has(r12)
            if (r13 == 0) goto L_0x025e
            java.lang.String r12 = r1.optString(r12)
            goto L_0x025f
        L_0x025e:
            r12 = r8
        L_0x025f:
            boolean r13 = r12.equals(r8)
            java.lang.String r14 = "bold"
            if (r13 == 0) goto L_0x026f
            android.text.TextPaint r12 = r2.getPaint()
            r12.setFakeBoldText(r7)
            goto L_0x027c
        L_0x026f:
            boolean r12 = r12.equals(r14)
            if (r12 == 0) goto L_0x027c
            android.text.TextPaint r12 = r2.getPaint()
            r12.setFakeBoldText(r5)
        L_0x027c:
            if (r1 == 0) goto L_0x028b
            java.lang.String r12 = "titleWeight"
            boolean r13 = r1.has(r12)
            if (r13 == 0) goto L_0x028b
            java.lang.String r12 = r1.optString(r12)
            goto L_0x028c
        L_0x028b:
            r12 = r8
        L_0x028c:
            boolean r8 = r12.equals(r8)
            if (r8 == 0) goto L_0x029a
            android.text.TextPaint r8 = r9.getPaint()
            r8.setFakeBoldText(r7)
            goto L_0x02a7
        L_0x029a:
            boolean r8 = r12.equals(r14)
            if (r8 == 0) goto L_0x02a7
            android.text.TextPaint r8 = r9.getPaint()
            r8.setFakeBoldText(r5)
        L_0x02a7:
            r8 = 0
            if (r1 == 0) goto L_0x02b7
            java.lang.String r12 = "badgeText"
            boolean r13 = r1.has(r12)
            if (r13 == 0) goto L_0x02b7
            java.lang.String r12 = r1.optString(r12)
            goto L_0x02b8
        L_0x02b7:
            r12 = r8
        L_0x02b8:
            io.dcloud.feature.nativeObj.TitleNView$BadgeLinearLayout r13 = r0.mBackButton
            r13.setBadgeStr(r12)
            io.dcloud.feature.nativeObj.TitleNView$BadgeLinearLayout r13 = r0.mBackButton
            if (r1 == 0) goto L_0x02d0
            java.lang.String r14 = "redDot"
            boolean r15 = r1.has(r14)
            if (r15 == 0) goto L_0x02d0
            boolean r14 = r1.optBoolean(r14)
            if (r14 == 0) goto L_0x02d0
            goto L_0x02d1
        L_0x02d0:
            r5 = 0
        L_0x02d1:
            r13.setDrawRedDot(r5)
            boolean r5 = io.dcloud.common.util.PdrUtil.isEmpty(r12)
            if (r5 != 0) goto L_0x02e3
            android.widget.TextView$BufferType r3 = android.widget.TextView.BufferType.SPANNABLE
            r10.setText(r12, r3)
            r10.setVisibility(r7)
            goto L_0x02e6
        L_0x02e3:
            r10.setVisibility(r3)
        L_0x02e6:
            if (r1 == 0) goto L_0x02f7
            java.lang.String r3 = "badgeBackground"
            boolean r3 = r1.has(r3)
            if (r3 == 0) goto L_0x02f7
            java.lang.String r3 = "badgeBackground"
            java.lang.String r3 = r1.optString(r3)
            goto L_0x02f8
        L_0x02f7:
            r3 = r8
        L_0x02f8:
            if (r1 == 0) goto L_0x0308
            java.lang.String r5 = "badgeColor"
            boolean r5 = r1.has(r5)
            if (r5 == 0) goto L_0x0308
            java.lang.String r5 = "badgeColor"
            java.lang.String r8 = r1.optString(r5)
        L_0x0308:
            android.graphics.drawable.GradientDrawable r1 = new android.graphics.drawable.GradientDrawable
            r1.<init>()
            boolean r5 = io.dcloud.common.util.PdrUtil.isEmpty(r3)
            if (r5 != 0) goto L_0x031b
            int r3 = io.dcloud.common.util.PdrUtil.stringToColor(r3)
            r1.setColor(r3)
            goto L_0x0320
        L_0x031b:
            r3 = -65536(0xffffffffffff0000, float:NaN)
            r1.setColor(r3)
        L_0x0320:
            int r3 = r11.height
            int r3 = r3 / 2
            float r3 = (float) r3
            r1.setCornerRadius(r3)
            r10.setBackground(r1)
            boolean r1 = io.dcloud.common.util.PdrUtil.isEmpty(r8)
            if (r1 != 0) goto L_0x0338
            int r1 = io.dcloud.common.util.PdrUtil.stringToColor(r8)
            r10.setTextColor(r1)
        L_0x0338:
            int r1 = android.graphics.Color.parseColor(r4)     // Catch:{ Exception -> 0x033d }
            goto L_0x0341
        L_0x033d:
            int r1 = io.dcloud.common.util.PdrUtil.stringToColor(r4)
        L_0x0341:
            int r3 = android.graphics.Color.parseColor(r6)     // Catch:{ Exception -> 0x0346 }
            goto L_0x034a
        L_0x0346:
            int r3 = io.dcloud.common.util.PdrUtil.stringToColor(r6)
        L_0x034a:
            android.content.res.ColorStateList r4 = r0.createColorStateList(r1, r3)
            r2.setTextColor(r4)
            android.content.res.ColorStateList r1 = r0.createColorStateList(r1, r3)
            r9.setTextColor(r1)
            io.dcloud.feature.nativeObj.TitleNView$BadgeLinearLayout r1 = r0.mBackButton
            android.view.ViewParent r1 = r1.getParent()
            if (r1 == 0) goto L_0x036d
            io.dcloud.feature.nativeObj.TitleNView$BadgeLinearLayout r1 = r0.mBackButton
            android.view.ViewParent r1 = r1.getParent()
            android.view.ViewGroup r1 = (android.view.ViewGroup) r1
            io.dcloud.feature.nativeObj.TitleNView$BadgeLinearLayout r2 = r0.mBackButton
            r1.removeView(r2)
        L_0x036d:
            android.widget.LinearLayout r1 = r0.mLeftButtonLayout
            io.dcloud.feature.nativeObj.TitleNView$BadgeLinearLayout r2 = r0.mBackButton
            int r1 = r1.indexOfChild(r2)
            r2 = -1
            if (r2 != r1) goto L_0x03a3
            int r1 = r0.mInnerHeight
            int r2 = r0.mAppScreenWidth
            float r3 = r0.mCreateScale
            java.lang.String r4 = "12px"
            int r2 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r4, r2, r7, r3)
            int r1 = r1 - r2
            android.widget.LinearLayout$LayoutParams r2 = new android.widget.LinearLayout$LayoutParams
            r3 = -2
            r2.<init>(r3, r1)
            int r3 = r0.mAppScreenWidth
            float r4 = r0.mCreateScale
            java.lang.String r5 = "5px"
            int r3 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r5, r3, r7, r4)
            r2.leftMargin = r3
            io.dcloud.feature.nativeObj.TitleNView$BadgeLinearLayout r3 = r0.mBackButton
            r3.setMinimumWidth(r1)
            android.widget.LinearLayout r1 = r0.mLeftButtonLayout
            io.dcloud.feature.nativeObj.TitleNView$BadgeLinearLayout r3 = r0.mBackButton
            r1.addView(r3, r7, r2)
        L_0x03a3:
            io.dcloud.feature.nativeObj.TitleNView$BadgeLinearLayout r1 = r0.mBackButton
            r1.setVisibility(r7)
            r16.requestLayout()
            r16.caculateTitleMargin()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.nativeObj.TitleNView.addBackButton(java.lang.String, java.lang.String, java.lang.String, org.json.JSONObject):void");
    }

    public void addHomeButton(String str, String str2, String str3) {
        int i;
        int i2;
        initTitleNViewLayout();
        initLeftButtonLayout();
        initRightButtonLayout();
        if (this.mHomeButton == null) {
            TextView textView = new TextView(getContext());
            this.mHomeButton = textView;
            textView.setGravity(17);
            this.mHomeButton.setTag("TitleNView.HomeButton");
            this.mHomeButton.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/dcloud_iconfont.ttf"));
            this.mHomeButton.setText("");
            this.mHomeButton.setIncludeFontPadding(false);
            this.mHomeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    IApp iApp = TitleNView.this.mApp;
                    if (iApp != null && iApp.getActivity() != null) {
                        TitleNView.this.mFrameViewParent.obtainWebAppRootView().goHome(TitleNView.this.mFrameViewParent);
                    }
                }
            });
            if ("transparent".equals(str3)) {
                this.mHomeButton.getPaint().setTextSize((float) PdrUtil.convertToScreenInt("22px", this.mInnerWidth, 0, this.mCreateScale));
                NativeViewBackButtonDrawable nativeViewBackButtonDrawable = new NativeViewBackButtonDrawable(Color.parseColor(TitleNViewUtil.TRANSPARENT_BUTTON_BACKGROUND_COLOR));
                nativeViewBackButtonDrawable.setWidth("");
                this.mHomeButton.setBackground(nativeViewBackButtonDrawable);
                str = TitleNViewUtil.TRANSPARENT_BUTTON_TEXT_COLOR;
                str2 = TitleNViewUtil.changeColorAlpha(str, 0.3f);
            } else {
                this.mHomeButton.getPaint().setTextSize((float) PdrUtil.convertToScreenInt("27px", this.mInnerWidth, 0, this.mCreateScale));
            }
            try {
                i = Color.parseColor(str);
            } catch (Exception unused) {
                i = PdrUtil.stringToColor(str);
            }
            try {
                i2 = Color.parseColor(str2);
            } catch (Exception unused2) {
                i2 = PdrUtil.stringToColor(str2);
            }
            this.mHomeButton.setTextColor(createColorStateList(i, i2));
        }
        int convertToScreenInt = this.mInnerHeight - PdrUtil.convertToScreenInt("12px", this.mAppScreenWidth, 0, this.mCreateScale);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(convertToScreenInt, convertToScreenInt);
        if (this.mHomeButton.getParent() != null) {
            ((ViewGroup) this.mHomeButton.getParent()).removeView(this.mHomeButton);
        }
        if (this.mRightButtonLayout.getChildCount() == 0) {
            layoutParams.rightMargin = PdrUtil.convertToScreenInt("5px", this.mAppScreenWidth, 0, this.mCreateScale);
            this.mRightButtonLayout.addView(this.mHomeButton, 0, layoutParams);
        } else if (this.mRightButtonLayout.getChildCount() == 1 && -1 == this.mRightButtonLayout.indexOfChild(this.mHomeButton)) {
            layoutParams.rightMargin = PdrUtil.convertToScreenInt("5px", this.mAppScreenWidth, 0, this.mCreateScale);
            this.mRightButtonLayout.addView(this.mHomeButton, 0, layoutParams);
        } else if (this.mRightButtonLayout.getChildCount() == 2 && -1 == this.mRightButtonLayout.indexOfChild(this.mHomeButton)) {
            if (this.mLeftButtonLayout.getChildCount() == 1 && this.mBackButton != null && -1 == this.mLeftButtonLayout.indexOfChild(this.mHomeButton)) {
                layoutParams.leftMargin = PdrUtil.convertToScreenInt("5px", this.mAppScreenWidth, 0, this.mCreateScale);
                this.mLeftButtonLayout.addView(this.mHomeButton, 1, layoutParams);
            } else if (this.mLeftButtonLayout.getChildCount() == 2 && -1 == this.mLeftButtonLayout.indexOfChild(this.mHomeButton)) {
                LinearLayout linearLayout = this.mLeftButtonLayout;
                linearLayout.removeView(linearLayout.getChildAt(1));
                layoutParams.leftMargin = PdrUtil.convertToScreenInt("5px", this.mAppScreenWidth, 0, this.mCreateScale);
                this.mLeftButtonLayout.addView(this.mHomeButton, 1, layoutParams);
            }
        }
        this.mHomeButton.setVisibility(0);
        requestLayout();
        caculateTitleMargin();
    }

    public void addLeftButton(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, IWebview iWebview, String str11, String str12, boolean z, String str13, boolean z2, String str14) {
        int i;
        String str15 = str9;
        View createButton = createButton(str, str3, str4, str5, str6, str7, str8, str10, iWebview, str11, str12, z, str13, z2, str14, str9);
        initTitleNViewLayout();
        initLeftButtonLayout();
        if (-1 == this.mLeftButtonLayout.indexOfChild(createButton)) {
            int convertToScreenInt = this.mInnerHeight - PdrUtil.convertToScreenInt("12px", this.mAppScreenWidth, 0, this.mCreateScale);
            int convertToScreenInt2 = PdrUtil.convertToScreenInt(str14, this.mAppScreenWidth, -1, this.mCreateScale);
            String str16 = str9;
            int convertToScreenInt3 = PdrUtil.convertToScreenInt(str16, this.mAppScreenWidth, -1, this.mCreateScale);
            String str17 = "5px";
            if (convertToScreenInt2 <= 0 || convertToScreenInt3 <= convertToScreenInt2) {
                if (PdrUtil.isEmpty(str9)) {
                    i = convertToScreenInt;
                } else if (!str16.equals("auto")) {
                    i = PdrUtil.convertToScreenInt(str16, this.mAppScreenWidth, convertToScreenInt, this.mCreateScale);
                    str17 = "0px";
                }
                int convertToScreenInt4 = PdrUtil.convertToScreenInt(str17, this.mAppScreenWidth, 0, this.mCreateScale);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(i, convertToScreenInt);
                layoutParams.leftMargin = convertToScreenInt4;
                this.mLeftButtonLayout.addView(createButton, layoutParams);
                if (!PdrUtil.isEmpty(str9) && str16.equals("auto") && z2) {
                    createButton.setPadding(convertToScreenInt4, createButton.getPaddingTop(), convertToScreenInt4, createButton.getPaddingBottom());
                }
            }
            i = -2;
            int convertToScreenInt42 = PdrUtil.convertToScreenInt(str17, this.mAppScreenWidth, 0, this.mCreateScale);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(i, convertToScreenInt);
            layoutParams2.leftMargin = convertToScreenInt42;
            this.mLeftButtonLayout.addView(createButton, layoutParams2);
            createButton.setPadding(convertToScreenInt42, createButton.getPaddingTop(), convertToScreenInt42, createButton.getPaddingBottom());
        }
        caculateTitleMargin();
    }

    public void addRightButton(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, IWebview iWebview, String str11, String str12, boolean z, String str13, boolean z2, String str14) {
        int i;
        String str15 = str9;
        initTitleNViewLayout();
        initRightButtonLayout();
        if ((this.maxButton <= 2 || this.mRightButtonLayout.getChildCount() > this.maxButton - 2) && this.mMenuButtons != null) {
            String str16 = null;
            if (iWebview != null) {
                str16 = iWebview.getWebviewUUID();
            }
            ButtonDataItem buttonDataItem = new ButtonDataItem(str, str2, str16, str7, str8, str10);
            this.mMenuButtons.add(buttonDataItem);
            this.mButtons.add(buttonDataItem);
            return;
        }
        View createButton = createButton(str, str3, str4, str5, str6, str7, str8, str10, iWebview, str11, str12, z, str13, z2, str14, str9);
        if (-1 == this.mRightButtonLayout.indexOfChild(createButton)) {
            int convertToScreenInt = this.mInnerHeight - PdrUtil.convertToScreenInt("12px", this.mAppScreenWidth, 0, this.mCreateScale);
            int convertToScreenInt2 = PdrUtil.convertToScreenInt(str14, this.mAppScreenWidth, -1, this.mCreateScale);
            String str17 = str9;
            int convertToScreenInt3 = PdrUtil.convertToScreenInt(str17, this.mAppScreenWidth, -1, this.mCreateScale);
            String str18 = "5px";
            if (convertToScreenInt2 <= 0 || convertToScreenInt3 <= convertToScreenInt2) {
                if (PdrUtil.isEmpty(str9)) {
                    i = convertToScreenInt;
                } else if (!str17.equals("auto")) {
                    i = PdrUtil.convertToScreenInt(str17, this.mAppScreenWidth, convertToScreenInt, this.mCreateScale);
                    str18 = "0px";
                }
                int convertToScreenInt4 = PdrUtil.convertToScreenInt(str18, this.mAppScreenWidth, 0, this.mCreateScale);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(i, convertToScreenInt);
                layoutParams.rightMargin = convertToScreenInt4;
                this.mRightButtonLayout.addView(createButton, 0, layoutParams);
                if (!PdrUtil.isEmpty(str9) && str17.equals("auto") && z2) {
                    createButton.setPadding(convertToScreenInt4, createButton.getPaddingTop(), convertToScreenInt4, createButton.getPaddingBottom());
                }
            }
            i = -2;
            int convertToScreenInt42 = PdrUtil.convertToScreenInt(str18, this.mAppScreenWidth, 0, this.mCreateScale);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(i, convertToScreenInt);
            layoutParams2.rightMargin = convertToScreenInt42;
            this.mRightButtonLayout.addView(createButton, 0, layoutParams2);
            createButton.setPadding(convertToScreenInt42, createButton.getPaddingTop(), convertToScreenInt42, createButton.getPaddingBottom());
        }
        caculateTitleMargin();
    }

    public void addSearchInput(String str, String str2, String str3, String str4, String str5, String str6, boolean z, boolean z2, IWebview iWebview) {
        boolean z3;
        final TextView textView;
        int i;
        int i2;
        int i3;
        int i4;
        String str7 = str;
        final boolean z4 = z2;
        final IWebview iWebview2 = iWebview;
        initTitleNViewLayout();
        initLeftButtonLayout();
        initRightButtonLayout();
        initCenterSearchLayout();
        RelativeLayout relativeLayout = this.mTitlelayout;
        if (relativeLayout != null) {
            relativeLayout.setVisibility(4);
        }
        Typeface createFromAsset = Typeface.createFromAsset(getContext().getAssets(), "fonts/dcloud_iconfont.ttf");
        int convertToScreenInt = PdrUtil.convertToScreenInt("13px", this.mInnerWidth, 0, this.mCreateScale);
        int convertToScreenInt2 = PdrUtil.convertToScreenInt("8px", this.mAppScreenWidth, 0, this.mCreateScale);
        if (this.searchInput == null) {
            TextView textView2 = new TextView(getContext());
            textView2.setGravity(17);
            textView2.setTypeface(createFromAsset);
            textView2.setText(" ");
            textView2.setIncludeFontPadding(false);
            float f = (float) convertToScreenInt;
            textView2.getPaint().setTextSize(f);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -1);
            layoutParams.leftMargin = convertToScreenInt2;
            this.centerSearchLayout.addView(textView2, layoutParams);
            EditText editText = new EditText(getContext());
            this.searchInput = editText;
            editText.setTag("TitleNView.SearchInput");
            int i5 = convertToScreenInt2 / 2;
            this.searchInput.setPadding(0, i5, 0, i5);
            this.searchInput.setPaddingRelative(0, i5, 0, i5);
            this.searchInput.setIncludeFontPadding(false);
            this.searchInput.setGravity(17);
            this.searchInput.setSingleLine();
            this.searchInput.setLines(1);
            this.searchInput.setTypeface(createFromAsset);
            this.searchInput.setImeOptions(3);
            this.searchInput.getPaint().setTextSize(f);
            this.searchInput.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
            z3 = true;
        } else {
            z3 = false;
        }
        initTitleNViewLayout();
        if (this.searchInput.getParent() != null) {
            ((ViewGroup) this.searchInput.getParent()).removeView(this.searchInput);
        }
        if (-1 == this.centerSearchLayout.indexOfChild(this.searchInput)) {
            this.centerSearchLayout.addView(this.searchInput, 1, new LinearLayout.LayoutParams(-1, -2, 1.0f));
        }
        TextView textView3 = this.centerSearchLayout.getChildAt(0) instanceof TextView ? (TextView) this.centerSearchLayout.getChildAt(0) : null;
        if (textView3 != null) {
            if (str7.equalsIgnoreCase("left")) {
                textView3.setVisibility(0);
            } else if (this.searchInput.getText() == null || this.searchInput.getText().toString().length() <= 0) {
                textView3.setVisibility(8);
            } else {
                textView3.setVisibility(0);
            }
        }
        if (this.centerSearchLayout.getChildCount() > 2) {
            textView = (TextView) this.centerSearchLayout.getChildAt(2);
        } else {
            TextView textView4 = new TextView(getContext());
            textView4.setGravity(17);
            textView4.setTypeface(createFromAsset);
            textView4.setText("");
            textView4.setTextColor(-1);
            textView4.setTextSize(0, (float) convertToScreenInt);
            textView4.setIncludeFontPadding(false);
            textView4.getPaint().setFakeBoldText(true);
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setCornerRadius(100.0f);
            gradientDrawable.setColor(-4802890);
            textView4.setBackground(gradientDrawable);
            int i6 = convertToScreenInt2 / 4;
            textView4.setPadding(i6, i6, i6, i6);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
            layoutParams2.rightMargin = convertToScreenInt2;
            this.centerSearchLayout.addView(textView4, 2, layoutParams2);
            textView4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    TitleNView.this.searchInput.setText("");
                }
            });
            textView4.setVisibility(8);
            textView = textView4;
        }
        if (PdrUtil.isEmpty(str2)) {
            i = Color.parseColor("#8fffffff");
        } else {
            try {
                i = Color.parseColor(str2);
            } catch (Exception unused) {
                i = PdrUtil.stringToColor(str2);
            }
        }
        if (PdrUtil.isEmpty(str3)) {
            i2 = 0;
        } else {
            i2 = PdrUtil.convertToScreenInt(str3, this.mInnerWidth, 0, this.mCreateScale);
        }
        String str8 = !PdrUtil.isEmpty(str4) ? str4 : "";
        this.searchInput.setHint(" " + str8);
        if (PdrUtil.isEmpty(str5)) {
            i3 = Color.parseColor("#CCCCCC");
        } else {
            try {
                i3 = Color.parseColor(str5);
            } catch (Exception unused2) {
                i3 = PdrUtil.stringToColor(str5);
            }
        }
        if (textView3 != null) {
            textView3.setTextColor(i3);
        }
        this.searchInput.setHintTextColor(i3);
        this.searchInput.setEllipsize(TextUtils.TruncateAt.END);
        this.searchInput.setBackground((Drawable) null);
        this.centerSearchLayout.setBackground(new SearchInputDrawable(i, i2));
        if (PdrUtil.isEmpty(str6)) {
            i4 = -16777216;
        } else {
            try {
                i4 = Color.parseColor(str6);
            } catch (Exception unused3) {
                i4 = PdrUtil.stringToColor(str6);
            }
        }
        this.searchInput.setTextColor(i4);
        if (z3) {
            this.searchInput.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable editable) {
                }

                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    if (charSequence.toString().length() > 0) {
                        textView.setVisibility(0);
                    } else {
                        textView.setVisibility(8);
                    }
                    String jsResponseText = JSUtil.toJsResponseText(charSequence.toString());
                    IWebview iWebview = TitleNView.this.mWebView;
                    if (iWebview == null || iWebview.obtainFrameView() == null || !(TitleNView.this.mWebView.obtainFrameView() instanceof AdaFrameView)) {
                        IWebview iWebview2 = iWebview2;
                        if (iWebview2 != null && iWebview2.obtainFrameView() != null && (iWebview2.obtainFrameView() instanceof AdaFrameView)) {
                            ((AdaFrameView) iWebview2.obtainFrameView()).dispatchFrameViewEvents(AbsoluteConst.EVENT_TITLENVIEW_SEARCHINPUT_CHANGED, StringUtil.format("{text:\"%s\"}", jsResponseText));
                            return;
                        }
                        return;
                    }
                    ((AdaFrameView) TitleNView.this.mWebView.obtainFrameView()).dispatchFrameViewEvents(AbsoluteConst.EVENT_TITLENVIEW_SEARCHINPUT_CHANGED, StringUtil.format("{text:\"%s\"}", jsResponseText));
                }
            });
        }
        final IWebview iWebview3 = iWebview;
        final TextView textView5 = textView3;
        final String str9 = str8;
        final String str10 = str;
        this.searchInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
                if (!TitleNView.this.isSetText.get()) {
                    IWebview iWebview = TitleNView.this.mWebView;
                    if (!(iWebview == null || iWebview.obtainFrameView() == null || !(TitleNView.this.mWebView.obtainFrameView() instanceof AdaFrameView))) {
                        ((AdaFrameView) TitleNView.this.mWebView.obtainFrameView()).dispatchFrameViewEvents(AbsoluteConst.EVENT_TITLENVIEW_SEARCHINPUT_FOCUSCHANGED, StringUtil.format("{focus:%b}", Boolean.valueOf(z)));
                    }
                    IWebview iWebview2 = iWebview3;
                    if (!(iWebview2 == null || iWebview2.obtainFrameView() == null || !(iWebview3.obtainFrameView() instanceof AdaFrameView))) {
                        ((AdaFrameView) iWebview3.obtainFrameView()).dispatchFrameViewEvents(AbsoluteConst.EVENT_TITLENVIEW_SEARCHINPUT_FOCUSCHANGED, StringUtil.format("{focus:%b}", Boolean.valueOf(z)));
                    }
                }
                if (z) {
                    TitleNView.this.searchInput.setGravity(3);
                    textView5.setVisibility(0);
                    TitleNView.this.searchInput.setHint(str9);
                } else if (TitleNView.this.searchInput.getText().toString().length() < 1) {
                    TitleNView.this.setTextGravity(str10, str9);
                    if (str10.equalsIgnoreCase("left")) {
                        textView5.setVisibility(0);
                    } else {
                        textView5.setVisibility(8);
                    }
                }
            }
        });
        this.searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 3) {
                    String jsResponseText = JSUtil.toJsResponseText(textView.getText().toString());
                    IWebview iWebview = TitleNView.this.mWebView;
                    if (iWebview == null || iWebview.obtainFrameView() == null || !(TitleNView.this.mWebView.obtainFrameView() instanceof AdaFrameView)) {
                        IWebview iWebview2 = iWebview2;
                        if (!(iWebview2 == null || iWebview2.obtainFrameView() == null || !(iWebview2.obtainFrameView() instanceof AdaFrameView))) {
                            ((AdaFrameView) iWebview2.obtainFrameView()).dispatchFrameViewEvents(AbsoluteConst.EVENT_TITLENVIEW_SEARCHINPUT_CONFIRMED, StringUtil.format("{text:\"%s\"}", jsResponseText));
                        }
                    } else {
                        ((AdaFrameView) TitleNView.this.mWebView.obtainFrameView()).dispatchFrameViewEvents(AbsoluteConst.EVENT_TITLENVIEW_SEARCHINPUT_CONFIRMED, StringUtil.format("{text:\"%s\"}", jsResponseText));
                        return true;
                    }
                }
                return false;
            }
        });
        if (this.searchInput.getText() == null || this.searchInput.getText().toString().length() < 1) {
            setTextGravity(str7, str8);
        }
        boolean z5 = !z;
        this.searchInput.setCursorVisible(z5);
        this.searchInput.setFocusable(z5);
        this.searchInput.setFocusableInTouchMode(z5);
        final IWebview iWebview4 = this.mWebView;
        if (iWebview4 == null) {
            iWebview4 = iWebview2;
        }
        try {
            final boolean didCloseSplash = ((IWebAppRootView) ((AdaFrameView) iWebview4.obtainFrameView()).getParent()).didCloseSplash();
            if (!didCloseSplash) {
                iWebview4.obtainApp().registerSysEventListener(new ISysEventListener() {
                    public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
                        ISysEventListener.SysEventType sysEventType2;
                        if (z4 && sysEventType == (sysEventType2 = ISysEventListener.SysEventType.onSplashclosed)) {
                            TitleNView.this.searchInput.requestFocus();
                            if (!DeviceInfo.isIMEShow) {
                                DeviceInfo.showIME(TitleNView.this.searchInput);
                            }
                            iWebview4.obtainApp().unregisterSysEventListener(this, sysEventType2);
                        }
                        if (z4) {
                            return false;
                        }
                        DeviceInfo.hideIME(TitleNView.this.searchInput);
                        return false;
                    }
                }, ISysEventListener.SysEventType.onSplashclosed);
            }
            iWebview4.obtainFrameView().addFrameViewListener(new IEventCallback() {
                public Object onCallBack(String str, Object obj) {
                    if (!AbsoluteConst.EVENTS_SHOW_ANIMATION_END.equalsIgnoreCase(str) || !z4 || !didCloseSplash) {
                        return null;
                    }
                    TitleNView.this.searchInput.requestFocus();
                    if (!DeviceInfo.isIMEShow) {
                        DeviceInfo.showIME(TitleNView.this.searchInput);
                    }
                    iWebview4.obtainFrameView().removeFrameViewListener(this);
                    return null;
                }
            });
        } catch (Exception unused4) {
        }
        if (!z4) {
            DeviceInfo.hideIME(this.searchInput);
        }
        if (z) {
            this.searchInput.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    IWebview iWebview = TitleNView.this.mWebView;
                    if (iWebview == null || iWebview.obtainFrameView() == null || !(TitleNView.this.mWebView.obtainFrameView() instanceof AdaFrameView)) {
                        IWebview iWebview2 = iWebview2;
                        if (iWebview2 != null && iWebview2.obtainFrameView() != null && (iWebview2.obtainFrameView() instanceof AdaFrameView)) {
                            ((AdaFrameView) iWebview2.obtainFrameView()).dispatchFrameViewEvents(AbsoluteConst.EVENT_TITLENVIEW_SEARCHINPUT_CLICKED, String.format(Operators.SPACE_STR, new Object[0]));
                            return;
                        }
                        return;
                    }
                    ((AdaFrameView) TitleNView.this.mWebView.obtainFrameView()).dispatchFrameViewEvents(AbsoluteConst.EVENT_TITLENVIEW_SEARCHINPUT_CLICKED, String.format(Operators.SPACE_STR, new Object[0]));
                }
            });
        }
        this.searchInput.setVisibility(0);
        requestLayout();
    }

    public void clearButtons() {
        LinearLayout linearLayout = this.mRightButtonLayout;
        if (linearLayout != null) {
            linearLayout.removeAllViews();
        }
        LinearLayout linearLayout2 = this.mLeftButtonLayout;
        if (linearLayout2 != null) {
            linearLayout2.removeAllViews();
        }
        ArrayList<ButtonDataItem> arrayList = this.mMenuButtons;
        if (arrayList != null) {
            arrayList.clear();
        }
        ArrayList<Object> arrayList2 = this.mButtons;
        if (arrayList2 != null) {
            arrayList2.clear();
        }
    }

    public void clearNativeViewData() {
        IFrameView iFrameView = this.mFrameViewParent;
        if (iFrameView != null && (iFrameView instanceof AdaFrameView)) {
            ((AdaFrameView) iFrameView).removeNativeViewChild(this);
        }
        postDelayed(new Runnable() {
            public void run() {
                try {
                    TitleNView.this.setVisibility(8);
                    if (TitleNView.this.getParent() != null) {
                        ((ViewGroup) TitleNView.this.getParent()).removeView(TitleNView.this);
                    }
                    TitleNView.this.clearViewData();
                    if (TitleNView.this.mButtons != null) {
                        Iterator it = TitleNView.this.mButtons.iterator();
                        while (it.hasNext()) {
                            Object next = it.next();
                            if (next instanceof BadgeRelateiveLayout) {
                                View view = (View) next;
                                if (view.getBackground() != null) {
                                    view.getBackground().setCallback((Drawable.Callback) null);
                                }
                            }
                        }
                        TitleNView.this.mButtons.clear();
                    }
                    TitleNView.this.mFrameViewParent = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, (long) 200);
    }

    public void clearSearchInput() {
        LinearLayout linearLayout = this.centerSearchLayout;
        if (linearLayout != null && this.mTitleNViewLayout != null) {
            linearLayout.removeAllViews();
            this.mTitleNViewLayout.removeView(this.centerSearchLayout);
            this.centerSearchLayout = null;
            this.searchInput = null;
            RelativeLayout relativeLayout = this.mTitlelayout;
            if (relativeLayout != null) {
                relativeLayout.setVisibility(0);
            }
        }
    }

    public String getBackgroundColor() {
        return getStyleBackgroundColor();
    }

    public String getStatusBarColor() {
        JSONObject optJSONObject;
        int i;
        if (!this.isImmersed) {
            return "{color:-1,alpha:true}";
        }
        if (!this.mStyle.has(AbsoluteConst.JSONKEY_STATUSBAR) || (optJSONObject = this.mStyle.optJSONObject(AbsoluteConst.JSONKEY_STATUSBAR)) == null || !optJSONObject.has("backgroundnoalpha")) {
            View view = this.mStatusbarView;
            if (view != null) {
                Drawable background = view.getBackground();
                if (!(background instanceof ColorDrawable)) {
                    return "{color:-1,alpha:true}";
                }
                int color = ((ColorDrawable) background).getColor();
                return "{color:" + color + ",alpha:true}";
            }
            ViewParent parent = getParent();
            if (!TextUtils.equals(this.mStyle.optString("position", AbsoluteConst.JSON_VALUE_POSITION_ABSOLUTE), AbsoluteConst.JSON_VALUE_POSITION_ABSOLUTE) || parent == null || !(parent instanceof ViewGroup)) {
                return "{color:-1,alpha:true}";
            }
            int i2 = 0;
            while (true) {
                ViewGroup viewGroup = (ViewGroup) parent;
                if (i2 >= viewGroup.getChildCount()) {
                    return "{color:-1,alpha:true}";
                }
                Object tag = viewGroup.getChildAt(i2).getTag();
                if (tag != null && "StatusBar".equalsIgnoreCase(tag.toString())) {
                    Drawable background2 = viewGroup.getChildAt(i2).getBackground();
                    if (background2 instanceof ColorDrawable) {
                        int color2 = ((ColorDrawable) background2).getColor();
                        return "{color:" + color2 + ",alpha:true}";
                    }
                }
                i2++;
            }
        } else {
            String optString = optJSONObject.optString("backgroundnoalpha");
            try {
                i = Color.parseColor(optString);
            } catch (Exception unused) {
                i = PdrUtil.stringToColor(optString);
            }
            return "{color:" + i + ",alpha:false}";
        }
    }

    public int getTitleColor() {
        return this.mTitleView.getTextColors().getDefaultColor();
    }

    public String getTitleNViewSearchInputText() {
        EditText editText = this.searchInput;
        return editText != null ? editText.getText().toString() : "";
    }

    public String getViewType() {
        return AbsoluteConst.NATIVE_TITLE_N_VIEW;
    }

    /* access modifiers changed from: protected */
    public void init() {
        View view;
        super.init();
        if (this.mStyle.has("blurEffect")) {
            this.mBlurEffect = this.mStyle.optString("blurEffect", "none");
        }
        if (this.mBackgroudView == null) {
            View view2 = new View(getContext());
            this.mBackgroudView = view2;
            addView(view2, 0, new FrameLayout.LayoutParams(-1, -1));
        }
        updateCapsuleLayout();
        this.redDotColor = -65536;
        if (this.mStyle != null) {
            if (this.mBackGroundDrawable == null) {
                BackGroundDrawable backGroundDrawable = new BackGroundDrawable();
                this.mBackGroundDrawable = backGroundDrawable;
                this.mBackgroudView.setBackground(backGroundDrawable);
            }
            if (this.mStyle.has(AbsoluteConst.JSONKEY_STATUSBAR) && this.mStyle.has(Constants.Name.BACKGROUND_IMAGE) && (view = this.mStatusbarView) != null) {
                view.setVisibility(8);
            }
            this.mBackGroundDrawable.setBackgroundColor(this.mBackGroundColor);
            this.mBackGroundDrawable.updatebound();
            this.mBackGroundDrawable.setBackgroundImage(this.mBackgroundImageSrc);
            this.mBackGroundDrawable.setAlpha(Color.alpha(this.mBackGroundColor));
            this.mBackGroundDrawable.invalidateSelf();
            if (this.mStyle.has("redDotColor") && !PdrUtil.isEmpty(this.mStyle.optString("redDotColor"))) {
                this.redDotColor = PdrUtil.stringToColor(this.mStyle.optString("redDotColor"));
            }
        }
        if ((DCBlurDraweeView.LIGHT.equals(this.mBlurEffect) || DCBlurDraweeView.DARK.equals(this.mBlurEffect) || DCBlurDraweeView.EXTRALIGHT.equals(this.mBlurEffect)) && this.mBlurDraweeView == null) {
            int i = this.mInnerHeight;
            if (this.isStatusBar && this.isImmersed) {
                i += DeviceInfo.sStatusBarHeight;
            }
            if (this.mWebView.obtainWindowView() instanceof IX5WebView) {
                this.mBlurDraweeView = new DCBlurDraweeView(getContext(), false, "none");
            } else {
                this.mBlurDraweeView = new DCBlurDraweeView(getContext(), true, DCBlurDraweeView.SEMI_AUTOMATICALLY);
            }
            addView(this.mBlurDraweeView, 0, new FrameLayout.LayoutParams(-1, i));
            this.mBlurDraweeView.setGravityType(48);
            this.mBlurDraweeView.setBlurEffect(this.mBlurEffect);
            this.mBlurDraweeView.setBlurRadius(20);
            this.mBlurDraweeView.setDownscaleFactor(0.3f);
            this.mBlurDraweeView.setRootView(this.mWebView.obtainFrameView().obtainMainView());
            this.mBlurDraweeView.setBlurLayoutChangeCallBack(new DCBlurDraweeView.BlurLayoutChangeCallBack() {
                public void setVisibility(int i) {
                    NativeView.NativeCanvasView nativeCanvasView = TitleNView.this.mCanvasView;
                    if (nativeCanvasView != null) {
                        nativeCanvasView.setVisibility(i);
                    }
                    if (TitleNView.this.mTitleNViewLayout != null) {
                        TitleNView.this.mTitleNViewLayout.setVisibility(i);
                    }
                    View view = TitleNView.this.mStatusbarView;
                    if (!(view == null || view.getVisibility() == 8)) {
                        TitleNView.this.mStatusbarView.setVisibility(i);
                    }
                    if (TitleNView.this.mBackgroudView != null) {
                        TitleNView.this.mBackgroudView.setVisibility(i);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void measureChildViewToTop(int i) {
        super.measureChildViewToTop(i);
        RelativeLayout relativeLayout = this.mTitleNViewLayout;
        if (relativeLayout != null && relativeLayout.getLayoutParams() != null) {
            ((FrameLayout.LayoutParams) this.mTitleNViewLayout.getLayoutParams()).topMargin = i;
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        caculateTitleMargin();
    }

    public void reMeasure() {
        measureFitViewParent(false);
    }

    public void removeBackButton() {
        BadgeLinearLayout badgeLinearLayout = this.mBackButton;
        if (badgeLinearLayout != null) {
            badgeLinearLayout.setVisibility(8);
        }
    }

    public void removeHomeButton() {
        TextView textView = this.mHomeButton;
        if (textView != null) {
            textView.setVisibility(8);
        }
    }

    public void removeSplitLine() {
        RelativeLayout relativeLayout;
        View view = this.mSplitLine;
        if (!(view == null || (relativeLayout = this.mTitleNViewLayout) == null)) {
            relativeLayout.removeView(view);
        }
        this.mSplitLine = null;
    }

    public void resetNativeView() {
        int i;
        IFrameView iFrameView = this.mFrameViewParent;
        if (iFrameView != null) {
            try {
                String titleNViewId = TitleNViewUtil.getTitleNViewId(iFrameView);
                if (this.mOverlayMaps.containsKey(titleNViewId)) {
                    i = this.mOverlayMaps.get(titleNViewId).intValue();
                    this.mOverlayMaps.clear();
                    this.mOverlayMaps.put(titleNViewId, Integer.valueOf(i));
                } else {
                    this.mOverlayMaps.clear();
                    i = -1;
                }
                if (-1 != i) {
                    NativeView.Overlay overlay = this.mOverlays.get(i);
                    Iterator<NativeView.Overlay> it = this.mOverlays.iterator();
                    while (it.hasNext()) {
                        NativeView.Overlay next = it.next();
                        if (next != overlay) {
                            NativeBitmap nativeBitmap = next.mNativeBitmap;
                            if (nativeBitmap != null) {
                                nativeBitmap.recycle(true);
                            }
                            it.remove();
                        }
                    }
                } else {
                    clearViewData();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            clearViewData();
        }
        clearAnimate();
    }

    public void setBackButtonColor(String str, String str2, String str3) {
        setButtonColor(this.mBackButton, str, str2, str3);
    }

    public void setBackgroundColor(String str) {
        setStyleBackgroundColor(str);
        updateCapsuleLayout();
    }

    public void setBackgroundImage(String str) {
        if (!this.mBackgroundImageSrc.equals(str)) {
            this.mBackgroundImageSrc = str;
            if (this.mBackGroundDrawable == null) {
                this.mBackGroundDrawable = new BackGroundDrawable();
            }
            this.mBackGroundDrawable.setBackgroundImage(this.mBackgroundImageSrc);
            this.mBackgroudView.setBackground(this.mBackGroundDrawable);
            this.mBackGroundDrawable.invalidateSelf();
        }
    }

    public void setBackgroundRepeat(String str) {
        BackGroundDrawable backGroundDrawable = this.mBackGroundDrawable;
        if (backGroundDrawable != null) {
            backGroundDrawable.setRepeatType(str, this.mBackgroundImageSrc);
        }
    }

    public void setBadgeText(JSONObject jSONObject, boolean z) {
        int optInt = jSONObject.optInt("index");
        String optString = jSONObject.optString("text");
        if (!z) {
            optString = "";
        }
        if (optInt < this.mButtons.size() && optInt >= 0) {
            Object obj = this.mButtons.get(optInt);
            if (obj instanceof BadgeRelateiveLayout) {
                ((BadgeRelateiveLayout) obj).setBadgeStr(optString);
            }
        }
    }

    public void setButtonColorByIndex(int i, String str, String str2, String str3) {
        Object obj = this.mButtons.get(i);
        if (obj != null && (obj instanceof View)) {
            setButtonColor((View) obj, str, str2, str3);
        }
    }

    public void setButtonsColor(String str, String str2, String str3) {
        for (int i = 0; i < this.mButtons.size(); i++) {
            setButtonColorByIndex(i, str, str2, str3);
        }
    }

    public void setCapsuleButtonStyle(JSONObject jSONObject) {
        CapsuleLayout capsuleLayout = this.mCapsuleLayout;
        if (capsuleLayout != null && jSONObject != null) {
            capsuleLayout.isDiy = true;
            if (jSONObject.has("backgroundColor")) {
                this.mCapsuleLayout.setBackgroundColor(PdrUtil.stringToColor(jSONObject.optString("backgroundColor")));
            }
            if (jSONObject.has(Constants.Name.BORDER_COLOR)) {
                this.mCapsuleLayout.setRoundColor(PdrUtil.stringToColor(jSONObject.optString(Constants.Name.BORDER_COLOR)));
            }
            if (jSONObject.has("highlightColor")) {
                int stringToColor = PdrUtil.stringToColor(jSONObject.optString("highlightColor"));
                TextView textView = this.menuBt;
                if (textView != null) {
                    this.mCapsuleLayout.setButtonSelectColor(textView, CapsuleLayout.ButtonType.LIFT, stringToColor);
                }
                TextView textView2 = this.closeBt;
                if (textView2 != null) {
                    this.mCapsuleLayout.setButtonSelectColor(textView2, CapsuleLayout.ButtonType.RIGHT, stringToColor);
                }
            }
            if (jSONObject.has("textColor")) {
                int stringToColor2 = PdrUtil.stringToColor(jSONObject.optString("textColor"));
                TextView textView3 = this.menuBt;
                if (textView3 != null) {
                    textView3.setTextColor(stringToColor2);
                }
                TextView textView4 = this.closeBt;
                if (textView4 != null) {
                    textView4.setTextColor(stringToColor2);
                }
            }
        }
    }

    public void setHomeButtonColor(String str, String str2, String str3) {
        setButtonColor(this.mHomeButton, str, str2, str3);
    }

    public void setIconSubTitleStyle(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) {
        ImageView imageView;
        TextView textView;
        RelativeLayout relativeLayout = this.mTitlelayout;
        if (relativeLayout != null) {
            if (relativeLayout.getChildAt(0) instanceof ImageView) {
                imageView = (ImageView) this.mTitlelayout.getChildAt(0);
            } else {
                imageView = new ImageView(getContext());
                imageView.setId(View.generateViewId());
            }
            ImageView imageView2 = imageView;
            if (this.mTitlelayout.getChildAt(2) instanceof TextView) {
                textView = (TextView) this.mTitlelayout.getChildAt(2);
            } else {
                textView = new TextView(getContext());
                textView.setId(View.generateViewId());
            }
            layoutSubtitleIcon(str, str2, str3, str4, str5, str6, imageView2, textView, PdrUtil.stringToColor(str7), str8, str9);
        }
    }

    public void setProgress(String str, String str2) {
        int i;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            Progress progress = this.mProgress;
            if (progress == null) {
                return;
            }
            if (!progress.isFinish()) {
                this.mProgress.finishProgress();
            } else {
                this.mProgress.setVisibility(0);
            }
        } else {
            if (this.mProgress == null) {
                Progress progress2 = new Progress(getContext());
                this.mProgress = progress2;
                progress2.setTag("TitleNView.Progress");
            }
            try {
                i = Color.parseColor(str2);
            } catch (Exception unused) {
                i = PdrUtil.stringToColor(str2);
            }
            this.mProgress.setColorInt(i);
            this.mProgress.setHeightInt(PdrUtil.convertToScreenInt(str, this.mInnerWidth, 0, this.mCreateScale));
        }
    }

    public void setRedDot(JSONObject jSONObject, boolean z) {
        int optInt = jSONObject.optInt("index");
        if (optInt < this.mButtons.size() && optInt >= 0) {
            Object obj = this.mButtons.get(optInt);
            if (obj instanceof BadgeRelateiveLayout) {
                ((BadgeRelateiveLayout) obj).setDrawRedDot(z);
            }
        }
    }

    public void setRedDotColor(int i) {
        if (this.redDotColor != i) {
            this.redDotColor = i;
            this.mBackButton.setRedDotColor(i);
            if (this.mButtons.size() > 0) {
                Iterator<Object> it = this.mButtons.iterator();
                while (it.hasNext()) {
                    Object next = it.next();
                    if (next instanceof BadgeRelateiveLayout) {
                        ((BadgeRelateiveLayout) next).setRedDotColor(this.redDotColor);
                    }
                }
            }
        }
    }

    public void setSearchInputColor(String str) {
        Drawable background;
        int i;
        LinearLayout linearLayout = this.centerSearchLayout;
        if (linearLayout != null && (background = linearLayout.getBackground()) != null && (background instanceof SearchInputDrawable)) {
            try {
                i = Color.parseColor(str);
            } catch (Exception unused) {
                i = PdrUtil.stringToColor(str);
            }
            SearchInputDrawable searchInputDrawable = (SearchInputDrawable) background;
            if (i != searchInputDrawable.getDrawableColor()) {
                searchInputDrawable.setDrawableColor(i);
            }
        }
    }

    public void setSearchInputFocus(boolean z) {
        EditText editText = this.searchInput;
        if (editText == null) {
            return;
        }
        if (z) {
            DeviceInfo.hideIME(editText);
            if (this.searchInput.requestFocus()) {
                DeviceInfo.showIME(this.searchInput);
                return;
            }
            return;
        }
        editText.clearFocus();
        DeviceInfo.hideIME(this.searchInput);
    }

    public void setShadow(JSONObject jSONObject) {
        BackGroundDrawable backGroundDrawable = this.mBackGroundDrawable;
        if (backGroundDrawable != null) {
            backGroundDrawable.setShadowColor(jSONObject);
        }
    }

    public void setSplitLine(String str, String str2) {
        int i;
        if (this.mSplitLine == null) {
            View view = new View(getContext());
            this.mSplitLine = view;
            view.setTag("TitleNView.SplitLine");
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
            layoutParams.addRule(12);
            this.mSplitLine.setLayoutParams(layoutParams);
        }
        initTitleNViewLayout();
        RelativeLayout relativeLayout = this.mTitleNViewLayout;
        if (relativeLayout != null && -1 == relativeLayout.indexOfChild(this.mSplitLine)) {
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -2);
            layoutParams2.addRule(12);
            this.mTitleNViewLayout.addView(this.mSplitLine, layoutParams2);
        }
        try {
            i = Color.parseColor(str2);
        } catch (Exception unused) {
            i = PdrUtil.stringToColor(str2);
        }
        this.mSplitLine.setBackgroundColor(i);
        this.mSplitLine.getLayoutParams().height = PdrUtil.convertToScreenInt(str, this.mInnerWidth, 0, this.mCreateScale);
        this.mSplitLine.requestLayout();
        this.mSplitLine.invalidate();
    }

    public void setStatusBarColor(int i) {
        if (!this.isImmersed) {
            return;
        }
        if (this.mStatusbarView == null) {
            ViewParent parent = getParent();
            if (TextUtils.equals(this.mStyle.optString("position", AbsoluteConst.JSON_VALUE_POSITION_ABSOLUTE), AbsoluteConst.JSON_VALUE_POSITION_ABSOLUTE) && parent != null && (parent instanceof ViewGroup)) {
                int i2 = 0;
                while (true) {
                    ViewGroup viewGroup = (ViewGroup) parent;
                    if (i2 < viewGroup.getChildCount()) {
                        View childAt = viewGroup.getChildAt(i2);
                        if (childAt.getTag() == null || !"StatusBar".equalsIgnoreCase(childAt.getTag().toString())) {
                            i2++;
                        } else {
                            childAt.setBackgroundColor(i);
                            childAt.invalidate();
                            return;
                        }
                    } else {
                        return;
                    }
                }
            }
        } else if (TextUtils.isEmpty(this.mBackgroundImageSrc) || !isStatusBar()) {
            this.mStatusbarView.setBackgroundColor(i);
            this.mStatusbarView.invalidate();
        }
    }

    public void setStyleBackgroundColor(int i) {
        this.mBackGroundDrawable.setBackgroundColor(i);
    }

    public void setTitle(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12) {
        ImageView imageView;
        TextView textView;
        int stringToColor;
        String str13 = str;
        String str14 = str5;
        initTitleNViewLayout();
        initLeftButtonLayout();
        if (this.mTitlelayout == null) {
            this.mTitlelayout = new RelativeLayout(getContext());
        }
        if (this.mTitlelayout.getChildAt(0) instanceof ImageView) {
            imageView = (ImageView) this.mTitlelayout.getChildAt(0);
        } else {
            imageView = new ImageView(getContext());
            imageView.setId(View.generateViewId());
        }
        ImageView imageView2 = imageView;
        if (this.mTitleView == null) {
            TextView textView2 = new TextView(getContext());
            this.mTitleView = textView2;
            textView2.setTag("TitleNView.Title");
            this.mTitleView.setLines(1);
            this.mTitleView.setSingleLine(true);
            this.mTitleView.setIncludeFontPadding(false);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams.addRule(13);
            this.mTitleView.setLayoutParams(layoutParams);
            if ("clip".equals(str4)) {
                this.mTitleView.setEllipsize((TextUtils.TruncateAt) null);
            } else {
                this.mTitleView.setEllipsize(TextUtils.TruncateAt.END);
            }
            this.mTitleView.setId(View.generateViewId());
        }
        if (this.mTitlelayout.getChildAt(2) instanceof TextView) {
            textView = (TextView) this.mTitlelayout.getChildAt(2);
        } else {
            textView = new TextView(getContext());
            textView.setId(View.generateViewId());
        }
        TextView textView3 = textView;
        if (!TextUtils.isEmpty(str) && !str13.equals(this.mTitleView.getText())) {
            this.mTitleView.setText(str13);
        }
        String str15 = str3;
        this.mTitleView.getPaint().setTextSize((float) PdrUtil.convertToScreenInt(str15, this.mInnerWidth, PdrUtil.convertToScreenInt("17px", this.mInnerWidth, 0, this.mCreateScale), this.mCreateScale));
        try {
            stringToColor = Color.parseColor(str2);
        } catch (Exception unused) {
            stringToColor = PdrUtil.stringToColor(str2);
        }
        int i = stringToColor;
        this.mTitleView.setTextColor(i);
        if (-1 == this.mTitleNViewLayout.indexOfChild(this.mTitlelayout)) {
            this.mTitleNViewLayout.addView(this.mTitlelayout);
        }
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
        int convertToScreenInt = PdrUtil.convertToScreenInt("88px", this.mInnerWidth, 0, this.mCreateScale);
        if (str14.equals("left") || str14.equals("auto")) {
            layoutParams2.addRule(1, this.mLeftButtonLayout.getId());
            layoutParams2.addRule(15);
            layoutParams2.removeRule(13);
            layoutParams2.leftMargin = PdrUtil.convertToScreenInt("10px", this.mInnerWidth, 0, this.mCreateScale);
        } else {
            layoutParams2.addRule(13);
            layoutParams2.removeRule(1);
            layoutParams2.removeRule(15);
            layoutParams2.leftMargin = convertToScreenInt;
        }
        layoutSubtitleIcon(str6, str7, str8, str9, str10, str11, imageView2, textView3, i, str5, str12);
        layoutParams2.rightMargin = convertToScreenInt;
        this.mTitlelayout.setLayoutParams(layoutParams2);
        EditText editText = this.searchInput;
        if (editText != null && editText.getVisibility() == 0) {
            this.mTitlelayout.setVisibility(4);
        }
    }

    public void setTitleAlign(String str) {
        RelativeLayout relativeLayout = this.mTitlelayout;
        if (relativeLayout != null) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
            if (str.equals("left") || str.equals("auto")) {
                layoutParams.addRule(1, this.mLeftButtonLayout.getId());
                layoutParams.addRule(15);
                layoutParams.removeRule(13);
                layoutParams.leftMargin = PdrUtil.convertToScreenInt("5px", this.mInnerWidth, 0, this.mCreateScale);
            } else {
                layoutParams.addRule(13);
                layoutParams.removeRule(1);
                layoutParams.removeRule(15);
                layoutParams.leftMargin = layoutParams.rightMargin;
            }
            this.mTitlelayout.setLayoutParams(layoutParams);
        }
    }

    public void setTitleColor(String str) {
        int i;
        TextView textView = this.mTitleView;
        if (textView != null) {
            int defaultColor = textView.getTextColors().getDefaultColor();
            try {
                i = Color.parseColor(str);
            } catch (Exception unused) {
                i = PdrUtil.stringToColor(str);
            }
            if (i != defaultColor) {
                this.mTitleView.setTextColor(i);
            }
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: IfRegionVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Don't wrap MOVE or CONST insns: 0x0150: MOVE  (r2v10 int) = (r17v0 int)
        	at jadx.core.dex.instructions.args.InsnArg.wrapArg(InsnArg.java:164)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.assignInline(CodeShrinkVisitor.java:133)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.checkInline(CodeShrinkVisitor.java:118)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkBlock(CodeShrinkVisitor.java:65)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkMethod(CodeShrinkVisitor.java:43)
        	at jadx.core.dex.visitors.regions.TernaryMod.makeTernaryInsn(TernaryMod.java:122)
        	at jadx.core.dex.visitors.regions.TernaryMod.visitRegion(TernaryMod.java:34)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:73)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterative(DepthRegionTraversal.java:27)
        	at jadx.core.dex.visitors.regions.IfRegionVisitor.visit(IfRegionVisitor.java:31)
        */
    public boolean setTitleNViewButtonStyle(int r20, java.lang.String r21, java.lang.String r22, java.lang.String r23, java.lang.String r24, java.lang.String r25, java.lang.String r26, java.lang.String r27, java.lang.String r28, java.lang.String r29, io.dcloud.common.DHInterface.IWebview r30, java.lang.String r31, java.lang.String r32, java.lang.String r33, java.lang.String r34, java.lang.String r35, java.lang.String r36, java.lang.String r37, java.lang.String r38) {
        /*
            r19 = this;
            r6 = r19
            r0 = r20
            r7 = r29
            r8 = r30
            r9 = r33
            r10 = r35
            java.util.ArrayList<java.lang.Object> r1 = r6.mButtons
            int r1 = r1.size()
            r11 = 0
            if (r0 >= r1) goto L_0x0218
            if (r0 < 0) goto L_0x0218
            java.util.ArrayList<java.lang.Object> r1 = r6.mButtons
            java.lang.Object r0 = r1.get(r0)
            boolean r1 = r0 instanceof android.view.View
            r12 = 1
            if (r1 == 0) goto L_0x0217
            r13 = r0
            android.view.ViewGroup r13 = (android.view.ViewGroup) r13
            int r1 = r13.getChildCount()
            r14 = 0
            if (r1 < r12) goto L_0x01f9
            android.view.View r0 = r13.getChildAt(r11)
            r15 = r0
            android.widget.TextView r15 = (android.widget.TextView) r15
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r23)
            r5 = -1
            if (r0 != 0) goto L_0x005c
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r24)
            if (r0 != 0) goto L_0x005c
            int r0 = android.graphics.Color.parseColor(r23)     // Catch:{ Exception -> 0x0045 }
            goto L_0x0049
        L_0x0045:
            int r0 = io.dcloud.common.util.PdrUtil.stringToColor(r23)
        L_0x0049:
            int r1 = android.graphics.Color.parseColor(r24)     // Catch:{ Exception -> 0x004e }
            goto L_0x0052
        L_0x004e:
            int r1 = io.dcloud.common.util.PdrUtil.stringToColor(r24)
        L_0x0052:
            android.content.res.ColorStateList r2 = r6.createColorStateList(r0, r1)
            r15.setTextColor(r2)
            r4 = r0
            r3 = r1
            goto L_0x005e
        L_0x005c:
            r3 = -1
            r4 = -1
        L_0x005e:
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r25)
            if (r0 != 0) goto L_0x0080
            java.lang.String r0 = "bold"
            r1 = r25
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0076
            android.text.TextPaint r0 = r15.getPaint()
            r0.setFakeBoldText(r12)
            goto L_0x007d
        L_0x0076:
            android.text.TextPaint r0 = r15.getPaint()
            r0.setFakeBoldText(r11)
        L_0x007d:
            r15.postInvalidate()
        L_0x0080:
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r26)
            if (r0 != 0) goto L_0x0094
            int r0 = r6.mInnerWidth
            float r1 = r6.mCreateScale
            r2 = r26
            int r0 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r2, r0, r11, r1)
            float r0 = (float) r0
            r15.setTextSize(r11, r0)
        L_0x0094:
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r28)
            if (r0 == 0) goto L_0x00a6
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r21)
            if (r0 != 0) goto L_0x00a1
            goto L_0x00a6
        L_0x00a1:
            r17 = r3
            r18 = r4
            goto L_0x00bc
        L_0x00a6:
            boolean r16 = io.dcloud.common.util.PdrUtil.isEmpty(r28)
            r0 = r19
            r1 = r21
            r2 = r27
            r17 = r3
            r3 = r28
            r18 = r4
            r4 = r15
            r5 = r16
            r0.setTextAndFont(r1, r2, r3, r4, r5)
        L_0x00bc:
            android.graphics.drawable.Drawable r0 = r13.getBackground()
            boolean r1 = io.dcloud.common.util.PdrUtil.isEmpty(r31)
            if (r1 != 0) goto L_0x00f0
            java.lang.String r1 = "transparent"
            r2 = r36
            boolean r1 = r2.equals(r1)
            if (r1 == 0) goto L_0x00f0
            int r1 = android.graphics.Color.parseColor(r31)     // Catch:{ Exception -> 0x00d5 }
            goto L_0x00d9
        L_0x00d5:
            int r1 = io.dcloud.common.util.PdrUtil.stringToColor(r31)
        L_0x00d9:
            boolean r2 = r0 instanceof io.dcloud.feature.nativeObj.NativeViewBackButtonDrawable
            if (r2 == 0) goto L_0x00e3
            r3 = r0
            io.dcloud.feature.nativeObj.NativeViewBackButtonDrawable r3 = (io.dcloud.feature.nativeObj.NativeViewBackButtonDrawable) r3
            r3.setWidth(r10)
        L_0x00e3:
            if (r2 == 0) goto L_0x00f0
            io.dcloud.feature.nativeObj.NativeViewBackButtonDrawable r0 = (io.dcloud.feature.nativeObj.NativeViewBackButtonDrawable) r0
            int r2 = r0.getDrawableColor()
            if (r1 == r2) goto L_0x00f0
            r0.setDrawableColor(r1)
        L_0x00f0:
            if (r9 == 0) goto L_0x00fc
            boolean r0 = r13 instanceof io.dcloud.feature.nativeObj.TitleNView.BadgeRelateiveLayout
            if (r0 == 0) goto L_0x00fc
            r0 = r13
            io.dcloud.feature.nativeObj.TitleNView$BadgeRelateiveLayout r0 = (io.dcloud.feature.nativeObj.TitleNView.BadgeRelateiveLayout) r0
            r0.setBadgeStr(r9)
        L_0x00fc:
            boolean r0 = r13 instanceof io.dcloud.feature.nativeObj.TitleNView.BadgeRelateiveLayout
            if (r0 == 0) goto L_0x0110
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r32)
            if (r0 != 0) goto L_0x0110
            r0 = r13
            io.dcloud.feature.nativeObj.TitleNView$BadgeRelateiveLayout r0 = (io.dcloud.feature.nativeObj.TitleNView.BadgeRelateiveLayout) r0
            boolean r1 = java.lang.Boolean.parseBoolean(r32)
            r0.setDrawRedDot(r1)
        L_0x0110:
            int r0 = r13.getChildCount()
            r1 = 2
            if (r0 < r1) goto L_0x013b
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r34)
            if (r0 != 0) goto L_0x013b
            android.view.View r0 = r13.getChildAt(r12)
            android.widget.TextView r0 = (android.widget.TextView) r0
            boolean r1 = java.lang.Boolean.parseBoolean(r34)
            if (r1 == 0) goto L_0x0132
            r0.setVisibility(r11)
            android.text.TextUtils$TruncateAt r0 = android.text.TextUtils.TruncateAt.END
            r15.setEllipsize(r0)
            goto L_0x015b
        L_0x0132:
            r1 = 8
            r0.setVisibility(r1)
            r15.setEllipsize(r14)
            goto L_0x015b
        L_0x013b:
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r34)
            if (r0 != 0) goto L_0x015b
            boolean r0 = java.lang.Boolean.parseBoolean(r34)
            if (r0 == 0) goto L_0x015b
            android.widget.TextView r0 = r6.addSelect(r13, r15, r11)
            r5 = r18
            r1 = -1
            if (r1 == r5) goto L_0x015b
            r2 = r17
            if (r1 == r2) goto L_0x015b
            android.content.res.ColorStateList r1 = r6.createColorStateList(r5, r2)
            r0.setTextColor(r1)
        L_0x015b:
            if (r7 == 0) goto L_0x0160
            r6.addButtonOnClickListener(r7, r8, r13)
        L_0x0160:
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r35)
            java.lang.String r1 = "auto"
            if (r0 != 0) goto L_0x0190
            boolean r0 = r10.equals(r1)
            if (r0 == 0) goto L_0x0190
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r34)
            if (r0 != 0) goto L_0x0190
            boolean r0 = java.lang.Boolean.parseBoolean(r34)
            if (r0 == 0) goto L_0x0190
            int r0 = r6.mAppScreenWidth
            float r2 = r6.mCreateScale
            java.lang.String r3 = "5px"
            int r0 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r3, r0, r11, r2)
            int r2 = r15.getPaddingTop()
            int r3 = r15.getPaddingBottom()
            r13.setPadding(r0, r2, r0, r3)
            goto L_0x019b
        L_0x0190:
            int r0 = r15.getPaddingTop()
            int r2 = r15.getPaddingBottom()
            r13.setPadding(r11, r0, r11, r2)
        L_0x019b:
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r37)
            if (r0 == 0) goto L_0x01a7
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r35)
            if (r0 != 0) goto L_0x01f5
        L_0x01a7:
            int r0 = r6.mAppScreenWidth
            float r2 = r6.mCreateScale
            r3 = r37
            int r0 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r3, r0, r11, r2)
            boolean r2 = io.dcloud.common.util.PdrUtil.isEmpty(r35)
            if (r2 == 0) goto L_0x01ba
            r2 = r38
            goto L_0x01bb
        L_0x01ba:
            r2 = r10
        L_0x01bb:
            int r3 = r6.mAppScreenWidth
            float r4 = r6.mCreateScale
            int r2 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r2, r3, r11, r4)
            if (r0 <= 0) goto L_0x01cd
            android.text.TextUtils$TruncateAt r3 = android.text.TextUtils.TruncateAt.END
            r15.setEllipsize(r3)
            r15.setMaxWidth(r0)
        L_0x01cd:
            android.view.ViewGroup$LayoutParams r3 = r13.getLayoutParams()
            int r4 = r3.width
            if (r0 <= 0) goto L_0x01d8
            if (r2 <= r0) goto L_0x01d8
            goto L_0x01e4
        L_0x01d8:
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r35)
            if (r0 != 0) goto L_0x01f0
            boolean r0 = r10.equals(r1)
            if (r0 == 0) goto L_0x01e6
        L_0x01e4:
            r4 = -2
            goto L_0x01f0
        L_0x01e6:
            int r0 = r6.mAppScreenWidth
            int r1 = r3.height
            float r2 = r6.mCreateScale
            int r4 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r10, r0, r1, r2)
        L_0x01f0:
            r3.width = r4
            r13.setLayoutParams(r3)
        L_0x01f5:
            r19.caculateTitleMargin()
            goto L_0x0217
        L_0x01f9:
            boolean r1 = r0 instanceof io.dcloud.feature.nativeObj.data.ButtonDataItem
            if (r1 == 0) goto L_0x0217
            io.dcloud.feature.nativeObj.data.ButtonDataItem r0 = (io.dcloud.feature.nativeObj.data.ButtonDataItem) r0
            if (r8 == 0) goto L_0x0206
            java.lang.String r1 = r30.getWebviewUUID()
            r14 = r1
        L_0x0206:
            r30 = r0
            r31 = r21
            r32 = r22
            r33 = r14
            r34 = r27
            r35 = r28
            r36 = r29
            r30.update(r31, r32, r33, r34, r35, r36)
        L_0x0217:
            return r12
        L_0x0218:
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.nativeObj.TitleNView.setTitleNViewButtonStyle(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, io.dcloud.common.DHInterface.IWebview, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String):boolean");
    }

    public void setTitleNViewFocusable(boolean z) {
        DCBlurDraweeView dCBlurDraweeView = this.mBlurDraweeView;
        if (dCBlurDraweeView != null && dCBlurDraweeView.checkBlurEffect(this.mBlurEffect)) {
            this.mBlurDraweeView.setContentFocusable(z);
        }
    }

    public void setTitleNViewPadding(int i, int i2, int i3, int i4) {
        initTitleNViewLayout();
        this.mTitleNViewLayout.setPadding(i, i2, i3, i4);
    }

    public void setTitleNViewSearchInputText(String str) {
        EditText editText = this.searchInput;
        if (editText != null) {
            editText.setText(str);
            this.searchInput.setSelection(str.length());
            if (!this.searchInput.isFocusable()) {
                this.isSetText.set(true);
                this.searchInput.setFocusable(true);
                this.searchInput.setFocusableInTouchMode(true);
                this.searchInput.requestFocus();
                this.searchInput.setFocusable(false);
                this.searchInput.setFocusableInTouchMode(false);
                this.isSetText.set(false);
            }
        }
    }

    public void setTitleOverflow(String str) {
        if (this.mTitleView != null && !TextUtils.isEmpty(str)) {
            if ("clip".equals(str)) {
                this.mTitleView.setEllipsize((TextUtils.TruncateAt) null);
            } else {
                this.mTitleView.setEllipsize(TextUtils.TruncateAt.END);
            }
        }
    }

    public void setTitleSize(String str) {
        if (this.mTitleView != null && !TextUtils.isEmpty(str) && str.endsWith("px")) {
            float textSize = this.mTitleView.getPaint().getTextSize();
            float convertToScreenInt = (float) PdrUtil.convertToScreenInt(str, this.mInnerWidth, 0, this.mCreateScale);
            if (textSize != convertToScreenInt) {
                this.mTitleView.getPaint().setTextSize(convertToScreenInt);
            }
        }
    }

    public void setTitleText(String str) {
        if (this.mTitleView != null && !TextUtils.isEmpty(str) && !str.equals(this.mTitleView.getText())) {
            this.mTitleView.setText(str);
        }
    }

    public void startProgress() {
        Progress progress = this.mProgress;
        if (progress != null) {
            progress.setVisibility(0);
            this.mProgress.setAlphaInt(255);
            this.mProgress.setCurProgress(0);
            this.mProgress.setWebviewProgress(0);
            RelativeLayout relativeLayout = this.mTitleNViewLayout;
            if (relativeLayout != null && -1 == relativeLayout.indexOfChild(this.mProgress)) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, (int) this.mProgress.getHeightInt());
                layoutParams.addRule(12);
                this.mTitleNViewLayout.addView(this.mProgress, layoutParams);
            }
            IFrameView iFrameView = this.mFrameViewParent;
            if (iFrameView != null && iFrameView.obtainWebView() != null && this.mFrameViewParent.obtainWebView().obtainWindowView() != null) {
                if (this.mIWebviewStateListenerImpl != null) {
                    this.mFrameViewParent.obtainWebView().removeStateListener(this.mIWebviewStateListenerImpl);
                }
                this.mIWebviewStateListenerImpl = new IWebviewStateListenerImpl(this.mProgress);
                this.mFrameViewParent.obtainWebView().addStateListener(this.mIWebviewStateListenerImpl);
            }
        }
    }

    public void stopProgress() {
        Progress progress = this.mProgress;
        if (progress != null && !progress.isFinish()) {
            this.mProgress.finishProgress();
        }
    }

    private ColorStateList createColorStateList(int i, int i2) {
        int[] iArr = {i2, i, i, i, i, i};
        return new ColorStateList(new int[][]{new int[]{16842919, 16842910}, new int[]{16842910, 16842908}, new int[]{16842910}, new int[]{16842908}, new int[]{16842909}, new int[0]}, iArr);
    }

    private class BadgeLinearLayout extends LinearLayout {
        private String badgeStr;
        Rect canvasRect;
        float circle4PX;
        float circle8PX;
        private boolean isDrawRedDot;
        private Paint redDotPaint;
        TextPaint textPaint;

        public BadgeLinearLayout(Context context) {
            super(context);
            this.isDrawRedDot = false;
            this.badgeStr = "";
            this.canvasRect = new Rect();
        }

        /* access modifiers changed from: protected */
        public void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            canvas.getClipBounds(this.canvasRect);
            if (this.isDrawRedDot && PdrUtil.isEmpty(this.badgeStr)) {
                Rect rect = this.canvasRect;
                float f = this.circle4PX;
                canvas.drawCircle(((float) rect.right) - f, ((float) rect.top) + f, f, this.redDotPaint);
            }
        }

        public void draw(Canvas canvas) {
            super.draw(canvas);
        }

        public void setBadgeStr(String str) {
            if (str != null && !str.equals(this.badgeStr)) {
                if (str.length() > 4) {
                    this.badgeStr = str.trim().substring(0, 3) + "…";
                } else {
                    this.badgeStr = str.trim();
                }
                postInvalidate();
            }
        }

        public void setDrawRedDot(boolean z) {
            if (this.isDrawRedDot != z) {
                this.isDrawRedDot = z;
                postInvalidate();
            }
        }

        public void setRedDotColor(int i) {
            this.redDotPaint.setColor(i);
            postInvalidate();
        }

        public BadgeLinearLayout(TitleNView titleNView, Context context, float f, int i) {
            this(context);
            Paint paint = new Paint();
            this.redDotPaint = paint;
            paint.setColor(i);
            this.redDotPaint.setAntiAlias(true);
            TextPaint textPaint2 = new TextPaint();
            this.textPaint = textPaint2;
            textPaint2.setColor(-1);
            this.textPaint.setFakeBoldText(true);
            this.textPaint.setTextAlign(Paint.Align.CENTER);
            float convertToScreenInt = (float) PdrUtil.convertToScreenInt("8px", 0, 0, f);
            this.circle8PX = convertToScreenInt;
            this.textPaint.setTextSize(convertToScreenInt);
            this.circle4PX = (float) PdrUtil.convertToScreenInt("4px", 0, 0, f);
        }
    }

    private class BadgeRelateiveLayout extends LinearLayout {
        private String badgeStr;
        Rect canvasRect;
        float circle4PX;
        float circle8PX;
        private boolean isDrawRedDot;
        private Paint redDotPaint;
        TextPaint textPaint;

        public BadgeRelateiveLayout(Context context) {
            super(context);
            this.isDrawRedDot = false;
            this.badgeStr = "";
            this.canvasRect = new Rect();
        }

        /* access modifiers changed from: protected */
        public void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            canvas.getClipBounds(this.canvasRect);
            if (this.isDrawRedDot && PdrUtil.isEmpty(this.badgeStr)) {
                Rect rect = this.canvasRect;
                float f = this.circle4PX;
                canvas.drawCircle(((float) rect.right) - f, ((float) rect.top) + f, f, this.redDotPaint);
            }
            if (this.badgeStr.length() > 0) {
                Rect rect2 = new Rect();
                TextPaint textPaint2 = this.textPaint;
                String str = this.badgeStr;
                textPaint2.getTextBounds(str, 0, str.length(), rect2);
                Paint paint = new Paint(this.redDotPaint);
                paint.setColor(-65536);
                float abs = ((float) (this.canvasRect.right - Math.abs(rect2.width()))) - this.circle8PX;
                Rect rect3 = this.canvasRect;
                int i = rect3.top;
                RectF rectF = new RectF(abs, (float) i, (float) rect3.right, ((float) (i + Math.abs(rect2.height()))) + this.circle4PX);
                canvas.drawRoundRect(rectF, rectF.height() / 2.0f, rectF.height() / 2.0f, paint);
                Paint.FontMetrics fontMetrics = this.textPaint.getFontMetrics();
                canvas.drawText(this.badgeStr, rectF.centerX(), (rectF.centerY() - (fontMetrics.top / 2.0f)) - (fontMetrics.bottom / 2.0f), this.textPaint);
            }
        }

        public void draw(Canvas canvas) {
            super.draw(canvas);
        }

        public void setBadgeStr(String str) {
            if (str != null && !str.equals(this.badgeStr)) {
                if (str.length() > 4) {
                    this.badgeStr = str.trim().substring(0, 3) + "…";
                } else {
                    this.badgeStr = str.trim();
                }
                postInvalidate();
            }
        }

        public void setDrawRedDot(boolean z) {
            if (this.isDrawRedDot != z) {
                this.isDrawRedDot = z;
                postInvalidate();
            }
        }

        public void setRedDotColor(int i) {
            this.redDotPaint.setColor(i);
            postInvalidate();
        }

        public BadgeRelateiveLayout(TitleNView titleNView, Context context, float f, int i) {
            this(context);
            Paint paint = new Paint();
            this.redDotPaint = paint;
            paint.setColor(i);
            this.redDotPaint.setAntiAlias(true);
            TextPaint textPaint2 = new TextPaint();
            this.textPaint = textPaint2;
            textPaint2.setColor(-1);
            this.textPaint.setFakeBoldText(true);
            this.textPaint.setTextAlign(Paint.Align.CENTER);
            float convertToScreenInt = (float) PdrUtil.convertToScreenInt("8px", 0, 0, f);
            this.circle8PX = convertToScreenInt;
            this.textPaint.setTextSize(convertToScreenInt);
            this.circle4PX = (float) PdrUtil.convertToScreenInt("4px", 0, 0, f);
        }
    }

    public void setTitleColor(int i) {
        TextView textView = this.mTitleView;
        if (textView != null && i != textView.getTextColors().getDefaultColor()) {
            this.mTitleView.setTextColor(i);
        }
    }
}
