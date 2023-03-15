package io.dcloud.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import androidx.core.content.FileProvider;
import com.facebook.common.callercontext.ContextChain;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

public class LoadAppUtils {
    public static final int APK_DOWNGRADE = -1;
    public static final int APK_INSTALLED = 0;
    public static final int APK_UNINSTALLED = -2;
    public static final int APK_UPGRADE = 1;
    private static final int APP_TYPE_ALL = 0;
    private static final int APP_TYPE_NON_SYSTEM = 1;
    private static final int APP_TYPE_SYSTEM = 2;
    private static final String TAG = "LoadAppUtils";

    static class SafeCenter {
        private static ArrayList<Item> datas = new ArrayList<>();

        static class Item {
            String action;
            String clsName;
            String extParamName;
            String pname;

            Item(String str, String str2, String str3, String str4) {
                this.pname = str;
                this.clsName = str2;
                this.extParamName = str3;
                this.action = str4;
            }
        }

        SafeCenter() {
        }

        /* access modifiers changed from: private */
        public static boolean goSafeCenter(Context context) {
            try {
                init(context);
                PackageManager packageManager = context.getPackageManager();
                int i = 0;
                while (i < datas.size()) {
                    Intent intent = new Intent();
                    Item item = datas.get(i);
                    try {
                        if (packageManager.getPackageInfo(item.pname, 0) == null) {
                            i++;
                        } else {
                            if (!TextUtils.isEmpty(item.clsName)) {
                                intent.setClassName(item.pname, item.clsName);
                            } else if (!TextUtils.isEmpty(item.pname)) {
                                intent.setPackage(item.pname);
                            }
                            if (!TextUtils.isEmpty(item.action)) {
                                intent.setAction(item.action);
                            }
                            if (!TextUtils.isEmpty(item.extParamName)) {
                                intent.putExtra(item.extParamName, context.getPackageName());
                            }
                            intent.setFlags(268435456);
                            context.startActivity(intent);
                            Logger.e("Permission", "successful " + Build.MODEL + "intent=" + intent);
                            return true;
                        }
                    } catch (PackageManager.NameNotFoundException unused) {
                    }
                }
                return true;
            } catch (Exception e) {
                Logger.e("Permission", "Exception =" + e);
                return false;
            }
        }

        static void init(Context context) {
            if (datas.isEmpty()) {
                try {
                    JSONArray jSONArray = new JSONArray(new String(IOUtil.toString(new FileInputStream(new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/temp.j")))));
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject optJSONObject = jSONArray.optJSONObject(i);
                        String optString = optJSONObject.optString(ContextChain.TAG_PRODUCT);
                        if (!TextUtils.isEmpty(optString)) {
                            datas.add(new Item(optString, optJSONObject.optString(c.a), optJSONObject.optString("e"), optJSONObject.optString("a")));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (datas.isEmpty()) {
                    datas.add(new Item("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity", "extra_pkgname", (String) null));
                    datas.add(new Item("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity", "extra_pkgname", (String) null));
                    datas.add(new Item("com.meizu.safe", "com.meizu.safe.security.AppSecActivity", "packageName", (String) null));
                    datas.add(new Item("com.aliyun.mobile.permission", "com.aliyun.mobile.permission.ExternalAppDetailActivity", "packageName", (String) null));
                    datas.add(new Item("com.iqoo.secure", "com.iqoo.secure.MainActivity", "packageName", (String) null));
                    datas.add(new Item("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity", "package", (String) null));
                    datas.add(new Item("com.mediatek.security", "com.mediatek.security.ui.PermissionControlPageActivity", "package", (String) null));
                    datas.add(new Item("com.yulong.android.launcher3", "com.yulong.android.launcher3.LauncherSettingsActivity", "package", (String) null));
                    datas.add(new Item("com.android.settings", "com.android.settings.Settings$ManageApplicationsActivity", "package", (String) null));
                    datas.add(new Item((String) null, (String) null, "package", "android.settings.MANAGE_APPLICATIONS_SETTINGS"));
                }
            }
        }
    }

    public static String getApkFileLable(Context context, String str) {
        Constructor<?> constructor;
        Object obj;
        try {
            Class<?> cls = Class.forName(ReflectUtils.CLASSNAME_PAGEAGEPARSE);
            Class[] clsArr = {String.class};
            int i = Build.VERSION.SDK_INT;
            if (i >= 21) {
                constructor = cls.getConstructor(new Class[0]);
            } else {
                constructor = cls.getConstructor(clsArr);
            }
            Object[] objArr = {str};
            if (i >= 21) {
                obj = constructor.newInstance(new Object[0]);
            } else {
                obj = constructor.newInstance(objArr);
            }
            Log.d("DownloadUtils", "pkgParser:" + obj.toString());
            DisplayMetrics displayMetrics = new DisplayMetrics();
            displayMetrics.setToDefaults();
            Object invoke = cls.getDeclaredMethod("parsePackage", i >= 21 ? new Class[]{File.class, Integer.TYPE} : new Class[]{File.class, String.class, DisplayMetrics.class, Integer.TYPE}).invoke(obj, i >= 21 ? new Object[]{new File(str), 0} : new Object[]{new File(str), str, displayMetrics, 0});
            ApplicationInfo applicationInfo = (ApplicationInfo) invoke.getClass().getDeclaredField("applicationInfo").get(invoke);
            Log.d("DownloadUtils", "pkg:" + applicationInfo.packageName + " uid=" + applicationInfo.uid);
            Class<?> cls2 = Class.forName("android.content.res.AssetManager");
            CharSequence charSequence = null;
            Object newInstance = cls2.getConstructor((Class[]) null).newInstance((Object[]) null);
            cls2.getDeclaredMethod("addAssetPath", new Class[]{String.class}).invoke(newInstance, new Object[]{str});
            Resources resources = context.getResources();
            Resources newInstance2 = Resources.class.getConstructor(new Class[]{newInstance.getClass(), resources.getDisplayMetrics().getClass(), resources.getConfiguration().getClass()}).newInstance(new Object[]{newInstance, resources.getDisplayMetrics(), resources.getConfiguration()});
            int i2 = applicationInfo.labelRes;
            if (i2 != 0) {
                charSequence = newInstance2.getText(i2);
            }
            Log.d("DownloadUtils", "label=" + charSequence);
            return charSequence.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "-1";
        }
    }

    public static String[] getApkFileSignatureAndPackageName(Context context, String str) {
        Object obj;
        try {
            String[] packageSignatures = getPackageSignatures(context, str);
            if (packageSignatures != null) {
                return packageSignatures;
            }
            try {
                obj = parsePackage(str, 64);
            } catch (OutOfMemoryError unused) {
                obj = null;
            }
            if (obj == null) {
                return null;
            }
            Signature[] apkSignature = getApkSignature(obj, str);
            if (apkSignature == null || apkSignature.length <= 0) {
                return null;
            }
            return new String[]{HashUtils.getHash(Arrays.toString(apkSignature[0].toByteArray())).toLowerCase(Locale.ENGLISH), (String) ReflectUtils.getObjectFieldNoDeclared(ReflectUtils.getField(obj, "applicationInfo"), "packageName")};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] getApkFileSignatureAndPackageNameEx(Context context, String str) {
        Signature[] signatureArr;
        try {
            Object parsePackage = parsePackage(str, 64);
            if (!(parsePackage == null || (signatureArr = (Signature[]) ReflectUtils.getField(parsePackage, "mSignatures")) == null)) {
                if (signatureArr.length != 0) {
                    return new String[]{HashUtils.getHash(Arrays.toString(signatureArr[0].toByteArray())).toLowerCase(Locale.ENGLISH), (String) ReflectUtils.getField(parsePackage, "packageName")};
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getAppName(Context context) {
        try {
            return context.getResources().getString(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.labelRes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getAppSignatureMd5(Context context, String str) {
        try {
            return Md5Utils.md5LowerCase(Arrays.toString(context.getPackageManager().getPackageInfo(str, 64).signatures[0].toByteArray()));
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    public static String getAppSignatureSHA1(Context context) {
        try {
            String str = null;
            for (Signature signatureString : context.getPackageManager().getPackageInfo(context.getPackageName(), 64).signatures) {
                str = getSignatureString(signatureString, "SHA1");
            }
            return str;
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    public static String getAppVersionName(Context context, String str) {
        try {
            return context.getPackageManager().getPackageInfo(str, 0).versionName;
        } catch (Exception unused) {
            return "";
        }
    }

    public static Intent getDataAndTypeIntent(Context context, String str, String str2) {
        Intent intent = new Intent();
        intent.addFlags(268435456);
        intent.setAction("android.intent.action.VIEW");
        if (Build.VERSION.SDK_INT >= 24) {
            Uri uriForFile = FileProvider.getUriForFile(context, context.getPackageName() + ".dc.fileprovider", new File(str));
            intent.addFlags(1);
            intent.setDataAndType(uriForFile, str2);
        } else {
            intent.setDataAndType(Uri.fromFile(new File(str)), str2);
        }
        return intent;
    }

    public static int getLoadState(PackageInfo packageInfo, int i) {
        if (packageInfo == null) {
            return -2;
        }
        int i2 = packageInfo.versionCode;
        if (i2 == i) {
            return 0;
        }
        return i2 < i ? 1 : -1;
    }

    public static PackageInfo getLoadedApp(Context context, PackageManager packageManager, String str) {
        try {
            return context.getPackageManager().getPackageInfo(str, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getLoadedAppNameByPackageInfo(Context context, PackageManager packageManager, PackageInfo packageInfo) {
        if (packageManager == null) {
            packageManager = context.getPackageManager();
        }
        return packageManager.getApplicationLabel(packageInfo.applicationInfo).toString();
    }

    private static String[] getPackageSignatures(Context context, String str) {
        Signature[] signatureArr;
        try {
            PackageInfo packageArchiveInfo = context.getPackageManager().getPackageArchiveInfo(str, 64);
            if (packageArchiveInfo == null || (signatureArr = packageArchiveInfo.signatures) == null || signatureArr.length <= 0) {
                return null;
            }
            return new String[]{HashUtils.getHash(Arrays.toString(signatureArr[0].toByteArray())).toLowerCase(Locale.ENGLISH), packageArchiveInfo.applicationInfo.packageName};
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public static String getSignatureString(Signature signature, String str) {
        byte[] byteArray = signature.toByteArray();
        try {
            MessageDigest instance = MessageDigest.getInstance(str);
            if (instance != null) {
                byte[] digest = instance.digest(byteArray);
                StringBuilder sb = new StringBuilder();
                for (byte b : digest) {
                    sb.append(Integer.toHexString((b & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) | 256).substring(1, 3));
                }
                return sb.toString();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "error!";
    }

    public static boolean isAppLoad(Context context, String str) {
        if (str == null) {
            return false;
        }
        if (str.equals(context.getPackageName())) {
            return true;
        }
        try {
            if (context.getPackageManager().getPackageInfo(str, 256) != null) {
                return true;
            }
            return false;
        } catch (PackageManager.NameNotFoundException unused) {
            return new File("/sdcard/Android/data/" + str).exists();
        }
    }

    public static boolean isEMUIRom() {
        return Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.HUAWEI);
    }

    public static boolean isSystemApp(ApplicationInfo applicationInfo) {
        return (applicationInfo.flags & 1) > 0 && !applicationInfo.publicSourceDir.startsWith("data/dataapp") && !applicationInfo.publicSourceDir.startsWith("/data/dataapp");
    }

    public static boolean isValidAppPackageName(String str) {
        return Pattern.compile("^[a-zA-Z_]\\w*(\\.[a-zA-Z_]\\w*)*$").matcher(str).matches();
    }

    public static boolean isVivoRom() {
        String str = Build.FINGERPRINT;
        Locale locale = Locale.ENGLISH;
        return str.toLowerCase(locale).contains("vivo") || Build.MODEL.toLowerCase(locale).contains("vivo");
    }

    public static void loadRunApp(Context context, String str) {
        context.startActivity(getDataAndTypeIntent(context, str, "application/vnd.android.package-archive"));
    }

    public static boolean openApp(Context context, String str) {
        Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(str);
        if (launchIntentForPackage == null) {
            return false;
        }
        context.startActivity(launchIntentForPackage);
        return true;
    }

    public static Object parsePackage(String str, int i) {
        Object obj;
        Object obj2;
        String str2 = str;
        try {
            int i2 = Build.VERSION.SDK_INT;
            if (i2 >= 21) {
                obj = ReflectUtils.getObjectConstructor(ReflectUtils.CLASSNAME_PAGEAGEPARSE, new Class[0]).newInstance(new Object[0]);
            } else {
                obj = ReflectUtils.getObjectConstructor(ReflectUtils.CLASSNAME_PAGEAGEPARSE, String.class).newInstance(new Object[]{str2});
            }
            DisplayMetrics displayMetrics = new DisplayMetrics();
            displayMetrics.setToDefaults();
            File file = new File(str2);
            if (i2 >= 21) {
                obj2 = obj.getClass().getMethod("parsePackage", new Class[]{File.class, Integer.TYPE}).invoke(obj, new Object[]{file, Integer.valueOf(i)});
            } else {
                obj2 = obj.getClass().getMethod("parsePackage", new Class[]{File.class, String.class, DisplayMetrics.class, Integer.TYPE}).invoke(obj, new Object[]{file, str2, displayMetrics, Integer.valueOf(i)});
            }
            if (obj2 == null) {
                Log.d(TAG, "---parsePackage is null------;;sourceFile=" + file.getAbsolutePath());
                return null;
            }
            if (i2 >= 21) {
                Method declaredMethod = obj.getClass().getDeclaredMethod("collectCertificates", new Class[]{ReflectUtils.classForName(ReflectUtils.CLASSNAME_PAGEAGEPARSE_PACKAGE), File.class, Integer.TYPE});
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(obj, new Object[]{obj2, file, 1});
            } else {
                obj.getClass().getDeclaredMethod("collectCertificates", new Class[]{ReflectUtils.classForName(ReflectUtils.CLASSNAME_PAGEAGEPARSE_PACKAGE), Integer.TYPE}).invoke(obj, new Object[]{obj2, 1});
            }
            return obj2;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean startAppDetailSettings(Context context, String str) {
        return SafeCenter.goSafeCenter(context);
    }

    public static boolean startSecuritySettingPage(Context context) {
        if (isVivoRom()) {
            return startShortcutSettingsVivo(context);
        }
        if (isEMUIRom()) {
            return startShortcutSettingsEMUI(context, context.getPackageName());
        }
        return startAppDetailSettings(context, context.getPackageName());
    }

    public static boolean startShortcutSettingsEMUI(Context context, String str) {
        Intent intent = new Intent();
        if (!(context instanceof Activity)) {
            intent.setFlags(268435456);
        }
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", str, (String) null));
        context.startActivity(intent);
        return true;
    }

    public static boolean startShortcutSettingsVivo(Context context) {
        try {
            Intent intent = new Intent("com.bbk.launcher.installshortcutpermission.open");
            intent.setPackage("com.bbk.launcher2");
            if (!(context instanceof Activity)) {
                intent.setFlags(268435456);
            }
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void uninstall(Context context, String str) {
        try {
            Intent intent = new Intent("android.intent.action.DELETE", Uri.fromParts("package", str, (String) null));
            intent.addFlags(268435456);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Signature[] getApkSignature(Object obj, String str) {
        Signature[] signatureArr = new Signature[0];
        try {
            signatureArr = (Signature[]) ReflectUtils.getField(obj, "mSignatures");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
        if (signatureArr != null && signatureArr.length > 0) {
            return signatureArr;
        }
        return null;
    }
}
