package io.dcloud.feature.uniapp.ui.shadow;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

public class UniInsetBoxShadowDrawable extends Drawable {
    private static final int BOTTOM_TO_TOP = 3;
    private static final int LEFT_TO_RIGHT = 0;
    private static final int RIGHT_TO_LEFT = 2;
    private static final int TOP_TO_BOTTOM = 1;
    private float blurRadius;
    private float height;
    private Paint paint;
    private Path[] paths = new Path[4];
    private float[] radii;
    private Shader[] shades = new Shader[4];
    private int shadowColor;
    private float shadowXSize;
    private float shadowYSize;
    private float width;

    public int getOpacity() {
        return -1;
    }

    public void setAlpha(int i) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public UniInsetBoxShadowDrawable(int i, int i2, float f, float f2, float f3, float f4, int i3, float[] fArr) {
        this.blurRadius = f3;
        this.shadowColor = i3;
        this.width = ((float) i) + (f * 2.0f);
        this.height = ((float) i2) + (2.0f * f2);
        this.shadowXSize = f + f4;
        this.shadowYSize = f2 + f4;
        this.radii = fArr;
        setBounds(0, 0, i, i2);
        prepare();
    }

    private void prepare() {
        PointF pointF = new PointF(0.0f, 0.0f);
        PointF pointF2 = new PointF(this.width, 0.0f);
        PointF pointF3 = new PointF(pointF2.x, this.height);
        PointF pointF4 = new PointF(pointF.x, pointF3.y);
        PointF pointF5 = new PointF(this.shadowXSize, this.shadowYSize);
        PointF pointF6 = new PointF(pointF2.x - this.shadowXSize, pointF5.y);
        PointF pointF7 = new PointF(pointF6.x, pointF3.y - this.shadowYSize);
        PointF pointF8 = new PointF(pointF5.x, pointF7.y);
        LinearGradient linearGradient = new LinearGradient(pointF5.x - this.blurRadius, pointF5.y, pointF5.x, pointF5.y, this.shadowColor, 0, Shader.TileMode.CLAMP);
        float f = pointF5.x;
        float f2 = pointF5.y - this.blurRadius;
        float f3 = pointF5.x;
        float f4 = pointF5.y;
        LinearGradient linearGradient2 = new LinearGradient(f, f2, f3, f4, this.shadowColor, 0, Shader.TileMode.CLAMP);
        float f5 = pointF7.x + this.blurRadius;
        float f6 = pointF7.y;
        float f7 = pointF7.x;
        float f8 = pointF7.y;
        LinearGradient linearGradient3 = new LinearGradient(f5, f6, f7, f8, this.shadowColor, 0, Shader.TileMode.CLAMP);
        float f9 = pointF7.x;
        float f10 = pointF7.y + this.blurRadius;
        float f11 = pointF7.x;
        float f12 = pointF7.y;
        PointF pointF9 = pointF7;
        LinearGradient linearGradient4 = new LinearGradient(f9, f10, f11, f12, this.shadowColor, 0, Shader.TileMode.CLAMP);
        Shader[] shaderArr = this.shades;
        shaderArr[0] = linearGradient;
        shaderArr[1] = linearGradient2;
        shaderArr[2] = linearGradient3;
        shaderArr[3] = linearGradient4;
        Path path = new Path();
        path.moveTo(pointF.x, pointF.y);
        path.lineTo(pointF5.x, pointF5.y);
        path.lineTo(pointF8.x, pointF8.y);
        path.lineTo(pointF4.x, pointF4.y);
        path.close();
        Path path2 = new Path();
        path2.moveTo(pointF.x, pointF.y);
        path2.lineTo(pointF2.x, pointF2.y);
        path2.lineTo(pointF6.x, pointF6.y);
        path2.lineTo(pointF5.x, pointF5.y);
        path2.close();
        Path path3 = new Path();
        path3.moveTo(pointF2.x, pointF2.y);
        path3.lineTo(pointF3.x, pointF3.y);
        PointF pointF10 = pointF9;
        path3.lineTo(pointF10.x, pointF10.y);
        path3.lineTo(pointF6.x, pointF6.y);
        path3.close();
        Path path4 = new Path();
        path4.moveTo(pointF4.x, pointF4.y);
        path4.lineTo(pointF3.x, pointF3.y);
        path4.lineTo(pointF10.x, pointF10.y);
        path4.lineTo(pointF8.x, pointF8.y);
        path4.close();
        Path[] pathArr = this.paths;
        pathArr[0] = path;
        pathArr[1] = path2;
        pathArr[2] = path3;
        pathArr[3] = path4;
        Paint paint2 = new Paint();
        this.paint = paint2;
        paint2.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(this.shadowColor);
    }

    public void draw(Canvas canvas) {
        Rect clipBounds = canvas.getClipBounds();
        Path path = new Path();
        path.addRoundRect(new RectF(clipBounds), this.radii, Path.Direction.CCW);
        canvas.clipPath(path);
        canvas.translate((float) clipBounds.left, (float) clipBounds.top);
        for (int i = 0; i < 4; i++) {
            Shader shader = this.shades[i];
            Path path2 = this.paths[i];
            this.paint.setShader(shader);
            canvas.drawPath(path2, this.paint);
        }
    }
}
