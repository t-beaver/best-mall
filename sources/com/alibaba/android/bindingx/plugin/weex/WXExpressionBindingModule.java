package com.alibaba.android.bindingx.plugin.weex;

import android.content.Context;
import com.alibaba.android.bindingx.core.BindingXCore;
import com.alibaba.android.bindingx.core.IEventHandler;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.alibaba.android.bindingx.core.internal.ExpressionPair;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import java.util.List;
import java.util.Map;

@Deprecated
public final class WXExpressionBindingModule extends WXSDKEngine.DestroyableModule {
    private BindingXCore mExpressionBindingCore;
    private PlatformManager mPlatformManager;

    @JSMethod
    @Deprecated
    public void enableBinding(String str, String str2) {
        if (this.mPlatformManager == null) {
            this.mPlatformManager = WXBindingXModule.createPlatformManager(this.mWXSDKInstance);
        }
        if (this.mExpressionBindingCore == null) {
            BindingXCore bindingXCore = new BindingXCore(this.mPlatformManager);
            this.mExpressionBindingCore = bindingXCore;
            bindingXCore.registerEventHandler("scroll", new BindingXCore.ObjectCreator<IEventHandler, Context, PlatformManager>() {
                public IEventHandler createWith(Context context, PlatformManager platformManager, Object... objArr) {
                    return new BindingXScrollHandler(context, platformManager, objArr);
                }
            });
        }
    }

    @JSMethod
    @Deprecated
    public void createBinding(String str, String str2, String str3, List<Map<String, Object>> list, JSCallback jSCallback) {
        String str4 = null;
        enableBinding((String) null, (String) null);
        ExpressionPair create = ExpressionPair.create((String) null, str3);
        BindingXCore bindingXCore = this.mExpressionBindingCore;
        final JSCallback jSCallback2 = jSCallback;
        AnonymousClass2 r10 = new BindingXCore.JavaScriptCallback() {
            public void callback(Object obj) {
                JSCallback jSCallback = jSCallback2;
                if (jSCallback != null) {
                    jSCallback.invokeAndKeepAlive(obj);
                }
            }
        };
        Context context = this.mWXSDKInstance == null ? null : this.mWXSDKInstance.getContext();
        if (this.mWXSDKInstance != null) {
            str4 = this.mWXSDKInstance.getInstanceId();
        }
        bindingXCore.doBind(str, (String) null, str2, (Map<String, Object>) null, create, list, (Map<String, ExpressionPair>) null, r10, context, str4, new Object[0]);
    }

    @JSMethod
    @Deprecated
    public void disableBinding(String str, String str2) {
        BindingXCore bindingXCore = this.mExpressionBindingCore;
        if (bindingXCore != null) {
            bindingXCore.doUnbind(str, str2);
        }
    }

    @JSMethod
    @Deprecated
    public void disableAll() {
        BindingXCore bindingXCore = this.mExpressionBindingCore;
        if (bindingXCore != null) {
            bindingXCore.doRelease();
        }
    }

    public void destroy() {
        BindingXCore bindingXCore = this.mExpressionBindingCore;
        if (bindingXCore != null) {
            bindingXCore.doRelease();
            this.mExpressionBindingCore = null;
        }
    }

    public void onActivityPause() {
        BindingXCore bindingXCore = this.mExpressionBindingCore;
        if (bindingXCore != null) {
            bindingXCore.onActivityPause();
        }
    }

    public void onActivityResume() {
        BindingXCore bindingXCore = this.mExpressionBindingCore;
        if (bindingXCore != null) {
            bindingXCore.onActivityResume();
        }
    }
}
