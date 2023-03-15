package com.nostra13.dcloudimageloader.cache.disc.impl;

import com.nostra13.dcloudimageloader.cache.disc.LimitedDiscCache;
import com.nostra13.dcloudimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.dcloudimageloader.core.DefaultConfigurationFactory;
import java.io.File;

public class FileCountLimitedDiscCache extends LimitedDiscCache {
    public FileCountLimitedDiscCache(File file, int i) {
        this(file, DefaultConfigurationFactory.createFileNameGenerator(), i);
    }

    /* access modifiers changed from: protected */
    public int getSize(File file) {
        return 1;
    }

    public FileCountLimitedDiscCache(File file, FileNameGenerator fileNameGenerator, int i) {
        super(file, fileNameGenerator, i);
    }
}
