package com.alibaba.android.bindingx.plugin.weex;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import com.alibaba.android.bindingx.core.LogProxy;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.alibaba.android.bindingx.core.WeakRunnable;
import com.alibaba.android.bindingx.core.internal.Utils;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.dom.transition.WXTransition;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXScroller;
import com.taobao.weex.ui.component.WXText;
import com.taobao.weex.ui.view.WXTextView;
import com.taobao.weex.ui.view.border.BorderDrawable;
import com.taobao.weex.utils.WXUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class WXViewUpdateService {
    private static final NOpUpdater EMPTY_INVOKER = new NOpUpdater();
    private static final List<String> LAYOUT_PROPERTIES = Arrays.asList(new String[]{"width", "height", LAYOUT_PROPERTY_MARGIN_LEFT, LAYOUT_PROPERTY_MARGIN_RIGHT, LAYOUT_PROPERTY_MARGIN_TOP, LAYOUT_PROPERTY_MARGIN_BOTTOM, "padding-left", "padding-right", LAYOUT_PROPERTY_PADDING_TOP, LAYOUT_PROPERTY_PADDING_BOTTOM});
    private static final String LAYOUT_PROPERTY_HEIGHT = "height";
    private static final String LAYOUT_PROPERTY_MARGIN_BOTTOM = "margin-bottom";
    private static final String LAYOUT_PROPERTY_MARGIN_LEFT = "margin-left";
    private static final String LAYOUT_PROPERTY_MARGIN_RIGHT = "margin-right";
    private static final String LAYOUT_PROPERTY_MARGIN_TOP = "margin-top";
    private static final String LAYOUT_PROPERTY_PADDING_BOTTOM = "padding-bottom";
    private static final String LAYOUT_PROPERTY_PADDING_LEFT = "padding-left";
    private static final String LAYOUT_PROPERTY_PADDING_RIGHT = "padding-right";
    private static final String LAYOUT_PROPERTY_PADDING_TOP = "padding-top";
    private static final String LAYOUT_PROPERTY_WIDTH = "width";
    private static final String PERSPECTIVE = "perspective";
    private static final String TRANSFORM_ORIGIN = "transformOrigin";
    private static final LayoutUpdater sLayoutUpdater = new LayoutUpdater();
    private static final Map<String, IWXViewUpdater> sTransformPropertyUpdaterMap;
    private static final Handler sUIHandler = new Handler(Looper.getMainLooper());

    WXViewUpdateService() {
    }

    static {
        HashMap hashMap = new HashMap();
        sTransformPropertyUpdaterMap = hashMap;
        hashMap.put("opacity", new OpacityUpdater());
        hashMap.put("transform.translate", new TranslateUpdater());
        hashMap.put("transform.translateX", new TranslateXUpdater());
        hashMap.put("transform.translateY", new TranslateYUpdater());
        hashMap.put("transform.scale", new ScaleUpdater());
        hashMap.put("transform.scaleX", new ScaleXUpdater());
        hashMap.put("transform.scaleY", new ScaleYUpdater());
        hashMap.put("transform.rotate", new RotateUpdater());
        hashMap.put("transform.rotateZ", new RotateUpdater());
        hashMap.put("transform.rotateX", new RotateXUpdater());
        hashMap.put("transform.rotateY", new RotateYUpdater());
        hashMap.put("background-color", new BackgroundUpdater());
        hashMap.put("color", new ColorUpdater());
        hashMap.put("scroll.contentOffset", new ContentOffsetUpdater());
        hashMap.put("scroll.contentOffsetX", new ContentOffsetXUpdater());
        hashMap.put("scroll.contentOffsetY", new ContentOffsetYUpdater());
        hashMap.put("border-top-left-radius", new BorderRadiusTopLeftUpdater());
        hashMap.put("border-top-right-radius", new BorderRadiusTopRightUpdater());
        hashMap.put("border-bottom-left-radius", new BorderRadiusBottomLeftUpdater());
        hashMap.put("border-bottom-right-radius", new BorderRadiusBottomRightUpdater());
        hashMap.put("border-radius", new BorderRadiusUpdater());
    }

    static IWXViewUpdater findUpdater(String str) {
        IWXViewUpdater iWXViewUpdater = sTransformPropertyUpdaterMap.get(str);
        if (iWXViewUpdater != null) {
            return iWXViewUpdater;
        }
        if (LAYOUT_PROPERTIES.contains(str)) {
            LayoutUpdater layoutUpdater = sLayoutUpdater;
            layoutUpdater.setPropertyName(str);
            return layoutUpdater;
        }
        LogProxy.e("unknown property [" + str + Operators.ARRAY_END_STR);
        return EMPTY_INVOKER;
    }

    private static final class NOpUpdater implements IWXViewUpdater {
        public void update(WXComponent wXComponent, View view, Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, Map<String, Object> map) {
        }

        private NOpUpdater() {
        }
    }

    /* access modifiers changed from: private */
    public static void runOnUIThread(Runnable runnable) {
        sUIHandler.post(new WeakRunnable(runnable));
    }

    public static void clearCallbacks() {
        sUIHandler.removeCallbacksAndMessages((Object) null);
    }

    private static final class ContentOffsetUpdater implements IWXViewUpdater {
        private ContentOffsetUpdater() {
        }

        public void update(WXComponent wXComponent, View view, Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, Map<String, Object> map) {
            final View access$2100 = WXViewUpdateService.findScrollTarget(wXComponent);
            if (access$2100 != null) {
                if (obj instanceof Double) {
                    final double doubleValue = ((Double) obj).doubleValue();
                    final PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator2 = iDeviceResolutionTranslator;
                    WXViewUpdateService.runOnUIThread(new Runnable() {
                        public void run() {
                            access$2100.setScrollX((int) WXViewUpdateService.getRealSize(doubleValue, iDeviceResolutionTranslator2));
                            access$2100.setScrollY((int) WXViewUpdateService.getRealSize(doubleValue, iDeviceResolutionTranslator2));
                        }
                    });
                } else if (obj instanceof ArrayList) {
                    ArrayList arrayList = (ArrayList) obj;
                    if (arrayList.size() >= 2 && (arrayList.get(0) instanceof Double) && (arrayList.get(1) instanceof Double)) {
                        final double doubleValue2 = ((Double) arrayList.get(0)).doubleValue();
                        final double doubleValue3 = ((Double) arrayList.get(1)).doubleValue();
                        final PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator3 = iDeviceResolutionTranslator;
                        WXViewUpdateService.runOnUIThread(new Runnable() {
                            public void run() {
                                access$2100.setScrollX((int) WXViewUpdateService.getRealSize(doubleValue2, iDeviceResolutionTranslator3));
                                access$2100.setScrollY((int) WXViewUpdateService.getRealSize(doubleValue3, iDeviceResolutionTranslator3));
                            }
                        });
                    }
                }
            }
        }
    }

    private static final class ContentOffsetXUpdater implements IWXViewUpdater {
        private ContentOffsetXUpdater() {
        }

        public void update(WXComponent wXComponent, View view, Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, Map<String, Object> map) {
            final View access$2100 = WXViewUpdateService.findScrollTarget(wXComponent);
            if (access$2100 != null && (obj instanceof Double)) {
                final double doubleValue = ((Double) obj).doubleValue();
                final PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator2 = iDeviceResolutionTranslator;
                WXViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        access$2100.setScrollX((int) WXViewUpdateService.getRealSize(doubleValue, iDeviceResolutionTranslator2));
                    }
                });
            }
        }
    }

    private static final class ContentOffsetYUpdater implements IWXViewUpdater {
        private ContentOffsetYUpdater() {
        }

        /* JADX WARNING: Code restructure failed: missing block: B:3:0x0005, code lost:
            r2 = com.alibaba.android.bindingx.plugin.weex.WXViewUpdateService.access$2100(r7);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void update(com.taobao.weex.ui.component.WXComponent r7, android.view.View r8, java.lang.Object r9, com.alibaba.android.bindingx.core.PlatformManager.IDeviceResolutionTranslator r10, java.util.Map<java.lang.String, java.lang.Object> r11) {
            /*
                r6 = this;
                boolean r8 = r9 instanceof java.lang.Double
                if (r8 != 0) goto L_0x0005
                return
            L_0x0005:
                android.view.View r2 = com.alibaba.android.bindingx.plugin.weex.WXViewUpdateService.findScrollTarget(r7)
                if (r2 != 0) goto L_0x000c
                return
            L_0x000c:
                java.lang.Double r9 = (java.lang.Double) r9
                double r3 = r9.doubleValue()
                com.alibaba.android.bindingx.plugin.weex.WXViewUpdateService$ContentOffsetYUpdater$1 r7 = new com.alibaba.android.bindingx.plugin.weex.WXViewUpdateService$ContentOffsetYUpdater$1
                r0 = r7
                r1 = r6
                r5 = r10
                r0.<init>(r2, r3, r5)
                com.alibaba.android.bindingx.plugin.weex.WXViewUpdateService.runOnUIThread(r7)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.bindingx.plugin.weex.WXViewUpdateService.ContentOffsetYUpdater.update(com.taobao.weex.ui.component.WXComponent, android.view.View, java.lang.Object, com.alibaba.android.bindingx.core.PlatformManager$IDeviceResolutionTranslator, java.util.Map):void");
        }
    }

    private static final class OpacityUpdater implements IWXViewUpdater {
        private OpacityUpdater() {
        }

        public void update(WXComponent wXComponent, final View view, Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, Map<String, Object> map) {
            if (obj instanceof Double) {
                final float doubleValue = (float) ((Double) obj).doubleValue();
                WXViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        view.setAlpha(doubleValue);
                    }
                });
            }
        }
    }

    private static final class TranslateUpdater implements IWXViewUpdater {
        private TranslateUpdater() {
        }

        public void update(WXComponent wXComponent, View view, Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, Map<String, Object> map) {
            if (obj instanceof ArrayList) {
                ArrayList arrayList = (ArrayList) obj;
                if (arrayList.size() >= 2 && (arrayList.get(0) instanceof Double) && (arrayList.get(1) instanceof Double)) {
                    final double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                    final double doubleValue2 = ((Double) arrayList.get(1)).doubleValue();
                    final View view2 = view;
                    final PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator2 = iDeviceResolutionTranslator;
                    WXViewUpdateService.runOnUIThread(new Runnable() {
                        public void run() {
                            view2.setTranslationX((float) WXViewUpdateService.getRealSize(doubleValue, iDeviceResolutionTranslator2));
                            view2.setTranslationY((float) WXViewUpdateService.getRealSize(doubleValue2, iDeviceResolutionTranslator2));
                        }
                    });
                }
            }
        }
    }

    private static final class TranslateXUpdater implements IWXViewUpdater {
        private TranslateXUpdater() {
        }

        public void update(WXComponent wXComponent, View view, Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, Map<String, Object> map) {
            if (obj instanceof Double) {
                final double doubleValue = ((Double) obj).doubleValue();
                final View view2 = view;
                final PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator2 = iDeviceResolutionTranslator;
                WXViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        view2.setTranslationX((float) WXViewUpdateService.getRealSize(doubleValue, iDeviceResolutionTranslator2));
                    }
                });
            }
        }
    }

    private static final class TranslateYUpdater implements IWXViewUpdater {
        private TranslateYUpdater() {
        }

        public void update(WXComponent wXComponent, View view, Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, Map<String, Object> map) {
            if (obj instanceof Double) {
                final double doubleValue = ((Double) obj).doubleValue();
                final View view2 = view;
                final PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator2 = iDeviceResolutionTranslator;
                WXViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        view2.setTranslationY((float) WXViewUpdateService.getRealSize(doubleValue, iDeviceResolutionTranslator2));
                    }
                });
            }
        }
    }

    private static final class ScaleUpdater implements IWXViewUpdater {
        private ScaleUpdater() {
        }

        public void update(WXComponent wXComponent, final View view, final Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, final Map<String, Object> map) {
            WXViewUpdateService.runOnUIThread(new Runnable() {
                public void run() {
                    int normalizedPerspectiveValue = Utils.normalizedPerspectiveValue(view.getContext(), WXUtils.getInt(map.get("perspective")));
                    Pair<Float, Float> parseTransformOrigin = Utils.parseTransformOrigin(WXUtils.getString(map.get("transformOrigin"), (String) null), view);
                    if (normalizedPerspectiveValue != 0) {
                        view.setCameraDistance((float) normalizedPerspectiveValue);
                    }
                    if (parseTransformOrigin != null) {
                        view.setPivotX(((Float) parseTransformOrigin.first).floatValue());
                        view.setPivotY(((Float) parseTransformOrigin.second).floatValue());
                    }
                    Object obj = obj;
                    if (obj instanceof Double) {
                        float doubleValue = (float) ((Double) obj).doubleValue();
                        view.setScaleX(doubleValue);
                        view.setScaleY(doubleValue);
                    } else if (obj instanceof ArrayList) {
                        ArrayList arrayList = (ArrayList) obj;
                        if (arrayList.size() >= 2 && (arrayList.get(0) instanceof Double) && (arrayList.get(1) instanceof Double)) {
                            double doubleValue2 = ((Double) arrayList.get(0)).doubleValue();
                            double doubleValue3 = ((Double) arrayList.get(1)).doubleValue();
                            view.setScaleX((float) doubleValue2);
                            view.setScaleY((float) doubleValue3);
                        }
                    }
                }
            });
        }
    }

    private static final class ScaleXUpdater implements IWXViewUpdater {
        private ScaleXUpdater() {
        }

        public void update(WXComponent wXComponent, final View view, final Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, final Map<String, Object> map) {
            if (obj instanceof Double) {
                WXViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        Pair<Float, Float> parseTransformOrigin = Utils.parseTransformOrigin(WXUtils.getString(map.get("transformOrigin"), (String) null), view);
                        if (parseTransformOrigin != null) {
                            view.setPivotX(((Float) parseTransformOrigin.first).floatValue());
                            view.setPivotY(((Float) parseTransformOrigin.second).floatValue());
                        }
                        view.setScaleX((float) ((Double) obj).doubleValue());
                    }
                });
            }
        }
    }

    private static final class ScaleYUpdater implements IWXViewUpdater {
        private ScaleYUpdater() {
        }

        public void update(WXComponent wXComponent, final View view, final Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, final Map<String, Object> map) {
            if (obj instanceof Double) {
                WXViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        Pair<Float, Float> parseTransformOrigin = Utils.parseTransformOrigin(WXUtils.getString(map.get("transformOrigin"), (String) null), view);
                        if (parseTransformOrigin != null) {
                            view.setPivotX(((Float) parseTransformOrigin.first).floatValue());
                            view.setPivotY(((Float) parseTransformOrigin.second).floatValue());
                        }
                        view.setScaleY((float) ((Double) obj).doubleValue());
                    }
                });
            }
        }
    }

    private static final class RotateUpdater implements IWXViewUpdater {
        private RotateUpdater() {
        }

        public void update(WXComponent wXComponent, final View view, final Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, final Map<String, Object> map) {
            if (obj instanceof Double) {
                WXViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        int normalizedPerspectiveValue = Utils.normalizedPerspectiveValue(view.getContext(), WXUtils.getInt(map.get("perspective")));
                        Pair<Float, Float> parseTransformOrigin = Utils.parseTransformOrigin(WXUtils.getString(map.get("transformOrigin"), (String) null), view);
                        if (normalizedPerspectiveValue != 0) {
                            view.setCameraDistance((float) normalizedPerspectiveValue);
                        }
                        if (parseTransformOrigin != null) {
                            view.setPivotX(((Float) parseTransformOrigin.first).floatValue());
                            view.setPivotY(((Float) parseTransformOrigin.second).floatValue());
                        }
                        view.setRotation((float) ((Double) obj).doubleValue());
                    }
                });
            }
        }
    }

    private static final class RotateXUpdater implements IWXViewUpdater {
        private RotateXUpdater() {
        }

        public void update(WXComponent wXComponent, final View view, final Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, final Map<String, Object> map) {
            if (obj instanceof Double) {
                WXViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        int normalizedPerspectiveValue = Utils.normalizedPerspectiveValue(view.getContext(), WXUtils.getInt(map.get("perspective")));
                        Pair<Float, Float> parseTransformOrigin = Utils.parseTransformOrigin(WXUtils.getString(map.get("transformOrigin"), (String) null), view);
                        if (normalizedPerspectiveValue != 0) {
                            view.setCameraDistance((float) normalizedPerspectiveValue);
                        }
                        if (parseTransformOrigin != null) {
                            view.setPivotX(((Float) parseTransformOrigin.first).floatValue());
                            view.setPivotY(((Float) parseTransformOrigin.second).floatValue());
                        }
                        view.setRotationX((float) ((Double) obj).doubleValue());
                    }
                });
            }
        }
    }

    private static final class RotateYUpdater implements IWXViewUpdater {
        private RotateYUpdater() {
        }

        public void update(WXComponent wXComponent, final View view, final Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, final Map<String, Object> map) {
            if (obj instanceof Double) {
                WXViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        int normalizedPerspectiveValue = Utils.normalizedPerspectiveValue(view.getContext(), WXUtils.getInt(map.get("perspective")));
                        Pair<Float, Float> parseTransformOrigin = Utils.parseTransformOrigin(WXUtils.getString(map.get("transformOrigin"), (String) null), view);
                        if (normalizedPerspectiveValue != 0) {
                            view.setCameraDistance((float) normalizedPerspectiveValue);
                        }
                        if (parseTransformOrigin != null) {
                            view.setPivotX(((Float) parseTransformOrigin.first).floatValue());
                            view.setPivotY(((Float) parseTransformOrigin.second).floatValue());
                        }
                        view.setRotationY((float) ((Double) obj).doubleValue());
                    }
                });
            }
        }
    }

    static final class LayoutUpdater implements IWXViewUpdater {
        private String propertyName;

        LayoutUpdater() {
        }

        /* access modifiers changed from: package-private */
        public void setPropertyName(String str) {
            this.propertyName = str;
        }

        public void update(WXComponent wXComponent, View view, Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, Map<String, Object> map) {
            if ((obj instanceof Double) && !TextUtils.isEmpty(this.propertyName)) {
                double doubleValue = ((Double) obj).doubleValue();
                String str = this.propertyName;
                str.hashCode();
                char c = 65535;
                String str2 = "width";
                switch (str.hashCode()) {
                    case -1502084711:
                        if (str.equals(WXViewUpdateService.LAYOUT_PROPERTY_PADDING_TOP)) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1221029593:
                        if (str.equals("height")) {
                            c = 1;
                            break;
                        }
                        break;
                    case -887955139:
                        if (str.equals(WXViewUpdateService.LAYOUT_PROPERTY_MARGIN_RIGHT)) {
                            c = 2;
                            break;
                        }
                        break;
                    case -396426912:
                        if (str.equals("padding-right")) {
                            c = 3;
                            break;
                        }
                        break;
                    case 113126854:
                        if (str.equals(str2)) {
                            c = 4;
                            break;
                        }
                        break;
                    case 143541095:
                        if (str.equals(WXViewUpdateService.LAYOUT_PROPERTY_PADDING_BOTTOM)) {
                            c = 5;
                            break;
                        }
                        break;
                    case 679766083:
                        if (str.equals("padding-left")) {
                            c = 6;
                            break;
                        }
                        break;
                    case 941004998:
                        if (str.equals(WXViewUpdateService.LAYOUT_PROPERTY_MARGIN_LEFT)) {
                            c = 7;
                            break;
                        }
                        break;
                    case 1970025654:
                        if (str.equals(WXViewUpdateService.LAYOUT_PROPERTY_MARGIN_TOP)) {
                            c = 8;
                            break;
                        }
                        break;
                    case 2086035242:
                        if (str.equals(WXViewUpdateService.LAYOUT_PROPERTY_MARGIN_BOTTOM)) {
                            c = 9;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        str2 = Constants.Name.PADDING_TOP;
                        break;
                    case 1:
                        str2 = "height";
                        break;
                    case 2:
                        str2 = Constants.Name.MARGIN_RIGHT;
                        break;
                    case 3:
                        str2 = Constants.Name.PADDING_RIGHT;
                        break;
                    case 4:
                        break;
                    case 5:
                        str2 = Constants.Name.PADDING_BOTTOM;
                        break;
                    case 6:
                        str2 = Constants.Name.PADDING_LEFT;
                        break;
                    case 7:
                        str2 = Constants.Name.MARGIN_LEFT;
                        break;
                    case 8:
                        str2 = Constants.Name.MARGIN_TOP;
                        break;
                    case 9:
                        str2 = Constants.Name.MARGIN_BOTTOM;
                        break;
                    default:
                        str2 = null;
                        break;
                }
                if (!TextUtils.isEmpty(str2)) {
                    WXTransition.asynchronouslyUpdateLayout(wXComponent, str2, (float) WXViewUpdateService.getRealSize(doubleValue, iDeviceResolutionTranslator));
                    this.propertyName = null;
                }
            }
        }
    }

    private static final class BackgroundUpdater implements IWXViewUpdater {
        private BackgroundUpdater() {
        }

        public void update(WXComponent wXComponent, final View view, Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, Map<String, Object> map) {
            if (obj instanceof Integer) {
                final int intValue = ((Integer) obj).intValue();
                WXViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        Drawable background = view.getBackground();
                        if (background == null) {
                            view.setBackgroundColor(intValue);
                        } else if (background instanceof BorderDrawable) {
                            ((BorderDrawable) background).setColor(intValue);
                        } else if (background instanceof ColorDrawable) {
                            ((ColorDrawable) background).setColor(intValue);
                        }
                    }
                });
            }
        }
    }

    private static final class ColorUpdater implements IWXViewUpdater {
        private ColorUpdater() {
        }

        public void update(final WXComponent wXComponent, final View view, Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, Map<String, Object> map) {
            if (obj instanceof Integer) {
                final int intValue = ((Integer) obj).intValue();
                WXViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        View view = view;
                        if (view instanceof TextView) {
                            ((TextView) view).setTextColor(intValue);
                        } else if ((wXComponent instanceof WXText) && (view instanceof WXTextView)) {
                            try {
                                ((WXTextView) view).setTextColor(intValue);
                                view.invalidate();
                            } catch (Throwable th) {
                                LogProxy.e("can not update text color, try fallback to call the old API", th);
                                Layout textLayout = ((WXTextView) view).getTextLayout();
                                if (textLayout != null) {
                                    TextPaint paint = textLayout.getPaint();
                                    if (paint != null) {
                                        paint.setColor(intValue);
                                    }
                                    view.invalidate();
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    private static final class BorderRadiusTopLeftUpdater implements IWXViewUpdater {
        private BorderRadiusTopLeftUpdater() {
        }

        public void update(WXComponent wXComponent, View view, Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, Map<String, Object> map) {
            if (obj instanceof Double) {
                final double doubleValue = ((Double) obj).doubleValue();
                final View view2 = view;
                final PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator2 = iDeviceResolutionTranslator;
                WXViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        Drawable background = view2.getBackground();
                        if (background != null && (background instanceof BorderDrawable)) {
                            ((BorderDrawable) background).setBorderRadius(CSSShorthand.CORNER.BORDER_TOP_LEFT, (float) WXViewUpdateService.getRealSize(doubleValue, iDeviceResolutionTranslator2));
                        }
                    }
                });
            }
        }
    }

    private static final class BorderRadiusTopRightUpdater implements IWXViewUpdater {
        private BorderRadiusTopRightUpdater() {
        }

        public void update(WXComponent wXComponent, View view, Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, Map<String, Object> map) {
            if (obj instanceof Double) {
                final double doubleValue = ((Double) obj).doubleValue();
                final View view2 = view;
                final PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator2 = iDeviceResolutionTranslator;
                WXViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        Drawable background = view2.getBackground();
                        if (background != null && (background instanceof BorderDrawable)) {
                            ((BorderDrawable) background).setBorderRadius(CSSShorthand.CORNER.BORDER_TOP_RIGHT, (float) WXViewUpdateService.getRealSize(doubleValue, iDeviceResolutionTranslator2));
                        }
                    }
                });
            }
        }
    }

    private static final class BorderRadiusBottomLeftUpdater implements IWXViewUpdater {
        private BorderRadiusBottomLeftUpdater() {
        }

        public void update(WXComponent wXComponent, View view, Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, Map<String, Object> map) {
            if (obj instanceof Double) {
                final double doubleValue = ((Double) obj).doubleValue();
                final View view2 = view;
                final PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator2 = iDeviceResolutionTranslator;
                WXViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        Drawable background = view2.getBackground();
                        if (background != null && (background instanceof BorderDrawable)) {
                            ((BorderDrawable) background).setBorderRadius(CSSShorthand.CORNER.BORDER_BOTTOM_LEFT, (float) WXViewUpdateService.getRealSize(doubleValue, iDeviceResolutionTranslator2));
                        }
                    }
                });
            }
        }
    }

    private static final class BorderRadiusBottomRightUpdater implements IWXViewUpdater {
        private BorderRadiusBottomRightUpdater() {
        }

        public void update(WXComponent wXComponent, View view, Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, Map<String, Object> map) {
            if (obj instanceof Double) {
                final double doubleValue = ((Double) obj).doubleValue();
                final View view2 = view;
                final PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator2 = iDeviceResolutionTranslator;
                WXViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        Drawable background = view2.getBackground();
                        if (background != null && (background instanceof BorderDrawable)) {
                            ((BorderDrawable) background).setBorderRadius(CSSShorthand.CORNER.BORDER_BOTTOM_RIGHT, (float) WXViewUpdateService.getRealSize(doubleValue, iDeviceResolutionTranslator2));
                        }
                    }
                });
            }
        }
    }

    private static final class BorderRadiusUpdater implements IWXViewUpdater {
        private BorderRadiusUpdater() {
        }

        public void update(WXComponent wXComponent, final View view, Object obj, final PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, Map<String, Object> map) {
            if (obj instanceof ArrayList) {
                final ArrayList arrayList = (ArrayList) obj;
                if (arrayList.size() == 4) {
                    WXViewUpdateService.runOnUIThread(new Runnable() {
                        public void run() {
                            Drawable background = view.getBackground();
                            if (background != null && (background instanceof BorderDrawable)) {
                                double d = 0.0d;
                                double doubleValue = arrayList.get(0) instanceof Double ? ((Double) arrayList.get(0)).doubleValue() : 0.0d;
                                double doubleValue2 = arrayList.get(1) instanceof Double ? ((Double) arrayList.get(1)).doubleValue() : 0.0d;
                                double doubleValue3 = arrayList.get(2) instanceof Double ? ((Double) arrayList.get(2)).doubleValue() : 0.0d;
                                if (arrayList.get(3) instanceof Double) {
                                    d = ((Double) arrayList.get(3)).doubleValue();
                                }
                                BorderDrawable borderDrawable = (BorderDrawable) background;
                                borderDrawable.setBorderRadius(CSSShorthand.CORNER.BORDER_TOP_LEFT, (float) WXViewUpdateService.getRealSize(doubleValue, iDeviceResolutionTranslator));
                                borderDrawable.setBorderRadius(CSSShorthand.CORNER.BORDER_TOP_RIGHT, (float) WXViewUpdateService.getRealSize(doubleValue2, iDeviceResolutionTranslator));
                                borderDrawable.setBorderRadius(CSSShorthand.CORNER.BORDER_BOTTOM_LEFT, (float) WXViewUpdateService.getRealSize(doubleValue3, iDeviceResolutionTranslator));
                                borderDrawable.setBorderRadius(CSSShorthand.CORNER.BORDER_BOTTOM_RIGHT, (float) WXViewUpdateService.getRealSize(d, iDeviceResolutionTranslator));
                            }
                        }
                    });
                }
            } else if (obj instanceof Double) {
                final double doubleValue = ((Double) obj).doubleValue();
                final View view2 = view;
                final PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator2 = iDeviceResolutionTranslator;
                WXViewUpdateService.runOnUIThread(new Runnable() {
                    public void run() {
                        Drawable background = view2.getBackground();
                        if (background != null && (background instanceof BorderDrawable)) {
                            BorderDrawable borderDrawable = (BorderDrawable) background;
                            borderDrawable.setBorderRadius(CSSShorthand.CORNER.BORDER_TOP_LEFT, (float) WXViewUpdateService.getRealSize(doubleValue, iDeviceResolutionTranslator2));
                            borderDrawable.setBorderRadius(CSSShorthand.CORNER.BORDER_TOP_RIGHT, (float) WXViewUpdateService.getRealSize(doubleValue, iDeviceResolutionTranslator2));
                            borderDrawable.setBorderRadius(CSSShorthand.CORNER.BORDER_BOTTOM_LEFT, (float) WXViewUpdateService.getRealSize(doubleValue, iDeviceResolutionTranslator2));
                            borderDrawable.setBorderRadius(CSSShorthand.CORNER.BORDER_BOTTOM_RIGHT, (float) WXViewUpdateService.getRealSize(doubleValue, iDeviceResolutionTranslator2));
                        }
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public static double getRealSize(double d, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator) {
        return iDeviceResolutionTranslator.webToNative(d, new Object[0]);
    }

    /* access modifiers changed from: private */
    public static View findScrollTarget(WXComponent wXComponent) {
        if (wXComponent instanceof WXScroller) {
            return ((WXScroller) wXComponent).getInnerView();
        }
        LogProxy.e("scroll offset only support on Scroller Component");
        return null;
    }
}
