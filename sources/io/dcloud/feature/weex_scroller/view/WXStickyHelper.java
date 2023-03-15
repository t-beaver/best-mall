package io.dcloud.feature.weex_scroller.view;

import com.taobao.weex.ui.component.Scrollable;
import com.taobao.weex.ui.component.WXComponent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WXStickyHelper {
    private Scrollable scrollable;

    public WXStickyHelper(Scrollable scrollable2) {
        this.scrollable = scrollable2;
    }

    public void bindStickStyle(WXComponent wXComponent, Map<String, Map<String, WXComponent>> map) {
        Scrollable parentScroller = wXComponent.getParentScroller();
        if (parentScroller != null) {
            Map map2 = map.get(parentScroller.getRef());
            if (map2 == null) {
                map2 = new ConcurrentHashMap();
            }
            if (!map2.containsKey(wXComponent.getRef())) {
                map2.put(wXComponent.getRef(), wXComponent);
                map.put(parentScroller.getRef(), map2);
            }
        }
    }

    public void unbindStickStyle(WXComponent wXComponent, Map<String, Map<String, WXComponent>> map) {
        Map map2;
        Scrollable parentScroller = wXComponent.getParentScroller();
        if (parentScroller != null && (map2 = map.get(parentScroller.getRef())) != null) {
            map2.remove(wXComponent.getRef());
        }
    }
}
