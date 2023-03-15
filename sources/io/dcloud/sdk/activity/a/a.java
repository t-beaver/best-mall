package io.dcloud.sdk.activity.a;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import io.dcloud.base.R;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class a extends AlertDialog implements View.OnClickListener {
    private Context a;
    public b b;
    private View c;
    private ViewGroup d;
    private View e;
    private List<c> f;
    private String g;
    private String h;
    private int i;
    private boolean j;
    private boolean k;
    /* access modifiers changed from: private */
    public boolean l;
    private float m;
    private int n;
    private boolean o;
    private boolean p;

    public interface b {
        void initCancelText(TextView textView);

        void initTextItem(int i, TextView textView, String str);

        boolean onDismiss(int i);

        void onItemClick(int i);
    }

    /* renamed from: io.dcloud.sdk.activity.a.a$a  reason: collision with other inner class name */
    class C0078a implements DialogInterface.OnCancelListener {
        C0078a() {
        }

        public void onCancel(DialogInterface dialogInterface) {
            a.this.a(-1);
            b bVar = a.this.b;
            if (bVar != null) {
                bVar.onItemClick(-1);
            }
            boolean unused = a.this.l = false;
        }
    }

    public class c {
        public String a;
        public String b;
        public String c;

        public c(String str, String str2, String str3) {
            if (str2 == null) {
                this.b = "normal";
            } else {
                this.b = str2;
            }
            this.a = str;
            this.c = str3;
        }
    }

    public a(Context context) {
        super(context);
        this.g = "";
        this.h = "";
        this.i = -16777216;
        this.k = true;
        this.l = true;
        this.m = 16.0f;
        this.n = 0;
        this.o = true;
        this.p = false;
        a(context);
    }

    private Animation b() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setFillAfter(true);
        return alphaAnimation;
    }

    private void c() {
        int intrinsicHeight = g().getIntrinsicHeight();
        boolean z = !TextUtils.isEmpty(this.g);
        boolean z2 = !TextUtils.isEmpty(this.h);
        String str = this.h;
        if (str != null && TextUtils.isEmpty(str)) {
            this.h = getContext().getResources().getString(17039360);
            z2 = true;
        }
        if (z2) {
            TextView textView = new TextView(this.a);
            textView.setGravity(17);
            textView.getPaint().setFakeBoldText(true);
            textView.setTextSize(2, this.m);
            textView.setId(100);
            textView.setBackground(this.a.getResources().getDrawable(R.drawable.dcloud_ad_slt_as_ios7_other_bt_middle));
            textView.setText(this.h);
            textView.setTextColor(this.i);
            textView.setOnClickListener(this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, b(50));
            layoutParams.addRule(12);
            layoutParams.topMargin = b(this.n);
            this.d.addView(textView, layoutParams);
            b bVar = this.b;
            if (bVar != null) {
                bVar.initCancelText(textView);
            }
        }
        LinearLayout linearLayout = new LinearLayout(this.a);
        ScrollView scrollView = new ScrollView(this.a);
        linearLayout.setOrientation(1);
        scrollView.setId(300);
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-1, -2);
        if (!z) {
            intrinsicHeight = 0;
        }
        linearLayout.setPadding(0, intrinsicHeight, 0, 0);
        scrollView.addView(linearLayout, layoutParams2);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-1, -2);
        if (z2) {
            layoutParams3.addRule(2, 100);
        } else {
            layoutParams3.addRule(12);
        }
        this.d.addView(scrollView, layoutParams3);
        if (z) {
            TextView textView2 = new TextView(this.a);
            textView2.setGravity(17);
            textView2.setId(200);
            textView2.setOnClickListener(this);
            textView2.setBackgroundDrawable(g());
            textView2.setText(this.g);
            textView2.setTextColor(Color.parseColor("#8C8C8C"));
            textView2.setTextSize(2, this.m);
            RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(-1, -2);
            layoutParams4.topMargin = 0;
            layoutParams4.addRule(6, 300);
            this.d.addView(textView2, layoutParams4);
        }
        List<c> list = this.f;
        if (list != null && list.size() > 0) {
            for (int i2 = 0; i2 < this.f.size(); i2++) {
                TextView textView3 = new TextView(this.a);
                textView3.setGravity(17);
                textView3.setId(i2 + 100 + 1);
                textView3.setOnClickListener(this);
                textView3.setBackgroundDrawable(a(this.f.size(), i2, z));
                textView3.setText(this.f.get(i2).a);
                textView3.setTextColor(Color.parseColor("#000000"));
                textView3.setTextSize(2, this.m);
                if (i2 > 0) {
                    LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(-1, b(50));
                    layoutParams5.topMargin = 0;
                    linearLayout.addView(textView3, layoutParams5);
                } else {
                    linearLayout.addView(textView3, new LinearLayout.LayoutParams(-1, b(50)));
                }
                b bVar2 = this.b;
                if (bVar2 != null) {
                    bVar2.initTextItem(i2, textView3, this.f.get(i2).a);
                }
            }
        }
        int b2 = b(this.n);
        this.d.setBackgroundDrawable(new ColorDrawable(0));
        if (this.p) {
            this.d.setPadding(b2, b2, b2, b2);
        } else {
            this.d.setPadding(0, b2, 0, b2);
        }
    }

    private Animation d() {
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
        translateAnimation.setDuration(300);
        return translateAnimation;
    }

    private Animation e() {
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, 1.0f);
        translateAnimation.setDuration(300);
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }

    private View f() {
        FrameLayout frameLayout = new FrameLayout(this.a);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        layoutParams.gravity = 80;
        frameLayout.setLayoutParams(layoutParams);
        View view = new View(this.a);
        this.e = view;
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        if (this.p) {
            this.e.setBackgroundColor(Color.argb(136, 0, 0, 0));
        }
        this.e.setId(10);
        this.e.setOnClickListener(this);
        this.d = new RelativeLayout(this.a);
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-1, -2);
        layoutParams2.gravity = 80;
        this.d.setLayoutParams(layoutParams2);
        frameLayout.addView(this.e);
        frameLayout.addView(this.d);
        return frameLayout;
    }

    private Drawable g() {
        return this.a.getResources().getDrawable(R.drawable.dcloud_ad_slt_as_ios7_other_bt_middle);
    }

    public void h() {
        View currentFocus;
        InputMethodManager inputMethodManager = (InputMethodManager) this.a.getSystemService("input_method");
        if (inputMethodManager.isActive() && (currentFocus = ((Activity) this.a).getCurrentFocus()) != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
        this.c = f();
        this.e.startAnimation(a());
        this.d.startAnimation(d());
    }

    public void i() {
        if (this.k) {
            c();
            show();
            getWindow().setContentView(this.c);
            this.k = false;
        }
    }

    public void onClick(View view) {
        if ((view.getId() == 10 && !this.j) || view.getId() == 200) {
            return;
        }
        if (view.getId() != 10) {
            int id = view.getId() - 100;
            a(id);
            b bVar = this.b;
            if (bVar != null) {
                bVar.onItemClick(id);
            }
            this.l = false;
            return;
        }
        a(-1);
        b bVar2 = this.b;
        if (bVar2 != null) {
            bVar2.onItemClick(-1);
        }
        this.l = false;
    }

    public void show() {
        super.show();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.gravity = 80;
        attributes.width = -1;
        attributes.height = -2;
        View decorView = getWindow().getDecorView();
        int i2 = this.n;
        decorView.setPadding(i2, 0, i2, 0);
        getWindow().setAttributes(attributes);
    }

    public void a(Context context) {
        this.a = context;
        this.n = 10;
        h();
        getWindow().setGravity(80);
        ColorDrawable colorDrawable = new ColorDrawable(0);
        colorDrawable.setAlpha(0);
        getWindow().setBackgroundDrawable(colorDrawable);
        setOnCancelListener(new C0078a());
    }

    private int b(int i2) {
        return (int) TypedValue.applyDimension(1, (float) i2, this.a.getResources().getDisplayMetrics());
    }

    private Animation a() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(300);
        return alphaAnimation;
    }

    private Drawable a(int i2, int i3, boolean z) {
        return this.a.getResources().getDrawable(R.drawable.dcloud_ad_slt_as_ios7_other_bt_middle);
    }

    public void a(int i2) {
        if (!this.k && !c(i2)) {
            dismiss();
            this.k = true;
            List<c> list = this.f;
            if (list != null) {
                list.clear();
            }
        }
    }

    public a(Context context, int i2) {
        super(context, i2);
        this.g = "";
        this.h = "";
        this.i = -16777216;
        this.k = true;
        this.l = true;
        this.m = 16.0f;
        this.n = 0;
        this.o = true;
        this.p = false;
        this.p = true;
        a(context);
    }

    public a a(String str) {
        this.h = str;
        return this;
    }

    public a a(boolean z) {
        this.j = z;
        return this;
    }

    public a a(JSONArray jSONArray) {
        this.f = new ArrayList();
        int length = jSONArray.length();
        for (int i2 = 0; i2 < length; i2++) {
            try {
                JSONObject jSONObject = jSONArray.getJSONObject(i2);
                if (jSONObject != null) {
                    this.f.add(new c(jSONObject.optString(AbsoluteConst.JSON_KEY_TITLE), jSONObject.optString("style"), jSONObject.optString("color")));
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        return this;
    }

    public a a(b bVar) {
        this.b = bVar;
        return this;
    }

    private boolean c(int i2) {
        b bVar = this.b;
        if (bVar == null) {
            return false;
        }
        boolean onDismiss = bVar.onDismiss(i2);
        if (onDismiss) {
            return onDismiss;
        }
        this.d.startAnimation(e());
        this.e.startAnimation(b());
        return onDismiss;
    }
}
