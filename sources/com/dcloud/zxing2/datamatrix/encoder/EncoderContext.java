package com.dcloud.zxing2.datamatrix.encoder;

import com.dcloud.zxing2.Dimension;
import io.dcloud.common.DHInterface.IApp;
import java.nio.charset.Charset;
import net.lingala.zip4j.util.InternalZipConstants;

final class EncoderContext {
    private final StringBuilder codewords;
    private Dimension maxSize;
    private Dimension minSize;
    private final String msg;
    private int newEncoding;
    int pos;
    private SymbolShapeHint shape;
    private int skipAtEnd;
    private SymbolInfo symbolInfo;

    EncoderContext(String str) {
        byte[] bytes = str.getBytes(Charset.forName(InternalZipConstants.AES_HASH_CHARSET));
        StringBuilder sb = new StringBuilder(bytes.length);
        int length = bytes.length;
        int i = 0;
        while (i < length) {
            char c = (char) (bytes[i] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE);
            if (c != '?' || str.charAt(i) == '?') {
                sb.append(c);
                i++;
            } else {
                throw new IllegalArgumentException("Message contains characters outside ISO-8859-1 encoding.");
            }
        }
        this.msg = sb.toString();
        this.shape = SymbolShapeHint.FORCE_NONE;
        this.codewords = new StringBuilder(str.length());
        this.newEncoding = -1;
    }

    private int getTotalMessageCharCount() {
        return this.msg.length() - this.skipAtEnd;
    }

    public int getCodewordCount() {
        return this.codewords.length();
    }

    public StringBuilder getCodewords() {
        return this.codewords;
    }

    public char getCurrent() {
        return this.msg.charAt(this.pos);
    }

    public char getCurrentChar() {
        return this.msg.charAt(this.pos);
    }

    public String getMessage() {
        return this.msg;
    }

    public int getNewEncoding() {
        return this.newEncoding;
    }

    public int getRemainingCharacters() {
        return getTotalMessageCharCount() - this.pos;
    }

    public SymbolInfo getSymbolInfo() {
        return this.symbolInfo;
    }

    public boolean hasMoreCharacters() {
        return this.pos < getTotalMessageCharCount();
    }

    public void resetEncoderSignal() {
        this.newEncoding = -1;
    }

    public void resetSymbolInfo() {
        this.symbolInfo = null;
    }

    public void setSizeConstraints(Dimension dimension, Dimension dimension2) {
        this.minSize = dimension;
        this.maxSize = dimension2;
    }

    public void setSkipAtEnd(int i) {
        this.skipAtEnd = i;
    }

    public void setSymbolShape(SymbolShapeHint symbolShapeHint) {
        this.shape = symbolShapeHint;
    }

    public void signalEncoderChange(int i) {
        this.newEncoding = i;
    }

    public void updateSymbolInfo() {
        updateSymbolInfo(getCodewordCount());
    }

    public void writeCodeword(char c) {
        this.codewords.append(c);
    }

    public void writeCodewords(String str) {
        this.codewords.append(str);
    }

    public void updateSymbolInfo(int i) {
        SymbolInfo symbolInfo2 = this.symbolInfo;
        if (symbolInfo2 == null || i > symbolInfo2.getDataCapacity()) {
            this.symbolInfo = SymbolInfo.lookup(i, this.shape, this.minSize, this.maxSize, true);
        }
    }
}
