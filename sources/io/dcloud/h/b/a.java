package io.dcloud.h.b;

import com.facebook.cache.disk.DefaultDiskStorage;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.common.util.JSUtil;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public final class a implements Closeable {
    static final Pattern a = Pattern.compile("[a-z0-9_-]{1,64}");
    /* access modifiers changed from: private */
    public static final OutputStream b = new b();
    /* access modifiers changed from: private */
    public final File c;
    private final File d;
    private final File e;
    private final File f;
    private final int g;
    private long h;
    /* access modifiers changed from: private */
    public final int i;
    private long j = 0;
    /* access modifiers changed from: private */
    public Writer k;
    private final LinkedHashMap<String, d> l = new LinkedHashMap<>(0, 0.75f, true);
    /* access modifiers changed from: private */
    public int m;
    private long n = 0;
    final ThreadPoolExecutor o = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue());
    private final Callable<Void> p = new C0051a();

    /* renamed from: io.dcloud.h.b.a$a  reason: collision with other inner class name */
    class C0051a implements Callable<Void> {
        C0051a() {
        }

        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0027, code lost:
            return null;
         */
        /* renamed from: a */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.Void call() throws java.lang.Exception {
            /*
                r4 = this;
                io.dcloud.h.b.a r0 = io.dcloud.h.b.a.this
                monitor-enter(r0)
                io.dcloud.h.b.a r1 = io.dcloud.h.b.a.this     // Catch:{ all -> 0x0028 }
                java.io.Writer r1 = r1.k     // Catch:{ all -> 0x0028 }
                r2 = 0
                if (r1 != 0) goto L_0x000e
                monitor-exit(r0)     // Catch:{ all -> 0x0028 }
                return r2
            L_0x000e:
                io.dcloud.h.b.a r1 = io.dcloud.h.b.a.this     // Catch:{ all -> 0x0028 }
                r1.h()     // Catch:{ all -> 0x0028 }
                io.dcloud.h.b.a r1 = io.dcloud.h.b.a.this     // Catch:{ all -> 0x0028 }
                boolean r1 = r1.d()     // Catch:{ all -> 0x0028 }
                if (r1 == 0) goto L_0x0026
                io.dcloud.h.b.a r1 = io.dcloud.h.b.a.this     // Catch:{ all -> 0x0028 }
                r1.g()     // Catch:{ all -> 0x0028 }
                io.dcloud.h.b.a r1 = io.dcloud.h.b.a.this     // Catch:{ all -> 0x0028 }
                r3 = 0
                int unused = r1.m = r3     // Catch:{ all -> 0x0028 }
            L_0x0026:
                monitor-exit(r0)     // Catch:{ all -> 0x0028 }
                return r2
            L_0x0028:
                r1 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0028 }
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.h.b.a.C0051a.call():java.lang.Void");
        }
    }

    class b extends OutputStream {
        b() {
        }

        public void write(int i) throws IOException {
        }
    }

    public final class c {
        /* access modifiers changed from: private */
        public final d a;
        /* access modifiers changed from: private */
        public final boolean[] b;
        /* access modifiers changed from: private */
        public boolean c;
        private boolean d;

        /* renamed from: io.dcloud.h.b.a$c$a  reason: collision with other inner class name */
        private class C0052a extends FilterOutputStream {
            /* synthetic */ C0052a(c cVar, OutputStream outputStream, C0051a aVar) {
                this(outputStream);
            }

            public void close() {
                try {
                    this.out.close();
                } catch (IOException unused) {
                    boolean unused2 = c.this.c = true;
                }
            }

            public void flush() {
                try {
                    this.out.flush();
                } catch (IOException unused) {
                    boolean unused2 = c.this.c = true;
                }
            }

            public void write(int i) {
                try {
                    this.out.write(i);
                } catch (IOException unused) {
                    boolean unused2 = c.this.c = true;
                }
            }

            private C0052a(OutputStream outputStream) {
                super(outputStream);
            }

            public void write(byte[] bArr, int i, int i2) {
                try {
                    this.out.write(bArr, i, i2);
                } catch (IOException unused) {
                    boolean unused2 = c.this.c = true;
                }
            }
        }

        /* synthetic */ c(a aVar, d dVar, C0051a aVar2) {
            this(dVar);
        }

        private c(d dVar) {
            this.a = dVar;
            this.b = dVar.c ? null : new boolean[a.this.i];
        }

        public void b() throws IOException {
            if (this.c) {
                a.this.a(this, false);
                a.this.d(this.a.a);
            } else {
                a.this.a(this, true);
            }
            this.d = true;
        }

        /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0024 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.io.OutputStream a(int r4) throws java.io.IOException {
            /*
                r3 = this;
                io.dcloud.h.b.a r0 = io.dcloud.h.b.a.this
                monitor-enter(r0)
                io.dcloud.h.b.a$d r1 = r3.a     // Catch:{ all -> 0x0046 }
                io.dcloud.h.b.a$c r1 = r1.d     // Catch:{ all -> 0x0046 }
                if (r1 != r3) goto L_0x0040
                io.dcloud.h.b.a$d r1 = r3.a     // Catch:{ all -> 0x0046 }
                boolean r1 = r1.c     // Catch:{ all -> 0x0046 }
                if (r1 != 0) goto L_0x0018
                boolean[] r1 = r3.b     // Catch:{ all -> 0x0046 }
                r2 = 1
                r1[r4] = r2     // Catch:{ all -> 0x0046 }
            L_0x0018:
                io.dcloud.h.b.a$d r1 = r3.a     // Catch:{ all -> 0x0046 }
                java.io.File r4 = r1.b((int) r4)     // Catch:{ all -> 0x0046 }
                java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x0024 }
                r1.<init>(r4)     // Catch:{ FileNotFoundException -> 0x0024 }
                goto L_0x0032
            L_0x0024:
                io.dcloud.h.b.a r1 = io.dcloud.h.b.a.this     // Catch:{ all -> 0x0046 }
                java.io.File r1 = r1.c     // Catch:{ all -> 0x0046 }
                r1.mkdirs()     // Catch:{ all -> 0x0046 }
                java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x003a }
                r1.<init>(r4)     // Catch:{ FileNotFoundException -> 0x003a }
            L_0x0032:
                io.dcloud.h.b.a$c$a r4 = new io.dcloud.h.b.a$c$a     // Catch:{ all -> 0x0046 }
                r2 = 0
                r4.<init>(r3, r1, r2)     // Catch:{ all -> 0x0046 }
                monitor-exit(r0)     // Catch:{ all -> 0x0046 }
                return r4
            L_0x003a:
                java.io.OutputStream r4 = io.dcloud.h.b.a.b     // Catch:{ all -> 0x0046 }
                monitor-exit(r0)     // Catch:{ all -> 0x0046 }
                return r4
            L_0x0040:
                java.lang.IllegalStateException r4 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0046 }
                r4.<init>()     // Catch:{ all -> 0x0046 }
                throw r4     // Catch:{ all -> 0x0046 }
            L_0x0046:
                r4 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0046 }
                throw r4
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.h.b.a.c.a(int):java.io.OutputStream");
        }

        public void a() throws IOException {
            a.this.a(this, false);
        }
    }

    private final class d {
        /* access modifiers changed from: private */
        public final String a;
        /* access modifiers changed from: private */
        public final long[] b;
        /* access modifiers changed from: private */
        public boolean c;
        /* access modifiers changed from: private */
        public c d;
        /* access modifiers changed from: private */
        public long e;

        /* synthetic */ d(a aVar, String str, C0051a aVar2) {
            this(str);
        }

        private d(String str) {
            this.a = str;
            this.b = new long[a.this.i];
        }

        /* access modifiers changed from: private */
        public void b(String[] strArr) throws IOException {
            if (strArr.length == a.this.i) {
                int i = 0;
                while (i < strArr.length) {
                    try {
                        this.b[i] = Long.parseLong(strArr[i]);
                        i++;
                    } catch (NumberFormatException unused) {
                        throw a(strArr);
                    }
                }
                return;
            }
            throw a(strArr);
        }

        public String a() throws IOException {
            StringBuilder sb = new StringBuilder();
            for (long append : this.b) {
                sb.append(' ');
                sb.append(append);
            }
            return sb.toString();
        }

        private IOException a(String[] strArr) throws IOException {
            throw new IOException("unexpected journal line: " + Arrays.toString(strArr));
        }

        public File a(int i) {
            File d2 = a.this.c;
            return new File(d2, this.a + Operators.DOT_STR + i);
        }

        public File b(int i) {
            File d2 = a.this.c;
            return new File(d2, this.a + Operators.DOT_STR + i + DefaultDiskStorage.FileType.TEMP);
        }
    }

    public final class e implements Closeable {
        private final String a;
        private final long b;
        private final InputStream[] c;
        private final long[] d;

        /* synthetic */ e(a aVar, String str, long j, InputStream[] inputStreamArr, long[] jArr, C0051a aVar2) {
            this(str, j, inputStreamArr, jArr);
        }

        public InputStream a(int i) {
            return this.c[i];
        }

        public String b(int i) throws IOException {
            return a.b(a(i));
        }

        public void close() {
            for (InputStream a2 : this.c) {
                c.a((Closeable) a2);
            }
        }

        private e(String str, long j, InputStream[] inputStreamArr, long[] jArr) {
            this.a = str;
            this.b = j;
            this.c = inputStreamArr;
            this.d = jArr;
        }
    }

    private a(File file, int i2, int i3, long j2) {
        File file2 = file;
        this.c = file2;
        this.g = i2;
        this.d = new File(file2, "journal");
        this.e = new File(file2, "journal.tmp");
        this.f = new File(file2, "journal.bkp");
        this.i = i3;
        this.h = j2;
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: private */
    public synchronized void g() throws IOException {
        Writer writer = this.k;
        if (writer != null) {
            writer.close();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.e), c.a));
        try {
            bufferedWriter.write("libcore.io.DiskLruCache");
            bufferedWriter.write("\n");
            bufferedWriter.write("1");
            bufferedWriter.write("\n");
            bufferedWriter.write(Integer.toString(this.g));
            bufferedWriter.write("\n");
            bufferedWriter.write(Integer.toString(this.i));
            bufferedWriter.write("\n");
            bufferedWriter.write("\n");
            for (d next : this.l.values()) {
                if (next.d != null) {
                    bufferedWriter.write("DIRTY " + next.a + 10);
                } else {
                    bufferedWriter.write("CLEAN " + next.a + next.a() + 10);
                }
            }
            bufferedWriter.close();
            if (this.d.exists()) {
                a(this.d, this.f, true);
            }
            a(this.e, this.d, false);
            this.f.delete();
            this.k = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.d, true), c.a));
        } catch (Throwable th) {
            bufferedWriter.close();
            throw th;
        }
    }

    /* access modifiers changed from: private */
    public void h() throws IOException {
        while (this.j > this.h) {
            d((String) this.l.entrySet().iterator().next().getKey());
        }
    }

    public synchronized void close() throws IOException {
        if (this.k != null) {
            Iterator it = new ArrayList(this.l.values()).iterator();
            while (it.hasNext()) {
                d dVar = (d) it.next();
                if (dVar.d != null) {
                    dVar.d.a();
                }
            }
            h();
            this.k.close();
            this.k = null;
        }
    }

    public synchronized void flush() throws IOException {
        b();
        h();
        this.k.flush();
    }

    private void c(String str) throws IOException {
        String str2;
        int indexOf = str.indexOf(32);
        if (indexOf != -1) {
            int i2 = indexOf + 1;
            int indexOf2 = str.indexOf(32, i2);
            if (indexOf2 == -1) {
                str2 = str.substring(i2);
                if (indexOf == 6 && str.startsWith("REMOVE")) {
                    this.l.remove(str2);
                    return;
                }
            } else {
                str2 = str.substring(i2, indexOf2);
            }
            d dVar = this.l.get(str2);
            if (dVar == null) {
                dVar = new d(this, str2, (C0051a) null);
                this.l.put(str2, dVar);
            }
            if (indexOf2 != -1 && indexOf == 5 && str.startsWith("CLEAN")) {
                String[] split = str.substring(indexOf2 + 1).split(Operators.SPACE_STR);
                boolean unused = dVar.c = true;
                c unused2 = dVar.d = null;
                dVar.b(split);
            } else if (indexOf2 == -1 && indexOf == 5 && str.startsWith("DIRTY")) {
                c unused3 = dVar.d = new c(this, dVar, (C0051a) null);
            } else if (indexOf2 != -1 || indexOf != 4 || !str.startsWith("READ")) {
                throw new IOException("unexpected journal line: " + str);
            }
        } else {
            throw new IOException("unexpected journal line: " + str);
        }
    }

    /* access modifiers changed from: private */
    public boolean d() {
        int i2 = this.m;
        return i2 >= 2000 && i2 >= this.l.size();
    }

    private void e() throws IOException {
        a(this.e);
        Iterator<d> it = this.l.values().iterator();
        while (it.hasNext()) {
            d next = it.next();
            int i2 = 0;
            if (next.d == null) {
                while (i2 < this.i) {
                    this.j += next.b[i2];
                    i2++;
                }
            } else {
                c unused = next.d = null;
                while (i2 < this.i) {
                    a(next.a(i2));
                    a(next.b(i2));
                    i2++;
                }
                it.remove();
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:16|17|18|19) */
    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        r9.m = r0 - r9.l.size();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x006b, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:16:0x005f */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:20:0x006c=Splitter:B:20:0x006c, B:16:0x005f=Splitter:B:16:0x005f} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void f() throws java.io.IOException {
        /*
            r9 = this;
            java.lang.String r0 = ", "
            io.dcloud.h.b.b r1 = new io.dcloud.h.b.b
            java.io.FileInputStream r2 = new java.io.FileInputStream
            java.io.File r3 = r9.d
            r2.<init>(r3)
            java.nio.charset.Charset r3 = io.dcloud.h.b.c.a
            r1.<init>(r2, r3)
            java.lang.String r2 = r1.b()     // Catch:{ all -> 0x009a }
            java.lang.String r3 = r1.b()     // Catch:{ all -> 0x009a }
            java.lang.String r4 = r1.b()     // Catch:{ all -> 0x009a }
            java.lang.String r5 = r1.b()     // Catch:{ all -> 0x009a }
            java.lang.String r6 = r1.b()     // Catch:{ all -> 0x009a }
            java.lang.String r7 = "libcore.io.DiskLruCache"
            boolean r7 = r7.equals(r2)     // Catch:{ all -> 0x009a }
            if (r7 == 0) goto L_0x006c
            java.lang.String r7 = "1"
            boolean r7 = r7.equals(r3)     // Catch:{ all -> 0x009a }
            if (r7 == 0) goto L_0x006c
            int r7 = r9.g     // Catch:{ all -> 0x009a }
            java.lang.String r7 = java.lang.Integer.toString(r7)     // Catch:{ all -> 0x009a }
            boolean r4 = r7.equals(r4)     // Catch:{ all -> 0x009a }
            if (r4 == 0) goto L_0x006c
            int r4 = r9.i     // Catch:{ all -> 0x009a }
            java.lang.String r4 = java.lang.Integer.toString(r4)     // Catch:{ all -> 0x009a }
            boolean r4 = r4.equals(r5)     // Catch:{ all -> 0x009a }
            if (r4 == 0) goto L_0x006c
            java.lang.String r4 = ""
            boolean r4 = r4.equals(r6)     // Catch:{ all -> 0x009a }
            if (r4 == 0) goto L_0x006c
            r0 = 0
        L_0x0055:
            java.lang.String r2 = r1.b()     // Catch:{ EOFException -> 0x005f }
            r9.c((java.lang.String) r2)     // Catch:{ EOFException -> 0x005f }
            int r0 = r0 + 1
            goto L_0x0055
        L_0x005f:
            java.util.LinkedHashMap<java.lang.String, io.dcloud.h.b.a$d> r2 = r9.l     // Catch:{ all -> 0x009a }
            int r2 = r2.size()     // Catch:{ all -> 0x009a }
            int r0 = r0 - r2
            r9.m = r0     // Catch:{ all -> 0x009a }
            io.dcloud.h.b.c.a((java.io.Closeable) r1)
            return
        L_0x006c:
            java.io.IOException r4 = new java.io.IOException     // Catch:{ all -> 0x009a }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x009a }
            r7.<init>()     // Catch:{ all -> 0x009a }
            java.lang.String r8 = "unexpected journal header: ["
            r7.append(r8)     // Catch:{ all -> 0x009a }
            r7.append(r2)     // Catch:{ all -> 0x009a }
            r7.append(r0)     // Catch:{ all -> 0x009a }
            r7.append(r3)     // Catch:{ all -> 0x009a }
            r7.append(r0)     // Catch:{ all -> 0x009a }
            r7.append(r5)     // Catch:{ all -> 0x009a }
            r7.append(r0)     // Catch:{ all -> 0x009a }
            r7.append(r6)     // Catch:{ all -> 0x009a }
            java.lang.String r0 = "]"
            r7.append(r0)     // Catch:{ all -> 0x009a }
            java.lang.String r0 = r7.toString()     // Catch:{ all -> 0x009a }
            r4.<init>(r0)     // Catch:{ all -> 0x009a }
            throw r4     // Catch:{ all -> 0x009a }
        L_0x009a:
            r0 = move-exception
            io.dcloud.h.b.c.a((java.io.Closeable) r1)
            goto L_0x00a0
        L_0x009f:
            throw r0
        L_0x00a0:
            goto L_0x009f
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.h.b.a.f():void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:32|33|28|27) */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        r11.m++;
        r11.k.append("READ " + r12 + 10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0059, code lost:
        if (d() == false) goto L_0x0062;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x005b, code lost:
        r11.o.submit(r11.p);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0074, code lost:
        return new io.dcloud.h.b.a.e(r11, r12, io.dcloud.h.b.a.d.c(r0), r8, io.dcloud.h.b.a.d.a(r0), (io.dcloud.h.b.a.C0051a) null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0086, code lost:
        return null;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0075 */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x007d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized io.dcloud.h.b.a.e b(java.lang.String r12) throws java.io.IOException {
        /*
            r11 = this;
            monitor-enter(r11)
            r11.b()     // Catch:{ all -> 0x0087 }
            r11.e((java.lang.String) r12)     // Catch:{ all -> 0x0087 }
            java.util.LinkedHashMap<java.lang.String, io.dcloud.h.b.a$d> r0 = r11.l     // Catch:{ all -> 0x0087 }
            java.lang.Object r0 = r0.get(r12)     // Catch:{ all -> 0x0087 }
            io.dcloud.h.b.a$d r0 = (io.dcloud.h.b.a.d) r0     // Catch:{ all -> 0x0087 }
            r1 = 0
            if (r0 != 0) goto L_0x0014
            monitor-exit(r11)
            return r1
        L_0x0014:
            boolean r2 = r0.c     // Catch:{ all -> 0x0087 }
            if (r2 != 0) goto L_0x001c
            monitor-exit(r11)
            return r1
        L_0x001c:
            int r2 = r11.i     // Catch:{ all -> 0x0087 }
            java.io.InputStream[] r8 = new java.io.InputStream[r2]     // Catch:{ all -> 0x0087 }
            r2 = 0
            r3 = 0
        L_0x0022:
            int r4 = r11.i     // Catch:{ FileNotFoundException -> 0x0075 }
            if (r3 >= r4) goto L_0x0034
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x0075 }
            java.io.File r5 = r0.a((int) r3)     // Catch:{ FileNotFoundException -> 0x0075 }
            r4.<init>(r5)     // Catch:{ FileNotFoundException -> 0x0075 }
            r8[r3] = r4     // Catch:{ FileNotFoundException -> 0x0075 }
            int r3 = r3 + 1
            goto L_0x0022
        L_0x0034:
            int r1 = r11.m     // Catch:{ all -> 0x0087 }
            int r1 = r1 + 1
            r11.m = r1     // Catch:{ all -> 0x0087 }
            java.io.Writer r1 = r11.k     // Catch:{ all -> 0x0087 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0087 }
            r2.<init>()     // Catch:{ all -> 0x0087 }
            java.lang.String r3 = "READ "
            r2.append(r3)     // Catch:{ all -> 0x0087 }
            r2.append(r12)     // Catch:{ all -> 0x0087 }
            r3 = 10
            r2.append(r3)     // Catch:{ all -> 0x0087 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0087 }
            r1.append(r2)     // Catch:{ all -> 0x0087 }
            boolean r1 = r11.d()     // Catch:{ all -> 0x0087 }
            if (r1 == 0) goto L_0x0062
            java.util.concurrent.ThreadPoolExecutor r1 = r11.o     // Catch:{ all -> 0x0087 }
            java.util.concurrent.Callable<java.lang.Void> r2 = r11.p     // Catch:{ all -> 0x0087 }
            r1.submit(r2)     // Catch:{ all -> 0x0087 }
        L_0x0062:
            io.dcloud.h.b.a$e r1 = new io.dcloud.h.b.a$e     // Catch:{ all -> 0x0087 }
            long r6 = r0.e     // Catch:{ all -> 0x0087 }
            long[] r9 = r0.b     // Catch:{ all -> 0x0087 }
            r10 = 0
            r3 = r1
            r4 = r11
            r5 = r12
            r3.<init>(r4, r5, r6, r8, r9, r10)     // Catch:{ all -> 0x0087 }
            monitor-exit(r11)
            return r1
        L_0x0075:
            int r12 = r11.i     // Catch:{ all -> 0x0087 }
            if (r2 >= r12) goto L_0x0085
            r12 = r8[r2]     // Catch:{ all -> 0x0087 }
            if (r12 == 0) goto L_0x0085
            r12 = r8[r2]     // Catch:{ all -> 0x0087 }
            io.dcloud.h.b.c.a((java.io.Closeable) r12)     // Catch:{ all -> 0x0087 }
            int r2 = r2 + 1
            goto L_0x0075
        L_0x0085:
            monitor-exit(r11)
            return r1
        L_0x0087:
            r12 = move-exception
            monitor-exit(r11)
            goto L_0x008b
        L_0x008a:
            throw r12
        L_0x008b:
            goto L_0x008a
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.h.b.a.b(java.lang.String):io.dcloud.h.b.a$e");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0090, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0092, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean d(java.lang.String r8) throws java.io.IOException {
        /*
            r7 = this;
            monitor-enter(r7)
            r7.b()     // Catch:{ all -> 0x0093 }
            r7.e((java.lang.String) r8)     // Catch:{ all -> 0x0093 }
            java.util.LinkedHashMap<java.lang.String, io.dcloud.h.b.a$d> r0 = r7.l     // Catch:{ all -> 0x0093 }
            java.lang.Object r0 = r0.get(r8)     // Catch:{ all -> 0x0093 }
            io.dcloud.h.b.a$d r0 = (io.dcloud.h.b.a.d) r0     // Catch:{ all -> 0x0093 }
            r1 = 0
            if (r0 == 0) goto L_0x0091
            io.dcloud.h.b.a$c r2 = r0.d     // Catch:{ all -> 0x0093 }
            if (r2 == 0) goto L_0x001a
            goto L_0x0091
        L_0x001a:
            int r2 = r7.i     // Catch:{ all -> 0x0093 }
            if (r1 >= r2) goto L_0x005c
            java.io.File r2 = r0.a((int) r1)     // Catch:{ all -> 0x0093 }
            boolean r3 = r2.exists()     // Catch:{ all -> 0x0093 }
            if (r3 == 0) goto L_0x0046
            boolean r3 = r2.delete()     // Catch:{ all -> 0x0093 }
            if (r3 == 0) goto L_0x002f
            goto L_0x0046
        L_0x002f:
            java.io.IOException r8 = new java.io.IOException     // Catch:{ all -> 0x0093 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0093 }
            r0.<init>()     // Catch:{ all -> 0x0093 }
            java.lang.String r1 = "failed to delete "
            r0.append(r1)     // Catch:{ all -> 0x0093 }
            r0.append(r2)     // Catch:{ all -> 0x0093 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0093 }
            r8.<init>(r0)     // Catch:{ all -> 0x0093 }
            throw r8     // Catch:{ all -> 0x0093 }
        L_0x0046:
            long r2 = r7.j     // Catch:{ all -> 0x0093 }
            long[] r4 = r0.b     // Catch:{ all -> 0x0093 }
            r5 = r4[r1]     // Catch:{ all -> 0x0093 }
            long r2 = r2 - r5
            r7.j = r2     // Catch:{ all -> 0x0093 }
            long[] r2 = r0.b     // Catch:{ all -> 0x0093 }
            r3 = 0
            r2[r1] = r3     // Catch:{ all -> 0x0093 }
            int r1 = r1 + 1
            goto L_0x001a
        L_0x005c:
            int r0 = r7.m     // Catch:{ all -> 0x0093 }
            r1 = 1
            int r0 = r0 + r1
            r7.m = r0     // Catch:{ all -> 0x0093 }
            java.io.Writer r0 = r7.k     // Catch:{ all -> 0x0093 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0093 }
            r2.<init>()     // Catch:{ all -> 0x0093 }
            java.lang.String r3 = "REMOVE "
            r2.append(r3)     // Catch:{ all -> 0x0093 }
            r2.append(r8)     // Catch:{ all -> 0x0093 }
            r3 = 10
            r2.append(r3)     // Catch:{ all -> 0x0093 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0093 }
            r0.append(r2)     // Catch:{ all -> 0x0093 }
            java.util.LinkedHashMap<java.lang.String, io.dcloud.h.b.a$d> r0 = r7.l     // Catch:{ all -> 0x0093 }
            r0.remove(r8)     // Catch:{ all -> 0x0093 }
            boolean r8 = r7.d()     // Catch:{ all -> 0x0093 }
            if (r8 == 0) goto L_0x008f
            java.util.concurrent.ThreadPoolExecutor r8 = r7.o     // Catch:{ all -> 0x0093 }
            java.util.concurrent.Callable<java.lang.Void> r0 = r7.p     // Catch:{ all -> 0x0093 }
            r8.submit(r0)     // Catch:{ all -> 0x0093 }
        L_0x008f:
            monitor-exit(r7)
            return r1
        L_0x0091:
            monitor-exit(r7)
            return r1
        L_0x0093:
            r8 = move-exception
            monitor-exit(r7)
            goto L_0x0097
        L_0x0096:
            throw r8
        L_0x0097:
            goto L_0x0096
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.h.b.a.d(java.lang.String):boolean");
    }

    public static a a(File file, int i2, int i3, long j2) throws IOException {
        if (j2 <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        } else if (i3 > 0) {
            File file2 = new File(file, "journal.bkp");
            if (file2.exists()) {
                File file3 = new File(file, "journal");
                if (file3.exists()) {
                    file2.delete();
                } else {
                    a(file2, file3, false);
                }
            }
            a aVar = new a(file, i2, i3, j2);
            if (aVar.d.exists()) {
                try {
                    aVar.f();
                    aVar.e();
                    aVar.k = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(aVar.d, true), c.a));
                    return aVar;
                } catch (IOException e2) {
                    PrintStream printStream = System.out;
                    printStream.println("DiskLruCache " + file + " is corrupt: " + e2.getMessage() + ", removing");
                    aVar.c();
                }
            }
            file.mkdirs();
            a aVar2 = new a(file, i2, i3, j2);
            aVar2.g();
            return aVar2;
        } else {
            throw new IllegalArgumentException("valueCount <= 0");
        }
    }

    private void e(String str) {
        if (!a.matcher(str).matches()) {
            throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,64}: \"" + str + JSUtil.QUOTE);
        }
    }

    public void c() throws IOException {
        close();
        c.a(this.c);
    }

    private void b() {
        if (this.k == null) {
            throw new IllegalStateException("cache is closed");
        }
    }

    /* access modifiers changed from: private */
    public static String b(InputStream inputStream) throws IOException {
        return c.a((Reader) new InputStreamReader(inputStream, c.b));
    }

    private static void a(File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }

    private static void a(File file, File file2, boolean z) throws IOException {
        if (z) {
            a(file2);
        }
        if (!file.renameTo(file2)) {
            throw new IOException();
        }
    }

    public c a(String str) throws IOException {
        return a(str, -1);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0021, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized io.dcloud.h.b.a.c a(java.lang.String r6, long r7) throws java.io.IOException {
        /*
            r5 = this;
            monitor-enter(r5)
            r5.b()     // Catch:{ all -> 0x0061 }
            r5.e((java.lang.String) r6)     // Catch:{ all -> 0x0061 }
            java.util.LinkedHashMap<java.lang.String, io.dcloud.h.b.a$d> r0 = r5.l     // Catch:{ all -> 0x0061 }
            java.lang.Object r0 = r0.get(r6)     // Catch:{ all -> 0x0061 }
            io.dcloud.h.b.a$d r0 = (io.dcloud.h.b.a.d) r0     // Catch:{ all -> 0x0061 }
            r1 = -1
            r3 = 0
            int r4 = (r7 > r1 ? 1 : (r7 == r1 ? 0 : -1))
            if (r4 == 0) goto L_0x0022
            if (r0 == 0) goto L_0x0020
            long r1 = r0.e     // Catch:{ all -> 0x0061 }
            int r4 = (r1 > r7 ? 1 : (r1 == r7 ? 0 : -1))
            if (r4 == 0) goto L_0x0022
        L_0x0020:
            monitor-exit(r5)
            return r3
        L_0x0022:
            if (r0 != 0) goto L_0x002f
            io.dcloud.h.b.a$d r0 = new io.dcloud.h.b.a$d     // Catch:{ all -> 0x0061 }
            r0.<init>(r5, r6, r3)     // Catch:{ all -> 0x0061 }
            java.util.LinkedHashMap<java.lang.String, io.dcloud.h.b.a$d> r7 = r5.l     // Catch:{ all -> 0x0061 }
            r7.put(r6, r0)     // Catch:{ all -> 0x0061 }
            goto L_0x0037
        L_0x002f:
            io.dcloud.h.b.a$c r7 = r0.d     // Catch:{ all -> 0x0061 }
            if (r7 == 0) goto L_0x0037
            monitor-exit(r5)
            return r3
        L_0x0037:
            io.dcloud.h.b.a$c r7 = new io.dcloud.h.b.a$c     // Catch:{ all -> 0x0061 }
            r7.<init>(r5, r0, r3)     // Catch:{ all -> 0x0061 }
            io.dcloud.h.b.a.c unused = r0.d = r7     // Catch:{ all -> 0x0061 }
            java.io.Writer r8 = r5.k     // Catch:{ all -> 0x0061 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0061 }
            r0.<init>()     // Catch:{ all -> 0x0061 }
            java.lang.String r1 = "DIRTY "
            r0.append(r1)     // Catch:{ all -> 0x0061 }
            r0.append(r6)     // Catch:{ all -> 0x0061 }
            r6 = 10
            r0.append(r6)     // Catch:{ all -> 0x0061 }
            java.lang.String r6 = r0.toString()     // Catch:{ all -> 0x0061 }
            r8.write(r6)     // Catch:{ all -> 0x0061 }
            java.io.Writer r6 = r5.k     // Catch:{ all -> 0x0061 }
            r6.flush()     // Catch:{ all -> 0x0061 }
            monitor-exit(r5)
            return r7
        L_0x0061:
            r6 = move-exception
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.h.b.a.a(java.lang.String, long):io.dcloud.h.b.a$c");
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0109, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void a(io.dcloud.h.b.a.c r10, boolean r11) throws java.io.IOException {
        /*
            r9 = this;
            monitor-enter(r9)
            io.dcloud.h.b.a$d r0 = r10.a     // Catch:{ all -> 0x0110 }
            io.dcloud.h.b.a$c r1 = r0.d     // Catch:{ all -> 0x0110 }
            if (r1 != r10) goto L_0x010a
            r1 = 0
            if (r11 == 0) goto L_0x004d
            boolean r2 = r0.c     // Catch:{ all -> 0x0110 }
            if (r2 != 0) goto L_0x004d
            r2 = 0
        L_0x0015:
            int r3 = r9.i     // Catch:{ all -> 0x0110 }
            if (r2 >= r3) goto L_0x004d
            boolean[] r3 = r10.b     // Catch:{ all -> 0x0110 }
            boolean r3 = r3[r2]     // Catch:{ all -> 0x0110 }
            if (r3 == 0) goto L_0x0033
            java.io.File r3 = r0.b((int) r2)     // Catch:{ all -> 0x0110 }
            boolean r3 = r3.exists()     // Catch:{ all -> 0x0110 }
            if (r3 != 0) goto L_0x0030
            r10.a()     // Catch:{ all -> 0x0110 }
            monitor-exit(r9)
            return
        L_0x0030:
            int r2 = r2 + 1
            goto L_0x0015
        L_0x0033:
            r10.a()     // Catch:{ all -> 0x0110 }
            java.lang.IllegalStateException r10 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0110 }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x0110 }
            r11.<init>()     // Catch:{ all -> 0x0110 }
            java.lang.String r0 = "Newly created entry didn't create value for index "
            r11.append(r0)     // Catch:{ all -> 0x0110 }
            r11.append(r2)     // Catch:{ all -> 0x0110 }
            java.lang.String r11 = r11.toString()     // Catch:{ all -> 0x0110 }
            r10.<init>(r11)     // Catch:{ all -> 0x0110 }
            throw r10     // Catch:{ all -> 0x0110 }
        L_0x004d:
            int r10 = r9.i     // Catch:{ all -> 0x0110 }
            if (r1 >= r10) goto L_0x0081
            java.io.File r10 = r0.b((int) r1)     // Catch:{ all -> 0x0110 }
            if (r11 == 0) goto L_0x007b
            boolean r2 = r10.exists()     // Catch:{ all -> 0x0110 }
            if (r2 == 0) goto L_0x007e
            java.io.File r2 = r0.a((int) r1)     // Catch:{ all -> 0x0110 }
            r10.renameTo(r2)     // Catch:{ all -> 0x0110 }
            long[] r10 = r0.b     // Catch:{ all -> 0x0110 }
            r3 = r10[r1]     // Catch:{ all -> 0x0110 }
            long r5 = r2.length()     // Catch:{ all -> 0x0110 }
            long[] r10 = r0.b     // Catch:{ all -> 0x0110 }
            r10[r1] = r5     // Catch:{ all -> 0x0110 }
            long r7 = r9.j     // Catch:{ all -> 0x0110 }
            long r7 = r7 - r3
            long r7 = r7 + r5
            r9.j = r7     // Catch:{ all -> 0x0110 }
            goto L_0x007e
        L_0x007b:
            a((java.io.File) r10)     // Catch:{ all -> 0x0110 }
        L_0x007e:
            int r1 = r1 + 1
            goto L_0x004d
        L_0x0081:
            int r10 = r9.m     // Catch:{ all -> 0x0110 }
            r1 = 1
            int r10 = r10 + r1
            r9.m = r10     // Catch:{ all -> 0x0110 }
            r10 = 0
            io.dcloud.h.b.a.c unused = r0.d = r10     // Catch:{ all -> 0x0110 }
            boolean r10 = r0.c     // Catch:{ all -> 0x0110 }
            r10 = r10 | r11
            r2 = 10
            if (r10 == 0) goto L_0x00c8
            boolean unused = r0.c = r1     // Catch:{ all -> 0x0110 }
            java.io.Writer r10 = r9.k     // Catch:{ all -> 0x0110 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0110 }
            r1.<init>()     // Catch:{ all -> 0x0110 }
            java.lang.String r3 = "CLEAN "
            r1.append(r3)     // Catch:{ all -> 0x0110 }
            java.lang.String r3 = r0.a     // Catch:{ all -> 0x0110 }
            r1.append(r3)     // Catch:{ all -> 0x0110 }
            java.lang.String r3 = r0.a()     // Catch:{ all -> 0x0110 }
            r1.append(r3)     // Catch:{ all -> 0x0110 }
            r1.append(r2)     // Catch:{ all -> 0x0110 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0110 }
            r10.write(r1)     // Catch:{ all -> 0x0110 }
            if (r11 == 0) goto L_0x00ee
            long r10 = r9.n     // Catch:{ all -> 0x0110 }
            r1 = 1
            long r1 = r1 + r10
            r9.n = r1     // Catch:{ all -> 0x0110 }
            long unused = r0.e = r10     // Catch:{ all -> 0x0110 }
            goto L_0x00ee
        L_0x00c8:
            java.util.LinkedHashMap<java.lang.String, io.dcloud.h.b.a$d> r10 = r9.l     // Catch:{ all -> 0x0110 }
            java.lang.String r11 = r0.a     // Catch:{ all -> 0x0110 }
            r10.remove(r11)     // Catch:{ all -> 0x0110 }
            java.io.Writer r10 = r9.k     // Catch:{ all -> 0x0110 }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x0110 }
            r11.<init>()     // Catch:{ all -> 0x0110 }
            java.lang.String r1 = "REMOVE "
            r11.append(r1)     // Catch:{ all -> 0x0110 }
            java.lang.String r0 = r0.a     // Catch:{ all -> 0x0110 }
            r11.append(r0)     // Catch:{ all -> 0x0110 }
            r11.append(r2)     // Catch:{ all -> 0x0110 }
            java.lang.String r11 = r11.toString()     // Catch:{ all -> 0x0110 }
            r10.write(r11)     // Catch:{ all -> 0x0110 }
        L_0x00ee:
            java.io.Writer r10 = r9.k     // Catch:{ all -> 0x0110 }
            r10.flush()     // Catch:{ all -> 0x0110 }
            long r10 = r9.j     // Catch:{ all -> 0x0110 }
            long r0 = r9.h     // Catch:{ all -> 0x0110 }
            int r2 = (r10 > r0 ? 1 : (r10 == r0 ? 0 : -1))
            if (r2 > 0) goto L_0x0101
            boolean r10 = r9.d()     // Catch:{ all -> 0x0110 }
            if (r10 == 0) goto L_0x0108
        L_0x0101:
            java.util.concurrent.ThreadPoolExecutor r10 = r9.o     // Catch:{ all -> 0x0110 }
            java.util.concurrent.Callable<java.lang.Void> r11 = r9.p     // Catch:{ all -> 0x0110 }
            r10.submit(r11)     // Catch:{ all -> 0x0110 }
        L_0x0108:
            monitor-exit(r9)
            return
        L_0x010a:
            java.lang.IllegalStateException r10 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0110 }
            r10.<init>()     // Catch:{ all -> 0x0110 }
            throw r10     // Catch:{ all -> 0x0110 }
        L_0x0110:
            r10 = move-exception
            monitor-exit(r9)
            goto L_0x0114
        L_0x0113:
            throw r10
        L_0x0114:
            goto L_0x0113
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.h.b.a.a(io.dcloud.h.b.a$c, boolean):void");
    }
}
