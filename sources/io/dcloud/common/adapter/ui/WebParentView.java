package io.dcloud.common.adapter.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import io.dcloud.common.adapter.util.EventActionInfo;
import io.dcloud.common.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class WebParentView extends FrameLayout {
    private boolean mIsBeingDragged = true;
    float mLastMotionX;
    float mLastMotionY;
    AdaWebview mWebView;

    public WebParentView(Context context) {
        super(context);
    }

    private boolean isCanCircleRefresh() {
        return this.mWebView.obtainFrameView() != null && ((AdaFrameView) this.mWebView.obtainFrameView()).getCircleRefreshView() != null && ((AdaFrameView) this.mWebView.obtainFrameView()).getCircleRefreshView().isRefreshEnable() && this.mWebView.getDCWebView().getWebViewScrollY() <= 0;
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        AdaWebview adaWebview = this.mWebView;
        if (adaWebview instanceof AdaUniWebView) {
            ((AdaUniWebView) this.mWebView).fireEvent(new EventActionInfo(AbsoluteConst.EVENTS_PLUS_ORIENTATI_ONCHANGE));
            return;
        }
        adaWebview.executeScript(StringUtil.format(AbsoluteConst.EVENTS_DOCUMENT_EXECUTE_TEMPLATE, AbsoluteConst.EVENTS_PLUS_ORIENTATI_ONCHANGE));
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!isCanCircleRefresh()) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        float rawX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mLastMotionY = rawY;
            this.mLastMotionX = rawX;
            this.mIsBeingDragged = false;
        } else if (action == 2) {
            float f = rawX - this.mLastMotionX;
            float f2 = rawY - this.mLastMotionY;
            if (f2 > 20.0f && Math.abs(f2) > Math.abs(f)) {
                this.mIsBeingDragged = true;
                motionEvent.setAction(0);
                onTouchEvent(motionEvent);
            }
        }
        return this.mIsBeingDragged;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.mWebView != null) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("width", (double) (((float) i) / this.mWebView.getScale()));
                jSONObject.put("height", (double) (((float) i2) / this.mWebView.getScale()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            this.mWebView.mFrameView.dispatchFrameViewEvents("resize", jSONObject.toString());
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean isCanCircleRefresh = isCanCircleRefresh();
        if (isCanCircleRefresh && ((AdaFrameView) this.mWebView.obtainFrameView()).getCircleRefreshView().onSelfTouchEvent(motionEvent)) {
            return true;
        }
        if (isCanCircleRefresh) {
            return false;
        }
        return super.onTouchEvent(motionEvent);
    }

    public void setWebView(AdaWebview adaWebview) {
        this.mWebView = adaWebview;
    }
}
