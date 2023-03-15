package com.dcloud.zxing2.oned.rss;

import com.taobao.weex.el.parse.Operators;

public class DataCharacter {
    private final int checksumPortion;
    private final int value;

    public DataCharacter(int i, int i2) {
        this.value = i;
        this.checksumPortion = i2;
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof DataCharacter)) {
            return false;
        }
        DataCharacter dataCharacter = (DataCharacter) obj;
        if (this.value == dataCharacter.value && this.checksumPortion == dataCharacter.checksumPortion) {
            return true;
        }
        return false;
    }

    public final int getChecksumPortion() {
        return this.checksumPortion;
    }

    public final int getValue() {
        return this.value;
    }

    public final int hashCode() {
        return this.value ^ this.checksumPortion;
    }

    public final String toString() {
        return this.value + Operators.BRACKET_START_STR + this.checksumPortion + Operators.BRACKET_END;
    }
}
