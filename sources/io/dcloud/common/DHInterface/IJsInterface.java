package io.dcloud.common.DHInterface;

import org.json.JSONArray;

public interface IJsInterface {
    @Deprecated
    String exec(String str, String str2, String str3);

    String exec(String str, String str2, JSONArray jSONArray);

    void forceStop(String str);

    String prompt(String str, String str2);
}
