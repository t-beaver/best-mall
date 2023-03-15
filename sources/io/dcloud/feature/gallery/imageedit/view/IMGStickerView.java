package io.dcloud.feature.gallery.imageedit.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import io.dcloud.base.R;
import io.dcloud.feature.gallery.imageedit.c.j.a;
import io.dcloud.feature.gallery.imageedit.c.j.b;
import io.dcloud.feature.gallery.imageedit.c.j.c;
import io.dcloud.feature.gallery.imageedit.c.j.d;
import io.dcloud.feature.gallery.imageedit.c.j.e;

public abstract class IMGStickerView extends ViewGroup implements a, View.OnClickListener {
    private View a;
    private float b;
    private int c;
    private d d;
    private c<IMGStickerView> e;
    private ImageView f;
    private ImageView g;
    private float h;
    private Paint i;
    private Matrix j;
    private RectF k;
    private Rect l;

    public IMGStickerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    private ViewGroup.LayoutParams getAnchorLayoutParams() {
        return new ViewGroup.LayoutParams(48, 48);
    }

    private ViewGroup.LayoutParams getContentLayoutParams() {
        return new ViewGroup.LayoutParams(-2, -2);
    }

    public abstract View a(Context context);

    public void a(float f2) {
        setScale(getScale() * f2);
    }

    public void b(Context context) {
        setBackgroundColor(0);
        View a2 = a(context);
        this.a = a2;
        addView(a2, getContentLayoutParams());
        ImageView imageView = new ImageView(context);
        this.f = imageView;
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        this.f.setImageResource(R.mipmap.image_ic_delete);
        addView(this.f, getAnchorLayoutParams());
        this.f.setOnClickListener(this);
        ImageView imageView2 = new ImageView(context);
        this.g = imageView2;
        imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
        this.g.setImageResource(R.mipmap.image_ic_adjust);
        addView(this.g, getAnchorLayoutParams());
        new b(this, this.g);
        this.e = new c<>(this);
        this.d = new d(this);
    }

    public void c() {
    }

    public void d() {
        this.e.c();
    }

    public boolean dismiss() {
        return this.e.dismiss();
    }

    public void draw(Canvas canvas) {
        if (b()) {
            canvas.drawRect(24.0f, 24.0f, (float) (getWidth() - 24), (float) (getHeight() - 24), this.i);
        }
        super.draw(canvas);
    }

    /* access modifiers changed from: protected */
    public boolean drawChild(Canvas canvas, View view, long j2) {
        return b() && super.drawChild(canvas, view, j2);
    }

    public RectF getFrame() {
        return this.e.getFrame();
    }

    public float getScale() {
        return this.b;
    }

    public void onClick(View view) {
        if (view == this.f) {
            d();
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!b() && motionEvent.getAction() == 0) {
            this.c = 0;
            a();
            return true;
        } else if (!b() || !super.onInterceptTouchEvent(motionEvent)) {
            return false;
        } else {
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i2, int i3, int i4, int i5) {
        this.k.set((float) i2, (float) i3, (float) i4, (float) i5);
        if (getChildCount() != 0) {
            ImageView imageView = this.f;
            imageView.layout(0, 0, imageView.getMeasuredWidth(), this.f.getMeasuredHeight());
            ImageView imageView2 = this.g;
            int i6 = i4 - i2;
            int i7 = i5 - i3;
            imageView2.layout(i6 - imageView2.getMeasuredWidth(), i7 - this.g.getMeasuredHeight(), i6, i7);
            int i8 = i6 >> 1;
            int i9 = i7 >> 1;
            int measuredWidth = this.a.getMeasuredWidth() >> 1;
            int measuredHeight = this.a.getMeasuredHeight() >> 1;
            this.a.layout(i8 - measuredWidth, i9 - measuredHeight, i8 + measuredWidth, i9 + measuredHeight);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i2, int i3) {
        int childCount = getChildCount();
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        for (int i7 = 0; i7 < childCount; i7++) {
            View childAt = getChildAt(i7);
            if (childAt.getVisibility() != 8) {
                childAt.measure(i2, i3);
                i6 = Math.round(Math.max((float) i6, ((float) childAt.getMeasuredWidth()) * childAt.getScaleX()));
                i4 = Math.round(Math.max((float) i4, ((float) childAt.getMeasuredHeight()) * childAt.getScaleY()));
                i5 = ViewGroup.combineMeasuredStates(i5, childAt.getMeasuredState());
            }
        }
        setMeasuredDimension(ViewGroup.resolveSizeAndState(Math.max(i6, getSuggestedMinimumWidth()), i2, i5), ViewGroup.resolveSizeAndState(Math.max(i4, getSuggestedMinimumHeight()), i3, i5 << 16));
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean a2 = this.d.a(this, motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.c++;
        } else if (actionMasked == 1 && this.c > 1 && motionEvent.getEventTime() - motionEvent.getDownTime() < ((long) ViewConfiguration.getTapTimeout())) {
            c();
            return true;
        }
        return super.onTouchEvent(motionEvent) | a2;
    }

    public void setScale(float f2) {
        this.b = f2;
        this.a.setScaleX(f2);
        this.a.setScaleY(this.b);
        float left = (float) ((getLeft() + getRight()) >> 1);
        float top = (float) ((getTop() + getBottom()) >> 1);
        this.k.set(left, top, left, top);
        this.k.inset((float) (-(this.a.getMeasuredWidth() >> 1)), (float) (-(this.a.getMeasuredHeight() >> 1)));
        Matrix matrix = this.j;
        float f3 = this.b;
        matrix.setScale(f3, f3, this.k.centerX(), this.k.centerY());
        this.j.mapRect(this.k);
        this.k.round(this.l);
        Rect rect = this.l;
        layout(rect.left, rect.top, rect.right, rect.bottom);
    }

    public IMGStickerView(Context context, AttributeSet attributeSet, int i2) {
        super(context, attributeSet, i2);
        this.b = 1.0f;
        this.c = 0;
        this.h = 4.0f;
        this.j = new Matrix();
        this.k = new RectF();
        this.l = new Rect();
        Paint paint = new Paint(1);
        this.i = paint;
        paint.setColor(-1);
        this.i.setStyle(Paint.Style.STROKE);
        this.i.setStrokeWidth(3.0f);
        b(context);
    }

    public boolean a() {
        return this.e.a();
    }

    public void a(Canvas canvas) {
        canvas.translate(this.a.getX(), this.a.getY());
        this.a.draw(canvas);
    }

    public void a(e.a aVar) {
        this.e.a(aVar);
    }

    public boolean b() {
        return this.e.b();
    }

    public void b(e.a aVar) {
        this.e.b(aVar);
    }
}
