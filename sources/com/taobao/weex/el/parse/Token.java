package com.taobao.weex.el.parse;

public class Token {
    public static final int TYPE_ARRAY = 7;
    public static final int TYPE_BLOCK = 6;
    public static final int TYPE_DOUBLE = 2;
    public static final int TYPE_IDENTIFIER = 0;
    public static final int TYPE_INT = 1;
    public static final int TYPE_KEYWORD = 4;
    public static final int TYPE_OPERATOR = 5;
    public static final int TYPE_STRING = 3;
    private String token;
    private int type;

    public Token(String str, int i) {
        this.token = str;
        this.type = i;
    }

    public Object execute(Object obj) {
        int i = this.type;
        if (i == 0) {
            return Operators.el(obj, this.token);
        }
        if (i == 3) {
            return this.token;
        }
        if (i == 1) {
            try {
                return Integer.valueOf(Integer.parseInt(this.token));
            } catch (Exception unused) {
                return 0;
            }
        } else if (i == 2) {
            try {
                return Double.valueOf(Double.parseDouble(this.token));
            } catch (Exception unused2) {
                return 0;
            }
        } else if (i == 4) {
            return Operators.KEYWORDS.get(this.token);
        } else {
            throw new IllegalArgumentException("unhandled token type " + this.type);
        }
    }

    public String toString() {
        return Operators.BLOCK_START_STR + this.token + "," + this.type + Operators.BLOCK_END;
    }

    public String getToken() {
        return this.token;
    }

    public int getType() {
        return this.type;
    }
}
