package io.src.dcloud.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.RuningAcitvityUtil;
import java.io.File;

public class DCloudBaseActivity extends FragmentActivity implements IReflectAble {
    public static String loadDexDirectInfo;
    /* access modifiers changed from: private */
    public int loadingSecond = 0;
    /* access modifiers changed from: private */
    public AlertDialog mDebugDialog;
    private DebugSocketStatusReceiver mDebugSocketStatusReceiver;
    /* access modifiers changed from: private */
    public Dialog mLoadingPD;
    private SocketCheckReceiver mSocketCheckReceiver;
    /* access modifiers changed from: private */
    public String preDebuggingInfoForWeexDebugging2;
    public Activity that = this;
    private WeexDebugStartReceiver weexDebugStartReceiver;

    public class DebugSocketStatusReceiver extends BroadcastReceiver {
        public DebugSocketStatusReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            DCloudBaseActivity.this.debugSocketAlert(intent.getStringExtra(AbsoluteConst.WEEX_DEBUG_CONNECT_BROADCAST_KEY), true);
        }
    }

    public class SocketCheckReceiver extends BroadcastReceiver {
        public SocketCheckReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String stringExtra = intent.getStringExtra(AbsoluteConst.WEEX_DEBUG_PING_IP_KEY);
            if (DCloudBaseActivity.this.mLoadingPD != null) {
                int unused = DCloudBaseActivity.this.loadingSecond = 0;
                ((TextView) DCloudBaseActivity.this.mLoadingPD.findViewById(R.id.debugTV)).setText(context.getString(R.string.dcloud_debug_connecting) + "\n(" + stringExtra + Operators.BRACKET_END_STR);
            }
        }
    }

    public class WeexDebugStartReceiver extends BroadcastReceiver {
        public WeexDebugStartReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String unused = DCloudBaseActivity.this.preDebuggingInfoForWeexDebugging2 = intent.getStringExtra("debugging_info");
            DCloudBaseActivity.this.startDebug();
        }
    }

    class a implements Runnable {
        a() {
        }

        public void run() {
            String str;
            boolean z;
            Object invokeMethod = PlatformUtil.invokeMethod("io.dcloud.feature.weex.WeexDevtoolImpl", "getDebugSocketStatus", (Object) null, (Class[]) null, (Object[]) null);
            while (true) {
                str = (String) invokeMethod;
                if (str != null) {
                    z = false;
                    break;
                } else if (DCloudBaseActivity.this.loadingSecond >= 6) {
                    z = true;
                    break;
                } else {
                    SystemClock.sleep(1000);
                    DCloudBaseActivity.access$208(DCloudBaseActivity.this);
                    invokeMethod = PlatformUtil.invokeMethod("io.dcloud.feature.weex.WeexDevtoolImpl", "getDebugSocketStatus", (Object) null, (Class[]) null, (Object[]) null);
                }
            }
            DCloudBaseActivity.this.debugSocketAlert(str, false, z);
        }
    }

    class b implements Runnable {
        final /* synthetic */ String a;

        class a implements DialogInterface.OnClickListener {
            a() {
            }

            public void onClick(DialogInterface dialogInterface, int i) {
                String appName = RuningAcitvityUtil.getAppName(DCloudBaseActivity.this);
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "Android/data/" + appName + "/apps/" + BaseInfo.sDefaultBootApp + "/www/__nvue_debug__");
                if (file.exists()) {
                    file.delete();
                }
                DCloudBaseActivity.this.startActivity(Intent.makeRestartActivityTask(DCloudBaseActivity.this.getPackageManager().getLaunchIntentForPackage(DCloudBaseActivity.this.getPackageName()).getComponent()));
                Runtime.getRuntime().exit(0);
            }
        }

        /* renamed from: io.src.dcloud.adapter.DCloudBaseActivity$b$b  reason: collision with other inner class name */
        class C0080b implements DialogInterface.OnClickListener {
            C0080b() {
            }

            public void onClick(DialogInterface dialogInterface, int i) {
                int access$800 = DCloudBaseActivity.this.getDebuggingVersion();
                if (access$800 == 1) {
                    DCloudBaseActivity.this.startActivity(Intent.makeRestartActivityTask(DCloudBaseActivity.this.getPackageManager().getLaunchIntentForPackage(DCloudBaseActivity.this.getPackageName()).getComponent()));
                    Runtime.getRuntime().exit(0);
                } else if (access$800 == 2 && !PdrUtil.isEmpty(DCloudBaseActivity.this.preDebuggingInfoForWeexDebugging2)) {
                    DCloudBaseActivity.this.stopDebug();
                    Intent intent = new Intent(AbsoluteConst.WEEX_DEBUG_START_WAITING_CONNECT);
                    intent.putExtra("debugging_info", DCloudBaseActivity.this.preDebuggingInfoForWeexDebugging2);
                    LocalBroadcastManager.getInstance(DCloudBaseActivity.this.getApplication()).sendBroadcast(intent);
                    PlatformUtil.invokeMethod("io.dcloud.feature.weex.WeexDevtoolImpl", "initDebugEnvironment", (Object) null, new Class[]{Application.class, String.class}, new Object[]{DCloudBaseActivity.this.getApplication(), DCloudBaseActivity.this.preDebuggingInfoForWeexDebugging2});
                }
            }
        }

        b(String str) {
            this.a = str;
        }

        public void run() {
            DCloudBaseActivity.this.dismissDebugLoading();
            if (DCloudBaseActivity.this.mDebugDialog == null) {
                AlertDialog unused = DCloudBaseActivity.this.mDebugDialog = new AlertDialog.Builder(DCloudBaseActivity.this).setTitle("").setMessage(this.a).setPositiveButton(R.string.dcloud_debug_reconnection_service, new C0080b()).setNegativeButton(R.string.dcloud_common_cancel, new a()).setCancelable(false).show();
            } else if (!DCloudBaseActivity.this.mDebugDialog.isShowing()) {
                DCloudBaseActivity.this.mDebugDialog.setMessage(this.a);
                DCloudBaseActivity.this.mDebugDialog.show();
            }
        }
    }

    static /* synthetic */ int access$208(DCloudBaseActivity dCloudBaseActivity) {
        int i = dCloudBaseActivity.loadingSecond;
        dCloudBaseActivity.loadingSecond = i + 1;
        return i;
    }

    /* access modifiers changed from: private */
    public void debugSocketAlert(String str, boolean z, boolean z2) {
        if (z2) {
            dismissDebugLoading();
        }
        debugSocketAlert(str, z);
    }

    /* access modifiers changed from: private */
    public void dismissDebugLoading() {
        Dialog dialog = this.mLoadingPD;
        if (dialog != null) {
            dialog.dismiss();
            this.mLoadingPD = null;
        }
    }

    /* access modifiers changed from: private */
    public int getDebuggingVersion() {
        return isWeexDebuggingInVersion1() ? 1 : 2;
    }

    private boolean isWeexDebuggingInVersion1() {
        String str;
        if (Build.VERSION.SDK_INT <= 28) {
            str = Environment.getExternalStorageDirectory() + File.separator + "Android/data/" + RuningAcitvityUtil.getAppName(this);
        } else {
            str = getExternalFilesDir((String) null).getParentFile().getPath();
        }
        return new File(str + "/apps/" + BaseInfo.sDefaultBootApp + "/www/__nvue_debug__").exists();
    }

    private void showDebugLoading() {
        Dialog dialog = new Dialog(this);
        this.mLoadingPD = dialog;
        dialog.getWindow().setGravity(17);
        this.mLoadingPD.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.mLoadingPD.getWindow().setBackgroundDrawableResource(17170445);
        this.mLoadingPD.setCancelable(false);
        this.mLoadingPD.show();
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.dcloud_weex_debug_progress, (ViewGroup) null);
        TextView textView = (TextView) viewGroup.findViewById(R.id.debugTV);
        String str = (String) PlatformUtil.invokeMethod("io.dcloud.feature.weex.WeexDevtoolImpl", "getCurrentPingIP", (Object) null, (Class[]) null, (Object[]) null);
        if (str != null) {
            textView.setText(getString(R.string.dcloud_debug_connecting) + "\n(" + str + Operators.BRACKET_END_STR);
        }
        this.mLoadingPD.setContentView(viewGroup);
        new Thread(new a()).start();
    }

    /* access modifiers changed from: private */
    public void startDebug() {
        if (BaseInfo.SyncDebug) {
            this.mDebugSocketStatusReceiver = new DebugSocketStatusReceiver();
            LocalBroadcastManager.getInstance(this).registerReceiver(this.mDebugSocketStatusReceiver, new IntentFilter(AbsoluteConst.WEEX_DEBUG_CONNECT_BROADCAST));
            this.mSocketCheckReceiver = new SocketCheckReceiver();
            LocalBroadcastManager.getInstance(this).registerReceiver(this.mSocketCheckReceiver, new IntentFilter(AbsoluteConst.WEEX_DEBUG_PING_BROADCAST));
            showDebugLoading();
        }
    }

    /* access modifiers changed from: private */
    public void stopDebug() {
        if (BaseInfo.SyncDebug && this.mDebugSocketStatusReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mDebugSocketStatusReceiver);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mSocketCheckReceiver);
            this.mDebugSocketStatusReceiver = null;
            this.mSocketCheckReceiver = null;
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (BaseInfo.SyncDebug) {
            this.weexDebugStartReceiver = new WeexDebugStartReceiver();
            LocalBroadcastManager.getInstance(this).registerReceiver(this.weexDebugStartReceiver, new IntentFilter(AbsoluteConst.WEEX_DEBUG_START_WAITING_CONNECT));
            loadDexDirectInfo = getIntent().getStringExtra("load_dex_direct_info");
            if (isWeexDebuggingInVersion1()) {
                startDebug();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.mDebugSocketStatusReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mDebugSocketStatusReceiver);
        }
        if (this.mSocketCheckReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mSocketCheckReceiver);
        }
        if (this.weexDebugStartReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(this.weexDebugStartReceiver);
        }
    }

    /* access modifiers changed from: protected */
    public final void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onNewIntentImpl(intent);
    }

    public void onNewIntentImpl(Intent intent) {
    }

    /* access modifiers changed from: private */
    public void debugSocketAlert(String str, boolean z) {
        String str2;
        if (AbsoluteConst.WEEX_DEBUG_CONNECT_SUCCESS.equalsIgnoreCase(str)) {
            dismissDebugLoading();
            AlertDialog alertDialog = this.mDebugDialog;
            if (alertDialog != null) {
                alertDialog.dismiss();
                return;
            }
            return;
        }
        if (z) {
            str2 = getString(R.string.dcloud_debug_break_off_reason) + "\n";
        } else {
            str2 = getString(R.string.dcloud_debug_cannot_connect) + "\n";
        }
        new Handler(Looper.getMainLooper()).post(new b(str2 + getString(R.string.dcloud_debug_possible_causes)));
    }
}
