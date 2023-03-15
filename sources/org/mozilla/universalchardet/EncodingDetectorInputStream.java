package org.mozilla.universalchardet;

import java.io.IOException;
import java.io.InputStream;

public class EncodingDetectorInputStream extends InputStream {
    private final UniversalDetector detector = new UniversalDetector((CharsetListener) null);
    private InputStream in;

    public EncodingDetectorInputStream(InputStream inputStream) {
        this.in = inputStream;
    }

    public int available() throws IOException {
        return this.in.available();
    }

    public void close() throws IOException {
        this.in.close();
    }

    public String getDetectedCharset() {
        return this.detector.getDetectedCharset();
    }

    public void mark(int i) {
        this.in.mark(i);
    }

    public boolean markSupported() {
        return this.in.markSupported();
    }

    public void reset() throws IOException {
        this.in.reset();
    }

    public long skip(long j) throws IOException {
        if (this.detector.isDone()) {
            return this.in.skip(j);
        }
        int i = 0;
        long j2 = -1;
        for (long j3 = 0; j3 < j && i >= 0; j3++) {
            i = this.in.read();
            j2++;
        }
        return j2;
    }

    public int read() throws IOException {
        byte[] bArr = new byte[1];
        if (read(bArr, 0, 1) >= 0) {
            return bArr[0];
        }
        return -1;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        int read = this.in.read(bArr, i, i2);
        if (!this.detector.isDone() && read > 0) {
            this.detector.handleData(bArr, i, read);
        }
        if (read == -1) {
            this.detector.dataEnd();
        }
        return read;
    }

    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }
}
