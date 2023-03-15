package io.dcloud.feature.weex.adapter;

import android.net.Uri;
import android.text.TextUtils;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.adapter.URIAdapter;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.uniapp.adapter.AbsURIAdapter;
import io.dcloud.feature.weex.WeexInstanceMgr;

public class PlusUriAdapter implements URIAdapter {
    public Uri rewrite(WXSDKInstance wXSDKInstance, String str, Uri uri) {
        String str2;
        if (!TextUtils.isEmpty(wXSDKInstance.getBundleUrl()) && !PdrUtil.isEmpty(uri)) {
            String bundleUrl = wXSDKInstance.getBundleUrl();
            String uri2 = uri.toString();
            if (uri.isRelative()) {
                if (uri.getEncodedPath().length() == 0) {
                    return Uri.parse(bundleUrl);
                }
                IWebview findWebview = WeexInstanceMgr.self().findWebview(wXSDKInstance);
                if (findWebview != null) {
                    byte obtainRunningAppMode = findWebview.obtainApp().obtainRunningAppMode();
                    if (uri2.startsWith("/storage") || obtainRunningAppMode != 1) {
                        str2 = findWebview.obtainApp().convert2WebviewFullPath(bundleUrl, uri2);
                    } else {
                        String convert2AbsFullPath = findWebview.obtainApp().convert2AbsFullPath(bundleUrl, uri2);
                        if ("web".equals(str)) {
                            convert2AbsFullPath = findWebview.obtainApp().convert2WebviewFullPath(bundleUrl, uri2);
                        }
                        if (convert2AbsFullPath.startsWith("/storage/")) {
                            str2 = findWebview.obtainApp().convert2WebviewFullPath(bundleUrl, uri2);
                        } else {
                            if (convert2AbsFullPath.startsWith("/")) {
                                convert2AbsFullPath = convert2AbsFullPath.substring(1, convert2AbsFullPath.length());
                            }
                            if (str.equals(AbsURIAdapter.FONT)) {
                                str2 = "local:///" + convert2AbsFullPath;
                            } else {
                                str2 = "asset:///" + convert2AbsFullPath;
                            }
                        }
                    }
                } else {
                    str2 = standardizedURL(bundleUrl, uri2);
                }
                if (bundleUrl.startsWith("/storage")) {
                    str2 = DeviceInfo.FILE_PROTOCOL + str2;
                } else if (bundleUrl.startsWith("storage")) {
                    str2 = "file:///" + str2;
                }
                return Uri.parse(str2);
            }
        }
        return uri;
    }

    public Uri rewrite(String str, String str2, Uri uri) {
        String uri2 = uri.toString();
        if (!uri.isRelative()) {
            return uri;
        }
        if (uri.getEncodedPath().length() == 0) {
            return Uri.parse(str);
        }
        return Uri.parse(standardizedURL(str, uri2));
    }

    private String standardizedURL(String str, String str2) {
        boolean z = true;
        if (str2.startsWith("./")) {
            str2 = str2.substring(2);
            int lastIndexOf = str.lastIndexOf(47);
            if (lastIndexOf >= 0) {
                return str.substring(0, lastIndexOf + 1) + str2;
            }
        }
        int indexOf = str2.indexOf("../");
        int lastIndexOf2 = str.lastIndexOf(47);
        if (lastIndexOf2 <= -1) {
            z = false;
        }
        if (!z) {
            return str2;
        }
        String substring = str.substring(0, lastIndexOf2);
        while (indexOf > -1) {
            str2 = str2.substring(3);
            substring = substring.substring(0, substring.lastIndexOf(47));
            indexOf = str2.indexOf("../");
        }
        return substring + '/' + str2;
    }
}
