package com.taobao.weex.ui.action;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import androidx.core.view.animation.PathInterpolatorCompat;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.animation.BackgroundColorProperty;
import com.taobao.weex.ui.animation.HeightProperty;
import com.taobao.weex.ui.animation.WXAnimationBean;
import com.taobao.weex.ui.animation.WXAnimationModule;
import com.taobao.weex.ui.animation.WidthProperty;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.list.template.TemplateDom;
import com.taobao.weex.ui.view.border.BorderDrawable;
import com.taobao.weex.utils.SingleFunctionParser;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXResourceUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.util.HashMap;
import java.util.List;

public class GraphicActionAnimation extends BasicGraphicAction {
    private static final String TAG = "GraphicActionAnimation";
    private final String callback;
    private WXAnimationBean mAnimationBean;
    private final boolean styleNeedInit;

    public GraphicActionAnimation(WXSDKInstance wXSDKInstance, String str, WXAnimationBean wXAnimationBean) {
        super(wXSDKInstance, str);
        this.styleNeedInit = false;
        this.callback = null;
        this.mAnimationBean = wXAnimationBean;
    }

    public GraphicActionAnimation(WXSDKInstance wXSDKInstance, String str, String str2, String str3) {
        super(wXSDKInstance, str);
        this.styleNeedInit = true;
        this.callback = str3;
        if (!TextUtils.isEmpty(str2)) {
            this.mAnimationBean = (WXAnimationBean) JSONObject.parseObject(str2, WXAnimationBean.class);
        }
    }

    public GraphicActionAnimation(WXSDKInstance wXSDKInstance, String str, WXAnimationBean wXAnimationBean, String str2) {
        super(wXSDKInstance, str);
        this.styleNeedInit = false;
        this.mAnimationBean = wXAnimationBean;
        this.callback = str2;
    }

    public void executeAction() {
        WXSDKInstance wXSDKInstance;
        if (this.mAnimationBean != null) {
            WXComponent wXComponent = WXSDKManager.getInstance().getWXRenderManager().getWXComponent(getPageId(), getRef());
            if ((wXComponent != null || (TemplateDom.isVirtualDomRef(getRef()) && (wXComponent = TemplateDom.findVirtualComponentByVRef(getPageId(), getRef())) != null)) && (wXSDKInstance = WXSDKManager.getInstance().getWXRenderManager().getWXSDKInstance(getPageId())) != null && this.mAnimationBean.styles != null) {
                if (this.styleNeedInit) {
                    String str = (String) wXComponent.getStyles().get(Constants.Name.TRANSFORM_ORIGIN);
                    if (TextUtils.isEmpty(this.mAnimationBean.styles.transformOrigin)) {
                        this.mAnimationBean.styles.transformOrigin = str;
                    }
                    this.mAnimationBean.styles.init(this.mAnimationBean.styles.transformOrigin, this.mAnimationBean.styles.transform, (int) wXComponent.getLayoutWidth(), (int) wXComponent.getLayoutHeight(), wXSDKInstance.getInstanceViewPortWidthWithFloat(), wXSDKInstance);
                }
                startAnimation(wXSDKInstance, wXComponent);
            }
        }
    }

    private void startAnimation(WXSDKInstance wXSDKInstance, WXComponent wXComponent) {
        if (wXComponent != null) {
            WXAnimationBean wXAnimationBean = this.mAnimationBean;
            if (wXAnimationBean != null) {
                wXComponent.setNeedLayoutOnAnimation(wXAnimationBean.needLayout);
            }
            if (wXComponent.getHostView() == null) {
                wXComponent.postAnimation(new WXAnimationModule.AnimationHolder(this.mAnimationBean, this.callback));
                return;
            }
            try {
                ObjectAnimator createAnimator = createAnimator(wXComponent.getHostView(), wXSDKInstance.getInstanceViewPortWidthWithFloat());
                if (createAnimator != null) {
                    Animator.AnimatorListener createAnimatorListener = createAnimatorListener(wXSDKInstance, this.callback);
                    if (Build.VERSION.SDK_INT < 18 && wXComponent.isLayerTypeEnabled()) {
                        wXComponent.getHostView().setLayerType(2, (Paint) null);
                    }
                    Interpolator createTimeInterpolator = createTimeInterpolator();
                    if (createAnimatorListener != null) {
                        createAnimator.addListener(createAnimatorListener);
                    }
                    if (createTimeInterpolator != null) {
                        createAnimator.setInterpolator(createTimeInterpolator);
                    }
                    wXComponent.getHostView().setCameraDistance(this.mAnimationBean.styles.getCameraDistance());
                    createAnimator.setDuration(this.mAnimationBean.duration);
                    createAnimator.start();
                }
            } catch (RuntimeException e) {
                WXLogUtils.e(TAG, WXLogUtils.getStackTrace(e));
            }
        }
    }

    private ObjectAnimator createAnimator(View view, float f) {
        WXAnimationBean.Style style;
        if (view == null || (style = this.mAnimationBean.styles) == null) {
            return null;
        }
        List<PropertyValuesHolder> holders = style.getHolders();
        if (!TextUtils.isEmpty(style.backgroundColor)) {
            BorderDrawable borderDrawable = WXViewUtils.getBorderDrawable(view);
            if (borderDrawable != null) {
                holders.add(PropertyValuesHolder.ofObject(new BackgroundColorProperty(), new ArgbEvaluator(), new Integer[]{Integer.valueOf(borderDrawable.getColor()), Integer.valueOf(WXResourceUtils.getColor(style.backgroundColor))}));
            } else if (view.getBackground() instanceof ColorDrawable) {
                holders.add(PropertyValuesHolder.ofObject(new BackgroundColorProperty(), new ArgbEvaluator(), new Integer[]{Integer.valueOf(((ColorDrawable) view.getBackground()).getColor()), Integer.valueOf(WXResourceUtils.getColor(style.backgroundColor))}));
            }
        }
        if (view.getLayoutParams() != null && (!TextUtils.isEmpty(style.width) || !TextUtils.isEmpty(style.height))) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (!TextUtils.isEmpty(style.width)) {
                holders.add(PropertyValuesHolder.ofInt(new WidthProperty(), new int[]{layoutParams.width, (int) WXViewUtils.getRealPxByWidth(WXUtils.getFloat(style.width), f)}));
            }
            if (!TextUtils.isEmpty(style.height)) {
                holders.add(PropertyValuesHolder.ofInt(new HeightProperty(), new int[]{layoutParams.height, (int) WXViewUtils.getRealPxByWidth(WXUtils.getFloat(style.height), f)}));
            }
        }
        if (style.getPivot() != null) {
            Pair<Float, Float> pivot = style.getPivot();
            view.setPivotX(((Float) pivot.first).floatValue());
            view.setPivotY(((Float) pivot.second).floatValue());
        }
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, (PropertyValuesHolder[]) holders.toArray(new PropertyValuesHolder[holders.size()]));
        ofPropertyValuesHolder.setStartDelay(this.mAnimationBean.delay);
        return ofPropertyValuesHolder;
    }

    private Animator.AnimatorListener createAnimatorListener(final WXSDKInstance wXSDKInstance, final String str) {
        if (!TextUtils.isEmpty(str)) {
            return new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    WXSDKInstance wXSDKInstance = wXSDKInstance;
                    if (wXSDKInstance == null || wXSDKInstance.isDestroy()) {
                        WXLogUtils.e("RenderContextImpl-onAnimationEnd WXSDKInstance == null NPE or instance is destroyed");
                    } else {
                        WXSDKManager.getInstance().callback(wXSDKInstance.getInstanceId(), str, new HashMap());
                    }
                }
            };
        }
        return null;
    }

    private Interpolator createTimeInterpolator() {
        String str = this.mAnimationBean.timingFunction;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1965120668:
                if (str.equals("ease-in")) {
                    c = 0;
                    break;
                }
                break;
            case -1102672091:
                if (str.equals("linear")) {
                    c = 1;
                    break;
                }
                break;
            case -789192465:
                if (str.equals("ease-out")) {
                    c = 2;
                    break;
                }
                break;
            case -361990811:
                if (str.equals("ease-in-out")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return new AccelerateInterpolator();
            case 1:
                return new LinearInterpolator();
            case 2:
                return new DecelerateInterpolator();
            case 3:
                return new AccelerateDecelerateInterpolator();
            default:
                try {
                    List parse = new SingleFunctionParser(this.mAnimationBean.timingFunction, new SingleFunctionParser.FlatMapper<Float>() {
                        public Float map(String str) {
                            return Float.valueOf(Float.parseFloat(str));
                        }
                    }).parse("cubic-bezier");
                    if (parse != null && parse.size() == 4) {
                        return PathInterpolatorCompat.create(((Float) parse.get(0)).floatValue(), ((Float) parse.get(1)).floatValue(), ((Float) parse.get(2)).floatValue(), ((Float) parse.get(3)).floatValue());
                    }
                } catch (RuntimeException unused) {
                }
                return null;
        }
    }
}
