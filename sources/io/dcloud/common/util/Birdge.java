package io.dcloud.common.util;

import android.webkit.JavascriptInterface;
import io.dcloud.common.DHInterface.IJsInterface;
import io.dcloud.common.adapter.util.MessageHandler;
import org.json.JSONArray;

public class Birdge implements IJsInterface, MessageHandler.IMessages {
    IJsInterface mJsInterface;

    public Birdge(IJsInterface iJsInterface) {
        this.mJsInterface = iJsInterface;
    }

    @Deprecated
    public String exec(String str, String str2, String str3) {
        return this.mJsInterface.exec(str, str2, str3);
    }

    public void execute(Object obj) {
        this.mJsInterface.forceStop((String) obj);
    }

    @JavascriptInterface
    public void forceStop(String str) {
        MessageHandler.sendMessage(this, str);
    }

    @JavascriptInterface
    public String prompt(String str, String str2) {
        return this.mJsInterface.prompt(str, str2);
    }

    public String exec(String str, String str2, JSONArray jSONArray) {
        return this.mJsInterface.exec(str, str2, jSONArray);
    }
}
