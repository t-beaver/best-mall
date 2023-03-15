package com.alibaba.android.bindingx.core.internal;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import com.alibaba.android.bindingx.core.LogProxy;
import java.util.List;

class SensorManagerProxyImpl implements SensorManagerProxy {
    private final SensorManager mSensorManager;

    SensorManagerProxyImpl(SensorManager sensorManager) {
        this.mSensorManager = sensorManager;
    }

    public boolean registerListener(SensorEventListener sensorEventListener, int i, int i2, Handler handler) {
        List<Sensor> sensorList = this.mSensorManager.getSensorList(i);
        if (sensorList.isEmpty()) {
            return false;
        }
        return this.mSensorManager.registerListener(sensorEventListener, sensorList.get(0), i2, handler);
    }

    public void unregisterListener(SensorEventListener sensorEventListener, int i) {
        List<Sensor> sensorList = this.mSensorManager.getSensorList(i);
        if (!sensorList.isEmpty()) {
            try {
                this.mSensorManager.unregisterListener(sensorEventListener, sensorList.get(0));
            } catch (Throwable unused) {
                LogProxy.w("Failed to unregister device sensor " + sensorList.get(0).getName());
            }
        }
    }
}
