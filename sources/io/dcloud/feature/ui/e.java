package io.dcloud.feature.ui;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.ui.webview.WebResUtil;
import io.dcloud.common.adapter.util.AnimOptions;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import io.dcloud.nineoldandroids.animation.Animator;
import io.dcloud.nineoldandroids.animation.ValueAnimator;
import io.dcloud.nineoldandroids.view.ViewHelper;
import java.util.HashMap;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

public class e {
    private static HashMap<String, String> a;
    AbsMgr b = null;
    HashMap<String, a> c = new HashMap<>(1);
    final boolean d = false;
    String e = null;

    class a implements MessageHandler.IMessages {
        final /* synthetic */ a a;
        final /* synthetic */ c b;

        a(a aVar, c cVar) {
            this.a = aVar;
            this.b = cVar;
        }

        public void execute(Object obj) {
            this.a.g(this.b);
        }
    }

    class b implements ValueAnimator.AnimatorUpdateListener {
        final /* synthetic */ View a;

        b(View view) {
            this.a = view;
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (this.a.getLayoutParams() instanceof AbsoluteLayout.LayoutParams) {
                AbsoluteLayout.LayoutParams layoutParams = (AbsoluteLayout.LayoutParams) this.a.getLayoutParams();
                layoutParams.height = this.a.getHeight();
                layoutParams.width = this.a.getWidth();
                try {
                    ViewHelper.setX(this.a, (float) ((Integer) valueAnimator.getAnimatedValue()).intValue());
                } catch (Exception unused) {
                    ViewHelper.setX(this.a, ((Float) valueAnimator.getAnimatedValue()).floatValue());
                }
                this.a.requestLayout();
            } else if (this.a.getLayoutParams() instanceof FrameLayout.LayoutParams) {
                ViewHelper.setX(this.a, ((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        }
    }

    class c implements Animator.AnimatorListener {
        final /* synthetic */ View a;
        final /* synthetic */ c b;
        final /* synthetic */ IWebview c;
        final /* synthetic */ String d;
        final /* synthetic */ String e;

        c(View view, c cVar, IWebview iWebview, String str, String str2) {
            this.a = view;
            this.b = cVar;
            this.c = iWebview;
            this.d = str;
            this.e = str2;
        }

        public void onAnimationCancel(Animator animator) {
        }

        public void onAnimationEnd(Animator animator) {
            c cVar;
            if (this.a != null && (cVar = this.b) != null && cVar.r() != null) {
                int a2 = e.this.a(this.a);
                int width = this.a.getWidth();
                if (a2 >= PlatformUtil.SCREEN_WIDTH(this.a.getContext()) || a2 <= (-width)) {
                    this.b.z.popFromViewStack();
                }
                if (this.c != null && !TextUtils.isEmpty(this.d)) {
                    String m = this.b.m();
                    if (TextUtils.isEmpty(m)) {
                        m = "";
                    }
                    Deprecated_JSUtil.execCallback(this.c, this.d, StringUtil.format("{\"id\":\"%s\",\"target\":%s}", m, this.b.h()), JSUtil.OK, true, true);
                }
                if (TextUtils.isEmpty(this.e)) {
                    return;
                }
                if ("hide".equals(this.e)) {
                    c cVar2 = this.b;
                    cVar2.a(cVar2.r(), "hide", JSONUtil.createJSONArray("[null,null,null]"));
                } else if (AbsoluteConst.EVENTS_CLOSE.equals(this.e)) {
                    c cVar3 = this.b;
                    cVar3.a(cVar3.r(), AbsoluteConst.EVENTS_CLOSE, JSONUtil.createJSONArray("[null,null,null]"));
                }
            }
        }

        public void onAnimationRepeat(Animator animator) {
        }

        public void onAnimationStart(Animator animator) {
        }
    }

    static /* synthetic */ class d {
        static final /* synthetic */ int[] a;

        /* JADX WARNING: Can't wrap try/catch for region: R(36:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|36) */
        /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0054 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0060 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0078 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0084 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0090 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x009c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x00a8 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x00b4 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x00c0 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                io.dcloud.feature.ui.e$e[] r0 = io.dcloud.feature.ui.e.C0044e.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                a = r0
                io.dcloud.feature.ui.e$e r1 = io.dcloud.feature.ui.e.C0044e.findWindowByName     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x001d }
                io.dcloud.feature.ui.e$e r1 = io.dcloud.feature.ui.e.C0044e.getTopWebview     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0028 }
                io.dcloud.feature.ui.e$e r1 = io.dcloud.feature.ui.e.C0044e.prefetchURL     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0033 }
                io.dcloud.feature.ui.e$e r1 = io.dcloud.feature.ui.e.C0044e.prefetchURLs     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x003e }
                io.dcloud.feature.ui.e$e r1 = io.dcloud.feature.ui.e.C0044e.enumWindow     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0049 }
                io.dcloud.feature.ui.e$e r1 = io.dcloud.feature.ui.e.C0044e.getWapLaunchWebview     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0054 }
                io.dcloud.feature.ui.e$e r1 = io.dcloud.feature.ui.e.C0044e.getLaunchWebview     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0060 }
                io.dcloud.feature.ui.e$e r1 = io.dcloud.feature.ui.e.C0044e.getSecondWebview     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x006c }
                io.dcloud.feature.ui.e$e r1 = io.dcloud.feature.ui.e.C0044e.currentWebview     // Catch:{ NoSuchFieldError -> 0x006c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006c }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006c }
            L_0x006c:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0078 }
                io.dcloud.feature.ui.e$e r1 = io.dcloud.feature.ui.e.C0044e.createView     // Catch:{ NoSuchFieldError -> 0x0078 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0078 }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0078 }
            L_0x0078:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0084 }
                io.dcloud.feature.ui.e$e r1 = io.dcloud.feature.ui.e.C0044e.setcallbackid     // Catch:{ NoSuchFieldError -> 0x0084 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0084 }
                r2 = 11
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0084 }
            L_0x0084:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0090 }
                io.dcloud.feature.ui.e$e r1 = io.dcloud.feature.ui.e.C0044e.debug     // Catch:{ NoSuchFieldError -> 0x0090 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0090 }
                r2 = 12
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0090 }
            L_0x0090:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x009c }
                io.dcloud.feature.ui.e$e r1 = io.dcloud.feature.ui.e.C0044e.defaultHardwareAccelerated     // Catch:{ NoSuchFieldError -> 0x009c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x009c }
                r2 = 13
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x009c }
            L_0x009c:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x00a8 }
                io.dcloud.feature.ui.e$e r1 = io.dcloud.feature.ui.e.C0044e.startAnimation     // Catch:{ NoSuchFieldError -> 0x00a8 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00a8 }
                r2 = 14
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00a8 }
            L_0x00a8:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x00b4 }
                io.dcloud.feature.ui.e$e r1 = io.dcloud.feature.ui.e.C0044e.getDisplayWebview     // Catch:{ NoSuchFieldError -> 0x00b4 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00b4 }
                r2 = 15
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00b4 }
            L_0x00b4:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x00c0 }
                io.dcloud.feature.ui.e$e r1 = io.dcloud.feature.ui.e.C0044e.__callNativeModuleSync     // Catch:{ NoSuchFieldError -> 0x00c0 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00c0 }
                r2 = 16
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00c0 }
            L_0x00c0:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x00cc }
                io.dcloud.feature.ui.e$e r1 = io.dcloud.feature.ui.e.C0044e.postMessageToUniNView     // Catch:{ NoSuchFieldError -> 0x00cc }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x00cc }
                r2 = 17
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x00cc }
            L_0x00cc:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.e.d.<clinit>():void");
        }
    }

    /* renamed from: io.dcloud.feature.ui.e$e  reason: collision with other inner class name */
    private enum C0044e {
        findWindowByName,
        enumWindow,
        getLaunchWebview,
        getWapLaunchWebview,
        currentWebview,
        getTopWebview,
        createView,
        setcallbackid,
        debug,
        setLogs,
        isLogs,
        defaultHardwareAccelerated,
        startAnimation,
        getSecondWebview,
        getDisplayWebview,
        updateAppFrameViews,
        prefetchURL,
        prefetchURLs,
        postMessageToUniNView,
        __callNativeModuleSync
    }

    e(AbsMgr absMgr, String str) {
        this.b = absMgr;
        this.e = str;
        a();
    }

    public static String c(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if (str.startsWith("./")) {
            return str.substring(2);
        }
        if (str.startsWith("../")) {
            return str.substring(3);
        }
        return str.startsWith(".../") ? str.substring(4) : str;
    }

    public synchronized String b(IWebview iWebview, String str, JSONArray jSONArray) {
        return a(iWebview, str, jSONArray);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v37, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v15, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v16, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Code restructure failed: missing block: B:188:0x0473, code lost:
        r9 = r28;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:108:0x0238 A[SYNTHETIC, Splitter:B:108:0x0238] */
    /* JADX WARNING: Removed duplicated region for block: B:128:0x02bf  */
    /* JADX WARNING: Removed duplicated region for block: B:130:0x02c7 A[SYNTHETIC, Splitter:B:130:0x02c7] */
    /* JADX WARNING: Removed duplicated region for block: B:148:0x0354 A[Catch:{ Exception -> 0x0477 }] */
    /* JADX WARNING: Removed duplicated region for block: B:152:0x0366 A[Catch:{ Exception -> 0x0477 }] */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x0377 A[Catch:{ Exception -> 0x0477 }] */
    /* JADX WARNING: Removed duplicated region for block: B:204:0x04be  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0147 A[SYNTHETIC, Splitter:B:72:0x0147] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:200:0x04b2=Splitter:B:200:0x04b2, B:217:0x0513=Splitter:B:217:0x0513} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String a(io.dcloud.common.DHInterface.IWebview r29, java.lang.String r30, org.json.JSONArray r31) {
        /*
            r28 = this;
            r9 = r28
            r8 = r29
            r10 = r30
            r11 = r31
            java.lang.String r12 = "WebviewGroup"
            java.lang.String r1 = "NWindow"
            java.lang.String r2 = "js"
            r13 = 0
            r14 = 0
            java.lang.String r3 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r11, (int) r14)     // Catch:{ Exception -> 0x090f }
            r15 = 1
            java.lang.String r4 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r11, (int) r15)     // Catch:{ Exception -> 0x090f }
            r7 = 2
            org.json.JSONArray r5 = io.dcloud.common.util.JSONUtil.getJSONArray((org.json.JSONArray) r11, (int) r7)     // Catch:{ Exception -> 0x090f }
            java.lang.String r6 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r5, (int) r14)     // Catch:{ Exception -> 0x090f }
            org.json.JSONArray r14 = io.dcloud.common.util.JSONUtil.getJSONArray((org.json.JSONArray) r5, (int) r15)     // Catch:{ Exception -> 0x090f }
            io.dcloud.common.DHInterface.IApp r7 = r29.obtainApp()     // Catch:{ Exception -> 0x090f }
            if (r7 != 0) goto L_0x002d
            return r13
        L_0x002d:
            java.lang.String r13 = r7.obtainAppId()     // Catch:{ Exception -> 0x0908 }
            java.util.HashMap<java.lang.String, io.dcloud.feature.ui.a> r15 = r9.c     // Catch:{ Exception -> 0x0908 }
            java.lang.Object r15 = r15.get(r13)     // Catch:{ Exception -> 0x0908 }
            io.dcloud.feature.ui.a r15 = (io.dcloud.feature.ui.a) r15     // Catch:{ Exception -> 0x0908 }
            r19 = r2
            java.lang.String r2 = "Main_Path"
            if (r15 == 0) goto L_0x00a5
            io.dcloud.common.DHInterface.IApp r11 = r15.f     // Catch:{ Exception -> 0x009c }
            byte r11 = r11.obtainAppStatus()     // Catch:{ Exception -> 0x009c }
            r10 = 1
            if (r11 != r10) goto L_0x0049
            goto L_0x00a5
        L_0x0049:
            io.dcloud.common.DHInterface.IFrameView r10 = r29.obtainFrameView()     // Catch:{ Exception -> 0x009c }
            int r10 = r10.getFrameType()     // Catch:{ Exception -> 0x009c }
            r11 = 3
            if (r10 != r11) goto L_0x0063
            boolean r10 = r15.i     // Catch:{ Exception -> 0x009c }
            if (r10 != 0) goto L_0x0096
            io.dcloud.common.DHInterface.IFrameView r10 = r29.obtainFrameView()     // Catch:{ Exception -> 0x009c }
            r9.b((java.lang.String) r13, (io.dcloud.feature.ui.a) r15, (io.dcloud.common.DHInterface.IFrameView) r10)     // Catch:{ Exception -> 0x009c }
            r10 = 1
            r15.i = r10     // Catch:{ Exception -> 0x009c }
            goto L_0x0096
        L_0x0063:
            io.dcloud.common.DHInterface.IFrameView r10 = r29.obtainFrameView()     // Catch:{ Exception -> 0x009c }
            int r10 = r10.getFrameType()     // Catch:{ Exception -> 0x009c }
            r11 = 2
            if (r10 != r11) goto L_0x007d
            boolean r10 = r15.h     // Catch:{ Exception -> 0x009c }
            if (r10 != 0) goto L_0x0096
            io.dcloud.common.DHInterface.IFrameView r10 = r29.obtainFrameView()     // Catch:{ Exception -> 0x009c }
            r9.b((java.lang.String) r13, (io.dcloud.feature.ui.a) r15, (io.dcloud.common.DHInterface.IFrameView) r10)     // Catch:{ Exception -> 0x009c }
            r10 = 1
            r15.h = r10     // Catch:{ Exception -> 0x009c }
            goto L_0x0096
        L_0x007d:
            io.dcloud.common.DHInterface.IFrameView r10 = r29.obtainFrameView()     // Catch:{ Exception -> 0x009c }
            int r10 = r10.getFrameType()     // Catch:{ Exception -> 0x009c }
            r11 = 7
            if (r10 != r11) goto L_0x0096
            boolean r10 = r15.j     // Catch:{ Exception -> 0x009c }
            if (r10 != 0) goto L_0x0096
            io.dcloud.common.DHInterface.IFrameView r10 = r29.obtainFrameView()     // Catch:{ Exception -> 0x009c }
            r9.a((java.lang.String) r13, (io.dcloud.feature.ui.a) r15, (io.dcloud.common.DHInterface.IFrameView) r10)     // Catch:{ Exception -> 0x009c }
            r10 = 1
            r15.j = r10     // Catch:{ Exception -> 0x009c }
        L_0x0096:
            r22 = r7
            r20 = r12
            goto L_0x0137
        L_0x009c:
            r0 = move-exception
        L_0x009d:
            r1 = r30
            r12 = r31
        L_0x00a1:
            r2 = r0
        L_0x00a2:
            r13 = 0
            goto L_0x0914
        L_0x00a5:
            if (r15 == 0) goto L_0x00b5
            io.dcloud.common.DHInterface.IApp r10 = r15.f     // Catch:{ Exception -> 0x009c }
            byte r10 = r10.obtainAppStatus()     // Catch:{ Exception -> 0x009c }
            r11 = 1
            if (r10 != r11) goto L_0x00b5
            java.util.HashMap<java.lang.String, io.dcloud.feature.ui.a> r10 = r9.c     // Catch:{ Exception -> 0x009c }
            r10.remove(r13)     // Catch:{ Exception -> 0x009c }
        L_0x00b5:
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0902 }
            r10.<init>()     // Catch:{ Exception -> 0x0902 }
            java.lang.String r11 = "init AppWidgetMgr pAppid="
            r10.append(r11)     // Catch:{ Exception -> 0x0902 }
            r10.append(r13)     // Catch:{ Exception -> 0x0902 }
            java.lang.String r10 = r10.toString()     // Catch:{ Exception -> 0x0902 }
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r2, (java.lang.String) r10)     // Catch:{ Exception -> 0x0902 }
            io.dcloud.feature.ui.a r10 = new io.dcloud.feature.ui.a     // Catch:{ Exception -> 0x0902 }
            io.dcloud.common.DHInterface.AbsMgr r11 = r9.b     // Catch:{ Exception -> 0x0902 }
            r10.<init>(r11, r7)     // Catch:{ Exception -> 0x0902 }
            io.dcloud.common.DHInterface.IFrameView r11 = r29.obtainFrameView()     // Catch:{ Exception -> 0x0902 }
            int r11 = r11.getFrameType()     // Catch:{ Exception -> 0x0902 }
            r15 = 3
            if (r11 != r15) goto L_0x00dd
            r11 = 1
            goto L_0x00de
        L_0x00dd:
            r11 = 0
        L_0x00de:
            r10.i = r11     // Catch:{ Exception -> 0x0902 }
            io.dcloud.common.DHInterface.IFrameView r11 = r29.obtainFrameView()     // Catch:{ Exception -> 0x0902 }
            int r11 = r11.getFrameType()     // Catch:{ Exception -> 0x0902 }
            r15 = 2
            if (r11 != r15) goto L_0x00ed
            r11 = 1
            goto L_0x00ee
        L_0x00ed:
            r11 = 0
        L_0x00ee:
            r10.h = r11     // Catch:{ Exception -> 0x0902 }
            io.dcloud.common.DHInterface.IFrameView r11 = r29.obtainFrameView()     // Catch:{ Exception -> 0x0902 }
            int r11 = r11.getFrameType()     // Catch:{ Exception -> 0x0902 }
            r15 = 7
            if (r11 != r15) goto L_0x00fd
            r11 = 1
            goto L_0x00fe
        L_0x00fd:
            r11 = 0
        L_0x00fe:
            r10.j = r11     // Catch:{ Exception -> 0x0902 }
            java.util.HashMap<java.lang.String, io.dcloud.feature.ui.a> r11 = r9.c     // Catch:{ Exception -> 0x0902 }
            r11.put(r13, r10)     // Catch:{ Exception -> 0x0902 }
            io.dcloud.common.DHInterface.IFrameView r11 = r29.obtainFrameView()     // Catch:{ Exception -> 0x0902 }
            boolean r15 = r10.j     // Catch:{ Exception -> 0x0902 }
            if (r15 == 0) goto L_0x0119
            io.dcloud.common.DHInterface.IFrameView r11 = r29.obtainFrameView()     // Catch:{ Exception -> 0x009c }
            r9.a((java.lang.String) r13, (io.dcloud.feature.ui.a) r10, (io.dcloud.common.DHInterface.IFrameView) r11)     // Catch:{ Exception -> 0x009c }
            r22 = r7
            r20 = r12
            goto L_0x0136
        L_0x0119:
            r9.b((java.lang.String) r13, (io.dcloud.feature.ui.a) r10, (io.dcloud.common.DHInterface.IFrameView) r11)     // Catch:{ Exception -> 0x0902 }
            io.dcloud.common.DHInterface.AbsMgr r15 = r9.b     // Catch:{ Exception -> 0x0902 }
            r20 = r12
            io.dcloud.common.DHInterface.IMgr$MgrType r12 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr     // Catch:{ Exception -> 0x0902 }
            r22 = r7
            r7 = 9
            java.lang.Object r7 = r15.processEvent(r12, r7, r13)     // Catch:{ Exception -> 0x0902 }
            io.dcloud.common.DHInterface.IFrameView r7 = (io.dcloud.common.DHInterface.IFrameView) r7     // Catch:{ Exception -> 0x0902 }
            if (r7 == 0) goto L_0x0136
            if (r11 == r7) goto L_0x0136
            r11 = 1
            r10.h = r11     // Catch:{ Exception -> 0x009c }
            r9.b((java.lang.String) r13, (io.dcloud.feature.ui.a) r10, (io.dcloud.common.DHInterface.IFrameView) r7)     // Catch:{ Exception -> 0x009c }
        L_0x0136:
            r15 = r10
        L_0x0137:
            io.dcloud.feature.ui.e$e r7 = io.dcloud.feature.ui.e.C0044e.valueOf(r4)     // Catch:{ Exception -> 0x013c }
            goto L_0x013d
        L_0x013c:
            r7 = 0
        L_0x013d:
            java.lang.String r10 = "UI"
            boolean r10 = io.dcloud.common.util.PdrUtil.isEquals(r10, r3)     // Catch:{ Exception -> 0x0902 }
            r11 = 5
            r12 = 4
            if (r10 == 0) goto L_0x04be
            r15.c((io.dcloud.common.DHInterface.IWebview) r8)     // Catch:{ Exception -> 0x009c }
            int[] r1 = io.dcloud.feature.ui.e.d.a     // Catch:{ Exception -> 0x009c }
            int r3 = r7.ordinal()     // Catch:{ Exception -> 0x009c }
            r1 = r1[r3]     // Catch:{ Exception -> 0x009c }
            r3 = 1
            if (r1 == r3) goto L_0x04a0
            r4 = 2
            if (r1 == r4) goto L_0x047c
            switch(r1) {
                case 5: goto L_0x046f;
                case 6: goto L_0x0461;
                case 7: goto L_0x0456;
                case 8: goto L_0x044b;
                case 9: goto L_0x0417;
                case 10: goto L_0x03e8;
                case 11: goto L_0x03d1;
                case 12: goto L_0x0399;
                case 13: goto L_0x038d;
                case 14: goto L_0x01f5;
                case 15: goto L_0x01ef;
                case 16: goto L_0x015d;
                case 17: goto L_0x015d;
                default: goto L_0x015b;
            }     // Catch:{ Exception -> 0x009c }
        L_0x015b:
            goto L_0x04b1
        L_0x015d:
            r1 = 0
            java.lang.String r2 = r14.optString(r1)     // Catch:{ Exception -> 0x009c }
            java.lang.String r1 = r14.optString(r3)     // Catch:{ Exception -> 0x009c }
            boolean r3 = io.dcloud.common.util.PdrUtil.isEmpty(r1)     // Catch:{ Exception -> 0x009c }
            if (r3 == 0) goto L_0x0175
            io.dcloud.common.DHInterface.IFrameView r3 = r29.obtainFrameView()     // Catch:{ Exception -> 0x009c }
            java.lang.String r3 = io.dcloud.common.util.BaseInfo.getUniNViewId(r3)     // Catch:{ Exception -> 0x009c }
            goto L_0x0187
        L_0x0175:
            r3 = 0
            io.dcloud.feature.ui.c r4 = r15.a((java.lang.String) r3, (java.lang.String) r3, (java.lang.String) r1)     // Catch:{ Exception -> 0x009c }
            if (r4 == 0) goto L_0x0185
            io.dcloud.common.DHInterface.IFrameView r3 = r4.z     // Catch:{ Exception -> 0x009c }
            if (r3 == 0) goto L_0x0185
            java.lang.String r3 = io.dcloud.common.util.BaseInfo.getUniNViewId(r3)     // Catch:{ Exception -> 0x009c }
            goto L_0x0187
        L_0x0185:
            java.lang.String r3 = ""
        L_0x0187:
            io.dcloud.feature.ui.e$e r4 = io.dcloud.feature.ui.e.C0044e.__callNativeModuleSync     // Catch:{ Exception -> 0x009c }
            boolean r4 = r7.equals(r4)     // Catch:{ Exception -> 0x009c }
            java.lang.String r5 = "weex,io.dcloud.feature.weex.WeexFeature"
            r6 = 10
            if (r4 == 0) goto L_0x01c3
            io.dcloud.common.DHInterface.AbsMgr r4 = r9.b     // Catch:{ Exception -> 0x009c }
            io.dcloud.common.DHInterface.IMgr$MgrType r7 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr     // Catch:{ Exception -> 0x009c }
            java.lang.Object[] r10 = new java.lang.Object[r12]     // Catch:{ Exception -> 0x009c }
            io.dcloud.common.DHInterface.IApp r14 = r29.obtainApp()     // Catch:{ Exception -> 0x009c }
            r16 = 0
            r10[r16] = r14     // Catch:{ Exception -> 0x009c }
            r14 = 1
            r10[r14] = r5     // Catch:{ Exception -> 0x009c }
            java.lang.String r5 = "callNativeModuleSync"
            r17 = 2
            r10[r17] = r5     // Catch:{ Exception -> 0x009c }
            java.lang.Object[] r5 = new java.lang.Object[r11]     // Catch:{ Exception -> 0x009c }
            r11 = 0
            r5[r11] = r8     // Catch:{ Exception -> 0x009c }
            r5[r14] = r3     // Catch:{ Exception -> 0x009c }
            r5[r17] = r1     // Catch:{ Exception -> 0x009c }
            r1 = 3
            r5[r1] = r13     // Catch:{ Exception -> 0x009c }
            r5[r12] = r2     // Catch:{ Exception -> 0x009c }
            r10[r1] = r5     // Catch:{ Exception -> 0x009c }
            java.lang.Object r1 = r4.processEvent(r7, r6, r10)     // Catch:{ Exception -> 0x009c }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ Exception -> 0x009c }
            goto L_0x0475
        L_0x01c3:
            io.dcloud.common.DHInterface.AbsMgr r4 = r9.b     // Catch:{ Exception -> 0x009c }
            io.dcloud.common.DHInterface.IMgr$MgrType r7 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr     // Catch:{ Exception -> 0x009c }
            java.lang.Object[] r10 = new java.lang.Object[r12]     // Catch:{ Exception -> 0x009c }
            io.dcloud.common.DHInterface.IApp r14 = r29.obtainApp()     // Catch:{ Exception -> 0x009c }
            r16 = 0
            r10[r16] = r14     // Catch:{ Exception -> 0x009c }
            r14 = 1
            r10[r14] = r5     // Catch:{ Exception -> 0x009c }
            java.lang.String r5 = "postMessageToUniNView"
            r17 = 2
            r10[r17] = r5     // Catch:{ Exception -> 0x009c }
            java.lang.Object[] r5 = new java.lang.Object[r11]     // Catch:{ Exception -> 0x009c }
            r11 = 0
            r5[r11] = r8     // Catch:{ Exception -> 0x009c }
            r5[r14] = r3     // Catch:{ Exception -> 0x009c }
            r5[r17] = r1     // Catch:{ Exception -> 0x009c }
            r1 = 3
            r5[r1] = r13     // Catch:{ Exception -> 0x009c }
            r5[r12] = r2     // Catch:{ Exception -> 0x009c }
            r10[r1] = r5     // Catch:{ Exception -> 0x009c }
            r4.processEvent(r7, r6, r10)     // Catch:{ Exception -> 0x009c }
            goto L_0x046c
        L_0x01ef:
            java.lang.String r1 = r15.e()     // Catch:{ Exception -> 0x009c }
            goto L_0x0475
        L_0x01f5:
            r1 = 0
            org.json.JSONObject r2 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONArray) r14, (int) r1)     // Catch:{ Exception -> 0x0477 }
            r1 = 1
            org.json.JSONObject r3 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONArray) r14, (int) r1)     // Catch:{ Exception -> 0x0477 }
            r1 = 2
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r14, (int) r1)     // Catch:{ Exception -> 0x0477 }
            r4 = 3
            java.lang.String r10 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r14, (int) r4)     // Catch:{ Exception -> 0x0477 }
            android.view.ViewGroup r4 = r29.obtainWindowView()     // Catch:{ Exception -> 0x0477 }
            android.content.Context r4 = r4.getContext()     // Catch:{ Exception -> 0x0477 }
            android.content.res.Resources r4 = r4.getResources()     // Catch:{ Exception -> 0x0477 }
            android.util.DisplayMetrics r4 = r4.getDisplayMetrics()     // Catch:{ Exception -> 0x0477 }
            int r4 = r4.widthPixels     // Catch:{ Exception -> 0x0477 }
            boolean r5 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x0477 }
            if (r5 != 0) goto L_0x022d
            io.dcloud.feature.ui.c r1 = r15.a((java.lang.String) r1, (java.lang.String) r1, (java.lang.String) r1)     // Catch:{ Exception -> 0x009c }
            if (r1 == 0) goto L_0x022d
            io.dcloud.common.DHInterface.IWebview r1 = r1.r()     // Catch:{ Exception -> 0x009c }
            r11 = r1
            goto L_0x022e
        L_0x022d:
            r11 = 0
        L_0x022e:
            java.lang.String r1 = "action"
            java.lang.String r5 = "view"
            r6 = 2147483647(0x7fffffff, float:NaN)
            if (r2 == 0) goto L_0x02bf
            java.lang.String r7 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r2, (java.lang.String) r5)     // Catch:{ Exception -> 0x009c }
            java.lang.String r8 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r2, (java.lang.String) r1)     // Catch:{ Exception -> 0x009c }
            java.lang.String r12 = "styles"
            org.json.JSONObject r2 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r2, (java.lang.String) r12)     // Catch:{ Exception -> 0x009c }
            java.lang.String r12 = "fromLeft"
            java.lang.String r12 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r2, (java.lang.String) r12)     // Catch:{ Exception -> 0x009c }
            java.lang.String r13 = "toLeft"
            java.lang.String r2 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r2, (java.lang.String) r13)     // Catch:{ Exception -> 0x009c }
            int r12 = io.dcloud.common.util.PdrUtil.parseInt(r12, r4, r6)     // Catch:{ Exception -> 0x009c }
            int r2 = io.dcloud.common.util.PdrUtil.parseInt(r2, r4, r6)     // Catch:{ Exception -> 0x009c }
            r13 = 0
            io.dcloud.feature.ui.c r7 = r15.a((java.lang.String) r13, (java.lang.String) r7, (java.lang.String) r7)     // Catch:{ Exception -> 0x009c }
            if (r6 == r2) goto L_0x02b9
            if (r7 == 0) goto L_0x02b9
            io.dcloud.common.DHInterface.IFrameView r13 = r7.z     // Catch:{ Exception -> 0x009c }
            android.view.View r13 = r13.obtainMainView()     // Catch:{ Exception -> 0x009c }
            int r14 = r13.getVisibility()     // Catch:{ Exception -> 0x009c }
            if (r14 != 0) goto L_0x02b0
            if (r6 == r12) goto L_0x0272
            goto L_0x028a
        L_0x0272:
            android.view.ViewGroup$LayoutParams r12 = r13.getLayoutParams()     // Catch:{ Exception -> 0x009c }
            boolean r14 = r12 instanceof android.widget.AbsoluteLayout.LayoutParams     // Catch:{ Exception -> 0x009c }
            if (r14 == 0) goto L_0x027f
            android.widget.AbsoluteLayout$LayoutParams r12 = (android.widget.AbsoluteLayout.LayoutParams) r12     // Catch:{ Exception -> 0x009c }
            int r12 = r12.x     // Catch:{ Exception -> 0x009c }
            goto L_0x028a
        L_0x027f:
            boolean r12 = r12 instanceof android.widget.FrameLayout.LayoutParams     // Catch:{ Exception -> 0x009c }
            if (r12 == 0) goto L_0x0289
            float r12 = io.dcloud.nineoldandroids.view.ViewHelper.getX(r13)     // Catch:{ Exception -> 0x009c }
            int r12 = (int) r12     // Catch:{ Exception -> 0x009c }
            goto L_0x028a
        L_0x0289:
            r12 = 0
        L_0x028a:
            io.dcloud.common.DHInterface.IFrameView r14 = r7.z     // Catch:{ Exception -> 0x009c }
            r14.pushToViewStack()     // Catch:{ Exception -> 0x009c }
            io.dcloud.common.adapter.ui.AdaFrameItem r14 = r7.d()     // Catch:{ Exception -> 0x009c }
            io.dcloud.common.adapter.util.ViewOptions r14 = r14.obtainFrameOptions()     // Catch:{ Exception -> 0x009c }
            r14.left = r2     // Catch:{ Exception -> 0x009c }
            io.dcloud.common.adapter.ui.AdaFrameItem r14 = r7.d()     // Catch:{ Exception -> 0x009c }
            io.dcloud.common.adapter.util.ViewOptions r19 = r14.obtainFrameOptions()     // Catch:{ Exception -> 0x009c }
            java.lang.String r20 = "left"
            r23 = 1
            r24 = 1
            r21 = r2
            r22 = r4
            r19.checkValueIsPercentage(r20, r21, r22, r23, r24)     // Catch:{ Exception -> 0x009c }
            r14 = 1
            goto L_0x02b2
        L_0x02b0:
            r12 = 0
            r14 = 0
        L_0x02b2:
            r27 = r7
            r7 = r2
            r2 = r13
            r13 = r27
            goto L_0x02c5
        L_0x02b9:
            r13 = r7
            r12 = 0
            r14 = 0
            r7 = r2
            r2 = 0
            goto L_0x02c5
        L_0x02bf:
            r2 = 0
            r7 = 0
            r8 = 0
            r12 = 0
            r13 = 0
            r14 = 0
        L_0x02c5:
            if (r3 == 0) goto L_0x0354
            java.lang.String r5 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r3, (java.lang.String) r5)     // Catch:{ Exception -> 0x0477 }
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r3, (java.lang.String) r1)     // Catch:{ Exception -> 0x0477 }
            java.lang.String r6 = "styles"
            org.json.JSONObject r3 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r3, (java.lang.String) r6)     // Catch:{ Exception -> 0x0477 }
            java.lang.String r6 = "fromLeft"
            java.lang.String r6 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r3, (java.lang.String) r6)     // Catch:{ Exception -> 0x0477 }
            r17 = r1
            java.lang.String r1 = "toLeft"
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r3, (java.lang.String) r1)     // Catch:{ Exception -> 0x0477 }
            r3 = 2147483647(0x7fffffff, float:NaN)
            int r6 = io.dcloud.common.util.PdrUtil.parseInt(r6, r4, r3)     // Catch:{ Exception -> 0x0477 }
            int r1 = io.dcloud.common.util.PdrUtil.parseInt(r1, r4, r3)     // Catch:{ Exception -> 0x0477 }
            r3 = 0
            io.dcloud.feature.ui.c r5 = r15.a((java.lang.String) r3, (java.lang.String) r5, (java.lang.String) r5)     // Catch:{ Exception -> 0x0477 }
            r3 = 2147483647(0x7fffffff, float:NaN)
            if (r3 == r1) goto L_0x0352
            if (r5 == 0) goto L_0x0352
            io.dcloud.common.DHInterface.IFrameView r3 = r5.z     // Catch:{ Exception -> 0x0477 }
            android.view.View r3 = r3.obtainMainView()     // Catch:{ Exception -> 0x0477 }
            int r19 = r3.getVisibility()     // Catch:{ Exception -> 0x0477 }
            if (r19 != 0) goto L_0x0359
            r9 = 2147483647(0x7fffffff, float:NaN)
            if (r9 == r6) goto L_0x030c
            goto L_0x0324
        L_0x030c:
            android.view.ViewGroup$LayoutParams r6 = r3.getLayoutParams()     // Catch:{ Exception -> 0x0477 }
            boolean r9 = r6 instanceof android.widget.AbsoluteLayout.LayoutParams     // Catch:{ Exception -> 0x0477 }
            if (r9 == 0) goto L_0x0319
            android.widget.AbsoluteLayout$LayoutParams r6 = (android.widget.AbsoluteLayout.LayoutParams) r6     // Catch:{ Exception -> 0x0477 }
            int r6 = r6.x     // Catch:{ Exception -> 0x0477 }
            goto L_0x0324
        L_0x0319:
            boolean r6 = r6 instanceof android.widget.FrameLayout.LayoutParams     // Catch:{ Exception -> 0x0477 }
            if (r6 == 0) goto L_0x0323
            float r6 = io.dcloud.nineoldandroids.view.ViewHelper.getX(r3)     // Catch:{ Exception -> 0x0477 }
            int r6 = (int) r6     // Catch:{ Exception -> 0x0477 }
            goto L_0x0324
        L_0x0323:
            r6 = 0
        L_0x0324:
            io.dcloud.common.DHInterface.IFrameView r9 = r5.z     // Catch:{ Exception -> 0x0477 }
            r9.pushToViewStack()     // Catch:{ Exception -> 0x0477 }
            io.dcloud.common.adapter.ui.AdaFrameItem r9 = r5.d()     // Catch:{ Exception -> 0x0477 }
            io.dcloud.common.adapter.util.ViewOptions r9 = r9.obtainFrameOptions()     // Catch:{ Exception -> 0x0477 }
            r9.left = r1     // Catch:{ Exception -> 0x0477 }
            io.dcloud.common.adapter.ui.AdaFrameItem r9 = r5.d()     // Catch:{ Exception -> 0x0477 }
            io.dcloud.common.adapter.util.ViewOptions r19 = r9.obtainFrameOptions()     // Catch:{ Exception -> 0x0477 }
            java.lang.String r20 = "left"
            r23 = 1
            r24 = 1
            r21 = r1
            r22 = r4
            r19.checkValueIsPercentage(r20, r21, r22, r23, r24)     // Catch:{ Exception -> 0x0477 }
            r9 = r3
            r19 = r5
            r16 = r6
            r18 = r17
            r20 = 1
            goto L_0x0362
        L_0x0352:
            r3 = 0
            goto L_0x0359
        L_0x0354:
            r1 = 0
            r3 = 0
            r5 = 0
            r17 = 0
        L_0x0359:
            r9 = r3
            r19 = r5
            r18 = r17
            r16 = 0
            r20 = 0
        L_0x0362:
            r17 = r1
            if (r14 == 0) goto L_0x0375
            r1 = r28
            r3 = r12
            r4 = r7
            r5 = r8
            r6 = r11
            r7 = r10
            r8 = r13
            io.dcloud.nineoldandroids.animation.ValueAnimator r1 = r1.a(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ Exception -> 0x0477 }
            r1.start()     // Catch:{ Exception -> 0x0477 }
        L_0x0375:
            if (r20 == 0) goto L_0x046c
            r1 = r28
            r2 = r9
            r3 = r16
            r4 = r17
            r5 = r18
            r6 = r11
            r7 = r10
            r8 = r19
            io.dcloud.nineoldandroids.animation.ValueAnimator r1 = r1.a(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ Exception -> 0x0477 }
            r1.start()     // Catch:{ Exception -> 0x0477 }
            goto L_0x046c
        L_0x038d:
            java.lang.String r1 = android.os.Build.BRAND     // Catch:{ Exception -> 0x0477 }
            boolean r1 = io.dcloud.common.adapter.util.MobilePhoneModel.checkPhoneBanAcceleration(r1)     // Catch:{ Exception -> 0x0477 }
            java.lang.String r1 = io.dcloud.common.util.JSUtil.wrapJsVar((boolean) r1)     // Catch:{ Exception -> 0x0477 }
            goto L_0x0473
        L_0x0399:
            r1 = 0
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r5, (int) r1)     // Catch:{ Exception -> 0x0477 }
            r2 = 1
            org.json.JSONObject r2 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONArray) r5, (int) r2)     // Catch:{ Exception -> 0x0477 }
            java.lang.String r3 = "0"
            java.lang.String r2 = r2.optString(r3)     // Catch:{ Exception -> 0x03ca }
            java.lang.String r3 = "save"
            boolean r3 = r3.equals(r2)     // Catch:{ Exception -> 0x03ca }
            if (r3 == 0) goto L_0x03b9
            r2 = r8
            io.dcloud.common.adapter.ui.AdaWebview r2 = (io.dcloud.common.adapter.ui.AdaWebview) r2     // Catch:{ Exception -> 0x03ca }
            r2.saveWebViewData(r1)     // Catch:{ Exception -> 0x03ca }
            goto L_0x046c
        L_0x03b9:
            java.lang.String r3 = "update"
            boolean r2 = r3.equals(r2)     // Catch:{ Exception -> 0x03ca }
            if (r2 == 0) goto L_0x046c
            r2 = r8
            io.dcloud.common.adapter.ui.AdaWebview r2 = (io.dcloud.common.adapter.ui.AdaWebview) r2     // Catch:{ Exception -> 0x03ca }
            java.lang.String r1 = r2.syncUpdateWebViewData(r1)     // Catch:{ Exception -> 0x03ca }
            goto L_0x0473
        L_0x03ca:
            r0 = move-exception
            r1 = r0
            r1.printStackTrace()     // Catch:{ Exception -> 0x0477 }
            goto L_0x046c
        L_0x03d1:
            r1 = 0
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r14, (int) r1)     // Catch:{ Exception -> 0x0477 }
            r2 = 0
            io.dcloud.feature.ui.c r3 = r15.a((java.lang.String) r6, (java.lang.String) r6, (java.lang.String) r2)     // Catch:{ Exception -> 0x0477 }
            if (r3 == 0) goto L_0x046c
            java.util.HashMap<java.lang.String, java.lang.String> r2 = r3.i     // Catch:{ Exception -> 0x0477 }
            java.lang.String r3 = r29.getWebviewANID()     // Catch:{ Exception -> 0x0477 }
            r2.put(r3, r1)     // Catch:{ Exception -> 0x0477 }
            goto L_0x046c
        L_0x03e8:
            r1 = 0
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r14, (int) r1)     // Catch:{ Exception -> 0x0477 }
            r2 = 1
            java.lang.String r5 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r14, (int) r2)     // Catch:{ Exception -> 0x0477 }
            io.dcloud.feature.ui.b r1 = a((java.lang.String) r1)     // Catch:{ Exception -> 0x0477 }
            r7 = 2
            org.json.JSONObject r6 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONArray) r14, (int) r7)     // Catch:{ Exception -> 0x0477 }
            r2 = 3
            java.lang.String r2 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r14, (int) r2)     // Catch:{ Exception -> 0x0477 }
            java.util.HashMap<java.lang.String, java.lang.String> r3 = r1.i     // Catch:{ Exception -> 0x0477 }
            java.lang.String r4 = r29.getWebviewANID()     // Catch:{ Exception -> 0x0477 }
            r3.put(r4, r2)     // Catch:{ Exception -> 0x0477 }
            r15.a((java.lang.String) r5, (io.dcloud.feature.ui.b) r1)     // Catch:{ Exception -> 0x0477 }
            android.app.Activity r2 = r29.getActivity()     // Catch:{ Exception -> 0x0477 }
            r3 = r15
            r4 = r29
            r1.a(r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x0477 }
            goto L_0x046c
        L_0x0417:
            io.dcloud.common.DHInterface.IFrameView r1 = r29.obtainFrameView()     // Catch:{ Exception -> 0x0477 }
            io.dcloud.feature.ui.c r3 = r15.a((io.dcloud.common.DHInterface.IFrameView) r1)     // Catch:{ Exception -> 0x0477 }
            if (r3 == 0) goto L_0x0426
            java.lang.String r1 = r3.h()     // Catch:{ Exception -> 0x0477 }
            goto L_0x0473
        L_0x0426:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0477 }
            r3.<init>()     // Catch:{ Exception -> 0x0477 }
            java.lang.String r4 = "ui.execute "
            r3.append(r4)     // Catch:{ Exception -> 0x0477 }
            r3.append(r13)     // Catch:{ Exception -> 0x0477 }
            java.lang.String r4 = " not found NWindow uuid="
            r3.append(r4)     // Catch:{ Exception -> 0x0477 }
            r3.append(r6)     // Catch:{ Exception -> 0x0477 }
            java.lang.String r4 = ";frameView="
            r3.append(r4)     // Catch:{ Exception -> 0x0477 }
            r3.append(r1)     // Catch:{ Exception -> 0x0477 }
            java.lang.String r1 = r3.toString()     // Catch:{ Exception -> 0x0477 }
            io.dcloud.common.adapter.util.Logger.e(r2, r1)     // Catch:{ Exception -> 0x0477 }
            goto L_0x046c
        L_0x044b:
            io.dcloud.feature.ui.c r1 = r15.a((int) r12)     // Catch:{ Exception -> 0x0477 }
            if (r1 == 0) goto L_0x046c
            java.lang.String r1 = r1.h()     // Catch:{ Exception -> 0x0477 }
            goto L_0x0473
        L_0x0456:
            io.dcloud.feature.ui.c r1 = r15.c()     // Catch:{ Exception -> 0x0477 }
            if (r1 == 0) goto L_0x046c
            java.lang.String r1 = r1.h()     // Catch:{ Exception -> 0x0477 }
            goto L_0x0473
        L_0x0461:
            io.dcloud.feature.ui.c r1 = r15.d()     // Catch:{ Exception -> 0x0477 }
            if (r1 == 0) goto L_0x046c
            java.lang.String r1 = r1.h()     // Catch:{ Exception -> 0x0477 }
            goto L_0x0473
        L_0x046c:
            r9 = r28
            goto L_0x04b1
        L_0x046f:
            java.lang.String r1 = r15.b()     // Catch:{ Exception -> 0x0477 }
        L_0x0473:
            r9 = r28
        L_0x0475:
            r13 = r1
            goto L_0x04b2
        L_0x0477:
            r0 = move-exception
            r9 = r28
            goto L_0x009d
        L_0x047c:
            io.dcloud.common.DHInterface.AbsMgr r1 = r9.b     // Catch:{ Exception -> 0x009c }
            io.dcloud.common.DHInterface.IMgr$MgrType r2 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr     // Catch:{ Exception -> 0x009c }
            r3 = 44
            io.dcloud.common.DHInterface.IApp r4 = r29.obtainApp()     // Catch:{ Exception -> 0x009c }
            java.lang.Object r1 = r1.processEvent(r2, r3, r4)     // Catch:{ Exception -> 0x009c }
            io.dcloud.common.DHInterface.IFrameView r1 = (io.dcloud.common.DHInterface.IFrameView) r1     // Catch:{ Exception -> 0x009c }
            int r1 = r1.hashCode()     // Catch:{ Exception -> 0x009c }
            java.lang.String r1 = java.lang.String.valueOf(r1)     // Catch:{ Exception -> 0x009c }
            r2 = 0
            io.dcloud.feature.ui.c r1 = r15.a((java.lang.String) r1, (java.lang.String) r1, (java.lang.String) r2)     // Catch:{ Exception -> 0x009c }
            if (r1 == 0) goto L_0x04b1
            java.lang.String r1 = r1.h()     // Catch:{ Exception -> 0x009c }
            goto L_0x0475
        L_0x04a0:
            r1 = 0
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r14, (int) r1)     // Catch:{ Exception -> 0x009c }
            r2 = 0
            io.dcloud.feature.ui.c r1 = r15.a((java.lang.String) r2, (java.lang.String) r2, (java.lang.String) r1)     // Catch:{ Exception -> 0x009c }
            if (r1 == 0) goto L_0x04b1
            java.lang.String r1 = r1.h()     // Catch:{ Exception -> 0x009c }
            goto L_0x0475
        L_0x04b1:
            r13 = 0
        L_0x04b2:
            r15.g()     // Catch:{ Exception -> 0x04b7 }
            goto L_0x0935
        L_0x04b7:
            r0 = move-exception
            r1 = r30
            r12 = r31
            goto L_0x0913
        L_0x04be:
            r7 = 2
            boolean r5 = io.dcloud.common.util.PdrUtil.isEquals(r1, r3)     // Catch:{ Exception -> 0x0902 }
            if (r5 == 0) goto L_0x0518
            r15.c((io.dcloud.common.DHInterface.IWebview) r8)     // Catch:{ Exception -> 0x009c }
            boolean r1 = r1.equals(r4)     // Catch:{ Exception -> 0x009c }
            if (r1 == 0) goto L_0x04db
            r7 = 0
            r1 = r28
            r2 = r15
            r3 = r29
            r4 = r14
            r5 = r22
            r1.a(r2, r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x009c }
            goto L_0x0512
        L_0x04db:
            r1 = 0
            io.dcloud.feature.ui.c r3 = r15.a((java.lang.String) r6, (java.lang.String) r6, (java.lang.String) r1)     // Catch:{ Exception -> 0x009c }
            if (r3 == 0) goto L_0x04e7
            java.lang.String r13 = r3.a((io.dcloud.common.DHInterface.IWebview) r8, (java.lang.String) r4, (org.json.JSONArray) r14)     // Catch:{ Exception -> 0x009c }
            goto L_0x0513
        L_0x04e7:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x009c }
            r1.<init>()     // Catch:{ Exception -> 0x009c }
            r1.append(r13)     // Catch:{ Exception -> 0x009c }
            java.lang.String r3 = " App not found NWindow ;uuid="
            r1.append(r3)     // Catch:{ Exception -> 0x009c }
            r1.append(r6)     // Catch:{ Exception -> 0x009c }
            java.lang.String r3 = ";_action="
            r1.append(r3)     // Catch:{ Exception -> 0x009c }
            r1.append(r4)     // Catch:{ Exception -> 0x009c }
            java.lang.String r3 = ";at "
            r1.append(r3)     // Catch:{ Exception -> 0x009c }
            java.lang.String r3 = r29.obtainFullUrl()     // Catch:{ Exception -> 0x009c }
            r1.append(r3)     // Catch:{ Exception -> 0x009c }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x009c }
            io.dcloud.common.adapter.util.Logger.e(r2, r1)     // Catch:{ Exception -> 0x009c }
        L_0x0512:
            r13 = 0
        L_0x0513:
            r15.g()     // Catch:{ Exception -> 0x04b7 }
            goto L_0x0935
        L_0x0518:
            r10 = r20
            boolean r1 = io.dcloud.common.util.PdrUtil.isEquals(r10, r3)     // Catch:{ Exception -> 0x0902 }
            if (r1 == 0) goto L_0x05f7
            java.lang.String r1 = "createGroup"
            boolean r1 = r1.equals(r4)     // Catch:{ Exception -> 0x009c }
            if (r1 == 0) goto L_0x05eb
            java.util.ArrayList r13 = new java.util.ArrayList     // Catch:{ Exception -> 0x009c }
            r13.<init>()     // Catch:{ Exception -> 0x009c }
            r1 = 0
            org.json.JSONArray r4 = io.dcloud.common.util.JSONUtil.getJSONArray((org.json.JSONArray) r14, (int) r1)     // Catch:{ Exception -> 0x009c }
            r1 = 1
            org.json.JSONObject r2 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONArray) r14, (int) r1)     // Catch:{ Exception -> 0x009c }
            if (r2 != 0) goto L_0x0543
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Exception -> 0x009c }
            java.lang.String r2 = "{}"
            r1.<init>(r2)     // Catch:{ Exception -> 0x009c }
            r3 = r1
            goto L_0x0544
        L_0x0543:
            r3 = r2
        L_0x0544:
            r2 = 0
        L_0x0545:
            int r1 = r4.length()     // Catch:{ Exception -> 0x009c }
            if (r2 >= r1) goto L_0x0586
            org.json.JSONArray r1 = io.dcloud.common.util.JSONUtil.getJSONArray((org.json.JSONArray) r4, (int) r2)     // Catch:{ Exception -> 0x009c }
            r5 = 1
            org.json.JSONArray r19 = io.dcloud.common.util.JSONUtil.getJSONArray((org.json.JSONArray) r1, (int) r5)     // Catch:{ Exception -> 0x009c }
            r20 = 1
            r1 = r28
            r23 = r2
            r2 = r15
            r5 = r3
            r3 = r29
            r24 = r4
            r4 = r19
            r11 = r5
            r12 = 6
            r5 = r22
            r25 = r6
            r26 = r22
            r7 = r20
            io.dcloud.feature.ui.c r1 = r1.a(r2, r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x009c }
            r2 = 1
            r1.b((boolean) r2)     // Catch:{ Exception -> 0x009c }
            io.dcloud.common.DHInterface.IFrameView r1 = r1.z     // Catch:{ Exception -> 0x009c }
            r13.add(r1)     // Catch:{ Exception -> 0x009c }
            int r2 = r23 + 1
            r3 = r11
            r4 = r24
            r6 = r25
            r22 = r26
            r7 = 2
            r11 = 5
            r12 = 4
            goto L_0x0545
        L_0x0586:
            r11 = r3
            r25 = r6
            r26 = r22
            r12 = 6
            io.dcloud.feature.ui.d r7 = new io.dcloud.feature.ui.d     // Catch:{ Exception -> 0x009c }
            r7.<init>(r10, r13, r11)     // Catch:{ Exception -> 0x009c }
            io.dcloud.common.DHInterface.AbsMgr r1 = r9.b     // Catch:{ Exception -> 0x009c }
            io.dcloud.common.DHInterface.IMgr$MgrType r2 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr     // Catch:{ Exception -> 0x009c }
            java.lang.Object[] r3 = new java.lang.Object[r12]     // Catch:{ Exception -> 0x009c }
            r4 = 1
            java.lang.Integer r5 = java.lang.Integer.valueOf(r4)     // Catch:{ Exception -> 0x009c }
            r6 = 0
            r3[r6] = r5     // Catch:{ Exception -> 0x009c }
            r5 = r26
            r3[r4] = r5     // Catch:{ Exception -> 0x009c }
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Exception -> 0x009c }
            r4[r6] = r11     // Catch:{ Exception -> 0x009c }
            r10 = 2
            r3[r10] = r4     // Catch:{ Exception -> 0x009c }
            io.dcloud.common.DHInterface.IFrameView r4 = r29.obtainFrameView()     // Catch:{ Exception -> 0x009c }
            r6 = 3
            r3[r6] = r4     // Catch:{ Exception -> 0x009c }
            r4 = 4
            r3[r4] = r7     // Catch:{ Exception -> 0x009c }
            r4 = 5
            r3[r4] = r13     // Catch:{ Exception -> 0x009c }
            java.lang.Object r1 = r1.processEvent(r2, r6, r3)     // Catch:{ Exception -> 0x009c }
            r12 = r1
            io.dcloud.common.DHInterface.IFrameView r12 = (io.dcloud.common.DHInterface.IFrameView) r12     // Catch:{ Exception -> 0x009c }
            r7.a((io.dcloud.common.DHInterface.IFrameView) r12)     // Catch:{ Exception -> 0x009c }
            android.app.Activity r2 = r29.getActivity()     // Catch:{ Exception -> 0x009c }
            r1 = r7
            r3 = r15
            r4 = r29
            r5 = r25
            r6 = r11
            r1.a(r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x009c }
            java.util.HashMap<java.lang.String, java.lang.String> r1 = r7.i     // Catch:{ Exception -> 0x009c }
            java.lang.String r2 = r29.getWebviewANID()     // Catch:{ Exception -> 0x009c }
            java.lang.String r3 = r14.getString(r10)     // Catch:{ Exception -> 0x009c }
            r1.put(r2, r3)     // Catch:{ Exception -> 0x009c }
            r11 = r25
            r15.a((java.lang.String) r11, (io.dcloud.feature.ui.b) r7)     // Catch:{ Exception -> 0x009c }
            int r1 = io.dcloud.common.adapter.util.DeviceInfo.sDeviceSdkVer     // Catch:{ Exception -> 0x009c }
            r2 = 11
            if (r1 < r2) goto L_0x060a
            r15.b((io.dcloud.common.DHInterface.IFrameView) r12)     // Catch:{ Exception -> 0x009c }
            goto L_0x060a
        L_0x05eb:
            r11 = r6
            io.dcloud.feature.ui.b r1 = r15.a((java.lang.String) r11)     // Catch:{ Exception -> 0x009c }
            java.lang.String r1 = r1.a((io.dcloud.common.DHInterface.IWebview) r8, (java.lang.String) r4, (org.json.JSONArray) r14)     // Catch:{ Exception -> 0x009c }
        L_0x05f4:
            r13 = r1
            goto L_0x0935
        L_0x05f7:
            r11 = r6
            r5 = r22
            r6 = 3
            r10 = 2
            r12 = 6
            java.lang.String r1 = "updateAppFrameViews"
            r7 = r30
            boolean r1 = android.text.TextUtils.equals(r7, r1)     // Catch:{ Exception -> 0x08fd }
            if (r1 == 0) goto L_0x0614
            r15.i()     // Catch:{ Exception -> 0x060d }
        L_0x060a:
            r3 = 0
            goto L_0x08f7
        L_0x060d:
            r0 = move-exception
            r12 = r31
        L_0x0610:
            r2 = r0
            r1 = r7
            goto L_0x00a2
        L_0x0614:
            java.lang.String r1 = "n_createDirectWebview"
            boolean r1 = android.text.TextUtils.equals(r7, r1)     // Catch:{ Exception -> 0x08fd }
            java.lang.String r2 = "winType"
            if (r1 == 0) goto L_0x06d8
            org.json.JSONArray r4 = new org.json.JSONArray     // Catch:{ Exception -> 0x060d }
            r4.<init>()     // Catch:{ Exception -> 0x060d }
            io.dcloud.common.DHInterface.IApp r1 = r29.obtainApp()     // Catch:{ Exception -> 0x060d }
            android.content.Intent r1 = r1.obtainWebAppIntent()     // Catch:{ Exception -> 0x060d }
            java.lang.String r3 = "direct_page"
            java.lang.String r1 = r1.getStringExtra(r3)     // Catch:{ Exception -> 0x060d }
            r4.put(r1)     // Catch:{ Exception -> 0x060d }
            io.dcloud.common.DHInterface.IApp r1 = r29.obtainApp()     // Catch:{ Exception -> 0x060d }
            io.dcloud.common.DHInterface.IApp$ConfigProperty$ThridInfo r3 = io.dcloud.common.DHInterface.IApp.ConfigProperty.ThridInfo.DirectPageJsonData     // Catch:{ Exception -> 0x060d }
            org.json.JSONObject r1 = r1.obtainThridInfo(r3)     // Catch:{ Exception -> 0x060d }
            if (r1 != 0) goto L_0x0646
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Exception -> 0x060d }
            r1.<init>()     // Catch:{ Exception -> 0x060d }
        L_0x0646:
            r3 = 5
            r1.put(r2, r3)     // Catch:{ Exception -> 0x060d }
            r4.put(r1)     // Catch:{ Exception -> 0x060d }
            java.lang.String r2 = "backButtonAutoControl"
            java.lang.String r3 = "quit"
            r1.put(r2, r3)     // Catch:{ Exception -> 0x060d }
            java.lang.String r2 = "extras"
            org.json.JSONObject r1 = r1.optJSONObject(r2)     // Catch:{ Exception -> 0x060d }
            java.lang.String r2 = "__wap2app_type"
            if (r1 == 0) goto L_0x066b
            boolean r3 = r1.has(r2)     // Catch:{ Exception -> 0x060d }
            if (r3 != 0) goto L_0x0669
            java.lang.String r3 = "direct"
            r1.put(r2, r3)     // Catch:{ Exception -> 0x060d }
        L_0x0669:
            r2 = 4
            goto L_0x0676
        L_0x066b:
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Exception -> 0x060d }
            r1.<init>()     // Catch:{ Exception -> 0x060d }
            java.lang.String r3 = "direct"
            r1.put(r2, r3)     // Catch:{ Exception -> 0x060d }
            goto L_0x0669
        L_0x0676:
            r4.put(r2, r1)     // Catch:{ Exception -> 0x060d }
            r12 = 0
            r1 = r28
            r2 = r15
            r3 = r29
            r6 = r11
            r8 = r7
            r7 = r12
            io.dcloud.feature.ui.c r1 = r1.a(r2, r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x06d1 }
            if (r11 != 0) goto L_0x0694
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x06d1 }
            int r2 = r2.hashCode()     // Catch:{ Exception -> 0x06d1 }
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ Exception -> 0x06d1 }
            r1.l = r2     // Catch:{ Exception -> 0x06d1 }
        L_0x0694:
            r2 = 1
            r1.G = r2     // Catch:{ Exception -> 0x06d1 }
            r1.J = r2     // Catch:{ Exception -> 0x06d1 }
            r2 = 4
            io.dcloud.feature.ui.c r2 = r15.a((int) r2)     // Catch:{ Exception -> 0x06d1 }
            if (r2 == 0) goto L_0x06a8
            long r2 = r2.v     // Catch:{ Exception -> 0x06d1 }
            r4 = 2
            long r2 = r2 + r4
            r1.v = r2     // Catch:{ Exception -> 0x06d1 }
            goto L_0x06b5
        L_0x06a8:
            io.dcloud.feature.ui.c r2 = r15.a((int) r10)     // Catch:{ Exception -> 0x06d1 }
            if (r2 == 0) goto L_0x06b5
            long r2 = r2.v     // Catch:{ Exception -> 0x06d1 }
            r4 = 2
            long r2 = r2 + r4
            r1.v = r2     // Catch:{ Exception -> 0x06d1 }
        L_0x06b5:
            int r2 = r15.c((io.dcloud.feature.ui.c) r1)     // Catch:{ Exception -> 0x06d1 }
            r15.a((java.lang.String) r13, (io.dcloud.feature.ui.c) r1, (int) r2)     // Catch:{ Exception -> 0x06d1 }
            io.dcloud.common.DHInterface.AbsMgr r2 = r9.b     // Catch:{ Exception -> 0x06d1 }
            io.dcloud.common.DHInterface.IMgr$MgrType r3 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr     // Catch:{ Exception -> 0x06d1 }
            java.lang.Object[] r4 = new java.lang.Object[r10]     // Catch:{ Exception -> 0x06d1 }
            io.dcloud.common.DHInterface.IFrameView r1 = r1.z     // Catch:{ Exception -> 0x06d1 }
            r5 = 0
            r4[r5] = r1     // Catch:{ Exception -> 0x06d1 }
            java.lang.Boolean r1 = java.lang.Boolean.FALSE     // Catch:{ Exception -> 0x06d1 }
            r5 = 1
            r4[r5] = r1     // Catch:{ Exception -> 0x06d1 }
            r2.processEvent(r3, r5, r4)     // Catch:{ Exception -> 0x06d1 }
            goto L_0x060a
        L_0x06d1:
            r0 = move-exception
            r12 = r31
        L_0x06d4:
            r2 = r0
            r1 = r8
            goto L_0x00a2
        L_0x06d8:
            java.lang.String r1 = "n_createHDWebview"
            boolean r1 = android.text.TextUtils.equals(r7, r1)     // Catch:{ Exception -> 0x08fd }
            java.lang.String r3 = "id"
            java.lang.String r6 = "name"
            if (r1 == 0) goto L_0x0780
            r1 = r31
            r10 = 3
            org.json.JSONObject r4 = r1.optJSONObject(r10)     // Catch:{ Exception -> 0x077c }
            if (r4 == 0) goto L_0x060a
            java.lang.String r10 = "url"
            r13 = 0
            java.lang.String r10 = r4.optString(r10, r13)     // Catch:{ Exception -> 0x077c }
            org.json.JSONArray r13 = new org.json.JSONArray     // Catch:{ Exception -> 0x077c }
            r13.<init>()     // Catch:{ Exception -> 0x077c }
            r14 = r19
            boolean r17 = r4.isNull(r14)     // Catch:{ Exception -> 0x077c }
            if (r17 != 0) goto L_0x072c
            java.lang.String r17 = r4.optString(r14)     // Catch:{ Exception -> 0x060d }
            r4.remove(r14)     // Catch:{ Exception -> 0x060d }
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x060d }
            r14.<init>()     // Catch:{ Exception -> 0x060d }
            java.lang.String r12 = r5.obtainAppTempPath()     // Catch:{ Exception -> 0x060d }
            r14.append(r12)     // Catch:{ Exception -> 0x060d }
            int r12 = r17.hashCode()     // Catch:{ Exception -> 0x060d }
            r14.append(r12)     // Catch:{ Exception -> 0x060d }
            java.lang.String r12 = r14.toString()     // Catch:{ Exception -> 0x060d }
            byte[] r14 = r17.getBytes()     // Catch:{ Exception -> 0x060d }
            r1 = 0
            io.dcloud.common.adapter.io.DHFile.writeFile((byte[]) r14, (int) r1, (java.lang.String) r12)     // Catch:{ Exception -> 0x060d }
            java.lang.String r1 = "preloadjs"
            r4.put(r1, r12)     // Catch:{ Exception -> 0x060d }
        L_0x072c:
            r13.put(r10)     // Catch:{ Exception -> 0x060d }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x060d }
            r1.<init>()     // Catch:{ Exception -> 0x060d }
            java.lang.String r12 = r5.obtainAppId()     // Catch:{ Exception -> 0x060d }
            r1.append(r12)     // Catch:{ Exception -> 0x060d }
            java.lang.String r12 = "__hd"
            r1.append(r12)     // Catch:{ Exception -> 0x060d }
            int r10 = r10.hashCode()     // Catch:{ Exception -> 0x060d }
            r1.append(r10)     // Catch:{ Exception -> 0x060d }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x060d }
            java.lang.String r1 = r4.optString(r3, r1)     // Catch:{ Exception -> 0x060d }
            r4.put(r6, r1)     // Catch:{ Exception -> 0x060d }
            r1 = 6
            r4.put(r2, r1)     // Catch:{ Exception -> 0x060d }
            r13.put(r4)     // Catch:{ Exception -> 0x060d }
            r10 = 0
            r12 = r31
            r1 = r28
            r2 = r15
            r3 = r29
            r4 = r13
            r6 = r11
            r8 = r7
            r7 = r10
            io.dcloud.feature.ui.c r1 = r1.a(r2, r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x0779 }
            if (r11 != 0) goto L_0x060a
            io.dcloud.common.DHInterface.IFrameView r2 = r1.z     // Catch:{ Exception -> 0x0779 }
            int r2 = r2.hashCode()     // Catch:{ Exception -> 0x0779 }
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ Exception -> 0x0779 }
            r1.l = r2     // Catch:{ Exception -> 0x0779 }
            goto L_0x060a
        L_0x0779:
            r0 = move-exception
            goto L_0x06d4
        L_0x077c:
            r0 = move-exception
            r12 = r1
            goto L_0x0610
        L_0x0780:
            r12 = r31
            r10 = 3
            java.lang.String r1 = "n_createSecondWebview"
            boolean r1 = android.text.TextUtils.equals(r7, r1)     // Catch:{ Exception -> 0x08fb }
            if (r1 == 0) goto L_0x08e9
            io.dcloud.common.DHInterface.IApp$ConfigProperty$ThridInfo r1 = io.dcloud.common.DHInterface.IApp.ConfigProperty.ThridInfo.SecondWebviewJsonData     // Catch:{ Exception -> 0x08e3 }
            org.json.JSONObject r1 = r5.obtainThridInfo(r1)     // Catch:{ Exception -> 0x08e3 }
            if (r1 == 0) goto L_0x060a
            java.lang.String r4 = "launch_path"
            r14 = 0
            java.lang.String r4 = r1.optString(r4, r14)     // Catch:{ Exception -> 0x08e0 }
            java.lang.String r14 = "mode"
            java.lang.String r10 = "front"
            java.lang.String r10 = r1.optString(r14, r10)     // Catch:{ Exception -> 0x08e3 }
            org.json.JSONArray r14 = new org.json.JSONArray     // Catch:{ Exception -> 0x08e3 }
            r14.<init>()     // Catch:{ Exception -> 0x08e3 }
            r14.put(r4)     // Catch:{ Exception -> 0x08e3 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x08e3 }
            r4.<init>()     // Catch:{ Exception -> 0x08e3 }
            java.lang.String r7 = r5.obtainAppId()     // Catch:{ Exception -> 0x08db }
            r4.append(r7)     // Catch:{ Exception -> 0x08db }
            java.lang.String r7 = "__second"
            r4.append(r7)     // Catch:{ Exception -> 0x08db }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x08db }
            java.lang.String r3 = r1.optString(r3, r4)     // Catch:{ Exception -> 0x08db }
            r1.put(r6, r3)     // Catch:{ Exception -> 0x08db }
            java.lang.String r3 = "child"
            boolean r3 = r3.equals(r10)     // Catch:{ Exception -> 0x08db }
            if (r3 == 0) goto L_0x07e3
            java.lang.String r3 = "position"
            boolean r3 = r1.has(r3)     // Catch:{ Exception -> 0x07de }
            if (r3 != 0) goto L_0x07e3
            java.lang.String r3 = "position"
            java.lang.String r4 = "absolute"
            r1.put(r3, r4)     // Catch:{ Exception -> 0x07de }
            goto L_0x07e3
        L_0x07de:
            r0 = move-exception
            r1 = r30
            goto L_0x00a1
        L_0x07e3:
            r3 = 4
            r1.put(r2, r3)     // Catch:{ Exception -> 0x08db }
            r14.put(r1)     // Catch:{ Exception -> 0x08db }
            io.dcloud.feature.ui.c r1 = r15.a((int) r3)     // Catch:{ Exception -> 0x08db }
            if (r1 == 0) goto L_0x07f3
            r17 = 1
            goto L_0x07f5
        L_0x07f3:
            r17 = 0
        L_0x07f5:
            if (r11 != 0) goto L_0x080e
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x07de }
            r1.<init>()     // Catch:{ Exception -> 0x07de }
            java.lang.String r2 = r5.obtainAppId()     // Catch:{ Exception -> 0x07de }
            r1.append(r2)     // Catch:{ Exception -> 0x07de }
            java.lang.String r2 = "__second"
            r1.append(r2)     // Catch:{ Exception -> 0x07de }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x07de }
            r6 = r1
            goto L_0x080f
        L_0x080e:
            r6 = r11
        L_0x080f:
            r7 = 0
            r1 = r28
            r2 = r15
            r3 = r29
            r4 = r14
            r11 = r30
            io.dcloud.feature.ui.c r1 = r1.a(r2, r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x08d5 }
            if (r17 != 0) goto L_0x060a
            r2 = 1
            r1.G = r2     // Catch:{ Exception -> 0x08d5 }
            r1.J = r2     // Catch:{ Exception -> 0x08d5 }
            io.dcloud.common.DHInterface.IFrameView r2 = r29.obtainFrameView()     // Catch:{ Exception -> 0x08d5 }
            int r2 = r2.hashCode()     // Catch:{ Exception -> 0x08d5 }
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ Exception -> 0x08d5 }
            r3 = 0
            io.dcloud.feature.ui.c r2 = r15.a((java.lang.String) r2, (java.lang.String) r3, (java.lang.String) r3)     // Catch:{ Exception -> 0x08d3 }
            java.util.List<io.dcloud.feature.ui.c> r4 = r15.b     // Catch:{ Exception -> 0x08d3 }
            int r4 = r4.indexOf(r2)     // Catch:{ Exception -> 0x08d3 }
            java.lang.String r5 = "behind"
            boolean r5 = r5.equals(r10)     // Catch:{ Exception -> 0x08d3 }
            if (r5 == 0) goto L_0x0855
            r5 = 1
            int r4 = r4 - r5
            long r5 = r2.v     // Catch:{ Exception -> 0x08d3 }
            r7 = 1
            long r5 = r5 - r7
            r1.v = r5     // Catch:{ Exception -> 0x08d3 }
            if (r4 >= 0) goto L_0x084f
            r14 = 0
            goto L_0x0850
        L_0x084f:
            r14 = r4
        L_0x0850:
            r15.a((java.lang.String) r13, (io.dcloud.feature.ui.c) r1, (int) r14)     // Catch:{ Exception -> 0x08d3 }
            goto L_0x08f7
        L_0x0855:
            java.lang.String r5 = "parent"
            boolean r5 = r5.equals(r10)     // Catch:{ Exception -> 0x08d3 }
            if (r5 == 0) goto L_0x0883
            r2.f()     // Catch:{ Exception -> 0x08d3 }
            r1.a((io.dcloud.feature.ui.b) r2)     // Catch:{ Exception -> 0x08d3 }
            int r2 = r15.c((io.dcloud.feature.ui.c) r1)     // Catch:{ Exception -> 0x08d3 }
            r15.a((java.lang.String) r13, (io.dcloud.feature.ui.c) r1, (int) r2)     // Catch:{ Exception -> 0x08d3 }
            io.dcloud.common.DHInterface.AbsMgr r2 = r9.b     // Catch:{ Exception -> 0x08d3 }
            io.dcloud.common.DHInterface.IMgr$MgrType r4 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr     // Catch:{ Exception -> 0x08d3 }
            r5 = 3
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Exception -> 0x08d3 }
            io.dcloud.common.DHInterface.IFrameView r1 = r1.z     // Catch:{ Exception -> 0x08d3 }
            r6 = 0
            r5[r6] = r1     // Catch:{ Exception -> 0x08d3 }
            java.lang.Boolean r1 = java.lang.Boolean.FALSE     // Catch:{ Exception -> 0x08d3 }
            r6 = 1
            r5[r6] = r1     // Catch:{ Exception -> 0x08d3 }
            r7 = 2
            r5[r7] = r1     // Catch:{ Exception -> 0x08d3 }
            r2.processEvent(r4, r6, r5)     // Catch:{ Exception -> 0x08d3 }
            goto L_0x08f7
        L_0x0883:
            java.lang.String r5 = "child"
            boolean r5 = r5.equals(r10)     // Catch:{ Exception -> 0x08d3 }
            if (r5 == 0) goto L_0x08af
            io.dcloud.common.DHInterface.IFrameView r4 = r29.obtainFrameView()     // Catch:{ Exception -> 0x08d3 }
            io.dcloud.common.adapter.ui.AdaFrameView r4 = (io.dcloud.common.adapter.ui.AdaFrameView) r4     // Catch:{ Exception -> 0x08d3 }
            io.dcloud.common.adapter.util.ViewOptions r4 = r4.obtainFrameOptions()     // Catch:{ Exception -> 0x08d3 }
            org.json.JSONObject r5 = r4.mJsonViewOption     // Catch:{ Exception -> 0x08d3 }
            if (r5 != 0) goto L_0x08a4
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{ Exception -> 0x08d3 }
            java.lang.String r6 = "{\"width\":\"100%\",\"height\":\"100%\"}"
            r5.<init>(r6)     // Catch:{ Exception -> 0x08d3 }
            r4.updateViewData((org.json.JSONObject) r5)     // Catch:{ Exception -> 0x08d3 }
        L_0x08a4:
            r2.a((io.dcloud.feature.ui.b) r1)     // Catch:{ Exception -> 0x08d3 }
            int r2 = r15.c((io.dcloud.feature.ui.c) r1)     // Catch:{ Exception -> 0x08d3 }
            r15.a((java.lang.String) r13, (io.dcloud.feature.ui.c) r1, (int) r2)     // Catch:{ Exception -> 0x08d3 }
            goto L_0x08f7
        L_0x08af:
            long r5 = r2.v     // Catch:{ Exception -> 0x08d3 }
            r7 = 1
            long r5 = r5 + r7
            r1.v = r5     // Catch:{ Exception -> 0x08d3 }
            r2 = 1
            int r4 = r4 + r2
            r15.a((java.lang.String) r13, (io.dcloud.feature.ui.c) r1, (int) r4)     // Catch:{ Exception -> 0x08d3 }
            io.dcloud.common.DHInterface.AbsMgr r2 = r9.b     // Catch:{ Exception -> 0x08d3 }
            io.dcloud.common.DHInterface.IMgr$MgrType r4 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr     // Catch:{ Exception -> 0x08d3 }
            r5 = 3
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Exception -> 0x08d3 }
            io.dcloud.common.DHInterface.IFrameView r1 = r1.z     // Catch:{ Exception -> 0x08d3 }
            r6 = 0
            r5[r6] = r1     // Catch:{ Exception -> 0x08d3 }
            java.lang.Boolean r1 = java.lang.Boolean.FALSE     // Catch:{ Exception -> 0x08d3 }
            r6 = 1
            r5[r6] = r1     // Catch:{ Exception -> 0x08d3 }
            r7 = 2
            r5[r7] = r1     // Catch:{ Exception -> 0x08d3 }
            r2.processEvent(r4, r6, r5)     // Catch:{ Exception -> 0x08d3 }
            goto L_0x08f7
        L_0x08d3:
            r0 = move-exception
            goto L_0x08d7
        L_0x08d5:
            r0 = move-exception
            r3 = 0
        L_0x08d7:
            r2 = r0
            r13 = r3
            r1 = r11
            goto L_0x0914
        L_0x08db:
            r0 = move-exception
            r3 = 0
            r1 = r30
            goto L_0x090c
        L_0x08e0:
            r0 = move-exception
            r3 = r14
            goto L_0x08e5
        L_0x08e3:
            r0 = move-exception
            r3 = 0
        L_0x08e5:
            r2 = r0
            r13 = r3
            r1 = r7
            goto L_0x0914
        L_0x08e9:
            r1 = r7
            r3 = 0
            io.dcloud.feature.ui.b r2 = r15.a((java.lang.String) r11)     // Catch:{ Exception -> 0x08f9 }
            if (r2 == 0) goto L_0x08f7
            java.lang.String r1 = r2.a((io.dcloud.common.DHInterface.IWebview) r8, (java.lang.String) r4, (org.json.JSONArray) r14)     // Catch:{ Exception -> 0x08f9 }
            goto L_0x05f4
        L_0x08f7:
            r13 = r3
            goto L_0x0935
        L_0x08f9:
            r0 = move-exception
            goto L_0x090c
        L_0x08fb:
            r0 = move-exception
            goto L_0x0900
        L_0x08fd:
            r0 = move-exception
            r12 = r31
        L_0x0900:
            r1 = r7
            goto L_0x090b
        L_0x0902:
            r0 = move-exception
            r1 = r30
            r12 = r31
            goto L_0x090b
        L_0x0908:
            r0 = move-exception
            r1 = r10
            r12 = r11
        L_0x090b:
            r3 = 0
        L_0x090c:
            r2 = r0
            r13 = r3
            goto L_0x0914
        L_0x090f:
            r0 = move-exception
            r1 = r10
            r12 = r11
            r3 = r13
        L_0x0913:
            r2 = r0
        L_0x0914:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "pActionName="
            r3.append(r4)
            r3.append(r1)
            java.lang.String r1 = ";pJsArgs="
            r3.append(r1)
            r3.append(r12)
            java.lang.String r1 = r3.toString()
            java.lang.String r3 = "UIWidgetMgr"
            io.dcloud.common.adapter.util.Logger.e(r3, r1)
            r2.printStackTrace()
        L_0x0935:
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.e.a(io.dcloud.common.DHInterface.IWebview, java.lang.String, org.json.JSONArray):java.lang.String");
    }

    public void b(String str, a aVar, IFrameView iFrameView) {
        a aVar2 = aVar;
        IFrameView iFrameView2 = iFrameView;
        String valueOf = String.valueOf(iFrameView.hashCode());
        IWebview obtainWebView = iFrameView.obtainWebView();
        String obtainUrl = obtainWebView.obtainUrl();
        AdaFrameView adaFrameView = (AdaFrameView) iFrameView2;
        JSONObject jSONObject = adaFrameView.obtainFrameOptions() != null ? adaFrameView.obtainFrameOptions().mJsonViewOption : null;
        String obtainFrameId = obtainWebView.obtainFrameId();
        String str2 = !PdrUtil.isEmpty(obtainFrameId) ? obtainFrameId : iFrameView.getFrameType() == 2 ? str : obtainUrl;
        String str3 = valueOf;
        JSONObject jSONObject2 = jSONObject;
        c cVar = new c(aVar, obtainUrl, str2, str3, jSONObject2);
        cVar.a(iFrameView.getContext(), aVar, iFrameView.obtainWebView(), str3, jSONObject2);
        cVar.G = iFrameView.getFrameType() != 2 || adaFrameView.obtainMainView().getVisibility() == 0;
        cVar.J = true;
        adaFrameView.addFrameViewListener(cVar);
        cVar.a(iFrameView2, str2);
        aVar2.e(cVar);
        aVar2.a(str, cVar, 0);
        MessageHandler.sendMessage(new a(aVar2, cVar), (Object) null);
    }

    public void b(String str) {
        if (PdrUtil.isEmpty(str)) {
            for (a a2 : this.c.values()) {
                a2.a();
            }
            this.c.clear();
            return;
        }
        a aVar = this.c.get(str);
        if (aVar != null) {
            Logger.d(Logger.MAIN_TAG, "UIWidgetMgr.dispose pAppid=" + str);
            aVar.a();
        }
        this.c.remove(str);
    }

    private void a(String str, a aVar, IFrameView iFrameView) {
        IWebview obtainWebView = iFrameView.obtainWebView();
        String valueOf = String.valueOf(obtainWebView.obtainFrameId());
        String obtainUrl = obtainWebView.obtainUrl();
        String obtainFrameId = obtainWebView.obtainFrameId();
        String str2 = !PdrUtil.isEmpty(obtainFrameId) ? obtainFrameId : obtainUrl;
        String str3 = valueOf;
        c cVar = new c(aVar, obtainUrl, str2, str3, (JSONObject) null);
        cVar.a(iFrameView.getContext(), aVar, obtainWebView, str3, (JSONObject) null);
        cVar.G = false;
        cVar.J = false;
        cVar.a(true);
        iFrameView.addFrameViewListener(cVar);
        cVar.a(iFrameView, str2);
        aVar.e(cVar);
        aVar.a(str, cVar, 0);
    }

    private c a(a aVar, IWebview iWebview, JSONArray jSONArray, IApp iApp, String str, boolean z) throws Exception {
        JSONObject jSONObject;
        String str2;
        JSONArray jSONArray2 = jSONArray;
        a aVar2 = aVar;
        c a2 = aVar.a(iWebview.obtainFrameView());
        String optString = jSONArray2.optString(0);
        Log.e("UIWidgetMgr", "new -- JSNWindow=" + optString);
        JSONObject optJSONObject = jSONArray2.optJSONObject(1);
        String optString2 = jSONArray2.optString(2);
        JSONObject optJSONObject2 = jSONArray2.optJSONObject(4);
        JSONArray optJSONArray = jSONArray2.optJSONArray(5);
        if (optJSONObject == null) {
            jSONObject = new JSONObject("{}");
            str2 = "";
        } else {
            String string = JSONUtil.getString(optJSONObject, "name");
            if (TextUtils.isEmpty(string)) {
                string = JSONUtil.getString(optJSONObject, "webviewid");
            }
            str2 = string;
            jSONObject = optJSONObject;
        }
        c a3 = a(aVar, iWebview, iApp, optString, str2, str, jSONObject, optJSONObject2, optJSONArray, z);
        if (a2 != null) {
            a2.b(a3);
        }
        if (optString2 != null) {
            a3.i.put(iWebview.getWebviewANID(), optString2);
        }
        AnimOptions animOptions = ((AdaFrameItem) a3.z).getAnimOptions();
        ViewOptions obtainFrameOptions = ((AdaFrameItem) a3.z).obtainFrameOptions();
        a3.L = obtainFrameOptions.hasBackground();
        animOptions.parseTransition(obtainFrameOptions.transition);
        animOptions.parseTransform(obtainFrameOptions.transform);
        return a3;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v3, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private io.dcloud.feature.ui.c a(io.dcloud.feature.ui.a r23, io.dcloud.common.DHInterface.IWebview r24, io.dcloud.common.DHInterface.IApp r25, java.lang.String r26, java.lang.String r27, java.lang.String r28, org.json.JSONObject r29, org.json.JSONObject r30, org.json.JSONArray r31, boolean r32) {
        /*
            r22 = this;
            r0 = r22
            r7 = r23
            r8 = r24
            r9 = r25
            r10 = r26
            r11 = r27
            r12 = r29
            r13 = r31
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "createNWindow pUrl="
            r1.append(r2)
            r1.append(r10)
            java.lang.String r1 = r1.toString()
            io.dcloud.common.adapter.util.Logger.e(r1)
            java.lang.String r1 = "directPage"
            r14 = 0
            boolean r1 = r12.optBoolean(r1, r14)
            java.lang.String r2 = "winType"
            int r2 = r12.optInt(r2, r14)
            if (r2 != 0) goto L_0x0039
            java.lang.String r3 = r24.obtainFullUrl()
            goto L_0x003a
        L_0x0039:
            r3 = 0
        L_0x003a:
            java.lang.String r3 = r9.convert2WebviewFullPath(r3, r10)
            r6 = 5
            if (r1 == 0) goto L_0x0043
            r5 = 5
            goto L_0x0044
        L_0x0043:
            r5 = r2
        L_0x0044:
            if (r32 == 0) goto L_0x0048
            r4 = 0
            goto L_0x004a
        L_0x0048:
            r4 = r3
            r3 = 0
        L_0x004a:
            r25.obtainWebviewBaseUrl()
            r0.a((io.dcloud.common.DHInterface.IWebview) r8, (io.dcloud.common.DHInterface.IApp) r9, (java.lang.String) r4)
            java.lang.String r2 = r25.obtainAppId()
            boolean r1 = io.dcloud.common.util.PdrUtil.isEmpty(r26)
            r15 = 1
            r16 = r1 ^ 1
            r1 = 4
            if (r5 != r1) goto L_0x0063
            io.dcloud.feature.ui.c r17 = r7.a((int) r1)
            goto L_0x006c
        L_0x0063:
            if (r5 != r6) goto L_0x006a
            io.dcloud.feature.ui.c r17 = r7.a((int) r6)
            goto L_0x006c
        L_0x006a:
            r17 = 0
        L_0x006c:
            if (r17 != 0) goto L_0x0088
            io.dcloud.feature.ui.c r17 = new io.dcloud.feature.ui.c
            r1 = r17
            r18 = r2
            r2 = r23
            r19 = r3
            r3 = r4
            r20 = r4
            r4 = r27
            r21 = r5
            r5 = r28
            r15 = 5
            r6 = r29
            r1.<init>(r2, r3, r4, r5, r6)
            goto L_0x0091
        L_0x0088:
            r18 = r2
            r19 = r3
            r20 = r4
            r21 = r5
            r15 = 5
        L_0x0091:
            r1 = r30
            r6 = r17
            r6.y = r1
            io.dcloud.common.DHInterface.AbsMgr r1 = r0.b
            io.dcloud.common.DHInterface.IMgr$MgrType r2 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr
            java.lang.Object[] r3 = new java.lang.Object[r15]
            java.lang.Integer r4 = java.lang.Integer.valueOf(r21)
            r3[r14] = r4
            r4 = 1
            r3[r4] = r9
            r5 = 3
            java.lang.Object[] r15 = new java.lang.Object[r5]
            r15[r14] = r10
            r15[r4] = r12
            r4 = 2
            r15[r4] = r28
            r3[r4] = r15
            io.dcloud.common.DHInterface.IFrameView r4 = r24.obtainFrameView()
            r3[r5] = r4
            r10 = 4
            r3[r10] = r6
            java.lang.Object r1 = r1.processEvent(r2, r5, r3)
            r15 = r1
            io.dcloud.common.DHInterface.IFrameView r15 = (io.dcloud.common.DHInterface.IFrameView) r15
            if (r32 == 0) goto L_0x00cd
            io.dcloud.common.DHInterface.IWebview r1 = r15.obtainWebView()
            r3 = r19
            r1.setOriginalUrl(r3)
        L_0x00cd:
            if (r13 == 0) goto L_0x00d3
            r6.w = r13
            r6.x = r8
        L_0x00d3:
            r6.a((io.dcloud.common.DHInterface.IFrameView) r15, (java.lang.String) r11)
            io.dcloud.common.DHInterface.IFrameView r1 = r6.z
            io.dcloud.common.DHInterface.IWebview r1 = r1.obtainWebView()
            java.lang.String r2 = "plusrequire"
            boolean r3 = r12.has(r2)
            if (r3 == 0) goto L_0x00eb
            java.lang.String r3 = r12.optString(r2)
            r1.setWebviewProperty(r2, r3)
        L_0x00eb:
            java.lang.String r3 = "replacewebapi"
            boolean r4 = r12.has(r3)
            java.lang.String r5 = "geolocation"
            if (r4 == 0) goto L_0x0109
            org.json.JSONObject r3 = r12.optJSONObject(r3)
            if (r3 == 0) goto L_0x0116
            boolean r4 = r3.has(r5)
            if (r4 == 0) goto L_0x0116
            java.lang.String r3 = r3.optString(r5)
            r1.setWebviewProperty(r5, r3)
            goto L_0x0116
        L_0x0109:
            boolean r3 = r12.has(r5)
            if (r3 == 0) goto L_0x0116
            java.lang.String r3 = r12.optString(r5)
            r1.setWebviewProperty(r5, r3)
        L_0x0116:
            java.lang.String r3 = "injection"
            boolean r4 = r12.has(r3)
            if (r4 == 0) goto L_0x0129
            boolean r4 = r12.optBoolean(r3)
            java.lang.String r4 = java.lang.String.valueOf(r4)
            r1.setWebviewProperty(r3, r4)
        L_0x0129:
            java.lang.String r3 = "overrideresource"
            boolean r4 = r12.has(r3)
            if (r4 == 0) goto L_0x0138
            org.json.JSONArray r3 = r12.optJSONArray(r3)
            r1.setOverrideResourceRequest(r3)
        L_0x0138:
            java.lang.String r3 = "overrideurl"
            boolean r4 = r12.has(r3)
            if (r4 == 0) goto L_0x0147
            org.json.JSONObject r3 = r12.optJSONObject(r3)
            r1.setOverrideUrlLoadingData(r3)
        L_0x0147:
            boolean r3 = io.dcloud.common.util.BaseInfo.isWap2AppAppid(r18)
            if (r3 == 0) goto L_0x01ba
            io.dcloud.common.DHInterface.IFrameView r3 = r1.obtainFrameView()
            int r3 = r3.getFrameType()
            if (r3 == r10) goto L_0x0162
            io.dcloud.common.DHInterface.IFrameView r3 = r1.obtainFrameView()
            int r3 = r3.getFrameType()
            r4 = 5
            if (r3 != r4) goto L_0x01ba
        L_0x0162:
            java.lang.String r2 = r1.getWebviewProperty(r2)
            java.lang.String r3 = "none"
            boolean r2 = r2.equals(r3)
            if (r2 != 0) goto L_0x0182
            java.lang.String r2 = "_www/__wap2app.js"
            r3 = 0
            java.lang.String r2 = r9.convert2AbsFullPath(r3, r2)
            r1.appendPreloadJsFile(r2)
            java.lang.String r2 = "_www/__wap2appconfig.js"
            java.lang.String r2 = r9.convert2AbsFullPath(r3, r2)
            r1.appendPreloadJsFile(r2)
            goto L_0x0183
        L_0x0182:
            r3 = 0
        L_0x0183:
            java.lang.String r2 = r24.obtainFullUrl()
            java.lang.String r4 = "_www/server_index_append.js"
            java.lang.String r2 = r9.convert2AbsFullPath(r2, r4)
            r4 = 1
            r1.setPreloadJsFile(r2, r4)
            java.lang.String r2 = "_www/server_index_append.css"
            java.lang.String r2 = r9.convert2AbsFullPath(r3, r2)
            java.io.File r4 = new java.io.File
            r4.<init>(r2)
            boolean r4 = r4.exists()
            if (r4 == 0) goto L_0x01a6
            r1.setCssFile(r2, r3)
            goto L_0x01ba
        L_0x01a6:
            java.lang.String r2 = "_www/__wap2app.css"
            java.lang.String r2 = r9.convert2AbsFullPath(r3, r2)
            java.io.File r4 = new java.io.File
            r4.<init>(r2)
            boolean r4 = r4.exists()
            if (r4 == 0) goto L_0x01ba
            r1.setCssFile(r2, r3)
        L_0x01ba:
            java.lang.String r2 = "appendCss"
            boolean r3 = r12.has(r2)
            if (r3 == 0) goto L_0x01c7
            java.lang.String r3 = r12.optString(r2)
            goto L_0x01d5
        L_0x01c7:
            java.lang.String r2 = "preloadcss"
            boolean r3 = r12.has(r2)
            if (r3 == 0) goto L_0x01d4
            java.lang.String r3 = r12.optString(r2)
            goto L_0x01d5
        L_0x01d4:
            r3 = 0
        L_0x01d5:
            boolean r2 = android.text.TextUtils.isEmpty(r3)
            if (r2 != 0) goto L_0x01e0
            r2 = 0
            r1.setCssFile(r2, r3)
            goto L_0x01e1
        L_0x01e0:
            r2 = 0
        L_0x01e1:
            java.lang.String r3 = "appendJs"
            boolean r4 = r12.has(r3)
            if (r4 == 0) goto L_0x01f2
            java.lang.String r3 = r12.optString(r3)
            java.lang.String r2 = r9.convert2LocalFullPath(r2, r3)
            goto L_0x0202
        L_0x01f2:
            java.lang.String r3 = "preloadjs"
            boolean r4 = r12.has(r3)
            if (r4 == 0) goto L_0x0202
            java.lang.String r3 = r12.optString(r3)
            java.lang.String r2 = r9.convert2LocalFullPath(r2, r3)
        L_0x0202:
            boolean r3 = android.text.TextUtils.isEmpty(r2)
            if (r3 != 0) goto L_0x020b
            r1.appendPreloadJsFile(r2)
        L_0x020b:
            if (r16 == 0) goto L_0x0239
            r1 = 6
            r2 = r21
            if (r2 != r1) goto L_0x022e
            java.lang.String r1 = "additionalHttpHeaders"
            boolean r2 = r12.has(r1)
            if (r2 == 0) goto L_0x022e
            io.dcloud.common.DHInterface.IFrameView r2 = r6.z
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()
            org.json.JSONObject r1 = r12.optJSONObject(r1)
            java.util.HashMap r1 = io.dcloud.common.util.JSONUtil.toMap(r1)
            r3 = r20
            r2.setLoadURLHeads(r3, r1)
            goto L_0x0230
        L_0x022e:
            r3 = r20
        L_0x0230:
            io.dcloud.common.DHInterface.IFrameView r1 = r6.z
            io.dcloud.common.DHInterface.IWebview r1 = r1.obtainWebView()
            r1.loadUrl(r3)
        L_0x0239:
            android.content.Context r2 = r24.getContext()
            io.dcloud.common.DHInterface.IWebview r4 = r15.obtainWebView()
            r1 = r6
            r3 = r23
            r5 = r28
            r8 = r6
            r6 = r29
            r1.a(r2, r3, r4, r5, r6)
            android.view.View r1 = r15.obtainMainView()
            r1.setVisibility(r10)
            int r1 = io.dcloud.common.adapter.util.DeviceInfo.sDeviceSdkVer
            r2 = 11
            if (r1 < r2) goto L_0x025c
            r7.b((io.dcloud.common.DHInterface.IFrameView) r15)
        L_0x025c:
            r7.e(r8)
            r8.a((org.json.JSONObject) r12, (boolean) r14)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r2 = r18
            r1.append(r2)
            java.lang.String r2 = " createNWindow webview_name="
            r1.append(r2)
            r1.append(r11)
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "View_Visible_Path"
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r2, (java.lang.String) r1)
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.e.a(io.dcloud.feature.ui.a, io.dcloud.common.DHInterface.IWebview, io.dcloud.common.DHInterface.IApp, java.lang.String, java.lang.String, java.lang.String, org.json.JSONObject, org.json.JSONObject, org.json.JSONArray, boolean):io.dcloud.feature.ui.c");
    }

    private void a(IWebview iWebview, IApp iApp, String str) {
        if (BaseInfo.isBase(iWebview.getContext()) && !TextUtils.isEmpty(str)) {
            String obtainUrl = iWebview.obtainUrl();
            if (!str.startsWith(DeviceInfo.HTTP_PROTOCOL) && !obtainUrl.startsWith(DeviceInfo.HTTP_PROTOCOL) && !str.startsWith(DeviceInfo.HTTPS_PROTOCOL) && !obtainUrl.startsWith(DeviceInfo.HTTPS_PROTOCOL)) {
                String originalUrl = WebResUtil.getOriginalUrl(obtainUrl);
                String originalUrl2 = WebResUtil.getOriginalUrl(str);
                Log.i(AbsoluteConst.HBUILDER_TAG, StringUtil.format(AbsoluteConst.OPENLOG, c(WebResUtil.getHBuilderPrintUrl(iApp.convert2RelPath(originalUrl))), c(WebResUtil.getHBuilderPrintUrl(iApp.convert2RelPath(originalUrl2)))));
            }
        }
    }

    public static b a(String str) {
        if (!PdrUtil.isEmpty(str)) {
            try {
                Object newInstance = Class.forName(a.get(str.toLowerCase(Locale.ENGLISH))).newInstance();
                if (newInstance instanceof b) {
                    return (b) newInstance;
                }
            } catch (InstantiationException e2) {
                e2.printStackTrace();
            } catch (IllegalAccessException e3) {
                e3.printStackTrace();
            } catch (ClassNotFoundException e4) {
                e4.printStackTrace();
            }
        }
        return null;
    }

    private void a() {
        a = (HashMap) this.b.processEvent(IMgr.MgrType.FeatureMgr, 4, this.e);
    }

    private ValueAnimator a(View view, int i, int i2, String str, IWebview iWebview, String str2, c cVar) {
        ValueAnimator valueAnimator;
        if (view.getLayoutParams() instanceof AbsoluteLayout.LayoutParams) {
            valueAnimator = ValueAnimator.ofInt(i, i2);
        } else if (view.getLayoutParams() instanceof FrameLayout.LayoutParams) {
            valueAnimator = ValueAnimator.ofFloat((float) i, (float) i2);
        } else {
            valueAnimator = null;
        }
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new b(view));
        valueAnimator.addListener(new c(view, cVar, iWebview, str2, str));
        return valueAnimator;
    }

    /* access modifiers changed from: private */
    public int a(View view) {
        float x;
        if (view == null) {
            return 0;
        }
        if (view.getLayoutParams() instanceof AbsoluteLayout.LayoutParams) {
            x = ViewHelper.getX(view);
        } else if (!(view.getLayoutParams() instanceof FrameLayout.LayoutParams)) {
            return 0;
        } else {
            x = ViewHelper.getX(view);
        }
        return (int) x;
    }
}
