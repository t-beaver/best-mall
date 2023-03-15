package io.dcloud.common.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.webkit.URLUtil;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import com.taobao.weex.common.Constants;
import com.taobao.weex.performance.WXInstanceApm;
import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.io.UnicodeInputStream;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.adapter.util.UEH;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.util.XmlUtil;
import io.dcloud.feature.internal.sdk.SDK;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class BaseInfo {
    private static String APPS_NAME = "apps/";
    public static String APP_DB_DATA = "dbdata/";
    public static String APP_JSDATA = "jsdata/";
    public static String APP_WEB_CHACHE = "webcache/";
    public static String APP_WWW_FS_DIR = "www/";
    public static boolean AuxiliaryInput = false;
    private static final X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");
    public static boolean ISAMU = false;
    public static boolean ISDEBUG = false;
    public static String PDR = "pdr";
    public static String REAL_PRIVATE_DOC_DIR = "doc/";
    public static String REAL_PRIVATE_WWW_DIR = "www/";
    public static String REAL_PUBLIC_DOCUMENTS_DIR = "documents/";
    public static String REAL_PUBLIC_DOWNLOADS_DIR = "downloads/";
    public static final String REL_PRIVATE_DOC_DIR = "_doc";
    public static final String REL_PRIVATE_WWW_DIR = "_www";
    public static final String REL_PUBLIC_DOCUMENTS_DIR = "_documents";
    public static final String REL_PUBLIC_DOWNLOADS_DIR = "_downloads";
    private static String SITMAP = "sitemap/";
    public static String STKEY = "2aSGNEUriKrg4cDH";
    public static boolean SyncDebug = false;
    public static boolean USE_ACTIVITY_HANDLE_KEYEVENT = false;
    public static String WGTU_UPDATE_XML = "update.xml";
    public static boolean allowDownloadWithoutWiFi = false;
    public static boolean injectionGeolocationJS = false;
    public static boolean isDefaultAim = false;
    public static boolean isDefense = false;
    public static boolean isFirstRun = false;
    public static boolean isImmersive = false;
    public static final boolean isOnlinePackage = true;
    public static boolean isPostChcekShortCut = false;
    public static boolean isUniStatistics = false;
    public static String lia = "";
    public static HashMap<String, BaseAppInfo> mBaseAppInfoSet = new HashMap<>();
    public static int mDeStatusBarBackground = -111111;
    public static HashMap<String, BaseAppInfo> mInstalledAppInfoSet = new HashMap<>();
    public static HashMap<String, CmtInfo> mLaunchers = new HashMap<>();
    public static HashMap<String, BaseAppInfo> mUnInstalledAppInfoSet = new HashMap<>();
    public static HashMap<String, byte[]> mW2AE = new HashMap<>();
    public static ArrayList<String> mWap2appTemplateFiles = new ArrayList<>();
    public static String minUserAgentVersion = "";
    public static String renderer = "";
    public static long run5appEndTime = 0;
    public static boolean sAnimationCaptureB = true;
    public static boolean sAnimationCaptureC = true;
    public static String sBaseConfigTemplatePath = (DeviceInfo.sBaseResRootPathName + "data/wap2app/__template.json");
    public static String sBaseControlPath = (DeviceInfo.sBaseResRootPathName + "data/dcloud_control.xml");
    public static String sBaseFsAppsPath = null;
    public static String sBaseFsSitMapPath = null;
    public static String sBaseNotificationPath = null;
    public static String sBaseResAppsFullPath = null;
    public static String sBaseResAppsPath = null;
    public static final String sBaseVersion = "1.9.9.81676";
    public static String sBaseWap2AppFilePath = (DeviceInfo.sBaseResRootPathName + "data/wap2app/__wap2app.js");
    public static String sBaseWap2AppTemplatePath = null;
    public static String sCacheFsAppsPath = null;
    public static String sChannel = "";
    public static String sConfigXML = "manifest.json";
    public static boolean sCoverApkRuning = false;
    public static String sCurrentAppOriginalAppid = null;
    public static String sDefWebViewUserAgent = "";
    public static String sDefaultBootApp = null;
    public static String sDocumentFullPath = "";
    public static boolean sDoingAnimation = false;
    public static String sDownloadFullPath = null;
    public static String sFontScale = "none";
    public static float sFontScaleFloat = 1.0f;
    public static boolean sFullScreenChanged = false;
    public static String sGlobalAuthority = null;
    public static boolean sGlobalFullScreen = false;
    public static String sGlobalUserAgent = null;
    public static String sLastAppVersionName = null;
    public static String sLastRunApp = null;
    public static int sOpenedCount = 0;
    private static boolean sParsedControl = false;
    public static long sProcessId = 0;
    public static ArrayList<String> sRunningApp = null;
    public static String sRuntimeJsPath = "io/dcloud/all.js";
    public static SDK.IntegratedMode sRuntimeMode = null;
    public static String sSplashExitCondition = AbsoluteConst.EVENTS_LOADED;
    public static boolean sSupportAddByHand = true;
    public static long sTemplateModifyTime = 0;
    public static int sTimeOutCount = 0;
    public static int sTimeOutMax = 3;
    public static int sTimeoutCapture = 350;
    public static String sURDFilePath = null;
    public static String sUniNViewServiceJsPath = (DeviceInfo.sBaseResRootPathName + "data/dcloud3.dat");
    public static String sWap2AppTemplateVersion;
    public static boolean s_Is_DCloud_Packaged = false;
    public static int s_Runing_App_Count = 0;
    public static int s_Runing_App_Count_Max = 3;
    public static int s_Runing_App_Count_Trim = 0;
    public static int s_Webview_Count = 0;
    public static String s_properties = "/data/dcloud_properties.xml";
    public static boolean showTipsWithoutWifi = false;
    public static long splashCloseTime;
    public static long splashCreateTime;
    public static long startTime;
    public static int timeOut = PathInterpolatorCompat.MAX_NUM_POINTS;
    public static String uniVersionV3 = "";
    public static String untrustedca = "accept";

    public static class AppIsTestWrapper {
        static final String name = "test_app";
        Context mContext = null;

        public boolean containsKey(String str) {
            return SP.getOrCreateBundle(name, true).contains(str);
        }

        public void init(Context context) {
            this.mContext = context;
        }

        public void put(String str, String str2) {
            SharedPreferences.Editor edit = SP.getOrCreateBundle(name, true).edit();
            edit.putString(str, str2);
            edit.commit();
        }

        public void remove(String str) {
            SP.getOrCreateBundle(name, true).edit().remove(str).commit();
        }
    }

    public static class BaseAppInfo {
        public String mAppVer = null;
        String mAppid = null;
        public boolean mDeleted = false;
        public boolean mMoreRecent = true;

        public BaseAppInfo(String str, String str2) {
            this.mAppid = str;
            this.mAppVer = str2;
        }

        public static final boolean compareVersion(String str, String str2) {
            if (TextUtils.isEmpty(str2)) {
                return true;
            }
            if (TextUtils.isEmpty(str)) {
                return false;
            }
            try {
                String[] split = str.split("\\.");
                String[] split2 = str2.split("\\.");
                int length = split.length;
                int length2 = split2.length;
                for (int i = 0; i < length; i++) {
                    if (i >= length2) {
                        return true;
                    }
                    int parseInt = Integer.parseInt(split[i]);
                    int parseInt2 = Integer.parseInt(split2[i]);
                    if (parseInt > parseInt2) {
                        return true;
                    }
                    if (parseInt < parseInt2) {
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return false;
        }

        public void clearBundleData() {
            String str = BaseInfo.PDR;
            SP.removeBundleData(str, this.mAppid + "_" + AbsoluteConst.XML_APPVER);
        }

        /* access modifiers changed from: package-private */
        public boolean high(BaseAppInfo baseAppInfo) {
            return compareVersion(this.mAppVer, baseAppInfo.mAppVer);
        }

        public void saveToBundleData(Context context) {
            String str = BaseInfo.PDR;
            SP.setBundleData(context, str, this.mAppid + "_" + AbsoluteConst.XML_APPVER, this.mAppVer);
            String str2 = BaseInfo.PDR;
            SP.setBundleData(context, str2, this.mAppid + "_" + AbsoluteConst.XML_DELETED, String.valueOf(this.mDeleted));
            String access$000 = BaseInfo.installAppMapToString();
            StringBuffer stringBuffer = new StringBuffer();
            if (!PdrUtil.isEmpty(access$000)) {
                stringBuffer.append(access$000);
                stringBuffer.append("|");
            }
            stringBuffer.append(this.mAppid);
            SP.setBundleData(context, BaseInfo.PDR, AbsoluteConst.XML_APPS, stringBuffer.toString());
        }
    }

    public static class CmtInfo {
        public boolean needUpdate = true;
        public String plusLauncher;
        public boolean rptCrs = true;
        public boolean rptJse = true;
        public String sStartupTime;
        public String sfd;
        public String templateVersion;
    }

    static {
        int i = Build.VERSION.SDK_INT;
        boolean z = true;
        USE_ACTIVITY_HANDLE_KEYEVENT = (i < 19) | true;
        if (i < 21) {
            z = false;
        }
        isDefaultAim = z;
    }

    public static boolean checkAppIsTest(String str) {
        return new File(sCacheFsAppsPath + str + "/.test").exists();
    }

    private static void checkOrResetTemplate(IApp iApp) {
        File file = new File(sBaseWap2AppTemplatePath + "wap2app__template/__template.json");
        long lastModified = file.lastModified();
        if (sTemplateModifyTime != lastModified) {
            try {
                mWap2appTemplateFiles.clear();
                mW2AE.clear();
                JSONObject jSONObject = null;
                try {
                    jSONObject = new JSONObject(new String(IOUtil.getBytes(new UnicodeInputStream(new FileInputStream(file), Charset.defaultCharset().name()))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int i = 0;
                if (jSONObject == null || !jSONObject.has("files")) {
                    String[] list = new File(sBaseWap2AppTemplatePath + "wap2app__template/").list();
                    if (list != null) {
                        while (i < list.length) {
                            mWap2appTemplateFiles.add(list[i]);
                            i++;
                        }
                    }
                } else {
                    JSONArray optJSONArray = jSONObject.optJSONArray("files");
                    int length = optJSONArray.length();
                    while (i < length) {
                        mWap2appTemplateFiles.add(optJSONArray.optString(i));
                        i++;
                    }
                }
                if (jSONObject.has(IApp.ConfigProperty.CONFIG_CONFUSION)) {
                    String handleEncryption = iApp.getConfusionMgr().handleEncryption(iApp.getActivity(), Base64.decode2bytes(jSONObject.optString(IApp.ConfigProperty.CONFIG_CONFUSION)));
                    if (PdrUtil.isEmpty(handleEncryption)) {
                        handleEncryption = "{}";
                    }
                    iApp.getConfusionMgr().removeData("__w2a__template__");
                    iApp.getConfusionMgr().recordEncryptionResources("__w2a__template__", new JSONObject(handleEncryption));
                }
                sWap2AppTemplateVersion = jSONObject.optString("version");
            } catch (JSONException e2) {
                e2.printStackTrace();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        sTemplateModifyTime = lastModified;
    }

    public static boolean checkTestOpenFile() {
        return new File(DeviceInfo.sDeviceRootDir + "/.system/d85a37c6-afdc-11e6-80f5-76304dec7eb7").exists();
    }

    public static void clearData() {
        sParsedControl = false;
        sGlobalFullScreen = false;
        UEH.sInited = false;
        sCurrentAppOriginalAppid = null;
        sLastRunApp = null;
        sRunningApp = null;
        s_Webview_Count = 0;
        s_Runing_App_Count = 0;
        DeviceInfo.DEVICESTATUS_JS = null;
    }

    public static boolean containsInTemplate(IApp iApp, String str) {
        if ((iApp.getActivity() instanceof IActivityHandler) && ((IActivityHandler) iApp.getActivity()).isMultiProcessMode()) {
            checkOrResetTemplate(iApp);
        }
        return mWap2appTemplateFiles.contains(str);
    }

    public static void createAppTestFile(String str) {
        File file = new File(sCacheFsAppsPath + str + "/.test");
        if (!file.exists()) {
            try {
                file.mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean existsBase() {
        try {
            return new File(Environment.getExternalStorageDirectory() + "/Android/data/io.dcloud.HBuilder").exists();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean existsLibso() {
        try {
            System.loadLibrary("so");
            return true;
        } catch (UnsatisfiedLinkError unused) {
            return false;
        }
    }

    public static boolean existsStreamEnv() {
        try {
            Class.forName("io.dcloud.appstream.StreamAppMainActivity");
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static String getAnalysisChannel() {
        String str = sChannel;
        if (TextUtils.isEmpty(str)) {
            str = AndroidResources.getMetaValue("DCLOUD_STREAMAPP_CHANNEL");
        }
        if (!TextUtils.isEmpty(str)) {
            if (str.startsWith("_")) {
                return str.substring(1);
            }
            if (!str.contains("|")) {
                return str;
            }
            String[] split = str.split("\\|");
            if (split.length >= 4) {
                return split[3];
            }
        }
        return "";
    }

    public static CmtInfo getCmitInfo(String str) {
        CmtInfo cmtInfo = mLaunchers.get(str);
        if (cmtInfo != null) {
            return cmtInfo;
        }
        CmtInfo cmtInfo2 = new CmtInfo();
        mLaunchers.put(str, cmtInfo2);
        return cmtInfo2;
    }

    public static String getCrashLogsPath(Context context) {
        return DeviceInfo.sBaseFsRootPath + "logs/" + context.getPackageName() + "/";
    }

    public static String getLastKey(LinkedHashMap<String, Intent> linkedHashMap) {
        String str = null;
        if (linkedHashMap != null) {
            Iterator<String> it = linkedHashMap.keySet().iterator();
            while (it.hasNext()) {
                str = it.next();
            }
        }
        return str;
    }

    public static String getLaunchType(Intent intent) {
        String str = "default";
        if (intent == null) {
            return str;
        }
        Uri data = intent.getData();
        if (intent.hasExtra(IntentConst.RUNING_STREAPP_LAUNCHER)) {
            str = intent.getStringExtra(IntentConst.RUNING_STREAPP_LAUNCHER);
        }
        if (data == null || URLUtil.isNetworkUrl(data.toString())) {
            if (intent.getExtras() == null) {
                return str;
            }
            if (!TextUtils.isEmpty(intent.getStringExtra(IntentConst.STREAM_LAUNCHER))) {
                return intent.getStringExtra(IntentConst.STREAM_LAUNCHER);
            }
            if (intent.getBooleanExtra(IntentConst.FROM_SHORT_CUT_STRAT, false)) {
                return IApp.ConfigProperty.CONFIG_SHORTCUT;
            }
            if (!intent.getBooleanExtra(IntentConst.FROM_BARCODE, false)) {
                if (intent.getIntExtra(IntentConst.START_FROM, -1) == 3 || intent.hasExtra("UP-OL-SU")) {
                    return "push";
                }
                if (intent.getIntExtra(IntentConst.START_FROM, -1) == 5) {
                    return "myapp";
                }
                if (intent.getIntExtra(IntentConst.START_FROM, -1) == 7) {
                    return "browser";
                }
                if (intent.getIntExtra(IntentConst.START_FROM, -1) == 8) {
                    return "favorite";
                }
                if (intent.getIntExtra(IntentConst.START_FROM, -1) == 9) {
                    return "engines";
                }
                if (intent.getIntExtra(IntentConst.START_FROM, -1) == 40) {
                    return "apush";
                }
                return intent.getIntExtra(IntentConst.START_FROM, -1) == 10 ? "speech" : str;
            }
        } else if (intent.getBooleanExtra(IntentConst.FROM_BARCODE, false)) {
            return "barcode";
        } else {
            return "scheme";
        }
        return "barcode";
    }

    public static String getLauncherData(String str) {
        CmtInfo cmtInfo = mLaunchers.get(str);
        if (cmtInfo != null && !TextUtils.isEmpty(cmtInfo.plusLauncher)) {
            return cmtInfo.plusLauncher;
        }
        return "default";
    }

    public static String getShortCutActivity(Context context) {
        return null;
    }

    public static String getStackTrace() {
        StringBuffer stringBuffer = new StringBuffer();
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement append : stackTrace) {
            stringBuffer.append(append);
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }

    public static String getStartupTimeData(String str) {
        CmtInfo cmtInfo = mLaunchers.get(str);
        if (cmtInfo != null) {
            return cmtInfo.sStartupTime;
        }
        return null;
    }

    public static String getUniNViewId(IFrameView iFrameView) {
        if (iFrameView.getFrameType() == 2) {
            return iFrameView.obtainApp().obtainAppId();
        }
        return iFrameView.obtainWebView().getWebviewUUID();
    }

    /* access modifiers changed from: private */
    public static String installAppMapToString() {
        StringBuffer stringBuffer = new StringBuffer();
        Set<String> keySet = mInstalledAppInfoSet.keySet();
        int size = keySet.size();
        for (String append : keySet) {
            stringBuffer.append(append);
            stringBuffer.append("|");
        }
        if (size > 1) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        return stringBuffer.toString();
    }

    public static boolean isBase(Context context) {
        return SyncDebug || context.getPackageName().equals("io.dcloud.HBuilder");
    }

    public static boolean isChannelGooglePlay() {
        String str = sChannel;
        if (TextUtils.isEmpty(str)) {
            str = AndroidResources.getMetaValue("DCLOUD_STREAMAPP_CHANNEL");
        }
        return !TextUtils.isEmpty(str) && str.endsWith("|google");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v1, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v2, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v3, resolved type: int} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean isDebugSignature(android.content.Context r5) {
        /*
            r0 = 0
            android.content.pm.PackageManager r1 = r5.getPackageManager()     // Catch:{ NameNotFoundException | Exception | CertificateException -> 0x003d }
            java.lang.String r5 = r5.getPackageName()     // Catch:{ NameNotFoundException | Exception | CertificateException -> 0x003d }
            r2 = 64
            android.content.pm.PackageInfo r5 = r1.getPackageInfo(r5, r2)     // Catch:{ NameNotFoundException | Exception | CertificateException -> 0x003d }
            android.content.pm.Signature[] r5 = r5.signatures     // Catch:{ NameNotFoundException | Exception | CertificateException -> 0x003d }
            r1 = 0
        L_0x0012:
            int r2 = r5.length     // Catch:{ NameNotFoundException | Exception | CertificateException -> 0x003c }
            if (r0 >= r2) goto L_0x003e
            java.lang.String r2 = "X.509"
            java.security.cert.CertificateFactory r2 = java.security.cert.CertificateFactory.getInstance(r2)     // Catch:{ NameNotFoundException | Exception | CertificateException -> 0x003c }
            java.io.ByteArrayInputStream r3 = new java.io.ByteArrayInputStream     // Catch:{ NameNotFoundException | Exception | CertificateException -> 0x003c }
            r4 = r5[r0]     // Catch:{ NameNotFoundException | Exception | CertificateException -> 0x003c }
            byte[] r4 = r4.toByteArray()     // Catch:{ NameNotFoundException | Exception | CertificateException -> 0x003c }
            r3.<init>(r4)     // Catch:{ NameNotFoundException | Exception | CertificateException -> 0x003c }
            java.security.cert.Certificate r2 = r2.generateCertificate(r3)     // Catch:{ NameNotFoundException | Exception | CertificateException -> 0x003c }
            java.security.cert.X509Certificate r2 = (java.security.cert.X509Certificate) r2     // Catch:{ NameNotFoundException | Exception | CertificateException -> 0x003c }
            javax.security.auth.x500.X500Principal r2 = r2.getSubjectX500Principal()     // Catch:{ NameNotFoundException | Exception | CertificateException -> 0x003c }
            javax.security.auth.x500.X500Principal r3 = DEBUG_DN     // Catch:{ NameNotFoundException | Exception | CertificateException -> 0x003c }
            boolean r1 = r2.equals(r3)     // Catch:{ NameNotFoundException | Exception | CertificateException -> 0x003c }
            if (r1 == 0) goto L_0x0039
            goto L_0x003e
        L_0x0039:
            int r0 = r0 + 1
            goto L_0x0012
        L_0x003c:
            r0 = r1
        L_0x003d:
            r1 = r0
        L_0x003e:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.BaseInfo.isDebugSignature(android.content.Context):boolean");
    }

    public static synchronized boolean isLoadingLaunchePage() {
        synchronized (BaseInfo.class) {
        }
        return false;
    }

    public static boolean isStandardBase(Context context) {
        return context.getPackageName().equals("io.dcloud.HBuilder");
    }

    public static boolean isTest(String str) {
        return false;
    }

    public static boolean isUniAppAppid(IApp iApp) {
        String str;
        if (iApp == null) {
            return false;
        }
        String obtainConfigProperty = iApp.obtainConfigProperty(AbsoluteConst.APP_IS_UNIAPP);
        if (!PdrUtil.isEmpty(obtainConfigProperty)) {
            return Boolean.valueOf(obtainConfigProperty).booleanValue();
        }
        String obtainAppId = iApp.obtainAppId();
        if (TextUtils.isEmpty(obtainAppId)) {
            return false;
        }
        if ("HBuilder".equals(obtainAppId) && (str = sCurrentAppOriginalAppid) != null) {
            obtainAppId = str;
        }
        return obtainAppId.startsWith("__UNI__");
    }

    public static boolean isUniNViewBackgroud() {
        return Boolean.valueOf(AndroidResources.getMetaValue(AbsoluteConst.DCLOUD_UNINVIEW_BACKGROUD)).booleanValue();
    }

    public static boolean isWap2AppAppid(String str) {
        String str2;
        if (str == null) {
            return false;
        }
        if ("HBuilder".equals(str) && (str2 = sCurrentAppOriginalAppid) != null) {
            str = str2;
        }
        String lowerCase = str.toLowerCase(Locale.ENGLISH);
        if (lowerCase.startsWith("__w2a__") || "H52588A9C".equalsIgnoreCase(lowerCase) || "H5B5EEFBB".equalsIgnoreCase(lowerCase) || "H5A0B1958".equalsIgnoreCase(lowerCase) || "H5EA885FD".equalsIgnoreCase(lowerCase) || "H592E7F63".equalsIgnoreCase(lowerCase) || "H5BCD03E4".equalsIgnoreCase(lowerCase)) {
            return true;
        }
        return false;
    }

    public static boolean isWeexUniJs(IApp iApp) {
        String obtainConfigProperty = iApp.obtainConfigProperty(IApp.ConfigProperty.CONFIG_UNIAPP_CONTROL);
        if (!TextUtils.isEmpty(obtainConfigProperty)) {
            return (obtainConfigProperty.equals(Constants.CodeCache.SAVE_PATH) || obtainConfigProperty.equals(AbsoluteConst.UNI_V3)) && isUniAppAppid(iApp);
        }
        return false;
    }

    public static boolean isWifi(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.getType() == 1;
    }

    public static void loadInstalledAppInfo(Context context) {
        String[] listFsAppsFiles;
        String bundleData = SP.getBundleData(context, PDR, AbsoluteConst.XML_APPS);
        if (bundleData != null) {
            for (String str : bundleData.split("\\|")) {
                BaseAppInfo baseAppInfo = new BaseAppInfo(str, SP.getBundleData(context, PDR, str + "_" + AbsoluteConst.XML_APPVER));
                boolean parseBoolean = Boolean.parseBoolean(SP.getBundleData(context, PDR, str + "_" + AbsoluteConst.XML_DELETED));
                baseAppInfo.mDeleted = parseBoolean;
                if (parseBoolean) {
                    mUnInstalledAppInfoSet.put(str, baseAppInfo);
                } else if (!PdrUtil.isEmpty(str)) {
                    mInstalledAppInfoSet.put(str, baseAppInfo);
                }
            }
        }
        if (sSupportAddByHand && (listFsAppsFiles = PlatformUtil.listFsAppsFiles(sBaseFsAppsPath)) != null) {
            for (String str2 : listFsAppsFiles) {
                if (!mInstalledAppInfoSet.containsKey(str2)) {
                    String str3 = sDefaultBootApp;
                    Locale locale = Locale.ENGLISH;
                    if (TextUtils.equals(str3.toLowerCase(locale), str2.toLowerCase(locale))) {
                        mInstalledAppInfoSet.put(str2, new BaseAppInfo(str2, WXInstanceApm.VALUE_ERROR_CODE_DEFAULT));
                    }
                }
            }
        }
    }

    public static void parseControl() {
        XmlUtil.DHNode XML_Parser;
        InputStream resInputStream = PlatformUtil.getResInputStream(sBaseControlPath);
        if (resInputStream != null && (XML_Parser = XmlUtil.XML_Parser(resInputStream)) != null) {
            boolean parseBoolean = Boolean.parseBoolean(XmlUtil.getAttributeValue(XML_Parser, AbsoluteConst.XML_SyncDebug, AbsoluteConst.FALSE));
            SyncDebug = parseBoolean;
            ISDEBUG = parseBoolean || Boolean.parseBoolean(XmlUtil.getAttributeValue(XML_Parser, "debug", AbsoluteConst.FALSE));
            XmlUtil.DHNode element = XmlUtil.getElement(XML_Parser, AbsoluteConst.XML_APPS);
            if (!SDK.isUniMPSDK()) {
                ArrayList<XmlUtil.DHNode> elements = XmlUtil.getElements(element, AbsoluteConst.XML_APP);
                if (elements.size() > 0) {
                    sDefaultBootApp = XmlUtil.getAttributeValue(elements.get(0), "appid");
                }
                XmlUtil.DHNode element2 = XmlUtil.getElement(XML_Parser, "lia");
                if (element2 != null) {
                    lia = XmlUtil.getText(element2);
                }
            }
        }
    }

    public static void putLauncherData(String str, String str2) {
        getCmitInfo(str).plusLauncher = str2;
    }

    public static void putStartupTimeData(String str, String str2) {
        getCmitInfo(str).sStartupTime = str2;
    }

    public static void removeTestFile(String str) {
        File file = new File(sCacheFsAppsPath + str + "/.test");
        if (file.exists()) {
            file.delete();
        }
    }

    public static void saveInstalledAppInfo(Context context) {
        SP.setBundleData(context, PDR, AbsoluteConst.XML_APPS, installAppMapToString());
    }

    public static synchronized void setLoadingLaunchePage(boolean z, String str) {
        synchronized (BaseInfo.class) {
        }
    }

    public static void updateBaseInfo(boolean z) {
        if (!APPS_NAME.equals("/")) {
            sBaseResAppsFullPath = DeviceInfo.sBaseResRootFullPath + APPS_NAME;
            sBaseResAppsPath = DeviceInfo.sBaseResRootPathName + APPS_NAME;
            sCacheFsAppsPath = DeviceInfo.sBaseFsCachePath + APPS_NAME;
            sBaseFsAppsPath = DeviceInfo.sBaseFsRootPath + APPS_NAME;
            sBaseFsSitMapPath = DeviceInfo.sBaseFsRootPath + SITMAP;
            sBaseWap2AppTemplatePath = DeviceInfo.sBaseFsCachePath + "cnc3ejE5/";
            sURDFilePath = DeviceInfo.sBaseFsCachePath + "cnc3ejE6/eje3cnc";
            if (z) {
                DHFile.createNewFile(sBaseFsAppsPath);
                DHFile.createNewFile(sBaseFsSitMapPath);
            }
        }
        if (z) {
            String str = sDownloadFullPath;
            if (str == null || str.indexOf("sdcard/") <= -1) {
                sDownloadFullPath = DeviceInfo.sBaseFsRootPath + REAL_PUBLIC_DOWNLOADS_DIR;
            } else {
                sDownloadFullPath = PdrUtil.appendByDeviceRootDir(sDownloadFullPath);
            }
            String str2 = sDocumentFullPath;
            if (str2 == null || str2.indexOf("sdcard/") <= -1) {
                sDocumentFullPath = DeviceInfo.sBaseFsRootPath + REAL_PUBLIC_DOCUMENTS_DIR;
                return;
            }
            sDocumentFullPath = PdrUtil.appendByDeviceRootDir(sDocumentFullPath);
        }
    }

    public static void updateBaseInfoByApp(String str, String str2) {
        if (str != null) {
            PDR = str;
        }
        if (str2 != null) {
            DeviceInfo.sBaseFsRootPath = str2;
        }
        DeviceInfo.initBaseFsRootPath();
    }

    public static boolean useStreamAppStatistic(Context context) {
        return true;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v0, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v1, resolved type: java.lang.String[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v2, resolved type: java.lang.String[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v3, resolved type: java.lang.String[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v4, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v5, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v6, resolved type: java.lang.String[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v7, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v8, resolved type: java.lang.String[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v9, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v13, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v18, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v21, resolved type: java.lang.String[]} */
    /* JADX WARNING: type inference failed for: r4v1 */
    /* JADX WARNING: type inference failed for: r4v4 */
    /* JADX WARNING: type inference failed for: r4v5 */
    /* JADX WARNING: type inference failed for: r10v2 */
    /* JADX WARNING: type inference failed for: r10v3 */
    /* JADX WARNING: type inference failed for: r4v7, types: [boolean] */
    /* JADX WARNING: type inference failed for: r4v9 */
    /* JADX WARNING: type inference failed for: r10v4 */
    /* JADX WARNING: type inference failed for: r4v10 */
    /* JADX WARNING: type inference failed for: r4v14 */
    /* JADX WARNING: type inference failed for: r10v5, types: [java.lang.String[]] */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Failed to insert additional move for type inference */
    /* JADX WARNING: Missing exception handler attribute for start block: B:49:0x00d0 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:108:0x01ca  */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x01cf  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0071  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0083  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00ba A[SYNTHETIC, Splitter:B:42:0x00ba] */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00eb  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0100 A[SYNTHETIC, Splitter:B:59:0x0100] */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x011b A[SYNTHETIC, Splitter:B:70:0x011b] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String[] parseControl(android.content.Context r17, io.dcloud.common.DHInterface.ICore r18, io.dcloud.common.DHInterface.ICore.ICoreStatusListener r19) {
        /*
            r1 = r18
            r2 = r19
            java.lang.String r3 = "apps"
            java.lang.String r0 = "false"
            boolean r4 = sParsedControl
            java.lang.String r5 = "Main_Path"
            r6 = 0
            if (r4 != 0) goto L_0x01db
            r4 = 0
            r7 = 1
            java.lang.String r8 = sBaseControlPath     // Catch:{ Exception -> 0x01be }
            java.io.InputStream r8 = io.dcloud.common.adapter.util.PlatformUtil.getResInputStream(r8)     // Catch:{ Exception -> 0x01be }
            if (r8 == 0) goto L_0x01b8
            io.dcloud.common.util.XmlUtil$DHNode r9 = io.dcloud.common.util.XmlUtil.XML_Parser(r8)     // Catch:{ Exception -> 0x01be }
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r8)     // Catch:{ Exception -> 0x01be }
            if (r9 == 0) goto L_0x01bb
            java.lang.String r8 = "syncDebug"
            java.lang.String r8 = io.dcloud.common.util.XmlUtil.getAttributeValue((io.dcloud.common.util.XmlUtil.DHNode) r9, (java.lang.String) r8, (java.lang.String) r0)     // Catch:{ Exception -> 0x01be }
            boolean r8 = java.lang.Boolean.parseBoolean(r8)     // Catch:{ Exception -> 0x01be }
            SyncDebug = r8     // Catch:{ Exception -> 0x01be }
            if (r8 != 0) goto L_0x0042
            java.lang.String r8 = "debug"
            java.lang.String r8 = io.dcloud.common.util.XmlUtil.getAttributeValue((io.dcloud.common.util.XmlUtil.DHNode) r9, (java.lang.String) r8, (java.lang.String) r0)     // Catch:{ Exception -> 0x003f }
            boolean r8 = java.lang.Boolean.parseBoolean(r8)     // Catch:{ Exception -> 0x003f }
            if (r8 == 0) goto L_0x003d
            goto L_0x0042
        L_0x003d:
            r8 = 0
            goto L_0x0043
        L_0x003f:
            r0 = move-exception
            goto L_0x01c1
        L_0x0042:
            r8 = 1
        L_0x0043:
            ISDEBUG = r8     // Catch:{ Exception -> 0x01be }
            java.lang.String r8 = "auxiliary"
            java.lang.String r8 = io.dcloud.common.util.XmlUtil.getAttributeValue((io.dcloud.common.util.XmlUtil.DHNode) r9, (java.lang.String) r8, (java.lang.String) r0)     // Catch:{ Exception -> 0x01be }
            boolean r8 = java.lang.Boolean.parseBoolean(r8)     // Catch:{ Exception -> 0x01be }
            AuxiliaryInput = r8     // Catch:{ Exception -> 0x01be }
            java.lang.String r8 = "amu"
            java.lang.String r8 = io.dcloud.common.util.XmlUtil.getAttributeValue((io.dcloud.common.util.XmlUtil.DHNode) r9, (java.lang.String) r8, (java.lang.String) r0)     // Catch:{ Exception -> 0x01be }
            boolean r8 = java.lang.Boolean.parseBoolean(r8)     // Catch:{ Exception -> 0x01be }
            ISAMU = r8     // Catch:{ Exception -> 0x01be }
            java.lang.String r8 = "authority"
            java.lang.String r8 = io.dcloud.common.util.XmlUtil.getAttributeValue((io.dcloud.common.util.XmlUtil.DHNode) r9, (java.lang.String) r8, (java.lang.String) r6)     // Catch:{ Exception -> 0x01be }
            sGlobalAuthority = r8     // Catch:{ Exception -> 0x01be }
            java.lang.String r8 = "DCLOUD_STREAMAPP_CHANNEL"
            java.lang.String r8 = io.dcloud.common.adapter.util.AndroidResources.getMetaValue(r8)     // Catch:{ Exception -> 0x01be }
            boolean r10 = io.dcloud.common.util.PdrUtil.isEmpty(r8)     // Catch:{ Exception -> 0x01be }
            if (r10 != 0) goto L_0x0083
            java.lang.String r10 = "_"
            boolean r10 = r8.startsWith(r10)     // Catch:{ Exception -> 0x003f }
            if (r10 == 0) goto L_0x0080
            java.lang.String r8 = r8.substring(r7)     // Catch:{ Exception -> 0x003f }
            sChannel = r8     // Catch:{ Exception -> 0x003f }
            goto L_0x0096
        L_0x0080:
            sChannel = r8     // Catch:{ Exception -> 0x003f }
            goto L_0x0096
        L_0x0083:
            java.lang.String r8 = "channel"
            java.lang.String r8 = io.dcloud.common.util.XmlUtil.getAttributeValue((io.dcloud.common.util.XmlUtil.DHNode) r9, (java.lang.String) r8, (java.lang.String) r6)     // Catch:{ Exception -> 0x01be }
            boolean r10 = io.dcloud.common.util.PdrUtil.isEmpty(r8)     // Catch:{ Exception -> 0x01be }
            if (r10 != 0) goto L_0x0092
            sChannel = r8     // Catch:{ Exception -> 0x003f }
            goto L_0x0096
        L_0x0092:
            java.lang.String r8 = ""
            sChannel = r8     // Catch:{ Exception -> 0x01be }
        L_0x0096:
            java.lang.String r8 = "back"
            java.lang.String r10 = sSplashExitCondition     // Catch:{ Exception -> 0x01be }
            java.lang.String r8 = io.dcloud.common.util.XmlUtil.getAttributeValue((io.dcloud.common.util.XmlUtil.DHNode) r9, (java.lang.String) r8, (java.lang.String) r10)     // Catch:{ Exception -> 0x01be }
            sSplashExitCondition = r8     // Catch:{ Exception -> 0x01be }
            java.lang.String r8 = "ns"
            java.lang.String r0 = io.dcloud.common.util.XmlUtil.getAttributeValue((io.dcloud.common.util.XmlUtil.DHNode) r9, (java.lang.String) r8, (java.lang.String) r0)     // Catch:{ Exception -> 0x01be }
            boolean r0 = java.lang.Boolean.parseBoolean(r0)     // Catch:{ Exception -> 0x01be }
            s_Is_DCloud_Packaged = r0     // Catch:{ Exception -> 0x01be }
            java.lang.String r0 = "fontscale"
            java.lang.String r8 = sFontScale     // Catch:{ Exception -> 0x01be }
            java.lang.String r0 = io.dcloud.common.util.XmlUtil.getAttributeValue((io.dcloud.common.util.XmlUtil.DHNode) r9, (java.lang.String) r0, (java.lang.String) r8)     // Catch:{ Exception -> 0x01be }
            boolean r8 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x01be }
            if (r8 != 0) goto L_0x00d0
            sFontScale = r0     // Catch:{ Exception -> 0x003f }
            java.lang.String r8 = "none"
            boolean r0 = r8.equals(r0)     // Catch:{ Exception -> 0x00d0 }
            if (r0 != 0) goto L_0x00d0
            java.lang.String r0 = sFontScale     // Catch:{ Exception -> 0x00d0 }
            java.lang.Float r0 = java.lang.Float.valueOf(r0)     // Catch:{ Exception -> 0x00d0 }
            float r0 = r0.floatValue()     // Catch:{ Exception -> 0x00d0 }
            sFontScaleFloat = r0     // Catch:{ Exception -> 0x00d0 }
        L_0x00d0:
            boolean r0 = sSupportAddByHand     // Catch:{ Exception -> 0x01be }
            boolean r8 = ISDEBUG     // Catch:{ Exception -> 0x01be }
            r0 = r0 & r8
            sSupportAddByHand = r0     // Catch:{ Exception -> 0x01be }
            io.dcloud.common.util.XmlUtil$DHNode r0 = io.dcloud.common.util.XmlUtil.getElement(r9, r3)     // Catch:{ Exception -> 0x01be }
            java.lang.String r8 = "max"
            java.lang.String r8 = io.dcloud.common.util.XmlUtil.getAttributeValue(r0, r8)     // Catch:{ Exception -> 0x01be }
            int r9 = s_Runing_App_Count_Max     // Catch:{ Exception -> 0x01be }
            int r8 = io.dcloud.common.util.PdrUtil.parseInt(r8, r9)     // Catch:{ Exception -> 0x01be }
            s_Runing_App_Count_Max = r8     // Catch:{ Exception -> 0x01be }
            if (r8 > 0) goto L_0x00f0
            r8 = 2147483647(0x7fffffff, float:NaN)
            s_Runing_App_Count_Max = r8     // Catch:{ Exception -> 0x003f }
        L_0x00f0:
            java.lang.String r8 = "trim"
            java.lang.String r8 = io.dcloud.common.util.XmlUtil.getAttributeValue(r0, r8)     // Catch:{ Exception -> 0x01be }
            int r9 = s_Runing_App_Count_Trim     // Catch:{ Exception -> 0x01be }
            int r8 = io.dcloud.common.util.PdrUtil.parseInt(r8, r9)     // Catch:{ Exception -> 0x01be }
            s_Runing_App_Count_Trim = r8     // Catch:{ Exception -> 0x01be }
            if (r8 > 0) goto L_0x0102
            s_Runing_App_Count_Trim = r4     // Catch:{ Exception -> 0x003f }
        L_0x0102:
            java.lang.String r8 = "app"
            java.util.ArrayList r0 = io.dcloud.common.util.XmlUtil.getElements(r0, r8)     // Catch:{ Exception -> 0x01be }
            if (r0 == 0) goto L_0x01bb
            boolean r8 = io.dcloud.feature.internal.sdk.SDK.isUniMPSDK()     // Catch:{ Exception -> 0x01be }
            if (r8 != 0) goto L_0x01bb
            int r8 = r0.size()     // Catch:{ Exception -> 0x01be }
            r10 = r6
            r13 = r10
            r9 = 0
            r11 = 0
            r12 = 1
        L_0x0119:
            if (r9 >= r8) goto L_0x01b5
            java.lang.Object r14 = r0.get(r9)     // Catch:{ Exception -> 0x01b1 }
            io.dcloud.common.util.XmlUtil$DHNode r14 = (io.dcloud.common.util.XmlUtil.DHNode) r14     // Catch:{ Exception -> 0x01b1 }
            java.lang.String r15 = "appid"
            java.lang.String r15 = io.dcloud.common.util.XmlUtil.getAttributeValue(r14, r15)     // Catch:{ Exception -> 0x01b1 }
            if (r9 != 0) goto L_0x0155
            java.lang.String[] r10 = new java.lang.String[r7]     // Catch:{ Exception -> 0x01b1 }
            r10[r4] = r15     // Catch:{ Exception -> 0x01b1 }
            boolean r12 = io.dcloud.feature.internal.sdk.SDK.isUniMPSDK()     // Catch:{ Exception -> 0x01b1 }
            if (r12 != 0) goto L_0x0135
            sDefaultBootApp = r15     // Catch:{ Exception -> 0x01b1 }
        L_0x0135:
            updateBaseInfoByApp(r15, r6)     // Catch:{ Exception -> 0x01b1 }
            boolean r12 = io.dcloud.common.adapter.util.DeviceInfo.checkCoverLoadApp()     // Catch:{ Exception -> 0x01b1 }
            sCoverApkRuning = r12     // Catch:{ Exception -> 0x01b1 }
            io.dcloud.application.DCLoudApplicationImpl r13 = io.dcloud.application.DCLoudApplicationImpl.self()     // Catch:{ Exception -> 0x01b1 }
            android.content.Context r13 = r13.getContext()     // Catch:{ Exception -> 0x01b1 }
            boolean r13 = isBase(r13)     // Catch:{ Exception -> 0x01b1 }
            if (r13 == 0) goto L_0x0150
            boolean r13 = ISDEBUG     // Catch:{ Exception -> 0x01b1 }
            r13 = r13 ^ r7
            r12 = r12 & r13
        L_0x0150:
            loadInstalledAppInfo(r17)     // Catch:{ Exception -> 0x01b1 }
            java.util.HashMap<java.lang.String, io.dcloud.common.util.BaseInfo$BaseAppInfo> r13 = mInstalledAppInfoSet     // Catch:{ Exception -> 0x01b1 }
        L_0x0155:
            java.lang.String r6 = "appver"
            java.lang.String r6 = io.dcloud.common.util.XmlUtil.getAttributeValue(r14, r6)     // Catch:{ Exception -> 0x01b1 }
            io.dcloud.common.util.BaseInfo$BaseAppInfo r14 = new io.dcloud.common.util.BaseInfo$BaseAppInfo     // Catch:{ Exception -> 0x01b1 }
            r14.<init>(r15, r6)     // Catch:{ Exception -> 0x01b1 }
            java.util.HashMap<java.lang.String, io.dcloud.common.util.BaseInfo$BaseAppInfo> r6 = mBaseAppInfoSet     // Catch:{ Exception -> 0x01b1 }
            r6.put(r15, r14)     // Catch:{ Exception -> 0x01b1 }
            if (r12 == 0) goto L_0x01ab
            boolean r6 = r13.containsKey(r15)     // Catch:{ Exception -> 0x01b1 }
            if (r6 == 0) goto L_0x01ab
            java.lang.Object r6 = r13.get(r15)     // Catch:{ Exception -> 0x01b1 }
            io.dcloud.common.util.BaseInfo$BaseAppInfo r6 = (io.dcloud.common.util.BaseInfo.BaseAppInfo) r6     // Catch:{ Exception -> 0x01b1 }
            boolean r16 = sCoverApkRuning     // Catch:{ Exception -> 0x01b1 }
            if (r16 != 0) goto L_0x0186
            boolean r6 = r14.high(r6)     // Catch:{ Exception -> 0x01b1 }
            if (r6 == 0) goto L_0x017e
            goto L_0x0186
        L_0x017e:
            r14.mMoreRecent = r4     // Catch:{ Exception -> 0x01b1 }
            java.util.HashMap<java.lang.String, io.dcloud.common.util.BaseInfo$BaseAppInfo> r6 = mBaseAppInfoSet     // Catch:{ Exception -> 0x01b1 }
            r6.remove(r15)     // Catch:{ Exception -> 0x01b1 }
            goto L_0x01ab
        L_0x0186:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01b1 }
            r6.<init>()     // Catch:{ Exception -> 0x01b1 }
            r6.append(r15)     // Catch:{ Exception -> 0x01b1 }
            java.lang.String r4 = " App has new version! it is "
            r6.append(r4)     // Catch:{ Exception -> 0x01b1 }
            java.lang.String r4 = r14.mAppVer     // Catch:{ Exception -> 0x01b1 }
            r6.append(r4)     // Catch:{ Exception -> 0x01b1 }
            java.lang.String r4 = r6.toString()     // Catch:{ Exception -> 0x01b1 }
            android.util.Log.i(r5, r4)     // Catch:{ Exception -> 0x01b1 }
            r14.clearBundleData()     // Catch:{ Exception -> 0x01a7 }
            r13.remove(r15)     // Catch:{ Exception -> 0x01a7 }
            r11 = 1
            goto L_0x01ab
        L_0x01a7:
            r0 = move-exception
            r6 = r10
            r4 = 1
            goto L_0x01c1
        L_0x01ab:
            int r9 = r9 + 1
            r4 = 0
            r6 = 0
            goto L_0x0119
        L_0x01b1:
            r0 = move-exception
            r6 = r10
            r4 = r11
            goto L_0x01c1
        L_0x01b5:
            r6 = r10
            r4 = r11
            goto L_0x01c4
        L_0x01b8:
            io.dcloud.common.adapter.util.DeviceInfo.initBaseFsRootPath()     // Catch:{ Exception -> 0x01be }
        L_0x01bb:
            r4 = 0
            r6 = 0
            goto L_0x01c4
        L_0x01be:
            r0 = move-exception
            r4 = 0
            r6 = 0
        L_0x01c1:
            r0.printStackTrace()
        L_0x01c4:
            sParsedControl = r7
            boolean r0 = ISDEBUG
            if (r0 == 0) goto L_0x01cd
            io.dcloud.common.adapter.util.Logger.setOpen(r7)
        L_0x01cd:
            if (r4 == 0) goto L_0x01dc
            java.lang.String r0 = PDR
            java.lang.String r4 = installAppMapToString()
            r7 = r17
            io.dcloud.common.adapter.util.SP.setBundleData(r7, r0, r3, r4)
            goto L_0x01dc
        L_0x01db:
            r6 = 0
        L_0x01dc:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r3 = "channel:"
            r0.append(r3)
            java.lang.String r3 = sChannel
            r0.append(r3)
            java.lang.String r3 = ";ver:"
            r0.append(r3)
            java.lang.String r3 = "1.9.9.81676"
            r0.append(r3)
            java.lang.String r3 = ";max:"
            r0.append(r3)
            int r3 = s_Runing_App_Count_Max
            r0.append(r3)
            java.lang.String r3 = ";trim:"
            r0.append(r3)
            int r3 = s_Runing_App_Count_Trim
            r0.append(r3)
            java.lang.String r3 = ";dg:"
            r0.append(r3)
            boolean r3 = ISDEBUG
            r0.append(r3)
            java.lang.String r0 = r0.toString()
            io.dcloud.common.adapter.util.Logger.e(r0)
            if (r2 == 0) goto L_0x0227
            if (r1 == 0) goto L_0x0227
            java.lang.String r0 = "will exc coreListener.onCoreReady"
            android.util.Log.i(r5, r0)
            r2.onCoreReady(r1)
        L_0x0227:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.BaseInfo.parseControl(android.content.Context, io.dcloud.common.DHInterface.ICore, io.dcloud.common.DHInterface.ICore$ICoreStatusListener):java.lang.String[]");
    }
}
