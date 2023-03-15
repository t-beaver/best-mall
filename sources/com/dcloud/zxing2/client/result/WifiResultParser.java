package com.dcloud.zxing2.client.result;

import com.dcloud.zxing2.Result;

public final class WifiResultParser extends ResultParser {
    public WifiParsedResult parse(Result result) {
        String matchSinglePrefixedField;
        String massagedText = ResultParser.getMassagedText(result);
        if (!massagedText.startsWith("WIFI:") || (matchSinglePrefixedField = ResultParser.matchSinglePrefixedField("S:", massagedText, ';', false)) == null || matchSinglePrefixedField.isEmpty()) {
            return null;
        }
        String matchSinglePrefixedField2 = ResultParser.matchSinglePrefixedField("P:", massagedText, ';', false);
        String matchSinglePrefixedField3 = ResultParser.matchSinglePrefixedField("T:", massagedText, ';', false);
        if (matchSinglePrefixedField3 == null) {
            matchSinglePrefixedField3 = "nopass";
        }
        return new WifiParsedResult(matchSinglePrefixedField3, matchSinglePrefixedField, matchSinglePrefixedField2, Boolean.parseBoolean(ResultParser.matchSinglePrefixedField("H:", massagedText, ';', false)));
    }
}
