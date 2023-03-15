package io.dcloud.feature.internal.splash;

import android.graphics.Bitmap;
import io.dcloud.common.DHInterface.IReflectAble;

public interface ISplash extends IReflectAble {
    void setImageBitmap(Bitmap bitmap);

    void setNameText(String str);
}
