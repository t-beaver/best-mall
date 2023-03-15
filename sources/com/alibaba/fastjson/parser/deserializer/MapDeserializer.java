package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.common.util.PdrUtil;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MapDeserializer extends ContextObjectDeserializer implements ObjectDeserializer {
    public static MapDeserializer instance = new MapDeserializer();

    public int getFastMatchToken() {
        return 12;
    }

    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj, String str, int i) {
        Map<Object, Object> map;
        if (type == JSONObject.class && defaultJSONParser.getFieldTypeResolver() == null) {
            return defaultJSONParser.parseObject();
        }
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        if (jSONLexer.token() == 8) {
            jSONLexer.nextToken(16);
            return null;
        }
        boolean z = (type instanceof Class) && "java.util.Collections$UnmodifiableMap".equals(((Class) type).getName());
        if ((jSONLexer.getFeatures() & Feature.OrderedField.mask) != 0) {
            map = createMap(type, jSONLexer.getFeatures());
        } else {
            map = createMap(type);
        }
        Map<Object, Object> map2 = map;
        ParseContext context = defaultJSONParser.getContext();
        try {
            defaultJSONParser.setContext(context, map2, obj);
            T deserialze = deserialze(defaultJSONParser, type, obj, (Map) map2, i);
            if (z) {
                deserialze = Collections.unmodifiableMap((Map) deserialze);
            }
            return deserialze;
        } finally {
            defaultJSONParser.setContext(context);
        }
    }

    /* access modifiers changed from: protected */
    public Object deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj, Map map) {
        return deserialze(defaultJSONParser, type, obj, map, 0);
    }

    /* access modifiers changed from: protected */
    public Object deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj, Map map, int i) {
        Type type2;
        if (!(type instanceof ParameterizedType)) {
            return defaultJSONParser.parseObject(map, obj);
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type type3 = parameterizedType.getActualTypeArguments()[0];
        if (map.getClass().getName().equals("org.springframework.util.LinkedMultiValueMap")) {
            type2 = List.class;
        } else {
            type2 = parameterizedType.getActualTypeArguments()[1];
        }
        if (String.class == type3) {
            return parseMap(defaultJSONParser, (Map<String, Object>) map, type2, obj, i);
        }
        return parseMap(defaultJSONParser, (Map<Object, Object>) map, type3, type2, obj);
    }

    public static Map parseMap(DefaultJSONParser defaultJSONParser, Map<String, Object> map, Type type, Object obj) {
        return parseMap(defaultJSONParser, map, type, obj, 0);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:87:?, code lost:
        r12 = r5.getDeserializer((java.lang.reflect.Type) r4);
        r0.nextToken(16);
        r11.setResolveStatus(2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x01dd, code lost:
        if (r1 == null) goto L_0x01e6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x01e1, code lost:
        if ((r14 instanceof java.lang.Integer) != false) goto L_0x01e6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:0x01e3, code lost:
        r11.popContext();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x01e6, code lost:
        r12 = (java.util.Map) r12.deserialze(r11, r4, r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x01ec, code lost:
        r11.setContext(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x01ef, code lost:
        return r12;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Map parseMap(com.alibaba.fastjson.parser.DefaultJSONParser r11, java.util.Map<java.lang.String, java.lang.Object> r12, java.lang.reflect.Type r13, java.lang.Object r14, int r15) {
        /*
            com.alibaba.fastjson.parser.JSONLexer r0 = r11.lexer
            int r1 = r0.token()
            r2 = 0
            r3 = 0
            r4 = 12
            if (r1 == r4) goto L_0x00a1
            r12 = 4
            if (r1 != r12) goto L_0x0022
            java.lang.String r13 = r0.stringVal()
            int r15 = r13.length()
            if (r15 == 0) goto L_0x0021
            java.lang.String r15 = "null"
            boolean r13 = r13.equals(r15)
            if (r13 == 0) goto L_0x0022
        L_0x0021:
            return r3
        L_0x0022:
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r15 = "syntax error, expect {, actual "
            r13.append(r15)
            java.lang.String r15 = r0.tokenName()
            r13.append(r15)
            java.lang.String r13 = r13.toString()
            boolean r15 = r14 instanceof java.lang.String
            if (r15 == 0) goto L_0x005b
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            r15.append(r13)
            java.lang.String r13 = ", fieldName "
            r15.append(r13)
            java.lang.String r13 = r15.toString()
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            r15.append(r13)
            r15.append(r14)
            java.lang.String r13 = r15.toString()
        L_0x005b:
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            r15.append(r13)
            java.lang.String r13 = ", "
            r15.append(r13)
            java.lang.String r13 = r15.toString()
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            r15.append(r13)
            java.lang.String r13 = r0.info()
            r15.append(r13)
            java.lang.String r13 = r15.toString()
            if (r1 == r12) goto L_0x009b
            com.alibaba.fastjson.JSONArray r12 = new com.alibaba.fastjson.JSONArray
            r12.<init>()
            r11.parseArray((java.util.Collection) r12, (java.lang.Object) r14)
            int r11 = r12.size()
            r14 = 1
            if (r11 != r14) goto L_0x009b
            java.lang.Object r11 = r12.get(r2)
            boolean r12 = r11 instanceof com.alibaba.fastjson.JSONObject
            if (r12 == 0) goto L_0x009b
            com.alibaba.fastjson.JSONObject r11 = (com.alibaba.fastjson.JSONObject) r11
            return r11
        L_0x009b:
            com.alibaba.fastjson.JSONException r11 = new com.alibaba.fastjson.JSONException
            r11.<init>(r13)
            throw r11
        L_0x00a1:
            com.alibaba.fastjson.parser.ParseContext r1 = r11.getContext()
        L_0x00a5:
            r0.skipWhitespace()     // Catch:{ all -> 0x025a }
            char r4 = r0.getCurrent()     // Catch:{ all -> 0x025a }
            com.alibaba.fastjson.parser.Feature r5 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas     // Catch:{ all -> 0x025a }
            boolean r5 = r0.isEnabled((com.alibaba.fastjson.parser.Feature) r5)     // Catch:{ all -> 0x025a }
            if (r5 == 0) goto L_0x00c3
        L_0x00b4:
            r5 = 44
            if (r4 != r5) goto L_0x00c3
            r0.next()     // Catch:{ all -> 0x025a }
            r0.skipWhitespace()     // Catch:{ all -> 0x025a }
            char r4 = r0.getCurrent()     // Catch:{ all -> 0x025a }
            goto L_0x00b4
        L_0x00c3:
            java.lang.String r5 = "expect ':' at "
            r6 = 58
            r7 = 34
            r8 = 16
            if (r4 != r7) goto L_0x00f9
            com.alibaba.fastjson.parser.SymbolTable r4 = r11.getSymbolTable()     // Catch:{ all -> 0x025a }
            java.lang.String r4 = r0.scanSymbol(r4, r7)     // Catch:{ all -> 0x025a }
            r0.skipWhitespace()     // Catch:{ all -> 0x025a }
            char r9 = r0.getCurrent()     // Catch:{ all -> 0x025a }
            if (r9 != r6) goto L_0x00e0
            goto L_0x0162
        L_0x00e0:
            com.alibaba.fastjson.JSONException r12 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x025a }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ all -> 0x025a }
            r13.<init>()     // Catch:{ all -> 0x025a }
            r13.append(r5)     // Catch:{ all -> 0x025a }
            int r14 = r0.pos()     // Catch:{ all -> 0x025a }
            r13.append(r14)     // Catch:{ all -> 0x025a }
            java.lang.String r13 = r13.toString()     // Catch:{ all -> 0x025a }
            r12.<init>(r13)     // Catch:{ all -> 0x025a }
            throw r12     // Catch:{ all -> 0x025a }
        L_0x00f9:
            r9 = 125(0x7d, float:1.75E-43)
            if (r4 != r9) goto L_0x010a
            r0.next()     // Catch:{ all -> 0x025a }
            r0.resetStringPosition()     // Catch:{ all -> 0x025a }
            r0.nextToken(r8)     // Catch:{ all -> 0x025a }
            r11.setContext(r1)
            return r12
        L_0x010a:
            java.lang.String r9 = "syntax error"
            r10 = 39
            if (r4 != r10) goto L_0x0149
            com.alibaba.fastjson.parser.Feature r4 = com.alibaba.fastjson.parser.Feature.AllowSingleQuotes     // Catch:{ all -> 0x025a }
            boolean r4 = r0.isEnabled((com.alibaba.fastjson.parser.Feature) r4)     // Catch:{ all -> 0x025a }
            if (r4 == 0) goto L_0x0143
            com.alibaba.fastjson.parser.SymbolTable r4 = r11.getSymbolTable()     // Catch:{ all -> 0x025a }
            java.lang.String r4 = r0.scanSymbol(r4, r10)     // Catch:{ all -> 0x025a }
            r0.skipWhitespace()     // Catch:{ all -> 0x025a }
            char r9 = r0.getCurrent()     // Catch:{ all -> 0x025a }
            if (r9 != r6) goto L_0x012a
            goto L_0x0162
        L_0x012a:
            com.alibaba.fastjson.JSONException r12 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x025a }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ all -> 0x025a }
            r13.<init>()     // Catch:{ all -> 0x025a }
            r13.append(r5)     // Catch:{ all -> 0x025a }
            int r14 = r0.pos()     // Catch:{ all -> 0x025a }
            r13.append(r14)     // Catch:{ all -> 0x025a }
            java.lang.String r13 = r13.toString()     // Catch:{ all -> 0x025a }
            r12.<init>(r13)     // Catch:{ all -> 0x025a }
            throw r12     // Catch:{ all -> 0x025a }
        L_0x0143:
            com.alibaba.fastjson.JSONException r12 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x025a }
            r12.<init>(r9)     // Catch:{ all -> 0x025a }
            throw r12     // Catch:{ all -> 0x025a }
        L_0x0149:
            com.alibaba.fastjson.parser.Feature r4 = com.alibaba.fastjson.parser.Feature.AllowUnQuotedFieldNames     // Catch:{ all -> 0x025a }
            boolean r4 = r0.isEnabled((com.alibaba.fastjson.parser.Feature) r4)     // Catch:{ all -> 0x025a }
            if (r4 == 0) goto L_0x0254
            com.alibaba.fastjson.parser.SymbolTable r4 = r11.getSymbolTable()     // Catch:{ all -> 0x025a }
            java.lang.String r4 = r0.scanSymbolUnQuoted(r4)     // Catch:{ all -> 0x025a }
            r0.skipWhitespace()     // Catch:{ all -> 0x025a }
            char r9 = r0.getCurrent()     // Catch:{ all -> 0x025a }
            if (r9 != r6) goto L_0x0233
        L_0x0162:
            r0.next()     // Catch:{ all -> 0x025a }
            r0.skipWhitespace()     // Catch:{ all -> 0x025a }
            r0.getCurrent()     // Catch:{ all -> 0x025a }
            r0.resetStringPosition()     // Catch:{ all -> 0x025a }
            java.lang.String r5 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY     // Catch:{ all -> 0x025a }
            r6 = 13
            if (r4 != r5) goto L_0x01f0
            com.alibaba.fastjson.parser.Feature r5 = com.alibaba.fastjson.parser.Feature.DisableSpecialKeyDetect     // Catch:{ all -> 0x025a }
            boolean r5 = r0.isEnabled((com.alibaba.fastjson.parser.Feature) r5)     // Catch:{ all -> 0x025a }
            if (r5 != 0) goto L_0x01f0
            com.alibaba.fastjson.parser.Feature r5 = com.alibaba.fastjson.parser.Feature.DisableSpecialKeyDetect     // Catch:{ all -> 0x025a }
            boolean r5 = com.alibaba.fastjson.parser.Feature.isEnabled(r15, r5)     // Catch:{ all -> 0x025a }
            if (r5 != 0) goto L_0x01f0
            com.alibaba.fastjson.parser.SymbolTable r4 = r11.getSymbolTable()     // Catch:{ all -> 0x025a }
            java.lang.String r4 = r0.scanSymbol(r4, r7)     // Catch:{ all -> 0x025a }
            com.alibaba.fastjson.parser.ParserConfig r5 = r11.getConfig()     // Catch:{ all -> 0x025a }
            java.lang.String r7 = "java.util.HashMap"
            boolean r7 = r4.equals(r7)     // Catch:{ all -> 0x025a }
            if (r7 == 0) goto L_0x019b
            java.lang.Class<java.util.HashMap> r4 = java.util.HashMap.class
            goto L_0x01ba
        L_0x019b:
            java.lang.String r7 = "java.util.LinkedHashMap"
            boolean r7 = r4.equals(r7)     // Catch:{ all -> 0x025a }
            if (r7 == 0) goto L_0x01a6
            java.lang.Class<java.util.LinkedHashMap> r4 = java.util.LinkedHashMap.class
            goto L_0x01ba
        L_0x01a6:
            boolean r7 = r5.isSafeMode()     // Catch:{ all -> 0x025a }
            if (r7 == 0) goto L_0x01af
            java.lang.Class<java.util.HashMap> r4 = java.util.HashMap.class
            goto L_0x01ba
        L_0x01af:
            int r7 = r0.getFeatures()     // Catch:{ JSONException -> 0x01b8 }
            java.lang.Class r4 = r5.checkAutoType(r4, r3, r7)     // Catch:{ JSONException -> 0x01b8 }
            goto L_0x01ba
        L_0x01b8:
            java.lang.Class<java.util.HashMap> r4 = java.util.HashMap.class
        L_0x01ba:
            java.lang.Class<java.util.Map> r7 = java.util.Map.class
            boolean r7 = r7.isAssignableFrom(r4)     // Catch:{ all -> 0x025a }
            if (r7 == 0) goto L_0x01d2
            r0.nextToken(r8)     // Catch:{ all -> 0x025a }
            int r4 = r0.token()     // Catch:{ all -> 0x025a }
            if (r4 != r6) goto L_0x022b
            r0.nextToken(r8)     // Catch:{ all -> 0x025a }
            r11.setContext(r1)
            return r12
        L_0x01d2:
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r12 = r5.getDeserializer((java.lang.reflect.Type) r4)     // Catch:{ all -> 0x025a }
            r0.nextToken(r8)     // Catch:{ all -> 0x025a }
            r13 = 2
            r11.setResolveStatus(r13)     // Catch:{ all -> 0x025a }
            if (r1 == 0) goto L_0x01e6
            boolean r13 = r14 instanceof java.lang.Integer     // Catch:{ all -> 0x025a }
            if (r13 != 0) goto L_0x01e6
            r11.popContext()     // Catch:{ all -> 0x025a }
        L_0x01e6:
            java.lang.Object r12 = r12.deserialze(r11, r4, r14)     // Catch:{ all -> 0x025a }
            java.util.Map r12 = (java.util.Map) r12     // Catch:{ all -> 0x025a }
            r11.setContext(r1)
            return r12
        L_0x01f0:
            r0.nextToken()     // Catch:{ all -> 0x025a }
            if (r2 == 0) goto L_0x01f8
            r11.setContext(r1)     // Catch:{ all -> 0x025a }
        L_0x01f8:
            int r5 = r0.token()     // Catch:{ all -> 0x025a }
            r7 = 8
            if (r5 != r7) goto L_0x0205
            r0.nextToken()     // Catch:{ all -> 0x025a }
            r5 = r3
            goto L_0x0209
        L_0x0205:
            java.lang.Object r5 = r11.parseObject((java.lang.reflect.Type) r13, (java.lang.Object) r4)     // Catch:{ all -> 0x025a }
        L_0x0209:
            r12.put(r4, r5)     // Catch:{ all -> 0x025a }
            r11.checkMapResolve(r12, r4)     // Catch:{ all -> 0x025a }
            r11.setContext(r1, r5, r4)     // Catch:{ all -> 0x025a }
            r11.setContext(r1)     // Catch:{ all -> 0x025a }
            int r4 = r0.token()     // Catch:{ all -> 0x025a }
            r5 = 20
            if (r4 == r5) goto L_0x022f
            r5 = 15
            if (r4 != r5) goto L_0x0222
            goto L_0x022f
        L_0x0222:
            if (r4 != r6) goto L_0x022b
            r0.nextToken()     // Catch:{ all -> 0x025a }
            r11.setContext(r1)
            return r12
        L_0x022b:
            int r2 = r2 + 1
            goto L_0x00a5
        L_0x022f:
            r11.setContext(r1)
            return r12
        L_0x0233:
            com.alibaba.fastjson.JSONException r12 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x025a }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ all -> 0x025a }
            r13.<init>()     // Catch:{ all -> 0x025a }
            r13.append(r5)     // Catch:{ all -> 0x025a }
            int r14 = r0.pos()     // Catch:{ all -> 0x025a }
            r13.append(r14)     // Catch:{ all -> 0x025a }
            java.lang.String r14 = ", actual "
            r13.append(r14)     // Catch:{ all -> 0x025a }
            r13.append(r9)     // Catch:{ all -> 0x025a }
            java.lang.String r13 = r13.toString()     // Catch:{ all -> 0x025a }
            r12.<init>(r13)     // Catch:{ all -> 0x025a }
            throw r12     // Catch:{ all -> 0x025a }
        L_0x0254:
            com.alibaba.fastjson.JSONException r12 = new com.alibaba.fastjson.JSONException     // Catch:{ all -> 0x025a }
            r12.<init>(r9)     // Catch:{ all -> 0x025a }
            throw r12     // Catch:{ all -> 0x025a }
        L_0x025a:
            r12 = move-exception
            r11.setContext(r1)
            goto L_0x0260
        L_0x025f:
            throw r12
        L_0x0260:
            goto L_0x025f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.MapDeserializer.parseMap(com.alibaba.fastjson.parser.DefaultJSONParser, java.util.Map, java.lang.reflect.Type, java.lang.Object, int):java.util.Map");
    }

    public static Object parseMap(DefaultJSONParser defaultJSONParser, Map<Object, Object> map, Type type, Type type2, Object obj) {
        Object obj2;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        if (jSONLexer.token() == 12 || jSONLexer.token() == 16) {
            ObjectDeserializer deserializer = defaultJSONParser.getConfig().getDeserializer(type);
            ObjectDeserializer deserializer2 = defaultJSONParser.getConfig().getDeserializer(type2);
            jSONLexer.nextToken(deserializer.getFastMatchToken());
            ParseContext context = defaultJSONParser.getContext();
            while (jSONLexer.token() != 13) {
                try {
                    Object obj3 = null;
                    if (jSONLexer.token() != 4 || !jSONLexer.isRef() || jSONLexer.isEnabled(Feature.DisableSpecialKeyDetect)) {
                        if (map.size() == 0 && jSONLexer.token() == 4 && JSON.DEFAULT_TYPE_KEY.equals(jSONLexer.stringVal()) && !jSONLexer.isEnabled(Feature.DisableSpecialKeyDetect)) {
                            jSONLexer.nextTokenWithColon(4);
                            jSONLexer.nextToken(16);
                            if (jSONLexer.token() == 13) {
                                jSONLexer.nextToken();
                                return map;
                            }
                            jSONLexer.nextToken(deserializer.getFastMatchToken());
                        }
                        if (jSONLexer.token() != 4 || !(deserializer instanceof JavaBeanDeserializer)) {
                            obj2 = deserializer.deserialze(defaultJSONParser, type, (Object) null);
                        } else {
                            String stringVal = jSONLexer.stringVal();
                            jSONLexer.nextToken();
                            DefaultJSONParser defaultJSONParser2 = new DefaultJSONParser(stringVal, defaultJSONParser.getConfig(), defaultJSONParser.getLexer().getFeatures());
                            defaultJSONParser2.setDateFormat(defaultJSONParser.getDateFomartPattern());
                            obj2 = deserializer.deserialze(defaultJSONParser2, type, (Object) null);
                        }
                        if (jSONLexer.token() == 17) {
                            jSONLexer.nextToken(deserializer2.getFastMatchToken());
                            Object deserialze = deserializer2.deserialze(defaultJSONParser, type2, obj2);
                            defaultJSONParser.checkMapResolve(map, obj2);
                            map.put(obj2, deserialze);
                            if (jSONLexer.token() == 16) {
                                jSONLexer.nextToken(deserializer.getFastMatchToken());
                            }
                        } else {
                            throw new JSONException("syntax error, expect :, actual " + jSONLexer.token());
                        }
                    } else {
                        jSONLexer.nextTokenWithColon(4);
                        if (jSONLexer.token() == 4) {
                            String stringVal2 = jSONLexer.stringVal();
                            if (PdrUtil.FILE_PATH_ENTRY_BACK.equals(stringVal2)) {
                                obj3 = context.parent.object;
                            } else if (Operators.DOLLAR_STR.equals(stringVal2)) {
                                ParseContext parseContext = context;
                                while (parseContext.parent != null) {
                                    parseContext = parseContext.parent;
                                }
                                obj3 = parseContext.object;
                            } else {
                                defaultJSONParser.addResolveTask(new DefaultJSONParser.ResolveTask(context, stringVal2));
                                defaultJSONParser.setResolveStatus(1);
                            }
                            jSONLexer.nextToken(13);
                            if (jSONLexer.token() == 13) {
                                jSONLexer.nextToken(16);
                                defaultJSONParser.setContext(context);
                                return obj3;
                            }
                            throw new JSONException("illegal ref");
                        }
                        throw new JSONException("illegal ref, " + JSONToken.name(jSONLexer.token()));
                    }
                } finally {
                    defaultJSONParser.setContext(context);
                }
            }
            jSONLexer.nextToken(16);
            defaultJSONParser.setContext(context);
            return map;
        }
        throw new JSONException("syntax error, expect {, actual " + jSONLexer.tokenName());
    }

    public Map<Object, Object> createMap(Type type) {
        return createMap(type, JSON.DEFAULT_GENERATE_FEATURE);
    }

    public Map<Object, Object> createMap(Type type, int i) {
        if (type == Properties.class) {
            return new Properties();
        }
        if (type == Hashtable.class) {
            return new Hashtable();
        }
        if (type == IdentityHashMap.class) {
            return new IdentityHashMap();
        }
        if (type == SortedMap.class || type == TreeMap.class) {
            return new TreeMap();
        }
        if (type == ConcurrentMap.class || type == ConcurrentHashMap.class) {
            return new ConcurrentHashMap();
        }
        if (type == Map.class) {
            return (Feature.OrderedField.mask & i) != 0 ? new LinkedHashMap() : new HashMap();
        }
        if (type == HashMap.class) {
            return new HashMap();
        }
        if (type == LinkedHashMap.class) {
            return new LinkedHashMap();
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (EnumMap.class.equals(rawType)) {
                return new EnumMap((Class) parameterizedType.getActualTypeArguments()[0]);
            }
            return createMap(rawType, i);
        }
        Class cls = (Class) type;
        if (cls.isInterface()) {
            throw new JSONException("unsupport type " + type);
        } else if ("java.util.Collections$UnmodifiableMap".equals(cls.getName())) {
            return new HashMap();
        } else {
            try {
                return (Map) cls.newInstance();
            } catch (Exception e) {
                throw new JSONException("unsupport type " + type, e);
            }
        }
    }
}
