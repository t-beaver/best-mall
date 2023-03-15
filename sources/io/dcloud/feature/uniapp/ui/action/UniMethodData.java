package io.dcloud.feature.uniapp.ui.action;

import com.alibaba.fastjson.JSONArray;

public class UniMethodData {
    JSONArray args;
    String method;

    public String getMethod() {
        return this.method;
    }

    public JSONArray getArgs() {
        return this.args;
    }

    public UniMethodData(String str, JSONArray jSONArray) {
        this.method = str;
        this.args = jSONArray;
    }
}
