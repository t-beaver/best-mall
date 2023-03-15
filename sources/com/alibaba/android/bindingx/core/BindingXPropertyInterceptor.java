package com.alibaba.android.bindingx.core;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import com.alibaba.android.bindingx.core.PlatformManager;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BindingXPropertyInterceptor {
    private static BindingXPropertyInterceptor sInstance = new BindingXPropertyInterceptor();
    /* access modifiers changed from: private */
    public final LinkedList<IPropertyUpdateInterceptor> mPropertyInterceptors = new LinkedList<>();
    private final Handler sUIHandler = new Handler(Looper.getMainLooper());

    public interface IPropertyUpdateInterceptor {
        boolean updateView(View view, String str, Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, Map<String, Object> map, Object... objArr);
    }

    private BindingXPropertyInterceptor() {
    }

    public static BindingXPropertyInterceptor getInstance() {
        return sInstance;
    }

    public void addInterceptor(IPropertyUpdateInterceptor iPropertyUpdateInterceptor) {
        if (iPropertyUpdateInterceptor != null) {
            this.mPropertyInterceptors.add(iPropertyUpdateInterceptor);
        }
    }

    public boolean removeInterceptor(IPropertyUpdateInterceptor iPropertyUpdateInterceptor) {
        if (iPropertyUpdateInterceptor != null) {
            return this.mPropertyInterceptors.remove(iPropertyUpdateInterceptor);
        }
        return false;
    }

    public void clear() {
        this.mPropertyInterceptors.clear();
    }

    public void performIntercept(View view, String str, Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, Map<String, Object> map, Object... objArr) {
        if (!this.mPropertyInterceptors.isEmpty()) {
            final View view2 = view;
            final String str2 = str;
            final Object obj2 = obj;
            final PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator2 = iDeviceResolutionTranslator;
            final Map<String, Object> map2 = map;
            final Object[] objArr2 = objArr;
            this.sUIHandler.post(new WeakRunnable(new Runnable() {
                public void run() {
                    Iterator it = BindingXPropertyInterceptor.this.mPropertyInterceptors.iterator();
                    while (it.hasNext()) {
                        ((IPropertyUpdateInterceptor) it.next()).updateView(view2, str2, obj2, iDeviceResolutionTranslator2, map2, objArr2);
                    }
                }
            }));
        }
    }

    public void clearCallbacks() {
        this.sUIHandler.removeCallbacksAndMessages((Object) null);
    }

    public List<IPropertyUpdateInterceptor> getInterceptors() {
        return Collections.unmodifiableList(this.mPropertyInterceptors);
    }
}
