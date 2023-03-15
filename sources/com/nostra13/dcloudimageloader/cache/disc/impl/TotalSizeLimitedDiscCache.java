package com.nostra13.dcloudimageloader.cache.disc.impl;

import com.nostra13.dcloudimageloader.cache.disc.LimitedDiscCache;
import com.nostra13.dcloudimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.dcloudimageloader.core.DefaultConfigurationFactory;
import com.nostra13.dcloudimageloader.utils.L;
import java.io.File;

public class TotalSizeLimitedDiscCache extends LimitedDiscCache {
    private static final int MIN_NORMAL_CACHE_SIZE = 2097152;
    private static final int MIN_NORMAL_CACHE_SIZE_IN_MB = 2;

    public TotalSizeLimitedDiscCache(File file, int i) {
        this(file, DefaultConfigurationFactory.createFileNameGenerator(), i);
    }

    /* access modifiers changed from: protected */
    public int getSize(File file) {
        return (int) file.length();
    }

    public TotalSizeLimitedDiscCache(File file, FileNameGenerator fileNameGenerator, int i) {
        super(file, fileNameGenerator, i);
        if (i < 2097152) {
            L.w("You set too small disc cache size (less than %1$d Mb)", 2);
        }
    }
}
