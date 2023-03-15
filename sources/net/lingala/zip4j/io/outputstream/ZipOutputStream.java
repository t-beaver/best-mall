package net.lingala.zip4j.io.outputstream;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.zip.CRC32;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.headers.FileHeaderFactory;
import net.lingala.zip4j.headers.HeaderSignature;
import net.lingala.zip4j.headers.HeaderWriter;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.Zip4jConfig;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesVersion;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import net.lingala.zip4j.util.RawIO;

public class ZipOutputStream extends OutputStream {
    private CompressedOutputStream compressedOutputStream;
    private CountingOutputStream countingOutputStream;
    private CRC32 crc32;
    private boolean entryClosed;
    private FileHeader fileHeader;
    private FileHeaderFactory fileHeaderFactory;
    private HeaderWriter headerWriter;
    private LocalFileHeader localFileHeader;
    private char[] password;
    private RawIO rawIO;
    private boolean streamClosed;
    private long uncompressedSizeForThisEntry;
    private Zip4jConfig zip4jConfig;
    private ZipModel zipModel;

    public ZipOutputStream(OutputStream outputStream) throws IOException {
        this(outputStream, (char[]) null, (Charset) null);
    }

    private void ensureStreamOpen() throws IOException {
        if (this.streamClosed) {
            throw new IOException("Stream is closed");
        }
    }

    private void initializeAndWriteFileHeader(ZipParameters zipParameters) throws IOException {
        FileHeader generateFileHeader = this.fileHeaderFactory.generateFileHeader(zipParameters, this.countingOutputStream.isSplitZipFile(), this.countingOutputStream.getCurrentSplitFileCounter(), this.zip4jConfig.getCharset(), this.rawIO);
        this.fileHeader = generateFileHeader;
        generateFileHeader.setOffsetLocalHeader(this.countingOutputStream.getOffsetForNextEntry());
        LocalFileHeader generateLocalFileHeader = this.fileHeaderFactory.generateLocalFileHeader(this.fileHeader);
        this.localFileHeader = generateLocalFileHeader;
        this.headerWriter.writeLocalFileHeader(this.zipModel, generateLocalFileHeader, this.countingOutputStream, this.zip4jConfig.getCharset());
    }

    private CipherOutputStream initializeCipherOutputStream(ZipEntryOutputStream zipEntryOutputStream, ZipParameters zipParameters) throws IOException {
        if (!zipParameters.isEncryptFiles()) {
            return new NoCipherOutputStream(zipEntryOutputStream, zipParameters, (char[]) null);
        }
        char[] cArr = this.password;
        if (cArr == null || cArr.length == 0) {
            throw new ZipException("password not set");
        } else if (zipParameters.getEncryptionMethod() == EncryptionMethod.AES) {
            return new AesCipherOutputStream(zipEntryOutputStream, zipParameters, this.password);
        } else {
            if (zipParameters.getEncryptionMethod() == EncryptionMethod.ZIP_STANDARD) {
                return new ZipStandardCipherOutputStream(zipEntryOutputStream, zipParameters, this.password);
            }
            EncryptionMethod encryptionMethod = zipParameters.getEncryptionMethod();
            EncryptionMethod encryptionMethod2 = EncryptionMethod.ZIP_STANDARD_VARIANT_STRONG;
            if (encryptionMethod == encryptionMethod2) {
                throw new ZipException(encryptionMethod2 + " encryption method is not supported");
            }
            throw new ZipException("Invalid encryption method");
        }
    }

    private CompressedOutputStream initializeCompressedOutputStream(ZipParameters zipParameters) throws IOException {
        return initializeCompressedOutputStream(initializeCipherOutputStream(new ZipEntryOutputStream(this.countingOutputStream), zipParameters), zipParameters);
    }

    private ZipModel initializeZipModel(ZipModel zipModel2, CountingOutputStream countingOutputStream2) {
        if (zipModel2 == null) {
            zipModel2 = new ZipModel();
        }
        if (countingOutputStream2.isSplitZipFile()) {
            zipModel2.setSplitArchive(true);
            zipModel2.setSplitLength(countingOutputStream2.getSplitLength());
        }
        return zipModel2;
    }

    private boolean isEntryDirectory(String str) {
        return str.endsWith("/") || str.endsWith("\\");
    }

    private void reset() throws IOException {
        this.uncompressedSizeForThisEntry = 0;
        this.crc32.reset();
        this.compressedOutputStream.close();
    }

    private void verifyZipParameters(ZipParameters zipParameters) {
        if (zipParameters.getCompressionMethod() == CompressionMethod.STORE && zipParameters.getEntrySize() < 0 && !isEntryDirectory(zipParameters.getFileNameInZip()) && zipParameters.isWriteExtendedLocalFileHeader()) {
            throw new IllegalArgumentException("uncompressed size should be set for zip entries of compression type store");
        }
    }

    private boolean writeCrc(FileHeader fileHeader2) {
        if (!(fileHeader2.isEncrypted() && fileHeader2.getEncryptionMethod().equals(EncryptionMethod.AES))) {
            return true;
        }
        return fileHeader2.getAesExtraDataRecord().getAesVersion().equals(AesVersion.ONE);
    }

    private void writeSplitZipHeaderIfApplicable() throws IOException {
        if (this.countingOutputStream.isSplitZipFile()) {
            this.rawIO.writeIntLittleEndian(this.countingOutputStream, (int) HeaderSignature.SPLIT_ZIP.getValue());
        }
    }

    public void close() throws IOException {
        if (!this.entryClosed) {
            closeEntry();
        }
        this.zipModel.getEndOfCentralDirectoryRecord().setOffsetOfStartOfCentralDirectory(this.countingOutputStream.getNumberOfBytesWritten());
        this.headerWriter.finalizeZipFile(this.zipModel, this.countingOutputStream, this.zip4jConfig.getCharset());
        this.countingOutputStream.close();
        this.streamClosed = true;
    }

    public FileHeader closeEntry() throws IOException {
        this.compressedOutputStream.closeEntry();
        long compressedSize = this.compressedOutputStream.getCompressedSize();
        this.fileHeader.setCompressedSize(compressedSize);
        this.localFileHeader.setCompressedSize(compressedSize);
        this.fileHeader.setUncompressedSize(this.uncompressedSizeForThisEntry);
        this.localFileHeader.setUncompressedSize(this.uncompressedSizeForThisEntry);
        if (writeCrc(this.fileHeader)) {
            this.fileHeader.setCrc(this.crc32.getValue());
            this.localFileHeader.setCrc(this.crc32.getValue());
        }
        this.zipModel.getLocalFileHeaders().add(this.localFileHeader);
        this.zipModel.getCentralDirectory().getFileHeaders().add(this.fileHeader);
        if (this.localFileHeader.isDataDescriptorExists()) {
            this.headerWriter.writeExtendedLocalHeader(this.localFileHeader, this.countingOutputStream);
        }
        reset();
        this.entryClosed = true;
        return this.fileHeader;
    }

    public void putNextEntry(ZipParameters zipParameters) throws IOException {
        verifyZipParameters(zipParameters);
        initializeAndWriteFileHeader(zipParameters);
        this.compressedOutputStream = initializeCompressedOutputStream(zipParameters);
        this.entryClosed = false;
    }

    public void setComment(String str) throws IOException {
        ensureStreamOpen();
        this.zipModel.getEndOfCentralDirectoryRecord().setComment(str);
    }

    public void write(int i) throws IOException {
        write(new byte[]{(byte) i});
    }

    public ZipOutputStream(OutputStream outputStream, Charset charset) throws IOException {
        this(outputStream, (char[]) null, charset);
    }

    public void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    public ZipOutputStream(OutputStream outputStream, char[] cArr) throws IOException {
        this(outputStream, cArr, (Charset) null);
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        ensureStreamOpen();
        this.crc32.update(bArr, i, i2);
        this.compressedOutputStream.write(bArr, i, i2);
        this.uncompressedSizeForThisEntry += (long) i2;
    }

    public ZipOutputStream(OutputStream outputStream, char[] cArr, Charset charset) throws IOException {
        this(outputStream, cArr, new Zip4jConfig(charset, 4096), new ZipModel());
    }

    private CompressedOutputStream initializeCompressedOutputStream(CipherOutputStream cipherOutputStream, ZipParameters zipParameters) {
        if (zipParameters.getCompressionMethod() == CompressionMethod.DEFLATE) {
            return new DeflaterOutputStream(cipherOutputStream, zipParameters.getCompressionLevel(), this.zip4jConfig.getBufferSize());
        }
        return new StoreOutputStream(cipherOutputStream);
    }

    public ZipOutputStream(OutputStream outputStream, char[] cArr, Zip4jConfig zip4jConfig2, ZipModel zipModel2) throws IOException {
        this.fileHeaderFactory = new FileHeaderFactory();
        this.headerWriter = new HeaderWriter();
        this.crc32 = new CRC32();
        this.rawIO = new RawIO();
        this.uncompressedSizeForThisEntry = 0;
        this.entryClosed = true;
        if (zip4jConfig2.getBufferSize() >= 512) {
            CountingOutputStream countingOutputStream2 = new CountingOutputStream(outputStream);
            this.countingOutputStream = countingOutputStream2;
            this.password = cArr;
            this.zip4jConfig = zip4jConfig2;
            this.zipModel = initializeZipModel(zipModel2, countingOutputStream2);
            this.streamClosed = false;
            writeSplitZipHeaderIfApplicable();
            return;
        }
        throw new IllegalArgumentException("Buffer size cannot be less than 512 bytes");
    }
}
