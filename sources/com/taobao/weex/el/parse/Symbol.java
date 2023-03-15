package com.taobao.weex.el.parse;

public class Symbol {
    public final String op;
    public final int pos;

    public Symbol(String str, int i) {
        this.op = str;
        this.pos = i;
    }
}
