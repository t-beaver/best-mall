package com.taobao.weex.ui.component;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.R;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.OnWXScrollListener;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.instance.InstanceOnFireEventInterceptor;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.NestedContainer;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.adapter.ui.webview.WebViewFactory;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;

@Component(lazyload = false)
public class WXEmbed extends WXDiv implements WXSDKInstance.OnInstanceVisibleListener, NestedContainer {
    /* access modifiers changed from: private */
    public static int ERROR_IMG_HEIGHT = ((int) WXViewUtils.getRealPxByWidth(260.0f, 750));
    /* access modifiers changed from: private */
    public static int ERROR_IMG_WIDTH = ((int) WXViewUtils.getRealPxByWidth(270.0f, 750));
    public static final String ITEM_ID = "itemId";
    public static final String PRIORITY_HIGH = "high";
    public static final String PRIORITY_LOW = "low";
    public static final String PRIORITY_NORMAL = "normal";
    public static final String STRATEGY_HIGH = "high";
    public static final String STRATEGY_NONE = "none";
    public static final String STRATEGY_NORMAL = "normal";
    /* access modifiers changed from: private */
    public long hiddenTime;
    private EmbedInstanceOnScrollFireEventInterceptor mInstanceOnScrollFireEventInterceptor;
    private boolean mIsVisible;
    private EmbedRenderListener mListener;
    protected WXSDKInstance mNestedInstance;
    private String originUrl;
    private String priority;
    /* access modifiers changed from: private */
    public String src;
    private String strategy;

    public interface EmbedManager {
        WXEmbed getEmbed(String str);

        void putEmbed(String str, WXEmbed wXEmbed);
    }

    public static class FailToH5Listener extends ClickToReloadListener {
        public void onException(NestedContainer nestedContainer, String str, String str2) {
            if (str == null || !(nestedContainer instanceof WXEmbed) || !str.startsWith("1|")) {
                super.onException(nestedContainer, str, str2);
                return;
            }
            ViewGroup viewContainer = nestedContainer.getViewContainer();
            WebView webView = new WebView(viewContainer.getContext());
            webView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            webView.getSettings().setAllowFileAccess(false);
            WebViewFactory.setFileAccess(webView.getSettings(), true);
            WebViewFactory.openJSEnabled(webView.getSettings(), (IApp) null);
            webView.removeJavascriptInterface("searchBoxJavaBridge_");
            webView.removeJavascriptInterface("accessibility");
            webView.removeJavascriptInterface("accessibilityTraversal");
            webView.getSettings().setSavePassword(false);
            viewContainer.removeAllViews();
            viewContainer.addView(webView);
            webView.loadUrl(((WXEmbed) nestedContainer).src);
        }
    }

    public static class ClickToReloadListener implements NestedContainer.OnNestedInstanceEventListener {
        public void onCreated(NestedContainer nestedContainer, WXSDKInstance wXSDKInstance) {
        }

        public boolean onPreCreate(NestedContainer nestedContainer, String str) {
            return true;
        }

        public String transformUrl(String str) {
            return str;
        }

        public void onException(NestedContainer nestedContainer, String str, String str2) {
            if (TextUtils.equals(str, WXErrorCode.WX_DEGRAD_ERR_NETWORK_BUNDLE_DOWNLOAD_FAILED.getErrorCode()) && (nestedContainer instanceof WXEmbed)) {
                final WXEmbed wXEmbed = (WXEmbed) nestedContainer;
                final ImageView imageView = new ImageView(wXEmbed.getContext());
                imageView.setImageResource(R.drawable.weex_error);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(WXEmbed.ERROR_IMG_WIDTH, WXEmbed.ERROR_IMG_HEIGHT);
                layoutParams.gravity = 17;
                imageView.setLayoutParams(layoutParams);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setAdjustViewBounds(true);
                imageView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        imageView.setOnClickListener((View.OnClickListener) null);
                        imageView.setEnabled(false);
                        wXEmbed.loadContent();
                    }
                });
                FrameLayout frameLayout = (FrameLayout) wXEmbed.getHostView();
                frameLayout.removeAllViews();
                frameLayout.addView(imageView);
                WXLogUtils.e("WXEmbed", "NetWork failure :" + str + ",\n error message :" + str2);
            }
        }
    }

    static class EmbedRenderListener implements IWXRenderListener {
        WXEmbed mComponent;
        NestedContainer.OnNestedInstanceEventListener mEventListener = new ClickToReloadListener();

        public void onRefreshSuccess(WXSDKInstance wXSDKInstance, int i, int i2) {
        }

        public void onRenderSuccess(WXSDKInstance wXSDKInstance, int i, int i2) {
        }

        EmbedRenderListener(WXEmbed wXEmbed) {
            this.mComponent = wXEmbed;
        }

        public void onViewCreated(WXSDKInstance wXSDKInstance, View view) {
            FrameLayout frameLayout = (FrameLayout) this.mComponent.getHostView();
            frameLayout.removeAllViews();
            frameLayout.addView(view);
        }

        public void onException(WXSDKInstance wXSDKInstance, String str, String str2) {
            NestedContainer.OnNestedInstanceEventListener onNestedInstanceEventListener = this.mEventListener;
            if (onNestedInstanceEventListener != null) {
                onNestedInstanceEventListener.onException(this.mComponent, str, str2);
            }
        }
    }

    @Deprecated
    public WXEmbed(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public WXEmbed(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        Object obj;
        this.mIsVisible = true;
        this.priority = "normal";
        this.strategy = "normal";
        this.mListener = new EmbedRenderListener(this);
        this.mInstanceOnScrollFireEventInterceptor = new EmbedInstanceOnScrollFireEventInterceptor(this);
        ERROR_IMG_WIDTH = (int) WXViewUtils.getRealPxByWidth(270.0f, wXSDKInstance.getInstanceViewPortWidthWithFloat());
        ERROR_IMG_HEIGHT = (int) WXViewUtils.getRealPxByWidth(260.0f, wXSDKInstance.getInstanceViewPortWidthWithFloat());
        if ((wXSDKInstance instanceof EmbedManager) && (obj = getAttrs().get(ITEM_ID)) != null) {
            ((EmbedManager) wXSDKInstance).putEmbed(obj.toString(), this);
        }
        this.priority = WXUtils.getString(getAttrs().get("priority"), "normal");
        this.strategy = WXUtils.getString(getAttrs().get(Constants.Name.STRATEGY), "none");
        wXSDKInstance.getApmForInstance().updateDiffStats(WXInstanceApm.KEY_PAGE_STATS_EMBED_COUNT, 1.0d);
    }

    public void setOnNestEventListener(NestedContainer.OnNestedInstanceEventListener onNestedInstanceEventListener) {
        this.mListener.mEventListener = onNestedInstanceEventListener;
    }

    public ViewGroup getViewContainer() {
        return (ViewGroup) getHostView();
    }

    /* access modifiers changed from: protected */
    public boolean setProperty(String str, Object obj) {
        str.hashCode();
        if (str.equals("priority")) {
            String string = WXUtils.getString(obj, (String) null);
            if (string != null) {
                setPriority(string);
            }
            return true;
        } else if (!str.equals("src")) {
            return super.setProperty(str, obj);
        } else {
            String string2 = WXUtils.getString(obj, (String) null);
            if (string2 != null) {
                setSrc(string2);
            }
            return true;
        }
    }

    public void renderNewURL(String str) {
        this.src = str;
        loadContent();
    }

    public void reload() {
        if (!TextUtils.isEmpty(this.src)) {
            loadContent();
        }
    }

    public String getOriginUrl() {
        return this.originUrl;
    }

    public void setOriginUrl(String str) {
        this.originUrl = str;
    }

    public void addEvent(String str) {
        super.addEvent(str);
        if (Constants.Event.SCROLL_START.equals(str)) {
            this.mInstanceOnScrollFireEventInterceptor.addInterceptEvent(str);
        } else if (Constants.Event.SCROLL_END.equals(str)) {
            this.mInstanceOnScrollFireEventInterceptor.addInterceptEvent(str);
        } else if ("scroll".equals(str)) {
            this.mInstanceOnScrollFireEventInterceptor.addInterceptEvent(str);
        }
    }

    @WXComponentProp(name = "src")
    public void setSrc(String str) {
        this.originUrl = str;
        this.src = str;
        WXSDKInstance wXSDKInstance = this.mNestedInstance;
        if (wXSDKInstance != null) {
            wXSDKInstance.destroy();
            this.mNestedInstance = null;
        }
        if (this.mIsVisible && !TextUtils.isEmpty(this.src)) {
            loadContent();
        }
    }

    public String getSrc() {
        return this.src;
    }

    @WXComponentProp(name = "priority")
    public void setPriority(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.priority = str;
        }
    }

    /* access modifiers changed from: protected */
    public void loadContent() {
        WXSDKInstance wXSDKInstance = this.mNestedInstance;
        if (wXSDKInstance != null) {
            wXSDKInstance.destroy();
        }
        this.mNestedInstance = createInstance();
        EmbedRenderListener embedRenderListener = this.mListener;
        if (embedRenderListener != null && embedRenderListener.mEventListener != null && !this.mListener.mEventListener.onPreCreate(this, this.src)) {
            this.mListener.mEventListener.onCreated(this, this.mNestedInstance);
        }
    }

    /* access modifiers changed from: private */
    public static final int getLevel(WXEmbed wXEmbed) {
        String str = wXEmbed.priority;
        if (!"high".equals(wXEmbed.strategy)) {
            if (TextUtils.equals(str, "low")) {
                return 0;
            }
            if (TextUtils.equals(str, "high")) {
                return 10;
            }
        }
        return 5;
    }

    private WXSDKInstance createInstance() {
        WXSDKInstance createNestedInstance = getInstance().createNestedInstance(this);
        createNestedInstance.setParentInstance(getInstance());
        if (!getAttrs().containsKey("disableInstanceVisibleListener")) {
            getInstance().addOnInstanceVisibleListener(this);
        }
        createNestedInstance.registerRenderListener(this.mListener);
        this.mInstanceOnScrollFireEventInterceptor.resetFirstLaterScroller();
        createNestedInstance.addInstanceOnFireEventInterceptor(this.mInstanceOnScrollFireEventInterceptor);
        createNestedInstance.registerOnWXScrollListener(this.mInstanceOnScrollFireEventInterceptor);
        String str = this.src;
        EmbedRenderListener embedRenderListener = this.mListener;
        if (!(embedRenderListener == null || embedRenderListener.mEventListener == null)) {
            str = this.mListener.mEventListener.transformUrl(this.src);
            if (!this.mListener.mEventListener.onPreCreate(this, this.src)) {
                return null;
            }
        }
        String str2 = str;
        if (TextUtils.isEmpty(str2)) {
            NestedContainer.OnNestedInstanceEventListener onNestedInstanceEventListener = this.mListener.mEventListener;
            String errorCode = WXErrorCode.WX_DEGRAD_ERR_BUNDLE_CONTENTTYPE_ERROR.getErrorCode();
            onNestedInstanceEventListener.onException(this, errorCode, WXErrorCode.WX_DEGRAD_ERR_BUNDLE_CONTENTTYPE_ERROR.getErrorMsg() + "!!wx embed src url is null");
            return createNestedInstance;
        }
        createNestedInstance.setContainerInfo(WXInstanceApm.KEY_PAGE_PROPERTIES_INSTANCE_TYPE, WXBasicComponentType.EMBED);
        createNestedInstance.setContainerInfo(WXInstanceApm.KEY_PAGE_PROPERTIES_PARENT_PAGE, getInstance().getWXPerformance().pageName);
        createNestedInstance.renderByUrl(str2, str2, (Map<String, Object>) null, (String) null, WXRenderStrategy.APPEND_ASYNC);
        return createNestedInstance;
    }

    public void setVisibility(String str) {
        WXSDKInstance wXSDKInstance;
        super.setVisibility(str);
        boolean equals = TextUtils.equals(str, "visible");
        if (this.mIsVisible != equals) {
            if (!TextUtils.isEmpty(this.src) && equals) {
                WXSDKInstance wXSDKInstance2 = this.mNestedInstance;
                if (wXSDKInstance2 == null) {
                    loadContent();
                } else {
                    wXSDKInstance2.onViewAppear();
                }
            }
            if (!equals && (wXSDKInstance = this.mNestedInstance) != null) {
                wXSDKInstance.onViewDisappear();
            }
            this.mIsVisible = equals;
            doAutoEmbedMemoryStrategy();
        }
    }

    public void destroy() {
        super.destroy();
        destoryNestInstance();
        this.src = null;
        if (getInstance() != null) {
            getInstance().removeOnInstanceVisibleListener(this);
        }
    }

    private void doAutoEmbedMemoryStrategy() {
        if (!"none".equals(this.strategy)) {
            if (!this.mIsVisible && this.mNestedInstance != null) {
                if ("low".equals(this.priority)) {
                    destoryNestInstance();
                } else {
                    if (getInstance().hiddenEmbeds == null) {
                        getInstance().hiddenEmbeds = new PriorityQueue<>(8, new Comparator<WXEmbed>() {
                            public int compare(WXEmbed wXEmbed, WXEmbed wXEmbed2) {
                                int access$300 = WXEmbed.getLevel(wXEmbed) - WXEmbed.getLevel(wXEmbed2);
                                if (access$300 != 0) {
                                    return access$300;
                                }
                                return (int) (wXEmbed.hiddenTime - wXEmbed2.hiddenTime);
                            }
                        });
                    }
                    if (!getInstance().hiddenEmbeds.contains(this)) {
                        this.hiddenTime = System.currentTimeMillis();
                        getInstance().hiddenEmbeds.add(this);
                    }
                    if (getInstance().hiddenEmbeds != null && getInstance().getMaxHiddenEmbedsNum() >= 0) {
                        while (getInstance().hiddenEmbeds.size() > getInstance().getMaxHiddenEmbedsNum()) {
                            WXEmbed poll = getInstance().hiddenEmbeds.poll();
                            if (!poll.mIsVisible && poll != null) {
                                poll.destoryNestInstance();
                            }
                        }
                    }
                }
            }
            if (this.mIsVisible && this.mNestedInstance != null && getInstance().hiddenEmbeds != null && getInstance().hiddenEmbeds.contains(this)) {
                getInstance().hiddenEmbeds.remove(this);
            }
        }
    }

    public void onAppear() {
        WXSDKInstance wXSDKInstance;
        WXComponent rootComponent;
        if (this.mIsVisible && (wXSDKInstance = this.mNestedInstance) != null && (rootComponent = wXSDKInstance.getRootComponent()) != null) {
            rootComponent.fireEvent(Constants.Event.VIEWAPPEAR);
        }
    }

    public void onDisappear() {
        WXSDKInstance wXSDKInstance;
        WXComponent rootComponent;
        if (this.mIsVisible && (wXSDKInstance = this.mNestedInstance) != null && (rootComponent = wXSDKInstance.getRootComponent()) != null) {
            rootComponent.fireEvent(Constants.Event.VIEWDISAPPEAR);
        }
    }

    public void onActivityStart() {
        super.onActivityStart();
        WXSDKInstance wXSDKInstance = this.mNestedInstance;
        if (wXSDKInstance != null) {
            wXSDKInstance.onActivityStart();
        }
    }

    public void onActivityResume() {
        super.onActivityResume();
        WXSDKInstance wXSDKInstance = this.mNestedInstance;
        if (wXSDKInstance != null) {
            wXSDKInstance.onActivityResume();
        }
    }

    public void onActivityPause() {
        super.onActivityPause();
        WXSDKInstance wXSDKInstance = this.mNestedInstance;
        if (wXSDKInstance != null) {
            wXSDKInstance.onActivityPause();
        }
    }

    public void onActivityStop() {
        super.onActivityStop();
        WXSDKInstance wXSDKInstance = this.mNestedInstance;
        if (wXSDKInstance != null) {
            wXSDKInstance.onActivityStop();
        }
    }

    public void onActivityDestroy() {
        super.onActivityDestroy();
        WXSDKInstance wXSDKInstance = this.mNestedInstance;
        if (wXSDKInstance != null) {
            wXSDKInstance.onActivityDestroy();
        }
    }

    public void setStrategy(String str) {
        this.strategy = str;
    }

    private void destoryNestInstance() {
        if (getInstance().hiddenEmbeds != null && getInstance().hiddenEmbeds.contains(this)) {
            getInstance().hiddenEmbeds.remove(this);
        }
        WXSDKInstance wXSDKInstance = this.mNestedInstance;
        if (wXSDKInstance != null) {
            wXSDKInstance.destroy();
            this.mNestedInstance = null;
        }
        if (WXEnvironment.isApkDebugable()) {
            StringBuilder sb = new StringBuilder();
            sb.append("WXEmbed destoryNestInstance priority ");
            sb.append(this.priority);
            sb.append(" index ");
            sb.append(getAttrs().get("index"));
            sb.append("  ");
            sb.append(this.hiddenTime);
            sb.append(" embeds size ");
            sb.append(getInstance().hiddenEmbeds == null ? 0 : getInstance().hiddenEmbeds.size());
            sb.append(" strategy ");
            sb.append(this.strategy);
            WXLogUtils.w(sb.toString());
        }
    }

    public void addLayerOverFlowListener(String str) {
        WXSDKInstance wXSDKInstance = this.mNestedInstance;
        if (wXSDKInstance != null) {
            wXSDKInstance.addLayerOverFlowListener(getRef());
        }
    }

    public void removeLayerOverFlowListener(String str) {
        WXSDKInstance wXSDKInstance = this.mNestedInstance;
        if (wXSDKInstance != null) {
            wXSDKInstance.removeLayerOverFlowListener(str);
        }
    }

    static class EmbedInstanceOnScrollFireEventInterceptor extends InstanceOnFireEventInterceptor implements OnWXScrollListener {
        private WXComponent firstLayerScroller;
        private WXEmbed mEmbed;

        public void onScrollStateChanged(View view, int i, int i2, int i3) {
        }

        public EmbedInstanceOnScrollFireEventInterceptor(WXEmbed wXEmbed) {
            this.mEmbed = wXEmbed;
        }

        public void resetFirstLaterScroller() {
            this.firstLayerScroller = null;
        }

        public void onFireEvent(String str, String str2, String str3, Map<String, Object> map, Map<String, Object> map2) {
            WXEmbed wXEmbed = this.mEmbed;
            if (wXEmbed != null && wXEmbed.mNestedInstance != null && this.mEmbed.mNestedInstance.getInstanceId().equals(str)) {
                if (this.firstLayerScroller == null) {
                    initFirstLayerScroller();
                }
                WXComponent wXComponent = this.firstLayerScroller;
                if (wXComponent != null && wXComponent.getRef().equals(str2)) {
                    this.mEmbed.getInstance().fireEvent(this.mEmbed.getRef(), str3, map, map2);
                }
            }
        }

        private void initFirstLayerScroller() {
            if (this.firstLayerScroller == null) {
                WXComponent findFirstLayerScroller = findFirstLayerScroller();
                this.firstLayerScroller = findFirstLayerScroller;
                if (findFirstLayerScroller != null) {
                    for (String next : getListenEvents()) {
                        if (!this.firstLayerScroller.containsEvent(next)) {
                            this.firstLayerScroller.getEvents().add(next);
                            this.firstLayerScroller.addEvent(next);
                        }
                    }
                }
            }
        }

        private WXComponent findFirstLayerScroller() {
            WXComponent wXComponent;
            if (this.mEmbed.mNestedInstance == null) {
                return null;
            }
            WXComponent rootComponent = this.mEmbed.mNestedInstance.getRootComponent();
            if (rootComponent instanceof Scrollable) {
                return rootComponent;
            }
            ArrayDeque arrayDeque = new ArrayDeque();
            arrayDeque.offer(rootComponent);
            while (!arrayDeque.isEmpty() && (wXComponent = (WXComponent) arrayDeque.poll()) != null) {
                if (wXComponent instanceof Scrollable) {
                    return wXComponent;
                }
                if (wXComponent instanceof WXVContainer) {
                    WXVContainer wXVContainer = (WXVContainer) wXComponent;
                    for (int i = 0; i < wXVContainer.getChildCount(); i++) {
                        arrayDeque.offer(wXVContainer.getChild(i));
                    }
                }
            }
            return null;
        }

        public void onScrolled(View view, int i, int i2) {
            if (this.firstLayerScroller == null && getListenEvents().size() > 0) {
                initFirstLayerScroller();
            }
        }
    }
}
