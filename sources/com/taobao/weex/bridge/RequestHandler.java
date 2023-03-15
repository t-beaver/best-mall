package com.taobao.weex.bridge;

import android.net.Uri;
import android.text.TextUtils;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXHttpListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXRequest;
import com.taobao.weex.common.WXResponse;
import com.taobao.weex.http.WXHttpUtil;
import com.taobao.weex.utils.WXExceptionUtils;
import com.taobao.weex.utils.WXLogUtils;
import io.dcloud.feature.uniapp.adapter.AbsURIAdapter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RequestHandler {
    /* access modifiers changed from: package-private */
    public native void nativeInvokeOnFailed(long j);

    /* access modifiers changed from: package-private */
    public native void nativeInvokeOnSuccess(long j, String str, String str2);

    public static RequestHandler create() {
        return new RequestHandler();
    }

    public void send(String str, String str2, long j) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && j != 0 && WXSDKManager.getInstance().getAllInstanceMap().containsKey(str)) {
            WXSDKManager instance = WXSDKManager.getInstance();
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance != null) {
                IWXHttpAdapter iWXHttpAdapter = WXSDKManager.getInstance().getIWXHttpAdapter();
                WXRequest wXRequest = new WXRequest();
                wXRequest.url = instance.getURIAdapter().rewrite(sDKInstance, AbsURIAdapter.BUNDLE, Uri.parse(str2)).toString();
                if (wXRequest.paramMap == null) {
                    wXRequest.paramMap = new HashMap();
                }
                wXRequest.paramMap.put(WXHttpUtil.KEY_USER_AGENT, WXHttpUtil.assembleUserAgent(sDKInstance.getContext(), WXEnvironment.getConfig()));
                wXRequest.paramMap.put("isBundleRequest", AbsoluteConst.TRUE);
                WXLogUtils.i("Eagle", String.format(Locale.ENGLISH, "Weex eagle is going to download script from %s", new Object[]{str2}));
                iWXHttpAdapter.sendRequest(wXRequest, new OnHttpListenerInner(sDKInstance, j, str2));
            }
        }
    }

    public void getBundleType(String str, String str2, long j) {
        final String str3;
        WXBridgeManager.BundType bundleType = WXBridgeManager.getInstance().getBundleType("", str2);
        if (bundleType == null) {
            str3 = "Others";
        } else {
            str3 = bundleType.toString();
        }
        WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
        if ("Others".equalsIgnoreCase(str3) && sDKInstance != null) {
            WXErrorCode wXErrorCode = WXErrorCode.WX_KEY_EXCEPTION_NO_BUNDLE_TYPE;
            WXExceptionUtils.commitCriticalExceptionRT(str, wXErrorCode, "RequestHandler.onSuccess", "eagle ->" + WXErrorCode.WX_KEY_EXCEPTION_NO_BUNDLE_TYPE.getErrorMsg(), (Map<String, String>) null);
        }
        final long j2 = j;
        final String str4 = str2;
        WXBridgeManager.getInstance().post(new Runnable() {
            public void run() {
                if (WXBridgeManager.getInstance().isJSFrameworkInit()) {
                    RequestHandler.this.nativeInvokeOnSuccess(j2, str4, str3);
                } else {
                    RequestHandler.this.nativeInvokeOnFailed(j2);
                }
            }
        });
    }

    class OnHttpListenerInner extends WXHttpListener {
        /* access modifiers changed from: private */
        public long sNativeCallback;

        OnHttpListenerInner(WXSDKInstance wXSDKInstance, long j, String str) {
            super(wXSDKInstance, str);
            this.sNativeCallback = j;
        }

        public void onSuccess(WXResponse wXResponse) {
            final String str;
            final String str2 = new String(wXResponse.originalData);
            WXBridgeManager.BundType bundleType = WXBridgeManager.getInstance().getBundleType("", str2);
            if (bundleType == null) {
                str = "Others";
            } else {
                str = bundleType.toString();
            }
            if ("Others".equalsIgnoreCase(str) && getInstance() != null) {
                String instanceId = getInstance().getInstanceId();
                WXErrorCode wXErrorCode = WXErrorCode.WX_KEY_EXCEPTION_NO_BUNDLE_TYPE;
                WXExceptionUtils.commitCriticalExceptionRT(instanceId, wXErrorCode, "RequestHandler.onSuccess", "eagle ->" + WXErrorCode.WX_KEY_EXCEPTION_NO_BUNDLE_TYPE.getErrorMsg(), (Map<String, String>) null);
            }
            WXBridgeManager.getInstance().post(new Runnable() {
                public void run() {
                    if (WXBridgeManager.getInstance().isJSFrameworkInit()) {
                        RequestHandler.this.nativeInvokeOnSuccess(OnHttpListenerInner.this.sNativeCallback, str2, str);
                    } else {
                        RequestHandler.this.nativeInvokeOnFailed(OnHttpListenerInner.this.sNativeCallback);
                    }
                }
            });
        }

        public void onFail(WXResponse wXResponse) {
            RequestHandler.this.nativeInvokeOnFailed(this.sNativeCallback);
        }
    }
}
