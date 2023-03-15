package net.lingala.zip4j.io.outputstream;

import java.io.IOException;
import java.io.OutputStream;

abstract class CompressedOutputStream extends OutputStream {
    private CipherOutputStream cipherOutputStream;

    public CompressedOutputStream(CipherOutputStream cipherOutputStream2) {
        this.cipherOutputStream = cipherOutputStream2;
    }

    public void close() throws IOException {
        this.cipherOutputStream.close();
    }

    /* access modifiers changed from: protected */
    public void closeEntry() throws IOException {
        this.cipherOutputStream.closeEntry();
    }

    public long getCompressedSize() {
        return this.cipherOutputStream.getNumberOfBytesWrittenForThisEntry();
    }

    public void write(int i) throws IOException {
        write(new byte[]{(byte) i});
    }

    public void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        this.cipherOutputStream.write(bArr, i, i2);
    }
}
