package net.lingala.zip4j.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ZipModel implements Cloneable {
    private ArchiveExtraDataRecord archiveExtraDataRecord = new ArchiveExtraDataRecord();
    private CentralDirectory centralDirectory = new CentralDirectory();
    private List<DataDescriptor> dataDescriptors = new ArrayList();
    private long end;
    private EndOfCentralDirectoryRecord endOfCentralDirectoryRecord = new EndOfCentralDirectoryRecord();
    private boolean isNestedZipFile;
    private boolean isZip64Format = false;
    private List<LocalFileHeader> localFileHeaders = new ArrayList();
    private boolean splitArchive;
    private long splitLength = -1;
    private long start;
    private Zip64EndOfCentralDirectoryLocator zip64EndOfCentralDirectoryLocator = new Zip64EndOfCentralDirectoryLocator();
    private Zip64EndOfCentralDirectoryRecord zip64EndOfCentralDirectoryRecord = new Zip64EndOfCentralDirectoryRecord();
    private File zipFile;

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public ArchiveExtraDataRecord getArchiveExtraDataRecord() {
        return this.archiveExtraDataRecord;
    }

    public CentralDirectory getCentralDirectory() {
        return this.centralDirectory;
    }

    public List<DataDescriptor> getDataDescriptors() {
        return this.dataDescriptors;
    }

    public long getEnd() {
        return this.end;
    }

    public EndOfCentralDirectoryRecord getEndOfCentralDirectoryRecord() {
        return this.endOfCentralDirectoryRecord;
    }

    public List<LocalFileHeader> getLocalFileHeaders() {
        return this.localFileHeaders;
    }

    public long getSplitLength() {
        return this.splitLength;
    }

    public long getStart() {
        return this.start;
    }

    public Zip64EndOfCentralDirectoryLocator getZip64EndOfCentralDirectoryLocator() {
        return this.zip64EndOfCentralDirectoryLocator;
    }

    public Zip64EndOfCentralDirectoryRecord getZip64EndOfCentralDirectoryRecord() {
        return this.zip64EndOfCentralDirectoryRecord;
    }

    public File getZipFile() {
        return this.zipFile;
    }

    public boolean isNestedZipFile() {
        return this.isNestedZipFile;
    }

    public boolean isSplitArchive() {
        return this.splitArchive;
    }

    public boolean isZip64Format() {
        return this.isZip64Format;
    }

    public void setArchiveExtraDataRecord(ArchiveExtraDataRecord archiveExtraDataRecord2) {
        this.archiveExtraDataRecord = archiveExtraDataRecord2;
    }

    public void setCentralDirectory(CentralDirectory centralDirectory2) {
        this.centralDirectory = centralDirectory2;
    }

    public void setDataDescriptors(List<DataDescriptor> list) {
        this.dataDescriptors = list;
    }

    public void setEnd(long j) {
        this.end = j;
    }

    public void setEndOfCentralDirectoryRecord(EndOfCentralDirectoryRecord endOfCentralDirectoryRecord2) {
        this.endOfCentralDirectoryRecord = endOfCentralDirectoryRecord2;
    }

    public void setLocalFileHeaders(List<LocalFileHeader> list) {
        this.localFileHeaders = list;
    }

    public void setNestedZipFile(boolean z) {
        this.isNestedZipFile = z;
    }

    public void setSplitArchive(boolean z) {
        this.splitArchive = z;
    }

    public void setSplitLength(long j) {
        this.splitLength = j;
    }

    public void setStart(long j) {
        this.start = j;
    }

    public void setZip64EndOfCentralDirectoryLocator(Zip64EndOfCentralDirectoryLocator zip64EndOfCentralDirectoryLocator2) {
        this.zip64EndOfCentralDirectoryLocator = zip64EndOfCentralDirectoryLocator2;
    }

    public void setZip64EndOfCentralDirectoryRecord(Zip64EndOfCentralDirectoryRecord zip64EndOfCentralDirectoryRecord2) {
        this.zip64EndOfCentralDirectoryRecord = zip64EndOfCentralDirectoryRecord2;
    }

    public void setZip64Format(boolean z) {
        this.isZip64Format = z;
    }

    public void setZipFile(File file) {
        this.zipFile = file;
    }
}
