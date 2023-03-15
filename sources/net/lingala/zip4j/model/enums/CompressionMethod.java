package net.lingala.zip4j.model.enums;

import net.lingala.zip4j.exception.ZipException;

public enum CompressionMethod {
    STORE(0),
    DEFLATE(8),
    AES_INTERNAL_ONLY(99);
    
    private int code;

    private CompressionMethod(int i) {
        this.code = i;
    }

    public static CompressionMethod getCompressionMethodFromCode(int i) throws ZipException {
        for (CompressionMethod compressionMethod : values()) {
            if (compressionMethod.getCode() == i) {
                return compressionMethod;
            }
        }
        throw new ZipException("Unknown compression method", ZipException.Type.UNKNOWN_COMPRESSION_METHOD);
    }

    public int getCode() {
        return this.code;
    }
}
