package com.dcloud.zxing2.oned.rss.expanded.decoders;

import com.dcloud.zxing2.NotFoundException;
import com.dcloud.zxing2.common.BitArray;

abstract class AI013x0xDecoder extends AI01weightDecoder {
    private static final int HEADER_SIZE = 5;
    private static final int WEIGHT_SIZE = 15;

    AI013x0xDecoder(BitArray bitArray) {
        super(bitArray);
    }

    public String parseInformation() throws NotFoundException {
        if (getInformation().getSize() == 60) {
            StringBuilder sb = new StringBuilder();
            encodeCompressedGtin(sb, 5);
            encodeCompressedWeight(sb, 45, 15);
            return sb.toString();
        }
        throw NotFoundException.getNotFoundInstance();
    }
}
