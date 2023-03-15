package net.lingala.zip4j.io.inputstream;

import io.dcloud.common.DHInterface.IApp;
import java.io.IOException;
import java.io.InputStream;
import net.lingala.zip4j.crypto.Decrypter;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.util.Zip4jUtil;

abstract class CipherInputStream<T extends Decrypter> extends InputStream {
    private T decrypter;
    private byte[] lastReadRawDataCache;
    private LocalFileHeader localFileHeader;
    private byte[] singleByteBuffer = new byte[1];
    private ZipEntryInputStream zipEntryInputStream;

    public CipherInputStream(ZipEntryInputStream zipEntryInputStream2, LocalFileHeader localFileHeader2, char[] cArr, int i) throws IOException {
        this.zipEntryInputStream = zipEntryInputStream2;
        this.decrypter = initializeDecrypter(localFileHeader2, cArr);
        this.localFileHeader = localFileHeader2;
        if (Zip4jUtil.getCompressionMethod(localFileHeader2).equals(CompressionMethod.DEFLATE)) {
            this.lastReadRawDataCache = new byte[i];
        }
    }

    private void cacheRawData(byte[] bArr, int i) {
        byte[] bArr2 = this.lastReadRawDataCache;
        if (bArr2 != null) {
            System.arraycopy(bArr, 0, bArr2, 0, i);
        }
    }

    public void close() throws IOException {
        this.zipEntryInputStream.close();
    }

    /* access modifiers changed from: protected */
    public void endOfEntryReached(InputStream inputStream) throws IOException {
    }

    public T getDecrypter() {
        return this.decrypter;
    }

    public byte[] getLastReadRawDataCache() {
        return this.lastReadRawDataCache;
    }

    public LocalFileHeader getLocalFileHeader() {
        return this.localFileHeader;
    }

    /* access modifiers changed from: protected */
    public long getNumberOfBytesReadForThisEntry() {
        return this.zipEntryInputStream.getNumberOfBytesRead();
    }

    /* access modifiers changed from: protected */
    public abstract T initializeDecrypter(LocalFileHeader localFileHeader2, char[] cArr) throws IOException, ZipException;

    public int read() throws IOException {
        if (read(this.singleByteBuffer) == -1) {
            return -1;
        }
        return this.singleByteBuffer[0] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE;
    }

    /* access modifiers changed from: protected */
    public int readRaw(byte[] bArr) throws IOException {
        return this.zipEntryInputStream.readRawFully(bArr);
    }

    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        int readFully = Zip4jUtil.readFully(this.zipEntryInputStream, bArr, i, i2);
        if (readFully > 0) {
            cacheRawData(bArr, readFully);
            this.decrypter.decryptData(bArr, i, readFully);
        }
        return readFully;
    }
}
