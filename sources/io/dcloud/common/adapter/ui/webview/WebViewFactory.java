package io.dcloud.common.adapter.ui.webview;

import android.app.Activity;
import android.content.Context;
import android.webkit.WebSettings;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.performance.WXInstanceApm;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.IDCloudWebviewClientListener;
import io.dcloud.common.DHInterface.IWebViewFactory;
import io.dcloud.common.DHInterface.IWebViewInstallListener;
import io.dcloud.common.adapter.ui.AdaWebview;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.PermissionUtil;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.f.a;
import java.util.ArrayList;

public class WebViewFactory {
    public static boolean isAllowFileAccessFromFileURLs = true;
    /* access modifiers changed from: private */
    public static boolean isLoadOtherTimeOut = false;
    private static boolean isOther = false;
    private static boolean isOtherInitSuccess = false;
    private static boolean isOtherInitialised = false;
    private static boolean isSysWebViewCreate = false;
    /* access modifiers changed from: private */
    public static ICallBack otherCallBack;
    private static Runnable otherDelyedRunnable = new Runnable() {
        public void run() {
            boolean unused = WebViewFactory.isLoadOtherTimeOut = true;
            if (WebViewFactory.otherCallBack != null) {
                WebViewFactory.otherCallBack.onCallBack(0, (Object) null);
                ICallBack unused2 = WebViewFactory.otherCallBack = null;
            }
        }
    };
    private static IWebViewFactory sOtherWebViewFactory;
    public static PermissionUtil.StreamPermissionRequest sStreamPermissionRequest = null;
    public static ArrayList<PerWrapper> sUsePermissionWebviews = new ArrayList<>();
    private static IWebViewInstallListener webViewInstallListener;

    public static String getDefWebViewUA(Context context) {
        String defWebViewUA = getOtherWebViewFactory() != null ? getOtherWebViewFactory().getDefWebViewUA(context) : "";
        return PdrUtil.isEmpty(defWebViewUA) ? WebSettings.getDefaultUserAgent(context) : defWebViewUA;
    }

    private static IWebViewFactory getOtherWebViewFactory() {
        IWebViewFactory iWebViewFactory;
        if (!isIsOtherInitSuccess() || (iWebViewFactory = sOtherWebViewFactory) == null || isSysWebViewCreate) {
            return null;
        }
        return iWebViewFactory;
    }

    public static DCWebView getWebView(Activity activity, AdaWebview adaWebview) {
        DCWebView webView = getOtherWebViewFactory() != null ? getOtherWebViewFactory().getWebView(activity, adaWebview) : null;
        if (webView != null) {
            return webView;
        }
        isSysWebViewCreate = true;
        return new SysWebView(activity, adaWebview);
    }

    public static IWebViewInstallListener getWebViewInstallListener() {
        return webViewInstallListener;
    }

    public static String getWebViewUserAgentVersion(Context context) {
        return getWebViewUserAgentVersion(context, (String) null);
    }

    public static void initOther(boolean z, long j) {
        isOther = z;
        if (j != 0) {
            MessageHandler.postDelayed(otherDelyedRunnable, j);
        }
    }

    public static boolean isIsLoadOtherTimeOut() {
        return isLoadOtherTimeOut;
    }

    public static boolean isIsOtherInitSuccess() {
        return isOtherInitSuccess;
    }

    public static boolean isOther() {
        return isOther;
    }

    public static boolean isOtherInitialised() {
        return isOtherInitialised;
    }

    public static void openJSEnabled(Object obj, IApp iApp) {
        Class[] clsArr = {Boolean.TYPE};
        Object[] objArr = {Boolean.TRUE};
        String decodeString = iApp != null ? iApp.getConfusionMgr().decodeString("e218Qml+aVtremF4fEtpZkd4bWZfYWZsZ397SX18Z2VpfGFraWRkcQ==") : a.a("e218Qml+aVtremF4fEtpZkd4bWZfYWZsZ397SX18Z2VpfGFraWRkcQ==");
        String decodeString2 = iApp != null ? iApp.getConfusionMgr().decodeString("e218Qml+aVtremF4fE1maWpkbWw=") : a.a("e218Qml+aVtremF4fE1maWpkbWw=");
        PlatformUtil.invokeMethod(obj, decodeString, clsArr, objArr);
        PlatformUtil.invokeMethod(obj, decodeString2, clsArr, objArr);
    }

    public static void removeDelayRunnable() {
        if (MessageHandler.hasCallbacks(otherDelyedRunnable)) {
            MessageHandler.removeCallbacks(otherDelyedRunnable);
        }
    }

    public static void resetSysWebViewState() {
        isSysWebViewCreate = false;
    }

    public static void resetUA() {
        BaseInfo.sDefWebViewUserAgent = "";
    }

    public static void setFileAccess(Object obj, IApp iApp, boolean z) {
        if (obj != null && DeviceInfo.sDeviceSdkVer > 16) {
            try {
                Class[] clsArr = {Boolean.TYPE};
                Object[] objArr = {Boolean.valueOf(z)};
                String decodeString = iApp != null ? iApp.getConfusionMgr().decodeString("eW9+S2ZmZX1fZGN8b3h5a2ZLaWlveXlMeGVnTGNmb19YRnkqNmEzZDg4ZmEtNGJhMC00NzlmLTk0MjItZTVhYWJlMTU4OTdiNzQ=", true, 10) : a.a("eW9+S2ZmZX1fZGN8b3h5a2ZLaWlveXlMeGVnTGNmb19YRnkqNmEzZDg4ZmEtNGJhMC00NzlmLTk0MjItZTVhYWJlMTU4OTdiNzQ=", true, 10);
                String decodeString2 = iApp != null ? iApp.getConfusionMgr().decodeString("eG5/SmdnZHxNYmduSmhobnh4TXlkZk1iZ25eWUd4KjZhM2Q4OGZhLTRiYTAtNDc5Zi05NDIyLWU1YWFiZTE1ODk3Yjc1", true, 11) : a.a("eG5/SmdnZHxNYmduSmhobnh4TXlkZk1iZ25eWUd4KjZhM2Q4OGZhLTRiYTAtNDc5Zi05NDIyLWU1YWFiZTE1ODk3Yjc1", true, 11);
                String decodeString3 = iApp != null ? iApp.getConfusionMgr().decodeString("f2l4TWBgY3tKZWBpTW9vaX9/KjZhM2Q4OGZhLTRiYTAtNDc5Zi05NDIyLWU1YWFiZTE1ODk3Yjc2", true, 12) : a.a("f2l4TWBgY3tKZWBpTW9vaX9/KjZhM2Q4OGZhLTRiYTAtNDc5Zi05NDIyLWU1YWFiZTE1ODk3Yjc2", true, 12);
                if (isAllowFileAccessFromFileURLs || !z) {
                    PlatformUtil.invokeMethod(obj, decodeString, clsArr, objArr);
                    PlatformUtil.invokeMethod(obj, decodeString2, clsArr, objArr);
                }
                PlatformUtil.invokeMethod(obj, decodeString3, clsArr, objArr);
            } catch (Exception unused) {
            }
        }
    }

    public static void setOtherCallBack(ICallBack iCallBack) {
        isLoadOtherTimeOut = false;
        otherCallBack = iCallBack;
    }

    public static void setOtherState(boolean z, IWebViewFactory iWebViewFactory) {
        isOtherInitSuccess = z;
        isOtherInitialised = true;
        if (otherCallBack != null) {
            MessageHandler.removeCallbacks(otherDelyedRunnable);
            Boolean bool = (Boolean) otherCallBack.onCallBack(z ? 1 : 0, iWebViewFactory);
            if (bool != null && !bool.booleanValue()) {
                isOtherInitSuccess = false;
            } else if (isOtherInitSuccess) {
                sOtherWebViewFactory = iWebViewFactory;
            }
        } else if (z) {
            sOtherWebViewFactory = iWebViewFactory;
        }
    }

    public static void setSslHandlerState(Object obj, int i) {
        PlatformUtil.invokeMethod(obj, i != 1 ? i != 2 ? "" : BindingXConstants.STATE_CANCEL : "proceed", new Class[0], new Object[0]);
    }

    public static void setWebViewInstallListener(IWebViewInstallListener iWebViewInstallListener) {
        webViewInstallListener = iWebViewInstallListener;
    }

    public static boolean verifyVersion(String str, String str2) {
        String[] split = str.split("\\.");
        String[] split2 = str2.split("\\.");
        int i = 0;
        while (true) {
            if (i >= split.length && i >= split2.length) {
                return true;
            }
            int parseInt = i < split.length ? Integer.parseInt(split[i]) : 0;
            int parseInt2 = i < split2.length ? Integer.parseInt(split2[i]) : 0;
            if (parseInt > parseInt2) {
                return true;
            }
            if (parseInt < parseInt2) {
                return false;
            }
            i++;
        }
    }

    public static String getWebViewUserAgentVersion(Context context, String str) {
        if (PdrUtil.isEmpty(str)) {
            if (PdrUtil.isEmpty(BaseInfo.sDefWebViewUserAgent)) {
                BaseInfo.sDefWebViewUserAgent = getDefWebViewUA(context);
            }
            str = BaseInfo.sDefWebViewUserAgent;
        }
        String[] split = str.split(Operators.SPACE_STR);
        for (int length = split.length - 1; length > 0; length--) {
            String str2 = split[length];
            if (PdrUtil.isContains(str2.toLowerCase(), "chrome")) {
                String[] split2 = str2.split("/");
                if (split2.length > 1) {
                    return split2[1];
                }
                return WXInstanceApm.VALUE_ERROR_CODE_DEFAULT;
            }
        }
        return WXInstanceApm.VALUE_ERROR_CODE_DEFAULT;
    }

    public static DCWebView getWebView(Activity activity, AdaWebview adaWebview, IDCloudWebviewClientListener iDCloudWebviewClientListener) {
        DCWebView webView = getOtherWebViewFactory() != null ? getOtherWebViewFactory().getWebView(activity, adaWebview, iDCloudWebviewClientListener) : null;
        if (webView != null) {
            return webView;
        }
        isSysWebViewCreate = true;
        return new SysWebView((Context) activity, adaWebview, iDCloudWebviewClientListener);
    }

    public static DCWebView getWebView(Activity activity, AdaWebview adaWebview, OnPageFinishedCallack onPageFinishedCallack) {
        DCWebView webView = getOtherWebViewFactory() != null ? getOtherWebViewFactory().getWebView(activity, adaWebview, onPageFinishedCallack) : null;
        if (webView != null) {
            return webView;
        }
        isSysWebViewCreate = true;
        return new SysWebView((Context) activity, adaWebview, onPageFinishedCallack);
    }

    public static void setFileAccess(Object obj, boolean z) {
        if (obj != null) {
            setFileAccess(obj, (IApp) null, z);
        }
    }
}
