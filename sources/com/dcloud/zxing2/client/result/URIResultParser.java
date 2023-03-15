package com.dcloud.zxing2.client.result;

import com.dcloud.zxing2.Result;
import com.taobao.weex.el.parse.Operators;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class URIResultParser extends ResultParser {
    private static final Pattern URL_WITHOUT_PROTOCOL_PATTERN = Pattern.compile("([a-zA-Z0-9\\-]+\\.)+[a-zA-Z]{2,}(:\\d{1,5})?(/|\\?|$)");
    private static final Pattern URL_WITH_PROTOCOL_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9+-.]+:");

    static boolean isBasicallyValidURI(String str) {
        if (str.contains(Operators.SPACE_STR)) {
            return false;
        }
        Matcher matcher = URL_WITH_PROTOCOL_PATTERN.matcher(str);
        if (matcher.find() && matcher.start() == 0) {
            return true;
        }
        Matcher matcher2 = URL_WITHOUT_PROTOCOL_PATTERN.matcher(str);
        if (!matcher2.find() || matcher2.start() != 0) {
            return false;
        }
        return true;
    }

    public URIParsedResult parse(Result result) {
        String massagedText = ResultParser.getMassagedText(result);
        if (massagedText.startsWith("URL:") || massagedText.startsWith("URI:")) {
            return new URIParsedResult(massagedText.substring(4).trim(), (String) null);
        }
        String trim = massagedText.trim();
        if (isBasicallyValidURI(trim)) {
            return new URIParsedResult(trim, (String) null);
        }
        return null;
    }
}
