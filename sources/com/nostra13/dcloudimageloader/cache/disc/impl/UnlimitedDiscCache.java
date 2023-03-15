package com.nostra13.dcloudimageloader.cache.disc.impl;

import com.nostra13.dcloudimageloader.cache.disc.BaseDiscCache;
import com.nostra13.dcloudimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.dcloudimageloader.core.DefaultConfigurationFactory;
import java.io.File;

public class UnlimitedDiscCache extends BaseDiscCache {
    public UnlimitedDiscCache(File file) {
        this(file, DefaultConfigurationFactory.createFileNameGenerator());
    }

    public void put(String str, File file) {
    }

    public UnlimitedDiscCache(File file, FileNameGenerator fileNameGenerator) {
        super(file, fileNameGenerator);
    }
}
