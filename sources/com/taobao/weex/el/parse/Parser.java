package com.taobao.weex.el.parse;

import com.taobao.weex.WXEnvironment;
import com.taobao.weex.utils.WXLogUtils;
import java.util.List;

public class Parser {
    private String code;
    private ArrayStack<Symbol> operators = new ArrayStack<>();
    private int position = 0;
    private ArrayStack<Token> stacks = new ArrayStack<>();

    public Parser(String str) {
        this.code = str;
    }

    public final Token parse() {
        while (hasNextToken()) {
            scanNextToken();
        }
        while (!this.operators.isEmpty()) {
            doOperator(this.operators.pop());
        }
        if (this.stacks.size() == 1) {
            return this.stacks.pop();
        }
        return new Block(this.stacks.getList(), 6);
    }

    public static Token parse(String str) {
        try {
            return new Parser(str).parse();
        } catch (Exception e) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e("code " + str, (Throwable) e);
            }
            return new Block((List<Token>) null, 6);
        }
    }

    /* access modifiers changed from: package-private */
    public final char scanNextToken() {
        char nextToken = nextToken();
        if (nextToken == '$') {
            this.position++;
            return nextToken;
        }
        if (Character.isJavaIdentifierStart(nextToken)) {
            scanIdentifier();
        } else if (nextToken == '(' || nextToken == '{') {
            scanBracket();
        } else if (nextToken == '[') {
            scanArray();
        } else if (nextToken == '\"' || nextToken == '\'') {
            scanString();
        } else if ((nextToken == '.' && Character.isDigit(this.code.charAt(this.position + 1))) || Character.isDigit(nextToken)) {
            scanNumber();
        } else if (nextToken == '?') {
            scanIf();
        } else if (nextToken == ':' || nextToken == ')' || nextToken == '}' || nextToken == ' ' || nextToken == ']') {
            this.position++;
            return nextToken;
        } else {
            scanOperator();
        }
        return nextToken;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x003d A[LOOP:0: B:7:0x003d->B:10:0x0049, LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void scanArray() {
        /*
            r10 = this;
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r0 = r10.stacks
            int r0 = r0.size()
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Symbol> r1 = r10.operators
            int r1 = r1.size()
            int r2 = r10.position
            int r3 = r2 + -1
            r4 = 0
            r5 = 7
            r6 = 1
            if (r3 < 0) goto L_0x0025
            java.lang.String r3 = r10.code
            int r2 = r2 - r6
            char r2 = r3.charAt(r2)
            boolean r2 = java.lang.Character.isJavaIdentifierPart(r2)
            if (r2 != 0) goto L_0x0023
            goto L_0x0025
        L_0x0023:
            r2 = 0
            goto L_0x0026
        L_0x0025:
            r2 = 7
        L_0x0026:
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Symbol> r3 = r10.operators
            com.taobao.weex.el.parse.Symbol r7 = new com.taobao.weex.el.parse.Symbol
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r8 = r10.stacks
            int r8 = r8.size()
            java.lang.String r9 = "["
            r7.<init>(r9, r8)
            r3.push(r7)
            int r3 = r10.position
            int r3 = r3 + r6
            r10.position = r3
        L_0x003d:
            boolean r3 = r10.hasNextToken()
            if (r3 == 0) goto L_0x004b
            char r3 = r10.scanNextToken()
            r7 = 93
            if (r3 != r7) goto L_0x003d
        L_0x004b:
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r3 = r10.stacks
            int r3 = r3.size()
            if (r3 > r0) goto L_0x0062
        L_0x0053:
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Symbol> r0 = r10.operators
            int r0 = r0.size()
            if (r0 <= r1) goto L_0x0061
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Symbol> r0 = r10.operators
            r0.pop()
            goto L_0x0053
        L_0x0061:
            return
        L_0x0062:
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Symbol> r3 = r10.operators
            int r3 = r3.size()
            if (r3 <= r1) goto L_0x007e
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Symbol> r3 = r10.operators
            java.lang.Object r3 = r3.pop()
            com.taobao.weex.el.parse.Symbol r3 = (com.taobao.weex.el.parse.Symbol) r3
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r7 = r10.stacks
            int r7 = r7.size()
            if (r7 <= r0) goto L_0x0062
            r10.doOperator(r3)
            goto L_0x0062
        L_0x007e:
            java.util.ArrayList r1 = new java.util.ArrayList
            r3 = 4
            r1.<init>(r3)
            r3 = r0
        L_0x0085:
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r7 = r10.stacks
            int r7 = r7.size()
            if (r3 >= r7) goto L_0x009b
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r7 = r10.stacks
            java.lang.Object r7 = r7.get(r3)
            com.taobao.weex.el.parse.Token r7 = (com.taobao.weex.el.parse.Token) r7
            r1.add(r7)
            int r3 = r3 + 1
            goto L_0x0085
        L_0x009b:
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r3 = r10.stacks
            int r3 = r3.size()
            if (r3 <= r0) goto L_0x00a9
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r3 = r10.stacks
            r3.pop()
            goto L_0x009b
        L_0x00a9:
            if (r2 == r5) goto L_0x00e1
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r0 = r10.stacks
            int r0 = r0.size()
            if (r0 != 0) goto L_0x00b4
            goto L_0x00e1
        L_0x00b4:
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r0 = r10.stacks
            java.lang.Object r0 = r0.pop()
            com.taobao.weex.el.parse.Token r0 = (com.taobao.weex.el.parse.Token) r0
            int r3 = r1.size()
            if (r3 != r6) goto L_0x00c9
            java.lang.Object r1 = r1.get(r4)
            com.taobao.weex.el.parse.Token r1 = (com.taobao.weex.el.parse.Token) r1
            goto L_0x00d0
        L_0x00c9:
            com.taobao.weex.el.parse.Block r3 = new com.taobao.weex.el.parse.Block
            r4 = 6
            r3.<init>(r1, r4)
            r1 = r3
        L_0x00d0:
            com.taobao.weex.el.parse.Operator r3 = new com.taobao.weex.el.parse.Operator
            java.lang.String r4 = "."
            r3.<init>(r4, r2)
            r3.first = r0
            r3.second = r1
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r0 = r10.stacks
            r0.push(r3)
            return
        L_0x00e1:
            com.taobao.weex.el.parse.Block r0 = new com.taobao.weex.el.parse.Block
            r0.<init>(r1, r5)
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r1 = r10.stacks
            r1.push(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.el.parse.Parser.scanArray():void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:3:0x0031 A[LOOP:0: B:3:0x0031->B:6:0x003d, LOOP_START] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0057 A[LOOP:1: B:8:0x0057->B:11:0x0063, LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void scanBracket() {
        /*
            r7 = this;
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r0 = r7.stacks
            int r0 = r0.size()
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Symbol> r1 = r7.operators
            int r1 = r1.size()
            java.lang.String r2 = r7.code
            int r3 = r7.position
            char r2 = r2.charAt(r3)
            r3 = 1
            r4 = 123(0x7b, float:1.72E-43)
            if (r2 != r4) goto L_0x0040
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Symbol> r2 = r7.operators
            com.taobao.weex.el.parse.Symbol r4 = new com.taobao.weex.el.parse.Symbol
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r5 = r7.stacks
            int r5 = r5.size()
            java.lang.String r6 = "{"
            r4.<init>(r6, r5)
            r2.push(r4)
            int r2 = r7.position
            int r2 = r2 + r3
            r7.position = r2
        L_0x0031:
            boolean r2 = r7.hasNextToken()
            if (r2 == 0) goto L_0x0065
            char r2 = r7.scanNextToken()
            r4 = 125(0x7d, float:1.75E-43)
            if (r2 != r4) goto L_0x0031
            goto L_0x0065
        L_0x0040:
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Symbol> r2 = r7.operators
            com.taobao.weex.el.parse.Symbol r4 = new com.taobao.weex.el.parse.Symbol
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r5 = r7.stacks
            int r5 = r5.size()
            java.lang.String r6 = "("
            r4.<init>(r6, r5)
            r2.push(r4)
            int r2 = r7.position
            int r2 = r2 + r3
            r7.position = r2
        L_0x0057:
            boolean r2 = r7.hasNextToken()
            if (r2 == 0) goto L_0x0065
            char r2 = r7.scanNextToken()
            r4 = 41
            if (r2 != r4) goto L_0x0057
        L_0x0065:
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r2 = r7.stacks
            int r2 = r2.size()
            if (r2 > r0) goto L_0x007c
        L_0x006d:
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Symbol> r0 = r7.operators
            int r0 = r0.size()
            if (r0 <= r1) goto L_0x007b
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Symbol> r0 = r7.operators
            r0.pop()
            goto L_0x006d
        L_0x007b:
            return
        L_0x007c:
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Symbol> r2 = r7.operators
            int r2 = r2.size()
            if (r2 <= r1) goto L_0x0098
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Symbol> r2 = r7.operators
            java.lang.Object r2 = r2.pop()
            com.taobao.weex.el.parse.Symbol r2 = (com.taobao.weex.el.parse.Symbol) r2
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r4 = r7.stacks
            int r4 = r4.size()
            if (r4 <= r0) goto L_0x007c
            r7.doOperator(r2)
            goto L_0x007c
        L_0x0098:
            java.util.ArrayList r1 = new java.util.ArrayList
            r2 = 4
            r1.<init>(r2)
            r2 = r0
        L_0x009f:
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r4 = r7.stacks
            int r4 = r4.size()
            if (r2 >= r4) goto L_0x00b5
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r4 = r7.stacks
            java.lang.Object r4 = r4.get(r2)
            com.taobao.weex.el.parse.Token r4 = (com.taobao.weex.el.parse.Token) r4
            r1.add(r4)
            int r2 = r2 + 1
            goto L_0x009f
        L_0x00b5:
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r2 = r7.stacks
            int r2 = r2.size()
            if (r2 <= r0) goto L_0x00c3
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r2 = r7.stacks
            r2.pop()
            goto L_0x00b5
        L_0x00c3:
            int r0 = r1.size()
            if (r0 != r3) goto L_0x00d6
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r0 = r7.stacks
            r2 = 0
            java.lang.Object r1 = r1.get(r2)
            com.taobao.weex.el.parse.Token r1 = (com.taobao.weex.el.parse.Token) r1
            r0.push(r1)
            goto L_0x00e1
        L_0x00d6:
            com.taobao.weex.el.parse.Block r0 = new com.taobao.weex.el.parse.Block
            r2 = 6
            r0.<init>(r1, r2)
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r1 = r7.stacks
            r1.push(r0)
        L_0x00e1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.el.parse.Parser.scanBracket():void");
    }

    /* access modifiers changed from: package-private */
    public void scanOperator() {
        int i = this.position;
        String substring = this.code.substring(this.position, Math.min(i + 3, this.code.length()));
        if (substring.length() >= 3 && !Operators.OPERATORS_PRIORITY.containsKey(substring)) {
            substring = substring.substring(0, 2);
        }
        if (substring.length() >= 2 && !Operators.OPERATORS_PRIORITY.containsKey(substring)) {
            substring = substring.substring(0, 1);
        }
        if (!Operators.OPERATORS_PRIORITY.containsKey(substring)) {
            int min = Math.min(i + 1, this.code.length());
            WXLogUtils.e("weex", (Throwable) new IllegalArgumentException(this.code.substring(0, min) + " illegal code operator" + substring));
            this.position = this.position + substring.length();
            return;
        }
        if (!this.operators.isEmpty() && this.operators.peek() != null) {
            if (Operators.OPERATORS_PRIORITY.get(this.operators.peek().op).intValue() >= Operators.OPERATORS_PRIORITY.get(substring).intValue()) {
                doOperator(this.operators.pop());
            }
        }
        if (!Operators.isOpEnd(substring)) {
            this.operators.push(new Symbol(substring, this.stacks.size()));
        }
        this.position += substring.length();
    }

    /* access modifiers changed from: package-private */
    public void doOperator(Symbol symbol) {
        String str = symbol.op;
        if (!Operators.BRACKET_START_STR.equals(symbol.op) && !Operators.BLOCK_START_STR.equals(symbol.op) && !Operators.ARRAY_START_STR.equals(symbol.op) && !Operators.DOLLAR_STR.equals(symbol.op) && !Operators.BLOCK_START_STR.equals(symbol.op)) {
            int i = symbol.pos;
            int max = Math.max(symbol.pos - 1, 0);
            if (!this.operators.isEmpty()) {
                max = Math.max(max, this.operators.peek().pos);
            }
            Operator operator = new Operator(str, 5);
            if (Operators.AND_NOT.equals(str)) {
                if (this.stacks.size() > i) {
                    operator.self = this.stacks.remove(i);
                    this.stacks.add(i, operator);
                }
            } else if (this.stacks.size() > i) {
                operator.second = this.stacks.remove(i);
                if (this.stacks.size() > max) {
                    operator.first = this.stacks.remove(max);
                } else if (operator.second == null) {
                    return;
                }
                this.stacks.add(max, operator);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0046 A[LOOP:0: B:7:0x0046->B:10:0x0052, LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void scanIf() {
        /*
            r5 = this;
            com.taobao.weex.el.parse.Operator r0 = new com.taobao.weex.el.parse.Operator
            java.lang.String r1 = "?"
            r2 = 5
            r0.<init>(r1, r2)
            r1 = 0
            r5.doStackOperators(r1)
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Symbol> r2 = r5.operators
            int r2 = r2.size()
            if (r2 <= 0) goto L_0x0022
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Symbol> r2 = r5.operators
            java.lang.Object r2 = r2.peek()
            com.taobao.weex.el.parse.Symbol r2 = (com.taobao.weex.el.parse.Symbol) r2
            int r2 = r2.pos
            int r1 = java.lang.Math.max(r2, r1)
        L_0x0022:
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r2 = r5.stacks
            int r2 = r2.size()
            if (r2 <= r1) goto L_0x0034
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r1 = r5.stacks
            java.lang.Object r1 = r1.pop()
            com.taobao.weex.el.parse.Token r1 = (com.taobao.weex.el.parse.Token) r1
            r0.self = r1
        L_0x0034:
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r1 = r5.stacks
            int r1 = r1.size()
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Symbol> r2 = r5.operators
            int r2 = r2.size()
            int r3 = r5.position
            int r3 = r3 + 1
            r5.position = r3
        L_0x0046:
            boolean r3 = r5.hasNextToken()
            if (r3 == 0) goto L_0x0054
            char r3 = r5.scanNextToken()
            r4 = 58
            if (r3 != r4) goto L_0x0046
        L_0x0054:
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Symbol> r3 = r5.operators
            int r3 = r3.size()
            if (r3 <= r2) goto L_0x0068
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Symbol> r3 = r5.operators
            java.lang.Object r3 = r3.pop()
            com.taobao.weex.el.parse.Symbol r3 = (com.taobao.weex.el.parse.Symbol) r3
            r5.doOperator(r3)
            goto L_0x0054
        L_0x0068:
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r2 = r5.stacks
            int r2 = r2.size()
            if (r2 <= r1) goto L_0x007b
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r2 = r5.stacks
            java.lang.Object r2 = r2.pop()
            com.taobao.weex.el.parse.Token r2 = (com.taobao.weex.el.parse.Token) r2
            r0.first = r2
            goto L_0x0068
        L_0x007b:
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Symbol> r2 = r5.operators
            int r2 = r2.size()
        L_0x0081:
            boolean r3 = r5.hasNextToken()
            if (r3 == 0) goto L_0x009b
            r5.scanNextToken()
            boolean r3 = r5.hasNextToken()
            if (r3 == 0) goto L_0x0093
            r5.scanNextToken()
        L_0x0093:
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Symbol> r3 = r5.operators
            int r3 = r3.size()
            if (r3 > r2) goto L_0x0081
        L_0x009b:
            r5.doStackOperators(r2)
        L_0x009e:
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r2 = r5.stacks
            int r2 = r2.size()
            if (r2 <= r1) goto L_0x00b1
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r2 = r5.stacks
            java.lang.Object r2 = r2.pop()
            com.taobao.weex.el.parse.Token r2 = (com.taobao.weex.el.parse.Token) r2
            r0.second = r2
            goto L_0x009e
        L_0x00b1:
            com.taobao.weex.el.parse.ArrayStack<com.taobao.weex.el.parse.Token> r1 = r5.stacks
            r1.push(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.el.parse.Parser.scanIf():void");
    }

    private final void doStackOperators(int i) {
        while (this.operators.size() > i) {
            doOperator(this.operators.pop());
        }
    }

    /* access modifiers changed from: package-private */
    public final void scanNumber() {
        Token token;
        int i = this.position;
        boolean z = (this.code.charAt(i) == 'e' || this.code.charAt(this.position) == '.') ? false : true;
        this.position++;
        while (hasNext()) {
            char charAt = this.code.charAt(this.position);
            if (!Character.isDigit(charAt) && charAt != '.' && charAt != 'e') {
                break;
            }
            if (charAt == 'e' || charAt == '.') {
                z = false;
            }
            this.position++;
        }
        String substring = this.code.substring(i, this.position);
        if (!Operators.DOT_STR.equals(substring)) {
            if (z) {
                token = new Token(substring, 1);
            } else {
                token = new Token(substring, 2);
            }
            this.stacks.push(token);
        }
    }

    /* access modifiers changed from: package-private */
    public final void scanString() {
        int i = this.position;
        ArrayStack arrayStack = new ArrayStack();
        char charAt = this.code.charAt(i);
        arrayStack.push(Character.valueOf(charAt));
        StringBuilder sb = new StringBuilder();
        while (true) {
            this.position = i + 1;
            if (this.position >= this.code.length()) {
                break;
            }
            char charAt2 = this.code.charAt(this.position);
            if (charAt2 != charAt) {
                sb.append(charAt2);
            } else if (this.code.charAt(this.position - 1) != '\\') {
                arrayStack.pop();
                if (arrayStack.size() == 0) {
                    this.position++;
                    break;
                }
            } else {
                sb.deleteCharAt(sb.length() - 1);
                sb.append(charAt2);
            }
            i = this.position;
        }
        this.stacks.push(new Token(sb.toString(), 3));
    }

    /* access modifiers changed from: package-private */
    public final void scanIdentifier() {
        int i = this.position;
        this.position = i + 1;
        while (hasNext() && Character.isJavaIdentifierPart(this.code.charAt(this.position))) {
            this.position++;
        }
        String substring = this.code.substring(i, this.position);
        if (substring.startsWith(Operators.DOLLAR_STR)) {
            if (substring.length() != 1) {
                substring = substring.substring(1);
            } else {
                return;
            }
        }
        int i2 = 0;
        if (Operators.KEYWORDS.containsKey(substring) && (this.operators.isEmpty() || !Operators.isDot(this.operators.peek().op))) {
            i2 = 4;
        }
        this.stacks.push(new Token(substring, i2));
    }

    /* access modifiers changed from: package-private */
    public final boolean hasNext() {
        return this.position < this.code.length();
    }

    /* access modifiers changed from: package-private */
    public final boolean hasNextToken() {
        while (hasNext()) {
            if (this.code.charAt(this.position) != ' ') {
                return true;
            }
            this.position++;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public final char nextToken() {
        char charAt = this.code.charAt(this.position);
        while (charAt == ' ') {
            this.position++;
            int length = this.code.length();
            int i = this.position;
            if (length <= i) {
                break;
            }
            charAt = this.code.charAt(i);
        }
        return charAt;
    }
}
