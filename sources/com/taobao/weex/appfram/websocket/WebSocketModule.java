package com.taobao.weex.appfram.websocket;

import android.os.Looper;
import com.alibaba.fastjson.JSON;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.appfram.websocket.IWebSocketAdapter;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.utils.WXLogUtils;
import java.util.HashMap;

public class WebSocketModule extends WXSDKEngine.DestroyableModule {
    private static final String KEY_CODE = "code";
    private static final String KEY_DATA = "data";
    private static final String KEY_REASON = "reason";
    private static final String KEY_WAS_CLEAN = "wasClean";
    private static final String TAG = "WebSocketModule";
    /* access modifiers changed from: private */
    public WebSocketEventListener eventListener;
    /* access modifiers changed from: private */
    public IWebSocketAdapter webSocketAdapter;

    public WebSocketModule() {
        WXLogUtils.e(TAG, "create new instance");
    }

    @JSMethod(uiThread = false)
    public void WebSocket(String str, String str2) {
        if (this.webSocketAdapter != null) {
            WXLogUtils.w(TAG, AbsoluteConst.EVENTS_CLOSE);
            this.webSocketAdapter.close(WebSocketCloseCodes.CLOSE_GOING_AWAY.getCode(), WebSocketCloseCodes.CLOSE_GOING_AWAY.name());
        }
        this.webSocketAdapter = this.mWXSDKInstance.getWXWebSocketAdapter();
        if (!reportErrorIfNoAdapter()) {
            WebSocketEventListener webSocketEventListener = new WebSocketEventListener();
            this.eventListener = webSocketEventListener;
            this.webSocketAdapter.connect(str, str2, webSocketEventListener);
        }
    }

    @JSMethod(uiThread = false)
    public void WebSocket(String str, String str2, String str3) {
        if (this.webSocketAdapter != null) {
            WXLogUtils.w(TAG, AbsoluteConst.EVENTS_CLOSE);
            this.webSocketAdapter.close(WebSocketCloseCodes.CLOSE_GOING_AWAY.getCode(), WebSocketCloseCodes.CLOSE_GOING_AWAY.name());
        }
        this.webSocketAdapter = this.mWXSDKInstance.getWXWebSocketAdapter();
        if (!reportErrorIfNoAdapter()) {
            WebSocketEventListener webSocketEventListener = new WebSocketEventListener();
            this.eventListener = webSocketEventListener;
            this.webSocketAdapter.connect(str, str2, str3, webSocketEventListener);
        }
    }

    @JSMethod(uiThread = false)
    public void send(String str) {
        if (!reportErrorIfNoAdapter()) {
            this.webSocketAdapter.send(str);
        }
    }

    @JSMethod(uiThread = false)
    public void close(String str, String str2) {
        if (!reportErrorIfNoAdapter()) {
            int code = WebSocketCloseCodes.CLOSE_NORMAL.getCode();
            if (str != null) {
                try {
                    code = Integer.parseInt(str);
                } catch (NumberFormatException unused) {
                }
            }
            this.webSocketAdapter.close(code, str2);
        }
    }

    @JSMethod(uiThread = false)
    public void onopen(JSCallback jSCallback) {
        WebSocketEventListener webSocketEventListener = this.eventListener;
        if (webSocketEventListener != null) {
            JSCallback unused = webSocketEventListener.onOpen = jSCallback;
        }
    }

    @JSMethod(uiThread = false)
    public void onmessage(JSCallback jSCallback) {
        WebSocketEventListener webSocketEventListener = this.eventListener;
        if (webSocketEventListener != null) {
            JSCallback unused = webSocketEventListener.onMessage = jSCallback;
        }
    }

    @JSMethod(uiThread = false)
    public void onclose(JSCallback jSCallback) {
        WebSocketEventListener webSocketEventListener = this.eventListener;
        if (webSocketEventListener != null) {
            JSCallback unused = webSocketEventListener.onClose = jSCallback;
        }
    }

    @JSMethod(uiThread = false)
    public void onerror(JSCallback jSCallback) {
        WebSocketEventListener webSocketEventListener = this.eventListener;
        if (webSocketEventListener != null) {
            JSCallback unused = webSocketEventListener.onError = jSCallback;
        }
    }

    public void destroy() {
        AnonymousClass1 r0 = new Runnable() {
            public void run() {
                WXLogUtils.w(WebSocketModule.TAG, "close session with instance id " + WebSocketModule.this.mWXSDKInstance.getInstanceId());
                if (WebSocketModule.this.webSocketAdapter != null) {
                    WebSocketModule.this.webSocketAdapter.destroy();
                }
                IWebSocketAdapter unused = WebSocketModule.this.webSocketAdapter = null;
                WebSocketEventListener unused2 = WebSocketModule.this.eventListener = null;
            }
        };
        if (Looper.myLooper() == Looper.getMainLooper()) {
            WXBridgeManager.getInstance().post(r0);
        } else {
            r0.run();
        }
    }

    private boolean reportErrorIfNoAdapter() {
        if (this.webSocketAdapter != null) {
            return false;
        }
        WebSocketEventListener webSocketEventListener = this.eventListener;
        if (webSocketEventListener != null) {
            webSocketEventListener.onError("No implementation found for IWebSocketAdapter");
        }
        WXLogUtils.e(TAG, "No implementation found for IWebSocketAdapter");
        return true;
    }

    private class WebSocketEventListener implements IWebSocketAdapter.EventListener {
        /* access modifiers changed from: private */
        public JSCallback onClose;
        /* access modifiers changed from: private */
        public JSCallback onError;
        /* access modifiers changed from: private */
        public JSCallback onMessage;
        /* access modifiers changed from: private */
        public JSCallback onOpen;

        private WebSocketEventListener() {
        }

        public void onOpen() {
            JSCallback jSCallback = this.onOpen;
            if (jSCallback != null) {
                jSCallback.invoke(new HashMap(0));
            }
        }

        public void onMessage(String str) {
            if (this.onMessage != null) {
                HashMap hashMap = new HashMap(1);
                try {
                    hashMap.put("data", JSON.parseObject(str));
                } catch (Exception unused) {
                    hashMap.put("data", str);
                }
                this.onMessage.invokeAndKeepAlive(hashMap);
            }
        }

        public void onClose(int i, String str, boolean z) {
            if (this.onClose != null) {
                HashMap hashMap = new HashMap(3);
                hashMap.put("code", Integer.valueOf(i));
                hashMap.put(WebSocketModule.KEY_REASON, str);
                hashMap.put(WebSocketModule.KEY_WAS_CLEAN, Boolean.valueOf(z));
                this.onClose.invoke(hashMap);
            }
        }

        public void onError(String str) {
            if (this.onError != null) {
                HashMap hashMap = new HashMap(1);
                hashMap.put("data", str);
                this.onError.invokeAndKeepAlive(hashMap);
            }
        }
    }
}
