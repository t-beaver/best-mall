package io.dcloud.feature.gg;

import android.text.TextUtils;
import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.DHInterface.IWaiter;
import io.dcloud.feature.gg.dcloud.AdFeatureImpl;

public class AdFeatureImplMgr implements IReflectAble, IWaiter {
    static final AdFeatureImplMgr mSingleInstance = new AdFeatureImplMgr();
    String mAdType = null;

    public static IWaiter self() {
        return mSingleInstance;
    }

    public void clearAdType() {
        this.mAdType = null;
    }

    public Object doForFeature(String str, Object obj) {
        if (TextUtils.isEmpty(this.mAdType)) {
            this.mAdType = AdSplashUtil.getPlashType();
        }
        Object doForFeature = AdFeatureImpl.doForFeature(str, obj);
        if (str.equals("onWillCloseSplash") || str.equals("onBack")) {
            clearAdType();
        }
        return doForFeature;
    }
}
