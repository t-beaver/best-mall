package com.dcloud.zxing2.pdf417.decoder;

import com.dcloud.zxing2.FormatException;
import com.dcloud.zxing2.common.CharacterSetECI;
import com.dcloud.zxing2.common.DecoderResult;
import com.dcloud.zxing2.pdf417.PDF417ResultMetadata;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.ui.component.list.template.TemplateDom;
import com.taobao.weex.utils.WXUtils;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import net.lingala.zip4j.util.InternalZipConstants;

final class DecodedBitStreamParser {
    private static final int AL = 28;
    private static final int AS = 27;
    private static final int BEGIN_MACRO_PDF417_CONTROL_BLOCK = 928;
    private static final int BEGIN_MACRO_PDF417_OPTIONAL_FIELD = 923;
    private static final int BYTE_COMPACTION_MODE_LATCH = 901;
    private static final int BYTE_COMPACTION_MODE_LATCH_6 = 924;
    private static final Charset DEFAULT_ENCODING = Charset.forName(InternalZipConstants.AES_HASH_CHARSET);
    private static final int ECI_CHARSET = 927;
    private static final int ECI_GENERAL_PURPOSE = 926;
    private static final int ECI_USER_DEFINED = 925;
    private static final BigInteger[] EXP900;
    private static final int LL = 27;
    private static final int MACRO_PDF417_TERMINATOR = 922;
    private static final int MAX_NUMERIC_CODEWORDS = 15;
    private static final char[] MIXED_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '&', 13, 9, Operators.ARRAY_SEPRATOR, Operators.CONDITION_IF_MIDDLE, '#', '-', Operators.DOT, Operators.DOLLAR, '/', '+', WXUtils.PERCENT, '*', '=', '^'};
    private static final int ML = 28;
    private static final int MODE_SHIFT_TO_BYTE_COMPACTION_MODE = 913;
    private static final int NUMBER_OF_SEQUENCE_CODEWORDS = 2;
    private static final int NUMERIC_COMPACTION_MODE_LATCH = 902;
    private static final int PAL = 29;
    private static final int PL = 25;
    private static final int PS = 29;
    private static final char[] PUNCT_CHARS = {';', '<', '>', TemplateDom.SEPARATOR, Operators.ARRAY_START, '\\', Operators.ARRAY_END, '_', '`', '~', '!', 13, 9, Operators.ARRAY_SEPRATOR, Operators.CONDITION_IF_MIDDLE, 10, '-', Operators.DOT, Operators.DOLLAR, '/', Operators.QUOTE, '|', '*', Operators.BRACKET_START, Operators.BRACKET_END, Operators.CONDITION_IF, Operators.BLOCK_START, Operators.BLOCK_END, Operators.SINGLE_QUOTE};
    private static final int TEXT_COMPACTION_MODE_LATCH = 900;

    /* renamed from: com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$dcloud$zxing2$pdf417$decoder$DecodedBitStreamParser$Mode;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|14) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode[] r0 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$dcloud$zxing2$pdf417$decoder$DecodedBitStreamParser$Mode = r0
                com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$pdf417$decoder$DecodedBitStreamParser$Mode     // Catch:{ NoSuchFieldError -> 0x001d }
                com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.LOWER     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$pdf417$decoder$DecodedBitStreamParser$Mode     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.MIXED     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$pdf417$decoder$DecodedBitStreamParser$Mode     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.PUNCT     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$pdf417$decoder$DecodedBitStreamParser$Mode     // Catch:{ NoSuchFieldError -> 0x003e }
                com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA_SHIFT     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$pdf417$decoder$DecodedBitStreamParser$Mode     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.PUNCT_SHIFT     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.AnonymousClass1.<clinit>():void");
        }
    }

    private enum Mode {
        ALPHA,
        LOWER,
        MIXED,
        PUNCT,
        ALPHA_SHIFT,
        PUNCT_SHIFT
    }

    static {
        BigInteger[] bigIntegerArr = new BigInteger[16];
        EXP900 = bigIntegerArr;
        bigIntegerArr[0] = BigInteger.ONE;
        BigInteger valueOf = BigInteger.valueOf(900);
        bigIntegerArr[1] = valueOf;
        int i = 2;
        while (true) {
            BigInteger[] bigIntegerArr2 = EXP900;
            if (i < bigIntegerArr2.length) {
                bigIntegerArr2[i] = bigIntegerArr2[i - 1].multiply(valueOf);
                i++;
            } else {
                return;
            }
        }
    }

    private DecodedBitStreamParser() {
    }

    private static int byteCompaction(int i, int[] iArr, Charset charset, int i2, StringBuilder sb) {
        int i3;
        long j;
        int i4;
        int i5;
        int i6 = i;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i7 = MACRO_PDF417_TERMINATOR;
        int i8 = BEGIN_MACRO_PDF417_OPTIONAL_FIELD;
        int i9 = 928;
        int i10 = NUMERIC_COMPACTION_MODE_LATCH;
        long j2 = 900;
        if (i6 == BYTE_COMPACTION_MODE_LATCH) {
            int[] iArr2 = new int[6];
            int i11 = i2 + 1;
            int i12 = iArr[i2];
            boolean z = false;
            loop0:
            while (true) {
                i4 = 0;
                long j3 = 0;
                while (i3 < iArr[0] && !z) {
                    int i13 = i4 + 1;
                    iArr2[i4] = i12;
                    j3 = (j3 * j) + ((long) i12);
                    int i14 = i3 + 1;
                    i12 = iArr[i3];
                    if (i12 == TEXT_COMPACTION_MODE_LATCH || i12 == BYTE_COMPACTION_MODE_LATCH || i12 == NUMERIC_COMPACTION_MODE_LATCH || i12 == BYTE_COMPACTION_MODE_LATCH_6 || i12 == 928 || i12 == i8 || i12 == i7) {
                        i3 = i14 - 1;
                        i4 = i13;
                        i7 = MACRO_PDF417_TERMINATOR;
                        i8 = BEGIN_MACRO_PDF417_OPTIONAL_FIELD;
                        j = 900;
                        z = true;
                    } else if (i13 % 5 != 0 || i13 <= 0) {
                        i3 = i14;
                        i4 = i13;
                        i7 = MACRO_PDF417_TERMINATOR;
                        i8 = BEGIN_MACRO_PDF417_OPTIONAL_FIELD;
                        j = 900;
                    } else {
                        int i15 = 0;
                        while (i15 < 6) {
                            byteArrayOutputStream.write((byte) ((int) (j3 >> ((5 - i15) * 8))));
                            i15++;
                            i7 = MACRO_PDF417_TERMINATOR;
                            i8 = BEGIN_MACRO_PDF417_OPTIONAL_FIELD;
                        }
                        i11 = i14;
                        j2 = 900;
                    }
                }
            }
            if (i3 != iArr[0] || i12 >= TEXT_COMPACTION_MODE_LATCH) {
                i5 = i4;
            } else {
                i5 = i4 + 1;
                iArr2[i4] = i12;
            }
            for (int i16 = 0; i16 < i5; i16++) {
                byteArrayOutputStream.write((byte) iArr2[i16]);
            }
        } else if (i6 == BYTE_COMPACTION_MODE_LATCH_6) {
            int i17 = i2;
            boolean z2 = false;
            int i18 = 0;
            long j4 = 0;
            while (i17 < iArr[0] && !z2) {
                int i19 = i17 + 1;
                int i20 = iArr[i17];
                if (i20 < TEXT_COMPACTION_MODE_LATCH) {
                    i18++;
                    j4 = (j4 * 900) + ((long) i20);
                    i17 = i19;
                } else {
                    if (i20 != TEXT_COMPACTION_MODE_LATCH && i20 != BYTE_COMPACTION_MODE_LATCH && i20 != i10 && i20 != BYTE_COMPACTION_MODE_LATCH_6 && i20 != i9) {
                        if (!(i20 == BEGIN_MACRO_PDF417_OPTIONAL_FIELD || i20 == MACRO_PDF417_TERMINATOR)) {
                            i17 = i19;
                        }
                    }
                    i17 = i19 - 1;
                    z2 = true;
                }
                if (i18 % 5 == 0 && i18 > 0) {
                    for (int i21 = 0; i21 < 6; i21++) {
                        byteArrayOutputStream.write((byte) ((int) (j4 >> ((5 - i21) * 8))));
                    }
                    i18 = 0;
                    j4 = 0;
                }
                i9 = 928;
                i10 = NUMERIC_COMPACTION_MODE_LATCH;
            }
            i3 = i17;
        } else {
            i3 = i2;
        }
        sb.append(new String(byteArrayOutputStream.toByteArray(), charset));
        return i3;
    }

    static DecoderResult decode(int[] iArr, String str) throws FormatException {
        int i;
        int i2 = 2;
        StringBuilder sb = new StringBuilder(iArr.length * 2);
        Charset forName = Charset.forName("UTF-8");
        int i3 = iArr[1];
        PDF417ResultMetadata pDF417ResultMetadata = new PDF417ResultMetadata();
        while (i2 < iArr[0]) {
            if (i3 != MODE_SHIFT_TO_BYTE_COMPACTION_MODE) {
                switch (i3) {
                    case TEXT_COMPACTION_MODE_LATCH /*900*/:
                        i = textCompaction(iArr, i2, sb);
                        break;
                    case BYTE_COMPACTION_MODE_LATCH /*901*/:
                        i = byteCompaction(i3, iArr, forName, i2, sb);
                        break;
                    case NUMERIC_COMPACTION_MODE_LATCH /*902*/:
                        i = numericCompaction(iArr, i2, sb);
                        break;
                    default:
                        switch (i3) {
                            case MACRO_PDF417_TERMINATOR /*922*/:
                            case BEGIN_MACRO_PDF417_OPTIONAL_FIELD /*923*/:
                                throw FormatException.getFormatInstance();
                            case BYTE_COMPACTION_MODE_LATCH_6 /*924*/:
                                break;
                            case ECI_USER_DEFINED /*925*/:
                                i = i2 + 1;
                                break;
                            case ECI_GENERAL_PURPOSE /*926*/:
                                i = i2 + 2;
                                break;
                            case ECI_CHARSET /*927*/:
                                Charset forName2 = Charset.forName(CharacterSetECI.getCharacterSetECIByValue(iArr[i2]).name());
                                i = i2 + 1;
                                forName = forName2;
                                break;
                            case 928:
                                i = decodeMacroBlock(iArr, i2, pDF417ResultMetadata);
                                break;
                            default:
                                i = textCompaction(iArr, i2 - 1, sb);
                                break;
                        }
                        i = byteCompaction(i3, iArr, forName, i2, sb);
                        break;
                }
            } else {
                sb.append((char) iArr[i2]);
                i = i2 + 1;
            }
            if (i < iArr.length) {
                int i4 = i + 1;
                i3 = iArr[i];
                i2 = i4;
            } else {
                throw FormatException.getFormatInstance();
            }
        }
        if (sb.length() != 0) {
            DecoderResult decoderResult = new DecoderResult((byte[]) null, sb.toString(), (List<byte[]>) null, str);
            decoderResult.setOther(pDF417ResultMetadata);
            return decoderResult;
        }
        throw FormatException.getFormatInstance();
    }

    private static String decodeBase900toBase10(int[] iArr, int i) throws FormatException {
        BigInteger bigInteger = BigInteger.ZERO;
        for (int i2 = 0; i2 < i; i2++) {
            bigInteger = bigInteger.add(EXP900[(i - i2) - 1].multiply(BigInteger.valueOf((long) iArr[i2])));
        }
        String bigInteger2 = bigInteger.toString();
        if (bigInteger2.charAt(0) == '1') {
            return bigInteger2.substring(1);
        }
        throw FormatException.getFormatInstance();
    }

    private static int decodeMacroBlock(int[] iArr, int i, PDF417ResultMetadata pDF417ResultMetadata) throws FormatException {
        if (i + 2 <= iArr[0]) {
            int[] iArr2 = new int[2];
            int i2 = 0;
            while (i2 < 2) {
                iArr2[i2] = iArr[i];
                i2++;
                i++;
            }
            pDF417ResultMetadata.setSegmentIndex(Integer.parseInt(decodeBase900toBase10(iArr2, 2)));
            StringBuilder sb = new StringBuilder();
            int textCompaction = textCompaction(iArr, i, sb);
            pDF417ResultMetadata.setFileId(sb.toString());
            if (iArr[textCompaction] == BEGIN_MACRO_PDF417_OPTIONAL_FIELD) {
                int i3 = textCompaction + 1;
                int[] iArr3 = new int[(iArr[0] - i3)];
                boolean z = false;
                int i4 = 0;
                while (i3 < iArr[0] && !z) {
                    int i5 = i3 + 1;
                    int i6 = iArr[i3];
                    if (i6 < TEXT_COMPACTION_MODE_LATCH) {
                        iArr3[i4] = i6;
                        i3 = i5;
                        i4++;
                    } else if (i6 == MACRO_PDF417_TERMINATOR) {
                        pDF417ResultMetadata.setLastSegment(true);
                        i3 = i5 + 1;
                        z = true;
                    } else {
                        throw FormatException.getFormatInstance();
                    }
                }
                pDF417ResultMetadata.setOptionalData(Arrays.copyOf(iArr3, i4));
                return i3;
            } else if (iArr[textCompaction] != MACRO_PDF417_TERMINATOR) {
                return textCompaction;
            } else {
                pDF417ResultMetadata.setLastSegment(true);
                return textCompaction + 1;
            }
        } else {
            throw FormatException.getFormatInstance();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0047, code lost:
        r8 = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0048, code lost:
        r1 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0053, code lost:
        r1 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0080, code lost:
        r8 = 0;
        r15 = r3;
        r3 = r1;
        r1 = r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00b4, code lost:
        r8 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00d2, code lost:
        r8 = (char) r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x00f4, code lost:
        r15 = r3;
        r3 = r1;
        r1 = r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x00f8, code lost:
        r8 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x00f9, code lost:
        if (r8 == 0) goto L_0x00fe;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x00fb, code lost:
        r0.append(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x00fe, code lost:
        r5 = r5 + 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void decodeTextCompaction(int[] r16, int[] r17, int r18, java.lang.StringBuilder r19) {
        /*
            r0 = r19
            com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            r2 = 0
            r4 = r18
            r3 = r1
            r5 = 0
        L_0x0009:
            if (r5 >= r4) goto L_0x0102
            r6 = r16[r5]
            int[] r7 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.AnonymousClass1.$SwitchMap$com$dcloud$zxing2$pdf417$decoder$DecodedBitStreamParser$Mode
            int r8 = r1.ordinal()
            r7 = r7[r8]
            r8 = 32
            r9 = 28
            r10 = 27
            r11 = 913(0x391, float:1.28E-42)
            r12 = 900(0x384, float:1.261E-42)
            r13 = 29
            r14 = 26
            switch(r7) {
                case 1: goto L_0x00ce;
                case 2: goto L_0x00a8;
                case 3: goto L_0x0072;
                case 4: goto L_0x0056;
                case 5: goto L_0x0042;
                case 6: goto L_0x0028;
                default: goto L_0x0026;
            }
        L_0x0026:
            goto L_0x00f8
        L_0x0028:
            if (r6 >= r13) goto L_0x002f
            char[] r1 = PUNCT_CHARS
            char r1 = r1[r6]
            goto L_0x0047
        L_0x002f:
            if (r6 != r13) goto L_0x0034
            com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            goto L_0x0080
        L_0x0034:
            if (r6 != r11) goto L_0x003d
            r1 = r17[r5]
            char r1 = (char) r1
            r0.append(r1)
            goto L_0x0053
        L_0x003d:
            if (r6 != r12) goto L_0x0053
            com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            goto L_0x0080
        L_0x0042:
            if (r6 >= r14) goto L_0x004b
            int r6 = r6 + 65
            char r1 = (char) r6
        L_0x0047:
            r8 = r1
        L_0x0048:
            r1 = r3
            goto L_0x00f4
        L_0x004b:
            if (r6 != r14) goto L_0x004e
            goto L_0x0048
        L_0x004e:
            if (r6 != r12) goto L_0x0053
            com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            goto L_0x0080
        L_0x0053:
            r1 = r3
            goto L_0x00b4
        L_0x0056:
            if (r6 >= r13) goto L_0x005e
            char[] r7 = PUNCT_CHARS
            char r8 = r7[r6]
            goto L_0x00f9
        L_0x005e:
            if (r6 != r13) goto L_0x0063
            com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            goto L_0x0080
        L_0x0063:
            if (r6 != r11) goto L_0x006d
            r6 = r17[r5]
            char r6 = (char) r6
            r0.append(r6)
            goto L_0x00f8
        L_0x006d:
            if (r6 != r12) goto L_0x00f8
            com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            goto L_0x0080
        L_0x0072:
            r7 = 25
            if (r6 >= r7) goto L_0x007c
            char[] r7 = MIXED_CHARS
            char r8 = r7[r6]
            goto L_0x00f9
        L_0x007c:
            if (r6 != r7) goto L_0x0086
            com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.PUNCT
        L_0x0080:
            r8 = 0
            r15 = r3
            r3 = r1
            r1 = r15
            goto L_0x00f4
        L_0x0086:
            if (r6 != r14) goto L_0x008a
            goto L_0x00f9
        L_0x008a:
            if (r6 != r10) goto L_0x008f
            com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.LOWER
            goto L_0x0080
        L_0x008f:
            if (r6 != r9) goto L_0x0094
            com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            goto L_0x0080
        L_0x0094:
            if (r6 != r13) goto L_0x0099
            com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r3 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.PUNCT_SHIFT
            goto L_0x00b4
        L_0x0099:
            if (r6 != r11) goto L_0x00a3
            r6 = r17[r5]
            char r6 = (char) r6
            r0.append(r6)
            goto L_0x00f8
        L_0x00a3:
            if (r6 != r12) goto L_0x00f8
            com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            goto L_0x0080
        L_0x00a8:
            if (r6 >= r14) goto L_0x00ad
            int r6 = r6 + 97
            goto L_0x00d2
        L_0x00ad:
            if (r6 != r14) goto L_0x00b0
            goto L_0x00f9
        L_0x00b0:
            if (r6 != r10) goto L_0x00b6
            com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r3 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA_SHIFT
        L_0x00b4:
            r8 = 0
            goto L_0x00f4
        L_0x00b6:
            if (r6 != r9) goto L_0x00bb
            com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.MIXED
            goto L_0x0080
        L_0x00bb:
            if (r6 != r13) goto L_0x00c0
            com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r3 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.PUNCT_SHIFT
            goto L_0x00b4
        L_0x00c0:
            if (r6 != r11) goto L_0x00c9
            r6 = r17[r5]
            char r6 = (char) r6
            r0.append(r6)
            goto L_0x00f8
        L_0x00c9:
            if (r6 != r12) goto L_0x00f8
            com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            goto L_0x0080
        L_0x00ce:
            if (r6 >= r14) goto L_0x00d4
            int r6 = r6 + 65
        L_0x00d2:
            char r8 = (char) r6
            goto L_0x00f9
        L_0x00d4:
            if (r6 != r14) goto L_0x00d7
            goto L_0x00f9
        L_0x00d7:
            if (r6 != r10) goto L_0x00dc
            com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.LOWER
            goto L_0x0080
        L_0x00dc:
            if (r6 != r9) goto L_0x00e1
            com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.MIXED
            goto L_0x0080
        L_0x00e1:
            if (r6 != r13) goto L_0x00e6
            com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r3 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.PUNCT_SHIFT
            goto L_0x00b4
        L_0x00e6:
            if (r6 != r11) goto L_0x00ef
            r6 = r17[r5]
            char r6 = (char) r6
            r0.append(r6)
            goto L_0x00f8
        L_0x00ef:
            if (r6 != r12) goto L_0x00f8
            com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser$Mode r1 = com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.Mode.ALPHA
            goto L_0x0080
        L_0x00f4:
            r15 = r3
            r3 = r1
            r1 = r15
            goto L_0x00f9
        L_0x00f8:
            r8 = 0
        L_0x00f9:
            if (r8 == 0) goto L_0x00fe
            r0.append(r8)
        L_0x00fe:
            int r5 = r5 + 1
            goto L_0x0009
        L_0x0102:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dcloud.zxing2.pdf417.decoder.DecodedBitStreamParser.decodeTextCompaction(int[], int[], int, java.lang.StringBuilder):void");
    }

    private static int textCompaction(int[] iArr, int i, StringBuilder sb) {
        int[] iArr2 = new int[((iArr[0] - i) * 2)];
        int[] iArr3 = new int[((iArr[0] - i) * 2)];
        boolean z = false;
        int i2 = 0;
        while (i < iArr[0] && !z) {
            int i3 = i + 1;
            int i4 = iArr[i];
            if (i4 < TEXT_COMPACTION_MODE_LATCH) {
                iArr2[i2] = i4 / 30;
                iArr2[i2 + 1] = i4 % 30;
                i2 += 2;
            } else if (i4 != MODE_SHIFT_TO_BYTE_COMPACTION_MODE) {
                if (i4 != 928) {
                    switch (i4) {
                        case TEXT_COMPACTION_MODE_LATCH /*900*/:
                            iArr2[i2] = TEXT_COMPACTION_MODE_LATCH;
                            i2++;
                            break;
                        case BYTE_COMPACTION_MODE_LATCH /*901*/:
                        case NUMERIC_COMPACTION_MODE_LATCH /*902*/:
                            break;
                        default:
                            switch (i4) {
                                case MACRO_PDF417_TERMINATOR /*922*/:
                                case BEGIN_MACRO_PDF417_OPTIONAL_FIELD /*923*/:
                                case BYTE_COMPACTION_MODE_LATCH_6 /*924*/:
                                    break;
                            }
                    }
                }
                i = i3 - 1;
                z = true;
            } else {
                iArr2[i2] = MODE_SHIFT_TO_BYTE_COMPACTION_MODE;
                i = i3 + 1;
                iArr3[i2] = iArr[i3];
                i2++;
            }
            i = i3;
        }
        decodeTextCompaction(iArr2, iArr3, i2, sb);
        return i;
    }

    private static int numericCompaction(int[] iArr, int i, StringBuilder sb) throws FormatException {
        int[] iArr2 = new int[15];
        boolean z = false;
        int i2 = 0;
        while (i < iArr[0] && !z) {
            int i3 = i + 1;
            int i4 = iArr[i];
            if (i3 == iArr[0]) {
                z = true;
            }
            if (i4 < TEXT_COMPACTION_MODE_LATCH) {
                iArr2[i2] = i4;
                i2++;
            } else if (i4 == TEXT_COMPACTION_MODE_LATCH || i4 == BYTE_COMPACTION_MODE_LATCH || i4 == BYTE_COMPACTION_MODE_LATCH_6 || i4 == 928 || i4 == BEGIN_MACRO_PDF417_OPTIONAL_FIELD || i4 == MACRO_PDF417_TERMINATOR) {
                i3--;
                z = true;
            }
            if ((i2 % 15 == 0 || i4 == NUMERIC_COMPACTION_MODE_LATCH || z) && i2 > 0) {
                sb.append(decodeBase900toBase10(iArr2, i2));
                i2 = 0;
            }
            i = i3;
        }
        return i;
    }
}
