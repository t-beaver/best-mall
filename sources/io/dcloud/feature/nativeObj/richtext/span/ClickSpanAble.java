package io.dcloud.feature.nativeObj.richtext.span;

import android.view.View;
import io.dcloud.common.DHInterface.IWebview;

public interface ClickSpanAble {
    String getHref();

    String getOnClickEvent();

    boolean hasClickEvent();

    void onClick(View view, IWebview iWebview);
}
