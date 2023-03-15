package com.dcloud.zxing2.client.result;

import com.taobao.weex.el.parse.Operators;

public final class ExpandedProductResultParser extends ResultParser {
    private static String findAIvalue(int i, String str) {
        if (str.charAt(i) != '(') {
            return null;
        }
        String substring = str.substring(i + 1);
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < substring.length(); i2++) {
            char charAt = substring.charAt(i2);
            if (charAt == ')') {
                return sb.toString();
            }
            if (charAt < '0' || charAt > '9') {
                return null;
            }
            sb.append(charAt);
        }
        return sb.toString();
    }

    private static String findValue(int i, String str) {
        StringBuilder sb = new StringBuilder();
        String substring = str.substring(i);
        for (int i2 = 0; i2 < substring.length(); i2++) {
            char charAt = substring.charAt(i2);
            if (charAt == '(') {
                if (findAIvalue(i2, substring) != null) {
                    break;
                }
                sb.append(Operators.BRACKET_START);
            } else {
                sb.append(charAt);
            }
        }
        return sb.toString();
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:124:0x0237, code lost:
        if (r1.equals("10") == false) goto L_0x0253;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:141:0x028f, code lost:
        r14 = r1;
        r13 = r12;
        r15 = r22;
        r12 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:148:0x02a6, code lost:
        r15 = r22;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:149:0x02a8, code lost:
        r14 = r23;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:151:0x02ae, code lost:
        r3 = r21;
        r2 = null;
     */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x0256  */
    /* JADX WARNING: Removed duplicated region for block: B:134:0x025b  */
    /* JADX WARNING: Removed duplicated region for block: B:138:0x0277  */
    /* JADX WARNING: Removed duplicated region for block: B:139:0x0280  */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x0288  */
    /* JADX WARNING: Removed duplicated region for block: B:142:0x0295  */
    /* JADX WARNING: Removed duplicated region for block: B:143:0x0298  */
    /* JADX WARNING: Removed duplicated region for block: B:144:0x029b  */
    /* JADX WARNING: Removed duplicated region for block: B:145:0x029e  */
    /* JADX WARNING: Removed duplicated region for block: B:146:0x02a1  */
    /* JADX WARNING: Removed duplicated region for block: B:147:0x02a4  */
    /* JADX WARNING: Removed duplicated region for block: B:150:0x02ab  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.dcloud.zxing2.client.result.ExpandedProductParsedResult parse(com.dcloud.zxing2.Result r25) {
        /*
            r24 = this;
            com.dcloud.zxing2.BarcodeFormat r0 = r25.getBarcodeFormat()
            com.dcloud.zxing2.BarcodeFormat r1 = com.dcloud.zxing2.BarcodeFormat.RSS_EXPANDED
            r2 = 0
            if (r0 == r1) goto L_0x000a
            return r2
        L_0x000a:
            java.lang.String r4 = com.dcloud.zxing2.client.result.ResultParser.getMassagedText(r25)
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            r5 = r2
            r6 = r5
            r7 = r6
            r8 = r7
            r9 = r8
            r10 = r9
            r11 = r10
            r12 = r11
            r13 = r12
            r14 = r13
            r15 = r14
            r16 = r15
            r17 = r16
            r3 = 0
        L_0x0023:
            int r1 = r4.length()
            if (r3 >= r1) goto L_0x02b3
            java.lang.String r1 = findAIvalue(r3, r4)
            if (r1 != 0) goto L_0x0030
            return r2
        L_0x0030:
            int r18 = r1.length()
            r19 = 2
            int r18 = r18 + 2
            int r3 = r3 + r18
            java.lang.String r2 = findValue(r3, r4)
            int r20 = r2.length()
            int r3 = r3 + r20
            r20 = -1
            r21 = r3
            int r3 = r1.hashCode()
            r22 = r15
            r15 = 1536(0x600, float:2.152E-42)
            r23 = r14
            r14 = 3
            if (r3 == r15) goto L_0x0246
            r15 = 1537(0x601, float:2.154E-42)
            if (r3 == r15) goto L_0x023a
            r15 = 1567(0x61f, float:2.196E-42)
            if (r3 == r15) goto L_0x0231
            r15 = 1568(0x620, float:2.197E-42)
            if (r3 == r15) goto L_0x0225
            r15 = 1570(0x622, float:2.2E-42)
            if (r3 == r15) goto L_0x0219
            r15 = 1572(0x624, float:2.203E-42)
            if (r3 == r15) goto L_0x020d
            r15 = 1574(0x626, float:2.206E-42)
            if (r3 == r15) goto L_0x0201
            switch(r3) {
                case 1567966: goto L_0x01f5;
                case 1567967: goto L_0x01e7;
                case 1567968: goto L_0x01d9;
                case 1567969: goto L_0x01cb;
                case 1567970: goto L_0x01bd;
                case 1567971: goto L_0x01af;
                case 1567972: goto L_0x01a1;
                case 1567973: goto L_0x0193;
                case 1567974: goto L_0x0185;
                case 1567975: goto L_0x0177;
                default: goto L_0x0070;
            }
        L_0x0070:
            switch(r3) {
                case 1568927: goto L_0x0169;
                case 1568928: goto L_0x015b;
                case 1568929: goto L_0x014d;
                case 1568930: goto L_0x013f;
                case 1568931: goto L_0x0131;
                case 1568932: goto L_0x0123;
                case 1568933: goto L_0x0115;
                case 1568934: goto L_0x0107;
                case 1568935: goto L_0x00f9;
                case 1568936: goto L_0x00eb;
                default: goto L_0x0073;
            }
        L_0x0073:
            switch(r3) {
                case 1575716: goto L_0x00dd;
                case 1575717: goto L_0x00cf;
                case 1575718: goto L_0x00c1;
                case 1575719: goto L_0x00b3;
                default: goto L_0x0076;
            }
        L_0x0076:
            switch(r3) {
                case 1575747: goto L_0x00a5;
                case 1575748: goto L_0x0097;
                case 1575749: goto L_0x0089;
                case 1575750: goto L_0x007b;
                default: goto L_0x0079;
            }
        L_0x0079:
            goto L_0x0253
        L_0x007b:
            java.lang.String r3 = "3933"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x0085
            goto L_0x0253
        L_0x0085:
            r19 = 34
            goto L_0x0251
        L_0x0089:
            java.lang.String r3 = "3932"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x0093
            goto L_0x0253
        L_0x0093:
            r19 = 33
            goto L_0x0251
        L_0x0097:
            java.lang.String r3 = "3931"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x00a1
            goto L_0x0253
        L_0x00a1:
            r19 = 32
            goto L_0x0251
        L_0x00a5:
            java.lang.String r3 = "3930"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x00af
            goto L_0x0253
        L_0x00af:
            r19 = 31
            goto L_0x0251
        L_0x00b3:
            java.lang.String r3 = "3923"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x00bd
            goto L_0x0253
        L_0x00bd:
            r19 = 30
            goto L_0x0251
        L_0x00c1:
            java.lang.String r3 = "3922"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x00cb
            goto L_0x0253
        L_0x00cb:
            r19 = 29
            goto L_0x0251
        L_0x00cf:
            java.lang.String r3 = "3921"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x00d9
            goto L_0x0253
        L_0x00d9:
            r19 = 28
            goto L_0x0251
        L_0x00dd:
            java.lang.String r3 = "3920"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x00e7
            goto L_0x0253
        L_0x00e7:
            r19 = 27
            goto L_0x0251
        L_0x00eb:
            java.lang.String r3 = "3209"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x00f5
            goto L_0x0253
        L_0x00f5:
            r19 = 26
            goto L_0x0251
        L_0x00f9:
            java.lang.String r3 = "3208"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x0103
            goto L_0x0253
        L_0x0103:
            r19 = 25
            goto L_0x0251
        L_0x0107:
            java.lang.String r3 = "3207"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x0111
            goto L_0x0253
        L_0x0111:
            r19 = 24
            goto L_0x0251
        L_0x0115:
            java.lang.String r3 = "3206"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x011f
            goto L_0x0253
        L_0x011f:
            r19 = 23
            goto L_0x0251
        L_0x0123:
            java.lang.String r3 = "3205"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x012d
            goto L_0x0253
        L_0x012d:
            r19 = 22
            goto L_0x0251
        L_0x0131:
            java.lang.String r3 = "3204"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x013b
            goto L_0x0253
        L_0x013b:
            r19 = 21
            goto L_0x0251
        L_0x013f:
            java.lang.String r3 = "3203"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x0149
            goto L_0x0253
        L_0x0149:
            r19 = 20
            goto L_0x0251
        L_0x014d:
            java.lang.String r3 = "3202"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x0157
            goto L_0x0253
        L_0x0157:
            r19 = 19
            goto L_0x0251
        L_0x015b:
            java.lang.String r3 = "3201"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x0165
            goto L_0x0253
        L_0x0165:
            r19 = 18
            goto L_0x0251
        L_0x0169:
            java.lang.String r3 = "3200"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x0173
            goto L_0x0253
        L_0x0173:
            r19 = 17
            goto L_0x0251
        L_0x0177:
            java.lang.String r3 = "3109"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x0181
            goto L_0x0253
        L_0x0181:
            r19 = 16
            goto L_0x0251
        L_0x0185:
            java.lang.String r3 = "3108"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x018f
            goto L_0x0253
        L_0x018f:
            r19 = 15
            goto L_0x0251
        L_0x0193:
            java.lang.String r3 = "3107"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x019d
            goto L_0x0253
        L_0x019d:
            r19 = 14
            goto L_0x0251
        L_0x01a1:
            java.lang.String r3 = "3106"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x01ab
            goto L_0x0253
        L_0x01ab:
            r19 = 13
            goto L_0x0251
        L_0x01af:
            java.lang.String r3 = "3105"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x01b9
            goto L_0x0253
        L_0x01b9:
            r19 = 12
            goto L_0x0251
        L_0x01bd:
            java.lang.String r3 = "3104"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x01c7
            goto L_0x0253
        L_0x01c7:
            r19 = 11
            goto L_0x0251
        L_0x01cb:
            java.lang.String r3 = "3103"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x01d5
            goto L_0x0253
        L_0x01d5:
            r19 = 10
            goto L_0x0251
        L_0x01d9:
            java.lang.String r3 = "3102"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x01e3
            goto L_0x0253
        L_0x01e3:
            r19 = 9
            goto L_0x0251
        L_0x01e7:
            java.lang.String r3 = "3101"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x01f1
            goto L_0x0253
        L_0x01f1:
            r19 = 8
            goto L_0x0251
        L_0x01f5:
            java.lang.String r3 = "3100"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x01fe
            goto L_0x0253
        L_0x01fe:
            r19 = 7
            goto L_0x0251
        L_0x0201:
            java.lang.String r3 = "17"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x020a
            goto L_0x0253
        L_0x020a:
            r19 = 6
            goto L_0x0251
        L_0x020d:
            java.lang.String r3 = "15"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x0216
            goto L_0x0253
        L_0x0216:
            r19 = 5
            goto L_0x0251
        L_0x0219:
            java.lang.String r3 = "13"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x0222
            goto L_0x0253
        L_0x0222:
            r19 = 4
            goto L_0x0251
        L_0x0225:
            java.lang.String r3 = "11"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x022e
            goto L_0x0253
        L_0x022e:
            r19 = 3
            goto L_0x0251
        L_0x0231:
            java.lang.String r3 = "10"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x0251
            goto L_0x0253
        L_0x023a:
            java.lang.String r3 = "01"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x0243
            goto L_0x0253
        L_0x0243:
            r19 = 1
            goto L_0x0251
        L_0x0246:
            java.lang.String r3 = "00"
            boolean r3 = r1.equals(r3)
            if (r3 != 0) goto L_0x024f
            goto L_0x0253
        L_0x024f:
            r19 = 0
        L_0x0251:
            r20 = r19
        L_0x0253:
            switch(r20) {
                case 0: goto L_0x02ab;
                case 1: goto L_0x02a4;
                case 2: goto L_0x02a1;
                case 3: goto L_0x029e;
                case 4: goto L_0x029b;
                case 5: goto L_0x0298;
                case 6: goto L_0x0295;
                case 7: goto L_0x0288;
                case 8: goto L_0x0288;
                case 9: goto L_0x0288;
                case 10: goto L_0x0288;
                case 11: goto L_0x0288;
                case 12: goto L_0x0288;
                case 13: goto L_0x0288;
                case 14: goto L_0x0288;
                case 15: goto L_0x0288;
                case 16: goto L_0x0288;
                case 17: goto L_0x0280;
                case 18: goto L_0x0280;
                case 19: goto L_0x0280;
                case 20: goto L_0x0280;
                case 21: goto L_0x0280;
                case 22: goto L_0x0280;
                case 23: goto L_0x0280;
                case 24: goto L_0x0280;
                case 25: goto L_0x0280;
                case 26: goto L_0x0280;
                case 27: goto L_0x0277;
                case 28: goto L_0x0277;
                case 29: goto L_0x0277;
                case 30: goto L_0x0277;
                case 31: goto L_0x025b;
                case 32: goto L_0x025b;
                case 33: goto L_0x025b;
                case 34: goto L_0x025b;
                default: goto L_0x0256;
            }
        L_0x0256:
            r3 = 0
            r0.put(r1, r2)
            goto L_0x02a6
        L_0x025b:
            int r3 = r2.length()
            r15 = 4
            if (r3 >= r15) goto L_0x0264
            r3 = 0
            return r3
        L_0x0264:
            r3 = 0
            java.lang.String r15 = r2.substring(r14)
            r3 = 0
            java.lang.String r2 = r2.substring(r3, r14)
            java.lang.String r1 = r1.substring(r14)
            r16 = r1
            r17 = r2
            goto L_0x02a8
        L_0x0277:
            r3 = 0
            java.lang.String r1 = r1.substring(r14)
            r16 = r1
            r15 = r2
            goto L_0x02a8
        L_0x0280:
            r3 = 0
            java.lang.String r1 = r1.substring(r14)
            java.lang.String r12 = "LB"
            goto L_0x028f
        L_0x0288:
            r3 = 0
            java.lang.String r1 = r1.substring(r14)
            java.lang.String r12 = "KG"
        L_0x028f:
            r14 = r1
            r13 = r12
            r15 = r22
            r12 = r2
            goto L_0x02ae
        L_0x0295:
            r3 = 0
            r11 = r2
            goto L_0x02a6
        L_0x0298:
            r3 = 0
            r10 = r2
            goto L_0x02a6
        L_0x029b:
            r3 = 0
            r9 = r2
            goto L_0x02a6
        L_0x029e:
            r3 = 0
            r8 = r2
            goto L_0x02a6
        L_0x02a1:
            r3 = 0
            r7 = r2
            goto L_0x02a6
        L_0x02a4:
            r3 = 0
            r5 = r2
        L_0x02a6:
            r15 = r22
        L_0x02a8:
            r14 = r23
            goto L_0x02ae
        L_0x02ab:
            r3 = 0
            r6 = r2
            goto L_0x02a6
        L_0x02ae:
            r3 = r21
            r2 = 0
            goto L_0x0023
        L_0x02b3:
            r23 = r14
            r22 = r15
            com.dcloud.zxing2.client.result.ExpandedProductParsedResult r1 = new com.dcloud.zxing2.client.result.ExpandedProductParsedResult
            r3 = r1
            r18 = r0
            r3.<init>(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dcloud.zxing2.client.result.ExpandedProductResultParser.parse(com.dcloud.zxing2.Result):com.dcloud.zxing2.client.result.ExpandedProductParsedResult");
    }
}
