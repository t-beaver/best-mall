package com.taobao.weex.dom.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Interpolator;
import androidx.collection.ArrayMap;
import androidx.core.view.animation.PathInterpolatorCompat;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.ui.animation.BackgroundColorProperty;
import com.taobao.weex.ui.animation.TransformParser;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.utils.SingleFunctionParser;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXResourceUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class WXTransition {
    private static final Set<String> LAYOUT_PROPERTIES;
    public static final Pattern PROPERTY_SPLIT_PATTERN = Pattern.compile("\\||,");
    private static final Set<String> TRANSFORM_PROPERTIES;
    public static final String TRANSITION_DELAY = "transitionDelay";
    public static final String TRANSITION_DURATION = "transitionDuration";
    public static final String TRANSITION_PROPERTY = "transitionProperty";
    public static final String TRANSITION_TIMING_FUNCTION = "transitionTimingFunction";
    /* access modifiers changed from: private */
    public Runnable animationRunnable;
    private long delay;
    /* access modifiers changed from: private */
    public long duration;
    private Handler handler = new Handler();
    private Interpolator interpolator;
    private Map<String, Object> layoutPendingUpdates = new ArrayMap();
    private ValueAnimator layoutValueAnimator;
    /* access modifiers changed from: private */
    public volatile AtomicInteger lockToken = new AtomicInteger(0);
    /* access modifiers changed from: private */
    public WXComponent mWXComponent;
    private List<String> properties = new ArrayList(4);
    private Map<String, Object> targetStyles = new ArrayMap();
    private Runnable transformAnimationRunnable;
    private ObjectAnimator transformAnimator;
    private Map<String, Object> transformPendingUpdates = new ArrayMap();
    /* access modifiers changed from: private */
    public Runnable transitionEndEvent;

    static {
        HashSet hashSet = new HashSet();
        LAYOUT_PROPERTIES = hashSet;
        hashSet.add("width");
        hashSet.add("height");
        hashSet.add(Constants.Name.MARGIN_TOP);
        hashSet.add(Constants.Name.MARGIN_BOTTOM);
        hashSet.add(Constants.Name.MARGIN_LEFT);
        hashSet.add(Constants.Name.MARGIN_RIGHT);
        hashSet.add("left");
        hashSet.add("right");
        hashSet.add("top");
        hashSet.add("bottom");
        hashSet.add(Constants.Name.PADDING_LEFT);
        hashSet.add(Constants.Name.PADDING_RIGHT);
        hashSet.add(Constants.Name.PADDING_TOP);
        hashSet.add(Constants.Name.PADDING_BOTTOM);
        HashSet hashSet2 = new HashSet();
        TRANSFORM_PROPERTIES = hashSet2;
        hashSet2.add("opacity");
        hashSet2.add("backgroundColor");
        hashSet2.add("transform");
    }

    public static WXTransition fromMap(Map<String, Object> map, WXComponent wXComponent) {
        String string;
        if (map.get(TRANSITION_PROPERTY) == null || (string = WXUtils.getString(map.get(TRANSITION_PROPERTY), (String) null)) == null) {
            return null;
        }
        WXTransition wXTransition = new WXTransition();
        updateTransitionProperties(wXTransition, string);
        if (wXTransition.properties.isEmpty()) {
            return null;
        }
        wXTransition.duration = parseTimeMillis(map, TRANSITION_DURATION, 0);
        wXTransition.delay = parseTimeMillis(map, TRANSITION_DELAY, 0);
        wXTransition.interpolator = createTimeInterpolator(WXUtils.getString(map.get(TRANSITION_TIMING_FUNCTION), (String) null));
        wXTransition.mWXComponent = wXComponent;
        return wXTransition;
    }

    public boolean hasTransitionProperty(Map<String, Object> map) {
        for (String containsKey : this.properties) {
            if (map.containsKey(containsKey)) {
                return true;
            }
        }
        return false;
    }

    public void updateTranstionParams(Map<String, Object> map) {
        if (map.containsKey(TRANSITION_DELAY)) {
            this.mWXComponent.getStyles().put(TRANSITION_DELAY, map.remove(TRANSITION_DELAY));
            this.delay = parseTimeMillis(this.mWXComponent.getStyles(), TRANSITION_DELAY, 0);
        }
        if (map.containsKey(TRANSITION_TIMING_FUNCTION) && map.get(TRANSITION_TIMING_FUNCTION) != null) {
            this.mWXComponent.getStyles().put(TRANSITION_TIMING_FUNCTION, map.remove(TRANSITION_TIMING_FUNCTION));
            this.interpolator = createTimeInterpolator(this.mWXComponent.getStyles().get(TRANSITION_TIMING_FUNCTION).toString());
        }
        if (map.containsKey(TRANSITION_DURATION)) {
            this.mWXComponent.getStyles().put(TRANSITION_DURATION, map.remove(TRANSITION_DURATION));
            this.duration = parseTimeMillis(this.mWXComponent.getStyles(), TRANSITION_DURATION, 0);
        }
        if (map.containsKey(TRANSITION_PROPERTY)) {
            this.mWXComponent.getStyles().put(TRANSITION_PROPERTY, map.remove(TRANSITION_PROPERTY));
            updateTransitionProperties(this, WXUtils.getString(this.mWXComponent.getStyles().get(TRANSITION_PROPERTY), (String) null));
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0080, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void startTransition(java.util.Map<java.lang.String, java.lang.Object> r8) {
        /*
            r7 = this;
            java.util.concurrent.atomic.AtomicInteger r0 = r7.lockToken
            monitor-enter(r0)
            android.view.View r1 = r7.getTargetView()     // Catch:{ all -> 0x0081 }
            if (r1 != 0) goto L_0x000b
            monitor-exit(r0)     // Catch:{ all -> 0x0081 }
            return
        L_0x000b:
            java.util.concurrent.atomic.AtomicInteger r1 = r7.lockToken     // Catch:{ all -> 0x0081 }
            int r1 = r1.incrementAndGet()     // Catch:{ all -> 0x0081 }
            java.util.List<java.lang.String> r2 = r7.properties     // Catch:{ all -> 0x0081 }
            java.util.Iterator r2 = r2.iterator()     // Catch:{ all -> 0x0081 }
        L_0x0017:
            boolean r3 = r2.hasNext()     // Catch:{ all -> 0x0081 }
            if (r3 == 0) goto L_0x0049
            java.lang.Object r3 = r2.next()     // Catch:{ all -> 0x0081 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ all -> 0x0081 }
            boolean r4 = r8.containsKey(r3)     // Catch:{ all -> 0x0081 }
            if (r4 == 0) goto L_0x0017
            java.lang.Object r4 = r8.remove(r3)     // Catch:{ all -> 0x0081 }
            java.util.Set<java.lang.String> r5 = LAYOUT_PROPERTIES     // Catch:{ all -> 0x0081 }
            boolean r5 = r5.contains(r3)     // Catch:{ all -> 0x0081 }
            if (r5 == 0) goto L_0x003b
            java.util.Map<java.lang.String, java.lang.Object> r5 = r7.layoutPendingUpdates     // Catch:{ all -> 0x0081 }
            r5.put(r3, r4)     // Catch:{ all -> 0x0081 }
            goto L_0x0017
        L_0x003b:
            java.util.Set<java.lang.String> r5 = TRANSFORM_PROPERTIES     // Catch:{ all -> 0x0081 }
            boolean r5 = r5.contains(r3)     // Catch:{ all -> 0x0081 }
            if (r5 == 0) goto L_0x0017
            java.util.Map<java.lang.String, java.lang.Object> r5 = r7.transformPendingUpdates     // Catch:{ all -> 0x0081 }
            r5.put(r3, r4)     // Catch:{ all -> 0x0081 }
            goto L_0x0017
        L_0x0049:
            com.taobao.weex.ui.component.WXComponent r8 = r7.mWXComponent     // Catch:{ all -> 0x0081 }
            com.taobao.weex.dom.WXAttr r8 = r8.getAttrs()     // Catch:{ all -> 0x0081 }
            java.lang.String r2 = "actionDelay"
            java.lang.Object r8 = r8.get(r2)     // Catch:{ all -> 0x0081 }
            r2 = 16
            int r8 = com.taobao.weex.utils.WXUtils.getNumberInt(r8, r2)     // Catch:{ all -> 0x0081 }
            long r2 = (long) r8     // Catch:{ all -> 0x0081 }
            long r4 = r7.duration     // Catch:{ all -> 0x0081 }
            int r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r6 <= 0) goto L_0x0063
            int r8 = (int) r4     // Catch:{ all -> 0x0081 }
        L_0x0063:
            java.lang.Runnable r2 = r7.animationRunnable     // Catch:{ all -> 0x0081 }
            if (r2 == 0) goto L_0x006c
            android.os.Handler r3 = r7.handler     // Catch:{ all -> 0x0081 }
            r3.removeCallbacks(r2)     // Catch:{ all -> 0x0081 }
        L_0x006c:
            com.taobao.weex.dom.transition.WXTransition$1 r2 = new com.taobao.weex.dom.transition.WXTransition$1     // Catch:{ all -> 0x0081 }
            r2.<init>(r1)     // Catch:{ all -> 0x0081 }
            r7.animationRunnable = r2     // Catch:{ all -> 0x0081 }
            if (r8 <= 0) goto L_0x007c
            android.os.Handler r1 = r7.handler     // Catch:{ all -> 0x0081 }
            long r3 = (long) r8     // Catch:{ all -> 0x0081 }
            r1.postDelayed(r2, r3)     // Catch:{ all -> 0x0081 }
            goto L_0x007f
        L_0x007c:
            r2.run()     // Catch:{ all -> 0x0081 }
        L_0x007f:
            monitor-exit(r0)     // Catch:{ all -> 0x0081 }
            return
        L_0x0081:
            r8 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0081 }
            goto L_0x0085
        L_0x0084:
            throw r8
        L_0x0085:
            goto L_0x0084
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.dom.transition.WXTransition.startTransition(java.util.Map):void");
    }

    /* access modifiers changed from: private */
    public void doTransitionAnimation(final int i) {
        View targetView = getTargetView();
        if (targetView != null) {
            if (this.targetStyles.size() > 0) {
                for (String next : this.properties) {
                    if ((LAYOUT_PROPERTIES.contains(next) || TRANSFORM_PROPERTIES.contains(next)) && !this.layoutPendingUpdates.containsKey(next) && !this.transformPendingUpdates.containsKey(next)) {
                        synchronized (this.targetStyles) {
                            if (this.targetStyles.containsKey(next)) {
                                this.mWXComponent.getStyles().put(next, this.targetStyles.remove(next));
                            }
                        }
                    }
                }
            }
            Runnable runnable = this.transitionEndEvent;
            if (runnable != null) {
                targetView.removeCallbacks(runnable);
            }
            if (this.transitionEndEvent == null && ((float) this.duration) > Float.MIN_NORMAL) {
                this.transitionEndEvent = new Runnable() {
                    public void run() {
                        Runnable unused = WXTransition.this.transitionEndEvent = null;
                        if (((float) WXTransition.this.duration) >= Float.MIN_NORMAL && WXTransition.this.mWXComponent != null && WXTransition.this.mWXComponent.getEvents().contains(Constants.Event.ON_TRANSITION_END)) {
                            WXTransition.this.mWXComponent.fireEvent(Constants.Event.ON_TRANSITION_END);
                        }
                    }
                };
            }
            Runnable runnable2 = this.transformAnimationRunnable;
            if (runnable2 != null) {
                targetView.removeCallbacks(runnable2);
            }
            AnonymousClass3 r1 = new Runnable() {
                public void run() {
                    synchronized (WXTransition.this.lockToken) {
                        if (i == WXTransition.this.lockToken.get()) {
                            WXTransition.this.doPendingTransformAnimation(i);
                        }
                    }
                }
            };
            this.transformAnimationRunnable = r1;
            targetView.post(r1);
            doPendingLayoutAnimation();
        }
    }

    /* access modifiers changed from: private */
    public void doPendingTransformAnimation(int i) {
        View targetView;
        ObjectAnimator objectAnimator = this.transformAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
            this.transformAnimator = null;
        }
        if (this.transformPendingUpdates.size() != 0 && (targetView = getTargetView()) != null && this.mWXComponent.getInstance() != null) {
            ArrayList arrayList = new ArrayList(8);
            String string = WXUtils.getString(this.transformPendingUpdates.remove("transform"), (String) null);
            if (!TextUtils.isEmpty(string)) {
                for (PropertyValuesHolder add : TransformParser.toHolders(TransformParser.parseTransForm(this.mWXComponent.getInstanceId(), string, (int) this.mWXComponent.getLayoutWidth(), (int) this.mWXComponent.getLayoutHeight(), this.mWXComponent.getViewPortWidthForFloat()))) {
                    arrayList.add(add);
                }
                synchronized (this.targetStyles) {
                    this.targetStyles.put("transform", string);
                }
            }
            for (String next : this.properties) {
                if (TRANSFORM_PROPERTIES.contains(next) && this.transformPendingUpdates.containsKey(next)) {
                    Object remove = this.transformPendingUpdates.remove(next);
                    synchronized (this.targetStyles) {
                        this.targetStyles.put(next, remove);
                    }
                    next.hashCode();
                    if (next.equals("opacity")) {
                        arrayList.add(PropertyValuesHolder.ofFloat(View.ALPHA, new float[]{targetView.getAlpha(), WXUtils.getFloat(remove, Float.valueOf(1.0f)).floatValue()}));
                        targetView.setLayerType(1, (Paint) null);
                    } else if (next.equals("backgroundColor")) {
                        int color = WXResourceUtils.getColor(WXUtils.getString(this.mWXComponent.getStyles().getBackgroundColor(), (String) null), 0);
                        int color2 = WXResourceUtils.getColor(WXUtils.getString(remove, (String) null), 0);
                        if (WXViewUtils.getBorderDrawable(targetView) != null) {
                            color = WXViewUtils.getBorderDrawable(targetView).getColor();
                        } else if (targetView.getBackground() instanceof ColorDrawable) {
                            color = ((ColorDrawable) targetView.getBackground()).getColor();
                        }
                        arrayList.add(PropertyValuesHolder.ofObject(new BackgroundColorProperty(), new ArgbEvaluator(), new Integer[]{Integer.valueOf(color), Integer.valueOf(color2)}));
                    }
                }
            }
            if (i == this.lockToken.get()) {
                this.transformPendingUpdates.clear();
            }
            ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(targetView, (PropertyValuesHolder[]) arrayList.toArray(new PropertyValuesHolder[arrayList.size()]));
            this.transformAnimator = ofPropertyValuesHolder;
            ofPropertyValuesHolder.setDuration(this.duration);
            long j = this.delay;
            if (j > 0) {
                this.transformAnimator.setStartDelay(j);
            }
            Interpolator interpolator2 = this.interpolator;
            if (interpolator2 != null) {
                this.transformAnimator.setInterpolator(interpolator2);
            }
            this.transformAnimator.addListener(new AnimatorListenerAdapter() {
                boolean hasCancel = false;

                public void onAnimationCancel(Animator animator) {
                    super.onAnimationCancel(animator);
                    this.hasCancel = true;
                }

                public void onAnimationEnd(Animator animator) {
                    if (!this.hasCancel) {
                        super.onAnimationEnd(animator);
                        WXTransition.this.onTransitionAnimationEnd();
                    }
                }
            });
            this.transformAnimator.start();
        }
    }

    public void doPendingLayoutAnimation() {
        ValueAnimator valueAnimator = this.layoutValueAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.layoutValueAnimator = null;
        }
        if (this.layoutPendingUpdates.size() != 0) {
            PropertyValuesHolder[] propertyValuesHolderArr = new PropertyValuesHolder[this.layoutPendingUpdates.size()];
            int i = 0;
            if (this.properties.size() != 0) {
                for (String next : this.properties) {
                    if (LAYOUT_PROPERTIES.contains(next) && this.layoutPendingUpdates.containsKey(next)) {
                        Object remove = this.layoutPendingUpdates.remove(next);
                        synchronized (this.targetStyles) {
                            this.targetStyles.put(next, remove);
                        }
                        propertyValuesHolderArr[i] = createLayoutPropertyValueHolder(next, remove);
                        i++;
                    }
                }
                this.layoutPendingUpdates.clear();
                doLayoutPropertyValuesHolderAnimation(propertyValuesHolderArr);
            }
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.animation.PropertyValuesHolder createLayoutPropertyValueHolder(java.lang.String r21, java.lang.Object r22) {
        /*
            r20 = this;
            r0 = r20
            r1 = r21
            r2 = r22
            r21.hashCode()
            int r3 = r21.hashCode()
            java.lang.String r4 = "marginLeft"
            java.lang.String r5 = "marginRight"
            java.lang.String r6 = "paddingRight"
            java.lang.String r7 = "paddingBottom"
            java.lang.String r8 = "width"
            java.lang.String r9 = "right"
            java.lang.String r10 = "paddingTop"
            java.lang.String r11 = "left"
            java.lang.String r12 = "top"
            java.lang.String r13 = "marginBottom"
            java.lang.String r14 = "marginTop"
            java.lang.String r15 = "height"
            java.lang.String r2 = "bottom"
            java.lang.String r0 = "paddingLeft"
            r16 = 1
            r17 = 0
            r18 = r0
            r0 = 2
            r19 = -1
            switch(r3) {
                case -1501175880: goto L_0x00c1;
                case -1383228885: goto L_0x00b4;
                case -1221029593: goto L_0x00a8;
                case -1044792121: goto L_0x009e;
                case -289173127: goto L_0x0094;
                case 115029: goto L_0x008a;
                case 3317767: goto L_0x0080;
                case 90130308: goto L_0x0076;
                case 108511772: goto L_0x006c;
                case 113126854: goto L_0x0062;
                case 202355100: goto L_0x0058;
                case 713848971: goto L_0x004e;
                case 975087886: goto L_0x0044;
                case 1970934485: goto L_0x003a;
                default: goto L_0x0036;
            }
        L_0x0036:
            r3 = r18
            goto L_0x00cc
        L_0x003a:
            boolean r3 = r1.equals(r4)
            if (r3 != 0) goto L_0x0041
            goto L_0x0036
        L_0x0041:
            r19 = 13
            goto L_0x0036
        L_0x0044:
            boolean r3 = r1.equals(r5)
            if (r3 != 0) goto L_0x004b
            goto L_0x0036
        L_0x004b:
            r19 = 12
            goto L_0x0036
        L_0x004e:
            boolean r3 = r1.equals(r6)
            if (r3 != 0) goto L_0x0055
            goto L_0x0036
        L_0x0055:
            r19 = 11
            goto L_0x0036
        L_0x0058:
            boolean r3 = r1.equals(r7)
            if (r3 != 0) goto L_0x005f
            goto L_0x0036
        L_0x005f:
            r19 = 10
            goto L_0x0036
        L_0x0062:
            boolean r3 = r1.equals(r8)
            if (r3 != 0) goto L_0x0069
            goto L_0x0036
        L_0x0069:
            r19 = 9
            goto L_0x0036
        L_0x006c:
            boolean r3 = r1.equals(r9)
            if (r3 != 0) goto L_0x0073
            goto L_0x0036
        L_0x0073:
            r19 = 8
            goto L_0x0036
        L_0x0076:
            boolean r3 = r1.equals(r10)
            if (r3 != 0) goto L_0x007d
            goto L_0x0036
        L_0x007d:
            r19 = 7
            goto L_0x0036
        L_0x0080:
            boolean r3 = r1.equals(r11)
            if (r3 != 0) goto L_0x0087
            goto L_0x0036
        L_0x0087:
            r19 = 6
            goto L_0x0036
        L_0x008a:
            boolean r3 = r1.equals(r12)
            if (r3 != 0) goto L_0x0091
            goto L_0x0036
        L_0x0091:
            r19 = 5
            goto L_0x0036
        L_0x0094:
            boolean r3 = r1.equals(r13)
            if (r3 != 0) goto L_0x009b
            goto L_0x0036
        L_0x009b:
            r19 = 4
            goto L_0x0036
        L_0x009e:
            boolean r3 = r1.equals(r14)
            if (r3 != 0) goto L_0x00a5
            goto L_0x0036
        L_0x00a5:
            r19 = 3
            goto L_0x0036
        L_0x00a8:
            boolean r3 = r1.equals(r15)
            if (r3 != 0) goto L_0x00af
            goto L_0x0036
        L_0x00af:
            r3 = r18
            r19 = 2
            goto L_0x00cc
        L_0x00b4:
            boolean r3 = r1.equals(r2)
            if (r3 != 0) goto L_0x00bc
            goto L_0x0036
        L_0x00bc:
            r3 = r18
            r19 = 1
            goto L_0x00cc
        L_0x00c1:
            r3 = r18
            boolean r18 = r1.equals(r3)
            if (r18 != 0) goto L_0x00ca
            goto L_0x00cc
        L_0x00ca:
            r19 = 0
        L_0x00cc:
            r18 = 0
            switch(r19) {
                case 0: goto L_0x0335;
                case 1: goto L_0x0307;
                case 2: goto L_0x02dc;
                case 3: goto L_0x02ac;
                case 4: goto L_0x027c;
                case 5: goto L_0x024e;
                case 6: goto L_0x0220;
                case 7: goto L_0x01f0;
                case 8: goto L_0x01c2;
                case 9: goto L_0x0196;
                case 10: goto L_0x0166;
                case 11: goto L_0x0136;
                case 12: goto L_0x0106;
                case 13: goto L_0x00d6;
                default: goto L_0x00d1;
            }
        L_0x00d1:
            r2 = 0
            r3 = r20
            goto L_0x0364
        L_0x00d6:
            float[] r2 = new float[r0]
            r3 = r20
            com.taobao.weex.ui.component.WXComponent r5 = r3.mWXComponent
            com.taobao.weex.dom.CSSShorthand r5 = r5.getMargin()
            com.taobao.weex.dom.CSSShorthand$EDGE r6 = com.taobao.weex.dom.CSSShorthand.EDGE.LEFT
            float r5 = r5.get((java.lang.Enum<? extends io.dcloud.feature.uniapp.dom.AbsCSSShorthand.CSSProperty>) r6)
            r2[r17] = r5
            com.taobao.weex.ui.component.WXComponent r5 = r3.mWXComponent
            float r5 = r5.getViewPortWidthForFloat()
            r15 = r22
            float r5 = com.taobao.weex.utils.WXUtils.getFloatByViewport((java.lang.Object) r15, (float) r5)
            com.taobao.weex.ui.component.WXComponent r6 = r3.mWXComponent
            float r6 = r6.getViewPortWidthForFloat()
            float r5 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth((float) r5, (float) r6)
            r2[r16] = r5
            android.animation.PropertyValuesHolder r2 = android.animation.PropertyValuesHolder.ofFloat(r4, r2)
            goto L_0x0364
        L_0x0106:
            r3 = r20
            r15 = r22
            float[] r2 = new float[r0]
            com.taobao.weex.ui.component.WXComponent r4 = r3.mWXComponent
            com.taobao.weex.dom.CSSShorthand r4 = r4.getMargin()
            com.taobao.weex.dom.CSSShorthand$EDGE r6 = com.taobao.weex.dom.CSSShorthand.EDGE.RIGHT
            float r4 = r4.get((java.lang.Enum<? extends io.dcloud.feature.uniapp.dom.AbsCSSShorthand.CSSProperty>) r6)
            r2[r17] = r4
            com.taobao.weex.ui.component.WXComponent r4 = r3.mWXComponent
            float r4 = r4.getViewPortWidthForFloat()
            float r4 = com.taobao.weex.utils.WXUtils.getFloatByViewport((java.lang.Object) r15, (float) r4)
            com.taobao.weex.ui.component.WXComponent r6 = r3.mWXComponent
            float r6 = r6.getViewPortWidthForFloat()
            float r4 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth((float) r4, (float) r6)
            r2[r16] = r4
            android.animation.PropertyValuesHolder r2 = android.animation.PropertyValuesHolder.ofFloat(r5, r2)
            goto L_0x0364
        L_0x0136:
            r3 = r20
            r15 = r22
            float[] r2 = new float[r0]
            com.taobao.weex.ui.component.WXComponent r4 = r3.mWXComponent
            com.taobao.weex.dom.CSSShorthand r4 = r4.getPadding()
            com.taobao.weex.dom.CSSShorthand$EDGE r5 = com.taobao.weex.dom.CSSShorthand.EDGE.RIGHT
            float r4 = r4.get((java.lang.Enum<? extends io.dcloud.feature.uniapp.dom.AbsCSSShorthand.CSSProperty>) r5)
            r2[r17] = r4
            com.taobao.weex.ui.component.WXComponent r4 = r3.mWXComponent
            float r4 = r4.getViewPortWidthForFloat()
            float r4 = com.taobao.weex.utils.WXUtils.getFloatByViewport((java.lang.Object) r15, (float) r4)
            com.taobao.weex.ui.component.WXComponent r5 = r3.mWXComponent
            float r5 = r5.getViewPortWidthForFloat()
            float r4 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth((float) r4, (float) r5)
            r2[r16] = r4
            android.animation.PropertyValuesHolder r2 = android.animation.PropertyValuesHolder.ofFloat(r6, r2)
            goto L_0x0364
        L_0x0166:
            r3 = r20
            r15 = r22
            float[] r2 = new float[r0]
            com.taobao.weex.ui.component.WXComponent r4 = r3.mWXComponent
            com.taobao.weex.dom.CSSShorthand r4 = r4.getPadding()
            com.taobao.weex.dom.CSSShorthand$EDGE r5 = com.taobao.weex.dom.CSSShorthand.EDGE.BOTTOM
            float r4 = r4.get((java.lang.Enum<? extends io.dcloud.feature.uniapp.dom.AbsCSSShorthand.CSSProperty>) r5)
            r2[r17] = r4
            com.taobao.weex.ui.component.WXComponent r4 = r3.mWXComponent
            float r4 = r4.getViewPortWidthForFloat()
            float r4 = com.taobao.weex.utils.WXUtils.getFloatByViewport((java.lang.Object) r15, (float) r4)
            com.taobao.weex.ui.component.WXComponent r5 = r3.mWXComponent
            float r5 = r5.getViewPortWidthForFloat()
            float r4 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth((float) r4, (float) r5)
            r2[r16] = r4
            android.animation.PropertyValuesHolder r2 = android.animation.PropertyValuesHolder.ofFloat(r7, r2)
            goto L_0x0364
        L_0x0196:
            r3 = r20
            r15 = r22
            float[] r2 = new float[r0]
            com.taobao.weex.ui.component.WXComponent r4 = r3.mWXComponent
            float r4 = r4.getLayoutWidth()
            r2[r17] = r4
            java.lang.Float r4 = java.lang.Float.valueOf(r18)
            java.lang.Float r4 = com.taobao.weex.utils.WXUtils.getFloat(r15, r4)
            float r4 = r4.floatValue()
            com.taobao.weex.ui.component.WXComponent r5 = r3.mWXComponent
            float r5 = r5.getViewPortWidthForFloat()
            float r4 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth((float) r4, (float) r5)
            r2[r16] = r4
            android.animation.PropertyValuesHolder r2 = android.animation.PropertyValuesHolder.ofFloat(r8, r2)
            goto L_0x0364
        L_0x01c2:
            r3 = r20
            r15 = r22
            float[] r2 = new float[r0]
            com.taobao.weex.ui.component.WXComponent r4 = r3.mWXComponent
            com.taobao.weex.ui.action.GraphicPosition r4 = r4.getLayoutPosition()
            float r4 = r4.getRight()
            r2[r17] = r4
            com.taobao.weex.ui.component.WXComponent r4 = r3.mWXComponent
            float r4 = r4.getViewPortWidthForFloat()
            float r4 = com.taobao.weex.utils.WXUtils.getFloatByViewport((java.lang.Object) r15, (float) r4)
            com.taobao.weex.ui.component.WXComponent r5 = r3.mWXComponent
            float r5 = r5.getViewPortWidthForFloat()
            float r4 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth((float) r4, (float) r5)
            r2[r16] = r4
            android.animation.PropertyValuesHolder r2 = android.animation.PropertyValuesHolder.ofFloat(r9, r2)
            goto L_0x0364
        L_0x01f0:
            r3 = r20
            r15 = r22
            float[] r2 = new float[r0]
            com.taobao.weex.ui.component.WXComponent r4 = r3.mWXComponent
            com.taobao.weex.dom.CSSShorthand r4 = r4.getPadding()
            com.taobao.weex.dom.CSSShorthand$EDGE r5 = com.taobao.weex.dom.CSSShorthand.EDGE.TOP
            float r4 = r4.get((java.lang.Enum<? extends io.dcloud.feature.uniapp.dom.AbsCSSShorthand.CSSProperty>) r5)
            r2[r17] = r4
            com.taobao.weex.ui.component.WXComponent r4 = r3.mWXComponent
            float r4 = r4.getViewPortWidthForFloat()
            float r4 = com.taobao.weex.utils.WXUtils.getFloatByViewport((java.lang.Object) r15, (float) r4)
            com.taobao.weex.ui.component.WXComponent r5 = r3.mWXComponent
            float r5 = r5.getViewPortWidthForFloat()
            float r4 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth((float) r4, (float) r5)
            r2[r16] = r4
            android.animation.PropertyValuesHolder r2 = android.animation.PropertyValuesHolder.ofFloat(r10, r2)
            goto L_0x0364
        L_0x0220:
            r3 = r20
            r15 = r22
            float[] r2 = new float[r0]
            com.taobao.weex.ui.component.WXComponent r4 = r3.mWXComponent
            com.taobao.weex.ui.action.GraphicPosition r4 = r4.getLayoutPosition()
            float r4 = r4.getLeft()
            r2[r17] = r4
            com.taobao.weex.ui.component.WXComponent r4 = r3.mWXComponent
            float r4 = r4.getViewPortWidthForFloat()
            float r4 = com.taobao.weex.utils.WXUtils.getFloatByViewport((java.lang.Object) r15, (float) r4)
            com.taobao.weex.ui.component.WXComponent r5 = r3.mWXComponent
            float r5 = r5.getViewPortWidthForFloat()
            float r4 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth((float) r4, (float) r5)
            r2[r16] = r4
            android.animation.PropertyValuesHolder r2 = android.animation.PropertyValuesHolder.ofFloat(r11, r2)
            goto L_0x0364
        L_0x024e:
            r3 = r20
            r15 = r22
            float[] r2 = new float[r0]
            com.taobao.weex.ui.component.WXComponent r4 = r3.mWXComponent
            com.taobao.weex.ui.action.GraphicPosition r4 = r4.getLayoutPosition()
            float r4 = r4.getTop()
            r2[r17] = r4
            com.taobao.weex.ui.component.WXComponent r4 = r3.mWXComponent
            float r4 = r4.getViewPortWidthForFloat()
            float r4 = com.taobao.weex.utils.WXUtils.getFloatByViewport((java.lang.Object) r15, (float) r4)
            com.taobao.weex.ui.component.WXComponent r5 = r3.mWXComponent
            float r5 = r5.getViewPortWidthForFloat()
            float r4 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth((float) r4, (float) r5)
            r2[r16] = r4
            android.animation.PropertyValuesHolder r2 = android.animation.PropertyValuesHolder.ofFloat(r12, r2)
            goto L_0x0364
        L_0x027c:
            r3 = r20
            r15 = r22
            float[] r2 = new float[r0]
            com.taobao.weex.ui.component.WXComponent r4 = r3.mWXComponent
            com.taobao.weex.dom.CSSShorthand r4 = r4.getMargin()
            com.taobao.weex.dom.CSSShorthand$EDGE r5 = com.taobao.weex.dom.CSSShorthand.EDGE.BOTTOM
            float r4 = r4.get((java.lang.Enum<? extends io.dcloud.feature.uniapp.dom.AbsCSSShorthand.CSSProperty>) r5)
            r2[r17] = r4
            com.taobao.weex.ui.component.WXComponent r4 = r3.mWXComponent
            float r4 = r4.getViewPortWidthForFloat()
            float r4 = com.taobao.weex.utils.WXUtils.getFloatByViewport((java.lang.Object) r15, (float) r4)
            com.taobao.weex.ui.component.WXComponent r5 = r3.mWXComponent
            float r5 = r5.getViewPortWidthForFloat()
            float r4 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth((float) r4, (float) r5)
            r2[r16] = r4
            android.animation.PropertyValuesHolder r2 = android.animation.PropertyValuesHolder.ofFloat(r13, r2)
            goto L_0x0364
        L_0x02ac:
            r3 = r20
            r15 = r22
            float[] r2 = new float[r0]
            com.taobao.weex.ui.component.WXComponent r4 = r3.mWXComponent
            com.taobao.weex.dom.CSSShorthand r4 = r4.getMargin()
            com.taobao.weex.dom.CSSShorthand$EDGE r5 = com.taobao.weex.dom.CSSShorthand.EDGE.TOP
            float r4 = r4.get((java.lang.Enum<? extends io.dcloud.feature.uniapp.dom.AbsCSSShorthand.CSSProperty>) r5)
            r2[r17] = r4
            com.taobao.weex.ui.component.WXComponent r4 = r3.mWXComponent
            float r4 = r4.getViewPortWidthForFloat()
            float r4 = com.taobao.weex.utils.WXUtils.getFloatByViewport((java.lang.Object) r15, (float) r4)
            com.taobao.weex.ui.component.WXComponent r5 = r3.mWXComponent
            float r5 = r5.getViewPortWidthForFloat()
            float r4 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth((float) r4, (float) r5)
            r2[r16] = r4
            android.animation.PropertyValuesHolder r2 = android.animation.PropertyValuesHolder.ofFloat(r14, r2)
            goto L_0x0364
        L_0x02dc:
            r3 = r20
            r2 = r22
            float[] r4 = new float[r0]
            com.taobao.weex.ui.component.WXComponent r5 = r3.mWXComponent
            float r5 = r5.getLayoutHeight()
            r4[r17] = r5
            java.lang.Float r5 = java.lang.Float.valueOf(r18)
            java.lang.Float r2 = com.taobao.weex.utils.WXUtils.getFloat(r2, r5)
            float r2 = r2.floatValue()
            com.taobao.weex.ui.component.WXComponent r5 = r3.mWXComponent
            float r5 = r5.getViewPortWidthForFloat()
            float r2 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth((float) r2, (float) r5)
            r4[r16] = r2
            android.animation.PropertyValuesHolder r2 = android.animation.PropertyValuesHolder.ofFloat(r15, r4)
            goto L_0x0364
        L_0x0307:
            r3 = r20
            r4 = r2
            r2 = r22
            float[] r5 = new float[r0]
            com.taobao.weex.ui.component.WXComponent r6 = r3.mWXComponent
            com.taobao.weex.ui.action.GraphicPosition r6 = r6.getLayoutPosition()
            float r6 = r6.getBottom()
            r5[r17] = r6
            com.taobao.weex.ui.component.WXComponent r6 = r3.mWXComponent
            float r6 = r6.getViewPortWidthForFloat()
            float r2 = com.taobao.weex.utils.WXUtils.getFloatByViewport((java.lang.Object) r2, (float) r6)
            com.taobao.weex.ui.component.WXComponent r6 = r3.mWXComponent
            float r6 = r6.getViewPortWidthForFloat()
            float r2 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth((float) r2, (float) r6)
            r5[r16] = r2
            android.animation.PropertyValuesHolder r2 = android.animation.PropertyValuesHolder.ofFloat(r4, r5)
            goto L_0x0364
        L_0x0335:
            r2 = r22
            r4 = r3
            r3 = r20
            float[] r5 = new float[r0]
            com.taobao.weex.ui.component.WXComponent r6 = r3.mWXComponent
            com.taobao.weex.dom.CSSShorthand r6 = r6.getPadding()
            com.taobao.weex.dom.CSSShorthand$EDGE r7 = com.taobao.weex.dom.CSSShorthand.EDGE.LEFT
            float r6 = r6.get((java.lang.Enum<? extends io.dcloud.feature.uniapp.dom.AbsCSSShorthand.CSSProperty>) r7)
            r5[r17] = r6
            com.taobao.weex.ui.component.WXComponent r6 = r3.mWXComponent
            float r6 = r6.getViewPortWidthForFloat()
            float r2 = com.taobao.weex.utils.WXUtils.getFloatByViewport((java.lang.Object) r2, (float) r6)
            com.taobao.weex.ui.component.WXComponent r6 = r3.mWXComponent
            float r6 = r6.getViewPortWidthForFloat()
            float r2 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth((float) r2, (float) r6)
            r5[r16] = r2
            android.animation.PropertyValuesHolder r2 = android.animation.PropertyValuesHolder.ofFloat(r4, r5)
        L_0x0364:
            if (r2 != 0) goto L_0x036f
            float[] r0 = new float[r0]
            r0 = {1065353216, 1065353216} // fill-array
            android.animation.PropertyValuesHolder r2 = android.animation.PropertyValuesHolder.ofFloat(r1, r0)
        L_0x036f:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.dom.transition.WXTransition.createLayoutPropertyValueHolder(java.lang.String, java.lang.Object):android.animation.PropertyValuesHolder");
    }

    private void doLayoutPropertyValuesHolderAnimation(PropertyValuesHolder[] propertyValuesHolderArr) {
        ValueAnimator ofPropertyValuesHolder = ValueAnimator.ofPropertyValuesHolder(propertyValuesHolderArr);
        this.layoutValueAnimator = ofPropertyValuesHolder;
        ofPropertyValuesHolder.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                for (PropertyValuesHolder propertyName : valueAnimator.getValues()) {
                    String propertyName2 = propertyName.getPropertyName();
                    WXTransition.asynchronouslyUpdateLayout(WXTransition.this.mWXComponent, propertyName2, ((Float) valueAnimator.getAnimatedValue(propertyName2)).floatValue());
                }
            }
        });
        this.layoutValueAnimator.addListener(new AnimatorListenerAdapter() {
            boolean hasCancel = false;

            public void onAnimationCancel(Animator animator) {
                super.onAnimationCancel(animator);
                this.hasCancel = true;
            }

            public void onAnimationEnd(Animator animator) {
                if (!this.hasCancel) {
                    super.onAnimationEnd(animator);
                    WXTransition.this.onTransitionAnimationEnd();
                }
            }
        });
        Interpolator interpolator2 = this.interpolator;
        if (interpolator2 != null) {
            this.layoutValueAnimator.setInterpolator(interpolator2);
        }
        this.layoutValueAnimator.setStartDelay(this.delay);
        this.layoutValueAnimator.setDuration(this.duration);
        this.layoutValueAnimator.start();
    }

    public static void asynchronouslyUpdateLayout(WXComponent wXComponent, final String str, final float f) {
        if (wXComponent != null && wXComponent.getInstance() != null) {
            final String ref = wXComponent.getRef();
            final String instanceId = wXComponent.getInstanceId();
            if (!TextUtils.isEmpty(ref) && !TextUtils.isEmpty(instanceId)) {
                WXSDKManager.getInstance().getWXBridgeManager().post(new Runnable() {
                    public void run() {
                        String str = str;
                        str.hashCode();
                        char c = 65535;
                        switch (str.hashCode()) {
                            case -1501175880:
                                if (str.equals(Constants.Name.PADDING_LEFT)) {
                                    c = 0;
                                    break;
                                }
                                break;
                            case -1383228885:
                                if (str.equals("bottom")) {
                                    c = 1;
                                    break;
                                }
                                break;
                            case -1221029593:
                                if (str.equals("height")) {
                                    c = 2;
                                    break;
                                }
                                break;
                            case -1044792121:
                                if (str.equals(Constants.Name.MARGIN_TOP)) {
                                    c = 3;
                                    break;
                                }
                                break;
                            case -289173127:
                                if (str.equals(Constants.Name.MARGIN_BOTTOM)) {
                                    c = 4;
                                    break;
                                }
                                break;
                            case 115029:
                                if (str.equals("top")) {
                                    c = 5;
                                    break;
                                }
                                break;
                            case 3317767:
                                if (str.equals("left")) {
                                    c = 6;
                                    break;
                                }
                                break;
                            case 90130308:
                                if (str.equals(Constants.Name.PADDING_TOP)) {
                                    c = 7;
                                    break;
                                }
                                break;
                            case 108511772:
                                if (str.equals("right")) {
                                    c = 8;
                                    break;
                                }
                                break;
                            case 113126854:
                                if (str.equals("width")) {
                                    c = 9;
                                    break;
                                }
                                break;
                            case 202355100:
                                if (str.equals(Constants.Name.PADDING_BOTTOM)) {
                                    c = 10;
                                    break;
                                }
                                break;
                            case 713848971:
                                if (str.equals(Constants.Name.PADDING_RIGHT)) {
                                    c = 11;
                                    break;
                                }
                                break;
                            case 975087886:
                                if (str.equals(Constants.Name.MARGIN_RIGHT)) {
                                    c = 12;
                                    break;
                                }
                                break;
                            case 1970934485:
                                if (str.equals(Constants.Name.MARGIN_LEFT)) {
                                    c = 13;
                                    break;
                                }
                                break;
                        }
                        switch (c) {
                            case 0:
                                WXBridgeManager.getInstance().setPadding(instanceId, ref, CSSShorthand.EDGE.LEFT, f);
                                return;
                            case 1:
                                WXBridgeManager.getInstance().setPosition(instanceId, ref, CSSShorthand.EDGE.BOTTOM, f);
                                return;
                            case 2:
                                WXBridgeManager.getInstance().setStyleHeight(instanceId, ref, f);
                                return;
                            case 3:
                                WXBridgeManager.getInstance().setMargin(instanceId, ref, CSSShorthand.EDGE.TOP, f);
                                return;
                            case 4:
                                WXBridgeManager.getInstance().setMargin(instanceId, ref, CSSShorthand.EDGE.BOTTOM, f);
                                return;
                            case 5:
                                WXBridgeManager.getInstance().setPosition(instanceId, ref, CSSShorthand.EDGE.TOP, f);
                                return;
                            case 6:
                                WXBridgeManager.getInstance().setPosition(instanceId, ref, CSSShorthand.EDGE.LEFT, f);
                                return;
                            case 7:
                                WXBridgeManager.getInstance().setPadding(instanceId, ref, CSSShorthand.EDGE.TOP, f);
                                return;
                            case 8:
                                WXBridgeManager.getInstance().setPosition(instanceId, ref, CSSShorthand.EDGE.RIGHT, f);
                                return;
                            case 9:
                                WXBridgeManager.getInstance().setStyleWidth(instanceId, ref, f);
                                return;
                            case 10:
                                WXBridgeManager.getInstance().setPadding(instanceId, ref, CSSShorthand.EDGE.BOTTOM, f);
                                return;
                            case 11:
                                WXBridgeManager.getInstance().setPadding(instanceId, ref, CSSShorthand.EDGE.RIGHT, f);
                                return;
                            case 12:
                                WXBridgeManager.getInstance().setMargin(instanceId, ref, CSSShorthand.EDGE.RIGHT, f);
                                return;
                            case 13:
                                WXBridgeManager.getInstance().setMargin(instanceId, ref, CSSShorthand.EDGE.LEFT, f);
                                return;
                            default:
                                return;
                        }
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public synchronized void onTransitionAnimationEnd() {
        Runnable runnable;
        if (this.duration > 0 && this.transitionEndEvent != null) {
            View targetView = getTargetView();
            if (!(targetView == null || (runnable = this.transitionEndEvent) == null)) {
                targetView.post(runnable);
            }
            this.transitionEndEvent = null;
        }
        synchronized (this.targetStyles) {
            if (this.targetStyles.size() > 0) {
                for (String next : this.properties) {
                    if (this.targetStyles.containsKey(next)) {
                        this.mWXComponent.getStyles().put(next, this.targetStyles.remove(next));
                    }
                }
                this.targetStyles.clear();
            }
        }
    }

    private View getTargetView() {
        WXComponent wXComponent = this.mWXComponent;
        if (wXComponent != null) {
            return wXComponent.getHostView();
        }
        return null;
    }

    private static long parseTimeMillis(Map<String, Object> map, String str, long j) {
        String string = WXUtils.getString(map.get(str), (String) null);
        if (string != null) {
            string = string.replaceAll("ms", "");
        }
        if (string != null) {
            if (WXEnvironment.isApkDebugable() && string.contains("px")) {
                WXLogUtils.w("Transition Duration Unit Only Support ms, " + string + " is not ms Unit");
            }
            string = string.replaceAll("px", "");
        }
        if (TextUtils.isEmpty(string)) {
            return j;
        }
        try {
            return (long) Float.parseFloat(string);
        } catch (NumberFormatException unused) {
            return j;
        }
    }

    private static Interpolator createTimeInterpolator(String str) {
        if (!TextUtils.isEmpty(str)) {
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
                case 3105774:
                    if (str.equals(Constants.TimeFunction.EASE)) {
                        c = 4;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    return PathInterpolatorCompat.create(0.42f, 0.0f, 1.0f, 1.0f);
                case 1:
                    return PathInterpolatorCompat.create(0.0f, 0.0f, 1.0f, 1.0f);
                case 2:
                    return PathInterpolatorCompat.create(0.0f, 0.0f, 0.58f, 1.0f);
                case 3:
                    return PathInterpolatorCompat.create(0.42f, 0.0f, 0.58f, 1.0f);
                case 4:
                    return PathInterpolatorCompat.create(0.25f, 0.1f, 0.25f, 1.0f);
                default:
                    try {
                        List parse = new SingleFunctionParser(str, new SingleFunctionParser.FlatMapper<Float>() {
                            public Float map(String str) {
                                return Float.valueOf(Float.parseFloat(str));
                            }
                        }).parse("cubic-bezier");
                        if (parse != null && parse.size() == 4) {
                            return PathInterpolatorCompat.create(((Float) parse.get(0)).floatValue(), ((Float) parse.get(1)).floatValue(), ((Float) parse.get(2)).floatValue(), ((Float) parse.get(3)).floatValue());
                        }
                    } catch (RuntimeException e) {
                        if (WXEnvironment.isApkDebugable()) {
                            WXLogUtils.e("WXTransition", (Throwable) e);
                            break;
                        }
                    }
                    break;
            }
        }
        return PathInterpolatorCompat.create(0.25f, 0.1f, 0.25f, 1.0f);
    }

    private static void updateTransitionProperties(WXTransition wXTransition, String str) {
        if (str != null) {
            wXTransition.properties.clear();
            for (String trim : PROPERTY_SPLIT_PATTERN.split(str)) {
                String trim2 = trim.trim();
                if (!TextUtils.isEmpty(trim2)) {
                    if (LAYOUT_PROPERTIES.contains(trim2) || TRANSFORM_PROPERTIES.contains(trim2)) {
                        wXTransition.properties.add(trim2);
                    } else if (WXEnvironment.isApkDebugable()) {
                        WXLogUtils.e("WXTransition Property Not Supported" + trim2 + " in " + str);
                    }
                }
            }
        }
    }

    public List<String> getProperties() {
        return this.properties;
    }
}
