package io.dcloud.common.adapter.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import io.dcloud.base.R;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;

public class PullRefreshView extends View {
    static final byte FLAG_MOVEED = 1;
    static final byte FLAG_NO_THING = -1;
    static final byte FLAG_STARTED = 0;
    static int HEIGHT = 25;
    static int MAX_FRAME_COUNT = 9;
    static final byte STATE_NO_REFRESH = 0;
    static final byte STATE_ON_MOVE_ING = 1;
    static final byte STATE_ON_OVER = 2;
    static final byte STATE_ON_REFRESH_ING = 3;
    public static final String TAG = "PullRefreshView";
    public static final byte TYPE_PULL_DOWN_REFRESH = 1;
    public static final byte TYPE_PULL_UP_REFRESH = 0;
    static final int color_tr = 16711920;
    byte SCROLL_STATE_MAX = 1;
    byte SCROLL_STATE_MIDDLE = 2;
    byte SCROLL_STATE_MIN = 0;
    int changeStateHeight = 100;
    private int contentLeft = 0;
    private int contentTop = 0;
    private int contentWidth = 0;
    private RectF dst;
    int fontSize;
    int icon_x;
    int icon_y;
    int index = 0;
    float lastScrollY;
    boolean mCaptureTouchEnd = false;
    String mContent_down;
    String mContent_over;
    String mContent_refresh;
    boolean mEnableScrollMaxHeight = true;
    boolean mEnableScrollMinHeight = true;
    byte mFlag = 0;
    float mFontScale = 1.2f;
    Bitmap mIcon = null;
    AdaFrameItem mParent;
    boolean mRefreshState = false;
    int mScrollHeight = 0;
    byte mScrollState = 0;
    String mSecInfo = null;
    String mShowContent = null;
    byte mState = 0;
    byte mType = 1;
    Timer mUpdateProgressBar = null;
    AdaWebview mWebview;
    private float mWebviewScale;
    int maxPullHeight = 100;
    Paint paint = new Paint();
    int sScreenHeight;
    int sScreenWidth;
    private Rect src;
    float startX;
    float startY;
    boolean touch_started = false;

    public PullRefreshView(AdaFrameItem adaFrameItem, AdaWebview adaWebview) {
        super(adaFrameItem.getContext());
        this.mParent = adaFrameItem;
        this.mWebview = adaWebview;
        this.mContent_down = getResources().getString(R.string.dcloud_drop_down_refresh1);
        this.mContent_over = getResources().getString(R.string.dcloud_drop_down_refresh2);
        this.mContent_refresh = getResources().getString(R.string.dcloud_drop_down_refresh3);
        this.sScreenWidth = adaWebview.obtainApp().getInt(0);
        this.sScreenHeight = adaWebview.obtainApp().getInt(1);
        this.mWebviewScale = adaWebview.getScale();
        init((String) null);
        this.paint.setAntiAlias(true);
    }

    /* access modifiers changed from: private */
    public static void scrollToByMessage(final View view, final int i, final int i2) {
        view.post(new Runnable() {
            public void run() {
                view.scrollTo(i, i2);
            }
        });
    }

    static void smoothScrollTo(ViewGroup viewGroup, View view, int i, int i2, int i3) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask(view, viewGroup, i, i2, timer) {
            final int TIME = 500;
            View child;
            int flagTimes;
            int fromX;
            int fromY;
            ViewGroup parent;
            int timesCount;
            int toX;
            int toY;
            int vX;
            int vY;
            final /* synthetic */ Timer val$_timer;
            final /* synthetic */ ViewGroup val$pParent;
            final /* synthetic */ View val$pView;
            final /* synthetic */ int val$x;
            final /* synthetic */ int val$y;

            {
                this.val$pView = r1;
                this.val$pParent = r2;
                this.val$x = r3;
                this.val$y = r4;
                this.val$_timer = r5;
                this.child = r1;
                this.parent = r2;
                this.toX = r3;
                this.toY = r4;
                this.fromX = r2.getScrollX();
                this.fromY = this.parent.getScrollY();
                this.timesCount = 10;
                this.flagTimes = 1;
                this.vX = Math.abs(this.toX - this.fromX) / this.timesCount;
                int abs = Math.abs(this.toY - this.fromY) / this.timesCount;
                this.vY = abs;
                if (abs >= 5) {
                    this.vY = 5;
                    this.timesCount = Math.abs(this.toY - this.fromY) / this.vY;
                }
                this.vX = Math.abs(this.toX - this.fromX) / this.timesCount;
            }

            public void run() {
                int i = this.fromX + this.vX;
                int i2 = this.fromY + this.vY;
                if (this.flagTimes == this.timesCount) {
                    i = this.toX;
                    i2 = this.toY;
                }
                PullRefreshView.scrollToByMessage(this.parent, i, i2);
                if (this.flagTimes == this.timesCount) {
                    if (this.child != null) {
                        this.parent.post(new Runnable() {
                            public void run() {
                                AnonymousClass2 r0 = AnonymousClass2.this;
                                r0.parent.removeView(r0.child);
                            }
                        });
                    }
                    this.val$_timer.cancel();
                }
                this.fromX = i;
                this.fromY = i2;
                this.flagTimes++;
            }
        }, 0, (long) i3);
    }

    private void startUpdateScreenTimer() {
        stopUpdateScreenTimer();
        Timer timer = new Timer();
        this.mUpdateProgressBar = timer;
        timer.schedule(new TimerTask() {
            public void run() {
                PullRefreshView.this.updateScreen();
            }
        }, 0, 100);
    }

    private void stopUpdateScreenTimer() {
        Timer timer = this.mUpdateProgressBar;
        if (timer != null) {
            timer.cancel();
            this.mUpdateProgressBar = null;
        }
    }

    private boolean updateScrollState(byte b) {
        this.mScrollState = b;
        boolean z = false;
        if (b == this.SCROLL_STATE_MAX) {
            this.mScrollHeight = this.maxPullHeight;
            if (this.mEnableScrollMaxHeight) {
                this.mEnableScrollMaxHeight = false;
                z = true;
            }
            this.mEnableScrollMinHeight = true;
            return z;
        }
        if (b == this.SCROLL_STATE_MIN) {
            this.mEnableScrollMaxHeight = true;
            this.mScrollHeight = 0;
            if (!this.mEnableScrollMinHeight) {
                return false;
            }
            this.mEnableScrollMinHeight = false;
        } else if (b != this.SCROLL_STATE_MIDDLE) {
            return false;
        } else {
            this.mEnableScrollMinHeight = true;
            this.mEnableScrollMaxHeight = true;
        }
        return true;
    }

    public void changeStringInfo(String str) {
        this.mShowContent = str;
        this.fontSize = (int) (DeviceInfo.DEFAULT_FONT_SIZE * DeviceInfo.sDensity * this.mFontScale);
        this.paint.setAntiAlias(true);
        int length = str.length();
        float[] fArr = new float[length];
        this.paint.getTextWidths(str, fArr);
        float f = 0.0f;
        for (int i = 0; i < length; i++) {
            f += fArr[i];
        }
        this.contentWidth = (int) f;
    }

    public void init(String str) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap decodeStream = BitmapFactory.decodeStream(PlatformUtil.getResInputStream(AbsoluteConst.RES_PROGRASS_SNOW1), (Rect) null, options);
        this.mIcon = decodeStream;
        int height = decodeStream.getHeight();
        HEIGHT = height;
        float f = (float) this.sScreenWidth;
        this.contentLeft = (int) (0.43f * f);
        this.icon_x = ((int) (f * 0.41f)) - height;
        int i = HEIGHT;
        this.src = new Rect(0, 0, i, i);
        float f2 = (float) HEIGHT;
        this.dst = new RectF(0.0f, 150.0f, f2, f2);
        MAX_FRAME_COUNT = decodeStream.getWidth() / HEIGHT;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mState != 0) {
            canvas.drawColor(-1907998);
            this.paint.setColor(-16777216);
            this.paint.setTextSize((float) this.fontSize);
            canvas.drawText(this.mShowContent, (float) this.contentLeft, (float) this.contentTop, this.paint);
            Bitmap bitmap = this.mIcon;
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, this.src, this.dst, this.paint);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void onExecuting() {
        Logger.d(TAG, "onExecuting");
        this.mState = 3;
        this.mRefreshState = true;
        this.mFlag = -1;
        Logger.d(TAG, "onExecuting; mFlag = FLAG_NO_THING");
        changeStringInfo(this.mContent_refresh);
        this.mScrollHeight = this.changeStateHeight;
        smoothScrollTo((ViewGroup) this.mParent.obtainMainView(), (View) null, 0, -this.changeStateHeight, 1);
    }

    /* access modifiers changed from: package-private */
    public boolean onMove(float f, float f2) {
        boolean z;
        int i = (int) (((f2 - this.lastScrollY) * ((float) this.maxPullHeight)) / ((float) this.mWebview.mFrameView.mViewOptions.height));
        if (Math.abs(i) >= 1) {
            int i2 = this.mScrollHeight + i;
            this.mScrollHeight = i2;
            if (i2 >= this.maxPullHeight) {
                z = updateScrollState(this.SCROLL_STATE_MAX);
            } else if (i2 <= 0) {
                z = updateScrollState(this.SCROLL_STATE_MIN);
            } else {
                z = updateScrollState(this.SCROLL_STATE_MIDDLE);
            }
            byte b = this.mState;
            if (b != 3) {
                if (b == 1 && this.mScrollHeight >= this.changeStateHeight) {
                    this.mState = 2;
                    changeStringInfo(this.mContent_over);
                } else if (b == 2 && this.mScrollHeight < this.changeStateHeight) {
                    this.mState = 1;
                    changeStringInfo(this.mContent_down);
                }
            }
            if (z) {
                if (this.mFlag == 0) {
                    this.mFlag = 1;
                    Logger.d(TAG, "onMove; mFlag=FLAG_MOVEED");
                    startUpdateScreenTimer();
                }
                this.mParent.obtainMainView().scrollBy(0, -i);
                this.lastScrollY = f2;
            }
            return z;
        } else if (this.mScrollHeight > 0) {
            return true;
        } else {
            float f3 = (float) i;
            if ((f3 <= 0.5f && f3 >= -0.5f) || this.mFlag != 0) {
                return false;
            }
            this.mFlag = 1;
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public void onPullDown_end() {
        if (this.mScrollHeight <= this.changeStateHeight) {
            this.mState = 0;
            this.mScrollHeight = 0;
            this.mFlag = -1;
            Logger.d(TAG, "onPullDown_end; mFlag = FLAG_NO_THING");
            changeStringInfo(this.mContent_down);
            Timer timer = this.mUpdateProgressBar;
            if (timer != null) {
                timer.cancel();
            }
            this.mParent.obtainMainView().scrollTo(0, 0);
            stopUpdateScreenTimer();
        } else {
            smoothScrollToStateHeight(true);
        }
        this.mRefreshState = false;
    }

    /* access modifiers changed from: package-private */
    public void onPullDown_start(float f, float f2) {
        if (!this.touch_started) {
            Logger.d(TAG, "onPullDown_start");
            this.startX = f;
            this.startY = f2;
            this.lastScrollY = f2;
            if (getParent() == null) {
                int i = this.maxPullHeight;
                ViewOptions viewOptions = this.mWebview.mViewOptions;
                ((ViewGroup) this.mParent.obtainMainView()).addView(this, 0, new AbsoluteLayout.LayoutParams(-1, i, viewOptions.left, viewOptions.top - i));
            }
            byte b = this.mState;
            if (b == 0) {
                this.mState = 1;
                this.mFlag = 0;
            } else if (b == 3) {
                this.mFlag = 0;
            }
            this.touch_started = true;
        }
    }

    public void parseJsonOption(JSONObject jSONObject) {
        try {
            if (!jSONObject.isNull("height")) {
                int convertToScreenInt = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject, "height"), this.mWebview.mFrameView.mViewOptions.height, this.changeStateHeight, this.mWebviewScale);
                this.changeStateHeight = convertToScreenInt;
                this.maxPullHeight = convertToScreenInt;
            }
            if (!jSONObject.isNull(AbsoluteConst.PULL_REFRESH_RANGE)) {
                this.maxPullHeight = PdrUtil.convertToScreenInt(jSONObject.getString(AbsoluteConst.PULL_REFRESH_RANGE), this.mWebview.mFrameView.mViewOptions.height, this.changeStateHeight, this.mWebviewScale);
            }
            if (!jSONObject.isNull(AbsoluteConst.PULL_REFRESH_CONTENTDOWN)) {
                changeStringInfo(JSONUtil.getString(jSONObject.getJSONObject(AbsoluteConst.PULL_REFRESH_CONTENTDOWN), AbsoluteConst.PULL_REFRESH_CAPTION));
            }
            if (!jSONObject.isNull(AbsoluteConst.PULL_REFRESH_CONTENTOVER)) {
                this.mContent_over = JSONUtil.getString(jSONObject.getJSONObject(AbsoluteConst.PULL_REFRESH_CONTENTOVER), AbsoluteConst.PULL_REFRESH_CAPTION);
            }
            if (!jSONObject.isNull(AbsoluteConst.PULL_REFRESH_CONTENTREFRESH)) {
                this.mContent_refresh = JSONUtil.getString(jSONObject.getJSONObject(AbsoluteConst.PULL_REFRESH_CONTENTREFRESH), AbsoluteConst.PULL_REFRESH_CAPTION);
            }
            int max = Math.max(this.maxPullHeight - this.changeStateHeight, 0);
            Paint.FontMetricsInt fontMetricsInt = DeviceInfo.sPaint.getFontMetricsInt();
            int i = this.changeStateHeight;
            this.contentTop = (i >> 1) + max + ((fontMetricsInt.bottom - fontMetricsInt.top) >> 1);
            int i2 = HEIGHT;
            int i3 = max + ((i - i2) >> 1);
            this.icon_y = i3;
            RectF rectF = this.dst;
            int i4 = this.icon_x;
            rectF.set((float) i4, (float) i3, (float) (i4 + i2), (float) (i3 + i2));
            Logger.d(TAG, "height=" + this.changeStateHeight + ";range=" + this.maxPullHeight + ";contentdown=" + this.mShowContent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: package-private */
    public void setColorByParentChild(View view) {
        for (int i = 0; i != 2; i++) {
            view = (View) view.getParent();
            view.setBackgroundColor(color_tr);
        }
    }

    /* access modifiers changed from: package-private */
    public void smoothScrollToStateHeight(boolean z) {
        if (z) {
            this.mScrollHeight = this.changeStateHeight;
            smoothScrollTo((ViewGroup) this.mParent.obtainMainView(), (View) null, 0, -this.changeStateHeight, 1);
        } else if (this.mScrollHeight > this.changeStateHeight) {
            smoothScrollToStateHeight(true);
        } else {
            this.mRefreshState = false;
        }
    }

    public void updateScreen() {
        int i = this.index + 1;
        this.index = i;
        if (i >= MAX_FRAME_COUNT) {
            this.index = 0;
        }
        Rect rect = this.src;
        int i2 = HEIGHT;
        int i3 = this.index;
        rect.set(i2 * i3, 0, (i3 + 1) * i2, i2);
        postInvalidate();
    }
}
