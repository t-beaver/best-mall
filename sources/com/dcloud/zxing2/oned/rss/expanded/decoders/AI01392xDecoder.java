package com.dcloud.zxing2.oned.rss.expanded.decoders;

import com.dcloud.zxing2.FormatException;
import com.dcloud.zxing2.NotFoundException;
import com.dcloud.zxing2.common.BitArray;
import com.taobao.weex.el.parse.Operators;

final class AI01392xDecoder extends AI01decoder {
    private static final int HEADER_SIZE = 8;
    private static final int LAST_DIGIT_SIZE = 2;

    AI01392xDecoder(BitArray bitArray) {
        super(bitArray);
    }

    public String parseInformation() throws NotFoundException, FormatException {
        if (getInformation().getSize() >= 48) {
            StringBuilder sb = new StringBuilder();
            encodeCompressedGtin(sb, 8);
            int extractNumericValueFromBitArray = getGeneralDecoder().extractNumericValueFromBitArray(48, 2);
            sb.append("(392");
            sb.append(extractNumericValueFromBitArray);
            sb.append(Operators.BRACKET_END);
            sb.append(getGeneralDecoder().decodeGeneralPurposeField(50, (String) null).getNewString());
            return sb.toString();
        }
        throw NotFoundException.getNotFoundInstance();
    }
}
