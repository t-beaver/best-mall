package io.dcloud.feature.oauth;

import android.content.Context;
import io.dcloud.common.DHInterface.BaseFeature;
import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.Base64;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.NetTool;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseOAuthService extends BaseFeature.BaseModule implements IReflectAble {
    protected static final String KEY_ACCESS_TOKEN = "access_token";
    protected static final String KEY_APPID = "appid";
    protected static final String KEY_APPKEY = "appkey";
    protected static final String KEY_APSECRET = "appsecret";
    protected static final String KEY_AUTHRESULT = "authResult";
    protected static final String KEY_EMAIL = "email";
    protected static final String KEY_EXPIRES_IN = "expires_in";
    protected static final String KEY_HEADIMGURL = "headimgurl";
    protected static final String KEY_NICKNAME = "nickname";
    protected static final String KEY_OPENID = "openid";
    protected static final String KEY_REDIRECT_URI = "redirect_uri";
    protected static final String KEY_REFRESH_TOKEN = "refresh_token";
    protected static final String KEY_SCOPE = "scope";
    protected static final String KEY_STATE = "state";
    protected static final String KEY_UNIONID = "unionid";
    protected static final String KEY_UNIVERIFYINFO = "univerifyInfo";
    protected static final String KEY_USERINFO = "userInfo";
    protected static final String NULL = "null";
    protected JSONObject authResult;
    protected String mAddPhoneNumberCallbackId = null;
    protected IWebview mAddPhoneNumberWebViewImpl = null;
    protected String mAuthCallbackId = null;
    protected JSONObject mAuthOptions = null;
    protected IWebview mAuthWebview = null;
    protected String mGetUserInfoCallbackId = null;
    protected IWebview mGetUserInfoWebViewImpl = null;
    protected String mLoginCallbackId = null;
    protected JSONObject mLoginOptions = null;
    protected IWebview mLoginWebViewImpl = null;
    protected String mLogoutCallbackId = null;
    protected IWebview mLogoutWebViewImpl = null;
    protected String mOtherClickCallbackId = null;
    protected IWebview mOtherClickWebViewImpl = null;
    protected String mPreLoginCallbackId = null;
    protected IWebview mPreLoginWebViewImpl = null;
    protected boolean nativeClient = false;
    protected JSONObject univerifyInfo;
    protected JSONObject userInfo;

    public void addPhoneNumber(IWebview iWebview, JSONArray jSONArray) {
        this.mAddPhoneNumberWebViewImpl = iWebview;
        try {
            this.mAddPhoneNumberCallbackId = jSONArray.getString(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void authorize(IWebview iWebview, JSONArray jSONArray) {
        initMetaData();
        this.mAuthWebview = iWebview;
        this.mAuthCallbackId = jSONArray.optString(1, "");
        JSONObject optJSONObject = jSONArray.optJSONObject(2);
        this.mAuthOptions = optJSONObject;
        initAuthOptions(optJSONObject);
    }

    /* access modifiers changed from: protected */
    public String checkToken(String str) {
        return new String(NetTool.httpGet(str));
    }

    public void closeAuthView(IWebview iWebview, JSONArray jSONArray) {
    }

    public String decrypt(String str) throws Exception {
        return Base64.decodeString(str, true, 5);
    }

    public String encrypt(String str) throws Exception {
        return Base64.encodeString(str, true, 5);
    }

    public boolean getCheckBoxState() {
        return false;
    }

    public JSONObject getErrorJsonbject(int i, String str) {
        try {
            return new JSONObject(DOMException.toJSON(i, str));
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    /* access modifiers changed from: protected */
    public String getToken(String str) {
        byte[] httpGet = NetTool.httpGet(str);
        if (httpGet != null) {
            return new String(httpGet);
        }
        return null;
    }

    public void getUserInfo(IWebview iWebview, JSONArray jSONArray) {
        this.mGetUserInfoWebViewImpl = iWebview;
        try {
            this.mGetUserInfoCallbackId = jSONArray.getString(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public String getValue(String str) {
        try {
            String encrypt = encrypt(str);
            Context context = this.mApplicationContext;
            String bundleData = SP.getBundleData(context, "oauth_" + this.id, encrypt);
            if (bundleData == null) {
                return "{}";
            }
            return decrypt(bundleData);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

    public boolean hasFullConfigData() {
        return false;
    }

    public boolean hasGeneralError(IWebview iWebview, String str) {
        if (hasFullConfigData()) {
            return false;
        }
        IWebview iWebview2 = iWebview;
        String str2 = str;
        Deprecated_JSUtil.execCallback(iWebview2, str2, StringUtil.format(DOMException.JSON_ERROR_INFO, -7, DOMException.toString(DOMException.MSG_BUSINESS_PARAMETER_HAS_NOT)), JSUtil.ERROR, true, false);
        return true;
    }

    public void initAuthOptions(JSONObject jSONObject) {
    }

    public void initMetaData() {
    }

    public void login(IWebview iWebview, JSONArray jSONArray) {
        initMetaData();
        this.mLoginWebViewImpl = iWebview;
        try {
            this.mLoginCallbackId = jSONArray.getString(1);
            JSONObject optJSONObject = jSONArray.optJSONObject(2);
            this.mLoginOptions = optJSONObject;
            initAuthOptions(optJSONObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void logout(IWebview iWebview, JSONArray jSONArray) {
        this.mLogoutWebViewImpl = iWebview;
        try {
            this.mLogoutCallbackId = jSONArray.getString(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject makeResultJSONObject() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(KEY_AUTHRESULT, this.authResult);
            jSONObject.put(KEY_USERINFO, this.userInfo);
            jSONObject.put(KEY_UNIVERIFYINFO, this.univerifyInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    public JSONObject makeResultJson() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(KEY_AUTHRESULT, this.authResult);
            jSONObject.put(KEY_USERINFO, this.userInfo);
            jSONObject.put(KEY_UNIVERIFYINFO, this.univerifyInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    /* access modifiers changed from: protected */
    public void onAddPhoneNumberFinished(JSONObject jSONObject, boolean z) {
        JSUtil.execCallback(this.mAddPhoneNumberWebViewImpl, this.mAddPhoneNumberCallbackId, jSONObject, z ? JSUtil.OK : JSUtil.ERROR, false);
    }

    /* access modifiers changed from: protected */
    public void onGetUserInfoFinished(JSONObject jSONObject, boolean z) {
        JSUtil.execCallback(this.mGetUserInfoWebViewImpl, this.mGetUserInfoCallbackId, jSONObject, z ? JSUtil.OK : JSUtil.ERROR, false);
    }

    /* access modifiers changed from: protected */
    public void onLoginFinished(JSONObject jSONObject, boolean z) {
        JSUtil.execCallback(this.mLoginWebViewImpl, this.mLoginCallbackId, jSONObject, z ? JSUtil.OK : JSUtil.ERROR, false);
        this.mLoginWebViewImpl = null;
        this.mLoginCallbackId = null;
    }

    /* access modifiers changed from: protected */
    public void onLogoutFinished(JSONObject jSONObject, boolean z) {
        JSUtil.execCallback(this.mLogoutWebViewImpl, this.mLogoutCallbackId, jSONObject, z ? JSUtil.OK : JSUtil.ERROR, false);
    }

    /* access modifiers changed from: protected */
    public void onPreLoginFinished(JSONObject jSONObject, boolean z) {
        JSUtil.execCallback(this.mPreLoginWebViewImpl, this.mPreLoginCallbackId, jSONObject, z ? JSUtil.OK : JSUtil.ERROR, false);
    }

    public void otherLoginButtonClick(IWebview iWebview, JSONArray jSONArray) {
        this.mOtherClickWebViewImpl = iWebview;
        try {
            this.mOtherClickCallbackId = jSONArray.getString(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void preLogin(IWebview iWebview, JSONArray jSONArray) {
        initMetaData();
        this.mPreLoginWebViewImpl = iWebview;
        try {
            this.mPreLoginCallbackId = jSONArray.getString(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public String refreshToken(String str) {
        byte[] httpGet = NetTool.httpGet(str);
        if (httpGet != null) {
            return new String(httpGet);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void removeToken() {
        Context context = this.mApplicationContext;
        SP.clearBundle(context, "oauth_" + this.id);
    }

    /* access modifiers changed from: protected */
    public void removeValue(String str) {
        try {
            String encrypt = encrypt(str);
            Context context = this.mApplicationContext;
            SP.removeBundleData(context, "oauth_" + this.id, encrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void saveValue(String str, String str2) {
        try {
            String encrypt = encrypt(str);
            String encrypt2 = encrypt(str2);
            Context context = this.mApplicationContext;
            SP.setBundleData(context, "oauth_" + this.id, encrypt, encrypt2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSONObject() throws JSONException {
        Object[] objArr = new Object[5];
        objArr[0] = this.id;
        objArr[1] = this.description;
        objArr[2] = Boolean.valueOf(this.nativeClient);
        Object obj = this.authResult;
        Object obj2 = NULL;
        if (obj == null) {
            obj = obj2;
        }
        objArr[3] = obj;
        JSONObject jSONObject = this.userInfo;
        if (jSONObject != null) {
            obj2 = jSONObject;
        }
        objArr[4] = obj2;
        return new JSONObject(StringUtil.format("{id:'%s',description:'%s', nativeClient:%b, authResult:%s,userInfo:%s}", objArr));
    }

    /* access modifiers changed from: protected */
    public String getUserInfo(String str) {
        byte[] httpGet = NetTool.httpGet(str);
        if (!PdrUtil.isEmpty(httpGet)) {
            return new String(httpGet);
        }
        return null;
    }

    public JSONObject getErrorJsonbject(String str, String str2) {
        try {
            return new JSONObject(DOMException.toJSON(str, str2));
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public JSONObject getErrorJsonbject(int i, String str, int i2) {
        try {
            return new JSONObject(DOMException.toJSON(i, i + ":" + str, i2));
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }
}
