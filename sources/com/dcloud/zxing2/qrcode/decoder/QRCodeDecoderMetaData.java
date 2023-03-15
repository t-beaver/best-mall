package com.dcloud.zxing2.qrcode.decoder;

import com.dcloud.zxing2.ResultPoint;

public final class QRCodeDecoderMetaData {
    private final boolean mirrored;

    QRCodeDecoderMetaData(boolean z) {
        this.mirrored = z;
    }

    public void applyMirroredCorrection(ResultPoint[] resultPointArr) {
        if (this.mirrored && resultPointArr != null && resultPointArr.length >= 3) {
            ResultPoint resultPoint = resultPointArr[0];
            resultPointArr[0] = resultPointArr[2];
            resultPointArr[2] = resultPoint;
        }
    }

    public boolean isMirrored() {
        return this.mirrored;
    }
}
