package io.dcloud.feature.gallery.imageedit.c.k;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import com.taobao.weex.WXEnvironment;

public class c {
    public static int a(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= 29) {
                Resources resources = context.getResources();
                int identifier = resources.getIdentifier("status_bar_height", "dimen", WXEnvironment.OS);
                if (identifier > 0) {
                    return resources.getDimensionPixelSize(identifier);
                }
                return -1;
            }
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            return context.getResources().getDimensionPixelSize(Integer.parseInt(cls.getField("status_bar_height").get(cls.newInstance()).toString()));
        } catch (Exception unused) {
        }
    }
}
