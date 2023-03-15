package io.dcloud.sdk.core.util;

public enum AdType {
    AD_FULLSCREEN("full_screen_video"),
    AD_REWARD("rewarded"),
    AD_NONE(""),
    AD_SPLASH("splash"),
    AD_INTERSTITIAL("interstitial"),
    AD_DRAW("draw_flow"),
    AD_CONTENT_PAGE("content_page"),
    AD_TEMPLATE("template");
    
    private String b;

    private AdType(String str) {
        this.b = str;
    }

    public static AdType getAdType(String str) {
        for (AdType adType : values()) {
            if (adType.b.equals(str)) {
                return adType;
            }
        }
        return AD_NONE;
    }

    public String getType() {
        return this.b;
    }
}
