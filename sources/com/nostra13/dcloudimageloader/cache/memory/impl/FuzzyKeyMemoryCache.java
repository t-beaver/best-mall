package com.nostra13.dcloudimageloader.cache.memory.impl;

import android.graphics.Bitmap;
import com.nostra13.dcloudimageloader.cache.memory.MemoryCacheAware;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class FuzzyKeyMemoryCache implements MemoryCacheAware {
    private final MemoryCacheAware cache;
    private final Comparator<String> keyComparator;

    public FuzzyKeyMemoryCache(MemoryCacheAware memoryCacheAware, Comparator<String> comparator) {
        this.cache = memoryCacheAware;
        this.keyComparator = comparator;
    }

    public void clear() {
        this.cache.clear();
    }

    public Bitmap get(String str) {
        return this.cache.get(str);
    }

    public Collection<String> keys() {
        return this.cache.keys();
    }

    public boolean put(String str, Bitmap bitmap) {
        String str2;
        synchronized (this.cache) {
            Iterator<String> it = this.cache.keys().iterator();
            while (true) {
                if (!it.hasNext()) {
                    str2 = null;
                    break;
                }
                str2 = it.next();
                if (this.keyComparator.compare(str, str2) == 0) {
                    break;
                }
            }
            if (str2 != null) {
                this.cache.remove(str2);
            }
        }
        return this.cache.put(str, bitmap);
    }

    public Bitmap remove(String str) {
        return this.cache.remove(str);
    }
}
