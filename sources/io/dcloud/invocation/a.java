package io.dcloud.invocation;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IEventCallback;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.ui.AdaUniWebView;
import io.dcloud.common.adapter.util.PermissionUtil;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import io.dcloud.feature.internal.sdk.SDK;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class a implements IEventCallback {
    static HashMap<Integer, HashMap<String, c>> a = new HashMap<>(2);
    static a b = null;
    ArrayList<Integer> c = new ArrayList<>();

    /* renamed from: io.dcloud.invocation.a$a  reason: collision with other inner class name */
    class C0063a extends PermissionUtil.Request {
        final /* synthetic */ JSONArray a;
        final /* synthetic */ IWebview b;
        final /* synthetic */ String[] c;
        final /* synthetic */ JSONArray d;
        final /* synthetic */ JSONArray e;
        final /* synthetic */ JSONArray f;

        C0063a(JSONArray jSONArray, IWebview iWebview, String[] strArr, JSONArray jSONArray2, JSONArray jSONArray3, JSONArray jSONArray4) {
            this.a = jSONArray;
            this.b = iWebview;
            this.c = strArr;
            this.d = jSONArray2;
            this.e = jSONArray3;
            this.f = jSONArray4;
        }

        public void onDenied(String str) {
            String convertNativePermission = PermissionUtil.convertNativePermission(str);
            try {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this.b.getActivity(), convertNativePermission)) {
                    this.f.put(convertNativePermission);
                } else {
                    this.e.put(convertNativePermission);
                }
            } catch (RuntimeException unused) {
            }
            a.this.a(this.b, this.c, this.d, this.a, this.e, this.f);
        }

        public void onGranted(String str) {
            this.a.put(PermissionUtil.convertNativePermission(str));
            a.this.a(this.b, this.c, this.d, this.a, this.e, this.f);
        }
    }

    class b implements ISysEventListener {
        final /* synthetic */ IWebview a;
        final /* synthetic */ String b;

        b(IWebview iWebview, String str) {
            this.a = iWebview;
            this.b = str;
        }

        public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
            if (sysEventType != ISysEventListener.SysEventType.onActivityResult) {
                return false;
            }
            Object[] objArr = (Object[]) obj;
            int intValue = ((Integer) objArr[0]).intValue();
            int intValue2 = ((Integer) objArr[1]).intValue();
            Intent intent = (Intent) objArr[2];
            StringBuffer stringBuffer = new StringBuffer(Operators.ARRAY_START_STR);
            stringBuffer.append(intValue);
            stringBuffer.append(",");
            stringBuffer.append(intValue2);
            if (intent != null) {
                stringBuffer.append(",");
                stringBuffer.append(a.a(this.a, (Object) intent));
            }
            stringBuffer.append(Operators.ARRAY_END_STR);
            Deprecated_JSUtil.execCallback(this.a, this.b, stringBuffer.toString(), JSUtil.OK, true, true);
            return true;
        }
    }

    class c implements IEventCallback {
        final /* synthetic */ IWebview a;
        final /* synthetic */ ISysEventListener b;

        c(IWebview iWebview, ISysEventListener iSysEventListener) {
            this.a = iWebview;
            this.b = iSysEventListener;
        }

        public Object onCallBack(String str, Object obj) {
            if (!PdrUtil.isEquals(str, AbsoluteConst.EVENTS_WINDOW_CLOSE) && !PdrUtil.isEquals(str, AbsoluteConst.EVENTS_CLOSE)) {
                return null;
            }
            this.a.obtainApp().unregisterSysEventListener(this.b, ISysEventListener.SysEventType.onActivityResult);
            return null;
        }
    }

    static /* synthetic */ class d {
        static final /* synthetic */ int[] a;

        static {
            int[] iArr = new int[ISysEventListener.SysEventType.values().length];
            a = iArr;
            try {
                iArr[ISysEventListener.SysEventType.onActivityResult.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public a(AbsMgr absMgr) {
        b = this;
    }

    static void b(Class cls, JSONStringer jSONStringer, ArrayList<String> arrayList) throws JSONException {
        Class<? super Object> superclass = cls.getSuperclass();
        while (superclass != null) {
            String name = superclass.getName();
            if (!arrayList.contains(name)) {
                jSONStringer.value(name);
                arrayList.add(name);
                a((Class) superclass, jSONStringer, arrayList);
            }
            if (superclass != Object.class) {
                superclass = superclass.getSuperclass();
            } else {
                return;
            }
        }
    }

    public Object onCallBack(String str, Object obj) {
        if (!PdrUtil.isEquals(str, AbsoluteConst.EVENTS_CLOSE) || !(obj instanceof IWebview)) {
            return null;
        }
        try {
            ((AdaFrameView) ((IWebview) obj).obtainFrameView()).removeFrameViewListener(this);
            this.c.remove(Integer.valueOf(((IWebview) obj).hashCode()));
            HashMap remove = a.remove(Integer.valueOf(((IWebview) obj).hashCode()));
            if (remove == null) {
                return null;
            }
            for (Map.Entry entry : remove.entrySet()) {
                a.remove(entry.getKey());
                ((c) entry.getValue()).a();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String a(IWebview iWebview, String str, String[] strArr) {
        c remove;
        int i;
        String b2;
        Class<String> cls;
        String str2;
        Object a2;
        JSONArray jSONArray;
        IWebview iWebview2 = iWebview;
        String str3 = str;
        String[] strArr2 = strArr;
        if (!this.c.contains(Integer.valueOf(iWebview.hashCode()))) {
            this.c.add(Integer.valueOf(iWebview.hashCode()));
            ((AdaFrameView) iWebview.obtainFrameView()).addFrameViewListener(this);
        }
        boolean z = true;
        int i2 = 0;
        if ("__Instance".equals(str3)) {
            String str4 = strArr2[0];
            String str5 = strArr2[1];
            if (strArr2.length <= 2 || PdrUtil.isEmpty(strArr2[2])) {
                jSONArray = null;
            } else {
                JSONArray createJSONArray = JSONUtil.createJSONArray(strArr2[2]);
                JSONObject jSONObject = JSONUtil.getJSONObject(createJSONArray, 0);
                if (jSONObject != null) {
                    if ("boolean".equals(jSONObject.optString("type"))) {
                        z = jSONObject.optBoolean("value");
                    } else {
                        z = true ^ PdrUtil.isEquals("__super__constructor__", JSONUtil.getString(jSONObject, "value"));
                    }
                }
                jSONArray = createJSONArray;
            }
            if (z) {
                try {
                    a(iWebview2, str4, new c(iWebview, this, d.a(str5), str4, jSONArray));
                } catch (Exception e) {
                    String a3 = a(e, "new " + str5);
                    Log.e("InvProxy", "NativeObject.execMethod __Instance " + str5 + " method ; params=" + jSONArray + e);
                    return a3;
                }
            }
        } else if ("release".equals(str3) || "__autoCollection".equals(str3)) {
            String str6 = strArr2[0];
            HashMap<String, c> a4 = a(iWebview);
            if (!(a4 == null || (remove = a4.remove(str6)) == null)) {
                remove.a();
            }
        } else if ("getWebviewById".equals(str3)) {
            return b(iWebview2, SDK.obtainWebview(iWebview.obtainFrameView().obtainApp().obtainAppId(), strArr2[0]).obtainWebview());
        } else {
            if ("currentWebview".equals(str3)) {
                if (iWebview2 instanceof AdaUniWebView) {
                    return "";
                }
                return b(iWebview2, iWebview.obtainWebview());
            } else if ("getContext".equals(str3)) {
                String str7 = strArr2[0];
                String b3 = b(iWebview2, iWebview.getActivity());
                a(iWebview2, "onActivityResult", str7);
                return b3;
            } else if ("importFields".equals(str3)) {
                c a5 = a(iWebview2, strArr2[0]);
                if (a5 != null) {
                    return JSUtil.wrapJsVar(a5.a(iWebview2, a5.a));
                }
            } else if ("import".equals(str3)) {
                return Deprecated_JSUtil.wrapJsVar(d.a(iWebview2, this, strArr2[0]), false);
            } else {
                if ("__plusGetAttribute".equals(str3)) {
                    String str8 = strArr2[0];
                    String str9 = strArr2[1];
                    c a6 = a(iWebview2, str8);
                    if (!(a6 == null || (a2 = c.a(a6.b, a6.c, str9)) == null)) {
                        return b(iWebview2, a2);
                    }
                } else if ("__plusSetAttribute".equals(str3)) {
                    String str10 = strArr2[0];
                    String str11 = strArr2[1];
                    JSONArray createJSONArray2 = JSONUtil.createJSONArray(strArr2[2]);
                    c a7 = a(iWebview2, str10);
                    if (a7 != null) {
                        c.b(iWebview, this, a7.b, a7.c, str11, createJSONArray2);
                    }
                } else if ("implements".equals(str3)) {
                    String str12 = strArr2[0];
                    b bVar = new b(iWebview2, strArr2[1], JSONUtil.createJSONArray(strArr2[2]), strArr2[3]);
                    bVar.a = str12;
                    return b(iWebview2, bVar.a((JSONArray) null));
                } else if (!"__loadDylib".equals(str3) && !"__release".equals(str3)) {
                    if ("__inheritList".equals(str3)) {
                        String str13 = strArr2[0];
                        try {
                            String str14 = strArr2[1];
                            if (!TextUtils.isEmpty(str14)) {
                                c a8 = a(iWebview2, str14);
                                if (a8 != null) {
                                    b2 = d.c(a8.b);
                                } else {
                                    b2 = d.b(str13);
                                }
                            } else {
                                b2 = d.b(str13);
                            }
                        } catch (Exception e2) {
                            b2 = a(e2, "importClass " + str13);
                        }
                    } else if (!"__execCFunction".equals(str3)) {
                        if ("__newObject".equals(str3)) {
                            String str15 = strArr2[0];
                            JSONArray createJSONArray3 = JSONUtil.createJSONArray(strArr2[1]);
                            try {
                                b2 = b(iWebview2, c.a(iWebview2, this, d.a(str15), createJSONArray3));
                            } catch (Exception e3) {
                                String a9 = a(e3, "newObject " + str15);
                                Log.e("InvProxy", "NativeObject.execMethod __newObject " + str15 + " method ; params=" + createJSONArray3 + e3);
                                return a9;
                            }
                        } else if ("__execStatic".equals(str3)) {
                            String str16 = strArr2[0];
                            String str17 = strArr2[1];
                            if (a(str16, str17, iWebview2)) {
                                return null;
                            }
                            JSONArray createJSONArray4 = (strArr2.length <= 2 || PdrUtil.isEmpty(strArr2[2])) ? null : JSONUtil.createJSONArray(strArr2[2]);
                            Class<String> a10 = d.a(str16);
                            if (a10 == null) {
                                str2 = str16;
                                cls = String.class;
                            } else {
                                cls = a10;
                                str2 = null;
                            }
                            try {
                                Object a11 = c.a(iWebview, this, cls, str2, str17, createJSONArray4);
                                if (a11 != null) {
                                    b2 = b(iWebview2, a11);
                                }
                            } catch (Exception e4) {
                                String a12 = a(e4, "static " + cls.getName() + Operators.DOT_STR + str17);
                                Log.e("InvProxy", "NativeObject.execMethod " + str17 + " method ; params=" + cls + e4);
                                return a12;
                            }
                        } else if ("__exec".equals(str3)) {
                            String str18 = strArr2[0];
                            String str19 = strArr2[1];
                            if (a("", str19, iWebview2)) {
                                return null;
                            }
                            JSONArray createJSONArray5 = JSONUtil.createJSONArray(strArr2[2]);
                            c a13 = a(iWebview2, str18);
                            if (a13 != null) {
                                try {
                                    Object a14 = a13.a(iWebview2, str19, createJSONArray5);
                                    if (a14 != null) {
                                        b2 = b(iWebview2, a14);
                                    }
                                } catch (Exception e5) {
                                    String a15 = a(e5, a13.b.getName() + Operators.DOT_STR + str19);
                                    Log.e("InvProxy", "NativeObject.execMethod " + str19 + " method ; params=" + createJSONArray5 + e5);
                                    return a15;
                                }
                            }
                        } else if ("__saveContent".equals(str3)) {
                            DHFile.writeFile(strArr2[1].toString().getBytes(), 0, iWebview.obtainFrameView().obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), strArr2[0]));
                        } else if ("requestPermissions".equals(str3)) {
                            JSONArray createJSONArray6 = JSONUtil.createJSONArray(strArr2[1]);
                            ArrayList arrayList = new ArrayList();
                            JSONArray jSONArray2 = new JSONArray();
                            JSONArray jSONArray3 = new JSONArray();
                            JSONArray jSONArray4 = new JSONArray();
                            int i3 = 0;
                            while (i3 < createJSONArray6.length()) {
                                String optString = createJSONArray6.optString(i3);
                                if (PermissionChecker.checkSelfPermission(iWebview.getActivity(), optString) == 0) {
                                    i = i3;
                                    jSONArray2.put(optString);
                                    a(iWebview, strArr, createJSONArray6, jSONArray2, jSONArray3, jSONArray4);
                                } else if (iWebview.getActivity().getApplicationInfo().targetSdkVersion < 23 || Build.VERSION.SDK_INT < 23) {
                                    jSONArray4.put(optString);
                                    i = i3;
                                    a(iWebview, strArr, createJSONArray6, jSONArray2, jSONArray3, jSONArray3);
                                } else {
                                    arrayList.add(optString);
                                    i = i3;
                                }
                                i3 = i + 1;
                            }
                            if (arrayList.size() == 0) {
                                return null;
                            }
                            Activity activity = iWebview.getActivity();
                            C0063a aVar = r1;
                            int requestCode = PermissionUtil.getRequestCode();
                            C0063a aVar2 = new C0063a(jSONArray2, iWebview, strArr, createJSONArray6, jSONArray3, jSONArray4);
                            PermissionUtil.requestSystemPermissions(activity, (String[]) arrayList.toArray(new String[arrayList.size()]), requestCode, aVar);
                        } else if ("checkPermission".equals(str3)) {
                            String str20 = strArr2[0];
                            int checkSelfPermission = PermissionChecker.checkSelfPermission(iWebview.getActivity(), strArr2[1]);
                            if (checkSelfPermission == -2) {
                                i2 = -2;
                            } else if (checkSelfPermission == -1) {
                                i2 = -1;
                            } else if (checkSelfPermission != 0) {
                                i2 = checkSelfPermission;
                            }
                            Deprecated_JSUtil.execCallback(iWebview, str20, "{checkResult:+" + i2 + Operators.BLOCK_END_STR, JSUtil.OK, true, false);
                        }
                    }
                    return b2;
                }
            }
        }
        return null;
    }

    static String b(IWebview iWebview, Object obj) {
        Class<?> cls = obj.getClass();
        String b2 = d.b((Class) cls);
        String str = (d.a((Class) cls) || cls == String.class || cls == CharSequence.class || cls.isArray()) ? "basic" : "object";
        StringBuffer stringBuffer = new StringBuffer();
        a(iWebview, obj, cls, stringBuffer);
        return Deprecated_JSUtil.wrapJsVar(StringUtil.format("{\"type\":\"%s\", \"value\":%s, \"className\":\"%s\",\"superClassNames\":%s}", str, stringBuffer.toString(), b2, a((Class) cls)), false);
    }

    /* access modifiers changed from: private */
    public void a(IWebview iWebview, String[] strArr, JSONArray jSONArray, JSONArray jSONArray2, JSONArray jSONArray3, JSONArray jSONArray4) {
        if (jSONArray.length() == jSONArray2.length() + jSONArray3.length() + jSONArray4.length()) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("granted", jSONArray2);
                jSONObject.put("deniedPresent", jSONArray3);
                jSONObject.put("deniedAlways", jSONArray4);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Deprecated_JSUtil.execCallback(iWebview, strArr[0], jSONObject.toString(), JSUtil.OK, true, false);
        }
    }

    private static String a(Exception exc, String str) {
        Object obj;
        Object[] objArr = new Object[1];
        StringBuilder sb = new StringBuilder();
        if (exc.getCause() != null) {
            obj = exc.getCause();
        } else {
            boolean isEmpty = TextUtils.isEmpty(exc.getMessage());
            obj = exc;
            if (!isEmpty) {
                obj = exc.getMessage();
            }
        }
        sb.append(obj);
        sb.append(";at ");
        sb.append(str);
        objArr[0] = sb.toString();
        return StringUtil.format("throw '%s';", objArr);
    }

    private void a(IWebview iWebview, String str, String str2) {
        if (d.a[ISysEventListener.SysEventType.valueOf(str).ordinal()] == 1) {
            b bVar = new b(iWebview, str2);
            iWebview.obtainApp().registerSysEventListener(bVar, ISysEventListener.SysEventType.onActivityResult);
            iWebview.obtainFrameView().addFrameViewListener(new c(iWebview, bVar));
        }
    }

    static String a(IWebview iWebview, Object obj) {
        String str;
        Class<?> cls = obj.getClass();
        String b2 = d.b((Class) cls);
        if (cls == String.class || cls == CharSequence.class) {
            str = JSUtil.QUOTE + String.valueOf(obj) + JSUtil.QUOTE;
        } else if (d.a((Class) cls)) {
            str = String.valueOf(obj);
        } else {
            String a2 = a(obj);
            a(iWebview, a2, obj);
            return StringUtil.format("plus.ios.__Tool.New(%s, true)", Deprecated_JSUtil.wrapJsVar(StringUtil.format("{\"type\":\"%s\", \"value\":%s, \"className\":\"%s\",\"superClassNames\":%s}", "object", JSUtil.QUOTE + a2 + JSUtil.QUOTE, b2, a((Class) cls)), false));
        }
        return Deprecated_JSUtil.wrapJsVar(StringUtil.format("{\"type\":\"%s\", \"value\":%s, \"className\":\"%s\",\"superClassNames\":%s}", "basic", str, b2, a((Class) cls)), false);
    }

    static String a(Class cls) {
        JSONStringer jSONStringer = new JSONStringer();
        ArrayList arrayList = new ArrayList();
        try {
            jSONStringer.array();
            b(cls, jSONStringer, arrayList);
            jSONStringer.endArray();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jSONStringer2 = jSONStringer.toString();
        return jSONStringer2 == null ? "[]" : jSONStringer2;
    }

    static void a(Class cls, JSONStringer jSONStringer, ArrayList<String> arrayList) throws JSONException {
        Class[] interfaces = cls.getInterfaces();
        if (interfaces != null) {
            for (Class cls2 : interfaces) {
                String name = cls2.getName();
                if (!arrayList.contains(name)) {
                    jSONStringer.value(name);
                    arrayList.add(name);
                    a(cls2, jSONStringer, arrayList);
                }
            }
        }
    }

    static void a(IWebview iWebview, Object obj, Class cls, StringBuffer stringBuffer) {
        if (cls == String.class || cls == CharSequence.class) {
            stringBuffer.append(JSONObject.quote(String.valueOf(obj)));
        } else if (d.a(cls)) {
            stringBuffer.append(String.valueOf(obj));
        } else if (cls.isArray()) {
            int length = Array.getLength(obj);
            stringBuffer.append(Operators.ARRAY_START_STR);
            for (int i = 0; i < length; i++) {
                stringBuffer.append(b(iWebview, d.a(Array.get(obj, i), cls)));
                if (i != length - 1) {
                    stringBuffer.append(",");
                }
            }
            stringBuffer.append(Operators.ARRAY_END_STR);
        } else {
            String a2 = a(obj);
            a(iWebview, a2, obj);
            stringBuffer.append(JSUtil.QUOTE);
            stringBuffer.append(a2);
            stringBuffer.append(JSUtil.QUOTE);
        }
    }

    private static HashMap<String, c> a(IWebview iWebview) {
        HashMap<String, c> hashMap = a.get(Integer.valueOf(iWebview.hashCode()));
        if (hashMap != null) {
            return hashMap;
        }
        HashMap<String, c> hashMap2 = new HashMap<>(2);
        a.put(Integer.valueOf(iWebview.hashCode()), hashMap2);
        return hashMap2;
    }

    /* access modifiers changed from: package-private */
    public c a(HashMap<String, c> hashMap, String str) {
        return hashMap.get(str);
    }

    /* access modifiers changed from: package-private */
    public c a(IWebview iWebview, String str) {
        return a(a(iWebview), str);
    }

    private static void a(IWebview iWebview, String str, c cVar) {
        a(iWebview).put(str, cVar);
    }

    private static c a(IWebview iWebview, String str, Object obj) {
        Class<?> cls = obj.getClass();
        cls.getName();
        c cVar = new c(b, cls, str, obj);
        a(iWebview, str, cVar);
        return cVar;
    }

    static String a(Object obj) {
        return IFeature.F_INVOCATION + obj.hashCode();
    }

    private boolean a(String str, String str2, IWebview iWebview) {
        if (iWebview == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || !Boolean.parseBoolean(iWebview.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_USE_ENCRYPTION)) || !"setWebContentsDebuggingEnabled".equalsIgnoreCase(str2) || (!TextUtils.isEmpty(str) && !"WebView".equalsIgnoreCase(str) && !"android.webkit.WebView".equalsIgnoreCase(str))) {
            return false;
        }
        return true;
    }

    public void a(String str) {
        TextUtils.isEmpty(str);
    }
}
