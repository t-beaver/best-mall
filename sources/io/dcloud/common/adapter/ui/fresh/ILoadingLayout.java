package io.dcloud.common.adapter.ui.fresh;

public interface ILoadingLayout {

    public enum State {
        NONE,
        RESET,
        PULL_TO_REFRESH,
        RELEASE_TO_REFRESH,
        REFRESHING,
        LOADING,
        NO_MORE_DATA
    }

    int getContentSize();

    State getState();

    void onPull(float f);

    void setState(State state);
}
