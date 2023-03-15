package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.alibaba.fastjson.util.TypeUtils;
import com.taobao.weex.el.parse.Operators;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class JavaBeanDeserializer implements ObjectDeserializer {
    private final Map<String, FieldDeserializer> alterNameFieldDeserializers;
    private final ParserConfig.AutoTypeCheckHandler autoTypeCheckHandler;
    public final JavaBeanInfo beanInfo;
    protected final Class<?> clazz;
    private ConcurrentMap<String, Object> extraFieldDeserializers;
    private Map<String, FieldDeserializer> fieldDeserializerMap;
    private final FieldDeserializer[] fieldDeserializers;
    private transient long[] hashArray;
    private transient short[] hashArrayMapping;
    private transient long[] smartMatchHashArray;
    private transient short[] smartMatchHashArrayMapping;
    protected final FieldDeserializer[] sortedFieldDeserializers;

    public int getFastMatchToken() {
        return 12;
    }

    public JavaBeanDeserializer(ParserConfig parserConfig, Class<?> cls) {
        this(parserConfig, cls, cls);
    }

    public JavaBeanDeserializer(ParserConfig parserConfig, Class<?> cls, Type type) {
        this(parserConfig, JavaBeanInfo.build(cls, type, parserConfig.propertyNamingStrategy, parserConfig.fieldBased, parserConfig.compatibleWithJavaBean, parserConfig.isJacksonCompatible()));
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0036  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x007e A[LOOP:2: B:24:0x007c->B:25:0x007e, LOOP_END] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public JavaBeanDeserializer(com.alibaba.fastjson.parser.ParserConfig r10, com.alibaba.fastjson.util.JavaBeanInfo r11) {
        /*
            r9 = this;
            r9.<init>()
            java.lang.Class<?> r0 = r11.clazz
            r9.clazz = r0
            r9.beanInfo = r11
            com.alibaba.fastjson.annotation.JSONType r0 = r11.jsonType
            r1 = 0
            if (r0 == 0) goto L_0x0025
            com.alibaba.fastjson.annotation.JSONType r0 = r11.jsonType
            java.lang.Class r0 = r0.autoTypeCheckHandler()
            java.lang.Class<com.alibaba.fastjson.parser.ParserConfig$AutoTypeCheckHandler> r2 = com.alibaba.fastjson.parser.ParserConfig.AutoTypeCheckHandler.class
            if (r0 == r2) goto L_0x0025
            com.alibaba.fastjson.annotation.JSONType r0 = r11.jsonType     // Catch:{ Exception -> 0x0025 }
            java.lang.Class r0 = r0.autoTypeCheckHandler()     // Catch:{ Exception -> 0x0025 }
            java.lang.Object r0 = r0.newInstance()     // Catch:{ Exception -> 0x0025 }
            com.alibaba.fastjson.parser.ParserConfig$AutoTypeCheckHandler r0 = (com.alibaba.fastjson.parser.ParserConfig.AutoTypeCheckHandler) r0     // Catch:{ Exception -> 0x0025 }
            goto L_0x0026
        L_0x0025:
            r0 = r1
        L_0x0026:
            r9.autoTypeCheckHandler = r0
            com.alibaba.fastjson.util.FieldInfo[] r0 = r11.sortedFields
            int r0 = r0.length
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer[] r0 = new com.alibaba.fastjson.parser.deserializer.FieldDeserializer[r0]
            r9.sortedFieldDeserializers = r0
            com.alibaba.fastjson.util.FieldInfo[] r0 = r11.sortedFields
            int r0 = r0.length
            r2 = 0
            r3 = 0
        L_0x0034:
            if (r3 >= r0) goto L_0x0070
            com.alibaba.fastjson.util.FieldInfo[] r4 = r11.sortedFields
            r4 = r4[r3]
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer r5 = r10.createFieldDeserializer(r10, r11, r4)
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer[] r6 = r9.sortedFieldDeserializers
            r6[r3] = r5
            r6 = 128(0x80, float:1.794E-43)
            if (r0 <= r6) goto L_0x0058
            java.util.Map<java.lang.String, com.alibaba.fastjson.parser.deserializer.FieldDeserializer> r6 = r9.fieldDeserializerMap
            if (r6 != 0) goto L_0x0051
            java.util.HashMap r6 = new java.util.HashMap
            r6.<init>()
            r9.fieldDeserializerMap = r6
        L_0x0051:
            java.util.Map<java.lang.String, com.alibaba.fastjson.parser.deserializer.FieldDeserializer> r6 = r9.fieldDeserializerMap
            java.lang.String r7 = r4.name
            r6.put(r7, r5)
        L_0x0058:
            java.lang.String[] r4 = r4.alternateNames
            int r6 = r4.length
            r7 = 0
        L_0x005c:
            if (r7 >= r6) goto L_0x006d
            r8 = r4[r7]
            if (r1 != 0) goto L_0x0067
            java.util.HashMap r1 = new java.util.HashMap
            r1.<init>()
        L_0x0067:
            r1.put(r8, r5)
            int r7 = r7 + 1
            goto L_0x005c
        L_0x006d:
            int r3 = r3 + 1
            goto L_0x0034
        L_0x0070:
            r9.alterNameFieldDeserializers = r1
            com.alibaba.fastjson.util.FieldInfo[] r10 = r11.fields
            int r10 = r10.length
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer[] r10 = new com.alibaba.fastjson.parser.deserializer.FieldDeserializer[r10]
            r9.fieldDeserializers = r10
            com.alibaba.fastjson.util.FieldInfo[] r10 = r11.fields
            int r10 = r10.length
        L_0x007c:
            if (r2 >= r10) goto L_0x008f
            com.alibaba.fastjson.util.FieldInfo[] r0 = r11.fields
            r0 = r0[r2]
            java.lang.String r0 = r0.name
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer r0 = r9.getFieldDeserializer((java.lang.String) r0)
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer[] r1 = r9.fieldDeserializers
            r1[r2] = r0
            int r2 = r2 + 1
            goto L_0x007c
        L_0x008f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.<init>(com.alibaba.fastjson.parser.ParserConfig, com.alibaba.fastjson.util.JavaBeanInfo):void");
    }

    public FieldDeserializer getFieldDeserializer(String str) {
        return getFieldDeserializer(str, (int[]) null);
    }

    public FieldDeserializer getFieldDeserializer(String str, int[] iArr) {
        FieldDeserializer fieldDeserializer;
        if (str == null) {
            return null;
        }
        Map<String, FieldDeserializer> map = this.fieldDeserializerMap;
        if (map != null && (fieldDeserializer = map.get(str)) != null) {
            return fieldDeserializer;
        }
        int i = 0;
        int length = this.sortedFieldDeserializers.length - 1;
        while (i <= length) {
            int i2 = (i + length) >>> 1;
            int compareTo = this.sortedFieldDeserializers[i2].fieldInfo.name.compareTo(str);
            if (compareTo < 0) {
                i = i2 + 1;
            } else if (compareTo > 0) {
                length = i2 - 1;
            } else if (isSetFlag(i2, iArr)) {
                return null;
            } else {
                return this.sortedFieldDeserializers[i2];
            }
        }
        Map<String, FieldDeserializer> map2 = this.alterNameFieldDeserializers;
        if (map2 != null) {
            return map2.get(str);
        }
        return null;
    }

    public FieldDeserializer getFieldDeserializer(long j) {
        int i = 0;
        if (this.hashArray == null) {
            long[] jArr = new long[this.sortedFieldDeserializers.length];
            int i2 = 0;
            while (true) {
                FieldDeserializer[] fieldDeserializerArr = this.sortedFieldDeserializers;
                if (i2 >= fieldDeserializerArr.length) {
                    break;
                }
                jArr[i2] = TypeUtils.fnv1a_64(fieldDeserializerArr[i2].fieldInfo.name);
                i2++;
            }
            Arrays.sort(jArr);
            this.hashArray = jArr;
        }
        int binarySearch = Arrays.binarySearch(this.hashArray, j);
        if (binarySearch < 0) {
            return null;
        }
        if (this.hashArrayMapping == null) {
            short[] sArr = new short[this.hashArray.length];
            Arrays.fill(sArr, -1);
            while (true) {
                FieldDeserializer[] fieldDeserializerArr2 = this.sortedFieldDeserializers;
                if (i >= fieldDeserializerArr2.length) {
                    break;
                }
                int binarySearch2 = Arrays.binarySearch(this.hashArray, TypeUtils.fnv1a_64(fieldDeserializerArr2[i].fieldInfo.name));
                if (binarySearch2 >= 0) {
                    sArr[binarySearch2] = (short) i;
                }
                i++;
            }
            this.hashArrayMapping = sArr;
        }
        short s = this.hashArrayMapping[binarySearch];
        if (s != -1) {
            return this.sortedFieldDeserializers[s];
        }
        return null;
    }

    static boolean isSetFlag(int i, int[] iArr) {
        int i2;
        if (iArr == null || (i2 = i / 32) >= iArr.length) {
            return false;
        }
        if (((1 << (i % 32)) & iArr[i2]) != 0) {
            return true;
        }
        return false;
    }

    public Object createInstance(DefaultJSONParser defaultJSONParser, Type type) {
        Object obj;
        if (!(type instanceof Class) || !this.clazz.isInterface()) {
            Object obj2 = null;
            if (this.beanInfo.defaultConstructor == null && this.beanInfo.factoryMethod == null) {
                return null;
            }
            if (this.beanInfo.factoryMethod != null && this.beanInfo.defaultConstructorParameterSize > 0) {
                return null;
            }
            try {
                Constructor<?> constructor = this.beanInfo.defaultConstructor;
                if (this.beanInfo.defaultConstructorParameterSize != 0) {
                    ParseContext context = defaultJSONParser.getContext();
                    if (context != null) {
                        if (context.object != null) {
                            if (type instanceof Class) {
                                String name = ((Class) type).getName();
                                String substring = name.substring(0, name.lastIndexOf(36));
                                Object obj3 = context.object;
                                String name2 = obj3.getClass().getName();
                                if (!name2.equals(substring)) {
                                    ParseContext parseContext = context.parent;
                                    if (parseContext == null || parseContext.object == null || (!"java.util.ArrayList".equals(name2) && !"java.util.List".equals(name2) && !"java.util.Collection".equals(name2) && !"java.util.Map".equals(name2) && !"java.util.HashMap".equals(name2))) {
                                        obj2 = obj3;
                                    } else if (parseContext.object.getClass().getName().equals(substring)) {
                                        obj2 = parseContext.object;
                                    }
                                    obj3 = obj2;
                                }
                                if (obj3 == null || ((obj3 instanceof Collection) && ((Collection) obj3).isEmpty())) {
                                    throw new JSONException("can't create non-static inner class instance.");
                                }
                                obj = constructor.newInstance(new Object[]{obj3});
                            } else {
                                throw new JSONException("can't create non-static inner class instance.");
                            }
                        }
                    }
                    throw new JSONException("can't create non-static inner class instance.");
                } else if (constructor != null) {
                    obj = constructor.newInstance(new Object[0]);
                } else {
                    obj = this.beanInfo.factoryMethod.invoke((Object) null, new Object[0]);
                }
                if (defaultJSONParser != null && defaultJSONParser.lexer.isEnabled(Feature.InitStringFieldAsEmpty)) {
                    for (FieldInfo fieldInfo : this.beanInfo.fields) {
                        if (fieldInfo.fieldClass == String.class) {
                            try {
                                fieldInfo.set(obj, "");
                            } catch (Exception e) {
                                throw new JSONException("create instance error, class " + this.clazz.getName(), e);
                            }
                        }
                    }
                }
                return obj;
            } catch (JSONException e2) {
                throw e2;
            } catch (Exception e3) {
                throw new JSONException("create instance error, class " + this.clazz.getName(), e3);
            }
        } else {
            return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{(Class) type}, new JSONObject());
        }
    }

    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        return deserialze(defaultJSONParser, type, obj, 0);
    }

    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj, int i) {
        return deserialze(defaultJSONParser, type, obj, (Object) null, i, (int[]) null);
    }

    public <T> T deserialzeArrayMapping(DefaultJSONParser defaultJSONParser, Type type, Object obj, Object obj2) {
        Enum<?> enumR;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        if (jSONLexer.token() == 14) {
            String scanTypeName = jSONLexer.scanTypeName(defaultJSONParser.symbolTable);
            if (scanTypeName != null) {
                Object seeAlso = getSeeAlso(defaultJSONParser.getConfig(), this.beanInfo, scanTypeName);
                if (seeAlso == null) {
                    seeAlso = defaultJSONParser.getConfig().getDeserializer((Type) defaultJSONParser.getConfig().checkAutoType(scanTypeName, TypeUtils.getClass(type), jSONLexer.getFeatures()));
                }
                if (seeAlso instanceof JavaBeanDeserializer) {
                    return ((JavaBeanDeserializer) seeAlso).deserialzeArrayMapping(defaultJSONParser, type, obj, obj2);
                }
            }
            T createInstance = createInstance(defaultJSONParser, type);
            int i = 0;
            int length = this.sortedFieldDeserializers.length;
            while (true) {
                int i2 = 16;
                if (i >= length) {
                    break;
                }
                char c = i == length + -1 ? Operators.ARRAY_END : Operators.ARRAY_SEPRATOR;
                FieldDeserializer fieldDeserializer = this.sortedFieldDeserializers[i];
                Class<?> cls = fieldDeserializer.fieldInfo.fieldClass;
                if (cls == Integer.TYPE) {
                    fieldDeserializer.setValue((Object) createInstance, jSONLexer.scanInt(c));
                } else if (cls == String.class) {
                    fieldDeserializer.setValue((Object) createInstance, jSONLexer.scanString(c));
                } else if (cls == Long.TYPE) {
                    fieldDeserializer.setValue((Object) createInstance, jSONLexer.scanLong(c));
                } else if (cls.isEnum()) {
                    char current = jSONLexer.getCurrent();
                    if (current == '\"' || current == 'n') {
                        enumR = jSONLexer.scanEnum(cls, defaultJSONParser.getSymbolTable(), c);
                    } else if (current < '0' || current > '9') {
                        enumR = scanEnum(jSONLexer, c);
                    } else {
                        enumR = ((EnumDeserializer) ((DefaultFieldDeserializer) fieldDeserializer).getFieldValueDeserilizer(defaultJSONParser.getConfig())).valueOf(jSONLexer.scanInt(c));
                    }
                    fieldDeserializer.setValue((Object) createInstance, (Object) enumR);
                } else if (cls == Boolean.TYPE) {
                    fieldDeserializer.setValue((Object) createInstance, jSONLexer.scanBoolean(c));
                } else if (cls == Float.TYPE) {
                    fieldDeserializer.setValue((Object) createInstance, (Object) Float.valueOf(jSONLexer.scanFloat(c)));
                } else if (cls == Double.TYPE) {
                    fieldDeserializer.setValue((Object) createInstance, (Object) Double.valueOf(jSONLexer.scanDouble(c)));
                } else if (cls == Date.class && jSONLexer.getCurrent() == '1') {
                    fieldDeserializer.setValue((Object) createInstance, (Object) new Date(jSONLexer.scanLong(c)));
                } else if (cls == BigDecimal.class) {
                    fieldDeserializer.setValue((Object) createInstance, (Object) jSONLexer.scanDecimal(c));
                } else {
                    jSONLexer.nextToken(14);
                    fieldDeserializer.setValue((Object) createInstance, defaultJSONParser.parseObject(fieldDeserializer.fieldInfo.fieldType, (Object) fieldDeserializer.fieldInfo.name));
                    if (jSONLexer.token() == 15) {
                        break;
                    }
                    if (c == ']') {
                        i2 = 15;
                    }
                    check(jSONLexer, i2);
                }
                i++;
            }
            jSONLexer.nextToken(16);
            return createInstance;
        }
        throw new JSONException("error");
    }

    /* access modifiers changed from: protected */
    public void check(JSONLexer jSONLexer, int i) {
        if (jSONLexer.token() != i) {
            throw new JSONException("syntax error");
        }
    }

    /* access modifiers changed from: protected */
    public Enum<?> scanEnum(JSONLexer jSONLexer, char c) {
        throw new JSONException("illegal enum. " + jSONLexer.info());
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:101:0x012c, code lost:
        throw new com.alibaba.fastjson.JSONException(r1.getMessage(), r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0049, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x004a, code lost:
        r14 = r35;
        r1 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:314:0x038f, code lost:
        if (r12.matchStat == -2) goto L_0x0391;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:336:0x03da, code lost:
        if (r12.isEnabled(com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas) != false) goto L_0x03dc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:345:?, code lost:
        r12.nextTokenWithColon(4);
        r1 = r12.token();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:346:0x03f6, code lost:
        if (r1 != 4) goto L_0x04ab;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:347:0x03f8, code lost:
        r1 = r12.stringVal();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:348:0x0402, code lost:
        if ("@".equals(r1) == false) goto L_0x0408;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:349:0x0404, code lost:
        r1 = r7.object;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:351:0x040e, code lost:
        if (io.dcloud.common.util.PdrUtil.FILE_PATH_ENTRY_BACK.equals(r1) == false) goto L_0x0426;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:352:0x0410, code lost:
        r2 = r7.parent;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:353:0x0414, code lost:
        if (r2.object == null) goto L_0x041a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:354:0x0416, code lost:
        r1 = r2.object;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:355:0x041a, code lost:
        r9.addResolveTask(new com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask(r2, r1));
        r9.resolveStatus = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:357:0x042c, code lost:
        if (com.taobao.weex.el.parse.Operators.DOLLAR_STR.equals(r1) == false) goto L_0x0449;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:358:0x042e, code lost:
        r2 = r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:360:0x0431, code lost:
        if (r2.parent == null) goto L_0x0436;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:361:0x0433, code lost:
        r2 = r2.parent;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:363:0x0438, code lost:
        if (r2.object == null) goto L_0x043d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:364:0x043a, code lost:
        r1 = r2.object;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:365:0x043d, code lost:
        r9.addResolveTask(new com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask(r2, r1));
        r9.resolveStatus = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:367:0x044f, code lost:
        if (r1.indexOf(92) <= 0) goto L_0x0473;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:368:0x0451, code lost:
        r3 = new java.lang.StringBuilder();
        r4 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:370:0x045b, code lost:
        if (r4 >= r1.length()) goto L_0x046f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:371:0x045d, code lost:
        r6 = r1.charAt(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:372:0x0461, code lost:
        if (r6 != '\\') goto L_0x0469;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:373:0x0463, code lost:
        r4 = r4 + 1;
        r6 = r1.charAt(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:374:0x0469, code lost:
        r3.append(r6);
        r4 = r4 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:375:0x046f, code lost:
        r1 = r3.toString();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:376:0x0473, code lost:
        r2 = r9.resolveReference(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:377:0x0477, code lost:
        if (r2 == null) goto L_0x047b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:378:0x0479, code lost:
        r1 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:379:0x047b, code lost:
        r9.addResolveTask(new com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask(r7, r1));
        r9.resolveStatus = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:380:0x0486, code lost:
        r1 = r27;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:383:?, code lost:
        r12.nextToken(13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:384:0x0491, code lost:
        if (r12.token() != 13) goto L_0x04a3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:385:0x0493, code lost:
        r12.nextToken(16);
        r9.setContext(r7, r1, r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:386:0x049b, code lost:
        if (r5 == null) goto L_0x049f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:387:0x049d, code lost:
        r5.object = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:388:0x049f, code lost:
        r9.setContext(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:389:0x04a2, code lost:
        return r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:392:0x04aa, code lost:
        throw new com.alibaba.fastjson.JSONException("illegal ref");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:395:0x04c5, code lost:
        throw new com.alibaba.fastjson.JSONException("illegal ref, " + com.alibaba.fastjson.parser.JSONToken.name(r1));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:427:0x053f, code lost:
        r2 = r32.getConfig().getDeserializer(r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:430:?, code lost:
        r3 = r2.deserialze(r9, r15, r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:431:0x054f, code lost:
        if ((r2 instanceof com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) == false) goto L_0x055e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:433:?, code lost:
        r2 = (com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:434:0x0553, code lost:
        if (r6 == null) goto L_0x055e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:435:0x0555, code lost:
        r2 = r2.getFieldDeserializer(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:436:0x0559, code lost:
        if (r2 == null) goto L_0x055e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:437:0x055b, code lost:
        r2.setValue((java.lang.Object) r3, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:438:0x055e, code lost:
        if (r5 == null) goto L_0x0564;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:439:0x0560, code lost:
        r5.object = r27;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:440:0x0564, code lost:
        r9.setContext(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:441:0x0567, code lost:
        return r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:542:0x06f4, code lost:
        r1 = r19;
        r5 = r27;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:745:0x09c3, code lost:
        throw new com.alibaba.fastjson.JSONException("syntax error, unexpect token " + com.alibaba.fastjson.parser.JSONToken.name(r12.token()));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x0121, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x0122, code lost:
        r1 = r0;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:14:0x0039, B:81:0x00e6] */
    /* JADX WARNING: Removed duplicated region for block: B:266:0x030b A[Catch:{ all -> 0x03e2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:324:0x03b3 A[SYNTHETIC, Splitter:B:324:0x03b3] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0071 A[Catch:{ Exception -> 0x0121, all -> 0x0049 }] */
    /* JADX WARNING: Removed duplicated region for block: B:404:0x04dc A[Catch:{ all -> 0x05a4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:452:0x059f  */
    /* JADX WARNING: Removed duplicated region for block: B:456:0x05ab  */
    /* JADX WARNING: Removed duplicated region for block: B:462:0x05be A[Catch:{ all -> 0x05ea }] */
    /* JADX WARNING: Removed duplicated region for block: B:466:0x05cb A[Catch:{ all -> 0x0691 }] */
    /* JADX WARNING: Removed duplicated region for block: B:469:0x05d3 A[SYNTHETIC, Splitter:B:469:0x05d3] */
    /* JADX WARNING: Removed duplicated region for block: B:479:0x05f7  */
    /* JADX WARNING: Removed duplicated region for block: B:515:0x0680 A[Catch:{ all -> 0x0691 }] */
    /* JADX WARNING: Removed duplicated region for block: B:538:0x06e7 A[Catch:{ all -> 0x09d0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:539:0x06eb A[Catch:{ all -> 0x09d0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:666:0x087e A[SYNTHETIC, Splitter:B:666:0x087e] */
    /* JADX WARNING: Removed duplicated region for block: B:687:0x08bd A[SYNTHETIC, Splitter:B:687:0x08bd] */
    /* JADX WARNING: Removed duplicated region for block: B:759:0x09e5  */
    /* JADX WARNING: Removed duplicated region for block: B:768:0x0595 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> T deserialze(com.alibaba.fastjson.parser.DefaultJSONParser r32, java.lang.reflect.Type r33, java.lang.Object r34, java.lang.Object r35, int r36, int[] r37) {
        /*
            r31 = this;
            r8 = r31
            r9 = r32
            r10 = r33
            r11 = r34
            java.lang.Class<com.alibaba.fastjson.JSON> r1 = com.alibaba.fastjson.JSON.class
            if (r10 == r1) goto L_0x09eb
            java.lang.Class<com.alibaba.fastjson.JSONObject> r1 = com.alibaba.fastjson.JSONObject.class
            if (r10 != r1) goto L_0x0012
            goto L_0x09eb
        L_0x0012:
            com.alibaba.fastjson.parser.JSONLexer r1 = r9.lexer
            r12 = r1
            com.alibaba.fastjson.parser.JSONLexerBase r12 = (com.alibaba.fastjson.parser.JSONLexerBase) r12
            com.alibaba.fastjson.parser.ParserConfig r13 = r32.getConfig()
            int r1 = r12.token()
            r2 = 8
            r14 = 16
            r15 = 0
            if (r1 != r2) goto L_0x002a
            r12.nextToken(r14)
            return r15
        L_0x002a:
            com.alibaba.fastjson.parser.ParseContext r2 = r32.getContext()
            if (r35 == 0) goto L_0x0034
            if (r2 == 0) goto L_0x0034
            com.alibaba.fastjson.parser.ParseContext r2 = r2.parent
        L_0x0034:
            r7 = r2
            r6 = 13
            if (r1 != r6) goto L_0x0050
            r12.nextToken(r14)     // Catch:{ all -> 0x0049 }
            if (r35 != 0) goto L_0x0043
            java.lang.Object r1 = r31.createInstance((com.alibaba.fastjson.parser.DefaultJSONParser) r32, (java.lang.reflect.Type) r33)     // Catch:{ all -> 0x0049 }
            goto L_0x0045
        L_0x0043:
            r1 = r35
        L_0x0045:
            r9.setContext(r7)
            return r1
        L_0x0049:
            r0 = move-exception
            r14 = r35
            r1 = r0
        L_0x004d:
            r3 = r7
            goto L_0x09e3
        L_0x0050:
            r2 = 14
            if (r1 != r2) goto L_0x0079
            com.alibaba.fastjson.parser.Feature r3 = com.alibaba.fastjson.parser.Feature.SupportArrayToBean     // Catch:{ all -> 0x0049 }
            int r3 = r3.mask     // Catch:{ all -> 0x0049 }
            com.alibaba.fastjson.util.JavaBeanInfo r6 = r8.beanInfo     // Catch:{ all -> 0x0049 }
            int r6 = r6.parserFeatures     // Catch:{ all -> 0x0049 }
            r6 = r6 & r3
            if (r6 != 0) goto L_0x006e
            com.alibaba.fastjson.parser.Feature r6 = com.alibaba.fastjson.parser.Feature.SupportArrayToBean     // Catch:{ all -> 0x0049 }
            boolean r6 = r12.isEnabled((com.alibaba.fastjson.parser.Feature) r6)     // Catch:{ all -> 0x0049 }
            if (r6 != 0) goto L_0x006e
            r3 = r36 & r3
            if (r3 == 0) goto L_0x006c
            goto L_0x006e
        L_0x006c:
            r3 = 0
            goto L_0x006f
        L_0x006e:
            r3 = 1
        L_0x006f:
            if (r3 == 0) goto L_0x0079
            java.lang.Object r1 = r31.deserialzeArrayMapping(r32, r33, r34, r35)     // Catch:{ all -> 0x0049 }
            r9.setContext(r7)
            return r1
        L_0x0079:
            r3 = 12
            r6 = 4
            if (r1 == r3) goto L_0x016a
            if (r1 == r14) goto L_0x016a
            boolean r3 = r12.isBlankInput()     // Catch:{ all -> 0x0049 }
            if (r3 == 0) goto L_0x008a
            r9.setContext(r7)
            return r15
        L_0x008a:
            if (r1 != r6) goto L_0x00c4
            java.lang.String r3 = r12.stringVal()     // Catch:{ all -> 0x0049 }
            int r10 = r3.length()     // Catch:{ all -> 0x0049 }
            if (r10 != 0) goto L_0x009d
            r12.nextToken()     // Catch:{ all -> 0x0049 }
            r9.setContext(r7)
            return r15
        L_0x009d:
            com.alibaba.fastjson.util.JavaBeanInfo r10 = r8.beanInfo     // Catch:{ all -> 0x0049 }
            com.alibaba.fastjson.annotation.JSONType r10 = r10.jsonType     // Catch:{ all -> 0x0049 }
            if (r10 == 0) goto L_0x00c4
            com.alibaba.fastjson.util.JavaBeanInfo r10 = r8.beanInfo     // Catch:{ all -> 0x0049 }
            com.alibaba.fastjson.annotation.JSONType r10 = r10.jsonType     // Catch:{ all -> 0x0049 }
            java.lang.Class[] r10 = r10.seeAlso()     // Catch:{ all -> 0x0049 }
            int r14 = r10.length     // Catch:{ all -> 0x0049 }
            r6 = 0
        L_0x00ad:
            if (r6 >= r14) goto L_0x00c4
            r5 = r10[r6]     // Catch:{ all -> 0x0049 }
            java.lang.Class<java.lang.Enum> r4 = java.lang.Enum.class
            boolean r4 = r4.isAssignableFrom(r5)     // Catch:{ all -> 0x0049 }
            if (r4 == 0) goto L_0x00c1
            java.lang.Enum r1 = java.lang.Enum.valueOf(r5, r3)     // Catch:{ IllegalArgumentException -> 0x00c1 }
            r9.setContext(r7)
            return r1
        L_0x00c1:
            int r6 = r6 + 1
            goto L_0x00ad
        L_0x00c4:
            if (r1 != r2) goto L_0x00d8
            char r2 = r12.getCurrent()     // Catch:{ all -> 0x0049 }
            r3 = 93
            if (r2 != r3) goto L_0x00d8
            r12.next()     // Catch:{ all -> 0x0049 }
            r12.nextToken()     // Catch:{ all -> 0x0049 }
            r9.setContext(r7)
            return r15
        L_0x00d8:
            com.alibaba.fastjson.util.JavaBeanInfo r2 = r8.beanInfo     // Catch:{ all -> 0x0049 }
            java.lang.reflect.Method r2 = r2.factoryMethod     // Catch:{ all -> 0x0049 }
            if (r2 == 0) goto L_0x012d
            com.alibaba.fastjson.util.JavaBeanInfo r2 = r8.beanInfo     // Catch:{ all -> 0x0049 }
            com.alibaba.fastjson.util.FieldInfo[] r2 = r2.fields     // Catch:{ all -> 0x0049 }
            int r2 = r2.length     // Catch:{ all -> 0x0049 }
            r3 = 1
            if (r2 != r3) goto L_0x012d
            com.alibaba.fastjson.util.JavaBeanInfo r2 = r8.beanInfo     // Catch:{ Exception -> 0x0121 }
            com.alibaba.fastjson.util.FieldInfo[] r2 = r2.fields     // Catch:{ Exception -> 0x0121 }
            r3 = 0
            r2 = r2[r3]     // Catch:{ Exception -> 0x0121 }
            java.lang.Class<?> r3 = r2.fieldClass     // Catch:{ Exception -> 0x0121 }
            java.lang.Class<java.lang.Integer> r4 = java.lang.Integer.class
            if (r3 != r4) goto L_0x0109
            r3 = 2
            if (r1 != r3) goto L_0x012d
            int r1 = r12.intValue()     // Catch:{ Exception -> 0x0121 }
            r12.nextToken()     // Catch:{ Exception -> 0x0121 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ Exception -> 0x0121 }
            java.lang.Object r1 = r8.createFactoryInstance(r13, r1)     // Catch:{ Exception -> 0x0121 }
            r9.setContext(r7)
            return r1
        L_0x0109:
            java.lang.Class<?> r2 = r2.fieldClass     // Catch:{ Exception -> 0x0121 }
            java.lang.Class<java.lang.String> r3 = java.lang.String.class
            if (r2 != r3) goto L_0x012d
            r2 = 4
            if (r1 != r2) goto L_0x012d
            java.lang.String r1 = r12.stringVal()     // Catch:{ Exception -> 0x0121 }
            r12.nextToken()     // Catch:{ Exception -> 0x0121 }
            java.lang.Object r1 = r8.createFactoryInstance(r13, r1)     // Catch:{ Exception -> 0x0121 }
            r9.setContext(r7)
            return r1
        L_0x0121:
            r0 = move-exception
            r1 = r0
            com.alibaba.fastjson.JSONException r2 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0049 }
            java.lang.String r3 = r1.getMessage()     // Catch:{ all -> 0x0049 }
            r2.<init>(r3, r1)     // Catch:{ all -> 0x0049 }
            throw r2     // Catch:{ all -> 0x0049 }
        L_0x012d:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0049 }
            r1.<init>()     // Catch:{ all -> 0x0049 }
            java.lang.String r2 = "syntax error, expect {, actual "
            r1.append(r2)     // Catch:{ all -> 0x0049 }
            java.lang.String r2 = r12.tokenName()     // Catch:{ all -> 0x0049 }
            r1.append(r2)     // Catch:{ all -> 0x0049 }
            java.lang.String r2 = ", pos "
            r1.append(r2)     // Catch:{ all -> 0x0049 }
            int r2 = r12.pos()     // Catch:{ all -> 0x0049 }
            r1.append(r2)     // Catch:{ all -> 0x0049 }
            boolean r2 = r11 instanceof java.lang.String     // Catch:{ all -> 0x0049 }
            if (r2 == 0) goto L_0x0156
            java.lang.String r2 = ", fieldName "
            r1.append(r2)     // Catch:{ all -> 0x0049 }
            r1.append(r11)     // Catch:{ all -> 0x0049 }
        L_0x0156:
            java.lang.String r2 = ", fastjson-version "
            r1.append(r2)     // Catch:{ all -> 0x0049 }
            java.lang.String r2 = "1.2.83"
            r1.append(r2)     // Catch:{ all -> 0x0049 }
            com.alibaba.fastjson.JSONException r2 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0049 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0049 }
            r2.<init>(r1)     // Catch:{ all -> 0x0049 }
            throw r2     // Catch:{ all -> 0x0049 }
        L_0x016a:
            int r1 = r9.resolveStatus     // Catch:{ all -> 0x09dd }
            r2 = 2
            if (r1 != r2) goto L_0x0173
            r5 = 0
            r9.resolveStatus = r5     // Catch:{ all -> 0x0049 }
            goto L_0x0174
        L_0x0173:
            r5 = 0
        L_0x0174:
            com.alibaba.fastjson.util.JavaBeanInfo r1 = r8.beanInfo     // Catch:{ all -> 0x09dd }
            java.lang.String r6 = r1.typeKey     // Catch:{ all -> 0x09dd }
            r1 = r35
            r2 = r37
            r5 = r15
            r17 = r5
            r3 = 0
            r4 = 0
        L_0x0181:
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer[] r15 = r8.sortedFieldDeserializers     // Catch:{ all -> 0x09d4 }
            int r14 = r15.length     // Catch:{ all -> 0x09d4 }
            if (r4 >= r14) goto L_0x01b3
            r14 = 16
            if (r3 >= r14) goto L_0x01b3
            r14 = r15[r4]     // Catch:{ all -> 0x01ad }
            com.alibaba.fastjson.util.FieldInfo r15 = r14.fieldInfo     // Catch:{ all -> 0x01ad }
            r35 = r4
            java.lang.Class<?> r4 = r15.fieldClass     // Catch:{ all -> 0x01ad }
            com.alibaba.fastjson.annotation.JSONField r21 = r15.getAnnotation()     // Catch:{ all -> 0x01ad }
            if (r21 == 0) goto L_0x01a5
            r37 = r4
            boolean r4 = r14 instanceof com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer     // Catch:{ all -> 0x01ad }
            if (r4 == 0) goto L_0x01a7
            r4 = r14
            com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer r4 = (com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer) r4     // Catch:{ all -> 0x01ad }
            boolean r4 = r4.customDeserilizer     // Catch:{ all -> 0x01ad }
            r10 = r15
            goto L_0x01a9
        L_0x01a5:
            r37 = r4
        L_0x01a7:
            r10 = r15
            r4 = 0
        L_0x01a9:
            r15 = r14
            r14 = r37
            goto L_0x01bb
        L_0x01ad:
            r0 = move-exception
            r14 = r1
            r15 = r5
            r3 = r7
            goto L_0x09e2
        L_0x01b3:
            r35 = r4
            r4 = 0
            r10 = 0
            r14 = 0
            r15 = 0
            r21 = 0
        L_0x01bb:
            r22 = 0
            r24 = 0
            r25 = 0
            if (r15 == 0) goto L_0x03a8
            r37 = r2
            char[] r2 = r10.name_chars     // Catch:{ all -> 0x039e }
            if (r4 == 0) goto L_0x01d6
            boolean r4 = r12.matchField((char[]) r2)     // Catch:{ all -> 0x01ad }
            if (r4 == 0) goto L_0x01d6
            r27 = r1
        L_0x01d1:
            r29 = r10
            r1 = 1
            goto L_0x03af
        L_0x01d6:
            java.lang.Class r4 = java.lang.Integer.TYPE     // Catch:{ all -> 0x039e }
            r27 = r1
            r1 = -2
            if (r14 == r4) goto L_0x0372
            java.lang.Class<java.lang.Integer> r4 = java.lang.Integer.class
            if (r14 != r4) goto L_0x01e3
            goto L_0x0372
        L_0x01e3:
            java.lang.Class r4 = java.lang.Long.TYPE     // Catch:{ all -> 0x03e2 }
            if (r14 == r4) goto L_0x0351
            java.lang.Class<java.lang.Long> r4 = java.lang.Long.class
            if (r14 != r4) goto L_0x01ed
            goto L_0x0351
        L_0x01ed:
            java.lang.Class<java.lang.String> r4 = java.lang.String.class
            if (r14 != r4) goto L_0x020a
            java.lang.String r2 = r12.scanFieldString(r2)     // Catch:{ all -> 0x03e2 }
            int r4 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            if (r4 <= 0) goto L_0x01ff
        L_0x01f9:
            r29 = r10
        L_0x01fb:
            r1 = 1
            r4 = 1
            goto L_0x03b1
        L_0x01ff:
            int r4 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            if (r4 != r1) goto L_0x0205
            goto L_0x0391
        L_0x0205:
            r29 = r10
        L_0x0207:
            r1 = 0
            goto L_0x03b0
        L_0x020a:
            java.lang.Class<java.util.Date> r4 = java.util.Date.class
            if (r14 != r4) goto L_0x0221
            java.lang.String r4 = r10.format     // Catch:{ all -> 0x03e2 }
            if (r4 != 0) goto L_0x0221
            java.util.Date r2 = r12.scanFieldDate(r2)     // Catch:{ all -> 0x03e2 }
            int r4 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            if (r4 <= 0) goto L_0x021b
            goto L_0x01f9
        L_0x021b:
            int r4 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            if (r4 != r1) goto L_0x0205
            goto L_0x0391
        L_0x0221:
            java.lang.Class<java.math.BigDecimal> r4 = java.math.BigDecimal.class
            if (r14 != r4) goto L_0x0234
            java.math.BigDecimal r2 = r12.scanFieldDecimal(r2)     // Catch:{ all -> 0x03e2 }
            int r4 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            if (r4 <= 0) goto L_0x022e
            goto L_0x01f9
        L_0x022e:
            int r4 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            if (r4 != r1) goto L_0x0205
            goto L_0x0391
        L_0x0234:
            java.lang.Class<java.math.BigInteger> r4 = java.math.BigInteger.class
            if (r14 != r4) goto L_0x0247
            java.math.BigInteger r2 = r12.scanFieldBigInteger(r2)     // Catch:{ all -> 0x03e2 }
            int r4 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            if (r4 <= 0) goto L_0x0241
            goto L_0x01f9
        L_0x0241:
            int r4 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            if (r4 != r1) goto L_0x0205
            goto L_0x0391
        L_0x0247:
            java.lang.Class r4 = java.lang.Boolean.TYPE     // Catch:{ all -> 0x03e2 }
            if (r14 == r4) goto L_0x0334
            java.lang.Class<java.lang.Boolean> r4 = java.lang.Boolean.class
            if (r14 != r4) goto L_0x0251
            goto L_0x0334
        L_0x0251:
            java.lang.Class r4 = java.lang.Float.TYPE     // Catch:{ all -> 0x03e2 }
            if (r14 == r4) goto L_0x0312
            java.lang.Class<java.lang.Float> r4 = java.lang.Float.class
            if (r14 != r4) goto L_0x025b
            goto L_0x0312
        L_0x025b:
            java.lang.Class r4 = java.lang.Double.TYPE     // Catch:{ all -> 0x03e2 }
            if (r14 == r4) goto L_0x02ed
            java.lang.Class<java.lang.Double> r4 = java.lang.Double.class
            if (r14 != r4) goto L_0x0265
            goto L_0x02ed
        L_0x0265:
            boolean r4 = r14.isEnum()     // Catch:{ all -> 0x03e2 }
            if (r4 == 0) goto L_0x02a2
            com.alibaba.fastjson.parser.ParserConfig r4 = r32.getConfig()     // Catch:{ all -> 0x03e2 }
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r4 = r4.getDeserializer((java.lang.reflect.Type) r14)     // Catch:{ all -> 0x03e2 }
            boolean r4 = r4 instanceof com.alibaba.fastjson.parser.deserializer.EnumDeserializer     // Catch:{ all -> 0x03e2 }
            if (r4 == 0) goto L_0x02a2
            if (r21 == 0) goto L_0x0281
            java.lang.Class r4 = r21.deserializeUsing()     // Catch:{ all -> 0x03e2 }
            java.lang.Class<java.lang.Void> r1 = java.lang.Void.class
            if (r4 != r1) goto L_0x02a2
        L_0x0281:
            boolean r1 = r15 instanceof com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer     // Catch:{ all -> 0x03e2 }
            if (r1 == 0) goto L_0x03ac
            r1 = r15
            com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer r1 = (com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer) r1     // Catch:{ all -> 0x03e2 }
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r1 = r1.fieldValueDeserilizer     // Catch:{ all -> 0x03e2 }
            java.lang.Enum r2 = r8.scanEnum(r12, r2, r1)     // Catch:{ all -> 0x03e2 }
            int r1 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            if (r1 <= 0) goto L_0x0295
            r1 = 1
            r4 = 1
            goto L_0x029e
        L_0x0295:
            int r1 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            r4 = -2
            if (r1 != r4) goto L_0x029c
            goto L_0x0391
        L_0x029c:
            r1 = 0
            r4 = 0
        L_0x029e:
            r29 = r10
            goto L_0x03b1
        L_0x02a2:
            java.lang.Class<int[]> r1 = int[].class
            if (r14 != r1) goto L_0x02b7
            int[] r2 = r12.scanFieldIntArray(r2)     // Catch:{ all -> 0x03e2 }
            int r1 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            if (r1 <= 0) goto L_0x02b0
            goto L_0x01f9
        L_0x02b0:
            int r1 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            r4 = -2
            if (r1 != r4) goto L_0x0205
            goto L_0x0391
        L_0x02b7:
            java.lang.Class<float[]> r1 = float[].class
            if (r14 != r1) goto L_0x02cc
            float[] r2 = r12.scanFieldFloatArray(r2)     // Catch:{ all -> 0x03e2 }
            int r1 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            if (r1 <= 0) goto L_0x02c5
            goto L_0x01f9
        L_0x02c5:
            int r1 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            r4 = -2
            if (r1 != r4) goto L_0x0205
            goto L_0x0391
        L_0x02cc:
            java.lang.Class<float[][]> r1 = float[][].class
            if (r14 != r1) goto L_0x02e1
            float[][] r2 = r12.scanFieldFloatArray2(r2)     // Catch:{ all -> 0x03e2 }
            int r1 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            if (r1 <= 0) goto L_0x02da
            goto L_0x01f9
        L_0x02da:
            int r1 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            r4 = -2
            if (r1 != r4) goto L_0x0205
            goto L_0x0391
        L_0x02e1:
            boolean r1 = r12.matchField((char[]) r2)     // Catch:{ all -> 0x03e2 }
            if (r1 == 0) goto L_0x02e9
            goto L_0x01d1
        L_0x02e9:
            r21 = r3
            goto L_0x03dc
        L_0x02ed:
            double r1 = r12.scanFieldDouble(r2)     // Catch:{ all -> 0x03e2 }
            int r4 = (r1 > r22 ? 1 : (r1 == r22 ? 0 : -1))
            if (r4 != 0) goto L_0x02fe
            int r4 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            r29 = r10
            r10 = 5
            if (r4 != r10) goto L_0x0300
            r2 = 0
            goto L_0x0305
        L_0x02fe:
            r29 = r10
        L_0x0300:
            java.lang.Double r1 = java.lang.Double.valueOf(r1)     // Catch:{ all -> 0x03e2 }
            r2 = r1
        L_0x0305:
            int r1 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            if (r1 <= 0) goto L_0x030b
        L_0x0309:
            goto L_0x01fb
        L_0x030b:
            int r1 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            r4 = -2
            if (r1 != r4) goto L_0x0207
            goto L_0x0391
        L_0x0312:
            r29 = r10
            float r1 = r12.scanFieldFloat(r2)     // Catch:{ all -> 0x03e2 }
            int r2 = (r1 > r24 ? 1 : (r1 == r24 ? 0 : -1))
            if (r2 != 0) goto L_0x0323
            int r2 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            r4 = 5
            if (r2 != r4) goto L_0x0323
            r2 = 0
            goto L_0x0328
        L_0x0323:
            java.lang.Float r1 = java.lang.Float.valueOf(r1)     // Catch:{ all -> 0x03e2 }
            r2 = r1
        L_0x0328:
            int r1 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            if (r1 <= 0) goto L_0x032d
            goto L_0x0309
        L_0x032d:
            int r1 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            r4 = -2
            if (r1 != r4) goto L_0x0207
            goto L_0x0391
        L_0x0334:
            r29 = r10
            boolean r1 = r12.scanFieldBoolean(r2)     // Catch:{ all -> 0x03e2 }
            int r2 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            r4 = 5
            if (r2 != r4) goto L_0x0341
            r2 = 0
            goto L_0x0346
        L_0x0341:
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ all -> 0x03e2 }
            r2 = r1
        L_0x0346:
            int r1 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            if (r1 <= 0) goto L_0x034b
            goto L_0x0309
        L_0x034b:
            int r1 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            r4 = -2
            if (r1 != r4) goto L_0x0207
            goto L_0x0391
        L_0x0351:
            r29 = r10
            long r1 = r12.scanFieldLong(r2)     // Catch:{ all -> 0x03e2 }
            int r4 = (r1 > r25 ? 1 : (r1 == r25 ? 0 : -1))
            if (r4 != 0) goto L_0x0362
            int r4 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            r10 = 5
            if (r4 != r10) goto L_0x0362
            r2 = 0
            goto L_0x0367
        L_0x0362:
            java.lang.Long r1 = java.lang.Long.valueOf(r1)     // Catch:{ all -> 0x03e2 }
            r2 = r1
        L_0x0367:
            int r1 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            if (r1 <= 0) goto L_0x036c
            goto L_0x0309
        L_0x036c:
            int r1 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            r4 = -2
            if (r1 != r4) goto L_0x0207
            goto L_0x0391
        L_0x0372:
            r29 = r10
            int r1 = r12.scanFieldInt(r2)     // Catch:{ all -> 0x03e2 }
            if (r1 != 0) goto L_0x0381
            int r2 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            r4 = 5
            if (r2 != r4) goto L_0x0381
            r2 = 0
            goto L_0x0386
        L_0x0381:
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x03e2 }
            r2 = r1
        L_0x0386:
            int r1 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            if (r1 <= 0) goto L_0x038c
            goto L_0x0309
        L_0x038c:
            int r1 = r12.matchStat     // Catch:{ all -> 0x03e2 }
            r4 = -2
            if (r1 != r4) goto L_0x0207
        L_0x0391:
            int r3 = r3 + 1
            r10 = r35
            r16 = r3
            r20 = r6
            r3 = r7
            r19 = r17
            goto L_0x0588
        L_0x039e:
            r0 = move-exception
            r27 = r1
        L_0x03a1:
            r1 = r0
            r15 = r5
            r3 = r7
            r14 = r27
            goto L_0x09e3
        L_0x03a8:
            r27 = r1
            r37 = r2
        L_0x03ac:
            r29 = r10
            r1 = 0
        L_0x03af:
            r2 = 0
        L_0x03b0:
            r4 = 0
        L_0x03b1:
            if (r1 != 0) goto L_0x05ab
            com.alibaba.fastjson.parser.SymbolTable r10 = r9.symbolTable     // Catch:{ all -> 0x05a4 }
            java.lang.String r10 = r12.scanSymbol(r10)     // Catch:{ all -> 0x05a4 }
            if (r10 != 0) goto L_0x03e4
            r21 = r3
            int r3 = r12.token()     // Catch:{ all -> 0x03e2 }
            r28 = r14
            r14 = 13
            if (r3 != r14) goto L_0x03d0
            r14 = 16
            r12.nextToken(r14)     // Catch:{ all -> 0x03e2 }
        L_0x03cc:
            r14 = r27
            goto L_0x0575
        L_0x03d0:
            r14 = 16
            if (r3 != r14) goto L_0x03e8
            com.alibaba.fastjson.parser.Feature r3 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas     // Catch:{ all -> 0x03e2 }
            boolean r3 = r12.isEnabled((com.alibaba.fastjson.parser.Feature) r3)     // Catch:{ all -> 0x03e2 }
            if (r3 == 0) goto L_0x03e8
        L_0x03dc:
            r14 = r27
            r3 = 13
            goto L_0x057d
        L_0x03e2:
            r0 = move-exception
            goto L_0x03a1
        L_0x03e4:
            r21 = r3
            r28 = r14
        L_0x03e8:
            java.lang.String r3 = "$ref"
            if (r3 != r10) goto L_0x04c6
            if (r7 == 0) goto L_0x04c6
            r3 = 4
            r12.nextTokenWithColon(r3)     // Catch:{ all -> 0x03e2 }
            int r1 = r12.token()     // Catch:{ all -> 0x03e2 }
            if (r1 != r3) goto L_0x04ab
            java.lang.String r1 = r12.stringVal()     // Catch:{ all -> 0x03e2 }
            java.lang.String r2 = "@"
            boolean r2 = r2.equals(r1)     // Catch:{ all -> 0x03e2 }
            if (r2 == 0) goto L_0x0408
            java.lang.Object r1 = r7.object     // Catch:{ all -> 0x03e2 }
            goto L_0x0488
        L_0x0408:
            java.lang.String r2 = ".."
            boolean r2 = r2.equals(r1)     // Catch:{ all -> 0x03e2 }
            if (r2 == 0) goto L_0x0426
            com.alibaba.fastjson.parser.ParseContext r2 = r7.parent     // Catch:{ all -> 0x03e2 }
            java.lang.Object r3 = r2.object     // Catch:{ all -> 0x03e2 }
            if (r3 == 0) goto L_0x041a
            java.lang.Object r1 = r2.object     // Catch:{ all -> 0x03e2 }
            goto L_0x0488
        L_0x041a:
            com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask r3 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask     // Catch:{ all -> 0x03e2 }
            r3.<init>(r2, r1)     // Catch:{ all -> 0x03e2 }
            r9.addResolveTask(r3)     // Catch:{ all -> 0x03e2 }
            r1 = 1
            r9.resolveStatus = r1     // Catch:{ all -> 0x03e2 }
            goto L_0x0486
        L_0x0426:
            java.lang.String r2 = "$"
            boolean r2 = r2.equals(r1)     // Catch:{ all -> 0x03e2 }
            if (r2 == 0) goto L_0x0449
            r2 = r7
        L_0x042f:
            com.alibaba.fastjson.parser.ParseContext r3 = r2.parent     // Catch:{ all -> 0x03e2 }
            if (r3 == 0) goto L_0x0436
            com.alibaba.fastjson.parser.ParseContext r2 = r2.parent     // Catch:{ all -> 0x03e2 }
            goto L_0x042f
        L_0x0436:
            java.lang.Object r3 = r2.object     // Catch:{ all -> 0x03e2 }
            if (r3 == 0) goto L_0x043d
            java.lang.Object r1 = r2.object     // Catch:{ all -> 0x03e2 }
            goto L_0x0488
        L_0x043d:
            com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask r3 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask     // Catch:{ all -> 0x03e2 }
            r3.<init>(r2, r1)     // Catch:{ all -> 0x03e2 }
            r9.addResolveTask(r3)     // Catch:{ all -> 0x03e2 }
            r1 = 1
            r9.resolveStatus = r1     // Catch:{ all -> 0x03e2 }
            goto L_0x0486
        L_0x0449:
            r2 = 92
            int r3 = r1.indexOf(r2)     // Catch:{ all -> 0x03e2 }
            if (r3 <= 0) goto L_0x0473
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x03e2 }
            r3.<init>()     // Catch:{ all -> 0x03e2 }
            r4 = 0
        L_0x0457:
            int r6 = r1.length()     // Catch:{ all -> 0x03e2 }
            if (r4 >= r6) goto L_0x046f
            char r6 = r1.charAt(r4)     // Catch:{ all -> 0x03e2 }
            if (r6 != r2) goto L_0x0469
            int r4 = r4 + 1
            char r6 = r1.charAt(r4)     // Catch:{ all -> 0x03e2 }
        L_0x0469:
            r3.append(r6)     // Catch:{ all -> 0x03e2 }
            r6 = 1
            int r4 = r4 + r6
            goto L_0x0457
        L_0x046f:
            java.lang.String r1 = r3.toString()     // Catch:{ all -> 0x03e2 }
        L_0x0473:
            java.lang.Object r2 = r9.resolveReference(r1)     // Catch:{ all -> 0x03e2 }
            if (r2 == 0) goto L_0x047b
            r1 = r2
            goto L_0x0488
        L_0x047b:
            com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask r2 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask     // Catch:{ all -> 0x03e2 }
            r2.<init>(r7, r1)     // Catch:{ all -> 0x03e2 }
            r9.addResolveTask(r2)     // Catch:{ all -> 0x03e2 }
            r1 = 1
            r9.resolveStatus = r1     // Catch:{ all -> 0x03e2 }
        L_0x0486:
            r1 = r27
        L_0x0488:
            r2 = 13
            r12.nextToken(r2)     // Catch:{ all -> 0x01ad }
            int r3 = r12.token()     // Catch:{ all -> 0x01ad }
            if (r3 != r2) goto L_0x04a3
            r2 = 16
            r12.nextToken(r2)     // Catch:{ all -> 0x01ad }
            r9.setContext(r7, r1, r11)     // Catch:{ all -> 0x01ad }
            if (r5 == 0) goto L_0x049f
            r5.object = r1
        L_0x049f:
            r9.setContext(r7)
            return r1
        L_0x04a3:
            com.alibaba.fastjson.JSONException r2 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x01ad }
            java.lang.String r3 = "illegal ref"
            r2.<init>(r3)     // Catch:{ all -> 0x01ad }
            throw r2     // Catch:{ all -> 0x01ad }
        L_0x04ab:
            com.alibaba.fastjson.JSONException r2 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x03e2 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x03e2 }
            r3.<init>()     // Catch:{ all -> 0x03e2 }
            java.lang.String r4 = "illegal ref, "
            r3.append(r4)     // Catch:{ all -> 0x03e2 }
            java.lang.String r1 = com.alibaba.fastjson.parser.JSONToken.name(r1)     // Catch:{ all -> 0x03e2 }
            r3.append(r1)     // Catch:{ all -> 0x03e2 }
            java.lang.String r1 = r3.toString()     // Catch:{ all -> 0x03e2 }
            r2.<init>(r1)     // Catch:{ all -> 0x03e2 }
            throw r2     // Catch:{ all -> 0x03e2 }
        L_0x04c6:
            if (r6 == 0) goto L_0x04ce
            boolean r3 = r6.equals(r10)     // Catch:{ all -> 0x03e2 }
            if (r3 != 0) goto L_0x04d2
        L_0x04ce:
            java.lang.String r3 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY     // Catch:{ all -> 0x05a4 }
            if (r3 != r10) goto L_0x059f
        L_0x04d2:
            r1 = 4
            r12.nextTokenWithColon(r1)     // Catch:{ all -> 0x05a4 }
            int r2 = r12.token()     // Catch:{ all -> 0x05a4 }
            if (r2 != r1) goto L_0x0595
            java.lang.String r1 = r12.stringVal()     // Catch:{ all -> 0x05a4 }
            r2 = 16
            r12.nextToken(r2)     // Catch:{ all -> 0x05a4 }
            com.alibaba.fastjson.util.JavaBeanInfo r2 = r8.beanInfo     // Catch:{ all -> 0x05a4 }
            java.lang.String r2 = r2.typeName     // Catch:{ all -> 0x05a4 }
            boolean r2 = r1.equals(r2)     // Catch:{ all -> 0x05a4 }
            if (r2 != 0) goto L_0x0568
            com.alibaba.fastjson.parser.Feature r2 = com.alibaba.fastjson.parser.Feature.IgnoreAutoType     // Catch:{ all -> 0x05a4 }
            boolean r2 = r9.isEnabled(r2)     // Catch:{ all -> 0x05a4 }
            if (r2 == 0) goto L_0x04f9
            goto L_0x0568
        L_0x04f9:
            com.alibaba.fastjson.util.JavaBeanInfo r2 = r8.beanInfo     // Catch:{ all -> 0x05a4 }
            com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer r2 = getSeeAlso(r13, r2, r1)     // Catch:{ all -> 0x05a4 }
            if (r2 != 0) goto L_0x0548
            java.lang.Class r2 = com.alibaba.fastjson.util.TypeUtils.getClass(r33)     // Catch:{ all -> 0x03e2 }
            com.alibaba.fastjson.parser.ParserConfig$AutoTypeCheckHandler r3 = r8.autoTypeCheckHandler     // Catch:{ all -> 0x03e2 }
            if (r3 == 0) goto L_0x0512
            int r4 = r12.getFeatures()     // Catch:{ all -> 0x03e2 }
            java.lang.Class r3 = r3.handler(r1, r2, r4)     // Catch:{ all -> 0x03e2 }
            goto L_0x0513
        L_0x0512:
            r3 = 0
        L_0x0513:
            if (r3 != 0) goto L_0x0532
            java.lang.String r4 = "java.util.HashMap"
            boolean r4 = r1.equals(r4)     // Catch:{ all -> 0x03e2 }
            if (r4 != 0) goto L_0x0525
            java.lang.String r4 = "java.util.LinkedHashMap"
            boolean r4 = r1.equals(r4)     // Catch:{ all -> 0x03e2 }
            if (r4 == 0) goto L_0x0532
        L_0x0525:
            int r1 = r12.token()     // Catch:{ all -> 0x03e2 }
            r2 = 13
            if (r1 != r2) goto L_0x03dc
            r12.nextToken()     // Catch:{ all -> 0x03e2 }
            goto L_0x03cc
        L_0x0532:
            if (r3 != 0) goto L_0x053e
            int r3 = r12.getFeatures()     // Catch:{ all -> 0x03e2 }
            java.lang.Class r2 = r13.checkAutoType(r1, r2, r3)     // Catch:{ all -> 0x03e2 }
            r15 = r2
            goto L_0x053f
        L_0x053e:
            r15 = r3
        L_0x053f:
            com.alibaba.fastjson.parser.ParserConfig r2 = r32.getConfig()     // Catch:{ all -> 0x03e2 }
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r2 = r2.getDeserializer((java.lang.reflect.Type) r15)     // Catch:{ all -> 0x03e2 }
            goto L_0x0549
        L_0x0548:
            r15 = 0
        L_0x0549:
            java.lang.Object r3 = r2.deserialze(r9, r15, r11)     // Catch:{ all -> 0x05a4 }
            boolean r4 = r2 instanceof com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer     // Catch:{ all -> 0x05a4 }
            if (r4 == 0) goto L_0x055e
            com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer r2 = (com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) r2     // Catch:{ all -> 0x03e2 }
            if (r6 == 0) goto L_0x055e
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer r2 = r2.getFieldDeserializer((java.lang.String) r6)     // Catch:{ all -> 0x03e2 }
            if (r2 == 0) goto L_0x055e
            r2.setValue((java.lang.Object) r3, (java.lang.String) r1)     // Catch:{ all -> 0x03e2 }
        L_0x055e:
            if (r5 == 0) goto L_0x0564
            r14 = r27
            r5.object = r14
        L_0x0564:
            r9.setContext(r7)
            return r3
        L_0x0568:
            r14 = r27
            int r1 = r12.token()     // Catch:{ all -> 0x05e5 }
            r3 = 13
            if (r1 != r3) goto L_0x057d
            r12.nextToken()     // Catch:{ all -> 0x05e5 }
        L_0x0575:
            r30 = r7
            r1 = r17
            r35 = 0
            goto L_0x06f8
        L_0x057d:
            r10 = r35
            r20 = r6
            r3 = r7
            r27 = r14
            r19 = r17
            r16 = r21
        L_0x0588:
            r1 = 0
            r2 = 16
            r4 = 0
            r6 = 1
            r15 = 13
            r21 = 4
            r17 = r37
            goto L_0x0991
        L_0x0595:
            r14 = r27
            com.alibaba.fastjson.JSONException r1 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x05e5 }
            java.lang.String r2 = "syntax error"
            r1.<init>(r2)     // Catch:{ all -> 0x05e5 }
            throw r1     // Catch:{ all -> 0x05e5 }
        L_0x059f:
            r14 = r27
            r3 = 13
            goto L_0x05b4
        L_0x05a4:
            r0 = move-exception
            r14 = r27
        L_0x05a7:
            r1 = r0
            r15 = r5
            goto L_0x004d
        L_0x05ab:
            r21 = r3
            r28 = r14
            r14 = r27
            r3 = 13
            r10 = 0
        L_0x05b4:
            if (r14 != 0) goto L_0x05e7
            if (r17 != 0) goto L_0x05e7
            java.lang.Object r14 = r31.createInstance((com.alibaba.fastjson.parser.DefaultJSONParser) r32, (java.lang.reflect.Type) r33)     // Catch:{ all -> 0x05ea }
            if (r14 != 0) goto L_0x05cb
            java.util.HashMap r3 = new java.util.HashMap     // Catch:{ all -> 0x05ea }
            r27 = r5
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer[] r5 = r8.fieldDeserializers     // Catch:{ all -> 0x0691 }
            int r5 = r5.length     // Catch:{ all -> 0x0691 }
            r3.<init>(r5)     // Catch:{ all -> 0x0691 }
            r17 = r3
            goto L_0x05cd
        L_0x05cb:
            r27 = r5
        L_0x05cd:
            com.alibaba.fastjson.parser.ParseContext r5 = r9.setContext(r7, r14, r11)     // Catch:{ all -> 0x0691 }
            if (r37 != 0) goto L_0x05e7
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer[] r3 = r8.fieldDeserializers     // Catch:{ all -> 0x05e5 }
            int r3 = r3.length     // Catch:{ all -> 0x05e5 }
            int r3 = r3 / 32
            r20 = 1
            int r3 = r3 + 1
            int[] r3 = new int[r3]     // Catch:{ all -> 0x05e5 }
            r27 = r5
            r5 = r17
            r17 = r3
            goto L_0x05f5
        L_0x05e5:
            r0 = move-exception
            goto L_0x05a7
        L_0x05e7:
            r27 = r5
            goto L_0x05f1
        L_0x05ea:
            r0 = move-exception
            r27 = r5
        L_0x05ed:
            r1 = r0
            r3 = r7
            goto L_0x09da
        L_0x05f1:
            r5 = r17
            r17 = r37
        L_0x05f5:
            if (r1 == 0) goto L_0x0680
            if (r4 != 0) goto L_0x0610
            r4 = r33
            r15.parseField(r9, r14, r4, r5)     // Catch:{ all -> 0x0691 }
        L_0x05fe:
            r10 = r35
            r19 = r5
            r20 = r6
            r30 = r7
            r16 = r21
            r35 = 0
            r15 = 13
            r21 = 4
            goto L_0x06df
        L_0x0610:
            r4 = r33
            r1 = r29
            if (r14 != 0) goto L_0x061c
            java.lang.String r1 = r1.name     // Catch:{ all -> 0x0691 }
            r5.put(r1, r2)     // Catch:{ all -> 0x0691 }
            goto L_0x0663
        L_0x061c:
            if (r2 != 0) goto L_0x0638
            java.lang.Class r1 = java.lang.Integer.TYPE     // Catch:{ all -> 0x0691 }
            r3 = r28
            if (r3 == r1) goto L_0x0663
            java.lang.Class r1 = java.lang.Long.TYPE     // Catch:{ all -> 0x0691 }
            if (r3 == r1) goto L_0x0663
            java.lang.Class r1 = java.lang.Float.TYPE     // Catch:{ all -> 0x0691 }
            if (r3 == r1) goto L_0x0663
            java.lang.Class r1 = java.lang.Double.TYPE     // Catch:{ all -> 0x0691 }
            if (r3 == r1) goto L_0x0663
            java.lang.Class r1 = java.lang.Boolean.TYPE     // Catch:{ all -> 0x0691 }
            if (r3 == r1) goto L_0x0663
            r15.setValue((java.lang.Object) r14, (java.lang.Object) r2)     // Catch:{ all -> 0x0691 }
            goto L_0x0663
        L_0x0638:
            r3 = r28
            java.lang.Class<java.lang.String> r10 = java.lang.String.class
            if (r3 != r10) goto L_0x0660
            com.alibaba.fastjson.parser.Feature r3 = com.alibaba.fastjson.parser.Feature.TrimStringFieldValue     // Catch:{ all -> 0x0691 }
            int r3 = r3.mask     // Catch:{ all -> 0x0691 }
            r3 = r36 & r3
            if (r3 != 0) goto L_0x065a
            com.alibaba.fastjson.util.JavaBeanInfo r3 = r8.beanInfo     // Catch:{ all -> 0x0691 }
            int r3 = r3.parserFeatures     // Catch:{ all -> 0x0691 }
            com.alibaba.fastjson.parser.Feature r10 = com.alibaba.fastjson.parser.Feature.TrimStringFieldValue     // Catch:{ all -> 0x0691 }
            int r10 = r10.mask     // Catch:{ all -> 0x0691 }
            r3 = r3 & r10
            if (r3 != 0) goto L_0x065a
            int r1 = r1.parserFeatures     // Catch:{ all -> 0x0691 }
            com.alibaba.fastjson.parser.Feature r3 = com.alibaba.fastjson.parser.Feature.TrimStringFieldValue     // Catch:{ all -> 0x0691 }
            int r3 = r3.mask     // Catch:{ all -> 0x0691 }
            r1 = r1 & r3
            if (r1 == 0) goto L_0x0660
        L_0x065a:
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ all -> 0x0691 }
            java.lang.String r2 = r2.trim()     // Catch:{ all -> 0x0691 }
        L_0x0660:
            r15.setValue((java.lang.Object) r14, (java.lang.Object) r2)     // Catch:{ all -> 0x0691 }
        L_0x0663:
            if (r17 == 0) goto L_0x0672
            int r1 = r35 / 32
            int r2 = r35 % 32
            r3 = r17[r1]     // Catch:{ all -> 0x0691 }
            r15 = 1
            int r2 = r15 << r2
            r2 = r2 | r3
            r17[r1] = r2     // Catch:{ all -> 0x0691 }
            goto L_0x0673
        L_0x0672:
            r15 = 1
        L_0x0673:
            int r1 = r12.matchStat     // Catch:{ all -> 0x0691 }
            r3 = 4
            if (r1 != r3) goto L_0x05fe
            r19 = r5
            r30 = r7
            r35 = 0
            goto L_0x06f4
        L_0x0680:
            r4 = r33
            r3 = 4
            r15 = 1
            if (r5 != 0) goto L_0x0694
            java.util.HashMap r1 = new java.util.HashMap     // Catch:{ all -> 0x0691 }
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer[] r2 = r8.fieldDeserializers     // Catch:{ all -> 0x0691 }
            int r2 = r2.length     // Catch:{ all -> 0x0691 }
            r1.<init>(r2)     // Catch:{ all -> 0x0691 }
            r18 = r1
            goto L_0x0696
        L_0x0691:
            r0 = move-exception
            goto L_0x05ed
        L_0x0694:
            r18 = r5
        L_0x0696:
            r1 = r31
            r2 = r32
            r16 = r21
            r20 = 4
            r21 = 13
            r3 = r10
            r10 = r35
            r4 = r14
            r19 = r5
            r35 = 0
            r5 = r33
            r20 = r6
            r15 = 13
            r21 = 4
            r6 = r18
            r30 = r7
            r7 = r17
            boolean r1 = r1.parseField(r2, r3, r4, r5, r6, r7)     // Catch:{ all -> 0x09d0 }
            if (r1 != 0) goto L_0x06d7
            int r1 = r12.token()     // Catch:{ all -> 0x06cf }
            if (r1 != r15) goto L_0x06c6
            r12.nextToken()     // Catch:{ all -> 0x06cf }
            goto L_0x06f4
        L_0x06c6:
            r3 = r30
            r1 = 0
            r2 = 16
        L_0x06cb:
            r4 = 0
            r6 = 1
            goto L_0x098d
        L_0x06cf:
            r0 = move-exception
            r1 = r0
            r15 = r27
            r3 = r30
            goto L_0x09e3
        L_0x06d7:
            int r1 = r12.token()     // Catch:{ all -> 0x09d0 }
            r2 = 17
            if (r1 == r2) goto L_0x09c4
        L_0x06df:
            int r1 = r12.token()     // Catch:{ all -> 0x09d0 }
            r2 = 16
            if (r1 != r2) goto L_0x06eb
            r3 = r30
            r1 = 0
            goto L_0x06cb
        L_0x06eb:
            int r1 = r12.token()     // Catch:{ all -> 0x09d0 }
            if (r1 != r15) goto L_0x097a
            r12.nextToken(r2)     // Catch:{ all -> 0x09d0 }
        L_0x06f4:
            r1 = r19
            r5 = r27
        L_0x06f8:
            if (r14 != 0) goto L_0x094c
            if (r1 != 0) goto L_0x071d
            java.lang.Object r1 = r31.createInstance((com.alibaba.fastjson.parser.DefaultJSONParser) r32, (java.lang.reflect.Type) r33)     // Catch:{ all -> 0x0718 }
            if (r5 != 0) goto L_0x070e
            r3 = r30
            com.alibaba.fastjson.parser.ParseContext r5 = r9.setContext(r3, r1, r11)     // Catch:{ all -> 0x0709 }
            goto L_0x0710
        L_0x0709:
            r0 = move-exception
            r14 = r1
            r15 = r5
            goto L_0x09e2
        L_0x070e:
            r3 = r30
        L_0x0710:
            if (r5 == 0) goto L_0x0714
            r5.object = r1
        L_0x0714:
            r9.setContext(r3)
            return r1
        L_0x0718:
            r0 = move-exception
            r3 = r30
            goto L_0x0976
        L_0x071d:
            r3 = r30
            com.alibaba.fastjson.util.JavaBeanInfo r2 = r8.beanInfo     // Catch:{ all -> 0x0975 }
            java.lang.String[] r2 = r2.creatorConstructorParameters     // Catch:{ all -> 0x0975 }
            java.lang.String r4 = ""
            if (r2 == 0) goto L_0x07d3
            int r6 = r2.length     // Catch:{ all -> 0x0975 }
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x0975 }
            r7 = 0
        L_0x072b:
            int r10 = r2.length     // Catch:{ all -> 0x0975 }
            if (r7 >= r10) goto L_0x0847
            r10 = r2[r7]     // Catch:{ all -> 0x0975 }
            java.lang.Object r10 = r1.remove(r10)     // Catch:{ all -> 0x0975 }
            if (r10 != 0) goto L_0x078f
            com.alibaba.fastjson.util.JavaBeanInfo r11 = r8.beanInfo     // Catch:{ all -> 0x0975 }
            java.lang.reflect.Type[] r11 = r11.creatorConstructorParameterTypes     // Catch:{ all -> 0x0975 }
            r11 = r11[r7]     // Catch:{ all -> 0x0975 }
            com.alibaba.fastjson.util.JavaBeanInfo r12 = r8.beanInfo     // Catch:{ all -> 0x0975 }
            com.alibaba.fastjson.util.FieldInfo[] r12 = r12.fields     // Catch:{ all -> 0x0975 }
            r12 = r12[r7]     // Catch:{ all -> 0x0975 }
            java.lang.Class r13 = java.lang.Byte.TYPE     // Catch:{ all -> 0x0975 }
            if (r11 != r13) goto L_0x074b
            java.lang.Byte r10 = java.lang.Byte.valueOf(r35)     // Catch:{ all -> 0x0975 }
            goto L_0x078d
        L_0x074b:
            java.lang.Class r13 = java.lang.Short.TYPE     // Catch:{ all -> 0x0975 }
            if (r11 != r13) goto L_0x0754
            java.lang.Short r10 = java.lang.Short.valueOf(r35)     // Catch:{ all -> 0x0975 }
            goto L_0x078d
        L_0x0754:
            java.lang.Class r13 = java.lang.Integer.TYPE     // Catch:{ all -> 0x0975 }
            if (r11 != r13) goto L_0x075d
            java.lang.Integer r10 = java.lang.Integer.valueOf(r35)     // Catch:{ all -> 0x0975 }
            goto L_0x078d
        L_0x075d:
            java.lang.Class r13 = java.lang.Long.TYPE     // Catch:{ all -> 0x0975 }
            if (r11 != r13) goto L_0x0766
            java.lang.Long r10 = java.lang.Long.valueOf(r25)     // Catch:{ all -> 0x0975 }
            goto L_0x078d
        L_0x0766:
            java.lang.Class r13 = java.lang.Float.TYPE     // Catch:{ all -> 0x0975 }
            if (r11 != r13) goto L_0x076f
            java.lang.Float r10 = java.lang.Float.valueOf(r24)     // Catch:{ all -> 0x0975 }
            goto L_0x078d
        L_0x076f:
            java.lang.Class r13 = java.lang.Double.TYPE     // Catch:{ all -> 0x0975 }
            if (r11 != r13) goto L_0x0778
            java.lang.Double r10 = java.lang.Double.valueOf(r22)     // Catch:{ all -> 0x0975 }
            goto L_0x078d
        L_0x0778:
            java.lang.Class r13 = java.lang.Boolean.TYPE     // Catch:{ all -> 0x0975 }
            if (r11 != r13) goto L_0x077f
            java.lang.Boolean r10 = java.lang.Boolean.FALSE     // Catch:{ all -> 0x0975 }
            goto L_0x078d
        L_0x077f:
            java.lang.Class<java.lang.String> r13 = java.lang.String.class
            if (r11 != r13) goto L_0x078d
            int r11 = r12.parserFeatures     // Catch:{ all -> 0x0975 }
            com.alibaba.fastjson.parser.Feature r12 = com.alibaba.fastjson.parser.Feature.InitStringFieldAsEmpty     // Catch:{ all -> 0x0975 }
            int r12 = r12.mask     // Catch:{ all -> 0x0975 }
            r11 = r11 & r12
            if (r11 == 0) goto L_0x078d
            r10 = r4
        L_0x078d:
            r13 = 0
            goto L_0x07cb
        L_0x078f:
            com.alibaba.fastjson.util.JavaBeanInfo r11 = r8.beanInfo     // Catch:{ all -> 0x0975 }
            java.lang.reflect.Type[] r11 = r11.creatorConstructorParameterTypes     // Catch:{ all -> 0x0975 }
            if (r11 == 0) goto L_0x078d
            com.alibaba.fastjson.util.JavaBeanInfo r11 = r8.beanInfo     // Catch:{ all -> 0x0975 }
            java.lang.reflect.Type[] r11 = r11.creatorConstructorParameterTypes     // Catch:{ all -> 0x0975 }
            int r11 = r11.length     // Catch:{ all -> 0x0975 }
            if (r7 >= r11) goto L_0x078d
            com.alibaba.fastjson.util.JavaBeanInfo r11 = r8.beanInfo     // Catch:{ all -> 0x0975 }
            java.lang.reflect.Type[] r11 = r11.creatorConstructorParameterTypes     // Catch:{ all -> 0x0975 }
            r11 = r11[r7]     // Catch:{ all -> 0x0975 }
            boolean r12 = r11 instanceof java.lang.Class     // Catch:{ all -> 0x0975 }
            if (r12 == 0) goto L_0x078d
            java.lang.Class r11 = (java.lang.Class) r11     // Catch:{ all -> 0x0975 }
            boolean r12 = r11.isInstance(r10)     // Catch:{ all -> 0x0975 }
            if (r12 != 0) goto L_0x078d
            boolean r12 = r10 instanceof java.util.List     // Catch:{ all -> 0x0975 }
            if (r12 == 0) goto L_0x078d
            r12 = r10
            java.util.List r12 = (java.util.List) r12     // Catch:{ all -> 0x0975 }
            int r13 = r12.size()     // Catch:{ all -> 0x0975 }
            r15 = 1
            if (r13 != r15) goto L_0x078d
            r13 = 0
            java.lang.Object r15 = r12.get(r13)     // Catch:{ all -> 0x0975 }
            boolean r11 = r11.isInstance(r15)     // Catch:{ all -> 0x0975 }
            if (r11 == 0) goto L_0x07cb
            java.lang.Object r10 = r12.get(r13)     // Catch:{ all -> 0x0975 }
        L_0x07cb:
            r6[r7] = r10     // Catch:{ all -> 0x0975 }
            int r7 = r7 + 1
            r35 = 0
            goto L_0x072b
        L_0x07d3:
            r13 = 0
            com.alibaba.fastjson.util.JavaBeanInfo r6 = r8.beanInfo     // Catch:{ all -> 0x0975 }
            com.alibaba.fastjson.util.FieldInfo[] r6 = r6.fields     // Catch:{ all -> 0x0975 }
            int r7 = r6.length     // Catch:{ all -> 0x0975 }
            java.lang.Object[] r10 = new java.lang.Object[r7]     // Catch:{ all -> 0x0975 }
            r11 = 0
        L_0x07dc:
            if (r11 >= r7) goto L_0x0846
            r12 = r6[r11]     // Catch:{ all -> 0x0975 }
            java.lang.String r15 = r12.name     // Catch:{ all -> 0x0975 }
            java.lang.Object r15 = r1.get(r15)     // Catch:{ all -> 0x0975 }
            if (r15 != 0) goto L_0x083c
            java.lang.reflect.Type r13 = r12.fieldType     // Catch:{ all -> 0x0975 }
            r33 = r4
            java.lang.Class r4 = java.lang.Byte.TYPE     // Catch:{ all -> 0x0975 }
            if (r13 != r4) goto L_0x07f6
            r4 = 0
            java.lang.Byte r15 = java.lang.Byte.valueOf(r4)     // Catch:{ all -> 0x0975 }
            goto L_0x083e
        L_0x07f6:
            java.lang.Class r4 = java.lang.Short.TYPE     // Catch:{ all -> 0x0975 }
            if (r13 != r4) goto L_0x0800
            r4 = 0
            java.lang.Short r15 = java.lang.Short.valueOf(r4)     // Catch:{ all -> 0x0975 }
            goto L_0x083e
        L_0x0800:
            java.lang.Class r4 = java.lang.Integer.TYPE     // Catch:{ all -> 0x0975 }
            if (r13 != r4) goto L_0x080a
            r4 = 0
            java.lang.Integer r15 = java.lang.Integer.valueOf(r4)     // Catch:{ all -> 0x0975 }
            goto L_0x083e
        L_0x080a:
            java.lang.Class r4 = java.lang.Long.TYPE     // Catch:{ all -> 0x0975 }
            if (r13 != r4) goto L_0x0813
            java.lang.Long r15 = java.lang.Long.valueOf(r25)     // Catch:{ all -> 0x0975 }
            goto L_0x083e
        L_0x0813:
            java.lang.Class r4 = java.lang.Float.TYPE     // Catch:{ all -> 0x0975 }
            if (r13 != r4) goto L_0x081c
            java.lang.Float r15 = java.lang.Float.valueOf(r24)     // Catch:{ all -> 0x0975 }
            goto L_0x083e
        L_0x081c:
            java.lang.Class r4 = java.lang.Double.TYPE     // Catch:{ all -> 0x0975 }
            if (r13 != r4) goto L_0x0825
            java.lang.Double r15 = java.lang.Double.valueOf(r22)     // Catch:{ all -> 0x0975 }
            goto L_0x083e
        L_0x0825:
            java.lang.Class r4 = java.lang.Boolean.TYPE     // Catch:{ all -> 0x0975 }
            if (r13 != r4) goto L_0x082c
            java.lang.Boolean r15 = java.lang.Boolean.FALSE     // Catch:{ all -> 0x0975 }
            goto L_0x083e
        L_0x082c:
            java.lang.Class<java.lang.String> r4 = java.lang.String.class
            if (r13 != r4) goto L_0x083e
            int r4 = r12.parserFeatures     // Catch:{ all -> 0x0975 }
            com.alibaba.fastjson.parser.Feature r12 = com.alibaba.fastjson.parser.Feature.InitStringFieldAsEmpty     // Catch:{ all -> 0x0975 }
            int r12 = r12.mask     // Catch:{ all -> 0x0975 }
            r4 = r4 & r12
            if (r4 == 0) goto L_0x083e
            r15 = r33
            goto L_0x083e
        L_0x083c:
            r33 = r4
        L_0x083e:
            r10[r11] = r15     // Catch:{ all -> 0x0975 }
            int r11 = r11 + 1
            r4 = r33
            r13 = 0
            goto L_0x07dc
        L_0x0846:
            r6 = r10
        L_0x0847:
            com.alibaba.fastjson.util.JavaBeanInfo r4 = r8.beanInfo     // Catch:{ all -> 0x0975 }
            java.lang.reflect.Constructor<?> r4 = r4.creatorConstructor     // Catch:{ all -> 0x0975 }
            if (r4 == 0) goto L_0x0915
            com.alibaba.fastjson.util.JavaBeanInfo r4 = r8.beanInfo     // Catch:{ all -> 0x0975 }
            boolean r4 = r4.f0kotlin     // Catch:{ all -> 0x0975 }
            if (r4 == 0) goto L_0x087a
            r4 = 0
        L_0x0854:
            int r7 = r6.length     // Catch:{ all -> 0x0975 }
            if (r4 >= r7) goto L_0x087a
            r7 = r6[r4]     // Catch:{ all -> 0x0975 }
            if (r7 != 0) goto L_0x0877
            com.alibaba.fastjson.util.JavaBeanInfo r7 = r8.beanInfo     // Catch:{ all -> 0x0975 }
            com.alibaba.fastjson.util.FieldInfo[] r7 = r7.fields     // Catch:{ all -> 0x0975 }
            if (r7 == 0) goto L_0x0877
            com.alibaba.fastjson.util.JavaBeanInfo r7 = r8.beanInfo     // Catch:{ all -> 0x0975 }
            com.alibaba.fastjson.util.FieldInfo[] r7 = r7.fields     // Catch:{ all -> 0x0975 }
            int r7 = r7.length     // Catch:{ all -> 0x0975 }
            if (r4 >= r7) goto L_0x0877
            com.alibaba.fastjson.util.JavaBeanInfo r7 = r8.beanInfo     // Catch:{ all -> 0x0975 }
            com.alibaba.fastjson.util.FieldInfo[] r7 = r7.fields     // Catch:{ all -> 0x0975 }
            r4 = r7[r4]     // Catch:{ all -> 0x0975 }
            java.lang.Class<?> r4 = r4.fieldClass     // Catch:{ all -> 0x0975 }
            java.lang.Class<java.lang.String> r7 = java.lang.String.class
            if (r4 != r7) goto L_0x087a
            r28 = 1
            goto L_0x087c
        L_0x0877:
            int r4 = r4 + 1
            goto L_0x0854
        L_0x087a:
            r28 = 0
        L_0x087c:
            if (r28 == 0) goto L_0x08b3
            com.alibaba.fastjson.util.JavaBeanInfo r4 = r8.beanInfo     // Catch:{ Exception -> 0x08e7 }
            java.lang.reflect.Constructor<?> r4 = r4.kotlinDefaultConstructor     // Catch:{ Exception -> 0x08e7 }
            if (r4 == 0) goto L_0x08b3
            com.alibaba.fastjson.util.JavaBeanInfo r4 = r8.beanInfo     // Catch:{ Exception -> 0x08e7 }
            java.lang.reflect.Constructor<?> r4 = r4.kotlinDefaultConstructor     // Catch:{ Exception -> 0x08e7 }
            r7 = 0
            java.lang.Object[] r10 = new java.lang.Object[r7]     // Catch:{ Exception -> 0x08e7 }
            java.lang.Object r4 = r4.newInstance(r10)     // Catch:{ Exception -> 0x08e7 }
            r7 = 0
        L_0x0890:
            int r10 = r6.length     // Catch:{ Exception -> 0x08b0 }
            if (r7 >= r10) goto L_0x08bb
            r10 = r6[r7]     // Catch:{ Exception -> 0x08b0 }
            if (r10 == 0) goto L_0x08ad
            com.alibaba.fastjson.util.JavaBeanInfo r11 = r8.beanInfo     // Catch:{ Exception -> 0x08b0 }
            com.alibaba.fastjson.util.FieldInfo[] r11 = r11.fields     // Catch:{ Exception -> 0x08b0 }
            if (r11 == 0) goto L_0x08ad
            com.alibaba.fastjson.util.JavaBeanInfo r11 = r8.beanInfo     // Catch:{ Exception -> 0x08b0 }
            com.alibaba.fastjson.util.FieldInfo[] r11 = r11.fields     // Catch:{ Exception -> 0x08b0 }
            int r11 = r11.length     // Catch:{ Exception -> 0x08b0 }
            if (r7 >= r11) goto L_0x08ad
            com.alibaba.fastjson.util.JavaBeanInfo r11 = r8.beanInfo     // Catch:{ Exception -> 0x08b0 }
            com.alibaba.fastjson.util.FieldInfo[] r11 = r11.fields     // Catch:{ Exception -> 0x08b0 }
            r11 = r11[r7]     // Catch:{ Exception -> 0x08b0 }
            r11.set(r4, r10)     // Catch:{ Exception -> 0x08b0 }
        L_0x08ad:
            int r7 = r7 + 1
            goto L_0x0890
        L_0x08b0:
            r0 = move-exception
            r1 = r0
            goto L_0x08ea
        L_0x08b3:
            com.alibaba.fastjson.util.JavaBeanInfo r4 = r8.beanInfo     // Catch:{ Exception -> 0x08e7 }
            java.lang.reflect.Constructor<?> r4 = r4.creatorConstructor     // Catch:{ Exception -> 0x08e7 }
            java.lang.Object r4 = r4.newInstance(r6)     // Catch:{ Exception -> 0x08e7 }
        L_0x08bb:
            if (r2 == 0) goto L_0x08e5
            java.util.Set r1 = r1.entrySet()     // Catch:{ all -> 0x0911 }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x0911 }
        L_0x08c5:
            boolean r2 = r1.hasNext()     // Catch:{ all -> 0x0911 }
            if (r2 == 0) goto L_0x08e5
            java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x0911 }
            java.util.Map$Entry r2 = (java.util.Map.Entry) r2     // Catch:{ all -> 0x0911 }
            java.lang.Object r6 = r2.getKey()     // Catch:{ all -> 0x0911 }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ all -> 0x0911 }
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer r6 = r8.getFieldDeserializer((java.lang.String) r6)     // Catch:{ all -> 0x0911 }
            if (r6 == 0) goto L_0x08c5
            java.lang.Object r2 = r2.getValue()     // Catch:{ all -> 0x0911 }
            r6.setValue((java.lang.Object) r4, (java.lang.Object) r2)     // Catch:{ all -> 0x0911 }
            goto L_0x08c5
        L_0x08e5:
            r14 = r4
            goto L_0x0947
        L_0x08e7:
            r0 = move-exception
            r1 = r0
            r4 = r14
        L_0x08ea:
            com.alibaba.fastjson.JSONException r6 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0911 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x0911 }
            r7.<init>()     // Catch:{ all -> 0x0911 }
            java.lang.String r10 = "create instance error, "
            r7.append(r10)     // Catch:{ all -> 0x0911 }
            r7.append(r2)     // Catch:{ all -> 0x0911 }
            java.lang.String r2 = ", "
            r7.append(r2)     // Catch:{ all -> 0x0911 }
            com.alibaba.fastjson.util.JavaBeanInfo r2 = r8.beanInfo     // Catch:{ all -> 0x0911 }
            java.lang.reflect.Constructor<?> r2 = r2.creatorConstructor     // Catch:{ all -> 0x0911 }
            java.lang.String r2 = r2.toGenericString()     // Catch:{ all -> 0x0911 }
            r7.append(r2)     // Catch:{ all -> 0x0911 }
            java.lang.String r2 = r7.toString()     // Catch:{ all -> 0x0911 }
            r6.<init>(r2, r1)     // Catch:{ all -> 0x0911 }
            throw r6     // Catch:{ all -> 0x0911 }
        L_0x0911:
            r0 = move-exception
            r1 = r0
            r14 = r4
            goto L_0x0977
        L_0x0915:
            com.alibaba.fastjson.util.JavaBeanInfo r1 = r8.beanInfo     // Catch:{ all -> 0x0975 }
            java.lang.reflect.Method r1 = r1.factoryMethod     // Catch:{ all -> 0x0975 }
            if (r1 == 0) goto L_0x0947
            com.alibaba.fastjson.util.JavaBeanInfo r1 = r8.beanInfo     // Catch:{ Exception -> 0x0926 }
            java.lang.reflect.Method r1 = r1.factoryMethod     // Catch:{ Exception -> 0x0926 }
            r4 = 0
            java.lang.Object r1 = r1.invoke(r4, r6)     // Catch:{ Exception -> 0x0926 }
            r14 = r1
            goto L_0x0947
        L_0x0926:
            r0 = move-exception
            r1 = r0
            com.alibaba.fastjson.JSONException r2 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0975 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0975 }
            r4.<init>()     // Catch:{ all -> 0x0975 }
            java.lang.String r6 = "create factory method error, "
            r4.append(r6)     // Catch:{ all -> 0x0975 }
            com.alibaba.fastjson.util.JavaBeanInfo r6 = r8.beanInfo     // Catch:{ all -> 0x0975 }
            java.lang.reflect.Method r6 = r6.factoryMethod     // Catch:{ all -> 0x0975 }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x0975 }
            r4.append(r6)     // Catch:{ all -> 0x0975 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x0975 }
            r2.<init>(r4, r1)     // Catch:{ all -> 0x0975 }
            throw r2     // Catch:{ all -> 0x0975 }
        L_0x0947:
            if (r5 == 0) goto L_0x094e
            r5.object = r14     // Catch:{ all -> 0x0975 }
            goto L_0x094e
        L_0x094c:
            r3 = r30
        L_0x094e:
            com.alibaba.fastjson.util.JavaBeanInfo r1 = r8.beanInfo     // Catch:{ all -> 0x0975 }
            java.lang.reflect.Method r1 = r1.buildMethod     // Catch:{ all -> 0x0975 }
            if (r1 != 0) goto L_0x095c
            if (r5 == 0) goto L_0x0958
            r5.object = r14
        L_0x0958:
            r9.setContext(r3)
            return r14
        L_0x095c:
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x096b }
            java.lang.Object r1 = r1.invoke(r14, r2)     // Catch:{ Exception -> 0x096b }
            if (r5 == 0) goto L_0x0967
            r5.object = r14
        L_0x0967:
            r9.setContext(r3)
            return r1
        L_0x096b:
            r0 = move-exception
            r1 = r0
            com.alibaba.fastjson.JSONException r2 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0975 }
            java.lang.String r4 = "build object error"
            r2.<init>(r4, r1)     // Catch:{ all -> 0x0975 }
            throw r2     // Catch:{ all -> 0x0975 }
        L_0x0975:
            r0 = move-exception
        L_0x0976:
            r1 = r0
        L_0x0977:
            r15 = r5
            goto L_0x09e3
        L_0x097a:
            r3 = r30
            r1 = 0
            r4 = 0
            int r5 = r12.token()     // Catch:{ all -> 0x09ce }
            r6 = 18
            if (r5 == r6) goto L_0x09a5
            int r5 = r12.token()     // Catch:{ all -> 0x09ce }
            r6 = 1
            if (r5 == r6) goto L_0x09a5
        L_0x098d:
            r5 = r27
            r27 = r14
        L_0x0991:
            int r7 = r10 + 1
            r10 = r33
            r4 = r7
            r2 = r17
            r17 = r19
            r6 = r20
            r1 = r27
            r14 = 16
            r7 = r3
            r3 = r16
            goto L_0x0181
        L_0x09a5:
            com.alibaba.fastjson.JSONException r1 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x09ce }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x09ce }
            r2.<init>()     // Catch:{ all -> 0x09ce }
            java.lang.String r4 = "syntax error, unexpect token "
            r2.append(r4)     // Catch:{ all -> 0x09ce }
            int r4 = r12.token()     // Catch:{ all -> 0x09ce }
            java.lang.String r4 = com.alibaba.fastjson.parser.JSONToken.name(r4)     // Catch:{ all -> 0x09ce }
            r2.append(r4)     // Catch:{ all -> 0x09ce }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x09ce }
            r1.<init>(r2)     // Catch:{ all -> 0x09ce }
            throw r1     // Catch:{ all -> 0x09ce }
        L_0x09c4:
            r3 = r30
            com.alibaba.fastjson.JSONException r1 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x09ce }
            java.lang.String r2 = "syntax error, unexpect token ':'"
            r1.<init>(r2)     // Catch:{ all -> 0x09ce }
            throw r1     // Catch:{ all -> 0x09ce }
        L_0x09ce:
            r0 = move-exception
            goto L_0x09d9
        L_0x09d0:
            r0 = move-exception
            r3 = r30
            goto L_0x09d9
        L_0x09d4:
            r0 = move-exception
            r14 = r1
            r27 = r5
            r3 = r7
        L_0x09d9:
            r1 = r0
        L_0x09da:
            r15 = r27
            goto L_0x09e3
        L_0x09dd:
            r0 = move-exception
            r3 = r7
            r4 = r15
            r14 = r35
        L_0x09e2:
            r1 = r0
        L_0x09e3:
            if (r15 == 0) goto L_0x09e7
            r15.object = r14
        L_0x09e7:
            r9.setContext(r3)
            throw r1
        L_0x09eb:
            java.lang.Object r1 = r32.parse()
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.deserialze(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.lang.Object, java.lang.Object, int, int[]):java.lang.Object");
    }

    /* access modifiers changed from: protected */
    public Enum scanEnum(JSONLexerBase jSONLexerBase, char[] cArr, ObjectDeserializer objectDeserializer) {
        EnumDeserializer enumDeserializer = objectDeserializer instanceof EnumDeserializer ? (EnumDeserializer) objectDeserializer : null;
        if (enumDeserializer == null) {
            jSONLexerBase.matchStat = -1;
            return null;
        }
        long scanEnumSymbol = jSONLexerBase.scanEnumSymbol(cArr);
        if (jSONLexerBase.matchStat <= 0) {
            return null;
        }
        Enum enumByHashCode = enumDeserializer.getEnumByHashCode(scanEnumSymbol);
        if (enumByHashCode == null) {
            if (scanEnumSymbol == TypeUtils.fnv1a_64_magic_hashcode) {
                return null;
            }
            if (jSONLexerBase.isEnabled(Feature.ErrorOnEnumNotMatch)) {
                throw new JSONException("not match enum value, " + enumDeserializer.enumClass);
            }
        }
        return enumByHashCode;
    }

    public boolean parseField(DefaultJSONParser defaultJSONParser, String str, Object obj, Type type, Map<String, Object> map) {
        return parseField(defaultJSONParser, str, obj, type, map, (int[]) null);
    }

    /* JADX WARNING: type inference failed for: r17v0, types: [int, boolean] */
    /* JADX WARNING: type inference failed for: r17v1 */
    /* JADX WARNING: type inference failed for: r17v3 */
    /* JADX WARNING: type inference failed for: r17v4 */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x0212  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x0125  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean parseField(com.alibaba.fastjson.parser.DefaultJSONParser r22, java.lang.String r23, java.lang.Object r24, java.lang.reflect.Type r25, java.util.Map<java.lang.String, java.lang.Object> r26, int[] r27) {
        /*
            r21 = this;
            r1 = r21
            r0 = r22
            r11 = r23
            r12 = r24
            r13 = r25
            r14 = r26
            r15 = r27
            com.alibaba.fastjson.parser.JSONLexer r10 = r0.lexer
            com.alibaba.fastjson.parser.Feature r2 = com.alibaba.fastjson.parser.Feature.DisableFieldSmartMatch
            int r2 = r2.mask
            com.alibaba.fastjson.parser.Feature r3 = com.alibaba.fastjson.parser.Feature.InitStringFieldAsEmpty
            int r3 = r3.mask
            boolean r4 = r10.isEnabled((int) r2)
            if (r4 != 0) goto L_0x003e
            com.alibaba.fastjson.util.JavaBeanInfo r4 = r1.beanInfo
            int r4 = r4.parserFeatures
            r2 = r2 & r4
            if (r2 == 0) goto L_0x0026
            goto L_0x003e
        L_0x0026:
            boolean r2 = r10.isEnabled((int) r3)
            if (r2 != 0) goto L_0x0039
            com.alibaba.fastjson.util.JavaBeanInfo r2 = r1.beanInfo
            int r2 = r2.parserFeatures
            r2 = r2 & r3
            if (r2 == 0) goto L_0x0034
            goto L_0x0039
        L_0x0034:
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer r2 = r1.smartMatch(r11, r15)
            goto L_0x0042
        L_0x0039:
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer r2 = r1.smartMatch(r11)
            goto L_0x0042
        L_0x003e:
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer r2 = r1.getFieldDeserializer((java.lang.String) r11)
        L_0x0042:
            com.alibaba.fastjson.parser.Feature r3 = com.alibaba.fastjson.parser.Feature.SupportNonPublicField
            int r3 = r3.mask
            r16 = 0
            r9 = 1
            if (r2 != 0) goto L_0x011b
            boolean r4 = r10.isEnabled((int) r3)
            if (r4 != 0) goto L_0x0058
            com.alibaba.fastjson.util.JavaBeanInfo r4 = r1.beanInfo
            int r4 = r4.parserFeatures
            r3 = r3 & r4
            if (r3 == 0) goto L_0x011b
        L_0x0058:
            java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> r3 = r1.extraFieldDeserializers
            if (r3 != 0) goto L_0x00c8
            java.util.concurrent.ConcurrentHashMap r3 = new java.util.concurrent.ConcurrentHashMap
            r4 = 1061158912(0x3f400000, float:0.75)
            r3.<init>(r9, r4, r9)
            java.lang.Class<?> r4 = r1.clazz
        L_0x0065:
            if (r4 == 0) goto L_0x00c3
            java.lang.Class<java.lang.Object> r5 = java.lang.Object.class
            if (r4 == r5) goto L_0x00c3
            java.lang.reflect.Field[] r5 = r4.getDeclaredFields()
            int r6 = r5.length
            r7 = 0
        L_0x0071:
            if (r7 >= r6) goto L_0x00bb
            r8 = r5[r7]
            java.lang.String r9 = r8.getName()
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer r18 = r1.getFieldDeserializer((java.lang.String) r9)
            if (r18 == 0) goto L_0x0080
            goto L_0x00af
        L_0x0080:
            int r18 = r8.getModifiers()
            r19 = r18 & 16
            if (r19 != 0) goto L_0x00af
            r18 = r18 & 8
            if (r18 == 0) goto L_0x008d
            goto L_0x00af
        L_0x008d:
            r18 = r2
            java.lang.Class<com.alibaba.fastjson.annotation.JSONField> r2 = com.alibaba.fastjson.annotation.JSONField.class
            java.lang.annotation.Annotation r2 = com.alibaba.fastjson.util.TypeUtils.getAnnotation((java.lang.reflect.Field) r8, r2)
            com.alibaba.fastjson.annotation.JSONField r2 = (com.alibaba.fastjson.annotation.JSONField) r2
            if (r2 == 0) goto L_0x00a9
            java.lang.String r2 = r2.name()
            r19 = r5
            java.lang.String r5 = ""
            boolean r5 = r5.equals(r2)
            if (r5 != 0) goto L_0x00ab
            r9 = r2
            goto L_0x00ab
        L_0x00a9:
            r19 = r5
        L_0x00ab:
            r3.put(r9, r8)
            goto L_0x00b3
        L_0x00af:
            r18 = r2
            r19 = r5
        L_0x00b3:
            int r7 = r7 + 1
            r2 = r18
            r5 = r19
            r9 = 1
            goto L_0x0071
        L_0x00bb:
            r18 = r2
            java.lang.Class r4 = r4.getSuperclass()
            r9 = 1
            goto L_0x0065
        L_0x00c3:
            r18 = r2
            r1.extraFieldDeserializers = r3
            goto L_0x00ca
        L_0x00c8:
            r18 = r2
        L_0x00ca:
            java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> r2 = r1.extraFieldDeserializers
            java.lang.Object r2 = r2.get(r11)
            if (r2 == 0) goto L_0x011d
            boolean r3 = r2 instanceof com.alibaba.fastjson.parser.deserializer.FieldDeserializer
            if (r3 == 0) goto L_0x00dc
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer r2 = (com.alibaba.fastjson.parser.deserializer.FieldDeserializer) r2
            r15 = r10
            r17 = 1
            goto L_0x0122
        L_0x00dc:
            r7 = r2
            java.lang.reflect.Field r7 = (java.lang.reflect.Field) r7
            r9 = 1
            r7.setAccessible(r9)
            com.alibaba.fastjson.util.FieldInfo r8 = new com.alibaba.fastjson.util.FieldInfo
            java.lang.Class r4 = r7.getDeclaringClass()
            java.lang.Class r5 = r7.getType()
            java.lang.reflect.Type r6 = r7.getGenericType()
            r17 = 0
            r18 = 0
            r19 = 0
            r2 = r8
            r3 = r23
            r20 = r8
            r8 = r17
            r17 = 1
            r9 = r18
            r15 = r10
            r10 = r19
            r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10)
            com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer r2 = new com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer
            com.alibaba.fastjson.parser.ParserConfig r3 = r22.getConfig()
            java.lang.Class<?> r4 = r1.clazz
            r5 = r20
            r2.<init>(r3, r4, r5)
            java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> r3 = r1.extraFieldDeserializers
            r3.put(r11, r2)
            goto L_0x0122
        L_0x011b:
            r18 = r2
        L_0x011d:
            r15 = r10
            r17 = 1
            r2 = r18
        L_0x0122:
            r3 = -1
            if (r2 != 0) goto L_0x0212
            com.alibaba.fastjson.parser.Feature r2 = com.alibaba.fastjson.parser.Feature.IgnoreNotMatch
            boolean r2 = r15.isEnabled((com.alibaba.fastjson.parser.Feature) r2)
            if (r2 == 0) goto L_0x01ed
            r2 = 0
            r4 = -1
        L_0x012f:
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer[] r5 = r1.sortedFieldDeserializers
            int r6 = r5.length
            if (r2 >= r6) goto L_0x01d7
            r5 = r5[r2]
            com.alibaba.fastjson.util.FieldInfo r6 = r5.fieldInfo
            boolean r7 = r6.unwrapped
            if (r7 == 0) goto L_0x01d3
            boolean r7 = r5 instanceof com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer
            if (r7 == 0) goto L_0x01d3
            java.lang.reflect.Field r7 = r6.field
            java.lang.String r8 = "parse unwrapped field error."
            if (r7 == 0) goto L_0x01ae
            r7 = r5
            com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer r7 = (com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer) r7
            com.alibaba.fastjson.parser.ParserConfig r9 = r22.getConfig()
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r9 = r7.getFieldValueDeserilizer(r9)
            boolean r10 = r9 instanceof com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer
            if (r10 == 0) goto L_0x0183
            r10 = r9
            com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer r10 = (com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) r10
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer r10 = r10.getFieldDeserializer((java.lang.String) r11)
            if (r10 == 0) goto L_0x01d3
            java.lang.reflect.Field r4 = r6.field     // Catch:{ Exception -> 0x017c }
            java.lang.Object r4 = r4.get(r12)     // Catch:{ Exception -> 0x017c }
            if (r4 != 0) goto L_0x0171
            com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer r9 = (com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) r9     // Catch:{ Exception -> 0x017c }
            java.lang.reflect.Type r4 = r6.fieldType     // Catch:{ Exception -> 0x017c }
            java.lang.Object r4 = r9.createInstance((com.alibaba.fastjson.parser.DefaultJSONParser) r0, (java.lang.reflect.Type) r4)     // Catch:{ Exception -> 0x017c }
            r5.setValue((java.lang.Object) r12, (java.lang.Object) r4)     // Catch:{ Exception -> 0x017c }
        L_0x0171:
            int r5 = r7.getFastMatchToken()     // Catch:{ Exception -> 0x017c }
            r15.nextTokenWithColon(r5)     // Catch:{ Exception -> 0x017c }
            r10.parseField(r0, r4, r13, r14)     // Catch:{ Exception -> 0x017c }
            goto L_0x01ca
        L_0x017c:
            r0 = move-exception
            com.alibaba.fastjson.JSONException r2 = new com.alibaba.fastjson.JSONException
            r2.<init>(r8, r0)
            throw r2
        L_0x0183:
            boolean r7 = r9 instanceof com.alibaba.fastjson.parser.deserializer.MapDeserializer
            if (r7 == 0) goto L_0x01d3
            com.alibaba.fastjson.parser.deserializer.MapDeserializer r9 = (com.alibaba.fastjson.parser.deserializer.MapDeserializer) r9
            java.lang.reflect.Field r4 = r6.field     // Catch:{ Exception -> 0x01a7 }
            java.lang.Object r4 = r4.get(r12)     // Catch:{ Exception -> 0x01a7 }
            java.util.Map r4 = (java.util.Map) r4     // Catch:{ Exception -> 0x01a7 }
            if (r4 != 0) goto L_0x019c
            java.lang.reflect.Type r4 = r6.fieldType     // Catch:{ Exception -> 0x01a7 }
            java.util.Map r4 = r9.createMap(r4)     // Catch:{ Exception -> 0x01a7 }
            r5.setValue((java.lang.Object) r12, (java.lang.Object) r4)     // Catch:{ Exception -> 0x01a7 }
        L_0x019c:
            r15.nextTokenWithColon()     // Catch:{ Exception -> 0x01a7 }
            java.lang.Object r5 = r22.parse(r23)     // Catch:{ Exception -> 0x01a7 }
            r4.put(r11, r5)     // Catch:{ Exception -> 0x01a7 }
            goto L_0x01ca
        L_0x01a7:
            r0 = move-exception
            com.alibaba.fastjson.JSONException r2 = new com.alibaba.fastjson.JSONException
            r2.<init>(r8, r0)
            throw r2
        L_0x01ae:
            java.lang.reflect.Method r5 = r6.method
            java.lang.Class[] r5 = r5.getParameterTypes()
            int r5 = r5.length
            r7 = 2
            if (r5 != r7) goto L_0x01d3
            r15.nextTokenWithColon()
            java.lang.Object r4 = r22.parse(r23)
            java.lang.reflect.Method r5 = r6.method     // Catch:{ Exception -> 0x01cc }
            java.lang.Object[] r6 = new java.lang.Object[r7]     // Catch:{ Exception -> 0x01cc }
            r6[r16] = r11     // Catch:{ Exception -> 0x01cc }
            r6[r17] = r4     // Catch:{ Exception -> 0x01cc }
            r5.invoke(r12, r6)     // Catch:{ Exception -> 0x01cc }
        L_0x01ca:
            r4 = r2
            goto L_0x01d3
        L_0x01cc:
            r0 = move-exception
            com.alibaba.fastjson.JSONException r2 = new com.alibaba.fastjson.JSONException
            r2.<init>(r8, r0)
            throw r2
        L_0x01d3:
            int r2 = r2 + 1
            goto L_0x012f
        L_0x01d7:
            if (r4 == r3) goto L_0x01e9
            r5 = r27
            if (r5 == 0) goto L_0x01e8
            int r0 = r4 / 32
            int r4 = r4 % 32
            r2 = r5[r0]
            int r3 = r17 << r4
            r2 = r2 | r3
            r5[r0] = r2
        L_0x01e8:
            return r17
        L_0x01e9:
            r0.parseExtra(r12, r11)
            return r16
        L_0x01ed:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "setter not found, class "
            r2.append(r3)
            java.lang.Class<?> r3 = r1.clazz
            java.lang.String r3 = r3.getName()
            r2.append(r3)
            java.lang.String r3 = ", property "
            r2.append(r3)
            r2.append(r11)
            java.lang.String r2 = r2.toString()
            r0.<init>(r2)
            throw r0
        L_0x0212:
            r5 = r27
            r4 = r15
            r6 = 0
        L_0x0216:
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer[] r7 = r1.sortedFieldDeserializers
            int r8 = r7.length
            if (r6 >= r8) goto L_0x0223
            r7 = r7[r6]
            if (r7 != r2) goto L_0x0220
            goto L_0x0224
        L_0x0220:
            int r6 = r6 + 1
            goto L_0x0216
        L_0x0223:
            r6 = -1
        L_0x0224:
            if (r6 == r3) goto L_0x023a
            if (r5 == 0) goto L_0x023a
            java.lang.String r3 = "_"
            boolean r3 = r11.startsWith(r3)
            if (r3 == 0) goto L_0x023a
            boolean r3 = isSetFlag(r6, r5)
            if (r3 == 0) goto L_0x023a
            r0.parseExtra(r12, r11)
            return r16
        L_0x023a:
            int r3 = r2.getFastMatchToken()
            r4.nextTokenWithColon(r3)
            r2.parseField(r0, r12, r13, r14)
            if (r5 == 0) goto L_0x0251
            int r0 = r6 / 32
            int r6 = r6 % 32
            r2 = r5[r0]
            int r3 = r17 << r6
            r2 = r2 | r3
            r5[r0] = r2
        L_0x0251:
            return r17
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.parseField(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.String, java.lang.Object, java.lang.reflect.Type, java.util.Map, int[]):boolean");
    }

    public FieldDeserializer smartMatch(String str) {
        return smartMatch(str, (int[]) null);
    }

    public FieldDeserializer smartMatch(String str, int[] iArr) {
        boolean z;
        if (str == null) {
            return null;
        }
        FieldDeserializer fieldDeserializer = getFieldDeserializer(str, iArr);
        if (fieldDeserializer == null) {
            int i = 0;
            if (this.smartMatchHashArray == null) {
                long[] jArr = new long[this.sortedFieldDeserializers.length];
                int i2 = 0;
                while (true) {
                    FieldDeserializer[] fieldDeserializerArr = this.sortedFieldDeserializers;
                    if (i2 >= fieldDeserializerArr.length) {
                        break;
                    }
                    jArr[i2] = fieldDeserializerArr[i2].fieldInfo.nameHashCode;
                    i2++;
                }
                Arrays.sort(jArr);
                this.smartMatchHashArray = jArr;
            }
            int binarySearch = Arrays.binarySearch(this.smartMatchHashArray, TypeUtils.fnv1a_64_lower(str));
            if (binarySearch < 0) {
                binarySearch = Arrays.binarySearch(this.smartMatchHashArray, TypeUtils.fnv1a_64_extract(str));
            }
            if (binarySearch < 0) {
                z = str.startsWith("is");
                if (z) {
                    binarySearch = Arrays.binarySearch(this.smartMatchHashArray, TypeUtils.fnv1a_64_extract(str.substring(2)));
                }
            } else {
                z = false;
            }
            if (binarySearch >= 0) {
                if (this.smartMatchHashArrayMapping == null) {
                    short[] sArr = new short[this.smartMatchHashArray.length];
                    Arrays.fill(sArr, -1);
                    while (true) {
                        FieldDeserializer[] fieldDeserializerArr2 = this.sortedFieldDeserializers;
                        if (i >= fieldDeserializerArr2.length) {
                            break;
                        }
                        int binarySearch2 = Arrays.binarySearch(this.smartMatchHashArray, fieldDeserializerArr2[i].fieldInfo.nameHashCode);
                        if (binarySearch2 >= 0) {
                            sArr[binarySearch2] = (short) i;
                        }
                        i++;
                    }
                    this.smartMatchHashArrayMapping = sArr;
                }
                short s = this.smartMatchHashArrayMapping[binarySearch];
                if (s != -1 && !isSetFlag(s, iArr)) {
                    fieldDeserializer = this.sortedFieldDeserializers[s];
                }
            }
            if (fieldDeserializer != null) {
                FieldInfo fieldInfo = fieldDeserializer.fieldInfo;
                if ((fieldInfo.parserFeatures & Feature.DisableFieldSmartMatch.mask) != 0) {
                    return null;
                }
                Class<?> cls = fieldInfo.fieldClass;
                if (!(!z || cls == Boolean.TYPE || cls == Boolean.class)) {
                    return null;
                }
            }
        }
        return fieldDeserializer;
    }

    private Object createFactoryInstance(ParserConfig parserConfig, Object obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        return this.beanInfo.factoryMethod.invoke((Object) null, new Object[]{obj});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x006b, code lost:
        if (com.alibaba.fastjson.JSONValidator.from(r6).validate() != false) goto L_0x0072;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object createInstance(java.util.Map<java.lang.String, java.lang.Object> r13, com.alibaba.fastjson.parser.ParserConfig r14) throws java.lang.IllegalArgumentException, java.lang.IllegalAccessException, java.lang.reflect.InvocationTargetException {
        /*
            r12 = this;
            com.alibaba.fastjson.util.JavaBeanInfo r0 = r12.beanInfo
            java.lang.reflect.Constructor<?> r0 = r0.creatorConstructor
            r1 = 1
            r2 = 0
            r3 = 0
            if (r0 != 0) goto L_0x017e
            com.alibaba.fastjson.util.JavaBeanInfo r0 = r12.beanInfo
            java.lang.reflect.Method r0 = r0.factoryMethod
            if (r0 != 0) goto L_0x017e
            java.lang.Class<?> r0 = r12.clazz
            java.lang.Object r0 = r12.createInstance((com.alibaba.fastjson.parser.DefaultJSONParser) r2, (java.lang.reflect.Type) r0)
            java.util.Set r13 = r13.entrySet()
            java.util.Iterator r13 = r13.iterator()
        L_0x001d:
            boolean r4 = r13.hasNext()
            if (r4 == 0) goto L_0x0163
            java.lang.Object r4 = r13.next()
            java.util.Map$Entry r4 = (java.util.Map.Entry) r4
            java.lang.Object r5 = r4.getKey()
            java.lang.String r5 = (java.lang.String) r5
            java.lang.Object r4 = r4.getValue()
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer r5 = r12.smartMatch(r5)
            if (r5 != 0) goto L_0x003a
            goto L_0x001d
        L_0x003a:
            com.alibaba.fastjson.util.FieldInfo r6 = r5.fieldInfo
            com.alibaba.fastjson.util.FieldInfo r7 = r5.fieldInfo
            java.lang.reflect.Field r7 = r7.field
            java.lang.reflect.Type r8 = r6.fieldType
            java.lang.Class<?> r9 = r6.fieldClass
            com.alibaba.fastjson.annotation.JSONField r10 = r6.getAnnotation()
            java.lang.Class<?> r11 = r6.declaringClass
            if (r11 == 0) goto L_0x007b
            boolean r9 = r9.isInstance(r4)
            if (r9 == 0) goto L_0x005c
            if (r10 == 0) goto L_0x007b
            java.lang.Class r9 = r10.deserializeUsing()
            java.lang.Class<java.lang.Void> r10 = java.lang.Void.class
            if (r9 == r10) goto L_0x007b
        L_0x005c:
            boolean r6 = r4 instanceof java.lang.String
            if (r6 == 0) goto L_0x006e
            r6 = r4
            java.lang.String r6 = (java.lang.String) r6
            com.alibaba.fastjson.JSONValidator r7 = com.alibaba.fastjson.JSONValidator.from((java.lang.String) r6)
            boolean r7 = r7.validate()
            if (r7 == 0) goto L_0x006e
            goto L_0x0072
        L_0x006e:
            java.lang.String r6 = com.alibaba.fastjson.JSON.toJSONString(r4)
        L_0x0072:
            com.alibaba.fastjson.parser.DefaultJSONParser r4 = new com.alibaba.fastjson.parser.DefaultJSONParser
            r4.<init>((java.lang.String) r6)
            r5.parseField(r4, r0, r8, r2)
            goto L_0x001d
        L_0x007b:
            if (r7 == 0) goto L_0x0128
            java.lang.reflect.Method r9 = r6.method
            if (r9 != 0) goto L_0x0128
            java.lang.Class r9 = r7.getType()
            java.lang.Class r10 = java.lang.Boolean.TYPE
            if (r9 != r10) goto L_0x0099
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r4 != r9) goto L_0x0091
            r7.setBoolean(r0, r3)
            goto L_0x001d
        L_0x0091:
            java.lang.Boolean r9 = java.lang.Boolean.TRUE
            if (r4 != r9) goto L_0x0128
            r7.setBoolean(r0, r1)
            goto L_0x001d
        L_0x0099:
            java.lang.Class r10 = java.lang.Integer.TYPE
            if (r9 != r10) goto L_0x00ac
            boolean r9 = r4 instanceof java.lang.Number
            if (r9 == 0) goto L_0x0128
            java.lang.Number r4 = (java.lang.Number) r4
            int r4 = r4.intValue()
            r7.setInt(r0, r4)
            goto L_0x001d
        L_0x00ac:
            java.lang.Class r10 = java.lang.Long.TYPE
            if (r9 != r10) goto L_0x00bf
            boolean r9 = r4 instanceof java.lang.Number
            if (r9 == 0) goto L_0x0128
            java.lang.Number r4 = (java.lang.Number) r4
            long r4 = r4.longValue()
            r7.setLong(r0, r4)
            goto L_0x001d
        L_0x00bf:
            java.lang.Class r10 = java.lang.Float.TYPE
            r11 = 10
            if (r9 != r10) goto L_0x00ee
            boolean r9 = r4 instanceof java.lang.Number
            if (r9 == 0) goto L_0x00d4
            java.lang.Number r4 = (java.lang.Number) r4
            float r4 = r4.floatValue()
            r7.setFloat(r0, r4)
            goto L_0x001d
        L_0x00d4:
            boolean r9 = r4 instanceof java.lang.String
            if (r9 == 0) goto L_0x0128
            java.lang.String r4 = (java.lang.String) r4
            int r5 = r4.length()
            if (r5 > r11) goto L_0x00e5
            float r4 = com.alibaba.fastjson.util.TypeUtils.parseFloat(r4)
            goto L_0x00e9
        L_0x00e5:
            float r4 = java.lang.Float.parseFloat(r4)
        L_0x00e9:
            r7.setFloat(r0, r4)
            goto L_0x001d
        L_0x00ee:
            java.lang.Class r10 = java.lang.Double.TYPE
            if (r9 != r10) goto L_0x011b
            boolean r9 = r4 instanceof java.lang.Number
            if (r9 == 0) goto L_0x0101
            java.lang.Number r4 = (java.lang.Number) r4
            double r4 = r4.doubleValue()
            r7.setDouble(r0, r4)
            goto L_0x001d
        L_0x0101:
            boolean r9 = r4 instanceof java.lang.String
            if (r9 == 0) goto L_0x0128
            java.lang.String r4 = (java.lang.String) r4
            int r5 = r4.length()
            if (r5 > r11) goto L_0x0112
            double r4 = com.alibaba.fastjson.util.TypeUtils.parseDouble(r4)
            goto L_0x0116
        L_0x0112:
            double r4 = java.lang.Double.parseDouble(r4)
        L_0x0116:
            r7.setDouble(r0, r4)
            goto L_0x001d
        L_0x011b:
            if (r4 == 0) goto L_0x0128
            java.lang.Class r9 = r4.getClass()
            if (r8 != r9) goto L_0x0128
            r7.set(r0, r4)
            goto L_0x001d
        L_0x0128:
            java.lang.String r6 = r6.format
            if (r6 == 0) goto L_0x0135
            java.lang.Class<java.util.Date> r7 = java.util.Date.class
            if (r8 != r7) goto L_0x0135
            java.util.Date r4 = com.alibaba.fastjson.util.TypeUtils.castToDate(r4, r6)
            goto L_0x015e
        L_0x0135:
            if (r6 == 0) goto L_0x014f
            boolean r7 = r8 instanceof java.lang.Class
            if (r7 == 0) goto L_0x014f
            r7 = r8
            java.lang.Class r7 = (java.lang.Class) r7
            java.lang.String r7 = r7.getName()
            java.lang.String r9 = "java.time.LocalDateTime"
            boolean r7 = r7.equals(r9)
            if (r7 == 0) goto L_0x014f
            java.lang.Object r4 = com.alibaba.fastjson.parser.deserializer.Jdk8DateCodec.castToLocalDateTime(r4, r6)
            goto L_0x015e
        L_0x014f:
            boolean r6 = r8 instanceof java.lang.reflect.ParameterizedType
            if (r6 == 0) goto L_0x015a
            java.lang.reflect.ParameterizedType r8 = (java.lang.reflect.ParameterizedType) r8
            java.lang.Object r4 = com.alibaba.fastjson.util.TypeUtils.cast((java.lang.Object) r4, (java.lang.reflect.ParameterizedType) r8, (com.alibaba.fastjson.parser.ParserConfig) r14)
            goto L_0x015e
        L_0x015a:
            java.lang.Object r4 = com.alibaba.fastjson.util.TypeUtils.cast((java.lang.Object) r4, (java.lang.reflect.Type) r8, (com.alibaba.fastjson.parser.ParserConfig) r14)
        L_0x015e:
            r5.setValue((java.lang.Object) r0, (java.lang.Object) r4)
            goto L_0x001d
        L_0x0163:
            com.alibaba.fastjson.util.JavaBeanInfo r13 = r12.beanInfo
            java.lang.reflect.Method r13 = r13.buildMethod
            if (r13 == 0) goto L_0x017d
            com.alibaba.fastjson.util.JavaBeanInfo r13 = r12.beanInfo     // Catch:{ Exception -> 0x0174 }
            java.lang.reflect.Method r13 = r13.buildMethod     // Catch:{ Exception -> 0x0174 }
            java.lang.Object[] r14 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x0174 }
            java.lang.Object r13 = r13.invoke(r0, r14)     // Catch:{ Exception -> 0x0174 }
            return r13
        L_0x0174:
            r13 = move-exception
            com.alibaba.fastjson.JSONException r14 = new com.alibaba.fastjson.JSONException
            java.lang.String r0 = "build object error"
            r14.<init>(r0, r13)
            throw r14
        L_0x017d:
            return r0
        L_0x017e:
            com.alibaba.fastjson.util.JavaBeanInfo r0 = r12.beanInfo
            com.alibaba.fastjson.util.FieldInfo[] r0 = r0.fields
            int r4 = r0.length
            java.lang.Object[] r5 = new java.lang.Object[r4]
            r7 = r2
            r6 = 0
        L_0x0187:
            if (r6 >= r4) goto L_0x01f8
            r8 = r0[r6]
            java.lang.String r9 = r8.name
            java.lang.Object r9 = r13.get(r9)
            if (r9 != 0) goto L_0x01f3
            java.lang.Class<?> r10 = r8.fieldClass
            java.lang.Class r11 = java.lang.Integer.TYPE
            if (r10 != r11) goto L_0x019e
            java.lang.Integer r9 = java.lang.Integer.valueOf(r3)
            goto L_0x01e3
        L_0x019e:
            java.lang.Class r11 = java.lang.Long.TYPE
            if (r10 != r11) goto L_0x01a9
            r9 = 0
            java.lang.Long r9 = java.lang.Long.valueOf(r9)
            goto L_0x01e3
        L_0x01a9:
            java.lang.Class r11 = java.lang.Short.TYPE
            if (r10 != r11) goto L_0x01b2
            java.lang.Short r9 = java.lang.Short.valueOf(r3)
            goto L_0x01e3
        L_0x01b2:
            java.lang.Class r11 = java.lang.Byte.TYPE
            if (r10 != r11) goto L_0x01bb
            java.lang.Byte r9 = java.lang.Byte.valueOf(r3)
            goto L_0x01e3
        L_0x01bb:
            java.lang.Class r11 = java.lang.Float.TYPE
            if (r10 != r11) goto L_0x01c5
            r9 = 0
            java.lang.Float r9 = java.lang.Float.valueOf(r9)
            goto L_0x01e3
        L_0x01c5:
            java.lang.Class r11 = java.lang.Double.TYPE
            if (r10 != r11) goto L_0x01d0
            r9 = 0
            java.lang.Double r9 = java.lang.Double.valueOf(r9)
            goto L_0x01e3
        L_0x01d0:
            java.lang.Class r11 = java.lang.Character.TYPE
            if (r10 != r11) goto L_0x01db
            r9 = 48
            java.lang.Character r9 = java.lang.Character.valueOf(r9)
            goto L_0x01e3
        L_0x01db:
            java.lang.Class r11 = java.lang.Boolean.TYPE
            if (r10 != r11) goto L_0x01e3
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r3)
        L_0x01e3:
            if (r7 != 0) goto L_0x01ea
            java.util.HashMap r7 = new java.util.HashMap
            r7.<init>()
        L_0x01ea:
            java.lang.String r8 = r8.name
            java.lang.Integer r10 = java.lang.Integer.valueOf(r6)
            r7.put(r8, r10)
        L_0x01f3:
            r5[r6] = r9
            int r6 = r6 + 1
            goto L_0x0187
        L_0x01f8:
            if (r7 == 0) goto L_0x0231
            java.util.Set r13 = r13.entrySet()
            java.util.Iterator r13 = r13.iterator()
        L_0x0202:
            boolean r0 = r13.hasNext()
            if (r0 == 0) goto L_0x0231
            java.lang.Object r0 = r13.next()
            java.util.Map$Entry r0 = (java.util.Map.Entry) r0
            java.lang.Object r6 = r0.getKey()
            java.lang.String r6 = (java.lang.String) r6
            java.lang.Object r0 = r0.getValue()
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer r6 = r12.smartMatch(r6)
            if (r6 == 0) goto L_0x0202
            com.alibaba.fastjson.util.FieldInfo r6 = r6.fieldInfo
            java.lang.String r6 = r6.name
            java.lang.Object r6 = r7.get(r6)
            java.lang.Integer r6 = (java.lang.Integer) r6
            if (r6 == 0) goto L_0x0202
            int r6 = r6.intValue()
            r5[r6] = r0
            goto L_0x0202
        L_0x0231:
            com.alibaba.fastjson.util.JavaBeanInfo r13 = r12.beanInfo
            java.lang.reflect.Constructor<?> r13 = r13.creatorConstructor
            if (r13 == 0) goto L_0x02fb
            com.alibaba.fastjson.util.JavaBeanInfo r13 = r12.beanInfo
            boolean r13 = r13.f0kotlin
            if (r13 == 0) goto L_0x027f
            r13 = 0
            r0 = 0
        L_0x023f:
            if (r13 >= r4) goto L_0x0280
            r2 = r5[r13]
            if (r2 != 0) goto L_0x0260
            com.alibaba.fastjson.util.JavaBeanInfo r2 = r12.beanInfo
            com.alibaba.fastjson.util.FieldInfo[] r2 = r2.fields
            if (r2 == 0) goto L_0x027c
            com.alibaba.fastjson.util.JavaBeanInfo r2 = r12.beanInfo
            com.alibaba.fastjson.util.FieldInfo[] r2 = r2.fields
            int r2 = r2.length
            if (r13 >= r2) goto L_0x027c
            com.alibaba.fastjson.util.JavaBeanInfo r2 = r12.beanInfo
            com.alibaba.fastjson.util.FieldInfo[] r2 = r2.fields
            r2 = r2[r13]
            java.lang.Class<?> r2 = r2.fieldClass
            java.lang.Class<java.lang.String> r6 = java.lang.String.class
            if (r2 != r6) goto L_0x027c
            r0 = 1
            goto L_0x027c
        L_0x0260:
            java.lang.Class r6 = r2.getClass()
            com.alibaba.fastjson.util.JavaBeanInfo r7 = r12.beanInfo
            com.alibaba.fastjson.util.FieldInfo[] r7 = r7.fields
            r7 = r7[r13]
            java.lang.Class<?> r7 = r7.fieldClass
            if (r6 == r7) goto L_0x027c
            com.alibaba.fastjson.util.JavaBeanInfo r6 = r12.beanInfo
            com.alibaba.fastjson.util.FieldInfo[] r6 = r6.fields
            r6 = r6[r13]
            java.lang.Class<?> r6 = r6.fieldClass
            java.lang.Object r2 = com.alibaba.fastjson.util.TypeUtils.cast((java.lang.Object) r2, r6, (com.alibaba.fastjson.parser.ParserConfig) r14)
            r5[r13] = r2
        L_0x027c:
            int r13 = r13 + 1
            goto L_0x023f
        L_0x027f:
            r0 = 0
        L_0x0280:
            java.lang.String r13 = "create instance error, "
            if (r0 == 0) goto L_0x02d3
            com.alibaba.fastjson.util.JavaBeanInfo r14 = r12.beanInfo
            java.lang.reflect.Constructor<?> r14 = r14.kotlinDefaultConstructor
            if (r14 == 0) goto L_0x02d3
            com.alibaba.fastjson.util.JavaBeanInfo r14 = r12.beanInfo     // Catch:{ Exception -> 0x02b5 }
            java.lang.reflect.Constructor<?> r14 = r14.kotlinDefaultConstructor     // Catch:{ Exception -> 0x02b5 }
            java.lang.Object[] r0 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x02b5 }
            java.lang.Object r14 = r14.newInstance(r0)     // Catch:{ Exception -> 0x02b5 }
        L_0x0294:
            if (r3 >= r4) goto L_0x02b3
            r0 = r5[r3]     // Catch:{ Exception -> 0x02b5 }
            if (r0 == 0) goto L_0x02b0
            com.alibaba.fastjson.util.JavaBeanInfo r1 = r12.beanInfo     // Catch:{ Exception -> 0x02b5 }
            com.alibaba.fastjson.util.FieldInfo[] r1 = r1.fields     // Catch:{ Exception -> 0x02b5 }
            if (r1 == 0) goto L_0x02b0
            com.alibaba.fastjson.util.JavaBeanInfo r1 = r12.beanInfo     // Catch:{ Exception -> 0x02b5 }
            com.alibaba.fastjson.util.FieldInfo[] r1 = r1.fields     // Catch:{ Exception -> 0x02b5 }
            int r1 = r1.length     // Catch:{ Exception -> 0x02b5 }
            if (r3 >= r1) goto L_0x02b0
            com.alibaba.fastjson.util.JavaBeanInfo r1 = r12.beanInfo     // Catch:{ Exception -> 0x02b5 }
            com.alibaba.fastjson.util.FieldInfo[] r1 = r1.fields     // Catch:{ Exception -> 0x02b5 }
            r1 = r1[r3]     // Catch:{ Exception -> 0x02b5 }
            r1.set(r14, r0)     // Catch:{ Exception -> 0x02b5 }
        L_0x02b0:
            int r3 = r3 + 1
            goto L_0x0294
        L_0x02b3:
            r2 = r14
            goto L_0x032a
        L_0x02b5:
            r14 = move-exception
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r13)
            com.alibaba.fastjson.util.JavaBeanInfo r13 = r12.beanInfo
            java.lang.reflect.Constructor<?> r13 = r13.creatorConstructor
            java.lang.String r13 = r13.toGenericString()
            r1.append(r13)
            java.lang.String r13 = r1.toString()
            r0.<init>(r13, r14)
            throw r0
        L_0x02d3:
            com.alibaba.fastjson.util.JavaBeanInfo r14 = r12.beanInfo     // Catch:{ Exception -> 0x02dd }
            java.lang.reflect.Constructor<?> r14 = r14.creatorConstructor     // Catch:{ Exception -> 0x02dd }
            java.lang.Object r13 = r14.newInstance(r5)     // Catch:{ Exception -> 0x02dd }
            r2 = r13
            goto L_0x032a
        L_0x02dd:
            r14 = move-exception
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r13)
            com.alibaba.fastjson.util.JavaBeanInfo r13 = r12.beanInfo
            java.lang.reflect.Constructor<?> r13 = r13.creatorConstructor
            java.lang.String r13 = r13.toGenericString()
            r1.append(r13)
            java.lang.String r13 = r1.toString()
            r0.<init>(r13, r14)
            throw r0
        L_0x02fb:
            com.alibaba.fastjson.util.JavaBeanInfo r13 = r12.beanInfo
            java.lang.reflect.Method r13 = r13.factoryMethod
            if (r13 == 0) goto L_0x032a
            com.alibaba.fastjson.util.JavaBeanInfo r13 = r12.beanInfo     // Catch:{ Exception -> 0x030a }
            java.lang.reflect.Method r13 = r13.factoryMethod     // Catch:{ Exception -> 0x030a }
            java.lang.Object r2 = r13.invoke(r2, r5)     // Catch:{ Exception -> 0x030a }
            goto L_0x032a
        L_0x030a:
            r13 = move-exception
            com.alibaba.fastjson.JSONException r14 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "create factory method error, "
            r0.append(r1)
            com.alibaba.fastjson.util.JavaBeanInfo r1 = r12.beanInfo
            java.lang.reflect.Method r1 = r1.factoryMethod
            java.lang.String r1 = r1.toString()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r14.<init>(r0, r13)
            throw r14
        L_0x032a:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.createInstance(java.util.Map, com.alibaba.fastjson.parser.ParserConfig):java.lang.Object");
    }

    public Type getFieldType(int i) {
        return this.sortedFieldDeserializers[i].fieldInfo.fieldType;
    }

    /* access modifiers changed from: protected */
    public Object parseRest(DefaultJSONParser defaultJSONParser, Type type, Object obj, Object obj2, int i) {
        return parseRest(defaultJSONParser, type, obj, obj2, i, new int[0]);
    }

    /* access modifiers changed from: protected */
    public Object parseRest(DefaultJSONParser defaultJSONParser, Type type, Object obj, Object obj2, int i, int[] iArr) {
        return deserialze(defaultJSONParser, type, obj, obj2, i, iArr);
    }

    protected static JavaBeanDeserializer getSeeAlso(ParserConfig parserConfig, JavaBeanInfo javaBeanInfo, String str) {
        if (javaBeanInfo.jsonType == null) {
            return null;
        }
        for (Class deserializer : javaBeanInfo.jsonType.seeAlso()) {
            ObjectDeserializer deserializer2 = parserConfig.getDeserializer((Type) deserializer);
            if (deserializer2 instanceof JavaBeanDeserializer) {
                JavaBeanDeserializer javaBeanDeserializer = (JavaBeanDeserializer) deserializer2;
                JavaBeanInfo javaBeanInfo2 = javaBeanDeserializer.beanInfo;
                if (javaBeanInfo2.typeName.equals(str)) {
                    return javaBeanDeserializer;
                }
                JavaBeanDeserializer seeAlso = getSeeAlso(parserConfig, javaBeanInfo2, str);
                if (seeAlso != null) {
                    return seeAlso;
                }
            }
        }
        return null;
    }

    protected static void parseArray(Collection collection, ObjectDeserializer objectDeserializer, DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        JSONLexerBase jSONLexerBase = (JSONLexerBase) defaultJSONParser.lexer;
        int i = jSONLexerBase.token();
        if (i == 8) {
            jSONLexerBase.nextToken(16);
            jSONLexerBase.token();
            return;
        }
        if (i != 14) {
            defaultJSONParser.throwException(i);
        }
        if (jSONLexerBase.getCurrent() == '[') {
            jSONLexerBase.next();
            jSONLexerBase.setToken(14);
        } else {
            jSONLexerBase.nextToken(14);
        }
        if (jSONLexerBase.token() == 15) {
            jSONLexerBase.nextToken();
            return;
        }
        int i2 = 0;
        while (true) {
            collection.add(objectDeserializer.deserialze(defaultJSONParser, type, Integer.valueOf(i2)));
            i2++;
            if (jSONLexerBase.token() != 16) {
                break;
            } else if (jSONLexerBase.getCurrent() == '[') {
                jSONLexerBase.next();
                jSONLexerBase.setToken(14);
            } else {
                jSONLexerBase.nextToken(14);
            }
        }
        int i3 = jSONLexerBase.token();
        if (i3 != 15) {
            defaultJSONParser.throwException(i3);
        }
        if (jSONLexerBase.getCurrent() == ',') {
            jSONLexerBase.next();
            jSONLexerBase.setToken(16);
            return;
        }
        jSONLexerBase.nextToken(16);
    }
}
