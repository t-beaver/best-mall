package io.dcloud.e.f;

import android.os.Build;
import android.os.Process;
import android.util.Base64;
import android.util.Log;
import com.facebook.imagepipeline.memory.BitmapCounterConfig;
import io.dcloud.common.util.AESUtil;
import io.dcloud.common.util.ExifInterface;
import io.dcloud.common.util.StringUtil;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.SecureRandomSpi;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

class a {
    static final AtomicBoolean a = new AtomicBoolean(false);

    /* renamed from: io.dcloud.e.f.a$a  reason: collision with other inner class name */
    public static class C0035a {
        private final byte[] a;
        private final byte[] b;
        private final byte[] c;

        public C0035a(byte[] bArr, byte[] bArr2, byte[] bArr3) {
            byte[] bArr4 = new byte[bArr.length];
            this.a = bArr4;
            System.arraycopy(bArr, 0, bArr4, 0, bArr.length);
            byte[] bArr5 = new byte[bArr2.length];
            this.b = bArr5;
            System.arraycopy(bArr2, 0, bArr5, 0, bArr2.length);
            byte[] bArr6 = new byte[bArr3.length];
            this.c = bArr6;
            System.arraycopy(bArr3, 0, bArr6, 0, bArr3.length);
        }

        public byte[] a() {
            return this.a;
        }

        public byte[] b() {
            return this.b;
        }

        public byte[] c() {
            return this.c;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || C0035a.class != obj.getClass()) {
                return false;
            }
            C0035a aVar = (C0035a) obj;
            return Arrays.equals(this.a, aVar.a) && Arrays.equals(this.b, aVar.b) && Arrays.equals(this.c, aVar.c);
        }

        public int hashCode() {
            return ((((Arrays.hashCode(this.a) + 31) * 31) + Arrays.hashCode(this.b)) * 31) + Arrays.hashCode(this.c);
        }

        public String toString() {
            String encodeToString = Base64.encodeToString(this.b, 2);
            String encodeToString2 = Base64.encodeToString(this.a, 2);
            String encodeToString3 = Base64.encodeToString(this.c, 2);
            return StringUtil.format(encodeToString + ":" + encodeToString3 + ":" + encodeToString2, new Object[0]);
        }

        public static byte[] a(byte[] bArr, byte[] bArr2) {
            byte[] bArr3 = new byte[(bArr.length + bArr2.length)];
            System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
            System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
            return bArr3;
        }

        public C0035a(String str) {
            String[] split = str.split(":");
            if (split.length == 3) {
                this.b = Base64.decode(split[0], 2);
                this.c = Base64.decode(split[1], 2);
                this.a = Base64.decode(split[2], 2);
                return;
            }
            throw new IllegalArgumentException("Cannot parse iv:ciphertext:mac");
        }
    }

    public static final class b {
        private static final byte[] a = e();

        /* renamed from: io.dcloud.e.f.a$b$a  reason: collision with other inner class name */
        public static class C0036a extends SecureRandomSpi {
            private static final File a = new File("/dev/urandom");
            private static final Object b = new Object();
            private static DataInputStream c;
            private static OutputStream d;
            private boolean e;

            private DataInputStream a() {
                DataInputStream dataInputStream;
                synchronized (b) {
                    if (c == null) {
                        try {
                            c = new DataInputStream(new FileInputStream(a));
                        } catch (IOException e2) {
                            throw new SecurityException("Failed to open " + a + " for reading", e2);
                        }
                    }
                    dataInputStream = c;
                }
                return dataInputStream;
            }

            private OutputStream b() throws IOException {
                OutputStream outputStream;
                synchronized (b) {
                    if (d == null) {
                        d = new FileOutputStream(a);
                    }
                    outputStream = d;
                }
                return outputStream;
            }

            /* access modifiers changed from: protected */
            public byte[] engineGenerateSeed(int i) {
                byte[] bArr = new byte[i];
                engineNextBytes(bArr);
                return bArr;
            }

            /* access modifiers changed from: protected */
            public void engineNextBytes(byte[] bArr) {
                DataInputStream a2;
                if (!this.e) {
                    engineSetSeed(b.d());
                }
                try {
                    synchronized (b) {
                        a2 = a();
                    }
                    synchronized (a2) {
                        a2.readFully(bArr);
                    }
                } catch (IOException e2) {
                    throw new SecurityException("Failed to read from " + a, e2);
                }
            }

            /* access modifiers changed from: protected */
            public void engineSetSeed(byte[] bArr) {
                OutputStream b2;
                try {
                    synchronized (b) {
                        b2 = b();
                    }
                    b2.write(bArr);
                    b2.flush();
                } catch (IOException unused) {
                    try {
                        String simpleName = b.class.getSimpleName();
                        Log.w(simpleName, "Failed to mix seed into " + a);
                    } catch (Throwable th) {
                        this.e = true;
                        throw th;
                    }
                }
                this.e = true;
            }
        }

        /* renamed from: io.dcloud.e.f.a$b$b  reason: collision with other inner class name */
        private static class C0037b extends Provider {
            public C0037b() {
                super("LinuxPRNG", 1.0d, "A Linux-specific random number provider that uses /dev/urandom");
                put("SecureRandom.SHA1PRNG", C0036a.class.getName());
                put("SecureRandom.SHA1PRNG ImplementedIn", ExifInterface.TAG_SOFTWARE);
            }
        }

        private b() {
        }

        public static void b() {
            c();
            g();
        }

        private static void c() throws SecurityException {
            int i = Build.VERSION.SDK_INT;
            if (i >= 16 && i <= 18) {
                try {
                    Class.forName("org.apache.harmony.xnet.provider.jsse.NativeCrypto").getMethod("RAND_seed", new Class[]{byte[].class}).invoke((Object) null, new Object[]{d()});
                    Class<?> cls = Class.forName("org.apache.harmony.xnet.provider.jsse.NativeCrypto");
                    int intValue = ((Integer) cls.getMethod("RAND_load_file", new Class[]{String.class, Long.TYPE}).invoke((Object) null, new Object[]{"/dev/urandom", 1024})).intValue();
                    if (intValue != 1024) {
                        throw new IOException("Unexpected number of bytes read from Linux PRNG: " + intValue);
                    }
                } catch (Exception e) {
                    throw new SecurityException("Failed to seed OpenSSL PRNG", e);
                }
            }
        }

        /* access modifiers changed from: private */
        public static byte[] d() {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                dataOutputStream.writeLong(System.currentTimeMillis());
                dataOutputStream.writeLong(System.nanoTime());
                dataOutputStream.writeInt(Process.myPid());
                dataOutputStream.writeInt(Process.myUid());
                dataOutputStream.write(a);
                dataOutputStream.close();
                return byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                throw new SecurityException("Failed to generate seed", e);
            }
        }

        private static byte[] e() {
            StringBuilder sb = new StringBuilder();
            String str = Build.FINGERPRINT;
            if (str != null) {
                sb.append(str);
            }
            String f = f();
            if (f != null) {
                sb.append(f);
            }
            try {
                return sb.toString().getBytes("UTF-8");
            } catch (UnsupportedEncodingException unused) {
                throw new RuntimeException("UTF-8 encoding not supported");
            }
        }

        private static String f() {
            try {
                return (String) Build.class.getField("SERIAL").get((Object) null);
            } catch (Exception unused) {
                return null;
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:11:0x002b, code lost:
            if (r0[0].getClass().getSimpleName().equals(io.dcloud.e.f.a.b.C0037b.class.getSimpleName()) != false) goto L_0x0035;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private static void g() throws java.lang.SecurityException {
            /*
                int r0 = android.os.Build.VERSION.SDK_INT
                r1 = 18
                if (r0 <= r1) goto L_0x0007
                return
            L_0x0007:
                java.lang.String r0 = "SecureRandom.SHA1PRNG"
                java.security.Provider[] r0 = java.security.Security.getProviders(r0)
                java.lang.Class<java.security.Security> r1 = java.security.Security.class
                monitor-enter(r1)
                r2 = 1
                if (r0 == 0) goto L_0x002d
                int r3 = r0.length     // Catch:{ all -> 0x00ba }
                if (r3 < r2) goto L_0x002d
                r3 = 0
                r0 = r0[r3]     // Catch:{ all -> 0x00ba }
                java.lang.Class r0 = r0.getClass()     // Catch:{ all -> 0x00ba }
                java.lang.String r0 = r0.getSimpleName()     // Catch:{ all -> 0x00ba }
                java.lang.Class<io.dcloud.e.f.a$b$b> r3 = io.dcloud.e.f.a.b.C0037b.class
                java.lang.String r3 = r3.getSimpleName()     // Catch:{ all -> 0x00ba }
                boolean r0 = r0.equals(r3)     // Catch:{ all -> 0x00ba }
                if (r0 != 0) goto L_0x0035
            L_0x002d:
                io.dcloud.e.f.a$b$b r0 = new io.dcloud.e.f.a$b$b     // Catch:{ all -> 0x00ba }
                r0.<init>()     // Catch:{ all -> 0x00ba }
                java.security.Security.insertProviderAt(r0, r2)     // Catch:{ all -> 0x00ba }
            L_0x0035:
                java.security.SecureRandom r0 = new java.security.SecureRandom     // Catch:{ all -> 0x00ba }
                r0.<init>()     // Catch:{ all -> 0x00ba }
                java.security.Provider r2 = r0.getProvider()     // Catch:{ all -> 0x00ba }
                java.lang.Class r2 = r2.getClass()     // Catch:{ all -> 0x00ba }
                java.lang.String r2 = r2.getSimpleName()     // Catch:{ all -> 0x00ba }
                java.lang.Class<io.dcloud.e.f.a$b$b> r3 = io.dcloud.e.f.a.b.C0037b.class
                java.lang.String r3 = r3.getSimpleName()     // Catch:{ all -> 0x00ba }
                boolean r2 = r2.equals(r3)     // Catch:{ all -> 0x00ba }
                if (r2 == 0) goto L_0x009b
                r0 = 0
                java.lang.String r2 = "SHA1PRNG"
                java.security.SecureRandom r0 = java.security.SecureRandom.getInstance(r2)     // Catch:{ NoSuchAlgorithmException -> 0x005a }
                goto L_0x0062
            L_0x005a:
                r2 = move-exception
                java.lang.SecurityException r3 = new java.lang.SecurityException     // Catch:{ all -> 0x00ba }
                java.lang.String r4 = "SHA1PRNG not available"
                r3.<init>(r4, r2)     // Catch:{ all -> 0x00ba }
            L_0x0062:
                java.security.Provider r2 = r0.getProvider()     // Catch:{ all -> 0x00ba }
                java.lang.Class r2 = r2.getClass()     // Catch:{ all -> 0x00ba }
                java.lang.String r2 = r2.getSimpleName()     // Catch:{ all -> 0x00ba }
                java.lang.Class<io.dcloud.e.f.a$b$b> r3 = io.dcloud.e.f.a.b.C0037b.class
                java.lang.String r3 = r3.getSimpleName()     // Catch:{ all -> 0x00ba }
                boolean r2 = r2.equals(r3)     // Catch:{ all -> 0x00ba }
                if (r2 == 0) goto L_0x007c
                monitor-exit(r1)     // Catch:{ all -> 0x00ba }
                return
            L_0x007c:
                java.lang.SecurityException r2 = new java.lang.SecurityException     // Catch:{ all -> 0x00ba }
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ba }
                r3.<init>()     // Catch:{ all -> 0x00ba }
                java.lang.String r4 = "SecureRandom.getInstance(\"SHA1PRNG\") backed by wrong Provider: "
                r3.append(r4)     // Catch:{ all -> 0x00ba }
                java.security.Provider r0 = r0.getProvider()     // Catch:{ all -> 0x00ba }
                java.lang.Class r0 = r0.getClass()     // Catch:{ all -> 0x00ba }
                r3.append(r0)     // Catch:{ all -> 0x00ba }
                java.lang.String r0 = r3.toString()     // Catch:{ all -> 0x00ba }
                r2.<init>(r0)     // Catch:{ all -> 0x00ba }
                throw r2     // Catch:{ all -> 0x00ba }
            L_0x009b:
                java.lang.SecurityException r2 = new java.lang.SecurityException     // Catch:{ all -> 0x00ba }
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ba }
                r3.<init>()     // Catch:{ all -> 0x00ba }
                java.lang.String r4 = "new SecureRandom() backed by wrong Provider: "
                r3.append(r4)     // Catch:{ all -> 0x00ba }
                java.security.Provider r0 = r0.getProvider()     // Catch:{ all -> 0x00ba }
                java.lang.Class r0 = r0.getClass()     // Catch:{ all -> 0x00ba }
                r3.append(r0)     // Catch:{ all -> 0x00ba }
                java.lang.String r0 = r3.toString()     // Catch:{ all -> 0x00ba }
                r2.<init>(r0)     // Catch:{ all -> 0x00ba }
                throw r2     // Catch:{ all -> 0x00ba }
            L_0x00ba:
                r0 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x00ba }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.e.f.a.b.g():void");
        }
    }

    public static class c {
        private Key a;
        private Key b;

        public c(Key key, Key key2) {
            a(key);
            b(key2);
        }

        public Key a() {
            return this.a;
        }

        public Key b() {
            return this.b;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || c.class != obj.getClass()) {
                return false;
            }
            c cVar = (c) obj;
            return this.b.equals(cVar.b) && this.a.equals(cVar.a);
        }

        public int hashCode() {
            return ((this.a.hashCode() + 31) * 31) + this.b.hashCode();
        }

        public String toString() {
            return Base64.encodeToString(a().getEncoded(), 2) + ":" + Base64.encodeToString(b().getEncoded(), 2);
        }

        public void a(Key key) {
            this.a = key;
        }

        public void b(Key key) {
            this.b = key;
        }
    }

    public static c a(String str) throws InvalidKeyException {
        String[] split = str.split(":");
        if (split.length == 2) {
            byte[] decode = Base64.decode(split[0], 2);
            if (decode.length == 16) {
                byte[] decode2 = Base64.decode(split[1], 2);
                if (decode2.length == 32) {
                    return new c(new SecretKeySpec(decode, 0, decode.length, "AES"), new SecretKeySpec(decode2, d()));
                }
                throw new InvalidKeyException("Base64 decoded key is not 256 bytes");
            }
            throw new InvalidKeyException("Base64 decoded key is not 128 bytes");
        }
        throw new IllegalArgumentException("Cannot parse aesKey:hmacKey");
    }

    public static byte[] b() throws GeneralSecurityException {
        return a(16);
    }

    public static c c() throws GeneralSecurityException {
        a();
        KeyGenerator instance = KeyGenerator.getInstance("AES");
        instance.init(128);
        return new c(instance.generateKey(), new SecretKeySpec(a(32), d()));
    }

    private static String d() {
        return io.dcloud.common.util.Base64.decodeString("##U1d4Z1lsSkpRRE0wTnc9PSo2YTNkODhmYS00YmEwLTQ3OWYtOTQyMi1lNWFhYmUxNTg5N2I2NQ==", true, 1);
    }

    public static String b(C0035a aVar, c cVar) throws UnsupportedEncodingException, GeneralSecurityException {
        return a(aVar, cVar, "UTF-8");
    }

    public static c a(String str, byte[] bArr, int i) throws GeneralSecurityException {
        a();
        byte[] encoded = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(new PBEKeySpec(str.toCharArray(), bArr, i, BitmapCounterConfig.DEFAULT_MAX_BITMAP_COUNT)).getEncoded();
        return new c(new SecretKeySpec(a(encoded, 0, 16), "AES"), new SecretKeySpec(a(encoded, 16, 48), d()));
    }

    private static byte[] a(int i) throws GeneralSecurityException {
        a();
        byte[] bArr = new byte[i];
        new SecureRandom().nextBytes(bArr);
        return bArr;
    }

    public static C0035a a(String str, c cVar) throws UnsupportedEncodingException, GeneralSecurityException {
        return a(str, cVar, "UTF-8");
    }

    public static C0035a a(String str, c cVar, String str2) throws UnsupportedEncodingException, GeneralSecurityException {
        return a(str.getBytes(str2), cVar);
    }

    public static C0035a a(byte[] bArr, c cVar) throws GeneralSecurityException {
        byte[] b2 = b();
        Cipher instance = Cipher.getInstance(AESUtil.getDefaultTransformation());
        instance.init(1, cVar.a(), new IvParameterSpec(b2));
        byte[] iv = instance.getIV();
        byte[] doFinal = instance.doFinal(bArr);
        return new C0035a(doFinal, iv, a(C0035a.a(iv, doFinal), cVar.b()));
    }

    private static void a() {
        AtomicBoolean atomicBoolean = a;
        if (!atomicBoolean.get()) {
            synchronized (b.class) {
                if (!atomicBoolean.get()) {
                    b.b();
                    atomicBoolean.set(true);
                }
            }
        }
    }

    public static String a(C0035a aVar, c cVar, String str) throws UnsupportedEncodingException, GeneralSecurityException {
        return new String(a(aVar, cVar), str);
    }

    public static byte[] a(C0035a aVar, c cVar) throws GeneralSecurityException {
        if (a(a(C0035a.a(aVar.b(), aVar.a()), cVar.b()), aVar.c())) {
            Cipher instance = Cipher.getInstance(AESUtil.getDefaultTransformation());
            instance.init(2, cVar.a(), new IvParameterSpec(aVar.b()));
            return instance.doFinal(aVar.a());
        }
        throw new GeneralSecurityException("MAC stored in civ does not match computed MAC.");
    }

    public static byte[] a(byte[] bArr, Key key) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac instance = Mac.getInstance(d());
        instance.init(key);
        return instance.doFinal(bArr);
    }

    public static boolean a(byte[] bArr, byte[] bArr2) {
        if (bArr.length != bArr2.length) {
            return false;
        }
        byte b2 = 0;
        for (int i = 0; i < bArr.length; i++) {
            b2 |= bArr[i] ^ bArr2[i];
        }
        if (b2 == 0) {
            return true;
        }
        return false;
    }

    private static byte[] a(byte[] bArr, int i, int i2) {
        int i3 = i2 - i;
        byte[] bArr2 = new byte[i3];
        System.arraycopy(bArr, i, bArr2, 0, i3);
        return bArr2;
    }
}
