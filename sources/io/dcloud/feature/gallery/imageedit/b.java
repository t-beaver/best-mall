package io.dcloud.feature.gallery.imageedit;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import io.dcloud.base.R;
import io.dcloud.feature.gallery.imageedit.c.d;
import io.dcloud.feature.gallery.imageedit.view.IMGColorGroup;
import java.lang.reflect.Field;

public class b extends Dialog implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private EditText a;
    private a b;
    private d c;
    private IMGColorGroup d;
    private int e = -1;
    private TextView f;

    public interface a {
        void a(d dVar);
    }

    public b(Context context, a aVar) {
        super(context, R.style.ImageTextDialog);
        setContentView(R.layout.image_text_dialog);
        this.b = aVar;
        Window window = getWindow();
        if (window != null) {
            window.setLayout(-1, -1);
        }
    }

    private void b() {
        try {
            Field declaredField = TextView.class.getDeclaredField("mCursorDrawableRes");
            declaredField.setAccessible(true);
            declaredField.set(this.a, Integer.valueOf(R.drawable.image_edit_cursor));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void a(d dVar) {
        this.c = dVar;
    }

    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        this.e = this.d.getCheckColor();
        if (this.f.isSelected()) {
            if (this.e == -1) {
                this.a.setTextColor(-16777216);
            } else {
                this.a.setTextColor(-1);
            }
            this.a.setBackgroundColor(this.e);
            return;
        }
        this.a.setTextColor(this.e);
        this.a.setBackgroundColor(0);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_done) {
            a();
            this.e = -1;
            this.a.setBackgroundColor(0);
        } else if (id == R.id.tv_cancel) {
            dismiss();
            this.e = -1;
            this.a.setBackgroundColor(0);
        } else if (id == R.id.textview_1) {
            view.setSelected(!view.isSelected());
            if (view.isSelected()) {
                if (this.e == -1) {
                    this.a.setTextColor(-16777216);
                } else {
                    this.a.setTextColor(-1);
                }
                this.a.setBackgroundColor(this.e);
                return;
            }
            this.a.setTextColor(this.e);
            this.a.setBackgroundColor(0);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        IMGColorGroup iMGColorGroup = (IMGColorGroup) findViewById(R.id.cg_colors);
        this.d = iMGColorGroup;
        iMGColorGroup.setOnCheckedChangeListener(this);
        this.a = (EditText) findViewById(R.id.et_text);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_done).setOnClickListener(this);
        TextView textView = (TextView) findViewById(R.id.textview_1);
        this.f = textView;
        textView.setOnClickListener(this);
        this.f.setSelected(false);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        this.a.setPadding(30, 30, 30, 30);
        d dVar = this.c;
        if (dVar != null) {
            this.a.setText(dVar.c());
            int b2 = this.c.b();
            this.e = b2;
            this.a.setTextColor(b2);
            b();
            int a2 = this.c.a();
            if (a2 == 0) {
                this.f.setSelected(false);
                this.a.setBackgroundColor(0);
            } else {
                this.e = a2;
                this.a.setBackgroundColor(a2);
                this.f.setSelected(true);
            }
            if (!this.c.d()) {
                EditText editText = this.a;
                editText.setSelection(editText.length());
            }
            this.c = null;
        } else {
            this.a.setText("");
            this.f.setSelected(false);
        }
        getCurrentFocus();
        this.d.setCheckColor(this.e);
        this.a.requestFocus();
    }

    private void a() {
        int i;
        String obj = this.a.getText().toString();
        if (!TextUtils.isEmpty(obj) && this.b != null) {
            int i2 = -1;
            if (this.f.isSelected()) {
                i = this.e;
                if (i == -1) {
                    i2 = -16777216;
                }
            } else {
                i = 0;
                i2 = this.e;
            }
            this.b.a(new d(obj, i2, i));
        }
        dismiss();
    }
}
