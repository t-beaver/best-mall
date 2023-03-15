package com.taobao.weex.ui.module;

import android.text.TextUtils;
import android.util.Log;
import androidx.collection.ArrayMap;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.ui.component.WXImage;
import com.taobao.weex.utils.LogLevel;
import io.dcloud.feature.uniapp.utils.AbsLogLevel;

public class ConsoleLogModule extends WXModule {
    @JSMethod(uiThread = false)
    public void switchLogLevel(String str, JSCallback jSCallback) {
        AbsLogLevel logLevel = getLogLevel(str);
        ArrayMap arrayMap = new ArrayMap();
        if (logLevel != null) {
            WXEnvironment.sLogLevel = logLevel;
            WXBridgeManager.getInstance().setLogLevel(WXEnvironment.sLogLevel.getValue(), WXEnvironment.isPerf());
            Log.e("shutao", "switchLogLevel--------" + WXEnvironment.isPerf());
            arrayMap.put("status", WXImage.SUCCEED);
        } else {
            arrayMap.put("status", "failure");
        }
        if (jSCallback != null) {
            jSCallback.invoke(arrayMap);
        }
    }

    @JSMethod(uiThread = false)
    public void setPerfMode(String str) {
        WXEnvironment.isPerf = AbsoluteConst.TRUE.equals(str);
        WXBridgeManager.getInstance().setLogLevel(WXEnvironment.sLogLevel.getValue(), WXEnvironment.isPerf());
    }

    private AbsLogLevel getLogLevel(String str) {
        if (!TextUtils.isEmpty(str)) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case 109935:
                    if (str.equals("off")) {
                        c = 0;
                        break;
                    }
                    break;
                case 3237038:
                    if (str.equals("info")) {
                        c = 1;
                        break;
                    }
                    break;
                case 95458899:
                    if (str.equals("debug")) {
                        c = 2;
                        break;
                    }
                    break;
                case 96784904:
                    if (str.equals("error")) {
                        c = 3;
                        break;
                    }
                    break;
                case 1124446108:
                    if (str.equals("warning")) {
                        c = 4;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    return LogLevel.OFF;
                case 1:
                    return LogLevel.INFO;
                case 2:
                    return LogLevel.DEBUG;
                case 3:
                    return LogLevel.ERROR;
                case 4:
                    return LogLevel.WARN;
            }
        }
        return null;
    }
}
