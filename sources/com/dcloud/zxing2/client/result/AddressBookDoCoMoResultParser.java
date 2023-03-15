package com.dcloud.zxing2.client.result;

import com.dcloud.zxing2.Result;

public final class AddressBookDoCoMoResultParser extends AbstractDoCoMoResultParser {
    private static String parseName(String str) {
        int indexOf = str.indexOf(44);
        if (indexOf < 0) {
            return str;
        }
        return str.substring(indexOf + 1) + ' ' + str.substring(0, indexOf);
    }

    public AddressBookParsedResult parse(Result result) {
        String[] matchDoCoMoPrefixedField;
        String massagedText = ResultParser.getMassagedText(result);
        if (!massagedText.startsWith("MECARD:") || (matchDoCoMoPrefixedField = AbstractDoCoMoResultParser.matchDoCoMoPrefixedField("N:", massagedText, true)) == null) {
            return null;
        }
        String parseName = parseName(matchDoCoMoPrefixedField[0]);
        String matchSingleDoCoMoPrefixedField = AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("SOUND:", massagedText, true);
        String[] matchDoCoMoPrefixedField2 = AbstractDoCoMoResultParser.matchDoCoMoPrefixedField("TEL:", massagedText, true);
        String[] matchDoCoMoPrefixedField3 = AbstractDoCoMoResultParser.matchDoCoMoPrefixedField("EMAIL:", massagedText, true);
        String matchSingleDoCoMoPrefixedField2 = AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("NOTE:", massagedText, false);
        String[] matchDoCoMoPrefixedField4 = AbstractDoCoMoResultParser.matchDoCoMoPrefixedField("ADR:", massagedText, true);
        String matchSingleDoCoMoPrefixedField3 = AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("BDAY:", massagedText, true);
        return new AddressBookParsedResult(ResultParser.maybeWrap(parseName), (String[]) null, matchSingleDoCoMoPrefixedField, matchDoCoMoPrefixedField2, (String[]) null, matchDoCoMoPrefixedField3, (String[]) null, (String) null, matchSingleDoCoMoPrefixedField2, matchDoCoMoPrefixedField4, (String[]) null, AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("ORG:", massagedText, true), !ResultParser.isStringOfDigits(matchSingleDoCoMoPrefixedField3, 8) ? null : matchSingleDoCoMoPrefixedField3, (String) null, AbstractDoCoMoResultParser.matchDoCoMoPrefixedField("URL:", massagedText, true), (String[]) null);
    }
}
