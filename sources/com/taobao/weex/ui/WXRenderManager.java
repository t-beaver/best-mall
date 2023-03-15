package com.taobao.weex.ui;

import android.opengl.GLES10;
import android.text.TextUtils;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXRuntimeException;
import com.taobao.weex.common.WXThread;
import com.taobao.weex.dom.RenderContext;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.ui.action.BasicGraphicAction;
import com.taobao.weex.ui.action.GraphicActionBatchAction;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.utils.WXExceptionUtils;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

public class WXRenderManager {
    private static int mOpenGLRenderLimitValue = 0;
    private static int nativeBatchTimes = 0;
    private static final String sKeyAction = "Action";
    private final int MAX_DROP_FRAME_NATIVE_BATCH = 2000;
    private ArrayList<Map<String, Object>> mBatchActions = new ArrayList<>();
    private String mCurrentBatchInstanceId = null;
    private volatile ConcurrentHashMap<String, RenderContextImpl> mRenderContext = new ConcurrentHashMap<>();
    private WXRenderHandler mWXRenderHandler = new WXRenderHandler();

    public RenderContext getRenderContext(String str) {
        return this.mRenderContext.get(str);
    }

    public WXComponent getWXComponent(String str, String str2) {
        RenderContext renderContext;
        if (str == null || TextUtils.isEmpty(str2) || (renderContext = getRenderContext(str)) == null) {
            return null;
        }
        return renderContext.getComponent(str2);
    }

    public WXComponent getWXComponentById(String str, String str2) {
        RenderContextImpl renderContextImpl;
        if (str == null || TextUtils.isEmpty(str2) || (renderContextImpl = this.mRenderContext.get(str)) == null) {
            return null;
        }
        return renderContextImpl.getComponentById(str2);
    }

    public void setScrollable(String str, boolean z, String str2) {
        if (str != null) {
            this.mRenderContext.get(str).setAllScrollerScrollable(z, str2);
        }
    }

    public WXSDKInstance getWXSDKInstance(String str) {
        RenderContextImpl renderContextImpl = this.mRenderContext.get(str);
        if (renderContextImpl == null) {
            return null;
        }
        return renderContextImpl.getWXSDKInstance();
    }

    public static int getOpenGLRenderLimitValue() {
        if (mOpenGLRenderLimitValue == 0) {
            int i = 0;
            try {
                EGL10 egl10 = (EGL10) EGLContext.getEGL();
                EGLDisplay eglGetDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
                egl10.eglInitialize(eglGetDisplay, new int[2]);
                EGLConfig[] eGLConfigArr = new EGLConfig[1];
                int[] iArr = new int[1];
                egl10.eglChooseConfig(eglGetDisplay, new int[]{12351, 12430, 12329, 0, 12339, 1, 12344}, eGLConfigArr, 1, iArr);
                if (iArr[0] == 0) {
                    i = -1;
                    egl10.eglTerminate(eglGetDisplay);
                } else {
                    EGLConfig eGLConfig = eGLConfigArr[0];
                    EGLSurface eglCreatePbufferSurface = egl10.eglCreatePbufferSurface(eglGetDisplay, eGLConfig, new int[]{12375, 64, 12374, 64, 12344});
                    EGLContext eglCreateContext = egl10.eglCreateContext(eglGetDisplay, eGLConfig, EGL10.EGL_NO_CONTEXT, new int[]{12440, 1, 12344});
                    egl10.eglMakeCurrent(eglGetDisplay, eglCreatePbufferSurface, eglCreatePbufferSurface, eglCreateContext);
                    int[] iArr2 = new int[1];
                    GLES10.glGetIntegerv(3379, iArr2, 0);
                    egl10.eglMakeCurrent(eglGetDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                    egl10.eglDestroySurface(eglGetDisplay, eglCreatePbufferSurface);
                    egl10.eglDestroyContext(eglGetDisplay, eglCreateContext);
                    egl10.eglTerminate(eglGetDisplay);
                    i = iArr2[0];
                }
            } catch (Exception e) {
                WXLogUtils.e(WXLogUtils.getStackTrace(e));
            }
            mOpenGLRenderLimitValue = i;
        }
        return mOpenGLRenderLimitValue;
    }

    public void postOnUiThread(Runnable runnable, long j) {
        this.mWXRenderHandler.postDelayed(WXThread.secure(runnable), j);
    }

    public void postOnUiThread(Runnable runnable, String str) {
        this.mWXRenderHandler.post(str, WXThread.secure(runnable));
    }

    public void postOnUiThread(Runnable runnable) {
        this.mWXRenderHandler.post(WXThread.secure(runnable));
    }

    public void removeTask(Runnable runnable) {
        this.mWXRenderHandler.removeCallbacks(runnable);
    }

    public void removeRenderStatement(String str) {
        if (WXUtils.isUiThread()) {
            RenderContextImpl remove = this.mRenderContext.remove(str);
            if (remove != null) {
                remove.destroy();
            }
            if (str == null) {
                this.mWXRenderHandler.removeCallbacksAndMessages((Object) null);
            } else {
                this.mWXRenderHandler.removeMessages(str.hashCode());
            }
        } else {
            throw new WXRuntimeException("[WXRenderManager] removeRenderStatement can only be called in main thread");
        }
    }

    private void postAllStashedGraphicAction(String str, BasicGraphicAction basicGraphicAction) {
        RenderContextImpl renderContextImpl = this.mRenderContext.get(str);
        ArrayList arrayList = renderContextImpl != null ? new ArrayList(this.mBatchActions) : null;
        this.mBatchActions.clear();
        this.mCurrentBatchInstanceId = null;
        nativeBatchTimes = 0;
        if (renderContextImpl != null) {
            ArrayList arrayList2 = new ArrayList(arrayList.size());
            for (int i = 0; i < arrayList.size(); i++) {
                Object obj = ((Map) arrayList.get(i)).get(sKeyAction);
                if (obj instanceof BasicGraphicAction) {
                    BasicGraphicAction basicGraphicAction2 = (BasicGraphicAction) obj;
                    if (!(basicGraphicAction2.mActionType == 1 || basicGraphicAction2.mActionType == 2)) {
                        arrayList2.add(basicGraphicAction2);
                    }
                }
            }
            postGraphicAction(str, new GraphicActionBatchAction(basicGraphicAction.getWXSDKIntance(), basicGraphicAction.getRef(), arrayList2));
        }
    }

    public void postGraphicAction(String str, BasicGraphicAction basicGraphicAction) {
        if (this.mRenderContext.get(str) != null) {
            String str2 = this.mCurrentBatchInstanceId;
            if (str2 != null && str != null && !str2.equals(str) && this.mBatchActions.size() > 0) {
                ArrayList<Map<String, Object>> arrayList = this.mBatchActions;
                Object obj = arrayList.get(arrayList.size() - 1).get(sKeyAction);
                if (obj instanceof BasicGraphicAction) {
                    postAllStashedGraphicAction(this.mCurrentBatchInstanceId, (BasicGraphicAction) obj);
                }
            }
            if (basicGraphicAction.mActionType == 2) {
                postAllStashedGraphicAction(str, basicGraphicAction);
                return;
            }
            if (basicGraphicAction.mActionType == 1 || this.mBatchActions.size() > 0) {
                int i = nativeBatchTimes + 1;
                nativeBatchTimes = i;
                if (i > 2000) {
                    postAllStashedGraphicAction(str, basicGraphicAction);
                } else {
                    HashMap hashMap = new HashMap(1);
                    basicGraphicAction.mIsRunByBatch = true;
                    hashMap.put(sKeyAction, basicGraphicAction);
                    this.mBatchActions.add(hashMap);
                    this.mCurrentBatchInstanceId = str;
                    return;
                }
            }
            this.mWXRenderHandler.post(str, basicGraphicAction);
        }
    }

    public void registerInstance(WXSDKInstance wXSDKInstance) {
        if (wXSDKInstance.getInstanceId() == null) {
            WXErrorCode wXErrorCode = WXErrorCode.WX_RENDER_ERR_INSTANCE_ID_NULL;
            WXExceptionUtils.commitCriticalExceptionRT((String) null, wXErrorCode, "registerInstance", WXErrorCode.WX_RENDER_ERR_INSTANCE_ID_NULL.getErrorMsg() + "instanceId is null", (Map<String, String>) null);
            return;
        }
        this.mRenderContext.put(wXSDKInstance.getInstanceId(), new RenderContextImpl(wXSDKInstance));
    }

    public List<WXSDKInstance> getAllInstances() {
        if (this.mRenderContext == null || this.mRenderContext.isEmpty()) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<String, RenderContextImpl> value : this.mRenderContext.entrySet()) {
            RenderContextImpl renderContextImpl = (RenderContextImpl) value.getValue();
            if (renderContextImpl != null) {
                arrayList.add(renderContextImpl.getWXSDKInstance());
            }
        }
        return arrayList;
    }

    public void registerComponent(String str, String str2, WXComponent wXComponent) {
        RenderContextImpl renderContextImpl = this.mRenderContext.get(str);
        if (renderContextImpl != null) {
            renderContextImpl.registerComponent(str2, wXComponent);
            if (renderContextImpl.getInstance() != null) {
                renderContextImpl.getInstance().getApmForInstance().updateMaxStats(WXInstanceApm.KEY_PAGE_STATS_MAX_COMPONENT_NUM, (double) renderContextImpl.getComponentCount());
            }
        }
    }

    public WXComponent unregisterComponent(String str, String str2) {
        RenderContextImpl renderContextImpl = this.mRenderContext.get(str);
        if (renderContextImpl == null) {
            return null;
        }
        if (renderContextImpl.getInstance() != null) {
            renderContextImpl.getInstance().getApmForInstance().updateMaxStats(WXInstanceApm.KEY_PAGE_STATS_MAX_COMPONENT_NUM, (double) renderContextImpl.getComponentCount());
        }
        return renderContextImpl.unregisterComponent(str2);
    }
}
