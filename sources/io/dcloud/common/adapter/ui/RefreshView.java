package io.dcloud.common.adapter.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import io.dcloud.base.R;
import io.dcloud.common.adapter.ui.fresh.ILoadingLayout;
import io.dcloud.common.adapter.ui.fresh.PullToRefreshBase;
import io.dcloud.common.adapter.ui.webview.DCWebView;
import io.dcloud.common.adapter.util.CanvasHelper;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;

public class RefreshView implements PullToRefreshBase.OnStateChangeListener {
    public static final String TAG = "RefreshView";
    int HEIGHT = 25;
    int MAX_FRAME_COUNT = 9;
    int changeStateHeight = 100;
    private int contentLeft = 0;
    private int contentTop = 0;
    private RectF dst;
    int fontSize;
    int icon_x;
    int icon_y;
    int index = 0;
    String mContent_down;
    String mContent_over;
    String mContent_refresh;
    float mFontScale = 1.2f;
    AdaFrameView mFrameView;
    Bitmap mIcon = null;
    JSONObject mJSONObject;
    String mShowContent = null;
    ILoadingLayout.State mState = ILoadingLayout.State.RESET;
    Timer mUpdateProgressBar = null;
    AdaWebview mWebview;
    private float mWebviewScale;
    int maxPullHeight = 100;
    Paint paint = new Paint();
    private Rect src;
    float startX;
    float startY;

    public RefreshView(AdaFrameView adaFrameView, AdaWebview adaWebview) {
        this.mFrameView = adaFrameView;
        this.mContent_down = adaFrameView.getContext().getString(R.string.dcloud_drop_down_refresh1);
        this.mContent_over = adaFrameView.getContext().getString(R.string.dcloud_drop_down_refresh2);
        this.mContent_refresh = adaFrameView.getContext().getString(R.string.dcloud_drop_down_refresh3);
        this.mWebview = adaWebview;
        this.mWebviewScale = adaWebview.getScale();
        this.paint.setAntiAlias(true);
    }

    private void computePosition(Paint paint2) {
        Paint.FontMetricsInt fontMetricsInt = paint2.getFontMetricsInt();
        int i = this.changeStateHeight;
        int i2 = this.HEIGHT;
        int i3 = (i - i2) >> 1;
        this.icon_y = i3;
        this.contentTop = i3 + ((i2 - (fontMetricsInt.bottom - fontMetricsInt.top)) / 2);
        int i4 = this.mFrameView.obtainApp().getInt(0);
        float measureText = paint2.measureText(this.mShowContent);
        float f = (float) i4;
        float f2 = 0.02f * f;
        float f3 = (float) this.HEIGHT;
        int i5 = ((int) (((f - f2) - f3) - measureText)) / 2;
        this.icon_x = i5;
        this.contentLeft = (int) (((float) i5) + f2 + f3);
    }

    private void startUpdateScreenTimer() {
        stopUpdateScreenTimer();
        if (this.mFrameView.obtainMainView() != null) {
            Timer timer = new Timer();
            this.mUpdateProgressBar = timer;
            timer.schedule(new TimerTask() {
                public void run() {
                    if (RefreshView.this.mFrameView.obtainMainView() == null) {
                        cancel();
                    }
                    try {
                        RefreshView.this.updateScreen();
                        RefreshView.this.mFrameView.obtainMainView().postInvalidate(0, 0, RefreshView.this.mFrameView.obtainFrameOptions().width, RefreshView.this.maxPullHeight);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 100);
        }
    }

    private void stopUpdateScreenTimer() {
        Timer timer = this.mUpdateProgressBar;
        if (timer != null) {
            timer.cancel();
            this.mUpdateProgressBar = null;
        }
    }

    /* access modifiers changed from: private */
    public void updateScreen() {
        int i = this.index + 1;
        this.index = i;
        if (i >= this.MAX_FRAME_COUNT) {
            this.index = 0;
        }
        Rect rect = this.src;
        int i2 = this.HEIGHT;
        int i3 = this.index;
        rect.set(i2 * i3, 0, (i3 + 1) * i2, i2);
    }

    public void changeStringInfo(String str) {
        this.mShowContent = str;
        int length = str.length();
        float[] fArr = new float[length];
        this.paint.getTextWidths(str, fArr);
        for (int i = 0; i < length; i++) {
            float f = fArr[i];
        }
        this.mFrameView.obtainMainView().postInvalidate(0, 0, this.mFrameView.obtainFrameOptions().width, this.maxPullHeight);
    }

    public void init(String str) {
        this.fontSize = (int) (DeviceInfo.DEFAULT_FONT_SIZE * DeviceInfo.sDensity * this.mFontScale);
        this.paint.setAntiAlias(true);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap decodeStream = BitmapFactory.decodeStream(PlatformUtil.getResInputStream(AbsoluteConst.RES_PROGRASS_SNOW1), (Rect) null, options);
        this.mIcon = decodeStream;
        this.HEIGHT = decodeStream.getHeight();
        int i = this.HEIGHT;
        this.src = new Rect(0, 0, i, i);
        float f = (float) this.HEIGHT;
        this.dst = new RectF(0.0f, 150.0f, f, f);
        Logger.d(TAG, "height=" + this.changeStateHeight + ";range=" + this.maxPullHeight + ";contentdown=" + this.mShowContent);
        this.MAX_FRAME_COUNT = decodeStream.getWidth() / this.HEIGHT;
    }

    /* access modifiers changed from: package-private */
    public void onResize() {
        parseJsonOption(this.mJSONObject);
    }

    public void onStateChanged(ILoadingLayout.State state, boolean z) {
        DCWebView dCWebView;
        boolean z2 = this.mState != state;
        this.mState = state;
        if (!z2) {
            return;
        }
        if (state == ILoadingLayout.State.RESET) {
            Logger.d("refresh", "RefreshView RESET");
            changeStringInfo(this.mContent_down);
            stopUpdateScreenTimer();
        } else if (state == ILoadingLayout.State.PULL_TO_REFRESH) {
            Logger.d("refresh", "RefreshView PULL_TO_REFRESH");
            changeStringInfo(this.mContent_down);
        } else if (state == ILoadingLayout.State.RELEASE_TO_REFRESH) {
            Logger.d("refresh", "RefreshView RELEASE_TO_REFRESH");
            changeStringInfo(this.mContent_over);
        } else if (state == ILoadingLayout.State.REFRESHING) {
            Logger.d("refresh", "RefreshView REFRESHING");
            changeStringInfo(this.mContent_refresh);
            startUpdateScreenTimer();
            AdaWebview adaWebview = this.mWebview;
            if (adaWebview != null && (dCWebView = adaWebview.mWebViewImpl) != null) {
                dCWebView.onRefresh(3);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void paint(Canvas canvas, int i, int i2) {
        canvas.drawColor(-1907998);
        this.paint.setColor(-16777216);
        this.paint.setTextSize((float) this.fontSize);
        if (this.mShowContent != null && this.mIcon != null) {
            computePosition(this.paint);
            CanvasHelper.drawString(canvas, this.mShowContent, this.contentLeft + i, this.contentTop + i2, 17, this.paint);
            RectF rectF = this.dst;
            int i3 = i + this.icon_x;
            int i4 = i2 + this.icon_y;
            int i5 = this.HEIGHT;
            rectF.set((float) i3, (float) i4, (float) (i3 + i5), (float) (i4 + i5));
            canvas.drawBitmap(this.mIcon, this.src, this.dst, this.paint);
        }
    }

    public void parseJsonOption(JSONObject jSONObject) {
        try {
            JSONObject combinJSONObject = JSONUtil.combinJSONObject(this.mJSONObject, jSONObject);
            this.mJSONObject = combinJSONObject;
            if (!combinJSONObject.isNull("height")) {
                this.changeStateHeight = PdrUtil.convertToScreenInt(JSONUtil.getString(combinJSONObject, "height"), this.mWebview.mFrameView.mViewOptions.height, this.changeStateHeight, this.mWebviewScale);
            }
            if (!combinJSONObject.isNull(AbsoluteConst.PULL_REFRESH_RANGE)) {
                this.maxPullHeight = PdrUtil.convertToScreenInt(combinJSONObject.getString(AbsoluteConst.PULL_REFRESH_RANGE), this.mWebview.mFrameView.mViewOptions.height, this.maxPullHeight, this.mWebviewScale);
            }
            if (!combinJSONObject.isNull(AbsoluteConst.PULL_REFRESH_CONTENTDOWN)) {
                this.mContent_down = JSONUtil.getString(combinJSONObject.getJSONObject(AbsoluteConst.PULL_REFRESH_CONTENTDOWN), AbsoluteConst.PULL_REFRESH_CAPTION);
            }
            if (!combinJSONObject.isNull(AbsoluteConst.PULL_REFRESH_CONTENTOVER)) {
                this.mContent_over = JSONUtil.getString(combinJSONObject.getJSONObject(AbsoluteConst.PULL_REFRESH_CONTENTOVER), AbsoluteConst.PULL_REFRESH_CAPTION);
            }
            if (!combinJSONObject.isNull(AbsoluteConst.PULL_REFRESH_CONTENTREFRESH)) {
                this.mContent_refresh = JSONUtil.getString(combinJSONObject.getJSONObject(AbsoluteConst.PULL_REFRESH_CONTENTREFRESH), AbsoluteConst.PULL_REFRESH_CAPTION);
            }
            init((String) null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
