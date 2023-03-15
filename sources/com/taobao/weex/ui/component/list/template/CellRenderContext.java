package com.taobao.weex.ui.component.list.template;

import com.taobao.weex.el.parse.ArrayStack;
import java.util.HashMap;
import java.util.Map;

public class CellRenderContext {
    public Map map = new HashMap(8);
    public int position;
    public CellRenderState renderState;
    public ArrayStack stack = new ArrayStack();
    public WXRecyclerTemplateList templateList;

    public CellRenderState getRenderState() {
        if (this.renderState != null) {
            this.renderState = this.templateList.getCellDataManager().getRenderState(this.position);
        }
        return this.renderState;
    }

    public void clear() {
        if (this.stack.getList().size() > 0) {
            this.stack.getList().clear();
        }
        if (this.map.size() > 0) {
            this.map.clear();
        }
        this.renderState = null;
        this.position = 0;
        this.templateList = null;
    }
}
