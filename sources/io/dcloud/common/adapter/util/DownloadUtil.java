package io.dcloud.common.adapter.util;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import io.dcloud.common.DHInterface.ILoadCallBack;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.util.LoadAppUtils;
import io.dcloud.common.util.PdrUtil;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class DownloadUtil {
    private static DownloadUtil mInstance;
    BroadcastReceiver download_receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.v("intent", "" + intent.getLongExtra("extra_download_id", 0));
            try {
                DownloadUtil.this.queryDownloadStatus(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    Context mContext;
    private DownloadManager mDownloader;
    HashMap<Long, MyRequest> rs = null;

    class MyRequest {
        ILoadCallBack callback = null;
        long id;
        DownloadManager.Request request = null;

        MyRequest(String str, String str2, ILoadCallBack iLoadCallBack) {
            DownloadManager.Request request2 = new DownloadManager.Request(Uri.parse(str));
            this.request = request2;
            request2.setMimeType(str2);
            this.callback = iLoadCallBack;
        }
    }

    private DownloadUtil(Context context) {
        this.mDownloader = (DownloadManager) context.getSystemService(AbsoluteConst.SPNAME_DOWNLOAD);
        this.rs = new HashMap<>(2);
        this.mContext = context;
        try {
            context.getApplicationContext().registerReceiver(this.download_receiver, new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"));
        } catch (Exception unused) {
        }
    }

    public static Intent getAPKInstallIntent(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String mimeType = PdrUtil.getMimeType(str);
        if (str.startsWith(DeviceInfo.FILE_PROTOCOL)) {
            str = str.substring(7);
        }
        if (str.startsWith("content://")) {
            str = PlatformUtil.getFilePathFromContentUri(Uri.parse(str), context.getContentResolver());
            mimeType = PdrUtil.getMimeType(str);
        }
        return LoadAppUtils.getDataAndTypeIntent(context, str, mimeType);
    }

    public static DownloadUtil getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DownloadUtil(context);
        }
        return mInstance;
    }

    /* access modifiers changed from: private */
    public void queryDownloadStatus(Context context) {
        Set<Long> keySet = this.rs.keySet();
        int size = keySet.size();
        Long[] lArr = new Long[size];
        keySet.toArray(lArr);
        for (int i = size - 1; i >= 0; i--) {
            long longValue = lArr[i].longValue();
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(new long[]{longValue});
            Cursor query2 = this.mDownloader.query(query);
            if (query2.moveToFirst()) {
                int i2 = query2.getInt(query2.getColumnIndex("status"));
                if (i2 != 1) {
                    if (i2 != 2) {
                        if (i2 == 4) {
                            Log.v("down", "STATUS_PAUSED");
                        } else if (i2 == 8) {
                            Log.e("down", "下载完成");
                            String str = null;
                            try {
                                Uri uriForDownloadedFile = this.mDownloader.getUriForDownloadedFile(longValue);
                                if (uriForDownloadedFile.toString().startsWith("content://")) {
                                    str = uriForDownloadedFile.toString();
                                } else {
                                    str = uriForDownloadedFile.getPath();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ILoadCallBack iLoadCallBack = this.rs.remove(Long.valueOf(longValue)).callback;
                            if (iLoadCallBack != null) {
                                iLoadCallBack.onCallBack(0, context, str);
                            }
                        } else if (i2 == 16) {
                            Log.v("down", "STATUS_FAILED");
                            this.mDownloader.remove(new long[]{longValue});
                            this.rs.remove(Long.valueOf(longValue));
                        }
                    }
                    Log.v("down", "STATUS_RUNNING");
                }
                Log.v("down", "STATUS_PENDING");
                Log.v("down", "STATUS_RUNNING");
            }
        }
    }

    private void runCallBack(Activity activity, ILoadCallBack iLoadCallBack, int i, Object obj) {
        final ILoadCallBack iLoadCallBack2 = iLoadCallBack;
        final int i2 = i;
        final Activity activity2 = activity;
        final Object obj2 = obj;
        activity.runOnUiThread(new Runnable() {
            public void run() {
                iLoadCallBack2.onCallBack(i2, activity2, obj2);
            }
        });
    }

    public void addDownlaodCallBack(long j, String str, String str2, ILoadCallBack iLoadCallBack) {
        if (this.rs.size() <= 0 || !this.rs.containsKey(Long.valueOf(j))) {
            this.rs.put(Long.valueOf(j), new MyRequest(str, str2, iLoadCallBack));
        }
    }

    public void checkDownloadStatus(Activity activity, long j, ILoadCallBack iLoadCallBack) {
        String path;
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(new long[]{j});
        Cursor query2 = this.mDownloader.query(query);
        if (query2.moveToFirst()) {
            int i = query2.getInt(query2.getColumnIndex("status"));
            String str = null;
            if (!(i == 1 || i == 2)) {
                if (i == 4) {
                    this.mDownloader.remove(new long[]{j});
                } else if (i == 8) {
                    try {
                        Uri uriForDownloadedFile = this.mDownloader.getUriForDownloadedFile(j);
                        if (uriForDownloadedFile.toString().startsWith("content://")) {
                            path = uriForDownloadedFile.toString();
                        } else {
                            path = uriForDownloadedFile.getPath();
                        }
                        str = path;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    runCallBack(activity, iLoadCallBack, i, getAPKInstallIntent(activity, str));
                    return;
                } else if (i == 16) {
                    this.mDownloader.remove(new long[]{j});
                }
            }
            if (iLoadCallBack != null) {
                runCallBack(activity, iLoadCallBack, i, (Object) null);
            }
        }
    }

    public void onReceive(Context context, Intent intent) {
    }

    public long startRequest(Context context, String str, String str2, String str3, String str4, ILoadCallBack iLoadCallBack) {
        try {
            MyRequest myRequest = new MyRequest(str, str2, iLoadCallBack);
            try {
                if (!DHFile.isExist(str3)) {
                    new File(str3).mkdirs();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (str3.endsWith("/")) {
                str3 = str3.substring(0, str3.length() - 1);
            }
            if (str3.startsWith(DeviceInfo.sDeviceRootDir)) {
                str3 = str3.substring(DeviceInfo.sDeviceRootDir.length() + 1);
            }
            myRequest.request.setTitle(str4);
            myRequest.request.setDestinationInExternalPublicDir(str3, str4);
            long enqueue = this.mDownloader.enqueue(myRequest.request);
            myRequest.id = enqueue;
            this.rs.put(Long.valueOf(enqueue), myRequest);
            return enqueue;
        } catch (Exception e2) {
            e2.printStackTrace();
            iLoadCallBack.onCallBack(-1, (Context) null, (Object) null);
            return -1;
        }
    }

    public void stop() {
        Context context = this.mContext;
        if (context != null) {
            try {
                context.getApplicationContext().unregisterReceiver(this.download_receiver);
                mInstance = null;
                this.mContext = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
