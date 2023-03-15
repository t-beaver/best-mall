package net.lingala.zip4j.tasks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.headers.HeaderUtil;
import net.lingala.zip4j.headers.HeaderWriter;
import net.lingala.zip4j.io.outputstream.SplitOutputStream;
import net.lingala.zip4j.io.outputstream.ZipOutputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.Zip4jConfig;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.tasks.AsyncZipTask;
import net.lingala.zip4j.tasks.RemoveFilesFromZipTask;
import net.lingala.zip4j.util.BitUtils;
import net.lingala.zip4j.util.CrcUtil;
import net.lingala.zip4j.util.FileUtils;
import net.lingala.zip4j.util.Zip4jUtil;

public abstract class AbstractAddFileToZipTask<T> extends AsyncZipTask<T> {
    private final HeaderWriter headerWriter;
    private final char[] password;
    private final ZipModel zipModel;

    AbstractAddFileToZipTask(ZipModel zipModel2, char[] cArr, HeaderWriter headerWriter2, AsyncZipTask.AsyncTaskParameters asyncTaskParameters) {
        super(asyncTaskParameters);
        this.zipModel = zipModel2;
        this.password = cArr;
        this.headerWriter = headerWriter2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002d, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0032, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0033, code lost:
        r4.addSuppressed(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0036, code lost:
        throw r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void addFileToZip(java.io.File r4, net.lingala.zip4j.io.outputstream.ZipOutputStream r5, net.lingala.zip4j.model.ZipParameters r6, net.lingala.zip4j.io.outputstream.SplitOutputStream r7, net.lingala.zip4j.progress.ProgressMonitor r8, byte[] r9) throws java.io.IOException {
        /*
            r3 = this;
            r5.putNextEntry(r6)
            boolean r6 = r4.exists()
            r0 = 0
            if (r6 == 0) goto L_0x0037
            boolean r6 = r4.isDirectory()
            if (r6 != 0) goto L_0x0037
            java.io.FileInputStream r6 = new java.io.FileInputStream
            r6.<init>(r4)
        L_0x0015:
            int r1 = r6.read(r9)     // Catch:{ all -> 0x002b }
            r2 = -1
            if (r1 == r2) goto L_0x0027
            r5.write(r9, r0, r1)     // Catch:{ all -> 0x002b }
            long r1 = (long) r1     // Catch:{ all -> 0x002b }
            r8.updateWorkCompleted(r1)     // Catch:{ all -> 0x002b }
            r3.verifyIfTaskIsCancelled()     // Catch:{ all -> 0x002b }
            goto L_0x0015
        L_0x0027:
            r6.close()
            goto L_0x0037
        L_0x002b:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x002d }
        L_0x002d:
            r5 = move-exception
            r6.close()     // Catch:{ all -> 0x0032 }
            goto L_0x0036
        L_0x0032:
            r6 = move-exception
            r4.addSuppressed(r6)
        L_0x0036:
            throw r5
        L_0x0037:
            r3.closeEntry(r5, r7, r4, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.tasks.AbstractAddFileToZipTask.addFileToZip(java.io.File, net.lingala.zip4j.io.outputstream.ZipOutputStream, net.lingala.zip4j.model.ZipParameters, net.lingala.zip4j.io.outputstream.SplitOutputStream, net.lingala.zip4j.progress.ProgressMonitor, byte[]):void");
    }

    private boolean addSymlink(ZipParameters zipParameters) {
        return ZipParameters.SymbolicLinkAction.INCLUDE_LINK_ONLY.equals(zipParameters.getSymbolicLinkAction()) || ZipParameters.SymbolicLinkAction.INCLUDE_LINK_AND_LINKED_FILE.equals(zipParameters.getSymbolicLinkAction());
    }

    private void addSymlinkToZip(File file, ZipOutputStream zipOutputStream, ZipParameters zipParameters, SplitOutputStream splitOutputStream) throws IOException {
        ZipParameters zipParameters2 = new ZipParameters(zipParameters);
        zipParameters2.setFileNameInZip(replaceFileNameInZip(zipParameters.getFileNameInZip(), file.getName()));
        zipParameters2.setEncryptFiles(false);
        zipParameters2.setCompressionMethod(CompressionMethod.STORE);
        zipOutputStream.putNextEntry(zipParameters2);
        zipOutputStream.write(FileUtils.readSymbolicLink(file).getBytes());
        closeEntry(zipOutputStream, splitOutputStream, file, true);
    }

    private ZipParameters cloneAndAdjustZipParameters(ZipParameters zipParameters, File file, ProgressMonitor progressMonitor) throws IOException {
        ZipParameters zipParameters2 = new ZipParameters(zipParameters);
        zipParameters2.setLastModifiedFileTime(Zip4jUtil.epochToExtendedDosTime(file.lastModified()));
        if (file.isDirectory()) {
            zipParameters2.setEntrySize(0);
        } else {
            zipParameters2.setEntrySize(file.length());
        }
        zipParameters2.setWriteExtendedLocalFileHeader(false);
        zipParameters2.setLastModifiedFileTime(file.lastModified());
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(zipParameters.getFileNameInZip())) {
            zipParameters2.setFileNameInZip(FileUtils.getRelativeFileName(file, zipParameters));
        }
        if (file.isDirectory()) {
            zipParameters2.setCompressionMethod(CompressionMethod.STORE);
            zipParameters2.setEncryptionMethod(EncryptionMethod.NONE);
            zipParameters2.setEncryptFiles(false);
        } else {
            if (zipParameters2.isEncryptFiles() && zipParameters2.getEncryptionMethod() == EncryptionMethod.ZIP_STANDARD) {
                progressMonitor.setCurrentTask(ProgressMonitor.Task.CALCULATE_CRC);
                zipParameters2.setEntryCRC(CrcUtil.computeFileCrc(file, progressMonitor));
                progressMonitor.setCurrentTask(ProgressMonitor.Task.ADD_ENTRY);
            }
            if (file.length() == 0) {
                zipParameters2.setCompressionMethod(CompressionMethod.STORE);
            }
        }
        return zipParameters2;
    }

    private void closeEntry(ZipOutputStream zipOutputStream, SplitOutputStream splitOutputStream, File file, boolean z) throws IOException {
        FileHeader closeEntry = zipOutputStream.closeEntry();
        byte[] fileAttributes = FileUtils.getFileAttributes(file);
        if (!z) {
            fileAttributes[3] = BitUtils.unsetBit(fileAttributes[3], 5);
        }
        closeEntry.setExternalFileAttributes(fileAttributes);
        updateLocalFileHeader(closeEntry, splitOutputStream);
    }

    private List<File> removeFilesIfExists(List<File> list, ZipParameters zipParameters, ProgressMonitor progressMonitor, Zip4jConfig zip4jConfig) throws ZipException {
        ArrayList arrayList = new ArrayList(list);
        if (!this.zipModel.getZipFile().exists()) {
            return arrayList;
        }
        for (File next : list) {
            if (!Zip4jUtil.isStringNotNullAndNotEmpty(next.getName())) {
                arrayList.remove(next);
            }
            FileHeader fileHeader = HeaderUtil.getFileHeader(this.zipModel, FileUtils.getRelativeFileName(next, zipParameters));
            if (fileHeader != null) {
                if (zipParameters.isOverrideExistingFilesInZip()) {
                    progressMonitor.setCurrentTask(ProgressMonitor.Task.REMOVE_ENTRY);
                    removeFile(fileHeader, progressMonitor, zip4jConfig);
                    verifyIfTaskIsCancelled();
                    progressMonitor.setCurrentTask(ProgressMonitor.Task.ADD_ENTRY);
                } else {
                    arrayList.remove(next);
                }
            }
        }
        return arrayList;
    }

    private String replaceFileNameInZip(String str, String str2) {
        if (!str.contains("/")) {
            return str2;
        }
        return str.substring(0, str.lastIndexOf("/") + 1) + str2;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0075, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0076, code lost:
        if (r13 != null) goto L_0x0078;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        r13.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x007c, code lost:
        r12 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        r10.addSuppressed(r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0080, code lost:
        throw r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0083, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
        r8.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0088, code lost:
        r12 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0089, code lost:
        r10.addSuppressed(r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x008d, code lost:
        throw r11;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addFilesToZip(java.util.List<java.io.File> r10, net.lingala.zip4j.progress.ProgressMonitor r11, net.lingala.zip4j.model.ZipParameters r12, net.lingala.zip4j.model.Zip4jConfig r13) throws java.io.IOException {
        /*
            r9 = this;
            net.lingala.zip4j.model.ZipParameters$SymbolicLinkAction r0 = r12.getSymbolicLinkAction()
            net.lingala.zip4j.util.FileUtils.assertFilesExist(r10, r0)
            int r0 = r13.getBufferSize()
            byte[] r0 = new byte[r0]
            java.util.List r10 = r9.removeFilesIfExists(r10, r12, r11, r13)
            net.lingala.zip4j.io.outputstream.SplitOutputStream r8 = new net.lingala.zip4j.io.outputstream.SplitOutputStream
            net.lingala.zip4j.model.ZipModel r1 = r9.zipModel
            java.io.File r1 = r1.getZipFile()
            net.lingala.zip4j.model.ZipModel r2 = r9.zipModel
            long r2 = r2.getSplitLength()
            r8.<init>(r1, r2)
            net.lingala.zip4j.io.outputstream.ZipOutputStream r13 = r9.initializeOutputStream(r8, r13)     // Catch:{ all -> 0x0081 }
            java.util.Iterator r10 = r10.iterator()     // Catch:{ all -> 0x0073 }
        L_0x002a:
            boolean r1 = r10.hasNext()     // Catch:{ all -> 0x0073 }
            if (r1 == 0) goto L_0x006a
            java.lang.Object r1 = r10.next()     // Catch:{ all -> 0x0073 }
            r2 = r1
            java.io.File r2 = (java.io.File) r2     // Catch:{ all -> 0x0073 }
            r9.verifyIfTaskIsCancelled()     // Catch:{ all -> 0x0073 }
            net.lingala.zip4j.model.ZipParameters r4 = r9.cloneAndAdjustZipParameters(r12, r2, r11)     // Catch:{ all -> 0x0073 }
            java.lang.String r1 = r2.getAbsolutePath()     // Catch:{ all -> 0x0073 }
            r11.setFileName(r1)     // Catch:{ all -> 0x0073 }
            boolean r1 = net.lingala.zip4j.util.FileUtils.isSymbolicLink(r2)     // Catch:{ all -> 0x0073 }
            if (r1 == 0) goto L_0x0061
            boolean r1 = r9.addSymlink(r4)     // Catch:{ all -> 0x0073 }
            if (r1 == 0) goto L_0x0061
            r9.addSymlinkToZip(r2, r13, r4, r8)     // Catch:{ all -> 0x0073 }
            net.lingala.zip4j.model.ZipParameters$SymbolicLinkAction r1 = net.lingala.zip4j.model.ZipParameters.SymbolicLinkAction.INCLUDE_LINK_ONLY     // Catch:{ all -> 0x0073 }
            net.lingala.zip4j.model.ZipParameters$SymbolicLinkAction r3 = r4.getSymbolicLinkAction()     // Catch:{ all -> 0x0073 }
            boolean r1 = r1.equals(r3)     // Catch:{ all -> 0x0073 }
            if (r1 == 0) goto L_0x0061
            goto L_0x002a
        L_0x0061:
            r1 = r9
            r3 = r13
            r5 = r8
            r6 = r11
            r7 = r0
            r1.addFileToZip(r2, r3, r4, r5, r6, r7)     // Catch:{ all -> 0x0073 }
            goto L_0x002a
        L_0x006a:
            if (r13 == 0) goto L_0x006f
            r13.close()     // Catch:{ all -> 0x0081 }
        L_0x006f:
            r8.close()
            return
        L_0x0073:
            r10 = move-exception
            throw r10     // Catch:{ all -> 0x0075 }
        L_0x0075:
            r11 = move-exception
            if (r13 == 0) goto L_0x0080
            r13.close()     // Catch:{ all -> 0x007c }
            goto L_0x0080
        L_0x007c:
            r12 = move-exception
            r10.addSuppressed(r12)     // Catch:{ all -> 0x0081 }
        L_0x0080:
            throw r11     // Catch:{ all -> 0x0081 }
        L_0x0081:
            r10 = move-exception
            throw r10     // Catch:{ all -> 0x0083 }
        L_0x0083:
            r11 = move-exception
            r8.close()     // Catch:{ all -> 0x0088 }
            goto L_0x008c
        L_0x0088:
            r12 = move-exception
            r10.addSuppressed(r12)
        L_0x008c:
            goto L_0x008e
        L_0x008d:
            throw r11
        L_0x008e:
            goto L_0x008d
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.tasks.AbstractAddFileToZipTask.addFilesToZip(java.util.List, net.lingala.zip4j.progress.ProgressMonitor, net.lingala.zip4j.model.ZipParameters, net.lingala.zip4j.model.Zip4jConfig):void");
    }

    /* access modifiers changed from: package-private */
    public long calculateWorkForFiles(List<File> list, ZipParameters zipParameters) throws ZipException {
        long j;
        long j2 = 0;
        for (File next : list) {
            if (next.exists()) {
                if (!zipParameters.isEncryptFiles() || zipParameters.getEncryptionMethod() != EncryptionMethod.ZIP_STANDARD) {
                    j = next.length();
                } else {
                    j = next.length() * 2;
                }
                j2 += j;
                FileHeader fileHeader = HeaderUtil.getFileHeader(getZipModel(), FileUtils.getRelativeFileName(next, zipParameters));
                if (fileHeader != null) {
                    j2 += getZipModel().getZipFile().length() - fileHeader.getCompressedSize();
                }
            }
        }
        return j2;
    }

    /* access modifiers changed from: protected */
    public ProgressMonitor.Task getTask() {
        return ProgressMonitor.Task.ADD_ENTRY;
    }

    /* access modifiers changed from: protected */
    public ZipModel getZipModel() {
        return this.zipModel;
    }

    /* access modifiers changed from: package-private */
    public ZipOutputStream initializeOutputStream(SplitOutputStream splitOutputStream, Zip4jConfig zip4jConfig) throws IOException {
        if (this.zipModel.getZipFile().exists()) {
            splitOutputStream.seek(HeaderUtil.getOffsetStartOfCentralDirectory(this.zipModel));
        }
        return new ZipOutputStream(splitOutputStream, this.password, zip4jConfig, this.zipModel);
    }

    /* access modifiers changed from: package-private */
    public void removeFile(FileHeader fileHeader, ProgressMonitor progressMonitor, Zip4jConfig zip4jConfig) throws ZipException {
        new RemoveFilesFromZipTask(this.zipModel, this.headerWriter, new AsyncZipTask.AsyncTaskParameters((ExecutorService) null, false, progressMonitor)).execute(new RemoveFilesFromZipTask.RemoveFilesFromZipTaskParameters(Collections.singletonList(fileHeader.getFileName()), zip4jConfig));
    }

    /* access modifiers changed from: package-private */
    public void updateLocalFileHeader(FileHeader fileHeader, SplitOutputStream splitOutputStream) throws IOException {
        this.headerWriter.updateLocalFileHeader(fileHeader, getZipModel(), splitOutputStream);
    }

    /* access modifiers changed from: package-private */
    public void verifyZipParameters(ZipParameters zipParameters) throws ZipException {
        if (zipParameters == null) {
            throw new ZipException("cannot validate zip parameters");
        } else if (zipParameters.getCompressionMethod() != CompressionMethod.STORE && zipParameters.getCompressionMethod() != CompressionMethod.DEFLATE) {
            throw new ZipException("unsupported compression type");
        } else if (!zipParameters.isEncryptFiles()) {
            zipParameters.setEncryptionMethod(EncryptionMethod.NONE);
        } else if (zipParameters.getEncryptionMethod() != EncryptionMethod.NONE) {
            char[] cArr = this.password;
            if (cArr == null || cArr.length <= 0) {
                throw new ZipException("input password is empty or null");
            }
        } else {
            throw new ZipException("Encryption method has to be set, when encrypt files flag is set");
        }
    }
}
