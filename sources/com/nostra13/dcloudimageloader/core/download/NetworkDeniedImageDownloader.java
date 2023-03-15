package com.nostra13.dcloudimageloader.core.download;

import com.nostra13.dcloudimageloader.core.download.ImageDownloader;
import java.io.IOException;
import java.io.InputStream;

public class NetworkDeniedImageDownloader implements ImageDownloader {
    private final ImageDownloader wrappedDownloader;

    public NetworkDeniedImageDownloader(ImageDownloader imageDownloader) {
        this.wrappedDownloader = imageDownloader;
    }

    public InputStream getStream(String str, Object obj) throws IOException {
        int ordinal = ImageDownloader.Scheme.ofUri(str).ordinal();
        if (ordinal != 1 && ordinal != 2) {
            return this.wrappedDownloader.getStream(str, obj);
        }
        throw new IllegalStateException();
    }
}
