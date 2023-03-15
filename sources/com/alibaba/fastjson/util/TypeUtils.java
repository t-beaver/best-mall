package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.EnumDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.CalendarCodec;
import com.alibaba.fastjson.serializer.SerializeBeanInfo;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.performance.WXInstanceApm;
import io.dcloud.common.util.ExifInterface;
import io.dcloud.feature.gg.dcloud.ADSim;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypeUtils {
    private static final Pattern NUMBER_WITH_TRAILING_ZEROS_PATTERN = Pattern.compile("\\.0*$");
    private static Object OPTIONAL_EMPTY = null;
    private static boolean OPTIONAL_ERROR = false;
    private static Function<Map<String, Class<?>>, Void> addBaseClassMappingsFunction = new Function<Map<String, Class<?>>, Void>() {
        public Void apply(Map<String, Class<?>> map) {
            Class[] clsArr = {Time.class, Date.class, Timestamp.class};
            for (int i = 0; i < 3; i++) {
                Class cls = clsArr[i];
                if (cls != null) {
                    map.put(cls.getName(), cls);
                }
            }
            return null;
        }
    };
    private static BiFunction<Object, Class, Object> castFunction = new BiFunction<Object, Class, Object>() {
        public Object apply(Object obj, Class cls) {
            if (cls == Date.class) {
                return TypeUtils.castToSqlDate(obj);
            }
            if (cls == Time.class) {
                return TypeUtils.castToSqlTime(obj);
            }
            if (cls == Timestamp.class) {
                return TypeUtils.castToTimestamp(obj);
            }
            return null;
        }
    };
    private static Function<Object, Object> castToSqlDateFunction = new Function<Object, Object>() {
        public Object apply(Object obj) {
            long j;
            if (obj == null) {
                return null;
            }
            if (obj instanceof Date) {
                return (Date) obj;
            }
            if (obj instanceof java.util.Date) {
                return new Date(((java.util.Date) obj).getTime());
            }
            if (obj instanceof Calendar) {
                return new Date(((Calendar) obj).getTimeInMillis());
            }
            if (obj instanceof BigDecimal) {
                j = TypeUtils.longValue((BigDecimal) obj);
            } else {
                j = obj instanceof Number ? ((Number) obj).longValue() : 0;
            }
            if (obj instanceof String) {
                String str = (String) obj;
                if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                    return null;
                }
                if (TypeUtils.isNumber(str)) {
                    j = Long.parseLong(str);
                } else {
                    JSONScanner jSONScanner = new JSONScanner(str);
                    if (jSONScanner.scanISO8601DateIfMatch(false)) {
                        j = jSONScanner.getCalendar().getTime().getTime();
                    } else {
                        throw new JSONException("can not cast to Timestamp, value : " + str);
                    }
                }
            }
            if (j > 0) {
                return new Date(j);
            }
            throw new JSONException("can not cast to Date, value : " + obj);
        }
    };
    private static Function<Object, Object> castToSqlTimeFunction = new Function<Object, Object>() {
        public Object apply(Object obj) {
            long j;
            if (obj == null) {
                return null;
            }
            if (obj instanceof Time) {
                return (Time) obj;
            }
            if (obj instanceof java.util.Date) {
                return new Time(((java.util.Date) obj).getTime());
            }
            if (obj instanceof Calendar) {
                return new Time(((Calendar) obj).getTimeInMillis());
            }
            if (obj instanceof BigDecimal) {
                j = TypeUtils.longValue((BigDecimal) obj);
            } else {
                j = obj instanceof Number ? ((Number) obj).longValue() : 0;
            }
            if (obj instanceof String) {
                String str = (String) obj;
                if (str.length() == 0 || "null".equalsIgnoreCase(str)) {
                    return null;
                }
                if (TypeUtils.isNumber(str)) {
                    j = Long.parseLong(str);
                } else if (str.length() == 8 && str.charAt(2) == ':' && str.charAt(5) == ':') {
                    return Time.valueOf(str);
                } else {
                    JSONScanner jSONScanner = new JSONScanner(str);
                    if (jSONScanner.scanISO8601DateIfMatch(false)) {
                        j = jSONScanner.getCalendar().getTime().getTime();
                    } else {
                        throw new JSONException("can not cast to Timestamp, value : " + str);
                    }
                }
            }
            if (j > 0) {
                return new Time(j);
            }
            throw new JSONException("can not cast to Date, value : " + obj);
        }
    };
    public static Function<Object, Object> castToTimestampFunction = new Function<Object, Object>() {
        public Object apply(Object obj) {
            Object obj2 = obj;
            if (obj2 == null) {
                return null;
            }
            if (obj2 instanceof Calendar) {
                return new Timestamp(((Calendar) obj2).getTimeInMillis());
            }
            if (obj2 instanceof Timestamp) {
                return (Timestamp) obj2;
            }
            if (obj2 instanceof java.util.Date) {
                return new Timestamp(((java.util.Date) obj2).getTime());
            }
            long j = 0;
            if (obj2 instanceof BigDecimal) {
                j = TypeUtils.longValue((BigDecimal) obj2);
            } else if (obj2 instanceof Number) {
                j = ((Number) obj2).longValue();
            }
            if (obj2 instanceof String) {
                String str = (String) obj2;
                if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                    return null;
                }
                if (str.endsWith(".000000000")) {
                    str = str.substring(0, str.length() - 10);
                } else if (str.endsWith(".000000")) {
                    str = str.substring(0, str.length() - 7);
                }
                if (str.length() == 29 && str.charAt(4) == '-' && str.charAt(7) == '-' && str.charAt(10) == ' ' && str.charAt(13) == ':' && str.charAt(16) == ':' && str.charAt(19) == '.') {
                    int num = TypeUtils.num(str.charAt(0), str.charAt(1), str.charAt(2), str.charAt(3));
                    return new Timestamp(num - 1900, TypeUtils.num(str.charAt(5), str.charAt(6)) - 1, TypeUtils.num(str.charAt(8), str.charAt(9)), TypeUtils.num(str.charAt(11), str.charAt(12)), TypeUtils.num(str.charAt(14), str.charAt(15)), TypeUtils.num(str.charAt(17), str.charAt(18)), TypeUtils.num(str.charAt(20), str.charAt(21), str.charAt(22), str.charAt(23), str.charAt(24), str.charAt(25), str.charAt(26), str.charAt(27), str.charAt(28)));
                } else if (TypeUtils.isNumber(str)) {
                    j = Long.parseLong(str);
                } else {
                    JSONScanner jSONScanner = new JSONScanner(str);
                    if (jSONScanner.scanISO8601DateIfMatch(false)) {
                        j = jSONScanner.getCalendar().getTime().getTime();
                    } else {
                        throw new JSONException("can not cast to Timestamp, value : " + str);
                    }
                }
            }
            return new Timestamp(j);
        }
    };
    private static volatile boolean classXmlAccessorType_error = false;
    private static Class<? extends Annotation> class_JacksonCreator = null;
    private static boolean class_JacksonCreator_error = false;
    private static Class<? extends Annotation> class_ManyToMany = null;
    private static boolean class_ManyToMany_error = false;
    private static Class<? extends Annotation> class_OneToMany = null;
    private static boolean class_OneToMany_error = false;
    private static volatile Class class_XmlAccessType = null;
    private static volatile Class class_XmlAccessorType = null;
    private static Class class_deque = null;
    public static boolean compatibleWithFieldName = false;
    public static boolean compatibleWithJavaBean = false;
    private static volatile Field field_XmlAccessType_FIELD = null;
    private static volatile Object field_XmlAccessType_FIELD_VALUE = null;
    public static final long fnv1a_64_magic_hashcode = -3750763034362895579L;
    public static final long fnv1a_64_magic_prime = 1099511628211L;
    private static Function<Class, Boolean> isClobFunction = new Function<Class, Boolean>() {
        public Boolean apply(Class cls) {
            return Boolean.valueOf(Clob.class.isAssignableFrom(cls));
        }
    };
    private static final Set<String> isProxyClassNames = new HashSet<String>(6) {
        {
            add("net.sf.cglib.proxy.Factory");
            add("org.springframework.cglib.proxy.Factory");
            add("javassist.util.proxy.ProxyObject");
            add("org.apache.ibatis.javassist.util.proxy.ProxyObject");
            add("org.hibernate.proxy.HibernateProxy");
            add("org.springframework.context.annotation.ConfigurationClassEnhancer$EnhancedConfiguration");
        }
    };
    private static volatile Map<Class, String[]> kotlinIgnores;
    private static volatile boolean kotlinIgnores_error;
    private static volatile boolean kotlin_class_klass_error;
    private static volatile boolean kotlin_error;
    private static volatile Constructor kotlin_kclass_constructor;
    private static volatile Method kotlin_kclass_getConstructors;
    private static volatile Method kotlin_kfunction_getParameters;
    private static volatile Method kotlin_kparameter_getName;
    private static volatile Class kotlin_metadata;
    private static volatile boolean kotlin_metadata_error;
    private static ConcurrentMap<String, Class<?>> mappings = new ConcurrentHashMap(256, 0.75f, 1);
    private static Method method_HibernateIsInitialized = null;
    private static boolean method_HibernateIsInitialized_error = false;
    private static volatile Method method_XmlAccessorType_value = null;
    private static Class<?> optionalClass;
    private static boolean optionalClassInited = false;
    private static Method oracleDateMethod;
    private static boolean oracleDateMethodInited = false;
    private static Method oracleTimestampMethod;
    private static boolean oracleTimestampMethodInited = false;
    private static Class<?> pathClass;
    private static boolean pathClass_error = false;
    private static final Map primitiveTypeMap = new HashMap<Class, String>(8) {
        {
            put(Boolean.TYPE, "Z");
            put(Character.TYPE, "C");
            put(Byte.TYPE, "B");
            put(Short.TYPE, ExifInterface.LATITUDE_SOUTH);
            put(Integer.TYPE, "I");
            put(Long.TYPE, "J");
            put(Float.TYPE, "F");
            put(Double.TYPE, "D");
        }
    };
    private static boolean setAccessibleEnable = true;
    private static Class<? extends Annotation> transientClass;
    private static boolean transientClassInited = false;

    static int num(char c, char c2) {
        if (c < '0' || c > '9' || c2 < '0' || c2 > '9') {
            return -1;
        }
        return ((c - '0') * 10) + (c2 - '0');
    }

    static int num(char c, char c2, char c3, char c4) {
        if (c < '0' || c > '9' || c2 < '0' || c2 > '9' || c3 < '0' || c3 > '9' || c4 < '0' || c4 > '9') {
            return -1;
        }
        return ((c - '0') * 1000) + ((c2 - '0') * 100) + ((c3 - '0') * 10) + (c4 - '0');
    }

    static int num(char c, char c2, char c3, char c4, char c5, char c6, char c7, char c8, char c9) {
        if (c < '0' || c > '9' || c2 < '0' || c2 > '9' || c3 < '0' || c3 > '9' || c4 < '0' || c4 > '9' || c5 < '0' || c5 > '9' || c6 < '0' || c6 > '9' || c7 < '0' || c7 > '9' || c8 < '0' || c8 > '9' || c9 < '0' || c9 > '9') {
            return -1;
        }
        return ((c - '0') * 100000000) + ((c2 - '0') * 10000000) + ((c3 - '0') * 1000000) + ((c4 - '0') * 100000) + ((c5 - '0') * ADSim.INTISPLSH) + ((c6 - '0') * 1000) + ((c7 - '0') * 100) + ((c8 - '0') * 10) + (c9 - '0');
    }

    static {
        compatibleWithJavaBean = false;
        compatibleWithFieldName = false;
        class_deque = null;
        try {
            compatibleWithJavaBean = AbsoluteConst.TRUE.equals(IOUtils.getStringProperty(IOUtils.FASTJSON_COMPATIBLEWITHJAVABEAN));
            compatibleWithFieldName = AbsoluteConst.TRUE.equals(IOUtils.getStringProperty(IOUtils.FASTJSON_COMPATIBLEWITHFIELDNAME));
        } catch (Throwable unused) {
        }
        try {
            class_deque = Class.forName("java.util.Deque");
        } catch (Throwable unused2) {
        }
        addBaseClassMappings();
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x0053 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0054  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isXmlField(java.lang.Class r5) {
        /*
            java.lang.Class r0 = class_XmlAccessorType
            r1 = 1
            if (r0 != 0) goto L_0x0014
            boolean r0 = classXmlAccessorType_error
            if (r0 != 0) goto L_0x0014
            java.lang.String r0 = "javax.xml.bind.annotation.XmlAccessorType"
            java.lang.Class r0 = java.lang.Class.forName(r0)     // Catch:{ all -> 0x0012 }
            class_XmlAccessorType = r0     // Catch:{ all -> 0x0012 }
            goto L_0x0014
        L_0x0012:
            classXmlAccessorType_error = r1
        L_0x0014:
            java.lang.Class r0 = class_XmlAccessorType
            r2 = 0
            if (r0 != 0) goto L_0x001a
            return r2
        L_0x001a:
            java.lang.Class r0 = class_XmlAccessorType
            java.lang.annotation.Annotation r5 = getAnnotation((java.lang.Class<?>) r5, r0)
            if (r5 != 0) goto L_0x0023
            return r2
        L_0x0023:
            java.lang.reflect.Method r0 = method_XmlAccessorType_value
            if (r0 != 0) goto L_0x003b
            boolean r0 = classXmlAccessorType_error
            if (r0 != 0) goto L_0x003b
            java.lang.Class r0 = class_XmlAccessorType     // Catch:{ all -> 0x0039 }
            java.lang.String r3 = "value"
            java.lang.Class[] r4 = new java.lang.Class[r2]     // Catch:{ all -> 0x0039 }
            java.lang.reflect.Method r0 = r0.getMethod(r3, r4)     // Catch:{ all -> 0x0039 }
            method_XmlAccessorType_value = r0     // Catch:{ all -> 0x0039 }
            goto L_0x003b
        L_0x0039:
            classXmlAccessorType_error = r1
        L_0x003b:
            java.lang.reflect.Method r0 = method_XmlAccessorType_value
            if (r0 != 0) goto L_0x0040
            return r2
        L_0x0040:
            boolean r0 = classXmlAccessorType_error
            r3 = 0
            if (r0 != 0) goto L_0x0050
            java.lang.reflect.Method r0 = method_XmlAccessorType_value     // Catch:{ all -> 0x004e }
            java.lang.Object[] r4 = new java.lang.Object[r2]     // Catch:{ all -> 0x004e }
            java.lang.Object r5 = r0.invoke(r5, r4)     // Catch:{ all -> 0x004e }
            goto L_0x0051
        L_0x004e:
            classXmlAccessorType_error = r1
        L_0x0050:
            r5 = r3
        L_0x0051:
            if (r5 != 0) goto L_0x0054
            return r2
        L_0x0054:
            java.lang.Class r0 = class_XmlAccessType
            if (r0 != 0) goto L_0x0079
            boolean r0 = classXmlAccessorType_error
            if (r0 != 0) goto L_0x0079
            java.lang.String r0 = "javax.xml.bind.annotation.XmlAccessType"
            java.lang.Class r0 = java.lang.Class.forName(r0)     // Catch:{ all -> 0x0077 }
            class_XmlAccessType = r0     // Catch:{ all -> 0x0077 }
            java.lang.Class r0 = class_XmlAccessType     // Catch:{ all -> 0x0077 }
            java.lang.String r4 = "FIELD"
            java.lang.reflect.Field r0 = r0.getField(r4)     // Catch:{ all -> 0x0077 }
            field_XmlAccessType_FIELD = r0     // Catch:{ all -> 0x0077 }
            java.lang.reflect.Field r0 = field_XmlAccessType_FIELD     // Catch:{ all -> 0x0077 }
            java.lang.Object r0 = r0.get(r3)     // Catch:{ all -> 0x0077 }
            field_XmlAccessType_FIELD_VALUE = r0     // Catch:{ all -> 0x0077 }
            goto L_0x0079
        L_0x0077:
            classXmlAccessorType_error = r1
        L_0x0079:
            java.lang.Object r0 = field_XmlAccessType_FIELD_VALUE
            if (r5 != r0) goto L_0x007e
            goto L_0x007f
        L_0x007e:
            r1 = 0
        L_0x007f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.TypeUtils.isXmlField(java.lang.Class):boolean");
    }

    public static Annotation getXmlAccessorType(Class cls) {
        if (class_XmlAccessorType == null && !classXmlAccessorType_error) {
            try {
                class_XmlAccessorType = Class.forName("javax.xml.bind.annotation.XmlAccessorType");
            } catch (Throwable unused) {
                classXmlAccessorType_error = true;
            }
        }
        if (class_XmlAccessorType == null) {
            return null;
        }
        return getAnnotation((Class<?>) cls, class_XmlAccessorType);
    }

    public static boolean isClob(Class cls) {
        Boolean bool = (Boolean) ModuleUtil.callWhenHasJavaSql(isClobFunction, cls);
        if (bool != null) {
            return bool.booleanValue();
        }
        return false;
    }

    public static String castToString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public static Byte castToByte(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof BigDecimal) {
            return Byte.valueOf(byteValue((BigDecimal) obj));
        }
        if (obj instanceof Number) {
            return Byte.valueOf(((Number) obj).byteValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                return null;
            }
            return Byte.valueOf(Byte.parseByte(str));
        } else if (obj instanceof Boolean) {
            return Byte.valueOf(((Boolean) obj).booleanValue() ? (byte) 1 : 0);
        } else {
            throw new JSONException("can not cast to byte, value : " + obj);
        }
    }

    public static Character castToChar(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Character) {
            return (Character) obj;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0) {
                return null;
            }
            if (str.length() == 1) {
                return Character.valueOf(str.charAt(0));
            }
            throw new JSONException("can not cast to char, value : " + obj);
        }
        throw new JSONException("can not cast to char, value : " + obj);
    }

    public static Short castToShort(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof BigDecimal) {
            return Short.valueOf(shortValue((BigDecimal) obj));
        }
        if (obj instanceof Number) {
            return Short.valueOf(((Number) obj).shortValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                return null;
            }
            return Short.valueOf(Short.parseShort(str));
        } else if (obj instanceof Boolean) {
            return Short.valueOf(((Boolean) obj).booleanValue() ? (short) 1 : 0);
        } else {
            throw new JSONException("can not cast to short, value : " + obj);
        }
    }

    public static BigDecimal castToBigDecimal(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Float) {
            Float f = (Float) obj;
            if (Float.isNaN(f.floatValue()) || Float.isInfinite(f.floatValue())) {
                return null;
            }
        } else if (obj instanceof Double) {
            Double d = (Double) obj;
            if (Double.isNaN(d.doubleValue()) || Double.isInfinite(d.doubleValue())) {
                return null;
            }
        } else if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        } else {
            if (obj instanceof BigInteger) {
                return new BigDecimal((BigInteger) obj);
            }
            if ((obj instanceof Map) && ((Map) obj).size() == 0) {
                return null;
            }
        }
        String obj2 = obj.toString();
        if (obj2.length() == 0 || obj2.equalsIgnoreCase("null")) {
            return null;
        }
        if (obj2.length() <= 65535) {
            return new BigDecimal(obj2);
        }
        throw new JSONException("decimal overflow");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0059, code lost:
        r1 = (java.math.BigDecimal) r4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.math.BigInteger castToBigInteger(java.lang.Object r4) {
        /*
            r0 = 0
            if (r4 != 0) goto L_0x0004
            return r0
        L_0x0004:
            boolean r1 = r4 instanceof java.lang.Float
            if (r1 == 0) goto L_0x0029
            java.lang.Float r4 = (java.lang.Float) r4
            float r1 = r4.floatValue()
            boolean r1 = java.lang.Float.isNaN(r1)
            if (r1 != 0) goto L_0x0028
            float r1 = r4.floatValue()
            boolean r1 = java.lang.Float.isInfinite(r1)
            if (r1 == 0) goto L_0x001f
            goto L_0x0028
        L_0x001f:
            long r0 = r4.longValue()
            java.math.BigInteger r4 = java.math.BigInteger.valueOf(r0)
            return r4
        L_0x0028:
            return r0
        L_0x0029:
            boolean r1 = r4 instanceof java.lang.Double
            if (r1 == 0) goto L_0x004e
            java.lang.Double r4 = (java.lang.Double) r4
            double r1 = r4.doubleValue()
            boolean r1 = java.lang.Double.isNaN(r1)
            if (r1 != 0) goto L_0x004d
            double r1 = r4.doubleValue()
            boolean r1 = java.lang.Double.isInfinite(r1)
            if (r1 == 0) goto L_0x0044
            goto L_0x004d
        L_0x0044:
            long r0 = r4.longValue()
            java.math.BigInteger r4 = java.math.BigInteger.valueOf(r0)
            return r4
        L_0x004d:
            return r0
        L_0x004e:
            boolean r1 = r4 instanceof java.math.BigInteger
            if (r1 == 0) goto L_0x0055
            java.math.BigInteger r4 = (java.math.BigInteger) r4
            return r4
        L_0x0055:
            boolean r1 = r4 instanceof java.math.BigDecimal
            if (r1 == 0) goto L_0x006d
            r1 = r4
            java.math.BigDecimal r1 = (java.math.BigDecimal) r1
            int r2 = r1.scale()
            r3 = -1000(0xfffffffffffffc18, float:NaN)
            if (r2 <= r3) goto L_0x006d
            r3 = 1000(0x3e8, float:1.401E-42)
            if (r2 >= r3) goto L_0x006d
            java.math.BigInteger r4 = r1.toBigInteger()
            return r4
        L_0x006d:
            java.lang.String r4 = r4.toString()
            int r1 = r4.length()
            if (r1 == 0) goto L_0x0097
            java.lang.String r1 = "null"
            boolean r1 = r4.equalsIgnoreCase(r1)
            if (r1 == 0) goto L_0x0080
            goto L_0x0097
        L_0x0080:
            int r0 = r4.length()
            r1 = 65535(0xffff, float:9.1834E-41)
            if (r0 > r1) goto L_0x008f
            java.math.BigInteger r0 = new java.math.BigInteger
            r0.<init>(r4)
            return r0
        L_0x008f:
            com.alibaba.fastjson.JSONException r4 = new com.alibaba.fastjson.JSONException
            java.lang.String r0 = "decimal overflow"
            r4.<init>(r0)
            throw r4
        L_0x0097:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.TypeUtils.castToBigInteger(java.lang.Object):java.math.BigInteger");
    }

    public static Float castToFloat(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return Float.valueOf(((Number) obj).floatValue());
        }
        if (obj instanceof String) {
            String obj2 = obj.toString();
            if (obj2.length() == 0 || "null".equals(obj2) || "NULL".equals(obj2)) {
                return null;
            }
            if (obj2.indexOf(44) != -1) {
                obj2 = obj2.replaceAll(",", "");
            }
            return Float.valueOf(Float.parseFloat(obj2));
        } else if (obj instanceof Boolean) {
            return Float.valueOf(((Boolean) obj).booleanValue() ? 1.0f : 0.0f);
        } else {
            throw new JSONException("can not cast to float, value : " + obj);
        }
    }

    public static Double castToDouble(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return Double.valueOf(((Number) obj).doubleValue());
        }
        if (obj instanceof String) {
            String obj2 = obj.toString();
            if (obj2.length() == 0 || "null".equals(obj2) || "NULL".equals(obj2)) {
                return null;
            }
            if (obj2.indexOf(44) != -1) {
                obj2 = obj2.replaceAll(",", "");
            }
            return Double.valueOf(Double.parseDouble(obj2));
        } else if (obj instanceof Boolean) {
            return Double.valueOf(((Boolean) obj).booleanValue() ? 1.0d : 0.0d);
        } else {
            throw new JSONException("can not cast to double, value : " + obj);
        }
    }

    public static java.util.Date castToDate(Object obj) {
        return castToDate(obj, (String) null);
    }

    public static java.util.Date castToDate(Object obj, String str) {
        long j;
        if (obj == null) {
            return null;
        }
        if (obj instanceof java.util.Date) {
            return (java.util.Date) obj;
        }
        if (obj instanceof Calendar) {
            return ((Calendar) obj).getTime();
        }
        if (obj instanceof BigDecimal) {
            return new java.util.Date(longValue((BigDecimal) obj));
        }
        if (obj instanceof Number) {
            long longValue = ((Number) obj).longValue();
            if ("unixtime".equals(str)) {
                longValue *= 1000;
            }
            return new java.util.Date(longValue);
        }
        if (obj instanceof String) {
            String str2 = (String) obj;
            JSONScanner jSONScanner = new JSONScanner(str2);
            try {
                if (jSONScanner.scanISO8601DateIfMatch(false)) {
                    return jSONScanner.getCalendar().getTime();
                }
                jSONScanner.close();
                if (str2.startsWith("/Date(") && str2.endsWith(")/")) {
                    str2 = str2.substring(6, str2.length() - 2);
                }
                if (str2.indexOf(45) > 0 || str2.indexOf(43) > 0 || str != null) {
                    if (str == null) {
                        int length = str2.length();
                        if (length == JSON.DEFFAULT_DATE_FORMAT.length() || (length == 22 && JSON.DEFFAULT_DATE_FORMAT.equals("yyyyMMddHHmmssSSSZ"))) {
                            str = JSON.DEFFAULT_DATE_FORMAT;
                        } else {
                            str = length == 10 ? "yyyy-MM-dd" : length == 19 ? "yyyy-MM-dd HH:mm:ss" : (length == 29 && str2.charAt(26) == ':' && str2.charAt(28) == '0') ? "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" : (length == 23 && str2.charAt(19) == ',') ? "yyyy-MM-dd HH:mm:ss,SSS" : "yyyy-MM-dd HH:mm:ss.SSS";
                        }
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str, JSON.defaultLocale);
                    simpleDateFormat.setTimeZone(JSON.defaultTimeZone);
                    try {
                        return simpleDateFormat.parse(str2);
                    } catch (ParseException unused) {
                        throw new JSONException("can not cast to Date, value : " + str2);
                    }
                } else if (str2.length() == 0) {
                    return null;
                } else {
                    j = Long.parseLong(str2);
                }
            } finally {
                jSONScanner.close();
            }
        } else {
            j = -1;
        }
        if (j != -1) {
            return new java.util.Date(j);
        }
        Class<?> cls = obj.getClass();
        if ("oracle.sql.TIMESTAMP".equals(cls.getName())) {
            if (oracleTimestampMethod == null && !oracleTimestampMethodInited) {
                try {
                    oracleTimestampMethod = cls.getMethod("toJdbc", new Class[0]);
                } catch (NoSuchMethodException unused2) {
                } catch (Throwable th) {
                    oracleTimestampMethodInited = true;
                    throw th;
                }
                oracleTimestampMethodInited = true;
            }
            try {
                return (java.util.Date) oracleTimestampMethod.invoke(obj, new Object[0]);
            } catch (Exception e) {
                throw new JSONException("can not cast oracle.sql.TIMESTAMP to Date", e);
            }
        } else if ("oracle.sql.DATE".equals(cls.getName())) {
            if (oracleDateMethod == null && !oracleDateMethodInited) {
                try {
                    oracleDateMethod = cls.getMethod("toJdbc", new Class[0]);
                } catch (NoSuchMethodException unused3) {
                } catch (Throwable th2) {
                    oracleDateMethodInited = true;
                    throw th2;
                }
                oracleDateMethodInited = true;
            }
            try {
                return (java.util.Date) oracleDateMethod.invoke(obj, new Object[0]);
            } catch (Exception e2) {
                throw new JSONException("can not cast oracle.sql.DATE to Date", e2);
            }
        } else {
            throw new JSONException("can not cast to Date, value : " + obj);
        }
    }

    public static Object castToSqlDate(Object obj) {
        return ModuleUtil.callWhenHasJavaSql(castToSqlDateFunction, obj);
    }

    public static long longExtractValue(Number number) {
        if (number instanceof BigDecimal) {
            return ((BigDecimal) number).longValueExact();
        }
        return number.longValue();
    }

    public static Object castToSqlTime(Object obj) {
        return ModuleUtil.callWhenHasJavaSql(castToSqlTimeFunction, obj);
    }

    public static Object castToTimestamp(Object obj) {
        return ModuleUtil.callWhenHasJavaSql(castToTimestampFunction, obj);
    }

    public static boolean isNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt == '+' || charAt == '-') {
                if (i != 0) {
                    return false;
                }
            } else if (charAt < '0' || charAt > '9') {
                return false;
            }
        }
        return true;
    }

    public static Long castToLong(Object obj) {
        Calendar calendar = null;
        if (obj == null) {
            return null;
        }
        if (obj instanceof BigDecimal) {
            return Long.valueOf(longValue((BigDecimal) obj));
        }
        if (obj instanceof Number) {
            return Long.valueOf(((Number) obj).longValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                return null;
            }
            if (str.indexOf(44) != -1) {
                str = str.replaceAll(",", "");
            }
            try {
                return Long.valueOf(Long.parseLong(str));
            } catch (NumberFormatException unused) {
                JSONScanner jSONScanner = new JSONScanner(str);
                if (jSONScanner.scanISO8601DateIfMatch(false)) {
                    calendar = jSONScanner.getCalendar();
                }
                jSONScanner.close();
                if (calendar != null) {
                    return Long.valueOf(calendar.getTimeInMillis());
                }
            }
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            if (map.size() == 2 && map.containsKey("andIncrement") && map.containsKey("andDecrement")) {
                Iterator it = map.values().iterator();
                it.next();
                return castToLong(it.next());
            }
        }
        if (obj instanceof Boolean) {
            return Long.valueOf(((Boolean) obj).booleanValue() ? 1 : 0);
        }
        throw new JSONException("can not cast to long, value : " + obj);
    }

    public static byte byteValue(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return 0;
        }
        int scale = bigDecimal.scale();
        if (scale < -100 || scale > 100) {
            return bigDecimal.byteValueExact();
        }
        return bigDecimal.byteValue();
    }

    public static short shortValue(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return 0;
        }
        int scale = bigDecimal.scale();
        if (scale < -100 || scale > 100) {
            return bigDecimal.shortValueExact();
        }
        return bigDecimal.shortValue();
    }

    public static int intValue(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return 0;
        }
        int scale = bigDecimal.scale();
        if (scale < -100 || scale > 100) {
            return bigDecimal.intValueExact();
        }
        return bigDecimal.intValue();
    }

    public static long longValue(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return 0;
        }
        int scale = bigDecimal.scale();
        if (scale < -100 || scale > 100) {
            return bigDecimal.longValueExact();
        }
        return bigDecimal.longValue();
    }

    public static Integer castToInt(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        if (obj instanceof BigDecimal) {
            return Integer.valueOf(intValue((BigDecimal) obj));
        }
        if (obj instanceof Number) {
            return Integer.valueOf(((Number) obj).intValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                return null;
            }
            if (str.indexOf(44) != -1) {
                str = str.replaceAll(",", "");
            }
            Matcher matcher = NUMBER_WITH_TRAILING_ZEROS_PATTERN.matcher(str);
            if (matcher.find()) {
                str = matcher.replaceAll("");
            }
            return Integer.valueOf(Integer.parseInt(str));
        } else if (obj instanceof Boolean) {
            return Integer.valueOf(((Boolean) obj).booleanValue() ? 1 : 0);
        } else {
            if (obj instanceof Map) {
                Map map = (Map) obj;
                if (map.size() == 2 && map.containsKey("andIncrement") && map.containsKey("andDecrement")) {
                    Iterator it = map.values().iterator();
                    it.next();
                    return castToInt(it.next());
                }
            }
            throw new JSONException("can not cast to int, value : " + obj);
        }
    }

    public static byte[] castToBytes(Object obj) {
        if (obj instanceof byte[]) {
            return (byte[]) obj;
        }
        if (obj instanceof String) {
            return IOUtils.decodeBase64((String) obj);
        }
        throw new JSONException("can not cast to byte[], value : " + obj);
    }

    public static Boolean castToBoolean(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        boolean z = false;
        if (obj instanceof BigDecimal) {
            if (intValue((BigDecimal) obj) == 1) {
                z = true;
            }
            return Boolean.valueOf(z);
        } else if (obj instanceof Number) {
            if (((Number) obj).intValue() == 1) {
                z = true;
            }
            return Boolean.valueOf(z);
        } else {
            if (obj instanceof String) {
                String str = (String) obj;
                if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                    return null;
                }
                if (AbsoluteConst.TRUE.equalsIgnoreCase(str) || "1".equals(str)) {
                    return Boolean.TRUE;
                }
                if (AbsoluteConst.FALSE.equalsIgnoreCase(str) || WXInstanceApm.VALUE_ERROR_CODE_DEFAULT.equals(str)) {
                    return Boolean.FALSE;
                }
                if ("Y".equalsIgnoreCase(str) || ExifInterface.GPS_DIRECTION_TRUE.equals(str)) {
                    return Boolean.TRUE;
                }
                if ("F".equalsIgnoreCase(str) || "N".equals(str)) {
                    return Boolean.FALSE;
                }
            }
            throw new JSONException("can not cast to boolean, value : " + obj);
        }
    }

    public static <T> T castToJavaBean(Object obj, Class<T> cls) {
        return cast(obj, cls, ParserConfig.getGlobalInstance());
    }

    public static <T> T cast(Object obj, Class<T> cls, ParserConfig parserConfig) {
        T t;
        int i = 0;
        if (obj == null) {
            if (cls == Integer.TYPE) {
                return 0;
            }
            if (cls == Long.TYPE) {
                return 0L;
            }
            if (cls == Short.TYPE) {
                return (short) 0;
            }
            if (cls == Byte.TYPE) {
                return (byte) 0;
            }
            if (cls == Float.TYPE) {
                return Float.valueOf(0.0f);
            }
            if (cls == Double.TYPE) {
                return Double.valueOf(0.0d);
            }
            if (cls == Boolean.TYPE) {
                return Boolean.FALSE;
            }
            return null;
        } else if (cls == null) {
            throw new IllegalArgumentException("clazz is null");
        } else if (cls == obj.getClass()) {
            return obj;
        } else {
            if (!(obj instanceof Map)) {
                if (cls.isArray()) {
                    if (obj instanceof Collection) {
                        Collection<Object> collection = (Collection) obj;
                        T newInstance = Array.newInstance(cls.getComponentType(), collection.size());
                        for (Object cast : collection) {
                            Array.set(newInstance, i, cast(cast, cls.getComponentType(), parserConfig));
                            i++;
                        }
                        return newInstance;
                    } else if (cls == byte[].class) {
                        return castToBytes(obj);
                    }
                }
                if (cls.isAssignableFrom(obj.getClass())) {
                    return obj;
                }
                if (cls == Boolean.TYPE || cls == Boolean.class) {
                    return castToBoolean(obj);
                }
                if (cls == Byte.TYPE || cls == Byte.class) {
                    return castToByte(obj);
                }
                if (cls == Character.TYPE || cls == Character.class) {
                    return castToChar(obj);
                }
                if (cls == Short.TYPE || cls == Short.class) {
                    return castToShort(obj);
                }
                if (cls == Integer.TYPE || cls == Integer.class) {
                    return castToInt(obj);
                }
                if (cls == Long.TYPE || cls == Long.class) {
                    return castToLong(obj);
                }
                if (cls == Float.TYPE || cls == Float.class) {
                    return castToFloat(obj);
                }
                if (cls == Double.TYPE || cls == Double.class) {
                    return castToDouble(obj);
                }
                if (cls == String.class) {
                    return castToString(obj);
                }
                if (cls == BigDecimal.class) {
                    return castToBigDecimal(obj);
                }
                if (cls == BigInteger.class) {
                    return castToBigInteger(obj);
                }
                if (cls == java.util.Date.class) {
                    return castToDate(obj);
                }
                T callWhenHasJavaSql = ModuleUtil.callWhenHasJavaSql(castFunction, obj, cls);
                if (callWhenHasJavaSql != null) {
                    return callWhenHasJavaSql;
                }
                if (cls.isEnum()) {
                    return castToEnum(obj, cls, parserConfig);
                }
                if (Calendar.class.isAssignableFrom(cls)) {
                    java.util.Date castToDate = castToDate(obj);
                    if (cls == Calendar.class) {
                        t = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
                    } else {
                        try {
                            t = (Calendar) cls.newInstance();
                        } catch (Exception e) {
                            throw new JSONException("can not cast to : " + cls.getName(), e);
                        }
                    }
                    t.setTime(castToDate);
                    return t;
                }
                String name = cls.getName();
                if (name.equals("javax.xml.datatype.XMLGregorianCalendar")) {
                    java.util.Date castToDate2 = castToDate(obj);
                    Calendar instance = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
                    instance.setTime(castToDate2);
                    return CalendarCodec.instance.createXMLGregorianCalendar(instance);
                }
                if (obj instanceof String) {
                    String str = (String) obj;
                    if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                        return null;
                    }
                    if (cls == Currency.class) {
                        return Currency.getInstance(str);
                    }
                    if (cls == Locale.class) {
                        return toLocale(str);
                    }
                    if (name.startsWith("java.time.")) {
                        return JSON.parseObject(JSON.toJSONString(str), cls);
                    }
                }
                if (parserConfig.get(cls) != null) {
                    return JSON.parseObject(JSON.toJSONString(obj), cls);
                }
                throw new JSONException("can not cast to : " + cls.getName());
            } else if (cls == Map.class) {
                return obj;
            } else {
                Map map = (Map) obj;
                if (cls != Object.class || map.containsKey(JSON.DEFAULT_TYPE_KEY)) {
                    return castToJavaBean(map, cls, parserConfig);
                }
                return obj;
            }
        }
    }

    public static Locale toLocale(String str) {
        String[] split = str.split("_");
        if (split.length == 1) {
            return new Locale(split[0]);
        }
        if (split.length == 2) {
            return new Locale(split[0], split[1]);
        }
        return new Locale(split[0], split[1], split[2]);
    }

    public static <T> T castToEnum(Object obj, Class<T> cls, ParserConfig parserConfig) {
        try {
            if (obj instanceof String) {
                String str = (String) obj;
                if (str.length() == 0) {
                    return null;
                }
                if (parserConfig == null) {
                    parserConfig = ParserConfig.getGlobalInstance();
                }
                ObjectDeserializer deserializer = parserConfig.getDeserializer((Type) cls);
                if (deserializer instanceof EnumDeserializer) {
                    return ((EnumDeserializer) deserializer).getEnumByHashCode(fnv1a_64(str));
                }
                return Enum.valueOf(cls, str);
            }
            if (obj instanceof BigDecimal) {
                int intValue = intValue((BigDecimal) obj);
                T[] enumConstants = cls.getEnumConstants();
                if (intValue < enumConstants.length) {
                    return enumConstants[intValue];
                }
            }
            if (obj instanceof Number) {
                int intValue2 = ((Number) obj).intValue();
                T[] enumConstants2 = cls.getEnumConstants();
                if (intValue2 < enumConstants2.length) {
                    return enumConstants2[intValue2];
                }
            }
            throw new JSONException("can not cast to : " + cls.getName());
        } catch (Exception e) {
            throw new JSONException("can not cast to : " + cls.getName(), e);
        }
    }

    public static <T> T cast(Object obj, Type type, ParserConfig parserConfig) {
        if (obj == null) {
            return null;
        }
        if (type instanceof Class) {
            return cast(obj, (Class) type, parserConfig);
        }
        if (type instanceof ParameterizedType) {
            return cast(obj, (ParameterizedType) type, parserConfig);
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                return null;
            }
        }
        if (type instanceof TypeVariable) {
            return obj;
        }
        throw new JSONException("can not cast to : " + type);
    }

    public static <T> T cast(Object obj, ParameterizedType parameterizedType, ParserConfig parserConfig) {
        T t;
        Object obj2;
        Object obj3;
        Type rawType = parameterizedType.getRawType();
        if (rawType == List.class || rawType == ArrayList.class) {
            Type type = parameterizedType.getActualTypeArguments()[0];
            if (obj instanceof List) {
                List list = (List) obj;
                T arrayList = new ArrayList(list.size());
                for (Object next : list) {
                    if (!(type instanceof Class)) {
                        obj3 = cast(next, type, parserConfig);
                    } else if (next == null || next.getClass() != JSONObject.class) {
                        obj3 = cast(next, (Class) type, parserConfig);
                    } else {
                        obj3 = ((JSONObject) next).toJavaObject((Class) type, parserConfig, 0);
                    }
                    arrayList.add(obj3);
                }
                return arrayList;
            }
        }
        if (rawType == Set.class || rawType == HashSet.class || rawType == TreeSet.class || rawType == Collection.class || rawType == List.class || rawType == ArrayList.class) {
            Type type2 = parameterizedType.getActualTypeArguments()[0];
            if (obj instanceof Iterable) {
                if (rawType == Set.class || rawType == HashSet.class) {
                    t = new HashSet();
                } else if (rawType == TreeSet.class) {
                    t = new TreeSet();
                } else {
                    t = new ArrayList();
                }
                for (Object next2 : (Iterable) obj) {
                    if (!(type2 instanceof Class)) {
                        obj2 = cast(next2, type2, parserConfig);
                    } else if (next2 == null || next2.getClass() != JSONObject.class) {
                        obj2 = cast(next2, (Class) type2, parserConfig);
                    } else {
                        obj2 = ((JSONObject) next2).toJavaObject((Class) type2, parserConfig, 0);
                    }
                    t.add(obj2);
                }
                return t;
            }
        }
        if (rawType == Map.class || rawType == HashMap.class) {
            Type type3 = parameterizedType.getActualTypeArguments()[0];
            Type type4 = parameterizedType.getActualTypeArguments()[1];
            if (obj instanceof Map) {
                T hashMap = new HashMap();
                for (Map.Entry entry : ((Map) obj).entrySet()) {
                    hashMap.put(cast(entry.getKey(), type3, parserConfig), cast(entry.getValue(), type4, parserConfig));
                }
                return hashMap;
            }
        }
        if ((obj instanceof String) && ((String) obj).length() == 0) {
            return null;
        }
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (actualTypeArguments.length == 1 && (parameterizedType.getActualTypeArguments()[0] instanceof WildcardType)) {
            return cast(obj, rawType, parserConfig);
        }
        if (rawType == Map.Entry.class && (obj instanceof Map)) {
            Map map = (Map) obj;
            if (map.size() == 1) {
                T t2 = (Map.Entry) map.entrySet().iterator().next();
                Object value = t2.getValue();
                if (actualTypeArguments.length == 2 && (value instanceof Map)) {
                    t2.setValue(cast(value, actualTypeArguments[1], parserConfig));
                }
                return t2;
            }
        }
        if (rawType instanceof Class) {
            if (parserConfig == null) {
                parserConfig = ParserConfig.global;
            }
            ObjectDeserializer deserializer = parserConfig.getDeserializer(rawType);
            if (deserializer != null) {
                return deserializer.deserialze(new DefaultJSONParser(JSON.toJSONString(obj), parserConfig), parameterizedType, (Object) null);
            }
        }
        throw new JSONException("can not cast to : " + parameterizedType);
    }

    /* JADX WARNING: type inference failed for: r0v10, types: [com.alibaba.fastjson.parser.deserializer.ObjectDeserializer] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> T castToJavaBean(java.util.Map<java.lang.String, java.lang.Object> r4, java.lang.Class<T> r5, com.alibaba.fastjson.parser.ParserConfig r6) {
        /*
            java.lang.Class<java.lang.StackTraceElement> r0 = java.lang.StackTraceElement.class
            r1 = 0
            if (r5 != r0) goto L_0x003d
            java.lang.String r5 = "className"
            java.lang.Object r5 = r4.get(r5)     // Catch:{ Exception -> 0x0148 }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ Exception -> 0x0148 }
            java.lang.String r6 = "methodName"
            java.lang.Object r6 = r4.get(r6)     // Catch:{ Exception -> 0x0148 }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ Exception -> 0x0148 }
            java.lang.String r0 = "fileName"
            java.lang.Object r0 = r4.get(r0)     // Catch:{ Exception -> 0x0148 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x0148 }
            java.lang.String r2 = "lineNumber"
            java.lang.Object r4 = r4.get(r2)     // Catch:{ Exception -> 0x0148 }
            java.lang.Number r4 = (java.lang.Number) r4     // Catch:{ Exception -> 0x0148 }
            if (r4 != 0) goto L_0x0028
            goto L_0x0037
        L_0x0028:
            boolean r1 = r4 instanceof java.math.BigDecimal     // Catch:{ Exception -> 0x0148 }
            if (r1 == 0) goto L_0x0033
            java.math.BigDecimal r4 = (java.math.BigDecimal) r4     // Catch:{ Exception -> 0x0148 }
            int r1 = r4.intValueExact()     // Catch:{ Exception -> 0x0148 }
            goto L_0x0037
        L_0x0033:
            int r1 = r4.intValue()     // Catch:{ Exception -> 0x0148 }
        L_0x0037:
            java.lang.StackTraceElement r4 = new java.lang.StackTraceElement     // Catch:{ Exception -> 0x0148 }
            r4.<init>(r5, r6, r0, r1)     // Catch:{ Exception -> 0x0148 }
            return r4
        L_0x003d:
            java.lang.String r0 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY     // Catch:{ Exception -> 0x0148 }
            java.lang.Object r0 = r4.get(r0)     // Catch:{ Exception -> 0x0148 }
            boolean r2 = r0 instanceof java.lang.String     // Catch:{ Exception -> 0x0148 }
            r3 = 0
            if (r2 == 0) goto L_0x0076
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x0148 }
            if (r6 != 0) goto L_0x004e
            com.alibaba.fastjson.parser.ParserConfig r6 = com.alibaba.fastjson.parser.ParserConfig.global     // Catch:{ Exception -> 0x0148 }
        L_0x004e:
            java.lang.Class r2 = r6.checkAutoType(r0, r3)     // Catch:{ Exception -> 0x0148 }
            if (r2 == 0) goto L_0x005f
            boolean r0 = r2.equals(r5)     // Catch:{ Exception -> 0x0148 }
            if (r0 != 0) goto L_0x0076
            java.lang.Object r4 = castToJavaBean(r4, r2, r6)     // Catch:{ Exception -> 0x0148 }
            return r4
        L_0x005f:
            java.lang.ClassNotFoundException r4 = new java.lang.ClassNotFoundException     // Catch:{ Exception -> 0x0148 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0148 }
            r5.<init>()     // Catch:{ Exception -> 0x0148 }
            r5.append(r0)     // Catch:{ Exception -> 0x0148 }
            java.lang.String r6 = " not found"
            r5.append(r6)     // Catch:{ Exception -> 0x0148 }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x0148 }
            r4.<init>(r5)     // Catch:{ Exception -> 0x0148 }
            throw r4     // Catch:{ Exception -> 0x0148 }
        L_0x0076:
            boolean r0 = r5.isInterface()     // Catch:{ Exception -> 0x0148 }
            if (r0 == 0) goto L_0x00b0
            boolean r0 = r4 instanceof com.alibaba.fastjson.JSONObject     // Catch:{ Exception -> 0x0148 }
            if (r0 == 0) goto L_0x0083
            com.alibaba.fastjson.JSONObject r4 = (com.alibaba.fastjson.JSONObject) r4     // Catch:{ Exception -> 0x0148 }
            goto L_0x0089
        L_0x0083:
            com.alibaba.fastjson.JSONObject r0 = new com.alibaba.fastjson.JSONObject     // Catch:{ Exception -> 0x0148 }
            r0.<init>((java.util.Map<java.lang.String, java.lang.Object>) r4)     // Catch:{ Exception -> 0x0148 }
            r4 = r0
        L_0x0089:
            if (r6 != 0) goto L_0x008f
            com.alibaba.fastjson.parser.ParserConfig r6 = com.alibaba.fastjson.parser.ParserConfig.getGlobalInstance()     // Catch:{ Exception -> 0x0148 }
        L_0x008f:
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r6 = r6.get(r5)     // Catch:{ Exception -> 0x0148 }
            if (r6 == 0) goto L_0x009e
            java.lang.String r4 = com.alibaba.fastjson.JSON.toJSONString(r4)     // Catch:{ Exception -> 0x0148 }
            java.lang.Object r4 = com.alibaba.fastjson.JSON.parseObject((java.lang.String) r4, r5)     // Catch:{ Exception -> 0x0148 }
            return r4
        L_0x009e:
            java.lang.Thread r6 = java.lang.Thread.currentThread()     // Catch:{ Exception -> 0x0148 }
            java.lang.ClassLoader r6 = r6.getContextClassLoader()     // Catch:{ Exception -> 0x0148 }
            r0 = 1
            java.lang.Class[] r0 = new java.lang.Class[r0]     // Catch:{ Exception -> 0x0148 }
            r0[r1] = r5     // Catch:{ Exception -> 0x0148 }
            java.lang.Object r4 = java.lang.reflect.Proxy.newProxyInstance(r6, r0, r4)     // Catch:{ Exception -> 0x0148 }
            return r4
        L_0x00b0:
            java.lang.Class<java.util.Locale> r0 = java.util.Locale.class
            if (r5 != r0) goto L_0x00da
            java.lang.String r0 = "language"
            java.lang.Object r0 = r4.get(r0)     // Catch:{ Exception -> 0x0148 }
            java.lang.String r1 = "country"
            java.lang.Object r1 = r4.get(r1)     // Catch:{ Exception -> 0x0148 }
            boolean r2 = r0 instanceof java.lang.String     // Catch:{ Exception -> 0x0148 }
            if (r2 == 0) goto L_0x00da
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x0148 }
            boolean r2 = r1 instanceof java.lang.String     // Catch:{ Exception -> 0x0148 }
            if (r2 == 0) goto L_0x00d2
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ Exception -> 0x0148 }
            java.util.Locale r4 = new java.util.Locale     // Catch:{ Exception -> 0x0148 }
            r4.<init>(r0, r1)     // Catch:{ Exception -> 0x0148 }
            return r4
        L_0x00d2:
            if (r1 != 0) goto L_0x00da
            java.util.Locale r4 = new java.util.Locale     // Catch:{ Exception -> 0x0148 }
            r4.<init>(r0)     // Catch:{ Exception -> 0x0148 }
            return r4
        L_0x00da:
            java.lang.Class<java.lang.String> r0 = java.lang.String.class
            if (r5 != r0) goto L_0x00e7
            boolean r0 = r4 instanceof com.alibaba.fastjson.JSONObject     // Catch:{ Exception -> 0x0148 }
            if (r0 == 0) goto L_0x00e7
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x0148 }
            return r4
        L_0x00e7:
            java.lang.Class<com.alibaba.fastjson.JSON> r0 = com.alibaba.fastjson.JSON.class
            if (r5 != r0) goto L_0x00f0
            boolean r0 = r4 instanceof com.alibaba.fastjson.JSONObject     // Catch:{ Exception -> 0x0148 }
            if (r0 == 0) goto L_0x00f0
            return r4
        L_0x00f0:
            java.lang.Class<java.util.LinkedHashMap> r0 = java.util.LinkedHashMap.class
            if (r5 != r0) goto L_0x0104
            boolean r0 = r4 instanceof com.alibaba.fastjson.JSONObject     // Catch:{ Exception -> 0x0148 }
            if (r0 == 0) goto L_0x0104
            r0 = r4
            com.alibaba.fastjson.JSONObject r0 = (com.alibaba.fastjson.JSONObject) r0     // Catch:{ Exception -> 0x0148 }
            java.util.Map r0 = r0.getInnerMap()     // Catch:{ Exception -> 0x0148 }
            boolean r1 = r0 instanceof java.util.LinkedHashMap     // Catch:{ Exception -> 0x0148 }
            if (r1 == 0) goto L_0x0104
            return r0
        L_0x0104:
            boolean r0 = r5.isInstance(r4)     // Catch:{ Exception -> 0x0148 }
            if (r0 == 0) goto L_0x010b
            return r4
        L_0x010b:
            java.lang.Class<com.alibaba.fastjson.JSONObject> r0 = com.alibaba.fastjson.JSONObject.class
            if (r5 != r0) goto L_0x0115
            com.alibaba.fastjson.JSONObject r5 = new com.alibaba.fastjson.JSONObject     // Catch:{ Exception -> 0x0148 }
            r5.<init>((java.util.Map<java.lang.String, java.lang.Object>) r4)     // Catch:{ Exception -> 0x0148 }
            return r5
        L_0x0115:
            if (r6 != 0) goto L_0x011b
            com.alibaba.fastjson.parser.ParserConfig r6 = com.alibaba.fastjson.parser.ParserConfig.getGlobalInstance()     // Catch:{ Exception -> 0x0148 }
        L_0x011b:
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r0 = r6.getDeserializer((java.lang.reflect.Type) r5)     // Catch:{ Exception -> 0x0148 }
            boolean r1 = r0 instanceof com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer     // Catch:{ Exception -> 0x0148 }
            if (r1 == 0) goto L_0x0126
            r3 = r0
            com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer r3 = (com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) r3     // Catch:{ Exception -> 0x0148 }
        L_0x0126:
            if (r3 == 0) goto L_0x012d
            java.lang.Object r4 = r3.createInstance((java.util.Map<java.lang.String, java.lang.Object>) r4, (com.alibaba.fastjson.parser.ParserConfig) r6)     // Catch:{ Exception -> 0x0148 }
            return r4
        L_0x012d:
            com.alibaba.fastjson.JSONException r4 = new com.alibaba.fastjson.JSONException     // Catch:{ Exception -> 0x0148 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0148 }
            r6.<init>()     // Catch:{ Exception -> 0x0148 }
            java.lang.String r0 = "can not get javaBeanDeserializer. "
            r6.append(r0)     // Catch:{ Exception -> 0x0148 }
            java.lang.String r5 = r5.getName()     // Catch:{ Exception -> 0x0148 }
            r6.append(r5)     // Catch:{ Exception -> 0x0148 }
            java.lang.String r5 = r6.toString()     // Catch:{ Exception -> 0x0148 }
            r4.<init>(r5)     // Catch:{ Exception -> 0x0148 }
            throw r4     // Catch:{ Exception -> 0x0148 }
        L_0x0148:
            r4 = move-exception
            com.alibaba.fastjson.JSONException r5 = new com.alibaba.fastjson.JSONException
            java.lang.String r6 = r4.getMessage()
            r5.<init>(r6, r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.TypeUtils.castToJavaBean(java.util.Map, java.lang.Class, com.alibaba.fastjson.parser.ParserConfig):java.lang.Object");
    }

    private static void addBaseClassMappings() {
        Class<char[]> cls = char[].class;
        Class<boolean[]> cls2 = boolean[].class;
        Class<double[]> cls3 = double[].class;
        Class<float[]> cls4 = float[].class;
        Class<long[]> cls5 = long[].class;
        Class<int[]> cls6 = int[].class;
        Class<short[]> cls7 = short[].class;
        Class<byte[]> cls8 = byte[].class;
        mappings.put("byte", Byte.TYPE);
        mappings.put("short", Short.TYPE);
        mappings.put("int", Integer.TYPE);
        mappings.put("long", Long.TYPE);
        mappings.put("float", Float.TYPE);
        mappings.put("double", Double.TYPE);
        mappings.put("boolean", Boolean.TYPE);
        mappings.put("char", Character.TYPE);
        mappings.put("[byte", cls8);
        mappings.put("[short", cls7);
        mappings.put("[int", cls6);
        mappings.put("[long", cls5);
        mappings.put("[float", cls4);
        mappings.put("[double", cls3);
        mappings.put("[boolean", cls2);
        mappings.put("[char", cls);
        mappings.put("[B", cls8);
        mappings.put("[S", cls7);
        mappings.put("[I", cls6);
        mappings.put("[J", cls5);
        mappings.put("[F", cls4);
        mappings.put("[D", cls3);
        mappings.put("[C", cls);
        mappings.put("[Z", cls2);
        Class[] clsArr = {Object.class, Cloneable.class, loadClass("java.lang.AutoCloseable"), Exception.class, RuntimeException.class, IllegalAccessError.class, IllegalAccessException.class, IllegalArgumentException.class, IllegalMonitorStateException.class, IllegalStateException.class, IllegalThreadStateException.class, IndexOutOfBoundsException.class, InstantiationError.class, InstantiationException.class, InternalError.class, InterruptedException.class, LinkageError.class, NegativeArraySizeException.class, NoClassDefFoundError.class, NoSuchFieldError.class, NoSuchFieldException.class, NoSuchMethodError.class, NoSuchMethodException.class, NullPointerException.class, NumberFormatException.class, OutOfMemoryError.class, SecurityException.class, StackOverflowError.class, StringIndexOutOfBoundsException.class, TypeNotPresentException.class, VerifyError.class, StackTraceElement.class, HashMap.class, LinkedHashMap.class, Hashtable.class, TreeMap.class, IdentityHashMap.class, WeakHashMap.class, LinkedHashMap.class, HashSet.class, LinkedHashSet.class, TreeSet.class, ArrayList.class, TimeUnit.class, ConcurrentHashMap.class, AtomicInteger.class, AtomicLong.class, Collections.EMPTY_MAP.getClass(), Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Number.class, String.class, BigDecimal.class, BigInteger.class, BitSet.class, Calendar.class, java.util.Date.class, Locale.class, UUID.class, SimpleDateFormat.class, JSONObject.class, JSONPObject.class, JSONArray.class};
        for (int i = 0; i < 69; i++) {
            Class cls9 = clsArr[i];
            if (cls9 != null) {
                mappings.put(cls9.getName(), cls9);
            }
        }
        ModuleUtil.callWhenHasJavaSql(addBaseClassMappingsFunction, mappings);
    }

    public static void clearClassMapping() {
        mappings.clear();
        addBaseClassMappings();
    }

    public static void addMapping(String str, Class<?> cls) {
        mappings.put(str, cls);
    }

    public static Class<?> loadClass(String str) {
        return loadClass(str, (ClassLoader) null);
    }

    public static boolean isPath(Class<?> cls) {
        if (pathClass == null && !pathClass_error) {
            try {
                pathClass = Class.forName("java.nio.file.Path");
            } catch (Throwable unused) {
                pathClass_error = true;
            }
        }
        Class<?> cls2 = pathClass;
        if (cls2 != null) {
            return cls2.isAssignableFrom(cls);
        }
        return false;
    }

    public static Class<?> getClassFromMapping(String str) {
        return (Class) mappings.get(str);
    }

    public static Class<?> loadClass(String str, ClassLoader classLoader) {
        return loadClass(str, classLoader, false);
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:37:0x0083 */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0089 A[Catch:{ all -> 0x008e }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Class<?> loadClass(java.lang.String r5, java.lang.ClassLoader r6, boolean r7) {
        /*
            if (r5 == 0) goto L_0x00a6
            int r0 = r5.length()
            if (r0 != 0) goto L_0x000a
            goto L_0x00a6
        L_0x000a:
            int r0 = r5.length()
            r1 = 198(0xc6, float:2.77E-43)
            if (r0 > r1) goto L_0x008f
            java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Class<?>> r0 = mappings
            java.lang.Object r0 = r0.get(r5)
            java.lang.Class r0 = (java.lang.Class) r0
            if (r0 == 0) goto L_0x001d
            return r0
        L_0x001d:
            r1 = 0
            char r2 = r5.charAt(r1)
            r3 = 91
            r4 = 1
            if (r2 != r3) goto L_0x0038
            java.lang.String r5 = r5.substring(r4)
            java.lang.Class r5 = loadClass(r5, r6)
            java.lang.Object r5 = java.lang.reflect.Array.newInstance(r5, r1)
            java.lang.Class r5 = r5.getClass()
            return r5
        L_0x0038:
            java.lang.String r1 = "L"
            boolean r1 = r5.startsWith(r1)
            if (r1 == 0) goto L_0x0056
            java.lang.String r1 = ";"
            boolean r1 = r5.endsWith(r1)
            if (r1 == 0) goto L_0x0056
            int r7 = r5.length()
            int r7 = r7 - r4
            java.lang.String r5 = r5.substring(r4, r7)
            java.lang.Class r5 = loadClass(r5, r6)
            return r5
        L_0x0056:
            if (r6 == 0) goto L_0x0068
            java.lang.Class r0 = r6.loadClass(r5)     // Catch:{ all -> 0x0064 }
            if (r7 == 0) goto L_0x0063
            java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Class<?>> r1 = mappings     // Catch:{ all -> 0x0064 }
            r1.put(r5, r0)     // Catch:{ all -> 0x0064 }
        L_0x0063:
            return r0
        L_0x0064:
            r1 = move-exception
            r1.printStackTrace()
        L_0x0068:
            java.lang.Thread r1 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x0083 }
            java.lang.ClassLoader r1 = r1.getContextClassLoader()     // Catch:{ all -> 0x0083 }
            if (r1 == 0) goto L_0x0083
            if (r1 == r6) goto L_0x0083
            java.lang.Class r6 = r1.loadClass(r5)     // Catch:{ all -> 0x0083 }
            if (r7 == 0) goto L_0x0082
            java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Class<?>> r0 = mappings     // Catch:{ all -> 0x0080 }
            r0.put(r5, r6)     // Catch:{ all -> 0x0080 }
            goto L_0x0082
        L_0x0080:
            r0 = r6
            goto L_0x0083
        L_0x0082:
            return r6
        L_0x0083:
            java.lang.Class r0 = java.lang.Class.forName(r5)     // Catch:{ all -> 0x008e }
            if (r7 == 0) goto L_0x008e
            java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Class<?>> r6 = mappings     // Catch:{ all -> 0x008e }
            r6.put(r5, r0)     // Catch:{ all -> 0x008e }
        L_0x008e:
            return r0
        L_0x008f:
            com.alibaba.fastjson.JSONException r6 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r0 = "illegal className : "
            r7.append(r0)
            r7.append(r5)
            java.lang.String r5 = r7.toString()
            r6.<init>(r5)
            throw r6
        L_0x00a6:
            r5 = 0
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.TypeUtils.loadClass(java.lang.String, java.lang.ClassLoader, boolean):java.lang.Class");
    }

    public static SerializeBeanInfo buildBeanInfo(Class<?> cls, Map<String, String> map, PropertyNamingStrategy propertyNamingStrategy) {
        return buildBeanInfo(cls, map, propertyNamingStrategy, false);
    }

    /* JADX WARNING: type inference failed for: r16v0, types: [java.lang.Class<?>, java.lang.Class] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.alibaba.fastjson.serializer.SerializeBeanInfo buildBeanInfo(java.lang.Class<?> r16, java.util.Map<java.lang.String, java.lang.String> r17, com.alibaba.fastjson.PropertyNamingStrategy r18, boolean r19) {
        /*
            r6 = r16
            r7 = r17
            java.lang.Class<com.alibaba.fastjson.annotation.JSONType> r0 = com.alibaba.fastjson.annotation.JSONType.class
            java.lang.annotation.Annotation r0 = getAnnotation((java.lang.Class<?>) r6, r0)
            r8 = r0
            com.alibaba.fastjson.annotation.JSONType r8 = (com.alibaba.fastjson.annotation.JSONType) r8
            r0 = 0
            r1 = 0
            if (r8 == 0) goto L_0x008b
            java.lang.String[] r2 = r8.orders()
            java.lang.String r3 = r8.typeName()
            int r4 = r3.length()
            if (r4 != 0) goto L_0x0020
            r3 = r1
        L_0x0020:
            com.alibaba.fastjson.PropertyNamingStrategy r4 = r8.naming()
            com.alibaba.fastjson.PropertyNamingStrategy r5 = com.alibaba.fastjson.PropertyNamingStrategy.NeverUseThisValueExceptDefaultValue
            if (r4 == r5) goto L_0x0029
            goto L_0x002b
        L_0x0029:
            r4 = r18
        L_0x002b:
            com.alibaba.fastjson.serializer.SerializerFeature[] r5 = r8.serialzeFeatures()
            int r5 = com.alibaba.fastjson.serializer.SerializerFeature.of(r5)
            java.lang.Class r9 = r16.getSuperclass()
            r10 = r1
        L_0x0038:
            if (r9 == 0) goto L_0x0059
            java.lang.Class<java.lang.Object> r11 = java.lang.Object.class
            if (r9 == r11) goto L_0x0059
            java.lang.Class<com.alibaba.fastjson.annotation.JSONType> r11 = com.alibaba.fastjson.annotation.JSONType.class
            java.lang.annotation.Annotation r11 = getAnnotation((java.lang.Class<?>) r9, r11)
            com.alibaba.fastjson.annotation.JSONType r11 = (com.alibaba.fastjson.annotation.JSONType) r11
            if (r11 != 0) goto L_0x0049
            goto L_0x0059
        L_0x0049:
            java.lang.String r10 = r11.typeKey()
            int r11 = r10.length()
            if (r11 == 0) goto L_0x0054
            goto L_0x0059
        L_0x0054:
            java.lang.Class r9 = r9.getSuperclass()
            goto L_0x0038
        L_0x0059:
            java.lang.Class[] r9 = r16.getInterfaces()
            int r11 = r9.length
            r12 = 0
        L_0x005f:
            if (r12 >= r11) goto L_0x007b
            r13 = r9[r12]
            java.lang.Class<com.alibaba.fastjson.annotation.JSONType> r14 = com.alibaba.fastjson.annotation.JSONType.class
            java.lang.annotation.Annotation r13 = getAnnotation((java.lang.Class<?>) r13, r14)
            com.alibaba.fastjson.annotation.JSONType r13 = (com.alibaba.fastjson.annotation.JSONType) r13
            if (r13 == 0) goto L_0x0078
            java.lang.String r10 = r13.typeKey()
            int r13 = r10.length()
            if (r13 == 0) goto L_0x0078
            goto L_0x007b
        L_0x0078:
            int r12 = r12 + 1
            goto L_0x005f
        L_0x007b:
            if (r10 == 0) goto L_0x0084
            int r9 = r10.length()
            if (r9 != 0) goto L_0x0084
            goto L_0x0085
        L_0x0084:
            r1 = r10
        L_0x0085:
            r11 = r1
            r9 = r2
            r10 = r3
            r12 = r4
            r13 = r5
            goto L_0x0091
        L_0x008b:
            r12 = r18
            r9 = r1
            r10 = r9
            r11 = r10
            r13 = 0
        L_0x0091:
            java.util.HashMap r14 = new java.util.HashMap
            r14.<init>()
            com.alibaba.fastjson.parser.ParserConfig.parserAllFieldToCache(r6, r14)
            if (r19 == 0) goto L_0x00a0
            java.util.List r0 = computeGettersWithFieldBase(r6, r7, r0, r12)
            goto L_0x00ac
        L_0x00a0:
            r4 = 0
            r0 = r16
            r1 = r8
            r2 = r17
            r3 = r14
            r5 = r12
            java.util.List r0 = computeGetters(r0, r1, r2, r3, r4, r5)
        L_0x00ac:
            int r1 = r0.size()
            com.alibaba.fastjson.util.FieldInfo[] r15 = new com.alibaba.fastjson.util.FieldInfo[r1]
            r0.toArray(r15)
            if (r9 == 0) goto L_0x00cf
            int r1 = r9.length
            if (r1 == 0) goto L_0x00cf
            if (r19 == 0) goto L_0x00c2
            r0 = 1
            java.util.List r0 = computeGettersWithFieldBase(r6, r7, r0, r12)
            goto L_0x00d8
        L_0x00c2:
            r4 = 1
            r0 = r16
            r1 = r8
            r2 = r17
            r3 = r14
            r5 = r12
            java.util.List r0 = computeGetters(r0, r1, r2, r3, r4, r5)
            goto L_0x00d8
        L_0x00cf:
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>(r0)
            java.util.Collections.sort(r1)
            r0 = r1
        L_0x00d8:
            int r1 = r0.size()
            com.alibaba.fastjson.util.FieldInfo[] r1 = new com.alibaba.fastjson.util.FieldInfo[r1]
            r0.toArray(r1)
            boolean r0 = java.util.Arrays.equals(r1, r15)
            if (r0 == 0) goto L_0x00e9
            r7 = r15
            goto L_0x00ea
        L_0x00e9:
            r7 = r1
        L_0x00ea:
            com.alibaba.fastjson.serializer.SerializeBeanInfo r9 = new com.alibaba.fastjson.serializer.SerializeBeanInfo
            r0 = r9
            r1 = r16
            r2 = r8
            r3 = r10
            r4 = r11
            r5 = r13
            r6 = r15
            r0.<init>(r1, r2, r3, r4, r5, r6, r7)
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.TypeUtils.buildBeanInfo(java.lang.Class, java.util.Map, com.alibaba.fastjson.PropertyNamingStrategy, boolean):com.alibaba.fastjson.serializer.SerializeBeanInfo");
    }

    public static List<FieldInfo> computeGettersWithFieldBase(Class<?> cls, Map<String, String> map, boolean z, PropertyNamingStrategy propertyNamingStrategy) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Class<?> cls2 = cls; cls2 != null; cls2 = cls2.getSuperclass()) {
            computeFields(cls2, map, propertyNamingStrategy, linkedHashMap, cls2.getDeclaredFields());
        }
        return getFieldInfos(cls, z, linkedHashMap);
    }

    public static List<FieldInfo> computeGetters(Class<?> cls, Map<String, String> map) {
        return computeGetters(cls, map, true);
    }

    public static List<FieldInfo> computeGetters(Class<?> cls, Map<String, String> map, boolean z) {
        HashMap hashMap = new HashMap();
        ParserConfig.parserAllFieldToCache(cls, hashMap);
        return computeGetters(cls, (JSONType) getAnnotation(cls, JSONType.class), map, hashMap, z, PropertyNamingStrategy.CamelCase);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:132:0x02bc, code lost:
        if (r1 == null) goto L_0x024b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:165:0x034d, code lost:
        if (r2 == null) goto L_0x024b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:203:0x043a, code lost:
        r0 = r12.substring(2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:213:0x045d, code lost:
        if (r2 == null) goto L_0x0520;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:231:0x04b8, code lost:
        if (r0 == null) goto L_0x0522;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0083, code lost:
        if (r4.getName().equals("groovy.lang.MetaClass") != false) goto L_0x003f;
     */
    /* JADX WARNING: Removed duplicated region for block: B:142:0x02e0  */
    /* JADX WARNING: Removed duplicated region for block: B:245:0x04f8  */
    /* JADX WARNING: Removed duplicated region for block: B:248:0x0503  */
    /* JADX WARNING: Removed duplicated region for block: B:249:0x0505  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.List<com.alibaba.fastjson.util.FieldInfo> computeGetters(java.lang.Class<?> r38, com.alibaba.fastjson.annotation.JSONType r39, java.util.Map<java.lang.String, java.lang.String> r40, java.util.Map<java.lang.String, java.lang.reflect.Field> r41, boolean r42, com.alibaba.fastjson.PropertyNamingStrategy r43) {
        /*
            r12 = r38
            r13 = r40
            r14 = r41
            r15 = r43
            java.util.LinkedHashMap r11 = new java.util.LinkedHashMap
            r11.<init>()
            boolean r16 = isKotlin(r38)
            r17 = 0
            r0 = r17
            java.lang.annotation.Annotation[][] r0 = (java.lang.annotation.Annotation[][]) r0
            java.lang.reflect.Method[] r10 = r38.getMethods()
            com.alibaba.fastjson.util.TypeUtils$MethodInheritanceComparator r1 = new com.alibaba.fastjson.util.TypeUtils$MethodInheritanceComparator     // Catch:{ all -> 0x0023 }
            r1.<init>()     // Catch:{ all -> 0x0023 }
            java.util.Arrays.sort(r10, r1)     // Catch:{ all -> 0x0023 }
        L_0x0023:
            int r9 = r10.length
            r1 = r17
            r2 = r1
            r3 = r2
            r7 = 0
        L_0x0029:
            if (r7 >= r9) goto L_0x053e
            r6 = r10[r7]
            java.lang.String r5 = r6.getName()
            r18 = 0
            int r4 = r6.getModifiers()
            boolean r4 = java.lang.reflect.Modifier.isStatic(r4)
            if (r4 == 0) goto L_0x004d
        L_0x003d:
            r19 = r0
        L_0x003f:
            r20 = r2
            r31 = r7
            r33 = r9
            r34 = r10
            r0 = r11
            r14 = r13
            r32 = 0
            goto L_0x052c
        L_0x004d:
            java.lang.Class r4 = r6.getReturnType()
            java.lang.Class r8 = java.lang.Void.TYPE
            boolean r8 = r4.equals(r8)
            if (r8 == 0) goto L_0x005a
            goto L_0x003d
        L_0x005a:
            java.lang.Class[] r8 = r6.getParameterTypes()
            int r8 = r8.length
            if (r8 == 0) goto L_0x0062
            goto L_0x003d
        L_0x0062:
            java.lang.Class<java.lang.ClassLoader> r8 = java.lang.ClassLoader.class
            if (r4 == r8) goto L_0x003d
            java.lang.Class<java.io.InputStream> r8 = java.io.InputStream.class
            if (r4 == r8) goto L_0x003d
            java.lang.Class<java.io.Reader> r8 = java.io.Reader.class
            if (r4 != r8) goto L_0x006f
            goto L_0x003d
        L_0x006f:
            java.lang.String r8 = "getMetaClass"
            boolean r8 = r5.equals(r8)
            if (r8 == 0) goto L_0x0086
            java.lang.String r8 = r4.getName()
            r19 = r0
            java.lang.String r0 = "groovy.lang.MetaClass"
            boolean r0 = r8.equals(r0)
            if (r0 == 0) goto L_0x0088
        L_0x0085:
            goto L_0x003f
        L_0x0086:
            r19 = r0
        L_0x0088:
            java.lang.String r0 = "getSuppressed"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x0099
            java.lang.Class r0 = r6.getDeclaringClass()
            java.lang.Class<java.lang.Throwable> r8 = java.lang.Throwable.class
            if (r0 != r8) goto L_0x0099
        L_0x0098:
            goto L_0x0085
        L_0x0099:
            if (r16 == 0) goto L_0x00a2
            boolean r0 = isKotlinIgnore(r12, r5)
            if (r0 == 0) goto L_0x00a2
            goto L_0x0098
        L_0x00a2:
            r0 = 0
            java.lang.Boolean r8 = java.lang.Boolean.valueOf(r0)
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r0 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r0 = getAnnotation((java.lang.reflect.Method) r6, r0)
            com.alibaba.fastjson.annotation.JSONField r0 = (com.alibaba.fastjson.annotation.JSONField) r0
            if (r0 != 0) goto L_0x00b5
            com.alibaba.fastjson.annotation.JSONField r0 = getSuperMethodAnnotation(r12, r6)
        L_0x00b5:
            r20 = r2
            java.lang.String r2 = "get"
            r21 = r11
            if (r0 != 0) goto L_0x0190
            if (r16 == 0) goto L_0x0190
            if (r1 != 0) goto L_0x010b
            java.lang.reflect.Constructor[] r1 = r38.getDeclaredConstructors()
            java.lang.reflect.Constructor r22 = getKotlinConstructor(r1)
            if (r22 == 0) goto L_0x0106
            java.lang.annotation.Annotation[][] r19 = getParameterAnnotations((java.lang.reflect.Constructor) r22)
            java.lang.String[] r11 = getKoltinConstructorParameters(r38)
            if (r11 == 0) goto L_0x00fd
            int r3 = r11.length
            java.lang.String[] r3 = new java.lang.String[r3]
            r23 = r0
            int r0 = r11.length
            r24 = r8
            r8 = 0
            java.lang.System.arraycopy(r11, r8, r3, r8, r0)
            java.util.Arrays.sort(r3)
            int r0 = r11.length
            short[] r0 = new short[r0]
            r25 = r1
        L_0x00e9:
            int r1 = r11.length
            if (r8 >= r1) goto L_0x00f8
            r1 = r11[r8]
            int r1 = java.util.Arrays.binarySearch(r3, r1)
            r0[r1] = r8
            int r8 = r8 + 1
            short r8 = (short) r8
            goto L_0x00e9
        L_0x00f8:
            r11 = r3
            r1 = r25
            r3 = r0
            goto L_0x0103
        L_0x00fd:
            r23 = r0
            r25 = r1
            r24 = r8
        L_0x0103:
            r0 = r19
            goto L_0x0113
        L_0x0106:
            r23 = r0
            r25 = r1
            goto L_0x010d
        L_0x010b:
            r23 = r0
        L_0x010d:
            r24 = r8
            r0 = r19
            r11 = r20
        L_0x0113:
            if (r11 == 0) goto L_0x0183
            if (r3 == 0) goto L_0x0183
            boolean r8 = r5.startsWith(r2)
            if (r8 == 0) goto L_0x0183
            r8 = 3
            java.lang.String r19 = r5.substring(r8)
            java.lang.String r8 = decapitalize(r19)
            int r19 = java.util.Arrays.binarySearch(r11, r8)
            r20 = r1
            r25 = r4
            if (r19 >= 0) goto L_0x0142
            r1 = 0
        L_0x0131:
            int r4 = r11.length
            if (r1 >= r4) goto L_0x0142
            r4 = r11[r1]
            boolean r4 = r8.equalsIgnoreCase(r4)
            if (r4 == 0) goto L_0x013f
            r19 = r1
            goto L_0x0142
        L_0x013f:
            int r1 = r1 + 1
            goto L_0x0131
        L_0x0142:
            if (r19 < 0) goto L_0x017e
            short r1 = r3[r19]
            r1 = r0[r1]
            if (r1 == 0) goto L_0x0165
            int r4 = r1.length
            r19 = r0
            r0 = 0
        L_0x014e:
            r26 = r3
            if (r0 >= r4) goto L_0x0169
            r3 = r1[r0]
            r27 = r1
            boolean r1 = r3 instanceof com.alibaba.fastjson.annotation.JSONField
            if (r1 == 0) goto L_0x015e
            r0 = r3
            com.alibaba.fastjson.annotation.JSONField r0 = (com.alibaba.fastjson.annotation.JSONField) r0
            goto L_0x016b
        L_0x015e:
            int r0 = r0 + 1
            r3 = r26
            r1 = r27
            goto L_0x014e
        L_0x0165:
            r19 = r0
            r26 = r3
        L_0x0169:
            r0 = r23
        L_0x016b:
            if (r0 != 0) goto L_0x017b
            java.lang.reflect.Field r1 = com.alibaba.fastjson.parser.ParserConfig.getFieldFromCache(r8, r14)
            if (r1 == 0) goto L_0x017b
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r0 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r0 = getAnnotation((java.lang.reflect.Field) r1, r0)
            com.alibaba.fastjson.annotation.JSONField r0 = (com.alibaba.fastjson.annotation.JSONField) r0
        L_0x017b:
            r27 = r0
            goto L_0x018d
        L_0x017e:
            r19 = r0
            r26 = r3
            goto L_0x018b
        L_0x0183:
            r19 = r0
            r20 = r1
            r26 = r3
            r25 = r4
        L_0x018b:
            r27 = r23
        L_0x018d:
            r23 = r11
            goto L_0x019e
        L_0x0190:
            r23 = r0
            r25 = r4
            r24 = r8
            r26 = r3
            r27 = r23
            r23 = r20
            r20 = r1
        L_0x019e:
            if (r27 == 0) goto L_0x022c
            boolean r0 = r27.serialize()
            if (r0 != 0) goto L_0x01b3
        L_0x01a6:
            r31 = r7
            r33 = r9
            r34 = r10
            r14 = r13
            r0 = r21
            r32 = 0
            goto L_0x0525
        L_0x01b3:
            int r8 = r27.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r0 = r27.serialzeFeatures()
            int r11 = com.alibaba.fastjson.serializer.SerializerFeature.of(r0)
            com.alibaba.fastjson.parser.Feature[] r0 = r27.parseFeatures()
            int r28 = com.alibaba.fastjson.parser.Feature.of(r0)
            java.lang.String r0 = r27.name()
            int r0 = r0.length()
            if (r0 == 0) goto L_0x0213
            java.lang.String r0 = r27.name()
            if (r13 == 0) goto L_0x01e0
            java.lang.Object r0 = r13.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            if (r0 != 0) goto L_0x01e0
            goto L_0x01a6
        L_0x01e0:
            r5 = r0
            com.alibaba.fastjson.util.FieldInfo r4 = new com.alibaba.fastjson.util.FieldInfo
            r3 = 0
            r22 = 0
            r24 = 0
            r0 = r4
            r1 = r5
            r2 = r6
            r6 = r4
            r4 = r38
            r29 = r5
            r5 = r22
            r30 = r6
            r6 = r8
            r31 = r7
            r7 = r11
            r32 = 0
            r8 = r28
            r33 = r9
            r9 = r27
            r34 = r10
            r10 = r24
            r15 = r21
            r11 = r18
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11)
            r1 = r29
            r0 = r30
            r15.put(r1, r0)
            goto L_0x024b
        L_0x0213:
            r31 = r7
            r33 = r9
            r34 = r10
            r15 = r21
            r32 = 0
            java.lang.String r0 = r27.label()
            int r0 = r0.length()
            if (r0 == 0) goto L_0x023a
            java.lang.String r18 = r27.label()
            goto L_0x023a
        L_0x022c:
            r31 = r7
            r33 = r9
            r34 = r10
            r15 = r21
            r32 = 0
            r8 = 0
            r11 = 0
            r28 = 0
        L_0x023a:
            boolean r0 = r5.startsWith(r2)
            r10 = 102(0x66, float:1.43E-43)
            r9 = 95
            if (r0 == 0) goto L_0x03cd
            int r0 = r5.length()
            r1 = 4
            if (r0 >= r1) goto L_0x024e
        L_0x024b:
            r14 = r13
            goto L_0x0522
        L_0x024e:
            java.lang.String r0 = "getClass"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x0257
            goto L_0x024b
        L_0x0257:
            java.lang.String r0 = "getDeclaringClass"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x0266
            boolean r0 = r38.isEnum()
            if (r0 == 0) goto L_0x0266
            goto L_0x024b
        L_0x0266:
            r0 = 3
            char r2 = r5.charAt(r0)
            boolean r3 = java.lang.Character.isUpperCase(r2)
            if (r3 != 0) goto L_0x02c1
            r3 = 512(0x200, float:7.175E-43)
            if (r2 <= r3) goto L_0x0276
            goto L_0x02c1
        L_0x0276:
            if (r2 != r9) goto L_0x0291
            java.lang.String r2 = r5.substring(r0)
            java.lang.Object r0 = r14.get(r2)
            java.lang.reflect.Field r0 = (java.lang.reflect.Field) r0
            if (r0 != 0) goto L_0x02d8
            java.lang.String r0 = r5.substring(r1)
            java.lang.reflect.Field r1 = com.alibaba.fastjson.parser.ParserConfig.getFieldFromCache(r0, r14)
            if (r1 != 0) goto L_0x028f
            goto L_0x02bf
        L_0x028f:
            r2 = r0
            goto L_0x02bf
        L_0x0291:
            if (r2 != r10) goto L_0x0299
            r0 = 3
            java.lang.String r2 = r5.substring(r0)
            goto L_0x02d6
        L_0x0299:
            r0 = 3
            int r2 = r5.length()
            r3 = 5
            if (r2 < r3) goto L_0x02b4
            char r1 = r5.charAt(r1)
            boolean r1 = java.lang.Character.isUpperCase(r1)
            if (r1 == 0) goto L_0x02b4
            java.lang.String r1 = r5.substring(r0)
            java.lang.String r2 = decapitalize(r1)
            goto L_0x02d6
        L_0x02b4:
            java.lang.String r2 = r5.substring(r0)
            java.lang.reflect.Field r1 = com.alibaba.fastjson.parser.ParserConfig.getFieldFromCache(r2, r14)
            if (r1 != 0) goto L_0x02bf
            goto L_0x024b
        L_0x02bf:
            r0 = r1
            goto L_0x02d8
        L_0x02c1:
            boolean r1 = compatibleWithJavaBean
            if (r1 == 0) goto L_0x02ce
            java.lang.String r1 = r5.substring(r0)
            java.lang.String r1 = decapitalize(r1)
            goto L_0x02d2
        L_0x02ce:
            java.lang.String r1 = getPropertyNameByMethodName(r5)
        L_0x02d2:
            java.lang.String r2 = getPropertyNameByCompatibleFieldName(r14, r5, r1, r0)
        L_0x02d6:
            r0 = r17
        L_0x02d8:
            boolean r1 = isJSONTypeIgnore(r12, r2)
            if (r1 == 0) goto L_0x02e0
            goto L_0x024b
        L_0x02e0:
            if (r0 != 0) goto L_0x02e6
            java.lang.reflect.Field r0 = com.alibaba.fastjson.parser.ParserConfig.getFieldFromCache(r2, r14)
        L_0x02e6:
            r1 = 1
            if (r0 != 0) goto L_0x0309
            int r3 = r2.length()
            if (r3 <= r1) goto L_0x0309
            char r3 = r2.charAt(r1)
            r4 = 65
            if (r3 < r4) goto L_0x0309
            r4 = 90
            if (r3 > r4) goto L_0x0309
            r7 = 3
            java.lang.String r0 = r5.substring(r7)
            java.lang.String r0 = decapitalize(r0)
            java.lang.reflect.Field r0 = com.alibaba.fastjson.parser.ParserConfig.getFieldFromCache(r0, r14)
            goto L_0x030a
        L_0x0309:
            r7 = 3
        L_0x030a:
            r3 = r0
            if (r3 == 0) goto L_0x0372
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r0 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r0 = getAnnotation((java.lang.reflect.Field) r3, r0)
            com.alibaba.fastjson.annotation.JSONField r0 = (com.alibaba.fastjson.annotation.JSONField) r0
            if (r0 == 0) goto L_0x036c
            boolean r4 = r0.serialize()
            if (r4 != 0) goto L_0x031f
            goto L_0x024b
        L_0x031f:
            int r4 = r0.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r8 = r0.serialzeFeatures()
            int r8 = com.alibaba.fastjson.serializer.SerializerFeature.of(r8)
            com.alibaba.fastjson.parser.Feature[] r11 = r0.parseFeatures()
            int r11 = com.alibaba.fastjson.parser.Feature.of(r11)
            java.lang.String r21 = r0.name()
            int r21 = r21.length()
            if (r21 == 0) goto L_0x0351
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)
            java.lang.String r2 = r0.name()
            if (r13 == 0) goto L_0x0353
            java.lang.Object r2 = r13.get(r2)
            java.lang.String r2 = (java.lang.String) r2
            if (r2 != 0) goto L_0x0353
            goto L_0x024b
        L_0x0351:
            r1 = r24
        L_0x0353:
            java.lang.String r21 = r0.label()
            int r21 = r21.length()
            if (r21 == 0) goto L_0x0361
            java.lang.String r18 = r0.label()
        L_0x0361:
            r21 = r8
            r28 = r11
            r22 = r18
            r11 = r0
            r8 = r1
            r18 = r4
            goto L_0x037c
        L_0x036c:
            r21 = r11
            r22 = r18
            r11 = r0
            goto L_0x0378
        L_0x0372:
            r21 = r11
            r11 = r17
            r22 = r18
        L_0x0378:
            r18 = r8
            r8 = r24
        L_0x037c:
            if (r13 == 0) goto L_0x0389
            java.lang.Object r0 = r13.get(r2)
            r2 = r0
            java.lang.String r2 = (java.lang.String) r2
            if (r2 != 0) goto L_0x0389
            goto L_0x024b
        L_0x0389:
            r4 = r15
            r15 = r43
            if (r15 == 0) goto L_0x0398
            boolean r0 = r8.booleanValue()
            if (r0 != 0) goto L_0x0398
            java.lang.String r2 = r15.translate(r2)
        L_0x0398:
            r8 = r2
            com.alibaba.fastjson.util.FieldInfo r2 = new com.alibaba.fastjson.util.FieldInfo
            r24 = 0
            r0 = r2
            r1 = r8
            r35 = r2
            r2 = r6
            r15 = r4
            r13 = r25
            r4 = r38
            r12 = r5
            r5 = r24
            r24 = r6
            r6 = r18
            r25 = 3
            r7 = r21
            r36 = r8
            r8 = r28
            r9 = r27
            r10 = r11
            r14 = 3
            r11 = r22
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11)
            r0 = r35
            r2 = r36
            r15.put(r2, r0)
            r8 = r18
            r11 = r21
            r18 = r22
            goto L_0x03d3
        L_0x03cd:
            r12 = r5
            r24 = r6
            r13 = r25
            r14 = 3
        L_0x03d3:
            java.lang.String r0 = "is"
            boolean r0 = r12.startsWith(r0)
            if (r0 == 0) goto L_0x0520
            int r0 = r12.length()
            if (r0 >= r14) goto L_0x03e3
            goto L_0x0520
        L_0x03e3:
            java.lang.Class r0 = java.lang.Boolean.TYPE
            if (r13 == r0) goto L_0x03ed
            java.lang.Class<java.lang.Boolean> r0 = java.lang.Boolean.class
            if (r13 == r0) goto L_0x03ed
            goto L_0x0520
        L_0x03ed:
            r0 = 2
            char r1 = r12.charAt(r0)
            boolean r2 = java.lang.Character.isUpperCase(r1)
            if (r2 == 0) goto L_0x0427
            boolean r1 = compatibleWithJavaBean
            if (r1 == 0) goto L_0x0405
            java.lang.String r1 = r12.substring(r0)
            java.lang.String r1 = decapitalize(r1)
            goto L_0x0420
        L_0x0405:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            char r2 = r12.charAt(r0)
            char r2 = java.lang.Character.toLowerCase(r2)
            r1.append(r2)
            java.lang.String r2 = r12.substring(r14)
            r1.append(r2)
            java.lang.String r1 = r1.toString()
        L_0x0420:
            r13 = r41
            java.lang.String r0 = getPropertyNameByCompatibleFieldName(r13, r12, r1, r0)
            goto L_0x0451
        L_0x0427:
            r13 = r41
            r2 = 95
            r3 = 3
            if (r1 != r2) goto L_0x0449
            java.lang.String r1 = r12.substring(r3)
            java.lang.Object r2 = r13.get(r1)
            java.lang.reflect.Field r2 = (java.lang.reflect.Field) r2
            if (r2 != 0) goto L_0x0444
            java.lang.String r0 = r12.substring(r0)
            java.lang.reflect.Field r2 = com.alibaba.fastjson.parser.ParserConfig.getFieldFromCache(r0, r13)
            if (r2 != 0) goto L_0x0445
        L_0x0444:
            r0 = r1
        L_0x0445:
            r1 = r12
        L_0x0446:
            r12 = r38
            goto L_0x0461
        L_0x0449:
            r2 = 102(0x66, float:1.43E-43)
            if (r1 != r2) goto L_0x0455
            java.lang.String r0 = r12.substring(r0)
        L_0x0451:
            r1 = r12
            r2 = r17
            goto L_0x0446
        L_0x0455:
            java.lang.String r0 = r12.substring(r0)
            java.lang.reflect.Field r2 = com.alibaba.fastjson.parser.ParserConfig.getFieldFromCache(r0, r13)
            if (r2 != 0) goto L_0x0445
            goto L_0x0520
        L_0x0461:
            boolean r3 = isJSONTypeIgnore(r12, r0)
            if (r3 == 0) goto L_0x0469
            goto L_0x0520
        L_0x0469:
            if (r2 != 0) goto L_0x046f
            java.lang.reflect.Field r2 = com.alibaba.fastjson.parser.ParserConfig.getFieldFromCache(r0, r13)
        L_0x046f:
            if (r2 != 0) goto L_0x0477
            java.lang.reflect.Field r1 = com.alibaba.fastjson.parser.ParserConfig.getFieldFromCache(r1, r13)
            r3 = r1
            goto L_0x0478
        L_0x0477:
            r3 = r2
        L_0x0478:
            if (r3 == 0) goto L_0x04de
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r1 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r1 = getAnnotation((java.lang.reflect.Field) r3, r1)
            com.alibaba.fastjson.annotation.JSONField r1 = (com.alibaba.fastjson.annotation.JSONField) r1
            if (r1 == 0) goto L_0x04d8
            boolean r2 = r1.serialize()
            if (r2 != 0) goto L_0x048c
            goto L_0x0520
        L_0x048c:
            int r2 = r1.ordinal()
            com.alibaba.fastjson.serializer.SerializerFeature[] r4 = r1.serialzeFeatures()
            int r4 = com.alibaba.fastjson.serializer.SerializerFeature.of(r4)
            com.alibaba.fastjson.parser.Feature[] r5 = r1.parseFeatures()
            int r5 = com.alibaba.fastjson.parser.Feature.of(r5)
            java.lang.String r6 = r1.name()
            int r6 = r6.length()
            if (r6 == 0) goto L_0x04bb
            java.lang.String r0 = r1.name()
            r14 = r40
            if (r14 == 0) goto L_0x04bd
            java.lang.Object r0 = r14.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            if (r0 != 0) goto L_0x04bd
            goto L_0x04f2
        L_0x04bb:
            r14 = r40
        L_0x04bd:
            java.lang.String r6 = r1.label()
            int r6 = r6.length()
            if (r6 == 0) goto L_0x04d1
            java.lang.String r6 = r1.label()
            r10 = r1
            r7 = r4
            r8 = r5
            r11 = r6
            r6 = r2
            goto L_0x04e8
        L_0x04d1:
            r10 = r1
            r6 = r2
            r7 = r4
            r8 = r5
            r11 = r18
            goto L_0x04e8
        L_0x04d8:
            r14 = r40
            r10 = r1
            r6 = r8
            r7 = r11
            goto L_0x04e4
        L_0x04de:
            r14 = r40
            r6 = r8
            r7 = r11
            r10 = r17
        L_0x04e4:
            r11 = r18
            r8 = r28
        L_0x04e8:
            if (r14 == 0) goto L_0x04f3
            java.lang.Object r0 = r14.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            if (r0 != 0) goto L_0x04f3
        L_0x04f2:
            goto L_0x0522
        L_0x04f3:
            r9 = r15
            r15 = r43
            if (r15 == 0) goto L_0x04fc
            java.lang.String r0 = r15.translate(r0)
        L_0x04fc:
            r5 = r0
            boolean r0 = r9.containsKey(r5)
            if (r0 == 0) goto L_0x0505
            r0 = r9
            goto L_0x0525
        L_0x0505:
            com.alibaba.fastjson.util.FieldInfo r4 = new com.alibaba.fastjson.util.FieldInfo
            r18 = 0
            r0 = r4
            r1 = r5
            r2 = r24
            r13 = r4
            r4 = r38
            r12 = r5
            r5 = r18
            r37 = r9
            r9 = r27
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11)
            r0 = r37
            r0.put(r12, r13)
            goto L_0x0525
        L_0x0520:
            r14 = r40
        L_0x0522:
            r0 = r15
            r15 = r43
        L_0x0525:
            r1 = r20
            r2 = r23
            r3 = r26
            goto L_0x052e
        L_0x052c:
            r2 = r20
        L_0x052e:
            int r7 = r31 + 1
            r12 = r38
            r11 = r0
            r13 = r14
            r0 = r19
            r9 = r33
            r10 = r34
            r14 = r41
            goto L_0x0029
        L_0x053e:
            r0 = r11
            r14 = r13
            java.lang.reflect.Field[] r1 = r38.getFields()
            r2 = r38
            computeFields(r2, r14, r15, r0, r1)
            r1 = r42
            java.util.List r0 = getFieldInfos(r2, r1, r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.TypeUtils.computeGetters(java.lang.Class, com.alibaba.fastjson.annotation.JSONType, java.util.Map, java.util.Map, boolean, com.alibaba.fastjson.PropertyNamingStrategy):java.util.List");
    }

    private static List<FieldInfo> getFieldInfos(Class<?> cls, boolean z, Map<String, FieldInfo> map) {
        ArrayList arrayList = new ArrayList();
        JSONType jSONType = (JSONType) getAnnotation(cls, JSONType.class);
        String[] orders = jSONType != null ? jSONType.orders() : null;
        if (orders == null || orders.length <= 0) {
            arrayList.addAll(map.values());
            if (z) {
                Collections.sort(arrayList);
            }
        } else {
            LinkedHashMap linkedHashMap = new LinkedHashMap(map.size());
            for (FieldInfo next : map.values()) {
                linkedHashMap.put(next.name, next);
            }
            for (String str : orders) {
                FieldInfo fieldInfo = (FieldInfo) linkedHashMap.get(str);
                if (fieldInfo != null) {
                    arrayList.add(fieldInfo);
                    linkedHashMap.remove(str);
                }
            }
            arrayList.addAll(linkedHashMap.values());
        }
        return arrayList;
    }

    private static void computeFields(Class<?> cls, Map<String, String> map, PropertyNamingStrategy propertyNamingStrategy, Map<String, FieldInfo> map2, Field[] fieldArr) {
        String str;
        int i;
        int i2;
        int i3;
        Map<String, String> map3 = map;
        PropertyNamingStrategy propertyNamingStrategy2 = propertyNamingStrategy;
        Map<String, FieldInfo> map4 = map2;
        for (Field field : fieldArr) {
            if (!Modifier.isStatic(field.getModifiers())) {
                JSONField jSONField = (JSONField) getAnnotation(field, JSONField.class);
                String name = field.getName();
                String str2 = null;
                if (jSONField == null) {
                    str = null;
                    i3 = 0;
                    i2 = 0;
                    i = 0;
                } else if (jSONField.serialize()) {
                    int ordinal = jSONField.ordinal();
                    int of = SerializerFeature.of(jSONField.serialzeFeatures());
                    int of2 = Feature.of(jSONField.parseFeatures());
                    if (jSONField.name().length() != 0) {
                        name = jSONField.name();
                    }
                    if (jSONField.label().length() != 0) {
                        str2 = jSONField.label();
                    }
                    str = str2;
                    i3 = ordinal;
                    i2 = of;
                    i = of2;
                }
                if (map3 == null || (name = map3.get(name)) != null) {
                    if (propertyNamingStrategy2 != null) {
                        name = propertyNamingStrategy2.translate(name);
                    }
                    String str3 = name;
                    if (!map4.containsKey(str3)) {
                        FieldInfo fieldInfo = r7;
                        FieldInfo fieldInfo2 = new FieldInfo(str3, (Method) null, field, cls, (Type) null, i3, i2, i, (JSONField) null, jSONField, str);
                        map4.put(str3, fieldInfo);
                    }
                }
            }
        }
    }

    private static String getPropertyNameByCompatibleFieldName(Map<String, Field> map, String str, String str2, int i) {
        if (!compatibleWithFieldName || map.containsKey(str2)) {
            return str2;
        }
        String substring = str.substring(i);
        return map.containsKey(substring) ? substring : str2;
    }

    public static JSONField getSuperMethodAnnotation(Class<?> cls, Method method) {
        boolean z;
        JSONField jSONField;
        boolean z2;
        JSONField jSONField2;
        Class[] interfaces = cls.getInterfaces();
        if (interfaces.length > 0) {
            Class[] parameterTypes = method.getParameterTypes();
            for (Class methods : interfaces) {
                for (Method method2 : methods.getMethods()) {
                    Class[] parameterTypes2 = method2.getParameterTypes();
                    if (parameterTypes2.length == parameterTypes.length && method2.getName().equals(method.getName())) {
                        int i = 0;
                        while (true) {
                            if (i >= parameterTypes.length) {
                                z2 = true;
                                break;
                            } else if (!parameterTypes2[i].equals(parameterTypes[i])) {
                                z2 = false;
                                break;
                            } else {
                                i++;
                            }
                        }
                        if (z2 && (jSONField2 = (JSONField) getAnnotation(method2, JSONField.class)) != null) {
                            return jSONField2;
                        }
                    }
                }
            }
        }
        Class<? super Object> superclass = cls.getSuperclass();
        if (superclass != null && Modifier.isAbstract(superclass.getModifiers())) {
            Class[] parameterTypes3 = method.getParameterTypes();
            for (Method method3 : superclass.getMethods()) {
                Class[] parameterTypes4 = method3.getParameterTypes();
                if (parameterTypes4.length == parameterTypes3.length && method3.getName().equals(method.getName())) {
                    int i2 = 0;
                    while (true) {
                        if (i2 >= parameterTypes3.length) {
                            z = true;
                            break;
                        } else if (!parameterTypes4[i2].equals(parameterTypes3[i2])) {
                            z = false;
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (z && (jSONField = (JSONField) getAnnotation(method3, JSONField.class)) != null) {
                        return jSONField;
                    }
                }
            }
        }
        return null;
    }

    private static boolean isJSONTypeIgnore(Class<?> cls, String str) {
        JSONType jSONType = (JSONType) getAnnotation(cls, JSONType.class);
        if (jSONType != null) {
            String[] includes = jSONType.includes();
            if (includes.length > 0) {
                for (String equals : includes) {
                    if (str.equals(equals)) {
                        return false;
                    }
                }
                return true;
            }
            for (String equals2 : jSONType.ignores()) {
                if (str.equals(equals2)) {
                    return true;
                }
            }
        }
        if (cls.getSuperclass() == Object.class || cls.getSuperclass() == null) {
            return false;
        }
        return isJSONTypeIgnore(cls.getSuperclass(), str);
    }

    public static boolean isGenericParamType(Type type) {
        Type genericSuperclass;
        if (type instanceof ParameterizedType) {
            return true;
        }
        if (!(type instanceof Class) || (genericSuperclass = ((Class) type).getGenericSuperclass()) == Object.class || !isGenericParamType(genericSuperclass)) {
            return false;
        }
        return true;
    }

    public static Type getGenericParamType(Type type) {
        return (!(type instanceof ParameterizedType) && (type instanceof Class)) ? getGenericParamType(((Class) type).getGenericSuperclass()) : type;
    }

    public static Type unwrapOptional(Type type) {
        if (!optionalClassInited) {
            try {
                optionalClass = Class.forName("java.util.Optional");
            } catch (Exception unused) {
            } catch (Throwable th) {
                optionalClassInited = true;
                throw th;
            }
            optionalClassInited = true;
        }
        if (!(type instanceof ParameterizedType)) {
            return type;
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        return parameterizedType.getRawType() == optionalClass ? parameterizedType.getActualTypeArguments()[0] : type;
    }

    public static Class<?> getClass(Type type) {
        if (type.getClass() == Class.class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        }
        if (type instanceof TypeVariable) {
            Type type2 = ((TypeVariable) type).getBounds()[0];
            if (type2 instanceof Class) {
                return (Class) type2;
            }
            return getClass(type2);
        } else if (!(type instanceof WildcardType)) {
            return Object.class;
        } else {
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            if (upperBounds.length == 1) {
                return getClass(upperBounds[0]);
            }
            return Object.class;
        }
    }

    public static Field getField(Class<?> cls, String str, Field[] fieldArr) {
        char charAt;
        char charAt2;
        for (Field field : fieldArr) {
            String name = field.getName();
            if (str.equals(name)) {
                return field;
            }
            if (str.length() > 2 && (charAt = str.charAt(0)) >= 'a' && charAt <= 'z' && (charAt2 = str.charAt(1)) >= 'A' && charAt2 <= 'Z' && str.equalsIgnoreCase(name)) {
                return field;
            }
        }
        Class<? super Object> superclass = cls.getSuperclass();
        if (superclass == null || superclass == Object.class) {
            return null;
        }
        return getField(superclass, str, superclass.getDeclaredFields());
    }

    public static int getSerializeFeatures(Class<?> cls) {
        JSONType jSONType = (JSONType) getAnnotation(cls, JSONType.class);
        if (jSONType == null) {
            return 0;
        }
        return SerializerFeature.of(jSONType.serialzeFeatures());
    }

    public static int getParserFeatures(Class<?> cls) {
        JSONType jSONType = (JSONType) getAnnotation(cls, JSONType.class);
        if (jSONType == null) {
            return 0;
        }
        return Feature.of(jSONType.parseFeatures());
    }

    public static String decapitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        if (str.length() > 1 && Character.isUpperCase(str.charAt(1)) && Character.isUpperCase(str.charAt(0))) {
            return str;
        }
        char[] charArray = str.toCharArray();
        charArray[0] = Character.toLowerCase(charArray[0]);
        return new String(charArray);
    }

    public static String getPropertyNameByMethodName(String str) {
        return Character.toLowerCase(str.charAt(3)) + str.substring(4);
    }

    static void setAccessible(AccessibleObject accessibleObject) {
        if (setAccessibleEnable && !accessibleObject.isAccessible()) {
            try {
                accessibleObject.setAccessible(true);
            } catch (Throwable unused) {
                setAccessibleEnable = false;
            }
        }
    }

    public static Type getCollectionItemType(Type type) {
        if (type instanceof ParameterizedType) {
            return getCollectionItemType((ParameterizedType) type);
        }
        if (type instanceof Class) {
            return getCollectionItemType((Class<?>) (Class) type);
        }
        return Object.class;
    }

    private static Type getCollectionItemType(Class<?> cls) {
        if (cls.getName().startsWith("java.")) {
            return Object.class;
        }
        return getCollectionItemType(getCollectionSuperType(cls));
    }

    private static Type getCollectionItemType(ParameterizedType parameterizedType) {
        Type rawType = parameterizedType.getRawType();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (rawType == Collection.class) {
            return getWildcardTypeUpperBounds(actualTypeArguments[0]);
        }
        Class cls = (Class) rawType;
        Map<TypeVariable, Type> createActualTypeMap = createActualTypeMap(cls.getTypeParameters(), actualTypeArguments);
        Type collectionSuperType = getCollectionSuperType(cls);
        if (!(collectionSuperType instanceof ParameterizedType)) {
            return getCollectionItemType((Class<?>) (Class) collectionSuperType);
        }
        Class<?> rawClass = getRawClass(collectionSuperType);
        Type[] actualTypeArguments2 = ((ParameterizedType) collectionSuperType).getActualTypeArguments();
        if (actualTypeArguments2.length > 0) {
            return getCollectionItemType(makeParameterizedType(rawClass, actualTypeArguments2, createActualTypeMap));
        }
        return getCollectionItemType(rawClass);
    }

    private static Type getCollectionSuperType(Class<?> cls) {
        Type type = null;
        for (Type type2 : cls.getGenericInterfaces()) {
            Class<?> rawClass = getRawClass(type2);
            if (rawClass == Collection.class) {
                return type2;
            }
            if (Collection.class.isAssignableFrom(rawClass)) {
                type = type2;
            }
        }
        return type == null ? cls.getGenericSuperclass() : type;
    }

    private static Map<TypeVariable, Type> createActualTypeMap(TypeVariable[] typeVariableArr, Type[] typeArr) {
        int length = typeVariableArr.length;
        HashMap hashMap = new HashMap(length);
        for (int i = 0; i < length; i++) {
            hashMap.put(typeVariableArr[i], typeArr[i]);
        }
        return hashMap;
    }

    private static ParameterizedType makeParameterizedType(Class<?> cls, Type[] typeArr, Map<TypeVariable, Type> map) {
        int length = typeArr.length;
        Type[] typeArr2 = new Type[length];
        for (int i = 0; i < length; i++) {
            typeArr2[i] = getActualType(typeArr[i], map);
        }
        return new ParameterizedTypeImpl(typeArr2, (Type) null, cls);
    }

    private static Type getActualType(Type type, Map<TypeVariable, Type> map) {
        if (type instanceof TypeVariable) {
            return map.get(type);
        }
        if (type instanceof ParameterizedType) {
            return makeParameterizedType(getRawClass(type), ((ParameterizedType) type).getActualTypeArguments(), map);
        }
        return type instanceof GenericArrayType ? new GenericArrayTypeImpl(getActualType(((GenericArrayType) type).getGenericComponentType(), map)) : type;
    }

    private static Type getWildcardTypeUpperBounds(Type type) {
        if (!(type instanceof WildcardType)) {
            return type;
        }
        Type[] upperBounds = ((WildcardType) type).getUpperBounds();
        return upperBounds.length > 0 ? upperBounds[0] : Object.class;
    }

    public static Class<?> getCollectionItemClass(Type type) {
        if (!(type instanceof ParameterizedType)) {
            return Object.class;
        }
        Type type2 = ((ParameterizedType) type).getActualTypeArguments()[0];
        if (type2 instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) type2).getUpperBounds();
            if (upperBounds.length == 1) {
                type2 = upperBounds[0];
            }
        }
        if (type2 instanceof Class) {
            Class<?> cls = (Class) type2;
            if (Modifier.isPublic(cls.getModifiers())) {
                return cls;
            }
            throw new JSONException("can not create ASMParser");
        }
        throw new JSONException("can not create ASMParser");
    }

    public static Type checkPrimitiveArray(GenericArrayType genericArrayType) {
        Type genericComponentType = genericArrayType.getGenericComponentType();
        String str = Operators.ARRAY_START_STR;
        while (genericComponentType instanceof GenericArrayType) {
            genericComponentType = ((GenericArrayType) genericComponentType).getGenericComponentType();
            str = str + str;
        }
        if (!(genericComponentType instanceof Class)) {
            return genericArrayType;
        }
        Class cls = (Class) genericComponentType;
        if (!cls.isPrimitive()) {
            return genericArrayType;
        }
        try {
            String str2 = (String) primitiveTypeMap.get(cls);
            if (str2 == null) {
                return genericArrayType;
            }
            return Class.forName(str + str2);
        } catch (ClassNotFoundException unused) {
            return genericArrayType;
        }
    }

    public static Set createSet(Type type) {
        Type type2;
        Class<?> rawClass = getRawClass(type);
        if (rawClass == AbstractCollection.class || rawClass == Collection.class) {
            return new HashSet();
        }
        if (rawClass.isAssignableFrom(HashSet.class)) {
            return new HashSet();
        }
        if (rawClass.isAssignableFrom(LinkedHashSet.class)) {
            return new LinkedHashSet();
        }
        if (rawClass.isAssignableFrom(TreeSet.class)) {
            return new TreeSet();
        }
        if (rawClass.isAssignableFrom(EnumSet.class)) {
            if (type instanceof ParameterizedType) {
                type2 = ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                type2 = Object.class;
            }
            return EnumSet.noneOf((Class) type2);
        }
        try {
            return (Set) rawClass.newInstance();
        } catch (Exception unused) {
            throw new JSONException("create instance error, class " + rawClass.getName());
        }
    }

    public static Collection createCollection(Type type) {
        Class cls;
        Type type2;
        Class<?> rawClass = getRawClass(type);
        if (rawClass == AbstractCollection.class || rawClass == Collection.class) {
            return new ArrayList();
        }
        if (rawClass.isAssignableFrom(HashSet.class)) {
            return new HashSet();
        }
        if (rawClass.isAssignableFrom(LinkedHashSet.class)) {
            return new LinkedHashSet();
        }
        if (rawClass.isAssignableFrom(TreeSet.class)) {
            return new TreeSet();
        }
        if (rawClass.isAssignableFrom(ArrayList.class)) {
            return new ArrayList();
        }
        if (rawClass.isAssignableFrom(EnumSet.class)) {
            if (type instanceof ParameterizedType) {
                type2 = ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                type2 = Object.class;
            }
            return EnumSet.noneOf((Class) type2);
        } else if (rawClass.isAssignableFrom(Queue.class) || ((cls = class_deque) != null && rawClass.isAssignableFrom(cls))) {
            return new LinkedList();
        } else {
            try {
                return (Collection) rawClass.newInstance();
            } catch (Exception unused) {
                throw new JSONException("create instance error, class " + rawClass.getName());
            }
        }
    }

    public static Class<?> getRawClass(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            return getRawClass(((ParameterizedType) type).getRawType());
        }
        if (type instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            if (upperBounds.length == 1) {
                return getRawClass(upperBounds[0]);
            }
            throw new JSONException("TODO");
        }
        throw new JSONException("TODO");
    }

    public static boolean isProxy(Class<?> cls) {
        for (Class name : cls.getInterfaces()) {
            if (isProxyClassNames.contains(name.getName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTransient(Method method) {
        if (method == null) {
            return false;
        }
        if (!transientClassInited) {
            try {
                transientClass = Class.forName("java.beans.Transient");
            } catch (Exception unused) {
            } catch (Throwable th) {
                transientClassInited = true;
                throw th;
            }
            transientClassInited = true;
        }
        Class<? extends Annotation> cls = transientClass;
        if (cls == null || getAnnotation(method, cls) == null) {
            return false;
        }
        return true;
    }

    public static boolean isAnnotationPresentOneToMany(Method method) {
        if (method == null) {
            return false;
        }
        if (class_OneToMany == null && !class_OneToMany_error) {
            try {
                class_OneToMany = Class.forName("javax.persistence.OneToMany");
            } catch (Throwable unused) {
                class_OneToMany_error = true;
            }
        }
        Class<? extends Annotation> cls = class_OneToMany;
        if (cls == null || !method.isAnnotationPresent(cls)) {
            return false;
        }
        return true;
    }

    public static boolean isAnnotationPresentManyToMany(Method method) {
        if (method == null) {
            return false;
        }
        if (class_ManyToMany == null && !class_ManyToMany_error) {
            try {
                class_ManyToMany = Class.forName("javax.persistence.ManyToMany");
            } catch (Throwable unused) {
                class_ManyToMany_error = true;
            }
        }
        if (class_ManyToMany == null) {
            return false;
        }
        if (method.isAnnotationPresent(class_OneToMany) || method.isAnnotationPresent(class_ManyToMany)) {
            return true;
        }
        return false;
    }

    public static boolean isHibernateInitialized(Object obj) {
        if (obj == null) {
            return false;
        }
        if (method_HibernateIsInitialized == null && !method_HibernateIsInitialized_error) {
            try {
                method_HibernateIsInitialized = Class.forName("org.hibernate.Hibernate").getMethod("isInitialized", new Class[]{Object.class});
            } catch (Throwable unused) {
                method_HibernateIsInitialized_error = true;
            }
        }
        Method method = method_HibernateIsInitialized;
        if (method != null) {
            try {
                return ((Boolean) method.invoke((Object) null, new Object[]{obj})).booleanValue();
            } catch (Throwable unused2) {
            }
        }
        return true;
    }

    public static double parseDouble(String str) {
        double d;
        double d2;
        int length = str.length();
        if (length > 10) {
            return Double.parseDouble(str);
        }
        long j = 0;
        boolean z = false;
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            char charAt = str.charAt(i2);
            if (charAt == '-' && i2 == 0) {
                z = true;
            } else if (charAt == '.') {
                if (i != 0) {
                    return Double.parseDouble(str);
                }
                i = (length - i2) - 1;
            } else if (charAt < '0' || charAt > '9') {
                return Double.parseDouble(str);
            } else {
                j = (j * 10) + ((long) (charAt - '0'));
            }
        }
        if (z) {
            j = -j;
        }
        switch (i) {
            case 0:
                return (double) j;
            case 1:
                d = (double) j;
                d2 = 10.0d;
                Double.isNaN(d);
                break;
            case 2:
                d = (double) j;
                d2 = 100.0d;
                Double.isNaN(d);
                break;
            case 3:
                d = (double) j;
                d2 = 1000.0d;
                Double.isNaN(d);
                break;
            case 4:
                d = (double) j;
                d2 = 10000.0d;
                Double.isNaN(d);
                break;
            case 5:
                d = (double) j;
                d2 = 100000.0d;
                Double.isNaN(d);
                break;
            case 6:
                d = (double) j;
                d2 = 1000000.0d;
                Double.isNaN(d);
                break;
            case 7:
                d = (double) j;
                d2 = 1.0E7d;
                Double.isNaN(d);
                break;
            case 8:
                d = (double) j;
                d2 = 1.0E8d;
                Double.isNaN(d);
                break;
            case 9:
                d = (double) j;
                d2 = 1.0E9d;
                Double.isNaN(d);
                break;
            default:
                return Double.parseDouble(str);
        }
        return d / d2;
    }

    public static float parseFloat(String str) {
        float f;
        float f2;
        int length = str.length();
        if (length >= 10) {
            return Float.parseFloat(str);
        }
        long j = 0;
        boolean z = false;
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            char charAt = str.charAt(i2);
            if (charAt == '-' && i2 == 0) {
                z = true;
            } else if (charAt == '.') {
                if (i != 0) {
                    return Float.parseFloat(str);
                }
                i = (length - i2) - 1;
            } else if (charAt < '0' || charAt > '9') {
                return Float.parseFloat(str);
            } else {
                j = (j * 10) + ((long) (charAt - '0'));
            }
        }
        if (z) {
            j = -j;
        }
        switch (i) {
            case 0:
                return (float) j;
            case 1:
                f = (float) j;
                f2 = 10.0f;
                break;
            case 2:
                f = (float) j;
                f2 = 100.0f;
                break;
            case 3:
                f = (float) j;
                f2 = 1000.0f;
                break;
            case 4:
                f = (float) j;
                f2 = 10000.0f;
                break;
            case 5:
                f = (float) j;
                f2 = 100000.0f;
                break;
            case 6:
                f = (float) j;
                f2 = 1000000.0f;
                break;
            case 7:
                f = (float) j;
                f2 = 1.0E7f;
                break;
            case 8:
                f = (float) j;
                f2 = 1.0E8f;
                break;
            case 9:
                f = (float) j;
                f2 = 1.0E9f;
                break;
            default:
                return Float.parseFloat(str);
        }
        return f / f2;
    }

    public static long fnv1a_64_extract(String str) {
        long j = fnv1a_64_magic_hashcode;
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (!(charAt == '_' || charAt == '-')) {
                if (charAt >= 'A' && charAt <= 'Z') {
                    charAt = (char) (charAt + ' ');
                }
                j = (j ^ ((long) charAt)) * fnv1a_64_magic_prime;
            }
        }
        return j;
    }

    public static long fnv1a_64_lower(String str) {
        long j = fnv1a_64_magic_hashcode;
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt >= 'A' && charAt <= 'Z') {
                charAt = (char) (charAt + ' ');
            }
            j = (j ^ ((long) charAt)) * fnv1a_64_magic_prime;
        }
        return j;
    }

    public static long fnv1a_64(String str) {
        long j = fnv1a_64_magic_hashcode;
        for (int i = 0; i < str.length(); i++) {
            j = (j ^ ((long) str.charAt(i))) * fnv1a_64_magic_prime;
        }
        return j;
    }

    public static boolean isKotlin(Class cls) {
        if (kotlin_metadata == null && !kotlin_metadata_error) {
            try {
                kotlin_metadata = Class.forName("kotlin.Metadata");
            } catch (Throwable unused) {
                kotlin_metadata_error = true;
            }
        }
        if (kotlin_metadata == null || !cls.isAnnotationPresent(kotlin_metadata)) {
            return false;
        }
        return true;
    }

    public static Constructor getKotlinConstructor(Constructor[] constructorArr) {
        return getKotlinConstructor(constructorArr, (String[]) null);
    }

    public static Constructor getKotlinConstructor(Constructor[] constructorArr, String[] strArr) {
        Constructor constructor = null;
        for (Constructor constructor2 : constructorArr) {
            Class[] parameterTypes = constructor2.getParameterTypes();
            if ((strArr == null || parameterTypes.length == strArr.length) && ((parameterTypes.length <= 0 || !parameterTypes[parameterTypes.length - 1].getName().equals("kotlin.jvm.internal.DefaultConstructorMarker")) && (constructor == null || constructor.getParameterTypes().length < parameterTypes.length))) {
                constructor = constructor2;
            }
        }
        return constructor;
    }

    public static String[] getKoltinConstructorParameters(Class cls) {
        if (kotlin_kclass_constructor == null && !kotlin_class_klass_error) {
            try {
                kotlin_kclass_constructor = Class.forName("kotlin.reflect.jvm.internal.KClassImpl").getConstructor(new Class[]{Class.class});
            } catch (Throwable unused) {
                kotlin_class_klass_error = true;
            }
        }
        if (kotlin_kclass_constructor == null) {
            return null;
        }
        if (kotlin_kclass_getConstructors == null && !kotlin_class_klass_error) {
            try {
                kotlin_kclass_getConstructors = Class.forName("kotlin.reflect.jvm.internal.KClassImpl").getMethod("getConstructors", new Class[0]);
            } catch (Throwable unused2) {
                kotlin_class_klass_error = true;
            }
        }
        if (kotlin_kfunction_getParameters == null && !kotlin_class_klass_error) {
            try {
                kotlin_kfunction_getParameters = Class.forName("kotlin.reflect.KFunction").getMethod("getParameters", new Class[0]);
            } catch (Throwable unused3) {
                kotlin_class_klass_error = true;
            }
        }
        if (kotlin_kparameter_getName == null && !kotlin_class_klass_error) {
            try {
                kotlin_kparameter_getName = Class.forName("kotlin.reflect.KParameter").getMethod("getName", new Class[0]);
            } catch (Throwable unused4) {
                kotlin_class_klass_error = true;
            }
        }
        if (kotlin_error) {
            return null;
        }
        try {
            Iterator it = ((Iterable) kotlin_kclass_getConstructors.invoke(kotlin_kclass_constructor.newInstance(new Object[]{cls}), new Object[0])).iterator();
            Object obj = null;
            while (it.hasNext()) {
                Object next = it.next();
                List list = (List) kotlin_kfunction_getParameters.invoke(next, new Object[0]);
                if (obj == null || list.size() != 0) {
                    obj = next;
                }
                it.hasNext();
            }
            if (obj == null) {
                return null;
            }
            List list2 = (List) kotlin_kfunction_getParameters.invoke(obj, new Object[0]);
            String[] strArr = new String[list2.size()];
            for (int i = 0; i < list2.size(); i++) {
                strArr[i] = (String) kotlin_kparameter_getName.invoke(list2.get(i), new Object[0]);
            }
            return strArr;
        } catch (Throwable th) {
            th.printStackTrace();
            kotlin_error = true;
            return null;
        }
    }

    private static boolean isKotlinIgnore(Class cls, String str) {
        String[] strArr;
        if (kotlinIgnores == null && !kotlinIgnores_error) {
            try {
                HashMap hashMap = new HashMap();
                hashMap.put(Class.forName("kotlin.ranges.CharRange"), new String[]{"getEndInclusive", "isEmpty"});
                hashMap.put(Class.forName("kotlin.ranges.IntRange"), new String[]{"getEndInclusive", "isEmpty"});
                hashMap.put(Class.forName("kotlin.ranges.LongRange"), new String[]{"getEndInclusive", "isEmpty"});
                hashMap.put(Class.forName("kotlin.ranges.ClosedFloatRange"), new String[]{"getEndInclusive", "isEmpty"});
                hashMap.put(Class.forName("kotlin.ranges.ClosedDoubleRange"), new String[]{"getEndInclusive", "isEmpty"});
                kotlinIgnores = hashMap;
            } catch (Throwable unused) {
                kotlinIgnores_error = true;
            }
        }
        if (kotlinIgnores == null || (strArr = kotlinIgnores.get(cls)) == null || Arrays.binarySearch(strArr, str) < 0) {
            return false;
        }
        return true;
    }

    public static <A extends Annotation> A getAnnotation(Class<?> cls, Class<A> cls2) {
        A annotation = cls.getAnnotation(cls2);
        Type mixInAnnotations = JSON.getMixInAnnotations(cls);
        Class cls3 = mixInAnnotations instanceof Class ? (Class) mixInAnnotations : null;
        if (cls3 != null) {
            A annotation2 = cls3.getAnnotation(cls2);
            Annotation[] annotations = cls3.getAnnotations();
            if (annotation2 == null && annotations.length > 0) {
                for (Annotation annotationType : annotations) {
                    annotation2 = annotationType.annotationType().getAnnotation(cls2);
                    if (annotation2 != null) {
                        break;
                    }
                }
            }
            if (annotation2 != null) {
                return annotation2;
            }
        }
        Annotation[] annotations2 = cls.getAnnotations();
        if (annotation == null && annotations2.length > 0) {
            for (Annotation annotationType2 : annotations2) {
                annotation = annotationType2.annotationType().getAnnotation(cls2);
                if (annotation != null) {
                    break;
                }
            }
        }
        return annotation;
    }

    public static <A extends Annotation> A getAnnotation(Field field, Class<A> cls) {
        A annotation;
        A annotation2 = field.getAnnotation(cls);
        Type mixInAnnotations = JSON.getMixInAnnotations(field.getDeclaringClass());
        Field field2 = null;
        Class<? super Object> cls2 = mixInAnnotations instanceof Class ? (Class) mixInAnnotations : null;
        if (cls2 != null) {
            String name = field.getName();
            while (true) {
                if (cls2 == null || cls2 == Object.class) {
                    break;
                }
                try {
                    field2 = cls2.getDeclaredField(name);
                    break;
                } catch (NoSuchFieldException unused) {
                    cls2 = cls2.getSuperclass();
                }
            }
            if (!(field2 == null || (annotation = field2.getAnnotation(cls)) == null)) {
                return annotation;
            }
        }
        return annotation2;
    }

    public static <A extends Annotation> A getAnnotation(Method method, Class<A> cls) {
        A annotation;
        A annotation2 = method.getAnnotation(cls);
        Type mixInAnnotations = JSON.getMixInAnnotations(method.getDeclaringClass());
        Method method2 = null;
        Class<? super Object> cls2 = mixInAnnotations instanceof Class ? (Class) mixInAnnotations : null;
        if (cls2 != null) {
            String name = method.getName();
            Class[] parameterTypes = method.getParameterTypes();
            while (true) {
                if (cls2 == null || cls2 == Object.class) {
                    break;
                }
                try {
                    method2 = cls2.getDeclaredMethod(name, parameterTypes);
                    break;
                } catch (NoSuchMethodException unused) {
                    cls2 = cls2.getSuperclass();
                }
            }
            if (!(method2 == null || (annotation = method2.getAnnotation(cls)) == null)) {
                return annotation;
            }
        }
        return annotation2;
    }

    public static Annotation[][] getParameterAnnotations(Method method) {
        Annotation[][] parameterAnnotations;
        Annotation[][] parameterAnnotations2 = method.getParameterAnnotations();
        Type mixInAnnotations = JSON.getMixInAnnotations(method.getDeclaringClass());
        Method method2 = null;
        Class<? super Object> cls = mixInAnnotations instanceof Class ? (Class) mixInAnnotations : null;
        if (cls != null) {
            String name = method.getName();
            Class[] parameterTypes = method.getParameterTypes();
            while (true) {
                if (cls == null || cls == Object.class) {
                    break;
                }
                try {
                    method2 = cls.getDeclaredMethod(name, parameterTypes);
                    break;
                } catch (NoSuchMethodException unused) {
                    cls = cls.getSuperclass();
                }
            }
            if (!(method2 == null || (parameterAnnotations = method2.getParameterAnnotations()) == null)) {
                return parameterAnnotations;
            }
        }
        return parameterAnnotations2;
    }

    public static Annotation[][] getParameterAnnotations(Constructor constructor) {
        Annotation[][] parameterAnnotations;
        Constructor<Object> declaredConstructor;
        Annotation[][] parameterAnnotations2 = constructor.getParameterAnnotations();
        Type mixInAnnotations = JSON.getMixInAnnotations(constructor.getDeclaringClass());
        Constructor<Object> constructor2 = null;
        Class<Object> cls = mixInAnnotations instanceof Class ? (Class) mixInAnnotations : null;
        if (cls != null) {
            Class[] parameterTypes = constructor.getParameterTypes();
            ArrayList arrayList = new ArrayList(2);
            for (Class<?> enclosingClass = cls.getEnclosingClass(); enclosingClass != null; enclosingClass = enclosingClass.getEnclosingClass()) {
                arrayList.add(enclosingClass);
            }
            int size = arrayList.size();
            Class<? super Object> cls2 = cls;
            while (true) {
                if (cls2 != null && cls2 != Object.class) {
                    if (size == 0) {
                        declaredConstructor = cls.getDeclaredConstructor(parameterTypes);
                        break;
                    }
                    try {
                        Class[] clsArr = new Class[(parameterTypes.length + size)];
                        System.arraycopy(parameterTypes, 0, clsArr, size, parameterTypes.length);
                        for (int i = size; i > 0; i--) {
                            int i2 = i - 1;
                            clsArr[i2] = (Class) arrayList.get(i2);
                        }
                        declaredConstructor = cls.getDeclaredConstructor(clsArr);
                    } catch (NoSuchMethodException unused) {
                        size--;
                        cls2 = cls2.getSuperclass();
                    }
                } else {
                    break;
                }
            }
            constructor2 = declaredConstructor;
            if (!(constructor2 == null || (parameterAnnotations = constructor2.getParameterAnnotations()) == null)) {
                return parameterAnnotations;
            }
        }
        return parameterAnnotations2;
    }

    public static boolean isJacksonCreator(Method method) {
        if (method == null) {
            return false;
        }
        if (class_JacksonCreator == null && !class_JacksonCreator_error) {
            try {
                class_JacksonCreator = Class.forName("com.fasterxml.jackson.annotation.JsonCreator");
            } catch (Throwable unused) {
                class_JacksonCreator_error = true;
            }
        }
        Class<? extends Annotation> cls = class_JacksonCreator;
        if (cls == null || !method.isAnnotationPresent(cls)) {
            return false;
        }
        return true;
    }

    public static Object optionalEmpty(Type type) {
        Class<?> cls;
        if (OPTIONAL_ERROR || (cls = getClass(type)) == null) {
            return null;
        }
        String name = cls.getName();
        if (!"java.util.Optional".equals(name)) {
            return null;
        }
        if (OPTIONAL_EMPTY == null) {
            try {
                OPTIONAL_EMPTY = Class.forName(name).getMethod("empty", new Class[0]).invoke((Object) null, new Object[0]);
            } catch (Throwable unused) {
                OPTIONAL_ERROR = true;
            }
        }
        return OPTIONAL_EMPTY;
    }

    public static class MethodInheritanceComparator implements Comparator<Method> {
        public int compare(Method method, Method method2) {
            int compareTo = method.getName().compareTo(method2.getName());
            if (compareTo != 0) {
                return compareTo;
            }
            Class<?> returnType = method.getReturnType();
            Class<?> returnType2 = method2.getReturnType();
            if (returnType.equals(returnType2)) {
                return 0;
            }
            if (returnType.isAssignableFrom(returnType2)) {
                return -1;
            }
            if (returnType2.isAssignableFrom(returnType)) {
                return 1;
            }
            return 0;
        }
    }
}
