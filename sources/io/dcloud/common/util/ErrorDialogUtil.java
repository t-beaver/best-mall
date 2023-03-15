package io.dcloud.common.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Process;
import android.view.KeyEvent;
import com.dcloud.android.widget.dialog.DCloudAlertDialog;
import io.dcloud.PdrR;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IWebview;
import java.util.ArrayList;

public class ErrorDialogUtil {
    /* access modifiers changed from: private */
    public static ArrayList<String> list = new ArrayList<>();

    public static void checkAppKeyErrorTips(Activity activity) {
        String string = activity.getString(R.string.dcloud_offline_fail_tips);
        DCloudAlertDialog dCloudAlertDialog = new DCloudAlertDialog(activity, AppRuntime.getAppDarkMode(activity) ? DCloudAlertDialog.DARK_THEME : DCloudAlertDialog.LIGHT_THEME, PdrUtil.isNavigationBarExist(activity));
        dCloudAlertDialog.setTitle(string);
        dCloudAlertDialog.setButton(-2, activity.getString(17039370), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Process.killProcess(Process.myPid());
            }
        });
        dCloudAlertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return i == 4;
            }
        });
        dCloudAlertDialog.setCancelable(false);
        dCloudAlertDialog.setCanceledOnTouchOutside(false);
        dCloudAlertDialog.show();
    }

    public static Dialog getLossDialog(final IWebview iWebview, String str, final String str2, final String str3) {
        if (list.contains(str3)) {
            return null;
        }
        list.add(str3);
        if (iWebview == null || iWebview.getActivity() == null) {
            return null;
        }
        DCloudAlertDialog dCloudAlertDialog = new DCloudAlertDialog(iWebview.getActivity(), PdrR.FEATURE_LOSS_STYLE, PdrUtil.isNavigationBarExist(iWebview.getActivity()));
        dCloudAlertDialog.setTitle("HTML5+ Runtime");
        dCloudAlertDialog.setIcon(17301659);
        dCloudAlertDialog.setMessage(str);
        dCloudAlertDialog.setButton(-1, iWebview.getContext().getString(R.string.dcloud_common_view_details), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(str2));
                iWebview.getActivity().startActivity(intent);
            }
        });
        dCloudAlertDialog.setButton(-2, iWebview.getContext().getString(R.string.dcloud_common_ignore), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dCloudAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                ErrorDialogUtil.list.remove(str3);
            }
        });
        return dCloudAlertDialog;
    }

    public static void showErrorTipsAlert(Activity activity, String str, DialogInterface.OnClickListener onClickListener) {
        DCloudAlertDialog dCloudAlertDialog = new DCloudAlertDialog(activity, AppRuntime.getAppDarkMode(activity) ? DCloudAlertDialog.DARK_THEME : DCloudAlertDialog.LIGHT_THEME, PdrUtil.isNavigationBarExist(activity));
        dCloudAlertDialog.setMessage(str);
        dCloudAlertDialog.setButton(-2, activity.getString(17039370), onClickListener);
        dCloudAlertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return i == 4;
            }
        });
        dCloudAlertDialog.setCancelable(false);
        dCloudAlertDialog.setCanceledOnTouchOutside(false);
        dCloudAlertDialog.show();
    }
}
