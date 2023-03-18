package com.alibaba.fastjson.asm;

import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.el.parse.Operators;

public class MethodCollector {
    private int currentParameter;
    protected boolean debugInfoPresent;
    private final int ignoreCount;
    private final int paramCount;
    private final StringBuilder result = new StringBuilder();

    protected MethodCollector(int i, int i2) {
        this.ignoreCount = i;
        this.paramCount = i2;
        boolean z = false;
        this.currentParameter = 0;
        this.debugInfoPresent = i2 == 0 ? true : z;
    }

    /* access modifiers changed from: protected */
    public void visitLocalVariable(String str, int i) {
        int i2 = this.ignoreCount;
        if (i >= i2 && i < i2 + this.paramCount) {
            if (!str.equals(IWXUserTrackAdapter.MONITOR_ARG + this.currentParameter)) {
                this.debugInfoPresent = true;
            }
            this.result.append(Operators.ARRAY_SEPRATOR);
            this.result.append(str);
            this.currentParameter++;
        }
    }

    /* access modifiers changed from: protected */
    public String getResult() {
        return this.result.length() != 0 ? this.result.substring(1) : "";
    }
}
