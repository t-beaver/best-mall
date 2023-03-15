package io.dcloud.sdk.core.v2.draw;

import android.app.Activity;
import android.view.View;
import io.dcloud.h.c.c.e.c.c.a;
import io.dcloud.sdk.core.api.AOLLoader;
import io.dcloud.sdk.core.v2.feed.DCFeedAOL;
import io.dcloud.sdk.core.v2.feed.DCFeedAOLListener;

public class DCDrawAOL extends DCFeedAOL implements AOLLoader.DrawAdInteractionListener {
    public DCDrawAOL(a aVar) {
        super(aVar);
    }

    public View getDrawAdView(Activity activity) {
        return getFeedAdView(activity);
    }

    public void onEnd() {
        DCFeedAOLListener dCFeedAOLListener = this.b;
        if (dCFeedAOLListener instanceof AOLLoader.DrawAdInteractionListener) {
            ((AOLLoader.DrawAdInteractionListener) dCFeedAOLListener).onEnd();
        }
    }

    public void onPause() {
        DCFeedAOLListener dCFeedAOLListener = this.b;
        if (dCFeedAOLListener instanceof AOLLoader.DrawAdInteractionListener) {
            ((AOLLoader.DrawAdInteractionListener) dCFeedAOLListener).onPause();
        }
    }

    public void onResume() {
        DCFeedAOLListener dCFeedAOLListener = this.b;
        if (dCFeedAOLListener instanceof AOLLoader.DrawAdInteractionListener) {
            ((AOLLoader.DrawAdInteractionListener) dCFeedAOLListener).onResume();
        }
    }

    public void onStart() {
        DCFeedAOLListener dCFeedAOLListener = this.b;
        if (dCFeedAOLListener instanceof AOLLoader.DrawAdInteractionListener) {
            ((AOLLoader.DrawAdInteractionListener) dCFeedAOLListener).onStart();
        }
    }

    public void setDrawAdListener(DCDrawAOLListener dCDrawAOLListener) {
        this.b = dCDrawAOLListener;
    }
}
