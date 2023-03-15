package io.dcloud.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.ViewGroup;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.e.c.h.b;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class TestUtil {
    public static String CREATE_NWINDOW = "createNWindow une create";
    public static String CREATE_SHOW_WEBVIEW_ANIMATION = "createShowWebviewAnimation";
    public static String CREATE_VIEW_OPTIONS = "createViewOptions";
    public static String CREATE_WEBVIEW = "createWebview";
    static final boolean DEBUG = true;
    public static String SHOW_WEBVIEW = "showWebview";
    public static String START_APP_SET_ROOTVIEW = "start_app_set_rootview";
    public static String START_SHOW_WEBVIEW_ANIMATION = "startShowWebviewAnimation";
    public static String START_STREAM_APP = "start_stream_app";
    public static String START_STREAM_APP_RETRY = "r";
    public static String STREAM_APP_POINT = "t";
    private static final String TAG = "useTime";
    public static String WEBVIEW_INIT = "webview_init";
    private static HashMap mObjs = new HashMap(2);
    private static ArrayList<Timer> mTimers = new ArrayList<>(1);

    public static class DCErrorInfo {
        int ec;
        int et;

        public DCErrorInfo(int i, int i2) {
            this.ec = i;
            this.et = i2;
        }
    }

    public static class PointTime {
        public static final int AC_TYPE_1 = 1;
        public static final int AC_TYPE_1_0 = 1000;
        public static final int AC_TYPE_1_1 = 1100;
        public static final int AC_TYPE_1_2 = 1200;
        public static final int AC_TYPE_1_3 = 1300;
        public static final int AC_TYPE_1_4 = 1400;
        public static final int AC_TYPE_2 = 2;
        public static final int AC_TYPE_3 = 3;
        public static final int AC_TYPE_4 = 4;
        public static final int AC_TYPE_5 = 5;
        public static final int AD_CLICK = 41;
        public static final int AD_DL_COMPLETE = 30;
        public static final int AD_DL_FAILED = 32;
        public static final int AD_DL_START = 29;
        public static final int AD_DPLK = 50;
        public static final int AD_INSTALL = 31;
        public static final int AD_SIM_CLICK = 46;
        public static final int AD_SIM_VIEW = 45;
        public static final int AD_VIEW = 40;
        public static final int BEGIN_APK = 29;
        public static final int CLICK_PUSH = 20;
        public static final int CLOSE_WEBVIEW = 10;
        public static final int CONNECT_TIMEOUT_ERROR = 7;
        public static final String DATA_CACHE_PAGES = "stream_app_cache_pages";
        public static final String DATA_DOWNLOAD_COMPLETED = "stream_app_completed";
        public static final String DATA_IN_APP_COMMIT_DATA = "in_app_commit_data";
        public static final String DATA_START_TIMES = "stream_app_start_times";
        public static final String DATA_START_TIMES_ACTIVATE = "stream_app_start_times_activate";
        public static final int DECOMPRESSION_ERROR = 15;
        public static final int ERROR_TYPE_INDEXS = 3;
        public static final int ERROR_TYPE_INDEX_ZIP = 2;
        public static final int ERROR_TYPE_STREAM = 1;
        public static final int ERROR_TYPE_WAP2APP_INDEX = 4;
        public static final int FILE_CREATION_ERROR = 13;
        public static final int FILE_DELETION_ERROR = 11;
        public static final int FILE_EXIST = 1;
        public static final int FILE_INPUT_READ = 16;
        public static final int FILE_RENAME_ERROR = 12;
        public static final int FINISH_APK = 30;
        public static final int IO_ERROR = 10;
        public static final int NETWORK_ERROR = 2;
        public static final int NOT_NETWORK = 20;
        public static final int OTHER_ERROR = 6;
        public static final int PARSE_ERROR = 14;
        static final String PT = "point_time_";
        public static final int QUIT_APP = 1;
        public static final int RESUOURCE_NOT_FOUND = 5;
        public static final int SDCARD_ERROR = 4;
        public static final int SDCARD_SPACE_SHORTAGE = 9;
        public static final int SOCKET_TIMEOUT_ERROR = 8;
        public static final String STATUS_DOWNLOAD_COMPLETED = "download_completed";
        public static final String STATUS_INSTALLED = "installed";
        public static final int SUCCESS = 0;
        public static final int S_TYPE_0 = 0;
        public static final int S_TYPE_1 = 1;
        public static final int S_TYPE_10 = 10;
        public static final int S_TYPE_2 = 2;
        public static final int S_TYPE_3 = 3;
        public static final int S_TYPE_4 = 4;
        public static final int S_TYPE_5 = 5;
        public static final int S_TYPE_6 = 6;
        public static final int S_TYPE_8 = 8;
        public static final int S_TYPE_9 = 9;
        public static final int T_0 = 0;
        public static final int T_1 = 1;
        public static final int T_2 = 2;
        public static final int T_3 = 3;
        public static final int T_4 = 4;
        public static final int T_5 = 5;
        public static final int T_6 = 6;
        public static final int UNKNOWN_HOST_ERROR = 21;
        public static final int URL_ERROR = 3;
        private static ArrayList<DCErrorInfo> arrayList = new ArrayList<>();
        public static int mEc = -1;
        public static int mEt = -1;
        static HashMap<String, PointTime> sPoinTimes = new HashMap<>();
        String appid;
        int index = 0;
        long lastPointTime = System.currentTimeMillis();
        String name;
        long[] points = null;
        long startTime = System.currentTimeMillis();

        PointTime(String str, int i) {
            this.name = str;
            this.points = new long[i];
        }

        public static synchronized void addErrorInfo(DCErrorInfo dCErrorInfo) {
            synchronized (PointTime.class) {
                arrayList.add(dCErrorInfo);
                mEc = dCErrorInfo.ec;
                mEt = dCErrorInfo.et;
            }
        }

        public static boolean checkCommitEnv(Context context, String str, String str2) {
            if (AppStatus.getAppStatus(str) != 0 && BaseInfo.useStreamAppStatistic(context) && hasPointTime(str2)) {
                return true;
            }
            return false;
        }

        public static void commitRatio(Context context, String str, String str2, int i, String str3, HashMap<String, Object> hashMap) {
            b.a(context, str, str2, i, str3, hashMap);
        }

        public static void commitTid(Context context, String str, String str2, String str3, int i) {
            commitTid(context, str, str2, str3, i, (String) null, (String) null, (JSONObject) null, (String) null, (String) null, (HashMap<String, Object>) null);
        }

        public static PointTime createPointTime(String str, int i) {
            PointTime pointTime = new PointTime(str, i);
            sPoinTimes.remove(str);
            sPoinTimes.put(str, pointTime);
            return pointTime;
        }

        public static void delData(Context context, String str, String str2) {
            context.getSharedPreferences(PT + str, 0).edit().remove(str2).commit();
        }

        public static void delPointData(Context context, String str) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PT + str, 0);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            int i = sharedPreferences.getInt(TestUtil.START_STREAM_APP_RETRY, 0);
            for (int i2 = 0; i2 < i; i2++) {
                edit.remove(TestUtil.STREAM_APP_POINT + i2);
            }
            edit.remove(TestUtil.START_STREAM_APP_RETRY);
            edit.commit();
        }

        public static void deleteStreamAppStatus(Context context, String str, String str2) {
            SharedPreferences.Editor edit = context.getSharedPreferences(PT + str, 0).edit();
            edit.remove(str2);
            edit.commit();
        }

        public static PointTime destroyPointTime(String str) {
            return sPoinTimes.remove(str);
        }

        public static String getAllCommitData(Context context, String str) {
            int i = 0;
            SharedPreferences sharedPreferences = context.getSharedPreferences(PT + str, 0);
            int i2 = sharedPreferences.getInt(TestUtil.START_STREAM_APP_RETRY, 0);
            if (i2 == 0) {
                return null;
            }
            StringBuffer stringBuffer = new StringBuffer();
            while (i < i2) {
                StringBuilder sb = new StringBuilder();
                sb.append(TestUtil.STREAM_APP_POINT);
                sb.append(i != 0 ? Integer.valueOf(i) : "");
                String string = sharedPreferences.getString(sb.toString(), "");
                if (!TextUtils.isEmpty(string)) {
                    stringBuffer.append(string);
                }
                i++;
            }
            return stringBuffer.toString();
        }

        public static String getBaseVer(Context context) {
            return "1.9.9.81676";
        }

        public static String getData(Context context, String str, String str2) {
            return context.getSharedPreferences(PT + str, 0).getString(str2, "");
        }

        public static String getErrorInfoString() {
            ArrayList<DCErrorInfo> arrayList2 = arrayList;
            if (arrayList2 == null || arrayList2.size() <= 0) {
                return null;
            }
            StringBuffer stringBuffer = new StringBuffer();
            StringBuffer stringBuffer2 = new StringBuffer();
            for (int i = 0; i < arrayList.size(); i++) {
                DCErrorInfo dCErrorInfo = arrayList.get(i);
                if (i == 0) {
                    stringBuffer.append(dCErrorInfo.ec);
                    stringBuffer2.append(dCErrorInfo.et);
                } else {
                    stringBuffer.append("%2c");
                    stringBuffer.append(dCErrorInfo.ec);
                    stringBuffer2.append("%2c");
                    stringBuffer2.append(dCErrorInfo.et);
                }
            }
            arrayList.clear();
            return "&ec=" + stringBuffer.toString() + "&et=" + stringBuffer2.toString();
        }

        public static PointTime getPointTime(String str) {
            return sPoinTimes.get(str);
        }

        public static boolean hasPointTime(String str) {
            return sPoinTimes.containsKey(str);
        }

        public static boolean hasStreamAppStatus(Context context, String str, String str2) {
            return context.getSharedPreferences(PT + str, 0).getBoolean(str2, false);
        }

        public static void pre(IApp iApp, String str, String str2, String str3, String str4, JSONArray jSONArray) {
            b.a(iApp, str, str2, str3, str4, jSONArray);
        }

        public static void saveData(Context context, String str, String str2, String str3) {
            SharedPreferences.Editor edit = context.getSharedPreferences(PT + str, 0).edit();
            edit.putString(str2, str3);
            edit.commit();
        }

        public static void saveStreamAppStatus(Context context, String str, String str2) {
            SharedPreferences.Editor edit = context.getSharedPreferences(PT + str, 0).edit();
            edit.putBoolean(str2, true);
            edit.commit();
        }

        public static PointTime updatePointTime(String str) {
            PointTime pointTime = sPoinTimes.get(str);
            if (pointTime == null) {
                return pointTime;
            }
            sPoinTimes.remove(str);
            PointTime pointTime2 = new PointTime(str, pointTime.points.length);
            pointTime2.updateData(pointTime);
            sPoinTimes.put(str, pointTime2);
            return pointTime2;
        }

        public int getIndex() {
            return this.index;
        }

        public long getPoint(int i) {
            return this.points[i];
        }

        public String getPointsString() {
            return getPointsString("|");
        }

        public long getStartTime() {
            return this.startTime;
        }

        public PointTime point() {
            point(this.index, System.currentTimeMillis() - this.lastPointTime);
            return this;
        }

        public void updateData(PointTime pointTime) {
            this.appid = pointTime.appid;
            this.index = pointTime.index;
            this.startTime = pointTime.startTime;
            long[] jArr = pointTime.points;
            System.arraycopy(jArr, 0, this.points, 0, jArr.length);
        }

        public static void commitTid(Context context, String str, String str2, String str3, int i, String str4, boolean z, String str5) {
            b.a(context, str, str2, str3, i, (String) null, (String) null, (JSONObject) null, (String) null, (String) null, str4, str5, (HashMap<String, Object>) null);
        }

        public String getPointsString(String str) {
            StringBuffer stringBuffer = new StringBuffer(str);
            int i = 0;
            while (true) {
                long[] jArr = this.points;
                if (i >= jArr.length) {
                    return stringBuffer.toString();
                }
                if (i < this.index) {
                    stringBuffer.append(jArr[i]);
                }
                stringBuffer.append(str);
                i++;
            }
        }

        public static void commitTid(Context context, String str, String str2, String str3, int i, String str4, String str5, String str6, HashMap<String, Object> hashMap) {
            b.a(context, str, str2, str3, i, (String) null, (String) null, (JSONObject) null, str4, str5, str6, "", hashMap);
        }

        public PointTime point(int i) {
            point(i, System.currentTimeMillis() - this.lastPointTime);
            return this;
        }

        public static void commitTid(Context context, String str, String str2, String str3, int i, String str4, String str5, JSONObject jSONObject, String str6, String str7, HashMap<String, Object> hashMap) {
            b.a(context, str, str2, str3, i, str4, str5, jSONObject, str6, str7, (String) null, "", hashMap);
        }

        public PointTime point(int i, long j) {
            if (i < this.points.length) {
                this.lastPointTime = System.currentTimeMillis();
                this.points[i] = j;
                this.index++;
            }
            return this;
        }
    }

    public static void clearTimers() {
        mTimers.clear();
    }

    public static void debug(ViewGroup viewGroup) {
    }

    public static void delete(String str) {
        Timer findTimer = findTimer(str);
        if (findTimer != null) {
            mTimers.remove(findTimer);
        }
    }

    private static Timer findTimer(String str) {
        for (int size = mTimers.size() - 1; size >= 0; size--) {
            Timer timer = mTimers.get(size);
            if (timer.name.equals(str)) {
                return timer;
            }
        }
        return null;
    }

    public static Object getRecord(String str) {
        return mObjs.get(str);
    }

    public static long getUseTime(String str, String str2) {
        Timer findTimer = findTimer(str);
        if (findTimer != null) {
            return findTimer.pointTime(str2);
        }
        return 0;
    }

    public static void mark(String str) {
        Logger.d(TAG, str);
    }

    public static void print(String str) {
        Timer findTimer = findTimer(str);
        if (findTimer != null) {
            findTimer.print();
        }
    }

    public static void record(String str, Object obj) {
        mObjs.put(str, obj);
    }

    private static void record0(String str, String str2) {
        Timer findTimer = findTimer(str);
        if (findTimer != null) {
            mTimers.remove(findTimer);
        }
        mTimers.add(new Timer(str, str2));
    }

    public static void setRecordExtra(String str, String str2) {
        Timer findTimer = findTimer(str);
        if (findTimer != null) {
            findTimer.extra = str2;
        }
    }

    public static void timePoints(String str, int i) {
    }

    public static void record(String str) {
        record0(str, "");
    }

    static class Timer {
        long birthTime;
        String extra;
        long lastPointTime;
        long lastPrintTime;
        String name;
        long wholeUseTime;

        Timer(String str) {
            this.name = str;
            long currentTimeMillis = System.currentTimeMillis();
            this.birthTime = currentTimeMillis;
            this.lastPrintTime = currentTimeMillis;
            this.lastPointTime = currentTimeMillis;
        }

        /* access modifiers changed from: package-private */
        public long pointTime(String str) {
            long j = this.lastPrintTime;
            long currentTimeMillis = System.currentTimeMillis();
            this.lastPointTime = currentTimeMillis;
            long j2 = currentTimeMillis - j;
            Logger.i(TestUtil.TAG, "name :" + this.name + "; <<-- " + str + " -->> pointTime = " + j2);
            return j2;
        }

        /* access modifiers changed from: package-private */
        public void print() {
            long currentTimeMillis = System.currentTimeMillis();
            this.lastPrintTime = currentTimeMillis;
            this.wholeUseTime = currentTimeMillis - this.birthTime;
            Logger.i(TestUtil.TAG, "name :" + this.name + "; <<-- " + this.extra + " -->> wholeUseTime = " + this.wholeUseTime);
        }

        Timer(String str, String str2) {
            this(str);
            this.extra = str2;
        }

        /* access modifiers changed from: package-private */
        public void print(String str) {
            long j = this.lastPrintTime;
            print();
            long j2 = this.lastPrintTime - j;
            Logger.e(TestUtil.TAG, "name :" + this.name + "; <<-- " + str + " -->> useTime = " + j2);
        }
    }

    public static void record(String str, String str2) {
        record0(str, str2);
    }

    public static void print(String str, String str2) {
        Timer findTimer = findTimer(str);
        if (findTimer != null) {
            findTimer.print(str2);
        }
    }
}
