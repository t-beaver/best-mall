package com.taobao.weex.utils.tools;

import com.alibaba.fastjson.annotation.JSONField;
import com.taobao.weex.el.parse.Operators;

public class Info {
    @JSONField(name = "instanceId")
    public String instanceId;
    @JSONField(name = "platform")
    public String platform;
    @JSONField(name = "taskId")
    public int taskId;
    @JSONField(name = "taskInfo")
    public TaskInfo taskInfo = new TaskInfo();
    @JSONField(name = "taskName")
    public String taskName;

    public String toString() {
        return "Info : {instanceId = '" + this.instanceId + Operators.SINGLE_QUOTE + ",taskName = '" + this.taskName + Operators.SINGLE_QUOTE + ",taskInfo = '" + this.taskInfo + Operators.SINGLE_QUOTE + ",platform = '" + this.platform + Operators.SINGLE_QUOTE + ",taskId = '" + this.taskId + Operators.SINGLE_QUOTE + Operators.BLOCK_END_STR;
    }
}
