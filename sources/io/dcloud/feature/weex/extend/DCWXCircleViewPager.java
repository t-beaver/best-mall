package io.dcloud.feature.weex.extend;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.viewpager.widget.ViewPager;
import com.taobao.weex.ui.view.WXCircleViewPager;

public class DCWXCircleViewPager extends WXCircleViewPager {
    private boolean isVertical = false;
    private int pointCounr = 0;

    public DCWXCircleViewPager(Context context) {
        super(context);
    }

    public DCWXCircleViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private MotionEvent swapTouchEvent(MotionEvent motionEvent) {
        float width = (float) getWidth();
        float height = (float) getHeight();
        motionEvent.setLocation((motionEvent.getY() / height) * width, (motionEvent.getX() / width) * height);
        return motionEvent;
    }

    public boolean isVertical() {
        return this.isVertical;
    }

    public void setVertical(boolean z) {
        this.isVertical = z;
        if (z) {
            setPageTransformer(false, new ViewPager.PageTransformer() {
                public void transformPage(View view, float f) {
                    view.setTranslationX(((float) view.getWidth()) * (-f));
                    view.setTranslationY(f * ((float) view.getHeight()));
                }
            });
        } else {
            setPageTransformer(false, (ViewPager.PageTransformer) null);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        try {
            if (!this.isVertical) {
                return super.onInterceptTouchEvent(motionEvent);
            }
            boolean onInterceptTouchEvent = super.onInterceptTouchEvent(swapTouchEvent(motionEvent));
            swapTouchEvent(motionEvent);
            return onInterceptTouchEvent;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return super.onInterceptTouchEvent(motionEvent);
        } catch (ArrayIndexOutOfBoundsException e2) {
            e2.printStackTrace();
            return super.onInterceptTouchEvent(motionEvent);
        }
    }

    public int getPointCounr() {
        return this.pointCounr;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002a, code lost:
        if (r4 != 3) goto L_0x0034;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(android.view.MotionEvent r7) {
        /*
            r6 = this;
            int r0 = r7.getAction()
            r1 = 0
            r2 = 3
            r3 = 1
            if (r0 == 0) goto L_0x0011
            if (r0 == r3) goto L_0x000e
            if (r0 == r2) goto L_0x000e
            goto L_0x0017
        L_0x000e:
            r6.pointCounr = r1
            goto L_0x0017
        L_0x0011:
            int r0 = r7.getPointerCount()
            r6.pointCounr = r0
        L_0x0017:
            boolean r0 = r6.isVertical
            if (r0 == 0) goto L_0x003d
            android.view.ViewParent r0 = r6.getParent()
            android.view.ViewGroup r0 = (android.view.ViewGroup) r0
            int r4 = r7.getAction()
            if (r4 == r3) goto L_0x0031
            r5 = 2
            if (r4 == r5) goto L_0x002d
            if (r4 == r2) goto L_0x0031
            goto L_0x0034
        L_0x002d:
            r0.requestDisallowInterceptTouchEvent(r3)
            goto L_0x0034
        L_0x0031:
            r0.requestDisallowInterceptTouchEvent(r1)
        L_0x0034:
            android.view.MotionEvent r7 = r6.swapTouchEvent(r7)
            boolean r7 = super.onTouchEvent(r7)
            return r7
        L_0x003d:
            boolean r7 = super.onTouchEvent(r7)
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.weex.extend.DCWXCircleViewPager.onTouchEvent(android.view.MotionEvent):boolean");
    }
}
