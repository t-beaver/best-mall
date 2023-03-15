package io.dcloud.common.adapter.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import com.facebook.common.callercontext.ContextChain;
import com.taobao.weex.common.RenderTypes;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.performance.WXInstanceApm;
import io.dcloud.application.DCLoudApplicationImpl;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.FileUtil;
import io.dcloud.common.util.IOUtil;
import io.dcloud.common.util.LoadAppUtils;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import io.dcloud.feature.internal.sdk.SDK;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class PlatformUtil extends SP {
    private static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    public static boolean APS_COVER = false;
    public static final byte ASSETS_RESOUCE = 0;
    private static final String EXTRA_SHORTCUT_DUPLICATE = "duplicate";
    public static final byte FILE_RESOUCE = 2;
    private static int MAX_SPAN_IN_ONE_SCREEN = 16;
    private static final String[] PROJECTION = {"_id", AbsoluteConst.JSON_KEY_TITLE, "iconResource"};
    public static final byte SRC_RESOUCE = 1;
    private static final String URI_HTC_LAUNCER = "content://com.htc.launcher.settings/favorites?notify=true";
    private static final String URI_SAMSUNG_LAUNCER = "content://com.sec.android.app.twlauncher.settings/favorites?notify=true";
    private static int _SCREEN_CONTENT_HEIGHT;
    private static int _SCREEN_HEIGHT;
    private static int _SCREEN_STATUSBAR_HEIGHT;
    private static int _SCREEN_WIDTH;
    private static int[] _blackpixels;
    private static int[] _pixels;

    public static class APKInfo {
        public String mAppName;
        public Drawable mIcon;
    }

    private static int[] GET_BLACK_LINE(int i) {
        int[] iArr = _blackpixels;
        if (iArr == null || iArr.length != i) {
            _blackpixels = new int[i];
            int i2 = 0;
            while (true) {
                int[] iArr2 = _blackpixels;
                if (i2 >= iArr2.length) {
                    break;
                }
                iArr2[i2] = -16777216;
                i2++;
            }
        }
        return _blackpixels;
    }

    private static int[] GET_WIHTE_LINE(int i) {
        int[] iArr = _pixels;
        if (iArr == null || iArr.length != i) {
            _pixels = new int[i];
            int i2 = 0;
            while (true) {
                int[] iArr2 = _pixels;
                if (i2 >= iArr2.length) {
                    break;
                }
                iArr2[i2] = -1;
                i2++;
            }
        }
        return _pixels;
    }

    public static int MESURE_SCREEN_CONTENT_HEIGHT(Activity activity) {
        if (_SCREEN_CONTENT_HEIGHT == 0) {
            Rect rect = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            _SCREEN_STATUSBAR_HEIGHT = rect.top;
            int height = rect.height();
            _SCREEN_CONTENT_HEIGHT = height;
            if (_SCREEN_STATUSBAR_HEIGHT < 0 || height > SCREEN_HEIGHT(activity)) {
                _SCREEN_STATUSBAR_HEIGHT = 0;
                _SCREEN_CONTENT_HEIGHT = SCREEN_HEIGHT(activity);
            }
        }
        return _SCREEN_CONTENT_HEIGHT;
    }

    public static int MESURE_SCREEN_STATUSBAR_HEIGHT(Activity activity) {
        if (_SCREEN_STATUSBAR_HEIGHT == 0) {
            Rect rect = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            _SCREEN_STATUSBAR_HEIGHT = rect.top;
            int height = rect.height();
            _SCREEN_CONTENT_HEIGHT = height;
            if (_SCREEN_STATUSBAR_HEIGHT < 0 || height > SCREEN_HEIGHT(activity)) {
                _SCREEN_STATUSBAR_HEIGHT = 0;
                _SCREEN_CONTENT_HEIGHT = SCREEN_HEIGHT(activity);
            }
        }
        return _SCREEN_STATUSBAR_HEIGHT;
    }

    public static void RESET_H_W() {
        _SCREEN_WIDTH = 0;
        _SCREEN_HEIGHT = 0;
        _SCREEN_STATUSBAR_HEIGHT = 0;
        _SCREEN_CONTENT_HEIGHT = 0;
        _pixels = null;
    }

    public static int SCREEN_HEIGHT(Context context) {
        if (_SCREEN_HEIGHT == 0) {
            _SCREEN_HEIGHT = context.getResources().getDisplayMetrics().heightPixels;
        }
        return _SCREEN_HEIGHT;
    }

    public static int SCREEN_WIDTH(Context context) {
        if (_SCREEN_WIDTH == 0) {
            _SCREEN_WIDTH = context.getResources().getDisplayMetrics().widthPixels;
        }
        return _SCREEN_WIDTH;
    }

    public static Bitmap captureView(View view) {
        return captureView(view, true, true, (Rect) null, "ARGB");
    }

    public static boolean checkClass(String str) {
        try {
            Class.forName(str);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean checkGTAndYoumeng() {
        return checkClass("io.dcloud.feature.apsGt.GTPushService") || checkClass("io.dcloud.feature.statistics.UmengStatisticsMgr");
    }

    /* JADX INFO: finally extract failed */
    public static boolean checkLauncherScreenSpace(Context context) {
        Cursor query = context.getContentResolver().query(Uri.parse(URI_SAMSUNG_LAUNCER), new String[]{"screen", "spanX", "spanY"}, (String) null, (String[]) null, (String) null);
        if (query == null) {
            return true;
        }
        int queryMaxLauncherScreenCount = queryMaxLauncherScreenCount(context);
        Logger.e("PlatformUtil", "Samsung Launcher: max screen num = " + queryMaxLauncherScreenCount);
        int columnIndexOrThrow = query.getColumnIndexOrThrow("spanX");
        int columnIndexOrThrow2 = query.getColumnIndexOrThrow("spanY");
        int i = queryMaxLauncherScreenCount * MAX_SPAN_IN_ONE_SCREEN;
        while (query.moveToNext()) {
            try {
                i -= query.getInt(columnIndexOrThrow) * query.getInt(columnIndexOrThrow2);
            } catch (Exception e) {
                Logger.e("PlatformUtil", "Check Launcher space" + e);
                query.close();
                i = 0;
            } catch (Throwable th) {
                query.close();
                throw th;
            }
        }
        query.close();
        if (i > 0) {
            return true;
        }
        return false;
    }

    public static void createShortut(Context context, String str, String str2, int i, boolean z) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setClassName(context, str2);
        Intent intent2 = new Intent(ACTION_INSTALL_SHORTCUT);
        intent2.putExtra("android.intent.extra.shortcut.INTENT", intent);
        intent2.putExtra("android.intent.extra.shortcut.NAME", str);
        intent2.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent.ShortcutIconResource.fromContext(context, i));
        intent2.putExtra(EXTRA_SHORTCUT_DUPLICATE, z);
        context.sendBroadcast(intent2);
    }

    public static void destroyDrawingCache(View view) {
        view.destroyDrawingCache();
    }

    public static void disableWebViewMultiProcess(Context context) {
        Object invokeFieldValue;
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                if (Settings.Global.getInt(context.getContentResolver(), "webview_multiprocess", 0) == 1) {
                    Object invokeFieldValue2 = invokeFieldValue(Settings.Global.class.getName(), "sNameValueCache", Settings.Global.class.newInstance());
                    if (invokeFieldValue2 != null && (invokeFieldValue = invokeFieldValue(invokeFieldValue2.getClass().getName(), "mValues", invokeFieldValue2)) != null && (invokeFieldValue instanceof HashMap)) {
                        ((HashMap) invokeFieldValue).put("webview_multiprocess", WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static APKInfo getApkFileInfo(Context context, String str) {
        String str2;
        APKInfo aPKInfo = new APKInfo();
        Drawable drawable = null;
        try {
            PackageInfo packageArchiveInfo = context.getPackageManager().getPackageArchiveInfo(str, 1);
            if (packageArchiveInfo != null) {
                ApplicationInfo applicationInfo = packageArchiveInfo.applicationInfo;
                if (applicationInfo != null) {
                    Class<?> cls = Class.forName("android.content.res.AssetManager");
                    AssetManager assetManager = (AssetManager) cls.getConstructor(new Class[0]).newInstance(new Object[0]);
                    cls.getDeclaredMethod("addAssetPath", new Class[]{String.class}).invoke(assetManager, new Object[]{str});
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    displayMetrics.setToDefaults();
                    Resources resources = new Resources(assetManager, displayMetrics, context.getResources().getConfiguration());
                    int i = applicationInfo.icon;
                    if (i == 0) {
                        str2 = null;
                    } else {
                        str2 = (String) resources.getText(applicationInfo.labelRes);
                        try {
                            drawable = resources.getDrawable(i);
                        } catch (Throwable th) {
                            th = th;
                            th.printStackTrace();
                            aPKInfo.mIcon = drawable;
                            aPKInfo.mAppName = str2;
                            return aPKInfo;
                        }
                    }
                    aPKInfo.mIcon = drawable;
                    aPKInfo.mAppName = str2;
                    return aPKInfo;
                }
            }
            return aPKInfo;
        } catch (Throwable th2) {
            th = th2;
            str2 = null;
            th.printStackTrace();
            aPKInfo.mIcon = drawable;
            aPKInfo.mAppName = str2;
            return aPKInfo;
        }
    }

    private static ActivityInfo getBestActivityInfo(List<ResolveInfo> list, LinkedList<String> linkedList) {
        LinkedList linkedList2 = new LinkedList();
        for (int i = 0; i < list.size(); i++) {
            ResolveInfo resolveInfo = list.get(i);
            if (linkedList.contains(resolveInfo.activityInfo.packageName)) {
                linkedList2.add(resolveInfo.activityInfo);
            }
        }
        int i2 = Integer.MAX_VALUE;
        ActivityInfo activityInfo = null;
        for (int i3 = 0; i3 < linkedList2.size(); i3++) {
            ActivityInfo activityInfo2 = (ActivityInfo) linkedList2.get(i3);
            int indexOf = linkedList.indexOf(activityInfo2.packageName);
            if (indexOf < i2) {
                activityInfo = activityInfo2;
                i2 = indexOf;
            }
        }
        return activityInfo;
    }

    public static byte[] getFileContent(String str, int i) {
        InputStream inputStream = getInputStream(str, i);
        byte[] bArr = null;
        if (inputStream != null) {
            try {
                bArr = IOUtil.getBytes(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
                IOUtil.close(inputStream);
            } catch (Throwable th) {
                IOUtil.close(inputStream);
                throw th;
            }
        }
        IOUtil.close(inputStream);
        return bArr;
    }

    public static String getFileContent4S(String str) {
        return new String(getFileContent(str, 0)).replace(ContextChain.TAG_PRODUCT, "");
    }

    public static String getFilePathFromContentUri(Uri uri, ContentResolver contentResolver) {
        String[] strArr = {"_data"};
        Cursor query = contentResolver.query(uri, strArr, (String) null, (String[]) null, (String) null);
        query.moveToFirst();
        String string = query.getString(query.getColumnIndex(strArr[0]));
        query.close();
        return string;
    }

    public static InputStream getInputStream(String str, int i) {
        try {
            String str2 = DeviceInfo.sDeviceRootDir;
            if (str2 != null && str.startsWith(str2)) {
                i = 2;
            }
            if (i == 0) {
                return getResInputStream(str);
            }
            if (i == 1) {
                return PlatformUtil.class.getClassLoader().getResourceAsStream(str);
            }
            if (i == 2) {
                File file = new File(str);
                if (file.exists()) {
                    return FileUtil.getFileInputStream(DCLoudApplicationImpl.self().getContext(), file);
                }
            }
            return null;
        } catch (Exception e) {
            Logger.e(RenderTypes.RENDER_TYPE_NATIVE, e.toString());
        }
    }

    public static InputStream getResInputStream(String str) {
        try {
            str = useAndroidPath(str);
            if (!TextUtils.isEmpty(str)) {
                if (str.startsWith("assets://")) {
                    str = str.replace("assets://", "");
                } else if (str.startsWith("android_asset/")) {
                    str = str.replace("android_asset/", "");
                } else if (str.startsWith(SDK.ANDROID_ASSET)) {
                    str = str.replace(SDK.ANDROID_ASSET, "");
                }
            }
            return AndroidResources.sAssetMgr.open(str, 2);
        } catch (RuntimeException unused) {
            Logger.e("PlatformUtil.getResInputStream RuntimeException pFilePath=" + str);
            return null;
        } catch (FileNotFoundException unused2) {
            Logger.e("PlatformUtil.getResInputStream FileNotFoundException pFilePath=" + str);
            return null;
        } catch (IOException unused3) {
            Logger.e("PlatformUtil.getResInputStream IOException pFilePath=" + str);
            return null;
        } catch (Exception unused4) {
            Logger.e("PlatformUtil.getResInputStream Exception pFilePath=" + str);
            return null;
        }
    }

    public static boolean hasAppInstalled(Context context, String str) {
        try {
            context.getPackageManager().getPackageInfo(str, 1);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return new File("/sdcard/Android/data/" + str).exists();
        }
    }

    public static void init(Context context) {
        DeviceInfo.sApplicationContext = context;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0039, code lost:
        io.dcloud.common.adapter.util.Logger.i(com.taobao.weex.common.RenderTypes.RENDER_TYPE_NATIVE, r3.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0041, code lost:
        io.dcloud.common.adapter.util.Logger.i(com.taobao.weex.common.RenderTypes.RENDER_TYPE_NATIVE, r3.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000a, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x000c, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0029 */
    /* JADX WARNING: Removed duplicated region for block: B:4:0x000a A[Catch:{ ClassNotFoundException -> 0x000c, all -> 0x000a }, ExcHandler: all (r3v11 'th' java.lang.Throwable A[CUSTOM_DECLARE, Catch:{ ClassNotFoundException -> 0x000c, all -> 0x000a }]), Splitter:B:2:0x0005] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object invokeFieldValue(java.lang.String r3, java.lang.String r4, java.lang.Object r5) {
        /*
            java.lang.String r0 = "platform"
            r1 = 0
            if (r5 == 0) goto L_0x000e
            java.lang.Class r2 = r5.getClass()     // Catch:{ ClassNotFoundException -> 0x000c, all -> 0x000a }
            goto L_0x000f
        L_0x000a:
            r3 = move-exception
            goto L_0x0039
        L_0x000c:
            r3 = move-exception
            goto L_0x0041
        L_0x000e:
            r2 = r1
        L_0x000f:
            if (r2 != 0) goto L_0x0015
            java.lang.Class r2 = java.lang.Class.forName(r3)     // Catch:{ ClassNotFoundException -> 0x000c, all -> 0x000a }
        L_0x0015:
            r3 = r1
        L_0x0016:
            if (r2 == 0) goto L_0x002e
            java.lang.reflect.Field r3 = r2.getField(r4)     // Catch:{ Exception -> 0x001f, all -> 0x000a }
            if (r3 == 0) goto L_0x0020
            goto L_0x002e
        L_0x001f:
        L_0x0020:
            if (r3 != 0) goto L_0x0029
            java.lang.reflect.Field r3 = r2.getDeclaredField(r4)     // Catch:{ Exception -> 0x0029, all -> 0x000a }
            if (r3 == 0) goto L_0x0029
            goto L_0x002e
        L_0x0029:
            java.lang.Class r2 = r2.getSuperclass()     // Catch:{ ClassNotFoundException -> 0x000c, all -> 0x000a }
            goto L_0x0016
        L_0x002e:
            if (r3 == 0) goto L_0x0048
            r4 = 1
            r3.setAccessible(r4)     // Catch:{ ClassNotFoundException -> 0x000c, all -> 0x000a }
            java.lang.Object r3 = r3.get(r5)     // Catch:{ ClassNotFoundException -> 0x000c, all -> 0x000a }
            return r3
        L_0x0039:
            java.lang.String r3 = r3.toString()
            io.dcloud.common.adapter.util.Logger.i(r0, r3)
            goto L_0x0048
        L_0x0041:
            java.lang.String r3 = r3.toString()
            io.dcloud.common.adapter.util.Logger.i(r0, r3)
        L_0x0048:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.util.PlatformUtil.invokeFieldValue(java.lang.String, java.lang.String, java.lang.Object):java.lang.Object");
    }

    public static Object invokeMethod(String str, String str2) {
        return invokeMethod(str, str2, (Object) null, new Class[0], new Object[0]);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:(3:8|9|(1:24))|(3:13|14|(1:26))|16|17) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:16:0x001e */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean invokeSetFieldValue(java.lang.Object r3, java.lang.String r4, java.lang.Object r5) {
        /*
            r0 = 0
            if (r3 != 0) goto L_0x0004
            return r0
        L_0x0004:
            java.lang.Class r1 = r3.getClass()     // Catch:{ all -> 0x002d }
            if (r1 == 0) goto L_0x0037
            r2 = 0
        L_0x000b:
            if (r1 == 0) goto L_0x0023
            java.lang.reflect.Field r2 = r1.getField(r4)     // Catch:{ Exception -> 0x0014 }
            if (r2 == 0) goto L_0x0015
            goto L_0x0023
        L_0x0014:
        L_0x0015:
            if (r2 != 0) goto L_0x001e
            java.lang.reflect.Field r2 = r1.getDeclaredField(r4)     // Catch:{ Exception -> 0x001e }
            if (r2 == 0) goto L_0x001e
            goto L_0x0023
        L_0x001e:
            java.lang.Class r1 = r1.getSuperclass()     // Catch:{ all -> 0x002d }
            goto L_0x000b
        L_0x0023:
            if (r2 == 0) goto L_0x0037
            r4 = 1
            r2.setAccessible(r4)     // Catch:{ all -> 0x002d }
            r2.set(r3, r5)     // Catch:{ all -> 0x002d }
            return r4
        L_0x002d:
            r3 = move-exception
            java.lang.String r3 = r3.toString()
            java.lang.String r4 = "platform"
            io.dcloud.common.adapter.util.Logger.i(r4, r3)
        L_0x0037:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.util.PlatformUtil.invokeSetFieldValue(java.lang.Object, java.lang.String, java.lang.Object):boolean");
    }

    public static boolean isAppInstalled(Context context, String str) {
        return LoadAppUtils.isAppLoad(context, str);
    }

    public static boolean isEmulator() {
        String str = Build.MODEL;
        return str.equals("sdk") || str.equals("google_sdk");
    }

    public static boolean isLineWhiteBitmap(Bitmap bitmap, boolean z) {
        int width = bitmap.getWidth();
        int[] iArr = new int[width];
        bitmap.getPixels(iArr, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), 1);
        boolean equals = Arrays.equals(iArr, GET_WIHTE_LINE(width));
        return z ? equals | Arrays.equals(iArr, GET_BLACK_LINE(width)) : equals;
    }

    private static boolean isPureColor(int[] iArr) {
        int i = iArr[0];
        boolean z = true;
        for (int i2 : iArr) {
            if (i != i2) {
                z = false;
            }
        }
        return z;
    }

    public static boolean isResFileExists(String str) {
        try {
            InputStream open = AndroidResources.sAssetMgr.open(str);
            if (open == null) {
                return false;
            }
            open.close();
            return true;
        } catch (IOException unused) {
            return false;
        }
    }

    public static boolean isWhiteBitmap(Bitmap bitmap) {
        return isWhiteBitmap(bitmap, false, false);
    }

    public static void launchApplication(Context context, String str, String str2, HashMap hashMap, boolean z) throws Exception {
        Intent intent;
        if (PdrUtil.isEmpty(str2)) {
            intent = new Intent("android.intent.action.MAIN");
        } else {
            intent = new Intent(str2);
        }
        if (PdrUtil.isEmpty(str) || setPackageName(context, intent, str)) {
            if (z) {
                intent.setFlags(268435456);
            }
            if (hashMap != null && !hashMap.isEmpty()) {
                for (String str3 : hashMap.keySet()) {
                    Object obj = hashMap.get(str3);
                    if (obj instanceof Integer) {
                        intent.putExtra(str3, ((Integer) obj).intValue());
                    } else if (obj instanceof String) {
                        intent.putExtra(str3, (String) obj);
                    } else if (obj instanceof Boolean) {
                        intent.putExtra(str3, ((Boolean) obj).booleanValue());
                    }
                }
            }
            context.startActivity(intent);
            return;
        }
        throw new RuntimeException();
    }

    public static String[] listFsAppsFiles(String str) {
        try {
            return new File(str).list();
        } catch (Exception e) {
            Logger.w("PlatformUtil.listResFiles pPath=" + str, e);
            return null;
        }
    }

    public static String[] listResFiles(String str) {
        try {
            return AndroidResources.sAssetMgr.list(useAndroidPath(str));
        } catch (IOException e) {
            Logger.w("PlatformUtil.listResFiles pPath=" + str, e);
            return null;
        }
    }

    public static Object newInstance(String str, Class[] clsArr, Object[] objArr) {
        try {
            return Class.forName(str).getConstructor(clsArr).newInstance(objArr);
        } catch (Exception e) {
            Logger.i(RenderTypes.RENDER_TYPE_NATIVE, e.toString());
            return null;
        } catch (Throwable th) {
            Logger.i(RenderTypes.RENDER_TYPE_NATIVE, th.toString());
            return null;
        }
    }

    public static void openFileBySystem(Context context, String str, String str2, String str3, ICallBack iCallBack) {
        if (Build.VERSION.SDK_INT >= 24) {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        }
        boolean hasAppInstalled = TextUtils.isEmpty(str2) ^ true ? hasAppInstalled(context, str2) : false;
        try {
            String mimeType = PdrUtil.getMimeType(str);
            if (str.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                str = str.substring(7);
            }
            if (str.startsWith("content://")) {
                str = getFilePathFromContentUri(Uri.parse(str), context.getContentResolver());
                mimeType = PdrUtil.getMimeType(str);
            }
            if (PdrUtil.isEmpty(str3)) {
                str3 = mimeType;
            }
            Intent dataAndTypeIntent = LoadAppUtils.getDataAndTypeIntent(context, str, str3);
            if (hasAppInstalled) {
                dataAndTypeIntent.setPackage(str2);
            }
            if (new File(str).exists()) {
                context.startActivity(dataAndTypeIntent);
                if (iCallBack != null) {
                    iCallBack.onCallBack(1, "{}");
                }
            } else if (iCallBack != null) {
                iCallBack.onCallBack(-1, StringUtil.format(DOMException.JSON_ERROR_INFO, 0, DOMException.MSG_NOT_FOUND_FILE));
            }
        } catch (ActivityNotFoundException e) {
            Logger.w(e);
            if (iCallBack != null) {
                iCallBack.onCallBack(-1, StringUtil.format(DOMException.JSON_ERROR_INFO, 1, DOMException.MSG_NOT_FOUND_3TH));
            }
        }
    }

    public static void openURL(Context context, String str, String str2) throws Exception {
        Intent parseUri = Intent.parseUri(str, 0);
        if (!PdrUtil.isEmpty(str2)) {
            parseUri.setPackage(str2);
        }
        parseUri.setSelector((Intent) null);
        if (BaseInfo.isDefense) {
            parseUri.setComponent((ComponentName) null);
            parseUri.addCategory("android.intent.category.BROWSABLE");
        }
        parseUri.setFlags(268435456);
        context.startActivity(parseUri);
    }

    public static PackageInfo parseApkInfo(Context context, String str) throws Exception {
        return context.getPackageManager().getPackageArchiveInfo(str, 1);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002b, code lost:
        if (r1 != null) goto L_0x004d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004b, code lost:
        if (r1 == null) goto L_0x0050;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x004d, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0050, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean queryDefaultShortcut(java.lang.String r9) {
        /*
            r0 = 0
            r1 = 0
            android.content.Context r2 = io.dcloud.common.adapter.util.DeviceInfo.sApplicationContext     // Catch:{ Exception -> 0x0030 }
            android.content.ContentResolver r3 = r2.getContentResolver()     // Catch:{ Exception -> 0x0030 }
            java.lang.String r2 = "content://com.android.launcher2.settings/favorites?notify=false"
            android.net.Uri r4 = android.net.Uri.parse(r2)     // Catch:{ Exception -> 0x0030 }
            java.lang.String[] r5 = PROJECTION     // Catch:{ Exception -> 0x0030 }
            java.lang.String r6 = "title=?"
            r2 = 1
            java.lang.String[] r7 = new java.lang.String[r2]     // Catch:{ Exception -> 0x0030 }
            r7[r0] = r9     // Catch:{ Exception -> 0x0030 }
            r8 = 0
            android.database.Cursor r1 = r3.query(r4, r5, r6, r7, r8)     // Catch:{ Exception -> 0x0030 }
            if (r1 == 0) goto L_0x002b
            boolean r9 = r1.moveToFirst()     // Catch:{ Exception -> 0x0030 }
            if (r9 == 0) goto L_0x002b
            r1.close()     // Catch:{ Exception -> 0x0030 }
            r1.close()
            return r2
        L_0x002b:
            if (r1 == 0) goto L_0x0050
            goto L_0x004d
        L_0x002e:
            r9 = move-exception
            goto L_0x0051
        L_0x0030:
            r9 = move-exception
            java.lang.String r2 = "PlatformUtil"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x002e }
            r3.<init>()     // Catch:{ all -> 0x002e }
            java.lang.String r4 = "queryHTCShortCut error:"
            r3.append(r4)     // Catch:{ all -> 0x002e }
            java.lang.String r9 = r9.getMessage()     // Catch:{ all -> 0x002e }
            r3.append(r9)     // Catch:{ all -> 0x002e }
            java.lang.String r9 = r3.toString()     // Catch:{ all -> 0x002e }
            io.dcloud.common.adapter.util.Logger.e(r2, r9)     // Catch:{ all -> 0x002e }
            if (r1 == 0) goto L_0x0050
        L_0x004d:
            r1.close()
        L_0x0050:
            return r0
        L_0x0051:
            if (r1 == 0) goto L_0x0056
            r1.close()
        L_0x0056:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.util.PlatformUtil.queryDefaultShortcut(java.lang.String):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002b, code lost:
        if (r1 != null) goto L_0x004d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004b, code lost:
        if (r1 == null) goto L_0x0050;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x004d, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0050, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean queryHTCShortCut(java.lang.String r9) {
        /*
            r0 = 0
            r1 = 0
            android.content.Context r2 = io.dcloud.common.adapter.util.DeviceInfo.sApplicationContext     // Catch:{ Exception -> 0x0030 }
            android.content.ContentResolver r3 = r2.getContentResolver()     // Catch:{ Exception -> 0x0030 }
            java.lang.String r2 = "content://com.htc.launcher.settings/favorites?notify=true"
            android.net.Uri r4 = android.net.Uri.parse(r2)     // Catch:{ Exception -> 0x0030 }
            java.lang.String[] r5 = PROJECTION     // Catch:{ Exception -> 0x0030 }
            java.lang.String r6 = "title=?"
            r2 = 1
            java.lang.String[] r7 = new java.lang.String[r2]     // Catch:{ Exception -> 0x0030 }
            r7[r0] = r9     // Catch:{ Exception -> 0x0030 }
            r8 = 0
            android.database.Cursor r1 = r3.query(r4, r5, r6, r7, r8)     // Catch:{ Exception -> 0x0030 }
            if (r1 == 0) goto L_0x002b
            boolean r9 = r1.moveToFirst()     // Catch:{ Exception -> 0x0030 }
            if (r9 == 0) goto L_0x002b
            r1.close()     // Catch:{ Exception -> 0x0030 }
            r1.close()
            return r2
        L_0x002b:
            if (r1 == 0) goto L_0x0050
            goto L_0x004d
        L_0x002e:
            r9 = move-exception
            goto L_0x0051
        L_0x0030:
            r9 = move-exception
            java.lang.String r2 = "PlatformUtil"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x002e }
            r3.<init>()     // Catch:{ all -> 0x002e }
            java.lang.String r4 = "queryHTCShortCut error:"
            r3.append(r4)     // Catch:{ all -> 0x002e }
            java.lang.String r9 = r9.getMessage()     // Catch:{ all -> 0x002e }
            r3.append(r9)     // Catch:{ all -> 0x002e }
            java.lang.String r9 = r3.toString()     // Catch:{ all -> 0x002e }
            io.dcloud.common.adapter.util.Logger.e(r2, r9)     // Catch:{ all -> 0x002e }
            if (r1 == 0) goto L_0x0050
        L_0x004d:
            r1.close()
        L_0x0050:
            return r0
        L_0x0051:
            if (r1 == 0) goto L_0x0056
            r1.close()
        L_0x0056:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.util.PlatformUtil.queryHTCShortCut(java.lang.String):boolean");
    }

    private static int queryMaxLauncherScreenCount(Context context) {
        Cursor query = context.getContentResolver().query(Uri.parse(URI_SAMSUNG_LAUNCER), new String[]{"MAX(screen)"}, (String) null, (String[]) null, (String) null);
        if (query != null) {
            try {
                query.moveToNext();
                return query.getInt(0) + 1;
            } catch (Exception e) {
                Logger.e("PlatformUtil", "Samsung Launcher" + e);
            } finally {
                query.close();
            }
        }
        return -1;
    }

    public static boolean setPackageName(Context context, Intent intent, String str) {
        Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(str);
        if (launchIntentForPackage == null) {
            return false;
        }
        intent.setClassName(str, launchIntentForPackage.getComponent().getClassName());
        return true;
    }

    private static String useAndroidPath(String str) {
        return StringUtil.trimString(StringUtil.trimString(str, '/'), '\\');
    }

    public void delShortcut(String str, String str2, String str3) {
        Intent intent = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        intent.putExtra("android.intent.extra.shortcut.NAME", str);
        Intent intent2 = new Intent("android.intent.action.MAIN");
        intent2.addCategory("android.intent.category.DEFAULT");
        intent2.setComponent(new ComponentName(str2, str3));
        intent.putExtra("android.intent.extra.shortcut.INTENT", intent2);
        DeviceInfo.sApplicationContext.sendBroadcast(intent);
    }

    public static Bitmap captureView(View view, boolean z, boolean z2, Rect rect, String str) {
        if (z2 && AndroidResources.sIMEAlive) {
            return null;
        }
        int width = rect != null ? rect.width() : view.getMeasuredWidth();
        int height = rect != null ? rect.height() : view.getMeasuredHeight();
        if (width == 0) {
            return null;
        }
        Bitmap.Config config = Bitmap.Config.RGB_565;
        if (str.equals("ARGB")) {
            config = Bitmap.Config.ARGB_4444;
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(createBitmap);
        if (rect != null) {
            canvas.translate((float) (-rect.left), (float) (-rect.top));
        }
        view.draw(canvas);
        if (!z || !isWhiteBitmap(createBitmap)) {
            return createBitmap;
        }
        createBitmap.recycle();
        return null;
    }

    public static Object invokeMethod(String str, String str2, Object obj) {
        return invokeMethod(str, str2, obj, new Class[0], new Object[0]);
    }

    public static boolean isWhiteBitmap(Bitmap bitmap, boolean z, boolean z2) {
        int height = bitmap.getHeight();
        int[] iArr = new int[height];
        bitmap.getPixels(iArr, 0, 1, bitmap.getWidth() / 4, 0, 1, height);
        boolean equals = Arrays.equals(iArr, GET_WIHTE_LINE(height));
        if (z2) {
            equals = isPureColor(iArr);
        }
        bitmap.getPixels(iArr, 0, 1, bitmap.getWidth() / 2, 0, 1, height);
        boolean equals2 = Arrays.equals(iArr, GET_WIHTE_LINE(height)) & equals;
        if (z2) {
            equals2 = isPureColor(iArr);
        }
        bitmap.getPixels(iArr, 0, 1, (bitmap.getWidth() * 3) / 4, 0, 1, height);
        boolean equals3 = Arrays.equals(iArr, GET_WIHTE_LINE(height)) & equals2;
        if (z2) {
            equals3 = isPureColor(iArr);
        }
        return z ? equals3 | Arrays.equals(iArr, GET_BLACK_LINE(height)) : equals3;
    }

    public static Object invokeMethod(String str, String str2, Object obj, Class[] clsArr, Object[] objArr) {
        Object obj2;
        String str3;
        String str4 = null;
        try {
            Method method = Class.forName(str).getMethod(str2, clsArr);
            if (method != null) {
                method.setAccessible(true);
                obj2 = method.invoke(obj, objArr);
            } else {
                obj2 = null;
            }
        } catch (ClassNotFoundException unused) {
            str3 = "ClassNotFoundException";
            str4 = str3;
            obj2 = null;
            Logger.i(RenderTypes.RENDER_TYPE_NATIVE, str4 + Operators.SPACE_STR + str + Operators.SPACE_STR + str2);
            return obj2;
        } catch (NoSuchMethodException unused2) {
            str3 = "NoSuchMethodException";
            str4 = str3;
            obj2 = null;
            Logger.i(RenderTypes.RENDER_TYPE_NATIVE, str4 + Operators.SPACE_STR + str + Operators.SPACE_STR + str2);
            return obj2;
        } catch (Exception e) {
            str3 = e.getMessage();
            str4 = str3;
            obj2 = null;
            Logger.i(RenderTypes.RENDER_TYPE_NATIVE, str4 + Operators.SPACE_STR + str + Operators.SPACE_STR + str2);
            return obj2;
        }
        if (str4 != null && !"getJsContent".equals(str2)) {
            Logger.i(RenderTypes.RENDER_TYPE_NATIVE, str4 + Operators.SPACE_STR + str + Operators.SPACE_STR + str2);
        }
        return obj2;
    }

    public static void createShortut(Context context, String str, String str2, Bitmap bitmap, boolean z) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setClassName(context, str2);
        Intent intent2 = new Intent(ACTION_INSTALL_SHORTCUT);
        intent2.putExtra("android.intent.extra.shortcut.INTENT", intent);
        intent2.putExtra("android.intent.extra.shortcut.NAME", str);
        intent2.putExtra("android.intent.extra.shortcut.ICON", bitmap);
        intent2.putExtra(EXTRA_SHORTCUT_DUPLICATE, z);
        context.sendBroadcast(intent2);
    }

    public static InputStream getInputStream(String str) {
        if (str == null) {
            return null;
        }
        return getInputStream(str, PdrUtil.isDeviceRootDir(str) ? 2 : 0);
    }

    public static Object invokeMethod(Object obj, String str, Class<?>[] clsArr, Object... objArr) {
        Method method;
        if (obj == null) {
            return null;
        }
        try {
            Class<?> cls = obj.getClass();
            if (Build.VERSION.SDK_INT > 10) {
                method = cls.getMethod(str, clsArr);
            } else {
                method = cls.getDeclaredMethod(str, clsArr);
            }
            method.setAccessible(true);
            if (objArr.length == 0) {
                objArr = null;
            }
            return method.invoke(obj, objArr);
        } catch (Throwable th) {
            Logger.i(RenderTypes.RENDER_TYPE_NATIVE, th.toString());
            return null;
        }
    }

    public static Bitmap captureView(View view, String str) {
        return captureView(view, (Rect) null, str);
    }

    public static Bitmap captureView(View view, Rect rect, String str) {
        try {
            int width = view.getWidth();
            int height = view.getHeight();
            boolean z = rect != null;
            if (z) {
                int i = rect.left;
                int i2 = rect.top;
                view.layout(i, i2, rect.right - i, rect.bottom - i2);
            } else {
                view.layout(0, 0, width, height);
            }
            view.setDrawingCacheEnabled(true);
            Bitmap createBitmap = Bitmap.createBitmap(view.getDrawingCache());
            createBitmap.setDensity(view.getContext().getResources().getDisplayMetrics().densityDpi);
            if (!PdrUtil.isEmpty(str)) {
                PdrUtil.saveBitmapToFile(createBitmap, str);
            }
            view.setDrawingCacheEnabled(false);
            if (z) {
                view.layout(0, 0, width, height);
            }
            return createBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
