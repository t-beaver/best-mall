package com.taobao.weex.ui.component.list.template;

import com.alibaba.fastjson.JSONAware;

public class PositionRef extends Number implements JSONAware {
    private CellRenderState renderState;

    public PositionRef(CellRenderState cellRenderState) {
        this.renderState = cellRenderState;
    }

    public int intValue() {
        return getPosition();
    }

    public long longValue() {
        return (long) getPosition();
    }

    public float floatValue() {
        return (float) getPosition();
    }

    public double doubleValue() {
        return (double) getPosition();
    }

    private int getPosition() {
        CellRenderState cellRenderState = this.renderState;
        if (cellRenderState == null) {
            return -1;
        }
        return cellRenderState.position;
    }

    public String toString() {
        return String.valueOf(getPosition());
    }

    public String toJSONString() {
        return String.valueOf(getPosition());
    }
}
