package io.dcloud.js.geolocation.system;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import io.dcloud.common.adapter.util.Logger;

public class c implements LocationListener {
    private Context a;
    private LocationManager b;
    private a c;
    private boolean d = false;
    private Location e;
    private boolean f = false;

    public c(Context context, a aVar) {
        this.c = aVar;
        this.a = context;
        this.b = (LocationManager) context.getSystemService("location");
        this.f = false;
    }

    public void a(int i) {
        if (!this.f) {
            this.f = true;
            this.b.requestLocationUpdates("network", (long) i, 0.0f, this);
        }
    }

    public void onLocationChanged(Location location) {
        Logger.d("NetworkListener: The location has been updated!");
        this.d = true;
        this.e = location;
        this.c.a(location, a.e);
    }

    public void onProviderDisabled(String str) {
        b bVar;
        this.f = false;
        if (!this.d && ((bVar = this.c.l) == null || !bVar.a())) {
            a aVar = this.c;
            int i = a.a;
            aVar.a(i, "The provider " + str + " is disabled", a.e);
        }
        Logger.d("NetworkListener: The provider " + str + " is disabled");
    }

    public void onProviderEnabled(String str) {
        Logger.d("NetworkListener: The provider " + str + " is enabled");
    }

    public void onStatusChanged(String str, int i, Bundle bundle) {
        Logger.d("NetworkListener: The status of the provider " + str + " has changed");
        if (i == 0) {
            Logger.d("NetworkListener: " + str + " is OUT OF SERVICE");
        } else if (i == 1) {
            Logger.d("NetworkListener: " + str + " is TEMPORARILY_UNAVAILABLE");
        } else {
            Logger.d("NetworkListener: " + str + " is Available");
        }
    }

    public void a() {
        if (this.f) {
            this.b.removeUpdates(this);
        }
        this.f = false;
    }
}
