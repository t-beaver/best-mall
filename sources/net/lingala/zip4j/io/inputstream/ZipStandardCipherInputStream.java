package net.lingala.zip4j.io.inputstream;

import java.io.IOException;
import net.lingala.zip4j.crypto.StandardDecrypter;
import net.lingala.zip4j.model.LocalFileHeader;

class ZipStandardCipherInputStream extends CipherInputStream<StandardDecrypter> {
    public ZipStandardCipherInputStream(ZipEntryInputStream zipEntryInputStream, LocalFileHeader localFileHeader, char[] cArr, int i) throws IOException {
        super(zipEntryInputStream, localFileHeader, cArr, i);
    }

    private byte[] getStandardDecrypterHeaderBytes() throws IOException {
        byte[] bArr = new byte[12];
        readRaw(bArr);
        return bArr;
    }

    /* access modifiers changed from: protected */
    public StandardDecrypter initializeDecrypter(LocalFileHeader localFileHeader, char[] cArr) throws IOException {
        return new StandardDecrypter(cArr, localFileHeader.getCrc(), localFileHeader.getLastModifiedTime(), getStandardDecrypterHeaderBytes());
    }
}
