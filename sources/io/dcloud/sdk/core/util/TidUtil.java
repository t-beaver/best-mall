package io.dcloud.sdk.core.util;

public class TidUtil {
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:112:0x0183, code lost:
        if (r0.equals(io.dcloud.sdk.core.util.Const.TYPE_GDT) == false) goto L_0x017d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0041, code lost:
        if (r0.equals(io.dcloud.sdk.core.util.Const.TYPE_GDT) == false) goto L_0x003b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x00e9, code lost:
        if (r0.equals(io.dcloud.sdk.core.util.Const.TYPE_GDT) == false) goto L_0x00e3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x0142, code lost:
        if (r0.equals(io.dcloud.sdk.core.util.Const.TYPE_GDT) == false) goto L_0x013c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getTid(java.lang.String r16, int r17) {
        /*
            r0 = r16
            r1 = r17
            java.lang.String r3 = "sgm"
            r4 = 5
            java.lang.String r5 = "hw"
            java.lang.String r6 = "bd"
            r7 = 4
            java.lang.String r8 = "gdt"
            java.lang.String r9 = "csj"
            r10 = 3
            java.lang.String r11 = "ks"
            r12 = 2
            java.lang.String r13 = "gm"
            r14 = 0
            r15 = -1
            r2 = 1
            if (r1 == r2) goto L_0x01c9
            if (r1 == r7) goto L_0x0170
            r2 = 7
            if (r1 == r2) goto L_0x012f
            r2 = 15
            if (r1 == r2) goto L_0x00d6
            r2 = 9
            if (r1 == r2) goto L_0x006f
            r2 = 10
            if (r1 == r2) goto L_0x002e
            goto L_0x0219
        L_0x002e:
            r16.hashCode()
            r16.hashCode()
            int r1 = r16.hashCode()
            switch(r1) {
                case 3302: goto L_0x0056;
                case 3432: goto L_0x004d;
                case 98810: goto L_0x0044;
                case 102199: goto L_0x003d;
                default: goto L_0x003b;
            }
        L_0x003b:
            r10 = -1
            goto L_0x005e
        L_0x003d:
            boolean r0 = r0.equals(r8)
            if (r0 != 0) goto L_0x005e
            goto L_0x003b
        L_0x0044:
            boolean r0 = r0.equals(r9)
            if (r0 != 0) goto L_0x004b
            goto L_0x003b
        L_0x004b:
            r10 = 2
            goto L_0x005e
        L_0x004d:
            boolean r0 = r0.equals(r11)
            if (r0 != 0) goto L_0x0054
            goto L_0x003b
        L_0x0054:
            r10 = 1
            goto L_0x005e
        L_0x0056:
            boolean r0 = r0.equals(r13)
            if (r0 != 0) goto L_0x005d
            goto L_0x003b
        L_0x005d:
            r10 = 0
        L_0x005e:
            switch(r10) {
                case 0: goto L_0x006c;
                case 1: goto L_0x0069;
                case 2: goto L_0x0066;
                case 3: goto L_0x0063;
                default: goto L_0x0061;
            }
        L_0x0061:
            goto L_0x0219
        L_0x0063:
            java.lang.String r0 = "103"
            return r0
        L_0x0066:
            java.lang.String r0 = "104"
            return r0
        L_0x0069:
            java.lang.String r0 = "105"
            return r0
        L_0x006c:
            java.lang.String r0 = "106"
            return r0
        L_0x006f:
            r16.hashCode()
            r16.hashCode()
            int r1 = r16.hashCode()
            switch(r1) {
                case 3138: goto L_0x00b4;
                case 3302: goto L_0x00ab;
                case 3343: goto L_0x00a2;
                case 3432: goto L_0x0099;
                case 98810: goto L_0x0090;
                case 102199: goto L_0x0087;
                case 113817: goto L_0x007e;
                default: goto L_0x007c;
            }
        L_0x007c:
            r2 = -1
            goto L_0x00bc
        L_0x007e:
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x0085
            goto L_0x007c
        L_0x0085:
            r2 = 6
            goto L_0x00bc
        L_0x0087:
            boolean r0 = r0.equals(r8)
            if (r0 != 0) goto L_0x008e
            goto L_0x007c
        L_0x008e:
            r2 = 5
            goto L_0x00bc
        L_0x0090:
            boolean r0 = r0.equals(r9)
            if (r0 != 0) goto L_0x0097
            goto L_0x007c
        L_0x0097:
            r2 = 4
            goto L_0x00bc
        L_0x0099:
            boolean r0 = r0.equals(r11)
            if (r0 != 0) goto L_0x00a0
            goto L_0x007c
        L_0x00a0:
            r2 = 3
            goto L_0x00bc
        L_0x00a2:
            boolean r0 = r0.equals(r5)
            if (r0 != 0) goto L_0x00a9
            goto L_0x007c
        L_0x00a9:
            r2 = 2
            goto L_0x00bc
        L_0x00ab:
            boolean r0 = r0.equals(r13)
            if (r0 != 0) goto L_0x00b2
            goto L_0x007c
        L_0x00b2:
            r2 = 1
            goto L_0x00bc
        L_0x00b4:
            boolean r0 = r0.equals(r6)
            if (r0 != 0) goto L_0x00bb
            goto L_0x007c
        L_0x00bb:
            r2 = 0
        L_0x00bc:
            switch(r2) {
                case 0: goto L_0x00d3;
                case 1: goto L_0x00d0;
                case 2: goto L_0x00cd;
                case 3: goto L_0x00ca;
                case 4: goto L_0x00c7;
                case 5: goto L_0x00c4;
                case 6: goto L_0x00c1;
                default: goto L_0x00bf;
            }
        L_0x00bf:
            goto L_0x0219
        L_0x00c1:
            java.lang.String r0 = "86"
            return r0
        L_0x00c4:
            java.lang.String r0 = "83"
            return r0
        L_0x00c7:
            java.lang.String r0 = "84"
            return r0
        L_0x00ca:
            java.lang.String r0 = "85"
            return r0
        L_0x00cd:
            java.lang.String r0 = "87"
            return r0
        L_0x00d0:
            java.lang.String r0 = "89"
            return r0
        L_0x00d3:
            java.lang.String r0 = "88"
            return r0
        L_0x00d6:
            r16.hashCode()
            r16.hashCode()
            int r1 = r16.hashCode()
            switch(r1) {
                case 3138: goto L_0x0110;
                case 3302: goto L_0x0107;
                case 3343: goto L_0x00fe;
                case 3432: goto L_0x00f5;
                case 98810: goto L_0x00ec;
                case 102199: goto L_0x00e5;
                default: goto L_0x00e3;
            }
        L_0x00e3:
            r4 = -1
            goto L_0x0118
        L_0x00e5:
            boolean r0 = r0.equals(r8)
            if (r0 != 0) goto L_0x0118
            goto L_0x00e3
        L_0x00ec:
            boolean r0 = r0.equals(r9)
            if (r0 != 0) goto L_0x00f3
            goto L_0x00e3
        L_0x00f3:
            r4 = 4
            goto L_0x0118
        L_0x00f5:
            boolean r0 = r0.equals(r11)
            if (r0 != 0) goto L_0x00fc
            goto L_0x00e3
        L_0x00fc:
            r4 = 3
            goto L_0x0118
        L_0x00fe:
            boolean r0 = r0.equals(r5)
            if (r0 != 0) goto L_0x0105
            goto L_0x00e3
        L_0x0105:
            r4 = 2
            goto L_0x0118
        L_0x0107:
            boolean r0 = r0.equals(r13)
            if (r0 != 0) goto L_0x010e
            goto L_0x00e3
        L_0x010e:
            r4 = 1
            goto L_0x0118
        L_0x0110:
            boolean r0 = r0.equals(r6)
            if (r0 != 0) goto L_0x0117
            goto L_0x00e3
        L_0x0117:
            r4 = 0
        L_0x0118:
            switch(r4) {
                case 0: goto L_0x012c;
                case 1: goto L_0x0129;
                case 2: goto L_0x0126;
                case 3: goto L_0x0123;
                case 4: goto L_0x0120;
                case 5: goto L_0x011d;
                default: goto L_0x011b;
            }
        L_0x011b:
            goto L_0x0219
        L_0x011d:
            java.lang.String r0 = "123"
            return r0
        L_0x0120:
            java.lang.String r0 = "124"
            return r0
        L_0x0123:
            java.lang.String r0 = "125"
            return r0
        L_0x0126:
            java.lang.String r0 = "126"
            return r0
        L_0x0129:
            java.lang.String r0 = "128"
            return r0
        L_0x012c:
            java.lang.String r0 = "127"
            return r0
        L_0x012f:
            r16.hashCode()
            r16.hashCode()
            int r1 = r16.hashCode()
            switch(r1) {
                case 3302: goto L_0x0157;
                case 3432: goto L_0x014e;
                case 98810: goto L_0x0145;
                case 102199: goto L_0x013e;
                default: goto L_0x013c;
            }
        L_0x013c:
            r10 = -1
            goto L_0x015f
        L_0x013e:
            boolean r0 = r0.equals(r8)
            if (r0 != 0) goto L_0x015f
            goto L_0x013c
        L_0x0145:
            boolean r0 = r0.equals(r9)
            if (r0 != 0) goto L_0x014c
            goto L_0x013c
        L_0x014c:
            r10 = 2
            goto L_0x015f
        L_0x014e:
            boolean r0 = r0.equals(r11)
            if (r0 != 0) goto L_0x0155
            goto L_0x013c
        L_0x0155:
            r10 = 1
            goto L_0x015f
        L_0x0157:
            boolean r0 = r0.equals(r13)
            if (r0 != 0) goto L_0x015e
            goto L_0x013c
        L_0x015e:
            r10 = 0
        L_0x015f:
            switch(r10) {
                case 0: goto L_0x016d;
                case 1: goto L_0x016a;
                case 2: goto L_0x0167;
                case 3: goto L_0x0164;
                default: goto L_0x0162;
            }
        L_0x0162:
            goto L_0x0219
        L_0x0164:
            java.lang.String r0 = "93"
            return r0
        L_0x0167:
            java.lang.String r0 = "94"
            return r0
        L_0x016a:
            java.lang.String r0 = "95"
            return r0
        L_0x016d:
            java.lang.String r0 = "96"
            return r0
        L_0x0170:
            r16.hashCode()
            r16.hashCode()
            int r1 = r16.hashCode()
            switch(r1) {
                case 3138: goto L_0x01aa;
                case 3302: goto L_0x01a1;
                case 3343: goto L_0x0198;
                case 3432: goto L_0x018f;
                case 98810: goto L_0x0186;
                case 102199: goto L_0x017f;
                default: goto L_0x017d;
            }
        L_0x017d:
            r4 = -1
            goto L_0x01b2
        L_0x017f:
            boolean r0 = r0.equals(r8)
            if (r0 != 0) goto L_0x01b2
            goto L_0x017d
        L_0x0186:
            boolean r0 = r0.equals(r9)
            if (r0 != 0) goto L_0x018d
            goto L_0x017d
        L_0x018d:
            r4 = 4
            goto L_0x01b2
        L_0x018f:
            boolean r0 = r0.equals(r11)
            if (r0 != 0) goto L_0x0196
            goto L_0x017d
        L_0x0196:
            r4 = 3
            goto L_0x01b2
        L_0x0198:
            boolean r0 = r0.equals(r5)
            if (r0 != 0) goto L_0x019f
            goto L_0x017d
        L_0x019f:
            r4 = 2
            goto L_0x01b2
        L_0x01a1:
            boolean r0 = r0.equals(r13)
            if (r0 != 0) goto L_0x01a8
            goto L_0x017d
        L_0x01a8:
            r4 = 1
            goto L_0x01b2
        L_0x01aa:
            boolean r0 = r0.equals(r6)
            if (r0 != 0) goto L_0x01b1
            goto L_0x017d
        L_0x01b1:
            r4 = 0
        L_0x01b2:
            switch(r4) {
                case 0: goto L_0x01c6;
                case 1: goto L_0x01c3;
                case 2: goto L_0x01c0;
                case 3: goto L_0x01bd;
                case 4: goto L_0x01ba;
                case 5: goto L_0x01b7;
                default: goto L_0x01b5;
            }
        L_0x01b5:
            goto L_0x0219
        L_0x01b7:
            java.lang.String r0 = "72"
            return r0
        L_0x01ba:
            java.lang.String r0 = "73"
            return r0
        L_0x01bd:
            java.lang.String r0 = "76"
            return r0
        L_0x01c0:
            java.lang.String r0 = "77"
            return r0
        L_0x01c3:
            java.lang.String r0 = "79"
            return r0
        L_0x01c6:
            java.lang.String r0 = "78"
            return r0
        L_0x01c9:
            r16.hashCode()
            r16.hashCode()
            int r1 = r16.hashCode()
            switch(r1) {
                case 3138: goto L_0x020e;
                case 3302: goto L_0x0205;
                case 3343: goto L_0x01fc;
                case 3432: goto L_0x01f3;
                case 98810: goto L_0x01ea;
                case 102199: goto L_0x01e1;
                case 113817: goto L_0x01d8;
                default: goto L_0x01d6;
            }
        L_0x01d6:
            r2 = -1
            goto L_0x0216
        L_0x01d8:
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x01df
            goto L_0x01d6
        L_0x01df:
            r2 = 6
            goto L_0x0216
        L_0x01e1:
            boolean r0 = r0.equals(r8)
            if (r0 != 0) goto L_0x01e8
            goto L_0x01d6
        L_0x01e8:
            r2 = 5
            goto L_0x0216
        L_0x01ea:
            boolean r0 = r0.equals(r9)
            if (r0 != 0) goto L_0x01f1
            goto L_0x01d6
        L_0x01f1:
            r2 = 4
            goto L_0x0216
        L_0x01f3:
            boolean r0 = r0.equals(r11)
            if (r0 != 0) goto L_0x01fa
            goto L_0x01d6
        L_0x01fa:
            r2 = 3
            goto L_0x0216
        L_0x01fc:
            boolean r0 = r0.equals(r5)
            if (r0 != 0) goto L_0x0203
            goto L_0x01d6
        L_0x0203:
            r2 = 2
            goto L_0x0216
        L_0x0205:
            boolean r0 = r0.equals(r13)
            if (r0 != 0) goto L_0x020c
            goto L_0x01d6
        L_0x020c:
            r2 = 1
            goto L_0x0216
        L_0x020e:
            boolean r0 = r0.equals(r6)
            if (r0 != 0) goto L_0x0215
            goto L_0x01d6
        L_0x0215:
            r2 = 0
        L_0x0216:
            switch(r2) {
                case 0: goto L_0x022e;
                case 1: goto L_0x022b;
                case 2: goto L_0x0228;
                case 3: goto L_0x0225;
                case 4: goto L_0x0222;
                case 5: goto L_0x021f;
                case 6: goto L_0x021c;
                default: goto L_0x0219;
            }
        L_0x0219:
            java.lang.String r0 = ""
            return r0
        L_0x021c:
            java.lang.String r0 = "110"
            return r0
        L_0x021f:
            java.lang.String r0 = "67"
            return r0
        L_0x0222:
            java.lang.String r0 = "68"
            return r0
        L_0x0225:
            java.lang.String r0 = "75"
            return r0
        L_0x0228:
            java.lang.String r0 = "130"
            return r0
        L_0x022b:
            java.lang.String r0 = "132"
            return r0
        L_0x022e:
            java.lang.String r0 = "131"
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.sdk.core.util.TidUtil.getTid(java.lang.String, int):java.lang.String");
    }
}
