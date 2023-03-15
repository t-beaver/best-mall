package org.mozilla.universalchardet.prober.contextanalysis;

import io.dcloud.common.DHInterface.IApp;
import org.mozilla.universalchardet.prober.contextanalysis.JapaneseContextAnalysis;

public class SJISContextAnalysis extends JapaneseContextAnalysis {
    public static final int HIGHBYTE_BEGIN_1 = 129;
    public static final int HIGHBYTE_BEGIN_2 = 224;
    public static final int HIGHBYTE_END_1 = 159;
    public static final int HIGHBYTE_END_2 = 239;
    public static final int HIRAGANA_HIGHBYTE = 130;
    public static final int HIRAGANA_LOWBYTE_BEGIN = 159;
    public static final int HIRAGANA_LOWBYTE_END = 241;

    /* access modifiers changed from: protected */
    public void getOrder(JapaneseContextAnalysis.Order order, byte[] bArr, int i) {
        byte b;
        order.order = -1;
        order.charLength = 1;
        byte b2 = bArr[i] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE;
        if ((b2 >= 129 && b2 <= 159) || (b2 >= 224 && b2 <= 239)) {
            order.charLength = 2;
        }
        if (b2 == 130 && (b = bArr[i + 1] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) >= 159 && b <= 241) {
            order.order = b - 159;
        }
    }

    /* access modifiers changed from: protected */
    public int getOrder(byte[] bArr, int i) {
        byte b;
        if ((bArr[i] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) != 130 || (b = bArr[i + 1] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) < 159 || b > 241) {
            return -1;
        }
        return b - 159;
    }
}
