package io.dcloud.feature.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.constant.DOMException;
import java.util.Locale;

public class c implements SensorEventListener {
    String a;
    IWebview b;
    private SensorManager c;
    private Sensor d;
    private Sensor e;
    private Sensor f;
    private float[] g;
    private float[] h;
    private float[] i;
    private float[] j;
    private boolean k = false;

    c(IWebview iWebview, String str) {
        this.a = str;
        this.b = iWebview;
        this.g = new float[3];
        this.h = new float[3];
        this.i = new float[3];
        this.j = new float[9];
        SensorManager sensorManager = (SensorManager) iWebview.getContext().getSystemService("sensor");
        this.c = sensorManager;
        this.d = sensorManager.getDefaultSensor(2);
        this.e = this.c.getDefaultSensor(1);
        this.f = this.c.getDefaultSensor(3);
    }

    public void a() {
        this.c.registerListener(this, this.d, 1);
        this.c.registerListener(this, this.e, 1);
        this.c.registerListener(this, this.f, 1);
    }

    public void b() {
        this.c.unregisterListener(this);
        this.d = null;
        this.e = null;
    }

    public void onAccuracyChanged(Sensor sensor, int i2) {
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        int type = sensorEvent.sensor.getType();
        if (type != 1) {
            if (type != 2) {
                if (type == 3) {
                    float[] fArr = sensorEvent.values;
                    if (fArr != null) {
                        this.k = true;
                        a(((float) Math.round(fArr[0] * 100.0f)) / 100.0f, ((float) Math.round(sensorEvent.values[1] * 100.0f)) / 100.0f, ((float) Math.round(sensorEvent.values[2] * 100.0f)) / 100.0f);
                        return;
                    }
                    a(0, "NO Accelerometer Message");
                }
            } else if (!this.k) {
                this.g = (float[]) sensorEvent.values.clone();
            }
        } else if (!this.k) {
            float[] fArr2 = (float[]) sensorEvent.values.clone();
            this.h = fArr2;
            float[] fArr3 = this.g;
            if (fArr3 != null) {
                SensorManager.getRotationMatrix(this.j, (float[]) null, fArr2, fArr3);
                SensorManager.getOrientation(this.j, this.i);
                float[] fArr4 = this.i;
                if (fArr4 != null) {
                    float degrees = (float) Math.toDegrees((double) fArr4[0]);
                    if (degrees < 0.0f) {
                        degrees += 360.0f;
                    }
                    float degrees2 = (float) Math.toDegrees((double) this.i[1]);
                    float f2 = -((float) Math.toDegrees((double) this.i[2]));
                    if (degrees != 0.0f && degrees2 != 0.0f) {
                        a(degrees, degrees2, f2);
                        return;
                    }
                    return;
                }
                a(0, "NO Accelerometer Message");
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void a(float f2, float f3, float f4) {
        Deprecated_JSUtil.excCallbackSuccess(this.b, this.a, String.format(Locale.ENGLISH, "{alpha:%f,beta:%f,gamma:%f,magneticHeading:%f,trueHeading:%f,headingAccuracy:%f}", new Object[]{Float.valueOf(f2), Float.valueOf(-f3), Float.valueOf(-f4), Float.valueOf(f2), Float.valueOf(f2), Float.valueOf(0.0f)}), true, true);
    }

    /* access modifiers changed from: package-private */
    public void a(int i2, String str) {
        b();
        Deprecated_JSUtil.excCallbackError(this.b, this.a, DOMException.toJSON(i2, str), true);
    }
}
