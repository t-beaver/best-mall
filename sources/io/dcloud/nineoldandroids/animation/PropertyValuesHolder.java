package io.dcloud.nineoldandroids.animation;

import android.util.Log;
import io.dcloud.nineoldandroids.util.FloatProperty;
import io.dcloud.nineoldandroids.util.IntProperty;
import io.dcloud.nineoldandroids.util.Property;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PropertyValuesHolder implements Cloneable {
    private static Class[] DOUBLE_VARIANTS;
    private static Class[] FLOAT_VARIANTS;
    private static Class[] INTEGER_VARIANTS;
    private static final TypeEvaluator sFloatEvaluator = new FloatEvaluator();
    private static final HashMap<Class, HashMap<String, Method>> sGetterPropertyMap = new HashMap<>();
    private static final TypeEvaluator sIntEvaluator = new IntEvaluator();
    private static final HashMap<Class, HashMap<String, Method>> sSetterPropertyMap = new HashMap<>();
    private Object mAnimatedValue;
    private TypeEvaluator mEvaluator;
    private Method mGetter;
    KeyframeSet mKeyframeSet;
    protected Property mProperty;
    final ReentrantReadWriteLock mPropertyMapLock;
    String mPropertyName;
    Method mSetter;
    final Object[] mTmpValueArray;
    Class mValueType;

    static class FloatPropertyValuesHolder extends PropertyValuesHolder {
        float mFloatAnimatedValue;
        FloatKeyframeSet mFloatKeyframeSet;
        private FloatProperty mFloatProperty;

        public FloatPropertyValuesHolder(String str, FloatKeyframeSet floatKeyframeSet) {
            super(str, (PropertyValuesHolder) null);
            this.mValueType = Float.TYPE;
            this.mKeyframeSet = floatKeyframeSet;
            this.mFloatKeyframeSet = floatKeyframeSet;
        }

        /* access modifiers changed from: package-private */
        public void calculateValue(float f) {
            this.mFloatAnimatedValue = this.mFloatKeyframeSet.getFloatValue(f);
        }

        /* access modifiers changed from: package-private */
        public Object getAnimatedValue() {
            return Float.valueOf(this.mFloatAnimatedValue);
        }

        /* access modifiers changed from: package-private */
        public void setAnimatedValue(Object obj) {
            FloatProperty floatProperty = this.mFloatProperty;
            if (floatProperty != null) {
                floatProperty.setValue(obj, this.mFloatAnimatedValue);
                return;
            }
            Property property = this.mProperty;
            if (property != null) {
                property.set(obj, Float.valueOf(this.mFloatAnimatedValue));
            } else if (this.mSetter != null) {
                try {
                    this.mTmpValueArray[0] = Float.valueOf(this.mFloatAnimatedValue);
                    this.mSetter.invoke(obj, this.mTmpValueArray);
                } catch (InvocationTargetException e) {
                    Log.e("PropertyValuesHolder", e.toString());
                } catch (IllegalAccessException e2) {
                    Log.e("PropertyValuesHolder", e2.toString());
                }
            }
        }

        public void setFloatValues(float... fArr) {
            PropertyValuesHolder.super.setFloatValues(fArr);
            this.mFloatKeyframeSet = (FloatKeyframeSet) this.mKeyframeSet;
        }

        /* access modifiers changed from: package-private */
        public void setupSetter(Class cls) {
            if (this.mProperty == null) {
                PropertyValuesHolder.super.setupSetter(cls);
            }
        }

        public FloatPropertyValuesHolder clone() {
            FloatPropertyValuesHolder floatPropertyValuesHolder = (FloatPropertyValuesHolder) PropertyValuesHolder.super.clone();
            floatPropertyValuesHolder.mFloatKeyframeSet = (FloatKeyframeSet) floatPropertyValuesHolder.mKeyframeSet;
            return floatPropertyValuesHolder;
        }

        public FloatPropertyValuesHolder(Property property, FloatKeyframeSet floatKeyframeSet) {
            super(property, (PropertyValuesHolder) null);
            this.mValueType = Float.TYPE;
            this.mKeyframeSet = floatKeyframeSet;
            this.mFloatKeyframeSet = floatKeyframeSet;
            if (property instanceof FloatProperty) {
                this.mFloatProperty = (FloatProperty) this.mProperty;
            }
        }

        public FloatPropertyValuesHolder(String str, float... fArr) {
            super(str, (PropertyValuesHolder) null);
            setFloatValues(fArr);
        }

        public FloatPropertyValuesHolder(Property property, float... fArr) {
            super(property, (PropertyValuesHolder) null);
            setFloatValues(fArr);
            if (property instanceof FloatProperty) {
                this.mFloatProperty = (FloatProperty) this.mProperty;
            }
        }
    }

    static class IntPropertyValuesHolder extends PropertyValuesHolder {
        int mIntAnimatedValue;
        IntKeyframeSet mIntKeyframeSet;
        private IntProperty mIntProperty;

        public IntPropertyValuesHolder(String str, IntKeyframeSet intKeyframeSet) {
            super(str, (PropertyValuesHolder) null);
            this.mValueType = Integer.TYPE;
            this.mKeyframeSet = intKeyframeSet;
            this.mIntKeyframeSet = intKeyframeSet;
        }

        /* access modifiers changed from: package-private */
        public void calculateValue(float f) {
            this.mIntAnimatedValue = this.mIntKeyframeSet.getIntValue(f);
        }

        /* access modifiers changed from: package-private */
        public Object getAnimatedValue() {
            return Integer.valueOf(this.mIntAnimatedValue);
        }

        /* access modifiers changed from: package-private */
        public void setAnimatedValue(Object obj) {
            IntProperty intProperty = this.mIntProperty;
            if (intProperty != null) {
                intProperty.setValue(obj, this.mIntAnimatedValue);
                return;
            }
            Property property = this.mProperty;
            if (property != null) {
                property.set(obj, Integer.valueOf(this.mIntAnimatedValue));
            } else if (this.mSetter != null) {
                try {
                    this.mTmpValueArray[0] = Integer.valueOf(this.mIntAnimatedValue);
                    this.mSetter.invoke(obj, this.mTmpValueArray);
                } catch (InvocationTargetException e) {
                    Log.e("PropertyValuesHolder", e.toString());
                } catch (IllegalAccessException e2) {
                    Log.e("PropertyValuesHolder", e2.toString());
                }
            }
        }

        public void setIntValues(int... iArr) {
            PropertyValuesHolder.super.setIntValues(iArr);
            this.mIntKeyframeSet = (IntKeyframeSet) this.mKeyframeSet;
        }

        /* access modifiers changed from: package-private */
        public void setupSetter(Class cls) {
            if (this.mProperty == null) {
                PropertyValuesHolder.super.setupSetter(cls);
            }
        }

        public IntPropertyValuesHolder clone() {
            IntPropertyValuesHolder intPropertyValuesHolder = (IntPropertyValuesHolder) PropertyValuesHolder.super.clone();
            intPropertyValuesHolder.mIntKeyframeSet = (IntKeyframeSet) intPropertyValuesHolder.mKeyframeSet;
            return intPropertyValuesHolder;
        }

        public IntPropertyValuesHolder(Property property, IntKeyframeSet intKeyframeSet) {
            super(property, (PropertyValuesHolder) null);
            this.mValueType = Integer.TYPE;
            this.mKeyframeSet = intKeyframeSet;
            this.mIntKeyframeSet = intKeyframeSet;
            if (property instanceof IntProperty) {
                this.mIntProperty = (IntProperty) this.mProperty;
            }
        }

        public IntPropertyValuesHolder(String str, int... iArr) {
            super(str, (PropertyValuesHolder) null);
            setIntValues(iArr);
        }

        public IntPropertyValuesHolder(Property property, int... iArr) {
            super(property, (PropertyValuesHolder) null);
            setIntValues(iArr);
            if (property instanceof IntProperty) {
                this.mIntProperty = (IntProperty) this.mProperty;
            }
        }
    }

    static {
        Class cls = Float.TYPE;
        Class cls2 = Integer.TYPE;
        FLOAT_VARIANTS = new Class[]{cls, Float.class, Double.TYPE, cls2, Double.class, Integer.class};
        Class cls3 = Double.TYPE;
        INTEGER_VARIANTS = new Class[]{cls2, Integer.class, cls, cls3, Float.class, Double.class};
        DOUBLE_VARIANTS = new Class[]{cls3, Double.class, cls, cls2, Float.class, Integer.class};
    }

    private PropertyValuesHolder(String str) {
        this.mSetter = null;
        this.mGetter = null;
        this.mKeyframeSet = null;
        this.mPropertyMapLock = new ReentrantReadWriteLock();
        this.mTmpValueArray = new Object[1];
        this.mPropertyName = str;
    }

    static String getMethodName(String str, String str2) {
        if (str2 == null || str2.length() == 0) {
            return str;
        }
        char upperCase = Character.toUpperCase(str2.charAt(0));
        String substring = str2.substring(1);
        return String.valueOf(str) + upperCase + substring;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(7:23|24|25|27|28|33|29) */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0094, code lost:
        return r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x009f, code lost:
        r6 = r6 + 1;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0095 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.reflect.Method getPropertyFunction(java.lang.Class r9, java.lang.String r10, java.lang.Class r11) {
        /*
            r8 = this;
            java.lang.String r0 = r8.mPropertyName
            java.lang.String r10 = getMethodName(r10, r0)
            java.lang.String r0 = "PropertyValuesHolder"
            r1 = 0
            r2 = 1
            if (r11 != 0) goto L_0x0037
            java.lang.reflect.Method r9 = r9.getMethod(r10, r1)     // Catch:{ NoSuchMethodException -> 0x0012 }
            goto L_0x0089
        L_0x0012:
            r11 = move-exception
            java.lang.reflect.Method r1 = r9.getDeclaredMethod(r10, r1)     // Catch:{ NoSuchMethodException -> 0x001b }
            r1.setAccessible(r2)     // Catch:{ NoSuchMethodException -> 0x001b }
            goto L_0x0088
        L_0x001b:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            java.lang.String r10 = "Couldn't find no-arg method for property "
            r9.<init>(r10)
            java.lang.String r10 = r8.mPropertyName
            r9.append(r10)
            java.lang.String r10 = ": "
            r9.append(r10)
            r9.append(r11)
            java.lang.String r9 = r9.toString()
            android.util.Log.e(r0, r9)
            goto L_0x0088
        L_0x0037:
            java.lang.Class[] r11 = new java.lang.Class[r2]
            java.lang.Class r3 = r8.mValueType
            java.lang.Class<java.lang.Float> r4 = java.lang.Float.class
            boolean r3 = r3.equals(r4)
            r4 = 0
            if (r3 == 0) goto L_0x0047
            java.lang.Class[] r3 = FLOAT_VARIANTS
            goto L_0x0067
        L_0x0047:
            java.lang.Class r3 = r8.mValueType
            java.lang.Class<java.lang.Integer> r5 = java.lang.Integer.class
            boolean r3 = r3.equals(r5)
            if (r3 == 0) goto L_0x0054
            java.lang.Class[] r3 = INTEGER_VARIANTS
            goto L_0x0067
        L_0x0054:
            java.lang.Class r3 = r8.mValueType
            java.lang.Class<java.lang.Double> r5 = java.lang.Double.class
            boolean r3 = r3.equals(r5)
            if (r3 == 0) goto L_0x0061
            java.lang.Class[] r3 = DOUBLE_VARIANTS
            goto L_0x0067
        L_0x0061:
            java.lang.Class[] r3 = new java.lang.Class[r2]
            java.lang.Class r5 = r8.mValueType
            r3[r4] = r5
        L_0x0067:
            int r5 = r3.length
            r6 = 0
        L_0x0069:
            if (r6 < r5) goto L_0x008a
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            java.lang.String r10 = "Couldn't find setter/getter for property "
            r9.<init>(r10)
            java.lang.String r10 = r8.mPropertyName
            r9.append(r10)
            java.lang.String r10 = " with value type "
            r9.append(r10)
            java.lang.Class r10 = r8.mValueType
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            android.util.Log.e(r0, r9)
        L_0x0088:
            r9 = r1
        L_0x0089:
            return r9
        L_0x008a:
            r7 = r3[r6]
            r11[r4] = r7
            java.lang.reflect.Method r1 = r9.getMethod(r10, r11)     // Catch:{ NoSuchMethodException -> 0x0095 }
            r8.mValueType = r7     // Catch:{ NoSuchMethodException -> 0x0095 }
            return r1
        L_0x0095:
            java.lang.reflect.Method r1 = r9.getDeclaredMethod(r10, r11)     // Catch:{ NoSuchMethodException -> 0x009f }
            r1.setAccessible(r2)     // Catch:{ NoSuchMethodException -> 0x009f }
            r8.mValueType = r7     // Catch:{ NoSuchMethodException -> 0x009f }
            return r1
        L_0x009f:
            int r6 = r6 + 1
            goto L_0x0069
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.nineoldandroids.animation.PropertyValuesHolder.getPropertyFunction(java.lang.Class, java.lang.String, java.lang.Class):java.lang.reflect.Method");
    }

    public static PropertyValuesHolder ofFloat(String str, float... fArr) {
        return new FloatPropertyValuesHolder(str, fArr);
    }

    public static PropertyValuesHolder ofInt(String str, int... iArr) {
        return new IntPropertyValuesHolder(str, iArr);
    }

    public static PropertyValuesHolder ofKeyframe(String str, Keyframe... keyframeArr) {
        KeyframeSet ofKeyframe = KeyframeSet.ofKeyframe(keyframeArr);
        if (ofKeyframe instanceof IntKeyframeSet) {
            return new IntPropertyValuesHolder(str, (IntKeyframeSet) ofKeyframe);
        }
        if (ofKeyframe instanceof FloatKeyframeSet) {
            return new FloatPropertyValuesHolder(str, (FloatKeyframeSet) ofKeyframe);
        }
        PropertyValuesHolder propertyValuesHolder = new PropertyValuesHolder(str);
        propertyValuesHolder.mKeyframeSet = ofKeyframe;
        propertyValuesHolder.mValueType = keyframeArr[0].getType();
        return propertyValuesHolder;
    }

    public static PropertyValuesHolder ofObject(String str, TypeEvaluator typeEvaluator, Object... objArr) {
        PropertyValuesHolder propertyValuesHolder = new PropertyValuesHolder(str);
        propertyValuesHolder.setObjectValues(objArr);
        propertyValuesHolder.setEvaluator(typeEvaluator);
        return propertyValuesHolder;
    }

    private void setupGetter(Class cls) {
        this.mGetter = setupSetterOrGetter(cls, sGetterPropertyMap, "get", (Class) null);
    }

    private Method setupSetterOrGetter(Class cls, HashMap<Class, HashMap<String, Method>> hashMap, String str, Class cls2) {
        try {
            this.mPropertyMapLock.writeLock().lock();
            HashMap hashMap2 = hashMap.get(cls);
            Method method = hashMap2 != null ? (Method) hashMap2.get(this.mPropertyName) : null;
            if (method == null) {
                method = getPropertyFunction(cls, str, cls2);
                if (hashMap2 == null) {
                    hashMap2 = new HashMap();
                    hashMap.put(cls, hashMap2);
                }
                hashMap2.put(this.mPropertyName, method);
            }
            return method;
        } finally {
            this.mPropertyMapLock.writeLock().unlock();
        }
    }

    private void setupValue(Object obj, Keyframe keyframe) {
        Property property = this.mProperty;
        if (property != null) {
            keyframe.setValue(property.get(obj));
        }
        try {
            if (this.mGetter == null) {
                setupGetter(obj.getClass());
            }
            keyframe.setValue(this.mGetter.invoke(obj, new Object[0]));
        } catch (InvocationTargetException e) {
            Log.e("PropertyValuesHolder", e.toString());
        } catch (IllegalAccessException e2) {
            Log.e("PropertyValuesHolder", e2.toString());
        }
    }

    /* access modifiers changed from: package-private */
    public void calculateValue(float f) {
        this.mAnimatedValue = this.mKeyframeSet.getValue(f);
    }

    /* access modifiers changed from: package-private */
    public Object getAnimatedValue() {
        return this.mAnimatedValue;
    }

    public String getPropertyName() {
        return this.mPropertyName;
    }

    /* access modifiers changed from: package-private */
    public void init() {
        TypeEvaluator typeEvaluator;
        if (this.mEvaluator == null) {
            Class<Float> cls = this.mValueType;
            if (cls == Integer.class) {
                typeEvaluator = sIntEvaluator;
            } else {
                typeEvaluator = cls == Float.class ? sFloatEvaluator : null;
            }
            this.mEvaluator = typeEvaluator;
        }
        TypeEvaluator typeEvaluator2 = this.mEvaluator;
        if (typeEvaluator2 != null) {
            this.mKeyframeSet.setEvaluator(typeEvaluator2);
        }
    }

    /* access modifiers changed from: package-private */
    public void setAnimatedValue(Object obj) {
        Property property = this.mProperty;
        if (property != null) {
            property.set(obj, getAnimatedValue());
        }
        if (this.mSetter != null) {
            try {
                this.mTmpValueArray[0] = getAnimatedValue();
                this.mSetter.invoke(obj, this.mTmpValueArray);
            } catch (InvocationTargetException e) {
                Log.e("PropertyValuesHolder", e.toString());
            } catch (IllegalAccessException e2) {
                Log.e("PropertyValuesHolder", e2.toString());
            }
        }
    }

    public void setEvaluator(TypeEvaluator typeEvaluator) {
        this.mEvaluator = typeEvaluator;
        this.mKeyframeSet.setEvaluator(typeEvaluator);
    }

    public void setFloatValues(float... fArr) {
        this.mValueType = Float.TYPE;
        this.mKeyframeSet = KeyframeSet.ofFloat(fArr);
    }

    public void setIntValues(int... iArr) {
        this.mValueType = Integer.TYPE;
        this.mKeyframeSet = KeyframeSet.ofInt(iArr);
    }

    public void setKeyframes(Keyframe... keyframeArr) {
        int length = keyframeArr.length;
        Keyframe[] keyframeArr2 = new Keyframe[Math.max(length, 2)];
        this.mValueType = keyframeArr[0].getType();
        for (int i = 0; i < length; i++) {
            keyframeArr2[i] = keyframeArr[i];
        }
        this.mKeyframeSet = new KeyframeSet(keyframeArr2);
    }

    public void setObjectValues(Object... objArr) {
        this.mValueType = objArr[0].getClass();
        this.mKeyframeSet = KeyframeSet.ofObject(objArr);
    }

    public void setProperty(Property property) {
        this.mProperty = property;
    }

    public void setPropertyName(String str) {
        this.mPropertyName = str;
    }

    /* access modifiers changed from: package-private */
    public void setupEndValue(Object obj) {
        ArrayList<Keyframe> arrayList = this.mKeyframeSet.mKeyframes;
        setupValue(obj, arrayList.get(arrayList.size() - 1));
    }

    /* access modifiers changed from: package-private */
    public void setupSetter(Class cls) {
        this.mSetter = setupSetterOrGetter(cls, sSetterPropertyMap, "set", this.mValueType);
    }

    /* access modifiers changed from: package-private */
    public void setupSetterAndGetter(Object obj) {
        Property property = this.mProperty;
        if (property != null) {
            try {
                property.get(obj);
                Iterator<Keyframe> it = this.mKeyframeSet.mKeyframes.iterator();
                while (it.hasNext()) {
                    Keyframe next = it.next();
                    if (!next.hasValue()) {
                        next.setValue(this.mProperty.get(obj));
                    }
                }
                return;
            } catch (ClassCastException unused) {
                Log.e("PropertyValuesHolder", "No such property (" + this.mProperty.getName() + ") on target object " + obj + ". Trying reflection instead");
                this.mProperty = null;
            }
        }
        Class<?> cls = obj.getClass();
        if (this.mSetter == null) {
            setupSetter(cls);
        }
        Iterator<Keyframe> it2 = this.mKeyframeSet.mKeyframes.iterator();
        while (it2.hasNext()) {
            Keyframe next2 = it2.next();
            if (!next2.hasValue()) {
                if (this.mGetter == null) {
                    setupGetter(cls);
                }
                try {
                    next2.setValue(this.mGetter.invoke(obj, new Object[0]));
                } catch (InvocationTargetException e) {
                    Log.e("PropertyValuesHolder", e.toString());
                } catch (IllegalAccessException e2) {
                    Log.e("PropertyValuesHolder", e2.toString());
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setupStartValue(Object obj) {
        setupValue(obj, this.mKeyframeSet.mKeyframes.get(0));
    }

    public String toString() {
        return String.valueOf(this.mPropertyName) + ": " + this.mKeyframeSet.toString();
    }

    public static PropertyValuesHolder ofFloat(Property<?, Float> property, float... fArr) {
        return new FloatPropertyValuesHolder((Property) property, fArr);
    }

    public static PropertyValuesHolder ofInt(Property<?, Integer> property, int... iArr) {
        return new IntPropertyValuesHolder((Property) property, iArr);
    }

    public PropertyValuesHolder clone() {
        try {
            PropertyValuesHolder propertyValuesHolder = (PropertyValuesHolder) super.clone();
            propertyValuesHolder.mPropertyName = this.mPropertyName;
            propertyValuesHolder.mProperty = this.mProperty;
            propertyValuesHolder.mKeyframeSet = this.mKeyframeSet.clone();
            propertyValuesHolder.mEvaluator = this.mEvaluator;
            return propertyValuesHolder;
        } catch (CloneNotSupportedException unused) {
            return null;
        }
    }

    public static <V> PropertyValuesHolder ofObject(Property property, TypeEvaluator<V> typeEvaluator, V... vArr) {
        PropertyValuesHolder propertyValuesHolder = new PropertyValuesHolder(property);
        propertyValuesHolder.setObjectValues(vArr);
        propertyValuesHolder.setEvaluator(typeEvaluator);
        return propertyValuesHolder;
    }

    public static PropertyValuesHolder ofKeyframe(Property property, Keyframe... keyframeArr) {
        KeyframeSet ofKeyframe = KeyframeSet.ofKeyframe(keyframeArr);
        if (ofKeyframe instanceof IntKeyframeSet) {
            return new IntPropertyValuesHolder(property, (IntKeyframeSet) ofKeyframe);
        }
        if (ofKeyframe instanceof FloatKeyframeSet) {
            return new FloatPropertyValuesHolder(property, (FloatKeyframeSet) ofKeyframe);
        }
        PropertyValuesHolder propertyValuesHolder = new PropertyValuesHolder(property);
        propertyValuesHolder.mKeyframeSet = ofKeyframe;
        propertyValuesHolder.mValueType = keyframeArr[0].getType();
        return propertyValuesHolder;
    }

    /* synthetic */ PropertyValuesHolder(String str, PropertyValuesHolder propertyValuesHolder) {
        this(str);
    }

    private PropertyValuesHolder(Property property) {
        this.mSetter = null;
        this.mGetter = null;
        this.mKeyframeSet = null;
        this.mPropertyMapLock = new ReentrantReadWriteLock();
        this.mTmpValueArray = new Object[1];
        this.mProperty = property;
        if (property != null) {
            this.mPropertyName = property.getName();
        }
    }

    /* synthetic */ PropertyValuesHolder(Property property, PropertyValuesHolder propertyValuesHolder) {
        this(property);
    }
}
