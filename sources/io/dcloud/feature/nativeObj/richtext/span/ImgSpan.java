package io.dcloud.feature.nativeObj.richtext.span;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import io.dcloud.common.DHInterface.IWebview;

public class ImgSpan extends ImageSpan implements ClickSpanAble {
    String mHref = "";
    String mOnClickEvent;
    String mSrc = "";

    public ImgSpan(Drawable drawable, String str, int i, String str2, String str3) {
        super(drawable, str, i);
        this.mOnClickEvent = str2;
        this.mHref = str3;
        this.mSrc = str;
    }

    public String getHref() {
        return this.mHref;
    }

    public String getOnClickEvent() {
        return this.mOnClickEvent;
    }

    public String getSrc() {
        return this.mSrc;
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
