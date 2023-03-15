package com.dcloud.zxing2.oned;

import com.dcloud.zxing2.BarcodeFormat;
import com.dcloud.zxing2.EncodeHintType;
import com.dcloud.zxing2.WriterException;
import com.dcloud.zxing2.common.BitMatrix;
import java.util.ArrayList;
import java.util.Map;
import org.mozilla.universalchardet.prober.HebrewProber;
import org.mozilla.universalchardet.prober.contextanalysis.SJISContextAnalysis;

public final class Code128Writer extends OneDimensionalCodeWriter {
    private static final int CODE_CODE_B = 100;
    private static final int CODE_CODE_C = 99;
    private static final int CODE_FNC_1 = 102;
    private static final int CODE_FNC_2 = 97;
    private static final int CODE_FNC_3 = 96;
    private static final int CODE_FNC_4_B = 100;
    private static final int CODE_START_B = 104;
    private static final int CODE_START_C = 105;
    private static final int CODE_STOP = 106;
    private static final char ESCAPE_FNC_1 = 'ñ';
    private static final char ESCAPE_FNC_2 = 'ò';
    private static final char ESCAPE_FNC_3 = 'ó';
    private static final char ESCAPE_FNC_4 = 'ô';

    private static boolean isDigits(CharSequence charSequence, int i, int i2) {
        int i3 = i2 + i;
        int length = charSequence.length();
        while (i < i3 && i < length) {
            char charAt = charSequence.charAt(i);
            if (charAt < '0' || charAt > '9') {
                if (charAt != 241) {
                    return false;
                }
                i3++;
            }
            i++;
        }
        if (i3 <= length) {
            return true;
        }
        return false;
    }

    public BitMatrix encode(String str, BarcodeFormat barcodeFormat, int i, int i2, Map<EncodeHintType, ?> map) throws WriterException {
        if (barcodeFormat == BarcodeFormat.CODE_128) {
            return super.encode(str, barcodeFormat, i, i2, map);
        }
        throw new IllegalArgumentException("Can only encode CODE_128, but got " + barcodeFormat);
    }

    public boolean[] encode(String str) {
        int length = str.length();
        if (length < 1 || length > 80) {
            throw new IllegalArgumentException("Contents length should be between 1 and 80 characters, but got " + length);
        }
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            char charAt = str.charAt(i2);
            if (charAt < ' ' || charAt > '~') {
                switch (charAt) {
                    case SJISContextAnalysis.HIRAGANA_LOWBYTE_END /*241*/:
                    case 242:
                    case 243:
                    case HebrewProber.NORMAL_PE /*244*/:
                        break;
                    default:
                        throw new IllegalArgumentException("Bad character in input: " + charAt);
                }
            }
        }
        ArrayList<int[]> arrayList = new ArrayList<>();
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 1;
        while (i3 < length) {
            int i7 = 99;
            int i8 = 100;
            if (!isDigits(str, i3, i5 == 99 ? 2 : 4)) {
                i7 = 100;
            }
            if (i7 == i5) {
                switch (str.charAt(i3)) {
                    case SJISContextAnalysis.HIRAGANA_LOWBYTE_END /*241*/:
                        i8 = 102;
                        break;
                    case 242:
                        i8 = 97;
                        break;
                    case 243:
                        i8 = 96;
                        break;
                    case HebrewProber.NORMAL_PE /*244*/:
                        break;
                    default:
                        if (i5 != 100) {
                            i8 = Integer.parseInt(str.substring(i3, i3 + 2));
                            i3++;
                            break;
                        } else {
                            i8 = str.charAt(i3) - ' ';
                            break;
                        }
                }
                i3++;
                i7 = i8;
            } else if (i5 != 0) {
                i5 = i7;
            } else if (i7 == 100) {
                i5 = i7;
                i7 = 104;
            } else {
                i5 = i7;
                i7 = 105;
            }
            arrayList.add(Code128Reader.CODE_PATTERNS[i7]);
            i4 += i7 * i6;
            if (i3 != 0) {
                i6++;
            }
        }
        int[][] iArr = Code128Reader.CODE_PATTERNS;
        arrayList.add(iArr[i4 % 103]);
        arrayList.add(iArr[106]);
        int i9 = 0;
        for (int[] iArr2 : arrayList) {
            for (int i10 : (int[]) r13.next()) {
                i9 += i10;
            }
        }
        boolean[] zArr = new boolean[i9];
        for (int[] appendPattern : arrayList) {
            i += OneDimensionalCodeWriter.appendPattern(zArr, i, appendPattern, true);
        }
        return zArr;
    }
}
