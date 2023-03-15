package com.taobao.weex.ui.component;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Pair;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.ComponentObserver;
import com.taobao.weex.IWXActivityStateListener;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXAccessibilityRoleAdapter;
import com.taobao.weex.adapter.IWXConfigAdapter;
import com.taobao.weex.bridge.EventResult;
import com.taobao.weex.bridge.Invoker;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.IWXObject;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXPerformance;
import com.taobao.weex.common.WXRuntimeException;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.dom.WXEvent;
import com.taobao.weex.dom.WXStyle;
import com.taobao.weex.dom.transition.WXTransition;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.layout.ContentBoxMeasurement;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.tracing.Stopwatch;
import com.taobao.weex.tracing.WXTracing;
import com.taobao.weex.ui.IFComponentHolder;
import com.taobao.weex.ui.WXRenderManager;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.action.GraphicActionAnimation;
import com.taobao.weex.ui.action.GraphicActionUpdateStyle;
import com.taobao.weex.ui.action.GraphicPosition;
import com.taobao.weex.ui.action.GraphicSize;
import com.taobao.weex.ui.animation.WXAnimationBean;
import com.taobao.weex.ui.component.basic.WXBasicComponent;
import com.taobao.weex.ui.component.binding.Statements;
import com.taobao.weex.ui.component.list.WXCell;
import com.taobao.weex.ui.component.list.template.jni.NativeRenderObjectUtils;
import com.taobao.weex.ui.component.pesudo.OnActivePseudoListner;
import com.taobao.weex.ui.component.pesudo.PesudoStatus;
import com.taobao.weex.ui.component.pesudo.TouchActivePseudoListener;
import com.taobao.weex.ui.flat.FlatComponent;
import com.taobao.weex.ui.flat.FlatGUIContext;
import com.taobao.weex.ui.flat.widget.AndroidViewWidget;
import com.taobao.weex.ui.flat.widget.Widget;
import com.taobao.weex.ui.view.BaseFrameLayout;
import com.taobao.weex.ui.view.border.BorderDrawable;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;
import com.taobao.weex.ui.view.gesture.WXGestureType;
import com.taobao.weex.utils.WXDataStructureUtil;
import com.taobao.weex.utils.WXExceptionUtils;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXReflectionUtils;
import com.taobao.weex.utils.WXResourceUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import io.dcloud.feature.uniapp.dom.AbsCSSShorthand;
import io.dcloud.feature.uniapp.ui.AbsAnimationHolder;
import io.dcloud.feature.uniapp.ui.action.UniMethodData;
import io.dcloud.feature.uniapp.ui.component.AbsBasicComponent;
import io.dcloud.feature.uniapp.ui.shadow.UniBoxShadowData;
import io.dcloud.feature.uniapp.ui.shadow.UniInsetBoxShadowLayer;
import io.dcloud.feature.uniapp.ui.shadow.UniNormalBoxShadowDrawable;
import io.dcloud.feature.uniapp.utils.UniBoxShadowUtil;
import io.dcloud.weex.ViewHover;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class WXComponent<T extends View> extends WXBasicComponent<T> implements IWXObject, IWXActivityStateListener, OnActivePseudoListner {
    public static final String PROP_FIXED_SIZE = "fixedSize";
    public static final String PROP_FS_MATCH_PARENT = "m";
    public static final String PROP_FS_WRAP_CONTENT = "w";
    public static final String ROOT = "_root";
    public static final int STATE_ALL_FINISH = 2;
    public static final int STATE_DOM_FINISH = 0;
    public static final int STATE_UI_FINISH = 1;
    public static final String TYPE = "type";
    public static final int TYPE_COMMON = 0;
    public static final int TYPE_VIRTUAL = 1;
    private int[] EMPTY_STATE_SET;
    private int[] ENABLED_STATE_SET;
    private int[] FOCUSED_ENABLED_STATE_SET;
    private long PRESSED_ANIM_DELAY;
    private long PRESSED_ANIM_DURATION;
    private int[] PRESSED_ENABLED_STATE_SET;
    private ConcurrentLinkedQueue<Pair<String, Map<String, Object>>> animations;
    protected ContentBoxMeasurement contentBoxMeasurement;
    public int interactionAbsoluteX;
    public int interactionAbsoluteY;
    public boolean isIgnoreInteraction;
    private boolean isLastLayoutDirectionRTL;
    private boolean isPreventGesture;
    private boolean isUsing;
    private int mAbsoluteX;
    private int mAbsoluteY;
    private AbsAnimationHolder mAnimationHolder;
    private Set<String> mAppendEvents;
    /* access modifiers changed from: private */
    public BorderDrawable mBackgroundDrawable;
    private UniBoxShadowData mBoxShadowData;
    private UniNormalBoxShadowDrawable mBoxShadowDrawable;
    private WXComponent<T>.OnClickListenerImp mClickEventListener;
    private Context mContext;
    public int mDeepInComponentTree;
    private float mElevation;
    private int mFixedProp;
    /* access modifiers changed from: private */
    public List<OnFocusChangeListener> mFocusChangeListeners;
    protected WXGesture mGesture;
    private Set<String> mGestureType;
    private boolean mHasAddFocusListener;
    private IFComponentHolder mHolder;
    T mHost;
    /* access modifiers changed from: private */
    public List<OnClickListener> mHostClickListeners;
    private ViewHover mHover;
    private UniInsetBoxShadowLayer mInsetBoxShadowDrawable;
    /* access modifiers changed from: private */
    public WXSDKInstance mInstance;
    public boolean mIsAddElementToTree;
    private boolean mIsDestroyed;
    private boolean mIsDisabled;
    private String mLastBoxShadowId;
    private boolean mLazy;
    private boolean mNeedLayoutOnAnimation;
    private volatile WXVContainer mParent;
    /* access modifiers changed from: private */
    public ConcurrentLinkedQueue<UniMethodData> mPendingComponetMethodQueue;
    private PesudoStatus mPesudoStatus;
    private int mPreRealHeight;
    private int mPreRealLeft;
    private int mPreRealRight;
    private int mPreRealTop;
    private int mPreRealWidth;
    private GraphicSize mPseudoResetGraphicSize;
    private Drawable mRippleBackground;
    private int mStickyOffset;
    public WXTracing.TraceInfo mTraceInfo;
    private WXTransition mTransition;
    private int mType;
    private String mViewTreeKey;
    private boolean waste;

    public static class MeasureOutput {
        public int height;
        public int width;
    }

    public interface OnClickListener {
        void onHostViewClick();
    }

    public interface OnFocusChangeListener {
        void onFocusChange(boolean z);
    }

    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RenderState {
    }

    /* access modifiers changed from: protected */
    public void appendEventToDOM(String str) {
    }

    public String getAttrByKey(String str) {
        return "default";
    }

    public int getLayoutTopOffsetForSibling() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public T initComponentHostView(Context context) {
        return null;
    }

    /* access modifiers changed from: protected */
    public void layoutDirectionDidChanged(boolean z) {
    }

    public boolean onActivityBack() {
        return false;
    }

    public void onActivityCreate() {
    }

    public void onActivityPause() {
    }

    public void onActivityResult(int i, int i2, Intent intent) {
    }

    public void onActivityResume() {
    }

    public void onActivityStart() {
    }

    public void onActivityStop() {
    }

    /* access modifiers changed from: protected */
    public void onCreate() {
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    /* access modifiers changed from: protected */
    public void onInvokeUnknownMethod(String str, JSONArray jSONArray) {
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
    }

    public void refreshData(WXComponent wXComponent) {
    }

    @Deprecated
    public void registerActivityStateListener() {
    }

    public void removeVirtualComponent() {
    }

    @Deprecated
    public WXComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    @Deprecated
    public WXComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public WXComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, 0, basicComponentData);
    }

    public WXComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, int i, BasicComponentData basicComponentData) {
        super(basicComponentData);
        this.mFixedProp = 0;
        this.mAbsoluteY = 0;
        this.mAbsoluteX = 0;
        this.isLastLayoutDirectionRTL = false;
        this.mPreRealWidth = 0;
        this.mPreRealHeight = 0;
        this.mPreRealLeft = 0;
        this.mPreRealRight = 0;
        this.mPreRealTop = 0;
        this.mStickyOffset = 0;
        this.isUsing = false;
        this.mIsDestroyed = false;
        this.mIsDisabled = false;
        this.mType = 0;
        this.mNeedLayoutOnAnimation = false;
        this.mDeepInComponentTree = 0;
        this.mIsAddElementToTree = false;
        this.interactionAbsoluteX = 0;
        this.interactionAbsoluteY = 0;
        this.mHasAddFocusListener = false;
        this.mTraceInfo = new WXTracing.TraceInfo();
        this.waste = false;
        this.isIgnoreInteraction = false;
        this.mPendingComponetMethodQueue = new ConcurrentLinkedQueue<>();
        this.PRESSED_ANIM_DURATION = 100;
        this.PRESSED_ANIM_DELAY = 100;
        this.ENABLED_STATE_SET = new int[]{16842910};
        this.EMPTY_STATE_SET = new int[0];
        this.PRESSED_ENABLED_STATE_SET = new int[]{16842919, 16842910};
        this.FOCUSED_ENABLED_STATE_SET = new int[]{16842908, 16842910};
        this.mElevation = 0.0f;
        this.mLazy = false;
        this.isPreventGesture = false;
        this.mInstance = wXSDKInstance;
        this.mContext = wXSDKInstance.getContext();
        this.mParent = wXVContainer;
        this.mType = i;
        if (wXSDKInstance != null) {
            setViewPortWidth(wXSDKInstance.getInstanceViewPortWidthWithFloat());
        }
        onCreate();
        ComponentObserver componentObserver = getInstance().getComponentObserver();
        if (componentObserver != null) {
            componentObserver.onCreate(this);
        }
    }

    /* access modifiers changed from: protected */
    public final void bindComponent(AbsBasicComponent absBasicComponent) {
        super.bindComponent(absBasicComponent);
        if (getInstance() != null) {
            setViewPortWidth(getInstance().getInstanceViewPortWidthWithFloat());
        }
        WXComponent wXComponent = (WXComponent) absBasicComponent;
        this.mParent = wXComponent.getParent();
        this.mType = wXComponent.getType();
    }

    /* access modifiers changed from: protected */
    public void setContentBoxMeasurement(ContentBoxMeasurement contentBoxMeasurement2) {
        this.contentBoxMeasurement = contentBoxMeasurement2;
        this.mInstance.addContentBoxMeasurement(getRenderObjectPtr(), contentBoxMeasurement2);
        WXBridgeManager.getInstance().bindMeasurementToRenderObject(getRenderObjectPtr());
    }

    public void setMarginsSupportRTL(ViewGroup.MarginLayoutParams marginLayoutParams, int i, int i2, int i3, int i4) {
        marginLayoutParams.setMargins(i, i2, i3, i4);
        if (marginLayoutParams instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) marginLayoutParams).gravity = 51;
        }
    }

    public void updateStyles(WXComponent wXComponent) {
        if (wXComponent != null) {
            updateProperties(wXComponent.getStyles());
            applyBorder(wXComponent);
        }
    }

    public void updateStyles(Map<String, Object> map) {
        if (map != null) {
            updateProperties(map);
            applyBorder(this);
        }
    }

    public void updateAttrs(AbsBasicComponent absBasicComponent) {
        if (absBasicComponent != null) {
            updateProperties(absBasicComponent.getAttrs());
        }
    }

    public void updateAttrs(Map<String, Object> map) {
        if (map != null) {
            updateProperties(map);
        }
    }

    private void applyBorder(AbsBasicComponent absBasicComponent) {
        AbsCSSShorthand border = absBasicComponent.getBorder();
        float f = border.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.LEFT);
        float f2 = border.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.TOP);
        float f3 = border.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.RIGHT);
        float f4 = border.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.BOTTOM);
        if (this.mHost != null) {
            setBorderWidth(Constants.Name.BORDER_LEFT_WIDTH, f);
            setBorderWidth(Constants.Name.BORDER_TOP_WIDTH, f2);
            setBorderWidth(Constants.Name.BORDER_RIGHT_WIDTH, f3);
            setBorderWidth(Constants.Name.BORDER_BOTTOM_WIDTH, f4);
        }
    }

    public void setPadding(AbsCSSShorthand absCSSShorthand, AbsCSSShorthand absCSSShorthand2) {
        int i = (int) (absCSSShorthand.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.LEFT) + absCSSShorthand2.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.LEFT));
        int i2 = (int) (absCSSShorthand.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.TOP) + absCSSShorthand2.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.TOP));
        int i3 = (int) (absCSSShorthand.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.RIGHT) + absCSSShorthand2.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.RIGHT));
        int i4 = (int) (absCSSShorthand.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.BOTTOM) + absCSSShorthand2.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.BOTTOM));
        if (this instanceof FlatComponent) {
            FlatComponent flatComponent = (FlatComponent) this;
            if (!flatComponent.promoteToView(true)) {
                flatComponent.getOrCreateFlatWidget().setContentBox(i, i2, i3, i4);
                return;
            }
        }
        if (this.mHost != null) {
            UniBoxShadowData uniBoxShadowData = this.mBoxShadowData;
            if (uniBoxShadowData != null) {
                if (uniBoxShadowData.getNormalLeft() > 0) {
                    int normalLeft = this.mBoxShadowData.getNormalLeft() / 2;
                    i += normalLeft;
                    i3 += normalLeft;
                }
                if (this.mBoxShadowData.getNormalTop() > 0) {
                    int normalTop = this.mBoxShadowData.getNormalTop() / 2;
                    i2 += normalTop;
                    i4 += normalTop;
                }
            }
            this.mHost.setPadding(i, i2, i3, i4);
        }
    }

    public void applyComponentEvents() {
        applyEvents();
    }

    private void applyEvents() {
        if (getEvents() != null && !getEvents().isEmpty()) {
            WXEvent events = getEvents();
            int size = events.size();
            int i = 0;
            while (i < size && i < events.size()) {
                addEvent((String) events.get(i));
                i++;
            }
            setActiveTouchListener();
        }
    }

    public void addEvent(String str) {
        if (this.mAppendEvents == null) {
            this.mAppendEvents = new HashSet();
        }
        if (!TextUtils.isEmpty(str) && !this.mAppendEvents.contains(str)) {
            View realView = getRealView();
            if (str.equals(Constants.Event.LAYEROVERFLOW)) {
                addLayerOverFlowListener(getRef());
            }
            if (str.equals(Constants.Event.CLICK)) {
                if (realView != null) {
                    if (this.mClickEventListener == null) {
                        this.mClickEventListener = new OnClickListenerImp();
                    }
                    addClickListener(this.mClickEventListener);
                } else {
                    return;
                }
            } else if (str.equals(Constants.Event.FOCUS) || str.equals(Constants.Event.BLUR)) {
                if (!this.mHasAddFocusListener) {
                    this.mHasAddFocusListener = true;
                    addFocusChangeListener(new OnFocusChangeListener() {
                        public void onFocusChange(boolean z) {
                            HashMap hashMap = new HashMap();
                            hashMap.put("timeStamp", Long.valueOf(System.currentTimeMillis()));
                            WXComponent.this.fireEvent(z ? Constants.Event.FOCUS : Constants.Event.BLUR, hashMap);
                        }
                    });
                }
            } else if (!needGestureDetector(str)) {
                Scrollable parentScroller = getParentScroller();
                if (parentScroller != null) {
                    if (str.equals(Constants.Event.APPEAR)) {
                        parentScroller.bindAppearEvent(this);
                    } else if (str.equals(Constants.Event.DISAPPEAR)) {
                        parentScroller.bindDisappearEvent(this);
                    }
                } else {
                    return;
                }
            } else if (realView != null) {
                if (realView instanceof WXGestureObservable) {
                    if (this.mGesture == null) {
                        this.mGesture = new WXGesture(this, this.mContext);
                        this.mGesture.setPreventMoveEvent(WXUtils.getBoolean(getAttrs().get(Constants.Name.PREVENT_MOVE_EVENT), false).booleanValue());
                    }
                    if (this.mGestureType == null) {
                        this.mGestureType = new HashSet();
                    }
                    if (!ViewHover.VIEW_HOVER_EVENT.equals(str)) {
                        this.mGestureType.add(str);
                    }
                    ((WXGestureObservable) realView).registerGestureListener(this.mGesture);
                } else {
                    WXLogUtils.e(realView.getClass().getSimpleName() + " don't implement WXGestureObservable, so no gesture is supported.");
                }
            } else {
                return;
            }
            this.mAppendEvents.add(str);
        }
    }

    public void interceptFocusAndBlurEvent() {
        this.mHasAddFocusListener = true;
    }

    public void bindHolder(IFComponentHolder iFComponentHolder) {
        this.mHolder = iFComponentHolder;
    }

    public WXSDKInstance getInstance() {
        return this.mInstance;
    }

    public Context getContext() {
        return this.mContext;
    }

    /* access modifiers changed from: protected */
    public final WXComponent findComponent(String str) {
        if (this.mInstance == null || str == null) {
            return null;
        }
        return WXSDKManager.getInstance().getWXRenderManager().getWXComponent(this.mInstance.getInstanceId(), str);
    }

    public void postAnimation(AbsAnimationHolder absAnimationHolder) {
        this.mAnimationHolder = absAnimationHolder;
    }

    public boolean isFlatUIEnabled() {
        return this.mParent != null && this.mParent.isFlatUIEnabled();
    }

    private class OnClickListenerImp implements OnClickListener {
        private OnClickListenerImp() {
        }

        public void onHostViewClick() {
            HashMap newHashMapWithExpectedSize = WXDataStructureUtil.newHashMapWithExpectedSize(1);
            HashMap newHashMapWithExpectedSize2 = WXDataStructureUtil.newHashMapWithExpectedSize(4);
            int[] iArr = new int[2];
            WXComponent.this.mHost.getLocationOnScreen(iArr);
            newHashMapWithExpectedSize2.put(Constants.Name.X, Float.valueOf(WXViewUtils.getWebPxByWidth((float) iArr[0], WXComponent.this.mInstance.getInstanceViewPortWidthWithFloat())));
            newHashMapWithExpectedSize2.put(Constants.Name.Y, Float.valueOf(WXViewUtils.getWebPxByWidth((float) iArr[1], WXComponent.this.mInstance.getInstanceViewPortWidthWithFloat())));
            newHashMapWithExpectedSize2.put("width", Float.valueOf(WXViewUtils.getWebPxByWidth(WXComponent.this.getLayoutWidth(), WXComponent.this.mInstance.getInstanceViewPortWidthWithFloat())));
            newHashMapWithExpectedSize2.put("height", Float.valueOf(WXViewUtils.getWebPxByWidth(WXComponent.this.getLayoutHeight(), WXComponent.this.mInstance.getInstanceViewPortWidthWithFloat())));
            newHashMapWithExpectedSize.put("position", newHashMapWithExpectedSize2);
            WXComponent.this.fireEvent(Constants.Event.CLICK, newHashMapWithExpectedSize);
        }
    }

    public String getInstanceId() {
        return this.mInstance.getInstanceId();
    }

    public Rect getComponentSize() {
        Rect rect = new Rect();
        if (!(this.mHost == null || this.mInstance.getContainerView() == null)) {
            int[] iArr = new int[2];
            int[] iArr2 = new int[2];
            this.mHost.getLocationOnScreen(iArr);
            this.mInstance.getContainerView().getLocationOnScreen(iArr2);
            int i = iArr[0] - iArr2[0];
            int i2 = (iArr[1] - this.mStickyOffset) - iArr2[1];
            rect.set(i, i2, ((int) getLayoutWidth()) + i, ((int) getLayoutHeight()) + i2);
        }
        return rect;
    }

    public final void invoke(String str, JSONArray jSONArray) {
        if (getHostView() == null || getRealView() == null) {
            this.mPendingComponetMethodQueue.offer(new UniMethodData(str, jSONArray));
            return;
        }
        Invoker methodInvoker = this.mHolder.getMethodInvoker(str);
        if (methodInvoker != null) {
            try {
                getInstance().getNativeInvokeHelper().invoke(this, methodInvoker, jSONArray);
            } catch (Exception e) {
                WXLogUtils.e("[WXComponent] updateProperties :class:" + getClass() + "method:" + methodInvoker.toString() + " function " + WXLogUtils.getStackTrace(e));
            }
        } else {
            onInvokeUnknownMethod(str, jSONArray);
        }
    }

    public final void fireEvent(String str) {
        fireEvent(str, (Map<String, Object>) null);
    }

    public final void fireEvent(String str, Map<String, Object> map) {
        if (WXUtils.getBoolean(getAttrs().get("fireEventSyn"), false).booleanValue()) {
            fireEventWait(str, map);
        } else {
            fireEvent(str, map, (Map<String, Object>) null, (EventResult) null);
        }
    }

    public final EventResult fireEventWait(String str, Map<String, Object> map) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        AnonymousClass2 r1 = new EventResult() {
            public void onCallback(Object obj) {
                super.onCallback(obj);
                countDownLatch.countDown();
            }
        };
        try {
            fireEvent(str, map, (Map<String, Object>) null, r1);
            countDownLatch.await(50, TimeUnit.MILLISECONDS);
            return r1;
        } catch (Exception e) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e("fireEventWait", (Throwable) e);
            }
            return r1;
        }
    }

    /* access modifiers changed from: protected */
    public final void fireEvent(String str, Map<String, Object> map, Map<String, Object> map2) {
        fireEvent(str, map, map2, (EventResult) null);
    }

    private final void fireEvent(String str, Map<String, Object> map, Map<String, Object> map2, EventResult eventResult) {
        String componentId;
        if (this.mInstance != null) {
            List list = null;
            if (!(getEvents() == null || getEvents().getEventBindingArgsValues() == null)) {
                list = getEvents().getEventBindingArgsValues().get(str);
            }
            List list2 = list;
            if (!(map == null || (componentId = Statements.getComponentId(this)) == null)) {
                map.put("componentId", componentId);
            }
            this.mInstance.fireEvent(getRef(), str, map, map2, list2, eventResult);
        }
    }

    public Object findTypeParent(AbsBasicComponent absBasicComponent, Class cls) {
        if (absBasicComponent.getClass() == cls) {
            return absBasicComponent;
        }
        WXComponent wXComponent = (WXComponent) absBasicComponent;
        if (wXComponent.getParent() == null) {
            return null;
        }
        findTypeParent(wXComponent.getParent(), cls);
        return null;
    }

    public boolean isLazy() {
        if (this.mLazy) {
            return true;
        }
        if (this.mParent == null || !this.mParent.isLazy()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public final void addFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        View realView;
        if (onFocusChangeListener != null && (realView = getRealView()) != null) {
            if (this.mFocusChangeListeners == null) {
                this.mFocusChangeListeners = new ArrayList();
                realView.setFocusable(true);
                realView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    public void onFocusChange(View view, boolean z) {
                        for (OnFocusChangeListener onFocusChangeListener : WXComponent.this.mFocusChangeListeners) {
                            if (onFocusChangeListener != null) {
                                onFocusChangeListener.onFocusChange(z);
                            }
                        }
                    }
                });
            }
            this.mFocusChangeListeners.add(onFocusChangeListener);
        }
    }

    /* access modifiers changed from: protected */
    public boolean ismHasFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        List<OnFocusChangeListener> list = this.mFocusChangeListeners;
        if (list != null) {
            return list.contains(onFocusChangeListener);
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public final void addClickListener(OnClickListener onClickListener) {
        View realView;
        if (onClickListener != null && (realView = getRealView()) != null) {
            if (this.mHostClickListeners == null) {
                this.mHostClickListeners = new ArrayList();
            }
            if (!realView.hasOnClickListeners()) {
                realView.setClickable(true);
                realView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (WXComponent.this.mGesture == null || !WXComponent.this.mGesture.isTouchEventConsumedByAdvancedGesture()) {
                            for (OnClickListener onClickListener : WXComponent.this.mHostClickListeners) {
                                if (onClickListener != null) {
                                    onClickListener.onHostViewClick();
                                }
                            }
                        }
                    }
                });
            }
            this.mHostClickListeners.add(onClickListener);
        }
    }

    /* access modifiers changed from: protected */
    public final void removeClickListener(OnClickListener onClickListener) {
        this.mHostClickListeners.remove(onClickListener);
    }

    public void bindData(WXComponent wXComponent) {
        bindComponentData(wXComponent);
    }

    /* access modifiers changed from: protected */
    public void bindComponentData(AbsBasicComponent absBasicComponent) {
        if (!isLazy()) {
            if (absBasicComponent == null) {
                absBasicComponent = this;
            }
            WXComponent wXComponent = (WXComponent) absBasicComponent;
            bindComponent(wXComponent);
            updateStyles(wXComponent);
            updateAttrs(absBasicComponent);
            updateExtra(absBasicComponent.getExtra());
        }
    }

    public void applyLayoutAndEvent(AbsBasicComponent absBasicComponent) {
        if (!isLazy()) {
            if (absBasicComponent == null) {
                absBasicComponent = this;
            }
            WXComponent wXComponent = (WXComponent) absBasicComponent;
            bindComponent(wXComponent);
            setSafeLayout(wXComponent);
            setPadding(absBasicComponent.getPadding(), absBasicComponent.getBorder());
            applyEvents();
        }
    }

    public void setDemission(GraphicSize graphicSize, GraphicPosition graphicPosition) {
        setLayoutPosition(graphicPosition);
        setLayoutSize(graphicSize);
    }

    public void updateDemission(float f, float f2, float f3, float f4, float f5, float f6) {
        getLayoutPosition().update(f, f2, f3, f4);
        getLayoutSize().update(f6, f5);
    }

    public void applyLayoutOnly() {
        if (!isLazy()) {
            setSafeLayout(this);
            setPadding(getPadding(), getBorder());
        }
    }

    @Deprecated
    public void updateProperties(Map<String, Object> map) {
        if (map == null) {
            return;
        }
        if ((this.mHost != null || isVirtualComponent()) && getInstance() != null) {
            for (Map.Entry next : map.entrySet()) {
                String str = (String) next.getKey();
                Object value = next.getValue();
                String string = WXUtils.getString(value, (String) null);
                if (str == null) {
                    WXExceptionUtils.commitCriticalExceptionRT(getInstanceId(), WXErrorCode.WX_RENDER_ERR_NULL_KEY, "updateProperties", WXErrorCode.WX_RENDER_ERR_NULL_KEY.getErrorMsg(), (Map<String, String>) null);
                } else {
                    if (TextUtils.isEmpty(string)) {
                        value = convertEmptyProperty(str, string);
                    }
                    if (setProperty(str, value)) {
                        continue;
                    } else if (this.mHolder != null && getInstance() != null) {
                        Invoker propertyInvoker = this.mHolder.getPropertyInvoker(str);
                        if (propertyInvoker != null) {
                            try {
                                Type[] parameterTypes = propertyInvoker.getParameterTypes();
                                if (parameterTypes.length != 1) {
                                    WXLogUtils.e("[WXComponent] setX method only one parameter：" + propertyInvoker);
                                    return;
                                }
                                propertyInvoker.invoke(this, WXReflectionUtils.parseArgument(parameterTypes[0], value));
                            } catch (Exception e) {
                                WXLogUtils.e("[WXComponent] updateProperties :class:" + getClass() + "method:" + propertyInvoker.toString() + " function " + WXLogUtils.getStackTrace(e));
                            }
                        } else {
                            continue;
                        }
                    } else {
                        return;
                    }
                }
            }
            readyToRender();
            if ((this instanceof FlatComponent) && this.mBackgroundDrawable != null) {
                FlatComponent flatComponent = (FlatComponent) this;
                if (!flatComponent.promoteToView(true) && !(flatComponent.getOrCreateFlatWidget() instanceof AndroidViewWidget)) {
                    flatComponent.getOrCreateFlatWidget().setBackgroundAndBorder(this.mBackgroundDrawable);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:229:0x03e0, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setProperty(java.lang.String r6, java.lang.Object r7) {
        /*
            r5 = this;
            r0 = 1
            if (r6 == 0) goto L_0x03f4
            com.taobao.weex.WXSDKInstance r1 = r5.getInstance()
            if (r1 != 0) goto L_0x000b
            goto L_0x03f4
        L_0x000b:
            r6.hashCode()
            r1 = -1
            int r2 = r6.hashCode()
            r3 = 0
            switch(r2) {
                case -1989576717: goto L_0x0324;
                case -1974639039: goto L_0x0319;
                case -1971292586: goto L_0x030e;
                case -1501175880: goto L_0x0303;
                case -1470826662: goto L_0x02f8;
                case -1455888984: goto L_0x02ed;
                case -1452542531: goto L_0x02e2;
                case -1383228885: goto L_0x02d7;
                case -1375815020: goto L_0x02c9;
                case -1308858324: goto L_0x02bb;
                case -1293920646: goto L_0x02ad;
                case -1290574193: goto L_0x029f;
                case -1267206133: goto L_0x0291;
                case -1228066334: goto L_0x0283;
                case -1221029593: goto L_0x0275;
                case -1111969773: goto L_0x0267;
                case -1081309778: goto L_0x0259;
                case -1063257157: goto L_0x024b;
                case -1044792121: goto L_0x023d;
                case -975171706: goto L_0x022f;
                case -906066005: goto L_0x0221;
                case -863700117: goto L_0x0213;
                case -806339567: goto L_0x0205;
                case -289173127: goto L_0x01f7;
                case -242276144: goto L_0x01e9;
                case -227338466: goto L_0x01db;
                case -223992013: goto L_0x01cd;
                case -133587431: goto L_0x01bf;
                case 115029: goto L_0x01b1;
                case 3145721: goto L_0x01a3;
                case 3317767: goto L_0x0195;
                case 3506294: goto L_0x0187;
                case 90130308: goto L_0x0179;
                case 108511772: goto L_0x016b;
                case 113126854: goto L_0x015c;
                case 202355100: goto L_0x014e;
                case 270940796: goto L_0x0140;
                case 333432965: goto L_0x0132;
                case 400381634: goto L_0x0124;
                case 581268560: goto L_0x0116;
                case 588239831: goto L_0x0108;
                case 713848971: goto L_0x00fa;
                case 717381201: goto L_0x00ec;
                case 722830999: goto L_0x00de;
                case 737768677: goto L_0x00d0;
                case 741115130: goto L_0x00c2;
                case 743055051: goto L_0x00b4;
                case 747463061: goto L_0x00a6;
                case 747804969: goto L_0x0098;
                case 975087886: goto L_0x008a;
                case 1287124693: goto L_0x007c;
                case 1292595405: goto L_0x006e;
                case 1349188574: goto L_0x0060;
                case 1744216035: goto L_0x0052;
                case 1767100401: goto L_0x0044;
                case 1860657097: goto L_0x0036;
                case 1941332754: goto L_0x0027;
                case 1970934485: goto L_0x0019;
                default: goto L_0x0017;
            }
        L_0x0017:
            goto L_0x032e
        L_0x0019:
            java.lang.String r2 = "marginLeft"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0023
            goto L_0x032e
        L_0x0023:
            r1 = 57
            goto L_0x032e
        L_0x0027:
            java.lang.String r2 = "visibility"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0032
            goto L_0x032e
        L_0x0032:
            r1 = 56
            goto L_0x032e
        L_0x0036:
            java.lang.String r2 = "justifyContent"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0040
            goto L_0x032e
        L_0x0040:
            r1 = 55
            goto L_0x032e
        L_0x0044:
            java.lang.String r2 = "alignSelf"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x004e
            goto L_0x032e
        L_0x004e:
            r1 = 54
            goto L_0x032e
        L_0x0052:
            java.lang.String r2 = "flexWrap"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x005c
            goto L_0x032e
        L_0x005c:
            r1 = 53
            goto L_0x032e
        L_0x0060:
            java.lang.String r2 = "borderRadius"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x006a
            goto L_0x032e
        L_0x006a:
            r1 = 52
            goto L_0x032e
        L_0x006e:
            java.lang.String r2 = "backgroundImage"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0078
            goto L_0x032e
        L_0x0078:
            r1 = 51
            goto L_0x032e
        L_0x007c:
            java.lang.String r2 = "backgroundColor"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0086
            goto L_0x032e
        L_0x0086:
            r1 = 50
            goto L_0x032e
        L_0x008a:
            java.lang.String r2 = "marginRight"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0094
            goto L_0x032e
        L_0x0094:
            r1 = 49
            goto L_0x032e
        L_0x0098:
            java.lang.String r2 = "position"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x00a2
            goto L_0x032e
        L_0x00a2:
            r1 = 48
            goto L_0x032e
        L_0x00a6:
            java.lang.String r2 = "fixedSize"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x00b0
            goto L_0x032e
        L_0x00b0:
            r1 = 47
            goto L_0x032e
        L_0x00b4:
            java.lang.String r2 = "boxShadow"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x00be
            goto L_0x032e
        L_0x00be:
            r1 = 46
            goto L_0x032e
        L_0x00c2:
            java.lang.String r2 = "borderWidth"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x00cc
            goto L_0x032e
        L_0x00cc:
            r1 = 45
            goto L_0x032e
        L_0x00d0:
            java.lang.String r2 = "borderStyle"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x00da
            goto L_0x032e
        L_0x00da:
            r1 = 44
            goto L_0x032e
        L_0x00de:
            java.lang.String r2 = "borderColor"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x00e8
            goto L_0x032e
        L_0x00e8:
            r1 = 43
            goto L_0x032e
        L_0x00ec:
            java.lang.String r2 = "preventMoveEvent"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x00f6
            goto L_0x032e
        L_0x00f6:
            r1 = 42
            goto L_0x032e
        L_0x00fa:
            java.lang.String r2 = "paddingRight"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0104
            goto L_0x032e
        L_0x0104:
            r1 = 41
            goto L_0x032e
        L_0x0108:
            java.lang.String r2 = "borderBottomRightRadius"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0112
            goto L_0x032e
        L_0x0112:
            r1 = 40
            goto L_0x032e
        L_0x0116:
            java.lang.String r2 = "borderBottomLeftRadius"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0120
            goto L_0x032e
        L_0x0120:
            r1 = 39
            goto L_0x032e
        L_0x0124:
            java.lang.String r2 = "maxWidth"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x012e
            goto L_0x032e
        L_0x012e:
            r1 = 38
            goto L_0x032e
        L_0x0132:
            java.lang.String r2 = "borderTopRightRadius"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x013c
            goto L_0x032e
        L_0x013c:
            r1 = 37
            goto L_0x032e
        L_0x0140:
            java.lang.String r2 = "disabled"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x014a
            goto L_0x032e
        L_0x014a:
            r1 = 36
            goto L_0x032e
        L_0x014e:
            java.lang.String r2 = "paddingBottom"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0158
            goto L_0x032e
        L_0x0158:
            r1 = 35
            goto L_0x032e
        L_0x015c:
            java.lang.String r2 = "width"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0167
            goto L_0x032e
        L_0x0167:
            r1 = 34
            goto L_0x032e
        L_0x016b:
            java.lang.String r2 = "right"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0175
            goto L_0x032e
        L_0x0175:
            r1 = 33
            goto L_0x032e
        L_0x0179:
            java.lang.String r2 = "paddingTop"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0183
            goto L_0x032e
        L_0x0183:
            r1 = 32
            goto L_0x032e
        L_0x0187:
            java.lang.String r2 = "role"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0191
            goto L_0x032e
        L_0x0191:
            r1 = 31
            goto L_0x032e
        L_0x0195:
            java.lang.String r2 = "left"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x019f
            goto L_0x032e
        L_0x019f:
            r1 = 30
            goto L_0x032e
        L_0x01a3:
            java.lang.String r2 = "flex"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x01ad
            goto L_0x032e
        L_0x01ad:
            r1 = 29
            goto L_0x032e
        L_0x01b1:
            java.lang.String r2 = "top"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x01bb
            goto L_0x032e
        L_0x01bb:
            r1 = 28
            goto L_0x032e
        L_0x01bf:
            java.lang.String r2 = "minHeight"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x01c9
            goto L_0x032e
        L_0x01c9:
            r1 = 27
            goto L_0x032e
        L_0x01cd:
            java.lang.String r2 = "borderLeftWidth"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x01d7
            goto L_0x032e
        L_0x01d7:
            r1 = 26
            goto L_0x032e
        L_0x01db:
            java.lang.String r2 = "borderLeftStyle"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x01e5
            goto L_0x032e
        L_0x01e5:
            r1 = 25
            goto L_0x032e
        L_0x01e9:
            java.lang.String r2 = "borderLeftColor"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x01f3
            goto L_0x032e
        L_0x01f3:
            r1 = 24
            goto L_0x032e
        L_0x01f7:
            java.lang.String r2 = "marginBottom"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0201
            goto L_0x032e
        L_0x0201:
            r1 = 23
            goto L_0x032e
        L_0x0205:
            java.lang.String r2 = "padding"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x020f
            goto L_0x032e
        L_0x020f:
            r1 = 22
            goto L_0x032e
        L_0x0213:
            java.lang.String r2 = "ariaLabel"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x021d
            goto L_0x032e
        L_0x021d:
            r1 = 21
            goto L_0x032e
        L_0x0221:
            java.lang.String r2 = "maxHeight"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x022b
            goto L_0x032e
        L_0x022b:
            r1 = 20
            goto L_0x032e
        L_0x022f:
            java.lang.String r2 = "flexDirection"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0239
            goto L_0x032e
        L_0x0239:
            r1 = 19
            goto L_0x032e
        L_0x023d:
            java.lang.String r2 = "marginTop"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0247
            goto L_0x032e
        L_0x0247:
            r1 = 18
            goto L_0x032e
        L_0x024b:
            java.lang.String r2 = "alignItems"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0255
            goto L_0x032e
        L_0x0255:
            r1 = 17
            goto L_0x032e
        L_0x0259:
            java.lang.String r2 = "margin"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0263
            goto L_0x032e
        L_0x0263:
            r1 = 16
            goto L_0x032e
        L_0x0267:
            java.lang.String r2 = "ariaHidden"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0271
            goto L_0x032e
        L_0x0271:
            r1 = 15
            goto L_0x032e
        L_0x0275:
            java.lang.String r2 = "height"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x027f
            goto L_0x032e
        L_0x027f:
            r1 = 14
            goto L_0x032e
        L_0x0283:
            java.lang.String r2 = "borderTopLeftRadius"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x028d
            goto L_0x032e
        L_0x028d:
            r1 = 13
            goto L_0x032e
        L_0x0291:
            java.lang.String r2 = "opacity"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x029b
            goto L_0x032e
        L_0x029b:
            r1 = 12
            goto L_0x032e
        L_0x029f:
            java.lang.String r2 = "borderBottomWidth"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x02a9
            goto L_0x032e
        L_0x02a9:
            r1 = 11
            goto L_0x032e
        L_0x02ad:
            java.lang.String r2 = "borderBottomStyle"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x02b7
            goto L_0x032e
        L_0x02b7:
            r1 = 10
            goto L_0x032e
        L_0x02bb:
            java.lang.String r2 = "borderBottomColor"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x02c5
            goto L_0x032e
        L_0x02c5:
            r1 = 9
            goto L_0x032e
        L_0x02c9:
            java.lang.String r2 = "minWidth"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x02d3
            goto L_0x032e
        L_0x02d3:
            r1 = 8
            goto L_0x032e
        L_0x02d7:
            java.lang.String r2 = "bottom"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x02e0
            goto L_0x032e
        L_0x02e0:
            r1 = 7
            goto L_0x032e
        L_0x02e2:
            java.lang.String r2 = "borderTopWidth"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x02eb
            goto L_0x032e
        L_0x02eb:
            r1 = 6
            goto L_0x032e
        L_0x02ed:
            java.lang.String r2 = "borderTopStyle"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x02f6
            goto L_0x032e
        L_0x02f6:
            r1 = 5
            goto L_0x032e
        L_0x02f8:
            java.lang.String r2 = "borderTopColor"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0301
            goto L_0x032e
        L_0x0301:
            r1 = 4
            goto L_0x032e
        L_0x0303:
            java.lang.String r2 = "paddingLeft"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x030c
            goto L_0x032e
        L_0x030c:
            r1 = 3
            goto L_0x032e
        L_0x030e:
            java.lang.String r2 = "borderRightWidth"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0317
            goto L_0x032e
        L_0x0317:
            r1 = 2
            goto L_0x032e
        L_0x0319:
            java.lang.String r2 = "borderRightStyle"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0322
            goto L_0x032e
        L_0x0322:
            r1 = 1
            goto L_0x032e
        L_0x0324:
            java.lang.String r2 = "borderRightColor"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x032d
            goto L_0x032e
        L_0x032d:
            r1 = 0
        L_0x032e:
            java.lang.String r2 = ""
            r4 = 0
            switch(r1) {
                case 0: goto L_0x03eb;
                case 1: goto L_0x03e1;
                case 2: goto L_0x03e0;
                case 3: goto L_0x03e0;
                case 4: goto L_0x03eb;
                case 5: goto L_0x03e1;
                case 6: goto L_0x03e0;
                case 7: goto L_0x03e0;
                case 8: goto L_0x03e0;
                case 9: goto L_0x03eb;
                case 10: goto L_0x03e1;
                case 11: goto L_0x03e0;
                case 12: goto L_0x03cd;
                case 13: goto L_0x03bf;
                case 14: goto L_0x03e0;
                case 15: goto L_0x03af;
                case 16: goto L_0x03e0;
                case 17: goto L_0x03e0;
                case 18: goto L_0x03e0;
                case 19: goto L_0x03e0;
                case 20: goto L_0x03e0;
                case 21: goto L_0x03a7;
                case 22: goto L_0x03e0;
                case 23: goto L_0x03e0;
                case 24: goto L_0x03eb;
                case 25: goto L_0x03e1;
                case 26: goto L_0x03e0;
                case 27: goto L_0x03e0;
                case 28: goto L_0x03e0;
                case 29: goto L_0x03e0;
                case 30: goto L_0x03e0;
                case 31: goto L_0x039f;
                case 32: goto L_0x03e0;
                case 33: goto L_0x03e0;
                case 34: goto L_0x03e0;
                case 35: goto L_0x03e0;
                case 36: goto L_0x0388;
                case 37: goto L_0x03bf;
                case 38: goto L_0x03e0;
                case 39: goto L_0x03bf;
                case 40: goto L_0x03bf;
                case 41: goto L_0x03e0;
                case 42: goto L_0x0374;
                case 43: goto L_0x03eb;
                case 44: goto L_0x03e1;
                case 45: goto L_0x03e0;
                case 46: goto L_0x036b;
                case 47: goto L_0x0361;
                case 48: goto L_0x0357;
                case 49: goto L_0x03e0;
                case 50: goto L_0x034d;
                case 51: goto L_0x033f;
                case 52: goto L_0x03bf;
                case 53: goto L_0x03e0;
                case 54: goto L_0x03e0;
                case 55: goto L_0x03e0;
                case 56: goto L_0x0335;
                case 57: goto L_0x03e0;
                default: goto L_0x0334;
            }
        L_0x0334:
            return r3
        L_0x0335:
            java.lang.String r6 = com.taobao.weex.utils.WXUtils.getString(r7, r4)
            if (r6 == 0) goto L_0x033e
            r5.setVisibility(r6)
        L_0x033e:
            return r0
        L_0x033f:
            java.lang.String r6 = com.taobao.weex.utils.WXUtils.getString(r7, r4)
            if (r6 == 0) goto L_0x034c
            T r7 = r5.mHost
            if (r7 == 0) goto L_0x034c
            r5.setBackgroundImage(r6)
        L_0x034c:
            return r0
        L_0x034d:
            java.lang.String r6 = com.taobao.weex.utils.WXUtils.getString(r7, r4)
            if (r6 == 0) goto L_0x0356
            r5.setBackgroundColor(r6)
        L_0x0356:
            return r0
        L_0x0357:
            java.lang.String r6 = com.taobao.weex.utils.WXUtils.getString(r7, r4)
            if (r6 == 0) goto L_0x0360
            r5.setSticky(r6)
        L_0x0360:
            return r0
        L_0x0361:
            java.lang.String r6 = "m"
            java.lang.String r6 = com.taobao.weex.utils.WXUtils.getString(r7, r6)
            r5.setFixedSize(r6)
            return r0
        L_0x036b:
            r5.getHostView()     // Catch:{ all -> 0x036f }
            goto L_0x0373
        L_0x036f:
            r6 = move-exception
            r6.printStackTrace()
        L_0x0373:
            return r0
        L_0x0374:
            com.taobao.weex.ui.view.gesture.WXGesture r6 = r5.mGesture
            if (r6 == 0) goto L_0x0387
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r3)
            java.lang.Boolean r7 = com.taobao.weex.utils.WXUtils.getBoolean(r7, r1)
            boolean r7 = r7.booleanValue()
            r6.setPreventMoveEvent(r7)
        L_0x0387:
            return r0
        L_0x0388:
            java.lang.Boolean r6 = com.taobao.weex.utils.WXUtils.getBoolean(r7, r4)
            if (r6 == 0) goto L_0x039e
            boolean r7 = r6.booleanValue()
            r5.setDisabled(r7)
            boolean r6 = r6.booleanValue()
            java.lang.String r7 = ":disabled"
            r5.setPseudoClassStatus(r7, r6)
        L_0x039e:
            return r0
        L_0x039f:
            java.lang.String r6 = com.taobao.weex.utils.WXUtils.getString(r7, r2)
            r5.setRole(r6)
            return r0
        L_0x03a7:
            java.lang.String r6 = com.taobao.weex.utils.WXUtils.getString(r7, r2)
            r5.setAriaLabel(r6)
            return r0
        L_0x03af:
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r3)
            java.lang.Boolean r6 = com.taobao.weex.utils.WXUtils.getBoolean(r7, r6)
            boolean r6 = r6.booleanValue()
            r5.setAriaHidden(r6)
            return r0
        L_0x03bf:
            java.lang.Float r7 = com.taobao.weex.utils.WXUtils.getFloat(r7, r4)
            if (r7 == 0) goto L_0x03cc
            float r7 = r7.floatValue()
            r5.setBorderRadius(r6, r7)
        L_0x03cc:
            return r0
        L_0x03cd:
            r6 = 1065353216(0x3f800000, float:1.0)
            java.lang.Float r6 = java.lang.Float.valueOf(r6)
            java.lang.Float r6 = com.taobao.weex.utils.WXUtils.getFloat(r7, r6)
            if (r6 == 0) goto L_0x03e0
            float r6 = r6.floatValue()
            r5.setOpacity(r6)
        L_0x03e0:
            return r0
        L_0x03e1:
            java.lang.String r7 = com.taobao.weex.utils.WXUtils.getString(r7, r4)
            if (r7 == 0) goto L_0x03ea
            r5.setBorderStyle(r6, r7)
        L_0x03ea:
            return r0
        L_0x03eb:
            java.lang.String r7 = com.taobao.weex.utils.WXUtils.getString(r7, r4)
            if (r7 == 0) goto L_0x03f4
            r5.setBorderColor(r6, r7)
        L_0x03f4:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.WXComponent.setProperty(java.lang.String, java.lang.Object):boolean");
    }

    @WXComponentProp(name = "elevation")
    public void setElevation(String str) {
        initElevation(str);
    }

    /* access modifiers changed from: protected */
    public void initElevation(String str) {
        if (!TextUtils.isEmpty(str) && Build.VERSION.SDK_INT > 20) {
            this.mElevation = WXViewUtils.getRealSubPxByWidth(WXUtils.getFloat(str, Float.valueOf(0.0f)).floatValue(), this.mInstance.getInstanceViewPortWidthWithFloat());
            if (this.mBoxShadowData != null) {
                clearBoxShadow();
            }
            if (Build.VERSION.SDK_INT == 21) {
                getHostView().setElevation(this.mElevation);
                return;
            }
            StateListAnimator stateListAnimator = new StateListAnimator();
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(ObjectAnimator.ofFloat(getHostView(), Constants.Name.ELEVATION, new float[]{this.mElevation}).setDuration(0)).with(ObjectAnimator.ofFloat(getHostView(), View.TRANSLATION_Z, new float[]{0.0f}).setDuration(this.PRESSED_ANIM_DURATION));
            animatorSet.setInterpolator(new FastOutLinearInInterpolator());
            stateListAnimator.addState(this.PRESSED_ENABLED_STATE_SET, animatorSet);
            AnimatorSet animatorSet2 = new AnimatorSet();
            animatorSet2.play(ObjectAnimator.ofFloat(getHostView(), Constants.Name.ELEVATION, new float[]{this.mElevation}).setDuration(0)).with(ObjectAnimator.ofFloat(getHostView(), View.TRANSLATION_Z, new float[]{0.0f}).setDuration(this.PRESSED_ANIM_DURATION));
            animatorSet2.setInterpolator(new FastOutLinearInInterpolator());
            stateListAnimator.addState(this.FOCUSED_ENABLED_STATE_SET, animatorSet2);
            AnimatorSet animatorSet3 = new AnimatorSet();
            ArrayList arrayList = new ArrayList();
            arrayList.add(ObjectAnimator.ofFloat(getHostView(), Constants.Name.ELEVATION, new float[]{this.mElevation}).setDuration(0));
            if (Build.VERSION.SDK_INT >= 22 && Build.VERSION.SDK_INT <= 24) {
                arrayList.add(ObjectAnimator.ofFloat(getHostView(), View.TRANSLATION_Z, new float[]{getHostView().getTranslationZ()}).setDuration(this.PRESSED_ANIM_DELAY));
            }
            arrayList.add(ObjectAnimator.ofFloat(getHostView(), View.TRANSLATION_Z, new float[]{0.0f}).setDuration(this.PRESSED_ANIM_DURATION));
            animatorSet3.playSequentially((Animator[]) arrayList.toArray(new ObjectAnimator[0]));
            animatorSet3.setInterpolator(new FastOutLinearInInterpolator());
            stateListAnimator.addState(this.ENABLED_STATE_SET, animatorSet3);
            AnimatorSet animatorSet4 = new AnimatorSet();
            animatorSet4.play(ObjectAnimator.ofFloat(getHostView(), Constants.Name.ELEVATION, new float[]{0.0f}).setDuration(0)).with(ObjectAnimator.ofFloat(getHostView(), View.TRANSLATION_Z, new float[]{0.0f}).setDuration(0));
            animatorSet4.setInterpolator(new FastOutLinearInInterpolator());
            stateListAnimator.addState(this.EMPTY_STATE_SET, animatorSet4);
            getHostView().setStateListAnimator(stateListAnimator);
        }
    }

    /* access modifiers changed from: protected */
    public BorderDrawable getOrCreateBorder() {
        if (this.mBackgroundDrawable == null) {
            this.mBackgroundDrawable = new BorderDrawable();
            T t = this.mHost;
            if (t != null) {
                WXViewUtils.setBackGround(t, (Drawable) null, this);
                if (this.mRippleBackground == null) {
                    if (this.mBoxShadowDrawable != null) {
                        WXViewUtils.setBackGround(this.mHost, new LayerDrawable(new Drawable[]{this.mBoxShadowDrawable, this.mBackgroundDrawable}), this);
                    } else if (this.mInsetBoxShadowDrawable != null) {
                        WXViewUtils.setBackGround(this.mHost, new LayerDrawable(new Drawable[]{this.mBackgroundDrawable, this.mInsetBoxShadowDrawable}), this);
                    } else {
                        WXViewUtils.setBackGround(this.mHost, this.mBackgroundDrawable, this);
                    }
                } else if (this.mBoxShadowDrawable != null) {
                    WXViewUtils.setBackGround(this.mHost, new LayerDrawable(new Drawable[]{this.mRippleBackground, this.mBoxShadowDrawable, this.mBackgroundDrawable}), this);
                } else if (this.mInsetBoxShadowDrawable != null) {
                    WXViewUtils.setBackGround(this.mHost, new LayerDrawable(new Drawable[]{this.mRippleBackground, this.mBackgroundDrawable, this.mInsetBoxShadowDrawable}), this);
                } else {
                    WXViewUtils.setBackGround(this.mHost, new LayerDrawable(new Drawable[]{this.mRippleBackground, this.mBackgroundDrawable}), this);
                }
            }
        }
        return this.mBackgroundDrawable;
    }

    public void setSafeLayout(WXComponent wXComponent) {
        if (!TextUtils.isEmpty(wXComponent.getComponentType()) && !TextUtils.isEmpty(wXComponent.getRef()) && wXComponent.getLayoutPosition() != null && wXComponent.getLayoutSize() != null) {
            setLayout(wXComponent);
        }
    }

    public void setLayout(WXComponent wXComponent) {
        int i;
        int i2;
        float f;
        WXComponent wXComponent2 = wXComponent;
        setLayoutSize(wXComponent.getLayoutSize());
        setLayoutPosition(wXComponent.getLayoutPosition());
        setPaddings(wXComponent.getPadding());
        setMargins(wXComponent.getMargin());
        setBorders(wXComponent.getBorder());
        boolean isLayoutRTL = wXComponent.isLayoutRTL();
        setIsLayoutRTL(isLayoutRTL);
        if (isLayoutRTL != wXComponent2.isLastLayoutDirectionRTL) {
            wXComponent2.isLastLayoutDirectionRTL = isLayoutRTL;
            layoutDirectionDidChanged(isLayoutRTL);
        }
        float f2 = 0.0f;
        try {
            if (!(getLayoutSize().getWidth() == 0.0f || getLayoutSize().getHeight() == 0.0f)) {
                parseAnimation();
            }
        } catch (Exception unused) {
        }
        int i3 = 0;
        boolean z = this.mParent == null;
        if (!z) {
            i3 = this.mParent.getChildrenLayoutTopOffset();
        }
        CSSShorthand cSSShorthand = z ? new CSSShorthand() : this.mParent.getPadding();
        CSSShorthand cSSShorthand2 = z ? new CSSShorthand() : this.mParent.getBorder();
        int width = (int) getLayoutSize().getWidth();
        int height = (int) getLayoutSize().getHeight();
        if (isFixed()) {
            i2 = (int) (getLayoutPosition().getLeft() - ((float) getInstance().getRenderContainerPaddingLeft()));
            i = ((int) (getLayoutPosition().getTop() - ((float) getInstance().getRenderContainerPaddingTop()))) + i3;
        } else {
            i = ((int) ((getLayoutPosition().getTop() - cSSShorthand.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.TOP)) - cSSShorthand2.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.TOP))) + i3;
            i2 = (int) ((getLayoutPosition().getLeft() - cSSShorthand.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.LEFT)) - cSSShorthand2.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.LEFT));
        }
        int i4 = (int) getMargin().get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.RIGHT);
        int i5 = (int) getMargin().get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.BOTTOM);
        Point point = new Point((int) getLayoutPosition().getLeft(), (int) getLayoutPosition().getTop());
        if (this.mPreRealWidth != width || this.mPreRealHeight != height || this.mPreRealLeft != i2 || this.mPreRealRight != i4 || this.mPreRealTop != i) {
            if ((this instanceof WXCell) && height >= WXPerformance.VIEW_LIMIT_HEIGHT && width >= WXPerformance.VIEW_LIMIT_WIDTH) {
                this.mInstance.getApmForInstance().updateDiffStats(WXInstanceApm.KEY_PAGE_STATS_CELL_EXCEED_NUM, 1.0d);
                this.mInstance.getWXPerformance().cellExceedNum++;
            }
            if (z) {
                f = 0.0f;
            } else {
                f = ((float) this.mParent.getAbsoluteY()) + getCSSLayoutTop();
            }
            this.mAbsoluteY = (int) f;
            if (!z) {
                f2 = ((float) this.mParent.getAbsoluteX()) + getCSSLayoutLeft();
            }
            this.mAbsoluteX = (int) f2;
            T t = this.mHost;
            if (t != null) {
                if (!(t instanceof ViewGroup) && this.mAbsoluteY + height > this.mInstance.getWeexHeight() + 1) {
                    if (!this.mInstance.mEnd) {
                        this.mInstance.onOldFsRenderTimeLogic();
                    }
                    if (!this.mInstance.isNewFsEnd) {
                        this.mInstance.isNewFsEnd = true;
                        this.mInstance.getApmForInstance().arriveNewFsRenderTime();
                    }
                }
                MeasureOutput measure = measure(width, height);
                setComponentLayoutParams(measure.width, measure.height, i2, i, i4, i5, point);
            }
        }
    }

    private void setComponentLayoutParams(int i, int i2, int i3, int i4, int i5, int i6, Point point) {
        int i7;
        int i8;
        int i9;
        int i10;
        Widget widget;
        int i11 = i;
        int i12 = i2;
        int i13 = i3;
        int i14 = i4;
        if (getInstance() != null && !getInstance().isDestroy()) {
            updateBoxShadow(i, i12, false);
            UniBoxShadowData uniBoxShadowData = this.mBoxShadowData;
            if (uniBoxShadowData == null || uniBoxShadowData.getNormalShadows().size() <= 0) {
                i10 = i11;
                i9 = i12;
                i8 = i13;
                i7 = i14;
            } else {
                int normalMaxWidth = this.mBoxShadowData.getNormalMaxWidth();
                i10 = normalMaxWidth;
                i9 = this.mBoxShadowData.getNormalMaxHeight();
                i8 = i13 - (this.mBoxShadowData.getNormalLeft() / 2);
                i7 = i14 - (this.mBoxShadowData.getNormalTop() / 2);
            }
            FlatGUIContext flatUIContext = getInstance().getFlatUIContext();
            if (flatUIContext != null && flatUIContext.getFlatComponentAncestor(this) != null) {
                if (this instanceof FlatComponent) {
                    FlatComponent flatComponent = (FlatComponent) this;
                    if (!flatComponent.promoteToView(true)) {
                        widget = flatComponent.getOrCreateFlatWidget();
                        setWidgetParams(widget, flatUIContext, point, i10, i9, i8, i5, i7, i6);
                    }
                }
                widget = flatUIContext.getAndroidViewWidget(this);
                setWidgetParams(widget, flatUIContext, point, i10, i9, i8, i5, i7, i6);
            } else if (this.mHost != null) {
                if (isFixed()) {
                    setFixedHostLayoutParams(this.mHost, i10, i9, i8, i5, i7, i6);
                } else {
                    setHostLayoutParams(this.mHost, i10, i9, i8, i5, i7, i6);
                }
                recordInteraction(i, i2);
                this.mPreRealWidth = i11;
                this.mPreRealHeight = i12;
                this.mPreRealLeft = i13;
                this.mPreRealRight = i5;
                this.mPreRealTop = i14;
                onFinishLayout();
            }
        }
    }

    private void recordInteraction(int i, int i2) {
        if (this.mIsAddElementToTree) {
            boolean z = false;
            this.mIsAddElementToTree = false;
            if (this.mParent == null) {
                this.interactionAbsoluteX = 0;
                this.interactionAbsoluteY = 0;
            } else {
                float cSSLayoutTop = getCSSLayoutTop();
                float cSSLayoutLeft = getCSSLayoutLeft();
                if (!isFixed()) {
                    cSSLayoutLeft += (float) this.mParent.interactionAbsoluteX;
                }
                this.interactionAbsoluteX = (int) cSSLayoutLeft;
                if (!isFixed()) {
                    cSSLayoutTop += (float) this.mParent.interactionAbsoluteY;
                }
                this.interactionAbsoluteY = (int) cSSLayoutTop;
            }
            if (getInstance().getApmForInstance().instanceRect == null) {
                getInstance().getApmForInstance().instanceRect = new Rect();
            }
            Rect rect = getInstance().getApmForInstance().instanceRect;
            rect.set(0, 0, this.mInstance.getWeexWidth(), this.mInstance.getWeexHeight());
            if (rect.contains(this.interactionAbsoluteX, this.interactionAbsoluteY) || rect.contains(this.interactionAbsoluteX + i, this.interactionAbsoluteY) || rect.contains(this.interactionAbsoluteX, this.interactionAbsoluteY + i2) || rect.contains(this.interactionAbsoluteX + i, this.interactionAbsoluteY + i2)) {
                z = true;
            }
            this.mInstance.onChangeElement(this, !z);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x00ae  */
    /* JADX WARNING: Removed duplicated region for block: B:28:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setWidgetParams(com.taobao.weex.ui.flat.widget.Widget r15, com.taobao.weex.ui.flat.FlatGUIContext r16, android.graphics.Point r17, int r18, int r19, int r20, int r21, int r22, int r23) {
        /*
            r14 = this;
            r9 = r14
            r10 = r15
            r0 = r16
            r1 = r17
            android.graphics.Point r11 = new android.graphics.Point
            r11.<init>()
            com.taobao.weex.ui.component.WXVContainer r2 = r9.mParent
            if (r2 == 0) goto L_0x008b
            com.taobao.weex.ui.component.WXVContainer r2 = r9.mParent
            boolean r2 = r2 instanceof com.taobao.weex.ui.flat.FlatComponent
            if (r2 == 0) goto L_0x0031
            com.taobao.weex.ui.component.WXVContainer r2 = r9.mParent
            com.taobao.weex.ui.flat.WidgetContainer r2 = r0.getFlatComponentAncestor(r2)
            if (r2 == 0) goto L_0x0031
            com.taobao.weex.ui.component.WXVContainer r2 = r9.mParent
            com.taobao.weex.ui.flat.widget.AndroidViewWidget r2 = r0.getAndroidViewWidget(r2)
            if (r2 != 0) goto L_0x0031
            int r2 = r1.x
            int r1 = r1.y
            r11.set(r2, r1)
            r12 = r20
            r13 = r22
            goto L_0x0038
        L_0x0031:
            r12 = r20
            r13 = r22
            r11.set(r12, r13)
        L_0x0038:
            com.taobao.weex.ui.component.WXVContainer r1 = r9.mParent
            boolean r1 = r1 instanceof com.taobao.weex.ui.flat.FlatComponent
            if (r1 == 0) goto L_0x0061
            com.taobao.weex.ui.component.WXVContainer r1 = r9.mParent
            com.taobao.weex.ui.flat.WidgetContainer r1 = r0.getFlatComponentAncestor(r1)
            if (r1 == 0) goto L_0x0061
            com.taobao.weex.ui.component.WXVContainer r1 = r9.mParent
            com.taobao.weex.ui.flat.widget.AndroidViewWidget r0 = r0.getAndroidViewWidget(r1)
            if (r0 != 0) goto L_0x0061
            com.taobao.weex.ui.component.WXVContainer r0 = r9.mParent
            com.taobao.weex.ui.flat.FlatComponent r0 = (com.taobao.weex.ui.flat.FlatComponent) r0
            com.taobao.weex.ui.flat.widget.Widget r0 = r0.getOrCreateFlatWidget()
            android.graphics.Point r0 = r0.getLocInFlatContainer()
            int r1 = r0.x
            int r0 = r0.y
            r11.offset(r1, r0)
        L_0x0061:
            com.taobao.weex.ui.component.WXVContainer r0 = r9.mParent
            T r2 = r9.mHost
            r1 = r14
            r3 = r18
            r4 = r19
            r5 = r20
            r6 = r21
            r7 = r22
            r8 = r23
            android.view.ViewGroup$LayoutParams r0 = r0.getChildLayoutParams(r1, r2, r3, r4, r5, r6, r7, r8)
            boolean r1 = r0 instanceof android.view.ViewGroup.MarginLayoutParams
            if (r1 == 0) goto L_0x008f
            int r1 = r0.width
            int r2 = r0.height
            android.view.ViewGroup$MarginLayoutParams r0 = (android.view.ViewGroup.MarginLayoutParams) r0
            int r3 = r0.leftMargin
            int r4 = r0.rightMargin
            int r5 = r0.topMargin
            int r0 = r0.bottomMargin
            r12 = r3
            r13 = r5
            goto L_0x0097
        L_0x008b:
            r12 = r20
            r13 = r22
        L_0x008f:
            r1 = r18
            r2 = r19
            r4 = r21
            r0 = r23
        L_0x0097:
            r16 = r15
            r17 = r1
            r18 = r2
            r19 = r12
            r20 = r4
            r21 = r13
            r22 = r0
            r23 = r11
            r16.setLayout(r17, r18, r19, r20, r21, r22, r23)
            boolean r3 = r10 instanceof com.taobao.weex.ui.flat.widget.AndroidViewWidget
            if (r3 == 0) goto L_0x00d1
            r3 = r10
            com.taobao.weex.ui.flat.widget.AndroidViewWidget r3 = (com.taobao.weex.ui.flat.widget.AndroidViewWidget) r3
            android.view.View r5 = r3.getView()
            if (r5 == 0) goto L_0x00d1
            android.view.View r3 = r3.getView()
            int r5 = r11.x
            int r6 = r11.y
            r15 = r14
            r16 = r3
            r17 = r1
            r18 = r2
            r19 = r5
            r20 = r4
            r21 = r6
            r22 = r0
            r15.setHostLayoutParams(r16, r17, r18, r19, r20, r21, r22)
        L_0x00d1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.WXComponent.setWidgetParams(com.taobao.weex.ui.flat.widget.Widget, com.taobao.weex.ui.flat.FlatGUIContext, android.graphics.Point, int, int, int, int, int, int):void");
    }

    /* JADX WARNING: type inference failed for: r1v1, types: [android.view.ViewGroup$MarginLayoutParams] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setHostLayoutParams(T r11, int r12, int r13, int r14, int r15, int r16, int r17) {
        /*
            r10 = this;
            r9 = r10
            com.taobao.weex.ui.component.WXVContainer r0 = r9.mParent
            if (r0 != 0) goto L_0x0018
            android.widget.FrameLayout$LayoutParams r6 = new android.widget.FrameLayout$LayoutParams
            r3 = r12
            r4 = r13
            r6.<init>(r12, r13)
            r0 = r10
            r1 = r6
            r2 = r14
            r3 = r16
            r4 = r15
            r5 = r17
            r0.setMarginsSupportRTL(r1, r2, r3, r4, r5)
            goto L_0x0028
        L_0x0018:
            r3 = r12
            r4 = r13
            com.taobao.weex.ui.component.WXVContainer r0 = r9.mParent
            r1 = r10
            r2 = r11
            r5 = r14
            r6 = r15
            r7 = r16
            r8 = r17
            android.view.ViewGroup$LayoutParams r6 = r0.getChildLayoutParams(r1, r2, r3, r4, r5, r6, r7, r8)
        L_0x0028:
            if (r6 == 0) goto L_0x002e
            r0 = r11
            r11.setLayoutParams(r6)
        L_0x002e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.WXComponent.setHostLayoutParams(android.view.View, int, int, int, int, int, int):void");
    }

    private void setFixedHostLayoutParams(T t, int i, int i2, int i3, int i4, int i5, int i6) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.width = i;
        layoutParams.height = i2;
        setMarginsSupportRTL(layoutParams, i3, i5, i4, i6);
        t.setLayoutParams(layoutParams);
        this.mInstance.moveFixedView(t);
        if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.d("Weex_Fixed_Style", "WXComponent:setLayout :" + i3 + Operators.SPACE_STR + i5 + Operators.SPACE_STR + i + Operators.SPACE_STR + i2);
            StringBuilder sb = new StringBuilder();
            sb.append("WXComponent:setLayout Left:");
            sb.append(getStyles().getLeft());
            sb.append(Operators.SPACE_STR);
            sb.append((int) getStyles().getTop());
            WXLogUtils.d("Weex_Fixed_Style", sb.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void updateBoxShadow(int i, int i2, boolean z) {
        UniBoxShadowData parseBoxShadow;
        LayerDrawable layerDrawable;
        int i3;
        int i4;
        int i5;
        int i6 = i;
        int i7 = i2;
        if (!UniBoxShadowUtil.isBoxShadowEnabled() || this.mElevation > 0.0f || getStyles().get(Constants.Name.ELEVATION) != null || getAttrs().get(Constants.Name.ELEVATION) != null) {
            WXLogUtils.w("BoxShadow", "box-shadow disabled");
            if (this.mBoxShadowData != null) {
                clearBoxShadow();
            }
        } else if (getStyles() != null) {
            Object obj = getStyles().get(Constants.Name.BOX_SHADOW);
            Object obj2 = getAttrs().get(Constants.Name.SHADOW_QUALITY);
            if (obj != null && i6 > 0 && i7 > 0) {
                float floatValue = WXUtils.getFloat(obj2, Float.valueOf(0.5f)).floatValue();
                float instanceViewPortWidthWithFloat = getInstance().getInstanceViewPortWidthWithFloat();
                float[] fArr = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
                WXStyle styles = getStyles();
                if (styles != null) {
                    float floatValue2 = WXUtils.getFloat(styles.get(Constants.Name.BORDER_TOP_LEFT_RADIUS), Float.valueOf(0.0f)).floatValue();
                    fArr[0] = WXViewUtils.getRealSubPxByWidth(floatValue2, instanceViewPortWidthWithFloat);
                    fArr[1] = WXViewUtils.getRealSubPxByWidth(floatValue2, instanceViewPortWidthWithFloat);
                    float floatValue3 = WXUtils.getFloat(styles.get(Constants.Name.BORDER_TOP_RIGHT_RADIUS), Float.valueOf(0.0f)).floatValue();
                    fArr[2] = WXViewUtils.getRealSubPxByWidth(floatValue3, instanceViewPortWidthWithFloat);
                    fArr[3] = WXViewUtils.getRealSubPxByWidth(floatValue3, instanceViewPortWidthWithFloat);
                    float floatValue4 = WXUtils.getFloat(styles.get(Constants.Name.BORDER_BOTTOM_RIGHT_RADIUS), Float.valueOf(0.0f)).floatValue();
                    fArr[4] = WXViewUtils.getRealSubPxByWidth(floatValue4, instanceViewPortWidthWithFloat);
                    fArr[5] = WXViewUtils.getRealSubPxByWidth(floatValue4, instanceViewPortWidthWithFloat);
                    float floatValue5 = WXUtils.getFloat(styles.get(Constants.Name.BORDER_BOTTOM_LEFT_RADIUS), Float.valueOf(0.0f)).floatValue();
                    fArr[6] = WXViewUtils.getRealSubPxByWidth(floatValue5, instanceViewPortWidthWithFloat);
                    fArr[7] = WXViewUtils.getRealSubPxByWidth(floatValue5, instanceViewPortWidthWithFloat);
                    if (styles.containsKey(Constants.Name.BORDER_RADIUS)) {
                        float floatValue6 = WXUtils.getFloat(styles.get(Constants.Name.BORDER_RADIUS), Float.valueOf(0.0f)).floatValue();
                        for (int i8 = 0; i8 < 8; i8++) {
                            fArr[i8] = WXViewUtils.getRealSubPxByWidth(floatValue6, instanceViewPortWidthWithFloat);
                        }
                    }
                }
                UniBoxShadowData uniBoxShadowData = this.mBoxShadowData;
                if ((uniBoxShadowData == null || !uniBoxShadowData.equalsUniBoxShadowData(obj.toString(), i6, i7, fArr)) && (parseBoxShadow = UniBoxShadowUtil.parseBoxShadow(i, i2, obj.toString(), fArr, instanceViewPortWidthWithFloat, floatValue)) != null) {
                    this.mBoxShadowData = parseBoxShadow;
                    UniNormalBoxShadowDrawable normalBoxShadowDrawable = UniBoxShadowUtil.getNormalBoxShadowDrawable(parseBoxShadow, getContext().getResources());
                    UniInsetBoxShadowLayer insetBoxShadowDrawable = UniBoxShadowUtil.getInsetBoxShadowDrawable(parseBoxShadow);
                    if (normalBoxShadowDrawable != null || insetBoxShadowDrawable != null) {
                        if (!(normalBoxShadowDrawable == null || parseBoxShadow == null || !z || this.mHost == null)) {
                            int normalMaxWidth = parseBoxShadow.getNormalMaxWidth();
                            int normalMaxHeight = parseBoxShadow.getNormalMaxHeight();
                            boolean z2 = this.mParent == null;
                            CSSShorthand cSSShorthand = z2 ? new CSSShorthand() : this.mParent.getPadding();
                            CSSShorthand cSSShorthand2 = z2 ? new CSSShorthand() : this.mParent.getBorder();
                            if (z2) {
                                i3 = 0;
                            } else {
                                i3 = this.mParent.getChildrenLayoutTopOffset();
                            }
                            if (isFixed()) {
                                i5 = (int) (getLayoutPosition().getLeft() - ((float) getInstance().getRenderContainerPaddingLeft()));
                                i4 = ((int) (getLayoutPosition().getTop() - ((float) getInstance().getRenderContainerPaddingTop()))) + i3;
                            } else {
                                i4 = ((int) ((getLayoutPosition().getTop() - cSSShorthand.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.TOP)) - cSSShorthand2.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.TOP))) + i3;
                                i5 = (int) ((getLayoutPosition().getLeft() - cSSShorthand.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.LEFT)) - cSSShorthand2.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.LEFT));
                            }
                            int i9 = (int) getMargin().get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.RIGHT);
                            int i10 = (int) getMargin().get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.BOTTOM);
                            int normalLeft = i5 - (parseBoxShadow.getNormalLeft() / 2);
                            int normalTop = i4 - (parseBoxShadow.getNormalTop() / 2);
                            if (isFixed()) {
                                setFixedHostLayoutParams(this.mHost, normalMaxWidth, normalMaxHeight, normalLeft, i9, normalTop, i10);
                            } else {
                                setHostLayoutParams(this.mHost, normalMaxWidth, normalMaxHeight, normalLeft, i9, normalTop, i10);
                            }
                            setPadding(getPadding(), getBorder());
                        }
                        if (this.mBackgroundDrawable != null) {
                            clearBoxShadow();
                            if (this.mRippleBackground == null) {
                                if (normalBoxShadowDrawable != null) {
                                    layerDrawable = new LayerDrawable(new Drawable[]{normalBoxShadowDrawable, this.mBackgroundDrawable});
                                } else if (insetBoxShadowDrawable != null) {
                                    layerDrawable = new LayerDrawable(new Drawable[]{this.mBackgroundDrawable, insetBoxShadowDrawable});
                                } else {
                                    layerDrawable = new LayerDrawable(new Drawable[]{this.mBackgroundDrawable});
                                }
                            } else if (normalBoxShadowDrawable != null) {
                                layerDrawable = new LayerDrawable(new Drawable[]{this.mRippleBackground, normalBoxShadowDrawable, this.mBackgroundDrawable});
                            } else if (insetBoxShadowDrawable != null) {
                                layerDrawable = new LayerDrawable(new Drawable[]{this.mRippleBackground, this.mBackgroundDrawable, insetBoxShadowDrawable});
                            } else {
                                layerDrawable = new LayerDrawable(new Drawable[]{this.mRippleBackground, this.mBackgroundDrawable});
                            }
                            WXViewUtils.setBackGround(getHostView(), layerDrawable, this);
                            this.mBoxShadowData = parseBoxShadow;
                            this.mBoxShadowDrawable = normalBoxShadowDrawable;
                            this.mInsetBoxShadowDrawable = insetBoxShadowDrawable;
                            this.mBackgroundDrawable.updateBoxShadowData(parseBoxShadow);
                            return;
                        }
                        clearBoxShadow();
                        this.mBoxShadowData = parseBoxShadow;
                        this.mBoxShadowDrawable = normalBoxShadowDrawable;
                        this.mInsetBoxShadowDrawable = insetBoxShadowDrawable;
                        getOrCreateBorder().updateBoxShadowData(parseBoxShadow);
                    }
                }
            }
        } else {
            WXLogUtils.w("Can not resolve styles");
        }
    }

    /* access modifiers changed from: protected */
    public void clearBoxShadow() {
        if (!UniBoxShadowUtil.isBoxShadowEnabled()) {
            WXLogUtils.w("BoxShadow", "box-shadow disabled");
            return;
        }
        BorderDrawable borderDrawable = this.mBackgroundDrawable;
        if (borderDrawable != null) {
            borderDrawable.updateBoxShadowData((UniBoxShadowData) null);
        }
        if (!(getHostView() == null || this.mBackgroundDrawable == null || !(getHostView().getBackground() instanceof LayerDrawable))) {
            if (this.mRippleBackground == null) {
                WXViewUtils.setBackGround(getHostView(), new LayerDrawable(new Drawable[]{this.mBackgroundDrawable}), this);
            } else {
                WXViewUtils.setBackGround(getHostView(), new LayerDrawable(new Drawable[]{this.mRippleBackground, this.mBackgroundDrawable}), this);
            }
        }
        if (this.mBoxShadowData != null) {
            this.mBoxShadowData = null;
        }
        UniNormalBoxShadowDrawable uniNormalBoxShadowDrawable = this.mBoxShadowDrawable;
        if (uniNormalBoxShadowDrawable != null) {
            if (uniNormalBoxShadowDrawable.getBitmap() != null) {
                this.mBoxShadowDrawable.getBitmap().recycle();
            }
            this.mBoxShadowDrawable = null;
        }
        if (this.mInsetBoxShadowDrawable != null) {
            this.mInsetBoxShadowDrawable = null;
        }
    }

    /* access modifiers changed from: protected */
    public void onFinishLayout() {
        Object obj = getStyles() != null ? getStyles().get(Constants.Name.BACKGROUND_IMAGE) : null;
        if (obj != null) {
            setBackgroundImage(obj.toString());
        }
    }

    /* access modifiers changed from: protected */
    public MeasureOutput measure(int i, int i2) {
        MeasureOutput measureOutput = new MeasureOutput();
        int i3 = this.mFixedProp;
        if (i3 != 0) {
            measureOutput.width = i3;
            measureOutput.height = this.mFixedProp;
        } else {
            measureOutput.width = i;
            measureOutput.height = i2;
        }
        return measureOutput;
    }

    /* access modifiers changed from: protected */
    public void setAriaHidden(boolean z) {
        View hostView = getHostView();
        if (hostView != null && Build.VERSION.SDK_INT >= 16) {
            hostView.setImportantForAccessibility(z ? 2 : 1);
        }
    }

    /* access modifiers changed from: protected */
    public void setAriaLabel(String str) {
        View hostView = getHostView();
        if (hostView != null) {
            hostView.setContentDescription(str);
        }
    }

    /* access modifiers changed from: protected */
    public void setRole(final String str) {
        View hostView = getHostView();
        if (hostView != null && !TextUtils.isEmpty(str)) {
            IWXAccessibilityRoleAdapter accessibilityRoleAdapter = WXSDKManager.getInstance().getAccessibilityRoleAdapter();
            if (accessibilityRoleAdapter != null) {
                str = accessibilityRoleAdapter.getRole(str);
            }
            ViewCompat.setAccessibilityDelegate(hostView, new AccessibilityDelegateCompat() {
                public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                    try {
                        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                        accessibilityNodeInfoCompat.setRoleDescription(str);
                    } catch (Throwable unused) {
                        WXLogUtils.e("SetRole failed!");
                    }
                }
            });
        }
    }

    private void setFixedSize(String str) {
        ViewGroup.LayoutParams layoutParams;
        if (PROP_FS_MATCH_PARENT.equals(str)) {
            this.mFixedProp = -1;
        } else if (PROP_FS_WRAP_CONTENT.equals(str)) {
            this.mFixedProp = -2;
        } else {
            this.mFixedProp = 0;
            return;
        }
        T t = this.mHost;
        if (t != null && (layoutParams = t.getLayoutParams()) != null) {
            layoutParams.height = this.mFixedProp;
            layoutParams.width = this.mFixedProp;
            this.mHost.setLayoutParams(layoutParams);
        }
    }

    public View getRealView() {
        return this.mHost;
    }

    private boolean needGestureDetector(String str) {
        if (this.mHost != null) {
            for (WXGestureType.LowLevelGesture obj : WXGestureType.LowLevelGesture.values()) {
                if (str.equals(obj.toString())) {
                    return true;
                }
            }
            for (WXGestureType.HighLevelGesture obj2 : WXGestureType.HighLevelGesture.values()) {
                if (str.equals(obj2.toString())) {
                    return true;
                }
            }
        }
        return WXGesture.isStopPropagation(str) || str.equals(ViewHover.VIEW_HOVER_EVENT) || isPreventGesture();
    }

    public Scrollable getParentScroller() {
        boolean equals;
        WXVContainer wXVContainer = this;
        do {
            WXVContainer parent = wXVContainer.getParent();
            if (parent == null) {
                return null;
            }
            if (parent instanceof Scrollable) {
                return (Scrollable) parent;
            }
            equals = parent.getRef().equals(ROOT);
            wXVContainer = parent;
        } while (!equals);
        return null;
    }

    public Scrollable getFirstScroller() {
        if (this instanceof Scrollable) {
            return (Scrollable) this;
        }
        return null;
    }

    public WXVContainer getParent() {
        return this.mParent;
    }

    public final void createView() {
        if (!isLazy()) {
            createViewImpl();
        }
    }

    /* access modifiers changed from: protected */
    public void createViewImpl() {
        Context context = this.mContext;
        if (context != null) {
            T initComponentHostView = initComponentHostView(context);
            this.mHost = initComponentHostView;
            if (initComponentHostView == null && !isVirtualComponent()) {
                initView();
            }
            T t = this.mHost;
            if (t != null) {
                if (t.getId() == -1) {
                    this.mHost.setId(WXViewUtils.generateViewId());
                }
                ComponentObserver componentObserver = getInstance().getComponentObserver();
                if (componentObserver != null) {
                    componentObserver.onViewCreated(this, this.mHost);
                }
                invokePendingComponetMethod();
            }
            onHostViewInitialized(this.mHost);
            return;
        }
        WXLogUtils.e("createViewImpl", "Context is null");
    }

    private final void invokePendingComponetMethod() {
        if (this.mPendingComponetMethodQueue.size() > 0) {
            WXBridgeManager.getInstance().post(new Runnable() {
                public void run() {
                    while (!WXComponent.this.mPendingComponetMethodQueue.isEmpty()) {
                        UniMethodData uniMethodData = (UniMethodData) WXComponent.this.mPendingComponetMethodQueue.poll();
                        if (uniMethodData != null) {
                            WXComponent.this.invoke(uniMethodData.getMethod(), uniMethodData.getArgs());
                        }
                    }
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public void initView() {
        Context context = this.mContext;
        if (context != null) {
            T initComponentHostView = initComponentHostView(context);
            this.mHost = initComponentHostView;
            if (initComponentHostView != null) {
                invokePendingComponetMethod();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onHostViewInitialized(T t) {
        AbsAnimationHolder absAnimationHolder = this.mAnimationHolder;
        if (absAnimationHolder != null) {
            absAnimationHolder.execute(this.mInstance, this);
        }
        setActiveTouchListener();
    }

    public T getHostView() {
        return this.mHost;
    }

    @Deprecated
    public View getView() {
        return this.mHost;
    }

    public int getAbsoluteY() {
        return this.mAbsoluteY;
    }

    public int getAbsoluteX() {
        return this.mAbsoluteX;
    }

    public void removeEvent(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (str.equals(Constants.Event.LAYEROVERFLOW)) {
                removeLayerOverFlowListener(getRef());
            }
            if (getEvents() != null) {
                getEvents().remove(str);
            }
            Set<String> set = this.mAppendEvents;
            if (set != null) {
                set.remove(str);
            }
            Set<String> set2 = this.mGestureType;
            if (set2 != null) {
                set2.remove(str);
            }
            removeEventFromView(str);
        }
    }

    /* access modifiers changed from: protected */
    public void removeEventFromView(String str) {
        if (!(!str.equals(Constants.Event.CLICK) || getRealView() == null || this.mHostClickListeners == null)) {
            if (this.mClickEventListener == null) {
                this.mClickEventListener = new OnClickListenerImp();
            }
            this.mHostClickListeners.remove(this.mClickEventListener);
            if (this.mHostClickListeners.size() < 1) {
                getRealView().setOnClickListener((View.OnClickListener) null);
                getRealView().setClickable(false);
            }
        }
        Scrollable parentScroller = getParentScroller();
        if (str.equals(Constants.Event.APPEAR) && parentScroller != null) {
            parentScroller.unbindAppearEvent(this);
        }
        if (str.equals(Constants.Event.DISAPPEAR) && parentScroller != null) {
            parentScroller.unbindDisappearEvent(this);
        }
    }

    public void removeAllEvent() {
        if (getEvents().size() >= 1) {
            WXEvent events = getEvents();
            int size = events.size();
            int i = 0;
            while (i < size && i < events.size()) {
                String str = (String) events.get(i);
                if (str != null) {
                    removeEventFromView(str);
                }
                i++;
            }
            Set<String> set = this.mAppendEvents;
            if (set != null) {
                set.clear();
            }
            Set<String> set2 = this.mGestureType;
            if (set2 != null) {
                set2.clear();
            }
            this.mGesture = null;
            if (getRealView() != null && (getRealView() instanceof WXGestureObservable)) {
                ((WXGestureObservable) getRealView()).registerGestureListener((WXGesture) null);
            }
            T t = this.mHost;
            if (t != null) {
                t.setOnFocusChangeListener((View.OnFocusChangeListener) null);
                List<OnClickListener> list = this.mHostClickListeners;
                if (list != null && list.size() > 0) {
                    this.mHostClickListeners.clear();
                    this.mHost.setOnClickListener((View.OnClickListener) null);
                }
            }
        }
    }

    public final void removeStickyStyle() {
        Scrollable parentScroller;
        if (isSticky() && (parentScroller = getParentScroller()) != null) {
            parentScroller.unbindStickStyle(this);
        }
    }

    public boolean isSticky() {
        return getStyles().isSticky();
    }

    public boolean isFixed() {
        return getStyles().isFixed();
    }

    public void setDisabled(boolean z) {
        this.mIsDisabled = z;
        T t = this.mHost;
        if (t != null) {
            t.setEnabled(!z);
        }
    }

    public boolean isDisabled() {
        return this.mIsDisabled;
    }

    public void setSticky(String str) {
        Scrollable parentScroller;
        if (!TextUtils.isEmpty(str) && str.equals("sticky") && (parentScroller = getParentScroller()) != null) {
            parentScroller.bindStickStyle(this);
        }
    }

    public void setBackgroundColor(String str) {
        LayerDrawable layerDrawable;
        if (!TextUtils.isEmpty(str)) {
            int color = WXResourceUtils.getColor(str);
            if (isRippleEnabled() && Build.VERSION.SDK_INT >= 21) {
                Drawable prepareBackgroundRipple = prepareBackgroundRipple();
                this.mRippleBackground = prepareBackgroundRipple;
                if (prepareBackgroundRipple != null) {
                    if (this.mBackgroundDrawable == null) {
                        WXViewUtils.setBackGround(this.mHost, prepareBackgroundRipple, this);
                        return;
                    }
                    if (this.mBoxShadowDrawable != null) {
                        layerDrawable = new LayerDrawable(new Drawable[]{this.mRippleBackground, this.mBoxShadowDrawable, this.mBackgroundDrawable, this.mInsetBoxShadowDrawable});
                    } else if (this.mInsetBoxShadowDrawable != null) {
                        layerDrawable = new LayerDrawable(new Drawable[]{this.mRippleBackground, this.mBackgroundDrawable, this.mInsetBoxShadowDrawable});
                    } else {
                        layerDrawable = new LayerDrawable(new Drawable[]{this.mRippleBackground, this.mBackgroundDrawable});
                    }
                    WXViewUtils.setBackGround(this.mHost, layerDrawable, this);
                    return;
                }
            }
            if (color != 0 || this.mBackgroundDrawable != null) {
                getOrCreateBorder().setColor(color);
            }
        }
    }

    private Drawable prepareBackgroundRipple() {
        int i;
        try {
            if (!(getStyles() == null || getStyles().getPesudoResetStyles() == null)) {
                Map<String, Object> pesudoResetStyles = getStyles().getPesudoResetStyles();
                Object obj = pesudoResetStyles.get("backgroundColor");
                if (obj != null) {
                    i = WXResourceUtils.getColor(obj.toString(), 0);
                    if (i == 0) {
                        return null;
                    }
                } else {
                    i = 0;
                }
                Object obj2 = pesudoResetStyles.get("backgroundColor:active");
                if (obj2 == null) {
                    return null;
                }
                int color = WXResourceUtils.getColor(obj2.toString(), i);
                if (Build.VERSION.SDK_INT >= 21) {
                    return new RippleDrawable(new ColorStateList(new int[][]{new int[0]}, new int[]{color}), new ColorDrawable(i), (Drawable) null) {
                        public void draw(Canvas canvas) {
                            if (WXComponent.this.mBackgroundDrawable != null) {
                                canvas.clipPath(WXComponent.this.mBackgroundDrawable.getContentPath(new RectF(0.0f, 0.0f, (float) canvas.getWidth(), (float) canvas.getHeight())));
                            }
                            super.draw(canvas);
                        }
                    };
                }
            }
        } catch (Throwable th) {
            WXLogUtils.w("Exception on create ripple: ", th);
        }
        return null;
    }

    public void setBackgroundImage(String str) {
        if ("".equals(str.trim())) {
            getOrCreateBorder().setImage((Shader) null);
            return;
        }
        getOrCreateBorder().setImage(WXResourceUtils.getShader(str, getLayoutSize().getWidth(), getLayoutSize().getHeight()));
    }

    private boolean shouldCancelHardwareAccelerate() {
        IWXConfigAdapter wxConfigAdapter = WXSDKManager.getInstance().getWxConfigAdapter();
        boolean z = true;
        if (wxConfigAdapter != null) {
            try {
                z = Boolean.parseBoolean(wxConfigAdapter.getConfig("android_weex_test_gpu", "cancel_hardware_accelerate", AbsoluteConst.TRUE));
            } catch (Exception e) {
                WXLogUtils.e(WXLogUtils.getStackTrace(e));
            }
            WXLogUtils.i("cancel_hardware_accelerate : " + z);
        }
        return z;
    }

    public void setOpacity(float f) {
        if (f >= 0.0f && f <= 1.0f && this.mHost.getAlpha() != f) {
            int openGLRenderLimitValue = WXRenderManager.getOpenGLRenderLimitValue();
            if (isLayerTypeEnabled()) {
                this.mHost.setLayerType(2, (Paint) null);
            }
            if (isLayerTypeEnabled() && shouldCancelHardwareAccelerate() && openGLRenderLimitValue > 0) {
                float f2 = (float) openGLRenderLimitValue;
                if (getLayoutHeight() > f2 || getLayoutWidth() > f2) {
                    this.mHost.setLayerType(0, (Paint) null);
                }
            }
            this.mHost.setAlpha(f);
        }
    }

    public void setBorderRadius(String str, float f) {
        if (f >= 0.0f) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -1228066334:
                    if (str.equals(Constants.Name.BORDER_TOP_LEFT_RADIUS)) {
                        c = 0;
                        break;
                    }
                    break;
                case 333432965:
                    if (str.equals(Constants.Name.BORDER_TOP_RIGHT_RADIUS)) {
                        c = 1;
                        break;
                    }
                    break;
                case 581268560:
                    if (str.equals(Constants.Name.BORDER_BOTTOM_LEFT_RADIUS)) {
                        c = 2;
                        break;
                    }
                    break;
                case 588239831:
                    if (str.equals(Constants.Name.BORDER_BOTTOM_RIGHT_RADIUS)) {
                        c = 3;
                        break;
                    }
                    break;
                case 1349188574:
                    if (str.equals(Constants.Name.BORDER_RADIUS)) {
                        c = 4;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    getOrCreateBorder().setBorderRadius(CSSShorthand.CORNER.BORDER_TOP_LEFT, WXViewUtils.getRealSubPxByWidth(f, this.mInstance.getInstanceViewPortWidthWithFloat()));
                    return;
                case 1:
                    getOrCreateBorder().setBorderRadius(CSSShorthand.CORNER.BORDER_TOP_RIGHT, WXViewUtils.getRealSubPxByWidth(f, this.mInstance.getInstanceViewPortWidthWithFloat()));
                    return;
                case 2:
                    getOrCreateBorder().setBorderRadius(CSSShorthand.CORNER.BORDER_BOTTOM_LEFT, WXViewUtils.getRealSubPxByWidth(f, this.mInstance.getInstanceViewPortWidthWithFloat()));
                    return;
                case 3:
                    getOrCreateBorder().setBorderRadius(CSSShorthand.CORNER.BORDER_BOTTOM_RIGHT, WXViewUtils.getRealSubPxByWidth(f, this.mInstance.getInstanceViewPortWidthWithFloat()));
                    return;
                case 4:
                    getOrCreateBorder().setBorderRadius(CSSShorthand.CORNER.ALL, WXViewUtils.getRealSubPxByWidth(f, this.mInstance.getInstanceViewPortWidthWithFloat()));
                    return;
                default:
                    return;
            }
        }
    }

    private void initOutlineProvider(final float f) {
        if (useFeature() && (getHostView() instanceof BaseFrameLayout)) {
            getHostView().setOutlineProvider(new ViewOutlineProvider() {
                public void getOutline(View view, Outline outline) {
                    if (!WXComponent.this.getInstance().isDestroy()) {
                        int width = view.getWidth();
                        int height = view.getHeight();
                        if (width != 0 && height != 0 && WXComponent.this.getOrCreateBorder().isRounded()) {
                            Rect rect = new Rect(0, 0, width, height);
                            float f = ((float) width) / (f * 2.0f);
                            if (Float.isNaN(f) || f >= 1.0f) {
                                outline.setRoundRect(rect, f);
                            } else {
                                outline.setRoundRect(rect, f * f);
                            }
                        }
                    }
                }
            });
            getHostView().setClipToOutline(true);
        }
    }

    public boolean useFeature() {
        return Build.VERSION.SDK_INT >= 24;
    }

    public void setBorderWidth(String str, float f) {
        if (f >= 0.0f) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -1971292586:
                    if (str.equals(Constants.Name.BORDER_RIGHT_WIDTH)) {
                        c = 0;
                        break;
                    }
                    break;
                case -1452542531:
                    if (str.equals(Constants.Name.BORDER_TOP_WIDTH)) {
                        c = 1;
                        break;
                    }
                    break;
                case -1290574193:
                    if (str.equals(Constants.Name.BORDER_BOTTOM_WIDTH)) {
                        c = 2;
                        break;
                    }
                    break;
                case -223992013:
                    if (str.equals(Constants.Name.BORDER_LEFT_WIDTH)) {
                        c = 3;
                        break;
                    }
                    break;
                case 741115130:
                    if (str.equals(Constants.Name.BORDER_WIDTH)) {
                        c = 4;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    getOrCreateBorder().setBorderWidth(CSSShorthand.EDGE.RIGHT, f);
                    return;
                case 1:
                    getOrCreateBorder().setBorderWidth(CSSShorthand.EDGE.TOP, f);
                    return;
                case 2:
                    getOrCreateBorder().setBorderWidth(CSSShorthand.EDGE.BOTTOM, f);
                    return;
                case 3:
                    getOrCreateBorder().setBorderWidth(CSSShorthand.EDGE.LEFT, f);
                    return;
                case 4:
                    getOrCreateBorder().setBorderWidth(CSSShorthand.EDGE.ALL, f);
                    return;
                default:
                    return;
            }
        }
    }

    public void setBorderStyle(String str, String str2) {
        if (!TextUtils.isEmpty(str2)) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -1974639039:
                    if (str.equals(Constants.Name.BORDER_RIGHT_STYLE)) {
                        c = 0;
                        break;
                    }
                    break;
                case -1455888984:
                    if (str.equals(Constants.Name.BORDER_TOP_STYLE)) {
                        c = 1;
                        break;
                    }
                    break;
                case -1293920646:
                    if (str.equals(Constants.Name.BORDER_BOTTOM_STYLE)) {
                        c = 2;
                        break;
                    }
                    break;
                case -227338466:
                    if (str.equals(Constants.Name.BORDER_LEFT_STYLE)) {
                        c = 3;
                        break;
                    }
                    break;
                case 737768677:
                    if (str.equals(Constants.Name.BORDER_STYLE)) {
                        c = 4;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    getOrCreateBorder().setBorderStyle(CSSShorthand.EDGE.RIGHT, str2);
                    return;
                case 1:
                    getOrCreateBorder().setBorderStyle(CSSShorthand.EDGE.TOP, str2);
                    return;
                case 2:
                    getOrCreateBorder().setBorderStyle(CSSShorthand.EDGE.BOTTOM, str2);
                    return;
                case 3:
                    getOrCreateBorder().setBorderStyle(CSSShorthand.EDGE.LEFT, str2);
                    return;
                case 4:
                    getOrCreateBorder().setBorderStyle(CSSShorthand.EDGE.ALL, str2);
                    return;
                default:
                    return;
            }
        }
    }

    public void setBorderColor(String str, String str2) {
        int color;
        if (!TextUtils.isEmpty(str2) && (color = WXResourceUtils.getColor(str2)) != Integer.MIN_VALUE) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -1989576717:
                    if (str.equals(Constants.Name.BORDER_RIGHT_COLOR)) {
                        c = 0;
                        break;
                    }
                    break;
                case -1470826662:
                    if (str.equals(Constants.Name.BORDER_TOP_COLOR)) {
                        c = 1;
                        break;
                    }
                    break;
                case -1308858324:
                    if (str.equals(Constants.Name.BORDER_BOTTOM_COLOR)) {
                        c = 2;
                        break;
                    }
                    break;
                case -242276144:
                    if (str.equals(Constants.Name.BORDER_LEFT_COLOR)) {
                        c = 3;
                        break;
                    }
                    break;
                case 722830999:
                    if (str.equals(Constants.Name.BORDER_COLOR)) {
                        c = 4;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    getOrCreateBorder().setBorderColor(CSSShorthand.EDGE.RIGHT, color);
                    return;
                case 1:
                    getOrCreateBorder().setBorderColor(CSSShorthand.EDGE.TOP, color);
                    return;
                case 2:
                    getOrCreateBorder().setBorderColor(CSSShorthand.EDGE.BOTTOM, color);
                    return;
                case 3:
                    getOrCreateBorder().setBorderColor(CSSShorthand.EDGE.LEFT, color);
                    return;
                case 4:
                    getOrCreateBorder().setBorderColor(CSSShorthand.EDGE.ALL, color);
                    return;
                default:
                    return;
            }
        }
    }

    public String getVisibility() {
        try {
            return (String) getStyles().get(Constants.Name.VISIBILITY);
        } catch (Exception unused) {
            return "visible";
        }
    }

    public void setVisibility(String str) {
        View realView = getRealView();
        if (realView == null) {
            return;
        }
        if (TextUtils.equals(str, "visible")) {
            realView.setVisibility(0);
        } else if (TextUtils.equals(str, "hidden")) {
            realView.setVisibility(8);
        }
    }

    private void updateElevation() {
        float elevation = getAttrs().getElevation(getInstance().getInstanceViewPortWidthWithFloat());
        if (!Float.isNaN(elevation)) {
            ViewCompat.setElevation(getHostView(), elevation);
        }
    }

    public void onActivityDestroy() {
        ConcurrentLinkedQueue<UniMethodData> concurrentLinkedQueue = this.mPendingComponetMethodQueue;
        if (concurrentLinkedQueue != null) {
            concurrentLinkedQueue.clear();
        }
    }

    public void recycled() {
        if (isFixed()) {
        }
    }

    public void destroy() {
        View hostView;
        if (getInstance() != null) {
            clearBoxShadow();
            ComponentObserver componentObserver = getInstance().getComponentObserver();
            if (componentObserver != null) {
                componentObserver.onPreDestory(this);
            }
            if (!WXEnvironment.isApkDebugable() || WXUtils.isUiThread()) {
                T t = this.mHost;
                if (t != null && t.getLayerType() == 2 && isLayerTypeEnabled()) {
                    this.mHost.setLayerType(0, (Paint) null);
                }
                removeAllEvent();
                removeStickyStyle();
                if (isFixed() && (hostView = getHostView()) != null) {
                    getInstance().removeFixedView(hostView);
                }
                ContentBoxMeasurement contentBoxMeasurement2 = this.contentBoxMeasurement;
                if (contentBoxMeasurement2 != null) {
                    contentBoxMeasurement2.destroy();
                    this.contentBoxMeasurement = null;
                }
                this.mIsDestroyed = true;
                ConcurrentLinkedQueue<Pair<String, Map<String, Object>>> concurrentLinkedQueue = this.animations;
                if (concurrentLinkedQueue != null) {
                    concurrentLinkedQueue.clear();
                }
                ViewHover viewHover = this.mHover;
                if (viewHover != null) {
                    viewHover.destroy();
                    this.mHover = null;
                }
                this.mInstance = null;
                List<OnFocusChangeListener> list = this.mFocusChangeListeners;
                if (list != null) {
                    list.clear();
                }
                List<OnClickListener> list2 = this.mHostClickListeners;
                if (list2 != null) {
                    list2.clear();
                    return;
                }
                return;
            }
            throw new WXRuntimeException("[WXComponent] destroy can only be called in main thread");
        }
    }

    public boolean isDestoryed() {
        return this.mIsDestroyed;
    }

    public View detachViewAndClearPreInfo() {
        T t = this.mHost;
        this.mPreRealLeft = 0;
        this.mPreRealRight = 0;
        this.mPreRealWidth = 0;
        this.mPreRealHeight = 0;
        this.mPreRealTop = 0;
        return t;
    }

    public void clearPreLayout() {
        this.mPreRealLeft = 0;
        this.mPreRealRight = 0;
        this.mPreRealWidth = 0;
        this.mPreRealHeight = 0;
        this.mPreRealTop = 0;
    }

    public void computeVisiblePointInViewCoordinate(PointF pointF) {
        View realView = getRealView();
        pointF.set((float) realView.getScrollX(), (float) realView.getScrollY());
    }

    public boolean containsGesture(WXGestureType wXGestureType) {
        Set<String> set = this.mGestureType;
        return set != null && set.contains(wXGestureType.toString());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x000a, code lost:
        r0 = r1.mAppendEvents;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean containsEvent(java.lang.String r2) {
        /*
            r1 = this;
            com.taobao.weex.dom.WXEvent r0 = r1.getEvents()
            boolean r0 = r0.contains(r2)
            if (r0 != 0) goto L_0x0017
            java.util.Set<java.lang.String> r0 = r1.mAppendEvents
            if (r0 == 0) goto L_0x0015
            boolean r2 = r0.contains(r2)
            if (r2 == 0) goto L_0x0015
            goto L_0x0017
        L_0x0015:
            r2 = 0
            goto L_0x0018
        L_0x0017:
            r2 = 1
        L_0x0018:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.WXComponent.containsEvent(java.lang.String):boolean");
    }

    public void notifyAppearStateChange(String str, String str2) {
        if (containsEvent(Constants.Event.APPEAR) || containsEvent(Constants.Event.DISAPPEAR)) {
            HashMap hashMap = new HashMap();
            hashMap.put("direction", str2);
            fireEvent(str, hashMap);
        }
    }

    public boolean isUsing() {
        return this.isUsing;
    }

    public void setUsing(boolean z) {
        this.isUsing = z;
    }

    public void readyToRender() {
        if (this.mParent != null && getInstance() != null && getInstance().isTrackComponent()) {
            this.mParent.readyToRender();
        }
    }

    public boolean isVirtualComponent() {
        return this.mType == 1;
    }

    public int getType() {
        return this.mType;
    }

    public boolean hasScrollParent(WXComponent wXComponent) {
        if (wXComponent.getParent() == null) {
            return true;
        }
        if (wXComponent.getParent() instanceof WXBaseScroller) {
            return false;
        }
        return hasScrollParent(wXComponent.getParent());
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00d2, code lost:
        if (r5.equals(com.taobao.weex.common.Constants.Name.BORDER_RIGHT_COLOR) == false) goto L_0x0010;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object convertEmptyProperty(java.lang.String r5, java.lang.Object r6) {
        /*
            r4 = this;
            r5.hashCode()
            int r0 = r5.hashCode()
            r1 = 0
            java.lang.Integer r2 = java.lang.Integer.valueOf(r1)
            r3 = -1
            switch(r0) {
                case -1989576717: goto L_0x00cc;
                case -1971292586: goto L_0x00c0;
                case -1470826662: goto L_0x00b4;
                case -1452542531: goto L_0x00a8;
                case -1308858324: goto L_0x009c;
                case -1290574193: goto L_0x0090;
                case -1228066334: goto L_0x0085;
                case -242276144: goto L_0x007a;
                case -223992013: goto L_0x006e;
                case 333432965: goto L_0x0061;
                case 581268560: goto L_0x0054;
                case 588239831: goto L_0x0047;
                case 722830999: goto L_0x003a;
                case 741115130: goto L_0x002d;
                case 1287124693: goto L_0x0020;
                case 1349188574: goto L_0x0013;
                default: goto L_0x0010;
            }
        L_0x0010:
            r1 = -1
            goto L_0x00d6
        L_0x0013:
            java.lang.String r0 = "borderRadius"
            boolean r5 = r5.equals(r0)
            if (r5 != 0) goto L_0x001c
            goto L_0x0010
        L_0x001c:
            r1 = 15
            goto L_0x00d6
        L_0x0020:
            java.lang.String r0 = "backgroundColor"
            boolean r5 = r5.equals(r0)
            if (r5 != 0) goto L_0x0029
            goto L_0x0010
        L_0x0029:
            r1 = 14
            goto L_0x00d6
        L_0x002d:
            java.lang.String r0 = "borderWidth"
            boolean r5 = r5.equals(r0)
            if (r5 != 0) goto L_0x0036
            goto L_0x0010
        L_0x0036:
            r1 = 13
            goto L_0x00d6
        L_0x003a:
            java.lang.String r0 = "borderColor"
            boolean r5 = r5.equals(r0)
            if (r5 != 0) goto L_0x0043
            goto L_0x0010
        L_0x0043:
            r1 = 12
            goto L_0x00d6
        L_0x0047:
            java.lang.String r0 = "borderBottomRightRadius"
            boolean r5 = r5.equals(r0)
            if (r5 != 0) goto L_0x0050
            goto L_0x0010
        L_0x0050:
            r1 = 11
            goto L_0x00d6
        L_0x0054:
            java.lang.String r0 = "borderBottomLeftRadius"
            boolean r5 = r5.equals(r0)
            if (r5 != 0) goto L_0x005d
            goto L_0x0010
        L_0x005d:
            r1 = 10
            goto L_0x00d6
        L_0x0061:
            java.lang.String r0 = "borderTopRightRadius"
            boolean r5 = r5.equals(r0)
            if (r5 != 0) goto L_0x006a
            goto L_0x0010
        L_0x006a:
            r1 = 9
            goto L_0x00d6
        L_0x006e:
            java.lang.String r0 = "borderLeftWidth"
            boolean r5 = r5.equals(r0)
            if (r5 != 0) goto L_0x0077
            goto L_0x0010
        L_0x0077:
            r1 = 8
            goto L_0x00d6
        L_0x007a:
            java.lang.String r0 = "borderLeftColor"
            boolean r5 = r5.equals(r0)
            if (r5 != 0) goto L_0x0083
            goto L_0x0010
        L_0x0083:
            r1 = 7
            goto L_0x00d6
        L_0x0085:
            java.lang.String r0 = "borderTopLeftRadius"
            boolean r5 = r5.equals(r0)
            if (r5 != 0) goto L_0x008e
            goto L_0x0010
        L_0x008e:
            r1 = 6
            goto L_0x00d6
        L_0x0090:
            java.lang.String r0 = "borderBottomWidth"
            boolean r5 = r5.equals(r0)
            if (r5 != 0) goto L_0x009a
            goto L_0x0010
        L_0x009a:
            r1 = 5
            goto L_0x00d6
        L_0x009c:
            java.lang.String r0 = "borderBottomColor"
            boolean r5 = r5.equals(r0)
            if (r5 != 0) goto L_0x00a6
            goto L_0x0010
        L_0x00a6:
            r1 = 4
            goto L_0x00d6
        L_0x00a8:
            java.lang.String r0 = "borderTopWidth"
            boolean r5 = r5.equals(r0)
            if (r5 != 0) goto L_0x00b2
            goto L_0x0010
        L_0x00b2:
            r1 = 3
            goto L_0x00d6
        L_0x00b4:
            java.lang.String r0 = "borderTopColor"
            boolean r5 = r5.equals(r0)
            if (r5 != 0) goto L_0x00be
            goto L_0x0010
        L_0x00be:
            r1 = 2
            goto L_0x00d6
        L_0x00c0:
            java.lang.String r0 = "borderRightWidth"
            boolean r5 = r5.equals(r0)
            if (r5 != 0) goto L_0x00ca
            goto L_0x0010
        L_0x00ca:
            r1 = 1
            goto L_0x00d6
        L_0x00cc:
            java.lang.String r0 = "borderRightColor"
            boolean r5 = r5.equals(r0)
            if (r5 != 0) goto L_0x00d6
            goto L_0x0010
        L_0x00d6:
            switch(r1) {
                case 0: goto L_0x00de;
                case 1: goto L_0x00dd;
                case 2: goto L_0x00de;
                case 3: goto L_0x00dd;
                case 4: goto L_0x00de;
                case 5: goto L_0x00dd;
                case 6: goto L_0x00dd;
                case 7: goto L_0x00de;
                case 8: goto L_0x00dd;
                case 9: goto L_0x00dd;
                case 10: goto L_0x00dd;
                case 11: goto L_0x00dd;
                case 12: goto L_0x00de;
                case 13: goto L_0x00dd;
                case 14: goto L_0x00da;
                case 15: goto L_0x00dd;
                default: goto L_0x00d9;
            }
        L_0x00d9:
            return r6
        L_0x00da:
            java.lang.String r5 = "transparent"
            return r5
        L_0x00dd:
            return r2
        L_0x00de:
            java.lang.String r5 = "black"
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.WXComponent.convertEmptyProperty(java.lang.String, java.lang.Object):java.lang.Object");
    }

    private void setActiveTouchListener() {
        View realView;
        if (getStyles().getPesudoStyles().containsKey(Constants.PSEUDO.ACTIVE) && (realView = getRealView()) != null) {
            realView.setOnTouchListener(new TouchActivePseudoListener(this, !isConsumeTouch()));
        }
    }

    /* access modifiers changed from: protected */
    public boolean isConsumeTouch() {
        List<OnClickListener> list = this.mHostClickListeners;
        return (list != null && list.size() > 0) || this.mGesture != null;
    }

    public void updateActivePseudo(boolean z) {
        if (getInstance() != null) {
            setPseudoClassStatus(Constants.PSEUDO.ACTIVE, z);
        }
    }

    /* access modifiers changed from: protected */
    public void setPseudoClassStatus(String str, boolean z) {
        WXStyle styles = getStyles();
        Map<String, Map<String, Object>> pesudoStyles = styles.getPesudoStyles();
        if (pesudoStyles != null && pesudoStyles.size() != 0) {
            if (this.mPesudoStatus == null) {
                this.mPesudoStatus = new PesudoStatus();
            }
            Map<String, Object> updateStatusAndGetUpdateStyles = this.mPesudoStatus.updateStatusAndGetUpdateStyles(str, z, pesudoStyles, styles.getPesudoResetStyles());
            if (updateStatusAndGetUpdateStyles != null) {
                if (z) {
                    this.mPseudoResetGraphicSize = new GraphicSize(getLayoutSize().getWidth(), getLayoutSize().getHeight());
                    if (updateStatusAndGetUpdateStyles.keySet().contains("width")) {
                        getLayoutSize().setWidth(WXViewUtils.getRealPxByWidth(WXUtils.parseFloat(styles.getPesudoResetStyles().get("width:active")), getViewPortWidthForFloat()));
                    } else if (updateStatusAndGetUpdateStyles.keySet().contains("height")) {
                        getLayoutSize().setHeight(WXViewUtils.getRealPxByWidth(WXUtils.parseFloat(styles.getPesudoResetStyles().get("height:active")), getViewPortWidthForFloat()));
                    }
                } else {
                    GraphicSize graphicSize = this.mPseudoResetGraphicSize;
                    if (graphicSize != null) {
                        setLayoutSize(graphicSize);
                    }
                }
            }
            updateStyleByPesudo(updateStatusAndGetUpdateStyles);
        }
    }

    private void updateStyleByPesudo(Map<String, Object> map) {
        if (getInstance() != null) {
            new GraphicActionUpdateStyle(getInstance(), getRef(), map, getPadding(), getMargin(), getBorder(), true).executeActionOnRender();
            if (getLayoutWidth() != 0.0f || getLayoutHeight() != 0.0f) {
                if (map.containsKey("width")) {
                    WXBridgeManager.getInstance().setStyleWidth(getInstanceId(), getRef(), getLayoutWidth());
                } else if (map.containsKey("height")) {
                    WXBridgeManager.getInstance().setStyleHeight(getInstanceId(), getRef(), getLayoutHeight());
                }
            }
        }
    }

    public int getStickyOffset() {
        return this.mStickyOffset;
    }

    public boolean canRecycled() {
        return (!isFixed() || !isSticky()) && getAttrs().canRecycled();
    }

    public void setStickyOffset(int i) {
        this.mStickyOffset = i;
    }

    public boolean isLayerTypeEnabled() {
        return getInstance().isLayerTypeEnabled();
    }

    public void setNeedLayoutOnAnimation(boolean z) {
        this.mNeedLayoutOnAnimation = z;
    }

    public void notifyNativeSizeChanged(int i, int i2) {
        if (this.mNeedLayoutOnAnimation) {
            UniBoxShadowData uniBoxShadowData = this.mBoxShadowData;
            if (uniBoxShadowData != null && uniBoxShadowData.getNormalShadows().size() > 0) {
                i -= this.mBoxShadowData.getNormalLeft();
                i2 -= this.mBoxShadowData.getNormalTop();
            }
            WXBridgeManager instance = WXBridgeManager.getInstance();
            instance.setStyleWidth(getInstanceId(), getRef(), (float) i);
            instance.setStyleHeight(getInstanceId(), getRef(), (float) i2);
        }
    }

    public void onRenderFinish(int i) {
        if (WXTracing.isAvailable()) {
            double nanosToMillis = Stopwatch.nanosToMillis(this.mTraceInfo.uiThreadNanos);
            if (i == 2 || i == 0) {
                WXTracing.TraceEvent newEvent = WXTracing.newEvent("DomExecute", getInstanceId(), this.mTraceInfo.rootEventId);
                newEvent.ph = "X";
                newEvent.ts = this.mTraceInfo.domThreadStart;
                newEvent.tname = "DOMThread";
                newEvent.name = getComponentType();
                newEvent.classname = getClass().getSimpleName();
                if (getParent() != null) {
                    newEvent.parentRef = getParent().getRef();
                }
                newEvent.submit();
            }
            if (i != 2 && i != 1) {
                return;
            }
            if (this.mTraceInfo.uiThreadStart != -1) {
                WXTracing.TraceEvent newEvent2 = WXTracing.newEvent("UIExecute", getInstanceId(), this.mTraceInfo.rootEventId);
                newEvent2.ph = "X";
                newEvent2.duration = nanosToMillis;
                newEvent2.ts = this.mTraceInfo.uiThreadStart;
                newEvent2.name = getComponentType();
                newEvent2.classname = getClass().getSimpleName();
                if (getParent() != null) {
                    newEvent2.parentRef = getParent().getRef();
                }
                newEvent2.submit();
            } else if (WXEnvironment.isApkDebugable()) {
                isLazy();
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean isRippleEnabled() {
        try {
            return WXUtils.getBoolean(getAttrs().get(Constants.Name.RIPPLE_ENABLED), false).booleanValue();
        } catch (Throwable unused) {
            return false;
        }
    }

    public boolean isWaste() {
        return this.waste;
    }

    public void setWaste(boolean z) {
        if (this.waste != z) {
            this.waste = z;
            if (!WXBasicComponentType.RECYCLE_LIST.equals(getParent().getComponentType())) {
                NativeRenderObjectUtils.nativeRenderObjectChildWaste(getRenderObjectPtr(), z);
            }
            if (z) {
                getStyles().put(Constants.Name.VISIBILITY, (Object) "hidden");
                if (getHostView() != null) {
                    getHostView().setVisibility(8);
                } else if (!this.mLazy) {
                    lazy(true);
                }
            } else {
                getStyles().put(Constants.Name.VISIBILITY, (Object) "visible");
                if (getHostView() != null) {
                    getHostView().setVisibility(0);
                } else if (!this.mLazy) {
                } else {
                    if (this.mParent == null || !this.mParent.isLazy()) {
                        Statements.initLazyComponent(this, this.mParent);
                    } else {
                        lazy(false);
                    }
                }
            }
        }
    }

    public String getViewTreeKey() {
        if (this.mViewTreeKey == null) {
            if (getParent() == null) {
                this.mViewTreeKey = hashCode() + "_" + getRef();
            } else {
                this.mViewTreeKey = hashCode() + "_" + getRef() + "_" + getParent().indexOf(this);
            }
        }
        return this.mViewTreeKey;
    }

    public WXTransition getTransition() {
        return this.mTransition;
    }

    public void setTransition(WXTransition wXTransition) {
        this.mTransition = wXTransition;
    }

    public void addAnimationForElement(Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            if (this.animations == null) {
                this.animations = new ConcurrentLinkedQueue<>();
            }
            this.animations.add(new Pair(getRef(), map));
        }
    }

    private void parseAnimation() {
        WXAnimationBean createAnimationBean;
        ConcurrentLinkedQueue<Pair<String, Map<String, Object>>> concurrentLinkedQueue = this.animations;
        if (concurrentLinkedQueue != null) {
            Iterator<Pair<String, Map<String, Object>>> it = concurrentLinkedQueue.iterator();
            while (it.hasNext()) {
                Pair next = it.next();
                if (!TextUtils.isEmpty((CharSequence) next.first) && (createAnimationBean = createAnimationBean((String) next.first, (Map) next.second)) != null) {
                    new GraphicActionAnimation(getInstance(), getRef(), createAnimationBean).executeAction();
                }
            }
            this.animations.clear();
        }
    }

    private WXAnimationBean createAnimationBean(String str, Map<String, Object> map) {
        if (map != null) {
            try {
                Object obj = map.get("transform");
                if ((obj instanceof String) && !TextUtils.isEmpty((String) obj)) {
                    WXAnimationBean wXAnimationBean = new WXAnimationBean();
                    int layoutWidth = (int) getLayoutWidth();
                    int layoutHeight = (int) getLayoutHeight();
                    wXAnimationBean.styles = new WXAnimationBean.Style();
                    wXAnimationBean.styles.init((String) map.get(Constants.Name.TRANSFORM_ORIGIN), (String) obj, layoutWidth, layoutHeight, WXSDKManager.getInstanceViewPortWidth(getInstanceId()), getInstance());
                    return wXAnimationBean;
                }
            } catch (RuntimeException e) {
                WXLogUtils.e("", (Throwable) e);
            }
        }
        return null;
    }

    public void lazy(boolean z) {
        this.mLazy = z;
    }

    public long getRenderObjectPtr() {
        if (getBasicComponentData().isRenderPtrEmpty()) {
            getBasicComponentData().setRenderObjectPr(NativeRenderObjectUtils.nativeGetRenderObject(getInstanceId(), getRef()));
        }
        return getBasicComponentData().getRenderObjectPr();
    }

    public void updateNativeAttr(String str, Object obj) {
        if (str != null) {
            if (obj == null) {
                obj = "";
            }
            getBasicComponentData().getAttrs().put(str, obj);
            NativeRenderObjectUtils.nativeUpdateRenderObjectAttr(getRenderObjectPtr(), str, obj.toString());
        }
    }

    public void nativeUpdateAttrs(Map<String, Object> map) {
        for (Map.Entry next : map.entrySet()) {
            if (next.getKey() != null) {
                updateNativeAttr((String) next.getKey(), next.getValue());
            }
        }
    }

    public void updateNativeStyle(String str, Object obj) {
        if (str != null) {
            if (obj == null) {
                obj = "";
            }
            getBasicComponentData().getStyles().put(str, obj);
            NativeRenderObjectUtils.nativeUpdateRenderObjectStyle(getRenderObjectPtr(), str, obj.toString());
        }
    }

    public void updateNativeStyles(Map<String, Object> map) {
        for (Map.Entry next : map.entrySet()) {
            if (next.getKey() != null) {
                updateNativeStyle((String) next.getKey(), next.getValue());
            }
        }
    }

    public void addLayerOverFlowListener(String str) {
        if (getInstance() != null) {
            getInstance().addLayerOverFlowListener(str);
        }
    }

    public void removeLayerOverFlowListener(String str) {
        if (getInstance() != null) {
            getInstance().removeLayerOverFlowListener(str);
        }
    }

    @WXComponentProp(name = "hoverClass")
    public void hoverClass(String str) {
        JSONObject parseObject = JSON.parseObject(str);
        if (parseObject != null) {
            ViewHover viewHover = this.mHover;
            if (viewHover == null) {
                this.mHover = new ViewHover(this, parseObject);
            } else {
                viewHover.update(parseObject);
            }
            if (!getEvents().contains(ViewHover.VIEW_HOVER_EVENT)) {
                addEvent(ViewHover.VIEW_HOVER_EVENT);
            }
        }
    }

    @WXComponentProp(name = "hoverStopPropagation")
    public void hoverStopPropagation(boolean z) {
        if (this.mHover == null) {
            this.mHover = new ViewHover(this);
        }
        this.mHover.setHoverStopPropagation(z);
        if (!getEvents().contains(ViewHover.VIEW_HOVER_EVENT)) {
            addEvent(ViewHover.VIEW_HOVER_EVENT);
        }
        if (this.mParent != null && z) {
            this.mParent.setHoverReceiveTouch(false);
        }
    }

    @WXComponentProp(name = "hoverStartTime")
    public void hoverStartTime(int i) {
        if (this.mHover == null) {
            this.mHover = new ViewHover(this);
        }
        this.mHover.setHoverStartTime(i);
    }

    @WXComponentProp(name = "hoverStayTime")
    public void hoverStayTime(int i) {
        if (this.mHover == null) {
            this.mHover = new ViewHover(this);
        }
        this.mHover.setHoverStayTime(i);
    }

    public void setHoverClassStatus(boolean z) {
        Map<String, Object> updateStatusAndGetUpdateStyles = this.mHover.updateStatusAndGetUpdateStyles(z);
        if (updateStatusAndGetUpdateStyles != null) {
            if (z) {
                boolean contains = updateStatusAndGetUpdateStyles.keySet().contains("width");
                boolean contains2 = updateStatusAndGetUpdateStyles.keySet().contains("height");
                if (contains || contains2) {
                    this.mPseudoResetGraphicSize = new GraphicSize(getLayoutSize().getWidth(), getLayoutSize().getHeight());
                }
                if (contains) {
                    getLayoutSize().setWidth(WXViewUtils.getRealPxByWidth(WXUtils.parseFloat(updateStatusAndGetUpdateStyles.get("width")), getViewPortWidthForFloat()));
                } else if (contains2) {
                    getLayoutSize().setHeight(WXViewUtils.getRealPxByWidth(WXUtils.parseFloat(updateStatusAndGetUpdateStyles.get("height")), getViewPortWidthForFloat()));
                }
            } else {
                GraphicSize graphicSize = this.mPseudoResetGraphicSize;
                if (graphicSize != null) {
                    setLayoutSize(graphicSize);
                }
            }
        }
        updateStyleByPesudo(updateStatusAndGetUpdateStyles);
    }

    public void setHoverReceiveTouch(boolean z) {
        if (getHover() != null) {
            getHover().setReceiveTouch(z);
        }
        if (this.mParent != null) {
            this.mParent.setHoverReceiveTouch(z);
        }
    }

    public ViewHover getHover() {
        return this.mHover;
    }

    public boolean isPreventGesture() {
        return this.isPreventGesture;
    }

    @WXComponentProp(name = "preventGesture")
    public void setPreventGesture(boolean z) {
        this.isPreventGesture = z;
        addEvent("preventGesture");
    }

    public float getViewPortWidthForFloat() {
        if (getInstance() != null) {
            return getInstance().getInstanceViewPortWidthWithFloat();
        }
        return 720.0f;
    }
}
