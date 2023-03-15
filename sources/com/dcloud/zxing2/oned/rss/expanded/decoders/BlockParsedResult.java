package com.dcloud.zxing2.oned.rss.expanded.decoders;

final class BlockParsedResult {
    private final DecodedInformation decodedInformation;
    private final boolean finished;

    BlockParsedResult(boolean z) {
        this((DecodedInformation) null, z);
    }

    /* access modifiers changed from: package-private */
    public DecodedInformation getDecodedInformation() {
        return this.decodedInformation;
    }

    /* access modifiers changed from: package-private */
    public boolean isFinished() {
        return this.finished;
    }

    BlockParsedResult(DecodedInformation decodedInformation2, boolean z) {
        this.finished = z;
        this.decodedInformation = decodedInformation2;
    }
}
