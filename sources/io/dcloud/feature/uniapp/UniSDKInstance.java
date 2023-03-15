package io.dcloud.feature.uniapp;

import android.content.Context;
import com.taobao.weex.WXSDKInstance;

public class UniSDKInstance extends WXSDKInstance {
    private boolean isCompilerWithUniapp = true;

    public UniSDKInstance(Context context) {
        super(context);
    }

    public UniSDKInstance(Context context, Context context2) {
        super(context, context2);
    }

    public UniSDKInstance() {
    }

    public UniSDKInstance(Context context, String str) {
        super(context, str);
    }

    /* access modifiers changed from: protected */
    public UniSDKInstance newNestedInstance() {
        return new UniSDKInstance(this.mContext);
    }

    public void setCompilerWithUniapp(boolean z) {
        this.isCompilerWithUniapp = z;
    }

    public boolean isCompilerWithUniapp() {
        return this.isCompilerWithUniapp;
    }
}
