package io.dcloud.feature.nativeObj;

import android.graphics.Typeface;
import io.dcloud.common.DHInterface.IApp;
import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;

public class NativeTypefaceFactory {
    public static HashMap<String, SoftReference<Typeface>> mCache = new HashMap<>();

    public static void clearCache() {
        HashMap<String, SoftReference<Typeface>> hashMap = mCache;
        if (hashMap != null) {
            hashMap.clear();
        }
    }

    public static Typeface getTypeface(IApp iApp, String str) {
        Typeface typeface;
        try {
            if (mCache.containsKey(str)) {
                SoftReference softReference = mCache.get(str);
                if (softReference != null && softReference.get() != null) {
                    return (Typeface) softReference.get();
                }
                mCache.remove(str);
            }
            File file = new File(str);
            if (iApp.obtainRunningAppMode() != 1 || file.exists()) {
                typeface = Typeface.createFromFile(str);
            } else {
                if (str.startsWith("/")) {
                    str = str.substring(1, str.length());
                }
                typeface = Typeface.createFromAsset(iApp.getActivity().getAssets(), str);
            }
            try {
                mCache.put(str, new SoftReference(typeface));
                return typeface;
            } catch (Exception unused) {
                return typeface;
            }
        } catch (Exception unused2) {
            return null;
        }
    }
}
