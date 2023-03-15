package io.dcloud.common.core.ui;

import android.text.TextUtils;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IJsInterface;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.TestUtil;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;

class h implements IJsInterface, IReflectAble {
    static boolean a = false;
    static final Class[] b = {String.class, String.class};
    AbsMgr c = null;
    IWebview d = null;
    String e = null;
    MessageHandler.IMessages f = new a();

    class a implements MessageHandler.IMessages {
        a() {
        }

        public void execute(Object obj) {
            Object[] objArr = (Object[]) obj;
            h.this.exec(String.valueOf(objArr[0]), String.valueOf(objArr[1]), (JSONArray) objArr[2]);
        }
    }

    h(IWebview iWebview) {
        this.d = iWebview;
        this.e = iWebview.obtainFrameView().obtainApp().obtainAppId();
        this.c = this.d.obtainFrameView().obtainWindowMgr();
    }

    public String exec(String str, String str2, String str3) {
        return exec(str, str2, JSONUtil.createJSONArray(str3));
    }

    public void forceStop(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.c.processEvent(IMgr.MgrType.WindowMgr, 20, str);
        }
    }

    public String prompt(String str, String str2) {
        if (!a) {
            TestUtil.record("JsInterfaceImpl", (Object) Thread.currentThread());
            a = true;
        }
        if (str2 == null || str2.length() <= 3 || !str2.substring(0, 4).equals("pdr:")) {
            return null;
        }
        try {
            JSONArray jSONArray = new JSONArray(str2.substring(4));
            String string = jSONArray.getString(0);
            String string2 = jSONArray.getString(1);
            boolean z = jSONArray.getBoolean(2);
            JSONArray createJSONArray = JSONUtil.createJSONArray(str);
            if (!z) {
                return exec(string, string2, createJSONArray);
            }
            MessageHandler.sendMessage(this.f, new Object[]{string, string2, createJSONArray});
            return null;
        } catch (JSONException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public String exec(String str, String str2, JSONArray jSONArray) {
        if (this.d.getContext() == null) {
            return "";
        }
        try {
            str = str.toLowerCase(Locale.ENGLISH);
            "io.dcloud.HBuilder".equals(this.d.getContext().getPackageName());
            return String.valueOf(this.c.processEvent(IMgr.MgrType.FeatureMgr, 1, new Object[]{this.d, str, str2, jSONArray}));
        } catch (Exception e2) {
            Logger.w("JsInterfaceImpl.exec pApiFeatureName=" + str + ";pActionName=" + str2 + ";pArgs=" + String.valueOf(jSONArray), e2);
            return null;
        }
    }
}
