package io.dcloud.feature.barcode2.view;

import com.dcloud.zxing2.ResultPoint;
import com.dcloud.zxing2.ResultPointCallback;

public final class ViewfinderResultPointCallback implements ResultPointCallback {
    private final ViewfinderView viewfinderView;

    public ViewfinderResultPointCallback(ViewfinderView viewfinderView2) {
        this.viewfinderView = viewfinderView2;
    }

    public void foundPossibleResultPoint(ResultPoint resultPoint) {
        this.viewfinderView.addPossibleResultPoint(resultPoint);
    }
}
