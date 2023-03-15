package net.lingala.zip4j.model;

import java.nio.charset.Charset;

public class Zip4jConfig {
    private final int bufferSize;
    private final Charset charset;

    public Zip4jConfig(Charset charset2, int i) {
        this.charset = charset2;
        this.bufferSize = i;
    }

    public int getBufferSize() {
        return this.bufferSize;
    }

    public Charset getCharset() {
        return this.charset;
    }
}
