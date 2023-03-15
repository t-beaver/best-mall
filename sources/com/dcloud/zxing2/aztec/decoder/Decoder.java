package com.dcloud.zxing2.aztec.decoder;

import com.dcloud.zxing2.FormatException;
import com.dcloud.zxing2.aztec.AztecDetectorResult;
import com.dcloud.zxing2.common.BitMatrix;
import com.dcloud.zxing2.common.DecoderResult;
import com.dcloud.zxing2.common.reedsolomon.GenericGF;
import com.dcloud.zxing2.common.reedsolomon.ReedSolomonDecoder;
import com.dcloud.zxing2.common.reedsolomon.ReedSolomonException;
import com.facebook.common.callercontext.ContextChain;
import com.taobao.weex.common.Constants;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.ui.component.WXComponent;
import io.dcloud.common.util.CreateShortResultReceiver;
import io.dcloud.common.util.ExifInterface;
import io.dcloud.common.util.JSUtil;
import java.util.Arrays;
import java.util.List;

public final class Decoder {
    private static final String[] DIGIT_TABLE = {"CTRL_PS", Operators.SPACE_STR, WXInstanceApm.VALUE_ERROR_CODE_DEFAULT, "1", ExifInterface.GPS_MEASUREMENT_2D, ExifInterface.GPS_MEASUREMENT_3D, "4", "5", "6", "7", "8", "9", ",", Operators.DOT_STR, "CTRL_UL", "CTRL_US"};
    private static final String[] LOWER_TABLE = {"CTRL_PS", Operators.SPACE_STR, "a", "b", c.a, "d", "e", "f", "g", "h", ContextChain.TAG_INFRA, "j", "k", "l", WXComponent.PROP_FS_MATCH_PARENT, "n", "o", ContextChain.TAG_PRODUCT, "q", "r", "s", "t", "u", CreateShortResultReceiver.KEY_VERSIONNAME, WXComponent.PROP_FS_WRAP_CONTENT, Constants.Name.X, Constants.Name.Y, "z", "CTRL_US", "CTRL_ML", "CTRL_DL", "CTRL_BS"};
    private static final String[] MIXED_TABLE = {"CTRL_PS", Operators.SPACE_STR, "\u0001", "\u0002", "\u0003", "\u0004", "\u0005", "\u0006", "\u0007", "\b", "\t", "\n", "\u000b", "\f", "\r", "\u001b", "\u001c", "\u001d", "\u001e", "\u001f", "@", "\\", "^", "_", "`", "|", "~", "", "CTRL_LL", "CTRL_UL", "CTRL_PL", "CTRL_BS"};
    private static final String[] PUNCT_TABLE = {"", "\r", "\r\n", ". ", ", ", ": ", Operators.AND_NOT, JSUtil.QUOTE, "#", Operators.DOLLAR_STR, "%", "&", "'", Operators.BRACKET_START_STR, Operators.BRACKET_END_STR, "*", Operators.PLUS, ",", Operators.SUB, Operators.DOT_STR, "/", ":", ";", Operators.L, "=", Operators.G, Operators.CONDITION_IF_STRING, Operators.ARRAY_START_STR, Operators.ARRAY_END_STR, Operators.BLOCK_START_STR, Operators.BLOCK_END_STR, "CTRL_UL"};
    private static final String[] UPPER_TABLE = {"CTRL_PS", Operators.SPACE_STR, ExifInterface.GPS_MEASUREMENT_IN_PROGRESS, "B", "C", "D", ExifInterface.LONGITUDE_EAST, "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", ExifInterface.LATITUDE_SOUTH, ExifInterface.GPS_DIRECTION_TRUE, "U", ExifInterface.GPS_MEASUREMENT_INTERRUPTED, ExifInterface.LONGITUDE_WEST, "X", "Y", "Z", "CTRL_LL", "CTRL_ML", "CTRL_DL", "CTRL_BS"};
    private AztecDetectorResult ddata;

    /* renamed from: com.dcloud.zxing2.aztec.decoder.Decoder$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$dcloud$zxing2$aztec$decoder$Decoder$Table;

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.dcloud.zxing2.aztec.decoder.Decoder$Table[] r0 = com.dcloud.zxing2.aztec.decoder.Decoder.Table.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$dcloud$zxing2$aztec$decoder$Decoder$Table = r0
                com.dcloud.zxing2.aztec.decoder.Decoder$Table r1 = com.dcloud.zxing2.aztec.decoder.Decoder.Table.UPPER     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$aztec$decoder$Decoder$Table     // Catch:{ NoSuchFieldError -> 0x001d }
                com.dcloud.zxing2.aztec.decoder.Decoder$Table r1 = com.dcloud.zxing2.aztec.decoder.Decoder.Table.LOWER     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$aztec$decoder$Decoder$Table     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.dcloud.zxing2.aztec.decoder.Decoder$Table r1 = com.dcloud.zxing2.aztec.decoder.Decoder.Table.MIXED     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$aztec$decoder$Decoder$Table     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.dcloud.zxing2.aztec.decoder.Decoder$Table r1 = com.dcloud.zxing2.aztec.decoder.Decoder.Table.PUNCT     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$aztec$decoder$Decoder$Table     // Catch:{ NoSuchFieldError -> 0x003e }
                com.dcloud.zxing2.aztec.decoder.Decoder$Table r1 = com.dcloud.zxing2.aztec.decoder.Decoder.Table.DIGIT     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.dcloud.zxing2.aztec.decoder.Decoder.AnonymousClass1.<clinit>():void");
        }
    }

    private enum Table {
        UPPER,
        LOWER,
        MIXED,
        DIGIT,
        PUNCT,
        BINARY
    }

    private boolean[] correctBits(boolean[] zArr) throws FormatException {
        GenericGF genericGF;
        int i = 8;
        if (this.ddata.getNbLayers() <= 2) {
            i = 6;
            genericGF = GenericGF.AZTEC_DATA_6;
        } else if (this.ddata.getNbLayers() <= 8) {
            genericGF = GenericGF.AZTEC_DATA_8;
        } else if (this.ddata.getNbLayers() <= 22) {
            i = 10;
            genericGF = GenericGF.AZTEC_DATA_10;
        } else {
            i = 12;
            genericGF = GenericGF.AZTEC_DATA_12;
        }
        int nbDatablocks = this.ddata.getNbDatablocks();
        int length = zArr.length / i;
        if (length >= nbDatablocks) {
            int length2 = zArr.length % i;
            int i2 = length - nbDatablocks;
            int[] iArr = new int[length];
            int i3 = 0;
            while (i3 < length) {
                iArr[i3] = readCode(zArr, length2, i);
                i3++;
                length2 += i;
            }
            try {
                new ReedSolomonDecoder(genericGF).decode(iArr, i2);
                int i4 = (1 << i) - 1;
                int i5 = 0;
                for (int i6 = 0; i6 < nbDatablocks; i6++) {
                    int i7 = iArr[i6];
                    if (i7 == 0 || i7 == i4) {
                        throw FormatException.getFormatInstance();
                    }
                    if (i7 == 1 || i7 == i4 - 1) {
                        i5++;
                    }
                }
                boolean[] zArr2 = new boolean[((nbDatablocks * i) - i5)];
                int i8 = 0;
                for (int i9 = 0; i9 < nbDatablocks; i9++) {
                    int i10 = iArr[i9];
                    if (i10 == 1 || i10 == i4 - 1) {
                        Arrays.fill(zArr2, i8, (i8 + i) - 1, i10 > 1);
                        i8 += i - 1;
                    } else {
                        int i11 = i - 1;
                        while (i11 >= 0) {
                            int i12 = i8 + 1;
                            zArr2[i8] = ((1 << i11) & i10) != 0;
                            i11--;
                            i8 = i12;
                        }
                    }
                }
                return zArr2;
            } catch (ReedSolomonException e) {
                throw FormatException.getFormatInstance(e);
            }
        } else {
            throw FormatException.getFormatInstance();
        }
    }

    private static String getCharacter(Table table, int i) {
        int i2 = AnonymousClass1.$SwitchMap$com$dcloud$zxing2$aztec$decoder$Decoder$Table[table.ordinal()];
        if (i2 == 1) {
            return UPPER_TABLE[i];
        }
        if (i2 == 2) {
            return LOWER_TABLE[i];
        }
        if (i2 == 3) {
            return MIXED_TABLE[i];
        }
        if (i2 == 4) {
            return PUNCT_TABLE[i];
        }
        if (i2 == 5) {
            return DIGIT_TABLE[i];
        }
        throw new IllegalStateException("Bad table");
    }

    private static String getEncodedData(boolean[] zArr) {
        int length = zArr.length;
        Table table = Table.UPPER;
        StringBuilder sb = new StringBuilder(20);
        Table table2 = table;
        int i = 0;
        while (i < length) {
            if (table != Table.BINARY) {
                int i2 = table == Table.DIGIT ? 4 : 5;
                if (length - i < i2) {
                    break;
                }
                int readCode = readCode(zArr, i, i2);
                i += i2;
                String character = getCharacter(table, readCode);
                if (character.startsWith("CTRL_")) {
                    Table table3 = getTable(character.charAt(5));
                    if (character.charAt(6) == 'L') {
                        table = table3;
                        table2 = table;
                    } else {
                        table = table3;
                    }
                } else {
                    sb.append(character);
                }
            } else if (length - i < 5) {
                break;
            } else {
                int readCode2 = readCode(zArr, i, 5);
                i += 5;
                if (readCode2 == 0) {
                    if (length - i < 11) {
                        break;
                    }
                    readCode2 = readCode(zArr, i, 11) + 31;
                    i += 11;
                }
                int i3 = 0;
                while (true) {
                    if (i3 >= readCode2) {
                        break;
                    } else if (length - i < 8) {
                        i = length;
                        break;
                    } else {
                        sb.append((char) readCode(zArr, i, 8));
                        i += 8;
                        i3++;
                    }
                }
            }
            table = table2;
        }
        return sb.toString();
    }

    private static Table getTable(char c) {
        if (c == 'B') {
            return Table.BINARY;
        }
        if (c == 'D') {
            return Table.DIGIT;
        }
        if (c == 'P') {
            return Table.PUNCT;
        }
        if (c == 'L') {
            return Table.LOWER;
        }
        if (c != 'M') {
            return Table.UPPER;
        }
        return Table.MIXED;
    }

    public static String highLevelDecode(boolean[] zArr) {
        return getEncodedData(zArr);
    }

    private static int readCode(boolean[] zArr, int i, int i2) {
        int i3 = 0;
        for (int i4 = i; i4 < i + i2; i4++) {
            i3 <<= 1;
            if (zArr[i4]) {
                i3 |= 1;
            }
        }
        return i3;
    }

    private static int totalBitsInLayer(int i, boolean z) {
        return ((z ? 88 : 112) + (i * 16)) * i;
    }

    public DecoderResult decode(AztecDetectorResult aztecDetectorResult) throws FormatException {
        this.ddata = aztecDetectorResult;
        return new DecoderResult((byte[]) null, getEncodedData(correctBits(extractBits(aztecDetectorResult.getBits()))), (List<byte[]>) null, (String) null);
    }

    /* access modifiers changed from: package-private */
    public boolean[] extractBits(BitMatrix bitMatrix) {
        BitMatrix bitMatrix2 = bitMatrix;
        boolean isCompact = this.ddata.isCompact();
        int nbLayers = this.ddata.getNbLayers();
        int i = nbLayers * 4;
        int i2 = isCompact ? i + 11 : i + 14;
        int[] iArr = new int[i2];
        boolean[] zArr = new boolean[totalBitsInLayer(nbLayers, isCompact)];
        int i3 = 2;
        if (isCompact) {
            for (int i4 = 0; i4 < i2; i4++) {
                iArr[i4] = i4;
            }
        } else {
            int i5 = i2 / 2;
            int i6 = ((i2 + 1) + (((i5 - 1) / 15) * 2)) / 2;
            for (int i7 = 0; i7 < i5; i7++) {
                int i8 = (i7 / 15) + i7;
                iArr[(i5 - i7) - 1] = (i6 - i8) - 1;
                iArr[i5 + i7] = i8 + i6 + 1;
            }
        }
        int i9 = 0;
        int i10 = 0;
        while (i9 < nbLayers) {
            int i11 = (nbLayers - i9) * 4;
            int i12 = isCompact ? i11 + 9 : i11 + 12;
            int i13 = i9 * 2;
            int i14 = (i2 - 1) - i13;
            int i15 = 0;
            while (i15 < i12) {
                int i16 = i15 * 2;
                int i17 = 0;
                while (i17 < i3) {
                    int i18 = i13 + i17;
                    int i19 = i13 + i15;
                    zArr[i10 + i16 + i17] = bitMatrix2.get(iArr[i18], iArr[i19]);
                    int i20 = iArr[i19];
                    int i21 = i14 - i17;
                    zArr[(i12 * 2) + i10 + i16 + i17] = bitMatrix2.get(i20, iArr[i21]);
                    int i22 = i14 - i15;
                    zArr[(i12 * 4) + i10 + i16 + i17] = bitMatrix2.get(iArr[i21], iArr[i22]);
                    zArr[(i12 * 6) + i10 + i16 + i17] = bitMatrix2.get(iArr[i22], iArr[i18]);
                    i17++;
                    nbLayers = nbLayers;
                    isCompact = isCompact;
                    i3 = 2;
                }
                boolean z = isCompact;
                int i23 = nbLayers;
                i15++;
                i3 = 2;
            }
            boolean z2 = isCompact;
            int i24 = nbLayers;
            i10 += i12 * 8;
            i9++;
            i3 = 2;
        }
        return zArr;
    }
}
