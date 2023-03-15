package com.alibaba.fastjson.util;

import com.taobao.weex.el.parse.Operators;

public final class RyuFloat {
    private static final int[][] POW5_INV_SPLIT = {new int[]{268435456, 1}, new int[]{214748364, 1717986919}, new int[]{171798691, 1803886265}, new int[]{137438953, 1013612282}, new int[]{219902325, 1192282922}, new int[]{175921860, 953826338}, new int[]{140737488, 763061070}, new int[]{225179981, 791400982}, new int[]{180143985, 203624056}, new int[]{144115188, 162899245}, new int[]{230584300, 1978625710}, new int[]{184467440, 1582900568}, new int[]{147573952, 1266320455}, new int[]{236118324, 308125809}, new int[]{188894659, 675997377}, new int[]{151115727, 970294631}, new int[]{241785163, 1981968139}, new int[]{193428131, 297084323}, new int[]{154742504, 1955654377}, new int[]{247588007, 1840556814}, new int[]{198070406, 613451992}, new int[]{158456325, 61264864}, new int[]{253530120, 98023782}, new int[]{202824096, 78419026}, new int[]{162259276, 1780722139}, new int[]{259614842, 1990161963}, new int[]{207691874, 733136111}, new int[]{166153499, 1016005619}, new int[]{265845599, 337118801}, new int[]{212676479, 699191770}, new int[]{170141183, 988850146}};
    private static final int[][] POW5_SPLIT = {new int[]{536870912, 0}, new int[]{671088640, 0}, new int[]{838860800, 0}, new int[]{1048576000, 0}, new int[]{655360000, 0}, new int[]{819200000, 0}, new int[]{1024000000, 0}, new int[]{640000000, 0}, new int[]{800000000, 0}, new int[]{1000000000, 0}, new int[]{625000000, 0}, new int[]{781250000, 0}, new int[]{976562500, 0}, new int[]{610351562, 1073741824}, new int[]{762939453, 268435456}, new int[]{953674316, 872415232}, new int[]{596046447, 1619001344}, new int[]{745058059, 1486880768}, new int[]{931322574, 1321730048}, new int[]{582076609, 289210368}, new int[]{727595761, 898383872}, new int[]{909494701, 1659850752}, new int[]{568434188, 1305842176}, new int[]{710542735, 1632302720}, new int[]{888178419, 1503507488}, new int[]{555111512, 671256724}, new int[]{693889390, 839070905}, new int[]{867361737, 2122580455}, new int[]{542101086, 521306416}, new int[]{677626357, 1725374844}, new int[]{847032947, 546105819}, new int[]{1058791184, 145761362}, new int[]{661744490, 91100851}, new int[]{827180612, 1187617888}, new int[]{1033975765, 1484522360}, new int[]{646234853, 1196261931}, new int[]{807793566, 2032198326}, new int[]{1009741958, 1466506084}, new int[]{631088724, 379695390}, new int[]{788860905, 474619238}, new int[]{986076131, 1130144959}, new int[]{616297582, 437905143}, new int[]{770371977, 1621123253}, new int[]{962964972, 415791331}, new int[]{601853107, 1333611405}, new int[]{752316384, 1130143345}, new int[]{940395480, 1412679181}};

    public static String toString(float f) {
        char[] cArr = new char[15];
        return new String(cArr, 0, toString(f, cArr, 0));
    }

    public static int toString(float f, char[] cArr, int i) {
        int i2;
        boolean z;
        int i3;
        boolean z2;
        int i4;
        int i5;
        boolean z3;
        boolean z4;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        if (Float.isNaN(f)) {
            int i17 = i + 1;
            cArr[i] = 'N';
            int i18 = i17 + 1;
            cArr[i17] = 'a';
            i16 = i18 + 1;
            cArr[i18] = 'N';
        } else if (f == Float.POSITIVE_INFINITY) {
            int i19 = i + 1;
            cArr[i] = 'I';
            int i20 = i19 + 1;
            cArr[i19] = 'n';
            int i21 = i20 + 1;
            cArr[i20] = 'f';
            int i22 = i21 + 1;
            cArr[i21] = 'i';
            int i23 = i22 + 1;
            cArr[i22] = 'n';
            int i24 = i23 + 1;
            cArr[i23] = 'i';
            int i25 = i24 + 1;
            cArr[i24] = 't';
            cArr[i25] = 'y';
            return (i25 + 1) - i;
        } else if (f == Float.NEGATIVE_INFINITY) {
            int i26 = i + 1;
            cArr[i] = '-';
            int i27 = i26 + 1;
            cArr[i26] = 'I';
            int i28 = i27 + 1;
            cArr[i27] = 'n';
            int i29 = i28 + 1;
            cArr[i28] = 'f';
            int i30 = i29 + 1;
            cArr[i29] = 'i';
            int i31 = i30 + 1;
            cArr[i30] = 'n';
            int i32 = i31 + 1;
            cArr[i31] = 'i';
            int i33 = i32 + 1;
            cArr[i32] = 't';
            i16 = i33 + 1;
            cArr[i33] = 'y';
        } else {
            int floatToIntBits = Float.floatToIntBits(f);
            if (floatToIntBits == 0) {
                int i34 = i + 1;
                cArr[i] = '0';
                int i35 = i34 + 1;
                cArr[i34] = Operators.DOT;
                i16 = i35 + 1;
                cArr[i35] = '0';
            } else if (floatToIntBits == Integer.MIN_VALUE) {
                int i36 = i + 1;
                cArr[i] = '-';
                int i37 = i36 + 1;
                cArr[i36] = '0';
                int i38 = i37 + 1;
                cArr[i37] = Operators.DOT;
                cArr[i38] = '0';
                return (i38 + 1) - i;
            } else {
                int i39 = (floatToIntBits >> 23) & 255;
                int i40 = 8388607 & floatToIntBits;
                if (i39 == 0) {
                    i2 = -149;
                } else {
                    i2 = (i39 - 127) - 23;
                    i40 |= 8388608;
                }
                boolean z5 = floatToIntBits < 0;
                boolean z6 = (i40 & 1) == 0;
                int i41 = i40 * 4;
                int i42 = i41 + 2;
                int i43 = i41 - ((((long) i40) != 8388608 || i39 <= 1) ? 2 : 1);
                int i44 = i2 - 2;
                if (i44 >= 0) {
                    i5 = (int) ((((long) i44) * 3010299) / 10000000);
                    if (i5 == 0) {
                        i14 = 1;
                    } else {
                        i14 = (int) ((((((long) i5) * 23219280) + 10000000) - 1) / 10000000);
                    }
                    int i45 = (-i44) + i5;
                    int[][] iArr = POW5_INV_SPLIT;
                    long j = (long) iArr[i5][0];
                    z = z6;
                    long j2 = (long) iArr[i5][1];
                    long j3 = (long) i41;
                    int i46 = (((i14 + 59) - 1) + i45) - 31;
                    long j4 = j3;
                    i4 = (int) (((j3 * j) + ((j3 * j2) >> 31)) >> i46);
                    long j5 = (long) i42;
                    i3 = (int) (((j5 * j) + ((j5 * j2) >> 31)) >> i46);
                    int i47 = i42;
                    long j6 = (long) i43;
                    i6 = (int) (((j * j6) + ((j6 * j2) >> 31)) >> i46);
                    if (i5 == 0 || (i3 - 1) / 10 > i6 / 10) {
                        i7 = 0;
                    } else {
                        int i48 = i5 - 1;
                        if (i48 == 0) {
                            i15 = 1;
                        } else {
                            i15 = (int) ((((((long) i48) * 23219280) + 10000000) - 1) / 10000000);
                        }
                        i7 = (int) ((((((long) iArr[i48][0]) * j4) + ((((long) iArr[i48][1]) * j4) >> 31)) >> (((i45 - 1) + ((i15 + 59) - 1)) - 31)) % 10);
                    }
                    int i49 = i47;
                    int i50 = 0;
                    while (i49 > 0 && i49 % 5 == 0) {
                        i49 /= 5;
                        i50++;
                    }
                    int i51 = 0;
                    while (i41 > 0 && i41 % 5 == 0) {
                        i41 /= 5;
                        i51++;
                    }
                    int i52 = 0;
                    while (i43 > 0 && i43 % 5 == 0) {
                        i43 /= 5;
                        i52++;
                    }
                    z4 = i50 >= i5;
                    z3 = i51 >= i5;
                    z2 = i52 >= i5;
                } else {
                    z = z6;
                    int i53 = i42;
                    int i54 = -i44;
                    int i55 = (int) ((((long) i54) * 6989700) / 10000000);
                    int i56 = i54 - i55;
                    if (i56 == 0) {
                        i11 = 1;
                    } else {
                        i11 = (int) ((((((long) i56) * 23219280) + 10000000) - 1) / 10000000);
                    }
                    int i57 = i55 - (i11 - 61);
                    int[][] iArr2 = POW5_SPLIT;
                    long j7 = (long) iArr2[i56][0];
                    long j8 = (long) iArr2[i56][1];
                    int i58 = i57 - 31;
                    long j9 = (long) i41;
                    int i59 = i53;
                    int i60 = (int) (((j9 * j7) + ((j9 * j8) >> 31)) >> i58);
                    long j10 = (long) i59;
                    i3 = (int) (((j10 * j7) + ((j10 * j8) >> 31)) >> i58);
                    long j11 = j9;
                    long j12 = (long) i43;
                    int i61 = (int) (((j7 * j12) + ((j12 * j8) >> 31)) >> i58);
                    if (i55 == 0 || (i3 - 1) / 10 > i61 / 10) {
                        i12 = 0;
                    } else {
                        int i62 = i56 + 1;
                        int i63 = i55 - 1;
                        if (i62 == 0) {
                            i13 = 1;
                        } else {
                            i13 = (int) ((((((long) i62) * 23219280) + 10000000) - 1) / 10000000);
                        }
                        i12 = (int) ((((((long) iArr2[i62][0]) * j11) + ((((long) iArr2[i62][1]) * j11) >> 31)) >> ((i63 - (i13 - 61)) - 31)) % 10);
                    }
                    int i64 = i55 + i44;
                    boolean z7 = 1 >= i55;
                    boolean z8 = i55 < 23 && (((1 << (i55 + -1)) - 1) & i41) == 0;
                    boolean z9 = (i43 % 2 == 1 ? 0 : 1) >= i55;
                    z4 = z7;
                    i4 = i60;
                    z2 = z9;
                    i6 = i61;
                    i5 = i64;
                    z3 = z8;
                }
                int i65 = 1000000000;
                int i66 = 10;
                while (i66 > 0 && i3 < i65) {
                    i65 /= 10;
                    i66--;
                }
                int i67 = (i5 + i66) - 1;
                boolean z10 = i67 < -3 || i67 >= 7;
                if (z4 && !z) {
                    i3--;
                }
                int i68 = 0;
                while (true) {
                    int i69 = i3 / 10;
                    int i70 = i6 / 10;
                    if (i69 > i70 && (i3 >= 100 || !z10)) {
                        z2 &= i6 % 10 == 0;
                        i7 = i4 % 10;
                        i4 /= 10;
                        i68++;
                        i3 = i69;
                        i6 = i70;
                    } else if (z2 && z) {
                        while (i6 % 10 == 0 && (i3 >= 100 || !z10)) {
                            i3 /= 10;
                            i7 = i4 % 10;
                            i4 /= 10;
                            i6 /= 10;
                            i68++;
                        }
                    }
                }
                while (i6 % 10 == 0) {
                    i3 /= 10;
                    i7 = i4 % 10;
                    i4 /= 10;
                    i6 /= 10;
                    i68++;
                }
                if (z3 && i7 == 5 && i4 % 2 == 0) {
                    i7 = 4;
                }
                int i71 = i4 + (((i4 != i6 || (z2 && z)) && i7 < 5) ? 0 : 1);
                int i72 = i66 - i68;
                if (z5) {
                    i8 = i + 1;
                    cArr[i] = '-';
                } else {
                    i8 = i;
                }
                if (z10) {
                    for (int i73 = 0; i73 < i72 - 1; i73++) {
                        int i74 = i71 % 10;
                        i71 /= 10;
                        cArr[(i8 + i72) - i73] = (char) (i74 + 48);
                    }
                    cArr[i8] = (char) ((i71 % 10) + 48);
                    cArr[i8 + 1] = Operators.DOT;
                    int i75 = i8 + i72 + 1;
                    if (i72 == 1) {
                        cArr[i75] = '0';
                        i75++;
                    }
                    int i76 = i75 + 1;
                    cArr[i75] = 'E';
                    if (i67 < 0) {
                        cArr[i76] = '-';
                        i67 = -i67;
                        i76++;
                    }
                    if (i67 >= 10) {
                        i10 = 48;
                        cArr[i76] = (char) ((i67 / 10) + 48);
                        i76++;
                    } else {
                        i10 = 48;
                    }
                    i9 = i76 + 1;
                    cArr[i76] = (char) ((i67 % 10) + i10);
                } else {
                    int i77 = 48;
                    if (i67 < 0) {
                        int i78 = i8 + 1;
                        cArr[i8] = '0';
                        int i79 = i78 + 1;
                        cArr[i78] = Operators.DOT;
                        int i80 = -1;
                        while (i80 > i67) {
                            cArr[i79] = '0';
                            i80--;
                            i79++;
                        }
                        int i81 = i79;
                        int i82 = 0;
                        while (i82 < i72) {
                            cArr[((i79 + i72) - i82) - 1] = (char) ((i71 % 10) + i77);
                            i71 /= 10;
                            i81++;
                            i82++;
                            i77 = 48;
                        }
                        i9 = i81;
                    } else {
                        int i83 = i67 + 1;
                        if (i83 >= i72) {
                            for (int i84 = 0; i84 < i72; i84++) {
                                cArr[((i8 + i72) - i84) - 1] = (char) ((i71 % 10) + 48);
                                i71 /= 10;
                            }
                            int i85 = i8 + i72;
                            while (i72 < i83) {
                                cArr[i85] = '0';
                                i72++;
                                i85++;
                            }
                            int i86 = i85 + 1;
                            cArr[i85] = Operators.DOT;
                            i9 = i86 + 1;
                            cArr[i86] = '0';
                        } else {
                            int i87 = i8 + 1;
                            for (int i88 = 0; i88 < i72; i88++) {
                                if ((i72 - i88) - 1 == i67) {
                                    cArr[((i87 + i72) - i88) - 1] = Operators.DOT;
                                    i87--;
                                }
                                cArr[((i87 + i72) - i88) - 1] = (char) ((i71 % 10) + 48);
                                i71 /= 10;
                            }
                            i9 = i8 + i72 + 1;
                        }
                    }
                }
                return i9 - i;
            }
        }
        return i16 - i;
    }
}
