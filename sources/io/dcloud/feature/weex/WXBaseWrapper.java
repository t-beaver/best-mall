package io.dcloud.feature.weex;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.ViewGroup;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import io.dcloud.common.DHInterface.IKeyHandler;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IUniNView;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.weex.adapter.widget.refresh.DCWeexBaseRefreshLayout;
import io.dcloud.feature.weex.adapter.widget.refresh.WeexDcRefreshLayout;
import java.util.Map;
import org.json.JSONObject;

public class WXBaseWrapper extends WeexDcRefreshLayout implements IUniNView {
    protected static int DE_INDEX = -1;
    protected String mPath = null;
    protected String mSrcPath = null;
    protected WXAnalyzerDelegate mWXAnaly;
    protected WXSDKInstance mWXSDKInstance;
    protected IWebview mWebview;
    protected String mWxId;

    public String evalJs(String str, int i) {
        return null;
    }

    public String getType() {
        return null;
    }

    public void loadTemplate(JSONObject jSONObject) {
    }

    public ViewGroup obtainMainView() {
        return this;
    }

    public void onReady() {
    }

    /* access modifiers changed from: protected */
    public void onRefresh() {
    }

    public void reload() {
    }

    public void titleNViewRefresh() {
    }

    public WXBaseWrapper(Context context) {
        super(context);
        setEnabled(false);
    }

    public void initRefresh(JSONObject jSONObject) {
        if (jSONObject != null) {
            boolean parseBoolean = Boolean.parseBoolean(JSONUtil.getString(jSONObject, AbsoluteConst.PULL_REFRESH_SUPPORT));
            String str = "default";
            if (jSONObject != null) {
                str = jSONObject.optString("style", str);
            }
            if (!parseBoolean || !str.equals("circle")) {
                setOnRefreshListener((DCWeexBaseRefreshLayout.OnRefreshListener) null);
                setEnabled(false);
                return;
            }
            setEnabled(true);
            setOnRefreshListener(new DCWeexBaseRefreshLayout.OnRefreshListener() {
                public void onRefresh() {
                    WXBaseWrapper.this.onRefresh();
                }
            });
            parseData(jSONObject);
        }
    }

    private void parseData(JSONObject jSONObject) {
        IWebview iWebview = this.mWebview;
        if (iWebview != null) {
            ViewOptions obtainFrameOptions = ((AdaFrameView) iWebview.obtainFrameView()).obtainFrameOptions();
            String optString = jSONObject.optString("offset");
            int convertToScreenInt = !TextUtils.isEmpty(optString) ? PdrUtil.convertToScreenInt(optString, obtainFrameOptions.height, 0, this.mWebview.getScale()) : 0;
            String optString2 = jSONObject.optString("height");
            int i = (int) this.mTotalDragDistance;
            int convertToScreenInt2 = !TextUtils.isEmpty(optString2) ? PdrUtil.convertToScreenInt(optString2, obtainFrameOptions.height, i, this.mWebview.getScale()) : i;
            String optString3 = jSONObject.optString(AbsoluteConst.PULL_REFRESH_RANGE);
            int i2 = (int) this.mSpinnerFinalOffset;
            if (!TextUtils.isEmpty(optString3)) {
                i2 = PdrUtil.convertToScreenInt(optString3, obtainFrameOptions.height, i2, this.mWebview.getScale());
            }
            int i3 = i2 + this.mOriginalOffsetTop;
            String optString4 = jSONObject.optString("color");
            int parseColor = Color.parseColor("#2BD009");
            if (!TextUtils.isEmpty(optString4) && optString4.startsWith("#")) {
                try {
                    parseColor = Color.parseColor(optString4);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            setColorSchemeColors(parseColor);
            setProgressViewOffset(false, this.mOriginalOffsetTop, i3, convertToScreenInt2, convertToScreenInt);
        }
    }

    public void beginPullRefresh() {
        beginRefresh();
    }

    public void endPullToRefresh() {
        setRefreshing(false);
    }

    public void onDestroy() {
        destroy();
    }

    public void destroy() {
        setEnabled(false);
        recoveryInstance();
    }

    public void recoveryInstance() {
        WXSDKInstance wXSDKInstance = this.mWXSDKInstance;
        if (wXSDKInstance != null) {
            wXSDKInstance.registerRenderListener((IWXRenderListener) null);
            this.mWXSDKInstance.onActivityDestroy();
            WXAnalyzerDelegate wXAnalyzerDelegate = this.mWXAnaly;
            if (wXAnalyzerDelegate != null) {
                wXAnalyzerDelegate.onDestroy();
                this.mWXAnaly = null;
            }
            this.mWXSDKInstance = null;
            clearTarget();
        }
    }

    public boolean fireGlobalEvent(String str, Map<String, Object> map) {
        WXSDKInstance wXSDKInstance = this.mWXSDKInstance;
        if (wXSDKInstance == null) {
            return false;
        }
        wXSDKInstance.fireGlobalEventCallback(str, map);
        return true;
    }

    public void onActivityResume() {
        WXSDKInstance wXSDKInstance = this.mWXSDKInstance;
        if (wXSDKInstance != null) {
            wXSDKInstance.onActivityResume();
            WXAnalyzerDelegate wXAnalyzerDelegate = this.mWXAnaly;
            if (wXAnalyzerDelegate != null) {
                wXAnalyzerDelegate.onResume();
            }
        }
    }

    public void onActivityPause() {
        WXSDKInstance wXSDKInstance = this.mWXSDKInstance;
        if (wXSDKInstance != null) {
            wXSDKInstance.onActivityPause();
            WXAnalyzerDelegate wXAnalyzerDelegate = this.mWXAnaly;
            if (wXAnalyzerDelegate != null) {
                wXAnalyzerDelegate.onPause();
            }
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        WXSDKInstance wXSDKInstance = this.mWXSDKInstance;
        if (wXSDKInstance != null) {
            wXSDKInstance.onActivityResult(i, i2, intent);
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        WXSDKInstance wXSDKInstance = this.mWXSDKInstance;
        if (wXSDKInstance != null) {
            wXSDKInstance.onRequestPermissionsResult(i, strArr, iArr);
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        IWebview iWebview;
        boolean onKeyDown = super.onKeyDown(i, keyEvent);
        if (onKeyDown && (iWebview = this.mWebview) != null && (iWebview.getActivity() instanceof IKeyHandler)) {
            ((IKeyHandler) this.mWebview.getActivity()).onKeyEventExecute(ISysEventListener.SysEventType.onKeyDown, i, keyEvent);
        }
        return onKeyDown;
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        IWebview iWebview;
        boolean onKeyUp = super.onKeyUp(i, keyEvent);
        if (onKeyUp && (iWebview = this.mWebview) != null && (iWebview.getActivity() instanceof IKeyHandler)) {
            ((IKeyHandler) this.mWebview.getActivity()).onKeyEventExecute(ISysEventListener.SysEventType.onKeyUp, i, keyEvent);
        }
        return onKeyUp;
    }
}
