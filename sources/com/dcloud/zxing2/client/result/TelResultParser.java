package com.dcloud.zxing2.client.result;

import com.dcloud.zxing2.Result;

public final class TelResultParser extends ResultParser {
    public TelParsedResult parse(Result result) {
        String str;
        String massagedText = ResultParser.getMassagedText(result);
        if (!massagedText.startsWith("tel:") && !massagedText.startsWith("TEL:")) {
            return null;
        }
        if (massagedText.startsWith("TEL:")) {
            str = "tel:" + massagedText.substring(4);
        } else {
            str = massagedText;
        }
        int indexOf = massagedText.indexOf(63, 4);
        return new TelParsedResult(indexOf < 0 ? massagedText.substring(4) : massagedText.substring(4, indexOf), str, (String) null);
    }
}
