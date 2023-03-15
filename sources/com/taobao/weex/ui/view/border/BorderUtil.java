package com.taobao.weex.ui.view.border;

import android.util.SparseIntArray;
import com.taobao.weex.dom.CSSShorthand;

class BorderUtil {
    BorderUtil() {
    }

    static int fetchFromSparseArray(SparseIntArray sparseIntArray, int i, int i2) {
        return sparseIntArray == null ? i2 : sparseIntArray.get(i, sparseIntArray.get(CSSShorthand.EDGE.ALL.ordinal()));
    }

    static void updateSparseArray(SparseIntArray sparseIntArray, int i, int i2) {
        if (i == CSSShorthand.EDGE.ALL.ordinal()) {
            sparseIntArray.put(CSSShorthand.EDGE.ALL.ordinal(), i2);
            sparseIntArray.put(CSSShorthand.EDGE.TOP.ordinal(), i2);
            sparseIntArray.put(CSSShorthand.EDGE.LEFT.ordinal(), i2);
            sparseIntArray.put(CSSShorthand.EDGE.RIGHT.ordinal(), i2);
            sparseIntArray.put(CSSShorthand.EDGE.BOTTOM.ordinal(), i2);
            return;
        }
        sparseIntArray.put(i, i2);
    }
}
