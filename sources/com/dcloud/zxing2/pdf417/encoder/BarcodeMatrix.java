package com.dcloud.zxing2.pdf417.encoder;

import java.lang.reflect.Array;

public final class BarcodeMatrix {
    private int currentRow;
    private final int height;
    private final BarcodeRow[] matrix;
    private final int width;

    BarcodeMatrix(int i, int i2) {
        this.matrix = new BarcodeRow[i];
        for (int i3 = 0; i3 < i; i3++) {
            this.matrix[i3] = new BarcodeRow(((i2 + 4) * 17) + 1);
        }
        this.width = i2 * 17;
        this.height = i;
        this.currentRow = -1;
    }

    /* access modifiers changed from: package-private */
    public BarcodeRow getCurrentRow() {
        return this.matrix[this.currentRow];
    }

    public byte[][] getMatrix() {
        return getScaledMatrix(1, 1);
    }

    public byte[][] getScaledMatrix(int i, int i2) {
        int i3 = this.height * i2;
        int[] iArr = new int[2];
        iArr[1] = this.width * i;
        iArr[0] = i3;
        byte[][] bArr = (byte[][]) Array.newInstance(byte.class, iArr);
        for (int i4 = 0; i4 < i3; i4++) {
            bArr[(i3 - i4) - 1] = this.matrix[i4 / i2].getScaledRow(i);
        }
        return bArr;
    }

    /* access modifiers changed from: package-private */
    public void set(int i, int i2, byte b) {
        this.matrix[i2].set(i, b);
    }

    /* access modifiers changed from: package-private */
    public void startRow() {
        this.currentRow++;
    }
}
