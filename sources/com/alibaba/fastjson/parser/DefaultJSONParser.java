package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.JSONPathException;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessable;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldTypeResolver;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.ResolveFieldDeserializer;
import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.LongCodec;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.StringCodec;
import com.alibaba.fastjson.util.TypeUtils;
import com.taobao.weex.el.parse.Operators;
import java.io.Closeable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class DefaultJSONParser implements Closeable {
    public static final int NONE = 0;
    public static final int NeedToResolve = 1;
    public static final int TypeNameRedirect = 2;
    private static final Set<Class<?>> primitiveClasses;
    private String[] autoTypeAccept;
    private boolean autoTypeEnable;
    protected ParserConfig config;
    protected ParseContext context;
    private ParseContext[] contextArray;
    private int contextArrayIndex;
    private DateFormat dateFormat;
    private String dateFormatPattern;
    private List<ExtraProcessor> extraProcessors;
    private List<ExtraTypeProvider> extraTypeProviders;
    protected FieldTypeResolver fieldTypeResolver;
    public final Object input;
    protected transient BeanContext lastBeanContext;
    public final JSONLexer lexer;
    private int objectKeyLevel;
    public int resolveStatus;
    private List<ResolveTask> resolveTaskList;
    public final SymbolTable symbolTable;

    static {
        HashSet hashSet = new HashSet();
        primitiveClasses = hashSet;
        hashSet.addAll(Arrays.asList(new Class[]{Boolean.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Boolean.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, BigInteger.class, BigDecimal.class, String.class}));
    }

    public String getDateFomartPattern() {
        return this.dateFormatPattern;
    }

    public DateFormat getDateFormat() {
        if (this.dateFormat == null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.dateFormatPattern, this.lexer.getLocale());
            this.dateFormat = simpleDateFormat;
            simpleDateFormat.setTimeZone(this.lexer.getTimeZone());
        }
        return this.dateFormat;
    }

    public void setDateFormat(String str) {
        this.dateFormatPattern = str;
        this.dateFormat = null;
    }

    public void setDateFomrat(DateFormat dateFormat2) {
        setDateFormat(dateFormat2);
    }

    public void setDateFormat(DateFormat dateFormat2) {
        this.dateFormat = dateFormat2;
    }

    public DefaultJSONParser(String str) {
        this(str, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
    }

    public DefaultJSONParser(String str, ParserConfig parserConfig) {
        this((Object) str, (JSONLexer) new JSONScanner(str, JSON.DEFAULT_PARSER_FEATURE), parserConfig);
    }

    public DefaultJSONParser(String str, ParserConfig parserConfig, int i) {
        this((Object) str, (JSONLexer) new JSONScanner(str, i), parserConfig);
    }

    public DefaultJSONParser(char[] cArr, int i, ParserConfig parserConfig, int i2) {
        this((Object) cArr, (JSONLexer) new JSONScanner(cArr, i, i2), parserConfig);
    }

    public DefaultJSONParser(JSONLexer jSONLexer) {
        this(jSONLexer, ParserConfig.getGlobalInstance());
    }

    public DefaultJSONParser(JSONLexer jSONLexer, ParserConfig parserConfig) {
        this((Object) null, jSONLexer, parserConfig);
    }

    public DefaultJSONParser(Object obj, JSONLexer jSONLexer, ParserConfig parserConfig) {
        this.dateFormatPattern = JSON.DEFFAULT_DATE_FORMAT;
        this.contextArrayIndex = 0;
        this.resolveStatus = 0;
        this.extraTypeProviders = null;
        this.extraProcessors = null;
        this.fieldTypeResolver = null;
        this.objectKeyLevel = 0;
        this.autoTypeAccept = null;
        this.lexer = jSONLexer;
        this.input = obj;
        this.config = parserConfig;
        this.symbolTable = parserConfig.symbolTable;
        char current = jSONLexer.getCurrent();
        if (current == '{') {
            jSONLexer.next();
            ((JSONLexerBase) jSONLexer).token = 12;
        } else if (current == '[') {
            jSONLexer.next();
            ((JSONLexerBase) jSONLexer).token = 14;
        } else {
            jSONLexer.nextToken();
        }
    }

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    public String getInput() {
        Object obj = this.input;
        if (obj instanceof char[]) {
            return new String((char[]) this.input);
        }
        return obj.toString();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v7, resolved type: java.lang.Number} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v9, resolved type: com.alibaba.fastjson.JSONArray} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v12, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v35, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v36, resolved type: java.util.Date} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v37, resolved type: java.lang.Number} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v89, resolved type: java.lang.Number} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v24, resolved type: com.alibaba.fastjson.JSONArray} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v25, resolved type: com.alibaba.fastjson.JSONArray} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v92, resolved type: java.util.Date} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v93, resolved type: java.util.Date} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v94, resolved type: java.util.Date} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v95, resolved type: java.util.Date} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v96, resolved type: java.util.Date} */
    /* JADX WARNING: Code restructure failed: missing block: B:150:0x029d, code lost:
        r4.nextToken(16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:151:0x02a8, code lost:
        if (r4.token() != 13) goto L_0x02fb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:152:0x02aa, code lost:
        r4.nextToken(16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:155:0x02b5, code lost:
        if ((r1.config.getDeserializer((java.lang.reflect.Type) r7) instanceof com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) == false) goto L_0x02be;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:156:0x02b7, code lost:
        r0 = com.alibaba.fastjson.util.TypeUtils.cast((java.lang.Object) r0, r7, r1.config);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:157:0x02be, code lost:
        r0 = r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:158:0x02bf, code lost:
        if (r0 != null) goto L_0x02ee;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:160:0x02c3, code lost:
        if (r7 != java.lang.Cloneable.class) goto L_0x02cb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:161:0x02c5, code lost:
        r0 = new java.util.HashMap();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:163:0x02d1, code lost:
        if ("java.util.Collections$EmptyMap".equals(r6) == false) goto L_0x02d8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:164:0x02d3, code lost:
        r0 = java.util.Collections.emptyMap();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:166:0x02de, code lost:
        if ("java.util.Collections$UnmodifiableMap".equals(r6) == false) goto L_0x02ea;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:167:0x02e0, code lost:
        r0 = java.util.Collections.unmodifiableMap(new java.util.HashMap());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:168:0x02ea, code lost:
        r0 = r7.newInstance();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:169:0x02ee, code lost:
        setContext(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:170:0x02f1, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:175:0x02fb, code lost:
        setResolveStatus(2);
        r3 = r1.context;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:176:0x0301, code lost:
        if (r3 == null) goto L_0x0312;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:177:0x0303, code lost:
        if (r2 == null) goto L_0x0312;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:179:0x0307, code lost:
        if ((r2 instanceof java.lang.Integer) != false) goto L_0x0312;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:181:0x030d, code lost:
        if ((r3.fieldName instanceof java.lang.Integer) != false) goto L_0x0312;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:182:0x030f, code lost:
        popContext();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:184:0x0316, code lost:
        if (r18.size() <= 0) goto L_0x0329;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:185:0x0318, code lost:
        r0 = com.alibaba.fastjson.util.TypeUtils.cast((java.lang.Object) r0, r7, r1.config);
        setResolveStatus(0);
        parseObject(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:186:0x0325, code lost:
        setContext(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:187:0x0328, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:189:?, code lost:
        r0 = r1.config.getDeserializer((java.lang.reflect.Type) r7);
        r3 = r0.getClass();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:190:0x0339, code lost:
        if (com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.class.isAssignableFrom(r3) == false) goto L_0x0348;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:192:0x033d, code lost:
        if (r3 == com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.class) goto L_0x0348;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:194:0x0341, code lost:
        if (r3 == com.alibaba.fastjson.parser.deserializer.ThrowableDeserializer.class) goto L_0x0348;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:195:0x0343, code lost:
        setResolveStatus(0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:197:0x034a, code lost:
        if ((r0 instanceof com.alibaba.fastjson.parser.deserializer.MapDeserializer) == false) goto L_0x0350;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:198:0x034c, code lost:
        setResolveStatus(0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:199:0x0350, code lost:
        r0 = r0.deserialze(r1, r7, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:200:0x0354, code lost:
        setContext(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:201:0x0357, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:208:0x0366, code lost:
        if (r18.size() == 0) goto L_0x0368;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:218:0x039c, code lost:
        if ("@".equals(r6) == false) goto L_0x03b8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:219:0x039e, code lost:
        r0 = r1.context;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:220:0x03a0, code lost:
        if (r0 == null) goto L_0x040c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:221:0x03a2, code lost:
        r2 = r0.object;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:222:0x03a6, code lost:
        if ((r2 instanceof java.lang.Object[]) != false) goto L_0x03b6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:224:0x03aa, code lost:
        if ((r2 instanceof java.util.Collection) == false) goto L_0x03ad;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:227:0x03af, code lost:
        if (r0.parent == null) goto L_0x040c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:228:0x03b1, code lost:
        r6 = r0.parent.object;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:229:0x03b6, code lost:
        r6 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:231:0x03be, code lost:
        if (io.dcloud.common.util.PdrUtil.FILE_PATH_ENTRY_BACK.equals(r6) == false) goto L_0x03d3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:233:0x03c2, code lost:
        if (r5.object == null) goto L_0x03c7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:234:0x03c4, code lost:
        r6 = r5.object;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:235:0x03c7, code lost:
        addResolveTask(new com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask(r5, r6));
        setResolveStatus(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:237:0x03d9, code lost:
        if (com.taobao.weex.el.parse.Operators.DOLLAR_STR.equals(r6) == false) goto L_0x03f7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:238:0x03db, code lost:
        r0 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:240:0x03de, code lost:
        if (r0.parent == null) goto L_0x03e3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:241:0x03e0, code lost:
        r0 = r0.parent;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:243:0x03e5, code lost:
        if (r0.object == null) goto L_0x03eb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:244:0x03e7, code lost:
        r6 = r0.object;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:245:0x03eb, code lost:
        addResolveTask(new com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask(r0, r6));
        setResolveStatus(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:247:0x03ff, code lost:
        if (com.alibaba.fastjson.JSONPath.compile(r6).isRef() == false) goto L_0x040e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:248:0x0401, code lost:
        addResolveTask(new com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask(r5, r6));
        setResolveStatus(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:249:0x040c, code lost:
        r6 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:250:0x040e, code lost:
        r6 = new com.alibaba.fastjson.JSONObject().fluentPut("$ref", r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:252:0x041d, code lost:
        if (r4.token() != 13) goto L_0x0428;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:253:0x041f, code lost:
        r4.nextToken(16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:254:0x0424, code lost:
        setContext(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:255:0x0427, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:258:0x0442, code lost:
        throw new com.alibaba.fastjson.JSONException("syntax error, " + r4.info());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:303:0x04f5, code lost:
        if (r8 != '}') goto L_0x0507;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:304:0x04f7, code lost:
        r4.next();
        r4.resetStringPosition();
        r4.nextToken();
        setContext(r7, r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:305:0x0503, code lost:
        setContext(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:306:0x0506, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:309:0x0527, code lost:
        throw new com.alibaba.fastjson.JSONException("syntax error, position at " + r4.pos() + ", name " + r14);
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:116:0x0215 A[Catch:{ Exception -> 0x02f2, NumberFormatException -> 0x019f, all -> 0x06a6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:121:0x022e A[Catch:{ Exception -> 0x02f2, NumberFormatException -> 0x019f, all -> 0x06a6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:149:0x0297 A[Catch:{ Exception -> 0x02f2, NumberFormatException -> 0x019f, all -> 0x06a6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:202:0x0358  */
    /* JADX WARNING: Removed duplicated region for block: B:359:0x05ee A[Catch:{ Exception -> 0x02f2, NumberFormatException -> 0x019f, all -> 0x06a6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:364:0x05fa A[Catch:{ Exception -> 0x02f2, NumberFormatException -> 0x019f, all -> 0x06a6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:367:0x0606 A[Catch:{ Exception -> 0x02f2, NumberFormatException -> 0x019f, all -> 0x06a6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:373:0x061b A[SYNTHETIC, Splitter:B:373:0x061b] */
    /* JADX WARNING: Removed duplicated region for block: B:403:0x0611 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:407:0x029d A[EDGE_INSN: B:407:0x029d->B:150:0x029d ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:418:0x0186 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x0184 A[Catch:{ Exception -> 0x02f2, NumberFormatException -> 0x019f, all -> 0x06a6 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object parseObject(java.util.Map r18, java.lang.Object r19) {
        /*
            r17 = this;
            r1 = r17
            r0 = r18
            r2 = r19
            java.lang.String r3 = "parse number key error"
            com.alibaba.fastjson.parser.JSONLexer r4 = r1.lexer
            int r5 = r4.token()
            r6 = 0
            r7 = 8
            if (r5 != r7) goto L_0x0017
            r4.nextToken()
            return r6
        L_0x0017:
            int r5 = r4.token()
            r7 = 13
            if (r5 != r7) goto L_0x0023
            r4.nextToken()
            return r0
        L_0x0023:
            int r5 = r4.token()
            r8 = 4
            if (r5 != r8) goto L_0x0038
            java.lang.String r5 = r4.stringVal()
            int r5 = r5.length()
            if (r5 != 0) goto L_0x0038
            r4.nextToken()
            return r0
        L_0x0038:
            int r5 = r4.token()
            r9 = 12
            r10 = 16
            if (r5 == r9) goto L_0x0070
            int r5 = r4.token()
            if (r5 != r10) goto L_0x0049
            goto L_0x0070
        L_0x0049:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "syntax error, expect {, actual "
            r2.append(r3)
            java.lang.String r3 = r4.tokenName()
            r2.append(r3)
            java.lang.String r3 = ", "
            r2.append(r3)
            java.lang.String r3 = r4.info()
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r0.<init>(r2)
            throw r0
        L_0x0070:
            com.alibaba.fastjson.parser.ParseContext r5 = r1.context
            boolean r9 = r0 instanceof com.alibaba.fastjson.JSONObject     // Catch:{ all -> 0x06a6 }
            if (r9 == 0) goto L_0x007e
            r11 = r0
            com.alibaba.fastjson.JSONObject r11 = (com.alibaba.fastjson.JSONObject) r11     // Catch:{ all -> 0x06a6 }
            java.util.Map r11 = r11.getInnerMap()     // Catch:{ all -> 0x06a6 }
            goto L_0x007f
        L_0x007e:
            r11 = r0
        L_0x007f:
            r13 = 0
        L_0x0080:
            r4.skipWhitespace()     // Catch:{ all -> 0x06a6 }
            char r14 = r4.getCurrent()     // Catch:{ all -> 0x06a6 }
            com.alibaba.fastjson.parser.Feature r15 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas     // Catch:{ all -> 0x06a6 }
            boolean r15 = r4.isEnabled((com.alibaba.fastjson.parser.Feature) r15)     // Catch:{ all -> 0x06a6 }
            r8 = 44
            if (r15 == 0) goto L_0x009e
        L_0x0091:
            if (r14 != r8) goto L_0x009e
            r4.next()     // Catch:{ all -> 0x06a6 }
            r4.skipWhitespace()     // Catch:{ all -> 0x06a6 }
            char r14 = r4.getCurrent()     // Catch:{ all -> 0x06a6 }
            goto L_0x0091
        L_0x009e:
            java.lang.String r6 = ", name "
            java.lang.String r12 = "expect ':' at "
            r15 = 58
            r7 = 34
            java.lang.String r8 = "syntax error"
            r10 = 1
            if (r14 != r7) goto L_0x00dc
            com.alibaba.fastjson.parser.SymbolTable r14 = r1.symbolTable     // Catch:{ all -> 0x06a6 }
            java.lang.String r14 = r4.scanSymbol(r14, r7)     // Catch:{ all -> 0x06a6 }
            r4.skipWhitespace()     // Catch:{ all -> 0x06a6 }
            char r7 = r4.getCurrent()     // Catch:{ all -> 0x06a6 }
            if (r7 != r15) goto L_0x00bd
        L_0x00ba:
            r7 = 0
            goto L_0x0213
        L_0x00bd:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x06a6 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x06a6 }
            r2.<init>()     // Catch:{ all -> 0x06a6 }
            r2.append(r12)     // Catch:{ all -> 0x06a6 }
            int r3 = r4.pos()     // Catch:{ all -> 0x06a6 }
            r2.append(r3)     // Catch:{ all -> 0x06a6 }
            r2.append(r6)     // Catch:{ all -> 0x06a6 }
            r2.append(r14)     // Catch:{ all -> 0x06a6 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x06a6 }
            r0.<init>(r2)     // Catch:{ all -> 0x06a6 }
            throw r0     // Catch:{ all -> 0x06a6 }
        L_0x00dc:
            r7 = 125(0x7d, float:1.75E-43)
            if (r14 != r7) goto L_0x0107
            r4.next()     // Catch:{ all -> 0x06a6 }
            r4.resetStringPosition()     // Catch:{ all -> 0x06a6 }
            r4.nextToken()     // Catch:{ all -> 0x06a6 }
            if (r13 != 0) goto L_0x0103
            com.alibaba.fastjson.parser.ParseContext r3 = r1.context     // Catch:{ all -> 0x06a6 }
            if (r3 == 0) goto L_0x00fc
            java.lang.Object r3 = r3.fieldName     // Catch:{ all -> 0x06a6 }
            if (r2 != r3) goto L_0x00fc
            com.alibaba.fastjson.parser.ParseContext r3 = r1.context     // Catch:{ all -> 0x06a6 }
            java.lang.Object r3 = r3.object     // Catch:{ all -> 0x06a6 }
            if (r0 != r3) goto L_0x00fc
            com.alibaba.fastjson.parser.ParseContext r5 = r1.context     // Catch:{ all -> 0x06a6 }
            goto L_0x0103
        L_0x00fc:
            com.alibaba.fastjson.parser.ParseContext r2 = r17.setContext(r18, r19)     // Catch:{ all -> 0x06a6 }
            if (r5 != 0) goto L_0x0103
            r5 = r2
        L_0x0103:
            r1.setContext(r5)
            return r0
        L_0x0107:
            r7 = 39
            if (r14 != r7) goto L_0x0142
            com.alibaba.fastjson.parser.Feature r14 = com.alibaba.fastjson.parser.Feature.AllowSingleQuotes     // Catch:{ all -> 0x06a6 }
            boolean r14 = r4.isEnabled((com.alibaba.fastjson.parser.Feature) r14)     // Catch:{ all -> 0x06a6 }
            if (r14 == 0) goto L_0x013c
            com.alibaba.fastjson.parser.SymbolTable r14 = r1.symbolTable     // Catch:{ all -> 0x06a6 }
            java.lang.String r14 = r4.scanSymbol(r14, r7)     // Catch:{ all -> 0x06a6 }
            r4.skipWhitespace()     // Catch:{ all -> 0x06a6 }
            char r7 = r4.getCurrent()     // Catch:{ all -> 0x06a6 }
            if (r7 != r15) goto L_0x0123
            goto L_0x00ba
        L_0x0123:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x06a6 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x06a6 }
            r2.<init>()     // Catch:{ all -> 0x06a6 }
            r2.append(r12)     // Catch:{ all -> 0x06a6 }
            int r3 = r4.pos()     // Catch:{ all -> 0x06a6 }
            r2.append(r3)     // Catch:{ all -> 0x06a6 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x06a6 }
            r0.<init>(r2)     // Catch:{ all -> 0x06a6 }
            throw r0     // Catch:{ all -> 0x06a6 }
        L_0x013c:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x06a6 }
            r0.<init>(r8)     // Catch:{ all -> 0x06a6 }
            throw r0     // Catch:{ all -> 0x06a6 }
        L_0x0142:
            r7 = 26
            if (r14 == r7) goto L_0x06a0
            r7 = 44
            if (r14 == r7) goto L_0x069a
            r7 = 48
            if (r14 < r7) goto L_0x0152
            r7 = 57
            if (r14 <= r7) goto L_0x0156
        L_0x0152:
            r7 = 45
            if (r14 != r7) goto L_0x01b8
        L_0x0156:
            r4.resetStringPosition()     // Catch:{ all -> 0x06a6 }
            r4.scanNumber()     // Catch:{ all -> 0x06a6 }
            int r7 = r4.token()     // Catch:{ NumberFormatException -> 0x019f }
            r12 = 2
            if (r7 != r12) goto L_0x0168
            java.lang.Number r7 = r4.integerValue()     // Catch:{ NumberFormatException -> 0x019f }
            goto L_0x016c
        L_0x0168:
            java.lang.Number r7 = r4.decimalValue(r10)     // Catch:{ NumberFormatException -> 0x019f }
        L_0x016c:
            com.alibaba.fastjson.parser.Feature r12 = com.alibaba.fastjson.parser.Feature.NonStringKeyAsString     // Catch:{ NumberFormatException -> 0x019f }
            boolean r12 = r4.isEnabled((com.alibaba.fastjson.parser.Feature) r12)     // Catch:{ NumberFormatException -> 0x019f }
            if (r12 != 0) goto L_0x0179
            if (r9 == 0) goto L_0x0177
            goto L_0x0179
        L_0x0177:
            r14 = r7
            goto L_0x017e
        L_0x0179:
            java.lang.String r7 = r7.toString()     // Catch:{ NumberFormatException -> 0x019f }
            goto L_0x0177
        L_0x017e:
            char r7 = r4.getCurrent()     // Catch:{ all -> 0x06a6 }
            if (r7 != r15) goto L_0x0186
            goto L_0x00ba
        L_0x0186:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x06a6 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x06a6 }
            r2.<init>()     // Catch:{ all -> 0x06a6 }
            r2.append(r3)     // Catch:{ all -> 0x06a6 }
            java.lang.String r3 = r4.info()     // Catch:{ all -> 0x06a6 }
            r2.append(r3)     // Catch:{ all -> 0x06a6 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x06a6 }
            r0.<init>(r2)     // Catch:{ all -> 0x06a6 }
            throw r0     // Catch:{ all -> 0x06a6 }
        L_0x019f:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x06a6 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x06a6 }
            r2.<init>()     // Catch:{ all -> 0x06a6 }
            r2.append(r3)     // Catch:{ all -> 0x06a6 }
            java.lang.String r3 = r4.info()     // Catch:{ all -> 0x06a6 }
            r2.append(r3)     // Catch:{ all -> 0x06a6 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x06a6 }
            r0.<init>(r2)     // Catch:{ all -> 0x06a6 }
            throw r0     // Catch:{ all -> 0x06a6 }
        L_0x01b8:
            r7 = 123(0x7b, float:1.72E-43)
            if (r14 == r7) goto L_0x0201
            r7 = 91
            if (r14 != r7) goto L_0x01c1
            goto L_0x0201
        L_0x01c1:
            com.alibaba.fastjson.parser.Feature r7 = com.alibaba.fastjson.parser.Feature.AllowUnQuotedFieldNames     // Catch:{ all -> 0x06a6 }
            boolean r7 = r4.isEnabled((com.alibaba.fastjson.parser.Feature) r7)     // Catch:{ all -> 0x06a6 }
            if (r7 == 0) goto L_0x01fb
            com.alibaba.fastjson.parser.SymbolTable r7 = r1.symbolTable     // Catch:{ all -> 0x06a6 }
            java.lang.String r14 = r4.scanSymbolUnQuoted(r7)     // Catch:{ all -> 0x06a6 }
            r4.skipWhitespace()     // Catch:{ all -> 0x06a6 }
            char r7 = r4.getCurrent()     // Catch:{ all -> 0x06a6 }
            if (r7 != r15) goto L_0x01da
            goto L_0x00ba
        L_0x01da:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x06a6 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x06a6 }
            r2.<init>()     // Catch:{ all -> 0x06a6 }
            r2.append(r12)     // Catch:{ all -> 0x06a6 }
            int r3 = r4.pos()     // Catch:{ all -> 0x06a6 }
            r2.append(r3)     // Catch:{ all -> 0x06a6 }
            java.lang.String r3 = ", actual "
            r2.append(r3)     // Catch:{ all -> 0x06a6 }
            r2.append(r7)     // Catch:{ all -> 0x06a6 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x06a6 }
            r0.<init>(r2)     // Catch:{ all -> 0x06a6 }
            throw r0     // Catch:{ all -> 0x06a6 }
        L_0x01fb:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x06a6 }
            r0.<init>(r8)     // Catch:{ all -> 0x06a6 }
            throw r0     // Catch:{ all -> 0x06a6 }
        L_0x0201:
            int r7 = r1.objectKeyLevel     // Catch:{ all -> 0x06a6 }
            int r12 = r7 + 1
            r1.objectKeyLevel = r12     // Catch:{ all -> 0x06a6 }
            r12 = 512(0x200, float:7.175E-43)
            if (r7 > r12) goto L_0x0692
            r4.nextToken()     // Catch:{ all -> 0x06a6 }
            java.lang.Object r14 = r17.parse()     // Catch:{ all -> 0x06a6 }
            r7 = 1
        L_0x0213:
            if (r7 != 0) goto L_0x021b
            r4.next()     // Catch:{ all -> 0x06a6 }
            r4.skipWhitespace()     // Catch:{ all -> 0x06a6 }
        L_0x021b:
            char r7 = r4.getCurrent()     // Catch:{ all -> 0x06a6 }
            r4.resetStringPosition()     // Catch:{ all -> 0x06a6 }
            java.lang.String r12 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY     // Catch:{ all -> 0x06a6 }
            if (r14 != r12) goto L_0x0358
            com.alibaba.fastjson.parser.Feature r12 = com.alibaba.fastjson.parser.Feature.DisableSpecialKeyDetect     // Catch:{ all -> 0x06a6 }
            boolean r12 = r4.isEnabled((com.alibaba.fastjson.parser.Feature) r12)     // Catch:{ all -> 0x06a6 }
            if (r12 != 0) goto L_0x0358
            com.alibaba.fastjson.parser.SymbolTable r6 = r1.symbolTable     // Catch:{ all -> 0x06a6 }
            r7 = 34
            java.lang.String r6 = r4.scanSymbol(r6, r7)     // Catch:{ all -> 0x06a6 }
            com.alibaba.fastjson.parser.Feature r7 = com.alibaba.fastjson.parser.Feature.IgnoreAutoType     // Catch:{ all -> 0x06a6 }
            boolean r7 = r4.isEnabled((com.alibaba.fastjson.parser.Feature) r7)     // Catch:{ all -> 0x06a6 }
            if (r7 == 0) goto L_0x0241
        L_0x023e:
            r15 = 4
            goto L_0x038e
        L_0x0241:
            if (r0 == 0) goto L_0x0256
            java.lang.Class r7 = r18.getClass()     // Catch:{ all -> 0x06a6 }
            java.lang.String r7 = r7.getName()     // Catch:{ all -> 0x06a6 }
            boolean r7 = r7.equals(r6)     // Catch:{ all -> 0x06a6 }
            if (r7 == 0) goto L_0x0256
            java.lang.Class r7 = r18.getClass()     // Catch:{ all -> 0x06a6 }
            goto L_0x0260
        L_0x0256:
            java.lang.String r7 = "java.util.HashMap"
            boolean r7 = r7.equals(r6)     // Catch:{ all -> 0x06a6 }
            if (r7 == 0) goto L_0x0262
            java.lang.Class<java.util.HashMap> r7 = java.util.HashMap.class
        L_0x0260:
            r12 = 0
            goto L_0x0295
        L_0x0262:
            java.lang.String r7 = "java.util.LinkedHashMap"
            boolean r7 = r7.equals(r6)     // Catch:{ all -> 0x06a6 }
            if (r7 == 0) goto L_0x026d
            java.lang.Class<java.util.LinkedHashMap> r7 = java.util.LinkedHashMap.class
            goto L_0x0260
        L_0x026d:
            r7 = 0
        L_0x026e:
            int r8 = r6.length()     // Catch:{ all -> 0x06a6 }
            if (r7 >= r8) goto L_0x0285
            char r8 = r6.charAt(r7)     // Catch:{ all -> 0x06a6 }
            r12 = 48
            if (r8 < r12) goto L_0x0284
            r12 = 57
            if (r8 <= r12) goto L_0x0281
            goto L_0x0284
        L_0x0281:
            int r7 = r7 + 1
            goto L_0x026e
        L_0x0284:
            r10 = 0
        L_0x0285:
            if (r10 != 0) goto L_0x0293
            com.alibaba.fastjson.parser.ParserConfig r7 = r1.config     // Catch:{ all -> 0x06a6 }
            int r8 = r4.getFeatures()     // Catch:{ all -> 0x06a6 }
            r12 = 0
            java.lang.Class r7 = r7.checkAutoType(r6, r12, r8)     // Catch:{ all -> 0x06a6 }
            goto L_0x0295
        L_0x0293:
            r12 = 0
            r7 = r12
        L_0x0295:
            if (r7 != 0) goto L_0x029d
            java.lang.String r7 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY     // Catch:{ all -> 0x06a6 }
            r11.put(r7, r6)     // Catch:{ all -> 0x06a6 }
            goto L_0x023e
        L_0x029d:
            r3 = 16
            r4.nextToken(r3)     // Catch:{ all -> 0x06a6 }
            int r8 = r4.token()     // Catch:{ all -> 0x06a6 }
            r9 = 13
            if (r8 != r9) goto L_0x02fb
            r4.nextToken(r3)     // Catch:{ all -> 0x06a6 }
            com.alibaba.fastjson.parser.ParserConfig r2 = r1.config     // Catch:{ Exception -> 0x02f2 }
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r2 = r2.getDeserializer((java.lang.reflect.Type) r7)     // Catch:{ Exception -> 0x02f2 }
            boolean r2 = r2 instanceof com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer     // Catch:{ Exception -> 0x02f2 }
            if (r2 == 0) goto L_0x02be
            com.alibaba.fastjson.parser.ParserConfig r2 = r1.config     // Catch:{ Exception -> 0x02f2 }
            java.lang.Object r0 = com.alibaba.fastjson.util.TypeUtils.cast((java.lang.Object) r0, r7, (com.alibaba.fastjson.parser.ParserConfig) r2)     // Catch:{ Exception -> 0x02f2 }
            goto L_0x02bf
        L_0x02be:
            r0 = r12
        L_0x02bf:
            if (r0 != 0) goto L_0x02ee
            java.lang.Class<java.lang.Cloneable> r0 = java.lang.Cloneable.class
            if (r7 != r0) goto L_0x02cb
            java.util.HashMap r0 = new java.util.HashMap     // Catch:{ Exception -> 0x02f2 }
            r0.<init>()     // Catch:{ Exception -> 0x02f2 }
            goto L_0x02ee
        L_0x02cb:
            java.lang.String r0 = "java.util.Collections$EmptyMap"
            boolean r0 = r0.equals(r6)     // Catch:{ Exception -> 0x02f2 }
            if (r0 == 0) goto L_0x02d8
            java.util.Map r0 = java.util.Collections.emptyMap()     // Catch:{ Exception -> 0x02f2 }
            goto L_0x02ee
        L_0x02d8:
            java.lang.String r0 = "java.util.Collections$UnmodifiableMap"
            boolean r0 = r0.equals(r6)     // Catch:{ Exception -> 0x02f2 }
            if (r0 == 0) goto L_0x02ea
            java.util.HashMap r0 = new java.util.HashMap     // Catch:{ Exception -> 0x02f2 }
            r0.<init>()     // Catch:{ Exception -> 0x02f2 }
            java.util.Map r0 = java.util.Collections.unmodifiableMap(r0)     // Catch:{ Exception -> 0x02f2 }
            goto L_0x02ee
        L_0x02ea:
            java.lang.Object r0 = r7.newInstance()     // Catch:{ Exception -> 0x02f2 }
        L_0x02ee:
            r1.setContext(r5)
            return r0
        L_0x02f2:
            r0 = move-exception
            com.alibaba.fastjson.JSONException r2 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x06a6 }
            java.lang.String r3 = "create instance error"
            r2.<init>(r3, r0)     // Catch:{ all -> 0x06a6 }
            throw r2     // Catch:{ all -> 0x06a6 }
        L_0x02fb:
            r3 = 2
            r1.setResolveStatus(r3)     // Catch:{ all -> 0x06a6 }
            com.alibaba.fastjson.parser.ParseContext r3 = r1.context     // Catch:{ all -> 0x06a6 }
            if (r3 == 0) goto L_0x0312
            if (r2 == 0) goto L_0x0312
            boolean r4 = r2 instanceof java.lang.Integer     // Catch:{ all -> 0x06a6 }
            if (r4 != 0) goto L_0x0312
            java.lang.Object r3 = r3.fieldName     // Catch:{ all -> 0x06a6 }
            boolean r3 = r3 instanceof java.lang.Integer     // Catch:{ all -> 0x06a6 }
            if (r3 != 0) goto L_0x0312
            r17.popContext()     // Catch:{ all -> 0x06a6 }
        L_0x0312:
            int r3 = r18.size()     // Catch:{ all -> 0x06a6 }
            if (r3 <= 0) goto L_0x0329
            com.alibaba.fastjson.parser.ParserConfig r2 = r1.config     // Catch:{ all -> 0x06a6 }
            java.lang.Object r0 = com.alibaba.fastjson.util.TypeUtils.cast((java.lang.Object) r0, r7, (com.alibaba.fastjson.parser.ParserConfig) r2)     // Catch:{ all -> 0x06a6 }
            r2 = 0
            r1.setResolveStatus(r2)     // Catch:{ all -> 0x06a6 }
            r1.parseObject((java.lang.Object) r0)     // Catch:{ all -> 0x06a6 }
            r1.setContext(r5)
            return r0
        L_0x0329:
            com.alibaba.fastjson.parser.ParserConfig r0 = r1.config     // Catch:{ all -> 0x06a6 }
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r0 = r0.getDeserializer((java.lang.reflect.Type) r7)     // Catch:{ all -> 0x06a6 }
            java.lang.Class r3 = r0.getClass()     // Catch:{ all -> 0x06a6 }
            java.lang.Class<com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer> r4 = com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.class
            boolean r4 = r4.isAssignableFrom(r3)     // Catch:{ all -> 0x06a6 }
            if (r4 == 0) goto L_0x0348
            java.lang.Class<com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer> r4 = com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.class
            if (r3 == r4) goto L_0x0348
            java.lang.Class<com.alibaba.fastjson.parser.deserializer.ThrowableDeserializer> r4 = com.alibaba.fastjson.parser.deserializer.ThrowableDeserializer.class
            if (r3 == r4) goto L_0x0348
            r3 = 0
            r1.setResolveStatus(r3)     // Catch:{ all -> 0x06a6 }
            goto L_0x0350
        L_0x0348:
            boolean r3 = r0 instanceof com.alibaba.fastjson.parser.deserializer.MapDeserializer     // Catch:{ all -> 0x06a6 }
            if (r3 == 0) goto L_0x0350
            r15 = 0
            r1.setResolveStatus(r15)     // Catch:{ all -> 0x06a6 }
        L_0x0350:
            java.lang.Object r0 = r0.deserialze(r1, r7, r2)     // Catch:{ all -> 0x06a6 }
            r1.setContext(r5)
            return r0
        L_0x0358:
            r12 = 0
            r15 = 0
            java.lang.String r12 = "$ref"
            if (r14 != r12) goto L_0x0462
            if (r5 == 0) goto L_0x0462
            if (r0 == 0) goto L_0x0368
            int r16 = r18.size()     // Catch:{ all -> 0x06a6 }
            if (r16 != 0) goto L_0x0462
        L_0x0368:
            com.alibaba.fastjson.parser.Feature r15 = com.alibaba.fastjson.parser.Feature.DisableSpecialKeyDetect     // Catch:{ all -> 0x06a6 }
            boolean r15 = r4.isEnabled((com.alibaba.fastjson.parser.Feature) r15)     // Catch:{ all -> 0x06a6 }
            if (r15 != 0) goto L_0x0462
            r15 = 4
            r4.nextToken(r15)     // Catch:{ all -> 0x06a6 }
            int r6 = r4.token()     // Catch:{ all -> 0x06a6 }
            if (r6 != r15) goto L_0x0443
            java.lang.String r6 = r4.stringVal()     // Catch:{ all -> 0x06a6 }
            r7 = 13
            r4.nextToken(r7)     // Catch:{ all -> 0x06a6 }
            int r7 = r4.token()     // Catch:{ all -> 0x06a6 }
            r8 = 16
            if (r7 != r8) goto L_0x0396
            r11.put(r14, r6)     // Catch:{ all -> 0x06a6 }
        L_0x038e:
            r6 = 0
            r7 = 13
            r8 = 4
            r10 = 16
            goto L_0x0080
        L_0x0396:
            java.lang.String r0 = "@"
            boolean r0 = r0.equals(r6)     // Catch:{ all -> 0x06a6 }
            if (r0 == 0) goto L_0x03b8
            com.alibaba.fastjson.parser.ParseContext r0 = r1.context     // Catch:{ all -> 0x06a6 }
            if (r0 == 0) goto L_0x040c
            java.lang.Object r2 = r0.object     // Catch:{ all -> 0x06a6 }
            boolean r3 = r2 instanceof java.lang.Object[]     // Catch:{ all -> 0x06a6 }
            if (r3 != 0) goto L_0x03b6
            boolean r3 = r2 instanceof java.util.Collection     // Catch:{ all -> 0x06a6 }
            if (r3 == 0) goto L_0x03ad
            goto L_0x03b6
        L_0x03ad:
            com.alibaba.fastjson.parser.ParseContext r2 = r0.parent     // Catch:{ all -> 0x06a6 }
            if (r2 == 0) goto L_0x040c
            com.alibaba.fastjson.parser.ParseContext r0 = r0.parent     // Catch:{ all -> 0x06a6 }
            java.lang.Object r6 = r0.object     // Catch:{ all -> 0x06a6 }
            goto L_0x0417
        L_0x03b6:
            r6 = r2
            goto L_0x0417
        L_0x03b8:
            java.lang.String r0 = ".."
            boolean r0 = r0.equals(r6)     // Catch:{ all -> 0x06a6 }
            if (r0 == 0) goto L_0x03d3
            java.lang.Object r0 = r5.object     // Catch:{ all -> 0x06a6 }
            if (r0 == 0) goto L_0x03c7
            java.lang.Object r6 = r5.object     // Catch:{ all -> 0x06a6 }
            goto L_0x0417
        L_0x03c7:
            com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask r0 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask     // Catch:{ all -> 0x06a6 }
            r0.<init>(r5, r6)     // Catch:{ all -> 0x06a6 }
            r1.addResolveTask(r0)     // Catch:{ all -> 0x06a6 }
            r1.setResolveStatus(r10)     // Catch:{ all -> 0x06a6 }
            goto L_0x040c
        L_0x03d3:
            java.lang.String r0 = "$"
            boolean r0 = r0.equals(r6)     // Catch:{ all -> 0x06a6 }
            if (r0 == 0) goto L_0x03f7
            r0 = r5
        L_0x03dc:
            com.alibaba.fastjson.parser.ParseContext r2 = r0.parent     // Catch:{ all -> 0x06a6 }
            if (r2 == 0) goto L_0x03e3
            com.alibaba.fastjson.parser.ParseContext r0 = r0.parent     // Catch:{ all -> 0x06a6 }
            goto L_0x03dc
        L_0x03e3:
            java.lang.Object r2 = r0.object     // Catch:{ all -> 0x06a6 }
            if (r2 == 0) goto L_0x03eb
            java.lang.Object r0 = r0.object     // Catch:{ all -> 0x06a6 }
            r6 = r0
            goto L_0x0417
        L_0x03eb:
            com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask r2 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask     // Catch:{ all -> 0x06a6 }
            r2.<init>(r0, r6)     // Catch:{ all -> 0x06a6 }
            r1.addResolveTask(r2)     // Catch:{ all -> 0x06a6 }
            r1.setResolveStatus(r10)     // Catch:{ all -> 0x06a6 }
            goto L_0x040c
        L_0x03f7:
            com.alibaba.fastjson.JSONPath r0 = com.alibaba.fastjson.JSONPath.compile(r6)     // Catch:{ all -> 0x06a6 }
            boolean r0 = r0.isRef()     // Catch:{ all -> 0x06a6 }
            if (r0 == 0) goto L_0x040e
            com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask r0 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask     // Catch:{ all -> 0x06a6 }
            r0.<init>(r5, r6)     // Catch:{ all -> 0x06a6 }
            r1.addResolveTask(r0)     // Catch:{ all -> 0x06a6 }
            r1.setResolveStatus(r10)     // Catch:{ all -> 0x06a6 }
        L_0x040c:
            r6 = 0
            goto L_0x0417
        L_0x040e:
            com.alibaba.fastjson.JSONObject r0 = new com.alibaba.fastjson.JSONObject     // Catch:{ all -> 0x06a6 }
            r0.<init>()     // Catch:{ all -> 0x06a6 }
            com.alibaba.fastjson.JSONObject r6 = r0.fluentPut(r12, r6)     // Catch:{ all -> 0x06a6 }
        L_0x0417:
            int r0 = r4.token()     // Catch:{ all -> 0x06a6 }
            r2 = 13
            if (r0 != r2) goto L_0x0428
            r0 = 16
            r4.nextToken(r0)     // Catch:{ all -> 0x06a6 }
            r1.setContext(r5)
            return r6
        L_0x0428:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x06a6 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x06a6 }
            r2.<init>()     // Catch:{ all -> 0x06a6 }
            java.lang.String r3 = "syntax error, "
            r2.append(r3)     // Catch:{ all -> 0x06a6 }
            java.lang.String r3 = r4.info()     // Catch:{ all -> 0x06a6 }
            r2.append(r3)     // Catch:{ all -> 0x06a6 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x06a6 }
            r0.<init>(r2)     // Catch:{ all -> 0x06a6 }
            throw r0     // Catch:{ all -> 0x06a6 }
        L_0x0443:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x06a6 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x06a6 }
            r2.<init>()     // Catch:{ all -> 0x06a6 }
            java.lang.String r3 = "illegal ref, "
            r2.append(r3)     // Catch:{ all -> 0x06a6 }
            int r3 = r4.token()     // Catch:{ all -> 0x06a6 }
            java.lang.String r3 = com.alibaba.fastjson.parser.JSONToken.name(r3)     // Catch:{ all -> 0x06a6 }
            r2.append(r3)     // Catch:{ all -> 0x06a6 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x06a6 }
            r0.<init>(r2)     // Catch:{ all -> 0x06a6 }
            throw r0     // Catch:{ all -> 0x06a6 }
        L_0x0462:
            r15 = 4
            if (r13 != 0) goto L_0x047e
            com.alibaba.fastjson.parser.ParseContext r12 = r1.context     // Catch:{ all -> 0x06a6 }
            if (r12 == 0) goto L_0x0476
            java.lang.Object r12 = r12.fieldName     // Catch:{ all -> 0x06a6 }
            if (r2 != r12) goto L_0x0476
            com.alibaba.fastjson.parser.ParseContext r12 = r1.context     // Catch:{ all -> 0x06a6 }
            java.lang.Object r12 = r12.object     // Catch:{ all -> 0x06a6 }
            if (r0 != r12) goto L_0x0476
            com.alibaba.fastjson.parser.ParseContext r5 = r1.context     // Catch:{ all -> 0x06a6 }
            goto L_0x047e
        L_0x0476:
            com.alibaba.fastjson.parser.ParseContext r12 = r17.setContext(r18, r19)     // Catch:{ all -> 0x06a6 }
            if (r5 != 0) goto L_0x047d
            r5 = r12
        L_0x047d:
            r13 = 1
        L_0x047e:
            java.lang.Class r12 = r18.getClass()     // Catch:{ all -> 0x06a6 }
            java.lang.Class<com.alibaba.fastjson.JSONObject> r10 = com.alibaba.fastjson.JSONObject.class
            if (r12 != r10) goto L_0x048a
            if (r14 != 0) goto L_0x048a
            java.lang.String r14 = "null"
        L_0x048a:
            r10 = 34
            if (r7 != r10) goto L_0x04b7
            r4.scanString()     // Catch:{ all -> 0x06a6 }
            java.lang.String r7 = r4.stringVal()     // Catch:{ all -> 0x06a6 }
            com.alibaba.fastjson.parser.Feature r8 = com.alibaba.fastjson.parser.Feature.AllowISO8601DateFormat     // Catch:{ all -> 0x06a6 }
            boolean r8 = r4.isEnabled((com.alibaba.fastjson.parser.Feature) r8)     // Catch:{ all -> 0x06a6 }
            if (r8 == 0) goto L_0x04b3
            com.alibaba.fastjson.parser.JSONScanner r8 = new com.alibaba.fastjson.parser.JSONScanner     // Catch:{ all -> 0x06a6 }
            r8.<init>(r7)     // Catch:{ all -> 0x06a6 }
            boolean r10 = r8.scanISO8601DateIfMatch()     // Catch:{ all -> 0x06a6 }
            if (r10 == 0) goto L_0x04b0
            java.util.Calendar r7 = r8.getCalendar()     // Catch:{ all -> 0x06a6 }
            java.util.Date r7 = r7.getTime()     // Catch:{ all -> 0x06a6 }
        L_0x04b0:
            r8.close()     // Catch:{ all -> 0x06a6 }
        L_0x04b3:
            r11.put(r14, r7)     // Catch:{ all -> 0x06a6 }
            goto L_0x04df
        L_0x04b7:
            r10 = 48
            if (r7 < r10) goto L_0x04bf
            r10 = 57
            if (r7 <= r10) goto L_0x04c3
        L_0x04bf:
            r10 = 45
            if (r7 != r10) goto L_0x0528
        L_0x04c3:
            r4.scanNumber()     // Catch:{ all -> 0x06a6 }
            int r7 = r4.token()     // Catch:{ all -> 0x06a6 }
            r8 = 2
            if (r7 != r8) goto L_0x04d2
            java.lang.Number r7 = r4.integerValue()     // Catch:{ all -> 0x06a6 }
            goto L_0x04dc
        L_0x04d2:
            com.alibaba.fastjson.parser.Feature r7 = com.alibaba.fastjson.parser.Feature.UseBigDecimal     // Catch:{ all -> 0x06a6 }
            boolean r7 = r4.isEnabled((com.alibaba.fastjson.parser.Feature) r7)     // Catch:{ all -> 0x06a6 }
            java.lang.Number r7 = r4.decimalValue(r7)     // Catch:{ all -> 0x06a6 }
        L_0x04dc:
            r11.put(r14, r7)     // Catch:{ all -> 0x06a6 }
        L_0x04df:
            r4.skipWhitespace()     // Catch:{ all -> 0x06a6 }
            char r8 = r4.getCurrent()     // Catch:{ all -> 0x06a6 }
            r10 = 44
            if (r8 != r10) goto L_0x04f3
            r4.next()     // Catch:{ all -> 0x06a6 }
        L_0x04ed:
            r8 = 13
            r10 = 16
            goto L_0x066b
        L_0x04f3:
            r2 = 125(0x7d, float:1.75E-43)
            if (r8 != r2) goto L_0x0507
            r4.next()     // Catch:{ all -> 0x06a6 }
            r4.resetStringPosition()     // Catch:{ all -> 0x06a6 }
            r4.nextToken()     // Catch:{ all -> 0x06a6 }
            r1.setContext(r7, r14)     // Catch:{ all -> 0x06a6 }
            r1.setContext(r5)
            return r0
        L_0x0507:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x06a6 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x06a6 }
            r2.<init>()     // Catch:{ all -> 0x06a6 }
            java.lang.String r3 = "syntax error, position at "
            r2.append(r3)     // Catch:{ all -> 0x06a6 }
            int r3 = r4.pos()     // Catch:{ all -> 0x06a6 }
            r2.append(r3)     // Catch:{ all -> 0x06a6 }
            r2.append(r6)     // Catch:{ all -> 0x06a6 }
            r2.append(r14)     // Catch:{ all -> 0x06a6 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x06a6 }
            r0.<init>(r2)     // Catch:{ all -> 0x06a6 }
            throw r0     // Catch:{ all -> 0x06a6 }
        L_0x0528:
            r10 = 91
            if (r7 != r10) goto L_0x0571
            r4.nextToken()     // Catch:{ all -> 0x06a6 }
            com.alibaba.fastjson.JSONArray r6 = new com.alibaba.fastjson.JSONArray     // Catch:{ all -> 0x06a6 }
            r6.<init>()     // Catch:{ all -> 0x06a6 }
            if (r2 == 0) goto L_0x053c
            java.lang.Class r7 = r19.getClass()     // Catch:{ all -> 0x06a6 }
            java.lang.Class<java.lang.Integer> r10 = java.lang.Integer.class
        L_0x053c:
            if (r2 != 0) goto L_0x0541
            r1.setContext(r5)     // Catch:{ all -> 0x06a6 }
        L_0x0541:
            r1.parseArray((java.util.Collection) r6, (java.lang.Object) r14)     // Catch:{ all -> 0x06a6 }
            com.alibaba.fastjson.parser.Feature r7 = com.alibaba.fastjson.parser.Feature.UseObjectArray     // Catch:{ all -> 0x06a6 }
            boolean r7 = r4.isEnabled((com.alibaba.fastjson.parser.Feature) r7)     // Catch:{ all -> 0x06a6 }
            if (r7 == 0) goto L_0x0550
            java.lang.Object[] r6 = r6.toArray()     // Catch:{ all -> 0x06a6 }
        L_0x0550:
            r11.put(r14, r6)     // Catch:{ all -> 0x06a6 }
            int r6 = r4.token()     // Catch:{ all -> 0x06a6 }
            r7 = 13
            if (r6 != r7) goto L_0x0562
            r4.nextToken()     // Catch:{ all -> 0x06a6 }
            r1.setContext(r5)
            return r0
        L_0x0562:
            int r6 = r4.token()     // Catch:{ all -> 0x06a6 }
            r7 = 16
            if (r6 != r7) goto L_0x056b
            goto L_0x04ed
        L_0x056b:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x06a6 }
            r0.<init>(r8)     // Catch:{ all -> 0x06a6 }
            throw r0     // Catch:{ all -> 0x06a6 }
        L_0x0571:
            r8 = 123(0x7b, float:1.72E-43)
            if (r7 != r8) goto L_0x064a
            r4.nextToken()     // Catch:{ all -> 0x06a6 }
            if (r2 == 0) goto L_0x0584
            java.lang.Class r6 = r19.getClass()     // Catch:{ all -> 0x06a6 }
            java.lang.Class<java.lang.Integer> r7 = java.lang.Integer.class
            if (r6 != r7) goto L_0x0584
            r6 = 1
            goto L_0x0585
        L_0x0584:
            r6 = 0
        L_0x0585:
            com.alibaba.fastjson.parser.Feature r7 = com.alibaba.fastjson.parser.Feature.CustomMapDeserializer     // Catch:{ all -> 0x06a6 }
            boolean r7 = r4.isEnabled((com.alibaba.fastjson.parser.Feature) r7)     // Catch:{ all -> 0x06a6 }
            if (r7 == 0) goto L_0x05b4
            com.alibaba.fastjson.parser.ParserConfig r7 = r1.config     // Catch:{ all -> 0x06a6 }
            java.lang.Class<java.util.Map> r8 = java.util.Map.class
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r7 = r7.getDeserializer((java.lang.reflect.Type) r8)     // Catch:{ all -> 0x06a6 }
            com.alibaba.fastjson.parser.deserializer.MapDeserializer r7 = (com.alibaba.fastjson.parser.deserializer.MapDeserializer) r7     // Catch:{ all -> 0x06a6 }
            int r8 = r4.getFeatures()     // Catch:{ all -> 0x06a6 }
            com.alibaba.fastjson.parser.Feature r10 = com.alibaba.fastjson.parser.Feature.OrderedField     // Catch:{ all -> 0x06a6 }
            int r10 = r10.mask     // Catch:{ all -> 0x06a6 }
            r8 = r8 & r10
            if (r8 == 0) goto L_0x05ad
            java.lang.Class<java.util.Map> r8 = java.util.Map.class
            int r10 = r4.getFeatures()     // Catch:{ all -> 0x06a6 }
            java.util.Map r7 = r7.createMap(r8, r10)     // Catch:{ all -> 0x06a6 }
            goto L_0x05bf
        L_0x05ad:
            java.lang.Class<java.util.Map> r8 = java.util.Map.class
            java.util.Map r7 = r7.createMap(r8)     // Catch:{ all -> 0x06a6 }
            goto L_0x05bf
        L_0x05b4:
            com.alibaba.fastjson.JSONObject r7 = new com.alibaba.fastjson.JSONObject     // Catch:{ all -> 0x06a6 }
            com.alibaba.fastjson.parser.Feature r8 = com.alibaba.fastjson.parser.Feature.OrderedField     // Catch:{ all -> 0x06a6 }
            boolean r8 = r4.isEnabled((com.alibaba.fastjson.parser.Feature) r8)     // Catch:{ all -> 0x06a6 }
            r7.<init>((boolean) r8)     // Catch:{ all -> 0x06a6 }
        L_0x05bf:
            if (r6 != 0) goto L_0x05c8
            com.alibaba.fastjson.parser.ParseContext r8 = r1.context     // Catch:{ all -> 0x06a6 }
            com.alibaba.fastjson.parser.ParseContext r8 = r1.setContext(r8, r7, r14)     // Catch:{ all -> 0x06a6 }
            goto L_0x05c9
        L_0x05c8:
            r8 = 0
        L_0x05c9:
            com.alibaba.fastjson.parser.deserializer.FieldTypeResolver r10 = r1.fieldTypeResolver     // Catch:{ all -> 0x06a6 }
            if (r10 == 0) goto L_0x05ea
            if (r14 == 0) goto L_0x05d4
            java.lang.String r10 = r14.toString()     // Catch:{ all -> 0x06a6 }
            goto L_0x05d5
        L_0x05d4:
            r10 = 0
        L_0x05d5:
            com.alibaba.fastjson.parser.deserializer.FieldTypeResolver r12 = r1.fieldTypeResolver     // Catch:{ all -> 0x06a6 }
            java.lang.reflect.Type r10 = r12.resolve(r0, r10)     // Catch:{ all -> 0x06a6 }
            if (r10 == 0) goto L_0x05ea
            com.alibaba.fastjson.parser.ParserConfig r12 = r1.config     // Catch:{ all -> 0x06a6 }
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r12 = r12.getDeserializer((java.lang.reflect.Type) r10)     // Catch:{ all -> 0x06a6 }
            java.lang.Object r10 = r12.deserialze(r1, r10, r14)     // Catch:{ all -> 0x06a6 }
            r12 = r10
            r10 = 1
            goto L_0x05ec
        L_0x05ea:
            r10 = 0
            r12 = 0
        L_0x05ec:
            if (r10 != 0) goto L_0x05f2
            java.lang.Object r12 = r1.parseObject((java.util.Map) r7, (java.lang.Object) r14)     // Catch:{ all -> 0x06a6 }
        L_0x05f2:
            if (r8 == 0) goto L_0x05f8
            if (r7 == r12) goto L_0x05f8
            r8.object = r0     // Catch:{ all -> 0x06a6 }
        L_0x05f8:
            if (r14 == 0) goto L_0x0601
            java.lang.String r7 = r14.toString()     // Catch:{ all -> 0x06a6 }
            r1.checkMapResolve(r0, r7)     // Catch:{ all -> 0x06a6 }
        L_0x0601:
            r11.put(r14, r12)     // Catch:{ all -> 0x06a6 }
            if (r6 == 0) goto L_0x0609
            r1.setContext(r12, r14)     // Catch:{ all -> 0x06a6 }
        L_0x0609:
            int r7 = r4.token()     // Catch:{ all -> 0x06a6 }
            r8 = 13
            if (r7 != r8) goto L_0x061b
            r4.nextToken()     // Catch:{ all -> 0x06a6 }
            r1.setContext(r5)     // Catch:{ all -> 0x06a6 }
            r1.setContext(r5)
            return r0
        L_0x061b:
            int r7 = r4.token()     // Catch:{ all -> 0x06a6 }
            r8 = 16
            if (r7 != r8) goto L_0x062f
            if (r6 == 0) goto L_0x062a
            r17.popContext()     // Catch:{ all -> 0x06a6 }
            goto L_0x04ed
        L_0x062a:
            r1.setContext(r5)     // Catch:{ all -> 0x06a6 }
            goto L_0x04ed
        L_0x062f:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x06a6 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x06a6 }
            r2.<init>()     // Catch:{ all -> 0x06a6 }
            java.lang.String r3 = "syntax error, "
            r2.append(r3)     // Catch:{ all -> 0x06a6 }
            java.lang.String r3 = r4.tokenName()     // Catch:{ all -> 0x06a6 }
            r2.append(r3)     // Catch:{ all -> 0x06a6 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x06a6 }
            r0.<init>(r2)     // Catch:{ all -> 0x06a6 }
            throw r0     // Catch:{ all -> 0x06a6 }
        L_0x064a:
            r4.nextToken()     // Catch:{ all -> 0x06a6 }
            java.lang.Object r7 = r17.parse()     // Catch:{ all -> 0x06a6 }
            r11.put(r14, r7)     // Catch:{ all -> 0x06a6 }
            int r7 = r4.token()     // Catch:{ all -> 0x06a6 }
            r8 = 13
            if (r7 != r8) goto L_0x0663
            r4.nextToken()     // Catch:{ all -> 0x06a6 }
            r1.setContext(r5)
            return r0
        L_0x0663:
            int r7 = r4.token()     // Catch:{ all -> 0x06a6 }
            r10 = 16
            if (r7 != r10) goto L_0x0671
        L_0x066b:
            r6 = 0
            r7 = 13
            r8 = 4
            goto L_0x0080
        L_0x0671:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x06a6 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x06a6 }
            r2.<init>()     // Catch:{ all -> 0x06a6 }
            java.lang.String r3 = "syntax error, position at "
            r2.append(r3)     // Catch:{ all -> 0x06a6 }
            int r3 = r4.pos()     // Catch:{ all -> 0x06a6 }
            r2.append(r3)     // Catch:{ all -> 0x06a6 }
            r2.append(r6)     // Catch:{ all -> 0x06a6 }
            r2.append(r14)     // Catch:{ all -> 0x06a6 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x06a6 }
            r0.<init>(r2)     // Catch:{ all -> 0x06a6 }
            throw r0     // Catch:{ all -> 0x06a6 }
        L_0x0692:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x06a6 }
            java.lang.String r2 = "object key level > 512"
            r0.<init>(r2)     // Catch:{ all -> 0x06a6 }
            throw r0     // Catch:{ all -> 0x06a6 }
        L_0x069a:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x06a6 }
            r0.<init>(r8)     // Catch:{ all -> 0x06a6 }
            throw r0     // Catch:{ all -> 0x06a6 }
        L_0x06a0:
            com.alibaba.fastjson.JSONException r0 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x06a6 }
            r0.<init>(r8)     // Catch:{ all -> 0x06a6 }
            throw r0     // Catch:{ all -> 0x06a6 }
        L_0x06a6:
            r0 = move-exception
            r1.setContext(r5)
            goto L_0x06ac
        L_0x06ab:
            throw r0
        L_0x06ac:
            goto L_0x06ab
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.DefaultJSONParser.parseObject(java.util.Map, java.lang.Object):java.lang.Object");
    }

    public ParserConfig getConfig() {
        return this.config;
    }

    public void setConfig(ParserConfig parserConfig) {
        this.config = parserConfig;
    }

    public <T> T parseObject(Class<T> cls) {
        return parseObject((Type) cls, (Object) null);
    }

    public <T> T parseObject(Type type) {
        return parseObject(type, (Object) null);
    }

    public <T> T parseObject(Type type, Object obj) {
        int i = this.lexer.token();
        if (i == 8) {
            this.lexer.nextToken();
            return TypeUtils.optionalEmpty(type);
        }
        if (i == 4) {
            if (type == byte[].class) {
                Object bytesValue = this.lexer.bytesValue();
                this.lexer.nextToken();
                return bytesValue;
            } else if (type == char[].class) {
                String stringVal = this.lexer.stringVal();
                this.lexer.nextToken();
                return stringVal.toCharArray();
            }
        }
        ObjectDeserializer deserializer = this.config.getDeserializer(type);
        try {
            if (deserializer.getClass() != JavaBeanDeserializer.class) {
                return deserializer.deserialze(this, type, obj);
            }
            if (this.lexer.token() != 12) {
                if (this.lexer.token() != 14) {
                    throw new JSONException("syntax error,expect start with { or [,but actually start with " + this.lexer.tokenName());
                }
            }
            return ((JavaBeanDeserializer) deserializer).deserialze(this, type, obj, 0);
        } catch (JSONException e) {
            throw e;
        } catch (Throwable th) {
            throw new JSONException(th.getMessage(), th);
        }
    }

    public <T> List<T> parseArray(Class<T> cls) {
        ArrayList arrayList = new ArrayList();
        parseArray((Class<?>) cls, (Collection) arrayList);
        return arrayList;
    }

    public void parseArray(Class<?> cls, Collection collection) {
        parseArray((Type) cls, collection);
    }

    public void parseArray(Type type, Collection collection) {
        parseArray(type, collection, (Object) null);
    }

    /* JADX INFO: finally extract failed */
    public void parseArray(Type type, Collection collection, Object obj) {
        ObjectDeserializer objectDeserializer;
        int i = this.lexer.token();
        if (i == 21 || i == 22) {
            this.lexer.nextToken();
            i = this.lexer.token();
        }
        if (i == 14) {
            if (Integer.TYPE == type) {
                objectDeserializer = IntegerCodec.instance;
                this.lexer.nextToken(2);
            } else if (String.class == type) {
                objectDeserializer = StringCodec.instance;
                this.lexer.nextToken(4);
            } else {
                objectDeserializer = this.config.getDeserializer(type);
                this.lexer.nextToken(objectDeserializer.getFastMatchToken());
            }
            ParseContext parseContext = this.context;
            setContext(collection, obj);
            int i2 = 0;
            while (true) {
                try {
                    if (this.lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                        while (this.lexer.token() == 16) {
                            this.lexer.nextToken();
                        }
                    }
                    if (this.lexer.token() == 15) {
                        setContext(parseContext);
                        this.lexer.nextToken(16);
                        return;
                    }
                    Object obj2 = null;
                    if (Integer.TYPE == type) {
                        collection.add(IntegerCodec.instance.deserialze(this, (Type) null, (Object) null));
                    } else if (String.class == type) {
                        if (this.lexer.token() == 4) {
                            obj2 = this.lexer.stringVal();
                            this.lexer.nextToken(16);
                        } else {
                            Object parse = parse();
                            if (parse != null) {
                                obj2 = parse.toString();
                            }
                        }
                        collection.add(obj2);
                    } else {
                        if (this.lexer.token() == 8) {
                            this.lexer.nextToken();
                        } else {
                            obj2 = objectDeserializer.deserialze(this, type, Integer.valueOf(i2));
                        }
                        collection.add(obj2);
                        checkListResolve(collection);
                    }
                    if (this.lexer.token() == 16) {
                        this.lexer.nextToken(objectDeserializer.getFastMatchToken());
                    }
                    i2++;
                } catch (Throwable th) {
                    setContext(parseContext);
                    throw th;
                }
            }
        } else {
            throw new JSONException("field " + obj + " expect '[', but " + JSONToken.name(i) + ", " + this.lexer.info());
        }
    }

    public Object[] parseArray(Type[] typeArr) {
        Object obj;
        Class<?> cls;
        boolean z;
        Class<char[]> cls2;
        Type[] typeArr2 = typeArr;
        int i = 8;
        if (this.lexer.token() == 8) {
            this.lexer.nextToken(16);
            return null;
        }
        int i2 = 14;
        if (this.lexer.token() == 14) {
            Object[] objArr = new Object[typeArr2.length];
            if (typeArr2.length == 0) {
                this.lexer.nextToken(15);
                if (this.lexer.token() == 15) {
                    this.lexer.nextToken(16);
                    return new Object[0];
                }
                throw new JSONException("syntax error");
            }
            this.lexer.nextToken(2);
            int i3 = 0;
            while (i3 < typeArr2.length) {
                if (this.lexer.token() == i) {
                    this.lexer.nextToken(16);
                    obj = null;
                } else {
                    Type type = typeArr2[i3];
                    if (type == Integer.TYPE || type == Integer.class) {
                        if (this.lexer.token() == 2) {
                            obj = Integer.valueOf(this.lexer.intValue());
                            this.lexer.nextToken(16);
                        } else {
                            obj = TypeUtils.cast(parse(), type, this.config);
                        }
                    } else if (type != String.class) {
                        if (i3 != typeArr2.length - 1 || !(type instanceof Class) || (((cls2 = (Class) type) == byte[].class || cls2 == char[].class) && this.lexer.token() == 4)) {
                            cls = null;
                            z = false;
                        } else {
                            z = cls2.isArray();
                            cls = cls2.getComponentType();
                        }
                        if (!z || this.lexer.token() == i2) {
                            obj = this.config.getDeserializer(type).deserialze(this, type, Integer.valueOf(i3));
                        } else {
                            ArrayList arrayList = new ArrayList();
                            ObjectDeserializer deserializer = this.config.getDeserializer((Type) cls);
                            int fastMatchToken = deserializer.getFastMatchToken();
                            if (this.lexer.token() != 15) {
                                while (true) {
                                    arrayList.add(deserializer.deserialze(this, type, (Object) null));
                                    if (this.lexer.token() != 16) {
                                        break;
                                    }
                                    this.lexer.nextToken(fastMatchToken);
                                }
                                if (this.lexer.token() != 15) {
                                    throw new JSONException("syntax error :" + JSONToken.name(this.lexer.token()));
                                }
                            }
                            obj = TypeUtils.cast((Object) arrayList, type, this.config);
                        }
                    } else if (this.lexer.token() == 4) {
                        obj = this.lexer.stringVal();
                        this.lexer.nextToken(16);
                    } else {
                        obj = TypeUtils.cast(parse(), type, this.config);
                    }
                }
                objArr[i3] = obj;
                if (this.lexer.token() == 15) {
                    break;
                } else if (this.lexer.token() == 16) {
                    if (i3 == typeArr2.length - 1) {
                        this.lexer.nextToken(15);
                    } else {
                        this.lexer.nextToken(2);
                    }
                    i3++;
                    i = 8;
                    i2 = 14;
                } else {
                    throw new JSONException("syntax error :" + JSONToken.name(this.lexer.token()));
                }
            }
            if (this.lexer.token() == 15) {
                this.lexer.nextToken(16);
                return objArr;
            }
            throw new JSONException("syntax error");
        }
        throw new JSONException("syntax error : " + this.lexer.tokenName());
    }

    public void parseObject(Object obj) {
        Object obj2;
        Class<?> cls = obj.getClass();
        ObjectDeserializer deserializer = this.config.getDeserializer((Type) cls);
        JavaBeanDeserializer javaBeanDeserializer = deserializer instanceof JavaBeanDeserializer ? (JavaBeanDeserializer) deserializer : null;
        if (this.lexer.token() == 12 || this.lexer.token() == 16) {
            while (true) {
                String scanSymbol = this.lexer.scanSymbol(this.symbolTable);
                if (scanSymbol == null) {
                    if (this.lexer.token() == 13) {
                        this.lexer.nextToken(16);
                        return;
                    } else if (this.lexer.token() == 16 && this.lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                    }
                }
                FieldDeserializer fieldDeserializer = javaBeanDeserializer != null ? javaBeanDeserializer.getFieldDeserializer(scanSymbol) : null;
                if (fieldDeserializer != null) {
                    Class<?> cls2 = fieldDeserializer.fieldInfo.fieldClass;
                    Type type = fieldDeserializer.fieldInfo.fieldType;
                    if (cls2 == Integer.TYPE) {
                        this.lexer.nextTokenWithColon(2);
                        obj2 = IntegerCodec.instance.deserialze(this, type, (Object) null);
                    } else if (cls2 == String.class) {
                        this.lexer.nextTokenWithColon(4);
                        obj2 = StringCodec.deserialze(this);
                    } else if (cls2 == Long.TYPE) {
                        this.lexer.nextTokenWithColon(2);
                        obj2 = LongCodec.instance.deserialze(this, type, (Object) null);
                    } else {
                        ObjectDeserializer deserializer2 = this.config.getDeserializer(cls2, type);
                        this.lexer.nextTokenWithColon(deserializer2.getFastMatchToken());
                        obj2 = deserializer2.deserialze(this, type, (Object) null);
                    }
                    fieldDeserializer.setValue(obj, obj2);
                    if (this.lexer.token() != 16 && this.lexer.token() == 13) {
                        this.lexer.nextToken(16);
                        return;
                    }
                } else if (this.lexer.isEnabled(Feature.IgnoreNotMatch)) {
                    this.lexer.nextTokenWithColon();
                    parse();
                    if (this.lexer.token() == 13) {
                        this.lexer.nextToken();
                        return;
                    }
                } else {
                    throw new JSONException("setter not found, class " + cls.getName() + ", property " + scanSymbol);
                }
            }
        } else {
            throw new JSONException("syntax error, expect {, actual " + this.lexer.tokenName());
        }
    }

    public Object parseArrayWithType(Type type) {
        if (this.lexer.token() == 8) {
            this.lexer.nextToken();
            return null;
        }
        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        if (actualTypeArguments.length == 1) {
            Type type2 = actualTypeArguments[0];
            if (type2 instanceof Class) {
                ArrayList arrayList = new ArrayList();
                parseArray((Class<?>) (Class) type2, (Collection) arrayList);
                return arrayList;
            } else if (type2 instanceof WildcardType) {
                WildcardType wildcardType = (WildcardType) type2;
                Type type3 = wildcardType.getUpperBounds()[0];
                if (!Object.class.equals(type3)) {
                    ArrayList arrayList2 = new ArrayList();
                    parseArray((Class<?>) (Class) type3, (Collection) arrayList2);
                    return arrayList2;
                } else if (wildcardType.getLowerBounds().length == 0) {
                    return parse();
                } else {
                    throw new JSONException("not support type : " + type);
                }
            } else {
                if (type2 instanceof TypeVariable) {
                    TypeVariable typeVariable = (TypeVariable) type2;
                    Type[] bounds = typeVariable.getBounds();
                    if (bounds.length == 1) {
                        Type type4 = bounds[0];
                        if (type4 instanceof Class) {
                            ArrayList arrayList3 = new ArrayList();
                            parseArray((Class<?>) (Class) type4, (Collection) arrayList3);
                            return arrayList3;
                        }
                    } else {
                        throw new JSONException("not support : " + typeVariable);
                    }
                }
                if (type2 instanceof ParameterizedType) {
                    ArrayList arrayList4 = new ArrayList();
                    parseArray((Type) (ParameterizedType) type2, (Collection) arrayList4);
                    return arrayList4;
                }
                throw new JSONException("TODO : " + type);
            }
        } else {
            throw new JSONException("not support type " + type);
        }
    }

    public void acceptType(String str) {
        JSONLexer jSONLexer = this.lexer;
        jSONLexer.nextTokenWithColon();
        if (jSONLexer.token() != 4) {
            throw new JSONException("type not match error");
        } else if (str.equals(jSONLexer.stringVal())) {
            jSONLexer.nextToken();
            if (jSONLexer.token() == 16) {
                jSONLexer.nextToken();
            }
        } else {
            throw new JSONException("type not match error");
        }
    }

    public int getResolveStatus() {
        return this.resolveStatus;
    }

    public void setResolveStatus(int i) {
        this.resolveStatus = i;
    }

    public Object getObject(String str) {
        for (int i = 0; i < this.contextArrayIndex; i++) {
            if (str.equals(this.contextArray[i].toString())) {
                return this.contextArray[i].object;
            }
        }
        return null;
    }

    public void checkListResolve(Collection collection) {
        if (this.resolveStatus != 1) {
            return;
        }
        if (collection instanceof List) {
            ResolveTask lastResolveTask = getLastResolveTask();
            lastResolveTask.fieldDeserializer = new ResolveFieldDeserializer(this, (List) collection, collection.size() - 1);
            lastResolveTask.ownerContext = this.context;
            setResolveStatus(0);
            return;
        }
        ResolveTask lastResolveTask2 = getLastResolveTask();
        lastResolveTask2.fieldDeserializer = new ResolveFieldDeserializer(collection);
        lastResolveTask2.ownerContext = this.context;
        setResolveStatus(0);
    }

    public void checkMapResolve(Map map, Object obj) {
        if (this.resolveStatus == 1) {
            ResolveFieldDeserializer resolveFieldDeserializer = new ResolveFieldDeserializer(map, obj);
            ResolveTask lastResolveTask = getLastResolveTask();
            lastResolveTask.fieldDeserializer = resolveFieldDeserializer;
            lastResolveTask.ownerContext = this.context;
            setResolveStatus(0);
        }
    }

    public Object parseObject(Map map) {
        return parseObject(map, (Object) null);
    }

    public JSONObject parseObject() {
        Object parseObject = parseObject((Map) new JSONObject(this.lexer.isEnabled(Feature.OrderedField)));
        if (parseObject instanceof JSONObject) {
            return (JSONObject) parseObject;
        }
        if (parseObject == null) {
            return null;
        }
        return new JSONObject((Map<String, Object>) (Map) parseObject);
    }

    public final void parseArray(Collection collection) {
        parseArray(collection, (Object) null);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v0, resolved type: java.lang.Number} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v1, resolved type: java.lang.Number} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v3, resolved type: java.lang.Number} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v4, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v5, resolved type: java.util.Date} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v6, resolved type: java.util.Date} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v13, resolved type: java.lang.Number} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v14, resolved type: java.lang.Number} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v15, resolved type: java.util.Date} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v16, resolved type: java.util.Date} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v17, resolved type: java.lang.Boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v18, resolved type: java.lang.Boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v20, resolved type: com.alibaba.fastjson.JSONArray} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v21, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void parseArray(java.util.Collection r10, java.lang.Object r11) {
        /*
            r9 = this;
            com.alibaba.fastjson.parser.JSONLexer r0 = r9.lexer
            int r1 = r0.token()
            r2 = 21
            if (r1 == r2) goto L_0x0012
            int r1 = r0.token()
            r2 = 22
            if (r1 != r2) goto L_0x0015
        L_0x0012:
            r0.nextToken()
        L_0x0015:
            int r1 = r0.token()
            r2 = 14
            if (r1 != r2) goto L_0x0137
            r1 = 4
            r0.nextToken(r1)
            com.alibaba.fastjson.parser.ParseContext r3 = r9.context
            if (r3 == 0) goto L_0x0034
            int r3 = r3.level
            r4 = 512(0x200, float:7.175E-43)
            if (r3 > r4) goto L_0x002c
            goto L_0x0034
        L_0x002c:
            com.alibaba.fastjson.JSONException r10 = new com.alibaba.fastjson.JSONException
            java.lang.String r11 = "array level > 512"
            r10.<init>(r11)
            throw r10
        L_0x0034:
            com.alibaba.fastjson.parser.ParseContext r3 = r9.context
            r9.setContext(r10, r11)
            r11 = 0
            r4 = 0
        L_0x003b:
            com.alibaba.fastjson.parser.Feature r5 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas     // Catch:{ ClassCastException -> 0x012a }
            boolean r5 = r0.isEnabled((com.alibaba.fastjson.parser.Feature) r5)     // Catch:{ ClassCastException -> 0x012a }
            r6 = 16
            if (r5 == 0) goto L_0x004f
        L_0x0045:
            int r5 = r0.token()     // Catch:{ ClassCastException -> 0x012a }
            if (r5 != r6) goto L_0x004f
            r0.nextToken()     // Catch:{ ClassCastException -> 0x012a }
            goto L_0x0045
        L_0x004f:
            int r5 = r0.token()     // Catch:{ ClassCastException -> 0x012a }
            r7 = 2
            r8 = 0
            if (r5 == r7) goto L_0x010e
            r7 = 3
            if (r5 == r7) goto L_0x00f7
            if (r5 == r1) goto L_0x00d0
            r7 = 6
            if (r5 == r7) goto L_0x00ca
            r7 = 7
            if (r5 == r7) goto L_0x00c4
            r7 = 8
            if (r5 == r7) goto L_0x00c0
            r7 = 12
            if (r5 == r7) goto L_0x00ac
            r7 = 20
            if (r5 == r7) goto L_0x00a4
            r7 = 23
            if (r5 == r7) goto L_0x009f
            if (r5 == r2) goto L_0x0085
            r7 = 15
            if (r5 == r7) goto L_0x007e
            java.lang.Object r8 = r9.parse()     // Catch:{ ClassCastException -> 0x012a }
            goto L_0x0115
        L_0x007e:
            r0.nextToken(r6)     // Catch:{ ClassCastException -> 0x012a }
            r9.setContext(r3)
            return
        L_0x0085:
            com.alibaba.fastjson.JSONArray r8 = new com.alibaba.fastjson.JSONArray     // Catch:{ ClassCastException -> 0x012a }
            r8.<init>()     // Catch:{ ClassCastException -> 0x012a }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r4)     // Catch:{ ClassCastException -> 0x012a }
            r9.parseArray((java.util.Collection) r8, (java.lang.Object) r5)     // Catch:{ ClassCastException -> 0x012a }
            com.alibaba.fastjson.parser.Feature r5 = com.alibaba.fastjson.parser.Feature.UseObjectArray     // Catch:{ ClassCastException -> 0x012a }
            boolean r5 = r0.isEnabled((com.alibaba.fastjson.parser.Feature) r5)     // Catch:{ ClassCastException -> 0x012a }
            if (r5 == 0) goto L_0x0115
            java.lang.Object[] r8 = r8.toArray()     // Catch:{ ClassCastException -> 0x012a }
            goto L_0x0115
        L_0x009f:
            r0.nextToken(r1)     // Catch:{ ClassCastException -> 0x012a }
            goto L_0x0115
        L_0x00a4:
            com.alibaba.fastjson.JSONException r10 = new com.alibaba.fastjson.JSONException     // Catch:{ ClassCastException -> 0x012a }
            java.lang.String r11 = "unclosed jsonArray"
            r10.<init>(r11)     // Catch:{ ClassCastException -> 0x012a }
            throw r10     // Catch:{ ClassCastException -> 0x012a }
        L_0x00ac:
            com.alibaba.fastjson.JSONObject r5 = new com.alibaba.fastjson.JSONObject     // Catch:{ ClassCastException -> 0x012a }
            com.alibaba.fastjson.parser.Feature r7 = com.alibaba.fastjson.parser.Feature.OrderedField     // Catch:{ ClassCastException -> 0x012a }
            boolean r7 = r0.isEnabled((com.alibaba.fastjson.parser.Feature) r7)     // Catch:{ ClassCastException -> 0x012a }
            r5.<init>((boolean) r7)     // Catch:{ ClassCastException -> 0x012a }
            java.lang.Integer r7 = java.lang.Integer.valueOf(r4)     // Catch:{ ClassCastException -> 0x012a }
            java.lang.Object r8 = r9.parseObject((java.util.Map) r5, (java.lang.Object) r7)     // Catch:{ ClassCastException -> 0x012a }
            goto L_0x0115
        L_0x00c0:
            r0.nextToken(r1)     // Catch:{ ClassCastException -> 0x012a }
            goto L_0x0115
        L_0x00c4:
            java.lang.Boolean r8 = java.lang.Boolean.FALSE     // Catch:{ ClassCastException -> 0x012a }
            r0.nextToken(r6)     // Catch:{ ClassCastException -> 0x012a }
            goto L_0x0115
        L_0x00ca:
            java.lang.Boolean r8 = java.lang.Boolean.TRUE     // Catch:{ ClassCastException -> 0x012a }
            r0.nextToken(r6)     // Catch:{ ClassCastException -> 0x012a }
            goto L_0x0115
        L_0x00d0:
            java.lang.String r8 = r0.stringVal()     // Catch:{ ClassCastException -> 0x012a }
            r0.nextToken(r6)     // Catch:{ ClassCastException -> 0x012a }
            com.alibaba.fastjson.parser.Feature r5 = com.alibaba.fastjson.parser.Feature.AllowISO8601DateFormat     // Catch:{ ClassCastException -> 0x012a }
            boolean r5 = r0.isEnabled((com.alibaba.fastjson.parser.Feature) r5)     // Catch:{ ClassCastException -> 0x012a }
            if (r5 == 0) goto L_0x0115
            com.alibaba.fastjson.parser.JSONScanner r5 = new com.alibaba.fastjson.parser.JSONScanner     // Catch:{ ClassCastException -> 0x012a }
            r5.<init>(r8)     // Catch:{ ClassCastException -> 0x012a }
            boolean r7 = r5.scanISO8601DateIfMatch()     // Catch:{ ClassCastException -> 0x012a }
            if (r7 == 0) goto L_0x00f3
            java.util.Calendar r7 = r5.getCalendar()     // Catch:{ ClassCastException -> 0x012a }
            java.util.Date r7 = r7.getTime()     // Catch:{ ClassCastException -> 0x012a }
            r8 = r7
        L_0x00f3:
            r5.close()     // Catch:{ ClassCastException -> 0x012a }
            goto L_0x0115
        L_0x00f7:
            com.alibaba.fastjson.parser.Feature r5 = com.alibaba.fastjson.parser.Feature.UseBigDecimal     // Catch:{ ClassCastException -> 0x012a }
            boolean r5 = r0.isEnabled((com.alibaba.fastjson.parser.Feature) r5)     // Catch:{ ClassCastException -> 0x012a }
            if (r5 == 0) goto L_0x0105
            r5 = 1
            java.lang.Number r5 = r0.decimalValue(r5)     // Catch:{ ClassCastException -> 0x012a }
            goto L_0x0109
        L_0x0105:
            java.lang.Number r5 = r0.decimalValue(r11)     // Catch:{ ClassCastException -> 0x012a }
        L_0x0109:
            r8 = r5
            r0.nextToken(r6)     // Catch:{ ClassCastException -> 0x012a }
            goto L_0x0115
        L_0x010e:
            java.lang.Number r8 = r0.integerValue()     // Catch:{ ClassCastException -> 0x012a }
            r0.nextToken(r6)     // Catch:{ ClassCastException -> 0x012a }
        L_0x0115:
            r10.add(r8)     // Catch:{ ClassCastException -> 0x012a }
            r9.checkListResolve(r10)     // Catch:{ ClassCastException -> 0x012a }
            int r5 = r0.token()     // Catch:{ ClassCastException -> 0x012a }
            if (r5 != r6) goto L_0x0124
            r0.nextToken(r1)     // Catch:{ ClassCastException -> 0x012a }
        L_0x0124:
            int r4 = r4 + 1
            goto L_0x003b
        L_0x0128:
            r10 = move-exception
            goto L_0x0133
        L_0x012a:
            r10 = move-exception
            com.alibaba.fastjson.JSONException r11 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0128 }
            java.lang.String r0 = "unkown error"
            r11.<init>(r0, r10)     // Catch:{ all -> 0x0128 }
            throw r11     // Catch:{ all -> 0x0128 }
        L_0x0133:
            r9.setContext(r3)
            throw r10
        L_0x0137:
            com.alibaba.fastjson.JSONException r10 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "syntax error, expect [, actual "
            r1.append(r2)
            int r2 = r0.token()
            java.lang.String r2 = com.alibaba.fastjson.parser.JSONToken.name(r2)
            r1.append(r2)
            java.lang.String r2 = ", pos "
            r1.append(r2)
            int r0 = r0.pos()
            r1.append(r0)
            java.lang.String r0 = ", fieldName "
            r1.append(r0)
            r1.append(r11)
            java.lang.String r11 = r1.toString()
            r10.<init>(r11)
            goto L_0x016b
        L_0x016a:
            throw r10
        L_0x016b:
            goto L_0x016a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.DefaultJSONParser.parseArray(java.util.Collection, java.lang.Object):void");
    }

    public ParseContext getContext() {
        return this.context;
    }

    public ParseContext getOwnerContext() {
        return this.context.parent;
    }

    public List<ResolveTask> getResolveTaskList() {
        if (this.resolveTaskList == null) {
            this.resolveTaskList = new ArrayList(2);
        }
        return this.resolveTaskList;
    }

    public void addResolveTask(ResolveTask resolveTask) {
        if (this.resolveTaskList == null) {
            this.resolveTaskList = new ArrayList(2);
        }
        this.resolveTaskList.add(resolveTask);
    }

    public ResolveTask getLastResolveTask() {
        List<ResolveTask> list = this.resolveTaskList;
        return list.get(list.size() - 1);
    }

    public List<ExtraProcessor> getExtraProcessors() {
        if (this.extraProcessors == null) {
            this.extraProcessors = new ArrayList(2);
        }
        return this.extraProcessors;
    }

    public List<ExtraTypeProvider> getExtraTypeProviders() {
        if (this.extraTypeProviders == null) {
            this.extraTypeProviders = new ArrayList(2);
        }
        return this.extraTypeProviders;
    }

    public FieldTypeResolver getFieldTypeResolver() {
        return this.fieldTypeResolver;
    }

    public void setFieldTypeResolver(FieldTypeResolver fieldTypeResolver2) {
        this.fieldTypeResolver = fieldTypeResolver2;
    }

    public void setContext(ParseContext parseContext) {
        if (!this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            this.context = parseContext;
        }
    }

    public void popContext() {
        if (!this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            this.context = this.context.parent;
            int i = this.contextArrayIndex;
            if (i > 0) {
                int i2 = i - 1;
                this.contextArrayIndex = i2;
                this.contextArray[i2] = null;
            }
        }
    }

    public ParseContext setContext(Object obj, Object obj2) {
        if (this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return null;
        }
        return setContext(this.context, obj, obj2);
    }

    public ParseContext setContext(ParseContext parseContext, Object obj, Object obj2) {
        if (this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return null;
        }
        ParseContext parseContext2 = new ParseContext(parseContext, obj, obj2);
        this.context = parseContext2;
        addContext(parseContext2);
        return this.context;
    }

    private void addContext(ParseContext parseContext) {
        int i = this.contextArrayIndex;
        this.contextArrayIndex = i + 1;
        ParseContext[] parseContextArr = this.contextArray;
        if (parseContextArr == null) {
            this.contextArray = new ParseContext[8];
        } else if (i >= parseContextArr.length) {
            ParseContext[] parseContextArr2 = new ParseContext[((parseContextArr.length * 3) / 2)];
            System.arraycopy(parseContextArr, 0, parseContextArr2, 0, parseContextArr.length);
            this.contextArray = parseContextArr2;
        }
        this.contextArray[i] = parseContext;
    }

    public Object parse() {
        return parse((Object) null);
    }

    public Object parseKey() {
        if (this.lexer.token() != 18) {
            return parse((Object) null);
        }
        String stringVal = this.lexer.stringVal();
        this.lexer.nextToken(16);
        return stringVal;
    }

    public Object parse(Object obj) {
        Map map;
        JSONLexer jSONLexer = this.lexer;
        int i = jSONLexer.token();
        if (i == 2) {
            Number integerValue = jSONLexer.integerValue();
            jSONLexer.nextToken();
            return integerValue;
        } else if (i == 3) {
            Number decimalValue = jSONLexer.decimalValue(jSONLexer.isEnabled(Feature.UseBigDecimal));
            jSONLexer.nextToken();
            return decimalValue;
        } else if (i == 4) {
            String stringVal = jSONLexer.stringVal();
            jSONLexer.nextToken(16);
            if (jSONLexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                JSONScanner jSONScanner = new JSONScanner(stringVal);
                try {
                    if (jSONScanner.scanISO8601DateIfMatch()) {
                        return jSONScanner.getCalendar().getTime();
                    }
                    jSONScanner.close();
                } finally {
                    jSONScanner.close();
                }
            }
            return stringVal;
        } else if (i == 12) {
            if (isEnabled(Feature.UseNativeJavaObject)) {
                map = jSONLexer.isEnabled(Feature.OrderedField) ? new HashMap() : new LinkedHashMap();
            } else {
                map = new JSONObject(jSONLexer.isEnabled(Feature.OrderedField));
            }
            return parseObject(map, obj);
        } else if (i == 14) {
            Collection arrayList = isEnabled(Feature.UseNativeJavaObject) ? new ArrayList() : new JSONArray();
            parseArray(arrayList, obj);
            return jSONLexer.isEnabled(Feature.UseObjectArray) ? arrayList.toArray() : arrayList;
        } else if (i != 18) {
            if (i != 26) {
                switch (i) {
                    case 6:
                        jSONLexer.nextToken();
                        return Boolean.TRUE;
                    case 7:
                        jSONLexer.nextToken();
                        return Boolean.FALSE;
                    case 8:
                        jSONLexer.nextToken();
                        return null;
                    case 9:
                        jSONLexer.nextToken(18);
                        if (jSONLexer.token() == 18) {
                            jSONLexer.nextToken(10);
                            accept(10);
                            long longValue = jSONLexer.integerValue().longValue();
                            accept(2);
                            accept(11);
                            return new Date(longValue);
                        }
                        throw new JSONException("syntax error");
                    default:
                        switch (i) {
                            case 20:
                                if (jSONLexer.isBlankInput()) {
                                    return null;
                                }
                                throw new JSONException("unterminated json string, " + jSONLexer.info());
                            case 21:
                                jSONLexer.nextToken();
                                HashSet hashSet = new HashSet();
                                parseArray((Collection) hashSet, obj);
                                return hashSet;
                            case 22:
                                jSONLexer.nextToken();
                                TreeSet treeSet = new TreeSet();
                                parseArray((Collection) treeSet, obj);
                                return treeSet;
                            case 23:
                                jSONLexer.nextToken();
                                return null;
                            default:
                                throw new JSONException("syntax error, " + jSONLexer.info());
                        }
                }
            } else {
                byte[] bytesValue = jSONLexer.bytesValue();
                jSONLexer.nextToken();
                return bytesValue;
            }
        } else if ("NaN".equals(jSONLexer.stringVal())) {
            jSONLexer.nextToken();
            return null;
        } else {
            throw new JSONException("syntax error, " + jSONLexer.info());
        }
    }

    public void config(Feature feature, boolean z) {
        this.lexer.config(feature, z);
    }

    public boolean isEnabled(Feature feature) {
        return this.lexer.isEnabled(feature);
    }

    public JSONLexer getLexer() {
        return this.lexer;
    }

    public final void accept(int i) {
        JSONLexer jSONLexer = this.lexer;
        if (jSONLexer.token() == i) {
            jSONLexer.nextToken();
            return;
        }
        throw new JSONException("syntax error, expect " + JSONToken.name(i) + ", actual " + JSONToken.name(jSONLexer.token()));
    }

    public final void accept(int i, int i2) {
        JSONLexer jSONLexer = this.lexer;
        if (jSONLexer.token() == i) {
            jSONLexer.nextToken(i2);
        } else {
            throwException(i);
        }
    }

    public void throwException(int i) {
        throw new JSONException("syntax error, expect " + JSONToken.name(i) + ", actual " + JSONToken.name(this.lexer.token()));
    }

    public void close() {
        JSONLexer jSONLexer = this.lexer;
        try {
            if (jSONLexer.isEnabled(Feature.AutoCloseSource)) {
                if (jSONLexer.token() != 20) {
                    throw new JSONException("not close json text, token : " + JSONToken.name(jSONLexer.token()));
                }
            }
        } finally {
            jSONLexer.close();
        }
    }

    public Object resolveReference(String str) {
        if (this.contextArray == null) {
            return null;
        }
        int i = 0;
        while (true) {
            ParseContext[] parseContextArr = this.contextArray;
            if (i >= parseContextArr.length || i >= this.contextArrayIndex) {
                return null;
            }
            ParseContext parseContext = parseContextArr[i];
            if (parseContext.toString().equals(str)) {
                return parseContext.object;
            }
            i++;
        }
        return null;
    }

    public void handleResovleTask(Object obj) {
        Object obj2;
        List<ResolveTask> list = this.resolveTaskList;
        if (list != null) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                ResolveTask resolveTask = this.resolveTaskList.get(i);
                String str = resolveTask.referenceValue;
                Object obj3 = null;
                if (resolveTask.ownerContext != null) {
                    obj3 = resolveTask.ownerContext.object;
                }
                if (str.startsWith(Operators.DOLLAR_STR)) {
                    obj2 = getObject(str);
                    if (obj2 == null) {
                        try {
                            JSONPath jSONPath = new JSONPath(str, SerializeConfig.getGlobalInstance(), this.config, true);
                            if (jSONPath.isRef()) {
                                obj2 = jSONPath.eval(obj);
                            }
                        } catch (JSONPathException unused) {
                        }
                    }
                } else {
                    obj2 = resolveTask.context.object;
                }
                FieldDeserializer fieldDeserializer = resolveTask.fieldDeserializer;
                if (fieldDeserializer != null) {
                    if (obj2 != null && obj2.getClass() == JSONObject.class && fieldDeserializer.fieldInfo != null && !Map.class.isAssignableFrom(fieldDeserializer.fieldInfo.fieldClass)) {
                        Object obj4 = this.contextArray[0].object;
                        JSONPath compile = JSONPath.compile(str);
                        if (compile.isRef()) {
                            obj2 = compile.eval(obj4);
                        }
                    }
                    if (fieldDeserializer.getOwnerClass() != null && !fieldDeserializer.getOwnerClass().isInstance(obj3) && resolveTask.ownerContext.parent != null) {
                        ParseContext parseContext = resolveTask.ownerContext;
                        while (true) {
                            parseContext = parseContext.parent;
                            if (parseContext != null) {
                                if (fieldDeserializer.getOwnerClass().isInstance(parseContext.object)) {
                                    obj3 = parseContext.object;
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                    fieldDeserializer.setValue(obj3, obj2);
                }
            }
        }
    }

    public static class ResolveTask {
        public final ParseContext context;
        public FieldDeserializer fieldDeserializer;
        public ParseContext ownerContext;
        public final String referenceValue;

        public ResolveTask(ParseContext parseContext, String str) {
            this.context = parseContext;
            this.referenceValue = str;
        }
    }

    public void parseExtra(Object obj, String str) {
        Object obj2;
        this.lexer.nextTokenWithColon();
        List<ExtraTypeProvider> list = this.extraTypeProviders;
        Type type = null;
        if (list != null) {
            for (ExtraTypeProvider extraType : list) {
                type = extraType.getExtraType(obj, str);
            }
        }
        if (type == null) {
            obj2 = parse();
        } else {
            obj2 = parseObject(type);
        }
        if (obj instanceof ExtraProcessable) {
            ((ExtraProcessable) obj).processExtra(str, obj2);
            return;
        }
        List<ExtraProcessor> list2 = this.extraProcessors;
        if (list2 != null) {
            for (ExtraProcessor processExtra : list2) {
                processExtra.processExtra(obj, str, obj2);
            }
        }
        if (this.resolveStatus == 1) {
            this.resolveStatus = 0;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:61:?, code lost:
        r11 = r10.config.getDeserializer((java.lang.reflect.Type) r2);
        r10.lexer.nextToken(16);
        setResolveStatus(2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x01d7, code lost:
        if (r0 == null) goto L_0x01e0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x01db, code lost:
        if ((r12 instanceof java.lang.Integer) != false) goto L_0x01e0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x01dd, code lost:
        popContext();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x01e0, code lost:
        r11 = (java.util.Map) r11.deserialze(r10, r2, r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x01e6, code lost:
        setContext(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x01e9, code lost:
        return r11;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object parse(com.alibaba.fastjson.parser.deserializer.PropertyProcessable r11, java.lang.Object r12) {
        /*
            r10 = this;
            com.alibaba.fastjson.parser.JSONLexer r0 = r10.lexer
            int r0 = r0.token()
            r1 = 0
            r2 = 12
            if (r0 == r2) goto L_0x008c
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r0 = "syntax error, expect {, actual "
            r11.append(r0)
            com.alibaba.fastjson.parser.JSONLexer r0 = r10.lexer
            java.lang.String r0 = r0.tokenName()
            r11.append(r0)
            java.lang.String r11 = r11.toString()
            boolean r0 = r12 instanceof java.lang.String
            if (r0 == 0) goto L_0x0046
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r11)
            java.lang.String r11 = ", fieldName "
            r0.append(r11)
            java.lang.String r11 = r0.toString()
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r11)
            r0.append(r12)
            java.lang.String r11 = r0.toString()
        L_0x0046:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r11)
            java.lang.String r11 = ", "
            r0.append(r11)
            java.lang.String r11 = r0.toString()
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r11)
            com.alibaba.fastjson.parser.JSONLexer r11 = r10.lexer
            java.lang.String r11 = r11.info()
            r0.append(r11)
            java.lang.String r11 = r0.toString()
            com.alibaba.fastjson.JSONArray r0 = new com.alibaba.fastjson.JSONArray
            r0.<init>()
            r10.parseArray((java.util.Collection) r0, (java.lang.Object) r12)
            int r12 = r0.size()
            r2 = 1
            if (r12 != r2) goto L_0x0086
            java.lang.Object r12 = r0.get(r1)
            boolean r0 = r12 instanceof com.alibaba.fastjson.JSONObject
            if (r0 == 0) goto L_0x0086
            com.alibaba.fastjson.JSONObject r12 = (com.alibaba.fastjson.JSONObject) r12
            return r12
        L_0x0086:
            com.alibaba.fastjson.JSONException r12 = new com.alibaba.fastjson.JSONException
            r12.<init>(r11)
            throw r12
        L_0x008c:
            com.alibaba.fastjson.parser.ParseContext r0 = r10.context
        L_0x008e:
            com.alibaba.fastjson.parser.JSONLexer r2 = r10.lexer     // Catch:{ all -> 0x0260 }
            r2.skipWhitespace()     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.JSONLexer r2 = r10.lexer     // Catch:{ all -> 0x0260 }
            char r2 = r2.getCurrent()     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.JSONLexer r3 = r10.lexer     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.Feature r4 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas     // Catch:{ all -> 0x0260 }
            boolean r3 = r3.isEnabled((com.alibaba.fastjson.parser.Feature) r4)     // Catch:{ all -> 0x0260 }
            if (r3 == 0) goto L_0x00b8
        L_0x00a3:
            r3 = 44
            if (r2 != r3) goto L_0x00b8
            com.alibaba.fastjson.parser.JSONLexer r2 = r10.lexer     // Catch:{ all -> 0x0260 }
            r2.next()     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.JSONLexer r2 = r10.lexer     // Catch:{ all -> 0x0260 }
            r2.skipWhitespace()     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.JSONLexer r2 = r10.lexer     // Catch:{ all -> 0x0260 }
            char r2 = r2.getCurrent()     // Catch:{ all -> 0x0260 }
            goto L_0x00a3
        L_0x00b8:
            java.lang.String r3 = "expect ':' at "
            r4 = 58
            r5 = 34
            r6 = 16
            if (r2 != r5) goto L_0x00f4
            com.alibaba.fastjson.parser.JSONLexer r2 = r10.lexer     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.SymbolTable r7 = r10.symbolTable     // Catch:{ all -> 0x0260 }
            java.lang.String r2 = r2.scanSymbol(r7, r5)     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.JSONLexer r7 = r10.lexer     // Catch:{ all -> 0x0260 }
            r7.skipWhitespace()     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.JSONLexer r7 = r10.lexer     // Catch:{ all -> 0x0260 }
            char r7 = r7.getCurrent()     // Catch:{ all -> 0x0260 }
            if (r7 != r4) goto L_0x00d9
            goto L_0x0171
        L_0x00d9:
            com.alibaba.fastjson.JSONException r11 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0260 }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ all -> 0x0260 }
            r12.<init>()     // Catch:{ all -> 0x0260 }
            r12.append(r3)     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.JSONLexer r1 = r10.lexer     // Catch:{ all -> 0x0260 }
            int r1 = r1.pos()     // Catch:{ all -> 0x0260 }
            r12.append(r1)     // Catch:{ all -> 0x0260 }
            java.lang.String r12 = r12.toString()     // Catch:{ all -> 0x0260 }
            r11.<init>(r12)     // Catch:{ all -> 0x0260 }
            throw r11     // Catch:{ all -> 0x0260 }
        L_0x00f4:
            r7 = 125(0x7d, float:1.75E-43)
            if (r2 != r7) goto L_0x010b
            com.alibaba.fastjson.parser.JSONLexer r12 = r10.lexer     // Catch:{ all -> 0x0260 }
            r12.next()     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.JSONLexer r12 = r10.lexer     // Catch:{ all -> 0x0260 }
            r12.resetStringPosition()     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.JSONLexer r12 = r10.lexer     // Catch:{ all -> 0x0260 }
            r12.nextToken(r6)     // Catch:{ all -> 0x0260 }
            r10.setContext(r0)
            return r11
        L_0x010b:
            java.lang.String r7 = "syntax error"
            r8 = 39
            if (r2 != r8) goto L_0x0152
            com.alibaba.fastjson.parser.JSONLexer r2 = r10.lexer     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.Feature r9 = com.alibaba.fastjson.parser.Feature.AllowSingleQuotes     // Catch:{ all -> 0x0260 }
            boolean r2 = r2.isEnabled((com.alibaba.fastjson.parser.Feature) r9)     // Catch:{ all -> 0x0260 }
            if (r2 == 0) goto L_0x014c
            com.alibaba.fastjson.parser.JSONLexer r2 = r10.lexer     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.SymbolTable r7 = r10.symbolTable     // Catch:{ all -> 0x0260 }
            java.lang.String r2 = r2.scanSymbol(r7, r8)     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.JSONLexer r7 = r10.lexer     // Catch:{ all -> 0x0260 }
            r7.skipWhitespace()     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.JSONLexer r7 = r10.lexer     // Catch:{ all -> 0x0260 }
            char r7 = r7.getCurrent()     // Catch:{ all -> 0x0260 }
            if (r7 != r4) goto L_0x0131
            goto L_0x0171
        L_0x0131:
            com.alibaba.fastjson.JSONException r11 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0260 }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ all -> 0x0260 }
            r12.<init>()     // Catch:{ all -> 0x0260 }
            r12.append(r3)     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.JSONLexer r1 = r10.lexer     // Catch:{ all -> 0x0260 }
            int r1 = r1.pos()     // Catch:{ all -> 0x0260 }
            r12.append(r1)     // Catch:{ all -> 0x0260 }
            java.lang.String r12 = r12.toString()     // Catch:{ all -> 0x0260 }
            r11.<init>(r12)     // Catch:{ all -> 0x0260 }
            throw r11     // Catch:{ all -> 0x0260 }
        L_0x014c:
            com.alibaba.fastjson.JSONException r11 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0260 }
            r11.<init>(r7)     // Catch:{ all -> 0x0260 }
            throw r11     // Catch:{ all -> 0x0260 }
        L_0x0152:
            com.alibaba.fastjson.parser.JSONLexer r2 = r10.lexer     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.Feature r8 = com.alibaba.fastjson.parser.Feature.AllowUnQuotedFieldNames     // Catch:{ all -> 0x0260 }
            boolean r2 = r2.isEnabled((com.alibaba.fastjson.parser.Feature) r8)     // Catch:{ all -> 0x0260 }
            if (r2 == 0) goto L_0x025a
            com.alibaba.fastjson.parser.JSONLexer r2 = r10.lexer     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.SymbolTable r7 = r10.symbolTable     // Catch:{ all -> 0x0260 }
            java.lang.String r2 = r2.scanSymbolUnQuoted(r7)     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.JSONLexer r7 = r10.lexer     // Catch:{ all -> 0x0260 }
            r7.skipWhitespace()     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.JSONLexer r7 = r10.lexer     // Catch:{ all -> 0x0260 }
            char r7 = r7.getCurrent()     // Catch:{ all -> 0x0260 }
            if (r7 != r4) goto L_0x0237
        L_0x0171:
            com.alibaba.fastjson.parser.JSONLexer r3 = r10.lexer     // Catch:{ all -> 0x0260 }
            r3.next()     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.JSONLexer r3 = r10.lexer     // Catch:{ all -> 0x0260 }
            r3.skipWhitespace()     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.JSONLexer r3 = r10.lexer     // Catch:{ all -> 0x0260 }
            r3.getCurrent()     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.JSONLexer r3 = r10.lexer     // Catch:{ all -> 0x0260 }
            r3.resetStringPosition()     // Catch:{ all -> 0x0260 }
            java.lang.String r3 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY     // Catch:{ all -> 0x0260 }
            r4 = 13
            r7 = 0
            if (r2 != r3) goto L_0x01ea
            com.alibaba.fastjson.parser.JSONLexer r3 = r10.lexer     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.Feature r8 = com.alibaba.fastjson.parser.Feature.DisableSpecialKeyDetect     // Catch:{ all -> 0x0260 }
            boolean r3 = r3.isEnabled((com.alibaba.fastjson.parser.Feature) r8)     // Catch:{ all -> 0x0260 }
            if (r3 != 0) goto L_0x01ea
            com.alibaba.fastjson.parser.JSONLexer r2 = r10.lexer     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.SymbolTable r3 = r10.symbolTable     // Catch:{ all -> 0x0260 }
            java.lang.String r2 = r2.scanSymbol(r3, r5)     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.ParserConfig r3 = r10.config     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.JSONLexer r5 = r10.lexer     // Catch:{ all -> 0x0260 }
            int r5 = r5.getFeatures()     // Catch:{ all -> 0x0260 }
            java.lang.Class r2 = r3.checkAutoType(r2, r7, r5)     // Catch:{ all -> 0x0260 }
            java.lang.Class<java.util.Map> r3 = java.util.Map.class
            boolean r3 = r3.isAssignableFrom(r2)     // Catch:{ all -> 0x0260 }
            if (r3 == 0) goto L_0x01c8
            com.alibaba.fastjson.parser.JSONLexer r2 = r10.lexer     // Catch:{ all -> 0x0260 }
            r2.nextToken(r6)     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.JSONLexer r2 = r10.lexer     // Catch:{ all -> 0x0260 }
            int r2 = r2.token()     // Catch:{ all -> 0x0260 }
            if (r2 != r4) goto L_0x022f
            com.alibaba.fastjson.parser.JSONLexer r12 = r10.lexer     // Catch:{ all -> 0x0260 }
            r12.nextToken(r6)     // Catch:{ all -> 0x0260 }
            r10.setContext(r0)
            return r11
        L_0x01c8:
            com.alibaba.fastjson.parser.ParserConfig r11 = r10.config     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r11 = r11.getDeserializer((java.lang.reflect.Type) r2)     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.JSONLexer r1 = r10.lexer     // Catch:{ all -> 0x0260 }
            r1.nextToken(r6)     // Catch:{ all -> 0x0260 }
            r1 = 2
            r10.setResolveStatus(r1)     // Catch:{ all -> 0x0260 }
            if (r0 == 0) goto L_0x01e0
            boolean r1 = r12 instanceof java.lang.Integer     // Catch:{ all -> 0x0260 }
            if (r1 != 0) goto L_0x01e0
            r10.popContext()     // Catch:{ all -> 0x0260 }
        L_0x01e0:
            java.lang.Object r11 = r11.deserialze(r10, r2, r12)     // Catch:{ all -> 0x0260 }
            java.util.Map r11 = (java.util.Map) r11     // Catch:{ all -> 0x0260 }
            r10.setContext(r0)
            return r11
        L_0x01ea:
            com.alibaba.fastjson.parser.JSONLexer r3 = r10.lexer     // Catch:{ all -> 0x0260 }
            r3.nextToken()     // Catch:{ all -> 0x0260 }
            if (r1 == 0) goto L_0x01f4
            r10.setContext(r0)     // Catch:{ all -> 0x0260 }
        L_0x01f4:
            java.lang.reflect.Type r3 = r11.getType(r2)     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.JSONLexer r5 = r10.lexer     // Catch:{ all -> 0x0260 }
            int r5 = r5.token()     // Catch:{ all -> 0x0260 }
            r6 = 8
            if (r5 != r6) goto L_0x0208
            com.alibaba.fastjson.parser.JSONLexer r3 = r10.lexer     // Catch:{ all -> 0x0260 }
            r3.nextToken()     // Catch:{ all -> 0x0260 }
            goto L_0x020c
        L_0x0208:
            java.lang.Object r7 = r10.parseObject((java.lang.reflect.Type) r3, (java.lang.Object) r2)     // Catch:{ all -> 0x0260 }
        L_0x020c:
            r11.apply(r2, r7)     // Catch:{ all -> 0x0260 }
            r10.setContext(r0, r7, r2)     // Catch:{ all -> 0x0260 }
            r10.setContext(r0)     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.JSONLexer r2 = r10.lexer     // Catch:{ all -> 0x0260 }
            int r2 = r2.token()     // Catch:{ all -> 0x0260 }
            r3 = 20
            if (r2 == r3) goto L_0x0233
            r3 = 15
            if (r2 != r3) goto L_0x0224
            goto L_0x0233
        L_0x0224:
            if (r2 != r4) goto L_0x022f
            com.alibaba.fastjson.parser.JSONLexer r12 = r10.lexer     // Catch:{ all -> 0x0260 }
            r12.nextToken()     // Catch:{ all -> 0x0260 }
            r10.setContext(r0)
            return r11
        L_0x022f:
            int r1 = r1 + 1
            goto L_0x008e
        L_0x0233:
            r10.setContext(r0)
            return r11
        L_0x0237:
            com.alibaba.fastjson.JSONException r11 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0260 }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ all -> 0x0260 }
            r12.<init>()     // Catch:{ all -> 0x0260 }
            r12.append(r3)     // Catch:{ all -> 0x0260 }
            com.alibaba.fastjson.parser.JSONLexer r1 = r10.lexer     // Catch:{ all -> 0x0260 }
            int r1 = r1.pos()     // Catch:{ all -> 0x0260 }
            r12.append(r1)     // Catch:{ all -> 0x0260 }
            java.lang.String r1 = ", actual "
            r12.append(r1)     // Catch:{ all -> 0x0260 }
            r12.append(r7)     // Catch:{ all -> 0x0260 }
            java.lang.String r12 = r12.toString()     // Catch:{ all -> 0x0260 }
            r11.<init>(r12)     // Catch:{ all -> 0x0260 }
            throw r11     // Catch:{ all -> 0x0260 }
        L_0x025a:
            com.alibaba.fastjson.JSONException r11 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x0260 }
            r11.<init>(r7)     // Catch:{ all -> 0x0260 }
            throw r11     // Catch:{ all -> 0x0260 }
        L_0x0260:
            r11 = move-exception
            r10.setContext(r0)
            goto L_0x0266
        L_0x0265:
            throw r11
        L_0x0266:
            goto L_0x0265
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.DefaultJSONParser.parse(com.alibaba.fastjson.parser.deserializer.PropertyProcessable, java.lang.Object):java.lang.Object");
    }
}
