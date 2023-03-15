package io.dcloud.feature.nativeObj;

import android.text.TextUtils;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.INativeBitmap;
import io.dcloud.common.DHInterface.IWaiter;
import io.dcloud.common.DHInterface.IWebview;
import java.util.HashMap;
import java.util.Map;

public class FeatureImpl implements IFeature, IWaiter {
    private static HashMap<String, NativeBitmapMgr> mNativeBitMapMs = new HashMap<>();
    private String mCurrentAppId;
    AbsMgr mFeatureMgr;

    public static void destroyNativeView(String str, NativeView nativeView) {
        if (mNativeBitMapMs.containsKey(str)) {
            mNativeBitMapMs.get(str).destroyNativeView(nativeView);
        }
    }

    public static INativeBitmap getNativeBitmap(String str, String str2) {
        if (mNativeBitMapMs.containsKey(str)) {
            return mNativeBitMapMs.get(str).getBitmapByUuid(str2);
        }
        return null;
    }

    public void dispose(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (mNativeBitMapMs.containsKey(str)) {
                mNativeBitMapMs.get(str).destroy();
                mNativeBitMapMs.remove(str);
            }
        } else if (str == null) {
            for (Map.Entry next : mNativeBitMapMs.entrySet()) {
                ((NativeBitmapMgr) next.getValue()).destroy();
                mNativeBitMapMs.remove(next.getValue());
            }
            mNativeBitMapMs.clear();
        }
    }

    public Object doForFeature(String str, Object obj) {
        if (str.equals("getNativeBitmap")) {
            String[] strArr = (String[]) obj;
            return getNativeBitmap(strArr[0], strArr[1]);
        }
        String obtainAppId = ((IFrameView) ((Object[]) obj)[0]).obtainApp().obtainAppId();
        if (!mNativeBitMapMs.containsKey(obtainAppId)) {
            mNativeBitMapMs.put(obtainAppId, new NativeBitmapMgr());
        }
        if (mNativeBitMapMs.containsKey(obtainAppId)) {
            return mNativeBitMapMs.get(obtainAppId).doForFeature(str, obj);
        }
        return mNativeBitMapMs.get(this.mCurrentAppId).doForFeature(str, obj);
    }

    public String execute(IWebview iWebview, String str, String[] strArr) {
        String obtainAppId = iWebview.obtainApp().obtainAppId();
        this.mCurrentAppId = obtainAppId;
        if (!mNativeBitMapMs.containsKey(obtainAppId)) {
            mNativeBitMapMs.put(this.mCurrentAppId, new NativeBitmapMgr());
        }
        return mNativeBitMapMs.get(this.mCurrentAppId).execute(iWebview, str, strArr);
    }

    public void init(AbsMgr absMgr, String str) {
        this.mFeatureMgr = absMgr;
        if (mNativeBitMapMs == null) {
            mNativeBitMapMs = new HashMap<>();
        }
    }
}
