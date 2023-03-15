package com.nostra13.dcloudimageloader.cache.memory;

import android.graphics.Bitmap;
import com.nostra13.dcloudimageloader.utils.L;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class LimitedMemoryCache extends BaseMemoryCache {
    private static final int MAX_NORMAL_CACHE_SIZE = 16777216;
    private static final int MAX_NORMAL_CACHE_SIZE_IN_MB = 16;
    private final AtomicInteger cacheSize;
    private final List<Bitmap> hardCache = Collections.synchronizedList(new LinkedList());
    private final int sizeLimit;

    public LimitedMemoryCache(int i) {
        this.sizeLimit = i;
        this.cacheSize = new AtomicInteger();
        if (i > 16777216) {
            L.w("You set too large memory cache size (more than %1$d Mb)", 16);
        }
    }

    public void clear() {
        this.hardCache.clear();
        this.cacheSize.set(0);
        super.clear();
    }

    /* access modifiers changed from: protected */
    public abstract int getSize(Bitmap bitmap);

    /* access modifiers changed from: protected */
    public int getSizeLimit() {
        return this.sizeLimit;
    }

    public boolean put(String str, Bitmap bitmap) {
        boolean z;
        int size = getSize(bitmap);
        int sizeLimit2 = getSizeLimit();
        int i = this.cacheSize.get();
        if (size < sizeLimit2) {
            while (i + size > sizeLimit2) {
                Bitmap removeNext = removeNext();
                if (this.hardCache.remove(removeNext)) {
                    i = this.cacheSize.addAndGet(-getSize(removeNext));
                }
            }
            this.hardCache.add(bitmap);
            this.cacheSize.addAndGet(size);
            z = true;
        } else {
            z = false;
        }
        super.put(str, bitmap);
        return z;
    }

    public Bitmap remove(String str) {
        Bitmap bitmap = super.get(str);
        if (bitmap != null && this.hardCache.remove(bitmap)) {
            this.cacheSize.addAndGet(-getSize(bitmap));
        }
        return super.remove(str);
    }

    /* access modifiers changed from: protected */
    public abstract Bitmap removeNext();
}
