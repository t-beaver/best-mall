package com.alibaba.fastjson.serializer;

import com.taobao.weex.el.parse.Operators;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

public class AdderSerializer implements ObjectSerializer {
    public static final AdderSerializer instance = new AdderSerializer();

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        SerializeWriter serializeWriter = jSONSerializer.out;
        if (obj instanceof LongAdder) {
            serializeWriter.writeFieldValue((char) Operators.BLOCK_START, "value", ((LongAdder) obj).longValue());
            serializeWriter.write(125);
        } else if (obj instanceof DoubleAdder) {
            serializeWriter.writeFieldValue((char) Operators.BLOCK_START, "value", ((DoubleAdder) obj).doubleValue());
            serializeWriter.write(125);
        }
    }
}
