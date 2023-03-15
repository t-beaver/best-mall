package io.dcloud.common.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import com.dcloud.android.widget.dialog.DCloudAlertDialog;
import com.dcloud.android.widget.toast.ToastCompat;
import com.taobao.weex.WXEnvironment;
import io.dcloud.PdrR;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.ui.PermissionGuideWindow;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class ShortCutUtil {
    public static final String NOPERMISSIONS = "nopermissions";
    private static final String SHORTCUT_SRC_STREAM_APPS = "short_cut_src_stream_apps";
    public static final String SHORT_CUT_EXISTING = "short_cut_existing";
    public static final String SHORT_CUT_NONE = "short_cut_none";
    public static final String TAG = "ShortCutUtil";
    public static final String UNKNOWN = "unknown";
    public static String activityNameSDK = null;
    public static HashMap<String, String> extraProSDK = null;
    public static boolean mAutoCreateShortcut = true;
    static TypeRunnable mRunnable;
    private boolean isChekShortCut;

    interface TypeRunnable extends Runnable {
        String getType();

        void setType(String str);
    }

    private static void addShortCutSrc(Context context, Intent intent, String str) {
        intent.putExtra(IntentConst.SHORT_CUT_SRC, str.hashCode() + "_" + context.getPackageName());
    }

    private static void checkShortcutPermission(IApp iApp, String str, SharedPreferences sharedPreferences, String str2) {
        if (PdrUtil.isEquals(iApp.forceShortCut(), "tipOnce")) {
            if (sharedPreferences.getBoolean(str + str2, true)) {
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putBoolean(str + str2, false).commit();
                AppPermissionUtil.showShortCutOpsDialog(iApp, iApp.getActivity(), str, sharedPreferences);
                return;
            }
            return;
        }
        AppPermissionUtil.showShortCutOpsDialog(iApp, iApp.getActivity(), str, sharedPreferences);
    }

    public static void createShortcut(IApp iApp, String str, Bitmap bitmap, boolean z) {
        Bitmap bitmap2;
        IApp iApp2 = iApp;
        String str2 = str;
        Bitmap bitmap3 = bitmap;
        Logger.e("StreamSDK", "come in createShortcut");
        Logger.e("IAN", "createShortcut: BaseInfo.mAutoCreateShortcut" + mAutoCreateShortcut);
        Logger.e("IAN", "createShortcut: ShortCutUtil.activityNameSDK" + activityNameSDK);
        if (iApp2 == null || TextUtils.isEmpty(str) || iApp.startFromShortCut() || iApp.forceShortCut().equals("none")) {
            if (iApp2 != null) {
                Logger.e("IAN", "createShortcut: filePath==" + str2 + "app.startFromShortCut()==" + iApp.startFromShortCut() + "app.forceShortCut().equals(none)==" + iApp.forceShortCut().equals("none"));
            }
        } else if (!PdrUtil.isEquals(iApp.forceShortCut(), "none")) {
            Logger.e("StreamSDK", "come out return 1");
            Intent obtainWebAppIntent = iApp.obtainWebAppIntent();
            boolean z2 = obtainWebAppIntent != null && obtainWebAppIntent.getIntExtra(IntentConst.START_FROM, -1) == 5;
            Logger.e("StreamSDK", "isMyRuning" + z2);
            if (!z2) {
                Activity activity = iApp.getActivity();
                String obtainAppName = iApp.obtainAppName();
                String obtainAppId = iApp.obtainAppId();
                if (bitmap3 == null) {
                    if (!TextUtils.isEmpty(str) && str2.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                        str2 = str2.substring(7);
                    }
                    bitmap2 = BitmapFactory.decodeFile(str2);
                } else {
                    bitmap2 = bitmap3;
                }
                if (bitmap2 == null) {
                    bitmap2 = BitmapFactory.decodeResource(activity.getResources(), PdrR.DRAWABLE_ICON);
                }
                Bitmap bitmap4 = bitmap2;
                Intent obtainWebAppIntent2 = iApp.obtainWebAppIntent();
                String stringExtra = obtainWebAppIntent2 != null ? obtainWebAppIntent2.getStringExtra(IntentConst.WEBAPP_SHORT_CUT_CLASS_NAME) : "";
                if (hasShortcut(activity, obtainAppName)) {
                    Logger.e("StreamSDK", "ShortCutUtil.hasShortcut(context, name)");
                    return;
                }
                SharedPreferences orCreateBundle = SP.getOrCreateBundle((Context) activity, "pdr");
                boolean z3 = orCreateBundle.getBoolean(obtainAppId + SP.STAREMAPP_FIRST_SHORT_CUT, true);
                if (ShortcutCreateUtil.isDisableShort(iApp.getActivity())) {
                    handleDisableShort(iApp.getActivity(), obtainAppId, z3, orCreateBundle);
                    SharedPreferences.Editor edit = orCreateBundle.edit();
                    edit.putBoolean(obtainAppId + SP.STAREMAPP_FIRST_SHORT_CUT, false).commit();
                } else if (!MobilePhoneModel.isSpecialPhone(activity) || !showSettingsDialog(iApp2, str2, bitmap3)) {
                    String string = orCreateBundle.getString(AbsoluteConst.TEST_RUN + obtainAppId, (String) null);
                    if (!TextUtils.isEmpty(string)) {
                        string.equals("__am=t");
                    }
                    boolean z4 = orCreateBundle.getBoolean(obtainAppId + SP.K_CREATED_SHORTCUT, false);
                    if (Build.BRAND.equalsIgnoreCase(MobilePhoneModel.MEIZU) && !AppPermissionUtil.isFlymeShortcutallowAllow(activity, getHeadShortCutIntent(obtainAppName))) {
                        checkShortcutPermission(iApp2, obtainAppId, orCreateBundle, SP.STAREMAPP_SHORTCUT_GUIDE_IS_FIRST_FLYME);
                    } else if (Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.HUAWEI) && !AppPermissionUtil.isEmuiShortcutallowAllow()) {
                        checkShortcutPermission(iApp2, obtainAppId, orCreateBundle, SP.STAREMAPP_SHORTCUT_GUIDE_IS_FIRST_EMUI);
                    } else if (ShortcutCreateUtil.isDuplicateLauncher(activity) || !iApp.forceShortCut().equals("auto") || !z4) {
                        if (!z4) {
                            SharedPreferences orCreateBundle2 = SP.getOrCreateBundle((Context) activity, "streamapp_create_shortcut");
                            orCreateBundle2.getBoolean("is_create_shortcut" + obtainAppId, false);
                        }
                        if (orCreateBundle.getBoolean(SP.K_SHORT_CUT_ONE_TIPS, true)) {
                            orCreateBundle.edit().putBoolean(SP.K_SHORT_CUT_ONE_TIPS, false).commit();
                        }
                        SharedPreferences.Editor edit2 = orCreateBundle.edit();
                        edit2.putBoolean(obtainAppId + SP.STAREMAPP_FIRST_SHORT_CUT, false).commit();
                        String str3 = "auto";
                        SharedPreferences sharedPreferences = orCreateBundle;
                        if (createShortcutToDeskTop(activity, obtainAppId, obtainAppName, bitmap4, stringExtra, (JSONObject) null, false)) {
                            Logger.e("StreamSDK", "come into createShortcutToDeskTop and return ture already");
                            if (z) {
                                if (PdrUtil.isEquals(iApp.forceShortCut(), "tipOnce")) {
                                    if (sharedPreferences.getBoolean(obtainAppId + SP.STAREMAPP_SHORTCUT_TIP_IS_FIRST, true)) {
                                        SharedPreferences.Editor edit3 = sharedPreferences.edit();
                                        edit3.putBoolean(obtainAppId + SP.STAREMAPP_SHORTCUT_TIP_IS_FIRST, false).commit();
                                        if (showToast(iApp2, activity, obtainAppId, sharedPreferences)) {
                                            return;
                                        }
                                    }
                                } else if (showToast(iApp2, activity, obtainAppId, sharedPreferences)) {
                                    return;
                                }
                            } else if (!isHasShortCut(iApp2, 1000, str3)) {
                                showCreateShortCutToast(iApp);
                            } else {
                                return;
                            }
                        }
                        SharedPreferences.Editor edit4 = sharedPreferences.edit();
                        edit4.putString(obtainAppId + SP.K_CREATE_SHORTCUT_NAME, obtainAppName).commit();
                        SharedPreferences.Editor edit5 = sharedPreferences.edit();
                        edit5.putBoolean(obtainAppId + SP.K_CREATED_SHORTCUT, true).commit();
                    }
                }
            }
        }
    }

    public static int createShortcutGuide(IApp iApp, String str, Bitmap bitmap, boolean z, boolean z2, boolean z3) {
        Bitmap bitmap2;
        IApp iApp2 = iApp;
        String str2 = str;
        if (iApp2 == null || TextUtils.isEmpty(str) || iApp.startFromShortCut()) {
            return -1;
        }
        Intent obtainWebAppIntent = iApp.obtainWebAppIntent();
        if (obtainWebAppIntent != null && obtainWebAppIntent.getIntExtra(IntentConst.START_FROM, -1) == 5) {
            return -1;
        }
        Activity activity = iApp.getActivity();
        String obtainAppName = iApp.obtainAppName();
        String obtainAppId = iApp.obtainAppId();
        if (bitmap == null) {
            if (!TextUtils.isEmpty(str) && str2.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                str2 = str2.substring(7);
            }
            bitmap2 = BitmapFactory.decodeFile(str2);
        } else {
            bitmap2 = bitmap;
        }
        if (bitmap2 == null) {
            bitmap2 = BitmapFactory.decodeResource(activity.getResources(), PdrR.DRAWABLE_ICON);
        }
        Bitmap bitmap3 = bitmap2;
        Intent obtainWebAppIntent2 = iApp.obtainWebAppIntent();
        String stringExtra = obtainWebAppIntent2 != null ? obtainWebAppIntent2.getStringExtra(IntentConst.WEBAPP_SHORT_CUT_CLASS_NAME) : "";
        if (hasShortcut(activity, obtainAppName)) {
            return -1;
        }
        SharedPreferences orCreateBundle = SP.getOrCreateBundle((Context) activity, "pdr");
        boolean z4 = orCreateBundle.getBoolean(obtainAppId + SP.STAREMAPP_FIRST_SHORT_CUT, true);
        if (ShortcutCreateUtil.isDisableShort(iApp.getActivity())) {
            handleDisableShort(iApp.getActivity(), obtainAppId, z4, orCreateBundle);
            SharedPreferences.Editor edit = orCreateBundle.edit();
            edit.putBoolean(obtainAppId + SP.STAREMAPP_FIRST_SHORT_CUT, false).commit();
            return -1;
        }
        String string = orCreateBundle.getString(AbsoluteConst.TEST_RUN + obtainAppId, (String) null);
        if (!TextUtils.isEmpty(string)) {
            string.equals("__am=t");
        }
        boolean z5 = orCreateBundle.getBoolean(obtainAppId + SP.K_CREATED_SHORTCUT, false);
        int checkNoShortcutPermionGuide = AppPermissionUtil.checkNoShortcutPermionGuide(activity, obtainAppName, z3, iApp, obtainAppId, orCreateBundle, z2);
        if (checkNoShortcutPermionGuide == 1) {
            return checkNoShortcutPermionGuide;
        }
        if (!ShortcutCreateUtil.isDuplicateLauncher(activity) && iApp.forceShortCut().equals("auto") && z5) {
            return checkNoShortcutPermionGuide;
        }
        if (!z5) {
            SharedPreferences orCreateBundle2 = SP.getOrCreateBundle((Context) activity, "streamapp_create_shortcut");
            orCreateBundle2.getBoolean("is_create_shortcut" + obtainAppId, false);
        }
        if (orCreateBundle.getBoolean(SP.K_SHORT_CUT_ONE_TIPS, true)) {
            orCreateBundle.edit().putBoolean(SP.K_SHORT_CUT_ONE_TIPS, false).commit();
        }
        SharedPreferences.Editor edit2 = orCreateBundle.edit();
        edit2.putBoolean(obtainAppId + SP.STAREMAPP_FIRST_SHORT_CUT, false).commit();
        String str3 = SP.K_CREATED_SHORTCUT;
        SharedPreferences sharedPreferences = orCreateBundle;
        String str4 = obtainAppId;
        String str5 = obtainAppName;
        Activity activity2 = activity;
        if (createShortcutToDeskTop(activity, obtainAppId, obtainAppName, bitmap3, stringExtra, (JSONObject) null, false)) {
            if (!z) {
                showCreateShortCutToast(iApp);
            } else if (PdrUtil.isEquals(iApp.forceShortCut(), "tipOnce")) {
                if (sharedPreferences.getBoolean(str4 + SP.STAREMAPP_SHORTCUT_TIP_IS_FIRST, true)) {
                    SharedPreferences.Editor edit3 = sharedPreferences.edit();
                    edit3.putBoolean(str4 + SP.STAREMAPP_SHORTCUT_TIP_IS_FIRST, false).commit();
                    if (showToast(iApp2, activity2, str4, sharedPreferences)) {
                        return checkNoShortcutPermionGuide;
                    }
                }
            } else if (showToast(iApp2, activity2, str4, sharedPreferences)) {
                return checkNoShortcutPermionGuide;
            }
        }
        SharedPreferences.Editor edit4 = sharedPreferences.edit();
        edit4.putString(str4 + SP.K_CREATE_SHORTCUT_NAME, str5).commit();
        SharedPreferences.Editor edit5 = sharedPreferences.edit();
        edit5.putBoolean(str4 + str3, true).commit();
        return checkNoShortcutPermionGuide;
    }

    public static boolean createShortcutToDeskTop(Context context, String str, String str2, Bitmap bitmap, String str3, JSONObject jSONObject, boolean z) {
        return createShortcutToDeskTop(context, str, str2, bitmap, str3, jSONObject, z, false);
    }

    public static Intent getHeadShortCutIntent(String str) {
        Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        intent.putExtra("android.intent.extra.shortcut.NAME", str);
        intent.putExtra("duplicate", false);
        return intent;
    }

    public static String getShortCutUri(Context context) {
        ActivityInfo activityInfo;
        if (context == null) {
            return "";
        }
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        ResolveInfo resolveActivity = context.getPackageManager().resolveActivity(intent, 0);
        if (resolveActivity == null || (activityInfo = resolveActivity.activityInfo) == null || activityInfo.packageName.equals(WXEnvironment.OS)) {
            return "";
        }
        String str = resolveActivity.activityInfo.packageName;
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return "content://" + str + ".settings/favorites?notify=true";
    }

    public static Uri getUriFromLauncher(Context context) {
        StringBuilder sb = new StringBuilder();
        String launcherPackageName = LauncherUtil.getLauncherPackageName(context);
        Logger.e("tag", "getUriFromLauncher: packageName" + launcherPackageName);
        if ("com.nd.android.pandahome2".equals(launcherPackageName)) {
            return Uri.parse("content://com.nd.android.launcher2.settings/com.nd.hilauncherdev/favorites?notify=true");
        }
        String authorityFromPermission = LauncherUtil.getAuthorityFromPermission(context, launcherPackageName + ".permission.READ_SETTINGS");
        Logger.e("TAG", "getUriFromLauncher: LauncherUtil.getAuthorityFromPermissionwithpackagename(" + authorityFromPermission);
        if (TextUtils.isEmpty(authorityFromPermission)) {
            authorityFromPermission = LauncherUtil.getAuthorityFromPermissionDefault(context);
            Logger.e("TAG", "getUriFromLauncher: LauncherUtil.getAuthorityFromPermissionDefault(" + authorityFromPermission);
        }
        if (!TextUtils.isEmpty(authorityFromPermission)) {
            sb.append("content://");
            sb.append(authorityFromPermission);
            if (Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.OPPO)) {
                sb.append("/singledesktopitems?notify=true");
            } else {
                sb.append("/favorites?notify=true");
            }
            return Uri.parse(sb.toString());
        } else if (Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.QiKU)) {
            return Uri.parse("content://com.yulong.android.launcher3.compound/compoundworkspace?notify=false");
        } else {
            return null;
        }
    }

    public static void handleDisableShort(Activity activity, String str, boolean z, SharedPreferences sharedPreferences) {
    }

    public static boolean hasShortcut(Context context, String str) {
        return SHORT_CUT_EXISTING.equals(requestShortCut(context, str));
    }

    public static boolean isHasShortCut(final IApp iApp, long j, String str) {
        if (!Build.BRAND.equalsIgnoreCase(MobilePhoneModel.XIAOMI)) {
            return false;
        }
        TypeRunnable typeRunnable = mRunnable;
        if (typeRunnable != null) {
            if (typeRunnable.getType().equals("back") && str.equals(mRunnable.getType())) {
                return true;
            }
            removeRunHandler();
        }
        BaseInfo.isPostChcekShortCut = true;
        AnonymousClass3 r0 = new TypeRunnable() {
            String type;

            public String getType() {
                return this.type;
            }

            public void run() {
                BaseInfo.isPostChcekShortCut = false;
                if (!IApp.this.getActivity().isFinishing()) {
                    SharedPreferences orCreateBundle = SP.getOrCreateBundle((Context) IApp.this.getActivity(), "pdr");
                    if (ShortCutUtil.hasShortcut(IApp.this.getActivity(), IApp.this.obtainAppName())) {
                        SharedPreferences.Editor edit = orCreateBundle.edit();
                        edit.putString(IApp.this.obtainAppId() + SP.K_CREATE_SHORTCUT_NAME, IApp.this.obtainAppName()).commit();
                        SharedPreferences.Editor edit2 = orCreateBundle.edit();
                        edit2.putBoolean(IApp.this.obtainAppId() + SP.K_CREATED_SHORTCUT, true).commit();
                        ShortCutUtil.showCreateShortCutToast(IApp.this);
                    } else if (AppPermissionUtil.getCheckShortcutOps(IApp.this.getActivity()) == 0) {
                        ShortCutUtil.createShortcutToDeskTop(IApp.this, true);
                        ShortCutUtil.showCreateShortCutToast(IApp.this);
                    } else {
                        IApp iApp = IApp.this;
                        AppPermissionUtil.showShortCutOpsDialog(iApp, iApp.getActivity(), IApp.this.obtainAppId(), orCreateBundle);
                    }
                }
                ShortCutUtil.mRunnable = null;
            }

            public void setType(String str) {
                this.type = str;
            }
        };
        mRunnable = r0;
        r0.setType(str);
        MessageHandler.postDelayed(mRunnable, j);
        return true;
    }

    public static boolean isOpsCreateShortcut(Context context, String str) {
        SharedPreferences orCreateBundle = SP.getOrCreateBundle(context, "pdr");
        boolean z = orCreateBundle.getBoolean(str + SP.IS_CREATE_SHORTCUT, false);
        if (z) {
            SharedPreferences.Editor edit = orCreateBundle.edit();
            edit.remove(str + SP.IS_CREATE_SHORTCUT).commit();
        }
        return z;
    }

    public static boolean isRunShortCut(Context context, String str) {
        SharedPreferences orCreateBundle = SP.getOrCreateBundle(context, "pdr");
        if (!orCreateBundle.getString(SP.RECORD_RUN_SHORT_CUT, "").equals(str)) {
            return false;
        }
        orCreateBundle.edit().remove(SP.RECORD_RUN_SHORT_CUT).commit();
        return true;
    }

    public static void onResumeCreateShortcut(IApp iApp) {
        if (AppPermissionUtil.getCheckShortcutOps(iApp.getActivity()) == 1) {
            AppPermissionUtil.checkShortcutOps(iApp, iApp.getActivity(), iApp.obtainAppId(), iApp.obtainAppName());
            Intent obtainWebAppIntent = iApp.obtainWebAppIntent();
            createShortcutToDeskTop(iApp.getActivity(), iApp.obtainAppId(), iApp.obtainAppName(), (Bitmap) null, obtainWebAppIntent != null ? obtainWebAppIntent.getStringExtra(IntentConst.WEBAPP_SHORT_CUT_CLASS_NAME) : "", (JSONObject) null, false);
            return;
        }
        createShortcut(iApp, (String) null, (Bitmap) null, false);
    }

    public static void onResumeCreateShortcutGuide(IApp iApp) {
        PermissionGuideWindow.getInstance(iApp.getActivity()).dismissWindow();
        String str = Build.BRAND;
        if (str.equalsIgnoreCase(MobilePhoneModel.GOOGLE)) {
            str = Build.MANUFACTURER;
        }
        if (!MobilePhoneModel.SMARTISAN.equals(str)) {
            MobilePhoneModel.XIAOMI.equals(str);
            MobilePhoneModel.MEIZU.equals(str);
            Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.HUAWEI);
            createShortcutGuide(iApp, (String) null, (Bitmap) null, false, true, true);
            return;
        }
        createShortcutGuide(iApp, (String) null, (Bitmap) null, false, false, false);
    }

    public static void removeRunHandler() {
        if (mRunnable != null) {
            BaseInfo.isPostChcekShortCut = false;
            MessageHandler.removeCallbacks(mRunnable);
        }
    }

    public static boolean removeShortcutFromDeskTop(Context context, String str, String str2, String str3, JSONObject jSONObject) {
        Intent intent = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        intent.putExtra("android.intent.extra.shortcut.NAME", str2);
        intent.putExtra("duplicate", false);
        Intent intent2 = new Intent();
        if (TextUtils.isEmpty(str3)) {
            intent2 = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        } else {
            if (PdrUtil.isEmpty(activityNameSDK)) {
                intent2.setClassName(context.getPackageName(), str3);
            } else {
                intent2.putExtra(IntentConst.WEBAPP_ACTIVITY_SHORTCUTACTIVITY, activityNameSDK);
                intent2.setClassName(context.getPackageName(), activityNameSDK);
            }
            intent2.setAction("android.intent.action.MAIN");
            intent2.addCategory("android.intent.category.LAUNCHER");
        }
        if (jSONObject != null) {
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                try {
                    String next = keys.next();
                    intent2.putExtra(next, jSONObject.getString(next));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        addShortCutSrc(context, intent2, str2);
        intent2.putExtra(IntentConst.SHORT_CUT_APPID, str);
        intent2.putExtra(IntentConst.FROM_SHORT_CUT_STRAT, true);
        intent2.setFlags(268435456);
        intent2.setData(Uri.parse("http://m3w.cn/s/" + str));
        intent.putExtra("android.intent.extra.shortcut.INTENT", intent2);
        context.sendBroadcast(intent);
        return true;
    }

    public static String requestShortCut(Context context, String str) {
        String str2 = "unknown";
        if (Build.VERSION.SDK_INT >= 25) {
            List<ShortcutInfo> pinnedShortcuts = ((ShortcutManager) context.getSystemService(ShortcutManager.class)).getPinnedShortcuts();
            String str3 = "id_" + str.hashCode();
            for (int i = 0; i < pinnedShortcuts.size(); i++) {
                if (pinnedShortcuts.get(i).getId().equals(str3)) {
                    return SHORT_CUT_EXISTING;
                }
            }
            return str2;
        }
        ContentResolver contentResolver = context.getContentResolver();
        Uri uriFromLauncher = getUriFromLauncher(context);
        if (uriFromLauncher == null) {
            String shortCutUri = getShortCutUri(context);
            if (!TextUtils.isEmpty(shortCutUri)) {
                uriFromLauncher = Uri.parse(shortCutUri);
                Logger.es("shortcututil", context.getString(R.string.dcloud_short_cut_err1) + uriFromLauncher);
            }
        }
        Logger.e("shortcututil", "requestShortCut: uri===" + uriFromLauncher);
        if (uriFromLauncher != null) {
            try {
                Cursor query = contentResolver.query(uriFromLauncher, new String[]{AbsoluteConst.JSON_KEY_TITLE, "intent"}, "title=? ", new String[]{str}, (String) null);
                if (query != null) {
                    if (query.getCount() > 0) {
                        Logger.e("shortcututil", "c != null && c.getCount() > 0");
                        while (query.moveToNext()) {
                            String string = query.getString(query.getColumnIndex("intent"));
                            if (!TextUtils.isEmpty(string)) {
                                Logger.e("shortcututil", "intent=====" + string);
                                if (string.contains(IntentConst.SHORT_CUT_APPID)) {
                                    if (!BaseInfo.isBase(context)) {
                                        if (string.contains("io.dcloud.appstream.StreamAppMainActivity")) {
                                            if (!string.contains(str.hashCode() + "_" + context.getPackageName())) {
                                            }
                                        }
                                    }
                                    str2 = SHORT_CUT_EXISTING;
                                }
                            } else {
                                str2 = SHORT_CUT_NONE;
                            }
                        }
                        if (query != null && !query.isClosed()) {
                            query.close();
                        }
                    }
                }
                str2 = SHORT_CUT_NONE;
                query.close();
            } catch (Exception e) {
                if (e.getMessage() != null && e.getMessage().contains("READ_SETTINGS")) {
                    str2 = NOPERMISSIONS;
                }
                Logger.es("shortcututil", e.getMessage() + "URI==" + uriFromLauncher);
                e.printStackTrace();
            }
        }
        return str2;
    }

    public static String requestShortCutForCommit(Context context, String str) {
        return requestShortCut(context, str);
    }

    public static String requestShortCutPermissionVivo(Context context, String str) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uriFromLauncher = getUriFromLauncher(context);
        if (uriFromLauncher == null) {
            String shortCutUri = getShortCutUri(context);
            if (!TextUtils.isEmpty(shortCutUri)) {
                uriFromLauncher = Uri.parse(shortCutUri);
            }
        }
        Uri uri = uriFromLauncher;
        Logger.e("shortcututil", "requestShortCut: uri===" + uri);
        if (uri == null) {
            return "-1";
        }
        try {
            Cursor query = contentResolver.query(uri, new String[]{AbsoluteConst.JSON_KEY_TITLE, "intent", "shortcutPermission"}, "title=? ", new String[]{str}, (String) null);
            if (query != null && query.getCount() > 0) {
                Logger.e("shortcututil", "c != null && c.getCount() > 0");
                if (query.moveToNext()) {
                    return query.getString(query.getColumnIndex("shortcutPermission"));
                }
            }
            if (query == null || query.isClosed()) {
                return "-1";
            }
            query.close();
            return "-1";
        } catch (Exception e) {
            Logger.es("shortcututil", e.getMessage() + "URI==" + uri);
            e.printStackTrace();
            return "-1";
        }
    }

    public static void showCreateShortCutToast(IApp iApp) {
        String format = StringUtil.format(iApp.getActivity().getString(R.string.dcloud_short_cut_created), iApp.obtainAppName());
        if (iApp.forceShortCut().equals(AbsoluteConst.INSTALL_OPTIONS_FORCE) && !ShortcutCreateUtil.isDuplicateLauncher(iApp.getActivity())) {
            String str = "“" + iApp.obtainAppName() + iApp.getActivity().getString(R.string.dcloud_short_cut_created_tip);
            if (ShortcutCreateUtil.needToast(iApp.getActivity())) {
                ToastCompat.makeText(iApp.getActivity().getApplicationContext(), (CharSequence) str, 1).show();
            }
        } else if (ShortcutCreateUtil.needToast(iApp.getActivity())) {
            ToastCompat.makeText(iApp.getActivity().getApplicationContext(), (CharSequence) format, 1).show();
        }
    }

    public static boolean showSettingsDialog(final IApp iApp, final String str, final Bitmap bitmap) {
        String str2;
        String str3;
        String str4;
        final SharedPreferences orCreateBundle = SP.getOrCreateBundle((Context) iApp.getActivity(), "pdr");
        if (!orCreateBundle.getBoolean(SP.K_SHORT_CUT_ONE_TIPS, true) && !Build.BRAND.equals(MobilePhoneModel.SMARTISAN)) {
            return false;
        }
        if (orCreateBundle.getBoolean(SP.K_SHORT_CUT_ONE_TIPS, true)) {
            orCreateBundle.edit().putBoolean(SP.K_SHORT_CUT_ONE_TIPS, false).commit();
        }
        String string = iApp.getActivity().getString(R.string.dcloud_short_cut_goto_pms);
        String string2 = iApp.getActivity().getString(R.string.dcloud_short_cut_it_set);
        String str5 = Build.BRAND;
        if (str5.equalsIgnoreCase(MobilePhoneModel.QiKU)) {
            str3 = iApp.getActivity().getString(R.string.dcloud_short_cut_qiku1);
            str2 = iApp.getActivity().getString(R.string.dcloud_short_cut_goto_run);
        } else {
            if (str5.equalsIgnoreCase(MobilePhoneModel.VIVO)) {
                if (!LoadAppUtils.isAppLoad(iApp.getActivity(), "com.iqoo.secure") || Build.VERSION.SDK_INT < 21) {
                    return false;
                }
                str4 = iApp.getActivity().getString(R.string.dcloud_short_cut_vivo1) + "App" + iApp.getActivity().getString(R.string.dcloud_short_cut_vivo2);
            } else if (str5.equalsIgnoreCase(MobilePhoneModel.SMARTISAN)) {
                str2 = string;
                str3 = iApp.getActivity().getString(R.string.dcloud_short_cut_chuizi1);
                string2 = iApp.getActivity().getString(R.string.dcloud_short_cut_not_install);
            } else {
                str4 = "";
            }
            String str6 = str4;
            str2 = string;
            str3 = str6;
        }
        DCloudAlertDialog initDialogTheme = DialogUtil.initDialogTheme(iApp.getActivity(), true);
        initDialogTheme.setMessage(str3);
        initDialogTheme.setTitle(iApp.getActivity().getString(R.string.dcloud_short_cut_tips));
        initDialogTheme.setButton(-1, str2, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent;
                orCreateBundle.edit().putString(SP.RECORD_RUN_SHORT_CUT, iApp.obtainAppId()).commit();
                String str = Build.BRAND;
                if (str.equalsIgnoreCase(MobilePhoneModel.QiKU) || str.equalsIgnoreCase(MobilePhoneModel.SMARTISAN)) {
                    if (str.equalsIgnoreCase(MobilePhoneModel.QiKU)) {
                        intent = new Intent();
                        intent.setClassName("com.yulong.android.launcher3", "com.yulong.android.launcher3.LauncherSettingsActivity");
                    } else {
                        intent = new Intent("android.settings.SETTINGS");
                    }
                    iApp.getActivity().startActivity(intent);
                } else if (str.equalsIgnoreCase(MobilePhoneModel.VIVO)) {
                    PackageManager packageManager = iApp.getActivity().getPackageManager();
                    new Intent();
                    Intent launchIntentForPackage = packageManager.getLaunchIntentForPackage("com.iqoo.secure");
                    launchIntentForPackage.setFlags(337641472);
                    iApp.getActivity().startActivity(launchIntentForPackage);
                }
            }
        });
        initDialogTheme.setButton(-2, string2, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!Build.BRAND.equalsIgnoreCase(MobilePhoneModel.SMARTISAN)) {
                    ShortCutUtil.createShortcut(IApp.this, str, bitmap, false);
                }
            }
        });
        initDialogTheme.setCanceledOnTouchOutside(false);
        initDialogTheme.show();
        return true;
    }

    private static boolean showToast(IApp iApp, Activity activity, String str, SharedPreferences sharedPreferences) {
        if (!"12214060304".equals(BaseInfo.sChannel) || !"com.aliyun.homeshell".equals(LauncherUtil.getLauncherPackageName(activity))) {
            showCreateShortCutToast(iApp);
        } else {
            if (sharedPreferences.getBoolean(str + SP.STAREMAPP_ALIYUN_SHORT_CUT_IS_FIRST_CREATED, true)) {
                showCreateShortCutToast(iApp);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putBoolean(str + SP.STAREMAPP_ALIYUN_SHORT_CUT_IS_FIRST_CREATED, false).commit();
            }
        }
        return false;
    }

    public static void updateShortcutFromDeskTop(Activity activity, String str, String str2, Bitmap bitmap, String str3) {
        removeShortcutFromDeskTop(activity, str, str2, str3, (JSONObject) null);
        createShortcutToDeskTop(activity, str, str2, bitmap, str3, (JSONObject) null, false);
    }

    public static boolean createShortcutToDeskTop(IApp iApp, boolean z) {
        Intent obtainWebAppIntent = iApp.obtainWebAppIntent();
        return createShortcutToDeskTop(iApp.getActivity(), iApp.obtainAppId(), iApp.obtainAppName(), (Bitmap) null, obtainWebAppIntent != null ? obtainWebAppIntent.getStringExtra(IntentConst.WEBAPP_SHORT_CUT_CLASS_NAME) : "", (JSONObject) null, false, z);
    }

    public static boolean createShortcutToDeskTop(Context context, String str, String str2, Bitmap bitmap, String str3, JSONObject jSONObject, boolean z, boolean z2) {
        Intent intent = new Intent();
        if (TextUtils.isEmpty(str3)) {
            intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        } else {
            intent.setClassName(context.getPackageName(), str3);
            intent.setAction("android.intent.action.MAIN");
        }
        if (jSONObject != null) {
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                try {
                    String next = keys.next();
                    intent.putExtra(next, jSONObject.getString(next));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        addShortCutSrc(context, intent, str2);
        intent.putExtra(IntentConst.SHORT_CUT_APPID, str);
        intent.putExtra(IntentConst.FROM_SHORT_CUT_STRAT, true);
        intent.setFlags(268435456);
        if (Build.VERSION.SDK_INT >= 26) {
            ShortcutManager shortcutManager = (ShortcutManager) context.getSystemService(IApp.ConfigProperty.CONFIG_SHORTCUT);
            if (shortcutManager != null && shortcutManager.isRequestPinShortcutSupported()) {
                ShortcutInfo build = new ShortcutInfo.Builder(context, "id_" + str2.hashCode()).setIcon(Icon.createWithBitmap(bitmap)).setIntent(intent).setShortLabel(str2).build();
                shortcutManager.requestPinShortcut(build, PendingIntent.getBroadcast(context, 0, shortcutManager.createShortcutResultIntent(build), 0).getIntentSender());
            }
        } else {
            Intent headShortCutIntent = getHeadShortCutIntent(str2);
            headShortCutIntent.putExtra("android.intent.extra.shortcut.INTENT", intent);
            headShortCutIntent.putExtra("android.intent.extra.shortcut.ICON", bitmap);
            context.sendBroadcast(headShortCutIntent);
        }
        return true;
    }
}
