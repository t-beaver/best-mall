package io.dcloud.feature.weex.config;

import io.dcloud.common.util.BaseInfo;
import java.io.File;

public class UniPathParser {
    public static String getAndroidPath(String str) {
        String str2 = BaseInfo.sDefaultBootApp + "/www/" + str;
        File file = new File(BaseInfo.sCacheFsAppsPath + str2);
        if (file.exists()) {
            return file.getPath();
        }
        return "file:///android_asset/apps/" + str2;
    }
}
