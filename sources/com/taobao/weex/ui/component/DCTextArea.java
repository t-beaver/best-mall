package com.taobao.weex.ui.component;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.CSSConstants;
import com.taobao.weex.dom.WXAttr;
import com.taobao.weex.layout.ContentBoxMeasurement;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.view.WXEditText;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import io.dcloud.feature.weex.WeexInstanceMgr;
import java.util.HashMap;

public class DCTextArea extends DCWXInput {
    /* access modifiers changed from: private */
    public WXAttr attr;
    /* access modifiers changed from: private */
    public boolean isAutoHeight = false;
    /* access modifiers changed from: private */
    public boolean isLineChange = false;
    boolean isShowConfirm = true;
    private WXComponent.OnFocusChangeListener mOnFocusChangeListener = new WXComponent.OnFocusChangeListener() {
        int count = 0;

        public void onFocusChange(boolean z) {
            TextView textView = (TextView) DCTextArea.this.getHostView();
            if (textView != null) {
                HashMap hashMap = new HashMap(1);
                HashMap hashMap2 = new HashMap(1);
                hashMap2.put("value", textView.getText().toString());
                if (!z) {
                    hashMap2.put("cursor", Integer.valueOf(textView.getSelectionStart()));
                    hashMap.put("detail", hashMap2);
                    DCTextArea.this.fireEvent(Constants.Event.BLUR, hashMap);
                } else if (DCTextArea.this.keyboardHeight == 0.0f) {
                    fireEventForFocus(textView);
                } else {
                    hashMap2.put("height", Float.valueOf(DCTextArea.this.keyboardHeight));
                    hashMap2.put("value", textView.getText().toString());
                    hashMap.put("detail", hashMap2);
                    DCTextArea.this.fireEvent(Constants.Event.FOCUS, hashMap);
                }
            }
        }

        /* access modifiers changed from: private */
        public void fireEventForFocus(final TextView textView) {
            ((WXEditText) DCTextArea.this.getHostView()).postDelayed(new Runnable() {
                public void run() {
                    if (DCTextArea.this.keyboardHeight == 0.0f) {
                        AnonymousClass2.this.count++;
                        if (AnonymousClass2.this.count > 3) {
                            HashMap hashMap = new HashMap(1);
                            HashMap hashMap2 = new HashMap(1);
                            hashMap2.put("value", textView.getText().toString());
                            hashMap2.put("height", Float.valueOf(DCTextArea.this.keyboardHeight));
                            hashMap.put("detail", hashMap2);
                            DCTextArea.this.fireEvent(Constants.Event.FOCUS, hashMap);
                            return;
                        }
                        AnonymousClass2.this.fireEventForFocus(textView);
                        return;
                    }
                    AnonymousClass2.this.count = 0;
                    HashMap hashMap3 = new HashMap(1);
                    HashMap hashMap4 = new HashMap(1);
                    hashMap4.put("value", textView.getText().toString());
                    hashMap4.put("height", Float.valueOf(DCTextArea.this.keyboardHeight));
                    hashMap3.put("detail", hashMap4);
                    DCTextArea.this.fireEvent(Constants.Event.FOCUS, hashMap3);
                }
            }, 200);
        }
    };

    /* access modifiers changed from: protected */
    public int getVerticalGravity() {
        return 48;
    }

    public DCTextArea(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
        this.attr = basicComponentData.getAttrs();
        setContentBoxMeasurement(new ContentBoxMeasurement() {
            public void layoutAfter(float f, float f2) {
            }

            public void layoutBefore() {
            }

            public void measureInternal(float f, float f2, int i, int i2) {
                if (CSSConstants.isUndefined(f)) {
                    this.mMeasureWidth = WXViewUtils.getRealPxByWidth(300.0f, DCTextArea.this.getInstance().getInstanceViewPortWidthWithFloat());
                }
                if (!CSSConstants.isUndefined(f2)) {
                    return;
                }
                if (!DCTextArea.this.attr.containsKey("autoHeight") || !WXUtils.getBoolean(DCTextArea.this.attr.get("autoHeight"), false).booleanValue()) {
                    this.mMeasureHeight = WXViewUtils.getRealPxByWidth(150.0f, DCTextArea.this.getInstance().getInstanceViewPortWidthWithFloat());
                } else {
                    this.mMeasureHeight = WXViewUtils.getRealPxByWidth(((float) DCTextArea.this.getInstance().getDefaultFontSize()) * 1.4f, DCTextArea.this.getInstance().getInstanceViewPortWidthWithFloat());
                }
            }
        });
    }

    public void addEvent(String str) {
        super.addEvent(str);
        if (str.equals("linechange")) {
            this.isLineChange = true;
        }
    }

    /* access modifiers changed from: protected */
    public void setFocusAndBlur() {
        if (!ismHasFocusChangeListener(this.mOnFocusChangeListener)) {
            addFocusChangeListener(this.mOnFocusChangeListener);
        }
    }

    /* access modifiers changed from: protected */
    public void onHostViewInitialized(WXEditText wXEditText) {
        this.isNeedConfirm = false;
        wXEditText.setAllowDisableMovement(false);
        super.onHostViewInitialized(wXEditText);
        try {
            ConfirmBar.getInstance().createConfirmBar(getContext(), WeexInstanceMgr.self().findWebview(getInstance()).obtainApp());
            ConfirmBar.getInstance().addComponent(this);
        } catch (Exception unused) {
        }
        if (this.attr.containsKey("autoHeight") && WXUtils.getBoolean(this.attr.get("autoHeight"), false).booleanValue()) {
            WXBridgeManager.getInstance().setStyleHeight(getInstanceId(), getRef(), WXViewUtils.getRealPxByWidth(((float) getInstance().getDefaultFontSize()) * 1.4f, getInstance().getInstanceViewPortWidthWithFloat()));
        }
        watchLine();
    }

    private void watchLine() {
        addTextChangedListener(new TextWatcher() {
            int line = 0;

            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (((WXEditText) DCTextArea.this.getHostView()).getLineCount() != this.line) {
                    this.line = ((WXEditText) DCTextArea.this.getHostView()).getLineCount();
                    int extendedPaddingTop = ((WXEditText) DCTextArea.this.getHostView()).getExtendedPaddingTop() + ((WXEditText) DCTextArea.this.getHostView()).getExtendedPaddingBottom();
                    float lineTop = (float) (((WXEditText) DCTextArea.this.getHostView()).getLayout().getLineTop(((WXEditText) DCTextArea.this.getHostView()).getLineCount()) + extendedPaddingTop);
                    if (extendedPaddingTop == 0 && DCTextArea.this.isAutoHeight) {
                        lineTop = WXViewUtils.getRealPxByWidth(((float) DCTextArea.this.getInstance().getDefaultFontSize()) * 1.4f, DCTextArea.this.getInstance().getInstanceViewPortWidthWithFloat()) + ((float) ((WXEditText) DCTextArea.this.getHostView()).getLayout().getLineTop(((WXEditText) DCTextArea.this.getHostView()).getLineCount() - 1));
                    }
                    if (DCTextArea.this.isAutoHeight && lineTop > 0.0f) {
                        WXBridgeManager.getInstance().setStyleHeight(DCTextArea.this.getInstanceId(), DCTextArea.this.getRef(), lineTop);
                        WXBridgeManager.getInstance().notifyLayout(DCTextArea.this.getInstanceId());
                        WXBridgeManager.getInstance().forceLayout(DCTextArea.this.getInstanceId());
                    }
                    if (DCTextArea.this.isLineChange) {
                        HashMap hashMap = new HashMap(3);
                        hashMap.put("lineCount", Integer.valueOf(this.line));
                        hashMap.put("height", Float.valueOf(WXViewUtils.getWebPxByWidth(lineTop, DCTextArea.this.getInstance().getInstanceViewPortWidthWithFloat())));
                        hashMap.put("heightRpx", Float.valueOf(lineTop));
                        HashMap hashMap2 = new HashMap(1);
                        hashMap2.put("detail", hashMap);
                        DCTextArea.this.fireEvent("linechange", hashMap2);
                    }
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void appleStyleAfterCreated(WXEditText wXEditText) {
        super.appleStyleAfterCreated(wXEditText);
        wXEditText.setSingleLine(false);
        wXEditText.setMinLines(1);
        wXEditText.setMaxLines(100);
        wXEditText.setInputType(131073);
    }

    /* access modifiers changed from: protected */
    public void setHostLayoutParams(WXEditText wXEditText, int i, int i2, int i3, int i4, int i5, int i6) {
        super.setHostLayoutParams(wXEditText, i, i2, i3, i4, i5, i6);
    }

    /* access modifiers changed from: protected */
    public boolean setProperty(String str, Object obj) {
        this.isConfirmHold = WXUtils.getBoolean(obj, true).booleanValue();
        return super.setProperty(str, obj);
    }

    public void setType(String str) {
        if (getHostView() != null && str != null && getHostView() != null && !this.mType.equals(str)) {
            ((WXEditText) getHostView()).setInputType(131073);
        }
    }

    @WXComponentProp(name = "autoHeight")
    public void setAutoHeight(boolean z) {
        this.isAutoHeight = z;
    }

    public void setSingleLine(boolean z) {
        ((WXEditText) getHostView()).setSingleLine(false);
    }

    /* access modifiers changed from: protected */
    public float getMeasureHeight() {
        if (this.isAutoHeight) {
            return getMeasuredLineHeight();
        }
        return super.getMeasureHeight();
    }

    @WXComponentProp(name = "showConfirmBar")
    public void setShowConfirmBar(boolean z) {
        this.isShowConfirm = z;
    }

    public void destroy() {
        super.destroy();
        ConfirmBar.getInstance().removeComponent(this);
    }
}
