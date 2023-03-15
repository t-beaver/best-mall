package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JavaBeanInfo {
    public final Method buildMethod;
    public final Class<?> builderClass;
    public final Class<?> clazz;
    public final Constructor<?> creatorConstructor;
    public Type[] creatorConstructorParameterTypes;
    public String[] creatorConstructorParameters;
    public final Constructor<?> defaultConstructor;
    public final int defaultConstructorParameterSize;
    public final Method factoryMethod;
    public final FieldInfo[] fields;
    public final JSONType jsonType;

    /* renamed from: kotlin  reason: collision with root package name */
    public boolean f0kotlin;
    public Constructor<?> kotlinDefaultConstructor;
    public String[] orders;
    public final int parserFeatures;
    public final FieldInfo[] sortedFields;
    public final String typeKey;
    public final String typeName;

    public JavaBeanInfo(Class<?> cls, Class<?> cls2, Constructor<?> constructor, Constructor<?> constructor2, Method method, Method method2, JSONType jSONType, List<FieldInfo> list) {
        JSONField jSONField;
        this.clazz = cls;
        this.builderClass = cls2;
        this.defaultConstructor = constructor;
        this.creatorConstructor = constructor2;
        this.factoryMethod = method;
        this.parserFeatures = TypeUtils.getParserFeatures(cls);
        this.buildMethod = method2;
        this.jsonType = jSONType;
        if (jSONType != null) {
            String typeName2 = jSONType.typeName();
            String typeKey2 = jSONType.typeKey();
            this.typeKey = typeKey2.length() <= 0 ? null : typeKey2;
            if (typeName2.length() != 0) {
                this.typeName = typeName2;
            } else {
                this.typeName = cls.getName();
            }
            String[] orders2 = jSONType.orders();
            this.orders = orders2.length == 0 ? null : orders2;
        } else {
            this.typeName = cls.getName();
            this.typeKey = null;
            this.orders = null;
        }
        FieldInfo[] fieldInfoArr = new FieldInfo[list.size()];
        this.fields = fieldInfoArr;
        list.toArray(fieldInfoArr);
        FieldInfo[] fieldInfoArr2 = new FieldInfo[fieldInfoArr.length];
        boolean z = false;
        if (this.orders != null) {
            LinkedHashMap linkedHashMap = new LinkedHashMap(list.size());
            for (FieldInfo fieldInfo : fieldInfoArr) {
                linkedHashMap.put(fieldInfo.name, fieldInfo);
            }
            int i = 0;
            for (String str : this.orders) {
                FieldInfo fieldInfo2 = (FieldInfo) linkedHashMap.get(str);
                if (fieldInfo2 != null) {
                    fieldInfoArr2[i] = fieldInfo2;
                    linkedHashMap.remove(str);
                    i++;
                }
            }
            for (FieldInfo fieldInfo3 : linkedHashMap.values()) {
                fieldInfoArr2[i] = fieldInfo3;
                i++;
            }
        } else {
            System.arraycopy(fieldInfoArr, 0, fieldInfoArr2, 0, fieldInfoArr.length);
            Arrays.sort(fieldInfoArr2);
        }
        this.sortedFields = Arrays.equals(this.fields, fieldInfoArr2) ? this.fields : fieldInfoArr2;
        if (constructor != null) {
            this.defaultConstructorParameterSize = constructor.getParameterTypes().length;
        } else if (method != null) {
            this.defaultConstructorParameterSize = method.getParameterTypes().length;
        } else {
            this.defaultConstructorParameterSize = 0;
        }
        if (constructor2 != null) {
            this.creatorConstructorParameterTypes = constructor2.getParameterTypes();
            boolean isKotlin = TypeUtils.isKotlin(cls);
            this.f0kotlin = isKotlin;
            if (isKotlin) {
                this.creatorConstructorParameters = TypeUtils.getKoltinConstructorParameters(cls);
                try {
                    this.kotlinDefaultConstructor = cls.getConstructor(new Class[0]);
                } catch (Throwable unused) {
                }
                Annotation[][] parameterAnnotations = TypeUtils.getParameterAnnotations((Constructor) constructor2);
                int i2 = 0;
                while (i2 < this.creatorConstructorParameters.length && i2 < parameterAnnotations.length) {
                    Annotation[] annotationArr = parameterAnnotations[i2];
                    int length = annotationArr.length;
                    int i3 = 0;
                    while (true) {
                        if (i3 >= length) {
                            jSONField = null;
                            break;
                        }
                        Annotation annotation = annotationArr[i3];
                        if (annotation instanceof JSONField) {
                            jSONField = (JSONField) annotation;
                            break;
                        }
                        i3++;
                    }
                    if (jSONField != null) {
                        String name = jSONField.name();
                        if (name.length() > 0) {
                            this.creatorConstructorParameters[i2] = name;
                        }
                    }
                    i2++;
                }
                return;
            }
            if (this.creatorConstructorParameterTypes.length == this.fields.length) {
                int i4 = 0;
                while (true) {
                    Type[] typeArr = this.creatorConstructorParameterTypes;
                    if (i4 >= typeArr.length) {
                        z = true;
                        break;
                    } else if (typeArr[i4] != this.fields[i4].fieldClass) {
                        break;
                    } else {
                        i4++;
                    }
                }
            }
            if (!z) {
                this.creatorConstructorParameters = ASMUtils.lookupParameterNames(constructor2);
            }
        }
    }

    private static FieldInfo getField(List<FieldInfo> list, String str) {
        for (FieldInfo next : list) {
            if (next.name.equals(str)) {
                return next;
            }
            Field field = next.field;
            if (field != null && next.getAnnotation() != null && field.getName().equals(str)) {
                return next;
            }
        }
        return null;
    }

    static boolean add(List<FieldInfo> list, FieldInfo fieldInfo) {
        int size = list.size() - 1;
        while (size >= 0) {
            FieldInfo fieldInfo2 = list.get(size);
            if (!fieldInfo2.name.equals(fieldInfo.name) || (fieldInfo2.getOnly && !fieldInfo.getOnly)) {
                size--;
            } else if (fieldInfo2.fieldClass.isAssignableFrom(fieldInfo.fieldClass)) {
                list.set(size, fieldInfo);
                return true;
            } else if (fieldInfo2.compareTo(fieldInfo) >= 0) {
                return false;
            } else {
                list.set(size, fieldInfo);
                return true;
            }
        }
        list.add(fieldInfo);
        return true;
    }

    public static JavaBeanInfo build(Class<?> cls, Type type, PropertyNamingStrategy propertyNamingStrategy) {
        return build(cls, type, propertyNamingStrategy, false, TypeUtils.compatibleWithJavaBean, false);
    }

    private static Map<TypeVariable, Type> buildGenericInfo(Class<?> cls) {
        Class<? super Object> superclass = cls.getSuperclass();
        HashMap hashMap = null;
        if (superclass == null) {
            return null;
        }
        while (true) {
            Class<? super Object> cls2 = superclass;
            Class<?> cls3 = cls;
            cls = cls2;
            if (cls == null || cls == Object.class) {
                return hashMap;
            }
            if (cls3.getGenericSuperclass() instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) cls3.getGenericSuperclass()).getActualTypeArguments();
                TypeVariable[] typeParameters = cls.getTypeParameters();
                for (int i = 0; i < actualTypeArguments.length; i++) {
                    if (hashMap == null) {
                        hashMap = new HashMap();
                    }
                    if (hashMap.containsKey(actualTypeArguments[i])) {
                        hashMap.put(typeParameters[i], hashMap.get(actualTypeArguments[i]));
                    } else {
                        hashMap.put(typeParameters[i], actualTypeArguments[i]);
                    }
                }
            }
            superclass = cls.getSuperclass();
        }
        return hashMap;
    }

    public static JavaBeanInfo build(Class<?> cls, Type type, PropertyNamingStrategy propertyNamingStrategy, boolean z, boolean z2) {
        return build(cls, type, propertyNamingStrategy, z, z2, false);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v6, resolved type: java.lang.Class<?>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v7, resolved type: java.lang.Class<?>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v8, resolved type: java.lang.Class<?>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v133, resolved type: java.lang.Class<?>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v134, resolved type: java.lang.Class<?>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v136, resolved type: java.lang.Class<?>} */
    /* JADX WARNING: type inference failed for: r1v33, types: [java.lang.annotation.Annotation] */
    /* JADX WARNING: type inference failed for: r0v105, types: [java.lang.annotation.Annotation] */
    /* JADX WARNING: Code restructure failed: missing block: B:136:0x02a8, code lost:
        r7 = r1;
        r22 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0011, code lost:
        r0 = r15.naming();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:364:0x07e6, code lost:
        r0 = r0.substring(3);
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:118:0x025d  */
    /* JADX WARNING: Removed duplicated region for block: B:173:0x032e  */
    /* JADX WARNING: Removed duplicated region for block: B:174:0x0334  */
    /* JADX WARNING: Removed duplicated region for block: B:178:0x033c  */
    /* JADX WARNING: Removed duplicated region for block: B:210:0x040d  */
    /* JADX WARNING: Removed duplicated region for block: B:293:0x062e  */
    /* JADX WARNING: Removed duplicated region for block: B:391:0x0866  */
    /* JADX WARNING: Removed duplicated region for block: B:393:0x086c  */
    /* JADX WARNING: Removed duplicated region for block: B:397:0x089a  */
    /* JADX WARNING: Removed duplicated region for block: B:399:0x089e  */
    /* JADX WARNING: Removed duplicated region for block: B:410:0x091c  */
    /* JADX WARNING: Removed duplicated region for block: B:413:0x092f  */
    /* JADX WARNING: Removed duplicated region for block: B:418:0x097b  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00c1  */
    /* JADX WARNING: Removed duplicated region for block: B:470:0x0ac1  */
    /* JADX WARNING: Removed duplicated region for block: B:514:0x095e A[EDGE_INSN: B:514:0x095e->B:416:0x095e ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x01ab  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.alibaba.fastjson.util.JavaBeanInfo build(java.lang.Class<?> r45, java.lang.reflect.Type r46, com.alibaba.fastjson.PropertyNamingStrategy r47, boolean r48, boolean r49, boolean r50) {
        /*
            r13 = r45
            r14 = r46
            r9 = r50
            java.lang.Class<com.alibaba.fastjson.annotation.JSONType> r0 = com.alibaba.fastjson.annotation.JSONType.class
            java.lang.annotation.Annotation r0 = com.alibaba.fastjson.util.TypeUtils.getAnnotation((java.lang.Class<?>) r13, r0)
            r15 = r0
            com.alibaba.fastjson.annotation.JSONType r15 = (com.alibaba.fastjson.annotation.JSONType) r15
            if (r15 == 0) goto L_0x001d
            com.alibaba.fastjson.PropertyNamingStrategy r0 = r15.naming()
            if (r0 == 0) goto L_0x001d
            com.alibaba.fastjson.PropertyNamingStrategy r1 = com.alibaba.fastjson.PropertyNamingStrategy.CamelCase
            if (r0 == r1) goto L_0x001d
            r12 = r0
            goto L_0x001f
        L_0x001d:
            r12 = r47
        L_0x001f:
            java.lang.Class r11 = getBuilderClass(r13, r15)
            java.lang.reflect.Field[] r10 = r45.getDeclaredFields()
            java.lang.reflect.Method[] r8 = r45.getMethods()
            java.util.Map r16 = buildGenericInfo(r45)
            boolean r17 = com.alibaba.fastjson.util.TypeUtils.isKotlin(r45)
            java.lang.reflect.Constructor[] r0 = r45.getDeclaredConstructors()
            r18 = 0
            r7 = 1
            if (r17 == 0) goto L_0x0043
            int r1 = r0.length
            if (r1 != r7) goto L_0x0040
            goto L_0x0043
        L_0x0040:
            r19 = r18
            goto L_0x0054
        L_0x0043:
            if (r11 != 0) goto L_0x004a
            java.lang.reflect.Constructor r1 = getDefaultConstructor(r13, r0)
            goto L_0x0052
        L_0x004a:
            java.lang.reflect.Constructor[] r1 = r11.getDeclaredConstructors()
            java.lang.reflect.Constructor r1 = getDefaultConstructor(r11, r1)
        L_0x0052:
            r19 = r1
        L_0x0054:
            r20 = 0
            r21 = 0
            java.util.ArrayList r6 = new java.util.ArrayList
            r6.<init>()
            if (r48 == 0) goto L_0x0086
            r0 = r13
        L_0x0060:
            if (r0 == 0) goto L_0x006e
            java.lang.reflect.Field[] r1 = r0.getDeclaredFields()
            computeFields(r13, r14, r12, r6, r1)
            java.lang.Class r0 = r0.getSuperclass()
            goto L_0x0060
        L_0x006e:
            if (r19 == 0) goto L_0x0073
            com.alibaba.fastjson.util.TypeUtils.setAccessible(r19)
        L_0x0073:
            com.alibaba.fastjson.util.JavaBeanInfo r9 = new com.alibaba.fastjson.util.JavaBeanInfo
            r4 = 0
            r0 = r9
            r1 = r45
            r2 = r11
            r3 = r19
            r5 = r21
            r8 = r6
            r6 = r20
            r7 = r15
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            return r9
        L_0x0086:
            boolean r1 = r45.isInterface()
            if (r1 != 0) goto L_0x0099
            int r1 = r45.getModifiers()
            boolean r1 = java.lang.reflect.Modifier.isAbstract(r1)
            if (r1 == 0) goto L_0x0097
            goto L_0x0099
        L_0x0097:
            r1 = 0
            goto L_0x009a
        L_0x0099:
            r1 = 1
        L_0x009a:
            if (r19 != 0) goto L_0x009e
            if (r11 == 0) goto L_0x00a0
        L_0x009e:
            if (r1 == 0) goto L_0x0424
        L_0x00a0:
            java.lang.reflect.Type r2 = com.alibaba.fastjson.JSON.getMixInAnnotations(r45)
            boolean r3 = r2 instanceof java.lang.Class
            if (r3 == 0) goto L_0x00bd
            java.lang.Class r2 = (java.lang.Class) r2
            java.lang.reflect.Constructor[] r2 = r2.getConstructors()
            java.lang.reflect.Constructor r2 = getCreatorConstructor(r2)
            if (r2 == 0) goto L_0x00bd
            java.lang.Class[] r2 = r2.getParameterTypes()     // Catch:{ NoSuchMethodException -> 0x00bd }
            java.lang.reflect.Constructor r2 = r13.getConstructor(r2)     // Catch:{ NoSuchMethodException -> 0x00bd }
            goto L_0x00bf
        L_0x00bd:
            r2 = r18
        L_0x00bf:
            if (r2 != 0) goto L_0x00c5
            java.lang.reflect.Constructor r2 = getCreatorConstructor(r0)
        L_0x00c5:
            r22 = r2
            if (r22 == 0) goto L_0x01a1
            if (r1 != 0) goto L_0x01a1
            com.alibaba.fastjson.util.TypeUtils.setAccessible(r22)
            java.lang.Class[] r9 = r22.getParameterTypes()
            int r0 = r9.length
            if (r0 <= 0) goto L_0x0199
            java.lang.annotation.Annotation[][] r3 = com.alibaba.fastjson.util.TypeUtils.getParameterAnnotations((java.lang.reflect.Constructor) r22)
            r0 = r18
            r2 = 0
        L_0x00dc:
            int r1 = r9.length
            if (r2 >= r1) goto L_0x0199
            int r1 = r3.length
            if (r2 >= r1) goto L_0x0199
            r1 = r3[r2]
            int r4 = r1.length
            r5 = 0
        L_0x00e6:
            if (r5 >= r4) goto L_0x00f9
            r7 = r1[r5]
            r50 = r1
            boolean r1 = r7 instanceof com.alibaba.fastjson.annotation.JSONField
            if (r1 == 0) goto L_0x00f3
            com.alibaba.fastjson.annotation.JSONField r7 = (com.alibaba.fastjson.annotation.JSONField) r7
            goto L_0x00fb
        L_0x00f3:
            int r5 = r5 + 1
            r1 = r50
            r7 = 1
            goto L_0x00e6
        L_0x00f9:
            r7 = r18
        L_0x00fb:
            r4 = r9[r2]
            java.lang.reflect.Type[] r1 = r22.getGenericParameterTypes()
            r5 = r1[r2]
            if (r7 == 0) goto L_0x0126
            java.lang.String r1 = r7.name()
            java.lang.reflect.Field r1 = com.alibaba.fastjson.util.TypeUtils.getField(r13, r1, r10)
            int r25 = r7.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r26 = r7.serialzeFeatures()
            int r26 = com.alibaba.fastjson.serializer.SerializerFeature.of(r26)
            com.alibaba.fastjson.parser.Feature[] r27 = r7.parseFeatures()
            int r27 = com.alibaba.fastjson.parser.Feature.of(r27)
            java.lang.String r7 = r7.name()
            goto L_0x012f
        L_0x0126:
            r1 = r18
            r7 = r1
            r25 = 0
            r26 = 0
            r27 = 0
        L_0x012f:
            if (r7 == 0) goto L_0x0137
            int r28 = r7.length()
            if (r28 != 0) goto L_0x013f
        L_0x0137:
            if (r0 != 0) goto L_0x013d
            java.lang.String[] r0 = com.alibaba.fastjson.util.ASMUtils.lookupParameterNames(r22)
        L_0x013d:
            r7 = r0[r2]
        L_0x013f:
            if (r1 != 0) goto L_0x015e
            if (r0 != 0) goto L_0x014e
            if (r17 == 0) goto L_0x014a
            java.lang.String[] r0 = com.alibaba.fastjson.util.TypeUtils.getKoltinConstructorParameters(r45)
            goto L_0x014e
        L_0x014a:
            java.lang.String[] r0 = com.alibaba.fastjson.util.ASMUtils.lookupParameterNames(r22)
        L_0x014e:
            r50 = r1
            int r1 = r0.length
            if (r1 <= r2) goto L_0x0160
            r1 = r0[r2]
            java.lang.reflect.Field r1 = com.alibaba.fastjson.util.TypeUtils.getField(r13, r1, r10)
            r28 = r0
            r29 = r1
            goto L_0x0164
        L_0x015e:
            r50 = r1
        L_0x0160:
            r29 = r50
            r28 = r0
        L_0x0164:
            com.alibaba.fastjson.util.FieldInfo r1 = new com.alibaba.fastjson.util.FieldInfo
            r0 = r1
            r50 = r9
            r9 = r1
            r1 = r7
            r30 = r2
            r2 = r45
            r31 = r3
            r7 = 3
            r3 = r4
            r14 = 2
            r4 = r5
            r5 = r29
            r14 = r6
            r6 = r25
            r23 = r12
            r12 = 1
            r7 = r26
            r12 = r8
            r8 = r27
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            add(r14, r9)
            int r2 = r30 + 1
            r9 = r50
            r8 = r12
            r6 = r14
            r12 = r23
            r0 = r28
            r3 = r31
            r7 = 1
            r14 = r46
            goto L_0x00dc
        L_0x0199:
            r14 = r6
            r23 = r12
            r12 = r8
        L_0x019d:
            r24 = 1
            goto L_0x042c
        L_0x01a1:
            r14 = r6
            r23 = r12
            r12 = r8
            java.lang.reflect.Method r21 = getFactoryMethod(r13, r12, r9)
            if (r21 == 0) goto L_0x025d
            com.alibaba.fastjson.util.TypeUtils.setAccessible(r21)
            java.lang.Class[] r8 = r21.getParameterTypes()
            int r0 = r8.length
            if (r0 <= 0) goto L_0x019d
            java.lang.annotation.Annotation[][] r12 = com.alibaba.fastjson.util.TypeUtils.getParameterAnnotations((java.lang.reflect.Method) r21)
            r0 = r18
            r7 = 0
        L_0x01bc:
            int r1 = r8.length
            if (r7 >= r1) goto L_0x024c
            r1 = r12[r7]
            int r2 = r1.length
            r5 = 0
        L_0x01c3:
            if (r5 >= r2) goto L_0x01d1
            r3 = r1[r5]
            boolean r4 = r3 instanceof com.alibaba.fastjson.annotation.JSONField
            if (r4 == 0) goto L_0x01ce
            com.alibaba.fastjson.annotation.JSONField r3 = (com.alibaba.fastjson.annotation.JSONField) r3
            goto L_0x01d3
        L_0x01ce:
            int r5 = r5 + 1
            goto L_0x01c3
        L_0x01d1:
            r3 = r18
        L_0x01d3:
            if (r3 != 0) goto L_0x01e6
            if (r9 == 0) goto L_0x01de
            boolean r1 = com.alibaba.fastjson.util.TypeUtils.isJacksonCreator(r21)
            if (r1 == 0) goto L_0x01de
            goto L_0x01e6
        L_0x01de:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException
            java.lang.String r1 = "illegal json creator"
            r0.<init>(r1)
            throw r0
        L_0x01e6:
            if (r3 == 0) goto L_0x0206
            java.lang.String r1 = r3.name()
            int r2 = r3.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r4 = r3.serialzeFeatures()
            int r4 = com.alibaba.fastjson.serializer.SerializerFeature.of(r4)
            com.alibaba.fastjson.parser.Feature[] r3 = r3.parseFeatures()
            int r3 = com.alibaba.fastjson.parser.Feature.of(r3)
            r6 = r2
            r17 = r3
            r16 = r4
            goto L_0x020d
        L_0x0206:
            r1 = r18
            r6 = 0
            r16 = 0
            r17 = 0
        L_0x020d:
            if (r1 == 0) goto L_0x0219
            int r2 = r1.length()
            if (r2 != 0) goto L_0x0216
            goto L_0x0219
        L_0x0216:
            r19 = r0
            goto L_0x0222
        L_0x0219:
            if (r0 != 0) goto L_0x021f
            java.lang.String[] r0 = com.alibaba.fastjson.util.ASMUtils.lookupParameterNames(r21)
        L_0x021f:
            r1 = r0[r7]
            goto L_0x0216
        L_0x0222:
            r3 = r8[r7]
            java.lang.reflect.Type[] r0 = r21.getGenericParameterTypes()
            r4 = r0[r7]
            java.lang.reflect.Field r5 = com.alibaba.fastjson.util.TypeUtils.getField(r13, r1, r10)
            com.alibaba.fastjson.util.FieldInfo r2 = new com.alibaba.fastjson.util.FieldInfo
            r0 = r2
            r9 = r2
            r2 = r45
            r20 = r7
            r7 = r16
            r16 = r8
            r8 = r17
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            add(r14, r9)
            int r7 = r20 + 1
            r9 = r50
            r8 = r16
            r0 = r19
            goto L_0x01bc
        L_0x024c:
            com.alibaba.fastjson.util.JavaBeanInfo r9 = new com.alibaba.fastjson.util.JavaBeanInfo
            r3 = 0
            r4 = 0
            r6 = 0
            r0 = r9
            r1 = r45
            r2 = r11
            r5 = r21
            r7 = r15
            r8 = r14
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            return r9
        L_0x025d:
            if (r1 != 0) goto L_0x019d
            java.lang.String r9 = r45.getName()
            if (r17 == 0) goto L_0x027b
            int r1 = r0.length
            if (r1 <= 0) goto L_0x027b
            java.lang.String[] r1 = com.alibaba.fastjson.util.TypeUtils.getKoltinConstructorParameters(r45)
            java.lang.reflect.Constructor r0 = com.alibaba.fastjson.util.TypeUtils.getKotlinConstructor(r0, r1)
            com.alibaba.fastjson.util.TypeUtils.setAccessible(r0)
            r22 = r0
            r7 = r1
        L_0x0276:
            r8 = 1
            r24 = 0
            goto L_0x032c
        L_0x027b:
            int r1 = r0.length
            r2 = r18
            r5 = 0
        L_0x027f:
            if (r5 >= r1) goto L_0x0328
            r3 = r0[r5]
            java.lang.Class[] r4 = r3.getParameterTypes()
            java.lang.String r6 = "org.springframework.security.web.authentication.WebAuthenticationDetails"
            boolean r6 = r9.equals(r6)
            if (r6 == 0) goto L_0x02b1
            int r6 = r4.length
            r7 = 2
            if (r6 != r7) goto L_0x02ac
            r8 = 0
            r6 = r4[r8]
            java.lang.Class<java.lang.String> r7 = java.lang.String.class
            if (r6 != r7) goto L_0x02ac
            r6 = 1
            r4 = r4[r6]
            java.lang.Class<java.lang.String> r7 = java.lang.String.class
            if (r4 != r7) goto L_0x02ac
            r3.setAccessible(r6)
            java.lang.String[] r1 = com.alibaba.fastjson.util.ASMUtils.lookupParameterNames(r3)
        L_0x02a8:
            r7 = r1
            r22 = r3
            goto L_0x0276
        L_0x02ac:
            r8 = 1
            r24 = 0
            goto L_0x0324
        L_0x02b1:
            r8 = 0
            java.lang.String r6 = "org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken"
            boolean r6 = r9.equals(r6)
            if (r6 == 0) goto L_0x02e0
            int r6 = r4.length
            r7 = 3
            if (r6 != r7) goto L_0x02ac
            r6 = r4[r8]
            java.lang.Class<java.lang.Object> r7 = java.lang.Object.class
            if (r6 != r7) goto L_0x02ac
            r6 = 1
            r7 = r4[r6]
            java.lang.Class<java.lang.Object> r8 = java.lang.Object.class
            if (r7 != r8) goto L_0x02ac
            r7 = 2
            r4 = r4[r7]
            java.lang.Class<java.util.Collection> r7 = java.util.Collection.class
            if (r4 != r7) goto L_0x02ac
            r3.setAccessible(r6)
            java.lang.String r0 = "principal"
            java.lang.String r1 = "credentials"
            java.lang.String r2 = "authorities"
            java.lang.String[] r1 = new java.lang.String[]{r0, r1, r2}
            goto L_0x02a8
        L_0x02e0:
            java.lang.String r6 = "org.springframework.security.core.authority.SimpleGrantedAuthority"
            boolean r6 = r9.equals(r6)
            if (r6 == 0) goto L_0x02fe
            int r6 = r4.length
            r8 = 1
            r24 = 0
            if (r6 != r8) goto L_0x0324
            r4 = r4[r24]
            java.lang.Class<java.lang.String> r6 = java.lang.String.class
            if (r4 != r6) goto L_0x0324
            java.lang.String r0 = "authority"
            java.lang.String[] r1 = new java.lang.String[]{r0}
            r7 = r1
            r22 = r3
            goto L_0x032c
        L_0x02fe:
            r8 = 1
            r24 = 0
            int r4 = r3.getModifiers()
            r4 = r4 & r8
            if (r4 == 0) goto L_0x030a
            r7 = 1
            goto L_0x030b
        L_0x030a:
            r7 = 0
        L_0x030b:
            if (r7 != 0) goto L_0x030e
            goto L_0x0324
        L_0x030e:
            java.lang.String[] r4 = com.alibaba.fastjson.util.ASMUtils.lookupParameterNames(r3)
            if (r4 == 0) goto L_0x0324
            int r6 = r4.length
            if (r6 != 0) goto L_0x0318
            goto L_0x0324
        L_0x0318:
            if (r22 == 0) goto L_0x0321
            if (r2 == 0) goto L_0x0321
            int r6 = r4.length
            int r7 = r2.length
            if (r6 > r7) goto L_0x0321
            goto L_0x0324
        L_0x0321:
            r22 = r3
            r2 = r4
        L_0x0324:
            int r5 = r5 + 1
            goto L_0x027f
        L_0x0328:
            r8 = 1
            r24 = 0
            r7 = r2
        L_0x032c:
            if (r7 == 0) goto L_0x0334
            java.lang.Class[] r0 = r22.getParameterTypes()
            r6 = r0
            goto L_0x0336
        L_0x0334:
            r6 = r18
        L_0x0336:
            if (r7 == 0) goto L_0x040d
            int r0 = r6.length
            int r1 = r7.length
            if (r0 != r1) goto L_0x040d
            java.lang.annotation.Annotation[][] r25 = com.alibaba.fastjson.util.TypeUtils.getParameterAnnotations((java.lang.reflect.Constructor) r22)
            r5 = 0
        L_0x0341:
            int r0 = r6.length
            if (r5 >= r0) goto L_0x03ec
            r0 = r25[r5]
            r1 = r7[r5]
            int r2 = r0.length
            r3 = 0
        L_0x034a:
            if (r3 >= r2) goto L_0x0359
            r4 = r0[r3]
            boolean r8 = r4 instanceof com.alibaba.fastjson.annotation.JSONField
            if (r8 == 0) goto L_0x0355
            com.alibaba.fastjson.annotation.JSONField r4 = (com.alibaba.fastjson.annotation.JSONField) r4
            goto L_0x035b
        L_0x0355:
            int r3 = r3 + 1
            r8 = 1
            goto L_0x034a
        L_0x0359:
            r4 = r18
        L_0x035b:
            r3 = r6[r5]
            java.lang.reflect.Type[] r0 = r22.getGenericParameterTypes()
            r8 = r0[r5]
            java.lang.reflect.Field r2 = com.alibaba.fastjson.util.TypeUtils.getField(r13, r1, r10)
            if (r2 == 0) goto L_0x0374
            if (r4 != 0) goto L_0x0374
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r0 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r0 = com.alibaba.fastjson.util.TypeUtils.getAnnotation((java.lang.reflect.Field) r2, r0)
            r4 = r0
            com.alibaba.fastjson.annotation.JSONField r4 = (com.alibaba.fastjson.annotation.JSONField) r4
        L_0x0374:
            if (r4 != 0) goto L_0x0398
            java.lang.String r0 = "org.springframework.security.core.userdetails.User"
            boolean r0 = r0.equals(r9)
            if (r0 == 0) goto L_0x0391
            java.lang.String r0 = "password"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0391
            com.alibaba.fastjson.parser.Feature r0 = com.alibaba.fastjson.parser.Feature.InitStringFieldAsEmpty
            int r0 = r0.mask
            r29 = r0
            r27 = 0
            r28 = 0
            goto L_0x03bd
        L_0x0391:
            r27 = 0
            r28 = 0
            r29 = 0
            goto L_0x03bd
        L_0x0398:
            java.lang.String r0 = r4.name()
            int r27 = r0.length()
            if (r27 == 0) goto L_0x03a3
            r1 = r0
        L_0x03a3:
            int r0 = r4.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r27 = r4.serialzeFeatures()
            int r27 = com.alibaba.fastjson.serializer.SerializerFeature.of(r27)
            com.alibaba.fastjson.parser.Feature[] r4 = r4.parseFeatures()
            int r4 = com.alibaba.fastjson.parser.Feature.of(r4)
            r29 = r4
            r28 = r27
            r27 = r0
        L_0x03bd:
            com.alibaba.fastjson.util.FieldInfo r4 = new com.alibaba.fastjson.util.FieldInfo
            r0 = r4
            r30 = r2
            r2 = r45
            r50 = r9
            r9 = r4
            r4 = r8
            r31 = r5
            r5 = r30
            r30 = r6
            r6 = r27
            r27 = r7
            r8 = 3
            r7 = r28
            r24 = 1
            r8 = r29
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            add(r14, r9)
            int r5 = r31 + 1
            r9 = r50
            r7 = r27
            r6 = r30
            r8 = 1
            r24 = 0
            goto L_0x0341
        L_0x03ec:
            r24 = 1
            if (r17 != 0) goto L_0x042c
            java.lang.String r0 = r45.getName()
            java.lang.String r1 = "javax.servlet.http.Cookie"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L_0x042c
            com.alibaba.fastjson.util.JavaBeanInfo r9 = new com.alibaba.fastjson.util.JavaBeanInfo
            r3 = 0
            r5 = 0
            r6 = 0
            r0 = r9
            r1 = r45
            r2 = r11
            r4 = r22
            r7 = r15
            r8 = r14
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            return r9
        L_0x040d:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "default constructor not found. "
            r1.append(r2)
            r1.append(r13)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x0424:
            r14 = r6
            r23 = r12
            r24 = 1
            r12 = r8
            r22 = r18
        L_0x042c:
            if (r19 == 0) goto L_0x0431
            com.alibaba.fastjson.util.TypeUtils.setAccessible(r19)
        L_0x0431:
            java.lang.String r9 = "set"
            if (r11 == 0) goto L_0x061c
            java.lang.Class<com.alibaba.fastjson.annotation.JSONPOJOBuilder> r0 = com.alibaba.fastjson.annotation.JSONPOJOBuilder.class
            java.lang.annotation.Annotation r0 = com.alibaba.fastjson.util.TypeUtils.getAnnotation((java.lang.Class<?>) r11, r0)
            com.alibaba.fastjson.annotation.JSONPOJOBuilder r0 = (com.alibaba.fastjson.annotation.JSONPOJOBuilder) r0
            if (r0 == 0) goto L_0x0444
            java.lang.String r0 = r0.withPrefix()
            goto L_0x0446
        L_0x0444:
            r0 = r18
        L_0x0446:
            if (r0 != 0) goto L_0x044b
            java.lang.String r0 = "with"
        L_0x044b:
            r8 = r0
            java.lang.reflect.Method[] r7 = r11.getMethods()
            int r6 = r7.length
            r5 = 0
        L_0x0452:
            if (r5 >= r6) goto L_0x05d2
            r2 = r7[r5]
            int r0 = r2.getModifiers()
            boolean r0 = java.lang.reflect.Modifier.isStatic(r0)
            if (r0 == 0) goto L_0x0475
        L_0x0460:
            r31 = r5
            r32 = r6
            r33 = r7
            r25 = r8
            r39 = r9
            r36 = r10
            r37 = r11
            r29 = r15
            r38 = r23
            r15 = r12
            goto L_0x05b9
        L_0x0475:
            java.lang.Class r0 = r2.getReturnType()
            boolean r0 = r0.equals(r11)
            if (r0 != 0) goto L_0x0480
            goto L_0x0460
        L_0x0480:
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r0 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r0 = com.alibaba.fastjson.util.TypeUtils.getAnnotation((java.lang.reflect.Method) r2, r0)
            com.alibaba.fastjson.annotation.JSONField r0 = (com.alibaba.fastjson.annotation.JSONField) r0
            if (r0 != 0) goto L_0x048e
            com.alibaba.fastjson.annotation.JSONField r0 = com.alibaba.fastjson.util.TypeUtils.getSuperMethodAnnotation(r13, r2)
        L_0x048e:
            r25 = r0
            if (r25 == 0) goto L_0x0514
            boolean r0 = r25.deserialize()
            if (r0 != 0) goto L_0x0499
            goto L_0x0460
        L_0x0499:
            int r26 = r25.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r0 = r25.serialzeFeatures()
            int r27 = com.alibaba.fastjson.serializer.SerializerFeature.of(r0)
            com.alibaba.fastjson.parser.Feature[] r0 = r25.parseFeatures()
            int r28 = com.alibaba.fastjson.parser.Feature.of(r0)
            java.lang.String r0 = r25.name()
            int r0 = r0.length()
            if (r0 == 0) goto L_0x04f9
            java.lang.String r1 = r25.name()
            com.alibaba.fastjson.util.FieldInfo r4 = new com.alibaba.fastjson.util.FieldInfo
            r3 = 0
            r29 = 0
            r30 = 0
            r0 = r4
            r34 = r4
            r4 = r45
            r31 = r5
            r5 = r46
            r32 = r6
            r6 = r26
            r33 = r7
            r7 = r27
            r47 = r8
            r8 = r28
            r35 = r9
            r9 = r25
            r36 = r10
            r10 = r29
            r37 = r11
            r11 = r30
            r29 = r15
            r38 = r23
            r13 = 1
            r15 = r12
            r12 = r16
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12)
            r0 = r34
            add(r14, r0)
            r25 = r47
            r39 = r35
            goto L_0x05b9
        L_0x04f9:
            r31 = r5
            r32 = r6
            r33 = r7
            r47 = r8
            r35 = r9
            r36 = r10
            r37 = r11
            r29 = r15
            r38 = r23
            r13 = 1
            r15 = r12
            r6 = r26
            r7 = r27
            r8 = r28
            goto L_0x052b
        L_0x0514:
            r31 = r5
            r32 = r6
            r33 = r7
            r47 = r8
            r35 = r9
            r36 = r10
            r37 = r11
            r29 = r15
            r38 = r23
            r13 = 1
            r15 = r12
            r6 = 0
            r7 = 0
            r8 = 0
        L_0x052b:
            java.lang.String r0 = r2.getName()
            r12 = r35
            boolean r1 = r0.startsWith(r12)
            if (r1 == 0) goto L_0x0548
            int r1 = r0.length()
            r11 = 3
            if (r1 <= r11) goto L_0x0549
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r0 = r0.substring(r11)
            r1.<init>(r0)
            goto L_0x0554
        L_0x0548:
            r11 = 3
        L_0x0549:
            int r1 = r47.length()
            if (r1 != 0) goto L_0x0558
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>(r0)
        L_0x0554:
            r10 = r47
        L_0x0556:
            r9 = 0
            goto L_0x057e
        L_0x0558:
            r10 = r47
            boolean r1 = r0.startsWith(r10)
            if (r1 != 0) goto L_0x0565
        L_0x0560:
            r25 = r10
            r39 = r12
            goto L_0x05b9
        L_0x0565:
            int r1 = r0.length()
            int r3 = r10.length()
            if (r1 > r3) goto L_0x0570
            goto L_0x0560
        L_0x0570:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            int r3 = r10.length()
            java.lang.String r0 = r0.substring(r3)
            r1.<init>(r0)
            goto L_0x0556
        L_0x057e:
            char r0 = r1.charAt(r9)
            int r3 = r10.length()
            if (r3 == 0) goto L_0x058f
            boolean r3 = java.lang.Character.isUpperCase(r0)
            if (r3 != 0) goto L_0x058f
            goto L_0x0560
        L_0x058f:
            char r0 = java.lang.Character.toLowerCase(r0)
            r1.setCharAt(r9, r0)
            java.lang.String r1 = r1.toString()
            com.alibaba.fastjson.util.FieldInfo r5 = new com.alibaba.fastjson.util.FieldInfo
            r3 = 0
            r23 = 0
            r24 = 0
            r0 = r5
            r4 = r45
            r13 = r5
            r5 = r46
            r9 = r25
            r25 = r10
            r10 = r23
            r11 = r24
            r39 = r12
            r12 = r16
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12)
            add(r14, r13)
        L_0x05b9:
            int r5 = r31 + 1
            r13 = r45
            r12 = r15
            r8 = r25
            r15 = r29
            r6 = r32
            r7 = r33
            r10 = r36
            r11 = r37
            r23 = r38
            r9 = r39
            r24 = 1
            goto L_0x0452
        L_0x05d2:
            r39 = r9
            r36 = r10
            r13 = r11
            r29 = r15
            r38 = r23
            r15 = r12
            if (r13 == 0) goto L_0x0626
            java.lang.Class<com.alibaba.fastjson.annotation.JSONPOJOBuilder> r0 = com.alibaba.fastjson.annotation.JSONPOJOBuilder.class
            java.lang.annotation.Annotation r0 = com.alibaba.fastjson.util.TypeUtils.getAnnotation((java.lang.Class<?>) r13, r0)
            com.alibaba.fastjson.annotation.JSONPOJOBuilder r0 = (com.alibaba.fastjson.annotation.JSONPOJOBuilder) r0
            if (r0 == 0) goto L_0x05ed
            java.lang.String r0 = r0.buildMethod()
            goto L_0x05ef
        L_0x05ed:
            r0 = r18
        L_0x05ef:
            if (r0 == 0) goto L_0x05f7
            int r1 = r0.length()
            if (r1 != 0) goto L_0x05f9
        L_0x05f7:
            java.lang.String r0 = "build"
        L_0x05f9:
            r12 = 0
            java.lang.Class[] r1 = new java.lang.Class[r12]     // Catch:{ NoSuchMethodException | SecurityException -> 0x0601 }
            java.lang.reflect.Method r20 = r13.getMethod(r0, r1)     // Catch:{ NoSuchMethodException | SecurityException -> 0x0601 }
            goto L_0x0602
        L_0x0601:
        L_0x0602:
            if (r20 != 0) goto L_0x060e
            java.lang.String r0 = "create"
            java.lang.Class[] r1 = new java.lang.Class[r12]     // Catch:{ NoSuchMethodException | SecurityException -> 0x060d }
            java.lang.reflect.Method r20 = r13.getMethod(r0, r1)     // Catch:{ NoSuchMethodException | SecurityException -> 0x060d }
            goto L_0x060e
        L_0x060d:
        L_0x060e:
            if (r20 == 0) goto L_0x0614
            com.alibaba.fastjson.util.TypeUtils.setAccessible(r20)
            goto L_0x0627
        L_0x0614:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException
            java.lang.String r1 = "buildMethod not found."
            r0.<init>(r1)
            throw r0
        L_0x061c:
            r39 = r9
            r36 = r10
            r13 = r11
            r29 = r15
            r38 = r23
            r15 = r12
        L_0x0626:
            r12 = 0
        L_0x0627:
            int r11 = r15.length
            r10 = 0
        L_0x0629:
            java.lang.String r9 = "get"
            r8 = 4
            if (r10 >= r11) goto L_0x095e
            r2 = r15[r10]
            r6 = 0
            r7 = 0
            r23 = 0
            java.lang.String r0 = r2.getName()
            int r1 = r2.getModifiers()
            boolean r1 = java.lang.reflect.Modifier.isStatic(r1)
            if (r1 == 0) goto L_0x0658
        L_0x0642:
            r30 = r10
            r25 = r11
            r37 = r13
            r32 = r15
            r42 = r36
            r13 = r38
            r33 = r39
            r27 = 1
            r28 = 2
        L_0x0654:
            r31 = 0
            goto L_0x094d
        L_0x0658:
            java.lang.Class r1 = r2.getReturnType()
            java.lang.Class r3 = java.lang.Void.TYPE
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x066f
            java.lang.Class r3 = r2.getDeclaringClass()
            boolean r1 = r1.equals(r3)
            if (r1 != 0) goto L_0x066f
            goto L_0x0642
        L_0x066f:
            java.lang.Class r1 = r2.getDeclaringClass()
            java.lang.Class<java.lang.Object> r3 = java.lang.Object.class
            if (r1 != r3) goto L_0x0678
            goto L_0x0642
        L_0x0678:
            java.lang.Class[] r1 = r2.getParameterTypes()
            int r3 = r1.length
            if (r3 == 0) goto L_0x0642
            int r3 = r1.length
            r5 = 2
            if (r3 <= r5) goto L_0x0684
            goto L_0x0642
        L_0x0684:
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r3 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r3 = com.alibaba.fastjson.util.TypeUtils.getAnnotation((java.lang.reflect.Method) r2, r3)
            r24 = r3
            com.alibaba.fastjson.annotation.JSONField r24 = (com.alibaba.fastjson.annotation.JSONField) r24
            if (r24 == 0) goto L_0x06c9
            int r3 = r1.length
            if (r3 != r5) goto L_0x06c9
            r3 = r1[r12]
            java.lang.Class<java.lang.String> r4 = java.lang.String.class
            if (r3 != r4) goto L_0x06c9
            r3 = 1
            r4 = r1[r3]
            java.lang.Class<java.lang.Object> r3 = java.lang.Object.class
            if (r4 != r3) goto L_0x06c9
            com.alibaba.fastjson.util.FieldInfo r9 = new com.alibaba.fastjson.util.FieldInfo
            r3 = 0
            r25 = 0
            r27 = 0
            java.lang.String r1 = ""
            r0 = r9
            r4 = r45
            r28 = 2
            r5 = r46
            r8 = r23
            r40 = r9
            r9 = r24
            r30 = r10
            r10 = r25
            r25 = r11
            r11 = r27
            r12 = r16
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12)
            r0 = r40
            add(r14, r0)
            goto L_0x06d3
        L_0x06c9:
            r30 = r10
            r25 = r11
            r28 = 2
            int r3 = r1.length
            r4 = 1
            if (r3 == r4) goto L_0x06e1
        L_0x06d3:
            r37 = r13
            r32 = r15
            r42 = r36
            r13 = r38
            r33 = r39
        L_0x06dd:
            r27 = 1
            goto L_0x0654
        L_0x06e1:
            r11 = 1
            r12 = r45
            if (r24 != 0) goto L_0x06ec
            com.alibaba.fastjson.annotation.JSONField r3 = com.alibaba.fastjson.util.TypeUtils.getSuperMethodAnnotation(r12, r2)
            r24 = r3
        L_0x06ec:
            if (r24 != 0) goto L_0x06f5
            int r3 = r0.length()
            if (r3 >= r8) goto L_0x06f5
            goto L_0x06d3
        L_0x06f5:
            if (r24 == 0) goto L_0x0743
            boolean r3 = r24.deserialize()
            if (r3 != 0) goto L_0x06fe
            goto L_0x06d3
        L_0x06fe:
            int r6 = r24.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r3 = r24.serialzeFeatures()
            int r7 = com.alibaba.fastjson.serializer.SerializerFeature.of(r3)
            com.alibaba.fastjson.parser.Feature[] r3 = r24.parseFeatures()
            int r10 = com.alibaba.fastjson.parser.Feature.of(r3)
            java.lang.String r3 = r24.name()
            int r3 = r3.length()
            if (r3 == 0) goto L_0x0741
            java.lang.String r1 = r24.name()
            com.alibaba.fastjson.util.FieldInfo r9 = new com.alibaba.fastjson.util.FieldInfo
            r3 = 0
            r23 = 0
            r26 = 0
            r0 = r9
            r4 = r45
            r5 = r46
            r8 = r10
            r10 = r9
            r9 = r24
            r41 = r10
            r10 = r23
            r11 = r26
            r12 = r16
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12)
            r0 = r41
            add(r14, r0)
            goto L_0x06d3
        L_0x0741:
            r23 = r10
        L_0x0743:
            r12 = r39
            if (r24 != 0) goto L_0x0759
            boolean r3 = r0.startsWith(r12)
            if (r3 == 0) goto L_0x074e
            goto L_0x0759
        L_0x074e:
            r33 = r12
            r37 = r13
            r32 = r15
            r42 = r36
        L_0x0756:
            r13 = r38
            goto L_0x06dd
        L_0x0759:
            if (r13 == 0) goto L_0x075c
            goto L_0x074e
        L_0x075c:
            r11 = 3
            char r3 = r0.charAt(r11)
            if (r17 == 0) goto L_0x0784
            java.util.ArrayList r4 = new java.util.ArrayList
            r4.<init>()
            r5 = 0
        L_0x0769:
            int r10 = r15.length
            if (r5 >= r10) goto L_0x0786
            r10 = r15[r5]
            java.lang.String r10 = r10.getName()
            boolean r10 = r10.startsWith(r9)
            if (r10 == 0) goto L_0x0781
            r10 = r15[r5]
            java.lang.String r10 = r10.getName()
            r4.add(r10)
        L_0x0781:
            int r5 = r5 + 1
            goto L_0x0769
        L_0x0784:
            r4 = r18
        L_0x0786:
            boolean r5 = java.lang.Character.isUpperCase(r3)
            java.lang.String r9 = "is"
            java.lang.String r10 = "g"
            if (r5 != 0) goto L_0x0831
            r5 = 512(0x200, float:7.175E-43)
            if (r3 <= r5) goto L_0x0796
            goto L_0x0831
        L_0x0796:
            r5 = 95
            if (r3 != r5) goto L_0x07f5
            if (r17 == 0) goto L_0x07d7
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r10)
            r5 = 1
            java.lang.String r8 = r0.substring(r5)
            r3.append(r8)
            java.lang.String r3 = r3.toString()
            boolean r3 = r4.contains(r3)
            if (r3 == 0) goto L_0x07bb
            java.lang.String r0 = r0.substring(r11)
            goto L_0x07ce
        L_0x07bb:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r9)
            java.lang.String r0 = r0.substring(r11)
            r3.append(r0)
            java.lang.String r0 = r3.toString()
        L_0x07ce:
            r4 = r45
            r10 = r36
            java.lang.reflect.Field r3 = com.alibaba.fastjson.util.TypeUtils.getField(r4, r0, r10)
            goto L_0x07f2
        L_0x07d7:
            r5 = 1
            r4 = r45
            r10 = r36
            java.lang.String r3 = r0.substring(r8)
            java.lang.reflect.Field r8 = com.alibaba.fastjson.util.TypeUtils.getField(r4, r3, r10)
            if (r8 != 0) goto L_0x07f0
            java.lang.String r0 = r0.substring(r11)
            java.lang.reflect.Field r8 = com.alibaba.fastjson.util.TypeUtils.getField(r4, r0, r10)
            if (r8 != 0) goto L_0x07f1
        L_0x07f0:
            r0 = r3
        L_0x07f1:
            r3 = r8
        L_0x07f2:
            r8 = r10
            goto L_0x0864
        L_0x07f5:
            r4 = r45
            r10 = r36
            r5 = 102(0x66, float:1.43E-43)
            if (r3 != r5) goto L_0x0803
            java.lang.String r0 = r0.substring(r11)
        L_0x0801:
            r8 = r10
            goto L_0x084f
        L_0x0803:
            int r3 = r0.length()
            r5 = 5
            if (r3 < r5) goto L_0x081d
            char r3 = r0.charAt(r8)
            boolean r3 = java.lang.Character.isUpperCase(r3)
            if (r3 == 0) goto L_0x081d
            java.lang.String r0 = r0.substring(r11)
            java.lang.String r0 = com.alibaba.fastjson.util.TypeUtils.decapitalize(r0)
            goto L_0x0801
        L_0x081d:
            java.lang.String r0 = r0.substring(r11)
            java.lang.reflect.Field r3 = com.alibaba.fastjson.util.TypeUtils.getField(r4, r0, r10)
            if (r3 != 0) goto L_0x07f2
            r42 = r10
        L_0x0829:
            r33 = r12
            r37 = r13
            r32 = r15
            goto L_0x0756
        L_0x0831:
            r4 = r45
            r8 = r36
            if (r17 == 0) goto L_0x0852
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r10)
            r5 = 1
            java.lang.String r0 = r0.substring(r5)
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            java.lang.String r0 = com.alibaba.fastjson.util.TypeUtils.getPropertyNameByMethodName(r0)
        L_0x084f:
            r3 = r18
            goto L_0x0864
        L_0x0852:
            boolean r3 = com.alibaba.fastjson.util.TypeUtils.compatibleWithJavaBean
            if (r3 == 0) goto L_0x085f
            java.lang.String r0 = r0.substring(r11)
            java.lang.String r0 = com.alibaba.fastjson.util.TypeUtils.decapitalize(r0)
            goto L_0x084f
        L_0x085f:
            java.lang.String r0 = com.alibaba.fastjson.util.TypeUtils.getPropertyNameByMethodName(r0)
            goto L_0x084f
        L_0x0864:
            if (r3 != 0) goto L_0x086a
            java.lang.reflect.Field r3 = com.alibaba.fastjson.util.TypeUtils.getField(r4, r0, r8)
        L_0x086a:
            if (r3 != 0) goto L_0x089a
            r10 = 0
            r1 = r1[r10]
            java.lang.Class r5 = java.lang.Boolean.TYPE
            if (r1 != r5) goto L_0x0898
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r9)
            char r3 = r0.charAt(r10)
            char r3 = java.lang.Character.toUpperCase(r3)
            r1.append(r3)
            r5 = 1
            java.lang.String r3 = r0.substring(r5)
            r1.append(r3)
            java.lang.String r1 = r1.toString()
            java.lang.reflect.Field r1 = com.alibaba.fastjson.util.TypeUtils.getField(r4, r1, r8)
            r3 = r1
            goto L_0x089c
        L_0x0898:
            r5 = 1
            goto L_0x089c
        L_0x089a:
            r5 = 1
            r10 = 0
        L_0x089c:
            if (r3 == 0) goto L_0x091c
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r1 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r1 = com.alibaba.fastjson.util.TypeUtils.getAnnotation((java.lang.reflect.Field) r3, r1)
            r26 = r1
            com.alibaba.fastjson.annotation.JSONField r26 = (com.alibaba.fastjson.annotation.JSONField) r26
            if (r26 == 0) goto L_0x090c
            boolean r1 = r26.deserialize()
            if (r1 != 0) goto L_0x08b4
            r42 = r8
            goto L_0x0829
        L_0x08b4:
            int r6 = r26.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r1 = r26.serialzeFeatures()
            int r7 = com.alibaba.fastjson.serializer.SerializerFeature.of(r1)
            com.alibaba.fastjson.parser.Feature[] r1 = r26.parseFeatures()
            int r9 = com.alibaba.fastjson.parser.Feature.of(r1)
            java.lang.String r1 = r26.name()
            int r1 = r1.length()
            if (r1 == 0) goto L_0x08ff
            java.lang.String r1 = r26.name()
            com.alibaba.fastjson.util.FieldInfo r0 = new com.alibaba.fastjson.util.FieldInfo
            r23 = 0
            r47 = r0
            r4 = r45
            r27 = 1
            r5 = r46
            r42 = r8
            r8 = r9
            r9 = r24
            r31 = 0
            r10 = r26
            r32 = r15
            r15 = 3
            r11 = r23
            r33 = r12
            r12 = r16
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12)
            add(r14, r0)
            r37 = r13
            r13 = r38
            goto L_0x094d
        L_0x08ff:
            r42 = r8
            r33 = r12
            r32 = r15
            r15 = 3
            r27 = 1
            r31 = 0
            r8 = r9
            goto L_0x0919
        L_0x090c:
            r42 = r8
            r33 = r12
            r32 = r15
            r15 = 3
            r27 = 1
            r31 = 0
            r8 = r23
        L_0x0919:
            r10 = r26
            goto L_0x092b
        L_0x091c:
            r42 = r8
            r33 = r12
            r32 = r15
            r15 = 3
            r27 = 1
            r31 = 0
            r10 = r18
            r8 = r23
        L_0x092b:
            r12 = r38
            if (r12 == 0) goto L_0x0933
            java.lang.String r0 = r12.translate(r0)
        L_0x0933:
            r1 = r0
            com.alibaba.fastjson.util.FieldInfo r11 = new com.alibaba.fastjson.util.FieldInfo
            r23 = 0
            r0 = r11
            r4 = r45
            r5 = r46
            r9 = r24
            r15 = r11
            r11 = r23
            r37 = r13
            r13 = r12
            r12 = r16
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12)
            add(r14, r15)
        L_0x094d:
            int r10 = r30 + 1
            r38 = r13
            r11 = r25
            r15 = r32
            r39 = r33
            r13 = r37
            r36 = r42
            r12 = 0
            goto L_0x0629
        L_0x095e:
            r37 = r13
            r42 = r36
            r13 = r38
            r27 = 1
            r31 = 0
            java.lang.reflect.Field[] r0 = r45.getFields()
            r15 = r45
            r12 = r46
            computeFields(r15, r12, r13, r14, r0)
            java.lang.reflect.Method[] r11 = r45.getMethods()
            int r10 = r11.length
            r7 = 0
        L_0x0979:
            if (r7 >= r10) goto L_0x0ab6
            r2 = r11[r7]
            java.lang.String r0 = r2.getName()
            int r1 = r0.length()
            if (r1 >= r8) goto L_0x099a
        L_0x0987:
            r31 = r7
            r24 = r9
            r17 = r10
            r25 = r11
            r38 = r13
            r44 = r42
        L_0x0993:
            r23 = 4
            r30 = 3
            r13 = r12
            goto L_0x0aa6
        L_0x099a:
            int r1 = r2.getModifiers()
            boolean r1 = java.lang.reflect.Modifier.isStatic(r1)
            if (r1 == 0) goto L_0x09a5
            goto L_0x0987
        L_0x09a5:
            if (r37 != 0) goto L_0x0987
            boolean r1 = r0.startsWith(r9)
            if (r1 == 0) goto L_0x0987
            r6 = 3
            char r1 = r0.charAt(r6)
            boolean r1 = java.lang.Character.isUpperCase(r1)
            if (r1 == 0) goto L_0x0987
            java.lang.Class[] r1 = r2.getParameterTypes()
            int r1 = r1.length
            if (r1 == 0) goto L_0x09c0
            goto L_0x0987
        L_0x09c0:
            java.lang.Class<java.util.Collection> r1 = java.util.Collection.class
            java.lang.Class r3 = r2.getReturnType()
            boolean r1 = r1.isAssignableFrom(r3)
            if (r1 != 0) goto L_0x09f0
            java.lang.Class<java.util.Map> r1 = java.util.Map.class
            java.lang.Class r3 = r2.getReturnType()
            boolean r1 = r1.isAssignableFrom(r3)
            if (r1 != 0) goto L_0x09f0
            java.lang.Class<java.util.concurrent.atomic.AtomicBoolean> r1 = java.util.concurrent.atomic.AtomicBoolean.class
            java.lang.Class r3 = r2.getReturnType()
            if (r1 == r3) goto L_0x09f0
            java.lang.Class<java.util.concurrent.atomic.AtomicInteger> r1 = java.util.concurrent.atomic.AtomicInteger.class
            java.lang.Class r3 = r2.getReturnType()
            if (r1 == r3) goto L_0x09f0
            java.lang.Class<java.util.concurrent.atomic.AtomicLong> r1 = java.util.concurrent.atomic.AtomicLong.class
            java.lang.Class r3 = r2.getReturnType()
            if (r1 != r3) goto L_0x0987
        L_0x09f0:
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r1 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r1 = com.alibaba.fastjson.util.TypeUtils.getAnnotation((java.lang.reflect.Method) r2, r1)
            r17 = r1
            com.alibaba.fastjson.annotation.JSONField r17 = (com.alibaba.fastjson.annotation.JSONField) r17
            if (r17 == 0) goto L_0x0a03
            boolean r1 = r17.deserialize()
            if (r1 == 0) goto L_0x0a03
            goto L_0x0987
        L_0x0a03:
            if (r17 == 0) goto L_0x0a18
            java.lang.String r1 = r17.name()
            int r1 = r1.length()
            if (r1 <= 0) goto L_0x0a18
            java.lang.String r0 = r17.name()
            r3 = r18
            r5 = r42
            goto L_0x0a51
        L_0x0a18:
            java.lang.String r0 = com.alibaba.fastjson.util.TypeUtils.getPropertyNameByMethodName(r0)
            r5 = r42
            java.lang.reflect.Field r1 = com.alibaba.fastjson.util.TypeUtils.getField(r15, r0, r5)
            if (r1 == 0) goto L_0x0a4f
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r3 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r3 = com.alibaba.fastjson.util.TypeUtils.getAnnotation((java.lang.reflect.Field) r1, r3)
            com.alibaba.fastjson.annotation.JSONField r3 = (com.alibaba.fastjson.annotation.JSONField) r3
            if (r3 == 0) goto L_0x0a35
            boolean r3 = r3.deserialize()
            if (r3 != 0) goto L_0x0a35
            goto L_0x0a5e
        L_0x0a35:
            java.lang.Class<java.util.Collection> r3 = java.util.Collection.class
            java.lang.Class r4 = r2.getReturnType()
            boolean r3 = r3.isAssignableFrom(r4)
            if (r3 != 0) goto L_0x0a4d
            java.lang.Class<java.util.Map> r3 = java.util.Map.class
            java.lang.Class r4 = r2.getReturnType()
            boolean r3 = r3.isAssignableFrom(r4)
            if (r3 == 0) goto L_0x0a4f
        L_0x0a4d:
            r3 = r1
            goto L_0x0a51
        L_0x0a4f:
            r3 = r18
        L_0x0a51:
            if (r13 == 0) goto L_0x0a57
            java.lang.String r0 = r13.translate(r0)
        L_0x0a57:
            r1 = r0
            com.alibaba.fastjson.util.FieldInfo r0 = getField(r14, r1)
            if (r0 == 0) goto L_0x0a6c
        L_0x0a5e:
            r44 = r5
            r31 = r7
            r24 = r9
            r17 = r10
            r25 = r11
            r38 = r13
            goto L_0x0993
        L_0x0a6c:
            com.alibaba.fastjson.util.FieldInfo r4 = new com.alibaba.fastjson.util.FieldInfo
            r23 = 0
            r24 = 0
            r25 = 0
            r26 = 0
            r28 = 0
            r0 = r4
            r43 = r4
            r4 = r45
            r44 = r5
            r5 = r46
            r30 = 3
            r6 = r23
            r31 = r7
            r7 = r24
            r23 = 4
            r8 = r25
            r24 = r9
            r9 = r17
            r17 = r10
            r10 = r26
            r25 = r11
            r11 = r28
            r38 = r13
            r13 = r12
            r12 = r16
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12)
            r0 = r43
            add(r14, r0)
        L_0x0aa6:
            int r7 = r31 + 1
            r12 = r13
            r10 = r17
            r9 = r24
            r11 = r25
            r13 = r38
            r42 = r44
            r8 = 4
            goto L_0x0979
        L_0x0ab6:
            r38 = r13
            r44 = r42
            r13 = r12
            int r0 = r14.size()
            if (r0 != 0) goto L_0x0adb
            boolean r0 = com.alibaba.fastjson.util.TypeUtils.isXmlField(r45)
            if (r0 == 0) goto L_0x0ac8
            goto L_0x0aca
        L_0x0ac8:
            r27 = r48
        L_0x0aca:
            if (r27 == 0) goto L_0x0adb
            r12 = r15
        L_0x0acd:
            if (r12 == 0) goto L_0x0adb
            r1 = r38
            r0 = r44
            computeFields(r15, r13, r1, r14, r0)
            java.lang.Class r12 = r12.getSuperclass()
            goto L_0x0acd
        L_0x0adb:
            com.alibaba.fastjson.util.JavaBeanInfo r9 = new com.alibaba.fastjson.util.JavaBeanInfo
            r0 = r9
            r1 = r45
            r2 = r37
            r3 = r19
            r4 = r22
            r5 = r21
            r6 = r20
            r7 = r29
            r8 = r14
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.JavaBeanInfo.build(java.lang.Class, java.lang.reflect.Type, com.alibaba.fastjson.PropertyNamingStrategy, boolean, boolean, boolean):com.alibaba.fastjson.util.JavaBeanInfo");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0055, code lost:
        if ((java.util.Map.class.isAssignableFrom(r2) || java.util.Collection.class.isAssignableFrom(r2) || java.util.concurrent.atomic.AtomicLong.class.equals(r2) || java.util.concurrent.atomic.AtomicInteger.class.equals(r2) || java.util.concurrent.atomic.AtomicBoolean.class.equals(r2)) == false) goto L_0x0018;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void computeFields(java.lang.Class<?> r20, java.lang.reflect.Type r21, com.alibaba.fastjson.PropertyNamingStrategy r22, java.util.List<com.alibaba.fastjson.util.FieldInfo> r23, java.lang.reflect.Field[] r24) {
        /*
            r0 = r22
            r1 = r24
            java.util.Map r15 = buildGenericInfo(r20)
            int r14 = r1.length
            r16 = 0
            r13 = 0
        L_0x000c:
            if (r13 >= r14) goto L_0x00e6
            r5 = r1[r13]
            int r2 = r5.getModifiers()
            r3 = r2 & 8
            if (r3 == 0) goto L_0x0020
        L_0x0018:
            r2 = r23
            r17 = r13
            r18 = r14
            goto L_0x00e0
        L_0x0020:
            r2 = r2 & 16
            r3 = 1
            if (r2 == 0) goto L_0x0058
            java.lang.Class r2 = r5.getType()
            java.lang.Class<java.util.Map> r4 = java.util.Map.class
            boolean r4 = r4.isAssignableFrom(r2)
            if (r4 != 0) goto L_0x0054
            java.lang.Class<java.util.Collection> r4 = java.util.Collection.class
            boolean r4 = r4.isAssignableFrom(r2)
            if (r4 != 0) goto L_0x0054
            java.lang.Class<java.util.concurrent.atomic.AtomicLong> r4 = java.util.concurrent.atomic.AtomicLong.class
            boolean r4 = r4.equals(r2)
            if (r4 != 0) goto L_0x0054
            java.lang.Class<java.util.concurrent.atomic.AtomicInteger> r4 = java.util.concurrent.atomic.AtomicInteger.class
            boolean r4 = r4.equals(r2)
            if (r4 != 0) goto L_0x0054
            java.lang.Class<java.util.concurrent.atomic.AtomicBoolean> r4 = java.util.concurrent.atomic.AtomicBoolean.class
            boolean r2 = r4.equals(r2)
            if (r2 == 0) goto L_0x0052
            goto L_0x0054
        L_0x0052:
            r2 = 0
            goto L_0x0055
        L_0x0054:
            r2 = 1
        L_0x0055:
            if (r2 != 0) goto L_0x0058
            goto L_0x0018
        L_0x0058:
            java.util.Iterator r2 = r23.iterator()
        L_0x005c:
            boolean r4 = r2.hasNext()
            if (r4 == 0) goto L_0x0075
            java.lang.Object r4 = r2.next()
            com.alibaba.fastjson.util.FieldInfo r4 = (com.alibaba.fastjson.util.FieldInfo) r4
            java.lang.String r4 = r4.name
            java.lang.String r6 = r5.getName()
            boolean r4 = r4.equals(r6)
            if (r4 == 0) goto L_0x005c
            goto L_0x0076
        L_0x0075:
            r3 = 0
        L_0x0076:
            if (r3 == 0) goto L_0x0079
            goto L_0x0018
        L_0x0079:
            java.lang.String r2 = r5.getName()
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r3 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r3 = com.alibaba.fastjson.util.TypeUtils.getAnnotation((java.lang.reflect.Field) r5, r3)
            r12 = r3
            com.alibaba.fastjson.annotation.JSONField r12 = (com.alibaba.fastjson.annotation.JSONField) r12
            if (r12 == 0) goto L_0x00b5
            boolean r3 = r12.deserialize()
            if (r3 != 0) goto L_0x008f
            goto L_0x0018
        L_0x008f:
            int r3 = r12.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r4 = r12.serialzeFeatures()
            int r4 = com.alibaba.fastjson.serializer.SerializerFeature.of(r4)
            com.alibaba.fastjson.parser.Feature[] r6 = r12.parseFeatures()
            int r6 = com.alibaba.fastjson.parser.Feature.of(r6)
            java.lang.String r7 = r12.name()
            int r7 = r7.length()
            if (r7 == 0) goto L_0x00b1
            java.lang.String r2 = r12.name()
        L_0x00b1:
            r8 = r3
            r9 = r4
            r10 = r6
            goto L_0x00b8
        L_0x00b5:
            r8 = 0
            r9 = 0
            r10 = 0
        L_0x00b8:
            if (r0 == 0) goto L_0x00be
            java.lang.String r2 = r0.translate(r2)
        L_0x00be:
            r3 = r2
            com.alibaba.fastjson.util.FieldInfo r11 = new com.alibaba.fastjson.util.FieldInfo
            r4 = 0
            r17 = 0
            r18 = 0
            r2 = r11
            r6 = r20
            r7 = r21
            r19 = r11
            r11 = r17
            r17 = r13
            r13 = r18
            r18 = r14
            r14 = r15
            r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14)
            r2 = r23
            r3 = r19
            add(r2, r3)
        L_0x00e0:
            int r13 = r17 + 1
            r14 = r18
            goto L_0x000c
        L_0x00e6:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.JavaBeanInfo.computeFields(java.lang.Class, java.lang.reflect.Type, com.alibaba.fastjson.PropertyNamingStrategy, java.util.List, java.lang.reflect.Field[]):void");
    }

    static Constructor<?> getDefaultConstructor(Class<?> cls, Constructor<?>[] constructorArr) {
        Constructor<?> constructor = null;
        if (Modifier.isAbstract(cls.getModifiers())) {
            return null;
        }
        int length = constructorArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            Constructor<?> constructor2 = constructorArr[i];
            if (constructor2.getParameterTypes().length == 0) {
                constructor = constructor2;
                break;
            }
            i++;
        }
        if (constructor != null || !cls.isMemberClass() || Modifier.isStatic(cls.getModifiers())) {
            return constructor;
        }
        for (Constructor<?> constructor3 : constructorArr) {
            Class[] parameterTypes = constructor3.getParameterTypes();
            if (parameterTypes.length == 1 && parameterTypes[0].equals(cls.getDeclaringClass())) {
                return constructor3;
            }
        }
        return constructor;
    }

    public static Constructor<?> getCreatorConstructor(Constructor[] constructorArr) {
        boolean z;
        Constructor<?> constructor = null;
        for (Constructor<?> constructor2 : constructorArr) {
            if (((JSONCreator) constructor2.getAnnotation(JSONCreator.class)) != null) {
                if (constructor == null) {
                    constructor = constructor2;
                } else {
                    throw new JSONException("multi-JSONCreator");
                }
            }
        }
        if (constructor != null) {
            return constructor;
        }
        for (Constructor<?> constructor3 : constructorArr) {
            Annotation[][] parameterAnnotations = TypeUtils.getParameterAnnotations((Constructor) constructor3);
            if (parameterAnnotations.length != 0) {
                int length = parameterAnnotations.length;
                int i = 0;
                while (true) {
                    z = true;
                    if (i >= length) {
                        break;
                    }
                    Annotation[] annotationArr = parameterAnnotations[i];
                    int length2 = annotationArr.length;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length2) {
                            z = false;
                            break;
                        } else if (annotationArr[i2] instanceof JSONField) {
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (!z) {
                        z = false;
                        break;
                    }
                    i++;
                }
                if (!z) {
                    continue;
                } else if (constructor == null) {
                    constructor = constructor3;
                } else {
                    throw new JSONException("multi-JSONCreator");
                }
            }
        }
        return constructor;
    }

    private static Method getFactoryMethod(Class<?> cls, Method[] methodArr, boolean z) {
        Method method = null;
        for (Method method2 : methodArr) {
            if (Modifier.isStatic(method2.getModifiers()) && cls.isAssignableFrom(method2.getReturnType()) && ((JSONCreator) TypeUtils.getAnnotation(method2, JSONCreator.class)) != null) {
                if (method == null) {
                    method = method2;
                } else {
                    throw new JSONException("multi-JSONCreator");
                }
            }
        }
        if (method != null || !z) {
            return method;
        }
        for (Method method3 : methodArr) {
            if (TypeUtils.isJacksonCreator(method3)) {
                return method3;
            }
        }
        return method;
    }

    public static Class<?> getBuilderClass(JSONType jSONType) {
        return getBuilderClass((Class<?>) null, jSONType);
    }

    public static Class<?> getBuilderClass(Class<?> cls, JSONType jSONType) {
        Class<?> builder;
        if (cls != null && cls.getName().equals("org.springframework.security.web.savedrequest.DefaultSavedRequest")) {
            return TypeUtils.loadClass("org.springframework.security.web.savedrequest.DefaultSavedRequest$Builder");
        }
        if (jSONType == null || (builder = jSONType.builder()) == Void.class) {
            return null;
        }
        return builder;
    }
}
