package com.dcloud.zxing2.client.result;

import com.dcloud.zxing2.Result;

public final class BookmarkDoCoMoResultParser extends AbstractDoCoMoResultParser {
    public URIParsedResult parse(Result result) {
        String text = result.getText();
        if (!text.startsWith("MEBKM:")) {
            return null;
        }
        String matchSingleDoCoMoPrefixedField = AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("TITLE:", text, true);
        String[] matchDoCoMoPrefixedField = AbstractDoCoMoResultParser.matchDoCoMoPrefixedField("URL:", text, true);
        if (matchDoCoMoPrefixedField == null) {
            return null;
        }
        String str = matchDoCoMoPrefixedField[0];
        if (URIResultParser.isBasicallyValidURI(str)) {
            return new URIParsedResult(str, matchSingleDoCoMoPrefixedField);
        }
        return null;
    }
}
