package com.nostra13.dcloudimageloader.cache.disc;

import com.nostra13.dcloudimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.dcloudimageloader.core.DefaultConfigurationFactory;
import com.taobao.weex.common.WXConfig;
import java.io.File;

public abstract class BaseDiscCache implements DiscCacheAware {
    private static final String ERROR_ARG_NULL = "\"%s\" argument must be not null";
    protected File cacheDir;
    private FileNameGenerator fileNameGenerator;

    public BaseDiscCache(File file) {
        this(file, DefaultConfigurationFactory.createFileNameGenerator());
    }

    public void clear() {
        File[] listFiles = this.cacheDir.listFiles();
        if (listFiles != null) {
            for (File delete : listFiles) {
                delete.delete();
            }
        }
    }

    public File get(String str) {
        return new File(this.cacheDir, this.fileNameGenerator.generate(str));
    }

    public BaseDiscCache(File file, FileNameGenerator fileNameGenerator2) {
        if (file == null) {
            throw new IllegalArgumentException(String.format(ERROR_ARG_NULL, new Object[]{WXConfig.cacheDir}));
        } else if (fileNameGenerator2 != null) {
            this.cacheDir = file;
            this.fileNameGenerator = fileNameGenerator2;
        } else {
            throw new IllegalArgumentException(String.format(ERROR_ARG_NULL, new Object[]{"fileNameGenerator"}));
        }
    }
}
