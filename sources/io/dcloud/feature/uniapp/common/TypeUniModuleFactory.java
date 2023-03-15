package io.dcloud.feature.uniapp.common;

import com.taobao.weex.WXEnvironment;
import com.taobao.weex.bridge.Invoker;
import com.taobao.weex.bridge.MethodInvoker;
import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniModuleFactory;
import io.dcloud.feature.uniapp.common.UniModule;
import io.dcloud.feature.uniapp.utils.UniLogUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TypeUniModuleFactory<T extends UniModule> implements UniModuleFactory<T> {
    public static final String TAG = "TypeModuleFactory";
    Class<T> mClazz;
    Map<String, Invoker> mMethodMap;

    public TypeUniModuleFactory(Class<T> cls) {
        this.mClazz = cls;
    }

    private void generateMethodMap() {
        if (WXEnvironment.isApkDebugable()) {
            UniLogUtils.d("TypeModuleFactory", "extractMethodNames:" + this.mClazz.getSimpleName());
        }
        HashMap hashMap = new HashMap();
        try {
            for (Method method : this.mClazz.getMethods()) {
                Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
                int length = declaredAnnotations.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    Annotation annotation = declaredAnnotations[i];
                    if (annotation != null) {
                        if (annotation instanceof UniJSMethod) {
                            UniJSMethod uniJSMethod = (UniJSMethod) annotation;
                            hashMap.put("_".equals(uniJSMethod.alias()) ? method.getName() : uniJSMethod.alias(), new MethodInvoker(method, uniJSMethod.uiThread()));
                        } else if (annotation instanceof UniModuleAnno) {
                            hashMap.put(method.getName(), new MethodInvoker(method, ((UniModuleAnno) annotation).runOnUIThread()));
                            break;
                        }
                    }
                    i++;
                }
            }
        } catch (Throwable th) {
            UniLogUtils.e("[WXModuleManager] extractMethodNames:", th);
        }
        this.mMethodMap = hashMap;
    }

    public T buildInstance() throws IllegalAccessException, InstantiationException {
        return (UniModule) this.mClazz.newInstance();
    }

    public String[] getMethods() {
        if (this.mMethodMap == null) {
            generateMethodMap();
        }
        Set<String> keySet = this.mMethodMap.keySet();
        return (String[]) keySet.toArray(new String[keySet.size()]);
    }

    public Invoker getMethodInvoker(String str) {
        if (this.mMethodMap == null) {
            generateMethodMap();
        }
        return this.mMethodMap.get(str);
    }
}
