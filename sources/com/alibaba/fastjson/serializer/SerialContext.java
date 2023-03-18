package com.alibaba.fastjson.serializer;

import com.taobao.weex.el.parse.Operators;

public class SerialContext {
    public final int features;
    public final Object fieldName;
    public final Object object;
    public final SerialContext parent;

    public SerialContext(SerialContext serialContext, Object obj, Object obj2, int i, int i2) {
        this.parent = serialContext;
        this.object = obj;
        this.fieldName = obj2;
        this.features = i;
    }

    public String toString() {
        if (this.parent == null) {
            return Operators.DOLLAR_STR;
        }
        StringBuilder sb = new StringBuilder();
        toString(sb);
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public void toString(StringBuilder sb) {
        boolean z;
        SerialContext serialContext = this.parent;
        if (serialContext == null) {
            sb.append(Operators.DOLLAR);
            return;
        }
        serialContext.toString(sb);
        Object obj = this.fieldName;
        if (obj == null) {
            sb.append(".null");
        } else if (obj instanceof Integer) {
            sb.append(Operators.ARRAY_START);
            sb.append(((Integer) this.fieldName).intValue());
            sb.append(Operators.ARRAY_END);
        } else {
            sb.append(Operators.DOT);
            String obj2 = this.fieldName.toString();
            int i = 0;
            while (true) {
                if (i >= obj2.length()) {
                    z = false;
                    break;
                }
                char charAt = obj2.charAt(i);
                if ((charAt < '0' || charAt > '9') && ((charAt < 'A' || charAt > 'Z') && ((charAt < 'a' || charAt > 'z') && charAt <= 128))) {
                    z = true;
                    break;
                }
                i++;
            }
            if (z) {
                for (int i2 = 0; i2 < obj2.length(); i2++) {
                    char charAt2 = obj2.charAt(i2);
                    if (charAt2 == '\\') {
                        sb.append('\\');
                        sb.append('\\');
                        sb.append('\\');
                    } else if ((charAt2 >= '0' && charAt2 <= '9') || ((charAt2 >= 'A' && charAt2 <= 'Z') || ((charAt2 >= 'a' && charAt2 <= 'z') || charAt2 > 128))) {
                        sb.append(charAt2);
                    } else if (charAt2 == '\"') {
                        sb.append('\\');
                        sb.append('\\');
                        sb.append('\\');
                    } else {
                        sb.append('\\');
                        sb.append('\\');
                    }
                    sb.append(charAt2);
                }
                return;
            }
            sb.append(obj2);
        }
    }

    public SerialContext getParent() {
        return this.parent;
    }

    public Object getObject() {
        return this.object;
    }

    public Object getFieldName() {
        return this.fieldName;
    }

    public String getPath() {
        return toString();
    }
}
