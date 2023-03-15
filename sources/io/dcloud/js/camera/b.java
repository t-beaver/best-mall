package io.dcloud.js.camera;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import io.dcloud.base.R;
import java.io.IOException;

class b {
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0024 A[SYNTHETIC, Splitter:B:17:0x0024] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x002c A[Catch:{ Exception -> 0x0028 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0038 A[SYNTHETIC, Splitter:B:28:0x0038] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0040 A[Catch:{ Exception -> 0x003c }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String a(android.graphics.Bitmap r4, java.lang.String r5) {
        /*
            r0 = 0
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x001d, all -> 0x001b }
            r1.<init>(r5)     // Catch:{ Exception -> 0x001d, all -> 0x001b }
            android.graphics.Bitmap$CompressFormat r2 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ Exception -> 0x0019 }
            r3 = 100
            r4.compress(r2, r3, r1)     // Catch:{ Exception -> 0x0019 }
            r1.close()     // Catch:{ Exception -> 0x0014 }
            r4.recycle()     // Catch:{ Exception -> 0x0014 }
            goto L_0x0018
        L_0x0014:
            r4 = move-exception
            r4.printStackTrace()
        L_0x0018:
            return r5
        L_0x0019:
            r5 = move-exception
            goto L_0x001f
        L_0x001b:
            r5 = move-exception
            goto L_0x0036
        L_0x001d:
            r5 = move-exception
            r1 = r0
        L_0x001f:
            r5.printStackTrace()     // Catch:{ all -> 0x0034 }
            if (r1 == 0) goto L_0x002a
            r1.close()     // Catch:{ Exception -> 0x0028 }
            goto L_0x002a
        L_0x0028:
            r4 = move-exception
            goto L_0x0030
        L_0x002a:
            if (r4 == 0) goto L_0x0033
            r4.recycle()     // Catch:{ Exception -> 0x0028 }
            goto L_0x0033
        L_0x0030:
            r4.printStackTrace()
        L_0x0033:
            return r0
        L_0x0034:
            r5 = move-exception
            r0 = r1
        L_0x0036:
            if (r0 == 0) goto L_0x003e
            r0.close()     // Catch:{ Exception -> 0x003c }
            goto L_0x003e
        L_0x003c:
            r4 = move-exception
            goto L_0x0044
        L_0x003e:
            if (r4 == 0) goto L_0x0047
            r4.recycle()     // Catch:{ Exception -> 0x003c }
            goto L_0x0047
        L_0x0044:
            r4.printStackTrace()
        L_0x0047:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.js.camera.b.a(android.graphics.Bitmap, java.lang.String):java.lang.String");
    }

    public static Bitmap b(String str) {
        return BitmapFactory.decodeFile(str, new BitmapFactory.Options());
    }

    public static int c(String str) {
        try {
            int attributeInt = new ExifInterface(str).getAttributeInt("Orientation", 1);
            if (attributeInt == 3) {
                return 180;
            }
            if (attributeInt != 6) {
                return attributeInt != 8 ? 0 : 270;
            }
            return 90;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String a(String str) {
        int c = c(str);
        if (c == 0) {
            return str;
        }
        Bitmap b = b(str);
        if (b == null) {
            return null;
        }
        return a(a(c, b), str);
    }

    public static Bitmap a(int i, Bitmap bitmap) {
        Bitmap bitmap2;
        Matrix matrix = new Matrix();
        matrix.postRotate((float) i);
        try {
            bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError unused) {
            bitmap2 = null;
        }
        if (bitmap2 == null) {
            bitmap2 = bitmap;
        }
        if (bitmap != bitmap2) {
            bitmap.recycle();
        }
        return bitmap2;
    }

    public static Dialog a(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog_transparent);
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.dcloud_dialog_loading, (ViewGroup) null);
        viewGroup.findViewById(R.id.loading_background).setBackgroundColor(0);
        AlertDialog create = builder.create();
        create.setCanceledOnTouchOutside(false);
        create.setView(viewGroup, 0, 0, 0, 0);
        return create;
    }
}
