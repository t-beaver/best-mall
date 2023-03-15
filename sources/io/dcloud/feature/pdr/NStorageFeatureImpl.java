package io.dcloud.feature.pdr;

import android.content.Context;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.db.DCStorage;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NStorageFeatureImpl implements IFeature {

    class a implements Runnable {
        final /* synthetic */ DCStorage a;
        final /* synthetic */ IWebview b;
        final /* synthetic */ String c;
        final /* synthetic */ String d;

        a(DCStorage dCStorage, IWebview iWebview, String str, String str2) {
            this.a = dCStorage;
            this.b = iWebview;
            this.c = str;
            this.d = str2;
        }

        public void run() {
            Object obj;
            DCStorage.StorageInfo performGetItem = this.a.performGetItem(this.b.obtainApp().obtainAppId(), this.c);
            JSONObject jSONObject = new JSONObject();
            int i = performGetItem.code;
            if (i != 1 || (obj = performGetItem.v) == null) {
                try {
                    jSONObject.put("code", i);
                    jSONObject.put("message", performGetItem.meg);
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                JSUtil.execCallback(this.b, this.d, jSONObject, JSUtil.ERROR, false);
                return;
            }
            try {
                jSONObject.put("data", String.valueOf(obj));
            } catch (JSONException e3) {
                e3.printStackTrace();
            }
            JSUtil.execCallback(this.b, this.d, jSONObject, JSUtil.OK, false);
        }
    }

    class b implements Runnable {
        final /* synthetic */ DCStorage a;
        final /* synthetic */ IWebview b;
        final /* synthetic */ String c;
        final /* synthetic */ String d;
        final /* synthetic */ String e;

        b(DCStorage dCStorage, IWebview iWebview, String str, String str2, String str3) {
            this.a = dCStorage;
            this.b = iWebview;
            this.c = str;
            this.d = str2;
            this.e = str3;
        }

        public void run() {
            DCStorage.StorageInfo performSetItem = this.a.performSetItem(this.b.getActivity(), this.b.obtainApp().obtainAppId(), this.c, this.d);
            int i = JSUtil.ERROR;
            JSONObject jSONObject = new JSONObject();
            int i2 = performSetItem.code;
            if (i2 == 1) {
                i = JSUtil.OK;
            } else {
                try {
                    jSONObject.put("code", i2);
                    jSONObject.put("message", performSetItem.meg);
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
            JSUtil.execCallback(this.b, this.e, jSONObject, i, false);
        }
    }

    class c implements Runnable {
        final /* synthetic */ DCStorage a;
        final /* synthetic */ IWebview b;
        final /* synthetic */ String c;

        c(DCStorage dCStorage, IWebview iWebview, String str) {
            this.a = dCStorage;
            this.b = iWebview;
            this.c = str;
        }

        public void run() {
            DCStorage.StorageInfo performClear = this.a.performClear(this.b.getContext(), this.b.obtainApp().obtainAppId());
            JSONObject jSONObject = new JSONObject();
            int i = performClear.code;
            if (i == 1) {
                JSUtil.execCallback(this.b, this.c, jSONObject, JSUtil.OK, false);
                return;
            }
            try {
                jSONObject.put("code", i);
                jSONObject.put("message", performClear.meg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSUtil.execCallback(this.b, this.c, jSONObject, JSUtil.ERROR, false);
        }
    }

    class d implements Runnable {
        final /* synthetic */ DCStorage a;
        final /* synthetic */ IWebview b;
        final /* synthetic */ String c;
        final /* synthetic */ String d;

        d(DCStorage dCStorage, IWebview iWebview, String str, String str2) {
            this.a = dCStorage;
            this.b = iWebview;
            this.c = str;
            this.d = str2;
        }

        public void run() {
            DCStorage.StorageInfo performRemoveItem = this.a.performRemoveItem(this.b.getContext(), this.b.obtainApp().obtainAppId(), this.c);
            JSONObject jSONObject = new JSONObject();
            int i = performRemoveItem.code;
            if (i == 1) {
                JSUtil.execCallback(this.b, this.d, jSONObject, JSUtil.OK, false);
                return;
            }
            try {
                jSONObject.put("code", i);
                jSONObject.put("message", performRemoveItem.meg);
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
            JSUtil.execCallback(this.b, this.d, jSONObject, JSUtil.ERROR, false);
        }
    }

    class e implements Runnable {
        final /* synthetic */ DCStorage a;
        final /* synthetic */ IWebview b;
        final /* synthetic */ String c;

        e(DCStorage dCStorage, IWebview iWebview, String str) {
            this.a = dCStorage;
            this.b = iWebview;
            this.c = str;
        }

        public void run() {
            Object obj;
            JSONArray jSONArray = new JSONArray();
            JSONObject jSONObject = new JSONObject();
            DCStorage.StorageInfo performGetAllKeys = this.a.performGetAllKeys(this.b.obtainApp().obtainAppId());
            int i = performGetAllKeys.code;
            if (i != 1 || (obj = performGetAllKeys.v) == null) {
                try {
                    jSONObject.put("code", i);
                    jSONObject.put("message", performGetAllKeys.meg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSUtil.execCallback(this.b, this.c, jSONObject, JSUtil.ERROR, false);
                return;
            }
            List<String> list = (List) obj;
            if (list.size() > 0) {
                for (String put : list) {
                    jSONArray.put(put);
                }
            }
            try {
                jSONObject.put("keys", jSONArray);
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
            JSUtil.execCallback(this.b, this.c, jSONObject, JSUtil.OK, false);
        }
    }

    /* access modifiers changed from: package-private */
    public void a(IWebview iWebview, String str, String str2) {
        try {
            DCStorage dCStorage = DCStorage.getDCStorage(iWebview.getContext());
            if (dCStorage != null) {
                dCStorage.execute(new a(dCStorage, iWebview, str, str2));
            }
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: package-private */
    public String b(IWebview iWebview, String str) {
        DCStorage.StorageInfo performGetItem;
        Object obj;
        try {
            DCStorage dCStorage = DCStorage.getDCStorage(iWebview.getContext());
            if (dCStorage == null || (performGetItem = dCStorage.performGetItem(iWebview.obtainApp().obtainAppId(), str)) == null || performGetItem.code != 1 || (obj = performGetItem.v) == null) {
                return null;
            }
            return (String) obj;
        } catch (Exception unused) {
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public void c(IWebview iWebview, String str, String str2) {
        try {
            DCStorage dCStorage = DCStorage.getDCStorage(iWebview.getContext());
            if (dCStorage != null) {
                dCStorage.performSetItem(iWebview.getActivity(), iWebview.obtainApp().obtainAppId(), str, str2);
            }
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: package-private */
    public void d(IWebview iWebview, String str) {
        try {
            DCStorage dCStorage = DCStorage.getDCStorage(iWebview.getContext());
            if (dCStorage != null) {
                dCStorage.performRemoveItem(iWebview.getContext(), iWebview.obtainApp().obtainAppId(), str);
            }
        } catch (Exception unused) {
        }
    }

    public void dispose(String str) {
        DCStorage dCStorage = DCStorage.getDCStorage((Context) null);
        if (dCStorage != null) {
            dCStorage.close();
        }
    }

    public String execute(IWebview iWebview, String str, String[] strArr) {
        str.hashCode();
        str.hashCode();
        char c2 = 65535;
        switch (str.hashCode()) {
            case -760527825:
                if (str.equals("clearAsync")) {
                    c2 = 0;
                    break;
                }
                break;
            case -629490457:
                if (str.equals("setItemAsync")) {
                    c2 = 1;
                    break;
                }
                break;
            case -75439223:
                if (str.equals("getItem")) {
                    c2 = 2;
                    break;
                }
                break;
            case -57276667:
                if (str.equals("removeItemAsync")) {
                    c2 = 3;
                    break;
                }
                break;
            case 106079:
                if (str.equals(IApp.ConfigProperty.CONFIG_KEY)) {
                    c2 = 4;
                    break;
                }
                break;
            case 94746189:
                if (str.equals("clear")) {
                    c2 = 5;
                    break;
                }
                break;
            case 124428031:
                if (str.equals("getAllKeys")) {
                    c2 = 6;
                    break;
                }
                break;
            case 589651420:
                if (str.equals("getLength")) {
                    c2 = 7;
                    break;
                }
                break;
            case 1098253751:
                if (str.equals("removeItem")) {
                    c2 = 8;
                    break;
                }
                break;
            case 1601567421:
                if (str.equals("getAllKeysAsync")) {
                    c2 = 9;
                    break;
                }
                break;
            case 1984670357:
                if (str.equals("setItem")) {
                    c2 = 10;
                    break;
                }
                break;
            case 2116484211:
                if (str.equals("getItemAsync")) {
                    c2 = 11;
                    break;
                }
                break;
        }
        switch (c2) {
            case 0:
                a(iWebview, strArr[0]);
                break;
            case 1:
                a(iWebview, strArr[1], strArr[2], strArr[0]);
                break;
            case 2:
                String b2 = b(iWebview, strArr[0]);
                if (b2 == null) {
                    return "null:";
                }
                return "string:" + b2;
            case 3:
                b(iWebview, strArr[1], strArr[0]);
                break;
            case 4:
                return Deprecated_JSUtil.wrapJsVar(a(iWebview, Integer.parseInt(strArr[0])), true);
            case 5:
                a(iWebview);
                break;
            case 6:
                return b(iWebview);
            case 7:
                return JSUtil.wrapJsVar((float) c(iWebview));
            case 8:
                d(iWebview, strArr[0]);
                break;
            case 9:
                c(iWebview, strArr[0]);
                break;
            case 10:
                c(iWebview, strArr[0], strArr[1]);
                break;
            case 11:
                a(iWebview, strArr[1], strArr[0]);
                break;
        }
        return null;
    }

    public void init(AbsMgr absMgr, String str) {
    }

    /* access modifiers changed from: package-private */
    public void a(IWebview iWebview, String str, String str2, String str3) {
        try {
            DCStorage dCStorage = DCStorage.getDCStorage(iWebview.getContext());
            if (dCStorage != null) {
                dCStorage.execute(new b(dCStorage, iWebview, str, str2, str3));
            }
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: package-private */
    public void c(IWebview iWebview, String str) {
        try {
            DCStorage dCStorage = DCStorage.getDCStorage(iWebview.getContext());
            if (dCStorage != null) {
                dCStorage.execute(new e(dCStorage, iWebview, str));
            }
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: package-private */
    public void b(IWebview iWebview, String str, String str2) {
        try {
            DCStorage dCStorage = DCStorage.getDCStorage(iWebview.getContext());
            if (dCStorage != null) {
                dCStorage.execute(new d(dCStorage, iWebview, str, str2));
            }
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: package-private */
    public void a(IWebview iWebview) {
        try {
            DCStorage dCStorage = DCStorage.getDCStorage(iWebview.getContext());
            if (dCStorage != null) {
                dCStorage.performClear(iWebview.getContext(), iWebview.obtainApp().obtainAppId());
            }
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: package-private */
    public int c(IWebview iWebview) {
        Object obj;
        try {
            DCStorage dCStorage = DCStorage.getDCStorage(iWebview.getContext());
            if (dCStorage == null) {
                return 0;
            }
            DCStorage.StorageInfo performGetAllKeys = dCStorage.performGetAllKeys(iWebview.obtainApp().obtainAppId());
            if (performGetAllKeys.code != 1 || (obj = performGetAllKeys.v) == null) {
                return 0;
            }
            return ((List) obj).size();
        } catch (Exception unused) {
            return 0;
        }
    }

    /* access modifiers changed from: package-private */
    public String b(IWebview iWebview) {
        Object obj;
        try {
            DCStorage dCStorage = DCStorage.getDCStorage(iWebview.getContext());
            if (dCStorage == null) {
                return "";
            }
            DCStorage.StorageInfo performGetAllKeys = dCStorage.performGetAllKeys(iWebview.obtainApp().obtainAppId());
            if (performGetAllKeys.code != 1 || (obj = performGetAllKeys.v) == null) {
                return "";
            }
            List list = (List) obj;
            StringBuffer stringBuffer = new StringBuffer(Operators.ARRAY_START_STR);
            if (list.size() > 0) {
                int size = list.size() - 1;
                for (int i = 0; i < list.size(); i++) {
                    stringBuffer.append("'");
                    stringBuffer.append((String) list.get(i));
                    stringBuffer.append("'");
                    if (i == size) {
                        stringBuffer.append(Operators.ARRAY_END_STR);
                    } else {
                        stringBuffer.append(",");
                    }
                }
            } else {
                stringBuffer.append(Operators.ARRAY_END_STR);
            }
            return stringBuffer.toString();
        } catch (Exception unused) {
            return "";
        }
    }

    /* access modifiers changed from: package-private */
    public void a(IWebview iWebview, String str) {
        try {
            DCStorage dCStorage = DCStorage.getDCStorage(iWebview.getContext());
            if (dCStorage != null) {
                dCStorage.execute(new c(dCStorage, iWebview, str));
            }
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0023, code lost:
        r0 = (java.util.List) (r0 = r3.v);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String a(io.dcloud.common.DHInterface.IWebview r3, int r4) {
        /*
            r2 = this;
            android.content.Context r0 = r3.getContext()     // Catch:{ Exception -> 0x0034 }
            io.dcloud.common.util.db.DCStorage r0 = io.dcloud.common.util.db.DCStorage.getDCStorage(r0)     // Catch:{ Exception -> 0x0034 }
            if (r0 == 0) goto L_0x0034
            io.dcloud.common.DHInterface.IApp r3 = r3.obtainApp()     // Catch:{ Exception -> 0x0034 }
            java.lang.String r3 = r3.obtainAppId()     // Catch:{ Exception -> 0x0034 }
            io.dcloud.common.util.db.DCStorage$StorageInfo r3 = r0.performGetAllKeys(r3)     // Catch:{ Exception -> 0x0034 }
            java.lang.Object r0 = r3.v     // Catch:{ Exception -> 0x0034 }
            if (r0 == 0) goto L_0x0034
            boolean r1 = r0 instanceof java.util.List     // Catch:{ Exception -> 0x0034 }
            if (r1 == 0) goto L_0x0034
            int r3 = r3.code     // Catch:{ Exception -> 0x0034 }
            r1 = 1
            if (r3 != r1) goto L_0x0034
            java.util.List r0 = (java.util.List) r0     // Catch:{ Exception -> 0x0034 }
            if (r0 == 0) goto L_0x0034
            int r3 = r0.size()     // Catch:{ Exception -> 0x0034 }
            if (r4 >= r3) goto L_0x0034
            java.lang.Object r3 = r0.get(r4)     // Catch:{ Exception -> 0x0034 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Exception -> 0x0034 }
            return r3
        L_0x0034:
            java.lang.String r3 = ""
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.pdr.NStorageFeatureImpl.a(io.dcloud.common.DHInterface.IWebview, int):java.lang.String");
    }
}
