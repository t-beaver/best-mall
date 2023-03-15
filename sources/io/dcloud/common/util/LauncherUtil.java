package io.dcloud.common.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.text.TextUtils;
import com.taobao.weex.WXEnvironment;

public class LauncherUtil {
    public static String getAuthorityFromPermission(Context context, String str) {
        boolean isEmpty = TextUtils.isEmpty(str);
        return "";
    }

    public static String getAuthorityFromPermissionDefault(Context context) {
        String authorityFromPermission = getAuthorityFromPermission(context, "com.android.launcher.permission.READ_SETTINGS");
        if (!TextUtils.isEmpty(authorityFromPermission)) {
            return authorityFromPermission;
        }
        if (Build.VERSION.SDK_INT < 19) {
            return getAuthorityFromPermission(context, "com.android.launcher2.permission.READ_SETTINGS");
        }
        return getAuthorityFromPermission(context, "com.android.launcher3.permission.READ_SETTINGS");
    }

    public static String getLauncherPackageName(Context context) {
        ActivityInfo activityInfo;
        Context applicationContext = context.getApplicationContext();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        ResolveInfo resolveActivity = applicationContext.getPackageManager().resolveActivity(intent, 0);
        if (resolveActivity == null || (activityInfo = resolveActivity.activityInfo) == null || activityInfo.packageName.equals(WXEnvironment.OS)) {
            return "";
        }
        return resolveActivity.activityInfo.packageName;
    }
}
