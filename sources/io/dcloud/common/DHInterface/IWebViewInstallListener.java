package io.dcloud.common.DHInterface;

public interface IWebViewInstallListener {
    void onDownloadFinish(int i);

    void onDownloadProgress(int i);

    void onInstallFinish(int i);
}
