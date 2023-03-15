package io.dcloud.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.view.Window;
import android.view.WindowManager;
import com.dcloud.android.widget.dialog.DCloudAlertDialog;
import com.dcloud.android.widget.toast.ToastCompat;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.ui.PermissionGuideWindow;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class AppPermissionUtil {
    public static final int MODE_ALLOWED = 0;
    public static final int MODE_ASK = 4;
    public static final int MODE_DEFAULT = 3;
    public static final int MODE_ERRORED = 2;
    public static final int MODE_IGNORED = 1;
    public static final int MODE_UNKNOWN = -1;
    public static String OP_INSTALL_SHORTCUT = "op_install_shortcut";
    public static HashMap<String, Integer> mXiaoMiCode19OPSIDs = new HashMap<>();
    public static HashMap<String, Integer> mXiaoMiCode21OPSIDs = new HashMap<>();
    public static HashMap<String, Integer> mXiaoMiCode23OPSIDs = new HashMap<>();

    static {
        mXiaoMiCode19OPSIDs.put("op_install_shortcut", 60);
        mXiaoMiCode21OPSIDs.put(OP_INSTALL_SHORTCUT, 63);
        mXiaoMiCode23OPSIDs.put(OP_INSTALL_SHORTCUT, 10017);
    }

    public static void againShortcutOpsDialog(final IApp iApp, final Activity activity, final String str, String str2) {
        final SharedPreferences orCreateBundle = SP.getOrCreateBundle((Context) activity, "pdr");
        DCloudAlertDialog initDialogTheme = DialogUtil.initDialogTheme(activity, true);
        initDialogTheme.setTitle(R.string.dcloud_short_cut_set_pms);
        initDialogTheme.setMessage(activity.getString(R.string.dcloud_short_cut_create_error_tips));
        initDialogTheme.setButton(-1, activity.getString(R.string.dcloud_short_cut_goto_pms), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.parse("package:" + activity.getPackageName()));
                SharedPreferences.Editor edit = orCreateBundle.edit();
                edit.putBoolean(str + SP.IS_CREATE_SHORTCUT, true).commit();
                activity.startActivity(intent);
            }
        });
        initDialogTheme.setButton(-2, activity.getString(R.string.dcloud_short_cut_not_install), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (Build.BRAND.equalsIgnoreCase(MobilePhoneModel.MEIZU) || Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.HUAWEI)) {
                    ShortCutUtil.createShortcutToDeskTop(IApp.this, false);
                }
            }
        });
        initDialogTheme.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
            }
        });
        initDialogTheme.setCanceledOnTouchOutside(false);
        initDialogTheme.show();
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x00eb  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0142  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int checkNoShortcutPermionGuide(android.content.Context r13, java.lang.String r14, boolean r15, io.dcloud.common.DHInterface.IApp r16, java.lang.String r17, android.content.SharedPreferences r18, boolean r19) {
        /*
            r0 = r13
            r2 = r17
            int r1 = io.dcloud.base.R.string.dcloud_short_cut_pms_unauthorized_tips1
            java.lang.String r1 = r13.getString(r1)
            java.lang.String r3 = android.os.Build.BRAND
            java.lang.String r4 = io.dcloud.common.adapter.util.MobilePhoneModel.MEIZU
            boolean r4 = r3.equalsIgnoreCase(r4)
            r5 = 0
            r6 = 1
            r7 = -1
            java.lang.String r8 = "_staremapp_shortcut_guide_is_first_vivo"
            if (r4 == 0) goto L_0x0032
            android.content.Intent r3 = io.dcloud.common.util.ShortCutUtil.getHeadShortCutIntent(r14)
            boolean r3 = isFlymeShortcutallowAllow(r13, r3)
            if (r3 != 0) goto L_0x00d6
            int r1 = io.dcloud.PdrR.DCLOUD_GUIDE_GIF_MEIZU
            int r3 = io.dcloud.base.R.string.dcloud_short_cut_pms_unauthorized_tips2
            java.lang.String r3 = r13.getString(r3)
            java.lang.String r8 = "_staremapp_shortcut_guide_is_first_flyme"
            r4 = r1
            r7 = r3
        L_0x002e:
            r1 = r8
        L_0x002f:
            r8 = 1
            goto L_0x00db
        L_0x0032:
            java.lang.String r4 = io.dcloud.common.adapter.util.MobilePhoneModel.XIAOMI
            boolean r4 = r3.equalsIgnoreCase(r4)
            if (r4 == 0) goto L_0x004f
            int r7 = checkOp(r13)
            int r1 = io.dcloud.base.R.string.dcloud_short_cut_pms_unauthorized_tips2
            java.lang.String r1 = r13.getString(r1)
            int r3 = io.dcloud.PdrR.DCLOUD_GUIDE_GIF_XIAOMI
            java.lang.String r8 = "_staremapp_shortcut_guide_is_first_miui"
            r4 = r3
            r12 = r7
            r7 = r1
            r1 = r8
            r8 = r12
            goto L_0x00db
        L_0x004f:
            java.lang.String r4 = android.os.Build.MANUFACTURER
            java.lang.String r9 = io.dcloud.common.adapter.util.MobilePhoneModel.HUAWEI
            boolean r4 = r4.equalsIgnoreCase(r9)
            if (r4 == 0) goto L_0x006c
            boolean r3 = isEmuiShortcutallowAllow()
            if (r3 != 0) goto L_0x00d6
            int r1 = io.dcloud.base.R.string.dcloud_short_cut_pms_unauthorized_tips3
            java.lang.String r1 = r13.getString(r1)
            int r3 = io.dcloud.PdrR.DCLOUD_GUIDE_GIF_HUAWEI
            java.lang.String r8 = "_staremapp_shortcut_guide_is_first_emui"
            r7 = r1
            r4 = r3
            goto L_0x002e
        L_0x006c:
            java.lang.String r4 = io.dcloud.common.adapter.util.MobilePhoneModel.VIVO
            boolean r3 = r3.equalsIgnoreCase(r4)
            if (r3 == 0) goto L_0x00d6
            java.lang.String r3 = "com.iqoo.secure"
            java.lang.String r3 = io.dcloud.common.util.LoadAppUtils.getAppVersionName(r13, r3)
            java.lang.String r4 = io.dcloud.common.util.LoadAppUtils.getAppName(r13)
            java.lang.String r9 = io.dcloud.common.util.ShortCutUtil.requestShortCutPermissionVivo(r13, r4)
            boolean r10 = io.dcloud.common.util.PdrUtil.isEmpty(r3)
            if (r10 != 0) goto L_0x00d6
            java.lang.String r10 = "2"
            boolean r10 = r3.startsWith(r10)
            if (r10 != 0) goto L_0x00d5
            java.lang.String r10 = "1"
            boolean r11 = r3.startsWith(r10)
            if (r11 == 0) goto L_0x0099
            goto L_0x00d5
        L_0x0099:
            boolean r10 = io.dcloud.common.util.PdrUtil.isEquals(r10, r9)
            if (r10 != 0) goto L_0x00a7
            java.lang.String r10 = "17"
            boolean r9 = io.dcloud.common.util.PdrUtil.isEquals(r10, r9)
            if (r9 == 0) goto L_0x00d6
        L_0x00a7:
            java.lang.String r7 = "3"
            boolean r7 = r3.startsWith(r7)
            if (r7 == 0) goto L_0x00c2
            int r1 = io.dcloud.base.R.string.dcloud_short_cut_pms_unauthorized_tips4
            java.lang.String r1 = r13.getString(r1)
            java.lang.Object[] r3 = new java.lang.Object[r6]
            r3[r5] = r4
            java.lang.String r1 = io.dcloud.common.util.StringUtil.format(r1, r3)
            r7 = r1
            r1 = r8
            r4 = 1
            goto L_0x002f
        L_0x00c2:
            java.lang.String r4 = "4"
            boolean r3 = r3.startsWith(r4)
            if (r3 == 0) goto L_0x00d0
            int r1 = io.dcloud.base.R.string.dcloud_short_cut_open_set_pms
            java.lang.String r1 = r13.getString(r1)
        L_0x00d0:
            r7 = r1
            r1 = r8
            r4 = 0
            goto L_0x002f
        L_0x00d5:
            return r7
        L_0x00d6:
            r8 = 0
            r7 = r1
            r1 = r8
            r4 = 0
            r8 = -1
        L_0x00db:
            if (r8 != r6) goto L_0x0169
            if (r19 == 0) goto L_0x0169
            java.lang.String r3 = r16.forceShortCut()
            java.lang.String r9 = "tipOnce"
            boolean r3 = io.dcloud.common.util.PdrUtil.isEquals(r3, r9)
            if (r3 == 0) goto L_0x0142
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r2)
            r3.append(r1)
            java.lang.String r3 = r3.toString()
            r9 = r18
            boolean r3 = r9.getBoolean(r3, r6)
            if (r3 == 0) goto L_0x0169
            android.content.SharedPreferences$Editor r3 = r18.edit()
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            r10.append(r2)
            r10.append(r1)
            java.lang.String r1 = r10.toString()
            android.content.SharedPreferences$Editor r1 = r3.putBoolean(r1, r5)
            r1.commit()
            if (r15 == 0) goto L_0x012d
            android.app.Activity r1 = r16.getActivity()
            r0 = r16
            r2 = r17
            r3 = r18
            r5 = r7
            showShortCutDialog(r0, r1, r2, r3, r4, r5)
            goto L_0x0169
        L_0x012d:
            boolean r1 = io.dcloud.common.util.LoadAppUtils.startSecuritySettingPage(r13)
            if (r1 == 0) goto L_0x0169
            com.dcloud.android.widget.toast.ToastCompat r1 = com.dcloud.android.widget.toast.ToastCompat.makeText((android.content.Context) r13, (java.lang.CharSequence) r7, (int) r6)
            r1.show()
            io.dcloud.common.ui.PermissionGuideWindow r0 = io.dcloud.common.ui.PermissionGuideWindow.getInstance(r13)
            r0.showWindow(r7, r4)
            goto L_0x0169
        L_0x0142:
            r9 = r18
            if (r15 == 0) goto L_0x0155
            android.app.Activity r1 = r16.getActivity()
            r0 = r16
            r2 = r17
            r3 = r18
            r5 = r7
            showShortCutDialog(r0, r1, r2, r3, r4, r5)
            goto L_0x0169
        L_0x0155:
            boolean r1 = io.dcloud.common.util.LoadAppUtils.startSecuritySettingPage(r13)
            if (r1 == 0) goto L_0x0169
            com.dcloud.android.widget.toast.ToastCompat r1 = com.dcloud.android.widget.toast.ToastCompat.makeText((android.content.Context) r13, (java.lang.CharSequence) r7, (int) r6)
            r1.show()
            io.dcloud.common.ui.PermissionGuideWindow r0 = io.dcloud.common.ui.PermissionGuideWindow.getInstance(r13)
            r0.showWindow(r7, r4)
        L_0x0169:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.AppPermissionUtil.checkNoShortcutPermionGuide(android.content.Context, java.lang.String, boolean, io.dcloud.common.DHInterface.IApp, java.lang.String, android.content.SharedPreferences, boolean):int");
    }

    public static int checkOp(Context context) {
        if (Build.VERSION.SDK_INT < 19) {
            return -1;
        }
        Object systemService = context.getSystemService("appops");
        Class<?> cls = systemService.getClass();
        try {
            int intValue = ((Integer) cls.getDeclaredField("OP_INSTALL_SHORTCUT").get(cls)).intValue();
            Class cls2 = Integer.TYPE;
            return ((Integer) cls.getDeclaredMethod("checkOp", new Class[]{cls2, cls2, String.class}).invoke(systemService, new Object[]{Integer.valueOf(intValue), Integer.valueOf(Binder.getCallingUid()), context.getPackageName()})).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int checkPermission(Context context, String str) {
        String str2 = Build.BRAND;
        if (str2.equalsIgnoreCase(MobilePhoneModel.MEIZU)) {
            return !isFlymeShortcutallowAllow(context, ShortCutUtil.getHeadShortCutIntent(str)) ? 1 : 3;
        }
        if (str2.equalsIgnoreCase(MobilePhoneModel.XIAOMI)) {
            int checkOp = checkOp(context);
            if (checkOp == 0 || checkOp == 1) {
                return checkOp;
            }
            if (checkOp == 3 || checkOp == 4) {
                return 2;
            }
        } else if (Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.HUAWEI)) {
            return !isEmuiShortcutallowAllow() ? 1 : 3;
        }
        return 4;
    }

    public static boolean checkShortcutOps(IApp iApp, Activity activity, String str, String str2) {
        SharedPreferences orCreateBundle = SP.getOrCreateBundle((Context) activity, "pdr");
        if (getCheckShortcutOps(activity) != 1) {
            return true;
        }
        showShortCutOpsDialog(iApp, activity, str, orCreateBundle);
        return false;
    }

    public static int getCheckShortcutOps(Activity activity) {
        if (-1 != getShotCutOpId()) {
            return checkOp(activity);
        }
        return 0;
    }

    private static int getFlymePermissionGranted(Context context, int i, Intent intent) {
        try {
            Class<?> cls = Class.forName("meizu.security.IFlymePermissionService$Stub");
            Class<?> cls2 = Class.forName("android.os.ServiceManager");
            IBinder iBinder = (IBinder) cls2.getDeclaredMethod("getService", new Class[]{String.class}).invoke(cls2, new Object[]{"flyme_permission"});
            Object invoke = cls.getDeclaredMethod("asInterface", new Class[]{IBinder.class}).invoke(cls, new Object[]{iBinder});
            Class<?> cls3 = invoke.getClass();
            Class cls4 = Integer.TYPE;
            Method method = cls3.getMethod("noteIntentOperation", new Class[]{cls4, cls4, String.class, Intent.class});
            int callingPid = Binder.getCallingPid();
            return ((Integer) method.invoke(invoke, new Object[]{Integer.valueOf(i), Integer.valueOf(callingPid), context.getPackageName(), intent})).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static int getFlymeShortcutPid() {
        try {
            Class<?> cls = Class.forName("meizu.security.FlymePermissionManager");
            Field declaredField = cls.getDeclaredField("OP_SEND_SHORTCUT_BROADCAST");
            declaredField.setAccessible(true);
            return ((Integer) declaredField.get(cls)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int getShotCutOpId() {
        int i;
        if (Build.BRAND.equalsIgnoreCase(MobilePhoneModel.XIAOMI)) {
            switch (Build.VERSION.SDK_INT) {
                case 19:
                    return mXiaoMiCode19OPSIDs.get(OP_INSTALL_SHORTCUT).intValue();
                case 21:
                case 22:
                    return mXiaoMiCode21OPSIDs.get(OP_INSTALL_SHORTCUT).intValue();
                case 23:
                    return mXiaoMiCode23OPSIDs.get(OP_INSTALL_SHORTCUT).intValue();
                default:
                    return -1;
            }
        } else if (!Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.HUAWEI) || ((i = Build.VERSION.SDK_INT) != 23 && i != 24)) {
            return -1;
        } else {
            return 16777216;
        }
    }

    public static boolean isEmuiShortcutallowAllow() {
        try {
            int shotCutOpId = getShotCutOpId();
            if (-1 == shotCutOpId) {
                return true;
            }
            Class<?> cls = Class.forName("com.huawei.hsm.permission.StubController");
            Class cls2 = Integer.TYPE;
            if (((Integer) cls.getDeclaredMethod("holdForGetPermissionSelection", new Class[]{cls2, cls2, cls2, String.class}).invoke(cls, new Object[]{Integer.valueOf(shotCutOpId), Integer.valueOf(Binder.getCallingUid()), Integer.valueOf(Binder.getCallingPid()), null})).intValue() == 2) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isFlymeShortcutallowAllow(Context context, Intent intent) {
        int flymeShortcutPid = getFlymeShortcutPid();
        if (flymeShortcutPid == -1 || getFlymePermissionGranted(context, flymeShortcutPid, intent) != 1) {
            return true;
        }
        return false;
    }

    public static void showShortCutDialog(IApp iApp, Activity activity, String str, SharedPreferences sharedPreferences, int i, String str2) {
        DCloudAlertDialog initDialogTheme = DialogUtil.initDialogTheme(activity, true);
        String string = activity.getString(R.string.dcloud_short_cut_create_error);
        initDialogTheme.setTitle(activity.getString(R.string.dcloud_short_cut_set_pms));
        initDialogTheme.setMessage(string);
        final Activity activity2 = activity;
        final SharedPreferences sharedPreferences2 = sharedPreferences;
        final String str3 = str;
        final String str4 = str2;
        final int i2 = i;
        initDialogTheme.setButton(-1, activity.getString(R.string.dcloud_short_cut_set_up), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (LoadAppUtils.startSecuritySettingPage(activity2)) {
                    SharedPreferences.Editor edit = sharedPreferences2.edit();
                    edit.putBoolean(str3 + SP.IS_CREATE_SHORTCUT, true).commit();
                    ToastCompat.makeText((Context) activity2, (CharSequence) str4, 1).show();
                    PermissionGuideWindow.getInstance(activity2).showWindow(str4, i2);
                }
            }
        });
        initDialogTheme.setButton(-2, activity.getString(R.string.dcloud_short_cut_abandon_install), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        initDialogTheme.setCanceledOnTouchOutside(false);
        initDialogTheme.show();
        Window window = initDialogTheme.getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.x = 0;
            attributes.y = 0;
            window.setGravity(80);
            double d = (double) activity.getResources().getDisplayMetrics().widthPixels;
            Double.isNaN(d);
            window.setLayout((int) (d * 0.9d), attributes.height);
        }
    }

    public static void showShortCutOpsDialog(final IApp iApp, final Activity activity, final String str, final SharedPreferences sharedPreferences) {
        DCloudAlertDialog initDialogTheme = DialogUtil.initDialogTheme(activity, true);
        String string = activity.getString(R.string.dcloud_short_cut_create_error_tips2);
        if (Build.BRAND.equalsIgnoreCase(MobilePhoneModel.MEIZU)) {
            string = activity.getString(R.string.dcloud_short_cut_create_error_tips3);
        } else if (Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.HUAWEI)) {
            string = activity.getString(R.string.dcloud_short_cut_create_error_tips4);
        }
        initDialogTheme.setTitle(R.string.dcloud_short_cut_set_pms);
        initDialogTheme.setMessage(string);
        initDialogTheme.setButton(-1, activity.getString(R.string.dcloud_short_cut_goto_pms), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.parse("package:" + activity.getPackageName()));
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putBoolean(str + SP.IS_CREATE_SHORTCUT, true).commit();
                activity.startActivity(intent);
            }
        });
        initDialogTheme.setButton(-2, activity.getString(R.string.dcloud_short_cut_not_install), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                IApp iApp = IApp.this;
                AppPermissionUtil.againShortcutOpsDialog(iApp, activity, str, iApp.obtainAppName());
            }
        });
        initDialogTheme.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                IApp iApp = IApp.this;
                AppPermissionUtil.againShortcutOpsDialog(iApp, activity, str, iApp.obtainAppName());
            }
        });
        initDialogTheme.setCanceledOnTouchOutside(false);
        initDialogTheme.show();
    }
}
