package net.lingala.zip4j.tasks;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.headers.HeaderUtil;
import net.lingala.zip4j.headers.HeaderWriter;
import net.lingala.zip4j.io.outputstream.SplitOutputStream;
import net.lingala.zip4j.model.EndOfCentralDirectoryRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.Zip4jConfig;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.model.enums.RandomAccessFileMode;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.tasks.AsyncZipTask;

public class RemoveFilesFromZipTask extends AbstractModifyFileTask<RemoveFilesFromZipTaskParameters> {
    private final HeaderWriter headerWriter;
    private final ZipModel zipModel;

    public static class RemoveFilesFromZipTaskParameters extends AbstractZipTaskParameters {
        /* access modifiers changed from: private */
        public final List<String> filesToRemove;

        public RemoveFilesFromZipTaskParameters(List<String> list, Zip4jConfig zip4jConfig) {
            super(zip4jConfig);
            this.filesToRemove = list;
        }
    }

    public RemoveFilesFromZipTask(ZipModel zipModel2, HeaderWriter headerWriter2, AsyncZipTask.AsyncTaskParameters asyncTaskParameters) {
        super(asyncTaskParameters);
        this.zipModel = zipModel2;
        this.headerWriter = headerWriter2;
    }

    private List<String> filterNonExistingEntries(List<String> list) throws ZipException {
        ArrayList arrayList = new ArrayList();
        for (String next : list) {
            if (HeaderUtil.getFileHeader(this.zipModel, next) != null) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    private long negate(long j) {
        if (j != Long.MIN_VALUE) {
            return -j;
        }
        throw new ArithmeticException("long overflow");
    }

    /* JADX WARNING: Removed duplicated region for block: B:3:0x000a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean shouldEntryBeRemoved(net.lingala.zip4j.model.FileHeader r4, java.util.List<java.lang.String> r5) {
        /*
            r3 = this;
            java.util.Iterator r5 = r5.iterator()
        L_0x0004:
            boolean r0 = r5.hasNext()
            if (r0 == 0) goto L_0x002f
            java.lang.Object r0 = r5.next()
            java.lang.String r0 = (java.lang.String) r0
            java.lang.String r1 = "/"
            boolean r1 = r0.endsWith(r1)
            r2 = 1
            if (r1 == 0) goto L_0x0024
            java.lang.String r1 = r4.getFileName()
            boolean r1 = r1.startsWith(r0)
            if (r1 == 0) goto L_0x0024
            return r2
        L_0x0024:
            java.lang.String r1 = r4.getFileName()
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0004
            return r2
        L_0x002f:
            r4 = 0
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.tasks.RemoveFilesFromZipTask.shouldEntryBeRemoved(net.lingala.zip4j.model.FileHeader, java.util.List):boolean");
    }

    private void updateHeaders(List<FileHeader> list, FileHeader fileHeader, long j) throws ZipException {
        updateOffsetsForAllSubsequentFileHeaders(list, this.zipModel, fileHeader, negate(j));
        EndOfCentralDirectoryRecord endOfCentralDirectoryRecord = this.zipModel.getEndOfCentralDirectoryRecord();
        endOfCentralDirectoryRecord.setOffsetOfStartOfCentralDirectory(endOfCentralDirectoryRecord.getOffsetOfStartOfCentralDirectory() - j);
        endOfCentralDirectoryRecord.setTotalNumberOfEntriesInCentralDirectory(endOfCentralDirectoryRecord.getTotalNumberOfEntriesInCentralDirectory() - 1);
        if (endOfCentralDirectoryRecord.getTotalNumberOfEntriesInCentralDirectoryOnThisDisk() > 0) {
            endOfCentralDirectoryRecord.setTotalNumberOfEntriesInCentralDirectoryOnThisDisk(endOfCentralDirectoryRecord.getTotalNumberOfEntriesInCentralDirectoryOnThisDisk() - 1);
        }
        if (this.zipModel.isZip64Format()) {
            this.zipModel.getZip64EndOfCentralDirectoryRecord().setOffsetStartCentralDirectoryWRTStartDiskNumber(this.zipModel.getZip64EndOfCentralDirectoryRecord().getOffsetStartCentralDirectoryWRTStartDiskNumber() - j);
            this.zipModel.getZip64EndOfCentralDirectoryRecord().setTotalNumberOfEntriesInCentralDirectoryOnThisDisk(this.zipModel.getZip64EndOfCentralDirectoryRecord().getTotalNumberOfEntriesInCentralDirectory() - 1);
            this.zipModel.getZip64EndOfCentralDirectoryLocator().setOffsetZip64EndOfCentralDirectoryRecord(this.zipModel.getZip64EndOfCentralDirectoryLocator().getOffsetZip64EndOfCentralDirectoryRecord() - j);
        }
    }

    /* access modifiers changed from: protected */
    public ProgressMonitor.Task getTask() {
        return ProgressMonitor.Task.REMOVE_ENTRY;
    }

    /* access modifiers changed from: protected */
    public long calculateTotalWork(RemoveFilesFromZipTaskParameters removeFilesFromZipTaskParameters) {
        return this.zipModel.getZipFile().length();
    }

    /* access modifiers changed from: protected */
    public void executeTask(RemoveFilesFromZipTaskParameters removeFilesFromZipTaskParameters, ProgressMonitor progressMonitor) throws IOException {
        Throwable th;
        List<FileHeader> list;
        RemoveFilesFromZipTaskParameters removeFilesFromZipTaskParameters2 = removeFilesFromZipTaskParameters;
        if (!this.zipModel.isSplitArchive()) {
            List<String> filterNonExistingEntries = filterNonExistingEntries(removeFilesFromZipTaskParameters.filesToRemove);
            if (!filterNonExistingEntries.isEmpty()) {
                File temporaryFile = getTemporaryFile(this.zipModel.getZipFile().getPath());
                boolean z = false;
                try {
                    SplitOutputStream splitOutputStream = new SplitOutputStream(temporaryFile);
                    try {
                        RandomAccessFile randomAccessFile = new RandomAccessFile(this.zipModel.getZipFile(), RandomAccessFileMode.READ.getValue());
                        try {
                            List<FileHeader> cloneAndSortFileHeadersByOffset = cloneAndSortFileHeadersByOffset(this.zipModel.getCentralDirectory().getFileHeaders());
                            long j = 0;
                            for (FileHeader next : cloneAndSortFileHeadersByOffset) {
                                long offsetOfNextEntry = getOffsetOfNextEntry(cloneAndSortFileHeadersByOffset, next, this.zipModel) - splitOutputStream.getFilePointer();
                                if (shouldEntryBeRemoved(next, filterNonExistingEntries)) {
                                    updateHeaders(cloneAndSortFileHeadersByOffset, next, offsetOfNextEntry);
                                    if (this.zipModel.getCentralDirectory().getFileHeaders().remove(next)) {
                                        j += offsetOfNextEntry;
                                        list = cloneAndSortFileHeadersByOffset;
                                    } else {
                                        throw new ZipException("Could not remove entry from list of central directory headers");
                                    }
                                } else {
                                    list = cloneAndSortFileHeadersByOffset;
                                    j += super.copyFile(randomAccessFile, splitOutputStream, j, offsetOfNextEntry, progressMonitor, removeFilesFromZipTaskParameters2.zip4jConfig.getBufferSize());
                                }
                                verifyIfTaskIsCancelled();
                                cloneAndSortFileHeadersByOffset = list;
                            }
                            this.headerWriter.finalizeZipFile(this.zipModel, splitOutputStream, removeFilesFromZipTaskParameters2.zip4jConfig.getCharset());
                            z = true;
                            randomAccessFile.close();
                            splitOutputStream.close();
                            cleanupFile(true, this.zipModel.getZipFile(), temporaryFile);
                        } catch (Throwable th2) {
                            Throwable th3 = th2;
                            randomAccessFile.close();
                            throw th3;
                        }
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    } finally {
                        th = th4;
                        try {
                        } catch (Throwable th5) {
                            Throwable th6 = th5;
                            splitOutputStream.close();
                            throw th6;
                        }
                    }
                } catch (Throwable th7) {
                    cleanupFile(z, this.zipModel.getZipFile(), temporaryFile);
                    throw th7;
                }
            }
        } else {
            throw new ZipException("This is a split archive. Zip file format does not allow updating split/spanned files");
        }
    }
}
