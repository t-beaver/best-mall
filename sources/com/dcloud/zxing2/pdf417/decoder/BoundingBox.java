package com.dcloud.zxing2.pdf417.decoder;

import com.dcloud.zxing2.NotFoundException;
import com.dcloud.zxing2.ResultPoint;
import com.dcloud.zxing2.common.BitMatrix;

final class BoundingBox {
    private ResultPoint bottomLeft;
    private ResultPoint bottomRight;
    private BitMatrix image;
    private int maxX;
    private int maxY;
    private int minX;
    private int minY;
    private ResultPoint topLeft;
    private ResultPoint topRight;

    BoundingBox(BitMatrix bitMatrix, ResultPoint resultPoint, ResultPoint resultPoint2, ResultPoint resultPoint3, ResultPoint resultPoint4) throws NotFoundException {
        if (!(resultPoint == null && resultPoint3 == null) && (!(resultPoint2 == null && resultPoint4 == null) && ((resultPoint == null || resultPoint2 != null) && (resultPoint3 == null || resultPoint4 != null)))) {
            init(bitMatrix, resultPoint, resultPoint2, resultPoint3, resultPoint4);
            return;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private void calculateMinMaxValues() {
        if (this.topLeft == null) {
            this.topLeft = new ResultPoint(0.0f, this.topRight.getY());
            this.bottomLeft = new ResultPoint(0.0f, this.bottomRight.getY());
        } else if (this.topRight == null) {
            this.topRight = new ResultPoint((float) (this.image.getWidth() - 1), this.topLeft.getY());
            this.bottomRight = new ResultPoint((float) (this.image.getWidth() - 1), this.bottomLeft.getY());
        }
        this.minX = (int) Math.min(this.topLeft.getX(), this.bottomLeft.getX());
        this.maxX = (int) Math.max(this.topRight.getX(), this.bottomRight.getX());
        this.minY = (int) Math.min(this.topLeft.getY(), this.topRight.getY());
        this.maxY = (int) Math.max(this.bottomLeft.getY(), this.bottomRight.getY());
    }

    private void init(BitMatrix bitMatrix, ResultPoint resultPoint, ResultPoint resultPoint2, ResultPoint resultPoint3, ResultPoint resultPoint4) {
        this.image = bitMatrix;
        this.topLeft = resultPoint;
        this.bottomLeft = resultPoint2;
        this.topRight = resultPoint3;
        this.bottomRight = resultPoint4;
        calculateMinMaxValues();
    }

    static BoundingBox merge(BoundingBox boundingBox, BoundingBox boundingBox2) throws NotFoundException {
        if (boundingBox == null) {
            return boundingBox2;
        }
        return boundingBox2 == null ? boundingBox : new BoundingBox(boundingBox.image, boundingBox.topLeft, boundingBox.bottomLeft, boundingBox2.topRight, boundingBox2.bottomRight);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x002d  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x005b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.dcloud.zxing2.pdf417.decoder.BoundingBox addMissingRows(int r13, int r14, boolean r15) throws com.dcloud.zxing2.NotFoundException {
        /*
            r12 = this;
            com.dcloud.zxing2.ResultPoint r0 = r12.topLeft
            com.dcloud.zxing2.ResultPoint r1 = r12.bottomLeft
            com.dcloud.zxing2.ResultPoint r2 = r12.topRight
            com.dcloud.zxing2.ResultPoint r3 = r12.bottomRight
            if (r13 <= 0) goto L_0x0029
            if (r15 == 0) goto L_0x000e
            r4 = r0
            goto L_0x000f
        L_0x000e:
            r4 = r2
        L_0x000f:
            float r5 = r4.getY()
            int r5 = (int) r5
            int r5 = r5 - r13
            if (r5 >= 0) goto L_0x0018
            r5 = 0
        L_0x0018:
            com.dcloud.zxing2.ResultPoint r13 = new com.dcloud.zxing2.ResultPoint
            float r4 = r4.getX()
            float r5 = (float) r5
            r13.<init>(r4, r5)
            if (r15 == 0) goto L_0x0026
            r8 = r13
            goto L_0x002a
        L_0x0026:
            r10 = r13
            r8 = r0
            goto L_0x002b
        L_0x0029:
            r8 = r0
        L_0x002a:
            r10 = r2
        L_0x002b:
            if (r14 <= 0) goto L_0x005b
            if (r15 == 0) goto L_0x0032
            com.dcloud.zxing2.ResultPoint r13 = r12.bottomLeft
            goto L_0x0034
        L_0x0032:
            com.dcloud.zxing2.ResultPoint r13 = r12.bottomRight
        L_0x0034:
            float r0 = r13.getY()
            int r0 = (int) r0
            int r0 = r0 + r14
            com.dcloud.zxing2.common.BitMatrix r14 = r12.image
            int r14 = r14.getHeight()
            if (r0 < r14) goto L_0x004a
            com.dcloud.zxing2.common.BitMatrix r14 = r12.image
            int r14 = r14.getHeight()
            int r0 = r14 + -1
        L_0x004a:
            com.dcloud.zxing2.ResultPoint r14 = new com.dcloud.zxing2.ResultPoint
            float r13 = r13.getX()
            float r0 = (float) r0
            r14.<init>(r13, r0)
            if (r15 == 0) goto L_0x0058
            r9 = r14
            goto L_0x005c
        L_0x0058:
            r11 = r14
            r9 = r1
            goto L_0x005d
        L_0x005b:
            r9 = r1
        L_0x005c:
            r11 = r3
        L_0x005d:
            r12.calculateMinMaxValues()
            com.dcloud.zxing2.pdf417.decoder.BoundingBox r13 = new com.dcloud.zxing2.pdf417.decoder.BoundingBox
            com.dcloud.zxing2.common.BitMatrix r7 = r12.image
            r6 = r13
            r6.<init>(r7, r8, r9, r10, r11)
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dcloud.zxing2.pdf417.decoder.BoundingBox.addMissingRows(int, int, boolean):com.dcloud.zxing2.pdf417.decoder.BoundingBox");
    }

    /* access modifiers changed from: package-private */
    public ResultPoint getBottomLeft() {
        return this.bottomLeft;
    }

    /* access modifiers changed from: package-private */
    public ResultPoint getBottomRight() {
        return this.bottomRight;
    }

    /* access modifiers changed from: package-private */
    public int getMaxX() {
        return this.maxX;
    }

    /* access modifiers changed from: package-private */
    public int getMaxY() {
        return this.maxY;
    }

    /* access modifiers changed from: package-private */
    public int getMinX() {
        return this.minX;
    }

    /* access modifiers changed from: package-private */
    public int getMinY() {
        return this.minY;
    }

    /* access modifiers changed from: package-private */
    public ResultPoint getTopLeft() {
        return this.topLeft;
    }

    /* access modifiers changed from: package-private */
    public ResultPoint getTopRight() {
        return this.topRight;
    }

    BoundingBox(BoundingBox boundingBox) {
        init(boundingBox.image, boundingBox.topLeft, boundingBox.bottomLeft, boundingBox.topRight, boundingBox.bottomRight);
    }
}
