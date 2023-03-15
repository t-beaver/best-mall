package com.alibaba.android.bindingx.core.internal;

import android.content.Context;
import android.view.animation.AnimationUtils;
import com.alibaba.android.bindingx.core.BindingXCore;
import com.alibaba.android.bindingx.core.BindingXEventType;
import com.alibaba.android.bindingx.core.LogProxy;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.alibaba.android.bindingx.core.internal.AnimationFrame;
import com.taobao.weex.el.parse.Operators;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BindingXTimingHandler extends AbstractEventHandler implements AnimationFrame.Callback {
    private boolean isFinish = false;
    private AnimationFrame mAnimationFrame;
    private long mStartTime = 0;

    public void onActivityPause() {
    }

    public void onActivityResume() {
    }

    public boolean onCreate(String str, String str2) {
        return true;
    }

    public void onStart(String str, String str2) {
    }

    public BindingXTimingHandler(Context context, PlatformManager platformManager, Object... objArr) {
        super(context, platformManager, objArr);
        AnimationFrame animationFrame = this.mAnimationFrame;
        if (animationFrame == null) {
            this.mAnimationFrame = AnimationFrame.newInstance();
        } else {
            animationFrame.clear();
        }
    }

    BindingXTimingHandler(Context context, PlatformManager platformManager, AnimationFrame animationFrame, Object... objArr) {
        super(context, platformManager, objArr);
        this.mAnimationFrame = animationFrame;
    }

    public void onBindExpression(String str, Map<String, Object> map, ExpressionPair expressionPair, List<Map<String, Object>> list, BindingXCore.JavaScriptCallback javaScriptCallback) {
        super.onBindExpression(str, map, expressionPair, list, javaScriptCallback);
        if (this.mAnimationFrame == null) {
            this.mAnimationFrame = AnimationFrame.newInstance();
        }
        fireEventByState("start", 0, new Object[0]);
        this.mAnimationFrame.clear();
        this.mAnimationFrame.requestAnimationFrame(this);
    }

    private void handleTimingCallback() {
        long j = 0;
        if (this.mStartTime == 0) {
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.isFinish = false;
        } else {
            j = AnimationUtils.currentAnimationTimeMillis() - this.mStartTime;
        }
        try {
            if (LogProxy.sEnableLog) {
                LogProxy.d(String.format(Locale.getDefault(), "[TimingHandler] timing elapsed. (t:%d)", new Object[]{Long.valueOf(j)}));
            }
            JSMath.applyTimingValuesToScope(this.mScope, (double) j);
            if (!this.isFinish) {
                consumeExpression(this.mExpressionHoldersMap, this.mScope, BindingXEventType.TYPE_TIMING);
            }
            this.isFinish = evaluateExitExpression(this.mExitExpressionPair, this.mScope);
        } catch (Exception e) {
            LogProxy.e("runtime error", e);
        }
    }

    public boolean onDisable(String str, String str2) {
        fireEventByState("end", System.currentTimeMillis() - this.mStartTime, new Object[0]);
        clearExpressions();
        AnimationFrame animationFrame = this.mAnimationFrame;
        if (animationFrame != null) {
            animationFrame.clear();
        }
        this.mStartTime = 0;
        return true;
    }

    public void onDestroy() {
        super.onDestroy();
        clearExpressions();
        AnimationFrame animationFrame = this.mAnimationFrame;
        if (animationFrame != null) {
            animationFrame.terminate();
            this.mAnimationFrame = null;
        }
        this.mStartTime = 0;
    }

    /* access modifiers changed from: protected */
    public void onExit(Map<String, Object> map) {
        fireEventByState(BindingXConstants.STATE_EXIT, (long) ((Double) map.get("t")).doubleValue(), new Object[0]);
        AnimationFrame animationFrame = this.mAnimationFrame;
        if (animationFrame != null) {
            animationFrame.clear();
        }
        this.mStartTime = 0;
    }

    /* access modifiers changed from: protected */
    public void onUserIntercept(String str, Map<String, Object> map) {
        fireEventByState(BindingXConstants.STATE_INTERCEPTOR, (long) ((Double) map.get("t")).doubleValue(), Collections.singletonMap(BindingXConstants.STATE_INTERCEPTOR, str));
    }

    private void fireEventByState(String str, long j, Object... objArr) {
        if (this.mCallback != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("state", str);
            hashMap.put("t", Long.valueOf(j));
            hashMap.put(BindingXConstants.KEY_TOKEN, this.mToken);
            if (objArr != null && objArr.length > 0 && (objArr[0] instanceof Map)) {
                hashMap.putAll(objArr[0]);
            }
            this.mCallback.callback(hashMap);
            LogProxy.d(">>>>>>>>>>>fire event:(" + str + "," + j + Operators.BRACKET_END_STR);
        }
    }

    public void doFrame() {
        handleTimingCallback();
    }
}
