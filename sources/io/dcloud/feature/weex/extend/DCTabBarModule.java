package io.dcloud.feature.weex.extend;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.ui.component.WXBasicComponentType;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.core.ui.TabBarWebview;
import io.dcloud.common.core.ui.TabBarWebviewMgr;
import java.util.HashMap;

public class DCTabBarModule extends WXModule {
    @JSMethod
    public void append(JSONObject jSONObject, final JSCallback jSCallback) {
        TabBarWebview launchTabBar = TabBarWebviewMgr.getInstance().getLaunchTabBar();
        if (launchTabBar != null) {
            launchTabBar.append(jSONObject.getString("id"), new ICallBack() {
                public Object onCallBack(int i, Object obj) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("code", Integer.valueOf(i));
                    jSCallback.invoke(hashMap);
                    return null;
                }
            });
        }
    }

    @JSMethod(uiThread = false)
    public boolean isValid() {
        return TabBarWebviewMgr.getInstance().getLaunchTabBar() != null;
    }

    @JSMethod
    public void hideTabBar(JSONObject jSONObject) {
        TabBarWebview launchTabBar = TabBarWebviewMgr.getInstance().getLaunchTabBar();
        if (launchTabBar != null) {
            launchTabBar.hideTabBar(jSONObject);
        }
    }

    @JSMethod
    public void showTabBar(JSONObject jSONObject) {
        TabBarWebview launchTabBar = TabBarWebviewMgr.getInstance().getLaunchTabBar();
        if (launchTabBar != null) {
            launchTabBar.showTabBar(jSONObject);
        }
    }

    @JSMethod
    public void setTabBarStyle(JSONObject jSONObject) {
        TabBarWebview launchTabBar = TabBarWebviewMgr.getInstance().getLaunchTabBar();
        if (launchTabBar != null) {
            launchTabBar.setStyle(jSONObject);
        }
    }

    @JSMethod
    public void setTabBarItem(JSONObject jSONObject) {
        TabBarWebview launchTabBar = TabBarWebviewMgr.getInstance().getLaunchTabBar();
        if (launchTabBar != null) {
            launchTabBar.setItem(jSONObject);
            launchTabBar.updateMidButton((JSONObject) null);
        }
    }

    @JSMethod
    public void setTabBarBadge(JSONObject jSONObject) {
        TabBarWebview launchTabBar = TabBarWebviewMgr.getInstance().getLaunchTabBar();
        if (launchTabBar != null) {
            launchTabBar.setTabBarBadge(jSONObject);
        }
    }

    @JSMethod
    public void removeTabBarBadge(JSONObject jSONObject) {
        TabBarWebview launchTabBar = TabBarWebviewMgr.getInstance().getLaunchTabBar();
        if (launchTabBar != null && launchTabBar.isVisible()) {
            launchTabBar.removeTabBarBadge(jSONObject);
        }
    }

    @JSMethod
    public void showTabBarRedDot(JSONObject jSONObject) {
        TabBarWebview launchTabBar = TabBarWebviewMgr.getInstance().getLaunchTabBar();
        if (launchTabBar != null && launchTabBar.isVisible()) {
            launchTabBar.showTabBarRedDot(jSONObject);
        }
    }

    @JSMethod
    public void hideTabBarRedDot(JSONObject jSONObject) {
        TabBarWebview launchTabBar = TabBarWebviewMgr.getInstance().getLaunchTabBar();
        if (launchTabBar != null && launchTabBar.isVisible()) {
            launchTabBar.hideTabBarRedDot(jSONObject);
        }
    }

    @JSMethod
    public void setMask(JSONObject jSONObject) {
        TabBarWebview launchTabBar = TabBarWebviewMgr.getInstance().getLaunchTabBar();
        if (launchTabBar != null) {
            launchTabBar.setMask(jSONObject);
        }
    }

    @JSMethod(uiThread = false)
    public boolean isTabBarVisible() {
        TabBarWebview launchTabBar = TabBarWebviewMgr.getInstance().getLaunchTabBar();
        return launchTabBar != null && launchTabBar.isVisible();
    }

    @JSMethod(uiThread = false)
    public String getTabBarHeight() {
        return TabBarWebviewMgr.getInstance().getLaunchTabBar().getTabBarHeight();
    }

    @JSMethod
    public void switchSelect(JSONObject jSONObject) {
        TabBarWebview launchTabBar = TabBarWebviewMgr.getInstance().getLaunchTabBar();
        if (launchTabBar != null) {
            launchTabBar.switchSelect(jSONObject.getIntValue("index"));
        }
    }

    @JSMethod
    public void setTabBarItems(JSONObject jSONObject) {
        JSONObject jSONObject2;
        JSONArray jSONArray;
        TabBarWebview launchTabBar = TabBarWebviewMgr.getInstance().getLaunchTabBar();
        if (launchTabBar != null) {
            if (jSONObject.containsKey(WXBasicComponentType.LIST) && (jSONArray = jSONObject.getJSONArray(WXBasicComponentType.LIST)) != null) {
                int size = jSONArray.size();
                for (int i = 0; i < size; i++) {
                    JSONObject jSONObject3 = (JSONObject) jSONArray.get(i);
                    if (jSONObject3 != null) {
                        jSONObject3.put("index", (Object) Integer.valueOf(i));
                        launchTabBar.setItem(jSONObject3);
                    }
                }
            }
            if (!jSONObject.containsKey("midButton") || (jSONObject2 = jSONObject.getJSONObject("midButton")) == null) {
                launchTabBar.updateMidButton((JSONObject) null);
            } else {
                launchTabBar.updateMidButton(jSONObject2);
            }
        }
    }

    @JSMethod
    public void onClick(final JSCallback jSCallback) {
        TabBarWebview launchTabBar = TabBarWebviewMgr.getInstance().getLaunchTabBar();
        if (launchTabBar != null) {
            launchTabBar.setClickCallBack(new ICallBack() {
                public Object onCallBack(int i, Object obj) {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("index", (Object) Integer.valueOf(i));
                    jSCallback.invokeAndKeepAlive(jSONObject);
                    return null;
                }
            });
        }
    }

    @JSMethod
    public void onDoubleClick(final JSCallback jSCallback) {
        TabBarWebview launchTabBar = TabBarWebviewMgr.getInstance().getLaunchTabBar();
        if (launchTabBar != null) {
            launchTabBar.setDoubleClickCallBack(new ICallBack() {
                public Object onCallBack(int i, Object obj) {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("index", (Object) Integer.valueOf(i));
                    jSCallback.invokeAndKeepAlive(jSONObject);
                    return null;
                }
            });
        }
    }

    @JSMethod
    public void onMidButtonClick(final JSCallback jSCallback) {
        TabBarWebview launchTabBar = TabBarWebviewMgr.getInstance().getLaunchTabBar();
        if (launchTabBar != null) {
            launchTabBar.setMidButtonClickCallBack(new ICallBack() {
                public Object onCallBack(int i, Object obj) {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("index", (Object) Integer.valueOf(i));
                    jSCallback.invokeAndKeepAlive(jSONObject);
                    return null;
                }
            });
        }
    }

    @JSMethod
    public void onMaskClick(final JSCallback jSCallback) {
        TabBarWebview launchTabBar = TabBarWebviewMgr.getInstance().getLaunchTabBar();
        if (launchTabBar != null) {
            launchTabBar.setMaskButtonClickCallBack(new ICallBack() {
                public Object onCallBack(int i, Object obj) {
                    jSCallback.invokeAndKeepAlive(new JSONObject());
                    return null;
                }
            });
        }
    }
}
