package io.dcloud.feature.ui;

import android.content.Context;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.ViewRect;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.JsEventUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class b {
    public static byte a = ViewRect.POSITION_STATIC;
    public static byte b = ViewRect.POSITION_ABSOLUTE;
    public static byte c = ViewRect.POSITION_DOCK;
    public static byte d = ViewRect.DOCK_LEFT;
    public static byte e = ViewRect.DOCK_RIGHT;
    public static byte f = ViewRect.DOCK_TOP;
    public static byte g = ViewRect.DOCK_BOTTOM;
    protected c h = null;
    protected HashMap<String, String> i = null;
    protected a j = null;
    protected String k;
    protected String l = null;
    protected String m = null;
    protected String n = null;
    protected JSONObject o = null;
    protected IWebview p = null;
    private byte q = b;
    private byte r = f;
    private Context s;
    protected HashMap<String, ArrayList<String[]>> t = null;

    public b(String str) {
        this.k = str;
        this.i = new HashMap<>();
    }

    public abstract String a(IWebview iWebview, String str, JSONArray jSONArray);

    public abstract void a(int i2, int i3, int i4, int i5, int i6, int i7);

    /* access modifiers changed from: protected */
    public void a(Context context, a aVar, IWebview iWebview, String str, JSONObject jSONObject) {
        this.j = aVar;
        this.s = context;
        this.p = iWebview;
        this.l = str;
        if (jSONObject == null) {
            jSONObject = JSONUtil.createJSONObject("{}");
        }
        this.o = jSONObject;
        f();
    }

    public final byte b() {
        return this.r;
    }

    public final byte c() {
        return this.q;
    }

    public abstract AdaFrameItem d();

    /* access modifiers changed from: protected */
    public abstract void e();

    /* access modifiers changed from: protected */
    public void f() {
        JSONObject jSONObject = this.o;
        if (!JSONUtil.isNull(jSONObject, "id") && PdrUtil.isEmpty(this.m)) {
            this.m = JSONUtil.getString(jSONObject, "id");
        }
        this.n = jSONObject.optString("tid");
        String string = JSONUtil.getString(jSONObject, "position");
        if (!PdrUtil.isEmpty(string)) {
            if (AbsoluteConst.JSON_VALUE_POSITION_ABSOLUTE.equals(string)) {
                this.q = b;
            } else if ("dock".equals(string)) {
                this.q = c;
            } else if ("static".equals(string)) {
                this.q = a;
            }
        }
        String string2 = JSONUtil.getString(jSONObject, "dock");
        if (PdrUtil.isEmpty(string2)) {
            return;
        }
        if ("bottom".equals(string2)) {
            this.r = g;
        } else if ("top".equals(string2)) {
            this.r = f;
        } else if ("left".equals(string2)) {
            this.r = d;
        } else if ("right".equals(string2)) {
            this.r = e;
        }
    }

    /* access modifiers changed from: protected */
    public void g() {
        for (c cVar : this.j.c) {
            a(this, cVar.t);
        }
    }

    public String h() {
        return StringUtil.format("(function(){return {uuid:'%s',identity:'%s',option:%s}})()", this.l, this.k, this.o);
    }

    /* access modifiers changed from: protected */
    public void b(String str, String str2) {
        ArrayList arrayList;
        HashMap<String, ArrayList<String[]>> hashMap = this.t;
        if (hashMap != null && (arrayList = hashMap.get(str2)) != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                String[] strArr = (String[]) it.next();
                if (strArr[0].equals(str)) {
                    arrayList.remove(strArr);
                }
            }
            if (arrayList.size() == 0) {
                this.t.remove(str2);
            }
        }
    }

    public final Context a() {
        return this.s;
    }

    /* access modifiers changed from: protected */
    public void a(String str, String str2, String str3) {
        if (this.t == null) {
            this.t = new HashMap<>(2);
        }
        ArrayList arrayList = this.t.get(str2);
        if (arrayList == null) {
            arrayList = new ArrayList(2);
            this.t.put(str2, arrayList);
        }
        arrayList.add(new String[]{str, str3});
    }

    private static void a(b bVar, HashMap<String, ArrayList<String[]>> hashMap) {
        if (hashMap != null) {
            for (ArrayList next : hashMap.values()) {
                if (next != null) {
                    for (int size = next.size() - 1; size >= 0; size--) {
                        String str = ((String[]) next.get(size))[0];
                        if (bVar.j.a(str, str, (String) null) == bVar) {
                            next.remove(size);
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean a(String str) {
        ArrayList arrayList;
        HashMap<String, ArrayList<String[]>> hashMap = this.t;
        if (hashMap == null || (arrayList = hashMap.get(str)) == null) {
            return false;
        }
        return !arrayList.isEmpty();
    }

    public final boolean a(String str, String str2) {
        return a(str, str2, true);
    }

    public boolean a(String str, String str2, boolean z) {
        ArrayList arrayList;
        IWebview obtainWebView;
        Logger.d("execCallback pEventType=" + str + ";");
        HashMap<String, ArrayList<String[]>> hashMap = this.t;
        if (hashMap == null || (arrayList = hashMap.get(str)) == null) {
            return false;
        }
        int size = arrayList.size();
        String eventListener_format = JsEventUtil.eventListener_format(str, str2, z);
        boolean z2 = false;
        for (int i2 = size - 1; i2 >= 0; i2--) {
            String[] strArr = (String[]) arrayList.get(i2);
            String str3 = strArr[0];
            String str4 = strArr[1];
            c a2 = this.j.a(str3, str3, (String) null);
            if (!(a2 == null || a2.K || (obtainWebView = a2.z.obtainWebView()) == null)) {
                Deprecated_JSUtil.execCallback(obtainWebView, str4, eventListener_format, JSUtil.OK, true, true);
                z2 = true;
            }
        }
        return z2;
    }
}
