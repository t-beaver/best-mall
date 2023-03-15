package io.dcloud.common.DHInterface;

public interface IPdrModule {
    String execute(String str, Object obj);

    void onDestroy();
}
