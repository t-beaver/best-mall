package com.taobao.weex.el.parse;

class Operator extends Token {
    public Token first;
    public Token second;
    public Token self;

    public Operator(String str, int i) {
        super(str, i);
    }

    public Object execute(Object obj) {
        String token = getToken();
        token.hashCode();
        boolean z = false;
        char c = 65535;
        switch (token.hashCode()) {
            case 33:
                if (token.equals(Operators.AND_NOT)) {
                    c = 0;
                    break;
                }
                break;
            case 37:
                if (token.equals("%")) {
                    c = 1;
                    break;
                }
                break;
            case 42:
                if (token.equals("*")) {
                    c = 2;
                    break;
                }
                break;
            case 43:
                if (token.equals(Operators.PLUS)) {
                    c = 3;
                    break;
                }
                break;
            case 45:
                if (token.equals(Operators.SUB)) {
                    c = 4;
                    break;
                }
                break;
            case 46:
                if (token.equals(Operators.DOT_STR)) {
                    c = 5;
                    break;
                }
                break;
            case 47:
                if (token.equals("/")) {
                    c = 6;
                    break;
                }
                break;
            case 60:
                if (token.equals(Operators.L)) {
                    c = 7;
                    break;
                }
                break;
            case 62:
                if (token.equals(Operators.G)) {
                    c = 8;
                    break;
                }
                break;
            case 63:
                if (token.equals(Operators.CONDITION_IF_STRING)) {
                    c = 9;
                    break;
                }
                break;
            case 91:
                if (token.equals(Operators.ARRAY_START_STR)) {
                    c = 10;
                    break;
                }
                break;
            case 1084:
                if (token.equals(Operators.NOT_EQUAL2)) {
                    c = 11;
                    break;
                }
                break;
            case 1216:
                if (token.equals(Operators.AND)) {
                    c = 12;
                    break;
                }
                break;
            case 1921:
                if (token.equals(Operators.LE)) {
                    c = 13;
                    break;
                }
                break;
            case 1952:
                if (token.equals(Operators.EQUAL2)) {
                    c = 14;
                    break;
                }
                break;
            case 1983:
                if (token.equals(Operators.GE)) {
                    c = 15;
                    break;
                }
                break;
            case 3968:
                if (token.equals(Operators.OR)) {
                    c = 16;
                    break;
                }
                break;
            case 33665:
                if (token.equals(Operators.NOT_EQUAL)) {
                    c = 17;
                    break;
                }
                break;
            case 60573:
                if (token.equals(Operators.EQUAL)) {
                    c = 18;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return Boolean.valueOf(!Operators.tokenTrue(this.self, obj));
            case 1:
                return Operators.mod(this.first, this.second, obj);
            case 2:
                return Operators.mul(this.first, this.second, obj);
            case 3:
                return Operators.plus(this.first, this.second, obj);
            case 4:
                return Operators.sub(this.first, this.second, obj);
            case 5:
            case 10:
                return Operators.dot(this.first, this.second, obj);
            case 6:
                return Operators.div(this.first, this.second, obj);
            case 7:
                if (Operators.tokenNumber(this.first, obj) < Operators.tokenNumber(this.second, obj)) {
                    z = true;
                }
                return Boolean.valueOf(z);
            case 8:
                if (Operators.tokenNumber(this.first, obj) > Operators.tokenNumber(this.second, obj)) {
                    z = true;
                }
                return Boolean.valueOf(z);
            case 9:
                return Operators.condition(this.self, this.first, this.second, obj);
            case 11:
            case 17:
                return Boolean.valueOf(!Operators.isEquals(this.first, this.second, obj));
            case 12:
                if (Operators.tokenTrue(this.first, obj) && Operators.tokenTrue(this.second, obj)) {
                    z = true;
                }
                return Boolean.valueOf(z);
            case 13:
                if (Operators.tokenNumber(this.first, obj) <= Operators.tokenNumber(this.second, obj)) {
                    z = true;
                }
                return Boolean.valueOf(z);
            case 14:
            case 18:
                return Boolean.valueOf(Operators.isEquals(this.first, this.second, obj));
            case 15:
                if (Operators.tokenNumber(this.first, obj) >= Operators.tokenNumber(this.second, obj)) {
                    z = true;
                }
                return Boolean.valueOf(z);
            case 16:
                if (Operators.tokenTrue(this.first, obj) || Operators.tokenTrue(this.second, obj)) {
                    z = true;
                }
                return Boolean.valueOf(z);
            default:
                throw new IllegalArgumentException(token + " operator is not supported");
        }
    }

    public String toString() {
        if (Operators.AND_NOT.equals(getToken())) {
            return "{!" + this.self + Operators.BLOCK_END_STR;
        } else if (this.self == null) {
            return Operators.BLOCK_START_STR + this.first + getToken() + this.second + Operators.BLOCK_END_STR;
        } else {
            return Operators.BLOCK_START_STR + this.self + getToken() + this.first + ":" + this.second + Operators.BLOCK_END_STR;
        }
    }
}
