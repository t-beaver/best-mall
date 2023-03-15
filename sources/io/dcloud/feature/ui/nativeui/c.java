package io.dcloud.feature.ui.nativeui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import io.dcloud.PandoraEntryActivity;
import io.dcloud.PdrR;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.util.EventDispatchManager;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONObject;

class c implements PopupWindow.OnDismissListener, ISysEventListener, EventDispatchManager.ActivityEventDispatchListener {
    public static final String a = "c";
    private int A;
    private int B;
    private boolean C = false;
    private long D;
    private String E;
    private int F = -1;
    private String G;
    private AnimationDrawable H;
    private Bitmap I;
    private int J;
    private int K = -2;
    private int L = -2;
    LinearLayout M;
    /* access modifiers changed from: private */
    public Activity b;
    private NativeUIFeatureImpl c;
    /* access modifiers changed from: private */
    public IWebview d;
    private IApp e;
    private String f;
    public String g;
    /* access modifiers changed from: private */
    public String h = AbsoluteConst.EVENTS_CLOSE;
    private PopupWindow i;
    private TextView j;
    private View k;
    private ProgressBar l;
    private ImageView m;
    private String n;
    private String o;
    private LinearLayout p;
    private String q;
    private String r;
    /* access modifiers changed from: private */
    public boolean s = true;
    private String t;
    private String u;
    private int v;
    private int w;
    private int x = -1308622848;
    private View y;
    private int z;

    class a implements View.OnTouchListener {
        a() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            return c.this.s;
        }
    }

    class b implements View.OnKeyListener {
        b() {
        }

        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getAction() == 0 && i == 4) {
                if (AbsoluteConst.EVENTS_CLOSE.equalsIgnoreCase(c.this.h)) {
                    c.this.a();
                    c.this.c();
                    return true;
                } else if ("transmit".equalsIgnoreCase(c.this.h)) {
                    if (!(c.this.b instanceof PandoraEntryActivity)) {
                        c.this.b.onBackPressed();
                    } else if (c.this.d.canGoBack()) {
                        c.this.d.goBackOrForward(-1);
                    } else {
                        c.this.d.getActivity().onBackPressed();
                    }
                    return false;
                } else if ("none".equalsIgnoreCase(c.this.h)) {
                    return true;
                }
            }
            return false;
        }
    }

    /* renamed from: io.dcloud.feature.ui.nativeui.c$c  reason: collision with other inner class name */
    class C0046c implements View.OnClickListener {
        C0046c() {
        }

        public void onClick(View view) {
            c.this.a();
            c.this.c();
        }
    }

    c(NativeUIFeatureImpl nativeUIFeatureImpl, IWebview iWebview, String str, JSONObject jSONObject, String str2, Activity activity) {
        this.c = nativeUIFeatureImpl;
        this.d = iWebview;
        this.e = iWebview.obtainApp();
        this.f = str2;
        this.b = activity;
        if (activity instanceof PandoraEntryActivity) {
            this.y = ((AdaFrameItem) iWebview.obtainFrameView()).obtainMainView();
        } else {
            this.y = ((ViewGroup) activity.findViewById(16908290)).getChildAt(0);
        }
        this.z = this.e.getInt(0);
        this.A = this.e.getInt(1);
        a(iWebview, jSONObject);
        b();
        a((View) this.p);
        a(str);
        d();
    }

    private void e() {
        this.p.setFocusable(true);
        this.p.setFocusableInTouchMode(true);
        this.p.setOnKeyListener(new b());
    }

    private void f() {
        if (AbsoluteConst.JSON_VALUE_BLOCK.equalsIgnoreCase(this.r)) {
            this.p.setOrientation(1);
        } else if (AbsoluteConst.JSON_VALUE_INLINE.equalsIgnoreCase(this.r)) {
            this.p.setOrientation(0);
        } else if ("none".equalsIgnoreCase(this.r)) {
            this.k.setVisibility(8);
            this.l.setVisibility(8);
        }
    }

    private void g() {
        if (!TextUtils.isEmpty(this.G)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            byte[] bArr = new byte[0];
            try {
                bArr = a(this.e.obtainResInStream(this.d.obtainFullUrl(), this.G));
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
            int i2 = options.outWidth;
            int i3 = options.outHeight;
            if (i2 != 0 && i3 != 0) {
                options.inSampleSize = a(i3);
                options.inJustDecodeBounds = false;
                Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
                this.I = decodeByteArray;
                if (i2 % i3 != 0) {
                    if (decodeByteArray != null) {
                        this.l.setVisibility(8);
                        this.m.setVisibility(0);
                        this.m.setImageBitmap(this.I);
                    }
                } else if (decodeByteArray != null) {
                    int width = decodeByteArray.getWidth();
                    int height = this.I.getHeight();
                    int i4 = width / height;
                    if (this.D <= 0) {
                        this.D = 100;
                    }
                    AnimationDrawable animationDrawable = new AnimationDrawable();
                    for (int i5 = 0; i5 < i4; i5++) {
                        animationDrawable.addFrame(new BitmapDrawable(Bitmap.createBitmap(this.I, i5 * height, 0, height, height)), (int) this.D);
                    }
                    animationDrawable.setOneShot(false);
                    ViewGroup.LayoutParams layoutParams = this.m.getLayoutParams();
                    if (layoutParams != null) {
                        int i6 = this.F;
                        if (i6 > 0) {
                            height = i6;
                        }
                        layoutParams.width = height;
                        layoutParams.height = height;
                        this.m.setLayoutParams(layoutParams);
                    }
                    this.l.setVisibility(8);
                    this.m.setVisibility(0);
                    this.m.setBackground(animationDrawable);
                    this.H = (AnimationDrawable) this.m.getBackground();
                }
            }
        }
    }

    private void h() {
        Drawable drawable;
        if (PdrUtil.isEquals(this.u, "snow")) {
            if (PdrUtil.isEquals(this.t, "black")) {
                drawable = this.b.getResources().getDrawable(PdrR.DRAWBLE_PROGRESSBAR_BLACK_SNOW);
            } else {
                drawable = this.b.getResources().getDrawable(PdrR.DRAWBLE_PROGRESSBAR_WHITE_SNOW);
            }
        } else if (PdrUtil.isEquals(this.t, "black")) {
            drawable = this.b.getResources().getDrawable(PdrR.DRAWBLE_PROGRESSBAR_BLACK_CIRCLE);
        } else {
            drawable = this.b.getResources().getDrawable(PdrR.DRAWBLE_PROGRESSBAR_WHITE_CIRCLE);
        }
        if (this.F > 0) {
            ProgressBar progressBar = this.l;
            int i2 = this.F;
            progressBar.setLayoutParams(new LinearLayout.LayoutParams(i2, i2));
        } else {
            double intrinsicHeight = (double) drawable.getIntrinsicHeight();
            Double.isNaN(intrinsicHeight);
            int i3 = (int) (intrinsicHeight * 0.3d);
            this.l.setLayoutParams(new LinearLayout.LayoutParams(i3, i3));
        }
        this.l.setIndeterminateDrawable(drawable);
    }

    private void i() {
        LinearLayout linearLayout = this.p;
        int i2 = this.v;
        int i3 = this.w;
        linearLayout.setPadding(i2, i3, i2, i3);
        GradientDrawable gradientDrawable = (GradientDrawable) this.p.getBackground();
        int i4 = this.B;
        if (i4 > 0) {
            gradientDrawable.setCornerRadius((float) i4);
        }
        gradientDrawable.setColor(this.x);
        if (this.C) {
            this.p.setOnClickListener(new C0046c());
        }
    }

    private void j() {
        this.j.setTextColor(!PdrUtil.isEmpty(this.n) ? PdrUtil.stringToColor(this.n) : -1);
        if (PdrUtil.isEmpty(this.o)) {
            this.j.setGravity(17);
        } else if ("left".equals(this.o)) {
            this.j.setGravity(3);
        } else if ("right".equals(this.o)) {
            this.j.setGravity(5);
        } else {
            this.j.setGravity(17);
        }
        int i2 = this.J;
        if (i2 > 0) {
            this.j.setTextSize(0, (float) i2);
        }
    }

    public void onDismiss() {
        Deprecated_JSUtil.execCallback(this.d, this.f, (String) null, JSUtil.OK, false, false);
        this.i = null;
        if (!this.s || (!TextUtils.isEmpty(this.h) && !AbsoluteConst.EVENTS_CLOSE.equalsIgnoreCase(this.h))) {
            this.e.unregisterSysEventListener(this, ISysEventListener.SysEventType.onKeyUp);
            EventDispatchManager.getInstance().removeListener(this);
        }
        Bitmap bitmap = this.I;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.I.recycle();
            System.gc();
        }
    }

    public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
        if (sysEventType != ISysEventListener.SysEventType.onKeyUp || ((Integer) ((Object[]) obj)[0]).intValue() != 4) {
            return false;
        }
        if ("none".equalsIgnoreCase(this.h)) {
            return true;
        }
        if ("transmit".equalsIgnoreCase(this.h)) {
            return false;
        }
        a();
        c();
        return true;
    }

    private void a(IWebview iWebview, JSONObject jSONObject) {
        if (!JSONUtil.isNull(jSONObject, "background")) {
            this.x = PdrUtil.stringToColor(JSONUtil.getString(jSONObject, "background"));
        }
        String string = JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_MODAL);
        if (!PdrUtil.isEmpty(string)) {
            this.s = !PdrUtil.isEquals(AbsoluteConst.FALSE, string);
        }
        float scale = iWebview.getScale();
        this.B = (int) (((float) PdrUtil.parseInt(JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_ROUND), 10)) * scale);
        String string2 = JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_PADLOCK);
        if (!PdrUtil.isEmpty(string2)) {
            this.C = Boolean.valueOf(string2).booleanValue() | this.C;
        }
        this.K = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject, "width"), this.z, this.K, scale);
        this.L = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject, "height"), this.A, this.L, scale);
        String string3 = JSONUtil.getString(jSONObject, "back");
        if (!TextUtils.isEmpty(string3)) {
            this.h = string3;
        }
        this.t = JSONUtil.getString(jSONObject, "style");
        if (!JSONUtil.isNull(jSONObject, "color")) {
            this.n = JSONUtil.getString(jSONObject, "color");
        }
        this.q = JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_PADDIN);
        if (!JSONUtil.isNull(jSONObject, "padding")) {
            String string4 = JSONUtil.getString(jSONObject, "padding");
            if (string4.indexOf(37) >= 0) {
                this.v = PdrUtil.convertToScreenInt(string4, this.z, this.v, scale);
                this.w = PdrUtil.convertToScreenInt(string4, this.A, this.w, scale);
            } else {
                int convertToScreenInt = PdrUtil.convertToScreenInt(string4, this.z, this.w, scale);
                this.w = convertToScreenInt;
                this.v = convertToScreenInt;
            }
        } else {
            int parseInt = PdrUtil.parseInt(this.q, this.z, PdrUtil.parseInt("3%", this.z, 0));
            this.w = parseInt;
            this.v = parseInt;
        }
        if (!JSONUtil.isNull(jSONObject, AbsoluteConst.JSON_KEY_TEXTALIGN)) {
            this.o = JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_TEXTALIGN);
        }
        JSONObject jSONObject2 = JSONUtil.getJSONObject(jSONObject, "loading");
        if (jSONObject2 != null) {
            this.r = JSONUtil.getString(jSONObject2, "display");
            this.D = JSONUtil.getLong(jSONObject2, "interval");
            this.E = JSONUtil.getString(jSONObject2, AbsoluteConst.JSON_KEY_ICON);
            this.F = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject2, "height"), this.z, -1, scale);
            this.u = JSONUtil.getString(jSONObject2, "type");
        }
        if (!TextUtils.isEmpty(this.E)) {
            this.G = this.E;
        }
        this.J = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject, AbsoluteConst.JSON_KEY_SIZE), this.z, 0, scale);
    }

    private void b() {
        LinearLayout linearLayout = (LinearLayout) ((LayoutInflater) this.b.getSystemService("layout_inflater")).inflate(NativeUIR.LAYOUT_DIALOG_LAYOUT_LOADING_DCLOUD, (ViewGroup) null, false);
        this.M = linearLayout;
        LinearLayout linearLayout2 = (LinearLayout) linearLayout.findViewById(NativeUIR.DCLOUD_LOADING_LAYOUT_ROOT);
        this.p = linearLayout2;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout2.getLayoutParams();
        layoutParams.width = this.K;
        layoutParams.height = this.L;
        this.p.setLayoutParams(layoutParams);
    }

    private void d() {
        int i2 = -1;
        int i3 = -2;
        if (Build.VERSION.SDK_INT < 23 || !this.s) {
            i2 = -2;
        } else {
            this.s = false;
            i3 = -1;
        }
        if (!this.s || (!TextUtils.isEmpty(this.h) && !AbsoluteConst.EVENTS_CLOSE.equalsIgnoreCase(this.h))) {
            if (this.b instanceof PandoraEntryActivity) {
                this.e.registerSysEventListener(this, ISysEventListener.SysEventType.onKeyUp);
            } else {
                EventDispatchManager.getInstance().addListener(this);
            }
        }
        PopupWindow popupWindow = new PopupWindow(this.M, i2, i3, this.s);
        this.i = popupWindow;
        popupWindow.showAtLocation(this.y, 17, 0, 0);
        this.i.setOnDismissListener(this);
        this.i.setBackgroundDrawable(new BitmapDrawable());
        this.i.setOutsideTouchable(true);
        this.i.setTouchInterceptor(new a());
        AnimationDrawable animationDrawable = this.H;
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            this.H.start();
        }
    }

    public void c() {
        this.c.a(this.g);
        this.e.unregisterSysEventListener(this, ISysEventListener.SysEventType.onKeyUp);
        EventDispatchManager.getInstance().removeListener(this);
    }

    /* access modifiers changed from: package-private */
    public void b(String str) {
        String trim = this.j.getText().toString().trim();
        if (!TextUtils.isEmpty(trim)) {
            if (trim.length() != str.length()) {
                this.j.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
            } else if (this.j.getLayoutParams() != null) {
                this.j.setLayoutParams(new LinearLayout.LayoutParams(this.j.getWidth(), this.j.getHeight()));
            }
        }
        if (TextUtils.isEmpty(str)) {
            this.k.setVisibility(8);
            this.j.setVisibility(8);
            return;
        }
        this.j.setText(str);
    }

    private void a(View view) {
        this.j = (TextView) view.findViewById(NativeUIR.ID_TEXT_LOADING_DCLOUD);
        this.l = (ProgressBar) view.findViewById(NativeUIR.ID_PROGRESSBAR_LOADING_DCLOUD);
        this.m = (ImageView) view.findViewById(NativeUIR.ID_IMAGE_LOADING_DCLOUD);
        this.k = view.findViewById(NativeUIR.ID_WAITING_SEPARATOR_DCLOUD);
    }

    private void a(String str) {
        f();
        j();
        h();
        g();
        b(str);
        e();
        i();
    }

    private int a(int i2) {
        int min = Math.min(this.z, this.A);
        int i3 = this.v;
        int i4 = (min - i3) - i3;
        if (i4 <= 0 || i2 <= i4) {
            return 1;
        }
        return i2 / i4;
    }

    private byte[] a(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                byteArrayOutputStream.write(bArr, 0, read);
            } else {
                inputStream.close();
                byteArrayOutputStream.close();
                return byteArrayOutputStream.toByteArray();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void a() {
        PopupWindow popupWindow = this.i;
        if (popupWindow != null && popupWindow.isShowing()) {
            try {
                this.i.dismiss();
            } catch (Exception unused) {
                onDismiss();
            }
        }
    }
}
