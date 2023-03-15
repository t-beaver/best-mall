package io.dcloud.js.geolocation.system;

import android.content.Context;
import io.dcloud.common.DHInterface.IEventCallback;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.js.geolocation.GeoManagerBase;

public class LocalGeoManager extends GeoManagerBase {
    public static final String TAG = "LocalGeoManager";
    /* access modifiers changed from: private */
    public a a;

    class a implements IEventCallback {
        a() {
        }

        public Object onCallBack(String str, Object obj) {
            if ((!PdrUtil.isEquals(str, AbsoluteConst.EVENTS_WINDOW_CLOSE) && !PdrUtil.isEquals(str, AbsoluteConst.EVENTS_CLOSE)) || !(obj instanceof IWebview)) {
                return null;
            }
            if (LocalGeoManager.this.a != null) {
                LocalGeoManager.this.a.c(a.f);
            }
            ((AdaFrameView) ((IWebview) obj).obtainFrameView()).removeFrameViewListener(this);
            return null;
        }
    }

    public LocalGeoManager(Context context) {
        super(context);
    }

    /* access modifiers changed from: package-private */
    public a b() {
        if (this.a == null) {
            this.a = new a(this.mContext, "");
        }
        return this.a;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x005a A[Catch:{ Exception -> 0x011a }] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x006a A[Catch:{ Exception -> 0x011a }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00b7 A[Catch:{ Exception -> 0x011a }] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00ba A[Catch:{ Exception -> 0x011a }] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00c1 A[Catch:{ Exception -> 0x011a }] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00cb A[Catch:{ Exception -> 0x011a }] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00ce  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00d8 A[Catch:{ Exception -> 0x011a }] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00e0 A[Catch:{ Exception -> 0x011a }] */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00e4 A[Catch:{ Exception -> 0x011a }] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00f3 A[Catch:{ Exception -> 0x011a }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String execute(io.dcloud.common.DHInterface.IWebview r20, java.lang.String r21, java.lang.String[] r22) {
        /*
            r19 = this;
            r7 = r19
            r0 = r21
            r1 = r22
            java.lang.String r8 = ""
            java.lang.String r2 = "getCurrentPosition"
            boolean r2 = r0.equals(r2)     // Catch:{ Exception -> 0x011a }
            java.lang.String r3 = "only support wgs84"
            r4 = 17
            java.lang.String r5 = "{code:%d,message:'%s'}"
            java.lang.String r6 = "wgs84"
            r9 = 6
            r10 = 2147483647(0x7fffffff, float:NaN)
            r11 = 7
            r12 = 3
            r13 = 2
            java.lang.String r14 = "null"
            r15 = 1
            r16 = 0
            if (r2 == 0) goto L_0x0085
            r0 = r1[r15]     // Catch:{ Exception -> 0x011a }
            boolean r17 = java.lang.Boolean.parseBoolean(r0)     // Catch:{ Exception -> 0x011a }
            r0 = r1[r13]     // Catch:{ Exception -> 0x011a }
            int r18 = java.lang.Integer.parseInt(r0)     // Catch:{ Exception -> 0x011a }
            int r0 = r1.length     // Catch:{ Exception -> 0x011a }
            if (r0 <= r11) goto L_0x0038
            r0 = r1[r9]     // Catch:{ Exception -> 0x011a }
            goto L_0x0039
        L_0x0038:
            r0 = r14
        L_0x0039:
            boolean r2 = r14.equals(r0)     // Catch:{ Exception -> 0x011a }
            if (r2 != 0) goto L_0x0044
            int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ Exception -> 0x011a }
            r10 = r0
        L_0x0044:
            r0 = r1[r12]     // Catch:{ Exception -> 0x011a }
            boolean r0 = io.dcloud.common.util.PdrUtil.isEquals(r0, r6)     // Catch:{ Exception -> 0x011a }
            if (r0 != 0) goto L_0x0057
            r0 = r1[r12]     // Catch:{ Exception -> 0x011a }
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r0)     // Catch:{ Exception -> 0x011a }
            if (r0 == 0) goto L_0x0055
            goto L_0x0057
        L_0x0055:
            r0 = 0
            goto L_0x0058
        L_0x0057:
            r0 = 1
        L_0x0058:
            if (r0 == 0) goto L_0x006a
            r2 = r1[r16]     // Catch:{ Exception -> 0x011a }
            r0 = r19
            r1 = r20
            r3 = r17
            r4 = r18
            r5 = r10
            r0.getCurrentLocation(r1, r2, r3, r4, r5)     // Catch:{ Exception -> 0x011a }
            goto L_0x011a
        L_0x006a:
            java.lang.Object[] r0 = new java.lang.Object[r13]     // Catch:{ Exception -> 0x011a }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r4)     // Catch:{ Exception -> 0x011a }
            r0[r16] = r2     // Catch:{ Exception -> 0x011a }
            r0[r15] = r3     // Catch:{ Exception -> 0x011a }
            java.lang.String r11 = io.dcloud.common.util.StringUtil.format(r5, r0)     // Catch:{ Exception -> 0x011a }
            r10 = r1[r16]     // Catch:{ Exception -> 0x011a }
            int r12 = io.dcloud.common.util.JSUtil.ERROR     // Catch:{ Exception -> 0x011a }
            r13 = 1
            r14 = 0
            r9 = r20
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r9, r10, r11, r12, r13, r14)     // Catch:{ Exception -> 0x011a }
            goto L_0x011a
        L_0x0085:
            java.lang.String r2 = "watchPosition"
            boolean r2 = r0.equals(r2)     // Catch:{ Exception -> 0x011a }
            if (r2 == 0) goto L_0x010d
            r0 = r1[r13]     // Catch:{ Exception -> 0x011a }
            boolean r17 = java.lang.Boolean.parseBoolean(r0)     // Catch:{ Exception -> 0x011a }
            io.dcloud.common.DHInterface.IFrameView r0 = r20.obtainFrameView()     // Catch:{ Exception -> 0x011a }
            io.dcloud.js.geolocation.system.LocalGeoManager$a r2 = new io.dcloud.js.geolocation.system.LocalGeoManager$a     // Catch:{ Exception -> 0x011a }
            r2.<init>()     // Catch:{ Exception -> 0x011a }
            r0.addFrameViewListener(r2)     // Catch:{ Exception -> 0x011a }
            r0 = r1[r12]     // Catch:{ Exception -> 0x011a }
            boolean r0 = io.dcloud.common.util.PdrUtil.isEquals(r0, r6)     // Catch:{ Exception -> 0x011a }
            if (r0 != 0) goto L_0x00b3
            r0 = r1[r12]     // Catch:{ Exception -> 0x011a }
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r0)     // Catch:{ Exception -> 0x011a }
            if (r0 == 0) goto L_0x00b1
            goto L_0x00b3
        L_0x00b1:
            r0 = 0
            goto L_0x00b4
        L_0x00b3:
            r0 = 1
        L_0x00b4:
            int r2 = r1.length     // Catch:{ Exception -> 0x011a }
            if (r2 <= r11) goto L_0x00ba
            r2 = r1[r9]     // Catch:{ Exception -> 0x011a }
            goto L_0x00bb
        L_0x00ba:
            r2 = r14
        L_0x00bb:
            boolean r6 = r14.equals(r2)     // Catch:{ Exception -> 0x011a }
            if (r6 != 0) goto L_0x00c6
            int r2 = java.lang.Integer.parseInt(r2)     // Catch:{ Exception -> 0x011a }
            r10 = r2
        L_0x00c6:
            int r2 = r1.length     // Catch:{ Exception -> 0x011a }
            r6 = 8
            if (r2 <= r6) goto L_0x00ce
            r2 = r1[r11]     // Catch:{ Exception -> 0x011a }
            goto L_0x00d0
        L_0x00ce:
            java.lang.String r2 = "5000"
        L_0x00d0:
            r6 = 5000(0x1388, float:7.006E-42)
            boolean r9 = r2.equals(r14)     // Catch:{ Exception -> 0x011a }
            if (r9 != 0) goto L_0x00dc
            int r6 = java.lang.Integer.parseInt(r2)     // Catch:{ Exception -> 0x011a }
        L_0x00dc:
            r2 = 1000(0x3e8, float:1.401E-42)
            if (r6 >= r2) goto L_0x00e2
            r6 = 1000(0x3e8, float:1.401E-42)
        L_0x00e2:
            if (r0 == 0) goto L_0x00f3
            r2 = r1[r16]     // Catch:{ Exception -> 0x011a }
            r3 = r1[r15]     // Catch:{ Exception -> 0x011a }
            r0 = r19
            r1 = r20
            r4 = r17
            r5 = r10
            r0.start(r1, r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x011a }
            goto L_0x011a
        L_0x00f3:
            java.lang.Object[] r0 = new java.lang.Object[r13]     // Catch:{ Exception -> 0x011a }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r4)     // Catch:{ Exception -> 0x011a }
            r0[r16] = r2     // Catch:{ Exception -> 0x011a }
            r0[r15] = r3     // Catch:{ Exception -> 0x011a }
            java.lang.String r11 = io.dcloud.common.util.StringUtil.format(r5, r0)     // Catch:{ Exception -> 0x011a }
            r10 = r1[r16]     // Catch:{ Exception -> 0x011a }
            int r12 = io.dcloud.common.util.JSUtil.ERROR     // Catch:{ Exception -> 0x011a }
            r13 = 1
            r14 = 0
            r9 = r20
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r9, r10, r11, r12, r13, r14)     // Catch:{ Exception -> 0x011a }
            goto L_0x011a
        L_0x010d:
            java.lang.String r2 = "clearWatch"
            boolean r0 = r0.equals(r2)     // Catch:{ Exception -> 0x011a }
            if (r0 == 0) goto L_0x011a
            r0 = r1[r16]     // Catch:{ Exception -> 0x011a }
            r7.stop(r0)     // Catch:{ Exception -> 0x011a }
        L_0x011a:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.js.geolocation.system.LocalGeoManager.execute(io.dcloud.common.DHInterface.IWebview, java.lang.String, java.lang.String[]):java.lang.String");
    }

    public void getCurrentLocation(IWebview iWebview, String str, boolean z, int i, int i2) {
        b().a(iWebview, i, str, i2);
    }

    public void onDestroy() {
        a aVar = this.a;
        if (aVar != null) {
            aVar.a();
        }
        this.a = null;
    }

    public void start(IWebview iWebview, String str, String str2, boolean z, int i, int i2) {
        if (b().b(iWebview, i2, str, i)) {
            this.keySet.add(str2);
        }
    }

    public void stop(String str) {
        if (this.a != null && this.keySet.contains(str)) {
            this.keySet.remove(str);
            this.a.c(a.f);
        }
    }
}
