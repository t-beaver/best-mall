package com.taobao.weex.ui;

import android.util.Log;
import android.util.Pair;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.bridge.Invoker;
import com.taobao.weex.common.WXRuntimeException;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXLogUtils;
import io.dcloud.feature.uniapp.UniSDKInstance;
import io.dcloud.feature.uniapp.ui.action.AbsComponentData;
import io.dcloud.feature.uniapp.ui.component.AbsVContainer;
import io.dcloud.feature.uniapp.ui.component.UniComponent;
import io.dcloud.feature.uniapp.ui.component.UniVContainer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class SimpleComponentHolder implements IFComponentHolder {
    public static final String TAG = "SimpleComponentHolder";
    private final Class<? extends WXComponent> mClz;
    private ComponentCreator mCreator;
    private Map<String, Invoker> mMethodInvokers;
    private Map<String, Invoker> mPropertyInvokers;

    public static class ClazzComponentCreator implements ComponentCreator {
        private Constructor<? extends WXComponent> mAbsConstructor;
        private final Class<? extends WXComponent> mCompClz;
        private Constructor<? extends WXComponent> mConstructor;

        public ClazzComponentCreator(Class<? extends WXComponent> cls) {
            this.mCompClz = cls;
        }

        private Constructor<? extends WXComponent> getComponentConstructor(Boolean bool) {
            Class cls;
            Class cls2;
            Class cls3;
            Class<? extends WXComponent> cls4 = this.mCompClz;
            if (bool.booleanValue()) {
                cls = UniSDKInstance.class;
                cls3 = AbsVContainer.class;
                cls2 = AbsComponentData.class;
            } else {
                cls = WXSDKInstance.class;
                cls3 = WXVContainer.class;
                cls2 = BasicComponentData.class;
            }
            try {
                return cls4.getConstructor(new Class[]{cls, cls3, cls2});
            } catch (NoSuchMethodException unused) {
                WXLogUtils.d("ClazzComponentCreator", "Use deprecated component constructor");
                try {
                    return cls4.getConstructor(new Class[]{cls, cls3, Boolean.TYPE, cls2});
                } catch (NoSuchMethodException unused2) {
                    try {
                        return cls4.getConstructor(new Class[]{cls, cls3, String.class, Boolean.TYPE, cls2});
                    } catch (NoSuchMethodException unused3) {
                        Log.e("shutao", "        " + cls4.getSuperclass().getSimpleName());
                        throw new WXRuntimeException("Can't find constructor of component.");
                    }
                }
            }
        }

        public WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            Constructor<? extends WXComponent> constructor;
            boolean z = UniVContainer.class.isAssignableFrom(this.mCompClz) || UniComponent.class.isAssignableFrom(this.mCompClz);
            if (z) {
                if (this.mAbsConstructor == null) {
                    this.mAbsConstructor = getComponentConstructor(Boolean.valueOf(z));
                }
                constructor = this.mAbsConstructor;
            } else {
                if (this.mConstructor == null) {
                    this.mConstructor = getComponentConstructor(Boolean.valueOf(z));
                }
                constructor = this.mConstructor;
            }
            int length = constructor.getParameterTypes().length;
            if (length == 3) {
                return (WXComponent) constructor.newInstance(new Object[]{wXSDKInstance, wXVContainer, basicComponentData});
            } else if (length == 4) {
                return (WXComponent) constructor.newInstance(new Object[]{wXSDKInstance, wXVContainer, false, basicComponentData});
            } else {
                return (WXComponent) constructor.newInstance(new Object[]{wXSDKInstance, wXVContainer, wXSDKInstance.getInstanceId(), Boolean.valueOf(wXVContainer.isLazy())});
            }
        }
    }

    public SimpleComponentHolder(Class<? extends WXComponent> cls) {
        this(cls, new ClazzComponentCreator(cls));
    }

    public SimpleComponentHolder(Class<? extends WXComponent> cls, ComponentCreator componentCreator) {
        this.mClz = cls;
        this.mCreator = componentCreator;
    }

    public void loadIfNonLazy() {
        Annotation[] declaredAnnotations = this.mClz.getDeclaredAnnotations();
        int length = declaredAnnotations.length;
        int i = 0;
        while (i < length) {
            Annotation annotation = declaredAnnotations[i];
            if (!(annotation instanceof Component)) {
                i++;
            } else if (!((Component) annotation).lazyload() && this.mMethodInvokers == null) {
                generate();
                return;
            } else {
                return;
            }
        }
    }

    private synchronized void generate() {
        if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.d("SimpleComponentHolder", "Generate Component:" + this.mClz.getSimpleName());
        }
        Pair<Map<String, Invoker>, Map<String, Invoker>> methods = getMethods(this.mClz);
        this.mPropertyInvokers = (Map) methods.first;
        this.mMethodInvokers = (Map) methods.second;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r9 = (com.taobao.weex.annotation.JSMethod) r9;
        r6 = r9.alias();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0046, code lost:
        if ("_".equals(r6) == false) goto L_0x004c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0048, code lost:
        r6 = r5.getName();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x004c, code lost:
        r1.put(r6, new com.taobao.weex.bridge.MethodInvoker(r5, r9.uiThread()));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.util.Pair<java.util.Map<java.lang.String, com.taobao.weex.bridge.Invoker>, java.util.Map<java.lang.String, com.taobao.weex.bridge.Invoker>> getMethods(java.lang.Class r13) {
        /*
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            java.util.HashMap r1 = new java.util.HashMap
            r1.<init>()
            java.lang.reflect.Method[] r13 = r13.getMethods()     // Catch:{ IndexOutOfBoundsException -> 0x009b, Exception -> 0x0094 }
            int r2 = r13.length     // Catch:{ IndexOutOfBoundsException -> 0x009b, Exception -> 0x0094 }
            r3 = 0
            r4 = 0
        L_0x0011:
            if (r4 >= r2) goto L_0x009f
            r5 = r13[r4]     // Catch:{ IndexOutOfBoundsException -> 0x009b, Exception -> 0x0094 }
            java.lang.annotation.Annotation[] r6 = r5.getDeclaredAnnotations()     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            int r7 = r6.length     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            r8 = 0
        L_0x001b:
            if (r8 >= r7) goto L_0x0090
            r9 = r6[r8]     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            if (r9 != 0) goto L_0x0022
            goto L_0x008d
        L_0x0022:
            boolean r10 = r9 instanceof com.taobao.weex.ui.component.WXComponentProp     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            r11 = 1
            if (r10 == 0) goto L_0x0036
            com.taobao.weex.ui.component.WXComponentProp r9 = (com.taobao.weex.ui.component.WXComponentProp) r9     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            java.lang.String r6 = r9.name()     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            com.taobao.weex.bridge.MethodInvoker r7 = new com.taobao.weex.bridge.MethodInvoker     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            r7.<init>(r5, r11)     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            r0.put(r6, r7)     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            goto L_0x0090
        L_0x0036:
            boolean r10 = r9 instanceof com.taobao.weex.annotation.JSMethod     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            java.lang.String r12 = "_"
            if (r10 == 0) goto L_0x0059
            com.taobao.weex.annotation.JSMethod r9 = (com.taobao.weex.annotation.JSMethod) r9     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            java.lang.String r6 = r9.alias()     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            boolean r7 = r12.equals(r6)     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            if (r7 == 0) goto L_0x004c
            java.lang.String r6 = r5.getName()     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
        L_0x004c:
            com.taobao.weex.bridge.MethodInvoker r7 = new com.taobao.weex.bridge.MethodInvoker     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            boolean r8 = r9.uiThread()     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            r7.<init>(r5, r8)     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            r1.put(r6, r7)     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            goto L_0x0090
        L_0x0059:
            boolean r10 = r9 instanceof io.dcloud.feature.uniapp.ui.component.UniComponentProp     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            if (r10 == 0) goto L_0x006c
            io.dcloud.feature.uniapp.ui.component.UniComponentProp r9 = (io.dcloud.feature.uniapp.ui.component.UniComponentProp) r9     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            java.lang.String r6 = r9.name()     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            com.taobao.weex.bridge.MethodInvoker r7 = new com.taobao.weex.bridge.MethodInvoker     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            r7.<init>(r5, r11)     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            r0.put(r6, r7)     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            goto L_0x0090
        L_0x006c:
            boolean r10 = r9 instanceof io.dcloud.feature.uniapp.annotation.UniJSMethod     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            if (r10 == 0) goto L_0x008d
            io.dcloud.feature.uniapp.annotation.UniJSMethod r9 = (io.dcloud.feature.uniapp.annotation.UniJSMethod) r9     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            java.lang.String r6 = r9.alias()     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            boolean r7 = r12.equals(r6)     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            if (r7 == 0) goto L_0x0080
            java.lang.String r6 = r5.getName()     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
        L_0x0080:
            com.taobao.weex.bridge.MethodInvoker r7 = new com.taobao.weex.bridge.MethodInvoker     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            boolean r8 = r9.uiThread()     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            r7.<init>(r5, r8)     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            r1.put(r6, r7)     // Catch:{ ArrayIndexOutOfBoundsException | IncompatibleClassChangeError -> 0x0090 }
            goto L_0x0090
        L_0x008d:
            int r8 = r8 + 1
            goto L_0x001b
        L_0x0090:
            int r4 = r4 + 1
            goto L_0x0011
        L_0x0094:
            r13 = move-exception
            java.lang.String r2 = "SimpleComponentHolder"
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r2, (java.lang.Throwable) r13)
            goto L_0x009f
        L_0x009b:
            r13 = move-exception
            r13.printStackTrace()
        L_0x009f:
            android.util.Pair r13 = new android.util.Pair
            r13.<init>(r0, r1)
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.SimpleComponentHolder.getMethods(java.lang.Class):android.util.Pair");
    }

    public synchronized WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        WXComponent createInstance;
        createInstance = this.mCreator.createInstance(wXSDKInstance, wXVContainer, basicComponentData);
        createInstance.bindHolder(this);
        return createInstance;
    }

    public synchronized Invoker getPropertyInvoker(String str) {
        if (this.mPropertyInvokers == null) {
            generate();
        }
        return this.mPropertyInvokers.get(str);
    }

    public Invoker getMethodInvoker(String str) {
        if (this.mMethodInvokers == null) {
            generate();
        }
        return this.mMethodInvokers.get(str);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:10|11|(1:13)|14|15|16) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        r0 = r4.mClz;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001e, code lost:
        if (r0 != null) goto L_0x0020;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0020, code lost:
        r0 = r0.getName();
        com.taobao.weex.utils.WXExceptionUtils.commitCriticalExceptionRT((java.lang.String) null, com.taobao.weex.common.WXErrorCode.WX_KEY_EXCEPTION_INVOKE_REGISTER_COMPONENT, com.taobao.weex.bridge.WXBridgeManager.METHOD_REGISTER_COMPONENTS, r0 + ": gen methods failed", (java.util.Map<java.lang.String, java.lang.String>) null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0041, code lost:
        return new java.lang.String[1];
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x001c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String[] getMethods() {
        /*
            r4 = this;
            monitor-enter(r4)
            java.util.Map<java.lang.String, com.taobao.weex.bridge.Invoker> r0 = r4.mMethodInvokers     // Catch:{ all -> 0x0042 }
            if (r0 != 0) goto L_0x0008
            r4.generate()     // Catch:{ all -> 0x0042 }
        L_0x0008:
            java.util.Map<java.lang.String, com.taobao.weex.bridge.Invoker> r0 = r4.mMethodInvokers     // Catch:{ all -> 0x0042 }
            java.util.Set r0 = r0.keySet()     // Catch:{ all -> 0x0042 }
            int r1 = r0.size()     // Catch:{ all -> 0x001c }
            java.lang.String[] r1 = new java.lang.String[r1]     // Catch:{ all -> 0x001c }
            java.lang.Object[] r0 = r0.toArray(r1)     // Catch:{ all -> 0x001c }
            java.lang.String[] r0 = (java.lang.String[]) r0     // Catch:{ all -> 0x001c }
            monitor-exit(r4)
            return r0
        L_0x001c:
            java.lang.Class<? extends com.taobao.weex.ui.component.WXComponent> r0 = r4.mClz     // Catch:{ all -> 0x0042 }
            if (r0 == 0) goto L_0x003d
            java.lang.String r0 = r0.getName()     // Catch:{ all -> 0x0042 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0042 }
            r1.<init>()     // Catch:{ all -> 0x0042 }
            r1.append(r0)     // Catch:{ all -> 0x0042 }
            java.lang.String r0 = ": gen methods failed"
            r1.append(r0)     // Catch:{ all -> 0x0042 }
            java.lang.String r0 = r1.toString()     // Catch:{ all -> 0x0042 }
            com.taobao.weex.common.WXErrorCode r1 = com.taobao.weex.common.WXErrorCode.WX_KEY_EXCEPTION_INVOKE_REGISTER_COMPONENT     // Catch:{ all -> 0x0042 }
            java.lang.String r2 = "registerComponents"
            r3 = 0
            com.taobao.weex.utils.WXExceptionUtils.commitCriticalExceptionRT(r3, r1, r2, r0, r3)     // Catch:{ all -> 0x0042 }
        L_0x003d:
            r0 = 1
            java.lang.String[] r0 = new java.lang.String[r0]     // Catch:{ all -> 0x0042 }
            monitor-exit(r4)
            return r0
        L_0x0042:
            r0 = move-exception
            monitor-exit(r4)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.SimpleComponentHolder.getMethods():java.lang.String[]");
    }
}
