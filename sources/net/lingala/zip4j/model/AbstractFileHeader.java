package net.lingala.zip4j.model;

import java.util.List;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import net.lingala.zip4j.util.Zip4jUtil;

public abstract class AbstractFileHeader extends ZipHeader {
    private AESExtraDataRecord aesExtraDataRecord;
    private long compressedSize = 0;
    private CompressionMethod compressionMethod;
    private long crc = 0;
    private boolean dataDescriptorExists;
    private EncryptionMethod encryptionMethod = EncryptionMethod.NONE;
    private List<ExtraDataRecord> extraDataRecords;
    private int extraFieldLength;
    private String fileName;
    private int fileNameLength;
    private boolean fileNameUTF8Encoded;
    private byte[] generalPurposeFlag;
    private boolean isDirectory;
    private boolean isEncrypted;
    private long lastModifiedTime;
    private long uncompressedSize = 0;
    private int versionNeededToExtract;
    private Zip64ExtendedInfo zip64ExtendedInfo;

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof AbstractFileHeader)) {
            return getFileName().equals(((AbstractFileHeader) obj).getFileName());
        }
        return false;
    }

    public AESExtraDataRecord getAesExtraDataRecord() {
        return this.aesExtraDataRecord;
    }

    public long getCompressedSize() {
        return this.compressedSize;
    }

    public CompressionMethod getCompressionMethod() {
        return this.compressionMethod;
    }

    public long getCrc() {
        return this.crc;
    }

    public EncryptionMethod getEncryptionMethod() {
        return this.encryptionMethod;
    }

    public List<ExtraDataRecord> getExtraDataRecords() {
        return this.extraDataRecords;
    }

    public int getExtraFieldLength() {
        return this.extraFieldLength;
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getFileNameLength() {
        return this.fileNameLength;
    }

    public byte[] getGeneralPurposeFlag() {
        return this.generalPurposeFlag;
    }

    public long getLastModifiedTime() {
        return this.lastModifiedTime;
    }

    public long getLastModifiedTimeEpoch() {
        return Zip4jUtil.dosToExtendedEpochTme(this.lastModifiedTime);
    }

    public long getUncompressedSize() {
        return this.uncompressedSize;
    }

    public int getVersionNeededToExtract() {
        return this.versionNeededToExtract;
    }

    public Zip64ExtendedInfo getZip64ExtendedInfo() {
        return this.zip64ExtendedInfo;
    }

    public boolean isDataDescriptorExists() {
        return this.dataDescriptorExists;
    }

    public boolean isDirectory() {
        return this.isDirectory;
    }

    public boolean isEncrypted() {
        return this.isEncrypted;
    }

    public boolean isFileNameUTF8Encoded() {
        return this.fileNameUTF8Encoded;
    }

    public void setAesExtraDataRecord(AESExtraDataRecord aESExtraDataRecord) {
        this.aesExtraDataRecord = aESExtraDataRecord;
    }

    public void setCompressedSize(long j) {
        this.compressedSize = j;
    }

    public void setCompressionMethod(CompressionMethod compressionMethod2) {
        this.compressionMethod = compressionMethod2;
    }

    public void setCrc(long j) {
        this.crc = j;
    }

    public void setDataDescriptorExists(boolean z) {
        this.dataDescriptorExists = z;
    }

    public void setDirectory(boolean z) {
        this.isDirectory = z;
    }

    public void setEncrypted(boolean z) {
        this.isEncrypted = z;
    }

    public void setEncryptionMethod(EncryptionMethod encryptionMethod2) {
        this.encryptionMethod = encryptionMethod2;
    }

    public void setExtraDataRecords(List<ExtraDataRecord> list) {
        this.extraDataRecords = list;
    }

    public void setExtraFieldLength(int i) {
        this.extraFieldLength = i;
    }

    public void setFileName(String str) {
        this.fileName = str;
    }

    public void setFileNameLength(int i) {
        this.fileNameLength = i;
    }

    public void setFileNameUTF8Encoded(boolean z) {
        this.fileNameUTF8Encoded = z;
    }

    public void setGeneralPurposeFlag(byte[] bArr) {
        this.generalPurposeFlag = bArr;
    }

    public void setLastModifiedTime(long j) {
        this.lastModifiedTime = j;
    }

    public void setUncompressedSize(long j) {
        this.uncompressedSize = j;
    }

    public void setVersionNeededToExtract(int i) {
        this.versionNeededToExtract = i;
    }

    public void setZip64ExtendedInfo(Zip64ExtendedInfo zip64ExtendedInfo2) {
        this.zip64ExtendedInfo = zip64ExtendedInfo2;
    }
}
