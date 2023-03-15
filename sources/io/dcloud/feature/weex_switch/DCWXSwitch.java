package io.dcloud.feature.weex_switch;

import android.content.Context;
import android.graphics.Color;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.CSSConstants;
import com.taobao.weex.layout.ContentBoxMeasurement;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXViewUtils;
import io.dcloud.feature.weex_switch.SwitchButton;
import java.util.HashMap;

public class DCWXSwitch extends WXComponent<SwitchButton> {
    public DCWXSwitch(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        setContentBoxMeasurement(new ContentBoxMeasurement() {
            public void layoutAfter(float f, float f2) {
            }

            public void layoutBefore() {
            }

            public void measureInternal(float f, float f2, int i, int i2) {
                int realPxByWidth = (int) WXViewUtils.getRealPxByWidth(31.0f, DCWXSwitch.this.getInstance().getInstanceViewPortWidthWithFloat());
                int realPxByWidth2 = (int) WXViewUtils.getRealPxByWidth(51.0f, DCWXSwitch.this.getInstance().getInstanceViewPortWidthWithFloat());
                if (CSSConstants.isUndefined(f2)) {
                    f = (float) realPxByWidth2;
                    f2 = (float) realPxByWidth;
                }
                this.mMeasureWidth = f;
                this.mMeasureHeight = f2;
            }
        });
    }

    /* access modifiers changed from: protected */
    public SwitchButton initComponentHostView(Context context) {
        SwitchButton switchButton = new SwitchButton(context);
        switchButton.setShadowEffect(true);
        switchButton.setEnableEffect(true);
        return switchButton;
    }

    @WXComponentProp(name = "checked")
    public void setChecked(boolean z) {
        ((SwitchButton) getHostView()).setChecked(z);
    }

    @WXComponentProp(name = "disabled")
    public void setDisabled(boolean z) {
        ((SwitchButton) getHostView()).setEnabled(!z);
    }

    @WXComponentProp(name = "color")
    public void setColor(String str) {
        ((SwitchButton) getHostView()).setCheckedColor(Color.parseColor(str));
    }

    public void addEvent(String str) {
        super.addEvent(str);
        if (str != null && str.equals(Constants.Event.CHANGE) && getHostView() != null) {
            ((SwitchButton) getHostView()).setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                public void onCheckedChanged(SwitchButton switchButton, boolean z) {
                    HashMap hashMap = new HashMap();
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put("value", Boolean.valueOf(z));
                    hashMap.put("detail", hashMap2);
                    DCWXSwitch.this.fireEvent(Constants.Event.CHANGE, hashMap);
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void removeEventFromView(String str) {
        super.removeEventFromView(str);
        if (getHostView() != null && Constants.Event.CHANGE.equals(str)) {
            ((SwitchButton) getHostView()).setOnCheckedChangeListener((SwitchButton.OnCheckedChangeListener) null);
        }
    }
}
