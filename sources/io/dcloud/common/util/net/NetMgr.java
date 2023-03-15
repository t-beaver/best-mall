package io.dcloud.common.util.net;

import android.content.IntentFilter;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.ICore;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.net.http.IServer;
import io.dcloud.feature.internal.sdk.SDK;

public class NetMgr extends AbsMgr implements IMgr.NetEvent {
    DownloadMgr mDownloadMgr;
    IServer mLocalServer = null;
    NetCheckReceiver mNetCheckReceiver = null;
    UploadMgr mUploadMgr;

    public NetMgr(ICore iCore) {
        super(iCore, "netmgr", IMgr.MgrType.NetMgr);
        startMiniServer();
        this.mUploadMgr = UploadMgr.getUploadMgr();
        this.mDownloadMgr = DownloadMgr.getDownloadMgr();
        this.mNetCheckReceiver = new NetCheckReceiver(this);
        IntentFilter intentFilter = new IntentFilter(NetCheckReceiver.netACTION);
        intentFilter.addAction(NetCheckReceiver.simACTION);
        getContext().registerReceiver(this.mNetCheckReceiver, intentFilter);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v2, resolved type: java.lang.String} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private io.dcloud.common.util.net.http.IServer initLocalServer() {
        /*
            r11 = this;
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            boolean r1 = io.dcloud.common.util.BaseInfo.SyncDebug
            r2 = 1
            r3 = 0
            r4 = 2
            if (r1 == 0) goto L_0x0033
            java.lang.Class[] r1 = new java.lang.Class[r4]
            java.lang.Class<io.dcloud.common.DHInterface.AbsMgr> r5 = io.dcloud.common.DHInterface.AbsMgr.class
            r1[r3] = r5
            java.lang.Class r5 = java.lang.Integer.TYPE
            r1[r2] = r5
            java.lang.Object[] r5 = new java.lang.Object[r4]
            r5[r3] = r11
            r6 = 13131(0x334b, float:1.84E-41)
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)
            r5[r2] = r6
            java.lang.String r6 = "io.dcloud.common.util.net.http.LocalServer"
            java.lang.Object r1 = io.dcloud.common.adapter.util.PlatformUtil.newInstance(r6, r1, r5)
            if (r1 == 0) goto L_0x0033
            boolean r5 = r1 instanceof io.dcloud.common.util.net.http.IServer
            if (r5 == 0) goto L_0x0033
            io.dcloud.common.util.net.http.IServer r1 = (io.dcloud.common.util.net.http.IServer) r1
            r0.add(r1)
        L_0x0033:
            int r1 = android.os.Build.VERSION.SDK_INT
            r5 = 21
            if (r1 < r5) goto L_0x00d6
            boolean r1 = io.dcloud.common.util.BaseInfo.SyncDebug
            if (r1 == 0) goto L_0x00d6
            io.dcloud.common.DHInterface.ICore r1 = r11.mCore
            android.content.Context r1 = r1.obtainActivityContext()
            android.app.Activity r1 = (android.app.Activity) r1
            android.content.Intent r5 = r1.getIntent()
            java.lang.String r6 = "ip"
            java.lang.String r7 = r5.getStringExtra(r6)
            java.lang.String r8 = "port"
            java.lang.String r5 = r5.getStringExtra(r8)
            boolean r9 = io.dcloud.common.util.PdrUtil.isEmpty(r7)
            if (r9 != 0) goto L_0x0061
            boolean r9 = io.dcloud.common.util.PdrUtil.isEmpty(r5)
            if (r9 == 0) goto L_0x009b
        L_0x0061:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.io.File r10 = r1.getExternalCacheDir()
            java.lang.String r10 = r10.getPath()
            r9.append(r10)
            java.lang.String r10 = java.io.File.separator
            r9.append(r10)
            java.lang.String r10 = "debug_info"
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            byte[] r9 = io.dcloud.common.adapter.io.DHFile.readAll(r9)
            if (r9 == 0) goto L_0x009b
            java.lang.String r5 = new java.lang.String
            r5.<init>(r9)
            com.alibaba.fastjson.JSONObject r5 = com.alibaba.fastjson.JSON.parseObject(r5)
            java.lang.Object r6 = r5.get(r6)
            r7 = r6
            java.lang.String r7 = (java.lang.String) r7
            java.lang.Object r5 = r5.get(r8)
            java.lang.String r5 = (java.lang.String) r5
        L_0x009b:
            boolean r6 = io.dcloud.common.util.PdrUtil.isEmpty(r7)
            if (r6 != 0) goto L_0x00d6
            boolean r6 = io.dcloud.common.util.PdrUtil.isEmpty(r5)
            if (r6 != 0) goto L_0x00d6
            r6 = 4
            java.lang.Class[] r8 = new java.lang.Class[r6]
            java.lang.Class<io.dcloud.common.DHInterface.AbsMgr> r9 = io.dcloud.common.DHInterface.AbsMgr.class
            r8[r3] = r9
            java.lang.Class<android.app.Activity> r9 = android.app.Activity.class
            r8[r2] = r9
            java.lang.Class<java.lang.String> r9 = java.lang.String.class
            r8[r4] = r9
            java.lang.Class<java.lang.String> r9 = java.lang.String.class
            r10 = 3
            r8[r10] = r9
            java.lang.Object[] r6 = new java.lang.Object[r6]
            r6[r3] = r11
            r6[r2] = r1
            r6[r4] = r7
            r6[r10] = r5
            java.lang.String r1 = "io.dcloud.common.util.net.http.LocalServer2"
            java.lang.Object r1 = io.dcloud.common.adapter.util.PlatformUtil.newInstance(r1, r8, r6)
            if (r1 == 0) goto L_0x00d6
            boolean r2 = r1 instanceof io.dcloud.common.util.net.http.IServer
            if (r2 == 0) goto L_0x00d6
            io.dcloud.common.util.net.http.IServer r1 = (io.dcloud.common.util.net.http.IServer) r1
            r0.add(r1)
        L_0x00d6:
            io.dcloud.common.util.net.NetMgr$1 r1 = new io.dcloud.common.util.net.NetMgr$1
            r1.<init>(r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.net.NetMgr.initLocalServer():io.dcloud.common.util.net.http.IServer");
    }

    public void dispose() {
        IServer iServer = this.mLocalServer;
        if (iServer != null) {
            iServer.stop();
        }
        UploadMgr uploadMgr = this.mUploadMgr;
        if (uploadMgr != null) {
            uploadMgr.dispose();
        }
        getContext().unregisterReceiver(this.mNetCheckReceiver);
    }

    public void onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
        if (sysEventType == ISysEventListener.SysEventType.onStop) {
            IServer iServer = this.mLocalServer;
            if (iServer != null) {
                iServer.stop();
                this.mLocalServer = null;
            }
        } else if ((sysEventType == ISysEventListener.SysEventType.onResume || sysEventType == ISysEventListener.SysEventType.onNewIntent) && this.mLocalServer == null) {
            startMiniServer();
        }
    }

    public Object processEvent(IMgr.MgrType mgrType, int i, Object obj) {
        try {
            if (!checkMgrId(mgrType)) {
                return this.mCore.dispatchEvent(mgrType, i, obj);
            }
        } catch (Throwable th) {
            Logger.w("NetMgr.processEvent", th);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void startMiniServer() {
        if (BaseInfo.ISDEBUG && !SDK.isUniMPSDK()) {
            IServer initLocalServer = initLocalServer();
            this.mLocalServer = initLocalServer;
            initLocalServer.start();
        }
    }
}
