package io.dcloud.feature.weex.extend;

import android.widget.ImageView;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXImage;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXUtils;
import io.dcloud.feature.uniapp.layout.UniContentBoxMeasurement;
import io.dcloud.feature.weex.adapter.GlideImageAdapter;
import java.util.Map;

public class DCCoverImageComponent extends WXImage {
    /* access modifiers changed from: private */
    public int mBitmapHeight = 0;
    /* access modifiers changed from: private */
    public int mBitmapWidth = 0;

    public DCCoverImageComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        setContentBoxMeasurement(new UniContentBoxMeasurement() {
            public void layoutAfter(float f, float f2) {
            }

            public void layoutBefore() {
            }

            public void measureInternal(float f, float f2, int i, int i2) {
                if (i == 0 || !DCCoverImageComponent.this.getStyles().containsKey("width")) {
                    this.mMeasureWidth = (float) DCCoverImageComponent.this.mBitmapWidth;
                }
                if (i2 == 0) {
                    this.mMeasureHeight = (float) DCCoverImageComponent.this.mBitmapHeight;
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setImage(String str, WXImageStrategy wXImageStrategy) {
        GlideImageAdapter.setImage(str, (ImageView) getHostView(), getImageQuality(), wXImageStrategy);
    }

    public void onImageFinish(boolean z, Map map) {
        super.onImageFinish(z, map);
        this.mBitmapWidth = WXUtils.getInt(map.get("width"));
        this.mBitmapHeight = WXUtils.getInt(map.get("height"));
        if (!getStyles().containsKey("height")) {
            WXBridgeManager.getInstance().post(new Runnable() {
                public final void run() {
                    DCCoverImageComponent.this.lambda$onImageFinish$0$DCCoverImageComponent();
                }
            });
        }
    }

    public /* synthetic */ void lambda$onImageFinish$0$DCCoverImageComponent() {
        WXBridgeManager.getInstance().setStyleWidth(getInstanceId(), getRef(), (float) this.mBitmapWidth);
        WXBridgeManager.getInstance().setStyleHeight(getInstanceId(), getRef(), (float) this.mBitmapHeight);
    }
}
