package com.taobao.weex.http;

import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.component.WXImage;
import io.dcloud.common.DHInterface.IReflectAble;

public class CertJSResponse implements IReflectAble {
    public int code;
    public String message;
    public String type;

    public static CertJSResponse obtainSuccess() {
        CertJSResponse certJSResponse = new CertJSResponse();
        certJSResponse.type = WXImage.SUCCEED;
        certJSResponse.code = 0;
        certJSResponse.message = "";
        return certJSResponse;
    }

    public static CertJSResponse obtainFail(int i, String str) {
        CertJSResponse certJSResponse = new CertJSResponse();
        certJSResponse.type = Constants.Event.FAIL;
        certJSResponse.code = i;
        certJSResponse.message = str;
        return certJSResponse;
    }
}
