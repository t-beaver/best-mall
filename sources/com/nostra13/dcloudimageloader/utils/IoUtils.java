package com.nostra13.dcloudimageloader.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class IoUtils {
    private static final int BUFFER_SIZE = 32768;

    private IoUtils() {
    }

    public static void closeSilently(Closeable closeable) {
        try {
            closeable.close();
        } catch (Exception unused) {
        }
    }

    public static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[1195];
        while (true) {
            int read = inputStream.read(bArr, 0, 1195);
            if (read != -1) {
                outputStream.write(bArr, 0, read);
            } else {
                return;
            }
        }
    }
}
