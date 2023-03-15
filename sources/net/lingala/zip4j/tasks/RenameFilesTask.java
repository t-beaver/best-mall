package net.lingala.zip4j.tasks;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.headers.HeaderUtil;
import net.lingala.zip4j.headers.HeaderWriter;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.Zip4jConfig;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.tasks.AsyncZipTask;
import net.lingala.zip4j.util.RawIO;
import net.lingala.zip4j.util.Zip4jUtil;

public class RenameFilesTask extends AbstractModifyFileTask<RenameFilesTaskParameters> {
    private final HeaderWriter headerWriter;
    private final RawIO rawIO;
    private final ZipModel zipModel;

    public static class RenameFilesTaskParameters extends AbstractZipTaskParameters {
        /* access modifiers changed from: private */
        public final Map<String, String> fileNamesMap;

        public RenameFilesTaskParameters(Map<String, String> map, Zip4jConfig zip4jConfig) {
            super(zip4jConfig);
            this.fileNamesMap = map;
        }
    }

    public RenameFilesTask(ZipModel zipModel2, HeaderWriter headerWriter2, RawIO rawIO2, AsyncZipTask.AsyncTaskParameters asyncTaskParameters) {
        super(asyncTaskParameters);
        this.zipModel = zipModel2;
        this.headerWriter = headerWriter2;
        this.rawIO = rawIO2;
    }

    private long copyEntryAndChangeFileName(byte[] bArr, FileHeader fileHeader, long j, long j2, RandomAccessFile randomAccessFile, OutputStream outputStream, ProgressMonitor progressMonitor, int i) throws IOException {
        OutputStream outputStream2 = outputStream;
        ProgressMonitor progressMonitor2 = progressMonitor;
        int i2 = i;
        this.rawIO.writeShortLittleEndian(outputStream2, bArr.length);
        long copyFile = j + copyFile(randomAccessFile, outputStream, j, 26, progressMonitor2, i2) + 2;
        outputStream2.write(bArr);
        long copyFile2 = copyFile + copyFile(randomAccessFile, outputStream, copyFile, 2, progressMonitor2, i2) + ((long) fileHeader.getFileNameLength());
        return copyFile2 + copyFile(randomAccessFile, outputStream, copyFile2, j2 - (copyFile2 - j), progressMonitor, i);
    }

    private Map<String, String> filterNonExistingEntriesAndAddSeparatorIfNeeded(Map<String, String> map) throws ZipException {
        FileHeader fileHeader;
        HashMap hashMap = new HashMap();
        for (Map.Entry next : map.entrySet()) {
            if (Zip4jUtil.isStringNotNullAndNotEmpty((String) next.getKey()) && (fileHeader = HeaderUtil.getFileHeader(this.zipModel, (String) next.getKey())) != null) {
                if (!fileHeader.isDirectory() || ((String) next.getValue()).endsWith("/")) {
                    hashMap.put(next.getKey(), next.getValue());
                } else {
                    Object key = next.getKey();
                    hashMap.put(key, ((String) next.getValue()) + "/");
                }
            }
        }
        return hashMap;
    }

    private Map.Entry<String, String> getCorrespondingEntryFromMap(FileHeader fileHeader, Map<String, String> map) {
        for (Map.Entry<String, String> next : map.entrySet()) {
            if (fileHeader.getFileName().startsWith(next.getKey())) {
                return next;
            }
        }
        return null;
    }

    private String getNewFileName(String str, String str2, String str3) throws ZipException {
        if (str3.equals(str2)) {
            return str;
        }
        if (str3.startsWith(str2)) {
            String substring = str3.substring(str2.length());
            return str + substring;
        }
        throw new ZipException("old file name was neither an exact match nor a partial match");
    }

    private void updateHeadersInZipModel(List<FileHeader> list, FileHeader fileHeader, String str, byte[] bArr, int i) throws ZipException {
        FileHeader fileHeader2 = HeaderUtil.getFileHeader(this.zipModel, fileHeader.getFileName());
        if (fileHeader2 != null) {
            fileHeader2.setFileName(str);
            fileHeader2.setFileNameLength(bArr.length);
            long j = (long) i;
            updateOffsetsForAllSubsequentFileHeaders(list, this.zipModel, fileHeader2, j);
            this.zipModel.getEndOfCentralDirectoryRecord().setOffsetOfStartOfCentralDirectory(this.zipModel.getEndOfCentralDirectoryRecord().getOffsetOfStartOfCentralDirectory() + j);
            if (this.zipModel.isZip64Format()) {
                this.zipModel.getZip64EndOfCentralDirectoryRecord().setOffsetStartCentralDirectoryWRTStartDiskNumber(this.zipModel.getZip64EndOfCentralDirectoryRecord().getOffsetStartCentralDirectoryWRTStartDiskNumber() + j);
                this.zipModel.getZip64EndOfCentralDirectoryLocator().setOffsetZip64EndOfCentralDirectoryRecord(this.zipModel.getZip64EndOfCentralDirectoryLocator().getOffsetZip64EndOfCentralDirectoryRecord() + j);
                return;
            }
            return;
        }
        throw new ZipException("could not find any header with name: " + fileHeader.getFileName());
    }

    /* access modifiers changed from: protected */
    public ProgressMonitor.Task getTask() {
        return ProgressMonitor.Task.RENAME_FILE;
    }

    /* access modifiers changed from: protected */
    public long calculateTotalWork(RenameFilesTaskParameters renameFilesTaskParameters) {
        return this.zipModel.getZipFile().length();
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x0142, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0143, code lost:
        r2 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:?, code lost:
        r24.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0148, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:?, code lost:
        r1.addSuppressed(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x014e, code lost:
        r0 = th;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:32:0x0116, B:57:0x0141, B:61:0x0144] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void executeTask(net.lingala.zip4j.tasks.RenameFilesTask.RenameFilesTaskParameters r26, net.lingala.zip4j.progress.ProgressMonitor r27) throws java.io.IOException {
        /*
            r25 = this;
            r12 = r25
            r0 = r26
            java.util.Map r1 = r26.fileNamesMap
            java.util.Map r13 = r12.filterNonExistingEntriesAndAddSeparatorIfNeeded(r1)
            int r1 = r13.size()
            if (r1 != 0) goto L_0x0013
            return
        L_0x0013:
            net.lingala.zip4j.model.ZipModel r1 = r12.zipModel
            java.io.File r1 = r1.getZipFile()
            java.lang.String r1 = r1.getPath()
            java.io.File r14 = r12.getTemporaryFile(r1)
            java.io.RandomAccessFile r11 = new java.io.RandomAccessFile     // Catch:{ all -> 0x0150 }
            net.lingala.zip4j.model.ZipModel r1 = r12.zipModel     // Catch:{ all -> 0x0150 }
            java.io.File r1 = r1.getZipFile()     // Catch:{ all -> 0x0150 }
            net.lingala.zip4j.model.enums.RandomAccessFileMode r2 = net.lingala.zip4j.model.enums.RandomAccessFileMode.WRITE     // Catch:{ all -> 0x0150 }
            java.lang.String r2 = r2.getValue()     // Catch:{ all -> 0x0150 }
            r11.<init>(r1, r2)     // Catch:{ all -> 0x0150 }
            net.lingala.zip4j.io.outputstream.SplitOutputStream r10 = new net.lingala.zip4j.io.outputstream.SplitOutputStream     // Catch:{ all -> 0x013c }
            r10.<init>(r14)     // Catch:{ all -> 0x013c }
            r1 = 0
            net.lingala.zip4j.model.Zip4jConfig r3 = r0.zip4jConfig     // Catch:{ all -> 0x0128 }
            java.nio.charset.Charset r9 = r3.getCharset()     // Catch:{ all -> 0x0128 }
            net.lingala.zip4j.model.ZipModel r3 = r12.zipModel     // Catch:{ all -> 0x0128 }
            net.lingala.zip4j.model.CentralDirectory r3 = r3.getCentralDirectory()     // Catch:{ all -> 0x0128 }
            java.util.List r3 = r3.getFileHeaders()     // Catch:{ all -> 0x0128 }
            java.util.List r8 = r12.cloneAndSortFileHeadersByOffset(r3)     // Catch:{ all -> 0x0128 }
            java.util.Iterator r16 = r8.iterator()     // Catch:{ all -> 0x0128 }
            r17 = r1
        L_0x0053:
            boolean r1 = r16.hasNext()     // Catch:{ all -> 0x0128 }
            if (r1 == 0) goto L_0x0107
            java.lang.Object r1 = r16.next()     // Catch:{ all -> 0x0128 }
            r6 = r1
            net.lingala.zip4j.model.FileHeader r6 = (net.lingala.zip4j.model.FileHeader) r6     // Catch:{ all -> 0x0128 }
            java.util.Map$Entry r1 = r12.getCorrespondingEntryFromMap(r6, r13)     // Catch:{ all -> 0x0128 }
            java.lang.String r2 = r6.getFileName()     // Catch:{ all -> 0x0128 }
            r7 = r27
            r7.setFileName(r2)     // Catch:{ all -> 0x0128 }
            net.lingala.zip4j.model.ZipModel r2 = r12.zipModel     // Catch:{ all -> 0x0128 }
            long r2 = r12.getOffsetOfNextEntry(r8, r6, r2)     // Catch:{ all -> 0x0128 }
            long r4 = r10.getFilePointer()     // Catch:{ all -> 0x0128 }
            long r19 = r2 - r4
            if (r1 != 0) goto L_0x00a4
            net.lingala.zip4j.model.Zip4jConfig r1 = r0.zip4jConfig     // Catch:{ all -> 0x009d }
            int r21 = r1.getBufferSize()     // Catch:{ all -> 0x009d }
            r1 = r25
            r2 = r11
            r3 = r10
            r4 = r17
            r6 = r19
            r22 = r8
            r8 = r27
            r15 = r9
            r9 = r21
            long r1 = r1.copyFile(r2, r3, r4, r6, r8, r9)     // Catch:{ all -> 0x009d }
            long r17 = r17 + r1
            r24 = r11
            r20 = r13
            r13 = r10
            goto L_0x00fa
        L_0x009d:
            r0 = move-exception
            r1 = r0
            r13 = r10
            r24 = r11
            goto L_0x012d
        L_0x00a4:
            r22 = r8
            r15 = r9
            java.lang.Object r2 = r1.getValue()     // Catch:{ all -> 0x0128 }
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ all -> 0x0128 }
            java.lang.Object r1 = r1.getKey()     // Catch:{ all -> 0x0128 }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ all -> 0x0128 }
            java.lang.String r3 = r6.getFileName()     // Catch:{ all -> 0x0128 }
            java.lang.String r9 = r12.getNewFileName(r2, r1, r3)     // Catch:{ all -> 0x0128 }
            byte[] r8 = net.lingala.zip4j.headers.HeaderUtil.getBytesFromString(r9, r15)     // Catch:{ all -> 0x0128 }
            int r1 = r8.length     // Catch:{ all -> 0x0128 }
            int r2 = r6.getFileNameLength()     // Catch:{ all -> 0x0128 }
            int r21 = r1 - r2
            net.lingala.zip4j.model.Zip4jConfig r1 = r0.zip4jConfig     // Catch:{ all -> 0x0128 }
            int r23 = r1.getBufferSize()     // Catch:{ all -> 0x0128 }
            r1 = r25
            r2 = r8
            r3 = r6
            r4 = r17
            r17 = r6
            r6 = r19
            r18 = r8
            r8 = r11
            r19 = r9
            r9 = r10
            r20 = r13
            r13 = r10
            r10 = r27
            r24 = r11
            r11 = r23
            long r7 = r1.copyEntryAndChangeFileName(r2, r3, r4, r6, r8, r9, r10, r11)     // Catch:{ all -> 0x0126 }
            r1 = r25
            r2 = r22
            r3 = r17
            r4 = r19
            r5 = r18
            r6 = r21
            r1.updateHeadersInZipModel(r2, r3, r4, r5, r6)     // Catch:{ all -> 0x0126 }
            r17 = r7
        L_0x00fa:
            r25.verifyIfTaskIsCancelled()     // Catch:{ all -> 0x0126 }
            r10 = r13
            r9 = r15
            r13 = r20
            r8 = r22
            r11 = r24
            goto L_0x0053
        L_0x0107:
            r15 = r9
            r13 = r10
            r24 = r11
            net.lingala.zip4j.headers.HeaderWriter r0 = r12.headerWriter     // Catch:{ all -> 0x0126 }
            net.lingala.zip4j.model.ZipModel r1 = r12.zipModel     // Catch:{ all -> 0x0126 }
            r0.finalizeZipFile(r1, r13, r15)     // Catch:{ all -> 0x0126 }
            r15 = 1
            r13.close()     // Catch:{ all -> 0x0123 }
            r24.close()     // Catch:{ all -> 0x014e }
            net.lingala.zip4j.model.ZipModel r0 = r12.zipModel
            java.io.File r0 = r0.getZipFile()
            r12.cleanupFile(r15, r0, r14)
            return
        L_0x0123:
            r0 = move-exception
            r1 = r0
            goto L_0x0141
        L_0x0126:
            r0 = move-exception
            goto L_0x012c
        L_0x0128:
            r0 = move-exception
            r13 = r10
            r24 = r11
        L_0x012c:
            r1 = r0
        L_0x012d:
            throw r1     // Catch:{ all -> 0x012e }
        L_0x012e:
            r0 = move-exception
            r2 = r0
            r13.close()     // Catch:{ all -> 0x0134 }
            goto L_0x0139
        L_0x0134:
            r0 = move-exception
            r3 = r0
            r1.addSuppressed(r3)     // Catch:{ all -> 0x013a }
        L_0x0139:
            throw r2     // Catch:{ all -> 0x013a }
        L_0x013a:
            r0 = move-exception
            goto L_0x013f
        L_0x013c:
            r0 = move-exception
            r24 = r11
        L_0x013f:
            r1 = r0
            r15 = 0
        L_0x0141:
            throw r1     // Catch:{ all -> 0x0142 }
        L_0x0142:
            r0 = move-exception
            r2 = r0
            r24.close()     // Catch:{ all -> 0x0148 }
            goto L_0x014d
        L_0x0148:
            r0 = move-exception
            r3 = r0
            r1.addSuppressed(r3)     // Catch:{ all -> 0x014e }
        L_0x014d:
            throw r2     // Catch:{ all -> 0x014e }
        L_0x014e:
            r0 = move-exception
            goto L_0x0152
        L_0x0150:
            r0 = move-exception
            r15 = 0
        L_0x0152:
            net.lingala.zip4j.model.ZipModel r1 = r12.zipModel
            java.io.File r1 = r1.getZipFile()
            r12.cleanupFile(r15, r1, r14)
            goto L_0x015d
        L_0x015c:
            throw r0
        L_0x015d:
            goto L_0x015c
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.tasks.RenameFilesTask.executeTask(net.lingala.zip4j.tasks.RenameFilesTask$RenameFilesTaskParameters, net.lingala.zip4j.progress.ProgressMonitor):void");
    }
}
