package com.nostra13.dcloudimageloader.core.assist;

import android.graphics.Bitmap;
import com.nostra13.dcloudimageloader.cache.memory.MemoryCacheAware;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class MemoryCacheUtil {
    private static final String URI_AND_SIZE_SEPARATOR = "_";
    private static final String WIDTH_AND_HEIGHT_SEPARATOR = "x";

    private MemoryCacheUtil() {
    }

    public static Comparator<String> createFuzzyKeyComparator() {
        return new Comparator<String>() {
            public int compare(String str, String str2) {
                return str.substring(0, str.lastIndexOf("_")).compareTo(str2.substring(0, str2.lastIndexOf("_")));
            }
        };
    }

    public static List<String> findCacheKeysForImageUri(String str, MemoryCacheAware memoryCacheAware) {
        ArrayList arrayList = new ArrayList();
        for (String next : memoryCacheAware.keys()) {
            if (next.startsWith(str)) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    public static List<Bitmap> findCachedBitmapsForImageUri(String str, MemoryCacheAware memoryCacheAware) {
        ArrayList arrayList = new ArrayList();
        for (String next : memoryCacheAware.keys()) {
            if (next.startsWith(str)) {
                arrayList.add(memoryCacheAware.get(next));
            }
        }
        return arrayList;
    }

    public static String generateKey(String str, ImageSize imageSize) {
        return str + "_" + imageSize.getWidth() + "x" + imageSize.getHeight();
    }

    public static void removeFromCache(String str, MemoryCacheAware memoryCacheAware) {
        for (String next : memoryCacheAware.keys()) {
            if (next.startsWith(str)) {
                memoryCacheAware.remove(next);
                return;
            }
        }
    }
}
