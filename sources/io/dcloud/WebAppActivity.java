package io.dcloud;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidtranscoder.format.MediaFormatExtraConstants;
import com.bumptech.glide.Glide;
import io.dcloud.application.DCLoudApplicationImpl;
import io.dcloud.application.DCloudApplication;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.DHInterface.SplashView;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.io.PushReceiver;
import io.dcloud.common.adapter.ui.FrameSwitchView;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import io.dcloud.common.adapter.util.PermissionUtil;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.constant.StringConst;
import io.dcloud.common.core.ui.DCKeyboardManager;
import io.dcloud.common.core.ui.TabBarWebview;
import io.dcloud.common.core.ui.TabBarWebviewMgr;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.ImageLoaderUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.ShortCutUtil;
import io.dcloud.common.util.TestUtil;
import io.dcloud.common.util.language.LanguageUtil;
import io.dcloud.feature.internal.sdk.SDK;
import io.dcloud.feature.internal.splash.ISplash;
import io.dcloud.feature.internal.splash.SplashViewDBackground;
import io.src.dcloud.adapter.DCloudAdapterUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

public class WebAppActivity extends b {
    public static final long ONE_SECOND = 1000;
    public static final long SPLASH_SECOND = 5000;
    boolean A = false;
    boolean B = true;
    FrameLayout C = null;
    LinearLayout D = null;
    FrameLayout E = null;
    ICallBack F = null;
    protected boolean mSplashShowing;
    protected View mSplashView = null;
    private AlertDialog n;
    private boolean o;
    private Handler p = new Handler();
    private final String q = "remove-app_action";
    BroadcastReceiver r;
    private f s;
    private HomeKeyEventBroadcastReceiver t;
    private Context u;
    Bitmap v = null;
    long w = 0;
    /* access modifiers changed from: private */
    public ArrayList<ICallBack> x;
    boolean y = false;
    View z = null;

    public static class HomeKeyEventBroadcastReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String stringExtra;
            if (intent.getAction().equals("android.intent.action.CLOSE_SYSTEM_DIALOGS") && (stringExtra = intent.getStringExtra("reason")) != null) {
                if (stringExtra.equals("homekey") || stringExtra.equals("recentapps")) {
                    DCLoudApplicationImpl.self().onApp2Back();
                }
            }
        }
    }

    class a extends BroadcastReceiver {
        a() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            action.hashCode();
            action.hashCode();
            char c = 65535;
            switch (action.hashCode()) {
                case -1538406691:
                    if (action.equals("android.intent.action.BATTERY_CHANGED")) {
                        c = 0;
                        break;
                    }
                    break;
                case -19011148:
                    if (action.equals("android.intent.action.LOCALE_CHANGED")) {
                        c = 1;
                        break;
                    }
                    break;
                case 821022474:
                    if (action.equals("com.huawei.intent.action.CLICK_STATUSBAR")) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    int intExtra = intent.getIntExtra(MediaFormatExtraConstants.KEY_LEVEL, 0);
                    double intExtra2 = (double) intent.getIntExtra("temperature", 0);
                    Double.isNaN(intExtra2);
                    AppRuntime.setBatteryLevel(intExtra);
                    AppRuntime.setTemperature((int) (intExtra2 / 10.0d));
                    return;
                case 1:
                    LanguageUtil.updateDeviceDefLocalLanguage(Locale.getDefault());
                    return;
                case 2:
                    WebAppActivity webAppActivity = WebAppActivity.this;
                    if (webAppActivity.C != null && webAppActivity.x != null) {
                        Iterator it = WebAppActivity.this.x.iterator();
                        while (it.hasNext()) {
                            ((ICallBack) it.next()).onCallBack(1, action);
                        }
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    class b implements ICallBack {
        b() {
        }

        public Object onCallBack(int i, Object obj) {
            if (i != 1) {
                return null;
            }
            WebAppActivity webAppActivity = WebAppActivity.this;
            if (webAppActivity.F != null) {
                webAppActivity.closeAppStreamSplash((String) obj);
                WebAppActivity.this.A = false;
                return null;
            }
            webAppActivity.A = true;
            return null;
        }
    }

    class c implements DialogInterface.OnClickListener {
        final /* synthetic */ String a;

        c(String str) {
            this.a = str;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            WebAppActivity.this.b(this.a, 1);
        }
    }

    class d implements DialogInterface.OnClickListener {
        final /* synthetic */ String a;

        d(String str) {
            this.a = str;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            WebAppActivity webAppActivity = WebAppActivity.this;
            webAppActivity.updateParam("closewebapp", webAppActivity.that);
            WebAppActivity.this.b(this.a, 0);
        }
    }

    class e implements DialogInterface.OnKeyListener {
        final /* synthetic */ String a;

        e(String str) {
            this.a = str;
        }

        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
            if (keyEvent.getAction() != 1 || i != 4) {
                return false;
            }
            WebAppActivity webAppActivity = WebAppActivity.this;
            webAppActivity.updateParam("closewebapp", webAppActivity.that);
            WebAppActivity.this.b(this.a, 0);
            return true;
        }
    }

    private class f implements DCLoudApplicationImpl.ActivityStatusListener {
        private SoftReference<Activity> a;

        public f(Activity activity) {
            this.a = new SoftReference<>(activity);
        }

        public void onBack() {
            if (!AppRuntime.hasPrivacyForNotShown(this.a.get())) {
                Activity activity = this.a.get();
                if (!activity.isDestroyed() && !activity.isFinishing()) {
                    try {
                        Class<?> cls = Class.forName("io.dcloud.feature.gg.dcloud.dcmgr.SplashInterstitialAdManager");
                        Object invoke = cls.getMethod("getInstance", new Class[0]).invoke((Object) null, new Object[0]);
                        cls.getMethod("load", new Class[]{Activity.class}).invoke(invoke, new Object[]{activity});
                    } catch (Exception unused) {
                    }
                }
            }
        }

        public void onFront() {
            if (!AppRuntime.hasPrivacyForNotShown(this.a.get())) {
                Activity activity = this.a.get();
                if (!activity.isDestroyed() && !activity.isFinishing()) {
                    try {
                        Class<?> cls = Class.forName("io.dcloud.feature.gg.dcloud.dcmgr.SplashInterstitialAdManager");
                        Object invoke = cls.getMethod("getInstance", new Class[0]).invoke((Object) null, new Object[0]);
                        cls.getMethod(AbsoluteConst.EVENTS_WEBVIEW_SHOW, new Class[]{Activity.class}).invoke(invoke, new Object[]{activity});
                    } catch (Exception unused) {
                    }
                }
            }
        }
    }

    class g extends RelativeLayout {
        int a;
        float b;
        int c = 0;
        Paint d = new Paint();
        int e = 0;
        int f = 0;
        int g = 255;

        class a implements Runnable {
            a() {
            }

            public void run() {
                g gVar = g.this;
                int i = gVar.g - 5;
                gVar.g = i;
                if (i > 0) {
                    gVar.postDelayed(this, 5);
                } else {
                    ViewGroup viewGroup = (ViewGroup) gVar.getParent();
                    if (viewGroup != null) {
                        viewGroup.removeView(g.this);
                    }
                }
                g.this.invalidate();
            }
        }

        class b implements Runnable {
            b() {
            }

            public void run() {
                g gVar = g.this;
                int i = gVar.f;
                int i2 = gVar.e;
                int i3 = 10;
                int i4 = (i - i2) / 10;
                if (i4 <= 10) {
                    i3 = i4 < 1 ? 1 : i4;
                }
                int i5 = i2 + i3;
                gVar.e = i5;
                if (i > i5) {
                    gVar.postDelayed(this, 5);
                } else if (i >= gVar.a) {
                    gVar.a();
                }
                g.this.invalidate();
            }
        }

        g(Context context) {
            super(context);
            this.a = context.getResources().getDisplayMetrics().widthPixels;
            int i = context.getResources().getDisplayMetrics().heightPixels;
            if (i == 1280) {
                this.b = 6.0f;
            } else if (i != 1920) {
                this.b = ((float) context.getResources().getDisplayMetrics().heightPixels) * 0.0045f;
            } else {
                this.b = 9.0f;
            }
        }

        /* access modifiers changed from: package-private */
        public void a() {
            postDelayed(new a(), 50);
        }

        /* access modifiers changed from: package-private */
        public void b() {
            a(100);
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            this.d.setColor(Color.argb(this.g, 26, 173, 25));
            float f2 = (float) this.c;
            canvas.drawRect(0.0f, f2, (float) this.e, f2 + this.b, this.d);
        }

        /* access modifiers changed from: protected */
        public void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            setMeasuredDimension(this.a, this.c + ((int) this.b));
        }

        /* access modifiers changed from: package-private */
        public void a(int i) {
            int i2 = (this.a * i) / 100;
            if (this.e >= this.f) {
                postDelayed(new b(), 5);
            }
            this.f = i2;
        }
    }

    private String b(String str) {
        if (DCloudAdapterUtil.isPlugin()) {
            return null;
        }
        try {
            Bundle bundle = this.that.getPackageManager().getApplicationInfo(getPackageName(), 128) != null ? this.that.getPackageManager().getApplicationInfo(getPackageName(), 128).metaData : null;
            if (bundle == null || PdrUtil.isEmpty(bundle.get(str))) {
                return null;
            }
            return String.valueOf(bundle.get(str));
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static void deviceInjectionGeoLocationJs(Context context) {
        BaseInfo.injectionGeolocationJS = !MobilePhoneModel.checkDeviceHtml5Geo();
    }

    private void f() {
        Intent intent = getIntent();
        boolean booleanExtra = intent != null ? intent.getBooleanExtra(IntentConst.PL_AUTO_HIDE, false) : false;
        Log.d("WebAppActivity", "checkAutoHide " + booleanExtra);
        if (booleanExtra) {
            Intent intent2 = new Intent();
            String stringExtra = intent.getStringExtra(IntentConst.PL_AUTO_HIDE_SHOW_PN);
            String stringExtra2 = intent.getStringExtra(IntentConst.PL_AUTO_HIDE_SHOW_ACTIVITY);
            intent2.putExtra(IntentConst.PL_AUTO_HIDE_SHOW_PN, true);
            intent2.setClassName(stringExtra, stringExtra2);
            this.that.startActivity(intent2);
            this.that.overridePendingTransition(0, 0);
            Log.d("WebAppActivity", "checkAutoHide return mini package " + stringExtra2);
        }
    }

    private String g() {
        return this.o ? getIntent().getStringExtra("appid") : BaseInfo.sDefaultBootApp;
    }

    private void h() {
        Intent intent = getIntent();
        if (intent.hasExtra("dcloud.push.broswer")) {
            PushReceiver.onReceive(this, intent);
        }
        boolean booleanExtra = intent.getBooleanExtra(IntentConst.IS_STREAM_APP, false);
        this.o = booleanExtra;
        if (!booleanExtra) {
            intent.removeExtra("appid");
        }
    }

    private void i() {
        IntentFilter intentFilter = new IntentFilter("com.huawei.intent.action.CLICK_STATUSBAR");
        intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        if (this.r == null) {
            this.r = new a();
        }
        registerReceiver(this.r, intentFilter, "huawei.permission.CLICK_STATUSBAR_BROADCAST", (Handler) null);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void j() {
        View view = this.z;
        if (view != null && this.mSplashView != null) {
            if (view.getHeight() == 0 || this.z.getWidth() == 0) {
                this.z.measure(View.MeasureSpec.makeMeasureSpec(this.mSplashView.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(this.mSplashView.getHeight(), 1073741824));
                this.z.layout(0, 0, this.mSplashView.getWidth(), this.mSplashView.getHeight());
            }
        }
    }

    public void addClickStatusbarCallBack(ICallBack iCallBack) {
        if (this.x == null) {
            this.x = new ArrayList<>();
        }
        if (!this.x.contains(iCallBack)) {
            this.x.add(iCallBack);
        }
    }

    /* access modifiers changed from: protected */
    public void addViewToContentView(View view) {
        int indexOfChild = this.C.indexOfChild(this.mSplashView);
        int childCount = this.C.getChildCount();
        if (childCount > 0) {
            for (int i = childCount - 1; i >= 0; i--) {
                View childAt = this.C.getChildAt(i);
                if (childAt != view) {
                    if ("AppRootView".equals(childAt.getTag())) {
                        this.C.addView(view, i);
                        this.C.removeView(childAt);
                        return;
                    } else if (i == 0) {
                        if (childAt == this.mSplashView) {
                            this.C.addView(view, 0);
                        } else if (indexOfChild > 0) {
                            this.C.addView(view, indexOfChild - 1);
                        } else {
                            this.C.addView(view);
                        }
                    }
                }
            }
            return;
        }
        this.C.addView(view);
    }

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context context) {
        this.u = context;
        if (Build.VERSION.SDK_INT < 26) {
            super.attachBaseContext(context);
        } else {
            super.attachBaseContext(LanguageUtil.updateContextLanguageAfterO(context, false));
        }
    }

    public int backPressed() {
        return 0;
    }

    public /* bridge */ /* synthetic */ void callBack(String str, Bundle bundle) {
        super.callBack(str, bundle);
    }

    public /* bridge */ /* synthetic */ void checkAndRequestPhoneState() {
        super.checkAndRequestPhoneState();
    }

    public /* bridge */ /* synthetic */ void checkAndRequestStoragePermission() {
        super.checkAndRequestStoragePermission();
    }

    public void closeAppStreamSplash(String str) {
        Logger.d("webappActivity closeAppStreamSplash");
        DCloudAdapterUtil.Plugin2Host_closeAppStreamSplash(str);
        Bitmap bitmap = this.v;
        if (bitmap != null && !bitmap.isRecycled()) {
            try {
                this.v.recycle();
                this.v = null;
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if (this.mSplashView != null) {
            Logger.d("webappActivity removeView mSplashView");
            View view = this.mSplashView;
            if (view instanceof g) {
                ((g) view).b();
            } else {
                this.C.removeView(view);
            }
            this.mSplashView = null;
            this.z = null;
        }
        this.y = false;
        this.mSplashShowing = false;
        this.w = 0;
        ICallBack iCallBack = this.F;
        if (iCallBack != null) {
            iCallBack.onCallBack(1, (Object) null);
            this.F = null;
        }
        initBackToFrontSplashAd();
        if (TextUtils.isEmpty(str)) {
            str = g();
        }
        a.a(this, str, "onCloseSplashNoAd", (Object) null);
    }

    public void closeSideBar() {
    }

    public void finish() {
        super.finish();
    }

    public /* bridge */ /* synthetic */ int getActivityState() {
        return super.getActivityState();
    }

    public /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public String getErrorTipMsg() {
        int i = TestUtil.PointTime.mEc;
        if (i == 4) {
            return "" + getString(R.string.dcloud_common_not_sd_card);
        } else if (i == 9) {
            return "" + getString(R.string.dcloud_common_sd_not_space);
        } else {
            int i2 = TestUtil.PointTime.mEt;
            if (i2 == 1) {
                return "" + getString(R.string.dcloud_common_setting_download_failed);
            } else if (i2 != 3 && i2 != 2) {
                return "";
            } else {
                return "" + getString(R.string.dcloud_common_app_res_download_failed);
            }
        }
    }

    public Context getOriginalContext() {
        return this.u;
    }

    public View getProgressView() {
        View view = this.mSplashView;
        if (view == null || !(view instanceof g)) {
            return null;
        }
        return view;
    }

    public /* bridge */ /* synthetic */ Resources getResources() {
        return super.getResources();
    }

    public /* bridge */ /* synthetic */ String getUrlByFilePath(String str, String str2) {
        return super.getUrlByFilePath(str, str2);
    }

    public boolean hasAdService() {
        return super.hasAdService() && this.y;
    }

    public void initBackToFrontSplashAd() {
        if (!SDK.isUniMPSDK() && (getApplication() instanceof DCloudApplication) && this.s == null) {
            HomeKeyEventBroadcastReceiver homeKeyEventBroadcastReceiver = new HomeKeyEventBroadcastReceiver();
            this.t = homeKeyEventBroadcastReceiver;
            registerReceiver(homeKeyEventBroadcastReceiver, new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
            this.s = new f(this);
            ((DCloudApplication) getApplication()).addActivityStatusListener(this.s);
        }
    }

    public /* bridge */ /* synthetic */ boolean isMultiProcessMode() {
        return super.isMultiProcessMode();
    }

    public boolean isSideBarCanRefresh() {
        if (this.C == null) {
            return false;
        }
        for (int i = 0; i < this.C.getChildCount(); i++) {
            View childAt = this.C.getChildAt(i);
            if (childAt != null && (childAt instanceof SplashView)) {
                return false;
            }
        }
        return true;
    }

    public FrameLayout obtainActivityContentView() {
        return this.C;
    }

    public /* bridge */ /* synthetic */ void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    public void onAppActive(IApp iApp) {
    }

    public void onAppActive(String str) {
    }

    public void onAppStart(IApp iApp) {
    }

    public void onAppStart(String str) {
    }

    public void onAppStop(String str) {
    }

    public /* bridge */ /* synthetic */ void onAsyncStartAppEnd(String str, Object obj) {
        super.onAsyncStartAppEnd(str, obj);
    }

    public /* bridge */ /* synthetic */ Object onAsyncStartAppStart(String str) {
        return super.onAsyncStartAppStart(str);
    }

    public void onBackPressed() {
        int backPressed;
        Logger.e("back", "WebAppActivity onBackPressed");
        if (isMultiProcessMode() || !(2 == (backPressed = backPressed()) || 1 == backPressed)) {
            a.a(this, TextUtils.isEmpty(this.e) ? g() : this.e, "onBack", this.z);
            super.onBackPressed();
        }
    }

    public /* bridge */ /* synthetic */ void onCloseSplash() {
        super.onCloseSplash();
    }

    public /* bridge */ /* synthetic */ void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    public void onCreate(Bundle bundle) {
        BaseInfo.startTime = System.currentTimeMillis();
        if (!SDK.isUniMPSDK()) {
            AppRuntime.restartWeex(getApplication(), (ICallBack) null, getIntent().getStringExtra("appid"));
        }
        LanguageUtil.updateDeviceDefLocalLanguage((Context) this);
        Log.e("Html5Plus-onCreate", System.currentTimeMillis() + "");
        ShortCutUtil.activityNameSDK = getIntent().getStringExtra(IntentConst.WEBAPP_ACTIVITY_SHORTCUTACTIVITY);
        ShortCutUtil.mAutoCreateShortcut = getIntent().getBooleanExtra(IntentConst.WEBAPP_ACTIVITY_AUTOCREATESHORTCUT, true);
        Serializable serializableExtra = getIntent().getSerializableExtra(IntentConst.WEBAPP_ACTIVITY_EXTRAPRO);
        if (PdrUtil.isEmpty(serializableExtra)) {
            ShortCutUtil.extraProSDK = (HashMap) serializableExtra;
        }
        if (getIntent().hasExtra(IntentConst.INTENT_ORIENTATION)) {
            setRequestedOrientation(getIntent().getIntExtra(IntentConst.INTENT_ORIENTATION, 2));
        }
        deviceInjectionGeoLocationJs(getApplicationContext());
        Log.d("WebAppActivity", "onCreate");
        h();
        if (!this.o) {
            TestUtil.record(AbsoluteConst.RUN_5AP_TIME_KEY);
        }
        super.onCreate(bundle);
        BaseInfo.isFirstRun = false;
        FrameSwitchView instance = FrameSwitchView.getInstance(this.that);
        if (!instance.isInit()) {
            instance.initView();
        }
        f();
    }

    public void onCreateAdSplash(Context context) {
        if (!this.o && this.mSplashView != null && this.z == null) {
            if (super.hasAdService()) {
                this.z = a.a(this.that, new b(), BaseInfo.sDefaultBootApp);
            }
            View view = this.z;
            this.y = view != null;
            if (view != null) {
                if (view.getParent() != null) {
                    ((ViewGroup) this.z.getParent()).removeView(this.z);
                }
                View view2 = this.mSplashView;
                if (view2 instanceof ViewGroup) {
                    ((ViewGroup) view2).addView(this.z, new ViewGroup.LayoutParams(-1, -1));
                }
                this.mSplashView.post(new Runnable() {
                    public final void run() {
                        WebAppActivity.this.j();
                    }
                });
            }
        }
    }

    public /* bridge */ /* synthetic */ boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public Object onCreateSplash(Context context) {
        Window window = getWindow();
        int i = Build.VERSION.SDK_INT;
        if (i >= 19 && i <= 25) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.flags |= 1024;
            window.setAttributes(attributes);
        } else if (i > 25) {
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | 1280);
            window.setStatusBarColor(0);
            if (i >= 28 && window.getDecorView().getRootWindowInsets() != null) {
                WindowManager.LayoutParams attributes2 = window.getAttributes();
                attributes2.flags |= 1024;
                attributes2.layoutInDisplayCutoutMode = 1;
                window.setAttributes(attributes2);
            }
        }
        if (this.mSplashView != null) {
            return null;
        }
        BaseInfo.splashCreateTime = System.currentTimeMillis();
        a(context);
        return null;
    }

    public void onDestroy() {
        Log.d("WebAppActivity", "onDestroy");
        super.onDestroy();
        try {
            unregisterReceiver(this.r);
            this.x.clear();
            PermissionUtil.clearUseRejectedCache();
            ImageLoaderUtil.clearCache();
        } catch (Exception unused) {
        }
        Handler handler = this.p;
        if (handler != null) {
            handler.removeCallbacksAndMessages((Object) null);
        }
        if (getApplication() instanceof DCloudApplication) {
            ((DCloudApplication) getApplication()).removeActivityStatusListener(this.s);
        }
        HomeKeyEventBroadcastReceiver homeKeyEventBroadcastReceiver = this.t;
        if (homeKeyEventBroadcastReceiver != null) {
            unregisterReceiver(homeKeyEventBroadcastReceiver);
        }
        FrameSwitchView.getInstance(this.that).clearData();
        TabBarWebviewMgr.getInstance().setLancheTabBar((TabBarWebview) null);
        AlertDialog alertDialog = this.n;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.n.dismiss();
        }
        PlatformUtil.invokeMethod("io.dcloud.feature.weex.WeexDevtoolImpl", "unregisterReceiver", (Object) null, new Class[]{Context.class}, new Object[]{getContext()});
        this.n = null;
        this.u = null;
        Glide.get(this).clearMemory();
    }

    public /* bridge */ /* synthetic */ boolean onKeyDown(int i, KeyEvent keyEvent) {
        return super.onKeyDown(i, keyEvent);
    }

    public /* bridge */ /* synthetic */ boolean onKeyEventExecute(ISysEventListener.SysEventType sysEventType, int i, KeyEvent keyEvent) {
        return super.onKeyEventExecute(sysEventType, i, keyEvent);
    }

    public /* bridge */ /* synthetic */ boolean onKeyLongPress(int i, KeyEvent keyEvent) {
        return super.onKeyLongPress(i, keyEvent);
    }

    public /* bridge */ /* synthetic */ boolean onKeyUp(int i, KeyEvent keyEvent) {
        return super.onKeyUp(i, keyEvent);
    }

    public /* bridge */ /* synthetic */ void onLowMemory() {
        super.onLowMemory();
    }

    public void onNewIntentImpl(Intent intent) {
        super.onNewIntentImpl(intent);
        if (intent.hasExtra("dcloud.push.broswer")) {
            PushReceiver.onReceive(this, intent);
        }
    }

    public /* bridge */ /* synthetic */ void onPause() {
        super.onPause();
    }

    public /* bridge */ /* synthetic */ void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
    }

    public void onResume() {
        super.onResume();
        Log.e("Html5Plus-onResume", System.currentTimeMillis() + "");
    }

    /* access modifiers changed from: protected */
    public void onRuntimeCreate(Bundle bundle) {
        i();
        super.onRuntimeCreate(bundle);
    }

    public /* bridge */ /* synthetic */ void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    public void onWindowFocusChanged(boolean z2) {
        super.onWindowFocusChanged(z2);
        PlatformUtil.SCREEN_WIDTH(this.that);
        PlatformUtil.SCREEN_HEIGHT(this.that);
        PlatformUtil.MESURE_SCREEN_STATUSBAR_HEIGHT(this.that);
    }

    public void recordDialog(Dialog dialog) {
    }

    public /* bridge */ /* synthetic */ void registerLocalReceiver(io.dcloud.feature.internal.reflect.BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        super.registerLocalReceiver(broadcastReceiver, intentFilter);
    }

    public /* bridge */ /* synthetic */ Intent registerReceiver(io.dcloud.feature.internal.reflect.BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, String str, Handler handler) {
        return super.registerReceiver(broadcastReceiver, intentFilter, str, handler);
    }

    public void removeClickStatusbarCallBack(ICallBack iCallBack) {
        ArrayList<ICallBack> arrayList = this.x;
        if (arrayList != null && arrayList.contains(iCallBack)) {
            this.x.remove(iCallBack);
        }
    }

    public void removeFromRecord(Dialog dialog) {
    }

    public /* bridge */ /* synthetic */ void sendLocalBroadcast(Intent intent) {
        super.sendLocalBroadcast(intent);
    }

    public void setProgressView() {
        int i = 0;
        while (true) {
            if (i < this.C.getChildCount()) {
                View childAt = this.C.getChildAt(i);
                if (childAt != null && childAt == this.mSplashView) {
                    this.C.removeViewAt(i);
                    break;
                }
                i++;
            } else {
                break;
            }
        }
        g gVar = new g(this.that);
        this.mSplashView = gVar;
        this.C.addView(gVar);
    }

    public /* bridge */ /* synthetic */ void setSecondPrivacyAlert() {
        super.setSecondPrivacyAlert();
    }

    public void setSideBarVisibility(int i) {
    }

    public void setSplashCloseListener(String str, ICallBack iCallBack) {
        this.F = iCallBack;
        if (this.mSplashView == null) {
            iCallBack.onCallBack(1, (Object) null);
            this.F = null;
        }
        if (this.A) {
            closeAppStreamSplash(str);
        }
        if (TextUtils.isEmpty(str)) {
            str = g();
        }
        a.a(this, str, "onWillCloseSplash", this.z);
        this.A = false;
    }

    public void setViewAsContentView(View view, FrameLayout.LayoutParams layoutParams) {
        if (this.C == null) {
            FrameLayout frameLayout = new FrameLayout(this.that);
            this.C = frameLayout;
            if (this.D != null) {
                this.C.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
                this.D.addView(this.C);
                setContentView(this.D);
            } else {
                setContentView(frameLayout);
            }
            DCKeyboardManager.getInstance().setContentView(this);
        }
        PlatformUtil.invokeMethod("io.dcloud.appstream.actionbar.StreamAppActionBarUtil", "checkNeedTitleView", (Object) null, new Class[]{Activity.class, String.class}, new Object[]{this.that, getIntent().getStringExtra("appid")});
        addViewToContentView(view);
        if (layoutParams != null) {
            view.setLayoutParams(layoutParams);
        }
        String str = TestUtil.START_APP_SET_ROOTVIEW;
        TestUtil.print(str, "start" + view);
    }

    public void setWebViewIntoPreloadView(View view) {
        if (this.E == null) {
            FrameLayout frameLayout = new FrameLayout(this.that);
            this.E = frameLayout;
            this.C.addView(frameLayout, 0);
        }
        this.E.addView(view);
    }

    public void showDownloadDialog(String str, String str2) {
        if (this.mSplashShowing) {
            AlertDialog alertDialog = this.n;
            if (alertDialog == null || !alertDialog.isShowing()) {
                if (this.n == null) {
                    this.n = new AlertDialog.Builder(this.that).create();
                }
                this.n.setTitle(getString(R.string.dcloud_common_tips));
                if (TestUtil.PointTime.mEc == 20) {
                    this.n.setMessage(getString(R.string.dcloud_common_no_network_tips));
                } else if (str != null) {
                    AlertDialog alertDialog2 = this.n;
                    alertDialog2.setMessage(getString(R.string.dcloud_common_into) + str + getString(R.string.dcloud_common_fail) + getErrorTipMsg());
                } else {
                    AlertDialog alertDialog3 = this.n;
                    alertDialog3.setMessage(getString(R.string.dcloud_common_run_app_failed) + getErrorTipMsg());
                }
                this.n.setCanceledOnTouchOutside(false);
                this.n.setButton(-1, getString(R.string.dcloud_common_retry), new c(str2));
                this.n.setButton(-2, getString(R.string.dcloud_common_close), new d(str2));
                this.n.setOnKeyListener(new e(str2));
                this.n.show();
            }
        }
    }

    public void showSplashWaiting() {
        if (this.B) {
            View view = this.mSplashView;
            if (view instanceof SplashView) {
                ((SplashView) view).showWaiting();
            }
        }
    }

    public void sideBarHideMenu() {
    }

    public void sideBarShowMenu(String str, String str2, IWebview iWebview, String str3) {
    }

    public /* bridge */ /* synthetic */ void unregisterReceiver(io.dcloud.feature.internal.reflect.BroadcastReceiver broadcastReceiver) {
        super.unregisterReceiver(broadcastReceiver);
    }

    public void updateParam(String str, Object obj) {
        if ("progress".equals(str)) {
            View view = this.mSplashView;
            if (view instanceof g) {
                ((g) view).a(((Integer) obj).intValue());
            }
        } else if ("setProgressView".equals(str)) {
            setProgressView();
        } else {
            super.updateParam(str, obj);
        }
    }

    public void updateSplash(String str) {
        View view = this.mSplashView;
        if (view != null && (view instanceof ISplash)) {
            ((ISplash) view).setNameText(str);
        }
    }

    public /* bridge */ /* synthetic */ void registerReceiver(io.dcloud.feature.internal.reflect.BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        super.registerReceiver(broadcastReceiver, intentFilter);
    }

    private Bitmap a(String str, String str2, String str3) {
        Bitmap bitmap = null;
        try {
            if (!TextUtils.isEmpty(str)) {
                if (new File(str).exists()) {
                    Logger.d(Logger.MAIN_TAG, "use splashPath=" + str);
                    bitmap = BitmapFactory.decodeFile(str);
                    if (bitmap != null) {
                        this.B = false;
                        try {
                            DHFile.deleteFile(str);
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                        Logger.d(Logger.MAIN_TAG, "use splashPath=" + str);
                    }
                }
            }
            if (bitmap == null && !TextUtils.isEmpty(str2) && new File(str2).exists()) {
                Logger.d(Logger.MAIN_TAG, "use splashPath=" + str2);
                bitmap = BitmapFactory.decodeFile(str2);
            }
            if (bitmap != null || TextUtils.isEmpty(str3) || !new File(str3).exists()) {
                return bitmap;
            }
            Logger.d(Logger.MAIN_TAG, "use splashPath=" + str3);
            return BitmapFactory.decodeFile(str3);
        } catch (Exception e3) {
            e3.printStackTrace();
            return bitmap;
        }
    }

    /* access modifiers changed from: private */
    public void b(String str, int i) {
        Intent intent = new Intent(AbsoluteConst.ACTION_APP_DOWNLOAD_ERROR_DIALOG_CLICKED);
        intent.putExtra("type", i);
        intent.putExtra("appid", str);
        intent.setPackage(getPackageName());
        sendBroadcast(intent);
    }

    private Object a(Context context) {
        boolean booleanExtra;
        View view;
        View view2;
        Context context2 = context;
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra("appid");
        Logger.d("splash", "WebAppActivity.onCreateSplash().appid=" + stringExtra + " this.appid=" + this.e + " this=" + this.that);
        StringBuilder sb = new StringBuilder();
        sb.append("WebAppActivity.onCreateSplash().mSplashView is Null =");
        sb.append(this.mSplashView == null);
        Logger.d("splash", sb.toString());
        View view3 = this.mSplashView;
        if (!(view3 == null || view3.getTag() == null)) {
            if (this.mSplashView.getTag().equals(stringExtra)) {
                return null;
            }
            closeAppStreamSplash(this.mSplashView.getTag().toString());
        }
        this.B = true;
        Logger.d("WebAppActivity", "onCreateSplash;intent=" + intent);
        boolean booleanExtra2 = intent.getBooleanExtra(IntentConst.IS_START_FIRST_WEB, false);
        boolean z2 = intent.hasExtra(IntentConst.DIRECT_PAGE) && BaseInfo.isWap2AppAppid(stringExtra);
        if ((booleanExtra2 && !z2) || !(booleanExtra = intent.getBooleanExtra(IntentConst.SPLASH_VIEW, true))) {
            return null;
        }
        Logger.d("WebAppActivity", "onCreateSplash hasSplash=" + booleanExtra);
        if (intent.getBooleanExtra(IntentConst.PL_AUTO_HIDE, false)) {
            return null;
        }
        String stringExtra2 = intent.getStringExtra(IntentConst.WEBAPP_ACTIVITY_SPLASH_MODE);
        intent.removeExtra(IntentConst.WEBAPP_ACTIVITY_SPLASH_MODE);
        if (stringExtra2 == null || "".equals(stringExtra2.trim()) || (!"auto".equals(stringExtra2) && !"default".equals(stringExtra2))) {
            stringExtra2 = "auto";
        }
        Logger.d("WebAppActivity", "onCreateSplash __splash_mode__=" + stringExtra2);
        if (intent.getBooleanExtra(IntentConst.WEBAPP_ACTIVITY_HIDE_STREAM_SPLASH, false)) {
            setViewAsContentView(new View(context2), (FrameLayout.LayoutParams) null);
            this.w = System.currentTimeMillis();
            this.mSplashShowing = true;
            return null;
        }
        String str = "splash";
        String str2 = "";
        if (intent.getBooleanExtra(IntentConst.WEBAPP_ACTIVITY_HAS_STREAM_SPLASH, false)) {
            if (this.mSplashView == null) {
                if ("auto".equals(stringExtra2)) {
                    if (!z2) {
                        StringBuilder sb2 = new StringBuilder();
                        String str3 = StringConst.STREAMAPP_KEY_ROOTPATH;
                        sb2.append(str3);
                        sb2.append("splash_temp/");
                        sb2.append(stringExtra);
                        sb2.append(".png");
                        String sb3 = sb2.toString();
                        String stringExtra3 = intent.getStringExtra(IntentConst.APP_SPLASH_PATH);
                        this.v = a(sb3, stringExtra3, str3 + "splash/" + stringExtra + ".png");
                    }
                    if (this.v != null) {
                        SplashView splashView = new SplashView(this.that, this.v);
                        this.mSplashView = splashView;
                        if (!this.B) {
                            splashView.showWaiting(SplashView.STYLE_BLACK);
                        }
                    }
                }
                if (this.mSplashView == null) {
                    String stringExtra4 = intent.getStringExtra(IntentConst.WEBAPP_ACTIVITY_APPICON);
                    if (!TextUtils.isEmpty(stringExtra4) && new File(stringExtra4).exists()) {
                        this.v = BitmapFactory.decodeFile(stringExtra4);
                    }
                    this.mSplashView = a(context2, this.v, intent.getStringExtra(IntentConst.NAME), stringExtra);
                    if (this.v == null && PdrUtil.isEmpty(stringExtra) && (view2 = this.mSplashView) != null && (view2 instanceof ISplash)) {
                        ((ISplash) view2).setImageBitmap(BitmapFactory.decodeResource(getResources(), PdrR.getInt(getContext(), "drawable", AbsoluteConst.JSON_KEY_ICON)));
                    }
                }
            }
            this.mSplashView.setTag(stringExtra);
            setViewAsContentView(this.mSplashView, (FrameLayout.LayoutParams) null);
            this.w = System.currentTimeMillis();
            this.mSplashShowing = true;
            Logger.e("IAN", "onCreateSplash aliyun ended");
            return null;
        } else if (this.mSplashView != null) {
            return null;
        } else {
            try {
                if ("auto".equals(stringExtra2)) {
                    if (!z2) {
                        StringBuilder sb4 = new StringBuilder();
                        String str4 = StringConst.STREAMAPP_KEY_ROOTPATH;
                        sb4.append(str4);
                        sb4.append("splash_temp/");
                        sb4.append(stringExtra);
                        sb4.append(".png");
                        String sb5 = sb4.toString();
                        String stringExtra5 = intent.getStringExtra(IntentConst.APP_SPLASH_PATH);
                        this.v = a(sb5, stringExtra5, str4 + "splash/" + stringExtra + ".png");
                    }
                    if (this.v == null) {
                        String string = SP.getOrCreateBundle(getContext(), "pdr").getString(SP.UPDATE_SPLASH_IMG_PATH, str2);
                        if (!TextUtils.isEmpty(string)) {
                            try {
                                if (PdrUtil.isDeviceRootDir(string)) {
                                    this.v = BitmapFactory.decodeFile(string);
                                } else {
                                    InputStream open = getResources().getAssets().open(string);
                                    this.v = BitmapFactory.decodeStream(open);
                                    open.close();
                                }
                            } catch (Exception unused) {
                                this.v = null;
                            }
                        }
                        if (this.v == null && !this.o) {
                            this.v = BitmapFactory.decodeResource(getResources(), PdrR.getInt(context2, "drawable", str));
                        }
                    }
                    if (this.v != null) {
                        SplashView splashView2 = new SplashView(this.that, this.v);
                        this.mSplashView = splashView2;
                        if (!this.B) {
                            splashView2.showWaiting(SplashView.STYLE_BLACK);
                        }
                    }
                }
                if (this.mSplashView == null) {
                    String stringExtra6 = intent.getStringExtra(IntentConst.WEBAPP_ACTIVITY_APPICON);
                    if (!TextUtils.isEmpty(stringExtra6) && new File(stringExtra6).exists()) {
                        this.v = BitmapFactory.decodeFile(stringExtra6);
                    }
                    Log.d(Logger.MAIN_TAG, "use defaultSplash");
                    this.mSplashView = a(context2, this.v, intent.getStringExtra(IntentConst.NAME), stringExtra);
                    if (this.v == null && PdrUtil.isEmpty(stringExtra) && (view = this.mSplashView) != null && (view instanceof ISplash)) {
                        ((ISplash) view).setImageBitmap(BitmapFactory.decodeResource(getResources(), PdrR.getInt(getContext(), "drawable", AbsoluteConst.JSON_KEY_ICON)));
                    }
                }
                this.mSplashView.setTag(stringExtra);
                setViewAsContentView(this.mSplashView, (FrameLayout.LayoutParams) null);
                this.w = System.currentTimeMillis();
                this.mSplashShowing = true;
                return null;
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    private View a(Context context, Bitmap bitmap, String str, String str2) {
        boolean z2;
        String b2 = b("DCLOUD_STREAMAPP_CHANNEL");
        if (!PdrUtil.isEmpty(str2) || !PdrUtil.isEmpty(str)) {
            z2 = false;
        } else {
            str = getString(PdrR.getInt(context, "string", IntentConst.WEBAPP_ACTIVITY_APPNAME));
            z2 = true;
        }
        if (!"_12214060304".equals(b2)) {
            return new SplashViewDBackground(context, bitmap, str, z2);
        }
        return (View) PlatformUtil.newInstance("io.dcloud.html5pframework.splash.SplashView4Yunos", new Class[]{Context.class, Bitmap.class, String.class}, new Object[]{context, bitmap, str});
    }
}
