package com.dcloud.zxing2.oned.rss.expanded.decoders;

import com.dcloud.zxing2.FormatException;
import com.dcloud.zxing2.NotFoundException;
import com.dcloud.zxing2.common.BitArray;
import com.taobao.weex.el.parse.Operators;

final class AI01393xDecoder extends AI01decoder {
    private static final int FIRST_THREE_DIGITS_SIZE = 10;
    private static final int HEADER_SIZE = 8;
    private static final int LAST_DIGIT_SIZE = 2;

    AI01393xDecoder(BitArray bitArray) {
        super(bitArray);
    }

    public String parseInformation() throws NotFoundException, FormatException {
        if (getInformation().getSize() >= 48) {
            StringBuilder sb = new StringBuilder();
            encodeCompressedGtin(sb, 8);
            int extractNumericValueFromBitArray = getGeneralDecoder().extractNumericValueFromBitArray(48, 2);
            sb.append("(393");
            sb.append(extractNumericValueFromBitArray);
            sb.append(Operators.BRACKET_END);
            int extractNumericValueFromBitArray2 = getGeneralDecoder().extractNumericValueFromBitArray(50, 10);
            if (extractNumericValueFromBitArray2 / 100 == 0) {
                sb.append('0');
            }
            if (extractNumericValueFromBitArray2 / 10 == 0) {
                sb.append('0');
            }
            sb.append(extractNumericValueFromBitArray2);
            sb.append(getGeneralDecoder().decodeGeneralPurposeField(60, (String) null).getNewString());
            return sb.toString();
        }
        throw NotFoundException.getNotFoundInstance();
    }
}
