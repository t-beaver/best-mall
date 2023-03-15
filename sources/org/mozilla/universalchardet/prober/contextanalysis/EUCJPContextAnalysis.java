package org.mozilla.universalchardet.prober.contextanalysis;

import io.dcloud.common.DHInterface.IApp;
import org.mozilla.universalchardet.prober.contextanalysis.JapaneseContextAnalysis;

public class EUCJPContextAnalysis extends JapaneseContextAnalysis {
    public static final int FIRSTPLANE_HIGHBYTE_BEGIN = 161;
    public static final int FIRSTPLANE_HIGHBYTE_END = 254;
    public static final int HIRAGANA_HIGHBYTE = 164;
    public static final int HIRAGANA_LOWBYTE_BEGIN = 161;
    public static final int HIRAGANA_LOWBYTE_END = 243;
    public static final int SINGLE_SHIFT_2 = 142;
    public static final int SINGLE_SHIFT_3 = 143;

    /* access modifiers changed from: protected */
    public void getOrder(JapaneseContextAnalysis.Order order, byte[] bArr, int i) {
        byte b;
        order.order = -1;
        order.charLength = 1;
        byte b2 = bArr[i] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE;
        if (b2 == 142 || (b2 >= 161 && b2 <= 254)) {
            order.charLength = 2;
        } else if (b2 == 143) {
            order.charLength = 3;
        }
        if (b2 == 164 && (b = bArr[i + 1] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) >= 161 && b <= 243) {
            order.order = b - 161;
        }
    }

    /* access modifiers changed from: protected */
    public int getOrder(byte[] bArr, int i) {
        byte b;
        if ((bArr[i] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) != 164 || (b = bArr[i + 1] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) < 161 || b > 243) {
            return -1;
        }
        return b - 161;
    }
}
