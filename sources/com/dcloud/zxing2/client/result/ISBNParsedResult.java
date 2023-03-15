package com.dcloud.zxing2.client.result;

public final class ISBNParsedResult extends ParsedResult {
    private final String isbn;

    ISBNParsedResult(String str) {
        super(ParsedResultType.ISBN);
        this.isbn = str;
    }

    public String getDisplayResult() {
        return this.isbn;
    }

    public String getISBN() {
        return this.isbn;
    }
}
