package io.dcloud.sdk.core.util;

import android.content.Context;
import io.dcloud.base.R;
import io.dcloud.h.c.a;
import java.util.Locale;

public class AdErrorUtil {
    public static String getErrorMsg(int i) {
        if (a.d() == null || a.d().c() == null) {
            return "zh".equalsIgnoreCase(Locale.getDefault().getCountry()) ? "广告未初始化或者初始化信息为空" : "Please init first";
        }
        Context c = a.d().c();
        if (i == -5100) {
            return c.getString(R.string.dcloud_ad_error_code_5100);
        }
        switch (i) {
            case -5022:
                return c.getString(R.string.dcloud_ad_error_code_5022);
            case -5021:
                return c.getString(R.string.dcloud_ad_error_code_5021);
            case -5020:
                return c.getString(R.string.dcloud_ad_error_code_5020);
            case -5019:
                return c.getString(R.string.dcloud_ad_error_code_5019);
            case -5018:
                return c.getString(R.string.dcloud_ad_error_code_5018);
            case -5017:
                return c.getString(R.string.dcloud_ad_error_code_5017);
            case -5016:
                return c.getString(R.string.dcloud_ad_error_code_5016);
            case -5015:
                return c.getString(R.string.dcloud_ad_error_code_5015);
            case -5014:
                return c.getString(R.string.dcloud_ad_error_code_5014);
            case -5013:
                return c.getString(R.string.dcloud_ad_error_code_5013);
            case -5012:
                return c.getString(R.string.dcloud_ad_error_code_5012);
            case -5011:
                return c.getString(R.string.dcloud_ad_error_code_5011);
            default:
                switch (i) {
                    case -5008:
                        return c.getString(R.string.dcloud_ad_error_code_5008);
                    case -5007:
                        return c.getString(R.string.dcloud_ad_error_code_5007);
                    case -5006:
                        return c.getString(R.string.dcloud_ad_error_code_5006);
                    case -5005:
                        return c.getString(R.string.dcloud_ad_error_code_5005);
                    case -5004:
                        return c.getString(R.string.dcloud_ad_error_code_5004);
                    case -5003:
                        return c.getString(R.string.dcloud_ad_error_code_5003);
                    case -5002:
                        return c.getString(R.string.dcloud_ad_error_code_5002);
                    case -5001:
                        return c.getString(R.string.dcloud_ad_error_code_5001);
                    case -5000:
                        return "";
                    default:
                        if (i == -603) {
                            return c.getString(R.string.dcloud_ad_error_code_603);
                        }
                        if (i != 200000) {
                            return "";
                        }
                        return c.getString(R.string.dcloud_ad_error_code_200000);
                }
        }
    }
}
