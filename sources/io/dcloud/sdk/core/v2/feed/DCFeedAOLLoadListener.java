package io.dcloud.sdk.core.v2.feed;

import io.dcloud.sdk.core.v2.base.DCBaseAOLLoadListener;
import java.util.List;

public interface DCFeedAOLLoadListener extends DCBaseAOLLoadListener {
    void onFeedAdLoad(List<DCFeedAOL> list);
}
