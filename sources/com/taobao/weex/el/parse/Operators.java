package com.taobao.weex.el.parse;

import com.taobao.weex.common.Constants;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Operators {
    public static final String AND = "&&";
    public static final String AND_NOT = "!";
    public static final char ARRAY_END = ']';
    public static final String ARRAY_END_STR = "]";
    public static final char ARRAY_SEPRATOR = ',';
    public static final String ARRAY_SEPRATOR_STR = ",";
    public static final char ARRAY_START = '[';
    public static final String ARRAY_START_STR = "[";
    public static final char BLOCK_END = '}';
    public static final String BLOCK_END_STR = "}";
    public static final char BLOCK_START = '{';
    public static final String BLOCK_START_STR = "{";
    public static final char BRACKET_END = ')';
    public static final String BRACKET_END_STR = ")";
    public static final char BRACKET_START = '(';
    public static final String BRACKET_START_STR = "(";
    public static final char CONDITION_IF = '?';
    public static final char CONDITION_IF_MIDDLE = ':';
    public static final String CONDITION_IF_STRING = "?";
    public static final String DIV = "/";
    public static final char DOLLAR = '$';
    public static final String DOLLAR_STR = "$";
    public static final char DOT = '.';
    public static final String DOT_STR = ".";
    public static final String EQUAL = "===";
    public static final String EQUAL2 = "==";
    public static final String G = ">";
    public static final String GE = ">=";
    public static final Map<String, Object> KEYWORDS;
    public static final String L = "<";
    public static final String LE = "<=";
    public static final String MOD = "%";
    public static final String MUL = "*";
    public static final String NOT_EQUAL = "!==";
    public static final String NOT_EQUAL2 = "!=";
    public static Map<String, Integer> OPERATORS_PRIORITY = null;
    public static final String OR = "||";
    public static final String PLUS = "+";
    public static final char QUOTE = '\"';
    public static final char SINGLE_QUOTE = '\'';
    public static final char SPACE = ' ';
    public static final String SPACE_STR = " ";
    public static final String SUB = "-";

    public static boolean isOpEnd(char c) {
        return c == ')' || c == ']' || c == ' ' || c == ',';
    }

    public static Object dot(Token token, Token token2, Object obj) {
        Object execute;
        Object obj2;
        String str;
        if (token == null || token2 == null || (execute = token.execute(obj)) == null) {
            return null;
        }
        if (token2.getType() != 0) {
            Object execute2 = token2.execute(obj);
            if (execute2 instanceof Double) {
                execute2 = Integer.valueOf(((Double) execute2).intValue());
            }
            if (execute2 == null) {
                str = "";
            } else {
                str = execute2.toString().trim();
            }
            obj2 = el(execute, str);
        } else {
            obj2 = token2.execute(execute);
        }
        if (obj2 != null) {
            return obj2;
        }
        return specialKey(execute, token2.getToken());
    }

    public static Object el(Object obj, String str) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof ArrayStack) {
            ArrayStack arrayStack = (ArrayStack) obj;
            for (int size = arrayStack.size() - 1; size >= 0; size--) {
                Object obj2 = arrayStack.get(size);
                if (obj2 instanceof Map) {
                    Map map = (Map) obj2;
                    if (map.containsKey(str)) {
                        return map.get(str);
                    }
                }
            }
        }
        if (obj instanceof Stack) {
            Stack stack = (Stack) obj;
            for (int size2 = stack.size() - 1; size2 >= 0; size2--) {
                Object obj3 = stack.get(size2);
                if (obj3 instanceof Map) {
                    Map map2 = (Map) obj3;
                    if (map2.containsKey(str)) {
                        return map2.get(str);
                    }
                }
            }
        }
        if (obj instanceof Map) {
            return ((Map) obj).get(str);
        }
        if (obj instanceof List) {
            try {
                return ((List) obj).get(Integer.parseInt(str));
            } catch (Exception unused) {
            }
        }
        if (obj.getClass().isArray()) {
            try {
                return Array.get(obj, Integer.parseInt(str));
            } catch (Exception unused2) {
            }
        }
        return null;
    }

    public static Object specialKey(Object obj, String str) {
        if (!"length".equals(str)) {
            return null;
        }
        if (obj instanceof CharSequence) {
            return Integer.valueOf(((CharSequence) obj).length());
        }
        boolean z = obj instanceof Map;
        if (z) {
            return Integer.valueOf(((Map) obj).size());
        }
        if (z) {
            return Integer.valueOf(((Map) obj).size());
        }
        if (obj instanceof List) {
            return Integer.valueOf(((List) obj).size());
        }
        if (obj.getClass().isArray()) {
            return Integer.valueOf(Array.getLength(obj));
        }
        return null;
    }

    public static Object plus(Token token, Token token2, Object obj) {
        Object execute = token != null ? token.execute(obj) : null;
        Object execute2 = token2 != null ? token2.execute(obj) : null;
        String str = "";
        if ((execute instanceof CharSequence) || (execute2 instanceof CharSequence)) {
            if (execute == null) {
                return execute2;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(execute.toString());
            if (execute2 != null) {
                str = execute2.toString();
            }
            sb.append(str);
            return sb.toString();
        } else if ((execute instanceof Number) || (execute2 instanceof Number)) {
            return Double.valueOf(getNumber(execute) + getNumber(execute2));
        } else {
            if (execute == null && execute2 == null) {
                return null;
            }
            if (execute == null) {
                return execute2.toString();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append(execute.toString());
            if (execute2 != null) {
                str = execute2.toString();
            }
            sb2.append(str);
            return sb2.toString();
        }
    }

    public static Object sub(Token token, Token token2, Object obj) {
        Object obj2 = null;
        Object execute = token != null ? token.execute(obj) : null;
        if (token2 != null) {
            obj2 = token2.execute(obj);
        }
        return Double.valueOf(getNumber(execute) - getNumber(obj2));
    }

    public static Object div(Token token, Token token2, Object obj) {
        Object obj2 = null;
        Object execute = token != null ? token.execute(obj) : null;
        if (token2 != null) {
            obj2 = token2.execute(obj);
        }
        return Double.valueOf(getNumber(execute) / getNumber(obj2));
    }

    public static Object mul(Token token, Token token2, Object obj) {
        Object obj2 = null;
        Object execute = token != null ? token.execute(obj) : null;
        if (token2 != null) {
            obj2 = token2.execute(obj);
        }
        return Double.valueOf(getNumber(execute) * getNumber(obj2));
    }

    public static Object mod(Token token, Token token2, Object obj) {
        Object obj2 = null;
        Object execute = token != null ? token.execute(obj) : null;
        if (token2 != null) {
            obj2 = token2.execute(obj);
        }
        return Double.valueOf(getNumber(execute) % getNumber(obj2));
    }

    public static Object condition(Token token, Token token2, Token token3, Object obj) {
        if (token != null ? isTrue(token.execute(obj)) : false) {
            if (token2 != null) {
                return token2.execute(obj);
            }
            return null;
        } else if (token3 != null) {
            return token3.execute(obj);
        } else {
            return null;
        }
    }

    public static boolean tokenTrue(Token token, Object obj) {
        if (token == null) {
            return false;
        }
        return isTrue(token.execute(obj));
    }

    public static double tokenNumber(Token token, Object obj) {
        if (token == null) {
            return 0.0d;
        }
        return getNumber(token.execute(obj));
    }

    public static boolean isEquals(Token token, Token token2, Object obj) {
        if (token == null && token2 == null) {
            return true;
        }
        Object obj2 = null;
        Object execute = token != null ? token.execute(obj) : null;
        if (token2 != null) {
            obj2 = token2.execute(obj);
        }
        if (execute == null) {
            if (obj2 == null) {
                return true;
            }
            if (!(obj2 instanceof CharSequence) || !isEmpty(obj2.toString())) {
                return false;
            }
            return true;
        } else if (obj2 == null) {
            if (isEmpty(execute.toString())) {
                return true;
            }
            return false;
        } else if (execute instanceof Number) {
            if (obj2 instanceof Number) {
                if (((Number) execute).doubleValue() == ((Number) obj2).doubleValue()) {
                    return true;
                }
                return false;
            } else if (((Number) execute).doubleValue() == getNumber(obj2)) {
                return true;
            } else {
                return false;
            }
        } else if (obj2 instanceof Number) {
            if (getNumber(execute) == ((Number) obj2).doubleValue()) {
                return true;
            }
            return false;
        } else if ((execute instanceof CharSequence) || (obj2 instanceof CharSequence)) {
            return execute.toString().trim().equals(obj2.toString().trim());
        } else {
            return execute.equals(obj2);
        }
    }

    public static boolean isTrue(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Number)) {
            String trim = obj.toString().trim();
            return !AbsoluteConst.FALSE.equals(trim) && !Constants.Name.UNDEFINED.equals(trim) && !"null".equals(trim) && !isEmpty(trim);
        } else if (((Number) obj).doubleValue() != 0.0d) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

    public static double getNumber(Object obj) {
        if (obj == null) {
            return 0.0d;
        }
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        try {
            return Double.parseDouble(obj.toString());
        } catch (Exception unused) {
            return 0.0d;
        }
    }

    public static boolean isOpEnd(String str) {
        return isOpEnd(str.charAt(0));
    }

    public static boolean isDot(String str) {
        char charAt = str.charAt(0);
        return charAt == '.' || charAt == '[';
    }

    static {
        HashMap hashMap = new HashMap();
        OPERATORS_PRIORITY = hashMap;
        hashMap.put(BLOCK_END_STR, 0);
        OPERATORS_PRIORITY.put(BRACKET_END_STR, 0);
        OPERATORS_PRIORITY.put(SPACE_STR, 0);
        OPERATORS_PRIORITY.put(",", 0);
        OPERATORS_PRIORITY.put(ARRAY_END_STR, 0);
        OPERATORS_PRIORITY.put(OR, 1);
        OPERATORS_PRIORITY.put(AND, 1);
        OPERATORS_PRIORITY.put(EQUAL, 2);
        OPERATORS_PRIORITY.put(EQUAL2, 2);
        OPERATORS_PRIORITY.put(NOT_EQUAL, 2);
        OPERATORS_PRIORITY.put(NOT_EQUAL2, 2);
        OPERATORS_PRIORITY.put(G, 7);
        OPERATORS_PRIORITY.put(GE, 7);
        OPERATORS_PRIORITY.put(L, 7);
        OPERATORS_PRIORITY.put(LE, 8);
        OPERATORS_PRIORITY.put(PLUS, 9);
        OPERATORS_PRIORITY.put(SUB, 9);
        OPERATORS_PRIORITY.put("*", 10);
        OPERATORS_PRIORITY.put("/", 10);
        OPERATORS_PRIORITY.put("%", 10);
        OPERATORS_PRIORITY.put(AND_NOT, 11);
        OPERATORS_PRIORITY.put(DOT_STR, 15);
        OPERATORS_PRIORITY.put(ARRAY_START_STR, 16);
        OPERATORS_PRIORITY.put(BRACKET_START_STR, 17);
        OPERATORS_PRIORITY.put(BLOCK_START_STR, 17);
        HashMap hashMap2 = new HashMap();
        KEYWORDS = hashMap2;
        hashMap2.put("null", (Object) null);
        hashMap2.put(AbsoluteConst.TRUE, Boolean.TRUE);
        hashMap2.put(AbsoluteConst.FALSE, Boolean.FALSE);
        hashMap2.put(Constants.Name.UNDEFINED, (Object) null);
    }
}
