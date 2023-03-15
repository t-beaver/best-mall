package com.dcloud.zxing2.oned.rss.expanded;

import com.dcloud.zxing2.oned.rss.DataCharacter;
import com.dcloud.zxing2.oned.rss.FinderPattern;

final class ExpandedPair {
    private final FinderPattern finderPattern;
    private final DataCharacter leftChar;
    private final boolean mayBeLast;
    private final DataCharacter rightChar;

    ExpandedPair(DataCharacter dataCharacter, DataCharacter dataCharacter2, FinderPattern finderPattern2, boolean z) {
        this.leftChar = dataCharacter;
        this.rightChar = dataCharacter2;
        this.finderPattern = finderPattern2;
        this.mayBeLast = z;
    }

    private static boolean equalsOrNull(Object obj, Object obj2) {
        if (obj == null) {
            return obj2 == null;
        }
        return obj.equals(obj2);
    }

    private static int hashNotNull(Object obj) {
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ExpandedPair)) {
            return false;
        }
        ExpandedPair expandedPair = (ExpandedPair) obj;
        if (!equalsOrNull(this.leftChar, expandedPair.leftChar) || !equalsOrNull(this.rightChar, expandedPair.rightChar) || !equalsOrNull(this.finderPattern, expandedPair.finderPattern)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public FinderPattern getFinderPattern() {
        return this.finderPattern;
    }

    /* access modifiers changed from: package-private */
    public DataCharacter getLeftChar() {
        return this.leftChar;
    }

    /* access modifiers changed from: package-private */
    public DataCharacter getRightChar() {
        return this.rightChar;
    }

    public int hashCode() {
        return (hashNotNull(this.leftChar) ^ hashNotNull(this.rightChar)) ^ hashNotNull(this.finderPattern);
    }

    /* access modifiers changed from: package-private */
    public boolean mayBeLast() {
        return this.mayBeLast;
    }

    public boolean mustBeLast() {
        return this.rightChar == null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        sb.append(this.leftChar);
        sb.append(" , ");
        sb.append(this.rightChar);
        sb.append(" : ");
        FinderPattern finderPattern2 = this.finderPattern;
        sb.append(finderPattern2 == null ? "null" : Integer.valueOf(finderPattern2.getValue()));
        sb.append(" ]");
        return sb.toString();
    }
}
