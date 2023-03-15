package io.dcloud.common.util.net;

public class UploadMgr {
    private static UploadMgr mUploadMgr;
    private NetWorkLoop mUploadLoop;

    private UploadMgr() {
        NetWorkLoop netWorkLoop = new NetWorkLoop();
        this.mUploadLoop = netWorkLoop;
        netWorkLoop.startThreadPool();
    }

    public static UploadMgr getUploadMgr() {
        if (mUploadMgr == null) {
            mUploadMgr = new UploadMgr();
        }
        return mUploadMgr;
    }

    public void abort(NetWork netWork) {
        removeNetWork(netWork);
    }

    public void dispose() {
    }

    public void removeNetWork(NetWork netWork) {
        this.mUploadLoop.removeNetWork(netWork);
    }

    public void start(NetWork netWork) {
        this.mUploadLoop.addNetWork(netWork);
    }
}
