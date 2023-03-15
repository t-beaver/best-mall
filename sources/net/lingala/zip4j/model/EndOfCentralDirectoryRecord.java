package net.lingala.zip4j.model;

import net.lingala.zip4j.headers.HeaderSignature;

public class EndOfCentralDirectoryRecord extends ZipHeader {
    private String comment = "";
    private int numberOfThisDisk;
    private int numberOfThisDiskStartOfCentralDir;
    private long offsetOfEndOfCentralDirectory;
    private long offsetOfStartOfCentralDirectory;
    private int sizeOfCentralDirectory;
    private int totalNumberOfEntriesInCentralDirectory;
    private int totalNumberOfEntriesInCentralDirectoryOnThisDisk;

    public EndOfCentralDirectoryRecord() {
        setSignature(HeaderSignature.END_OF_CENTRAL_DIRECTORY);
    }

    public String getComment() {
        return this.comment;
    }

    public int getNumberOfThisDisk() {
        return this.numberOfThisDisk;
    }

    public int getNumberOfThisDiskStartOfCentralDir() {
        return this.numberOfThisDiskStartOfCentralDir;
    }

    public long getOffsetOfEndOfCentralDirectory() {
        return this.offsetOfEndOfCentralDirectory;
    }

    public long getOffsetOfStartOfCentralDirectory() {
        return this.offsetOfStartOfCentralDirectory;
    }

    public int getSizeOfCentralDirectory() {
        return this.sizeOfCentralDirectory;
    }

    public int getTotalNumberOfEntriesInCentralDirectory() {
        return this.totalNumberOfEntriesInCentralDirectory;
    }

    public int getTotalNumberOfEntriesInCentralDirectoryOnThisDisk() {
        return this.totalNumberOfEntriesInCentralDirectoryOnThisDisk;
    }

    public void setComment(String str) {
        if (str != null) {
            this.comment = str;
        }
    }

    public void setNumberOfThisDisk(int i) {
        this.numberOfThisDisk = i;
    }

    public void setNumberOfThisDiskStartOfCentralDir(int i) {
        this.numberOfThisDiskStartOfCentralDir = i;
    }

    public void setOffsetOfEndOfCentralDirectory(long j) {
        this.offsetOfEndOfCentralDirectory = j;
    }

    public void setOffsetOfStartOfCentralDirectory(long j) {
        this.offsetOfStartOfCentralDirectory = j;
    }

    public void setSizeOfCentralDirectory(int i) {
        this.sizeOfCentralDirectory = i;
    }

    public void setTotalNumberOfEntriesInCentralDirectory(int i) {
        this.totalNumberOfEntriesInCentralDirectory = i;
    }

    public void setTotalNumberOfEntriesInCentralDirectoryOnThisDisk(int i) {
        this.totalNumberOfEntriesInCentralDirectoryOnThisDisk = i;
    }
}
