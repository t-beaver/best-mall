package io.dcloud.feature.internal.splash;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import io.dcloud.base.R;
import io.dcloud.common.util.AppRuntime;

public class SplashViewDBackground extends RelativeLayout implements ISplash {
    String a;
    b b = null;
    TextView c = null;
    boolean d = false;
    private boolean e = false;

    public SplashViewDBackground(Context context, Bitmap bitmap, String str, boolean z) {
        super(context);
        this.a = str;
        this.d = z;
        if (AppRuntime.getAppDarkMode(context)) {
            setBackgroundColor(context.getResources().getColor(R.color.nightBG));
        } else {
            setBackgroundColor(-1);
        }
        a(bitmap);
    }

    private void a(Bitmap bitmap) {
        float a2;
        if (!this.e) {
            this.b = new b(getContext(), this.d);
            int i = getResources().getDisplayMetrics().heightPixels;
            int a3 = (int) this.b.a(6.0f);
            int a4 = (int) this.b.a(65.0f);
            int a5 = (int) this.b.a(8.0f);
            if (getResources().getDisplayMetrics().widthPixels >= 1080) {
                a2 = this.b.a(6.0f);
            } else if (getResources().getDisplayMetrics().widthPixels >= 720) {
                a2 = this.b.a(8.0f);
            } else {
                if (getResources().getDisplayMetrics().widthPixels >= 540) {
                    a2 = this.b.a(10.0f);
                }
                int i2 = a5;
                b bVar = this.b;
                bVar.a(bitmap, a4, a4, (int) bVar.a(1.0f), 12962246, -5592406);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(a4, a4);
                layoutParams.addRule(14);
                layoutParams.topMargin = ((i / 2) - ((a4 + a3) + i2)) / 2;
                this.b.setId(16908313);
                addView(this.b, layoutParams);
                RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams2.addRule(3, this.b.getId());
                layoutParams2.addRule(13);
                layoutParams2.topMargin = a3;
                TextView textView = new TextView(getContext());
                this.c = textView;
                textView.setSingleLine();
                this.c.setTextSize((float) i2);
                this.c.setTextColor(-10855846);
                this.c.setId(16908314);
                setNameText(this.a);
                this.c.setTypeface(Typeface.create("宋体", 0));
                addView(this.c, layoutParams2);
            }
            a5 = (int) a2;
            int i22 = a5;
            b bVar2 = this.b;
            bVar2.a(bitmap, a4, a4, (int) bVar2.a(1.0f), 12962246, -5592406);
            RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(a4, a4);
            layoutParams3.addRule(14);
            layoutParams3.topMargin = ((i / 2) - ((a4 + a3) + i22)) / 2;
            this.b.setId(16908313);
            addView(this.b, layoutParams3);
            RelativeLayout.LayoutParams layoutParams22 = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams22.addRule(3, this.b.getId());
            layoutParams22.addRule(13);
            layoutParams22.topMargin = a3;
            TextView textView2 = new TextView(getContext());
            this.c = textView2;
            textView2.setSingleLine();
            this.c.setTextSize((float) i22);
            this.c.setTextColor(-10855846);
            this.c.setId(16908314);
            setNameText(this.a);
            this.c.setTypeface(Typeface.create("宋体", 0));
            addView(this.c, layoutParams22);
        }
    }

    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        if (view instanceof ISplash) {
            this.e = true;
        }
        super.addView(view, i, layoutParams);
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (this.e) {
            return super.dispatchTouchEvent(motionEvent);
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return true;
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.b.setBitmap(bitmap);
    }

    public void setNameText(String str) {
        TextView textView = this.c;
        if (textView != null && TextUtils.isEmpty(textView.getText())) {
            this.c.setText(str);
        }
    }
}
