package net.lingala.zip4j.model;

public class Zip64EndOfCentralDirectoryRecord extends ZipHeader {
    private byte[] extensibleDataSector;
    private int numberOfThisDisk;
    private int numberOfThisDiskStartOfCentralDirectory;
    private long offsetStartCentralDirectoryWRTStartDiskNumber = -1;
    private long sizeOfCentralDirectory;
    private long sizeOfZip64EndCentralDirectoryRecord;
    private long totalNumberOfEntriesInCentralDirectory;
    private long totalNumberOfEntriesInCentralDirectoryOnThisDisk;
    private int versionMadeBy;
    private int versionNeededToExtract;

    public byte[] getExtensibleDataSector() {
        return this.extensibleDataSector;
    }

    public int getNumberOfThisDisk() {
        return this.numberOfThisDisk;
    }

    public int getNumberOfThisDiskStartOfCentralDirectory() {
        return this.numberOfThisDiskStartOfCentralDirectory;
    }

    public long getOffsetStartCentralDirectoryWRTStartDiskNumber() {
        return this.offsetStartCentralDirectoryWRTStartDiskNumber;
    }

    public long getSizeOfCentralDirectory() {
        return this.sizeOfCentralDirectory;
    }

    public long getSizeOfZip64EndCentralDirectoryRecord() {
        return this.sizeOfZip64EndCentralDirectoryRecord;
    }

    public long getTotalNumberOfEntriesInCentralDirectory() {
        return this.totalNumberOfEntriesInCentralDirectory;
    }

    public long getTotalNumberOfEntriesInCentralDirectoryOnThisDisk() {
        return this.totalNumberOfEntriesInCentralDirectoryOnThisDisk;
    }

    public int getVersionMadeBy() {
        return this.versionMadeBy;
    }

    public int getVersionNeededToExtract() {
        return this.versionNeededToExtract;
    }

    public void setExtensibleDataSector(byte[] bArr) {
        this.extensibleDataSector = bArr;
    }

    public void setNumberOfThisDisk(int i) {
        this.numberOfThisDisk = i;
    }

    public void setNumberOfThisDiskStartOfCentralDirectory(int i) {
        this.numberOfThisDiskStartOfCentralDirectory = i;
    }

    public void setOffsetStartCentralDirectoryWRTStartDiskNumber(long j) {
        this.offsetStartCentralDirectoryWRTStartDiskNumber = j;
    }

    public void setSizeOfCentralDirectory(long j) {
        this.sizeOfCentralDirectory = j;
    }

    public void setSizeOfZip64EndCentralDirectoryRecord(long j) {
        this.sizeOfZip64EndCentralDirectoryRecord = j;
    }

    public void setTotalNumberOfEntriesInCentralDirectory(long j) {
        this.totalNumberOfEntriesInCentralDirectory = j;
    }

    public void setTotalNumberOfEntriesInCentralDirectoryOnThisDisk(long j) {
        this.totalNumberOfEntriesInCentralDirectoryOnThisDisk = j;
    }

    public void setVersionMadeBy(int i) {
        this.versionMadeBy = i;
    }

    public void setVersionNeededToExtract(int i) {
        this.versionNeededToExtract = i;
    }
}
