package net.lingala.zip4j.util;

public class CrcUtil {
    private static final int BUF_SIZE = 16384;

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x004d, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0052, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0053, code lost:
        r5.addSuppressed(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0056, code lost:
        throw r6;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static long computeFileCrc(java.io.File r5, net.lingala.zip4j.progress.ProgressMonitor r6) throws java.io.IOException {
        /*
            if (r5 == 0) goto L_0x0057
            boolean r0 = r5.exists()
            if (r0 == 0) goto L_0x0057
            boolean r0 = r5.canRead()
            if (r0 == 0) goto L_0x0057
            r0 = 16384(0x4000, float:2.2959E-41)
            byte[] r0 = new byte[r0]
            java.util.zip.CRC32 r1 = new java.util.zip.CRC32
            r1.<init>()
            java.io.FileInputStream r2 = new java.io.FileInputStream
            r2.<init>(r5)
        L_0x001c:
            int r5 = r2.read(r0)     // Catch:{ all -> 0x004b }
            r3 = -1
            if (r5 == r3) goto L_0x0043
            r3 = 0
            r1.update(r0, r3, r5)     // Catch:{ all -> 0x004b }
            if (r6 == 0) goto L_0x001c
            long r3 = (long) r5     // Catch:{ all -> 0x004b }
            r6.updateWorkCompleted(r3)     // Catch:{ all -> 0x004b }
            boolean r5 = r6.isCancelAllTasks()     // Catch:{ all -> 0x004b }
            if (r5 == 0) goto L_0x001c
            net.lingala.zip4j.progress.ProgressMonitor$Result r5 = net.lingala.zip4j.progress.ProgressMonitor.Result.CANCELLED     // Catch:{ all -> 0x004b }
            r6.setResult(r5)     // Catch:{ all -> 0x004b }
            net.lingala.zip4j.progress.ProgressMonitor$State r5 = net.lingala.zip4j.progress.ProgressMonitor.State.READY     // Catch:{ all -> 0x004b }
            r6.setState(r5)     // Catch:{ all -> 0x004b }
            r5 = 0
            r2.close()
            return r5
        L_0x0043:
            long r5 = r1.getValue()     // Catch:{ all -> 0x004b }
            r2.close()
            return r5
        L_0x004b:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x004d }
        L_0x004d:
            r6 = move-exception
            r2.close()     // Catch:{ all -> 0x0052 }
            goto L_0x0056
        L_0x0052:
            r0 = move-exception
            r5.addSuppressed(r0)
        L_0x0056:
            throw r6
        L_0x0057:
            net.lingala.zip4j.exception.ZipException r5 = new net.lingala.zip4j.exception.ZipException
            java.lang.String r6 = "input file is null or does not exist or cannot read. Cannot calculate CRC for the file"
            r5.<init>((java.lang.String) r6)
            goto L_0x0060
        L_0x005f:
            throw r5
        L_0x0060:
            goto L_0x005f
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.util.CrcUtil.computeFileCrc(java.io.File, net.lingala.zip4j.progress.ProgressMonitor):long");
    }
}
