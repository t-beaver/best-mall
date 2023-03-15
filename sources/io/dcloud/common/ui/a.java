package io.dcloud.common.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.app.ActivityOptionsCompat;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.dcloud.PdrR;
import io.dcloud.WebviewActivity;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.ui.Info.AndroidPrivacyResponse;
import io.dcloud.common.ui.b;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.PdrUtil;
import java.io.File;

public class a {
    Context a;
    c b;
    /* access modifiers changed from: private */
    public AndroidPrivacyResponse c = null;
    private b.C0023b d;
    private boolean e;
    /* access modifiers changed from: private */
    public int f = PdrR.UNI_CUSTOM_PRIVACY_DIALOG_LAYOUT;
    /* access modifiers changed from: private */
    public boolean g = true;
    /* access modifiers changed from: private */
    public d h;

    /* renamed from: io.dcloud.common.ui.a$a  reason: collision with other inner class name */
    class C0020a extends b {
        final /* synthetic */ URLSpan c;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        C0020a(e eVar, URLSpan uRLSpan) {
            super(eVar);
            this.c = uRLSpan;
        }

        public void onClick(View view) {
            try {
                if ("system".equalsIgnoreCase(a.this.c.hrefLoader)) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(Uri.parse(this.c.getURL()));
                    a.this.a.startActivity(intent);
                    return;
                }
                Intent intent2 = new Intent();
                intent2.setClass(a.this.a, WebviewActivity.class);
                String url = this.c.getURL();
                if (!TextUtils.isEmpty(url) && !url.startsWith(DeviceInfo.HTTP_PROTOCOL) && !url.startsWith(DeviceInfo.HTTPS_PROTOCOL) && !url.startsWith("HTTP://") && !url.startsWith("HTTPS://")) {
                    if (url.startsWith("./")) {
                        url = url.substring(2);
                    }
                    String str = BaseInfo.sDefaultBootApp + "/www/" + url;
                    if (!b.a().b()) {
                        File file = new File(BaseInfo.sCacheFsAppsPath + str);
                        url = DeviceInfo.FILE_PROTOCOL + file.getPath();
                    } else {
                        url = "file:///android_asset/apps/" + str;
                    }
                    intent2.putExtra(WebviewActivity.isLocalHtmlParam, true);
                    intent2.putExtra(WebviewActivity.noPermissionAllowParam, true);
                }
                intent2.putExtra("url", url);
                intent2.setData(Uri.parse(url));
                intent2.setAction("android.intent.action.VIEW");
                intent2.setFlags(268435456);
                intent2.putExtra("ANIM", "POP");
                a.this.a.startActivity(intent2, ActivityOptionsCompat.makeCustomAnimation(a.this.a, R.anim.dcloud_pop_in, R.anim.dcloud_pop_in_out).toBundle());
            } catch (Exception unused) {
            }
        }
    }

    public abstract class b extends ClickableSpan {
        e a;

        b(e eVar) {
            this.a = eVar;
        }

        public void updateDrawState(TextPaint textPaint) {
            super.updateDrawState(textPaint);
            e eVar = this.a;
            if (eVar != null) {
                textPaint.setUnderlineText(eVar.b);
                textPaint.setColor(this.a.a);
                return;
            }
            textPaint.setUnderlineText(false);
            textPaint.setColor(-16776961);
        }
    }

    class c extends Dialog {
        Context a;
        private TextView b;
        private TextView c;
        private Button d;
        private Button e;
        private Button f;
        private LinearLayout g;

        /* renamed from: io.dcloud.common.ui.a$c$a  reason: collision with other inner class name */
        class C0021a implements View.OnClickListener {
            C0021a() {
            }

            public void onClick(View view) {
                c.this.dismiss();
                a.this.h.a(a.this.c.version);
            }
        }

        class b implements View.OnClickListener {
            b() {
            }

            public void onClick(View view) {
                c.this.dismiss();
                a.this.h.a();
            }
        }

        /* renamed from: io.dcloud.common.ui.a$c$c  reason: collision with other inner class name */
        class C0022c implements View.OnClickListener {
            C0022c() {
            }

            public void onClick(View view) {
                c.this.dismiss();
                a.this.h.onCancel();
            }
        }

        class d implements View.OnClickListener {
            d() {
            }

            public void onClick(View view) {
                c.this.dismiss();
                a.this.h.onCancel();
            }
        }

        class e implements View.OnClickListener {
            e() {
            }

            public void onClick(View view) {
                c.this.cancel();
            }
        }

        class f implements View.OnClickListener {
            f() {
            }

            public void onClick(View view) {
                c.this.cancel();
            }
        }

        class g implements View.OnClickListener {
            g() {
            }

            public void onClick(View view) {
                c.this.cancel();
            }
        }

        public c(Context context) {
            super(context);
            requestWindowFeature(1);
            this.a = context;
            c();
            a();
            if (f.a(this.g.getTag() != null ? this.g.getTag().toString() : "").a) {
                getWindow().setLayout(-1, -1);
            }
        }

        /* access modifiers changed from: private */
        public void b() {
            int i;
            int i2;
            int i3;
            int i4;
            this.d.setOnClickListener(new C0021a());
            Button button = this.f;
            if (button != null) {
                button.setOnClickListener(new b());
            }
            String str = "";
            if (a.this.g) {
                if (!TextUtils.isEmpty(a.this.c.second.title)) {
                    this.b.setText(a.this.c.second.title);
                }
                if (!TextUtils.isEmpty(a.this.c.second.message)) {
                    if (this.c.getTag() != null) {
                        str = this.c.getTag().toString();
                    }
                    e a2 = e.a(str);
                    this.c.setMovementMethod(LinkMovementMethod.getInstance());
                    this.c.setAutoLinkMask(15);
                    TextView textView = this.c;
                    a aVar = a.this;
                    textView.setText(aVar.a(aVar.c.second.message, a2));
                    this.c.setGravity(a.this.a("left"));
                }
                if (!TextUtils.isEmpty(a.this.c.second.buttonAccept)) {
                    this.d.setVisibility(0);
                    this.d.setText(a.this.c.second.buttonAccept);
                }
                if (!TextUtils.isEmpty(a.this.c.second.buttonRefuse)) {
                    this.e.setText(a.this.c.second.buttonRefuse);
                    this.e.setVisibility(0);
                    this.e.setOnClickListener(new C0022c());
                } else {
                    this.e.setVisibility(8);
                }
            } else {
                if (!TextUtils.isEmpty(a.this.c.title)) {
                    this.b.setText(a.this.c.title);
                }
                if (!TextUtils.isEmpty(a.this.c.message)) {
                    if (this.c.getTag() != null) {
                        str = this.c.getTag().toString();
                    }
                    e a3 = e.a(str);
                    this.c.setMovementMethod(LinkMovementMethod.getInstance());
                    this.c.setAutoLinkMask(15);
                    TextView textView2 = this.c;
                    a aVar2 = a.this;
                    textView2.setText(aVar2.a(aVar2.c.message, a3));
                    this.c.setGravity(a.this.a("left"));
                }
                if (!TextUtils.isEmpty(a.this.c.buttonAccept)) {
                    this.d.setVisibility(0);
                    this.d.setText(a.this.c.buttonAccept);
                }
                if (!TextUtils.isEmpty(a.this.c.buttonRefuse)) {
                    this.e.setText(a.this.c.buttonRefuse);
                    this.e.setVisibility(0);
                    this.e.setOnClickListener(new d());
                } else {
                    this.e.setVisibility(8);
                }
            }
            if (a.this.c.styles != null) {
                if (!TextUtils.isEmpty(a.this.c.styles.backgroundColor)) {
                    String str2 = !TextUtils.isEmpty(a.this.c.styles.borderRadius) ? a.this.c.styles.borderRadius : "10px";
                    int i5 = -1;
                    try {
                        i5 = Color.parseColor(a.this.c.styles.backgroundColor);
                    } catch (Exception unused) {
                    }
                    GradientDrawable gradientDrawable = new GradientDrawable();
                    gradientDrawable.setColor(i5);
                    gradientDrawable.setCornerRadius((float) a.this.a(this.a, (float) PdrUtil.parseInt(str2, 1, 10)));
                    this.g.setBackground(gradientDrawable);
                }
                int i6 = -16777216;
                if (a.this.c.styles.title != null) {
                    try {
                        i4 = Color.parseColor(a.this.c.styles.title.color);
                    } catch (Exception unused2) {
                        i4 = -16777216;
                    }
                    this.b.setTextColor(i4);
                }
                if (a.this.c.styles.content != null) {
                    try {
                        i3 = Color.parseColor(a.this.c.styles.content.color);
                    } catch (Exception unused3) {
                        i3 = -16777216;
                    }
                    this.c.setTextColor(i3);
                }
                if (a.this.c.styles.buttonAccept != null && !TextUtils.isEmpty(a.this.c.styles.buttonAccept.color)) {
                    try {
                        i2 = Color.parseColor(a.this.c.styles.buttonAccept.color);
                    } catch (Exception unused4) {
                        i2 = -16777216;
                    }
                    this.d.setTextColor(i2);
                }
                if (a.this.c.styles.buttonRefuse != null && !TextUtils.isEmpty(a.this.c.styles.buttonRefuse.color)) {
                    try {
                        i = Color.parseColor(a.this.c.styles.buttonRefuse.color);
                    } catch (Exception unused5) {
                        i = -16777216;
                    }
                    this.e.setTextColor(i);
                }
                if (a.this.c.styles.buttonVisitor != null && !TextUtils.isEmpty(a.this.c.styles.buttonVisitor.color)) {
                    try {
                        i6 = Color.parseColor(a.this.c.styles.buttonVisitor.color);
                    } catch (Exception unused6) {
                    }
                    this.f.setTextColor(i6);
                }
            }
        }

        private void c() {
            View inflate = LayoutInflater.from(this.a).inflate(a.this.f, (ViewGroup) null);
            setContentView(inflate);
            this.e = (Button) inflate.findViewById(R.id.btn_custom_privacy_cancel);
            this.d = (Button) inflate.findViewById(R.id.btn_custom_privacy_sure);
            this.f = (Button) inflate.findViewById(R.id.btn_custom_privacy_visitor);
            this.c = (TextView) inflate.findViewById(R.id.tv_privacy_content);
            this.b = (TextView) inflate.findViewById(R.id.tv_custom_privacy_title);
            this.g = (LinearLayout) inflate.findViewById(R.id.ll_content_layout);
            getWindow().setBackgroundDrawable(new ColorDrawable(0));
            d();
            if (this.f == null) {
                return;
            }
            if (a.this.c.disagreeMode.visitorEntry) {
                this.f.setVisibility(0);
            } else {
                this.f.setVisibility(8);
            }
        }

        public void d() {
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

        private void a() {
            this.e.setOnClickListener(new e());
            this.d.setOnClickListener(new f());
            Button button = this.f;
            if (button != null) {
                button.setOnClickListener(new g());
            }
        }
    }

    public interface d {
        void a();

        void a(String str);

        void onCancel();
    }

    public static class e {
        public int a;
        public boolean b;

        public static e a(String str) {
            e eVar = new e();
            if (TextUtils.isEmpty(str)) {
                return eVar;
            }
            JSONObject parseObject = JSON.parseObject(str);
            eVar.b = parseObject.getBoolean("linkLine").booleanValue();
            eVar.a = Color.parseColor(parseObject.getString("linkColor"));
            return eVar;
        }
    }

    public static class f {
        public boolean a = false;

        public static f a(String str) {
            f fVar = new f();
            if (TextUtils.isEmpty(str)) {
                return fVar;
            }
            JSONObject parseObject = JSON.parseObject(str);
            if (parseObject.containsKey(IApp.ConfigProperty.CONFIG_FULLSCREEN)) {
                fVar.a = parseObject.getBoolean(IApp.ConfigProperty.CONFIG_FULLSCREEN).booleanValue();
            }
            return fVar;
        }
    }

    public a(Context context) {
        this.a = context;
    }

    public void e() {
        c cVar = new c(this.a);
        this.b = cVar;
        cVar.setCanceledOnTouchOutside(false);
        this.b.setCancelable(false);
        this.b.b();
        this.b.show();
    }

    public b.C0023b b() {
        return this.d;
    }

    public boolean c() {
        return this.e;
    }

    public boolean d() {
        c cVar = this.b;
        if (cVar != null && cVar.isShowing()) {
            return true;
        }
        return false;
    }

    public void a(b.C0023b bVar) {
        this.d = bVar;
    }

    public void a(boolean z) {
        this.e = z;
    }

    public void a(int i) {
        if (i != 0) {
            this.f = i;
        }
    }

    public void a(AndroidPrivacyResponse androidPrivacyResponse, boolean z, d dVar) {
        this.c = androidPrivacyResponse;
        this.g = z;
        this.h = dVar;
    }

    public CharSequence a(String str, e eVar) {
        Spanned fromHtml = Html.fromHtml(str);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(fromHtml);
        for (URLSpan a2 : (URLSpan[]) spannableStringBuilder.getSpans(0, fromHtml.length(), URLSpan.class)) {
            a(spannableStringBuilder, a2, eVar);
        }
        return spannableStringBuilder;
    }

    private void a(SpannableStringBuilder spannableStringBuilder, URLSpan uRLSpan, e eVar) {
        spannableStringBuilder.setSpan(new C0020a(eVar, uRLSpan), spannableStringBuilder.getSpanStart(uRLSpan), spannableStringBuilder.getSpanEnd(uRLSpan), spannableStringBuilder.getSpanFlags(uRLSpan));
    }

    public void a() {
        c cVar = this.b;
        if (cVar != null) {
            cVar.dismiss();
            this.b = null;
        }
    }

    /* access modifiers changed from: private */
    public int a(Context context, float f2) {
        return (int) ((f2 * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    /* access modifiers changed from: private */
    public int a(String str) {
        if (!TextUtils.isEmpty(str)) {
            str.hashCode();
            str.hashCode();
            char c2 = 65535;
            switch (str.hashCode()) {
                case -1383228885:
                    if (str.equals("bottom")) {
                        c2 = 0;
                        break;
                    }
                    break;
                case 3317767:
                    if (str.equals("left")) {
                        c2 = 1;
                        break;
                    }
                    break;
                case 108511772:
                    if (str.equals("right")) {
                        c2 = 2;
                        break;
                    }
                    break;
            }
            switch (c2) {
                case 0:
                    return 80;
                case 1:
                    return 3;
                case 2:
                    return 5;
            }
        }
        return 17;
    }
}
