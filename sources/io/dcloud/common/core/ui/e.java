package io.dcloud.common.core.ui;

import android.content.Context;
import android.view.animation.Animation;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IDCloudWebviewClientListener;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IJsInterface;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.ui.AdaWebview;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.Birdge;
import io.dcloud.common.util.DLGeolocation;

class e extends AdaWebview {
    protected e(Context context, AbsMgr absMgr, b bVar) {
        super(context, bVar);
        Logger.d("dhwebview", "DHWebview0");
        bVar.q = this;
        bVar.r = getWebviewParent();
        if (!bVar.obtainFrameOptions().mDisablePlus) {
            addJsInterface("_bridge", (IJsInterface) new Birdge(new h(this)));
        }
        addJsInterface("_dlGeolocation", (IJsInterface) new DLGeolocation(this));
        Logger.d("dhwebview", "DHWebview hashcode=" + bVar.hashCode());
    }

    public IApp obtainApp() {
        if (obtainFrameView() != null) {
            return obtainFrameView().obtainApp();
        }
        return null;
    }

    public void show(Animation animation) {
        IFrameView obtainFrameView = obtainFrameView();
        ((b) obtainFrameView).setVisible(true, true);
        obtainFrameView.obtainWindowMgr().processEvent(IMgr.MgrType.WindowMgr, 1, new Object[]{obtainFrameView, animation});
    }

    protected e(Context context, AbsMgr absMgr, b bVar, IDCloudWebviewClientListener iDCloudWebviewClientListener) {
        super(context, (AdaFrameView) bVar, iDCloudWebviewClientListener);
        Logger.d("dhwebview", "DHWebview0");
        bVar.q = this;
        bVar.r = getWebviewParent();
        if (!bVar.obtainFrameOptions().mDisablePlus) {
            addJsInterface("_bridge", (IJsInterface) new Birdge(new h(this)));
        }
        addJsInterface("_dlGeolocation", (IJsInterface) new DLGeolocation(this));
        Logger.d("dhwebview", "DHWebview hashcode=" + bVar.hashCode());
    }
}
