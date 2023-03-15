package io.dcloud.common.adapter.ui.webview;

import android.content.Context;
import android.text.TextUtils;
import io.dcloud.a;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.util.BaseInfo;
import java.io.File;
import java.io.InputStream;

public class WebResUtil {
    private static final String F_WT = "wap2app__template/";

    public static InputStream getEncryptionInputStream(String str, IApp iApp) {
        return iApp.getConfusionMgr().getEncryptionInputStream(str, iApp);
    }

    public static String getHBuilderPrintUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String str2 = BaseInfo.REL_PRIVATE_WWW_DIR + File.separator;
        return str.startsWith(str2) ? str.substring(str2.length()) : str;
    }

    public static String getOriginalUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str.startsWith(DeviceInfo.FILE_PROTOCOL) ? str.substring(7) : str;
    }

    public static String getRelPath(String str, IApp iApp) {
        String originalUrl = getOriginalUrl(str);
        int indexOf = originalUrl.indexOf(F_WT);
        return indexOf >= 0 ? originalUrl.substring(indexOf + 18) : getHBuilderPrintUrl(iApp.convert2RelPath(originalUrl));
    }

    public static String handleWap2appTemplateFilePath(String str) {
        return BaseInfo.sBaseWap2AppTemplatePath + F_WT + str;
    }

    public static boolean isWap2appTemplateFile(IApp iApp, String str) {
        return BaseInfo.containsInTemplate(iApp, str);
    }

    public static Object opAdData(Context context, String str, String str2, Object obj) {
        return a.a(context, str, str2, obj);
    }
}
