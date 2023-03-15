package io.dcloud.js.geolocation.system;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import java.util.Timer;
import java.util.TimerTask;

public class a {
    public static int a = 2;
    public static int b = 0;
    public static int c = 1;
    public static int d = 0;
    public static int e = 1;
    public static int f = 2;
    public int g = Integer.MAX_VALUE;
    private Timer h;
    private b i;
    private TimerTask j;
    String k;
    b l;
    c m;
    String n;
    IWebview o;
    private Context p;
    LocationManager q;
    String r = null;
    String s = null;
    IWebview t = null;
    int u = 0;

    /* renamed from: io.dcloud.js.geolocation.system.a$a  reason: collision with other inner class name */
    class C0072a extends TimerTask {
        C0072a() {
        }

        public void run() {
            a aVar = a.this;
            if (aVar.o != null && aVar.n != null && !PdrUtil.isEmpty(aVar.r)) {
                a aVar2 = a.this;
                Deprecated_JSUtil.excCallbackSuccess(aVar2.o, aVar2.n, aVar2.r, true, true);
            }
        }
    }

    class b extends TimerTask {
        b() {
        }

        public void run() {
            a aVar = a.this;
            if (aVar.l != null || aVar.m != null) {
                aVar.a(a.a, "get location fail.", a.f);
            }
        }
    }

    a(Context context, String str) {
        this.k = str;
        this.p = context;
        this.l = null;
        this.m = null;
        this.q = (LocationManager) context.getSystemService("location");
        if (this.h == null) {
            this.h = new Timer();
        }
    }

    public void a() {
        c(f);
    }

    /* access modifiers changed from: package-private */
    public boolean b(IWebview iWebview, int i2, String str, int i3) {
        this.o = iWebview;
        this.n = str;
        this.g = i3;
        return a(i2, c);
    }

    /* access modifiers changed from: package-private */
    public void c(int i2) {
        a(-1);
        if (this.u <= 0) {
            if (i2 == d) {
                b bVar = this.l;
                if (bVar != null) {
                    bVar.b();
                    this.l = null;
                }
            } else if (i2 == e) {
                c cVar = this.m;
                if (cVar != null) {
                    cVar.a();
                    this.m = null;
                }
            } else {
                b bVar2 = this.l;
                if (bVar2 != null) {
                    bVar2.b();
                    this.l = null;
                }
                c cVar2 = this.m;
                if (cVar2 != null) {
                    cVar2.a();
                    this.m = null;
                }
            }
            if (this.h != null) {
                b bVar3 = this.i;
                if (bVar3 != null) {
                    bVar3.cancel();
                    this.i = null;
                }
                TimerTask timerTask = this.j;
                if (timerTask != null) {
                    timerTask.cancel();
                    this.j = null;
                }
            }
            this.r = null;
            this.u = 0;
        }
        Logger.d("GeoListener", "mUseCount=" + this.u);
    }

    private String a(Location location, String str) {
        return StringUtil.format("{latitude:%f,longitude:%f,altitude:%f,accuracy:%f,heading:%f,velocity:%f,altitudeAccuracy:%d,timestamp:new Date('%s'),coordsType:'%s'}", Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude()), Double.valueOf(location.getAltitude()), Float.valueOf(location.getAccuracy()), Float.valueOf(location.getBearing()), Float.valueOf(location.getSpeed()), 0, Long.valueOf(location.getTime()), str);
    }

    private void b(int i2) {
        if (this.h != null) {
            b bVar = this.i;
            if (bVar != null) {
                bVar.cancel();
            }
            b bVar2 = new b();
            this.i = bVar2;
            this.h.schedule(bVar2, (long) i2);
        }
    }

    /* access modifiers changed from: package-private */
    public void a(Location location, int i2) {
        String str;
        IWebview iWebview;
        Log.i("geoListener", "successType==" + i2);
        String a2 = a(location, "wgs84");
        String str2 = this.s;
        if (!(str2 == null || (iWebview = this.t) == null)) {
            Deprecated_JSUtil.excCallbackSuccess(iWebview, str2, a2, true, false);
            c(f);
            this.s = null;
            this.t = null;
        }
        IWebview iWebview2 = this.o;
        if (iWebview2 != null && (str = this.n) != null) {
            if (this.r == null) {
                Deprecated_JSUtil.excCallbackSuccess(iWebview2, str, a2, true, true);
            }
            this.r = a2;
        }
    }

    /* access modifiers changed from: package-private */
    public void a(int i2, String str, int i3) {
        String str2;
        IWebview iWebview;
        Log.i("geoListener", "failType==" + i3);
        c(i3);
        String str3 = this.s;
        if (str3 != null && (iWebview = this.t) != null && this.l == null && this.m == null) {
            Deprecated_JSUtil.excCallbackError(iWebview, str3, DOMException.toJSON(i2, str), true);
        }
        IWebview iWebview2 = this.o;
        if (iWebview2 != null && (str2 = this.n) != null && this.l == null && this.m == null) {
            this.r = null;
            Deprecated_JSUtil.excCallbackError(iWebview2, str2, DOMException.toJSON(i2, str), true);
        }
    }

    /* access modifiers changed from: package-private */
    public void a(IWebview iWebview, int i2, String str, int i3) {
        this.t = iWebview;
        this.s = str;
        this.g = i3;
        a(i2, b);
    }

    private void a(int i2) {
        this.u += i2;
        Logger.d("GeoListener", "mUseCount=" + this.u);
    }

    private boolean a(int i2, int i3) {
        b bVar;
        if (this.u == 0) {
            if (this.l == null && this.q.isProviderEnabled("gps")) {
                this.l = new b(this.p, this);
            }
            if (this.m == null && this.q.isProviderEnabled("network")) {
                this.m = new c(this.p, this);
            }
            b bVar2 = this.l;
            if (bVar2 != null) {
                bVar2.a(i2);
            }
            c cVar = this.m;
            if (cVar != null) {
                cVar.a(i2);
            }
            if (i3 == b) {
                b(this.g);
            }
        }
        if (i3 == c) {
            if (!(this.h == null || (bVar = this.i) == null)) {
                bVar.cancel();
            }
            C0072a aVar = new C0072a();
            this.j = aVar;
            long j2 = (long) i2;
            this.h.schedule(aVar, j2, j2);
        }
        a(1);
        if (this.m != null || this.l != null) {
            return true;
        }
        a(a, "get location fail.", f);
        return false;
    }
}
