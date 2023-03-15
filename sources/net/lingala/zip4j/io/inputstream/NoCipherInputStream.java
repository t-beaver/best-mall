package net.lingala.zip4j.io.inputstream;

import java.io.IOException;
import net.lingala.zip4j.crypto.Decrypter;
import net.lingala.zip4j.model.LocalFileHeader;

class NoCipherInputStream extends CipherInputStream {

    static class NoDecrypter implements Decrypter {
        NoDecrypter() {
        }

        public int decryptData(byte[] bArr, int i, int i2) {
            return i2;
        }
    }

    public NoCipherInputStream(ZipEntryInputStream zipEntryInputStream, LocalFileHeader localFileHeader, char[] cArr, int i) throws IOException {
        super(zipEntryInputStream, localFileHeader, cArr, i);
    }

    /* access modifiers changed from: protected */
    public Decrypter initializeDecrypter(LocalFileHeader localFileHeader, char[] cArr) {
        return new NoDecrypter();
    }
}
