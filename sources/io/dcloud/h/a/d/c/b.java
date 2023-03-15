package io.dcloud.h.a.d.c;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nostra13.dcloudimageloader.core.download.BaseImageDownloader;
import io.dcloud.base.R;
import io.dcloud.h.a.c.a;
import io.dcloud.h.a.d.b.g;
import io.dcloud.sdk.base.entry.AdData;
import java.io.IOException;
import java.util.Locale;
import pl.droidsonroids.gif.GifDrawable;

public class b extends RelativeLayout implements View.OnClickListener {
    Drawable a;
    private ViewGroup b;
    private View c;
    /* access modifiers changed from: private */
    public ImageView d;
    private TextView e;
    /* access modifiers changed from: private */
    public a.c f;
    /* access modifiers changed from: private */
    public AdData g;
    private int h = BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;

    class a implements Runnable {
        a() {
        }

        public void run() {
            if (b.this.f != null) {
                b.this.f.b();
            }
        }
    }

    /* renamed from: io.dcloud.h.a.d.c.b$b  reason: collision with other inner class name */
    class C0050b implements Runnable {
        final /* synthetic */ ViewGroup a;

        C0050b(ViewGroup viewGroup) {
            this.a = viewGroup;
        }

        public void run() {
            if (io.dcloud.h.a.e.a.a(this.a)) {
                b bVar = b.this;
                if (bVar.a != null) {
                    this.a.removeAllViews();
                    this.a.getGlobalVisibleRect(new Rect());
                    this.a.getRootView().getGlobalVisibleRect(new Rect());
                    this.a.addView(b.this, new ViewGroup.LayoutParams(-1, -1));
                    b.this.g.a(new RectF(this.a.getX(), this.a.getY(), 0.0f, 0.0f));
                    b.this.d.setImageDrawable(b.this.a);
                    if (b.this.f != null) {
                        b.this.f.d();
                    }
                    b.this.g.a();
                    g.a().b(b.this.getContext(), b.this.g.k());
                } else if (bVar.f != null) {
                    b.this.f.a(60004, "图片资源加载失败");
                }
            } else if (b.this.f != null) {
                b.this.f.a(60010, "广告容器不可见");
            }
        }
    }

    public b(Context context, a.c cVar, AdData adData) {
        super(context);
        this.f = cVar;
        this.g = adData;
        this.h = adData.i();
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.dcloud_ad_splash_container, (ViewGroup) null);
        this.b = viewGroup;
        this.c = viewGroup.findViewById(R.id.ad_dcloud_main_skip);
        this.d = (ImageView) this.b.findViewById(R.id.ad_dcloud_main_img);
        this.e = (TextView) this.b.findViewById(R.id.ad_dcloud_main_click);
        this.d.setOnClickListener(this);
        this.c.setOnClickListener(this);
        addView(this.b, -1);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        if (adData.g() == null) {
            BitmapFactory.decodeFile(adData.f(), options);
            String str = options.outMimeType;
            if (TextUtils.isEmpty(str) || !str.toLowerCase(Locale.ENGLISH).contains("gif")) {
                this.a = new a(BitmapFactory.decodeFile(adData.f()), getContext());
                return;
            }
            try {
                this.a = new GifDrawable(adData.f());
            } catch (IOException unused) {
                this.a = new a(BitmapFactory.decodeFile(adData.f()), getContext());
            }
        } else {
            BitmapFactory.decodeByteArray(adData.g(), 0, adData.g().length, options);
            String str2 = options.outMimeType;
            if (TextUtils.isEmpty(str2) || !str2.toLowerCase(Locale.ENGLISH).contains("gif")) {
                this.a = new a(BitmapFactory.decodeByteArray(adData.g(), 0, adData.g().length), getContext());
                return;
            }
            try {
                this.a = new GifDrawable(adData.g());
            } catch (IOException unused2) {
                this.a = new a(BitmapFactory.decodeByteArray(adData.g(), 0, adData.g().length), getContext());
            }
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.g.a(motionEvent);
        } else if (motionEvent.getAction() == 1) {
            this.g.b(motionEvent);
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.ad_dcloud_main_skip) {
            a.c cVar = this.f;
            if (cVar != null) {
                cVar.c();
            }
        } else if (view.getId() == R.id.ad_dcloud_main_img) {
            a.c cVar2 = this.f;
            if (cVar2 != null) {
                cVar2.a();
            }
            this.g.a(getContext());
        }
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
    }

    public void a(ViewGroup viewGroup) {
        if (viewGroup != null) {
            viewGroup.postDelayed(new a(), (long) this.h);
            viewGroup.postDelayed(new C0050b(viewGroup), 50);
        }
    }
}
