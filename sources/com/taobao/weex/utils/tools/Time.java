package com.taobao.weex.utils.tools;

import com.alibaba.fastjson.annotation.JSONField;
import com.taobao.weex.el.parse.Operators;

public class Time {
    @JSONField(name = "constructor")
    public long constructor;
    @JSONField(name = "destructor")
    public long destructor;
    @JSONField(name = "execTime")
    public long execTime;
    @JSONField(name = "taskEnd")
    public long taskEnd;
    @JSONField(name = "taskStart")
    public long taskStart;
    @JSONField(name = "waitTime")
    public long waitTime;

    public String toString() {
        return "time : {constructor = '" + this.constructor + Operators.SINGLE_QUOTE + ",taskStart = '" + this.taskStart + Operators.SINGLE_QUOTE + ",execTime = '" + this.execTime + Operators.SINGLE_QUOTE + ",waitTime = '" + this.waitTime + Operators.SINGLE_QUOTE + ",destructor = '" + this.destructor + Operators.SINGLE_QUOTE + ",taskEnd = '" + this.taskEnd + Operators.SINGLE_QUOTE + Operators.BLOCK_END_STR;
    }

    /* access modifiers changed from: protected */
    public void constructor() {
        this.constructor = System.currentTimeMillis();
    }

    public void execTime() {
        this.execTime = this.taskEnd - this.taskStart;
    }

    public void taskStart() {
        this.taskStart = System.currentTimeMillis();
    }

    public void taskEnd() {
        this.taskEnd = System.currentTimeMillis();
        execTime();
        destructor();
    }

    private void destructor() {
        waitTime();
        this.destructor = System.currentTimeMillis();
    }

    public void waitTime() {
        this.waitTime = this.taskStart - this.constructor;
    }
}
