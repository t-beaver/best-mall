package io.dcloud.common.DHInterface;

import io.dcloud.common.util.JSUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import org.json.JSONArray;

public class StandardFeature extends BaseFeature implements IReflectAble {
    private HashMap<String, Method> mInnerClassMethod = null;

    private void arrangeInnerMethod() {
        this.mInnerClassMethod = new HashMap<>(1);
        for (Method method : getClass().getDeclaredMethods()) {
            int modifiers = method.getModifiers();
            if (!Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers) && isStandardFeatureMethod(method.getParameterTypes())) {
                this.mInnerClassMethod.put(method.getName(), method);
            }
        }
    }

    private String executeAction(String str, IWebview iWebview, JSONArray jSONArray) {
        String str2;
        Method method = this.mInnerClassMethod.get(str);
        if (method != null) {
            try {
                Object invoke = method.invoke(this, new Object[]{iWebview, jSONArray});
                if (invoke != null) {
                    return invoke.toString();
                }
                return null;
            } catch (IllegalAccessException e) {
                str2 = JSUtil.wrapJsVar(e.getMessage());
                e.printStackTrace();
                return str2;
            } catch (IllegalArgumentException e2) {
                str2 = JSUtil.wrapJsVar(e2.getMessage());
                e2.printStackTrace();
                return str2;
            } catch (InvocationTargetException e3) {
                str2 = JSUtil.wrapJsVar(e3.getMessage());
                e3.printStackTrace();
                return str2;
            }
        } else {
            return JSUtil.wrapJsVar("not found the " + str + " function");
        }
    }

    private boolean isStandardFeatureMethod(Class[] clsArr) {
        if (clsArr != null) {
            try {
                if (clsArr.length != 2 || !clsArr[0].equals(IWebview.class) || !clsArr[1].equals(JSONArray.class)) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public final String execute(IWebview iWebview, String str, JSONArray jSONArray) {
        return executeAction(str, iWebview, jSONArray);
    }

    public void init(AbsMgr absMgr, String str) {
        super.init(absMgr, str);
        arrangeInnerMethod();
    }
}
