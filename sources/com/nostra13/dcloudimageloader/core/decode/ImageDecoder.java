package com.nostra13.dcloudimageloader.core.decode;

import android.graphics.Bitmap;
import java.io.IOException;

public interface ImageDecoder {
    Bitmap decode(ImageDecodingInfo imageDecodingInfo) throws IOException;
}
