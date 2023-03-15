package net.lingala.zip4j.io.inputstream;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

abstract class DecompressedInputStream extends InputStream {
    private CipherInputStream cipherInputStream;
    protected byte[] oneByteBuffer = new byte[1];

    public DecompressedInputStream(CipherInputStream cipherInputStream2) {
        this.cipherInputStream = cipherInputStream2;
    }

    public void close() throws IOException {
        this.cipherInputStream.close();
    }

    public void endOfEntryReached(InputStream inputStream) throws IOException {
        this.cipherInputStream.endOfEntryReached(inputStream);
    }

    /* access modifiers changed from: protected */
    public byte[] getLastReadRawDataCache() {
        return this.cipherInputStream.getLastReadRawDataCache();
    }

    public void pushBackInputStreamIfNecessary(PushbackInputStream pushbackInputStream) throws IOException {
    }

    public int read() throws IOException {
        if (read(this.oneByteBuffer) == -1) {
            return -1;
        }
        return this.oneByteBuffer[0];
    }

    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        return this.cipherInputStream.read(bArr, i, i2);
    }
}
