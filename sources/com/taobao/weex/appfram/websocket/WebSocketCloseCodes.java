package com.taobao.weex.appfram.websocket;

import androidx.core.view.PointerIconCompat;
import io.dcloud.feature.barcode2.decoding.CaptureActivityHandler;

public enum WebSocketCloseCodes {
    CLOSE_NORMAL(1000),
    CLOSE_GOING_AWAY(1001),
    CLOSE_PROTOCOL_ERROR(1002),
    CLOSE_UNSUPPORTED(1003),
    CLOSE_NO_STATUS(CaptureActivityHandler.CODE_DECODE_portrait),
    CLOSE_ABNORMAL(1006),
    UNSUPPORTED_DATA(PointerIconCompat.TYPE_CROSSHAIR),
    POLICY_VIOLATION(PointerIconCompat.TYPE_TEXT),
    CLOSE_TOO_LARGE(PointerIconCompat.TYPE_VERTICAL_TEXT),
    MISSING_EXTENSION(1010),
    INTERNAL_ERROR(PointerIconCompat.TYPE_COPY),
    SERVICE_RESTART(PointerIconCompat.TYPE_NO_DROP),
    TRY_AGAIN_LATER(PointerIconCompat.TYPE_ALL_SCROLL),
    TLS_HANDSHAKE(PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW);
    
    private int code;

    private WebSocketCloseCodes(int i) {
        this.code = i;
    }

    public int getCode() {
        return this.code;
    }
}
