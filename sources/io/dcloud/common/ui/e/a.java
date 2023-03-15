package io.dcloud.common.ui.e;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import io.dcloud.base.R;

public class a extends Dialog {
    private final Context a;
    private TextView b;
    private TextView c;
    private Button d;
    private Button e;

    /* renamed from: io.dcloud.common.ui.e.a$a  reason: collision with other inner class name */
    class C0024a implements View.OnClickListener {
        final /* synthetic */ View.OnClickListener a;

        C0024a(View.OnClickListener onClickListener) {
            this.a = onClickListener;
        }

        public void onClick(View view) {
            View.OnClickListener onClickListener = this.a;
            if (onClickListener != null) {
                onClickListener.onClick(view);
                a.this.dismiss();
            }
        }
    }

    class b implements View.OnClickListener {
        final /* synthetic */ View.OnClickListener a;

        b(View.OnClickListener onClickListener) {
            this.a = onClickListener;
        }

        public void onClick(View view) {
            View.OnClickListener onClickListener = this.a;
            if (onClickListener != null) {
                onClickListener.onClick(view);
                a.this.cancel();
            }
        }
    }

    public a(Context context) {
        super(context);
        this.a = context;
        a();
    }

    private void a() {
        requestWindowFeature(1);
        setContentView(R.layout.dcloud_sample_dialog);
        b();
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        c();
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    private void b() {
        this.b = (TextView) findViewById(R.id.tv_sample_dialog_title);
        this.c = (TextView) findViewById(R.id.tv_sample_dialog_content);
        this.d = (Button) findViewById(R.id.btn_sample_dialog_sure);
        this.e = (Button) findViewById(R.id.btn_sample_dialog_cancel);
    }

    private void c() {
        Display defaultDisplay = ((Activity) this.a).getWindowManager().getDefaultDisplay();
        if (this.a.getResources().getConfiguration().orientation == 1) {
            TextView textView = this.c;
            double height = (double) defaultDisplay.getHeight();
            Double.isNaN(height);
            textView.setMaxHeight((int) (height * 0.6d));
            return;
        }
        TextView textView2 = this.c;
        double height2 = (double) defaultDisplay.getHeight();
        Double.isNaN(height2);
        textView2.setMaxHeight((int) (height2 * 0.5d));
    }

    public void b(int i) {
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.x = 0;
        attributes.y = 0;
        window.setLayout(i, attributes.height);
    }

    public void a(int i) {
        this.c.setGravity(i);
    }

    public void a(String str) {
        this.c.setText(str);
    }

    public void a(String str, View.OnClickListener onClickListener) {
        this.e.setVisibility(0);
        this.e.setText(str);
        this.e.setOnClickListener(new b(onClickListener));
    }

    public void b(String str) {
        this.b.setText(str);
    }

    public void b(String str, View.OnClickListener onClickListener) {
        this.d.setVisibility(0);
        this.d.setText(str);
        this.d.setOnClickListener(new C0024a(onClickListener));
    }
}
