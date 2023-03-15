package com.nostra13.dcloudimageloader.core.download;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import com.nostra13.dcloudimageloader.core.download.ImageDownloader;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import org.apache.http.conn.ssl.SSLSocketFactory;

public class BaseImageDownloader implements ImageDownloader {
    protected static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    protected static final int BUFFER_SIZE = 32768;
    public static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 5000;
    public static final int DEFAULT_HTTP_READ_TIMEOUT = 20000;
    private static final String ERROR_UNSUPPORTED_SCHEME = "UIL doesn't support scheme(protocol) by default [%s]. You should implement this support yourself (BaseImageDownloader.getStreamFromOtherSource(...))";
    protected static final int MAX_REDIRECT_COUNT = 5;
    protected final int connectTimeout;
    protected final Context context;
    protected final int readTimeout;

    /* renamed from: com.nostra13.dcloudimageloader.core.download.BaseImageDownloader$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$nostra13$dcloudimageloader$core$download$ImageDownloader$Scheme;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|(3:13|14|16)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|16) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.nostra13.dcloudimageloader.core.download.ImageDownloader$Scheme[] r0 = com.nostra13.dcloudimageloader.core.download.ImageDownloader.Scheme.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$nostra13$dcloudimageloader$core$download$ImageDownloader$Scheme = r0
                com.nostra13.dcloudimageloader.core.download.ImageDownloader$Scheme r1 = com.nostra13.dcloudimageloader.core.download.ImageDownloader.Scheme.HTTP     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$nostra13$dcloudimageloader$core$download$ImageDownloader$Scheme     // Catch:{ NoSuchFieldError -> 0x001d }
                com.nostra13.dcloudimageloader.core.download.ImageDownloader$Scheme r1 = com.nostra13.dcloudimageloader.core.download.ImageDownloader.Scheme.HTTPS     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$nostra13$dcloudimageloader$core$download$ImageDownloader$Scheme     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.nostra13.dcloudimageloader.core.download.ImageDownloader$Scheme r1 = com.nostra13.dcloudimageloader.core.download.ImageDownloader.Scheme.FILE     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$nostra13$dcloudimageloader$core$download$ImageDownloader$Scheme     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.nostra13.dcloudimageloader.core.download.ImageDownloader$Scheme r1 = com.nostra13.dcloudimageloader.core.download.ImageDownloader.Scheme.CONTENT     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$com$nostra13$dcloudimageloader$core$download$ImageDownloader$Scheme     // Catch:{ NoSuchFieldError -> 0x003e }
                com.nostra13.dcloudimageloader.core.download.ImageDownloader$Scheme r1 = com.nostra13.dcloudimageloader.core.download.ImageDownloader.Scheme.ASSETS     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$com$nostra13$dcloudimageloader$core$download$ImageDownloader$Scheme     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.nostra13.dcloudimageloader.core.download.ImageDownloader$Scheme r1 = com.nostra13.dcloudimageloader.core.download.ImageDownloader.Scheme.DRAWABLE     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$com$nostra13$dcloudimageloader$core$download$ImageDownloader$Scheme     // Catch:{ NoSuchFieldError -> 0x0054 }
                com.nostra13.dcloudimageloader.core.download.ImageDownloader$Scheme r1 = com.nostra13.dcloudimageloader.core.download.ImageDownloader.Scheme.UNKNOWN     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.nostra13.dcloudimageloader.core.download.BaseImageDownloader.AnonymousClass1.<clinit>():void");
        }
    }

    public BaseImageDownloader(Context context2) {
        this.context = context2.getApplicationContext();
        this.connectTimeout = DEFAULT_HTTP_CONNECT_TIMEOUT;
        this.readTimeout = DEFAULT_HTTP_READ_TIMEOUT;
    }

    private Object newInstance(String str, Class[] clsArr, Object[] objArr) {
        try {
            return Class.forName(str).getConstructor(clsArr).newInstance(objArr);
        } catch (Exception unused) {
            return null;
        }
    }

    private void trustAllHosts(HttpsURLConnection httpsURLConnection) {
        try {
            SSLContext instance = SSLContext.getInstance("TLS");
            Object newInstance = newInstance(this.context.getPackageName() + ".CustomTrustMgr", (Class[]) null, (Object[]) null);
            if (newInstance != null) {
                instance.init((KeyManager[]) null, new TrustManager[]{(TrustManager) newInstance}, new SecureRandom());
                httpsURLConnection.setSSLSocketFactory(instance.getSocketFactory());
                httpsURLConnection.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public HttpURLConnection createConnection(String str, Object obj) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Uri.encode(str, ALLOWED_URI_CHARS)).openConnection();
        if (str.startsWith("https")) {
            trustAllHosts((HttpsURLConnection) httpURLConnection);
        }
        httpURLConnection.setConnectTimeout(this.connectTimeout);
        httpURLConnection.setReadTimeout(this.readTimeout);
        return httpURLConnection;
    }

    public InputStream getStream(String str, Object obj) throws IOException {
        switch (AnonymousClass1.$SwitchMap$com$nostra13$dcloudimageloader$core$download$ImageDownloader$Scheme[ImageDownloader.Scheme.ofUri(str).ordinal()]) {
            case 1:
            case 2:
                return getStreamFromNetwork(str, obj);
            case 3:
                return getStreamFromFile(str, obj);
            case 4:
                return getStreamFromContent(str, obj);
            case 5:
                return getStreamFromAssets(str, obj);
            case 6:
                return getStreamFromDrawable(str, obj);
            default:
                return getStreamFromOtherSource(str, obj);
        }
    }

    /* access modifiers changed from: protected */
    public InputStream getStreamFromAssets(String str, Object obj) throws IOException {
        return this.context.getAssets().open(ImageDownloader.Scheme.ASSETS.crop(str));
    }

    /* access modifiers changed from: protected */
    public InputStream getStreamFromContent(String str, Object obj) throws FileNotFoundException {
        return this.context.getContentResolver().openInputStream(Uri.parse(str));
    }

    /* access modifiers changed from: protected */
    public InputStream getStreamFromDrawable(String str, Object obj) {
        Bitmap bitmap = ((BitmapDrawable) this.context.getResources().getDrawable(Integer.parseInt(ImageDownloader.Scheme.DRAWABLE.crop(str)))).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    /* access modifiers changed from: protected */
    public InputStream getStreamFromFile(String str, Object obj) throws IOException {
        return new BufferedInputStream(new FileInputStream(ImageDownloader.Scheme.FILE.crop(str)), 1195);
    }

    /* access modifiers changed from: protected */
    public InputStream getStreamFromNetwork(String str, Object obj) throws IOException {
        HttpURLConnection createConnection = createConnection(str, obj);
        int i = 0;
        while (createConnection.getResponseCode() / 100 == 3 && i < 5) {
            createConnection = createConnection(createConnection.getHeaderField("Location"), obj);
            i++;
        }
        return new BufferedInputStream(createConnection.getInputStream(), 1195);
    }

    /* access modifiers changed from: protected */
    public InputStream getStreamFromOtherSource(String str, Object obj) throws IOException {
        throw new UnsupportedOperationException(String.format(ERROR_UNSUPPORTED_SCHEME, new Object[]{str}));
    }

    public BaseImageDownloader(Context context2, int i, int i2) {
        this.context = context2.getApplicationContext();
        this.connectTimeout = i;
        this.readTimeout = i2;
    }
}
