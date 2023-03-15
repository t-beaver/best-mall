package io.dcloud.common.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.dcloud.android.downloader.DownloadService;
import com.dcloud.android.downloader.callback.DCDownloadManager;
import com.dcloud.android.downloader.callback.DownloadListener;
import com.dcloud.android.downloader.domain.DownloadInfo;
import com.dcloud.android.downloader.exception.DownloadException;
import com.dcloud.android.widget.toast.ToastCompat;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.WebviewActivity;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ILoadCallBack;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.DownloadUtil;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.e.c.h.b;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class ADUtils {
    /* access modifiers changed from: private */
    public static final DownloadListener DOWNLOAD_DC_LISTENER = new DownloadListener() {
        public void onDownloadFailed(DownloadInfo downloadInfo, DownloadException downloadException) {
            downloadException.printStackTrace();
            JSONObject loadData = ADUtils.getLoadData((long) downloadInfo.getId());
            if (loadData != null) {
                ADUtils.downloadCommit(downloadInfo.getContext(), loadData.optString("appid"), loadData.optString("tid"), loadData.optString("adid"), 32, String.valueOf(downloadException.getCode()), downloadException.getMessage(), loadData.optString("ua"));
            }
        }

        public void onDownloadSuccess(DownloadInfo downloadInfo) {
            JSONObject removeDownlaodData = ADUtils.removeDownlaodData(downloadInfo.getContext(), (long) downloadInfo.getId());
            if (removeDownlaodData != null) {
                ADUtils.downloadCommit(downloadInfo.getContext(), removeDownlaodData.optString("appid"), removeDownlaodData.optString("tid"), removeDownlaodData.optString("adid"), 30, (String) null, (String) null, removeDownlaodData.optString("ua"));
                ADUtils.saveLoadAppData(downloadInfo.getContext(), removeDownlaodData.optString("pname"), removeDownlaodData.optString("appid"), removeDownlaodData.optString("tid"), removeDownlaodData.optString("adid"), downloadInfo.getPath(), removeDownlaodData.optString("ua"));
            }
            Intent aPKInstallIntent = DownloadUtil.getAPKInstallIntent(downloadInfo.getContext(), downloadInfo.getPath());
            if (downloadInfo.getTag() == null || !(downloadInfo.getTag() instanceof ILoadCallBack)) {
                downloadInfo.getContext().startActivity(aPKInstallIntent);
            } else {
                ((ILoadCallBack) downloadInfo.getTag()).onCallBack(0, downloadInfo.getContext(), aPKInstallIntent);
            }
            DownloadService.getDownloadManager(downloadInfo.getContext().getApplicationContext()).remove(downloadInfo);
        }

        public void onDownloading(long j, long j2) {
        }

        public void onPaused() {
        }

        public void onRemoved() {
        }

        public void onStart() {
        }

        public void onWaited() {
        }
    };
    private static final String TAG = "ADUtils";

    public static class ADLoadData {
        public String adid;
        public String appid;
        public long expiresTime;
        public long id;
        public String name;
        public String pname;
        public String tid;
        public String type = "default";
        public String ua;
        public String url;
    }

    private ADUtils() {
    }

    public static void checkADDownload(Activity activity) throws Exception {
        Map<String, ?> all = SP.getOrCreateBundle((Context) activity, AbsoluteConst.AD_DOWNLOAD_DATA).getAll();
        if (all.size() > 0) {
            Iterator<?> it = all.values().iterator();
            while (it.hasNext()) {
                JSONObject jSONObject = new JSONObject((String) it.next());
                final long optLong = jSONObject.optLong("id");
                final long optLong2 = jSONObject.optLong("expiresTime");
                final String optString = jSONObject.optString("name");
                final String optString2 = jSONObject.optString("pname");
                final String optString3 = jSONObject.optString("url");
                final String optString4 = jSONObject.optString("appid");
                final String optString5 = jSONObject.optString("tid");
                final String optString6 = jSONObject.optString("adid");
                final String optString7 = jSONObject.optString("type");
                String optString8 = jSONObject.optString("ua");
                final DCDownloadManager downloadManager = DownloadService.getDownloadManager(activity.getApplicationContext());
                final DownloadInfo downloadById = downloadManager.getDownloadById((int) optLong);
                AnonymousClass4 r18 = r0;
                final Activity activity2 = activity;
                final String str = optString8;
                AnonymousClass4 r0 = new Runnable() {
                    public void run() {
                        int i;
                        DownloadInfo downloadInfo = DownloadInfo.this;
                        if (downloadInfo != null) {
                            i = downloadInfo.getStatus();
                        } else {
                            i = 0;
                        }
                        switch (i) {
                            case 0:
                            case 6:
                                ADUtils.removeDownlaodData(activity2, optLong);
                                if (TextUtils.isEmpty(optString7) || !optString7.equalsIgnoreCase("wanka")) {
                                    long j = optLong2;
                                    if (j <= 0 || j >= System.currentTimeMillis()) {
                                        DownloadInfo downloadInfo2 = DownloadInfo.this;
                                        if (downloadInfo2 != null) {
                                            downloadManager.remove(downloadInfo2);
                                        }
                                        ADUtils.dwApp(activity2, optString4, optString5, optString6, optString3, optString, optString2, optLong2, false, i != 6, str);
                                        ToastCompat.makeText((Context) activity2, R.string.dcloud_common_download_tips2, 0).show();
                                        return;
                                    }
                                    DownloadInfo downloadInfo3 = DownloadInfo.this;
                                    if (downloadInfo3 != null) {
                                        downloadManager.remove(downloadInfo3);
                                        return;
                                    }
                                    return;
                                }
                                DownloadInfo downloadInfo4 = DownloadInfo.this;
                                if (downloadInfo4 != null) {
                                    downloadManager.remove(downloadInfo4);
                                    return;
                                }
                                return;
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                                if (DownloadInfo.this != null) {
                                    long j2 = optLong2;
                                    if (j2 <= 0 || j2 >= System.currentTimeMillis()) {
                                        downloadManager.resume(DownloadInfo.this);
                                        DownloadInfo.this.setDownloadListener(ADUtils.DOWNLOAD_DC_LISTENER);
                                        ToastCompat.makeText((Context) activity2, R.string.dcloud_common_download_tips2, 0).show();
                                        return;
                                    }
                                    ADUtils.removeDownlaodData(activity2, optLong);
                                    downloadManager.remove(DownloadInfo.this);
                                    return;
                                }
                                return;
                            case 5:
                                if (DownloadInfo.this != null) {
                                    ADUtils.removeDownlaodData(activity2, optLong);
                                    String path = DownloadInfo.this.getPath();
                                    activity2.startActivity(DownloadUtil.getAPKInstallIntent(activity2, path));
                                    ADUtils.saveLoadAppData(activity2, optString2, optString4, optString5, optString6, path, str);
                                    downloadManager.remove(DownloadInfo.this);
                                    ToastCompat.makeText((Context) activity2, R.string.dcloud_common_download_tips3, 0).show();
                                    return;
                                }
                                return;
                            default:
                                return;
                        }
                    }
                };
                Activity activity3 = activity;
                activity3.runOnUiThread(r18);
                Activity activity4 = activity3;
            }
        }
    }

    public static void downloadCommit(Context context, String str, String str2, String str3, int i, String str4, String str5, String str6) {
        final Context context2 = context;
        final String str7 = str;
        final String str8 = str2;
        final String str9 = str3;
        final int i2 = i;
        final String str10 = str4;
        final String str11 = str5;
        final String str12 = str6;
        ThreadPool.self().addThreadTask(new Runnable() {
            public void run() {
                b.a(context2, str7, str8, str9, i2, str10, str11, (JSONObject) null, (String) null, (String) null, ADUtils.getSplashAdpId(), str12, (HashMap<String, Object>) null);
            }
        });
    }

    public static void dwApp(Context context, String str, String str2, String str3, String str4, String str5, String str6, long j, boolean z, boolean z2, String str7) {
        String str8;
        Context context2 = context;
        String str9 = str4;
        String str10 = str6;
        if (z2) {
            try {
                loadAppTip(context);
            } catch (Exception e) {
                Log.e(TAG, "downloadApk exception: " + e.getMessage());
                e.printStackTrace();
                return;
            }
        }
        if (getDdDataForUrl(str4) == null) {
            String str11 = Operators.DOT_STR + context.getString(R.string.in_package);
            if (TextUtils.isEmpty(str6)) {
                str8 = null;
            } else {
                str8 = str10 + Operators.DOT_STR + context.getString(R.string.in_package);
            }
            String fileNameByUrl = PdrUtil.getFileNameByUrl(str9, str11, str8);
            ADLoadData aDLoadData = new ADLoadData();
            aDLoadData.name = str5;
            aDLoadData.pname = str10;
            aDLoadData.url = str9;
            aDLoadData.expiresTime = j;
            aDLoadData.adid = str3;
            aDLoadData.appid = str;
            aDLoadData.tid = str2;
            aDLoadData.ua = str7;
            aDLoadData.id = loadADFile(context, str, str2, str3, fileNameByUrl, str5, str4, "application/vnd.android.package-archive", new ILoadCallBack() {
                public Object onCallBack(int i, Context context, Object obj) {
                    if (i != 0) {
                        return null;
                    }
                    context.startActivity(new Intent((Intent) obj));
                    return Boolean.TRUE;
                }
            }, z, str7);
            saveLoadData(aDLoadData);
        }
    }

    public static JSONObject getDdDataForUrl(String str) {
        Map<String, ?> all = SP.getOrCreateBundle(AbsoluteConst.AD_DOWNLOAD_DATA).getAll();
        if (all == null || all.size() <= 0) {
            return null;
        }
        Iterator<?> it = all.values().iterator();
        while (it.hasNext()) {
            try {
                JSONObject jSONObject = new JSONObject((String) it.next());
                if (str.equals(jSONObject.optString("url"))) {
                    return jSONObject;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String getDownloadDataKey(long j) {
        return AbsoluteConst.AD_DL_DATA_KEY + String.valueOf(j);
    }

    /* access modifiers changed from: private */
    public static String getLoadAppDataKey(String str) {
        return AbsoluteConst.AD_IA_DATA_KEY + str;
    }

    public static JSONObject getLoadData(long j) {
        try {
            return new JSONObject(SP.getOrCreateBundle(AbsoluteConst.AD_DOWNLOAD_DATA).getString(getDownloadDataKey(j), ""));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getSplashAdpId() {
        return String.valueOf(PlatformUtil.invokeMethod("io.dcloud.feature.gg.AdSplashUtil", "getSplashAdpId", (Object) null, new Class[]{IApp.class, String.class, String.class}, new Object[]{null, "_adpid_", "UNIAD_SPLASH_ADPID"}));
    }

    public static long loadADFile(Context context, String str, String str2, String str3, String str4, String str5, String str6, String str7, ILoadCallBack iLoadCallBack, boolean z, String str8) {
        if (z) {
            downloadCommit(context, str, str2, str3, 29, (String) null, (String) null, str8);
        }
        DCDownloadManager downloadManager = DownloadService.getDownloadManager(context.getApplicationContext());
        StringBuilder sb = new StringBuilder();
        sb.append(DeviceInfo.sDeviceRootDir);
        sb.append("/Download/");
        String str9 = str4;
        sb.append(str4);
        String str10 = str6;
        Context context2 = context;
        DownloadInfo build = new DownloadInfo.Builder().setUrl(str6).setPath(sb.toString()).build(context);
        build.setTag(iLoadCallBack);
        build.setDownloadListener(DOWNLOAD_DC_LISTENER);
        downloadManager.download(build);
        return (long) build.getId();
    }

    public static void loadAppTip(Context context) {
        MessageHandler.sendMessage(new MessageHandler.IMessages() {
            public void execute(Object obj) {
                ToastCompat.makeText((Context) obj, R.string.dcloud_common_download_tips1, 0).show();
            }
        }, context);
    }

    public static void openBrowser(Context context, String str) {
        try {
            Intent intent = new Intent();
            if (!Build.BRAND.equalsIgnoreCase(MobilePhoneModel.VIVO) && LoadAppUtils.isAppLoad(context, "com.android.browser")) {
                intent.setPackage("com.android.browser");
            }
            intent.setData(Uri.parse(str));
            intent.setAction("android.intent.action.VIEW");
            intent.setFlags(268435456);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "openBrowser exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean openDeepLink(Context context, String str) {
        try {
            Intent parseUri = Intent.parseUri(str, 1);
            if (BaseInfo.isDefense) {
                parseUri.setSelector((Intent) null);
                parseUri.setComponent((ComponentName) null);
                parseUri.addCategory("android.intent.category.BROWSABLE");
            }
            List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(parseUri, 65536);
            if (queryIntentActivities == null || queryIntentActivities.isEmpty()) {
                return false;
            }
            parseUri.setFlags(268435456);
            context.startActivity(parseUri);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "openDeepLink exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void openStreamApp(Context context, String str, JSONObject jSONObject, int i, String str2) {
        try {
            Intent intent = new Intent();
            intent.putExtra(IntentConst.START_FROM, i);
            intent.setAction("android.intent.action.MAIN");
            if (!TextUtils.isEmpty(str2)) {
                if ("com.qihoo.appstore".equals(str2)) {
                    intent.setClassName(str2, "io.dcloud.appstream.StreamAppListFakeActivity");
                } else if ("com.aspire.mm".equals(str2)) {
                    intent.setClassName(str2, "io.dcloud.StreamAppLauncherActivity");
                } else {
                    intent.setClassName(str2, "io.dcloud.appstream.StreamAppMainActivity");
                }
            } else if (BaseInfo.existsStreamEnv()) {
                intent.setClassName(context.getPackageName(), "io.dcloud.appstream.StreamAppMainActivity");
            }
            intent.putExtra("appid", str);
            boolean z = true;
            intent.putExtra(IntentConst.IS_STREAM_APP, true);
            if (jSONObject != null) {
                boolean z2 = false;
                if (jSONObject.has("arguments")) {
                    intent.putExtra(IntentConst.EXTRAS, jSONObject.opt("arguments").toString());
                    z2 = true;
                }
                if (jSONObject.has("richurl")) {
                    intent.putExtra(IntentConst.DIRECT_PAGE, jSONObject.optString("richurl"));
                    intent.putExtra(IntentConst.IS_START_FIRST_WEB, true);
                } else {
                    z = z2;
                }
                if (z) {
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("action", AbsoluteConst.XML_APP);
                    jSONObject2.put("parameters", jSONObject);
                    intent.putExtra("rules_msg", jSONObject2.toString());
                }
            }
            intent.setFlags(268435456);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "openStreamApp exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void openUrl(Context context, String str) {
        try {
            Intent intent = new Intent();
            intent.setClass(context, WebviewActivity.class);
            intent.putExtra("url", str);
            intent.setData(Uri.parse(str));
            intent.setAction("android.intent.action.VIEW");
            intent.setFlags(268435456);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "openUrl exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static JSONObject removeDownlaodData(Context context, long j) {
        SharedPreferences orCreateBundle = SP.getOrCreateBundle(context, AbsoluteConst.AD_DOWNLOAD_DATA);
        String downloadDataKey = getDownloadDataKey(j);
        String string = orCreateBundle.getString(downloadDataKey, "");
        orCreateBundle.edit().remove(downloadDataKey).commit();
        try {
            return new JSONObject(string);
        } catch (Exception unused) {
            return null;
        }
    }

    public static JSONObject removeLoadAppData(Context context, String str) {
        JSONObject jSONObject;
        SharedPreferences orCreateBundle = SP.getOrCreateBundle(context, AbsoluteConst.AD_INSTALL_DATA);
        String loadAppDataKey = getLoadAppDataKey(str);
        String string = orCreateBundle.getString(loadAppDataKey, "");
        if (!TextUtils.isEmpty(string)) {
            try {
                jSONObject = new JSONObject(string);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            orCreateBundle.edit().remove(loadAppDataKey).commit();
            return jSONObject;
        }
        jSONObject = null;
        orCreateBundle.edit().remove(loadAppDataKey).commit();
        return jSONObject;
    }

    public static void runThreadCheckADDownload(final Activity activity) {
        ThreadPool.self().addThreadTask(new Runnable() {
            public void run() {
                try {
                    ADUtils.checkADDownload(activity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void saveLoadAppData(Context context, String str, String str2, String str3, String str4, String str5, String str6) {
        final Context context2 = context;
        final String str7 = str;
        final String str8 = str5;
        final String str9 = str2;
        final String str10 = str3;
        final String str11 = str4;
        final String str12 = str6;
        ThreadPool.self().addThreadTask(new Runnable() {
            public void run() {
                SharedPreferences orCreateBundle = SP.getOrCreateBundle(context2, AbsoluteConst.AD_INSTALL_DATA);
                String access$100 = ADUtils.getLoadAppDataKey(str7);
                JSONObject jSONObject = new JSONObject();
                try {
                    if (PdrUtil.isEmpty(str7)) {
                        PackageInfo parseApkInfo = PlatformUtil.parseApkInfo(context2, str8);
                        if (parseApkInfo != null) {
                            jSONObject.put("packName", parseApkInfo.packageName);
                            access$100 = ADUtils.getLoadAppDataKey(parseApkInfo.packageName);
                        } else {
                            return;
                        }
                    } else {
                        jSONObject.put("packName", str7);
                    }
                    jSONObject.put("appid", str9);
                    jSONObject.put("tid", str10);
                    jSONObject.put("adid", str11);
                    jSONObject.put("ua", str12);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                orCreateBundle.edit().putString(access$100, jSONObject.toString()).commit();
            }
        });
    }

    public static void saveLoadData(ADLoadData aDLoadData) throws Exception {
        SharedPreferences orCreateBundle = SP.getOrCreateBundle(AbsoluteConst.AD_DOWNLOAD_DATA);
        String downloadDataKey = getDownloadDataKey(aDLoadData.id);
        if (TextUtils.isEmpty(orCreateBundle.getString(downloadDataKey, ""))) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("name", aDLoadData.name);
            jSONObject.put("url", aDLoadData.url);
            jSONObject.put("pname", aDLoadData.pname);
            jSONObject.put("id", aDLoadData.id);
            jSONObject.put("expiresTime", aDLoadData.expiresTime);
            jSONObject.put("tid", aDLoadData.tid);
            jSONObject.put("appid", aDLoadData.appid);
            jSONObject.put("adid", aDLoadData.adid);
            jSONObject.put("type", aDLoadData.type);
            jSONObject.put("ua", aDLoadData.ua);
            orCreateBundle.edit().putString(downloadDataKey, jSONObject.toString()).commit();
        }
    }

    public static Object ADHandlerMethod(String str, Object... objArr) {
        try {
            Class[] clsArr = new Class[0];
            if (objArr != null && objArr.length > 0) {
                clsArr = new Class[objArr.length];
                for (int i = 0; i < objArr.length; i++) {
                    clsArr[i] = objArr[i].getClass();
                }
            }
            return PlatformUtil.invokeMethod("io.dcloud.feature.gg.dcloud.ADHandler", str, (Object) null, clsArr, objArr);
        } catch (Exception unused) {
            return null;
        }
    }
}
