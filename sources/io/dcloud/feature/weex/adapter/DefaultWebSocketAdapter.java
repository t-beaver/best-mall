package io.dcloud.feature.weex.adapter;

import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.appfram.websocket.IWebSocketAdapter;
import com.taobao.weex.appfram.websocket.WebSocketCloseCodes;
import dc.squareup.okhttp3.ConnectionPool;
import dc.squareup.okhttp3.ConnectionSpec;
import dc.squareup.okhttp3.Headers;
import dc.squareup.okhttp3.OkHttpClient;
import dc.squareup.okhttp3.Request;
import dc.squareup.okhttp3.Response;
import dc.squareup.okhttp3.WebSocket;
import dc.squareup.okhttp3.WebSocketListener;
import dc.squareup.okio.ByteString;
import io.dcloud.common.adapter.util.DCloudTrustManager;
import java.io.EOFException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSocketFactory;

public class DefaultWebSocketAdapter implements IWebSocketAdapter {
    private static ConnectionPool mConnectPool;
    /* access modifiers changed from: private */
    public IWebSocketAdapter.EventListener eventListener;
    /* access modifiers changed from: private */
    public WebSocket ws;

    public void connect(String str, String str2, IWebSocketAdapter.EventListener eventListener2) {
        SSLSocketFactory sSLSocketFactory;
        this.eventListener = eventListener2;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        try {
            if (Build.VERSION.SDK_INT < 21) {
                sSLSocketFactory = DCloudTrustManager.getSSLSocketFactory("TLSv1.2");
                if (sSLSocketFactory != null) {
                    builder.connectionSpecs(Arrays.asList(new ConnectionSpec[]{ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT}));
                }
            } else {
                sSLSocketFactory = DCloudTrustManager.getSSLSocketFactory();
            }
            if (sSLSocketFactory != null) {
                builder.sslSocketFactory(sSLSocketFactory);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e2) {
            e2.printStackTrace();
        }
        builder.readTimeout(24, TimeUnit.HOURS);
        builder.writeTimeout(24, TimeUnit.HOURS);
        builder.hostnameVerifier(DCloudTrustManager.getHostnameVerifier(false));
        Request.Builder builder2 = new Request.Builder();
        if (!TextUtils.isEmpty(str2)) {
            builder2.addHeader(IWebSocketAdapter.HEADER_SEC_WEBSOCKET_PROTOCOL, str2);
        }
        builder2.addHeader("Origin", "http://localhost");
        builder2.url(str);
        builder.build().newWebSocket(builder2.build(), new WebSocketListener() {
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                WebSocket unused = DefaultWebSocketAdapter.this.ws = webSocket;
                DefaultWebSocketAdapter.this.eventListener.onOpen();
                Headers headers = response.headers();
                HashMap hashMap = new HashMap();
                for (String next : headers.names()) {
                    hashMap.put(next, headers.values(next).toString());
                }
            }

            public void onMessage(WebSocket webSocket, String str) {
                super.onMessage(webSocket, str);
                DefaultWebSocketAdapter.this.eventListener.onMessage(str);
            }

            public void onMessage(WebSocket webSocket, ByteString byteString) {
                super.onMessage(webSocket, byteString);
                String encodeToString = Base64.encodeToString(byteString.toByteArray(), 2);
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("@type", (Object) "binary");
                jSONObject.put("base64", (Object) encodeToString);
                DefaultWebSocketAdapter.this.eventListener.onMessage(jSONObject.toJSONString());
            }

            public void onClosing(WebSocket webSocket, int i, String str) {
                super.onClosing(webSocket, i, str);
                DefaultWebSocketAdapter.this.eventListener.onClose(i, str, true);
            }

            public void onClosed(WebSocket webSocket, int i, String str) {
                super.onClosed(webSocket, i, str);
                DefaultWebSocketAdapter.this.eventListener.onClose(i, str, true);
            }

            public void onFailure(WebSocket webSocket, Throwable th, Response response) {
                super.onFailure(webSocket, th, response);
                th.printStackTrace();
                if (th instanceof EOFException) {
                    DefaultWebSocketAdapter.this.eventListener.onClose(WebSocketCloseCodes.CLOSE_NORMAL.getCode(), WebSocketCloseCodes.CLOSE_NORMAL.name(), true);
                } else {
                    DefaultWebSocketAdapter.this.eventListener.onError(th.getMessage());
                }
            }
        });
    }

    public void connect(String str, String str2, String str3, IWebSocketAdapter.EventListener eventListener2) {
        Map map;
        SSLSocketFactory sSLSocketFactory;
        this.eventListener = eventListener2;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        try {
            if (Build.VERSION.SDK_INT < 21) {
                sSLSocketFactory = DCloudTrustManager.getSSLSocketFactory("TLSv1.2");
                if (sSLSocketFactory != null) {
                    builder.connectionSpecs(Arrays.asList(new ConnectionSpec[]{ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT}));
                }
            } else {
                sSLSocketFactory = DCloudTrustManager.getSSLSocketFactory();
            }
            if (sSLSocketFactory != null) {
                builder.sslSocketFactory(sSLSocketFactory);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e2) {
            e2.printStackTrace();
        }
        builder.readTimeout(24, TimeUnit.HOURS);
        builder.writeTimeout(24, TimeUnit.HOURS);
        if (mConnectPool == null) {
            mConnectPool = new ConnectionPool();
        }
        builder.connectionPool(mConnectPool);
        builder.hostnameVerifier(DCloudTrustManager.getHostnameVerifier(false));
        Request.Builder builder2 = new Request.Builder();
        if (!TextUtils.isEmpty(str2)) {
            builder2.addHeader(IWebSocketAdapter.HEADER_SEC_WEBSOCKET_PROTOCOL, str2);
        }
        builder2.addHeader("Origin", "http://localhost");
        try {
            if (!TextUtils.isEmpty(str3) && (map = (Map) JSON.parse(str3)) != null) {
                for (Map.Entry entry : map.entrySet()) {
                    String str4 = (String) entry.getKey();
                    String str5 = (String) entry.getValue();
                    if (!TextUtils.isEmpty(str4)) {
                        builder2.addHeader(str4, str5);
                    }
                }
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        builder2.url(str);
        builder.build().newWebSocket(builder2.build(), new WebSocketListener() {
            public void onClosing(WebSocket webSocket, int i, String str) {
            }

            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                WebSocket unused = DefaultWebSocketAdapter.this.ws = webSocket;
                DefaultWebSocketAdapter.this.eventListener.onOpen();
                Headers headers = response.headers();
                HashMap hashMap = new HashMap();
                for (String next : headers.names()) {
                    hashMap.put(next, headers.values(next).toString());
                }
            }

            public void onMessage(WebSocket webSocket, String str) {
                super.onMessage(webSocket, str);
                DefaultWebSocketAdapter.this.eventListener.onMessage(str);
            }

            public void onMessage(WebSocket webSocket, ByteString byteString) {
                super.onMessage(webSocket, byteString);
                String encodeToString = Base64.encodeToString(byteString.toByteArray(), 2);
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("@type", (Object) "binary");
                jSONObject.put("base64", (Object) encodeToString);
                DefaultWebSocketAdapter.this.eventListener.onMessage(jSONObject.toJSONString());
            }

            public void onClosed(WebSocket webSocket, int i, String str) {
                super.onClosed(webSocket, i, str);
                DefaultWebSocketAdapter.this.eventListener.onClose(i, str, true);
            }

            public void onFailure(WebSocket webSocket, Throwable th, Response response) {
                super.onFailure(webSocket, th, response);
                th.printStackTrace();
                if (th instanceof EOFException) {
                    DefaultWebSocketAdapter.this.eventListener.onClose(WebSocketCloseCodes.CLOSE_NORMAL.getCode(), WebSocketCloseCodes.CLOSE_NORMAL.name(), true);
                } else {
                    DefaultWebSocketAdapter.this.eventListener.onError(th.getMessage());
                }
            }
        });
    }

    public void send(String str) {
        try {
            JSONObject parseObject = JSON.parseObject(str);
            String string = parseObject.getString("@type");
            if (string != null && string.equals("binary") && parseObject.containsKey("base64")) {
                send(parseObject);
                return;
            }
        } catch (Exception unused) {
        }
        WebSocket webSocket = this.ws;
        if (webSocket != null) {
            try {
                webSocket.send(str);
            } catch (Exception e) {
                e.printStackTrace();
                reportError(e.getMessage());
            }
        } else {
            reportError("WebSocket is not ready");
        }
    }

    public void send(JSONObject jSONObject) {
        if (this.ws != null) {
            try {
                String string = jSONObject.getString("@type");
                if (string != null) {
                    if (string.equals("binary") && jSONObject.containsKey("base64")) {
                        byte[] decode = Base64.decode(jSONObject.getString("base64"), 0);
                        if (decode != null) {
                            this.ws.send(ByteString.of(decode));
                            return;
                        }
                        reportError("some error occur");
                        return;
                    }
                }
                reportError("some error occur");
            } catch (Exception e) {
                reportError(e.getMessage());
            }
        } else {
            reportError("WebSocket is not ready");
        }
    }

    public void close(int i, String str) {
        WebSocket webSocket = this.ws;
        if (webSocket != null) {
            try {
                webSocket.close(i, str);
            } catch (Exception e) {
                e.printStackTrace();
                reportError(e.getMessage());
            }
        }
    }

    public void destroy() {
        WebSocket webSocket = this.ws;
        if (webSocket != null) {
            try {
                webSocket.close(WebSocketCloseCodes.CLOSE_GOING_AWAY.getCode(), WebSocketCloseCodes.CLOSE_GOING_AWAY.name());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void reportError(String str) {
        IWebSocketAdapter.EventListener eventListener2 = this.eventListener;
        if (eventListener2 != null) {
            eventListener2.onError(str);
        }
    }
}
