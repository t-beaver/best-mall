package io.dcloud.js.geolocation.system;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import io.dcloud.common.adapter.util.Logger;

public class b implements LocationListener {
    private Context a;
    private LocationManager b;
    private a c;
    private boolean d = false;
    private Location e;
    private boolean f = false;
    long g = System.currentTimeMillis();

    public b(Context context, a aVar) {
        this.c = aVar;
        this.a = context;
        this.b = (LocationManager) context.getSystemService("location");
        this.f = false;
    }

    private void a(boolean z) {
        this.d = z;
        if (z) {
            this.g = System.currentTimeMillis();
        }
    }

    public void b() {
        if (this.f) {
            this.b.removeUpdates(this);
        }
        this.f = false;
    }

    public void onLocationChanged(Location location) {
        Logger.d("GpsListener: The location has been updated!");
        a(true);
        this.e = location;
        this.c.a(location, a.d);
    }

    public void onProviderDisabled(String str) {
        this.f = false;
        if (!this.d) {
            this.c.a(a.a, "GPS provider disabled.", a.d);
        }
    }

    public void onProviderEnabled(String str) {
        Logger.d("GpsListener: The provider " + str + " is enabled");
    }

    public void onStatusChanged(String str, int i, Bundle bundle) {
        Logger.d("GpsListener: The status of the provider " + str + " has changed");
        if (i == 0) {
            Logger.d("GpsListener: " + str + " is OUT OF SERVICE");
            this.c.a(a.a, "GPS out of service.", a.d);
        } else if (i == 1) {
            Logger.d("GpsListener: " + str + " is TEMPORARILY_UNAVAILABLE");
        } else {
            Logger.d("GpsListener: " + str + " is Available");
        }
    }

    public boolean a() {
        if (!(System.currentTimeMillis() - this.g < 10000)) {
            this.d = false;
        }
        return this.d;
    }

    public void a(int i) {
        if (!this.f) {
            this.f = true;
            this.b.requestLocationUpdates("gps", (long) i, 0.0f, this);
        }
    }
}
