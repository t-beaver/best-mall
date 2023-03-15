package com.alibaba.android.bindingx.core.internal;

import android.content.Context;
import com.alibaba.android.bindingx.core.LogProxy;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.taobao.weex.common.Constants;
import com.taobao.weex.el.parse.Operators;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class AbstractScrollEventHandler extends AbstractEventHandler {
    private boolean isStart = false;
    protected int mContentOffsetX;
    protected int mContentOffsetY;

    public AbstractScrollEventHandler(Context context, PlatformManager platformManager, Object... objArr) {
        super(context, platformManager, objArr);
    }

    public boolean onDisable(String str, String str2) {
        clearExpressions();
        this.isStart = false;
        fireEventByState("end", (double) this.mContentOffsetX, (double) this.mContentOffsetY, 0.0d, 0.0d, 0.0d, 0.0d, new Object[0]);
        return true;
    }

    /* access modifiers changed from: protected */
    public void onExit(Map<String, Object> map) {
        Map<String, Object> map2 = map;
        fireEventByState(BindingXConstants.STATE_EXIT, ((Double) map2.get("internal_x")).doubleValue(), ((Double) map2.get("internal_y")).doubleValue(), 0.0d, 0.0d, 0.0d, 0.0d, new Object[0]);
    }

    /* access modifiers changed from: protected */
    public void onUserIntercept(String str, Map<String, Object> map) {
        Map<String, Object> map2 = map;
        fireEventByState(BindingXConstants.STATE_INTERCEPTOR, ((Double) map2.get("internal_x")).doubleValue(), ((Double) map2.get("internal_y")).doubleValue(), ((Double) map2.get("dx")).doubleValue(), ((Double) map2.get(Constants.Name.DISTANCE_Y)).doubleValue(), ((Double) map2.get("tdx")).doubleValue(), ((Double) map2.get("tdy")).doubleValue(), Collections.singletonMap(BindingXConstants.STATE_INTERCEPTOR, str));
    }

    public void onDestroy() {
        super.onDestroy();
        this.isStart = false;
    }

    /* access modifiers changed from: protected */
    public void handleScrollEvent(int i, int i2, int i3, int i4, int i5, int i6) {
        AbstractScrollEventHandler abstractScrollEventHandler;
        int i7 = i;
        int i8 = i2;
        int i9 = i3;
        int i10 = i4;
        int i11 = i5;
        int i12 = i6;
        if (LogProxy.sEnableLog) {
            LogProxy.d(String.format(Locale.getDefault(), "[ScrollHandler] scroll changed. (contentOffsetX:%d,contentOffsetY:%d,dx:%d,dy:%d,tdx:%d,tdy:%d)", new Object[]{Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6)}));
        }
        this.mContentOffsetX = i7;
        this.mContentOffsetY = i8;
        if (!this.isStart) {
            this.isStart = true;
            double d = (double) i10;
            abstractScrollEventHandler = this;
            abstractScrollEventHandler.fireEventByState("start", (double) i7, (double) i8, (double) i9, d, (double) i11, (double) i12, new Object[0]);
        } else {
            abstractScrollEventHandler = this;
        }
        try {
            JSMath.applyScrollValuesToScope(abstractScrollEventHandler.mScope, (double) i7, (double) i2, (double) i3, (double) i4, (double) i5, (double) i6, abstractScrollEventHandler.mPlatformManager.getResolutionTranslator());
            if (!abstractScrollEventHandler.evaluateExitExpression(abstractScrollEventHandler.mExitExpressionPair, abstractScrollEventHandler.mScope)) {
                abstractScrollEventHandler.consumeExpression(abstractScrollEventHandler.mExpressionHoldersMap, abstractScrollEventHandler.mScope, "scroll");
            }
        } catch (Exception e) {
            LogProxy.e("runtime error", e);
        }
    }

    /* access modifiers changed from: protected */
    public void fireEventByState(String str, double d, double d2, double d3, double d4, double d5, double d6, Object... objArr) {
        String str2 = str;
        Object[] objArr2 = objArr;
        if (this.mCallback != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("state", str2);
            double nativeToWeb = this.mPlatformManager.getResolutionTranslator().nativeToWeb(d, new Object[0]);
            double nativeToWeb2 = this.mPlatformManager.getResolutionTranslator().nativeToWeb(d2, new Object[0]);
            hashMap.put(Constants.Name.X, Double.valueOf(nativeToWeb));
            hashMap.put(Constants.Name.Y, Double.valueOf(nativeToWeb2));
            double nativeToWeb3 = this.mPlatformManager.getResolutionTranslator().nativeToWeb(d3, new Object[0]);
            double nativeToWeb4 = this.mPlatformManager.getResolutionTranslator().nativeToWeb(d4, new Object[0]);
            hashMap.put("dx", Double.valueOf(nativeToWeb3));
            hashMap.put(Constants.Name.DISTANCE_Y, Double.valueOf(nativeToWeb4));
            double d7 = nativeToWeb;
            double nativeToWeb5 = this.mPlatformManager.getResolutionTranslator().nativeToWeb(d5, new Object[0]);
            double d8 = nativeToWeb4;
            double nativeToWeb6 = this.mPlatformManager.getResolutionTranslator().nativeToWeb(d6, new Object[0]);
            hashMap.put("tdx", Double.valueOf(nativeToWeb5));
            hashMap.put("tdy", Double.valueOf(nativeToWeb6));
            hashMap.put(BindingXConstants.KEY_TOKEN, this.mToken);
            if (objArr2 != null && objArr2.length > 0 && (objArr2[0] instanceof Map)) {
                hashMap.putAll((Map) objArr2[0]);
            }
            this.mCallback.callback(hashMap);
            LogProxy.d(">>>>>>>>>>>fire event:(" + str2 + "," + d7 + "," + nativeToWeb2 + "," + nativeToWeb3 + "," + d8 + "," + nativeToWeb5 + "," + nativeToWeb6 + Operators.BRACKET_END_STR);
        }
    }
}
