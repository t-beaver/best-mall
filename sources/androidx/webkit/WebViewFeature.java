package androidx.webkit;

import androidx.webkit.internal.WebViewFeatureInternal;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class WebViewFeature {
    public static final String CREATE_WEB_MESSAGE_CHANNEL = "CREATE_WEB_MESSAGE_CHANNEL";
    public static final String DISABLED_ACTION_MODE_MENU_ITEMS = "DISABLED_ACTION_MODE_MENU_ITEMS";
    public static final String DOCUMENT_START_SCRIPT = "DOCUMENT_START_SCRIPT";
    public static final String FORCE_DARK = "FORCE_DARK";
    public static final String FORCE_DARK_STRATEGY = "FORCE_DARK_STRATEGY";
    public static final String GET_WEB_CHROME_CLIENT = "GET_WEB_CHROME_CLIENT";
    public static final String GET_WEB_VIEW_CLIENT = "GET_WEB_VIEW_CLIENT";
    public static final String GET_WEB_VIEW_RENDERER = "GET_WEB_VIEW_RENDERER";
    public static final String MULTI_PROCESS = "MULTI_PROCESS";
    public static final String OFF_SCREEN_PRERASTER = "OFF_SCREEN_PRERASTER";
    public static final String POST_WEB_MESSAGE = "POST_WEB_MESSAGE";
    public static final String PROXY_OVERRIDE = "PROXY_OVERRIDE";
    public static final String RECEIVE_HTTP_ERROR = "RECEIVE_HTTP_ERROR";
    public static final String RECEIVE_WEB_RESOURCE_ERROR = "RECEIVE_WEB_RESOURCE_ERROR";
    public static final String SAFE_BROWSING_ENABLE = "SAFE_BROWSING_ENABLE";
    public static final String SAFE_BROWSING_HIT = "SAFE_BROWSING_HIT";
    public static final String SAFE_BROWSING_PRIVACY_POLICY_URL = "SAFE_BROWSING_PRIVACY_POLICY_URL";
    public static final String SAFE_BROWSING_RESPONSE_BACK_TO_SAFETY = "SAFE_BROWSING_RESPONSE_BACK_TO_SAFETY";
    public static final String SAFE_BROWSING_RESPONSE_PROCEED = "SAFE_BROWSING_RESPONSE_PROCEED";
    public static final String SAFE_BROWSING_RESPONSE_SHOW_INTERSTITIAL = "SAFE_BROWSING_RESPONSE_SHOW_INTERSTITIAL";
    public static final String SAFE_BROWSING_WHITELIST = "SAFE_BROWSING_WHITELIST";
    public static final String SERVICE_WORKER_BASIC_USAGE = "SERVICE_WORKER_BASIC_USAGE";
    public static final String SERVICE_WORKER_BLOCK_NETWORK_LOADS = "SERVICE_WORKER_BLOCK_NETWORK_LOADS";
    public static final String SERVICE_WORKER_CACHE_MODE = "SERVICE_WORKER_CACHE_MODE";
    public static final String SERVICE_WORKER_CONTENT_ACCESS = "SERVICE_WORKER_CONTENT_ACCESS";
    public static final String SERVICE_WORKER_FILE_ACCESS = "SERVICE_WORKER_FILE_ACCESS";
    public static final String SERVICE_WORKER_SHOULD_INTERCEPT_REQUEST = "SERVICE_WORKER_SHOULD_INTERCEPT_REQUEST";
    public static final String SHOULD_OVERRIDE_WITH_REDIRECTS = "SHOULD_OVERRIDE_WITH_REDIRECTS";
    public static final String START_SAFE_BROWSING = "START_SAFE_BROWSING";
    public static final String SUPPRESS_ERROR_PAGE = "SUPPRESS_ERROR_PAGE";
    public static final String TRACING_CONTROLLER_BASIC_USAGE = "TRACING_CONTROLLER_BASIC_USAGE";
    public static final String VISUAL_STATE_CALLBACK = "VISUAL_STATE_CALLBACK";
    public static final String WEB_MESSAGE_CALLBACK_ON_MESSAGE = "WEB_MESSAGE_CALLBACK_ON_MESSAGE";
    public static final String WEB_MESSAGE_LISTENER = "WEB_MESSAGE_LISTENER";
    public static final String WEB_MESSAGE_PORT_CLOSE = "WEB_MESSAGE_PORT_CLOSE";
    public static final String WEB_MESSAGE_PORT_POST_MESSAGE = "WEB_MESSAGE_PORT_POST_MESSAGE";
    public static final String WEB_MESSAGE_PORT_SET_MESSAGE_CALLBACK = "WEB_MESSAGE_PORT_SET_MESSAGE_CALLBACK";
    public static final String WEB_RESOURCE_ERROR_GET_CODE = "WEB_RESOURCE_ERROR_GET_CODE";
    public static final String WEB_RESOURCE_ERROR_GET_DESCRIPTION = "WEB_RESOURCE_ERROR_GET_DESCRIPTION";
    public static final String WEB_RESOURCE_REQUEST_IS_REDIRECT = "WEB_RESOURCE_REQUEST_IS_REDIRECT";
    public static final String WEB_VIEW_RENDERER_CLIENT_BASIC_USAGE = "WEB_VIEW_RENDERER_CLIENT_BASIC_USAGE";
    public static final String WEB_VIEW_RENDERER_TERMINATE = "WEB_VIEW_RENDERER_TERMINATE";

    @Target({ElementType.PARAMETER, ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface WebViewSupportFeature {
    }

    private WebViewFeature() {
    }

    public static boolean isFeatureSupported(String str) {
        WebViewFeatureInternal feature = WebViewFeatureInternal.getFeature(str);
        return feature.isSupportedByFramework() || feature.isSupportedByWebView();
    }
}
