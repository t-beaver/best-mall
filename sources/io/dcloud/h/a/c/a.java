package io.dcloud.h.a.c;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import io.dcloud.sdk.base.entry.AdData;

public abstract class a {
    protected c a;
    private Context b;
    /* access modifiers changed from: protected */
    public String c;
    /* access modifiers changed from: protected */
    public AdData d;

    /* renamed from: io.dcloud.h.a.c.a$a  reason: collision with other inner class name */
    class C0047a implements Runnable {
        final /* synthetic */ int a;
        final /* synthetic */ String b;

        C0047a(int i, String str) {
            this.a = i;
            this.b = str;
        }

        public void run() {
            a.this.a.onError(this.a, this.b);
        }
    }

    class b implements Runnable {
        b() {
        }

        public void run() {
            a.this.a.onSplashAdLoad();
        }
    }

    public interface c {
        void a();

        void a(int i, String str);

        void b();

        void c();

        void d();

        void onError(int i, String str);

        void onSplashAdLoad();
    }

    public a(c cVar, Context context, String str) {
        this.a = cVar;
        this.b = context;
        this.c = str;
    }

    public void a(ViewGroup viewGroup) {
        if (viewGroup == null) {
            a(60010, "广告容器不可见");
        } else if (this.d == null) {
            a(60005, "数据解析失败");
        } else {
            new io.dcloud.h.a.d.c.b(viewGroup.getContext(), this.a, this.d).a(viewGroup);
        }
    }

    public Context b() {
        return this.b;
    }

    /* access modifiers changed from: protected */
    public void a(int i, String str) {
        if (this.a != null) {
            new Handler(Looper.getMainLooper()).post(new C0047a(i, str));
        }
    }

    /* access modifiers changed from: protected */
    public void a() {
        if (this.a != null) {
            new Handler(Looper.getMainLooper()).post(new b());
        }
    }
}
