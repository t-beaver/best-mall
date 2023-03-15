package io.dcloud.common.adapter.util;

import io.dcloud.common.DHInterface.IApp;

public class ByteUtil {
    public static float bytesToFloat(byte[] bArr) {
        return Float.parseFloat(new String(bArr));
    }

    public static long bytesToLong(byte[] bArr) {
        return Long.parseLong(new String(bArr));
    }

    public static byte[] floatToBytes(float f) {
        return String.valueOf(f).getBytes();
    }

    public static byte[] longToBytes(long j) {
        return String.valueOf(j).getBytes();
    }

    public static byte[] toBytes(int i) {
        return new byte[]{(byte) ((i >> 24) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255)};
    }

    public static int toInt(byte[] bArr) {
        return ((bArr[0] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) << 24) | (bArr[3] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) | ((bArr[2] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) << 8) | ((bArr[1] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) << 16);
    }
}
