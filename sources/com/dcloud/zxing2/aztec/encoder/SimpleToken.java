package com.dcloud.zxing2.aztec.encoder;

import com.dcloud.zxing2.common.BitArray;

final class SimpleToken extends Token {
    private final short bitCount;
    private final short value;

    SimpleToken(Token token, int i, int i2) {
        super(token);
        this.value = (short) i;
        this.bitCount = (short) i2;
    }

    /* access modifiers changed from: package-private */
    public void appendTo(BitArray bitArray, byte[] bArr) {
        bitArray.appendBits(this.value, this.bitCount);
    }

    public String toString() {
        short s = this.value;
        int i = 1 << this.bitCount;
        short s2 = (s & (i - 1)) | i;
        return '<' + Integer.toBinaryString(s2 | (1 << this.bitCount)).substring(1) + '>';
    }
}
