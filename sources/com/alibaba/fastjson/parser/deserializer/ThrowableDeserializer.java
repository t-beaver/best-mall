package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.ParserConfig;
import java.lang.reflect.Constructor;

public class ThrowableDeserializer extends JavaBeanDeserializer {
    public int getFastMatchToken() {
        return 12;
    }

    public ThrowableDeserializer(ParserConfig parserConfig, Class<?> cls) {
        super(parserConfig, cls, cls);
    }

    /* JADX WARNING: type inference failed for: r14v7, types: [com.alibaba.fastjson.parser.deserializer.ObjectDeserializer] */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0033, code lost:
        if (java.lang.Throwable.class.isAssignableFrom(r14) != false) goto L_0x0037;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> T deserialze(com.alibaba.fastjson.parser.DefaultJSONParser r13, java.lang.reflect.Type r14, java.lang.Object r15) {
        /*
            r12 = this;
            com.alibaba.fastjson.parser.JSONLexer r15 = r13.lexer
            int r0 = r15.token()
            r1 = 8
            r2 = 0
            if (r0 != r1) goto L_0x000f
            r15.nextToken()
            return r2
        L_0x000f:
            int r0 = r13.getResolveStatus()
            r3 = 2
            java.lang.String r4 = "syntax error"
            if (r0 != r3) goto L_0x001d
            r0 = 0
            r13.setResolveStatus(r0)
            goto L_0x0025
        L_0x001d:
            int r0 = r15.token()
            r3 = 12
            if (r0 != r3) goto L_0x0188
        L_0x0025:
            if (r14 == 0) goto L_0x0036
            boolean r0 = r14 instanceof java.lang.Class
            if (r0 == 0) goto L_0x0036
            java.lang.Class r14 = (java.lang.Class) r14
            java.lang.Class<java.lang.Throwable> r0 = java.lang.Throwable.class
            boolean r0 = r0.isAssignableFrom(r14)
            if (r0 == 0) goto L_0x0036
            goto L_0x0037
        L_0x0036:
            r14 = r2
        L_0x0037:
            r0 = r2
            r3 = r0
            r5 = r3
            r6 = r5
        L_0x003b:
            com.alibaba.fastjson.parser.SymbolTable r7 = r13.getSymbolTable()
            java.lang.String r7 = r15.scanSymbol(r7)
            r8 = 13
            r9 = 16
            if (r7 != 0) goto L_0x0063
            int r10 = r15.token()
            if (r10 != r8) goto L_0x0054
            r15.nextToken(r9)
            goto L_0x00ec
        L_0x0054:
            int r10 = r15.token()
            if (r10 != r9) goto L_0x0063
            com.alibaba.fastjson.parser.Feature r10 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas
            boolean r10 = r15.isEnabled((com.alibaba.fastjson.parser.Feature) r10)
            if (r10 == 0) goto L_0x0063
            goto L_0x003b
        L_0x0063:
            r10 = 4
            r15.nextTokenWithColon(r10)
            java.lang.String r11 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY
            boolean r11 = r11.equals(r7)
            if (r11 == 0) goto L_0x0091
            int r14 = r15.token()
            if (r14 != r10) goto L_0x008b
            java.lang.String r14 = r15.stringVal()
            com.alibaba.fastjson.parser.ParserConfig r7 = r13.getConfig()
            java.lang.Class<java.lang.Throwable> r10 = java.lang.Throwable.class
            int r11 = r15.getFeatures()
            java.lang.Class r14 = r7.checkAutoType(r14, r10, r11)
            r15.nextToken(r9)
            goto L_0x00e3
        L_0x008b:
            com.alibaba.fastjson.JSONException r13 = new com.alibaba.fastjson.JSONException
            r13.<init>(r4)
            throw r13
        L_0x0091:
            java.lang.String r11 = "message"
            boolean r11 = r11.equals(r7)
            if (r11 == 0) goto L_0x00b5
            int r5 = r15.token()
            if (r5 != r1) goto L_0x00a1
            r5 = r2
            goto L_0x00ab
        L_0x00a1:
            int r5 = r15.token()
            if (r5 != r10) goto L_0x00af
            java.lang.String r5 = r15.stringVal()
        L_0x00ab:
            r15.nextToken()
            goto L_0x00e3
        L_0x00af:
            com.alibaba.fastjson.JSONException r13 = new com.alibaba.fastjson.JSONException
            r13.<init>(r4)
            throw r13
        L_0x00b5:
            java.lang.String r10 = "cause"
            boolean r11 = r10.equals(r7)
            if (r11 == 0) goto L_0x00c4
            java.lang.Object r3 = r12.deserialze(r13, r2, r10)
            java.lang.Throwable r3 = (java.lang.Throwable) r3
            goto L_0x00e3
        L_0x00c4:
            java.lang.String r10 = "stackTrace"
            boolean r10 = r10.equals(r7)
            if (r10 == 0) goto L_0x00d5
            java.lang.Class<java.lang.StackTraceElement[]> r6 = java.lang.StackTraceElement[].class
            java.lang.Object r6 = r13.parseObject(r6)
            java.lang.StackTraceElement[] r6 = (java.lang.StackTraceElement[]) r6
            goto L_0x00e3
        L_0x00d5:
            if (r0 != 0) goto L_0x00dc
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
        L_0x00dc:
            java.lang.Object r10 = r13.parse()
            r0.put(r7, r10)
        L_0x00e3:
            int r7 = r15.token()
            if (r7 != r8) goto L_0x003b
            r15.nextToken(r9)
        L_0x00ec:
            if (r14 != 0) goto L_0x00f4
            java.lang.Exception r15 = new java.lang.Exception
            r15.<init>(r5, r3)
            goto L_0x0107
        L_0x00f4:
            java.lang.Class<java.lang.Throwable> r15 = java.lang.Throwable.class
            boolean r15 = r15.isAssignableFrom(r14)
            if (r15 == 0) goto L_0x016d
            java.lang.Throwable r15 = r12.createException(r5, r3, r14)     // Catch:{ Exception -> 0x0164 }
            if (r15 != 0) goto L_0x0107
            java.lang.Exception r15 = new java.lang.Exception     // Catch:{ Exception -> 0x0164 }
            r15.<init>(r5, r3)     // Catch:{ Exception -> 0x0164 }
        L_0x0107:
            if (r6 == 0) goto L_0x010c
            r15.setStackTrace(r6)
        L_0x010c:
            if (r0 == 0) goto L_0x0163
            if (r14 == 0) goto L_0x0125
            java.lang.Class r1 = r12.clazz
            if (r14 != r1) goto L_0x0116
            r2 = r12
            goto L_0x0125
        L_0x0116:
            com.alibaba.fastjson.parser.ParserConfig r1 = r13.getConfig()
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r14 = r1.getDeserializer((java.lang.reflect.Type) r14)
            boolean r1 = r14 instanceof com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer
            if (r1 == 0) goto L_0x0125
            r2 = r14
            com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer r2 = (com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) r2
        L_0x0125:
            if (r2 == 0) goto L_0x0163
            java.util.Set r14 = r0.entrySet()
            java.util.Iterator r14 = r14.iterator()
        L_0x012f:
            boolean r0 = r14.hasNext()
            if (r0 == 0) goto L_0x0163
            java.lang.Object r0 = r14.next()
            java.util.Map$Entry r0 = (java.util.Map.Entry) r0
            java.lang.Object r1 = r0.getKey()
            java.lang.String r1 = (java.lang.String) r1
            java.lang.Object r0 = r0.getValue()
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer r1 = r2.getFieldDeserializer((java.lang.String) r1)
            if (r1 == 0) goto L_0x012f
            com.alibaba.fastjson.util.FieldInfo r3 = r1.fieldInfo
            java.lang.Class<?> r4 = r3.fieldClass
            boolean r4 = r4.isInstance(r0)
            if (r4 != 0) goto L_0x015f
            java.lang.reflect.Type r3 = r3.fieldType
            com.alibaba.fastjson.parser.ParserConfig r4 = r13.getConfig()
            java.lang.Object r0 = com.alibaba.fastjson.util.TypeUtils.cast((java.lang.Object) r0, (java.lang.reflect.Type) r3, (com.alibaba.fastjson.parser.ParserConfig) r4)
        L_0x015f:
            r1.setValue((java.lang.Object) r15, (java.lang.Object) r0)
            goto L_0x012f
        L_0x0163:
            return r15
        L_0x0164:
            r13 = move-exception
            com.alibaba.fastjson.JSONException r14 = new com.alibaba.fastjson.JSONException
            java.lang.String r15 = "create instance error"
            r14.<init>(r15, r13)
            throw r14
        L_0x016d:
            com.alibaba.fastjson.JSONException r13 = new com.alibaba.fastjson.JSONException
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            java.lang.String r0 = "type not match, not Throwable. "
            r15.append(r0)
            java.lang.String r14 = r14.getName()
            r15.append(r14)
            java.lang.String r14 = r15.toString()
            r13.<init>(r14)
            throw r13
        L_0x0188:
            com.alibaba.fastjson.JSONException r13 = new com.alibaba.fastjson.JSONException
            r13.<init>(r4)
            goto L_0x018f
        L_0x018e:
            throw r13
        L_0x018f:
            goto L_0x018e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.ThrowableDeserializer.deserialze(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.lang.Object):java.lang.Object");
    }

    private Throwable createException(String str, Throwable th, Class<?> cls) throws Exception {
        Constructor constructor = null;
        Constructor constructor2 = null;
        Constructor constructor3 = null;
        for (Constructor constructor4 : cls.getConstructors()) {
            Class<Throwable>[] parameterTypes = constructor4.getParameterTypes();
            if (parameterTypes.length == 0) {
                constructor3 = constructor4;
            } else if (parameterTypes.length == 1 && parameterTypes[0] == String.class) {
                constructor2 = constructor4;
            } else if (parameterTypes.length == 2 && parameterTypes[0] == String.class && parameterTypes[1] == Throwable.class) {
                constructor = constructor4;
            }
        }
        if (constructor != null) {
            return (Throwable) constructor.newInstance(new Object[]{str, th});
        } else if (constructor2 != null) {
            return (Throwable) constructor2.newInstance(new Object[]{str});
        } else if (constructor3 != null) {
            return (Throwable) constructor3.newInstance(new Object[0]);
        } else {
            return null;
        }
    }
}
