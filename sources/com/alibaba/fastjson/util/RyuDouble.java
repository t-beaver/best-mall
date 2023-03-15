package com.alibaba.fastjson.util;

import java.lang.reflect.Array;
import java.math.BigInteger;

public final class RyuDouble {
    private static final int[][] POW5_INV_SPLIT;
    private static final int[][] POW5_SPLIT;

    static {
        int i;
        Class<int> cls = int.class;
        POW5_SPLIT = (int[][]) Array.newInstance(cls, new int[]{326, 4});
        POW5_INV_SPLIT = (int[][]) Array.newInstance(cls, new int[]{291, 4});
        BigInteger subtract = BigInteger.ONE.shiftLeft(31).subtract(BigInteger.ONE);
        BigInteger subtract2 = BigInteger.ONE.shiftLeft(31).subtract(BigInteger.ONE);
        int i2 = 0;
        while (i2 < 326) {
            BigInteger pow = BigInteger.valueOf(5).pow(i2);
            int bitLength = pow.bitLength();
            if (i2 == 0) {
                i = 1;
            } else {
                i = (int) ((((((long) i2) * 23219280) + 10000000) - 1) / 10000000);
            }
            if (i == bitLength) {
                if (i2 < POW5_SPLIT.length) {
                    for (int i3 = 0; i3 < 4; i3++) {
                        POW5_SPLIT[i2][i3] = pow.shiftRight((bitLength - 121) + ((3 - i3) * 31)).and(subtract).intValue();
                    }
                }
                if (i2 < POW5_INV_SPLIT.length) {
                    BigInteger add = BigInteger.ONE.shiftLeft(bitLength + 121).divide(pow).add(BigInteger.ONE);
                    for (int i4 = 0; i4 < 4; i4++) {
                        if (i4 == 0) {
                            POW5_INV_SPLIT[i2][i4] = add.shiftRight((3 - i4) * 31).intValue();
                        } else {
                            POW5_INV_SPLIT[i2][i4] = add.shiftRight((3 - i4) * 31).and(subtract2).intValue();
                        }
                    }
                }
                i2++;
            } else {
                throw new IllegalStateException(bitLength + " != " + i);
            }
        }
    }

    public static String toString(double d) {
        char[] cArr = new char[24];
        return new String(cArr, 0, toString(d, cArr, 0));
    }

    /* JADX WARNING: Removed duplicated region for block: B:238:0x05bb  */
    /* JADX WARNING: Removed duplicated region for block: B:239:0x05bd  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int toString(double r43, char[] r45, int r46) {
        /*
            boolean r0 = java.lang.Double.isNaN(r43)
            if (r0 == 0) goto L_0x001b
            int r0 = r46 + 1
            r1 = 78
            r45[r46] = r1
            int r1 = r0 + 1
            r2 = 97
            r45[r0] = r2
            int r0 = r1 + 1
            r2 = 78
            r45[r1] = r2
        L_0x0018:
            int r0 = r0 - r46
            return r0
        L_0x001b:
            r0 = 9218868437227405312(0x7ff0000000000000, double:Infinity)
            r2 = 105(0x69, float:1.47E-43)
            r3 = 110(0x6e, float:1.54E-43)
            int r4 = (r43 > r0 ? 1 : (r43 == r0 ? 0 : -1))
            if (r4 != 0) goto L_0x0050
            int r0 = r46 + 1
            r1 = 73
            r45[r46] = r1
            int r1 = r0 + 1
            r45[r0] = r3
            int r0 = r1 + 1
            r4 = 102(0x66, float:1.43E-43)
            r45[r1] = r4
            int r1 = r0 + 1
            r45[r0] = r2
            int r0 = r1 + 1
            r45[r1] = r3
            int r1 = r0 + 1
            r45[r0] = r2
            int r0 = r1 + 1
            r2 = 116(0x74, float:1.63E-43)
            r45[r1] = r2
            int r1 = r0 + 1
            r2 = 121(0x79, float:1.7E-43)
            r45[r0] = r2
        L_0x004d:
            int r1 = r1 - r46
            return r1
        L_0x0050:
            r0 = -4503599627370496(0xfff0000000000000, double:-Infinity)
            r4 = 45
            int r5 = (r43 > r0 ? 1 : (r43 == r0 ? 0 : -1))
            if (r5 != 0) goto L_0x0085
            int r0 = r46 + 1
            r45[r46] = r4
            int r1 = r0 + 1
            r4 = 73
            r45[r0] = r4
            int r0 = r1 + 1
            r45[r1] = r3
            int r1 = r0 + 1
            r4 = 102(0x66, float:1.43E-43)
            r45[r0] = r4
            int r0 = r1 + 1
            r45[r1] = r2
            int r1 = r0 + 1
            r45[r0] = r3
            int r0 = r1 + 1
            r45[r1] = r2
            int r1 = r0 + 1
            r2 = 116(0x74, float:1.63E-43)
            r45[r0] = r2
            int r0 = r1 + 1
            r2 = 121(0x79, float:1.7E-43)
            r45[r1] = r2
            goto L_0x0018
        L_0x0085:
            long r0 = java.lang.Double.doubleToLongBits(r43)
            r2 = 46
            r3 = 48
            r5 = 0
            int r7 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
            if (r7 != 0) goto L_0x00a1
            int r0 = r46 + 1
            r45[r46] = r3
            int r1 = r0 + 1
            r45[r0] = r2
            int r0 = r1 + 1
            r45[r1] = r3
            goto L_0x0018
        L_0x00a1:
            r7 = -9223372036854775808
            int r9 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1))
            if (r9 != 0) goto L_0x00b8
            int r0 = r46 + 1
            r45[r46] = r4
            int r1 = r0 + 1
            r45[r0] = r3
            int r0 = r1 + 1
            r45[r1] = r2
            int r1 = r0 + 1
            r45[r0] = r3
            goto L_0x004d
        L_0x00b8:
            r7 = 52
            long r7 = r0 >>> r7
            r9 = 2047(0x7ff, double:1.0114E-320)
            long r7 = r7 & r9
            int r8 = (int) r7
            r9 = 4503599627370495(0xfffffffffffff, double:2.225073858507201E-308)
            long r9 = r9 & r0
            if (r8 != 0) goto L_0x00cb
            r7 = -1074(0xfffffffffffffbce, float:NaN)
            goto L_0x00d2
        L_0x00cb:
            int r7 = r8 + -1023
            int r7 = r7 + -52
            r11 = 4503599627370496(0x10000000000000, double:2.2250738585072014E-308)
            long r9 = r9 | r11
        L_0x00d2:
            r11 = 0
            r12 = 1
            int r13 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
            if (r13 >= 0) goto L_0x00da
            r0 = 1
            goto L_0x00db
        L_0x00da:
            r0 = 0
        L_0x00db:
            r13 = 1
            long r15 = r9 & r13
            int r1 = (r15 > r5 ? 1 : (r15 == r5 ? 0 : -1))
            if (r1 != 0) goto L_0x00e5
            r1 = 1
            goto L_0x00e6
        L_0x00e5:
            r1 = 0
        L_0x00e6:
            r15 = 4
            long r15 = r15 * r9
            r17 = 2
            long r17 = r15 + r17
            r19 = 4503599627370496(0x10000000000000, double:2.2250738585072014E-308)
            int r21 = (r9 > r19 ? 1 : (r9 == r19 ? 0 : -1))
            if (r21 != 0) goto L_0x00f9
            if (r8 > r12) goto L_0x00f7
            goto L_0x00f9
        L_0x00f7:
            r8 = 0
            goto L_0x00fa
        L_0x00f9:
            r8 = 1
        L_0x00fa:
            long r9 = r15 - r13
            long r2 = (long) r8
            long r9 = r9 - r2
            int r7 = r7 + -2
            r19 = 2147483647(0x7fffffff, double:1.060997895E-314)
            r21 = 10000000(0x989680, double:4.9406565E-317)
            r23 = 3
            r24 = 2
            r25 = 31
            if (r7 < 0) goto L_0x031f
            long r4 = (long) r7
            r26 = 3010299(0x2deefb, double:1.4872853E-317)
            long r4 = r4 * r26
            long r4 = r4 / r21
            int r5 = (int) r4
            int r5 = r5 - r12
            int r4 = java.lang.Math.max(r11, r5)
            if (r4 != 0) goto L_0x0120
            r3 = 1
            goto L_0x012c
        L_0x0120:
            long r2 = (long) r4
            r27 = 23219280(0x1624c50, double:1.14718486E-316)
            long r2 = r2 * r27
            long r2 = r2 + r21
            long r2 = r2 - r13
            long r2 = r2 / r21
            int r3 = (int) r2
        L_0x012c:
            int r3 = r3 + 122
            int r3 = r3 - r12
            int r2 = -r7
            int r2 = r2 + r4
            int r2 = r2 + r3
            int r2 = r2 + -93
            r3 = 21
            int r2 = r2 - r3
            if (r2 < 0) goto L_0x0304
            int[][] r3 = POW5_INV_SPLIT
            r3 = r3[r4]
            long r7 = r15 >>> r25
            long r27 = r15 & r19
            r5 = r3[r11]
            long r13 = (long) r5
            long r13 = r13 * r7
            r5 = r3[r11]
            r29 = r7
            long r6 = (long) r5
            long r6 = r6 * r27
            r5 = r3[r12]
            r31 = r0
            r32 = r1
            long r0 = (long) r5
            long r0 = r0 * r29
            r5 = r3[r12]
            long r11 = (long) r5
            long r11 = r11 * r27
            r5 = r3[r24]
            r8 = r4
            long r4 = (long) r5
            long r4 = r4 * r29
            r34 = r15
            r15 = r3[r24]
            r36 = r9
            r10 = r8
            long r8 = (long) r15
            long r8 = r8 * r27
            r15 = r3[r23]
            r38 = r13
            long r13 = (long) r15
            long r13 = r13 * r29
            r15 = r3[r23]
            r16 = r2
            r29 = r3
            long r2 = (long) r15
            long r27 = r27 * r2
            long r2 = r27 >>> r25
            long r2 = r2 + r8
            long r2 = r2 + r13
            long r2 = r2 >>> r25
            long r2 = r2 + r11
            long r2 = r2 + r4
            long r2 = r2 >>> r25
            long r2 = r2 + r6
            long r2 = r2 + r0
            r0 = 21
            long r1 = r2 >>> r0
            r0 = 10
            long r3 = r38 << r0
            long r1 = r1 + r3
            long r0 = r1 >>> r16
            long r2 = r17 >>> r25
            long r6 = r17 & r19
            r4 = 0
            r8 = r29[r4]
            long r8 = (long) r8
            long r8 = r8 * r2
            r11 = r29[r4]
            long r11 = (long) r11
            long r11 = r11 * r6
            r4 = 1
            r13 = r29[r4]
            long r13 = (long) r13
            long r13 = r13 * r2
            r15 = r29[r4]
            r27 = r0
            long r0 = (long) r15
            long r0 = r0 * r6
            r4 = r29[r24]
            r38 = r6
            long r5 = (long) r4
            long r5 = r5 * r2
            r4 = r29[r24]
            r40 = r8
            long r7 = (long) r4
            long r7 = r7 * r38
            r4 = r29[r23]
            r15 = r10
            long r9 = (long) r4
            long r2 = r2 * r9
            r4 = r29[r23]
            long r9 = (long) r4
            long r9 = r9 * r38
            long r9 = r9 >>> r25
            long r9 = r9 + r7
            long r9 = r9 + r2
            long r2 = r9 >>> r25
            long r2 = r2 + r0
            long r2 = r2 + r5
            long r0 = r2 >>> r25
            long r0 = r0 + r11
            long r0 = r0 + r13
            r2 = 21
            long r0 = r0 >>> r2
            r2 = 10
            long r3 = r40 << r2
            long r0 = r0 + r3
            long r0 = r0 >>> r16
            long r2 = r36 >>> r25
            long r6 = r36 & r19
            r4 = 0
            r8 = r29[r4]
            long r8 = (long) r8
            long r8 = r8 * r2
            r10 = r29[r4]
            long r10 = (long) r10
            long r10 = r10 * r6
            r4 = 1
            r12 = r29[r4]
            long r12 = (long) r12
            long r12 = r12 * r2
            r14 = r29[r4]
            long r4 = (long) r14
            long r4 = r4 * r6
            r14 = r29[r24]
            r30 = r15
            long r14 = (long) r14
            long r14 = r14 * r2
            r38 = r0
            r0 = r29[r24]
            long r0 = (long) r0
            long r0 = r0 * r6
            r19 = r8
            r8 = r29[r23]
            long r8 = (long) r8
            long r2 = r2 * r8
            r8 = r29[r23]
            long r8 = (long) r8
            long r6 = r6 * r8
            long r6 = r6 >>> r25
            long r6 = r6 + r0
            long r6 = r6 + r2
            long r0 = r6 >>> r25
            long r0 = r0 + r4
            long r0 = r0 + r14
            long r0 = r0 >>> r25
            long r0 = r0 + r10
            long r0 = r0 + r12
            r2 = 21
            long r0 = r0 >>> r2
            r3 = 10
            long r4 = r19 << r3
            long r0 = r0 + r4
            long r0 = r0 >>> r16
            r3 = r30
            if (r3 > r2) goto L_0x02f9
            r4 = 5
            long r15 = r34 % r4
            r6 = 625(0x271, double:3.09E-321)
            r8 = 0
            int r2 = (r15 > r8 ? 1 : (r15 == r8 ? 0 : -1))
            if (r2 != 0) goto L_0x0276
            int r2 = (r15 > r8 ? 1 : (r15 == r8 ? 0 : -1))
            if (r2 == 0) goto L_0x023d
            r2 = 0
            goto L_0x026d
        L_0x023d:
            r10 = 25
            long r15 = r34 % r10
            int r2 = (r15 > r8 ? 1 : (r15 == r8 ? 0 : -1))
            if (r2 == 0) goto L_0x0247
            r2 = 1
            goto L_0x026d
        L_0x0247:
            r10 = 125(0x7d, double:6.2E-322)
            long r15 = r34 % r10
            int r2 = (r15 > r8 ? 1 : (r15 == r8 ? 0 : -1))
            if (r2 == 0) goto L_0x0251
            r2 = 2
            goto L_0x026d
        L_0x0251:
            long r15 = r34 % r6
            int r2 = (r15 > r8 ? 1 : (r15 == r8 ? 0 : -1))
            if (r2 == 0) goto L_0x0259
            r2 = 3
            goto L_0x026d
        L_0x0259:
            long r15 = r34 / r6
            r2 = 4
        L_0x025c:
            int r6 = (r15 > r8 ? 1 : (r15 == r8 ? 0 : -1))
            if (r6 <= 0) goto L_0x026d
            long r6 = r15 % r4
            int r10 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r10 == 0) goto L_0x0267
            goto L_0x026d
        L_0x0267:
            long r15 = r15 / r4
            int r2 = r2 + 1
            r8 = 0
            goto L_0x025c
        L_0x026d:
            if (r2 < r3) goto L_0x0271
            r2 = 1
            goto L_0x0272
        L_0x0271:
            r2 = 0
        L_0x0272:
            r4 = r2
            r2 = 0
            goto L_0x02fb
        L_0x0276:
            if (r32 == 0) goto L_0x02b6
            long r9 = r36 % r4
            r11 = 0
            int r2 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1))
            if (r2 == 0) goto L_0x0282
            r2 = 0
            goto L_0x02b2
        L_0x0282:
            r8 = 25
            long r9 = r36 % r8
            int r2 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1))
            if (r2 == 0) goto L_0x028c
            r2 = 1
            goto L_0x02b2
        L_0x028c:
            r8 = 125(0x7d, double:6.2E-322)
            long r9 = r36 % r8
            int r2 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1))
            if (r2 == 0) goto L_0x0296
            r2 = 2
            goto L_0x02b2
        L_0x0296:
            long r9 = r36 % r6
            int r2 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1))
            if (r2 == 0) goto L_0x029e
            r2 = 3
            goto L_0x02b2
        L_0x029e:
            long r9 = r36 / r6
            r2 = 4
        L_0x02a1:
            int r6 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1))
            if (r6 <= 0) goto L_0x02b2
            long r6 = r9 % r4
            int r8 = (r6 > r11 ? 1 : (r6 == r11 ? 0 : -1))
            if (r8 == 0) goto L_0x02ac
            goto L_0x02b2
        L_0x02ac:
            long r9 = r9 / r4
            int r2 = r2 + 1
            r11 = 0
            goto L_0x02a1
        L_0x02b2:
            if (r2 < r3) goto L_0x02f9
            r2 = 1
            goto L_0x02fa
        L_0x02b6:
            long r8 = r17 % r4
            r10 = 0
            int r2 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r2 == 0) goto L_0x02c0
            r2 = 0
            goto L_0x02f1
        L_0x02c0:
            r8 = 25
            long r8 = r17 % r8
            int r2 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r2 == 0) goto L_0x02ca
            r2 = 1
            goto L_0x02f1
        L_0x02ca:
            r8 = 125(0x7d, double:6.2E-322)
            long r8 = r17 % r8
            int r2 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r2 == 0) goto L_0x02d4
            r2 = 2
            goto L_0x02f1
        L_0x02d4:
            long r8 = r17 % r6
            int r2 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r2 == 0) goto L_0x02dc
            r2 = 3
            goto L_0x02f1
        L_0x02dc:
            long r17 = r17 / r6
            r2 = 4
        L_0x02df:
            int r6 = (r17 > r10 ? 1 : (r17 == r10 ? 0 : -1))
            if (r6 <= 0) goto L_0x02f1
            long r6 = r17 % r4
            int r8 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1))
            if (r8 == 0) goto L_0x02ea
            goto L_0x02f1
        L_0x02ea:
            long r17 = r17 / r4
            int r2 = r2 + 1
            r10 = 0
            goto L_0x02df
        L_0x02f1:
            if (r2 < r3) goto L_0x02f9
            r4 = 1
            long r4 = r38 - r4
            r38 = r4
        L_0x02f9:
            r2 = 0
        L_0x02fa:
            r4 = 0
        L_0x02fb:
            r17 = r27
            r42 = r4
            r4 = r3
            r3 = r42
            goto L_0x0474
        L_0x0304:
            r16 = r2
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = ""
            r1.append(r2)
            r2 = r16
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x031f:
            r31 = r0
            r32 = r1
            r36 = r9
            r34 = r15
            int r0 = -r7
            long r1 = (long) r0
            r3 = 6989700(0x6aa784, double:3.4533706E-317)
            long r1 = r1 * r3
            long r1 = r1 / r21
            int r2 = (int) r1
            r1 = 1
            int r2 = r2 - r1
            r1 = 0
            int r2 = java.lang.Math.max(r1, r2)
            int r0 = r0 - r2
            if (r0 != 0) goto L_0x033d
            r1 = 1
            goto L_0x034b
        L_0x033d:
            long r3 = (long) r0
            r9 = 23219280(0x1624c50, double:1.14718486E-316)
            long r3 = r3 * r9
            long r3 = r3 + r21
            r9 = 1
            long r3 = r3 - r9
            long r3 = r3 / r21
            int r1 = (int) r3
        L_0x034b:
            int r1 = r1 + -121
            int r1 = r2 - r1
            int r1 = r1 + -93
            r3 = 21
            int r1 = r1 - r3
            if (r1 < 0) goto L_0x0708
            int[][] r3 = POW5_SPLIT
            r0 = r3[r0]
            long r3 = r34 >>> r25
            long r9 = r34 & r19
            r6 = 0
            r11 = r0[r6]
            long r11 = (long) r11
            long r11 = r11 * r3
            r13 = r0[r6]
            long r13 = (long) r13
            long r13 = r13 * r9
            r6 = 1
            r15 = r0[r6]
            r27 = r7
            r16 = r8
            long r7 = (long) r15
            long r7 = r7 * r3
            r15 = r0[r6]
            long r5 = (long) r15
            long r5 = r5 * r9
            r15 = r0[r24]
            r29 = r1
            r28 = r2
            long r1 = (long) r15
            long r1 = r1 * r3
            r15 = r0[r24]
            r38 = r11
            long r11 = (long) r15
            long r11 = r11 * r9
            r15 = r0[r23]
            r40 = r7
            long r7 = (long) r15
            long r3 = r3 * r7
            r7 = r0[r23]
            long r7 = (long) r7
            long r9 = r9 * r7
            long r7 = r9 >>> r25
            long r7 = r7 + r11
            long r7 = r7 + r3
            long r3 = r7 >>> r25
            long r3 = r3 + r5
            long r3 = r3 + r1
            long r1 = r3 >>> r25
            long r1 = r1 + r13
            long r1 = r1 + r40
            r3 = 21
            long r1 = r1 >>> r3
            r3 = 10
            long r6 = r38 << r3
            long r1 = r1 + r6
            long r1 = r1 >>> r29
            long r3 = r17 >>> r25
            long r6 = r17 & r19
            r8 = 0
            r9 = r0[r8]
            long r9 = (long) r9
            long r9 = r9 * r3
            r11 = r0[r8]
            long r11 = (long) r11
            long r11 = r11 * r6
            r8 = 1
            r13 = r0[r8]
            long r13 = (long) r13
            long r13 = r13 * r3
            r15 = r0[r8]
            r17 = r1
            long r1 = (long) r15
            long r1 = r1 * r6
            r8 = r0[r24]
            r38 = r6
            long r5 = (long) r8
            long r5 = r5 * r3
            r7 = r0[r24]
            long r7 = (long) r7
            long r7 = r7 * r38
            r15 = r0[r23]
            r40 = r9
            long r9 = (long) r15
            long r3 = r3 * r9
            r9 = r0[r23]
            long r9 = (long) r9
            long r9 = r9 * r38
            long r9 = r9 >>> r25
            long r9 = r9 + r7
            long r9 = r9 + r3
            long r3 = r9 >>> r25
            long r3 = r3 + r1
            long r3 = r3 + r5
            long r1 = r3 >>> r25
            long r1 = r1 + r11
            long r1 = r1 + r13
            r3 = 21
            long r1 = r1 >>> r3
            r3 = 10
            long r6 = r40 << r3
            long r1 = r1 + r6
            long r38 = r1 >>> r29
            long r1 = r36 >>> r25
            long r3 = r36 & r19
            r7 = 0
            r6 = r0[r7]
            long r8 = (long) r6
            long r8 = r8 * r1
            r6 = r0[r7]
            long r10 = (long) r6
            long r10 = r10 * r3
            r6 = 1
            r12 = r0[r6]
            long r12 = (long) r12
            long r12 = r12 * r1
            r14 = r0[r6]
            long r14 = (long) r14
            long r14 = r14 * r3
            r6 = r0[r24]
            long r5 = (long) r6
            long r5 = r5 * r1
            r7 = r0[r24]
            r19 = r8
            long r7 = (long) r7
            long r7 = r7 * r3
            r9 = r0[r23]
            r36 = r12
            long r12 = (long) r9
            long r1 = r1 * r12
            r0 = r0[r23]
            long r12 = (long) r0
            long r3 = r3 * r12
            long r3 = r3 >>> r25
            long r3 = r3 + r7
            long r3 = r3 + r1
            long r0 = r3 >>> r25
            long r0 = r0 + r14
            long r0 = r0 + r5
            long r0 = r0 >>> r25
            long r0 = r0 + r10
            long r0 = r0 + r36
            r2 = 21
            long r0 = r0 >>> r2
            r2 = 10
            long r3 = r19 << r2
            long r0 = r0 + r3
            long r0 = r0 >>> r29
            int r4 = r28 + r27
            r3 = r28
            r2 = 1
            if (r3 > r2) goto L_0x045a
            if (r32 == 0) goto L_0x0453
            r12 = r16
            if (r12 != r2) goto L_0x044e
            r33 = 1
            goto L_0x0450
        L_0x044e:
            r33 = 0
        L_0x0450:
            r2 = r33
            goto L_0x0458
        L_0x0453:
            r5 = 1
            long r38 = r38 - r5
            r2 = 0
        L_0x0458:
            r3 = 1
            goto L_0x0474
        L_0x045a:
            r5 = 1
            r7 = 63
            if (r3 >= r7) goto L_0x0472
            int r3 = r3 - r2
            long r2 = r5 << r3
            long r2 = r2 - r5
            long r2 = r34 & r2
            r5 = 0
            int r7 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1))
            if (r7 != 0) goto L_0x046e
            r2 = 1
            goto L_0x046f
        L_0x046e:
            r2 = 0
        L_0x046f:
            r3 = r2
            r2 = 0
            goto L_0x0474
        L_0x0472:
            r2 = 0
            r3 = 0
        L_0x0474:
            r5 = 1000000000000000000(0xde0b6b3a7640000, double:7.832953389245686E-242)
            r7 = 5
            r8 = 100
            r10 = 10
            int r12 = (r38 > r5 ? 1 : (r38 == r5 ? 0 : -1))
            if (r12 < 0) goto L_0x0486
            r23 = 19
            goto L_0x053b
        L_0x0486:
            r5 = 100000000000000000(0x16345785d8a0000, double:5.620395787888205E-302)
            int r12 = (r38 > r5 ? 1 : (r38 == r5 ? 0 : -1))
            if (r12 < 0) goto L_0x0493
            r23 = 18
            goto L_0x053b
        L_0x0493:
            r5 = 10000000000000000(0x2386f26fc10000, double:5.431165199810528E-308)
            int r12 = (r38 > r5 ? 1 : (r38 == r5 ? 0 : -1))
            if (r12 < 0) goto L_0x04a0
            r23 = 17
            goto L_0x053b
        L_0x04a0:
            r5 = 1000000000000000(0x38d7ea4c68000, double:4.940656458412465E-309)
            int r12 = (r38 > r5 ? 1 : (r38 == r5 ? 0 : -1))
            if (r12 < 0) goto L_0x04ad
            r23 = 16
            goto L_0x053b
        L_0x04ad:
            r5 = 100000000000000(0x5af3107a4000, double:4.94065645841247E-310)
            int r12 = (r38 > r5 ? 1 : (r38 == r5 ? 0 : -1))
            if (r12 < 0) goto L_0x04ba
            r23 = 15
            goto L_0x053b
        L_0x04ba:
            r5 = 10000000000000(0x9184e72a000, double:4.9406564584125E-311)
            int r12 = (r38 > r5 ? 1 : (r38 == r5 ? 0 : -1))
            if (r12 < 0) goto L_0x04c7
            r23 = 14
            goto L_0x053b
        L_0x04c7:
            r5 = 1000000000000(0xe8d4a51000, double:4.94065645841E-312)
            int r12 = (r38 > r5 ? 1 : (r38 == r5 ? 0 : -1))
            if (r12 < 0) goto L_0x04d4
            r23 = 13
            goto L_0x053b
        L_0x04d4:
            r5 = 100000000000(0x174876e800, double:4.9406564584E-313)
            int r12 = (r38 > r5 ? 1 : (r38 == r5 ? 0 : -1))
            if (r12 < 0) goto L_0x04e0
            r23 = 12
            goto L_0x053b
        L_0x04e0:
            r5 = 10000000000(0x2540be400, double:4.9406564584E-314)
            int r12 = (r38 > r5 ? 1 : (r38 == r5 ? 0 : -1))
            if (r12 < 0) goto L_0x04ec
            r23 = 11
            goto L_0x053b
        L_0x04ec:
            r5 = 1000000000(0x3b9aca00, double:4.94065646E-315)
            int r12 = (r38 > r5 ? 1 : (r38 == r5 ? 0 : -1))
            if (r12 < 0) goto L_0x04f6
            r23 = 10
            goto L_0x053b
        L_0x04f6:
            r5 = 100000000(0x5f5e100, double:4.94065646E-316)
            int r12 = (r38 > r5 ? 1 : (r38 == r5 ? 0 : -1))
            if (r12 < 0) goto L_0x0500
            r23 = 9
            goto L_0x053b
        L_0x0500:
            int r5 = (r38 > r21 ? 1 : (r38 == r21 ? 0 : -1))
            if (r5 < 0) goto L_0x0507
            r23 = 8
            goto L_0x053b
        L_0x0507:
            r5 = 1000000(0xf4240, double:4.940656E-318)
            int r12 = (r38 > r5 ? 1 : (r38 == r5 ? 0 : -1))
            if (r12 < 0) goto L_0x0511
            r23 = 7
            goto L_0x053b
        L_0x0511:
            r5 = 100000(0x186a0, double:4.94066E-319)
            int r12 = (r38 > r5 ? 1 : (r38 == r5 ? 0 : -1))
            if (r12 < 0) goto L_0x051b
            r23 = 6
            goto L_0x053b
        L_0x051b:
            r5 = 10000(0x2710, double:4.9407E-320)
            int r12 = (r38 > r5 ? 1 : (r38 == r5 ? 0 : -1))
            if (r12 < 0) goto L_0x0524
            r23 = 5
            goto L_0x053b
        L_0x0524:
            r5 = 1000(0x3e8, double:4.94E-321)
            int r12 = (r38 > r5 ? 1 : (r38 == r5 ? 0 : -1))
            if (r12 < 0) goto L_0x052d
            r23 = 4
            goto L_0x053b
        L_0x052d:
            int r5 = (r38 > r8 ? 1 : (r38 == r8 ? 0 : -1))
            if (r5 < 0) goto L_0x0532
            goto L_0x053b
        L_0x0532:
            int r5 = (r38 > r10 ? 1 : (r38 == r10 ? 0 : -1))
            if (r5 < 0) goto L_0x0539
            r23 = 2
            goto L_0x053b
        L_0x0539:
            r23 = 1
        L_0x053b:
            int r4 = r4 + r23
            r5 = 1
            int r4 = r4 - r5
            r5 = -3
            if (r4 < r5) goto L_0x0548
            r5 = 7
            if (r4 < r5) goto L_0x0546
            goto L_0x0548
        L_0x0546:
            r5 = 0
            goto L_0x0549
        L_0x0548:
            r5 = 1
        L_0x0549:
            if (r2 != 0) goto L_0x0579
            if (r3 == 0) goto L_0x054e
            goto L_0x0579
        L_0x054e:
            r2 = 0
            r3 = 0
        L_0x0550:
            long r12 = r38 / r10
            long r14 = r0 / r10
            int r6 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1))
            if (r6 <= 0) goto L_0x056a
            int r6 = (r38 > r8 ? 1 : (r38 == r8 ? 0 : -1))
            if (r6 >= 0) goto L_0x055f
            if (r5 == 0) goto L_0x055f
            goto L_0x056a
        L_0x055f:
            long r0 = r17 % r10
            int r3 = (int) r0
            long r17 = r17 / r10
            int r2 = r2 + 1
            r38 = r12
            r0 = r14
            goto L_0x0550
        L_0x056a:
            int r6 = (r17 > r0 ? 1 : (r17 == r0 ? 0 : -1))
            if (r6 == 0) goto L_0x0573
            if (r3 < r7) goto L_0x0571
            goto L_0x0573
        L_0x0571:
            r0 = 0
            goto L_0x0574
        L_0x0573:
            r0 = 1
        L_0x0574:
            long r0 = (long) r0
            long r17 = r17 + r0
            goto L_0x05ea
        L_0x0579:
            r6 = 0
            r12 = 0
        L_0x057b:
            long r13 = r38 / r10
            long r15 = r0 / r10
            int r19 = (r13 > r15 ? 1 : (r13 == r15 ? 0 : -1))
            if (r19 <= 0) goto L_0x05a6
            int r19 = (r38 > r8 ? 1 : (r38 == r8 ? 0 : -1))
            if (r19 >= 0) goto L_0x058a
            if (r5 == 0) goto L_0x058a
            goto L_0x05a6
        L_0x058a:
            long r0 = r0 % r10
            r19 = 0
            int r21 = (r0 > r19 ? 1 : (r0 == r19 ? 0 : -1))
            if (r21 != 0) goto L_0x0593
            r0 = 1
            goto L_0x0594
        L_0x0593:
            r0 = 0
        L_0x0594:
            r2 = r2 & r0
            if (r6 != 0) goto L_0x0599
            r0 = 1
            goto L_0x059a
        L_0x0599:
            r0 = 0
        L_0x059a:
            r3 = r3 & r0
            long r0 = r17 % r10
            int r6 = (int) r0
            long r17 = r17 / r10
            int r12 = r12 + 1
            r38 = r13
            r0 = r15
            goto L_0x057b
        L_0x05a6:
            if (r2 == 0) goto L_0x05ca
            if (r32 == 0) goto L_0x05ca
        L_0x05aa:
            long r13 = r0 % r10
            r15 = 0
            int r19 = (r13 > r15 ? 1 : (r13 == r15 ? 0 : -1))
            if (r19 != 0) goto L_0x05ca
            int r13 = (r38 > r8 ? 1 : (r38 == r8 ? 0 : -1))
            if (r13 >= 0) goto L_0x05b9
            if (r5 == 0) goto L_0x05b9
            goto L_0x05ca
        L_0x05b9:
            if (r6 != 0) goto L_0x05bd
            r6 = 1
            goto L_0x05be
        L_0x05bd:
            r6 = 0
        L_0x05be:
            r3 = r3 & r6
            long r13 = r17 % r10
            int r6 = (int) r13
            long r38 = r38 / r10
            long r17 = r17 / r10
            long r0 = r0 / r10
            int r12 = r12 + 1
            goto L_0x05aa
        L_0x05ca:
            if (r3 == 0) goto L_0x05d9
            if (r6 != r7) goto L_0x05d9
            r8 = 2
            long r8 = r17 % r8
            r13 = 0
            int r3 = (r8 > r13 ? 1 : (r8 == r13 ? 0 : -1))
            if (r3 != 0) goto L_0x05d9
            r6 = 4
        L_0x05d9:
            int r3 = (r17 > r0 ? 1 : (r17 == r0 ? 0 : -1))
            if (r3 != 0) goto L_0x05e1
            if (r2 == 0) goto L_0x05e3
            if (r32 == 0) goto L_0x05e3
        L_0x05e1:
            if (r6 < r7) goto L_0x05e5
        L_0x05e3:
            r0 = 1
            goto L_0x05e6
        L_0x05e5:
            r0 = 0
        L_0x05e6:
            long r0 = (long) r0
            long r17 = r17 + r0
            r2 = r12
        L_0x05ea:
            int r0 = r23 - r2
            if (r31 == 0) goto L_0x05f5
            int r1 = r46 + 1
            r2 = 45
            r45[r46] = r2
            goto L_0x05f7
        L_0x05f5:
            r1 = r46
        L_0x05f7:
            if (r5 == 0) goto L_0x066f
            r2 = 0
        L_0x05fa:
            int r3 = r0 + -1
            if (r2 >= r3) goto L_0x060f
            long r7 = r17 % r10
            int r3 = (int) r7
            long r17 = r17 / r10
            int r5 = r1 + r0
            int r5 = r5 - r2
            r7 = 48
            int r3 = r3 + r7
            char r3 = (char) r3
            r45[r5] = r3
            int r2 = r2 + 1
            goto L_0x05fa
        L_0x060f:
            r2 = 48
            long r17 = r17 % r10
            long r2 = r17 + r2
            int r3 = (int) r2
            char r2 = (char) r3
            r45[r1] = r2
            int r2 = r1 + 1
            r3 = 46
            r45[r2] = r3
            int r2 = r0 + 1
            int r1 = r1 + r2
            r2 = 1
            if (r0 != r2) goto L_0x062c
            int r0 = r1 + 1
            r2 = 48
            r45[r1] = r2
            r1 = r0
        L_0x062c:
            int r0 = r1 + 1
            r2 = 69
            r45[r1] = r2
            if (r4 >= 0) goto L_0x063c
            int r1 = r0 + 1
            r2 = 45
            r45[r0] = r2
            int r4 = -r4
            r0 = r1
        L_0x063c:
            r1 = 100
            if (r4 < r1) goto L_0x0655
            int r1 = r0 + 1
            int r2 = r4 / 100
            r3 = 48
            int r2 = r2 + r3
            char r2 = (char) r2
            r45[r0] = r2
            int r4 = r4 % 100
            int r0 = r1 + 1
            int r2 = r4 / 10
            int r2 = r2 + r3
            char r2 = (char) r2
            r45[r1] = r2
            goto L_0x0664
        L_0x0655:
            r1 = 10
            r3 = 48
            if (r4 < r1) goto L_0x0664
            int r1 = r0 + 1
            int r2 = r4 / 10
            int r2 = r2 + r3
            char r2 = (char) r2
            r45[r0] = r2
            r0 = r1
        L_0x0664:
            int r1 = r0 + 1
            r2 = 10
            int r4 = r4 % r2
            int r4 = r4 + r3
            char r2 = (char) r4
            r45[r0] = r2
            goto L_0x004d
        L_0x066f:
            r3 = 48
            if (r4 >= 0) goto L_0x06a3
            int r2 = r1 + 1
            r45[r1] = r3
            int r1 = r2 + 1
            r5 = 46
            r45[r2] = r5
            r2 = -1
        L_0x067e:
            if (r2 <= r4) goto L_0x068a
            int r5 = r1 + 1
            r45[r1] = r3
            int r2 = r2 + -1
            r1 = r5
            r3 = 48
            goto L_0x067e
        L_0x068a:
            r3 = r1
            r2 = 0
        L_0x068c:
            if (r2 >= r0) goto L_0x0705
            int r4 = r1 + r0
            int r4 = r4 - r2
            r5 = 1
            int r4 = r4 - r5
            r5 = 48
            long r7 = r17 % r10
            long r7 = r7 + r5
            int r5 = (int) r7
            char r5 = (char) r5
            r45[r4] = r5
            long r17 = r17 / r10
            int r3 = r3 + 1
            int r2 = r2 + 1
            goto L_0x068c
        L_0x06a3:
            int r2 = r4 + 1
            if (r2 < r0) goto L_0x06d7
            r3 = 0
        L_0x06a8:
            if (r3 >= r0) goto L_0x06bd
            int r4 = r1 + r0
            int r4 = r4 - r3
            r5 = 1
            int r4 = r4 - r5
            r5 = 48
            long r7 = r17 % r10
            long r7 = r7 + r5
            int r5 = (int) r7
            char r5 = (char) r5
            r45[r4] = r5
            long r17 = r17 / r10
            int r3 = r3 + 1
            goto L_0x06a8
        L_0x06bd:
            int r1 = r1 + r0
        L_0x06be:
            if (r0 >= r2) goto L_0x06ca
            int r3 = r1 + 1
            r4 = 48
            r45[r1] = r4
            int r0 = r0 + 1
            r1 = r3
            goto L_0x06be
        L_0x06ca:
            r4 = 48
            int r0 = r1 + 1
            r2 = 46
            r45[r1] = r2
            int r3 = r0 + 1
            r45[r0] = r4
            goto L_0x0705
        L_0x06d7:
            int r2 = r1 + 1
            r3 = 0
        L_0x06da:
            if (r3 >= r0) goto L_0x0701
            int r5 = r0 - r3
            r6 = 1
            int r5 = r5 - r6
            if (r5 != r4) goto L_0x06ed
            int r5 = r2 + r0
            int r5 = r5 - r3
            int r5 = r5 - r6
            r7 = 46
            r45[r5] = r7
            int r2 = r2 + -1
            goto L_0x06ef
        L_0x06ed:
            r7 = 46
        L_0x06ef:
            int r5 = r2 + r0
            int r5 = r5 - r3
            int r5 = r5 - r6
            r8 = 48
            long r12 = r17 % r10
            long r12 = r12 + r8
            int r8 = (int) r12
            char r8 = (char) r8
            r45[r5] = r8
            long r17 = r17 / r10
            int r3 = r3 + 1
            goto L_0x06da
        L_0x0701:
            r6 = 1
            int r0 = r0 + r6
            int r3 = r1 + r0
        L_0x0705:
            int r3 = r3 - r46
            return r3
        L_0x0708:
            r29 = r1
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = ""
            r1.append(r2)
            r2 = r29
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            goto L_0x0724
        L_0x0723:
            throw r0
        L_0x0724:
            goto L_0x0723
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.RyuDouble.toString(double, char[], int):int");
    }
}
