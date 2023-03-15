package com.taobao.weex.utils.tools;

import com.alibaba.fastjson.annotation.JSONField;
import com.taobao.weex.el.parse.Operators;

public class TaskInfo {
    @JSONField(name = "args")
    public String args;
    @JSONField(name = "relateTaskId")
    public int relateTaskId;

    public String toString() {
        return "TaskInfo{args = '" + this.args + Operators.SINGLE_QUOTE + ",relateTaskId = '" + this.relateTaskId + Operators.SINGLE_QUOTE + Operators.BLOCK_END_STR;
    }
}
