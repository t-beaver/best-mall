package com.taobao.weex.dom;

import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;
import io.dcloud.feature.uniapp.dom.AbsStyle;
import java.util.Map;

public class WXStyle extends AbsStyle {
    public WXStyle() {
    }

    public WXStyle(Map<String, Object> map) {
        super(map);
    }

    public WXStyle(Map<String, Object> map, boolean z) {
        super(map, z);
    }

    public WXStyle clone() {
        WXStyle wXStyle = new WXStyle();
        wXStyle.mStyles.putAll(this.mStyles);
        if (this.mBindingStyle != null) {
            wXStyle.mBindingStyle = new ArrayMap((SimpleArrayMap) this.mBindingStyle);
        }
        if (this.mPesudoStyleMap != null) {
            wXStyle.mPesudoStyleMap = new ArrayMap();
            for (Map.Entry entry : this.mPesudoStyleMap.entrySet()) {
                ArrayMap arrayMap = new ArrayMap();
                arrayMap.putAll((Map) entry.getValue());
                wXStyle.mPesudoStyleMap.put((String) entry.getKey(), arrayMap);
            }
        }
        if (this.mPesudoResetStyleMap != null) {
            wXStyle.mPesudoResetStyleMap = new ArrayMap();
            wXStyle.mPesudoResetStyleMap.putAll(this.mPesudoResetStyleMap);
        }
        return wXStyle;
    }
}
