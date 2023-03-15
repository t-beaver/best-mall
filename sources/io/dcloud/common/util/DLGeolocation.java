package io.dcloud.common.util;

import android.content.Context;
import android.webkit.JavascriptInterface;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IJsInterface;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.constant.DOMException;
import java.lang.reflect.Method;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DLGeolocation implements IJsInterface {
    IWebview mWebview;

    public DLGeolocation(IWebview iWebview) {
        this.mWebview = iWebview;
    }

    public static boolean checkAmapGeo() {
        try {
            Class.forName("io.dcloud.js.geolocation.amap.AMapGeoManager");
            return !PdrUtil.isEmpty(AndroidResources.getMetaValue("com.amap.api.v2.apikey"));
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean checkBDmapGeo() {
        try {
            Class.forName("io.dcloud.js.geolocation.baidu.BaiduGeoManager");
            return !PdrUtil.isEmpty(AndroidResources.getMetaValue("com.baidu.lbsapi.API_KEY"));
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean checkGeo(Context context) {
        return checkAmapGeo() || checkBDmapGeo();
    }

    public static boolean checkInjectGeo(String str) {
        if (str.equals("replace") || str.equals("alldevice")) {
            return true;
        }
        return str.equals("auto") && BaseInfo.injectionGeolocationJS;
    }

    public static String getGEOJS() {
        return "!function(){function t(t,e){t?(this.coordsType=t.coordsType,this.address=t.address,this.addresses=t.addresses,this.coords=new a(t.latitude,t.longitude,t.altitude,t.accuracy,t.heading,t.velocity,t.altitudeAccuracy)):this.coords=new a,this.timestamp=void 0!==e?e:(new Date).getTime()}function a(t,a,e,i,n,o,s){this.latitude=t,this.longitude=a,this.accuracy=void 0!==i?i:null,this.altitude=void 0!==e?e:null,this.heading=void 0!==n?n:null,this.speed=void 0!==o?o:null,(0===this.speed||null===this.speed)&&(this.heading=0/0),this.altitudeAccuracy=void 0!==s?s:null}function e(t,a){if(o(t,a))return[t,a];var e=i(t-105,a-35),s=n(t-105,a-35),d=a/180*c,u=Math.sin(d);u=1-r*u*u;var h=Math.sqrt(u);return e=180*e/(l*(1-r)/(u*h)*c),s=180*s/(l/h*Math.cos(d)*c),mglat=a+e,mglng=t+s,[2*t-mglng,2*a-mglat]}function i(t,a){var e=-100+2*t+3*a+.2*a*a+.1*t*a+.2*Math.sqrt(Math.abs(t));return e+=2*(20*Math.sin(6*t*c)+20*Math.sin(2*t*c))/3,e+=2*(20*Math.sin(a*c)+40*Math.sin(a/3*c))/3,e+=2*(160*Math.sin(a/12*c)+320*Math.sin(a*c/30))/3}function n(t,a){var e=300+t+2*a+.1*t*t+.1*t*a+.1*Math.sqrt(Math.abs(t));return e+=2*(20*Math.sin(6*t*c)+20*Math.sin(2*t*c))/3,e+=2*(20*Math.sin(t*c)+40*Math.sin(t/3*c))/3,e+=2*(150*Math.sin(t/12*c)+300*Math.sin(t/30*c))/3}function o(t,a){return 72.004>t||t>137.8347||.8293>a||a>55.8271||!1}window.__geo__={};var s=__geo__;s.callbacks={},s.callbackId=function(t,a){var e=\"dlgeolocation\"+(new Date).valueOf();return s.callbacks[e]={s:t,e:a},e},s.callbackFromNative=function(a,i){var n=s.callbacks[a];if(n){if(1==i.status){if(n.s){var o=i.message,c=o.longitude,l=o.latitude;if(\"gcj02\"===o.coordsType){var r=e(o.longitude,o.latitude);c=r[0],l=r[1]}var d=new t({latitude:l,longitude:c,altitude:o.altitude,accuracy:o.accuracy,heading:o.heading,velocity:o.velocity,coordsType:\"WGS84\",address:o.address,addresses:o.addresses,altitudeAccuracy:o.altitudeAccuracy},void 0===o.timestamp?(new Date).getTime():o.timestamp instanceof Date?o.timestamp.getTime():o.timestamp);n.s(d)}}else n.e&&n.e(i.message);i.keepCallback||delete s.callbacks[a]}},navigator.geolocation.getCurrentPosition=function(t,a,e){console.log(\"DLGeolocation-------navigator.geolocation.getCurrentPosition\",e);var i=t,n=a||function(){},o=e||{},c=JSON.stringify(o);_dlGeolocation.exec(\"getCurrentPosition\",s.callbackId(function(t){console.log(\"success:\",t),i(t)},function(t){console.log(\"error:\",t),n(t)}),c)},navigator.geolocation.watchPosition=function(t,a,e){var i=t,n=a||function(){},o=e||{},c=JSON.stringify(o);c.id=\"dlwatchPosition\"+(new Date).valueOf(),_dlGeolocation.exec(\"watchPosition\",s.callbackId(i,n),c)},navigator.geolocation.clearwatch=function(t){_dlGeolocation.exec(\"clearwatch\",null,{id:t})};var c=3.141592653589793,l=6378245,r=.006693421622965943}();";
    }

    private Object initGeoManager() {
        try {
            if (checkAmapGeo()) {
                return PlatformUtil.invokeMethod("io.dcloud.js.geolocation.amap.AMapGeoManager", "getInstance", (Object) null, new Class[]{Context.class}, new Object[]{this.mWebview.getContext()});
            } else if (!checkBDmapGeo()) {
                return null;
            } else {
                return PlatformUtil.invokeMethod("io.dcloud.js.geolocation.baidu.BaiduGeoManager", "getInstance", (Object) null, new Class[]{Context.class}, new Object[]{this.mWebview.getContext()});
            }
        } catch (Exception e) {
            Logger.e("DLGeolocation", "initGeoManager " + e.getMessage());
            return null;
        }
    }

    @JavascriptInterface
    public String exec(String str, String str2, String str3) {
        runGeolocation(str, str2, str3);
        return null;
    }

    public String exec(String str, String str2, JSONArray jSONArray) {
        return null;
    }

    public void forceStop(String str) {
    }

    public String[] getGeoArgs(String str, String str2, String str3) {
        String str4 = str;
        try {
            JSONObject jSONObject = new JSONObject(str2);
            int optInt = jSONObject.has("maximumAge") ? jSONObject.optInt("maximumAge") : 0;
            boolean optBoolean = jSONObject.has("enableHighAccuracy") ? jSONObject.optBoolean("enableHighAccuracy") : false;
            int optInt2 = jSONObject.has("timeout") ? jSONObject.optInt("timeout") : 0;
            boolean optBoolean2 = jSONObject.has("geocode") ? jSONObject.optBoolean("geocode") : true;
            if (str4.equals("getCurrentPosition")) {
                return new String[]{str3, optBoolean + "", optInt + "", null, null, optBoolean2 + "", optInt2 + ""};
            } else if (str4.equals("watchPosition")) {
                String optString = jSONObject.optString("id");
                return new String[]{str3, optString, optBoolean + "", "", "", optBoolean2 + "", optInt2 + "", optInt + ""};
            } else {
                if (str4.equals("clearwatch")) {
                    return new String[]{jSONObject.optString("id")};
                }
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String prompt(String str, String str2) {
        return null;
    }

    public void runGeolocation(String str, String str2, String str3) {
        if (this.mWebview != null) {
            try {
                Object initGeoManager = initGeoManager();
                Method method = initGeoManager.getClass().getMethod("execute", new Class[]{IWebview.class, String.class, String[].class});
                method.invoke(initGeoManager, new Object[]{this.mWebview, str + "DLGEO", getGeoArgs(str, str3, str2)});
            } catch (Exception unused) {
                String str4 = str2;
                JSUtil.execGEOCallback(this.mWebview, str4, StringUtil.format(DOMException.JSON_ERROR_INFO, -100, this.mWebview.getContext().getString(R.string.dcloud_geo_fail)), JSUtil.ERROR, true, false);
            }
        }
    }
}
