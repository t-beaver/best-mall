package com.taobao.weex.utils;

import java.io.Serializable;
import java.util.HashMap;

public class WXMap extends HashMap<String, String> implements Serializable {
    public String put(String str, byte[] bArr) {
        return (String) super.put(str, new String(bArr));
    }
}
