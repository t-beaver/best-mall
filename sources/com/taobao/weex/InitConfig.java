package com.taobao.weex;

import com.taobao.weex.adapter.ClassLoaderAdapter;
import com.taobao.weex.adapter.IDrawableLoader;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.adapter.IWXJSExceptionAdapter;
import com.taobao.weex.adapter.IWXJsFileLoaderAdapter;
import com.taobao.weex.adapter.IWXJscProcessManager;
import com.taobao.weex.adapter.IWXSoLoaderAdapter;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.adapter.URIAdapter;
import com.taobao.weex.appfram.storage.IWXStorageAdapter;
import com.taobao.weex.appfram.websocket.IWebSocketAdapterFactory;
import com.taobao.weex.bridge.IDCVueBridgeAdapter;
import com.taobao.weex.performance.IApmGenerator;
import java.util.LinkedList;
import java.util.List;

public class InitConfig {
    /* access modifiers changed from: private */
    public IApmGenerator apmGenerater;
    /* access modifiers changed from: private */
    public ClassLoaderAdapter classLoaderAdapter;
    /* access modifiers changed from: private */
    public IDrawableLoader drawableLoader;
    /* access modifiers changed from: private */
    public String framework;
    /* access modifiers changed from: private */
    public IWXHttpAdapter httpAdapter;
    /* access modifiers changed from: private */
    public IWXImgLoaderAdapter imgAdapter;
    /* access modifiers changed from: private */
    public IWXJsFileLoaderAdapter jsFileLoaderAdapter;
    /* access modifiers changed from: private */
    public IWXJscProcessManager jscProcessManager;
    /* access modifiers changed from: private */
    public IWXJSExceptionAdapter mJSExceptionAdapter;
    /* access modifiers changed from: private */
    public URIAdapter mURIAdapter;
    /* access modifiers changed from: private */
    public IDCVueBridgeAdapter mVueBridgeAdaper;
    /* access modifiers changed from: private */
    public List<String> nativeLibraryList;
    /* access modifiers changed from: private */
    public IWXSoLoaderAdapter soLoader;
    /* access modifiers changed from: private */
    public IWXStorageAdapter storageAdapter;
    /* access modifiers changed from: private */
    public IWXUserTrackAdapter utAdapter;
    /* access modifiers changed from: private */
    public IWebSocketAdapterFactory webSocketAdapterFactory;

    public IWXHttpAdapter getHttpAdapter() {
        return this.httpAdapter;
    }

    public IWXImgLoaderAdapter getImgAdapter() {
        return this.imgAdapter;
    }

    public IDrawableLoader getDrawableLoader() {
        return this.drawableLoader;
    }

    public IWXUserTrackAdapter getUtAdapter() {
        return this.utAdapter;
    }

    public IWXSoLoaderAdapter getIWXSoLoaderAdapter() {
        return this.soLoader;
    }

    public String getFramework() {
        return this.framework;
    }

    public IWXStorageAdapter getStorageAdapter() {
        return this.storageAdapter;
    }

    public URIAdapter getURIAdapter() {
        return this.mURIAdapter;
    }

    public IDCVueBridgeAdapter getVueBridgeAdaper() {
        return this.mVueBridgeAdaper;
    }

    public IWebSocketAdapterFactory getWebSocketAdapterFactory() {
        return this.webSocketAdapterFactory;
    }

    public ClassLoaderAdapter getClassLoaderAdapter() {
        return this.classLoaderAdapter;
    }

    public IApmGenerator getApmGenerater() {
        return this.apmGenerater;
    }

    public IWXJsFileLoaderAdapter getJsFileLoaderAdapter() {
        return this.jsFileLoaderAdapter;
    }

    public InitConfig setClassLoaderAdapter(ClassLoaderAdapter classLoaderAdapter2) {
        this.classLoaderAdapter = classLoaderAdapter2;
        return this;
    }

    public IWXJSExceptionAdapter getJSExceptionAdapter() {
        return this.mJSExceptionAdapter;
    }

    public IWXJscProcessManager getJscProcessManager() {
        return this.jscProcessManager;
    }

    /* access modifiers changed from: package-private */
    public Iterable<String> getNativeLibraryList() {
        if (this.nativeLibraryList == null) {
            this.nativeLibraryList = new LinkedList();
        }
        return this.nativeLibraryList;
    }

    private InitConfig() {
    }

    public static class Builder {
        IApmGenerator apmGenerater;
        ClassLoaderAdapter classLoaderAdapter;
        IDrawableLoader drawableLoader;
        String framework;
        IWXHttpAdapter httpAdapter;
        IWXImgLoaderAdapter imgAdapter;
        private IWXJsFileLoaderAdapter jsFileLoaderAdapter;
        IWXJscProcessManager jscProcessManager;
        IWXJSExceptionAdapter mJSExceptionAdapter;
        URIAdapter mURIAdapter;
        IDCVueBridgeAdapter mVueBridgeAdapter;
        private List<String> nativeLibraryList = new LinkedList();
        IWXSoLoaderAdapter soLoader;
        IWXStorageAdapter storageAdapter;
        IWXUserTrackAdapter utAdapter;
        IWebSocketAdapterFactory webSocketAdapterFactory;

        public IWXJscProcessManager getJscProcessManager() {
            return this.jscProcessManager;
        }

        public Builder setJscProcessManager(IWXJscProcessManager iWXJscProcessManager) {
            this.jscProcessManager = iWXJscProcessManager;
            return this;
        }

        public Builder setHttpAdapter(IWXHttpAdapter iWXHttpAdapter) {
            this.httpAdapter = iWXHttpAdapter;
            return this;
        }

        public Builder setImgAdapter(IWXImgLoaderAdapter iWXImgLoaderAdapter) {
            this.imgAdapter = iWXImgLoaderAdapter;
            return this;
        }

        public Builder setDrawableLoader(IDrawableLoader iDrawableLoader) {
            this.drawableLoader = iDrawableLoader;
            return this;
        }

        public Builder setUtAdapter(IWXUserTrackAdapter iWXUserTrackAdapter) {
            this.utAdapter = iWXUserTrackAdapter;
            return this;
        }

        public Builder setStorageAdapter(IWXStorageAdapter iWXStorageAdapter) {
            this.storageAdapter = iWXStorageAdapter;
            return this;
        }

        public Builder setURIAdapter(URIAdapter uRIAdapter) {
            this.mURIAdapter = uRIAdapter;
            return this;
        }

        public Builder setDCVueBridgeAdapter(IDCVueBridgeAdapter iDCVueBridgeAdapter) {
            this.mVueBridgeAdapter = iDCVueBridgeAdapter;
            return this;
        }

        public Builder setJSExceptionAdapter(IWXJSExceptionAdapter iWXJSExceptionAdapter) {
            this.mJSExceptionAdapter = iWXJSExceptionAdapter;
            return this;
        }

        public Builder setSoLoader(IWXSoLoaderAdapter iWXSoLoaderAdapter) {
            this.soLoader = iWXSoLoaderAdapter;
            return this;
        }

        public Builder setFramework(String str) {
            this.framework = str;
            return this;
        }

        public Builder setWebSocketAdapterFactory(IWebSocketAdapterFactory iWebSocketAdapterFactory) {
            this.webSocketAdapterFactory = iWebSocketAdapterFactory;
            return this;
        }

        public Builder setClassLoaderAdapter(ClassLoaderAdapter classLoaderAdapter2) {
            this.classLoaderAdapter = classLoaderAdapter2;
            return this;
        }

        public Builder setApmGenerater(IApmGenerator iApmGenerator) {
            this.apmGenerater = iApmGenerator;
            return this;
        }

        public Builder setJsFileLoaderAdapter(IWXJsFileLoaderAdapter iWXJsFileLoaderAdapter) {
            this.jsFileLoaderAdapter = iWXJsFileLoaderAdapter;
            return this;
        }

        public Builder addNativeLibrary(String str) {
            this.nativeLibraryList.add(str);
            return this;
        }

        public InitConfig build() {
            InitConfig initConfig = new InitConfig();
            IWXHttpAdapter unused = initConfig.httpAdapter = this.httpAdapter;
            IWXImgLoaderAdapter unused2 = initConfig.imgAdapter = this.imgAdapter;
            IDrawableLoader unused3 = initConfig.drawableLoader = this.drawableLoader;
            IWXUserTrackAdapter unused4 = initConfig.utAdapter = this.utAdapter;
            IWXStorageAdapter unused5 = initConfig.storageAdapter = this.storageAdapter;
            IWXSoLoaderAdapter unused6 = initConfig.soLoader = this.soLoader;
            String unused7 = initConfig.framework = this.framework;
            URIAdapter unused8 = initConfig.mURIAdapter = this.mURIAdapter;
            IDCVueBridgeAdapter unused9 = initConfig.mVueBridgeAdaper = this.mVueBridgeAdapter;
            IWebSocketAdapterFactory unused10 = initConfig.webSocketAdapterFactory = this.webSocketAdapterFactory;
            IWXJSExceptionAdapter unused11 = initConfig.mJSExceptionAdapter = this.mJSExceptionAdapter;
            ClassLoaderAdapter unused12 = initConfig.classLoaderAdapter = this.classLoaderAdapter;
            IApmGenerator unused13 = initConfig.apmGenerater = this.apmGenerater;
            IWXJsFileLoaderAdapter unused14 = initConfig.jsFileLoaderAdapter = this.jsFileLoaderAdapter;
            IWXJscProcessManager unused15 = initConfig.jscProcessManager = this.jscProcessManager;
            List unused16 = initConfig.nativeLibraryList = this.nativeLibraryList;
            return initConfig;
        }
    }
}
