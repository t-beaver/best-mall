package io.dcloud.feature.nativeObj;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.nostra13.dcloudimageloader.core.assist.FailReason;
import com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener;
import com.taobao.weex.common.Constants;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.ui.component.WXBasicComponentType;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IEventCallback;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.INativeView;
import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.DHInterface.IWaiter;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.ui.FrameBitmapView;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.nativeObj.richtext.RichTextLayout;
import io.dcloud.feature.uniapp.adapter.AbsURIAdapter;
import io.dcloud.nineoldandroids.view.ViewHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class NativeView extends FrameLayout implements IWaiter, INativeView, IReflectAble {
    static final String T = "{clientX:%d,clientY:%d,pageX:%d,pageY:%d,screenX:%d,screenY:%d}";
    private boolean isAnimate = false;
    protected boolean isImmersed = false;
    public boolean isLayoutAdapt = false;
    public boolean isStatusBar = false;
    boolean isWebAnimationRuning = false;
    IApp mApp = null;
    public int mAppScreenHeight;
    public int mAppScreenWidth;
    boolean mAttached = false;
    protected int mBackGroundColor = 0;
    protected String mBackgroundImageSrc = null;
    public NativeCanvasView mCanvasView = null;
    HashMap<String, INativeViewChildView> mChildViewMaps = new HashMap<>();
    public float mCreateScale = 1.0f;
    IFrameView mFrameViewParent = null;
    private Handler mHandler = new Handler();
    String mID = null;
    IEventCallback mIEventCallback = null;
    protected int mInnerBottom;
    public int mInnerHeight;
    public int mInnerLeft;
    protected int mInnerRight;
    public int mInnerTop;
    public int mInnerWidth;
    boolean mIntercept = true;
    public int mMarginBottom = 0;
    public int mMarginTop = 0;
    HashMap<String, Integer> mOverlayMaps = new HashMap<>();
    ArrayList<Overlay> mOverlays = new ArrayList<>();
    Paint mPaint = new Paint();
    private int mRegionBottom;
    private JSONObject mRegionJson;
    private int mRegionLeft;
    /* access modifiers changed from: private */
    public RectF mRegionRect;
    private int mRegionRight;
    private int mRegionTop;
    boolean mShow = false;
    public int mStatusColor;
    protected View mStatusbarView = null;
    public JSONObject mStyle = null;
    String mTouchRectJson = null;
    ArrayList<RectF> mTouchRects = new ArrayList<>();
    float mTouchX;
    float mTouchY;
    public String mUUID = null;
    protected IWebview mWebView;

    class NativeCanvasView extends View implements View.OnClickListener {
        /* access modifiers changed from: private */
        public Runnable clickEventRunnable = null;
        HashMap<String, HashMap<String, IWebview>> doEventListenerMap = new HashMap<>(2);
        boolean isAddDoubleClickEvent = false;
        boolean isTouchDown = false;
        long mCurClickTime = 0;
        long mLastClickTime = 0;

        public NativeCanvasView(Context context) {
            super(context);
            setOnClickListener(this);
            setClickable(false);
        }

        private void drawRect(Canvas canvas, int i, int i2, int i3, int i4, Overlay overlay) {
            NativeView.this.mPaint.reset();
            NativeView.this.mPaint.setAntiAlias(true);
            RectF rectF = new RectF((float) i, (float) i2, (float) i3, (float) i4);
            if (overlay.borderWidth <= 0.0f) {
                NativeView.this.mPaint.setColor(overlay.mRectColor);
                canvas.drawRoundRect(rectF, overlay.radius, overlay.radius, NativeView.this.mPaint);
                return;
            }
            int i5 = overlay.mRectColor;
            if ((-16777216 & i5) == 0) {
                NativeView.this.mPaint.setStyle(Paint.Style.STROKE);
                NativeView.this.mPaint.setStrokeWidth(overlay.borderWidth);
                NativeView.this.mPaint.setColor(overlay.borderColor);
                canvas.drawRoundRect(rectF, overlay.radius, overlay.radius, NativeView.this.mPaint);
                return;
            }
            NativeView.this.mPaint.setColor(i5);
            NativeView.this.mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRoundRect(rectF, overlay.radius, overlay.radius, NativeView.this.mPaint);
            NativeView.this.mPaint.setStyle(Paint.Style.STROKE);
            NativeView.this.mPaint.setStrokeWidth(overlay.borderWidth);
            NativeView.this.mPaint.setColor(overlay.borderColor);
            canvas.drawRoundRect(rectF, overlay.radius, overlay.radius, NativeView.this.mPaint);
        }

        private void initAuto(Overlay overlay) {
            Rect rect = overlay.mDestRect;
            if (rect.left == Integer.MIN_VALUE) {
                NativeView nativeView = NativeView.this;
                int i = nativeView.mInnerLeft;
                int i2 = nativeView.mInnerWidth;
                int i3 = rect.right;
                int i4 = i + ((i2 - i3) / 2);
                rect.left = i4;
                rect.right = i3 + i4;
            }
            if (rect.top == Integer.MIN_VALUE) {
                NativeView nativeView2 = NativeView.this;
                int i5 = nativeView2.mInnerTop;
                int i6 = nativeView2.mInnerHeight;
                int i7 = rect.bottom;
                int i8 = i5 + ((i6 - i7) / 2);
                rect.top = i8;
                rect.bottom = i7 + i8;
            }
        }

        private void postDelayedClickEvent() {
            if (this.clickEventRunnable == null) {
                this.clickEventRunnable = new Runnable() {
                    public void run() {
                        NativeCanvasView.this.doTypeEvent(Constants.Event.CLICK);
                        Runnable unused = NativeCanvasView.this.clickEventRunnable = null;
                    }
                };
            }
            postDelayed(this.clickEventRunnable, 300);
        }

        /* access modifiers changed from: package-private */
        public void addEventListener(String str, IWebview iWebview, String str2) {
            HashMap hashMap = this.doEventListenerMap.get(str);
            if (hashMap == null) {
                hashMap = new HashMap(2);
                this.doEventListenerMap.put(str, hashMap);
            }
            boolean z = true;
            if (TextUtils.equals(str, Constants.Event.CLICK)) {
                setClickable(true);
            }
            if (TextUtils.equals(str, "doubleclick")) {
                this.isAddDoubleClickEvent = true;
            }
            Iterator<String> it = this.doEventListenerMap.keySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String next = it.next();
                HashMap hashMap2 = this.doEventListenerMap.get(next);
                if (next != null && hashMap2.containsValue(iWebview)) {
                    z = false;
                    break;
                }
            }
            if (z) {
                iWebview.obtainFrameView().addFrameViewListener(new IEventCallback() {
                    public Object onCallBack(String str, Object obj) {
                        if (str != AbsoluteConst.EVENTS_CLOSE && str != AbsoluteConst.EVENTS_WINDOW_CLOSE) {
                            return null;
                        }
                        for (String next : NativeCanvasView.this.doEventListenerMap.keySet()) {
                            HashMap hashMap = NativeCanvasView.this.doEventListenerMap.get(next);
                            if (!hashMap.isEmpty()) {
                                Set keySet = hashMap.keySet();
                                int size = keySet.size();
                                String[] strArr = new String[size];
                                keySet.toArray(strArr);
                                for (int i = size - 1; i >= 0; i--) {
                                    String str2 = strArr[i];
                                    if (hashMap.get(str2) == obj) {
                                        hashMap.remove(str2);
                                    }
                                }
                            }
                            if (hashMap.isEmpty() && TextUtils.equals(next, Constants.Event.CLICK)) {
                                NativeCanvasView.this.setClickable(false);
                            }
                        }
                        ((IWebview) obj).obtainFrameView().removeFrameViewListener(this);
                        return null;
                    }
                });
            }
            hashMap.put(str2, iWebview);
        }

        public boolean doTypeEvent(String str) {
            HashMap hashMap = this.doEventListenerMap.get(str);
            boolean z = false;
            if (hashMap != null) {
                for (String str2 : hashMap.keySet()) {
                    z = true;
                    Deprecated_JSUtil.execCallback((IWebview) hashMap.get(str2), str2, NativeView.this.getEventJSON(), JSUtil.OK, true, true);
                }
            }
            return z;
        }

        public boolean listenClick() {
            HashMap hashMap = this.doEventListenerMap.get(Constants.Event.CLICK);
            return hashMap != null && !hashMap.isEmpty();
        }

        public void onClick(View view) {
            NativeView nativeView = NativeView.this;
            if (nativeView.checkTouchRectsContains(nativeView.mTouchX, nativeView.mTouchY)) {
                this.mLastClickTime = this.mCurClickTime;
                long currentTimeMillis = System.currentTimeMillis();
                this.mCurClickTime = currentTimeMillis;
                boolean z = this.isAddDoubleClickEvent;
                if (z && currentTimeMillis - this.mLastClickTime < 300) {
                    Runnable runnable = this.clickEventRunnable;
                    if (runnable != null) {
                        removeCallbacks(runnable);
                    }
                    doTypeEvent("doubleclick");
                } else if (z) {
                    postDelayedClickEvent();
                } else {
                    doTypeEvent(Constants.Event.CLICK);
                }
            }
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            int i;
            Typeface create;
            Canvas canvas2 = canvas;
            canvas.save();
            int i2 = 0;
            canvas2.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
            NativeView nativeView = NativeView.this;
            canvas2.clipRect(nativeView.mInnerLeft, nativeView.mInnerTop, nativeView.mInnerRight, nativeView.mInnerBottom);
            if (NativeView.this.getViewType().equals(AbsoluteConst.NATIVE_NVIEW)) {
                canvas2.drawColor(NativeView.this.mBackGroundColor);
            }
            if (NativeView.this.mRegionRect != null) {
                canvas2.clipRect(NativeView.this.mRegionRect, Region.Op.DIFFERENCE);
            }
            Iterator<Overlay> it = NativeView.this.mOverlays.iterator();
            while (it.hasNext()) {
                Overlay next = it.next();
                if (next.type.equals("clear")) {
                    canvas2.clipRect(next.mDestRect, Region.Op.DIFFERENCE);
                }
            }
            Iterator<Overlay> it2 = NativeView.this.mOverlays.iterator();
            while (it2.hasNext()) {
                Overlay next2 = it2.next();
                NativeView.this.mPaint.reset();
                canvas.save();
                NativeBitmap nativeBitmap = next2.mNativeBitmap;
                if (nativeBitmap != null && nativeBitmap.getBitmap() != null && !next2.mNativeBitmap.isRecycled() && !next2.mNativeBitmap.isGif()) {
                    Rect rect = next2.mDestRect;
                    if (rect.left == Integer.MIN_VALUE || rect.top == Integer.MIN_VALUE) {
                        initAuto(next2);
                        canvas2.drawBitmap(next2.mNativeBitmap.getBitmap(), next2.mSrcRect, next2.mDestRect, NativeView.this.mPaint);
                    } else {
                        canvas2.clipRect(rect);
                        canvas2.drawBitmap(next2.mNativeBitmap.getBitmap(), next2.mSrcRect, next2.mDestRect, NativeView.this.mPaint);
                    }
                } else if (next2.mText != null) {
                    initAuto(next2);
                    canvas2.clipRect(next2.mDestRect);
                    NativeView.this.mPaint.reset();
                    NativeView.this.mPaint.setAntiAlias(true);
                    int i3 = next2.mFontColor;
                    if (i3 != 0) {
                        NativeView.this.mPaint.setColor(i3);
                    }
                    if (next2.mFontColor == 0) {
                        NativeView.this.mPaint.setColor(i2);
                    }
                    float f = next2.mFontSize;
                    if (f != 0.0f) {
                        NativeView.this.mPaint.setTextSize(f);
                    }
                    if (!TextUtils.isEmpty(next2.textTTFPh)) {
                        Typeface typeface = NativeTypefaceFactory.getTypeface(NativeView.this.mApp, next2.textTTFPh);
                        if (typeface != null) {
                            NativeView.this.mPaint.setTypeface(typeface);
                        }
                    } else if (!TextUtils.isEmpty(next2.textFamily) && (create = Typeface.create(next2.textFamily, i2)) != null) {
                        NativeView.this.mPaint.setTypeface(create);
                    }
                    NativeView.this.mPaint.setFakeBoldText(next2.textWeight.equals(FrameBitmapView.BOLD));
                    if (next2.textStyle.equals(FrameBitmapView.ITALIC)) {
                        NativeView.this.mPaint.setTextSkewX(-0.5f);
                    }
                    NativeView.this.mPaint.setTextAlign(Paint.Align.CENTER);
                    int centerX = next2.mDestRect.centerX();
                    if (next2.textAlign.equals("right")) {
                        NativeView.this.mPaint.setTextAlign(Paint.Align.RIGHT);
                        centerX = next2.mDestRect.right;
                    } else if (next2.textAlign.equals("left")) {
                        NativeView.this.mPaint.setTextAlign(Paint.Align.LEFT);
                        centerX = next2.mDestRect.left;
                    }
                    String str = next2.mText;
                    if (next2.textDecoration.equals("underline")) {
                        NativeView.this.mPaint.setUnderlineText(true);
                    } else if (next2.textDecoration.equals("line-through")) {
                        NativeView.this.mPaint.setFlags(16);
                    }
                    TextPaint textPaint = new TextPaint();
                    textPaint.set(NativeView.this.mPaint);
                    if (next2.textWhiteSpace.equals("normal")) {
                        int width = next2.mDestRect.width();
                        Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
                        Object obj = Constants.Name.ELLIPSIS;
                        Object obj2 = "top";
                        StaticLayout staticLayout = new StaticLayout(str, textPaint, width, alignment, next2.textLineSpacing + 0.9f, 0.0f, true);
                        if (!next2.textAdapt && staticLayout.getHeight() > next2.mDestRect.height()) {
                            int lineEnd = staticLayout.getLineEnd((next2.mDestRect.height() / (staticLayout.getHeight() / staticLayout.getLineCount())) - 1);
                            String str2 = next2.textOverflow.equals(obj) ? "…" : "";
                            StringBuilder sb = new StringBuilder();
                            if (!TextUtils.isEmpty(str2)) {
                                lineEnd--;
                            }
                            sb.append(str.substring(0, lineEnd));
                            sb.append(str2);
                            staticLayout = new StaticLayout(sb.toString(), textPaint, next2.mDestRect.width(), Layout.Alignment.ALIGN_NORMAL, next2.textLineSpacing + 0.9f, 0.0f, false);
                        }
                        int height = (next2.mDestRect.height() - staticLayout.getHeight()) / 2;
                        if (next2.textVerticalAligin.equals(obj2)) {
                            height = 0;
                        } else if (next2.textVerticalAligin.equals("bottom")) {
                            height = next2.mDestRect.height() - staticLayout.getHeight();
                        }
                        canvas.save();
                        canvas2.translate((float) centerX, (float) (height + next2.mDestRect.top));
                        staticLayout.draw(canvas2);
                        canvas.restore();
                    } else {
                        Object obj3 = "top";
                        if (next2.textOverflow.equals(Constants.Name.ELLIPSIS)) {
                            str = TextUtils.ellipsize(str, textPaint, (float) next2.mDestRect.width(), TextUtils.TruncateAt.END).toString();
                        }
                        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
                        float f2 = fontMetrics.top;
                        float f3 = fontMetrics.bottom;
                        int centerY = (int) ((((float) next2.mDestRect.centerY()) - (f2 / 2.0f)) - (f3 / 2.0f));
                        if (next2.textVerticalAligin.equals(obj3)) {
                            centerY = (int) (((float) next2.mDestRect.top) - f2);
                        } else if (next2.textVerticalAligin.equals("bottom")) {
                            centerY = (int) (((float) next2.mDestRect.bottom) - f3);
                        }
                        canvas2.drawText(str, (float) centerX, (float) centerY, textPaint);
                    }
                } else if (next2.type.equals("rect")) {
                    canvas.save();
                    Rect rect2 = next2.mDestRect;
                    int i4 = rect2.left;
                    if (i4 == Integer.MIN_VALUE || (i = rect2.top) == Integer.MIN_VALUE) {
                        int i5 = rect2.top;
                        int i6 = rect2.right;
                        int i7 = rect2.bottom;
                        if (i4 == Integer.MIN_VALUE) {
                            NativeView nativeView2 = NativeView.this;
                            int i8 = (nativeView2.mInnerWidth - (i6 - nativeView2.mInnerLeft)) / 2;
                            i6 += i8;
                            i4 = i8;
                        }
                        if (i5 == Integer.MIN_VALUE) {
                            NativeView nativeView3 = NativeView.this;
                            i5 = (nativeView3.mInnerHeight - (i7 - nativeView3.mInnerTop)) / 2;
                            i7 += i5;
                        }
                        drawRect(canvas, i4, i5, i6, i7, next2);
                    } else {
                        drawRect(canvas, i4, i, rect2.right, rect2.bottom, next2);
                    }
                    canvas.restore();
                }
                canvas.restore();
                i2 = 0;
            }
            canvas.restore();
        }

        /* access modifiers changed from: protected */
        public void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            NativeView nativeView = NativeView.this;
            if (!(size == nativeView.mAppScreenWidth && size2 == nativeView.mAppScreenHeight)) {
                nativeView.mAppScreenWidth = size;
                nativeView.mAppScreenHeight = size2;
                nativeView.init();
                Logger.e("NativeView", NativeView.this.mAppScreenWidth + ";onMeasure;" + NativeView.this.mAppScreenHeight);
            }
            NativeView nativeView2 = NativeView.this;
            int i3 = nativeView2.mInnerHeight + nativeView2.mInnerTop;
            int i4 = nativeView2.mAppScreenHeight;
            if (i3 <= i4) {
                i3 = i4;
            }
            nativeView2.mAppScreenHeight = i3;
            nativeView2.measureFitViewParent(false);
            IWebview iWebview = NativeView.this.mWebView;
            if (!(iWebview == null || iWebview.obtainApp() == null)) {
                Iterator<Overlay> it = NativeView.this.mOverlays.iterator();
                while (it.hasNext()) {
                    it.next().parseJson(NativeView.this.mWebView);
                }
            }
            NativeView nativeView3 = NativeView.this;
            setMeasuredDimension(nativeView3.mAppScreenWidth, nativeView3.mAppScreenHeight);
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            NativeView.this.mTouchX = motionEvent.getX();
            NativeView.this.mTouchY = motionEvent.getY();
            NativeView nativeView = NativeView.this;
            if (!nativeView.mIntercept) {
                return false;
            }
            boolean access$600 = nativeView.checkTouchRectsContains(nativeView.mTouchX, nativeView.mTouchY);
            if (actionMasked != 0) {
                if (actionMasked == 1) {
                    this.isTouchDown = false;
                    if (access$600) {
                        doTypeEvent("touchend");
                    }
                } else if (actionMasked != 2) {
                    if (actionMasked == 3) {
                        this.isTouchDown = false;
                        if (access$600) {
                            doTypeEvent("touchcancel");
                        }
                    }
                } else if (this.isTouchDown) {
                    doTypeEvent("touchmove");
                }
            } else if (access$600) {
                this.isTouchDown = true;
                doTypeEvent(AbsoluteConst.EVENTS_WEBVIEW_ONTOUCH_START);
            }
            if (access$600) {
                if (NativeView.this.mIntercept) {
                    return listenClick() ? super.onTouchEvent(motionEvent) : NativeView.this.mIntercept;
                }
                return false;
            } else if (listenClick()) {
                return false;
            } else {
                return super.onTouchEvent(motionEvent);
            }
        }
    }

    class Overlay {
        /* access modifiers changed from: private */
        public int borderColor;
        /* access modifiers changed from: private */
        public int borderRadius;
        /* access modifiers changed from: private */
        public float borderWidth;
        String callBackId;
        int inputBackgroundColor;
        String inputOnBlurCallBackId;
        String inputOnFocusCallBackId;
        String inputType;
        JSONObject mDestJson;
        Rect mDestRect;
        int mFontColor = -16777216;
        float mFontSize;
        NativeBitmap mNativeBitmap;
        NativeView mNativeView;
        int mRectColor;
        JSONObject mSrcJson;
        Rect mSrcRect;
        JSONObject mStyleJson;
        String mText;
        int margin;
        String placeholder;
        int placeholderColor;
        /* access modifiers changed from: private */
        public float radius;
        boolean textAdapt;
        String textAlign;
        String textDecoration;
        String textFamily;
        float textLineSpacing;
        String textOverflow;
        String textStyle;
        String textTTFPh;
        String textVerticalAligin;
        String textWeight;
        String textWhiteSpace;
        String type;
        IWebview webview;

        Overlay() {
            String str = FrameBitmapView.NORMAL;
            this.textWeight = str;
            this.textStyle = str;
            this.textFamily = "";
            this.textAlign = "center";
            this.textTTFPh = null;
            this.textOverflow = "clip";
            this.textDecoration = "none";
            this.textWhiteSpace = "nowrap";
            this.textVerticalAligin = "middle";
            this.textLineSpacing = 0.2f;
            this.textAdapt = false;
            this.margin = 0;
            this.borderWidth = -1.0f;
            this.borderColor = -1;
            this.borderRadius = 0;
            this.radius = 0.0f;
            this.inputType = "text";
            this.inputBackgroundColor = 0;
            this.placeholder = "";
        }

        /* access modifiers changed from: package-private */
        public void parseJson(IWebview iWebview) {
            int i;
            NativeBitmap nativeBitmap = this.mNativeBitmap;
            if (nativeBitmap != null) {
                this.mSrcRect = NativeView.makeBitmapSrcRect(this.mNativeView, this.mSrcJson, nativeBitmap);
                if (this.mNativeBitmap.isNetWorkBitmap()) {
                    this.mNativeBitmap.initNetworkBitmap(new ImageLoadingListener() {
                        public void onLoadingCancelled(String str, View view) {
                        }

                        public void onLoadingComplete(String str, View view, Bitmap bitmap) {
                            NativeBitmap nativeBitmap = Overlay.this.mNativeBitmap;
                            if (nativeBitmap != null) {
                                nativeBitmap.setBitmap(bitmap);
                                Overlay.this.mNativeBitmap.setNetWorkBitmapDownload(true);
                            }
                            NativeView nativeView = Overlay.this.mNativeView;
                            if (nativeView != null && nativeView.getParent() != null) {
                                Overlay overlay = Overlay.this;
                                NativeView nativeView2 = overlay.mNativeView;
                                if (nativeView2.mCanvasView != null) {
                                    overlay.mSrcRect = NativeView.makeBitmapSrcRect(nativeView2, overlay.mSrcJson, overlay.mNativeBitmap);
                                    Overlay overlay2 = Overlay.this;
                                    overlay2.mDestRect = NativeView.this.makeRect(overlay2.mNativeView, overlay2.mDestJson, this);
                                    if (Overlay.this.mNativeBitmap.isGif()) {
                                        NativeView.this.addGifImagview(this);
                                    } else {
                                        Overlay.this.mNativeView.mCanvasView.invalidate();
                                    }
                                }
                            }
                        }

                        public void onLoadingFailed(String str, View view, FailReason failReason) {
                        }

                        public void onLoadingStarted(String str, View view) {
                        }
                    });
                }
            }
            JSONObject jSONObject = this.mStyleJson;
            if (jSONObject != null) {
                String optString = jSONObject.optString("color");
                if (!TextUtils.isEmpty(optString) && !optString.equals("null")) {
                    try {
                        this.mFontColor = Color.parseColor(optString);
                    } catch (Exception unused) {
                        this.mFontColor = PdrUtil.stringToColor(optString);
                    }
                }
                String optString2 = this.mStyleJson.optString(AbsoluteConst.JSON_KEY_SIZE);
                String str = "16px";
                if (TextUtils.isEmpty(optString2)) {
                    optString2 = str;
                }
                NativeView nativeView = this.mNativeView;
                this.mFontSize = (float) PdrUtil.convertToScreenInt(optString2, nativeView.mInnerWidth, 0, nativeView.mCreateScale);
                this.textWeight = this.mStyleJson.optString("weight", this.textWeight);
                this.textStyle = this.mStyleJson.optString("style", this.textStyle);
                this.textFamily = this.mStyleJson.optString("family", this.textFamily);
                this.textAlign = this.mStyleJson.optString(AbsoluteConst.JSON_KEY_ALIGN, this.textAlign);
                this.textOverflow = this.mStyleJson.optString(Constants.Name.OVERFLOW, this.textOverflow);
                this.textDecoration = this.mStyleJson.optString("decoration", this.textDecoration);
                this.textWhiteSpace = this.mStyleJson.optString("whiteSpace", this.textWhiteSpace);
                this.textVerticalAligin = this.mStyleJson.optString(AbsoluteConst.JSON_KEY_VERTICAL_ALIGN, this.textVerticalAligin);
                String str2 = "0px";
                if (this.mStyleJson.has(Constants.Name.BORDER_WIDTH)) {
                    String optString3 = this.mStyleJson.optString(Constants.Name.BORDER_WIDTH);
                    if (TextUtils.isEmpty(optString3)) {
                        optString3 = str2;
                    }
                    NativeView nativeView2 = this.mNativeView;
                    this.borderWidth = (float) PdrUtil.convertToScreenInt(optString3, nativeView2.mInnerWidth, 0, nativeView2.mCreateScale);
                }
                this.borderColor = this.mRectColor;
                if (this.mStyleJson.has(Constants.Name.BORDER_COLOR)) {
                    this.borderColor = PdrUtil.stringToColor(this.mStyleJson.optString(Constants.Name.BORDER_COLOR));
                }
                if (this.mStyleJson.has("radius")) {
                    String optString4 = this.mStyleJson.optString("radius");
                    if (TextUtils.isEmpty(optString4)) {
                        optString4 = str2;
                    }
                    NativeView nativeView3 = this.mNativeView;
                    this.radius = (float) PdrUtil.convertToScreenInt(optString4, nativeView3.mInnerWidth, 0, nativeView3.mCreateScale);
                }
                if (this.mStyleJson.has("lineSpacing")) {
                    this.textLineSpacing = ((float) PdrUtil.convertToScreenInt(this.mStyleJson.optString("lineSpacing"), (int) this.mFontSize, 0, this.mNativeView.mCreateScale)) / this.mFontSize;
                }
                if (this.mStyleJson.has("fontSrc")) {
                    String optString5 = this.mStyleJson.optString("fontSrc", "");
                    if (optString5.contains("__wap2app.ttf")) {
                        String str3 = BaseInfo.sBaseWap2AppTemplatePath + "wap2app__template/__wap2app.ttf";
                        if (new File(str3).exists()) {
                            this.textTTFPh = str3;
                        } else {
                            this.textTTFPh = iWebview.obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), optString5);
                        }
                    } else {
                        this.textTTFPh = iWebview.obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), optString5);
                    }
                }
                String optString6 = this.mStyleJson.optString("margin", str2);
                NativeView nativeView4 = this.mNativeView;
                this.margin = PdrUtil.convertToScreenInt(optString6, nativeView4.mInnerWidth, 0, nativeView4.mCreateScale);
                if ("input".equals(this.type)) {
                    this.inputType = "text";
                    if (this.mStyleJson.has("type")) {
                        String optString7 = this.mStyleJson.optString("type");
                        if (!TextUtils.isEmpty(optString7)) {
                            this.inputType = optString7;
                        }
                    }
                    this.placeholderColor = -7829368;
                    this.placeholder = "";
                    if (this.mStyleJson.has(Constants.Name.PLACEHOLDER)) {
                        this.placeholder = this.mStyleJson.optString(Constants.Name.PLACEHOLDER);
                    }
                    if (this.mStyleJson.has(Constants.Name.FONT_SIZE)) {
                        String optString8 = this.mStyleJson.optString(Constants.Name.FONT_SIZE);
                        if (!TextUtils.isEmpty(optString8)) {
                            str = optString8;
                        }
                        NativeView nativeView5 = this.mNativeView;
                        this.mFontSize = (float) PdrUtil.convertToScreenInt(str, nativeView5.mInnerWidth, 0, nativeView5.mCreateScale);
                    }
                    this.mFontColor = -16777216;
                    if (this.mStyleJson.has("fontColor")) {
                        String optString9 = this.mStyleJson.optString("fontColor");
                        if (!TextUtils.isEmpty(optString9)) {
                            if (Pattern.compile("^#[0-9a-fA-F]{6}$").matcher(optString9).matches()) {
                                this.mFontSize = (float) PdrUtil.stringToColor(optString9);
                            }
                            if (Pattern.compile("^rgba\\(((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\,){3}([0-1]{1}|0\\.[1-9]{1})\\)$").matcher(optString9).matches()) {
                                this.mFontSize = (float) PdrUtil.stringToColor(optString9);
                            }
                        }
                    }
                    this.inputBackgroundColor = 0;
                    if (this.mStyleJson.has("backgroundColor")) {
                        String optString10 = this.mStyleJson.optString("backgroundColor");
                        if (!TextUtils.isEmpty(optString10)) {
                            if (Pattern.compile("^#[0-9a-fA-F]{6}$").matcher(optString10).matches()) {
                                this.inputBackgroundColor = PdrUtil.stringToColor(optString10);
                            }
                            if (Pattern.compile("^rgba\\(((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\,){3}([0-1]{1}|0\\.[1-9]{1})\\)$").matcher(optString10).matches()) {
                                this.inputBackgroundColor = PdrUtil.stringToColor(optString10);
                            }
                        }
                    }
                    this.borderColor = -16777216;
                    if (this.mStyleJson.has(Constants.Name.BORDER_COLOR)) {
                        String optString11 = this.mStyleJson.optString(Constants.Name.BORDER_COLOR);
                        if (!TextUtils.isEmpty(optString11) && Pattern.compile("^#[0-9a-fA-F]{6}$").matcher(optString11).matches()) {
                            this.borderColor = PdrUtil.stringToColor(optString11);
                        }
                    }
                    NativeView nativeView6 = this.mNativeView;
                    String str4 = "1px";
                    this.borderWidth = (float) PdrUtil.convertToScreenInt(str4, nativeView6.mInnerWidth, 0, nativeView6.mCreateScale);
                    if (this.mStyleJson.has(Constants.Name.BORDER_WIDTH)) {
                        String optString12 = this.mStyleJson.optString(Constants.Name.BORDER_WIDTH);
                        if (!TextUtils.isEmpty(optString12) && Pattern.compile("^[1-9]\\d*px$").matcher(optString12).matches()) {
                            try {
                                if (!TextUtils.isEmpty(optString12)) {
                                    str4 = optString12;
                                }
                                NativeView nativeView7 = this.mNativeView;
                                this.borderWidth = (float) PdrUtil.convertToScreenInt(str4, nativeView7.mInnerWidth, 0, nativeView7.mCreateScale);
                            } catch (Exception unused2) {
                            }
                        }
                    }
                    this.borderRadius = 0;
                    if (this.mStyleJson.has(Constants.Name.BORDER_RADIUS)) {
                        String optString13 = this.mStyleJson.optString(Constants.Name.BORDER_RADIUS);
                        if (!TextUtils.isEmpty(optString13) && Pattern.compile("^[1-9]\\d*px$").matcher(optString13).matches()) {
                            try {
                                if (!TextUtils.isEmpty(optString13)) {
                                    str2 = optString13;
                                }
                                NativeView nativeView8 = this.mNativeView;
                                this.borderRadius = PdrUtil.convertToScreenInt(str2, nativeView8.mInnerWidth, 0, nativeView8.mCreateScale);
                            } catch (Exception unused3) {
                            }
                        }
                    }
                    if (this.mStyleJson.has("__onCompleteCallBackId__")) {
                        this.callBackId = this.mStyleJson.optString("__onCompleteCallBackId__");
                        this.mStyleJson.remove("__onCompleteCallBackId__");
                    } else if (this.mStyleJson.has("onComplete")) {
                        String optString14 = this.mStyleJson.optString("onComplete");
                        if (optString14.startsWith(AbsoluteConst.PROTOCOL_JAVASCRIPT) || optString14.startsWith("javaScript:")) {
                            this.callBackId = optString14;
                        }
                    }
                    if (this.mStyleJson.has("__onFocusCallBackId__")) {
                        this.inputOnFocusCallBackId = this.mStyleJson.optString("__onFocusCallBackId__");
                        this.mStyleJson.remove("__onFocusCallBackId__");
                    } else if (this.mStyleJson.has("onFocus")) {
                        String optString15 = this.mStyleJson.optString("onFocus");
                        if (optString15.startsWith(AbsoluteConst.PROTOCOL_JAVASCRIPT) || optString15.startsWith("javaScript:")) {
                            this.inputOnFocusCallBackId = optString15;
                        }
                    }
                    if (this.mStyleJson.has("__onBlurCallBackId__")) {
                        this.inputOnBlurCallBackId = this.mStyleJson.optString("__onBlurCallBackId__");
                        this.mStyleJson.remove("__onBlurCallBackId__");
                    } else if (this.mStyleJson.has("onBlur")) {
                        String optString16 = this.mStyleJson.optString("onBlur");
                        if (optString16.startsWith(AbsoluteConst.PROTOCOL_JAVASCRIPT) || optString16.startsWith("javaScript:")) {
                            this.inputOnBlurCallBackId = optString16;
                        }
                    }
                }
            }
            Rect makeRect = NativeView.this.makeRect(this.mNativeView, this.mDestJson, this);
            this.mDestRect = makeRect;
            if (makeRect != null && (i = this.margin) != 0) {
                makeRect.left += i;
                makeRect.top += i;
                makeRect.right -= i;
                makeRect.bottom -= i;
            }
        }
    }

    public NativeView(Context context, IWebview iWebview, String str, String str2, JSONObject jSONObject) {
        super(context);
        this.mCanvasView = new NativeCanvasView(context);
        setWillNotDraw(false);
        IApp obtainApp = iWebview.obtainApp();
        this.mApp = obtainApp;
        int statusBarDefaultColor = obtainApp.obtainStatusBarMgr().getStatusBarDefaultColor();
        if (statusBarDefaultColor != 0) {
            this.mStatusColor = statusBarDefaultColor;
        }
        this.mWebView = iWebview;
        this.mCreateScale = iWebview.getScale();
        this.mUUID = str;
        this.mID = str2;
        this.mStyle = jSONObject;
        this.isImmersed = this.mApp.obtainStatusBarMgr().checkImmersedStatusBar(this.mApp.getActivity(), Boolean.valueOf(this.mApp.obtainConfigProperty(AbsoluteConst.JSONKEY_STATUSBAR_IMMERSED)).booleanValue());
        setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        attachCanvasView();
        initScreenData();
        try {
            float optDouble = (float) jSONObject.optDouble("opacity", 1.0d);
            if (Build.VERSION.SDK_INT >= 11) {
                setAlpha(optDouble);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void UpdateRegionData() {
        if (this.mRegionRect != null) {
            askRegionJson();
            RectF rectF = this.mRegionRect;
            rectF.left = (float) this.mRegionLeft;
            rectF.right = (float) (this.mAppScreenWidth - this.mRegionRight);
            rectF.top = (float) (this.mRegionTop + this.mInnerTop);
            rectF.bottom = (float) (this.mInnerBottom - this.mRegionBottom);
        }
    }

    private void askRegionJson() {
        JSONObject jSONObject = this.mRegionJson;
        if (jSONObject != null) {
            this.mRegionLeft = PdrUtil.convertToScreenInt(jSONObject.optString("left"), this.mInnerWidth, 0, this.mCreateScale);
            this.mRegionRight = PdrUtil.convertToScreenInt(this.mRegionJson.optString("right"), this.mInnerWidth, 0, this.mCreateScale);
            this.mRegionTop = PdrUtil.convertToScreenInt(this.mRegionJson.optString("top"), this.mInnerHeight, 0, this.mCreateScale);
            this.mRegionBottom = PdrUtil.convertToScreenInt(this.mRegionJson.optString("bottom"), this.mInnerHeight, 0, this.mCreateScale);
        }
    }

    /* access modifiers changed from: private */
    public boolean checkTouchRectsContains(float f, float f2) {
        ArrayList<RectF> arrayList = this.mTouchRects;
        boolean z = false;
        if (arrayList != null && arrayList.size() > 0) {
            Iterator<RectF> it = this.mTouchRects.iterator();
            while (it.hasNext()) {
                if (it.next().contains(f, f2)) {
                    z = true;
                }
            }
        }
        return z;
    }

    /* access modifiers changed from: private */
    public void endAnimatecallback(IWebview iWebview, String str) {
        if (!TextUtils.isEmpty(str)) {
            Deprecated_JSUtil.execCallback(iWebview, str, (String) null, JSUtil.OK, false, false);
        }
    }

    private int getDrawLeft(int i) {
        return this.mInnerLeft + i;
    }

    private int getDrawTop(int i) {
        return this.mInnerTop + i;
    }

    private EditText getInputById(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = getChildAt(childCount);
            if (childAt != null && (childAt instanceof EditText) && childAt.getTag() != null && childAt.getTag().toString().equals(str)) {
                return (EditText) childAt;
            }
        }
        return null;
    }

    private void initScreenData() {
        int i;
        int i2;
        IApp iApp = this.mApp;
        if (iApp != null) {
            View obtainMainView = iApp.obtainWebAppRootView() != null ? this.mApp.obtainWebAppRootView().obtainMainView() : null;
            if (obtainMainView == null) {
                Object invokeMethod = PlatformUtil.invokeMethod("io.dcloud.appstream.actionbar.StreamAppActionBarUtil", "isTitlebarVisible", (Object) null, new Class[]{Activity.class, String.class}, new Object[]{this.mApp.getActivity(), this.mApp.obtainAppId()});
                if (invokeMethod instanceof Boolean ? Boolean.valueOf(invokeMethod.toString()).booleanValue() : false) {
                    Object invokeMethod2 = PlatformUtil.invokeMethod("io.dcloud.appstream.actionbar.StreamAppActionBarUtil", "getTitlebarHeightPx", (Object) null, new Class[]{Activity.class}, new Object[]{this.mApp.getActivity()});
                    if (invokeMethod2 instanceof Integer) {
                        i2 = Integer.valueOf(invokeMethod2.toString()).intValue();
                        i = i2;
                        obtainMainView = this.mApp.getActivity().getWindow().getDecorView().findViewById(16908290);
                    }
                }
                i2 = 0;
                i = i2;
                obtainMainView = this.mApp.getActivity().getWindow().getDecorView().findViewById(16908290);
            } else {
                i = 0;
            }
            if (obtainMainView != null) {
                this.mApp.getActivity().getWindowManager();
                int i3 = this.mApp.getInt(0);
                this.mAppScreenHeight = this.mApp.getInt(1) - i;
                this.mAppScreenWidth = i3 - i;
                init();
            }
        }
    }

    static Rect makeBitmapSrcRect(NativeView nativeView, JSONObject jSONObject, NativeBitmap nativeBitmap) {
        Rect rect = new Rect();
        if (nativeBitmap.getBitmap() == null) {
            return rect;
        }
        int width = nativeBitmap.getBitmap().getWidth();
        int height = nativeBitmap.getBitmap().getHeight();
        if (jSONObject != null) {
            int convertToScreenInt = PdrUtil.convertToScreenInt(jSONObject.optString("bottom"), height, 0, 1.0f);
            int convertToScreenInt2 = PdrUtil.convertToScreenInt(jSONObject.optString("right"), width, 0, 1.0f);
            int convertToScreenInt3 = PdrUtil.convertToScreenInt(jSONObject.optString("left"), width, 0, 1.0f);
            int convertToScreenInt4 = PdrUtil.convertToScreenInt(jSONObject.optString("top"), height, 0, 1.0f);
            if (convertToScreenInt2 == 0 || (jSONObject.has("width") && jSONObject.has("left"))) {
                rect.left = PdrUtil.convertToScreenInt(jSONObject.optString("left"), width, convertToScreenInt3, 1.0f);
                int convertToScreenInt5 = PdrUtil.convertToScreenInt(jSONObject.optString("width"), width, width, 1.0f) + rect.left;
                rect.right = convertToScreenInt5;
                if (convertToScreenInt5 <= width) {
                    width = convertToScreenInt5;
                }
                rect.right = width;
            } else {
                rect.right = width - convertToScreenInt2;
                if (jSONObject.has("width")) {
                    rect.left = rect.right - PdrUtil.convertToScreenInt(jSONObject.optString("width"), width, width, 1.0f);
                } else if (jSONObject.has("left")) {
                    rect.left = PdrUtil.convertToScreenInt(jSONObject.optString("left"), width, convertToScreenInt3, 1.0f);
                } else {
                    rect.left = convertToScreenInt3;
                }
            }
            if (convertToScreenInt == 0 || (jSONObject.has("height") && jSONObject.has("top"))) {
                rect.top = PdrUtil.convertToScreenInt(jSONObject.optString("top"), height, convertToScreenInt4, 1.0f);
                int convertToScreenInt6 = PdrUtil.convertToScreenInt(jSONObject.optString("height"), height, height, 1.0f) + rect.top;
                rect.bottom = convertToScreenInt6;
                if (convertToScreenInt6 <= height) {
                    height = convertToScreenInt6;
                }
                rect.bottom = height;
            } else {
                rect.bottom = height - convertToScreenInt;
                if (jSONObject.has("height")) {
                    rect.top = convertToScreenInt - PdrUtil.convertToScreenInt(jSONObject.optString("height"), height, height, 1.0f);
                } else if (jSONObject.has("top")) {
                    rect.top = PdrUtil.convertToScreenInt(jSONObject.optString("top"), height, convertToScreenInt4, 1.0f);
                } else {
                    rect.top = convertToScreenInt4;
                }
            }
        } else {
            rect.left = 0;
            rect.top = 0;
            rect.right = width;
            rect.bottom = height;
        }
        return rect;
    }

    private void measureGifImageview(int i) {
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            View childAt = getChildAt(i2);
            if (childAt instanceof GifImageView) {
                GifImageView gifImageView = (GifImageView) childAt;
                if (gifImageView.getTag() != null) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) gifImageView.getLayoutParams();
                    Rect rect = ((Overlay) gifImageView.getTag()).mDestRect;
                    int i3 = rect.top;
                    if (i3 != Integer.MIN_VALUE) {
                        layoutParams.topMargin = i3 + i;
                    } else {
                        int i4 = rect.bottom;
                        layoutParams.height = i4;
                        layoutParams.topMargin = ((this.mInnerHeight - i4) + this.mInnerTop) / 2;
                    }
                }
            }
        }
    }

    private int pxFromDp(int i) {
        return (int) TypedValue.applyDimension(1, (float) i, getResources().getDisplayMetrics());
    }

    /* access modifiers changed from: private */
    public void runDrawRectF(IWebview iWebview, String str, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        int i10 = i3;
        int i11 = i9;
        if (!this.isAnimate) {
            endAnimatecallback(iWebview, str);
            return;
        }
        if (this.mRegionRect == null) {
            this.mRegionRect = new RectF();
        }
        RectF rectF = this.mRegionRect;
        rectF.left = (float) i;
        rectF.right = (float) (this.mAppScreenWidth - i2);
        float f = (float) i10;
        rectF.top = f;
        if (i11 == i7) {
            rectF.bottom = (float) ((i6 * i11) + i10 + i8);
        } else {
            rectF.bottom = (float) ((i6 * i11) + i10);
        }
        if (this.isStatusBar) {
            float f2 = (float) DeviceInfo.sStatusBarHeight;
            rectF.top = f + f2;
            rectF.bottom += f2;
        }
        final int i12 = i9;
        final int i13 = i7;
        final IWebview iWebview2 = iWebview;
        final String str2 = str;
        final int i14 = i;
        final int i15 = i2;
        final int i16 = i3;
        final int i17 = i4;
        final int i18 = i5;
        final int i19 = i6;
        final int i20 = i8;
        postDelayed(new Runnable() {
            public void run() {
                NativeView.this.invalidate();
                int i = i12;
                int i2 = i13;
                if (i == i2) {
                    NativeView.this.endAnimatecallback(iWebview2, str2);
                } else {
                    NativeView.this.runDrawRectF(iWebview2, str2, i14, i15, i16, i17, i18, i19, i2, i20, i + 1);
                }
            }
        }, (long) i5);
    }

    private void viewPostResize(View view, ViewGroup.LayoutParams layoutParams, int i, int i2, int i3, int i4) {
        final ViewGroup.LayoutParams layoutParams2 = layoutParams;
        final int i5 = i2;
        final int i6 = i;
        final View view2 = view;
        final int i7 = i4;
        final int i8 = i3;
        view.post(new Runnable() {
            public void run() {
                ViewGroup.LayoutParams layoutParams = layoutParams2;
                layoutParams.height = i5;
                layoutParams.width = i6;
                view2.setLayoutParams(layoutParams);
                ViewHelper.setY(view2, (float) i7);
                ViewHelper.setX(view2, (float) i8);
            }
        });
    }

    public void StartAnimate(IWebview iWebview, String str, String str2) throws Exception {
        if (this.isWebAnimationRuning || TextUtils.isEmpty(str)) {
            return;
        }
        if (getParent() == null) {
            IWebview iWebview2 = iWebview;
            endAnimatecallback(iWebview, str2);
            return;
        }
        IWebview iWebview3 = iWebview;
        String str3 = str2;
        this.isAnimate = true;
        String str4 = str;
        JSONObject jSONObject = new JSONObject(str);
        String optString = jSONObject.optString("type");
        int optInt = jSONObject.optInt("duration", 200);
        int optInt2 = jSONObject.optInt("frames", 12);
        this.mRegionJson = jSONObject.optJSONObject("region");
        askRegionJson();
        int i = optInt / optInt2;
        int i2 = this.mInnerBottom - ((this.mRegionTop + this.mInnerTop) + this.mRegionBottom);
        int i3 = i2 / optInt2;
        int i4 = i2 - (i3 * optInt2);
        if (!TextUtils.isEmpty(optString) && optString.equals("shrink")) {
            runDrawRectF(iWebview, str2, this.mRegionLeft, this.mRegionRight, this.mInnerTop + this.mRegionTop, this.mInnerBottom - this.mRegionBottom, i, i3, optInt2, i4, 1);
        }
    }

    public void addEventListener(String str, IWebview iWebview, String str2) {
        this.mCanvasView.addEventListener(str, iWebview, str2);
    }

    public void addGifImagview(Overlay overlay) {
        GifDrawable gifDrawable = overlay.mNativeBitmap.getGifDrawable();
        if (gifDrawable != null) {
            GifImageView gifImageView = new GifImageView(this.mApp.getActivity());
            gifImageView.setImageDrawable(gifDrawable);
            gifImageView.setTag(overlay);
            int i = 0;
            if (this.isStatusBar) {
                i = DeviceInfo.sStatusBarHeight / 2;
            }
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(overlay.mDestRect.width(), overlay.mDestRect.height());
            Rect rect = overlay.mDestRect;
            int i2 = rect.left;
            if (i2 != Integer.MIN_VALUE) {
                layoutParams.leftMargin = i2;
            } else {
                int i3 = rect.right;
                layoutParams.width = i3;
                layoutParams.leftMargin = ((this.mInnerWidth - i3) + this.mInnerLeft) / 2;
            }
            int i4 = rect.top;
            if (i4 != Integer.MIN_VALUE) {
                layoutParams.topMargin = i4 + i;
            } else {
                int i5 = rect.bottom;
                layoutParams.height = i5;
                layoutParams.topMargin = ((this.mInnerHeight - i5) + this.mInnerTop) / 2;
            }
            addView(gifImageView, layoutParams);
            requestLayout();
            invalidate();
            NativeCanvasView nativeCanvasView = this.mCanvasView;
            if (nativeCanvasView != null) {
                nativeCanvasView.requestLayout();
                this.mCanvasView.invalidate();
            }
        }
    }

    public void addInput(final Overlay overlay, final String str) {
        EditText inputById = getInputById(str);
        if (inputById == null) {
            inputById = new EditText(this.mApp.getActivity());
            inputById.setTag(str);
            if (Build.VERSION.SDK_INT >= 14) {
                inputById.setGravity(8388627);
            } else {
                inputById.setGravity(19);
            }
            inputById.setSingleLine();
        }
        int i = 1;
        inputById.setImeOptions(1);
        if (!"text".equals(overlay.inputType)) {
            if ("email".equals(overlay.inputType)) {
                if (Build.VERSION.SDK_INT >= 11) {
                    i = 33;
                }
            } else if ("number".equals(overlay.inputType)) {
                i = 2;
            } else if ("search".equals(overlay.inputType)) {
                inputById.setImeOptions(3);
            } else if (Constants.Value.TEL.equals(overlay.inputType)) {
                i = 3;
            } else if ("url".equals(overlay.inputType)) {
                i = 17;
                inputById.setImeOptions(2);
            }
        }
        inputById.setInputType(i);
        inputById.getPaint().setTextSize(overlay.mFontSize);
        inputById.setTextColor(overlay.mFontColor);
        inputById.setHint(overlay.placeholder);
        inputById.setHintTextColor(overlay.placeholderColor);
        int access$300 = (int) overlay.borderWidth;
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(overlay.inputBackgroundColor);
        int i2 = 0;
        gradientDrawable.setShape(0);
        gradientDrawable.setStroke(access$300, overlay.borderColor);
        gradientDrawable.setCornerRadius((float) overlay.borderRadius);
        int access$500 = overlay.borderRadius + access$300;
        inputById.setPadding(access$500, access$300, access$500, access$300);
        if (Build.VERSION.SDK_INT >= 16) {
            inputById.setBackground(gradientDrawable);
        } else {
            inputById.setBackgroundDrawable(gradientDrawable);
        }
        inputById.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String str;
                IFrameView iFrameView;
                if ((i == 4 || i == 6 || i == 3 || i == 2 || (keyEvent != null && 66 == keyEvent.getKeyCode() && keyEvent.getAction() == 0)) && !TextUtils.isEmpty(overlay.callBackId)) {
                    if (overlay.callBackId.toLowerCase(Locale.ENGLISH).startsWith(AbsoluteConst.PROTOCOL_JAVASCRIPT)) {
                        AbsMgr absMgr = null;
                        String obtainAppId = NativeView.this.mApp.obtainAppId();
                        IWebview iWebview = NativeView.this.mWebView;
                        if (!(iWebview == null || iWebview.obtainFrameView() == null)) {
                            absMgr = NativeView.this.mWebView.obtainFrameView().obtainWindowMgr();
                        }
                        if (absMgr == null && (iFrameView = NativeView.this.mFrameViewParent) != null) {
                            absMgr = iFrameView.obtainWindowMgr();
                        }
                        if (absMgr != null) {
                            Object processEvent = absMgr.processEvent(IMgr.MgrType.WindowMgr, 47, obtainAppId);
                            if (processEvent instanceof IFrameView) {
                                IFrameView iFrameView2 = (IFrameView) processEvent;
                                if (!(iFrameView2.obtainWebView() == null || iFrameView2.obtainWebView().obtainWindowView() == null)) {
                                    iFrameView2.obtainWebView().loadUrl(overlay.callBackId);
                                    return true;
                                }
                            }
                        }
                        IWebview iWebview2 = overlay.webview;
                        if (iWebview2 == null || iWebview2.obtainWindowView() == null) {
                            IWebview iWebview3 = NativeView.this.mWebView;
                            if (!(iWebview3 == null || iWebview3.obtainWindowView() == null)) {
                                NativeView.this.mWebView.loadUrl(overlay.callBackId);
                                return true;
                            }
                        } else {
                            Overlay overlay = overlay;
                            overlay.webview.loadUrl(overlay.callBackId);
                            return true;
                        }
                    }
                    if (overlay.webview != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("{\"text\":\"");
                        sb.append(textView.getText());
                        sb.append("\",\"id\":");
                        if (!TextUtils.isEmpty(str)) {
                            str = JSUtil.QUOTE + str + JSUtil.QUOTE;
                        } else {
                            str = "\"\"";
                        }
                        sb.append(str);
                        sb.append(Operators.BLOCK_END_STR);
                        String sb2 = sb.toString();
                        Overlay overlay2 = overlay;
                        Deprecated_JSUtil.execCallback(overlay2.webview, overlay2.callBackId, sb2, JSUtil.OK, true, true);
                        if (overlay.webview.getOpener() != null) {
                            Deprecated_JSUtil.execCallback(overlay.webview.getOpener(), overlay.callBackId, sb2, JSUtil.OK, true, true);
                        }
                    }
                }
                return true;
            }
        });
        inputById.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
                String str;
                String str2;
                IFrameView iFrameView;
                StringBuilder sb = new StringBuilder();
                sb.append("{\"id\":");
                if (!TextUtils.isEmpty(str)) {
                    str = JSUtil.QUOTE + str + JSUtil.QUOTE;
                } else {
                    str = "\"\"";
                }
                sb.append(str);
                sb.append(Operators.BLOCK_END_STR);
                String sb2 = sb.toString();
                if (z) {
                    str2 = overlay.inputOnFocusCallBackId;
                } else {
                    str2 = overlay.inputOnBlurCallBackId;
                }
                if (!TextUtils.isEmpty(str2)) {
                    if (str2.toLowerCase(Locale.ENGLISH).startsWith(AbsoluteConst.PROTOCOL_JAVASCRIPT)) {
                        AbsMgr absMgr = null;
                        String obtainAppId = NativeView.this.mApp.obtainAppId();
                        IWebview iWebview = NativeView.this.mWebView;
                        if (!(iWebview == null || iWebview.obtainFrameView() == null)) {
                            absMgr = NativeView.this.mWebView.obtainFrameView().obtainWindowMgr();
                        }
                        if (absMgr == null && (iFrameView = NativeView.this.mFrameViewParent) != null) {
                            absMgr = iFrameView.obtainWindowMgr();
                        }
                        if (absMgr != null) {
                            Object processEvent = absMgr.processEvent(IMgr.MgrType.WindowMgr, 47, obtainAppId);
                            if (processEvent instanceof IFrameView) {
                                IFrameView iFrameView2 = (IFrameView) processEvent;
                                if (!(iFrameView2.obtainWebView() == null || iFrameView2.obtainWebView().obtainWindowView() == null)) {
                                    iFrameView2.obtainWebView().loadUrl(str2);
                                    return;
                                }
                            }
                        }
                        IWebview iWebview2 = overlay.webview;
                        if (iWebview2 == null || iWebview2.obtainWindowView() == null) {
                            IWebview iWebview3 = NativeView.this.mWebView;
                            if (!(iWebview3 == null || iWebview3.obtainWindowView() == null)) {
                                NativeView.this.mWebView.loadUrl(str2);
                                return;
                            }
                        } else {
                            overlay.webview.loadUrl(str2);
                            return;
                        }
                    }
                    IWebview iWebview4 = overlay.webview;
                    if (iWebview4 != null) {
                        Deprecated_JSUtil.execCallback(iWebview4, str2, sb2, JSUtil.OK, true, true);
                        if (overlay.webview.getOpener() != null) {
                            Deprecated_JSUtil.execCallback(overlay.webview.getOpener(), str2, sb2, JSUtil.OK, true, true);
                        }
                    }
                }
            }
        });
        if (this.isStatusBar) {
            i2 = DeviceInfo.sStatusBarHeight;
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(overlay.mDestRect.width(), overlay.mDestRect.height());
        Rect rect = overlay.mDestRect;
        int i3 = rect.left;
        if (i3 != Integer.MIN_VALUE) {
            layoutParams.leftMargin = i3;
        } else {
            int i4 = rect.right;
            layoutParams.width = i4;
            layoutParams.leftMargin = ((this.mInnerWidth - i4) + this.mInnerLeft) / 2;
        }
        int i5 = rect.top;
        if (i5 != Integer.MIN_VALUE) {
            layoutParams.topMargin = i5 + i2;
        } else {
            int i6 = rect.bottom;
            layoutParams.height = i6;
            layoutParams.topMargin = ((this.mInnerHeight - i6) + this.mInnerTop) / 2;
        }
        addView(inputById, layoutParams);
        requestLayout();
        invalidate();
        NativeCanvasView nativeCanvasView = this.mCanvasView;
        if (nativeCanvasView != null) {
            nativeCanvasView.requestLayout();
            this.mCanvasView.invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void attachCanvasView() {
        if (this.mCanvasView.getParent() != null) {
            ((ViewGroup) this.mCanvasView.getParent()).removeView(this.mCanvasView);
        }
        addView(this.mCanvasView, new FrameLayout.LayoutParams(-1, -2));
    }

    public void attachToViewGroup(IFrameView iFrameView) {
        if (!this.mAttached) {
            this.mFrameViewParent = iFrameView;
            if (iFrameView instanceof AdaFrameView) {
                ((AdaFrameView) iFrameView).addNativeViewChild(this);
            }
            final String obtainAppId = iFrameView.obtainApp().obtainAppId();
            AnonymousClass2 r1 = new IEventCallback() {
                public Object onCallBack(String str, Object obj) {
                    if (TextUtils.equals(str, AbsoluteConst.EVENTS_CLOSE)) {
                        FeatureImpl.destroyNativeView(obtainAppId, NativeView.this);
                        PlatformUtil.invokeMethod("io.dcloud.feature.ad.AdFlowFeatureImpl", "destroyNativeView", (Object) null, new Class[]{String.class, NativeView.class}, new Object[]{obtainAppId, NativeView.this});
                    } else {
                        TextUtils.equals(str, AbsoluteConst.EVENTS_FRAME_ONRESIZE);
                    }
                    return null;
                }
            };
            this.mIEventCallback = r1;
            iFrameView.addFrameViewListener(r1);
            ViewParent parent = getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(this);
                setVisibility(0);
            }
            measureFitViewParent(true);
            this.mShow = true;
            this.mAttached = true;
        }
    }

    public void clearAnimate() {
        this.isAnimate = false;
        this.mRegionRect = null;
        this.mRegionJson = null;
        invalidate();
        NativeCanvasView nativeCanvasView = this.mCanvasView;
        if (nativeCanvasView != null) {
            nativeCanvasView.requestLayout();
            this.mCanvasView.invalidate();
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
                    NativeView.this.setVisibility(8);
                    if (NativeView.this.getParent() != null) {
                        ((ViewGroup) NativeView.this.getParent()).removeView(NativeView.this);
                    }
                    NativeView.this.clearViewData();
                    NativeView.this.mFrameViewParent = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, (long) 200);
    }

    public void clearViewData() {
        NativeBitmap nativeBitmap;
        Iterator<Overlay> it = this.mOverlays.iterator();
        while (it.hasNext()) {
            Overlay next = it.next();
            if (next.type.equals(WXBasicComponentType.IMG) && (nativeBitmap = next.mNativeBitmap) != null && nativeBitmap.getBitmap() != null && !next.mNativeBitmap.isRecycled()) {
                next.mNativeBitmap.recycle(true);
            }
        }
        this.mOverlays.clear();
        this.mOverlayMaps.clear();
        this.mChildViewMaps.clear();
    }

    /* access modifiers changed from: protected */
    public void configurationCange() {
    }

    /* access modifiers changed from: package-private */
    public RectF createTouchRect(JSONObject jSONObject) {
        RectF rectF = new RectF(makeRect(this, jSONObject, (Overlay) null));
        float f = rectF.left;
        if (f != -2.14748365E9f && rectF.top != -2.14748365E9f) {
            return rectF;
        }
        float f2 = rectF.top;
        float f3 = rectF.right;
        float f4 = rectF.bottom;
        if (f == -2.14748365E9f) {
            float f5 = (float) this.mInnerLeft;
            f = ((((float) this.mInnerWidth) - (f3 - f5)) / 2.0f) + f5;
            f3 += f;
        }
        if (f2 == -2.14748365E9f) {
            float f6 = (float) this.mInnerTop;
            f2 = f6 + ((((float) this.mInnerHeight) - (f4 - f6)) / 2.0f);
            f4 += f2;
        }
        return new RectF(f, f2, f3, f4);
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        RectF rectF = this.mRegionRect;
        if (rectF != null) {
            canvas.clipRect(rectF, Region.Op.DIFFERENCE);
        }
        Iterator<Overlay> it = this.mOverlays.iterator();
        while (it.hasNext()) {
            Overlay next = it.next();
            if (next.type.equals("clear")) {
                canvas.clipRect(next.mDestRect, Region.Op.DIFFERENCE);
            }
        }
        super.dispatchDraw(canvas);
    }

    public Object doForFeature(String str, Object obj) {
        if (str.equals("clearAnimate")) {
            clearAnimate();
            return null;
        } else if (!str.equals("checkTouch")) {
            return null;
        } else {
            MotionEvent motionEvent = (MotionEvent) obj;
            if (checkTouchRectsContains(motionEvent.getX(), motionEvent.getY())) {
                return Boolean.valueOf(this.mIntercept);
            }
            return Boolean.FALSE;
        }
    }

    /* access modifiers changed from: package-private */
    public String getEventJSON() {
        return String.format(Locale.ENGLISH, T, new Object[]{Integer.valueOf((int) ((this.mTouchX - ((float) this.mInnerLeft)) / this.mCreateScale)), Integer.valueOf((int) ((this.mTouchY - ((float) this.mInnerTop)) / this.mCreateScale)), Integer.valueOf((int) (this.mTouchX / this.mCreateScale)), Integer.valueOf((int) (this.mTouchY / this.mCreateScale)), Integer.valueOf((int) (this.mTouchX / this.mCreateScale)), Integer.valueOf((int) (this.mTouchY / this.mCreateScale))});
    }

    public int getInnerBottom() {
        return this.mInnerBottom;
    }

    public int getInnerHeight() {
        return this.mInnerHeight;
    }

    public boolean getInputFocusById(String str) {
        EditText inputById = getInputById(str);
        if (inputById != null) {
            return inputById.hasFocus();
        }
        return false;
    }

    public String getInputValueById(String str) {
        EditText inputById;
        if (TextUtils.isEmpty(str) || (inputById = getInputById(str)) == null || inputById.getText() == null) {
            return null;
        }
        return inputById.getText().toString();
    }

    /* access modifiers changed from: protected */
    public int getNViewContentHeight() {
        ArrayList<Overlay> arrayList = this.mOverlays;
        if (arrayList == null) {
            return this.mAppScreenHeight;
        }
        int i = 0;
        this.mInnerHeight = this.mAppScreenHeight;
        Iterator<Overlay> it = arrayList.iterator();
        while (it.hasNext()) {
            Overlay next = it.next();
            int i2 = makeRect(this, next.mDestJson, next).bottom;
            if (i2 > i) {
                i = i2;
            }
        }
        return i;
    }

    public int getRectHeightForBitmap(NativeView nativeView, JSONObject jSONObject, Rect rect, Overlay overlay, int i) {
        if (overlay == null) {
            String optString = jSONObject.optString("height");
            int i2 = nativeView.mInnerHeight;
            return PdrUtil.convertToScreenInt(optString, i2, i2, nativeView.mCreateScale);
        }
        overlay.textAdapt = false;
        if (!jSONObject.has("height") || !"auto".equals(jSONObject.optString("height")) || rect == null) {
            if (jSONObject.has("height") && "wrap_content".equals(jSONObject.optString("height")) && PdrUtil.isEquals(overlay.type, AbsURIAdapter.FONT)) {
                overlay.textAdapt = true;
                if (!PdrUtil.isEquals(overlay.textWhiteSpace, "normal")) {
                    return (int) (overlay.mFontSize + ((float) (overlay.margin * 2)));
                }
                this.mPaint.reset();
                this.mPaint.setTextSize(overlay.mFontSize);
                return new StaticLayout(overlay.mText, new TextPaint(this.mPaint), i - (overlay.margin * 2), Layout.Alignment.ALIGN_NORMAL, overlay.textLineSpacing + 0.9f, 0.0f, false).getHeight() + (overlay.margin * 2);
            }
        } else if (jSONObject.has("width") && "auto".equals(jSONObject.optString("width"))) {
            return rect.height();
        } else {
            if (jSONObject.has("width")) {
                String optString2 = jSONObject.optString("width");
                int i3 = nativeView.mInnerWidth;
                return (int) (((float) rect.height()) * (((float) PdrUtil.convertToScreenInt(optString2, i3, i3, nativeView.mCreateScale)) / ((float) rect.width())));
            }
        }
        String optString3 = jSONObject.optString("height");
        int i4 = nativeView.mInnerHeight;
        return PdrUtil.convertToScreenInt(optString3, i4, i4, nativeView.mCreateScale);
    }

    public int getRectWidthForBitmap(NativeView nativeView, JSONObject jSONObject, Rect rect) {
        if (jSONObject.has("width") && "auto".equals(jSONObject.optString("width")) && rect != null) {
            if (jSONObject.has("height") && "auto".equals(jSONObject.optString("height"))) {
                return rect.width();
            }
            if (jSONObject.has("height")) {
                String optString = jSONObject.optString("height");
                int i = nativeView.mInnerHeight;
                return (int) (((float) rect.width()) * (((float) PdrUtil.convertToScreenInt(optString, i, i, nativeView.mCreateScale)) / ((float) rect.height())));
            }
        }
        String optString2 = jSONObject.optString("width");
        int i2 = nativeView.mInnerWidth;
        return PdrUtil.convertToScreenInt(optString2, i2, i2, nativeView.mCreateScale);
    }

    public String getStyleBackgroundColor() {
        JSONObject jSONObject = this.mStyle;
        if (jSONObject != null) {
            if (jSONObject.has("backgroudColor")) {
                return this.mStyle.optString("backgroudColor");
            }
            if (this.mStyle.has("backgroundColor")) {
                return this.mStyle.optString("backgroundColor");
            }
        }
        return null;
    }

    public int getStyleLeft() {
        return PdrUtil.convertToScreenInt(this.mStyle.optString("left"), this.mAppScreenWidth, 0, this.mCreateScale);
    }

    public int getStyleWidth() {
        return this.mInnerWidth;
    }

    public String getViewId() {
        return this.mID;
    }

    public String getViewType() {
        return AbsoluteConst.NATIVE_NVIEW;
    }

    public String getViewUUId() {
        return this.mUUID;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't wrap try/catch for region: R(4:12|13|14|15) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x003a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void init() {
        /*
            r8 = this;
            java.lang.String r0 = "background"
            java.lang.String r1 = "statusbar"
            java.lang.String r2 = "backgroundImage"
            java.lang.String r3 = "backgroundColor"
            java.lang.String r4 = "backgroudColor"
            org.json.JSONObject r5 = r8.mStyle
            r6 = 1
            if (r5 == 0) goto L_0x00c7
            boolean r5 = r5.has(r4)     // Catch:{ Exception -> 0x00c3 }
            r7 = 0
            if (r5 == 0) goto L_0x001d
            org.json.JSONObject r3 = r8.mStyle     // Catch:{ Exception -> 0x00c3 }
            java.lang.String r3 = r3.optString(r4)     // Catch:{ Exception -> 0x00c3 }
            goto L_0x002d
        L_0x001d:
            org.json.JSONObject r4 = r8.mStyle     // Catch:{ Exception -> 0x00c3 }
            boolean r4 = r4.has(r3)     // Catch:{ Exception -> 0x00c3 }
            if (r4 == 0) goto L_0x002c
            org.json.JSONObject r4 = r8.mStyle     // Catch:{ Exception -> 0x00c3 }
            java.lang.String r3 = r4.optString(r3)     // Catch:{ Exception -> 0x00c3 }
            goto L_0x002d
        L_0x002c:
            r3 = r7
        L_0x002d:
            boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Exception -> 0x00c3 }
            if (r4 != 0) goto L_0x0041
            int r4 = android.graphics.Color.parseColor(r3)     // Catch:{ Exception -> 0x003a }
            r8.mBackGroundColor = r4     // Catch:{ Exception -> 0x003a }
            goto L_0x0052
        L_0x003a:
            int r3 = io.dcloud.common.util.PdrUtil.stringToColor(r3)     // Catch:{ Exception -> 0x00c3 }
            r8.mBackGroundColor = r3     // Catch:{ Exception -> 0x00c3 }
            goto L_0x0052
        L_0x0041:
            java.lang.String r3 = "ImageSlider"
            java.lang.String r4 = r8.getViewType()     // Catch:{ Exception -> 0x00c3 }
            boolean r3 = android.text.TextUtils.equals(r3, r4)     // Catch:{ Exception -> 0x00c3 }
            if (r3 == 0) goto L_0x0052
            r3 = -1118482(0xffffffffffeeeeee, float:NaN)
            r8.mBackGroundColor = r3     // Catch:{ Exception -> 0x00c3 }
        L_0x0052:
            org.json.JSONObject r3 = r8.mStyle     // Catch:{ Exception -> 0x00c3 }
            boolean r3 = r3.has(r2)     // Catch:{ Exception -> 0x00c3 }
            if (r3 == 0) goto L_0x0060
            org.json.JSONObject r3 = r8.mStyle     // Catch:{ Exception -> 0x00c3 }
            java.lang.String r7 = r3.optString(r2)     // Catch:{ Exception -> 0x00c3 }
        L_0x0060:
            if (r7 == 0) goto L_0x006e
            java.lang.String r2 = r8.mBackgroundImageSrc     // Catch:{ Exception -> 0x00c3 }
            if (r2 == 0) goto L_0x006c
            boolean r2 = r2.equalsIgnoreCase(r7)     // Catch:{ Exception -> 0x00c3 }
            if (r2 != 0) goto L_0x006e
        L_0x006c:
            r8.mBackgroundImageSrc = r7     // Catch:{ Exception -> 0x00c3 }
        L_0x006e:
            org.json.JSONObject r2 = r8.mStyle     // Catch:{ Exception -> 0x00c3 }
            boolean r2 = r2.has(r1)     // Catch:{ Exception -> 0x00c3 }
            if (r2 == 0) goto L_0x00c7
            boolean r2 = io.dcloud.common.util.BaseInfo.isImmersive     // Catch:{ Exception -> 0x00c3 }
            if (r2 == 0) goto L_0x00c7
            int r2 = android.os.Build.VERSION.SDK_INT     // Catch:{ Exception -> 0x00c3 }
            r3 = 19
            if (r2 < r3) goto L_0x00c7
            r8.isStatusBar = r6     // Catch:{ Exception -> 0x00c3 }
            org.json.JSONObject r2 = r8.mStyle     // Catch:{ Exception -> 0x00c3 }
            org.json.JSONObject r1 = r2.optJSONObject(r1)     // Catch:{ Exception -> 0x00c3 }
            if (r1 == 0) goto L_0x00a4
            boolean r2 = r1.has(r0)     // Catch:{ Exception -> 0x00c3 }
            if (r2 == 0) goto L_0x00a4
            java.lang.String r0 = r1.optString(r0)     // Catch:{ Exception -> 0x00c3 }
            int r1 = io.dcloud.common.util.PdrUtil.stringToColor(r0)     // Catch:{ Exception -> 0x00c3 }
            boolean r1 = io.dcloud.common.util.PdrUtil.checkStatusbarColor(r1)     // Catch:{ Exception -> 0x00c3 }
            if (r1 == 0) goto L_0x00a4
            int r0 = io.dcloud.common.util.PdrUtil.stringToColor(r0)     // Catch:{ Exception -> 0x00c3 }
            r8.mStatusColor = r0     // Catch:{ Exception -> 0x00c3 }
        L_0x00a4:
            java.lang.String r0 = "TitleNView"
            java.lang.String r1 = r8.getViewType()     // Catch:{ Exception -> 0x00c3 }
            boolean r0 = r0.equals(r1)     // Catch:{ Exception -> 0x00c3 }
            if (r0 == 0) goto L_0x00c7
            int r0 = r8.mBackGroundColor     // Catch:{ Exception -> 0x00c3 }
            if (r0 == 0) goto L_0x00c7
            int r1 = r8.mStatusColor     // Catch:{ Exception -> 0x00c3 }
            if (r1 == r0) goto L_0x00c7
            boolean r0 = io.dcloud.common.util.PdrUtil.checkStatusbarColor(r0)     // Catch:{ Exception -> 0x00c3 }
            if (r0 == 0) goto L_0x00c7
            int r0 = r8.mBackGroundColor     // Catch:{ Exception -> 0x00c3 }
            r8.mStatusColor = r0     // Catch:{ Exception -> 0x00c3 }
            goto L_0x00c7
        L_0x00c3:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00c7:
            int r0 = io.dcloud.common.adapter.util.DeviceInfo.sStatusBarHeight
            r8.initStatusBarView(r0)
            org.json.JSONObject r0 = r8.mStyle
            java.lang.String r1 = "left"
            java.lang.String r0 = r0.optString(r1)
            int r1 = r8.mAppScreenWidth
            float r2 = r8.mCreateScale
            r3 = 0
            int r0 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r0, r1, r3, r2)
            r8.mInnerLeft = r0
            org.json.JSONObject r0 = r8.mStyle
            java.lang.String r1 = "top"
            java.lang.String r0 = r0.optString(r1)
            int r2 = r8.mAppScreenHeight
            float r4 = r8.mCreateScale
            int r0 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r0, r2, r3, r4)
            r8.mMarginTop = r0
            r8.mInnerTop = r0
            org.json.JSONObject r0 = r8.mStyle
            java.lang.String r2 = "width"
            java.lang.String r0 = r0.optString(r2)
            int r2 = r8.mAppScreenWidth
            float r4 = r8.mCreateScale
            int r0 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r0, r2, r2, r4)
            int r2 = r8.mInnerLeft
            int r0 = r0 + r2
            r8.mInnerRight = r0
            org.json.JSONObject r0 = r8.mStyle
            java.lang.String r2 = "height"
            java.lang.String r0 = r0.optString(r2)
            boolean r4 = android.text.TextUtils.isEmpty(r0)
            if (r4 != 0) goto L_0x0123
            java.lang.String r4 = "wrap_content"
            boolean r4 = r0.equals(r4)
            if (r4 == 0) goto L_0x0123
            r8.isLayoutAdapt = r6
            goto L_0x0125
        L_0x0123:
            r8.isLayoutAdapt = r3
        L_0x0125:
            org.json.JSONObject r4 = r8.mStyle
            java.lang.String r5 = "bottom"
            boolean r4 = r4.has(r5)
            if (r4 == 0) goto L_0x0187
            boolean r4 = r8.isLayoutAdapt
            if (r4 != 0) goto L_0x0187
            org.json.JSONObject r4 = r8.mStyle
            java.lang.String r4 = r4.optString(r5)
            int r5 = r8.mAppScreenHeight
            float r6 = r8.mCreateScale
            int r4 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r4, r5, r5, r6)
            r8.mMarginBottom = r4
            int r5 = r8.mAppScreenHeight
            int r5 = r5 - r4
            r8.mInnerBottom = r5
            org.json.JSONObject r4 = r8.mStyle
            boolean r4 = r4.has(r2)
            if (r4 == 0) goto L_0x016c
            org.json.JSONObject r4 = r8.mStyle
            boolean r1 = r4.has(r1)
            if (r1 != 0) goto L_0x016c
            int r1 = r8.mAppScreenHeight
            float r2 = r8.mCreateScale
            int r0 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r0, r1, r1, r2)
            r8.mInnerHeight = r0
            int r1 = r8.mInnerBottom
            int r1 = r1 - r0
            r8.mInnerTop = r1
            if (r1 >= 0) goto L_0x01af
            r8.mInnerTop = r3
            goto L_0x01af
        L_0x016c:
            org.json.JSONObject r1 = r8.mStyle
            boolean r1 = r1.has(r2)
            if (r1 != 0) goto L_0x017c
            int r0 = r8.mInnerBottom
            int r1 = r8.mInnerTop
            int r0 = r0 - r1
            r8.mInnerHeight = r0
            goto L_0x01af
        L_0x017c:
            int r1 = r8.mAppScreenHeight
            float r2 = r8.mCreateScale
            int r0 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r0, r1, r1, r2)
            r8.mInnerHeight = r0
            goto L_0x01af
        L_0x0187:
            boolean r1 = r8.isLayoutAdapt
            if (r1 == 0) goto L_0x0192
            int r0 = r8.getNViewContentHeight()
            r8.mInnerHeight = r0
            goto L_0x019c
        L_0x0192:
            int r1 = r8.mAppScreenHeight
            float r2 = r8.mCreateScale
            int r0 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r0, r1, r1, r2)
            r8.mInnerHeight = r0
        L_0x019c:
            int r0 = r8.mInnerHeight
            int r1 = r8.mAppScreenHeight
            if (r0 >= r1) goto L_0x01ad
            boolean r1 = r8.isLayoutAdapt
            if (r1 == 0) goto L_0x01a7
            goto L_0x01ad
        L_0x01a7:
            int r1 = r8.mInnerTop
            int r0 = r0 + r1
            r8.mInnerBottom = r0
            goto L_0x01af
        L_0x01ad:
            r8.mInnerBottom = r0
        L_0x01af:
            int r0 = r8.mInnerRight
            int r1 = r8.mInnerLeft
            int r0 = r0 - r1
            r8.mInnerWidth = r0
            r8.initJsonTouchRect()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.nativeObj.NativeView.init():void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v4, resolved type: org.json.JSONArray} */
    /* JADX WARNING: type inference failed for: r1v0 */
    /* JADX WARNING: type inference failed for: r1v1, types: [org.json.JSONObject] */
    /* JADX WARNING: type inference failed for: r1v3 */
    /* JADX WARNING: type inference failed for: r1v5 */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void initJsonTouchRect() {
        /*
            r4 = this;
            java.util.ArrayList<android.graphics.RectF> r0 = r4.mTouchRects
            r0.clear()
            java.lang.String r0 = r4.mTouchRectJson
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            r1 = 0
            if (r0 != 0) goto L_0x0039
            java.lang.String r0 = r4.mTouchRectJson
            java.lang.String r2 = "["
            boolean r0 = r0.startsWith(r2)
            if (r0 == 0) goto L_0x0039
            org.json.JSONArray r0 = new org.json.JSONArray     // Catch:{ Exception -> 0x0020 }
            java.lang.String r2 = r4.mTouchRectJson     // Catch:{ Exception -> 0x0020 }
            r0.<init>(r2)     // Catch:{ Exception -> 0x0020 }
            r1 = r0
        L_0x0020:
            r0 = 0
        L_0x0021:
            int r2 = r1.length()
            if (r0 >= r2) goto L_0x004a
            org.json.JSONObject r2 = r1.optJSONObject(r0)     // Catch:{ Exception -> 0x0036 }
            if (r2 == 0) goto L_0x0036
            java.util.ArrayList<android.graphics.RectF> r3 = r4.mTouchRects     // Catch:{ Exception -> 0x0036 }
            android.graphics.RectF r2 = r4.createTouchRect(r2)     // Catch:{ Exception -> 0x0036 }
            r3.add(r2)     // Catch:{ Exception -> 0x0036 }
        L_0x0036:
            int r0 = r0 + 1
            goto L_0x0021
        L_0x0039:
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Exception -> 0x0041 }
            java.lang.String r2 = r4.mTouchRectJson     // Catch:{ Exception -> 0x0041 }
            r0.<init>(r2)     // Catch:{ Exception -> 0x0041 }
            r1 = r0
        L_0x0041:
            java.util.ArrayList<android.graphics.RectF> r0 = r4.mTouchRects
            android.graphics.RectF r1 = r4.createTouchRect(r1)
            r0.add(r1)
        L_0x004a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.nativeObj.NativeView.initJsonTouchRect():void");
    }

    /* access modifiers changed from: package-private */
    public void initStatusBarView(int i) {
        if (-1 == indexOfChild(this.mCanvasView)) {
            attachCanvasView();
        }
        if (this.isStatusBar) {
            int i2 = 0;
            while (i2 < getChildCount()) {
                View childAt = getChildAt(i2);
                if (childAt.getTag() == null || !childAt.getTag().equals("StatusBar")) {
                    i2++;
                } else {
                    childAt.setBackgroundColor(this.mStatusColor);
                    return;
                }
            }
            View view = new View(getContext());
            this.mStatusbarView = view;
            view.setTag("StatusBar");
            this.mStatusbarView.setBackgroundColor(this.mStatusColor);
            this.mStatusbarView.setVisibility(0);
            this.mStatusbarView.setLayoutParams(new FrameLayout.LayoutParams(-1, i));
            addView(this.mStatusbarView);
            this.mStatusbarView.bringToFront();
            measureFitViewParent(false);
        }
    }

    /* access modifiers changed from: package-private */
    public void interceptTouchEvent(boolean z) {
        this.mIntercept = z;
    }

    public boolean isAnimate() {
        return this.mRegionRect != null;
    }

    public boolean isDock() {
        return TextUtils.equals(this.mStyle.optString("position", AbsoluteConst.JSON_VALUE_POSITION_ABSOLUTE), "dock");
    }

    public boolean isDockTop() {
        return TextUtils.equals(this.mStyle.optString("position", AbsoluteConst.JSON_VALUE_POSITION_ABSOLUTE), "dock") && TextUtils.equals(this.mStyle.optString("dock"), "top");
    }

    public boolean isStatusBar() {
        return this.isStatusBar;
    }

    /* access modifiers changed from: package-private */
    public Overlay makeOverlay(IWebview iWebview, NativeBitmap nativeBitmap, String str, int i, JSONObject jSONObject, JSONObject jSONObject2, JSONObject jSONObject3, String str2, String str3, boolean z) {
        return makeOverlay(iWebview, nativeBitmap, str, i, jSONObject, jSONObject2, jSONObject3, str2, str3, true, z);
    }

    /* access modifiers changed from: package-private */
    public Rect makeRect(NativeView nativeView, JSONObject jSONObject, Overlay overlay) {
        Rect rect = new Rect();
        Rect rect2 = overlay != null ? overlay.mSrcRect : null;
        if (jSONObject != null) {
            if (jSONObject.has("right") && (!jSONObject.has("left") || !jSONObject.has("width"))) {
                rect.right = nativeView.mInnerWidth - PdrUtil.convertToScreenInt(jSONObject.optString("right"), nativeView.mInnerWidth, 0, nativeView.mCreateScale);
                if (jSONObject.has("width")) {
                    rect.left = rect.right - getRectWidthForBitmap(nativeView, jSONObject, rect2);
                } else if (!jSONObject.has("left") || "auto".equals(jSONObject.optString("left"))) {
                    rect.left = nativeView.getDrawLeft(0);
                } else {
                    rect.left = nativeView.getDrawLeft(PdrUtil.convertToScreenInt(jSONObject.optString("left"), nativeView.mInnerWidth, 0, nativeView.mCreateScale));
                }
            } else if ("auto".equals(jSONObject.optString("left"))) {
                rect.left = Integer.MIN_VALUE;
                rect.right = getRectWidthForBitmap(nativeView, jSONObject, rect2);
            } else {
                rect.left = nativeView.getDrawLeft(PdrUtil.convertToScreenInt(jSONObject.optString("left"), nativeView.mInnerWidth, 0, nativeView.mCreateScale));
                rect.right = getRectWidthForBitmap(nativeView, jSONObject, rect2) + rect.left;
            }
            if (jSONObject.has("bottom") && (!jSONObject.has("top") || !jSONObject.has("height"))) {
                rect.bottom = nativeView.mInnerBottom - PdrUtil.convertToScreenInt(jSONObject.optString("bottom"), nativeView.mInnerWidth, 0, nativeView.mCreateScale);
                if (jSONObject.has("height")) {
                    rect.top = rect.bottom - getRectHeightForBitmap(nativeView, jSONObject, rect2, overlay, rect.width());
                } else if (!jSONObject.has("top") || "auto".equals(jSONObject.optString("top"))) {
                    rect.top = nativeView.getDrawTop(0);
                } else {
                    rect.top = nativeView.getDrawTop(PdrUtil.convertToScreenInt(jSONObject.optString("top"), nativeView.mInnerHeight, 0, nativeView.mCreateScale));
                }
            } else if ("auto".equals(jSONObject.optString("top"))) {
                rect.top = Integer.MIN_VALUE;
                rect.bottom = getRectHeightForBitmap(nativeView, jSONObject, rect2, overlay, rect.width());
            } else {
                rect.top = nativeView.getDrawTop(PdrUtil.convertToScreenInt(jSONObject.optString("top"), nativeView.mInnerHeight, 0, nativeView.mCreateScale));
                rect.bottom = getRectHeightForBitmap(nativeView, jSONObject, rect2, overlay, rect.width()) + rect.top;
            }
        } else {
            rect.left = nativeView.mInnerLeft;
            rect.top = nativeView.mInnerTop;
            rect.right = nativeView.mInnerRight;
            rect.bottom = nativeView.mInnerBottom;
        }
        return rect;
    }

    public void makeRichText(IWebview iWebview, String str, JSONObject jSONObject, JSONObject jSONObject2, String str2) {
        if (PdrUtil.isEmpty(str2)) {
            str2 = "richtext_" + System.currentTimeMillis();
        }
        INativeViewChildView iNativeViewChildView = this.mChildViewMaps.get(str2);
        if (iNativeViewChildView == null) {
            RichTextLayout.RichTextLayoutHolder makeRichText = RichTextLayout.makeRichText(getContext(), iWebview, this, str, jSONObject, jSONObject2, str2);
            addView(makeRichText);
            this.mChildViewMaps.put(str2, makeRichText);
            return;
        }
        RichTextLayout.makeRichText((RichTextLayout.RichTextLayoutHolder) iNativeViewChildView, str, jSONObject, jSONObject2);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v0, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void makeWeexView(io.dcloud.common.DHInterface.IWebview r9, org.json.JSONObject r10, java.lang.String r11) {
        /*
            r8 = this;
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r11)
            if (r0 == 0) goto L_0x001c
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r0 = "weexview_"
            r11.append(r0)
            long r0 = java.lang.System.currentTimeMillis()
            r11.append(r0)
            java.lang.String r11 = r11.toString()
        L_0x001c:
            java.util.HashMap<java.lang.String, io.dcloud.feature.nativeObj.INativeViewChildView> r0 = r8.mChildViewMaps
            java.lang.Object r0 = r0.get(r11)
            io.dcloud.feature.nativeObj.INativeViewChildView r0 = (io.dcloud.feature.nativeObj.INativeViewChildView) r0
            if (r0 != 0) goto L_0x0065
            io.dcloud.common.DHInterface.IFrameView r0 = r9.obtainFrameView()
            io.dcloud.common.DHInterface.AbsMgr r0 = r0.obtainWindowMgr()
            io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r2 = 4
            java.lang.Object[] r3 = new java.lang.Object[r2]
            io.dcloud.common.DHInterface.IApp r4 = r9.obtainApp()
            r5 = 0
            r3[r5] = r4
            java.lang.String r4 = "weex,io.dcloud.feature.weex.WeexFeature"
            r6 = 1
            r3[r6] = r4
            java.lang.String r4 = "createWeexWindow"
            r7 = 2
            r3[r7] = r4
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r2[r5] = r9
            r2[r6] = r8
            r2[r7] = r10
            r9 = 3
            r2[r9] = r11
            r3[r9] = r2
            r9 = 10
            java.lang.Object r9 = r0.processEvent(r1, r9, r3)
            io.dcloud.feature.nativeObj.INativeViewChildView r9 = (io.dcloud.feature.nativeObj.INativeViewChildView) r9
            r10 = r9
            android.view.View r10 = (android.view.View) r10
            r8.addView(r10)
            java.util.HashMap<java.lang.String, io.dcloud.feature.nativeObj.INativeViewChildView> r10 = r8.mChildViewMaps
            r10.put(r11, r9)
        L_0x0065:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.nativeObj.NativeView.makeWeexView(io.dcloud.common.DHInterface.IWebview, org.json.JSONObject, java.lang.String):void");
    }

    /* access modifiers changed from: protected */
    public void measureChildViewToTop(int i) {
        measureGifImageview(i);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:115:0x0218  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void measureFitViewParent(boolean r20) {
        /*
            r19 = this;
            r7 = r19
            io.dcloud.common.DHInterface.IFrameView r0 = r7.mFrameViewParent
            if (r0 != 0) goto L_0x0007
            return
        L_0x0007:
            org.json.JSONObject r0 = r7.mStyle
            java.lang.String r1 = "position"
            java.lang.String r2 = "absolute"
            java.lang.String r8 = r0.optString(r1, r2)
            java.lang.String r0 = "dock"
            boolean r1 = android.text.TextUtils.equals(r8, r0)
            java.lang.String r10 = "static"
            r3 = 0
            if (r1 == 0) goto L_0x0241
            io.dcloud.common.DHInterface.IFrameView r1 = r7.mFrameViewParent
            io.dcloud.common.adapter.ui.AdaFrameItem r1 = (io.dcloud.common.adapter.ui.AdaFrameItem) r1
            android.view.View r1 = r1.obtainMainView()
            android.view.ViewGroup r1 = (android.view.ViewGroup) r1
            io.dcloud.common.DHInterface.IFrameView r2 = r7.mFrameViewParent
            io.dcloud.common.adapter.ui.AdaWebViewParent r2 = r2.obtainWebviewParent()
            android.view.View r2 = r2.obtainMainView()
            android.view.ViewGroup r2 = (android.view.ViewGroup) r2
            android.view.ViewGroup$LayoutParams r4 = r2.getLayoutParams()
            io.dcloud.common.DHInterface.IFrameView r5 = r7.mFrameViewParent
            io.dcloud.common.adapter.ui.AdaFrameItem r5 = (io.dcloud.common.adapter.ui.AdaFrameItem) r5
            io.dcloud.common.adapter.util.ViewOptions r5 = r5.obtainFrameOptions()
            if (r20 == 0) goto L_0x0043
            int r6 = r5.left
            goto L_0x0047
        L_0x0043:
            int r6 = r2.getLeft()
        L_0x0047:
            java.lang.Object r11 = r19.getTag()
            if (r11 == 0) goto L_0x005e
            java.lang.Object r11 = r19.getTag()
            java.lang.String r11 = r11.toString()
            java.lang.String r12 = "titleNView"
            boolean r11 = r12.equals(r11)
            if (r11 == 0) goto L_0x005e
            r6 = 0
        L_0x005e:
            if (r20 == 0) goto L_0x0063
            int r11 = r5.top
            goto L_0x0068
        L_0x0063:
            float r11 = io.dcloud.nineoldandroids.view.ViewHelper.getY(r2)
            int r11 = (int) r11
        L_0x0068:
            int r12 = r5.width
            io.dcloud.common.DHInterface.IApp r13 = r7.mApp
            r14 = 1
            int r13 = r13.getInt(r14)
            int r15 = r7.mAppScreenHeight
            if (r13 >= r15) goto L_0x0076
            goto L_0x007c
        L_0x0076:
            io.dcloud.common.DHInterface.IApp r13 = r7.mApp
            int r15 = r13.getInt(r14)
        L_0x007c:
            int r13 = r5.bottom
            int r15 = r15 - r13
            org.json.JSONObject r13 = r7.mStyle
            java.lang.String r14 = "top"
            java.lang.String r0 = r13.optString(r0, r14)
            io.dcloud.common.DHInterface.IFrameView r13 = r7.mFrameViewParent
            android.view.View r13 = r13.obtainMainView()
            android.view.ViewGroup r13 = (android.view.ViewGroup) r13
            java.lang.String r9 = "left"
            boolean r9 = android.text.TextUtils.equals(r0, r9)
            if (r9 == 0) goto L_0x00a4
            int r0 = r7.mInnerWidth
            r7.mInnerRight = r0
            r7.mInnerLeft = r3
            int r12 = r12 - r0
            r17 = r0
        L_0x00a0:
            r6 = r11
        L_0x00a1:
            r3 = r12
            goto L_0x0216
        L_0x00a4:
            boolean r9 = android.text.TextUtils.equals(r0, r14)
            if (r9 == 0) goto L_0x0166
            int r0 = r7.mInnerHeight
            r7.mInnerBottom = r0
            r7.mInnerTop = r3
            int r0 = r13.indexOfChild(r7)
            r9 = 0
            r11 = 0
            r14 = 0
            r16 = 0
        L_0x00b9:
            int r3 = r13.getChildCount()
            if (r9 >= r3) goto L_0x012a
            android.view.View r3 = r13.getChildAt(r9)
            r17 = r6
            boolean r6 = r3 instanceof com.dcloud.android.widget.StatusBarView
            if (r6 == 0) goto L_0x00ca
            goto L_0x0102
        L_0x00ca:
            if (r3 == 0) goto L_0x0105
            boolean r6 = r3 instanceof io.dcloud.feature.nativeObj.NativeView
            if (r6 == 0) goto L_0x0105
            if (r3 == r7) goto L_0x0105
            boolean r6 = r3 instanceof io.dcloud.feature.nativeObj.TitleNView
            io.dcloud.feature.nativeObj.NativeView r3 = (io.dcloud.feature.nativeObj.NativeView) r3
            boolean r18 = r3.isDock()
            if (r18 == 0) goto L_0x0102
            if (r6 == 0) goto L_0x00e4
            int r18 = r3.getInnerHeight()
            int r16 = r16 + r18
        L_0x00e4:
            boolean r18 = r3.isDockTop()
            if (r18 == 0) goto L_0x00fd
            int r18 = r3.getInnerBottom()
            int r14 = r14 + r18
            if (r6 != 0) goto L_0x00fd
            if (r9 < r0) goto L_0x00f7
            r6 = -1
            if (r0 != r6) goto L_0x00fd
        L_0x00f7:
            int r6 = r3.getInnerHeight()
            int r16 = r16 + r6
        L_0x00fd:
            int r3 = r3.getInnerHeight()
            int r11 = r11 + r3
        L_0x0102:
            r18 = r0
            goto L_0x0123
        L_0x0105:
            boolean r6 = r3 instanceof com.dcloud.android.widget.AbsoluteLayout
            if (r6 == 0) goto L_0x0102
            r6 = r3
            com.dcloud.android.widget.AbsoluteLayout r6 = (com.dcloud.android.widget.AbsoluteLayout) r6
            io.dcloud.common.adapter.ui.AdaFrameView r18 = r6.getFrameView()
            if (r18 == 0) goto L_0x0102
            io.dcloud.common.adapter.ui.AdaFrameView r6 = r6.getFrameView()
            int r6 = r6.mPosition
            r18 = r0
            byte r0 = io.dcloud.common.adapter.util.ViewRect.DOCK_BOTTOM
            if (r6 != r0) goto L_0x0123
            int r0 = r3.getHeight()
            int r11 = r11 + r0
        L_0x0123:
            int r9 = r9 + 1
            r6 = r17
            r0 = r18
            goto L_0x00b9
        L_0x012a:
            r17 = r6
            int r0 = r7.mInnerHeight
            int r14 = r14 + r0
            int r11 = r11 + r0
            boolean r0 = r5.isStatusbar
            if (r0 != 0) goto L_0x014a
            boolean r0 = r7.isImmersed
            if (r0 == 0) goto L_0x0141
            org.json.JSONObject r0 = r5.titleNView
            boolean r0 = io.dcloud.common.util.TitleNViewUtil.isTitleTypeForDef(r0)
            if (r0 == 0) goto L_0x0141
            goto L_0x014a
        L_0x0141:
            boolean r0 = r7.isStatusBar
            if (r0 == 0) goto L_0x0148
            int r0 = io.dcloud.common.adapter.util.DeviceInfo.sStatusBarHeight
            int r14 = r14 + r0
        L_0x0148:
            r0 = r14
            goto L_0x0153
        L_0x014a:
            int r0 = io.dcloud.common.adapter.util.DeviceInfo.sStatusBarHeight
            int r14 = r14 + r0
            int r3 = r11 + r0
            int r16 = r16 + r0
            r0 = r14
            r14 = r3
        L_0x0153:
            r3 = r16
            io.dcloud.feature.nativeObj.NativeView$NativeCanvasView r6 = r7.mCanvasView
            android.view.ViewGroup$LayoutParams r6 = r6.getLayoutParams()
            android.widget.FrameLayout$LayoutParams r6 = (android.widget.FrameLayout.LayoutParams) r6
            r6.topMargin = r3
            r7.measureChildViewToTop(r3)
            int r15 = r15 - r14
            r6 = r0
            goto L_0x00a1
        L_0x0166:
            r17 = r6
            java.lang.String r3 = "right"
            boolean r0 = android.text.TextUtils.equals(r0, r3)
            if (r0 == 0) goto L_0x017e
            int r0 = r1.getRight()
            r7.mInnerRight = r0
            int r3 = r7.mInnerWidth
            int r0 = r0 - r3
            r7.mInnerLeft = r0
            int r12 = r12 - r3
            goto L_0x00a0
        L_0x017e:
            int r0 = r7.mAppScreenHeight
            r7.mInnerBottom = r0
            if (r0 != 0) goto L_0x018b
            int r0 = r5.top
            int r3 = r5.height
            int r0 = r0 + r3
            r7.mInnerBottom = r0
        L_0x018b:
            r0 = 0
            r3 = 0
            r6 = 0
        L_0x018e:
            int r9 = r13.getChildCount()
            if (r0 >= r9) goto L_0x01da
            android.view.View r9 = r13.getChildAt(r0)
            if (r9 == 0) goto L_0x01d7
            boolean r11 = r9 instanceof io.dcloud.feature.nativeObj.NativeView
            if (r11 == 0) goto L_0x01d7
            if (r9 == r7) goto L_0x01d7
            java.lang.Object r11 = r9.getTag()
            if (r11 == 0) goto L_0x01b3
            java.lang.Object r11 = r9.getTag()
            java.lang.String r14 = "StatusBar"
            boolean r11 = r11.equals(r14)
            if (r11 == 0) goto L_0x01b3
            goto L_0x01d7
        L_0x01b3:
            r11 = r9
            io.dcloud.feature.nativeObj.NativeView r11 = (io.dcloud.feature.nativeObj.NativeView) r11
            boolean r14 = r11.isDock()
            if (r14 == 0) goto L_0x01d7
            boolean r9 = r9 instanceof io.dcloud.feature.nativeObj.TitleNView
            if (r9 == 0) goto L_0x01cc
            org.json.JSONObject r9 = r11.mStyle
            boolean r9 = io.dcloud.common.util.TitleNViewUtil.isTitleTypeForDef(r9)
            if (r9 == 0) goto L_0x01cc
            int r3 = r11.getInnerHeight()
        L_0x01cc:
            boolean r9 = r11.isDockTop()
            if (r9 == 0) goto L_0x01d7
            int r9 = r11.getInnerBottom()
            int r6 = r6 + r9
        L_0x01d7:
            int r0 = r0 + 1
            goto L_0x018e
        L_0x01da:
            int r0 = r7.mInnerBottom
            int r9 = r7.mInnerHeight
            int r0 = r0 - r9
            r7.mInnerTop = r0
            io.dcloud.feature.nativeObj.NativeView$NativeCanvasView r0 = r7.mCanvasView
            android.view.ViewGroup$LayoutParams r0 = r0.getLayoutParams()
            android.widget.FrameLayout$LayoutParams r0 = (android.widget.FrameLayout.LayoutParams) r0
            boolean r9 = r7.isImmersed
            if (r9 == 0) goto L_0x01fc
            boolean r9 = r5.isStatusbar
            if (r9 != 0) goto L_0x01f9
            org.json.JSONObject r9 = r5.titleNView
            boolean r9 = io.dcloud.common.util.TitleNViewUtil.isTitleTypeForDef(r9)
            if (r9 == 0) goto L_0x01fc
        L_0x01f9:
            int r9 = io.dcloud.common.adapter.util.DeviceInfo.sStatusBarHeight
            int r3 = r3 + r9
        L_0x01fc:
            r0.topMargin = r3
            r7.measureChildViewToTop(r3)
            boolean r0 = r5.isStatusbar
            if (r0 == 0) goto L_0x0209
            int r0 = io.dcloud.common.adapter.util.DeviceInfo.sStatusBarHeight
        L_0x0207:
            int r6 = r6 + r0
            goto L_0x0210
        L_0x0209:
            boolean r0 = r7.isStatusBar
            if (r0 == 0) goto L_0x0210
            int r0 = io.dcloud.common.adapter.util.DeviceInfo.sStatusBarHeight
            goto L_0x0207
        L_0x0210:
            int r0 = r7.mInnerHeight
            int r0 = r0 + r6
            int r15 = r15 - r0
            goto L_0x00a1
        L_0x0216:
            if (r20 == 0) goto L_0x0235
            r1.addView(r7)
            org.json.JSONObject r0 = r5.titleNView
            if (r0 == 0) goto L_0x0235
            r0 = 0
        L_0x0220:
            int r5 = r1.getChildCount()
            if (r0 >= r5) goto L_0x0235
            android.view.View r5 = r1.getChildAt(r0)
            boolean r9 = r5 instanceof io.dcloud.feature.nativeObj.TitleNView
            if (r9 == 0) goto L_0x0232
            r5.bringToFront()
            goto L_0x0235
        L_0x0232:
            int r0 = r0 + 1
            goto L_0x0220
        L_0x0235:
            r0 = r19
            r1 = r2
            r2 = r4
            r4 = r15
            r5 = r17
            r0.viewPostResize(r1, r2, r3, r4, r5, r6)
            goto L_0x02ef
        L_0x0241:
            boolean r0 = android.text.TextUtils.equals(r8, r10)
            if (r0 == 0) goto L_0x0258
            if (r20 == 0) goto L_0x02ef
            io.dcloud.common.DHInterface.IFrameView r0 = r7.mFrameViewParent
            io.dcloud.common.DHInterface.IWebview r0 = r0.obtainWebView()
            android.view.ViewGroup r0 = r0.obtainWindowView()
            r0.addView(r7)
            goto L_0x02ef
        L_0x0258:
            boolean r0 = android.text.TextUtils.equals(r8, r2)
            if (r0 == 0) goto L_0x02ef
            io.dcloud.common.DHInterface.IFrameView r0 = r7.mFrameViewParent
            io.dcloud.common.adapter.ui.AdaFrameItem r0 = (io.dcloud.common.adapter.ui.AdaFrameItem) r0
            io.dcloud.common.adapter.util.ViewOptions r0 = r0.obtainFrameOptions()
            io.dcloud.common.DHInterface.IFrameView r1 = r7.mFrameViewParent
            io.dcloud.common.adapter.ui.AdaFrameItem r1 = (io.dcloud.common.adapter.ui.AdaFrameItem) r1
            android.view.View r1 = r1.obtainMainView()
            android.view.ViewGroup r1 = (android.view.ViewGroup) r1
            r2 = 0
            r3 = 0
        L_0x0272:
            int r4 = r1.getChildCount()
            if (r2 >= r4) goto L_0x0292
            android.view.View r4 = r1.getChildAt(r2)
            boolean r5 = r4 instanceof io.dcloud.feature.nativeObj.NativeView
            if (r5 == 0) goto L_0x028f
            io.dcloud.feature.nativeObj.NativeView r4 = (io.dcloud.feature.nativeObj.NativeView) r4
            boolean r5 = r4.isDockTop()
            if (r5 == 0) goto L_0x028f
            if (r4 == r7) goto L_0x028f
            int r4 = r4.getInnerHeight()
            int r3 = r3 + r4
        L_0x028f:
            int r2 = r2 + 1
            goto L_0x0272
        L_0x0292:
            boolean r2 = r7.isImmersed
            if (r2 == 0) goto L_0x02a5
            boolean r2 = r0.isStatusbar
            if (r2 != 0) goto L_0x02a2
            org.json.JSONObject r2 = r0.titleNView
            boolean r2 = io.dcloud.common.util.TitleNViewUtil.isTitleTypeForDef(r2)
            if (r2 == 0) goto L_0x02a5
        L_0x02a2:
            int r2 = io.dcloud.common.adapter.util.DeviceInfo.sStatusBarHeight
            int r3 = r3 + r2
        L_0x02a5:
            boolean r2 = r7.isImmersed
            if (r2 == 0) goto L_0x02b4
            boolean r0 = r0.isStatusbar
            if (r0 != 0) goto L_0x02b4
            boolean r0 = r7 instanceof io.dcloud.feature.nativeObj.TitleNView
            if (r0 == 0) goto L_0x02b4
            int r0 = io.dcloud.common.adapter.util.DeviceInfo.sStatusBarHeight
            int r3 = r3 + r0
        L_0x02b4:
            r0 = 0
        L_0x02b5:
            int r2 = r19.getChildCount()
            if (r0 >= r2) goto L_0x02ea
            android.view.View r2 = r7.getChildAt(r0)
            io.dcloud.feature.nativeObj.NativeView$NativeCanvasView r4 = r7.mCanvasView
            if (r2 != r4) goto L_0x02cf
            android.view.ViewGroup$LayoutParams r2 = r4.getLayoutParams()
            android.widget.FrameLayout$LayoutParams r2 = (android.widget.FrameLayout.LayoutParams) r2
            r2.topMargin = r3
            r7.measureChildViewToTop(r3)
            goto L_0x02e7
        L_0x02cf:
            boolean r4 = r2 instanceof io.dcloud.feature.nativeObj.INativeViewChildView
            if (r4 == 0) goto L_0x02e7
            io.dcloud.feature.nativeObj.INativeViewChildView r2 = (io.dcloud.feature.nativeObj.INativeViewChildView) r2
            android.view.View r2 = r2.obtainMainView()
            if (r2 == 0) goto L_0x02e7
            android.view.ViewGroup$LayoutParams r2 = r2.getLayoutParams()
            boolean r4 = r2 instanceof android.view.ViewGroup.MarginLayoutParams
            if (r4 == 0) goto L_0x02e7
            android.view.ViewGroup$MarginLayoutParams r2 = (android.view.ViewGroup.MarginLayoutParams) r2
            r2.topMargin = r3
        L_0x02e7:
            int r0 = r0 + 1
            goto L_0x02b5
        L_0x02ea:
            if (r20 == 0) goto L_0x02ef
            r1.addView(r7)
        L_0x02ef:
            int r0 = r7.mInnerHeight
            int r1 = r7.mAppScreenHeight
            if (r0 <= r1) goto L_0x02fe
            android.view.ViewGroup$LayoutParams r0 = r19.getLayoutParams()
            int r1 = r7.mInnerHeight
            r0.height = r1
            goto L_0x0314
        L_0x02fe:
            boolean r0 = android.text.TextUtils.equals(r8, r10)
            if (r0 == 0) goto L_0x030d
            android.view.ViewGroup$LayoutParams r0 = r19.getLayoutParams()
            int r1 = r7.mAppScreenHeight
            r0.height = r1
            goto L_0x0314
        L_0x030d:
            android.view.ViewGroup$LayoutParams r0 = r19.getLayoutParams()
            r1 = -1
            r0.height = r1
        L_0x0314:
            r19.initJsonTouchRect()
            java.util.HashMap<java.lang.String, io.dcloud.feature.nativeObj.INativeViewChildView> r0 = r7.mChildViewMaps
            java.util.Collection r0 = r0.values()
            if (r0 == 0) goto L_0x0333
            java.util.Iterator r0 = r0.iterator()
        L_0x0323:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0333
            java.lang.Object r1 = r0.next()
            io.dcloud.feature.nativeObj.INativeViewChildView r1 = (io.dcloud.feature.nativeObj.INativeViewChildView) r1
            r1.updateLayout()
            goto L_0x0323
        L_0x0333:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.nativeObj.NativeView.measureFitViewParent(boolean):void");
    }

    public void nativeInvalidate(boolean z) {
        if (this.mShow) {
            if (z) {
                requestLayout();
            }
            postInvalidate();
            NativeCanvasView nativeCanvasView = this.mCanvasView;
            if (nativeCanvasView != null) {
                if (z) {
                    nativeCanvasView.requestLayout();
                }
                this.mCanvasView.invalidate();
            }
        }
    }

    public View obtanMainView() {
        return this;
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.isAnimate) {
            this.isAnimate = false;
        }
        postDelayed(new Runnable() {
            public void run() {
                NativeView nativeView = NativeView.this;
                nativeView.mAppScreenWidth = nativeView.mApp.getInt(0);
                NativeView nativeView2 = NativeView.this;
                nativeView2.mAppScreenHeight = nativeView2.mApp.getInt(1);
                IWebview iWebview = NativeView.this.mWebView;
                if (!(iWebview == null || iWebview.obtainFrameView() == null)) {
                    NativeView.this.init();
                    NativeView.this.UpdateRegionData();
                    NativeView.this.configurationCange();
                    NativeView.this.requestLayout();
                    NativeView.this.invalidate();
                }
                NativeCanvasView nativeCanvasView = NativeView.this.mCanvasView;
                if (nativeCanvasView != null) {
                    nativeCanvasView.requestLayout();
                    NativeView.this.mCanvasView.invalidate();
                }
            }
        }, 100);
    }

    /* access modifiers changed from: package-private */
    public void removeFromViewGroup() {
        ViewGroup viewGroup;
        if (!this.mAttached) {
            if (getParent() != null) {
                ((ViewGroup) getParent()).removeView(this);
            }
            clearViewData();
            return;
        }
        IFrameView iFrameView = this.mFrameViewParent;
        if (iFrameView != null) {
            iFrameView.removeFrameViewListener(this.mIEventCallback);
            String optString = this.mStyle.optString("position", AbsoluteConst.JSON_VALUE_POSITION_ABSOLUTE);
            if (TextUtils.equals(optString, "dock")) {
                viewGroup = (ViewGroup) ((AdaFrameItem) this.mFrameViewParent).obtainMainView();
            } else if (TextUtils.equals(optString, "static")) {
                viewGroup = this.mFrameViewParent.obtainWebView().obtainWindowView();
            } else {
                viewGroup = TextUtils.equals(optString, AbsoluteConst.JSON_VALUE_POSITION_ABSOLUTE) ? (ViewGroup) ((AdaFrameItem) this.mFrameViewParent).obtainMainView() : null;
            }
            if (viewGroup != null) {
                viewGroup.removeView(this);
            } else {
                ViewParent parent = getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(this);
                }
            }
            if (this instanceof TitleNView) {
                ((AdaFrameView) this.mFrameViewParent).obtainFrameOptions().titleNView = null;
            }
            ((AdaFrameView) this.mFrameViewParent).changeWebParentViewRect();
            this.mAttached = false;
            this.mFrameViewParent = null;
            clearViewData();
        }
    }

    public void resetNativeView() {
        clearViewData();
        clearAnimate();
    }

    public void setInputFocusById(String str, boolean z) {
        EditText inputById = getInputById(str);
        if (inputById == null) {
            return;
        }
        if (z) {
            inputById.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) inputById.getContext().getSystemService("input_method");
            if (inputMethodManager != null) {
                try {
                    inputMethodManager.showSoftInput(inputById, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            inputById.clearFocus();
            InputMethodManager inputMethodManager2 = (InputMethodManager) inputById.getContext().getSystemService("input_method");
            if (inputMethodManager2 != null) {
                try {
                    inputMethodManager2.hideSoftInputFromWindow(inputById.getApplicationWindowToken(), 0);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public void setNativeShowType(boolean z) {
        this.mShow = z;
    }

    public void setStyle(JSONObject jSONObject, boolean z) {
        if (jSONObject != null) {
            try {
                JSONObject jSONObject2 = this.mStyle;
                if (jSONObject2 == null) {
                    this.mStyle = jSONObject;
                } else {
                    this.mStyle = JSONUtil.combinJSONObject(jSONObject2, jSONObject);
                }
                if (z) {
                    init();
                    requestLayout();
                    invalidate();
                    NativeCanvasView nativeCanvasView = this.mCanvasView;
                    if (nativeCanvasView != null) {
                        nativeCanvasView.requestLayout();
                        this.mCanvasView.invalidate();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setStyleBackgroundColor(String str) {
        int i;
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject = this.mStyle;
                if (jSONObject != null) {
                    if (jSONObject.has("backgroudColor")) {
                        this.mStyle.put("backgroudColor", str);
                    } else if (this.mStyle.has("backgroundColor")) {
                        this.mStyle.put("backgroundColor", str);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                i = Color.parseColor(str);
            } catch (Exception unused) {
                i = PdrUtil.stringToColor(str);
            }
            setStyleBackgroundColor(i);
        }
    }

    public void setStyleLeft(int i) {
        try {
            JSONObject jSONObject = this.mStyle;
            jSONObject.put("left", (Float.valueOf(String.valueOf(i)).floatValue() / this.mCreateScale) + "px");
            requestLayout();
            invalidate();
            NativeCanvasView nativeCanvasView = this.mCanvasView;
            if (nativeCanvasView != null) {
                nativeCanvasView.requestLayout();
                this.mCanvasView.invalidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: package-private */
    public void setTouchEventRect(String str) {
        this.mTouchRectJson = str;
        initJsonTouchRect();
    }

    public void setWebAnimationRuning(boolean z) {
        this.isWebAnimationRuning = z;
    }

    public JSONObject toJSON() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("id", this.mID);
            jSONObject.put("uuid", this.mUUID);
            jSONObject.put("type", getViewType());
            JSONObject jSONObject2 = this.mStyle;
            if (jSONObject2 != null) {
                jSONObject.put("styles", jSONObject2.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    public Overlay makeOverlay(IWebview iWebview, NativeBitmap nativeBitmap, String str, int i, JSONObject jSONObject, JSONObject jSONObject2, JSONObject jSONObject3, String str2, String str3, boolean z, boolean z2) {
        Overlay overlay = new Overlay();
        this.mWebView = iWebview;
        overlay.mNativeView = this;
        overlay.mSrcJson = jSONObject;
        overlay.mDestJson = jSONObject2;
        overlay.mStyleJson = jSONObject3;
        overlay.mNativeBitmap = nativeBitmap;
        overlay.mText = str;
        overlay.mRectColor = i;
        overlay.type = str3;
        if (PdrUtil.isEmpty(str2)) {
            this.mOverlays.add(overlay);
        } else if (this.mOverlayMaps.containsKey(str2)) {
            this.mOverlays.set(this.mOverlayMaps.get(str2).intValue(), overlay);
        } else {
            this.mOverlays.add(overlay);
            this.mOverlayMaps.put(str2, Integer.valueOf(this.mOverlays.indexOf(overlay)));
        }
        overlay.parseJson(this.mWebView);
        if (str3.equals(WXBasicComponentType.IMG) && nativeBitmap != null && nativeBitmap.isGif()) {
            addGifImagview(overlay);
        }
        if (str3.equals("input")) {
            overlay.webview = iWebview;
            addInput(overlay, str2);
        }
        if (z) {
            nativeInvalidate(z2);
        }
        return overlay;
    }

    public void setStyleBackgroundColor(int i) {
        this.mBackGroundColor = i;
        NativeCanvasView nativeCanvasView = this.mCanvasView;
        if (nativeCanvasView != null) {
            nativeCanvasView.invalidate();
        }
    }
}
