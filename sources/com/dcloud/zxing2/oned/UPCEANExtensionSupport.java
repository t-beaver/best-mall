package com.dcloud.zxing2.oned;

import com.dcloud.zxing2.NotFoundException;
import com.dcloud.zxing2.ReaderException;
import com.dcloud.zxing2.Result;
import com.dcloud.zxing2.common.BitArray;

final class UPCEANExtensionSupport {
    private static final int[] EXTENSION_START_PATTERN = {1, 1, 2};
    private final UPCEANExtension5Support fiveSupport = new UPCEANExtension5Support();
    private final UPCEANExtension2Support twoSupport = new UPCEANExtension2Support();

    UPCEANExtensionSupport() {
    }

    /* access modifiers changed from: package-private */
    public Result decodeRow(int i, BitArray bitArray, int i2) throws NotFoundException {
        int[] findGuardPattern = UPCEANReader.findGuardPattern(bitArray, i2, false, EXTENSION_START_PATTERN);
        try {
            return this.fiveSupport.decodeRow(i, bitArray, findGuardPattern);
        } catch (ReaderException unused) {
            return this.twoSupport.decodeRow(i, bitArray, findGuardPattern);
        }
    }
}
