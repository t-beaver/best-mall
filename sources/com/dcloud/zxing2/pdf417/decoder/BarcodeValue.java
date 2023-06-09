package com.dcloud.zxing2.pdf417.decoder;

import com.dcloud.zxing2.pdf417.PDF417Common;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

final class BarcodeValue {
    private final Map<Integer, Integer> values = new HashMap();

    BarcodeValue() {
    }

    public Integer getConfidence(int i) {
        return this.values.get(Integer.valueOf(i));
    }

    /* access modifiers changed from: package-private */
    public int[] getValue() {
        ArrayList arrayList = new ArrayList();
        int i = -1;
        for (Map.Entry next : this.values.entrySet()) {
            if (((Integer) next.getValue()).intValue() > i) {
                i = ((Integer) next.getValue()).intValue();
                arrayList.clear();
                arrayList.add((Integer) next.getKey());
            } else if (((Integer) next.getValue()).intValue() == i) {
                arrayList.add((Integer) next.getKey());
            }
        }
        return PDF417Common.toIntArray(arrayList);
    }

    /* access modifiers changed from: package-private */
    public void setValue(int i) {
        Integer num = this.values.get(Integer.valueOf(i));
        if (num == null) {
            num = 0;
        }
        this.values.put(Integer.valueOf(i), Integer.valueOf(num.intValue() + 1));
    }
}
