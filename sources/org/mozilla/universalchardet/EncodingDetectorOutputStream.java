package org.mozilla.universalchardet;

import java.io.IOException;
import java.io.OutputStream;

public class EncodingDetectorOutputStream extends OutputStream {
    private final UniversalDetector detector = new UniversalDetector((CharsetListener) null);
    private OutputStream out;

    public EncodingDetectorOutputStream(OutputStream outputStream) {
        this.out = outputStream;
    }

    public void close() throws IOException {
        this.out.close();
        this.detector.dataEnd();
    }

    public void flush() throws IOException {
        this.out.flush();
    }

    public String getDetectedCharset() {
        return this.detector.getDetectedCharset();
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        this.out.write(bArr, i, i2);
        if (!this.detector.isDone()) {
            this.detector.handleData(bArr, i, i2);
        }
    }

    public void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    public void write(int i) throws IOException {
        write(new byte[]{(byte) i});
    }
}
