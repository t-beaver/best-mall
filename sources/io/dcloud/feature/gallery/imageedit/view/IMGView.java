package io.dcloud.feature.gallery.imageedit.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import io.dcloud.feature.gallery.imageedit.c.a;
import io.dcloud.feature.gallery.imageedit.c.d;
import io.dcloud.feature.gallery.imageedit.c.j.e;

public class IMGView extends FrameLayout implements Runnable, ScaleGestureDetector.OnScaleGestureListener, ValueAnimator.AnimatorUpdateListener, e.a, Animator.AnimatorListener {
    private io.dcloud.feature.gallery.imageedit.c.b a;
    /* access modifiers changed from: private */
    public io.dcloud.feature.gallery.imageedit.c.a b;
    private GestureDetector c;
    private ScaleGestureDetector d;
    private io.dcloud.feature.gallery.imageedit.c.f.a e;
    private c f;
    private int g;
    private Paint h;
    private Paint i;

    class a implements a.c {
        a() {
        }

        public void a() {
            IMGView.this.e();
            IMGView.this.b.b((a.c) this);
        }
    }

    private class b extends GestureDetector.SimpleOnGestureListener {
        private b() {
        }

        public boolean onDown(MotionEvent motionEvent) {
            return true;
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return super.onFling(motionEvent, motionEvent2, f, f2);
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return IMGView.this.a(f, f2);
        }

        /* synthetic */ b(IMGView iMGView, a aVar) {
            this();
        }
    }

    public IMGView(Context context) {
        this(context, (AttributeSet) null, 0);
    }

    /* access modifiers changed from: private */
    public void e() {
        invalidate();
        j();
        a(this.b.c((float) getScrollX(), (float) getScrollY()), this.b.b((float) getScrollX(), (float) getScrollY()));
    }

    private boolean f(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            return b(motionEvent);
        }
        if (actionMasked != 1) {
            if (actionMasked == 2) {
                return c(motionEvent);
            }
            if (actionMasked != 3) {
                return false;
            }
        }
        if (!this.f.b(motionEvent.getPointerId(0)) || !f()) {
            return false;
        }
        return true;
    }

    private void j() {
        io.dcloud.feature.gallery.imageedit.c.f.a aVar = this.e;
        if (aVar != null) {
            aVar.cancel();
        }
    }

    public void c() {
        if (!d()) {
            this.b.a(-90);
            e();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean d() {
        io.dcloud.feature.gallery.imageedit.c.f.a aVar = this.e;
        return aVar != null && aVar.isRunning();
    }

    /* access modifiers changed from: package-private */
    public boolean g() {
        Log.d("IMGView", "onSteady: isHoming=" + d());
        if (d()) {
            return false;
        }
        this.b.e((float) getScrollX(), (float) getScrollY());
        e();
        return true;
    }

    public io.dcloud.feature.gallery.imageedit.c.b getMode() {
        return this.b.c();
    }

    public void h() {
        this.b.s();
        e();
    }

    public Bitmap i() {
        this.b.t();
        float e2 = 1.0f / this.b.e();
        RectF rectF = new RectF(this.b.b());
        Matrix matrix = new Matrix();
        matrix.setRotate(this.b.d(), rectF.centerX(), rectF.centerY());
        matrix.mapRect(rectF);
        matrix.setScale(e2, e2, rectF.left, rectF.top);
        matrix.mapRect(rectF);
        Bitmap createBitmap = Bitmap.createBitmap(Math.round(rectF.width()), Math.round(rectF.height()), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.translate(-rectF.left, -rectF.top);
        canvas.scale(e2, e2, rectF.left, rectF.top);
        a(canvas);
        return createBitmap;
    }

    public void k() {
        this.b.w();
        invalidate();
    }

    public void l() {
        this.b.x();
        invalidate();
    }

    public void onAnimationCancel(Animator animator) {
        Log.d("IMGView", "onAnimationCancel");
        this.b.a(this.e.a());
    }

    public void onAnimationEnd(Animator animator) {
        Log.d("IMGView", "onAnimationEnd");
        if (this.b.a((float) getScrollX(), (float) getScrollY(), this.e.a())) {
            a(this.b.a((float) getScrollX(), (float) getScrollY()));
        }
    }

    public void onAnimationRepeat(Animator animator) {
    }

    public void onAnimationStart(Animator animator) {
        Log.d("IMGView", "onAnimationStart");
        this.b.b(this.e.a());
    }

    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.b.a(valueAnimator.getAnimatedFraction());
        a((io.dcloud.feature.gallery.imageedit.c.i.a) valueAnimator.getAnimatedValue());
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this);
        this.b.r();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        a(canvas);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            return a(motionEvent) || super.onInterceptTouchEvent(motionEvent);
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i2, int i3, int i4, int i5) {
        super.onLayout(z, i2, i3, i4, i5);
        if (z) {
            this.b.h((float) (i4 - i2), (float) (i5 - i3));
        }
    }

    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        if (this.g <= 1) {
            return false;
        }
        this.b.a(scaleGestureDetector.getScaleFactor(), ((float) getScrollX()) + scaleGestureDetector.getFocusX(), ((float) getScrollY()) + scaleGestureDetector.getFocusY());
        invalidate();
        return true;
    }

    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
        if (this.g <= 1) {
            return false;
        }
        this.b.p();
        return true;
    }

    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        this.b.q();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            removeCallbacks(this);
        } else if (actionMasked == 1 || actionMasked == 3) {
            postDelayed(this, 1200);
        }
        return d(motionEvent);
    }

    public void run() {
        if (!g()) {
            postDelayed(this, 500);
        }
    }

    public void setDoodleTouchListener(a.b bVar) {
        this.b.a(bVar);
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.b.a(bitmap);
        invalidate();
    }

    public void setMode(io.dcloud.feature.gallery.imageedit.c.b bVar) {
        this.a = this.b.c();
        this.b.a(bVar);
        this.f.a(bVar);
        if (this.b.j()) {
            e();
        } else {
            this.b.a((a.c) new a());
        }
    }

    public void setPenColor(int i2) {
        this.f.a(i2);
    }

    public IMGView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public void b() {
        this.b.a((float) getScrollX(), (float) getScrollY());
        setMode(this.a);
        e();
    }

    private static class c extends io.dcloud.feature.gallery.imageedit.c.c {
        private int e;

        private c() {
            this.e = Integer.MIN_VALUE;
        }

        /* access modifiers changed from: package-private */
        public void a(float f, float f2) {
            this.a.lineTo(f, f2);
        }

        /* access modifiers changed from: package-private */
        public void b(float f, float f2) {
            this.a.reset();
            this.a.moveTo(f, f2);
            this.e = Integer.MIN_VALUE;
        }

        /* access modifiers changed from: package-private */
        public void c(int i) {
            this.e = i;
        }

        /* access modifiers changed from: package-private */
        public boolean e() {
            return this.a.isEmpty();
        }

        /* access modifiers changed from: package-private */
        public void f() {
            this.a.reset();
            this.e = Integer.MIN_VALUE;
        }

        /* access modifiers changed from: package-private */
        public io.dcloud.feature.gallery.imageedit.c.c g() {
            if (b() == io.dcloud.feature.gallery.imageedit.c.b.DOODLE) {
                a(14.0f);
            } else {
                a(72.0f);
            }
            return new io.dcloud.feature.gallery.imageedit.c.c(new Path(this.a), b(), a(), d());
        }

        /* synthetic */ c(a aVar) {
            this();
        }

        /* access modifiers changed from: package-private */
        public boolean b(int i) {
            return this.e == i;
        }
    }

    public IMGView(Context context, AttributeSet attributeSet, int i2) {
        super(context, attributeSet, i2);
        this.a = io.dcloud.feature.gallery.imageedit.c.b.NONE;
        this.b = new io.dcloud.feature.gallery.imageedit.c.a();
        this.f = new c((a) null);
        this.g = 0;
        this.h = new Paint(1);
        this.i = new Paint(1);
        this.h.setStyle(Paint.Style.STROKE);
        this.h.setStrokeWidth(14.0f);
        this.h.setColor(-65536);
        this.h.setPathEffect(new CornerPathEffect(14.0f));
        this.h.setStrokeCap(Paint.Cap.ROUND);
        this.h.setStrokeJoin(Paint.Join.ROUND);
        this.i.setStyle(Paint.Style.STROKE);
        this.i.setStrokeWidth(72.0f);
        this.i.setColor(-16777216);
        this.i.setPathEffect(new CornerPathEffect(72.0f));
        this.i.setStrokeCap(Paint.Cap.ROUND);
        this.i.setStrokeJoin(Paint.Join.ROUND);
        a(context);
        this.b.b(io.dcloud.feature.gallery.imageedit.c.k.c.a(getContext()));
    }

    private void a(Context context) {
        this.f.a(this.b.c());
        GestureDetector gestureDetector = new GestureDetector(context, new b(this, (a) null));
        this.c = gestureDetector;
        gestureDetector.setIsLongpressEnabled(false);
        this.d = new ScaleGestureDetector(context, this);
    }

    /* access modifiers changed from: package-private */
    public boolean d(MotionEvent motionEvent) {
        boolean z;
        if (d()) {
            return false;
        }
        this.g = motionEvent.getPointerCount();
        boolean onTouchEvent = this.d.onTouchEvent(motionEvent);
        io.dcloud.feature.gallery.imageedit.c.b c2 = this.b.c();
        if (c2 == io.dcloud.feature.gallery.imageedit.c.b.NONE || c2 == io.dcloud.feature.gallery.imageedit.c.b.CLIP) {
            z = e(motionEvent);
        } else if (this.g > 1) {
            f();
            z = e(motionEvent);
        } else {
            z = f(motionEvent);
        }
        boolean z2 = onTouchEvent | z;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.b.f(motionEvent.getX(), motionEvent.getY());
            if (this.b.c() == io.dcloud.feature.gallery.imageedit.c.b.CLIP) {
                invalidate();
            }
        } else if (actionMasked == 1 || actionMasked == 3) {
            this.b.g((float) getScrollX(), (float) getScrollY());
            e();
        }
        return z2;
    }

    private boolean c(MotionEvent motionEvent) {
        if (!this.f.b(motionEvent.getPointerId(0))) {
            return false;
        }
        this.f.a(motionEvent.getX(), motionEvent.getY());
        invalidate();
        return true;
    }

    public void b(int i2, int i3) {
        io.dcloud.feature.gallery.imageedit.c.a aVar = this.b;
        if (aVar != null) {
            aVar.a(i2, i3);
        }
    }

    private boolean e(MotionEvent motionEvent) {
        return this.c.onTouchEvent(motionEvent);
    }

    private void a(io.dcloud.feature.gallery.imageedit.c.i.a aVar, io.dcloud.feature.gallery.imageedit.c.i.a aVar2) {
        if (this.e == null) {
            io.dcloud.feature.gallery.imageedit.c.f.a aVar3 = new io.dcloud.feature.gallery.imageedit.c.f.a();
            this.e = aVar3;
            aVar3.addUpdateListener(this);
            this.e.addListener(this);
        }
        this.e.a(aVar, aVar2);
        this.e.start();
    }

    private boolean b(MotionEvent motionEvent) {
        this.f.b(motionEvent.getX(), motionEvent.getY());
        this.f.c(motionEvent.getPointerId(0));
        return true;
    }

    public <V extends View & io.dcloud.feature.gallery.imageedit.c.j.a> void c(V v) {
        this.b.f((io.dcloud.feature.gallery.imageedit.c.j.a) v);
        invalidate();
    }

    public <V extends View & io.dcloud.feature.gallery.imageedit.c.j.a> void b(V v) {
        this.b.d((io.dcloud.feature.gallery.imageedit.c.j.a) v);
        invalidate();
    }

    private boolean f() {
        if (this.f.e()) {
            return false;
        }
        this.b.a(this.f.g(), (float) getScrollX(), (float) getScrollY());
        this.f.f();
        invalidate();
        return true;
    }

    public void a() {
        this.b.u();
        setMode(this.a);
    }

    private void a(Canvas canvas) {
        canvas.save();
        RectF b2 = this.b.b();
        canvas.rotate(this.b.d(), b2.centerX(), b2.centerY());
        this.b.b(canvas);
        if (!this.b.k() || (this.b.c() == io.dcloud.feature.gallery.imageedit.c.b.MOSAIC && !this.f.e())) {
            int c2 = this.b.c(canvas);
            if (this.b.c() == io.dcloud.feature.gallery.imageedit.c.b.MOSAIC && !this.f.e()) {
                this.h.setStrokeWidth(72.0f);
                canvas.save();
                RectF b3 = this.b.b();
                canvas.rotate(-this.b.d(), b3.centerX(), b3.centerY());
                canvas.translate((float) getScrollX(), (float) getScrollY());
                canvas.drawPath(this.f.c(), this.h);
                canvas.restore();
            }
            this.b.a(canvas, c2);
        }
        this.b.a(canvas);
        if (this.b.c() == io.dcloud.feature.gallery.imageedit.c.b.DOODLE && !this.f.e()) {
            this.h.setColor(this.f.a());
            this.h.setStrokeWidth(14.0f);
            canvas.save();
            RectF b4 = this.b.b();
            canvas.rotate(-this.b.d(), b4.centerX(), b4.centerY());
            canvas.translate((float) getScrollX(), (float) getScrollY());
            canvas.drawPath(this.f.c(), this.h);
            canvas.restore();
        }
        if (this.b.i()) {
            this.b.f(canvas);
        }
        this.b.d(canvas);
        canvas.restore();
        if (!this.b.i()) {
            this.b.e(canvas);
            this.b.f(canvas);
        }
        if (this.b.c() == io.dcloud.feature.gallery.imageedit.c.b.CLIP) {
            canvas.save();
            canvas.translate((float) getScrollX(), (float) getScrollY());
            this.b.a(canvas, (float) getScrollX(), (float) getScrollY());
            canvas.restore();
        }
    }

    public <V extends View & io.dcloud.feature.gallery.imageedit.c.j.a> void a(V v, FrameLayout.LayoutParams layoutParams) {
        if (v != null) {
            addView(v, layoutParams);
            ((e) v).a((e.a) this);
            this.b.a(v);
        }
    }

    public void a(d dVar) {
        IMGStickerTextView iMGStickerTextView = new IMGStickerTextView(getContext());
        iMGStickerTextView.setText(dVar);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        iMGStickerTextView.setX((float) getScrollX());
        iMGStickerTextView.setY((float) getScrollY());
        a(iMGStickerTextView, layoutParams);
    }

    /* access modifiers changed from: package-private */
    public boolean a(MotionEvent motionEvent) {
        if (d()) {
            j();
            return true;
        } else if (this.b.c() == io.dcloud.feature.gallery.imageedit.c.b.CLIP) {
            return true;
        } else {
            return false;
        }
    }

    private void a(io.dcloud.feature.gallery.imageedit.c.i.a aVar) {
        this.b.d(aVar.c);
        this.b.c(aVar.d);
        if (!a(Math.round(aVar.a), Math.round(aVar.b))) {
            invalidate();
        }
    }

    private boolean a(int i2, int i3) {
        if (getScrollX() == i2 && getScrollY() == i3) {
            return false;
        }
        scrollTo(i2, i3);
        return true;
    }

    public <V extends View & io.dcloud.feature.gallery.imageedit.c.j.a> boolean a(V v) {
        io.dcloud.feature.gallery.imageedit.c.a aVar = this.b;
        if (aVar != null) {
            aVar.e((io.dcloud.feature.gallery.imageedit.c.j.a) v);
        }
        ((e) v).b(this);
        ViewParent parent = v.getParent();
        if (parent == null) {
            return true;
        }
        ((ViewGroup) parent).removeView(v);
        return true;
    }

    /* access modifiers changed from: private */
    public boolean a(float f2, float f3) {
        io.dcloud.feature.gallery.imageedit.c.i.a a2 = this.b.a((float) getScrollX(), (float) getScrollY(), -f2, -f3);
        if (a2 == null) {
            return a(getScrollX() + Math.round(f2), getScrollY() + Math.round(f3));
        }
        a(a2);
        return true;
    }
}
