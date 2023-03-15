package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.taobao.weex.el.parse.Operators;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;

public class ByteBufferCodec implements ObjectSerializer, ObjectDeserializer {
    public static final ByteBufferCodec instance = new ByteBufferCodec();

    public int getFastMatchToken() {
        return 14;
    }

    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        return ((ByteBufferBean) defaultJSONParser.parseObject(ByteBufferBean.class)).byteBuffer();
    }

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        ByteBuffer byteBuffer = (ByteBuffer) obj;
        byte[] array = byteBuffer.array();
        SerializeWriter serializeWriter = jSONSerializer.out;
        serializeWriter.write(123);
        serializeWriter.writeFieldName("array");
        serializeWriter.writeByteArray(array);
        serializeWriter.writeFieldValue((char) Operators.ARRAY_SEPRATOR, "limit", byteBuffer.limit());
        serializeWriter.writeFieldValue((char) Operators.ARRAY_SEPRATOR, "position", byteBuffer.position());
        serializeWriter.write(125);
    }

    public static class ByteBufferBean {
        public byte[] array;
        public int limit;
        public int position;

        public ByteBuffer byteBuffer() {
            ByteBuffer wrap = ByteBuffer.wrap(this.array);
            wrap.limit(this.limit);
            wrap.position(this.position);
            return wrap;
        }
    }
}
