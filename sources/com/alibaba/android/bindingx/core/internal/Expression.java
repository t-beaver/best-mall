package com.alibaba.android.bindingx.core.internal;

import com.alibaba.android.bindingx.core.LogProxy;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

class Expression {
    private JSONObject root;

    Expression(String str) {
        try {
            this.root = (JSONObject) new JSONTokener(str).nextValue();
        } catch (Throwable th) {
            LogProxy.e("[Expression] expression is illegal. \n ", th);
        }
    }

    Expression(JSONObject jSONObject) {
        this.root = jSONObject;
    }

    /* access modifiers changed from: package-private */
    public Object execute(Map<String, Object> map) throws IllegalArgumentException, JSONException {
        return execute(this.root, map);
    }

    private double toNumber(Object obj) {
        if (obj instanceof String) {
            return Double.parseDouble((String) obj);
        }
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue() ? 1.0d : 0.0d;
        }
        return ((Double) obj).doubleValue();
    }

    private boolean toBoolean(Object obj) {
        if (obj instanceof String) {
            return "".equals(obj);
        }
        if (obj instanceof Double) {
            return ((Double) obj).doubleValue() != 0.0d;
        }
        return ((Boolean) obj).booleanValue();
    }

    private String toString(Object obj) {
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue() ? AbsoluteConst.TRUE : AbsoluteConst.FALSE;
        }
        if (obj instanceof Double) {
            return Double.toString(((Double) obj).doubleValue());
        }
        return (String) obj;
    }

    private boolean equal(Object obj, Object obj2) {
        if ((obj instanceof JSObjectInterface) && (obj2 instanceof JSObjectInterface)) {
            return obj == obj2;
        }
        if ((obj instanceof String) && (obj2 instanceof String)) {
            return obj.equals(obj2);
        }
        if (!(obj instanceof Boolean) || !(obj2 instanceof Boolean)) {
            if (toNumber(obj) == toNumber(obj2)) {
                return true;
            }
            return false;
        } else if (toBoolean(obj) == toBoolean(obj2)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean strictlyEqual(Object obj, Object obj2) {
        if ((obj instanceof JSObjectInterface) && !(obj2 instanceof JSObjectInterface)) {
            return false;
        }
        if ((obj instanceof Boolean) && !(obj2 instanceof Boolean)) {
            return false;
        }
        if ((obj instanceof Double) && !(obj2 instanceof Double)) {
            return false;
        }
        if ((!(obj instanceof String) || (obj2 instanceof String)) && obj == obj2) {
            return true;
        }
        return false;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v3, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v5, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v7, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v9, resolved type: boolean} */
    /* JADX WARNING: type inference failed for: r5v0 */
    /* JADX WARNING: type inference failed for: r5v1, types: [int] */
    /* JADX WARNING: type inference failed for: r5v4 */
    /* JADX WARNING: type inference failed for: r5v6 */
    /* JADX WARNING: type inference failed for: r5v8 */
    /* JADX WARNING: type inference failed for: r5v10 */
    /* JADX WARNING: type inference failed for: r5v11 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.Object execute(org.json.JSONObject r9, java.util.Map<java.lang.String, java.lang.Object> r10) throws java.lang.IllegalArgumentException, org.json.JSONException {
        /*
            r8 = this;
            java.lang.String r0 = "type"
            java.lang.String r0 = r9.getString(r0)
            java.lang.String r1 = "children"
            org.json.JSONArray r2 = r9.optJSONArray(r1)
            r0.hashCode()
            int r3 = r0.hashCode()
            r4 = 2
            r5 = 0
            r6 = 1
            r7 = -1
            switch(r3) {
                case -1746151498: goto L_0x013c;
                case 33: goto L_0x0131;
                case 37: goto L_0x0126;
                case 42: goto L_0x011b;
                case 43: goto L_0x0110;
                case 45: goto L_0x0105;
                case 47: goto L_0x00fa;
                case 60: goto L_0x00ef;
                case 62: goto L_0x00e1;
                case 63: goto L_0x00d3;
                case 1084: goto L_0x00c5;
                case 1216: goto L_0x00b7;
                case 1344: goto L_0x00a9;
                case 1921: goto L_0x009b;
                case 1952: goto L_0x008d;
                case 1983: goto L_0x007f;
                case 3968: goto L_0x0070;
                case 33665: goto L_0x0062;
                case 60573: goto L_0x0054;
                case 189157634: goto L_0x0046;
                case 375032009: goto L_0x0038;
                case 1074430782: goto L_0x002a;
                case 1816238983: goto L_0x001c;
                default: goto L_0x001a;
            }
        L_0x001a:
            goto L_0x0146
        L_0x001c:
            java.lang.String r3 = "BooleanLiteral"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x0026
            goto L_0x0146
        L_0x0026:
            r7 = 22
            goto L_0x0146
        L_0x002a:
            java.lang.String r3 = "StringLiteral"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x0034
            goto L_0x0146
        L_0x0034:
            r7 = 21
            goto L_0x0146
        L_0x0038:
            java.lang.String r3 = "Identifier"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x0042
            goto L_0x0146
        L_0x0042:
            r7 = 20
            goto L_0x0146
        L_0x0046:
            java.lang.String r3 = "NumericLiteral"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x0050
            goto L_0x0146
        L_0x0050:
            r7 = 19
            goto L_0x0146
        L_0x0054:
            java.lang.String r3 = "==="
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x005e
            goto L_0x0146
        L_0x005e:
            r7 = 18
            goto L_0x0146
        L_0x0062:
            java.lang.String r3 = "!=="
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x006c
            goto L_0x0146
        L_0x006c:
            r7 = 17
            goto L_0x0146
        L_0x0070:
            java.lang.String r3 = "||"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x007b
            goto L_0x0146
        L_0x007b:
            r7 = 16
            goto L_0x0146
        L_0x007f:
            java.lang.String r3 = ">="
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x0089
            goto L_0x0146
        L_0x0089:
            r7 = 15
            goto L_0x0146
        L_0x008d:
            java.lang.String r3 = "=="
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x0097
            goto L_0x0146
        L_0x0097:
            r7 = 14
            goto L_0x0146
        L_0x009b:
            java.lang.String r3 = "<="
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x00a5
            goto L_0x0146
        L_0x00a5:
            r7 = 13
            goto L_0x0146
        L_0x00a9:
            java.lang.String r3 = "**"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x00b3
            goto L_0x0146
        L_0x00b3:
            r7 = 12
            goto L_0x0146
        L_0x00b7:
            java.lang.String r3 = "&&"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x00c1
            goto L_0x0146
        L_0x00c1:
            r7 = 11
            goto L_0x0146
        L_0x00c5:
            java.lang.String r3 = "!="
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x00cf
            goto L_0x0146
        L_0x00cf:
            r7 = 10
            goto L_0x0146
        L_0x00d3:
            java.lang.String r3 = "?"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x00dd
            goto L_0x0146
        L_0x00dd:
            r7 = 9
            goto L_0x0146
        L_0x00e1:
            java.lang.String r3 = ">"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x00eb
            goto L_0x0146
        L_0x00eb:
            r7 = 8
            goto L_0x0146
        L_0x00ef:
            java.lang.String r3 = "<"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x00f8
            goto L_0x0146
        L_0x00f8:
            r7 = 7
            goto L_0x0146
        L_0x00fa:
            java.lang.String r3 = "/"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x0103
            goto L_0x0146
        L_0x0103:
            r7 = 6
            goto L_0x0146
        L_0x0105:
            java.lang.String r3 = "-"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x010e
            goto L_0x0146
        L_0x010e:
            r7 = 5
            goto L_0x0146
        L_0x0110:
            java.lang.String r3 = "+"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x0119
            goto L_0x0146
        L_0x0119:
            r7 = 4
            goto L_0x0146
        L_0x011b:
            java.lang.String r3 = "*"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x0124
            goto L_0x0146
        L_0x0124:
            r7 = 3
            goto L_0x0146
        L_0x0126:
            java.lang.String r3 = "%"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x012f
            goto L_0x0146
        L_0x012f:
            r7 = 2
            goto L_0x0146
        L_0x0131:
            java.lang.String r3 = "!"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x013a
            goto L_0x0146
        L_0x013a:
            r7 = 1
            goto L_0x0146
        L_0x013c:
            java.lang.String r3 = "CallExpression"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x0145
            goto L_0x0146
        L_0x0145:
            r7 = 0
        L_0x0146:
            java.lang.String r0 = "value"
            switch(r7) {
                case 0: goto L_0x0378;
                case 1: goto L_0x0366;
                case 2: goto L_0x0348;
                case 3: goto L_0x0329;
                case 4: goto L_0x030b;
                case 5: goto L_0x02ed;
                case 6: goto L_0x02cf;
                case 7: goto L_0x02ad;
                case 8: goto L_0x028b;
                case 9: goto L_0x0269;
                case 10: goto L_0x024f;
                case 11: goto L_0x0237;
                case 12: goto L_0x0216;
                case 13: goto L_0x01f4;
                case 14: goto L_0x01db;
                case 15: goto L_0x01b9;
                case 16: goto L_0x01a1;
                case 17: goto L_0x0187;
                case 18: goto L_0x016e;
                case 19: goto L_0x0165;
                case 20: goto L_0x015c;
                case 21: goto L_0x0157;
                case 22: goto L_0x014e;
                default: goto L_0x014c;
            }
        L_0x014c:
            r9 = 0
            return r9
        L_0x014e:
            boolean r9 = r9.getBoolean(r0)
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r9)
            return r9
        L_0x0157:
            java.lang.String r9 = r9.getString(r0)
            return r9
        L_0x015c:
            java.lang.String r9 = r9.getString(r0)
            java.lang.Object r9 = r10.get(r9)
            return r9
        L_0x0165:
            double r9 = r9.getDouble(r0)
            java.lang.Double r9 = java.lang.Double.valueOf(r9)
            return r9
        L_0x016e:
            org.json.JSONObject r9 = r2.getJSONObject(r5)
            java.lang.Object r9 = r8.execute(r9, r10)
            org.json.JSONObject r0 = r2.getJSONObject(r6)
            java.lang.Object r10 = r8.execute(r0, r10)
            boolean r9 = r8.strictlyEqual(r9, r10)
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r9)
            return r9
        L_0x0187:
            org.json.JSONObject r9 = r2.getJSONObject(r5)
            java.lang.Object r9 = r8.execute(r9, r10)
            org.json.JSONObject r0 = r2.getJSONObject(r6)
            java.lang.Object r10 = r8.execute(r0, r10)
            boolean r9 = r8.strictlyEqual(r9, r10)
            r9 = r9 ^ r6
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r9)
            return r9
        L_0x01a1:
            org.json.JSONObject r9 = r2.getJSONObject(r5)
            java.lang.Object r9 = r8.execute(r9, r10)
            boolean r0 = r8.toBoolean(r9)
            if (r0 == 0) goto L_0x01b0
            return r9
        L_0x01b0:
            org.json.JSONObject r9 = r2.getJSONObject(r6)
            java.lang.Object r9 = r8.execute(r9, r10)
            return r9
        L_0x01b9:
            org.json.JSONObject r9 = r2.getJSONObject(r5)
            java.lang.Object r9 = r8.execute(r9, r10)
            double r0 = r8.toNumber(r9)
            org.json.JSONObject r9 = r2.getJSONObject(r6)
            java.lang.Object r9 = r8.execute(r9, r10)
            double r9 = r8.toNumber(r9)
            int r2 = (r0 > r9 ? 1 : (r0 == r9 ? 0 : -1))
            if (r2 < 0) goto L_0x01d6
            r5 = 1
        L_0x01d6:
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r5)
            return r9
        L_0x01db:
            org.json.JSONObject r9 = r2.getJSONObject(r5)
            java.lang.Object r9 = r8.execute(r9, r10)
            org.json.JSONObject r0 = r2.getJSONObject(r6)
            java.lang.Object r10 = r8.execute(r0, r10)
            boolean r9 = r8.equal(r9, r10)
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r9)
            return r9
        L_0x01f4:
            org.json.JSONObject r9 = r2.getJSONObject(r5)
            java.lang.Object r9 = r8.execute(r9, r10)
            double r0 = r8.toNumber(r9)
            org.json.JSONObject r9 = r2.getJSONObject(r6)
            java.lang.Object r9 = r8.execute(r9, r10)
            double r9 = r8.toNumber(r9)
            int r2 = (r0 > r9 ? 1 : (r0 == r9 ? 0 : -1))
            if (r2 > 0) goto L_0x0211
            r5 = 1
        L_0x0211:
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r5)
            return r9
        L_0x0216:
            org.json.JSONObject r9 = r2.getJSONObject(r5)
            java.lang.Object r9 = r8.execute(r9, r10)
            double r0 = r8.toNumber(r9)
            org.json.JSONObject r9 = r2.getJSONObject(r6)
            java.lang.Object r9 = r8.execute(r9, r10)
            double r9 = r8.toNumber(r9)
            double r9 = java.lang.Math.pow(r0, r9)
            java.lang.Double r9 = java.lang.Double.valueOf(r9)
            return r9
        L_0x0237:
            org.json.JSONObject r9 = r2.getJSONObject(r5)
            java.lang.Object r9 = r8.execute(r9, r10)
            boolean r0 = r8.toBoolean(r9)
            if (r0 != 0) goto L_0x0246
            return r9
        L_0x0246:
            org.json.JSONObject r9 = r2.getJSONObject(r6)
            java.lang.Object r9 = r8.execute(r9, r10)
            return r9
        L_0x024f:
            org.json.JSONObject r9 = r2.getJSONObject(r5)
            java.lang.Object r9 = r8.execute(r9, r10)
            org.json.JSONObject r0 = r2.getJSONObject(r6)
            java.lang.Object r10 = r8.execute(r0, r10)
            boolean r9 = r8.equal(r9, r10)
            r9 = r9 ^ r6
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r9)
            return r9
        L_0x0269:
            org.json.JSONObject r9 = r2.getJSONObject(r5)
            java.lang.Object r9 = r8.execute(r9, r10)
            java.lang.Boolean r9 = (java.lang.Boolean) r9
            boolean r9 = r9.booleanValue()
            if (r9 == 0) goto L_0x0282
            org.json.JSONObject r9 = r2.getJSONObject(r6)
            java.lang.Object r9 = r8.execute(r9, r10)
            return r9
        L_0x0282:
            org.json.JSONObject r9 = r2.getJSONObject(r4)
            java.lang.Object r9 = r8.execute(r9, r10)
            return r9
        L_0x028b:
            org.json.JSONObject r9 = r2.getJSONObject(r5)
            java.lang.Object r9 = r8.execute(r9, r10)
            double r0 = r8.toNumber(r9)
            org.json.JSONObject r9 = r2.getJSONObject(r6)
            java.lang.Object r9 = r8.execute(r9, r10)
            double r9 = r8.toNumber(r9)
            int r2 = (r0 > r9 ? 1 : (r0 == r9 ? 0 : -1))
            if (r2 <= 0) goto L_0x02a8
            r5 = 1
        L_0x02a8:
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r5)
            return r9
        L_0x02ad:
            org.json.JSONObject r9 = r2.getJSONObject(r5)
            java.lang.Object r9 = r8.execute(r9, r10)
            double r0 = r8.toNumber(r9)
            org.json.JSONObject r9 = r2.getJSONObject(r6)
            java.lang.Object r9 = r8.execute(r9, r10)
            double r9 = r8.toNumber(r9)
            int r2 = (r0 > r9 ? 1 : (r0 == r9 ? 0 : -1))
            if (r2 >= 0) goto L_0x02ca
            r5 = 1
        L_0x02ca:
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r5)
            return r9
        L_0x02cf:
            org.json.JSONObject r9 = r2.getJSONObject(r5)
            java.lang.Object r9 = r8.execute(r9, r10)
            double r0 = r8.toNumber(r9)
            org.json.JSONObject r9 = r2.getJSONObject(r6)
            java.lang.Object r9 = r8.execute(r9, r10)
            double r9 = r8.toNumber(r9)
            double r0 = r0 / r9
            java.lang.Double r9 = java.lang.Double.valueOf(r0)
            return r9
        L_0x02ed:
            org.json.JSONObject r9 = r2.getJSONObject(r5)
            java.lang.Object r9 = r8.execute(r9, r10)
            double r0 = r8.toNumber(r9)
            org.json.JSONObject r9 = r2.getJSONObject(r6)
            java.lang.Object r9 = r8.execute(r9, r10)
            double r9 = r8.toNumber(r9)
            double r0 = r0 - r9
            java.lang.Double r9 = java.lang.Double.valueOf(r0)
            return r9
        L_0x030b:
            org.json.JSONObject r9 = r2.getJSONObject(r5)
            java.lang.Object r9 = r8.execute(r9, r10)
            double r0 = r8.toNumber(r9)
            org.json.JSONObject r9 = r2.getJSONObject(r6)
            java.lang.Object r9 = r8.execute(r9, r10)
            double r9 = r8.toNumber(r9)
            double r0 = r0 + r9
            java.lang.Double r9 = java.lang.Double.valueOf(r0)
            return r9
        L_0x0329:
            org.json.JSONObject r9 = r2.getJSONObject(r5)
            java.lang.Object r9 = r8.execute(r9, r10)
            double r0 = r8.toNumber(r9)
            org.json.JSONObject r9 = r2.getJSONObject(r6)
            java.lang.Object r9 = r8.execute(r9, r10)
            double r9 = r8.toNumber(r9)
            double r0 = r0 * r9
            java.lang.Double r9 = java.lang.Double.valueOf(r0)
            return r9
        L_0x0348:
            org.json.JSONObject r9 = r2.getJSONObject(r5)
            java.lang.Object r9 = r8.execute(r9, r10)
            double r0 = r8.toNumber(r9)
            org.json.JSONObject r9 = r2.getJSONObject(r6)
            java.lang.Object r9 = r8.execute(r9, r10)
            double r9 = r8.toNumber(r9)
            double r0 = r0 % r9
            java.lang.Double r9 = java.lang.Double.valueOf(r0)
            return r9
        L_0x0366:
            org.json.JSONObject r9 = r2.getJSONObject(r5)
            java.lang.Object r9 = r8.execute(r9, r10)
            boolean r9 = r8.toBoolean(r9)
            r9 = r9 ^ r6
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r9)
            return r9
        L_0x0378:
            org.json.JSONObject r9 = r2.getJSONObject(r5)
            java.lang.Object r9 = r8.execute(r9, r10)
            com.alibaba.android.bindingx.core.internal.JSFunctionInterface r9 = (com.alibaba.android.bindingx.core.internal.JSFunctionInterface) r9
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            org.json.JSONObject r2 = r2.getJSONObject(r6)
            org.json.JSONArray r1 = r2.getJSONArray(r1)
        L_0x038f:
            int r2 = r1.length()
            if (r5 >= r2) goto L_0x03a3
            org.json.JSONObject r2 = r1.getJSONObject(r5)
            java.lang.Object r2 = r8.execute(r2, r10)     // Catch:{ all -> 0x03a8 }
            r0.add(r2)
            int r5 = r5 + 1
            goto L_0x038f
        L_0x03a3:
            java.lang.Object r9 = r9.execute(r0)
            return r9
        L_0x03a8:
            r9 = move-exception
            goto L_0x03ab
        L_0x03aa:
            throw r9
        L_0x03ab:
            goto L_0x03aa
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.bindingx.core.internal.Expression.execute(org.json.JSONObject, java.util.Map):java.lang.Object");
    }
}
