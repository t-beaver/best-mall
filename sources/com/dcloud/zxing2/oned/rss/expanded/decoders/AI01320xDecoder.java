package com.dcloud.zxing2.oned.rss.expanded.decoders;

import com.dcloud.zxing2.common.BitArray;
import io.dcloud.feature.gg.dcloud.ADSim;

final class AI01320xDecoder extends AI013x0xDecoder {
    AI01320xDecoder(BitArray bitArray) {
        super(bitArray);
    }

    /* access modifiers changed from: protected */
    public void addWeightCode(StringBuilder sb, int i) {
        if (i < 10000) {
            sb.append("(3202)");
        } else {
            sb.append("(3203)");
        }
    }

    /* access modifiers changed from: protected */
    public int checkWeight(int i) {
        return i < 10000 ? i : i - ADSim.INTISPLSH;
    }
}
