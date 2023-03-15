package io.dcloud.net;

import android.database.Cursor;
import android.net.Uri;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IReqListener;
import io.dcloud.common.DHInterface.IResponseListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.util.FileUtil;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import io.dcloud.common.util.net.RequestData;
import io.dcloud.common.util.net.UploadMgr;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.json.JSONObject;

public class JsUpload implements IReqListener, IResponseListener {
    private static final int STATE_COMPLETED = 4;
    private static final int STATE_CONNECTED = 2;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_INIT = 0;
    private static final int STATE_PAUSE = 5;
    private static final int STATE_RECEIVING = 3;
    private static final int STATE_UNKOWN = -1;
    boolean isStart = false;
    RequestData mRequestData;
    int mState;
    public String mUUID;
    UploadNetWork mUploadNetWork;
    String mUrl;
    IWebview mWebview = null;

    class UploadFile implements UploadItem {
        InputStream mFileInputS;
        long mFileSize;
        String mFilename;
        String mMimetype;
        String mRange;

        UploadFile() {
        }
    }

    interface UploadItem {
    }

    public static class UploadString implements UploadItem {
        String mData;

        public UploadString(String str) {
            this.mData = str;
        }
    }

    public JsUpload(IWebview iWebview, JSONObject jSONObject) {
        this.mWebview = iWebview;
        this.mUrl = jSONObject.optString("url");
        this.mUUID = jSONObject.optString(AbsoluteConst.JSON_KEY_UUID);
        RequestData requestData = new RequestData(this.mUrl, jSONObject.optString("method", "POST"));
        this.mRequestData = requestData;
        requestData.mChunkSize = jSONObject.optInt("chunkSize", 0);
        this.mRequestData.unTrustedCAType = iWebview.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_UNTRUSTEDCA);
        this.mRequestData.addHeader(IWebview.USER_AGENT, iWebview.getWebviewProperty(IWebview.USER_AGENT));
        UploadNetWork uploadNetWork = new UploadNetWork(1, this.mRequestData, this, this);
        this.mUploadNetWork = uploadNetWork;
        uploadNetWork.mPriority = jSONObject.optInt("priority");
        if (jSONObject.has("timeout")) {
            this.mRequestData.mTimeout = jSONObject.optInt("timeout") * 1000;
        }
        this.mUploadNetWork.MAX_TIMES = jSONObject.optInt(AbsoluteConst.JSON_KEY_RETRY);
        this.mUploadNetWork.setRetryIntervalTime(jSONObject.optLong(AbsoluteConst.JSON_KEY_RETRY_INTERVAL_TIME) * 1000);
    }

    public boolean addData(String str, String str2) {
        return this.mUploadNetWork.addParemeter(str, str2);
    }

    public boolean addFile(IWebview iWebview, String str, JSONObject jSONObject) {
        UploadFile uploadFile = new UploadFile();
        try {
            if (str.startsWith("content://")) {
                Uri parse = Uri.parse(str);
                Cursor query = iWebview.getContext().getContentResolver().query(parse, (String[]) null, (String) null, (String[]) null, (String) null);
                if (query == null) {
                    return false;
                }
                InputStream openInputStream = iWebview.getContext().getContentResolver().openInputStream(parse);
                query.moveToFirst();
                int i = query.getInt(query.getColumnIndex("_size"));
                String string = query.getString(query.getColumnIndex("_display_name"));
                String string2 = query.getString(query.getColumnIndex("mime_type"));
                uploadFile.mFileInputS = openInputStream;
                uploadFile.mFileSize = (long) i;
                String optString = jSONObject.optString(IApp.ConfigProperty.CONFIG_KEY, string);
                uploadFile.mFilename = jSONObject.optString("name", string);
                uploadFile.mMimetype = jSONObject.optString("mime", string2);
                boolean addFile = this.mUploadNetWork.addFile(optString, uploadFile);
                query.close();
                return addFile;
            }
            File file = new File(str);
            if (FileUtil.needMediaStoreOpenFile(iWebview.getContext())) {
                uploadFile.mFileInputS = FileUtil.getFileInputStream(iWebview.getContext(), file);
            } else {
                uploadFile.mFileInputS = new FileInputStream(file);
            }
            if (uploadFile.mFileInputS == null) {
                return false;
            }
            uploadFile.mFileSize = file.length();
            String optString2 = jSONObject.optString(IApp.ConfigProperty.CONFIG_KEY, file.getName());
            uploadFile.mFilename = jSONObject.optString("name", file.getName());
            uploadFile.mMimetype = jSONObject.optString("mime", PdrUtil.getMimeType(str));
            return this.mUploadNetWork.addFile(optString2, uploadFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void onNetStateChanged(IReqListener.NetState netState, boolean z) {
        if (netState == IReqListener.NetState.NET_INIT) {
            this.mState = 0;
            Deprecated_JSUtil.excUploadCallBack(this.mWebview, toJsonUpload(), this.mUUID);
        } else if (netState == IReqListener.NetState.NET_REQUEST_BEGIN) {
            this.mState = 1;
            Deprecated_JSUtil.excUploadCallBack(this.mWebview, toJsonUpload(), this.mUUID);
        } else if (netState == IReqListener.NetState.NET_CONNECTED) {
            this.mState = 2;
            Deprecated_JSUtil.excUploadCallBack(this.mWebview, toJsonUpload(), this.mUUID);
        } else if (netState == IReqListener.NetState.NET_HANDLE_ING) {
            this.mState = 3;
            Deprecated_JSUtil.excUploadCallBack(this.mWebview, toJsonUpload(), this.mUUID);
        } else if (netState == IReqListener.NetState.NET_HANDLE_END || netState == IReqListener.NetState.NET_ERROR) {
            this.mState = 4;
            UploadMgr.getUploadMgr().removeNetWork(this.mUploadNetWork);
            String jSONableString = JSONUtil.toJSONableString(this.mUploadNetWork.getResponseText());
            Deprecated_JSUtil.excUploadCallBack(this.mWebview, StringUtil.format("{state:%d,status:%d,filename:'%s',responseText:%s,headers:%s}", Integer.valueOf(this.mState), Integer.valueOf(this.mUploadNetWork.mStatus), this.mUploadNetWork.mUploadingFile.toString(), jSONableString, this.mUploadNetWork.responseHeaders), this.mUUID);
        }
    }

    public int onReceiving(InputStream inputStream) {
        return 0;
    }

    public void onResponseState(int i, String str) {
    }

    public void onResponsing(InputStream inputStream) {
    }

    public void setRequestHeader(String str, String str2) {
        this.mRequestData.addHeader(str, str2);
    }

    public String toJsonUpload() {
        return StringUtil.format("{state:%d,status:%d,uploadedSize:%d,totalSize:%d,headers:%s}", Integer.valueOf(this.mState), Integer.valueOf(this.mUploadNetWork.mStatus), Long.valueOf(this.mUploadNetWork.mUploadedSize), Long.valueOf(this.mUploadNetWork.mTotalSize), this.mUploadNetWork.responseHeaders);
    }
}
