package io.dcloud.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.util.JSUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class DownloadJSMgr {
    static final int DELETE = 2;
    static final int SAVE = 1;
    private static DownloadJSMgr mDownloadJSMgr;
    AppDownloadInfo curAppSharePref = null;
    public HashMap<String, AppDownloadInfo> mAppsDownloadTasks = null;
    Handler mHander = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                String[] strArr = (String[]) message.obj;
                String str = strArr[0];
                String str2 = strArr[1];
                String str3 = strArr[2];
                SharedPreferences.Editor edit = DownloadJSMgr.this.getAppTaskList(str).sharePref.edit();
                edit.putString(str2, str3);
                edit.commit();
            } else if (i == 2) {
                String[] strArr2 = (String[]) message.obj;
                String str4 = strArr2[0];
                String str5 = strArr2[1];
                SharedPreferences sharedPreferences = DownloadJSMgr.this.getAppTaskList(str4).sharePref;
                if (!TextUtils.isEmpty(sharedPreferences.getString(str5, ""))) {
                    sharedPreferences.edit().remove(str5).commit();
                }
            }
        }
    };

    class AppDownloadInfo {
        String appid;
        ArrayList<JsDownload> mList = null;
        SharedPreferences sharePref;

        AppDownloadInfo(Context context, IWebview iWebview, String str) {
            this.appid = str;
            this.sharePref = context.getSharedPreferences(str + JsDownload.DOWNLOAD_NAME, 0);
            this.mList = new ArrayList<>();
            Map<String, ?> all = this.sharePref.getAll();
            if (all != null) {
                ArrayList<JsDownload> arrayList = this.mList;
                for (String str2 : all.keySet()) {
                    try {
                        arrayList.add(new JsDownload(DownloadJSMgr.this, iWebview, new JSONObject((String) all.get(str2))));
                    } catch (JSONException unused) {
                    }
                }
                this.mList = arrayList;
            }
        }
    }

    private DownloadJSMgr() {
        if (this.mAppsDownloadTasks == null) {
            this.mAppsDownloadTasks = new HashMap<>();
        }
    }

    private JsDownload createDownloadTask(IWebview iWebview, JSONObject jSONObject) {
        return new JsDownload(this, iWebview, jSONObject);
    }

    private void enumerate(IWebview iWebview, String str, String str2, String str3) {
        String str4;
        AppDownloadInfo appTaskList = getAppTaskList(str3);
        try {
            int parseInt = Integer.parseInt(str2);
            if (!(parseInt == 0 || parseInt == 1 || parseInt == 2 || parseInt == 3 || parseInt == 4)) {
                if (parseInt != 5) {
                    str4 = enumerateArr(iWebview, appTaskList.mList);
                    Deprecated_JSUtil.execCallback(iWebview, str, str4, JSUtil.OK, true, false);
                }
            }
            ArrayList arrayList = new ArrayList();
            if (appTaskList != null && !appTaskList.mList.isEmpty()) {
                int size = appTaskList.mList.size();
                for (int i = 0; i < size; i++) {
                    JsDownload jsDownload = appTaskList.mList.get(i);
                    if (jsDownload.mWebview.obtainApp() == null) {
                        jsDownload.mWebview = iWebview;
                    }
                    if (parseInt == jsDownload.mState) {
                        arrayList.add(jsDownload);
                    }
                }
            }
            str4 = enumerateArr(iWebview, arrayList);
        } catch (Exception unused) {
            ArrayList arrayList2 = new ArrayList();
            if (appTaskList != null && !appTaskList.mList.isEmpty()) {
                int size2 = appTaskList.mList.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    JsDownload jsDownload2 = appTaskList.mList.get(i2);
                    jsDownload2.mWebview = iWebview;
                    if (jsDownload2.mState != 4) {
                        arrayList2.add(jsDownload2);
                    }
                }
            }
            str4 = enumerateArr(iWebview, arrayList2);
        }
        Deprecated_JSUtil.execCallback(iWebview, str, str4, JSUtil.OK, true, false);
    }

    private String enumerateArr(IWebview iWebview, ArrayList<JsDownload> arrayList) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Operators.ARRAY_START_STR);
        if (arrayList != null && !arrayList.isEmpty()) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                JsDownload jsDownload = arrayList.get(i);
                stringBuffer.append(jsDownload.toSaveJSON());
                jsDownload.addRelWebview(iWebview);
                if (i != size - 1) {
                    stringBuffer.append(",");
                }
            }
        }
        stringBuffer.append(Operators.ARRAY_END_STR);
        return stringBuffer.toString();
    }

    private JsDownload findDownloadTask(IWebview iWebview, String str, String str2) {
        AppDownloadInfo appTaskList = getAppTaskList(str);
        if (appTaskList != null) {
            int size = appTaskList.mList.size();
            int i = 0;
            while (i < size) {
                JsDownload jsDownload = appTaskList.mList.get(i);
                if (!str2.equals(jsDownload.mUUID)) {
                    i++;
                } else if (jsDownload.mWebview.obtainApp() != null) {
                    return jsDownload;
                } else {
                    jsDownload.mWebview = iWebview;
                    return jsDownload;
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public AppDownloadInfo getAppTaskList(String str) {
        return this.mAppsDownloadTasks.get(str);
    }

    protected static DownloadJSMgr getInstance() {
        if (mDownloadJSMgr == null) {
            mDownloadJSMgr = new DownloadJSMgr();
        }
        return mDownloadJSMgr;
    }

    private void initAppDownloadList(IWebview iWebview, String str) {
        if (!this.mAppsDownloadTasks.containsKey(str)) {
            this.mAppsDownloadTasks.put(str, new AppDownloadInfo(iWebview.getContext(), iWebview, str));
        }
    }

    private void pushDownloadTask(IWebview iWebview, String str, JsDownload jsDownload) {
        AppDownloadInfo appDownloadInfo = this.mAppsDownloadTasks.get(str);
        if (appDownloadInfo == null) {
            appDownloadInfo = new AppDownloadInfo(iWebview.getContext(), iWebview, str);
            this.mAppsDownloadTasks.put(str, appDownloadInfo);
        }
        appDownloadInfo.mList.add(jsDownload);
    }

    /* access modifiers changed from: package-private */
    public void deleteDownloadTaskInfo(String str, String str2) {
        Message obtain = Message.obtain();
        obtain.what = 2;
        obtain.obj = new String[]{str, str2};
        this.mHander.sendMessage(obtain);
    }

    public void dispose() {
        for (String appTaskList : this.mAppsDownloadTasks.keySet()) {
            Iterator<JsDownload> it = getAppTaskList(appTaskList).mList.iterator();
            while (it.hasNext()) {
                it.next().saveInDatabase();
            }
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String execute(io.dcloud.common.DHInterface.IWebview r7, java.lang.String r8, java.lang.String[] r9) {
        /*
            r6 = this;
            io.dcloud.common.DHInterface.IFrameView r0 = r7.obtainFrameView()     // Catch:{ Exception -> 0x0146 }
            io.dcloud.common.DHInterface.IApp r0 = r0.obtainApp()     // Catch:{ Exception -> 0x0146 }
            java.lang.String r0 = r0.obtainAppId()     // Catch:{ Exception -> 0x0146 }
            r6.initAppDownloadList(r7, r0)     // Catch:{ Exception -> 0x0146 }
            int r1 = r8.hashCode()     // Catch:{ Exception -> 0x0146 }
            r2 = 4
            r3 = -1
            r4 = 1
            r5 = 0
            switch(r1) {
                case -1944579676: goto L_0x0061;
                case -934426579: goto L_0x0057;
                case 92611376: goto L_0x004d;
                case 94746189: goto L_0x0043;
                case 106440182: goto L_0x0039;
                case 109757538: goto L_0x002f;
                case 247395940: goto L_0x0025;
                case 1316768223: goto L_0x001b;
                default: goto L_0x001a;
            }     // Catch:{ Exception -> 0x0146 }
        L_0x001a:
            goto L_0x006b
        L_0x001b:
            java.lang.String r1 = "startAll"
            boolean r8 = r8.equals(r1)     // Catch:{ Exception -> 0x0146 }
            if (r8 == 0) goto L_0x006b
            r8 = 5
            goto L_0x006c
        L_0x0025:
            java.lang.String r1 = "enumerate"
            boolean r8 = r8.equals(r1)     // Catch:{ Exception -> 0x0146 }
            if (r8 == 0) goto L_0x006b
            r8 = 6
            goto L_0x006c
        L_0x002f:
            java.lang.String r1 = "start"
            boolean r8 = r8.equals(r1)     // Catch:{ Exception -> 0x0146 }
            if (r8 == 0) goto L_0x006b
            r8 = 0
            goto L_0x006c
        L_0x0039:
            java.lang.String r1 = "pause"
            boolean r8 = r8.equals(r1)     // Catch:{ Exception -> 0x0146 }
            if (r8 == 0) goto L_0x006b
            r8 = 2
            goto L_0x006c
        L_0x0043:
            java.lang.String r1 = "clear"
            boolean r8 = r8.equals(r1)     // Catch:{ Exception -> 0x0146 }
            if (r8 == 0) goto L_0x006b
            r8 = 4
            goto L_0x006c
        L_0x004d:
            java.lang.String r1 = "abort"
            boolean r8 = r8.equals(r1)     // Catch:{ Exception -> 0x0146 }
            if (r8 == 0) goto L_0x006b
            r8 = 3
            goto L_0x006c
        L_0x0057:
            java.lang.String r1 = "resume"
            boolean r8 = r8.equals(r1)     // Catch:{ Exception -> 0x0146 }
            if (r8 == 0) goto L_0x006b
            r8 = 1
            goto L_0x006c
        L_0x0061:
            java.lang.String r1 = "createDownload"
            boolean r8 = r8.equals(r1)     // Catch:{ Exception -> 0x0146 }
            if (r8 == 0) goto L_0x006b
            r8 = 7
            goto L_0x006c
        L_0x006b:
            r8 = -1
        L_0x006c:
            switch(r8) {
                case 0: goto L_0x0112;
                case 1: goto L_0x0112;
                case 2: goto L_0x0100;
                case 3: goto L_0x00dc;
                case 4: goto L_0x00a8;
                case 5: goto L_0x008a;
                case 6: goto L_0x0081;
                case 7: goto L_0x0071;
                default: goto L_0x006f;
            }     // Catch:{ Exception -> 0x0146 }
        L_0x006f:
            goto L_0x014a
        L_0x0071:
            org.json.JSONObject r8 = new org.json.JSONObject     // Catch:{ Exception -> 0x0146 }
            r9 = r9[r5]     // Catch:{ Exception -> 0x0146 }
            r8.<init>(r9)     // Catch:{ Exception -> 0x0146 }
            io.dcloud.net.JsDownload r8 = r6.createDownloadTask(r7, r8)     // Catch:{ Exception -> 0x0146 }
            r6.pushDownloadTask(r7, r0, r8)     // Catch:{ Exception -> 0x0146 }
            goto L_0x014a
        L_0x0081:
            r8 = r9[r5]     // Catch:{ Exception -> 0x0146 }
            r9 = r9[r4]     // Catch:{ Exception -> 0x0146 }
            r6.enumerate(r7, r8, r9, r0)     // Catch:{ Exception -> 0x0146 }
            goto L_0x014a
        L_0x008a:
            io.dcloud.net.DownloadJSMgr$AppDownloadInfo r7 = r6.getAppTaskList(r0)     // Catch:{ Exception -> 0x0146 }
            java.util.ArrayList<io.dcloud.net.JsDownload> r8 = r7.mList     // Catch:{ Exception -> 0x0146 }
            if (r8 == 0) goto L_0x014a
        L_0x0092:
            java.util.ArrayList<io.dcloud.net.JsDownload> r8 = r7.mList     // Catch:{ Exception -> 0x0146 }
            int r8 = r8.size()     // Catch:{ Exception -> 0x0146 }
            if (r5 >= r8) goto L_0x014a
            java.util.ArrayList<io.dcloud.net.JsDownload> r8 = r7.mList     // Catch:{ Exception -> 0x0146 }
            java.lang.Object r8 = r8.get(r5)     // Catch:{ Exception -> 0x0146 }
            io.dcloud.net.JsDownload r8 = (io.dcloud.net.JsDownload) r8     // Catch:{ Exception -> 0x0146 }
            r8.start()     // Catch:{ Exception -> 0x0146 }
            int r5 = r5 + 1
            goto L_0x0092
        L_0x00a8:
            io.dcloud.net.DownloadJSMgr$AppDownloadInfo r7 = r6.getAppTaskList(r0)     // Catch:{ Exception -> 0x0146 }
            r8 = r9[r5]     // Catch:{ Exception -> 0x0146 }
            int r8 = java.lang.Integer.parseInt(r8)     // Catch:{ Exception -> 0x0146 }
            if (r7 == 0) goto L_0x014a
            java.util.ArrayList<io.dcloud.net.JsDownload> r9 = r7.mList     // Catch:{ Exception -> 0x0146 }
            int r9 = r9.size()     // Catch:{ Exception -> 0x0146 }
            int r9 = r9 - r4
        L_0x00bb:
            if (r9 < 0) goto L_0x014a
            java.util.ArrayList<io.dcloud.net.JsDownload> r0 = r7.mList     // Catch:{ Exception -> 0x0146 }
            java.lang.Object r0 = r0.get(r9)     // Catch:{ Exception -> 0x0146 }
            io.dcloud.net.JsDownload r0 = (io.dcloud.net.JsDownload) r0     // Catch:{ Exception -> 0x0146 }
            if (r8 == r3) goto L_0x00d1
            int r1 = r0.mState     // Catch:{ Exception -> 0x0146 }
            if (r8 == r1) goto L_0x00d1
            r4 = -10000(0xffffffffffffd8f0, float:NaN)
            if (r8 != r4) goto L_0x00d9
            if (r1 == r2) goto L_0x00d9
        L_0x00d1:
            r0.abort()     // Catch:{ Exception -> 0x0146 }
            java.util.ArrayList<io.dcloud.net.JsDownload> r0 = r7.mList     // Catch:{ Exception -> 0x0146 }
            r0.remove(r9)     // Catch:{ Exception -> 0x0146 }
        L_0x00d9:
            int r9 = r9 + -1
            goto L_0x00bb
        L_0x00dc:
            r8 = r9[r5]     // Catch:{ Exception -> 0x0146 }
            io.dcloud.net.JsDownload r7 = r6.findDownloadTask(r7, r0, r8)     // Catch:{ Exception -> 0x0146 }
            boolean r8 = io.dcloud.common.util.PdrUtil.isEmpty(r7)     // Catch:{ Exception -> 0x0146 }
            if (r8 != 0) goto L_0x014a
            io.dcloud.net.DownloadJSMgr$AppDownloadInfo r8 = r6.getAppTaskList(r0)     // Catch:{ Exception -> 0x0146 }
            java.util.ArrayList<io.dcloud.net.JsDownload> r8 = r8.mList     // Catch:{ Exception -> 0x0146 }
            r8.remove(r7)     // Catch:{ Exception -> 0x0146 }
            r7.mPause = r4     // Catch:{ Exception -> 0x0146 }
            io.dcloud.common.util.net.DownloadMgr r8 = io.dcloud.common.util.net.DownloadMgr.getDownloadMgr()     // Catch:{ Exception -> 0x0146 }
            io.dcloud.net.DownloadNetWork r9 = r7.mDownloadNetWork     // Catch:{ Exception -> 0x0146 }
            r8.removeTask(r9)     // Catch:{ Exception -> 0x0146 }
            r7.abort()     // Catch:{ Exception -> 0x0146 }
            goto L_0x014a
        L_0x0100:
            r8 = r9[r5]     // Catch:{ Exception -> 0x0146 }
            io.dcloud.net.JsDownload r7 = r6.findDownloadTask(r7, r0, r8)     // Catch:{ Exception -> 0x0146 }
            r7.mPause = r4     // Catch:{ Exception -> 0x0146 }
            io.dcloud.common.util.net.DownloadMgr r8 = io.dcloud.common.util.net.DownloadMgr.getDownloadMgr()     // Catch:{ Exception -> 0x0146 }
            io.dcloud.net.DownloadNetWork r7 = r7.mDownloadNetWork     // Catch:{ Exception -> 0x0146 }
            r8.removeTask(r7)     // Catch:{ Exception -> 0x0146 }
            goto L_0x014a
        L_0x0112:
            r8 = r9[r5]     // Catch:{ Exception -> 0x0146 }
            io.dcloud.net.JsDownload r7 = r6.findDownloadTask(r7, r0, r8)     // Catch:{ Exception -> 0x0146 }
            r7.start()     // Catch:{ Exception -> 0x0146 }
            r8 = r9[r4]     // Catch:{ Exception -> 0x0146 }
            boolean r9 = android.text.TextUtils.isEmpty(r8)     // Catch:{ Exception -> 0x0146 }
            if (r9 != 0) goto L_0x014a
            org.json.JSONObject r9 = new org.json.JSONObject     // Catch:{ Exception -> 0x0146 }
            r9.<init>(r8)     // Catch:{ Exception -> 0x0146 }
            int r8 = r9.length()     // Catch:{ Exception -> 0x0146 }
            if (r8 <= 0) goto L_0x014a
            java.util.Iterator r8 = r9.keys()     // Catch:{ Exception -> 0x0146 }
        L_0x0132:
            boolean r0 = r8.hasNext()     // Catch:{ Exception -> 0x0146 }
            if (r0 == 0) goto L_0x014a
            java.lang.Object r0 = r8.next()     // Catch:{ Exception -> 0x0146 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x0146 }
            java.lang.String r1 = r9.getString(r0)     // Catch:{ Exception -> 0x0146 }
            r7.setRequestHeader(r0, r1)     // Catch:{ Exception -> 0x0146 }
            goto L_0x0132
        L_0x0146:
            r7 = move-exception
            r7.printStackTrace()
        L_0x014a:
            r7 = 0
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.net.DownloadJSMgr.execute(io.dcloud.common.DHInterface.IWebview, java.lang.String, java.lang.String[]):java.lang.String");
    }

    /* access modifiers changed from: package-private */
    public void saveDownloadTaskInfo(String str, String str2, String str3) {
        Message obtain = Message.obtain();
        obtain.what = 1;
        obtain.obj = new String[]{str, str2, str3};
        this.mHander.sendMessage(obtain);
    }
}
