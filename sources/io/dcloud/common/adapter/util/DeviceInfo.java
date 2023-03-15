package io.dcloud.common.adapter.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.UiModeManager;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatDelegate;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.common.Constants;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.utils.tools.TimeCalculator;
import io.dcloud.application.DCLoudApplicationImpl;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.ExifInterface;
import io.dcloud.common.util.NetworkTypeUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import io.dcloud.common.util.TelephonyUtil;
import io.dcloud.common.util.language.LanguageUtil;
import io.dcloud.feature.internal.sdk.SDK;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;
import org.json.JSONObject;

public class DeviceInfo {
    private static final String CDMA_DATA_NETWORK = "cdma";
    private static String CONNECTION_CELL2G = "4";
    private static String CONNECTION_CELL3G = "5";
    private static String CONNECTION_CELL4G = "6";
    private static String CONNECTION_CELL5G = "7";
    private static String CONNECTION_ETHERNET = ExifInterface.GPS_MEASUREMENT_2D;
    private static String CONNECTION_UNKNOW = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT;
    private static final String DEFAULT_DATA_NETWORK = "default_data_network";
    public static float DEFAULT_FONT_SIZE = 0.0f;
    public static String DEVICESTATUS_JS = null;
    public static final String FILE_PROTOCOL = "file://";
    private static final String GSM_DATA_NETWORK = "gsm";
    public static int HARDWAREACCELERATED_VIEW = 1;
    public static int HARDWAREACCELERATED_WINDOW = 0;
    public static final String HTTPS_PROTOCOL = "https://";
    public static final String HTTP_PROTOCOL = "http://";
    private static String NETTYPE_NONE = "1";
    private static String NETTYPE_WIFI = ExifInterface.GPS_MEASUREMENT_3D;
    private static final String NONE_DATA_NETWORK = "none";
    public static final int OSTYPE_ANDROID = 0;
    public static final int OSTYPE_LEOS10 = 4;
    public static final int OSTYPE_OMS10 = 3;
    public static final int OSTYPE_OMS15 = 2;
    public static final int OSTYPE_OMS20 = 1;
    private static final String SAVED_DATA_NETWORK = "saved_data_network";
    private static final String TAG = "DeviceInfo";
    public static float dpiX;
    public static float dpiY;
    public static volatile boolean isIMEShow = false;
    public static boolean isPrivateDirectory = false;
    public static boolean isVolumeButtonEnabled = true;
    public static String mAppAuthorities;
    public static String oaids;
    public static int osType = 0;
    private static final String[] rootRelatedDirs = {"/su", "/su/bin/su", "/sbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/data/local/su", "/system/xbin/su", "/system/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/system/bin/cufsdosck", "/system/xbin/cufsdosck", "/system/bin/cufsmgr", "/system/xbin/cufsmgr", "/system/bin/cufaevdd", "/system/xbin/cufaevdd", "/system/bin/conbb", "/system/xbin/conbb"};
    public static Context sApplicationContext = null;
    public static String sBaseFsCachePath;
    public static String sBaseFsRootFullPath;
    public static String sBaseFsRootPath;
    public static String sBaseResRootFullPath;
    public static String sBaseResRootPathName;
    public static String sBrand = Build.BRAND;
    public static String sCacheRootDir;
    private static GsmCellLocation sCellLocation = null;
    static ConnectivityManager sConnectMgr = null;
    public static int sCoreNums = -1;
    public static String sDeftDataNetwork = GSM_DATA_NETWORK;
    public static float sDensity;
    public static String sDeviceRootDir;
    public static int sDeviceSdkVer = Build.VERSION.SDK_INT;
    public static String sIMEI;
    public static String sIMSI;
    public static int sInputMethodHeight = 0;
    public static String sLanguage = LanguageUtil.getDeviceDefLocalLanguage();
    public static String sMAC;
    public static String sModel = Build.MODEL;
    public static boolean sNetWorkInited = false;
    public static String sPackageName = null;
    public static Paint sPaint;
    public static String sPrivateDir = null;
    public static String sPrivateExternalDir = null;
    public static String sPublicDCIMDir = null;
    public static String sPublicDocumentsDir = null;
    public static String sPublicDownloadDir = null;
    public static String sPublicMoviesDir = null;
    public static String sPublicMusicDir = null;
    public static String sPublicPicturesDir = null;
    public static String sPublicRingtonesDir = null;
    public static char sSeparatorChar;
    public static String sSimOperator = null;
    public static int sStatusBarHeight = 0;
    public static JSONObject sSystemInfo;
    public static long sTotalMem = -1;
    public static String sVendor = Build.MANUFACTURER;
    public static String sVersion_release = Build.VERSION.RELEASE;

    static {
        char c = File.separatorChar;
        sSeparatorChar = c;
        sBaseResRootPathName = String.valueOf(c);
        Paint paint = new Paint();
        sPaint = paint;
        DEFAULT_FONT_SIZE = paint.getTextSize();
    }

    public static boolean blueToothEnable(Context context) throws Exception {
        if (Build.VERSION.SDK_INT < 23 || context.checkSelfPermission("android.permission.BLUETOOTH") != -1) {
            return ((BluetoothManager) context.getSystemService("bluetooth")).getAdapter().isEnabled();
        }
        throw new Exception();
    }

    public static boolean checkCoverLoadApp() {
        boolean z;
        String bundleData = SP.getBundleData(BaseInfo.PDR, "last_app_modify_date");
        long lastModified = new File(sApplicationContext.getPackageCodePath()).lastModified();
        Logger.d(TAG, "old_app_modify_date=" + bundleData);
        if (!PdrUtil.isEquals(bundleData, String.valueOf(lastModified))) {
            SP.setBundleData(sApplicationContext, BaseInfo.PDR, "last_app_modify_date", String.valueOf(lastModified));
            bundleData = Logger.generateTimeStamp(Logger.TIMESTAMP_YYYY_MM_DD_HH_MM_SS_SSS, new Date(lastModified));
            Logger.d(TAG, "new_app_modify_date=" + lastModified);
            z = true;
        } else {
            z = false;
        }
        Logger.d(TAG, "App Modify Date=" + bundleData + ";_ret=" + z);
        return z;
    }

    public static void closeHardwareAccelerated(Activity activity, int i, Object obj) {
        Window window = null;
        if (i == HARDWAREACCELERATED_WINDOW) {
            Window window2 = (Window) obj;
            if (window2 != null) {
                window = window2;
            } else if (activity != null) {
                window = activity.getWindow();
            }
            if (window != null) {
                window.clearFlags(16777216);
            }
        } else if (i == HARDWAREACCELERATED_VIEW && sDeviceSdkVer >= 11) {
            ((View) obj).setLayerType(1, (Paint) null);
        }
    }

    public static String deviceOrientation(Context context) {
        int i = context.getResources().getConfiguration().orientation;
        if (i == 1) {
            return "portrait";
        }
        return i == 2 ? "landscape" : "";
    }

    public static long getAvailMemory() {
        Context context = sApplicationContext;
        if (context == null) {
            return 0;
        }
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    public static String getBuildValue(String str) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getDeclaredMethod("get", new Class[]{String.class}).invoke(cls, new Object[]{str});
        } catch (Exception unused) {
            return null;
        }
    }

    public static String getCurrentAPN() {
        return NetworkTypeUtil.getCurrentAPN(DCLoudApplicationImpl.self().getContext().getApplicationContext());
    }

    public static int getDeivceSuitablePixel(Activity activity, int i) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return (int) (((float) i) * displayMetrics.density);
    }

    public static String getDevicestatus_js(IApp iApp) {
        DEVICESTATUS_JS = "(function(p){p.device.imei='%s';p.device.uuid='%s';p.device.imsi=['%s'];p.device.model='%s';p.device.vendor='%s';p.os.language='%s';p.os.version='%s';p.os.name='%s';p.os.vendor='%s';})(((window.__html5plus__&&__html5plus__.isReady)?__html5plus__:(navigator.plus&&navigator.plus.isReady)?navigator.plus:window.plus));";
        String updateIMEI = TelephonyUtil.updateIMEI(iApp.getActivity());
        String format = StringUtil.format(DEVICESTATUS_JS, updateIMEI, updateIMEI, sIMSI, sModel, sVendor, sLanguage, sVersion_release, TimeCalculator.PLATFORM_ANDROID, "Google");
        DEVICESTATUS_JS = format;
        return format;
    }

    @Deprecated
    public static String getMac(Context context) {
        try {
            sMAC = TelephonyUtil.getWifiData(context);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return sMAC;
    }

    public static String getNetWorkType() {
        String str = NETTYPE_NONE;
        ConnectivityManager connectivityManager = sConnectMgr;
        if (connectivityManager == null || connectivityManager.getActiveNetworkInfo() == null) {
            return str;
        }
        String str2 = CONNECTION_UNKNOW;
        if (sConnectMgr.getActiveNetworkInfo().getType() == 1) {
            return NETTYPE_WIFI;
        }
        if (sConnectMgr.getActiveNetworkInfo().getType() != 0) {
            return str2;
        }
        int subtype = sConnectMgr.getActiveNetworkInfo().getSubtype();
        switch (subtype) {
            case 1:
            case 2:
            case 4:
            case 7:
                return CONNECTION_CELL2G;
            case 3:
            case 8:
                return CONNECTION_CELL3G;
            case 5:
            case 6:
            case 12:
            case 14:
                return CONNECTION_CELL3G;
            case 9:
            case 10:
            case 11:
            case 13:
            case 15:
                return CONNECTION_CELL4G;
            case 17:
            case 18:
                return CONNECTION_CELL3G;
            case 20:
                return CONNECTION_CELL5G;
            default:
                return "" + subtype;
        }
    }

    public static int getNumCores() {
        int i = sCoreNums;
        if (i != -1) {
            return i;
        }
        try {
            File[] listFiles = new File("/sys/devices/system/cpu/").listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return Pattern.matches("cpu[0-9]", file.getName());
                }
            });
            sCoreNums = listFiles.length;
            return listFiles.length;
        } catch (Exception unused) {
            return 1;
        }
    }

    public static String getPlusCache() {
        return "window.plus.cache = navigator.plus.cache = (function(mkey){return {clear : function(clearCB){var callbackid = mkey.helper.callbackid( function(args){if ( clearCB ) {clearCB()};}, null);mkey.exec('Cache', 'clear', [callbackId]);},calculate : function(calculateCB){var callbackid = mkey.helper.callbackid( function(args){if ( calculateCB ) {calculateCB(args)};}, null);mkey.exec('Cache', 'calculate', [callbackid]);},setMaxSize : function (size) {mkey.exec('Cache', 'setMaxSize', [size]);}};})(window.__Mkey__);";
    }

    public static int getStatusHeight(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= 29) {
                Resources resources = context.getResources();
                int identifier = resources.getIdentifier("status_bar_height", "dimen", WXEnvironment.OS);
                if (identifier > 0) {
                    return resources.getDimensionPixelSize(identifier);
                }
                return -1;
            }
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            return context.getResources().getDimensionPixelSize(Integer.parseInt(cls.getField("status_bar_height").get(cls.newInstance()).toString()));
        } catch (Exception e) {
            Logger.e(TAG, "getStatusHeight --" + e.getMessage());
        }
    }

    public static String getSystemUIModeType(Activity activity) {
        switch (((UiModeManager) activity.getSystemService("uimode")).getCurrentModeType()) {
            case 0:
                return Constants.Name.UNDEFINED;
            case 1:
                return isTablet(activity) ? "pad" : "phone";
            case 2:
                return "pc";
            case 3:
                return "car";
            case 4:
                return "tv";
            case 5:
                return "appliance";
            case 6:
                return "watch";
            case 7:
                return "vr";
            default:
                return "unknown";
        }
    }

    public static long getTotalMemory() {
        long j = sTotalMem;
        if (j != -1) {
            return j;
        }
        long j2 = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 8192);
            j2 = (long) Integer.valueOf(bufferedReader.readLine().split("\\s+")[1]).intValue();
            bufferedReader.close();
            sTotalMem = j2;
            return j2;
        } catch (Exception unused) {
            return j2;
        }
    }

    public static String getUpdateIMSI() {
        String imsi = TelephonyUtil.getIMSI(sApplicationContext);
        sIMSI = imsi;
        if (imsi == null) {
            sIMSI = "";
        }
        return sIMSI;
    }

    public static boolean hasRootPrivilege() {
        boolean z;
        String[] strArr = rootRelatedDirs;
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                z = false;
                break;
            } else if (new File(strArr[i]).exists()) {
                z = true;
                break;
            } else {
                i++;
            }
        }
        String str = Build.TAGS;
        if ((str == null || !str.contains("test-keys")) && !z) {
            return false;
        }
        return true;
    }

    public static void hideIME(View view) {
        IBinder windowToken;
        Context context = sApplicationContext;
        if (context != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService("input_method");
            if (view != null && (windowToken = view.getWindowToken()) != null) {
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
            }
        }
    }

    public static void init(Context context) {
        sDeviceSdkVer = Build.VERSION.SDK_INT;
        String str = Build.MODEL;
        sModel = str;
        if ("OMAP_SS".equals(str)) {
            osType = 1;
        } else if ("OMS1_5".equals(sModel)) {
            osType = 2;
        } else if ("generic".equals(sModel)) {
            osType = 3;
        }
        mAppAuthorities = context.getPackageName() + ".dc.fileprovider";
        sBrand = Build.BRAND;
        sVendor = Build.MANUFACTURER;
        sLanguage = LanguageUtil.getDeviceDefLocalLanguage();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        sDensity = displayMetrics.density;
        dpiX = displayMetrics.xdpi;
        dpiY = displayMetrics.ydpi;
        sConnectMgr = (ConnectivityManager) context.getSystemService("connectivity");
        sStatusBarHeight = getStatusHeight(context);
    }

    public static void initAppDir(Context context) {
        if (TextUtils.isEmpty(sPrivateExternalDir)) {
            try {
                File externalCacheDir = context.getExternalCacheDir();
                if (externalCacheDir == null) {
                    sPrivateExternalDir = Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + context.getPackageName();
                } else {
                    sPrivateExternalDir = externalCacheDir.getParent();
                }
                sPrivateDir = context.getFilesDir().getParent();
            } catch (Exception unused) {
            }
            sPublicDocumentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath();
            sPublicDCIMDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath();
            sPublicDownloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            sPublicMoviesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getPath();
            sPublicMusicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getPath();
            sPublicPicturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
            sPublicRingtonesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES).getPath();
        }
    }

    public static void initBaseFsRootPath() {
        initPath(sApplicationContext);
    }

    public static void initGsmCdmaCell() {
        if (!sNetWorkInited && !AppRuntime.hasPrivacyForNotShown(sApplicationContext)) {
            String string = Settings.System.getString(sApplicationContext.getContentResolver(), DEFAULT_DATA_NETWORK);
            sDeftDataNetwork = string;
            if (string == null) {
                sDeftDataNetwork = GSM_DATA_NETWORK;
            }
            Logger.i("DefaultDataNetwork：", sDeftDataNetwork);
            TelephonyManager telephonyManager = (TelephonyManager) sApplicationContext.getSystemService("phone");
            int phoneType = telephonyManager.getPhoneType();
            sIMEI = TelephonyUtil.getIMEI(sApplicationContext, false);
            sIMSI = TelephonyUtil.getIMSI(sApplicationContext);
            sSimOperator = telephonyManager.getSimOperator();
            if ("none".equals(sDeftDataNetwork)) {
                sDeftDataNetwork = GSM_DATA_NETWORK;
                if (phoneType == 1) {
                    sCellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
                }
            }
            if (sIMEI == null) {
                sIMEI = "";
            }
            if (sIMSI == null) {
                sIMSI = "";
            }
            sNetWorkInited = true;
        }
    }

    public static void initPath(Context context) {
        initPath(context, true);
    }

    private static String intToIp(int i) {
        return (i & 255) + Operators.DOT_STR + ((i >> 8) & 255) + Operators.DOT_STR + ((i >> 16) & 255) + Operators.DOT_STR + ((i >> 24) & 255);
    }

    public static Boolean isAppNightMode(Context context) {
        if (AppCompatDelegate.getDefaultNightMode() == 2) {
            return Boolean.TRUE;
        }
        boolean z = true;
        if (AppCompatDelegate.getDefaultNightMode() == 1) {
            return Boolean.FALSE;
        }
        if (((UiModeManager) context.getSystemService("uimode")).getNightMode() != 2) {
            z = false;
        }
        return Boolean.valueOf(z);
    }

    public static boolean isOMS() {
        int i = osType;
        return i == 1 || i == 2 || i == 3;
    }

    public static boolean isSDcardExists() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    private static boolean isStreamMode() {
        try {
            Class.forName("io.dcloud.appstream.StreamAppMainActivity");
            return true;
        } catch (ClassNotFoundException e) {
            Logger.i(TAG, "e.getMessage" + e.getMessage());
            return false;
        }
    }

    public static Boolean isSystemNightMode(Activity activity) {
        return Boolean.valueOf(((UiModeManager) activity.getSystemService("uimode")).getNightMode() == 2);
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
    }

    public static boolean locationEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService("location");
        if (locationManager == null) {
            return false;
        }
        return locationManager.isLocationEnabled();
    }

    public static void openHardwareAccelerated(Activity activity, int i, Object obj) {
        Window window = null;
        if (i == HARDWAREACCELERATED_WINDOW) {
            Window window2 = (Window) obj;
            if (window2 != null) {
                window = window2;
            } else if (activity != null) {
                window = activity.getWindow();
            }
            if (window != null) {
                window.setFlags(16777216, 16777216);
            }
        } else if (i == HARDWAREACCELERATED_VIEW && sDeviceSdkVer >= 11) {
            ((View) obj).setLayerType(2, (Paint) null);
        }
    }

    public static void showIME(View view) {
        showIME(view, false);
    }

    public static boolean startsWithSdcard(String str) {
        return str.startsWith(sDeviceRootDir) || str.startsWith("/sdcard/") || str.startsWith("mnt/sdcard/") || str.startsWith(sCacheRootDir);
    }

    public static void updateIMEI() {
        sIMEI = TelephonyUtil.updateIMEI(sApplicationContext);
    }

    public static void updatePath(boolean z) {
        sBaseFsRootFullPath = FILE_PROTOCOL + sBaseFsRootPath;
        sBaseResRootFullPath = SDK.ANDROID_ASSET;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("sPackageName=" + sPackageName);
        stringBuffer.append(";\n");
        stringBuffer.append("sDeviceRootDir=" + sDeviceRootDir);
        stringBuffer.append(";\n");
        stringBuffer.append("sBaseFsRootPath=" + sBaseFsRootPath);
        stringBuffer.append(";\n");
        stringBuffer.append("sBaseFsRootFullPath=" + sBaseFsRootFullPath);
        stringBuffer.append(";\n");
        stringBuffer.append("sBaseResRootFullPath=" + sBaseResRootFullPath);
        stringBuffer.append(";\n");
        Logger.d(TAG, stringBuffer.toString());
        BaseInfo.updateBaseInfo(z);
    }

    public static void updateStatusBarHeight(Activity activity) {
        if (sStatusBarHeight == 0) {
            Rect rect = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            int i = rect.top;
            sStatusBarHeight = i;
            if (i <= 0) {
                sStatusBarHeight = getStatusHeight(activity);
            }
        }
    }

    public static boolean wifiEnable(Context context) {
        return ((WifiManager) context.getApplicationContext().getSystemService("wifi")).getWifiState() == 3;
    }

    public static void initPath(Context context, boolean z) {
        AndroidResources.initAndroidResources(context);
        sApplicationContext = context.getApplicationContext();
        String packageName = context.getPackageName();
        sPackageName = packageName;
        if (sDeviceRootDir == null) {
            boolean equals = packageName.equals("io.dcloud.HBuilder");
            boolean isSDcardExists = isSDcardExists();
            boolean z2 = BaseInfo.ISDEBUG || DHFile.hasFile();
            if (equals) {
                z2 = true;
            }
            if (z2 || SDK.isUniMP) {
                z = true;
            }
            if (z) {
                initAppDir(context);
            }
            String str = sBaseFsRootPath;
            if (isSDcardExists) {
                if (str == null) {
                    try {
                        context.getExternalCacheDir();
                    } catch (Exception unused) {
                    }
                    if (z) {
                        sDeviceRootDir = Environment.getExternalStorageDirectory().getPath();
                        str = sDeviceRootDir + "/Android/data/" + sPackageName + sSeparatorChar + "";
                    }
                }
                sBaseFsRootPath = str;
                if (z2) {
                    sCacheRootDir = sDeviceRootDir;
                    sBaseFsCachePath = str;
                } else {
                    isPrivateDirectory = true;
                    sCacheRootDir = context.getFilesDir().getParent() + sSeparatorChar;
                    sBaseFsCachePath = context.getFilesDir().getAbsolutePath() + sSeparatorChar;
                }
            } else {
                BaseInfo.ISDEBUG = false;
                isPrivateDirectory = true;
                String str2 = context.getFilesDir().getParent() + sSeparatorChar;
                sDeviceRootDir = str2;
                sCacheRootDir = str2;
                String str3 = sDeviceRootDir + "";
                sBaseFsRootPath = str3;
                sBaseFsCachePath = str3;
            }
            updatePath(z);
        }
    }

    public static void showIME(final View view, final boolean z) {
        if (sApplicationContext == null) {
            return;
        }
        if (view == null || !(view instanceof EditText)) {
            new Timer().schedule(new TimerTask() {
                public void run() {
                    Context context;
                    View view = view;
                    if (view != null && z && !view.hasFocus()) {
                        view.requestFocus();
                    }
                    if (!DeviceInfo.isIMEShow && (context = DeviceInfo.sApplicationContext) != null) {
                        ((InputMethodManager) context.getSystemService("input_method")).toggleSoftInput(0, 2);
                    }
                }
            }, 250);
        } else {
            new Timer().schedule(new TimerTask() {
                public void run() {
                    if (view != null) {
                        ((InputMethodManager) DeviceInfo.sApplicationContext.getSystemService("input_method")).showSoftInput(view, 0);
                    }
                }
            }, 100);
        }
    }
}
