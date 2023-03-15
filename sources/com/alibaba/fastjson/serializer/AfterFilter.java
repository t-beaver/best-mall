package com.alibaba.fastjson.serializer;

import com.taobao.weex.el.parse.Operators;

public abstract class AfterFilter implements SerializeFilter {
    private static final Character COMMA = Character.valueOf(Operators.ARRAY_SEPRATOR);
    private static final ThreadLocal<Character> seperatorLocal = new ThreadLocal<>();
    private static final ThreadLocal<JSONSerializer> serializerLocal = new ThreadLocal<>();

    public abstract void writeAfter(Object obj);

    /* access modifiers changed from: package-private */
    public final char writeAfter(JSONSerializer jSONSerializer, Object obj, char c) {
        ThreadLocal<JSONSerializer> threadLocal = serializerLocal;
        threadLocal.set(jSONSerializer);
        ThreadLocal<Character> threadLocal2 = seperatorLocal;
        threadLocal2.set(Character.valueOf(c));
        writeAfter(obj);
        threadLocal.set(threadLocal.get());
        return threadLocal2.get().charValue();
    }

    /* access modifiers changed from: protected */
    public final void writeKeyValue(String str, Object obj) {
        JSONSerializer jSONSerializer = serializerLocal.get();
        ThreadLocal<Character> threadLocal = seperatorLocal;
        char charValue = threadLocal.get().charValue();
        boolean containsReference = jSONSerializer.containsReference(obj);
        jSONSerializer.writeKeyValue(charValue, str, obj);
        if (!containsReference && jSONSerializer.references != null) {
            jSONSerializer.references.remove(obj);
        }
        if (charValue != ',') {
            threadLocal.set(COMMA);
        }
    }
}
