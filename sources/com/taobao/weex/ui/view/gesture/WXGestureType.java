package com.taobao.weex.ui.view.gesture;

import com.taobao.weex.ui.component.list.BasicListComponent;

public interface WXGestureType {

    public static class GestureInfo {
        public static final String DIRECTION = "direction";
        public static final String HISTORICAL_XY = "changedTouches";
        public static final String PAGE_X = "pageX";
        public static final String PAGE_Y = "pageY";
        public static final String POINTER_ID = "identifier";
        public static final String SCREEN_X = "screenX";
        public static final String SCREEN_Y = "screenY";
        public static final String STATE = "state";
    }

    public enum LowLevelGesture implements WXGestureType {
        ACTION_DOWN(AbsoluteConst.EVENTS_WEBVIEW_ONTOUCH_START),
        ACTION_MOVE("touchmove"),
        ACTION_UP("touchend"),
        ACTION_CANCEL("touchcancel");
        
        private String description;

        private LowLevelGesture(String str) {
            this.description = str;
        }

        public String toString() {
            return this.description;
        }
    }

    public enum HighLevelGesture implements WXGestureType {
        SWIPE("swipe"),
        LONG_PRESS(BasicListComponent.DragTriggerType.LONG_PRESS),
        PAN_START("panstart"),
        PAN_MOVE("panmove"),
        PAN_END("panend"),
        HORIZONTALPAN("horizontalpan"),
        VERTICALPAN("verticalpan");
        
        private String description;

        private HighLevelGesture(String str) {
            this.description = str;
        }

        public String toString() {
            return this.description;
        }
    }
}
