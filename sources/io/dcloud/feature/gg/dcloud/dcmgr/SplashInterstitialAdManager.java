package io.dcloud.feature.gg.dcloud.dcmgr;

import android.app.Activity;
import android.os.SystemClock;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.h.c.d.a;
import io.dcloud.sdk.core.entry.SplashConfig;
import io.dcloud.sdk.core.v2.splash.DCSplashAOLLoadListener;
import io.src.dcloud.adapter.DCloudAdapterUtil;

public class SplashInterstitialAdManager {
    private static SplashInterstitialAdManager instance;
    private long loadTime = 0;
    a wrapper;

    public static SplashInterstitialAdManager getInstance() {
        if (instance == null) {
            synchronized (SplashInterstitialAdManager.class) {
                if (instance == null) {
                    SplashInterstitialAdManager splashInterstitialAdManager = new SplashInterstitialAdManager();
                    instance = splashInterstitialAdManager;
                    return splashInterstitialAdManager;
                }
            }
        }
        return instance;
    }

    public void load(Activity activity) {
        this.wrapper = new a(activity);
        this.wrapper.a(new SplashConfig.Builder().width(0).height(0).build(), (DCSplashAOLLoadListener) null, true);
        this.loadTime = SystemClock.elapsedRealtime();
    }

    public void show(Activity activity) {
        IActivityHandler iActivityHandler = DCloudAdapterUtil.getIActivityHandler(activity);
        if (iActivityHandler != null && this.wrapper != null) {
            FrameLayout obtainActivityContentView = iActivityHandler.obtainActivityContentView();
            if (this.wrapper.a(this.loadTime)) {
                this.wrapper.a(activity, "", (ViewGroup) obtainActivityContentView);
            }
        }
    }
}
