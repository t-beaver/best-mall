package io.dcloud.common.util;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import io.dcloud.common.DHInterface.INativeView;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.nineoldandroids.animation.Animator;
import java.util.ArrayList;
import java.util.Iterator;

public class SubNViewsUtil {
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r15v0, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void initFrameSubNViews(io.dcloud.common.adapter.ui.AdaFrameView r18) {
        /*
            r1 = r18
            java.lang.String r2 = "NativeView"
            int r0 = r18.getFrameType()
            java.lang.String r3 = "auto"
            r4 = 4
            r5 = 2
            java.lang.String r6 = "animationOptimization"
            java.lang.String r7 = "subNViews"
            r8 = 1
            if (r0 != r5) goto L_0x004d
            io.dcloud.common.DHInterface.IApp r0 = r18.obtainApp()
            io.dcloud.common.DHInterface.IApp$ConfigProperty$ThridInfo r9 = io.dcloud.common.DHInterface.IApp.ConfigProperty.ThridInfo.LaunchWebviewJsonData
            org.json.JSONObject r9 = r0.obtainThridInfo(r9)
            if (r9 == 0) goto L_0x0034
            boolean r0 = r9.has(r7)
            if (r0 == 0) goto L_0x0034
            io.dcloud.common.adapter.util.ViewOptions r0 = r18.obtainFrameOptions()     // Catch:{ JSONException -> 0x0030 }
            org.json.JSONArray r7 = r9.getJSONArray(r7)     // Catch:{ JSONException -> 0x0030 }
            r0.mSubNViews = r7     // Catch:{ JSONException -> 0x0030 }
            goto L_0x0034
        L_0x0030:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0034:
            if (r9 == 0) goto L_0x008c
            boolean r0 = r9.has(r6)
            if (r0 == 0) goto L_0x008c
            java.lang.String r0 = r9.optString(r6)
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x008c
            io.dcloud.common.adapter.util.ViewOptions r0 = r18.obtainFrameOptions()
            r0.isAnimationOptimization = r8
            goto L_0x008c
        L_0x004d:
            int r0 = r18.getFrameType()
            if (r0 != r4) goto L_0x008c
            io.dcloud.common.DHInterface.IApp r0 = r18.obtainApp()
            io.dcloud.common.DHInterface.IApp$ConfigProperty$ThridInfo r9 = io.dcloud.common.DHInterface.IApp.ConfigProperty.ThridInfo.SecondWebviewJsonData
            org.json.JSONObject r9 = r0.obtainThridInfo(r9)
            if (r9 == 0) goto L_0x0074
            boolean r0 = r9.has(r7)
            if (r0 == 0) goto L_0x0074
            io.dcloud.common.adapter.util.ViewOptions r0 = r18.obtainFrameOptions()     // Catch:{ JSONException -> 0x0070 }
            org.json.JSONArray r7 = r9.getJSONArray(r7)     // Catch:{ JSONException -> 0x0070 }
            r0.mSubNViews = r7     // Catch:{ JSONException -> 0x0070 }
            goto L_0x0074
        L_0x0070:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0074:
            if (r9 == 0) goto L_0x008c
            boolean r0 = r9.has(r6)
            if (r0 == 0) goto L_0x008c
            java.lang.String r0 = r9.optString(r6)
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x008c
            io.dcloud.common.adapter.util.ViewOptions r0 = r18.obtainFrameOptions()
            r0.isAnimationOptimization = r8
        L_0x008c:
            io.dcloud.common.adapter.util.ViewOptions r0 = r18.obtainFrameOptions()
            org.json.JSONArray r0 = r0.mSubNViews
            if (r0 == 0) goto L_0x0135
            io.dcloud.common.adapter.util.ViewOptions r0 = r18.obtainFrameOptions()
            org.json.JSONArray r0 = r0.mSubNViews
            r3 = 0
            r6 = 0
        L_0x009c:
            int r7 = r0.length()     // Catch:{ JSONException -> 0x0131 }
            if (r6 >= r7) goto L_0x0135
            org.json.JSONObject r7 = r0.getJSONObject(r6)     // Catch:{ JSONException -> 0x0131 }
            java.lang.String r9 = "id"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x0131 }
            r10.<init>()     // Catch:{ JSONException -> 0x0131 }
            r10.append(r6)     // Catch:{ JSONException -> 0x0131 }
            r10.append(r2)     // Catch:{ JSONException -> 0x0131 }
            java.lang.String r10 = r10.toString()     // Catch:{ JSONException -> 0x0131 }
            java.lang.String r9 = r7.optString(r9, r10)     // Catch:{ JSONException -> 0x0131 }
            java.lang.String r10 = "uuid"
            java.lang.String r10 = r7.optString(r10)     // Catch:{ JSONException -> 0x0131 }
            boolean r11 = android.text.TextUtils.isEmpty(r10)     // Catch:{ JSONException -> 0x0131 }
            if (r11 == 0) goto L_0x00dc
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x0131 }
            r10.<init>()     // Catch:{ JSONException -> 0x0131 }
            r10.append(r2)     // Catch:{ JSONException -> 0x0131 }
            long r11 = java.lang.System.currentTimeMillis()     // Catch:{ JSONException -> 0x0131 }
            long r13 = (long) r6     // Catch:{ JSONException -> 0x0131 }
            long r11 = r11 + r13
            r10.append(r11)     // Catch:{ JSONException -> 0x0131 }
            java.lang.String r10 = r10.toString()     // Catch:{ JSONException -> 0x0131 }
        L_0x00dc:
            java.lang.String r11 = "styles"
            org.json.JSONObject r11 = r7.optJSONObject(r11)     // Catch:{ JSONException -> 0x0131 }
            java.lang.String r12 = "tags"
            org.json.JSONArray r12 = r7.optJSONArray(r12)     // Catch:{ JSONException -> 0x0131 }
            java.lang.String r13 = "type"
            java.lang.String r14 = "NView"
            java.lang.String r7 = r7.optString(r13, r14)     // Catch:{ JSONException -> 0x0131 }
            io.dcloud.common.core.ui.l r13 = r1.mWindowMgr     // Catch:{ JSONException -> 0x0131 }
            io.dcloud.common.DHInterface.IMgr$MgrType r14 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr     // Catch:{ JSONException -> 0x0131 }
            java.lang.Object[] r15 = new java.lang.Object[r4]     // Catch:{ JSONException -> 0x0131 }
            io.dcloud.common.DHInterface.IWebview r16 = r18.obtainWebView()     // Catch:{ JSONException -> 0x0131 }
            r15[r3] = r16     // Catch:{ JSONException -> 0x0131 }
            java.lang.String r16 = "nativeobj"
            r15[r8] = r16     // Catch:{ JSONException -> 0x0131 }
            java.lang.String r16 = "View"
            r15[r5] = r16     // Catch:{ JSONException -> 0x0131 }
            r4 = 7
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ JSONException -> 0x0131 }
            r4[r3] = r1     // Catch:{ JSONException -> 0x0131 }
            io.dcloud.common.DHInterface.IWebview r17 = r18.obtainWebView()     // Catch:{ JSONException -> 0x0131 }
            r4[r8] = r17     // Catch:{ JSONException -> 0x0131 }
            r4[r5] = r9     // Catch:{ JSONException -> 0x0131 }
            r9 = 3
            r4[r9] = r10     // Catch:{ JSONException -> 0x0131 }
            r10 = 4
            r4[r10] = r11     // Catch:{ JSONException -> 0x0131 }
            r11 = 5
            r4[r11] = r12     // Catch:{ JSONException -> 0x0131 }
            r11 = 6
            r4[r11] = r7     // Catch:{ JSONException -> 0x0131 }
            r15[r9] = r4     // Catch:{ JSONException -> 0x0131 }
            r4 = 10
            java.lang.Object r4 = r13.processEvent(r14, r4, r15)     // Catch:{ JSONException -> 0x0131 }
            if (r4 == 0) goto L_0x012c
            io.dcloud.common.DHInterface.INativeView r4 = (io.dcloud.common.DHInterface.INativeView) r4     // Catch:{ JSONException -> 0x0131 }
            r4.attachToViewGroup(r1)     // Catch:{ JSONException -> 0x0131 }
        L_0x012c:
            int r6 = r6 + 1
            r4 = 4
            goto L_0x009c
        L_0x0131:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0135:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.SubNViewsUtil.initFrameSubNViews(io.dcloud.common.adapter.ui.AdaFrameView):void");
    }

    public static boolean startAnimation(AdaFrameView adaFrameView, Animator animator, int i) {
        ArrayList<INativeView> arrayList;
        if (adaFrameView.getAnimOptions().mAnimator != null && i == 0 && (arrayList = adaFrameView.mChildNativeViewList) != null && arrayList.size() > 0) {
            final ViewGroup obtainWindowView = adaFrameView.obtainWebView().obtainWindowView();
            final ViewGroup viewGroup = (ViewGroup) obtainWindowView.getParent();
            final ArrayList arrayList2 = new ArrayList();
            if (adaFrameView.obtainFrameOptions().background == -1) {
                viewGroup.setBackgroundColor(-1);
            }
            for (int i2 = 0; i2 < obtainWindowView.getChildCount(); i2++) {
                View childAt = obtainWindowView.getChildAt(i2);
                if (childAt instanceof INativeView) {
                    arrayList2.add((INativeView) childAt);
                }
            }
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                INativeView iNativeView = (INativeView) it.next();
                obtainWindowView.removeView(iNativeView.obtanMainView());
                viewGroup.addView(iNativeView.obtanMainView());
            }
            viewGroup.removeView(obtainWindowView);
            try {
                obtainWindowView.getClass().getMethod("onPause", (Class[]) null).invoke(obtainWindowView, (Object[]) null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            View obtainMainView = adaFrameView.obtainMainView();
            if (!PdrUtil.isEmpty(obtainMainView)) {
                obtainMainView.bringToFront();
                obtainMainView.setVisibility(0);
                final AdaFrameView adaFrameView2 = adaFrameView;
                final View view = obtainMainView;
                adaFrameView.getAnimOptions().mAnimator.setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationEnd(Animation animation) {
                        Animator.AnimatorListener animatorListener = AdaFrameView.this.mAnimatorListener;
                        if (animatorListener != null) {
                            animatorListener.onAnimationEnd((Animator) null);
                        }
                        BaseInfo.sDoingAnimation = false;
                        AdaFrameView.this.setSlipping(false);
                        view.post(new Runnable() {
                            public void run() {
                                Iterator it = arrayList2.iterator();
                                while (it.hasNext()) {
                                    INativeView iNativeView = (INativeView) it.next();
                                    viewGroup.removeView(iNativeView.obtanMainView());
                                    obtainWindowView.addView(iNativeView.obtanMainView());
                                }
                                AnonymousClass1 r0 = AnonymousClass1.this;
                                viewGroup.addView(obtainWindowView);
                                if (AdaFrameView.this.obtainFrameOptions().background == -1) {
                                    viewGroup.setBackgroundColor(0);
                                }
                                try {
                                    obtainWindowView.getClass().getMethod("onResume", (Class[]) null).invoke(obtainWindowView, (Object[]) null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                        Animator.AnimatorListener animatorListener = AdaFrameView.this.mAnimatorListener;
                        if (animatorListener != null) {
                            animatorListener.onAnimationStart((Animator) null);
                        }
                        BaseInfo.sDoingAnimation = true;
                    }
                });
                adaFrameView.setSlipping(true);
                obtainMainView.startAnimation(adaFrameView.getAnimOptions().mAnimator);
                return true;
            }
        }
        return false;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void updateSubNViews(io.dcloud.common.adapter.ui.AdaFrameView r8, org.json.JSONArray r9) {
        /*
            if (r9 == 0) goto L_0x002e
            io.dcloud.common.core.ui.l r0 = r8.mWindowMgr
            io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r2 = 4
            java.lang.Object[] r2 = new java.lang.Object[r2]
            io.dcloud.common.DHInterface.IWebview r3 = r8.obtainWebView()
            r4 = 0
            r2[r4] = r3
            java.lang.String r3 = "nativeobj"
            r5 = 1
            r2[r5] = r3
            java.lang.String r3 = "updateSubNViews"
            r6 = 2
            r2[r6] = r3
            r3 = 3
            java.lang.Object[] r7 = new java.lang.Object[r3]
            r7[r4] = r8
            io.dcloud.common.DHInterface.IWebview r8 = r8.obtainWebView()
            r7[r5] = r8
            r7[r6] = r9
            r2[r3] = r7
            r8 = 10
            r0.processEvent(r1, r8, r2)
        L_0x002e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.SubNViewsUtil.updateSubNViews(io.dcloud.common.adapter.ui.AdaFrameView, org.json.JSONArray):void");
    }
}
