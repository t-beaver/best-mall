package io.dcloud.feature.gg.dcloud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MotionEvent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.dcloud.android.widget.dialog.DCloudAlertDialog;
import com.facebook.common.callercontext.ContextChain;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXConfig;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.ui.component.WXBasicComponentType;
import com.taobao.weex.ui.component.WXComponent;
import io.dcloud.application.DCLoudApplicationImpl;
import io.dcloud.common.DHInterface.DAI;
import io.dcloud.common.DHInterface.ILoadCallBack;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.util.ADUtils;
import io.dcloud.common.util.AESUtil;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.CreateShortResultReceiver;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.NetTool;
import io.dcloud.common.util.NetworkTypeUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.TelephonyUtil;
import io.dcloud.common.util.TestUtil;
import io.dcloud.common.util.ThreadPool;
import io.dcloud.common.util.ZipUtils;
import io.dcloud.common.util.hostpicker.HostPicker;
import io.dcloud.e.c.h.b;
import io.dcloud.feature.gg.AdSplashUtil;
import io.dcloud.feature.gg.dcloud.ADResult;
import io.dcloud.feature.uniapp.adapter.AbsURIAdapter;
import io.dcloud.h.c.a;
import io.dcloud.h.c.c.a.b.d;
import io.dcloud.sdk.core.DCloudAOLManager;
import io.dcloud.sdk.core.util.Const;
import io.dcloud.sdk.poly.base.utils.PrivacyManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ADHandler {
    public static final String AdTag = "_adio.dcloud.feature.ad.dcloud.ADHandler";
    private static final String File_Data = "data.json";
    private static final String File_Gif = "img.gif";
    private static final String File_Img = "img.png";
    private static final String File_S = "s.txt";
    private static final String File_Tid = "tid.txt";
    /* access modifiers changed from: private */
    public static LinkedList<File> expiresFileList = null;
    /* access modifiers changed from: private */
    public static d handler = null;
    static boolean isPullFor360 = false;
    static boolean sNeedShowSkipView = false;
    static long sPullBeginTime;

    public static class ADReceiver implements IADReceiver {
        Context mContext = null;
        long mStartTime = System.currentTimeMillis();

        public ADReceiver(Context context) {
            this.mContext = context;
        }

        private void broadcastADReceive() {
            Intent intent = new Intent();
            intent.setAction("ad_receive");
            intent.putExtra("begin", this.mStartTime);
            intent.putExtra("end", System.currentTimeMillis());
            this.mContext.sendBroadcast(intent);
            ADHandler.log("ADReceive", "broadcastADReceive");
        }

        private void pap(JSONObject jSONObject) {
            if (jSONObject != null && Boolean.valueOf(jSONObject.has("pap")).booleanValue()) {
                Context context = this.mContext;
                boolean z = true;
                if (jSONObject.optInt("pap") != 1) {
                    z = false;
                }
                ADHandler.handleSplashAdEnable(context, Boolean.valueOf(z));
            }
        }

        public void onError(String str, String str2) {
            broadcastADReceive();
        }

        public void onReceiver(JSONObject jSONObject) {
            JSONArray optJSONArray = jSONObject.optJSONArray("psas");
            boolean z = true;
            if (jSONObject.optInt("pap", 0) != 1) {
                z = false;
            }
            SP.setBundleData(this.mContext, ADHandler.AdTag, "dpap", jSONObject.optString("dpap", WXInstanceApm.VALUE_ERROR_CODE_DEFAULT));
            if (optJSONArray == null || !z) {
                ADHandler.log("ADReceiver", "onReceiver no data = " + jSONObject);
            } else {
                long currentTimeMillis = System.currentTimeMillis();
                int length = optJSONArray.length();
                ADHandler.log("ADReceiver", "onReceiver psas.length = " + length + "; data=" + jSONObject);
                for (int i = 0; i < length; i++) {
                    ADHandler.handleAdData(this.mContext, optJSONArray.optJSONObject(i), currentTimeMillis);
                }
            }
            if (!ADHandler.isPullFor360) {
                broadcastADReceive();
            }
        }
    }

    public static class AdData {
        int mEClick = 0;
        int mEShow = 0;
        public Object mImgData;
        String mImgPath;
        String mImgSrc;
        JSONObject mJsonData;
        MotionEvent mMotionEvent_down;
        MotionEvent mMotionEvent_up;
        String mOriginalAppid;
        String mProvider;

        /* access modifiers changed from: package-private */
        public boolean check() {
            return (this.mJsonData == null || this.mImgData == null) ? false : true;
        }

        /* access modifiers changed from: package-private */
        public JSONObject data() {
            return this.mJsonData.optJSONObject("data");
        }

        /* access modifiers changed from: package-private */
        public JSONObject full() {
            return this.mJsonData;
        }

        /* access modifiers changed from: package-private */
        public boolean isEClick() {
            return this.mEClick == 1;
        }

        /* access modifiers changed from: package-private */
        public boolean isEShow() {
            return this.mEShow == 1;
        }

        /* access modifiers changed from: package-private */
        public void listenADReceive(Context context, final IADReceiver iADReceiver) {
            if (iADReceiver != null) {
                AnonymousClass1 r0 = new BroadcastReceiver() {
                    public void onReceive(Context context, Intent intent) {
                        try {
                            long longExtra = intent.getLongExtra("end", 0) - intent.getLongExtra("begin", 0);
                            ADHandler.log("ADReceive", "useTime=" + longExtra);
                            if (longExtra <= 3000) {
                                iADReceiver.onReceiver((JSONObject) null);
                            }
                            ADHandler.log("ADReceive", "unregisterReceiver");
                            context.unregisterReceiver(this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                try {
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("ad_receive");
                    LocalBroadcastManager.getInstance(context).registerReceiver(r0, intentFilter);
                    ADHandler.log("ADReceive", "registerReceiver");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public JSONObject report() {
            return this.mJsonData.optJSONObject("report");
        }
    }

    interface AdDataWatcher<E> {
        boolean find();

        void operate(E e);
    }

    interface ThreadTask {
        void execute();
    }

    public static Boolean SplashAdIsEnable(Context context) {
        if (handler == null) {
            handler = (d) a.d().a();
        }
        return Boolean.valueOf(handler.a(context));
    }

    private static void addThreadTask(final ThreadTask threadTask) {
        if (threadTask == null) {
            return;
        }
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            ThreadPool.self().addThreadTask(new Runnable() {
                public void run() {
                    ThreadTask.this.execute();
                }
            });
        } else {
            threadTask.execute();
        }
    }

    public static boolean allReady(Context context) {
        return !TextUtils.isEmpty(get(context, "appid"));
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0042 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void analysisPullData(byte[] r6, io.dcloud.feature.gg.dcloud.IADReceiver... r7) throws org.json.JSONException {
        /*
            if (r6 != 0) goto L_0x0003
            return
        L_0x0003:
            java.lang.String r0 = new java.lang.String
            r0.<init>(r6)
            org.json.JSONObject r6 = new org.json.JSONObject
            r6.<init>(r0)
            r0 = -1
            java.lang.String r1 = "ret"
            int r0 = r6.optInt(r1, r0)
            r1 = 0
            if (r0 != 0) goto L_0x005d
            java.lang.String r0 = "data"
            boolean r2 = r6.has(r0)
            if (r2 == 0) goto L_0x004a
            java.lang.String r2 = r6.optString(r0)
            java.lang.String r3 = io.dcloud.f.a.c()
            java.lang.String r4 = io.dcloud.f.a.b()
            r5 = 2
            byte[] r2 = android.util.Base64.decode(r2, r5)
            java.lang.String r2 = io.dcloud.common.util.AESUtil.decrypt((java.lang.String) r3, (java.lang.String) r4, (byte[]) r2)
            if (r2 == 0) goto L_0x004a
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ Exception -> 0x0042 }
            r3.<init>(r2)     // Catch:{ Exception -> 0x0042 }
            io.dcloud.feature.gg.AdSplashUtil.decodeChannel(r3)     // Catch:{ Exception -> 0x0042 }
            r6.put(r0, r3)     // Catch:{ Exception -> 0x0042 }
            goto L_0x004a
        L_0x0042:
            org.json.JSONArray r3 = new org.json.JSONArray     // Catch:{ Exception -> 0x004a }
            r3.<init>(r2)     // Catch:{ Exception -> 0x004a }
            r6.put(r0, r3)     // Catch:{ Exception -> 0x004a }
        L_0x004a:
            int r0 = r7.length
        L_0x004b:
            if (r1 >= r0) goto L_0x0055
            r2 = r7[r1]
            r2.onReceiver(r6)
            int r1 = r1 + 1
            goto L_0x004b
        L_0x0055:
            io.dcloud.h.c.c.a.b.d r7 = handler
            if (r7 == 0) goto L_0x007f
            r7.a(r6)
            goto L_0x007f
        L_0x005d:
            int r2 = r7.length
        L_0x005e:
            java.lang.String r3 = "desc"
            if (r1 >= r2) goto L_0x0072
            r4 = r7[r1]
            java.lang.String r5 = java.lang.String.valueOf(r0)
            java.lang.String r3 = r6.optString(r3)
            r4.onError(r5, r3)
            int r1 = r1 + 1
            goto L_0x005e
        L_0x0072:
            io.dcloud.h.c.c.a.b.d r7 = handler
            if (r7 == 0) goto L_0x007f
            java.lang.String r6 = r6.optString(r3)
            r0 = -5007(0xffffffffffffec71, float:NaN)
            r7.a(r0, r6)
        L_0x007f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.gg.dcloud.ADHandler.analysisPullData(byte[], io.dcloud.feature.gg.dcloud.IADReceiver[]):void");
    }

    static String appid(Context context) {
        return get(context, "appid");
    }

    static int bg(Context context) {
        int stringToColor = PdrUtil.stringToColor(get(context, "bg"));
        if (stringToColor != -1) {
            return stringToColor;
        }
        return -1;
    }

    static void click(final Context context, final AdData adData, final String str) {
        final String optString = adData.data().optString("tid");
        ThreadPool.self().addThreadTask(new Runnable() {
            public void run() {
                JSONObject jSONObject;
                int i;
                if (!AdData.this.isEClick()) {
                    jSONObject = ADHandler.getClickData(AdData.this);
                    i = 41;
                } else {
                    jSONObject = null;
                    i = 46;
                }
                JSONObject full = AdData.this.full();
                b.a(context, AdData.this.mOriginalAppid, optString, str, i, (String) null, (String) null, jSONObject, (String) null, (String) null, AdSplashUtil.getSplashAdpId("_adpid_", "UNIAD_SPLASH_ADPID"), (full == null || !full.has("ua")) ? "" : full.optString("ua"), (HashMap<String, Object>) null);
            }
        });
        if ("wanka".equals(adData.mProvider)) {
            ADHandler_wanka.click_wanka(context, adData, str);
        } else if ("youdao".equals(adData.mProvider)) {
            ADHandler_youdao.click_youdao(context, adData, str);
        } else if ("common".equals(adData.mProvider)) {
            ADhandler_common.click_common(context, adData, str);
        } else {
            click_base(context, adData, str);
        }
    }

    static void click_base(final Context context, final AdData adData, final String str) {
        JSONObject data = adData.data();
        final String optString = adData.data().optString("tid");
        if (!data.has("dplk") || !ADUtils.openDeepLink(context, data.optString("dplk"))) {
            String optString2 = data.optString("action");
            if (TextUtils.equals("url", optString2)) {
                if (adData.isEClick()) {
                    ADSim.openUrl(context, data.optString("url"));
                } else {
                    ADUtils.openUrl(context, data.optString("url"));
                }
            } else if (TextUtils.equals(AbsoluteConst.SPNAME_DOWNLOAD, optString2)) {
                long j = 0;
                if (data.has("expires")) {
                    try {
                        j = new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(data.optString("expires")).getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                long j2 = j;
                String optString3 = adData.full() != null ? adData.full().optString("ua") : "";
                if (adData.isEClick()) {
                    ADSim.dwApp(context, adData.mOriginalAppid, optString, str, data.optString("url"), data.optString(AbsURIAdapter.BUNDLE), (ILoadCallBack) null, optString3);
                } else {
                    ADUtils.dwApp(context, adData.mOriginalAppid, optString, str, data.optString("url"), data.optString("downloadAppName"), data.optString(AbsURIAdapter.BUNDLE), j2, true, true, optString3);
                }
            } else if (!TextUtils.equals(AbsoluteConst.XML_STREAMAPP, optString2) || !data.has("appid")) {
                if (TextUtils.equals("browser", optString2) && !adData.isEClick()) {
                    ADUtils.openBrowser(context, data.optString("url"));
                }
            } else if (!adData.isEClick()) {
                ADUtils.openStreamApp(context, data.optString("appid"), data.optJSONObject("parameters"), -1, data.optString("streamapps"));
            }
        } else if (!adData.isEClick()) {
            ThreadPool.self().addThreadTask(new Runnable() {
                public void run() {
                    JSONObject full = AdData.this.full();
                    TestUtil.PointTime.commitTid(context, AdData.this.mOriginalAppid, optString, str, 50, AdSplashUtil.getSplashAdpId("_adpid_", "UNIAD_SPLASH_ADPID"), false, (full == null || !full.has("ua")) ? "" : full.optString("ua"));
                }
            });
            if ("wanka".equals(adData.mProvider)) {
                ADHandler_wanka.dplk_wanka(context, adData, str);
            } else if ("youdao".equals(adData.mProvider)) {
                ADHandler_youdao.dplk_youdao(context, adData, str);
            } else if ("common".equals(adData.mProvider)) {
                ADhandler_common.handletask_common(context, adData, str, "dptracker");
            }
        }
    }

    private static Boolean defAdConfig(Context context) {
        try {
            return Boolean.valueOf(context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.getBoolean("DCLOUD_AD_SPLASH", false));
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

    private static void exec5Plus(final List<HostPicker.Host> list, String str, final IADReceiver[] iADReceiverArr) {
        String str2;
        try {
            str2 = URLEncoder.encode(Base64.encodeToString(AESUtil.encrypt(io.dcloud.f.a.c(), io.dcloud.f.a.b(), ZipUtils.zipString(str)), 2), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            str2 = null;
        }
        final String str3 = "edata=" + str2;
        final boolean z = !hasOtherAd();
        addThreadTask(new ThreadTask() {
            public void execute() {
                List list = list;
                if (list != null) {
                    ADHandler.pull((List<HostPicker.Host>) list, "ThirdConfig", str3, z, iADReceiverArr);
                    return;
                }
                ArrayList arrayList = new ArrayList();
                for (String next : io.dcloud.h.c.c.b.b.b.keySet()) {
                    HostPicker.Host.PriorityEnum priorityEnum = HostPicker.Host.PriorityEnum.BACKUP;
                    int intValue = io.dcloud.h.c.c.b.b.b.get(next).intValue();
                    if (intValue != -1) {
                        if (intValue == 0) {
                            priorityEnum = HostPicker.Host.PriorityEnum.NORMAL;
                        } else if (intValue == 1) {
                            priorityEnum = HostPicker.Host.PriorityEnum.FIRST;
                        }
                    }
                    arrayList.add(new HostPicker.Host(next, priorityEnum));
                }
                ADHandler.pull((List<HostPicker.Host>) arrayList, "Splash", str3, z, iADReceiverArr);
            }
        });
    }

    /* access modifiers changed from: private */
    public static void fileAdData(Context context, File file, AdData adData) {
        try {
            JSONObject jSONObject = new JSONObject(new String(DHFile.readAll(file.getAbsolutePath() + "/" + File_Data)));
            JSONObject optJSONObject = jSONObject.optJSONObject("data");
            if (optJSONObject != null) {
                adData.mProvider = jSONObject.optString("provider");
                adData.mJsonData = jSONObject;
                adData.mEShow = jSONObject.optInt("es", 0);
                adData.mEClick = jSONObject.optInt("ec", 0);
                String optString = optJSONObject.optString("src");
                adData.mImgSrc = optString;
                boolean endsWith = optString.toLowerCase(Locale.ENGLISH).endsWith(".gif");
                StringBuilder sb = new StringBuilder();
                sb.append(file.getAbsolutePath());
                sb.append("/");
                sb.append(endsWith ? File_Gif : File_Img);
                String sb2 = sb.toString();
                String str = file.getAbsolutePath() + "/" + File_S;
                if (new File(sb2).exists() && !new File(str).exists()) {
                    if (endsWith) {
                        adData.mImgData = PlatformUtil.newInstance("pl.droidsonroids.gif.GifDrawable", new Class[]{String.class}, new Object[]{jSONObject.optString("srcPath")});
                    } else {
                        Bitmap decodeFile = BitmapFactory.decodeFile(sb2);
                        if (decodeFile != null) {
                            adData.mImgData = decodeFile;
                        }
                    }
                    adData.mImgPath = sb2;
                    new File(str).createNewFile();
                    DHFile.delete(file.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String formatUrl(String str, JSONObject jSONObject) {
        try {
            str = str.replace("${User-Agent}", URLEncoder.encode(jSONObject.optString("u-a"), "utf-8"));
            str = str.replace("${click_id}", jSONObject.optString("click_id"));
            str = str.replace("${down_x}", String.valueOf(jSONObject.optInt("down_x", DCloudAlertDialog.DARK_THEME)));
            str = str.replace("${down_y}", String.valueOf(jSONObject.optInt("down_y", DCloudAlertDialog.DARK_THEME)));
            str = str.replace("${up_x}", String.valueOf(jSONObject.optInt("up_x", DCloudAlertDialog.DARK_THEME)));
            str = str.replace("${up_y}", String.valueOf(jSONObject.optInt("up_y", DCloudAlertDialog.DARK_THEME)));
            str = str.replace("${relative_down_x}", String.valueOf(jSONObject.optInt("relative_down_x", DCloudAlertDialog.DARK_THEME)));
            str = str.replace("${relative_down_y}", String.valueOf(jSONObject.optInt("relative_down_y", DCloudAlertDialog.DARK_THEME)));
            str = str.replace("${relative_up_x}", String.valueOf(jSONObject.optInt("relative_up_x", DCloudAlertDialog.DARK_THEME)));
            return str.replace("${relative_up_y}", String.valueOf(jSONObject.optInt("relative_up_y", DCloudAlertDialog.DARK_THEME)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }
    }

    private static void get(StringBuilder sb, String str, String str2) {
        try {
            Class.forName(io.dcloud.f.a.b(str));
            sb.append(",");
            sb.append(str2);
        } catch (Exception unused) {
        }
    }

    static JSONObject getArgsJsonData(JSONObject jSONObject) {
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject2.put("ua", jSONObject.optString("ua"));
            jSONObject2.put("down_x", jSONObject.optInt("down_x"));
            jSONObject2.put("down_y", jSONObject.optInt("down_y"));
            jSONObject2.put("up_x", jSONObject.optInt("up_x"));
            jSONObject2.put("up_y", jSONObject.optInt("up_y"));
            jSONObject2.put("relative_down_x", jSONObject.optInt("relative_down_x"));
            jSONObject2.put("relative_down_y", jSONObject.optInt("relative_down_y"));
            jSONObject2.put("relative_up_x", jSONObject.optInt("relative_up_x"));
            jSONObject2.put("relative_up_y", jSONObject.optInt("relative_up_y"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jSONObject2;
    }

    private static String getBId() {
        try {
            File file = new File("/proc/sys/kernel/random/boot_id");
            if (file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] bArr = new byte[37];
                fileInputStream.read(bArr);
                String str = new String(bArr);
                try {
                    fileInputStream.close();
                    return str;
                } catch (Exception unused) {
                    return str;
                }
            }
        } catch (Exception unused2) {
        }
        return "";
    }

    public static AdData getBestAdData(Context context, String str) {
        return getBestAdData(context, str, new AdData());
    }

    /* access modifiers changed from: private */
    public static JSONObject getClickData(AdData adData) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(WXBasicComponentType.IMG, adData.mImgSrc);
            JSONObject full = adData.full();
            jSONObject.put("dw", full.optInt("dw"));
            jSONObject.put("dh", full.optInt("dh"));
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("dx", full.optInt("down_x"));
            jSONObject2.put(Constants.Name.DISTANCE_Y, full.optInt("down_y"));
            jSONObject2.put("ux", full.optInt("up_x"));
            jSONObject2.put("uy", full.optInt("up_y"));
            jSONObject2.put("rdx", full.optInt("relative_down_x"));
            jSONObject2.put("rdy", full.optInt("relative_down_y"));
            jSONObject2.put("rux", full.optInt("relative_up_x"));
            jSONObject2.put("ruy", full.optInt("relative_up_y"));
            jSONObject.put("click_coord", jSONObject2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    private static String getRootPath(Context context) {
        File externalCacheDir = context.getExternalCacheDir();
        if (externalCacheDir == null) {
            return "/sdcard/Android/data/" + context.getPackageName() + "/cache/ad/";
        }
        return externalCacheDir.getAbsolutePath() + "/ad/";
    }

    private static String getUT() {
        try {
            Process exec = Runtime.getRuntime().exec("stat -c \"%x\" /data/data");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            char[] cArr = new char[256];
            while (true) {
                int read = bufferedReader.read(cArr);
                if (read <= 0) {
                    break;
                }
                stringBuffer.append(cArr, 0, read);
            }
            bufferedReader.close();
            exec.waitFor();
            String[] split = stringBuffer.toString().replace(JSUtil.QUOTE, "").split("\\.");
            long time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(split[0]).getTime();
            String str = split[1];
            if (str.contains(Operators.PLUS)) {
                str = str.substring(0, str.indexOf(Operators.PLUS));
            }
            long parseLong = Long.parseLong(str.trim());
            return (time / 1000) + Operators.DOT_STR + parseLong;
        } catch (Exception unused) {
            return Operators.DOT_STR;
        }
    }

    private static void handleAdBaseData(final Context context, final JSONObject jSONObject, String str, final String str2, String str3) throws Exception {
        if (jSONObject == null || !jSONObject.has("es") || jSONObject.getInt("es") != 1) {
            System.currentTimeMillis();
            byte[] bytes = str.getBytes();
            DHFile.writeFile(bytes, 0, str3 + File_Tid);
            StringBuilder sb = new StringBuilder();
            sb.append(str3);
            sb.append(str2.endsWith(".gif") ? File_Gif : File_Img);
            final String sb2 = sb.toString();
            jSONObject.put("srcPath", sb2);
            byte[] bytes2 = jSONObject.toString().getBytes();
            DHFile.writeFile(bytes2, 0, str3 + File_Data);
            addThreadTask(new ThreadTask() {
                public void execute() {
                    HashMap hashMap;
                    byte[] bArr = null;
                    if (!jSONObject.has("ua") || !jSONObject.optString("ua").equalsIgnoreCase("webview")) {
                        hashMap = null;
                    } else {
                        hashMap = new HashMap();
                        hashMap.put(IWebview.USER_AGENT, ADHandler.get("ua-webview"));
                    }
                    boolean z = true;
                    try {
                        bArr = NetTool.httpGet(str2, (HashMap<String, String>) hashMap, true);
                    } catch (Exception unused) {
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("download file is nulll");
                    sb.append(bArr == null);
                    sb.append("src=");
                    sb.append(str2);
                    ADHandler.log("shutao", sb.toString());
                    if (bArr != null) {
                        DHFile.writeFile(bArr, 0, sb2);
                    }
                    if (!ADHandler.isPullFor360) {
                        Intent intent = new Intent();
                        intent.setAction("ad_img_downlaod_receive");
                        if (bArr == null) {
                            z = false;
                        }
                        intent.putExtra("downloadImage", z);
                        intent.putExtra("src", str2);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        ADHandler.log("shutao", "handleAdBaseData--downloadC");
                    }
                }
            });
            return;
        }
        new ADSim(context, jSONObject).start();
    }

    /* access modifiers changed from: private */
    public static void handleAdData(Context context, JSONObject jSONObject, long j) {
        try {
            String optString = jSONObject.optString("provider");
            if ("dcloud".equals(optString)) {
                handleAdData_dcloud(context, jSONObject, j);
            } else if ("wanka".equals(optString)) {
                ADHandler_wanka.handleAdData_wanka(context, jSONObject, j);
            } else if ("youdao".equals(optString)) {
                ADHandler_youdao.handleAdData_youdao(context, jSONObject, j);
            } else if ("common".equals(optString)) {
                ADhandler_common.handleAdData_common(context, jSONObject, j);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void handleAdData_dcloud(Context context, JSONObject jSONObject, long j) throws Exception {
        String rootPath = getRootPath(context);
        JSONObject optJSONObject = jSONObject.optJSONObject("data");
        Date parse = new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(optJSONObject.optString("expires"));
        if (parse.getTime() > System.currentTimeMillis()) {
            String optString = optJSONObject.optString("src");
            String encode = URLEncoder.encode(optString, "utf-8");
            handleAdBaseData(context, jSONObject, optJSONObject.optString("tid"), optString, rootPath + j + "/" + parse.getTime() + "/" + encode.hashCode() + "/");
        }
    }

    public static void handleSplashAdEnable(Context context, Boolean bool) {
        d dVar = handler;
        if (dVar != null) {
            dVar.a(context, bool.booleanValue() ? "1" : WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
        }
    }

    private static boolean hasOtherAd() {
        String bundleData = SP.getBundleData(AdTag, "pspType");
        if (!TextUtils.isEmpty(bundleData)) {
            return bundleData.contains("360") || bundleData.contains(Const.TYPE_GDT) || bundleData.contains(Const.TYPE_CSJ);
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0030  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0036 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static android.graphics.drawable.Drawable img(android.content.Context r2) {
        /*
            java.lang.String r0 = "img"
            java.lang.String r2 = get(r2, r0)
            boolean r0 = android.text.TextUtils.isEmpty(r2)
            r1 = 0
            if (r0 != 0) goto L_0x002d
            boolean r0 = io.dcloud.common.util.PdrUtil.isDeviceRootDir(r2)
            if (r0 == 0) goto L_0x0023
            java.io.File r0 = new java.io.File
            r0.<init>(r2)
            boolean r0 = r0.exists()
            if (r0 == 0) goto L_0x002d
            android.graphics.Bitmap r2 = android.graphics.BitmapFactory.decodeFile(r2)
            goto L_0x002e
        L_0x0023:
            r0 = 0
            java.io.InputStream r2 = io.dcloud.common.adapter.util.PlatformUtil.getInputStream(r2, r0)
            android.graphics.Bitmap r2 = android.graphics.BitmapFactory.decodeStream(r2)
            goto L_0x002e
        L_0x002d:
            r2 = r1
        L_0x002e:
            if (r2 == 0) goto L_0x0036
            android.graphics.drawable.BitmapDrawable r0 = new android.graphics.drawable.BitmapDrawable
            r0.<init>(r2)
            return r0
        L_0x0036:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.gg.dcloud.ADHandler.img(android.content.Context):android.graphics.drawable.Drawable");
    }

    private static void listExpiresAdData(Context context, AdDataWatcher<File> adDataWatcher) {
        File file = new File(getRootPath(context));
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            sortDesc(listFiles);
            for (File file2 : listFiles) {
                if (!adDataWatcher.find()) {
                    File[] listFiles2 = file2.listFiles();
                    for (File file3 : listFiles2) {
                        if (Long.parseLong(file3.getName()) <= System.currentTimeMillis()) {
                            DHFile.delete(file3);
                        } else if (!adDataWatcher.find()) {
                            File[] listFiles3 = file3.listFiles();
                            for (File operate : listFiles3) {
                                adDataWatcher.operate(operate);
                                if (adDataWatcher.find()) {
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    DHFile.delete(file2);
                }
            }
        }
    }

    static void log(String str, String str2) {
    }

    static String mc(Context context) {
        if (TextUtils.isEmpty(BaseInfo.sChannel) || TextUtils.equals("default", BaseInfo.sChannel)) {
            try {
                return context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.getString("DCLOUD_STREAMAPP_CHANNEL", BaseInfo.sChannel);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return BaseInfo.sChannel;
    }

    static String name(Context context) {
        return get(context, "name");
    }

    public static String papEnable(Context context) {
        String str = getRootPath(context).replaceAll("/ad/", "/") + "AdEnable.dat";
        try {
            if (DHFile.isExist(str)) {
                return new String(DHFile.readAll(str));
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static void postSplashError(Context context, String str, String str2, JSONArray jSONArray) {
        String str3;
        HashMap hashMap = new HashMap();
        hashMap.put(ContextChain.TAG_PRODUCT, "a");
        hashMap.put(CreateShortResultReceiver.KEY_VERSIONNAME, get(CreateShortResultReceiver.KEY_VERSIONNAME));
        hashMap.put("appid", get("appid"));
        hashMap.put("name", get(context, "name"));
        hashMap.put("pn", context.getPackageName());
        try {
            str3 = context.getPackageManager().getPackageInfo(context.getPackageName(), 1).versionName;
        } catch (Exception unused) {
            str3 = null;
        }
        hashMap.put("pv", str3);
        hashMap.put(ContextChain.TAG_INFRA, Base64.encodeToString(AESUtil.encrypt(io.dcloud.f.a.c(), io.dcloud.f.a.b(), ZipUtils.zipString(TelephonyUtil.getIMEI(context, true, true))), 2));
        hashMap.put("md", Build.MODEL);
        hashMap.put("vd", Build.MANUFACTURER);
        hashMap.put(WXConfig.os, Integer.valueOf(Build.VERSION.SDK_INT));
        String str4 = "1.9.9.81676";
        if (PdrUtil.isEmpty(str4)) {
            str4 = "";
        }
        hashMap.put("vb", str4);
        hashMap.put("net", Integer.valueOf(NetworkTypeUtil.getNetworkType(context)));
        hashMap.put("mc", mc(context));
        hashMap.put("paid", get(context, "adid"));
        hashMap.put("dw", Integer.valueOf(AdSplashUtil.dw(context)));
        hashMap.put("dh", Integer.valueOf(AdSplashUtil.dh(context)));
        hashMap.put(c.a, str);
        hashMap.put(WXComponent.PROP_FS_MATCH_PARENT, str2);
        if (jSONArray != null) {
            hashMap.put("d", jSONArray);
        }
        final String jSONObject = new JSONObject(hashMap).toString();
        addThreadTask(new ThreadTask() {
            public void execute() {
                NetTool.httpPost("https://96f0e031-f37a-48ef-84c7-2023f6360c0a.bspapp.com/http/splash-screen/report", jSONObject, new HashMap());
            }
        });
    }

    public static void pr(Context context, Map<String, Object> map) {
        String str;
        map.put("name", get(context, "name"));
        try {
            str = context.getPackageManager().getPackageInfo(context.getPackageName(), 1).versionName;
        } catch (Exception unused) {
            str = null;
        }
        map.put("pv", str);
        map.put(WXConfig.os, Integer.valueOf(Build.VERSION.SDK_INT));
        String str2 = "1.9.9.81676";
        if (PdrUtil.isEmpty(str2)) {
            str2 = "";
        }
        map.put("vb", str2);
        pullRad(context, map, new ADReceiver(context), new ADResult.CADReceiver(context));
    }

    private static String psap(Context context) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String append : Support.Surpport) {
            stringBuffer.append(append);
            stringBuffer.append(",");
        }
        return stringBuffer.length() > 0 ? stringBuffer.substring(0, stringBuffer.length() - 1) : stringBuffer.toString();
    }

    private static String psas(Context context) {
        final StringBuffer stringBuffer = new StringBuffer();
        listExpiresAdData(context, new AdDataWatcher<File>() {
            public boolean find() {
                return false;
            }

            public void operate(File file) {
                byte[] readAll = DHFile.readAll(file.getAbsolutePath() + "/" + ADHandler.File_Tid);
                if (readAll != null) {
                    String str = new String(readAll);
                    StringBuffer stringBuffer = stringBuffer;
                    stringBuffer.append(str);
                    stringBuffer.append(",");
                }
            }
        });
        return stringBuffer.length() > 0 ? stringBuffer.substring(0, stringBuffer.length() - 1) : stringBuffer.toString();
    }

    public static void pull(final Context context, String str, boolean z, List<HostPicker.Host> list, IADReceiver... iADReceiverArr) {
        if (TextUtils.isEmpty(BaseInfo.sDefaultBootApp)) {
            BaseInfo.parseControl();
        }
        boolean z2 = false;
        boolean startsWith = !TextUtils.isEmpty(BaseInfo.sDefaultBootApp) ? BaseInfo.sDefaultBootApp.startsWith("__UNI__") : false;
        String[] split = mc(context).split("\\|");
        String str2 = (split == null || split.length <= 2) ? "" : split[2];
        DCloudAOLManager.InitConfig initConfig = new DCloudAOLManager.InitConfig();
        initConfig.setAdId(str2).setAppId(BaseInfo.sDefaultBootApp);
        io.dcloud.sdk.core.a.a.a(context, initConfig, new d());
        AnonymousClass1 r0 = new PrivacyManager.a() {
            public String getAndroidId() {
                return TelephonyUtil.getAId(context);
            }

            public String[] getImeis() {
                return TelephonyUtil.getMultiIMEI(context);
            }

            public String getImsi() {
                return TelephonyUtil.getIMSI(context);
            }
        };
        r0.setAllowPrivacy(!AppRuntime.hasPrivacyForNotShown(context));
        DCloudAOLManager.setPrivacyConfig(r0);
        d dVar = (d) a.d().a();
        handler = dVar;
        HashMap<String, Object> b = dVar.b(context);
        b.put(CreateShortResultReceiver.KEY_VERSIONNAME, get(context, CreateShortResultReceiver.KEY_VERSIONNAME));
        b.put("name", get(context, "name"));
        b.put("psas", psas(context));
        b.put("ps", Integer.valueOf(BaseInfo.existsStreamEnv() ? 1 : 0));
        b.put("psd", Integer.valueOf(BaseInfo.ISDEBUG ? 1 : 0));
        Boolean SplashAdIsEnable = SplashAdIsEnable(context);
        Boolean defAdConfig = defAdConfig(context);
        String str3 = "1";
        b.put("pap", SplashAdIsEnable.booleanValue() ? str3 : WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
        b.put("papi", defAdConfig.booleanValue() ? str3 : WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
        b.put("psaf", allReady(context) ? WXInstanceApm.VALUE_ERROR_CODE_DEFAULT : str3);
        String str4 = get("cad");
        if (!TextUtils.isEmpty(str4)) {
            b.put("rad", str4);
        }
        String a = SP.getsOrCreateBundle(context, io.dcloud.f.a.b("IlKgfnao")).a(io.dcloud.f.a.b("[xdi{`IlMfijdm"), AbsoluteConst.TRUE);
        if (a != null && a.equalsIgnoreCase(AbsoluteConst.FALSE)) {
            str3 = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT;
        }
        b.put("mpap", str3);
        b.put("lia", AdSplashUtil.getAL(context));
        String jSONObject = new JSONObject(b).toString();
        if (startsWith) {
            try {
                Object invokeMethod = PlatformUtil.invokeMethod("io.dcloud.common.cs.DA", "getInstance", (Object) null);
                if (invokeMethod != null && (invokeMethod instanceof DAI)) {
                    if (list != null) {
                        ((DAI) invokeMethod).act(jSONObject, new ADResult(iADReceiverArr));
                    } else {
                        ((DAI) invokeMethod).ar(jSONObject, new ADResult(iADReceiverArr));
                    }
                    z2 = true;
                }
            } catch (Exception e) {
                Logger.e("ADHANDLER", e.toString());
            }
        }
        if (!z2) {
            exec5Plus(list, jSONObject, iADReceiverArr);
        }
    }

    static void pullRad(Context context, Map<String, Object> map, final IADReceiver... iADReceiverArr) {
        if (TextUtils.isEmpty(BaseInfo.sDefaultBootApp)) {
            BaseInfo.parseControl();
        }
        HashMap hashMap = new HashMap();
        hashMap.put(ContextChain.TAG_PRODUCT, "a");
        hashMap.put(CreateShortResultReceiver.KEY_VERSIONNAME, get(CreateShortResultReceiver.KEY_VERSIONNAME));
        hashMap.put("appid", BaseInfo.sDefaultBootApp);
        String str = "1.9.9.81676";
        if (PdrUtil.isEmpty(str)) {
            str = "";
        }
        hashMap.put("vb", str);
        hashMap.put("imei", TelephonyUtil.getIMEI(context, true, true));
        if (map != null && map.containsKey("rad") && !PdrUtil.isEmpty(map.get("rad"))) {
            hashMap.put("pn", context.getPackageName());
            hashMap.put("mc", mc(context));
            hashMap.put("paid", get(context, "adid"));
            hashMap.put("psdk", 0);
            hashMap.putAll(map);
            String str2 = null;
            try {
                str2 = URLEncoder.encode(Base64.encodeToString(AESUtil.encrypt(io.dcloud.f.a.c(), io.dcloud.f.a.b(), ZipUtils.zipString(new JSONObject(hashMap).toString())), 2), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            final String str3 = "edata=" + str2;
            final boolean z = !hasOtherAd();
            addThreadTask(new ThreadTask() {
                public void execute() {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(new HostPicker.Host("YHx8eHsyJydrOSZsa2RnfWwmZm18JmtmJ2tnZGRta3wneGR9e2l4eCdraWw=", HostPicker.Host.PriorityEnum.FIRST));
                    ADHandler.pull((List<HostPicker.Host>) arrayList, "CAD", str3, z, iADReceiverArr);
                }
            });
        }
    }

    public static void setSplashAd(Context context, boolean z, IADReceiver... iADReceiverArr) {
        String str;
        HashMap hashMap = new HashMap();
        hashMap.put(ContextChain.TAG_PRODUCT, "a");
        hashMap.put(CreateShortResultReceiver.KEY_VERSIONNAME, get(context, CreateShortResultReceiver.KEY_VERSIONNAME));
        hashMap.put("appid", BaseInfo.sDefaultBootApp);
        hashMap.put("name", get(context, "name"));
        try {
            hashMap.put("pname", context.getApplicationInfo().loadLabel(context.getPackageManager()));
        } catch (Exception unused) {
        }
        hashMap.put("pn", context.getPackageName());
        try {
            str = context.getPackageManager().getPackageInfo(context.getPackageName(), 1).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            str = null;
        }
        hashMap.put("pv", str);
        hashMap.put(WXConfig.os, Integer.valueOf(Build.VERSION.SDK_INT));
        String str2 = "1.9.9.81676";
        if (PdrUtil.isEmpty(str2)) {
            str2 = "";
        }
        hashMap.put("vb", str2);
        hashMap.put("mc", mc(context));
        hashMap.put("psdk", 0);
        hashMap.put("mpap", z ? "1" : WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
        hashMap.put("smpap", "1");
        exec5Plus((List<HostPicker.Host>) null, new JSONObject(hashMap).toString(), iADReceiverArr);
    }

    private static void sortDesc(File[] fileArr) {
        if (fileArr != null) {
            for (int i = 0; i < fileArr.length - 1; i++) {
                int i2 = 0;
                while (i2 < (fileArr.length - 1) - i) {
                    int i3 = i2 + 1;
                    if (Long.parseLong(fileArr[i2].getName()) < Long.parseLong(fileArr[i3].getName())) {
                        File file = fileArr[i2];
                        fileArr[i2] = fileArr[i3];
                        fileArr[i3] = file;
                    }
                    i2 = i3;
                }
            }
        }
    }

    static void view(final Context context, final AdData adData, final String str) {
        final String optString = adData.data().optString("tid");
        ThreadPool.self().addThreadTask(new Runnable() {
            public void run() {
                int i = AdData.this.isEShow() ? 45 : 40;
                JSONObject full = AdData.this.full();
                TestUtil.PointTime.commitTid(context, AdData.this.mOriginalAppid, optString, str, i, AdSplashUtil.getSplashAdpId("_adpid_", "UNIAD_SPLASH_ADPID"), false, (full == null || !full.has("ua")) ? "" : full.optString("ua"));
            }
        });
        if ("wanka".equals(adData.mProvider)) {
            ADHandler_wanka.view_wanka(context, adData, str);
        } else if ("youdao".equals(adData.mProvider)) {
            ADHandler_youdao.view_youdao(context, adData, str);
        } else if ("common".equals(adData.mProvider)) {
            ADhandler_common.handletask_common(context, adData, str, "imptracker");
        }
    }

    static String get(Context context, String str) {
        return SP.getBundleData(context, AdTag, str);
    }

    static AdData getBestAdData(final Context context, String str, final AdData adData) {
        adData.mOriginalAppid = str;
        expiresFileList = new LinkedList<>();
        listExpiresAdData(context, new AdDataWatcher<File>() {
            public boolean find() {
                return AdData.this.check();
            }

            public void operate(File file) {
                ADHandler.expiresFileList.add(file);
                ADHandler.fileAdData(context, file, AdData.this);
            }
        });
        if (!adData.check() && expiresFileList.size() != 0) {
            for (int i = 0; i < expiresFileList.size(); i++) {
                new File(expiresFileList.get(i).getAbsolutePath() + "/" + File_S).delete();
                if (i == 0) {
                    fileAdData(context, expiresFileList.get(i), adData);
                }
            }
        }
        return adData;
    }

    public static String get(String str) {
        return SP.getBundleData(AdTag, str);
    }

    public static void pull(Context context, String str) {
        ArrayList arrayList = new ArrayList();
        for (String next : io.dcloud.h.c.c.b.b.c.keySet()) {
            HostPicker.Host.PriorityEnum priorityEnum = HostPicker.Host.PriorityEnum.BACKUP;
            int intValue = io.dcloud.h.c.c.b.b.c.get(next).intValue();
            if (intValue != -1) {
                if (intValue == 0) {
                    priorityEnum = HostPicker.Host.PriorityEnum.NORMAL;
                } else if (intValue == 1) {
                    priorityEnum = HostPicker.Host.PriorityEnum.FIRST;
                }
            }
            arrayList.add(new HostPicker.Host(next, priorityEnum));
        }
        pull(context, str, false, (List<HostPicker.Host>) arrayList, new ADReceiver(context));
    }

    /* access modifiers changed from: private */
    public static void pull(List<HostPicker.Host> list, String str, final String str2, final boolean z, final IADReceiver... iADReceiverArr) {
        HostPicker.getInstance().pickSuitHost(DCLoudApplicationImpl.self().getContext(), list, str, new HostPicker.HostPickCallback() {
            String[] message = new String[1];

            public boolean doRequest(HostPicker.Host host) {
                byte[] httpPost = NetTool.httpPost(host.getRealHost(), str2, new HashMap(), false, z, this.message);
                if (httpPost == null) {
                    return false;
                }
                try {
                    ADHandler.analysisPullData(httpPost, iADReceiverArr);
                    return true;
                } catch (Exception e) {
                    for (IADReceiver onError : iADReceiverArr) {
                        onError.onError("Exception", e.getMessage());
                    }
                    if (ADHandler.handler == null) {
                        return true;
                    }
                    ADHandler.handler.a(-5007, e.getMessage());
                    return true;
                }
            }

            public void onNoOnePicked() {
                String str;
                IADReceiver[] iADReceiverArr = iADReceiverArr;
                int length = iADReceiverArr.length;
                int i = 0;
                while (true) {
                    str = "data invalid";
                    if (i >= length) {
                        break;
                    }
                    IADReceiver iADReceiver = iADReceiverArr[i];
                    String[] strArr = this.message;
                    if (strArr[0] != null) {
                        str = strArr[0];
                    }
                    iADReceiver.onError("NotFountDataError", str);
                    i++;
                }
                if (ADHandler.handler != null) {
                    d access$100 = ADHandler.handler;
                    String[] strArr2 = this.message;
                    if (strArr2[0] != null) {
                        str = strArr2[0];
                    }
                    access$100.a(-5007, str);
                }
            }

            public void onOneSelected(HostPicker.Host host) {
            }
        });
    }
}
