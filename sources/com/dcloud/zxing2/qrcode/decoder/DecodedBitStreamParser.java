package com.dcloud.zxing2.qrcode.decoder;

import com.dcloud.zxing2.DecodeHintType;
import com.dcloud.zxing2.FormatException;
import com.dcloud.zxing2.common.BitSource;
import com.dcloud.zxing2.common.CharacterSetECI;
import com.dcloud.zxing2.common.StringUtils;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.utils.WXUtils;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;

final class DecodedBitStreamParser {
    private static final char[] ALPHANUMERIC_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' ', Operators.DOLLAR, WXUtils.PERCENT, '*', '+', '-', Operators.DOT, '/', Operators.CONDITION_IF_MIDDLE};
    private static final int GB2312_SUBSET = 1;

    private DecodedBitStreamParser() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:60:0x00f6 A[LOOP:0: B:1:0x001f->B:60:0x00f6, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00d4 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static com.dcloud.zxing2.common.DecoderResult decode(byte[] r17, com.dcloud.zxing2.qrcode.decoder.Version r18, com.dcloud.zxing2.qrcode.decoder.ErrorCorrectionLevel r19, java.util.Map<com.dcloud.zxing2.DecodeHintType, ?> r20) throws com.dcloud.zxing2.FormatException {
        /*
            r0 = r18
            com.dcloud.zxing2.common.BitSource r7 = new com.dcloud.zxing2.common.BitSource
            r8 = r17
            r7.<init>(r8)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r1 = 50
            r9.<init>(r1)
            java.util.ArrayList r10 = new java.util.ArrayList
            r11 = 1
            r10.<init>(r11)
            r1 = -1
            java.lang.String r2 = ""
            r3 = 0
            r13 = -1
            r14 = -1
            r15 = 0
            r16 = 0
        L_0x001f:
            int r1 = r7.available()     // Catch:{ IllegalArgumentException -> 0x00fb }
            r3 = 4
            if (r1 >= r3) goto L_0x002a
            com.dcloud.zxing2.qrcode.decoder.Mode r1 = com.dcloud.zxing2.qrcode.decoder.Mode.TERMINATOR     // Catch:{ IllegalArgumentException -> 0x00fb }
        L_0x0028:
            r6 = r1
            goto L_0x0033
        L_0x002a:
            int r1 = r7.readBits(r3)     // Catch:{ IllegalArgumentException -> 0x00fb }
            com.dcloud.zxing2.qrcode.decoder.Mode r1 = com.dcloud.zxing2.qrcode.decoder.Mode.forBits(r1)     // Catch:{ IllegalArgumentException -> 0x00fb }
            goto L_0x0028
        L_0x0033:
            com.dcloud.zxing2.qrcode.decoder.Mode r5 = com.dcloud.zxing2.qrcode.decoder.Mode.TERMINATOR     // Catch:{ IllegalArgumentException -> 0x00fb }
            if (r6 == r5) goto L_0x0071
            com.dcloud.zxing2.qrcode.decoder.Mode r1 = com.dcloud.zxing2.qrcode.decoder.Mode.FNC1_FIRST_POSITION     // Catch:{ IllegalArgumentException -> 0x00fb }
            if (r6 == r1) goto L_0x00cb
            com.dcloud.zxing2.qrcode.decoder.Mode r1 = com.dcloud.zxing2.qrcode.decoder.Mode.FNC1_SECOND_POSITION     // Catch:{ IllegalArgumentException -> 0x00fb }
            if (r6 != r1) goto L_0x0041
            goto L_0x00cb
        L_0x0041:
            com.dcloud.zxing2.qrcode.decoder.Mode r1 = com.dcloud.zxing2.qrcode.decoder.Mode.STRUCTURED_APPEND     // Catch:{ IllegalArgumentException -> 0x00fb }
            if (r6 != r1) goto L_0x0063
            int r1 = r7.available()     // Catch:{ IllegalArgumentException -> 0x00fb }
            r3 = 16
            if (r1 < r3) goto L_0x005e
            r1 = 8
            int r3 = r7.readBits(r1)     // Catch:{ IllegalArgumentException -> 0x00fb }
            int r1 = r7.readBits(r1)     // Catch:{ IllegalArgumentException -> 0x00fb }
            r14 = r1
            r13 = r2
            r11 = r5
            r12 = r6
            r5 = r3
            goto L_0x00d2
        L_0x005e:
            com.dcloud.zxing2.FormatException r0 = com.dcloud.zxing2.FormatException.getFormatInstance()     // Catch:{ IllegalArgumentException -> 0x00fb }
            throw r0     // Catch:{ IllegalArgumentException -> 0x00fb }
        L_0x0063:
            com.dcloud.zxing2.qrcode.decoder.Mode r1 = com.dcloud.zxing2.qrcode.decoder.Mode.ECI     // Catch:{ IllegalArgumentException -> 0x00fb }
            if (r6 != r1) goto L_0x007a
            int r1 = parseECIValue(r7)     // Catch:{ IllegalArgumentException -> 0x00fb }
            com.dcloud.zxing2.common.CharacterSetECI r16 = com.dcloud.zxing2.common.CharacterSetECI.getCharacterSetECIByValue(r1)     // Catch:{ IllegalArgumentException -> 0x00fb }
            if (r16 == 0) goto L_0x0075
        L_0x0071:
            r11 = r5
            r12 = r6
            goto L_0x00d0
        L_0x0075:
            com.dcloud.zxing2.FormatException r0 = com.dcloud.zxing2.FormatException.getFormatInstance()     // Catch:{ IllegalArgumentException -> 0x00fb }
            throw r0     // Catch:{ IllegalArgumentException -> 0x00fb }
        L_0x007a:
            com.dcloud.zxing2.qrcode.decoder.Mode r1 = com.dcloud.zxing2.qrcode.decoder.Mode.HANZI     // Catch:{ IllegalArgumentException -> 0x00fb }
            if (r6 != r1) goto L_0x0090
            int r1 = r7.readBits(r3)     // Catch:{ IllegalArgumentException -> 0x00fb }
            int r3 = r6.getCharacterCountBits(r0)     // Catch:{ IllegalArgumentException -> 0x00fb }
            int r3 = r7.readBits(r3)     // Catch:{ IllegalArgumentException -> 0x00fb }
            if (r1 != r11) goto L_0x0071
            decodeHanziSegment(r7, r9, r3)     // Catch:{ IllegalArgumentException -> 0x00fb }
            goto L_0x0071
        L_0x0090:
            int r1 = r6.getCharacterCountBits(r0)     // Catch:{ IllegalArgumentException -> 0x00fb }
            int r3 = r7.readBits(r1)     // Catch:{ IllegalArgumentException -> 0x00fb }
            com.dcloud.zxing2.qrcode.decoder.Mode r1 = com.dcloud.zxing2.qrcode.decoder.Mode.NUMERIC     // Catch:{ IllegalArgumentException -> 0x00fb }
            if (r6 != r1) goto L_0x00a0
            decodeNumericSegment(r7, r9, r3)     // Catch:{ IllegalArgumentException -> 0x00fb }
            goto L_0x0071
        L_0x00a0:
            com.dcloud.zxing2.qrcode.decoder.Mode r1 = com.dcloud.zxing2.qrcode.decoder.Mode.ALPHANUMERIC     // Catch:{ IllegalArgumentException -> 0x00fb }
            if (r6 != r1) goto L_0x00a8
            decodeAlphanumericSegment(r7, r9, r3, r15)     // Catch:{ IllegalArgumentException -> 0x00fb }
            goto L_0x0071
        L_0x00a8:
            com.dcloud.zxing2.qrcode.decoder.Mode r1 = com.dcloud.zxing2.qrcode.decoder.Mode.BYTE     // Catch:{ IllegalArgumentException -> 0x00fb }
            if (r6 != r1) goto L_0x00bc
            r1 = r7
            r2 = r9
            r4 = r16
            r11 = r5
            r5 = r10
            r12 = r6
            r6 = r20
            java.lang.String r1 = decodeByteSegment(r1, r2, r3, r4, r5, r6)     // Catch:{ IllegalArgumentException -> 0x00fb }
            r5 = r13
            r13 = r1
            goto L_0x00d2
        L_0x00bc:
            r11 = r5
            r12 = r6
            com.dcloud.zxing2.qrcode.decoder.Mode r1 = com.dcloud.zxing2.qrcode.decoder.Mode.KANJI     // Catch:{ IllegalArgumentException -> 0x00fb }
            if (r12 != r1) goto L_0x00c6
            decodeKanjiSegment(r7, r9, r3)     // Catch:{ IllegalArgumentException -> 0x00fb }
            goto L_0x00d0
        L_0x00c6:
            com.dcloud.zxing2.FormatException r0 = com.dcloud.zxing2.FormatException.getFormatInstance()     // Catch:{ IllegalArgumentException -> 0x00fb }
            throw r0     // Catch:{ IllegalArgumentException -> 0x00fb }
        L_0x00cb:
            r11 = r5
            r12 = r6
            r5 = r13
            r15 = 1
            goto L_0x00d1
        L_0x00d0:
            r5 = r13
        L_0x00d1:
            r13 = r2
        L_0x00d2:
            if (r12 != r11) goto L_0x00f6
            com.dcloud.zxing2.common.DecoderResult r7 = new com.dcloud.zxing2.common.DecoderResult
            java.lang.String r2 = r9.toString()
            boolean r0 = r10.isEmpty()
            if (r0 == 0) goto L_0x00e2
            r3 = 0
            goto L_0x00e3
        L_0x00e2:
            r3 = r10
        L_0x00e3:
            if (r19 != 0) goto L_0x00e7
            r4 = 0
            goto L_0x00ec
        L_0x00e7:
            java.lang.String r0 = r19.toString()
            r4 = r0
        L_0x00ec:
            r0 = r7
            r1 = r17
            r6 = r14
            r0.<init>(r1, r2, r3, r4, r5, r6)
            r7.textCharset = r13
            return r7
        L_0x00f6:
            r2 = r13
            r11 = 1
            r13 = r5
            goto L_0x001f
        L_0x00fb:
            com.dcloud.zxing2.FormatException r0 = com.dcloud.zxing2.FormatException.getFormatInstance()
            goto L_0x0101
        L_0x0100:
            throw r0
        L_0x0101:
            goto L_0x0100
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dcloud.zxing2.qrcode.decoder.DecodedBitStreamParser.decode(byte[], com.dcloud.zxing2.qrcode.decoder.Version, com.dcloud.zxing2.qrcode.decoder.ErrorCorrectionLevel, java.util.Map):com.dcloud.zxing2.common.DecoderResult");
    }

    private static void decodeAlphanumericSegment(BitSource bitSource, StringBuilder sb, int i, boolean z) throws FormatException {
        while (i > 1) {
            if (bitSource.available() >= 11) {
                int readBits = bitSource.readBits(11);
                sb.append(toAlphaNumericChar(readBits / 45));
                sb.append(toAlphaNumericChar(readBits % 45));
                i -= 2;
            } else {
                throw FormatException.getFormatInstance();
            }
        }
        if (i == 1) {
            if (bitSource.available() >= 6) {
                sb.append(toAlphaNumericChar(bitSource.readBits(6)));
            } else {
                throw FormatException.getFormatInstance();
            }
        }
        if (z) {
            for (int length = sb.length(); length < sb.length(); length++) {
                if (sb.charAt(length) == '%') {
                    if (length < sb.length() - 1) {
                        int i2 = length + 1;
                        if (sb.charAt(i2) == '%') {
                            sb.deleteCharAt(i2);
                        }
                    }
                    sb.setCharAt(length, 29);
                }
            }
        }
    }

    private static String decodeByteSegment(BitSource bitSource, StringBuilder sb, int i, CharacterSetECI characterSetECI, Collection<byte[]> collection, Map<DecodeHintType, ?> map) throws FormatException {
        String str;
        if (i * 8 <= bitSource.available()) {
            byte[] bArr = new byte[i];
            for (int i2 = 0; i2 < i; i2++) {
                bArr[i2] = (byte) bitSource.readBits(8);
            }
            if (characterSetECI == null) {
                str = StringUtils.guessEncoding(bArr, map);
            } else {
                str = characterSetECI.name();
            }
            try {
                sb.append(new String(bArr, str));
                collection.add(bArr);
                return str;
            } catch (UnsupportedEncodingException unused) {
                throw FormatException.getFormatInstance();
            }
        } else {
            throw FormatException.getFormatInstance();
        }
    }

    private static void decodeHanziSegment(BitSource bitSource, StringBuilder sb, int i) throws FormatException {
        if (i * 13 <= bitSource.available()) {
            byte[] bArr = new byte[(i * 2)];
            int i2 = 0;
            while (i > 0) {
                int readBits = bitSource.readBits(13);
                int i3 = (readBits % 96) | ((readBits / 96) << 8);
                int i4 = i3 + (i3 < 959 ? 41377 : 42657);
                bArr[i2] = (byte) ((i4 >> 8) & 255);
                bArr[i2 + 1] = (byte) (i4 & 255);
                i2 += 2;
                i--;
            }
            try {
                sb.append(new String(bArr, StringUtils.GB2312));
            } catch (UnsupportedEncodingException unused) {
                throw FormatException.getFormatInstance();
            }
        } else {
            throw FormatException.getFormatInstance();
        }
    }

    private static void decodeKanjiSegment(BitSource bitSource, StringBuilder sb, int i) throws FormatException {
        if (i * 13 <= bitSource.available()) {
            byte[] bArr = new byte[(i * 2)];
            int i2 = 0;
            while (i > 0) {
                int readBits = bitSource.readBits(13);
                int i3 = (readBits % 192) | ((readBits / 192) << 8);
                int i4 = i3 + (i3 < 7936 ? 33088 : 49472);
                bArr[i2] = (byte) (i4 >> 8);
                bArr[i2 + 1] = (byte) i4;
                i2 += 2;
                i--;
            }
            try {
                sb.append(new String(bArr, "UTF-8"));
            } catch (UnsupportedEncodingException unused) {
                throw FormatException.getFormatInstance();
            }
        } else {
            throw FormatException.getFormatInstance();
        }
    }

    private static void decodeNumericSegment(BitSource bitSource, StringBuilder sb, int i) throws FormatException {
        while (i >= 3) {
            if (bitSource.available() >= 10) {
                int readBits = bitSource.readBits(10);
                if (readBits < 1000) {
                    sb.append(toAlphaNumericChar(readBits / 100));
                    sb.append(toAlphaNumericChar((readBits / 10) % 10));
                    sb.append(toAlphaNumericChar(readBits % 10));
                    i -= 3;
                } else {
                    throw FormatException.getFormatInstance();
                }
            } else {
                throw FormatException.getFormatInstance();
            }
        }
        if (i == 2) {
            if (bitSource.available() >= 7) {
                int readBits2 = bitSource.readBits(7);
                if (readBits2 < 100) {
                    sb.append(toAlphaNumericChar(readBits2 / 10));
                    sb.append(toAlphaNumericChar(readBits2 % 10));
                    return;
                }
                throw FormatException.getFormatInstance();
            }
            throw FormatException.getFormatInstance();
        } else if (i != 1) {
        } else {
            if (bitSource.available() >= 4) {
                int readBits3 = bitSource.readBits(4);
                if (readBits3 < 10) {
                    sb.append(toAlphaNumericChar(readBits3));
                    return;
                }
                throw FormatException.getFormatInstance();
            }
            throw FormatException.getFormatInstance();
        }
    }

    private static int parseECIValue(BitSource bitSource) throws FormatException {
        int readBits = bitSource.readBits(8);
        if ((readBits & 128) == 0) {
            return readBits & 127;
        }
        if ((readBits & 192) == 128) {
            return bitSource.readBits(8) | ((readBits & 63) << 8);
        }
        if ((readBits & 224) == 192) {
            return bitSource.readBits(16) | ((readBits & 31) << 16);
        }
        throw FormatException.getFormatInstance();
    }

    private static char toAlphaNumericChar(int i) throws FormatException {
        char[] cArr = ALPHANUMERIC_CHARS;
        if (i < cArr.length) {
            return cArr[i];
        }
        throw FormatException.getFormatInstance();
    }
}
