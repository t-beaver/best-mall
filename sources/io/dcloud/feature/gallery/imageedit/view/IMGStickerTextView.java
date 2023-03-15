package io.dcloud.feature.gallery.imageedit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import io.dcloud.feature.gallery.imageedit.b;
import io.dcloud.feature.gallery.imageedit.c.d;

public class IMGStickerTextView extends IMGStickerView implements b.a {
    private static float m = -1.0f;
    private TextView n;
    private d o;
    private b p;

    public IMGStickerTextView(Context context) {
        this(context, (AttributeSet) null, 0);
    }

    private b getDialog() {
        if (this.p == null) {
            this.p = new b(getContext(), this);
        }
        return this.p;
    }

    public View a(Context context) {
        TextView textView = new TextView(context);
        this.n = textView;
        textView.setTextSize(m);
        this.n.setPadding(26, 26, 26, 26);
        this.n.setTextColor(-1);
        return this.n;
    }

    public void b(Context context) {
        if (m <= 0.0f) {
            m = TypedValue.applyDimension(2, 24.0f, context.getResources().getDisplayMetrics());
        }
        super.b(context);
    }

    public void c() {
        b dialog = getDialog();
        dialog.a(this.o);
        dialog.show();
    }

    public d getText() {
        return this.o;
    }

    public void setText(d dVar) {
        TextView textView;
        this.o = dVar;
        if (dVar != null && (textView = this.n) != null) {
            textView.setText(dVar.c());
            this.n.setTextColor(this.o.b());
            this.n.setBackgroundColor(this.o.a());
        }
    }

    public IMGStickerTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public IMGStickerTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void a(d dVar) {
        TextView textView;
        this.o = dVar;
        if (dVar != null && (textView = this.n) != null) {
            textView.setText(dVar.c());
            this.n.setTextColor(this.o.b());
            this.n.setBackgroundColor(this.o.a());
        }
    }
}
