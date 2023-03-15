package com.alibaba.android.bindingx.core.internal;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.android.bindingx.core.BindingXCore;
import com.alibaba.android.bindingx.core.LogProxy;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.alibaba.android.bindingx.core.internal.OrientationDetector;
import com.taobao.weex.el.parse.Operators;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BindingXOrientationHandler extends AbstractEventHandler implements OrientationDetector.OnOrientationChangedListener {
    private boolean isStarted = false;
    private OrientationEvaluator mEvaluator3D;
    private OrientationEvaluator mEvaluatorX;
    private OrientationEvaluator mEvaluatorY;
    private double mLastAlpha;
    private double mLastBeta;
    private double mLastGamma;
    private OrientationDetector mOrientationDetector;
    private LinkedList<Double> mRecordsAlpha = new LinkedList<>();
    private String mSceneType;
    private double mStartAlpha;
    private double mStartBeta;
    private double mStartGamma;
    private ValueHolder mValueHolder = new ValueHolder(0.0d, 0.0d, 0.0d);
    private Vector3 mVectorX = new Vector3(0.0d, 0.0d, 1.0d);
    private Vector3 mVectorY = new Vector3(0.0d, 1.0d, 1.0d);

    public void onStart(String str, String str2) {
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public BindingXOrientationHandler(Context context, PlatformManager platformManager, Object... objArr) {
        super(context, platformManager, objArr);
        if (context != null) {
            this.mOrientationDetector = OrientationDetector.getInstance(context);
        }
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    BindingXOrientationHandler(Context context, PlatformManager platformManager, OrientationDetector orientationDetector, Object... objArr) {
        super(context, platformManager, objArr);
        this.mOrientationDetector = orientationDetector;
    }

    public boolean onCreate(String str, String str2) {
        OrientationDetector orientationDetector = this.mOrientationDetector;
        if (orientationDetector == null) {
            return false;
        }
        orientationDetector.addOrientationChangedListener(this);
        return this.mOrientationDetector.start(1);
    }

    public void onBindExpression(String str, Map<String, Object> map, ExpressionPair expressionPair, List<Map<String, Object>> list, BindingXCore.JavaScriptCallback javaScriptCallback) {
        String str2;
        super.onBindExpression(str, map, expressionPair, list, javaScriptCallback);
        if (map != null) {
            String str3 = (String) map.get(BindingXConstants.KEY_SCENE_TYPE);
            str2 = TextUtils.isEmpty(str3) ? "2d" : str3.toLowerCase();
        } else {
            str2 = null;
        }
        if (TextUtils.isEmpty(str2) || (!"2d".equals(str2) && !"3d".equals(str2))) {
            str2 = "2d";
        }
        this.mSceneType = str2;
        LogProxy.d("[ExpressionOrientationHandler] sceneType is " + str2);
        if ("2d".equals(str2)) {
            this.mEvaluatorX = new OrientationEvaluator((Double) null, Double.valueOf(90.0d), (Double) null);
            this.mEvaluatorY = new OrientationEvaluator(Double.valueOf(0.0d), (Double) null, Double.valueOf(90.0d));
        } else if ("3d".equals(str2)) {
            this.mEvaluator3D = new OrientationEvaluator((Double) null, (Double) null, (Double) null);
        }
    }

    public boolean onDisable(String str, String str2) {
        clearExpressions();
        if (this.mOrientationDetector == null) {
            return false;
        }
        fireEventByState("end", this.mLastAlpha, this.mLastBeta, this.mLastGamma, new Object[0]);
        return this.mOrientationDetector.removeOrientationChangedListener(this);
    }

    public void onDestroy() {
        super.onDestroy();
        OrientationDetector orientationDetector = this.mOrientationDetector;
        if (orientationDetector != null) {
            orientationDetector.removeOrientationChangedListener(this);
            this.mOrientationDetector.stop();
        }
        if (this.mExpressionHoldersMap != null) {
            this.mExpressionHoldersMap.clear();
            this.mExpressionHoldersMap = null;
        }
    }

    public void onOrientationChanged(double d, double d2, double d3) {
        char c;
        double d4;
        boolean z;
        double round = (double) Math.round(d);
        double round2 = (double) Math.round(d2);
        double round3 = (double) Math.round(d3);
        if (round != this.mLastAlpha || round2 != this.mLastBeta || round3 != this.mLastGamma) {
            if (!this.isStarted) {
                this.isStarted = true;
                c = 0;
                fireEventByState("start", round, round2, round3, new Object[0]);
                this.mStartAlpha = round;
                this.mStartBeta = round2;
                d4 = round3;
                this.mStartGamma = d4;
            } else {
                d4 = round3;
                c = 0;
            }
            if ("2d".equals(this.mSceneType)) {
                z = calculate2D(round, round2, d4);
            } else {
                z = "3d".equals(this.mSceneType) ? calculate3D(round, round2, d4) : false;
            }
            if (z) {
                double d5 = this.mValueHolder.x;
                double d6 = this.mValueHolder.y;
                double d7 = this.mValueHolder.z;
                this.mLastAlpha = round;
                this.mLastBeta = round2;
                this.mLastGamma = d4;
                try {
                    if (LogProxy.sEnableLog) {
                        Locale locale = Locale.getDefault();
                        Object[] objArr = new Object[6];
                        objArr[c] = Double.valueOf(round);
                        objArr[1] = Double.valueOf(round2);
                        objArr[2] = Double.valueOf(d4);
                        objArr[3] = Double.valueOf(d5);
                        objArr[4] = Double.valueOf(d6);
                        objArr[5] = Double.valueOf(d7);
                        LogProxy.d(String.format(locale, "[OrientationHandler] orientation changed. (alpha:%f,beta:%f,gamma:%f,x:%f,y:%f,z:%f)", objArr));
                    }
                    JSMath.applyOrientationValuesToScope(this.mScope, round, round2, d4, this.mStartAlpha, this.mStartBeta, this.mStartGamma, d5, d6, d7);
                    if (!evaluateExitExpression(this.mExitExpressionPair, this.mScope)) {
                        consumeExpression(this.mExpressionHoldersMap, this.mScope, "orientation");
                    }
                } catch (Exception e) {
                    LogProxy.e("runtime error", e);
                }
            }
        }
    }

    private boolean calculate2D(double d, double d2, double d3) {
        if (!(this.mEvaluatorX == null || this.mEvaluatorY == null)) {
            this.mRecordsAlpha.add(Double.valueOf(d));
            if (this.mRecordsAlpha.size() > 5) {
                this.mRecordsAlpha.removeFirst();
            }
            formatRecords(this.mRecordsAlpha, 360);
            LinkedList<Double> linkedList = this.mRecordsAlpha;
            double d4 = d;
            double d5 = d2;
            double d6 = d3;
            double doubleValue = (linkedList.get(linkedList.size() - 1).doubleValue() - this.mStartAlpha) % 360.0d;
            Quaternion calculate = this.mEvaluatorX.calculate(d4, d5, d6, doubleValue);
            Quaternion calculate2 = this.mEvaluatorY.calculate(d4, d5, d6, doubleValue);
            this.mVectorX.set(0.0d, 0.0d, 1.0d);
            this.mVectorX.applyQuaternion(calculate);
            this.mVectorY.set(0.0d, 1.0d, 1.0d);
            this.mVectorY.applyQuaternion(calculate2);
            double degrees = Math.toDegrees(Math.acos(this.mVectorX.x)) - 90.0d;
            double degrees2 = Math.toDegrees(Math.acos(this.mVectorY.y)) - 90.0d;
            if (Double.isNaN(degrees) || Double.isNaN(degrees2) || Double.isInfinite(degrees) || Double.isInfinite(degrees2)) {
                return false;
            }
            this.mValueHolder.x = (double) Math.round(degrees);
            this.mValueHolder.y = (double) Math.round(degrees2);
        }
        return true;
    }

    private boolean calculate3D(double d, double d2, double d3) {
        if (this.mEvaluator3D != null) {
            this.mRecordsAlpha.add(Double.valueOf(d));
            if (this.mRecordsAlpha.size() > 5) {
                this.mRecordsAlpha.removeFirst();
            }
            formatRecords(this.mRecordsAlpha, 360);
            LinkedList<Double> linkedList = this.mRecordsAlpha;
            Quaternion calculate = this.mEvaluator3D.calculate(d, d2, d3, (linkedList.get(linkedList.size() - 1).doubleValue() - this.mStartAlpha) % 360.0d);
            if (Double.isNaN(calculate.x) || Double.isNaN(calculate.y) || Double.isNaN(calculate.z) || Double.isInfinite(calculate.x) || Double.isInfinite(calculate.y) || Double.isInfinite(calculate.z)) {
                return false;
            }
            this.mValueHolder.x = calculate.x;
            this.mValueHolder.y = calculate.y;
            this.mValueHolder.z = calculate.z;
        }
        return true;
    }

    private void formatRecords(List<Double> list, int i) {
        int size = list.size();
        if (size > 1) {
            for (int i2 = 1; i2 < size; i2++) {
                int i3 = i2 - 1;
                if (!(list.get(i3) == null || list.get(i2) == null)) {
                    if (list.get(i2).doubleValue() - list.get(i3).doubleValue() < ((double) ((-i) / 2))) {
                        double doubleValue = list.get(i3).doubleValue();
                        double d = (double) i;
                        Double.isNaN(d);
                        double doubleValue2 = list.get(i2).doubleValue();
                        Double.isNaN(d);
                        list.set(i2, Double.valueOf(doubleValue2 + ((Math.floor(doubleValue / d) + 1.0d) * d)));
                    }
                    if (list.get(i2).doubleValue() - list.get(i3).doubleValue() > ((double) (i / 2))) {
                        double doubleValue3 = list.get(i2).doubleValue();
                        double d2 = (double) i;
                        Double.isNaN(d2);
                        list.set(i2, Double.valueOf(doubleValue3 - d2));
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onExit(Map<String, Object> map) {
        fireEventByState(BindingXConstants.STATE_EXIT, ((Double) map.get("alpha")).doubleValue(), ((Double) map.get("beta")).doubleValue(), ((Double) map.get("gamma")).doubleValue(), new Object[0]);
    }

    /* access modifiers changed from: protected */
    public void onUserIntercept(String str, Map<String, Object> map) {
        fireEventByState(BindingXConstants.STATE_INTERCEPTOR, ((Double) map.get("alpha")).doubleValue(), ((Double) map.get("beta")).doubleValue(), ((Double) map.get("gamma")).doubleValue(), Collections.singletonMap(BindingXConstants.STATE_INTERCEPTOR, str));
    }

    private void fireEventByState(String str, double d, double d2, double d3, Object... objArr) {
        if (this.mCallback != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("state", str);
            hashMap.put("alpha", Double.valueOf(d));
            hashMap.put("beta", Double.valueOf(d2));
            hashMap.put("gamma", Double.valueOf(d3));
            hashMap.put(BindingXConstants.KEY_TOKEN, this.mToken);
            if (objArr != null && objArr.length > 0 && (objArr[0] instanceof Map)) {
                hashMap.putAll(objArr[0]);
            }
            this.mCallback.callback(hashMap);
            LogProxy.d(">>>>>>>>>>>fire event:(" + str + "," + d + "," + d2 + "," + d3 + Operators.BRACKET_END_STR);
        }
    }

    public void onActivityPause() {
        OrientationDetector orientationDetector = this.mOrientationDetector;
        if (orientationDetector != null) {
            orientationDetector.stop();
        }
    }

    public void onActivityResume() {
        OrientationDetector orientationDetector = this.mOrientationDetector;
        if (orientationDetector != null) {
            orientationDetector.start(1);
        }
    }

    static class ValueHolder {
        double x;
        double y;
        double z;

        ValueHolder() {
        }

        ValueHolder(double d, double d2, double d3) {
            this.x = d;
            this.y = d2;
            this.z = d3;
        }
    }
}
