package io.dcloud.common.core.ui;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import io.dcloud.common.DHInterface.IActivityDelegate;
import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.core.ui.keyboard.DCEditText;
import io.dcloud.common.util.PdrUtil;
import io.src.dcloud.adapter.DCloudAdapterUtil;
import java.lang.ref.SoftReference;
import org.json.JSONObject;

public class DCKeyboardManager {
    public static String SOFT_INPUT_MODE_ADJUST_NOTHING = "nothing";
    public static String SOFT_INPUT_MODE_ADJUST_PAN = "adjustPan";
    public static String SOFT_INPUT_MODE_ADJUST_RESIZE = "adjustResize";
    public static DCKeyboardManager instance;
    String EVENTS_DOCUMENT_KEYBOARD = "javascript:(function(){if(!((window.__html5plus__&&__html5plus__.isReady)?__html5plus__:(navigator.plus&&navigator.plus.isReady)?navigator.plus:window.plus)){window.__load__plus__&&window.__load__plus__();}var e = document.createEvent('HTMLEvents');var evt = '%s';e.initEvent(evt, false, true); e.height = %d;/*console.log('dispatch ' + evt + ' event');*/document.dispatchEvent(e);})();";
    private final String TAG = "DCKeyboardManager";
    private boolean isAdjust = true;
    boolean isAdministration = false;
    /* access modifiers changed from: private */
    public boolean isAllScreen = false;
    private boolean isNativeFocus = false;
    boolean isNativeUpDate = false;
    /* access modifiers changed from: private */
    public Runnable keyBoardHideRunnable;
    /* access modifiers changed from: private */
    public Runnable keyBoardShowRunnable;
    private String mActivitySoftInputMode = "";
    /* access modifiers changed from: private */
    public View mContentView;
    /* access modifiers changed from: private */
    public a mDHAppRoot;
    /* access modifiers changed from: private */
    public float mFocusTop = 0.0f;
    private String mFrontInputType = "text";
    /* access modifiers changed from: private */
    public Handler mHandler;
    private float mHtmlInputFT = -1.0f;
    String mInputMode;
    private IFrameView mInputRootFrame = null;
    private ViewTreeObserver.OnGlobalLayoutListener mLayoutChangeListener;
    float mNaiveCursorSpacing = 0.0f;
    private View mNativeView = null;
    int mOrientation = -100;
    /* access modifiers changed from: private */
    public View mRootContentView;
    private SoftReference<Activity> mSoftAc = null;
    int rootViewVisibleHeight;

    class a implements ViewTreeObserver.OnGlobalLayoutListener {
        final /* synthetic */ Activity a;

        /* renamed from: io.dcloud.common.core.ui.DCKeyboardManager$a$a  reason: collision with other inner class name */
        class C0015a implements Runnable {
            final /* synthetic */ int a;

            C0015a(int i) {
                this.a = i;
            }

            public void run() {
                DCKeyboardManager.this.onKeyboardChanged(this.a, true);
                AndroidResources.sIMEAlive = true;
                DeviceInfo.isIMEShow = true;
                DeviceInfo.sInputMethodHeight = Math.abs(this.a);
            }
        }

        class b implements Runnable {
            b() {
            }

            public void run() {
                DCKeyboardManager.this.onKeyboardChanged(-1, AndroidResources.sIMEAlive);
                if (DCKeyboardManager.this.mDHAppRoot != null) {
                    DCKeyboardManager.this.mDHAppRoot.h();
                }
                AndroidResources.sIMEAlive = false;
                DeviceInfo.isIMEShow = false;
            }
        }

        a(Activity activity) {
            this.a = activity;
        }

        public void onGlobalLayout() {
            int i;
            if (DCKeyboardManager.this.mContentView != null) {
                int height = DCKeyboardManager.this.mRootContentView.getHeight();
                int height2 = DCKeyboardManager.this.mContentView.getHeight();
                DCKeyboardManager dCKeyboardManager = DCKeyboardManager.this;
                if (dCKeyboardManager.rootViewVisibleHeight == 0) {
                    dCKeyboardManager.rootViewVisibleHeight = height;
                    return;
                }
                boolean isFullScreen = PdrUtil.isFullScreen(this.a);
                Rect rect = new Rect();
                DCKeyboardManager.this.mContentView.getWindowVisibleDisplayFrame(rect);
                int height3 = rect.height();
                if (!isFullScreen && (i = DeviceInfo.sStatusBarHeight + height3) <= height2) {
                    height3 = i;
                }
                int i2 = DCKeyboardManager.this.isAllScreen ? height / 6 : height / 5;
                DCKeyboardManager dCKeyboardManager2 = DCKeyboardManager.this;
                if (dCKeyboardManager2.rootViewVisibleHeight != height3) {
                    int i3 = height2 - height3;
                    if (i3 > i2) {
                        try {
                            if (dCKeyboardManager2.keyBoardShowRunnable != null) {
                                DCKeyboardManager.this.mHandler.removeCallbacks(DCKeyboardManager.this.keyBoardShowRunnable);
                            }
                            Runnable unused = DCKeyboardManager.this.keyBoardShowRunnable = new C0015a(i3);
                            DCKeyboardManager.this.mHandler.post(DCKeyboardManager.this.keyBoardShowRunnable);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    } else {
                        if (dCKeyboardManager2.keyBoardHideRunnable != null) {
                            DCKeyboardManager.this.mHandler.removeCallbacks(DCKeyboardManager.this.keyBoardHideRunnable);
                        }
                        Runnable unused2 = DCKeyboardManager.this.keyBoardHideRunnable = new b();
                        DCKeyboardManager.this.mHandler.post(DCKeyboardManager.this.keyBoardHideRunnable);
                    }
                    DCKeyboardManager.this.rootViewVisibleHeight = height3;
                }
            }
        }
    }

    class b implements Runnable {
        b() {
        }

        public void run() {
            DCKeyboardManager.this.onKeyboardChanged(DCKeyboardManager.this.getKeyBoardHeight(), true);
        }
    }

    class c implements Runnable {
        c() {
        }

        public void run() {
            DCKeyboardManager.this.onKeyboardChanged(DCKeyboardManager.this.getKeyBoardHeight(), true);
        }
    }

    class d implements Runnable {
        final /* synthetic */ String a;
        final /* synthetic */ AdaFrameView b;
        final /* synthetic */ View c;

        d(String str, AdaFrameView adaFrameView, View view) {
            this.a = str;
            this.b = adaFrameView;
            this.c = view;
        }

        public void run() {
            if (this.a.equals(DCKeyboardManager.SOFT_INPUT_MODE_ADJUST_PAN)) {
                int i = 0;
                AdaFrameView adaFrameView = this.b;
                if (adaFrameView != null) {
                    i = adaFrameView.obtainFrameOptions().top;
                }
                View view = this.c;
                if (view != null) {
                    view.setTranslationY((float) i);
                }
            }
            ViewGroup.LayoutParams layoutParams = DCKeyboardManager.this.mContentView.getLayoutParams();
            layoutParams.height = -1;
            layoutParams.width = -1;
            DCKeyboardManager.this.mContentView.setLayoutParams(layoutParams);
        }
    }

    class e implements Runnable {
        final /* synthetic */ int a;
        final /* synthetic */ View b;

        e(int i, View view) {
            this.a = i;
            this.b = view;
        }

        public void run() {
            this.b.setTranslationY(((float) this.a) - DCKeyboardManager.this.mFocusTop);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v1, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void fireKeyboardEvent(io.dcloud.common.DHInterface.IFrameView r8, int r9) {
        /*
            r7 = this;
            io.dcloud.common.DHInterface.IWebview r0 = r8.obtainWebView()
            boolean r0 = r0.isUniWebView()
            r1 = 1
            r2 = 0
            r3 = 2
            if (r0 != 0) goto L_0x0031
            float r0 = (float) r9
            io.dcloud.common.DHInterface.IWebview r4 = r8.obtainWebView()
            float r4 = r4.getScale()
            float r0 = r0 / r4
            int r0 = (int) r0
            java.lang.String r4 = r7.EVENTS_DOCUMENT_KEYBOARD
            java.lang.Object[] r5 = new java.lang.Object[r3]
            java.lang.String r6 = "keyboardchange"
            r5[r2] = r6
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            r5[r1] = r0
            java.lang.String r0 = io.dcloud.common.util.StringUtil.format(r4, r5)
            io.dcloud.common.DHInterface.IWebview r4 = r8.obtainWebView()
            r4.loadUrl(r0)
        L_0x0031:
            io.dcloud.common.DHInterface.IApp r0 = r8.obtainApp()
            boolean r0 = io.dcloud.common.util.BaseInfo.isUniAppAppid(r0)
            if (r0 == 0) goto L_0x0067
            java.lang.Object[] r0 = new java.lang.Object[r3]
            java.lang.String r4 = "__uniapp__service"
            r0[r2] = r4
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
            r0[r1] = r9
            io.dcloud.common.DHInterface.AbsMgr r9 = r8.obtainWindowMgr()
            io.dcloud.common.DHInterface.IMgr$MgrType r4 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r5 = 4
            java.lang.Object[] r5 = new java.lang.Object[r5]
            io.dcloud.common.DHInterface.IApp r8 = r8.obtainApp()
            r5[r2] = r8
            java.lang.String r8 = "weex,io.dcloud.feature.weex.WeexFeature"
            r5[r1] = r8
            java.lang.String r8 = "onKeyboardHeightChange"
            r5[r3] = r8
            r8 = 3
            r5[r8] = r0
            r8 = 10
            r9.processEvent(r4, r8, r5)
        L_0x0067:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.DCKeyboardManager.fireKeyboardEvent(io.dcloud.common.DHInterface.IFrameView, int):void");
    }

    private String getActivityInput(Activity activity) {
        int i = activity.getWindow().getAttributes().softInputMode;
        if (i == 16) {
            return SOFT_INPUT_MODE_ADJUST_RESIZE;
        }
        if (i == 32) {
            return SOFT_INPUT_MODE_ADJUST_PAN;
        }
        if (i == 48) {
            return SOFT_INPUT_MODE_ADJUST_NOTHING;
        }
        String metaValue = AndroidResources.getMetaValue("DCLOUD_INPUT_MODE");
        if (!TextUtils.isEmpty(metaValue)) {
            return metaValue;
        }
        return SOFT_INPUT_MODE_ADJUST_RESIZE;
    }

    public static DCKeyboardManager getInstance() {
        if (instance == null) {
            instance = new DCKeyboardManager();
        }
        return instance;
    }

    /* access modifiers changed from: private */
    public int getKeyBoardHeight() {
        SoftReference<Activity> softReference;
        int i;
        if (this.mContentView == null || (softReference = this.mSoftAc) == null || softReference.get() == null) {
            return 0;
        }
        boolean isFullScreen = PdrUtil.isFullScreen(this.mSoftAc.get());
        Rect rect = new Rect();
        this.mContentView.getWindowVisibleDisplayFrame(rect);
        int height = rect.height();
        int height2 = this.mContentView.getHeight();
        if (!isFullScreen && (i = DeviceInfo.sStatusBarHeight + height) <= height2) {
            height = i;
        }
        return height2 - height;
    }

    private static View getScrollView(View view) {
        if (view instanceof ViewGroup) {
            try {
                if (view.canScrollVertically(1)) {
                    return view;
                }
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    View scrollView = getScrollView(((ViewGroup) view).getChildAt(i));
                    if (scrollView != null) {
                        return scrollView;
                    }
                }
            } catch (Exception unused) {
            }
        }
        return null;
    }

    private void keyboardHide(AdaFrameView adaFrameView, View view, String str) {
        if (this.isAdjust) {
            this.mFocusTop = 0.0f;
            this.mContentView.post(new d(str, adaFrameView, view));
        }
    }

    private void keyboardShow(View view, int i, String str, boolean z) {
        if (!str.equals(SOFT_INPUT_MODE_ADJUST_NOTHING) && this.isAdjust) {
            int height = this.mContentView.getHeight();
            if (str.equals(SOFT_INPUT_MODE_ADJUST_RESIZE)) {
                ViewGroup.LayoutParams layoutParams = this.mContentView.getLayoutParams();
                layoutParams.height = height - i;
                this.mContentView.setLayoutParams(layoutParams);
                return;
            }
            int i2 = height - i;
            if (!z) {
                this.mFocusTop = this.mHtmlInputFT;
            }
            float f = 0.0f;
            if (DeviceInfo.isIMEShow) {
                f = 0.0f - view.getTranslationY();
            }
            if (((float) i2) < this.mFocusTop - f) {
                this.mContentView.post(new e(i2, view));
            }
        }
    }

    public void dhAppRootIsReady(a aVar) {
        if (this.isAdministration) {
            this.mDHAppRoot = aVar;
            this.mContentView.getViewTreeObserver().addOnGlobalLayoutListener(this.mLayoutChangeListener);
        }
    }

    public String getFrameSoftInputMode() {
        a aVar = this.mDHAppRoot;
        if (aVar != null) {
            return aVar.i().obtainFrameOptions().softinputMode;
        }
        return null;
    }

    public String getFrontInputType() {
        return this.mFrontInputType;
    }

    public View getNativeInput() {
        return this.mNativeView;
    }

    public boolean isTakeOver() {
        String frameSoftInputMode = getInstance().getFrameSoftInputMode();
        if (frameSoftInputMode != null) {
            return SOFT_INPUT_MODE_ADJUST_PAN.equals(frameSoftInputMode) || SOFT_INPUT_MODE_ADJUST_NOTHING.equals(frameSoftInputMode);
        }
        return false;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void nativeEditTextFocus(java.lang.String r4, android.view.View r5, boolean r6, java.lang.String r7, float r8) {
        /*
            r3 = this;
            r3.isNativeFocus = r6
            r0 = 1
            r3.isNativeUpDate = r0
            r1 = 0
            r3.mInputRootFrame = r1
            r3.mInputMode = r7
            if (r6 == 0) goto L_0x007a
            r3.mNativeView = r5
            r3.mNaiveCursorSpacing = r8
            io.dcloud.common.core.ui.a r5 = r3.mDHAppRoot
            if (r5 == 0) goto L_0x0058
            boolean r5 = android.text.TextUtils.isEmpty(r4)
            if (r5 != 0) goto L_0x0058
            io.dcloud.common.core.ui.a r5 = r3.mDHAppRoot
            io.dcloud.common.core.ui.b r5 = r5.i()
            if (r5 != 0) goto L_0x0023
            return
        L_0x0023:
            java.lang.Object[] r6 = new java.lang.Object[r0]
            r8 = 0
            r6[r8] = r4
            io.dcloud.common.DHInterface.AbsMgr r4 = r5.obtainWindowMgr()
            io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r2 = 4
            java.lang.Object[] r2 = new java.lang.Object[r2]
            io.dcloud.common.DHInterface.IApp r5 = r5.obtainApp()
            r2[r8] = r5
            java.lang.String r5 = "weex,io.dcloud.feature.weex.WeexFeature"
            r2[r0] = r5
            r5 = 2
            java.lang.String r8 = "findWebviewByInstanceId"
            r2[r5] = r8
            r5 = 3
            r2[r5] = r6
            r5 = 10
            java.lang.Object r4 = r4.processEvent(r1, r5, r2)
            if (r4 == 0) goto L_0x0058
            boolean r5 = r4 instanceof io.dcloud.common.DHInterface.IWebview
            if (r5 == 0) goto L_0x0058
            io.dcloud.common.DHInterface.IWebview r4 = (io.dcloud.common.DHInterface.IWebview) r4
            io.dcloud.common.DHInterface.IFrameView r4 = r4.obtainFrameView()
            r3.mInputRootFrame = r4
        L_0x0058:
            java.lang.String r4 = SOFT_INPUT_MODE_ADJUST_NOTHING
            boolean r4 = r7.equals(r4)
            if (r4 == 0) goto L_0x0061
            return
        L_0x0061:
            boolean r4 = io.dcloud.common.adapter.util.DeviceInfo.isIMEShow
            if (r4 == 0) goto L_0x007a
            java.lang.Runnable r4 = r3.keyBoardShowRunnable
            if (r4 == 0) goto L_0x006e
            android.os.Handler r5 = r3.mHandler
            r5.removeCallbacks(r4)
        L_0x006e:
            io.dcloud.common.core.ui.DCKeyboardManager$c r4 = new io.dcloud.common.core.ui.DCKeyboardManager$c
            r4.<init>()
            r3.keyBoardShowRunnable = r4
            android.os.Handler r5 = r3.mHandler
            r5.post(r4)
        L_0x007a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.DCKeyboardManager.nativeEditTextFocus(java.lang.String, android.view.View, boolean, java.lang.String, float):void");
    }

    public void onCreate(Activity activity) {
        this.mActivitySoftInputMode = getActivityInput(activity);
        this.isAdministration = true;
        this.mSoftAc = new SoftReference<>(activity);
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mOrientation = activity.getResources().getConfiguration().orientation;
        this.mRootContentView = activity.getWindow().getDecorView();
        IActivityHandler iActivityHandler = DCloudAdapterUtil.getIActivityHandler(activity);
        if (iActivityHandler != null) {
            this.mContentView = iActivityHandler.obtainActivityContentView();
        } else if (activity instanceof IActivityDelegate) {
            this.mContentView = ((IActivityDelegate) activity).obtainActivityContentView();
        }
        this.isAllScreen = PdrUtil.isAllScreenDevice(activity);
        DeviceInfo.updateStatusBarHeight(activity);
        this.mLayoutChangeListener = new a(activity);
    }

    public void onKeyboardChanged(int i, boolean z) {
        b i2;
        View view;
        View view2;
        if (this.isAdjust && this.mDHAppRoot != null && this.mActivitySoftInputMode.equals(SOFT_INPUT_MODE_ADJUST_RESIZE) && (i2 = this.mDHAppRoot.i()) != null) {
            if (TextUtils.isEmpty(this.mInputMode)) {
                this.mInputMode = getFrameSoftInputMode();
            }
            if (z) {
                IFrameView iFrameView = this.mInputRootFrame;
                if (iFrameView != null && iFrameView.obtainWebView() != null) {
                    fireKeyboardEvent(this.mInputRootFrame, i);
                } else if (i2.obtainWebView() != null) {
                    fireKeyboardEvent(i2, i);
                }
            }
            if (i <= 1 || !this.mInputMode.equals(SOFT_INPUT_MODE_ADJUST_NOTHING)) {
                int height = this.mContentView.getHeight();
                IFrameView iFrameView2 = this.mInputRootFrame;
                if (iFrameView2 == null) {
                    view = i2.getChilds().size() > 1 ? i2.obtainMainView() : i2.obtainWebView().obtainWindowView();
                } else if (iFrameView2.obtainWebView() == null) {
                    this.mInputRootFrame = null;
                    return;
                } else {
                    view = this.mInputRootFrame.obtainWebView().obtainWindowView();
                }
                if (i <= 0) {
                    try {
                        View view3 = this.mNativeView;
                        if ((view3 instanceof DCEditText) && ((DCEditText) view3).getKeyboardHeightChangeListener() != null) {
                            ((DCEditText) this.mNativeView).getKeyboardHeightChangeListener().onChange(false, i);
                        }
                        if (this.mInputRootFrame == null) {
                            if (i2.getChilds().size() > 1) {
                                keyboardHide(i2, view, this.mInputMode);
                                this.isNativeUpDate = true;
                            }
                        }
                        i2 = null;
                        keyboardHide(i2, view, this.mInputMode);
                        this.isNativeUpDate = true;
                    } catch (Exception unused) {
                        this.mInputMode = null;
                        this.mNativeView = null;
                    }
                } else if (!this.isNativeFocus || (view2 = this.mNativeView) == null) {
                    this.mNativeView = null;
                    this.isNativeUpDate = true;
                    if (this.mInputMode.equals(SOFT_INPUT_MODE_ADJUST_PAN)) {
                        float f = this.mHtmlInputFT;
                        float f2 = (float) height;
                        if (f > f2) {
                            int i3 = (int) (f - f2);
                            if (view instanceof WebView) {
                                view.scrollBy(0, i3);
                            }
                            this.mHtmlInputFT = f2;
                        }
                    }
                    keyboardShow(view, i, this.mInputMode, false);
                } else {
                    if (this.isNativeUpDate) {
                        int[] iArr = new int[2];
                        view2.getLocationOnScreen(iArr);
                        float height2 = ((float) (iArr[1] + this.mNativeView.getHeight())) + this.mNaiveCursorSpacing;
                        this.mFocusTop = height2;
                        float f3 = (float) height;
                        if (height2 > f3) {
                            int i4 = (int) (height2 - f3);
                            View scrollView = getScrollView(view);
                            if (scrollView != null) {
                                scrollView.scrollBy(0, i4);
                            }
                            this.mFocusTop = f3;
                        }
                        this.isNativeUpDate = false;
                    }
                    View view4 = this.mNativeView;
                    if ((view4 instanceof DCEditText) && ((DCEditText) view4).getKeyboardHeightChangeListener() != null) {
                        ((DCEditText) this.mNativeView).getKeyboardHeightChangeListener().onChange(true, i);
                    }
                    keyboardShow(view, i, this.mInputMode, true);
                }
            } else {
                View view5 = this.mNativeView;
                if (view5 != null && (view5 instanceof DCEditText) && ((DCEditText) view5).getKeyboardHeightChangeListener() != null) {
                    ((DCEditText) this.mNativeView).getKeyboardHeightChangeListener().onChange(true, i);
                }
            }
        }
    }

    public void onStop() {
        if (this.isAdministration) {
            if (Build.VERSION.SDK_INT >= 16) {
                this.mRootContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this.mLayoutChangeListener);
            } else {
                this.mRootContentView.getViewTreeObserver().removeGlobalOnLayoutListener(this.mLayoutChangeListener);
            }
        }
    }

    public void setAdjust(boolean z) {
        this.isAdjust = z;
    }

    public void setContentView(IActivityHandler iActivityHandler) {
        if (iActivityHandler != null) {
            this.mContentView = iActivityHandler.obtainActivityContentView();
        }
    }

    public void setFrontInputType(String str) {
        this.mFrontInputType = str;
    }

    public void setHTMLInputRect(IWebview iWebview, String str) {
        try {
            if (this.isAdjust) {
                IFrameView obtainFrameView = iWebview.obtainFrameView();
                this.mInputRootFrame = obtainFrameView;
                if (obtainFrameView != null) {
                    int[] iArr = new int[2];
                    obtainFrameView.obtainWebView().obtainWindowView().getLocationOnScreen(iArr);
                    JSONObject jSONObject = new JSONObject(str);
                    JSONObject jSONObject2 = jSONObject.getJSONObject("position");
                    String optString = jSONObject.optString("mode");
                    this.mInputMode = optString;
                    if (!optString.equals(SOFT_INPUT_MODE_ADJUST_NOTHING)) {
                        float parseFloat = PdrUtil.parseFloat(jSONObject2.optString("top"), 0.0f, 0.0f, iWebview.getScale()) + PdrUtil.parseFloat(jSONObject2.optString("height"), 0.0f, 0.0f, iWebview.getScale()) + ((float) iArr[1]);
                        this.mHtmlInputFT = parseFloat;
                        this.mFocusTop = parseFloat;
                        if (DeviceInfo.isIMEShow) {
                            Runnable runnable = this.keyBoardShowRunnable;
                            if (runnable != null) {
                                this.mHandler.removeCallbacks(runnable);
                            }
                            b bVar = new b();
                            this.keyBoardShowRunnable = bVar;
                            this.mHandler.post(bVar);
                        }
                    }
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            this.mInputMode = null;
            this.mFocusTop = 0.0f;
            this.mHtmlInputFT = 0.0f;
        }
    }

    public void setNativeInput(View view, float f) {
        this.mNativeView = view;
        this.mNaiveCursorSpacing = f;
        this.isNativeUpDate = true;
    }
}
