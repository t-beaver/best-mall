package com.taobao.weex.common;

import io.dcloud.feature.uniapp.common.UniException;

public class WXException extends UniException {
    private static final long serialVersionUID = 7265837506862157379L;

    public WXException(String str) {
        super(str);
    }
}
