package com.facebook.soloader;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

public abstract class UnpackingSoSource extends DirectorySoSource {
    private static final String DEPS_FILE_NAME = "dso_deps";
    private static final String LOCK_FILE_NAME = "dso_lock";
    private static final String MANIFEST_FILE_NAME = "dso_manifest";
    private static final byte MANIFEST_VERSION = 1;
    private static final byte STATE_CLEAN = 1;
    private static final byte STATE_DIRTY = 0;
    private static final String STATE_FILE_NAME = "dso_state";
    private static final String TAG = "fb-UnpackingSoSource";
    @Nullable
    private String[] mAbis;
    protected final Context mContext;
    @Nullable
    protected String mCorruptedLib;
    private final Map<String, Object> mLibsBeingLoaded = new HashMap();

    /* access modifiers changed from: protected */
    public abstract Unpacker makeUnpacker() throws IOException;

    protected UnpackingSoSource(Context context, String str) {
        super(getSoStorePath(context, str), 1);
        this.mContext = context;
    }

    protected UnpackingSoSource(Context context, File file) {
        super(file, 1);
        this.mContext = context;
    }

    public static File getSoStorePath(Context context, String str) {
        return new File(context.getApplicationInfo().dataDir + "/" + str);
    }

    public String[] getSoSourceAbis() {
        String[] strArr = this.mAbis;
        return strArr == null ? super.getSoSourceAbis() : strArr;
    }

    public void setSoSourceAbis(String[] strArr) {
        this.mAbis = strArr;
    }

    public static class Dso {
        public final String hash;
        public final String name;

        public Dso(String str, String str2) {
            this.name = str;
            this.hash = str2;
        }
    }

    public static final class DsoManifest {
        public final Dso[] dsos;

        public DsoManifest(Dso[] dsoArr) {
            this.dsos = dsoArr;
        }

        static final DsoManifest read(DataInput dataInput) throws IOException {
            if (dataInput.readByte() == 1) {
                int readInt = dataInput.readInt();
                if (readInt >= 0) {
                    Dso[] dsoArr = new Dso[readInt];
                    for (int i = 0; i < readInt; i++) {
                        dsoArr[i] = new Dso(dataInput.readUTF(), dataInput.readUTF());
                    }
                    return new DsoManifest(dsoArr);
                }
                throw new RuntimeException("illegal number of shared libraries");
            }
            throw new RuntimeException("wrong dso manifest version");
        }

        public final void write(DataOutput dataOutput) throws IOException {
            dataOutput.writeByte(1);
            dataOutput.writeInt(this.dsos.length);
            int i = 0;
            while (true) {
                Dso[] dsoArr = this.dsos;
                if (i < dsoArr.length) {
                    dataOutput.writeUTF(dsoArr[i].name);
                    dataOutput.writeUTF(this.dsos[i].hash);
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    protected static final class InputDso implements Closeable {
        public final InputStream content;
        public final Dso dso;

        public InputDso(Dso dso2, InputStream inputStream) {
            this.dso = dso2;
            this.content = inputStream;
        }

        public void close() throws IOException {
            this.content.close();
        }
    }

    protected static abstract class InputDsoIterator implements Closeable {
        public void close() throws IOException {
        }

        public abstract boolean hasNext();

        public abstract InputDso next() throws IOException;

        protected InputDsoIterator() {
        }
    }

    protected static abstract class Unpacker implements Closeable {
        public void close() throws IOException {
        }

        /* access modifiers changed from: protected */
        public abstract DsoManifest getDsoManifest() throws IOException;

        /* access modifiers changed from: protected */
        public abstract InputDsoIterator openDsoIterator() throws IOException;

        protected Unpacker() {
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002a, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002b, code lost:
        r3.addSuppressed(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002f, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0032, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0023, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0024, code lost:
        if (r3 != null) goto L_0x0026;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void writeState(java.io.File r3, byte r4) throws java.io.IOException {
        /*
            java.io.RandomAccessFile r0 = new java.io.RandomAccessFile
            java.lang.String r1 = "rw"
            r0.<init>(r3, r1)
            r1 = 0
            r0.seek(r1)     // Catch:{ all -> 0x0021 }
            r0.write(r4)     // Catch:{ all -> 0x0021 }
            long r3 = r0.getFilePointer()     // Catch:{ all -> 0x0021 }
            r0.setLength(r3)     // Catch:{ all -> 0x0021 }
            java.io.FileDescriptor r3 = r0.getFD()     // Catch:{ all -> 0x0021 }
            r3.sync()     // Catch:{ all -> 0x0021 }
            r0.close()
            return
        L_0x0021:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x0023 }
        L_0x0023:
            r4 = move-exception
            if (r3 == 0) goto L_0x002f
            r0.close()     // Catch:{ all -> 0x002a }
            goto L_0x0032
        L_0x002a:
            r0 = move-exception
            r3.addSuppressed(r0)
            goto L_0x0032
        L_0x002f:
            r0.close()
        L_0x0032:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.UnpackingSoSource.writeState(java.io.File, byte):void");
    }

    private void deleteUnmentionedFiles(Dso[] dsoArr) throws IOException {
        String[] list = this.soDirectory.list();
        if (list != null) {
            for (String str : list) {
                if (!str.equals(STATE_FILE_NAME) && !str.equals(LOCK_FILE_NAME) && !str.equals(DEPS_FILE_NAME) && !str.equals(MANIFEST_FILE_NAME)) {
                    boolean z = false;
                    int i = 0;
                    while (!z && i < dsoArr.length) {
                        if (dsoArr[i].name.equals(str)) {
                            z = true;
                        }
                        i++;
                    }
                    if (!z) {
                        File file = new File(this.soDirectory, str);
                        Log.v(TAG, "deleting unaccounted-for file " + file);
                        SysUtil.dumbDeleteRecursive(file);
                    }
                }
            }
            return;
        }
        throw new IOException("unable to list directory " + this.soDirectory);
    }

    private void extractDso(InputDso inputDso, byte[] bArr) throws IOException {
        boolean writable;
        Log.i(TAG, "extracting DSO " + inputDso.dso.name);
        try {
            if (this.soDirectory.setWritable(true)) {
                extractDsoImpl(inputDso, bArr);
                if (writable) {
                    return;
                }
                return;
            }
            throw new IOException("cannot make directory writable for us: " + this.soDirectory);
        } finally {
            if (!this.soDirectory.setWritable(false)) {
                Log.w(TAG, "error removing " + this.soDirectory.getCanonicalPath() + " write permission");
            }
        }
    }

    private void extractDsoImpl(InputDso inputDso, byte[] bArr) throws IOException {
        RandomAccessFile randomAccessFile;
        File file = new File(this.soDirectory, inputDso.dso.name);
        RandomAccessFile randomAccessFile2 = null;
        try {
            if (file.exists() && !file.setWritable(true)) {
                Log.w(TAG, "error adding write permission to: " + file);
            }
            try {
                randomAccessFile = new RandomAccessFile(file, "rw");
            } catch (IOException e) {
                Log.w(TAG, "error overwriting " + file + " trying to delete and start over", e);
                SysUtil.dumbDeleteRecursive(file);
                randomAccessFile = new RandomAccessFile(file, "rw");
            }
            randomAccessFile2 = randomAccessFile;
            int available = inputDso.content.available();
            if (available > 1) {
                SysUtil.fallocateIfSupported(randomAccessFile2.getFD(), (long) available);
            }
            SysUtil.copyBytes(randomAccessFile2, inputDso.content, Integer.MAX_VALUE, bArr);
            randomAccessFile2.setLength(randomAccessFile2.getFilePointer());
            if (file.setExecutable(true, false)) {
                if (!file.setWritable(false)) {
                    Log.w(TAG, "error removing " + file + " write permission");
                }
                if (randomAccessFile2 != null) {
                    randomAccessFile2.close();
                    return;
                }
                return;
            }
            throw new IOException("cannot make file executable: " + file);
        } catch (IOException e2) {
            SysUtil.dumbDeleteRecursive(file);
            throw e2;
        } catch (Throwable th) {
            if (!file.setWritable(false)) {
                Log.w(TAG, "error removing " + file + " write permission");
            }
            if (randomAccessFile2 != null) {
                randomAccessFile2.close();
            }
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0043 A[Catch:{ all -> 0x009d, all -> 0x00cd }] */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x005a A[Catch:{ all -> 0x009d, all -> 0x00cd }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void regenerate(byte r10, com.facebook.soloader.UnpackingSoSource.DsoManifest r11, com.facebook.soloader.UnpackingSoSource.InputDsoIterator r12) throws java.io.IOException {
        /*
            r9 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "regenerating DSO store "
            r0.append(r1)
            java.lang.Class r1 = r9.getClass()
            java.lang.String r1 = r1.getName()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "fb-UnpackingSoSource"
            android.util.Log.v(r1, r0)
            java.io.File r0 = new java.io.File
            java.io.File r2 = r9.soDirectory
            java.lang.String r3 = "dso_manifest"
            r0.<init>(r2, r3)
            java.io.RandomAccessFile r2 = new java.io.RandomAccessFile
            java.lang.String r3 = "rw"
            r2.<init>(r0, r3)
            r0 = 1
            if (r10 != r0) goto L_0x003f
            com.facebook.soloader.UnpackingSoSource$DsoManifest r10 = com.facebook.soloader.UnpackingSoSource.DsoManifest.read(r2)     // Catch:{ Exception -> 0x0039 }
            goto L_0x0040
        L_0x0036:
            r10 = move-exception
            goto L_0x00cc
        L_0x0039:
            r10 = move-exception
            java.lang.String r3 = "error reading existing DSO manifest"
            android.util.Log.i(r1, r3, r10)     // Catch:{ all -> 0x0036 }
        L_0x003f:
            r10 = 0
        L_0x0040:
            r3 = 0
            if (r10 != 0) goto L_0x004a
            com.facebook.soloader.UnpackingSoSource$DsoManifest r10 = new com.facebook.soloader.UnpackingSoSource$DsoManifest     // Catch:{ all -> 0x0036 }
            com.facebook.soloader.UnpackingSoSource$Dso[] r4 = new com.facebook.soloader.UnpackingSoSource.Dso[r3]     // Catch:{ all -> 0x0036 }
            r10.<init>(r4)     // Catch:{ all -> 0x0036 }
        L_0x004a:
            com.facebook.soloader.UnpackingSoSource$Dso[] r11 = r11.dsos     // Catch:{ all -> 0x0036 }
            r9.deleteUnmentionedFiles(r11)     // Catch:{ all -> 0x0036 }
            r11 = 32768(0x8000, float:4.5918E-41)
            byte[] r11 = new byte[r11]     // Catch:{ all -> 0x0036 }
        L_0x0054:
            boolean r4 = r12.hasNext()     // Catch:{ all -> 0x0036 }
            if (r4 == 0) goto L_0x00ac
            com.facebook.soloader.UnpackingSoSource$InputDso r4 = r12.next()     // Catch:{ all -> 0x0036 }
            r5 = 1
            r6 = 0
        L_0x0060:
            if (r5 == 0) goto L_0x008d
            com.facebook.soloader.UnpackingSoSource$Dso[] r7 = r10.dsos     // Catch:{ all -> 0x008b }
            int r7 = r7.length     // Catch:{ all -> 0x008b }
            if (r6 >= r7) goto L_0x008d
            com.facebook.soloader.UnpackingSoSource$Dso[] r7 = r10.dsos     // Catch:{ all -> 0x008b }
            r7 = r7[r6]     // Catch:{ all -> 0x008b }
            java.lang.String r7 = r7.name     // Catch:{ all -> 0x008b }
            com.facebook.soloader.UnpackingSoSource$Dso r8 = r4.dso     // Catch:{ all -> 0x008b }
            java.lang.String r8 = r8.name     // Catch:{ all -> 0x008b }
            boolean r7 = r7.equals(r8)     // Catch:{ all -> 0x008b }
            if (r7 == 0) goto L_0x0088
            com.facebook.soloader.UnpackingSoSource$Dso[] r7 = r10.dsos     // Catch:{ all -> 0x008b }
            r7 = r7[r6]     // Catch:{ all -> 0x008b }
            java.lang.String r7 = r7.hash     // Catch:{ all -> 0x008b }
            com.facebook.soloader.UnpackingSoSource$Dso r8 = r4.dso     // Catch:{ all -> 0x008b }
            java.lang.String r8 = r8.hash     // Catch:{ all -> 0x008b }
            boolean r7 = r7.equals(r8)     // Catch:{ all -> 0x008b }
            if (r7 == 0) goto L_0x0088
            r5 = 0
        L_0x0088:
            int r6 = r6 + 1
            goto L_0x0060
        L_0x008b:
            r10 = move-exception
            goto L_0x0093
        L_0x008d:
            if (r5 == 0) goto L_0x00a6
            r9.extractDso(r4, r11)     // Catch:{ all -> 0x008b }
            goto L_0x00a6
        L_0x0093:
            throw r10     // Catch:{ all -> 0x0094 }
        L_0x0094:
            r11 = move-exception
            if (r4 == 0) goto L_0x00a5
            if (r10 == 0) goto L_0x00a2
            r4.close()     // Catch:{ all -> 0x009d }
            goto L_0x00a5
        L_0x009d:
            r12 = move-exception
            r10.addSuppressed(r12)     // Catch:{ all -> 0x0036 }
            goto L_0x00a5
        L_0x00a2:
            r4.close()     // Catch:{ all -> 0x0036 }
        L_0x00a5:
            throw r11     // Catch:{ all -> 0x0036 }
        L_0x00a6:
            if (r4 == 0) goto L_0x0054
            r4.close()     // Catch:{ all -> 0x0036 }
            goto L_0x0054
        L_0x00ac:
            r2.close()
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "Finished regenerating DSO store "
            r10.append(r11)
            java.lang.Class r11 = r9.getClass()
            java.lang.String r11 = r11.getName()
            r10.append(r11)
            java.lang.String r10 = r10.toString()
            android.util.Log.v(r1, r10)
            return
        L_0x00cc:
            throw r10     // Catch:{ all -> 0x00cd }
        L_0x00cd:
            r11 = move-exception
            if (r10 == 0) goto L_0x00d9
            r2.close()     // Catch:{ all -> 0x00d4 }
            goto L_0x00dc
        L_0x00d4:
            r12 = move-exception
            r10.addSuppressed(r12)
            goto L_0x00dc
        L_0x00d9:
            r2.close()
        L_0x00dc:
            goto L_0x00de
        L_0x00dd:
            throw r11
        L_0x00de:
            goto L_0x00dd
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.UnpackingSoSource.regenerate(byte, com.facebook.soloader.UnpackingSoSource$DsoManifest, com.facebook.soloader.UnpackingSoSource$InputDsoIterator):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0039, code lost:
        r13 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x003a, code lost:
        if (r12 != null) goto L_0x003c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0040, code lost:
        r14 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0041, code lost:
        r12.addSuppressed(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0045, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0048, code lost:
        throw r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00e0, code lost:
        r13 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00e1, code lost:
        if (r2 != null) goto L_0x00e3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00e7, code lost:
        r14 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:?, code lost:
        r12.addSuppressed(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00eb, code lost:
        throw r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x00ee, code lost:
        r13 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x00ef, code lost:
        if (r0 != null) goto L_0x00f1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x00f1, code lost:
        if (r12 != null) goto L_0x00f3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x00f7, code lost:
        r14 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:?, code lost:
        r12.addSuppressed(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x00fc, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x00ff, code lost:
        throw r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x0102, code lost:
        r13 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x0103, code lost:
        if (r12 != null) goto L_0x0105;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:?, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x0109, code lost:
        r14 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x010a, code lost:
        r12.addSuppressed(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x010e, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x0112, code lost:
        throw r13;
     */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00aa A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00ab  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean refreshLocked(com.facebook.soloader.FileLocker r12, int r13, byte[] r14) throws java.io.IOException {
        /*
            r11 = this;
            java.lang.String r0 = "fb-UnpackingSoSource"
            java.io.File r6 = new java.io.File
            java.io.File r1 = r11.soDirectory
            java.lang.String r2 = "dso_state"
            r6.<init>(r1, r2)
            java.io.RandomAccessFile r1 = new java.io.RandomAccessFile
            java.lang.String r2 = "rw"
            r1.<init>(r6, r2)
            r8 = 1
            r3 = 0
            byte r4 = r1.readByte()     // Catch:{ EOFException -> 0x0035, all -> 0x0037 }
            if (r4 == r8) goto L_0x0049
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ EOFException -> 0x0035, all -> 0x0037 }
            r4.<init>()     // Catch:{ EOFException -> 0x0035, all -> 0x0037 }
            java.lang.String r5 = "dso store "
            r4.append(r5)     // Catch:{ EOFException -> 0x0035, all -> 0x0037 }
            java.io.File r5 = r11.soDirectory     // Catch:{ EOFException -> 0x0035, all -> 0x0037 }
            r4.append(r5)     // Catch:{ EOFException -> 0x0035, all -> 0x0037 }
            java.lang.String r5 = " regeneration interrupted: wiping clean"
            r4.append(r5)     // Catch:{ EOFException -> 0x0035, all -> 0x0037 }
            java.lang.String r4 = r4.toString()     // Catch:{ EOFException -> 0x0035, all -> 0x0037 }
            android.util.Log.v(r0, r4)     // Catch:{ EOFException -> 0x0035, all -> 0x0037 }
        L_0x0035:
            r4 = 0
            goto L_0x0049
        L_0x0037:
            r12 = move-exception
            throw r12     // Catch:{ all -> 0x0039 }
        L_0x0039:
            r13 = move-exception
            if (r12 == 0) goto L_0x0045
            r1.close()     // Catch:{ all -> 0x0040 }
            goto L_0x0048
        L_0x0040:
            r14 = move-exception
            r12.addSuppressed(r14)
            goto L_0x0048
        L_0x0045:
            r1.close()
        L_0x0048:
            throw r13
        L_0x0049:
            r1.close()
            java.io.File r5 = new java.io.File
            java.io.File r1 = r11.soDirectory
            java.lang.String r7 = "dso_deps"
            r5.<init>(r1, r7)
            r1 = 0
            java.io.RandomAccessFile r7 = new java.io.RandomAccessFile
            r7.<init>(r5, r2)
            long r9 = r7.length()     // Catch:{ all -> 0x0100 }
            int r2 = (int) r9     // Catch:{ all -> 0x0100 }
            byte[] r9 = new byte[r2]     // Catch:{ all -> 0x0100 }
            int r10 = r7.read(r9)     // Catch:{ all -> 0x0100 }
            if (r10 == r2) goto L_0x006e
            java.lang.String r2 = "short read of so store deps file: marking unclean"
            android.util.Log.v(r0, r2)     // Catch:{ all -> 0x0100 }
            r4 = 0
        L_0x006e:
            boolean r2 = java.util.Arrays.equals(r9, r14)     // Catch:{ all -> 0x0100 }
            if (r2 != 0) goto L_0x007a
            java.lang.String r2 = "deps mismatch on deps store: regenerating"
            android.util.Log.v(r0, r2)     // Catch:{ all -> 0x0100 }
            r4 = 0
        L_0x007a:
            if (r4 == 0) goto L_0x0083
            r2 = r13 & 2
            if (r2 == 0) goto L_0x0081
            goto L_0x0083
        L_0x0081:
            r0 = r1
            goto L_0x00a5
        L_0x0083:
            java.lang.String r1 = "so store dirty: regenerating"
            android.util.Log.v(r0, r1)     // Catch:{ all -> 0x0100 }
            writeState(r6, r3)     // Catch:{ all -> 0x0100 }
            com.facebook.soloader.UnpackingSoSource$Unpacker r0 = r11.makeUnpacker()     // Catch:{ all -> 0x0100 }
            com.facebook.soloader.UnpackingSoSource$DsoManifest r1 = r0.getDsoManifest()     // Catch:{ all -> 0x00ec }
            com.facebook.soloader.UnpackingSoSource$InputDsoIterator r2 = r0.openDsoIterator()     // Catch:{ all -> 0x00ec }
            r11.regenerate(r4, r1, r2)     // Catch:{ all -> 0x00de }
            if (r2 == 0) goto L_0x009f
            r2.close()     // Catch:{ all -> 0x00ec }
        L_0x009f:
            if (r0 == 0) goto L_0x0081
            r0.close()     // Catch:{ all -> 0x0100 }
            goto L_0x0081
        L_0x00a5:
            r7.close()
            if (r0 != 0) goto L_0x00ab
            return r3
        L_0x00ab:
            com.facebook.soloader.UnpackingSoSource$1 r9 = new com.facebook.soloader.UnpackingSoSource$1
            r1 = r9
            r2 = r11
            r3 = r5
            r4 = r14
            r5 = r0
            r7 = r12
            r1.<init>(r3, r4, r5, r6, r7)
            r12 = r13 & 1
            if (r12 == 0) goto L_0x00da
            java.lang.Thread r12 = new java.lang.Thread
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r14 = "SoSync:"
            r13.append(r14)
            java.io.File r14 = r11.soDirectory
            java.lang.String r14 = r14.getName()
            r13.append(r14)
            java.lang.String r13 = r13.toString()
            r12.<init>(r9, r13)
            r12.start()
            goto L_0x00dd
        L_0x00da:
            r9.run()
        L_0x00dd:
            return r8
        L_0x00de:
            r12 = move-exception
            throw r12     // Catch:{ all -> 0x00e0 }
        L_0x00e0:
            r13 = move-exception
            if (r2 == 0) goto L_0x00eb
            r2.close()     // Catch:{ all -> 0x00e7 }
            goto L_0x00eb
        L_0x00e7:
            r14 = move-exception
            r12.addSuppressed(r14)     // Catch:{ all -> 0x00ec }
        L_0x00eb:
            throw r13     // Catch:{ all -> 0x00ec }
        L_0x00ec:
            r12 = move-exception
            throw r12     // Catch:{ all -> 0x00ee }
        L_0x00ee:
            r13 = move-exception
            if (r0 == 0) goto L_0x00ff
            if (r12 == 0) goto L_0x00fc
            r0.close()     // Catch:{ all -> 0x00f7 }
            goto L_0x00ff
        L_0x00f7:
            r14 = move-exception
            r12.addSuppressed(r14)     // Catch:{ all -> 0x0100 }
            goto L_0x00ff
        L_0x00fc:
            r0.close()     // Catch:{ all -> 0x0100 }
        L_0x00ff:
            throw r13     // Catch:{ all -> 0x0100 }
        L_0x0100:
            r12 = move-exception
            throw r12     // Catch:{ all -> 0x0102 }
        L_0x0102:
            r13 = move-exception
            if (r12 == 0) goto L_0x010e
            r7.close()     // Catch:{ all -> 0x0109 }
            goto L_0x0111
        L_0x0109:
            r14 = move-exception
            r12.addSuppressed(r14)
            goto L_0x0111
        L_0x010e:
            r7.close()
        L_0x0111:
            goto L_0x0113
        L_0x0112:
            throw r13
        L_0x0113:
            goto L_0x0112
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.UnpackingSoSource.refreshLocked(com.facebook.soloader.FileLocker, int, byte[]):boolean");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003a, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003b, code lost:
        if (r1 != null) goto L_0x003d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003d, code lost:
        if (r0 != null) goto L_0x003f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0043, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0044, code lost:
        r0.addSuppressed(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0048, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x004c, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte[] getDepsBlock() throws java.io.IOException {
        /*
            r5 = this;
            android.os.Parcel r0 = android.os.Parcel.obtain()
            com.facebook.soloader.UnpackingSoSource$Unpacker r1 = r5.makeUnpacker()
            com.facebook.soloader.UnpackingSoSource$DsoManifest r2 = r1.getDsoManifest()     // Catch:{ all -> 0x0038 }
            com.facebook.soloader.UnpackingSoSource$Dso[] r2 = r2.dsos     // Catch:{ all -> 0x0038 }
            r3 = 1
            r0.writeByte(r3)     // Catch:{ all -> 0x0038 }
            int r3 = r2.length     // Catch:{ all -> 0x0038 }
            r0.writeInt(r3)     // Catch:{ all -> 0x0038 }
            r3 = 0
        L_0x0017:
            int r4 = r2.length     // Catch:{ all -> 0x0038 }
            if (r3 >= r4) goto L_0x002b
            r4 = r2[r3]     // Catch:{ all -> 0x0038 }
            java.lang.String r4 = r4.name     // Catch:{ all -> 0x0038 }
            r0.writeString(r4)     // Catch:{ all -> 0x0038 }
            r4 = r2[r3]     // Catch:{ all -> 0x0038 }
            java.lang.String r4 = r4.hash     // Catch:{ all -> 0x0038 }
            r0.writeString(r4)     // Catch:{ all -> 0x0038 }
            int r3 = r3 + 1
            goto L_0x0017
        L_0x002b:
            if (r1 == 0) goto L_0x0030
            r1.close()
        L_0x0030:
            byte[] r1 = r0.marshall()
            r0.recycle()
            return r1
        L_0x0038:
            r0 = move-exception
            throw r0     // Catch:{ all -> 0x003a }
        L_0x003a:
            r2 = move-exception
            if (r1 == 0) goto L_0x004b
            if (r0 == 0) goto L_0x0048
            r1.close()     // Catch:{ all -> 0x0043 }
            goto L_0x004b
        L_0x0043:
            r1 = move-exception
            r0.addSuppressed(r1)
            goto L_0x004b
        L_0x0048:
            r1.close()
        L_0x004b:
            goto L_0x004d
        L_0x004c:
            throw r2
        L_0x004d:
            goto L_0x004c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.UnpackingSoSource.getDepsBlock():byte[]");
    }

    /* access modifiers changed from: protected */
    public void prepare(int i) throws IOException {
        SysUtil.mkdirOrThrow(this.soDirectory);
        FileLocker lock = FileLocker.lock(new File(this.soDirectory, LOCK_FILE_NAME));
        try {
            Log.v(TAG, "locked dso store " + this.soDirectory);
            if (refreshLocked(lock, i, getDepsBlock())) {
                lock = null;
            } else {
                Log.i(TAG, "dso store is up-to-date: " + this.soDirectory);
            }
            if (lock == null) {
                Log.v(TAG, "not releasing dso store lock for " + this.soDirectory + " (syncer thread started)");
            }
        } finally {
            if (lock != null) {
                Log.v(TAG, "releasing dso store lock for " + this.soDirectory);
                lock.close();
            } else {
                Log.v(TAG, "not releasing dso store lock for " + this.soDirectory + " (syncer thread started)");
            }
        }
    }

    private Object getLibraryLock(String str) {
        Object obj;
        synchronized (this.mLibsBeingLoaded) {
            obj = this.mLibsBeingLoaded.get(str);
            if (obj == null) {
                obj = new Object();
                this.mLibsBeingLoaded.put(str, obj);
            }
        }
        return obj;
    }

    /* access modifiers changed from: protected */
    public synchronized void prepare(String str) throws IOException {
        synchronized (getLibraryLock(str)) {
            this.mCorruptedLib = str;
            prepare(2);
        }
    }

    public int loadLibrary(String str, int i, StrictMode.ThreadPolicy threadPolicy) throws IOException {
        int loadLibraryFrom;
        synchronized (getLibraryLock(str)) {
            loadLibraryFrom = loadLibraryFrom(str, i, this.soDirectory, threadPolicy);
        }
        return loadLibraryFrom;
    }
}
