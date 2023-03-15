package com.dcloud.zxing2.datamatrix.encoder;

import java.util.Arrays;

public class DefaultPlacement {
    private final byte[] bits;
    private final CharSequence codewords;
    private final int numcols;
    private final int numrows;

    public DefaultPlacement(CharSequence charSequence, int i, int i2) {
        this.codewords = charSequence;
        this.numcols = i;
        this.numrows = i2;
        byte[] bArr = new byte[(i * i2)];
        this.bits = bArr;
        Arrays.fill(bArr, (byte) -1);
    }

    private void corner1(int i) {
        module(this.numrows - 1, 0, i, 1);
        module(this.numrows - 1, 1, i, 2);
        module(this.numrows - 1, 2, i, 3);
        module(0, this.numcols - 2, i, 4);
        module(0, this.numcols - 1, i, 5);
        module(1, this.numcols - 1, i, 6);
        module(2, this.numcols - 1, i, 7);
        module(3, this.numcols - 1, i, 8);
    }

    private void corner2(int i) {
        module(this.numrows - 3, 0, i, 1);
        module(this.numrows - 2, 0, i, 2);
        module(this.numrows - 1, 0, i, 3);
        module(0, this.numcols - 4, i, 4);
        module(0, this.numcols - 3, i, 5);
        module(0, this.numcols - 2, i, 6);
        module(0, this.numcols - 1, i, 7);
        module(1, this.numcols - 1, i, 8);
    }

    private void corner3(int i) {
        module(this.numrows - 3, 0, i, 1);
        module(this.numrows - 2, 0, i, 2);
        module(this.numrows - 1, 0, i, 3);
        module(0, this.numcols - 2, i, 4);
        module(0, this.numcols - 1, i, 5);
        module(1, this.numcols - 1, i, 6);
        module(2, this.numcols - 1, i, 7);
        module(3, this.numcols - 1, i, 8);
    }

    private void corner4(int i) {
        module(this.numrows - 1, 0, i, 1);
        module(this.numrows - 1, this.numcols - 1, i, 2);
        module(0, this.numcols - 3, i, 3);
        module(0, this.numcols - 2, i, 4);
        module(0, this.numcols - 1, i, 5);
        module(1, this.numcols - 3, i, 6);
        module(1, this.numcols - 2, i, 7);
        module(1, this.numcols - 1, i, 8);
    }

    private void module(int i, int i2, int i3, int i4) {
        if (i < 0) {
            int i5 = this.numrows;
            i += i5;
            i2 += 4 - ((i5 + 4) % 8);
        }
        if (i2 < 0) {
            int i6 = this.numcols;
            i2 += i6;
            i += 4 - ((i6 + 4) % 8);
        }
        boolean z = true;
        if ((this.codewords.charAt(i3) & (1 << (8 - i4))) == 0) {
            z = false;
        }
        setBit(i2, i, z);
    }

    private void utah(int i, int i2, int i3) {
        int i4 = i - 2;
        int i5 = i2 - 2;
        module(i4, i5, i3, 1);
        int i6 = i2 - 1;
        module(i4, i6, i3, 2);
        int i7 = i - 1;
        module(i7, i5, i3, 3);
        module(i7, i6, i3, 4);
        module(i7, i2, i3, 5);
        module(i, i5, i3, 6);
        module(i, i6, i3, 7);
        module(i, i2, i3, 8);
    }

    public final boolean getBit(int i, int i2) {
        return this.bits[(i2 * this.numcols) + i] == 1;
    }

    /* access modifiers changed from: package-private */
    public final byte[] getBits() {
        return this.bits;
    }

    /* access modifiers changed from: package-private */
    public final int getNumcols() {
        return this.numcols;
    }

    /* access modifiers changed from: package-private */
    public final int getNumrows() {
        return this.numrows;
    }

    /* access modifiers changed from: package-private */
    public final boolean hasBit(int i, int i2) {
        return this.bits[(i2 * this.numcols) + i] >= 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:52:0x0005 A[LOOP:0: B:1:0x0005->B:52:0x0005, LOOP_END, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x0092 A[EDGE_INSN: B:53:0x0092->B:49:0x0092 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x0088 A[EDGE_INSN: B:56:0x0088->B:45:0x0088 ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void place() {
        /*
            r8 = this;
            r0 = 0
            r1 = 4
            r2 = 0
            r4 = r8
            r3 = 4
        L_0x0005:
            int r5 = r4.numrows
            if (r3 != r5) goto L_0x0011
            if (r0 != 0) goto L_0x0011
            int r5 = r2 + 1
            r4.corner1(r2)
            r2 = r5
        L_0x0011:
            int r5 = r4.numrows
            r6 = 2
            int r5 = r5 - r6
            if (r3 != r5) goto L_0x0024
            if (r0 != 0) goto L_0x0024
            int r5 = r4.numcols
            int r5 = r5 % r1
            if (r5 == 0) goto L_0x0024
            int r5 = r2 + 1
            r4.corner2(r2)
            r2 = r5
        L_0x0024:
            int r5 = r4.numrows
            int r5 = r5 - r6
            if (r3 != r5) goto L_0x0037
            if (r0 != 0) goto L_0x0037
            int r5 = r4.numcols
            int r5 = r5 % 8
            if (r5 != r1) goto L_0x0037
            int r5 = r2 + 1
            r4.corner3(r2)
            r2 = r5
        L_0x0037:
            int r5 = r4.numrows
            int r5 = r5 + r1
            if (r3 != r5) goto L_0x004a
            if (r0 != r6) goto L_0x004a
            int r5 = r4.numcols
            int r5 = r5 % 8
            if (r5 != 0) goto L_0x004a
            int r5 = r2 + 1
            r4.corner4(r2)
            goto L_0x004b
        L_0x004a:
            r5 = r2
        L_0x004b:
            int r2 = r4.numrows
            if (r3 >= r2) goto L_0x005d
            if (r0 < 0) goto L_0x005d
            boolean r2 = r4.hasBit(r0, r3)
            if (r2 != 0) goto L_0x005d
            int r2 = r5 + 1
            r4.utah(r3, r0, r5)
            goto L_0x005e
        L_0x005d:
            r2 = r5
        L_0x005e:
            int r3 = r3 + -2
            int r0 = r0 + 2
            if (r3 < 0) goto L_0x0068
            int r5 = r4.numcols
            if (r0 < r5) goto L_0x004a
        L_0x0068:
            int r3 = r3 + 1
            int r0 = r0 + 3
        L_0x006c:
            if (r3 < 0) goto L_0x007e
            int r5 = r4.numcols
            if (r0 >= r5) goto L_0x007e
            boolean r5 = r4.hasBit(r0, r3)
            if (r5 != 0) goto L_0x007e
            int r5 = r2 + 1
            r4.utah(r3, r0, r2)
            r2 = r5
        L_0x007e:
            int r3 = r3 + 2
            int r0 = r0 + -2
            int r5 = r4.numrows
            if (r3 >= r5) goto L_0x0088
            if (r0 >= 0) goto L_0x006c
        L_0x0088:
            int r3 = r3 + 3
            int r0 = r0 + 1
            if (r3 < r5) goto L_0x0005
            int r7 = r4.numcols
            if (r0 < r7) goto L_0x0005
            r0 = 1
            int r7 = r7 - r0
            int r5 = r5 - r0
            boolean r1 = r4.hasBit(r7, r5)
            if (r1 != 0) goto L_0x00ad
            int r1 = r4.numcols
            int r1 = r1 - r0
            int r2 = r4.numrows
            int r2 = r2 - r0
            r4.setBit(r1, r2, r0)
            int r1 = r4.numcols
            int r1 = r1 - r6
            int r2 = r4.numrows
            int r2 = r2 - r6
            r4.setBit(r1, r2, r0)
        L_0x00ad:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dcloud.zxing2.datamatrix.encoder.DefaultPlacement.place():void");
    }

    /* access modifiers changed from: package-private */
    public final void setBit(int i, int i2, boolean z) {
        this.bits[(i2 * this.numcols) + i] = z ? (byte) 1 : 0;
    }
}
