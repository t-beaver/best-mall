package com.dcloud.zxing2.oned.rss.expanded.decoders;

import com.dcloud.zxing2.NotFoundException;
import com.dcloud.zxing2.common.BitArray;
import com.taobao.weex.el.parse.Operators;

final class AI013x0x1xDecoder extends AI01weightDecoder {
    private static final int DATE_SIZE = 16;
    private static final int HEADER_SIZE = 8;
    private static final int WEIGHT_SIZE = 20;
    private final String dateCode;
    private final String firstAIdigits;

    AI013x0x1xDecoder(BitArray bitArray, String str, String str2) {
        super(bitArray);
        this.dateCode = str2;
        this.firstAIdigits = str;
    }

    private void encodeCompressedDate(StringBuilder sb, int i) {
        int extractNumericValueFromBitArray = getGeneralDecoder().extractNumericValueFromBitArray(i, 16);
        if (extractNumericValueFromBitArray != 38400) {
            sb.append(Operators.BRACKET_START);
            sb.append(this.dateCode);
            sb.append(Operators.BRACKET_END);
            int i2 = extractNumericValueFromBitArray % 32;
            int i3 = extractNumericValueFromBitArray / 32;
            int i4 = (i3 % 12) + 1;
            int i5 = i3 / 12;
            if (i5 / 10 == 0) {
                sb.append('0');
            }
            sb.append(i5);
            if (i4 / 10 == 0) {
                sb.append('0');
            }
            sb.append(i4);
            if (i2 / 10 == 0) {
                sb.append('0');
            }
            sb.append(i2);
        }
    }

    /* access modifiers changed from: protected */
    public void addWeightCode(StringBuilder sb, int i) {
        sb.append(Operators.BRACKET_START);
        sb.append(this.firstAIdigits);
        sb.append(i / 100000);
        sb.append(Operators.BRACKET_END);
    }

    /* access modifiers changed from: protected */
    public int checkWeight(int i) {
        return i % 100000;
    }

    public String parseInformation() throws NotFoundException {
        if (getInformation().getSize() == 84) {
            StringBuilder sb = new StringBuilder();
            encodeCompressedGtin(sb, 8);
            encodeCompressedWeight(sb, 48, 20);
            encodeCompressedDate(sb, 68);
            return sb.toString();
        }
        throw NotFoundException.getNotFoundInstance();
    }
}
