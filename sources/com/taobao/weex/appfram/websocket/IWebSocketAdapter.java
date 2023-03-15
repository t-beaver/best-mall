package com.taobao.weex.appfram.websocket;

public interface IWebSocketAdapter {
    public static final String HEADER_SEC_WEBSOCKET_PROTOCOL = "Sec-WebSocket-Protocol";

    public interface EventListener {
        void onClose(int i, String str, boolean z);

        void onError(String str);

        void onMessage(String str);

        void onOpen();
    }

    void close(int i, String str);

    void connect(String str, String str2, EventListener eventListener);

    void connect(String str, String str2, String str3, EventListener eventListener);

    void destroy();

    void send(String str);
}
