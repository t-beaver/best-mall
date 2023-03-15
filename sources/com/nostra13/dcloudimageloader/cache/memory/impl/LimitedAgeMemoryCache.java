package com.nostra13.dcloudimageloader.cache.memory.impl;

import android.graphics.Bitmap;
import com.nostra13.dcloudimageloader.cache.memory.MemoryCacheAware;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LimitedAgeMemoryCache implements MemoryCacheAware {
    private final MemoryCacheAware cache;
    private final Map<String, Long> loadingDates = Collections.synchronizedMap(new HashMap());
    private final long maxAge;

    public LimitedAgeMemoryCache(MemoryCacheAware memoryCacheAware, long j) {
        this.cache = memoryCacheAware;
        this.maxAge = j * 1000;
    }

    public void clear() {
        this.cache.clear();
        this.loadingDates.clear();
    }

    public Bitmap get(String str) {
        Long l = this.loadingDates.get(str);
        if (l != null && System.currentTimeMillis() - l.longValue() > this.maxAge) {
            this.cache.remove(str);
            this.loadingDates.remove(str);
        }
        return this.cache.get(str);
    }

    public Collection<String> keys() {
        return this.cache.keys();
    }

    public boolean put(String str, Bitmap bitmap) {
        boolean put = this.cache.put(str, bitmap);
        if (put) {
            this.loadingDates.put(str, Long.valueOf(System.currentTimeMillis()));
        }
        return put;
    }

    public Bitmap remove(String str) {
        this.loadingDates.remove(str);
        return this.cache.remove(str);
    }
}
