package com.dcloud.zxing2.qrcode.encoder;

import com.dcloud.zxing2.EncodeHintType;
import com.dcloud.zxing2.WriterException;
import com.dcloud.zxing2.common.BitArray;
import com.dcloud.zxing2.common.CharacterSetECI;
import com.dcloud.zxing2.common.reedsolomon.GenericGF;
import com.dcloud.zxing2.common.reedsolomon.ReedSolomonEncoder;
import com.dcloud.zxing2.qrcode.decoder.ErrorCorrectionLevel;
import com.dcloud.zxing2.qrcode.decoder.Mode;
import com.dcloud.zxing2.qrcode.decoder.Version;
import io.dcloud.common.DHInterface.IApp;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

public final class Encoder {
    private static final int[] ALPHANUMERIC_TABLE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 36, -1, -1, -1, 37, 38, -1, -1, -1, -1, 39, 40, -1, 41, 42, 43, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 44, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, -1, -1, -1, -1, -1};
    static final String DEFAULT_BYTE_MODE_ENCODING = "ISO-8859-1";

    /* renamed from: com.dcloud.zxing2.qrcode.encoder.Encoder$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$dcloud$zxing2$qrcode$decoder$Mode;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                com.dcloud.zxing2.qrcode.decoder.Mode[] r0 = com.dcloud.zxing2.qrcode.decoder.Mode.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$dcloud$zxing2$qrcode$decoder$Mode = r0
                com.dcloud.zxing2.qrcode.decoder.Mode r1 = com.dcloud.zxing2.qrcode.decoder.Mode.NUMERIC     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$qrcode$decoder$Mode     // Catch:{ NoSuchFieldError -> 0x001d }
                com.dcloud.zxing2.qrcode.decoder.Mode r1 = com.dcloud.zxing2.qrcode.decoder.Mode.ALPHANUMERIC     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$qrcode$decoder$Mode     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.dcloud.zxing2.qrcode.decoder.Mode r1 = com.dcloud.zxing2.qrcode.decoder.Mode.BYTE     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$dcloud$zxing2$qrcode$decoder$Mode     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.dcloud.zxing2.qrcode.decoder.Mode r1 = com.dcloud.zxing2.qrcode.decoder.Mode.KANJI     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.dcloud.zxing2.qrcode.encoder.Encoder.AnonymousClass1.<clinit>():void");
        }
    }

    private Encoder() {
    }

    static void append8BitBytes(String str, BitArray bitArray, String str2) throws WriterException {
        try {
            for (byte appendBits : str.getBytes(str2)) {
                bitArray.appendBits(appendBits, 8);
            }
        } catch (UnsupportedEncodingException e) {
            throw new WriterException((Throwable) e);
        }
    }

    static void appendAlphanumericBytes(CharSequence charSequence, BitArray bitArray) throws WriterException {
        int length = charSequence.length();
        int i = 0;
        while (i < length) {
            int alphanumericCode = getAlphanumericCode(charSequence.charAt(i));
            if (alphanumericCode != -1) {
                int i2 = i + 1;
                if (i2 < length) {
                    int alphanumericCode2 = getAlphanumericCode(charSequence.charAt(i2));
                    if (alphanumericCode2 != -1) {
                        bitArray.appendBits((alphanumericCode * 45) + alphanumericCode2, 11);
                        i += 2;
                    } else {
                        throw new WriterException();
                    }
                } else {
                    bitArray.appendBits(alphanumericCode, 6);
                    i = i2;
                }
            } else {
                throw new WriterException();
            }
        }
    }

    static void appendBytes(String str, Mode mode, BitArray bitArray, String str2) throws WriterException {
        int i = AnonymousClass1.$SwitchMap$com$dcloud$zxing2$qrcode$decoder$Mode[mode.ordinal()];
        if (i == 1) {
            appendNumericBytes(str, bitArray);
        } else if (i == 2) {
            appendAlphanumericBytes(str, bitArray);
        } else if (i == 3) {
            append8BitBytes(str, bitArray, str2);
        } else if (i == 4) {
            appendKanjiBytes(str, bitArray);
        } else {
            throw new WriterException("Invalid mode: " + mode);
        }
    }

    private static void appendECI(CharacterSetECI characterSetECI, BitArray bitArray) {
        bitArray.appendBits(Mode.ECI.getBits(), 4);
        bitArray.appendBits(characterSetECI.getValue(), 8);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0035 A[LOOP:0: B:4:0x0008->B:17:0x0035, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0044 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void appendKanjiBytes(java.lang.String r6, com.dcloud.zxing2.common.BitArray r7) throws com.dcloud.zxing2.WriterException {
        /*
            java.lang.String r0 = "Shift_JIS"
            byte[] r6 = r6.getBytes(r0)     // Catch:{ UnsupportedEncodingException -> 0x004d }
            int r0 = r6.length
            r1 = 0
        L_0x0008:
            if (r1 >= r0) goto L_0x004c
            byte r2 = r6[r1]
            r2 = r2 & 255(0xff, float:3.57E-43)
            int r3 = r1 + 1
            byte r3 = r6[r3]
            r3 = r3 & 255(0xff, float:3.57E-43)
            int r2 = r2 << 8
            r2 = r2 | r3
            r3 = 33088(0x8140, float:4.6366E-41)
            r4 = -1
            if (r2 < r3) goto L_0x0024
            r5 = 40956(0x9ffc, float:5.7392E-41)
            if (r2 > r5) goto L_0x0024
        L_0x0022:
            int r2 = r2 - r3
            goto L_0x0033
        L_0x0024:
            r3 = 57408(0xe040, float:8.0446E-41)
            if (r2 < r3) goto L_0x0032
            r3 = 60351(0xebbf, float:8.457E-41)
            if (r2 > r3) goto L_0x0032
            r3 = 49472(0xc140, float:6.9325E-41)
            goto L_0x0022
        L_0x0032:
            r2 = -1
        L_0x0033:
            if (r2 == r4) goto L_0x0044
            int r3 = r2 >> 8
            int r3 = r3 * 192
            r2 = r2 & 255(0xff, float:3.57E-43)
            int r3 = r3 + r2
            r2 = 13
            r7.appendBits(r3, r2)
            int r1 = r1 + 2
            goto L_0x0008
        L_0x0044:
            com.dcloud.zxing2.WriterException r6 = new com.dcloud.zxing2.WriterException
            java.lang.String r7 = "Invalid byte sequence"
            r6.<init>((java.lang.String) r7)
            throw r6
        L_0x004c:
            return
        L_0x004d:
            r6 = move-exception
            com.dcloud.zxing2.WriterException r7 = new com.dcloud.zxing2.WriterException
            r7.<init>((java.lang.Throwable) r6)
            goto L_0x0055
        L_0x0054:
            throw r7
        L_0x0055:
            goto L_0x0054
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dcloud.zxing2.qrcode.encoder.Encoder.appendKanjiBytes(java.lang.String, com.dcloud.zxing2.common.BitArray):void");
    }

    static void appendLengthInfo(int i, Version version, Mode mode, BitArray bitArray) throws WriterException {
        int characterCountBits = mode.getCharacterCountBits(version);
        int i2 = 1 << characterCountBits;
        if (i < i2) {
            bitArray.appendBits(i, characterCountBits);
            return;
        }
        throw new WriterException(i + " is bigger than " + (i2 - 1));
    }

    static void appendModeInfo(Mode mode, BitArray bitArray) {
        bitArray.appendBits(mode.getBits(), 4);
    }

    static void appendNumericBytes(CharSequence charSequence, BitArray bitArray) {
        int length = charSequence.length();
        int i = 0;
        while (i < length) {
            int charAt = charSequence.charAt(i) - '0';
            int i2 = i + 2;
            if (i2 < length) {
                bitArray.appendBits((charAt * 100) + ((charSequence.charAt(i + 1) - '0') * 10) + (charSequence.charAt(i2) - '0'), 10);
                i += 3;
            } else {
                i++;
                if (i < length) {
                    bitArray.appendBits((charAt * 10) + (charSequence.charAt(i) - '0'), 7);
                    i = i2;
                } else {
                    bitArray.appendBits(charAt, 4);
                }
            }
        }
    }

    private static int calculateMaskPenalty(ByteMatrix byteMatrix) {
        return MaskUtil.applyMaskPenaltyRule1(byteMatrix) + MaskUtil.applyMaskPenaltyRule2(byteMatrix) + MaskUtil.applyMaskPenaltyRule3(byteMatrix) + MaskUtil.applyMaskPenaltyRule4(byteMatrix);
    }

    private static int chooseMaskPattern(BitArray bitArray, ErrorCorrectionLevel errorCorrectionLevel, Version version, ByteMatrix byteMatrix) throws WriterException {
        int i = Integer.MAX_VALUE;
        int i2 = -1;
        for (int i3 = 0; i3 < 8; i3++) {
            MatrixUtil.buildMatrix(bitArray, errorCorrectionLevel, version, i3, byteMatrix);
            int calculateMaskPenalty = calculateMaskPenalty(byteMatrix);
            if (calculateMaskPenalty < i) {
                i2 = i3;
                i = calculateMaskPenalty;
            }
        }
        return i2;
    }

    public static Mode chooseMode(String str) {
        return chooseMode(str, (String) null);
    }

    private static Version chooseVersion(int i, ErrorCorrectionLevel errorCorrectionLevel) throws WriterException {
        for (int i2 = 1; i2 <= 40; i2++) {
            Version versionForNumber = Version.getVersionForNumber(i2);
            if (versionForNumber.getTotalCodewords() - versionForNumber.getECBlocksForLevel(errorCorrectionLevel).getTotalECCodewords() >= (i + 7) / 8) {
                return versionForNumber;
            }
        }
        throw new WriterException("Data too big");
    }

    public static QRCode encode(String str, ErrorCorrectionLevel errorCorrectionLevel) throws WriterException {
        return encode(str, errorCorrectionLevel, (Map<EncodeHintType, ?>) null);
    }

    static byte[] generateECBytes(byte[] bArr, int i) {
        int length = bArr.length;
        int[] iArr = new int[(length + i)];
        for (int i2 = 0; i2 < length; i2++) {
            iArr[i2] = bArr[i2] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE;
        }
        new ReedSolomonEncoder(GenericGF.QR_CODE_FIELD_256).encode(iArr, i);
        byte[] bArr2 = new byte[i];
        for (int i3 = 0; i3 < i; i3++) {
            bArr2[i3] = (byte) iArr[length + i3];
        }
        return bArr2;
    }

    static int getAlphanumericCode(int i) {
        int[] iArr = ALPHANUMERIC_TABLE;
        if (i < iArr.length) {
            return iArr[i];
        }
        return -1;
    }

    static void getNumDataBytesAndNumECBytesForBlockID(int i, int i2, int i3, int i4, int[] iArr, int[] iArr2) throws WriterException {
        if (i4 < i3) {
            int i5 = i % i3;
            int i6 = i3 - i5;
            int i7 = i / i3;
            int i8 = i7 + 1;
            int i9 = i2 / i3;
            int i10 = i9 + 1;
            int i11 = i7 - i9;
            int i12 = i8 - i10;
            if (i11 != i12) {
                throw new WriterException("EC bytes mismatch");
            } else if (i3 != i6 + i5) {
                throw new WriterException("RS blocks mismatch");
            } else if (i != ((i9 + i11) * i6) + ((i10 + i12) * i5)) {
                throw new WriterException("Total bytes mismatch");
            } else if (i4 < i6) {
                iArr[0] = i9;
                iArr2[0] = i11;
            } else {
                iArr[0] = i10;
                iArr2[0] = i12;
            }
        } else {
            throw new WriterException("Block ID too large");
        }
    }

    static BitArray interleaveWithECBytes(BitArray bitArray, int i, int i2, int i3) throws WriterException {
        int i4 = i;
        int i5 = i2;
        int i6 = i3;
        if (bitArray.getSizeInBytes() == i5) {
            ArrayList<BlockPair> arrayList = new ArrayList<>(i6);
            int i7 = 0;
            int i8 = 0;
            int i9 = 0;
            for (int i10 = 0; i10 < i6; i10++) {
                int[] iArr = new int[1];
                int[] iArr2 = new int[1];
                getNumDataBytesAndNumECBytesForBlockID(i, i2, i3, i10, iArr, iArr2);
                int i11 = iArr[0];
                byte[] bArr = new byte[i11];
                bitArray.toBytes(i7 * 8, bArr, 0, i11);
                byte[] generateECBytes = generateECBytes(bArr, iArr2[0]);
                arrayList.add(new BlockPair(bArr, generateECBytes));
                i9 = Math.max(i9, i11);
                i8 = Math.max(i8, generateECBytes.length);
                i7 += iArr[0];
            }
            if (i5 == i7) {
                BitArray bitArray2 = new BitArray();
                for (int i12 = 0; i12 < i9; i12++) {
                    for (BlockPair dataBytes : arrayList) {
                        byte[] dataBytes2 = dataBytes.getDataBytes();
                        if (i12 < dataBytes2.length) {
                            bitArray2.appendBits(dataBytes2[i12], 8);
                        }
                    }
                }
                for (int i13 = 0; i13 < i8; i13++) {
                    for (BlockPair errorCorrectionBytes : arrayList) {
                        byte[] errorCorrectionBytes2 = errorCorrectionBytes.getErrorCorrectionBytes();
                        if (i13 < errorCorrectionBytes2.length) {
                            bitArray2.appendBits(errorCorrectionBytes2[i13], 8);
                        }
                    }
                }
                if (i4 == bitArray2.getSizeInBytes()) {
                    return bitArray2;
                }
                throw new WriterException("Interleaving error: " + i4 + " and " + bitArray2.getSizeInBytes() + " differ.");
            }
            throw new WriterException("Data bytes does not match offset");
        }
        throw new WriterException("Number of bits and data bytes does not match");
    }

    private static boolean isOnlyDoubleByteKanji(String str) {
        try {
            byte[] bytes = str.getBytes("Shift_JIS");
            int length = bytes.length;
            if (length % 2 != 0) {
                return false;
            }
            for (int i = 0; i < length; i += 2) {
                byte b = bytes[i] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE;
                if ((b < 129 || b > 159) && (b < 224 || b > 235)) {
                    return false;
                }
            }
            return true;
        } catch (UnsupportedEncodingException unused) {
            return false;
        }
    }

    static void terminateBits(int i, BitArray bitArray) throws WriterException {
        int i2 = i * 8;
        if (bitArray.getSize() <= i2) {
            for (int i3 = 0; i3 < 4 && bitArray.getSize() < i2; i3++) {
                bitArray.appendBit(false);
            }
            int size = bitArray.getSize() & 7;
            if (size > 0) {
                while (size < 8) {
                    bitArray.appendBit(false);
                    size++;
                }
            }
            int sizeInBytes = i - bitArray.getSizeInBytes();
            for (int i4 = 0; i4 < sizeInBytes; i4++) {
                bitArray.appendBits((i4 & 1) == 0 ? 236 : 17, 8);
            }
            if (bitArray.getSize() != i2) {
                throw new WriterException("Bits size does not equal capacity");
            }
            return;
        }
        throw new WriterException("data bits cannot fit in the QR Code" + bitArray.getSize() + " > " + i2);
    }

    private static Mode chooseMode(String str, String str2) {
        if ("Shift_JIS".equals(str2) && isOnlyDoubleByteKanji(str)) {
            return Mode.KANJI;
        }
        boolean z = false;
        boolean z2 = false;
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt >= '0' && charAt <= '9') {
                z2 = true;
            } else if (getAlphanumericCode(charAt) == -1) {
                return Mode.BYTE;
            } else {
                z = true;
            }
        }
        if (z) {
            return Mode.ALPHANUMERIC;
        }
        if (z2) {
            return Mode.NUMERIC;
        }
        return Mode.BYTE;
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0070  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0075  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.dcloud.zxing2.qrcode.encoder.QRCode encode(java.lang.String r5, com.dcloud.zxing2.qrcode.decoder.ErrorCorrectionLevel r6, java.util.Map<com.dcloud.zxing2.EncodeHintType, ?> r7) throws com.dcloud.zxing2.WriterException {
        /*
            java.lang.String r0 = "ISO-8859-1"
            if (r7 == 0) goto L_0x0015
            com.dcloud.zxing2.EncodeHintType r1 = com.dcloud.zxing2.EncodeHintType.CHARACTER_SET
            boolean r2 = r7.containsKey(r1)
            if (r2 == 0) goto L_0x0015
            java.lang.Object r7 = r7.get(r1)
            java.lang.String r7 = r7.toString()
            goto L_0x0016
        L_0x0015:
            r7 = r0
        L_0x0016:
            com.dcloud.zxing2.qrcode.decoder.Mode r1 = chooseMode(r5, r7)
            com.dcloud.zxing2.common.BitArray r2 = new com.dcloud.zxing2.common.BitArray
            r2.<init>()
            com.dcloud.zxing2.qrcode.decoder.Mode r3 = com.dcloud.zxing2.qrcode.decoder.Mode.BYTE
            if (r1 != r3) goto L_0x0032
            boolean r0 = r0.equals(r7)
            if (r0 != 0) goto L_0x0032
            com.dcloud.zxing2.common.CharacterSetECI r0 = com.dcloud.zxing2.common.CharacterSetECI.getCharacterSetECIByName(r7)
            if (r0 == 0) goto L_0x0032
            appendECI(r0, r2)
        L_0x0032:
            appendModeInfo(r1, r2)
            com.dcloud.zxing2.common.BitArray r0 = new com.dcloud.zxing2.common.BitArray
            r0.<init>()
            appendBytes(r5, r1, r0, r7)
            int r7 = r2.getSize()
            r4 = 1
            com.dcloud.zxing2.qrcode.decoder.Version r4 = com.dcloud.zxing2.qrcode.decoder.Version.getVersionForNumber(r4)
            int r4 = r1.getCharacterCountBits(r4)
            int r7 = r7 + r4
            int r4 = r0.getSize()
            int r7 = r7 + r4
            com.dcloud.zxing2.qrcode.decoder.Version r7 = chooseVersion(r7, r6)
            int r4 = r2.getSize()
            int r7 = r1.getCharacterCountBits(r7)
            int r4 = r4 + r7
            int r7 = r0.getSize()
            int r4 = r4 + r7
            com.dcloud.zxing2.qrcode.decoder.Version r7 = chooseVersion(r4, r6)
            com.dcloud.zxing2.common.BitArray r4 = new com.dcloud.zxing2.common.BitArray
            r4.<init>()
            r4.appendBitArray(r2)
            if (r1 != r3) goto L_0x0075
            int r5 = r0.getSizeInBytes()
            goto L_0x0079
        L_0x0075:
            int r5 = r5.length()
        L_0x0079:
            appendLengthInfo(r5, r7, r1, r4)
            r4.appendBitArray(r0)
            com.dcloud.zxing2.qrcode.decoder.Version$ECBlocks r5 = r7.getECBlocksForLevel(r6)
            int r0 = r7.getTotalCodewords()
            int r2 = r5.getTotalECCodewords()
            int r0 = r0 - r2
            terminateBits(r0, r4)
            int r2 = r7.getTotalCodewords()
            int r5 = r5.getNumBlocks()
            com.dcloud.zxing2.common.BitArray r5 = interleaveWithECBytes(r4, r2, r0, r5)
            com.dcloud.zxing2.qrcode.encoder.QRCode r0 = new com.dcloud.zxing2.qrcode.encoder.QRCode
            r0.<init>()
            r0.setECLevel(r6)
            r0.setMode(r1)
            r0.setVersion(r7)
            int r1 = r7.getDimensionForVersion()
            com.dcloud.zxing2.qrcode.encoder.ByteMatrix r2 = new com.dcloud.zxing2.qrcode.encoder.ByteMatrix
            r2.<init>(r1, r1)
            int r1 = chooseMaskPattern(r5, r6, r7, r2)
            r0.setMaskPattern(r1)
            com.dcloud.zxing2.qrcode.encoder.MatrixUtil.buildMatrix(r5, r6, r7, r1, r2)
            r0.setMatrix(r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dcloud.zxing2.qrcode.encoder.Encoder.encode(java.lang.String, com.dcloud.zxing2.qrcode.decoder.ErrorCorrectionLevel, java.util.Map):com.dcloud.zxing2.qrcode.encoder.QRCode");
    }
}
