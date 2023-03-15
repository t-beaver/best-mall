package io.dcloud.common.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import io.dcloud.PdrR;
import io.dcloud.common.DHInterface.IReflectAble;
import java.math.BigDecimal;

public class GifImageView extends View implements IReflectAble {
    private float a;
    private float b;
    private float c;
    private Movie d;
    private long e;
    private long f;
    private long g;
    float h;
    private int i;
    private volatile boolean j;
    private volatile boolean k;
    private volatile boolean l;
    private boolean m;
    private a n;
    private int o;
    private int p;
    private int q;
    private float r;
    private float s;

    public interface a {
        void a();

        void a(float f);

        void a(boolean z);

        void b();

        void c();
    }

    public GifImageView(Context context) {
        this(context, (AttributeSet) null);
    }

    private void a(Context context, AttributeSet attributeSet, int i2) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, PdrR.STYLE_GIFVIEW, i2, 0);
        int resourceId = obtainStyledAttributes.getResourceId(PdrR.STYLE_GIFVIEW_gifSrc, 0);
        boolean z = obtainStyledAttributes.getBoolean(PdrR.STYLE_GIFVIEW_authPlay, true);
        this.i = obtainStyledAttributes.getInt(PdrR.STYLE_GIFVIEW_playCount, -1);
        if (resourceId > 0) {
            setGifResource(resourceId, (a) null);
            if (z) {
                play(this.i);
            }
        }
        obtainStyledAttributes.recycle();
        setLayerType(1, (Paint) null);
    }

    private void b() {
        if (this.m) {
            postInvalidateOnAnimation();
        }
    }

    private void c() {
        this.j = false;
        this.e = SystemClock.uptimeMillis();
        this.k = false;
        this.l = true;
        this.f = 0;
        this.g = 0;
    }

    private int getCurrentFrameTime() {
        if (this.o == 0) {
            return 0;
        }
        long uptimeMillis = SystemClock.uptimeMillis() - this.g;
        int i2 = (int) ((uptimeMillis - this.e) / ((long) this.o));
        int i3 = this.i;
        if (i3 != -1 && i2 >= i3) {
            this.l = false;
            a aVar = this.n;
            if (aVar != null) {
                aVar.c();
            }
        }
        long j2 = uptimeMillis - this.e;
        int i4 = this.o;
        float f2 = (float) (j2 % ((long) i4));
        this.h = f2 / ((float) i4);
        if (this.n != null && this.l) {
            double doubleValue = new BigDecimal((double) this.h).setScale(2, 4).doubleValue();
            if (doubleValue == 0.99d) {
                doubleValue = 1.0d;
            }
            this.n.a((float) doubleValue);
        }
        return (int) f2;
    }

    public int getDuration() {
        Movie movie = this.d;
        if (movie != null) {
            return movie.duration();
        }
        return 0;
    }

    public boolean isPaused() {
        return this.k;
    }

    public boolean isPlaying() {
        return !this.k && this.l;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.d == null) {
            return;
        }
        if (this.k || !this.l) {
            a(canvas);
            return;
        }
        if (this.j) {
            this.d.setTime(this.o - getCurrentFrameTime());
        } else {
            this.d.setTime(getCurrentFrameTime());
        }
        a(canvas);
        b();
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i2, int i3, int i4, int i5) {
        super.onLayout(z, i2, i3, i4, i5);
        this.r = ((float) (getWidth() - this.p)) / 2.0f;
        this.s = ((float) (getHeight() - this.q)) / 2.0f;
        this.m = getVisibility() == 0;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i2, int i3) {
        Movie movie = this.d;
        if (movie != null) {
            int width = movie.width();
            int height = this.d.height();
            int size = View.MeasureSpec.getSize(i2);
            float f2 = 1.0f / (((float) width) / ((float) size));
            this.c = f2;
            this.p = size;
            int i4 = (int) (((float) height) * f2);
            this.q = i4;
            setMeasuredDimension(size, i4);
            return;
        }
        setMeasuredDimension(getSuggestedMinimumWidth(), getSuggestedMinimumHeight());
    }

    public void onScreenStateChanged(int i2) {
        super.onScreenStateChanged(i2);
        boolean z = true;
        if (i2 != 1) {
            z = false;
        }
        this.m = z;
        b();
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View view, int i2) {
        super.onVisibilityChanged(view, i2);
        this.m = i2 == 0;
        b();
    }

    /* access modifiers changed from: protected */
    public void onWindowVisibilityChanged(int i2) {
        super.onWindowVisibilityChanged(i2);
        this.m = i2 == 0;
        b();
    }

    public void pause() {
        if (this.d == null || this.k || !this.l) {
            a aVar = this.n;
            if (aVar != null) {
                aVar.a(false);
                return;
            }
            return;
        }
        this.k = true;
        invalidate();
        this.f = SystemClock.uptimeMillis();
        a aVar2 = this.n;
        if (aVar2 != null) {
            aVar2.a(true);
        }
    }

    public void play(int i2) {
        this.i = i2;
        c();
        a aVar = this.n;
        if (aVar != null) {
            aVar.a();
        }
        invalidate();
    }

    public void playOver() {
        if (this.d != null) {
            play(-1);
        }
    }

    public void playReverse() {
        if (this.d != null) {
            c();
            this.j = true;
            a aVar = this.n;
            if (aVar != null) {
                aVar.a();
            }
            invalidate();
        }
    }

    public void setGifResource(int i2, a aVar) {
        if (aVar != null) {
            this.n = aVar;
        }
        c();
        Movie decodeStream = Movie.decodeStream(getResources().openRawResource(i2));
        this.d = decodeStream;
        this.o = decodeStream.duration() == 0 ? 1000 : this.d.duration();
        requestLayout();
    }

    public void setOnPlayListener(a aVar) {
        this.n = aVar;
    }

    public void setPercent(float f2) {
        int i2;
        Movie movie = this.d;
        if (movie != null && (i2 = this.o) > 0) {
            this.h = f2;
            movie.setTime((int) (((float) i2) * f2));
            b();
            a aVar = this.n;
            if (aVar != null) {
                aVar.a(f2);
            }
        }
    }

    public GifImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, -1);
    }

    public GifImageView(Context context, AttributeSet attributeSet, int i2) {
        super(context, attributeSet, i2);
        this.a = 1.0f;
        this.b = 1.0f;
        this.c = 1.0f;
        this.i = -1;
        this.j = false;
        this.m = true;
        a(context, attributeSet, i2);
    }

    public void play() {
        if (this.d != null) {
            if (!this.l) {
                play(this.i);
            } else if (this.k && this.f > 0) {
                this.k = false;
                this.g = (this.g + SystemClock.uptimeMillis()) - this.f;
                invalidate();
                a aVar = this.n;
                if (aVar != null) {
                    aVar.b();
                }
            }
        }
    }

    public void setGifResource(int i2) {
        setGifResource(i2, (a) null);
    }

    public void setGifResource(String str, a aVar) {
        this.d = Movie.decodeFile(str);
        this.n = aVar;
        c();
        this.o = this.d.duration() == 0 ? 1000 : this.d.duration();
        requestLayout();
        a aVar2 = this.n;
        if (aVar2 != null) {
            aVar2.a();
        }
    }

    private void a(Canvas canvas) {
        canvas.save();
        float f2 = this.c;
        canvas.scale(f2, f2);
        Movie movie = this.d;
        float f3 = this.r;
        float f4 = this.c;
        movie.draw(canvas, f3 / f4, this.s / f4);
        canvas.restore();
    }
}
