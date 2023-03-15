package io.dcloud.feature.ui;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.taobao.weex.common.Constants;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.IContainerView;
import io.dcloud.common.DHInterface.IEventCallback;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.ITitleNView;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.ui.ReceiveJSValue;
import io.dcloud.common.adapter.ui.webview.WebResUtil;
import io.dcloud.common.adapter.util.AnimOptions;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import io.dcloud.common.util.TestUtil;
import io.dcloud.common.util.ThreadPool;
import io.dcloud.nineoldandroids.view.ViewHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class c extends b implements IEventCallback {
    private static final HashMap<String, String> u;
    String A;
    int B;
    Object C;
    boolean D;
    boolean E;
    int F;
    boolean G;
    boolean H;
    boolean I;
    boolean J;
    boolean K;
    boolean L;
    boolean M;
    protected ArrayList<b> N;
    String O;
    String P;
    IWebview Q;
    String R;
    IWebview S;
    String T;
    IWebview U;
    String V;
    private boolean W;
    c X;
    private ArrayList<c> Y;
    private boolean Z;
    /* access modifiers changed from: private */
    public String a0;
    private int b0;
    private boolean c0;
    Runnable d0;
    long v;
    JSONArray w;
    IWebview x;
    JSONObject y;
    IFrameView z;

    class a implements Runnable {
        a() {
        }

        public void run() {
            if (c.this.z.obtainWebView().checkWhite(c.this.a0)) {
                c.this.l();
            } else {
                c.this.a(AbsoluteConst.EVENTS_WEBVIEW_RENDERED, "{}", false);
            }
            c.this.d0 = null;
        }
    }

    class b implements ReceiveJSValue.ReceiveJSValueCallback {
        final /* synthetic */ IWebview a;
        final /* synthetic */ String b;

        b(IWebview iWebview, String str) {
            this.a = iWebview;
            this.b = str;
        }

        public String callback(JSONArray jSONArray) {
            Object obj;
            JSONArray jSONArray2 = jSONArray;
            String string = JSONUtil.getString(jSONArray2, 0);
            try {
                obj = jSONArray2.get(1);
            } catch (JSONException unused) {
                obj = null;
            }
            if ((obj instanceof String) || "string".equals(string)) {
                Deprecated_JSUtil.execCallback(this.a, this.b, String.valueOf(obj), JSUtil.OK, false, false);
            } else if (obj instanceof JSONArray) {
                Deprecated_JSUtil.execCallback(this.a, this.b, obj.toString(), JSUtil.OK, true, false);
            } else if ((obj instanceof JSONObject) || "object".equals(string)) {
                Deprecated_JSUtil.execCallback(this.a, this.b, obj.toString(), JSUtil.OK, true, false);
            } else if (Constants.Name.UNDEFINED.equals(string)) {
                Deprecated_JSUtil.execCallback(this.a, this.b, Constants.Name.UNDEFINED, JSUtil.OK, true, false);
            } else {
                Deprecated_JSUtil.execCallback(this.a, this.b, obj.toString(), JSUtil.OK, true, false);
            }
            return null;
        }
    }

    /* renamed from: io.dcloud.feature.ui.c$c  reason: collision with other inner class name */
    class C0043c implements Runnable {
        final /* synthetic */ IWebview a;
        final /* synthetic */ String b;

        C0043c(IWebview iWebview, String str) {
            this.a = iWebview;
            this.b = str;
        }

        public void run() {
            this.a.evalJSSync(this.b, (ICallBack) null);
        }
    }

    class d implements MessageHandler.UncheckedCallable {
        final /* synthetic */ IWebview a;
        final /* synthetic */ String b;

        class a implements ICallBack {
            final /* synthetic */ MessageHandler.WaitableRunnable a;

            a(MessageHandler.WaitableRunnable waitableRunnable) {
                this.a = waitableRunnable;
            }

            public Object onCallBack(int i, Object obj) {
                MessageHandler.WaitableRunnable waitableRunnable = this.a;
                if (waitableRunnable == null) {
                    return null;
                }
                waitableRunnable.callBack(obj);
                return null;
            }
        }

        d(IWebview iWebview, String str) {
            this.a = iWebview;
            this.b = str;
        }

        public void run(MessageHandler.WaitableRunnable waitableRunnable) {
            try {
                this.a.evalJSSync(this.b, new a(waitableRunnable));
            } catch (Exception e) {
                e.printStackTrace();
                if (waitableRunnable != null) {
                    waitableRunnable.callBack("");
                }
            }
        }
    }

    class e implements Runnable {
        final /* synthetic */ IWebview a;
        final /* synthetic */ String b;
        final /* synthetic */ IWebview c;
        final /* synthetic */ String d;

        e(IWebview iWebview, String str, IWebview iWebview2, String str2) {
            this.a = iWebview;
            this.b = str;
            this.c = iWebview2;
            this.d = str2;
        }

        public void run() {
            try {
                boolean checkWhite = this.a.checkWhite(this.b);
                IWebview iWebview = this.c;
                String str = this.d;
                StringBuilder sb = new StringBuilder();
                sb.append("{\"code\":100,\"rendered\":");
                sb.append(!checkWhite);
                sb.append(Operators.BLOCK_END_STR);
                Deprecated_JSUtil.execCallback(iWebview, str, sb.toString(), JSUtil.OK, true, false);
            } catch (Exception unused) {
                if (c.this.a() != null) {
                    IWebview iWebview2 = this.c;
                    String str2 = this.d;
                    Deprecated_JSUtil.execCallback(iWebview2, str2, "{\"code\":-100,\"message\":\"" + c.this.a().getString(R.string.dcloud_common_screenshot_fail) + "\"}", JSUtil.ERROR, true, false);
                }
            }
        }
    }

    class f implements ICallBack {
        final /* synthetic */ IWebview a;
        final /* synthetic */ String b;

        f(IWebview iWebview, String str) {
            this.a = iWebview;
            this.b = str;
        }

        public Object onCallBack(int i, Object obj) {
            Deprecated_JSUtil.execCallback(this.a, this.b, (String) null, JSUtil.OK, false, false);
            return null;
        }
    }

    class g implements ICallBack {
        final /* synthetic */ IWebview a;
        final /* synthetic */ String b;

        g(IWebview iWebview, String str) {
            this.a = iWebview;
            this.b = str;
        }

        public Object onCallBack(int i, Object obj) {
            IWebview iWebview = this.a;
            String str = this.b;
            Deprecated_JSUtil.execCallback(iWebview, str, "{\"code\":-100,\"message\":\"" + this.a.getContext().getString(R.string.dcloud_common_screenshot_fail) + "\"}", JSUtil.ERROR, true, false);
            return null;
        }
    }

    class h implements ICallBack {
        final /* synthetic */ IWebview a;
        final /* synthetic */ String b;

        h(IWebview iWebview, String str) {
            this.a = iWebview;
            this.b = str;
        }

        public Object onCallBack(int i, Object obj) {
            Deprecated_JSUtil.execCallback(this.a, this.b, (String) null, JSUtil.OK, false, false);
            return null;
        }
    }

    class i implements ICallBack {
        final /* synthetic */ IWebview a;
        final /* synthetic */ String b;

        i(IWebview iWebview, String str) {
            this.a = iWebview;
            this.b = str;
        }

        public Object onCallBack(int i, Object obj) {
            IWebview iWebview = this.a;
            String str = this.b;
            StringBuilder sb = new StringBuilder();
            sb.append("{\"code\":");
            sb.append(i);
            sb.append(",\"message\":\"");
            sb.append(obj != null ? obj.toString() : c.this.a().getString(R.string.dcloud_common_screenshot_fail));
            sb.append("\"}");
            Deprecated_JSUtil.execCallback(iWebview, str, sb.toString(), JSUtil.ERROR, true, false);
            return null;
        }
    }

    class j implements Runnable {
        final /* synthetic */ String a;
        final /* synthetic */ String b;

        j(String str, String str2) {
            this.a = str;
            this.b = str2;
        }

        public void run() {
            TestUtil.PointTime.commitTid(c.this.a(), this.a, c.this.n, this.b, 10);
        }
    }

    static {
        HashMap<String, String> hashMap = new HashMap<>();
        u = hashMap;
        hashMap.put(AbsoluteConst.EVENTS_CLOSE, "onclose");
        hashMap.put("loading", "onloading");
        hashMap.put(AbsoluteConst.EVENTS_FAILED, "onerror");
        hashMap.put(AbsoluteConst.EVENTS_LOADED, "onloaded");
    }

    c(a aVar, String str, String str2, String str3, JSONObject jSONObject) {
        this(aVar, (IFrameView) null, str, str2, str3, jSONObject);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private io.dcloud.common.DHInterface.INativeBitmap g(io.dcloud.common.DHInterface.IWebview r8, java.lang.String r9) {
        /*
            r7 = this;
            io.dcloud.common.DHInterface.IApp r0 = r8.obtainApp()
            io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r2 = 4
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r3 = 0
            r2[r3] = r8
            java.lang.String r4 = "nativeobj"
            r5 = 1
            r2[r5] = r4
            java.lang.String r4 = "getNativeBitmap"
            r6 = 2
            r2[r6] = r4
            java.lang.String[] r4 = new java.lang.String[r6]
            io.dcloud.common.DHInterface.IApp r8 = r8.obtainApp()
            java.lang.String r8 = r8.obtainAppId()
            r4[r3] = r8
            r4[r5] = r9
            r8 = 3
            r2[r8] = r4
            r8 = 10
            java.lang.Object r8 = r0.obtainMgrData(r1, r8, r2)
            io.dcloud.common.DHInterface.INativeBitmap r8 = (io.dcloud.common.DHInterface.INativeBitmap) r8
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.c.g(io.dcloud.common.DHInterface.IWebview, java.lang.String):io.dcloud.common.DHInterface.INativeBitmap");
    }

    private void i() {
        if (this.z.getFrameType() == 6) {
            ThreadPool.self().addThreadTask(new j(this.j.f.obtainAppId(), this.j.f.obtainConfigProperty("adid")));
        }
    }

    private void j() {
        View obtainMainView = this.z.obtainMainView();
        if (obtainMainView instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) obtainMainView;
            for (int i2 = 0; i2 < viewGroup.getChildCount(); i2++) {
                View childAt = viewGroup.getChildAt(i2);
                if (childAt instanceof ITitleNView) {
                    childAt.bringToFront();
                    return;
                }
            }
        }
    }

    private boolean k() {
        c cVar = this.h;
        if (cVar == null) {
            return true;
        }
        if (!cVar.G || !cVar.k()) {
            return false;
        }
        return true;
    }

    private void s() {
        this.j.b(this);
        if (!this.J) {
            d().onDispose();
            d().dispose();
        } else if (!this.K) {
            if (this.M) {
                c cVar = this.h;
                if (cVar != null) {
                    cVar.c((b) this);
                }
                d().onDispose();
                d().dispose();
            } else {
                ((AdaFrameItem) this.z).getAnimOptions().mOption = 1;
            }
        }
        e();
    }

    private void t() {
        ((AdaFrameItem) this.z).getAnimOptions().mOption = 3;
        this.G = false;
        this.H = true;
    }

    public void a(int i2, int i3, int i4, int i5, int i6, int i7) {
    }

    public void b(boolean z2) {
        this.Z = z2;
    }

    /* access modifiers changed from: protected */
    public b c(String str) {
        ArrayList<b> arrayList = this.N;
        b bVar = null;
        if (arrayList != null && !arrayList.isEmpty()) {
            for (int size = this.N.size() - 1; size >= 0; size--) {
                bVar = this.N.get(size);
                if (PdrUtil.isEquals(str, bVar.m)) {
                    break;
                }
            }
        }
        return bVar;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Object[]} */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void d(io.dcloud.common.DHInterface.IWebview r7, java.lang.String r8) {
        /*
            r6 = this;
            io.dcloud.feature.ui.a r0 = r6.j
            io.dcloud.common.DHInterface.AbsMgr r0 = r0.d
            io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r2 = 4
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r3 = 0
            r2[r3] = r7
            java.lang.String r7 = "maps"
            r4 = 1
            r2[r4] = r7
            java.lang.String r7 = "appendToFrameView"
            r5 = 2
            r2[r5] = r7
            java.lang.Object[] r7 = new java.lang.Object[r5]
            io.dcloud.common.DHInterface.IFrameView r5 = r6.z
            r7[r3] = r5
            r7[r4] = r8
            r8 = 3
            r2[r8] = r7
            r7 = 10
            r0.processEvent(r1, r7, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.c.d(io.dcloud.common.DHInterface.IWebview, java.lang.String):void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Object[]} */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void e(io.dcloud.common.DHInterface.IWebview r7, java.lang.String r8) {
        /*
            r6 = this;
            io.dcloud.feature.ui.a r0 = r6.j
            io.dcloud.common.DHInterface.AbsMgr r0 = r0.d
            io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r2 = 4
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r3 = 0
            r2[r3] = r7
            java.lang.String r7 = "nativeobj"
            r4 = 1
            r2[r4] = r7
            java.lang.String r7 = "addNativeView"
            r5 = 2
            r2[r5] = r7
            java.lang.Object[] r7 = new java.lang.Object[r5]
            io.dcloud.common.DHInterface.IFrameView r5 = r6.z
            r7[r3] = r5
            r7[r4] = r8
            r8 = 3
            r2[r8] = r7
            r7 = 10
            r0.processEvent(r1, r7, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.c.e(io.dcloud.common.DHInterface.IWebview, java.lang.String):void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Object[]} */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void f(io.dcloud.common.DHInterface.IWebview r7, java.lang.String r8) {
        /*
            r6 = this;
            io.dcloud.feature.ui.a r0 = r6.j
            io.dcloud.common.DHInterface.AbsMgr r0 = r0.d
            io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r2 = 4
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r3 = 0
            r2[r3] = r7
            java.lang.String r7 = "videoplayer"
            r4 = 1
            r2[r4] = r7
            java.lang.String r7 = "appendToFrameView"
            r5 = 2
            r2[r5] = r7
            java.lang.Object[] r7 = new java.lang.Object[r5]
            io.dcloud.common.DHInterface.IFrameView r5 = r6.z
            r7[r3] = r5
            r7[r4] = r8
            r8 = 3
            r2[r8] = r7
            r7 = 10
            r0.processEvent(r1, r7, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.c.f(io.dcloud.common.DHInterface.IWebview, java.lang.String):void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Object[]} */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void h(io.dcloud.common.DHInterface.IWebview r7, java.lang.String r8) {
        /*
            r6 = this;
            io.dcloud.feature.ui.a r0 = r6.j
            io.dcloud.common.DHInterface.AbsMgr r0 = r0.d
            io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r2 = 4
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r3 = 0
            r2[r3] = r7
            java.lang.String r7 = "nativeobj"
            r4 = 1
            r2[r4] = r7
            java.lang.String r7 = "removeNativeView"
            r5 = 2
            r2[r5] = r7
            java.lang.Object[] r7 = new java.lang.Object[r5]
            io.dcloud.common.DHInterface.IFrameView r5 = r6.z
            r7[r3] = r5
            r7[r4] = r8
            r8 = 3
            r2[r8] = r7
            r7 = 10
            r0.processEvent(r1, r7, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.c.h(io.dcloud.common.DHInterface.IWebview, java.lang.String):void");
    }

    public void l() {
        HashMap<String, ArrayList<String[]>> hashMap = this.t;
        if (hashMap != null && hashMap.containsKey(AbsoluteConst.EVENTS_WEBVIEW_RENDERED)) {
            Runnable runnable = this.d0;
            if (runnable != null) {
                MessageHandler.removeCallbacks(runnable);
            }
            a aVar = new a();
            this.d0 = aVar;
            MessageHandler.postDelayed(aVar, (long) this.b0);
        }
    }

    public String m() {
        IWebview obtainWebView = this.z.obtainWebView();
        if (obtainWebView != null) {
            return obtainWebView.obtainFrameId();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public String n() {
        ViewOptions obtainFrameOptions = ((AdaFrameItem) this.z).obtainFrameOptions();
        return StringUtil.format("{top:%d,left:%d,width:%d,height:%d}", Integer.valueOf((int) (((float) obtainFrameOptions.top) / obtainFrameOptions.mWebviewScale)), Integer.valueOf((int) (((float) obtainFrameOptions.left) / obtainFrameOptions.mWebviewScale)), Integer.valueOf((int) (((float) obtainFrameOptions.width) / obtainFrameOptions.mWebviewScale)), Integer.valueOf((int) (((float) obtainFrameOptions.height) / obtainFrameOptions.mWebviewScale)));
    }

    public boolean o() {
        return !this.z.isWebviewCovered();
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:132:0x02c6, code lost:
        if (r14.equals(io.dcloud.common.constant.AbsoluteConst.EVENTS_WINDOW_CLOSE) == false) goto L_0x0278;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object onCallBack(java.lang.String r14, java.lang.Object r15) {
        /*
            r13 = this;
            r0 = 2
            java.lang.Object[] r1 = new java.lang.Object[r0]
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "NWindow.onCallBack pEventType="
            r2.append(r3)
            r2.append(r14)
            java.lang.String r2 = r2.toString()
            r3 = 0
            r1[r3] = r2
            r2 = 1
            r1[r2] = r15
            java.lang.String r4 = "yl"
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r4, (java.lang.Object[]) r1)
            r14.hashCode()
            int r1 = r14.hashCode()
            r4 = 8
            r5 = 7
            r6 = 6
            java.lang.String r7 = "touchstart"
            r8 = 5
            r9 = 4
            r10 = 3
            r11 = -1
            switch(r1) {
                case -2051330937: goto L_0x011b;
                case -1914861741: goto L_0x0110;
                case -1894754084: goto L_0x0105;
                case -1578593149: goto L_0x00fc;
                case -1417193575: goto L_0x00f1;
                case -1175619677: goto L_0x00e6;
                case -1102511297: goto L_0x00db;
                case -934437708: goto L_0x00d0;
                case -351937593: goto L_0x00c2;
                case -265510946: goto L_0x00b4;
                case -82823752: goto L_0x00a6;
                case 18100665: goto L_0x0098;
                case 184525206: goto L_0x008a;
                case 570172737: goto L_0x007c;
                case 1004163032: goto L_0x006e;
                case 1420796964: goto L_0x0060;
                case 1429092717: goto L_0x0052;
                case 1550673632: goto L_0x0044;
                case 1778259450: goto L_0x0036;
                default: goto L_0x0034;
            }
        L_0x0034:
            goto L_0x0126
        L_0x0036:
            java.lang.String r1 = "show_loading"
            boolean r1 = r14.equals(r1)
            if (r1 != 0) goto L_0x0040
            goto L_0x0126
        L_0x0040:
            r1 = 18
            goto L_0x0127
        L_0x0044:
            java.lang.String r1 = "titleNViewSearchInputClicked"
            boolean r1 = r14.equals(r1)
            if (r1 != 0) goto L_0x004e
            goto L_0x0126
        L_0x004e:
            r1 = 17
            goto L_0x0127
        L_0x0052:
            java.lang.String r1 = "titleNViewSearchInputChanged"
            boolean r1 = r14.equals(r1)
            if (r1 != 0) goto L_0x005c
            goto L_0x0126
        L_0x005c:
            r1 = 16
            goto L_0x0127
        L_0x0060:
            java.lang.String r1 = "slide_webview_close"
            boolean r1 = r14.equals(r1)
            if (r1 != 0) goto L_0x006a
            goto L_0x0126
        L_0x006a:
            r1 = 15
            goto L_0x0127
        L_0x006e:
            java.lang.String r1 = "titleNViewSearchInputConfirmed"
            boolean r1 = r14.equals(r1)
            if (r1 != 0) goto L_0x0078
            goto L_0x0126
        L_0x0078:
            r1 = 14
            goto L_0x0127
        L_0x007c:
            java.lang.String r1 = "titleUpdate"
            boolean r1 = r14.equals(r1)
            if (r1 != 0) goto L_0x0086
            goto L_0x0126
        L_0x0086:
            r1 = 13
            goto L_0x0127
        L_0x008a:
            java.lang.String r1 = "slide_webview_hide"
            boolean r1 = r14.equals(r1)
            if (r1 != 0) goto L_0x0094
            goto L_0x0126
        L_0x0094:
            r1 = 12
            goto L_0x0127
        L_0x0098:
            java.lang.String r1 = "overrideUrlLoading"
            boolean r1 = r14.equals(r1)
            if (r1 != 0) goto L_0x00a2
            goto L_0x0126
        L_0x00a2:
            r1 = 11
            goto L_0x0127
        L_0x00a6:
            java.lang.String r1 = "popGesture"
            boolean r1 = r14.equals(r1)
            if (r1 != 0) goto L_0x00b0
            goto L_0x0126
        L_0x00b0:
            r1 = 10
            goto L_0x0127
        L_0x00b4:
            java.lang.String r1 = "show_animation_end"
            boolean r1 = r14.equals(r1)
            if (r1 != 0) goto L_0x00be
            goto L_0x0126
        L_0x00be:
            r1 = 9
            goto L_0x0127
        L_0x00c2:
            java.lang.String r1 = "progressChanged"
            boolean r1 = r14.equals(r1)
            if (r1 != 0) goto L_0x00cc
            goto L_0x0126
        L_0x00cc:
            r1 = 8
            goto L_0x0127
        L_0x00d0:
            java.lang.String r1 = "resize"
            boolean r1 = r14.equals(r1)
            if (r1 != 0) goto L_0x00d9
            goto L_0x0126
        L_0x00d9:
            r1 = 7
            goto L_0x0127
        L_0x00db:
            java.lang.String r1 = "hide_loading"
            boolean r1 = r14.equals(r1)
            if (r1 != 0) goto L_0x00e4
            goto L_0x0126
        L_0x00e4:
            r1 = 6
            goto L_0x0127
        L_0x00e6:
            java.lang.String r1 = "titleNViewSearchInputFocusChanged"
            boolean r1 = r14.equals(r1)
            if (r1 != 0) goto L_0x00ef
            goto L_0x0126
        L_0x00ef:
            r1 = 5
            goto L_0x0127
        L_0x00f1:
            java.lang.String r1 = "slideBounce"
            boolean r1 = r14.equals(r1)
            if (r1 != 0) goto L_0x00fa
            goto L_0x0126
        L_0x00fa:
            r1 = 4
            goto L_0x0127
        L_0x00fc:
            boolean r1 = r14.equals(r7)
            if (r1 != 0) goto L_0x0103
            goto L_0x0126
        L_0x0103:
            r1 = 3
            goto L_0x0127
        L_0x0105:
            java.lang.String r1 = "dragBounce"
            boolean r1 = r14.equals(r1)
            if (r1 != 0) goto L_0x010e
            goto L_0x0126
        L_0x010e:
            r1 = 2
            goto L_0x0127
        L_0x0110:
            java.lang.String r1 = "onresize"
            boolean r1 = r14.equals(r1)
            if (r1 != 0) goto L_0x0119
            goto L_0x0126
        L_0x0119:
            r1 = 1
            goto L_0x0127
        L_0x011b:
            java.lang.String r1 = "listenResourceLoading"
            boolean r1 = r14.equals(r1)
            if (r1 != 0) goto L_0x0124
            goto L_0x0126
        L_0x0124:
            r1 = 0
            goto L_0x0127
        L_0x0126:
            r1 = -1
        L_0x0127:
            r12 = 0
            switch(r1) {
                case 0: goto L_0x025e;
                case 1: goto L_0x0259;
                case 2: goto L_0x0248;
                case 3: goto L_0x022d;
                case 4: goto L_0x0248;
                case 5: goto L_0x0248;
                case 6: goto L_0x0226;
                case 7: goto L_0x0248;
                case 8: goto L_0x01f2;
                case 9: goto L_0x01c8;
                case 10: goto L_0x0198;
                case 11: goto L_0x0188;
                case 12: goto L_0x0183;
                case 13: goto L_0x0163;
                case 14: goto L_0x0248;
                case 15: goto L_0x015e;
                case 16: goto L_0x0248;
                case 17: goto L_0x014d;
                case 18: goto L_0x0142;
                default: goto L_0x012b;
            }
        L_0x012b:
            java.util.HashMap<java.lang.String, java.lang.String> r1 = u
            java.lang.Object r1 = r1.get(r14)
            java.lang.String r1 = (java.lang.String) r1
            boolean r7 = io.dcloud.common.util.PdrUtil.isEmpty(r1)
            if (r7 != 0) goto L_0x026e
            io.dcloud.feature.ui.a r7 = r13.j
            java.util.List<io.dcloud.feature.ui.c> r7 = r7.c
            a((java.lang.String) r1, (java.lang.Object) r15, (java.util.List<io.dcloud.feature.ui.c>) r7, (io.dcloud.feature.ui.c) r13)
            goto L_0x026e
        L_0x0142:
            boolean r14 = r13.J
            if (r14 == 0) goto L_0x0360
            io.dcloud.feature.ui.a r14 = r13.j
            r14.f(r13)
            goto L_0x0360
        L_0x014d:
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r15)
            if (r0 == 0) goto L_0x0155
            r15 = r12
            goto L_0x0159
        L_0x0155:
            java.lang.String r15 = java.lang.String.valueOf(r15)
        L_0x0159:
            r13.a((java.lang.String) r14, (java.lang.String) r15)
            goto L_0x0360
        L_0x015e:
            r13.s()
            goto L_0x0360
        L_0x0163:
            java.lang.Object[] r0 = new java.lang.Object[r2]
            if (r15 != 0) goto L_0x016a
            java.lang.String r15 = "''"
            goto L_0x0172
        L_0x016a:
            java.lang.String r15 = r15.toString()
            java.lang.String r15 = org.json.JSONObject.quote(r15)
        L_0x0172:
            r0[r3] = r15
            java.lang.String r15 = "{title:%s}"
            java.lang.String r15 = io.dcloud.common.util.StringUtil.format(r15, r0)
            r13.a((java.lang.String) r14, (java.lang.String) r15, (boolean) r3)
            r13.l()
            goto L_0x0360
        L_0x0183:
            r13.t()
            goto L_0x0360
        L_0x0188:
            io.dcloud.common.DHInterface.IWebview r4 = r13.S
            java.lang.String r5 = r13.R
            r6 = r15
            java.lang.String r6 = (java.lang.String) r6
            int r7 = io.dcloud.common.util.JSUtil.OK
            r8 = 1
            r9 = 1
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r4, r5, r6, r7, r8, r9)
            goto L_0x0360
        L_0x0198:
            java.lang.Object[] r15 = (java.lang.Object[]) r15
            r1 = r15[r3]
            java.lang.String r1 = (java.lang.String) r1
            r4 = r15[r2]
            r15 = r15[r0]
            io.dcloud.common.DHInterface.IFrameView r15 = (io.dcloud.common.DHInterface.IFrameView) r15
            io.dcloud.feature.ui.a r5 = r13.j
            io.dcloud.feature.ui.c r15 = r5.a((io.dcloud.common.DHInterface.IFrameView) r15)
            java.lang.Object[] r5 = new java.lang.Object[r8]
            r5[r3] = r1
            r5[r2] = r4
            java.lang.String r1 = r15.l
            r5[r0] = r1
            java.lang.String r0 = r15.m
            r5[r10] = r0
            org.json.JSONObject r15 = r15.y
            r5[r9] = r15
            java.lang.String r15 = "{type:'%s', result:%s, private_args:{uuid:'%s',id:'%s',extras:'%s'}}"
            java.lang.String r15 = io.dcloud.common.util.StringUtil.format(r15, r5)
            r13.a((java.lang.String) r14, (java.lang.String) r15, (boolean) r3)
            goto L_0x0360
        L_0x01c8:
            java.lang.String r14 = r13.P
            boolean r14 = io.dcloud.common.util.PdrUtil.isEmpty(r14)
            if (r14 != 0) goto L_0x01df
            io.dcloud.common.DHInterface.IWebview r0 = r13.Q
            if (r0 == 0) goto L_0x01df
            java.lang.String r1 = r13.P
            int r3 = io.dcloud.common.util.JSUtil.OK
            r4 = 0
            r5 = 0
            java.lang.String r2 = ""
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r0, r1, r2, r3, r4, r5)
        L_0x01df:
            boolean r14 = io.dcloud.common.util.PdrUtil.isEmpty(r15)
            if (r14 == 0) goto L_0x01e7
            r14 = r12
            goto L_0x01eb
        L_0x01e7:
            java.lang.String r14 = java.lang.String.valueOf(r15)
        L_0x01eb:
            java.lang.String r15 = "show"
            r13.a((java.lang.String) r15, (java.lang.String) r14)
            goto L_0x0360
        L_0x01f2:
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r0[r3] = r15
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            java.lang.String r1 = "plus.webview._find__Window_By_UUID__('"
            r15.append(r1)
            java.lang.String r1 = r13.m
            r15.append(r1)
            java.lang.String r1 = "','"
            r15.append(r1)
            java.lang.String r1 = r13.l
            r15.append(r1)
            java.lang.String r1 = "')"
            r15.append(r1)
            java.lang.String r15 = r15.toString()
            r0[r2] = r15
            java.lang.String r15 = "{progress:%s,target:%s}"
            java.lang.String r15 = io.dcloud.common.util.StringUtil.format(r15, r0)
            r13.a((java.lang.String) r14, (java.lang.String) r15, (boolean) r3)
            goto L_0x0360
        L_0x0226:
            io.dcloud.feature.ui.a r14 = r13.j
            r14.d(r13)
            goto L_0x0360
        L_0x022d:
            java.util.HashMap<java.lang.String, java.util.ArrayList<java.lang.String[]>> r0 = r13.t
            if (r0 == 0) goto L_0x0360
            boolean r0 = r0.containsKey(r7)
            if (r0 == 0) goto L_0x0360
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r15)
            if (r0 == 0) goto L_0x023f
            r15 = r12
            goto L_0x0243
        L_0x023f:
            java.lang.String r15 = java.lang.String.valueOf(r15)
        L_0x0243:
            r13.a((java.lang.String) r14, (java.lang.String) r15)
            goto L_0x0360
        L_0x0248:
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r15)
            if (r0 == 0) goto L_0x0250
            r15 = r12
            goto L_0x0254
        L_0x0250:
            java.lang.String r15 = java.lang.String.valueOf(r15)
        L_0x0254:
            r13.a((java.lang.String) r14, (java.lang.String) r15, (boolean) r3)
            goto L_0x0360
        L_0x0259:
            r13.q()
            goto L_0x0360
        L_0x025e:
            io.dcloud.common.DHInterface.IWebview r4 = r13.U
            java.lang.String r5 = r13.T
            r6 = r15
            java.lang.String r6 = (java.lang.String) r6
            int r7 = io.dcloud.common.util.JSUtil.OK
            r8 = 1
            r9 = 1
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r4, r5, r6, r7, r8, r9)
            goto L_0x0360
        L_0x026e:
            r14.hashCode()
            int r1 = r14.hashCode()
            switch(r1) {
                case -1183076191: goto L_0x02d4;
                case -1097519099: goto L_0x02c9;
                case -787126039: goto L_0x02bf;
                case -732864766: goto L_0x02b3;
                case 3202370: goto L_0x02a8;
                case 94756344: goto L_0x029d;
                case 301767451: goto L_0x0292;
                case 336650556: goto L_0x0287;
                case 1839654540: goto L_0x027b;
                default: goto L_0x0278;
            }
        L_0x0278:
            r0 = -1
            goto L_0x02df
        L_0x027b:
            java.lang.String r0 = "rendering"
            boolean r0 = r14.equals(r0)
            if (r0 != 0) goto L_0x0284
            goto L_0x0278
        L_0x0284:
            r0 = 8
            goto L_0x02df
        L_0x0287:
            java.lang.String r0 = "loading"
            boolean r0 = r14.equals(r0)
            if (r0 != 0) goto L_0x0290
            goto L_0x0278
        L_0x0290:
            r0 = 7
            goto L_0x02df
        L_0x0292:
            java.lang.String r0 = "pullToRefresh"
            boolean r0 = r14.equals(r0)
            if (r0 != 0) goto L_0x029b
            goto L_0x0278
        L_0x029b:
            r0 = 6
            goto L_0x02df
        L_0x029d:
            java.lang.String r0 = "close"
            boolean r0 = r14.equals(r0)
            if (r0 != 0) goto L_0x02a6
            goto L_0x0278
        L_0x02a6:
            r0 = 5
            goto L_0x02df
        L_0x02a8:
            java.lang.String r0 = "hide"
            boolean r0 = r14.equals(r0)
            if (r0 != 0) goto L_0x02b1
            goto L_0x0278
        L_0x02b1:
            r0 = 4
            goto L_0x02df
        L_0x02b3:
            java.lang.String r0 = "webPause"
            boolean r0 = r14.equals(r0)
            if (r0 != 0) goto L_0x02bd
            goto L_0x0278
        L_0x02bd:
            r0 = 3
            goto L_0x02df
        L_0x02bf:
            java.lang.String r1 = "window_close"
            boolean r1 = r14.equals(r1)
            if (r1 != 0) goto L_0x02df
            goto L_0x0278
        L_0x02c9:
            java.lang.String r0 = "loaded"
            boolean r0 = r14.equals(r0)
            if (r0 != 0) goto L_0x02d2
            goto L_0x0278
        L_0x02d2:
            r0 = 1
            goto L_0x02df
        L_0x02d4:
            java.lang.String r0 = "webResume"
            boolean r0 = r14.equals(r0)
            if (r0 != 0) goto L_0x02de
            goto L_0x0278
        L_0x02de:
            r0 = 0
        L_0x02df:
            java.lang.String r1 = "{}"
            switch(r0) {
                case 0: goto L_0x035d;
                case 1: goto L_0x0311;
                case 2: goto L_0x02fe;
                case 3: goto L_0x035d;
                case 4: goto L_0x035d;
                case 5: goto L_0x02f5;
                case 6: goto L_0x035d;
                case 7: goto L_0x035d;
                case 8: goto L_0x035d;
                default: goto L_0x02e5;
            }
        L_0x02e5:
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r15)
            if (r0 == 0) goto L_0x02ed
            r15 = r12
            goto L_0x02f1
        L_0x02ed:
            java.lang.String r15 = java.lang.String.valueOf(r15)
        L_0x02f1:
            r13.a((java.lang.String) r14, (java.lang.String) r15)
            goto L_0x0360
        L_0x02f5:
            io.dcloud.feature.ui.a r15 = r13.j
            r15.b((io.dcloud.feature.ui.c) r13)
            r13.a((java.lang.String) r14, (java.lang.String) r1, (boolean) r3)
            goto L_0x0360
        L_0x02fe:
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r15)
            if (r0 == 0) goto L_0x0306
            r15 = r12
            goto L_0x030a
        L_0x0306:
            java.lang.String r15 = java.lang.String.valueOf(r15)
        L_0x030a:
            r13.a((java.lang.String) r14, (java.lang.String) r15)
            r13.g()
            goto L_0x0360
        L_0x0311:
            r13.B = r11
            org.json.JSONArray r15 = r13.w
            if (r15 == 0) goto L_0x0326
            io.dcloud.common.DHInterface.IWebview r0 = r13.x
            io.dcloud.common.DHInterface.IFrameView r2 = r13.z
            io.dcloud.common.DHInterface.IApp r2 = r2.obtainApp()
            java.lang.String r2 = r2.obtainAppId()
            r13.a((io.dcloud.common.DHInterface.IWebview) r0, (org.json.JSONArray) r15, (io.dcloud.feature.ui.c) r13, (java.lang.String) r2)
        L_0x0326:
            java.lang.String r15 = io.dcloud.common.util.TestUtil.CREATE_WEBVIEW
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = r13.A
            r0.append(r2)
            java.lang.String r2 = " 从加载完成分发loaded事件到开始分发事件 "
            r0.append(r2)
            r0.append(r14)
            java.lang.String r0 = r0.toString()
            io.dcloud.common.util.TestUtil.print(r15, r0)
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            java.lang.String r0 = "EVENTS_LOADED mUrl="
            r15.append(r0)
            java.lang.String r0 = r13.A
            r15.append(r0)
            java.lang.String r15 = r15.toString()
            java.lang.String r0 = "Main_Path"
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r0, (java.lang.String) r15)
            r13.a((java.lang.String) r14, (java.lang.String) r1, (boolean) r3)
            goto L_0x0360
        L_0x035d:
            r13.a((java.lang.String) r14, (java.lang.String) r1, (boolean) r3)
        L_0x0360:
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.c.onCallBack(java.lang.String, java.lang.Object):java.lang.Object");
    }

    public boolean p() {
        return this.c0;
    }

    /* access modifiers changed from: protected */
    public void q() {
    }

    public IWebview r() {
        return this.z.obtainWebView();
    }

    private c(a aVar, IFrameView iFrameView, String str, String str2, String str3, JSONObject jSONObject) {
        super("NWindow");
        this.v = System.currentTimeMillis();
        this.w = null;
        this.x = null;
        this.y = null;
        this.A = null;
        this.B = -1;
        this.C = null;
        this.D = false;
        this.E = false;
        this.F = 0;
        this.G = false;
        this.H = false;
        this.I = true;
        this.J = false;
        this.K = false;
        this.L = false;
        this.M = false;
        this.N = null;
        this.O = null;
        this.P = null;
        this.Q = null;
        this.R = null;
        this.S = null;
        this.T = null;
        this.U = null;
        this.V = null;
        this.W = true;
        this.X = null;
        this.Y = null;
        this.Z = false;
        this.a0 = "auto";
        this.b0 = 150;
        this.c0 = false;
        this.d0 = null;
        this.j = aVar;
        this.A = str;
        this.l = str3;
        this.o = jSONObject;
        a(iFrameView, str2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:122:0x0308  */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x0359 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0050  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0091 A[SYNTHETIC, Splitter:B:30:0x0091] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00d9  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00e0  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0102  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x011b  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0132 A[Catch:{ JSONException -> 0x013e }] */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0182  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x0191  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x01ae  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x01ca  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x01d2  */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x01e9  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x01fc  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void e(io.dcloud.common.DHInterface.IWebview r20, org.json.JSONArray r21, io.dcloud.feature.ui.c r22) {
        /*
            r19 = this;
            r1 = r19
            r2 = r20
            r3 = r22
            java.lang.String r4 = "isUniH5"
            java.lang.String r5 = "debugRefresh"
            java.lang.String r6 = "animationAlphaBGColor"
            boolean r0 = r3.K
            if (r0 != 0) goto L_0x036e
            io.dcloud.common.DHInterface.IFrameView r0 = r3.z
            r7 = r0
            io.dcloud.common.adapter.ui.AdaFrameItem r7 = (io.dcloud.common.adapter.ui.AdaFrameItem) r7
            io.dcloud.common.adapter.util.ViewOptions r8 = r7.obtainFrameOptions()
            r9 = 0
            r0 = r21
            org.json.JSONObject r10 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONArray) r0, (int) r9)
            java.lang.Boolean r0 = r8.isTabItem
            boolean r0 = r0.booleanValue()
            java.lang.String r11 = "background"
            r12 = 1
            if (r0 == 0) goto L_0x0043
            boolean r0 = r10.isNull(r11)
            if (r0 != 0) goto L_0x0043
            java.lang.String r0 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r10, (java.lang.String) r11)     // Catch:{ JSONException -> 0x003f }
            r10.remove(r11)     // Catch:{ JSONException -> 0x003f }
            java.lang.String r13 = "tabBGColor"
            r10.put(r13, r0)     // Catch:{ JSONException -> 0x003f }
            r13 = 1
            goto L_0x0044
        L_0x003f:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0043:
            r13 = 0
        L_0x0044:
            boolean r14 = r1.a((org.json.JSONObject) r10, (boolean) r12)
            boolean r0 = r10.isNull(r11)
            r15 = r0 ^ 1
            if (r15 == 0) goto L_0x006a
            java.lang.String r0 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r10, (java.lang.String) r11)
            java.util.Locale r12 = java.util.Locale.ENGLISH
            java.lang.String r0 = r0.toLowerCase(r12)
            boolean r12 = android.text.TextUtils.isEmpty(r0)
            if (r12 != 0) goto L_0x006a
            java.lang.String r12 = "transparent"
            boolean r0 = r0.equals(r12)
            if (r0 == 0) goto L_0x006a
            r0 = 1
            goto L_0x006b
        L_0x006a:
            r0 = 0
        L_0x006b:
            java.lang.String r12 = "webviewBGTransparent"
            boolean r16 = r10.isNull(r12)
            if (r16 != 0) goto L_0x0085
            boolean r12 = io.dcloud.common.util.JSONUtil.getBoolean(r10, r12)
            if (r12 == 0) goto L_0x0085
            io.dcloud.common.DHInterface.IFrameView r12 = r3.z
            io.dcloud.common.DHInterface.IWebview r12 = r12.obtainWebView()
            io.dcloud.common.adapter.ui.AdaWebview r12 = (io.dcloud.common.adapter.ui.AdaWebview) r12
            r12.setBgcolor(r9)
        L_0x0085:
            boolean r12 = r3.L
            r16 = 0
            if (r12 != 0) goto L_0x008f
            if (r15 == 0) goto L_0x0115
            if (r0 != 0) goto L_0x0115
        L_0x008f:
            if (r15 == 0) goto L_0x00d1
            java.lang.String r0 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r10, (java.lang.String) r11)     // Catch:{ JSONException -> 0x00cd }
            boolean r12 = r10.isNull(r6)     // Catch:{ JSONException -> 0x00cd }
            if (r12 != 0) goto L_0x00a4
            org.json.JSONObject r12 = r8.mJsonViewOption     // Catch:{ JSONException -> 0x00cd }
            java.lang.String r9 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r10, (java.lang.String) r6)     // Catch:{ JSONException -> 0x00cd }
            r12.put(r6, r9)     // Catch:{ JSONException -> 0x00cd }
        L_0x00a4:
            org.json.JSONObject r6 = r8.mJsonViewOption     // Catch:{ JSONException -> 0x00cd }
            r6.put(r11, r0)     // Catch:{ JSONException -> 0x00cd }
            boolean r0 = r10.has(r5)     // Catch:{ JSONException -> 0x00cd }
            if (r0 == 0) goto L_0x00b8
            org.json.JSONObject r0 = r8.mJsonViewOption     // Catch:{ JSONException -> 0x00cd }
            org.json.JSONObject r6 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r10, (java.lang.String) r5)     // Catch:{ JSONException -> 0x00cd }
            r0.put(r5, r6)     // Catch:{ JSONException -> 0x00cd }
        L_0x00b8:
            boolean r0 = r10.has(r4)     // Catch:{ JSONException -> 0x00cd }
            if (r0 == 0) goto L_0x00c7
            org.json.JSONObject r0 = r8.mJsonViewOption     // Catch:{ JSONException -> 0x00cd }
            boolean r5 = io.dcloud.common.util.JSONUtil.getBoolean(r10, r4)     // Catch:{ JSONException -> 0x00cd }
            r0.put(r4, r5)     // Catch:{ JSONException -> 0x00cd }
        L_0x00c7:
            org.json.JSONObject r0 = r8.mJsonViewOption     // Catch:{ JSONException -> 0x00cd }
            r8.updateViewData((org.json.JSONObject) r0)     // Catch:{ JSONException -> 0x00cd }
            goto L_0x00d1
        L_0x00cd:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00d1:
            io.dcloud.common.adapter.util.ViewOptions r0 = r7.obtainFrameOptions()
            org.json.JSONObject r0 = r0.titleNView
            if (r0 == 0) goto L_0x00e0
            io.dcloud.common.adapter.util.ViewOptions r0 = r7.obtainFrameOptions()
            org.json.JSONObject r0 = r0.titleNView
            goto L_0x00e2
        L_0x00e0:
            r0 = r16
        L_0x00e2:
            io.dcloud.common.DHInterface.IFrameView r4 = r3.z
            io.dcloud.common.adapter.ui.AdaWebViewParent r7 = r4.obtainWebviewParent()
            if (r0 == 0) goto L_0x00f8
            io.dcloud.common.adapter.util.ViewOptions r4 = r7.obtainFrameOptions()
            org.json.JSONObject r4 = r4.titleNView
            if (r4 != 0) goto L_0x00f8
            io.dcloud.common.adapter.util.ViewOptions r4 = r7.obtainFrameOptions()
            r4.titleNView = r0
        L_0x00f8:
            io.dcloud.common.adapter.util.ViewOptions r0 = r7.obtainFrameOptions()
            io.dcloud.common.adapter.util.ViewRect r0 = r0.getParentViewRect()
            if (r0 != 0) goto L_0x0115
            io.dcloud.feature.ui.a r0 = r1.j
            io.dcloud.common.DHInterface.AbsMgr r0 = r0.d
            io.dcloud.common.DHInterface.IMgr$MgrType r4 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr
            r5 = 1
            java.lang.Object[] r6 = new java.lang.Object[r5]
            io.dcloud.common.DHInterface.IFrameView r5 = r3.z
            r9 = 0
            r6[r9] = r5
            r5 = 74
            r0.processEvent(r4, r5, r6)
        L_0x0115:
            boolean r0 = r8.hasBackground()
            if (r0 == 0) goto L_0x0121
            io.dcloud.common.DHInterface.IFrameView r0 = r3.z
            io.dcloud.common.adapter.ui.AdaWebViewParent r7 = r0.obtainWebviewParent()
        L_0x0121:
            io.dcloud.common.adapter.util.ViewOptions r0 = r7.obtainFrameOptions()
            r4 = 1
            r0.allowUpdate = r4
            io.dcloud.common.adapter.util.ViewOptions r4 = r7.obtainFrameOptions()
            int r5 = r4.maskColor
            org.json.JSONObject r0 = r4.titleNView     // Catch:{ JSONException -> 0x013e }
            if (r0 == 0) goto L_0x0142
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ JSONException -> 0x013e }
            org.json.JSONObject r6 = r4.titleNView     // Catch:{ JSONException -> 0x013e }
            java.lang.String r6 = r6.toString()     // Catch:{ JSONException -> 0x013e }
            r0.<init>(r6)     // Catch:{ JSONException -> 0x013e }
            goto L_0x0144
        L_0x013e:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0142:
            r0 = r16
        L_0x0144:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r8 = "setOption _old_win_options="
            r6.append(r8)
            r6.append(r4)
            java.lang.String r8 = ";_new_json_option="
            r6.append(r8)
            r6.append(r10)
            java.lang.String r6 = r6.toString()
            java.lang.String r8 = "shutao"
            io.dcloud.common.adapter.util.Logger.e(r8, r6)
            io.dcloud.common.adapter.util.ViewRect r6 = r4.getParentViewRect()
            io.dcloud.common.adapter.util.ViewOptions r6 = io.dcloud.common.adapter.util.ViewOptions.createViewOptionsData(r4, r6)
            io.dcloud.common.DHInterface.IFrameView r8 = r1.z
            io.dcloud.common.DHInterface.IWebview r8 = r8.obtainWebView()
            java.lang.String r9 = "shareable"
            java.lang.String r11 = r10.optString(r9)
            r8.setWebviewProperty(r9, r11)
            java.lang.String r9 = "videoFullscreen"
            boolean r11 = io.dcloud.common.util.JSONUtil.isNull(r10, r9)
            if (r11 != 0) goto L_0x0189
            java.lang.String r11 = r10.optString(r9)
            r8.setWebviewProperty(r9, r11)
        L_0x0189:
            java.lang.String r9 = "pullToRefresh"
            boolean r11 = r10.has(r9)
            if (r11 == 0) goto L_0x01a6
            org.json.JSONObject r9 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r10, (java.lang.String) r9)
            io.dcloud.common.DHInterface.IFrameView r11 = r3.z
            io.dcloud.common.DHInterface.IWebview r11 = r11.obtainWebView()
            org.json.JSONObject r12 = r4.mPullToRefresh
            org.json.JSONObject r9 = io.dcloud.common.util.JSONUtil.combinJSONObject(r12, r9)
            java.lang.String r12 = "pull_down_refresh"
            r11.setWebViewEvent(r12, r9)
        L_0x01a6:
            java.lang.String r9 = "bounce"
            boolean r11 = io.dcloud.common.util.JSONUtil.isNull(r10, r9)
            if (r11 != 0) goto L_0x01b5
            java.lang.String r11 = r10.optString(r9)
            r8.setWebviewProperty(r9, r11)
        L_0x01b5:
            java.lang.String r9 = r6.mCacheMode
            r2.setWebViewCacheMode(r9)
            org.json.JSONObject r9 = r1.o
            io.dcloud.common.util.JSONUtil.combinJSONObject(r9, r10)
            r19.f()
            java.lang.String r9 = "titleNView"
            boolean r11 = io.dcloud.common.util.JSONUtil.isNull(r10, r9)
            if (r11 != 0) goto L_0x01d2
            org.json.JSONObject r9 = r10.optJSONObject(r9)
            r1.a((io.dcloud.common.DHInterface.IWebview) r2, (io.dcloud.feature.ui.c) r3, (org.json.JSONObject) r0, (org.json.JSONObject) r9)
            goto L_0x01e1
        L_0x01d2:
            java.lang.String r9 = "navigationbar"
            boolean r11 = io.dcloud.common.util.JSONUtil.isNull(r10, r9)
            if (r11 != 0) goto L_0x01e1
            org.json.JSONObject r9 = r10.optJSONObject(r9)
            r1.a((io.dcloud.common.DHInterface.IWebview) r2, (io.dcloud.feature.ui.c) r3, (org.json.JSONObject) r0, (org.json.JSONObject) r9)
        L_0x01e1:
            java.lang.String r0 = "uniNView"
            boolean r2 = io.dcloud.common.util.JSONUtil.isNull(r10, r0)
            if (r2 != 0) goto L_0x01f0
            org.json.JSONObject r0 = r10.optJSONObject(r0)
            r1.a((io.dcloud.feature.ui.c) r3, (org.json.JSONObject) r0)
        L_0x01f0:
            boolean r0 = r3.J
            java.lang.String r2 = "geolocation"
            java.lang.String r9 = "plusrequire"
            java.lang.String r11 = "injection"
            java.lang.String r12 = "scalable"
            if (r0 == 0) goto L_0x0308
            int r0 = r6.background
            r16 = r13
            float r13 = r6.opacity
            boolean r17 = r6.updateViewData((org.json.JSONObject) r10)
            boolean r0 = io.dcloud.common.util.PdrUtil.checkAlphaTransparent(r0)
            r18 = r15
            int r15 = r6.background
            boolean r15 = io.dcloud.common.util.PdrUtil.checkAlphaTransparent(r15)
            if (r0 == r15) goto L_0x0216
            r0 = 1
            goto L_0x0217
        L_0x0216:
            r0 = 0
        L_0x0217:
            float r15 = r6.opacity
            int r13 = (r13 > r15 ? 1 : (r13 == r15 ? 0 : -1))
            if (r13 == 0) goto L_0x021f
            r13 = 1
            goto L_0x0220
        L_0x021f:
            r13 = 0
        L_0x0220:
            r0 = r0 | r13
            java.lang.String r13 = "render"
            boolean r15 = r10.has(r13)
            if (r15 == 0) goto L_0x023a
            java.lang.String r15 = "onscreen"
            java.lang.String r13 = r10.optString(r13, r15)
            java.lang.String r15 = "always"
            boolean r13 = io.dcloud.common.util.PdrUtil.isEquals(r13, r15)
            io.dcloud.common.DHInterface.IFrameView r15 = r3.z
            r15.setNeedRender(r13)
        L_0x023a:
            boolean r13 = r6.hasBackground()
            r3.L = r13
            java.lang.String r13 = "scrollIndicator"
            boolean r13 = io.dcloud.common.util.JSONUtil.isNull(r10, r13)
            if (r13 != 0) goto L_0x024f
            java.lang.String r13 = r6.getScrollIndicator()
            r8.setScrollIndicator(r13)
        L_0x024f:
            boolean r13 = io.dcloud.common.util.JSONUtil.isNull(r10, r12)
            if (r13 != 0) goto L_0x025e
            boolean r13 = r6.scalable
            java.lang.String r13 = java.lang.String.valueOf(r13)
            r8.setWebviewProperty(r12, r13)
        L_0x025e:
            java.lang.String r12 = r6.mInjection
            r8.setWebviewProperty(r11, r12)
            java.lang.String r11 = r6.mPlusrequire
            r8.setWebviewProperty(r9, r11)
            java.lang.String r9 = r6.mGeoInject
            r8.setWebviewProperty(r2, r9)
            io.dcloud.common.adapter.util.AnimOptions r2 = r7.getAnimOptions()
            java.lang.String r8 = "transition"
            boolean r8 = io.dcloud.common.util.JSONUtil.isNull(r10, r8)
            if (r8 != 0) goto L_0x028c
            org.json.JSONObject r8 = r6.transition
            r2.parseTransition(r8)
            org.json.JSONObject r8 = r6.transition
            java.lang.String r9 = "duration"
            boolean r8 = r8.isNull(r9)
            if (r8 == 0) goto L_0x028f
            r8 = 0
            r2.duration = r8
            goto L_0x028f
        L_0x028c:
            r8 = 0
            r2.duration = r8
        L_0x028f:
            java.lang.String r8 = "transform"
            boolean r8 = io.dcloud.common.util.JSONUtil.isNull(r10, r8)
            if (r8 != 0) goto L_0x029c
            org.json.JSONObject r8 = r6.transform
            r2.parseTransform(r8)
        L_0x029c:
            if (r17 != 0) goto L_0x02cb
            if (r14 != 0) goto L_0x02cb
            if (r0 == 0) goto L_0x02a3
            goto L_0x02cb
        L_0x02a3:
            r4.updateViewData((org.json.JSONObject) r10)
            int r0 = r6.maskColor
            if (r5 == r0) goto L_0x0357
            io.dcloud.common.adapter.util.ViewOptions r0 = r7.obtainFrameOptions()
            int r2 = r6.maskColor
            r0.maskColor = r2
            io.dcloud.common.DHInterface.IFrameView r0 = r3.z
            io.dcloud.common.adapter.ui.AdaFrameView r0 = (io.dcloud.common.adapter.ui.AdaFrameView) r0
            io.dcloud.common.adapter.util.ViewOptions r0 = r0.obtainFrameOptions()
            int r2 = r6.maskColor
            r0.maskColor = r2
            io.dcloud.common.DHInterface.IFrameView r0 = r3.z
            io.dcloud.common.adapter.ui.AdaFrameView r0 = (io.dcloud.common.adapter.ui.AdaFrameView) r0
            android.view.View r0 = r0.obtainMainView()
            r0.invalidate()
            goto L_0x0357
        L_0x02cb:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z
            r2.setFrameOptions_Animate(r6)
            r7.setFrameOptions_Animate(r6)
            io.dcloud.common.DHInterface.IFrameView r2 = r3.z
            io.dcloud.common.adapter.ui.AdaFrameItem r2 = (io.dcloud.common.adapter.ui.AdaFrameItem) r2
            io.dcloud.common.adapter.util.AnimOptions r2 = r2.getAnimOptions()
            r4 = 2
            r2.mOption = r4
            r2 = 4
            java.lang.Object[] r2 = new java.lang.Object[r2]
            io.dcloud.common.DHInterface.IFrameView r5 = r3.z
            r6 = 0
            r2[r6] = r5
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r17)
            r6 = 1
            r2[r6] = r5
            if (r17 == 0) goto L_0x02f0
            r14 = 0
        L_0x02f0:
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r14)
            r2[r4] = r5
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            r4 = 3
            r2[r4] = r0
            io.dcloud.feature.ui.a r0 = r1.j
            io.dcloud.common.DHInterface.AbsMgr r0 = r0.d
            io.dcloud.common.DHInterface.IMgr$MgrType r4 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr
            r5 = 7
            r0.processEvent(r4, r5, r2)
            goto L_0x0357
        L_0x0308:
            r16 = r13
            r18 = r15
            io.dcloud.common.adapter.util.ViewOptions r0 = r7.obtainFrameOptions()
            boolean r0 = r0.updateViewData((org.json.JSONObject) r10)
            io.dcloud.common.adapter.util.ViewOptions r4 = r7.obtainFrameOptions_Birth()
            r4.updateViewData((org.json.JSONObject) r10)
            io.dcloud.common.adapter.util.ViewOptions r4 = r7.obtainFrameOptions()
            java.lang.String r5 = r4.getScrollIndicator()
            r8.setScrollIndicator(r5)
            boolean r4 = r4.scalable
            java.lang.String r4 = java.lang.String.valueOf(r4)
            r8.setWebviewProperty(r12, r4)
            java.lang.String r4 = r6.mInjection
            r8.setWebviewProperty(r11, r4)
            java.lang.String r4 = r6.mPlusrequire
            r8.setWebviewProperty(r9, r4)
            java.lang.String r4 = r6.mGeoInject
            r8.setWebviewProperty(r2, r4)
            if (r0 == 0) goto L_0x0357
            io.dcloud.common.adapter.util.ViewOptions r0 = r7.obtainFrameOptions()
            int r2 = r0.left
            io.dcloud.common.DHInterface.IFrameView r2 = r3.z
            android.view.View r2 = r2.obtainMainView()
            int r4 = r0.left
            int r5 = r0.top
            int r6 = r0.width
            int r0 = r0.height
            io.dcloud.common.adapter.ui.AdaFrameItem.LayoutParamsUtil.setViewLayoutParams(r2, r4, r5, r6, r0)
        L_0x0357:
            if (r18 != 0) goto L_0x035b
            if (r16 == 0) goto L_0x036e
        L_0x035b:
            io.dcloud.feature.ui.a r0 = r1.j
            io.dcloud.common.DHInterface.AbsMgr r0 = r0.d
            io.dcloud.common.DHInterface.IMgr$MgrType r2 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr
            r4 = 1
            java.lang.Object[] r4 = new java.lang.Object[r4]
            io.dcloud.common.DHInterface.IFrameView r3 = r3.z
            r5 = 0
            r4[r5] = r3
            r3 = 75
            r0.processEvent(r2, r3, r4)
        L_0x036e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.c.e(io.dcloud.common.DHInterface.IWebview, org.json.JSONArray, io.dcloud.feature.ui.c):void");
    }

    public void a(boolean z2) {
        this.c0 = z2;
    }

    public boolean b(b bVar) {
        ArrayList<b> arrayList = this.N;
        if (arrayList == null) {
            return false;
        }
        return arrayList.contains(bVar);
    }

    public AdaFrameItem d() {
        return (AdaFrameItem) this.z;
    }

    public String h() {
        String str = "{}";
        if (PdrUtil.isEmpty(r().obtainFrameId())) {
            Object[] objArr = new Object[4];
            objArr[0] = this.l;
            objArr[1] = Constants.Name.UNDEFINED;
            objArr[2] = this.k;
            JSONObject jSONObject = this.y;
            if (jSONObject != null) {
                str = jSONObject.toString();
            }
            objArr[3] = str;
            return StringUtil.format("(function(){return {'uuid':'%s','id':%s,'identity':'%s','extras':%s}})()", objArr);
        }
        Object[] objArr2 = new Object[4];
        objArr2[0] = this.l;
        objArr2[1] = r().obtainFrameId();
        objArr2[2] = this.k;
        JSONObject jSONObject2 = this.y;
        if (jSONObject2 != null) {
            str = jSONObject2.toString();
        }
        objArr2[3] = str;
        return StringUtil.format("(function(){return {'uuid':'%s','id':'%s','identity':'%s','extras':%s}})()", objArr2);
    }

    private void d(IWebview iWebview, JSONArray jSONArray, c cVar) {
        String string = JSONUtil.getString(jSONArray, 0);
        String string2 = JSONUtil.getString(jSONArray, 1);
        AnimOptions animOptions = ((AdaFrameItem) cVar.z).getAnimOptions();
        if (!PdrUtil.isEmpty(string2)) {
            animOptions.duration_close = PdrUtil.parseInt(string2, animOptions.duration_close);
        } else {
            animOptions.duration_close = animOptions.duration_show;
        }
        animOptions.setCloseAnimType(string);
        animOptions.mOption = 3;
        Logger.d(Logger.VIEW_VISIBLE_TAG, "NWindow.hide view=" + cVar.d());
        if (cVar.G) {
            if (cVar.k()) {
                a(iWebview, JSONUtil.getJSONObject(jSONArray, 2), cVar, string);
                this.j.d.processEvent(IMgr.MgrType.WindowMgr, 23, cVar.z);
            } else {
                onCallBack("hide", (Object) null);
                cVar.z.setVisible(false, true);
            }
            cVar.G = false;
        } else {
            cVar.z.setVisible(false, true);
        }
        cVar.H = true;
    }

    public void a(IFrameView iFrameView, String str) {
        if (iFrameView != null) {
            this.z = iFrameView;
            IWebview obtainWebView = iFrameView.obtainWebView();
            if (obtainWebView != null) {
                obtainWebView.initWebviewUUID(this.l);
                obtainWebView.setFrameId(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void b(c cVar) {
        if (this.Y == null) {
            this.Y = new ArrayList<>();
        }
        this.Y.add(cVar);
        cVar.X = this;
        if (cVar.r() != null) {
            cVar.r().setOpener(r());
        }
    }

    /* access modifiers changed from: protected */
    public void c(b bVar) {
        ArrayList<b> arrayList = this.N;
        if (arrayList != null && arrayList.contains(bVar)) {
            this.N.remove(bVar);
            bVar.h = null;
            byte c = bVar.c();
            boolean z2 = bVar instanceof c;
            if (c == b.a) {
                this.z.obtainWebView().removeFrameItem(bVar.d());
            } else if (c == b.b) {
                this.z.obtainWebviewParent().removeFrameItem(bVar.d());
            } else if (c == b.c) {
                this.z.removeFrameItem(bVar.d());
                if (z2) {
                    this.z.obtainWebviewParent().obtainFrameOptions().delRelViewRect(bVar.d().obtainFrameOptions());
                }
                d().resize();
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Object[]} */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(io.dcloud.common.DHInterface.IWebview r7, java.lang.String r8) {
        /*
            r6 = this;
            io.dcloud.feature.ui.a r0 = r6.j
            io.dcloud.common.DHInterface.AbsMgr r0 = r0.d
            io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r2 = 4
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r3 = 0
            r2[r3] = r7
            java.lang.String r7 = "ad"
            r4 = 1
            r2[r4] = r7
            java.lang.String r7 = "addNativeView"
            r5 = 2
            r2[r5] = r7
            java.lang.Object[] r7 = new java.lang.Object[r5]
            io.dcloud.common.DHInterface.IFrameView r5 = r6.z
            r7[r3] = r5
            r7[r4] = r8
            r8 = 3
            r2[r8] = r7
            r7 = 10
            r0.processEvent(r1, r7, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.c.a(io.dcloud.common.DHInterface.IWebview, java.lang.String):void");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x0226, code lost:
        r5 = r2.titleNView;
     */
    /* JADX WARNING: Removed duplicated region for block: B:102:0x02a0  */
    /* JADX WARNING: Removed duplicated region for block: B:109:0x02c9  */
    /* JADX WARNING: Removed duplicated region for block: B:113:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00e7  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00f5  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0118  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0130  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x013d  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x01a2  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x01f0  */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x0278  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(io.dcloud.feature.ui.b r24) {
        /*
            r23 = this;
            r0 = r23
            r8 = r24
            java.util.ArrayList<io.dcloud.feature.ui.b> r1 = r0.N
            if (r1 != 0) goto L_0x0010
            java.util.ArrayList r1 = new java.util.ArrayList
            r2 = 2
            r1.<init>(r2)
            r0.N = r1
        L_0x0010:
            io.dcloud.common.adapter.ui.AdaFrameItem r15 = r24.d()
            byte r14 = r24.c()
            byte r1 = io.dcloud.common.adapter.util.ViewRect.POSITION_DOCK
            if (r14 != r1) goto L_0x0024
            byte r1 = r24.b()
            r15.setPosition(r1)
            goto L_0x0027
        L_0x0024:
            r15.setPosition(r14)
        L_0x0027:
            java.util.ArrayList<io.dcloud.feature.ui.b> r1 = r0.N
            boolean r1 = r1.contains(r8)
            if (r1 == 0) goto L_0x0030
            return
        L_0x0030:
            boolean r1 = r8 instanceof io.dcloud.feature.ui.c
            r2 = 0
            java.lang.String r9 = "View_Visible_Path"
            r13 = 0
            r3 = 1
            if (r1 == 0) goto L_0x00bb
            r1 = r8
            io.dcloud.feature.ui.c r1 = (io.dcloud.feature.ui.c) r1
            r0.a((io.dcloud.feature.ui.b) r8, (io.dcloud.feature.ui.c) r1)
            io.dcloud.feature.ui.a r4 = r0.j
            boolean r4 = r4.a((io.dcloud.feature.ui.c) r1)
            if (r4 == 0) goto L_0x0053
            boolean r4 = r0.G
            if (r4 == 0) goto L_0x0053
            r4 = 4
            r1.B = r4
            io.dcloud.feature.ui.a r4 = r0.j
            r4.f(r1)
        L_0x0053:
            io.dcloud.common.adapter.ui.AdaFrameItem r4 = r24.d()
            android.view.View r4 = r4.obtainMainView()
            int r4 = r4.getVisibility()
            if (r4 == 0) goto L_0x007e
            boolean r4 = r1.H
            if (r4 != 0) goto L_0x007e
            r1.G = r3
            io.dcloud.common.DHInterface.IFrameView r4 = r1.z
            r4.setVisible(r3, r3)
            io.dcloud.common.DHInterface.IFrameView r4 = r1.z
            boolean r5 = r4 instanceof io.dcloud.common.adapter.ui.AdaFrameView
            if (r5 == 0) goto L_0x0079
            io.dcloud.common.adapter.ui.AdaFrameView r4 = (io.dcloud.common.adapter.ui.AdaFrameView) r4
            java.lang.String r5 = "child_initialize_show"
            r4.dispatchFrameViewEvents(r5, r2)
        L_0x0079:
            java.lang.String r4 = "NWindow.appendView childView set visible true"
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r9, (java.lang.String) r4)
        L_0x007e:
            io.dcloud.feature.ui.a r4 = r0.j
            io.dcloud.common.DHInterface.IFrameView r5 = r1.z
            r4.c((io.dcloud.common.DHInterface.IFrameView) r5)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "append "
            r4.append(r5)
            io.dcloud.common.DHInterface.IFrameView r5 = r1.z
            r4.append(r5)
            java.lang.String r4 = r4.toString()
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r9, (java.lang.String) r4)
            r1.M = r3
            r1.J = r3
            io.dcloud.common.DHInterface.IWebview r4 = r1.r()
            if (r4 == 0) goto L_0x00c8
            io.dcloud.common.DHInterface.IWebview r4 = r1.r()
            io.dcloud.common.DHInterface.IApp r4 = r4.obtainApp()
            boolean r4 = io.dcloud.common.util.BaseInfo.isUniAppAppid(r4)
            if (r4 == 0) goto L_0x00c8
            io.dcloud.common.DHInterface.IWebview r1 = r1.r()
            r1.setIWebViewFocusable(r3)
            goto L_0x00c8
        L_0x00bb:
            boolean r1 = r8 instanceof io.dcloud.feature.ui.d
            if (r1 == 0) goto L_0x00ca
            r1 = r8
            io.dcloud.feature.ui.d r1 = (io.dcloud.feature.ui.d) r1
            r1.i()
            r1.a((boolean) r3)
        L_0x00c8:
            r10 = 1
            goto L_0x00cb
        L_0x00ca:
            r10 = 0
        L_0x00cb:
            org.json.JSONObject r1 = r8.o
            io.dcloud.common.DHInterface.IFrameView r4 = r0.z
            io.dcloud.common.adapter.ui.AdaFrameItem r4 = (io.dcloud.common.adapter.ui.AdaFrameItem) r4
            io.dcloud.common.adapter.util.ViewOptions r12 = r4.obtainFrameOptions()
            io.dcloud.common.DHInterface.IFrameView r4 = r0.z
            io.dcloud.common.adapter.ui.AdaWebViewParent r4 = r4.obtainWebviewParent()
            io.dcloud.common.adapter.util.ViewOptions r11 = r4.obtainFrameOptions()
            io.dcloud.common.adapter.util.ViewOptions r7 = r15.obtainFrameOptions()
            byte r5 = io.dcloud.feature.ui.b.c
            if (r14 != r5) goto L_0x00f5
            io.dcloud.common.DHInterface.IFrameView r2 = r0.z
            io.dcloud.common.adapter.util.ViewRect r5 = r12.getParentViewRect()
            r11.setParentViewRect(r5)
            r11.updateViewData((io.dcloud.common.adapter.util.ViewRect) r12)
        L_0x00f3:
            r6 = r2
            goto L_0x0112
        L_0x00f5:
            byte r5 = io.dcloud.feature.ui.b.b
            if (r14 != r5) goto L_0x00fc
            io.dcloud.common.DHInterface.IFrameView r2 = r0.z
            goto L_0x0106
        L_0x00fc:
            byte r5 = io.dcloud.feature.ui.b.a
            if (r14 != r5) goto L_0x0106
            io.dcloud.common.DHInterface.IFrameView r2 = r0.z
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()
        L_0x0106:
            io.dcloud.common.DHInterface.IFrameView r5 = r0.z
            io.dcloud.common.adapter.ui.AdaFrameView r5 = (io.dcloud.common.adapter.ui.AdaFrameView) r5
            io.dcloud.common.adapter.util.ViewOptions r5 = r5.obtainFrameOptions()
            r7.setParentViewRect(r5)
            goto L_0x00f3
        L_0x0112:
            boolean r2 = r12.hasBackground()
            if (r2 == 0) goto L_0x0130
            int r2 = r11.width
            int r5 = r11.height
            float r3 = r11.mWebviewScale
            r7.updateViewData(r1, r2, r5, r3)
            boolean r1 = r7.hasBackground()
            if (r1 == 0) goto L_0x0139
            r7.left = r13
            r7.top = r13
            r7.anim_top = r13
            r7.anim_left = r13
            goto L_0x0139
        L_0x0130:
            int r2 = r12.width
            int r3 = r12.height
            float r5 = r12.mWebviewScale
            r7.updateViewData(r1, r2, r3, r5)
        L_0x0139:
            byte r1 = io.dcloud.feature.ui.b.c
            if (r14 != r1) goto L_0x01a2
            r1 = r6
            io.dcloud.common.adapter.ui.AdaFrameItem r1 = (io.dcloud.common.adapter.ui.AdaFrameItem) r1
            io.dcloud.common.adapter.util.ViewOptions r1 = r1.obtainFrameOptions()
            r7.setParentViewRect(r1)
            io.dcloud.common.adapter.util.ViewRect.layoutDockViewRect(r11, r7)
            r1 = 1
            r4.mNeedOrientationUpdate = r1
            r11.putRelViewRect(r7)
            int r1 = r11.left
            int r2 = r11.top
            int r3 = r11.width
            int r5 = r11.height
            boolean r16 = io.dcloud.common.util.BaseInfo.isImmersive
            if (r16 == 0) goto L_0x016c
            boolean r13 = r11.isStatusbar
            if (r13 == 0) goto L_0x016c
            boolean r13 = r12.isHeightAbsolute()
            if (r13 != 0) goto L_0x0169
            int r13 = io.dcloud.common.adapter.util.DeviceInfo.sStatusBarHeight
            int r5 = r5 + r13
        L_0x0169:
            int r13 = io.dcloud.common.adapter.util.DeviceInfo.sStatusBarHeight
            int r2 = r2 + r13
        L_0x016c:
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            r17 = r6
            java.lang.String r6 = "NWindow.appendView ---> _webview left="
            r13.append(r6)
            r13.append(r1)
            java.lang.String r6 = ";top="
            r13.append(r6)
            r13.append(r2)
            java.lang.String r6 = ";width="
            r13.append(r6)
            r13.append(r3)
            java.lang.String r6 = ";height="
            r13.append(r6)
            r13.append(r5)
            java.lang.String r6 = r13.toString()
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r9, (java.lang.String) r6)
            android.view.View r4 = r4.obtainMainView()
            io.dcloud.common.adapter.ui.AdaFrameItem.LayoutParamsUtil.setViewLayoutParams(r4, r1, r2, r3, r5)
            goto L_0x01a4
        L_0x01a2:
            r17 = r6
        L_0x01a4:
            int r13 = r7.width
            int r6 = r7.height
            int r5 = r7.left
            int r4 = r7.top
            int r2 = r11.left
            int r3 = r11.top
            int r1 = r11.width
            r18 = r5
            int r5 = r11.height
            r19 = r1
            r1 = r24
            r20 = r4
            r4 = r19
            r19 = r18
            r18 = r6
            r22 = r17
            r17 = r12
            r12 = r22
            r6 = r13
            r21 = r7
            r7 = r18
            r1.a(r2, r3, r4, r5, r6, r7)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "NWindow.appendView childView="
            r1.append(r2)
            io.dcloud.common.adapter.ui.AdaFrameItem r2 = r24.d()
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r9, (java.lang.String) r1)
            r6 = r12
            io.dcloud.common.adapter.ui.AdaContainerFrameItem r6 = (io.dcloud.common.adapter.ui.AdaContainerFrameItem) r6
            r15.setParentFrameItem(r6)
            if (r10 == 0) goto L_0x0278
            byte r1 = io.dcloud.feature.ui.b.a
            if (r14 != r1) goto L_0x01ff
            boolean r1 = r21.hasHeightAbsolutevalue()
            if (r1 != 0) goto L_0x01ff
            int r1 = r11.height
            if (r1 <= 0) goto L_0x01ff
            goto L_0x0201
        L_0x01ff:
            r1 = r18
        L_0x0201:
            android.view.View r2 = r15.obtainMainView()
            android.view.ViewGroup$LayoutParams r11 = r2.getLayoutParams()
            r9 = r12
            r10 = r15
            r2 = r17
            r12 = r19
            r3 = r13
            r4 = 0
            r13 = r20
            r5 = r14
            r14 = r3
            r7 = r15
            r15 = r1
            a(r9, r10, r11, r12, r13, r14, r15)
            byte r9 = io.dcloud.feature.ui.b.c
            if (r5 != r9) goto L_0x0244
            byte r5 = r24.b()
            byte r9 = io.dcloud.feature.ui.b.f
            if (r5 != r9) goto L_0x0244
            org.json.JSONObject r5 = r2.titleNView
            if (r5 == 0) goto L_0x0244
            boolean r5 = io.dcloud.common.util.TitleNViewUtil.isTitleTypeForDef(r5)
            if (r5 == 0) goto L_0x0244
            io.dcloud.common.DHInterface.IFrameView r5 = r0.z
            io.dcloud.common.DHInterface.IWebview r5 = r5.obtainWebView()
            float r5 = r5.getScale()
            java.lang.String r9 = "44px"
            int r4 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r9, r4, r4, r5)
            r5 = r20
            int r4 = r4 + r5
            goto L_0x0247
        L_0x0244:
            r5 = r20
            r4 = r5
        L_0x0247:
            r5 = r21
            boolean r9 = r5.isStatusbar
            if (r9 != 0) goto L_0x025d
            boolean r2 = r2.isStatusbar
            if (r2 == 0) goto L_0x025d
            int r2 = io.dcloud.common.adapter.util.DeviceInfo.sStatusBarHeight
            int r4 = r4 + r2
            boolean r2 = r5.isBottomAbsolute()
            if (r2 == 0) goto L_0x025d
            int r2 = io.dcloud.common.adapter.util.DeviceInfo.sStatusBarHeight
            int r1 = r1 - r2
        L_0x025d:
            boolean r2 = r5.isStatusbar
            if (r2 == 0) goto L_0x026a
            boolean r2 = r5.isBottomAbsolute()
            if (r2 != 0) goto L_0x026a
            int r2 = io.dcloud.common.adapter.util.DeviceInfo.sStatusBarHeight
            int r1 = r1 + r2
        L_0x026a:
            r2 = -1
            if (r1 >= r2) goto L_0x026e
            r1 = -1
        L_0x026e:
            android.view.View r2 = r7.obtainMainView()
            r7 = r19
            io.dcloud.common.adapter.ui.AdaFrameItem.LayoutParamsUtil.setViewLayoutParams(r2, r7, r4, r3, r1)
            goto L_0x028a
        L_0x0278:
            r3 = r13
            r1 = r18
            r7 = r19
            r5 = r20
            android.view.ViewGroup$LayoutParams r1 = io.dcloud.common.adapter.ui.AdaFrameItem.LayoutParamsUtil.createLayoutParams(r7, r5, r3, r1)
            io.dcloud.common.adapter.ui.AdaFrameItem r2 = r24.d()
            r12.addFrameItem(r2, r1)
        L_0x028a:
            java.util.ArrayList<io.dcloud.feature.ui.b> r1 = r0.N
            r1.add(r8)
            r8.h = r0
            io.dcloud.common.DHInterface.IFrameView r1 = r0.z
            io.dcloud.common.adapter.ui.AdaFrameView r1 = (io.dcloud.common.adapter.ui.AdaFrameView) r1
            r1.sortNativeViewBringToFront()
            io.dcloud.common.adapter.util.ViewOptions r1 = r6.obtainFrameOptions()
            boolean r1 = r1.isStatusbar
            if (r1 == 0) goto L_0x02a7
            io.dcloud.common.adapter.ui.AdaFrameItem r1 = r24.d()
            r1.resize()
        L_0x02a7:
            io.dcloud.common.DHInterface.IFrameView r1 = r0.z
            java.lang.String r1 = io.dcloud.common.util.TitleNViewUtil.getTitleNViewId(r1)
            boolean r2 = io.dcloud.common.util.PdrUtil.isEmpty(r1)
            if (r2 != 0) goto L_0x02ce
            io.dcloud.feature.ui.a r2 = r0.j
            io.dcloud.common.DHInterface.AbsMgr r2 = r2.d
            io.dcloud.common.DHInterface.IFrameView r3 = r0.z
            io.dcloud.common.DHInterface.IWebview r3 = r3.obtainWebView()
            io.dcloud.common.DHInterface.IFrameView r4 = r0.z
            java.lang.Object r1 = io.dcloud.common.util.TitleNViewUtil.getTitleNView(r2, r3, r4, r1)
            if (r1 == 0) goto L_0x02ce
            boolean r2 = r1 instanceof io.dcloud.common.DHInterface.ITitleNView
            if (r2 == 0) goto L_0x02ce
            io.dcloud.common.DHInterface.ITitleNView r1 = (io.dcloud.common.DHInterface.ITitleNView) r1
            r1.reMeasure()
        L_0x02ce:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.c.a(io.dcloud.feature.ui.b):void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Object[]} */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void b(io.dcloud.common.DHInterface.IWebview r7, java.lang.String r8) {
        /*
            r6 = this;
            io.dcloud.feature.ui.a r0 = r6.j
            io.dcloud.common.DHInterface.AbsMgr r0 = r0.d
            io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r2 = 4
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r3 = 0
            r2[r3] = r7
            java.lang.String r7 = "barcode"
            r4 = 1
            r2[r4] = r7
            java.lang.String r7 = "appendToFrameView"
            r5 = 2
            r2[r5] = r7
            java.lang.Object[] r7 = new java.lang.Object[r5]
            io.dcloud.common.DHInterface.IFrameView r5 = r6.z
            r7[r3] = r5
            r7[r4] = r8
            r8 = 3
            r2[r8] = r7
            r7 = 10
            r0.processEvent(r1, r7, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.c.b(io.dcloud.common.DHInterface.IWebview, java.lang.String):void");
    }

    /* access modifiers changed from: package-private */
    public void b(IWebview iWebview, JSONArray jSONArray, c cVar) {
        if (!cVar.J) {
            this.j.b(cVar);
            cVar.d().onDispose();
            cVar.d().dispose();
        } else if (!cVar.K) {
            this.j.b(cVar);
            if (cVar.M) {
                c cVar2 = cVar.h;
                if (cVar2 != null) {
                    cVar2.c((b) cVar);
                }
                cVar.d().onDispose();
                cVar.d().dispose();
            } else {
                String string = JSONUtil.getString(jSONArray, 0);
                String string2 = JSONUtil.getString(jSONArray, 1);
                AnimOptions animOptions = ((AdaFrameItem) cVar.z).getAnimOptions();
                if (PdrUtil.isEmpty(string)) {
                    string = "auto";
                }
                if (!PdrUtil.isEmpty(string2)) {
                    animOptions.duration_close = PdrUtil.parseInt(string2, animOptions.duration_close);
                } else if (string.equals(AnimOptions.ANIM_POP_OUT)) {
                    animOptions.duration_close = 360;
                } else {
                    animOptions.duration_close = animOptions.duration_show;
                }
                animOptions.setCloseAnimType(string);
                animOptions.mOption = 1;
                a(iWebview, JSONUtil.getJSONObject(jSONArray, 2), cVar, string);
                this.j.d.processEvent(IMgr.MgrType.WindowMgr, 2, cVar.z);
            }
        }
        cVar.e();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Object[]} */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void c(io.dcloud.common.DHInterface.IWebview r7, java.lang.String r8) {
        /*
            r6 = this;
            io.dcloud.feature.ui.a r0 = r6.j
            io.dcloud.common.DHInterface.AbsMgr r0 = r0.d
            io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r2 = 4
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r3 = 0
            r2[r3] = r7
            java.lang.String r7 = "livepusher"
            r4 = 1
            r2[r4] = r7
            java.lang.String r7 = "appendToFrameView"
            r5 = 2
            r2[r5] = r7
            java.lang.Object[] r7 = new java.lang.Object[r5]
            io.dcloud.common.DHInterface.IFrameView r5 = r6.z
            r7[r3] = r5
            r7[r4] = r8
            r8 = 3
            r2[r8] = r7
            r7 = 10
            r0.processEvent(r1, r7, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.c.c(io.dcloud.common.DHInterface.IWebview, java.lang.String):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x009a  */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x009c  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x00a9  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void c(io.dcloud.common.DHInterface.IWebview r18, org.json.JSONArray r19, io.dcloud.feature.ui.c r20) {
        /*
            r17 = this;
            r0 = r17
            r1 = r18
            r2 = r19
            r3 = 0
            java.lang.String r4 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r2, (int) r3)
            r5 = 1
            java.lang.String r5 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r2, (int) r5)
            io.dcloud.feature.ui.a r6 = r0.j
            r7 = 0
            io.dcloud.feature.ui.c r5 = r6.a((java.lang.String) r5, (java.lang.String) r5, (java.lang.String) r7)
            io.dcloud.common.adapter.ui.AdaFrameItem r5 = r5.d()
            android.view.View r9 = r5.obtainMainView()
            r5 = 2
            java.lang.String r5 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r2, (int) r5)
            r6 = 3
            org.json.JSONObject r2 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONArray) r2, (int) r6)
            java.lang.String r6 = "RGB565"
            if (r2 == 0) goto L_0x0087
            java.lang.String r8 = "check"
            boolean r8 = r2.optBoolean(r8, r3)
            java.lang.String r10 = "checkKeyboard"
            boolean r10 = r2.optBoolean(r10, r3)
            java.lang.String r11 = "bit"
            java.lang.String r6 = r2.optString(r11, r6)
            java.lang.String r11 = "clip"
            org.json.JSONObject r2 = r2.optJSONObject(r11)
            if (r2 == 0) goto L_0x0085
            int r11 = r9.getWidth()
            int r12 = r9.getHeight()
            float r13 = r18.getScale()
            java.lang.String r14 = "left"
            java.lang.String r14 = r2.optString(r14)
            int r14 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r14, r11, r3, r13)
            java.lang.String r15 = "top"
            java.lang.String r15 = r2.optString(r15)
            int r3 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r15, r12, r3, r13)
            java.lang.String r15 = "width"
            java.lang.String r15 = r2.optString(r15)
            int r11 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r15, r11, r11, r13)
            java.lang.String r15 = "height"
            java.lang.String r2 = r2.optString(r15)
            int r2 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r2, r12, r12, r13)
            android.graphics.Rect r12 = new android.graphics.Rect
            r12.<init>(r14, r3, r11, r2)
            r14 = r6
            r11 = r8
            r13 = r12
            goto L_0x008b
        L_0x0085:
            r3 = r8
            goto L_0x0088
        L_0x0087:
            r10 = 0
        L_0x0088:
            r11 = r3
            r14 = r6
            r13 = r7
        L_0x008b:
            r12 = r10
            io.dcloud.common.DHInterface.IFrameView r8 = r18.obtainFrameView()
            io.dcloud.common.DHInterface.INativeBitmap r10 = r0.g(r1, r4)
            boolean r2 = android.text.TextUtils.isEmpty(r5)
            if (r2 == 0) goto L_0x009c
            r15 = r7
            goto L_0x00a2
        L_0x009c:
            io.dcloud.feature.ui.c$h r2 = new io.dcloud.feature.ui.c$h
            r2.<init>(r1, r5)
            r15 = r2
        L_0x00a2:
            boolean r2 = android.text.TextUtils.isEmpty(r5)
            if (r2 == 0) goto L_0x00a9
            goto L_0x00ae
        L_0x00a9:
            io.dcloud.feature.ui.c$i r7 = new io.dcloud.feature.ui.c$i
            r7.<init>(r1, r5)
        L_0x00ae:
            r16 = r7
            r8.draw(r9, r10, r11, r12, r13, r14, r15, r16)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.c.c(io.dcloud.common.DHInterface.IWebview, org.json.JSONArray, io.dcloud.feature.ui.c):void");
    }

    /* access modifiers changed from: package-private */
    public boolean b(String str, String str2, boolean z2) {
        ArrayList<b> arrayList = this.N;
        if (arrayList != null) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                b bVar = this.N.get(size);
                if (bVar instanceof c) {
                    c cVar = (c) bVar;
                    if (cVar.G && cVar.b(str, str2, z2)) {
                        return true;
                    }
                }
            }
        }
        if (!a(str) || !a(str, str2, z2)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean c(String str, String str2, boolean z2) {
        ArrayList<b> arrayList = this.N;
        if (arrayList != null) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                b bVar = this.N.get(size);
                if (bVar instanceof c) {
                    c cVar = (c) bVar;
                    if (cVar.G && cVar.c(str, str2, z2)) {
                        return true;
                    }
                }
            }
        }
        if (a(str)) {
            return a(str, str2, z2);
        }
        IFrameView iFrameView = this.z;
        if (iFrameView instanceof AdaFrameView) {
            String str3 = ((AdaFrameView) iFrameView).obtainFrameOptions().historyBack;
            if ((str3.equals("backButton") || str3.equals("all")) && this.z.obtainWebView() != null && this.z.obtainWebView().canGoBack()) {
                this.z.obtainWebView().goBackOrForward(-1);
                return true;
            }
        }
        if ("hide".equals(d().obtainFrameOptions().backButtonAutoControl)) {
            d(this.z.obtainWebView(), JSONUtil.createJSONArray("['auto',null]"), this);
            return true;
        } else if ("quit".equals(d().obtainFrameOptions().backButtonAutoControl)) {
            this.j.d.processEvent(IMgr.MgrType.WindowMgr, 20, this.z.obtainApp());
            return false;
        } else if (!AbsoluteConst.EVENTS_CLOSE.equals(d().obtainFrameOptions().backButtonAutoControl)) {
            return false;
        } else {
            b(this.z.obtainWebView(), JSONUtil.createJSONArray("['auto',null]"), this);
            return true;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean b(String str) {
        ArrayList<b> arrayList = this.N;
        if (arrayList != null) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                b bVar = this.N.get(size);
                if ((bVar instanceof c) && ((c) bVar).a(str)) {
                    return true;
                }
            }
        }
        return a(str);
    }

    private void a(b bVar, c cVar) {
        if (BaseInfo.isBase(bVar.a()) && !this.A.startsWith(DeviceInfo.HTTP_PROTOCOL) && !this.A.startsWith(DeviceInfo.HTTPS_PROTOCOL) && !cVar.A.startsWith(DeviceInfo.HTTP_PROTOCOL) && !cVar.A.startsWith(DeviceInfo.HTTPS_PROTOCOL) && !TextUtils.isEmpty(this.A) && !TextUtils.isEmpty(cVar.A)) {
            Log.i(AbsoluteConst.HBUILDER_TAG, StringUtil.format(AbsoluteConst.FILIATIONLOG, e.c(WebResUtil.getHBuilderPrintUrl(cVar.r().obtainApp().convert2RelPath(r().obtainUrl()))), e.c(WebResUtil.getHBuilderPrintUrl(cVar.r().obtainUrl()))));
        }
    }

    private static void a(IContainerView iContainerView, AdaFrameItem adaFrameItem, ViewGroup.LayoutParams layoutParams, int i2, int i3, int i4, int i5) {
        ViewOptions obtainFrameOptions = adaFrameItem.obtainFrameOptions();
        obtainFrameOptions.left = i2;
        obtainFrameOptions.top = i3;
        obtainFrameOptions.width = i4;
        obtainFrameOptions.height = i5;
        obtainFrameOptions.commitUpdate2JSONObject();
        AdaFrameView adaFrameView = (AdaFrameView) adaFrameItem;
        adaFrameView.isChildOfFrameView = true;
        View obtainMainView = adaFrameItem.obtainMainView();
        if (adaFrameView.obtainWebView().isUniWebView()) {
            obtainMainView.layout(0, 0, i4, i5);
        } else {
            obtainMainView.setTop(0);
            obtainMainView.setLeft(0);
        }
        ViewHelper.setX(obtainMainView, 0.0f);
        ViewHelper.setY(obtainMainView, 0.0f);
        iContainerView.addFrameItem(adaFrameItem, AdaFrameItem.LayoutParamsUtil.createLayoutParams(i2, i3, i4, i5));
        Logger.d(Logger.VIEW_VISIBLE_TAG, "appendNWindow Y=" + ViewHelper.getY(obtainMainView));
    }

    public static synchronized void a(String str, Object obj, List<c> list, c cVar) {
        synchronized (c.class) {
            for (c r : list) {
                JSUtil.broadcastWebviewEvent(r.r(), cVar.l, str, JSONUtil.toJSONableString(String.valueOf(obj)));
            }
            if (!list.contains(cVar)) {
                JSUtil.broadcastWebviewEvent(cVar.r(), cVar.l, str, JSONUtil.toJSONableString(String.valueOf(obj)));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void e() {
        ArrayList<c> arrayList;
        i();
        c cVar = this.X;
        if (!(cVar == null || (arrayList = cVar.Y) == null)) {
            arrayList.remove(this);
        }
        this.X = null;
        this.h = null;
        ArrayList<b> arrayList2 = this.N;
        if (arrayList2 != null) {
            Iterator<b> it = arrayList2.iterator();
            while (it.hasNext()) {
                it.next().e();
            }
            this.N.clear();
            this.N = null;
        }
        this.Q = null;
        this.P = null;
        this.R = null;
        this.S = null;
        this.b0 = 150;
        this.p = null;
        HashMap<String, String> hashMap = this.i;
        if (hashMap != null) {
            hashMap.clear();
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v32, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v2, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:265:0x0416, code lost:
        r6 = "";
        r18 = io.dcloud.common.constant.AbsoluteConst.EVENTS_OVERRIDE_URL_LOADING;
        r19 = io.dcloud.common.constant.AbsoluteConst.EVENTS_TITLE_UPDATE;
        r26 = io.dcloud.common.constant.AbsoluteConst.BOUNCE_REGISTER;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:266:0x0429, code lost:
        switch(r3) {
            case 0: goto L_0x0d5e;
            case 1: goto L_0x0d51;
            case 2: goto L_0x0d46;
            case 3: goto L_0x0d34;
            case 4: goto L_0x0d27;
            case 5: goto L_0x0d17;
            case 6: goto L_0x0cd5;
            case 7: goto L_0x0cd0;
            case 8: goto L_0x0cd0;
            case 9: goto L_0x0cbc;
            case 10: goto L_0x0cab;
            case 11: goto L_0x0c82;
            case 12: goto L_0x0c64;
            case 13: goto L_0x0c4b;
            case 14: goto L_0x0c3b;
            case 15: goto L_0x0c22;
            case 16: goto L_0x0c1d;
            case 17: goto L_0x0c18;
            case 18: goto L_0x0c13;
            case 19: goto L_0x0bef;
            case 20: goto L_0x0ba8;
            case 21: goto L_0x0b99;
            case 22: goto L_0x0b8a;
            case 23: goto L_0x0b6c;
            case 24: goto L_0x0b4e;
            case 25: goto L_0x0b43;
            case 26: goto L_0x0a96;
            case 27: goto L_0x0a8b;
            case 28: goto L_0x0a7c;
            case 29: goto L_0x0a71;
            case 30: goto L_0x0a4c;
            case 31: goto L_0x0a35;
            case 32: goto L_0x0a10;
            case 33: goto L_0x09ec;
            case 34: goto L_0x09c7;
            case 35: goto L_0x09a3;
            case 36: goto L_0x097f;
            case 37: goto L_0x095b;
            case 38: goto L_0x0937;
            case 39: goto L_0x090c;
            case 40: goto L_0x08fe;
            case 41: goto L_0x08c2;
            case 42: goto L_0x08b3;
            case 43: goto L_0x08a7;
            case 44: goto L_0x0898;
            case 45: goto L_0x0873;
            case 46: goto L_0x0865;
            case 47: goto L_0x0855;
            case 48: goto L_0x083a;
            case 49: goto L_0x0831;
            case 50: goto L_0x0822;
            case 51: goto L_0x0807;
            case 52: goto L_0x0800;
            case 53: goto L_0x07c7;
            case 54: goto L_0x07bd;
            case 55: goto L_0x0798;
            case 56: goto L_0x0777;
            case 57: goto L_0x06f7;
            case 58: goto L_0x06f2;
            case 59: goto L_0x06e6;
            case 60: goto L_0x06e1;
            case 61: goto L_0x06d1;
            case 62: goto L_0x06b2;
            case 63: goto L_0x069a;
            case 64: goto L_0x064e;
            case 65: goto L_0x064e;
            case 66: goto L_0x05a3;
            case 67: goto L_0x0594;
            case 68: goto L_0x057d;
            case 69: goto L_0x0564;
            case 70: goto L_0x055f;
            case 71: goto L_0x054e;
            case 72: goto L_0x0547;
            case 73: goto L_0x052c;
            case 74: goto L_0x0518;
            case 75: goto L_0x04cb;
            case 76: goto L_0x04b9;
            case 77: goto L_0x04a9;
            case 78: goto L_0x0492;
            case 79: goto L_0x0483;
            case 80: goto L_0x046c;
            case 81: goto L_0x0459;
            case 82: goto L_0x044d;
            case 83: goto L_0x0441;
            case 84: goto L_0x042f;
            default: goto L_0x042c;
        };
     */
    /* JADX WARNING: Code restructure failed: missing block: B:267:0x042c, code lost:
        r6 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:269:?, code lost:
        r2 = r1.z.obtainWebView();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:270:0x0435, code lost:
        if (r2 == null) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:271:0x0437, code lost:
        r2 = io.dcloud.common.util.JSUtil.wrapJsVar(r2.isPause());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:272:0x0441, code lost:
        r2 = r1.z.obtainWebView();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:273:0x0447, code lost:
        if (r2 == null) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:274:0x0449, code lost:
        r2.resume();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:275:0x044d, code lost:
        r2 = r1.z.obtainWebView();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:276:0x0453, code lost:
        if (r2 == null) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:277:0x0455, code lost:
        r2.pause();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:278:0x0459, code lost:
        io.dcloud.common.core.ui.DCKeyboardManager.getInstance().setHTMLInputRect(r1.z.obtainWebView(), r4.getString(0));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:279:0x046c, code lost:
        r2 = r1.z.obtainWebView().getWebviewProperty("getShareOptions");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:280:0x047a, code lost:
        if (android.text.TextUtils.isEmpty(r2) != false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:281:0x047c, code lost:
        r2 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r2, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:282:0x0483, code lost:
        r1.z.obtainWebView().setWebviewProperty("setShareOptions", r4.getString(0));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:283:0x0492, code lost:
        r2 = r1.z.obtainWebView().getWebviewProperty("getFavoriteOptions");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:284:0x04a0, code lost:
        if (android.text.TextUtils.isEmpty(r2) != false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:285:0x04a2, code lost:
        r2 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r2, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:286:0x04a9, code lost:
        r1.z.obtainWebView().setWebviewProperty("setFavoriteOptions", r4.getString(0));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:287:0x04b9, code lost:
        r2 = r1.z;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:288:0x04bd, code lost:
        if ((r2 instanceof io.dcloud.common.adapter.ui.AdaFrameView) == false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:289:0x04bf, code lost:
        io.dcloud.common.util.SubNViewsUtil.updateSubNViews((io.dcloud.common.adapter.ui.AdaFrameView) r2, r4.getJSONArray(0));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:290:0x04cb, code lost:
        r2 = r1.z;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:291:0x04cf, code lost:
        if ((r2 instanceof io.dcloud.common.adapter.ui.AdaFrameView) == false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:292:0x04d1, code lost:
        r2 = (io.dcloud.common.adapter.ui.AdaFrameView) r2;
        r3 = r2.mChildNativeViewList;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:293:0x04d5, code lost:
        if (r3 == null) goto L_0x04ff;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:294:0x04d7, code lost:
        r2 = new org.json.JSONArray();
        r3 = r3.iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:296:0x04e4, code lost:
        if (r3.hasNext() == false) goto L_0x04f4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:297:0x04e6, code lost:
        r2.put(r3.next().toJSON());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:298:0x04f4, code lost:
        r2 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r2.toString(), false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:300:0x0505, code lost:
        if (r2.obtainFrameOptions().mSubNViews == null) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:301:0x0507, code lost:
        r2 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r2.obtainFrameOptions().mSubNViews.toString(), false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:302:0x0518, code lost:
        r1.z.interceptTouchEvent(java.lang.Boolean.valueOf(io.dcloud.common.util.JSONUtil.getString(r4, 0)).booleanValue());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:303:0x052c, code lost:
        r2 = io.dcloud.common.util.JSONUtil.getJSONObject(r4, 0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:306:?, code lost:
        r1.a0 = r2.optString("type", r1.a0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:309:?, code lost:
        r1.b0 = r2.optInt("interval", r1.b0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:310:0x0547, code lost:
        r1.z.restore();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:311:0x054e, code lost:
        r1.z.animate(r2, io.dcloud.common.util.JSONUtil.getString(r4, 0), io.dcloud.common.util.JSONUtil.getString(r4, 1));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:312:0x055f, code lost:
        a(r2, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:313:0x0564, code lost:
        r2 = io.dcloud.common.util.JSONUtil.getString(r4, 1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:314:0x056f, code lost:
        if (r1.j.a(r2) != null) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:316:0x0573, code lost:
        r4 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:320:?, code lost:
        a((io.dcloud.feature.ui.b) r1.j.a(r2, r2, (java.lang.String) null), r1, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:321:0x057d, code lost:
        r2 = io.dcloud.common.util.JSONUtil.getString(r4, 0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:322:0x0586, code lost:
        if (io.dcloud.common.util.PdrUtil.isEmpty(r2) != false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:324:0x058e, code lost:
        r4 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:326:?, code lost:
        r1.z.obtainWebView().setCssFile((java.lang.String) null, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:328:?, code lost:
        r1.z.obtainWebView().setWebviewProperty("needTouchEvent", io.dcloud.common.constant.AbsoluteConst.TRUE);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:329:0x059f, code lost:
        return io.dcloud.common.constant.AbsoluteConst.FALSE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:332:?, code lost:
        r5 = io.dcloud.common.util.JSONUtil.getJSONObject(r4, 0);
        r7 = io.dcloud.common.util.JSONUtil.getJSONObject(r4, 1);
        r8 = io.dcloud.common.util.JSONUtil.getString(r4, 2);
        r4 = io.dcloud.common.util.JSONUtil.getString(r4, 3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:333:0x05b7, code lost:
        if (r5 == null) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:335:0x05c3, code lost:
        if (android.text.TextUtils.isEmpty(io.dcloud.common.util.JSONUtil.getString(r5, "direction")) != false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:337:0x05cf, code lost:
        if (android.text.TextUtils.isEmpty(io.dcloud.common.util.JSONUtil.getString(r5, "moveMode")) != false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:338:0x05d1, code lost:
        r3 = d().obtainFrameOptions();
        r9 = io.dcloud.common.util.JSONUtil.getString(r7, com.taobao.weex.ui.component.WXBasicComponentType.VIEW);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:339:0x05e4, code lost:
        if (android.text.TextUtils.isEmpty(r9) != false) goto L_0x05ed;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:340:0x05e6, code lost:
        r6 = r1.j.a(r6, r9, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:341:0x05ed, code lost:
        r6 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:342:0x05ee, code lost:
        if (r6 != null) goto L_0x0622;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:343:0x05f0, code lost:
        r2 = r1.j.d.processEvent(io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr, 10, new java.lang.Object[]{r2, "nativeobj", "getNativeView", new java.lang.Object[]{r1.z, r9}});
     */
    /* JADX WARNING: Code restructure failed: missing block: B:344:0x0617, code lost:
        if (r2 == null) goto L_0x0622;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:346:0x061b, code lost:
        if ((r2 instanceof android.view.View) == false) goto L_0x0622;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:347:0x061d, code lost:
        r23 = (android.view.View) r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:348:0x0622, code lost:
        r23 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:349:0x0624, code lost:
        r2 = r1.j.a(r8, r8, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:350:0x062a, code lost:
        if (r6 != null) goto L_0x062f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:351:0x062c, code lost:
        r20 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:352:0x062f, code lost:
        r20 = r6.z;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:353:0x0633, code lost:
        if (r2 != null) goto L_0x0638;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:354:0x0635, code lost:
        r21 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:355:0x0638, code lost:
        r21 = r2.z;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:356:0x063c, code lost:
        if (r4 == null) goto L_0x0641;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:357:0x063e, code lost:
        r22 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:358:0x0641, code lost:
        r22 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:359:0x0643, code lost:
        r3.setDragData(r5, r7, r20, r21, r22, r23);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:360:0x064e, code lost:
        r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNViewId(r1.z);
        r2 = r1.j.d.processEvent(io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr, 10, new java.lang.Object[]{r1.z.obtainWebView(), "nativeobj", "getNativeView", new java.lang.Object[]{r1.z, r2}});
     */
    /* JADX WARNING: Code restructure failed: missing block: B:361:0x0681, code lost:
        if (r2 == null) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:363:0x0685, code lost:
        if ((r2 instanceof io.dcloud.common.DHInterface.INativeView) == false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:364:0x0687, code lost:
        r2 = ((io.dcloud.common.DHInterface.INativeView) r2).toJSON();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:365:0x068d, code lost:
        if (r2 == null) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:366:0x068f, code lost:
        r2 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r2.toString(), false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:367:0x069a, code lost:
        r2 = r1.z.obtainWebView();
        r2.setFixBottom((int) (((float) r4.getInt(0)) * r2.getScale()));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:368:0x06b2, code lost:
        r3 = io.dcloud.common.util.JSONUtil.getString(r4, 0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:369:0x06bb, code lost:
        if (io.dcloud.common.util.PdrUtil.isEmpty(r3) != false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:371:0x06cb, code lost:
        r4 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:373:?, code lost:
        r1.z.obtainWebView().setCssFile(r7.convert2LocalFullPath(r25.obtainFullUrl(), r3), (java.lang.String) null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:375:?, code lost:
        r2 = io.dcloud.common.util.JSUtil.wrapJsVar(((io.dcloud.common.adapter.ui.AdaFrameItem) r1.z).obtainFrameOptions().mUseHardwave);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:376:0x06e1, code lost:
        c(r2, r4, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:377:0x06e6, code lost:
        r1.z.clearSnapshot(r4.getString(0));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:378:0x06f2, code lost:
        a(r2, r4, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:379:0x06f7, code lost:
        r5 = io.dcloud.common.util.JSONUtil.getString(r4, 1);
        r4 = io.dcloud.common.util.JSONUtil.getString(r4, 0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:380:0x0705, code lost:
        switch(r4.hashCode()) {
            case -1677935844: goto L_0x0731;
            case -1515621005: goto L_0x0727;
            case -333584256: goto L_0x071d;
            case 2115: goto L_0x0713;
            case 2390711: goto L_0x0709;
            default: goto L_0x0708;
        };
     */
    /* JADX WARNING: Code restructure failed: missing block: B:383:0x070f, code lost:
        if (r4.equals(io.dcloud.common.DHInterface.IFeature.F_MAPS) == false) goto L_0x073b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:384:0x0711, code lost:
        r6 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:386:0x0719, code lost:
        if (r4.equals("Ad") == false) goto L_0x073b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:387:0x071b, code lost:
        r6 = 4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:389:0x0723, code lost:
        if (r4.equals("barcode") == false) goto L_0x073b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:390:0x0725, code lost:
        r6 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:392:0x072d, code lost:
        if (r4.equals("LivePusher") == false) goto L_0x073b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:393:0x072f, code lost:
        r6 = 3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:395:0x0737, code lost:
        if (r4.equals("VideoPlayer") == false) goto L_0x073b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:396:0x0739, code lost:
        r6 = 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:397:0x073b, code lost:
        r6 = 65535;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:398:0x073c, code lost:
        if (r6 == 0) goto L_0x076f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:400:0x073f, code lost:
        if (r6 == 1) goto L_0x0767;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:402:0x0742, code lost:
        if (r6 == 2) goto L_0x075f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:404:0x0745, code lost:
        if (r6 == 3) goto L_0x0757;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:406:0x0748, code lost:
        if (r6 == 4) goto L_0x074f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:407:0x074a, code lost:
        e(r2, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:408:0x074f, code lost:
        a(r2, r5);
        j();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:409:0x0757, code lost:
        c(r2, r5);
        j();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:410:0x075f, code lost:
        f(r2, r5);
        j();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:411:0x0767, code lost:
        b(r2, r5);
        j();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:412:0x076f, code lost:
        d(r2, r5);
        j();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:413:0x0777, code lost:
        r2 = io.dcloud.common.util.JSONUtil.getString(r4, 1);
        r3 = r1.j.a(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:414:0x0782, code lost:
        if (r3 != null) goto L_0x078b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:416:0x0786, code lost:
        r4 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:418:?, code lost:
        r3 = r1.j.a(r2, r2, (java.lang.String) null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:421:0x078f, code lost:
        if (b(r3) != false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:422:0x0791, code lost:
        if (r3 == null) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:423:0x0793, code lost:
        a(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:424:0x0798, code lost:
        r2 = io.dcloud.common.util.JSONUtil.getString(r4, 0);
        r3 = r1.j.a(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:425:0x07a3, code lost:
        if (r3 != null) goto L_0x07b2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:427:0x07a7, code lost:
        r4 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:429:?, code lost:
        r3 = r1.j.a(r2, r2, (java.lang.String) null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:430:0x07ad, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:431:0x07ae, code lost:
        r2 = r0;
        r6 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:434:0x07b6, code lost:
        if (b(r3) == false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:435:0x07b8, code lost:
        c(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:436:0x07bd, code lost:
        h(r2, io.dcloud.common.util.JSONUtil.getString(r4, 1));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:437:0x07c7, code lost:
        r2 = io.dcloud.common.util.JSONUtil.getString(r4, 0);
        r3 = io.dcloud.common.util.JSONUtil.getJSONObject(r4, 1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:438:0x07d1, code lost:
        r4 = "text/html";
        r5 = "utf-8";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:439:0x07d5, code lost:
        if (r3 == null) goto L_0x07f5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:442:?, code lost:
        r5 = io.dcloud.common.util.PdrUtil.getNonString(r3.optString("encoding"), r5);
        r4 = io.dcloud.common.util.PdrUtil.getNonString(r3.optString("mimeType"), r4);
        r6 = io.dcloud.common.util.PdrUtil.getNonString(r3.optString("baseURL"), r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:443:0x07f5, code lost:
        r1.z.obtainWebView().loadContentData(r6, r2, r4, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:444:0x0800, code lost:
        r2 = a((java.util.ArrayList) r1.N);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:445:0x0807, code lost:
        r2 = r1.h;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:446:0x0809, code lost:
        if (r2 == null) goto L_0x0810;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:447:0x080b, code lost:
        r2 = r2.h();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:448:0x0810, code lost:
        r2 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(io.dcloud.common.util.StringUtil.format("{'uuid':%s,'id':%s}", com.taobao.weex.common.Constants.Name.UNDEFINED, com.taobao.weex.common.Constants.Name.UNDEFINED), false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:449:0x0822, code lost:
        r2 = r1.h;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:450:0x0824, code lost:
        if (r2 == null) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:452:0x082a, code lost:
        if (r2.b((io.dcloud.feature.ui.b) r1) == false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:453:0x082c, code lost:
        r2.c((io.dcloud.feature.ui.b) r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:454:0x0831, code lost:
        r2 = a((java.util.ArrayList) r1.Y);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:456:0x083a, code lost:
        r2 = r1.X;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:457:0x083c, code lost:
        if (r2 == null) goto L_0x0843;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:458:0x083e, code lost:
        r2 = r2.h();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:459:0x0843, code lost:
        r2 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(io.dcloud.common.util.StringUtil.format("{'uuid':%s,'id':%s}", com.taobao.weex.common.Constants.Name.UNDEFINED, com.taobao.weex.common.Constants.Name.UNDEFINED), false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:460:0x0855, code lost:
        r2 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r1.z.obtainWebView().obtainPageTitle(), true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:461:0x0865, code lost:
        r2 = c(io.dcloud.common.util.JSONUtil.getString(r4, 0)).h();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:462:0x0873, code lost:
        r2 = r4.getBoolean(0);
        r1.I = r2;
        r3 = (io.dcloud.common.adapter.ui.AdaFrameItem) r1.z.obtainWebView();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:463:0x0882, code lost:
        if (r2 == false) goto L_0x0887;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:464:0x0884, code lost:
        r2 = io.dcloud.common.adapter.ui.AdaFrameItem.VISIBLE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:465:0x0887, code lost:
        r2 = io.dcloud.common.adapter.ui.AdaFrameItem.GONE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:466:0x0889, code lost:
        r3.setVisibility(r2);
        r1.z.obtainWebviewParent().setBgcolor(-1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:467:0x0898, code lost:
        r2 = r4.getBoolean(0);
        r1.G = r2;
        r1.z.setVisible(r2, true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:468:0x08a7, code lost:
        r2 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(java.lang.String.valueOf(r1.G), false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:469:0x08b3, code lost:
        b(r4.getString(1), r4.getString(0));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:470:0x08c2, code lost:
        r3 = r4.getString(0);
        a(r4.getString(1), r3, r1.i.get(r25.getWebviewANID()));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:471:0x08e5, code lost:
        if (r1.z.obtainWebView().unReceiveTitle() != false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:472:0x08e7, code lost:
        r2 = r19;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:473:0x08ed, code lost:
        if (r2.equals(r3) == false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:474:0x08ef, code lost:
        onCallBack(r2, r1.z.obtainWebView().getTitle());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:475:0x08fe, code lost:
        r().setAssistantType(r4.getString(0));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:476:0x090c, code lost:
        r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNView(r1.j.d, r(), r1.z, io.dcloud.common.util.TitleNViewUtil.getTitleNViewId(r1.z));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:477:0x0922, code lost:
        if ((r2 instanceof io.dcloud.common.DHInterface.ITitleNView) == false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:478:0x0924, code lost:
        io.dcloud.common.util.TitleNViewUtil.setTitleNViewButtonStyle((io.dcloud.common.DHInterface.ITitleNView) r2, r4.optString(0), r4.optJSONObject(1), r1.z);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:479:0x0937, code lost:
        r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNView(r1.j.d, r(), r1.z, io.dcloud.common.util.TitleNViewUtil.getTitleNViewId(r1.z));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:480:0x094d, code lost:
        if ((r2 instanceof io.dcloud.common.DHInterface.ITitleNView) == false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:481:0x094f, code lost:
        r2 = io.dcloud.common.util.JSUtil.wrapJsVar(io.dcloud.common.util.TitleNViewUtil.getTitleNViewSearchInputText((io.dcloud.common.DHInterface.ITitleNView) r2));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:482:0x095b, code lost:
        r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNView(r1.j.d, r(), r1.z, io.dcloud.common.util.TitleNViewUtil.getTitleNViewId(r1.z));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:483:0x0971, code lost:
        if ((r2 instanceof io.dcloud.common.DHInterface.ITitleNView) == false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:484:0x0973, code lost:
        io.dcloud.common.util.TitleNViewUtil.setTitleNViewSearchInputText((io.dcloud.common.DHInterface.ITitleNView) r2, r4.optString(0));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:485:0x097f, code lost:
        r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNView(r1.j.d, r(), r1.z, io.dcloud.common.util.TitleNViewUtil.getTitleNViewId(r1.z));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:486:0x0995, code lost:
        if ((r2 instanceof io.dcloud.common.DHInterface.ITitleNView) == false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:487:0x0997, code lost:
        io.dcloud.common.util.TitleNViewUtil.setTitleNViewSearchInputFocus((io.dcloud.common.DHInterface.ITitleNView) r2, r4.optString(0));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:488:0x09a3, code lost:
        r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNView(r1.j.d, r(), r1.z, io.dcloud.common.util.TitleNViewUtil.getTitleNViewId(r1.z));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:489:0x09b9, code lost:
        if ((r2 instanceof io.dcloud.common.DHInterface.ITitleNView) == false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:490:0x09bb, code lost:
        io.dcloud.common.util.TitleNViewUtil.titleNViewButtonRedDot((io.dcloud.common.DHInterface.ITitleNView) r2, r4.optJSONObject(0), false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:491:0x09c7, code lost:
        r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNView(r1.j.d, r(), r1.z, io.dcloud.common.util.TitleNViewUtil.getTitleNViewId(r1.z));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:492:0x09dd, code lost:
        if ((r2 instanceof io.dcloud.common.DHInterface.ITitleNView) == false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:493:0x09df, code lost:
        io.dcloud.common.util.TitleNViewUtil.titleNViewButtonRedDot((io.dcloud.common.DHInterface.ITitleNView) r2, r4.optJSONObject(0), true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:494:0x09ec, code lost:
        r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNView(r1.j.d, r(), r1.z, io.dcloud.common.util.TitleNViewUtil.getTitleNViewId(r1.z));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:495:0x0a02, code lost:
        if ((r2 instanceof io.dcloud.common.DHInterface.ITitleNView) == false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:496:0x0a04, code lost:
        io.dcloud.common.util.TitleNViewUtil.titleNViewButtonBadge((io.dcloud.common.DHInterface.ITitleNView) r2, r4.optJSONObject(0), false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:497:0x0a10, code lost:
        r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNView(r1.j.d, r(), r1.z, io.dcloud.common.util.TitleNViewUtil.getTitleNViewId(r1.z));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:498:0x0a26, code lost:
        if ((r2 instanceof io.dcloud.common.DHInterface.ITitleNView) == false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:499:0x0a28, code lost:
        io.dcloud.common.util.TitleNViewUtil.titleNViewButtonBadge((io.dcloud.common.DHInterface.ITitleNView) r2, r4.optJSONObject(0), true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:500:0x0a35, code lost:
        r2 = io.dcloud.common.util.JSONUtil.getString(r4, 0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:501:0x0a3e, code lost:
        if (io.dcloud.common.util.PdrUtil.isEmpty(r2) != false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:502:0x0a40, code lost:
        io.dcloud.common.adapter.util.DeviceInfo.isVolumeButtonEnabled = java.lang.Boolean.valueOf(r2).booleanValue();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:503:0x0a4c, code lost:
        r2 = r1.z;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:504:0x0a4e, code lost:
        if (r2 == null) goto L_0x0a65;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:506:0x0a58, code lost:
        if (r2.obtainWebView().isUniService() == false) goto L_0x0a65;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:507:0x0a5a, code lost:
        io.dcloud.common.adapter.util.DeviceInfo.showIME(r1.z.obtainMainView());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:508:0x0a65, code lost:
        io.dcloud.common.adapter.util.DeviceInfo.showIME(r1.z.obtainMainView(), true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:509:0x0a71, code lost:
        io.dcloud.common.adapter.util.DeviceInfo.hideIME(r1.z.obtainMainView());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:510:0x0a7c, code lost:
        a(r1, io.dcloud.common.util.PdrUtil.parseBoolean(io.dcloud.common.util.JSONUtil.getString(r4, 0), true, false));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:511:0x0a8b, code lost:
        r1.z.obtainWebView().stopLoading();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:512:0x0a96, code lost:
        r2 = r1.z.obtainWebView();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:513:0x0a9c, code lost:
        if (r2 == null) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:514:0x0a9e, code lost:
        r3 = r2.obtainUrl();
        r5 = io.dcloud.common.util.JSONUtil.getString(r4, 0);
        r4 = io.dcloud.common.util.JSONUtil.getJSONObject(r4, 2);
        r6 = r2.obtainFrameView().obtainApp().convert2WebviewFullPath(r2.obtainFullUrl(), r5);
        io.dcloud.common.adapter.util.Logger.d("NWindow.load " + r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:515:0x0ad0, code lost:
        if (r4 == null) goto L_0x0af9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:517:0x0ad6, code lost:
        if (r4.length() <= 0) goto L_0x0af9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:518:0x0ad8, code lost:
        r7 = new java.util.HashMap(r4.length());
        r8 = r4.keys();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:520:0x0ae9, code lost:
        if (r8.hasNext() == false) goto L_0x0afa;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:521:0x0aeb, code lost:
        r9 = r8.next();
        r7.put(r9, r4.optString(r9));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:522:0x0af9, code lost:
        r7 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:524:0x0afc, code lost:
        if ((r2 instanceof io.dcloud.common.adapter.ui.AdaWebview) == false) goto L_0x0b29;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:526:0x0b05, code lost:
        if (((io.dcloud.common.adapter.ui.AdaWebview) r2).checkOverrideUrl(r6) == false) goto L_0x0b29;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:527:0x0b07, code lost:
        r2 = ((io.dcloud.common.adapter.ui.AdaWebview) r2).mFrameView;
        r2.dispatchFrameViewEvents(r18, "{url:'" + r6 + "'}");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:528:0x0b28, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:529:0x0b29, code lost:
        r2.setLoadURLHeads(r5, r7);
        r1.z.obtainWebView().setOriginalUrl(r5);
        r1.z.obtainWebView().reload(r6);
        a(r1, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:530:0x0b43, code lost:
        r1.z.obtainWebView().clearHistory();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:531:0x0b4e, code lost:
        io.dcloud.common.util.Deprecated_JSUtil.execCallback(r25, io.dcloud.common.util.JSONUtil.getString(r4, 0), java.lang.String.valueOf(r1.z.obtainWebView().canGoForward()), io.dcloud.common.util.JSUtil.OK, true, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:532:0x0b6c, code lost:
        io.dcloud.common.util.Deprecated_JSUtil.execCallback(r25, io.dcloud.common.util.JSONUtil.getString(r4, 0), java.lang.String.valueOf(r1.z.obtainWebView().canGoBack()), io.dcloud.common.util.JSUtil.OK, true, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:533:0x0b8a, code lost:
        r2 = r1.z.obtainWebView();
        r2.stopLoading();
        r2.goBackOrForward(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:534:0x0b99, code lost:
        r2 = r1.z.obtainWebView();
        r2.stopLoading();
        r2.goBackOrForward(-1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:536:0x0bbf, code lost:
        if (android.os.Looper.getMainLooper().getThread().getId() != java.lang.Thread.currentThread().getId()) goto L_0x0bc3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:537:0x0bc1, code lost:
        r6 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:538:0x0bc3, code lost:
        r6 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:539:0x0bc4, code lost:
        r2 = io.dcloud.common.util.JSONUtil.getString(r4, 0);
        r3 = r1.z.obtainWebView();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:540:0x0bce, code lost:
        if (r6 == false) goto L_0x0bda;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:541:0x0bd0, code lost:
        io.dcloud.common.adapter.util.MessageHandler.post(new io.dcloud.feature.ui.c.C0043c(r1, r3, r2));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:542:0x0bd9, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:543:0x0bda, code lost:
        r2 = io.dcloud.common.adapter.util.MessageHandler.postAndWait(new io.dcloud.feature.ui.c.d(r1, r3, r2));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:544:0x0be3, code lost:
        if (r2 == null) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:545:0x0be5, code lost:
        r2 = io.dcloud.common.util.JSUtil.wrapJsVar(java.lang.String.valueOf(r2));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:546:0x0bef, code lost:
        r3 = io.dcloud.common.util.JSONUtil.getString(r4, 0);
        r5 = r1.z.obtainWebView();
        r4 = io.dcloud.common.util.JSONUtil.getString(r4, 1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:547:0x0c03, code lost:
        if (io.dcloud.common.util.PdrUtil.isEmpty(r4) != false) goto L_0x0c0e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:548:0x0c05, code lost:
        r3 = io.dcloud.common.adapter.ui.ReceiveJSValue.registerCallback(r3, new io.dcloud.feature.ui.c.b(r1, r2, r4));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:549:0x0c0e, code lost:
        r5.evalJS(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:550:0x0c13, code lost:
        b(r2, r4, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:551:0x0c18, code lost:
        a(r2, r4, r1, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:552:0x0c1d, code lost:
        d(r2, r4, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:553:0x0c22, code lost:
        r3 = r4.optJSONObject(0);
        r1.R = r4.optString(1);
        r1.S = r2;
        r1.z.obtainWebView().setOverrideUrlLoadingData(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:554:0x0c3b, code lost:
        r1.z.obtainWebView().setOverrideResourceRequest(r4.optJSONArray(0));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:555:0x0c4b, code lost:
        r3 = r4.optJSONObject(0);
        r1.T = r4.optString(1);
        r1.U = r2;
        r1.z.obtainWebView().setListenResourceLoading(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:556:0x0c64, code lost:
        r1.z.obtainWebView().appendPreloadJsFile(r1.z.obtainApp().convert2AbsFullPath(r25.obtainFullUrl(), io.dcloud.common.util.JSONUtil.getString(r4, 0)));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:557:0x0c82, code lost:
        r5 = io.dcloud.common.util.JSONUtil.getString(r4, 0);
        r3 = r4.optBoolean(1, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:558:0x0c90, code lost:
        if (io.dcloud.common.util.PdrUtil.isEmpty(r5) != false) goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:559:0x0c92, code lost:
        r1.z.obtainWebView().setPreloadJsFile(r1.z.obtainApp().convert2AbsFullPath(r25.obtainFullUrl(), r5), r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:560:0x0cab, code lost:
        r2 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r1.z.obtainWebView().obtainFullUrl(), true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:561:0x0cbc, code lost:
        io.dcloud.common.util.Deprecated_JSUtil.execCallback(r25, io.dcloud.common.util.JSONUtil.getString(r4, 0), n(), io.dcloud.common.util.JSUtil.OK, true, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:562:0x0cd0, code lost:
        e(r2, r4, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:563:0x0cd5, code lost:
        r2 = ((io.dcloud.common.adapter.ui.AdaFrameItem) r1.z).obtainFrameOptions();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:564:0x0ce1, code lost:
        if (r2.hasBackground() == false) goto L_0x0ced;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:565:0x0ce3, code lost:
        r2 = r1.z.obtainWebviewParent().obtainFrameOptions();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:566:0x0ced, code lost:
        r3 = new org.json.JSONObject(r2.mJsonViewOption.toString());
        r2 = r16;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:567:0x0cfe, code lost:
        if (r3.has(r2) == false) goto L_0x0d0c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:568:0x0d00, code lost:
        r4 = r3.getString(r2);
        r3.remove(r2);
        r3.put("background", r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:569:0x0d0c, code lost:
        r2 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r3.toString(), false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:570:0x0d17, code lost:
        r().setWebviewProperty(io.dcloud.common.constant.AbsoluteConst.JSON_KEY_BLOCK_NETWORK_IMAGE, io.dcloud.common.util.JSONUtil.getString(r4, 0));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:571:0x0d27, code lost:
        r1.z.obtainWebView().endWebViewEvent(r26);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:572:0x0d34, code lost:
        r2 = io.dcloud.common.util.JSONUtil.getJSONObject(r4, 0);
        r1.z.obtainWebView().setWebViewEvent(r26, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:573:0x0d46, code lost:
        r1.z.obtainWebView().endWebViewEvent(io.dcloud.common.constant.AbsoluteConst.PULL_DOWN_REFRESH);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:576:0x0d59, code lost:
        r6 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:578:?, code lost:
        r1.z.obtainWebView().setWebViewEvent(io.dcloud.common.constant.AbsoluteConst.PULL_REFRESH_BEGIN, (java.lang.Object) null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:579:0x0d5e, code lost:
        r6 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:581:?, code lost:
        io.dcloud.common.adapter.util.Logger.d(io.dcloud.common.adapter.util.Logger.VIEW_VISIBLE_TAG, "refreshLoadingViewsSize setPullToRefresh args=" + r4);
        r2 = io.dcloud.common.util.JSONUtil.getJSONObject(r4, 0);
        r3 = io.dcloud.common.util.JSONUtil.getString(r4, 1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:582:0x0d83, code lost:
        if (io.dcloud.common.util.PdrUtil.isEmpty(r3) != false) goto L_0x0d87;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:583:0x0d85, code lost:
        r1.V = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:584:0x0d87, code lost:
        r1.z.obtainWebView().setWebViewEvent(io.dcloud.common.constant.AbsoluteConst.PULL_DOWN_REFRESH, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:585:0x0d91, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:594:?, code lost:
        return r2;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:432:0x07b2=Splitter:B:432:0x07b2, B:419:0x078b=Splitter:B:419:0x078b} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String a(io.dcloud.common.DHInterface.IWebview r25, java.lang.String r26, org.json.JSONArray r27) {
        /*
            r24 = this;
            r1 = r24
            r2 = r25
            r3 = r26
            r4 = r27
            java.lang.String r5 = "titleUpdate"
            java.lang.String r6 = "tabBGColor"
            io.dcloud.common.DHInterface.IFrameView r7 = r25.obtainFrameView()     // Catch:{ Exception -> 0x0d93 }
            io.dcloud.common.DHInterface.IApp r7 = r7.obtainApp()     // Catch:{ Exception -> 0x0d93 }
            java.lang.String r9 = r7.obtainAppId()     // Catch:{ Exception -> 0x0d93 }
            int r10 = r26.hashCode()     // Catch:{ Exception -> 0x0d93 }
            java.lang.String r11 = "needTouchEvent"
            java.lang.String r12 = "getFavoriteOptions"
            java.lang.String r13 = "setShareOptions"
            java.lang.String r14 = "overrideUrlLoading"
            java.lang.String r15 = "getShareOptions"
            java.lang.String r8 = "setFavoriteOptions"
            r16 = r6
            switch(r10) {
                case -2087705423: goto L_0x0405;
                case -2081275691: goto L_0x03fa;
                case -2051330937: goto L_0x03ef;
                case -2018969440: goto L_0x03e6;
                case -1815848150: goto L_0x03db;
                case -1679210541: goto L_0x03d0;
                case -1525164844: goto L_0x03c5;
                case -1476587689: goto L_0x03ba;
                case -1428651141: goto L_0x03af;
                case -1411068134: goto L_0x03a3;
                case -1389361719: goto L_0x0397;
                case -1295077293: goto L_0x038b;
                case -1291451675: goto L_0x037f;
                case -1278805514: goto L_0x0373;
                case -1263963897: goto L_0x0367;
                case -1263826761: goto L_0x035c;
                case -1249348039: goto L_0x0350;
                case -1205263208: goto L_0x0344;
                case -1199488876: goto L_0x0338;
                case -1199260532: goto L_0x032c;
                case -1183202654: goto L_0x0320;
                case -1093745411: goto L_0x0314;
                case -1068341284: goto L_0x0308;
                case -1010579351: goto L_0x02fc;
                case -1010579337: goto L_0x02f0;
                case -995424086: goto L_0x02e4;
                case -934641255: goto L_0x02d8;
                case -934610812: goto L_0x02cc;
                case -934426579: goto L_0x02c0;
                case -898815851: goto L_0x02b4;
                case -854558288: goto L_0x02a8;
                case -677145915: goto L_0x029c;
                case -625809843: goto L_0x0290;
                case -566318518: goto L_0x0284;
                case -541487286: goto L_0x0278;
                case -481402894: goto L_0x026c;
                case -453356751: goto L_0x0260;
                case -410173765: goto L_0x0254;
                case -400905144: goto L_0x0247;
                case -386427104: goto L_0x023b;
                case -252003491: goto L_0x022f;
                case -251589874: goto L_0x0223;
                case -155575552: goto L_0x0217;
                case -113035288: goto L_0x020b;
                case -41183179: goto L_0x0201;
                case -25924366: goto L_0x01f6;
                case 3015911: goto L_0x01ea;
                case 3091764: goto L_0x01de;
                case 3091780: goto L_0x01d2;
                case 3202370: goto L_0x01c6;
                case 3327206: goto L_0x01ba;
                case 3529469: goto L_0x01ae;
                case 3540994: goto L_0x01a2;
                case 18100665: goto L_0x0198;
                case 94746189: goto L_0x018c;
                case 94756344: goto L_0x0180;
                case 98192778: goto L_0x0175;
                case 106440182: goto L_0x0169;
                case 282257047: goto L_0x015d;
                case 387104256: goto L_0x0152;
                case 471261047: goto L_0x0147;
                case 509226590: goto L_0x013b;
                case 549228759: goto L_0x012f;
                case 648802871: goto L_0x0123;
                case 685878123: goto L_0x0118;
                case 770098485: goto L_0x010c;
                case 869569345: goto L_0x0102;
                case 1081068728: goto L_0x00f7;
                case 1192599283: goto L_0x00eb;
                case 1295982686: goto L_0x00df;
                case 1341702384: goto L_0x00d3;
                case 1348313218: goto L_0x00c7;
                case 1355964204: goto L_0x00bd;
                case 1404493423: goto L_0x00b1;
                case 1518415382: goto L_0x00a5;
                case 1566068146: goto L_0x0099;
                case 1647492569: goto L_0x008e;
                case 1659526655: goto L_0x0082;
                case 1747355346: goto L_0x0076;
                case 1845118384: goto L_0x006a;
                case 1872589777: goto L_0x0060;
                case 1939606683: goto L_0x0053;
                case 1966196898: goto L_0x0047;
                case 1992686733: goto L_0x003b;
                case 2067845868: goto L_0x002f;
                default: goto L_0x002d;
            }
        L_0x002d:
            goto L_0x0415
        L_0x002f:
            java.lang.String r10 = "isPause"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 84
            goto L_0x0416
        L_0x003b:
            java.lang.String r10 = "getMetrics"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 9
            goto L_0x0416
        L_0x0047:
            java.lang.String r10 = "getTitle"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 47
            goto L_0x0416
        L_0x0053:
            java.lang.String r10 = "webview_animate"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 71
            goto L_0x0416
        L_0x0060:
            boolean r3 = r3.equals(r11)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 67
            goto L_0x0416
        L_0x006a:
            java.lang.String r10 = "loadData"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 53
            goto L_0x0416
        L_0x0076:
            java.lang.String r10 = "setTitleNViewSearchInputText"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 37
            goto L_0x0416
        L_0x0082:
            java.lang.String r10 = "children"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 52
            goto L_0x0416
        L_0x008e:
            java.lang.String r10 = "setPullToRefresh"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 0
            goto L_0x0416
        L_0x0099:
            java.lang.String r10 = "removeTitleNViewButtonBadge"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 33
            goto L_0x0416
        L_0x00a5:
            java.lang.String r10 = "appendNativeView"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 57
            goto L_0x0416
        L_0x00b1:
            java.lang.String r10 = "setStyle"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 8
            goto L_0x0416
        L_0x00bd:
            boolean r3 = r3.equals(r12)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 78
            goto L_0x0416
        L_0x00c7:
            java.lang.String r10 = "showTitleNViewButtonRedDot"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 34
            goto L_0x0416
        L_0x00d3:
            java.lang.String r10 = "findViewById"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 46
            goto L_0x0416
        L_0x00df:
            java.lang.String r10 = "setFixBottom"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 63
            goto L_0x0416
        L_0x00eb:
            java.lang.String r10 = "setVolumeButtonEnabled"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 31
            goto L_0x0416
        L_0x00f7:
            java.lang.String r10 = "setBlockNetworkImage"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 5
            goto L_0x0416
        L_0x0102:
            boolean r3 = r3.equals(r13)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 79
            goto L_0x0416
        L_0x010c:
            java.lang.String r10 = "overrideResourceRequest"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 14
            goto L_0x0416
        L_0x0118:
            java.lang.String r10 = "getOption"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 6
            goto L_0x0416
        L_0x0123:
            java.lang.String r10 = "updateSubNViews"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 76
            goto L_0x0416
        L_0x012f:
            java.lang.String r10 = "canBack"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 23
            goto L_0x0416
        L_0x013b:
            java.lang.String r10 = "getTitleNViewSearchInputText"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 38
            goto L_0x0416
        L_0x0147:
            java.lang.String r10 = "setOption"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 7
            goto L_0x0416
        L_0x0152:
            java.lang.String r10 = "endPullToRefresh"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 2
            goto L_0x0416
        L_0x015d:
            java.lang.String r10 = "showBehind"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 69
            goto L_0x0416
        L_0x0169:
            java.lang.String r10 = "pause"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 82
            goto L_0x0416
        L_0x0175:
            java.lang.String r10 = "setBounce"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 3
            goto L_0x0416
        L_0x0180:
            java.lang.String r10 = "close"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 18
            goto L_0x0416
        L_0x018c:
            java.lang.String r10 = "clear"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 25
            goto L_0x0416
        L_0x0198:
            boolean r3 = r3.equals(r14)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 15
            goto L_0x0416
        L_0x01a2:
            java.lang.String r10 = "stop"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 27
            goto L_0x0416
        L_0x01ae:
            java.lang.String r10 = "show"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 17
            goto L_0x0416
        L_0x01ba:
            java.lang.String r10 = "load"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 26
            goto L_0x0416
        L_0x01c6:
            java.lang.String r10 = "hide"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 16
            goto L_0x0416
        L_0x01d2:
            java.lang.String r10 = "draw"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 60
            goto L_0x0416
        L_0x01de:
            java.lang.String r10 = "drag"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 66
            goto L_0x0416
        L_0x01ea:
            java.lang.String r10 = "back"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 21
            goto L_0x0416
        L_0x01f6:
            java.lang.String r10 = "beginPullToRefresh"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 1
            goto L_0x0416
        L_0x0201:
            boolean r3 = r3.equals(r15)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 80
            goto L_0x0416
        L_0x020b:
            java.lang.String r10 = "isVisible"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 43
            goto L_0x0416
        L_0x0217:
            java.lang.String r10 = "removeNativeView"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 54
            goto L_0x0416
        L_0x0223:
            java.lang.String r10 = "setCssText"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 68
            goto L_0x0416
        L_0x022f:
            java.lang.String r10 = "setCssFile"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 62
            goto L_0x0416
        L_0x023b:
            java.lang.String r10 = "evalJSSync"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 20
            goto L_0x0416
        L_0x0247:
            java.lang.String r10 = "webview_restore"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 72
            goto L_0x0416
        L_0x0254:
            java.lang.String r10 = "setContentVisible"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 45
            goto L_0x0416
        L_0x0260:
            java.lang.String r10 = "clearSnapshot"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 59
            goto L_0x0416
        L_0x026c:
            java.lang.String r10 = "hideSoftKeybord"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 29
            goto L_0x0416
        L_0x0278:
            java.lang.String r10 = "removeEventListener"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 42
            goto L_0x0416
        L_0x0284:
            java.lang.String r10 = "getSubNViews"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 75
            goto L_0x0416
        L_0x0290:
            java.lang.String r10 = "addEventListener"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 41
            goto L_0x0416
        L_0x029c:
            java.lang.String r10 = "forward"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 22
            goto L_0x0416
        L_0x02a8:
            java.lang.String r10 = "setVisible"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 44
            goto L_0x0416
        L_0x02b4:
            java.lang.String r10 = "isHardwareAccelerated"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 61
            goto L_0x0416
        L_0x02c0:
            java.lang.String r10 = "resume"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 83
            goto L_0x0416
        L_0x02cc:
            java.lang.String r10 = "remove"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 55
            goto L_0x0416
        L_0x02d8:
            java.lang.String r10 = "reload"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 28
            goto L_0x0416
        L_0x02e4:
            java.lang.String r10 = "parent"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 51
            goto L_0x0416
        L_0x02f0:
            java.lang.String r10 = "opener"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 48
            goto L_0x0416
        L_0x02fc:
            java.lang.String r10 = "opened"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 49
            goto L_0x0416
        L_0x0308:
            java.lang.String r10 = "checkRenderedContent"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 70
            goto L_0x0416
        L_0x0314:
            java.lang.String r10 = "interceptTouchEvent"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 74
            goto L_0x0416
        L_0x0320:
            java.lang.String r10 = "setTitleNViewButtonStyle"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 39
            goto L_0x0416
        L_0x032c:
            java.lang.String r10 = "setPreloadJsFile"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 11
            goto L_0x0416
        L_0x0338:
            java.lang.String r10 = "setTitleNViewButtonBadge"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 32
            goto L_0x0416
        L_0x0344:
            java.lang.String r10 = "removeFromParent"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 50
            goto L_0x0416
        L_0x0350:
            java.lang.String r10 = "getUrl"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 10
            goto L_0x0416
        L_0x035c:
            java.lang.String r10 = "resetBounce"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 4
            goto L_0x0416
        L_0x0367:
            java.lang.String r10 = "hideTitleNViewButtonRedDot"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 35
            goto L_0x0416
        L_0x0373:
            java.lang.String r10 = "setAssistantType"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 40
            goto L_0x0416
        L_0x037f:
            java.lang.String r10 = "evalJS"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 19
            goto L_0x0416
        L_0x038b:
            java.lang.String r10 = "setSoftinputTemporary"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 81
            goto L_0x0416
        L_0x0397:
            java.lang.String r10 = "getNavigationbar"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 64
            goto L_0x0416
        L_0x03a3:
            java.lang.String r10 = "append"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 56
            goto L_0x0416
        L_0x03af:
            java.lang.String r10 = "setRenderedEventOptions"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 73
            goto L_0x0416
        L_0x03ba:
            java.lang.String r10 = "showSoftKeybord"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 30
            goto L_0x0416
        L_0x03c5:
            java.lang.String r10 = "appendPreloadJsFile"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 12
            goto L_0x0416
        L_0x03d0:
            java.lang.String r10 = "setTitleNViewSearchInputFocus"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 36
            goto L_0x0416
        L_0x03db:
            java.lang.String r10 = "captureSnapshot"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 58
            goto L_0x0416
        L_0x03e6:
            boolean r3 = r3.equals(r8)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 77
            goto L_0x0416
        L_0x03ef:
            java.lang.String r10 = "listenResourceLoading"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 13
            goto L_0x0416
        L_0x03fa:
            java.lang.String r10 = "canForward"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 24
            goto L_0x0416
        L_0x0405:
            java.lang.String r10 = "getTitleNView"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0415
            r3 = 65
            goto L_0x0416
        L_0x0410:
            r0 = move-exception
            r2 = r0
            r6 = 0
            goto L_0x0d96
        L_0x0415:
            r3 = -1
        L_0x0416:
            java.lang.String r10 = "nativeobj"
            java.lang.String r6 = ""
            r18 = r14
            java.lang.String r14 = "{'uuid':%s,'id':%s}"
            r19 = r5
            java.lang.String r5 = "bounce_register"
            r26 = r5
            java.lang.String r5 = "pull_down_refresh"
            java.lang.String r20 = "undefined"
            switch(r3) {
                case 0: goto L_0x0d5e;
                case 1: goto L_0x0d51;
                case 2: goto L_0x0d46;
                case 3: goto L_0x0d34;
                case 4: goto L_0x0d27;
                case 5: goto L_0x0d17;
                case 6: goto L_0x0cd5;
                case 7: goto L_0x0cd0;
                case 8: goto L_0x0cd0;
                case 9: goto L_0x0cbc;
                case 10: goto L_0x0cab;
                case 11: goto L_0x0c82;
                case 12: goto L_0x0c64;
                case 13: goto L_0x0c4b;
                case 14: goto L_0x0c3b;
                case 15: goto L_0x0c22;
                case 16: goto L_0x0c1d;
                case 17: goto L_0x0c18;
                case 18: goto L_0x0c13;
                case 19: goto L_0x0bef;
                case 20: goto L_0x0ba8;
                case 21: goto L_0x0b99;
                case 22: goto L_0x0b8a;
                case 23: goto L_0x0b6c;
                case 24: goto L_0x0b4e;
                case 25: goto L_0x0b43;
                case 26: goto L_0x0a96;
                case 27: goto L_0x0a8b;
                case 28: goto L_0x0a7c;
                case 29: goto L_0x0a71;
                case 30: goto L_0x0a4c;
                case 31: goto L_0x0a35;
                case 32: goto L_0x0a10;
                case 33: goto L_0x09ec;
                case 34: goto L_0x09c7;
                case 35: goto L_0x09a3;
                case 36: goto L_0x097f;
                case 37: goto L_0x095b;
                case 38: goto L_0x0937;
                case 39: goto L_0x090c;
                case 40: goto L_0x08fe;
                case 41: goto L_0x08c2;
                case 42: goto L_0x08b3;
                case 43: goto L_0x08a7;
                case 44: goto L_0x0898;
                case 45: goto L_0x0873;
                case 46: goto L_0x0865;
                case 47: goto L_0x0855;
                case 48: goto L_0x083a;
                case 49: goto L_0x0831;
                case 50: goto L_0x0822;
                case 51: goto L_0x0807;
                case 52: goto L_0x0800;
                case 53: goto L_0x07c7;
                case 54: goto L_0x07bd;
                case 55: goto L_0x0798;
                case 56: goto L_0x0777;
                case 57: goto L_0x06f7;
                case 58: goto L_0x06f2;
                case 59: goto L_0x06e6;
                case 60: goto L_0x06e1;
                case 61: goto L_0x06d1;
                case 62: goto L_0x06b2;
                case 63: goto L_0x069a;
                case 64: goto L_0x064e;
                case 65: goto L_0x064e;
                case 66: goto L_0x05a3;
                case 67: goto L_0x0594;
                case 68: goto L_0x057d;
                case 69: goto L_0x0564;
                case 70: goto L_0x055f;
                case 71: goto L_0x054e;
                case 72: goto L_0x0547;
                case 73: goto L_0x052c;
                case 74: goto L_0x0518;
                case 75: goto L_0x04cb;
                case 76: goto L_0x04b9;
                case 77: goto L_0x04a9;
                case 78: goto L_0x0492;
                case 79: goto L_0x0483;
                case 80: goto L_0x046c;
                case 81: goto L_0x0459;
                case 82: goto L_0x044d;
                case 83: goto L_0x0441;
                case 84: goto L_0x042f;
                default: goto L_0x042c;
            }
        L_0x042c:
            r6 = 0
            goto L_0x0d99
        L_0x042f:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            if (r2 == 0) goto L_0x042c
            boolean r2 = r2.isPause()     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = io.dcloud.common.util.JSUtil.wrapJsVar((boolean) r2)     // Catch:{ Exception -> 0x0410 }
            goto L_0x0837
        L_0x0441:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            if (r2 == 0) goto L_0x042c
            r2.resume()     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x044d:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            if (r2 == 0) goto L_0x042c
            r2.pause()     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0459:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            r3 = 0
            java.lang.String r3 = r4.getString(r3)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.core.ui.DCKeyboardManager r4 = io.dcloud.common.core.ui.DCKeyboardManager.getInstance()     // Catch:{ Exception -> 0x0410 }
            r4.setHTMLInputRect(r2, r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x046c:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = r2.getWebviewProperty(r15)     // Catch:{ Exception -> 0x0410 }
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x0410 }
            if (r3 != 0) goto L_0x042c
            r3 = 0
            java.lang.String r2 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r2, r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x0837
        L_0x0483:
            r2 = 0
            java.lang.String r2 = r4.getString(r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r3 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r3 = r3.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            r3.setWebviewProperty(r13, r2)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0492:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = r2.getWebviewProperty(r12)     // Catch:{ Exception -> 0x0410 }
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x0410 }
            if (r3 != 0) goto L_0x042c
            r3 = 0
            java.lang.String r2 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r2, r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x0837
        L_0x04a9:
            r2 = 0
            java.lang.String r2 = r4.getString(r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r3 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r3 = r3.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            r3.setWebviewProperty(r8, r2)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x04b9:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            boolean r3 = r2 instanceof io.dcloud.common.adapter.ui.AdaFrameView     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x042c
            io.dcloud.common.adapter.ui.AdaFrameView r2 = (io.dcloud.common.adapter.ui.AdaFrameView) r2     // Catch:{ Exception -> 0x0410 }
            r3 = 0
            org.json.JSONArray r3 = r4.getJSONArray(r3)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.util.SubNViewsUtil.updateSubNViews(r2, r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x04cb:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            boolean r3 = r2 instanceof io.dcloud.common.adapter.ui.AdaFrameView     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x042c
            io.dcloud.common.adapter.ui.AdaFrameView r2 = (io.dcloud.common.adapter.ui.AdaFrameView) r2     // Catch:{ Exception -> 0x0410 }
            java.util.ArrayList<io.dcloud.common.DHInterface.INativeView> r3 = r2.mChildNativeViewList     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x04ff
            org.json.JSONArray r2 = new org.json.JSONArray     // Catch:{ Exception -> 0x0410 }
            r2.<init>()     // Catch:{ Exception -> 0x0410 }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ Exception -> 0x0410 }
        L_0x04e0:
            boolean r4 = r3.hasNext()     // Catch:{ Exception -> 0x0410 }
            if (r4 == 0) goto L_0x04f4
            java.lang.Object r4 = r3.next()     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.INativeView r4 = (io.dcloud.common.DHInterface.INativeView) r4     // Catch:{ Exception -> 0x0410 }
            org.json.JSONObject r4 = r4.toJSON()     // Catch:{ Exception -> 0x0410 }
            r2.put(r4)     // Catch:{ Exception -> 0x0410 }
            goto L_0x04e0
        L_0x04f4:
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0410 }
            r3 = 0
            java.lang.String r2 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r2, r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x0837
        L_0x04ff:
            io.dcloud.common.adapter.util.ViewOptions r3 = r2.obtainFrameOptions()     // Catch:{ Exception -> 0x0410 }
            org.json.JSONArray r3 = r3.mSubNViews     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x042c
            io.dcloud.common.adapter.util.ViewOptions r2 = r2.obtainFrameOptions()     // Catch:{ Exception -> 0x0410 }
            org.json.JSONArray r2 = r2.mSubNViews     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0410 }
            r3 = 0
            java.lang.String r2 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r2, r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x0837
        L_0x0518:
            r2 = 0
            java.lang.String r2 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r3 = r1.z     // Catch:{ Exception -> 0x0410 }
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r2)     // Catch:{ Exception -> 0x0410 }
            boolean r2 = r2.booleanValue()     // Catch:{ Exception -> 0x0410 }
            r3.interceptTouchEvent(r2)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x052c:
            r2 = 0
            org.json.JSONObject r2 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONArray) r4, (int) r2)     // Catch:{ Exception -> 0x0410 }
            java.lang.String r3 = "type"
            java.lang.String r4 = r1.a0     // Catch:{ Exception -> 0x0410 }
            java.lang.String r3 = r2.optString(r3, r4)     // Catch:{ Exception -> 0x0410 }
            r1.a0 = r3     // Catch:{ Exception -> 0x0410 }
            java.lang.String r3 = "interval"
            int r4 = r1.b0     // Catch:{ Exception -> 0x0410 }
            int r2 = r2.optInt(r3, r4)     // Catch:{ Exception -> 0x0410 }
            r1.b0 = r2     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0547:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            r2.restore()     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x054e:
            r3 = 0
            java.lang.String r3 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r3)     // Catch:{ Exception -> 0x0410 }
            r5 = 1
            java.lang.String r4 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r5)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r5 = r1.z     // Catch:{ Exception -> 0x0410 }
            r5.animate(r2, r3, r4)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x055f:
            r1.a((io.dcloud.common.DHInterface.IWebview) r2, (org.json.JSONArray) r4)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0564:
            r2 = 1
            java.lang.String r2 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.feature.ui.a r3 = r1.j     // Catch:{ Exception -> 0x0410 }
            io.dcloud.feature.ui.b r3 = r3.a((java.lang.String) r2)     // Catch:{ Exception -> 0x0410 }
            if (r3 != 0) goto L_0x042c
            io.dcloud.feature.ui.a r3 = r1.j     // Catch:{ Exception -> 0x0410 }
            r4 = 0
            io.dcloud.feature.ui.c r2 = r3.a((java.lang.String) r2, (java.lang.String) r2, (java.lang.String) r4)     // Catch:{ Exception -> 0x07ad }
            r1.a((io.dcloud.feature.ui.b) r2, (io.dcloud.feature.ui.c) r1, (java.lang.String) r9)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x057d:
            r2 = 0
            java.lang.String r2 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r2)     // Catch:{ Exception -> 0x0410 }
            boolean r3 = io.dcloud.common.util.PdrUtil.isEmpty(r2)     // Catch:{ Exception -> 0x0410 }
            if (r3 != 0) goto L_0x042c
            io.dcloud.common.DHInterface.IFrameView r3 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r3 = r3.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            r4 = 0
            r3.setCssFile(r4, r2)     // Catch:{ Exception -> 0x07ad }
            goto L_0x042c
        L_0x0594:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            java.lang.String r3 = "true"
            r2.setWebviewProperty(r11, r3)     // Catch:{ Exception -> 0x0410 }
            java.lang.String r8 = "false"
            goto L_0x0d9a
        L_0x05a3:
            r3 = 0
            org.json.JSONObject r5 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONArray) r4, (int) r3)     // Catch:{ Exception -> 0x0410 }
            r3 = 1
            org.json.JSONObject r7 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONArray) r4, (int) r3)     // Catch:{ Exception -> 0x0410 }
            r3 = 2
            java.lang.String r8 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r3)     // Catch:{ Exception -> 0x0410 }
            r3 = 3
            java.lang.String r4 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r3)     // Catch:{ Exception -> 0x0410 }
            if (r5 == 0) goto L_0x042c
            java.lang.String r3 = "direction"
            java.lang.String r3 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r5, (java.lang.String) r3)     // Catch:{ Exception -> 0x0410 }
            boolean r3 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Exception -> 0x0410 }
            if (r3 != 0) goto L_0x042c
            java.lang.String r3 = "moveMode"
            java.lang.String r3 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r5, (java.lang.String) r3)     // Catch:{ Exception -> 0x0410 }
            boolean r3 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Exception -> 0x0410 }
            if (r3 != 0) goto L_0x042c
            io.dcloud.common.adapter.ui.AdaFrameItem r3 = r24.d()     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.adapter.util.ViewOptions r3 = r3.obtainFrameOptions()     // Catch:{ Exception -> 0x0410 }
            java.lang.String r9 = "view"
            java.lang.String r9 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r7, (java.lang.String) r9)     // Catch:{ Exception -> 0x0410 }
            boolean r11 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Exception -> 0x0410 }
            if (r11 != 0) goto L_0x05ed
            io.dcloud.feature.ui.a r11 = r1.j     // Catch:{ Exception -> 0x0410 }
            io.dcloud.feature.ui.c r6 = r11.a((java.lang.String) r6, (java.lang.String) r9, (java.lang.String) r9)     // Catch:{ Exception -> 0x0410 }
            goto L_0x05ee
        L_0x05ed:
            r6 = 0
        L_0x05ee:
            if (r6 != 0) goto L_0x0622
            io.dcloud.feature.ui.a r11 = r1.j     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.AbsMgr r11 = r11.d     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IMgr$MgrType r12 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr     // Catch:{ Exception -> 0x0410 }
            r13 = 4
            java.lang.Object[] r13 = new java.lang.Object[r13]     // Catch:{ Exception -> 0x0410 }
            r14 = 0
            r13[r14] = r2     // Catch:{ Exception -> 0x0410 }
            r2 = 1
            r13[r2] = r10     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = "getNativeView"
            r10 = 2
            r13[r10] = r2     // Catch:{ Exception -> 0x0410 }
            java.lang.Object[] r2 = new java.lang.Object[r10]     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r10 = r1.z     // Catch:{ Exception -> 0x0410 }
            r14 = 0
            r2[r14] = r10     // Catch:{ Exception -> 0x0410 }
            r10 = 1
            r2[r10] = r9     // Catch:{ Exception -> 0x0410 }
            r9 = 3
            r13[r9] = r2     // Catch:{ Exception -> 0x0410 }
            r2 = 10
            java.lang.Object r2 = r11.processEvent(r12, r2, r13)     // Catch:{ Exception -> 0x0410 }
            if (r2 == 0) goto L_0x0622
            boolean r9 = r2 instanceof android.view.View     // Catch:{ Exception -> 0x0410 }
            if (r9 == 0) goto L_0x0622
            android.view.View r2 = (android.view.View) r2     // Catch:{ Exception -> 0x0410 }
            r23 = r2
            goto L_0x0624
        L_0x0622:
            r23 = 0
        L_0x0624:
            io.dcloud.feature.ui.a r2 = r1.j     // Catch:{ Exception -> 0x0410 }
            io.dcloud.feature.ui.c r2 = r2.a((java.lang.String) r8, (java.lang.String) r8, (java.lang.String) r8)     // Catch:{ Exception -> 0x0410 }
            if (r6 != 0) goto L_0x062f
            r20 = 0
            goto L_0x0633
        L_0x062f:
            io.dcloud.common.DHInterface.IFrameView r6 = r6.z     // Catch:{ Exception -> 0x0410 }
            r20 = r6
        L_0x0633:
            if (r2 != 0) goto L_0x0638
            r21 = 0
            goto L_0x063c
        L_0x0638:
            io.dcloud.common.DHInterface.IFrameView r2 = r2.z     // Catch:{ Exception -> 0x0410 }
            r21 = r2
        L_0x063c:
            if (r4 == 0) goto L_0x0641
            r22 = r4
            goto L_0x0643
        L_0x0641:
            r22 = 0
        L_0x0643:
            r17 = r3
            r18 = r5
            r19 = r7
            r17.setDragData(r18, r19, r20, r21, r22, r23)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x064e:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNViewId(r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.feature.ui.a r3 = r1.j     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.AbsMgr r3 = r3.d     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IMgr$MgrType r4 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr     // Catch:{ Exception -> 0x0410 }
            r5 = 4
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r6 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r6 = r6.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            r7 = 0
            r5[r7] = r6     // Catch:{ Exception -> 0x0410 }
            r6 = 1
            r5[r6] = r10     // Catch:{ Exception -> 0x0410 }
            java.lang.String r6 = "getNativeView"
            r7 = 2
            r5[r7] = r6     // Catch:{ Exception -> 0x0410 }
            java.lang.Object[] r6 = new java.lang.Object[r7]     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r7 = r1.z     // Catch:{ Exception -> 0x0410 }
            r8 = 0
            r6[r8] = r7     // Catch:{ Exception -> 0x0410 }
            r7 = 1
            r6[r7] = r2     // Catch:{ Exception -> 0x0410 }
            r2 = 3
            r5[r2] = r6     // Catch:{ Exception -> 0x0410 }
            r2 = 10
            java.lang.Object r2 = r3.processEvent(r4, r2, r5)     // Catch:{ Exception -> 0x0410 }
            if (r2 == 0) goto L_0x042c
            boolean r3 = r2 instanceof io.dcloud.common.DHInterface.INativeView     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x042c
            io.dcloud.common.DHInterface.INativeView r2 = (io.dcloud.common.DHInterface.INativeView) r2     // Catch:{ Exception -> 0x0410 }
            org.json.JSONObject r2 = r2.toJSON()     // Catch:{ Exception -> 0x0410 }
            if (r2 == 0) goto L_0x042c
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0410 }
            r3 = 0
            java.lang.String r2 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r2, r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x0837
        L_0x069a:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            r3 = 0
            int r3 = r4.getInt(r3)     // Catch:{ Exception -> 0x0410 }
            float r3 = (float) r3     // Catch:{ Exception -> 0x0410 }
            float r4 = r2.getScale()     // Catch:{ Exception -> 0x0410 }
            float r3 = r3 * r4
            int r3 = (int) r3     // Catch:{ Exception -> 0x0410 }
            r2.setFixBottom(r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x06b2:
            r3 = 0
            java.lang.String r3 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r3)     // Catch:{ Exception -> 0x0410 }
            boolean r4 = io.dcloud.common.util.PdrUtil.isEmpty(r3)     // Catch:{ Exception -> 0x0410 }
            if (r4 != 0) goto L_0x042c
            java.lang.String r2 = r25.obtainFullUrl()     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = r7.convert2LocalFullPath(r2, r3)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r3 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r3 = r3.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            r4 = 0
            r3.setCssFile(r2, r4)     // Catch:{ Exception -> 0x07ad }
            goto L_0x042c
        L_0x06d1:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.adapter.ui.AdaFrameItem r2 = (io.dcloud.common.adapter.ui.AdaFrameItem) r2     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.adapter.util.ViewOptions r2 = r2.obtainFrameOptions()     // Catch:{ Exception -> 0x0410 }
            boolean r2 = r2.mUseHardwave     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = io.dcloud.common.util.JSUtil.wrapJsVar((boolean) r2)     // Catch:{ Exception -> 0x0410 }
            goto L_0x0837
        L_0x06e1:
            r1.c((io.dcloud.common.DHInterface.IWebview) r2, (org.json.JSONArray) r4, (io.dcloud.feature.ui.c) r1)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x06e6:
            r2 = 0
            java.lang.String r2 = r4.getString(r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r3 = r1.z     // Catch:{ Exception -> 0x0410 }
            r3.clearSnapshot(r2)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x06f2:
            r1.a((io.dcloud.common.DHInterface.IWebview) r2, (org.json.JSONArray) r4, (io.dcloud.feature.ui.c) r1)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x06f7:
            r3 = 1
            java.lang.String r5 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r3)     // Catch:{ Exception -> 0x0410 }
            r3 = 0
            java.lang.String r4 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r3)     // Catch:{ Exception -> 0x0410 }
            int r3 = r4.hashCode()     // Catch:{ Exception -> 0x0410 }
            switch(r3) {
                case -1677935844: goto L_0x0731;
                case -1515621005: goto L_0x0727;
                case -333584256: goto L_0x071d;
                case 2115: goto L_0x0713;
                case 2390711: goto L_0x0709;
                default: goto L_0x0708;
            }     // Catch:{ Exception -> 0x0410 }
        L_0x0708:
            goto L_0x073b
        L_0x0709:
            java.lang.String r3 = "Maps"
            boolean r3 = r4.equals(r3)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x073b
            r6 = 0
            goto L_0x073c
        L_0x0713:
            java.lang.String r3 = "Ad"
            boolean r3 = r4.equals(r3)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x073b
            r6 = 4
            goto L_0x073c
        L_0x071d:
            java.lang.String r3 = "barcode"
            boolean r3 = r4.equals(r3)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x073b
            r6 = 1
            goto L_0x073c
        L_0x0727:
            java.lang.String r3 = "LivePusher"
            boolean r3 = r4.equals(r3)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x073b
            r6 = 3
            goto L_0x073c
        L_0x0731:
            java.lang.String r3 = "VideoPlayer"
            boolean r3 = r4.equals(r3)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x073b
            r6 = 2
            goto L_0x073c
        L_0x073b:
            r6 = -1
        L_0x073c:
            if (r6 == 0) goto L_0x076f
            r3 = 1
            if (r6 == r3) goto L_0x0767
            r3 = 2
            if (r6 == r3) goto L_0x075f
            r3 = 3
            if (r6 == r3) goto L_0x0757
            r3 = 4
            if (r6 == r3) goto L_0x074f
            r1.e(r2, r5)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x074f:
            r1.a((io.dcloud.common.DHInterface.IWebview) r2, (java.lang.String) r5)     // Catch:{ Exception -> 0x0410 }
            r24.j()     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0757:
            r1.c(r2, r5)     // Catch:{ Exception -> 0x0410 }
            r24.j()     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x075f:
            r1.f(r2, r5)     // Catch:{ Exception -> 0x0410 }
            r24.j()     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0767:
            r1.b(r2, r5)     // Catch:{ Exception -> 0x0410 }
            r24.j()     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x076f:
            r1.d(r2, r5)     // Catch:{ Exception -> 0x0410 }
            r24.j()     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0777:
            r2 = 1
            java.lang.String r2 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.feature.ui.a r3 = r1.j     // Catch:{ Exception -> 0x0410 }
            io.dcloud.feature.ui.b r3 = r3.a((java.lang.String) r2)     // Catch:{ Exception -> 0x0410 }
            if (r3 != 0) goto L_0x078b
            io.dcloud.feature.ui.a r3 = r1.j     // Catch:{ Exception -> 0x0410 }
            r4 = 0
            io.dcloud.feature.ui.c r3 = r3.a((java.lang.String) r2, (java.lang.String) r2, (java.lang.String) r4)     // Catch:{ Exception -> 0x07ad }
        L_0x078b:
            boolean r2 = r1.b((io.dcloud.feature.ui.b) r3)     // Catch:{ Exception -> 0x0410 }
            if (r2 != 0) goto L_0x042c
            if (r3 == 0) goto L_0x042c
            r1.a((io.dcloud.feature.ui.b) r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0798:
            r2 = 0
            java.lang.String r2 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.feature.ui.a r3 = r1.j     // Catch:{ Exception -> 0x0410 }
            io.dcloud.feature.ui.b r3 = r3.a((java.lang.String) r2)     // Catch:{ Exception -> 0x0410 }
            if (r3 != 0) goto L_0x07b2
            io.dcloud.feature.ui.a r3 = r1.j     // Catch:{ Exception -> 0x0410 }
            r4 = 0
            io.dcloud.feature.ui.c r3 = r3.a((java.lang.String) r2, (java.lang.String) r2, (java.lang.String) r4)     // Catch:{ Exception -> 0x07ad }
            goto L_0x07b2
        L_0x07ad:
            r0 = move-exception
            r2 = r0
            r6 = r4
            goto L_0x0d96
        L_0x07b2:
            boolean r2 = r1.b((io.dcloud.feature.ui.b) r3)     // Catch:{ Exception -> 0x0410 }
            if (r2 == 0) goto L_0x042c
            r1.c((io.dcloud.feature.ui.b) r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x07bd:
            r3 = 1
            java.lang.String r3 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r3)     // Catch:{ Exception -> 0x0410 }
            r1.h(r2, r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x07c7:
            r2 = 0
            java.lang.String r2 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r2)     // Catch:{ Exception -> 0x0410 }
            r3 = 1
            org.json.JSONObject r3 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONArray) r4, (int) r3)     // Catch:{ Exception -> 0x0410 }
            java.lang.String r4 = "text/html"
            java.lang.String r5 = "utf-8"
            if (r3 == 0) goto L_0x07f5
            java.lang.String r7 = "encoding"
            java.lang.String r7 = r3.optString(r7)     // Catch:{ Exception -> 0x0410 }
            java.lang.String r5 = io.dcloud.common.util.PdrUtil.getNonString(r7, r5)     // Catch:{ Exception -> 0x0410 }
            java.lang.String r7 = "mimeType"
            java.lang.String r7 = r3.optString(r7)     // Catch:{ Exception -> 0x0410 }
            java.lang.String r4 = io.dcloud.common.util.PdrUtil.getNonString(r7, r4)     // Catch:{ Exception -> 0x0410 }
            java.lang.String r7 = "baseURL"
            java.lang.String r3 = r3.optString(r7)     // Catch:{ Exception -> 0x0410 }
            java.lang.String r6 = io.dcloud.common.util.PdrUtil.getNonString(r3, r6)     // Catch:{ Exception -> 0x0410 }
        L_0x07f5:
            io.dcloud.common.DHInterface.IFrameView r3 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r3 = r3.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            r3.loadContentData(r6, r2, r4, r5)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0800:
            java.util.ArrayList<io.dcloud.feature.ui.b> r2 = r1.N     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = a((java.util.ArrayList) r2)     // Catch:{ Exception -> 0x0410 }
            goto L_0x0837
        L_0x0807:
            io.dcloud.feature.ui.c r2 = r1.h     // Catch:{ Exception -> 0x0410 }
            if (r2 == 0) goto L_0x0810
            java.lang.String r2 = r2.h()     // Catch:{ Exception -> 0x0410 }
            goto L_0x0837
        L_0x0810:
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x0410 }
            r3 = 0
            r2[r3] = r20     // Catch:{ Exception -> 0x0410 }
            r4 = 1
            r2[r4] = r20     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = io.dcloud.common.util.StringUtil.format(r14, r2)     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r2, r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x0837
        L_0x0822:
            io.dcloud.feature.ui.c r2 = r1.h     // Catch:{ Exception -> 0x0410 }
            if (r2 == 0) goto L_0x042c
            boolean r3 = r2.b((io.dcloud.feature.ui.b) r1)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x042c
            r2.c((io.dcloud.feature.ui.b) r1)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0831:
            java.util.ArrayList<io.dcloud.feature.ui.c> r2 = r1.Y     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = a((java.util.ArrayList) r2)     // Catch:{ Exception -> 0x0410 }
        L_0x0837:
            r8 = r2
            goto L_0x0d9a
        L_0x083a:
            io.dcloud.feature.ui.c r2 = r1.X     // Catch:{ Exception -> 0x0410 }
            if (r2 == 0) goto L_0x0843
            java.lang.String r2 = r2.h()     // Catch:{ Exception -> 0x0410 }
            goto L_0x0837
        L_0x0843:
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x0410 }
            r3 = 0
            r2[r3] = r20     // Catch:{ Exception -> 0x0410 }
            r4 = 1
            r2[r4] = r20     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = io.dcloud.common.util.StringUtil.format(r14, r2)     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r2, r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x0837
        L_0x0855:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = r2.obtainPageTitle()     // Catch:{ Exception -> 0x0410 }
            r3 = 1
            java.lang.String r2 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r2, r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x0837
        L_0x0865:
            r2 = 0
            java.lang.String r2 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.feature.ui.b r2 = r1.c((java.lang.String) r2)     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = r2.h()     // Catch:{ Exception -> 0x0410 }
            goto L_0x0837
        L_0x0873:
            r2 = 0
            boolean r2 = r4.getBoolean(r2)     // Catch:{ Exception -> 0x0410 }
            r1.I = r2     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r3 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r3 = r3.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.adapter.ui.AdaFrameItem r3 = (io.dcloud.common.adapter.ui.AdaFrameItem) r3     // Catch:{ Exception -> 0x0410 }
            if (r2 == 0) goto L_0x0887
            int r2 = io.dcloud.common.adapter.ui.AdaFrameItem.VISIBLE     // Catch:{ Exception -> 0x0410 }
            goto L_0x0889
        L_0x0887:
            int r2 = io.dcloud.common.adapter.ui.AdaFrameItem.GONE     // Catch:{ Exception -> 0x0410 }
        L_0x0889:
            r3.setVisibility(r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.adapter.ui.AdaWebViewParent r2 = r2.obtainWebviewParent()     // Catch:{ Exception -> 0x0410 }
            r3 = -1
            r2.setBgcolor(r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0898:
            r2 = 0
            boolean r2 = r4.getBoolean(r2)     // Catch:{ Exception -> 0x0410 }
            r1.G = r2     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r3 = r1.z     // Catch:{ Exception -> 0x0410 }
            r4 = 1
            r3.setVisible(r2, r4)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x08a7:
            boolean r2 = r1.G     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ Exception -> 0x0410 }
            r3 = 0
            java.lang.String r2 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r2, r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x0837
        L_0x08b3:
            r3 = 0
            java.lang.String r2 = r4.getString(r3)     // Catch:{ Exception -> 0x0410 }
            r3 = 1
            java.lang.String r3 = r4.getString(r3)     // Catch:{ Exception -> 0x0410 }
            r1.b(r3, r2)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x08c2:
            r3 = 0
            java.lang.String r3 = r4.getString(r3)     // Catch:{ Exception -> 0x0410 }
            r5 = 1
            java.lang.String r4 = r4.getString(r5)     // Catch:{ Exception -> 0x0410 }
            java.util.HashMap<java.lang.String, java.lang.String> r5 = r1.i     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = r25.getWebviewANID()     // Catch:{ Exception -> 0x0410 }
            java.lang.Object r2 = r5.get(r2)     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ Exception -> 0x0410 }
            r1.a((java.lang.String) r4, (java.lang.String) r3, (java.lang.String) r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            boolean r2 = r2.unReceiveTitle()     // Catch:{ Exception -> 0x0410 }
            if (r2 != 0) goto L_0x042c
            r2 = r19
            boolean r3 = r2.equals(r3)     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x042c
            io.dcloud.common.DHInterface.IFrameView r3 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r3 = r3.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            java.lang.String r3 = r3.getTitle()     // Catch:{ Exception -> 0x0410 }
            r1.onCallBack(r2, r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x08fe:
            r2 = 0
            java.lang.String r2 = r4.getString(r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r3 = r24.r()     // Catch:{ Exception -> 0x0410 }
            r3.setAssistantType(r2)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x090c:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNViewId(r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.feature.ui.a r3 = r1.j     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.AbsMgr r3 = r3.d     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r5 = r24.r()     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r6 = r1.z     // Catch:{ Exception -> 0x0410 }
            java.lang.Object r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNView(r3, r5, r6, r2)     // Catch:{ Exception -> 0x0410 }
            boolean r3 = r2 instanceof io.dcloud.common.DHInterface.ITitleNView     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x042c
            io.dcloud.common.DHInterface.ITitleNView r2 = (io.dcloud.common.DHInterface.ITitleNView) r2     // Catch:{ Exception -> 0x0410 }
            r3 = 0
            java.lang.String r3 = r4.optString(r3)     // Catch:{ Exception -> 0x0410 }
            r5 = 1
            org.json.JSONObject r4 = r4.optJSONObject(r5)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r5 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.util.TitleNViewUtil.setTitleNViewButtonStyle(r2, r3, r4, r5)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0937:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNViewId(r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.feature.ui.a r3 = r1.j     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.AbsMgr r3 = r3.d     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r4 = r24.r()     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r5 = r1.z     // Catch:{ Exception -> 0x0410 }
            java.lang.Object r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNView(r3, r4, r5, r2)     // Catch:{ Exception -> 0x0410 }
            boolean r3 = r2 instanceof io.dcloud.common.DHInterface.ITitleNView     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x042c
            io.dcloud.common.DHInterface.ITitleNView r2 = (io.dcloud.common.DHInterface.ITitleNView) r2     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNViewSearchInputText(r2)     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = io.dcloud.common.util.JSUtil.wrapJsVar((java.lang.String) r2)     // Catch:{ Exception -> 0x0410 }
            goto L_0x0837
        L_0x095b:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNViewId(r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.feature.ui.a r3 = r1.j     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.AbsMgr r3 = r3.d     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r5 = r24.r()     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r6 = r1.z     // Catch:{ Exception -> 0x0410 }
            java.lang.Object r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNView(r3, r5, r6, r2)     // Catch:{ Exception -> 0x0410 }
            boolean r3 = r2 instanceof io.dcloud.common.DHInterface.ITitleNView     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x042c
            io.dcloud.common.DHInterface.ITitleNView r2 = (io.dcloud.common.DHInterface.ITitleNView) r2     // Catch:{ Exception -> 0x0410 }
            r3 = 0
            java.lang.String r3 = r4.optString(r3)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.util.TitleNViewUtil.setTitleNViewSearchInputText(r2, r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x097f:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNViewId(r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.feature.ui.a r3 = r1.j     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.AbsMgr r3 = r3.d     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r5 = r24.r()     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r6 = r1.z     // Catch:{ Exception -> 0x0410 }
            java.lang.Object r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNView(r3, r5, r6, r2)     // Catch:{ Exception -> 0x0410 }
            boolean r3 = r2 instanceof io.dcloud.common.DHInterface.ITitleNView     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x042c
            io.dcloud.common.DHInterface.ITitleNView r2 = (io.dcloud.common.DHInterface.ITitleNView) r2     // Catch:{ Exception -> 0x0410 }
            r3 = 0
            java.lang.String r3 = r4.optString(r3)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.util.TitleNViewUtil.setTitleNViewSearchInputFocus(r2, r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x09a3:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNViewId(r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.feature.ui.a r3 = r1.j     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.AbsMgr r3 = r3.d     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r5 = r24.r()     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r6 = r1.z     // Catch:{ Exception -> 0x0410 }
            java.lang.Object r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNView(r3, r5, r6, r2)     // Catch:{ Exception -> 0x0410 }
            boolean r3 = r2 instanceof io.dcloud.common.DHInterface.ITitleNView     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x042c
            io.dcloud.common.DHInterface.ITitleNView r2 = (io.dcloud.common.DHInterface.ITitleNView) r2     // Catch:{ Exception -> 0x0410 }
            r3 = 0
            org.json.JSONObject r4 = r4.optJSONObject(r3)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.util.TitleNViewUtil.titleNViewButtonRedDot(r2, r4, r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x09c7:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNViewId(r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.feature.ui.a r3 = r1.j     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.AbsMgr r3 = r3.d     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r5 = r24.r()     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r6 = r1.z     // Catch:{ Exception -> 0x0410 }
            java.lang.Object r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNView(r3, r5, r6, r2)     // Catch:{ Exception -> 0x0410 }
            boolean r3 = r2 instanceof io.dcloud.common.DHInterface.ITitleNView     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x042c
            io.dcloud.common.DHInterface.ITitleNView r2 = (io.dcloud.common.DHInterface.ITitleNView) r2     // Catch:{ Exception -> 0x0410 }
            r3 = 0
            org.json.JSONObject r3 = r4.optJSONObject(r3)     // Catch:{ Exception -> 0x0410 }
            r4 = 1
            io.dcloud.common.util.TitleNViewUtil.titleNViewButtonRedDot(r2, r3, r4)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x09ec:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNViewId(r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.feature.ui.a r3 = r1.j     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.AbsMgr r3 = r3.d     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r5 = r24.r()     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r6 = r1.z     // Catch:{ Exception -> 0x0410 }
            java.lang.Object r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNView(r3, r5, r6, r2)     // Catch:{ Exception -> 0x0410 }
            boolean r3 = r2 instanceof io.dcloud.common.DHInterface.ITitleNView     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x042c
            io.dcloud.common.DHInterface.ITitleNView r2 = (io.dcloud.common.DHInterface.ITitleNView) r2     // Catch:{ Exception -> 0x0410 }
            r3 = 0
            org.json.JSONObject r4 = r4.optJSONObject(r3)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.util.TitleNViewUtil.titleNViewButtonBadge(r2, r4, r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0a10:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNViewId(r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.feature.ui.a r3 = r1.j     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.AbsMgr r3 = r3.d     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r5 = r24.r()     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r6 = r1.z     // Catch:{ Exception -> 0x0410 }
            java.lang.Object r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNView(r3, r5, r6, r2)     // Catch:{ Exception -> 0x0410 }
            boolean r3 = r2 instanceof io.dcloud.common.DHInterface.ITitleNView     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x042c
            io.dcloud.common.DHInterface.ITitleNView r2 = (io.dcloud.common.DHInterface.ITitleNView) r2     // Catch:{ Exception -> 0x0410 }
            r3 = 0
            org.json.JSONObject r3 = r4.optJSONObject(r3)     // Catch:{ Exception -> 0x0410 }
            r4 = 1
            io.dcloud.common.util.TitleNViewUtil.titleNViewButtonBadge(r2, r3, r4)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0a35:
            r2 = 0
            java.lang.String r2 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r2)     // Catch:{ Exception -> 0x0410 }
            boolean r3 = io.dcloud.common.util.PdrUtil.isEmpty(r2)     // Catch:{ Exception -> 0x0410 }
            if (r3 != 0) goto L_0x042c
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r2)     // Catch:{ Exception -> 0x0410 }
            boolean r2 = r2.booleanValue()     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.adapter.util.DeviceInfo.isVolumeButtonEnabled = r2     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0a4c:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            if (r2 == 0) goto L_0x0a65
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            boolean r2 = r2.isUniService()     // Catch:{ Exception -> 0x0410 }
            if (r2 == 0) goto L_0x0a65
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            android.view.View r2 = r2.obtainMainView()     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.adapter.util.DeviceInfo.showIME(r2)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0a65:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            android.view.View r2 = r2.obtainMainView()     // Catch:{ Exception -> 0x0410 }
            r3 = 1
            io.dcloud.common.adapter.util.DeviceInfo.showIME(r2, r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0a71:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            android.view.View r2 = r2.obtainMainView()     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.adapter.util.DeviceInfo.hideIME(r2)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0a7c:
            r2 = 0
            java.lang.String r3 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r2)     // Catch:{ Exception -> 0x0410 }
            r4 = 1
            boolean r2 = io.dcloud.common.util.PdrUtil.parseBoolean(r3, r4, r2)     // Catch:{ Exception -> 0x0410 }
            r1.a((io.dcloud.feature.ui.c) r1, (boolean) r2)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0a8b:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            r2.stopLoading()     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0a96:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            if (r2 == 0) goto L_0x042c
            java.lang.String r3 = r2.obtainUrl()     // Catch:{ Exception -> 0x0410 }
            r5 = 0
            java.lang.String r5 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r5)     // Catch:{ Exception -> 0x0410 }
            r6 = 2
            org.json.JSONObject r4 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONArray) r4, (int) r6)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r6 = r2.obtainFrameView()     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IApp r6 = r6.obtainApp()     // Catch:{ Exception -> 0x0410 }
            java.lang.String r7 = r2.obtainFullUrl()     // Catch:{ Exception -> 0x0410 }
            java.lang.String r6 = r6.convert2WebviewFullPath(r7, r5)     // Catch:{ Exception -> 0x0410 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0410 }
            r7.<init>()     // Catch:{ Exception -> 0x0410 }
            java.lang.String r8 = "NWindow.load "
            r7.append(r8)     // Catch:{ Exception -> 0x0410 }
            r7.append(r6)     // Catch:{ Exception -> 0x0410 }
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.adapter.util.Logger.d(r7)     // Catch:{ Exception -> 0x0410 }
            if (r4 == 0) goto L_0x0af9
            int r7 = r4.length()     // Catch:{ Exception -> 0x0410 }
            if (r7 <= 0) goto L_0x0af9
            java.util.HashMap r7 = new java.util.HashMap     // Catch:{ Exception -> 0x0410 }
            int r8 = r4.length()     // Catch:{ Exception -> 0x0410 }
            r7.<init>(r8)     // Catch:{ Exception -> 0x0410 }
            java.util.Iterator r8 = r4.keys()     // Catch:{ Exception -> 0x0410 }
        L_0x0ae5:
            boolean r9 = r8.hasNext()     // Catch:{ Exception -> 0x0410 }
            if (r9 == 0) goto L_0x0afa
            java.lang.Object r9 = r8.next()     // Catch:{ Exception -> 0x0410 }
            java.lang.String r9 = (java.lang.String) r9     // Catch:{ Exception -> 0x0410 }
            java.lang.String r10 = r4.optString(r9)     // Catch:{ Exception -> 0x0410 }
            r7.put(r9, r10)     // Catch:{ Exception -> 0x0410 }
            goto L_0x0ae5
        L_0x0af9:
            r7 = 0
        L_0x0afa:
            boolean r4 = r2 instanceof io.dcloud.common.adapter.ui.AdaWebview     // Catch:{ Exception -> 0x0410 }
            if (r4 == 0) goto L_0x0b29
            r4 = r2
            io.dcloud.common.adapter.ui.AdaWebview r4 = (io.dcloud.common.adapter.ui.AdaWebview) r4     // Catch:{ Exception -> 0x0410 }
            boolean r4 = r4.checkOverrideUrl(r6)     // Catch:{ Exception -> 0x0410 }
            if (r4 == 0) goto L_0x0b29
            io.dcloud.common.adapter.ui.AdaWebview r2 = (io.dcloud.common.adapter.ui.AdaWebview) r2     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.adapter.ui.AdaFrameView r2 = r2.mFrameView     // Catch:{ Exception -> 0x0410 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0410 }
            r3.<init>()     // Catch:{ Exception -> 0x0410 }
            java.lang.String r4 = "{url:'"
            r3.append(r4)     // Catch:{ Exception -> 0x0410 }
            r3.append(r6)     // Catch:{ Exception -> 0x0410 }
            java.lang.String r4 = "'}"
            r3.append(r4)     // Catch:{ Exception -> 0x0410 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0410 }
            r4 = r18
            r2.dispatchFrameViewEvents(r4, r3)     // Catch:{ Exception -> 0x0410 }
            r2 = 0
            return r2
        L_0x0b29:
            r2.setLoadURLHeads(r5, r7)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            r2.setOriginalUrl(r5)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            r2.reload((java.lang.String) r6)     // Catch:{ Exception -> 0x0410 }
            r1.a((io.dcloud.feature.ui.c) r1, (java.lang.String) r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0b43:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            r2.clearHistory()     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0b4e:
            r3 = 0
            java.lang.String r3 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r3)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r4 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r4 = r4.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            boolean r4 = r4.canGoForward()     // Catch:{ Exception -> 0x0410 }
            java.lang.String r4 = java.lang.String.valueOf(r4)     // Catch:{ Exception -> 0x0410 }
            int r5 = io.dcloud.common.util.JSUtil.OK     // Catch:{ Exception -> 0x0410 }
            r6 = 1
            r7 = 0
            r2 = r25
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r2, r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0b6c:
            r3 = 0
            java.lang.String r3 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r3)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r4 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r4 = r4.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            boolean r4 = r4.canGoBack()     // Catch:{ Exception -> 0x0410 }
            java.lang.String r4 = java.lang.String.valueOf(r4)     // Catch:{ Exception -> 0x0410 }
            int r5 = io.dcloud.common.util.JSUtil.OK     // Catch:{ Exception -> 0x0410 }
            r6 = 1
            r7 = 0
            r2 = r25
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r2, r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0b8a:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            r2.stopLoading()     // Catch:{ Exception -> 0x0410 }
            r3 = 1
            r2.goBackOrForward(r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0b99:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            r2.stopLoading()     // Catch:{ Exception -> 0x0410 }
            r3 = -1
            r2.goBackOrForward(r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0ba8:
            android.os.Looper r2 = android.os.Looper.getMainLooper()     // Catch:{ Exception -> 0x0410 }
            java.lang.Thread r2 = r2.getThread()     // Catch:{ Exception -> 0x0410 }
            long r2 = r2.getId()     // Catch:{ Exception -> 0x0410 }
            java.lang.Thread r5 = java.lang.Thread.currentThread()     // Catch:{ Exception -> 0x0410 }
            long r5 = r5.getId()     // Catch:{ Exception -> 0x0410 }
            int r7 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1))
            r2 = 0
            if (r7 != 0) goto L_0x0bc3
            r6 = 1
            goto L_0x0bc4
        L_0x0bc3:
            r6 = 0
        L_0x0bc4:
            java.lang.String r2 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r3 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r3 = r3.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            if (r6 == 0) goto L_0x0bda
            io.dcloud.feature.ui.c$c r4 = new io.dcloud.feature.ui.c$c     // Catch:{ Exception -> 0x0410 }
            r4.<init>(r3, r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.adapter.util.MessageHandler.post(r4)     // Catch:{ Exception -> 0x0410 }
            r2 = 0
            return r2
        L_0x0bda:
            io.dcloud.feature.ui.c$d r4 = new io.dcloud.feature.ui.c$d     // Catch:{ Exception -> 0x0410 }
            r4.<init>(r3, r2)     // Catch:{ Exception -> 0x0410 }
            java.lang.Object r2 = io.dcloud.common.adapter.util.MessageHandler.postAndWait(r4)     // Catch:{ Exception -> 0x0410 }
            if (r2 == 0) goto L_0x042c
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = io.dcloud.common.util.JSUtil.wrapJsVar((java.lang.String) r2)     // Catch:{ Exception -> 0x0410 }
            goto L_0x0837
        L_0x0bef:
            r3 = 0
            java.lang.String r3 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r3)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r5 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r5 = r5.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            r6 = 1
            java.lang.String r4 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r6)     // Catch:{ Exception -> 0x0410 }
            boolean r6 = io.dcloud.common.util.PdrUtil.isEmpty(r4)     // Catch:{ Exception -> 0x0410 }
            if (r6 != 0) goto L_0x0c0e
            io.dcloud.feature.ui.c$b r6 = new io.dcloud.feature.ui.c$b     // Catch:{ Exception -> 0x0410 }
            r6.<init>(r2, r4)     // Catch:{ Exception -> 0x0410 }
            java.lang.String r3 = io.dcloud.common.adapter.ui.ReceiveJSValue.registerCallback(r3, r6)     // Catch:{ Exception -> 0x0410 }
        L_0x0c0e:
            r5.evalJS(r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0c13:
            r1.b((io.dcloud.common.DHInterface.IWebview) r2, (org.json.JSONArray) r4, (io.dcloud.feature.ui.c) r1)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0c18:
            r1.a((io.dcloud.common.DHInterface.IWebview) r2, (org.json.JSONArray) r4, (io.dcloud.feature.ui.c) r1, (java.lang.String) r9)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0c1d:
            r1.d(r2, r4, r1)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0c22:
            r3 = 0
            org.json.JSONObject r3 = r4.optJSONObject(r3)     // Catch:{ Exception -> 0x0410 }
            r5 = 1
            java.lang.String r4 = r4.optString(r5)     // Catch:{ Exception -> 0x0410 }
            r1.R = r4     // Catch:{ Exception -> 0x0410 }
            r1.S = r2     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            r2.setOverrideUrlLoadingData(r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0c3b:
            r2 = 0
            org.json.JSONArray r2 = r4.optJSONArray(r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r3 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r3 = r3.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            r3.setOverrideResourceRequest(r2)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0c4b:
            r3 = 0
            org.json.JSONObject r3 = r4.optJSONObject(r3)     // Catch:{ Exception -> 0x0410 }
            r5 = 1
            java.lang.String r4 = r4.optString(r5)     // Catch:{ Exception -> 0x0410 }
            r1.T = r4     // Catch:{ Exception -> 0x0410 }
            r1.U = r2     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            r2.setListenResourceLoading(r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0c64:
            r3 = 0
            java.lang.String r3 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r3)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r4 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IApp r4 = r4.obtainApp()     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = r25.obtainFullUrl()     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = r4.convert2AbsFullPath(r2, r3)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r3 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r3 = r3.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            r3.appendPreloadJsFile(r2)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0c82:
            r3 = 0
            java.lang.String r5 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r3)     // Catch:{ Exception -> 0x0410 }
            r6 = 1
            boolean r3 = r4.optBoolean(r6, r3)     // Catch:{ Exception -> 0x0410 }
            boolean r4 = io.dcloud.common.util.PdrUtil.isEmpty(r5)     // Catch:{ Exception -> 0x0410 }
            if (r4 != 0) goto L_0x042c
            io.dcloud.common.DHInterface.IFrameView r4 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IApp r4 = r4.obtainApp()     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = r25.obtainFullUrl()     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = r4.convert2AbsFullPath(r2, r5)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r4 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r4 = r4.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            r4.setPreloadJsFile(r2, r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0cab:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = r2.obtainFullUrl()     // Catch:{ Exception -> 0x0410 }
            r3 = 1
            java.lang.String r2 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r2, r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x0837
        L_0x0cbc:
            r3 = 0
            java.lang.String r3 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r3)     // Catch:{ Exception -> 0x0410 }
            java.lang.String r4 = r24.n()     // Catch:{ Exception -> 0x0410 }
            int r5 = io.dcloud.common.util.JSUtil.OK     // Catch:{ Exception -> 0x0410 }
            r6 = 1
            r7 = 0
            r2 = r25
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r2, r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0cd0:
            r1.e(r2, r4, r1)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0cd5:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.adapter.ui.AdaFrameItem r2 = (io.dcloud.common.adapter.ui.AdaFrameItem) r2     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.adapter.util.ViewOptions r2 = r2.obtainFrameOptions()     // Catch:{ Exception -> 0x0410 }
            boolean r3 = r2.hasBackground()     // Catch:{ Exception -> 0x0410 }
            if (r3 == 0) goto L_0x0ced
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.adapter.ui.AdaWebViewParent r2 = r2.obtainWebviewParent()     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.adapter.util.ViewOptions r2 = r2.obtainFrameOptions()     // Catch:{ Exception -> 0x0410 }
        L_0x0ced:
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ Exception -> 0x0410 }
            org.json.JSONObject r2 = r2.mJsonViewOption     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0410 }
            r3.<init>(r2)     // Catch:{ Exception -> 0x0410 }
            r2 = r16
            boolean r4 = r3.has(r2)     // Catch:{ Exception -> 0x0410 }
            if (r4 == 0) goto L_0x0d0c
            java.lang.String r4 = r3.getString(r2)     // Catch:{ Exception -> 0x0410 }
            r3.remove(r2)     // Catch:{ Exception -> 0x0410 }
            java.lang.String r2 = "background"
            r3.put(r2, r4)     // Catch:{ Exception -> 0x0410 }
        L_0x0d0c:
            java.lang.String r2 = r3.toString()     // Catch:{ Exception -> 0x0410 }
            r3 = 0
            java.lang.String r2 = io.dcloud.common.util.Deprecated_JSUtil.wrapJsVar(r2, r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x0837
        L_0x0d17:
            r2 = 0
            java.lang.String r2 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r3 = r24.r()     // Catch:{ Exception -> 0x0410 }
            java.lang.String r4 = "blockNetworkImage"
            r3.setWebviewProperty(r4, r2)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0d27:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            r3 = r26
            r2.endWebViewEvent(r3)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0d34:
            r3 = r26
            r2 = 0
            org.json.JSONObject r2 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONArray) r4, (int) r2)     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IFrameView r4 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r4 = r4.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            r4.setWebViewEvent(r3, r2)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0d46:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0410 }
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0410 }
            r2.endWebViewEvent(r5)     // Catch:{ Exception -> 0x0410 }
            goto L_0x042c
        L_0x0d51:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0d93 }
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ Exception -> 0x0d93 }
            java.lang.String r3 = "pull_down_refresh_begin"
            r6 = 0
            r2.setWebViewEvent(r3, r6)     // Catch:{ Exception -> 0x0d91 }
            goto L_0x0d99
        L_0x0d5e:
            r6 = 0
            java.lang.String r2 = "View_Visible_Path"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0d91 }
            r3.<init>()     // Catch:{ Exception -> 0x0d91 }
            java.lang.String r7 = "refreshLoadingViewsSize setPullToRefresh args="
            r3.append(r7)     // Catch:{ Exception -> 0x0d91 }
            r3.append(r4)     // Catch:{ Exception -> 0x0d91 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0d91 }
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r2, (java.lang.String) r3)     // Catch:{ Exception -> 0x0d91 }
            r2 = 0
            org.json.JSONObject r2 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONArray) r4, (int) r2)     // Catch:{ Exception -> 0x0d91 }
            r3 = 1
            java.lang.String r3 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r4, (int) r3)     // Catch:{ Exception -> 0x0d91 }
            boolean r4 = io.dcloud.common.util.PdrUtil.isEmpty(r3)     // Catch:{ Exception -> 0x0d91 }
            if (r4 != 0) goto L_0x0d87
            r1.V = r3     // Catch:{ Exception -> 0x0d91 }
        L_0x0d87:
            io.dcloud.common.DHInterface.IFrameView r3 = r1.z     // Catch:{ Exception -> 0x0d91 }
            io.dcloud.common.DHInterface.IWebview r3 = r3.obtainWebView()     // Catch:{ Exception -> 0x0d91 }
            r3.setWebViewEvent(r5, r2)     // Catch:{ Exception -> 0x0d91 }
            goto L_0x0d99
        L_0x0d91:
            r0 = move-exception
            goto L_0x0d95
        L_0x0d93:
            r0 = move-exception
            r6 = 0
        L_0x0d95:
            r2 = r0
        L_0x0d96:
            r2.printStackTrace()
        L_0x0d99:
            r8 = r6
        L_0x0d9a:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.c.a(io.dcloud.common.DHInterface.IWebview, java.lang.String, org.json.JSONArray):java.lang.String");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0029, code lost:
        if (r0.z.obtainMainView().getVisibility() == 0) goto L_0x0066;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(io.dcloud.common.DHInterface.IWebview r9, org.json.JSONArray r10) {
        /*
            r8 = this;
            r0 = 0
            java.lang.String r0 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r10, (int) r0)
            r1 = 1
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r10, (int) r1)
            io.dcloud.feature.ui.a r2 = r8.j
            r3 = 0
            io.dcloud.feature.ui.c r0 = r2.a((java.lang.String) r0, (java.lang.String) r0, (java.lang.String) r3)
            if (r0 == 0) goto L_0x002b
            io.dcloud.common.DHInterface.IFrameView r2 = r0.z     // Catch:{ Exception -> 0x0056 }
            android.view.View r2 = r2.obtainMainView()     // Catch:{ Exception -> 0x0056 }
            android.view.ViewParent r2 = r2.getParent()     // Catch:{ Exception -> 0x0056 }
            if (r2 == 0) goto L_0x002b
            io.dcloud.common.DHInterface.IFrameView r2 = r0.z     // Catch:{ Exception -> 0x0056 }
            android.view.View r2 = r2.obtainMainView()     // Catch:{ Exception -> 0x0056 }
            int r2 = r2.getVisibility()     // Catch:{ Exception -> 0x0056 }
            if (r2 == 0) goto L_0x0066
        L_0x002b:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0056 }
            r2.<init>()     // Catch:{ Exception -> 0x0056 }
            java.lang.String r3 = "{\"code\":-100,\"message\":\""
            r2.append(r3)     // Catch:{ Exception -> 0x0056 }
            android.content.Context r3 = r9.getContext()     // Catch:{ Exception -> 0x0056 }
            int r4 = io.dcloud.base.R.string.dcloud_ui_webview_not_finished     // Catch:{ Exception -> 0x0056 }
            java.lang.String r3 = r3.getString(r4)     // Catch:{ Exception -> 0x0056 }
            r2.append(r3)     // Catch:{ Exception -> 0x0056 }
            java.lang.String r3 = "\"}"
            r2.append(r3)     // Catch:{ Exception -> 0x0056 }
            java.lang.String r4 = r2.toString()     // Catch:{ Exception -> 0x0056 }
            int r5 = io.dcloud.common.util.JSUtil.ERROR     // Catch:{ Exception -> 0x0056 }
            r6 = 1
            r7 = 0
            r2 = r9
            r3 = r1
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r2, r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x0056 }
            return
        L_0x0056:
            r2 = move-exception
            r2.printStackTrace()
            int r5 = io.dcloud.common.util.JSUtil.ERROR
            r6 = 1
            r7 = 0
            java.lang.String r4 = "{\"code\":-100,\"message\":\"\"+sWeb.getContext().getString(R.string.dcloud_ui_webview_not_finished)+\"\"}"
            r2 = r9
            r3 = r1
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r2, r3, r4, r5, r6, r7)
        L_0x0066:
            io.dcloud.common.DHInterface.IWebview r4 = r0.r()
            r0 = 2
            org.json.JSONObject r10 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONArray) r10, (int) r0)
            java.lang.String r0 = "auto"
            if (r10 == 0) goto L_0x0081
            java.lang.String r2 = "type"
            boolean r3 = r10.has(r2)
            if (r3 == 0) goto L_0x0081
            java.lang.String r10 = r10.optString(r2, r0)
            r5 = r10
            goto L_0x0082
        L_0x0081:
            r5 = r0
        L_0x0082:
            android.view.ViewGroup r10 = r4.obtainWindowView()
            if (r10 == 0) goto L_0x0094
            io.dcloud.feature.ui.c$e r0 = new io.dcloud.feature.ui.c$e
            r2 = r0
            r3 = r8
            r6 = r9
            r7 = r1
            r2.<init>(r4, r5, r6, r7)
            r10.post(r0)
        L_0x0094:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.c.a(io.dcloud.common.DHInterface.IWebview, org.json.JSONArray):void");
    }

    private void a(b bVar, c cVar, String str) {
        c cVar2 = (c) bVar;
        if (cVar2.G) {
            cVar.v = cVar2.v - 1;
            cVar.G = true;
            cVar.J = true;
            cVar.H = false;
            this.j.a(str, cVar, this.j.c(this));
            this.j.d.processEvent(IMgr.MgrType.WindowMgr, 45, new Object[]{cVar.z, cVar2.z});
        }
    }

    private void a(c cVar, String str) {
        IApp obtainApp;
        if (cVar != null && !PdrUtil.isEmpty(str) && (obtainApp = cVar.r().obtainApp()) != null) {
            c cVar2 = cVar.X;
            if (cVar2 != null) {
                str = cVar2.r().obtainUrl();
            }
            String obtainUrl = cVar.r().obtainUrl();
            if (BaseInfo.isBase(cVar.a()) && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(obtainUrl) && !str.startsWith(DeviceInfo.HTTP_PROTOCOL) && !obtainUrl.startsWith(DeviceInfo.HTTP_PROTOCOL)) {
                Log.i(AbsoluteConst.HBUILDER_TAG, StringUtil.format(AbsoluteConst.OPENLOG, WebResUtil.getHBuilderPrintUrl(obtainApp.convert2RelPath(WebResUtil.getOriginalUrl(str))), WebResUtil.getHBuilderPrintUrl(obtainApp.convert2RelPath(WebResUtil.getOriginalUrl(obtainUrl)))));
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void a(IWebview iWebview, JSONArray jSONArray, c cVar, String str) {
        String str2;
        if (this.j.a(cVar)) {
            Logger.d(Logger.StreamApp_TAG, "showWebview url=" + cVar.A);
            cVar.B = 1;
            this.j.f(cVar);
            cVar.C = new Object[]{iWebview, jSONArray, cVar, str};
            return;
        }
        cVar.v = System.currentTimeMillis();
        cVar.G = true;
        String string = JSONUtil.getString(jSONArray, 0);
        String string2 = JSONUtil.getString(jSONArray, 1);
        String string3 = JSONUtil.getString(jSONArray, 3);
        this.P = string3;
        if (!PdrUtil.isEmpty(string3)) {
            this.Q = iWebview;
        }
        AnimOptions animOptions = ((AdaFrameItem) cVar.z).getAnimOptions();
        if (PdrUtil.isEquals("auto", string)) {
            str2 = animOptions.mAnimType;
        } else {
            str2 = PdrUtil.isEmpty(string) ? "none" : string;
        }
        animOptions.mAnimType = str2;
        boolean z2 = !PdrUtil.isEquals("none", str2);
        if (!PdrUtil.isEmpty(string2)) {
            animOptions.duration_show = PdrUtil.parseInt(string2, animOptions.duration_show);
        } else if (animOptions.mAnimType.equals(AnimOptions.ANIM_POP_IN)) {
            animOptions.duration_show = 300;
        }
        if (!cVar.H && cVar.J) {
            z2 = false;
        }
        this.j.a(str, cVar, this.j.c(this));
        a(iWebview, JSONUtil.getJSONObject(jSONArray, 4), cVar, string);
        if (cVar.H) {
            animOptions.mOption = 4;
            this.j.d.processEvent(IMgr.MgrType.WindowMgr, 24, cVar.z);
        } else {
            animOptions.mOption = 0;
            cVar.J = true;
            this.j.d.processEvent(IMgr.MgrType.WindowMgr, 1, new Object[]{cVar.z, Boolean.valueOf(z2)});
        }
        cVar.H = false;
        Logger.d(Logger.VIEW_VISIBLE_TAG, "show " + cVar.z + ";webview_name=" + r().obtainFrameId());
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v7, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v12, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(io.dcloud.common.DHInterface.IWebview r21, org.json.JSONObject r22, io.dcloud.feature.ui.c r23, java.lang.String r24) {
        /*
            r20 = this;
            r0 = r20
            r1 = r22
            r2 = r23
            java.lang.String r3 = "auto"
            if (r1 == 0) goto L_0x0124
            java.lang.String r5 = "acceleration"
            java.lang.String r5 = r1.optString(r5)
            boolean r6 = android.text.TextUtils.isEmpty(r5)
            if (r6 == 0) goto L_0x0017
            goto L_0x0018
        L_0x0017:
            r3 = r5
        L_0x0018:
            java.lang.String r5 = "capture"
            java.lang.String r6 = "action"
            java.lang.String r7 = "none"
            java.lang.String r6 = r1.optString(r6, r7)
            io.dcloud.common.DHInterface.IFrameView r8 = r2.z
            r8.setAccelerationType(r3)
            boolean r8 = r1.has(r5)
            java.lang.String r11 = "getNativeView"
            java.lang.String r12 = "nativeobj"
            r13 = 4
            java.lang.String r14 = "nativeView"
            java.lang.String r15 = "__id__"
            r16 = 1
            r17 = 0
            java.lang.String r9 = "type"
            if (r8 == 0) goto L_0x00ad
            org.json.JSONObject r5 = r1.optJSONObject(r5)
            if (r5 != 0) goto L_0x0043
            return
        L_0x0043:
            java.lang.String r8 = r5.optString(r15)
            boolean r18 = r5.has(r9)
            if (r18 == 0) goto L_0x008d
            java.lang.String r5 = r5.optString(r9)
            boolean r5 = r5.equals(r14)
            if (r5 == 0) goto L_0x008d
            io.dcloud.feature.ui.a r5 = r0.j
            io.dcloud.common.DHInterface.AbsMgr r5 = r5.d
            io.dcloud.common.DHInterface.IMgr$MgrType r10 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            java.lang.Object[] r4 = new java.lang.Object[r13]
            r4[r17] = r21
            r4[r16] = r12
            r13 = 2
            r4[r13] = r11
            r19 = r11
            java.lang.Object[] r11 = new java.lang.Object[r13]
            io.dcloud.common.DHInterface.IFrameView r13 = r0.z
            r11[r17] = r13
            r11[r16] = r8
            r8 = 3
            r4[r8] = r11
            r8 = 10
            java.lang.Object r4 = r5.processEvent(r10, r8, r4)
            if (r4 == 0) goto L_0x00af
            boolean r5 = r4 instanceof io.dcloud.common.DHInterface.INativeView
            if (r5 == 0) goto L_0x00af
            io.dcloud.common.DHInterface.IFrameView r5 = r2.z
            io.dcloud.common.DHInterface.INativeView r4 = (io.dcloud.common.DHInterface.INativeView) r4
            r5.setSnapshotView(r4, r6)
            io.dcloud.common.DHInterface.IFrameView r4 = r2.z
            r5 = 0
            r4.setSnapshot(r5)
            goto L_0x00af
        L_0x008d:
            r19 = r11
            io.dcloud.common.DHInterface.IFrameView r4 = r2.z
            io.dcloud.common.DHInterface.IWebview r4 = r4.obtainWebView()
            io.dcloud.common.DHInterface.INativeBitmap r4 = r0.g(r4, r8)
            io.dcloud.common.DHInterface.IFrameView r5 = r2.z
            if (r4 == 0) goto L_0x00a2
            android.graphics.Bitmap r4 = r4.getBitmap()
            goto L_0x00a3
        L_0x00a2:
            r4 = 0
        L_0x00a3:
            r5.setSnapshot(r4)
            io.dcloud.common.DHInterface.IFrameView r4 = r2.z
            r5 = 0
            r4.setSnapshotView(r5, r7)
            goto L_0x00af
        L_0x00ad:
            r19 = r11
        L_0x00af:
            java.lang.String r4 = "otherCapture"
            io.dcloud.common.DHInterface.IFrameView r2 = r2.z
            io.dcloud.common.DHInterface.IFrameView r2 = r2.findPageB()
            if (r2 == 0) goto L_0x013d
            r2.setAccelerationType(r3)
            boolean r3 = r1.has(r4)
            if (r3 == 0) goto L_0x013d
            org.json.JSONObject r1 = r1.optJSONObject(r4)
            if (r1 != 0) goto L_0x00c9
            return
        L_0x00c9:
            java.lang.String r3 = r1.optString(r15)
            boolean r4 = r1.has(r9)
            if (r4 == 0) goto L_0x010c
            java.lang.String r1 = r1.optString(r9)
            boolean r1 = r1.equals(r14)
            if (r1 == 0) goto L_0x010c
            io.dcloud.feature.ui.a r1 = r0.j
            io.dcloud.common.DHInterface.AbsMgr r1 = r1.d
            io.dcloud.common.DHInterface.IMgr$MgrType r4 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r5 = 4
            java.lang.Object[] r5 = new java.lang.Object[r5]
            r5[r17] = r21
            r5[r16] = r12
            r7 = 2
            r5[r7] = r19
            java.lang.Object[] r7 = new java.lang.Object[r7]
            r7[r17] = r2
            r7[r16] = r3
            r3 = 3
            r5[r3] = r7
            r3 = 10
            java.lang.Object r1 = r1.processEvent(r4, r3, r5)
            if (r1 == 0) goto L_0x013d
            boolean r3 = r1 instanceof io.dcloud.common.DHInterface.INativeView
            if (r3 == 0) goto L_0x013d
            io.dcloud.common.DHInterface.INativeView r1 = (io.dcloud.common.DHInterface.INativeView) r1
            r2.setSnapshotView(r1, r6)
            r1 = 0
            r2.setSnapshot(r1)
            goto L_0x013d
        L_0x010c:
            io.dcloud.common.DHInterface.IWebview r1 = r2.obtainWebView()
            io.dcloud.common.DHInterface.INativeBitmap r1 = r0.g(r1, r3)
            if (r1 == 0) goto L_0x011b
            android.graphics.Bitmap r5 = r1.getBitmap()
            goto L_0x011c
        L_0x011b:
            r5 = 0
        L_0x011c:
            r2.setSnapshot(r5)
            r1 = 0
            r2.setSnapshotView(r1, r7)
            goto L_0x013d
        L_0x0124:
            r1 = 0
            io.dcloud.common.DHInterface.IFrameView r4 = r2.z
            r4.setSnapshot(r1)
            io.dcloud.common.DHInterface.IFrameView r4 = r2.z
            r4.setAccelerationType(r3)
            io.dcloud.common.DHInterface.IFrameView r2 = r2.z
            io.dcloud.common.DHInterface.IFrameView r2 = r2.findPageB()
            if (r2 == 0) goto L_0x013d
            r2.setSnapshot(r1)
            r2.setAccelerationType(r3)
        L_0x013d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.c.a(io.dcloud.common.DHInterface.IWebview, org.json.JSONObject, io.dcloud.feature.ui.c, java.lang.String):void");
    }

    /* access modifiers changed from: package-private */
    public void a(IWebview iWebview, JSONArray jSONArray, c cVar) {
        String string = JSONUtil.getString(jSONArray, 0);
        String string2 = JSONUtil.getString(jSONArray, 1);
        IFrameView iFrameView = cVar.z;
        g gVar = null;
        f fVar = TextUtils.isEmpty(string2) ? null : new f(iWebview, string2);
        if (!TextUtils.isEmpty(string2)) {
            gVar = new g(iWebview, string2);
        }
        iFrameView.captureSnapshot(string, fVar, gVar);
    }

    /* access modifiers changed from: package-private */
    public void a(c cVar, boolean z2) {
        cVar.z.obtainWebView().reload(z2);
    }

    /* access modifiers changed from: package-private */
    public boolean a(JSONObject jSONObject, boolean z2) {
        boolean z3 = false;
        if (!jSONObject.isNull(AbsoluteConst.JSON_KEY_ZINDEX)) {
            try {
                int parseInt = Integer.parseInt(JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_ZINDEX));
                if (parseInt != this.F) {
                    z3 = true;
                    this.F = parseInt;
                    ((AdaFrameView) this.z).mZIndex = parseInt;
                    if (z2) {
                        this.j.g(this);
                    }
                }
            } catch (Exception unused) {
            }
        }
        return z3;
    }

    private static String a(ArrayList arrayList) {
        StringBuffer stringBuffer = new StringBuffer(Operators.ARRAY_START_STR);
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i2 = 0; i2 < size; i2++) {
                b bVar = (b) arrayList.get(i2);
                if (bVar instanceof c) {
                    stringBuffer.append(((c) bVar).h());
                } else {
                    stringBuffer.append("'" + bVar.l + "'");
                }
                if (i2 != size - 1) {
                    stringBuffer.append(",");
                }
            }
        }
        stringBuffer.append(Operators.ARRAY_END_STR);
        return stringBuffer.toString();
    }

    /* JADX WARNING: Removed duplicated region for block: B:102:0x0334  */
    /* JADX WARNING: Removed duplicated region for block: B:145:0x043d  */
    /* JADX WARNING: Removed duplicated region for block: B:149:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:150:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0114 A[SYNTHETIC, Splitter:B:16:0x0114] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0149  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x014e  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0160  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0191  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x0196  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x01a9  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x01cb  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x01d0  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x01e3  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x01fb  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0202  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0214  */
    /* JADX WARNING: Removed duplicated region for block: B:96:0x02ca  */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x02d8  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(io.dcloud.common.DHInterface.IWebview r40, io.dcloud.feature.ui.c r41, org.json.JSONObject r42, org.json.JSONObject r43) {
        /*
            r39 = this;
            r0 = r39
            r1 = r41
            r2 = r42
            r3 = r43
            java.lang.String r4 = "titletext"
            java.lang.String r5 = "titleText"
            r6 = 3
            r7 = 2
            r8 = 0
            r9 = 1
            if (r2 != 0) goto L_0x0065
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z
            io.dcloud.common.adapter.ui.AdaFrameItem r2 = (io.dcloud.common.adapter.ui.AdaFrameItem) r2
            io.dcloud.common.adapter.util.ViewOptions r4 = r2.obtainFrameOptions()
            io.dcloud.common.DHInterface.IFrameView r5 = r1.z
            java.lang.String r5 = io.dcloud.common.util.TitleNViewUtil.getTitleNViewId(r5)
            io.dcloud.feature.ui.a r10 = r0.j
            io.dcloud.common.DHInterface.AbsMgr r10 = r10.d
            io.dcloud.common.DHInterface.IMgr$MgrType r11 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr
            java.lang.Object[] r6 = new java.lang.Object[r6]
            r6[r8] = r2
            r6[r9] = r3
            r6[r7] = r5
            r5 = 72
            r10.processEvent(r11, r5, r6)
            io.dcloud.common.DHInterface.IWebview r5 = r41.r()
            r4.setTitleNView(r3, r5)
            io.dcloud.feature.ui.a r3 = r0.j
            io.dcloud.common.DHInterface.AbsMgr r3 = r3.d
            java.lang.Object[] r4 = new java.lang.Object[r7]
            r4[r8] = r2
            io.dcloud.common.DHInterface.IFrameView r2 = r40.obtainFrameView()
            r4[r9] = r2
            r2 = 73
            r3.processEvent(r11, r2, r4)
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()
            boolean r2 = r2.isUniWebView()
            if (r2 == 0) goto L_0x0064
            io.dcloud.common.DHInterface.IFrameView r1 = r1.z
            io.dcloud.common.DHInterface.IWebview r1 = r1.obtainWebView()
            io.dcloud.common.adapter.ui.AdaUniWebView r1 = (io.dcloud.common.adapter.ui.AdaUniWebView) r1
            r1.titleNViewRefresh()
        L_0x0064:
            return
        L_0x0065:
            if (r3 != 0) goto L_0x0068
            return
        L_0x0068:
            boolean r10 = io.dcloud.common.util.TitleNViewUtil.paddingIsChanged(r42, r43)
            boolean r11 = io.dcloud.common.util.TitleNViewUtil.backgroundIsChanged(r42, r43)
            boolean r12 = io.dcloud.common.util.TitleNViewUtil.backgroundImageIsChanged(r42, r43)
            boolean r13 = io.dcloud.common.util.TitleNViewUtil.titleAlignIsChanged(r42, r43)
            boolean r14 = io.dcloud.common.util.TitleNViewUtil.redDotChange(r42, r43)
            io.dcloud.common.util.TitleNViewUtil.titleNViewStyleNoTitle(r42)
            boolean r15 = io.dcloud.common.util.TitleNViewUtil.titleIsChanged(r42, r43)
            boolean r16 = io.dcloud.common.util.TitleNViewUtil.titleColorIsChanged(r42, r43)
            boolean r17 = io.dcloud.common.util.TitleNViewUtil.titleSizeIsChanged(r42, r43)
            boolean r18 = io.dcloud.common.util.TitleNViewUtil.titleOverflowIsChanged(r42, r43)
            boolean r19 = io.dcloud.common.util.TitleNViewUtil.splitLineIsChanged(r42, r43)
            boolean r20 = io.dcloud.common.util.TitleNViewUtil.backButtonIsChanged(r42, r43)
            boolean r21 = io.dcloud.common.util.TitleNViewUtil.progressIsChanged(r42, r43)
            java.lang.String r6 = "homeButton"
            boolean r6 = r3.has(r6)
            boolean r22 = io.dcloud.common.util.TitleNViewUtil.isButtonsIsChanged(r42, r43)
            boolean r23 = io.dcloud.common.util.TitleNViewUtil.isSearchInputChange(r42, r43)
            boolean r24 = io.dcloud.common.util.TitleNViewUtil.subTitleIconChanged(r42, r43)
            boolean r25 = io.dcloud.common.util.TitleNViewUtil.isShadowChanged(r42, r43)
            io.dcloud.common.DHInterface.IFrameView r7 = r1.z
            java.lang.String r7 = io.dcloud.common.util.TitleNViewUtil.getTitleNViewId(r7)
            java.lang.String r27 = r42.toString()
            org.json.JSONObject r9 = io.dcloud.common.util.JSONUtil.createJSONObject(r27)
            java.lang.String r27 = r43.toString()
            org.json.JSONObject r8 = io.dcloud.common.util.JSONUtil.createJSONObject(r27)
            io.dcloud.common.util.JSONUtil.combinJSONObject(r9, r8)
            r40 = r12
            io.dcloud.common.DHInterface.IFrameView r12 = r1.z
            io.dcloud.common.adapter.ui.AdaFrameItem r12 = (io.dcloud.common.adapter.ui.AdaFrameItem) r12
            io.dcloud.common.adapter.util.ViewOptions r12 = r12.obtainFrameOptions()
            int r12 = r12.coverage
            if (r9 == 0) goto L_0x010c
            r27 = r13
            java.lang.String r13 = "coverage"
            boolean r29 = r9.has(r13)
            if (r29 == 0) goto L_0x0109
            r29 = r14
            io.dcloud.common.DHInterface.IFrameView r14 = r1.z
            io.dcloud.common.adapter.ui.AdaFrameItem r14 = (io.dcloud.common.adapter.ui.AdaFrameItem) r14
            io.dcloud.common.adapter.util.ViewOptions r14 = r14.obtainFrameOptions()
            java.lang.String r13 = r9.optString(r13)
            android.content.Context r30 = r41.a()
            r31 = r6
            int r6 = io.dcloud.common.adapter.util.PlatformUtil.SCREEN_WIDTH(r30)
            io.dcloud.common.DHInterface.IWebview r30 = r41.r()
            float r2 = r30.getScale()
            int r2 = io.dcloud.common.util.PdrUtil.convertToScreenInt(r13, r6, r12, r2)
            r14.coverage = r2
            goto L_0x0112
        L_0x0109:
            r31 = r6
            goto L_0x0110
        L_0x010c:
            r31 = r6
            r27 = r13
        L_0x0110:
            r29 = r14
        L_0x0112:
            if (r9 == 0) goto L_0x0448
            boolean r6 = r3.has(r5)     // Catch:{ Exception -> 0x013e }
            if (r6 == 0) goto L_0x0129
            java.lang.Object r4 = r9.get(r5)     // Catch:{ Exception -> 0x013e }
            if (r4 == 0) goto L_0x013e
            boolean r5 = r4 instanceof java.lang.String     // Catch:{ Exception -> 0x013e }
            if (r5 == 0) goto L_0x013e
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x013e }
            goto L_0x013f
        L_0x0129:
            boolean r5 = r3.has(r4)     // Catch:{ Exception -> 0x013e }
            if (r5 == 0) goto L_0x013e
            java.lang.Object r4 = r9.get(r4)     // Catch:{ Exception -> 0x013e }
            if (r4 == 0) goto L_0x013e
            boolean r5 = r4 instanceof java.lang.String     // Catch:{ Exception -> 0x013e }
            if (r5 == 0) goto L_0x013e
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x013e }
            goto L_0x013f
        L_0x013e:
            r4 = 0
        L_0x013f:
            java.lang.String r5 = "titleColor"
            boolean r6 = r3.has(r5)
            java.lang.String r12 = "titlecolor"
            if (r6 == 0) goto L_0x014e
            java.lang.String r6 = r9.optString(r5)
            goto L_0x015a
        L_0x014e:
            boolean r6 = r3.has(r12)
            if (r6 == 0) goto L_0x0159
            java.lang.String r6 = r9.optString(r12)
            goto L_0x015a
        L_0x0159:
            r6 = 0
        L_0x015a:
            boolean r13 = android.text.TextUtils.isEmpty(r6)
            if (r13 == 0) goto L_0x016e
            java.lang.String r6 = r9.optString(r5)
            boolean r5 = android.text.TextUtils.isEmpty(r6)
            if (r5 == 0) goto L_0x016e
            java.lang.String r6 = r9.optString(r12)
        L_0x016e:
            java.lang.String r5 = "type"
            java.lang.String r12 = r3.optString(r5)
            java.lang.String r13 = "transparent"
            boolean r12 = r13.equals(r12)
            r14 = 0
            if (r12 == 0) goto L_0x0187
            boolean r12 = android.text.TextUtils.isEmpty(r6)
            if (r12 != 0) goto L_0x0187
            java.lang.String r6 = io.dcloud.common.util.TitleNViewUtil.changeColorAlpha((java.lang.String) r6, (float) r14)
        L_0x0187:
            java.lang.String r12 = "titleSize"
            boolean r30 = r3.has(r12)
            java.lang.String r2 = "titlesize"
            if (r30 == 0) goto L_0x0196
            java.lang.String r30 = r9.optString(r12)
            goto L_0x01a3
        L_0x0196:
            boolean r30 = r3.has(r2)
            if (r30 == 0) goto L_0x01a1
            java.lang.String r30 = r9.optString(r2)
            goto L_0x01a3
        L_0x01a1:
            r30 = 0
        L_0x01a3:
            boolean r33 = android.text.TextUtils.isEmpty(r30)
            if (r33 == 0) goto L_0x01b7
            java.lang.String r30 = r9.optString(r12)
            boolean r12 = android.text.TextUtils.isEmpty(r30)
            if (r12 == 0) goto L_0x01b7
            java.lang.String r30 = r9.optString(r2)
        L_0x01b7:
            r2 = r30
            java.lang.String r12 = "titleOverflow"
            java.lang.String r12 = r9.optString(r12)
            java.lang.String r14 = "backgroundColor"
            boolean r33 = r3.has(r14)
            r34 = r10
            java.lang.String r10 = "backgroundcolor"
            if (r33 == 0) goto L_0x01d0
            java.lang.String r33 = r9.optString(r14)
            goto L_0x01dd
        L_0x01d0:
            boolean r33 = r3.has(r10)
            if (r33 == 0) goto L_0x01db
            java.lang.String r33 = r9.optString(r10)
            goto L_0x01dd
        L_0x01db:
            r33 = 0
        L_0x01dd:
            boolean r35 = android.text.TextUtils.isEmpty(r33)
            if (r35 == 0) goto L_0x01f1
            java.lang.String r33 = r9.optString(r14)
            boolean r14 = android.text.TextUtils.isEmpty(r33)
            if (r14 == 0) goto L_0x01f1
            java.lang.String r33 = r9.optString(r10)
        L_0x01f1:
            r10 = r33
            java.lang.String r14 = "backgroundImage"
            boolean r33 = r3.has(r14)
            if (r33 == 0) goto L_0x0202
            java.lang.String r14 = r9.optString(r14)
            r32 = r14
            goto L_0x0204
        L_0x0202:
            r32 = 0
        L_0x0204:
            java.lang.String r14 = "','"
            java.lang.String r3 = "['"
            java.lang.String r33 = "nativeobj"
            r35 = r12
            if (r11 == 0) goto L_0x02ca
            boolean r11 = android.text.TextUtils.isEmpty(r10)
            if (r11 != 0) goto L_0x02ca
            java.lang.String r11 = r9.optString(r5)
            boolean r11 = r13.equals(r11)
            if (r11 == 0) goto L_0x0224
            r11 = 0
            java.lang.String r11 = io.dcloud.common.util.TitleNViewUtil.changeColorAlpha((java.lang.String) r10, (float) r11)
            goto L_0x0238
        L_0x0224:
            java.lang.String r11 = r9.optString(r5)
            java.lang.String r13 = "float"
            boolean r11 = r13.equals(r11)
            if (r11 == 0) goto L_0x0232
            r11 = r10
            goto L_0x0238
        L_0x0232:
            r11 = 1065353216(0x3f800000, float:1.0)
            java.lang.String r11 = io.dcloud.common.util.TitleNViewUtil.changeColorAlpha((java.lang.String) r10, (float) r11)
        L_0x0238:
            io.dcloud.common.DHInterface.IFrameView r13 = r1.z
            io.dcloud.common.adapter.ui.AdaFrameItem r13 = (io.dcloud.common.adapter.ui.AdaFrameItem) r13
            io.dcloud.common.adapter.util.ViewOptions r13 = r13.obtainFrameOptions()
            boolean r13 = r13.isStatusbar
            if (r13 != 0) goto L_0x0271
            io.dcloud.common.DHInterface.IFrameView r13 = r1.z
            io.dcloud.common.DHInterface.IApp r13 = r13.obtainApp()
            io.dcloud.common.util.AppStatusBarManager r13 = r13.obtainStatusBarMgr()
            boolean r13 = r13.isImmersive
            if (r13 == 0) goto L_0x0271
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r12 = ",'statusbar':{'background':'"
            r13.append(r12)
            r13.append(r11)
            java.lang.String r12 = "','backgroundnoalpha':'"
            r13.append(r12)
            r13.append(r10)
            java.lang.String r10 = "'}"
            r13.append(r10)
            java.lang.String r10 = r13.toString()
            goto L_0x0273
        L_0x0271:
            java.lang.String r10 = ""
        L_0x0273:
            io.dcloud.feature.ui.a r12 = r0.j
            io.dcloud.common.DHInterface.AbsMgr r12 = r12.d
            io.dcloud.common.DHInterface.IMgr$MgrType r13 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r36 = r5
            r37 = r9
            r5 = 4
            java.lang.Object[] r9 = new java.lang.Object[r5]
            io.dcloud.common.DHInterface.IWebview r5 = r41.r()
            r28 = 0
            r9[r28] = r5
            r5 = 1
            r9[r5] = r33
            java.lang.String r5 = "setStyle"
            r26 = 2
            r9[r26] = r5
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            r5.append(r3)
            r5.append(r7)
            r5.append(r14)
            r5.append(r7)
            r38 = r2
            java.lang.String r2 = "',{'backgroundColor':'"
            r5.append(r2)
            r5.append(r11)
            java.lang.String r2 = "'"
            r5.append(r2)
            r5.append(r10)
            java.lang.String r2 = "}]"
            r5.append(r2)
            java.lang.String r2 = r5.toString()
            org.json.JSONArray r2 = io.dcloud.common.util.JSONUtil.createJSONArray(r2)
            r5 = 3
            r9[r5] = r2
            r2 = 1
            r12.processEvent(r13, r2, r9)
            goto L_0x02d0
        L_0x02ca:
            r38 = r2
            r36 = r5
            r37 = r9
        L_0x02d0:
            java.lang.String r2 = "tags"
            org.json.JSONArray r2 = r8.optJSONArray(r2)
            if (r2 == 0) goto L_0x0322
            io.dcloud.feature.ui.a r5 = r0.j
            io.dcloud.common.DHInterface.AbsMgr r5 = r5.d
            io.dcloud.common.DHInterface.IMgr$MgrType r8 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r9 = 4
            java.lang.Object[] r10 = new java.lang.Object[r9]
            io.dcloud.common.DHInterface.IWebview r9 = r41.r()
            r11 = 0
            r10[r11] = r9
            r9 = 1
            r10[r9] = r33
            java.lang.String r9 = "view_draw"
            r11 = 2
            r10[r11] = r9
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            r9.append(r3)
            r9.append(r7)
            r9.append(r14)
            r9.append(r7)
            java.lang.String r11 = "',"
            r9.append(r11)
            java.lang.String r2 = r2.toString()
            r9.append(r2)
            java.lang.String r2 = "]"
            r9.append(r2)
            java.lang.String r2 = r9.toString()
            org.json.JSONArray r2 = io.dcloud.common.util.JSONUtil.createJSONArray(r2)
            r9 = 3
            r10[r9] = r2
            r2 = 1
            r5.processEvent(r8, r2, r10)
        L_0x0322:
            io.dcloud.feature.ui.a r2 = r0.j
            io.dcloud.common.DHInterface.AbsMgr r2 = r2.d
            io.dcloud.common.DHInterface.IWebview r5 = r41.r()
            io.dcloud.common.DHInterface.IFrameView r8 = r1.z
            java.lang.Object r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNView(r2, r5, r8, r7)
            boolean r5 = r2 instanceof io.dcloud.common.DHInterface.ITitleNView
            if (r5 == 0) goto L_0x0431
            io.dcloud.common.DHInterface.ITitleNView r2 = (io.dcloud.common.DHInterface.ITitleNView) r2
            if (r15 == 0) goto L_0x033b
            r2.setTitleText(r4)
        L_0x033b:
            if (r16 == 0) goto L_0x0340
            r2.setTitleColor((java.lang.String) r6)
        L_0x0340:
            if (r17 == 0) goto L_0x0347
            r4 = r38
            r2.setTitleSize(r4)
        L_0x0347:
            if (r18 == 0) goto L_0x034e
            r4 = r35
            r2.setTitleOverflow(r4)
        L_0x034e:
            if (r34 == 0) goto L_0x035a
            io.dcloud.common.DHInterface.IWebview r4 = r41.r()
            r5 = r37
            io.dcloud.common.util.TitleNViewUtil.setTitleNViewPadding(r2, r4, r5)
            goto L_0x035c
        L_0x035a:
            r5 = r37
        L_0x035c:
            if (r19 == 0) goto L_0x037f
            io.dcloud.common.DHInterface.IWebview r9 = r41.r()
            java.lang.String r4 = "splitLine"
            r6 = r42
            org.json.JSONObject r10 = r6.optJSONObject(r4)
            java.lang.String r4 = "splitLine"
            r6 = r3
            r3 = r43
            org.json.JSONObject r11 = r3.optJSONObject(r4)
            r3 = r36
            java.lang.String r13 = r5.optString(r3)
            r12 = 0
            r8 = r2
            io.dcloud.common.util.TitleNViewUtil.setSplitLine(r8, r9, r10, r11, r12, r13)
            goto L_0x0380
        L_0x037f:
            r6 = r3
        L_0x0380:
            if (r20 == 0) goto L_0x038b
            io.dcloud.common.DHInterface.IFrameView r3 = r1.z
            int r3 = r3.getFrameType()
            io.dcloud.common.util.TitleNViewUtil.setBackButton(r2, r5, r3)
        L_0x038b:
            if (r21 == 0) goto L_0x0390
            io.dcloud.common.util.TitleNViewUtil.setProgress(r2, r5)
        L_0x0390:
            if (r23 == 0) goto L_0x039b
            io.dcloud.common.DHInterface.IFrameView r3 = r1.z
            io.dcloud.common.DHInterface.IWebview r3 = r3.obtainWebView()
            io.dcloud.common.util.TitleNViewUtil.setSearchInput(r2, r5, r3)
        L_0x039b:
            if (r22 == 0) goto L_0x03b2
            r2.clearButtons()
            io.dcloud.common.DHInterface.IFrameView r3 = r1.z
            io.dcloud.common.DHInterface.IWebview r3 = r3.obtainWebView()
            io.dcloud.common.util.TitleNViewUtil.setButtons(r2, r5, r3)
            io.dcloud.common.DHInterface.IFrameView r3 = r1.z
            int r3 = r3.getFrameType()
            io.dcloud.common.util.TitleNViewUtil.setBackButton(r2, r5, r3)
        L_0x03b2:
            if (r31 != 0) goto L_0x03b6
            if (r22 == 0) goto L_0x03bf
        L_0x03b6:
            io.dcloud.common.DHInterface.IFrameView r3 = r1.z
            int r3 = r3.getFrameType()
            io.dcloud.common.util.TitleNViewUtil.setHomeButton(r2, r5, r3)
        L_0x03bf:
            if (r29 == 0) goto L_0x03c4
            io.dcloud.common.util.TitleNViewUtil.setRedDotColor(r2, r5)
        L_0x03c4:
            if (r27 == 0) goto L_0x03c9
            io.dcloud.common.util.TitleNViewUtil.setTitleAlign(r2, r5)
        L_0x03c9:
            if (r40 == 0) goto L_0x0414
            if (r32 == 0) goto L_0x0414
            io.dcloud.feature.ui.a r3 = r0.j
            io.dcloud.common.DHInterface.AbsMgr r3 = r3.d
            io.dcloud.common.DHInterface.IMgr$MgrType r4 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r8 = 4
            java.lang.Object[] r8 = new java.lang.Object[r8]
            io.dcloud.common.DHInterface.IWebview r9 = r41.r()
            r10 = 0
            r8[r10] = r9
            r9 = 1
            r8[r9] = r33
            java.lang.String r9 = "setStyle"
            r10 = 2
            r8[r10] = r9
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            r9.append(r6)
            r9.append(r7)
            r9.append(r14)
            r9.append(r7)
            java.lang.String r6 = "',{'backgroundImage':'"
            r9.append(r6)
            r14 = r32
            r9.append(r14)
            java.lang.String r6 = "'}]"
            r9.append(r6)
            java.lang.String r6 = r9.toString()
            org.json.JSONArray r6 = io.dcloud.common.util.JSONUtil.createJSONArray(r6)
            r7 = 3
            r8[r7] = r6
            r6 = 1
            r3.processEvent(r4, r6, r8)
        L_0x0414:
            if (r2 == 0) goto L_0x0427
            java.lang.String r3 = "backgroundRepeat"
            boolean r3 = r5.has(r3)
            if (r3 == 0) goto L_0x0427
            java.lang.String r3 = "backgroundRepeat"
            java.lang.String r3 = r5.optString(r3)
            r2.setBackgroundRepeat(r3)
        L_0x0427:
            if (r24 == 0) goto L_0x042c
            io.dcloud.common.util.TitleNViewUtil.setSubTitleIcon(r2, r5)
        L_0x042c:
            if (r25 == 0) goto L_0x0431
            io.dcloud.common.util.TitleNViewUtil.setShadow(r2, r5)
        L_0x0431:
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()
            boolean r2 = r2.isUniWebView()
            if (r2 == 0) goto L_0x0448
            io.dcloud.common.DHInterface.IFrameView r1 = r1.z
            io.dcloud.common.DHInterface.IWebview r1 = r1.obtainWebView()
            io.dcloud.common.adapter.ui.AdaUniWebView r1 = (io.dcloud.common.adapter.ui.AdaUniWebView) r1
            r1.titleNViewRefresh()
        L_0x0448:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.c.a(io.dcloud.common.DHInterface.IWebview, io.dcloud.feature.ui.c, org.json.JSONObject, org.json.JSONObject):void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(io.dcloud.feature.ui.c r8, org.json.JSONObject r9) {
        /*
            r7 = this;
            if (r9 != 0) goto L_0x0003
            return
        L_0x0003:
            io.dcloud.common.DHInterface.IFrameView r0 = r8.z
            java.lang.String r0 = io.dcloud.common.util.BaseInfo.getUniNViewId(r0)
            r1 = 4
            java.lang.Object[] r2 = new java.lang.Object[r1]
            io.dcloud.common.DHInterface.IWebview r3 = r8.r()
            r4 = 0
            r2[r4] = r3
            io.dcloud.common.DHInterface.IFrameView r3 = r8.z
            android.view.View r3 = r3.obtainMainView()
            r5 = 1
            r2[r5] = r3
            r3 = 2
            r2[r3] = r9
            r9 = 3
            r2[r9] = r0
            io.dcloud.feature.ui.a r0 = r7.j
            io.dcloud.common.DHInterface.AbsMgr r0 = r0.d
            io.dcloud.common.DHInterface.IMgr$MgrType r6 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            java.lang.Object[] r1 = new java.lang.Object[r1]
            io.dcloud.common.DHInterface.IWebview r8 = r8.r()
            io.dcloud.common.DHInterface.IApp r8 = r8.obtainApp()
            r1[r4] = r8
            java.lang.String r8 = "weex,io.dcloud.feature.weex.WeexFeature"
            r1[r5] = r8
            java.lang.String r8 = "weexViewUpdate"
            r1[r3] = r8
            r1[r9] = r2
            r8 = 10
            r0.processEvent(r6, r8, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.c.a(io.dcloud.feature.ui.c, org.json.JSONObject):void");
    }
}
