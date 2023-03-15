package com.alibaba.android.bindingx.core;

import android.text.TextUtils;
import com.alibaba.android.bindingx.core.internal.JSFunctionInterface;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class BindingXJSFunctionRegister {
    private static final BindingXJSFunctionRegister sInstance = new BindingXJSFunctionRegister();
    private final LinkedHashMap<String, JSFunctionInterface> mJSFunctionMap = new LinkedHashMap<>(8);

    public static BindingXJSFunctionRegister getInstance() {
        return sInstance;
    }

    public void registerJSFunction(String str, JSFunctionInterface jSFunctionInterface) {
        if (!TextUtils.isEmpty(str) && jSFunctionInterface != null) {
            this.mJSFunctionMap.put(str, jSFunctionInterface);
        }
    }

    public boolean unregisterJSFunction(String str) {
        if (TextUtils.isEmpty(str) || this.mJSFunctionMap.remove(str) == null) {
            return false;
        }
        return true;
    }

    public void clear() {
        this.mJSFunctionMap.clear();
    }

    public Map<String, JSFunctionInterface> getJSFunctions() {
        return Collections.unmodifiableMap(this.mJSFunctionMap);
    }
}
