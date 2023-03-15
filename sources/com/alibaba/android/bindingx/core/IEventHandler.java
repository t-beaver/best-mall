package com.alibaba.android.bindingx.core;

import com.alibaba.android.bindingx.core.BindingXCore;
import com.alibaba.android.bindingx.core.internal.ExpressionPair;
import java.util.List;
import java.util.Map;

public interface IEventHandler extends IEventInterceptor {
    void onActivityPause();

    void onActivityResume();

    void onBindExpression(String str, Map<String, Object> map, ExpressionPair expressionPair, List<Map<String, Object>> list, BindingXCore.JavaScriptCallback javaScriptCallback);

    boolean onCreate(String str, String str2);

    void onDestroy();

    boolean onDisable(String str, String str2);

    void onStart(String str, String str2);

    void setAnchorInstanceId(String str);

    void setExtensionParams(Object[] objArr);

    void setGlobalConfig(Map<String, Object> map);

    void setToken(String str);
}
