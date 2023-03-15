package io.dcloud.feature.nativeObj.richtext.span;

import android.text.TextUtils;
import android.view.View;
import io.dcloud.common.DHInterface.IWebview;

public class AHrefSpan extends FontSpan implements ClickSpanAble {
    String mHref = "";
    String strOnClickEvent;

    public AHrefSpan(float f, int i, int i2, int i3, int i4, String str, String str2) {
        super(f, i, i2, i3, i4);
        this.strOnClickEvent = str;
        this.mHref = str2;
    }

    public String getHref() {
        return this.mHref;
    }

    public String getOnClickEvent() {
        return this.strOnClickEvent;
    }

    public boolean hasClickEvent() {
        return !TextUtils.isEmpty(getOnClickEvent());
    }

    public void onClick(View view, IWebview iWebview) {
        String onClickEvent = getOnClickEvent();
        if (!TextUtils.isEmpty(onClickEvent)) {
            iWebview.executeScript(onClickEvent);
        }
    }
}
