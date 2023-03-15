package io.dcloud.feature.gallery.imageedit.c.k;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import io.dcloud.common.util.ExifInterface;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class a {
    private static int a(int i) {
        if (i == 3) {
            return 180;
        }
        if (i != 6) {
            return i != 8 ? 0 : 270;
        }
        return 90;
    }

    public static Bitmap a(Bitmap bitmap, int i) {
        if (bitmap == null) {
            return null;
        }
        int a = a(i);
        if (a <= 0) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate((float) a);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap a(File file, BitmapFactory.Options options) {
        try {
            int parseInt = Integer.parseInt(new ExifInterface(file).getAttribute("Orientation"));
            byte[] a = a(file);
            if (parseInt > 1) {
                return a(BitmapFactory.decodeByteArray(a, 0, a.length, options), parseInt);
            }
            return BitmapFactory.decodeByteArray(a, 0, a.length, options);
        } catch (Throwable unused) {
            return null;
        }
    }

    public static Bitmap a(InputStream inputStream, BitmapFactory.Options options) {
        try {
            byte[] a = a(inputStream);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(a);
            ExifInterface exifInterface = new ExifInterface((InputStream) byteArrayInputStream);
            try {
                byteArrayInputStream.close();
            } catch (Exception unused) {
            }
            int parseInt = Integer.parseInt(exifInterface.getAttribute("Orientation"));
            if (parseInt > 1) {
                return a(BitmapFactory.decodeByteArray(a, 0, a.length), parseInt);
            }
            return BitmapFactory.decodeByteArray(a, 0, a.length, options);
        } catch (Throwable unused2) {
            return null;
        }
    }

    public static byte[] a(InputStream inputStream) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[10240];
            while (true) {
                int read = inputStream.read(bArr);
                if (read != -1) {
                    byteArrayOutputStream.write(bArr, 0, read);
                } else {
                    inputStream.close();
                    byteArrayOutputStream.close();
                    return byteArrayOutputStream.toByteArray();
                }
            }
        } catch (Exception unused) {
            return null;
        }
    }

    private static byte[] a(File file) throws IOException {
        if (file == null || !file.exists()) {
            return null;
        }
        try {
            return a((InputStream) new FileInputStream(file));
        } catch (Exception unused) {
            return null;
        }
    }
}
