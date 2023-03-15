package net.lingala.zip4j.model;

import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.AesVersion;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

public class ZipParameters {
    private AesKeyStrength aesKeyStrength = AesKeyStrength.KEY_STRENGTH_256;
    private AesVersion aesVersion = AesVersion.TWO;
    private CompressionLevel compressionLevel = CompressionLevel.NORMAL;
    private CompressionMethod compressionMethod = CompressionMethod.DEFLATE;
    private String defaultFolderPath;
    private boolean encryptFiles = false;
    private EncryptionMethod encryptionMethod = EncryptionMethod.NONE;
    private long entryCRC;
    private long entrySize = -1;
    private ExcludeFileFilter excludeFileFilter;
    private String fileComment;
    private String fileNameInZip;
    private boolean includeRootFolder = true;
    private long lastModifiedFileTime = System.currentTimeMillis();
    private boolean overrideExistingFilesInZip = true;
    private boolean readHiddenFiles = true;
    private boolean readHiddenFolders = true;
    private String rootFolderNameInZip;
    private SymbolicLinkAction symbolicLinkAction = SymbolicLinkAction.INCLUDE_LINKED_FILE_ONLY;
    private boolean unixMode;
    private boolean writeExtendedLocalFileHeader = true;

    public enum SymbolicLinkAction {
        INCLUDE_LINK_ONLY,
        INCLUDE_LINKED_FILE_ONLY,
        INCLUDE_LINK_AND_LINKED_FILE
    }

    public ZipParameters() {
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public AesKeyStrength getAesKeyStrength() {
        return this.aesKeyStrength;
    }

    public AesVersion getAesVersion() {
        return this.aesVersion;
    }

    public CompressionLevel getCompressionLevel() {
        return this.compressionLevel;
    }

    public CompressionMethod getCompressionMethod() {
        return this.compressionMethod;
    }

    public String getDefaultFolderPath() {
        return this.defaultFolderPath;
    }

    public EncryptionMethod getEncryptionMethod() {
        return this.encryptionMethod;
    }

    public long getEntryCRC() {
        return this.entryCRC;
    }

    public long getEntrySize() {
        return this.entrySize;
    }

    public ExcludeFileFilter getExcludeFileFilter() {
        return this.excludeFileFilter;
    }

    public String getFileComment() {
        return this.fileComment;
    }

    public String getFileNameInZip() {
        return this.fileNameInZip;
    }

    public long getLastModifiedFileTime() {
        return this.lastModifiedFileTime;
    }

    public String getRootFolderNameInZip() {
        return this.rootFolderNameInZip;
    }

    public SymbolicLinkAction getSymbolicLinkAction() {
        return this.symbolicLinkAction;
    }

    public boolean isEncryptFiles() {
        return this.encryptFiles;
    }

    public boolean isIncludeRootFolder() {
        return this.includeRootFolder;
    }

    public boolean isOverrideExistingFilesInZip() {
        return this.overrideExistingFilesInZip;
    }

    public boolean isReadHiddenFiles() {
        return this.readHiddenFiles;
    }

    public boolean isReadHiddenFolders() {
        return this.readHiddenFolders;
    }

    public boolean isUnixMode() {
        return this.unixMode;
    }

    public boolean isWriteExtendedLocalFileHeader() {
        return this.writeExtendedLocalFileHeader;
    }

    public void setAesKeyStrength(AesKeyStrength aesKeyStrength2) {
        this.aesKeyStrength = aesKeyStrength2;
    }

    public void setAesVersion(AesVersion aesVersion2) {
        this.aesVersion = aesVersion2;
    }

    public void setCompressionLevel(CompressionLevel compressionLevel2) {
        this.compressionLevel = compressionLevel2;
    }

    public void setCompressionMethod(CompressionMethod compressionMethod2) {
        this.compressionMethod = compressionMethod2;
    }

    public void setDefaultFolderPath(String str) {
        this.defaultFolderPath = str;
    }

    public void setEncryptFiles(boolean z) {
        this.encryptFiles = z;
    }

    public void setEncryptionMethod(EncryptionMethod encryptionMethod2) {
        this.encryptionMethod = encryptionMethod2;
    }

    public void setEntryCRC(long j) {
        this.entryCRC = j;
    }

    public void setEntrySize(long j) {
        this.entrySize = j;
    }

    public void setExcludeFileFilter(ExcludeFileFilter excludeFileFilter2) {
        this.excludeFileFilter = excludeFileFilter2;
    }

    public void setFileComment(String str) {
        this.fileComment = str;
    }

    public void setFileNameInZip(String str) {
        this.fileNameInZip = str;
    }

    public void setIncludeRootFolder(boolean z) {
        this.includeRootFolder = z;
    }

    public void setLastModifiedFileTime(long j) {
        if (j > 0) {
            this.lastModifiedFileTime = j;
        }
    }

    public void setOverrideExistingFilesInZip(boolean z) {
        this.overrideExistingFilesInZip = z;
    }

    public void setReadHiddenFiles(boolean z) {
        this.readHiddenFiles = z;
    }

    public void setReadHiddenFolders(boolean z) {
        this.readHiddenFolders = z;
    }

    public void setRootFolderNameInZip(String str) {
        this.rootFolderNameInZip = str;
    }

    public void setSymbolicLinkAction(SymbolicLinkAction symbolicLinkAction2) {
        this.symbolicLinkAction = symbolicLinkAction2;
    }

    public void setUnixMode(boolean z) {
        this.unixMode = z;
    }

    public void setWriteExtendedLocalFileHeader(boolean z) {
        this.writeExtendedLocalFileHeader = z;
    }

    public ZipParameters(ZipParameters zipParameters) {
        this.compressionMethod = zipParameters.getCompressionMethod();
        this.compressionLevel = zipParameters.getCompressionLevel();
        this.encryptFiles = zipParameters.isEncryptFiles();
        this.encryptionMethod = zipParameters.getEncryptionMethod();
        this.readHiddenFiles = zipParameters.isReadHiddenFiles();
        this.readHiddenFolders = zipParameters.isReadHiddenFolders();
        this.aesKeyStrength = zipParameters.getAesKeyStrength();
        this.aesVersion = zipParameters.getAesVersion();
        this.includeRootFolder = zipParameters.isIncludeRootFolder();
        this.entryCRC = zipParameters.getEntryCRC();
        this.defaultFolderPath = zipParameters.getDefaultFolderPath();
        this.fileNameInZip = zipParameters.getFileNameInZip();
        this.lastModifiedFileTime = zipParameters.getLastModifiedFileTime();
        this.entrySize = zipParameters.getEntrySize();
        this.writeExtendedLocalFileHeader = zipParameters.isWriteExtendedLocalFileHeader();
        this.overrideExistingFilesInZip = zipParameters.isOverrideExistingFilesInZip();
        this.rootFolderNameInZip = zipParameters.getRootFolderNameInZip();
        this.fileComment = zipParameters.getFileComment();
        this.symbolicLinkAction = zipParameters.getSymbolicLinkAction();
        this.excludeFileFilter = zipParameters.getExcludeFileFilter();
        this.unixMode = zipParameters.isUnixMode();
    }
}
