package io.dcloud.common.adapter.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class UnicodeInputStream extends InputStream {
    private static final int BOM_SIZE = 4;
    String defaultEnc;
    String encoding;
    PushbackInputStream internalIn;
    boolean isInited = false;

    public UnicodeInputStream(InputStream inputStream, String str) {
        this.internalIn = new PushbackInputStream(inputStream, 4);
        this.defaultEnc = str;
    }

    public void close() throws IOException {
        this.isInited = true;
        this.internalIn.close();
    }

    public String getDefaultEncoding() {
        return this.defaultEnc;
    }

    public String getEncoding() {
        if (!this.isInited) {
            try {
                init();
            } catch (IOException unused) {
                IllegalStateException illegalStateException = new IllegalStateException("Init method failed.");
                illegalStateException.initCause(illegalStateException);
                throw illegalStateException;
            }
        }
        return this.encoding;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x007c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void init() throws java.io.IOException {
        /*
            r9 = this;
            boolean r0 = r9.isInited
            if (r0 == 0) goto L_0x0005
            return
        L_0x0005:
            r0 = 4
            byte[] r1 = new byte[r0]
            java.io.PushbackInputStream r2 = r9.internalIn
            r3 = 0
            int r0 = r2.read(r1, r3, r0)
            byte r2 = r1[r3]
            r4 = 3
            r5 = -2
            r6 = -1
            r7 = 2
            r8 = 1
            if (r2 != 0) goto L_0x002b
            byte r2 = r1[r8]
            if (r2 != 0) goto L_0x002b
            byte r2 = r1[r7]
            if (r2 != r5) goto L_0x002b
            byte r2 = r1[r4]
            if (r2 != r6) goto L_0x002b
            java.lang.String r2 = "UTF-32BE"
            r9.encoding = r2
        L_0x0028:
            int r2 = r0 + -4
            goto L_0x007a
        L_0x002b:
            byte r2 = r1[r3]
            if (r2 != r6) goto L_0x0040
            byte r2 = r1[r8]
            if (r2 != r5) goto L_0x0040
            byte r2 = r1[r7]
            if (r2 != 0) goto L_0x0040
            byte r2 = r1[r4]
            if (r2 != 0) goto L_0x0040
            java.lang.String r2 = "UTF-32LE"
            r9.encoding = r2
            goto L_0x0028
        L_0x0040:
            byte r2 = r1[r3]
            r4 = -17
            if (r2 != r4) goto L_0x0059
            byte r2 = r1[r8]
            r4 = -69
            if (r2 != r4) goto L_0x0059
            byte r2 = r1[r7]
            r4 = -65
            if (r2 != r4) goto L_0x0059
            java.lang.String r2 = "UTF-8"
            r9.encoding = r2
            int r2 = r0 + -3
            goto L_0x007a
        L_0x0059:
            byte r2 = r1[r3]
            if (r2 != r5) goto L_0x0068
            byte r2 = r1[r8]
            if (r2 != r6) goto L_0x0068
            java.lang.String r2 = "UTF-16BE"
            r9.encoding = r2
        L_0x0065:
            int r2 = r0 + -2
            goto L_0x007a
        L_0x0068:
            byte r2 = r1[r3]
            if (r2 != r6) goto L_0x0075
            byte r2 = r1[r8]
            if (r2 != r5) goto L_0x0075
            java.lang.String r2 = "UTF-16LE"
            r9.encoding = r2
            goto L_0x0065
        L_0x0075:
            java.lang.String r2 = r9.defaultEnc
            r9.encoding = r2
            r2 = r0
        L_0x007a:
            if (r2 <= 0) goto L_0x0082
            java.io.PushbackInputStream r3 = r9.internalIn
            int r0 = r0 - r2
            r3.unread(r1, r0, r2)
        L_0x0082:
            r9.isInited = r8
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.io.UnicodeInputStream.init():void");
    }

    public int read() throws IOException {
        init();
        this.isInited = true;
        return this.internalIn.read();
    }
}
