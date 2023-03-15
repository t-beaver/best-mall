package io.dcloud.sdk.core.v2.draw;

import io.dcloud.sdk.core.v2.base.DCBaseAOLLoadListener;
import java.util.List;

public interface DCDrawAOLLoadListener extends DCBaseAOLLoadListener {
    void onDrawAdLoad(List<DCDrawAOL> list);
}
