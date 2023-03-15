package io.dcloud.e.b;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.util.ThreadPool;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

class d {
    AbsMgr a = null;
    ConcurrentHashMap<String, c> b = new ConcurrentHashMap<>();
    ArrayList<c> c = new ArrayList<>();

    class a implements MessageHandler.IMessages {
        final /* synthetic */ c a;

        /* renamed from: io.dcloud.e.b.d$a$a  reason: collision with other inner class name */
        class C0028a implements ICallBack {
            C0028a() {
            }

            public Object onCallBack(int i, Object obj) {
                if (i == -1) {
                    Log.i("console", "nativeApp pull fail");
                } else if (i == 1) {
                    Log.i("console", "nativeApp pull success");
                }
                d.this.b.clear();
                d.this.a();
                return null;
            }
        }

        class b implements Runnable {
            final /* synthetic */ String a;

            b(String str) {
                this.a = str;
            }

            public void run() {
                if (DHFile.delete(this.a)) {
                    Log.i("console", "rm file success");
                } else {
                    Log.i("console", "rm file fail");
                }
                d.this.b.clear();
                d.this.a();
            }
        }

        a(c cVar) {
            this.a = cVar;
        }

        public void execute(Object obj) {
            String str = this.a.a;
            str.hashCode();
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -1335458389:
                    if (str.equals("delete")) {
                        c = 0;
                        break;
                    }
                    break;
                case -907685685:
                    if (str.equals("script")) {
                        c = 1;
                        break;
                    }
                    break;
                case -838846263:
                    if (str.equals("update")) {
                        c = 2;
                        break;
                    }
                    break;
                case 3452485:
                    if (str.equals("pull")) {
                        c = 3;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    String str2 = this.a.b;
                    if (new File(str2).exists()) {
                        ThreadPool.self().addSingleThreadTask(new b(str2));
                        return;
                    }
                    Log.i("console", "rm file fail");
                    d.this.b.clear();
                    d.this.a();
                    return;
                case 1:
                    String str3 = this.a.b;
                    if ("restart".equals(str3)) {
                        d.this.a.processEvent(IMgr.MgrType.AppMgr, 3, "snc:CID");
                    } else if (AbsoluteConst.JSON_KEY_DEBUG_REFRESH.equals(str3)) {
                        d.this.a.processEvent(IMgr.MgrType.AppMgr, 27, (Object) null);
                    } else if ("restartAndRun".equals(str3)) {
                        d.this.a.getContext().startActivity(Intent.makeRestartActivityTask(d.this.a.getContext().getPackageManager().getLaunchIntentForPackage(d.this.a.getContext().getPackageName()).getComponent()));
                        Runtime.getRuntime().exit(0);
                    }
                    d.this.b.clear();
                    d.this.a();
                    return;
                case 2:
                    String str4 = this.a.b;
                    if ("all".equals(str4)) {
                        d.this.a.processEvent(IMgr.MgrType.WindowMgr, 13, (Object) null);
                    } else if ("current".equals(str4)) {
                        d.this.a.processEvent(IMgr.MgrType.WindowMgr, 12, (Object) null);
                    } else {
                        d.this.a.processEvent(IMgr.MgrType.WindowMgr, 14, str4);
                    }
                    d.this.b.clear();
                    d.this.a();
                    return;
                case 3:
                    String str5 = this.a.b;
                    if (!TextUtils.isEmpty(str5)) {
                        d.this.a(str5, new C0028a());
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    class b implements Runnable {
        final /* synthetic */ String a;
        final /* synthetic */ String b;
        final /* synthetic */ File c;
        final /* synthetic */ ICallBack d;

        b(String str, String str2, File file, ICallBack iCallBack) {
            this.a = str;
            this.b = str2;
            this.c = file;
            this.d = iCallBack;
        }

        public void run() {
            int copyFile = DHFile.copyFile(this.a, this.b);
            DHFile.delete(this.c.getParent());
            if (copyFile == 1) {
                ICallBack iCallBack = this.d;
                if (iCallBack != null) {
                    iCallBack.onCallBack(1, (Object) null);
                    return;
                }
                return;
            }
            ICallBack iCallBack2 = this.d;
            if (iCallBack2 != null) {
                iCallBack2.onCallBack(-1, (Object) null);
            }
        }
    }

    class c {
        String a;
        String b;

        c() {
        }
    }

    d(AbsMgr absMgr) {
        this.a = absMgr;
    }

    private ArrayList<c> b(String str) {
        ArrayList<c> arrayList = new ArrayList<>(1);
        if (str.startsWith(AbsoluteConst.SOCKET_NATIVE_COMMAND)) {
            str = str.substring(4);
        }
        String trim = str.trim();
        int length = trim.length();
        ArrayList arrayList2 = new ArrayList();
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (i2 < length) {
            char charAt = trim.charAt(i2);
            i2++;
            if (i2 == length || ((b(charAt) && arrayList2.size() % 2 == 0) || a(charAt))) {
                String trim2 = trim.substring(i3, i2).trim();
                if (!"".equals(trim2)) {
                    arrayList2.add(trim2);
                    i3 = i2;
                }
            }
        }
        int size = arrayList2.size();
        while (i < size) {
            c cVar = new c();
            cVar.a = (String) arrayList2.get(i);
            cVar.b = (String) arrayList2.get(i + 1);
            i += 2;
            arrayList.add(cVar);
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public synchronized void a(String str) {
        ArrayList<c> b2 = b(str);
        if (b2 != null && !b2.isEmpty()) {
            this.c.addAll(b2);
        }
        a();
    }

    /* access modifiers changed from: package-private */
    public boolean a(char c2) {
        return c2 == 13 || c2 == 10;
    }

    /* access modifiers changed from: package-private */
    public boolean b(char c2) {
        return c2 == 9 || c2 == 11 || c2 == 12 || c2 == ' ' || c2 == 160 || c2 == 12288;
    }

    public synchronized void a() {
        ArrayList<c> arrayList;
        c remove;
        if (this.b.isEmpty() && (arrayList = this.c) != null && !arrayList.isEmpty() && (remove = this.c.remove(0)) != null) {
            this.b.put("runing", remove);
            a(remove);
        }
    }

    public void a(c cVar) {
        MessageHandler.sendMessage(new a(cVar), (Object) null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0023, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0088, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x008a, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void a(java.lang.String r11, io.dcloud.common.DHInterface.ICallBack r12) {
        /*
            r10 = this;
            monitor-enter(r10)
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r11)     // Catch:{ all -> 0x008b }
            if (r0 != 0) goto L_0x0089
            r0 = 0
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ JSONException -> 0x000e }
            r1.<init>(r11)     // Catch:{ JSONException -> 0x000e }
            goto L_0x0013
        L_0x000e:
            r11 = move-exception
            r11.printStackTrace()     // Catch:{ all -> 0x008b }
            r1 = r0
        L_0x0013:
            r11 = -1
            if (r1 != 0) goto L_0x0024
            java.lang.String r1 = "console"
            java.lang.String r2 = "nativeApp pull fail"
            android.util.Log.i(r1, r2)     // Catch:{ all -> 0x008b }
            if (r12 == 0) goto L_0x0022
            r12.onCallBack(r11, r0)     // Catch:{ all -> 0x008b }
        L_0x0022:
            monitor-exit(r10)
            return
        L_0x0024:
            java.lang.String r2 = "appid"
            java.lang.String r2 = r1.optString(r2)     // Catch:{ all -> 0x008b }
            java.lang.String r3 = "filePath"
            java.lang.String r6 = r1.optString(r3)     // Catch:{ all -> 0x008b }
            boolean r1 = android.text.TextUtils.isEmpty(r2)     // Catch:{ all -> 0x008b }
            if (r1 != 0) goto L_0x007b
            boolean r1 = android.text.TextUtils.isEmpty(r6)     // Catch:{ all -> 0x008b }
            if (r1 == 0) goto L_0x003d
            goto L_0x007b
        L_0x003d:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x008b }
            r1.<init>()     // Catch:{ all -> 0x008b }
            java.lang.String r3 = io.dcloud.common.util.BaseInfo.sBaseFsAppsPath     // Catch:{ all -> 0x008b }
            r1.append(r3)     // Catch:{ all -> 0x008b }
            r1.append(r2)     // Catch:{ all -> 0x008b }
            java.lang.String r2 = "/www"
            r1.append(r2)     // Catch:{ all -> 0x008b }
            java.lang.String r7 = r1.toString()     // Catch:{ all -> 0x008b }
            java.io.File r8 = new java.io.File     // Catch:{ all -> 0x008b }
            r8.<init>(r6)     // Catch:{ all -> 0x008b }
            boolean r1 = r8.exists()     // Catch:{ all -> 0x008b }
            if (r1 == 0) goto L_0x006e
            io.dcloud.common.util.ThreadPool r11 = io.dcloud.common.util.ThreadPool.self()     // Catch:{ all -> 0x008b }
            io.dcloud.e.b.d$b r0 = new io.dcloud.e.b.d$b     // Catch:{ all -> 0x008b }
            r4 = r0
            r5 = r10
            r9 = r12
            r4.<init>(r6, r7, r8, r9)     // Catch:{ all -> 0x008b }
            r11.addSingleThreadTask(r0)     // Catch:{ all -> 0x008b }
            goto L_0x0089
        L_0x006e:
            java.lang.String r1 = "console"
            java.lang.String r2 = "nativeApp pull fail"
            android.util.Log.i(r1, r2)     // Catch:{ all -> 0x008b }
            if (r12 == 0) goto L_0x0089
            r12.onCallBack(r11, r0)     // Catch:{ all -> 0x008b }
            goto L_0x0089
        L_0x007b:
            java.lang.String r1 = "console"
            java.lang.String r2 = "nativeApp pull fail"
            android.util.Log.i(r1, r2)     // Catch:{ all -> 0x008b }
            if (r12 == 0) goto L_0x0087
            r12.onCallBack(r11, r0)     // Catch:{ all -> 0x008b }
        L_0x0087:
            monitor-exit(r10)
            return
        L_0x0089:
            monitor-exit(r10)
            return
        L_0x008b:
            r11 = move-exception
            monitor-exit(r10)
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.e.b.d.a(java.lang.String, io.dcloud.common.DHInterface.ICallBack):void");
    }
}
