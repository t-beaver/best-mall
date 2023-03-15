package io.dcloud.common.adapter.util;

import android.view.View;
import io.dcloud.common.DHInterface.IFrameView;
import org.json.JSONObject;

public class DragBean {
    public JSONObject dragBindViewOp = null;
    public IFrameView dragBindWebView = null;
    public IFrameView dragCallBackWebView = null;
    public String dragCbId = null;
    public JSONObject dragCurrentViewOp = null;
    public View nativeView = null;
}
