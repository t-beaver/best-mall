package io.dcloud.feature.ui;

import android.content.SharedPreferences;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.performance.WXInstanceApm;
import io.dcloud.application.DCLoudApplicationImpl;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.util.AnimOptions;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import io.src.dcloud.adapter.DCloudAdapterUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;

public class a implements ISysEventListener {
    IWebview a = null;
    List<c> b = null;
    List<c> c = new ArrayList(1);
    AbsMgr d;
    HashMap<String, b> e = new HashMap<>();
    IApp f = null;
    IActivityHandler g = null;
    boolean h = false;
    boolean i = false;
    boolean j = false;
    SharedPreferences k;
    private c l;
    private List<String> m = new ArrayList();
    boolean n = false;
    ArrayList<c> o = null;
    private C0042a p = new C0042a();
    StringBuffer q = new StringBuffer();
    HashMap<String, Integer> r = new HashMap<>();

    /* renamed from: io.dcloud.feature.ui.a$a  reason: collision with other inner class name */
    class C0042a implements Comparator<c> {
        C0042a() {
        }

        /* renamed from: a */
        public int compare(c cVar, c cVar2) {
            int i = cVar.F - cVar2.F;
            if (i == 0) {
                return cVar.v > cVar2.v ? 1 : -1;
            }
            return i;
        }
    }

    a(AbsMgr absMgr, IApp iApp) {
        Logger.e("IAN", "new AppWidgetMgr   " + System.currentTimeMillis() + "appid==" + iApp.obtainAppId());
        this.d = absMgr;
        this.f = iApp;
        this.k = DCLoudApplicationImpl.self().getContext().getApplicationContext().getSharedPreferences("pdr", 0);
        IActivityHandler iActivityHandler = DCloudAdapterUtil.getIActivityHandler(iApp.getActivity());
        if (iActivityHandler != null) {
            this.g = iActivityHandler;
        }
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onKeyUp);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onKeyDown);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onKeyLongPress);
        iApp.registerSysEventListener(this, ISysEventListener.SysEventType.onWebAppSrcUpZip);
    }

    private void h() {
        for (c next : this.c) {
            if (next.E) {
                next.z.obtainWebView().loadUrl(next.A);
                next.E = false;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void a(String str, b bVar) {
        this.e.put(str, bVar);
    }

    public void b(IFrameView iFrameView) {
        this.d.processEvent(IMgr.MgrType.WindowMgr, 8, iFrameView);
    }

    public void c(IFrameView iFrameView) {
        this.d.processEvent(IMgr.MgrType.WindowMgr, 22, iFrameView);
    }

    /* access modifiers changed from: package-private */
    public boolean c(String str) {
        return false;
    }

    /* access modifiers changed from: package-private */
    public c d() {
        return a(3);
    }

    /* access modifiers changed from: package-private */
    public void e(c cVar) {
        if (!this.c.contains(cVar)) {
            this.c.add(cVar);
        }
    }

    /* access modifiers changed from: package-private */
    public void f(c cVar) {
        if (!cVar.D) {
            Logger.d("AppWidgetMgr.showMaskLayer " + cVar.A);
            if (this.o == null) {
                this.o = new ArrayList<>();
            }
            cVar.D = true;
            this.o.add(cVar);
            this.d.processEvent(IMgr.MgrType.WindowMgr, 29, cVar.z);
        }
    }

    /* access modifiers changed from: package-private */
    public void g(c cVar) {
        Logger.d(Logger.MAP_TAG, "sortNWindowByZIndex beign");
        Collections.sort(this.c, this.p);
        Collections.sort(this.b, this.p);
        this.d.processEvent(IMgr.MgrType.WindowMgr, 26, cVar.z.obtainWebAppRootView());
    }

    /* access modifiers changed from: package-private */
    public void i() {
        IFrameView iFrameView = (IFrameView) this.d.processEvent(IMgr.MgrType.WindowMgr, 44, this.f);
        int frameType = iFrameView.getFrameType();
        if (frameType == 2 || frameType == 4 || frameType == 5) {
            iFrameView.obtainApp().setNeedRefreshApp(false);
            int size = this.c.size();
            JSONArray jSONArray = new JSONArray();
            for (int i2 = size - 1; i2 >= 0; i2--) {
                c cVar = this.c.get(i2);
                int frameType2 = cVar.z.getFrameType();
                if (frameType2 == 2) {
                    a(cVar.z.obtainApp(), cVar.z.obtainWebView());
                } else if (frameType2 == 4 || frameType2 == 5) {
                    cVar.a(cVar, true);
                } else {
                    cVar.b(cVar.z.obtainWebView(), jSONArray, cVar);
                }
            }
            return;
        }
        iFrameView.obtainApp().setNeedRefreshApp(true);
    }

    public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
        String str;
        IFrameView iFrameView;
        boolean z = true;
        if (BaseInfo.sDoingAnimation) {
            return true;
        }
        ISysEventListener.SysEventType sysEventType2 = ISysEventListener.SysEventType.onKeyUp;
        boolean z2 = false;
        if (sysEventType == sysEventType2 || sysEventType == ISysEventListener.SysEventType.onKeyDown || sysEventType == ISysEventListener.SysEventType.onKeyLongPress) {
            int intValue = ((Integer) ((Object[]) obj)[0]).intValue();
            if (sysEventType == sysEventType2) {
                if (intValue == 4) {
                    c d2 = d();
                    if (d2 != null) {
                        iFrameView = d2.z;
                        z2 = d2.b("back", StringUtil.format("{keyType:'%s',keyCode:%d}", "back", Integer.valueOf(intValue)), false);
                        if (!z2 && this.c.size() == 1) {
                            this.d.processEvent(IMgr.MgrType.WindowMgr, 20, this.f);
                            z2 = true;
                        }
                    } else {
                        iFrameView = (IFrameView) this.d.processEvent(IMgr.MgrType.WindowMgr, 43, this.f);
                    }
                    if (!z2 && iFrameView != null) {
                        IWebview obtainWebView = iFrameView.obtainWebView();
                        if (obtainWebView.canGoBack()) {
                            obtainWebView.goBackOrForward(-1);
                            z2 = true;
                        } else if (d2 != null) {
                            b(d2);
                            d2.e();
                        }
                        if (!z2) {
                            Collections.sort(this.c, this.p);
                            Collections.sort(this.b, this.p);
                            AbsMgr absMgr = this.d;
                            IMgr.MgrType mgrType = IMgr.MgrType.WindowMgr;
                            absMgr.processEvent(mgrType, 26, this.f.obtainWebAppRootView());
                            AnimOptions animOptions = ((AdaFrameItem) iFrameView).getAnimOptions();
                            String str2 = "none";
                            if (!PdrUtil.isEmpty(WXInstanceApm.VALUE_ERROR_CODE_DEFAULT)) {
                                animOptions.duration_close = PdrUtil.parseInt(WXInstanceApm.VALUE_ERROR_CODE_DEFAULT, animOptions.duration_close);
                            } else {
                                animOptions.duration_close = animOptions.duration_show;
                            }
                            animOptions.mOption = 1;
                            if (PdrUtil.isEmpty(str2)) {
                                str2 = "auto";
                            }
                            animOptions.setCloseAnimType(str2);
                            this.d.processEvent(mgrType, 2, iFrameView);
                            z2 = true;
                        }
                    }
                    if (!z2) {
                        z2 = a("back", sysEventType, "back", intValue, false);
                    }
                } else if (intValue == 82) {
                    z2 = a(AbsoluteConst.EVENTS_MENU, sysEventType, AbsoluteConst.EVENTS_MENU, intValue, false);
                } else if (intValue == 24) {
                    z2 = a(AbsoluteConst.EVENTS_VOLUME_UP, sysEventType, AbsoluteConst.EVENTS_VOLUME_UP, intValue, false);
                } else if (intValue == 25) {
                    z2 = a(AbsoluteConst.EVENTS_VOLUME_DOWN, sysEventType, AbsoluteConst.EVENTS_VOLUME_DOWN, intValue, false);
                } else if (intValue == 84) {
                    z2 = a("search", sysEventType, "search", intValue, false);
                }
            }
            if (sysEventType == sysEventType2) {
                str = AbsoluteConst.EVENTS_KEY_UP;
            } else if (sysEventType == ISysEventListener.SysEventType.onKeyDown) {
                str = AbsoluteConst.EVENTS_KEY_DOWN;
            } else {
                str = sysEventType == ISysEventListener.SysEventType.onKeyLongPress ? AbsoluteConst.EVENTS_LONG_PRESSED : null;
            }
            String str3 = str;
            if (DeviceInfo.isVolumeButtonEnabled || !(intValue == 24 || intValue == 25)) {
                z = z2;
            }
            return z | a(str3, sysEventType, str3, intValue, false);
        } else if (sysEventType == ISysEventListener.SysEventType.onStop) {
            for (c next : this.c) {
                next.d().onDispose();
                next.d().dispose();
            }
            return true;
        } else {
            if (sysEventType == ISysEventListener.SysEventType.onWebAppSrcUpZip) {
                f();
                h();
            }
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public b a(String str) {
        return this.e.get(str);
    }

    /* access modifiers changed from: package-private */
    public boolean b(IWebview iWebview) {
        return this.a == iWebview;
    }

    /* access modifiers changed from: package-private */
    public int c(c cVar) {
        int size = this.b.size();
        for (int i2 = size - 1; i2 >= 0; i2--) {
            if (this.b.get(i2).F <= cVar.F) {
                return i2 + 1;
            }
        }
        return size;
    }

    /* access modifiers changed from: package-private */
    public void d(c cVar) {
        if (cVar != null) {
            Logger.d("AppWidgetMgr.hideMaskLayer " + cVar.A);
            this.d.processEvent(IMgr.MgrType.WindowMgr, 30, cVar.z);
            cVar.D = false;
            ArrayList<c> arrayList = this.o;
            if (arrayList != null) {
                arrayList.remove(cVar);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void a() {
        this.c.clear();
    }

    /* access modifiers changed from: package-private */
    public synchronized String b() {
        StringBuffer stringBuffer;
        stringBuffer = new StringBuffer(Operators.ARRAY_START_STR);
        boolean z = false;
        for (c next : this.c) {
            if (!a(next.r()) && !next.p()) {
                stringBuffer.append(next.h());
                stringBuffer.append(",");
                z = true;
            }
        }
        if (z) {
            stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
        }
        stringBuffer.append(Operators.ARRAY_END_STR);
        return stringBuffer.toString();
    }

    /* access modifiers changed from: package-private */
    public synchronized String e() {
        StringBuffer stringBuffer;
        stringBuffer = new StringBuffer(Operators.ARRAY_START_STR);
        boolean z = false;
        for (c next : this.c) {
            if (next.o() && !next.p()) {
                stringBuffer.append(next.h());
                stringBuffer.append(",");
                z = true;
            }
        }
        if (z) {
            stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
        }
        stringBuffer.append(Operators.ARRAY_END_STR);
        return stringBuffer.toString();
    }

    /* access modifiers changed from: package-private */
    public void a(String str, c cVar, int i2) {
        if (this.b == null) {
            this.b = Collections.synchronizedList(new ArrayList(1));
        }
        if (!this.b.contains(cVar)) {
            this.b.add(i2, cVar);
        }
        Collections.sort(this.c, this.p);
        Collections.sort(this.b, this.p);
    }

    /* access modifiers changed from: package-private */
    public void g() {
        this.a = null;
    }

    private void f() {
        d(this.l);
    }

    /* access modifiers changed from: package-private */
    public void c(IWebview iWebview) {
        this.a = iWebview;
    }

    /* access modifiers changed from: package-private */
    public c c() {
        return a(2);
    }

    /* access modifiers changed from: package-private */
    public boolean a(IWebview iWebview) {
        if (iWebview.obtainFrameView().getFrameType() != 6 || b(iWebview)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public c a(int i2) {
        int size = this.c.size();
        for (int i3 = 0; i3 < size; i3++) {
            c cVar = this.c.get(i3);
            if (cVar.z.getFrameType() == i2) {
                return cVar;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void b(c cVar) {
        this.b.remove(cVar);
        this.c.remove(cVar);
    }

    public boolean b(String str) {
        boolean z = true;
        if (!(this.g == null || str == null)) {
            if (!PdrUtil.isNetPath(str.toLowerCase(Locale.getDefault())) && c(this.f.obtainAppId()) && BaseInfo.isWap2AppAppid(this.f.obtainAppId())) {
                if (str.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                    str = str.substring(7);
                }
                str = PdrUtil.stripQuery(PdrUtil.stripAnchor(str));
                String convert2RelPath = this.f.convert2RelPath(str);
                if (convert2RelPath.startsWith(BaseInfo.REL_PRIVATE_WWW_DIR)) {
                    convert2RelPath = convert2RelPath.substring(5);
                }
                if (!BaseInfo.containsInTemplate(this.f, convert2RelPath) && !new File(str).exists()) {
                    z = false;
                }
            }
            Logger.d("hasFile = " + z + ";filePath=" + str);
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    public c a(IFrameView iFrameView) {
        int size = this.c.size();
        for (int i2 = 0; i2 < size; i2++) {
            c cVar = this.c.get(i2);
            if (cVar.z.equals(iFrameView)) {
                return cVar;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public synchronized c a(String str, String str2, String str3) {
        int size = this.c.size();
        for (int i2 = 0; i2 < size; i2++) {
            c cVar = this.c.get(i2);
            String valueOf = String.valueOf(cVar.z.hashCode());
            String m2 = cVar.m();
            if ((PdrUtil.isEquals(str, valueOf) || PdrUtil.isEquals(str2, cVar.l) || PdrUtil.isEquals(str3, m2)) && !a(cVar.r())) {
                return cVar;
            }
        }
        return null;
    }

    private boolean a(String str, ISysEventListener.SysEventType sysEventType, String str2, int i2, boolean z) {
        String format = StringUtil.format("{keyType:'%s',keyCode:%d}", str2, Integer.valueOf(i2));
        List<c> list = this.b;
        if (list == null) {
            return false;
        }
        int size = list.size();
        Logger.d("AppWidgetMgr", "syncExecBaseEvent windowCount = " + size);
        int i3 = size - 1;
        for (int i4 = i3; i4 >= 0; i4--) {
            c cVar = this.b.get(i4);
            if (cVar != null && cVar.h == null && ((cVar.p() || cVar.G) && cVar.z.getFrameType() != 6 && ((i3 == i4 && "back".equals(str) && cVar.c(str, format, z)) || cVar.b(str, format, z) || (!BaseInfo.USE_ACTIVITY_HANDLE_KEYEVENT && str2 != null && (cVar.b(str2) || (sysEventType == ISysEventListener.SysEventType.onKeyDown && ((cVar.b("back") && i2 == 4) || ((cVar.b(AbsoluteConst.EVENTS_MENU) && i2 == 82) || ((cVar.b(AbsoluteConst.EVENTS_VOLUME_DOWN) && i2 == 25) || ((cVar.b(AbsoluteConst.EVENTS_VOLUME_UP) && i2 == 24) || (cVar.b("search") && i2 == 84))))))))))) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean a(c cVar) {
        if (!c(this.f.obtainAppId())) {
            cVar.E = false;
        }
        if (cVar.E && b(cVar.A)) {
            cVar.r().loadUrl(cVar.A);
            cVar.E = false;
        }
        return cVar.E;
    }

    /* access modifiers changed from: package-private */
    public void a(IApp iApp, IWebview iWebview) {
        if (!iWebview.getWebviewProperty("plusrequire").equals("none")) {
            iWebview.appendPreloadJsFile(iApp.convert2AbsFullPath((String) null, "_www/__wap2app.js"));
            iWebview.appendPreloadJsFile(iApp.convert2AbsFullPath((String) null, "_www/__wap2appconfig.js"));
        }
        iWebview.setPreloadJsFile(iApp.convert2AbsFullPath(iWebview.obtainFullUrl(), "_www/server_index_append.js"), true);
        String convert2AbsFullPath = iApp.convert2AbsFullPath((String) null, "_www/server_index_append.css");
        if (new File(convert2AbsFullPath).exists()) {
            iWebview.setCssFile(convert2AbsFullPath, (String) null);
            return;
        }
        String convert2AbsFullPath2 = iApp.convert2AbsFullPath((String) null, "_www/__wap2app.css");
        if (new File(convert2AbsFullPath2).exists()) {
            iWebview.setCssFile(convert2AbsFullPath2, (String) null);
        }
    }
}
