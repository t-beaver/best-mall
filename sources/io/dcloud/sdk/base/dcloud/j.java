package io.dcloud.sdk.base.dcloud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import io.dcloud.WebAppActivity;
import io.dcloud.base.R;
import io.dcloud.sdk.base.dcloud.ADHandler;
import io.dcloud.sdk.core.util.ReflectUtil;
import org.json.JSONObject;

public class j {
    f a = null;
    ADHandler.g b;
    View c = null;
    View d = null;
    boolean e = false;
    boolean f = false;
    boolean g = false;
    Context h;
    BroadcastReceiver i = new b();
    /* access modifiers changed from: private */
    public final Runnable j = new Runnable() {
        public final void run() {
            j.this.a();
        }
    };

    class a implements i {
        final /* synthetic */ Context a;

        a(Context context) {
            this.a = context;
        }

        public void onReceiver(JSONObject jSONObject) {
            ADHandler.a("shutao", "listenADReceive----------------onReceiver-");
            j jVar = j.this;
            if (!jVar.f) {
                Context context = jVar.h;
                ADHandler.g gVar = jVar.b;
                ADHandler.a(context, gVar.h, gVar);
                if (j.this.b.a()) {
                    j.this.c();
                    ADHandler.a("shutao", "initAdMainView");
                    return;
                }
                ADHandler.a("shutao", "setImageDownlaodListen");
                j.this.a(this.a);
            }
        }
    }

    class b extends BroadcastReceiver {
        b() {
        }

        public void onReceive(Context context, Intent intent) {
            ADHandler.a("shutao", "imageDownloadReceiver----shou");
            if (!j.this.f) {
                String stringExtra = intent.getStringExtra("src");
                ADHandler.a("shutao", "imageDownloadReceiver--src=" + stringExtra);
                if (!TextUtils.isEmpty(stringExtra) && j.this.b.c() != null && j.this.b.b().optString("src").equalsIgnoreCase(stringExtra)) {
                    if (intent.getBooleanExtra("downloadImage", false)) {
                        ADHandler.a("shutao", "imageDownloadReceiver--下载成功=");
                        ADHandler.g gVar = j.this.b;
                        ADHandler.a(context, gVar.h, gVar);
                        if (j.this.b.a()) {
                            j.this.c();
                        } else {
                            j.this.a();
                        }
                    } else {
                        j.this.a();
                    }
                }
            }
        }
    }

    class c implements View.OnClickListener {
        final /* synthetic */ Context a;

        c(Context context) {
            this.a = context;
        }

        public void onClick(View view) {
            try {
                j.this.b.c().put("down_x", Math.round(j.this.b.a.getX()));
                j.this.b.c().put("down_y", Math.round(j.this.b.a.getY()));
                j.this.b.c().put("up_x", Math.round(j.this.b.b.getX()));
                j.this.b.c().put("up_y", (double) j.this.b.b.getY());
                j.this.b.c().put("relative_down_x", Math.round(j.this.b.a.getX() - view.getX()));
                j.this.b.c().put("relative_down_y", Math.round(j.this.b.a.getY() - view.getY()));
                j.this.b.c().put("relative_up_x", Math.round(j.this.b.b.getX() - view.getX()));
                j.this.b.c().put("relative_up_y", Math.round(j.this.b.b.getY() - view.getY()));
                j.this.b.c().put("dw", io.dcloud.h.a.a.b(this.a));
                j.this.b.c().put("dh", io.dcloud.h.a.a.a(this.a));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Context context = this.a;
            ADHandler.a(context, j.this.b, ADHandler.a(context, "adid"));
            view.setOnClickListener((View.OnClickListener) null);
            j jVar = j.this;
            ADHandler.g gVar = jVar.b;
            gVar.a = null;
            gVar.b = null;
            jVar.a.onClicked();
        }
    }

    class d implements View.OnTouchListener {
        d() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                j.this.b.a = motionEvent;
                return false;
            } else if (motionEvent.getAction() != 1) {
                return false;
            } else {
                j.this.b.b = motionEvent;
                return false;
            }
        }
    }

    class e implements View.OnClickListener {
        e() {
        }

        public void onClick(View view) {
            j jVar = j.this;
            jVar.d.removeCallbacks(jVar.j);
            j.this.a();
        }
    }

    public j(Context context, ADHandler.g gVar, ViewGroup viewGroup, f fVar) {
        this.a = fVar;
        this.b = gVar;
        this.h = context;
        View inflate = LayoutInflater.from(context).inflate(R.layout.dcloud_ad_main_container, (ViewGroup) null);
        this.d = inflate;
        viewGroup.addView(inflate);
        this.c = (TextView) this.d.findViewById(R.id.ad_dcloud_main_skip);
        if (this.b.a()) {
            ADHandler.a("SplashADViewWrapper", "use cache AdData");
            a(context, this.d);
            return;
        }
        ADHandler.a("shutao", "listenADReceive-----------------");
        this.b.a(context, new a(context));
    }

    /* access modifiers changed from: private */
    public void c() {
        if (this.e) {
            this.d.removeCallbacks(this.j);
            this.d.postDelayed(this.j, 2000);
            ADHandler.a("shutao", "runInitMainView--延时2000");
        }
        a(this.h, this.d);
    }

    public void a(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ad_img_downlaod_receive");
        LocalBroadcastManager.getInstance(context).registerReceiver(this.i, intentFilter);
        this.g = true;
    }

    public void b() {
        ADHandler.a("ADReceive", "onWillCloseSplash ");
        this.e = true;
        if (this.c != null && this.b.a()) {
            this.c.setVisibility(0);
        }
        ADHandler.a("shutao", "Delayed---30000");
        this.d.postDelayed(this.j, WebAppActivity.SPLASH_SECOND);
    }

    private void a(Context context, View view) {
        ADHandler.a("ADReceive", "initAdMainView ");
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.ad_dcloud_main_img);
        frameLayout.setOnClickListener(new c(context));
        frameLayout.setOnTouchListener(new d());
        Object obj = this.b.e;
        if (obj instanceof Bitmap) {
            ADHandler.g gVar = this.b;
            frameLayout.addView(new g(context, (Bitmap) gVar.e, gVar), -1, -1);
        } else if (obj instanceof Drawable) {
            ImageView imageView = (ImageView) ReflectUtil.newInstance("pl.droidsonroids.gif.GifImageView", new Class[]{Context.class}, new Object[]{context});
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageDrawable((Drawable) this.b.e);
            frameLayout.addView(imageView, -1, -1);
        }
        if (this.e) {
            this.c.setVisibility(0);
        }
        this.c.setOnClickListener(new e());
        ADHandler.c(context, this.b, ADHandler.a(context, "adid"));
        this.a.onShow();
    }

    /* access modifiers changed from: private */
    public void a() {
        ADHandler.a("shutao", "onFinishShow");
        this.a.onFinishShow();
        View view = this.d;
        if (!(view == null || view.getParent() == null)) {
            ((ViewGroup) this.d.getParent()).removeView(this.d);
        }
        this.f = true;
        Object obj = this.b.e;
        if (obj instanceof Bitmap) {
            Bitmap bitmap = (Bitmap) obj;
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        this.e = false;
        if (this.g) {
            LocalBroadcastManager.getInstance(this.h).unregisterReceiver(this.i);
        }
    }
}
