package io.dcloud.common.ui;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import io.dcloud.PdrR;
import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import io.dcloud.common.ui.GifImageView;
import io.dcloud.common.util.DensityUtils;
import io.dcloud.common.util.PdrUtil;
import java.lang.ref.WeakReference;

public class PermissionGuideWindow implements IReflectAble {
    private static WeakReference<PermissionGuideWindow> a;
    private Context b;
    private final Handler c = new Handler(Looper.getMainLooper());
    private WindowManager d;
    private WindowManager.LayoutParams e;
    private ViewGroup f;

    class a implements Runnable {
        a() {
        }

        public void run() {
            PermissionGuideWindow.this.dismissWindow();
        }
    }

    class b implements View.OnClickListener {
        b() {
        }

        public void onClick(View view) {
            PermissionGuideWindow.this.dismissWindow();
        }
    }

    class c implements GifImageView.a {
        final /* synthetic */ ImageView a;

        c(ImageView imageView) {
            this.a = imageView;
        }

        public void a() {
        }

        public void a(float f) {
        }

        public void a(boolean z) {
        }

        public void b() {
        }

        public void c() {
            ImageView imageView = this.a;
            if (imageView != null) {
                imageView.setVisibility(0);
            }
        }
    }

    class d implements View.OnClickListener {
        final /* synthetic */ GifImageView a;
        final /* synthetic */ ImageView b;

        d(GifImageView gifImageView, ImageView imageView) {
            this.a = gifImageView;
            this.b = imageView;
        }

        public void onClick(View view) {
            GifImageView gifImageView = this.a;
            if (gifImageView != null) {
                gifImageView.pause();
            }
            ImageView imageView = this.b;
            if (imageView != null) {
                imageView.setVisibility(0);
            }
        }
    }

    class e implements View.OnClickListener {
        final /* synthetic */ GifImageView a;
        final /* synthetic */ ImageView b;

        e(GifImageView gifImageView, ImageView imageView) {
            this.a = gifImageView;
            this.b = imageView;
        }

        public void onClick(View view) {
            GifImageView gifImageView = this.a;
            if (gifImageView != null) {
                gifImageView.play();
            }
            ImageView imageView = this.b;
            if (imageView != null) {
                imageView.setVisibility(8);
            }
        }
    }

    public PermissionGuideWindow(Context context) {
        this.b = context.getApplicationContext();
    }

    public static PermissionGuideWindow getInstance(Context context) {
        WeakReference<PermissionGuideWindow> weakReference = a;
        if (weakReference == null || weakReference.get() == null) {
            a = new WeakReference<>(new PermissionGuideWindow(context));
        }
        return (PermissionGuideWindow) a.get();
    }

    public void dismissWindow() {
        try {
            WindowManager windowManager = this.d;
            if (windowManager != null) {
                windowManager.removeView(this.f);
                this.f.removeAllViews();
                this.f = null;
                this.d = null;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void dismissWindowDelayed(long j) {
        this.c.postDelayed(new a(), j);
    }

    public void showWindow(String str, int i) {
        if (i != 0) {
            try {
                if (this.d == null) {
                    this.d = (WindowManager) this.b.getSystemService("window");
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    this.e = layoutParams;
                    layoutParams.gravity = 21;
                    layoutParams.type = 2005;
                    if (Build.BRAND.equalsIgnoreCase(MobilePhoneModel.XIAOMI) && Build.VERSION.SDK_INT >= 25) {
                        this.e.type = 2002;
                    }
                    WindowManager.LayoutParams layoutParams2 = this.e;
                    layoutParams2.format = -3;
                    layoutParams2.flags = 40;
                    layoutParams2.width = DensityUtils.dip2px(this.b, 220.0f);
                    this.e.height = -2;
                }
                if (this.f == null) {
                    this.f = (ViewGroup) LayoutInflater.from(this.b).inflate(PdrR.DCLOUD_SHORTCUT_PERMISSION_GUIDE_LAYOUT, (ViewGroup) null);
                }
                if (this.f.getParent() != null) {
                    this.d.removeViewImmediate(this.f);
                }
                this.d.addView(this.f, this.e);
                this.f.findViewById(PdrR.DCLOUD_GUIDE_CLOSE).setOnClickListener(new b());
                ImageView imageView = (ImageView) this.f.findViewById(PdrR.DCLOUD_GUIDE_PLAY);
                GifImageView gifImageView = (GifImageView) this.f.findViewById(PdrR.DCLOUD_GUIDE_GIFVIEW);
                RelativeLayout relativeLayout = (RelativeLayout) this.f.findViewById(PdrR.DCLOUD_GUIDE_PLAY_LAYOUT);
                TextView textView = (TextView) this.f.findViewById(PdrR.DCLOUD_GUIDE_TIP);
                if (!PdrUtil.isEmpty(str)) {
                    textView.setText(str);
                }
                if (1 == i) {
                    relativeLayout.setVisibility(8);
                    gifImageView.setVisibility(8);
                } else {
                    gifImageView.setGifResource(i);
                }
                gifImageView.setOnPlayListener(new c(imageView));
                gifImageView.setOnClickListener(new d(gifImageView, imageView));
                imageView.setOnClickListener(new e(gifImageView, imageView));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
