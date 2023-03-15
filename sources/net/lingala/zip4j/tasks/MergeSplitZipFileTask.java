package net.lingala.zip4j.tasks;

import com.taobao.weex.el.parse.Operators;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.List;
import net.lingala.zip4j.headers.HeaderWriter;
import net.lingala.zip4j.model.EndOfCentralDirectoryRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.Zip4jConfig;
import net.lingala.zip4j.model.Zip64EndOfCentralDirectoryLocator;
import net.lingala.zip4j.model.Zip64EndOfCentralDirectoryRecord;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.model.enums.RandomAccessFileMode;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.tasks.AsyncZipTask;
import net.lingala.zip4j.util.RawIO;

public class MergeSplitZipFileTask extends AsyncZipTask<MergeSplitZipFileTaskParameters> {
    private final RawIO rawIO = new RawIO();
    private final ZipModel zipModel;

    public static class MergeSplitZipFileTaskParameters extends AbstractZipTaskParameters {
        /* access modifiers changed from: private */
        public File outputZipFile;

        public MergeSplitZipFileTaskParameters(File file, Zip4jConfig zip4jConfig) {
            super(zip4jConfig);
            this.outputZipFile = file;
        }
    }

    public MergeSplitZipFileTask(ZipModel zipModel2, AsyncZipTask.AsyncTaskParameters asyncTaskParameters) {
        super(asyncTaskParameters);
        this.zipModel = zipModel2;
    }

    private RandomAccessFile createSplitZipFileStream(ZipModel zipModel2, int i) throws FileNotFoundException {
        return new RandomAccessFile(getNextSplitZipFile(zipModel2, i), RandomAccessFileMode.READ.getValue());
    }

    private File getNextSplitZipFile(ZipModel zipModel2, int i) {
        if (i == zipModel2.getEndOfCentralDirectoryRecord().getNumberOfThisDisk()) {
            return zipModel2.getZipFile();
        }
        String str = i >= 9 ? ".z" : ".z0";
        String path = zipModel2.getZipFile().getPath();
        return new File(zipModel2.getZipFile().getPath().substring(0, path.lastIndexOf(Operators.DOT_STR)) + str + (i + 1));
    }

    private void updateFileHeaderOffsetsForIndex(List<FileHeader> list, long j, int i, int i2) {
        for (FileHeader next : list) {
            if (next.getDiskNumberStart() == i) {
                next.setOffsetLocalHeader((next.getOffsetLocalHeader() + j) - ((long) i2));
                next.setDiskNumberStart(0);
            }
        }
    }

    private void updateHeadersForMergeSplitFileAction(ZipModel zipModel2, long j, OutputStream outputStream, Charset charset) throws IOException, CloneNotSupportedException {
        ZipModel zipModel3 = (ZipModel) zipModel2.clone();
        zipModel3.getEndOfCentralDirectoryRecord().setOffsetOfStartOfCentralDirectory(j);
        updateSplitZipModel(zipModel3, j);
        new HeaderWriter().finalizeZipFileWithoutValidations(zipModel3, outputStream, charset);
    }

    private void updateSplitEndCentralDirectory(ZipModel zipModel2) {
        int size = zipModel2.getCentralDirectory().getFileHeaders().size();
        EndOfCentralDirectoryRecord endOfCentralDirectoryRecord = zipModel2.getEndOfCentralDirectoryRecord();
        endOfCentralDirectoryRecord.setNumberOfThisDisk(0);
        endOfCentralDirectoryRecord.setNumberOfThisDiskStartOfCentralDir(0);
        endOfCentralDirectoryRecord.setTotalNumberOfEntriesInCentralDirectory(size);
        endOfCentralDirectoryRecord.setTotalNumberOfEntriesInCentralDirectoryOnThisDisk(size);
    }

    private void updateSplitZip64EndCentralDirLocator(ZipModel zipModel2, long j) {
        if (zipModel2.getZip64EndOfCentralDirectoryLocator() != null) {
            Zip64EndOfCentralDirectoryLocator zip64EndOfCentralDirectoryLocator = zipModel2.getZip64EndOfCentralDirectoryLocator();
            zip64EndOfCentralDirectoryLocator.setNumberOfDiskStartOfZip64EndOfCentralDirectoryRecord(0);
            zip64EndOfCentralDirectoryLocator.setOffsetZip64EndOfCentralDirectoryRecord(zip64EndOfCentralDirectoryLocator.getOffsetZip64EndOfCentralDirectoryRecord() + j);
            zip64EndOfCentralDirectoryLocator.setTotalNumberOfDiscs(1);
        }
    }

    private void updateSplitZip64EndCentralDirRec(ZipModel zipModel2, long j) {
        if (zipModel2.getZip64EndOfCentralDirectoryRecord() != null) {
            Zip64EndOfCentralDirectoryRecord zip64EndOfCentralDirectoryRecord = zipModel2.getZip64EndOfCentralDirectoryRecord();
            zip64EndOfCentralDirectoryRecord.setNumberOfThisDisk(0);
            zip64EndOfCentralDirectoryRecord.setNumberOfThisDiskStartOfCentralDirectory(0);
            zip64EndOfCentralDirectoryRecord.setTotalNumberOfEntriesInCentralDirectoryOnThisDisk((long) zipModel2.getEndOfCentralDirectoryRecord().getTotalNumberOfEntriesInCentralDirectory());
            zip64EndOfCentralDirectoryRecord.setOffsetStartCentralDirectoryWRTStartDiskNumber(zip64EndOfCentralDirectoryRecord.getOffsetStartCentralDirectoryWRTStartDiskNumber() + j);
        }
    }

    private void updateSplitZipModel(ZipModel zipModel2, long j) {
        zipModel2.setSplitArchive(false);
        updateSplitEndCentralDirectory(zipModel2);
        if (zipModel2.isZip64Format()) {
            updateSplitZip64EndCentralDirLocator(zipModel2, j);
            updateSplitZip64EndCentralDirRec(zipModel2, j);
        }
    }

    /* access modifiers changed from: protected */
    public ProgressMonitor.Task getTask() {
        return ProgressMonitor.Task.MERGE_ZIP_FILES;
    }

    /* access modifiers changed from: protected */
    public long calculateTotalWork(MergeSplitZipFileTaskParameters mergeSplitZipFileTaskParameters) {
        long j = 0;
        if (!this.zipModel.isSplitArchive()) {
            return 0;
        }
        for (int i = 0; i <= this.zipModel.getEndOfCentralDirectoryRecord().getNumberOfThisDisk(); i++) {
            j += getNextSplitZipFile(this.zipModel, i).length();
        }
        return j;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x005c A[Catch:{ all -> 0x0050 }] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x008e  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0090  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void executeTask(net.lingala.zip4j.tasks.MergeSplitZipFileTask.MergeSplitZipFileTaskParameters r26, net.lingala.zip4j.progress.ProgressMonitor r27) throws java.io.IOException {
        /*
            r25 = this;
            r7 = r25
            r0 = r26
            net.lingala.zip4j.model.ZipModel r1 = r7.zipModel
            boolean r1 = r1.isSplitArchive()
            if (r1 == 0) goto L_0x0102
            java.io.FileOutputStream r6 = new java.io.FileOutputStream     // Catch:{ CloneNotSupportedException -> 0x00fb }
            java.io.File r1 = r26.outputZipFile     // Catch:{ CloneNotSupportedException -> 0x00fb }
            r6.<init>(r1)     // Catch:{ CloneNotSupportedException -> 0x00fb }
            net.lingala.zip4j.model.ZipModel r1 = r7.zipModel     // Catch:{ all -> 0x00eb }
            net.lingala.zip4j.model.EndOfCentralDirectoryRecord r1 = r1.getEndOfCentralDirectoryRecord()     // Catch:{ all -> 0x00eb }
            int r5 = r1.getNumberOfThisDisk()     // Catch:{ all -> 0x00eb }
            if (r5 <= 0) goto L_0x00e0
            r16 = 0
            r3 = 0
            r17 = r3
            r1 = 0
            r2 = 0
        L_0x0029:
            if (r2 > r5) goto L_0x00c8
            net.lingala.zip4j.model.ZipModel r8 = r7.zipModel     // Catch:{ all -> 0x00eb }
            java.io.RandomAccessFile r15 = r7.createSplitZipFileStream(r8, r2)     // Catch:{ all -> 0x00eb }
            long r8 = r15.length()     // Catch:{ all -> 0x00b4 }
            r10 = 4
            if (r2 != 0) goto L_0x0057
            net.lingala.zip4j.util.RawIO r11 = r7.rawIO     // Catch:{ all -> 0x0050 }
            int r11 = r11.readIntLittleEndian((java.io.RandomAccessFile) r15)     // Catch:{ all -> 0x0050 }
            long r11 = (long) r11     // Catch:{ all -> 0x0050 }
            net.lingala.zip4j.headers.HeaderSignature r13 = net.lingala.zip4j.headers.HeaderSignature.SPLIT_ZIP     // Catch:{ all -> 0x0050 }
            long r13 = r13.getValue()     // Catch:{ all -> 0x0050 }
            int r19 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1))
            if (r19 != 0) goto L_0x004c
            r19 = 4
            goto L_0x005a
        L_0x004c:
            r15.seek(r3)     // Catch:{ all -> 0x0050 }
            goto L_0x0057
        L_0x0050:
            r0 = move-exception
            r1 = r0
            r9 = r6
            r24 = r15
            goto L_0x00b9
        L_0x0057:
            r19 = r1
            r10 = 0
        L_0x005a:
            if (r2 != r5) goto L_0x0066
            net.lingala.zip4j.model.ZipModel r1 = r7.zipModel     // Catch:{ all -> 0x0050 }
            net.lingala.zip4j.model.EndOfCentralDirectoryRecord r1 = r1.getEndOfCentralDirectoryRecord()     // Catch:{ all -> 0x0050 }
            long r8 = r1.getOffsetOfStartOfCentralDirectory()     // Catch:{ all -> 0x0050 }
        L_0x0066:
            r20 = r8
            long r12 = (long) r10
            net.lingala.zip4j.model.Zip4jConfig r1 = r0.zip4jConfig     // Catch:{ all -> 0x00b4 }
            int r1 = r1.getBufferSize()     // Catch:{ all -> 0x00b4 }
            r8 = r15
            r9 = r6
            r10 = r12
            r22 = r12
            r12 = r20
            r14 = r27
            r24 = r15
            r15 = r1
            net.lingala.zip4j.util.FileUtils.copyFile(r8, r9, r10, r12, r14, r15)     // Catch:{ all -> 0x00b1 }
            long r20 = r20 - r22
            long r17 = r17 + r20
            net.lingala.zip4j.model.ZipModel r1 = r7.zipModel     // Catch:{ all -> 0x00b1 }
            net.lingala.zip4j.model.CentralDirectory r1 = r1.getCentralDirectory()     // Catch:{ all -> 0x00b1 }
            java.util.List r8 = r1.getFileHeaders()     // Catch:{ all -> 0x00b1 }
            if (r2 != 0) goto L_0x0090
            r9 = r3
            goto L_0x0092
        L_0x0090:
            r9 = r17
        L_0x0092:
            r1 = r25
            r11 = r2
            r2 = r8
            r12 = r3
            r3 = r9
            r8 = r5
            r5 = r11
            r9 = r6
            r6 = r19
            r1.updateFileHeaderOffsetsForIndex(r2, r3, r5, r6)     // Catch:{ all -> 0x00af }
            r25.verifyIfTaskIsCancelled()     // Catch:{ all -> 0x00af }
            r24.close()     // Catch:{ all -> 0x00e9 }
            int r2 = r11 + 1
            r5 = r8
            r6 = r9
            r3 = r12
            r1 = r19
            goto L_0x0029
        L_0x00af:
            r0 = move-exception
            goto L_0x00b8
        L_0x00b1:
            r0 = move-exception
            r9 = r6
            goto L_0x00b8
        L_0x00b4:
            r0 = move-exception
            r9 = r6
            r24 = r15
        L_0x00b8:
            r1 = r0
        L_0x00b9:
            throw r1     // Catch:{ all -> 0x00ba }
        L_0x00ba:
            r0 = move-exception
            r2 = r0
            if (r24 == 0) goto L_0x00c7
            r24.close()     // Catch:{ all -> 0x00c2 }
            goto L_0x00c7
        L_0x00c2:
            r0 = move-exception
            r3 = r0
            r1.addSuppressed(r3)     // Catch:{ all -> 0x00e9 }
        L_0x00c7:
            throw r2     // Catch:{ all -> 0x00e9 }
        L_0x00c8:
            r9 = r6
            net.lingala.zip4j.model.ZipModel r2 = r7.zipModel     // Catch:{ all -> 0x00e9 }
            net.lingala.zip4j.model.Zip4jConfig r0 = r0.zip4jConfig     // Catch:{ all -> 0x00e9 }
            java.nio.charset.Charset r6 = r0.getCharset()     // Catch:{ all -> 0x00e9 }
            r1 = r25
            r3 = r17
            r5 = r9
            r1.updateHeadersForMergeSplitFileAction(r2, r3, r5, r6)     // Catch:{ all -> 0x00e9 }
            r27.endProgressMonitor()     // Catch:{ all -> 0x00e9 }
            r9.close()     // Catch:{ CloneNotSupportedException -> 0x00fb }
            return
        L_0x00e0:
            r9 = r6
            net.lingala.zip4j.exception.ZipException r0 = new net.lingala.zip4j.exception.ZipException     // Catch:{ all -> 0x00e9 }
            java.lang.String r1 = "zip archive not a split zip file"
            r0.<init>((java.lang.String) r1)     // Catch:{ all -> 0x00e9 }
            throw r0     // Catch:{ all -> 0x00e9 }
        L_0x00e9:
            r0 = move-exception
            goto L_0x00ed
        L_0x00eb:
            r0 = move-exception
            r9 = r6
        L_0x00ed:
            r1 = r0
            throw r1     // Catch:{ all -> 0x00ef }
        L_0x00ef:
            r0 = move-exception
            r2 = r0
            r9.close()     // Catch:{ all -> 0x00f5 }
            goto L_0x00fa
        L_0x00f5:
            r0 = move-exception
            r3 = r0
            r1.addSuppressed(r3)     // Catch:{ CloneNotSupportedException -> 0x00fb }
        L_0x00fa:
            throw r2     // Catch:{ CloneNotSupportedException -> 0x00fb }
        L_0x00fb:
            r0 = move-exception
            net.lingala.zip4j.exception.ZipException r1 = new net.lingala.zip4j.exception.ZipException
            r1.<init>((java.lang.Exception) r0)
            throw r1
        L_0x0102:
            net.lingala.zip4j.exception.ZipException r0 = new net.lingala.zip4j.exception.ZipException
            java.lang.String r1 = "archive not a split zip file"
            r0.<init>((java.lang.String) r1)
            r1 = r27
            r1.endProgressMonitor(r0)
            goto L_0x0110
        L_0x010f:
            throw r0
        L_0x0110:
            goto L_0x010f
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.tasks.MergeSplitZipFileTask.executeTask(net.lingala.zip4j.tasks.MergeSplitZipFileTask$MergeSplitZipFileTaskParameters, net.lingala.zip4j.progress.ProgressMonitor):void");
    }
}
