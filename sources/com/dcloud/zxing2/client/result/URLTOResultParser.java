package com.dcloud.zxing2.client.result;

import com.dcloud.zxing2.Result;

public final class URLTOResultParser extends ResultParser {
    public URIParsedResult parse(Result result) {
        int indexOf;
        String massagedText = ResultParser.getMassagedText(result);
        String str = null;
        if ((!massagedText.startsWith("urlto:") && !massagedText.startsWith("URLTO:")) || (indexOf = massagedText.indexOf(58, 6)) < 0) {
            return null;
        }
        if (indexOf > 6) {
            str = massagedText.substring(6, indexOf);
        }
        return new URIParsedResult(massagedText.substring(indexOf + 1), str);
    }
}
