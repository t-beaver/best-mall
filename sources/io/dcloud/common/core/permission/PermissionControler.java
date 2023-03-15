package io.dcloud.common.core.permission;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import io.dcloud.application.DCLoudApplicationImpl;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import io.dcloud.common.adapter.util.PermissionUtil;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.util.AppPermissionUtil;
import io.dcloud.common.util.ShortCutUtil;
import io.dcloud.feature.internal.sdk.SDK;
import io.src.dcloud.adapter.DCloudBaseActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

public final class PermissionControler {
    public static final int PERMISSION_DENIED = -1;
    public static final int PERMISSION_GRANTED = 0;
    static HashMap<String, ArrayList<String>> mAppPermissions = new HashMap<>(2);
    static ArrayList<String> mRootAppList = new ArrayList<>();
    private static LinkedList<a> mRtPnInfos = new LinkedList<>();

    public static String checkPermission(IWebview iWebview, String[] strArr) {
        String str = strArr[0];
        iWebview.obtainApp().obtainAppId();
        String obtainAppName = iWebview.obtainApp().obtainAppName();
        Context context = iWebview.getContext();
        if (str.equals("SHORTCUT")) {
            String str2 = Build.BRAND;
            if (str2.equalsIgnoreCase(MobilePhoneModel.MEIZU)) {
                return !AppPermissionUtil.isFlymeShortcutallowAllow(context, ShortCutUtil.getHeadShortCutIntent(obtainAppName)) ? IApp.AUTHORITY_DENIED : "notdeny";
            }
            String str3 = Build.MANUFACTURER;
            if (str3.equalsIgnoreCase(MobilePhoneModel.SMARTISAN)) {
                return MobilePhoneModel.isSmartisanLauncherPhone(context) ? IApp.AUTHORITY_DENIED : "notdeny";
            }
            if (AppPermissionUtil.getShotCutOpId() == -1) {
                return "unknown";
            }
            AppPermissionUtil.getShotCutOpId();
            if (str2.equalsIgnoreCase(MobilePhoneModel.XIAOMI)) {
                int checkOp = AppPermissionUtil.checkOp(context);
                if (checkOp == -1) {
                    return "unsupported";
                }
                if (checkOp == 0) {
                    return IApp.AUTHORITY_AUTHORIZED;
                }
                if (checkOp == 1) {
                    return IApp.AUTHORITY_DENIED;
                }
                if (checkOp == 3 || checkOp == 4) {
                    return IApp.AUTHORITY_UNDETERMINED;
                }
                return "unknown";
            } else if (str3.equalsIgnoreCase(MobilePhoneModel.HUAWEI)) {
                return !AppPermissionUtil.isEmuiShortcutallowAllow() ? IApp.AUTHORITY_DENIED : "notdeny";
            } else {
                return "unknown";
            }
        } else {
            return String.valueOf(convert5PlusValue(iWebview.obtainApp().checkSelfPermission(convertNativePermission(str), iWebview.obtainApp().obtainAppName())));
        }
    }

    public static boolean checkPermission(String str, String str2) {
        return true;
    }

    public static boolean checkSafePermission(String str, String str2) {
        if (!"console".equals(str2)) {
            Locale locale = Locale.ENGLISH;
            return "events".equals(str2.toLowerCase(locale)) || "uninview".equals(str2.toLowerCase(locale)) || "webview-x5".equals(str2.toLowerCase(locale)) || "uiwebview".equals(str2.toLowerCase(locale)) || "faceid".equals(str2.toLowerCase(locale)) || "canvas".equals(str2.toLowerCase(locale)) || "record".equals(str2.toLowerCase(locale));
        }
    }

    public static void clearCRequestPermissionsCache() {
        mRtPnInfos.clear();
    }

    public static String convert5PlusValue(int i) {
        return i != -1 ? i != 0 ? "unknown" : IApp.AUTHORITY_AUTHORIZED : IApp.AUTHORITY_UNDETERMINED;
    }

    public static String convertNativePermission(String str) {
        return PermissionUtil.convert2SystemPermission(str);
    }

    public static void destroy() {
        mAppPermissions.clear();
        mAppPermissions = null;
    }

    public static ArrayList<String> getPermissionList(String str) {
        return mAppPermissions.get(str);
    }

    public static String getPermissionsErrorDesp(String str) {
        str.hashCode();
        if (!str.equals("invocation")) {
            return null;
        }
        return DCLoudApplicationImpl.self().getContext().getString(R.string.dcloud_permissions_njs_tips1);
    }

    public static void registerPermission(String str, ArrayList<String> arrayList) {
        mAppPermissions.remove(str);
        mAppPermissions.put(str, arrayList);
    }

    public static void registerRootPermission(String str) {
        mRootAppList.add(str);
    }

    private static void removeRequestPermissionForCode(int i) {
        Iterator it = mRtPnInfos.iterator();
        while (it.hasNext()) {
            a aVar = (a) it.next();
            if (aVar.b == i) {
                mRtPnInfos.remove(aVar);
                return;
            }
        }
    }

    public static void requestPermissions(Activity activity, String[] strArr, int i) {
        if (activity != null && strArr != null && strArr.length > 0) {
            if (activity instanceof DCloudBaseActivity) {
                mRtPnInfos.offer(new a(strArr, i));
                if (mRtPnInfos.size() == 1) {
                    runRequestPermissions(activity, strArr, i);
                    return;
                }
                return;
            }
            runRequestPermissions(activity, strArr, i);
        }
    }

    public static void runNextRequestPermission(Activity activity, int i) {
        a first;
        removeRequestPermissionForCode(i);
        if (mRtPnInfos.size() > 0 && (first = mRtPnInfos.getFirst()) != null) {
            runRequestPermissions(activity, first.a, first.b);
        }
    }

    private static void runRequestPermissions(Activity activity, String[] strArr, int i) {
        if (strArr != null && strArr.length > 0) {
            if (SDK.isUniMPSDK()) {
                PlatformUtil.invokeMethod(activity.getClass().getName(), "requestUniMPPermissions", activity, new Class[]{strArr.getClass(), Integer.TYPE}, new Object[]{strArr, Integer.valueOf(i)});
                return;
            }
            ActivityCompat.requestPermissions(activity, strArr, i);
        }
    }

    public static void unregisterRootPermission(String str) {
        mRootAppList.remove(str);
    }
}
