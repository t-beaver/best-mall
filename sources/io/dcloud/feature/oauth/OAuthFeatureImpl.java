package io.dcloud.feature.oauth;

import android.app.Application;
import io.dcloud.application.DCloudApplication;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.DHInterface.StandardFeature;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.StringUtil;
import org.json.JSONArray;

public class OAuthFeatureImpl extends StandardFeature {
    public void addPhoneNumber(IWebview iWebview, JSONArray jSONArray) throws Exception {
        AppRuntime.checkPrivacyComplianceAndPrompt(iWebview.getContext(), "Oauth-addPhoneNumber");
        BaseOAuthService baseOAuthService = (BaseOAuthService) getBaseModuleById(jSONArray.getString(0));
        if (baseOAuthService != null) {
            baseOAuthService.addPhoneNumber(iWebview, jSONArray);
            return;
        }
        JSUtil.execCallback(iWebview, jSONArray.getString(1), StringUtil.format(DOMException.JSON_ERROR_INFO, 19, DOMException.MSG_OAUTH_LOGIN), JSUtil.ERROR, false);
    }

    public void authorize(IWebview iWebview, JSONArray jSONArray) throws Exception {
        AppRuntime.checkPrivacyComplianceAndPrompt(iWebview.getContext(), "Oauth-authorize");
        BaseOAuthService baseOAuthService = (BaseOAuthService) getBaseModuleById(jSONArray.getString(0));
        if (baseOAuthService != null) {
            Application application = iWebview.getActivity().getApplication();
            if (application instanceof DCloudApplication) {
                ((DCloudApplication) application).stopB2FOnce();
            }
            baseOAuthService.authorize(iWebview, jSONArray);
            return;
        }
        JSUtil.execCallback(iWebview, jSONArray.getString(1), StringUtil.format(DOMException.JSON_ERROR_INFO, 19, DOMException.MSG_OAUTH_LOGIN), JSUtil.ERROR, false);
    }

    public void closeAuthView(IWebview iWebview, JSONArray jSONArray) throws Exception {
        AppRuntime.checkPrivacyComplianceAndPrompt(iWebview.getContext(), "Oauth-closeAuthView");
        BaseOAuthService baseOAuthService = (BaseOAuthService) getBaseModuleById(jSONArray.getString(0));
        if (baseOAuthService != null) {
            baseOAuthService.closeAuthView(iWebview, jSONArray);
        }
    }

    public boolean getCheckBoxState(IWebview iWebview, JSONArray jSONArray) throws Exception {
        AppRuntime.checkPrivacyComplianceAndPrompt(iWebview.getContext(), "Oauth-getCheckBoxState");
        BaseOAuthService baseOAuthService = (BaseOAuthService) getBaseModuleById(jSONArray.getString(0));
        if (baseOAuthService != null) {
            return baseOAuthService.getCheckBoxState();
        }
        return false;
    }

    public void getServices(IWebview iWebview, JSONArray jSONArray) throws Exception {
        AppRuntime.checkPrivacyComplianceAndPrompt(iWebview.getContext(), "Oauth-getServices");
        loadModules();
        JSUtil.execCallback(iWebview, jSONArray.getString(0), toModuleJSONArray(), JSUtil.OK, false);
    }

    public void getUserInfo(IWebview iWebview, JSONArray jSONArray) throws Exception {
        AppRuntime.checkPrivacyComplianceAndPrompt(iWebview.getContext(), "Oauth-getUserInfo");
        BaseOAuthService baseOAuthService = (BaseOAuthService) getBaseModuleById(jSONArray.getString(0));
        if (baseOAuthService != null) {
            baseOAuthService.getUserInfo(iWebview, jSONArray);
            return;
        }
        JSUtil.execCallback(iWebview, jSONArray.getString(1), StringUtil.format(DOMException.JSON_ERROR_INFO, 21, DOMException.MSG_OAUTH_GET_USERINFO), JSUtil.ERROR, false);
    }

    public void login(IWebview iWebview, JSONArray jSONArray) throws Exception {
        AppRuntime.checkPrivacyComplianceAndPrompt(iWebview.getContext(), "Oauth-login");
        BaseOAuthService baseOAuthService = (BaseOAuthService) getBaseModuleById(jSONArray.getString(0));
        if (baseOAuthService != null) {
            Application application = iWebview.getActivity().getApplication();
            if (application instanceof DCloudApplication) {
                ((DCloudApplication) application).stopB2FOnce();
            }
            baseOAuthService.login(iWebview, jSONArray);
            return;
        }
        JSUtil.execCallback(iWebview, jSONArray.getString(1), StringUtil.format(DOMException.JSON_ERROR_INFO, 19, DOMException.MSG_OAUTH_LOGIN), JSUtil.ERROR, false);
    }

    public void logout(IWebview iWebview, JSONArray jSONArray) throws Exception {
        AppRuntime.checkPrivacyComplianceAndPrompt(iWebview.getContext(), "Oauth-logout");
        BaseOAuthService baseOAuthService = (BaseOAuthService) getBaseModuleById(jSONArray.getString(0));
        if (baseOAuthService != null) {
            baseOAuthService.logout(iWebview, jSONArray);
            return;
        }
        JSUtil.execCallback(iWebview, jSONArray.getString(1), StringUtil.format(DOMException.JSON_ERROR_INFO, 20, DOMException.MSG_OAUTH_LOGOUT), JSUtil.ERROR, false);
    }

    public void otherLoginButtonClick(IWebview iWebview, JSONArray jSONArray) throws Exception {
        AppRuntime.checkPrivacyComplianceAndPrompt(iWebview.getContext(), "Oauth-otherLoginButtonClick");
        BaseOAuthService baseOAuthService = (BaseOAuthService) getBaseModuleById(jSONArray.getString(0));
        if (baseOAuthService != null) {
            baseOAuthService.otherLoginButtonClick(iWebview, jSONArray);
        }
    }

    public void preLogin(IWebview iWebview, JSONArray jSONArray) throws Exception {
        AppRuntime.checkPrivacyComplianceAndPrompt(iWebview.getContext(), "Oauth-preLogin");
        BaseOAuthService baseOAuthService = (BaseOAuthService) getBaseModuleById(jSONArray.getString(0));
        if (baseOAuthService != null) {
            baseOAuthService.preLogin(iWebview, jSONArray);
            return;
        }
        JSUtil.execCallback(iWebview, jSONArray.getString(1), StringUtil.format(DOMException.JSON_ERROR_INFO, -3, DOMException.MSG_OAUTH_LOGOUT), JSUtil.ERROR, false);
    }
}
