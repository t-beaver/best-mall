package io.dcloud.feature.internal.splash;

import android.app.Activity;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

public class a {
    private static HashMap<String, SoftReference<Activity>> a = new HashMap<>();
    static ArrayList<String> b = new ArrayList<>();

    public static void a(String str) {
        b.add(str);
    }

    public static void b(String str) {
        b.remove(str);
    }
}
