package io.dcloud.feature.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import com.taobao.weex.common.Constants;
import io.dcloud.common.DHInterface.IEventCallback;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.PdrUtil;
import java.util.Collection;
import java.util.HashMap;

public class b implements IEventCallback {
    private HashMap<IWebview, a> a = new HashMap<>();
    Context b;
    private SensorManager c;
    private Sensor d;

    public b(Context context) {
        this.b = context;
        SensorManager sensorManager = (SensorManager) context.getSystemService("sensor");
        this.c = sensorManager;
        this.d = sensorManager.getDefaultSensor(1);
    }

    public String a(IWebview iWebview, String str, String[] strArr) {
        if (str.equals("start")) {
            AdaFrameView adaFrameView = (AdaFrameView) iWebview.obtainFrameView();
            Logger.d("AccelerometerManager.execute start listen frameView=" + adaFrameView);
            adaFrameView.addFrameViewListener(this);
            a(iWebview, strArr[0], strArr[1]);
            return "";
        } else if (!str.equals(Constants.Value.STOP)) {
            return "";
        } else {
            a(iWebview);
            return "";
        }
    }

    public Object onCallBack(String str, Object obj) {
        if ((!PdrUtil.isEquals(str, AbsoluteConst.EVENTS_WINDOW_CLOSE) && !PdrUtil.isEquals(str, AbsoluteConst.EVENTS_CLOSE)) || !(obj instanceof IWebview)) {
            return null;
        }
        IWebview iWebview = (IWebview) obj;
        a(iWebview);
        ((AdaFrameView) iWebview.obtainFrameView()).removeFrameViewListener(this);
        return null;
    }

    private void a(IWebview iWebview, String str, String str2) {
        a aVar = this.a.get(iWebview);
        if (aVar == null) {
            aVar = new a(this, iWebview, str);
            this.a.put(iWebview, aVar);
        }
        int i = 500;
        int parseInt = PdrUtil.parseInt(str2, 500);
        if (parseInt >= 0) {
            i = parseInt;
        }
        this.c.registerListener(aVar, this.d, i * 1000);
    }

    /* access modifiers changed from: package-private */
    public void a(IWebview iWebview) {
        a remove = this.a.remove(iWebview);
        if (remove != null) {
            Logger.d("AccelerometerManager stop pWebViewImpl=" + iWebview);
            this.c.unregisterListener(remove);
        }
    }

    /* access modifiers changed from: package-private */
    public void a(String str) {
        Collection<a> values = this.a.values();
        if (!values.isEmpty()) {
            for (a unregisterListener : values) {
                this.c.unregisterListener(unregisterListener);
            }
        }
        this.a.clear();
    }
}
