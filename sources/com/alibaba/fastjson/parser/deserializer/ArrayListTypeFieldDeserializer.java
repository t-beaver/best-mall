package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Map;

public class ArrayListTypeFieldDeserializer extends FieldDeserializer {
    private ObjectDeserializer deserializer;
    private int itemFastMatchToken;
    private final Type itemType;

    public int getFastMatchToken() {
        return 14;
    }

    public ArrayListTypeFieldDeserializer(ParserConfig parserConfig, Class<?> cls, FieldInfo fieldInfo) {
        super(cls, fieldInfo);
        if (fieldInfo.fieldType instanceof ParameterizedType) {
            Type type = ((ParameterizedType) fieldInfo.fieldType).getActualTypeArguments()[0];
            if (type instanceof WildcardType) {
                Type[] upperBounds = ((WildcardType) type).getUpperBounds();
                if (upperBounds.length == 1) {
                    type = upperBounds[0];
                }
            }
            this.itemType = type;
            return;
        }
        this.itemType = Object.class;
    }

    public void parseField(DefaultJSONParser defaultJSONParser, Object obj, Type type, Map<String, Object> map) {
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        int i = jSONLexer.token();
        if (i != 8 && (i != 4 || jSONLexer.stringVal().length() != 0)) {
            ArrayList arrayList = new ArrayList();
            ParseContext context = defaultJSONParser.getContext();
            defaultJSONParser.setContext(context, obj, this.fieldInfo.name);
            parseArray(defaultJSONParser, type, arrayList);
            defaultJSONParser.setContext(context);
            if (obj == null) {
                map.put(this.fieldInfo.name, arrayList);
            } else {
                setValue(obj, (Object) arrayList);
            }
        } else if (obj == null) {
            map.put(this.fieldInfo.name, (Object) null);
        } else {
            setValue(obj, (String) null);
        }
    }

    /* JADX WARNING: type inference failed for: r3v12, types: [java.lang.reflect.Type] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00b0  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void parseArray(com.alibaba.fastjson.parser.DefaultJSONParser r13, java.lang.reflect.Type r14, java.util.Collection r15) {
        /*
            r12 = this;
            java.lang.reflect.Type r0 = r12.itemType
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r1 = r12.deserializer
            boolean r2 = r14 instanceof java.lang.reflect.ParameterizedType
            r3 = 1
            r4 = 0
            if (r2 == 0) goto L_0x00ca
            boolean r2 = r0 instanceof java.lang.reflect.TypeVariable
            r5 = 0
            r6 = -1
            if (r2 == 0) goto L_0x0063
            r2 = r0
            java.lang.reflect.TypeVariable r2 = (java.lang.reflect.TypeVariable) r2
            java.lang.reflect.ParameterizedType r14 = (java.lang.reflect.ParameterizedType) r14
            java.lang.reflect.Type r3 = r14.getRawType()
            boolean r3 = r3 instanceof java.lang.Class
            if (r3 == 0) goto L_0x0024
            java.lang.reflect.Type r3 = r14.getRawType()
            r5 = r3
            java.lang.Class r5 = (java.lang.Class) r5
        L_0x0024:
            if (r5 == 0) goto L_0x0046
            java.lang.reflect.TypeVariable[] r3 = r5.getTypeParameters()
            int r3 = r3.length
            r7 = 0
        L_0x002c:
            if (r7 >= r3) goto L_0x0046
            java.lang.reflect.TypeVariable[] r8 = r5.getTypeParameters()
            r8 = r8[r7]
            java.lang.String r8 = r8.getName()
            java.lang.String r9 = r2.getName()
            boolean r8 = r8.equals(r9)
            if (r8 == 0) goto L_0x0043
            goto L_0x0047
        L_0x0043:
            int r7 = r7 + 1
            goto L_0x002c
        L_0x0046:
            r7 = -1
        L_0x0047:
            if (r7 == r6) goto L_0x0103
            java.lang.reflect.Type[] r14 = r14.getActualTypeArguments()
            r14 = r14[r7]
            java.lang.reflect.Type r0 = r12.itemType
            boolean r0 = r14.equals(r0)
            if (r0 != 0) goto L_0x0060
            com.alibaba.fastjson.parser.ParserConfig r0 = r13.getConfig()
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r0 = r0.getDeserializer((java.lang.reflect.Type) r14)
            r1 = r0
        L_0x0060:
            r0 = r14
            goto L_0x0103
        L_0x0063:
            boolean r2 = r0 instanceof java.lang.reflect.ParameterizedType
            if (r2 == 0) goto L_0x0103
            r2 = r0
            java.lang.reflect.ParameterizedType r2 = (java.lang.reflect.ParameterizedType) r2
            java.lang.reflect.Type[] r7 = r2.getActualTypeArguments()
            int r8 = r7.length
            if (r8 != r3) goto L_0x0103
            r3 = r7[r4]
            boolean r3 = r3 instanceof java.lang.reflect.TypeVariable
            if (r3 == 0) goto L_0x0103
            r3 = r7[r4]
            java.lang.reflect.TypeVariable r3 = (java.lang.reflect.TypeVariable) r3
            java.lang.reflect.ParameterizedType r14 = (java.lang.reflect.ParameterizedType) r14
            java.lang.reflect.Type r8 = r14.getRawType()
            boolean r8 = r8 instanceof java.lang.Class
            if (r8 == 0) goto L_0x008b
            java.lang.reflect.Type r5 = r14.getRawType()
            java.lang.Class r5 = (java.lang.Class) r5
        L_0x008b:
            if (r5 == 0) goto L_0x00ad
            java.lang.reflect.TypeVariable[] r8 = r5.getTypeParameters()
            int r8 = r8.length
            r9 = 0
        L_0x0093:
            if (r9 >= r8) goto L_0x00ad
            java.lang.reflect.TypeVariable[] r10 = r5.getTypeParameters()
            r10 = r10[r9]
            java.lang.String r10 = r10.getName()
            java.lang.String r11 = r3.getName()
            boolean r10 = r10.equals(r11)
            if (r10 == 0) goto L_0x00aa
            goto L_0x00ae
        L_0x00aa:
            int r9 = r9 + 1
            goto L_0x0093
        L_0x00ad:
            r9 = -1
        L_0x00ae:
            if (r9 == r6) goto L_0x0103
            java.lang.reflect.Type[] r14 = r14.getActualTypeArguments()
            r14 = r14[r9]
            r7[r4] = r14
            com.alibaba.fastjson.util.ParameterizedTypeImpl r14 = new com.alibaba.fastjson.util.ParameterizedTypeImpl
            java.lang.reflect.Type r0 = r2.getOwnerType()
            java.lang.reflect.Type r2 = r2.getRawType()
            r14.<init>(r7, r0, r2)
            java.lang.reflect.Type r14 = com.alibaba.fastjson.TypeReference.intern(r14)
            goto L_0x0060
        L_0x00ca:
            boolean r2 = r0 instanceof java.lang.reflect.TypeVariable
            if (r2 == 0) goto L_0x0103
            boolean r2 = r14 instanceof java.lang.Class
            if (r2 == 0) goto L_0x0103
            java.lang.Class r14 = (java.lang.Class) r14
            r2 = r0
            java.lang.reflect.TypeVariable r2 = (java.lang.reflect.TypeVariable) r2
            r14.getTypeParameters()
            java.lang.reflect.TypeVariable[] r5 = r14.getTypeParameters()
            int r5 = r5.length
            r6 = 0
        L_0x00e0:
            if (r6 >= r5) goto L_0x0103
            java.lang.reflect.TypeVariable[] r7 = r14.getTypeParameters()
            r7 = r7[r6]
            java.lang.String r8 = r7.getName()
            java.lang.String r9 = r2.getName()
            boolean r8 = r8.equals(r9)
            if (r8 == 0) goto L_0x0100
            java.lang.reflect.Type[] r14 = r7.getBounds()
            int r2 = r14.length
            if (r2 != r3) goto L_0x0103
            r0 = r14[r4]
            goto L_0x0103
        L_0x0100:
            int r6 = r6 + 1
            goto L_0x00e0
        L_0x0103:
            com.alibaba.fastjson.parser.JSONLexer r14 = r13.lexer
            int r2 = r14.token()
            r3 = 14
            if (r2 != r3) goto L_0x0161
            if (r1 != 0) goto L_0x011f
            com.alibaba.fastjson.parser.ParserConfig r1 = r13.getConfig()
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r1 = r1.getDeserializer((java.lang.reflect.Type) r0)
            r12.deserializer = r1
            int r2 = r1.getFastMatchToken()
            r12.itemFastMatchToken = r2
        L_0x011f:
            r3 = r1
            int r1 = r12.itemFastMatchToken
            r14.nextToken(r1)
        L_0x0125:
            com.alibaba.fastjson.parser.Feature r1 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas
            boolean r1 = r14.isEnabled((com.alibaba.fastjson.parser.Feature) r1)
            r2 = 16
            if (r1 == 0) goto L_0x0139
        L_0x012f:
            int r1 = r14.token()
            if (r1 != r2) goto L_0x0139
            r14.nextToken()
            goto L_0x012f
        L_0x0139:
            int r1 = r14.token()
            r5 = 15
            if (r1 != r5) goto L_0x0145
            r14.nextToken(r2)
            goto L_0x0194
        L_0x0145:
            java.lang.Integer r1 = java.lang.Integer.valueOf(r4)
            java.lang.Object r1 = r3.deserialze(r13, r0, r1)
            r15.add(r1)
            r13.checkListResolve(r15)
            int r1 = r14.token()
            if (r1 != r2) goto L_0x015e
            int r1 = r12.itemFastMatchToken
            r14.nextToken(r1)
        L_0x015e:
            int r4 = r4 + 1
            goto L_0x0125
        L_0x0161:
            r3 = 4
            if (r2 != r3) goto L_0x017a
            com.alibaba.fastjson.util.FieldInfo r2 = r12.fieldInfo
            boolean r2 = r2.unwrapped
            if (r2 == 0) goto L_0x017a
            java.lang.String r13 = r14.stringVal()
            r14.nextToken()
            com.alibaba.fastjson.parser.DefaultJSONParser r14 = new com.alibaba.fastjson.parser.DefaultJSONParser
            r14.<init>((java.lang.String) r13)
            r14.parseArray((java.util.Collection) r15)
            goto L_0x0194
        L_0x017a:
            if (r1 != 0) goto L_0x0186
            com.alibaba.fastjson.parser.ParserConfig r14 = r13.getConfig()
            com.alibaba.fastjson.parser.deserializer.ObjectDeserializer r1 = r14.getDeserializer((java.lang.reflect.Type) r0)
            r12.deserializer = r1
        L_0x0186:
            java.lang.Integer r14 = java.lang.Integer.valueOf(r4)
            java.lang.Object r14 = r1.deserialze(r13, r0, r14)
            r15.add(r14)
            r13.checkListResolve(r15)
        L_0x0194:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.ArrayListTypeFieldDeserializer.parseArray(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.util.Collection):void");
    }
}
