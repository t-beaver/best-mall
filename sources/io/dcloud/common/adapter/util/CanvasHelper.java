package io.dcloud.common.adapter.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.facebook.common.statfs.StatFsHelper;
import io.dcloud.common.util.IOUtil;
import java.io.InputStream;

public class CanvasHelper {
    public static final int BASELINE = 0;
    public static final int BOTTOM = 80;
    private static final int DEVIANT = 5;
    public static final int HCENTER = 1;
    public static final int LEFT = 3;
    public static final int RIGHT = 5;
    public static final int TOP = 48;
    public static final int VCENTER = 16;
    private static BitmapDrawable sDrawable;

    public static void clearData() {
        BitmapDrawable bitmapDrawable = sDrawable;
        if (bitmapDrawable != null) {
            bitmapDrawable.getBitmap().recycle();
            sDrawable = null;
        }
    }

    public static int dip2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    private static void drawClipBitmap(Canvas canvas, Bitmap bitmap, Paint paint, int i, int i2, int i3, int i4, int i5, int i6) {
        canvas.save();
        canvas.clipRect(i, i2, i3, i4);
        canvas.drawBitmap(bitmap, (float) i5, (float) i6, paint);
        canvas.restore();
    }

    public static void drawNinePatchs(Canvas canvas, Bitmap bitmap, int[] iArr, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        Paint paint = new Paint();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int i16 = 0;
        int i17 = 0;
        int i18 = 0;
        int i19 = 0;
        int i20 = 0;
        int i21 = 0;
        int i22 = 1;
        while (i22 <= 9) {
            if (i22 == 1) {
                int i23 = iArr[0];
                int i24 = iArr[1];
                i7 = i;
                i6 = i7;
                i12 = i2;
                i5 = i12;
                i14 = i23;
                i13 = i14;
                i11 = i24;
                i10 = i11;
                i9 = i + i23;
                i8 = i2 + i24;
            } else if (i22 == 2) {
                int i25 = (width - iArr[0]) - iArr[2];
                int i26 = iArr[1];
                int i27 = i + iArr[0];
                i7 = i;
                i12 = i2;
                i5 = i12;
                i13 = i25;
                i11 = i26;
                i10 = i11;
                i9 = i27 + i25;
                i8 = i2 + i26;
                i6 = i27;
                i14 = (i3 - iArr[0]) - iArr[3];
            } else if (i22 == 3) {
                int i28 = iArr[2];
                int i29 = iArr[1];
                int i30 = i + i3;
                i12 = i2;
                i5 = i12;
                i14 = i28;
                i13 = i14;
                i11 = i29;
                i10 = i11;
                i9 = i30;
                i8 = i2 + i29;
                i6 = i30 - i28;
                i7 = i30 - width;
            } else if (i22 == 4) {
                int i31 = iArr[0];
                int i32 = (height - iArr[1]) - iArr[3];
                int i33 = i2 + iArr[1];
                i7 = i;
                i6 = i7;
                i5 = i2;
                i14 = i31;
                i13 = i14;
                i10 = i32;
                i12 = i33;
                i9 = i + i31;
                i8 = i33 + i32;
                i11 = (i4 - iArr[1]) - iArr[3];
            } else {
                if (i22 == 5) {
                    int i34 = (width - iArr[0]) - iArr[2];
                    int i35 = (height - iArr[1]) - iArr[3];
                    int i36 = i + iArr[0];
                    int i37 = i2 + iArr[1];
                    int i38 = (i3 - iArr[0]) - iArr[2];
                    i15 = (i4 - iArr[1]) - iArr[3];
                    i7 = i;
                    i5 = i2;
                    i13 = i34;
                    i10 = i35;
                    i12 = i37;
                    i9 = i36 + i34;
                    i6 = i36;
                    i8 = i37 + i35;
                    i14 = i38;
                } else if (i22 == 6) {
                    int i39 = iArr[2];
                    int i40 = (height - iArr[1]) - iArr[3];
                    int i41 = i + i3;
                    int i42 = i41 - i39;
                    int i43 = i2 + iArr[1];
                    i15 = (i4 - iArr[1]) - iArr[3];
                    i5 = i2;
                    i14 = i39;
                    i13 = i14;
                    i10 = i40;
                    i9 = i41;
                    i12 = i43;
                    i6 = i42;
                    i8 = i43 + i40;
                    i7 = i42 - (width - i39);
                } else if (i22 == 7) {
                    int i44 = iArr[0];
                    int i45 = iArr[3];
                    int i46 = (i2 + i4) - i45;
                    i7 = i;
                    i6 = i7;
                    i14 = i44;
                    i13 = i14;
                    i11 = i45;
                    i10 = i11;
                    i12 = i46;
                    i9 = i + i44;
                    i8 = i46 + i45;
                    i5 = (i46 - height) + i45;
                } else if (i22 == 8) {
                    int i47 = (width - iArr[0]) - iArr[2];
                    int i48 = iArr[3];
                    int i49 = i + iArr[0];
                    int i50 = (i2 + i4) - i48;
                    i7 = i;
                    i13 = i47;
                    i10 = i48;
                    i12 = i50;
                    i9 = i49 + i47;
                    i6 = i49;
                    i8 = i50 + i48;
                    i5 = (i50 - height) + i48;
                    i14 = (i3 - iArr[0]) - iArr[2];
                    i11 = iArr[3];
                } else if (i22 == 9) {
                    int i51 = iArr[2];
                    int i52 = iArr[3];
                    int i53 = i + i3;
                    int i54 = (i2 + i4) - i52;
                    i14 = i51;
                    i13 = i14;
                    i11 = i52;
                    i10 = i11;
                    i9 = i53;
                    i12 = i54;
                    i6 = i53 - i51;
                    i8 = i54 + i52;
                    i7 = i53 - width;
                    i5 = (i54 - height) + i52;
                } else {
                    i12 = i16;
                    i9 = i17;
                    i8 = i18;
                    i7 = i19;
                    i6 = i20;
                    i5 = i21;
                    i14 = 0;
                    i13 = 0;
                    i11 = 0;
                    i10 = 0;
                }
                i11 = i15;
            }
            int i55 = (i11 / i10) + (i11 % i10 > 0 ? 1 : 0);
            int i56 = (i14 / i13) + (i14 % i13 > 0 ? 1 : 0);
            int i57 = 0;
            while (i57 < i55) {
                int i58 = 0;
                while (i58 < i56) {
                    int i59 = i58 * i13;
                    int i60 = i9 + i59;
                    int i61 = i14 + i6;
                    int i62 = i60 > i61 ? i61 : i60;
                    int i63 = i57 * i10;
                    int i64 = i8 + i63;
                    int i65 = i11 + i12;
                    int i66 = i58;
                    int i67 = i62;
                    drawClipBitmap(canvas, bitmap, paint, i6 + i59, i12 + i63, i67, i64 > i65 ? i65 : i64, i7 + i59, i5 + i63);
                    i58 = i66 + 1;
                    i57 = i57;
                    i56 = i56;
                    i55 = i55;
                }
                int i68 = i56;
                int i69 = i55;
                i57++;
            }
            i22++;
            i16 = i12;
            i17 = i9;
            i18 = i8;
            i19 = i7;
            i20 = i6;
            i21 = i5;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0055  */
    /* JADX WARNING: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void drawString(android.graphics.Canvas r4, java.lang.String r5, int r6, int r7, int r8, android.graphics.Paint r9) {
        /*
            if (r4 != 0) goto L_0x0003
            return
        L_0x0003:
            if (r9 != 0) goto L_0x0006
            return
        L_0x0006:
            float r0 = r9.getTextSize()
            int r0 = (int) r0
            r1 = r8 & 3
            r2 = 3
            r3 = 5
            if (r1 != r2) goto L_0x0017
            android.graphics.Paint$Align r1 = android.graphics.Paint.Align.LEFT
            r9.setTextAlign(r1)
            goto L_0x002c
        L_0x0017:
            r1 = r8 & 5
            if (r1 != r3) goto L_0x0021
            android.graphics.Paint$Align r1 = android.graphics.Paint.Align.RIGHT
            r9.setTextAlign(r1)
            goto L_0x002c
        L_0x0021:
            r1 = r8 & 1
            r2 = 1
            if (r1 != r2) goto L_0x0027
            goto L_0x002c
        L_0x0027:
            android.graphics.Paint$Align r1 = android.graphics.Paint.Align.LEFT
            r9.setTextAlign(r1)
        L_0x002c:
            r1 = r8 & 48
            r2 = 48
            if (r1 != r2) goto L_0x0036
            int r7 = r7 + r0
            int r0 = r0 / r3
        L_0x0034:
            int r7 = r7 - r0
            goto L_0x0053
        L_0x0036:
            r1 = r8 & 80
            r2 = 80
            if (r1 != r2) goto L_0x0048
            android.graphics.Paint$FontMetrics r8 = r9.getFontMetrics()
            float r8 = r8.descent
            r0 = 1073741824(0x40000000, float:2.0)
            float r8 = r8 / r0
            int r8 = (int) r8
            int r7 = r7 - r8
            goto L_0x0053
        L_0x0048:
            r1 = 16
            r8 = r8 & r1
            int r7 = r7 + r0
            if (r8 != r1) goto L_0x0051
            int r7 = r7 >> 0
            goto L_0x0053
        L_0x0051:
            int r0 = r0 / r3
            goto L_0x0034
        L_0x0053:
            if (r5 == 0) goto L_0x005a
            float r6 = (float) r6
            float r7 = (float) r7
            r4.drawText(r5, r6, r7, r9)
        L_0x005a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.util.CanvasHelper.drawString(android.graphics.Canvas, java.lang.String, int, int, int, android.graphics.Paint):void");
    }

    public static Bitmap getBitmap(String str) {
        Bitmap bitmap = null;
        try {
            InputStream inputStream = PlatformUtil.getInputStream(str);
            bitmap = BitmapFactory.decodeStream(inputStream);
            IOUtil.close(inputStream);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    public static Drawable getDrawable() {
        if (sDrawable == null) {
            Bitmap createBitmap = Bitmap.createBitmap(StatFsHelper.DEFAULT_DISK_YELLOW_LEVEL_IN_MB, StatFsHelper.DEFAULT_DISK_YELLOW_LEVEL_IN_MB, Bitmap.Config.RGB_565);
            new Canvas(createBitmap).drawColor(0);
            sDrawable = new BitmapDrawable(createBitmap);
        }
        return sDrawable;
    }

    public static int getFontHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return ((int) Math.ceil((double) (fontMetrics.descent - fontMetrics.top))) + 2;
    }

    public static float getViablePx(int i) {
        return ((float) i) * DeviceInfo.sDensity;
    }

    public static int px2dip(Context context, float f) {
        return (int) ((f / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static Drawable getDrawable(Context context, String str) {
        Bitmap bitmap = getBitmap(str);
        if (bitmap == null) {
            return null;
        }
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public static Drawable getDrawable(String str) {
        Bitmap bitmap = getBitmap(str);
        if (bitmap == null) {
            return null;
        }
        return new BitmapDrawable(bitmap);
    }
}
