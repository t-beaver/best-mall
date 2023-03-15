package net.lingala.zip4j.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import net.lingala.zip4j.headers.HeaderUtil;
import net.lingala.zip4j.io.inputstream.SplitInputStream;
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.UnzipParameters;
import net.lingala.zip4j.model.Zip4jConfig;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.tasks.AsyncZipTask;
import net.lingala.zip4j.util.UnzipUtil;
import net.lingala.zip4j.util.Zip4jUtil;

public class ExtractFileTask extends AbstractExtractFileTask<ExtractFileTaskParameters> {
    private char[] password;
    private SplitInputStream splitInputStream;

    public static class ExtractFileTaskParameters extends AbstractZipTaskParameters {
        /* access modifiers changed from: private */
        public FileHeader fileHeader;
        /* access modifiers changed from: private */
        public String newFileName;
        /* access modifiers changed from: private */
        public String outputPath;

        public ExtractFileTaskParameters(String str, FileHeader fileHeader2, String str2, Zip4jConfig zip4jConfig) {
            super(zip4jConfig);
            this.outputPath = str;
            this.fileHeader = fileHeader2;
            this.newFileName = str2;
        }
    }

    public ExtractFileTask(ZipModel zipModel, char[] cArr, UnzipParameters unzipParameters, AsyncZipTask.AsyncTaskParameters asyncTaskParameters) {
        super(zipModel, unzipParameters, asyncTaskParameters);
        this.password = cArr;
    }

    private ZipInputStream createZipInputStream(FileHeader fileHeader, Zip4jConfig zip4jConfig) throws IOException {
        SplitInputStream createSplitInputStream = UnzipUtil.createSplitInputStream(getZipModel());
        this.splitInputStream = createSplitInputStream;
        createSplitInputStream.prepareExtractionForFileHeader(fileHeader);
        return new ZipInputStream((InputStream) this.splitInputStream, this.password, zip4jConfig);
    }

    private String determineNewFileName(String str, FileHeader fileHeader, FileHeader fileHeader2) {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(str) || !fileHeader.isDirectory()) {
            return str;
        }
        String str2 = "/";
        if (str.endsWith(str2)) {
            str2 = "";
        }
        String fileName = fileHeader2.getFileName();
        String fileName2 = fileHeader.getFileName();
        return fileName.replaceFirst(fileName2, str + str2);
    }

    private List<FileHeader> getFileHeadersToExtract(FileHeader fileHeader) {
        if (!fileHeader.isDirectory()) {
            return Collections.singletonList(fileHeader);
        }
        return HeaderUtil.getFileHeadersUnderDirectory(getZipModel().getCentralDirectory().getFileHeaders(), fileHeader);
    }

    /* access modifiers changed from: protected */
    public long calculateTotalWork(ExtractFileTaskParameters extractFileTaskParameters) {
        return HeaderUtil.getTotalUncompressedSizeOfAllFileHeaders(getFileHeadersToExtract(extractFileTaskParameters.fileHeader));
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0052, code lost:
        r12 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0053, code lost:
        if (r0 != null) goto L_0x0055;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x005d, code lost:
        throw r12;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void executeTask(net.lingala.zip4j.tasks.ExtractFileTask.ExtractFileTaskParameters r11, net.lingala.zip4j.progress.ProgressMonitor r12) throws java.io.IOException {
        /*
            r10 = this;
            net.lingala.zip4j.model.FileHeader r0 = r11.fileHeader     // Catch:{ all -> 0x005e }
            net.lingala.zip4j.model.Zip4jConfig r1 = r11.zip4jConfig     // Catch:{ all -> 0x005e }
            net.lingala.zip4j.io.inputstream.ZipInputStream r0 = r10.createZipInputStream(r0, r1)     // Catch:{ all -> 0x005e }
            net.lingala.zip4j.model.FileHeader r1 = r11.fileHeader     // Catch:{ all -> 0x0050 }
            java.util.List r1 = r10.getFileHeadersToExtract(r1)     // Catch:{ all -> 0x0050 }
            net.lingala.zip4j.model.Zip4jConfig r2 = r11.zip4jConfig     // Catch:{ all -> 0x0050 }
            int r2 = r2.getBufferSize()     // Catch:{ all -> 0x0050 }
            byte[] r9 = new byte[r2]     // Catch:{ all -> 0x0050 }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x0050 }
        L_0x001e:
            boolean r2 = r1.hasNext()     // Catch:{ all -> 0x0050 }
            if (r2 == 0) goto L_0x0043
            java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x0050 }
            r4 = r2
            net.lingala.zip4j.model.FileHeader r4 = (net.lingala.zip4j.model.FileHeader) r4     // Catch:{ all -> 0x0050 }
            java.lang.String r2 = r11.newFileName     // Catch:{ all -> 0x0050 }
            net.lingala.zip4j.model.FileHeader r3 = r11.fileHeader     // Catch:{ all -> 0x0050 }
            java.lang.String r6 = r10.determineNewFileName(r2, r3, r4)     // Catch:{ all -> 0x0050 }
            java.lang.String r5 = r11.outputPath     // Catch:{ all -> 0x0050 }
            r2 = r10
            r3 = r0
            r7 = r12
            r8 = r9
            r2.extractFile(r3, r4, r5, r6, r7, r8)     // Catch:{ all -> 0x0050 }
            goto L_0x001e
        L_0x0043:
            if (r0 == 0) goto L_0x0048
            r0.close()     // Catch:{ all -> 0x005e }
        L_0x0048:
            net.lingala.zip4j.io.inputstream.SplitInputStream r11 = r10.splitInputStream
            if (r11 == 0) goto L_0x004f
            r11.close()
        L_0x004f:
            return
        L_0x0050:
            r11 = move-exception
            throw r11     // Catch:{ all -> 0x0052 }
        L_0x0052:
            r12 = move-exception
            if (r0 == 0) goto L_0x005d
            r0.close()     // Catch:{ all -> 0x0059 }
            goto L_0x005d
        L_0x0059:
            r0 = move-exception
            r11.addSuppressed(r0)     // Catch:{ all -> 0x005e }
        L_0x005d:
            throw r12     // Catch:{ all -> 0x005e }
        L_0x005e:
            r11 = move-exception
            net.lingala.zip4j.io.inputstream.SplitInputStream r12 = r10.splitInputStream
            if (r12 == 0) goto L_0x0066
            r12.close()
        L_0x0066:
            goto L_0x0068
        L_0x0067:
            throw r11
        L_0x0068:
            goto L_0x0067
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.tasks.ExtractFileTask.executeTask(net.lingala.zip4j.tasks.ExtractFileTask$ExtractFileTaskParameters, net.lingala.zip4j.progress.ProgressMonitor):void");
    }
}
