package com.dcloud.zxing2.common;

import com.dcloud.zxing2.ResultPoint;

public class DetectorResult {
    private final BitMatrix bits;
    public float moduleSize = -1.0f;
    private final ResultPoint[] points;

    public DetectorResult(BitMatrix bitMatrix, ResultPoint[] resultPointArr) {
        this.bits = bitMatrix;
        this.points = resultPointArr;
    }

    public final BitMatrix getBits() {
        return this.bits;
    }

    public final ResultPoint[] getPoints() {
        return this.points;
    }
}
