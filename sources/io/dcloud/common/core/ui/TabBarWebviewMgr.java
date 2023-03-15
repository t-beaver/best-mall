package io.dcloud.common.core.ui;

public class TabBarWebviewMgr {
    private static TabBarWebviewMgr mInstance;
    private TabBarWebview mLaunchTabBar;

    public static TabBarWebviewMgr getInstance() {
        if (mInstance == null) {
            mInstance = new TabBarWebviewMgr();
        }
        return mInstance;
    }

    public TabBarWebview getLaunchTabBar() {
        return this.mLaunchTabBar;
    }

    public void setLancheTabBar(TabBarWebview tabBarWebview) {
        this.mLaunchTabBar = tabBarWebview;
    }
}
