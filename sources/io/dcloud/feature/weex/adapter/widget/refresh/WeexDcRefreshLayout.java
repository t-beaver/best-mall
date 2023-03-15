package io.dcloud.feature.weex.adapter.widget.refresh;

import android.content.Context;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

public class WeexDcRefreshLayout extends DCWeexBaseRefreshLayout {
    private boolean mDragger;
    private float mStartX;
    private float mStartY;
    private int mTouchSlop;

    public WeexDcRefreshLayout(Context context) {
        super(context);
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001b, code lost:
        if (r0 != 3) goto L_0x0058;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onInterceptTouchEvent(android.view.MotionEvent r6) {
        /*
            r5 = this;
            boolean r0 = r5.isEnabled()
            if (r0 == 0) goto L_0x0058
            int r0 = r6.getAction()
            boolean r1 = r5.isViewRefresh()
            r2 = 0
            if (r1 != 0) goto L_0x0012
            return r2
        L_0x0012:
            if (r0 == 0) goto L_0x004a
            r1 = 1
            if (r0 == r1) goto L_0x0047
            r3 = 2
            if (r0 == r3) goto L_0x001e
            r1 = 3
            if (r0 == r1) goto L_0x0047
            goto L_0x0058
        L_0x001e:
            boolean r0 = r5.mDragger
            if (r0 == 0) goto L_0x0023
            return r2
        L_0x0023:
            float r0 = r6.getY()
            float r3 = r6.getX()
            float r4 = r5.mStartX
            float r3 = r3 - r4
            float r3 = java.lang.Math.abs(r3)
            float r4 = r5.mStartY
            float r0 = r0 - r4
            float r0 = java.lang.Math.abs(r0)
            int r4 = r5.mTouchSlop
            float r4 = (float) r4
            int r4 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
            if (r4 <= 0) goto L_0x0058
            int r0 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1))
            if (r0 <= 0) goto L_0x0058
            r5.mDragger = r1
            return r2
        L_0x0047:
            r5.mDragger = r2
            goto L_0x0058
        L_0x004a:
            float r0 = r6.getY()
            r5.mStartY = r0
            float r0 = r6.getX()
            r5.mStartX = r0
            r5.mDragger = r2
        L_0x0058:
            boolean r6 = super.onInterceptTouchEvent(r6)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.weex.adapter.widget.refresh.WeexDcRefreshLayout.onInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    private boolean isViewRefresh() {
        for (int i = 0; i < getChildCount(); i++) {
            if (canScrollVertically(getChildAt(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean canScrollVertically(View view) {
        if (view instanceof ViewGroup) {
            if (!view.canScrollVertically(-1)) {
                int i = 0;
                while (true) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    if (i >= viewGroup.getChildCount()) {
                        break;
                    } else if (canScrollVertically(viewGroup.getChildAt(i))) {
                        return true;
                    } else {
                        i++;
                    }
                }
            } else {
                return true;
            }
        }
        return false;
    }
}
