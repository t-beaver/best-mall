package com.taobao.weex.ui.view.gesture;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.bridge.EventResult;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.WXEvent;
import com.taobao.weex.ui.component.Scrollable;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.view.gesture.WXGestureType;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WXGesture extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {
    private static final int CUR_EVENT = -1;
    public static final String DOWN = "down";
    public static final String END = "end";
    public static final String LEFT = "left";
    public static final String MOVE = "move";
    public static final String RIGHT = "right";
    public static final String START = "start";
    private static final String TAG = "Gesture";
    public static final String UNKNOWN = "unknown";
    public static final String UP = "up";
    private WXComponent component;
    private Point globalEventOffset;
    private Point globalOffset;
    private Rect globalRect;
    private PointF locEventOffset;
    private PointF locLeftTop;
    private GestureDetector mGestureDetector;
    private boolean mIsPreventMoveEvent = false;
    private boolean mIsTouchEventConsumed = false;
    private int mParentOrientation = -1;
    private WXGestureType mPendingPan = null;
    private final List<View.OnTouchListener> mTouchListeners = new LinkedList();
    private long panDownTime = -1;
    private boolean requestDisallowInterceptTouchEvent = false;
    private int shouldBubbleCallRemainTimes = 0;
    private int shouldBubbleInterval = 0;
    private boolean shouldBubbleResult = true;
    private long swipeDownTime = -1;

    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    public WXGesture(WXComponent wXComponent, Context context) {
        this.component = wXComponent;
        this.globalRect = new Rect();
        this.globalOffset = new Point();
        this.globalEventOffset = new Point();
        this.locEventOffset = new PointF();
        this.locLeftTop = new PointF();
        this.mGestureDetector = new GestureDetector(context, this, new GestureHandler());
        Scrollable parentScroller = wXComponent.getParentScroller();
        if (parentScroller != null) {
            this.mParentOrientation = parentScroller.getOrientation();
        }
        this.shouldBubbleResult = WXUtils.getBoolean(wXComponent.getAttrs().get(Constants.Name.SHOULD_STOP_PROPAGATION_INIT_RESULT), true).booleanValue();
        this.shouldBubbleInterval = WXUtils.getNumberInt(wXComponent.getAttrs().get(Constants.Name.SHOULD_STOP_PROPAGATION_INTERVAL), 0);
    }

    private boolean isParentScrollable() {
        Scrollable parentScroller;
        WXComponent wXComponent = this.component;
        if (wXComponent == null || (parentScroller = wXComponent.getParentScroller()) == null || parentScroller.isScrollable()) {
            return true;
        }
        return false;
    }

    private boolean hasSameOrientationWithParent() {
        if (this.mParentOrientation == 0 && this.component.containsGesture(WXGestureType.HighLevelGesture.HORIZONTALPAN)) {
            return true;
        }
        if (this.mParentOrientation != 1 || !this.component.containsGesture(WXGestureType.HighLevelGesture.VERTICALPAN)) {
            return false;
        }
        return true;
    }

    public void setPreventMoveEvent(boolean z) {
        this.mIsPreventMoveEvent = z;
    }

    public boolean isTouchEventConsumedByAdvancedGesture() {
        return this.mIsTouchEventConsumed;
    }

    public static boolean isStopPropagation(String str) {
        return Constants.Event.STOP_PROPAGATION.equals(str) || Constants.Event.STOP_PROPAGATION_RAX.equals(str);
    }

    public static boolean hasStopPropagation(WXComponent wXComponent) {
        WXEvent events = wXComponent.getEvents();
        if (events == null) {
            return false;
        }
        int size = events.size();
        int i = 0;
        while (i < size && i < events.size()) {
            if (isStopPropagation((String) events.get(i))) {
                return true;
            }
            i++;
        }
        return false;
    }

    private boolean shouldBubbleTouchEvent(MotionEvent motionEvent) {
        int i;
        if (!hasStopPropagation(this.component)) {
            return true;
        }
        if (this.shouldBubbleInterval <= 0 || (i = this.shouldBubbleCallRemainTimes) <= 0) {
            Map<String, Object> createFireEventParam = createFireEventParam(motionEvent, -1, (String) null);
            createFireEventParam.put("type", "touch");
            if (motionEvent.getAction() == 0) {
                createFireEventParam.put("action", "start");
            } else if (motionEvent.getAction() == 3 || motionEvent.getAction() == 1) {
                createFireEventParam.put("action", "end");
            } else {
                createFireEventParam.put("action", MOVE);
            }
            WXEvent events = this.component.getEvents();
            String str = Constants.Event.STOP_PROPAGATION;
            if (!events.contains(str)) {
                str = Constants.Event.STOP_PROPAGATION_RAX;
            }
            EventResult fireEventWait = this.component.fireEventWait(str, createFireEventParam);
            if (fireEventWait.isSuccess() && fireEventWait.getResult() != null) {
                this.shouldBubbleResult = !WXUtils.getBoolean(fireEventWait.getResult(), Boolean.valueOf(!this.shouldBubbleResult)).booleanValue();
            }
            this.shouldBubbleCallRemainTimes = this.shouldBubbleInterval;
            return this.shouldBubbleResult;
        }
        this.shouldBubbleCallRemainTimes = i - 1;
        return this.shouldBubbleResult;
    }

    public void addOnTouchListener(View.OnTouchListener onTouchListener) {
        if (onTouchListener != null) {
            this.mTouchListeners.add(onTouchListener);
        }
    }

    public boolean removeTouchListener(View.OnTouchListener onTouchListener) {
        if (onTouchListener != null) {
            return this.mTouchListeners.remove(onTouchListener);
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:43:0x00f9 A[Catch:{ Exception -> 0x013e }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouch(android.view.View r8, android.view.MotionEvent r9) {
        /*
            r7 = this;
            boolean r0 = r7.requestDisallowInterceptTouchEvent
            r1 = 0
            if (r0 == 0) goto L_0x0008
            r7.requestDisallowInterceptTouchEvent = r1
            return r1
        L_0x0008:
            android.view.GestureDetector r0 = r7.mGestureDetector     // Catch:{ Exception -> 0x013e }
            boolean r0 = r0.onTouchEvent(r9)     // Catch:{ Exception -> 0x013e }
            java.util.List<android.view.View$OnTouchListener> r2 = r7.mTouchListeners     // Catch:{ Exception -> 0x013e }
            if (r2 == 0) goto L_0x0030
            boolean r2 = r2.isEmpty()     // Catch:{ Exception -> 0x013e }
            if (r2 != 0) goto L_0x0030
            java.util.List<android.view.View$OnTouchListener> r2 = r7.mTouchListeners     // Catch:{ Exception -> 0x013e }
            java.util.Iterator r2 = r2.iterator()     // Catch:{ Exception -> 0x013e }
        L_0x001e:
            boolean r3 = r2.hasNext()     // Catch:{ Exception -> 0x013e }
            if (r3 == 0) goto L_0x0030
            java.lang.Object r3 = r2.next()     // Catch:{ Exception -> 0x013e }
            android.view.View$OnTouchListener r3 = (android.view.View.OnTouchListener) r3     // Catch:{ Exception -> 0x013e }
            boolean r3 = r3.onTouch(r8, r9)     // Catch:{ Exception -> 0x013e }
            r0 = r0 | r3
            goto L_0x001e
        L_0x0030:
            int r2 = r9.getActionMasked()     // Catch:{ Exception -> 0x013e }
            r3 = 3
            r4 = 1
            if (r2 == 0) goto L_0x00ae
            if (r2 == r4) goto L_0x007f
            r5 = 2
            if (r2 == r5) goto L_0x0077
            if (r2 == r3) goto L_0x0047
            r5 = 5
            if (r2 == r5) goto L_0x00ae
            r5 = 6
            if (r2 == r5) goto L_0x007f
            goto L_0x00f1
        L_0x0047:
            r7.finishDisallowInterceptTouchEvent(r8)     // Catch:{ Exception -> 0x013e }
            com.taobao.weex.ui.view.gesture.WXGestureType$LowLevelGesture r2 = com.taobao.weex.ui.view.gesture.WXGestureType.LowLevelGesture.ACTION_CANCEL     // Catch:{ Exception -> 0x013e }
            boolean r2 = r7.handleMotionEvent(r2, r9)     // Catch:{ Exception -> 0x013e }
            r0 = r0 | r2
            boolean r2 = r7.handlePanMotionEvent(r9)     // Catch:{ Exception -> 0x013e }
            r0 = r0 | r2
            com.taobao.weex.ui.component.WXComponent r2 = r7.component     // Catch:{ Exception -> 0x013e }
            boolean r2 = r2.isPreventGesture()     // Catch:{ Exception -> 0x013e }
            if (r2 == 0) goto L_0x00f1
            com.taobao.weex.WXSDKManager r2 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ Exception -> 0x013e }
            com.taobao.weex.ui.WXRenderManager r2 = r2.getWXRenderManager()     // Catch:{ Exception -> 0x013e }
            com.taobao.weex.ui.component.WXComponent r5 = r7.component     // Catch:{ Exception -> 0x013e }
            java.lang.String r5 = r5.getInstanceId()     // Catch:{ Exception -> 0x013e }
            com.taobao.weex.ui.component.WXComponent r6 = r7.component     // Catch:{ Exception -> 0x013e }
            java.lang.String r6 = r6.getRef()     // Catch:{ Exception -> 0x013e }
            r2.setScrollable(r5, r4, r6)     // Catch:{ Exception -> 0x013e }
            goto L_0x00f1
        L_0x0077:
            com.taobao.weex.ui.view.gesture.WXGestureType$LowLevelGesture r2 = com.taobao.weex.ui.view.gesture.WXGestureType.LowLevelGesture.ACTION_MOVE     // Catch:{ Exception -> 0x013e }
            boolean r2 = r7.handleMotionEvent(r2, r9)     // Catch:{ Exception -> 0x013e }
            r0 = r0 | r2
            goto L_0x00f1
        L_0x007f:
            r7.finishDisallowInterceptTouchEvent(r8)     // Catch:{ Exception -> 0x013e }
            com.taobao.weex.ui.view.gesture.WXGestureType$LowLevelGesture r2 = com.taobao.weex.ui.view.gesture.WXGestureType.LowLevelGesture.ACTION_UP     // Catch:{ Exception -> 0x013e }
            boolean r2 = r7.handleMotionEvent(r2, r9)     // Catch:{ Exception -> 0x013e }
            r0 = r0 | r2
            boolean r2 = r7.handlePanMotionEvent(r9)     // Catch:{ Exception -> 0x013e }
            r0 = r0 | r2
            com.taobao.weex.ui.component.WXComponent r2 = r7.component     // Catch:{ Exception -> 0x013e }
            boolean r2 = r2.isPreventGesture()     // Catch:{ Exception -> 0x013e }
            if (r2 == 0) goto L_0x00f1
            com.taobao.weex.WXSDKManager r2 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ Exception -> 0x013e }
            com.taobao.weex.ui.WXRenderManager r2 = r2.getWXRenderManager()     // Catch:{ Exception -> 0x013e }
            com.taobao.weex.ui.component.WXComponent r5 = r7.component     // Catch:{ Exception -> 0x013e }
            java.lang.String r5 = r5.getInstanceId()     // Catch:{ Exception -> 0x013e }
            com.taobao.weex.ui.component.WXComponent r6 = r7.component     // Catch:{ Exception -> 0x013e }
            java.lang.String r6 = r6.getRef()     // Catch:{ Exception -> 0x013e }
            r2.setScrollable(r5, r4, r6)     // Catch:{ Exception -> 0x013e }
            goto L_0x00f1
        L_0x00ae:
            r7.mIsTouchEventConsumed = r1     // Catch:{ Exception -> 0x013e }
            boolean r2 = r7.hasSameOrientationWithParent()     // Catch:{ Exception -> 0x013e }
            if (r2 == 0) goto L_0x00cb
            boolean r2 = r7.isParentScrollable()     // Catch:{ Exception -> 0x013e }
            if (r2 != 0) goto L_0x00cb
            com.taobao.weex.ui.component.WXComponent r2 = r7.component     // Catch:{ Exception -> 0x013e }
            android.view.View r2 = r2.getRealView()     // Catch:{ Exception -> 0x013e }
            android.view.ViewParent r2 = r2.getParent()     // Catch:{ Exception -> 0x013e }
            if (r2 == 0) goto L_0x00cb
            r2.requestDisallowInterceptTouchEvent(r4)     // Catch:{ Exception -> 0x013e }
        L_0x00cb:
            com.taobao.weex.ui.view.gesture.WXGestureType$LowLevelGesture r2 = com.taobao.weex.ui.view.gesture.WXGestureType.LowLevelGesture.ACTION_DOWN     // Catch:{ Exception -> 0x013e }
            boolean r2 = r7.handleMotionEvent(r2, r9)     // Catch:{ Exception -> 0x013e }
            r0 = r0 | r2
            com.taobao.weex.ui.component.WXComponent r2 = r7.component     // Catch:{ Exception -> 0x013e }
            boolean r2 = r2.isPreventGesture()     // Catch:{ Exception -> 0x013e }
            if (r2 == 0) goto L_0x00f1
            com.taobao.weex.WXSDKManager r2 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ Exception -> 0x013e }
            com.taobao.weex.ui.WXRenderManager r2 = r2.getWXRenderManager()     // Catch:{ Exception -> 0x013e }
            com.taobao.weex.ui.component.WXComponent r5 = r7.component     // Catch:{ Exception -> 0x013e }
            java.lang.String r5 = r5.getInstanceId()     // Catch:{ Exception -> 0x013e }
            com.taobao.weex.ui.component.WXComponent r6 = r7.component     // Catch:{ Exception -> 0x013e }
            java.lang.String r6 = r6.getRef()     // Catch:{ Exception -> 0x013e }
            r2.setScrollable(r5, r1, r6)     // Catch:{ Exception -> 0x013e }
        L_0x00f1:
            com.taobao.weex.ui.component.WXComponent r2 = r7.component     // Catch:{ Exception -> 0x013e }
            boolean r2 = hasStopPropagation(r2)     // Catch:{ Exception -> 0x013e }
            if (r2 == 0) goto L_0x013d
            android.view.ViewParent r8 = r8.getParent()     // Catch:{ Exception -> 0x013e }
            android.view.ViewGroup r8 = (android.view.ViewGroup) r8     // Catch:{ Exception -> 0x013e }
            if (r8 == 0) goto L_0x010a
            boolean r2 = r7.shouldBubbleTouchEvent(r9)     // Catch:{ Exception -> 0x013e }
            r2 = r2 ^ r4
            r8.requestDisallowInterceptTouchEvent(r2)     // Catch:{ Exception -> 0x013e }
            goto L_0x010b
        L_0x010a:
            r2 = 0
        L_0x010b:
            com.taobao.weex.ui.component.WXComponent r8 = r7.component     // Catch:{ Exception -> 0x013e }
            com.taobao.weex.ui.component.WXVContainer r8 = r8.getParent()     // Catch:{ Exception -> 0x013e }
            if (r8 == 0) goto L_0x011c
            com.taobao.weex.ui.component.WXComponent r8 = r7.component     // Catch:{ Exception -> 0x013e }
            com.taobao.weex.ui.component.WXVContainer r8 = r8.getParent()     // Catch:{ Exception -> 0x013e }
            r8.requestDisallowInterceptTouchEvent(r2)     // Catch:{ Exception -> 0x013e }
        L_0x011c:
            boolean r8 = r7.mIsTouchEventConsumed     // Catch:{ Exception -> 0x013e }
            if (r8 == 0) goto L_0x013d
            com.taobao.weex.ui.component.WXComponent r8 = r7.component     // Catch:{ Exception -> 0x013e }
            com.taobao.weex.dom.WXAttr r8 = r8.getAttrs()     // Catch:{ Exception -> 0x013e }
            java.lang.String r2 = "cancelTouchOnConsume"
            java.lang.Object r8 = r8.get(r2)     // Catch:{ Exception -> 0x013e }
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x013e }
            java.lang.Boolean r8 = com.taobao.weex.utils.WXUtils.getBoolean(r8, r2)     // Catch:{ Exception -> 0x013e }
            boolean r8 = r8.booleanValue()     // Catch:{ Exception -> 0x013e }
            if (r8 == 0) goto L_0x013d
            r9.setAction(r3)     // Catch:{ Exception -> 0x013e }
        L_0x013d:
            return r0
        L_0x013e:
            r8 = move-exception
            java.lang.String r9 = "Gesture RunTime Error "
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r9, (java.lang.Throwable) r8)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.view.gesture.WXGesture.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }

    private String getPanEventAction(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            return "start";
        }
        if (action == 1) {
            return "end";
        }
        if (action != 2) {
            return action != 3 ? "unknown" : "end";
        }
        return MOVE;
    }

    private void finishDisallowInterceptTouchEvent(View view) {
        if (view.getParent() != null) {
            view.getParent().requestDisallowInterceptTouchEvent(false);
        }
    }

    private boolean handlePanMotionEvent(MotionEvent motionEvent) {
        String str;
        WXGestureType wXGestureType = this.mPendingPan;
        if (wXGestureType == null) {
            return false;
        }
        if (wXGestureType == WXGestureType.HighLevelGesture.HORIZONTALPAN || this.mPendingPan == WXGestureType.HighLevelGesture.VERTICALPAN) {
            str = getPanEventAction(motionEvent);
        } else {
            str = null;
        }
        if (!this.component.containsGesture(this.mPendingPan)) {
            return false;
        }
        if (this.mIsPreventMoveEvent && MOVE.equals(str)) {
            return true;
        }
        for (Map<String, Object> fireEvent : createMultipleFireEventParam(motionEvent, str)) {
            this.component.fireEvent(this.mPendingPan.toString(), fireEvent);
        }
        if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            this.mPendingPan = null;
        }
        return true;
    }

    private boolean handleMotionEvent(WXGestureType wXGestureType, MotionEvent motionEvent) {
        if (this.component.getHover() != null) {
            this.component.getHover().handleMotionEvent(wXGestureType, motionEvent);
        }
        if (!this.component.containsGesture(wXGestureType)) {
            return false;
        }
        for (Map<String, Object> fireEvent : createMultipleFireEventParam(motionEvent, (String) null)) {
            this.component.fireEvent(wXGestureType.toString(), fireEvent);
        }
        return true;
    }

    private List<Map<String, Object>> createMultipleFireEventParam(MotionEvent motionEvent, String str) {
        ArrayList arrayList = new ArrayList(motionEvent.getHistorySize() + 1);
        arrayList.add(createFireEventParam(motionEvent, -1, str));
        return arrayList;
    }

    private List<Map<String, Object>> getHistoricalEvents(MotionEvent motionEvent) {
        ArrayList arrayList = new ArrayList(motionEvent.getHistorySize());
        if (motionEvent.getActionMasked() == 2) {
            for (int i = 0; i < motionEvent.getHistorySize(); i++) {
                arrayList.add(createFireEventParam(motionEvent, i, (String) null));
            }
        }
        return arrayList;
    }

    private Map<String, Object> createFireEventParam(MotionEvent motionEvent, int i, String str) {
        JSONArray jSONArray = new JSONArray(motionEvent.getPointerCount());
        if (motionEvent.getActionMasked() == 2) {
            for (int i2 = 0; i2 < motionEvent.getPointerCount(); i2++) {
                jSONArray.add(createJSONObject(motionEvent, i, i2));
            }
        } else if (isPointerNumChanged(motionEvent)) {
            jSONArray.add(createJSONObject(motionEvent, -1, motionEvent.getActionIndex()));
        }
        HashMap hashMap = new HashMap();
        hashMap.put(WXGestureType.GestureInfo.HISTORICAL_XY, jSONArray);
        if (str != null) {
            hashMap.put("state", str);
        }
        return hashMap;
    }

    private boolean isPointerNumChanged(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0 || motionEvent.getActionMasked() == 5 || motionEvent.getActionMasked() == 1 || motionEvent.getActionMasked() == 6 || motionEvent.getActionMasked() == 3) {
            return true;
        }
        return false;
    }

    private boolean containsSimplePan() {
        return this.component.containsGesture(WXGestureType.HighLevelGesture.PAN_START) || this.component.containsGesture(WXGestureType.HighLevelGesture.PAN_MOVE) || this.component.containsGesture(WXGestureType.HighLevelGesture.PAN_END);
    }

    private JSONObject createJSONObject(MotionEvent motionEvent, int i, int i2) {
        PointF pointF;
        PointF pointF2;
        if (i == -1) {
            pointF = getEventLocInPageCoordinate(motionEvent, i2);
            pointF2 = getEventLocInScreenCoordinate(motionEvent, i2);
        } else {
            PointF eventLocInPageCoordinate = getEventLocInPageCoordinate(motionEvent, i2, i);
            pointF2 = getEventLocInScreenCoordinate(motionEvent, i2, i);
            pointF = eventLocInPageCoordinate;
        }
        JSONObject createJSONObject = createJSONObject(pointF2, pointF, (float) motionEvent.getPointerId(i2));
        float pressure = motionEvent.getPressure();
        if (pressure > 0.0f && pressure < 1.0f) {
            createJSONObject.put(AbsoluteConst.INSTALL_OPTIONS_FORCE, (Object) Float.valueOf(motionEvent.getPressure()));
        }
        return createJSONObject;
    }

    private JSONObject createJSONObject(PointF pointF, PointF pointF2, float f) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(WXGestureType.GestureInfo.PAGE_X, (Object) Float.valueOf(pointF2.x));
        jSONObject.put(WXGestureType.GestureInfo.PAGE_Y, (Object) Float.valueOf(pointF2.y));
        jSONObject.put(WXGestureType.GestureInfo.SCREEN_X, (Object) Float.valueOf(pointF.x));
        jSONObject.put(WXGestureType.GestureInfo.SCREEN_Y, (Object) Float.valueOf(pointF.y));
        jSONObject.put(WXGestureType.GestureInfo.POINTER_ID, (Object) Float.valueOf(f));
        return jSONObject;
    }

    private PointF getEventLocInScreenCoordinate(MotionEvent motionEvent, int i) {
        return getEventLocInScreenCoordinate(motionEvent, i, -1);
    }

    private PointF getEventLocInScreenCoordinate(MotionEvent motionEvent, int i, int i2) {
        float f;
        float f2;
        if (i2 == -1) {
            f = motionEvent.getX(i);
            f2 = motionEvent.getY(i);
        } else {
            float historicalX = motionEvent.getHistoricalX(i, i2);
            f2 = motionEvent.getHistoricalY(i, i2);
            f = historicalX;
        }
        return getEventLocInScreenCoordinate(f, f2);
    }

    private PointF getEventLocInScreenCoordinate(float f, float f2) {
        this.globalRect.set(0, 0, 0, 0);
        this.globalOffset.set(0, 0);
        this.globalEventOffset.set((int) f, (int) f2);
        this.component.getRealView().getGlobalVisibleRect(this.globalRect, this.globalOffset);
        this.globalEventOffset.offset(this.globalOffset.x, this.globalOffset.y);
        return new PointF(WXViewUtils.getWebPxByWidth((float) this.globalEventOffset.x, this.component.getInstance().getInstanceViewPortWidthWithFloat()), WXViewUtils.getWebPxByWidth((float) this.globalEventOffset.y, this.component.getInstance().getInstanceViewPortWidthWithFloat()));
    }

    private PointF getEventLocInPageCoordinate(MotionEvent motionEvent, int i) {
        return getEventLocInPageCoordinate(motionEvent, i, -1);
    }

    private PointF getEventLocInPageCoordinate(MotionEvent motionEvent, int i, int i2) {
        float f;
        float f2;
        if (i2 == -1) {
            f = motionEvent.getX(i);
            f2 = motionEvent.getY(i);
        } else {
            float historicalX = motionEvent.getHistoricalX(i, i2);
            f2 = motionEvent.getHistoricalY(i, i2);
            f = historicalX;
        }
        return getEventLocInPageCoordinate(f, f2);
    }

    private PointF getEventLocInPageCoordinate(float f, float f2) {
        this.locEventOffset.set(f, f2);
        this.locLeftTop.set(0.0f, 0.0f);
        this.component.computeVisiblePointInViewCoordinate(this.locLeftTop);
        this.locEventOffset.offset(this.locLeftTop.x, this.locLeftTop.y);
        return new PointF(WXViewUtils.getWebPxByWidth(this.locEventOffset.x, this.component.getInstance().getInstanceViewPortWidthWithFloat()), WXViewUtils.getWebPxByWidth(this.locEventOffset.y, this.component.getInstance().getInstanceViewPortWidthWithFloat()));
    }

    private static class GestureHandler extends Handler {
        public GestureHandler() {
            super(Looper.getMainLooper());
        }
    }

    public void onLongPress(MotionEvent motionEvent) {
        if (this.component.containsGesture(WXGestureType.HighLevelGesture.LONG_PRESS)) {
            List<Map<String, Object>> createMultipleFireEventParam = createMultipleFireEventParam(motionEvent, (String) null);
            this.component.getInstance().fireEvent(this.component.getRef(), WXGestureType.HighLevelGesture.LONG_PRESS.toString(), createMultipleFireEventParam.get(createMultipleFireEventParam.size() - 1));
            this.mIsTouchEventConsumed = true;
        }
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        WXGestureType.HighLevelGesture highLevelGesture;
        boolean z;
        boolean z2 = false;
        if (motionEvent == null || motionEvent2 == null) {
            return false;
        }
        if (Math.abs(motionEvent2.getX() - motionEvent.getX()) > Math.abs(motionEvent2.getY() - motionEvent.getY())) {
            highLevelGesture = WXGestureType.HighLevelGesture.HORIZONTALPAN;
        } else {
            highLevelGesture = WXGestureType.HighLevelGesture.VERTICALPAN;
        }
        if (this.mPendingPan == WXGestureType.HighLevelGesture.HORIZONTALPAN || this.mPendingPan == WXGestureType.HighLevelGesture.VERTICALPAN) {
            z = handlePanMotionEvent(motionEvent2);
        } else {
            if (this.component.containsGesture(highLevelGesture)) {
                ViewParent parent = this.component.getRealView().getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                WXGestureType wXGestureType = this.mPendingPan;
                if (wXGestureType != null) {
                    handleMotionEvent(wXGestureType, motionEvent2);
                }
                this.mPendingPan = highLevelGesture;
                this.component.fireEvent(highLevelGesture.toString(), createFireEventParam(motionEvent2, -1, "start"));
            } else if (containsSimplePan()) {
                if (this.panDownTime != motionEvent.getEventTime()) {
                    this.panDownTime = motionEvent.getEventTime();
                    this.mPendingPan = WXGestureType.HighLevelGesture.PAN_END;
                    this.component.fireEvent(WXGestureType.HighLevelGesture.PAN_START.toString(), createFireEventParam(motionEvent, -1, (String) null));
                } else {
                    this.component.fireEvent(WXGestureType.HighLevelGesture.PAN_MOVE.toString(), createFireEventParam(motionEvent2, -1, (String) null));
                }
            } else if (!this.component.containsGesture(WXGestureType.HighLevelGesture.SWIPE) || this.swipeDownTime == motionEvent.getEventTime()) {
                z = false;
            } else {
                this.swipeDownTime = motionEvent.getEventTime();
                List<Map<String, Object>> createMultipleFireEventParam = createMultipleFireEventParam(motionEvent2, (String) null);
                Map map = createMultipleFireEventParam.get(createMultipleFireEventParam.size() - 1);
                if (Math.abs(f) > Math.abs(f2)) {
                    map.put("direction", f > 0.0f ? "left" : "right");
                } else {
                    map.put("direction", f2 > 0.0f ? "up" : "down");
                }
                this.component.getInstance().fireEvent(this.component.getRef(), WXGestureType.HighLevelGesture.SWIPE.toString(), map);
            }
            z = true;
        }
        if (this.mIsTouchEventConsumed || z) {
            z2 = true;
        }
        this.mIsTouchEventConsumed = z2;
        return z;
    }

    public boolean isRequestDisallowInterceptTouchEvent() {
        return this.requestDisallowInterceptTouchEvent;
    }

    public void setRequestDisallowInterceptTouchEvent(boolean z) {
        this.requestDisallowInterceptTouchEvent = z;
    }
}
