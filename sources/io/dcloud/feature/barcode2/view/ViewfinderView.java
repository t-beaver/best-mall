package io.dcloud.feature.barcode2.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View;
import com.dcloud.zxing2.ResultPoint;
import io.dcloud.feature.barcode2.decoding.IBarHandler;
import java.util.Collection;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

public final class ViewfinderView extends View {
    private static final long ANIMATION_DELAY = 100;
    private static final int OPAQUE = 255;
    IBarHandler barHandler = null;
    ShapeDrawable laserBitmap = null;
    int laserY = (-DetectorViewConfig.LASER_WIDTH);
    private Collection<ResultPoint> lastPossibleResultPoints;
    private Rect lastRect = null;
    Timer mUpdateProgressBar = null;
    private final Paint paint;
    private Collection<ResultPoint> possibleResultPoints;
    private boolean running = false;

    public ViewfinderView(Context context, IBarHandler iBarHandler) {
        super(context);
        this.barHandler = iBarHandler;
        this.paint = new Paint();
        getResources();
        this.possibleResultPoints = new HashSet(5);
    }

    private void drawDetectorCorner(Canvas canvas, Rect rect) {
        this.paint.setColor(DetectorViewConfig.cornerColor);
        int i = DetectorViewConfig.CORNER_WIDTH / 2;
        int i2 = DetectorViewConfig.CORNER_HEIGHT;
        int i3 = rect.left;
        int i4 = rect.top;
        Canvas canvas2 = canvas;
        canvas2.drawRect((float) (i3 - i), (float) (i4 - i), (float) (i3 + i2), (float) (i4 + i), this.paint);
        int i5 = rect.left;
        int i6 = rect.top;
        canvas2.drawRect((float) (i5 - i), (float) i6, (float) (i5 + i), (float) (i6 + i2), this.paint);
        int i7 = rect.right;
        int i8 = rect.top;
        Canvas canvas3 = canvas;
        canvas3.drawRect((float) (i7 - i2), (float) (i8 - i), (float) (i7 + i), (float) (i8 + i), this.paint);
        int i9 = rect.right;
        int i10 = rect.top;
        canvas3.drawRect((float) (i9 - i), (float) i10, (float) (i9 + i), (float) (i10 + i2), this.paint);
        int i11 = rect.left;
        int i12 = rect.bottom;
        Canvas canvas4 = canvas;
        canvas4.drawRect((float) (i11 - i), (float) (i12 - i2), (float) (i11 + i), (float) i12, this.paint);
        int i13 = rect.left;
        int i14 = rect.bottom;
        Canvas canvas5 = canvas;
        canvas5.drawRect((float) (i13 - i), (float) (i14 - i), (float) (i13 + i2), (float) (i14 + i), this.paint);
        int i15 = rect.right;
        int i16 = rect.bottom;
        Canvas canvas6 = canvas;
        canvas6.drawRect((float) (i15 - i2), (float) (i16 - i), (float) (i15 + i), (float) (i16 + i), this.paint);
        int i17 = rect.right;
        int i18 = rect.bottom;
        canvas6.drawRect((float) (i17 - i), (float) (i18 - i2), (float) (i17 + i), (float) (i18 + i), this.paint);
    }

    private void drawLaserLine(Canvas canvas, Rect rect) {
        if (this.laserBitmap == null) {
            this.laserBitmap = new ShapeDrawable(new OvalShape());
            int i = DetectorViewConfig.laserColor;
            this.laserBitmap.getPaint().setShader(new RadialGradient((float) (rect.width() / 2), (float) (DetectorViewConfig.LASER_WIDTH / 2), 240.0f, i, i & 16777215, Shader.TileMode.CLAMP));
        }
        this.paint.setAntiAlias(true);
        ShapeDrawable shapeDrawable = this.laserBitmap;
        int i2 = rect.left;
        shapeDrawable.setBounds(i2, this.laserY, rect.width() + i2, this.laserY + DetectorViewConfig.LASER_WIDTH);
        this.laserBitmap.draw(canvas);
        this.paint.setShader((Shader) null);
    }

    private void drawNonDetectorArea(Canvas canvas, Rect rect, Rect rect2) {
        this.paint.setColor(DetectorViewConfig.maskColor);
        Canvas canvas2 = canvas;
        canvas2.drawRect(0.0f, 0.0f, (float) rect2.right, (float) rect.top, this.paint);
        canvas2.drawRect(0.0f, (float) rect.top, (float) rect.left, (float) rect.bottom, this.paint);
        canvas2.drawRect((float) rect.right, (float) rect.top, (float) rect2.right, (float) rect.bottom, this.paint);
        canvas.drawRect(0.0f, (float) rect.bottom, (float) rect2.right, (float) rect2.bottom, this.paint);
    }

    private void drawResultPoint(Canvas canvas, Rect rect) {
        Collection<ResultPoint> collection = this.possibleResultPoints;
        Collection<ResultPoint> collection2 = this.lastPossibleResultPoints;
        if (collection.isEmpty()) {
            this.lastPossibleResultPoints = null;
        } else {
            this.possibleResultPoints = new HashSet(5);
            this.lastPossibleResultPoints = collection;
            this.paint.setAlpha(255);
            this.paint.setColor(DetectorViewConfig.resultPointColor);
            for (ResultPoint next : collection) {
                canvas.drawCircle(((float) rect.left) + next.getX(), ((float) rect.top) + next.getY(), 6.0f, this.paint);
            }
        }
        if (collection2 != null) {
            this.paint.setAlpha(127);
            this.paint.setColor(DetectorViewConfig.resultPointColor);
            for (ResultPoint next2 : collection2) {
                canvas.drawCircle(((float) rect.left) + next2.getX(), ((float) rect.top) + next2.getY(), 3.0f, this.paint);
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateScreen() {
        Rect detectorRect = DetectorViewConfig.getInstance().getDetectorRect();
        int i = this.laserY;
        if (i > detectorRect.bottom) {
            this.laserY = detectorRect.top;
        } else {
            this.laserY = i + 1;
        }
        postInvalidate();
    }

    public void addPossibleResultPoint(ResultPoint resultPoint) {
        this.possibleResultPoints.add(resultPoint);
    }

    public void drawViewfinder() {
        invalidate();
    }

    public void onDraw(Canvas canvas) {
        Rect detectorRect = DetectorViewConfig.getInstance().getDetectorRect();
        Rect rect = DetectorViewConfig.getInstance().gatherRect;
        if (detectorRect != null) {
            drawNonDetectorArea(canvas, detectorRect, rect);
            drawDetectorCorner(canvas, detectorRect);
            if (this.running) {
                drawLaserLine(canvas, detectorRect);
                return;
            }
            this.lastRect = detectorRect;
            if (detectorRect != null) {
                drawLaserLine(canvas, detectorRect);
            }
        }
    }

    public void startUpdateScreenTimer() {
        if (!this.running) {
            stopUpdateScreenTimer();
            this.laserY = DetectorViewConfig.getInstance().getDetectorRect().top;
            Timer timer = new Timer();
            this.mUpdateProgressBar = timer;
            timer.schedule(new TimerTask() {
                public void run() {
                    ViewfinderView.this.updateScreen();
                }
            }, 0, 10);
            this.running = true;
        }
    }

    public void stopUpdateScreenTimer() {
        if (this.running) {
            Timer timer = this.mUpdateProgressBar;
            if (timer != null) {
                timer.cancel();
                this.mUpdateProgressBar = null;
            }
            this.running = false;
            updateScreen();
        }
    }
}
