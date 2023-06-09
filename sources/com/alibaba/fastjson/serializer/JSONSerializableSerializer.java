package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class JSONSerializableSerializer implements ObjectSerializer {
    public static JSONSerializableSerializer instance = new JSONSerializableSerializer();

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        JSONSerializable jSONSerializable = (JSONSerializable) obj;
        if (jSONSerializable == null) {
            jSONSerializer.writeNull();
        } else {
            jSONSerializable.write(jSONSerializer, obj2, type, i);
        }
    }
}
