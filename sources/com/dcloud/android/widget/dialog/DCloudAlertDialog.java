package com.dcloud.android.widget.dialog;

import android.app.AlertDialog;
import android.content.Context;

public class DCloudAlertDialog extends AlertDialog {
    public static final int DARK_THEME = -999;
    public static final int LIGHT_THEME = -998;
    boolean hasNavigationBar;

    public DCloudAlertDialog(Context context, boolean z) {
        super(context);
        this.hasNavigationBar = z;
    }

    private static int getTheme(Context context, int i) {
        if (i == -998) {
            return 5;
        }
        if (i == -999) {
            return 4;
        }
        return i;
    }

    public void show() {
        if (!this.hasNavigationBar) {
            getWindow().addFlags(8);
            super.show();
            getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() | 256 | 512 | 2 | 4096);
            getWindow().clearFlags(8);
            return;
        }
        super.show();
    }

    public DCloudAlertDialog(Context context, int i, boolean z) {
        super(context, getTheme(context, i));
        this.hasNavigationBar = z;
    }
}
