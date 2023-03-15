package com.taobao.weex.performance;

import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXConfigAdapter;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.utils.WXUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WXStateRecord {
    /* access modifiers changed from: private */
    public long jsThreadTime;
    /* access modifiers changed from: private */
    public Runnable jsThreadWatchTask;
    private RecordList<Info> mActionHistory;
    private RecordList<Info> mExceptionHistory;
    private RecordList<Info> mIPCExceptionHistory;
    private RecordList<Info> mJsThradWatchHistory;
    private RecordList<Info> mJscCrashHistory;
    private RecordList<Info> mJscReloadHistory;
    private RecordList<Info> mJsfmInitHistory;

    private static class SingleTonHolder {
        /* access modifiers changed from: private */
        public static final WXStateRecord S_INSTANCE = new WXStateRecord();

        private SingleTonHolder() {
        }
    }

    public static WXStateRecord getInstance() {
        return SingleTonHolder.S_INSTANCE;
    }

    private WXStateRecord() {
        this.jsThreadTime = -1;
        this.jsThreadWatchTask = new Runnable() {
            public void run() {
                if (WXStateRecord.this.jsThreadTime == -1) {
                    long unused = WXStateRecord.this.jsThreadTime = WXUtils.getFixUnixTime();
                }
                long fixUnixTime = WXUtils.getFixUnixTime() - WXStateRecord.this.jsThreadTime;
                WXStateRecord wXStateRecord = WXStateRecord.this;
                wXStateRecord.recordJsThreadWatch("diff:" + fixUnixTime);
                long unused2 = WXStateRecord.this.jsThreadTime = WXUtils.getFixUnixTime();
                WXBridgeManager.getInstance().postDelay(WXStateRecord.this.jsThreadWatchTask, 500);
            }
        };
        this.mExceptionHistory = new RecordList<>(10);
        this.mActionHistory = new RecordList<>(20);
        this.mJsfmInitHistory = new RecordList<>(10);
        this.mJscCrashHistory = new RecordList<>(10);
        this.mJscReloadHistory = new RecordList<>(10);
        this.mJsThradWatchHistory = new RecordList<>(20);
        this.mIPCExceptionHistory = new RecordList<>(20);
    }

    public void recordException(String str, String str2) {
        if (str2.length() > 200) {
            str2 = str2.substring(0, 200);
        }
        recordCommon(this.mExceptionHistory, new Info(WXUtils.getFixUnixTime(), str, str2));
    }

    public void recordAction(String str, String str2) {
        recordCommon(this.mActionHistory, new Info(WXUtils.getFixUnixTime(), str, str2));
    }

    public void recordIPCException(String str, String str2) {
        if (str2.length() > 200) {
            str2 = str2.substring(0, 200);
        }
        recordCommon(this.mIPCExceptionHistory, new Info(WXUtils.getFixUnixTime(), str, str2));
    }

    public void onJSFMInit() {
        recoreJsfmInitHistory("setJsfmVersion");
    }

    public void recoreJsfmInitHistory(String str) {
        recordCommon(this.mJsfmInitHistory, new Info(WXUtils.getFixUnixTime(), "JSFM", str));
    }

    public void recordJsThreadWatch(String str) {
        recordCommon(this.mJsThradWatchHistory, new Info(WXUtils.getFixUnixTime(), "jsWatch", str));
    }

    public void onJSEngineReload(String str) {
        recordCommon(this.mJscReloadHistory, new Info(WXUtils.getFixUnixTime(), str, "onJSEngineReload"));
    }

    public void onJSCCrash(String str) {
        recordCommon(this.mJscCrashHistory, new Info(WXUtils.getFixUnixTime(), str, "onJSCCrash"));
    }

    private void recordCommon(RecordList<Info> recordList, Info info) {
        if (recordList != null && info != null) {
            try {
                recordList.add(info);
                if (!recordList.isEmpty() && recordList.size() > recordList.maxSize) {
                    recordList.poll();
                }
            } catch (Throwable th) {
                th.getStackTrace();
            }
        }
    }

    public Map<String, String> getStateInfo() {
        HashMap hashMap = new HashMap(5);
        hashMap.put("reInitCount", String.valueOf(WXBridgeManager.reInitCount));
        ArrayList arrayList = new ArrayList(this.mExceptionHistory.size() + this.mActionHistory.size() + this.mJsfmInitHistory.size() + this.mJscCrashHistory.size() + this.mJscReloadHistory.size() + this.mJsThradWatchHistory.size());
        arrayList.addAll(this.mExceptionHistory);
        arrayList.addAll(this.mActionHistory);
        arrayList.addAll(this.mJsfmInitHistory);
        arrayList.addAll(this.mJscCrashHistory);
        arrayList.addAll(this.mJscReloadHistory);
        arrayList.addAll(this.mJsThradWatchHistory);
        arrayList.addAll(this.mIPCExceptionHistory);
        Collections.sort(arrayList);
        hashMap.put("stateInfoList", arrayList.toString());
        IWXConfigAdapter wxConfigAdapter = WXSDKManager.getInstance().getWxConfigAdapter();
        if (wxConfigAdapter != null && AbsoluteConst.TRUE.equalsIgnoreCase(wxConfigAdapter.getConfig("wxapm", "dumpIpcPageInfo", AbsoluteConst.TRUE))) {
            hashMap.put("pageQueueInfo", WXBridgeManager.getInstance().dumpIpcPageInfo());
        }
        return hashMap;
    }

    private static class RecordList<E> extends ConcurrentLinkedQueue<E> {
        /* access modifiers changed from: private */
        public int maxSize;

        public RecordList(int i) {
            this.maxSize = i;
        }
    }

    private static class Info implements Comparable<Info> {
        private String instanceId;
        private String msg;
        private long time;

        public Info(long j, String str, String str2) {
            this.time = j;
            this.instanceId = str;
            this.msg = str2;
        }

        public String toString() {
            return Operators.ARRAY_START + this.instanceId + Operators.ARRAY_SEPRATOR + this.time + Operators.ARRAY_SEPRATOR + this.msg + "]->";
        }

        public int compareTo(Info info) {
            long j = this.time;
            long j2 = info.time;
            if (j == j2) {
                return 0;
            }
            return j > j2 ? 1 : -1;
        }
    }

    public void startJSThreadWatchDog() {
        WXBridgeManager.getInstance().post(this.jsThreadWatchTask);
    }
}
