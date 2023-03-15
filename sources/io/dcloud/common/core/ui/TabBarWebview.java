package io.dcloud.common.core.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dcloud.android.widget.TabView;
import com.taobao.weex.ui.component.WXBasicComponentType;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.ui.AdaWebview;
import io.dcloud.common.adapter.ui.ReceiveJSValue;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONException;

public class TabBarWebview extends AdaWebview {
    private boolean isVisible;
    private IApp mApp;
    private JSONArray mChildJson;
    private ArrayList<String> mPagePaths;
    ViewGroup mRoot;
    float mScale = 3.0f;
    private int mSelectIndex = 0;
    /* access modifiers changed from: private */
    public TabView mTabBar;
    private JSONObject mTabBarJson;
    private ArrayList<b> mTabItems;
    private FrameLayout mTabLayout;
    private l mWindowMgr;

    class a implements Runnable {
        final /* synthetic */ b a;

        a(b bVar) {
            this.a = bVar;
        }

        public void run() {
            this.a.resize();
        }
    }

    class b implements Animation.AnimationListener {
        b() {
        }

        public void onAnimationEnd(Animation animation) {
            TabBarWebview.this.mTabBar.setVisibility(8);
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    class c implements Runnable {
        c() {
        }

        public void run() {
            TabBarWebview.this.mTabBar.setVisibility(8);
        }
    }

    class d implements Animation.AnimationListener {
        d() {
        }

        public void onAnimationEnd(Animation animation) {
            TabBarWebview tabBarWebview = TabBarWebview.this;
            tabBarWebview.setTabItemsBottomMargin(tabBarWebview.mTabBar.getTabHeight());
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    class e implements Runnable {
        final /* synthetic */ b a;

        e(b bVar) {
            this.a = bVar;
        }

        public void run() {
            ViewGroup viewGroup = (ViewGroup) this.a.obtainWebviewParent().obtainMainView();
            if (!(viewGroup == null || viewGroup.getHeight() == this.a.obtainMainView().getHeight())) {
                AdaFrameItem.LayoutParamsUtil.setViewLayoutParams(viewGroup, 0, 0, -1, -1);
            }
            this.a.resize();
        }
    }

    public TabBarWebview(Context context, IApp iApp, l lVar, c cVar, org.json.JSONObject jSONObject) {
        super(context);
        this.mFrameView = cVar;
        this.mWindowMgr = lVar;
        this.mTabItems = new ArrayList<>();
        this.mPagePaths = new ArrayList<>();
        initWebviewUUID("TabBar");
        initPagePaths(JSON.parseObject(jSONObject.toString()));
        this.mScale = context.getResources().getDisplayMetrics().density;
        this.mApp = iApp;
        JSONObject parseObject = JSON.parseObject(jSONObject.toString());
        this.mTabBarJson = parseObject;
        this.mChildJson = parseObject.getJSONArray("child");
        this.mTabLayout = new FrameLayout(context);
        if (this.mTabBarJson.containsKey("selected")) {
            String string = this.mTabBarJson.getString("selected");
            if (!TextUtils.isEmpty(string)) {
                this.mSelectIndex = Integer.valueOf(string).intValue();
            }
        }
        this.mRoot = (FrameLayout) cVar.obtainMainView();
        TabView tabView = new TabView(context, this.mRoot, this.mTabBarJson, this.mScale, iApp);
        this.mTabBar = tabView;
        setMainView(tabView);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        layoutParams.bottomMargin = this.mTabBar.getTabHeight();
        this.mRoot.addView(this.mTabLayout, layoutParams);
        TabBarWebviewMgr.getInstance().setLancheTabBar(this);
        this.isVisible = true;
        cVar.addFrameItem((AdaFrameItem) this, new ViewGroup.LayoutParams(-1, -1));
    }

    private void initPagePaths(JSONObject jSONObject) {
        if (jSONObject.containsKey(WXBasicComponentType.LIST)) {
            JSONArray jSONArray = jSONObject.getJSONArray(WXBasicComponentType.LIST);
            for (int i = 0; i < jSONArray.size(); i++) {
                String string = jSONArray.getJSONObject(i).getString("pagePath");
                if (!TextUtils.isEmpty(string)) {
                    if (string.startsWith("/")) {
                        string = string.substring(1);
                    }
                    this.mPagePaths.add(string);
                }
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v1, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void append(java.lang.String r11, io.dcloud.common.DHInterface.ICallBack r12) {
        /*
            r10 = this;
            boolean r0 = android.text.TextUtils.isEmpty(r11)
            r1 = -1
            r2 = 0
            if (r0 != 0) goto L_0x0050
            io.dcloud.common.core.ui.l r0 = r10.mWindowMgr
            if (r0 == 0) goto L_0x0050
            io.dcloud.common.DHInterface.IMgr$MgrType r3 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r4 = 4
            java.lang.Object[] r4 = new java.lang.Object[r4]
            io.dcloud.common.DHInterface.IApp r5 = r10.mApp
            r6 = 0
            r4[r6] = r5
            java.lang.String r7 = "ui"
            r8 = 1
            r4[r8] = r7
            java.lang.String r7 = "findWebview"
            r9 = 2
            r4[r9] = r7
            java.lang.String[] r7 = new java.lang.String[r9]
            java.lang.String r5 = r5.obtainAppId()
            r7[r6] = r5
            r7[r8] = r11
            r11 = 3
            r4[r11] = r7
            r11 = 10
            java.lang.Object r11 = r0.processEvent(r3, r11, r4)
            if (r11 == 0) goto L_0x004c
            boolean r0 = r11 instanceof io.dcloud.common.DHInterface.IWebview
            if (r0 == 0) goto L_0x004c
            io.dcloud.common.DHInterface.IWebview r11 = (io.dcloud.common.DHInterface.IWebview) r11
            io.dcloud.common.DHInterface.IFrameView r11 = r11.obtainFrameView()
            boolean r0 = r11 instanceof io.dcloud.common.core.ui.b
            if (r0 == 0) goto L_0x0053
            io.dcloud.common.core.ui.b r11 = (io.dcloud.common.core.ui.b) r11
            r10.append(r11)
            r12.onCallBack(r6, r2)
            goto L_0x0053
        L_0x004c:
            r12.onCallBack(r1, r2)
            goto L_0x0053
        L_0x0050:
            r12.onCallBack(r1, r2)
        L_0x0053:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.TabBarWebview.append(java.lang.String, io.dcloud.common.DHInterface.ICallBack):void");
    }

    public boolean checkPagePathIsTab(String str) {
        Iterator<String> it = this.mPagePaths.iterator();
        while (it.hasNext()) {
            if (it.next().equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v1, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean checkUrlToReload(java.lang.String r9) {
        /*
            r8 = this;
            r0 = 0
            java.util.ArrayList<io.dcloud.common.core.ui.b> r1 = r8.mTabItems     // Catch:{ Exception -> 0x0056 }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ Exception -> 0x0056 }
        L_0x0007:
            boolean r2 = r1.hasNext()     // Catch:{ Exception -> 0x0056 }
            if (r2 == 0) goto L_0x0056
            java.lang.Object r2 = r1.next()     // Catch:{ Exception -> 0x0056 }
            io.dcloud.common.core.ui.b r2 = (io.dcloud.common.core.ui.b) r2     // Catch:{ Exception -> 0x0056 }
            java.lang.String r3 = ".js"
            boolean r3 = r9.endsWith(r3)     // Catch:{ Exception -> 0x0056 }
            r4 = 1
            if (r3 == 0) goto L_0x0040
            java.lang.Object[] r1 = new java.lang.Object[r4]     // Catch:{ Exception -> 0x0056 }
            r1[r0] = r9     // Catch:{ Exception -> 0x0056 }
            io.dcloud.common.core.ui.l r9 = r2.mWindowMgr     // Catch:{ Exception -> 0x0056 }
            io.dcloud.common.DHInterface.IMgr$MgrType r3 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr     // Catch:{ Exception -> 0x0056 }
            r5 = 10
            r6 = 4
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ Exception -> 0x0056 }
            io.dcloud.common.DHInterface.IApp r2 = r2.obtainApp()     // Catch:{ Exception -> 0x0056 }
            r6[r0] = r2     // Catch:{ Exception -> 0x0056 }
            java.lang.String r2 = "weex,io.dcloud.feature.weex.WeexFeature"
            r6[r4] = r2     // Catch:{ Exception -> 0x0056 }
            r2 = 2
            java.lang.String r7 = "updateReload"
            r6[r2] = r7     // Catch:{ Exception -> 0x0056 }
            r2 = 3
            r6[r2] = r1     // Catch:{ Exception -> 0x0056 }
            r9.processEvent(r3, r5, r6)     // Catch:{ Exception -> 0x0056 }
            return r4
        L_0x0040:
            io.dcloud.common.DHInterface.IWebview r3 = r2.obtainWebView()     // Catch:{ Exception -> 0x0056 }
            java.lang.String r3 = r3.obtainUrl()     // Catch:{ Exception -> 0x0056 }
            boolean r3 = r3.startsWith(r9)     // Catch:{ Exception -> 0x0056 }
            if (r3 == 0) goto L_0x0007
            io.dcloud.common.DHInterface.IWebview r9 = r2.obtainWebView()     // Catch:{ Exception -> 0x0056 }
            r9.reload()     // Catch:{ Exception -> 0x0056 }
            return r4
        L_0x0056:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.TabBarWebview.checkUrlToReload(java.lang.String):boolean");
    }

    public void dispose() {
        super.dispose();
        TabView tabView = this.mTabBar;
        if (tabView != null) {
            tabView.dispose();
        }
        this.mTabItems.clear();
        ArrayList<String> arrayList = this.mPagePaths;
        if (arrayList != null) {
            arrayList.clear();
        }
    }

    public void evalJS(String str) {
    }

    public void evalJS(String str, ReceiveJSValue.ReceiveJSValueCallback receiveJSValueCallback) {
    }

    public void executeScript(String str) {
    }

    public float getScale() {
        return this.mScale;
    }

    public int getSelectIndex() {
        return this.mSelectIndex;
    }

    public String getTabBarHeight() {
        return this.mTabBar.getTabHeightStr().substring(0, this.mTabBar.getTabHeightStr().length() - 2);
    }

    public void hideTabBar(JSONObject jSONObject) {
        if (this.isVisible) {
            boolean booleanValue = jSONObject.containsKey("animation") ? jSONObject.getBoolean("animation").booleanValue() : false;
            setTabItemsBottomMargin(0);
            this.mTabBar.bringToFront();
            this.mTabBar.bringMaskToFront();
            if (booleanValue) {
                TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (this.mTabBar.getTabHeight() + this.mTabBar.getMidHeight()));
                translateAnimation.setDuration(100);
                translateAnimation.setAnimationListener(new b());
                this.mTabBar.startAnimation(translateAnimation);
            } else {
                this.mTabBar.postDelayed(new c(), 150);
            }
            this.isVisible = false;
        }
    }

    public void hideTabBarRedDot(JSONObject jSONObject) {
        this.mTabBar.hideTabBarRedDot(jSONObject);
    }

    public boolean isInsertLauch() {
        if (this.mChildJson != null) {
            for (int i = 0; i < this.mChildJson.size(); i++) {
                if (this.mChildJson.getString(i).equals("lauchwebview")) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public IApp obtainApp() {
        return this.mApp;
    }

    public ViewGroup obtainWindowView() {
        return this.mRoot;
    }

    /* access modifiers changed from: protected */
    public void onResize() {
        super.onResize();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = r8.mWindowMgr;
        r1 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr;
        r3 = r8.mApp;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void popFrame(java.lang.String r9) {
        /*
            r8 = this;
            android.widget.FrameLayout r0 = r8.mTabLayout
            if (r0 == 0) goto L_0x0042
            io.dcloud.common.core.ui.l r0 = r8.mWindowMgr
            io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r2 = 4
            java.lang.Object[] r2 = new java.lang.Object[r2]
            io.dcloud.common.DHInterface.IApp r3 = r8.mApp
            r4 = 0
            r2[r4] = r3
            java.lang.String r5 = "ui"
            r6 = 1
            r2[r6] = r5
            java.lang.String r5 = "findWebview"
            r7 = 2
            r2[r7] = r5
            java.lang.String[] r5 = new java.lang.String[r7]
            java.lang.String r3 = r3.obtainAppId()
            r5[r4] = r3
            r5[r6] = r9
            r9 = 3
            r2[r9] = r5
            r9 = 10
            java.lang.Object r9 = r0.processEvent(r1, r9, r2)
            if (r9 == 0) goto L_0x0042
            boolean r0 = r9 instanceof io.dcloud.common.DHInterface.IWebview
            if (r0 == 0) goto L_0x0042
            io.dcloud.common.DHInterface.IWebview r9 = (io.dcloud.common.DHInterface.IWebview) r9
            io.dcloud.common.DHInterface.IFrameView r9 = r9.obtainFrameView()
            android.view.View r9 = r9.obtainMainView()
            r0 = 8
            r9.setVisibility(r0)
        L_0x0042:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.TabBarWebview.popFrame(java.lang.String):void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = r8.mWindowMgr;
        r1 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr;
        r3 = r8.mApp;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void pushFrame(java.lang.String r9) {
        /*
            r8 = this;
            android.widget.FrameLayout r0 = r8.mTabLayout
            if (r0 == 0) goto L_0x0040
            io.dcloud.common.core.ui.l r0 = r8.mWindowMgr
            io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r2 = 4
            java.lang.Object[] r2 = new java.lang.Object[r2]
            io.dcloud.common.DHInterface.IApp r3 = r8.mApp
            r4 = 0
            r2[r4] = r3
            java.lang.String r5 = "ui"
            r6 = 1
            r2[r6] = r5
            java.lang.String r5 = "findWebview"
            r7 = 2
            r2[r7] = r5
            java.lang.String[] r5 = new java.lang.String[r7]
            java.lang.String r3 = r3.obtainAppId()
            r5[r4] = r3
            r5[r6] = r9
            r9 = 3
            r2[r9] = r5
            r9 = 10
            java.lang.Object r9 = r0.processEvent(r1, r9, r2)
            if (r9 == 0) goto L_0x0040
            boolean r0 = r9 instanceof io.dcloud.common.DHInterface.IWebview
            if (r0 == 0) goto L_0x0040
            io.dcloud.common.DHInterface.IWebview r9 = (io.dcloud.common.DHInterface.IWebview) r9
            io.dcloud.common.DHInterface.IFrameView r9 = r9.obtainFrameView()
            android.view.View r9 = r9.obtainMainView()
            r9.setVisibility(r4)
        L_0x0040:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.TabBarWebview.pushFrame(java.lang.String):void");
    }

    public void removeFrameView(b bVar) {
        FrameLayout frameLayout;
        if (bVar != null && (frameLayout = this.mTabLayout) != null) {
            frameLayout.removeView(bVar.obtainMainView());
            if (this.mTabItems.contains(bVar)) {
                this.mTabItems.remove(bVar);
            }
            if (this.mTabItems.size() == 0 && this.mTabBar.getVisibility() == 0) {
                this.mTabBar.setVisibility(4);
            }
        }
    }

    public void removeTabBarBadge(JSONObject jSONObject) {
        this.mTabBar.removeTabBarBadge(jSONObject);
    }

    public void setClickCallBack(ICallBack iCallBack) {
        this.mTabBar.setSingleCallbackListener(iCallBack);
    }

    public void setDoubleClickCallBack(ICallBack iCallBack) {
        this.mTabBar.setDoubleCallbackListener(iCallBack);
    }

    public void setIWebViewFocusable(boolean z) {
        super.setIWebViewFocusable(z);
        ArrayList<b> arrayList = this.mTabItems;
        if (arrayList != null) {
            Iterator<b> it = arrayList.iterator();
            while (it.hasNext()) {
                b next = it.next();
                if (!(next == null || next.obtainWebView() == null)) {
                    next.obtainWebView().setIWebViewFocusable(z);
                }
            }
        }
        TabView tabView = this.mTabBar;
        if (tabView != null) {
            tabView.setIWebViewFocusable(z);
        }
    }

    public void setItem(JSONObject jSONObject) {
        this.mTabBar.setTabBarItem(jSONObject);
    }

    public void setMask(JSONObject jSONObject) {
        this.mTabBar.setMask(jSONObject);
    }

    public void setMaskButtonClickCallBack(ICallBack iCallBack) {
        this.mTabBar.setMaskCallbackListener(iCallBack);
    }

    public void setMidButtonClickCallBack(ICallBack iCallBack) {
        this.mTabBar.setMidCallbackListener(iCallBack);
    }

    public void setStyle(JSONObject jSONObject) {
        if (jSONObject != null) {
            this.mTabBar.setTabBarStyle(jSONObject);
            if (jSONObject.containsKey("height")) {
                setTabItemsBottomMargin(this.isVisible ? this.mTabBar.getTabHeight() : 0);
            }
        }
    }

    public void setTabBarBadge(JSONObject jSONObject) {
        this.mTabBar.setTabBarBadge(jSONObject);
    }

    public void setTabItemsBottomMargin(int i) {
        String str;
        if (this.mTabItems != null) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mTabLayout.getLayoutParams();
            layoutParams.bottomMargin = i;
            this.mTabLayout.setLayoutParams(layoutParams);
            Iterator<b> it = this.mTabItems.iterator();
            while (it.hasNext()) {
                b next = it.next();
                if (!(next == null || next.obtainMainView() == null)) {
                    org.json.JSONObject jSONObject = new org.json.JSONObject();
                    try {
                        jSONObject.put("top", "0px");
                        if (i > 0) {
                            str = this.mTabBar.getTabHeightStr();
                        } else {
                            str = "0px";
                        }
                        jSONObject.put("bottom", str);
                        jSONObject.put("isTab", true);
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                    next.obtainFrameOptions().updateViewData(jSONObject);
                    next.obtainMainView().post(new e(next));
                }
            }
            this.mFrameView.resize();
        }
    }

    public void show(Animation animation) {
    }

    public void showTabBar(JSONObject jSONObject) {
        if (!this.isVisible) {
            if (jSONObject.containsKey("animation") ? jSONObject.getBoolean("animation").booleanValue() : false) {
                TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) (this.mTabBar.getTabHeight() + this.mTabBar.getMidHeight()), 0.0f);
                translateAnimation.setDuration(100);
                translateAnimation.setAnimationListener(new d());
                this.mTabBar.startAnimation(translateAnimation);
            } else {
                setTabItemsBottomMargin(this.mTabBar.getTabHeight());
            }
            this.mTabBar.setVisibility(0);
            this.isVisible = true;
        }
    }

    public void showTabBarRedDot(JSONObject jSONObject) {
        this.mTabBar.showTabBarRedDot(jSONObject);
    }

    public void switchSelect(int i) {
        this.mSelectIndex = i;
        this.mTabBar.switchTab(i);
    }

    public void tabItemActive(b bVar) {
        for (int i = 0; i < this.mTabItems.size(); i++) {
            b bVar2 = this.mTabItems.get(i);
            if (bVar == null || bVar2 != bVar) {
                this.mTabItems.get(i).obtainMainView().setImportantForAccessibility(4);
                bVar2.obtainMainView().setImportantForAccessibility(4);
            } else {
                bVar2.obtainMainView().setImportantForAccessibility(0);
            }
        }
    }

    public void updateMidButton(JSONObject jSONObject) {
        this.mTabBar.updateMidButton(jSONObject);
    }

    public void append(b bVar) {
        String str = "0px";
        if (bVar != null) {
            View obtainMainView = bVar.obtainMainView();
            if (obtainMainView.getParent() != null) {
                bVar.q();
                bVar.getAnimOptions().mOption = 1;
                bVar.p.b(bVar);
                if (bVar.f()) {
                    this.mWindowMgr.processEvent(IMgr.MgrType.WindowMgr, 28, bVar.g);
                    bVar.g = null;
                }
                bVar.s();
                bVar.b(false);
                bVar.getAnimOptions().mOption = 0;
                ((ViewGroup) obtainMainView.getParent()).removeView(obtainMainView);
            }
            bVar.setTabItem(true);
            this.mWindowMgr.processEvent(IMgr.MgrType.WindowMgr, 22, bVar);
            org.json.JSONObject jSONObject = new org.json.JSONObject();
            try {
                jSONObject.put("top", str);
                if (this.isVisible) {
                    str = this.mTabBar.getTabHeightStr();
                }
                jSONObject.put("bottom", str);
                jSONObject.put("isTab", true);
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
            bVar.obtainFrameOptions().setParentViewRect(this.mFrameView.obtainFrameOptions());
            bVar.obtainFrameOptions().updateViewData(jSONObject);
            bVar.setParentFrameItem(this.mFrameView);
            this.mFrameView.mChildArrayList.add(bVar);
            bVar.inStack = true;
            bVar.isChildOfFrameView = true;
            this.mTabLayout.addView(obtainMainView, new FrameLayout.LayoutParams(-1, -1));
            bVar.obtainWebView().setIWebViewFocusable(true);
            if (!this.mTabItems.contains(bVar)) {
                this.mTabItems.add(bVar);
            }
            if (this.mTabItems.size() > 0 && this.mTabBar.getVisibility() != 0 && this.isVisible) {
                this.mTabBar.setVisibility(0);
            }
            if (obtainMainView.getImportantForAccessibility() == 4) {
                obtainMainView.setImportantForAccessibility(0);
            }
            bVar.obtainMainView().post(new a(bVar));
        }
    }
}
