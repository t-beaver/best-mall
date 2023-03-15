package io.dcloud.common.util;

import com.taobao.weex.common.Constants;
import java.util.ArrayList;

public class AppStreamUtil {
    public static ArrayList<String> AppStreamSchemeWhiteDefaultList;

    static {
        ArrayList<String> arrayList = new ArrayList<>();
        AppStreamSchemeWhiteDefaultList = arrayList;
        arrayList.add("sms");
        AppStreamSchemeWhiteDefaultList.add(Constants.Value.TEL);
        AppStreamSchemeWhiteDefaultList.add("mailto");
        AppStreamSchemeWhiteDefaultList.add("callto");
        AppStreamSchemeWhiteDefaultList.add("weixin");
        AppStreamSchemeWhiteDefaultList.add("alipay");
        AppStreamSchemeWhiteDefaultList.add("alipays");
        AppStreamSchemeWhiteDefaultList.add("alipayqr");
        AppStreamSchemeWhiteDefaultList.add("weibo");
        AppStreamSchemeWhiteDefaultList.add("mqq");
        AppStreamSchemeWhiteDefaultList.add("mqqapi");
        AppStreamSchemeWhiteDefaultList.add("qqmap");
        AppStreamSchemeWhiteDefaultList.add("baidumap");
        AppStreamSchemeWhiteDefaultList.add("amap");
        AppStreamSchemeWhiteDefaultList.add("iosamap");
        AppStreamSchemeWhiteDefaultList.add(AbsoluteConst.XML_STREAMAPP);
    }
}
