package net.lingala.zip4j.tasks;

import java.io.IOException;
import java.io.InputStream;
import net.lingala.zip4j.headers.HeaderUtil;
import net.lingala.zip4j.io.inputstream.SplitInputStream;
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.UnzipParameters;
import net.lingala.zip4j.model.Zip4jConfig;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.tasks.AsyncZipTask;
import net.lingala.zip4j.util.UnzipUtil;

public class ExtractAllFilesTask extends AbstractExtractFileTask<ExtractAllFilesTaskParameters> {
    private final char[] password;
    private SplitInputStream splitInputStream;

    public static class ExtractAllFilesTaskParameters extends AbstractZipTaskParameters {
        /* access modifiers changed from: private */
        public final String outputPath;

        public ExtractAllFilesTaskParameters(String str, Zip4jConfig zip4jConfig) {
            super(zip4jConfig);
            this.outputPath = str;
        }
    }

    public ExtractAllFilesTask(ZipModel zipModel, char[] cArr, UnzipParameters unzipParameters, AsyncZipTask.AsyncTaskParameters asyncTaskParameters) {
        super(zipModel, unzipParameters, asyncTaskParameters);
        this.password = cArr;
    }

    private FileHeader getFirstFileHeader(ZipModel zipModel) {
        if (zipModel.getCentralDirectory() == null || zipModel.getCentralDirectory().getFileHeaders() == null || zipModel.getCentralDirectory().getFileHeaders().size() == 0) {
            return null;
        }
        return zipModel.getCentralDirectory().getFileHeaders().get(0);
    }

    private ZipInputStream prepareZipInputStream(Zip4jConfig zip4jConfig) throws IOException {
        this.splitInputStream = UnzipUtil.createSplitInputStream(getZipModel());
        FileHeader firstFileHeader = getFirstFileHeader(getZipModel());
        if (firstFileHeader != null) {
            this.splitInputStream.prepareExtractionForFileHeader(firstFileHeader);
        }
        return new ZipInputStream((InputStream) this.splitInputStream, this.password, zip4jConfig);
    }

    /* access modifiers changed from: protected */
    public long calculateTotalWork(ExtractAllFilesTaskParameters extractAllFilesTaskParameters) {
        return HeaderUtil.getTotalUncompressedSizeOfAllFileHeaders(getZipModel().getCentralDirectory().getFileHeaders());
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0062, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0063, code lost:
        if (r0 != null) goto L_0x0065;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x006d, code lost:
        throw r11;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void executeTask(net.lingala.zip4j.tasks.ExtractAllFilesTask.ExtractAllFilesTaskParameters r10, net.lingala.zip4j.progress.ProgressMonitor r11) throws java.io.IOException {
        /*
            r9 = this;
            net.lingala.zip4j.model.Zip4jConfig r0 = r10.zip4jConfig     // Catch:{ all -> 0x006e }
            net.lingala.zip4j.io.inputstream.ZipInputStream r0 = r9.prepareZipInputStream(r0)     // Catch:{ all -> 0x006e }
            net.lingala.zip4j.model.ZipModel r1 = r9.getZipModel()     // Catch:{ all -> 0x0060 }
            net.lingala.zip4j.model.CentralDirectory r1 = r1.getCentralDirectory()     // Catch:{ all -> 0x0060 }
            java.util.List r1 = r1.getFileHeaders()     // Catch:{ all -> 0x0060 }
            java.util.Iterator r8 = r1.iterator()     // Catch:{ all -> 0x0060 }
        L_0x0016:
            boolean r1 = r8.hasNext()     // Catch:{ all -> 0x0060 }
            if (r1 == 0) goto L_0x0053
            java.lang.Object r1 = r8.next()     // Catch:{ all -> 0x0060 }
            r3 = r1
            net.lingala.zip4j.model.FileHeader r3 = (net.lingala.zip4j.model.FileHeader) r3     // Catch:{ all -> 0x0060 }
            java.lang.String r1 = r3.getFileName()     // Catch:{ all -> 0x0060 }
            java.lang.String r2 = "__MACOSX"
            boolean r1 = r1.startsWith(r2)     // Catch:{ all -> 0x0060 }
            if (r1 == 0) goto L_0x0037
            long r1 = r3.getUncompressedSize()     // Catch:{ all -> 0x0060 }
            r11.updateWorkCompleted(r1)     // Catch:{ all -> 0x0060 }
            goto L_0x0016
        L_0x0037:
            net.lingala.zip4j.io.inputstream.SplitInputStream r1 = r9.splitInputStream     // Catch:{ all -> 0x0060 }
            r1.prepareExtractionForFileHeader(r3)     // Catch:{ all -> 0x0060 }
            net.lingala.zip4j.model.Zip4jConfig r1 = r10.zip4jConfig     // Catch:{ all -> 0x0060 }
            int r1 = r1.getBufferSize()     // Catch:{ all -> 0x0060 }
            byte[] r7 = new byte[r1]     // Catch:{ all -> 0x0060 }
            java.lang.String r4 = r10.outputPath     // Catch:{ all -> 0x0060 }
            r5 = 0
            r1 = r9
            r2 = r0
            r6 = r11
            r1.extractFile(r2, r3, r4, r5, r6, r7)     // Catch:{ all -> 0x0060 }
            r9.verifyIfTaskIsCancelled()     // Catch:{ all -> 0x0060 }
            goto L_0x0016
        L_0x0053:
            if (r0 == 0) goto L_0x0058
            r0.close()     // Catch:{ all -> 0x006e }
        L_0x0058:
            net.lingala.zip4j.io.inputstream.SplitInputStream r10 = r9.splitInputStream
            if (r10 == 0) goto L_0x005f
            r10.close()
        L_0x005f:
            return
        L_0x0060:
            r10 = move-exception
            throw r10     // Catch:{ all -> 0x0062 }
        L_0x0062:
            r11 = move-exception
            if (r0 == 0) goto L_0x006d
            r0.close()     // Catch:{ all -> 0x0069 }
            goto L_0x006d
        L_0x0069:
            r0 = move-exception
            r10.addSuppressed(r0)     // Catch:{ all -> 0x006e }
        L_0x006d:
            throw r11     // Catch:{ all -> 0x006e }
        L_0x006e:
            r10 = move-exception
            net.lingala.zip4j.io.inputstream.SplitInputStream r11 = r9.splitInputStream
            if (r11 == 0) goto L_0x0076
            r11.close()
        L_0x0076:
            goto L_0x0078
        L_0x0077:
            throw r10
        L_0x0078:
            goto L_0x0077
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.tasks.ExtractAllFilesTask.executeTask(net.lingala.zip4j.tasks.ExtractAllFilesTask$ExtractAllFilesTaskParameters, net.lingala.zip4j.progress.ProgressMonitor):void");
    }
}
