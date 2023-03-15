package io.dcloud.feature.weex.adapter;

import com.taobao.weex.appfram.websocket.IWebSocketAdapter;
import com.taobao.weex.appfram.websocket.IWebSocketAdapterFactory;

public class DefaultWebSocketAdapterFactory implements IWebSocketAdapterFactory {
    public IWebSocketAdapter createWebSocketAdapter() {
        return new DefaultWebSocketAdapter();
    }
}
