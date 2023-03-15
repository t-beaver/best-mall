package com.alibaba.android.bindingx.core;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.alibaba.android.bindingx.core.internal.BindingXOrientationHandler;
import com.alibaba.android.bindingx.core.internal.BindingXTimingHandler;
import com.alibaba.android.bindingx.core.internal.BindingXTouchHandler;
import com.alibaba.android.bindingx.core.internal.ExpressionPair;
import com.alibaba.android.bindingx.core.internal.Utils;
import com.taobao.weex.el.parse.Operators;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.json.JSONObject;

public class BindingXCore {
    private static final Map<String, ObjectCreator<IEventHandler, Context, PlatformManager>> sGlobalEventHandlerCreatorMap = new HashMap(4);
    private Map<String, Map<String, IEventHandler>> mBindingCouples;
    private final Map<String, ObjectCreator<IEventHandler, Context, PlatformManager>> mInternalEventHandlerCreatorMap = new HashMap(8);
    private final PlatformManager mPlatformManager;

    public interface JavaScriptCallback {
        void callback(Object obj);
    }

    public interface ObjectCreator<Type, ParamA, ParamB> {
        Type createWith(ParamA parama, ParamB paramb, Object... objArr);
    }

    public BindingXCore(PlatformManager platformManager) {
        this.mPlatformManager = platformManager;
        registerEventHandler("pan", new ObjectCreator<IEventHandler, Context, PlatformManager>() {
            public IEventHandler createWith(Context context, PlatformManager platformManager, Object... objArr) {
                return new BindingXTouchHandler(context, platformManager, objArr);
            }
        });
        registerEventHandler("orientation", new ObjectCreator<IEventHandler, Context, PlatformManager>() {
            public IEventHandler createWith(Context context, PlatformManager platformManager, Object... objArr) {
                return new BindingXOrientationHandler(context, platformManager, objArr);
            }
        });
        registerEventHandler(BindingXEventType.TYPE_TIMING, new ObjectCreator<IEventHandler, Context, PlatformManager>() {
            public IEventHandler createWith(Context context, PlatformManager platformManager, Object... objArr) {
                return new BindingXTimingHandler(context, platformManager, objArr);
            }
        });
    }

    public String doBind(Context context, String str, Map<String, Object> map, JavaScriptCallback javaScriptCallback, Object... objArr) {
        Map<String, Object> map2;
        Map<String, Object> map3 = map;
        String stringValue = Utils.getStringValue(map3, BindingXConstants.KEY_EVENT_TYPE);
        String stringValue2 = Utils.getStringValue(map3, "instanceId");
        LogProxy.enableLogIfNeeded(map);
        Object obj = map3.get("options");
        if (obj != null && (obj instanceof Map)) {
            try {
                map2 = Utils.toMap(new JSONObject((Map) obj));
            } catch (Exception e) {
                LogProxy.e("parse external config failed.\n", e);
            }
            ExpressionPair expressionPair = Utils.getExpressionPair(map3, BindingXConstants.KEY_EXIT_EXPRESSION);
            String stringValue3 = Utils.getStringValue(map3, BindingXConstants.KEY_ANCHOR);
            return doBind(stringValue3, stringValue2, stringValue, map2, expressionPair, Utils.getRuntimeProps(map), Utils.getCustomInterceptors(map), javaScriptCallback, context, str, objArr);
        }
        map2 = null;
        ExpressionPair expressionPair2 = Utils.getExpressionPair(map3, BindingXConstants.KEY_EXIT_EXPRESSION);
        String stringValue32 = Utils.getStringValue(map3, BindingXConstants.KEY_ANCHOR);
        return doBind(stringValue32, stringValue2, stringValue, map2, expressionPair2, Utils.getRuntimeProps(map), Utils.getCustomInterceptors(map), javaScriptCallback, context, str, objArr);
    }

    public void doUnbind(Map<String, Object> map) {
        if (map != null) {
            doUnbind(Utils.getStringValue(map, BindingXConstants.KEY_TOKEN), Utils.getStringValue(map, BindingXConstants.KEY_EVENT_TYPE));
        }
    }

    public void doUnbind(String str, String str2) {
        LogProxy.d("disable binding [" + str + "," + str2 + Operators.ARRAY_END_STR);
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            LogProxy.d("disable binding failed(0x1) [" + str + "," + str2 + Operators.ARRAY_END_STR);
            return;
        }
        Map<String, Map<String, IEventHandler>> map = this.mBindingCouples;
        if (map == null || map.isEmpty()) {
            LogProxy.d("disable binding failed(0x2) [" + str + "," + str2 + Operators.ARRAY_END_STR);
            return;
        }
        Map map2 = this.mBindingCouples.get(str);
        if (map2 == null || map2.isEmpty()) {
            LogProxy.d("disable binding failed(0x3) [" + str + "," + str2 + Operators.ARRAY_END_STR);
            return;
        }
        IEventHandler iEventHandler = (IEventHandler) map2.get(str2);
        if (iEventHandler == null) {
            LogProxy.d("disable binding failed(0x4) [" + str + "," + str2 + Operators.ARRAY_END_STR);
        } else if (iEventHandler.onDisable(str, str2)) {
            this.mBindingCouples.remove(str);
            LogProxy.d("disable binding success[" + str + "," + str2 + Operators.ARRAY_END_STR);
        } else {
            LogProxy.d("disabled failed(0x4) [" + str + "," + str2 + Operators.ARRAY_END_STR);
        }
    }

    public void doRelease() {
        Map<String, Map<String, IEventHandler>> map = this.mBindingCouples;
        if (map != null) {
            try {
                for (Map next : map.values()) {
                    if (next != null && !next.isEmpty()) {
                        for (IEventHandler iEventHandler : next.values()) {
                            if (iEventHandler != null) {
                                iEventHandler.onDestroy();
                            }
                        }
                    }
                }
                this.mBindingCouples.clear();
                this.mBindingCouples = null;
            } catch (Exception e) {
                LogProxy.e("release failed", e);
            }
        }
    }

    public String doPrepare(Context context, String str, String str2, String str3, String str4, Map<String, Object> map) {
        IEventHandler iEventHandler;
        if (TextUtils.isEmpty(str4)) {
            LogProxy.e("[doPrepare] failed. can not found eventType");
            return null;
        } else if (context == null) {
            LogProxy.e("[doPrepare] failed. context or wxInstance is null");
            return null;
        } else {
            if (TextUtils.isEmpty(str2)) {
                str2 = generateToken();
            }
            if (this.mBindingCouples == null) {
                this.mBindingCouples = new HashMap();
            }
            Map map2 = this.mBindingCouples.get(str2);
            if (map2 == null || (iEventHandler = (IEventHandler) map2.get(str4)) == null) {
                if (map2 == null) {
                    map2 = new HashMap(4);
                    this.mBindingCouples.put(str2, map2);
                }
                IEventHandler createEventHandler = createEventHandler(context, str, str4);
                if (createEventHandler != null) {
                    createEventHandler.setAnchorInstanceId(str3);
                    createEventHandler.setToken(str2);
                    createEventHandler.setGlobalConfig(map);
                    if (createEventHandler.onCreate(str2, str4)) {
                        createEventHandler.onStart(str2, str4);
                        map2.put(str4, createEventHandler);
                        LogProxy.d("enableBinding success.[token:" + str2 + ",type:" + str4 + Operators.ARRAY_END_STR);
                    } else {
                        LogProxy.e("expression enabled failed. [token:" + str2 + ",type:" + str4 + Operators.ARRAY_END_STR);
                        return null;
                    }
                } else {
                    LogProxy.e("unknown eventType: " + str4);
                    return null;
                }
            } else {
                LogProxy.d("you have already enabled binding,[token:" + str2 + ",type:" + str4 + Operators.ARRAY_END_STR);
                iEventHandler.onStart(str2, str4);
                LogProxy.d("enableBinding success.[token:" + str2 + ",type:" + str4 + Operators.ARRAY_END_STR);
            }
            return str2;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v15, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v3, resolved type: com.alibaba.android.bindingx.core.IEventHandler} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v19, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v16, resolved type: com.alibaba.android.bindingx.core.IEventHandler} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String doBind(java.lang.String r14, java.lang.String r15, java.lang.String r16, java.util.Map<java.lang.String, java.lang.Object> r17, com.alibaba.android.bindingx.core.internal.ExpressionPair r18, java.util.List<java.util.Map<java.lang.String, java.lang.Object>> r19, java.util.Map<java.lang.String, com.alibaba.android.bindingx.core.internal.ExpressionPair> r20, com.alibaba.android.bindingx.core.BindingXCore.JavaScriptCallback r21, android.content.Context r22, java.lang.String r23, java.lang.Object... r24) {
        /*
            r13 = this;
            r7 = r13
            r8 = r14
            r9 = r16
            r10 = r19
            boolean r0 = android.text.TextUtils.isEmpty(r16)
            r1 = 0
            java.lang.String r11 = "]"
            if (r0 != 0) goto L_0x00d4
            if (r10 != 0) goto L_0x0013
            goto L_0x00d4
        L_0x0013:
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, com.alibaba.android.bindingx.core.IEventHandler>> r0 = r7.mBindingCouples
            if (r0 == 0) goto L_0x002e
            boolean r0 = android.text.TextUtils.isEmpty(r14)
            if (r0 != 0) goto L_0x002e
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, com.alibaba.android.bindingx.core.IEventHandler>> r0 = r7.mBindingCouples
            java.lang.Object r0 = r0.get(r14)
            java.util.Map r0 = (java.util.Map) r0
            if (r0 == 0) goto L_0x002e
            java.lang.Object r0 = r0.get(r9)
            r1 = r0
            com.alibaba.android.bindingx.core.IEventHandler r1 = (com.alibaba.android.bindingx.core.IEventHandler) r1
        L_0x002e:
            r12 = r1
            if (r12 != 0) goto L_0x007a
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "binding not enabled,try auto enable it.[sourceRef:"
            r0.append(r1)
            r0.append(r14)
            java.lang.String r1 = ",eventType:"
            r0.append(r1)
            r0.append(r9)
            r0.append(r11)
            java.lang.String r0 = r0.toString()
            com.alibaba.android.bindingx.core.LogProxy.d(r0)
            r0 = r13
            r1 = r22
            r2 = r23
            r3 = r14
            r4 = r15
            r5 = r16
            r6 = r17
            java.lang.String r0 = r0.doPrepare(r1, r2, r3, r4, r5, r6)
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 != 0) goto L_0x0078
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, com.alibaba.android.bindingx.core.IEventHandler>> r1 = r7.mBindingCouples
            if (r1 == 0) goto L_0x0078
            java.lang.Object r1 = r1.get(r0)
            java.util.Map r1 = (java.util.Map) r1
            if (r1 == 0) goto L_0x0078
            java.lang.Object r1 = r1.get(r9)
            r12 = r1
            com.alibaba.android.bindingx.core.IEventHandler r12 = (com.alibaba.android.bindingx.core.IEventHandler) r12
        L_0x0078:
            r6 = r0
            goto L_0x007b
        L_0x007a:
            r6 = r8
        L_0x007b:
            if (r12 == 0) goto L_0x00b7
            r0 = r12
            r1 = r16
            r2 = r17
            r3 = r18
            r4 = r19
            r5 = r21
            r0.onBindExpression(r1, r2, r3, r4, r5)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "createBinding success.[exitExp:"
            r0.append(r1)
            r1 = r18
            r0.append(r1)
            java.lang.String r1 = ",args:"
            r0.append(r1)
            r0.append(r10)
            r0.append(r11)
            java.lang.String r0 = r0.toString()
            com.alibaba.android.bindingx.core.LogProxy.d(r0)
            r0 = r20
            r12.setInterceptors(r0)
            r0 = r24
            r12.setExtensionParams(r0)
            goto L_0x00d3
        L_0x00b7:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "internal error.binding failed for ref:"
            r0.append(r1)
            r0.append(r14)
            java.lang.String r1 = ",type:"
            r0.append(r1)
            r0.append(r9)
            java.lang.String r0 = r0.toString()
            com.alibaba.android.bindingx.core.LogProxy.e(r0)
        L_0x00d3:
            return r6
        L_0x00d4:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "doBind failed,illegal argument.["
            r0.append(r2)
            r0.append(r9)
            java.lang.String r2 = ","
            r0.append(r2)
            r0.append(r10)
            r0.append(r11)
            java.lang.String r0 = r0.toString()
            com.alibaba.android.bindingx.core.LogProxy.e(r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.bindingx.core.BindingXCore.doBind(java.lang.String, java.lang.String, java.lang.String, java.util.Map, com.alibaba.android.bindingx.core.internal.ExpressionPair, java.util.List, java.util.Map, com.alibaba.android.bindingx.core.BindingXCore$JavaScriptCallback, android.content.Context, java.lang.String, java.lang.Object[]):java.lang.String");
    }

    public void onActivityPause() {
        Map<String, Map<String, IEventHandler>> map = this.mBindingCouples;
        if (map != null) {
            try {
                for (Map<String, IEventHandler> values : map.values()) {
                    for (IEventHandler onActivityPause : values.values()) {
                        try {
                            onActivityPause.onActivityPause();
                        } catch (Exception e) {
                            LogProxy.e("execute activity pause failed.", e);
                        }
                    }
                }
            } catch (Exception e2) {
                LogProxy.e("activity pause failed", e2);
            }
        }
    }

    public void onActivityResume() {
        Map<String, Map<String, IEventHandler>> map = this.mBindingCouples;
        if (map != null) {
            try {
                for (Map<String, IEventHandler> values : map.values()) {
                    for (IEventHandler onActivityResume : values.values()) {
                        try {
                            onActivityResume.onActivityResume();
                        } catch (Exception e) {
                            LogProxy.e("execute activity pause failed.", e);
                        }
                    }
                }
            } catch (Exception e2) {
                LogProxy.e("activity pause failed", e2);
            }
        }
    }

    public void registerEventHandler(String str, ObjectCreator<IEventHandler, Context, PlatformManager> objectCreator) {
        if (!TextUtils.isEmpty(str) && objectCreator != null) {
            this.mInternalEventHandlerCreatorMap.put(str, objectCreator);
        }
    }

    public static void registerGlobalEventHandler(String str, ObjectCreator<IEventHandler, Context, PlatformManager> objectCreator) {
        if (!TextUtils.isEmpty(str) && objectCreator != null) {
            sGlobalEventHandlerCreatorMap.put(str, objectCreator);
        }
    }

    public static boolean unregisterGlobalEventHandler(String str) {
        return sGlobalEventHandlerCreatorMap.remove(str) != null;
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    private IEventHandler createEventHandler(Context context, String str, String str2) {
        if (this.mInternalEventHandlerCreatorMap.isEmpty() || this.mPlatformManager == null) {
            return null;
        }
        ObjectCreator objectCreator = this.mInternalEventHandlerCreatorMap.get(str2);
        if (objectCreator == null) {
            objectCreator = sGlobalEventHandlerCreatorMap.get(str2);
        }
        if (objectCreator == null) {
            return null;
        }
        return (IEventHandler) objectCreator.createWith(context, this.mPlatformManager, str);
    }
}
