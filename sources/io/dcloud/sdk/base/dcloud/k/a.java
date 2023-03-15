package io.dcloud.sdk.base.dcloud.k;

import android.content.Context;
import com.facebook.imagepipeline.producers.HttpUrlConnectionNetworkFetcher;
import io.dcloud.h.a.e.d;
import io.dcloud.sdk.base.dcloud.h;
import java.io.InputStream;

public class a implements Runnable {
    private C0075a a;
    private Context b;
    private String c;
    private String d;

    /* renamed from: io.dcloud.sdk.base.dcloud.k.a$a  reason: collision with other inner class name */
    public interface C0075a {
        void a(a aVar);

        void b(a aVar);
    }

    public void a(Context context, String str, String str2) {
        this.b = context;
        this.c = str;
        this.d = str2;
    }

    public String b() {
        return this.d;
    }

    public String c() {
        return this.c;
    }

    public void run() {
        InputStream a2 = d.a(this.c, (int) HttpUrlConnectionNetworkFetcher.HTTP_DEFAULT_TIMEOUT, true, new String[1]);
        C0075a aVar = this.a;
        if (aVar == null) {
            return;
        }
        if (a2 != null) {
            h.a(a2, this.d);
            this.a.b(this);
            return;
        }
        aVar.a(this);
    }

    public void a(C0075a aVar) {
        this.a = aVar;
    }

    public Context a() {
        return this.b;
    }
}
