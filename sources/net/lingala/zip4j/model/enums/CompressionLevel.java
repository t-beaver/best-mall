package net.lingala.zip4j.model.enums;

public enum CompressionLevel {
    FASTEST(1),
    FASTER(2),
    FAST(3),
    MEDIUM_FAST(4),
    NORMAL(5),
    HIGHER(6),
    MAXIMUM(7),
    PRE_ULTRA(8),
    ULTRA(9);
    
    private int level;

    private CompressionLevel(int i) {
        this.level = i;
    }

    public int getLevel() {
        return this.level;
    }
}
