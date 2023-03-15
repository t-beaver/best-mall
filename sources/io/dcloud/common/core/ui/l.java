package io.dcloud.common.core.ui;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.dcloud.android.widget.StatusBarView;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.ICore;
import io.dcloud.common.DHInterface.IEventCallback;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.DHInterface.IWebviewStateListener;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.ui.AdaWebViewParent;
import io.dcloud.common.adapter.ui.AdaWebview;
import io.dcloud.common.adapter.util.AnimOptions;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.adapter.util.ViewRect;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import io.dcloud.common.util.TestUtil;
import io.dcloud.common.util.TitleNViewUtil;
import io.dcloud.feature.gg.dcloud.ADSim;
import io.dcloud.nineoldandroids.view.ViewHelper;
import io.src.dcloud.adapter.DCloudAdapterUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class l extends AbsMgr implements IMgr.WindowEvent {
    HashMap<String, a> a = new HashMap<>(0);
    List<m> b = Collections.synchronizedList(new ArrayList());
    String c = null;
    Runnable d = null;
    Runnable e;
    boolean f = false;
    WindowManager.LayoutParams g = null;

    class a implements IEventCallback {
        final /* synthetic */ b a;
        final /* synthetic */ IApp b;
        final /* synthetic */ a c;

        a(b bVar, IApp iApp, a aVar) {
            this.a = bVar;
            this.b = iApp;
            this.c = aVar;
        }

        public Object onCallBack(String str, Object obj) {
            if (!PdrUtil.isEquals(str, AbsoluteConst.EVENTS_CLOSE)) {
                return null;
            }
            this.a.removeFrameViewListener(this);
            l.this.a(this.b, this.c);
            return null;
        }
    }

    class b implements IWebviewStateListener {
        boolean a = false;
        final /* synthetic */ IApp b;
        final /* synthetic */ b c;
        final /* synthetic */ boolean d;
        final /* synthetic */ boolean e;
        final /* synthetic */ String f;
        final /* synthetic */ AdaWebview g;
        final /* synthetic */ a h;
        final /* synthetic */ int i;

        class a implements MessageHandler.IMessages {
            a() {
            }

            public void execute(Object obj) {
                if (((a) b.this.b.obtainWebAppRootView()).a(5) == null) {
                    b.this.b.checkOrLoadlaunchWebview();
                }
            }
        }

        b(IApp iApp, b bVar, boolean z, boolean z2, String str, AdaWebview adaWebview, a aVar, int i2) {
            this.b = iApp;
            this.c = bVar;
            this.d = z;
            this.e = z2;
            this.f = str;
            this.g = adaWebview;
            this.h = aVar;
            this.i = i2;
        }

        public Object onCallBack(int i2, Object obj) {
            int i3;
            int i4;
            int i5 = i2;
            if (AbsoluteConst.EVENTS_TITLE_UPDATE.equals(l.this.c)) {
                i3 = 4;
            } else {
                i3 = AbsoluteConst.EVENTS_RENDERING.equals(l.this.c) ? 6 : 1;
            }
            if (i5 == 3 && !this.a) {
                Integer num = (Integer) obj;
                if (num.intValue() >= 50) {
                    this.a = true;
                    Intent intent = new Intent();
                    intent.setAction(this.b.getActivity().getPackageName() + ".streamdownload.downloadfinish." + this.b.obtainAppId());
                    intent.putExtra("appid", this.b.obtainAppId());
                    intent.putExtra("progress", num.intValue());
                    intent.putExtra("flag", AbsoluteConst.STREAMAPP_KEY_DIRECT_PAGE_PROGRESSED);
                    this.b.getActivity().sendBroadcast(intent);
                }
            }
            if (i5 == 1) {
                if (this.c.getFrameType() == 5) {
                    this.b.checkOrLoadlaunchWebview();
                } else if (this.c.getFrameType() == 4) {
                    MessageHandler.sendMessage(new a(), 3000, (Object) null);
                }
            }
            if (i5 == i3 && this.d) {
                if (this.e || (PdrUtil.isNetPath(this.f) && (i5 == 4 || i5 == 6))) {
                    boolean z = this.e;
                    if (!z) {
                        if (i5 == 4) {
                            i4 = TestUtil.PointTime.AC_TYPE_1_2;
                        } else if (i5 == 6) {
                            i4 = TestUtil.PointTime.AC_TYPE_1_3;
                        }
                        l lVar = l.this;
                        lVar.f = false;
                        lVar.a(this.g, this.b, z, this.h, 1, this.c, this.i, i4);
                    }
                    i4 = TestUtil.PointTime.AC_TYPE_1_1;
                    l lVar2 = l.this;
                    lVar2.f = false;
                    lVar2.a(this.g, this.b, z, this.h, 1, this.c, this.i, i4);
                } else {
                    this.b.setConfigProperty("timeout", "-1");
                    a aVar = this.h;
                    aVar.a(aVar, this.c, this.i, true, (int) TestUtil.PointTime.AC_TYPE_1_1);
                }
            }
            return null;
        }
    }

    class c implements Runnable {
        final /* synthetic */ AdaFrameItem a;

        c(AdaFrameItem adaFrameItem) {
            this.a = adaFrameItem;
        }

        public void run() {
            ((AdaFrameView) this.a).changeWebParentViewRect();
        }
    }

    class d implements ICallBack {
        final /* synthetic */ b a;
        final /* synthetic */ Object[] b;

        d(b bVar, Object[] objArr) {
            this.a = bVar;
            this.b = objArr;
        }

        public Object onCallBack(int i, Object obj) {
            if (this.a.z) {
                return null;
            }
            this.a.c(((Boolean) this.b[1]).booleanValue());
            return null;
        }
    }

    class e implements ICallBack {
        final /* synthetic */ b a;

        e(b bVar) {
            this.a = bVar;
        }

        public Object onCallBack(int i, Object obj) {
            boolean z = true;
            this.a.setVisible(true, false);
            this.a.q();
            this.a.lastShowTime = System.currentTimeMillis();
            this.a.p.l();
            b bVar = this.a;
            if (!bVar.isChildOfFrameView) {
                TestUtil.record("computeStackArray");
                b bVar2 = this.a;
                bVar2.p.b(bVar2);
                b bVar3 = this.a;
                bVar3.onPushToStack(bVar3.isAutoPop());
                TestUtil.print("computeStackArray", "计算满屏幕时间");
                if (!this.a.p.e().contains(this.a)) {
                    b bVar4 = this.a;
                    bVar4.p.e(bVar4);
                } else {
                    this.a.p.m();
                }
            } else if (bVar.getParentFrameItem() != null) {
                b bVar5 = this.a;
                bVar5.p.h(bVar5);
            }
            b bVar6 = this.a;
            if (!bVar6.isChildOfFrameView) {
                int i2 = bVar6.obtainApp().getInt(0);
                int i3 = this.a.obtainApp().getInt(1);
                if ((i2 != this.a.obtainFrameOptions().width || this.a.obtainFrameOptions().height + 1 < i3) && !(this.a.obtainFrameOptions().width == -1 && this.a.obtainFrameOptions().height == -1)) {
                    z = false;
                }
                if (z) {
                    i.a(this.a, 0);
                }
                if (PdrUtil.isEquals(this.a.getAnimOptions().mAnimType, "none")) {
                    this.a.makeViewOptions_animate();
                    this.a.n();
                } else {
                    this.a.t();
                    this.a.startAnimator(0);
                }
            } else if (PdrUtil.isEquals(bVar6.getAnimOptions().mAnimType, AnimOptions.ANIM_FADE_IN)) {
                this.a.t();
                this.a.startAnimator(0);
            } else {
                this.a.makeViewOptions_animate();
                this.a.n();
            }
            b bVar7 = this.a;
            bVar7.p.i(bVar7);
            return null;
        }
    }

    class f implements ICallBack {
        final /* synthetic */ b a;

        f(b bVar) {
            this.a = bVar;
        }

        public Object onCallBack(int i, Object obj) {
            b bVar = this.a;
            int c = bVar.p.c(bVar);
            this.a.q();
            boolean z = false;
            boolean z2 = this.a.obtainMainView().getVisibility() == AdaFrameItem.VISIBLE;
            b bVar2 = this.a;
            if (!bVar2.inStack || !z2 || bVar2.isChildOfFrameView) {
                bVar2.makeViewOptions_animate();
                this.a.m();
                this.a.l();
            } else {
                bVar2.p.b(bVar2);
                if (this.a.f()) {
                    l.this.processEvent(IMgr.MgrType.WindowMgr, 28, this.a.g);
                    this.a.g = null;
                }
                int i2 = this.a.obtainApp().getInt(0);
                int i3 = this.a.obtainApp().getInt(1);
                if ((i2 == this.a.obtainFrameOptions().width && this.a.obtainFrameOptions().height + 1 >= i3) || (this.a.obtainFrameOptions().width == -1 && this.a.obtainFrameOptions().height == -1)) {
                    z = true;
                }
                if ((!PdrUtil.isEquals(this.a.getAnimOptions().mAnimType_close, "none") || (BaseInfo.isDefaultAim && z)) && c >= 0) {
                    this.a.t();
                    if (z && !PdrUtil.isEquals(this.a.getAnimOptions().mAnimType_close, "none")) {
                        i.a(this.a, 1);
                    }
                    this.a.startAnimator(1);
                } else {
                    this.a.makeViewOptions_animate();
                    this.a.m();
                    this.a.l();
                }
            }
            return null;
        }
    }

    class g implements ICallBack {
        final /* synthetic */ b a;
        final /* synthetic */ int b;

        g(b bVar, int i) {
            this.a = bVar;
            this.b = i;
        }

        public Object onCallBack(int i, Object obj) {
            Object obj2;
            b bVar = this.a;
            int c2 = bVar.p.c(bVar);
            this.a.q();
            b bVar2 = this.a;
            if (!bVar2.p.d(bVar2)) {
                b bVar3 = this.a;
                if (bVar3.p.q) {
                    IApp obtainApp = bVar3.obtainApp();
                    this.a.s();
                    if (this.b == 2) {
                        b bVar4 = this.a;
                        if (bVar4.inStack) {
                            bVar4.getAnimOptions().mAnimType_close = AnimOptions.ANIM_ZOOM_FADE_IN;
                            this.a.t();
                            this.a.startAnimator(1);
                        } else {
                            bVar4.j();
                        }
                        if (this.a.getFrameType() == 3) {
                            l.this.processEvent(IMgr.MgrType.WindowMgr, 42, this.a);
                        }
                    } else {
                        this.a.j();
                    }
                    l lVar = l.this;
                    IMgr.MgrType mgrType = IMgr.MgrType.AppMgr;
                    boolean parseBoolean = Boolean.parseBoolean(String.valueOf(lVar.processEvent(mgrType, 13, obtainApp)));
                    if (parseBoolean) {
                        l.this.processEvent(mgrType, 10, obtainApp);
                        obj2 = Boolean.valueOf(parseBoolean);
                        b bVar5 = this.a;
                        bVar5.n = false;
                        bVar5.m = false;
                        bVar5.inStack = false;
                        return obj2;
                    }
                    obj2 = AbsoluteConst.TRUE;
                    b bVar52 = this.a;
                    bVar52.n = false;
                    bVar52.m = false;
                    bVar52.inStack = false;
                    return obj2;
                }
            }
            b bVar6 = this.a;
            boolean z = bVar6.isChildOfFrameView;
            boolean z2 = bVar6.obtainMainView().getVisibility() == 0;
            boolean z3 = (this.a.obtainApp().getInt(0) == this.a.obtainFrameOptions().width && this.a.obtainFrameOptions().height + 1 >= this.a.obtainApp().getInt(1)) || (this.a.obtainFrameOptions().width == -1 && this.a.obtainFrameOptions().height == -1);
            if (c2 >= 0 && z3 && !PdrUtil.isEquals(this.a.getAnimOptions().mAnimType_close, "none")) {
                i.a(this.a, 1);
            }
            this.a.s();
            if (this.b != 2 || c2 < 0) {
                this.a.j();
                obj2 = AbsoluteConst.TRUE;
                b bVar522 = this.a;
                bVar522.n = false;
                bVar522.m = false;
                bVar522.inStack = false;
                return obj2;
            }
            b bVar7 = this.a;
            if (!bVar7.inStack || !z2 || PdrUtil.isEquals(bVar7.getAnimOptions().mAnimType_close, "none")) {
                this.a.j();
            } else {
                this.a.t();
                this.a.startAnimator(1);
            }
            if (this.a.getFrameType() == 3) {
                l.this.processEvent(IMgr.MgrType.WindowMgr, 42, this.a);
            }
            obj2 = AbsoluteConst.TRUE;
            b bVar5222 = this.a;
            bVar5222.n = false;
            bVar5222.m = false;
            bVar5222.inStack = false;
            return obj2;
        }
    }

    class h implements ICallBack {
        final /* synthetic */ b a;

        h(b bVar) {
            this.a = bVar;
        }

        public Object onCallBack(int i, Object obj) {
            b bVar = this.a;
            bVar.p.e(bVar);
            this.a.setVisible(true, false);
            this.a.p.j();
            return Boolean.FALSE;
        }
    }

    class i implements IWebviewStateListener {
        boolean a = false;
        final /* synthetic */ String b;
        final /* synthetic */ boolean c;
        final /* synthetic */ IApp d;
        final /* synthetic */ a e;
        final /* synthetic */ String f;
        final /* synthetic */ IWebview g;
        final /* synthetic */ int h;
        final /* synthetic */ b i;
        final /* synthetic */ int j;
        final /* synthetic */ long k;

        i(String str, boolean z, IApp iApp, a aVar, String str2, IWebview iWebview, int i2, b bVar, int i3, long j2) {
            this.b = str;
            this.c = z;
            this.d = iApp;
            this.e = aVar;
            this.f = str2;
            this.g = iWebview;
            this.h = i2;
            this.i = bVar;
            this.j = i3;
            this.k = j2;
        }

        public Object onCallBack(int i2, Object obj) {
            int i3;
            int i4 = i2;
            Object obj2 = obj;
            if (AbsoluteConst.EVENTS_TITLE_UPDATE.equals(l.this.c)) {
                i3 = 4;
            } else {
                i3 = AbsoluteConst.EVENTS_RENDERING.equals(l.this.c) ? 6 : 1;
            }
            Logger.d(Logger.MAIN_TAG, "autoCloseSplash4LaunchWebview  IWebviewStateListener pType= " + i4 + ";pArgs=" + obj2);
            if (i4 == i3) {
                if (this.b.equals("id:*") && this.c) {
                    l.this.a(this.d, this.e);
                } else if (this.b.equals("default") && this.c) {
                    if (!PdrUtil.isNetPath(this.f) || !(i4 == 4 || i4 == 6)) {
                        this.d.setConfigProperty("timeout", "-1");
                        a aVar = this.e;
                        aVar.a(aVar, this.i, this.j, true, (int) TestUtil.PointTime.AC_TYPE_1_1);
                    } else {
                        int i5 = i4 == 4 ? TestUtil.PointTime.AC_TYPE_1_2 : i4 == 6 ? TestUtil.PointTime.AC_TYPE_1_3 : TestUtil.PointTime.AC_TYPE_1_1;
                        l lVar = l.this;
                        lVar.f = false;
                        lVar.a(this.g, this.d, false, this.e, this.h, this.i, this.j, i5);
                    }
                }
                BaseInfo.setLoadingLaunchePage(false, "f_need_auto_close_splash");
                long currentTimeMillis = System.currentTimeMillis() - this.k;
                this.d.setConfigProperty(IApp.ConfigProperty.CONFIG_LOADED_TIME, String.valueOf(currentTimeMillis));
                IWebview iWebview = this.g;
                iWebview.evalJS(AbsoluteConst.PROTOCOL_JAVASCRIPT + StringUtil.format(AbsoluteConst.JS_RUNTIME_BASE, StringUtil.format(AbsoluteConst.JS_RUNTIME_LOADEDTIME, String.valueOf(currentTimeMillis))));
                return null;
            } else if (i4 != 3) {
                return null;
            } else {
                IActivityHandler iActivityHandler = DCloudAdapterUtil.getIActivityHandler(this.d.getActivity());
                if (iActivityHandler != null) {
                    iActivityHandler.updateParam("progress", obj2);
                }
                if (!l.this.a(this.d) || this.a) {
                    return null;
                }
                Integer num = (Integer) obj2;
                if (num.intValue() < 50) {
                    return null;
                }
                this.a = true;
                Intent intent = new Intent();
                intent.setAction(this.d.getActivity().getPackageName() + ".streamdownload.downloadfinish." + this.d.obtainAppId());
                intent.putExtra("appid", this.d.obtainAppId());
                intent.putExtra("progress", num.intValue());
                intent.putExtra("flag", AbsoluteConst.STREAMAPP_KEY_DIRECT_PAGE_PROGRESSED);
                this.d.getActivity().sendBroadcast(intent);
                return null;
            }
        }
    }

    class j implements Runnable {
        final /* synthetic */ a a;
        final /* synthetic */ b b;
        final /* synthetic */ int c;

        j(a aVar, b bVar, int i) {
            this.a = aVar;
            this.b = bVar;
            this.c = i;
        }

        public void run() {
            a aVar = this.a;
            if (aVar != null) {
                aVar.a(aVar, this.b, this.c, true, 1000);
            }
            l.this.d = null;
        }
    }

    class k implements Runnable {
        final /* synthetic */ b a;
        final /* synthetic */ a b;
        final /* synthetic */ IApp c;

        k(b bVar, a aVar, IApp iApp) {
            this.a = bVar;
            this.b = aVar;
            this.c = iApp;
        }

        public void run() {
            try {
                if (!this.a.obtainWebView().isLoaded()) {
                    l.this.a(this.c, this.b);
                } else if (this.a.obtainWebView().obtainUrl().endsWith("__uniappservice.html") || this.a.obtainWebView().checkWhite("auto")) {
                    l.this.a(this.c, this.b);
                } else {
                    a aVar = this.b;
                    aVar.a(aVar, this.a, 0, true, 1);
                }
            } catch (Exception unused) {
            }
        }
    }

    /* renamed from: io.dcloud.common.core.ui.l$l  reason: collision with other inner class name */
    class C0019l implements Runnable {
        final /* synthetic */ a a;
        final /* synthetic */ boolean b;
        final /* synthetic */ b c;
        final /* synthetic */ IWebview d;
        final /* synthetic */ IApp e;
        final /* synthetic */ int f;
        final /* synthetic */ int g;
        final /* synthetic */ int h;

        C0019l(a aVar, boolean z, b bVar, IWebview iWebview, IApp iApp, int i2, int i3, int i4) {
            this.a = aVar;
            this.b = z;
            this.c = bVar;
            this.d = iWebview;
            this.e = iApp;
            this.f = i2;
            this.g = i3;
            this.h = i4;
        }

        public void run() {
            try {
                a aVar = this.a;
                if (aVar == null) {
                    return;
                }
                if (!aVar.q) {
                    if (!l.this.f) {
                        if ((this.b || this.c.obtainFrameOptions().titleNView == null) && this.d.checkWhite("auto")) {
                            l.this.a(this.d, this.e, this.b, this.a, this.h, this.c, this.f, this.g);
                            return;
                        }
                        System.currentTimeMillis();
                        long j = BaseInfo.startTime;
                        this.e.setConfigProperty("timeout", "-1");
                        a aVar2 = this.a;
                        aVar2.a(aVar2, this.c, this.f, true, this.g);
                    }
                }
            } catch (Exception unused) {
            }
        }
    }

    public interface m {
        void onAnimationEnd();
    }

    public l(ICore iCore) {
        super(iCore, "windowmgr", IMgr.MgrType.WindowMgr);
    }

    private boolean a(int i2, int i3, int i4, int i5, int i6, int i7) {
        return i2 == 0 && i3 == 0 && i4 == i6 && i5 == i7;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v2, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v5, resolved type: io.dcloud.common.core.ui.b} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0089  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00c5  */
    /* JADX WARNING: Removed duplicated region for block: B:37:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void b(int r14, java.lang.Object r15) {
        /*
            r13 = this;
            boolean r14 = r15 instanceof java.lang.Object[]
            if (r14 == 0) goto L_0x00c8
            java.lang.Object[] r15 = (java.lang.Object[]) r15
            r14 = 0
            r0 = r15[r14]
            io.dcloud.common.DHInterface.IApp r0 = (io.dcloud.common.DHInterface.IApp) r0
            int r1 = r15.length
            r2 = 2
            r3 = 3
            if (r1 < r3) goto L_0x0019
            r1 = r15[r2]
            java.lang.Boolean r1 = (java.lang.Boolean) r1
            boolean r1 = r1.booleanValue()
            goto L_0x001a
        L_0x0019:
            r1 = 0
        L_0x001a:
            java.lang.String r4 = r0.obtainAppId()
            java.util.HashMap<java.lang.String, io.dcloud.common.core.ui.a> r5 = r13.a
            java.lang.Object r5 = r5.get(r4)
            io.dcloud.common.core.ui.a r5 = (io.dcloud.common.core.ui.a) r5
            io.dcloud.common.core.ui.b r6 = r5.d
            r7 = 1
            if (r6 != 0) goto L_0x002d
            r8 = 1
            goto L_0x002e
        L_0x002d:
            r8 = 0
        L_0x002e:
            r9 = 0
            if (r6 != 0) goto L_0x007f
            android.content.Intent r6 = r0.obtainWebAppIntent()
            java.lang.String r10 = "__from_stream_open_style__"
            java.lang.String r6 = r6.getStringExtra(r10)
            boolean r11 = android.text.TextUtils.isEmpty(r6)     // Catch:{ JSONException -> 0x0058 }
            if (r11 != 0) goto L_0x0050
            org.json.JSONObject r11 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0058 }
            r11.<init>(r6)     // Catch:{ JSONException -> 0x0058 }
            android.content.Intent r6 = r0.obtainWebAppIntent()     // Catch:{ JSONException -> 0x004e }
            r6.removeExtra(r10)     // Catch:{ JSONException -> 0x004e }
            goto L_0x005d
        L_0x004e:
            r6 = move-exception
            goto L_0x005a
        L_0x0050:
            java.lang.String r6 = "{}"
            org.json.JSONObject r11 = io.dcloud.common.util.JSONUtil.createJSONObject(r6)     // Catch:{ JSONException -> 0x0058 }
            goto L_0x005d
        L_0x0058:
            r6 = move-exception
            r11 = r9
        L_0x005a:
            r6.printStackTrace()
        L_0x005d:
            io.dcloud.common.DHInterface.IMgr$MgrType r6 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr
            r10 = 4
            java.lang.Object[] r10 = new java.lang.Object[r10]
            java.lang.Integer r12 = java.lang.Integer.valueOf(r3)
            r10[r14] = r12
            r10[r7] = r0
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r12 = r15[r7]
            r0[r14] = r12
            r0[r7] = r11
            r10[r2] = r0
            r10[r3] = r5
            java.lang.Object r0 = r13.processEvent(r6, r3, r10)
            r6 = r0
            io.dcloud.common.core.ui.b r6 = (io.dcloud.common.core.ui.b) r6
            r5.d = r6
        L_0x007f:
            io.dcloud.common.DHInterface.IWebview r0 = r6.obtainWebView()
            int r2 = android.os.Build.VERSION.SDK_INT
            r3 = 10
            if (r2 <= r3) goto L_0x009a
            if (r1 != 0) goto L_0x0093
            android.view.ViewGroup r14 = r0.obtainWindowView()
            r14.setLayerType(r7, r9)
            goto L_0x009a
        L_0x0093:
            android.view.ViewGroup r1 = r0.obtainWindowView()
            r1.setLayerType(r14, r9)
        L_0x009a:
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r1 = "load "
            r14.append(r1)
            r14.append(r4)
            java.lang.String r1 = " launchPage ="
            r14.append(r1)
            r1 = r15[r7]
            r14.append(r1)
            java.lang.String r14 = r14.toString()
            java.lang.String r1 = "Main_Path"
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r1, (java.lang.String) r14)
            r14 = r15[r7]
            java.lang.String r14 = java.lang.String.valueOf(r14)
            r0.loadUrl(r14)
            if (r8 == 0) goto L_0x00c8
            r5.e((io.dcloud.common.core.ui.b) r6)
        L_0x00c8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.l.b(int, java.lang.Object):void");
    }

    /* access modifiers changed from: protected */
    public synchronized void c() {
        if (this.b != null) {
            try {
                ArrayList arrayList = new ArrayList();
                for (m next : this.b) {
                    next.onAnimationEnd();
                    arrayList.add(next);
                }
                if (arrayList.size() > 0) {
                    this.b.removeAll(arrayList);
                }
                arrayList.clear();
            } catch (Exception unused) {
            }
        }
    }

    public void d(b bVar) {
        IApp obtainApp = bVar.obtainApp();
        obtainApp.setMaskLayer(true);
        obtainApp.obtainWebAppRootView().obtainMainView().invalidate();
    }

    public void dispose() {
        try {
            List<m> list = this.b;
            if (list != null) {
                list.clear();
            }
            for (String str : this.a.keySet()) {
                this.a.get(str).dispose();
            }
            this.a.clear();
            if (BaseInfo.ISDEBUG) {
                f.b();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v5, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v27, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v253, resolved type: io.dcloud.common.core.ui.b} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v254, resolved type: java.lang.Boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v255, resolved type: java.util.ArrayList<io.dcloud.common.core.ui.b>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v256, resolved type: java.util.ArrayList<io.dcloud.common.core.ui.b>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v258, resolved type: io.dcloud.common.DHInterface.IFrameView} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v259, resolved type: io.dcloud.common.core.ui.b} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v260, resolved type: io.dcloud.common.core.ui.b} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v261, resolved type: io.dcloud.common.core.ui.b} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v262, resolved type: io.dcloud.common.core.ui.b} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v263, resolved type: io.dcloud.common.core.ui.b} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v264, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v265, resolved type: java.lang.String} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x024c A[Catch:{ Exception -> 0x0278, all -> 0x0af8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:101:0x0251 A[Catch:{ Exception -> 0x0278, all -> 0x0af8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:103:0x0254 A[Catch:{ Exception -> 0x0278, all -> 0x0af8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x025c A[Catch:{ Exception -> 0x0278, all -> 0x0af8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x0268 A[Catch:{ Exception -> 0x0278, all -> 0x0af8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x026f  */
    /* JADX WARNING: Removed duplicated region for block: B:534:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object processEvent(io.dcloud.common.DHInterface.IMgr.MgrType r17, int r18, java.lang.Object r19) {
        /*
            r16 = this;
            r9 = r16
            r1 = r18
            r2 = r19
            java.lang.String r3 = "event"
            java.lang.String r4 = "auto_pop "
            java.lang.String r5 = "quitModel"
            java.lang.String r6 = "auto_push "
            java.lang.String r7 = "query"
            r10 = 0
            boolean r8 = r16.checkMgrId(r17)     // Catch:{ all -> 0x0af8 }
            if (r8 != 0) goto L_0x0021
            io.dcloud.common.DHInterface.ICore r3 = r9.mCore     // Catch:{ all -> 0x0af8 }
            r4 = r17
            java.lang.Object r1 = r3.dispatchEvent(r4, r1, r2)     // Catch:{ all -> 0x0af8 }
            goto L_0x01d5
        L_0x0021:
            r8 = -1
            if (r1 == r8) goto L_0x0aec
            r11 = 52
            r12 = 1
            r13 = 0
            if (r1 == r11) goto L_0x0ad3
            java.lang.String r11 = "View_Visible_Path"
            java.lang.String r15 = "Animation_Path"
            r14 = 2
            r8 = 3
            switch(r1) {
                case 1: goto L_0x0296;
                case 2: goto L_0x063d;
                case 3: goto L_0x01f3;
                case 4: goto L_0x01dd;
                case 5: goto L_0x01d8;
                case 6: goto L_0x01b9;
                case 7: goto L_0x00cc;
                case 8: goto L_0x0090;
                case 9: goto L_0x006e;
                case 10: goto L_0x01d8;
                case 11: goto L_0x0064;
                case 12: goto L_0x005b;
                case 13: goto L_0x0052;
                case 14: goto L_0x0045;
                default: goto L_0x0033;
            }
        L_0x0033:
            switch(r1) {
                case 16: goto L_0x034d;
                case 17: goto L_0x02ea;
                case 18: goto L_0x02e4;
                default: goto L_0x0036;
            }
        L_0x0036:
            java.lang.String r8 = "Auto_Pop_Push_Path"
            java.lang.String r14 = "appid"
            switch(r1) {
                case 20: goto L_0x0676;
                case 21: goto L_0x063d;
                case 22: goto L_0x05f3;
                case 23: goto L_0x05ba;
                case 24: goto L_0x0581;
                case 25: goto L_0x056c;
                case 26: goto L_0x055f;
                case 27: goto L_0x045b;
                case 28: goto L_0x03e1;
                case 29: goto L_0x03d9;
                case 30: goto L_0x03d1;
                case 31: goto L_0x03b7;
                case 32: goto L_0x0365;
                default: goto L_0x003d;
            }
        L_0x003d:
            switch(r1) {
                case 41: goto L_0x0896;
                case 42: goto L_0x0889;
                case 43: goto L_0x087c;
                case 44: goto L_0x0867;
                case 45: goto L_0x0810;
                case 46: goto L_0x07fc;
                case 47: goto L_0x07e8;
                case 48: goto L_0x077b;
                case 49: goto L_0x0742;
                case 50: goto L_0x06fe;
                default: goto L_0x0040;
            }
        L_0x0040:
            switch(r1) {
                case 70: goto L_0x0acf;
                case 71: goto L_0x0ac8;
                case 72: goto L_0x0aaa;
                case 73: goto L_0x0a9b;
                case 74: goto L_0x0a90;
                case 75: goto L_0x0a5e;
                case 76: goto L_0x0a12;
                case 77: goto L_0x09f0;
                case 78: goto L_0x0995;
                case 79: goto L_0x0914;
                case 80: goto L_0x08f4;
                case 81: goto L_0x089b;
                default: goto L_0x0043;
            }
        L_0x0043:
            goto L_0x0aff
        L_0x0045:
            io.dcloud.common.core.ui.a r1 = r16.b()     // Catch:{ all -> 0x0af8 }
            java.lang.String r2 = java.lang.String.valueOf(r19)     // Catch:{ all -> 0x0af8 }
            r1.reload((java.lang.String) r2)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x0052:
            io.dcloud.common.core.ui.a r1 = r16.b()     // Catch:{ all -> 0x0af8 }
            r1.reload((boolean) r12)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x005b:
            io.dcloud.common.core.ui.a r1 = r16.b()     // Catch:{ all -> 0x0af8 }
            r1.reload((boolean) r13)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x0064:
            r1 = r2
            io.dcloud.common.core.ui.b r1 = (io.dcloud.common.core.ui.b) r1     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r1 = r1.p     // Catch:{ all -> 0x0af8 }
            r1.a((io.dcloud.common.core.ui.a) r1, (int) r8)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x006e:
            java.lang.String r1 = java.lang.String.valueOf(r19)     // Catch:{ all -> 0x0af8 }
            java.util.HashMap<java.lang.String, io.dcloud.common.core.ui.a> r2 = r9.a     // Catch:{ all -> 0x0af8 }
            java.lang.Object r1 = r2.get(r1)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r1 = (io.dcloud.common.core.ui.a) r1     // Catch:{ all -> 0x0af8 }
            if (r1 == 0) goto L_0x0aff
            java.util.Stack r2 = r1.e()     // Catch:{ all -> 0x0af8 }
            boolean r2 = r2.isEmpty()     // Catch:{ all -> 0x0af8 }
            if (r2 != 0) goto L_0x0aff
            java.util.Stack r1 = r1.e()     // Catch:{ all -> 0x0af8 }
            java.lang.Object r1 = r1.firstElement()     // Catch:{ all -> 0x0af8 }
            goto L_0x01d5
        L_0x0090:
            r1 = r2
            io.dcloud.common.core.ui.b r1 = (io.dcloud.common.core.ui.b) r1     // Catch:{ all -> 0x0af8 }
            if (r1 == 0) goto L_0x00c6
            io.dcloud.common.core.ui.a r2 = r1.p     // Catch:{ all -> 0x0af8 }
            java.util.Stack r2 = r2.e()     // Catch:{ all -> 0x0af8 }
            boolean r2 = r2.contains(r1)     // Catch:{ all -> 0x0af8 }
            if (r2 != 0) goto L_0x00c6
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0af8 }
            r2.<init>()     // Catch:{ all -> 0x0af8 }
            java.lang.String r3 = "setParent "
            r2.append(r3)     // Catch:{ all -> 0x0af8 }
            r2.append(r1)     // Catch:{ all -> 0x0af8 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r11, (java.lang.String) r2)     // Catch:{ all -> 0x0af8 }
            r1.i()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r2 = r1.p     // Catch:{ all -> 0x0af8 }
            r2.e((io.dcloud.common.core.ui.b) r1)     // Catch:{ all -> 0x0af8 }
            boolean r2 = r1.l     // Catch:{ all -> 0x0af8 }
            if (r2 == 0) goto L_0x00c6
            r1.resize()     // Catch:{ all -> 0x0af8 }
            r1.l = r13     // Catch:{ all -> 0x0af8 }
        L_0x00c6:
            r1.inStack = r12     // Catch:{ all -> 0x0af8 }
            r1.n = r13     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x00cc:
            r1 = r2
            java.lang.Object[] r1 = (java.lang.Object[]) r1     // Catch:{ all -> 0x0af8 }
            r2 = r1[r13]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r2 = (io.dcloud.common.core.ui.b) r2     // Catch:{ all -> 0x0af8 }
            r3 = r1[r12]     // Catch:{ all -> 0x0af8 }
            java.lang.Boolean r3 = (java.lang.Boolean) r3     // Catch:{ all -> 0x0af8 }
            boolean r3 = r3.booleanValue()     // Catch:{ all -> 0x0af8 }
            r4 = r1[r14]     // Catch:{ all -> 0x0af8 }
            java.lang.Boolean r4 = (java.lang.Boolean) r4     // Catch:{ all -> 0x0af8 }
            boolean r4 = r4.booleanValue()     // Catch:{ all -> 0x0af8 }
            r1 = r1[r8]     // Catch:{ all -> 0x0af8 }
            java.lang.Boolean r1 = (java.lang.Boolean) r1     // Catch:{ all -> 0x0af8 }
            boolean r1 = r1.booleanValue()     // Catch:{ all -> 0x0af8 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0af8 }
            r5.<init>()     // Catch:{ all -> 0x0af8 }
            java.lang.String r6 = "setStyle "
            r5.append(r6)     // Catch:{ all -> 0x0af8 }
            r5.append(r2)     // Catch:{ all -> 0x0af8 }
            if (r3 == 0) goto L_0x00fe
            java.lang.String r6 = "发生位置区域变化"
            goto L_0x010c
        L_0x00fe:
            if (r4 == 0) goto L_0x0104
            java.lang.String r6 = "zindex发生了变化"
            goto L_0x010c
        L_0x0104:
            if (r1 == 0) goto L_0x010a
            java.lang.String r6 = "设置透明度变化"
            goto L_0x010c
        L_0x010a:
            java.lang.String r6 = ""
        L_0x010c:
            r5.append(r6)     // Catch:{ all -> 0x0af8 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r15, (java.lang.String) r5)     // Catch:{ all -> 0x0af8 }
            r2.i = r1     // Catch:{ all -> 0x0af8 }
            r2.q()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r5 = r2.p     // Catch:{ all -> 0x0af8 }
            java.util.Stack r5 = r5.e()     // Catch:{ all -> 0x0af8 }
            boolean r5 = r5.contains(r2)     // Catch:{ all -> 0x0af8 }
            r5 = r5 ^ r12
            r2.l = r5     // Catch:{ all -> 0x0af8 }
            boolean r5 = r2.inStack     // Catch:{ all -> 0x0af8 }
            r6 = 20
            r7 = 28
            if (r5 == 0) goto L_0x0179
            if (r3 == 0) goto L_0x0179
            boolean r1 = r2.isChildOfFrameView     // Catch:{ all -> 0x0af8 }
            if (r1 != 0) goto L_0x0152
            io.dcloud.common.core.ui.a r1 = r2.p     // Catch:{ all -> 0x0af8 }
            r1.b((io.dcloud.common.core.ui.b) r2)     // Catch:{ all -> 0x0af8 }
            boolean r1 = r2.f()     // Catch:{ all -> 0x0af8 }
            if (r1 == 0) goto L_0x014a
            io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr     // Catch:{ all -> 0x0af8 }
            java.util.ArrayList<io.dcloud.common.core.ui.b> r3 = r2.g     // Catch:{ all -> 0x0af8 }
            r9.processEvent(r1, r7, r3)     // Catch:{ all -> 0x0af8 }
            r2.g = r10     // Catch:{ all -> 0x0af8 }
        L_0x014a:
            r2.t()     // Catch:{ all -> 0x0af8 }
            r2.startAnimator(r13)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x0152:
            r2.makeViewOptions_animate()     // Catch:{ all -> 0x0af8 }
            r2.p()     // Catch:{ all -> 0x0af8 }
            if (r3 == 0) goto L_0x0aff
            android.view.View r1 = r2.obtainMainView()     // Catch:{ all -> 0x0af8 }
            android.content.res.Resources r3 = io.dcloud.common.adapter.util.AndroidResources.mResources     // Catch:{ all -> 0x0af8 }
            android.content.res.Configuration r3 = r3.getConfiguration()     // Catch:{ all -> 0x0af8 }
            r1.dispatchConfigurationChanged(r3)     // Catch:{ all -> 0x0af8 }
            int r1 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x0af8 }
            if (r1 >= r6) goto L_0x0172
            android.view.View r1 = r2.obtainMainView()     // Catch:{ all -> 0x0af8 }
            r1.requestLayout()     // Catch:{ all -> 0x0af8 }
        L_0x0172:
            io.dcloud.common.core.ui.a r1 = r2.p     // Catch:{ all -> 0x0af8 }
            r1.resize()     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x0179:
            if (r4 != 0) goto L_0x0181
            if (r1 != 0) goto L_0x0181
            if (r3 == 0) goto L_0x0180
            goto L_0x0181
        L_0x0180:
            r12 = 0
        L_0x0181:
            r2.A = r12     // Catch:{ all -> 0x0af8 }
            boolean r1 = r2.isChildOfFrameView     // Catch:{ all -> 0x0af8 }
            if (r1 != 0) goto L_0x0197
            if (r12 == 0) goto L_0x0197
            io.dcloud.common.core.ui.a r1 = r2.p     // Catch:{ all -> 0x0af8 }
            r1.b((io.dcloud.common.core.ui.b) r2)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr     // Catch:{ all -> 0x0af8 }
            java.util.ArrayList<io.dcloud.common.core.ui.b> r4 = r2.g     // Catch:{ all -> 0x0af8 }
            r9.processEvent(r1, r7, r4)     // Catch:{ all -> 0x0af8 }
            r2.g = r10     // Catch:{ all -> 0x0af8 }
        L_0x0197:
            r2.makeViewOptions_animate()     // Catch:{ all -> 0x0af8 }
            r2.p()     // Catch:{ all -> 0x0af8 }
            if (r3 == 0) goto L_0x0aff
            android.view.View r1 = r2.obtainMainView()     // Catch:{ all -> 0x0af8 }
            android.content.res.Resources r3 = io.dcloud.common.adapter.util.AndroidResources.mResources     // Catch:{ all -> 0x0af8 }
            android.content.res.Configuration r3 = r3.getConfiguration()     // Catch:{ all -> 0x0af8 }
            r1.dispatchConfigurationChanged(r3)     // Catch:{ all -> 0x0af8 }
            int r1 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x0af8 }
            if (r1 >= r6) goto L_0x0aff
            android.view.View r1 = r2.obtainMainView()     // Catch:{ all -> 0x0af8 }
            r1.requestLayout()     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x01b9:
            boolean r1 = r2 instanceof java.lang.String     // Catch:{ all -> 0x0af8 }
            if (r1 == 0) goto L_0x01cd
            r1 = r2
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ all -> 0x0af8 }
            java.util.HashMap<java.lang.String, io.dcloud.common.core.ui.a> r2 = r9.a     // Catch:{ all -> 0x0af8 }
            java.lang.Object r1 = r2.get(r1)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r1 = (io.dcloud.common.core.ui.a) r1     // Catch:{ all -> 0x0af8 }
            java.util.ArrayList r1 = r1.d()     // Catch:{ all -> 0x0af8 }
            goto L_0x01d5
        L_0x01cd:
            io.dcloud.common.core.ui.a r1 = r16.b()     // Catch:{ all -> 0x0af8 }
            java.util.ArrayList r1 = r1.d()     // Catch:{ all -> 0x0af8 }
        L_0x01d5:
            r10 = r1
            goto L_0x0aff
        L_0x01d8:
            r9.a((int) r1, (java.lang.Object) r2)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x01dd:
            r1 = r2
            java.lang.Object[] r1 = (java.lang.Object[]) r1     // Catch:{ all -> 0x0af8 }
            r2 = r1[r13]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IApp r2 = (io.dcloud.common.DHInterface.IApp) r2     // Catch:{ all -> 0x0af8 }
            r1 = r1[r12]     // Catch:{ all -> 0x0af8 }
            java.lang.String r1 = java.lang.String.valueOf(r1)     // Catch:{ all -> 0x0af8 }
            boolean r1 = r9.a((io.dcloud.common.DHInterface.IApp) r2, (java.lang.String) r1)     // Catch:{ all -> 0x0af8 }
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ all -> 0x0af8 }
            goto L_0x01d5
        L_0x01f3:
            r1 = r2
            java.lang.Object[] r1 = (java.lang.Object[]) r1     // Catch:{ all -> 0x0af8 }
            r2 = r1[r13]     // Catch:{ all -> 0x0af8 }
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ all -> 0x0af8 }
            int r2 = java.lang.Integer.parseInt(r2)     // Catch:{ all -> 0x0af8 }
            r3 = r1[r12]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IApp r3 = (io.dcloud.common.DHInterface.IApp) r3     // Catch:{ all -> 0x0af8 }
            r4 = r1[r14]     // Catch:{ all -> 0x0af8 }
            r7 = r4
            java.lang.Object[] r7 = (java.lang.Object[]) r7     // Catch:{ all -> 0x0af8 }
            int r4 = r1.length     // Catch:{ all -> 0x0af8 }
            r5 = 4
            if (r4 <= r5) goto L_0x0221
            r4 = r1[r5]     // Catch:{ all -> 0x0af8 }
            boolean r4 = io.dcloud.common.util.PdrUtil.isEmpty(r4)     // Catch:{ all -> 0x0af8 }
            if (r4 != 0) goto L_0x0221
            r4 = r1[r5]     // Catch:{ all -> 0x0af8 }
            boolean r4 = r4 instanceof io.dcloud.common.DHInterface.IDCloudWebviewClientListener     // Catch:{ all -> 0x0af8 }
            if (r4 == 0) goto L_0x0221
            r4 = r1[r5]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IDCloudWebviewClientListener r4 = (io.dcloud.common.DHInterface.IDCloudWebviewClientListener) r4     // Catch:{ all -> 0x0af8 }
            r11 = r4
            goto L_0x0222
        L_0x0221:
            r11 = r10
        L_0x0222:
            int r4 = r1.length     // Catch:{ all -> 0x0af8 }
            r5 = 4
            if (r4 < r5) goto L_0x023f
            r4 = r1[r8]     // Catch:{ all -> 0x0af8 }
            boolean r4 = r4 instanceof io.dcloud.common.core.ui.a     // Catch:{ all -> 0x0af8 }
            if (r4 == 0) goto L_0x0232
            r4 = r1[r8]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r4 = (io.dcloud.common.core.ui.a) r4     // Catch:{ all -> 0x0af8 }
            r5 = r10
            goto L_0x0241
        L_0x0232:
            r4 = r1[r8]     // Catch:{ all -> 0x0af8 }
            boolean r4 = r4 instanceof io.dcloud.common.core.ui.b     // Catch:{ all -> 0x0af8 }
            if (r4 == 0) goto L_0x023f
            r4 = r1[r8]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r4 = (io.dcloud.common.core.ui.b) r4     // Catch:{ all -> 0x0af8 }
            r5 = r4
            r4 = r10
            goto L_0x0241
        L_0x023f:
            r4 = r10
            r5 = r4
        L_0x0241:
            int r6 = r1.length     // Catch:{ all -> 0x0af8 }
            r8 = 5
            if (r6 < r8) goto L_0x0251
            r6 = 4
            r8 = r1[r6]     // Catch:{ all -> 0x0af8 }
            boolean r8 = r8 instanceof io.dcloud.common.DHInterface.IEventCallback     // Catch:{ all -> 0x0af8 }
            if (r8 == 0) goto L_0x0251
            r6 = r1[r6]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IEventCallback r6 = (io.dcloud.common.DHInterface.IEventCallback) r6     // Catch:{ all -> 0x0af8 }
            goto L_0x0252
        L_0x0251:
            r6 = r10
        L_0x0252:
            if (r4 != 0) goto L_0x025a
            io.dcloud.common.DHInterface.IWebAppRootView r4 = r3.obtainWebAppRootView()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r4 = (io.dcloud.common.core.ui.a) r4     // Catch:{ all -> 0x0af8 }
        L_0x025a:
            if (r5 != 0) goto L_0x0264
            if (r4 != 0) goto L_0x0260
            r5 = r10
            goto L_0x0264
        L_0x0260:
            io.dcloud.common.core.ui.b r5 = r4.i()     // Catch:{ all -> 0x0af8 }
        L_0x0264:
            int r8 = r1.length     // Catch:{ all -> 0x0af8 }
            r13 = 6
            if (r8 != r13) goto L_0x026b
            r8 = 5
            r1 = r1[r8]     // Catch:{ all -> 0x0af8 }
        L_0x026b:
            if (r2 != r12) goto L_0x026f
            goto L_0x0aff
        L_0x026f:
            r1 = r16
            r8 = r11
            io.dcloud.common.core.ui.b r1 = r1.a((int) r2, (io.dcloud.common.DHInterface.IApp) r3, (io.dcloud.common.core.ui.a) r4, (io.dcloud.common.core.ui.b) r5, (io.dcloud.common.DHInterface.IEventCallback) r6, (java.lang.Object[]) r7, (io.dcloud.common.DHInterface.IDCloudWebviewClientListener) r8)     // Catch:{ Exception -> 0x0278 }
            goto L_0x01d5
        L_0x0278:
            r0 = move-exception
            r1 = r0
            r1.printStackTrace()     // Catch:{ all -> 0x0af8 }
            java.lang.String r2 = "winmgr"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0af8 }
            r3.<init>()     // Catch:{ all -> 0x0af8 }
            java.lang.String r4 = "Exception msg="
            r3.append(r4)     // Catch:{ all -> 0x0af8 }
            r3.append(r1)     // Catch:{ all -> 0x0af8 }
            java.lang.String r1 = r3.toString()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.adapter.util.Logger.e(r2, r1)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x0296:
            r1 = r2
            java.lang.Object[] r1 = (java.lang.Object[]) r1     // Catch:{ all -> 0x0af8 }
            r2 = r1[r13]     // Catch:{ all -> 0x0af8 }
            boolean r2 = r2 instanceof io.dcloud.common.core.ui.b     // Catch:{ all -> 0x0af8 }
            if (r2 == 0) goto L_0x0aff
            r2 = r1[r13]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r2 = (io.dcloud.common.core.ui.b) r2     // Catch:{ all -> 0x0af8 }
            int r3 = r1.length     // Catch:{ all -> 0x0af8 }
            if (r3 >= r8) goto L_0x02a7
            goto L_0x02af
        L_0x02a7:
            r3 = r1[r14]     // Catch:{ all -> 0x0af8 }
            java.lang.Boolean r3 = (java.lang.Boolean) r3     // Catch:{ all -> 0x0af8 }
            boolean r12 = r3.booleanValue()     // Catch:{ all -> 0x0af8 }
        L_0x02af:
            if (r12 == 0) goto L_0x02b7
            long r3 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0af8 }
            r2.lastShowTime = r3     // Catch:{ all -> 0x0af8 }
        L_0x02b7:
            int r3 = io.dcloud.common.core.ui.b.c     // Catch:{ all -> 0x0af8 }
            r2.a((int) r3)     // Catch:{ all -> 0x0af8 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0af8 }
            r3.<init>()     // Catch:{ all -> 0x0af8 }
            java.lang.String r4 = "showWindow"
            r3.append(r4)     // Catch:{ all -> 0x0af8 }
            r3.append(r2)     // Catch:{ all -> 0x0af8 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r15, (java.lang.String) r3)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r3 = r2.p     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IWebview r4 = r2.obtainWebView()     // Catch:{ all -> 0x0af8 }
            android.view.ViewGroup r4 = r4.obtainWindowView()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.l$d r5 = new io.dcloud.common.core.ui.l$d     // Catch:{ all -> 0x0af8 }
            r5.<init>(r2, r1)     // Catch:{ all -> 0x0af8 }
            r3.a((android.view.View) r4, (io.dcloud.common.DHInterface.ICallBack) r5)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x02e4:
            io.dcloud.common.core.ui.b r1 = r16.a()     // Catch:{ all -> 0x0af8 }
            goto L_0x01d5
        L_0x02ea:
            r1 = r2
            java.lang.Object[] r1 = (java.lang.Object[]) r1     // Catch:{ all -> 0x0af8 }
            r2 = r1[r13]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IApp r2 = (io.dcloud.common.DHInterface.IApp) r2     // Catch:{ all -> 0x0af8 }
            r3 = r1[r12]     // Catch:{ all -> 0x0af8 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ all -> 0x0af8 }
            r4 = r1[r14]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IWebviewStateListener r4 = (io.dcloud.common.DHInterface.IWebviewStateListener) r4     // Catch:{ all -> 0x0af8 }
            int r5 = r1.length     // Catch:{ all -> 0x0af8 }
            if (r5 <= r8) goto L_0x0309
            r5 = r1[r8]     // Catch:{ all -> 0x0af8 }
            boolean r5 = io.dcloud.common.util.PdrUtil.isEmpty(r5)     // Catch:{ all -> 0x0af8 }
            if (r5 != 0) goto L_0x0309
            r1 = r1[r8]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IDCloudWebviewClientListener r1 = (io.dcloud.common.DHInterface.IDCloudWebviewClientListener) r1     // Catch:{ all -> 0x0af8 }
            goto L_0x030a
        L_0x0309:
            r1 = r10
        L_0x030a:
            java.lang.String r5 = r2.obtainAppId()     // Catch:{ all -> 0x0af8 }
            r9.a((io.dcloud.common.DHInterface.IApp) r2, (java.lang.String) r5)     // Catch:{ all -> 0x0af8 }
            java.util.HashMap<java.lang.String, io.dcloud.common.core.ui.a> r5 = r9.a     // Catch:{ all -> 0x0af8 }
            java.lang.String r6 = r2.obtainAppId()     // Catch:{ all -> 0x0af8 }
            java.lang.Object r5 = r5.get(r6)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r5 = (io.dcloud.common.core.ui.a) r5     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IMgr$MgrType r6 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr     // Catch:{ all -> 0x0af8 }
            r7 = 5
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ all -> 0x0af8 }
            java.lang.Integer r11 = java.lang.Integer.valueOf(r14)     // Catch:{ all -> 0x0af8 }
            r7[r13] = r11     // Catch:{ all -> 0x0af8 }
            r7[r12] = r2     // Catch:{ all -> 0x0af8 }
            java.lang.Object[] r2 = new java.lang.Object[r12]     // Catch:{ all -> 0x0af8 }
            r2[r13] = r3     // Catch:{ all -> 0x0af8 }
            r7[r14] = r2     // Catch:{ all -> 0x0af8 }
            r7[r8] = r5     // Catch:{ all -> 0x0af8 }
            r2 = 4
            r7[r2] = r1     // Catch:{ all -> 0x0af8 }
            java.lang.Object r1 = r9.processEvent(r6, r8, r7)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IFrameView r1 = (io.dcloud.common.DHInterface.IFrameView) r1     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IWebview r2 = r1.obtainWebView()     // Catch:{ all -> 0x0af8 }
            if (r4 == 0) goto L_0x0348
            r2.addStateListener(r4)     // Catch:{ all -> 0x0af8 }
            r5 = -1
            r4.onCallBack(r5, r2)     // Catch:{ all -> 0x0af8 }
        L_0x0348:
            r2.loadUrl(r3)     // Catch:{ all -> 0x0af8 }
            goto L_0x01d5
        L_0x034d:
            r1 = r2
            java.lang.Object[] r1 = (java.lang.Object[]) r1     // Catch:{ all -> 0x0af8 }
            r2 = r1[r13]     // Catch:{ all -> 0x0af8 }
            android.view.ViewGroup r2 = (android.view.ViewGroup) r2     // Catch:{ all -> 0x0af8 }
            r3 = r1[r12]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IApp r3 = (io.dcloud.common.DHInterface.IApp) r3     // Catch:{ all -> 0x0af8 }
            r4 = r1[r14]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IWebview r4 = (io.dcloud.common.DHInterface.IWebview) r4     // Catch:{ all -> 0x0af8 }
            r1 = r1[r8]     // Catch:{ all -> 0x0af8 }
            android.view.ViewGroup$LayoutParams r1 = (android.view.ViewGroup.LayoutParams) r1     // Catch:{ all -> 0x0af8 }
            r9.a((android.view.ViewGroup) r2, (io.dcloud.common.DHInterface.IApp) r3, (io.dcloud.common.DHInterface.IWebview) r4, (android.view.ViewGroup.LayoutParams) r1)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x0365:
            boolean r1 = r2 instanceof io.dcloud.common.DHInterface.IApp     // Catch:{ all -> 0x0af8 }
            if (r1 == 0) goto L_0x0375
            r1 = r2
            io.dcloud.common.DHInterface.IApp r1 = (io.dcloud.common.DHInterface.IApp) r1     // Catch:{ all -> 0x0af8 }
            android.app.Activity r2 = r1.getActivity()     // Catch:{ all -> 0x0af8 }
            java.lang.String r1 = r1.obtainAppId()     // Catch:{ all -> 0x0af8 }
            goto L_0x0387
        L_0x0375:
            boolean r1 = r2 instanceof java.lang.Object[]     // Catch:{ all -> 0x0af8 }
            if (r1 == 0) goto L_0x0385
            r1 = r2
            java.lang.Object[] r1 = (java.lang.Object[]) r1     // Catch:{ all -> 0x0af8 }
            r2 = r1[r13]     // Catch:{ all -> 0x0af8 }
            android.app.Activity r2 = (android.app.Activity) r2     // Catch:{ all -> 0x0af8 }
            r1 = r1[r12]     // Catch:{ all -> 0x0af8 }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ all -> 0x0af8 }
            goto L_0x0387
        L_0x0385:
            r1 = r10
            r2 = r1
        L_0x0387:
            java.util.HashMap<java.lang.String, io.dcloud.common.core.ui.a> r3 = r9.a     // Catch:{ all -> 0x0af8 }
            java.util.Set r3 = r3.keySet()     // Catch:{ all -> 0x0af8 }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ all -> 0x0af8 }
            java.lang.Boolean r10 = java.lang.Boolean.FALSE     // Catch:{ all -> 0x0af8 }
        L_0x0393:
            boolean r4 = r3.hasNext()     // Catch:{ all -> 0x0af8 }
            if (r4 == 0) goto L_0x0aff
            java.lang.Object r4 = r3.next()     // Catch:{ all -> 0x0af8 }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ all -> 0x0af8 }
            boolean r5 = io.dcloud.common.util.PdrUtil.isEquals(r4, r1)     // Catch:{ all -> 0x0af8 }
            if (r5 != 0) goto L_0x0393
            java.util.HashMap<java.lang.String, io.dcloud.common.core.ui.a> r5 = r9.a     // Catch:{ all -> 0x0af8 }
            java.lang.Object r4 = r5.get(r4)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r4 = (io.dcloud.common.core.ui.a) r4     // Catch:{ all -> 0x0af8 }
            android.app.Activity r4 = r4.getActivity()     // Catch:{ all -> 0x0af8 }
            if (r2 != r4) goto L_0x0393
            java.lang.Boolean r10 = java.lang.Boolean.TRUE     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x03b7:
            r1 = r2
            io.dcloud.common.DHInterface.IFrameView r1 = (io.dcloud.common.DHInterface.IFrameView) r1     // Catch:{ all -> 0x0af8 }
            android.view.View r2 = r1.obtainMainView()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IApp r1 = r1.obtainApp()     // Catch:{ all -> 0x0af8 }
            android.app.Activity r1 = r1.getActivity()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IActivityHandler r1 = io.src.dcloud.adapter.DCloudAdapterUtil.getIActivityHandler(r1)     // Catch:{ all -> 0x0af8 }
            if (r1 == 0) goto L_0x0aff
            r1.setWebViewIntoPreloadView(r2)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x03d1:
            r1 = r2
            io.dcloud.common.core.ui.b r1 = (io.dcloud.common.core.ui.b) r1     // Catch:{ all -> 0x0af8 }
            r9.b((io.dcloud.common.core.ui.b) r1)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x03d9:
            r1 = r2
            io.dcloud.common.core.ui.b r1 = (io.dcloud.common.core.ui.b) r1     // Catch:{ all -> 0x0af8 }
            r9.d(r1)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x03e1:
            if (r2 == 0) goto L_0x0aff
            r1 = r2
            java.util.ArrayList r1 = (java.util.ArrayList) r1     // Catch:{ all -> 0x0af8 }
            int r2 = r1.size()     // Catch:{ all -> 0x0af8 }
            if (r2 <= 0) goto L_0x0aff
            int r2 = r1.size()     // Catch:{ all -> 0x0af8 }
            int r2 = r2 - r12
            r4 = r10
            r5 = r4
            r3 = 0
        L_0x03f4:
            if (r2 < 0) goto L_0x044c
            java.lang.Object r7 = r1.get(r2)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r7 = (io.dcloud.common.core.ui.b) r7     // Catch:{ all -> 0x0af8 }
            if (r7 == 0) goto L_0x0449
            if (r4 != 0) goto L_0x0402
            io.dcloud.common.core.ui.a r4 = r7.p     // Catch:{ all -> 0x0af8 }
        L_0x0402:
            if (r5 != 0) goto L_0x0408
            io.dcloud.common.DHInterface.IApp r5 = r7.obtainApp()     // Catch:{ all -> 0x0af8 }
        L_0x0408:
            java.util.Stack r11 = r4.e()     // Catch:{ all -> 0x0af8 }
            boolean r11 = r11.contains(r7)     // Catch:{ all -> 0x0af8 }
            if (r11 != 0) goto L_0x0443
            r7.onPushToStack(r12)     // Catch:{ all -> 0x0af8 }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x0af8 }
            r11.<init>()     // Catch:{ all -> 0x0af8 }
            r11.append(r6)     // Catch:{ all -> 0x0af8 }
            r11.append(r7)     // Catch:{ all -> 0x0af8 }
            java.lang.String r11 = r11.toString()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r8, (java.lang.String) r11)     // Catch:{ all -> 0x0af8 }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x0af8 }
            r11.<init>()     // Catch:{ all -> 0x0af8 }
            r11.append(r6)     // Catch:{ all -> 0x0af8 }
            r11.append(r7)     // Catch:{ all -> 0x0af8 }
            java.lang.String r11 = r11.toString()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r15, (java.lang.String) r11)     // Catch:{ all -> 0x0af8 }
            boolean r11 = r7.l     // Catch:{ all -> 0x0af8 }
            r3 = r3 | r11
            r7.l = r13     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r11 = r7.p     // Catch:{ all -> 0x0af8 }
            r11.e((io.dcloud.common.core.ui.b) r7)     // Catch:{ all -> 0x0af8 }
        L_0x0443:
            r7.n = r12     // Catch:{ all -> 0x0af8 }
            r7.m = r13     // Catch:{ all -> 0x0af8 }
            r7.inStack = r12     // Catch:{ all -> 0x0af8 }
        L_0x0449:
            int r2 = r2 + -1
            goto L_0x03f4
        L_0x044c:
            if (r3 == 0) goto L_0x0aff
            java.lang.Object r1 = r1.get(r13)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r1 = (io.dcloud.common.core.ui.b) r1     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r1 = r1.p     // Catch:{ all -> 0x0af8 }
            r1.resize()     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x045b:
            r1 = r2
            java.util.ArrayList r1 = (java.util.ArrayList) r1     // Catch:{ all -> 0x0af8 }
            if (r1 == 0) goto L_0x0aff
            int r2 = r1.size()     // Catch:{ all -> 0x0af8 }
            int r2 = r2 - r12
            r3 = r10
            r5 = r3
        L_0x0467:
            if (r2 < 0) goto L_0x04f6
            java.lang.Object r6 = r1.get(r2)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r6 = (io.dcloud.common.core.ui.b) r6     // Catch:{ all -> 0x0af8 }
            if (r6 == 0) goto L_0x04f2
            if (r3 != 0) goto L_0x0475
            io.dcloud.common.core.ui.a r3 = r6.p     // Catch:{ all -> 0x0af8 }
        L_0x0475:
            if (r5 != 0) goto L_0x047b
            io.dcloud.common.DHInterface.IApp r5 = r6.obtainApp()     // Catch:{ all -> 0x0af8 }
        L_0x047b:
            io.dcloud.common.core.ui.a r7 = r6.p     // Catch:{ all -> 0x0af8 }
            java.util.Stack r7 = r7.e()     // Catch:{ all -> 0x0af8 }
            boolean r7 = r7.contains(r6)     // Catch:{ all -> 0x0af8 }
            if (r7 == 0) goto L_0x04f2
            boolean r7 = r6.checkITypeofAble()     // Catch:{ all -> 0x0af8 }
            if (r7 != 0) goto L_0x0491
            boolean r7 = r6.f     // Catch:{ all -> 0x0af8 }
            if (r7 != 0) goto L_0x04f2
        L_0x0491:
            io.dcloud.common.DHInterface.IWebview r7 = r6.obtainWebView()     // Catch:{ all -> 0x0af8 }
            boolean r7 = r7.isUniService()     // Catch:{ all -> 0x0af8 }
            if (r7 == 0) goto L_0x049c
            goto L_0x04f2
        L_0x049c:
            io.dcloud.common.DHInterface.IApp r7 = r6.obtainApp()     // Catch:{ all -> 0x0af8 }
            boolean r7 = io.dcloud.common.util.BaseInfo.isUniAppAppid(r7)     // Catch:{ all -> 0x0af8 }
            if (r7 == 0) goto L_0x04b3
            io.dcloud.common.DHInterface.IWebview r7 = r6.obtainWebView()     // Catch:{ all -> 0x0af8 }
            if (r7 == 0) goto L_0x04b3
            io.dcloud.common.DHInterface.IWebview r7 = r6.obtainWebView()     // Catch:{ all -> 0x0af8 }
            r7.setIWebViewFocusable(r13)     // Catch:{ all -> 0x0af8 }
        L_0x04b3:
            boolean r7 = r6.h()     // Catch:{ all -> 0x0af8 }
            r6.onPopFromStack(r7)     // Catch:{ all -> 0x0af8 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x0af8 }
            r7.<init>()     // Catch:{ all -> 0x0af8 }
            r7.append(r4)     // Catch:{ all -> 0x0af8 }
            r7.append(r6)     // Catch:{ all -> 0x0af8 }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r8, (java.lang.String) r7)     // Catch:{ all -> 0x0af8 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x0af8 }
            r7.<init>()     // Catch:{ all -> 0x0af8 }
            r7.append(r4)     // Catch:{ all -> 0x0af8 }
            r7.append(r6)     // Catch:{ all -> 0x0af8 }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r15, (java.lang.String) r7)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r7 = r6.p     // Catch:{ all -> 0x0af8 }
            r7.removeFrameItem(r6)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r7 = r6.p     // Catch:{ all -> 0x0af8 }
            java.util.Stack r7 = r7.e()     // Catch:{ all -> 0x0af8 }
            r7.remove(r6)     // Catch:{ all -> 0x0af8 }
            r6.n = r13     // Catch:{ all -> 0x0af8 }
            r6.m = r12     // Catch:{ all -> 0x0af8 }
            r6.inStack = r13     // Catch:{ all -> 0x0af8 }
        L_0x04f2:
            int r2 = r2 + -1
            goto L_0x0467
        L_0x04f6:
            if (r3 == 0) goto L_0x0aff
            if (r5 == 0) goto L_0x0aff
            boolean r1 = io.dcloud.common.util.BaseInfo.isUniAppAppid(r5)     // Catch:{ all -> 0x0af8 }
            if (r1 == 0) goto L_0x0aff
            java.util.Stack r1 = r3.e()     // Catch:{ all -> 0x0af8 }
            int r1 = r1.size()     // Catch:{ all -> 0x0af8 }
            int r2 = r1 + -1
            r4 = 1
        L_0x050b:
            if (r2 < 0) goto L_0x0aff
            java.util.Stack r5 = r3.e()     // Catch:{ all -> 0x0af8 }
            java.lang.Object r5 = r5.get(r2)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r5 = (io.dcloud.common.core.ui.b) r5     // Catch:{ all -> 0x0af8 }
            if (r5 == 0) goto L_0x055c
            io.dcloud.common.DHInterface.IWebview r6 = r5.obtainWebView()     // Catch:{ all -> 0x0af8 }
            if (r6 == 0) goto L_0x055c
            android.view.View r6 = r5.obtainMainView()     // Catch:{ all -> 0x0af8 }
            if (r6 == 0) goto L_0x055c
            int r6 = r1 - r4
            if (r2 != r6) goto L_0x0555
            android.view.View r6 = r5.obtainMainView()     // Catch:{ all -> 0x0af8 }
            int r6 = r6.getVisibility()     // Catch:{ all -> 0x0af8 }
            if (r6 != 0) goto L_0x054b
            int r6 = r5.d()     // Catch:{ all -> 0x0af8 }
            int r7 = io.dcloud.common.core.ui.b.d     // Catch:{ all -> 0x0af8 }
            if (r6 == r7) goto L_0x054b
            int r6 = r5.d()     // Catch:{ all -> 0x0af8 }
            int r7 = io.dcloud.common.core.ui.b.e     // Catch:{ all -> 0x0af8 }
            if (r6 == r7) goto L_0x054b
            io.dcloud.common.DHInterface.IWebview r5 = r5.obtainWebView()     // Catch:{ all -> 0x0af8 }
            r5.setIWebViewFocusable(r12)     // Catch:{ all -> 0x0af8 }
            goto L_0x055c
        L_0x054b:
            io.dcloud.common.DHInterface.IWebview r5 = r5.obtainWebView()     // Catch:{ all -> 0x0af8 }
            r5.setIWebViewFocusable(r13)     // Catch:{ all -> 0x0af8 }
            int r4 = r4 + 1
            goto L_0x055c
        L_0x0555:
            io.dcloud.common.DHInterface.IWebview r5 = r5.obtainWebView()     // Catch:{ all -> 0x0af8 }
            r5.setIWebViewFocusable(r13)     // Catch:{ all -> 0x0af8 }
        L_0x055c:
            int r2 = r2 + -1
            goto L_0x050b
        L_0x055f:
            r1 = r2
            io.dcloud.common.core.ui.a r1 = (io.dcloud.common.core.ui.a) r1     // Catch:{ all -> 0x0af8 }
            if (r1 == 0) goto L_0x0aff
            r1.l()     // Catch:{ all -> 0x0af8 }
            r1.m()     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x056c:
            r1 = r2
            io.dcloud.common.DHInterface.IApp r1 = (io.dcloud.common.DHInterface.IApp) r1     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IWebAppRootView r1 = r1.obtainWebAppRootView()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r1 = (io.dcloud.common.core.ui.a) r1     // Catch:{ all -> 0x0af8 }
            r1.dispose()     // Catch:{ all -> 0x0af8 }
            java.util.HashMap<java.lang.String, io.dcloud.common.core.ui.a> r2 = r9.a     // Catch:{ all -> 0x0af8 }
            java.lang.String r1 = r1.i     // Catch:{ all -> 0x0af8 }
            r2.remove(r1)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x0581:
            boolean r1 = r2 instanceof io.dcloud.common.core.ui.b     // Catch:{ all -> 0x0af8 }
            if (r1 == 0) goto L_0x0589
            r1 = r2
            io.dcloud.common.core.ui.b r1 = (io.dcloud.common.core.ui.b) r1     // Catch:{ all -> 0x0af8 }
            goto L_0x058a
        L_0x0589:
            r1 = r10
        L_0x058a:
            if (r1 != 0) goto L_0x058d
            return r10
        L_0x058d:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0af8 }
            r2.<init>()     // Catch:{ all -> 0x0af8 }
            java.lang.String r3 = "hideShowWindow"
            r2.append(r3)     // Catch:{ all -> 0x0af8 }
            r2.append(r1)     // Catch:{ all -> 0x0af8 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r15, (java.lang.String) r2)     // Catch:{ all -> 0x0af8 }
            int r2 = io.dcloud.common.core.ui.b.c     // Catch:{ all -> 0x0af8 }
            r1.a((int) r2)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r2 = r1.p     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IWebview r3 = r1.obtainWebView()     // Catch:{ all -> 0x0af8 }
            android.view.ViewGroup r3 = r3.obtainWindowView()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.l$e r4 = new io.dcloud.common.core.ui.l$e     // Catch:{ all -> 0x0af8 }
            r4.<init>(r1)     // Catch:{ all -> 0x0af8 }
            r2.a((android.view.View) r3, (io.dcloud.common.DHInterface.ICallBack) r4)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x05ba:
            boolean r1 = r2 instanceof io.dcloud.common.core.ui.b     // Catch:{ all -> 0x0af8 }
            if (r1 == 0) goto L_0x05c2
            r1 = r2
            io.dcloud.common.core.ui.b r1 = (io.dcloud.common.core.ui.b) r1     // Catch:{ all -> 0x0af8 }
            goto L_0x05c3
        L_0x05c2:
            r1 = r10
        L_0x05c3:
            if (r1 != 0) goto L_0x05c6
            return r10
        L_0x05c6:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0af8 }
            r2.<init>()     // Catch:{ all -> 0x0af8 }
            java.lang.String r3 = "hideWindow"
            r2.append(r3)     // Catch:{ all -> 0x0af8 }
            r2.append(r1)     // Catch:{ all -> 0x0af8 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r15, (java.lang.String) r2)     // Catch:{ all -> 0x0af8 }
            int r2 = io.dcloud.common.core.ui.b.d     // Catch:{ all -> 0x0af8 }
            r1.a((int) r2)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r2 = r1.p     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IWebview r3 = r1.obtainWebView()     // Catch:{ all -> 0x0af8 }
            android.view.ViewGroup r3 = r3.obtainWindowView()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.l$f r4 = new io.dcloud.common.core.ui.l$f     // Catch:{ all -> 0x0af8 }
            r4.<init>(r1)     // Catch:{ all -> 0x0af8 }
            r2.a((android.view.View) r3, (io.dcloud.common.DHInterface.ICallBack) r4)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x05f3:
            if (r2 == 0) goto L_0x0aff
            r1 = r2
            io.dcloud.common.core.ui.b r1 = (io.dcloud.common.core.ui.b) r1     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r2 = r1.p     // Catch:{ all -> 0x0af8 }
            java.util.Stack r2 = r2.e()     // Catch:{ all -> 0x0af8 }
            boolean r2 = r2.contains(r1)     // Catch:{ all -> 0x0af8 }
            if (r2 == 0) goto L_0x0637
            io.dcloud.common.DHInterface.IWebview r2 = r1.obtainWebView()     // Catch:{ all -> 0x0af8 }
            boolean r2 = r2.isUniService()     // Catch:{ all -> 0x0af8 }
            if (r2 != 0) goto L_0x0637
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0af8 }
            r2.<init>()     // Catch:{ all -> 0x0af8 }
            java.lang.String r3 = "setUnParent "
            r2.append(r3)     // Catch:{ all -> 0x0af8 }
            r2.append(r1)     // Catch:{ all -> 0x0af8 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r11, (java.lang.String) r2)     // Catch:{ all -> 0x0af8 }
            boolean r2 = r1.h()     // Catch:{ all -> 0x0af8 }
            r1.onPopFromStack(r2)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r2 = r1.p     // Catch:{ all -> 0x0af8 }
            r2.removeFrameItem(r1)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r2 = r1.p     // Catch:{ all -> 0x0af8 }
            java.util.Stack r2 = r2.e()     // Catch:{ all -> 0x0af8 }
            r2.remove(r1)     // Catch:{ all -> 0x0af8 }
        L_0x0637:
            r1.inStack = r13     // Catch:{ all -> 0x0af8 }
            r1.m = r13     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x063d:
            boolean r3 = r2 instanceof io.dcloud.common.core.ui.b     // Catch:{ all -> 0x0af8 }
            if (r3 == 0) goto L_0x0644
            io.dcloud.common.core.ui.b r2 = (io.dcloud.common.core.ui.b) r2     // Catch:{ all -> 0x0af8 }
            goto L_0x0645
        L_0x0644:
            r2 = r10
        L_0x0645:
            if (r2 != 0) goto L_0x0648
            return r10
        L_0x0648:
            int r3 = io.dcloud.common.core.ui.b.e     // Catch:{ all -> 0x0af8 }
            r2.a((int) r3)     // Catch:{ all -> 0x0af8 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0af8 }
            r3.<init>()     // Catch:{ all -> 0x0af8 }
            java.lang.String r4 = "closeWindow"
            r3.append(r4)     // Catch:{ all -> 0x0af8 }
            r3.append(r2)     // Catch:{ all -> 0x0af8 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r15, (java.lang.String) r3)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r3 = r2.p     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IWebview r4 = r2.obtainWebView()     // Catch:{ all -> 0x0af8 }
            android.view.ViewGroup r4 = r4.obtainWindowView()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.l$g r5 = new io.dcloud.common.core.ui.l$g     // Catch:{ all -> 0x0af8 }
            r5.<init>(r2, r1)     // Catch:{ all -> 0x0af8 }
            java.lang.Object r1 = r3.a((android.view.View) r4, (io.dcloud.common.DHInterface.ICallBack) r5)     // Catch:{ all -> 0x0af8 }
            goto L_0x01d5
        L_0x0676:
            boolean r1 = io.dcloud.feature.internal.sdk.SDK.isEnableBackground     // Catch:{ all -> 0x0af8 }
            r1 = r1 ^ r12
            boolean r3 = r2 instanceof io.dcloud.common.DHInterface.IApp     // Catch:{ all -> 0x0af8 }
            if (r3 == 0) goto L_0x0687
            r3 = r2
            io.dcloud.common.DHInterface.IApp r3 = (io.dcloud.common.DHInterface.IApp) r3     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IWebAppRootView r3 = r3.obtainWebAppRootView()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r3 = (io.dcloud.common.core.ui.a) r3     // Catch:{ all -> 0x0af8 }
            goto L_0x06d9
        L_0x0687:
            boolean r3 = r2 instanceof java.lang.String     // Catch:{ all -> 0x0af8 }
            if (r3 == 0) goto L_0x0697
            java.util.HashMap<java.lang.String, io.dcloud.common.core.ui.a> r3 = r9.a     // Catch:{ all -> 0x0af8 }
            r4 = r2
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ all -> 0x0af8 }
            java.lang.Object r3 = r3.get(r4)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r3 = (io.dcloud.common.core.ui.a) r3     // Catch:{ all -> 0x0af8 }
            goto L_0x06d9
        L_0x0697:
            boolean r3 = r2 instanceof java.util.Map     // Catch:{ all -> 0x0af8 }
            if (r3 == 0) goto L_0x06d8
            r1 = r2
            java.util.Map r1 = (java.util.Map) r1     // Catch:{ all -> 0x0af8 }
            java.lang.Object r3 = r1.get(r14)     // Catch:{ all -> 0x0af8 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ all -> 0x0af8 }
            boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch:{ all -> 0x0af8 }
            if (r4 == 0) goto L_0x06ab
            return r10
        L_0x06ab:
            java.util.HashMap<java.lang.String, io.dcloud.common.core.ui.a> r4 = r9.a     // Catch:{ all -> 0x0af8 }
            java.lang.Object r3 = r4.get(r3)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r3 = (io.dcloud.common.core.ui.a) r3     // Catch:{ all -> 0x0af8 }
            java.lang.String r4 = "isStopApp"
            java.lang.Object r4 = r1.get(r4)     // Catch:{ all -> 0x0af8 }
            java.lang.Boolean r4 = (java.lang.Boolean) r4     // Catch:{ all -> 0x0af8 }
            boolean r4 = r4.booleanValue()     // Catch:{ all -> 0x0af8 }
            boolean r6 = r1.containsKey(r5)     // Catch:{ all -> 0x0af8 }
            if (r6 == 0) goto L_0x06d6
            if (r4 == 0) goto L_0x06d6
            java.lang.Object r1 = r1.get(r5)     // Catch:{ all -> 0x0af8 }
            java.lang.Integer r1 = (java.lang.Integer) r1     // Catch:{ all -> 0x0af8 }
            int r1 = r1.intValue()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IApp r5 = r3.j     // Catch:{ all -> 0x0af8 }
            r5.setQuitModel(r1)     // Catch:{ all -> 0x0af8 }
        L_0x06d6:
            r1 = r4
            goto L_0x06d9
        L_0x06d8:
            r3 = r10
        L_0x06d9:
            boolean r4 = io.dcloud.feature.internal.sdk.SDK.isUniMPSDK()     // Catch:{ all -> 0x0af8 }
            if (r4 == 0) goto L_0x06eb
            if (r1 != 0) goto L_0x06eb
            if (r3 == 0) goto L_0x06ea
            android.app.Activity r1 = r3.getActivity()     // Catch:{ all -> 0x0af8 }
            r1.moveTaskToBack(r12)     // Catch:{ all -> 0x0af8 }
        L_0x06ea:
            return r10
        L_0x06eb:
            if (r3 == 0) goto L_0x06f5
            io.dcloud.common.DHInterface.IApp r1 = r3.j     // Catch:{ all -> 0x0af8 }
            r4 = 2
            r1.setStatus(r4)     // Catch:{ all -> 0x0af8 }
            r3.h = r13     // Catch:{ all -> 0x0af8 }
        L_0x06f5:
            io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.AppMgr     // Catch:{ all -> 0x0af8 }
            r3 = 10
            r9.processEvent(r1, r3, r2)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x06fe:
            boolean r1 = r2 instanceof io.dcloud.common.DHInterface.IApp     // Catch:{ all -> 0x0af8 }
            if (r1 == 0) goto L_0x0aff
            r1 = r2
            io.dcloud.common.DHInterface.IApp r1 = (io.dcloud.common.DHInterface.IApp) r1     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IWebAppRootView r1 = r1.obtainWebAppRootView()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r1 = (io.dcloud.common.core.ui.a) r1     // Catch:{ all -> 0x0af8 }
            if (r1 == 0) goto L_0x0aff
            io.dcloud.common.core.ui.b r3 = r1.f()     // Catch:{ all -> 0x0af8 }
            if (r3 == 0) goto L_0x072a
            io.dcloud.common.DHInterface.IApp r2 = (io.dcloud.common.DHInterface.IApp) r2     // Catch:{ all -> 0x0af8 }
            boolean r2 = r9.a((io.dcloud.common.DHInterface.IApp) r2)     // Catch:{ all -> 0x0af8 }
            if (r2 == 0) goto L_0x072a
            io.dcloud.common.DHInterface.IWebview r2 = r3.obtainWebView()     // Catch:{ all -> 0x0af8 }
            if (r2 == 0) goto L_0x072a
            io.dcloud.common.DHInterface.IWebview r2 = r3.obtainWebView()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.adapter.ui.AdaWebview r2 = (io.dcloud.common.adapter.ui.AdaWebview) r2     // Catch:{ all -> 0x0af8 }
            r2.checkPreLoadJsContent()     // Catch:{ all -> 0x0af8 }
        L_0x072a:
            r2 = 5
            io.dcloud.common.core.ui.b r1 = r1.a((int) r2)     // Catch:{ all -> 0x0af8 }
            if (r1 == 0) goto L_0x0aff
            io.dcloud.common.DHInterface.IWebview r2 = r1.obtainWebView()     // Catch:{ all -> 0x0af8 }
            if (r2 == 0) goto L_0x0aff
            io.dcloud.common.DHInterface.IWebview r1 = r1.obtainWebView()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.adapter.ui.AdaWebview r1 = (io.dcloud.common.adapter.ui.AdaWebview) r1     // Catch:{ all -> 0x0af8 }
            r1.checkPreLoadJsContent()     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x0742:
            boolean r1 = r2 instanceof io.dcloud.common.DHInterface.IApp     // Catch:{ all -> 0x0af8 }
            if (r1 == 0) goto L_0x0aff
            r1 = r2
            io.dcloud.common.DHInterface.IApp r1 = (io.dcloud.common.DHInterface.IApp) r1     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IWebAppRootView r1 = r1.obtainWebAppRootView()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r1 = (io.dcloud.common.core.ui.a) r1     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r2 = r1.f()     // Catch:{ all -> 0x0af8 }
            if (r2 == 0) goto L_0x0764
            io.dcloud.common.DHInterface.IWebview r3 = r2.obtainWebView()     // Catch:{ all -> 0x0af8 }
            if (r3 == 0) goto L_0x0764
            io.dcloud.common.DHInterface.IWebview r2 = r2.obtainWebView()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.adapter.ui.AdaWebview r2 = (io.dcloud.common.adapter.ui.AdaWebview) r2     // Catch:{ all -> 0x0af8 }
            r2.checkInjectSitemap()     // Catch:{ all -> 0x0af8 }
        L_0x0764:
            io.dcloud.common.core.ui.b r1 = r1.g()     // Catch:{ all -> 0x0af8 }
            if (r1 == 0) goto L_0x0aff
            io.dcloud.common.DHInterface.IWebview r2 = r1.obtainWebView()     // Catch:{ all -> 0x0af8 }
            if (r2 == 0) goto L_0x0aff
            io.dcloud.common.DHInterface.IWebview r1 = r1.obtainWebView()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.adapter.ui.AdaWebview r1 = (io.dcloud.common.adapter.ui.AdaWebview) r1     // Catch:{ all -> 0x0af8 }
            r1.checkInjectSitemap()     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x077b:
            boolean r1 = r2 instanceof io.dcloud.common.DHInterface.IApp     // Catch:{ all -> 0x0af8 }
            if (r1 == 0) goto L_0x0aff
            r1 = r2
            io.dcloud.common.DHInterface.IApp r1 = (io.dcloud.common.DHInterface.IApp) r1     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IWebAppRootView r1 = r1.obtainWebAppRootView()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r1 = (io.dcloud.common.core.ui.a) r1     // Catch:{ all -> 0x0af8 }
            r3 = r2
            io.dcloud.common.DHInterface.IApp r3 = (io.dcloud.common.DHInterface.IApp) r3     // Catch:{ all -> 0x0af8 }
            boolean r3 = r9.a((io.dcloud.common.DHInterface.IApp) r3)     // Catch:{ all -> 0x0af8 }
            if (r3 == 0) goto L_0x0796
            io.dcloud.common.core.ui.b r1 = r1.f()     // Catch:{ all -> 0x0af8 }
            goto L_0x079b
        L_0x0796:
            r3 = 5
            io.dcloud.common.core.ui.b r1 = r1.a((int) r3)     // Catch:{ all -> 0x0af8 }
        L_0x079b:
            io.dcloud.common.DHInterface.IApp r2 = (io.dcloud.common.DHInterface.IApp) r2     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IApp$ConfigProperty$ThridInfo r3 = io.dcloud.common.DHInterface.IApp.ConfigProperty.ThridInfo.DirectPageJsonData     // Catch:{ all -> 0x0af8 }
            org.json.JSONObject r2 = r2.obtainThridInfo(r3)     // Catch:{ all -> 0x0af8 }
            org.json.JSONArray r3 = new org.json.JSONArray     // Catch:{ all -> 0x0af8 }
            r3.<init>()     // Catch:{ all -> 0x0af8 }
            java.lang.String r4 = "NWindow"
            r3.put(r4)     // Catch:{ all -> 0x0af8 }
            java.lang.String r4 = "setStyle"
            r3.put(r4)     // Catch:{ all -> 0x0af8 }
            org.json.JSONArray r4 = new org.json.JSONArray     // Catch:{ all -> 0x0af8 }
            r4.<init>()     // Catch:{ all -> 0x0af8 }
            int r5 = r1.hashCode()     // Catch:{ all -> 0x0af8 }
            r4.put(r5)     // Catch:{ all -> 0x0af8 }
            org.json.JSONArray r5 = new org.json.JSONArray     // Catch:{ all -> 0x0af8 }
            r5.<init>()     // Catch:{ all -> 0x0af8 }
            r5.put(r2)     // Catch:{ all -> 0x0af8 }
            r4.put(r5)     // Catch:{ all -> 0x0af8 }
            r3.put(r4)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IMgr$MgrType r2 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr     // Catch:{ all -> 0x0af8 }
            r4 = 4
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IWebview r1 = r1.obtainWebView()     // Catch:{ all -> 0x0af8 }
            r4[r13] = r1     // Catch:{ all -> 0x0af8 }
            java.lang.String r1 = "UI"
            r4[r12] = r1     // Catch:{ all -> 0x0af8 }
            java.lang.String r1 = "execMethod"
            r5 = 2
            r4[r5] = r1     // Catch:{ all -> 0x0af8 }
            r1 = 3
            r4[r1] = r3     // Catch:{ all -> 0x0af8 }
            r9.processEvent(r2, r12, r4)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x07e8:
            java.lang.String r1 = java.lang.String.valueOf(r19)     // Catch:{ all -> 0x0af8 }
            java.util.HashMap<java.lang.String, io.dcloud.common.core.ui.a> r2 = r9.a     // Catch:{ all -> 0x0af8 }
            java.lang.Object r1 = r2.get(r1)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r1 = (io.dcloud.common.core.ui.a) r1     // Catch:{ all -> 0x0af8 }
            if (r1 == 0) goto L_0x0aff
            io.dcloud.common.core.ui.b r1 = r1.g()     // Catch:{ all -> 0x0af8 }
            goto L_0x01d5
        L_0x07fc:
            java.lang.String r1 = java.lang.String.valueOf(r19)     // Catch:{ all -> 0x0af8 }
            java.util.HashMap<java.lang.String, io.dcloud.common.core.ui.a> r2 = r9.a     // Catch:{ all -> 0x0af8 }
            java.lang.Object r1 = r2.get(r1)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r1 = (io.dcloud.common.core.ui.a) r1     // Catch:{ all -> 0x0af8 }
            if (r1 == 0) goto L_0x0aff
            io.dcloud.common.core.ui.b r1 = r1.f()     // Catch:{ all -> 0x0af8 }
            goto L_0x01d5
        L_0x0810:
            r1 = r2
            java.lang.Object[] r1 = (java.lang.Object[]) r1     // Catch:{ all -> 0x0af8 }
            r2 = r1[r13]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r2 = (io.dcloud.common.core.ui.b) r2     // Catch:{ all -> 0x0af8 }
            r1 = r1[r12]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r1 = (io.dcloud.common.core.ui.b) r1     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IApp r3 = r2.obtainApp()     // Catch:{ all -> 0x0af8 }
            boolean r3 = io.dcloud.common.util.BaseInfo.isUniAppAppid(r3)     // Catch:{ all -> 0x0af8 }
            if (r3 == 0) goto L_0x0833
            io.dcloud.common.DHInterface.IWebview r3 = r2.obtainWebView()     // Catch:{ all -> 0x0af8 }
            r3.setIWebViewFocusable(r12)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IWebview r3 = r1.obtainWebView()     // Catch:{ all -> 0x0af8 }
            r3.setIWebViewFocusable(r12)     // Catch:{ all -> 0x0af8 }
        L_0x0833:
            long r3 = r1.lastShowTime     // Catch:{ all -> 0x0af8 }
            r5 = 1
            long r3 = r3 - r5
            r2.lastShowTime = r3     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r3 = r2.p     // Catch:{ all -> 0x0af8 }
            java.util.Stack r3 = r3.e()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r4 = r2.p     // Catch:{ all -> 0x0af8 }
            java.util.ArrayList r4 = r4.d()     // Catch:{ all -> 0x0af8 }
            r3.remove(r2)     // Catch:{ all -> 0x0af8 }
            r4.remove(r2)     // Catch:{ all -> 0x0af8 }
            int r1 = r4.indexOf(r1)     // Catch:{ all -> 0x0af8 }
            r4.add(r1, r2)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r1 = r2.p     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IWebview r3 = r2.obtainWebView()     // Catch:{ all -> 0x0af8 }
            android.view.ViewGroup r3 = r3.obtainWindowView()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.l$h r4 = new io.dcloud.common.core.ui.l$h     // Catch:{ all -> 0x0af8 }
            r4.<init>(r2)     // Catch:{ all -> 0x0af8 }
            r1.a((android.view.View) r3, (io.dcloud.common.DHInterface.ICallBack) r4)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x0867:
            r1 = r2
            io.dcloud.common.DHInterface.IApp r1 = (io.dcloud.common.DHInterface.IApp) r1     // Catch:{ all -> 0x0af8 }
            java.util.HashMap<java.lang.String, io.dcloud.common.core.ui.a> r2 = r9.a     // Catch:{ all -> 0x0af8 }
            java.lang.String r1 = r1.obtainAppId()     // Catch:{ all -> 0x0af8 }
            java.lang.Object r1 = r2.get(r1)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r1 = (io.dcloud.common.core.ui.a) r1     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r1 = r1.i()     // Catch:{ all -> 0x0af8 }
            goto L_0x01d5
        L_0x087c:
            r1 = r2
            io.dcloud.common.DHInterface.IApp r1 = (io.dcloud.common.DHInterface.IApp) r1     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IWebAppRootView r1 = r1.obtainWebAppRootView()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r1 = (io.dcloud.common.core.ui.a) r1     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r1 = r1.d     // Catch:{ all -> 0x0af8 }
            goto L_0x01d5
        L_0x0889:
            r1 = r2
            io.dcloud.common.core.ui.b r1 = (io.dcloud.common.core.ui.b) r1     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r2 = r1.p     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r3 = r2.d     // Catch:{ all -> 0x0af8 }
            if (r1 != r3) goto L_0x0aff
            r2.d = r10     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x0896:
            r9.b((int) r1, (java.lang.Object) r2)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x089b:
            r1 = r2
            io.dcloud.common.DHInterface.IApp r1 = (io.dcloud.common.DHInterface.IApp) r1     // Catch:{ all -> 0x0af8 }
            java.util.HashMap<java.lang.String, io.dcloud.common.core.ui.a> r2 = r9.a     // Catch:{ all -> 0x0af8 }
            java.lang.String r1 = r1.obtainAppId()     // Catch:{ all -> 0x0af8 }
            java.lang.Object r1 = r2.get(r1)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r1 = (io.dcloud.common.core.ui.a) r1     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r1 = r1.i()     // Catch:{ all -> 0x0af8 }
            if (r1 == 0) goto L_0x08ba
            boolean r2 = r1 instanceof io.dcloud.common.core.ui.c     // Catch:{ all -> 0x0af8 }
            if (r2 == 0) goto L_0x08ba
            io.dcloud.common.core.ui.c r1 = (io.dcloud.common.core.ui.c) r1     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r1 = r1.v()     // Catch:{ all -> 0x0af8 }
        L_0x08ba:
            if (r1 == 0) goto L_0x08ce
            io.dcloud.common.adapter.util.ViewOptions r2 = r1.obtainFrameOptions()     // Catch:{ all -> 0x0af8 }
            org.json.JSONObject r2 = r2.mDebugRefresh     // Catch:{ all -> 0x0af8 }
            if (r2 == 0) goto L_0x08ce
            io.dcloud.common.adapter.util.ViewOptions r2 = r1.obtainFrameOptions()     // Catch:{ all -> 0x0af8 }
            org.json.JSONObject r2 = r2.mDebugRefresh     // Catch:{ all -> 0x0af8 }
            java.lang.String r10 = r2.toString()     // Catch:{ all -> 0x0af8 }
        L_0x08ce:
            if (r10 != 0) goto L_0x0aff
            if (r1 == 0) goto L_0x0aff
            io.dcloud.common.adapter.util.ViewOptions r2 = r1.obtainFrameOptions()     // Catch:{ all -> 0x0af8 }
            org.json.JSONObject r2 = r2.mUniPageUrl     // Catch:{ all -> 0x0af8 }
            if (r2 == 0) goto L_0x0aff
            io.dcloud.common.adapter.util.ViewOptions r1 = r1.obtainFrameOptions()     // Catch:{ all -> 0x0af8 }
            org.json.JSONObject r1 = r1.mUniPageUrl     // Catch:{ all -> 0x0af8 }
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ all -> 0x0af8 }
            r2.<init>()     // Catch:{ all -> 0x0af8 }
            java.lang.String r3 = "arguments"
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0af8 }
            r2.put(r3, r1)     // Catch:{ all -> 0x0af8 }
            java.lang.String r1 = r2.toString()     // Catch:{ all -> 0x0af8 }
            goto L_0x01d5
        L_0x08f4:
            r1 = r2
            java.lang.Object[] r1 = (java.lang.Object[]) r1     // Catch:{ all -> 0x0af8 }
            r2 = r1[r13]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IWebview r2 = (io.dcloud.common.DHInterface.IWebview) r2     // Catch:{ all -> 0x0af8 }
            r1 = r1[r12]     // Catch:{ all -> 0x0af8 }
            android.os.Bundle r1 = (android.os.Bundle) r1     // Catch:{ all -> 0x0af8 }
            android.app.Activity r3 = r2.getActivity()     // Catch:{ all -> 0x0af8 }
            boolean r3 = r3 instanceof io.dcloud.common.DHInterface.IActivityHandler     // Catch:{ all -> 0x0af8 }
            if (r3 == 0) goto L_0x0aff
            android.app.Activity r2 = r2.getActivity()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IActivityHandler r2 = (io.dcloud.common.DHInterface.IActivityHandler) r2     // Catch:{ all -> 0x0af8 }
            java.lang.String r3 = "unimp_capsule_button_click"
            r2.callBack(r3, r1)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x0914:
            if (r2 == 0) goto L_0x0aff
            r1 = r2
            android.os.Bundle r1 = (android.os.Bundle) r1     // Catch:{ all -> 0x0af8 }
            java.lang.String r2 = r1.getString(r14)     // Catch:{ all -> 0x0af8 }
            java.util.HashMap<java.lang.String, io.dcloud.common.core.ui.a> r4 = r9.a     // Catch:{ all -> 0x0af8 }
            java.lang.Object r4 = r4.get(r2)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r4 = (io.dcloud.common.core.ui.a) r4     // Catch:{ all -> 0x0af8 }
            if (r4 == 0) goto L_0x0aff
            io.dcloud.common.core.ui.b r4 = r4.i()     // Catch:{ all -> 0x0af8 }
            if (r4 == 0) goto L_0x0aff
            io.dcloud.common.DHInterface.IMgr$MgrType r5 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr     // Catch:{ all -> 0x0af8 }
            r6 = 4
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IApp r4 = r4.obtainApp()     // Catch:{ all -> 0x0af8 }
            r6[r13] = r4     // Catch:{ all -> 0x0af8 }
            java.lang.String r4 = "ui"
            r6[r12] = r4     // Catch:{ all -> 0x0af8 }
            java.lang.String r4 = "findWebview"
            r7 = 2
            r6[r7] = r4     // Catch:{ all -> 0x0af8 }
            java.lang.String[] r4 = new java.lang.String[r7]     // Catch:{ all -> 0x0af8 }
            r4[r13] = r2     // Catch:{ all -> 0x0af8 }
            java.lang.String r2 = "__uniapp__service"
            r4[r12] = r2     // Catch:{ all -> 0x0af8 }
            r2 = 3
            r6[r2] = r4     // Catch:{ all -> 0x0af8 }
            r2 = 10
            java.lang.Object r2 = r9.processEvent(r5, r2, r6)     // Catch:{ all -> 0x0af8 }
            if (r2 == 0) goto L_0x0aff
            boolean r4 = r2 instanceof io.dcloud.common.adapter.ui.AdaUniWebView     // Catch:{ all -> 0x0af8 }
            if (r4 == 0) goto L_0x0aff
            java.util.HashMap r4 = new java.util.HashMap     // Catch:{ all -> 0x0af8 }
            r4.<init>()     // Catch:{ all -> 0x0af8 }
            java.lang.String r5 = "dataType"
            java.lang.String r5 = r1.getString(r5)     // Catch:{ all -> 0x0af8 }
            java.lang.String r6 = "JSON"
            boolean r5 = r5.equals(r6)     // Catch:{ all -> 0x0af8 }
            java.lang.String r6 = "data"
            if (r5 == 0) goto L_0x0979
            java.lang.String r5 = r1.getString(r6)     // Catch:{ all -> 0x0af8 }
            java.lang.Object r5 = com.alibaba.fastjson.JSON.parse(r5)     // Catch:{ all -> 0x0af8 }
            r4.put(r6, r5)     // Catch:{ all -> 0x0af8 }
            goto L_0x0980
        L_0x0979:
            java.lang.String r5 = r1.getString(r6)     // Catch:{ all -> 0x0af8 }
            r4.put(r6, r5)     // Catch:{ all -> 0x0af8 }
        L_0x0980:
            java.lang.String r1 = r1.getString(r3)     // Catch:{ all -> 0x0af8 }
            r4.put(r3, r1)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.adapter.util.EventActionInfo r1 = new io.dcloud.common.adapter.util.EventActionInfo     // Catch:{ all -> 0x0af8 }
            java.lang.String r3 = "uniMPNativeEvent"
            r1.<init>((java.lang.String) r3, (java.util.Map<java.lang.String, java.lang.Object>) r4)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.adapter.ui.AdaUniWebView r2 = (io.dcloud.common.adapter.ui.AdaUniWebView) r2     // Catch:{ all -> 0x0af8 }
            r2.fireEvent(r1)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x0995:
            if (r2 == 0) goto L_0x0aff
            r1 = r2
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ all -> 0x0af8 }
            java.util.HashMap<java.lang.String, io.dcloud.common.core.ui.a> r2 = r9.a     // Catch:{ all -> 0x0af8 }
            java.lang.Object r1 = r2.get(r1)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r1 = (io.dcloud.common.core.ui.a) r1     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r1 = r1.i()     // Catch:{ all -> 0x0af8 }
            if (r1 == 0) goto L_0x09b2
            boolean r2 = r1 instanceof io.dcloud.common.core.ui.c     // Catch:{ all -> 0x0af8 }
            if (r2 == 0) goto L_0x09b2
            io.dcloud.common.core.ui.c r1 = (io.dcloud.common.core.ui.c) r1     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r1 = r1.v()     // Catch:{ all -> 0x0af8 }
        L_0x09b2:
            if (r1 == 0) goto L_0x0aff
            io.dcloud.common.adapter.util.ViewOptions r2 = r1.obtainFrameOptions()     // Catch:{ all -> 0x0af8 }
            org.json.JSONObject r2 = r2.mUniPageUrl     // Catch:{ all -> 0x0af8 }
            if (r2 == 0) goto L_0x0aff
            io.dcloud.common.adapter.util.ViewOptions r1 = r1.obtainFrameOptions()     // Catch:{ all -> 0x0af8 }
            org.json.JSONObject r1 = r1.mUniPageUrl     // Catch:{ all -> 0x0af8 }
            if (r1 == 0) goto L_0x0aff
            java.lang.String r2 = "path"
            java.lang.String r10 = r1.getString(r2)     // Catch:{ all -> 0x0af8 }
            boolean r2 = r1.has(r7)     // Catch:{ all -> 0x0af8 }
            if (r2 == 0) goto L_0x0aff
            java.lang.String r1 = r1.getString(r7)     // Catch:{ all -> 0x0af8 }
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ all -> 0x0af8 }
            if (r2 != 0) goto L_0x0aff
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0af8 }
            r2.<init>()     // Catch:{ all -> 0x0af8 }
            r2.append(r10)     // Catch:{ all -> 0x0af8 }
            java.lang.String r3 = "?"
            r2.append(r3)     // Catch:{ all -> 0x0af8 }
            r2.append(r1)     // Catch:{ all -> 0x0af8 }
            java.lang.String r1 = r2.toString()     // Catch:{ all -> 0x0af8 }
            goto L_0x01d5
        L_0x09f0:
            if (r2 == 0) goto L_0x0aff
            r1 = r2
            java.lang.Object[] r1 = (java.lang.Object[]) r1     // Catch:{ all -> 0x0af8 }
            r2 = r1[r13]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IWebview r2 = (io.dcloud.common.DHInterface.IWebview) r2     // Catch:{ all -> 0x0af8 }
            r1 = r1[r12]     // Catch:{ all -> 0x0af8 }
            android.os.Bundle r1 = (android.os.Bundle) r1     // Catch:{ all -> 0x0af8 }
            android.app.Activity r3 = r2.getActivity()     // Catch:{ all -> 0x0af8 }
            boolean r3 = r3 instanceof io.dcloud.common.DHInterface.IActivityHandler     // Catch:{ all -> 0x0af8 }
            if (r3 == 0) goto L_0x0aff
            android.app.Activity r2 = r2.getActivity()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IActivityHandler r2 = (io.dcloud.common.DHInterface.IActivityHandler) r2     // Catch:{ all -> 0x0af8 }
            java.lang.String r3 = "TITLE_BAR_MENU_CLICK"
            r2.callBack(r3, r1)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x0a12:
            r1 = r2
            io.dcloud.common.DHInterface.IApp r1 = (io.dcloud.common.DHInterface.IApp) r1     // Catch:{ all -> 0x0af8 }
            java.util.HashMap<java.lang.String, io.dcloud.common.core.ui.a> r2 = r9.a     // Catch:{ all -> 0x0af8 }
            java.lang.String r3 = r1.obtainAppId()     // Catch:{ all -> 0x0af8 }
            java.lang.Object r2 = r2.get(r3)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.a r2 = (io.dcloud.common.core.ui.a) r2     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r2 = r2.i()     // Catch:{ all -> 0x0af8 }
            if (r2 == 0) goto L_0x0a31
            boolean r3 = r2 instanceof io.dcloud.common.core.ui.c     // Catch:{ all -> 0x0af8 }
            if (r3 == 0) goto L_0x0a31
            io.dcloud.common.core.ui.c r2 = (io.dcloud.common.core.ui.c) r2     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r2 = r2.v()     // Catch:{ all -> 0x0af8 }
        L_0x0a31:
            java.lang.String r3 = "uni_restart_to_direct"
            if (r2 == 0) goto L_0x0a55
            io.dcloud.common.adapter.util.ViewOptions r4 = r2.obtainFrameOptions()     // Catch:{ all -> 0x0af8 }
            org.json.JSONObject r4 = r4.mDebugRefresh     // Catch:{ all -> 0x0af8 }
            if (r4 == 0) goto L_0x0a55
            java.lang.String r4 = "debugRefresh"
            io.dcloud.common.adapter.util.ViewOptions r2 = r2.obtainFrameOptions()     // Catch:{ all -> 0x0af8 }
            org.json.JSONObject r2 = r2.mDebugRefresh     // Catch:{ all -> 0x0af8 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0af8 }
            r1.setConfigProperty(r4, r2)     // Catch:{ all -> 0x0af8 }
            java.lang.String r2 = java.lang.String.valueOf(r12)     // Catch:{ all -> 0x0af8 }
            r1.setConfigProperty(r3, r2)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x0a55:
            java.lang.String r2 = java.lang.String.valueOf(r13)     // Catch:{ all -> 0x0af8 }
            r1.setConfigProperty(r3, r2)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x0a5e:
            r1 = r2
            java.lang.Object[] r1 = (java.lang.Object[]) r1     // Catch:{ all -> 0x0af8 }
            r1 = r1[r13]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r1 = (io.dcloud.common.core.ui.b) r1     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.adapter.ui.AdaWebViewParent r2 = r1.obtainWebviewParent()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.adapter.util.ViewOptions r2 = r2.obtainFrameOptions()     // Catch:{ all -> 0x0af8 }
            boolean r3 = r2.hasBackground()     // Catch:{ all -> 0x0af8 }
            if (r3 == 0) goto L_0x0a7d
            io.dcloud.common.adapter.util.ViewOptions r3 = r1.obtainFrameOptions()     // Catch:{ all -> 0x0af8 }
            boolean r3 = r3.isTabHasBg()     // Catch:{ all -> 0x0af8 }
            if (r3 == 0) goto L_0x0a81
        L_0x0a7d:
            io.dcloud.common.adapter.util.ViewOptions r2 = r1.obtainFrameOptions()     // Catch:{ all -> 0x0af8 }
        L_0x0a81:
            io.dcloud.common.adapter.ui.AdaWebViewParent r3 = r1.obtainWebviewParent()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IWebview r4 = r1.obtainWebView()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.adapter.ui.AdaFrameItem r4 = (io.dcloud.common.adapter.ui.AdaFrameItem) r4     // Catch:{ all -> 0x0af8 }
            r1.b(r2, r1, r3, r4)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x0a90:
            r1 = r2
            java.lang.Object[] r1 = (java.lang.Object[]) r1     // Catch:{ all -> 0x0af8 }
            r1 = r1[r13]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r1 = (io.dcloud.common.core.ui.b) r1     // Catch:{ all -> 0x0af8 }
            r9.a((io.dcloud.common.core.ui.b) r1, (boolean) r12)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x0a9b:
            r1 = r2
            java.lang.Object[] r1 = (java.lang.Object[]) r1     // Catch:{ all -> 0x0af8 }
            r2 = r1[r13]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r2 = (io.dcloud.common.core.ui.b) r2     // Catch:{ all -> 0x0af8 }
            r1 = r1[r12]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r1 = (io.dcloud.common.core.ui.b) r1     // Catch:{ all -> 0x0af8 }
            r9.a((io.dcloud.common.core.ui.b) r2, (io.dcloud.common.core.ui.b) r1)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x0aaa:
            r1 = r2
            java.lang.Object[] r1 = (java.lang.Object[]) r1     // Catch:{ all -> 0x0af8 }
            r2 = r1[r13]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r2 = (io.dcloud.common.core.ui.b) r2     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IApp r3 = r2.obtainApp()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.util.AppStatusBarManager r3 = r3.obtainStatusBarMgr()     // Catch:{ all -> 0x0af8 }
            boolean r3 = r3.isImmersive     // Catch:{ all -> 0x0af8 }
            r4 = r1[r12]     // Catch:{ all -> 0x0af8 }
            org.json.JSONObject r4 = (org.json.JSONObject) r4     // Catch:{ all -> 0x0af8 }
            r5 = 2
            r1 = r1[r5]     // Catch:{ all -> 0x0af8 }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ all -> 0x0af8 }
            r9.a((io.dcloud.common.core.ui.b) r2, (boolean) r3, (org.json.JSONObject) r4, (java.lang.String) r1)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x0ac8:
            r1 = r2
            io.dcloud.common.core.ui.l$m r1 = (io.dcloud.common.core.ui.l.m) r1     // Catch:{ all -> 0x0af8 }
            r9.a((io.dcloud.common.core.ui.l.m) r1)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x0acf:
            r16.c()     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x0ad3:
            r1 = r2
            java.lang.Object[] r1 = (java.lang.Object[]) r1     // Catch:{ all -> 0x0af8 }
            r2 = r1[r13]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IApp r2 = (io.dcloud.common.DHInterface.IApp) r2     // Catch:{ all -> 0x0af8 }
            r3 = r1[r13]     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.DHInterface.IApp r3 = (io.dcloud.common.DHInterface.IApp) r3     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.core.ui.b r3 = r9.b((io.dcloud.common.DHInterface.IApp) r3)     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.adapter.ui.AdaWebview r3 = r3.q     // Catch:{ all -> 0x0af8 }
            r1 = r1[r12]     // Catch:{ all -> 0x0af8 }
            org.json.JSONObject r1 = (org.json.JSONObject) r1     // Catch:{ all -> 0x0af8 }
            r9.a((io.dcloud.common.DHInterface.IApp) r2, (io.dcloud.common.DHInterface.IWebview) r3, (org.json.JSONObject) r1)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x0aec:
            io.dcloud.common.core.ui.a r1 = r16.b()     // Catch:{ all -> 0x0af8 }
            android.view.ViewGroup r1 = r1.obtainMainViewGroup()     // Catch:{ all -> 0x0af8 }
            io.dcloud.common.util.TestUtil.debug(r1)     // Catch:{ all -> 0x0af8 }
            goto L_0x0aff
        L_0x0af8:
            r0 = move-exception
            r1 = r0
            java.lang.String r2 = "WindowMgr.processEvent"
            io.dcloud.common.adapter.util.Logger.w(r2, r1)
        L_0x0aff:
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.l.processEvent(io.dcloud.common.DHInterface.IMgr$MgrType, int, java.lang.Object):java.lang.Object");
    }

    public synchronized void a(m mVar) {
        if (!this.b.contains(mVar)) {
            this.b.add(mVar);
        }
    }

    /* access modifiers changed from: package-private */
    public void a(ViewGroup viewGroup, IApp iApp, IWebview iWebview, ViewGroup.LayoutParams layoutParams) {
        a(iApp, iApp.obtainAppId());
        b bVar = (b) iWebview.obtainFrameView();
        bVar.p = this.a.get(iApp.obtainAppId());
        View obtainMainView = bVar.obtainMainView();
        if (obtainMainView.getParent() != null) {
            ((ViewGroup) obtainMainView.getParent()).removeView(obtainMainView);
        }
        viewGroup.addView(obtainMainView, layoutParams);
    }

    public void c(b bVar) {
        bVar.a(b.d);
        bVar.q();
        bVar.p.b(bVar);
        if (bVar.f()) {
            processEvent(IMgr.MgrType.WindowMgr, 28, bVar.g);
            bVar.g = null;
        }
        bVar.makeViewOptions_animate();
        bVar.m();
        bVar.l();
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean a(IApp iApp, String str) {
        boolean z;
        Logger.e("streamsdk", "come into createAppRootView pAppid===" + str);
        a aVar = this.a.get(str);
        z = true;
        if (aVar != null) {
            if (aVar.h) {
                z = false;
            }
        }
        if (aVar != null && !aVar.h) {
            this.a.remove(str);
        }
        Logger.e("streamsdk", "come into createAppRootView and new le rootview  pAppid===" + str);
        Logger.d(Logger.MAIN_TAG, "create " + str + " AppRootView");
        a aVar2 = new a(iApp.getActivity(), iApp, (b) null);
        aVar2.onAppStart(iApp);
        aVar2.obtainFrameOptions().setParentViewRect(iApp.getAppViewRect());
        aVar2.obtainFrameOptions().updateViewData(JSONUtil.createJSONObject("{}"), iApp.getInt(0), iApp.getInt(1));
        this.a.put(str, aVar2);
        iApp.obtainAppId();
        return z;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v8, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v27, resolved type: org.json.JSONObject} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v28, resolved type: org.json.JSONObject} */
    /* JADX WARNING: type inference failed for: r3v4, types: [org.json.JSONObject] */
    /* JADX WARNING: type inference failed for: r3v26 */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x0172, code lost:
        r3 = new org.json.JSONObject(r2.getString("arguments"));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x017a, code lost:
        r3 = null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:65:0x0169 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x0261  */
    /* JADX WARNING: Removed duplicated region for block: B:114:0x027f  */
    /* JADX WARNING: Removed duplicated region for block: B:117:0x028c  */
    /* JADX WARNING: Removed duplicated region for block: B:122:0x02a4  */
    /* JADX WARNING: Removed duplicated region for block: B:123:0x02ac  */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x02c6  */
    /* JADX WARNING: Removed duplicated region for block: B:163:0x0387  */
    /* JADX WARNING: Removed duplicated region for block: B:176:0x03b2 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:177:0x03b5  */
    /* JADX WARNING: Removed duplicated region for block: B:181:0x03f8  */
    /* JADX WARNING: Removed duplicated region for block: B:182:0x0404  */
    /* JADX WARNING: Removed duplicated region for block: B:185:0x0419  */
    /* JADX WARNING: Removed duplicated region for block: B:186:0x041c  */
    /* JADX WARNING: Removed duplicated region for block: B:190:0x0428  */
    /* JADX WARNING: Removed duplicated region for block: B:193:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00be  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00e6  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x0111  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x015f A[SYNTHETIC, Splitter:B:61:0x015f] */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0189  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0198  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(int r26, java.lang.Object r27) {
        /*
            r25 = this;
            r9 = r25
            r0 = r27
            java.lang.String r7 = "arguments"
            java.lang.String r1 = "StreamSDK"
            java.lang.String r2 = "come into activeAppRootView"
            io.dcloud.common.adapter.util.Logger.e(r1, r2)
            boolean r1 = r0 instanceof java.lang.Object[]
            if (r1 == 0) goto L_0x042c
            r10 = r0
            java.lang.Object[] r10 = (java.lang.Object[]) r10
            r11 = 0
            r0 = r10[r11]
            r12 = r0
            io.dcloud.common.DHInterface.IApp r12 = (io.dcloud.common.DHInterface.IApp) r12
            r8 = 0
            boolean r0 = io.dcloud.common.util.BaseInfo.isWap2AppAppid(r8)
            if (r0 == 0) goto L_0x0028
            boolean r0 = io.dcloud.e.c.g.c()
            if (r0 != 0) goto L_0x0028
            return
        L_0x0028:
            int r0 = r10.length
            r13 = 2
            r14 = 3
            if (r0 < r14) goto L_0x0037
            r0 = r10[r13]
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            r15 = r0
            goto L_0x0038
        L_0x0037:
            r15 = 0
        L_0x0038:
            int r0 = r10.length
            r6 = 4
            if (r0 < r6) goto L_0x0041
            r0 = r10[r14]
            java.lang.String r0 = (java.lang.String) r0
            goto L_0x0043
        L_0x0041:
            java.lang.String r0 = "default"
        L_0x0043:
            r5 = r0
            r4 = 1
            r0 = r10[r4]
            java.lang.String r16 = java.lang.String.valueOf(r0)
            java.lang.String r3 = r12.obtainAppId()
            java.util.HashMap<java.lang.String, io.dcloud.common.core.ui.a> r0 = r9.a
            java.lang.Object r0 = r0.get(r3)
            r2 = r0
            io.dcloud.common.core.ui.a r2 = (io.dcloud.common.core.ui.a) r2
            r1 = 10
            r14 = r26
            if (r1 != r14) goto L_0x0072
            java.lang.String r0 = "winmgr"
            java.lang.String r1 = "RESTART_APP_ROOT_VIEW"
            io.dcloud.common.adapter.util.Logger.i(r0, r1)
            r2.b()
            r2.k()
            r2.onAppStart(r12)
            r9.a((io.dcloud.common.core.ui.a) r2)
        L_0x0072:
            android.content.Intent r0 = r12.obtainWebAppIntent()
            java.lang.String r1 = "__from_stream_open_style__"
            java.lang.String r0 = r0.getStringExtra(r1)
            boolean r18 = android.text.TextUtils.isEmpty(r0)     // Catch:{ JSONException -> 0x00af }
            if (r18 != 0) goto L_0x008f
            org.json.JSONObject r4 = new org.json.JSONObject     // Catch:{ JSONException -> 0x00af }
            r4.<init>(r0)     // Catch:{ JSONException -> 0x00af }
            android.content.Intent r0 = r12.obtainWebAppIntent()     // Catch:{ JSONException -> 0x00ad }
            r0.removeExtra(r1)     // Catch:{ JSONException -> 0x00ad }
            goto L_0x00b4
        L_0x008f:
            io.dcloud.common.DHInterface.IApp$ConfigProperty$ThridInfo r0 = io.dcloud.common.DHInterface.IApp.ConfigProperty.ThridInfo.LaunchWebviewJsonData     // Catch:{ JSONException -> 0x00af }
            org.json.JSONObject r4 = r12.obtainThridInfo(r0)     // Catch:{ JSONException -> 0x00af }
            if (r4 != 0) goto L_0x00b4
            boolean r0 = r9.a((io.dcloud.common.DHInterface.IApp) r12)     // Catch:{ JSONException -> 0x00ad }
            if (r0 == 0) goto L_0x00a5
            io.dcloud.common.DHInterface.IApp$ConfigProperty$ThridInfo r0 = io.dcloud.common.DHInterface.IApp.ConfigProperty.ThridInfo.DirectPageJsonData     // Catch:{ JSONException -> 0x00ad }
            org.json.JSONObject r0 = r12.obtainThridInfo(r0)     // Catch:{ JSONException -> 0x00ad }
        L_0x00a3:
            r4 = r0
            goto L_0x00b4
        L_0x00a5:
            java.lang.String r0 = "{}"
            org.json.JSONObject r0 = io.dcloud.common.util.JSONUtil.createJSONObject(r0)     // Catch:{ JSONException -> 0x00ad }
            goto L_0x00a3
        L_0x00ad:
            r0 = move-exception
            goto L_0x00b1
        L_0x00af:
            r0 = move-exception
            r4 = r8
        L_0x00b1:
            r0.printStackTrace()
        L_0x00b4:
            r19 = r4
            java.lang.String r0 = io.dcloud.common.util.BaseInfo.sDefWebViewUserAgent
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r0)
            if (r0 == 0) goto L_0x00c8
            android.app.Activity r0 = r12.getActivity()
            java.lang.String r0 = io.dcloud.common.adapter.ui.webview.WebViewFactory.getDefWebViewUA(r0)
            io.dcloud.common.util.BaseInfo.sDefWebViewUserAgent = r0
        L_0x00c8:
            boolean r0 = io.dcloud.common.util.BaseInfo.isUniAppAppid(r12)
            java.lang.String r4 = "path"
            if (r0 == 0) goto L_0x0111
            boolean r0 = io.dcloud.common.util.BaseInfo.isWeexUniJs(r12)
            if (r0 == 0) goto L_0x0111
            java.lang.String r0 = "uniapp_weex_js_service"
            java.lang.String r0 = r12.obtainConfigProperty(r0)
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            boolean r0 = r0.booleanValue()
            if (r0 != 0) goto L_0x0111
            org.json.JSONObject r1 = new org.json.JSONObject
            r1.<init>()
            java.lang.String r0 = "_www/app-service.js"
            r1.put(r4, r0)     // Catch:{ JSONException -> 0x00f1 }
            goto L_0x00f5
        L_0x00f1:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00f5:
            java.lang.String r0 = "__uniapp__service"
            java.lang.String r20 = "__uniapp__service"
            r17 = r1
            r1 = r25
            r21 = r2
            r2 = r12
            r11 = r3
            r3 = r21
            r13 = r4
            r22 = 1
            r4 = r0
            r23 = r5
            r5 = r20
            r6 = r17
            r1.a(r2, r3, r4, r5, r6)
            goto L_0x0119
        L_0x0111:
            r21 = r2
            r11 = r3
            r13 = r4
            r23 = r5
            r22 = 1
        L_0x0119:
            android.view.View r0 = r21.obtainMainView()
            android.view.ViewGroup r0 = (android.view.ViewGroup) r0
            r9.a((io.dcloud.common.DHInterface.IApp) r12, (android.view.ViewGroup) r0)
            r6 = r21
            io.dcloud.common.core.ui.c r1 = r9.b((io.dcloud.common.DHInterface.IApp) r12, (io.dcloud.common.core.ui.a) r6)
            io.dcloud.common.core.ui.b r0 = r6.f
            java.lang.String r2 = "uni_restart_to_direct"
            java.lang.String r3 = r12.obtainConfigProperty(r2)
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r3)
            boolean r4 = r3.booleanValue()
            java.lang.String r3 = "debugRefresh"
            java.lang.String r5 = r12.obtainConfigProperty(r3)
            boolean r17 = io.dcloud.common.util.BaseInfo.SyncDebug
            if (r17 == 0) goto L_0x0159
            java.lang.String r17 = io.src.dcloud.adapter.DCloudBaseActivity.loadDexDirectInfo
            boolean r17 = io.dcloud.common.util.PdrUtil.isEmpty(r17)
            if (r17 != 0) goto L_0x0159
            java.lang.String r5 = io.src.dcloud.adapter.DCloudBaseActivity.loadDexDirectInfo
            io.src.dcloud.adapter.DCloudBaseActivity.loadDexDirectInfo = r8
            r12.setConfigProperty(r3, r5)
            java.lang.String r3 = java.lang.String.valueOf(r22)
            r12.setConfigProperty(r2, r3)
            r4 = 1
        L_0x0159:
            boolean r2 = android.text.TextUtils.isEmpty(r5)
            if (r2 != 0) goto L_0x017c
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ JSONException -> 0x017c }
            r2.<init>(r5)     // Catch:{ JSONException -> 0x017c }
            org.json.JSONObject r3 = r2.getJSONObject(r7)     // Catch:{ JSONException -> 0x0169 }
            goto L_0x0173
        L_0x0169:
            java.lang.String r3 = r2.getString(r7)     // Catch:{ JSONException -> 0x017a }
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{ JSONException -> 0x017a }
            r5.<init>(r3)     // Catch:{ JSONException -> 0x017a }
            r3 = r5
        L_0x0173:
            java.lang.String r5 = r3.optString(r13)     // Catch:{ JSONException -> 0x0178 }
            goto L_0x017f
        L_0x0178:
            r5 = r8
            goto L_0x017f
        L_0x017a:
            r3 = r8
            goto L_0x017e
        L_0x017c:
            r2 = r8
            r3 = r2
        L_0x017e:
            r5 = r3
        L_0x017f:
            java.lang.String r7 = ""
            if (r0 == 0) goto L_0x0198
            boolean r17 = r9.a((io.dcloud.common.DHInterface.IApp) r12)
            if (r17 == 0) goto L_0x0198
            io.dcloud.common.DHInterface.IWebview r3 = r0.obtainWebView()
            r14 = r3
            r20 = r4
            r21 = r5
            r27 = r7
            r4 = 0
            r13 = 1
            goto L_0x0385
        L_0x0198:
            java.lang.String r8 = "id"
            if (r4 == 0) goto L_0x022d
            org.json.JSONObject r14 = new org.json.JSONObject
            r14.<init>()
            java.lang.String r0 = "1"
            r14.put(r8, r0)     // Catch:{ JSONException -> 0x0224 }
            java.lang.String r0 = "render"
            r20 = r4
            java.lang.String r4 = "always"
            r14.put(r0, r4)     // Catch:{ JSONException -> 0x0222 }
            boolean r0 = android.text.TextUtils.isEmpty(r5)     // Catch:{ JSONException -> 0x0222 }
            if (r0 != 0) goto L_0x021f
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x0222 }
            r0.<init>()     // Catch:{ JSONException -> 0x0222 }
            java.lang.String r4 = io.dcloud.common.util.BaseInfo.sCacheFsAppsPath     // Catch:{ JSONException -> 0x0222 }
            r0.append(r4)     // Catch:{ JSONException -> 0x0222 }
            r0.append(r11)     // Catch:{ JSONException -> 0x0222 }
            char r4 = io.dcloud.common.adapter.util.DeviceInfo.sSeparatorChar     // Catch:{ JSONException -> 0x0222 }
            r0.append(r4)     // Catch:{ JSONException -> 0x0222 }
            java.lang.String r4 = io.dcloud.common.util.BaseInfo.REAL_PRIVATE_WWW_DIR     // Catch:{ JSONException -> 0x0222 }
            r0.append(r4)     // Catch:{ JSONException -> 0x0222 }
            r0.append(r5)     // Catch:{ JSONException -> 0x0222 }
            java.lang.String r4 = ".js"
            r0.append(r4)     // Catch:{ JSONException -> 0x0222 }
            java.lang.String r0 = r0.toString()     // Catch:{ JSONException -> 0x0222 }
            java.io.File r4 = new java.io.File     // Catch:{ JSONException -> 0x0222 }
            r4.<init>(r0)     // Catch:{ JSONException -> 0x0222 }
            boolean r4 = r4.exists()     // Catch:{ JSONException -> 0x0222 }
            if (r4 == 0) goto L_0x021f
            org.json.JSONObject r4 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0222 }
            r4.<init>()     // Catch:{ JSONException -> 0x0222 }
            r4.put(r13, r0)     // Catch:{ JSONException -> 0x0222 }
            if (r2 == 0) goto L_0x0215
            if (r3 == 0) goto L_0x0215
            r21 = r5
            java.lang.String r5 = "query"
            java.lang.String r3 = r3.optString(r5)     // Catch:{ JSONException -> 0x021d }
            boolean r5 = android.text.TextUtils.isEmpty(r3)     // Catch:{ JSONException -> 0x021d }
            if (r5 != 0) goto L_0x0217
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x021d }
            r5.<init>()     // Catch:{ JSONException -> 0x021d }
            r5.append(r0)     // Catch:{ JSONException -> 0x021d }
            java.lang.String r0 = "?"
            r5.append(r0)     // Catch:{ JSONException -> 0x021d }
            r5.append(r3)     // Catch:{ JSONException -> 0x021d }
            java.lang.String r0 = r5.toString()     // Catch:{ JSONException -> 0x021d }
            r4.put(r13, r0)     // Catch:{ JSONException -> 0x021d }
            goto L_0x0217
        L_0x0215:
            r21 = r5
        L_0x0217:
            java.lang.String r0 = "uniNView"
            r14.put(r0, r4)     // Catch:{ JSONException -> 0x021d }
            goto L_0x0233
        L_0x021d:
            r0 = move-exception
            goto L_0x0229
        L_0x021f:
            r21 = r5
            goto L_0x0233
        L_0x0222:
            r0 = move-exception
            goto L_0x0227
        L_0x0224:
            r0 = move-exception
            r20 = r4
        L_0x0227:
            r21 = r5
        L_0x0229:
            r0.printStackTrace()
            goto L_0x0233
        L_0x022d:
            r20 = r4
            r21 = r5
            r14 = r19
        L_0x0233:
            io.dcloud.common.DHInterface.IMgr$MgrType r0 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr
            r3 = 4
            java.lang.Object[] r4 = new java.lang.Object[r3]
            r5 = 2
            java.lang.Integer r13 = java.lang.Integer.valueOf(r5)
            r18 = 0
            r4[r18] = r13
            r13 = 1
            r4[r13] = r12
            java.lang.Object[] r3 = new java.lang.Object[r5]
            r19 = r10[r13]
            r3[r18] = r19
            r3[r13] = r14
            r4[r5] = r3
            r3 = 3
            r4[r3] = r6
            java.lang.Object r0 = r9.processEvent(r0, r3, r4)
            io.dcloud.common.core.ui.b r0 = (io.dcloud.common.core.ui.b) r0
            io.dcloud.common.DHInterface.IWebview r3 = r0.obtainWebView()
            boolean r3 = r3.isUniWebView()
            if (r3 == 0) goto L_0x027f
            io.dcloud.common.DHInterface.IMgr$MgrType r3 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r4 = 4
            java.lang.Object[] r4 = new java.lang.Object[r4]
            io.dcloud.common.DHInterface.IWebview r5 = r0.obtainWebView()
            r19 = 0
            r4[r19] = r5
            java.lang.String r5 = "UI"
            r4[r13] = r5
            r5 = 2
            r4[r5] = r7
            r27 = r7
            r5 = 3
            r7 = 0
            r4[r5] = r7
            r9.processEvent(r3, r13, r4)
            goto L_0x0282
        L_0x027f:
            r27 = r7
            r7 = 0
        L_0x0282:
            io.dcloud.common.DHInterface.IWebview r3 = r0.obtainWebView()
            int r4 = android.os.Build.VERSION.SDK_INT
            r5 = 10
            if (r4 <= r5) goto L_0x029e
            if (r15 != 0) goto L_0x0296
            android.view.ViewGroup r4 = r3.obtainWindowView()
            r4.setLayerType(r13, r7)
            goto L_0x029e
        L_0x0296:
            android.view.ViewGroup r4 = r3.obtainWindowView()
            r5 = 0
            r4.setLayerType(r5, r7)
        L_0x029e:
            boolean r4 = r14.has(r8)
            if (r4 == 0) goto L_0x02ac
            java.lang.String r4 = r14.optString(r8)
            r3.setFrameId(r4)
            goto L_0x02af
        L_0x02ac:
            r3.setFrameId(r11)
        L_0x02af:
            r4 = r23
            r3.setWebViewCacheMode(r4)
            io.dcloud.common.adapter.util.ViewOptions r4 = r0.obtainFrameOptions()
            r4.name = r11
            io.dcloud.common.adapter.util.ViewOptions r4 = r0.obtainFrameOptions()
            r4.mUseHardwave = r15
            boolean r4 = io.dcloud.common.util.BaseInfo.isWap2AppAppid(r11)
            if (r4 == 0) goto L_0x0383
            java.lang.String r4 = "plusrequire"
            java.lang.String r4 = r3.getWebviewProperty(r4)
            java.lang.String r5 = "none"
            boolean r4 = r4.equals(r5)
            if (r4 != 0) goto L_0x02e8
            java.lang.String r4 = "_www/__wap2app.js"
            r7 = 0
            java.lang.String r4 = r12.convert2AbsFullPath(r7, r4)
            r3.appendPreloadJsFile(r4)
            java.lang.String r4 = "_www/__wap2appconfig.js"
            java.lang.String r4 = r12.convert2AbsFullPath(r7, r4)
            r3.appendPreloadJsFile(r4)
            goto L_0x02e9
        L_0x02e8:
            r7 = 0
        L_0x02e9:
            java.lang.String r4 = "_www/server_index_append.js"
            java.lang.String r4 = r12.convert2AbsFullPath(r7, r4)
            r3.setPreloadJsFile(r4, r13)
            java.lang.String r4 = "_www/server_index_append.css"
            java.lang.String r4 = r12.convert2AbsFullPath(r7, r4)
            java.io.File r5 = new java.io.File
            r5.<init>(r4)
            boolean r5 = r5.exists()
            if (r5 == 0) goto L_0x0307
            r3.setCssFile(r4, r7)
            goto L_0x032b
        L_0x0307:
            byte r5 = r12.obtainRunningAppMode()
            if (r5 != r13) goto L_0x0317
            boolean r5 = android.text.TextUtils.isEmpty(r4)
            if (r5 != 0) goto L_0x0317
            r3.setCssFile(r4, r7)
            goto L_0x032b
        L_0x0317:
            java.lang.String r4 = "_www/__wap2app.css"
            java.lang.String r4 = r12.convert2AbsFullPath(r7, r4)
            java.io.File r5 = new java.io.File
            r5.<init>(r4)
            boolean r5 = r5.exists()
            if (r5 == 0) goto L_0x032b
            r3.setCssFile(r4, r7)
        L_0x032b:
            java.lang.String r4 = "appendCss"
            boolean r5 = r14.has(r4)
            if (r5 == 0) goto L_0x033c
            java.lang.String r4 = r14.optString(r4)
            java.lang.String r4 = r12.convert2AbsFullPath(r7, r4)
            goto L_0x034e
        L_0x033c:
            java.lang.String r4 = "preloadcss"
            boolean r5 = r14.has(r4)
            if (r5 == 0) goto L_0x034d
            java.lang.String r4 = r14.optString(r4)
            java.lang.String r4 = r12.convert2AbsFullPath(r7, r4)
            goto L_0x034e
        L_0x034d:
            r4 = r7
        L_0x034e:
            boolean r5 = android.text.TextUtils.isEmpty(r4)
            if (r5 != 0) goto L_0x0357
            r3.setCssFile(r7, r4)
        L_0x0357:
            java.lang.String r4 = "appendJs"
            boolean r5 = r14.has(r4)
            if (r5 == 0) goto L_0x0368
            java.lang.String r4 = r14.optString(r4)
            java.lang.String r8 = r12.convert2AbsFullPath(r7, r4)
            goto L_0x037a
        L_0x0368:
            java.lang.String r4 = "preloadjs"
            boolean r5 = r14.has(r4)
            if (r5 == 0) goto L_0x0379
            java.lang.String r4 = r14.optString(r4)
            java.lang.String r8 = r12.convert2AbsFullPath(r7, r4)
            goto L_0x037a
        L_0x0379:
            r8 = r7
        L_0x037a:
            boolean r4 = android.text.TextUtils.isEmpty(r8)
            if (r4 != 0) goto L_0x0383
            r3.appendPreloadJsFile(r8)
        L_0x0383:
            r14 = r3
            r4 = 1
        L_0x0385:
            if (r1 == 0) goto L_0x03ac
            if (r20 == 0) goto L_0x03a9
            if (r2 == 0) goto L_0x03a9
            java.lang.String r3 = "isTab"
            boolean r2 = r2.optBoolean(r3)
            if (r2 == 0) goto L_0x0397
            r1.c(r0)
            goto L_0x03ac
        L_0x0397:
            boolean r2 = android.text.TextUtils.isEmpty(r21)
            if (r2 != 0) goto L_0x03ac
            r8 = r21
            boolean r2 = r1.checkPagePathIsTab(r8)
            if (r2 == 0) goto L_0x03ac
            r1.c(r0)
            goto L_0x03ac
        L_0x03a9:
            r1.c(r0)
        L_0x03ac:
            boolean r1 = r9.a((io.dcloud.common.DHInterface.IApp) r12)
            if (r1 == 0) goto L_0x03b5
            if (r4 == 0) goto L_0x0420
            goto L_0x03bb
        L_0x03b5:
            boolean r1 = r12.manifestBeParsed()
            if (r1 == 0) goto L_0x0420
        L_0x03bb:
            r1 = r25
            r2 = r26
            r3 = r6
            r4 = r11
            r5 = r0
            r15 = r6
            r6 = r12
            r8 = r27
            r7 = r16
            r24 = r8
            r8 = r14
            r1.a((int) r2, (io.dcloud.common.core.ui.a) r3, (java.lang.String) r4, (io.dcloud.common.core.ui.b) r5, (io.dcloud.common.DHInterface.IApp) r6, (java.lang.String) r7, (io.dcloud.common.DHInterface.IWebview) r8)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "load "
            r1.append(r2)
            r1.append(r11)
            java.lang.String r2 = " launchPage ="
            r1.append(r2)
            r2 = r10[r13]
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "Main_Path"
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r2, (java.lang.String) r1)
            r1 = r10[r13]
            java.lang.String r2 = "about:blank"
            boolean r1 = r2.equals(r1)
            if (r1 == 0) goto L_0x0404
            java.lang.String r1 = "<html><head><meta charset=\"utf-8\"></head><body></body><html>"
            java.lang.String r2 = "text/html"
            java.lang.String r3 = "utf-8"
            r4 = r24
            r14.loadContentData(r4, r1, r2, r3)
            goto L_0x040d
        L_0x0404:
            r1 = r10[r13]
            java.lang.String r1 = java.lang.String.valueOf(r1)
            r14.loadUrl(r1)
        L_0x040d:
            java.lang.String r1 = "splashscreen"
            java.lang.String r1 = r12.obtainConfigProperty(r1)
            boolean r1 = java.lang.Boolean.parseBoolean(r1)
            if (r1 == 0) goto L_0x041c
            r0.s = r13
            goto L_0x0421
        L_0x041c:
            r1 = 0
            r0.s = r1
            goto L_0x0421
        L_0x0420:
            r15 = r6
        L_0x0421:
            r9.b((io.dcloud.common.DHInterface.IApp) r12, (io.dcloud.common.DHInterface.IWebview) r14)
            io.dcloud.common.core.ui.b r0 = r15.e
            if (r0 != 0) goto L_0x042e
            r9.a((io.dcloud.common.DHInterface.IApp) r12, (io.dcloud.common.DHInterface.IWebview) r14)
            goto L_0x042e
        L_0x042c:
            boolean r0 = r0 instanceof java.lang.String
        L_0x042e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.l.a(int, java.lang.Object):void");
    }

    /* access modifiers changed from: package-private */
    public void b(IApp iApp, IWebview iWebview) {
        if (iApp.obtainThridInfo(IApp.ConfigProperty.ThridInfo.SecondWebviewJsonData) != null || (BaseInfo.isWap2AppAppid(iApp.obtainAppId()) && !TextUtils.isEmpty(iApp.getOriginalDirectPage()))) {
            processEvent(IMgr.MgrType.FeatureMgr, 1, new Object[]{iWebview, "UI", "n_createSecondWebview", null});
        }
    }

    private b b(IApp iApp) {
        a aVar = (a) iApp.obtainWebAppRootView();
        if (aVar != null) {
            return aVar.i();
        }
        return null;
    }

    private a b() {
        return this.a.get(String.valueOf(processEvent(IMgr.MgrType.AppMgr, 11, (Object) null)));
    }

    public void b(b bVar) {
        IApp obtainApp = bVar.obtainApp();
        obtainApp.setMaskLayer(false);
        obtainApp.obtainWebAppRootView().obtainMainView().invalidate();
    }

    private c b(IApp iApp, a aVar) {
        JSONObject obtainThridInfo = iApp.obtainThridInfo(IApp.ConfigProperty.ThridInfo.Tabbar);
        if (obtainThridInfo == null) {
            return null;
        }
        c cVar = new c(iApp.getActivity(), this, iApp, aVar, 8, obtainThridInfo);
        int i2 = iApp.getInt(0);
        int i3 = iApp.getInt(1);
        ViewOptions obtainFrameOptions = cVar.obtainFrameOptions();
        ViewOptions obtainFrameOptions2 = aVar.obtainFrameOptions();
        if (obtainFrameOptions2.height > i3) {
            obtainFrameOptions2.updateViewData(obtainFrameOptions2.mJsonViewOption, i2, i3);
        }
        obtainFrameOptions.setParentViewRect(obtainFrameOptions2);
        obtainFrameOptions.popGesture = iApp.getPopGesture();
        View obtainMainView = cVar.obtainMainView();
        obtainFrameOptions.width = -1;
        obtainFrameOptions.height = -1;
        AdaFrameItem.LayoutParamsUtil.setViewLayoutParams(obtainMainView, obtainFrameOptions.left, obtainFrameOptions.top, -1, -1);
        aVar.addFrameItem((AdaFrameItem) cVar, new ViewGroup.LayoutParams(-1, -1));
        cVar.p.e((b) cVar);
        processEvent(IMgr.MgrType.FeatureMgr, 1, new Object[]{cVar.obtainWebView(), "UI", "", null});
        return cVar;
    }

    public void a(IApp iApp, a aVar, String str, String str2, JSONObject jSONObject) {
        IApp iApp2 = iApp;
        JSONObject jSONObject2 = jSONObject;
        String optString = (jSONObject2 == null || !jSONObject2.has(AbsoluteConst.XML_PATH)) ? null : jSONObject2.optString(AbsoluteConst.XML_PATH);
        if (!PdrUtil.isEmpty(optString)) {
            iApp2.setConfigProperty(AbsoluteConst.UNIAPP_WEEX_JS_SERVICE, String.valueOf(true));
            int i2 = iApp2.getInt(0);
            int i3 = iApp2.getInt(1);
            b bVar = new b(iApp.getActivity(), this, iApp, aVar, 7, (Object) null);
            d dVar = r0;
            d dVar2 = new d(iApp.getActivity(), bVar, optString, str, jSONObject, true);
            dVar.initWebviewUUID(str);
            ViewOptions obtainFrameOptions = bVar.obtainFrameOptions();
            ViewOptions obtainFrameOptions2 = aVar.obtainFrameOptions();
            if (obtainFrameOptions2.height > i3) {
                obtainFrameOptions2.updateViewData(obtainFrameOptions2.mJsonViewOption, i2, i3);
            }
            obtainFrameOptions.setParentViewRect(obtainFrameOptions2);
            obtainFrameOptions.popGesture = iApp.getPopGesture();
            View obtainMainView = bVar.obtainMainView();
            int i4 = obtainFrameOptions.width;
            if (i4 == i2) {
                i4 = -1;
            }
            int i5 = obtainFrameOptions.height;
            if (i5 == i3) {
                i5 = -1;
            }
            AdaFrameItem.LayoutParamsUtil.setViewLayoutParams(obtainMainView, obtainFrameOptions.left, obtainFrameOptions.top, i4, i5);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, -1);
            bVar.addFrameItem((AdaFrameItem) bVar.obtainWebviewParent(), layoutParams);
            bVar.setVisible(false, false);
            aVar.addFrameItem((AdaFrameItem) bVar, layoutParams);
            dVar.setFrameId(str2);
            bVar.p.e(bVar);
            processEvent(IMgr.MgrType.FeatureMgr, 1, new Object[]{dVar, "UI", "", null});
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v5, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(io.dcloud.common.DHInterface.IApp r7, android.view.ViewGroup r8) {
        /*
            r6 = this;
            boolean r0 = io.dcloud.common.util.BaseInfo.isUniNViewBackgroud()
            if (r0 == 0) goto L_0x0069
            boolean r0 = io.dcloud.common.util.BaseInfo.isWeexUniJs(r7)
            if (r0 != 0) goto L_0x0069
            io.dcloud.common.DHInterface.IMgr$MgrType r0 = io.dcloud.common.DHInterface.IMgr.MgrType.AppMgr
            r1 = 24
            r2 = 0
            java.lang.Object r0 = r6.processEvent(r0, r1, r2)
            org.json.JSONObject r1 = new org.json.JSONObject
            r1.<init>()
            java.lang.String r2 = "template"
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ JSONException -> 0x003e }
            r1.put(r2, r0)     // Catch:{ JSONException -> 0x003e }
            java.lang.String r0 = "path"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x003e }
            r2.<init>()     // Catch:{ JSONException -> 0x003e }
            java.lang.String r3 = r7.obtainAppDataPath()     // Catch:{ JSONException -> 0x003e }
            r2.append(r3)     // Catch:{ JSONException -> 0x003e }
            java.lang.String r3 = "nvue_service.js"
            r2.append(r3)     // Catch:{ JSONException -> 0x003e }
            java.lang.String r2 = r2.toString()     // Catch:{ JSONException -> 0x003e }
            r1.put(r0, r2)     // Catch:{ JSONException -> 0x003e }
            goto L_0x0042
        L_0x003e:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0042:
            r0 = 4
            java.lang.Object[] r2 = new java.lang.Object[r0]
            r3 = 0
            r2[r3] = r7
            r4 = 1
            r2[r4] = r1
            r1 = 2
            r2[r1] = r8
            r8 = 3
            java.lang.String r5 = "__uniapp__nvue"
            r2[r8] = r5
            io.dcloud.common.DHInterface.IMgr$MgrType r5 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r0[r3] = r7
            java.lang.String r7 = "weex,io.dcloud.feature.weex.WeexFeature"
            r0[r4] = r7
            java.lang.String r7 = "createServiceUniNView"
            r0[r1] = r7
            r0[r8] = r2
            r7 = 10
            r6.processEvent(r5, r7, r0)
        L_0x0069:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.l.a(io.dcloud.common.DHInterface.IApp, android.view.ViewGroup):void");
    }

    /* access modifiers changed from: private */
    public boolean a(IApp iApp) {
        return !TextUtils.isEmpty(iApp.getOriginalDirectPage()) && !iApp.obtainWebAppIntent().hasExtra(IntentConst.DIRECT_PAGE);
    }

    private void a(int i2, a aVar, String str, b bVar, IApp iApp, String str2, IWebview iWebview) {
        boolean z;
        b bVar2;
        a aVar2;
        a aVar3 = aVar;
        b bVar3 = bVar;
        IApp iApp2 = iApp;
        IWebview iWebview2 = iWebview;
        IWebviewStateListener obtainLaunchPageStateListener = iApp.obtainLaunchPageStateListener();
        boolean z2 = false;
        if (obtainLaunchPageStateListener != null) {
            boolean parseBoolean = PdrUtil.parseBoolean(String.valueOf(obtainLaunchPageStateListener.onCallBack(-1, iWebview2)), true, false);
            iWebview2.addStateListener(iApp.obtainLaunchPageStateListener());
            z = parseBoolean;
        } else {
            z = true;
        }
        int parseInt = Integer.parseInt(iApp2.obtainConfigProperty(IApp.ConfigProperty.CONFIG_DELAY));
        boolean parseBoolean2 = Boolean.parseBoolean(iApp2.obtainConfigProperty(IApp.ConfigProperty.CONFIG_AUTOCLOSE));
        long currentTimeMillis = System.currentTimeMillis();
        boolean z3 = BaseInfo.isWap2AppAppid(str) && Boolean.parseBoolean(iApp2.obtainConfigProperty("w2a_autoclose"));
        Intent obtainWebAppIntent = iApp.obtainWebAppIntent();
        String obtainConfigProperty = iApp2.obtainConfigProperty(IApp.ConfigProperty.CONFIG_TARGET);
        if (TextUtils.isEmpty(obtainConfigProperty)) {
            obtainConfigProperty = "default";
        }
        if (parseBoolean2 || z3) {
            z2 = true;
        }
        int intExtra = obtainWebAppIntent.getIntExtra(IntentConst.FROM_STREAM_OPEN_TIMEOUT, 6000);
        boolean booleanExtra = obtainWebAppIntent.getBooleanExtra(IntentConst.FROM_STREAM_OPEN_AUTOCLOSE, z2);
        int i3 = (!obtainConfigProperty.startsWith("id:") || !booleanExtra) ? intExtra : ADSim.INTISPLSH;
        int parseInt2 = z3 ? Integer.parseInt(iApp2.obtainConfigProperty("w2a_delay")) : parseInt;
        if (!BaseInfo.isWap2AppAppid(str) || !PdrUtil.isNetPath(str2)) {
            this.c = AbsoluteConst.EVENTS_LOADED;
        } else {
            this.c = AbsoluteConst.EVENTS_RENDERING;
        }
        String obtainConfigProperty2 = iApp2.obtainConfigProperty("event");
        if (!TextUtils.isEmpty(obtainConfigProperty2)) {
            this.c = obtainConfigProperty2;
        }
        Logger.d(Logger.MAIN_TAG, "_need_auto_close_splash = " + parseBoolean2 + ";_delay=" + parseInt + ";appid=" + str + ";f_event=" + this.c);
        i iVar = r0;
        int i4 = parseInt2;
        int i5 = i3;
        i iVar2 = new i(obtainConfigProperty, booleanExtra, iApp, aVar, str2, iWebview, i2, bVar, parseInt2, currentTimeMillis);
        iWebview2.addStateListener(iVar);
        if (booleanExtra) {
            aVar2 = aVar;
            bVar2 = bVar;
            a(i5, aVar2, bVar2, i4);
        } else {
            aVar2 = aVar;
            bVar2 = bVar;
        }
        if (z && !bVar2.isChildOfFrameView) {
            aVar2.e(bVar2);
        }
    }

    private void a(int i2, a aVar, b bVar, int i3) {
        if (this.d != null) {
            aVar.obtainMainView().removeCallbacks(this.d);
        }
        this.d = new j(aVar, bVar, i3);
        aVar.obtainMainView().postDelayed(this.d, (long) i2);
    }

    private void a(a aVar) {
        if (this.d != null && aVar != null) {
            aVar.obtainMainView().removeCallbacks(this.d);
            this.d = null;
        }
    }

    /* access modifiers changed from: package-private */
    public void a(IApp iApp, IWebview iWebview, JSONObject jSONObject) {
        JSONArray jSONArray = new JSONArray();
        try {
            jSONArray.put(0, (Object) null);
            jSONArray.put(1, (Object) null);
            JSONArray jSONArray2 = new JSONArray();
            jSONArray2.put(0, (Object) null);
            jSONArray.put(2, jSONArray2);
            jSONArray.put(3, jSONObject);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        processEvent(IMgr.MgrType.FeatureMgr, 1, new Object[]{iWebview, "UI", "n_createHDWebview", jSONArray});
    }

    /* access modifiers changed from: package-private */
    public void a(IApp iApp, IWebview iWebview) {
        if (BaseInfo.isWap2AppAppid(iApp.obtainAppId()) && iApp.obtainWebAppIntent().hasExtra(IntentConst.DIRECT_PAGE)) {
            processEvent(IMgr.MgrType.FeatureMgr, 1, new Object[]{iWebview, "UI", "n_createDirectWebview", null});
        }
    }

    /* access modifiers changed from: private */
    public void a(IApp iApp, a aVar) {
        b i2;
        if (aVar != null && !aVar.q && (i2 = aVar.i()) != null) {
            k kVar = new k(i2, aVar, iApp);
            Runnable runnable = this.e;
            if (runnable != null) {
                this.f = true;
                MessageHandler.removeCallbacks(runnable);
            }
            MessageHandler.postDelayed(kVar, 100);
        }
    }

    public void a(IWebview iWebview, IApp iApp, boolean z, a aVar, int i2, b bVar, int i3, int i4) {
        C0019l lVar = new C0019l(aVar, z, bVar, iWebview, iApp, i3, i4, i2);
        this.e = lVar;
        MessageHandler.postDelayed(lVar, 100);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v1, resolved type: io.dcloud.common.core.ui.d} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v2, resolved type: io.dcloud.common.core.ui.e} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v36, resolved type: io.dcloud.common.core.ui.e} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v3, resolved type: io.dcloud.common.core.ui.d} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v10, resolved type: io.dcloud.common.adapter.ui.AdaWebview} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v11, resolved type: io.dcloud.common.core.ui.d} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r22v1, resolved type: io.dcloud.common.core.ui.d} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v12, resolved type: io.dcloud.common.core.ui.d} */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:180:0x0413 A[LOOP:0: B:178:0x040d->B:180:0x0413, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:184:0x0430  */
    /* JADX WARNING: Removed duplicated region for block: B:186:0x0437  */
    /* JADX WARNING: Removed duplicated region for block: B:195:0x048b  */
    /* JADX WARNING: Removed duplicated region for block: B:226:0x0527  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00b0  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00b8  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00c9  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0112  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x011c  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x013a  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0143  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x01b3  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x01bc  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x01c4  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x01f0  */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x01f6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public io.dcloud.common.core.ui.b a(int r30, io.dcloud.common.DHInterface.IApp r31, io.dcloud.common.core.ui.a r32, io.dcloud.common.core.ui.b r33, io.dcloud.common.DHInterface.IEventCallback r34, java.lang.Object[] r35, io.dcloud.common.DHInterface.IDCloudWebviewClientListener r36) {
        /*
            r29 = this;
            r10 = r29
            r7 = r30
            r8 = r31
            r9 = r32
            r11 = r35
            r12 = r36
            java.lang.String r13 = "createFrameView"
            io.dcloud.common.util.TestUtil.record((java.lang.String) r13, (java.lang.String) r13)
            r14 = 0
            r0 = r11[r14]
            java.lang.String r15 = java.lang.String.valueOf(r0)
            java.lang.String r0 = "Layout_Path"
            java.lang.String r1 = "WindowMgr createWindow"
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r0, (java.lang.String) r1)
            int r6 = r8.getInt(r14)
            r5 = 1
            int r4 = r8.getInt(r5)
            r3 = 5
            r2 = 4
            r1 = 0
            r0 = 2
            if (r7 != r0) goto L_0x0031
            io.dcloud.common.core.ui.b r0 = r9.f
            goto L_0x003c
        L_0x0031:
            if (r7 != r2) goto L_0x0036
            io.dcloud.common.core.ui.b r0 = r9.g
            goto L_0x003c
        L_0x0036:
            if (r7 != r3) goto L_0x003b
            io.dcloud.common.core.ui.b r0 = r9.e
            goto L_0x003c
        L_0x003b:
            r0 = r1
        L_0x003c:
            if (r0 != 0) goto L_0x0080
            io.dcloud.common.core.ui.b r0 = new io.dcloud.common.core.ui.b
            android.app.Activity r16 = r31.getActivity()
            r17 = 0
            r18 = r0
            r14 = 2
            r1 = r16
            r2 = r29
            r3 = r31
            r20 = r4
            r4 = r32
            r5 = r30
            r21 = r6
            r6 = r17
            r0.<init>(r1, r2, r3, r4, r5, r6)
            if (r7 != r14) goto L_0x0065
            r0 = r18
            r9.f = r0
            r1 = 4
        L_0x0063:
            r2 = 5
            goto L_0x0090
        L_0x0065:
            r0 = r18
            r1 = 4
            if (r7 != r1) goto L_0x0078
            r9.g = r0
            io.dcloud.common.core.ui.b r2 = r9.e
            if (r2 == 0) goto L_0x0063
            long r3 = r0.lastShowTime
            r5 = 1
            long r3 = r3 + r5
            r2.lastShowTime = r3
            goto L_0x0063
        L_0x0078:
            r2 = 5
            if (r7 != r2) goto L_0x0090
            r9.e = r0
            r6 = r0
            r5 = 1
            goto L_0x0092
        L_0x0080:
            r20 = r4
            r21 = r6
            r1 = 4
            r2 = 5
            r14 = 2
            if (r7 != r14) goto L_0x0090
            boolean r3 = r10.a((io.dcloud.common.DHInterface.IApp) r8)
            if (r3 == 0) goto L_0x0090
            return r0
        L_0x0090:
            r6 = r0
            r5 = 0
        L_0x0092:
            io.dcloud.common.adapter.util.ViewOptions r0 = r6.obtainFrameOptions()
            android.content.Context r3 = r29.getContext()
            android.content.res.Resources r3 = r3.getResources()
            android.util.DisplayMetrics r3 = r3.getDisplayMetrics()
            float r3 = r3.density
            r0.mWebviewScale = r3
            io.dcloud.common.adapter.util.ViewOptions r3 = r32.obtainFrameOptions()
            int r4 = r3.height
            r1 = r20
            if (r4 <= r1) goto L_0x00b8
            org.json.JSONObject r4 = r3.mJsonViewOption
            r2 = r21
            r3.updateViewData(r4, r2, r1)
            goto L_0x00ba
        L_0x00b8:
            r2 = r21
        L_0x00ba:
            r0.setParentViewRect(r3)
            java.lang.String r3 = r31.getPopGesture()
            r0.popGesture = r3
            int r3 = r11.length
            java.lang.String r4 = ""
            r14 = 1
            if (r3 <= r14) goto L_0x011c
            r3 = r11[r14]
            org.json.JSONObject r3 = (org.json.JSONObject) r3
            r0.updateViewData((org.json.JSONObject) r3)
            if (r3 == 0) goto L_0x0108
            java.lang.String r14 = "render"
            boolean r17 = r3.has(r14)
            if (r17 == 0) goto L_0x00ec
            r17 = r4
            java.lang.String r4 = "onscreen"
            java.lang.String r4 = r3.optString(r14, r4)
            java.lang.String r14 = "always"
            boolean r4 = io.dcloud.common.util.PdrUtil.isEquals(r4, r14)
            r6.setNeedRender(r4)
            goto L_0x00ee
        L_0x00ec:
            r17 = r4
        L_0x00ee:
            java.lang.String r4 = "name"
            boolean r14 = r3.has(r4)
            if (r14 == 0) goto L_0x00fb
            java.lang.String r4 = r3.optString(r4)
            goto L_0x010c
        L_0x00fb:
            java.lang.String r4 = "id"
            boolean r14 = r3.has(r4)
            if (r14 == 0) goto L_0x010a
            java.lang.String r4 = r3.optString(r4)
            goto L_0x010c
        L_0x0108:
            r17 = r4
        L_0x010a:
            r4 = r17
        L_0x010c:
            int r14 = r11.length
            r18 = r3
            r3 = 2
            if (r14 <= r3) goto L_0x0118
            r14 = r11[r3]
            java.lang.String r14 = (java.lang.String) r14
            r17 = r14
        L_0x0118:
            r14 = r4
            r4 = r18
            goto L_0x0126
        L_0x011c:
            r17 = r4
            r3 = 2
            r0.width = r2
            r0.height = r1
            r14 = r17
            r4 = 0
        L_0x0126:
            if (r7 != r3) goto L_0x0132
            boolean r3 = android.text.TextUtils.isEmpty(r17)
            if (r3 == 0) goto L_0x0132
            java.lang.String r17 = r31.obtainAppId()
        L_0x0132:
            r3 = r17
            io.dcloud.common.DHInterface.IWebview r17 = r6.obtainWebView()
            if (r17 == 0) goto L_0x0143
            io.dcloud.common.DHInterface.IWebview r12 = r6.obtainWebView()
            io.dcloud.common.adapter.ui.AdaWebview r12 = (io.dcloud.common.adapter.ui.AdaWebview) r12
            r17 = r5
            goto L_0x01aa
        L_0x0143:
            org.json.JSONObject r9 = r0.mUniNViewJson
            r17 = r5
            if (r9 == 0) goto L_0x0194
            java.lang.String r5 = "path"
            boolean r9 = r9.has(r5)
            if (r9 == 0) goto L_0x0194
            org.json.JSONObject r9 = r0.mUniNViewJson
            java.lang.String r5 = r9.optString(r5)
            boolean r9 = android.text.TextUtils.isEmpty(r5)
            if (r9 != 0) goto L_0x017c
            java.lang.String r9 = ".js"
            boolean r12 = r5.endsWith(r9)
            if (r12 != 0) goto L_0x017c
            java.lang.String r12 = ".js?"
            boolean r12 = r5.contains(r12)
            if (r12 != 0) goto L_0x017c
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            r12.append(r5)
            r12.append(r9)
            java.lang.String r5 = r12.toString()
        L_0x017c:
            r25 = r5
            io.dcloud.common.core.ui.d r12 = new io.dcloud.common.core.ui.d
            android.app.Activity r23 = r31.getActivity()
            org.json.JSONObject r5 = r0.mUniNViewJson
            r28 = 0
            r22 = r12
            r24 = r6
            r26 = r3
            r27 = r5
            r22.<init>(r23, r24, r25, r26, r27, r28)
            goto L_0x01aa
        L_0x0194:
            if (r12 == 0) goto L_0x01a1
            io.dcloud.common.core.ui.e r5 = new io.dcloud.common.core.ui.e
            android.app.Activity r9 = r31.getActivity()
            r5.<init>(r9, r10, r6, r12)
            r12 = r5
            goto L_0x01aa
        L_0x01a1:
            io.dcloud.common.core.ui.e r12 = new io.dcloud.common.core.ui.e
            android.app.Activity r5 = r31.getActivity()
            r12.<init>(r5, r10, r6)
        L_0x01aa:
            r12.setOriginalUrl(r15)
            boolean r5 = android.text.TextUtils.isEmpty(r3)
            if (r5 != 0) goto L_0x01b6
            r12.initWebviewUUID(r3)
        L_0x01b6:
            boolean r3 = io.dcloud.common.util.BaseInfo.isUniAppAppid(r31)
            if (r3 == 0) goto L_0x01c0
            r3 = 0
            r12.setIWebViewFocusable(r3)
        L_0x01c0:
            org.json.JSONObject r3 = r0.mPullToRefresh
            if (r3 == 0) goto L_0x01c9
            java.lang.String r5 = "pull_down_refresh"
            r12.setWebViewEvent(r5, r3)
        L_0x01c9:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "createWindow before _url="
            r3.append(r5)
            r3.append(r15)
            java.lang.String r3 = r3.toString()
            io.dcloud.common.adapter.util.Logger.e(r3)
            boolean r3 = r31.manifestBeParsed()
            if (r3 != 0) goto L_0x01f6
            r3 = 2
            if (r7 != r3) goto L_0x01ed
            boolean r3 = r10.a((io.dcloud.common.DHInterface.IApp) r8)
            if (r3 == 0) goto L_0x01ed
            goto L_0x01f6
        L_0x01ed:
            r3 = 5
            if (r7 == r3) goto L_0x01f6
            java.lang.String r0 = "createWindow not manifestBeParsed"
            io.dcloud.common.adapter.util.Logger.e(r0)
            return r6
        L_0x01f6:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "createWindow after _url="
            r3.append(r5)
            r3.append(r15)
            java.lang.String r3 = r3.toString()
            io.dcloud.common.adapter.util.Logger.e(r3)
            int r3 = r6.getFrameType()
            r5 = 2
            if (r3 != r5) goto L_0x022a
            java.lang.String r3 = "launch_is_statusbar"
            java.lang.String r3 = r8.obtainConfigProperty(r3)
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r3)
            boolean r3 = r3.booleanValue()
            r0.isStatusbar = r3
            java.lang.String r3 = "launch_statusbar_color"
            java.lang.String r3 = r8.obtainConfigProperty(r3)
            r0.mStatusbarColor = r3
            goto L_0x0249
        L_0x022a:
            int r3 = r6.getFrameType()
            r5 = 4
            if (r3 != r5) goto L_0x0249
            java.lang.String r3 = "second_is_statusbar"
            java.lang.String r3 = r8.obtainConfigProperty(r3)
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r3)
            boolean r3 = r3.booleanValue()
            r0.isStatusbar = r3
            java.lang.String r3 = "second_statusbar_color"
            java.lang.String r3 = r8.obtainConfigProperty(r3)
            r0.mStatusbarColor = r3
        L_0x0249:
            r10.a((io.dcloud.common.core.ui.b) r6, (java.lang.Object[]) r11)
            int r3 = android.os.Build.VERSION.SDK_INT
            r5 = 10
            if (r3 <= r5) goto L_0x026a
            boolean r3 = r0.mUseHardwave
            if (r3 != 0) goto L_0x0260
            android.view.ViewGroup r3 = r12.obtainWindowView()
            r5 = 0
            r9 = 1
            r3.setLayerType(r9, r5)
            goto L_0x026b
        L_0x0260:
            r5 = 0
            android.view.ViewGroup r3 = r12.obtainWindowView()
            r9 = 0
            r3.setLayerType(r9, r5)
            goto L_0x026b
        L_0x026a:
            r5 = 0
        L_0x026b:
            java.lang.String r3 = r0.mCacheMode
            r12.setWebViewCacheMode(r3)
            r12.init()
            r3 = r34
            r6.addFrameViewListener(r3)
            io.dcloud.common.adapter.util.ViewOptions r3 = r32.obtainFrameOptions()
            io.dcloud.common.adapter.util.ViewOptions r3 = io.dcloud.common.adapter.util.ViewOptions.createViewOptionsData(r0, r3)
            r6.setFrameOptions_Birth(r3)
            io.dcloud.common.adapter.ui.AdaWebViewParent r3 = r6.obtainWebviewParent()
            java.lang.String r9 = "blockNetworkImage"
            java.lang.String r11 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r4, (java.lang.String) r9)
            r12.setWebviewProperty(r9, r11)
            java.lang.String r9 = "shareable"
            java.lang.String r9 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r4, (java.lang.String) r9)
            java.lang.String r11 = "shareable"
            r12.setWebviewProperty(r11, r9)
            if (r4 == 0) goto L_0x02b2
            java.lang.String r9 = "visible"
            r11 = 1
            boolean r9 = r4.optBoolean(r9, r11)
            android.view.View r11 = r6.obtainMainView()
            if (r9 == 0) goto L_0x02ad
            r9 = 0
            goto L_0x02af
        L_0x02ad:
            r9 = 8
        L_0x02af:
            r11.setVisibility(r9)
        L_0x02b2:
            java.lang.String r9 = "injection"
            java.lang.String r9 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r4, (java.lang.String) r9)
            java.lang.String r11 = "injection"
            r12.setWebviewProperty(r11, r9)
            java.lang.String r9 = "videoFullscreen"
            java.lang.String r9 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r4, (java.lang.String) r9)
            java.lang.String r11 = "videoFullscreen"
            r12.setWebviewProperty(r11, r9)
            int r9 = r6.getFrameType()
            java.lang.String r11 = "all"
            java.lang.String r5 = "horizontal"
            java.lang.String r7 = "vertical"
            r18 = r14
            java.lang.String r14 = "bounce"
            r19 = r13
            java.lang.String r13 = "additionalHttpHeaders"
            r8 = 2
            if (r9 != r8) goto L_0x033d
            io.dcloud.common.DHInterface.IApp r4 = r6.obtainApp()
            io.dcloud.common.DHInterface.IApp$ConfigProperty$ThridInfo r8 = io.dcloud.common.DHInterface.IApp.ConfigProperty.ThridInfo.TitleNViewJsonData
            org.json.JSONObject r4 = r4.obtainThridInfo(r8)
            r0.setTitleNView(r4, r12)
            io.dcloud.common.DHInterface.IApp r4 = r6.obtainApp()
            io.dcloud.common.DHInterface.IApp$ConfigProperty$ThridInfo r8 = io.dcloud.common.DHInterface.IApp.ConfigProperty.ThridInfo.LaunchWebviewJsonData
            org.json.JSONObject r4 = r4.obtainThridInfo(r8)
            if (r4 == 0) goto L_0x0337
            r0.setBackButtonAutoControl(r4)
            boolean r8 = r4.has(r13)
            if (r8 == 0) goto L_0x0307
            org.json.JSONObject r8 = r4.optJSONObject(r13)
            goto L_0x0308
        L_0x0307:
            r8 = 0
        L_0x0308:
            java.lang.String r9 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r4, (java.lang.String) r14)
            boolean r7 = r7.equalsIgnoreCase(r9)
            if (r7 != 0) goto L_0x0323
            boolean r5 = r5.equalsIgnoreCase(r9)
            if (r5 != 0) goto L_0x0323
            boolean r5 = r11.equalsIgnoreCase(r9)
            if (r5 == 0) goto L_0x031f
            goto L_0x0323
        L_0x031f:
            r5 = 0
            r0.mBounce = r5
            goto L_0x0326
        L_0x0323:
            r5 = 1
            r0.mBounce = r5
        L_0x0326:
            java.lang.String r5 = r0.historyBack
            java.lang.String r7 = "historyBack"
            java.lang.String r4 = r4.optString(r7, r5)
            r0.historyBack = r4
            r20 = r1
            r21 = r2
            r1 = r8
            goto L_0x03ae
        L_0x0337:
            r20 = r1
            r21 = r2
            goto L_0x03ab
        L_0x033d:
            int r8 = r6.getFrameType()
            java.lang.String r9 = "navigationbar"
            r20 = r1
            java.lang.String r1 = "titleNView"
            r21 = r2
            r2 = 4
            if (r8 != r2) goto L_0x03b0
            io.dcloud.common.DHInterface.IApp r2 = r6.obtainApp()
            io.dcloud.common.DHInterface.IApp$ConfigProperty$ThridInfo r4 = io.dcloud.common.DHInterface.IApp.ConfigProperty.ThridInfo.SecondWebviewJsonData
            org.json.JSONObject r2 = r2.obtainThridInfo(r4)
            if (r2 == 0) goto L_0x03ab
            boolean r4 = r2.has(r1)
            if (r4 == 0) goto L_0x0366
            org.json.JSONObject r1 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r2, (java.lang.String) r1)
            r0.setTitleNView(r1, r12)
            goto L_0x0373
        L_0x0366:
            boolean r1 = r2.has(r9)
            if (r1 == 0) goto L_0x0373
            org.json.JSONObject r1 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r2, (java.lang.String) r9)
            r0.setTitleNView(r1, r12)
        L_0x0373:
            r0.setBackButtonAutoControl(r2)
            boolean r1 = r2.has(r13)
            if (r1 == 0) goto L_0x0381
            org.json.JSONObject r1 = r2.optJSONObject(r13)
            goto L_0x0382
        L_0x0381:
            r1 = 0
        L_0x0382:
            java.lang.String r9 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r2, (java.lang.String) r14)
            boolean r4 = r7.equalsIgnoreCase(r9)
            if (r4 != 0) goto L_0x039d
            boolean r4 = r5.equalsIgnoreCase(r9)
            if (r4 != 0) goto L_0x039d
            boolean r4 = r11.equalsIgnoreCase(r9)
            if (r4 == 0) goto L_0x0399
            goto L_0x039d
        L_0x0399:
            r4 = 0
            r0.mBounce = r4
            goto L_0x03a0
        L_0x039d:
            r4 = 1
            r0.mBounce = r4
        L_0x03a0:
            java.lang.String r4 = r0.historyBack
            java.lang.String r5 = "historyBack"
            java.lang.String r2 = r2.optString(r5, r4)
            r0.historyBack = r2
            goto L_0x03ae
        L_0x03ab:
            java.lang.String r9 = "none"
            r1 = 0
        L_0x03ae:
            r2 = 1
            goto L_0x03f8
        L_0x03b0:
            boolean r2 = r4.has(r1)
            if (r2 == 0) goto L_0x03be
            org.json.JSONObject r1 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r4, (java.lang.String) r1)
            r0.setTitleNView(r1, r12)
            goto L_0x03cb
        L_0x03be:
            boolean r1 = r4.has(r9)
            if (r1 == 0) goto L_0x03cb
            org.json.JSONObject r1 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r4, (java.lang.String) r9)
            r0.setTitleNView(r1, r12)
        L_0x03cb:
            boolean r1 = r4.has(r13)
            if (r1 == 0) goto L_0x03d6
            org.json.JSONObject r1 = r4.optJSONObject(r13)
            goto L_0x03d7
        L_0x03d6:
            r1 = 0
        L_0x03d7:
            r0.setBackButtonAutoControl(r4)
            java.lang.String r9 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r4, (java.lang.String) r14)
            boolean r2 = r7.equalsIgnoreCase(r9)
            if (r2 != 0) goto L_0x03f5
            boolean r2 = r5.equalsIgnoreCase(r9)
            if (r2 != 0) goto L_0x03f5
            boolean r2 = r11.equalsIgnoreCase(r9)
            if (r2 == 0) goto L_0x03f1
            goto L_0x03f5
        L_0x03f1:
            r2 = 0
            r0.mBounce = r2
            goto L_0x03ae
        L_0x03f5:
            r2 = 1
            r0.mBounce = r2
        L_0x03f8:
            if (r1 == 0) goto L_0x0424
            int r4 = r1.length()
            if (r4 <= 0) goto L_0x0424
            java.util.Iterator r4 = r1.keys()
            java.util.HashMap r5 = new java.util.HashMap
            int r7 = r1.length()
            r5.<init>(r7)
        L_0x040d:
            boolean r7 = r4.hasNext()
            if (r7 == 0) goto L_0x0421
            java.lang.Object r7 = r4.next()
            java.lang.String r7 = (java.lang.String) r7
            java.lang.String r8 = r1.optString(r7)
            r5.put(r7, r8)
            goto L_0x040d
        L_0x0421:
            r12.setLoadURLHeads(r15, r5)
        L_0x0424:
            r12.setWebviewProperty(r14, r9)
            r6.a((io.dcloud.common.adapter.util.ViewOptions) r0, (io.dcloud.common.adapter.ui.AdaFrameItem) r6, (io.dcloud.common.adapter.ui.AdaFrameItem) r3, (io.dcloud.common.adapter.ui.AdaFrameItem) r12)
            boolean r1 = r0.hasBackground()
            if (r1 == 0) goto L_0x0437
            r1 = 0
            r10.a((io.dcloud.common.core.ui.b) r6, (boolean) r1)
        L_0x0434:
            r3 = r31
            goto L_0x045f
        L_0x0437:
            r1 = 0
            android.view.View r3 = r6.obtainMainView()
            int r4 = r0.width
            r5 = -1
            r7 = r21
            if (r4 != r7) goto L_0x0444
            r4 = -1
        L_0x0444:
            int r7 = r0.height
            r8 = r20
            if (r7 != r8) goto L_0x044b
            r7 = -1
        L_0x044b:
            int r8 = r0.left
            int r0 = r0.top
            io.dcloud.common.adapter.ui.AdaFrameItem.LayoutParamsUtil.setViewLayoutParams(r3, r8, r0, r4, r7)
            android.view.ViewGroup$LayoutParams r0 = new android.view.ViewGroup$LayoutParams
            r0.<init>(r5, r5)
            io.dcloud.common.adapter.ui.AdaWebViewParent r3 = r6.obtainWebviewParent()
            r6.addFrameItem((io.dcloud.common.adapter.ui.AdaFrameItem) r3, (android.view.ViewGroup.LayoutParams) r0)
            goto L_0x0434
        L_0x045f:
            r10.a((io.dcloud.common.adapter.ui.AdaFrameItem) r6, (io.dcloud.common.DHInterface.IApp) r3)
            r0 = r33
            r10.a((io.dcloud.common.core.ui.b) r6, (io.dcloud.common.core.ui.b) r0)
            io.dcloud.common.util.SubNViewsUtil.initFrameSubNViews(r6)
            java.lang.String r0 = "winmgr"
            java.lang.String r4 = "createWindow end !"
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r0, (java.lang.String) r4)
            java.lang.String r0 = "createFrameView耗时："
            r4 = r19
            io.dcloud.common.util.TestUtil.print(r4, r0)
            java.lang.String r0 = "target"
            java.lang.String r0 = r3.obtainConfigProperty(r0)
            r4 = r30
            r7 = r17
            r5 = r18
            boolean r4 = r10.a((int) r4, (java.lang.String) r5, (java.lang.String) r0, (boolean) r7)
            if (r4 == 0) goto L_0x0527
            java.lang.String r4 = r31.obtainAppId()
            boolean r5 = io.dcloud.common.util.BaseInfo.isWap2AppAppid(r4)
            if (r5 == 0) goto L_0x04a0
            boolean r5 = io.dcloud.common.util.PdrUtil.isNetPath(r15)
            if (r5 == 0) goto L_0x04a0
            java.lang.String r5 = "rendering"
            r10.c = r5
            goto L_0x04a4
        L_0x04a0:
            java.lang.String r5 = "loaded"
            r10.c = r5
        L_0x04a4:
            java.lang.String r5 = "event"
            java.lang.String r5 = r3.obtainConfigProperty(r5)
            boolean r7 = android.text.TextUtils.isEmpty(r5)
            if (r7 != 0) goto L_0x04b2
            r10.c = r5
        L_0x04b2:
            java.lang.String r5 = "autoclose"
            java.lang.String r5 = r3.obtainConfigProperty(r5)
            boolean r4 = io.dcloud.common.util.BaseInfo.isWap2AppAppid(r4)
            if (r4 == 0) goto L_0x04cd
            java.lang.String r4 = "w2a_autoclose"
            java.lang.String r4 = r3.obtainConfigProperty(r4)
            boolean r4 = java.lang.Boolean.parseBoolean(r4)
            if (r4 == 0) goto L_0x04cd
            r4 = 1
            goto L_0x04ce
        L_0x04cd:
            r4 = 0
        L_0x04ce:
            boolean r5 = java.lang.Boolean.parseBoolean(r5)
            if (r5 != 0) goto L_0x04d9
            if (r4 == 0) goto L_0x04d7
            goto L_0x04d9
        L_0x04d7:
            r5 = 0
            goto L_0x04da
        L_0x04d9:
            r5 = 1
        L_0x04da:
            java.lang.String r1 = "delay"
            java.lang.String r1 = r3.obtainConfigProperty(r1)
            int r1 = java.lang.Integer.parseInt(r1)
            java.lang.String r2 = "w2a_delay"
            java.lang.String r2 = r3.obtainConfigProperty(r2)
            int r2 = java.lang.Integer.parseInt(r2)
            if (r4 == 0) goto L_0x04f3
            r9 = r2
            goto L_0x04f4
        L_0x04f3:
            r9 = r1
        L_0x04f4:
            java.lang.String r1 = "id:"
            boolean r7 = r0.startsWith(r1)
            if (r7 == 0) goto L_0x050f
            java.lang.String r1 = "id:*"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L_0x050f
            io.dcloud.common.core.ui.l$a r0 = new io.dcloud.common.core.ui.l$a
            r8 = r32
            r0.<init>(r6, r3, r8)
            r6.addFrameViewListener(r0)
            goto L_0x0511
        L_0x050f:
            r8 = r32
        L_0x0511:
            io.dcloud.common.core.ui.l$b r11 = new io.dcloud.common.core.ui.l$b
            r0 = r11
            r1 = r29
            r2 = r31
            r3 = r6
            r4 = r5
            r5 = r7
            r13 = r6
            r6 = r15
            r7 = r12
            r8 = r32
            r0.<init>(r2, r3, r4, r5, r6, r7, r8, r9)
            r12.addStateListener(r11)
            goto L_0x0528
        L_0x0527:
            r13 = r6
        L_0x0528:
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.l.a(int, io.dcloud.common.DHInterface.IApp, io.dcloud.common.core.ui.a, io.dcloud.common.core.ui.b, io.dcloud.common.DHInterface.IEventCallback, java.lang.Object[], io.dcloud.common.DHInterface.IDCloudWebviewClientListener):io.dcloud.common.core.ui.b");
    }

    private void a(b bVar, boolean z) {
        b bVar2 = bVar;
        int i2 = bVar.obtainApp().getInt(0);
        int i3 = bVar.obtainApp().getInt(1);
        AdaWebViewParent obtainWebviewParent = bVar.obtainWebviewParent();
        ViewOptions obtainFrameOptions = bVar.obtainFrameOptions();
        ViewOptions obtainFrameOptions2 = obtainWebviewParent.obtainFrameOptions();
        a aVar = (a) bVar.obtainWebAppRootView();
        ViewOptions obtainFrameOptions3 = aVar.obtainFrameOptions();
        obtainFrameOptions2.setParentViewRect(obtainFrameOptions3);
        obtainFrameOptions2.updateViewData((ViewRect) obtainFrameOptions);
        obtainFrameOptions.left = 0;
        obtainFrameOptions.top = 0;
        obtainFrameOptions.anim_top = 0;
        obtainFrameOptions.anim_left = 0;
        ViewHelper.setY(bVar.obtainMainView(), 0.0f);
        ViewHelper.setX(bVar.obtainMainView(), 0.0f);
        obtainFrameOptions.width = i2;
        obtainFrameOptions.height = i3;
        int i4 = obtainFrameOptions2.left;
        int i5 = obtainFrameOptions2.top;
        int i6 = obtainFrameOptions2.width;
        int i7 = obtainFrameOptions2.height;
        obtainWebviewParent.setFrameOptions_Birth(ViewOptions.createViewOptionsData(obtainFrameOptions2, obtainFrameOptions3, obtainFrameOptions2));
        obtainFrameOptions2.allowUpdate = false;
        obtainFrameOptions2.maskColor = obtainFrameOptions.maskColor;
        obtainWebviewParent.mNeedOrientationUpdate = true;
        ViewOptions viewOptions = obtainFrameOptions;
        int i8 = i7;
        int i9 = i6;
        viewOptions.checkValueIsPercentage("left", -1, -1, false, true);
        viewOptions.checkValueIsPercentage("top", -1, -1, false, true);
        viewOptions.checkValueIsPercentage("width", -1, -1, false, true);
        viewOptions.checkValueIsPercentage("height", -1, -1, false, true);
        int i10 = i5;
        int i11 = i4;
        if (a(i4, i10, i9, i8, aVar.obtainFrameOptions().width, aVar.obtainFrameOptions().height)) {
            Logger.d("winmgr", "createWindow use LayoutParams.MATCH_PARENT !");
            bVar2.addFrameItem((AdaFrameItem) bVar.obtainWebviewParent(), new ViewGroup.LayoutParams(-1, -1));
            return;
        }
        bVar2.addFrameItem((AdaFrameItem) bVar.obtainWebviewParent(), AdaFrameItem.LayoutParamsUtil.createLayoutParams(i11, i10, i9, i8));
        if (z) {
            bVar2.a(i2, i3);
            return;
        }
        int i12 = i11 + i9;
        if (i12 > i2 || i10 + i8 > i3) {
            StringBuilder sb = new StringBuilder();
            sb.append("updateLayoutParams allW=");
            sb.append(i12);
            sb.append(";pdrW=");
            sb.append(i2);
            sb.append(";pdrH=");
            sb.append(i3);
            sb.append(";allH=");
            int i13 = i10 + i8;
            sb.append(i13);
            Logger.d("winmgr", sb.toString());
            bVar2.a(Math.max(i12, i2), Math.max(i13, i3));
        }
    }

    private boolean a(int i2, String str, String str2, boolean z) {
        if (TextUtils.isEmpty(str2) || !str2.startsWith("id:") || PdrUtil.isEmpty(str)) {
            if (i2 == 4) {
                if (TextUtils.isEmpty(str2) || !str2.equals("second")) {
                    return false;
                }
                return true;
            } else if (i2 != 5 || !z) {
                return false;
            } else {
                return true;
            }
        } else if (str2.substring(3).equals(str)) {
            return true;
        } else {
            return false;
        }
    }

    private void a(AdaFrameItem adaFrameItem, IApp iApp) {
        int statusHeight;
        int i2;
        ViewOptions obtainFrameOptions = adaFrameItem.obtainFrameOptions();
        if (obtainFrameOptions.isStatusbar) {
            if ((PdrUtil.isEmpty(obtainFrameOptions.mStatusbarColor) || iApp.obtainStatusBarMgr().isImmersive) && -1 != (statusHeight = DeviceInfo.getStatusHeight(adaFrameItem.getContext()))) {
                int hashCode = adaFrameItem.hashCode();
                int statusBarDefaultColor = iApp.obtainStatusBarMgr().getStatusBarDefaultColor();
                if (!PdrUtil.isEmpty(obtainFrameOptions.mStatusbarColor)) {
                    try {
                        i2 = Color.parseColor(obtainFrameOptions.mStatusbarColor);
                    } catch (Exception unused) {
                        i2 = PdrUtil.stringToColor(obtainFrameOptions.mStatusbarColor);
                    }
                    if (PdrUtil.checkStatusbarColor(i2)) {
                        statusBarDefaultColor = i2;
                    }
                }
                ViewGroup viewGroup = (ViewGroup) adaFrameItem.obtainMainView();
                if (viewGroup.findViewById(hashCode) == null && obtainFrameOptions.height != 0) {
                    StatusBarView statusBarView = new StatusBarView(adaFrameItem.getContext());
                    statusBarView.setStatusBarHeight(statusHeight);
                    statusBarView.setBackgroundColor(statusBarDefaultColor);
                    statusBarView.setId(hashCode);
                    ViewGroup viewGroup2 = (ViewGroup) ((AdaFrameView) adaFrameItem).obtainWebviewParent().obtainMainView();
                    if (obtainFrameOptions.isStatusbarDodifyHeight) {
                        viewGroup.getLayoutParams().height = obtainFrameOptions.height + DeviceInfo.sStatusBarHeight;
                        viewGroup.addView(statusBarView);
                    } else {
                        viewGroup.addView(statusBarView);
                    }
                    JSONObject jSONObject = obtainFrameOptions.titleNView;
                    if (jSONObject == null || !TitleNViewUtil.isTitleTypeForDef(jSONObject)) {
                        viewGroup2.post(new c(adaFrameItem));
                    }
                }
            }
        }
    }

    private b a() {
        a b2 = b();
        if (b2 != null) {
            return b2.i();
        }
        return null;
    }

    public void a(b bVar) {
        bVar.a(b.e);
        bVar.q();
        bVar.p.b(bVar);
        if (bVar.f()) {
            processEvent(IMgr.MgrType.WindowMgr, 28, bVar.g);
            bVar.g = null;
        }
        bVar.s();
        bVar.j();
        bVar.n = false;
        bVar.m = false;
        bVar.inStack = false;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: org.json.JSONObject} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v1, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: org.json.JSONObject} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v2, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v2, resolved type: org.json.JSONObject} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v3, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v3, resolved type: org.json.JSONObject} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v6, resolved type: org.json.JSONObject} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v5, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v11, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v16, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v12, resolved type: org.json.JSONObject} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v14, resolved type: org.json.JSONObject} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v15, resolved type: org.json.JSONObject} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v16, resolved type: org.json.JSONObject} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v17, resolved type: org.json.JSONObject} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v18, resolved type: org.json.JSONObject} */
    /* JADX WARNING: type inference failed for: r2v5, types: [java.lang.String] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00c8  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(io.dcloud.common.core.ui.b r9, java.lang.Object[] r10) {
        /*
            r8 = this;
            io.dcloud.common.DHInterface.IApp r0 = r9.obtainApp()
            io.dcloud.common.util.AppStatusBarManager r0 = r0.obtainStatusBarMgr()
            boolean r0 = r0.isImmersive
            int r1 = r9.getFrameType()
            r2 = 0
            r3 = 2
            if (r1 != r3) goto L_0x002e
            io.dcloud.common.DHInterface.IApp r10 = r9.obtainApp()
            io.dcloud.common.DHInterface.IApp$ConfigProperty$ThridInfo r1 = io.dcloud.common.DHInterface.IApp.ConfigProperty.ThridInfo.TitleNViewJsonData
            org.json.JSONObject r2 = r10.obtainThridInfo(r1)
            io.dcloud.common.DHInterface.IWebview r10 = r9.obtainWebView()
            android.view.ViewGroup r10 = r10.obtainWindowView()
            int r10 = r10.hashCode()
            java.lang.String r10 = java.lang.String.valueOf(r10)
            goto L_0x00d1
        L_0x002e:
            int r1 = r9.getFrameType()
            r4 = 4
            java.lang.String r5 = "navigationbar"
            java.lang.String r6 = "titleNView"
            if (r1 != r4) goto L_0x006d
            io.dcloud.common.DHInterface.IWebview r10 = r9.obtainWebView()
            android.view.ViewGroup r10 = r10.obtainWindowView()
            int r10 = r10.hashCode()
            java.lang.String r10 = java.lang.String.valueOf(r10)
            io.dcloud.common.DHInterface.IApp r1 = r9.obtainApp()
            io.dcloud.common.DHInterface.IApp$ConfigProperty$ThridInfo r3 = io.dcloud.common.DHInterface.IApp.ConfigProperty.ThridInfo.SecondWebviewJsonData
            org.json.JSONObject r1 = r1.obtainThridInfo(r3)
            if (r1 == 0) goto L_0x00d1
            boolean r3 = r1.has(r6)
            if (r3 == 0) goto L_0x0061
            org.json.JSONObject r2 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r1, (java.lang.String) r6)
            goto L_0x00d1
        L_0x0061:
            boolean r3 = r1.has(r5)
            if (r3 == 0) goto L_0x00d1
            org.json.JSONObject r2 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r1, (java.lang.String) r5)
            goto L_0x00d1
        L_0x006d:
            int r1 = r9.getFrameType()
            r4 = 5
            r7 = 1
            if (r1 != r4) goto L_0x00a5
            io.dcloud.common.DHInterface.IWebview r1 = r9.obtainWebView()
            android.view.ViewGroup r1 = r1.obtainWindowView()
            int r1 = r1.hashCode()
            java.lang.String r1 = java.lang.String.valueOf(r1)
            int r3 = r10.length
            if (r3 <= r7) goto L_0x00a3
            r10 = r10[r7]
            org.json.JSONObject r10 = (org.json.JSONObject) r10
            if (r10 == 0) goto L_0x00a3
            boolean r3 = r10.has(r6)
            if (r3 == 0) goto L_0x0099
            org.json.JSONObject r2 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r10, (java.lang.String) r6)
            goto L_0x00a3
        L_0x0099:
            boolean r3 = r10.has(r5)
            if (r3 == 0) goto L_0x00a3
            org.json.JSONObject r2 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r10, (java.lang.String) r5)
        L_0x00a3:
            r10 = r1
            goto L_0x00d1
        L_0x00a5:
            int r1 = r10.length
            if (r1 <= r7) goto L_0x00d0
            r1 = r10[r7]
            org.json.JSONObject r1 = (org.json.JSONObject) r1
            if (r1 == 0) goto L_0x00c4
            boolean r4 = r1.has(r6)
            if (r4 == 0) goto L_0x00b9
            org.json.JSONObject r1 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r1, (java.lang.String) r6)
            goto L_0x00c5
        L_0x00b9:
            boolean r4 = r1.has(r5)
            if (r4 == 0) goto L_0x00c4
            org.json.JSONObject r1 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r1, (java.lang.String) r5)
            goto L_0x00c5
        L_0x00c4:
            r1 = r2
        L_0x00c5:
            int r4 = r10.length
            if (r4 <= r3) goto L_0x00cd
            r10 = r10[r3]
            r2 = r10
            java.lang.String r2 = (java.lang.String) r2
        L_0x00cd:
            r10 = r2
            r2 = r1
            goto L_0x00d1
        L_0x00d0:
            r10 = r2
        L_0x00d1:
            r8.a((io.dcloud.common.core.ui.b) r9, (boolean) r0, (org.json.JSONObject) r2, (java.lang.String) r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.l.a(io.dcloud.common.core.ui.b, java.lang.Object[]):void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v0, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0095 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00d2  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0117  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0136  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0142  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x015d  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x0180  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0199  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x01a5  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(io.dcloud.common.core.ui.b r17, boolean r18, org.json.JSONObject r19, java.lang.String r20) {
        /*
            r16 = this;
            r1 = r17
            r2 = r19
            java.lang.String r3 = "backgroundColor"
            java.lang.String r4 = "backgroundcolor"
            if (r2 == 0) goto L_0x01dd
            boolean r0 = android.text.TextUtils.isEmpty(r20)
            if (r0 != 0) goto L_0x01dd
            boolean r0 = r2.has(r4)     // Catch:{ JSONException -> 0x0039 }
            if (r0 == 0) goto L_0x0020
            java.lang.String r0 = r2.optString(r4)     // Catch:{ JSONException -> 0x0039 }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ JSONException -> 0x0039 }
            if (r0 == 0) goto L_0x003d
        L_0x0020:
            boolean r0 = r2.has(r3)     // Catch:{ JSONException -> 0x0039 }
            if (r0 == 0) goto L_0x0030
            java.lang.String r0 = r2.optString(r3)     // Catch:{ JSONException -> 0x0039 }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ JSONException -> 0x0039 }
            if (r0 == 0) goto L_0x003d
        L_0x0030:
            r2.remove(r4)     // Catch:{ JSONException -> 0x0039 }
            java.lang.String r0 = "#F7F7F7"
            r2.put(r3, r0)     // Catch:{ JSONException -> 0x0039 }
            goto L_0x003d
        L_0x0039:
            r0 = move-exception
            r0.printStackTrace()
        L_0x003d:
            java.lang.String r0 = r2.optString(r3)
            boolean r3 = android.text.TextUtils.isEmpty(r0)
            if (r3 == 0) goto L_0x004b
            java.lang.String r0 = r2.optString(r4)
        L_0x004b:
            r3 = r0
            java.lang.String r0 = "type"
            java.lang.String r4 = r2.optString(r0)
            java.lang.String r5 = "transparent"
            boolean r4 = r5.equals(r4)
            java.lang.String r5 = "absolute"
            if (r4 == 0) goto L_0x006e
            boolean r0 = android.text.TextUtils.isEmpty(r3)
            if (r0 != 0) goto L_0x007a
            r0 = 0
            java.lang.String r0 = io.dcloud.common.util.TitleNViewUtil.changeColorAlpha((java.lang.String) r3, (float) r0)     // Catch:{ Exception -> 0x0068 }
            goto L_0x0084
        L_0x0068:
            r0 = move-exception
            r4 = r0
            r4.printStackTrace()
            goto L_0x007a
        L_0x006e:
            java.lang.String r0 = r2.optString(r0)
            java.lang.String r4 = "float"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x007c
        L_0x007a:
            r0 = r3
            goto L_0x0084
        L_0x007c:
            r0 = 1065353216(0x3f800000, float:1.0)
            java.lang.String r0 = io.dcloud.common.util.TitleNViewUtil.changeColorAlpha((java.lang.String) r3, (float) r0)
            java.lang.String r5 = "dock"
        L_0x0084:
            android.content.Context r4 = r17.getContext()
            int r4 = io.dcloud.common.adapter.util.DeviceInfo.getStatusHeight(r4)
            io.dcloud.common.adapter.util.ViewOptions r6 = r17.obtainFrameOptions()
            boolean r6 = r6.isStatusbar
            r7 = 0
            if (r18 == 0) goto L_0x00a6
            if (r6 != 0) goto L_0x00a6
            r6 = -1
            if (r6 == r4) goto L_0x00a6
            float r4 = (float) r4
            io.dcloud.common.DHInterface.IWebview r6 = r17.obtainWebView()
            float r6 = r6.getScale()
            float r4 = r4 / r6
            int r4 = (int) r4
            goto L_0x00a7
        L_0x00a6:
            r4 = 0
        L_0x00a7:
            io.dcloud.common.DHInterface.IApp r6 = r17.obtainApp()
            io.dcloud.common.DHInterface.IApp$ConfigProperty$ThridInfo r8 = io.dcloud.common.DHInterface.IApp.ConfigProperty.ThridInfo.SecondWebviewJsonData
            org.json.JSONObject r6 = r6.obtainThridInfo(r8)
            int r8 = r17.getFrameType()
            r9 = 2
            if (r9 != r8) goto L_0x00c9
            if (r6 == 0) goto L_0x00c9
            java.lang.String r8 = "mode"
            java.lang.String r6 = r6.optString(r8)
            java.lang.String r8 = "parent"
            boolean r6 = r8.equals(r6)
            if (r6 == 0) goto L_0x00c9
            r4 = 0
        L_0x00c9:
            r6 = 0
            java.lang.String r8 = "tags"
            boolean r10 = r2.has(r8)
            if (r10 == 0) goto L_0x00d6
            org.json.JSONArray r6 = r2.optJSONArray(r8)
        L_0x00d6:
            io.dcloud.common.core.ui.l r8 = r1.mWindowMgr
            io.dcloud.common.DHInterface.IMgr$MgrType r10 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r11 = 10
            r12 = 4
            java.lang.Object[] r13 = new java.lang.Object[r12]
            io.dcloud.common.DHInterface.IWebview r14 = r17.obtainWebView()
            r13[r7] = r14
            java.lang.String r14 = "nativeobj"
            r15 = 1
            r13[r15] = r14
            java.lang.String r14 = "View"
            r13[r9] = r14
            r14 = 7
            java.lang.Object[] r14 = new java.lang.Object[r14]
            r14[r7] = r1
            io.dcloud.common.DHInterface.IWebview r1 = r17.obtainWebView()
            r14[r15] = r1
            r14[r9] = r20
            r1 = 3
            r14[r1] = r20
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r9 = "{'top':'0px','left':'0px','height':'"
            r7.append(r9)
            r9 = 44
            r7.append(r9)
            java.lang.String r9 = "px','width':'100%',"
            r7.append(r9)
            java.lang.String r9 = ""
            if (r4 <= 0) goto L_0x0136
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r15 = "'statusbar':{'background':'"
            r4.append(r15)
            r4.append(r0)
            java.lang.String r15 = "','backgroundnoalpha':'"
            r4.append(r15)
            r4.append(r3)
            java.lang.String r3 = "'},"
            r4.append(r3)
            java.lang.String r3 = r4.toString()
            goto L_0x0137
        L_0x0136:
            r3 = r9
        L_0x0137:
            r7.append(r3)
            java.lang.String r3 = "blurEffect"
            boolean r4 = r2.has(r3)
            if (r4 == 0) goto L_0x015d
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r15 = "'blurEffect':'"
            r4.append(r15)
            java.lang.String r3 = r2.optString(r3)
            r4.append(r3)
            java.lang.String r3 = "',"
            r4.append(r3)
            java.lang.String r3 = r4.toString()
            goto L_0x015e
        L_0x015d:
            r3 = r9
        L_0x015e:
            r7.append(r3)
            java.lang.String r3 = "'backgroundColor':'"
            r7.append(r3)
            r7.append(r0)
            java.lang.String r0 = "','position':'"
            r7.append(r0)
            r7.append(r5)
            java.lang.String r0 = "','dock':'top'"
            r7.append(r0)
            java.lang.String r0 = "backgroundImage"
            boolean r3 = r2.has(r0)
            java.lang.String r4 = "'"
            if (r3 == 0) goto L_0x0199
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = ",'backgroundImage':'"
            r3.append(r5)
            java.lang.String r0 = r2.optString(r0)
            r3.append(r0)
            r3.append(r4)
            java.lang.String r0 = r3.toString()
            goto L_0x019a
        L_0x0199:
            r0 = r9
        L_0x019a:
            r7.append(r0)
            java.lang.String r0 = "redDotColor"
            boolean r3 = r2.has(r0)
            if (r3 == 0) goto L_0x01bd
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = ",'redDotColor':'"
            r3.append(r5)
            java.lang.String r0 = r2.optString(r0)
            r3.append(r0)
            r3.append(r4)
            java.lang.String r9 = r3.toString()
        L_0x01bd:
            r7.append(r9)
            java.lang.String r0 = "}"
            r7.append(r0)
            java.lang.String r0 = r7.toString()
            org.json.JSONObject r0 = io.dcloud.common.util.JSONUtil.createJSONObject(r0)
            r14[r12] = r0
            r0 = 5
            r14[r0] = r6
            r0 = 6
            java.lang.String r2 = "TitleNView"
            r14[r0] = r2
            r13[r1] = r14
            r8.processEvent(r10, r11, r13)
        L_0x01dd:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.l.a(io.dcloud.common.core.ui.b, boolean, org.json.JSONObject, java.lang.String):void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r14v3, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0046, code lost:
        if (com.taobao.weex.common.Constants.Name.UNDEFINED.equals(r14.getString("titletext")) == false) goto L_0x0061;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00ec  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00fb  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(io.dcloud.common.core.ui.b r33, io.dcloud.common.core.ui.b r34) {
        /*
            r32 = this;
            r15 = r33
            java.lang.String r1 = "titlesize"
            java.lang.String r2 = "titlecolor"
            java.lang.String r3 = "titleSize"
            java.lang.String r4 = "titleColor"
            java.lang.String r5 = "titleText"
            java.lang.String r6 = "titletext"
            io.dcloud.common.adapter.util.ViewOptions r0 = r33.obtainFrameOptions()
            org.json.JSONObject r0 = r0.titleNView
            if (r0 == 0) goto L_0x02a5
            io.dcloud.common.adapter.util.ViewOptions r0 = r33.obtainFrameOptions()
            org.json.JSONObject r14 = r0.titleNView
            java.lang.String r7 = io.dcloud.common.util.TitleNViewUtil.getTitleNViewId(r33)
            java.lang.String r8 = " "
            java.lang.String r13 = "type"
            r16 = 0
            java.lang.String r0 = ""
            if (r14 == 0) goto L_0x0162
            boolean r9 = android.text.TextUtils.isEmpty(r7)
            if (r9 != 0) goto L_0x0162
            boolean r0 = r14.has(r6)     // Catch:{ Exception -> 0x00b2 }
            java.lang.String r9 = "undefined"
            if (r0 == 0) goto L_0x0048
            java.lang.String r0 = r14.getString(r6)     // Catch:{ Exception -> 0x00b2 }
            if (r0 == 0) goto L_0x0048
            java.lang.String r0 = r14.getString(r6)     // Catch:{ Exception -> 0x00b2 }
            boolean r0 = r9.equals(r0)     // Catch:{ Exception -> 0x00b2 }
            if (r0 == 0) goto L_0x0061
        L_0x0048:
            boolean r0 = r14.has(r5)     // Catch:{ Exception -> 0x00b2 }
            if (r0 == 0) goto L_0x005e
            java.lang.String r0 = r14.getString(r5)     // Catch:{ Exception -> 0x00b2 }
            if (r0 == 0) goto L_0x005e
            java.lang.String r0 = r14.getString(r5)     // Catch:{ Exception -> 0x00b2 }
            boolean r0 = r9.equals(r0)     // Catch:{ Exception -> 0x00b2 }
            if (r0 == 0) goto L_0x0061
        L_0x005e:
            r14.remove(r6)     // Catch:{ Exception -> 0x00b2 }
        L_0x0061:
            boolean r0 = r14.has(r2)     // Catch:{ Exception -> 0x00b2 }
            if (r0 == 0) goto L_0x0071
            java.lang.String r0 = r14.optString(r2)     // Catch:{ Exception -> 0x00b2 }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x00b2 }
            if (r0 == 0) goto L_0x0089
        L_0x0071:
            boolean r0 = r14.has(r4)     // Catch:{ Exception -> 0x00b2 }
            if (r0 == 0) goto L_0x0081
            java.lang.String r0 = r14.optString(r4)     // Catch:{ Exception -> 0x00b2 }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x00b2 }
            if (r0 == 0) goto L_0x0089
        L_0x0081:
            r14.remove(r2)     // Catch:{ Exception -> 0x00b2 }
            java.lang.String r0 = "#000000"
            r14.put(r4, r0)     // Catch:{ Exception -> 0x00b2 }
        L_0x0089:
            boolean r0 = r14.has(r1)     // Catch:{ Exception -> 0x00b2 }
            if (r0 == 0) goto L_0x0099
            java.lang.String r0 = r14.optString(r1)     // Catch:{ Exception -> 0x00b2 }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x00b2 }
            if (r0 == 0) goto L_0x00b6
        L_0x0099:
            boolean r0 = r14.has(r3)     // Catch:{ Exception -> 0x00b2 }
            if (r0 == 0) goto L_0x00a9
            java.lang.String r0 = r14.optString(r3)     // Catch:{ Exception -> 0x00b2 }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x00b2 }
            if (r0 == 0) goto L_0x00b6
        L_0x00a9:
            r14.remove(r1)     // Catch:{ Exception -> 0x00b2 }
            java.lang.String r0 = "17px"
            r14.put(r3, r0)     // Catch:{ Exception -> 0x00b2 }
            goto L_0x00b6
        L_0x00b2:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00b6:
            boolean r0 = r14.has(r5)     // Catch:{ Exception -> 0x00e1 }
            if (r0 == 0) goto L_0x00cc
            java.lang.Object r0 = r14.get(r5)     // Catch:{ Exception -> 0x00e1 }
            if (r0 == 0) goto L_0x00e2
            boolean r5 = r0 instanceof java.lang.String     // Catch:{ Exception -> 0x00e1 }
            if (r5 == 0) goto L_0x00e2
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x00e1 }
        L_0x00ca:
            r8 = r0
            goto L_0x00e2
        L_0x00cc:
            boolean r0 = r14.has(r6)     // Catch:{ Exception -> 0x00e1 }
            if (r0 == 0) goto L_0x00e2
            java.lang.Object r0 = r14.get(r6)     // Catch:{ Exception -> 0x00e1 }
            if (r0 == 0) goto L_0x00e2
            boolean r5 = r0 instanceof java.lang.String     // Catch:{ Exception -> 0x00e1 }
            if (r5 == 0) goto L_0x00e2
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x00e1 }
            goto L_0x00ca
        L_0x00e1:
        L_0x00e2:
            java.lang.String r0 = r14.optString(r4)
            boolean r4 = android.text.TextUtils.isEmpty(r0)
            if (r4 == 0) goto L_0x00f0
            java.lang.String r0 = r14.optString(r2)
        L_0x00f0:
            r2 = r0
            java.lang.String r0 = r14.optString(r3)
            boolean r3 = android.text.TextUtils.isEmpty(r0)
            if (r3 == 0) goto L_0x00ff
            java.lang.String r0 = r14.optString(r1)
        L_0x00ff:
            r1 = r0
            java.lang.String r0 = r14.optString(r13)
            java.lang.String r3 = "transparent"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x011d
            boolean r0 = android.text.TextUtils.isEmpty(r2)
            if (r0 != 0) goto L_0x011d
            r0 = 0
            java.lang.String r2 = io.dcloud.common.util.TitleNViewUtil.changeColorAlpha((java.lang.String) r2, (float) r0)     // Catch:{ Exception -> 0x0118 }
            goto L_0x011d
        L_0x0118:
            r0 = move-exception
            r3 = r0
            r3.printStackTrace()
        L_0x011d:
            java.lang.String r0 = "titleOverflow"
            java.lang.String r0 = r14.optString(r0)
            java.lang.String r3 = "titleAlign"
            java.lang.String r3 = r14.optString(r3)
            java.lang.String r4 = "titleIcon"
            java.lang.String r4 = r14.optString(r4)
            java.lang.String r5 = "titleIconRadius"
            java.lang.String r5 = r14.optString(r5)
            java.lang.String r6 = "subtitleText"
            java.lang.String r6 = r14.optString(r6)
            java.lang.String r9 = "subtitleColor"
            java.lang.String r9 = r14.optString(r9)
            java.lang.String r10 = "subtitleSize"
            java.lang.String r10 = r14.optString(r10)
            java.lang.String r11 = "subtitleOverflow"
            java.lang.String r11 = r14.optString(r11)
            java.lang.String r12 = "titleIconWidth"
            java.lang.String r12 = r14.optString(r12)
            r17 = r11
            r18 = r12
            r11 = r9
            r12 = r10
            r9 = r5
            r10 = r6
            r6 = r0
            r5 = r1
            r0 = r3
            r3 = r8
            r8 = r4
            r4 = r2
            goto L_0x0171
        L_0x0162:
            r9 = r0
            r10 = r9
            r11 = r10
            r12 = r11
            r17 = r12
            r18 = r17
            r3 = r8
            r4 = r16
            r5 = r4
            r6 = r5
            r8 = r18
        L_0x0171:
            io.dcloud.common.core.ui.l r1 = r15.mWindowMgr
            io.dcloud.common.DHInterface.IMgr$MgrType r2 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r19 = r13
            r13 = 4
            r20 = r14
            java.lang.Object[] r14 = new java.lang.Object[r13]
            io.dcloud.common.DHInterface.IWebview r21 = r33.obtainWebView()
            r22 = 0
            r14[r22] = r21
            r13 = 1
            java.lang.String r23 = "nativeobj"
            r14[r13] = r23
            java.lang.String r24 = "show"
            r13 = 2
            r14[r13] = r24
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            r25 = r12
            java.lang.String r12 = "['"
            r13.append(r12)
            r13.append(r7)
            java.lang.String r12 = "','"
            r13.append(r12)
            r13.append(r7)
            java.lang.String r12 = "']"
            r13.append(r12)
            java.lang.String r12 = r13.toString()
            org.json.JSONArray r12 = io.dcloud.common.util.JSONUtil.createJSONArray(r12)
            r13 = 3
            r14[r13] = r12
            r12 = 1
            r1.processEvent(r2, r12, r14)
            io.dcloud.common.core.ui.l r1 = r15.mWindowMgr
            r14 = 4
            java.lang.Object[] r14 = new java.lang.Object[r14]
            io.dcloud.common.DHInterface.IWebview r21 = r33.obtainWebView()
            r14[r22] = r21
            r14[r12] = r23
            java.lang.String r21 = "addNativeView"
            r13 = 2
            r14[r13] = r21
            java.lang.Object[] r13 = new java.lang.Object[r13]
            r13[r22] = r15
            r13[r12] = r7
            r12 = 3
            r14[r12] = r13
            r12 = 10
            r1.processEvent(r2, r12, r14)
            io.dcloud.common.core.ui.l r1 = r15.mWindowMgr
            io.dcloud.common.DHInterface.IWebview r2 = r33.obtainWebView()
            java.lang.Object r1 = io.dcloud.common.util.TitleNViewUtil.getTitleNView(r1, r2, r15, r7)
            boolean r2 = r1 instanceof io.dcloud.common.DHInterface.ITitleNView
            if (r2 == 0) goto L_0x02a5
            r14 = r1
            io.dcloud.common.DHInterface.ITitleNView r14 = (io.dcloud.common.DHInterface.ITitleNView) r14
            r1 = r33
            r2 = r14
            r7 = r0
            r12 = r25
            r15 = r19
            r13 = r17
            r0 = r14
            r15 = r20
            r14 = r18
            io.dcloud.common.util.TitleNViewUtil.drawTitle(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14)
            io.dcloud.common.DHInterface.IWebview r1 = r33.obtainWebView()
            io.dcloud.common.adapter.util.ViewOptions r2 = r33.obtainFrameOptions()
            org.json.JSONObject r2 = r2.titleNView
            io.dcloud.common.util.TitleNViewUtil.setTitleNViewPadding(r0, r1, r2)
            io.dcloud.common.adapter.util.ViewOptions r1 = r33.obtainFrameOptions()
            org.json.JSONObject r1 = r1.titleNView
            if (r34 == 0) goto L_0x0216
            io.dcloud.common.DHInterface.IWebview r2 = r34.obtainWebView()
            goto L_0x0218
        L_0x0216:
            r2 = r16
        L_0x0218:
            io.dcloud.common.util.TitleNViewUtil.setButtons(r0, r1, r2)
            io.dcloud.common.adapter.util.ViewOptions r1 = r33.obtainFrameOptions()
            org.json.JSONObject r1 = r1.titleNView
            int r2 = r33.getFrameType()
            io.dcloud.common.util.TitleNViewUtil.setBackButton(r0, r1, r2)
            java.lang.String r1 = "splitLine"
            org.json.JSONObject r2 = r15.optJSONObject(r1)
            if (r2 == 0) goto L_0x0247
            io.dcloud.common.DHInterface.IWebview r27 = r33.obtainWebView()
            org.json.JSONObject r29 = r15.optJSONObject(r1)
            r1 = r19
            java.lang.String r31 = r15.optString(r1)
            r28 = 0
            r30 = 1
            r26 = r0
            io.dcloud.common.util.TitleNViewUtil.setSplitLine(r26, r27, r28, r29, r30, r31)
        L_0x0247:
            io.dcloud.common.adapter.util.ViewOptions r1 = r33.obtainFrameOptions()
            org.json.JSONObject r1 = r1.titleNView
            io.dcloud.common.util.TitleNViewUtil.setProgress(r0, r1)
            io.dcloud.common.adapter.util.ViewOptions r1 = r33.obtainFrameOptions()
            org.json.JSONObject r1 = r1.titleNView
            int r2 = r33.getFrameType()
            io.dcloud.common.util.TitleNViewUtil.setHomeButton(r0, r1, r2)
            io.dcloud.common.adapter.util.ViewOptions r1 = r33.obtainFrameOptions()
            org.json.JSONObject r1 = r1.titleNView
            io.dcloud.common.util.TitleNViewUtil.setCapsuleButtonStyle(r0, r1)
            java.lang.String r1 = "searchInput"
            org.json.JSONObject r1 = r15.optJSONObject(r1)
            if (r1 == 0) goto L_0x027f
            io.dcloud.common.adapter.util.ViewOptions r1 = r33.obtainFrameOptions()
            org.json.JSONObject r1 = r1.titleNView
            if (r34 == 0) goto L_0x027a
            io.dcloud.common.DHInterface.IWebview r16 = r34.obtainWebView()
        L_0x027a:
            r2 = r16
            io.dcloud.common.util.TitleNViewUtil.setSearchInput(r0, r1, r2)
        L_0x027f:
            java.lang.String r1 = "backgroundRepeat"
            java.lang.String r2 = r15.optString(r1)
            boolean r2 = io.dcloud.common.util.PdrUtil.isEmpty(r2)
            if (r2 != 0) goto L_0x0294
            if (r0 == 0) goto L_0x0294
            java.lang.String r1 = r15.optString(r1)
            r0.setBackgroundRepeat(r1)
        L_0x0294:
            java.lang.String r1 = "shadow"
            org.json.JSONObject r1 = r15.optJSONObject(r1)
            if (r1 == 0) goto L_0x02a5
            io.dcloud.common.adapter.util.ViewOptions r1 = r33.obtainFrameOptions()
            org.json.JSONObject r1 = r1.titleNView
            io.dcloud.common.util.TitleNViewUtil.setShadow(r0, r1)
        L_0x02a5:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.l.a(io.dcloud.common.core.ui.b, io.dcloud.common.core.ui.b):void");
    }
}
