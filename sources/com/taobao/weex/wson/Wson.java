package com.taobao.weex.wson;

import androidx.collection.LruCache;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.utils.WXLogUtils;
import io.dcloud.common.DHInterface.IApp;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Wson {
    private static final byte ARRAY_TYPE = 91;
    private static final byte BOOLEAN_TYPE_FALSE = 102;
    private static final byte BOOLEAN_TYPE_TRUE = 116;
    private static final int GLOBAL_STRING_CACHE_SIZE = 2048;
    /* access modifiers changed from: private */
    public static final boolean IS_NATIVE_LITTLE_ENDIAN = (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN);
    private static final byte MAP_TYPE = 123;
    private static final String METHOD_PREFIX_GET = "get";
    private static final String METHOD_PREFIX_IS = "is";
    private static final byte NULL_TYPE = 48;
    private static final byte NUMBER_BIG_DECIMAL_TYPE = 101;
    private static final byte NUMBER_BIG_INTEGER_TYPE = 103;
    private static final byte NUMBER_DOUBLE_TYPE = 100;
    private static final byte NUMBER_FLOAT_TYPE = 70;
    private static final byte NUMBER_INT_TYPE = 105;
    private static final byte NUMBER_LONG_TYPE = 108;
    private static final byte STRING_TYPE = 115;
    public static final boolean WriteMapNullValue = false;
    private static LruCache<String, List<Field>> fieldsCache = new LruCache<>(128);
    /* access modifiers changed from: private */
    public static final String[] globalStringBytesCache = new String[2048];
    /* access modifiers changed from: private */
    public static final ThreadLocal<char[]> localCharsBufferCache = new ThreadLocal<>();
    private static LruCache<String, List<Method>> methodsCache = new LruCache<>(128);
    /* access modifiers changed from: private */
    public static LruCache<String, Boolean> specialClass = new LruCache<>(16);

    public static final Object parse(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        try {
            Parser parser = new Parser(bArr);
            Object access$100 = parser.parse();
            parser.close();
            return access$100;
        } catch (Exception e) {
            WXLogUtils.e("parseWson", (Throwable) e);
            return null;
        }
    }

    public static final byte[] toWson(Object obj) {
        if (obj == null) {
            return null;
        }
        Builder builder = new Builder();
        byte[] access$400 = builder.toWson(obj);
        builder.close();
        return access$400;
    }

    private static final class Parser {
        private byte[] buffer;
        private char[] charsBuffer;
        private int position;

        private Parser(byte[] bArr) {
            this.position = 0;
            this.buffer = bArr;
            char[] cArr = (char[]) Wson.localCharsBufferCache.get();
            this.charsBuffer = cArr;
            if (cArr != null) {
                Wson.localCharsBufferCache.set((Object) null);
            } else {
                this.charsBuffer = new char[512];
            }
        }

        /* access modifiers changed from: private */
        public final Object parse() {
            return readObject();
        }

        /* access modifiers changed from: private */
        public final void close() {
            this.position = 0;
            this.buffer = null;
            if (this.charsBuffer != null) {
                Wson.localCharsBufferCache.set(this.charsBuffer);
            }
            this.charsBuffer = null;
        }

        private final Object readObject() {
            byte readType = readType();
            if (readType == 48) {
                return null;
            }
            if (readType == 70) {
                return readFloat();
            }
            if (readType == 91) {
                return readArray();
            }
            if (readType == 105) {
                return Integer.valueOf(readVarInt());
            }
            if (readType == 108) {
                return Long.valueOf(readLong());
            }
            if (readType == 123) {
                return readMap();
            }
            if (readType == 115) {
                return readUTF16String();
            }
            if (readType == 116) {
                return Boolean.TRUE;
            }
            switch (readType) {
                case 100:
                    return readDouble();
                case 101:
                    return new BigDecimal(readUTF16String());
                case 102:
                    return Boolean.FALSE;
                case 103:
                    return new BigInteger(readUTF16String());
                default:
                    throw new RuntimeException("wson unhandled type " + readType + Operators.SPACE_STR + this.position + " length " + this.buffer.length);
            }
        }

        private final Object readMap() {
            int readUInt = readUInt();
            JSONObject jSONObject = new JSONObject();
            for (int i = 0; i < readUInt; i++) {
                jSONObject.put(readMapKeyUTF16(), readObject());
            }
            return jSONObject;
        }

        private final Object readArray() {
            int readUInt = readUInt();
            JSONArray jSONArray = new JSONArray(readUInt);
            for (int i = 0; i < readUInt; i++) {
                jSONArray.add(readObject());
            }
            return jSONArray;
        }

        private final byte readType() {
            byte[] bArr = this.buffer;
            int i = this.position;
            byte b = bArr[i];
            this.position = i + 1;
            return b;
        }

        private final String readMapKeyUTF16() {
            int readUInt = readUInt() / 2;
            if (this.charsBuffer.length < readUInt) {
                this.charsBuffer = new char[readUInt];
            }
            int i = 5381;
            if (Wson.IS_NATIVE_LITTLE_ENDIAN) {
                for (int i2 = 0; i2 < readUInt; i2++) {
                    byte[] bArr = this.buffer;
                    int i3 = this.position;
                    char c = (char) ((bArr[i3] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) + (bArr[i3 + 1] << 8));
                    this.charsBuffer[i2] = c;
                    i = (i << 5) + i + c;
                    this.position = i3 + 2;
                }
            } else {
                for (int i4 = 0; i4 < readUInt; i4++) {
                    byte[] bArr2 = this.buffer;
                    int i5 = this.position;
                    char c2 = (char) ((bArr2[i5 + 1] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) + (bArr2[i5] << 8));
                    this.charsBuffer[i4] = c2;
                    i = (i << 5) + i + c2;
                    this.position = i5 + 2;
                }
            }
            boolean z = true;
            int length = i & (Wson.globalStringBytesCache.length - 1);
            String str = Wson.globalStringBytesCache[length];
            if (str != null && str.length() == readUInt) {
                int i6 = 0;
                while (true) {
                    if (i6 >= readUInt) {
                        break;
                    } else if (this.charsBuffer[i6] != str.charAt(i6)) {
                        z = false;
                        break;
                    } else {
                        i6++;
                    }
                }
                if (z) {
                    return str;
                }
            }
            String str2 = new String(this.charsBuffer, 0, readUInt);
            if (readUInt < 64) {
                Wson.globalStringBytesCache[length] = str2;
            }
            return str2;
        }

        private final String readUTF16String() {
            int readUInt = readUInt() / 2;
            if (this.charsBuffer.length < readUInt) {
                this.charsBuffer = new char[readUInt];
            }
            if (Wson.IS_NATIVE_LITTLE_ENDIAN) {
                for (int i = 0; i < readUInt; i++) {
                    byte[] bArr = this.buffer;
                    int i2 = this.position;
                    this.charsBuffer[i] = (char) ((bArr[i2] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) + (bArr[i2 + 1] << 8));
                    this.position = i2 + 2;
                }
            } else {
                for (int i3 = 0; i3 < readUInt; i3++) {
                    byte[] bArr2 = this.buffer;
                    int i4 = this.position;
                    this.charsBuffer[i3] = (char) ((bArr2[i4 + 1] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) + (bArr2[i4] << 8));
                    this.position = i4 + 2;
                }
            }
            return new String(this.charsBuffer, 0, readUInt);
        }

        private final int readVarInt() {
            int readUInt = readUInt();
            return (readUInt & Integer.MIN_VALUE) ^ ((((readUInt << 31) >> 31) ^ readUInt) >> 1);
        }

        private final int readUInt() {
            int i = 0;
            int i2 = 0;
            do {
                byte[] bArr = this.buffer;
                int i3 = this.position;
                byte b = bArr[i3];
                if ((b & 128) != 0) {
                    i |= (b & Byte.MAX_VALUE) << i2;
                    i2 += 7;
                    this.position = i3 + 1;
                } else {
                    this.position = i3 + 1;
                    return i | (b << i2);
                }
            } while (i2 <= 35);
            throw new IllegalArgumentException("Variable length quantity is too long");
        }

        private final long readLong() {
            byte[] bArr = this.buffer;
            int i = this.position;
            long j = (((long) bArr[i + 7]) & 255) + ((((long) bArr[i + 6]) & 255) << 8) + ((((long) bArr[i + 5]) & 255) << 16) + ((((long) bArr[i + 4]) & 255) << 24) + ((((long) bArr[i + 3]) & 255) << 32) + ((((long) bArr[i + 2]) & 255) << 40) + ((255 & ((long) bArr[i + 1])) << 48) + (((long) bArr[i]) << 56);
            this.position = i + 8;
            return j;
        }

        private final Object readDouble() {
            double longBitsToDouble = Double.longBitsToDouble(readLong());
            if (longBitsToDouble > 2.147483647E9d) {
                long j = (long) longBitsToDouble;
                double d = (double) j;
                Double.isNaN(d);
                if (longBitsToDouble - d < Double.MIN_NORMAL) {
                    return Long.valueOf(j);
                }
            }
            return Double.valueOf(longBitsToDouble);
        }

        private Object readFloat() {
            byte[] bArr = this.buffer;
            int i = this.position;
            this.position = i + 4;
            return Float.valueOf(Float.intBitsToFloat((bArr[i + 3] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) + ((bArr[i + 2] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) << 8) + ((bArr[i + 1] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) << 16) + ((bArr[i] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) << 24)));
        }
    }

    private static final class Builder {
        private static final ThreadLocal<byte[]> bufLocal = new ThreadLocal<>();
        private static final ThreadLocal<ArrayList> refsLocal = new ThreadLocal<>();
        private byte[] buffer;
        private int position;
        private ArrayList refs;

        private Builder() {
            ThreadLocal<byte[]> threadLocal = bufLocal;
            byte[] bArr = threadLocal.get();
            this.buffer = bArr;
            if (bArr != null) {
                threadLocal.set((Object) null);
            } else {
                this.buffer = new byte[1024];
            }
            ThreadLocal<ArrayList> threadLocal2 = refsLocal;
            ArrayList arrayList = threadLocal2.get();
            this.refs = arrayList;
            if (arrayList != null) {
                threadLocal2.set((Object) null);
            } else {
                this.refs = new ArrayList(16);
            }
        }

        /* access modifiers changed from: private */
        public final byte[] toWson(Object obj) {
            writeObject(obj);
            int i = this.position;
            byte[] bArr = new byte[i];
            System.arraycopy(this.buffer, 0, bArr, 0, i);
            return bArr;
        }

        /* access modifiers changed from: private */
        public final void close() {
            byte[] bArr = this.buffer;
            if (bArr.length <= 16384) {
                bufLocal.set(bArr);
            }
            if (this.refs.isEmpty()) {
                refsLocal.set(this.refs);
            } else {
                this.refs.clear();
            }
            this.refs = null;
            this.buffer = null;
            this.position = 0;
        }

        private final void writeObject(Object obj) {
            if (obj instanceof CharSequence) {
                ensureCapacity(2);
                writeByte(Wson.STRING_TYPE);
                writeUTF16String((CharSequence) obj);
            } else if (obj instanceof Map) {
                if (this.refs.contains(obj)) {
                    ensureCapacity(2);
                    writeByte(Wson.NULL_TYPE);
                    return;
                }
                this.refs.add(obj);
                writeMap((Map) obj);
                ArrayList arrayList = this.refs;
                arrayList.remove(arrayList.size() - 1);
            } else if (obj instanceof List) {
                if (this.refs.contains(obj)) {
                    ensureCapacity(2);
                    writeByte(Wson.NULL_TYPE);
                    return;
                }
                this.refs.add(obj);
                ensureCapacity(8);
                List<Object> list = (List) obj;
                writeByte(Wson.ARRAY_TYPE);
                writeUInt(list.size());
                for (Object writeObject : list) {
                    writeObject(writeObject);
                }
                ArrayList arrayList2 = this.refs;
                arrayList2.remove(arrayList2.size() - 1);
            } else if (obj instanceof Number) {
                writeNumber((Number) obj);
            } else if (obj instanceof Boolean) {
                ensureCapacity(2);
                if (((Boolean) obj).booleanValue()) {
                    writeByte(Wson.BOOLEAN_TYPE_TRUE);
                } else {
                    writeByte(Wson.BOOLEAN_TYPE_FALSE);
                }
            } else if (obj == null) {
                ensureCapacity(2);
                writeByte(Wson.NULL_TYPE);
            } else if (obj.getClass().isArray()) {
                if (this.refs.contains(obj)) {
                    ensureCapacity(2);
                    writeByte(Wson.NULL_TYPE);
                    return;
                }
                this.refs.add(obj);
                ensureCapacity(8);
                int length = Array.getLength(obj);
                writeByte(Wson.ARRAY_TYPE);
                writeUInt(length);
                for (int i = 0; i < length; i++) {
                    writeObject(Array.get(obj, i));
                }
                ArrayList arrayList3 = this.refs;
                arrayList3.remove(arrayList3.size() - 1);
            } else if (obj instanceof Date) {
                ensureCapacity(10);
                writeByte(Wson.NUMBER_DOUBLE_TYPE);
                writeDouble((double) ((Date) obj).getTime());
            } else if (obj instanceof Calendar) {
                ensureCapacity(10);
                writeByte(Wson.NUMBER_DOUBLE_TYPE);
                writeDouble((double) ((Calendar) obj).getTime().getTime());
            } else if (obj instanceof Collection) {
                if (this.refs.contains(obj)) {
                    ensureCapacity(2);
                    writeByte(Wson.NULL_TYPE);
                    return;
                }
                this.refs.add(obj);
                ensureCapacity(8);
                Collection<Object> collection = (Collection) obj;
                writeByte(Wson.ARRAY_TYPE);
                writeUInt(collection.size());
                for (Object writeObject2 : collection) {
                    writeObject(writeObject2);
                }
                ArrayList arrayList4 = this.refs;
                arrayList4.remove(arrayList4.size() - 1);
            } else if (this.refs.contains(obj)) {
                ensureCapacity(2);
                writeByte(Wson.NULL_TYPE);
            } else {
                this.refs.add(obj);
                if (obj.getClass().isEnum()) {
                    writeObject(JSON.toJSONString(obj));
                } else {
                    writeAdapterObject(obj);
                }
                ArrayList arrayList5 = this.refs;
                arrayList5.remove(arrayList5.size() - 1);
            }
        }

        private final void writeNumber(Number number) {
            ensureCapacity(12);
            if (number instanceof Integer) {
                writeByte(Wson.NUMBER_INT_TYPE);
                writeVarInt(number.intValue());
            } else if (number instanceof Float) {
                writeByte(Wson.NUMBER_FLOAT_TYPE);
                writeFloat(number.floatValue());
            } else if (number instanceof Double) {
                writeByte(Wson.NUMBER_DOUBLE_TYPE);
                writeDouble(number.doubleValue());
            } else if (number instanceof Long) {
                writeByte(Wson.NUMBER_LONG_TYPE);
                writeLong(number.longValue());
            } else if ((number instanceof Short) || (number instanceof Byte)) {
                writeByte(Wson.NUMBER_INT_TYPE);
                writeVarInt(number.intValue());
            } else if (number instanceof BigInteger) {
                writeByte(Wson.NUMBER_BIG_INTEGER_TYPE);
                writeUTF16String(number.toString());
            } else if (number instanceof BigDecimal) {
                String obj = number.toString();
                double doubleValue = number.doubleValue();
                if (obj.equals(Double.toString(doubleValue))) {
                    writeByte(Wson.NUMBER_DOUBLE_TYPE);
                    writeDouble(doubleValue);
                    return;
                }
                writeByte(Wson.NUMBER_BIG_DECIMAL_TYPE);
                writeUTF16String(obj);
            } else {
                writeByte(Wson.STRING_TYPE);
                writeUTF16String(number.toString());
            }
        }

        private final void writeMap(Map map) {
            Set<Map.Entry> entrySet = map.entrySet();
            int i = 0;
            for (Map.Entry value : entrySet) {
                if (value.getValue() == null) {
                    i++;
                }
            }
            ensureCapacity(8);
            writeByte(Wson.MAP_TYPE);
            writeUInt(map.size() - i);
            for (Map.Entry entry : entrySet) {
                if (entry.getValue() != null) {
                    writeMapKeyUTF16(entry.getKey().toString());
                    writeObject(entry.getValue());
                }
            }
        }

        private final void writeByte(byte b) {
            byte[] bArr = this.buffer;
            int i = this.position;
            bArr[i] = b;
            this.position = i + 1;
        }

        private final void writeAdapterObject(Object obj) {
            if (Wson.specialClass.get(obj.getClass().getName()) != null) {
                writeObject(JSON.toJSON(obj));
                return;
            }
            try {
                writeMap(toMap(obj));
            } catch (Exception unused) {
                Wson.specialClass.put(obj.getClass().getName(), true);
                writeObject(JSON.toJSON(obj));
            }
        }

        private final Map toMap(Object obj) {
            Object invoke;
            JSONObject jSONObject = new JSONObject();
            try {
                Class<?> cls = obj.getClass();
                String name = cls.getName();
                for (Method method : Wson.getBeanMethod(name, cls)) {
                    String name2 = method.getName();
                    if (name2.startsWith(Wson.METHOD_PREFIX_GET)) {
                        Object invoke2 = method.invoke(obj, new Object[0]);
                        if (invoke2 != null) {
                            StringBuilder sb = new StringBuilder(method.getName().substring(3));
                            sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
                            jSONObject.put(sb.toString(), invoke2);
                        }
                    } else if (name2.startsWith(Wson.METHOD_PREFIX_IS) && (invoke = method.invoke(obj, new Object[0])) != null) {
                        StringBuilder sb2 = new StringBuilder(method.getName().substring(2));
                        sb2.setCharAt(0, Character.toLowerCase(sb2.charAt(0)));
                        jSONObject.put(sb2.toString(), invoke);
                    }
                }
                for (Field field : Wson.getBeanFields(name, cls)) {
                    String name3 = field.getName();
                    if (!jSONObject.containsKey(name3)) {
                        Object obj2 = field.get(obj);
                        if (obj2 != null) {
                            jSONObject.put(name3, obj2);
                        }
                    }
                }
                return jSONObject;
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw ((RuntimeException) e);
                }
                throw new RuntimeException(e);
            }
        }

        private final void writeMapKeyUTF16(String str) {
            writeUTF16String(str);
        }

        private final void writeUTF16String(CharSequence charSequence) {
            int length = charSequence.length();
            int i = length * 2;
            ensureCapacity(i + 8);
            writeUInt(i);
            int i2 = 0;
            if (Wson.IS_NATIVE_LITTLE_ENDIAN) {
                while (i2 < length) {
                    char charAt = charSequence.charAt(i2);
                    byte[] bArr = this.buffer;
                    int i3 = this.position;
                    bArr[i3] = (byte) charAt;
                    bArr[i3 + 1] = (byte) (charAt >>> 8);
                    this.position = i3 + 2;
                    i2++;
                }
                return;
            }
            while (i2 < length) {
                char charAt2 = charSequence.charAt(i2);
                byte[] bArr2 = this.buffer;
                int i4 = this.position;
                bArr2[i4 + 1] = (byte) charAt2;
                bArr2[i4] = (byte) (charAt2 >>> 8);
                this.position = i4 + 2;
                i2++;
            }
        }

        private final void writeDouble(double d) {
            writeLong(Double.doubleToLongBits(d));
        }

        private final void writeFloat(float f) {
            int floatToIntBits = Float.floatToIntBits(f);
            byte[] bArr = this.buffer;
            int i = this.position;
            bArr[i + 3] = (byte) floatToIntBits;
            bArr[i + 2] = (byte) (floatToIntBits >>> 8);
            bArr[i + 1] = (byte) (floatToIntBits >>> 16);
            bArr[i] = (byte) (floatToIntBits >>> 24);
            this.position = i + 4;
        }

        private final void writeLong(long j) {
            byte[] bArr = this.buffer;
            int i = this.position;
            bArr[i + 7] = (byte) ((int) j);
            bArr[i + 6] = (byte) ((int) (j >>> 8));
            bArr[i + 5] = (byte) ((int) (j >>> 16));
            bArr[i + 4] = (byte) ((int) (j >>> 24));
            bArr[i + 3] = (byte) ((int) (j >>> 32));
            bArr[i + 2] = (byte) ((int) (j >>> 40));
            bArr[i + 1] = (byte) ((int) (j >>> 48));
            bArr[i] = (byte) ((int) (j >>> 56));
            this.position = i + 8;
        }

        private final void writeVarInt(int i) {
            writeUInt((i >> 31) ^ (i << 1));
        }

        private final void writeUInt(int i) {
            while ((i & -128) != 0) {
                byte[] bArr = this.buffer;
                int i2 = this.position;
                bArr[i2] = (byte) ((i & 127) | 128);
                this.position = i2 + 1;
                i >>>= 7;
            }
            byte[] bArr2 = this.buffer;
            int i3 = this.position;
            bArr2[i3] = (byte) (i & 127);
            this.position = i3 + 1;
        }

        private final void ensureCapacity(int i) {
            int i2 = i + this.position;
            byte[] bArr = this.buffer;
            if (i2 - bArr.length > 0) {
                int length = bArr.length << 1;
                if (length < 16384) {
                    length = 16384;
                }
                if (length - i2 >= 0) {
                    i2 = length;
                }
                this.buffer = Arrays.copyOf(bArr, i2);
            }
        }
    }

    /* access modifiers changed from: private */
    public static final List<Method> getBeanMethod(String str, Class cls) {
        List<Method> list = methodsCache.get(str);
        if (list == null) {
            list = new ArrayList<>();
            for (Method method : cls.getMethods()) {
                if (method.getDeclaringClass() != Object.class && (method.getModifiers() & 8) == 0) {
                    String name = method.getName();
                    if (name.startsWith(METHOD_PREFIX_GET) || name.startsWith(METHOD_PREFIX_IS)) {
                        if (method.getAnnotation(JSONField.class) == null) {
                            list.add(method);
                        } else {
                            throw new UnsupportedOperationException("getBeanMethod JSONField Annotation Not Handled, Use toJSON");
                        }
                    }
                }
            }
            methodsCache.put(str, list);
        }
        return list;
    }

    /* access modifiers changed from: private */
    public static final List<Field> getBeanFields(String str, Class cls) {
        List<Field> list = fieldsCache.get(str);
        if (list == null) {
            Field[] fields = cls.getFields();
            list = new ArrayList<>(fields.length);
            for (Field field : fields) {
                if ((field.getModifiers() & 8) == 0) {
                    if (field.getAnnotation(JSONField.class) == null) {
                        list.add(field);
                    } else {
                        throw new UnsupportedOperationException("getBeanMethod JSONField Annotation Not Handled, Use toJSON");
                    }
                }
            }
            fieldsCache.put(str, list);
        }
        return list;
    }
}
