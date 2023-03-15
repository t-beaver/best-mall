package com.nostra13.dcloudimageloader.cache.disc;

import com.nostra13.dcloudimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.dcloudimageloader.core.DefaultConfigurationFactory;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class LimitedDiscCache extends BaseDiscCache {
    private static final int INVALID_SIZE = -1;
    /* access modifiers changed from: private */
    public final AtomicInteger cacheSize;
    /* access modifiers changed from: private */
    public final Map<File, Long> lastUsageDates;
    private final int sizeLimit;

    public LimitedDiscCache(File file, int i) {
        this(file, DefaultConfigurationFactory.createFileNameGenerator(), i);
    }

    private void calculateCacheSizeAndFillUsageMap() {
        new Thread(new Runnable() {
            public void run() {
                File[] listFiles = LimitedDiscCache.this.cacheDir.listFiles();
                if (listFiles != null) {
                    int i = 0;
                    for (File file : listFiles) {
                        i += LimitedDiscCache.this.getSize(file);
                        LimitedDiscCache.this.lastUsageDates.put(file, Long.valueOf(file.lastModified()));
                    }
                    LimitedDiscCache.this.cacheSize.set(i);
                }
            }
        }).start();
    }

    private int removeNext() {
        File file;
        if (this.lastUsageDates.isEmpty()) {
            return -1;
        }
        Set<Map.Entry<File, Long>> entrySet = this.lastUsageDates.entrySet();
        synchronized (this.lastUsageDates) {
            file = null;
            Long l = null;
            for (Map.Entry next : entrySet) {
                if (file == null) {
                    file = (File) next.getKey();
                    l = (Long) next.getValue();
                } else {
                    Long l2 = (Long) next.getValue();
                    if (l2.longValue() < l.longValue()) {
                        file = (File) next.getKey();
                        l = l2;
                    }
                }
            }
        }
        int i = 0;
        if (file != null) {
            if (file.exists()) {
                i = getSize(file);
                if (file.delete()) {
                    this.lastUsageDates.remove(file);
                }
            } else {
                this.lastUsageDates.remove(file);
            }
        }
        return i;
    }

    public void clear() {
        this.lastUsageDates.clear();
        this.cacheSize.set(0);
        super.clear();
    }

    public File get(String str) {
        File file = super.get(str);
        Long valueOf = Long.valueOf(System.currentTimeMillis());
        file.setLastModified(valueOf.longValue());
        this.lastUsageDates.put(file, valueOf);
        return file;
    }

    /* access modifiers changed from: protected */
    public abstract int getSize(File file);

    public void put(String str, File file) {
        int removeNext;
        int size = getSize(file);
        int i = this.cacheSize.get();
        while (i + size > this.sizeLimit && (removeNext = removeNext()) != -1) {
            i = this.cacheSize.addAndGet(-removeNext);
        }
        this.cacheSize.addAndGet(size);
        Long valueOf = Long.valueOf(System.currentTimeMillis());
        file.setLastModified(valueOf.longValue());
        this.lastUsageDates.put(file, valueOf);
    }

    public LimitedDiscCache(File file, FileNameGenerator fileNameGenerator, int i) {
        super(file, fileNameGenerator);
        this.lastUsageDates = Collections.synchronizedMap(new HashMap());
        this.sizeLimit = i;
        this.cacheSize = new AtomicInteger();
        calculateCacheSizeAndFillUsageMap();
    }
}
