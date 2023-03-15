package net.lingala.zip4j.model;

public class Zip64EndOfCentralDirectoryLocator extends ZipHeader {
    private int numberOfDiskStartOfZip64EndOfCentralDirectoryRecord;
    private long offsetZip64EndOfCentralDirectoryRecord;
    private int totalNumberOfDiscs;

    public int getNumberOfDiskStartOfZip64EndOfCentralDirectoryRecord() {
        return this.numberOfDiskStartOfZip64EndOfCentralDirectoryRecord;
    }

    public long getOffsetZip64EndOfCentralDirectoryRecord() {
        return this.offsetZip64EndOfCentralDirectoryRecord;
    }

    public int getTotalNumberOfDiscs() {
        return this.totalNumberOfDiscs;
    }

    public void setNumberOfDiskStartOfZip64EndOfCentralDirectoryRecord(int i) {
        this.numberOfDiskStartOfZip64EndOfCentralDirectoryRecord = i;
    }

    public void setOffsetZip64EndOfCentralDirectoryRecord(long j) {
        this.offsetZip64EndOfCentralDirectoryRecord = j;
    }

    public void setTotalNumberOfDiscs(int i) {
        this.totalNumberOfDiscs = i;
    }
}
