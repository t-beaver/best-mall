package com.dcloud.zxing2.pdf417.detector;

import com.dcloud.zxing2.ResultPoint;
import com.dcloud.zxing2.common.BitMatrix;
import java.util.List;

public final class PDF417DetectorResult {
    private final BitMatrix bits;
    private final List<ResultPoint[]> points;

    public PDF417DetectorResult(BitMatrix bitMatrix, List<ResultPoint[]> list) {
        this.bits = bitMatrix;
        this.points = list;
    }

    public BitMatrix getBits() {
        return this.bits;
    }

    public List<ResultPoint[]> getPoints() {
        return this.points;
    }
}
