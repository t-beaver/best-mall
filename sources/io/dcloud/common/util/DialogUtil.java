package io.dcloud.common.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.dcloud.android.widget.dialog.DCloudAlertDialog;
import io.dcloud.PdrR;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.adapter.util.AndroidResources;

public class DialogUtil {
    public static DCloudAlertDialog initDialogTheme(Activity activity, boolean z) {
        int i;
        int i2;
        boolean isNavigationBarExist = PdrUtil.isNavigationBarExist(activity);
        int i3 = PdrR.STREAMAPP_DELETE_THEME;
        if (AppRuntime.getAppDarkMode(activity)) {
            i2 = DCloudAlertDialog.DARK_THEME;
            i = PdrR.STREAMAPP_DELETE_DARK_THEME;
        } else {
            i = i3;
            i2 = DCloudAlertDialog.LIGHT_THEME;
        }
        if (z) {
            return new DCloudAlertDialog(activity, i2, isNavigationBarExist);
        }
        if (Build.VERSION.SDK_INT < 20) {
            return new DCloudAlertDialog(activity, i, isNavigationBarExist);
        }
        return new DCloudAlertDialog(activity, i2, isNavigationBarExist);
    }

    public static void showAlertDialog(Activity activity, String str, String str2, String str3, final View.OnClickListener onClickListener, final View.OnClickListener onClickListener2, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener, boolean z, int i, int i2, int i3) {
        DialogInterface.OnCancelListener onCancelListener2 = onCancelListener;
        DialogInterface.OnDismissListener onDismissListener2 = onDismissListener;
        if (activity != null) {
            final AlertDialog initDialogTheme = initDialogTheme(activity);
            View inflate = LayoutInflater.from(activity).inflate(PdrR.STREAMAPP_CUSTOM_ALERT_DIALOG_LAYOUT, (ViewGroup) null);
            TextView textView = (TextView) inflate.findViewById(PdrR.STREAMAPP_CUSTOM_ALERT_DIALOG_TITLE);
            Button button = (Button) inflate.findViewById(PdrR.STREAMAPP_CUSTOM_ALERT_DIALOG_SURE);
            Button button2 = (Button) inflate.findViewById(PdrR.STREAMAPP_CUSTOM_ALERT_DIALOG_CANCEL);
            if (!TextUtils.isEmpty(str)) {
                String str4 = str;
                textView.setText(str);
                textView.setGravity(i);
            }
            if (!z) {
                inflate.findViewById(PdrR.STREAMAPP_CUSTOM_ALERT_DIALOG_CUSTOM_LAYOUT).setVisibility(8);
            }
            if (!TextUtils.isEmpty(str2)) {
                String str5 = str2;
                button.setText(str2);
            }
            if (!TextUtils.isEmpty(str3)) {
                String str6 = str3;
                button2.setText(str3);
            }
            View.OnClickListener onClickListener3 = onClickListener;
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    initDialogTheme.dismiss();
                    View.OnClickListener onClickListener = onClickListener;
                    if (onClickListener != null) {
                        onClickListener.onClick(view);
                    }
                }
            });
            View.OnClickListener onClickListener4 = onClickListener2;
            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    initDialogTheme.dismiss();
                    View.OnClickListener onClickListener = onClickListener2;
                    if (onClickListener != null) {
                        onClickListener.onClick(view);
                    }
                }
            });
            if (onCancelListener2 != null) {
                initDialogTheme.setOnCancelListener(onCancelListener);
            }
            if (onDismissListener2 != null) {
                initDialogTheme.setOnDismissListener(onDismissListener);
            }
            initDialogTheme.show();
            initDialogTheme.setContentView(inflate);
            Window window = initDialogTheme.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.x = 0;
            attributes.y = 0;
            window.setGravity(i2);
            window.setLayout(i3, attributes.height);
        }
    }

    public static void showConfirm(Activity activity, String str, String str2, String[] strArr, final ICallBack iCallBack) {
        final DCloudAlertDialog dCloudAlertDialog = new DCloudAlertDialog(activity, AppRuntime.getAppDarkMode(activity) ? DCloudAlertDialog.DARK_THEME : DCloudAlertDialog.LIGHT_THEME, PdrUtil.isNavigationBarExist(activity));
        dCloudAlertDialog.setTitle(str);
        dCloudAlertDialog.setCanceledOnTouchOutside(false);
        dCloudAlertDialog.setMessage(str2);
        AnonymousClass1 r4 = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -2) {
                    dCloudAlertDialog.cancel();
                    dCloudAlertDialog.dismiss();
                } else if (i != -3 && i == -1) {
                    dCloudAlertDialog.dismiss();
                }
                iCallBack.onCallBack(i, (Object) null);
            }
        };
        dCloudAlertDialog.setButton(-1, strArr[0], r4);
        dCloudAlertDialog.setButton(-2, strArr[1], r4);
        dCloudAlertDialog.show();
    }

    public static void showDialog(Activity activity, String str, String str2, String[] strArr) {
        final AlertDialog initDialogTheme = initDialogTheme(activity);
        if (strArr != null && PdrUtil.isEmpty(strArr[0])) {
            strArr[0] = AndroidResources.getString(17039370);
        }
        if (!PdrUtil.isEmpty(str)) {
            initDialogTheme.setTitle(str);
        }
        initDialogTheme.setCanceledOnTouchOutside(true);
        initDialogTheme.setMessage(str2);
        initDialogTheme.setButton(-1, strArr[0], new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                initDialogTheme.dismiss();
            }
        });
        initDialogTheme.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() != 1 || i != 4) {
                    return false;
                }
                initDialogTheme.dismiss();
                return true;
            }
        });
        initDialogTheme.show();
    }

    public static void showLoadAPPDialog(final Context context, String str, final String str2) {
        AlertDialog create = new AlertDialog.Builder(context).setMessage(str).setNegativeButton(context.getString(R.string.dcloud_common_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                NotificationUtil.cancelNotification(context, str2.hashCode());
                context.startActivity(LoadAppUtils.getDataAndTypeIntent(context, str2, "application/vnd.android.package-archive"));
            }
        }).setNeutralButton(context.getString(R.string.dcloud_common_cancel), (DialogInterface.OnClickListener) null).create();
        create.setCanceledOnTouchOutside(false);
        create.show();
    }

    public static AlertDialog initDialogTheme(Activity activity) {
        int i;
        int i2;
        boolean isNavigationBarExist = PdrUtil.isNavigationBarExist(activity);
        int i3 = PdrR.STREAMAPP_DELETE_THEME;
        if (AppRuntime.getAppDarkMode(activity)) {
            i2 = DCloudAlertDialog.DARK_THEME;
            i = PdrR.STREAMAPP_DELETE_DARK_THEME;
        } else {
            i = i3;
            i2 = DCloudAlertDialog.LIGHT_THEME;
        }
        if (Build.VERSION.SDK_INT < 20) {
            return new DCloudAlertDialog(activity, i, isNavigationBarExist);
        }
        return new DCloudAlertDialog(activity, i2, isNavigationBarExist);
    }
}
