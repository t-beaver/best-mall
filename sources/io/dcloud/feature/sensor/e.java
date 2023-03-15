package io.dcloud.feature.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.JSUtil;

public class e implements SensorEventListener {
    SensorManager a;
    IWebview b;
    private Sensor c;
    String d = null;
    String e = null;

    e(IWebview iWebview) {
        this.b = iWebview;
        SensorManager sensorManager = (SensorManager) iWebview.getContext().getSystemService("sensor");
        this.a = sensorManager;
        this.c = sensorManager.getDefaultSensor(8);
    }

    /* access modifiers changed from: package-private */
    public void a(float f) {
        String str = this.d;
        if (str != null) {
            Deprecated_JSUtil.execCallback(this.b, str, String.valueOf(f), JSUtil.OK, true, true);
            if (this.e == null) {
                b();
            }
            this.d = null;
        }
        String str2 = this.e;
        if (str2 != null) {
            Deprecated_JSUtil.execCallback(this.b, str2, String.valueOf(f), JSUtil.OK, true, true);
        }
    }

    public void b() {
        this.a.unregisterListener(this);
        this.d = null;
        this.e = null;
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == 8) {
            float[] fArr = sensorEvent.values;
            if (fArr != null) {
                a(fArr[0]);
            } else {
                a(0, "NO Proximity Message");
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void a(int i, String str) {
        b();
        String str2 = this.d;
        if (str2 != null) {
            Deprecated_JSUtil.execCallback(this.b, str2, DOMException.toJSON(i, str), JSUtil.ERROR, true, true);
            if (this.e == null) {
                b();
            }
            this.d = null;
        }
        String str3 = this.e;
        if (str3 != null) {
            Deprecated_JSUtil.execCallback(this.b, str3, DOMException.toJSON(i, str), JSUtil.ERROR, true, true);
        }
    }

    public void a() {
        this.a.registerListener(this, this.c, 500000);
    }
}
