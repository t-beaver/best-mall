package com.dcloud.zxing2.client.result;

import com.dcloud.zxing2.Result;
import java.util.regex.Pattern;

public final class EmailDoCoMoResultParser extends AbstractDoCoMoResultParser {
    private static final Pattern ATEXT_ALPHANUMERIC = Pattern.compile("[a-zA-Z0-9@.!#$%&'*+\\-/=?^_`{|}~]+");

    static boolean isBasicallyValidEmailAddress(String str) {
        return str != null && ATEXT_ALPHANUMERIC.matcher(str).matches() && str.indexOf(64) >= 0;
    }

    public EmailAddressParsedResult parse(Result result) {
        String[] matchDoCoMoPrefixedField;
        String massagedText = ResultParser.getMassagedText(result);
        if (!massagedText.startsWith("MATMSG:") || (matchDoCoMoPrefixedField = AbstractDoCoMoResultParser.matchDoCoMoPrefixedField("TO:", massagedText, true)) == null) {
            return null;
        }
        for (String isBasicallyValidEmailAddress : matchDoCoMoPrefixedField) {
            if (!isBasicallyValidEmailAddress(isBasicallyValidEmailAddress)) {
                return null;
            }
        }
        return new EmailAddressParsedResult(matchDoCoMoPrefixedField, (String[]) null, (String[]) null, AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("SUB:", massagedText, false), AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("BODY:", massagedText, false));
    }
}
