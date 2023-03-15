package com.taobao.weex.ui.component;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.common.Constants;
import com.taobao.weex.layout.ContentBoxMeasurement;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.list.template.TemplateDom;
import com.taobao.weex.ui.view.WXSwitchView;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import java.util.HashMap;

@Component(lazyload = false)
public class WXSwitch extends WXComponent<WXSwitchView> {
    private CompoundButton.OnCheckedChangeListener mListener;

    @Deprecated
    public WXSwitch(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    public WXSwitch(final WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
        setContentBoxMeasurement(new ContentBoxMeasurement() {
            public void layoutAfter(float f, float f2) {
            }

            public void layoutBefore() {
            }

            public void measureInternal(float f, float f2, int i, int i2) {
                int i3;
                this.mMeasureWidth = 0.0f;
                this.mMeasureHeight = 0.0f;
                try {
                    WXSwitchView wXSwitchView = new WXSwitchView(wXSDKInstance.getContext());
                    int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
                    if (Float.isNaN(f)) {
                        i3 = View.MeasureSpec.makeMeasureSpec(0, 0);
                    } else {
                        i3 = View.MeasureSpec.makeMeasureSpec((int) f, Integer.MIN_VALUE);
                    }
                    wXSwitchView.measure(i3, makeMeasureSpec);
                    this.mMeasureWidth = (float) wXSwitchView.getMeasuredWidth();
                    this.mMeasureHeight = (float) wXSwitchView.getMeasuredHeight();
                } catch (RuntimeException e) {
                    WXLogUtils.e(WXLogUtils.getStackTrace(e));
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public WXSwitchView initComponentHostView(Context context) {
        return new WXSwitchView(context);
    }

    public void addEvent(String str) {
        super.addEvent(str);
        if (str != null && str.equals(Constants.Event.CHANGE) && getHostView() != null) {
            if (this.mListener == null) {
                this.mListener = new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                        HashMap hashMap = new HashMap(2);
                        hashMap.put("value", Boolean.valueOf(z));
                        HashMap hashMap2 = new HashMap();
                        HashMap hashMap3 = new HashMap();
                        hashMap3.put(Constants.Name.CHECKED, Boolean.toString(z));
                        hashMap2.put(TemplateDom.KEY_ATTRS, hashMap3);
                        WXSwitch.this.fireEvent(Constants.Event.CHANGE, hashMap, hashMap2);
                    }
                };
            }
            ((WXSwitchView) getHostView()).setOnCheckedChangeListener(this.mListener);
        }
    }

    /* access modifiers changed from: protected */
    public void removeEventFromView(String str) {
        super.removeEventFromView(str);
        if (getHostView() != null && Constants.Event.CHANGE.equals(str)) {
            ((WXSwitchView) getHostView()).setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
        }
    }

    /* access modifiers changed from: protected */
    public boolean setProperty(String str, Object obj) {
        str.hashCode();
        if (!str.equals(Constants.Name.CHECKED)) {
            return super.setProperty(str, obj);
        }
        Boolean bool = WXUtils.getBoolean(obj, (Boolean) null);
        if (bool == null) {
            return true;
        }
        setChecked(bool.booleanValue());
        return true;
    }

    @WXComponentProp(name = "checked")
    public void setChecked(boolean z) {
        ((WXSwitchView) getHostView()).setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
        ((WXSwitchView) getHostView()).setChecked(z);
        ((WXSwitchView) getHostView()).setOnCheckedChangeListener(this.mListener);
    }
}
