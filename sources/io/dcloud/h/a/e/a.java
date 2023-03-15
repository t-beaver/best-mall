package io.dcloud.h.a.e;

import android.view.View;

public class a {
    public static boolean a(View view) {
        return view != null && view.getVisibility() == 0 && view.isShown() && view.getWindowVisibility() == 0;
    }
}
