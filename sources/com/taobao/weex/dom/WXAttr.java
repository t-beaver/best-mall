package com.taobao.weex.dom;

import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;
import com.taobao.weex.dom.binding.WXStatement;
import io.dcloud.feature.uniapp.dom.AbsAttr;
import java.util.Map;

public class WXAttr extends AbsAttr {
    public WXAttr() {
    }

    public WXAttr(Map<String, Object> map) {
        super(map);
    }

    public WXAttr(Map<String, Object> map, int i) {
        super(map, i);
    }

    public WXAttr clone() {
        WXAttr wXAttr = new WXAttr();
        wXAttr.skipFilterPutAll(getAttr());
        if (getBindingAttrs() != null) {
            wXAttr.setBindingAttrs(new ArrayMap((SimpleArrayMap) getBindingAttrs()));
        }
        if (getStatement() != null) {
            wXAttr.setStatement(new WXStatement(getStatement()));
        }
        return wXAttr;
    }
}
