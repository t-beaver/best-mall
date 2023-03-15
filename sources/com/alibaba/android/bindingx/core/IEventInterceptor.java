package com.alibaba.android.bindingx.core;

import com.alibaba.android.bindingx.core.internal.ExpressionPair;
import java.util.Map;

public interface IEventInterceptor {
    void performInterceptIfNeeded(String str, ExpressionPair expressionPair, Map<String, Object> map);

    void setInterceptors(Map<String, ExpressionPair> map);
}
