package io.dcloud.common.adapter.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.widget.CheckBox;
import androidx.core.app.ActivityCompat;
import com.dcloud.android.widget.dialog.DCloudAlertDialog;
import com.facebook.common.callercontext.ContextChain;
import io.dcloud.WebAppActivity;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.core.permission.PermissionControler;
import io.dcloud.common.util.AppPermissionUtil;
import io.dcloud.common.util.DialogUtil;
import io.dcloud.common.util.IOUtil;
import io.dcloud.common.util.StringUtil;
import io.dcloud.feature.gg.dcloud.ADSim;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

public class PermissionUtil implements IReflectAble {
    public static final String PMS_CAMERA = "CAMERA";
    public static final String PMS_CONTACTS = "CONTACTS";
    public static final String PMS_GALLERY = "GALLERY";
    public static final String PMS_LOCATION = "LOCATION";
    public static final String PMS_NATIVEJS = "NATIVE.JS";
    public static final String PMS_PHONE = "PHONE";
    public static final String PMS_PUSH = "PUSH";
    public static final String PMS_RECORD = "RECORD";
    public static final String PMS_SHORTCUT = "SHORTCUT";
    public static final String PMS_SMS = "SMS";
    public static final String PMS_STORAGE = "STORAGE";
    private static List<String> alwaysDeniedPer = null;
    public static boolean isCheckPermissionDisabled = false;
    private static HashMap<Integer, Object[]> sActivityResultCallBacks = new HashMap<>();
    /* access modifiers changed from: private */
    public static int sDefQequestCode = 60505;
    private static HashMap<String, PermissionData> sPermissionData;
    private static HashMap<Integer, HashMap<Request, String[]>> sRequestCallBacks = new HashMap<>();
    private static int sRequestCodeCounter = 60505;
    public static int sUseStreamAppPermissionDialogCount = 0;
    private static LinkedList<ShowDialogData> sUseStreamAppPermissionDialogs = new LinkedList<>();
    /* access modifiers changed from: private */
    public static HashMap<String, HashMap<String, Integer>> useRejectedCache = new HashMap<>();

    static class PermissionData {
        static final int CB_NOSHOW = -1;
        static final int CB_SELECTED = 1;
        static final int CB_SHOW = 0;
        static final int GT_DENIED = -1;
        static final int GT_GRANTED = 1;
        static final int GT_ONCE = 0;
        int checkbox;
        int grantType;
        int messageId;
        String name;

        PermissionData(String str, int i, int i2, int i3) {
            this.name = str;
            this.messageId = i;
            this.checkbox = i2;
            this.grantType = i3;
        }
    }

    public static abstract class Request {
        public static final int PERMISSION_ASK = 1;
        public static final int PERMISSION_DENIED = -1;
        public static final int PERMISSION_GRANTED = 0;
        private int mRequestCode = PermissionUtil.sDefQequestCode;

        public String getAppName() {
            return null;
        }

        public int getRequestCode() {
            return this.mRequestCode;
        }

        public abstract void onDenied(String str);

        public abstract void onGranted(String str);

        public void setRequestCode(int i) {
            this.mRequestCode = i;
        }
    }

    public static abstract class StreamPermissionRequest extends Request {
        IApp mApp;
        private String mAppName = null;
        public String mAppid = null;
        private String[] mOriginalPermisson = null;
        private String[] mPermission = null;
        public Object mTag = null;

        public StreamPermissionRequest(IApp iApp) {
            setApp(iApp);
        }

        public String getAppName() {
            return this.mAppName;
        }

        /* access modifiers changed from: package-private */
        public String[] getStreamRequestPermission() {
            return this.mOriginalPermisson;
        }

        /* access modifiers changed from: protected */
        public String[] getSystemRequestPermission() {
            return this.mPermission;
        }

        public void setApp(IApp iApp) {
            this.mApp = iApp;
            this.mAppid = iApp.obtainAppId();
            this.mAppName = iApp.obtainAppName();
        }

        public StreamPermissionRequest setRequestPermission(String... strArr) {
            this.mOriginalPermisson = strArr;
            this.mPermission = new String[strArr.length];
            int i = 0;
            while (true) {
                String[] strArr2 = this.mPermission;
                if (i >= strArr2.length) {
                    return this;
                }
                strArr2[i] = PermissionUtil.convert2SystemPermission(strArr[i]);
                i++;
            }
        }
    }

    static {
        HashMap<String, PermissionData> hashMap = new HashMap<>();
        sPermissionData = hashMap;
        hashMap.put(PMS_LOCATION, new PermissionData(PMS_LOCATION, R.string.dcloud_permissions_whether_allow, -1, 1));
        sPermissionData.put(PMS_RECORD, new PermissionData(PMS_RECORD, R.string.dcloud_permissions_record_whether_allow, -1, 1));
        sPermissionData.put(PMS_CAMERA, new PermissionData(PMS_CAMERA, R.string.dcloud_permissions_camera_whether_allow, -1, 1));
        sPermissionData.put(PMS_GALLERY, new PermissionData(PMS_GALLERY, R.string.dcloud_permissions_album_whether_allow, -1, 1));
        sPermissionData.put(PMS_PUSH, new PermissionData(PMS_PUSH, R.string.dcloud_permissions_informs_whether_allow, -1, 1));
        sPermissionData.put("SHORTCUT", new PermissionData("SHORTCUT", R.string.dcloud_permissions_short_cut_close_tips, 1, 0));
        sPermissionData.put(PMS_SMS, new PermissionData(PMS_SMS, R.string.dcloud_permissions_sms_whether_allow, -1, 1));
        sPermissionData.put(PMS_PHONE, new PermissionData(PMS_PHONE, R.string.dcloud_permissions_phone_call_whether_allow, -1, 1));
        sPermissionData.put(PMS_NATIVEJS, new PermissionData(PMS_NATIVEJS, R.string.dcloud_permissions_njs_whether_allow, 1, 0));
    }

    private PermissionUtil() {
    }

    private static boolean caseVersion(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23 && activity != null && activity.getApplicationInfo().targetSdkVersion >= 23) {
            String str = Build.BRAND;
            return !str.equalsIgnoreCase(MobilePhoneModel.GIONEE) && !str.equalsIgnoreCase(MobilePhoneModel.QIHU360);
        }
    }

    public static boolean checkLocationPermission(Activity activity) {
        if (isEMUIRom(activity)) {
            return checkPermission_EMUI(activity);
        }
        if (isMiuiRom(activity)) {
            int isMiui = isMiui(activity, "android.permission.ACCESS_COARSE_LOCATION", (String) null);
            if (isMiui == -100 || isMiui == 0) {
                return true;
            }
            return false;
        } else if (isFlymeRom(activity)) {
            return checkPermission_Flyme(activity);
        } else {
            return true;
        }
    }

    public static boolean checkLocationService(Activity activity) {
        try {
            LocationManager locationManager = (LocationManager) activity.getSystemService("location");
            boolean isProviderEnabled = locationManager.isProviderEnabled("gps");
            boolean isProviderEnabled2 = locationManager.isProviderEnabled("network");
            if (isProviderEnabled || isProviderEnabled2) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public static String checkPermission(IWebview iWebview, String[] strArr) {
        if (strArr[0].equals("SHORTCUT")) {
            return PermissionControler.checkPermission(iWebview, strArr);
        }
        if (caseVersion(iWebview.getActivity())) {
            return PermissionControler.checkPermission(iWebview, strArr);
        }
        return checkSelfPermission(iWebview.getActivity(), convert2SystemPermission(strArr[0]), iWebview.obtainApp().obtainAppName()) == -1 ? IApp.AUTHORITY_DENIED : "notdeny";
    }

    public static boolean checkPermissionDenied(Activity activity, String str) {
        initAlwaysDenied(activity);
        if (alwaysDeniedPer.contains(str)) {
            return isCheckPermissionDisabled || !ActivityCompat.shouldShowRequestPermissionRationale(activity, str);
        }
        return false;
    }

    private static boolean checkPermission_EMUI(Activity activity) {
        try {
            if (((Integer) PlatformUtil.invokeMethod("com.huawei.android.app.AppOpsManagerEx", "getMode", (Object) null, new Class[]{Integer.TYPE, String.class}, new Object[]{8, activity.getPackageName()})).intValue() == 1) {
                return true;
            }
            return false;
        } catch (Throwable th) {
            th.printStackTrace();
            return true;
        }
    }

    private static boolean checkPermission_Flyme(Activity activity) {
        try {
            return ((Boolean) PlatformUtil.invokeMethod("meizu.security.FlymePermissionManager", "isFlymePermissionGranted", (Object) null, new Class[]{Integer.TYPE}, new Object[]{75})).booleanValue();
        } catch (Throwable th) {
            th.printStackTrace();
            return true;
        }
    }

    public static boolean checkPermissions(Context context, String[] strArr) {
        if (strArr != null && strArr.length > 0) {
            for (String checkSelfPermission : strArr) {
                if (checkSelfPermission((Activity) context, checkSelfPermission, "") == -1) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int checkSelfPermission(Activity activity, String str, String str2) {
        if ("android.permission.INSTALL_SHORTCUT".equals(str)) {
            return 1 != AppPermissionUtil.checkPermission(activity, str2) ? 0 : -1;
        }
        if (!caseVersion(activity) || str == null) {
            return trycatchGetPermission(activity, str, str2);
        }
        return ActivityCompat.checkSelfPermission(activity, str);
    }

    public static int checkStreamAppPermission(Context context, String str, String str2) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("stream_permission", 0);
        return sharedPreferences.getInt(str + "_" + str2, 1);
    }

    public static void clearPermission(Context context, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("stream_permission", 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        for (String str2 : sPermissionData.keySet()) {
            edit.remove(str + "_" + str2);
        }
        edit.commit();
    }

    public static void clearUseRejectedCache() {
        useRejectedCache.clear();
    }

    private static synchronized boolean continueShowStreamAppPermissionDialog(ShowDialogData showDialogData) {
        synchronized (PermissionUtil.class) {
            if (showDialogData.force) {
                return true;
            }
            if (sUseStreamAppPermissionDialogCount != 0) {
                sUseStreamAppPermissionDialogs.add(showDialogData);
            }
            sUseStreamAppPermissionDialogCount++;
            boolean isEmpty = sUseStreamAppPermissionDialogs.isEmpty();
            return isEmpty;
        }
    }

    public static String convert2StreamPermission(String str) {
        if ("android.permission.CAMERA".equals(str)) {
            return PMS_CAMERA;
        }
        if ("android.permission.RECORD_AUDIO".equals(str)) {
            return PMS_RECORD;
        }
        if ("android.permission.ACCESS_COARSE_LOCATION".equals(str)) {
            return PMS_LOCATION;
        }
        if ("android.permission.WRITE_CONTACTS".equals(str)) {
            return PMS_CONTACTS;
        }
        if ("android.permission.SEND_SMS".equals(str)) {
            return PMS_SMS;
        }
        if ("android.permission.CALL_PHONE".equals(str)) {
            return PMS_PHONE;
        }
        if ("android.permission.WRITE_EXTERNAL_STORAGE".equals(str)) {
            return PMS_STORAGE;
        }
        if ("android.permission.INSTALL_SHORTCUT".equals(str)) {
            return "SHORTCUT";
        }
        if (!PMS_GALLERY.equals(str) && !PMS_NATIVEJS.equals(str)) {
            PMS_PUSH.equals(str);
        }
        return str;
    }

    public static String convert2SystemPermission(String str) {
        if (PMS_CAMERA.equalsIgnoreCase(str)) {
            return "android.permission.CAMERA";
        }
        if (PMS_RECORD.equalsIgnoreCase(str)) {
            return "android.permission.RECORD_AUDIO";
        }
        if (PMS_LOCATION.equalsIgnoreCase(str)) {
            return "android.permission.ACCESS_COARSE_LOCATION";
        }
        if (PMS_CONTACTS.equalsIgnoreCase(str)) {
            return "android.permission.WRITE_CONTACTS";
        }
        if (PMS_STORAGE.equalsIgnoreCase(str)) {
            return "android.permission.WRITE_EXTERNAL_STORAGE";
        }
        if (PMS_SMS.equalsIgnoreCase(str)) {
            return "android.permission.SEND_SMS";
        }
        if (PMS_PHONE.equalsIgnoreCase(str)) {
            return "android.permission.CALL_PHONE";
        }
        if ("SHORTCUT".equalsIgnoreCase(str)) {
            return "android.permission.INSTALL_SHORTCUT";
        }
        if (!PMS_GALLERY.equalsIgnoreCase(str) && !PMS_NATIVEJS.equalsIgnoreCase(str)) {
            PMS_PUSH.equalsIgnoreCase(str);
        }
        return str;
    }

    public static String convert5PlusValue(int i) {
        return PermissionControler.convert5PlusValue(i);
    }

    public static String convertNativePermission(String str) {
        return PermissionControler.convertNativePermission(str);
    }

    private static int getDeivceSuitablePixel(Activity activity, int i) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return (int) (((float) i) * displayMetrics.density);
    }

    public static int getRequestCode() {
        if (sRequestCodeCounter >= 65535) {
            restRequstCode();
        }
        int i = sRequestCodeCounter;
        sRequestCodeCounter = i + 1;
        return i;
    }

    public static void goPermissionCenter(Activity activity, String str, String str2, Request request) {
        if (!SafeCenter.goSafeCenter(activity, str2, request)) {
            request.onDenied(str2);
        }
    }

    public static void goSafeCenter(Activity activity) {
        SafeCenter.goSafeCenter(activity);
    }

    public static boolean hasDefinedInManifest(Context context, String str) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getApplicationInfo().packageName, 4096);
            if (packageInfo != null) {
                int i = 0;
                while (true) {
                    String[] strArr = packageInfo.requestedPermissions;
                    if (i >= strArr.length) {
                        break;
                    } else if (str.equals(strArr[i])) {
                        return true;
                    } else {
                        i++;
                    }
                }
            }
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void initAlwaysDenied(Activity activity) {
        if (alwaysDeniedPer == null) {
            String bundleData = SP.getBundleData((Context) activity, "ALWAYS_DENIED_PERMISSION", "permissions");
            if (!TextUtils.isEmpty(bundleData)) {
                alwaysDeniedPer = new ArrayList(Arrays.asList(bundleData.split(",")));
            } else {
                alwaysDeniedPer = new ArrayList();
            }
        }
    }

    private static boolean isAndroid(Activity activity, String str, Request request) {
        Intent intent = new Intent();
        intent.setClassName("com.android.Setting", "com.android.SubSetting");
        intent.putExtra("package", activity.getPackageName());
        int requestCode = getRequestCode();
        activity.startActivityForResult(intent, requestCode);
        saveCallabckData(activity, str, request, requestCode);
        return true;
    }

    public static boolean isEMUIRom(Activity activity) {
        return Build.MANUFACTURER.toLowerCase(Locale.ENGLISH).contains("huawei");
    }

    private static boolean isFlyme(Activity activity, String str, Request request) {
        if (!Build.BRAND.contains("Meizu")) {
            return false;
        }
        Intent intent = new Intent();
        intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity");
        intent.putExtra("packageName", activity.getPackageName());
        int requestCode = getRequestCode();
        activity.startActivityForResult(intent, requestCode);
        saveCallabckData(activity, str, request, requestCode);
        return true;
    }

    private static boolean isFlymeRom(Activity activity) {
        return Build.BRAND.toLowerCase(Locale.ENGLISH).contains("meizu");
    }

    public static boolean isMainStreamPermission(String str) {
        return PMS_LOCATION.equalsIgnoreCase(str) || "SHORTCUT".equalsIgnoreCase(str) || PMS_RECORD.equalsIgnoreCase(str);
    }

    private static int isMiui(Activity activity, String str, String str2) {
        Object systemService;
        try {
            if (isMiuiRom(activity) && (systemService = activity.getSystemService("appops")) != null) {
                int i = systemService.getClass().getField("OP_GPS").getInt((Object) null);
                Class<?> cls = systemService.getClass();
                Class cls2 = Integer.TYPE;
                int intValue = ((Integer) cls.getMethod("checkOp", new Class[]{cls2, cls2, String.class}).invoke(systemService, new Object[]{Integer.valueOf(i), Integer.valueOf(Binder.getCallingUid()), activity.getPackageName()})).intValue();
                if (intValue == systemService.getClass().getField("MODE_IGNORED").getInt((Object) null)) {
                    return -1;
                }
                if (intValue == systemService.getClass().getField("MODE_ALLOWED").getInt((Object) null)) {
                    return 0;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return -100;
    }

    private static boolean isMiuiRom(Activity activity) {
        if (Build.VERSION.SDK_INT < 21) {
            return false;
        }
        String property = System.getProperty("http.agent");
        return !TextUtils.isEmpty(property) && property.toLowerCase(Locale.ENGLISH).contains("miui");
    }

    public static void onActivityResult(Activity activity, int i, int i2, Intent intent) {
        HashMap<Integer, Object[]> hashMap = sActivityResultCallBacks;
        int i3 = i % ADSim.INTISPLSH;
        Object[] objArr = hashMap.get(Integer.valueOf(i3));
        if (objArr != null) {
            if (System.currentTimeMillis() - activity.getIntent().getLongExtra(IntentConst.PER_GO_CENTER_TIME, System.currentTimeMillis()) > 1000) {
                sActivityResultCallBacks.remove(Integer.valueOf(i3));
                String str = (String) objArr[0];
                String convert2SystemPermission = convert2SystemPermission(str);
                Request request = (Request) objArr[1];
                activity.getIntent().removeExtra(IntentConst.PER_GO_CENTER_TIME);
                if (checkSelfPermission(activity, convert2SystemPermission, request.getAppName()) == 0) {
                    request.onGranted(str);
                } else {
                    request.onDenied(str);
                }
            }
        }
    }

    public static void onRequestSysPermissionResume(Activity activity) {
        int intExtra = activity.getIntent().getIntExtra(IntentConst.PER_GO_CENTER_REQUESTCODE, 0);
        if (intExtra != 0) {
            onActivityResult(activity, intExtra, 0, (Intent) null);
        }
    }

    public static void onSystemPermissionsResult(Activity activity, int i, String[] strArr, int[] iArr) {
        String[] strArr2;
        HashMap remove = sRequestCallBacks.remove(Integer.valueOf(i));
        if (remove != null && remove.size() > 0) {
            Request[] requestArr = (Request[]) remove.keySet().toArray(new Request[0]);
            Request request = null;
            if (requestArr.length > 0) {
                request = requestArr[0];
            }
            initAlwaysDenied(activity);
            for (int i2 = 0; i2 < strArr.length; i2++) {
                int i3 = iArr[i2];
                String convert2StreamPermission = convert2StreamPermission(strArr[i2]);
                if (i3 == -1) {
                    try {
                        if (!checkPermissionDenied(activity, strArr[i2])) {
                            alwaysDeniedPer.add(strArr[i2]);
                        }
                    } catch (RuntimeException unused) {
                    }
                    if (request != null) {
                        request.onDenied(convert2StreamPermission);
                    }
                } else if (i3 == 0) {
                    if (alwaysDeniedPer.contains(strArr[i2])) {
                        alwaysDeniedPer.remove(strArr[i2]);
                    }
                    if (request != null) {
                        request.onGranted(convert2StreamPermission);
                    }
                }
            }
            if (alwaysDeniedPer.size() > 0) {
                SP.setBundleData(activity, "ALWAYS_DENIED_PERMISSION", "permissions", TextUtils.join(",", (String[]) alwaysDeniedPer.toArray(new String[0])));
            }
            if (strArr.length == 0 && iArr.length == 0 && request != null && (strArr2 = (String[]) remove.get(request)) != null) {
                for (String convert2StreamPermission2 : strArr2) {
                    request.onDenied(convert2StreamPermission(convert2StreamPermission2));
                }
            }
        }
    }

    public static void putStreamAppPermission(Context context, String str, String str2, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences("stream_permission", 0).edit();
        edit.putInt(str + "_" + str2, i).commit();
    }

    public static void removeStreamAppPermission(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences("stream_permission", 0).edit();
        edit.remove(str + "_" + str2).commit();
    }

    public static void removeTempPermission(Context context, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("stream_permission", 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        for (String next : sPermissionData.keySet()) {
            String str2 = str + "_" + next;
            if (sharedPreferences.contains(str2) && (sharedPreferences.getInt(str2, 1) != 0 || "SHORTCUT".equals(next) || PMS_NATIVEJS.equals(next))) {
                edit.remove(str2);
            }
        }
        edit.commit();
    }

    public static void requestPermissions(Activity activity, String[] strArr, int i) {
        PermissionControler.requestPermissions(activity, strArr, i);
    }

    public static void requestSystemPermissions(Activity activity, String[] strArr, int i, Request request) {
        requestSystemPermissions(activity, strArr, i, request, true);
    }

    public static void restRequstCode() {
        sRequestCodeCounter = sDefQequestCode;
    }

    /* access modifiers changed from: private */
    public static void saveCallabckData(Activity activity, String str, Request request, int i) {
        sActivityResultCallBacks.put(Integer.valueOf(i), new Object[]{str, request});
        activity.getIntent().putExtra(IntentConst.PER_GO_CENTER_REQUESTCODE, i);
        activity.getIntent().putExtra(IntentConst.PER_GO_CENTER_TIME, System.currentTimeMillis());
    }

    /* access modifiers changed from: private */
    public static synchronized void showStreamAppPermissionDialog() {
        synchronized (PermissionUtil.class) {
            int i = sUseStreamAppPermissionDialogCount;
            if (i > 0) {
                sUseStreamAppPermissionDialogCount = i - 1;
            }
            if (!sUseStreamAppPermissionDialogs.isEmpty()) {
                ShowDialogData pop = sUseStreamAppPermissionDialogs.pop();
                pop.force = true;
                if (1 != useStreamPermission(pop)) {
                    showStreamAppPermissionDialog();
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:108:?, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:109:?, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:110:?, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:111:?, code lost:
        return r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:119:?, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003f, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r6.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00cc, code lost:
        r6 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00cd, code lost:
        r2 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00cf, code lost:
        r6 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00d0, code lost:
        r2 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00d2, code lost:
        r6 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
        r6 = r6.getMessage();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00d7, code lost:
        if (r6 == null) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00ec, code lost:
        r6 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00f1, code lost:
        if (r6.getMessage() != null) goto L_0x00f3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00fd, code lost:
        if (r6.getMessage().contains("Permission deny") == false) goto L_0x00ff;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0109, code lost:
        if (r6.getMessage().contains("Permission denied") != false) goto L_0x010b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0114, code lost:
        if (android.os.Build.BRAND.equalsIgnoreCase(io.dcloud.common.adapter.util.MobilePhoneModel.GIONEE) == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x01aa, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:?, code lost:
        r6.printStackTrace();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:105:? A[ExcHandler: all (unused java.lang.Throwable), SYNTHETIC, Splitter:B:1:0x0006] */
    /* JADX WARNING: Removed duplicated region for block: B:109:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:119:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00d9 A[Catch:{ SecurityException -> 0x00e9, all -> 0x01d7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00f3 A[Catch:{ SecurityException -> 0x00e9, all -> 0x01d7 }] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:36:0x00d3=Splitter:B:36:0x00d3, B:6:0x000f=Splitter:B:6:0x000f} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int trycatchGetPermission(android.app.Activity r6, java.lang.String r7, java.lang.String r8) {
        /*
            java.lang.String r0 = "/temp.3gp"
            r1 = 0
            r2 = -1
            java.lang.String r3 = "android.permission.CAMERA"
            boolean r3 = r3.equals(r7)     // Catch:{ SecurityException -> 0x01cf, all -> 0x01d7 }
            r4 = 1
            if (r3 == 0) goto L_0x0045
            r6 = 0
            r7 = 0
        L_0x000f:
            int r8 = android.hardware.Camera.getNumberOfCameras()     // Catch:{ Exception -> 0x003f, all -> 0x01d7 }
            if (r7 >= r8) goto L_0x0029
            android.hardware.Camera$CameraInfo r8 = new android.hardware.Camera$CameraInfo     // Catch:{ Exception -> 0x003f, all -> 0x01d7 }
            r8.<init>()     // Catch:{ Exception -> 0x003f, all -> 0x01d7 }
            android.hardware.Camera.getCameraInfo(r7, r8)     // Catch:{ Exception -> 0x003f, all -> 0x01d7 }
            int r8 = r8.facing     // Catch:{ Exception -> 0x003f, all -> 0x01d7 }
            if (r8 != r4) goto L_0x0026
            android.hardware.Camera r6 = android.hardware.Camera.open(r7)     // Catch:{ Exception -> 0x003f, all -> 0x01d7 }
            goto L_0x0029
        L_0x0026:
            int r7 = r7 + 1
            goto L_0x000f
        L_0x0029:
            if (r6 != 0) goto L_0x002f
            android.hardware.Camera r6 = android.hardware.Camera.open()     // Catch:{ Exception -> 0x003f, all -> 0x01d7 }
        L_0x002f:
            if (r6 == 0) goto L_0x01cf
            android.hardware.Camera$Parameters r7 = r6.getParameters()     // Catch:{ Exception -> 0x003f, all -> 0x01d7 }
            if (r7 == 0) goto L_0x003a
            r7.getSupportedVideoSizes()     // Catch:{ Exception -> 0x003f, all -> 0x01d7 }
        L_0x003a:
            r6.release()     // Catch:{ Exception -> 0x003f, all -> 0x01d7 }
            goto L_0x01d7
        L_0x003f:
            r6 = move-exception
            r6.printStackTrace()     // Catch:{ SecurityException -> 0x01cf, all -> 0x01d7 }
            goto L_0x01cf
        L_0x0045:
            java.lang.String r3 = "android.permission.RECORD_AUDIO"
            boolean r3 = r3.equals(r7)     // Catch:{ SecurityException -> 0x01cf, all -> 0x01d7 }
            if (r3 == 0) goto L_0x0118
            android.media.MediaRecorder r7 = new android.media.MediaRecorder     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            r7.<init>()     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            r7.reset()     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            r7.setAudioSource(r1)     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            r8.<init>()     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            java.lang.String r3 = android.os.Environment.DIRECTORY_DOWNLOADS     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            java.io.File r3 = r6.getExternalFilesDir(r3)     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            r8.append(r3)     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            r8.append(r0)     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            java.lang.String r8 = r8.toString()     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            r7.setOutputFile(r8)     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            r8 = 96000(0x17700, float:1.34525E-40)
            r7.setAudioSamplingRate(r8)     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            r7.setOutputFormat(r4)     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            r8 = 3
            r7.setAudioEncoder(r8)     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            r7.prepare()     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            r7.start()     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            r7.stop()     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            r7.release()     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            java.io.File r7 = new java.io.File     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            r8.<init>()     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            java.lang.String r3 = android.os.Environment.DIRECTORY_DOWNLOADS     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            java.io.File r3 = r6.getExternalFilesDir(r3)     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            r8.append(r3)     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            r8.append(r0)     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            java.lang.String r8 = r8.toString()     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            r7.<init>(r8)     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            long r7 = r7.length()     // Catch:{ IOException -> 0x00ec, Exception -> 0x00d2, all -> 0x01d7 }
            r3 = 0
            int r5 = (r7 > r3 ? 1 : (r7 == r3 ? 0 : -1))
            if (r5 <= 0) goto L_0x01cf
            java.io.File r7 = new java.io.File     // Catch:{ all -> 0x01d7, all -> 0x01d7, IOException -> 0x00cf, Exception -> 0x00cc, all -> 0x01d7 }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x01d7, all -> 0x01d7, IOException -> 0x00cf, Exception -> 0x00cc, all -> 0x01d7 }
            r8.<init>()     // Catch:{ all -> 0x01d7, all -> 0x01d7, IOException -> 0x00cf, Exception -> 0x00cc, all -> 0x01d7 }
            java.lang.String r2 = android.os.Environment.DIRECTORY_DOWNLOADS     // Catch:{ all -> 0x01d7, all -> 0x01d7, IOException -> 0x00cf, Exception -> 0x00cc, all -> 0x01d7 }
            java.io.File r6 = r6.getExternalFilesDir(r2)     // Catch:{ all -> 0x01d7, all -> 0x01d7, IOException -> 0x00cf, Exception -> 0x00cc, all -> 0x01d7 }
            r8.append(r6)     // Catch:{ all -> 0x01d7, all -> 0x01d7, IOException -> 0x00cf, Exception -> 0x00cc, all -> 0x01d7 }
            r8.append(r0)     // Catch:{ all -> 0x01d7, all -> 0x01d7, IOException -> 0x00cf, Exception -> 0x00cc, all -> 0x01d7 }
            java.lang.String r6 = r8.toString()     // Catch:{ all -> 0x01d7, all -> 0x01d7, IOException -> 0x00cf, Exception -> 0x00cc, all -> 0x01d7 }
            r7.<init>(r6)     // Catch:{ all -> 0x01d7, all -> 0x01d7, IOException -> 0x00cf, Exception -> 0x00cc, all -> 0x01d7 }
            r7.delete()     // Catch:{ all -> 0x01d7, all -> 0x01d7, IOException -> 0x00cf, Exception -> 0x00cc, all -> 0x01d7 }
            goto L_0x01d7
        L_0x00cc:
            r6 = move-exception
            r2 = 0
            goto L_0x00d3
        L_0x00cf:
            r6 = move-exception
            r2 = 0
            goto L_0x00ed
        L_0x00d2:
            r6 = move-exception
        L_0x00d3:
            java.lang.String r6 = r6.getMessage()     // Catch:{ SecurityException -> 0x00e9, all -> 0x01d7 }
            if (r6 == 0) goto L_0x01d7
            java.lang.String r7 = "start failed"
            boolean r7 = r6.contains(r7)     // Catch:{ SecurityException -> 0x00e9, all -> 0x01d7 }
            if (r7 != 0) goto L_0x00e9
            java.lang.String r7 = "setAudioSource failed"
            boolean r6 = r6.contains(r7)     // Catch:{ SecurityException -> 0x00e9, all -> 0x01d7 }
            if (r6 == 0) goto L_0x01d7
        L_0x00e9:
            r1 = r2
            goto L_0x01d7
        L_0x00ec:
            r6 = move-exception
        L_0x00ed:
            java.lang.String r7 = r6.getMessage()     // Catch:{ SecurityException -> 0x00e9, all -> 0x01d7 }
            if (r7 == 0) goto L_0x010c
            java.lang.String r7 = r6.getMessage()     // Catch:{ SecurityException -> 0x00e9, all -> 0x01d7 }
            java.lang.String r8 = "Permission deny"
            boolean r7 = r7.contains(r8)     // Catch:{ SecurityException -> 0x00e9, all -> 0x01d7 }
            if (r7 != 0) goto L_0x00e9
            java.lang.String r6 = r6.getMessage()     // Catch:{ SecurityException -> 0x00e9, all -> 0x01d7 }
            java.lang.String r7 = "Permission denied"
            boolean r6 = r6.contains(r7)     // Catch:{ SecurityException -> 0x00e9, all -> 0x01d7 }
            if (r6 == 0) goto L_0x010c
            goto L_0x00e9
        L_0x010c:
            java.lang.String r6 = android.os.Build.BRAND     // Catch:{ SecurityException -> 0x00e9, all -> 0x01d7 }
            java.lang.String r7 = io.dcloud.common.adapter.util.MobilePhoneModel.GIONEE     // Catch:{ SecurityException -> 0x00e9, all -> 0x01d7 }
            boolean r6 = r6.equalsIgnoreCase(r7)     // Catch:{ SecurityException -> 0x00e9, all -> 0x01d7 }
            if (r6 != 0) goto L_0x00e9
            goto L_0x01d7
        L_0x0118:
            java.lang.String r0 = "android.permission.ACCESS_COARSE_LOCATION"
            boolean r0 = r0.equals(r7)     // Catch:{ SecurityException -> 0x01cf, all -> 0x01d7 }
            if (r0 == 0) goto L_0x014e
            java.lang.String r0 = "location"
            java.lang.Object r0 = r6.getSystemService(r0)     // Catch:{ SecurityException -> 0x01cf, all -> 0x01d7 }
            android.location.LocationManager r0 = (android.location.LocationManager) r0     // Catch:{ SecurityException -> 0x01cf, all -> 0x01d7 }
            java.lang.String r3 = "gps"
            boolean r3 = r0.isProviderEnabled(r3)     // Catch:{ SecurityException -> 0x01cf, all -> 0x01d7 }
            java.lang.String r4 = "network"
            boolean r0 = r0.isProviderEnabled(r4)     // Catch:{ SecurityException -> 0x01cf, all -> 0x01d7 }
            java.lang.String r4 = "ZTE B880"
            java.lang.String r5 = android.os.Build.MODEL     // Catch:{ SecurityException -> 0x01cf, all -> 0x01d7 }
            boolean r4 = android.text.TextUtils.equals(r4, r5)     // Catch:{ SecurityException -> 0x01cf, all -> 0x01d7 }
            if (r3 != 0) goto L_0x0145
            if (r0 == 0) goto L_0x0141
            goto L_0x0145
        L_0x0141:
            if (r4 == 0) goto L_0x01cf
            goto L_0x01d7
        L_0x0145:
            int r6 = isMiui((android.app.Activity) r6, (java.lang.String) r7, (java.lang.String) r8)     // Catch:{ SecurityException -> 0x01cf, all -> 0x01d7 }
            r7 = -100
            if (r6 == r7) goto L_0x01d7
            return r6
        L_0x014e:
            java.lang.String r0 = "android.permission.WRITE_CONTACTS"
            boolean r0 = r0.equals(r7)     // Catch:{ SecurityException -> 0x01cf, all -> 0x01d7 }
            if (r0 == 0) goto L_0x0158
            goto L_0x01cf
        L_0x0158:
            java.lang.String r0 = "android.permission.SEND_SMS"
            boolean r0 = r0.equals(r7)     // Catch:{ SecurityException -> 0x01cf, all -> 0x01d7 }
            if (r0 == 0) goto L_0x0162
            goto L_0x01cf
        L_0x0162:
            java.lang.String r0 = "android.permission.CALL_PHONE"
            boolean r0 = r0.equals(r7)     // Catch:{ SecurityException -> 0x01cf, all -> 0x01d7 }
            if (r0 == 0) goto L_0x016b
            goto L_0x01cf
        L_0x016b:
            java.lang.String r0 = "android.permission.WRITE_EXTERNAL_STORAGE"
            boolean r0 = r0.equals(r7)     // Catch:{ SecurityException -> 0x01cf, all -> 0x01d7 }
            if (r0 == 0) goto L_0x01af
            java.io.File r7 = new java.io.File     // Catch:{ Exception -> 0x01aa, all -> 0x01d7 }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01aa, all -> 0x01d7 }
            r8.<init>()     // Catch:{ Exception -> 0x01aa, all -> 0x01d7 }
            java.lang.String r0 = android.os.Environment.DIRECTORY_DOWNLOADS     // Catch:{ Exception -> 0x01aa, all -> 0x01d7 }
            java.io.File r6 = r6.getExternalFilesDir(r0)     // Catch:{ Exception -> 0x01aa, all -> 0x01d7 }
            r8.append(r6)     // Catch:{ Exception -> 0x01aa, all -> 0x01d7 }
            java.lang.String r6 = "/temp.arm"
            r8.append(r6)     // Catch:{ Exception -> 0x01aa, all -> 0x01d7 }
            java.lang.String r6 = r8.toString()     // Catch:{ Exception -> 0x01aa, all -> 0x01d7 }
            r7.<init>(r6)     // Catch:{ Exception -> 0x01aa, all -> 0x01d7 }
            java.io.File r6 = r7.getParentFile()     // Catch:{ Exception -> 0x01aa, all -> 0x01d7 }
            boolean r6 = r6.exists()     // Catch:{ Exception -> 0x01aa, all -> 0x01d7 }
            if (r6 != 0) goto L_0x01a0
            java.io.File r6 = r7.getParentFile()     // Catch:{ Exception -> 0x01aa, all -> 0x01d7 }
            r6.mkdirs()     // Catch:{ Exception -> 0x01aa, all -> 0x01d7 }
        L_0x01a0:
            boolean r6 = r7.exists()     // Catch:{ Exception -> 0x01aa, all -> 0x01d7 }
            if (r6 != 0) goto L_0x01d7
            r7.createNewFile()     // Catch:{ Exception -> 0x01aa, all -> 0x01d7 }
            goto L_0x01d7
        L_0x01aa:
            r6 = move-exception
            r6.printStackTrace()     // Catch:{ SecurityException -> 0x01cf, all -> 0x01d7 }
            goto L_0x01cf
        L_0x01af:
            java.lang.String r0 = "android.permission.INSTALL_SHORTCUT"
            boolean r0 = r0.equals(r7)     // Catch:{ SecurityException -> 0x01cf, all -> 0x01d7 }
            if (r0 == 0) goto L_0x01be
            int r6 = io.dcloud.common.util.AppPermissionUtil.checkPermission(r6, r8)     // Catch:{ SecurityException -> 0x01cf, all -> 0x01d7 }
            if (r4 == r6) goto L_0x01cf
            goto L_0x01d7
        L_0x01be:
            java.lang.String r6 = "GALLERY"
            boolean r6 = r6.equals(r7)     // Catch:{ SecurityException -> 0x01cf, all -> 0x01d7 }
            if (r6 == 0) goto L_0x01c7
            goto L_0x01cf
        L_0x01c7:
            java.lang.String r6 = "NATIVE.JS"
            boolean r6 = r6.equals(r7)     // Catch:{ SecurityException -> 0x01cf, all -> 0x01d7 }
            if (r6 == 0) goto L_0x01d1
        L_0x01cf:
            r1 = -1
            goto L_0x01d7
        L_0x01d1:
            java.lang.String r6 = "PUSH"
            r6.equals(r7)     // Catch:{ SecurityException -> 0x01cf, all -> 0x01d7 }
            goto L_0x01cf
        L_0x01d7:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.util.PermissionUtil.trycatchGetPermission(android.app.Activity, java.lang.String, java.lang.String):int");
    }

    /* access modifiers changed from: private */
    public static void unregisterWebAppReStartEvent(IApp iApp, ISysEventListener iSysEventListener) {
        iApp.unregisterSysEventListener(iSysEventListener, ISysEventListener.SysEventType.onWebAppReStart);
    }

    public static synchronized void usePermission(Activity activity, final String str, String str2, int i, final Request request) {
        HashMap hashMap;
        synchronized (PermissionUtil.class) {
            String convert2SystemPermission = convert2SystemPermission(str2);
            if (checkSelfPermission(activity, convert2SystemPermission, request.getAppName()) == 0) {
                request.onGranted(str2);
            } else if (!useRejectedCache.containsKey(str) || i <= 0 || (hashMap = useRejectedCache.get(str)) == null || hashMap.isEmpty() || ((Integer) hashMap.get(str2)).intValue() < i) {
                useSystemPermission(activity, convert2SystemPermission, new Request() {
                    public void onDenied(String str) {
                        try {
                            if (PermissionUtil.useRejectedCache.containsKey(str)) {
                                HashMap hashMap = (HashMap) PermissionUtil.useRejectedCache.get(str);
                                if (hashMap == null) {
                                    hashMap = new HashMap();
                                }
                                if (hashMap.containsKey(str)) {
                                    hashMap.put(str, Integer.valueOf(((Integer) hashMap.get(str)).intValue() + 1));
                                } else {
                                    hashMap.put(str, 1);
                                }
                                PermissionUtil.useRejectedCache.put(str, hashMap);
                            } else {
                                HashMap hashMap2 = new HashMap();
                                hashMap2.put(str, 1);
                                PermissionUtil.useRejectedCache.put(str, hashMap2);
                            }
                        } catch (Exception unused) {
                        }
                        request.onDenied(str);
                    }

                    public void onGranted(String str) {
                        try {
                            if (PermissionUtil.useRejectedCache.containsKey(str)) {
                                ((HashMap) PermissionUtil.useRejectedCache.get(str)).remove(str);
                            }
                        } catch (Exception unused) {
                        }
                        request.onGranted(str);
                    }
                });
            } else {
                request.onDenied(str2);
            }
        }
    }

    private static int useStreamPermission(ShowDialogData showDialogData) {
        Activity activity = showDialogData.activity;
        String str = showDialogData.appid;
        String str2 = showDialogData.streamPerName;
        Request request = showDialogData.request;
        int checkStreamAppPermission = checkStreamAppPermission(activity, str, str2);
        if (checkStreamAppPermission == -1) {
            request.onDenied(str2);
        } else if (checkStreamAppPermission == 0) {
            request.onGranted(str2);
        } else if (checkStreamAppPermission == 1) {
            showStreamAppPermissionDialog(showDialogData);
        }
        return checkStreamAppPermission;
    }

    public static boolean useSystemPermission(Activity activity, String str, Request request) {
        boolean z = activity.getApplicationInfo().targetSdkVersion >= 23 && str != null && str.contains("android.permission") && Build.VERSION.SDK_INT >= 23;
        String convert2StreamPermission = convert2StreamPermission(str);
        if (z) {
            request.setRequestCode(getRequestCode());
            int checkSelfPermission = checkSelfPermission(activity, str, request.getAppName());
            if (checkSelfPermission == -1) {
                if (checkPermissionDenied(activity, str)) {
                    request.onDenied(convert2StreamPermission);
                    return true;
                } else if (str.equals("android.permission.ACCESS_COARSE_LOCATION")) {
                    requestSystemPermissions(activity, new String[]{str, "android.permission.ACCESS_FINE_LOCATION"}, request.getRequestCode(), request);
                    return false;
                } else {
                    requestSystemPermissions(activity, new String[]{str}, request.getRequestCode(), request);
                    return false;
                }
            } else if (checkSelfPermission != 0) {
                return false;
            } else {
                request.onGranted(convert2StreamPermission);
            }
        } else {
            request.onGranted(convert2StreamPermission);
        }
        return true;
    }

    public static void useSystemPermissions(Activity activity, String[] strArr, Request request) {
        boolean z = true;
        boolean z2 = activity.getApplicationInfo().targetSdkVersion >= 23 && strArr != null && strArr.length > 0 && Build.VERSION.SDK_INT >= 23;
        ArrayList arrayList = new ArrayList(Arrays.asList(strArr));
        if (z2) {
            request.setRequestCode(getRequestCode());
            ArrayList arrayList2 = new ArrayList();
            if (alwaysDeniedPer != null) {
                z = false;
            }
            for (int i = 0; i < arrayList.size(); i++) {
                String convert2SystemPermission = convert2SystemPermission((String) arrayList.get(i));
                if (checkSelfPermission(activity, convert2SystemPermission, request.getAppName()) == 0) {
                    arrayList2.add(convert2SystemPermission);
                    request.onGranted(convert2StreamPermission(convert2SystemPermission));
                } else if (checkPermissionDenied(activity, convert2SystemPermission) && !z) {
                    arrayList2.add(convert2SystemPermission);
                    request.onDenied(convert2StreamPermission(convert2SystemPermission));
                }
            }
            if (arrayList2.size() > 0) {
                arrayList.removeAll(arrayList2);
            }
            if (arrayList.size() > 0) {
                requestSystemPermissions(activity, (String[]) arrayList.toArray(new String[0]), request.getRequestCode(), request);
                return;
            }
            return;
        }
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            request.onGranted(convert2StreamPermission((String) arrayList.get(i2)));
        }
    }

    private static class ShowDialogData {
        Activity activity;
        String appName;
        String appid;
        boolean force;
        IApp mApp;
        Request request;
        String streamPerName;
        int tryTimes;

        ShowDialogData(Activity activity2, IApp iApp, String str, String str2, String str3, Request request2) {
            this(activity2, iApp, str, str2, str3);
            setRequestHandler(request2);
        }

        /* access modifiers changed from: package-private */
        public void setRequestHandler(Request request2) {
            this.request = request2;
        }

        ShowDialogData(Activity activity2, IApp iApp, String str, String str2, String str3) {
            this.tryTimes = 0;
            this.activity = activity2;
            this.mApp = iApp;
            this.streamPerName = str;
            this.appid = str2;
            this.appName = str3;
        }
    }

    public static void requestSystemPermissions(Activity activity, String[] strArr, int i, Request request, boolean z) {
        if (caseVersion(activity) && strArr != null) {
            HashMap hashMap = new HashMap();
            hashMap.put(request, strArr);
            sRequestCallBacks.put(Integer.valueOf(i), hashMap);
            ArrayList arrayList = new ArrayList();
            if (z) {
                int length = strArr.length;
                int i2 = 0;
                while (i2 < length) {
                    String str = strArr[i2];
                    if (!checkPermissionDenied(activity, str)) {
                        arrayList.add(str);
                        i2++;
                    } else if (request != null) {
                        request.onDenied(str);
                        return;
                    } else {
                        return;
                    }
                }
                if (arrayList.size() > 0) {
                    strArr = (String[]) arrayList.toArray(new String[0]);
                }
            }
            requestPermissions(activity, strArr, i);
        } else if (strArr != null) {
            try {
                for (String convert2StreamPermission : strArr) {
                    request.onGranted(convert2StreamPermission(convert2StreamPermission));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void showStreamAppPermissionDialog(ShowDialogData showDialogData) {
        String str;
        final ShowDialogData showDialogData2 = showDialogData;
        final Activity activity = showDialogData2.activity;
        final String str2 = showDialogData2.appid;
        String str3 = showDialogData2.appName;
        final String str4 = showDialogData2.streamPerName;
        final Request request = showDialogData2.request;
        Logger.e("Permission", "showStreamAppPermissionDialog streamPerName=" + str4 + ";count=" + sUseStreamAppPermissionDialogCount);
        PermissionData permissionData = sPermissionData.get(str4);
        if (permissionData == null) {
            request.onGranted(str4);
        } else if (continueShowStreamAppPermissionDialog(showDialogData)) {
            boolean z = true;
            int i = showDialogData2.tryTimes + 1;
            showDialogData2.tryTimes = i;
            boolean z2 = i == 1;
            final DCloudAlertDialog initDialogTheme = DialogUtil.initDialogTheme(activity, true);
            if (activity != null && (activity instanceof WebAppActivity)) {
                ((WebAppActivity) activity).recordDialog(initDialogTheme);
            }
            initDialogTheme.setCanceledOnTouchOutside(false);
            if (!z2) {
                if (Build.BRAND.equalsIgnoreCase(MobilePhoneModel.QiKU)) {
                    initDialogTheme.setMessage(activity.getString(R.string.dcloud_permissions_short_cut_tips2));
                }
                if (PMS_LOCATION.equalsIgnoreCase(str4)) {
                    initDialogTheme.setMessage(activity.getString(R.string.dcloud_permissions_geo_retry_tips));
                } else {
                    initDialogTheme.setMessage(StringUtil.format(activity.getString(R.string.dcloud_permissions_retry_tips), activity.getPackageManager().getApplicationLabel(activity.getApplicationInfo())));
                }
            } else if (Build.BRAND.equalsIgnoreCase(MobilePhoneModel.QiKU)) {
                initDialogTheme.setMessage(activity.getString(R.string.dcloud_permissions_short_cut_tips));
            } else if (!TextUtils.isEmpty(str3)) {
                initDialogTheme.setMessage(StringUtil.format(activity.getString(permissionData.messageId), str3));
            } else {
                initDialogTheme.setMessage(StringUtil.format(activity.getString(permissionData.messageId), "App"));
            }
            CheckBox checkBox = null;
            if (permissionData.checkbox != -1 && z2) {
                checkBox = new CheckBox(activity);
                checkBox.setText(R.string.dcloud_permissions_checkbox_close_tips);
                checkBox.setTextColor(-65536);
                if (permissionData.checkbox != 1) {
                    z = false;
                }
                checkBox.setChecked(z);
                int deivceSuitablePixel = getDeivceSuitablePixel(activity, 20);
                initDialogTheme.setView(checkBox, deivceSuitablePixel, deivceSuitablePixel, 0, 0);
            }
            final CheckBox checkBox2 = checkBox;
            AnonymousClass3 r14 = new ISysEventListener() {
                public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
                    Logger.e("Permission", "unregisterSysEventListener registerSysEventListener pEventType=" + sysEventType);
                    if (sysEventType != ISysEventListener.SysEventType.onWebAppReStart) {
                        return false;
                    }
                    initDialogTheme.dismiss();
                    PermissionUtil.unregisterWebAppReStartEvent(showDialogData2.mApp, this);
                    try {
                        request.onDenied(str4);
                        PermissionUtil.showStreamAppPermissionDialog();
                        return false;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            };
            if (showDialogData2.mApp != null) {
                Logger.e("Permission", "showStreamAppPermissionDialog registerSysEventListener");
                showDialogData2.mApp.registerSysEventListener(r14, ISysEventListener.SysEventType.onWebAppReStart);
            }
            final DCloudAlertDialog dCloudAlertDialog = initDialogTheme;
            final ShowDialogData showDialogData3 = showDialogData;
            final AnonymousClass3 r4 = r14;
            final Activity activity2 = activity;
            String str5 = PMS_LOCATION;
            final String str6 = str4;
            AnonymousClass3 r17 = r14;
            final DCloudAlertDialog dCloudAlertDialog2 = initDialogTheme;
            final Request request2 = request;
            AnonymousClass4 r1 = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dCloudAlertDialog.dismiss();
                    IApp iApp = showDialogData3.mApp;
                    if (iApp != null) {
                        PermissionUtil.unregisterWebAppReStartEvent(iApp, r4);
                    }
                    if (i == -1) {
                        CheckBox checkBox = checkBox2;
                        PermissionUtil.putStreamAppPermission(activity2, str2, str6, (checkBox == null ? true : checkBox.isChecked()) ^ true ? 1 : 0);
                        request2.onGranted(str6);
                    } else if (i == -2) {
                        CheckBox checkBox2 = checkBox2;
                        if (checkBox2 != null && checkBox2.isChecked()) {
                            PermissionUtil.putStreamAppPermission(activity2, str2, str6, -1);
                        }
                        request2.onDenied(str6);
                    }
                    PermissionUtil.showStreamAppPermissionDialog();
                }
            };
            String string = activity.getString(z2 ? R.string.dcloud_common_no_allow : R.string.dcloud_common_cancel);
            if (z2) {
                str = activity.getString(R.string.dcloud_common_allow);
            } else if (Build.BRAND.equalsIgnoreCase(MobilePhoneModel.QiKU) || str5.equalsIgnoreCase(str4)) {
                str = activity.getString(R.string.dcloud_permissions_reopened);
            } else {
                str = activity.getString(R.string.dcloud_permissions_reauthorization);
            }
            dCloudAlertDialog2.setButton(-2, string, r1);
            dCloudAlertDialog2.setButton(-1, str, r1);
            final DCloudAlertDialog dCloudAlertDialog3 = dCloudAlertDialog2;
            final ShowDialogData showDialogData4 = showDialogData;
            final AnonymousClass3 r42 = r17;
            final Request request3 = request;
            final String str7 = str4;
            dCloudAlertDialog2.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    if (keyEvent.getAction() != 1 || i != 4) {
                        return false;
                    }
                    dCloudAlertDialog3.dismiss();
                    IApp iApp = showDialogData4.mApp;
                    if (iApp != null) {
                        PermissionUtil.unregisterWebAppReStartEvent(iApp, r42);
                    }
                    request3.onDenied(str7);
                    PermissionUtil.showStreamAppPermissionDialog();
                    return true;
                }
            });
            try {
                dCloudAlertDialog2.show();
                dCloudAlertDialog2.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                        Activity activity = activity;
                        if (activity != null && (activity instanceof WebAppActivity)) {
                            ((WebAppActivity) activity).removeFromRecord(dCloudAlertDialog2);
                        }
                    }
                });
            } catch (Exception e) {
                Logger.e("ian", "try dialog");
                e.printStackTrace();
            }
        }
    }

    public static int checkSelfPermission(Activity activity, String str) {
        if (activity == null || str == null) {
            return 0;
        }
        return ((Integer) PlatformUtil.invokeMethod(activity.getClass().getName(), "checkSelfPermission", activity, new Class[]{str.getClass()}, new Object[]{str})).intValue();
    }

    public static void usePermission(Activity activity, String str, final Request request) {
        useSystemPermission(activity, convert2SystemPermission(str), new Request() {
            public void onDenied(String str) {
                Request.this.onDenied(str);
            }

            public void onGranted(String str) {
                Request.this.onGranted(str);
            }
        });
    }

    private static boolean isMiui(Activity activity, String str, Request request) {
        String property = System.getProperty("http.agent");
        if (!TextUtils.isEmpty(property) ? !property.toLowerCase(Locale.ENGLISH).contains("miui") : !Build.BRAND.contains("Xiaomi")) {
            return false;
        }
        int requestCode = getRequestCode();
        Intent intent = new Intent();
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
        intent.putExtra("extra_pkgname", activity.getPackageName());
        try {
            activity.startActivityForResult(intent, requestCode);
            saveCallabckData(activity, str, request, requestCode);
            return true;
        } catch (ActivityNotFoundException unused) {
            intent.setComponent((ComponentName) null);
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            try {
                activity.startActivityForResult(intent, requestCode);
                saveCallabckData(activity, str, request, requestCode);
                return true;
            } catch (ActivityNotFoundException unused2) {
                intent.setComponent((ComponentName) null);
                intent.setPackage("com.android.Setting");
                activity.startActivityForResult(intent, requestCode);
                saveCallabckData(activity, str, request, requestCode);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return true;
        }
    }

    static class SafeCenter {
        private static ArrayList<Item> datas = new ArrayList<>();

        static class Item {
            String action;
            String clsName;
            String extParamName;
            String pname;

            Item(String str, String str2, String str3, String str4) {
                this.pname = str;
                this.clsName = str2;
                this.extParamName = str3;
                this.action = str4;
            }
        }

        SafeCenter() {
        }

        /* access modifiers changed from: private */
        public static void goSafeCenter(Activity activity) {
            init(activity);
            PackageManager packageManager = activity.getPackageManager();
            for (int i = 0; i < datas.size(); i++) {
                Intent intent = new Intent();
                Item item = datas.get(i);
                try {
                    if (packageManager.getPackageInfo(item.pname, 0) != null) {
                        if (!TextUtils.isEmpty(item.clsName)) {
                            intent.setClassName(item.pname, item.clsName);
                        } else if (!TextUtils.isEmpty(item.pname)) {
                            intent.setPackage(item.pname);
                        }
                        if (!TextUtils.isEmpty(item.action)) {
                            intent.setAction(item.action);
                        }
                        if (!TextUtils.isEmpty(item.extParamName)) {
                            intent.putExtra(item.extParamName, activity.getPackageName());
                        }
                        try {
                            intent.setFlags(268435456);
                            activity.startActivity(intent);
                            Logger.i("Permission", "successful " + Build.MODEL + "intent=" + intent);
                        } catch (ActivityNotFoundException e) {
                            Logger.e("Permission", "ActivityNotFoundException =" + e);
                            e.printStackTrace();
                        } catch (Exception e2) {
                            Logger.e("Permission", "Exception =" + e2);
                        }
                    }
                } catch (PackageManager.NameNotFoundException unused) {
                }
            }
        }

        static void init(Context context) {
            if (datas.isEmpty()) {
                try {
                    JSONArray jSONArray = new JSONArray(new String(IOUtil.toString(new FileInputStream(new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/temp.j")))));
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject optJSONObject = jSONArray.optJSONObject(i);
                        String optString = optJSONObject.optString(ContextChain.TAG_PRODUCT);
                        if (!TextUtils.isEmpty(optString)) {
                            datas.add(new Item(optString, optJSONObject.optString(c.a), optJSONObject.optString("e"), optJSONObject.optString("a")));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (datas.isEmpty()) {
                    datas.add(new Item("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity", "extra_pkgname", (String) null));
                    datas.add(new Item("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity", "extra_pkgname", (String) null));
                    datas.add(new Item("com.meizu.safe", "com.meizu.safe.security.AppSecActivity", "packageName", (String) null));
                    datas.add(new Item("com.aliyun.mobile.permission", "com.aliyun.mobile.permission.ExternalAppDetailActivity", "packageName", (String) null));
                    datas.add(new Item("com.iqoo.secure", "com.iqoo.secure.MainActivity", "packageName", (String) null));
                    datas.add(new Item("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity", "package", (String) null));
                    datas.add(new Item("com.mediatek.security", "com.mediatek.security.ui.PermissionControlPageActivity", "package", (String) null));
                    datas.add(new Item("com.yulong.android.launcher3", "com.yulong.android.launcher3.LauncherSettingsActivity", "package", (String) null));
                    datas.add(new Item("com.android.settings", "com.android.settings.Settings$ManageApplicationsActivity", "package", (String) null));
                    datas.add(new Item((String) null, (String) null, "package", "android.settings.MANAGE_APPLICATIONS_SETTINGS"));
                }
            }
        }

        /* access modifiers changed from: private */
        public static boolean goSafeCenter(Activity activity, String str, Request request) {
            Activity activity2 = activity;
            String str2 = str;
            Request request2 = request;
            int requestCode = PermissionUtil.getRequestCode();
            if (PermissionUtil.PMS_LOCATION.equalsIgnoreCase(str2)) {
                LocationManager locationManager = (LocationManager) activity2.getSystemService("location");
                boolean isProviderEnabled = locationManager.isProviderEnabled("gps");
                boolean isProviderEnabled2 = locationManager.isProviderEnabled("network");
                if (!isProviderEnabled && !isProviderEnabled2) {
                    Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
                    try {
                        activity2.startActivity(intent);
                        PermissionUtil.saveCallabckData(activity2, str2, request2, requestCode);
                        Logger.i("Permission", "successful " + Build.MODEL + "intent=" + intent);
                        return true;
                    } catch (ActivityNotFoundException e) {
                        Logger.e("Permission", "ActivityNotFoundException =" + e);
                        e.printStackTrace();
                    } catch (Exception e2) {
                        Logger.e("Permission", "Exception =" + e2);
                    }
                }
            }
            init(activity);
            PackageManager packageManager = activity.getPackageManager();
            int i = 0;
            int i2 = 0;
            while (i2 < datas.size()) {
                Intent intent2 = new Intent();
                Item item = datas.get(i2);
                try {
                    if (packageManager.getPackageInfo(item.pname, i) == null) {
                        continue;
                        i2++;
                        i = 0;
                    } else {
                        if (!TextUtils.isEmpty(item.clsName)) {
                            intent2.setClassName(item.pname, item.clsName);
                        } else if (!TextUtils.isEmpty(item.pname)) {
                            intent2.setPackage(item.pname);
                        }
                        if (!TextUtils.isEmpty(item.action)) {
                            intent2.setAction(item.action);
                        }
                        if (!TextUtils.isEmpty(item.extParamName)) {
                            intent2.putExtra(item.extParamName, activity.getPackageName());
                        }
                        try {
                            activity2.startActivityForResult(intent2, requestCode);
                            PermissionUtil.saveCallabckData(activity2, str2, request2, requestCode);
                            Logger.i("Permission", "successful " + Build.MODEL + "intent=" + intent2);
                            return true;
                        } catch (ActivityNotFoundException e3) {
                            Logger.e("Permission", "ActivityNotFoundException =" + e3);
                            e3.printStackTrace();
                        } catch (Exception e4) {
                            Logger.e("Permission", "Exception =" + e4);
                        }
                    }
                } catch (PackageManager.NameNotFoundException unused) {
                }
            }
            return true;
        }
    }
}
