package io.dcloud.common.adapter.util;

import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import com.taobao.weex.performance.WXInstanceApm;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;
import java.util.HashMap;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

public class ViewOptions extends ViewRect {
    public static final int BG_NONE = -1;
    public String animationAlphaBackground;
    public String backButtonAutoControl;
    public int background;
    public int coverage;
    public HashMap<String, DragBean> dragData;
    public boolean dragH5NeedTouchEvent;
    public String errorPage;
    public String historyBack;
    public boolean isAnimationOptimization;
    public Boolean isTabItem;
    public boolean isUniH5;
    public boolean isUserSelect;
    public boolean mBounce;
    public String mCacheMode;
    public JSONObject mDebugRefresh;
    public boolean mDisablePlus;
    public String mGeoInject;
    public String mInjection;
    public String mPlusrequire;
    public JSONObject mProgressJson;
    public JSONObject mPullToRefresh;
    private String mScrollIndicator;
    public JSONArray mSubNViews;
    public Object mTag;
    public JSONObject mUniNViewJson;
    public JSONObject mUniPageUrl;
    public boolean mUseHardwave;
    public String mVideoFullscree;
    public int maskColor;
    public String name;
    public float opacity;
    public String popGesture;
    public boolean scalable;
    public String softinputMode;
    public String strBackground;
    public String strTabBG;
    public JSONObject titleNView;
    public JSONObject transform;
    public JSONObject transition;
    public boolean webviewBGTransparent;

    public ViewOptions() {
        this.mUseHardwave = true;
        this.scalable = false;
        this.mInjection = AbsoluteConst.TRUE;
        this.mPlusrequire = "normal";
        this.mDisablePlus = false;
        this.mScrollIndicator = "all";
        this.opacity = -1.0f;
        this.background = -1;
        this.maskColor = -1;
        this.strBackground = null;
        this.webviewBGTransparent = false;
        this.strTabBG = null;
        this.errorPage = null;
        this.mBounce = false;
        this.mCacheMode = "default";
        this.mVideoFullscree = "auto";
        this.popGesture = "none";
        this.historyBack = "none";
        this.mGeoInject = "none";
        this.dragH5NeedTouchEvent = false;
        this.backButtonAutoControl = "none";
        this.isAnimationOptimization = false;
        this.isUserSelect = true;
        this.softinputMode = "adjustResize";
        this.mUniNViewJson = null;
        this.mProgressJson = null;
        this.mDebugRefresh = null;
        this.mUniPageUrl = null;
        this.isTabItem = Boolean.FALSE;
        this.isUniH5 = false;
        this.mUseHardwave = MobilePhoneModel.checkPhoneBanAcceleration(Build.BRAND);
    }

    public static ViewOptions createViewOptionsData(ViewOptions viewOptions, ViewRect viewRect) {
        return createViewOptionsData(viewOptions, (ViewRect) null, viewRect);
    }

    public String getScrollIndicator() {
        return this.mScrollIndicator;
    }

    public boolean hasBackground() {
        return this.background != -1 || (!PdrUtil.isEmpty(this.strBackground) && !this.strBackground.equals("transparent"));
    }

    public boolean hasMask() {
        return this.maskColor != -1;
    }

    public boolean hasTransparentValue() {
        if (!isTransparent() && !PdrUtil.checkAlphaTransparent(this.background)) {
            float f = this.opacity;
            if (f < 0.0f || f >= 1.0f) {
                return false;
            }
        }
        return true;
    }

    public boolean isTabHasBg() {
        return this.isTabItem.booleanValue() && !PdrUtil.isEmpty(this.strTabBG);
    }

    public boolean isTransparent() {
        return PdrUtil.isEquals("transparent", this.strBackground);
    }

    public void setBackButtonAutoControl(JSONObject jSONObject) {
        if (jSONObject != null && jSONObject.has("backButtonAutoControl")) {
            String string = JSONUtil.getString(jSONObject, "backButtonAutoControl");
            if ("none".equals(string) || "hide".equals(string) || AbsoluteConst.EVENTS_CLOSE.equals(string) || "quit".equals(string)) {
                this.backButtonAutoControl = string;
            }
        }
    }

    public void setDragData(JSONObject jSONObject, JSONObject jSONObject2, IFrameView iFrameView, IFrameView iFrameView2, String str, View view) {
        try {
            if (this.dragData == null) {
                this.dragData = new HashMap<>();
            }
            DragBean dragBean = new DragBean();
            dragBean.dragCurrentViewOp = jSONObject;
            dragBean.dragBindViewOp = jSONObject2;
            dragBean.dragBindWebView = iFrameView;
            dragBean.dragCallBackWebView = iFrameView2;
            dragBean.dragCbId = str;
            dragBean.nativeView = view;
            if (jSONObject.has("direction")) {
                String string = jSONObject.getString("direction");
                if (!TextUtils.isEmpty(string)) {
                    this.dragData.put(string.toLowerCase(Locale.ENGLISH), dragBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTitleNView(JSONObject jSONObject, IWebview iWebview) {
        if (jSONObject != null) {
            if ("transparent".equals(jSONObject.optString("type"))) {
                String str = null;
                if (jSONObject.has("titleColor")) {
                    str = jSONObject.optString("titleColor");
                } else if (jSONObject.has("titlecolor")) {
                    str = jSONObject.optString("titlecolor");
                }
                if (!TextUtils.isEmpty(str)) {
                    try {
                        int parseColor = Color.parseColor(str);
                        String hexString = Integer.toHexString(0);
                        if (1 == hexString.length()) {
                            hexString = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT + hexString.toUpperCase();
                        }
                        String hexString2 = Integer.toHexString(Color.red(parseColor));
                        if (1 == hexString2.length()) {
                            hexString2 = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT + hexString2.toUpperCase();
                        }
                        String hexString3 = Integer.toHexString(Color.green(parseColor));
                        if (1 == hexString3.length()) {
                            hexString3 = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT + hexString3.toUpperCase();
                        }
                        String hexString4 = Integer.toHexString(Color.blue(parseColor));
                        if (1 == hexString4.length()) {
                            hexString4 = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT + hexString4.toUpperCase();
                        }
                        String str2 = "#" + hexString + hexString2 + hexString3 + hexString4;
                        if (jSONObject.has("titleColor")) {
                            jSONObject.put("titleColor", str2);
                        } else if (jSONObject.has("titlecolor")) {
                            jSONObject.put("titlecolor", str2);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            this.coverage = PdrUtil.convertToScreenInt("136px", PlatformUtil.SCREEN_WIDTH(iWebview.getContext()), 0, iWebview.getScale());
            this.coverage = PdrUtil.convertToScreenInt(jSONObject.optString("coverage"), PlatformUtil.SCREEN_WIDTH(iWebview.getContext()), this.coverage, iWebview.getScale());
            this.titleNView = jSONObject;
        }
    }

    public void updateViewData(ViewRect viewRect) {
        super.updateViewData(viewRect);
    }

    public static ViewOptions createViewOptionsData(ViewOptions viewOptions, ViewRect viewRect, ViewRect viewRect2) {
        if (viewOptions == null) {
            return null;
        }
        ViewOptions viewOptions2 = new ViewOptions();
        if (viewRect != null) {
            viewOptions2.setFrameParentViewRect(viewRect);
        }
        viewOptions2.mWebviewScale = viewOptions.mWebviewScale;
        viewOptions2.setParentViewRect(viewRect2);
        viewOptions2.updateViewData(viewOptions.mJsonViewOption);
        return viewOptions2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0075  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0083  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0097 A[SYNTHETIC, Splitter:B:33:0x0097] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00a9  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00be A[SYNTHETIC, Splitter:B:45:0x00be] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00db  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x016e  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x017e  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0193  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x01cf  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x01f5  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x01f8  */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x020e  */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x021b  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x0237  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x024b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean updateViewData(org.json.JSONObject r7) {
        /*
            r6 = this;
            boolean r0 = super.updateViewData((org.json.JSONObject) r7)
            if (r7 == 0) goto L_0x0267
            java.lang.String r1 = "isTab"
            boolean r2 = r7.isNull(r1)
            r3 = 0
            if (r2 != 0) goto L_0x0019
            boolean r1 = r7.optBoolean(r1, r3)
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)
            r6.isTabItem = r1
        L_0x0019:
            java.lang.String r1 = "isUniH5"
            boolean r2 = r7.isNull(r1)
            if (r2 != 0) goto L_0x0027
            boolean r1 = r7.optBoolean(r1, r3)
            r6.isUniH5 = r1
        L_0x0027:
            java.lang.String r1 = "scrollIndicator"
            boolean r2 = io.dcloud.common.util.JSONUtil.isNull(r7, r1)
            if (r2 != 0) goto L_0x0035
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r7, (java.lang.String) r1)
            r6.mScrollIndicator = r1
        L_0x0035:
            java.lang.String r1 = "background"
            boolean r2 = r7.isNull(r1)
            r4 = 0
            if (r2 != 0) goto L_0x006c
            java.lang.String r2 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r7, (java.lang.String) r1)     // Catch:{ Exception -> 0x0068 }
            java.util.Locale r5 = java.util.Locale.ENGLISH     // Catch:{ Exception -> 0x0068 }
            java.lang.String r2 = r2.toLowerCase(r5)     // Catch:{ Exception -> 0x0068 }
            java.lang.Boolean r5 = r6.isTabItem     // Catch:{ Exception -> 0x0065 }
            boolean r5 = r5.booleanValue()     // Catch:{ Exception -> 0x0065 }
            if (r5 == 0) goto L_0x005b
            r7.remove(r1)     // Catch:{ Exception -> 0x0065 }
            r6.strTabBG = r2     // Catch:{ Exception -> 0x0065 }
            r6.strBackground = r4     // Catch:{ Exception -> 0x0065 }
            r1 = -1
            r6.background = r1     // Catch:{ Exception -> 0x0065 }
            goto L_0x0063
        L_0x005b:
            r6.strBackground = r2     // Catch:{ Exception -> 0x0065 }
            int r1 = io.dcloud.common.util.PdrUtil.stringToColor(r2)     // Catch:{ Exception -> 0x0065 }
            r6.background = r1     // Catch:{ Exception -> 0x0065 }
        L_0x0063:
            r4 = r2
            goto L_0x006c
        L_0x0065:
            r1 = move-exception
            r4 = r2
            goto L_0x0069
        L_0x0068:
            r1 = move-exception
        L_0x0069:
            r1.printStackTrace()
        L_0x006c:
            java.lang.String r1 = "webviewBGTransparent"
            boolean r2 = r7.isNull(r1)
            if (r2 != 0) goto L_0x007b
            boolean r1 = io.dcloud.common.util.JSONUtil.getBoolean(r7, r1)
            r6.webviewBGTransparent = r1
        L_0x007b:
            java.lang.String r1 = "tabBGColor"
            boolean r2 = r7.isNull(r1)
            if (r2 != 0) goto L_0x008f
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r7, (java.lang.String) r1)
            java.util.Locale r2 = java.util.Locale.ENGLISH
            java.lang.String r4 = r1.toLowerCase(r2)
            r6.strTabBG = r4
        L_0x008f:
            java.lang.String r1 = "animationAlphaBGColor"
            boolean r2 = r7.isNull(r1)
            if (r2 != 0) goto L_0x00a9
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r7, (java.lang.String) r1)     // Catch:{ Exception -> 0x00a4 }
            java.util.Locale r2 = java.util.Locale.ENGLISH     // Catch:{ Exception -> 0x00a4 }
            java.lang.String r1 = r1.toLowerCase(r2)     // Catch:{ Exception -> 0x00a4 }
            r6.animationAlphaBackground = r1     // Catch:{ Exception -> 0x00a4 }
            goto L_0x00b6
        L_0x00a4:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00b6
        L_0x00a9:
            boolean r1 = android.text.TextUtils.isEmpty(r4)
            if (r1 != 0) goto L_0x00b6
            r6.animationAlphaBackground = r4     // Catch:{ Exception -> 0x00b2 }
            goto L_0x00b6
        L_0x00b2:
            r1 = move-exception
            r1.printStackTrace()
        L_0x00b6:
            java.lang.String r1 = "mask"
            boolean r2 = r7.isNull(r1)
            if (r2 != 0) goto L_0x00d3
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r7, (java.lang.String) r1)     // Catch:{ Exception -> 0x00cf }
            java.util.Locale r2 = java.util.Locale.ENGLISH     // Catch:{ Exception -> 0x00cf }
            java.lang.String r1 = r1.toLowerCase(r2)     // Catch:{ Exception -> 0x00cf }
            int r1 = io.dcloud.common.util.PdrUtil.stringToColor(r1)     // Catch:{ Exception -> 0x00cf }
            r6.maskColor = r1     // Catch:{ Exception -> 0x00cf }
            goto L_0x00d3
        L_0x00cf:
            r1 = move-exception
            r1.printStackTrace()
        L_0x00d3:
            java.lang.String r1 = "cachemode"
            boolean r2 = r7.isNull(r1)
            if (r2 != 0) goto L_0x00e1
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r7, (java.lang.String) r1)
            r6.mCacheMode = r1
        L_0x00e1:
            java.lang.String r1 = "hardwareAccelerated"
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r7, (java.lang.String) r1)
            boolean r2 = r6.mUseHardwave
            boolean r1 = io.dcloud.common.util.PdrUtil.parseBoolean(r1, r2, r3)
            r6.mUseHardwave = r1
            java.lang.String r1 = "opacity"
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r7, (java.lang.String) r1)
            float r2 = r6.opacity
            float r1 = io.dcloud.common.util.PdrUtil.parseFloat(r1, r2)
            r6.opacity = r1
            java.lang.String r1 = "scalable"
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r7, (java.lang.String) r1)
            boolean r2 = r6.scalable
            boolean r1 = io.dcloud.common.util.PdrUtil.parseBoolean(r1, r2, r3)
            r6.scalable = r1
            java.lang.String r1 = "transition"
            org.json.JSONObject r1 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r7, (java.lang.String) r1)
            r6.transition = r1
            java.lang.String r1 = "transform"
            org.json.JSONObject r1 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r7, (java.lang.String) r1)
            r6.transform = r1
            java.lang.String r1 = "errorPage"
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r7, (java.lang.String) r1)
            r6.errorPage = r1
            java.lang.String r1 = "injection"
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r7, (java.lang.String) r1)
            r6.mInjection = r1
            java.lang.String r1 = r6.mPlusrequire
            java.lang.String r2 = "plusrequire"
            java.lang.String r1 = r7.optString(r2, r1)
            r6.mPlusrequire = r1
            boolean r1 = r6.mDisablePlus
            java.lang.String r2 = "disablePlus"
            boolean r1 = r7.optBoolean(r2, r1)
            r6.mDisablePlus = r1
            java.lang.String r1 = r6.popGesture
            java.lang.String r2 = "popGesture"
            java.lang.String r1 = r7.optString(r2, r1)
            r6.popGesture = r1
            java.lang.String r1 = r6.historyBack
            java.lang.String r2 = "historyBack"
            java.lang.String r1 = r7.optString(r2, r1)
            r6.historyBack = r1
            java.lang.String r1 = "userSelect"
            r2 = 1
            boolean r1 = r7.optBoolean(r1, r2)
            r6.isUserSelect = r1
            java.lang.String r1 = r6.softinputMode
            java.lang.String r4 = "softinputMode"
            java.lang.String r1 = r7.optString(r4, r1)
            r6.softinputMode = r1
            java.lang.String r1 = "uniNView"
            boolean r4 = r7.has(r1)
            if (r4 == 0) goto L_0x0174
            org.json.JSONObject r1 = r7.optJSONObject(r1)
            r6.mUniNViewJson = r1
        L_0x0174:
            java.lang.String r1 = "replacewebapi"
            boolean r4 = r7.has(r1)
            java.lang.String r5 = "geolocation"
            if (r4 == 0) goto L_0x0193
            org.json.JSONObject r1 = r7.optJSONObject(r1)
            if (r1 == 0) goto L_0x01a1
            boolean r4 = r1.has(r5)
            if (r4 == 0) goto L_0x01a1
            java.lang.String r4 = r6.mGeoInject
            java.lang.String r1 = r1.optString(r5, r4)
            r6.mGeoInject = r1
            goto L_0x01a1
        L_0x0193:
            boolean r1 = r7.has(r5)
            if (r1 == 0) goto L_0x01a1
            java.lang.String r1 = r6.mGeoInject
            java.lang.String r1 = r7.optString(r5, r1)
            r6.mGeoInject = r1
        L_0x01a1:
            java.lang.String r1 = "progress"
            org.json.JSONObject r1 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r7, (java.lang.String) r1)
            r6.mProgressJson = r1
            java.lang.String r1 = "debugRefresh"
            org.json.JSONObject r1 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r7, (java.lang.String) r1)
            r6.mDebugRefresh = r1
            if (r1 == 0) goto L_0x01c7
            java.lang.String r4 = "arguments"
            boolean r1 = r1.has(r4)
            if (r1 == 0) goto L_0x01c7
            org.json.JSONObject r1 = r6.mDebugRefresh
            java.lang.String r1 = r1.optString(r4)
            org.json.JSONObject r1 = io.dcloud.common.util.JSONUtil.createJSONObject(r1)
            r6.mUniPageUrl = r1
        L_0x01c7:
            java.lang.String r1 = "uniPageUrl"
            boolean r4 = r7.has(r1)
            if (r4 == 0) goto L_0x01d5
            org.json.JSONObject r1 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r7, (java.lang.String) r1)
            r6.mUniPageUrl = r1
        L_0x01d5:
            java.lang.String r1 = "bounce"
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r7, (java.lang.String) r1)
            java.lang.String r4 = "vertical"
            boolean r4 = r4.equalsIgnoreCase(r1)
            if (r4 != 0) goto L_0x01f8
            java.lang.String r4 = "horizontal"
            boolean r4 = r4.equalsIgnoreCase(r1)
            if (r4 != 0) goto L_0x01f8
            java.lang.String r4 = "all"
            boolean r1 = r4.equalsIgnoreCase(r1)
            if (r1 == 0) goto L_0x01f5
            goto L_0x01f8
        L_0x01f5:
            r6.mBounce = r3
            goto L_0x01fa
        L_0x01f8:
            r6.mBounce = r2
        L_0x01fa:
            java.lang.String r1 = "videoFullscreen"
            java.lang.String r1 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r7, (java.lang.String) r1)
            r6.mVideoFullscree = r1
            r6.setBackButtonAutoControl(r7)
            java.lang.String r1 = "titleNView"
            boolean r3 = r7.has(r1)
            if (r3 == 0) goto L_0x021b
            org.json.JSONObject r3 = r6.titleNView
            org.json.JSONObject r1 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r7, (java.lang.String) r1)
            org.json.JSONObject r1 = io.dcloud.common.util.JSONUtil.combinJSONObject(r3, r1)
            r6.titleNView = r1
            goto L_0x022f
        L_0x021b:
            java.lang.String r1 = "navigationbar"
            boolean r3 = r7.has(r1)
            if (r3 == 0) goto L_0x022f
            org.json.JSONObject r3 = r6.titleNView
            org.json.JSONObject r1 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r7, (java.lang.String) r1)
            org.json.JSONObject r1 = io.dcloud.common.util.JSONUtil.combinJSONObject(r3, r1)
            r6.titleNView = r1
        L_0x022f:
            java.lang.String r1 = "pullToRefresh"
            boolean r3 = r7.has(r1)
            if (r3 == 0) goto L_0x0243
            org.json.JSONObject r3 = r6.mPullToRefresh
            org.json.JSONObject r1 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r7, (java.lang.String) r1)
            org.json.JSONObject r1 = io.dcloud.common.util.JSONUtil.combinJSONObject(r3, r1)
            r6.mPullToRefresh = r1
        L_0x0243:
            java.lang.String r1 = "subNViews"
            boolean r3 = r7.has(r1)
            if (r3 == 0) goto L_0x0251
            org.json.JSONArray r1 = io.dcloud.common.util.JSONUtil.getJSONArray((org.json.JSONObject) r7, (java.lang.String) r1)
            r6.mSubNViews = r1
        L_0x0251:
            java.lang.String r1 = "animationOptimization"
            boolean r3 = r7.has(r1)
            if (r3 == 0) goto L_0x0267
            java.lang.String r7 = r7.optString(r1)
            java.lang.String r1 = "auto"
            boolean r7 = r1.equals(r7)
            if (r7 == 0) goto L_0x0267
            r6.isAnimationOptimization = r2
        L_0x0267:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.util.ViewOptions.updateViewData(org.json.JSONObject):boolean");
    }
}
