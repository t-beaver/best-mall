package com.taobao.weex.common;

import com.alibaba.fastjson.JSONObject;
import java.util.Map;

public class WXRequest {
    public static final int DEFAULT_TIMEOUT_MS = 60000;
    public String body;
    public String inputType;
    public String instanceId;
    public boolean isFirstIpv4 = false;
    public String method;
    public Map<String, String> paramMap;
    public boolean sslVerify = false;
    public int timeoutMs = DEFAULT_TIMEOUT_MS;
    public JSONObject tls = null;
    public String url;
}
