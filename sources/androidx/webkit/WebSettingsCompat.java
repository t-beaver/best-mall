package androidx.webkit;

import android.webkit.WebSettings;
import androidx.webkit.internal.WebSettingsAdapter;
import androidx.webkit.internal.WebViewFeatureInternal;
import androidx.webkit.internal.WebViewGlueCommunicator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class WebSettingsCompat {
    public static final int DARK_STRATEGY_PREFER_WEB_THEME_OVER_USER_AGENT_DARKENING = 2;
    public static final int DARK_STRATEGY_USER_AGENT_DARKENING_ONLY = 0;
    public static final int DARK_STRATEGY_WEB_THEME_DARKENING_ONLY = 1;
    public static final int FORCE_DARK_AUTO = 1;
    public static final int FORCE_DARK_OFF = 0;
    public static final int FORCE_DARK_ON = 2;

    @Target({ElementType.PARAMETER, ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ForceDark {
    }

    @Target({ElementType.PARAMETER, ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ForceDarkStrategy {
    }

    @Target({ElementType.PARAMETER, ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MenuItemFlags {
    }

    private WebSettingsCompat() {
    }

    public static void setOffscreenPreRaster(WebSettings webSettings, boolean z) {
        WebViewFeatureInternal feature = WebViewFeatureInternal.getFeature("OFF_SCREEN_PRERASTER");
        if (feature.isSupportedByFramework()) {
            webSettings.setOffscreenPreRaster(z);
        } else if (feature.isSupportedByWebView()) {
            getAdapter(webSettings).setOffscreenPreRaster(z);
        } else {
            throw WebViewFeatureInternal.getUnsupportedOperationException();
        }
    }

    public static boolean getOffscreenPreRaster(WebSettings webSettings) {
        WebViewFeatureInternal feature = WebViewFeatureInternal.getFeature("OFF_SCREEN_PRERASTER");
        if (feature.isSupportedByFramework()) {
            return webSettings.getOffscreenPreRaster();
        }
        if (feature.isSupportedByWebView()) {
            return getAdapter(webSettings).getOffscreenPreRaster();
        }
        throw WebViewFeatureInternal.getUnsupportedOperationException();
    }

    public static void setSafeBrowsingEnabled(WebSettings webSettings, boolean z) {
        WebViewFeatureInternal feature = WebViewFeatureInternal.getFeature("SAFE_BROWSING_ENABLE");
        if (feature.isSupportedByFramework()) {
            webSettings.setSafeBrowsingEnabled(z);
        } else if (feature.isSupportedByWebView()) {
            getAdapter(webSettings).setSafeBrowsingEnabled(z);
        } else {
            throw WebViewFeatureInternal.getUnsupportedOperationException();
        }
    }

    public static boolean getSafeBrowsingEnabled(WebSettings webSettings) {
        WebViewFeatureInternal feature = WebViewFeatureInternal.getFeature("SAFE_BROWSING_ENABLE");
        if (feature.isSupportedByFramework()) {
            return webSettings.getSafeBrowsingEnabled();
        }
        if (feature.isSupportedByWebView()) {
            return getAdapter(webSettings).getSafeBrowsingEnabled();
        }
        throw WebViewFeatureInternal.getUnsupportedOperationException();
    }

    public static void setDisabledActionModeMenuItems(WebSettings webSettings, int i) {
        WebViewFeatureInternal feature = WebViewFeatureInternal.getFeature("DISABLED_ACTION_MODE_MENU_ITEMS");
        if (feature.isSupportedByFramework()) {
            webSettings.setDisabledActionModeMenuItems(i);
        } else if (feature.isSupportedByWebView()) {
            getAdapter(webSettings).setDisabledActionModeMenuItems(i);
        } else {
            throw WebViewFeatureInternal.getUnsupportedOperationException();
        }
    }

    public static int getDisabledActionModeMenuItems(WebSettings webSettings) {
        WebViewFeatureInternal feature = WebViewFeatureInternal.getFeature("DISABLED_ACTION_MODE_MENU_ITEMS");
        if (feature.isSupportedByFramework()) {
            return webSettings.getDisabledActionModeMenuItems();
        }
        if (feature.isSupportedByWebView()) {
            return getAdapter(webSettings).getDisabledActionModeMenuItems();
        }
        throw WebViewFeatureInternal.getUnsupportedOperationException();
    }

    public static void setWillSuppressErrorPage(WebSettings webSettings, boolean z) {
        if (WebViewFeatureInternal.getFeature("SUPPRESS_ERROR_PAGE").isSupportedByWebView()) {
            getAdapter(webSettings).setWillSuppressErrorPage(z);
            return;
        }
        throw WebViewFeatureInternal.getUnsupportedOperationException();
    }

    public static boolean willSuppressErrorPage(WebSettings webSettings) {
        if (WebViewFeatureInternal.getFeature("SUPPRESS_ERROR_PAGE").isSupportedByWebView()) {
            return getAdapter(webSettings).willSuppressErrorPage();
        }
        throw WebViewFeatureInternal.getUnsupportedOperationException();
    }

    public static void setForceDark(WebSettings webSettings, int i) {
        WebViewFeatureInternal feature = WebViewFeatureInternal.getFeature("FORCE_DARK");
        if (feature.isSupportedByFramework()) {
            webSettings.setForceDark(i);
        } else if (feature.isSupportedByWebView()) {
            getAdapter(webSettings).setForceDark(i);
        } else {
            throw WebViewFeatureInternal.getUnsupportedOperationException();
        }
    }

    public static int getForceDark(WebSettings webSettings) {
        WebViewFeatureInternal feature = WebViewFeatureInternal.getFeature("FORCE_DARK");
        if (feature.isSupportedByFramework()) {
            return webSettings.getForceDark();
        }
        if (feature.isSupportedByWebView()) {
            return getAdapter(webSettings).getForceDark();
        }
        throw WebViewFeatureInternal.getUnsupportedOperationException();
    }

    public static void setForceDarkStrategy(WebSettings webSettings, int i) {
        if (WebViewFeatureInternal.getFeature(WebViewFeature.FORCE_DARK_STRATEGY).isSupportedByWebView()) {
            getAdapter(webSettings).setForceDarkStrategy(i);
            return;
        }
        throw WebViewFeatureInternal.getUnsupportedOperationException();
    }

    public static int getForceDarkStrategy(WebSettings webSettings) {
        if (WebViewFeatureInternal.getFeature(WebViewFeature.FORCE_DARK_STRATEGY).isSupportedByWebView()) {
            return getAdapter(webSettings).getForceDark();
        }
        throw WebViewFeatureInternal.getUnsupportedOperationException();
    }

    private static WebSettingsAdapter getAdapter(WebSettings webSettings) {
        return WebViewGlueCommunicator.getCompatConverter().convertSettings(webSettings);
    }
}
