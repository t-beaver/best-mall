package net.lingala.zip4j.crypto.engine;

import io.dcloud.common.DHInterface.IApp;
import net.lingala.zip4j.util.Zip4jUtil;

public class ZipCryptoEngine {
    private static final int[] CRC_TABLE = new int[256];
    private final int[] keys = new int[3];

    static {
        for (int i = 0; i < 256; i++) {
            int i2 = i;
            for (int i3 = 0; i3 < 8; i3++) {
                int i4 = i2 & 1;
                i2 >>>= 1;
                if (i4 == 1) {
                    i2 ^= -306674912;
                }
            }
            CRC_TABLE[i] = i2;
        }
    }

    private int crc32(int i, byte b) {
        return CRC_TABLE[(i ^ b) & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE] ^ (i >>> 8);
    }

    public byte decryptByte() {
        int i = this.keys[2] | 2;
        return (byte) ((i * (i ^ 1)) >>> 8);
    }

    public void initKeys(char[] cArr) {
        int[] iArr = this.keys;
        iArr[0] = 305419896;
        iArr[1] = 591751049;
        iArr[2] = 878082192;
        for (byte b : Zip4jUtil.convertCharArrayToByteArray(cArr)) {
            updateKeys((byte) (b & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE));
        }
    }

    public void updateKeys(byte b) {
        int[] iArr = this.keys;
        iArr[0] = crc32(iArr[0], b);
        int[] iArr2 = this.keys;
        iArr2[1] = iArr2[1] + (iArr2[0] & 255);
        iArr2[1] = (iArr2[1] * 134775813) + 1;
        iArr2[2] = crc32(iArr2[2], (byte) (iArr2[1] >> 24));
    }
}
