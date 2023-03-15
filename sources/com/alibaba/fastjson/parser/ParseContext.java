package com.alibaba.fastjson.parser;

import com.taobao.weex.el.parse.Operators;
import java.lang.reflect.Type;

public class ParseContext {
    public final Object fieldName;
    public final int level;
    public Object object;
    public final ParseContext parent;
    private transient String path;
    public Type type;

    public ParseContext(ParseContext parseContext, Object obj, Object obj2) {
        int i;
        this.parent = parseContext;
        this.object = obj;
        this.fieldName = obj2;
        if (parseContext == null) {
            i = 0;
        } else {
            i = parseContext.level + 1;
        }
        this.level = i;
    }

    public String toString() {
        if (this.path == null) {
            if (this.parent == null) {
                this.path = Operators.DOLLAR_STR;
            } else if (this.fieldName instanceof Integer) {
                this.path = this.parent.toString() + Operators.ARRAY_START_STR + this.fieldName + Operators.ARRAY_END_STR;
            } else {
                this.path = this.parent.toString() + Operators.DOT_STR + this.fieldName;
            }
        }
        return this.path;
    }
}
