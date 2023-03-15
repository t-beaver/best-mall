package com.alibaba.android.bindingx.core;

import android.view.View;
import java.util.Map;

public class PlatformManager {
    /* access modifiers changed from: private */
    public IDeviceResolutionTranslator mResolutionTranslator;
    /* access modifiers changed from: private */
    public IViewFinder mViewFinder;
    /* access modifiers changed from: private */
    public IViewUpdater mViewUpdater;

    public interface IDeviceResolutionTranslator {
        double nativeToWeb(double d, Object... objArr);

        double webToNative(double d, Object... objArr);
    }

    public interface IViewFinder {
        View findViewBy(String str, Object... objArr);
    }

    public interface IViewUpdater {
        void synchronouslyUpdateViewOnUIThread(View view, String str, Object obj, IDeviceResolutionTranslator iDeviceResolutionTranslator, Map<String, Object> map, Object... objArr);
    }

    private PlatformManager() {
    }

    public IDeviceResolutionTranslator getResolutionTranslator() {
        return this.mResolutionTranslator;
    }

    public IViewFinder getViewFinder() {
        return this.mViewFinder;
    }

    public IViewUpdater getViewUpdater() {
        return this.mViewUpdater;
    }

    public static class Builder {
        private IDeviceResolutionTranslator deviceResolutionTranslator;
        private IViewFinder viewFinder;
        private IViewUpdater viewUpdater;

        public PlatformManager build() {
            PlatformManager platformManager = new PlatformManager();
            IViewFinder unused = platformManager.mViewFinder = this.viewFinder;
            IDeviceResolutionTranslator unused2 = platformManager.mResolutionTranslator = this.deviceResolutionTranslator;
            IViewUpdater unused3 = platformManager.mViewUpdater = this.viewUpdater;
            return platformManager;
        }

        public Builder withDeviceResolutionTranslator(IDeviceResolutionTranslator iDeviceResolutionTranslator) {
            this.deviceResolutionTranslator = iDeviceResolutionTranslator;
            return this;
        }

        public Builder withViewFinder(IViewFinder iViewFinder) {
            this.viewFinder = iViewFinder;
            return this;
        }

        public Builder withViewUpdater(IViewUpdater iViewUpdater) {
            this.viewUpdater = iViewUpdater;
            return this;
        }
    }
}
