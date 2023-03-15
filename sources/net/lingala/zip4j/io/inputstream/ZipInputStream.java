package net.lingala.zip4j.io.inputstream;

import io.dcloud.common.DHInterface.IApp;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.zip.CRC32;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.headers.HeaderReader;
import net.lingala.zip4j.headers.HeaderSignature;
import net.lingala.zip4j.model.DataDescriptor;
import net.lingala.zip4j.model.ExtraDataRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.Zip4jConfig;
import net.lingala.zip4j.model.enums.AesVersion;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import net.lingala.zip4j.util.Zip4jUtil;

public class ZipInputStream extends InputStream {
    private boolean canSkipExtendedLocalFileHeader;
    private CRC32 crc32;
    private DecompressedInputStream decompressedInputStream;
    private byte[] endOfEntryBuffer;
    private boolean entryEOFReached;
    private HeaderReader headerReader;
    private PushbackInputStream inputStream;
    private LocalFileHeader localFileHeader;
    private char[] password;
    private boolean streamClosed;
    private Zip4jConfig zip4jConfig;

    public ZipInputStream(InputStream inputStream2) {
        this(inputStream2, (char[]) null, (Charset) null);
    }

    private void assertStreamOpen() throws IOException {
        if (this.streamClosed) {
            throw new IOException("Stream closed");
        }
    }

    private boolean checkIfZip64ExtraDataRecordPresentInLFH(List<ExtraDataRecord> list) {
        if (list == null) {
            return false;
        }
        for (ExtraDataRecord header : list) {
            if (header.getHeader() == HeaderSignature.ZIP64_EXTRA_FIELD_SIGNATURE.getValue()) {
                return true;
            }
        }
        return false;
    }

    private void endOfCompressedDataReached() throws IOException {
        this.decompressedInputStream.pushBackInputStreamIfNecessary(this.inputStream);
        this.decompressedInputStream.endOfEntryReached(this.inputStream);
        readExtendedLocalFileHeaderIfPresent();
        verifyCrc();
        resetFields();
        this.entryEOFReached = true;
    }

    private long getCompressedSize(LocalFileHeader localFileHeader2) {
        if (Zip4jUtil.getCompressionMethod(localFileHeader2).equals(CompressionMethod.STORE)) {
            return localFileHeader2.getUncompressedSize();
        }
        if (!localFileHeader2.isDataDescriptorExists() || this.canSkipExtendedLocalFileHeader) {
            return localFileHeader2.getCompressedSize() - ((long) getEncryptionHeaderSize(localFileHeader2));
        }
        return -1;
    }

    private int getEncryptionHeaderSize(LocalFileHeader localFileHeader2) {
        if (!localFileHeader2.isEncrypted()) {
            return 0;
        }
        if (localFileHeader2.getEncryptionMethod().equals(EncryptionMethod.AES)) {
            return localFileHeader2.getAesExtraDataRecord().getAesKeyStrength().getSaltLength() + 12;
        }
        if (localFileHeader2.getEncryptionMethod().equals(EncryptionMethod.ZIP_STANDARD)) {
            return 12;
        }
        return 0;
    }

    private CipherInputStream initializeCipherInputStream(ZipEntryInputStream zipEntryInputStream, LocalFileHeader localFileHeader2) throws IOException {
        if (!localFileHeader2.isEncrypted()) {
            return new NoCipherInputStream(zipEntryInputStream, localFileHeader2, this.password, this.zip4jConfig.getBufferSize());
        }
        if (localFileHeader2.getEncryptionMethod() == EncryptionMethod.AES) {
            return new AesCipherInputStream(zipEntryInputStream, localFileHeader2, this.password, this.zip4jConfig.getBufferSize());
        }
        if (localFileHeader2.getEncryptionMethod() == EncryptionMethod.ZIP_STANDARD) {
            return new ZipStandardCipherInputStream(zipEntryInputStream, localFileHeader2, this.password, this.zip4jConfig.getBufferSize());
        }
        throw new ZipException(String.format("Entry [%s] Strong Encryption not supported", new Object[]{localFileHeader2.getFileName()}), ZipException.Type.UNSUPPORTED_ENCRYPTION);
    }

    private DecompressedInputStream initializeDecompressorForThisEntry(CipherInputStream cipherInputStream, LocalFileHeader localFileHeader2) {
        if (Zip4jUtil.getCompressionMethod(localFileHeader2) == CompressionMethod.DEFLATE) {
            return new InflaterInputStream(cipherInputStream, this.zip4jConfig.getBufferSize());
        }
        return new StoreInputStream(cipherInputStream);
    }

    private DecompressedInputStream initializeEntryInputStream(LocalFileHeader localFileHeader2) throws IOException {
        return initializeDecompressorForThisEntry(initializeCipherInputStream(new ZipEntryInputStream(this.inputStream, getCompressedSize(localFileHeader2)), localFileHeader2), localFileHeader2);
    }

    private boolean isEncryptionMethodZipStandard(LocalFileHeader localFileHeader2) {
        return localFileHeader2.isEncrypted() && EncryptionMethod.ZIP_STANDARD.equals(localFileHeader2.getEncryptionMethod());
    }

    private boolean isEntryDirectory(String str) {
        return str.endsWith("/") || str.endsWith("\\");
    }

    private void readExtendedLocalFileHeaderIfPresent() throws IOException {
        if (this.localFileHeader.isDataDescriptorExists() && !this.canSkipExtendedLocalFileHeader) {
            DataDescriptor readDataDescriptor = this.headerReader.readDataDescriptor(this.inputStream, checkIfZip64ExtraDataRecordPresentInLFH(this.localFileHeader.getExtraDataRecords()));
            this.localFileHeader.setCompressedSize(readDataDescriptor.getCompressedSize());
            this.localFileHeader.setUncompressedSize(readDataDescriptor.getUncompressedSize());
            this.localFileHeader.setCrc(readDataDescriptor.getCrc());
        }
    }

    private void readUntilEndOfEntry() throws IOException {
        if ((!this.localFileHeader.isDirectory() && this.localFileHeader.getCompressedSize() != 0) || this.localFileHeader.isDataDescriptorExists()) {
            if (this.endOfEntryBuffer == null) {
                this.endOfEntryBuffer = new byte[512];
            }
            do {
            } while (read(this.endOfEntryBuffer) != -1);
            this.entryEOFReached = true;
        }
    }

    private void resetFields() {
        this.localFileHeader = null;
        this.crc32.reset();
    }

    private void verifyCrc() throws IOException {
        if ((this.localFileHeader.getEncryptionMethod() != EncryptionMethod.AES || !this.localFileHeader.getAesExtraDataRecord().getAesVersion().equals(AesVersion.TWO)) && this.localFileHeader.getCrc() != this.crc32.getValue()) {
            ZipException.Type type = ZipException.Type.CHECKSUM_MISMATCH;
            if (isEncryptionMethodZipStandard(this.localFileHeader)) {
                type = ZipException.Type.WRONG_PASSWORD;
            }
            throw new ZipException("Reached end of entry, but crc verification failed for " + this.localFileHeader.getFileName(), type);
        }
    }

    private void verifyLocalFileHeader(LocalFileHeader localFileHeader2) throws IOException {
        if (!isEntryDirectory(localFileHeader2.getFileName()) && localFileHeader2.getCompressionMethod() == CompressionMethod.STORE && localFileHeader2.getUncompressedSize() < 0) {
            throw new IOException("Invalid local file header for: " + localFileHeader2.getFileName() + ". Uncompressed size has to be set for entry of compression type store which is not a directory");
        }
    }

    public int available() throws IOException {
        assertStreamOpen();
        return this.entryEOFReached ^ true ? 1 : 0;
    }

    public void close() throws IOException {
        DecompressedInputStream decompressedInputStream2 = this.decompressedInputStream;
        if (decompressedInputStream2 != null) {
            decompressedInputStream2.close();
        }
        this.streamClosed = true;
    }

    public LocalFileHeader getNextEntry() throws IOException {
        return getNextEntry((FileHeader) null);
    }

    public void setPassword(char[] cArr) {
        this.password = cArr;
    }

    public ZipInputStream(InputStream inputStream2, Charset charset) {
        this(inputStream2, (char[]) null, charset);
    }

    public LocalFileHeader getNextEntry(FileHeader fileHeader) throws IOException {
        if (this.localFileHeader != null) {
            readUntilEndOfEntry();
        }
        LocalFileHeader readLocalFileHeader = this.headerReader.readLocalFileHeader(this.inputStream, this.zip4jConfig.getCharset());
        this.localFileHeader = readLocalFileHeader;
        if (readLocalFileHeader == null) {
            return null;
        }
        verifyLocalFileHeader(readLocalFileHeader);
        this.crc32.reset();
        if (fileHeader != null) {
            this.localFileHeader.setCrc(fileHeader.getCrc());
            this.localFileHeader.setCompressedSize(fileHeader.getCompressedSize());
            this.localFileHeader.setUncompressedSize(fileHeader.getUncompressedSize());
            this.localFileHeader.setDirectory(fileHeader.isDirectory());
            this.canSkipExtendedLocalFileHeader = true;
        } else {
            this.canSkipExtendedLocalFileHeader = false;
        }
        this.decompressedInputStream = initializeEntryInputStream(this.localFileHeader);
        this.entryEOFReached = false;
        return this.localFileHeader;
    }

    public int read() throws IOException {
        byte[] bArr = new byte[1];
        if (read(bArr) == -1) {
            return -1;
        }
        return bArr[0] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE;
    }

    public ZipInputStream(InputStream inputStream2, char[] cArr) {
        this(inputStream2, cArr, (Charset) null);
    }

    public ZipInputStream(InputStream inputStream2, char[] cArr, Charset charset) {
        this(inputStream2, cArr, new Zip4jConfig(charset, 4096));
    }

    public ZipInputStream(InputStream inputStream2, char[] cArr, Zip4jConfig zip4jConfig2) {
        this.headerReader = new HeaderReader();
        this.crc32 = new CRC32();
        this.canSkipExtendedLocalFileHeader = false;
        this.streamClosed = false;
        this.entryEOFReached = false;
        if (zip4jConfig2.getBufferSize() >= 512) {
            this.inputStream = new PushbackInputStream(inputStream2, zip4jConfig2.getBufferSize());
            this.password = cArr;
            this.zip4jConfig = zip4jConfig2;
            return;
        }
        throw new IllegalArgumentException("Buffer size cannot be less than 512 bytes");
    }

    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        if (i2 < 0) {
            throw new IllegalArgumentException("Negative read length");
        } else if (i2 == 0) {
            return 0;
        } else {
            LocalFileHeader localFileHeader2 = this.localFileHeader;
            if (localFileHeader2 == null || localFileHeader2.isDirectory()) {
                return -1;
            }
            try {
                int read = this.decompressedInputStream.read(bArr, i, i2);
                if (read == -1) {
                    endOfCompressedDataReached();
                } else {
                    this.crc32.update(bArr, i, read);
                }
                return read;
            } catch (IOException e) {
                if (isEncryptionMethodZipStandard(this.localFileHeader)) {
                    throw new ZipException(e.getMessage(), e.getCause(), ZipException.Type.WRONG_PASSWORD);
                }
                throw e;
            }
        }
    }
}
