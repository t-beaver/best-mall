package com.alibaba.android.bindingx.plugin.weex;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.ui.IExternalModuleGetter;

public class WXBindingXModuleService extends Service implements IExternalModuleGetter {
    public IBinder onBind(Intent intent) {
        return null;
    }

    public Class<? extends WXModule> getExternalModuleClass(String str, Context context) {
        if ("bindingx".equals(str) || "binding".equals(str)) {
            return WXBindingXModule.class;
        }
        if ("expressionBinding".equals(str)) {
            return WXExpressionBindingModule.class;
        }
        return null;
    }
}
