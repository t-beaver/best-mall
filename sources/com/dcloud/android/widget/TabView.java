package com.dcloud.android.widget;

import android.app.Activity;
import android.content.Context;
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
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.taobao.weex.common.Constants;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.ui.component.WXBasicComponentType;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.ui.blur.DCBlurDraweeView;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.TitleNViewUtil;
import io.dcloud.common.util.language.LanguageUtil;
import io.dcloud.feature.internal.sdk.SDK;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;

public class TabView extends FrameLayout {
    private String mBackgroundColor;
    private String mBackgroundImage;
    private DCBlurDraweeView mBlurDraweeView;
    private String mBlurEffect = "none";
    private String mBorderStyle;
    private View mBorderView;
    private JSONArray mCommonList;
    private String mCommonSelectedIndex;
    /* access modifiers changed from: private */
    public Context mContext;
    private String mDefaultBackgroundColor = TitleNViewUtil.TRANSPARENT_BUTTON_TEXT_COLOR;
    private String mDefaultBorderColor = "#000000";
    private String mDefaultMaskBackgroundColor = "#00000000";
    private String mDefaultSelectedTextColor = "#3cc51f";
    private String mDefaultTextColor = "#7A7E83";
    private ICallBack mIDoubleCallback;
    /* access modifiers changed from: private */
    public ICallBack mIMaskCallback;
    /* access modifiers changed from: private */
    public ICallBack mIMidCallback;
    /* access modifiers changed from: private */
    public ICallBack mISingleCallback;
    private String mIconfontPath;
    private String mImageSize = "24px";
    private LinearLayout mMask;
    private JSONObject mMidButton;
    /* access modifiers changed from: private */
    public RelativeLayout mMidButtonView;
    private int mMidIndex = 0;
    private View.OnTouchListener mMidTouchListener = new View.OnTouchListener() {
        float X = 0.0f;
        float Y = 0.0f;
        boolean downInMid = false;
        long downTime = 0;

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (TabView.this.mMidButtonView == null || TabView.this.mMidButtonView.getParent() == null) {
                this.downInMid = false;
            } else {
                int action = motionEvent.getAction();
                if (action == 0) {
                    RelativeLayout access$100 = TabView.this.mMidButtonView;
                    if (access$100 == null || access$100.getVisibility() != 0) {
                        this.downInMid = false;
                    } else {
                        this.downInMid = new Rect(access$100.getLeft(), access$100.getTop() + TabView.this.mTabBar.getTop(), access$100.getRight(), access$100.getBottom() + TabView.this.mTabBar.getTop()).contains((int) motionEvent.getX(), (int) motionEvent.getY());
                    }
                    this.downTime = System.currentTimeMillis();
                    this.X = motionEvent.getX();
                    this.Y = motionEvent.getY();
                } else if (action == 1 && this.downInMid && System.currentTimeMillis() - this.downTime < 500 && Math.abs(motionEvent.getY() - this.Y) < 70.0f && Math.abs(motionEvent.getX() - this.X) < 70.0f && TabView.this.mIMidCallback != null) {
                    TabView.this.mIMidCallback.onCallBack(0, (Object) null);
                }
            }
            return this.downInMid;
        }
    };
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            int intValue = ((Integer) view.getTag()).intValue();
            TabView.this.switchTab(intValue);
            if (TabView.this.mISingleCallback != null) {
                TabView.this.mISingleCallback.onCallBack(intValue, (Object) null);
            }
        }
    };
    private View.OnClickListener mOnMaskClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (TabView.this.mIMaskCallback != null) {
                TabView.this.mIMaskCallback.onCallBack(0, (Object) null);
            }
        }
    };
    private float mScale;
    private String mSelectedColor;
    private JSONObject mStyleJson;
    /* access modifiers changed from: private */
    public LinearLayout mTabBar;
    private int mTabHeight;
    private String mTabHeightStr;
    private ArrayList<RelativeLayout> mTabItemViews;
    private String mTextColor;
    private String mTextSize = "10px";
    private String mTextTop = "3px";
    private IApp mWebApp;
    private int redDotColor = -65536;
    private String repeatType;

    class BackGroundDrawable extends Drawable {
        private String bitmapPath = null;
        private Rect bound;
        private Paint colorPaint;
        private Shader mBackgroundBitmap = null;
        private int mBackgroundColor = 0;
        private Paint mPaint;
        private String repeat = "no-repeat";

        BackGroundDrawable() {
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
            throw new UnsupportedOperationException("Method not decompiled: com.dcloud.android.widget.TabView.BackGroundDrawable.parseGradientDirection(java.lang.String, float, float):float[]");
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
            if (this.repeat.equals("repeat")) {
                return bitmap;
            }
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            float f = ((float) i) / ((float) width);
            float f2 = ((float) i2) / ((float) height);
            Matrix matrix = new Matrix();
            if (this.repeat.equals("repeat-x")) {
                matrix.preScale(1.0f, f2);
            } else if (this.repeat.equals("repeat-y")) {
                matrix.preScale(f, 1.0f);
            } else {
                matrix.preScale(f, f2);
            }
            return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        }

        public void draw(Canvas canvas) {
            if (this.bound == null) {
                this.bound = getBounds();
            }
            if (this.mBackgroundBitmap != null) {
                getPaint().setShader(this.mBackgroundBitmap);
                getBackgroundColorPaint().setColor(Color.argb(getBackgroundColorPaint().getAlpha(), 255, 255, 255));
                canvas.drawRect(this.bound, getBackgroundColorPaint());
            } else {
                String str = this.bitmapPath;
                if (str != null) {
                    setBackgroundImage(str);
                    this.bitmapPath = null;
                    getBackgroundColorPaint().setColor(Color.argb(getBackgroundColorPaint().getAlpha(), 255, 255, 255));
                    canvas.drawRect(this.bound, getBackgroundColorPaint());
                    getPaint().setShader(this.mBackgroundBitmap);
                } else {
                    getPaint().setColor(this.mBackgroundColor);
                }
            }
            canvas.drawRect(this.bound, getPaint());
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
            } else {
                this.mBackgroundColor = i;
            }
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
                    String access$500 = TabView.this.getIconPath(str);
                    if (access$500.startsWith(SDK.ANDROID_ASSET)) {
                        try {
                            bitmap = BitmapFactory.decodeStream(TabView.this.getContext().getAssets().open(access$500.replace(SDK.ANDROID_ASSET, "")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (access$500.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                            bitmap = BitmapFactory.decodeFile(access$500.replace(DeviceInfo.FILE_PROTOCOL, ""));
                        }
                        bitmap = null;
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

        public void setBackgroundRepeat(String str, String str2) {
            if (!PdrUtil.isEmpty(str) && !str.equals(this.repeat) && !TextUtils.isEmpty(str2)) {
                this.repeat = str;
                this.mBackgroundBitmap = null;
                setBackgroundImage(str2);
            }
        }

        public void setColorFilter(ColorFilter colorFilter) {
        }
    }

    public TabView(Context context, View view, JSONObject jSONObject, float f, IApp iApp) {
        super(context);
        this.mContext = context;
        this.mScale = f;
        this.mWebApp = iApp;
        setClipChildren(false);
        this.mStyleJson = jSONObject;
        if (jSONObject == null) {
            this.mStyleJson = new JSONObject();
        }
        if (jSONObject.containsKey("iconfontSrc")) {
            String string = jSONObject.getString("iconfontSrc");
            if (!PdrUtil.isNetPath(string)) {
                this.mIconfontPath = this.mWebApp.convert2AbsFullPath(string);
            }
        }
        this.mTextColor = this.mStyleJson.getString("color");
        this.mSelectedColor = this.mStyleJson.getString("selectedColor");
        this.mBackgroundColor = this.mStyleJson.getString("backgroundColor");
        this.mBackgroundImage = this.mStyleJson.getString(Constants.Name.BACKGROUND_IMAGE);
        this.repeatType = this.mStyleJson.getString("backgroundRepeat");
        if (this.mStyleJson.containsKey("blurEffect")) {
            this.mBlurEffect = this.mStyleJson.getString("blurEffect");
        }
        if (this.mStyleJson.containsKey("redDotColor")) {
            String string2 = this.mStyleJson.getString("redDotColor");
            if (!PdrUtil.isEmpty(string2)) {
                this.redDotColor = PdrUtil.stringToColor(string2);
            }
        }
        this.mBorderStyle = this.mStyleJson.getString(Constants.Name.BORDER_STYLE);
        if (this.mStyleJson.containsKey(Constants.Name.FONT_SIZE)) {
            this.mTextSize = this.mStyleJson.getString(Constants.Name.FONT_SIZE);
        }
        if (this.mStyleJson.containsKey(AbsoluteConst.JSON_KEY_ICON_WIDTH)) {
            this.mImageSize = this.mStyleJson.getString(AbsoluteConst.JSON_KEY_ICON_WIDTH);
        }
        float applyDimension = TypedValue.applyDimension(1, 72.0f, getResources().getDisplayMetrics());
        this.mTabHeightStr = !this.mStyleJson.containsKey("height") ? "50px" : this.mStyleJson.getString("height");
        if (this.mStyleJson.containsKey("spacing")) {
            this.mTextTop = this.mStyleJson.getString("spacing");
        }
        this.mTabHeight = (int) PdrUtil.parseFloat(this.mTabHeightStr, 0.0f, applyDimension, f);
        this.mCommonSelectedIndex = this.mStyleJson.getString("selected") == null ? WXInstanceApm.VALUE_ERROR_CODE_DEFAULT : this.mStyleJson.getString("selected");
        this.mCommonList = this.mStyleJson.getJSONArray(WXBasicComponentType.LIST);
        this.mMidButton = this.mStyleJson.getJSONObject("midButton");
        this.mTabItemViews = new ArrayList<>();
        this.mBorderView = new View(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, 1);
        layoutParams.bottomMargin = this.mTabHeight;
        layoutParams.gravity = 80;
        addView(this.mBorderView, layoutParams);
        LinearLayout linearLayout = new LinearLayout(context);
        this.mTabBar = linearLayout;
        linearLayout.setOrientation(0);
        this.mTabBar.setGravity(80);
        this.mTabBar.setClipChildren(false);
        if (this.mBlurEffect.equals(DCBlurDraweeView.LIGHT) || this.mBlurEffect.equals(DCBlurDraweeView.DARK) || this.mBlurEffect.equals(DCBlurDraweeView.EXTRALIGHT)) {
            this.mDefaultBackgroundColor = "#00FFFFFF";
            this.mBlurDraweeView = new DCBlurDraweeView(iApp.getActivity(), true, DCBlurDraweeView.SEMI_AUTOMATICALLY);
            FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-1, this.mTabHeight);
            layoutParams2.gravity = 80;
            this.mBlurDraweeView.setDownscaleFactor(0.3f);
            this.mBlurDraweeView.setBlurRadius(20);
            this.mBlurDraweeView.setBlurEffect(this.mBlurEffect);
            this.mBlurDraweeView.setGravityType(80);
            addView(this.mBlurDraweeView, layoutParams2);
            this.mBlurDraweeView.setBlur(true);
            this.mBlurDraweeView.setRootView(view);
            this.mBlurDraweeView.setBlurLayoutChangeCallBack(new DCBlurDraweeView.BlurLayoutChangeCallBack() {
                public void setVisibility(int i) {
                    if (TabView.this.mTabBar != null) {
                        TabView.this.mTabBar.setVisibility(i);
                    }
                }
            });
        }
        addView(this.mTabBar, new ViewGroup.LayoutParams(-1, this.mTabHeight));
        initTabStyle();
        initTabItemStyle();
        setSelectedStyle();
        setOnTouchListener(this.mMidTouchListener);
    }

    private void changeNavigationBarColor(final int i) {
        if (Build.VERSION.SDK_INT >= 26 && this.mContext != null) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                public void run() {
                    Window window = ((Activity) TabView.this.mContext).getWindow();
                    window.setNavigationBarColor(i);
                    int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
                    window.getDecorView().setSystemUiVisibility(PdrUtil.isLightColor(i) ? systemUiVisibility | 16 : systemUiVisibility & -17);
                }
            }, 400);
        }
    }

    private ViewGroup getCommonItemByIndex(int i) {
        if (this.mTabItemViews.size() - 1 >= i) {
            return this.mTabItemViews.get(i);
        }
        return null;
    }

    /* access modifiers changed from: private */
    public String getIconPath(String str) {
        String convert2AbsFullPath = this.mWebApp.convert2AbsFullPath(str);
        if (convert2AbsFullPath == null || !PdrUtil.isDeviceRootDir(convert2AbsFullPath)) {
            if (convert2AbsFullPath != null && convert2AbsFullPath.startsWith("/") && convert2AbsFullPath.length() > 1) {
                convert2AbsFullPath = convert2AbsFullPath.substring(1);
            }
            if (convert2AbsFullPath != null && convert2AbsFullPath.startsWith("android_asset/")) {
                convert2AbsFullPath = convert2AbsFullPath.replace("android_asset/", "");
            }
            return SDK.ANDROID_ASSET + convert2AbsFullPath;
        }
        return DeviceInfo.FILE_PROTOCOL + convert2AbsFullPath;
    }

    private void initTabItemStyle() {
        for (int i = 0; i < this.mCommonList.size(); i++) {
            setCommonItemStyle(i, (JSONObject) this.mCommonList.get(i));
        }
        updateMidItemStyle();
    }

    private void initTabStyle() {
        BackGroundDrawable backGroundDrawable;
        if (this.mTabBar.getBackground() instanceof BackGroundDrawable) {
            backGroundDrawable = (BackGroundDrawable) this.mTabBar.getBackground();
        } else {
            backGroundDrawable = new BackGroundDrawable();
            this.mTabBar.setBackground(backGroundDrawable);
        }
        backGroundDrawable.setBackgroundRepeat(this.repeatType, this.mBackgroundImage);
        int optColor = optColor(this.mBackgroundColor, this.mDefaultBackgroundColor);
        backGroundDrawable.setBackgroundColor(optColor);
        backGroundDrawable.setBackgroundImage(this.mBackgroundImage);
        changeNavigationBarColor(optColor);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mTabBar.getLayoutParams();
        layoutParams.gravity = 80;
        layoutParams.height = this.mTabHeight;
        this.mTabBar.setLayoutParams(layoutParams);
        this.mBorderView.setBackgroundColor(optColor(this.mBorderStyle, this.mDefaultBorderColor));
    }

    private static int optColor(String str, String str2) {
        if (str != null) {
            return PdrUtil.stringToColor(str);
        }
        try {
            return PdrUtil.stringToColor(str2);
        } catch (Exception unused) {
            return PdrUtil.stringToColor(str2);
        }
    }

    private void placeholder(String str, ImageView imageView) {
        try {
            if (!TextUtils.isEmpty(str)) {
                ((RequestBuilder) ((RequestBuilder) Glide.with(getContext()).load(getIconPath(str)).dontAnimate()).placeholder(imageView.getDrawable())).into(imageView);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void setCommonItemJson(int i, String str, String str2, String str3, JSONObject jSONObject, boolean z) {
        if (i < this.mCommonList.size()) {
            JSONObject jSONObject2 = this.mCommonList.getJSONObject(i);
            if (str != null) {
                jSONObject2.put("text", (Object) str);
            }
            if (str2 != null) {
                jSONObject2.put("iconPath", (Object) str2);
            }
            if (str3 != null) {
                jSONObject2.put("selectedIconPath", (Object) str3);
            }
            if (jSONObject != null) {
                if (jSONObject2.containsKey("iconfont")) {
                    JSONObject jSONObject3 = jSONObject2.getJSONObject("iconfont");
                    if (!(jSONObject3 == null || (r5 = jSONObject.keySet().iterator()) == null)) {
                        for (String next : jSONObject.keySet()) {
                            jSONObject3.put(next, (Object) jSONObject.getString(next));
                        }
                    }
                } else {
                    jSONObject2.put("iconfont", (Object) jSONObject);
                }
            }
            jSONObject2.put("visible", (Object) Boolean.valueOf(z));
            setCommonItemStyle(i, jSONObject2);
        }
    }

    private void setCommonItemStyle(int i, JSONObject jSONObject) {
        RelativeLayout relativeLayout;
        int i2;
        int i3;
        GifDrawable gifDrawable;
        int i4 = i;
        JSONObject jSONObject2 = jSONObject;
        jSONObject2.getString("pagePath");
        String string = jSONObject2.getString("text");
        JSONObject jSONObject3 = jSONObject2.getJSONObject("textLocales");
        if (jSONObject3 != null) {
            string = LanguageUtil.getString(jSONObject3, string);
        }
        String string2 = jSONObject2.getString("iconPath");
        String string3 = jSONObject2.getString("selectedIconPath");
        JSONObject jSONObject4 = jSONObject2.getJSONObject("iconfont");
        int parseInt = Integer.parseInt(this.mCommonSelectedIndex);
        boolean booleanValue = jSONObject2.containsKey("visible") ? jSONObject2.getBooleanValue("visible") : true;
        if (this.mTabItemViews.size() - 1 < i4) {
            relativeLayout = (RelativeLayout) LayoutInflater.from(this.mContext).inflate(R.layout.dcloud_tabbar_item, (ViewGroup) null);
            relativeLayout.setTag(Integer.valueOf(i));
            relativeLayout.setOnClickListener(this.mOnClickListener);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
            layoutParams.weight = 1.0f;
            this.mTabBar.addView(relativeLayout, layoutParams);
            this.mTabItemViews.add(relativeLayout);
        } else {
            relativeLayout = this.mTabItemViews.get(i4);
        }
        ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.tabIV);
        TextView textView = (TextView) relativeLayout.findViewById(R.id.iconfontTV);
        if (jSONObject4 != null) {
            imageView.setVisibility(8);
            updateIconfont(textView, jSONObject4, PdrUtil.parseFloat(this.mImageSize, 0.0f, 0.0f, this.mScale));
        } else if (!PdrUtil.isEmpty(string2) || !PdrUtil.isEmpty(string3)) {
            if (!PdrUtil.isEmpty(string3) && parseInt == i4) {
                imageView.setVisibility(0);
                LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                layoutParams2.height = (int) PdrUtil.parseFloat(this.mImageSize, 0.0f, 0.0f, this.mScale);
                layoutParams2.width = (int) PdrUtil.parseFloat(this.mImageSize, 0.0f, 0.0f, this.mScale);
                imageView.setLayoutParams(layoutParams2);
                String iconPath = getIconPath(string3);
                try {
                    if (iconPath.startsWith(SDK.ANDROID_ASSET)) {
                        gifDrawable = new GifDrawable(this.mContext.getAssets(), iconPath.replace(SDK.ANDROID_ASSET, ""));
                    } else {
                        gifDrawable = new GifDrawable(getContext().getContentResolver(), Uri.parse(iconPath));
                    }
                    gifDrawable.setLoopCount(1);
                    imageView.setImageDrawable(gifDrawable);
                } catch (Exception unused) {
                    placeholder(string3, imageView);
                }
            } else if (!PdrUtil.isEmpty(string2)) {
                imageView.setVisibility(0);
                LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                layoutParams3.height = (int) PdrUtil.parseFloat(this.mImageSize, 0.0f, 0.0f, this.mScale);
                layoutParams3.width = (int) PdrUtil.parseFloat(this.mImageSize, 0.0f, 0.0f, this.mScale);
                imageView.setLayoutParams(layoutParams3);
                placeholder(string2, imageView);
            } else {
                i3 = 8;
                imageView.setVisibility(8);
                textView.setVisibility(i3);
            }
            i3 = 8;
            textView.setVisibility(i3);
        } else {
            imageView.setVisibility(8);
            textView.setVisibility(8);
        }
        TextView textView2 = (TextView) relativeLayout.findViewById(R.id.tabTV);
        textView2.setTag(jSONObject2);
        ((GradientDrawable) ((ImageView) relativeLayout.findViewById(R.id.itemDot)).getDrawable()).setColor(this.redDotColor);
        if (!PdrUtil.isEmpty(string)) {
            textView2.setVisibility(0);
            LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) textView2.getLayoutParams();
            layoutParams4.topMargin = (int) PdrUtil.parseFloat(this.mTextTop, 0.0f, 0.0f, this.mScale);
            textView2.setLayoutParams(layoutParams4);
            textView2.setTextSize(0, PdrUtil.parseFloat(this.mTextSize, 0.0f, 0.0f, this.mScale));
            if (parseInt != i4) {
                textView2.setTextColor(optColor(this.mTextColor, this.mDefaultTextColor));
            } else {
                textView2.setTextColor(optColor(this.mSelectedColor, this.mDefaultSelectedTextColor));
            }
            textView2.setText(string);
            i2 = 8;
        } else {
            i2 = 8;
            textView2.setVisibility(8);
        }
        if (booleanValue) {
            relativeLayout.setVisibility(0);
        } else {
            relativeLayout.setVisibility(i2);
        }
    }

    private void setDotBadgeMarginTop(ViewGroup viewGroup, View view) {
        float f;
        float f2;
        View findViewById = viewGroup.findViewById(R.id.tabIV);
        float height = ((float) viewGroup.getHeight()) / this.mScale;
        float height2 = ((float) viewGroup.findViewById(R.id.contentWrapper).getHeight()) / this.mScale;
        float height3 = ((float) view.getHeight()) / this.mScale;
        float f3 = (height - height2) / 2.0f;
        if (findViewById.getVisibility() == 0) {
            height3 /= 2.0f;
        }
        float f4 = f3 - height3;
        float f5 = 0.0f;
        if (f4 <= 2.0f) {
            height3 = ((f3 <= height3 || f4 >= 2.0f) && (f3 >= height3 || f3 <= 2.0f)) ? 0.0f : f3 - 2.0f;
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.topMargin = (int) ((-height3) * this.mScale);
        if (findViewById.getVisibility() == 0) {
            if (view.getId() == R.id.itemDot) {
                f = this.mScale;
                f2 = -5.0f;
            } else {
                if (view.getId() == R.id.itemBadge) {
                    f = this.mScale;
                    f2 = -9.0f;
                }
                layoutParams.leftMargin = (int) (f5 - ((float) ((viewGroup.findViewById(R.id.contentWrapper).getWidth() - findViewById.getWidth()) / 2)));
            }
            f5 = f * f2;
            layoutParams.leftMargin = (int) (f5 - ((float) ((viewGroup.findViewById(R.id.contentWrapper).getWidth() - findViewById.getWidth()) / 2)));
        }
        view.setLayoutParams(layoutParams);
    }

    private void setSelectedStyle() {
        GifDrawable gifDrawable;
        for (int i = 0; i < this.mTabItemViews.size(); i++) {
            String str = this.mCommonSelectedIndex;
            if (str != null) {
                int parseInt = Integer.parseInt(str);
                ViewGroup viewGroup = this.mTabItemViews.get(i);
                TextView textView = (TextView) viewGroup.findViewById(R.id.tabTV);
                final ImageView imageView = (ImageView) viewGroup.findViewById(R.id.tabIV);
                TextView textView2 = (TextView) viewGroup.findViewById(R.id.iconfontTV);
                JSONObject jSONObject = (JSONObject) textView.getTag();
                JSONObject jSONObject2 = jSONObject.getJSONObject("iconfont");
                if (parseInt == i) {
                    textView.setTextColor(optColor(this.mSelectedColor, this.mDefaultSelectedTextColor));
                    if (jSONObject2 != null) {
                        if (jSONObject2.containsKey("selectedText")) {
                            textView2.setText(jSONObject2.getString("selectedText"));
                        }
                        if (jSONObject2.containsKey("selectedColor")) {
                            textView2.setTextColor(PdrUtil.stringToColor(jSONObject2.getString("selectedColor")));
                        }
                    } else {
                        String string = jSONObject.getString("selectedIconPath");
                        try {
                            String iconPath = getIconPath(string);
                            if (iconPath.startsWith(SDK.ANDROID_ASSET)) {
                                gifDrawable = new GifDrawable(this.mContext.getAssets(), iconPath.replace(SDK.ANDROID_ASSET, ""));
                            } else {
                                gifDrawable = new GifDrawable(getContext().getContentResolver(), Uri.parse(iconPath));
                            }
                            gifDrawable.setLoopCount(1);
                            gifDrawable.addAnimationListener(new AnimationListener() {
                                public void onAnimationCompleted(int i) {
                                    Drawable drawable = imageView.getDrawable();
                                    if (drawable instanceof GifDrawable) {
                                        GifDrawable gifDrawable = (GifDrawable) drawable;
                                        gifDrawable.seekToFrame(gifDrawable.getNumberOfFrames());
                                        gifDrawable.removeAnimationListener(this);
                                    }
                                }
                            });
                            imageView.setImageDrawable(gifDrawable);
                        } catch (Exception unused) {
                            placeholder(string, imageView);
                        }
                    }
                } else {
                    textView.setTextColor(optColor(this.mTextColor, this.mDefaultTextColor));
                    if (jSONObject2 != null) {
                        if (jSONObject2.containsKey("text")) {
                            textView2.setText(jSONObject2.getString("text"));
                        }
                        if (jSONObject2.containsKey("color")) {
                            textView2.setTextColor(optColor(jSONObject2.getString("color"), this.mDefaultTextColor));
                        }
                    } else {
                        placeholder(jSONObject.getString("iconPath"), imageView);
                    }
                }
            }
        }
    }

    private void setTabItemStyle() {
        initTabItemStyle();
    }

    private void updateMidItemStyle() {
        JSONObject jSONObject = this.mMidButton;
        boolean booleanValue = (jSONObject == null || !jSONObject.containsKey("visible")) ? true : this.mMidButton.getBooleanValue("visible");
        if (booleanValue) {
            booleanValue = canMidButtonShowDisplayed();
        }
        if (!booleanValue) {
            RelativeLayout relativeLayout = this.mMidButtonView;
            if (relativeLayout != null && relativeLayout.getParent() != null) {
                this.mTabBar.removeView(this.mMidButtonView);
                return;
            }
            return;
        }
        JSONObject jSONObject2 = this.mMidButton;
        if (jSONObject2 != null) {
            float parseFloat = jSONObject2.getString("height") != null ? PdrUtil.parseFloat(this.mMidButton.getString("height"), 0.0f, 0.0f, this.mScale) : -1.0f;
            float parseFloat2 = this.mMidButton.getString("width") != null ? PdrUtil.parseFloat(this.mMidButton.getString("width"), 0.0f, 0.0f, this.mScale) : -1.0f;
            String string = this.mMidButton.getString("text");
            JSONObject jSONObject3 = this.mMidButton.getJSONObject("textLocales");
            if (jSONObject3 != null) {
                string = LanguageUtil.getString(jSONObject3, string);
            }
            float parseFloat3 = PdrUtil.parseFloat(this.mMidButton.getString(AbsoluteConst.JSON_KEY_ICON_WIDTH) != null ? this.mMidButton.getString(AbsoluteConst.JSON_KEY_ICON_WIDTH) : this.mImageSize, 0.0f, 0.0f, this.mScale);
            String string2 = this.mMidButton.getString("iconPath");
            String string3 = this.mMidButton.getString(Constants.Name.BACKGROUND_IMAGE);
            JSONObject jSONObject4 = this.mMidButton.getJSONObject("iconfont");
            RelativeLayout relativeLayout2 = this.mMidButtonView;
            if (relativeLayout2 == null) {
                relativeLayout2 = (RelativeLayout) LayoutInflater.from(this.mContext).inflate(R.layout.dcloud_tabbar_mid, (ViewGroup) null);
                this.mMidButtonView = relativeLayout2;
            }
            ((GradientDrawable) ((ImageView) relativeLayout2.findViewById(R.id.itemDot)).getDrawable()).setColor(this.redDotColor);
            float parseFloat4 = (float) ((int) PdrUtil.parseFloat(this.mImageSize, 0.0f, 0.0f, this.mScale));
            float parseFloat5 = (float) ((int) PdrUtil.parseFloat(this.mTextTop, 0.0f, 0.0f, this.mScale));
            float parseFloat6 = (float) ((int) PdrUtil.parseFloat(this.mTextSize, 0.0f, 0.0f, this.mScale));
            ImageView imageView = (ImageView) relativeLayout2.findViewById(R.id.tabIV);
            TextView textView = (TextView) relativeLayout2.findViewById(R.id.tabIconTV);
            if (jSONObject4 != null) {
                imageView.setVisibility(8);
                updateIconfont(textView, jSONObject4, parseFloat3);
            } else if (!TextUtils.isEmpty(string2)) {
                textView.setVisibility(8);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                int i = (int) parseFloat3;
                layoutParams.height = i;
                layoutParams.width = i;
                imageView.setLayoutParams(layoutParams);
                placeholder(string2, imageView);
                imageView.setVisibility(0);
            } else {
                textView.setVisibility(8);
                imageView.setVisibility(8);
            }
            TextView textView2 = (TextView) relativeLayout2.findViewById(R.id.tabTV);
            textView2.setTextSize(0, PdrUtil.parseFloat(this.mTextSize, 0.0f, 0.0f, this.mScale));
            textView2.setTextColor(optColor(this.mTextColor, this.mDefaultTextColor));
            textView2.setText(string);
            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) textView2.getLayoutParams();
            layoutParams2.bottomMargin = (int) ((((float) this.mTabHeight) - ((parseFloat4 + parseFloat5) + parseFloat6)) / 2.0f);
            textView2.setLayoutParams(layoutParams2);
            if (TextUtils.isEmpty(string)) {
                textView2.setVisibility(8);
            } else {
                textView2.setVisibility(0);
            }
            placeholder(string3, (ImageView) relativeLayout2.findViewById(R.id.bgImg));
            LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams((int) parseFloat2, (int) parseFloat);
            if (parseFloat2 == -1.0f) {
                layoutParams3.weight = 1.0f;
            }
            if (this.mMidButtonView.getParent() != null) {
                this.mTabBar.removeView(this.mMidButtonView);
            }
            this.mTabBar.addView(this.mMidButtonView, this.mMidIndex, layoutParams3);
        }
    }

    public void bringMaskToFront() {
        LinearLayout linearLayout = this.mMask;
        if (linearLayout != null) {
            linearLayout.bringToFront();
        }
    }

    public boolean canMidButtonShowDisplayed() {
        int tabItemDisplayedSize = getTabItemDisplayedSize();
        boolean z = tabItemDisplayedSize % 2 == 0;
        if (z) {
            int i = tabItemDisplayedSize / 2;
            int i2 = 0;
            for (int i3 = 0; i3 < this.mCommonList.size(); i3++) {
                JSONObject jSONObject = this.mCommonList.getJSONObject(i3);
                if (jSONObject != null) {
                    if (!jSONObject.containsKey("visible") || jSONObject.getBoolean("visible").booleanValue()) {
                        i2++;
                    }
                    if (i2 == i) {
                        this.mMidIndex = i3 + 1;
                    }
                }
            }
        }
        return z;
    }

    public void dispose() {
        this.mTabItemViews.clear();
        this.mMidButtonView = null;
    }

    public int getMidHeight() {
        JSONObject jSONObject = this.mMidButton;
        if (jSONObject == null) {
            return 0;
        }
        return (int) PdrUtil.parseFloat(jSONObject.getString("height"), 0.0f, 0.0f, this.mScale);
    }

    public int getTabHeight() {
        DCBlurDraweeView dCBlurDraweeView = this.mBlurDraweeView;
        if (dCBlurDraweeView == null || !dCBlurDraweeView.checkBlurEffect(this.mBlurEffect)) {
            return this.mTabHeight;
        }
        return 0;
    }

    public String getTabHeightStr() {
        DCBlurDraweeView dCBlurDraweeView = this.mBlurDraweeView;
        if (dCBlurDraweeView == null || !dCBlurDraweeView.checkBlurEffect(this.mBlurEffect)) {
            return this.mTabHeightStr;
        }
        return WXInstanceApm.VALUE_ERROR_CODE_DEFAULT;
    }

    public int getTabItemDisplayedSize() {
        int i = 0;
        for (int i2 = 0; i2 < this.mCommonList.size(); i2++) {
            JSONObject jSONObject = this.mCommonList.getJSONObject(i2);
            if (jSONObject != null && (!jSONObject.containsKey("visible") || jSONObject.getBoolean("visible").booleanValue())) {
                i++;
            }
        }
        return i;
    }

    public void hideTabBarRedDot(JSONObject jSONObject) {
        ViewGroup commonItemByIndex = getCommonItemByIndex(jSONObject.getInteger("index").intValue());
        if (commonItemByIndex != null) {
            ((ImageView) commonItemByIndex.findViewById(R.id.itemDot)).setVisibility(4);
        }
    }

    public void removeTabBarBadge(JSONObject jSONObject) {
        ViewGroup commonItemByIndex = getCommonItemByIndex(jSONObject.getInteger("index").intValue());
        if (commonItemByIndex != null) {
            ((TextView) commonItemByIndex.findViewById(R.id.itemBadge)).setVisibility(4);
        }
    }

    public void setDoubleCallbackListener(ICallBack iCallBack) {
        this.mIDoubleCallback = iCallBack;
    }

    public void setIWebViewFocusable(boolean z) {
        DCBlurDraweeView dCBlurDraweeView = this.mBlurDraweeView;
        if (dCBlurDraweeView != null && dCBlurDraweeView.checkBlurEffect(this.mBlurEffect)) {
            this.mBlurDraweeView.setContentFocusable(z);
        }
    }

    public void setMask(JSONObject jSONObject) {
        String string = (jSONObject == null || !jSONObject.containsKey("color")) ? null : jSONObject.getString("color");
        if (!"none".equals(string)) {
            if (this.mMask == null) {
                LinearLayout linearLayout = new LinearLayout(this.mContext);
                this.mMask = linearLayout;
                linearLayout.setOnClickListener(this.mOnMaskClickListener);
                this.mMask.setBackgroundColor(optColor(string, this.mDefaultMaskBackgroundColor));
                ((FrameLayout) getParent()).addView(this.mMask, new ViewGroup.LayoutParams(-1, -1));
            }
        } else if (this.mMask != null) {
            ((FrameLayout) getParent()).removeView(this.mMask);
            this.mMask = null;
        }
    }

    public void setMaskCallbackListener(ICallBack iCallBack) {
        this.mIMaskCallback = iCallBack;
    }

    public void setMidCallbackListener(ICallBack iCallBack) {
        this.mIMidCallback = iCallBack;
    }

    public void setSingleCallbackListener(ICallBack iCallBack) {
        this.mISingleCallback = iCallBack;
    }

    public void setTabBarBadge(JSONObject jSONObject) {
        int intValue = jSONObject.getInteger("index").intValue();
        String string = jSONObject.getString("text");
        ViewGroup commonItemByIndex = getCommonItemByIndex(intValue);
        if (commonItemByIndex != null) {
            TextView textView = (TextView) commonItemByIndex.findViewById(R.id.itemBadge);
            ((ImageView) commonItemByIndex.findViewById(R.id.itemDot)).setVisibility(4);
            textView.setText(string);
            setDotBadgeMarginTop(commonItemByIndex, textView);
            textView.setVisibility(0);
        }
    }

    public void setTabBarItem(JSONObject jSONObject) {
        setCommonItemJson(jSONObject.getInteger("index").intValue(), jSONObject.getString("text"), jSONObject.getString("iconPath"), jSONObject.getString("selectedIconPath"), jSONObject.containsKey("iconfont") ? jSONObject.getJSONObject("iconfont") : null, jSONObject.containsKey("visible") ? jSONObject.getBooleanValue("visible") : true);
    }

    public void setTabBarStyle(JSONObject jSONObject) {
        if (jSONObject.containsKey("color")) {
            this.mTextColor = jSONObject.getString("color");
        }
        if (jSONObject.containsKey("selectedColor")) {
            this.mSelectedColor = jSONObject.getString("selectedColor");
        }
        if (jSONObject.containsKey("backgroundColor")) {
            this.mBackgroundColor = jSONObject.getString("backgroundColor");
        }
        if (jSONObject.containsKey(Constants.Name.BACKGROUND_IMAGE)) {
            this.mBackgroundImage = jSONObject.getString(Constants.Name.BACKGROUND_IMAGE);
        }
        if (jSONObject.containsKey(Constants.Name.BORDER_STYLE)) {
            this.mBorderStyle = jSONObject.getString(Constants.Name.BORDER_STYLE);
        }
        if (jSONObject.containsKey("height")) {
            String string = jSONObject.getString("height");
            this.mTabHeightStr = string;
            this.mTabHeight = (int) PdrUtil.parseFloat(string, 0.0f, 0.0f, this.mScale);
        }
        if (jSONObject.containsKey("midButton")) {
            this.mMidButton = jSONObject.getJSONObject("midButton");
        }
        if (jSONObject.containsKey(Constants.Name.FONT_SIZE)) {
            this.mTextSize = jSONObject.getString(Constants.Name.FONT_SIZE);
        }
        if (jSONObject.containsKey(AbsoluteConst.JSON_KEY_ICON_WIDTH)) {
            this.mImageSize = jSONObject.getString(AbsoluteConst.JSON_KEY_ICON_WIDTH);
        }
        if (jSONObject.containsKey("backgroundRepeat")) {
            this.repeatType = jSONObject.getString("backgroundRepeat");
        }
        initTabStyle();
        setTabItemStyle();
        setSelectedStyle();
        if (jSONObject.containsKey("redDotColor")) {
            String string2 = jSONObject.getString("redDotColor");
            if (!PdrUtil.isEmpty(string2) && this.redDotColor != PdrUtil.stringToColor(string2)) {
                this.redDotColor = PdrUtil.stringToColor(string2);
                for (int i = 0; i < this.mTabItemViews.size(); i++) {
                    ViewGroup viewGroup = this.mTabItemViews.get(i);
                    if (viewGroup != null) {
                        ((GradientDrawable) ((ImageView) viewGroup.findViewById(R.id.itemDot)).getDrawable()).setColor(this.redDotColor);
                    }
                }
            }
        }
    }

    public void showTabBarRedDot(JSONObject jSONObject) {
        ViewGroup commonItemByIndex = getCommonItemByIndex(jSONObject.getInteger("index").intValue());
        if (commonItemByIndex != null) {
            ImageView imageView = (ImageView) commonItemByIndex.findViewById(R.id.itemDot);
            ((TextView) commonItemByIndex.findViewById(R.id.itemBadge)).setVisibility(4);
            setDotBadgeMarginTop(commonItemByIndex, imageView);
            imageView.setVisibility(0);
        }
    }

    public void switchTab(int i) {
        this.mCommonSelectedIndex = String.valueOf(i);
        setSelectedStyle();
        DCBlurDraweeView dCBlurDraweeView = this.mBlurDraweeView;
        if (dCBlurDraweeView != null) {
            dCBlurDraweeView.postInvalidate(1000);
        }
    }

    public void updateIconfont(TextView textView, JSONObject jSONObject, float f) {
        if (this.mWebApp != null && textView != null && jSONObject != null && !TextUtils.isEmpty(this.mIconfontPath)) {
            textView.setVisibility(0);
            byte obtainRunningAppMode = this.mWebApp.obtainRunningAppMode();
            String string = jSONObject.getString("text");
            if (jSONObject.containsKey(Constants.Name.FONT_SIZE)) {
                textView.setTextSize(0, (float) ((int) PdrUtil.parseFloat(jSONObject.getString(Constants.Name.FONT_SIZE), 0.0f, 0.0f, this.mScale)));
            }
            if (jSONObject.containsKey("color")) {
                textView.setTextColor(PdrUtil.stringToColor(jSONObject.getString("color")));
            }
            if (this.mIconfontPath.startsWith("/storage") || obtainRunningAppMode != 1) {
                File file = new File(this.mIconfontPath);
                if (file.exists()) {
                    textView.setTypeface(Typeface.createFromFile(file));
                }
            } else {
                try {
                    textView.setTypeface(Typeface.createFromAsset(getContext().getAssets(), this.mIconfontPath));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
            int i = (int) f;
            layoutParams.height = i;
            layoutParams.width = i;
            textView.setLayoutParams(layoutParams);
            textView.setText(string);
            textView.setVisibility(0);
        }
    }

    public void updateMidButton(JSONObject jSONObject) {
        if (jSONObject != null) {
            this.mMidButton = jSONObject;
        }
        updateMidItemStyle();
    }
}
