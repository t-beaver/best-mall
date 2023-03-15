package net.lingala.zip4j.tasks;

import net.lingala.zip4j.model.Zip4jConfig;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.tasks.AsyncZipTask;

public class SetCommentTask extends AsyncZipTask<SetCommentTaskTaskParameters> {
    private final ZipModel zipModel;

    public static class SetCommentTaskTaskParameters extends AbstractZipTaskParameters {
        /* access modifiers changed from: private */
        public String comment;

        public SetCommentTaskTaskParameters(String str, Zip4jConfig zip4jConfig) {
            super(zip4jConfig);
            this.comment = str;
        }
    }

    public SetCommentTask(ZipModel zipModel2, AsyncZipTask.AsyncTaskParameters asyncTaskParameters) {
        super(asyncTaskParameters);
        this.zipModel = zipModel2;
    }

    /* access modifiers changed from: protected */
    public long calculateTotalWork(SetCommentTaskTaskParameters setCommentTaskTaskParameters) {
        return 0;
    }

    /* access modifiers changed from: protected */
    public ProgressMonitor.Task getTask() {
        return ProgressMonitor.Task.SET_COMMENT;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0051, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0056, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0057, code lost:
        r4.addSuppressed(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x005a, code lost:
        throw r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void executeTask(net.lingala.zip4j.tasks.SetCommentTask.SetCommentTaskTaskParameters r4, net.lingala.zip4j.progress.ProgressMonitor r5) throws java.io.IOException {
        /*
            r3 = this;
            java.lang.String r5 = r4.comment
            if (r5 == 0) goto L_0x005b
            net.lingala.zip4j.model.ZipModel r5 = r3.zipModel
            net.lingala.zip4j.model.EndOfCentralDirectoryRecord r5 = r5.getEndOfCentralDirectoryRecord()
            java.lang.String r0 = r4.comment
            r5.setComment(r0)
            net.lingala.zip4j.io.outputstream.SplitOutputStream r0 = new net.lingala.zip4j.io.outputstream.SplitOutputStream
            net.lingala.zip4j.model.ZipModel r1 = r3.zipModel
            java.io.File r1 = r1.getZipFile()
            r0.<init>(r1)
            net.lingala.zip4j.model.ZipModel r1 = r3.zipModel     // Catch:{ all -> 0x004f }
            boolean r1 = r1.isZip64Format()     // Catch:{ all -> 0x004f }
            if (r1 == 0) goto L_0x0034
            net.lingala.zip4j.model.ZipModel r5 = r3.zipModel     // Catch:{ all -> 0x004f }
            net.lingala.zip4j.model.Zip64EndOfCentralDirectoryRecord r5 = r5.getZip64EndOfCentralDirectoryRecord()     // Catch:{ all -> 0x004f }
            long r1 = r5.getOffsetStartCentralDirectoryWRTStartDiskNumber()     // Catch:{ all -> 0x004f }
            r0.seek(r1)     // Catch:{ all -> 0x004f }
            goto L_0x003b
        L_0x0034:
            long r1 = r5.getOffsetOfStartOfCentralDirectory()     // Catch:{ all -> 0x004f }
            r0.seek(r1)     // Catch:{ all -> 0x004f }
        L_0x003b:
            net.lingala.zip4j.headers.HeaderWriter r5 = new net.lingala.zip4j.headers.HeaderWriter     // Catch:{ all -> 0x004f }
            r5.<init>()     // Catch:{ all -> 0x004f }
            net.lingala.zip4j.model.ZipModel r1 = r3.zipModel     // Catch:{ all -> 0x004f }
            net.lingala.zip4j.model.Zip4jConfig r4 = r4.zip4jConfig     // Catch:{ all -> 0x004f }
            java.nio.charset.Charset r4 = r4.getCharset()     // Catch:{ all -> 0x004f }
            r5.finalizeZipFileWithoutValidations(r1, r0, r4)     // Catch:{ all -> 0x004f }
            r0.close()
            return
        L_0x004f:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x0051 }
        L_0x0051:
            r5 = move-exception
            r0.close()     // Catch:{ all -> 0x0056 }
            goto L_0x005a
        L_0x0056:
            r0 = move-exception
            r4.addSuppressed(r0)
        L_0x005a:
            throw r5
        L_0x005b:
            net.lingala.zip4j.exception.ZipException r4 = new net.lingala.zip4j.exception.ZipException
            java.lang.String r5 = "comment is null, cannot update Zip file with comment"
            r4.<init>((java.lang.String) r5)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.tasks.SetCommentTask.executeTask(net.lingala.zip4j.tasks.SetCommentTask$SetCommentTaskTaskParameters, net.lingala.zip4j.progress.ProgressMonitor):void");
    }
}
