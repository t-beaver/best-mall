package io.dcloud.feature.gallery.imageedit;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.ViewSwitcher;
import io.dcloud.base.R;
import io.dcloud.feature.gallery.imageedit.b;
import io.dcloud.feature.gallery.imageedit.c.a;
import io.dcloud.feature.gallery.imageedit.view.IMGColorGroup;
import io.dcloud.feature.gallery.imageedit.view.IMGView;

abstract class a extends Activity implements View.OnClickListener, b.a, RadioGroup.OnCheckedChangeListener, DialogInterface.OnShowListener, DialogInterface.OnDismissListener, a.b {
    protected IMGView a;
    private RadioGroup b;
    private IMGColorGroup c;
    private b d;
    private View e;
    private View f;
    private View g;
    private ViewSwitcher h;
    private ViewSwitcher i;

    /* renamed from: io.dcloud.feature.gallery.imageedit.a$a  reason: collision with other inner class name */
    static /* synthetic */ class C0038a {
        static final /* synthetic */ int[] a;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                io.dcloud.feature.gallery.imageedit.c.b[] r0 = io.dcloud.feature.gallery.imageedit.c.b.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                a = r0
                io.dcloud.feature.gallery.imageedit.c.b r1 = io.dcloud.feature.gallery.imageedit.c.b.DOODLE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x001d }
                io.dcloud.feature.gallery.imageedit.c.b r1 = io.dcloud.feature.gallery.imageedit.c.b.MOSAIC     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0028 }
                io.dcloud.feature.gallery.imageedit.c.b r1 = io.dcloud.feature.gallery.imageedit.c.b.NONE     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.gallery.imageedit.a.C0038a.<clinit>():void");
        }
    }

    a() {
    }

    private void d() {
        this.a = (IMGView) findViewById(R.id.image_canvas);
        this.b = (RadioGroup) findViewById(R.id.rg_modes);
        this.a.setDoodleTouchListener(this);
        this.h = (ViewSwitcher) findViewById(R.id.vs_op);
        this.i = (ViewSwitcher) findViewById(R.id.vs_op_sub);
        IMGColorGroup iMGColorGroup = (IMGColorGroup) findViewById(R.id.cg_colors);
        this.c = iMGColorGroup;
        iMGColorGroup.setOnCheckedChangeListener(this);
        this.e = findViewById(R.id.layout_op_sub);
        this.f = findViewById(R.id.dcloud_image_edit_head);
        this.g = findViewById(R.id.dcloud_image_edit_foot);
    }

    public void a() {
        this.f.setVisibility(0);
        this.g.setVisibility(0);
    }

    public abstract void a(int i2);

    public abstract void a(io.dcloud.feature.gallery.imageedit.c.b bVar);

    public void b(int i2) {
        if (i2 >= 0) {
            this.h.setDisplayedChild(i2);
        }
    }

    public abstract Bitmap c();

    public void c(int i2) {
        if (i2 < 0) {
            this.e.setVisibility(8);
            return;
        }
        this.i.setDisplayedChild(i2);
        this.e.setVisibility(0);
    }

    public abstract void e();

    public abstract void f();

    public void g() {
    }

    public abstract void h();

    public abstract void i();

    public abstract void j();

    public abstract void k();

    public void l() {
        if (this.d == null) {
            b bVar = new b(this, this);
            this.d = bVar;
            bVar.setOnShowListener(this);
            this.d.setOnDismissListener(this);
        }
        this.d.show();
    }

    public abstract void m();

    public void n() {
        int i2 = C0038a.a[this.a.getMode().ordinal()];
        if (i2 == 1) {
            this.b.check(R.id.rb_doodle);
            c(0);
        } else if (i2 == 2) {
            this.b.check(R.id.rb_mosaic);
            c(1);
        } else if (i2 == 3) {
            this.b.clearCheck();
            c(-1);
        }
    }

    public void onBackPressed() {
        if (this.a.getMode() == io.dcloud.feature.gallery.imageedit.c.b.CLIP) {
            f();
        } else {
            super.onBackPressed();
        }
    }

    public final void onCheckedChanged(RadioGroup radioGroup, int i2) {
        a(this.c.getCheckColor());
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.rb_doodle) {
            a(io.dcloud.feature.gallery.imageedit.c.b.DOODLE);
        } else if (id == R.id.btn_text) {
            l();
        } else if (id == R.id.rb_mosaic) {
            a(io.dcloud.feature.gallery.imageedit.c.b.MOSAIC);
        } else if (id == R.id.btn_clip) {
            a(io.dcloud.feature.gallery.imageedit.c.b.CLIP);
        } else if (id == R.id.btn_undo) {
            m();
        } else if (id == R.id.tv_done) {
            h();
        } else if (id == R.id.tv_cancel) {
            e();
        } else if (id == R.id.ib_clip_cancel) {
            f();
        } else if (id == R.id.ib_clip_done) {
            i();
        } else if (id == R.id.tv_clip_reset) {
            j();
        } else if (id == R.id.ib_clip_rotate) {
            k();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bitmap c2 = c();
        if (c2 != null) {
            setContentView(R.layout.image_edit_activity);
            d();
            this.a.setImageBitmap(c2);
            g();
        } else {
            finish();
        }
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 21) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(512);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
            window.setNavigationBarColor(0);
            if (i2 >= 28) {
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.layoutInDisplayCutoutMode = 1;
                getWindow().setAttributes(attributes);
            }
        }
    }

    public void onDismiss(DialogInterface dialogInterface) {
        this.h.setVisibility(0);
    }

    public void onShow(DialogInterface dialogInterface) {
        this.h.setVisibility(8);
    }

    public void b() {
        this.f.setVisibility(4);
        this.g.setVisibility(4);
    }
}
