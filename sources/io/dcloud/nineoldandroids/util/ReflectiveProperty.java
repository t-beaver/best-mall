package io.dcloud.nineoldandroids.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ReflectiveProperty<T, V> extends Property<T, V> {
    private static final String PREFIX_GET = "get";
    private static final String PREFIX_IS = "is";
    private static final String PREFIX_SET = "set";
    private Field mField;
    private Method mGetter;
    private Method mSetter;

    /* JADX WARNING: Can't wrap try/catch for region: R(3:19|20|(1:22)(2:23|24)) */
    /* JADX WARNING: Can't wrap try/catch for region: R(6:6|5|7|8|9|10) */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        r11 = r11.getField(r13);
        r10.mField = r11;
        r11 = r11.getType();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x00bb, code lost:
        if (typesMatch(r12, r11) != false) goto L_0x00bd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x00bd, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x00db, code lost:
        throw new io.dcloud.nineoldandroids.util.NoSuchPropertyException("Underlying type (" + r11 + ") " + "does not match Property type (" + r12 + com.taobao.weex.el.parse.Operators.BRACKET_END_STR);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x00ad */
    /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x003f */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x005e */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ReflectiveProperty(java.lang.Class<T> r11, java.lang.Class<V> r12, java.lang.String r13) {
        /*
            r10 = this;
            java.lang.String r0 = ")"
            java.lang.String r1 = "does not match Property type ("
            java.lang.String r2 = ") "
            java.lang.String r3 = "Underlying type ("
            r10.<init>(r12, r13)
            r4 = 0
            char r5 = r13.charAt(r4)
            char r5 = java.lang.Character.toUpperCase(r5)
            r6 = 1
            java.lang.String r7 = r13.substring(r6)
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r5 = java.lang.String.valueOf(r5)
            r8.<init>(r5)
            r8.append(r7)
            java.lang.String r5 = r8.toString()
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = "get"
            r7.<init>(r8)
            r7.append(r5)
            java.lang.String r7 = r7.toString()
            r8 = 0
            java.lang.reflect.Method r9 = r11.getMethod(r7, r8)     // Catch:{ NoSuchMethodException -> 0x003f }
            r10.mGetter = r9     // Catch:{ NoSuchMethodException -> 0x003f }
            goto L_0x0067
        L_0x003f:
            java.lang.reflect.Method r7 = r11.getDeclaredMethod(r7, r8)     // Catch:{ NoSuchMethodException -> 0x0049 }
            r10.mGetter = r7     // Catch:{ NoSuchMethodException -> 0x0049 }
            r7.setAccessible(r6)     // Catch:{ NoSuchMethodException -> 0x0049 }
            goto L_0x0067
        L_0x0049:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r9 = "is"
            r7.<init>(r9)
            r7.append(r5)
            java.lang.String r7 = r7.toString()
            java.lang.reflect.Method r9 = r11.getMethod(r7, r8)     // Catch:{ NoSuchMethodException -> 0x005e }
            r10.mGetter = r9     // Catch:{ NoSuchMethodException -> 0x005e }
            goto L_0x0067
        L_0x005e:
            java.lang.reflect.Method r7 = r11.getDeclaredMethod(r7, r8)     // Catch:{ NoSuchMethodException -> 0x00ad }
            r10.mGetter = r7     // Catch:{ NoSuchMethodException -> 0x00ad }
            r7.setAccessible(r6)     // Catch:{ NoSuchMethodException -> 0x00ad }
        L_0x0067:
            java.lang.reflect.Method r13 = r10.mGetter
            java.lang.Class r13 = r13.getReturnType()
            boolean r7 = r10.typesMatch(r12, r13)
            if (r7 == 0) goto L_0x008f
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            java.lang.String r0 = "set"
            r12.<init>(r0)
            r12.append(r5)
            java.lang.String r12 = r12.toString()
            java.lang.Class[] r0 = new java.lang.Class[r6]     // Catch:{ NoSuchMethodException -> 0x008e }
            r0[r4] = r13     // Catch:{ NoSuchMethodException -> 0x008e }
            java.lang.reflect.Method r11 = r11.getDeclaredMethod(r12, r0)     // Catch:{ NoSuchMethodException -> 0x008e }
            r10.mSetter = r11     // Catch:{ NoSuchMethodException -> 0x008e }
            r11.setAccessible(r6)     // Catch:{ NoSuchMethodException -> 0x008e }
        L_0x008e:
            return
        L_0x008f:
            io.dcloud.nineoldandroids.util.NoSuchPropertyException r11 = new io.dcloud.nineoldandroids.util.NoSuchPropertyException
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>(r3)
            r4.append(r13)
            r4.append(r2)
            r4.append(r1)
            r4.append(r12)
            r4.append(r0)
            java.lang.String r12 = r4.toString()
            r11.<init>(r12)
            throw r11
        L_0x00ad:
            java.lang.reflect.Field r11 = r11.getField(r13)     // Catch:{ NoSuchFieldException -> 0x00dc }
            r10.mField = r11     // Catch:{ NoSuchFieldException -> 0x00dc }
            java.lang.Class r11 = r11.getType()     // Catch:{ NoSuchFieldException -> 0x00dc }
            boolean r4 = r10.typesMatch(r12, r11)     // Catch:{ NoSuchFieldException -> 0x00dc }
            if (r4 == 0) goto L_0x00be
            return
        L_0x00be:
            io.dcloud.nineoldandroids.util.NoSuchPropertyException r4 = new io.dcloud.nineoldandroids.util.NoSuchPropertyException     // Catch:{ NoSuchFieldException -> 0x00dc }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ NoSuchFieldException -> 0x00dc }
            r5.<init>(r3)     // Catch:{ NoSuchFieldException -> 0x00dc }
            r5.append(r11)     // Catch:{ NoSuchFieldException -> 0x00dc }
            r5.append(r2)     // Catch:{ NoSuchFieldException -> 0x00dc }
            r5.append(r1)     // Catch:{ NoSuchFieldException -> 0x00dc }
            r5.append(r12)     // Catch:{ NoSuchFieldException -> 0x00dc }
            r5.append(r0)     // Catch:{ NoSuchFieldException -> 0x00dc }
            java.lang.String r11 = r5.toString()     // Catch:{ NoSuchFieldException -> 0x00dc }
            r4.<init>(r11)     // Catch:{ NoSuchFieldException -> 0x00dc }
            throw r4     // Catch:{ NoSuchFieldException -> 0x00dc }
        L_0x00dc:
            io.dcloud.nineoldandroids.util.NoSuchPropertyException r11 = new io.dcloud.nineoldandroids.util.NoSuchPropertyException
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            java.lang.String r0 = "No accessor method or field found for property with name "
            r12.<init>(r0)
            r12.append(r13)
            java.lang.String r12 = r12.toString()
            r11.<init>(r12)
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.nineoldandroids.util.ReflectiveProperty.<init>(java.lang.Class, java.lang.Class, java.lang.String):void");
    }

    private boolean typesMatch(Class<V> cls, Class cls2) {
        if (cls2 != cls) {
            return cls2.isPrimitive() && ((cls2 == Float.TYPE && cls == Float.class) || ((cls2 == Integer.TYPE && cls == Integer.class) || ((cls2 == Boolean.TYPE && cls == Boolean.class) || ((cls2 == Long.TYPE && cls == Long.class) || ((cls2 == Double.TYPE && cls == Double.class) || ((cls2 == Short.TYPE && cls == Short.class) || ((cls2 == Byte.TYPE && cls == Byte.class) || (cls2 == Character.TYPE && cls == Character.class))))))));
        }
        return true;
    }

    public V get(T t) {
        Method method = this.mGetter;
        if (method != null) {
            try {
                return method.invoke(t, (Object[]) null);
            } catch (IllegalAccessException unused) {
                throw new AssertionError();
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getCause());
            }
        } else {
            Field field = this.mField;
            if (field != null) {
                try {
                    return field.get(t);
                } catch (IllegalAccessException unused2) {
                    throw new AssertionError();
                }
            } else {
                throw new AssertionError();
            }
        }
    }

    public boolean isReadOnly() {
        return this.mSetter == null && this.mField == null;
    }

    public void set(T t, V v) {
        Method method = this.mSetter;
        if (method != null) {
            try {
                method.invoke(t, new Object[]{v});
            } catch (IllegalAccessException unused) {
                throw new AssertionError();
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getCause());
            }
        } else {
            Field field = this.mField;
            if (field != null) {
                try {
                    field.set(t, v);
                } catch (IllegalAccessException unused2) {
                    throw new AssertionError();
                }
            } else {
                throw new UnsupportedOperationException("Property " + getName() + " is read-only");
            }
        }
    }
}
