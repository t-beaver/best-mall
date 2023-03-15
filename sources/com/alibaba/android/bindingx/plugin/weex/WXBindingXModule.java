package com.alibaba.android.bindingx.plugin.weex;

import android.content.Context;
import android.util.Log;
import android.view.View;
import com.alibaba.android.bindingx.core.BindingXCore;
import com.alibaba.android.bindingx.core.BindingXEventType;
import com.alibaba.android.bindingx.core.IEventHandler;
import com.alibaba.android.bindingx.core.LogProxy;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.utils.WXViewUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WXBindingXModule extends WXSDKEngine.DestroyableModule {
    /* access modifiers changed from: private */
    public BindingXCore mBindingXCore;
    private PlatformManager mPlatformManager;

    public WXBindingXModule() {
    }

    WXBindingXModule(BindingXCore bindingXCore) {
        this.mBindingXCore = bindingXCore;
    }

    private void prepareInternal() {
        if (this.mPlatformManager == null) {
            this.mPlatformManager = createPlatformManager(this.mWXSDKInstance);
        }
        if (this.mBindingXCore == null) {
            BindingXCore bindingXCore = new BindingXCore(this.mPlatformManager);
            this.mBindingXCore = bindingXCore;
            bindingXCore.registerEventHandler("scroll", new BindingXCore.ObjectCreator<IEventHandler, Context, PlatformManager>() {
                public IEventHandler createWith(Context context, PlatformManager platformManager, Object... objArr) {
                    return new BindingXScrollHandler(context, platformManager, objArr);
                }
            });
            this.mBindingXCore.registerEventHandler("pan", new BindingXCore.ObjectCreator<IEventHandler, Context, PlatformManager>() {
                public IEventHandler createWith(Context context, PlatformManager platformManager, Object... objArr) {
                    return new BindingXGestureHandler(context, platformManager, objArr);
                }
            });
        }
    }

    @JSMethod(uiThread = false)
    public void prepare(Map<String, Object> map) {
        prepareInternal();
    }

    @JSMethod(uiThread = false)
    public Map<String, String> bind(Map<String, Object> map, final JSCallback jSCallback) {
        prepareInternal();
        BindingXCore bindingXCore = this.mBindingXCore;
        String str = null;
        Context context = this.mWXSDKInstance == null ? null : this.mWXSDKInstance.getContext();
        if (this.mWXSDKInstance != null) {
            str = this.mWXSDKInstance.getInstanceId();
        }
        if (map == null) {
            map = Collections.emptyMap();
        }
        String doBind = bindingXCore.doBind(context, str, map, new BindingXCore.JavaScriptCallback() {
            public void callback(Object obj) {
                if (jSCallback != null) {
                    Log.e("触发去往前端的回调", WXBridgeManager.METHOD_CALLBACK);
                    jSCallback.invokeAndKeepAlive(obj);
                }
            }
        }, new Object[0]);
        HashMap hashMap = new HashMap(2);
        hashMap.put(BindingXConstants.KEY_TOKEN, doBind);
        return hashMap;
    }

    @JSMethod(uiThread = false)
    public void bindAsync(Map<String, Object> map, JSCallback jSCallback, JSCallback jSCallback2) {
        Map<String, String> bind = bind(map, jSCallback);
        if (jSCallback2 != null && bind != null) {
            jSCallback2.invoke(bind);
        }
    }

    @JSMethod(uiThread = false)
    public void unbind(Map<String, Object> map) {
        BindingXCore bindingXCore = this.mBindingXCore;
        if (bindingXCore != null) {
            bindingXCore.doUnbind(map);
        }
    }

    @JSMethod(uiThread = false)
    public void unbindAll() {
        BindingXCore bindingXCore = this.mBindingXCore;
        if (bindingXCore != null) {
            bindingXCore.doRelease();
        }
    }

    @JSMethod(uiThread = false)
    public List<String> supportFeatures() {
        return Arrays.asList(new String[]{"pan", "orientation", BindingXEventType.TYPE_TIMING, "scroll", "experimentalGestureFeatures"});
    }

    @JSMethod(uiThread = false)
    public void getComputedStyleAsync(String str, JSCallback jSCallback) {
        Map<String, Object> computedStyle = getComputedStyle(str);
        if (jSCallback != null) {
            jSCallback.invoke(computedStyle);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0232  */
    @com.taobao.weex.annotation.JSMethod(uiThread = false)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Map<java.lang.String, java.lang.Object> getComputedStyle(java.lang.String r18) {
        /*
            r17 = this;
            r0 = r17
            r17.prepareInternal()
            com.alibaba.android.bindingx.core.PlatformManager r1 = r0.mPlatformManager
            com.alibaba.android.bindingx.core.PlatformManager$IDeviceResolutionTranslator r1 = r1.getResolutionTranslator()
            com.taobao.weex.WXSDKInstance r2 = r0.mWXSDKInstance
            java.lang.String r2 = r2.getInstanceId()
            r3 = r18
            com.taobao.weex.ui.component.WXComponent r2 = com.alibaba.android.bindingx.plugin.weex.WXModuleUtils.findComponentByRef(r2, r3)
            if (r2 != 0) goto L_0x001e
            java.util.Map r1 = java.util.Collections.emptyMap()
            return r1
        L_0x001e:
            android.view.View r3 = r2.getHostView()
            if (r3 != 0) goto L_0x0029
            java.util.Map r1 = java.util.Collections.emptyMap()
            return r1
        L_0x0029:
            java.util.HashMap r4 = new java.util.HashMap
            r4.<init>()
            float r5 = r2.getLayoutWidth()
            double r5 = (double) r5
            r7 = 0
            java.lang.Object[] r8 = new java.lang.Object[r7]
            double r5 = r1.nativeToWeb(r5, r8)
            java.lang.Double r5 = java.lang.Double.valueOf(r5)
            java.lang.String r6 = "width"
            r4.put(r6, r5)
            float r5 = r2.getLayoutHeight()
            double r5 = (double) r5
            java.lang.Object[] r8 = new java.lang.Object[r7]
            double r5 = r1.nativeToWeb(r5, r8)
            java.lang.Double r5 = java.lang.Double.valueOf(r5)
            java.lang.String r6 = "height"
            r4.put(r6, r5)
            com.taobao.weex.dom.CSSShorthand r5 = r2.getPadding()
            io.dcloud.feature.uniapp.dom.AbsCSSShorthand$EDGE r6 = io.dcloud.feature.uniapp.dom.AbsCSSShorthand.EDGE.LEFT
            float r5 = r5.get((java.lang.Enum<? extends io.dcloud.feature.uniapp.dom.AbsCSSShorthand.CSSProperty>) r6)
            double r5 = (double) r5
            java.lang.Object[] r8 = new java.lang.Object[r7]
            double r5 = r1.nativeToWeb(r5, r8)
            java.lang.Double r5 = java.lang.Double.valueOf(r5)
            java.lang.String r6 = "padding-left"
            r4.put(r6, r5)
            com.taobao.weex.dom.CSSShorthand r5 = r2.getPadding()
            io.dcloud.feature.uniapp.dom.AbsCSSShorthand$EDGE r6 = io.dcloud.feature.uniapp.dom.AbsCSSShorthand.EDGE.TOP
            float r5 = r5.get((java.lang.Enum<? extends io.dcloud.feature.uniapp.dom.AbsCSSShorthand.CSSProperty>) r6)
            double r5 = (double) r5
            java.lang.Object[] r8 = new java.lang.Object[r7]
            double r5 = r1.nativeToWeb(r5, r8)
            java.lang.Double r5 = java.lang.Double.valueOf(r5)
            java.lang.String r6 = "padding-top"
            r4.put(r6, r5)
            com.taobao.weex.dom.CSSShorthand r5 = r2.getPadding()
            io.dcloud.feature.uniapp.dom.AbsCSSShorthand$EDGE r6 = io.dcloud.feature.uniapp.dom.AbsCSSShorthand.EDGE.RIGHT
            float r5 = r5.get((java.lang.Enum<? extends io.dcloud.feature.uniapp.dom.AbsCSSShorthand.CSSProperty>) r6)
            double r5 = (double) r5
            java.lang.Object[] r8 = new java.lang.Object[r7]
            double r5 = r1.nativeToWeb(r5, r8)
            java.lang.Double r5 = java.lang.Double.valueOf(r5)
            java.lang.String r6 = "padding-right"
            r4.put(r6, r5)
            com.taobao.weex.dom.CSSShorthand r5 = r2.getPadding()
            io.dcloud.feature.uniapp.dom.AbsCSSShorthand$EDGE r6 = io.dcloud.feature.uniapp.dom.AbsCSSShorthand.EDGE.BOTTOM
            float r5 = r5.get((java.lang.Enum<? extends io.dcloud.feature.uniapp.dom.AbsCSSShorthand.CSSProperty>) r6)
            double r5 = (double) r5
            java.lang.Object[] r8 = new java.lang.Object[r7]
            double r5 = r1.nativeToWeb(r5, r8)
            java.lang.Double r5 = java.lang.Double.valueOf(r5)
            java.lang.String r6 = "padding-bottom"
            r4.put(r6, r5)
            com.taobao.weex.dom.CSSShorthand r5 = r2.getMargin()
            io.dcloud.feature.uniapp.dom.AbsCSSShorthand$EDGE r6 = io.dcloud.feature.uniapp.dom.AbsCSSShorthand.EDGE.LEFT
            float r5 = r5.get((java.lang.Enum<? extends io.dcloud.feature.uniapp.dom.AbsCSSShorthand.CSSProperty>) r6)
            double r5 = (double) r5
            java.lang.Object[] r8 = new java.lang.Object[r7]
            double r5 = r1.nativeToWeb(r5, r8)
            java.lang.Double r5 = java.lang.Double.valueOf(r5)
            java.lang.String r6 = "margin-left"
            r4.put(r6, r5)
            com.taobao.weex.dom.CSSShorthand r5 = r2.getMargin()
            io.dcloud.feature.uniapp.dom.AbsCSSShorthand$EDGE r6 = io.dcloud.feature.uniapp.dom.AbsCSSShorthand.EDGE.TOP
            float r5 = r5.get((java.lang.Enum<? extends io.dcloud.feature.uniapp.dom.AbsCSSShorthand.CSSProperty>) r6)
            double r5 = (double) r5
            java.lang.Object[] r8 = new java.lang.Object[r7]
            double r5 = r1.nativeToWeb(r5, r8)
            java.lang.Double r5 = java.lang.Double.valueOf(r5)
            java.lang.String r6 = "margin-top"
            r4.put(r6, r5)
            com.taobao.weex.dom.CSSShorthand r5 = r2.getMargin()
            io.dcloud.feature.uniapp.dom.AbsCSSShorthand$EDGE r6 = io.dcloud.feature.uniapp.dom.AbsCSSShorthand.EDGE.RIGHT
            float r5 = r5.get((java.lang.Enum<? extends io.dcloud.feature.uniapp.dom.AbsCSSShorthand.CSSProperty>) r6)
            double r5 = (double) r5
            java.lang.Object[] r8 = new java.lang.Object[r7]
            double r5 = r1.nativeToWeb(r5, r8)
            java.lang.Double r5 = java.lang.Double.valueOf(r5)
            java.lang.String r6 = "margin-right"
            r4.put(r6, r5)
            com.taobao.weex.dom.CSSShorthand r5 = r2.getMargin()
            io.dcloud.feature.uniapp.dom.AbsCSSShorthand$EDGE r6 = io.dcloud.feature.uniapp.dom.AbsCSSShorthand.EDGE.BOTTOM
            float r5 = r5.get((java.lang.Enum<? extends io.dcloud.feature.uniapp.dom.AbsCSSShorthand.CSSProperty>) r6)
            double r5 = (double) r5
            java.lang.Object[] r8 = new java.lang.Object[r7]
            double r5 = r1.nativeToWeb(r5, r8)
            java.lang.Double r5 = java.lang.Double.valueOf(r5)
            java.lang.String r6 = "margin-bottom"
            r4.put(r6, r5)
            float r5 = r3.getTranslationX()
            double r5 = (double) r5
            java.lang.Object[] r8 = new java.lang.Object[r7]
            double r5 = r1.nativeToWeb(r5, r8)
            java.lang.Double r5 = java.lang.Double.valueOf(r5)
            java.lang.String r6 = "translateX"
            r4.put(r6, r5)
            float r5 = r3.getTranslationY()
            double r5 = (double) r5
            java.lang.Object[] r8 = new java.lang.Object[r7]
            double r5 = r1.nativeToWeb(r5, r8)
            java.lang.Double r5 = java.lang.Double.valueOf(r5)
            java.lang.String r6 = "translateY"
            r4.put(r6, r5)
            float r5 = r3.getRotationX()
            float r5 = com.alibaba.android.bindingx.core.internal.Utils.normalizeRotation(r5)
            java.lang.Float r5 = java.lang.Float.valueOf(r5)
            java.lang.String r6 = "rotateX"
            r4.put(r6, r5)
            float r5 = r3.getRotationY()
            float r5 = com.alibaba.android.bindingx.core.internal.Utils.normalizeRotation(r5)
            java.lang.Float r5 = java.lang.Float.valueOf(r5)
            java.lang.String r6 = "rotateY"
            r4.put(r6, r5)
            float r5 = r3.getRotation()
            float r5 = com.alibaba.android.bindingx.core.internal.Utils.normalizeRotation(r5)
            java.lang.Float r5 = java.lang.Float.valueOf(r5)
            java.lang.String r6 = "rotateZ"
            r4.put(r6, r5)
            float r5 = r3.getScaleX()
            java.lang.Float r5 = java.lang.Float.valueOf(r5)
            java.lang.String r6 = "scaleX"
            r4.put(r6, r5)
            float r5 = r3.getScaleY()
            java.lang.Float r5 = java.lang.Float.valueOf(r5)
            java.lang.String r6 = "scaleY"
            r4.put(r6, r5)
            float r5 = r3.getAlpha()
            java.lang.Float r5 = java.lang.Float.valueOf(r5)
            java.lang.String r6 = "opacity"
            r4.put(r6, r5)
            android.graphics.drawable.Drawable r5 = r3.getBackground()
            r6 = 4
            r8 = 2
            r9 = 0
            if (r5 == 0) goto L_0x01e4
            boolean r11 = r5 instanceof com.taobao.weex.ui.view.border.BorderDrawable
            if (r11 == 0) goto L_0x01e4
            com.taobao.weex.ui.view.border.BorderDrawable r5 = (com.taobao.weex.ui.view.border.BorderDrawable) r5
            android.graphics.RectF r11 = new android.graphics.RectF
            int r12 = r3.getWidth()
            float r12 = (float) r12
            int r13 = r3.getHeight()
            float r13 = (float) r13
            r14 = 0
            r11.<init>(r14, r14, r12, r13)
            float[] r5 = r5.getBorderRadius((android.graphics.RectF) r11)
            int r11 = r5.length
            r12 = 8
            if (r11 != r12) goto L_0x01e4
            r9 = r5[r7]
            double r9 = (double) r9
            r11 = r5[r8]
            double r11 = (double) r11
            r13 = 6
            r13 = r5[r13]
            double r13 = (double) r13
            r5 = r5[r6]
            r15 = r9
            double r8 = (double) r5
            r5 = r8
            r9 = r15
            goto L_0x01e7
        L_0x01e4:
            r5 = r9
            r11 = r5
            r13 = r11
        L_0x01e7:
            java.lang.Object[] r15 = new java.lang.Object[r7]
            double r9 = r1.nativeToWeb(r9, r15)
            java.lang.Double r9 = java.lang.Double.valueOf(r9)
            java.lang.String r10 = "border-top-left-radius"
            r4.put(r10, r9)
            java.lang.Object[] r9 = new java.lang.Object[r7]
            double r9 = r1.nativeToWeb(r11, r9)
            java.lang.Double r9 = java.lang.Double.valueOf(r9)
            java.lang.String r10 = "border-top-right-radius"
            r4.put(r10, r9)
            java.lang.Object[] r9 = new java.lang.Object[r7]
            double r9 = r1.nativeToWeb(r13, r9)
            java.lang.Double r9 = java.lang.Double.valueOf(r9)
            java.lang.String r10 = "border-bottom-left-radius"
            r4.put(r10, r9)
            java.lang.Object[] r9 = new java.lang.Object[r7]
            double r5 = r1.nativeToWeb(r5, r9)
            java.lang.Double r1 = java.lang.Double.valueOf(r5)
            java.lang.String r5 = "border-bottom-right-radius"
            r4.put(r5, r1)
            android.graphics.drawable.Drawable r1 = r3.getBackground()
            r5 = 3
            java.lang.String r6 = "rgba(%d,%d,%d,%f)"
            r9 = 4643176031446892544(0x406fe00000000000, double:255.0)
            r11 = 1
            if (r1 == 0) goto L_0x0297
            r1 = -16777216(0xffffffffff000000, float:-1.7014118E38)
            android.graphics.drawable.Drawable r12 = r3.getBackground()
            boolean r12 = r12 instanceof android.graphics.drawable.ColorDrawable
            if (r12 == 0) goto L_0x0247
            android.graphics.drawable.Drawable r1 = r3.getBackground()
            android.graphics.drawable.ColorDrawable r1 = (android.graphics.drawable.ColorDrawable) r1
            int r1 = r1.getColor()
            goto L_0x0259
        L_0x0247:
            android.graphics.drawable.Drawable r12 = r3.getBackground()
            boolean r12 = r12 instanceof com.taobao.weex.ui.view.border.BorderDrawable
            if (r12 == 0) goto L_0x0259
            android.graphics.drawable.Drawable r1 = r3.getBackground()
            com.taobao.weex.ui.view.border.BorderDrawable r1 = (com.taobao.weex.ui.view.border.BorderDrawable) r1
            int r1 = r1.getColor()
        L_0x0259:
            int r12 = android.graphics.Color.alpha(r1)
            double r12 = (double) r12
            java.lang.Double.isNaN(r12)
            double r12 = r12 / r9
            int r14 = android.graphics.Color.red(r1)
            int r15 = android.graphics.Color.green(r1)
            int r1 = android.graphics.Color.blue(r1)
            java.util.Locale r8 = java.util.Locale.getDefault()
            r9 = 4
            java.lang.Object[] r10 = new java.lang.Object[r9]
            java.lang.Integer r14 = java.lang.Integer.valueOf(r14)
            r10[r7] = r14
            java.lang.Integer r14 = java.lang.Integer.valueOf(r15)
            r10[r11] = r14
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
            r14 = 2
            r10[r14] = r1
            java.lang.Double r1 = java.lang.Double.valueOf(r12)
            r10[r5] = r1
            java.lang.String r1 = java.lang.String.format(r8, r6, r10)
            java.lang.String r8 = "background-color"
            r4.put(r8, r1)
        L_0x0297:
            boolean r1 = r2 instanceof com.taobao.weex.ui.component.WXText
            if (r1 == 0) goto L_0x030e
            boolean r1 = r3 instanceof com.taobao.weex.ui.view.WXTextView
            if (r1 == 0) goto L_0x030e
            com.taobao.weex.ui.view.WXTextView r3 = (com.taobao.weex.ui.view.WXTextView) r3
            android.text.Layout r1 = r3.getTextLayout()
            if (r1 == 0) goto L_0x030e
            java.lang.CharSequence r1 = r1.getText()
            if (r1 == 0) goto L_0x030e
            boolean r2 = r1 instanceof android.text.SpannableString
            if (r2 == 0) goto L_0x030e
            r2 = r1
            android.text.SpannableString r2 = (android.text.SpannableString) r2
            int r1 = r1.length()
            java.lang.Class<android.text.style.ForegroundColorSpan> r3 = android.text.style.ForegroundColorSpan.class
            java.lang.Object[] r1 = r2.getSpans(r7, r1, r3)
            android.text.style.ForegroundColorSpan[] r1 = (android.text.style.ForegroundColorSpan[]) r1
            if (r1 == 0) goto L_0x030e
            int r2 = r1.length
            if (r2 != r11) goto L_0x030e
            r1 = r1[r7]
            int r1 = r1.getForegroundColor()
            int r2 = android.graphics.Color.alpha(r1)
            double r2 = (double) r2
            java.lang.Double.isNaN(r2)
            r12 = 4643176031446892544(0x406fe00000000000, double:255.0)
            double r2 = r2 / r12
            int r8 = android.graphics.Color.red(r1)
            int r10 = android.graphics.Color.green(r1)
            int r1 = android.graphics.Color.blue(r1)
            java.util.Locale r12 = java.util.Locale.getDefault()
            r9 = 4
            java.lang.Object[] r9 = new java.lang.Object[r9]
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)
            r9[r7] = r8
            java.lang.Integer r7 = java.lang.Integer.valueOf(r10)
            r9[r11] = r7
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
            r7 = 2
            r9[r7] = r1
            java.lang.Double r1 = java.lang.Double.valueOf(r2)
            r9[r5] = r1
            java.lang.String r1 = java.lang.String.format(r12, r6, r9)
            java.lang.String r2 = "color"
            r4.put(r2, r1)
        L_0x030e:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.bindingx.plugin.weex.WXBindingXModule.getComputedStyle(java.lang.String):java.util.Map");
    }

    public void destroy() {
        WXBridgeManager.getInstance().post(new Runnable() {
            public void run() {
                if (WXBindingXModule.this.mBindingXCore != null) {
                    WXBindingXModule.this.mBindingXCore.doRelease();
                    BindingXCore unused = WXBindingXModule.this.mBindingXCore = null;
                }
                WXViewUpdateService.clearCallbacks();
            }
        }, (Object) null);
    }

    static PlatformManager createPlatformManager(WXSDKInstance wXSDKInstance) {
        final int instanceViewPortWidth = wXSDKInstance == null ? 750 : wXSDKInstance.getInstanceViewPortWidth();
        return new PlatformManager.Builder().withViewFinder(new PlatformManager.IViewFinder() {
            public View findViewBy(String str, Object... objArr) {
                if (objArr.length <= 0 || !(objArr[0] instanceof String)) {
                    return null;
                }
                return WXModuleUtils.findViewByRef(objArr[0], str);
            }
        }).withViewUpdater(new PlatformManager.IViewUpdater() {
            public void synchronouslyUpdateViewOnUIThread(View view, String str, Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, Map<String, Object> map, Object... objArr) {
                if (objArr != null && objArr.length >= 2 && (objArr[0] instanceof String) && (objArr[1] instanceof String)) {
                    String str2 = objArr[0];
                    String str3 = objArr[1];
                    WXComponent findComponentByRef = WXModuleUtils.findComponentByRef(str3, str2);
                    if (findComponentByRef == null) {
                        LogProxy.e("unexpected error. component not found [ref:" + str2 + ",instanceId:" + str3 + Operators.ARRAY_END_STR);
                        return;
                    }
                    WXViewUpdateService.findUpdater(str).update(findComponentByRef, view, obj, iDeviceResolutionTranslator, map);
                }
            }
        }).withDeviceResolutionTranslator(new PlatformManager.IDeviceResolutionTranslator() {
            public double webToNative(double d, Object... objArr) {
                return (double) WXViewUtils.getRealPxByWidth((float) d, instanceViewPortWidth);
            }

            public double nativeToWeb(double d, Object... objArr) {
                return (double) WXViewUtils.getWebPxByWidth((float) d, instanceViewPortWidth);
            }
        }).build();
    }

    public void onActivityPause() {
        WXBridgeManager.getInstance().post(new Runnable() {
            public void run() {
                if (WXBindingXModule.this.mBindingXCore != null) {
                    WXBindingXModule.this.mBindingXCore.onActivityPause();
                }
            }
        }, (Object) null);
    }

    public void onActivityResume() {
        WXBridgeManager.getInstance().post(new Runnable() {
            public void run() {
                if (WXBindingXModule.this.mBindingXCore != null) {
                    WXBindingXModule.this.mBindingXCore.onActivityResume();
                }
            }
        }, (Object) null);
    }
}
