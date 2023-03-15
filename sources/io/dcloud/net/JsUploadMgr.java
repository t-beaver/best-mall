package io.dcloud.net;

import android.text.TextUtils;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.net.UploadMgr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsUploadMgr {
    public HashMap<String, ArrayList<JsUpload>> mAppsUploadTasks;
    private UploadMgr mUploadMgr;

    JsUploadMgr() {
        this.mAppsUploadTasks = null;
        this.mAppsUploadTasks = new HashMap<>();
        this.mUploadMgr = UploadMgr.getUploadMgr();
    }

    private JsUpload createUploadTask(IWebview iWebview, JSONObject jSONObject) {
        return new JsUpload(iWebview, jSONObject);
    }

    private JSONArray enumerate(String str, ArrayList<JsUpload> arrayList) {
        JSONArray jSONArray = new JSONArray();
        if (arrayList != null && !arrayList.isEmpty()) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                try {
                    jSONArray.put(new JSONObject(arrayList.get(i).toJsonUpload()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return jSONArray;
    }

    private JsUpload findUploadTask(String str, String str2) {
        ArrayList arrayList = this.mAppsUploadTasks.get(str);
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                JsUpload jsUpload = (JsUpload) arrayList.get(i);
                if (str2.equals(jsUpload.mUUID)) {
                    return jsUpload;
                }
            }
        }
        return null;
    }

    private void pushUploadTask(String str, JsUpload jsUpload) {
        ArrayList arrayList = this.mAppsUploadTasks.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.mAppsUploadTasks.put(str, arrayList);
        }
        arrayList.add(jsUpload);
    }

    public String execute(IWebview iWebview, String str, String[] strArr) {
        String obtainAppId = iWebview.obtainFrameView().obtainApp().obtainAppId();
        if ("start".equals(str) || AbsoluteConst.EVENTS_RESUME.equals(str)) {
            JsUpload findUploadTask = findUploadTask(obtainAppId, strArr[0]);
            if (findUploadTask != null && !findUploadTask.isStart) {
                this.mUploadMgr.start(findUploadTask.mUploadNetWork);
                findUploadTask.isStart = true;
            }
            String str2 = strArr[1];
            if (TextUtils.isEmpty(str2)) {
                return null;
            }
            try {
                JSONObject jSONObject = new JSONObject(str2);
                Iterator<String> keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String next = keys.next();
                    findUploadTask.setRequestHeader(next, jSONObject.getString(next));
                }
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else if ("pause".equals(str)) {
            JsUpload findUploadTask2 = findUploadTask(obtainAppId, strArr[0]);
            if (findUploadTask2 == null || !findUploadTask2.isStart) {
                return null;
            }
            this.mUploadMgr.abort(findUploadTask2.mUploadNetWork);
            findUploadTask2.isStart = false;
            return null;
        } else if ("abort".equals(str)) {
            JsUpload findUploadTask3 = findUploadTask(obtainAppId, strArr[0]);
            if (findUploadTask3 == null) {
                return null;
            }
            this.mUploadMgr.abort(findUploadTask3.mUploadNetWork);
            this.mAppsUploadTasks.get(obtainAppId).remove(findUploadTask3);
            return null;
        } else if ("createUpload".equals(str)) {
            try {
                pushUploadTask(obtainAppId, createUploadTask(iWebview, new JSONObject(strArr[0])));
                return null;
            } catch (JSONException e2) {
                e2.printStackTrace();
                return null;
            }
        } else if ("enumerate".equals(str)) {
            JSUtil.execCallback(iWebview, strArr[0], enumerate(strArr[0], this.mAppsUploadTasks.get(obtainAppId)), JSUtil.OK, false);
            return null;
        } else if ("clear".equals(str)) {
            ArrayList arrayList = this.mAppsUploadTasks.get(obtainAppId);
            int parseInt = Integer.parseInt(strArr[0]);
            if (arrayList == null) {
                return null;
            }
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                JsUpload jsUpload = (JsUpload) arrayList.get(size);
                if (jsUpload != null && parseInt == jsUpload.mState) {
                    UploadMgr.getUploadMgr().abort(jsUpload.mUploadNetWork);
                    arrayList.remove(size);
                }
            }
            return null;
        } else if ("startAll".equals(str)) {
            ArrayList arrayList2 = this.mAppsUploadTasks.get(obtainAppId);
            if (arrayList2 == null) {
                return null;
            }
            for (int i = 0; i < arrayList2.size(); i++) {
                this.mUploadMgr.start(((JsUpload) arrayList2.get(i)).mUploadNetWork);
            }
            return null;
        } else if ("addFile".equals(str)) {
            try {
                findUploadTask(obtainAppId, strArr[0]).addFile(iWebview, iWebview.obtainFrameView().obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), strArr[1]), new JSONObject(strArr[2]));
                return null;
            } catch (JSONException e3) {
                e3.printStackTrace();
                return null;
            }
        } else if (!"addData".equals(str)) {
            return null;
        } else {
            findUploadTask(obtainAppId, strArr[0]).addData(strArr[1], strArr[2]);
            return null;
        }
    }
}
