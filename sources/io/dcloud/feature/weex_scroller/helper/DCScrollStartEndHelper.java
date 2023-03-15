package io.dcloud.feature.weex_scroller.helper;

import com.taobao.weex.common.Constants;

public class DCScrollStartEndHelper {
    public static boolean isScrollEvent(String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -907680051:
                if (str.equals("scroll")) {
                    c = 0;
                    break;
                }
                break;
            case 417796846:
                if (str.equals(Constants.Event.SCROLL_END)) {
                    c = 1;
                    break;
                }
                break;
            case 1946106361:
                if (str.equals("scrolltolower")) {
                    c = 2;
                    break;
                }
                break;
            case 1954441114:
                if (str.equals("scrolltoupper")) {
                    c = 3;
                    break;
                }
                break;
            case 2083919285:
                if (str.equals(Constants.Event.SCROLL_START)) {
                    c = 4;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                return true;
            default:
                return false;
        }
    }
}
