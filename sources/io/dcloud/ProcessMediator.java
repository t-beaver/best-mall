package io.dcloud;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.util.PdrUtil;

public class ProcessMediator extends Activity implements IReflectAble {
    public static final int CODE_REQUEST = 1000;
    public static final int CODE_RESULT = 1001;
    public static final String LOGIC_CLASS = "__class__";
    public static final String PROCESS_ACTIVITY_SOURCE = "process_activity_source";
    public static final String REQ_DATA = "req";
    public static final String RESULT_ACTION = "mediator_process_result_action";
    public static final String RESULT_DATA = "result";
    public static final String STYLE_DATA = "style";
    public static boolean isMultiProcess = false;
    boolean a = false;
    private String b;
    String c = "";
    BroadcastReceiver d = new a();

    public interface Logic extends IReflectAble {
        void exec(Context context, Intent intent);
    }

    class a extends BroadcastReceiver {
        a() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ProcessMediator.this.c)) {
                ProcessMediator.this.onResult(intent);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        String stringExtra = getIntent().getStringExtra(LOGIC_CLASS);
        if (TextUtils.isEmpty(stringExtra)) {
            finish();
            return;
        }
        isMultiProcess = true;
        String stringExtra2 = getIntent().getStringExtra("transaction");
        if (getIntent().hasExtra(PROCESS_ACTIVITY_SOURCE)) {
            this.b = getIntent().getStringExtra(PROCESS_ACTIVITY_SOURCE);
        }
        IntentFilter intentFilter = new IntentFilter();
        boolean isEmpty = TextUtils.isEmpty(stringExtra2);
        String str = RESULT_ACTION;
        if (!isEmpty) {
            str = str + stringExtra2;
        }
        this.c = str;
        intentFilter.addAction(str);
        LocalBroadcastManager.getInstance(this).registerReceiver(this.d, intentFilter);
        try {
            Object newInstance = Class.forName(stringExtra).newInstance();
            if (newInstance instanceof Logic) {
                ((Logic) newInstance).exec(this, getIntent());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.b = null;
        isMultiProcess = false;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.d);
    }

    public void onResult(Intent intent) {
        setResult(1001, intent);
        finishActivity(1000);
        finish();
        if (!PdrUtil.isEmpty(this.b)) {
            Intent intent2 = new Intent();
            intent2.setClassName(getPackageName(), this.b);
            startActivity(intent2);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }
}
