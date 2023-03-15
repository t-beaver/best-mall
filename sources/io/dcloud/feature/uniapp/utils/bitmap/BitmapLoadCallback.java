package io.dcloud.feature.uniapp.utils.bitmap;

public interface BitmapLoadCallback<T> {
    void onFailure(String str, Throwable th);

    void onSuccess(String str, T t);
}
