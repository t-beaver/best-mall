package com.nostra13.dcloudimageloader.core.download;

import com.nostra13.dcloudimageloader.core.assist.FlushedInputStream;
import com.nostra13.dcloudimageloader.core.download.ImageDownloader;
import java.io.IOException;
import java.io.InputStream;

public class SlowNetworkImageDownloader implements ImageDownloader {
    private final ImageDownloader wrappedDownloader;

    public SlowNetworkImageDownloader(ImageDownloader imageDownloader) {
        this.wrappedDownloader = imageDownloader;
    }

    public InputStream getStream(String str, Object obj) throws IOException {
        InputStream stream = this.wrappedDownloader.getStream(str, obj);
        int ordinal = ImageDownloader.Scheme.ofUri(str).ordinal();
        if (ordinal == 1 || ordinal == 2) {
            return new FlushedInputStream(stream);
        }
        return stream;
    }
}
