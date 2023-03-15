package io.dcloud.feature.barcode2.decoding;

import android.graphics.Bitmap;
import android.os.Handler;
import com.dcloud.zxing2.Result;
import io.dcloud.feature.barcode2.view.ViewfinderView;

public interface IBarHandler {
    void autoFocus();

    void drawViewfinder();

    Handler getHandler();

    ViewfinderView getViewfinderView();

    void handleDecode(Result result, Bitmap bitmap);

    boolean isRunning();
}
