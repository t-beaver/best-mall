package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.spi.Module;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.IdentityHashMap;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.File;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public class SerializeConfig {
    private static boolean awtError = false;
    public static final SerializeConfig globalInstance = new SerializeConfig();
    private static boolean guavaError = false;
    private static boolean jdk8Error = false;
    private static boolean jodaError = false;
    private static boolean oracleJdbcError = false;
    private static boolean springfoxError = false;
    private boolean asm;
    private ASMSerializerFactory asmFactory;
    private long[] denyClasses;
    private final boolean fieldBased;
    private final IdentityHashMap<Type, IdentityHashMap<Type, ObjectSerializer>> mixInSerializers;
    private List<Module> modules;
    public PropertyNamingStrategy propertyNamingStrategy;
    private final IdentityHashMap<Type, ObjectSerializer> serializers;
    protected String typeKey;

    public String getTypeKey() {
        return this.typeKey;
    }

    public void setTypeKey(String str) {
        this.typeKey = str;
    }

    private final JavaBeanSerializer createASMSerializer(SerializeBeanInfo serializeBeanInfo) throws Exception {
        JavaBeanSerializer createJavaBeanSerializer = this.asmFactory.createJavaBeanSerializer(serializeBeanInfo);
        for (FieldSerializer fieldSerializer : createJavaBeanSerializer.sortedGetters) {
            Class<?> cls = fieldSerializer.fieldInfo.fieldClass;
            if (cls.isEnum() && !(getObjectWriter(cls) instanceof EnumSerializer)) {
                createJavaBeanSerializer.writeDirect = false;
            }
        }
        return createJavaBeanSerializer;
    }

    public final ObjectSerializer createJavaBeanSerializer(Class<?> cls) {
        String name = cls.getName();
        if (Arrays.binarySearch(this.denyClasses, TypeUtils.fnv1a_64(name)) < 0) {
            SerializeBeanInfo buildBeanInfo = TypeUtils.buildBeanInfo(cls, (Map<String, String>) null, this.propertyNamingStrategy, this.fieldBased);
            if (buildBeanInfo.fields.length != 0 || !Iterable.class.isAssignableFrom(cls)) {
                return createJavaBeanSerializer(buildBeanInfo);
            }
            return MiscCodec.instance;
        }
        throw new JSONException("not support class : " + name);
    }

    /* JADX WARNING: Removed duplicated region for block: B:111:0x0162 A[SYNTHETIC, Splitter:B:111:0x0162] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.alibaba.fastjson.serializer.ObjectSerializer createJavaBeanSerializer(com.alibaba.fastjson.serializer.SerializeBeanInfo r14) {
        /*
            r13 = this;
            com.alibaba.fastjson.annotation.JSONType r0 = r14.jsonType
            boolean r1 = r13.asm
            r2 = 0
            if (r1 == 0) goto L_0x000d
            boolean r1 = r13.fieldBased
            if (r1 != 0) goto L_0x000d
            r1 = 1
            goto L_0x000e
        L_0x000d:
            r1 = 0
        L_0x000e:
            if (r0 == 0) goto L_0x0056
            java.lang.Class r3 = r0.serializer()
            java.lang.Class<java.lang.Void> r4 = java.lang.Void.class
            if (r3 == r4) goto L_0x0024
            java.lang.Object r3 = r3.newInstance()     // Catch:{ all -> 0x0023 }
            boolean r4 = r3 instanceof com.alibaba.fastjson.serializer.ObjectSerializer     // Catch:{ all -> 0x0023 }
            if (r4 == 0) goto L_0x0024
            com.alibaba.fastjson.serializer.ObjectSerializer r3 = (com.alibaba.fastjson.serializer.ObjectSerializer) r3     // Catch:{ all -> 0x0023 }
            return r3
        L_0x0023:
        L_0x0024:
            boolean r3 = r0.asm()
            if (r3 != 0) goto L_0x002b
            r1 = 0
        L_0x002b:
            if (r1 == 0) goto L_0x004c
            com.alibaba.fastjson.serializer.SerializerFeature[] r3 = r0.serialzeFeatures()
            int r4 = r3.length
            r5 = 0
        L_0x0033:
            if (r5 >= r4) goto L_0x004c
            r6 = r3[r5]
            com.alibaba.fastjson.serializer.SerializerFeature r7 = com.alibaba.fastjson.serializer.SerializerFeature.WriteNonStringValueAsString
            if (r7 == r6) goto L_0x004b
            com.alibaba.fastjson.serializer.SerializerFeature r7 = com.alibaba.fastjson.serializer.SerializerFeature.WriteEnumUsingToString
            if (r7 == r6) goto L_0x004b
            com.alibaba.fastjson.serializer.SerializerFeature r7 = com.alibaba.fastjson.serializer.SerializerFeature.NotWriteDefaultValue
            if (r7 == r6) goto L_0x004b
            com.alibaba.fastjson.serializer.SerializerFeature r7 = com.alibaba.fastjson.serializer.SerializerFeature.BrowserCompatible
            if (r7 != r6) goto L_0x0048
            goto L_0x004b
        L_0x0048:
            int r5 = r5 + 1
            goto L_0x0033
        L_0x004b:
            r1 = 0
        L_0x004c:
            if (r1 == 0) goto L_0x0056
            java.lang.Class[] r0 = r0.serialzeFilters()
            int r0 = r0.length
            if (r0 == 0) goto L_0x0056
            r1 = 0
        L_0x0056:
            java.lang.Class<?> r0 = r14.beanType
            java.lang.Class<?> r3 = r14.beanType
            int r3 = r3.getModifiers()
            boolean r3 = java.lang.reflect.Modifier.isPublic(r3)
            if (r3 != 0) goto L_0x006a
            com.alibaba.fastjson.serializer.JavaBeanSerializer r0 = new com.alibaba.fastjson.serializer.JavaBeanSerializer
            r0.<init>((com.alibaba.fastjson.serializer.SerializeBeanInfo) r14)
            return r0
        L_0x006a:
            if (r1 == 0) goto L_0x0076
            com.alibaba.fastjson.serializer.ASMSerializerFactory r3 = r13.asmFactory
            com.alibaba.fastjson.util.ASMClassLoader r3 = r3.classLoader
            boolean r3 = r3.isExternalClass(r0)
            if (r3 != 0) goto L_0x007e
        L_0x0076:
            java.lang.Class<java.io.Serializable> r3 = java.io.Serializable.class
            if (r0 == r3) goto L_0x007e
            java.lang.Class<java.lang.Object> r3 = java.lang.Object.class
            if (r0 != r3) goto L_0x007f
        L_0x007e:
            r1 = 0
        L_0x007f:
            if (r1 == 0) goto L_0x008c
            java.lang.String r3 = r0.getSimpleName()
            boolean r3 = com.alibaba.fastjson.util.ASMUtils.checkName(r3)
            if (r3 != 0) goto L_0x008c
            r1 = 0
        L_0x008c:
            if (r1 == 0) goto L_0x0097
            java.lang.Class<?> r3 = r14.beanType
            boolean r3 = r3.isInterface()
            if (r3 == 0) goto L_0x0097
            r1 = 0
        L_0x0097:
            if (r1 == 0) goto L_0x015f
            com.alibaba.fastjson.util.FieldInfo[] r3 = r14.fields
            int r4 = r3.length
            r5 = 0
        L_0x009d:
            if (r5 >= r4) goto L_0x015f
            r6 = r3[r5]
            java.lang.reflect.Field r7 = r6.field
            if (r7 == 0) goto L_0x00b3
            java.lang.Class r7 = r7.getType()
            java.lang.Class<?> r8 = r6.fieldClass
            boolean r7 = r7.equals(r8)
            if (r7 != 0) goto L_0x00b3
            goto L_0x0160
        L_0x00b3:
            java.lang.reflect.Method r7 = r6.method
            if (r7 == 0) goto L_0x00c5
            java.lang.Class r8 = r7.getReturnType()
            java.lang.Class<?> r9 = r6.fieldClass
            boolean r8 = r8.equals(r9)
            if (r8 != 0) goto L_0x00c5
            goto L_0x0160
        L_0x00c5:
            java.lang.Class<?> r8 = r6.fieldClass
            boolean r8 = r8.isEnum()
            if (r8 == 0) goto L_0x00d9
            java.lang.Class<?> r8 = r6.fieldClass
            com.alibaba.fastjson.serializer.ObjectSerializer r8 = r13.get(r8)
            com.alibaba.fastjson.serializer.EnumSerializer r9 = com.alibaba.fastjson.serializer.EnumSerializer.instance
            if (r8 == r9) goto L_0x00d9
            goto L_0x0160
        L_0x00d9:
            com.alibaba.fastjson.annotation.JSONField r8 = r6.getAnnotation()
            if (r8 != 0) goto L_0x00e1
            goto L_0x015b
        L_0x00e1:
            java.lang.String r9 = r8.format()
            int r10 = r9.length()
            if (r10 == 0) goto L_0x00f9
            java.lang.Class<?> r6 = r6.fieldClass
            java.lang.Class<java.lang.String> r10 = java.lang.String.class
            if (r6 != r10) goto L_0x0160
            java.lang.String r6 = "trim"
            boolean r6 = r6.equals(r9)
            if (r6 == 0) goto L_0x0160
        L_0x00f9:
            java.lang.String r6 = r8.name()
            boolean r6 = com.alibaba.fastjson.util.ASMUtils.checkName(r6)
            if (r6 == 0) goto L_0x0160
            boolean r6 = r8.jsonDirect()
            if (r6 != 0) goto L_0x0160
            java.lang.Class r6 = r8.serializeUsing()
            java.lang.Class<java.lang.Void> r9 = java.lang.Void.class
            if (r6 != r9) goto L_0x0160
            boolean r6 = r8.unwrapped()
            if (r6 == 0) goto L_0x0118
            goto L_0x0160
        L_0x0118:
            com.alibaba.fastjson.serializer.SerializerFeature[] r6 = r8.serialzeFeatures()
            int r9 = r6.length
            r10 = 0
        L_0x011e:
            if (r10 >= r9) goto L_0x013b
            r11 = r6[r10]
            com.alibaba.fastjson.serializer.SerializerFeature r12 = com.alibaba.fastjson.serializer.SerializerFeature.WriteNonStringValueAsString
            if (r12 == r11) goto L_0x013a
            com.alibaba.fastjson.serializer.SerializerFeature r12 = com.alibaba.fastjson.serializer.SerializerFeature.WriteEnumUsingToString
            if (r12 == r11) goto L_0x013a
            com.alibaba.fastjson.serializer.SerializerFeature r12 = com.alibaba.fastjson.serializer.SerializerFeature.NotWriteDefaultValue
            if (r12 == r11) goto L_0x013a
            com.alibaba.fastjson.serializer.SerializerFeature r12 = com.alibaba.fastjson.serializer.SerializerFeature.BrowserCompatible
            if (r12 == r11) goto L_0x013a
            com.alibaba.fastjson.serializer.SerializerFeature r12 = com.alibaba.fastjson.serializer.SerializerFeature.WriteClassName
            if (r12 != r11) goto L_0x0137
            goto L_0x013a
        L_0x0137:
            int r10 = r10 + 1
            goto L_0x011e
        L_0x013a:
            r1 = 0
        L_0x013b:
            boolean r6 = com.alibaba.fastjson.util.TypeUtils.isAnnotationPresentOneToMany(r7)
            if (r6 != 0) goto L_0x0160
            boolean r6 = com.alibaba.fastjson.util.TypeUtils.isAnnotationPresentManyToMany(r7)
            if (r6 == 0) goto L_0x0148
            goto L_0x0160
        L_0x0148:
            java.lang.String r6 = r8.defaultValue()
            if (r6 == 0) goto L_0x015b
            java.lang.String r6 = r8.defaultValue()
            java.lang.String r7 = ""
            boolean r6 = r7.equals(r6)
            if (r6 != 0) goto L_0x015b
            goto L_0x0160
        L_0x015b:
            int r5 = r5 + 1
            goto L_0x009d
        L_0x015f:
            r2 = r1
        L_0x0160:
            if (r2 == 0) goto L_0x0191
            com.alibaba.fastjson.serializer.JavaBeanSerializer r0 = r13.createASMSerializer(r14)     // Catch:{ ClassCastException | ClassFormatError | ClassNotFoundException -> 0x0191, OutOfMemoryError -> 0x0181, all -> 0x0169 }
            if (r0 == 0) goto L_0x0191
            return r0
        L_0x0169:
            r14 = move-exception
            com.alibaba.fastjson.JSONException r1 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "create asm serializer error, verson 1.2.83, class "
            r2.append(r3)
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            r1.<init>(r0, r14)
            throw r1
        L_0x0181:
            r0 = move-exception
            java.lang.String r1 = r0.getMessage()
            java.lang.String r2 = "Metaspace"
            int r1 = r1.indexOf(r2)
            r2 = -1
            if (r1 != r2) goto L_0x0190
            goto L_0x0191
        L_0x0190:
            throw r0
        L_0x0191:
            com.alibaba.fastjson.serializer.JavaBeanSerializer r0 = new com.alibaba.fastjson.serializer.JavaBeanSerializer
            r0.<init>((com.alibaba.fastjson.serializer.SerializeBeanInfo) r14)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.SerializeConfig.createJavaBeanSerializer(com.alibaba.fastjson.serializer.SerializeBeanInfo):com.alibaba.fastjson.serializer.ObjectSerializer");
    }

    public boolean isAsmEnable() {
        return this.asm;
    }

    public void setAsmEnable(boolean z) {
        if (!ASMUtils.IS_ANDROID) {
            this.asm = z;
        }
    }

    public static SerializeConfig getGlobalInstance() {
        return globalInstance;
    }

    public SerializeConfig() {
        this(8192);
    }

    public SerializeConfig(boolean z) {
        this(8192, z);
    }

    public SerializeConfig(int i) {
        this(i, false);
    }

    public SerializeConfig(int i, boolean z) {
        this.asm = !ASMUtils.IS_ANDROID;
        this.typeKey = JSON.DEFAULT_TYPE_KEY;
        this.denyClasses = new long[]{4165360493669296979L, 4446674157046724083L};
        this.modules = new ArrayList();
        this.fieldBased = z;
        this.serializers = new IdentityHashMap<>(i);
        this.mixInSerializers = new IdentityHashMap<>(16);
        try {
            if (this.asm) {
                this.asmFactory = new ASMSerializerFactory();
            }
        } catch (Throwable unused) {
            this.asm = false;
        }
        initSerializers();
    }

    private void initSerializers() {
        put((Type) Boolean.class, (ObjectSerializer) BooleanCodec.instance);
        put((Type) Character.class, (ObjectSerializer) CharacterCodec.instance);
        put((Type) Byte.class, (ObjectSerializer) IntegerCodec.instance);
        put((Type) Short.class, (ObjectSerializer) IntegerCodec.instance);
        put((Type) Integer.class, (ObjectSerializer) IntegerCodec.instance);
        put((Type) Long.class, (ObjectSerializer) LongCodec.instance);
        put((Type) Float.class, (ObjectSerializer) FloatCodec.instance);
        put((Type) Double.class, (ObjectSerializer) DoubleSerializer.instance);
        put((Type) BigDecimal.class, (ObjectSerializer) BigDecimalCodec.instance);
        put((Type) BigInteger.class, (ObjectSerializer) BigIntegerCodec.instance);
        put((Type) String.class, (ObjectSerializer) StringCodec.instance);
        put((Type) byte[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put((Type) short[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put((Type) int[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put((Type) long[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put((Type) float[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put((Type) double[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put((Type) boolean[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put((Type) char[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put((Type) Object[].class, (ObjectSerializer) ObjectArrayCodec.instance);
        put((Type) Class.class, (ObjectSerializer) MiscCodec.instance);
        put((Type) SimpleDateFormat.class, (ObjectSerializer) MiscCodec.instance);
        put((Type) Currency.class, (ObjectSerializer) new MiscCodec());
        put((Type) TimeZone.class, (ObjectSerializer) MiscCodec.instance);
        put((Type) InetAddress.class, (ObjectSerializer) MiscCodec.instance);
        put((Type) Inet4Address.class, (ObjectSerializer) MiscCodec.instance);
        put((Type) Inet6Address.class, (ObjectSerializer) MiscCodec.instance);
        put((Type) InetSocketAddress.class, (ObjectSerializer) MiscCodec.instance);
        put((Type) File.class, (ObjectSerializer) MiscCodec.instance);
        put((Type) Appendable.class, (ObjectSerializer) AppendableSerializer.instance);
        put((Type) StringBuffer.class, (ObjectSerializer) AppendableSerializer.instance);
        put((Type) StringBuilder.class, (ObjectSerializer) AppendableSerializer.instance);
        put((Type) Charset.class, (ObjectSerializer) ToStringSerializer.instance);
        put((Type) Pattern.class, (ObjectSerializer) ToStringSerializer.instance);
        put((Type) Locale.class, (ObjectSerializer) ToStringSerializer.instance);
        put((Type) URI.class, (ObjectSerializer) ToStringSerializer.instance);
        put((Type) URL.class, (ObjectSerializer) ToStringSerializer.instance);
        put((Type) UUID.class, (ObjectSerializer) ToStringSerializer.instance);
        put((Type) AtomicBoolean.class, (ObjectSerializer) AtomicCodec.instance);
        put((Type) AtomicInteger.class, (ObjectSerializer) AtomicCodec.instance);
        put((Type) AtomicLong.class, (ObjectSerializer) AtomicCodec.instance);
        put((Type) AtomicReference.class, (ObjectSerializer) ReferenceCodec.instance);
        put((Type) AtomicIntegerArray.class, (ObjectSerializer) AtomicCodec.instance);
        put((Type) AtomicLongArray.class, (ObjectSerializer) AtomicCodec.instance);
        put((Type) WeakReference.class, (ObjectSerializer) ReferenceCodec.instance);
        put((Type) SoftReference.class, (ObjectSerializer) ReferenceCodec.instance);
        put((Type) LinkedList.class, (ObjectSerializer) CollectionCodec.instance);
    }

    public void addFilter(Class<?> cls, SerializeFilter serializeFilter) {
        ObjectSerializer objectWriter = getObjectWriter(cls);
        if (objectWriter instanceof SerializeFilterable) {
            SerializeFilterable serializeFilterable = (SerializeFilterable) objectWriter;
            if (this == globalInstance || serializeFilterable != MapSerializer.instance) {
                serializeFilterable.addFilter(serializeFilter);
                return;
            }
            MapSerializer mapSerializer = new MapSerializer();
            put((Type) cls, (ObjectSerializer) mapSerializer);
            mapSerializer.addFilter(serializeFilter);
        }
    }

    public void config(Class<?> cls, SerializerFeature serializerFeature, boolean z) {
        ObjectSerializer objectWriter = getObjectWriter(cls, false);
        if (objectWriter == null) {
            SerializeBeanInfo buildBeanInfo = TypeUtils.buildBeanInfo(cls, (Map<String, String>) null, this.propertyNamingStrategy);
            if (z) {
                buildBeanInfo.features = serializerFeature.mask | buildBeanInfo.features;
            } else {
                buildBeanInfo.features = (serializerFeature.mask ^ -1) & buildBeanInfo.features;
            }
            put((Type) cls, createJavaBeanSerializer(buildBeanInfo));
        } else if (objectWriter instanceof JavaBeanSerializer) {
            SerializeBeanInfo serializeBeanInfo = ((JavaBeanSerializer) objectWriter).beanInfo;
            int i = serializeBeanInfo.features;
            if (z) {
                serializeBeanInfo.features = serializerFeature.mask | serializeBeanInfo.features;
            } else {
                serializeBeanInfo.features = (serializerFeature.mask ^ -1) & serializeBeanInfo.features;
            }
            if (i != serializeBeanInfo.features && objectWriter.getClass() != JavaBeanSerializer.class) {
                put((Type) cls, createJavaBeanSerializer(serializeBeanInfo));
            }
        }
    }

    public ObjectSerializer getObjectWriter(Class<?> cls) {
        return getObjectWriter(cls, true);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v1, resolved type: java.lang.Class} */
    /* JADX WARNING: type inference failed for: r5v0 */
    /* JADX WARNING: type inference failed for: r5v2 */
    /* JADX WARNING: type inference failed for: r5v4, types: [java.lang.reflect.Member] */
    /* JADX WARNING: type inference failed for: r5v6 */
    /* JADX WARNING: type inference failed for: r5v13 */
    /* JADX WARNING: type inference failed for: r5v14 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.alibaba.fastjson.serializer.ObjectSerializer getObjectWriter(java.lang.Class<?> r26, boolean r27) {
        /*
            r25 = this;
            r0 = r25
            r1 = r26
            com.alibaba.fastjson.serializer.ObjectSerializer r2 = r25.get(r26)
            if (r2 == 0) goto L_0x000b
            return r2
        L_0x000b:
            java.lang.Thread r2 = java.lang.Thread.currentThread()     // Catch:{ ClassCastException -> 0x0046 }
            java.lang.ClassLoader r2 = r2.getContextClassLoader()     // Catch:{ ClassCastException -> 0x0046 }
            java.lang.Class<com.alibaba.fastjson.serializer.AutowiredObjectSerializer> r3 = com.alibaba.fastjson.serializer.AutowiredObjectSerializer.class
            java.util.Set r2 = com.alibaba.fastjson.util.ServiceLoader.load(r3, (java.lang.ClassLoader) r2)     // Catch:{ ClassCastException -> 0x0046 }
            java.util.Iterator r2 = r2.iterator()     // Catch:{ ClassCastException -> 0x0046 }
        L_0x001d:
            boolean r3 = r2.hasNext()     // Catch:{ ClassCastException -> 0x0046 }
            if (r3 == 0) goto L_0x0047
            java.lang.Object r3 = r2.next()     // Catch:{ ClassCastException -> 0x0046 }
            boolean r4 = r3 instanceof com.alibaba.fastjson.serializer.AutowiredObjectSerializer     // Catch:{ ClassCastException -> 0x0046 }
            if (r4 != 0) goto L_0x002c
            goto L_0x001d
        L_0x002c:
            com.alibaba.fastjson.serializer.AutowiredObjectSerializer r3 = (com.alibaba.fastjson.serializer.AutowiredObjectSerializer) r3     // Catch:{ ClassCastException -> 0x0046 }
            java.util.Set r4 = r3.getAutowiredFor()     // Catch:{ ClassCastException -> 0x0046 }
            java.util.Iterator r4 = r4.iterator()     // Catch:{ ClassCastException -> 0x0046 }
        L_0x0036:
            boolean r5 = r4.hasNext()     // Catch:{ ClassCastException -> 0x0046 }
            if (r5 == 0) goto L_0x001d
            java.lang.Object r5 = r4.next()     // Catch:{ ClassCastException -> 0x0046 }
            java.lang.reflect.Type r5 = (java.lang.reflect.Type) r5     // Catch:{ ClassCastException -> 0x0046 }
            r0.put((java.lang.reflect.Type) r5, (com.alibaba.fastjson.serializer.ObjectSerializer) r3)     // Catch:{ ClassCastException -> 0x0046 }
            goto L_0x0036
        L_0x0046:
        L_0x0047:
            com.alibaba.fastjson.serializer.ObjectSerializer r2 = r25.get(r26)
            if (r2 != 0) goto L_0x0094
            java.lang.Class<com.alibaba.fastjson.JSON> r3 = com.alibaba.fastjson.JSON.class
            java.lang.ClassLoader r3 = r3.getClassLoader()
            java.lang.Thread r4 = java.lang.Thread.currentThread()
            java.lang.ClassLoader r4 = r4.getContextClassLoader()
            if (r3 == r4) goto L_0x0094
            java.lang.Class<com.alibaba.fastjson.serializer.AutowiredObjectSerializer> r2 = com.alibaba.fastjson.serializer.AutowiredObjectSerializer.class
            java.util.Set r2 = com.alibaba.fastjson.util.ServiceLoader.load(r2, (java.lang.ClassLoader) r3)     // Catch:{ ClassCastException -> 0x0090 }
            java.util.Iterator r2 = r2.iterator()     // Catch:{ ClassCastException -> 0x0090 }
        L_0x0067:
            boolean r3 = r2.hasNext()     // Catch:{ ClassCastException -> 0x0090 }
            if (r3 == 0) goto L_0x0090
            java.lang.Object r3 = r2.next()     // Catch:{ ClassCastException -> 0x0090 }
            boolean r4 = r3 instanceof com.alibaba.fastjson.serializer.AutowiredObjectSerializer     // Catch:{ ClassCastException -> 0x0090 }
            if (r4 != 0) goto L_0x0076
            goto L_0x0067
        L_0x0076:
            com.alibaba.fastjson.serializer.AutowiredObjectSerializer r3 = (com.alibaba.fastjson.serializer.AutowiredObjectSerializer) r3     // Catch:{ ClassCastException -> 0x0090 }
            java.util.Set r4 = r3.getAutowiredFor()     // Catch:{ ClassCastException -> 0x0090 }
            java.util.Iterator r4 = r4.iterator()     // Catch:{ ClassCastException -> 0x0090 }
        L_0x0080:
            boolean r5 = r4.hasNext()     // Catch:{ ClassCastException -> 0x0090 }
            if (r5 == 0) goto L_0x0067
            java.lang.Object r5 = r4.next()     // Catch:{ ClassCastException -> 0x0090 }
            java.lang.reflect.Type r5 = (java.lang.reflect.Type) r5     // Catch:{ ClassCastException -> 0x0090 }
            r0.put((java.lang.reflect.Type) r5, (com.alibaba.fastjson.serializer.ObjectSerializer) r3)     // Catch:{ ClassCastException -> 0x0090 }
            goto L_0x0080
        L_0x0090:
            com.alibaba.fastjson.serializer.ObjectSerializer r2 = r25.get(r26)
        L_0x0094:
            java.util.List<com.alibaba.fastjson.spi.Module> r3 = r0.modules
            java.util.Iterator r3 = r3.iterator()
        L_0x009a:
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L_0x00b0
            java.lang.Object r2 = r3.next()
            com.alibaba.fastjson.spi.Module r2 = (com.alibaba.fastjson.spi.Module) r2
            com.alibaba.fastjson.serializer.ObjectSerializer r2 = r2.createSerializer(r0, r1)
            if (r2 == 0) goto L_0x009a
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            return r2
        L_0x00b0:
            if (r2 != 0) goto L_0x04c3
            java.lang.String r3 = r26.getName()
            java.lang.Class<java.util.Map> r4 = java.util.Map.class
            boolean r4 = r4.isAssignableFrom(r1)
            if (r4 == 0) goto L_0x00c5
            com.alibaba.fastjson.serializer.MapSerializer r2 = com.alibaba.fastjson.serializer.MapSerializer.instance
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            goto L_0x04bd
        L_0x00c5:
            java.lang.Class<java.util.List> r4 = java.util.List.class
            boolean r4 = r4.isAssignableFrom(r1)
            if (r4 == 0) goto L_0x00d4
            com.alibaba.fastjson.serializer.ListSerializer r2 = com.alibaba.fastjson.serializer.ListSerializer.instance
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            goto L_0x04bd
        L_0x00d4:
            java.lang.Class<java.util.Collection> r4 = java.util.Collection.class
            boolean r4 = r4.isAssignableFrom(r1)
            if (r4 == 0) goto L_0x00e3
            com.alibaba.fastjson.serializer.CollectionCodec r2 = com.alibaba.fastjson.serializer.CollectionCodec.instance
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            goto L_0x04bd
        L_0x00e3:
            java.lang.Class<java.util.Date> r4 = java.util.Date.class
            boolean r4 = r4.isAssignableFrom(r1)
            if (r4 == 0) goto L_0x00f2
            com.alibaba.fastjson.serializer.DateCodec r2 = com.alibaba.fastjson.serializer.DateCodec.instance
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            goto L_0x04bd
        L_0x00f2:
            java.lang.Class<com.alibaba.fastjson.JSONAware> r4 = com.alibaba.fastjson.JSONAware.class
            boolean r4 = r4.isAssignableFrom(r1)
            if (r4 == 0) goto L_0x0101
            com.alibaba.fastjson.serializer.JSONAwareSerializer r2 = com.alibaba.fastjson.serializer.JSONAwareSerializer.instance
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            goto L_0x04bd
        L_0x0101:
            java.lang.Class<com.alibaba.fastjson.serializer.JSONSerializable> r4 = com.alibaba.fastjson.serializer.JSONSerializable.class
            boolean r4 = r4.isAssignableFrom(r1)
            if (r4 == 0) goto L_0x0110
            com.alibaba.fastjson.serializer.JSONSerializableSerializer r2 = com.alibaba.fastjson.serializer.JSONSerializableSerializer.instance
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            goto L_0x04bd
        L_0x0110:
            java.lang.Class<com.alibaba.fastjson.JSONStreamAware> r4 = com.alibaba.fastjson.JSONStreamAware.class
            boolean r4 = r4.isAssignableFrom(r1)
            if (r4 == 0) goto L_0x011f
            com.alibaba.fastjson.serializer.MiscCodec r2 = com.alibaba.fastjson.serializer.MiscCodec.instance
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            goto L_0x04bd
        L_0x011f:
            boolean r4 = r26.isEnum()
            r5 = 0
            if (r4 == 0) goto L_0x0187
            java.lang.reflect.Type r2 = com.alibaba.fastjson.JSON.getMixInAnnotations(r26)
            java.lang.Class r2 = (java.lang.Class) r2
            if (r2 == 0) goto L_0x0137
            java.lang.Class<com.alibaba.fastjson.annotation.JSONType> r3 = com.alibaba.fastjson.annotation.JSONType.class
            java.lang.annotation.Annotation r3 = com.alibaba.fastjson.util.TypeUtils.getAnnotation((java.lang.Class<?>) r2, r3)
            com.alibaba.fastjson.annotation.JSONType r3 = (com.alibaba.fastjson.annotation.JSONType) r3
            goto L_0x013f
        L_0x0137:
            java.lang.Class<com.alibaba.fastjson.annotation.JSONType> r3 = com.alibaba.fastjson.annotation.JSONType.class
            java.lang.annotation.Annotation r3 = com.alibaba.fastjson.util.TypeUtils.getAnnotation((java.lang.Class<?>) r1, r3)
            com.alibaba.fastjson.annotation.JSONType r3 = (com.alibaba.fastjson.annotation.JSONType) r3
        L_0x013f:
            if (r3 == 0) goto L_0x0150
            boolean r3 = r3.serializeEnumAsJavaBean()
            if (r3 == 0) goto L_0x0150
            com.alibaba.fastjson.serializer.ObjectSerializer r2 = r25.createJavaBeanSerializer((java.lang.Class<?>) r26)
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            goto L_0x04bd
        L_0x0150:
            if (r2 == 0) goto L_0x016e
            java.lang.reflect.Member r2 = getEnumValueField(r2)
            if (r2 == 0) goto L_0x0172
            boolean r3 = r2 instanceof java.lang.reflect.Method     // Catch:{ Exception -> 0x016c }
            if (r3 == 0) goto L_0x0172
            java.lang.reflect.Method r2 = (java.lang.reflect.Method) r2     // Catch:{ Exception -> 0x016c }
            java.lang.String r3 = r2.getName()     // Catch:{ Exception -> 0x016c }
            java.lang.Class[] r2 = r2.getParameterTypes()     // Catch:{ Exception -> 0x016c }
            java.lang.reflect.Method r2 = r1.getMethod(r3, r2)     // Catch:{ Exception -> 0x016c }
            r5 = r2
            goto L_0x0172
        L_0x016c:
            goto L_0x0172
        L_0x016e:
            java.lang.reflect.Member r5 = getEnumValueField(r26)
        L_0x0172:
            if (r5 == 0) goto L_0x017e
            com.alibaba.fastjson.serializer.EnumSerializer r2 = new com.alibaba.fastjson.serializer.EnumSerializer
            r2.<init>(r5)
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            goto L_0x04bd
        L_0x017e:
            com.alibaba.fastjson.serializer.ObjectSerializer r2 = r25.getEnumSerializer()
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            goto L_0x04bd
        L_0x0187:
            java.lang.Class r4 = r26.getSuperclass()
            if (r4 == 0) goto L_0x01b5
            boolean r6 = r4.isEnum()
            if (r6 == 0) goto L_0x01b5
            java.lang.Class<com.alibaba.fastjson.annotation.JSONType> r2 = com.alibaba.fastjson.annotation.JSONType.class
            java.lang.annotation.Annotation r2 = com.alibaba.fastjson.util.TypeUtils.getAnnotation((java.lang.Class<?>) r4, r2)
            com.alibaba.fastjson.annotation.JSONType r2 = (com.alibaba.fastjson.annotation.JSONType) r2
            if (r2 == 0) goto L_0x01ac
            boolean r2 = r2.serializeEnumAsJavaBean()
            if (r2 == 0) goto L_0x01ac
            com.alibaba.fastjson.serializer.ObjectSerializer r2 = r25.createJavaBeanSerializer((java.lang.Class<?>) r26)
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            goto L_0x04bd
        L_0x01ac:
            com.alibaba.fastjson.serializer.ObjectSerializer r2 = r25.getEnumSerializer()
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            goto L_0x04bd
        L_0x01b5:
            boolean r4 = r26.isArray()
            if (r4 == 0) goto L_0x01ce
            java.lang.Class r2 = r26.getComponentType()
            com.alibaba.fastjson.serializer.ObjectSerializer r3 = r0.getObjectWriter(r2)
            com.alibaba.fastjson.serializer.ArraySerializer r4 = new com.alibaba.fastjson.serializer.ArraySerializer
            r4.<init>(r2, r3)
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r4)
            r2 = r4
            goto L_0x04bd
        L_0x01ce:
            java.lang.Class<java.lang.Throwable> r4 = java.lang.Throwable.class
            boolean r4 = r4.isAssignableFrom(r1)
            if (r4 == 0) goto L_0x01f0
            com.alibaba.fastjson.PropertyNamingStrategy r2 = r0.propertyNamingStrategy
            com.alibaba.fastjson.serializer.SerializeBeanInfo r2 = com.alibaba.fastjson.util.TypeUtils.buildBeanInfo(r1, r5, r2)
            int r3 = r2.features
            com.alibaba.fastjson.serializer.SerializerFeature r4 = com.alibaba.fastjson.serializer.SerializerFeature.WriteClassName
            int r4 = r4.mask
            r3 = r3 | r4
            r2.features = r3
            com.alibaba.fastjson.serializer.JavaBeanSerializer r3 = new com.alibaba.fastjson.serializer.JavaBeanSerializer
            r3.<init>((com.alibaba.fastjson.serializer.SerializeBeanInfo) r2)
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r3)
            r2 = r3
            goto L_0x04bd
        L_0x01f0:
            java.lang.Class<java.util.TimeZone> r4 = java.util.TimeZone.class
            boolean r4 = r4.isAssignableFrom(r1)
            if (r4 != 0) goto L_0x04b8
            java.lang.Class<java.util.Map$Entry> r4 = java.util.Map.Entry.class
            boolean r4 = r4.isAssignableFrom(r1)
            if (r4 == 0) goto L_0x0202
            goto L_0x04b8
        L_0x0202:
            java.lang.Class<java.lang.Appendable> r4 = java.lang.Appendable.class
            boolean r4 = r4.isAssignableFrom(r1)
            if (r4 == 0) goto L_0x0211
            com.alibaba.fastjson.serializer.AppendableSerializer r2 = com.alibaba.fastjson.serializer.AppendableSerializer.instance
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            goto L_0x04bd
        L_0x0211:
            java.lang.Class<java.nio.charset.Charset> r4 = java.nio.charset.Charset.class
            boolean r4 = r4.isAssignableFrom(r1)
            if (r4 == 0) goto L_0x0220
            com.alibaba.fastjson.serializer.ToStringSerializer r2 = com.alibaba.fastjson.serializer.ToStringSerializer.instance
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            goto L_0x04bd
        L_0x0220:
            java.lang.Class<java.util.Enumeration> r4 = java.util.Enumeration.class
            boolean r4 = r4.isAssignableFrom(r1)
            if (r4 == 0) goto L_0x022f
            com.alibaba.fastjson.serializer.EnumerationSerializer r2 = com.alibaba.fastjson.serializer.EnumerationSerializer.instance
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            goto L_0x04bd
        L_0x022f:
            java.lang.Class<java.util.Calendar> r4 = java.util.Calendar.class
            boolean r4 = r4.isAssignableFrom(r1)
            if (r4 != 0) goto L_0x04b2
            java.lang.Class<javax.xml.datatype.XMLGregorianCalendar> r4 = javax.xml.datatype.XMLGregorianCalendar.class
            boolean r4 = r4.isAssignableFrom(r1)
            if (r4 == 0) goto L_0x0241
            goto L_0x04b2
        L_0x0241:
            boolean r4 = com.alibaba.fastjson.util.TypeUtils.isClob(r26)
            if (r4 == 0) goto L_0x024e
            com.alibaba.fastjson.serializer.ClobSerializer r2 = com.alibaba.fastjson.serializer.ClobSerializer.instance
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            goto L_0x04bd
        L_0x024e:
            boolean r4 = com.alibaba.fastjson.util.TypeUtils.isPath(r26)
            if (r4 == 0) goto L_0x025b
            com.alibaba.fastjson.serializer.ToStringSerializer r2 = com.alibaba.fastjson.serializer.ToStringSerializer.instance
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            goto L_0x04bd
        L_0x025b:
            java.lang.Class<java.util.Iterator> r4 = java.util.Iterator.class
            boolean r4 = r4.isAssignableFrom(r1)
            if (r4 == 0) goto L_0x026a
            com.alibaba.fastjson.serializer.MiscCodec r2 = com.alibaba.fastjson.serializer.MiscCodec.instance
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            goto L_0x04bd
        L_0x026a:
            java.lang.Class<org.w3c.dom.Node> r4 = org.w3c.dom.Node.class
            boolean r4 = r4.isAssignableFrom(r1)
            if (r4 == 0) goto L_0x0279
            com.alibaba.fastjson.serializer.MiscCodec r2 = com.alibaba.fastjson.serializer.MiscCodec.instance
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            goto L_0x04bd
        L_0x0279:
            java.lang.String r4 = "java.awt."
            boolean r4 = r3.startsWith(r4)
            r6 = 4
            r7 = 0
            r8 = 1
            if (r4 == 0) goto L_0x02b4
            boolean r4 = com.alibaba.fastjson.serializer.AwtCodec.support(r26)
            if (r4 == 0) goto L_0x02b4
            boolean r4 = awtError
            if (r4 != 0) goto L_0x02b4
            java.lang.String r4 = "java.awt.Color"
            java.lang.String r9 = "java.awt.Font"
            java.lang.String r10 = "java.awt.Point"
            java.lang.String r11 = "java.awt.Rectangle"
            java.lang.String[] r4 = new java.lang.String[]{r4, r9, r10, r11}     // Catch:{ all -> 0x02b2 }
            r9 = 0
        L_0x029b:
            if (r9 >= r6) goto L_0x02b4
            r10 = r4[r9]     // Catch:{ all -> 0x02b2 }
            boolean r11 = r10.equals(r3)     // Catch:{ all -> 0x02b2 }
            if (r11 == 0) goto L_0x02af
            java.lang.Class r4 = java.lang.Class.forName(r10)     // Catch:{ all -> 0x02b2 }
            com.alibaba.fastjson.serializer.AwtCodec r2 = com.alibaba.fastjson.serializer.AwtCodec.instance     // Catch:{ all -> 0x02b2 }
            r0.put((java.lang.reflect.Type) r4, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)     // Catch:{ all -> 0x02b2 }
            return r2
        L_0x02af:
            int r9 = r9 + 1
            goto L_0x029b
        L_0x02b2:
            awtError = r8
        L_0x02b4:
            boolean r4 = jdk8Error
            r9 = 11
            r10 = 2
            if (r4 != 0) goto L_0x034d
            java.lang.String r4 = "java.time."
            boolean r4 = r3.startsWith(r4)
            java.lang.String r11 = "java.util.concurrent.atomic.DoubleAdder"
            java.lang.String r12 = "java.util.concurrent.atomic.LongAdder"
            java.lang.String r13 = "java.util.Optional"
            if (r4 != 0) goto L_0x02db
            boolean r4 = r3.startsWith(r13)
            if (r4 != 0) goto L_0x02db
            boolean r4 = r3.equals(r12)
            if (r4 != 0) goto L_0x02db
            boolean r4 = r3.equals(r11)
            if (r4 == 0) goto L_0x034d
        L_0x02db:
            java.lang.String r14 = "java.time.LocalDateTime"
            java.lang.String r15 = "java.time.LocalDate"
            java.lang.String r16 = "java.time.LocalTime"
            java.lang.String r17 = "java.time.ZonedDateTime"
            java.lang.String r18 = "java.time.OffsetDateTime"
            java.lang.String r19 = "java.time.OffsetTime"
            java.lang.String r20 = "java.time.ZoneOffset"
            java.lang.String r21 = "java.time.ZoneRegion"
            java.lang.String r22 = "java.time.Period"
            java.lang.String r23 = "java.time.Duration"
            java.lang.String r24 = "java.time.Instant"
            java.lang.String[] r4 = new java.lang.String[]{r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24}     // Catch:{ all -> 0x034b }
            r14 = 0
        L_0x02f6:
            if (r14 >= r9) goto L_0x030d
            r15 = r4[r14]     // Catch:{ all -> 0x034b }
            boolean r16 = r15.equals(r3)     // Catch:{ all -> 0x034b }
            if (r16 == 0) goto L_0x030a
            java.lang.Class r4 = java.lang.Class.forName(r15)     // Catch:{ all -> 0x034b }
            com.alibaba.fastjson.parser.deserializer.Jdk8DateCodec r2 = com.alibaba.fastjson.parser.deserializer.Jdk8DateCodec.instance     // Catch:{ all -> 0x034b }
            r0.put((java.lang.reflect.Type) r4, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)     // Catch:{ all -> 0x034b }
            return r2
        L_0x030a:
            int r14 = r14 + 1
            goto L_0x02f6
        L_0x030d:
            java.lang.String r4 = "java.util.OptionalDouble"
            java.lang.String r14 = "java.util.OptionalInt"
            java.lang.String r15 = "java.util.OptionalLong"
            java.lang.String[] r4 = new java.lang.String[]{r13, r4, r14, r15}     // Catch:{ all -> 0x034b }
            r13 = 0
        L_0x0318:
            if (r13 >= r6) goto L_0x032f
            r14 = r4[r13]     // Catch:{ all -> 0x034b }
            boolean r15 = r14.equals(r3)     // Catch:{ all -> 0x034b }
            if (r15 == 0) goto L_0x032c
            java.lang.Class r4 = java.lang.Class.forName(r14)     // Catch:{ all -> 0x034b }
            com.alibaba.fastjson.parser.deserializer.OptionalCodec r2 = com.alibaba.fastjson.parser.deserializer.OptionalCodec.instance     // Catch:{ all -> 0x034b }
            r0.put((java.lang.reflect.Type) r4, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)     // Catch:{ all -> 0x034b }
            return r2
        L_0x032c:
            int r13 = r13 + 1
            goto L_0x0318
        L_0x032f:
            java.lang.String[] r4 = new java.lang.String[]{r12, r11}     // Catch:{ all -> 0x034b }
            r6 = 0
        L_0x0334:
            if (r6 >= r10) goto L_0x034d
            r11 = r4[r6]     // Catch:{ all -> 0x034b }
            boolean r12 = r11.equals(r3)     // Catch:{ all -> 0x034b }
            if (r12 == 0) goto L_0x0348
            java.lang.Class r4 = java.lang.Class.forName(r11)     // Catch:{ all -> 0x034b }
            com.alibaba.fastjson.serializer.AdderSerializer r2 = com.alibaba.fastjson.serializer.AdderSerializer.instance     // Catch:{ all -> 0x034b }
            r0.put((java.lang.reflect.Type) r4, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)     // Catch:{ all -> 0x034b }
            return r2
        L_0x0348:
            int r6 = r6 + 1
            goto L_0x0334
        L_0x034b:
            jdk8Error = r8
        L_0x034d:
            boolean r4 = oracleJdbcError
            if (r4 != 0) goto L_0x037b
            java.lang.String r4 = "oracle.sql."
            boolean r4 = r3.startsWith(r4)
            if (r4 == 0) goto L_0x037b
            java.lang.String r4 = "oracle.sql.DATE"
            java.lang.String r6 = "oracle.sql.TIMESTAMP"
            java.lang.String[] r4 = new java.lang.String[]{r4, r6}     // Catch:{ all -> 0x0379 }
            r6 = 0
        L_0x0362:
            if (r6 >= r10) goto L_0x037b
            r11 = r4[r6]     // Catch:{ all -> 0x0379 }
            boolean r12 = r11.equals(r3)     // Catch:{ all -> 0x0379 }
            if (r12 == 0) goto L_0x0376
            java.lang.Class r4 = java.lang.Class.forName(r11)     // Catch:{ all -> 0x0379 }
            com.alibaba.fastjson.serializer.DateCodec r2 = com.alibaba.fastjson.serializer.DateCodec.instance     // Catch:{ all -> 0x0379 }
            r0.put((java.lang.reflect.Type) r4, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)     // Catch:{ all -> 0x0379 }
            return r2
        L_0x0376:
            int r6 = r6 + 1
            goto L_0x0362
        L_0x0379:
            oracleJdbcError = r8
        L_0x037b:
            boolean r4 = springfoxError
            if (r4 != 0) goto L_0x0393
            java.lang.String r4 = "springfox.documentation.spring.web.json.Json"
            boolean r6 = r3.equals(r4)
            if (r6 == 0) goto L_0x0393
            java.lang.Class r4 = java.lang.Class.forName(r4)     // Catch:{ ClassNotFoundException -> 0x0391 }
            com.alibaba.fastjson.support.springfox.SwaggerJsonSerializer r2 = com.alibaba.fastjson.support.springfox.SwaggerJsonSerializer.instance     // Catch:{ ClassNotFoundException -> 0x0391 }
            r0.put((java.lang.reflect.Type) r4, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)     // Catch:{ ClassNotFoundException -> 0x0391 }
            return r2
        L_0x0391:
            springfoxError = r8
        L_0x0393:
            boolean r4 = guavaError
            if (r4 != 0) goto L_0x03c8
            java.lang.String r4 = "com.google.common.collect."
            boolean r4 = r3.startsWith(r4)
            if (r4 == 0) goto L_0x03c8
            r4 = 5
            java.lang.String r6 = "com.google.common.collect.HashMultimap"
            java.lang.String r11 = "com.google.common.collect.LinkedListMultimap"
            java.lang.String r12 = "com.google.common.collect.LinkedHashMultimap"
            java.lang.String r13 = "com.google.common.collect.ArrayListMultimap"
            java.lang.String r14 = "com.google.common.collect.TreeMultimap"
            java.lang.String[] r6 = new java.lang.String[]{r6, r11, r12, r13, r14}     // Catch:{ ClassNotFoundException -> 0x03c6 }
            r11 = 0
        L_0x03af:
            if (r11 >= r4) goto L_0x03c8
            r12 = r6[r11]     // Catch:{ ClassNotFoundException -> 0x03c6 }
            boolean r13 = r12.equals(r3)     // Catch:{ ClassNotFoundException -> 0x03c6 }
            if (r13 == 0) goto L_0x03c3
            java.lang.Class r4 = java.lang.Class.forName(r12)     // Catch:{ ClassNotFoundException -> 0x03c6 }
            com.alibaba.fastjson.serializer.GuavaCodec r2 = com.alibaba.fastjson.serializer.GuavaCodec.instance     // Catch:{ ClassNotFoundException -> 0x03c6 }
            r0.put((java.lang.reflect.Type) r4, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)     // Catch:{ ClassNotFoundException -> 0x03c6 }
            return r2
        L_0x03c3:
            int r11 = r11 + 1
            goto L_0x03af
        L_0x03c6:
            guavaError = r8
        L_0x03c8:
            java.lang.String r4 = "net.sf.json.JSONNull"
            boolean r4 = r3.equals(r4)
            if (r4 == 0) goto L_0x03d6
            com.alibaba.fastjson.serializer.MiscCodec r2 = com.alibaba.fastjson.serializer.MiscCodec.instance
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            return r2
        L_0x03d6:
            java.lang.String r4 = "org.json.JSONObject"
            boolean r4 = r3.equals(r4)
            if (r4 == 0) goto L_0x03e4
            com.alibaba.fastjson.serializer.JSONObjectCodec r2 = com.alibaba.fastjson.serializer.JSONObjectCodec.instance
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            return r2
        L_0x03e4:
            boolean r4 = jodaError
            if (r4 != 0) goto L_0x0424
            java.lang.String r4 = "org.joda."
            boolean r4 = r3.startsWith(r4)
            if (r4 == 0) goto L_0x0424
            java.lang.String r11 = "org.joda.time.LocalDate"
            java.lang.String r12 = "org.joda.time.LocalDateTime"
            java.lang.String r13 = "org.joda.time.LocalTime"
            java.lang.String r14 = "org.joda.time.Instant"
            java.lang.String r15 = "org.joda.time.DateTime"
            java.lang.String r16 = "org.joda.time.Period"
            java.lang.String r17 = "org.joda.time.Duration"
            java.lang.String r18 = "org.joda.time.DateTimeZone"
            java.lang.String r19 = "org.joda.time.UTCDateTimeZone"
            java.lang.String r20 = "org.joda.time.tz.CachedDateTimeZone"
            java.lang.String r21 = "org.joda.time.tz.FixedDateTimeZone"
            java.lang.String[] r4 = new java.lang.String[]{r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21}     // Catch:{ ClassNotFoundException -> 0x0422 }
            r6 = 0
        L_0x040b:
            if (r6 >= r9) goto L_0x0424
            r11 = r4[r6]     // Catch:{ ClassNotFoundException -> 0x0422 }
            boolean r12 = r11.equals(r3)     // Catch:{ ClassNotFoundException -> 0x0422 }
            if (r12 == 0) goto L_0x041f
            java.lang.Class r4 = java.lang.Class.forName(r11)     // Catch:{ ClassNotFoundException -> 0x0422 }
            com.alibaba.fastjson.serializer.JodaCodec r2 = com.alibaba.fastjson.serializer.JodaCodec.instance     // Catch:{ ClassNotFoundException -> 0x0422 }
            r0.put((java.lang.reflect.Type) r4, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)     // Catch:{ ClassNotFoundException -> 0x0422 }
            return r2
        L_0x041f:
            int r6 = r6 + 1
            goto L_0x040b
        L_0x0422:
            jodaError = r8
        L_0x0424:
            java.lang.String r4 = "java.nio.HeapByteBuffer"
            boolean r4 = r4.equals(r3)
            if (r4 == 0) goto L_0x0432
            com.alibaba.fastjson.serializer.ByteBufferCodec r2 = com.alibaba.fastjson.serializer.ByteBufferCodec.instance
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            return r2
        L_0x0432:
            java.lang.String r4 = "org.javamoney.moneta.Money"
            boolean r4 = r4.equals(r3)
            if (r4 == 0) goto L_0x0440
            com.alibaba.fastjson.support.moneta.MonetaCodec r2 = com.alibaba.fastjson.support.moneta.MonetaCodec.instance
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            return r2
        L_0x0440:
            java.lang.String r4 = "com.google.protobuf.Descriptors$FieldDescriptor"
            boolean r3 = r4.equals(r3)
            if (r3 == 0) goto L_0x044e
            com.alibaba.fastjson.serializer.ToStringSerializer r2 = com.alibaba.fastjson.serializer.ToStringSerializer.instance
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            return r2
        L_0x044e:
            java.lang.Class[] r3 = r26.getInterfaces()
            int r4 = r3.length
            if (r4 != r8) goto L_0x0465
            r4 = r3[r7]
            boolean r4 = r4.isAnnotation()
            if (r4 == 0) goto L_0x0465
            com.alibaba.fastjson.serializer.AnnotationSerializer r2 = com.alibaba.fastjson.serializer.AnnotationSerializer.instance
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            com.alibaba.fastjson.serializer.AnnotationSerializer r1 = com.alibaba.fastjson.serializer.AnnotationSerializer.instance
            return r1
        L_0x0465:
            boolean r4 = com.alibaba.fastjson.util.TypeUtils.isProxy(r26)
            if (r4 == 0) goto L_0x0477
            java.lang.Class r2 = r26.getSuperclass()
            com.alibaba.fastjson.serializer.ObjectSerializer r2 = r0.getObjectWriter(r2)
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            return r2
        L_0x0477:
            boolean r4 = java.lang.reflect.Proxy.isProxyClass(r26)
            if (r4 == 0) goto L_0x04a8
            int r4 = r3.length
            if (r4 != r10) goto L_0x0483
            r5 = r3[r8]
            goto L_0x049e
        L_0x0483:
            int r4 = r3.length
            r6 = r5
        L_0x0485:
            if (r7 >= r4) goto L_0x049d
            r8 = r3[r7]
            java.lang.String r9 = r8.getName()
            java.lang.String r10 = "org.springframework.aop."
            boolean r9 = r9.startsWith(r10)
            if (r9 == 0) goto L_0x0496
            goto L_0x049a
        L_0x0496:
            if (r6 == 0) goto L_0x0499
            goto L_0x049e
        L_0x0499:
            r6 = r8
        L_0x049a:
            int r7 = r7 + 1
            goto L_0x0485
        L_0x049d:
            r5 = r6
        L_0x049e:
            if (r5 == 0) goto L_0x04a8
            com.alibaba.fastjson.serializer.ObjectSerializer r2 = r0.getObjectWriter(r5)
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            return r2
        L_0x04a8:
            if (r27 == 0) goto L_0x04bd
            com.alibaba.fastjson.serializer.ObjectSerializer r2 = r25.createJavaBeanSerializer((java.lang.Class<?>) r26)
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            goto L_0x04bd
        L_0x04b2:
            com.alibaba.fastjson.serializer.CalendarCodec r2 = com.alibaba.fastjson.serializer.CalendarCodec.instance
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
            goto L_0x04bd
        L_0x04b8:
            com.alibaba.fastjson.serializer.MiscCodec r2 = com.alibaba.fastjson.serializer.MiscCodec.instance
            r0.put((java.lang.reflect.Type) r1, (com.alibaba.fastjson.serializer.ObjectSerializer) r2)
        L_0x04bd:
            if (r2 != 0) goto L_0x04c3
            com.alibaba.fastjson.serializer.ObjectSerializer r2 = r25.get(r26)
        L_0x04c3:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.SerializeConfig.getObjectWriter(java.lang.Class, boolean):com.alibaba.fastjson.serializer.ObjectSerializer");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v0, resolved type: java.lang.reflect.Method} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v1, resolved type: java.lang.reflect.Method} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v1, resolved type: java.lang.reflect.Field[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v2, resolved type: java.lang.reflect.Method} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: java.lang.reflect.Field} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v3, resolved type: java.lang.reflect.Method} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v4, resolved type: java.lang.reflect.Method} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v5, resolved type: java.lang.reflect.Method} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v6, resolved type: java.lang.reflect.Method} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.reflect.Member getEnumValueField(java.lang.Class r9) {
        /*
            java.lang.reflect.Method[] r0 = r9.getMethods()
            int r1 = r0.length
            r2 = 0
            r3 = 0
            r5 = r3
            r4 = 0
        L_0x0009:
            if (r4 >= r1) goto L_0x0027
            r6 = r0[r4]
            java.lang.Class r7 = r6.getReturnType()
            java.lang.Class<java.lang.Void> r8 = java.lang.Void.class
            if (r7 != r8) goto L_0x0016
            goto L_0x0024
        L_0x0016:
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r7 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r7 = r6.getAnnotation(r7)
            com.alibaba.fastjson.annotation.JSONField r7 = (com.alibaba.fastjson.annotation.JSONField) r7
            if (r7 == 0) goto L_0x0024
            if (r5 == 0) goto L_0x0023
            return r3
        L_0x0023:
            r5 = r6
        L_0x0024:
            int r4 = r4 + 1
            goto L_0x0009
        L_0x0027:
            java.lang.reflect.Field[] r9 = r9.getFields()
            int r0 = r9.length
        L_0x002c:
            if (r2 >= r0) goto L_0x0041
            r1 = r9[r2]
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r4 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r4 = r1.getAnnotation(r4)
            com.alibaba.fastjson.annotation.JSONField r4 = (com.alibaba.fastjson.annotation.JSONField) r4
            if (r4 == 0) goto L_0x003e
            if (r5 == 0) goto L_0x003d
            return r3
        L_0x003d:
            r5 = r1
        L_0x003e:
            int r2 = r2 + 1
            goto L_0x002c
        L_0x0041:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.SerializeConfig.getEnumValueField(java.lang.Class):java.lang.reflect.Member");
    }

    /* access modifiers changed from: protected */
    public ObjectSerializer getEnumSerializer() {
        return EnumSerializer.instance;
    }

    public final ObjectSerializer get(Type type) {
        Type mixInAnnotations = JSON.getMixInAnnotations(type);
        if (mixInAnnotations == null) {
            return this.serializers.get(type);
        }
        IdentityHashMap identityHashMap = this.mixInSerializers.get(type);
        if (identityHashMap == null) {
            return null;
        }
        return (ObjectSerializer) identityHashMap.get(mixInAnnotations);
    }

    public boolean put(Object obj, Object obj2) {
        return put((Type) obj, (ObjectSerializer) obj2);
    }

    public boolean put(Type type, ObjectSerializer objectSerializer) {
        Type mixInAnnotations = JSON.getMixInAnnotations(type);
        if (mixInAnnotations == null) {
            return this.serializers.put(type, objectSerializer);
        }
        IdentityHashMap identityHashMap = this.mixInSerializers.get(type);
        if (identityHashMap == null) {
            identityHashMap = new IdentityHashMap(4);
            this.mixInSerializers.put(type, identityHashMap);
        }
        return identityHashMap.put(mixInAnnotations, objectSerializer);
    }

    public void configEnumAsJavaBean(Class<? extends Enum>... clsArr) {
        for (Class<? extends Enum> cls : clsArr) {
            put((Type) cls, createJavaBeanSerializer((Class<?>) cls));
        }
    }

    public void setPropertyNamingStrategy(PropertyNamingStrategy propertyNamingStrategy2) {
        this.propertyNamingStrategy = propertyNamingStrategy2;
    }

    public void clearSerializers() {
        this.serializers.clear();
        initSerializers();
    }

    public void register(Module module) {
        this.modules.add(module);
    }
}
