package io.dcloud.feature.weex.adapter;

import com.taobao.weex.adapter.IWXConfigAdapter;

public class DCDefaultConfigAdapter implements IWXConfigAdapter {
    public boolean checkMode(String str) {
        return false;
    }

    public String getConfig(String str, String str2, String str3) {
        return str3;
    }

    public String getConfigWhenInit(String str, String str2, String str3) {
        return str3;
    }
}
