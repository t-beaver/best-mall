package io.dcloud.feature.weex_websocket;

import android.os.Looper;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.appfram.websocket.IWebSocketAdapter;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.ui.component.WXBasicComponentType;
import com.taobao.weex.utils.WXLogUtils;
import java.util.HashMap;
import java.util.Map;

public class UniWebSocketModule extends WXSDKEngine.DestroyableModule {
    private static final String KEY_CODE = "code";
    private static final String KEY_DATA = "data";
    private static final String KEY_ID = "id";
    private static final String KEY_REASON = "reason";
    private static final String KEY_WAS_CLEAN = "wasClean";
    private static final String TAG = "WebSocketModule";
    private JSCallback callback_onclose;
    private JSCallback callback_onerror;
    private JSCallback callback_onmessage;
    private JSCallback callback_onopen;
    private String currentId;
    /* access modifiers changed from: private */
    public Map<String, IWebSocketAdapter> webSocketAdapterMap;
    /* access modifiers changed from: private */
    public Map<String, WebSocketEventListener> webSocketEventListenerMap;

    public UniWebSocketModule() {
        WXLogUtils.e(TAG, "create new instance");
    }

    @JSMethod(uiThread = false)
    public void WebSocket(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject parseObject = JSON.parseObject(new String(str.getBytes(), "utf-8"));
                String string = parseObject.getString("id");
                String string2 = parseObject.getString("url");
                String string3 = parseObject.getString("protocol");
                String string4 = parseObject.getString(WXBasicComponentType.HEADER);
                if (this.webSocketAdapterMap == null) {
                    this.webSocketAdapterMap = new HashMap();
                    this.webSocketEventListenerMap = new HashMap();
                }
                if (this.webSocketAdapterMap.get(string) == null || this.webSocketEventListenerMap.get(string) == null) {
                    if (this.webSocketAdapterMap.get(string) != null && this.webSocketEventListenerMap.get(string) == null) {
                        this.webSocketAdapterMap.remove(string);
                    }
                    IWebSocketAdapter wXWebSocketAdapter = this.mWXSDKInstance.getWXWebSocketAdapter();
                    WebSocketEventListener webSocketEventListener = new WebSocketEventListener(string);
                    JSCallback unused = webSocketEventListener.onOpen = this.callback_onopen;
                    JSCallback unused2 = webSocketEventListener.onMessage = this.callback_onmessage;
                    JSCallback unused3 = webSocketEventListener.onClose = this.callback_onclose;
                    JSCallback unused4 = webSocketEventListener.onError = this.callback_onerror;
                    wXWebSocketAdapter.connect(string2, string3, string4, webSocketEventListener);
                    this.webSocketEventListenerMap.put(string, webSocketEventListener);
                    this.currentId = string;
                    this.webSocketAdapterMap.put(string, wXWebSocketAdapter);
                }
            } catch (Exception e) {
                WXLogUtils.e("[UniWebSocketModule] alert param parse error ", (Throwable) e);
            }
        }
    }

    @JSMethod(uiThread = false)
    public void send(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject parseObject = JSON.parseObject(new String(str.getBytes(), "utf-8"));
                String string = parseObject.getString("id");
                this.webSocketAdapterMap.get(string).send(parseObject.getString("data"));
            } catch (Exception e) {
                WXLogUtils.e("[UniWebSocketModule] alert param parse error ", (Throwable) e);
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:2|3|(2:5|6)|7|8|13) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0033 */
    @com.taobao.weex.annotation.JSMethod(uiThread = false)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void close(java.lang.String r4) {
        /*
            r3 = this;
            boolean r0 = android.text.TextUtils.isEmpty(r4)
            if (r0 != 0) goto L_0x0045
            java.lang.String r0 = new java.lang.String     // Catch:{ Exception -> 0x003f }
            byte[] r4 = r4.getBytes()     // Catch:{ Exception -> 0x003f }
            java.lang.String r1 = "utf-8"
            r0.<init>(r4, r1)     // Catch:{ Exception -> 0x003f }
            com.alibaba.fastjson.JSONObject r4 = com.alibaba.fastjson.JSON.parseObject(r0)     // Catch:{ Exception -> 0x003f }
            java.lang.String r0 = "id"
            java.lang.String r0 = r4.getString(r0)     // Catch:{ Exception -> 0x003f }
            java.lang.String r1 = "code"
            java.lang.String r1 = r4.getString(r1)     // Catch:{ Exception -> 0x003f }
            java.lang.String r2 = "reason"
            java.lang.String r4 = r4.getString(r2)     // Catch:{ Exception -> 0x003f }
            com.taobao.weex.appfram.websocket.WebSocketCloseCodes r2 = com.taobao.weex.appfram.websocket.WebSocketCloseCodes.CLOSE_NORMAL     // Catch:{ Exception -> 0x003f }
            int r2 = r2.getCode()     // Catch:{ Exception -> 0x003f }
            if (r1 == 0) goto L_0x0033
            int r2 = java.lang.Integer.parseInt(r1)     // Catch:{ NumberFormatException -> 0x0033 }
        L_0x0033:
            java.util.Map<java.lang.String, com.taobao.weex.appfram.websocket.IWebSocketAdapter> r1 = r3.webSocketAdapterMap     // Catch:{ Exception -> 0x003f }
            java.lang.Object r0 = r1.get(r0)     // Catch:{ Exception -> 0x003f }
            com.taobao.weex.appfram.websocket.IWebSocketAdapter r0 = (com.taobao.weex.appfram.websocket.IWebSocketAdapter) r0     // Catch:{ Exception -> 0x003f }
            r0.close(r2, r4)     // Catch:{ Exception -> 0x003f }
            goto L_0x0045
        L_0x003f:
            r4 = move-exception
            java.lang.String r0 = "[UniWebSocketModule] alert param parse error "
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r0, (java.lang.Throwable) r4)
        L_0x0045:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.weex_websocket.UniWebSocketModule.close(java.lang.String):void");
    }

    @JSMethod(uiThread = false)
    public void onopen(JSCallback jSCallback) {
        this.callback_onopen = jSCallback;
        Map<String, WebSocketEventListener> map = this.webSocketEventListenerMap;
        if (map != null) {
            for (Map.Entry<String, WebSocketEventListener> value : map.entrySet()) {
                JSCallback unused = ((WebSocketEventListener) value.getValue()).onOpen = this.callback_onopen;
            }
        }
    }

    @JSMethod(uiThread = false)
    public void onmessage(JSCallback jSCallback) {
        this.callback_onmessage = jSCallback;
        Map<String, WebSocketEventListener> map = this.webSocketEventListenerMap;
        if (map != null) {
            for (Map.Entry<String, WebSocketEventListener> value : map.entrySet()) {
                JSCallback unused = ((WebSocketEventListener) value.getValue()).onMessage = this.callback_onmessage;
            }
        }
    }

    @JSMethod(uiThread = false)
    public void onclose(JSCallback jSCallback) {
        this.callback_onclose = jSCallback;
        Map<String, WebSocketEventListener> map = this.webSocketEventListenerMap;
        if (map != null) {
            for (Map.Entry<String, WebSocketEventListener> value : map.entrySet()) {
                JSCallback unused = ((WebSocketEventListener) value.getValue()).onClose = this.callback_onclose;
            }
        }
    }

    @JSMethod(uiThread = false)
    public void onerror(JSCallback jSCallback) {
        this.callback_onerror = jSCallback;
        Map<String, WebSocketEventListener> map = this.webSocketEventListenerMap;
        if (map != null) {
            for (Map.Entry<String, WebSocketEventListener> value : map.entrySet()) {
                JSCallback unused = ((WebSocketEventListener) value.getValue()).onError = this.callback_onerror;
            }
        }
    }

    public void destroy() {
        AnonymousClass1 r0 = new Runnable() {
            public void run() {
                WXLogUtils.w(UniWebSocketModule.TAG, "close session with instance id " + UniWebSocketModule.this.mWXSDKInstance.getInstanceId());
                if (UniWebSocketModule.this.webSocketAdapterMap != null) {
                    for (Map.Entry value : UniWebSocketModule.this.webSocketAdapterMap.entrySet()) {
                        ((IWebSocketAdapter) value.getValue()).destroy();
                    }
                    UniWebSocketModule.this.webSocketAdapterMap.clear();
                    Map unused = UniWebSocketModule.this.webSocketAdapterMap = null;
                }
                if (UniWebSocketModule.this.webSocketEventListenerMap != null) {
                    UniWebSocketModule.this.webSocketEventListenerMap.clear();
                }
                Map unused2 = UniWebSocketModule.this.webSocketEventListenerMap = null;
            }
        };
        if (Looper.myLooper() == Looper.getMainLooper()) {
            WXBridgeManager.getInstance().post(r0);
        } else {
            r0.run();
        }
    }

    private class WebSocketEventListener implements IWebSocketAdapter.EventListener {
        private String id;
        /* access modifiers changed from: private */
        public JSCallback onClose;
        /* access modifiers changed from: private */
        public JSCallback onError;
        /* access modifiers changed from: private */
        public JSCallback onMessage;
        /* access modifiers changed from: private */
        public JSCallback onOpen;

        public WebSocketEventListener(String str) {
            this.id = str;
        }

        public void onOpen() {
            if (this.onOpen != null) {
                HashMap hashMap = new HashMap();
                hashMap.put("id", this.id);
                this.onOpen.invokeAndKeepAlive(hashMap);
            }
        }

        public void onMessage(String str) {
            if (this.onMessage != null) {
                HashMap hashMap = new HashMap(2);
                hashMap.put("id", this.id);
                try {
                    JSONObject parseObject = JSON.parseObject(str);
                    if (!parseObject.containsKey("@type") || !parseObject.containsKey("base64")) {
                        hashMap.put("data", str);
                    } else {
                        hashMap.put("data", parseObject);
                    }
                } catch (Exception unused) {
                    hashMap.put("data", str);
                }
                this.onMessage.invokeAndKeepAlive(hashMap);
            }
        }

        public void onClose(int i, String str, boolean z) {
            if (this.onClose != null) {
                HashMap hashMap = new HashMap(4);
                hashMap.put("id", this.id);
                hashMap.put("code", Integer.valueOf(i));
                hashMap.put(UniWebSocketModule.KEY_REASON, str);
                hashMap.put(UniWebSocketModule.KEY_WAS_CLEAN, Boolean.valueOf(z));
                this.onClose.invokeAndKeepAlive(hashMap);
                if (UniWebSocketModule.this.webSocketEventListenerMap != null) {
                    UniWebSocketModule.this.webSocketEventListenerMap.remove(this.id);
                }
            }
        }

        public void onError(String str) {
            if (this.onError != null) {
                HashMap hashMap = new HashMap(2);
                hashMap.put("id", this.id);
                hashMap.put("data", str);
                this.onError.invokeAndKeepAlive(hashMap);
                if (UniWebSocketModule.this.webSocketEventListenerMap != null) {
                    UniWebSocketModule.this.webSocketEventListenerMap.remove(this.id);
                }
            }
        }
    }
}
