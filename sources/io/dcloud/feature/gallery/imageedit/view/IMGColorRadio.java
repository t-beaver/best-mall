package io.dcloud.feature.gallery.imageedit.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RadioButton;
import io.dcloud.base.R;

public class IMGColorRadio extends RadioButton implements ValueAnimator.AnimatorUpdateListener {
    private int a;
    private int b;
    private float c;
    private ValueAnimator d;
    private Paint e;

    public IMGColorRadio(Context context) {
        this(context, (AttributeSet) null, 0);
    }

    private void a(Context context, AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.IMGColorRadio);
        this.a = obtainStyledAttributes.getColor(R.styleable.IMGColorRadio_image_color, -1);
        this.b = obtainStyledAttributes.getColor(R.styleable.IMGColorRadio_image_stroke_color, -1);
        obtainStyledAttributes.recycle();
        setButtonDrawable((Drawable) null);
        this.e.setColor(this.a);
        this.e.setStrokeWidth(5.0f);
    }

    private float b(float f) {
        return f * ((this.c * 0.29999995f) + 0.6f);
    }

    private ValueAnimator getAnimator() {
        if (this.d == null) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            this.d = ofFloat;
            ofFloat.addUpdateListener(this);
            this.d.setDuration(200);
            this.d.setInterpolator(new AccelerateDecelerateInterpolator());
        }
        return this.d;
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        float width = ((float) getWidth()) / 2.0f;
        float height = ((float) getHeight()) / 2.0f;
        float min = Math.min(width, height);
        canvas.save();
        this.e.setColor(this.a);
        this.e.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width, height, a(min), this.e);
        this.e.setColor(this.b);
        this.e.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(width, height, b(min), this.e);
        canvas.restore();
    }

    public int getColor() {
        return this.a;
    }

    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.c = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    public void setChecked(boolean z) {
        boolean z2 = z != isChecked();
        super.setChecked(z);
        if (z2) {
            ValueAnimator animator = getAnimator();
            if (z) {
                animator.start();
            } else {
                animator.reverse();
            }
        }
    }

    public void setColor(int i) {
        this.a = i;
        this.e.setColor(i);
    }

    public IMGColorRadio(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.a = -1;
        this.b = -1;
        this.c = 0.0f;
        this.e = new Paint(1);
        a(context, attributeSet, 0);
    }

    private float a(float f) {
        return f * ((this.c * 0.120000005f) + 0.6f);
    }

    public IMGColorRadio(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.a = -1;
        this.b = -1;
        this.c = 0.0f;
        this.e = new Paint(1);
        a(context, attributeSet, i);
    }
}
