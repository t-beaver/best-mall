package io.dcloud.sdk.core.api;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import io.dcloud.sdk.core.DCloudAOLManager;
import io.dcloud.sdk.core.api.ContentPage;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public interface AOLLoader {

    public interface FeedAdInteractionListener {
        void onClicked();

        void onClosed(String str);

        void onRenderFail();

        void onRenderSuccess();

        void onShow();

        void onShowError();
    }

    public interface SplashAdLoadListener {
        void onError(int i, String str);

        void onSplashAdLoad(SplashAOL splashAOL);

        void pushAd(JSONObject jSONObject);

        void redBag(View view, FrameLayout.LayoutParams layoutParams);
    }

    public interface VideoAdInteractionListener {
        void onClick();

        void onClose();

        void onShow();

        void onShowError(int i, String str);

        void onSkip();

        void onVideoPlayEnd();
    }

    public interface AdLoadListener {
        void onError(int i, String str);
    }

    public interface ContentPageLoadListener {
        void onContentPageAdLoad(ContentPage contentPage);

        void onError(int i, String str, JSONArray jSONArray);
    }

    public interface ContentPageVideoListener extends VideoAdInteractionListener {
        void onComplete(ContentPage.ContentPageItem contentPageItem);

        void onError(ContentPage.ContentPageItem contentPageItem);

        void onPause(ContentPage.ContentPageItem contentPageItem);

        void onResume(ContentPage.ContentPageItem contentPageItem);

        void onStart(ContentPage.ContentPageItem contentPageItem);
    }

    public interface DrawAdInteractionListener extends FeedAdInteractionListener {
        void onEnd();

        void onPause();

        void onResume();

        void onStart();
    }

    public interface DrawAdLoadListener extends AdLoadListener {
        void onDrawAdLoad(List<DrawAOL> list);

        void onError(int i, String str);
    }

    public interface FeedAdLoadListener extends AdLoadListener {
        void onError(int i, String str);

        void onFeedAdLoad(List<FeedAOL> list);
    }

    public interface FullScreenVideoAdInteractionListener extends VideoAdInteractionListener {
    }

    public interface FullScreenVideoAdLoadListener {
        void onError(int i, String str);

        void onFullScreenVideoAdLoad(FullScreenVideoAOL fullScreenVideoAOL);
    }

    public interface InterstitialAdListener extends VideoAdInteractionListener {
    }

    public interface InterstitialAdLoadListener {
        void onError(int i, String str);

        void onInterstitialAdLoad(InterstitialAOL interstitialAOL);
    }

    public interface RewardVideoAdInteractionListener extends VideoAdInteractionListener {
        void onReward(JSONObject jSONObject);
    }

    public interface RewardVideoAdLoadListener {
        void onError(int i, String str);

        void onRewardVideoAdLoad(RewardVideoAOL rewardVideoAOL);
    }

    public interface SplashAdInteractionListener extends VideoAdInteractionListener {
    }

    public interface SplashAdListener {
        void onClick();

        void onError(int i, String str);

        void onPlayError(int i, String str);

        void onShow();
    }

    boolean getPersonalAd(Context context);

    void setPersonalAd(Context context, boolean z);

    void setPrivacyConfig(DCloudAOLManager.PrivacyConfig privacyConfig);
}
