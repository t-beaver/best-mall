package com.alibaba.android.bindingx.core.internal;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;
import com.alibaba.android.bindingx.core.LogProxy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

class OrientationDetector implements SensorEventListener {
    private static final Set<Integer> DEVICE_ORIENTATION_SENSORS_A = Utils.newHashSet(15);
    private static final Set<Integer> DEVICE_ORIENTATION_SENSORS_B = Utils.newHashSet(11);
    private static final Set<Integer> DEVICE_ORIENTATION_SENSORS_C = Utils.newHashSet(1, 2);
    private static OrientationDetector sSingleton;
    private static final Object sSingletonLock = new Object();
    private final Set<Integer> mActiveSensors = new HashSet();
    private final Context mAppContext;
    private boolean mDeviceOrientationIsActive;
    private boolean mDeviceOrientationIsActiveWithBackupSensors;
    private Set<Integer> mDeviceOrientationSensors;
    private float[] mDeviceRotationMatrix;
    private Handler mHandler;
    private ArrayList<OnOrientationChangedListener> mListeners = new ArrayList<>();
    private float[] mMagneticFieldVector;
    private boolean mOrientationNotAvailable;
    private final List<Set<Integer>> mOrientationSensorSets;
    private double[] mRotationAngles;
    SensorManagerProxy mSensorManagerProxy;
    private HandlerThread mThread;
    private float[] mTruncatedRotationVector;

    interface OnOrientationChangedListener {
        void onOrientationChanged(double d, double d2, double d3);
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    private OrientationDetector(Context context) {
        this.mAppContext = context.getApplicationContext();
        this.mOrientationSensorSets = Utils.newArrayList(DEVICE_ORIENTATION_SENSORS_A, DEVICE_ORIENTATION_SENSORS_B, DEVICE_ORIENTATION_SENSORS_C);
    }

    static OrientationDetector getInstance(Context context) {
        OrientationDetector orientationDetector;
        synchronized (sSingletonLock) {
            if (sSingleton == null) {
                sSingleton = new OrientationDetector(context);
            }
            orientationDetector = sSingleton;
        }
        return orientationDetector;
    }

    /* access modifiers changed from: package-private */
    public void addOrientationChangedListener(OnOrientationChangedListener onOrientationChangedListener) {
        ArrayList<OnOrientationChangedListener> arrayList = this.mListeners;
        if (arrayList != null && !arrayList.contains(onOrientationChangedListener)) {
            this.mListeners.add(onOrientationChangedListener);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean removeOrientationChangedListener(OnOrientationChangedListener onOrientationChangedListener) {
        ArrayList<OnOrientationChangedListener> arrayList = this.mListeners;
        if (arrayList == null) {
            return false;
        }
        if (onOrientationChangedListener != null) {
            return arrayList.remove(onOrientationChangedListener);
        }
        arrayList.clear();
        return true;
    }

    private boolean registerOrientationSensorsWithFallback(int i) {
        if (this.mOrientationNotAvailable) {
            return false;
        }
        if (this.mDeviceOrientationSensors != null) {
            String orientationSensorTypeUsed = getOrientationSensorTypeUsed();
            LogProxy.d("[OrientationDetector] register sensor:" + orientationSensorTypeUsed);
            return registerSensors(this.mDeviceOrientationSensors, i, true);
        }
        ensureRotationStructuresAllocated();
        for (Set<Integer> next : this.mOrientationSensorSets) {
            this.mDeviceOrientationSensors = next;
            if (registerSensors(next, i, true)) {
                String orientationSensorTypeUsed2 = getOrientationSensorTypeUsed();
                LogProxy.d("[OrientationDetector] register sensor:" + orientationSensorTypeUsed2);
                return true;
            }
        }
        this.mOrientationNotAvailable = true;
        this.mDeviceOrientationSensors = null;
        this.mDeviceRotationMatrix = null;
        this.mRotationAngles = null;
        return false;
    }

    private String getOrientationSensorTypeUsed() {
        if (this.mOrientationNotAvailable) {
            return "NOT_AVAILABLE";
        }
        Set<Integer> set = this.mDeviceOrientationSensors;
        if (set == DEVICE_ORIENTATION_SENSORS_A) {
            return "GAME_ROTATION_VECTOR";
        }
        if (set == DEVICE_ORIENTATION_SENSORS_B) {
            return "ROTATION_VECTOR";
        }
        if (set == DEVICE_ORIENTATION_SENSORS_C) {
            return "ACCELEROMETER_MAGNETIC";
        }
        return "NOT_AVAILABLE";
    }

    public boolean start(int i) {
        LogProxy.d("[OrientationDetector] sensor started");
        boolean registerOrientationSensorsWithFallback = registerOrientationSensorsWithFallback(i);
        if (registerOrientationSensorsWithFallback) {
            setEventTypeActive(true);
        }
        return registerOrientationSensorsWithFallback;
    }

    /* access modifiers changed from: package-private */
    public void stop() {
        LogProxy.d("[OrientationDetector] sensor stopped");
        unregisterSensors(new HashSet(this.mActiveSensors));
        setEventTypeActive(false);
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        int type = sensorEvent.sensor.getType();
        float[] fArr = sensorEvent.values;
        if (type != 1) {
            if (type != 2) {
                if (type != 11) {
                    if (type != 15) {
                        LogProxy.e("unexpected sensor type:" + type);
                    } else if (this.mDeviceOrientationIsActive) {
                        convertRotationVectorToAngles(fArr, this.mRotationAngles);
                        double[] dArr = this.mRotationAngles;
                        gotOrientation(dArr[0], dArr[1], dArr[2]);
                    }
                } else if (this.mDeviceOrientationIsActive && this.mDeviceOrientationSensors == DEVICE_ORIENTATION_SENSORS_B) {
                    convertRotationVectorToAngles(fArr, this.mRotationAngles);
                    double[] dArr2 = this.mRotationAngles;
                    gotOrientation(dArr2[0], dArr2[1], dArr2[2]);
                }
            } else if (this.mDeviceOrientationIsActiveWithBackupSensors) {
                if (this.mMagneticFieldVector == null) {
                    this.mMagneticFieldVector = new float[3];
                }
                float[] fArr2 = this.mMagneticFieldVector;
                System.arraycopy(fArr, 0, fArr2, 0, fArr2.length);
            }
        } else if (this.mDeviceOrientationIsActiveWithBackupSensors) {
            getOrientationFromGeomagneticVectors(fArr, this.mMagneticFieldVector);
        }
    }

    private static double[] computeDeviceOrientationFromRotationMatrix(float[] fArr, double[] dArr) {
        float[] fArr2 = fArr;
        if (fArr2.length != 9) {
            return dArr;
        }
        if (fArr2[8] > 0.0f) {
            dArr[0] = Math.atan2((double) (-fArr2[1]), (double) fArr2[4]);
            dArr[1] = Math.asin((double) fArr2[7]);
            dArr[2] = Math.atan2((double) (-fArr2[6]), (double) fArr2[8]);
        } else if (fArr2[8] < 0.0f) {
            dArr[0] = Math.atan2((double) fArr2[1], (double) (-fArr2[4]));
            dArr[1] = -Math.asin((double) fArr2[7]);
            dArr[1] = dArr[1] + (dArr[1] >= 0.0d ? -3.141592653589793d : 3.141592653589793d);
            dArr[2] = Math.atan2((double) fArr2[6], (double) (-fArr2[8]));
        } else {
            double d = -1.5707963267948966d;
            if (fArr2[6] > 0.0f) {
                dArr[0] = Math.atan2((double) (-fArr2[1]), (double) fArr2[4]);
                dArr[1] = Math.asin((double) fArr2[7]);
                dArr[2] = -1.5707963267948966d;
            } else if (fArr2[6] < 0.0f) {
                dArr[0] = Math.atan2((double) fArr2[1], (double) (-fArr2[4]));
                dArr[1] = -Math.asin((double) fArr2[7]);
                dArr[1] = dArr[1] + (dArr[1] >= 0.0d ? -3.141592653589793d : 3.141592653589793d);
                dArr[2] = -1.5707963267948966d;
            } else {
                dArr[0] = Math.atan2((double) fArr2[3], (double) fArr2[0]);
                if (fArr2[7] > 0.0f) {
                    d = 1.5707963267948966d;
                }
                dArr[1] = d;
                dArr[2] = 0.0d;
            }
        }
        if (dArr[0] < 0.0d) {
            dArr[0] = dArr[0] + 6.283185307179586d;
        }
        return dArr;
    }

    private void convertRotationVectorToAngles(float[] fArr, double[] dArr) {
        if (fArr.length > 4) {
            System.arraycopy(fArr, 0, this.mTruncatedRotationVector, 0, 4);
            SensorManager.getRotationMatrixFromVector(this.mDeviceRotationMatrix, this.mTruncatedRotationVector);
        } else {
            SensorManager.getRotationMatrixFromVector(this.mDeviceRotationMatrix, fArr);
        }
        computeDeviceOrientationFromRotationMatrix(this.mDeviceRotationMatrix, dArr);
        for (int i = 0; i < 3; i++) {
            dArr[i] = Math.toDegrees(dArr[i]);
        }
    }

    private void getOrientationFromGeomagneticVectors(float[] fArr, float[] fArr2) {
        if (fArr != null && fArr2 != null && SensorManager.getRotationMatrix(this.mDeviceRotationMatrix, (float[]) null, fArr, fArr2)) {
            computeDeviceOrientationFromRotationMatrix(this.mDeviceRotationMatrix, this.mRotationAngles);
            gotOrientation(Math.toDegrees(this.mRotationAngles[0]), Math.toDegrees(this.mRotationAngles[1]), Math.toDegrees(this.mRotationAngles[2]));
        }
    }

    private SensorManagerProxy getSensorManagerProxy() {
        SensorManagerProxy sensorManagerProxy = this.mSensorManagerProxy;
        if (sensorManagerProxy != null) {
            return sensorManagerProxy;
        }
        SensorManager sensorManager = (SensorManager) this.mAppContext.getSystemService("sensor");
        if (sensorManager != null) {
            this.mSensorManagerProxy = new SensorManagerProxyImpl(sensorManager);
        }
        return this.mSensorManagerProxy;
    }

    private void setEventTypeActive(boolean z) {
        this.mDeviceOrientationIsActive = z;
        this.mDeviceOrientationIsActiveWithBackupSensors = z && this.mDeviceOrientationSensors == DEVICE_ORIENTATION_SENSORS_C;
    }

    private void ensureRotationStructuresAllocated() {
        if (this.mDeviceRotationMatrix == null) {
            this.mDeviceRotationMatrix = new float[9];
        }
        if (this.mRotationAngles == null) {
            this.mRotationAngles = new double[3];
        }
        if (this.mTruncatedRotationVector == null) {
            this.mTruncatedRotationVector = new float[4];
        }
    }

    private boolean registerSensors(Set<Integer> set, int i, boolean z) {
        HashSet<Integer> hashSet = new HashSet<>(set);
        hashSet.removeAll(this.mActiveSensors);
        if (hashSet.isEmpty()) {
            return true;
        }
        boolean z2 = false;
        for (Integer num : hashSet) {
            boolean registerForSensorType = registerForSensorType(num.intValue(), i);
            if (!registerForSensorType && z) {
                unregisterSensors(hashSet);
                return false;
            } else if (registerForSensorType) {
                this.mActiveSensors.add(num);
                z2 = true;
            }
        }
        return z2;
    }

    private void unregisterSensors(Iterable<Integer> iterable) {
        for (Integer next : iterable) {
            if (this.mActiveSensors.contains(next)) {
                getSensorManagerProxy().unregisterListener(this, next.intValue());
                this.mActiveSensors.remove(next);
            }
        }
    }

    private boolean registerForSensorType(int i, int i2) {
        SensorManagerProxy sensorManagerProxy = getSensorManagerProxy();
        if (sensorManagerProxy == null) {
            return false;
        }
        return sensorManagerProxy.registerListener(this, i, i2, getHandler());
    }

    /* access modifiers changed from: package-private */
    public void gotOrientation(double d, double d2, double d3) {
        ArrayList<OnOrientationChangedListener> arrayList = this.mListeners;
        if (arrayList != null) {
            try {
                Iterator<OnOrientationChangedListener> it = arrayList.iterator();
                while (it.hasNext()) {
                    it.next().onOrientationChanged(d, d2, d3);
                }
            } catch (Throwable th) {
                LogProxy.e("[OrientationDetector] ", th);
            }
        }
    }

    private Handler getHandler() {
        if (this.mHandler == null) {
            HandlerThread handlerThread = new HandlerThread("DeviceOrientation");
            this.mThread = handlerThread;
            handlerThread.start();
            this.mHandler = new Handler(this.mThread.getLooper());
        }
        return this.mHandler;
    }
}
