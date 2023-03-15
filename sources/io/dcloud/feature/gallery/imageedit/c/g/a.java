package io.dcloud.feature.gallery.imageedit.c.g;

import android.graphics.RectF;

public interface a {
    public static final float[] a = {0.0f, 1.0f, 0.33f, 0.66f};
    public static final float[] b = {0.0f, 3.0f, -3.0f};
    public static final float[] c = {0.0f, 48.0f, -48.0f};
    public static final byte[] d = {8, 8, 9, 8, 6, 8, 4, 8, 4, 8, 4, 1, 4, 10, 4, 8, 4, 4, 6, 4, 9, 4, 8, 4, 8, 4, 8, 6, 8, 9, 8, 8};

    /* renamed from: io.dcloud.feature.gallery.imageedit.c.g.a$a  reason: collision with other inner class name */
    public enum C0040a {
        LEFT(1),
        RIGHT(2),
        TOP(4),
        BOTTOM(8),
        LEFT_TOP(5),
        RIGHT_TOP(6),
        LEFT_BOTTOM(9),
        RIGHT_BOTTOM(10);
        
        static final int[] i = null;
        int k;

        static {
            i = new int[]{1, -1};
        }

        private C0040a(int i2) {
            this.k = i2;
        }

        public void a(RectF rectF, RectF rectF2, float f, float f2, float[] fArr, boolean z) {
            float f3;
            float f4;
            float f5;
            int i2;
            RectF rectF3 = rectF2;
            float[] a = a(rectF, 0.0f, 0.0f);
            float[] a2 = a(rectF3, 150.72f, 150.72f);
            float[] a3 = a(rectF3, 0.0f, 0.0f);
            int i3 = this.k;
            if (z) {
                a2 = a(rectF3, 150.72f, (150.72f / fArr[0]) * fArr[1]);
                int[] iArr = {(int) (Math.abs(f) / f), (int) (Math.abs(f2) / f2)};
                int i4 = this.k;
                if (i4 == 4) {
                    rectF3.top += f2;
                    f4 = ((float) iArr[1]) * Math.abs(rectF2.width() - ((rectF2.height() / fArr[1]) * fArr[0]));
                    f3 = f2;
                    i3 = 5;
                } else if (i4 == 8) {
                    rectF3.bottom += f2;
                    f4 = ((float) iArr[1]) * Math.abs(rectF2.width() - ((rectF2.height() / fArr[1]) * fArr[0]));
                    f3 = f2;
                    i3 = 10;
                } else {
                    if (i4 == 1) {
                        i3 = 5;
                    }
                    if (i4 == 2) {
                        i3 = 10;
                    }
                    if ((i3 & 2) != 0) {
                        rectF3.right += f;
                    } else if ((i3 & 1) != 0) {
                        rectF3.left += f;
                    }
                    f3 = rectF2.height() - ((rectF2.width() / fArr[0]) * fArr[1]);
                    if (i3 == 5 || i3 == 10) {
                        f5 = Math.abs(f3);
                        i2 = iArr[0];
                    } else {
                        if (i3 == 6 || i3 == 9) {
                            f5 = Math.abs(f3);
                            i2 = -iArr[0];
                        }
                        f4 = f;
                    }
                    f3 = ((float) i2) * f5;
                    f4 = f;
                }
            } else {
                f4 = f;
                f3 = f2;
            }
            float[] fArr2 = {f4, 0.0f, f3};
            int i5 = 0;
            for (int i6 = 4; i5 < i6; i6 = 4) {
                if (((1 << i5) & i3) != 0) {
                    int[] iArr2 = i;
                    int i7 = i5 & 1;
                    float f6 = (float) iArr2[i7];
                    a3[i5] = f6 * a(f6 * (a3[i5] + fArr2[i5 & 2]), f6 * a[i5], a2[iArr2[i7] + i5] * f6);
                }
                i5++;
            }
            rectF3.set(a3[0], a3[2], a3[1], a3[3]);
        }

        public static float a(float f, float f2, float f3) {
            return Math.min(Math.max(f, f2), f3);
        }

        public static float[] a(RectF rectF, float f, float f2) {
            return new float[]{rectF.left + f, rectF.right - f, rectF.top + f2, rectF.bottom - f2};
        }

        public static boolean a(RectF rectF, float f, float f2, float f3) {
            return rectF.left + f < f2 && rectF.right - f > f2 && rectF.top + f < f3 && rectF.bottom - f > f3;
        }

        public static C0040a a(int i2) {
            for (C0040a aVar : values()) {
                if (aVar.k == i2) {
                    return aVar;
                }
            }
            return null;
        }
    }
}
