package io.dcloud;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.performance.WXInstanceApm;
import io.dcloud.application.DCLoudApplicationImpl;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.ICore;
import io.dcloud.common.DHInterface.IKeyHandler;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IOnCreateSplashView;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebViewFactory;
import io.dcloud.common.DHInterface.IWebViewInstallListener;
import io.dcloud.common.adapter.ui.webview.WebViewFactory;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.PermissionUtil;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.adapter.util.UEH;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.core.permission.PermissionControler;
import io.dcloud.common.ui.Info.AndroidPrivacyResponse;
import io.dcloud.common.ui.b;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.DensityUtils;
import io.dcloud.common.util.ErrorDialogUtil;
import io.dcloud.common.util.ImageLoaderUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.TestUtil;
import io.dcloud.common.util.net.http.CookieManager;
import io.dcloud.e.d.a;
import io.dcloud.feature.internal.sdk.SDK;
import java.util.HashMap;

abstract class b extends c implements IOnCreateSplashView, IKeyHandler {
    String d = null;
    String e = "Main_App";
    EntryProxy f = null;
    /* access modifiers changed from: private */
    public String g;
    /* access modifiers changed from: private */
    public String h;
    AlertDialog i;
    int j = 20;
    /* access modifiers changed from: private */
    public int k = 9101;
    /* access modifiers changed from: private */
    public int l = 9102;
    Runnable m;

    class a implements Runnable {
        final /* synthetic */ Bundle a;

        /* renamed from: io.dcloud.b$a$a  reason: collision with other inner class name */
        class C0011a implements Runnable {
            C0011a() {
            }

            public void run() {
                a aVar = a.this;
                b.this.onRuntimeCreate(aVar.a);
            }
        }

        a(Bundle bundle) {
            this.a = bundle;
        }

        public void run() {
            DeviceInfo.initPath(b.this.that);
            ImageLoaderUtil.initImageLoader(b.this.that);
            ImageLoaderUtil.initImageLoaderL(b.this.that);
            a.a(b.this, (String) null, "ba_pull", (Object) null);
            b bVar = b.this;
            bVar.a(bVar.getIntent());
            b bVar2 = b.this;
            bVar2.d = "Main_Path_" + b.this.e;
            io.dcloud.feature.internal.splash.a.a("Main_App");
            String str = b.this.d;
            Logger.d(str, "onCreate appid=" + b.this.e);
            b.this.a((Runnable) new C0011a());
        }
    }

    /* renamed from: io.dcloud.b$b  reason: collision with other inner class name */
    class C0012b implements ICallBack {

        /* renamed from: io.dcloud.b$b$a */
        class a implements View.OnClickListener {
            a() {
            }

            public void onClick(View view) {
                Process.killProcess(Process.myPid());
            }
        }

        C0012b() {
        }

        public Object onCallBack(int i, Object obj) {
            io.dcloud.common.ui.e.a aVar = new io.dcloud.common.ui.e.a(b.this.that);
            aVar.b(b.this.getContext().getString(R.string.dcloud_common_tips));
            aVar.a(b.this.getContext().getString(R.string.dcloud_ua_version_verify_fail_tips));
            aVar.b(b.this.that.getString(17039370), new a());
            aVar.show();
            double d = (double) b.this.getResources().getDisplayMetrics().widthPixels;
            Double.isNaN(d);
            aVar.b((int) (d * 0.9d));
            aVar.a(17);
            return null;
        }
    }

    class c implements ICallBack {
        final /* synthetic */ ICallBack a;
        final /* synthetic */ ICallBack b;
        final /* synthetic */ Runnable c;

        c(ICallBack iCallBack, ICallBack iCallBack2, Runnable runnable) {
            this.a = iCallBack;
            this.b = iCallBack2;
            this.c = runnable;
        }

        public Object onCallBack(int i, Object obj) {
            int intValue = ((Integer) this.a.onCallBack(i, obj)).intValue();
            if (intValue == 1) {
                this.b.onCallBack(i, (Object) null);
                return Boolean.FALSE;
            } else if (intValue == 2) {
                Runnable runnable = this.c;
                if (runnable != null) {
                    runnable.run();
                }
                return Boolean.FALSE;
            } else if (intValue != 3) {
                return Boolean.FALSE;
            } else {
                Runnable runnable2 = this.c;
                if (runnable2 != null) {
                    runnable2.run();
                }
                return Boolean.TRUE;
            }
        }
    }

    class d implements ICallBack {
        final /* synthetic */ ICallBack a;

        class a implements ICallBack {
            final /* synthetic */ TextView[] a;

            a(TextView[] textViewArr) {
                this.a = textViewArr;
            }

            public Object onCallBack(int i, Object obj) {
                if (obj == null) {
                    return null;
                }
                this.a[0] = (TextView) obj;
                return null;
            }
        }

        /* renamed from: io.dcloud.b$d$b  reason: collision with other inner class name */
        class C0013b implements ICallBack {
            final /* synthetic */ Dialog a;

            C0013b(Dialog dialog) {
                this.a = dialog;
            }

            public Object onCallBack(int i, Object obj) {
                this.a.dismiss();
                return d.this.a.onCallBack(i, obj);
            }
        }

        class c implements IWebViewInstallListener {
            final /* synthetic */ TextView[] a;

            class a implements Runnable {
                final /* synthetic */ int a;

                a(int i) {
                    this.a = i;
                }

                public void run() {
                    c cVar = c.this;
                    cVar.a[0].setText(String.format(b.this.getContext().getString(R.string.dcloud_x5_download_progress), new Object[]{Integer.valueOf(this.a)}));
                }
            }

            c(TextView[] textViewArr) {
                this.a = textViewArr;
            }

            public void onDownloadFinish(int i) {
            }

            public void onDownloadProgress(int i) {
                if (this.a[0] != null) {
                    new Handler(Looper.getMainLooper()).post(new a(i));
                }
            }

            public void onInstallFinish(int i) {
            }
        }

        d(ICallBack iCallBack) {
            this.a = iCallBack;
        }

        public Object onCallBack(int i, Object obj) {
            TextView[] textViewArr = {null};
            b bVar = b.this;
            Dialog a2 = bVar.a((Context) bVar, (ICallBack) new a(textViewArr));
            a2.show();
            WebViewFactory.setOtherCallBack(new C0013b(a2));
            WebViewFactory.setWebViewInstallListener(new c(textViewArr));
            return null;
        }
    }

    class e implements ICallBack {
        final /* synthetic */ ICallBack a;
        final /* synthetic */ boolean b;
        final /* synthetic */ ICallBack c;

        class a implements View.OnClickListener {
            a() {
            }

            public void onClick(View view) {
                AppRuntime.initX5(b.this.getApplication(), true, (ICallBack) null);
                e.this.a.onCallBack(1, (Object) null);
            }
        }

        /* renamed from: io.dcloud.b$e$b  reason: collision with other inner class name */
        class C0014b implements View.OnClickListener {
            C0014b() {
            }

            public void onClick(View view) {
                e.this.c.onCallBack(-1, (Object) null);
            }
        }

        e(ICallBack iCallBack, boolean z, ICallBack iCallBack2) {
            this.a = iCallBack;
            this.b = z;
            this.c = iCallBack2;
        }

        public Object onCallBack(int i, Object obj) {
            if (i == 0) {
                this.a.onCallBack(0, (Object) null);
            } else if (i == 1) {
                if (this.b) {
                    io.dcloud.common.ui.e.a aVar = new io.dcloud.common.ui.e.a(b.this.that);
                    aVar.b(b.this.getContext().getString(R.string.dcloud_common_tips));
                    aVar.a(b.this.getContext().getString(R.string.dcloud_x5_download_without_wifi));
                    aVar.b(b.this.that.getString(R.string.dcloud_common_allow), new a());
                    aVar.a(b.this.that.getString(R.string.dcloud_common_no_allow), new C0014b());
                    aVar.show();
                } else {
                    this.c.onCallBack(-1, (Object) null);
                }
            }
            return null;
        }
    }

    class f implements DialogInterface.OnClickListener {
        f() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            Process.killProcess(Process.myPid());
        }
    }

    class h implements a.b {
        h() {
        }

        public void a(String str, boolean z) {
            DeviceInfo.oaids = str;
            SP.setBundleData(b.this.getContext(), BaseInfo.PDR, "android_ten_ids", str);
        }
    }

    class j extends PermissionUtil.Request {
        j() {
        }

        public void onDenied(String str) {
            if (str.equals("android.permission.READ_PHONE_STATE") && b.this.g != null && b.this.g.equalsIgnoreCase("ALWAYS")) {
                int i = PdrR.getInt(b.this, "string", "dcloud_permission_read_phone_state_message");
                AlertDialog alertDialog = b.this.i;
                if (alertDialog == null || !alertDialog.isShowing()) {
                    b.this.a(str, i);
                }
            } else if (!str.equals(PermissionUtil.PMS_STORAGE)) {
            } else {
                if (b.this.h == null || !b.this.h.equals("once")) {
                    int i2 = PdrR.getInt(b.this, "string", "dcloud_permission_write_external_storage_message");
                    AlertDialog alertDialog2 = b.this.i;
                    if (alertDialog2 == null || !alertDialog2.isShowing()) {
                        b.this.a(PermissionUtil.convert2SystemPermission(str), i2);
                        return;
                    }
                    return;
                }
                b.this.checkAndRequestPhoneState();
                Handler handler = new Handler();
                b bVar = b.this;
                handler.postDelayed(bVar.m, (long) bVar.j);
            }
        }

        public void onGranted(String str) {
            if (str.equals(PermissionUtil.PMS_STORAGE)) {
                DeviceInfo.initPath(b.this.that);
                b.this.checkAndRequestPhoneState();
                Handler handler = new Handler();
                b bVar = b.this;
                handler.postDelayed(bVar.m, (long) bVar.j);
            }
        }
    }

    class k implements DialogInterface.OnClickListener {
        k() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            b.this.finish();
        }
    }

    class l implements DialogInterface.OnClickListener {
        final /* synthetic */ String a;

        l(String str) {
            this.a = str;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(b.this, PermissionUtil.convert2SystemPermission(this.a))) {
                try {
                    Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", b.this.getPackageName(), (String) null));
                    int d = b.this.k;
                    if (!this.a.equalsIgnoreCase("android.permission.READ_PHONE_STATE")) {
                        d = b.this.l;
                    }
                    b.this.startActivityForResult(intent, d);
                } catch (Exception unused) {
                    b.this.finish();
                    Process.killProcess(Process.myPid());
                }
            } else {
                b.this.a(new String[]{this.a});
            }
        }
    }

    class m implements ICallBack {
        m() {
        }

        public Object onCallBack(int i, Object obj) {
            String str = BaseInfo.minUserAgentVersion;
            if (PdrUtil.isEmpty(str)) {
                str = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT;
            }
            WebViewFactory.resetSysWebViewState();
            WebViewFactory.resetUA();
            int i2 = WebViewFactory.verifyVersion(WebViewFactory.getWebViewUserAgentVersion(b.this.getContext()), str) ? 2 : 1;
            if (i == 1 && obj != null && WebViewFactory.verifyVersion(WebViewFactory.getWebViewUserAgentVersion(b.this.getApplication(), ((IWebViewFactory) obj).getDefWebViewUA(b.this.getApplication())), str)) {
                i2 = 3;
            }
            return Integer.valueOf(i2);
        }
    }

    b() {
    }

    public void checkAndRequestPhoneState() {
        String str = this.g;
        if (str == null) {
            return;
        }
        if (str.equalsIgnoreCase("once")) {
            if (!SP.getBundleData(getContext(), "dcloud_phone_read_state", "isshow").equals("1")) {
                SP.setBundleData(getContext(), "dcloud_phone_read_state", "isshow", "1");
                a(new String[]{"android.permission.READ_PHONE_STATE"});
            }
        } else if (this.g.equalsIgnoreCase("always")) {
            a(new String[]{"android.permission.READ_PHONE_STATE"});
        }
    }

    public void checkAndRequestStoragePermission() {
        if (SDK.isUniMPSDK()) {
            new Handler().postDelayed(this.m, (long) this.j);
            return;
        }
        String[] strArr = null;
        String str = this.h;
        if (str == null) {
            strArr = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};
        } else if (str.equalsIgnoreCase("once")) {
            if (!SP.getBundleData(getContext(), "dcloud_phone_read_state", "isStorageRequest").equals("1")) {
                strArr = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};
                SP.setBundleData(getContext(), "dcloud_phone_read_state", "isStorageRequest", "1");
            }
        } else if (this.h.equalsIgnoreCase("always")) {
            strArr = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};
        }
        if (strArr != null) {
            a(strArr);
            return;
        }
        checkAndRequestPhoneState();
        new Handler().postDelayed(this.m, (long) this.j);
    }

    /* access modifiers changed from: protected */
    public void displayBriefMemory() {
        ((ActivityManager) getSystemService("activity")).getMemoryInfo(new ActivityManager.MemoryInfo());
    }

    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = resources.getConfiguration();
        try {
            if (!"none".equals(BaseInfo.sFontScale)) {
                float f2 = configuration.fontScale;
                float f3 = BaseInfo.sFontScaleFloat;
                if (f2 != f3) {
                    configuration.fontScale = f3;
                }
            } else if (configuration.fontScale != 1.0f) {
                configuration.fontScale = 1.0f;
            }
        } catch (Exception unused) {
        }
        return resources;
    }

    /* access modifiers changed from: protected */
    public void handleNewIntent(Intent intent) {
        EntryProxy entryProxy;
        setIntent(intent);
        a(intent);
        StringBuilder sb = new StringBuilder();
        sb.append("BaseActivity handleNewIntent =");
        sb.append(this.e);
        sb.append(";");
        sb.append(intent.getFlags() != 274726912);
        Logger.d("syncStartApp", sb.toString());
        if (!(intent.getFlags() == 274726912 || (entryProxy = this.f) == null)) {
            entryProxy.onNewIntent(this.that, intent);
        }
        if (BaseInfo.SyncDebug && Build.VERSION.SDK_INT >= 21 && intent.getBooleanExtra("debug_restart", false)) {
            EntryProxy entryProxy2 = this.f;
            if (entryProxy2 == null || entryProxy2.getCoreHandler() == null) {
                intent.setFlags(335544320);
                startActivity(intent);
                Runtime.getRuntime().exit(0);
                return;
            }
            String stringExtra = intent.getStringExtra("appid");
            ICore coreHandler = this.f.getCoreHandler();
            IMgr.MgrType mgrType = IMgr.MgrType.AppMgr;
            if (PdrUtil.isEmpty(stringExtra)) {
                stringExtra = "snc:CID";
            }
            coreHandler.dispatchEvent(mgrType, 3, stringExtra);
        }
    }

    public boolean hasAdService() {
        return true;
    }

    public void onActivityResult(int i2, int i3, Intent intent) {
        String str;
        AlertDialog alertDialog;
        Logger.d(this.d, "onActivityResult");
        PermissionUtil.onActivityResult(this.that, i2, i3, intent);
        EntryProxy entryProxy = this.f;
        if (entryProxy != null) {
            entryProxy.onActivityExecute(this.that, ISysEventListener.SysEventType.onActivityResult, new Object[]{Integer.valueOf(i2), Integer.valueOf(i3), intent});
        }
        if (i2 == this.k && (str = this.g) != null && str.equalsIgnoreCase("always") && (alertDialog = this.i) != null && !alertDialog.isShowing()) {
            a(new String[]{"android.permission.READ_PHONE_STATE"});
        }
        if (i2 == this.l) {
            String str2 = this.h;
            if (str2 == null || (!str2.equalsIgnoreCase("once") && !this.h.equalsIgnoreCase("none"))) {
                a(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"});
            }
        }
    }

    public void onBackPressed() {
        EntryProxy entryProxy;
        if (!BaseInfo.USE_ACTIVITY_HANDLE_KEYEVENT) {
            super.onBackPressed();
        } else if (!onKeyEventExecute(ISysEventListener.SysEventType.onKeyUp, 4, (KeyEvent) null) && (entryProxy = this.f) != null) {
            entryProxy.destroy(this.that);
            super.onBackPressed();
        }
    }

    public void onCloseSplash() {
    }

    public void onConfigurationChanged(Configuration configuration) {
        try {
            Logger.d(this.d, "onConfigurationChanged");
            int i2 = getResources().getConfiguration().orientation;
            EntryProxy entryProxy = this.f;
            if (entryProxy != null) {
                entryProxy.onConfigurationChanged(this.that, i2);
            }
            super.onConfigurationChanged(configuration);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        io.dcloud.common.ui.b.a().a((Activity) this);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (io.dcloud.common.ui.b.c()) {
            io.dcloud.common.ui.b.a().b(getContext(), (JSONObject) null);
        }
        e();
        c();
        String metaValue = AndroidResources.getMetaValue("DCLOUD_READ_PHONE_STATE");
        this.g = metaValue;
        if (metaValue == null) {
            this.g = "none";
        }
        this.m = new a(bundle);
        String metaValue2 = AndroidResources.getMetaValue("DCLOUD_UNISTATISTICS");
        BaseInfo.isUniStatistics = false;
        if (!TextUtils.isEmpty(metaValue2) && Boolean.parseBoolean(metaValue2)) {
            BaseInfo.isUniStatistics = true;
        }
        if (!DCLoudApplicationImpl.self().isInit()) {
            String string = getString(R.string.dcloud_Init_fail_tips);
            BaseInfo.USE_ACTIVITY_HANDLE_KEYEVENT = true;
            ErrorDialogUtil.showErrorTipsAlert(this, string, new f());
            return;
        }
        String metaValue3 = AndroidResources.getMetaValue("DCLOUD_WRITE_EXTERNAL_STORAGE");
        this.h = metaValue3;
        if (metaValue3 == null) {
            this.h = "none";
        }
        io.dcloud.common.ui.b.a().a(this, new g(), false, false);
        UEH.catchUncaughtException(this.that);
        Log.d("download_manager", "BaseActivity onCreate");
        TestUtil.print(TestUtil.START_STREAM_APP, "BaseActivity onCreate");
        onRuntimePreCreate(bundle);
        onCreateSplash(this.that);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        String str = this.d;
        Logger.d(str, "onCreateOptionsMenu appid=" + this.e);
        EntryProxy entryProxy = this.f;
        if (entryProxy != null) {
            return entryProxy.onActivityExecute(this.that, ISysEventListener.SysEventType.onCreateOptionMenu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public abstract Object onCreateSplash(Context context);

    public void onDestroy() {
        super.onDestroy();
        io.dcloud.feature.internal.splash.a.b("Main_App");
        String str = this.d;
        Logger.d(str, "onDestroy appid=" + this.e);
        EntryProxy entryProxy = this.f;
        if (entryProxy != null) {
            entryProxy.onStop(this.that);
        }
        HashMap<String, BaseInfo.CmtInfo> hashMap = BaseInfo.mLaunchers;
        if (hashMap != null) {
            hashMap.clear();
        }
        MessageHandler.removeCallbacksAndMessages();
        PermissionControler.clearCRequestPermissionsCache();
        io.dcloud.e.c.e.a().b();
    }

    public boolean onKeyDown(int i2, KeyEvent keyEvent) {
        boolean z;
        Logger.e("back", "BaseActivity onKeyDown");
        if (!BaseInfo.USE_ACTIVITY_HANDLE_KEYEVENT) {
            return super.onKeyDown(i2, keyEvent);
        }
        if (keyEvent.getRepeatCount() == 0) {
            z = onKeyEventExecute(ISysEventListener.SysEventType.onKeyDown, i2, keyEvent);
        } else {
            z = onKeyEventExecute(ISysEventListener.SysEventType.onKeyLongPress, i2, keyEvent);
        }
        if (z && i2 == 4) {
            onBackPressed();
        }
        return z ? z : super.onKeyDown(i2, keyEvent);
    }

    public boolean onKeyEventExecute(ISysEventListener.SysEventType sysEventType, int i2, KeyEvent keyEvent) {
        EntryProxy entryProxy = this.f;
        if (entryProxy == null) {
            return false;
        }
        return entryProxy.onActivityExecute(this.that, sysEventType, new Object[]{Integer.valueOf(i2), keyEvent});
    }

    public boolean onKeyLongPress(int i2, KeyEvent keyEvent) {
        if (!BaseInfo.USE_ACTIVITY_HANDLE_KEYEVENT) {
            return super.onKeyLongPress(i2, keyEvent);
        }
        EntryProxy entryProxy = this.f;
        boolean z = false;
        if (entryProxy != null) {
            z = entryProxy.onActivityExecute(this.that, ISysEventListener.SysEventType.onKeyLongPress, new Object[]{Integer.valueOf(i2), keyEvent});
        }
        return z ? z : super.onKeyLongPress(i2, keyEvent);
    }

    public boolean onKeyUp(int i2, KeyEvent keyEvent) {
        EntryProxy entryProxy;
        if (!BaseInfo.USE_ACTIVITY_HANDLE_KEYEVENT) {
            return super.onKeyUp(i2, keyEvent);
        }
        Logger.d(this.d, "onKeyUp");
        boolean z = false;
        if (!(i2 == 4 || (entryProxy = this.f) == null)) {
            z = entryProxy.onActivityExecute(this.that, ISysEventListener.SysEventType.onKeyUp, new Object[]{Integer.valueOf(i2), keyEvent});
        }
        return z ? z : super.onKeyUp(i2, keyEvent);
    }

    public void onLowMemory() {
        super.onLowMemory();
        Logger.d(this.d, "onLowMemory");
        displayBriefMemory();
    }

    public void onNewIntentImpl(Intent intent) {
        super.onNewIntentImpl(intent);
        Logger.d("syncStartApp", "BaseActivity onNewIntent appid=" + this.e);
        handleNewIntent(intent);
    }

    public void onPause() {
        super.onPause();
        String str = this.d;
        Logger.d(str, "onPause appid=" + this.e);
        EntryProxy entryProxy = this.f;
        if (entryProxy != null) {
            entryProxy.onPause(this.that);
        }
    }

    public void onRequestPermissionsResult(int i2, String[] strArr, int[] iArr) {
        PermissionUtil.onSystemPermissionsResult(this.that, i2, strArr, iArr);
        EntryProxy entryProxy = this.f;
        if (entryProxy != null) {
            entryProxy.onActivityExecute(this.that, ISysEventListener.SysEventType.onRequestPermissionsResult, new Object[]{Integer.valueOf(i2), strArr, iArr});
        }
        PermissionControler.runNextRequestPermission(this, i2);
    }

    public void onResume() {
        super.onResume();
        a(getIntent());
        PermissionUtil.onRequestSysPermissionResume(this.that);
        String str = this.d;
        Logger.d(str, "onResume appid=" + this.e);
        EntryProxy entryProxy = this.f;
        if (entryProxy != null) {
            entryProxy.onResume(this.that);
        }
        if (Build.VERSION.SDK_INT >= 21 && BaseInfo.mDeStatusBarBackground == -111111) {
            BaseInfo.mDeStatusBarBackground = getWindow().getStatusBarColor();
        }
    }

    /* access modifiers changed from: protected */
    public void onRuntimeCreate(Bundle bundle) {
        String str = this.d;
        Logger.d(str, "onRuntimeCreate appid=" + this.e);
        EntryProxy init = EntryProxy.init(this.that);
        this.f = init;
        init.onCreate(this.that, bundle, BaseInfo.sRuntimeMode, (IOnCreateSplashView) null);
    }

    /* access modifiers changed from: protected */
    public void onRuntimePreCreate(Bundle bundle) {
        String str = this.d;
        Log.d(str, "onRuntimePreCreate appid=" + this.e);
        this.that.getWindow().setFormat(-3);
    }

    public void onSaveInstanceState(Bundle bundle) {
        if (!(bundle == null || getIntent() == null || getIntent().getExtras() == null)) {
            bundle.putAll(getIntent().getExtras());
        }
        Logger.d(this.d, "onSaveInstanceState");
        EntryProxy entryProxy = this.f;
        if (entryProxy != null) {
            entryProxy.onActivityExecute(this.that, ISysEventListener.SysEventType.onSaveInstanceState, new Object[]{bundle});
        }
        super.onSaveInstanceState(bundle);
    }

    public void setSecondPrivacyAlert() {
        io.dcloud.common.ui.b.a().a(this, new i(), true, false);
    }

    public void updateParam(String str, Object obj) {
        if ("tab_change".equals(str)) {
            Logger.d("BaseActivity updateParam newintent value(appid)=" + obj);
            this.f.getCoreHandler().dispatchEvent(IMgr.MgrType.AppMgr, 21, obj);
        } else if ("closewebapp".equals(str)) {
            Logger.e("IAN", "updateParam closewebapp");
            Activity activity = (Activity) obj;
            Bundle extras = activity.getIntent().getExtras();
            String string = (extras == null || !extras.containsKey("appid")) ? null : extras.getString("appid");
            if (TextUtils.isEmpty(string)) {
                string = BaseInfo.sDefaultBootApp;
            }
            if (activity instanceof IActivityHandler) {
                ((IActivityHandler) activity).closeAppStreamSplash(string);
            }
            this.f.getCoreHandler().dispatchEvent((IMgr.MgrType) null, 0, new Object[]{activity, activity.getIntent(), string});
            Logger.e("IAN", "updateParam closewebapp WEBAPP_QUIT");
        }
    }

    private void c() {
        String metaValue = AndroidResources.getMetaValue("DClOUD_SECURITY_POLICY");
        if (TextUtils.isEmpty(metaValue) || !metaValue.equals("safe")) {
            BaseInfo.isDefense = false;
        } else {
            BaseInfo.isDefense = true;
        }
    }

    /* access modifiers changed from: private */
    public void d() {
        if (!SDK.isUniMPSDK() || TextUtils.isEmpty(SDK.customOAID)) {
            if (!PdrUtil.checkIntl() && !BaseInfo.isChannelGooglePlay()) {
                try {
                    Class.forName("com.bun.miitmdid.core.MdidSdkHelper");
                } catch (ClassNotFoundException unused) {
                    throw new RuntimeException(getString(R.string.dcloud_common_app_not_oaid));
                }
            }
            if (PdrUtil.isSupportOaid() && !AppRuntime.hasPrivacyForNotShown(this)) {
                if (PdrUtil.isEmpty(DeviceInfo.oaids) || DeviceInfo.oaids.equals(Operators.OR)) {
                    DeviceInfo.oaids = SP.getBundleData(getContext(), BaseInfo.PDR, "android_ten_ids");
                    new io.dcloud.e.d.a(new h()).b(this);
                }
            }
        }
    }

    private void e() {
        io.dcloud.e.c.e.a().a(new io.dcloud.e.c.f().getPdrModuleMap());
    }

    class i implements b.C0023b {
        i() {
        }

        public void a(String str) {
            b.this.d();
            AppRuntime.initUTS();
            CookieManager.initCookieConfig(b.this.getApplication());
        }

        public void b(AndroidPrivacyResponse androidPrivacyResponse) {
            if (!androidPrivacyResponse.disagreeMode.support) {
                b.this.finish();
                Process.killProcess(Process.myPid());
                return;
            }
            Handler handler = new Handler();
            b bVar = b.this;
            handler.postDelayed(bVar.m, (long) bVar.j);
            if (androidPrivacyResponse.disagreeMode.loadNativePlugins) {
                AppRuntime.initUniappPlugin(b.this.getApplication());
            }
            CookieManager.initCookieConfig(b.this.getApplication());
        }

        public void a() {
            b.this.d();
            b.this.checkAndRequestStoragePermission();
            AppRuntime.initUniappPlugin(b.this.getApplication());
            AppRuntime.initUTS();
            CookieManager.initCookieConfig(b.this.getApplication());
        }

        public void a(AndroidPrivacyResponse androidPrivacyResponse) {
            Handler handler = new Handler();
            b bVar = b.this;
            handler.postDelayed(bVar.m, (long) bVar.j);
            if (androidPrivacyResponse.disagreeMode.loadNativePlugins) {
                AppRuntime.initUniappPlugin(b.this.getApplication());
            }
            CookieManager.initCookieConfig(b.this.getApplication());
        }
    }

    /* access modifiers changed from: private */
    public void a(String[] strArr) {
        PermissionUtil.useSystemPermissions(this.that, strArr, new j());
    }

    class g implements b.C0023b {
        g() {
        }

        public void a(String str) {
            b.this.d();
            if (str.equalsIgnoreCase("custom")) {
                Handler handler = new Handler();
                b bVar = b.this;
                handler.postDelayed(bVar.m, (long) bVar.j);
            } else {
                b.this.checkAndRequestStoragePermission();
            }
            CookieManager.initCookieConfig(b.this.getApplication());
            AppRuntime.initUTS();
        }

        public void b(AndroidPrivacyResponse androidPrivacyResponse) {
            if (!TextUtils.isEmpty(androidPrivacyResponse.second.message)) {
                b.this.setSecondPrivacyAlert();
            } else if (!androidPrivacyResponse.disagreeMode.support) {
                b.this.finish();
                Process.killProcess(Process.myPid());
            } else {
                Handler handler = new Handler();
                b bVar = b.this;
                handler.postDelayed(bVar.m, (long) bVar.j);
                if (androidPrivacyResponse.disagreeMode.loadNativePlugins) {
                    AppRuntime.initUniappPlugin(b.this.getApplication());
                }
                CookieManager.initCookieConfig(b.this.getApplication());
            }
        }

        public void a() {
            b.this.d();
            b.this.checkAndRequestStoragePermission();
            AppRuntime.initUniappPlugin(b.this.getApplication());
            AppRuntime.initUTS();
            CookieManager.initCookieConfig(b.this.getApplication());
        }

        public void a(AndroidPrivacyResponse androidPrivacyResponse) {
            Handler handler = new Handler();
            b bVar = b.this;
            handler.postDelayed(bVar.m, (long) bVar.j);
            if (androidPrivacyResponse.disagreeMode.loadNativePlugins) {
                AppRuntime.initUniappPlugin(b.this.getApplication());
            }
            CookieManager.initCookieConfig(b.this.getApplication());
        }
    }

    /* access modifiers changed from: private */
    public void a(String str, int i2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.that);
        if (i2 == 0) {
            i2 = PdrR.getInt(this, "string", IntentConst.WEBAPP_ACTIVITY_APPNAME);
        }
        AlertDialog create = builder.setMessage(i2).setPositiveButton(17039370, (DialogInterface.OnClickListener) new l(str)).setNegativeButton(17039360, (DialogInterface.OnClickListener) new k()).create();
        this.i = create;
        create.setCanceledOnTouchOutside(false);
        this.i.setCancelable(false);
        this.i.show();
    }

    /* access modifiers changed from: private */
    public void a(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null && extras.containsKey("appid")) {
            this.e = extras.getString("appid");
        }
    }

    /* access modifiers changed from: private */
    public void a(Runnable runnable) {
        if (!PdrUtil.isEquals(BaseInfo.renderer.toLowerCase(), "native")) {
            m mVar = new m();
            c cVar = new c(mVar, new C0012b(), runnable);
            d dVar = new d(cVar);
            boolean z = false;
            try {
                Class.forName("io.dcloud.feature.x5.X5InitImpl");
                z = true;
            } catch (ClassNotFoundException unused) {
            }
            if (!z) {
                cVar.onCallBack(-1, (Object) null);
                return;
            }
            AppRuntime.preInitX5(getApplication());
            if (WebViewFactory.isOtherInitialised() || WebViewFactory.isIsLoadOtherTimeOut()) {
                cVar.onCallBack(-1, (Object) null);
                return;
            }
            int intValue = ((Integer) mVar.onCallBack(-1, (Object) null)).intValue();
            if (intValue == 1) {
                boolean z2 = BaseInfo.showTipsWithoutWifi;
                boolean z3 = BaseInfo.allowDownloadWithoutWiFi;
                if (!WebViewFactory.isOther()) {
                    AppRuntime.initX5(getApplication(), z3, new e(dVar, z2, cVar));
                } else {
                    dVar.onCallBack(z2 ? 1 : 0, (Object) null);
                }
            } else if (intValue == 2) {
                AppRuntime.initX5(getApplication(), BaseInfo.allowDownloadWithoutWiFi, (ICallBack) null);
                if (runnable != null) {
                    runnable.run();
                }
            }
        } else if (runnable != null) {
            runnable.run();
        }
    }

    /* access modifiers changed from: private */
    public Dialog a(Context context, ICallBack iCallBack) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog_transparent);
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.dcloud_dialog_loading, (ViewGroup) null);
        viewGroup.findViewById(R.id.loading_background).setBackgroundColor(0);
        viewGroup.findViewById(R.id.bg).setLayoutParams(new LinearLayout.LayoutParams(DensityUtils.dp2px(context, 150.0f), -2));
        float dp2px = (float) DensityUtils.dp2px(context, 8.0f);
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{dp2px, dp2px, dp2px, dp2px, dp2px, dp2px, dp2px, dp2px}, (RectF) null, (float[]) null));
        shapeDrawable.getPaint().setColor(-16777216);
        viewGroup.findViewById(R.id.bg).setBackground(shapeDrawable);
        iCallBack.onCallBack(0, (TextView) viewGroup.findViewById(R.id.title));
        AlertDialog create = builder.create();
        create.setCanceledOnTouchOutside(false);
        create.setView(viewGroup, 0, 0, 0, 0);
        return create;
    }
}
