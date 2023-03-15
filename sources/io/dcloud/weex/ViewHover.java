package io.dcloud.weex;

import android.os.Handler;
import android.view.MotionEvent;
import com.alibaba.fastjson.JSONObject;
import com.facebook.common.statfs.StatFsHelper;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.view.gesture.WXGestureType;
import java.util.Map;
import java.util.Set;

public class ViewHover {
    public static final String VIEW_HOVER_EVENT = "view_hover_event";
    /* access modifiers changed from: private */
    public WXComponent component;
    private Handler handler;
    private JSONObject hoverClass;
    private int hoverStartTime;
    private int hoverStayTime;
    private boolean hoverStopPropagation;
    /* access modifiers changed from: private */
    public boolean isHover;
    private boolean isReceiveTouch;
    /* access modifiers changed from: private */
    public Map<String, Object> originalStyles;
    public Runnable touchEndRunnable;
    public Runnable touchStartRunnable;

    public ViewHover(WXComponent wXComponent) {
        this(wXComponent, (JSONObject) null);
    }

    public ViewHover(WXComponent wXComponent, JSONObject jSONObject) {
        this.hoverStopPropagation = false;
        this.hoverClass = null;
        this.hoverStartTime = 50;
        this.hoverStayTime = StatFsHelper.DEFAULT_DISK_YELLOW_LEVEL_IN_MB;
        this.isHover = false;
        this.isReceiveTouch = true;
        this.handler = new Handler();
        this.touchStartRunnable = new Runnable() {
            public void run() {
                if (!ViewHover.this.isHover && ViewHover.this.component != null && ViewHover.this.component.getInstance() != null) {
                    ViewHover viewHover = ViewHover.this;
                    Map unused = viewHover.originalStyles = viewHover.component.getStyles().clone();
                    ViewHover.this.component.setHoverClassStatus(true);
                }
            }
        };
        this.touchEndRunnable = new Runnable() {
            public void run() {
                if (ViewHover.this.component != null && ViewHover.this.component.getInstance() != null) {
                    ViewHover.this.component.setHoverClassStatus(false);
                    boolean unused = ViewHover.this.isHover = false;
                }
            }
        };
        update(jSONObject);
        this.component = wXComponent;
    }

    public void update(JSONObject jSONObject) {
        if (jSONObject != null) {
            JSONObject jSONObject2 = this.hoverClass;
            if (jSONObject2 == null) {
                this.hoverClass = jSONObject;
            } else {
                jSONObject2.putAll(jSONObject);
            }
        }
    }

    public boolean isHoverStopPropagation() {
        return this.hoverStopPropagation;
    }

    public void setHoverStopPropagation(boolean z) {
        this.hoverStopPropagation = z;
    }

    public int getHoverStartTime() {
        return this.hoverStartTime;
    }

    public void setHoverStartTime(int i) {
        this.hoverStartTime = i;
    }

    public int getHoverStayTime() {
        return this.hoverStayTime;
    }

    public void setHoverStayTime(int i) {
        this.hoverStayTime = i;
    }

    public void handleMotionEvent(WXGestureType wXGestureType, MotionEvent motionEvent) {
        if (this.hoverClass != null && this.isReceiveTouch) {
            String obj = wXGestureType.toString();
            obj.hashCode();
            char c = 65535;
            switch (obj.hashCode()) {
                case -1578593149:
                    if (obj.equals(AbsoluteConst.EVENTS_WEBVIEW_ONTOUCH_START)) {
                        c = 0;
                        break;
                    }
                    break;
                case -819532484:
                    if (obj.equals("touchend")) {
                        c = 1;
                        break;
                    }
                    break;
                case 2127979129:
                    if (obj.equals("touchcancel")) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    this.handler.removeCallbacks(this.touchEndRunnable);
                    this.handler.removeCallbacks(this.touchStartRunnable);
                    this.handler.postDelayed(this.touchStartRunnable, (long) this.hoverStartTime);
                    return;
                case 1:
                    this.isHover = true;
                    this.handler.removeCallbacks(this.touchEndRunnable);
                    this.handler.postDelayed(this.touchEndRunnable, (long) this.hoverStayTime);
                    return;
                case 2:
                    this.isHover = true;
                    this.handler.removeCallbacks(this.touchEndRunnable);
                    this.handler.postDelayed(this.touchEndRunnable, (long) this.hoverStayTime);
                    return;
                default:
                    return;
            }
        }
    }

    public Map<String, Object> updateStatusAndGetUpdateStyles(boolean z) {
        JSONObject jSONObject = new JSONObject();
        if (z) {
            return this.hoverClass;
        }
        if (this.originalStyles == null) {
            return jSONObject;
        }
        Set<String> keySet = this.hoverClass.keySet();
        JSONObject jSONObject2 = new JSONObject();
        for (String next : keySet) {
            if (this.originalStyles.containsKey(next)) {
                jSONObject2.put(next, this.originalStyles.get(next));
            } else {
                jSONObject2.put(next, "");
            }
        }
        return jSONObject2;
    }

    public void setReceiveTouch(boolean z) {
        this.isReceiveTouch = z;
    }

    public void destroy() {
        this.component = null;
        this.originalStyles = null;
    }
}
