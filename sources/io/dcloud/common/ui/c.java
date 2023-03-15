package io.dcloud.common.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.URLSpan;
import android.view.View;
import androidx.core.app.ActivityOptionsCompat;
import io.dcloud.WebviewActivity;
import io.dcloud.base.R;

public class c {
    private static c a;
    int b = -16777216;
    int c = -16777216;
    int d = -16777216;
    int e = 17;
    Context f;
    String g = "isSelected";

    class a extends ClickableSpan {
        final /* synthetic */ URLSpan a;

        a(URLSpan uRLSpan) {
            this.a = uRLSpan;
        }

        public void onClick(View view) {
            try {
                String url = this.a.getURL();
                Intent intent = new Intent();
                intent.setClass(c.this.f, WebviewActivity.class);
                intent.putExtra("url", url);
                intent.setData(Uri.parse(url));
                intent.setAction("android.intent.action.VIEW");
                intent.setFlags(268435456);
                intent.putExtra("ANIM", "POP");
                c.this.f.startActivity(intent, ActivityOptionsCompat.makeCustomAnimation(c.this.f, R.anim.dcloud_pop_in, R.anim.dcloud_pop_in_out).toBundle());
            } catch (Exception unused) {
            }
        }
    }

    public c(Context context) {
        this.f = context;
    }

    public static c a(Context context) {
        if (a == null) {
            a = new c(context);
        }
        return a;
    }

    public CharSequence a(String str) {
        Spanned fromHtml = Html.fromHtml(str);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(fromHtml);
        for (URLSpan a2 : (URLSpan[]) spannableStringBuilder.getSpans(0, fromHtml.length(), URLSpan.class)) {
            a(spannableStringBuilder, a2);
        }
        return spannableStringBuilder;
    }

    private void a(SpannableStringBuilder spannableStringBuilder, URLSpan uRLSpan) {
        int spanStart = spannableStringBuilder.getSpanStart(uRLSpan);
        int spanEnd = spannableStringBuilder.getSpanEnd(uRLSpan);
        spannableStringBuilder.setSpan(new a(uRLSpan), spanStart, spanEnd, spannableStringBuilder.getSpanFlags(uRLSpan));
        spannableStringBuilder.setSpan(new TextAppearanceSpan(this.f, R.style.textAppearance), spanStart, spanEnd, 33);
    }
}
