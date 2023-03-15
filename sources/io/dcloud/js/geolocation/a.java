package io.dcloud.js.geolocation;

import android.content.Context;
import android.util.Log;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;

public class a {
    AbsMgr a = null;
    GeoManagerBase b = null;
    GeoManagerBase c = null;
    GeoManagerBase d = null;
    boolean e = false;

    public a(AbsMgr absMgr) {
        this.a = absMgr;
    }

    private GeoManagerBase a(String str) {
        try {
            if (str.equals("io.dcloud.js.geolocation.amap.AMapGeoManager")) {
                GeoManagerBase geoManagerBase = (GeoManagerBase) PlatformUtil.invokeMethod(str, "getInstance", (Object) null, new Class[]{Context.class}, new Object[]{this.a.getContext()});
                if (geoManagerBase != null) {
                    return geoManagerBase;
                }
            }
            return (GeoManagerBase) Class.forName(str).getConstructor(new Class[]{Context.class}).newInstance(new Object[]{this.a.getContext()});
        } catch (Exception unused) {
            Log.w("geoLoaction", str + " exception");
            return null;
        }
    }

    public String a(IWebview iWebview, String str, String[] strArr) {
        GeoManagerBase geoManagerBase;
        if ("clearWatch".equals(str)) {
            GeoManagerBase geoManagerBase2 = this.d;
            if (geoManagerBase2 == null || !geoManagerBase2.hasKey(strArr[0])) {
                GeoManagerBase geoManagerBase3 = this.c;
                if (geoManagerBase3 == null || !geoManagerBase3.hasKey(strArr[0])) {
                    GeoManagerBase geoManagerBase4 = this.b;
                    geoManagerBase = (geoManagerBase4 == null || !geoManagerBase4.hasKey(strArr[0])) ? null : this.b;
                } else {
                    geoManagerBase = this.c;
                }
            } else {
                geoManagerBase = this.d;
            }
        } else {
            geoManagerBase = a(iWebview, strArr[4], strArr[3], strArr[0]);
        }
        if (geoManagerBase != null) {
            geoManagerBase.execute(iWebview, str, strArr);
        }
        return null;
    }

    private GeoManagerBase a(IWebview iWebview, String str, String str2, String str3) {
        GeoManagerBase geoManagerBase;
        GeoManagerBase geoManagerBase2;
        if (PdrUtil.isEmpty(str2)) {
            str2 = "wgs84";
        }
        if (PdrUtil.isEmpty(str)) {
            if (PdrUtil.isEquals(str2, "wgs84")) {
                geoManagerBase = this.d;
                if (geoManagerBase == null) {
                    geoManagerBase = a("io.dcloud.js.geolocation.system.LocalGeoManager");
                }
                this.d = geoManagerBase;
            } else if (PdrUtil.isEquals(str2, "gcj02")) {
                GeoManagerBase geoManagerBase3 = this.c;
                if (geoManagerBase3 == null) {
                    geoManagerBase3 = a("io.dcloud.js.geolocation.amap.AMapGeoManager");
                }
                this.c = geoManagerBase;
                if (geoManagerBase == null) {
                    if (geoManagerBase == null) {
                        geoManagerBase = a("io.dcloud.js.geolocation.baidu.BaiduGeoManager");
                    }
                    this.c = geoManagerBase;
                }
            } else if (PdrUtil.isEquals(str2, "bd09") || PdrUtil.isEquals(str2, "bd09ll") || PdrUtil.isEquals(str2, "gcj02")) {
                GeoManagerBase geoManagerBase4 = this.c;
                if (geoManagerBase4 == null) {
                    geoManagerBase4 = a("io.dcloud.js.geolocation.baidu.BaiduGeoManager");
                }
                this.c = geoManagerBase;
            } else {
                geoManagerBase = null;
            }
            if (geoManagerBase == null) {
                Deprecated_JSUtil.execCallback(iWebview, str3, DOMException.toJSON(18, "not support " + str2), JSUtil.ERROR, true, false);
            }
        } else {
            if (PdrUtil.isEquals("baidu", str)) {
                geoManagerBase2 = this.b;
                if (geoManagerBase2 == null) {
                    geoManagerBase2 = a("io.dcloud.js.geolocation.baidu.BaiduGeoManager");
                }
                this.b = geoManagerBase2;
            } else if (PdrUtil.isEquals("amap", str)) {
                GeoManagerBase geoManagerBase5 = this.c;
                if (geoManagerBase5 == null) {
                    geoManagerBase5 = a("io.dcloud.js.geolocation.amap.AMapGeoManager");
                }
                this.c = geoManagerBase2;
            } else {
                GeoManagerBase geoManagerBase6 = this.d;
                if (geoManagerBase6 == null) {
                    geoManagerBase6 = a("io.dcloud.js.geolocation.system.LocalGeoManager");
                }
                this.d = geoManagerBase2;
            }
            if (geoManagerBase == null) {
                Deprecated_JSUtil.execCallback(iWebview, str3, DOMException.toJSON(17, DOMException.MSG_GEOLOCATION_PROVIDER_ERROR), JSUtil.ERROR, true, false);
            }
        }
        return geoManagerBase;
    }

    public void a() {
        GeoManagerBase geoManagerBase = this.b;
        if (geoManagerBase != null) {
            geoManagerBase.onDestroy();
        }
        GeoManagerBase geoManagerBase2 = this.d;
        if (geoManagerBase2 != null) {
            geoManagerBase2.onDestroy();
        }
        GeoManagerBase geoManagerBase3 = this.c;
        if (geoManagerBase3 != null) {
            geoManagerBase3.onDestroy();
        }
    }
}
