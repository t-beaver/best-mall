package com.dcloud.zxing2.client.result;

import com.dcloud.zxing2.Result;
import java.util.ArrayList;

public final class BizcardResultParser extends AbstractDoCoMoResultParser {
    private static String buildName(String str, String str2) {
        if (str == null) {
            return str2;
        }
        if (str2 == null) {
            return str;
        }
        return str + ' ' + str2;
    }

    private static String[] buildPhoneNumbers(String str, String str2, String str3) {
        ArrayList arrayList = new ArrayList(3);
        if (str != null) {
            arrayList.add(str);
        }
        if (str2 != null) {
            arrayList.add(str2);
        }
        if (str3 != null) {
            arrayList.add(str3);
        }
        int size = arrayList.size();
        if (size == 0) {
            return null;
        }
        return (String[]) arrayList.toArray(new String[size]);
    }

    public AddressBookParsedResult parse(Result result) {
        String massagedText = ResultParser.getMassagedText(result);
        if (!massagedText.startsWith("BIZCARD:")) {
            return null;
        }
        String buildName = buildName(AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("N:", massagedText, true), AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("X:", massagedText, true));
        String matchSingleDoCoMoPrefixedField = AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("T:", massagedText, true);
        String matchSingleDoCoMoPrefixedField2 = AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("C:", massagedText, true);
        return new AddressBookParsedResult(ResultParser.maybeWrap(buildName), (String[]) null, (String) null, buildPhoneNumbers(AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("B:", massagedText, true), AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("M:", massagedText, true), AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("F:", massagedText, true)), (String[]) null, ResultParser.maybeWrap(AbstractDoCoMoResultParser.matchSingleDoCoMoPrefixedField("E:", massagedText, true)), (String[]) null, (String) null, (String) null, AbstractDoCoMoResultParser.matchDoCoMoPrefixedField("A:", massagedText, true), (String[]) null, matchSingleDoCoMoPrefixedField2, (String) null, matchSingleDoCoMoPrefixedField, (String[]) null, (String[]) null);
    }
}
