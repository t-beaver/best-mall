package org.mozilla.universalchardet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class UnicodeBOMInputStream extends InputStream {
    private final BOM bom;
    private final PushbackInputStream in;
    private boolean skipped;

    public static final class BOM {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        public static final BOM NONE = new BOM(new byte[0], "NONE");
        public static final BOM UTF_16_BE = new BOM(new byte[]{-2, -1}, "UTF-16 big-endian");
        public static final BOM UTF_16_LE = new BOM(new byte[]{-1, -2}, "UTF-16 little-endian");
        public static final BOM UTF_32_BE = new BOM(new byte[]{0, 0, -2, -1}, "UTF-32 big-endian");
        public static final BOM UTF_32_LE = new BOM(new byte[]{-1, -2, 0, 0}, "UTF-32 little-endian");
        public static final BOM UTF_8 = new BOM(new byte[]{-17, -69, -65}, "UTF-8");
        final byte[] bytes;
        private final String description;

        static {
            Class<UnicodeBOMInputStream> cls = UnicodeBOMInputStream.class;
        }

        private BOM(byte[] bArr, String str) {
            this.bytes = bArr;
            this.description = str;
        }

        public final byte[] getBytes() {
            byte[] bArr = this.bytes;
            int length = bArr.length;
            byte[] bArr2 = new byte[length];
            System.arraycopy(bArr, 0, bArr2, 0, length);
            return bArr2;
        }

        public final String toString() {
            return this.description;
        }
    }

    public UnicodeBOMInputStream(InputStream inputStream) throws IOException {
        this(inputStream, true);
    }

    public int available() throws IOException {
        return this.in.available();
    }

    public void close() throws IOException {
        this.in.close();
    }

    public final BOM getBOM() {
        return this.bom;
    }

    public synchronized void mark(int i) {
        this.in.mark(i);
    }

    public boolean markSupported() {
        return this.in.markSupported();
    }

    public int read() throws IOException {
        this.skipped = true;
        return this.in.read();
    }

    public synchronized void reset() throws IOException {
        this.in.reset();
    }

    public long skip(long j) throws IOException {
        this.skipped = true;
        return this.in.skip(j);
    }

    public final synchronized UnicodeBOMInputStream skipBOM() throws IOException {
        if (!this.skipped) {
            long length = (long) this.bom.bytes.length;
            for (long skip = this.in.skip(length); skip < length; skip++) {
                this.in.read();
            }
            this.skipped = true;
        }
        return this;
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x0086  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x008b  */
    /* JADX WARNING: Removed duplicated region for block: B:46:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public UnicodeBOMInputStream(java.io.InputStream r10, boolean r11) throws java.io.IOException {
        /*
            r9 = this;
            r9.<init>()
            r0 = 0
            r9.skipped = r0
            java.lang.String r1 = "invalid input stream: null is not allowed"
            java.util.Objects.requireNonNull(r10, r1)
            java.io.PushbackInputStream r1 = new java.io.PushbackInputStream
            r2 = 4
            r1.<init>(r10, r2)
            r9.in = r1
            byte[] r10 = new byte[r2]
            int r3 = r1.read(r10)
            r4 = -2
            r5 = -1
            r6 = 2
            r7 = 1
            if (r3 == r6) goto L_0x0066
            r8 = 3
            if (r3 == r8) goto L_0x004f
            if (r3 == r2) goto L_0x0025
            goto L_0x0080
        L_0x0025:
            byte r2 = r10[r0]
            if (r2 != r5) goto L_0x003a
            byte r2 = r10[r7]
            if (r2 != r4) goto L_0x003a
            byte r2 = r10[r6]
            if (r2 != 0) goto L_0x003a
            byte r2 = r10[r8]
            if (r2 != 0) goto L_0x003a
            org.mozilla.universalchardet.UnicodeBOMInputStream$BOM r2 = org.mozilla.universalchardet.UnicodeBOMInputStream.BOM.UTF_32_LE
            r9.bom = r2
            goto L_0x0084
        L_0x003a:
            byte r2 = r10[r0]
            if (r2 != 0) goto L_0x004f
            byte r2 = r10[r7]
            if (r2 != 0) goto L_0x004f
            byte r2 = r10[r6]
            if (r2 != r4) goto L_0x004f
            byte r2 = r10[r8]
            if (r2 != r5) goto L_0x004f
            org.mozilla.universalchardet.UnicodeBOMInputStream$BOM r2 = org.mozilla.universalchardet.UnicodeBOMInputStream.BOM.UTF_32_BE
            r9.bom = r2
            goto L_0x0084
        L_0x004f:
            byte r2 = r10[r0]
            r8 = -17
            if (r2 != r8) goto L_0x0066
            byte r2 = r10[r7]
            r8 = -69
            if (r2 != r8) goto L_0x0066
            byte r2 = r10[r6]
            r6 = -65
            if (r2 != r6) goto L_0x0066
            org.mozilla.universalchardet.UnicodeBOMInputStream$BOM r2 = org.mozilla.universalchardet.UnicodeBOMInputStream.BOM.UTF_8
            r9.bom = r2
            goto L_0x0084
        L_0x0066:
            byte r2 = r10[r0]
            if (r2 != r5) goto L_0x0073
            byte r2 = r10[r7]
            if (r2 != r4) goto L_0x0073
            org.mozilla.universalchardet.UnicodeBOMInputStream$BOM r2 = org.mozilla.universalchardet.UnicodeBOMInputStream.BOM.UTF_16_LE
            r9.bom = r2
            goto L_0x0084
        L_0x0073:
            byte r2 = r10[r0]
            if (r2 != r4) goto L_0x0080
            byte r2 = r10[r7]
            if (r2 != r5) goto L_0x0080
            org.mozilla.universalchardet.UnicodeBOMInputStream$BOM r2 = org.mozilla.universalchardet.UnicodeBOMInputStream.BOM.UTF_16_BE
            r9.bom = r2
            goto L_0x0084
        L_0x0080:
            org.mozilla.universalchardet.UnicodeBOMInputStream$BOM r2 = org.mozilla.universalchardet.UnicodeBOMInputStream.BOM.NONE
            r9.bom = r2
        L_0x0084:
            if (r3 <= 0) goto L_0x0089
            r1.unread(r10, r0, r3)
        L_0x0089:
            if (r11 == 0) goto L_0x008e
            r9.skipBOM()
        L_0x008e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.universalchardet.UnicodeBOMInputStream.<init>(java.io.InputStream, boolean):void");
    }

    public int read(byte[] bArr) throws IOException {
        this.skipped = true;
        return this.in.read(bArr, 0, bArr.length);
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        this.skipped = true;
        return this.in.read(bArr, i, i2);
    }
}
