package com.dcloud.zxing2.oned;

public abstract class UPCEANWriter extends OneDimensionalCodeWriter {
    public int getDefaultMargin() {
        return UPCEANReader.START_END_PATTERN.length;
    }
}
