package com.dcloud.android.downloader;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.dcloud.android.downloader.callback.DCDownloadManager;
import com.dcloud.android.downloader.config.Config;
import java.util.List;

public class DownloadService extends Service {
    private static final String TAG = "DownloadService";
    public static DCDownloadManager downloadManager;

    public static DCDownloadManager getDownloadManager(Context context) {
        return getDownloadManager(context, (Config) null);
    }

    private static boolean isServiceRunning(Context context) {
        List<ActivityManager.RunningServiceInfo> runningServices = ((ActivityManager) context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE);
        if (runningServices == null || runningServices.size() == 0) {
            return false;
        }
        for (int i = 0; i < runningServices.size(); i++) {
            if (runningServices.get(i).service.getClassName().equals(DownloadService.class.getName())) {
                return true;
            }
        }
        return false;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        DCDownloadManager dCDownloadManager = downloadManager;
        if (dCDownloadManager != null) {
            dCDownloadManager.onDestroy();
            downloadManager = null;
        }
        super.onDestroy();
    }

    public static DCDownloadManager getDownloadManager(Context context, Config config) {
        if (!isServiceRunning(context)) {
            context.startService(new Intent(context, DownloadService.class));
        }
        if (downloadManager == null) {
            downloadManager = DownloadManagerImpl.getInstance(context, config);
        }
        return downloadManager;
    }
}
