package io.dcloud.feature.nativeObj.photoview;

import io.dcloud.common.DHInterface.IWebview;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class LongClickEventManager {
    private static LongClickEventManager instance;
    private Map<String, OnLongClickListener> clicks = new HashMap();

    public static abstract class OnLongClickListener {
        String callbackIds;
        IWebview pwebview;

        protected OnLongClickListener(IWebview iWebview, String str) {
            this.pwebview = iWebview;
            this.callbackIds = str;
        }

        public String getCallbackIds() {
            return this.callbackIds;
        }

        public IWebview getPwebview() {
            return this.pwebview;
        }

        public abstract void onLongClickListener(JSONObject jSONObject);
    }

    public static LongClickEventManager getInstance() {
        if (instance == null) {
            synchronized (LongClickEventManager.class) {
                if (instance == null) {
                    instance = new LongClickEventManager();
                }
            }
        }
        return instance;
    }

    public void addOnlongClickListener(String str, OnLongClickListener onLongClickListener) {
        this.clicks.put(str, onLongClickListener);
    }

    public void fireEvent(JSONObject jSONObject) {
        for (String str : this.clicks.keySet()) {
            OnLongClickListener onLongClickListener = this.clicks.get(str);
            if (onLongClickListener != null) {
                onLongClickListener.onLongClickListener(jSONObject);
            }
        }
    }

    public void removeOnLongClickListener(String str) {
        this.clicks.remove(str);
    }
}
