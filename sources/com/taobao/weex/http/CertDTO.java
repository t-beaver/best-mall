package com.taobao.weex.http;

import io.dcloud.common.DHInterface.IReflectAble;

public class CertDTO implements IReflectAble {
    public String client;
    public String clientPassword;
    public String host;
    public String[] server;

    public String getHost() {
        return this.host;
    }

    public void setHost(String str) {
        this.host = str;
    }

    public String getClient() {
        return this.client;
    }

    public void setClient(String str) {
        this.client = str;
    }

    public String getClientPassword() {
        return this.clientPassword;
    }

    public void setClientPassword(String str) {
        this.clientPassword = str;
    }

    public String[] getServer() {
        return this.server;
    }

    public void setServer(String[] strArr) {
        this.server = strArr;
    }
}
