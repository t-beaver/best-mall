package com.nostra13.dcloudimageloader.cache.memory.impl;

import android.graphics.Bitmap;
import com.nostra13.dcloudimageloader.cache.memory.MemoryCacheAware;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Objects;

public class LruMemoryCache implements MemoryCacheAware {
    private final LinkedHashMap<String, Bitmap> map;
    private final int maxSize;
    private int size;

    public LruMemoryCache(int i) {
        if (i > 0) {
            this.maxSize = i;
            this.map = new LinkedHashMap<>(0, 0.75f, true);
            return;
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }

    private int sizeOf(String str, Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x006e, code lost:
        throw new java.lang.IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void trimToSize(int r4) {
        /*
            r3 = this;
        L_0x0000:
            monitor-enter(r3)
            int r0 = r3.size     // Catch:{ all -> 0x006f }
            if (r0 < 0) goto L_0x0050
            java.util.LinkedHashMap<java.lang.String, android.graphics.Bitmap> r0 = r3.map     // Catch:{ all -> 0x006f }
            boolean r0 = r0.isEmpty()     // Catch:{ all -> 0x006f }
            if (r0 == 0) goto L_0x0011
            int r0 = r3.size     // Catch:{ all -> 0x006f }
            if (r0 != 0) goto L_0x0050
        L_0x0011:
            int r0 = r3.size     // Catch:{ all -> 0x006f }
            if (r0 <= r4) goto L_0x004e
            java.util.LinkedHashMap<java.lang.String, android.graphics.Bitmap> r0 = r3.map     // Catch:{ all -> 0x006f }
            boolean r0 = r0.isEmpty()     // Catch:{ all -> 0x006f }
            if (r0 == 0) goto L_0x001e
            goto L_0x004e
        L_0x001e:
            java.util.LinkedHashMap<java.lang.String, android.graphics.Bitmap> r0 = r3.map     // Catch:{ all -> 0x006f }
            java.util.Set r0 = r0.entrySet()     // Catch:{ all -> 0x006f }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ all -> 0x006f }
            java.lang.Object r0 = r0.next()     // Catch:{ all -> 0x006f }
            java.util.Map$Entry r0 = (java.util.Map.Entry) r0     // Catch:{ all -> 0x006f }
            if (r0 != 0) goto L_0x0032
            monitor-exit(r3)     // Catch:{ all -> 0x006f }
            goto L_0x004f
        L_0x0032:
            java.lang.Object r1 = r0.getKey()     // Catch:{ all -> 0x006f }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ all -> 0x006f }
            java.lang.Object r0 = r0.getValue()     // Catch:{ all -> 0x006f }
            android.graphics.Bitmap r0 = (android.graphics.Bitmap) r0     // Catch:{ all -> 0x006f }
            java.util.LinkedHashMap<java.lang.String, android.graphics.Bitmap> r2 = r3.map     // Catch:{ all -> 0x006f }
            r2.remove(r1)     // Catch:{ all -> 0x006f }
            int r2 = r3.size     // Catch:{ all -> 0x006f }
            int r0 = r3.sizeOf(r1, r0)     // Catch:{ all -> 0x006f }
            int r2 = r2 - r0
            r3.size = r2     // Catch:{ all -> 0x006f }
            monitor-exit(r3)     // Catch:{ all -> 0x006f }
            goto L_0x0000
        L_0x004e:
            monitor-exit(r3)     // Catch:{ all -> 0x006f }
        L_0x004f:
            return
        L_0x0050:
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException     // Catch:{ all -> 0x006f }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x006f }
            r0.<init>()     // Catch:{ all -> 0x006f }
            java.lang.Class r1 = r3.getClass()     // Catch:{ all -> 0x006f }
            java.lang.String r1 = r1.getName()     // Catch:{ all -> 0x006f }
            r0.append(r1)     // Catch:{ all -> 0x006f }
            java.lang.String r1 = ".sizeOf() is reporting inconsistent results!"
            r0.append(r1)     // Catch:{ all -> 0x006f }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x006f }
            r4.<init>(r0)     // Catch:{ all -> 0x006f }
            throw r4     // Catch:{ all -> 0x006f }
        L_0x006f:
            r4 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x006f }
            goto L_0x0073
        L_0x0072:
            throw r4
        L_0x0073:
            goto L_0x0072
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nostra13.dcloudimageloader.cache.memory.impl.LruMemoryCache.trimToSize(int):void");
    }

    public void clear() {
        trimToSize(-1);
    }

    public Collection<String> keys() {
        HashSet hashSet;
        synchronized (this) {
            hashSet = new HashSet(this.map.keySet());
        }
        return hashSet;
    }

    public final boolean put(String str, Bitmap bitmap) {
        if (str == null || bitmap == null) {
            throw new NullPointerException("key == null || value == null");
        }
        synchronized (this) {
            this.size += sizeOf(str, bitmap);
            Bitmap bitmap2 = (Bitmap) this.map.put(str, bitmap);
            if (bitmap2 != null) {
                this.size -= sizeOf(str, bitmap2);
            }
        }
        trimToSize(this.maxSize);
        return true;
    }

    public final synchronized String toString() {
        return String.format("LruCache[maxSize=%d]", new Object[]{Integer.valueOf(this.maxSize)});
    }

    public final Bitmap get(String str) {
        Bitmap bitmap;
        Objects.requireNonNull(str, "key == null");
        synchronized (this) {
            bitmap = this.map.get(str);
        }
        return bitmap;
    }

    public final Bitmap remove(String str) {
        Bitmap bitmap;
        Objects.requireNonNull(str, "key == null");
        synchronized (this) {
            bitmap = (Bitmap) this.map.remove(str);
            if (bitmap != null) {
                this.size -= sizeOf(str, bitmap);
            }
        }
        return bitmap;
    }
}
