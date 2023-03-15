package com.alibaba.android.bindingx.core.internal;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.alibaba.android.bindingx.core.BindingXCore;
import com.alibaba.android.bindingx.core.BindingXEventType;
import com.alibaba.android.bindingx.core.LogProxy;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.taobao.weex.el.parse.Operators;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BindingXTouchHandler extends AbstractEventHandler implements View.OnTouchListener, GestureDetector.OnGestureListener {
    private long downTimeMillis = 0;
    private float downX = 0.0f;
    private float downY = 0.0f;
    private boolean isFlickGestureAvailable;
    private boolean isPanGestureAvailable;
    private float mDownX;
    private float mDownY;
    private double mDx;
    private double mDy;
    private GestureDetector mGestureDetector;
    private int movetime = 0;
    private int touchTime = 0;
    private float upX = 0.0f;
    private float upY = 0.0f;

    public void onActivityPause() {
    }

    public void onActivityResume() {
    }

    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    public void onLongPress(MotionEvent motionEvent) {
    }

    public void onShowPress(MotionEvent motionEvent) {
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    public BindingXTouchHandler(Context context, PlatformManager platformManager, Object... objArr) {
        super(context, platformManager, objArr);
        this.mGestureDetector = new GestureDetector(context, this, new Handler(Looper.myLooper() == null ? Looper.getMainLooper() : Looper.myLooper()));
    }

    /* access modifiers changed from: package-private */
    public void setPanGestureAvailable(boolean z) {
        this.isPanGestureAvailable = z;
    }

    /* access modifiers changed from: package-private */
    public void setFlickGestureAvailable(boolean z) {
        this.isFlickGestureAvailable = z;
    }

    /* access modifiers changed from: package-private */
    public boolean isPanGestureAvailable() {
        return this.isPanGestureAvailable;
    }

    /* access modifiers changed from: package-private */
    public boolean isFlickGestureAvailable() {
        return this.isFlickGestureAvailable;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        try {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                this.mDownX = motionEvent.getRawX();
                this.mDownY = motionEvent.getRawY();
                fireEventByState("start", 0.0d, 0.0d, new Object[0]);
                this.downTimeMillis = System.currentTimeMillis();
                this.downX = motionEvent.getRawX();
                this.downY = motionEvent.getRawY();
            } else if (actionMasked == 1) {
                this.mDownX = 0.0f;
                this.mDownY = 0.0f;
                this.upX = motionEvent.getRawX();
                this.upY = motionEvent.getRawY();
                clearExpressions();
                fireEventByState("end", this.mDx, this.mDy, new Object[0]);
                this.mDx = 0.0d;
                this.mDy = 0.0d;
            } else if (actionMasked != 2) {
                if (actionMasked == 3) {
                    this.mDownX = 0.0f;
                    this.mDownY = 0.0f;
                    clearExpressions();
                    fireEventByState(BindingXConstants.STATE_CANCEL, this.mDx, this.mDy, new Object[0]);
                }
            } else if (this.mDownX == 0.0f && this.mDownY == 0.0f) {
                this.mDownX = motionEvent.getRawX();
                this.mDownY = motionEvent.getRawY();
                fireEventByState("start", 0.0d, 0.0d, new Object[0]);
            } else {
                this.mDx = (double) (motionEvent.getRawX() - this.mDownX);
                this.mDy = (double) (motionEvent.getRawY() - this.mDownY);
            }
        } catch (Exception e) {
            LogProxy.e("runtime error ", e);
        }
        return this.mGestureDetector.onTouchEvent(motionEvent);
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        float f3;
        float f4;
        this.movetime++;
        if (!this.isPanGestureAvailable) {
            LogProxy.d("pan gesture is not enabled");
            return false;
        }
        if (motionEvent == null) {
            f4 = this.mDownX;
            f3 = this.mDownY;
        } else {
            float rawX = motionEvent.getRawX();
            f3 = motionEvent.getRawY();
            f4 = rawX;
        }
        if (motionEvent2 == null) {
            return false;
        }
        float rawX2 = motionEvent2.getRawX() - f4;
        float rawY = motionEvent2.getRawY() - f3;
        try {
            if (LogProxy.sEnableLog) {
                LogProxy.d(String.format(Locale.getDefault(), "[TouchHandler] pan moved. (x:%f,y:%f)", new Object[]{Float.valueOf(rawX2), Float.valueOf(rawY)}));
            }
            JSMath.applyXYToScope(this.mScope, (double) rawX2, (double) rawY, this.mPlatformManager.getResolutionTranslator());
            if (!evaluateExitExpression(this.mExitExpressionPair, this.mScope)) {
                consumeExpression(this.mExpressionHoldersMap, this.mScope, "pan");
            }
        } catch (Exception e) {
            LogProxy.e("runtime error", e);
        }
        return false;
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (!this.isFlickGestureAvailable) {
        }
        return false;
    }

    public boolean onCreate(String str, String str2) {
        View findViewBy = this.mPlatformManager.getViewFinder().findViewBy(str, TextUtils.isEmpty(this.mAnchorInstanceId) ? this.mInstanceId : this.mAnchorInstanceId);
        if (findViewBy == null) {
            LogProxy.e("[ExpressionTouchHandler] onCreate failed. sourceView not found:" + str);
            return false;
        }
        this.movetime = 0;
        findViewBy.setOnTouchListener(this);
        try {
            Method declaredMethod = findViewBy.getClass().getDeclaredMethod("addPan", new Class[]{Object.class});
            if (declaredMethod != null) {
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(findViewBy, new Object[]{this});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogProxy.d("[ExpressionTouchHandler] onCreate success. {source:" + str + ",type:" + str2 + Operators.BLOCK_END_STR);
        return true;
    }

    public void onStart(String str, String str2) {
        str2.hashCode();
        if (str2.equals("pan")) {
            setPanGestureAvailable(true);
        } else if (str2.equals(BindingXEventType.TYPE_FLICK)) {
            setFlickGestureAvailable(true);
        }
    }

    public void onBindExpression(String str, Map<String, Object> map, ExpressionPair expressionPair, List<Map<String, Object>> list, BindingXCore.JavaScriptCallback javaScriptCallback) {
        super.onBindExpression(str, map, expressionPair, list, javaScriptCallback);
    }

    public boolean onDisable(String str, String str2) {
        str2.hashCode();
        if (str2.equals("pan")) {
            setPanGestureAvailable(false);
        } else if (str2.equals(BindingXEventType.TYPE_FLICK)) {
            setFlickGestureAvailable(false);
        }
        if (isPanGestureAvailable() || isFlickGestureAvailable()) {
            return false;
        }
        View findViewBy = this.mPlatformManager.getViewFinder().findViewBy(str, TextUtils.isEmpty(this.mAnchorInstanceId) ? this.mInstanceId : this.mAnchorInstanceId);
        if (findViewBy != null) {
            findViewBy.setOnTouchListener((View.OnTouchListener) null);
            this.movetime = 0;
            try {
                Method declaredMethod = findViewBy.getClass().getDeclaredMethod("removePan", new Class[0]);
                if (declaredMethod != null) {
                    declaredMethod.setAccessible(true);
                    declaredMethod.invoke(findViewBy, new Object[0]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LogProxy.d("remove touch listener success.[" + str + "," + str2 + Operators.ARRAY_END_STR);
        return true;
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mExpressionHoldersMap != null) {
            this.mExpressionHoldersMap.clear();
            this.mExpressionHoldersMap = null;
        }
        this.mExitExpressionPair = null;
        this.mCallback = null;
        this.isFlickGestureAvailable = false;
        this.isPanGestureAvailable = false;
    }

    /* access modifiers changed from: protected */
    public void onExit(Map<String, Object> map) {
        fireEventByState(BindingXConstants.STATE_EXIT, ((Double) map.get("internal_x")).doubleValue(), ((Double) map.get("internal_y")).doubleValue(), new Object[0]);
    }

    /* access modifiers changed from: protected */
    public void onUserIntercept(String str, Map<String, Object> map) {
        fireEventByState(BindingXConstants.STATE_INTERCEPTOR, ((Double) map.get("internal_x")).doubleValue(), ((Double) map.get("internal_y")).doubleValue(), Collections.singletonMap(BindingXConstants.STATE_INTERCEPTOR, str));
    }

    private void fireEventByState(String str, double d, double d2, Object... objArr) {
        if (this.mCallback != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("state", str);
            double nativeToWeb = this.mPlatformManager.getResolutionTranslator().nativeToWeb(d, new Object[0]);
            double nativeToWeb2 = this.mPlatformManager.getResolutionTranslator().nativeToWeb(d2, new Object[0]);
            hashMap.put("deltaX", Double.valueOf(nativeToWeb));
            hashMap.put("deltaY", Double.valueOf(nativeToWeb2));
            hashMap.put(BindingXConstants.KEY_TOKEN, this.mToken);
            if (objArr != null && objArr.length > 0 && (objArr[0] instanceof Map)) {
                hashMap.putAll(objArr[0]);
            }
            this.mCallback.callback(hashMap);
            LogProxy.d(">>>>>>>>>>>fire event:(" + str + "," + nativeToWeb + "," + nativeToWeb2 + Operators.BRACKET_END_STR);
        }
    }
}
