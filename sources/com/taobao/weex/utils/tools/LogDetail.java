package com.taobao.weex.utils.tools;

import android.text.TextUtils;
import android.util.Log;
import com.alibaba.fastjson.annotation.JSONField;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.el.parse.Operators;
import java.util.Locale;

public class LogDetail {
    public static final String KeyWords_Render = "Weex_Render";
    public static final String KeyWrod_Init = "Weex_Init";
    @JSONField(name = "Info")
    public Info info = new Info();
    public String keyWords = KeyWords_Render;
    @JSONField(name = "time")
    public Time time = new Time();

    public String toString() {
        return "taskName : " + this.info.taskName + " - LogDetail : {time = '" + this.time + Operators.SINGLE_QUOTE + ", info = '" + this.info + Operators.SINGLE_QUOTE + Operators.BLOCK_END_STR;
    }

    public void println() {
        if (WXEnvironment.isPerf()) {
            Log.e(TimeCalculator.TIMELINE_TAG, " timeline " + this.keyWords + " java LogDetail: " + toString());
        }
    }

    public void name(String str) {
        this.time.constructor();
        this.info.taskName = str;
        if (!TextUtils.isEmpty(str)) {
            String lowerCase = str.toLowerCase(Locale.ROOT);
            if (lowerCase.contains("module") || lowerCase.contains(WXBridgeManager.COMPONENT) || lowerCase.contains("framework")) {
                this.keyWords = KeyWrod_Init;
            }
        }
    }

    public void keyWorkds(String str) {
        this.keyWords = str;
    }

    public void taskStart() {
        this.time.taskStart();
    }

    public void taskEnd() {
        this.time.taskEnd();
        println();
    }
}
