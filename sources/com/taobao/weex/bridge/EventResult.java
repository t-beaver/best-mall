package com.taobao.weex.bridge;

public class EventResult {
    private Object result;
    private boolean success = false;

    public void onCallback(Object obj) {
        this.success = true;
        this.result = obj;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public Object getResult() {
        return this.result;
    }
}
