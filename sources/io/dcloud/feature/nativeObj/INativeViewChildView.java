package io.dcloud.feature.nativeObj;

import android.view.View;
import io.dcloud.common.DHInterface.IReflectAble;

public interface INativeViewChildView extends IReflectAble {
    View obtainMainView();

    void updateLayout();
}
