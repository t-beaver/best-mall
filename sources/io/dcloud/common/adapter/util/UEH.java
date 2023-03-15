package io.dcloud.common.adapter.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.utils.tools.TimeCalculator;
import io.dcloud.common.adapter.ui.webview.WebViewFactory;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.AppStatus;
import io.dcloud.common.util.Base64;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.HarmonyUtils;
import io.dcloud.common.util.NetTool;
import io.dcloud.common.util.NetworkTypeUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.TelephonyUtil;
import io.dcloud.common.util.ThreadPool;
import io.dcloud.common.util.net.NetWork;
import io.dcloud.e.a;
import io.dcloud.feature.internal.sdk.SDK;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.Thread;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class UEH {
    private static final String CRASH_DIRECTORY = "crash/";
    private static final boolean SAVE_CRASH_LOG = true;
    private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINESE);
    public static boolean sInited = false;

    public static void catchUncaughtException(final Context context) {
        if (!sInited) {
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                public void uncaughtException(Thread thread, Throwable th) {
                    File unused = UEH.handleUncaughtException(context, th);
                    try {
                        if (BaseInfo.getCmitInfo(BaseInfo.sLastRunApp).rptCrs) {
                            UEH.commitUncatchException(context, th);
                        }
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Logger.e("UncaughtExceptionHandler", "commitUncatchException");
                    }
                    th.printStackTrace();
                    Logger.e("UncaughtExceptionHandler", th.toString());
                    Process.killProcess(Process.myPid());
                }
            });
            sInited = true;
        }
    }

    private static void commitBaseUncatchInfo(Context context, StringBuffer stringBuffer) {
        String str;
        int networkType = NetworkTypeUtil.getNetworkType(context);
        try {
            str = URLEncoder.encode(Build.MODEL, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            str = null;
        }
        int i = Build.VERSION.SDK_INT;
        stringBuffer.append("s=99");
        stringBuffer.append("&p=a");
        if (!AppRuntime.hasPrivacyForNotShown(context)) {
            stringBuffer.append("&net=" + networkType);
            stringBuffer.append("&dcid=" + AppRuntime.getDCloudDeviceID(context));
            stringBuffer.append("&carrierid=" + TelephonyUtil.getSimOperator(context));
            String str2 = BaseInfo.sDefWebViewUserAgent;
            if (PdrUtil.isEmpty(str2) && Thread.currentThread() == Looper.getMainLooper().getThread()) {
                str2 = WebViewFactory.getDefWebViewUA(context);
            }
            if (!PdrUtil.isEmpty(str2)) {
                stringBuffer.append("&ua=" + str2);
            }
        }
        String str3 = BaseInfo.sLastRunApp;
        if (str3 == null) {
            str3 = BaseInfo.sDefaultBootApp;
        }
        if (PdrUtil.isEmpty(str3) && !SDK.isUniMPSDK()) {
            BaseInfo.parseControl();
            str3 = BaseInfo.sDefaultBootApp;
        }
        a.C0025a a = a.a(context);
        int round = Math.round(((float) (System.currentTimeMillis() - BaseInfo.startTime)) / 1000.0f);
        stringBuffer.append("&pv=" + AndroidResources.versionName);
        stringBuffer.append("&root=" + (DeviceInfo.hasRootPrivilege() ? 1 : 0));
        stringBuffer.append("&t=" + System.currentTimeMillis());
        stringBuffer.append("&duration=" + round);
        StringBuilder sb = new StringBuilder();
        sb.append("&fore=");
        sb.append(AppStatus.getAppStatus(BaseInfo.sLastRunApp) == 2 ? 0 : 1);
        stringBuffer.append(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("&osn=");
        sb2.append(HarmonyUtils.isHarmonyOs() ? "HarmonyOS" : TimeCalculator.PLATFORM_ANDROID);
        stringBuffer.append(sb2.toString());
        stringBuffer.append("&batlevel=" + AppRuntime.getBatteryLevel());
        stringBuffer.append("&battemp=" + AppRuntime.getTemperature());
        stringBuffer.append("&channel=" + BaseInfo.getAnalysisChannel());
        stringBuffer.append("&md=" + str);
        stringBuffer.append("&os=" + i);
        stringBuffer.append("&osv=" + Build.VERSION.RELEASE);
        stringBuffer.append("&appid=" + str3);
        stringBuffer.append("&vb=1.9.9.81676");
        stringBuffer.append("&appcount=" + BaseInfo.s_Runing_App_Count);
        stringBuffer.append("&wvcount=" + BaseInfo.s_Webview_Count);
        stringBuffer.append("&pn=" + context.getPackageName());
        stringBuffer.append("&memuse=" + (a.a - a.b));
        stringBuffer.append("&memtotal=" + a.a);
        stringBuffer.append("&diskuse=" + (a.d - a.c));
        stringBuffer.append("&disktotal=" + a.d);
        stringBuffer.append("&vd=" + PdrUtil.encodeURL(Build.MANUFACTURER));
        stringBuffer.append("&abis=" + getAbis());
        stringBuffer.append("&psdk=0");
        stringBuffer.append("&it=" + (SDK.isUniMPSDK() ? 1 : 0));
        if (!TextUtils.isEmpty(BaseInfo.sLastAppVersionName)) {
            stringBuffer.append("&v=" + BaseInfo.sLastAppVersionName);
        }
        if (!TextUtils.isEmpty(AppRuntime.getUniStatistics())) {
            stringBuffer.append("&us=" + AppRuntime.getUniStatistics());
        }
    }

    private static void commitErrorLog(final String str) {
        ThreadPool.self().addThreadTask(new Runnable() {
            public void run() {
                HashMap hashMap = new HashMap();
                hashMap.put(NetWork.CONTENT_TYPE, "application/x-www-form-urlencoded");
                String decode2String = Base64.decode2String("aHR0cHM6Ly9jci5kY2xvdWQubmV0LmNuLw==");
                NetTool.httpPost(decode2String + "collect/crash", str, hashMap);
            }
        });
    }

    public static void commitUncatchException(Context context, String str, String str2, int i) {
        StringBuffer stringBuffer = new StringBuffer();
        commitBaseUncatchInfo(context, stringBuffer);
        stringBuffer.append("&etype=" + i);
        stringBuffer.append("&log=" + PdrUtil.encodeURL(str2));
        stringBuffer.append("&eurl=" + PdrUtil.encodeURL(str));
        commitErrorLog(stringBuffer.toString());
    }

    private static String getAbis() {
        String[] strArr = Build.VERSION.SDK_INT >= 21 ? Build.SUPPORTED_ABIS : new String[]{Build.CPU_ABI, Build.CPU_ABI2};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strArr.length; i++) {
            sb.append(strArr[i]);
            if (i < strArr.length - 1) {
                sb.append(Operators.ARRAY_SEPRATOR);
            }
        }
        return sb.toString();
    }

    /* access modifiers changed from: private */
    public static File handleUncaughtException(Context context, Throwable th) {
        File file;
        File file2 = null;
        try {
            Field[] declaredFields = Build.class.getDeclaredFields();
            StringBuffer stringBuffer = new StringBuffer();
            for (Field field : declaredFields) {
                try {
                    field.setAccessible(true);
                    stringBuffer.append(field.getName() + ":" + field.get((Object) null) + "\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            StringWriter stringWriter = new StringWriter();
            th.printStackTrace(new PrintWriter(stringWriter));
            String stringWriter2 = stringWriter.toString();
            if ("mounted".equalsIgnoreCase(Environment.getExternalStorageState())) {
                file = new File(BaseInfo.getCrashLogsPath(context) + CRASH_DIRECTORY);
            } else {
                File cacheDir = context.getCacheDir();
                file = new File(cacheDir.getAbsolutePath() + CRASH_DIRECTORY);
            }
            if (!file.exists()) {
                file.mkdirs();
            }
            File file3 = new File(file.getAbsolutePath(), "crash_" + System.currentTimeMillis() + "_" + formatter.format(new Date()) + ".log");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file3);
                stringBuffer.append(stringWriter2);
                fileOutputStream.write(stringBuffer.toString().getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
                return file3;
            } catch (Exception e2) {
                e = e2;
                file2 = file3;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return file2;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0012, code lost:
        r1 = r0.listFiles();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void uploadNativeUncaughtException(final android.content.Context r4) {
        /*
            java.io.File r0 = new java.io.File
            java.io.File r1 = r4.getExternalCacheDir()
            java.lang.String r2 = "dcCrashDump"
            r0.<init>(r1, r2)
            boolean r1 = r0.exists()
            if (r1 != 0) goto L_0x0012
            return
        L_0x0012:
            java.io.File[] r1 = r0.listFiles()
            if (r1 != 0) goto L_0x0019
            return
        L_0x0019:
            io.dcloud.common.util.ThreadPool r2 = io.dcloud.common.util.ThreadPool.self()
            io.dcloud.common.adapter.util.UEH$2 r3 = new io.dcloud.common.adapter.util.UEH$2
            r3.<init>(r4, r1, r0)
            r2.addThreadTask(r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.util.UEH.uploadNativeUncaughtException(android.content.Context):void");
    }

    /* access modifiers changed from: private */
    public static void commitUncatchException(Context context, Throwable th) {
        if (io.dcloud.e.c.h.a.c(context)) {
            StringWriter stringWriter = new StringWriter();
            th.printStackTrace(new PrintWriter(stringWriter));
            String stringWriter2 = stringWriter.toString();
            StringBuffer stringBuffer = new StringBuffer();
            commitBaseUncatchInfo(context, stringBuffer);
            stringBuffer.append("&etype=1");
            stringBuffer.append("&log=" + PdrUtil.encodeURL(stringWriter2));
            commitErrorLog(stringBuffer.toString());
        }
    }
}
