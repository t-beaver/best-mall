package io.dcloud.feature.gg.dcloud;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.dcloud.android.downloader.DownloadService;
import com.dcloud.android.downloader.callback.DCDownloadManager;
import com.dcloud.android.downloader.callback.DownloadListener;
import com.dcloud.android.downloader.domain.DownloadInfo;
import com.dcloud.android.downloader.exception.DownloadException;
import io.dcloud.common.DHInterface.ILoadCallBack;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.util.ADUtils;
import io.dcloud.common.util.NetTool;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.ThreadPool;
import io.dcloud.feature.gg.dcloud.ADHandler;
import java.io.File;
import java.util.HashMap;
import org.json.JSONObject;

public class ADSim {
    public static final int INTISPLSH = 10000;
    ADHandler.AdData mAdData = null;
    private Context mContext;
    JSONObject mData;
    Handler mHandler = null;

    public ADSim(Context context, JSONObject jSONObject) {
        this.mData = jSONObject;
        this.mContext = context;
        this.mHandler = new Handler(context.getMainLooper()) {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (message.what == 10000) {
                    ADSim.this.initSimSplsh();
                }
            }
        };
    }

    /* access modifiers changed from: private */
    public void click() {
        ADHandler.log("shutao", "ADSim---click");
        Context context = this.mContext;
        ADHandler.click(context, this.mAdData, ADHandler.get(context, "adid"));
    }

    /* access modifiers changed from: private */
    public ADHandler.AdData crateAdData(JSONObject jSONObject) {
        ADHandler.AdData adData = new ADHandler.AdData();
        JSONObject optJSONObject = jSONObject.optJSONObject("data");
        if (optJSONObject != null) {
            adData.mProvider = jSONObject.optString("provider");
            adData.mJsonData = jSONObject;
            adData.mEShow = jSONObject.optInt("es", 0);
            adData.mEClick = jSONObject.optInt("ec", 0);
            adData.mImgSrc = optJSONObject.optString("src");
            adData.mImgData = "000";
            adData.mOriginalAppid = ADHandler.get("appid");
        }
        return adData;
    }

    public static void dwApp(Context context, String str, String str2, String str3, String str4, String str5, ILoadCallBack iLoadCallBack, String str6) {
        ADUtils.downloadCommit(context, str, str2, str3, 29, (String) null, (String) null, str6);
        DCDownloadManager downloadManager = DownloadService.getDownloadManager(context.getApplicationContext());
        String str7 = DeviceInfo.sDeviceRootDir + "/Download/" + "ADSIM-INFO.io";
        File file = new File(str7);
        if (file.exists()) {
            file.delete();
        }
        for (DownloadInfo next : downloadManager.findAllDownloading()) {
            if (next.getUri().equals(str4)) {
                downloadManager.remove(next);
            }
        }
        DownloadInfo build = new DownloadInfo.Builder().setUrl(str4).setPath(str7).build(context);
        if (iLoadCallBack != null) {
            build.setTag(iLoadCallBack);
        }
        final Context context2 = context;
        final String str8 = str;
        final String str9 = str2;
        final String str10 = str3;
        final String str11 = str6;
        final DCDownloadManager dCDownloadManager = downloadManager;
        build.setDownloadListener(new DownloadListener() {
            public void onDownloadFailed(DownloadInfo downloadInfo, DownloadException downloadException) {
                ADUtils.downloadCommit(context2, str8, str9, str10, 32, (String) null, (String) null, str11);
                File file = new File(downloadInfo.getPath());
                if (file.exists()) {
                    file.delete();
                }
                dCDownloadManager.remove(downloadInfo);
            }

            public void onDownloadSuccess(DownloadInfo downloadInfo) {
                if (downloadInfo.getTag() != null && (downloadInfo.getTag() instanceof ILoadCallBack)) {
                    ((ILoadCallBack) downloadInfo.getTag()).onCallBack(0, downloadInfo.getContext(), (Object) null);
                }
                ADUtils.downloadCommit(context2, str8, str9, str10, 30, (String) null, (String) null, str11);
                File file = new File(downloadInfo.getPath());
                if (file.exists()) {
                    file.delete();
                }
                dCDownloadManager.remove(downloadInfo);
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
        });
        downloadManager.download(build);
    }

    public static int getRandomInt(int i, int i2) {
        double d = (double) i;
        double random = Math.random();
        double d2 = (double) ((i2 - i) + 1);
        Double.isNaN(d2);
        Double.isNaN(d);
        return (int) (d + (random * d2));
    }

    /* access modifiers changed from: private */
    public void initAdImg(final String str, final String str2) {
        ThreadPool.self().addThreadTask(new Runnable() {
            public void run() {
                HashMap hashMap = new HashMap();
                if (!PdrUtil.isEmpty(str2) && str2.equalsIgnoreCase("webview")) {
                    hashMap.put(IWebview.USER_AGENT, ADHandler.get("ua-webview"));
                }
                try {
                    if (NetTool.httpGet(str, (HashMap<String, String>) hashMap, true) != null) {
                        ADSim.this.mHandler.sendEmptyMessage(ADSim.INTISPLSH);
                    }
                } catch (Exception unused) {
                }
            }
        });
    }

    private void initClick() {
        if (this.mAdData.isEClick()) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    ADSim.this.click();
                }
            }, (long) getRandomInt(800, 2000));
        }
    }

    /* access modifiers changed from: private */
    public void initSimSplsh() {
        ADHandler.log("shutao", "ADSim---view");
        Context context = this.mContext;
        ADHandler.view(context, this.mAdData, ADHandler.get(context, "adid"));
        initClick();
    }

    public static void openUrl(Context context, String str) {
        ADHandler.log("shutao", "ADSim---openUrl");
        new ADWebView(context).loadUrl(str);
    }

    public void start() {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                ADSim aDSim = ADSim.this;
                aDSim.mAdData = aDSim.crateAdData(aDSim.mData);
                ADHandler.AdData adData = ADSim.this.mAdData;
                if (adData != null) {
                    String optString = adData.full() != null ? ADSim.this.mAdData.full().optString("ua") : "";
                    ADSim aDSim2 = ADSim.this;
                    aDSim2.initAdImg(aDSim2.mAdData.mImgSrc, optString);
                }
            }
        }, (long) getRandomInt(250, 350));
    }
}
