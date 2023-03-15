package com.alibaba.android.bindingx.core.internal;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.alibaba.android.bindingx.core.BindingXCore;
import com.alibaba.android.bindingx.core.BindingXJSFunctionRegister;
import com.alibaba.android.bindingx.core.BindingXPropertyInterceptor;
import com.alibaba.android.bindingx.core.IEventHandler;
import com.alibaba.android.bindingx.core.LogProxy;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.taobao.weex.el.parse.Operators;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class AbstractEventHandler implements IEventHandler {
    protected String mAnchorInstanceId;
    private Cache<String, Expression> mCachedExpressionMap = new Cache<>(16);
    protected BindingXCore.JavaScriptCallback mCallback;
    protected Context mContext;
    protected ExpressionPair mExitExpressionPair;
    protected volatile Map<String, List<ExpressionHolder>> mExpressionHoldersMap;
    protected Object[] mExtensionParams;
    protected String mInstanceId;
    protected volatile Map<String, ExpressionPair> mInterceptorsMap;
    protected PlatformManager mPlatformManager;
    protected final Map<String, Object> mScope = new HashMap();
    protected String mToken;

    /* access modifiers changed from: protected */
    public abstract void onExit(Map<String, Object> map);

    /* access modifiers changed from: protected */
    public abstract void onUserIntercept(String str, Map<String, Object> map);

    public void setGlobalConfig(Map<String, Object> map) {
    }

    public AbstractEventHandler(Context context, PlatformManager platformManager, Object... objArr) {
        this.mContext = context;
        this.mPlatformManager = platformManager;
        this.mInstanceId = (objArr == null || objArr.length <= 0 || !(objArr[0] instanceof String)) ? null : objArr[0];
    }

    public void setAnchorInstanceId(String str) {
        this.mAnchorInstanceId = str;
    }

    public void onBindExpression(String str, Map<String, Object> map, ExpressionPair expressionPair, List<Map<String, Object>> list, BindingXCore.JavaScriptCallback javaScriptCallback) {
        clearExpressions();
        transformArgs(str, list);
        this.mCallback = javaScriptCallback;
        this.mExitExpressionPair = expressionPair;
        if (!this.mScope.isEmpty()) {
            this.mScope.clear();
        }
        applyFunctionsToScope();
    }

    public void onDestroy() {
        this.mCachedExpressionMap.clear();
        BindingXPropertyInterceptor.getInstance().clearCallbacks();
    }

    private void applyFunctionsToScope() {
        JSMath.applyToScope(this.mScope);
        TimingFunctions.applyToScope(this.mScope);
        Map<String, JSFunctionInterface> jSFunctions = BindingXJSFunctionRegister.getInstance().getJSFunctions();
        if (jSFunctions != null && !jSFunctions.isEmpty()) {
            this.mScope.putAll(jSFunctions);
        }
    }

    private void transformArgs(String str, List<Map<String, Object>> list) {
        Map<String, Object> map;
        if (this.mExpressionHoldersMap == null) {
            this.mExpressionHoldersMap = new HashMap();
        }
        for (Map next : list) {
            String stringValue = Utils.getStringValue(next, BindingXConstants.KEY_ELEMENT);
            String stringValue2 = Utils.getStringValue(next, "instanceId");
            String stringValue3 = Utils.getStringValue(next, "property");
            ExpressionPair expressionPair = Utils.getExpressionPair(next, BindingXConstants.KEY_EXPRESSION);
            Object obj = next.get(BindingXConstants.KEY_CONFIG);
            if (obj != null && (obj instanceof Map)) {
                try {
                    map = Utils.toMap(new JSONObject((Map) obj));
                } catch (Exception e) {
                    LogProxy.e("parse config failed", e);
                }
                if (!TextUtils.isEmpty(stringValue) || !TextUtils.isEmpty(stringValue3) || expressionPair == null) {
                    LogProxy.e("skip illegal binding args[" + stringValue + "," + stringValue3 + "," + expressionPair + Operators.ARRAY_END_STR);
                } else {
                    ExpressionHolder expressionHolder = new ExpressionHolder(stringValue, stringValue2, expressionPair, stringValue3, str, map);
                    List list2 = this.mExpressionHoldersMap.get(stringValue);
                    if (list2 == null) {
                        ArrayList arrayList = new ArrayList(4);
                        this.mExpressionHoldersMap.put(stringValue, arrayList);
                        arrayList.add(expressionHolder);
                    } else if (!list2.contains(expressionHolder)) {
                        list2.add(expressionHolder);
                    }
                }
            }
            map = null;
            if (!TextUtils.isEmpty(stringValue) && !TextUtils.isEmpty(stringValue3)) {
            }
            LogProxy.e("skip illegal binding args[" + stringValue + "," + stringValue3 + "," + expressionPair + Operators.ARRAY_END_STR);
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0021  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean evaluateExitExpression(com.alibaba.android.bindingx.core.internal.ExpressionPair r2, java.util.Map<java.lang.String, java.lang.Object> r3) {
        /*
            r1 = this;
            boolean r0 = com.alibaba.android.bindingx.core.internal.ExpressionPair.isValid(r2)
            if (r0 == 0) goto L_0x001e
            com.alibaba.android.bindingx.core.internal.Expression r0 = new com.alibaba.android.bindingx.core.internal.Expression
            java.lang.String r2 = r2.transformed
            r0.<init>((java.lang.String) r2)
            java.lang.Object r2 = r0.execute(r3)     // Catch:{ Exception -> 0x0018 }
            java.lang.Boolean r2 = (java.lang.Boolean) r2     // Catch:{ Exception -> 0x0018 }
            boolean r2 = r2.booleanValue()     // Catch:{ Exception -> 0x0018 }
            goto L_0x001f
        L_0x0018:
            r2 = move-exception
            java.lang.String r0 = "evaluateExitExpression failed. "
            com.alibaba.android.bindingx.core.LogProxy.e(r0, r2)
        L_0x001e:
            r2 = 0
        L_0x001f:
            if (r2 == 0) goto L_0x0033
            r1.clearExpressions()
            r1.onExit(r3)     // Catch:{ Exception -> 0x0028 }
            goto L_0x002e
        L_0x0028:
            r3 = move-exception
            java.lang.String r0 = "execute exit expression failed: "
            com.alibaba.android.bindingx.core.LogProxy.e(r0, r3)
        L_0x002e:
            java.lang.String r3 = "exit = true,consume finished"
            com.alibaba.android.bindingx.core.LogProxy.d(r3)
        L_0x0033:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.bindingx.core.internal.AbstractEventHandler.evaluateExitExpression(com.alibaba.android.bindingx.core.internal.ExpressionPair, java.util.Map):boolean");
    }

    public void setInterceptors(Map<String, ExpressionPair> map) {
        this.mInterceptorsMap = map;
    }

    public void performInterceptIfNeeded(String str, ExpressionPair expressionPair, Map<String, Object> map) {
        if (ExpressionPair.isValid(expressionPair)) {
            Expression expression = new Expression(expressionPair.transformed);
            boolean z = false;
            try {
                z = ((Boolean) expression.execute(map)).booleanValue();
            } catch (Exception e) {
                LogProxy.e("evaluate interceptor [" + str + "] expression failed. ", e);
            }
            if (z) {
                onUserIntercept(str, map);
            }
        }
    }

    private void tryInterceptAllIfNeeded(Map<String, Object> map) {
        if (this.mInterceptorsMap != null && !this.mInterceptorsMap.isEmpty()) {
            for (Map.Entry next : this.mInterceptorsMap.entrySet()) {
                String str = (String) next.getKey();
                ExpressionPair expressionPair = (ExpressionPair) next.getValue();
                if (!TextUtils.isEmpty(str) && expressionPair != null) {
                    performInterceptIfNeeded(str, expressionPair, map);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void consumeExpression(Map<String, List<ExpressionHolder>> map, Map<String, Object> map2, String str) throws IllegalArgumentException, JSONException {
        Map<String, Object> map3 = map2;
        String str2 = str;
        tryInterceptAllIfNeeded(map3);
        if (map == null) {
            LogProxy.e("expression args is null");
        } else if (map.isEmpty()) {
            LogProxy.e("no expression need consumed");
        } else {
            int i = 2;
            if (LogProxy.sEnableLog) {
                LogProxy.d(String.format(Locale.getDefault(), "consume expression with %d tasks. event type is %s", new Object[]{Integer.valueOf(map.size()), str2}));
            }
            LinkedList linkedList = new LinkedList();
            for (List<ExpressionHolder> it : map.values()) {
                for (ExpressionHolder expressionHolder : it) {
                    if (!str2.equals(expressionHolder.eventType)) {
                        LogProxy.d("skip expression with wrong event type.[expected:" + str2 + ",found:" + expressionHolder.eventType + Operators.ARRAY_END_STR);
                    } else {
                        linkedList.clear();
                        Object[] objArr = this.mExtensionParams;
                        if (objArr != null && objArr.length > 0) {
                            Collections.addAll(linkedList, objArr);
                        }
                        String str3 = TextUtils.isEmpty(expressionHolder.targetInstanceId) ? this.mInstanceId : expressionHolder.targetInstanceId;
                        if (!TextUtils.isEmpty(str3)) {
                            linkedList.add(str3);
                        }
                        ExpressionPair expressionPair = expressionHolder.expressionPair;
                        if (ExpressionPair.isValid(expressionPair)) {
                            Expression expression = (Expression) this.mCachedExpressionMap.get(expressionPair.transformed);
                            if (expression == null) {
                                expression = new Expression(expressionPair.transformed);
                                this.mCachedExpressionMap.put(expressionPair.transformed, expression);
                            }
                            Object execute = expression.execute(map3);
                            if (execute == null) {
                                LogProxy.e("failed to execute expression,expression result is null");
                            } else if ((!(execute instanceof Double) || !Double.isNaN(((Double) execute).doubleValue())) && (!(execute instanceof Float) || !Float.isNaN(((Float) execute).floatValue()))) {
                                View findViewBy = this.mPlatformManager.getViewFinder().findViewBy(expressionHolder.targetRef, linkedList.toArray());
                                BindingXPropertyInterceptor instance = BindingXPropertyInterceptor.getInstance();
                                String str4 = expressionHolder.prop;
                                PlatformManager.IDeviceResolutionTranslator resolutionTranslator = this.mPlatformManager.getResolutionTranslator();
                                Map<String, Object> map4 = expressionHolder.config;
                                Object[] objArr2 = new Object[i];
                                objArr2[0] = expressionHolder.targetRef;
                                objArr2[1] = str3;
                                instance.performIntercept(findViewBy, str4, execute, resolutionTranslator, map4, objArr2);
                                if (findViewBy == null) {
                                    LogProxy.e("failed to execute expression,target view not found.[ref:" + expressionHolder.targetRef + Operators.ARRAY_END_STR);
                                    map3 = map2;
                                    i = 2;
                                } else {
                                    i = 2;
                                    this.mPlatformManager.getViewUpdater().synchronouslyUpdateViewOnUIThread(findViewBy, expressionHolder.prop, execute, this.mPlatformManager.getResolutionTranslator(), expressionHolder.config, expressionHolder.targetRef, str3);
                                    map3 = map2;
                                }
                            } else {
                                LogProxy.e("failed to execute expression,expression result is NaN");
                            }
                        }
                    }
                }
                map3 = map2;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void clearExpressions() {
        LogProxy.d("all expression are cleared");
        if (this.mExpressionHoldersMap != null) {
            this.mExpressionHoldersMap.clear();
            this.mExpressionHoldersMap = null;
        }
        this.mExitExpressionPair = null;
    }

    public void setToken(String str) {
        this.mToken = str;
    }

    public void setExtensionParams(Object[] objArr) {
        this.mExtensionParams = objArr;
    }

    static class Cache<K, V> extends LinkedHashMap<K, V> {
        private int maxSize;

        Cache(int i) {
            super(4, 0.75f, true);
            this.maxSize = Math.max(i, 4);
        }

        /* access modifiers changed from: protected */
        public boolean removeEldestEntry(Map.Entry entry) {
            return size() > this.maxSize;
        }
    }
}
