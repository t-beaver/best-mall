package io.dcloud.feature.weex.adapter;

import android.content.Context;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.ui.view.WXFrameLayout;

public class ScalableView extends WXFrameLayout {
    int mHeight = 0;
    int mWidth = 0;

    public ScalableView(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(final int i, final int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        try {
            if ((getComponent() instanceof ScalableViewComponent) && ((ScalableViewComponent) getComponent()).isScalable()) {
                if (this.mHeight != i2 || this.mWidth != i) {
                    WXBridgeManager.getInstance().post(new Runnable() {
                        public void run() {
                            if (ScalableView.this.getComponent() != null && ScalableView.this.getComponent().getInstance() != null) {
                                WXBridgeManager.getInstance().setStyleHeight(ScalableView.this.getComponent().getInstanceId(), ScalableView.this.getComponent().getRef(), (float) i2, true);
                                WXBridgeManager.getInstance().setStyleWidth(ScalableView.this.getComponent().getInstanceId(), ScalableView.this.getComponent().getRef(), (float) i, true);
                                ScalableView.this.mWidth = i;
                                ScalableView.this.mHeight = i2;
                            }
                        }
                    });
                }
            }
        } catch (Exception unused) {
        }
    }
}
