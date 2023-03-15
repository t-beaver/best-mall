package com.nostra13.dcloudimageloader.cache.memory;

import android.graphics.Bitmap;
import java.lang.ref.Reference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public abstract class BaseMemoryCache implements MemoryCacheAware {
    private final Map<String, Reference<Bitmap>> softMap = Collections.synchronizedMap(new HashMap());

    public void clear() {
        this.softMap.clear();
    }

    /* access modifiers changed from: protected */
    public abstract Reference<Bitmap> createReference(Bitmap bitmap);

    public Bitmap get(String str) {
        Reference reference = this.softMap.get(str);
        if (reference != null) {
            return (Bitmap) reference.get();
        }
        return null;
    }

    public Collection<String> keys() {
        HashSet hashSet;
        synchronized (this.softMap) {
            hashSet = new HashSet(this.softMap.keySet());
        }
        return hashSet;
    }

    public boolean put(String str, Bitmap bitmap) {
        this.softMap.put(str, createReference(bitmap));
        return true;
    }

    public Bitmap remove(String str) {
        Reference remove = this.softMap.remove(str);
        if (remove == null) {
            return null;
        }
        return (Bitmap) remove.get();
    }
}
