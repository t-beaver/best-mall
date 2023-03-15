package io.dcloud.net;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import com.facebook.common.statfs.StatFsHelper;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IReqListener;
import io.dcloud.common.DHInterface.IResponseListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.IOUtil;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import io.dcloud.common.util.ThreadPool;
import io.dcloud.common.util.net.DownloadMgr;
import io.dcloud.common.util.net.NetWork;
import io.dcloud.common.util.net.RequestData;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

public class JsDownload implements IReqListener, IResponseListener {
    public static final String DOWNLOAD_NAME = "_download_dcloud";
    static final int STATE_COMPLETED = 4;
    static final int STATE_CONNECTED = 2;
    static final int STATE_CONNECTING = 1;
    static final int STATE_INIT = 0;
    static final int STATE_PAUSE = 5;
    static final int STATE_RECEIVING = 3;
    static final int STATE_UNDEFINED = -1000;
    static final int STATE_UNKOWN = -1;
    boolean append = false;
    public boolean mAbort;
    String mConfigFilePath = null;
    String mData;
    private boolean mDownloadComplete;
    private DownloadJSMgr mDownloadMgr = null;
    DownloadNetWork mDownloadNetWork = null;
    /* access modifiers changed from: private */
    public File mFile = null;
    String mFileName = "";
    RandomAccessFile mFileOs = null;
    long mFileSize = 0;
    String mMethod;
    JSONObject mOptions = null;
    private String mParentPath = null;
    public boolean mPause;
    int mPriority;
    String mRealURL = null;
    /* access modifiers changed from: private */
    public ArrayList<IWebview> mRelWebviews = null;
    RequestData mRequestData = null;
    int mRetry = 3;
    private long mRetryIntervalTime;
    int mState = -1000;
    long mTotalSize = 0;
    String mUUID = null;
    String mUrl = null;
    public IWebview mWebview = null;
    private long preTime = 0;
    private long responseOffset = 0;
    private String sAppid;
    private String sharedPreferenceName;

    JsDownload(DownloadJSMgr downloadJSMgr, IWebview iWebview, JSONObject jSONObject) {
        this.mDownloadMgr = downloadJSMgr;
        this.mWebview = iWebview;
        ArrayList<IWebview> arrayList = new ArrayList<>();
        this.mRelWebviews = arrayList;
        arrayList.add(iWebview);
        parseJson(jSONObject);
        RequestData requestData = new RequestData(this.mUrl, this.mMethod);
        this.mRequestData = requestData;
        requestData.unTrustedCAType = iWebview.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_UNTRUSTEDCA);
        if (!jSONObject.isNull("timeout")) {
            this.mRequestData.mTimeout = jSONObject.optInt("timeout") * 1000;
        }
        this.mRequestData.addHeader(IWebview.USER_AGENT, iWebview.getWebviewProperty(IWebview.USER_AGENT));
        DownloadNetWork downloadNetWork = new DownloadNetWork(2, this.mRequestData, this, this);
        this.mDownloadNetWork = downloadNetWork;
        downloadNetWork.MAX_TIMES = this.mRetry;
        downloadNetWork.mPriority = this.mPriority;
        downloadNetWork.setRetryIntervalTime(this.mRetryIntervalTime);
        this.sAppid = this.mWebview.obtainFrameView().obtainApp().obtainAppId();
        this.sharedPreferenceName = this.sAppid + DOWNLOAD_NAME;
    }

    private void checkSpecialFile(String str) {
        if (!TextUtils.isEmpty(str) && BaseInfo.ISAMU) {
            int length = str.length();
            if (((length - str.indexOf(".apk")) - 4 == 0 || (length - str.indexOf(".wgt")) - 4 == 0 || (length - str.indexOf(".wgtu")) - 5 == 0) && this.mWebview != null) {
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("type", AbsoluteConst.SPNAME_DOWNLOAD);
                    jSONObject.put("file", this.mParentPath + this.mFileName);
                    jSONObject.put("url", this.mUrl);
                    jSONObject.put("appid", this.mWebview.obtainApp().obtainOriginalAppId());
                    jSONObject.put("version", this.mWebview.obtainApp().obtainAppVersionName());
                    Log.i(AbsoluteConst.HBUILDER_TAG, jSONObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private File createDownloadFile(boolean z) {
        String str;
        String str2;
        try {
            String realPath = getRealPath(this.mFileName);
            if (realPath == null) {
                return null;
            }
            File file = new File(realPath);
            if (z && file.exists()) {
                return file;
            }
            String str3 = this.mFileName;
            int lastIndexOf = str3.lastIndexOf(Operators.DOT_STR);
            if (lastIndexOf < 0) {
                str = str3;
            } else {
                str = str3.substring(0, lastIndexOf);
            }
            if (lastIndexOf < 0) {
                str2 = "";
            } else {
                str2 = str3.substring(lastIndexOf);
            }
            int i = 1;
            while (file.exists()) {
                str3 = str + Operators.BRACKET_START_STR + i + Operators.BRACKET_END_STR + str2;
                i++;
                file = new File(getRealPath(str3));
            }
            this.mFileName = str3;
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            return file;
        } catch (Exception e) {
            String str4 = this.mFileName;
            if (str4 == null || !str4.toLowerCase(Locale.ENGLISH).startsWith(DeviceInfo.FILE_PROTOCOL)) {
                e.printStackTrace();
                return null;
            }
            this.mFileName = this.mFileName.substring(7);
            return createDownloadFile(this.append);
        }
    }

    private void initPath(String str) {
        IApp obtainApp = this.mWebview.obtainFrameView().obtainApp();
        if (PdrUtil.isEmpty(str)) {
            this.mParentPath = new File(BaseInfo.sDownloadFullPath).getParent() + "/";
            this.mFileName = "_downloads/";
            return;
        }
        this.mFileName = str;
        if (startsWith(str, BaseInfo.REL_PRIVATE_DOC_DIR, true)) {
            this.mParentPath = new File(obtainApp.obtainAppDocPath()).getParent() + "/";
        } else if (startsWith(str, BaseInfo.REL_PUBLIC_DOCUMENTS_DIR, true)) {
            this.mParentPath = new File(BaseInfo.sDocumentFullPath).getParent() + "/";
        } else if (startsWith(str, BaseInfo.REL_PUBLIC_DOWNLOADS_DIR, true)) {
            this.mParentPath = new File(BaseInfo.sDownloadFullPath).getParent() + "/";
        } else {
            this.mParentPath = new File(BaseInfo.sDownloadFullPath).getParent() + "/";
        }
    }

    private boolean justDirPath() {
        return TextUtils.isEmpty(this.mFileName) || this.mFileName.endsWith("/");
    }

    private void onStateChanged(int i) {
        if (i == 3 || i == 5) {
            this.mState = i;
        }
        boolean z = i == 3 && SystemClock.elapsedRealtime() - this.preTime >= 10;
        if (z || i != 3 || this.mTotalSize - this.mFileSize <= 5120 || this.mAbort) {
            if (z) {
                this.preTime = SystemClock.elapsedRealtime();
            }
            String json = toJSON();
            int size = this.mRelWebviews.size();
            for (int i2 = 0; i2 < size; i2++) {
                Deprecated_JSUtil.excDownloadCallBack(this.mRelWebviews.get(i2), json, this.mUUID);
            }
        }
        if (!this.mAbort) {
            saveDownloadState();
        }
    }

    private void parseJson(JSONObject jSONObject) {
        this.mOptions = jSONObject.optJSONObject("options");
        this.mUrl = JSONUtil.getString(jSONObject, "url");
        String string = JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_REALURL);
        if (string != null && !string.equalsIgnoreCase("null") && !string.equalsIgnoreCase(this.mUrl)) {
            this.mUrl = string;
        }
        String string2 = JSONUtil.getString(jSONObject, "id");
        this.mUUID = string2;
        if (TextUtils.isEmpty(string2)) {
            this.mUUID = JSONUtil.getString(jSONObject, "uuid");
        }
        this.mMethod = JSONUtil.getString(jSONObject, "method");
        this.mPriority = JSONUtil.getInt(jSONObject, "priority");
        this.mRetry = JSONUtil.getInt(jSONObject, AbsoluteConst.JSON_KEY_RETRY);
        this.mFileSize = (long) JSONUtil.getInt(jSONObject, AbsoluteConst.JSON_KEY_DOWNLOADEDSIZE);
        this.mTotalSize = (long) JSONUtil.getInt(jSONObject, AbsoluteConst.JSON_KEY_TOTALSIZE);
        String string3 = JSONUtil.getString(jSONObject, "state");
        if (!PdrUtil.isEmpty(string3)) {
            try {
                this.mState = Integer.parseInt(string3);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        this.mConfigFilePath = BaseInfo.sDownloadFullPath + this.mUUID + ".download";
        File file = new File(this.mConfigFilePath);
        try {
            if (file.exists()) {
                String iOUtil = IOUtil.toString(new FileInputStream(file));
                iOUtil.replace("\n", "");
                String[] split = iOUtil.split(Operators.SUB);
                this.mTotalSize = Long.parseLong(split[0]);
                this.mState = Integer.parseInt(split[2]);
                File file2 = new File(split[3].replace("\n", ""));
                this.mFile = file2;
                if (file2.exists()) {
                    this.mFileSize = this.mFile.length();
                } else {
                    this.mFileSize = 0;
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        String string4 = JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_FILENAME);
        if (TextUtils.isEmpty(string4)) {
            string4 = JSONUtil.getString(this.mOptions, AbsoluteConst.JSON_KEY_FILENAME);
        }
        initPath(string4);
        this.mData = JSONUtil.getString(jSONObject, "data");
        this.mRetryIntervalTime = JSONUtil.getLong(jSONObject, AbsoluteConst.JSON_KEY_RETRY_INTERVAL_TIME) * 1000;
    }

    private void saveDownloadState() {
        if (this.mFileSize < this.mTotalSize) {
            try {
                File file = new File(this.mConfigFilePath);
                if (file.exists()) {
                    FileOutputStream fileOutputStream = new FileOutputStream(file, false);
                    fileOutputStream.write((this.mTotalSize + Operators.SUB + this.mFileSize + Operators.SUB + this.mState + Operators.SUB + this.mFile.getAbsolutePath()).getBytes());
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean startsWith(String str, String str2, boolean z) {
        if (str == null || !str.startsWith(str2)) {
            return false;
        }
        if (!z) {
            return true;
        }
        String substring = str.substring(str2.length());
        if (substring.length() == 0 || (substring.length() > 1 && substring.charAt(0) == '/')) {
            return true;
        }
        return false;
    }

    public void abort() {
        try {
            this.mAbort = true;
            ThreadPool.self().addThreadTask(new Runnable() {
                public void run() {
                    JsDownload.this.mDownloadNetWork.cancelWork();
                    DownloadMgr.getDownloadMgr().removeTask(JsDownload.this.mDownloadNetWork);
                    JsDownload.this.deleteDownloadData();
                    if (!PdrUtil.isEmpty(JsDownload.this.mFileOs)) {
                        try {
                            JsDownload.this.mFileOs.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (JsDownload.this.mFile != null) {
                        JsDownload.this.mFile.delete();
                    }
                    JsDownload jsDownload = JsDownload.this;
                    jsDownload.mWebview = null;
                    jsDownload.mRelWebviews.clear();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRelWebview(IWebview iWebview) {
        if (!this.mRelWebviews.contains(iWebview)) {
            this.mRelWebviews.add(iWebview);
        }
    }

    public void deleteDownloadData() {
        this.mDownloadMgr.deleteDownloadTaskInfo(this.sAppid, this.mUUID);
        new File(this.mConfigFilePath).delete();
        saveInDatabase();
    }

    public String getRealPath(String str) {
        if (str != null && str.startsWith("/") && !str.toLowerCase(Locale.ENGLISH).startsWith("/sdcard")) {
            String convert2AbsFullPath = this.mWebview.obtainApp().convert2AbsFullPath(str.substring(1));
            if (convert2AbsFullPath == null || !convert2AbsFullPath.contains("/www/")) {
                return convert2AbsFullPath;
            }
            return convert2AbsFullPath.replace("/www/", "/" + BaseInfo.REAL_PUBLIC_DOWNLOADS_DIR);
        } else if (str != null && str.toLowerCase(Locale.ENGLISH).startsWith("/sdcard")) {
            return str;
        } else {
            String convert2AbsFullPath2 = this.mWebview.obtainApp().convert2AbsFullPath(str);
            if (convert2AbsFullPath2 == null || !convert2AbsFullPath2.contains("/www/")) {
                return convert2AbsFullPath2;
            }
            return convert2AbsFullPath2.replace("/www/", "/" + BaseInfo.REAL_PUBLIC_DOWNLOADS_DIR);
        }
    }

    public void onNetStateChanged(IReqListener.NetState netState, boolean z) {
        String str;
        int lastIndexOf;
        if (!this.mPause) {
            if (netState == IReqListener.NetState.NET_INIT) {
                this.mState = 0;
                this.mDownloadComplete = false;
            } else if (netState == IReqListener.NetState.NET_CONNECTED) {
                this.mState = 2;
            } else if (netState == IReqListener.NetState.NET_HANDLE_END) {
                this.mState = 4;
                Logger.d("----NetState.NET_HANDLE_END-----");
                DownloadMgr.getDownloadMgr().removeTask(this.mDownloadNetWork);
                deleteDownloadData();
                this.mDownloadComplete = true;
                checkSpecialFile(this.mFileName);
            } else if (netState == IReqListener.NetState.NET_ERROR) {
                this.mState = 4;
                this.mDownloadNetWork.mStatus = StatFsHelper.DEFAULT_DISK_YELLOW_LEVEL_IN_MB;
                Logger.d("----NetState.NET_ERROR-----");
                DownloadMgr.getDownloadMgr().removeTask(this.mDownloadNetWork);
                if (this.mDownloadComplete) {
                    return;
                }
            } else if (netState == IReqListener.NetState.NET_TIMEOUT) {
                this.mState = 4;
                this.mDownloadNetWork.mStatus = 0;
                Logger.d("----NetState.NET_TIMEOUT-----");
                DownloadMgr.getDownloadMgr().removeTask(this.mDownloadNetWork);
                if (this.mDownloadComplete) {
                    return;
                }
            } else if (netState == IReqListener.NetState.NET_REQUEST_BEGIN) {
                try {
                    if (this.mFileSize > 0) {
                        this.mDownloadNetWork.mUrlConn.setRequestProperty("Range", "bytes=" + String.valueOf(this.mFileSize) + Operators.SUB);
                    }
                    this.mDownloadNetWork.mUrlConn.setRequestMethod(this.mMethod);
                    if (this.mMethod.equals("POST")) {
                        this.mDownloadNetWork.mUrlConn.setDoOutput(true);
                        this.mDownloadNetWork.mUrlConn.getOutputStream().write(this.mData.getBytes("utf8"));
                        this.mDownloadNetWork.mUrlConn.getOutputStream().flush();
                        this.mDownloadNetWork.mUrlConn.getOutputStream().close();
                        this.mDownloadNetWork.mUrlConn.setChunkedStreamingMode(0);
                    }
                    this.mDownloadNetWork.mUrlConn.setConnectTimeout(this.mRequestData.mTimeout);
                    this.mDownloadNetWork.mUrlConn.setReadTimeout(this.mRequestData.mTimeout);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (netState == IReqListener.NetState.NET_HANDLE_BEGIN) {
                String headerField = this.mDownloadNetWork.mUrlConn.getHeaderField("Content-Length");
                String headerField2 = this.mDownloadNetWork.mUrlConn.getHeaderField("Content-Range");
                String headerField3 = this.mDownloadNetWork.mUrlConn.getHeaderField(NetWork.CONTENT_TYPE);
                String headerField4 = this.mDownloadNetWork.mUrlConn.getHeaderField(IWebview.SET_COOKIE);
                if (!PdrUtil.isEmpty(headerField4)) {
                    CookieManager.getInstance().setCookie(this.mRequestData.getUrl(), headerField4);
                }
                if (headerField2 == null) {
                    this.mTotalSize = PdrUtil.parseLong(headerField, 0);
                    this.mFileSize = 0;
                    this.responseOffset = 0;
                    File file = this.mFile;
                    if (file != null && file.exists()) {
                        this.mFile.delete();
                        new File(this.mConfigFilePath).delete();
                    }
                } else {
                    this.append = true;
                    try {
                        MessageFormat messageFormat = new MessageFormat("bytes {0,number}-{1,number}");
                        messageFormat.setLocale(Locale.US);
                        long longValue = ((Number) messageFormat.parse(headerField2)[0]).longValue();
                        this.responseOffset = longValue;
                        if (longValue < 0) {
                            this.responseOffset = 0;
                        }
                    } catch (Exception unused) {
                        this.responseOffset = 0;
                    }
                }
                if (justDirPath()) {
                    String headerField5 = this.mDownloadNetWork.mUrlConn.getHeaderField("content-disposition");
                    try {
                        if (PdrUtil.isEmpty(headerField5) && (lastIndexOf = str.lastIndexOf(47)) >= 0) {
                            String substring = (str = this.mDownloadNetWork.mUrlConn.getURL().getFile().toString()).substring(lastIndexOf + 1);
                            if (substring.indexOf(46) >= 0) {
                                if (substring.contains(Operators.CONDITION_IF_STRING)) {
                                    substring = substring.substring(0, substring.indexOf(Operators.CONDITION_IF_STRING));
                                }
                                this.mFileName += substring;
                            }
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    if (justDirPath()) {
                        this.mFileName += PdrUtil.getDownloadFilename(headerField5, headerField3, this.mUrl);
                    }
                }
                if (!this.mDownloadNetWork.isStop) {
                    try {
                        this.mFile = createDownloadFile(this.append);
                        RandomAccessFile randomAccessFile = new RandomAccessFile(this.mFile, "rw");
                        this.mFileOs = randomAccessFile;
                        randomAccessFile.seek(this.responseOffset);
                        return;
                    } catch (IOException e3) {
                        e3.printStackTrace();
                        saveInDatabase();
                        return;
                    }
                } else {
                    return;
                }
            }
            onStateChanged(this.mState);
        }
    }

    public void onResponseState(int i, String str) {
    }

    public void onResponsing(InputStream inputStream) {
        String json = toJSON();
        this.mState = 1;
        int size = this.mRelWebviews.size();
        for (int i = 0; i < size; i++) {
            Deprecated_JSUtil.excDownloadCallBack(this.mRelWebviews.get(i), json, this.mUUID);
        }
    }

    public void saveInDatabase() {
        this.mDownloadMgr.saveDownloadTaskInfo(this.sAppid, this.mUUID, toSaveJSON());
    }

    public void setRequestHeader(String str, String str2) {
        this.mRequestData.addHeader(str, str2);
    }

    public void start() {
        this.mDownloadNetWork.mTimes = 1;
        this.mPause = false;
        DownloadMgr.getDownloadMgr().addQuestTask(this.mDownloadNetWork);
        saveInDatabase();
    }

    public String toJSON() {
        if (this.mState == -1000) {
            return StringUtil.format("{status: %d,filename: '%s',uuid: '%s',downloadedSize:%d,totalSize:%d,headers:%s}", Integer.valueOf(this.mDownloadNetWork.mStatus), this.mFileName, this.mUUID, Long.valueOf(this.mFileSize), Long.valueOf(this.mTotalSize), this.mDownloadNetWork.getResponseHeaders());
        }
        return StringUtil.format("{status: %d,state: %d,filename: '%s',uuid: '%s',downloadedSize:%d,totalSize:%d,headers:%s}", Integer.valueOf(this.mDownloadNetWork.mStatus), Integer.valueOf(this.mState), this.mFileName, this.mUUID, Long.valueOf(this.mFileSize), Long.valueOf(this.mTotalSize), this.mDownloadNetWork.getResponseHeaders());
    }

    /* access modifiers changed from: package-private */
    public String toSaveJSON() {
        return StringUtil.format("{url: '%s',uuid: '%s',method: '%s',priority: %d,timeout:%d,retry:%d,filename:'%s',downloadedSize:%d,totalSize:%d,state: %d,options:%s,RealURL:'%s'}", this.mUrl, this.mUUID, this.mMethod, Integer.valueOf(this.mPriority), Integer.valueOf(this.mRequestData.mTimeout), Integer.valueOf(this.mRetry), this.mFileName, Long.valueOf(this.mFileSize), Long.valueOf(this.mTotalSize), Integer.valueOf(this.mState), this.mOptions.toString(), this.mRealURL);
    }

    public int onReceiving(InputStream inputStream) throws Exception {
        byte[] bArr = new byte[10240];
        if (inputStream != null) {
            this.mDownloadNetWork.mTimes = 1;
            boolean z = false;
            while (true) {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    this.mFileOs.close();
                    break;
                } else if (this.mPause) {
                    onStateChanged(5);
                    return -1;
                } else {
                    this.mFileOs.write(bArr, 0, read);
                    this.mFileSize += (long) read;
                    onStateChanged(3);
                    if (!z) {
                        this.mRealURL = this.mDownloadNetWork.mUrlConn.getURL().toString();
                        saveInDatabase();
                        z = true;
                    }
                }
            }
        }
        if (this.mFileSize >= this.mTotalSize) {
            new File(this.mConfigFilePath).delete();
            return -1;
        }
        throw new RuntimeException();
    }
}
