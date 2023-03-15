package com.dcloud.android.v4.widget;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import org.json.JSONObject;

public interface IRefreshAble {

    public interface OnRefreshListener {
        public static final int STATE_REFRESHING = 3;

        void onRefresh(int i);
    }

    void beginRefresh();

    void endRefresh();

    boolean hasRefreshOperator();

    boolean isRefreshEnable();

    boolean isRefreshing();

    void onInit(ViewGroup viewGroup, View view, OnRefreshListener onRefreshListener);

    void onResize(int i, int i2, float f);

    void onSelfDraw(Canvas canvas);

    boolean onSelfTouchEvent(MotionEvent motionEvent);

    void parseData(JSONObject jSONObject, int i, int i2, float f);

    void setRefreshEnable(boolean z);
}
