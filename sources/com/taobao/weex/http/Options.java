package com.taobao.weex.http;

import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.common.WXRequest;
import java.util.HashMap;
import java.util.Map;

class Options {
    private String body;
    private Map<String, String> headers;
    private String inputType;
    private boolean isFirstIpv4;
    private String method;
    private boolean sslVerify;
    private int timeout;
    private JSONObject tlsConfig;
    private Type type;
    private String url;

    public enum Type {
        json,
        text,
        jsonp,
        base64
    }

    private Options(String str, String str2, Map<String, String> map, String str3, Type type2, int i) {
        this.type = Type.text;
        this.timeout = WXRequest.DEFAULT_TIMEOUT_MS;
        this.sslVerify = false;
        this.isFirstIpv4 = false;
        this.method = str;
        this.url = str2;
        this.headers = map;
        this.body = str3;
        this.type = type2;
        this.timeout = i == 0 ? WXRequest.DEFAULT_TIMEOUT_MS : i;
    }

    private Options(String str, String str2, Map<String, String> map, String str3, Type type2, int i, boolean z, boolean z2) {
        this.type = Type.text;
        this.timeout = WXRequest.DEFAULT_TIMEOUT_MS;
        this.sslVerify = false;
        this.isFirstIpv4 = false;
        this.method = str;
        this.url = str2;
        this.headers = map;
        this.body = str3;
        this.type = type2;
        this.timeout = i == 0 ? WXRequest.DEFAULT_TIMEOUT_MS : i;
        this.sslVerify = z;
        this.isFirstIpv4 = z2;
    }

    public String getMethod() {
        return this.method;
    }

    public String getUrl() {
        return this.url;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public String getBody() {
        return this.body;
    }

    public Type getType() {
        return this.type;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public boolean getSslVerify() {
        return this.sslVerify;
    }

    public boolean isFirstIpv4() {
        return this.isFirstIpv4;
    }

    public JSONObject getTlsConfig() {
        return this.tlsConfig;
    }

    public void setTlsConfig(JSONObject jSONObject) {
        this.tlsConfig = jSONObject;
    }

    public String getInputType() {
        return this.inputType;
    }

    public void setInputType(String str) {
        this.inputType = str;
    }

    public static class Builder {
        private JSONObject androidTLSConfig;
        private String body;
        private Map<String, String> headers = new HashMap();
        private String inputType;
        private boolean isFirstIpv4;
        private String method;
        private boolean sslVerify;
        private int timeout;
        private Type type;
        private String url;

        public Builder setAndroidTlsConfig(JSONObject jSONObject) {
            this.androidTLSConfig = jSONObject;
            return this;
        }

        public Builder setMethod(String str) {
            this.method = str;
            return this;
        }

        public Builder setUrl(String str) {
            this.url = str;
            return this;
        }

        public Builder putHeader(String str, String str2) {
            this.headers.put(str, str2);
            return this;
        }

        public Builder setBody(String str) {
            this.body = str;
            return this;
        }

        public Builder setSslVerify(boolean z) {
            this.sslVerify = z;
            return this;
        }

        public Builder setFirstIpv4(boolean z) {
            this.isFirstIpv4 = z;
            return this;
        }

        public Builder setType(String str) {
            if (Type.json.name().equals(str)) {
                this.type = Type.json;
            } else if (Type.jsonp.name().equals(str)) {
                this.type = Type.jsonp;
            } else if (Type.base64.name().equals(str)) {
                this.type = Type.base64;
            } else {
                this.type = Type.text;
            }
            return this;
        }

        public Builder setType(Type type2) {
            this.type = type2;
            return this;
        }

        public Builder setInputTypes(String str) {
            this.inputType = str;
            return this;
        }

        public Builder setTimeout(int i) {
            this.timeout = i;
            return this;
        }

        public Options createOptions() {
            Options options = new Options(this.method, this.url, this.headers, this.body, this.type, this.timeout, this.sslVerify, this.isFirstIpv4);
            options.setInputType(this.inputType);
            options.setTlsConfig(this.androidTLSConfig);
            return options;
        }
    }
}
