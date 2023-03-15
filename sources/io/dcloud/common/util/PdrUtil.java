package io.dcloud.common.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dcloud.android.widget.toast.ToastCompat;
import com.nostra13.dcloudimageloader.core.download.ImageDownloader;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.performance.WXInstanceApm;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.util.XmlUtil;
import io.dcloud.f.a;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PdrUtil {
    public static final String FILE_PATH_ENTRY_BACK = "..";
    public static final String FILE_PATH_ENTRY_SEPARATOR2 = "%";
    private static final String NAVIGATION = "navigationBarBackground";
    private static volatile boolean mHasCheckAllScreen;
    private static volatile boolean mIsAllScreenDevice;

    public static void alert(Activity activity, String str, Bitmap bitmap) {
        final AlertDialog create = new AlertDialog.Builder(activity).create();
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(1);
        linearLayout.setGravity(17);
        TextView textView = new TextView(activity);
        textView.setText(str);
        linearLayout.addView(textView);
        ImageView imageView = new ImageView(activity);
        imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
        linearLayout.addView(imageView, new ViewGroup.LayoutParams(-2, -2));
        create.setView(linearLayout);
        create.setCanceledOnTouchOutside(false);
        create.setButton(activity.getString(R.string.dcloud_common_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                create.dismiss();
            }
        });
        create.show();
    }

    public static String appendByDeviceRootDir(String str) {
        if (str == null || str.startsWith(DeviceInfo.sDeviceRootDir)) {
            return str;
        }
        if (str.startsWith(DeviceInfo.FILE_PROTOCOL)) {
            str = str.substring(7);
        }
        if (str.indexOf("sdcard/") > -1) {
            str = str.substring(str.indexOf("sdcard/") + 7);
        }
        if (!str.endsWith("/")) {
            str = str + "/";
        }
        return DeviceInfo.sDeviceRootDir + "/" + str;
    }

    public static boolean checkAlphaTransparent(int i) {
        return (i == -1 || (i >>> 24) == 255) ? false : true;
    }

    public static boolean checkIntl() {
        return PlatformUtil.checkClass("io.dcloud.common.DHInterface.IntlCallback");
    }

    public static boolean checkStatusbarColor(int i) {
        String str = Build.BRAND;
        if (str.equals(MobilePhoneModel.GOOGLE)) {
            str = Build.MANUFACTURER;
        }
        if ((Build.VERSION.SDK_INT >= 23 || str.equalsIgnoreCase(MobilePhoneModel.XIAOMI) || str.equalsIgnoreCase(MobilePhoneModel.MEIZU)) && !str.equalsIgnoreCase(MobilePhoneModel.DUOWEI)) {
            return true;
        }
        int i2 = (i >> 16) & 255;
        int i3 = (i >> 8) & 255;
        int i4 = i & 255;
        if (i2 >= 30 || i3 >= 30 || i4 >= 30) {
            return i2 <= 235 || i3 <= 235 || i4 <= 235;
        }
        return false;
    }

    public static void closeAndroidPDialog() {
        if (Build.VERSION.SDK_INT >= 28) {
            try {
                PlatformUtil.invokeSetFieldValue(PlatformUtil.invokeMethod(Base64.decode2String("YW5kcm9pZC5hcHAuQWN0aXZpdHlUaHJlYWQ="), "currentActivityThread"), "mHiddenApiWarningShown", Boolean.TRUE);
            } catch (Exception e) {
                Logger.e("PdrUtil", "closeAndroidPDialog--" + e.getMessage());
            }
        }
    }

    public static String convertAppPath(IWebview iWebview, String str) {
        IApp obtainApp = iWebview.obtainApp();
        if (isNetPath(str) || str.startsWith("file:") || str.contains("/storage") || obtainApp.obtainRunningAppMode() != 1) {
            return obtainApp.convert2WebviewFullPath(iWebview.obtainFullUrl(), str);
        }
        String convert2AbsFullPath = iWebview.obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), str);
        if (convert2AbsFullPath.startsWith("/")) {
            convert2AbsFullPath = convert2AbsFullPath.substring(1, convert2AbsFullPath.length());
        }
        if (convert2AbsFullPath.contains("android_asset/")) {
            convert2AbsFullPath = convert2AbsFullPath.replace("android_asset/", "");
        }
        return ImageDownloader.Scheme.ASSETS.wrap(convert2AbsFullPath);
    }

    public static int convertToScreenInt(String str, int i, int i2, float f) {
        return convertToScreenInt(str, i, i2, f, false);
    }

    public static String dealString(String str) throws UnsupportedEncodingException {
        byte[] bytes = str.getBytes("GBK");
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (bytes[i] ^ 8);
        }
        return new String(bytes, "GBK");
    }

    public static float dpiFromPx(int i, DisplayMetrics displayMetrics) {
        return ((float) i) / (((float) displayMetrics.densityDpi) / 160.0f);
    }

    public static String encodeURL(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException unused) {
            Logger.e("URLEncode error str=" + str);
            return URLEncoder.encode(str);
        }
    }

    public static JSONObject getConfigData(Context context, String str, String str2, boolean z) {
        InputStream inputStream;
        if (z) {
            try {
                inputStream = PlatformUtil.getResInputStream(str2);
            } catch (Exception unused) {
                return null;
            }
        } else {
            inputStream = DHFile.getInputStream(DHFile.createFileHandler(str2));
        }
        if (inputStream == null) {
            return null;
        }
        byte[] bytes = IOUtil.getBytes(inputStream);
        String a = a.a(context, bytes);
        if (TextUtils.isEmpty(a)) {
            a = new String(bytes);
        }
        return new JSONObject(a);
    }

    public static int getConfigOrientation(JSONObject jSONObject) throws JSONException {
        if (jSONObject == null || !jSONObject.has("screenOrientation")) {
            return 2;
        }
        JSONArray optJSONArray = jSONObject.optJSONArray("screenOrientation");
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        for (int i = 0; i < optJSONArray.length(); i++) {
            String string = optJSONArray.getString(i);
            string.hashCode();
            string.hashCode();
            char c = 65535;
            switch (string.hashCode()) {
                case -1228021296:
                    if (string.equals("portrait-primary")) {
                        c = 0;
                        break;
                    }
                    break;
                case -147105566:
                    if (string.equals("landscape-secondary")) {
                        c = 1;
                        break;
                    }
                    break;
                case 1862465776:
                    if (string.equals("landscape-primary")) {
                        c = 2;
                        break;
                    }
                    break;
                case 2012187074:
                    if (string.equals("portrait-secondary")) {
                        c = 3;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    z3 = true;
                    break;
                case 1:
                    z2 = true;
                    break;
                case 2:
                    z = true;
                    break;
                case 3:
                    z4 = true;
                    break;
            }
        }
        if ((z || z2) && (z3 || z4)) {
            return 2;
        }
        if (z && z2) {
            return 6;
        }
        if (z3 && z4) {
            return 7;
        }
        if (z) {
            return 0;
        }
        if (z2) {
            return 8;
        }
        if (z3) {
            return 1;
        }
        if (z4) {
            return 9;
        }
        return 2;
    }

    public static String getDefaultPrivateDocPath(String str, String str2) {
        if (isEmpty(str)) {
            str = AbsoluteConst.MINI_SERVER_APP_DOC + System.currentTimeMillis();
        } else if (str.endsWith("/")) {
            str = str + System.currentTimeMillis();
        }
        if (str.endsWith(Operators.DOT_STR + str2)) {
            return str;
        }
        return str + Operators.DOT_STR + str2;
    }

    public static String getDownloadFilename(String str, String str2, String str3) {
        String[] stringSplit;
        String[] stringSplit2;
        String str4 = null;
        try {
            str3 = URLDecoder.decode(str3, "utf-8");
            Uri parse = Uri.parse(str3);
            if (parse != null) {
                String path = parse.getPath();
                if (!isEmpty(path)) {
                    str3 = path;
                }
            }
            if (!isEmpty(str) && (stringSplit = stringSplit(str, ";")) != null) {
                int i = 0;
                while (true) {
                    if (i >= stringSplit.length) {
                        break;
                    }
                    if (!(stringSplit[i] == null || !stringSplit[i].contains(AbsoluteConst.JSON_KEY_FILENAME) || (stringSplit2 = stringSplit(stringSplit[i].trim(), "=")) == null)) {
                        String replace = stringSplit2[0].replace(JSUtil.QUOTE, "");
                        String replace2 = stringSplit2[1].replace(JSUtil.QUOTE, "");
                        if (!isEmpty(stringSplit2[1]) && isEquals(AbsoluteConst.JSON_KEY_FILENAME, replace) && !isEmpty(replace2)) {
                            str4 = replace2;
                            break;
                        }
                    }
                    i++;
                }
            }
        } catch (Exception unused) {
            Logger.d("PdrUtil.getDownloadFilename " + str + " not found filename");
        }
        if (isEmpty(str4)) {
            int lastIndexOf = str3.lastIndexOf(47);
            if (lastIndexOf <= 0 || lastIndexOf >= str3.length() - 1) {
                str4 = String.valueOf(System.currentTimeMillis());
            } else {
                str4 = str3.substring(lastIndexOf + 1);
                int indexOf = str4.indexOf(Operators.CONDITION_IF_STRING);
                if (indexOf > 0) {
                    if (indexOf < str4.length() - 1) {
                        str4 = str4.substring(0, indexOf);
                    } else {
                        str4 = String.valueOf(System.currentTimeMillis());
                    }
                }
            }
        }
        if (str4.indexOf(Operators.DOT_STR) < 0) {
            String extensionFromMimeType = getExtensionFromMimeType(str2);
            if (!isEmpty(extensionFromMimeType)) {
                str4 = str4 + Operators.DOT_STR + extensionFromMimeType;
            }
        }
        try {
            String replaceAll = URLDecoder.decode(str4, "UTF-8").replaceAll(File.separator, "");
            if (replaceAll.contains(Operators.CONDITION_IF_STRING)) {
                replaceAll = replaceAll.replaceAll("\\?", WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
            }
            if (replaceAll.length() <= 80) {
                return replaceAll;
            }
            return replaceAll.substring(0, 80) + System.currentTimeMillis();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str4;
        }
    }

    public static String getExtensionFromMimeType(String str) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(str);
    }

    public static String getFileNameByUrl(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str2)) {
            Matcher matcher = Pattern.compile("[\\w\\.]+[\\.](" + "avi|mpeg|3gp|mp3|mp4|wav|jpeg|gif|jpg|png|apk|exe|pdf|rar|zip|docx|doc" + Operators.BRACKET_END_STR).matcher(str);
            if (matcher.find()) {
                return matcher.group();
            }
            if (!TextUtils.isEmpty(str3)) {
                return str3;
            }
            return String.valueOf(System.currentTimeMillis()) + ".dat";
        }
        String lastPathSegment = Uri.parse(str).getLastPathSegment();
        if (lastPathSegment != null && lastPathSegment.contains(str2)) {
            return lastPathSegment;
        }
        if (!TextUtils.isEmpty(str3)) {
            return str3;
        }
        return String.valueOf(System.currentTimeMillis()) + str2;
    }

    public static Object getKeyByIndex(HashMap hashMap, int i) {
        if (i < 0) {
            return null;
        }
        int i2 = 0;
        for (Object next : hashMap.keySet()) {
            if (i == i2) {
                return next;
            }
            i2++;
        }
        return null;
    }

    public static Object getKeyByValue(HashMap hashMap, Object obj, boolean z) {
        if (z && !hashMap.containsValue(obj)) {
            return null;
        }
        for (Object next : hashMap.keySet()) {
            Object obj2 = hashMap.get(next);
            if (obj2 != null && obj2.equals(obj)) {
                return next;
            }
        }
        return null;
    }

    public static String getMimeType(String str) {
        MimeTypeMap singleton = MimeTypeMap.getSingleton();
        String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(str);
        if (isEmpty(fileExtensionFromUrl) && str.lastIndexOf(Operators.DOT_STR) >= 0) {
            fileExtensionFromUrl = str.substring(str.lastIndexOf(Operators.DOT_STR) + 1);
        }
        String mimeTypeFromExtension = singleton.getMimeTypeFromExtension(fileExtensionFromUrl);
        if (!TextUtils.isEmpty(mimeTypeFromExtension)) {
            return mimeTypeFromExtension;
        }
        if (TextUtils.isEmpty(fileExtensionFromUrl)) {
            return "*/*";
        }
        return "application/" + fileExtensionFromUrl;
    }

    private static String getNavBarOverride() {
        if (Build.VERSION.SDK_INT < 19) {
            return null;
        }
        try {
            Method declaredMethod = Class.forName("android.os.SystemProperties").getDeclaredMethod("get", new Class[]{String.class});
            declaredMethod.setAccessible(true);
            return (String) declaredMethod.invoke((Object) null, new Object[]{"qemu.hw.mainkeys"});
        } catch (Throwable unused) {
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r3 = r3.getResources();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getNavigationBarHeight(android.content.Context r3) {
        /*
            boolean r0 = hasNavBar(r3)
            if (r0 == 0) goto L_0x001b
            android.content.res.Resources r3 = r3.getResources()
            java.lang.String r0 = "navigation_bar_height"
            java.lang.String r1 = "dimen"
            java.lang.String r2 = "android"
            int r0 = r3.getIdentifier(r0, r1, r2)
            if (r0 <= 0) goto L_0x001b
            int r3 = r3.getDimensionPixelSize(r0)
            goto L_0x001c
        L_0x001b:
            r3 = 0
        L_0x001c:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.PdrUtil.getNavigationBarHeight(android.content.Context):int");
    }

    public static String getNonString(String str, String str2) {
        return isEmpty(str) ? str2 : str;
    }

    public static Object getObject(Object[] objArr, int i) {
        try {
            return objArr[i];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0161, code lost:
        r23 = matchArray(r1, r7);
     */
    /* JADX WARNING: Removed duplicated region for block: B:104:0x0216 A[LOOP:1: B:17:0x0074->B:104:0x0216, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:109:0x0172 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x0168 A[ADDED_TO_REGION, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x015f A[ADDED_TO_REGION, Catch:{ Exception -> 0x023d }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static org.json.JSONObject getSitemapParameters(org.json.JSONObject r30, java.lang.String r31, java.lang.String r32) {
        /*
            r0 = r30
            r1 = r32
            java.lang.String r2 = "href"
            java.lang.String r3 = "hash"
            java.lang.String r4 = "search"
            java.lang.String r5 = "pathname"
            java.lang.String r6 = "host"
            java.lang.String r7 = "port"
            java.lang.String r8 = "global"
            java.lang.String r9 = "hostname"
            java.lang.String r10 = "protocol"
            java.lang.String r11 = "matchUrls"
            java.lang.String r12 = "webviewId"
            java.lang.String r13 = "launch_path"
            java.lang.String r14 = "webviewParameter"
            java.lang.String r15 = "pages"
            org.json.JSONArray r15 = r0.optJSONArray(r15)     // Catch:{ Exception -> 0x023d }
            r16 = r8
            java.lang.String r8 = r0.optString(r13)     // Catch:{ Exception -> 0x023d }
            r17 = 0
            r18 = r8
            r0 = 0
        L_0x0031:
            int r8 = r15.length()     // Catch:{ Exception -> 0x023d }
            if (r0 >= r8) goto L_0x0258
            org.json.JSONObject r8 = r15.optJSONObject(r0)     // Catch:{ Exception -> 0x023d }
            boolean r19 = r8.has(r12)     // Catch:{ Exception -> 0x023d }
            r20 = r0
            java.lang.String r0 = "webviewid"
            if (r19 == 0) goto L_0x004f
            java.lang.String r19 = r8.optString(r12)     // Catch:{ Exception -> 0x023d }
        L_0x004a:
            r21 = r12
            r12 = r19
            goto L_0x0054
        L_0x004f:
            java.lang.String r19 = r8.optString(r0)     // Catch:{ Exception -> 0x023d }
            goto L_0x004a
        L_0x0054:
            boolean r19 = r8.has(r11)     // Catch:{ Exception -> 0x023d }
            if (r19 == 0) goto L_0x0065
            org.json.JSONArray r19 = r8.optJSONArray(r11)     // Catch:{ Exception -> 0x023d }
            r29 = r19
            r19 = r11
            r11 = r29
            goto L_0x006d
        L_0x0065:
            r19 = r11
            java.lang.String r11 = "matchurls"
            org.json.JSONArray r11 = r8.optJSONArray(r11)     // Catch:{ Exception -> 0x023d }
        L_0x006d:
            r24 = r0
            r22 = r15
            r15 = 0
            r23 = 0
        L_0x0074:
            int r0 = r11.length()     // Catch:{ Exception -> 0x023d }
            if (r15 >= r0) goto L_0x0228
            java.lang.Object r0 = r11.opt(r15)     // Catch:{ Exception -> 0x023d }
            r25 = r11
            boolean r11 = r0 instanceof java.lang.String     // Catch:{ Exception -> 0x023d }
            if (r11 == 0) goto L_0x008c
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x023d }
            boolean r23 = match(r0, r1)     // Catch:{ Exception -> 0x023d }
            goto L_0x016e
        L_0x008c:
            boolean r11 = r0 instanceof org.json.JSONObject     // Catch:{ Exception -> 0x023d }
            if (r11 == 0) goto L_0x016e
            org.json.JSONObject r0 = (org.json.JSONObject) r0     // Catch:{ Exception -> 0x023d }
            java.net.URL r11 = new java.net.URL     // Catch:{ Exception -> 0x023d }
            r11.<init>(r1)     // Catch:{ Exception -> 0x023d }
            java.util.Iterator r26 = r0.keys()     // Catch:{ Exception -> 0x023d }
        L_0x009b:
            boolean r27 = r26.hasNext()     // Catch:{ Exception -> 0x023d }
            if (r27 == 0) goto L_0x016e
            java.lang.Object r27 = r26.next()     // Catch:{ Exception -> 0x023d }
            r1 = r27
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ Exception -> 0x023d }
            boolean r27 = r10.equals(r1)     // Catch:{ Exception -> 0x023d }
            if (r27 == 0) goto L_0x00bb
            org.json.JSONArray r1 = r0.optJSONArray(r10)     // Catch:{ Exception -> 0x023d }
            java.lang.String r27 = r11.getProtocol()     // Catch:{ Exception -> 0x023d }
        L_0x00b7:
            r28 = r7
            goto L_0x014b
        L_0x00bb:
            boolean r27 = r9.equals(r1)     // Catch:{ Exception -> 0x023d }
            if (r27 == 0) goto L_0x00ca
            org.json.JSONArray r1 = r0.optJSONArray(r9)     // Catch:{ Exception -> 0x023d }
            java.lang.String r27 = r11.getHost()     // Catch:{ Exception -> 0x023d }
            goto L_0x00b7
        L_0x00ca:
            boolean r27 = r7.equals(r1)     // Catch:{ Exception -> 0x023d }
            if (r27 == 0) goto L_0x00f4
            org.json.JSONArray r1 = r0.optJSONArray(r7)     // Catch:{ Exception -> 0x023d }
            r27 = r1
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x023d }
            r1.<init>()     // Catch:{ Exception -> 0x023d }
            r28 = r7
            java.lang.String r7 = ""
            r1.append(r7)     // Catch:{ Exception -> 0x023d }
            int r7 = r11.getPort()     // Catch:{ Exception -> 0x023d }
            r1.append(r7)     // Catch:{ Exception -> 0x023d }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x023d }
        L_0x00ed:
            r29 = r27
            r27 = r1
            r1 = r29
            goto L_0x014b
        L_0x00f4:
            r28 = r7
            boolean r7 = r6.equals(r1)     // Catch:{ Exception -> 0x023d }
            if (r7 == 0) goto L_0x011f
            org.json.JSONArray r1 = r0.optJSONArray(r6)     // Catch:{ Exception -> 0x023d }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x023d }
            r7.<init>()     // Catch:{ Exception -> 0x023d }
            r27 = r1
            java.lang.String r1 = r11.getHost()     // Catch:{ Exception -> 0x023d }
            r7.append(r1)     // Catch:{ Exception -> 0x023d }
            java.lang.String r1 = ":"
            r7.append(r1)     // Catch:{ Exception -> 0x023d }
            int r1 = r11.getPort()     // Catch:{ Exception -> 0x023d }
            r7.append(r1)     // Catch:{ Exception -> 0x023d }
            java.lang.String r1 = r7.toString()     // Catch:{ Exception -> 0x023d }
            goto L_0x00ed
        L_0x011f:
            boolean r7 = r5.equals(r1)     // Catch:{ Exception -> 0x023d }
            if (r7 == 0) goto L_0x012e
            org.json.JSONArray r1 = r0.optJSONArray(r5)     // Catch:{ Exception -> 0x023d }
            java.lang.String r27 = r11.getPath()     // Catch:{ Exception -> 0x023d }
            goto L_0x014b
        L_0x012e:
            boolean r7 = r4.equals(r1)     // Catch:{ Exception -> 0x023d }
            if (r7 == 0) goto L_0x013d
            org.json.JSONArray r1 = r0.optJSONArray(r4)     // Catch:{ Exception -> 0x023d }
            java.lang.String r27 = r11.getQuery()     // Catch:{ Exception -> 0x023d }
            goto L_0x014b
        L_0x013d:
            boolean r7 = r3.equals(r1)     // Catch:{ Exception -> 0x023d }
            if (r7 == 0) goto L_0x014e
            org.json.JSONArray r1 = r0.optJSONArray(r3)     // Catch:{ Exception -> 0x023d }
            java.lang.String r27 = r11.getRef()     // Catch:{ Exception -> 0x023d }
        L_0x014b:
            r7 = r27
            goto L_0x015d
        L_0x014e:
            boolean r1 = r2.equals(r1)     // Catch:{ Exception -> 0x023d }
            if (r1 == 0) goto L_0x015b
            org.json.JSONArray r1 = r0.optJSONArray(r2)     // Catch:{ Exception -> 0x023d }
            r7 = r32
            goto L_0x015d
        L_0x015b:
            r1 = 0
            r7 = 0
        L_0x015d:
            if (r1 == 0) goto L_0x0168
            if (r7 == 0) goto L_0x0168
            boolean r23 = matchArray(r1, r7)     // Catch:{ Exception -> 0x023d }
            if (r23 != 0) goto L_0x0168
            goto L_0x0170
        L_0x0168:
            r1 = r32
            r7 = r28
            goto L_0x009b
        L_0x016e:
            r28 = r7
        L_0x0170:
            if (r23 == 0) goto L_0x0216
            boolean r0 = r8.has(r14)     // Catch:{ Exception -> 0x023d }
            java.lang.String r1 = "__html5plusWebviewParameter "
            if (r0 == 0) goto L_0x017f
            org.json.JSONObject r0 = r8.optJSONObject(r14)     // Catch:{ Exception -> 0x023d }
            goto L_0x0183
        L_0x017f:
            org.json.JSONObject r0 = r8.optJSONObject(r1)     // Catch:{ Exception -> 0x023d }
        L_0x0183:
            if (r0 != 0) goto L_0x018a
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Exception -> 0x023d }
            r0.<init>()     // Catch:{ Exception -> 0x023d }
        L_0x018a:
            r2 = r0
            r0 = r18
            r2.put(r13, r0)     // Catch:{ Exception -> 0x023d }
            r7 = r24
            r2.put(r7, r12)     // Catch:{ Exception -> 0x023d }
            java.lang.String r0 = "appid"
            r11 = r31
            r2.put(r0, r11)     // Catch:{ Exception -> 0x023d }
            r0 = r30
            r3 = r16
            boolean r4 = r0.has(r3)     // Catch:{ Exception -> 0x0211 }
            java.lang.String r5 = "statusbar"
            if (r4 == 0) goto L_0x01ee
            org.json.JSONObject r3 = r0.optJSONObject(r3)     // Catch:{ Exception -> 0x0211 }
            if (r3 == 0) goto L_0x01ee
            boolean r4 = r3.has(r14)     // Catch:{ Exception -> 0x0211 }
            if (r4 == 0) goto L_0x01b9
            org.json.JSONObject r1 = r3.optJSONObject(r14)     // Catch:{ Exception -> 0x0211 }
            goto L_0x01bd
        L_0x01b9:
            org.json.JSONObject r1 = r3.optJSONObject(r1)     // Catch:{ Exception -> 0x0211 }
        L_0x01bd:
            if (r1 == 0) goto L_0x01cc
            org.json.JSONObject r4 = new org.json.JSONObject     // Catch:{ Exception -> 0x0211 }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x0211 }
            r4.<init>(r1)     // Catch:{ Exception -> 0x0211 }
            org.json.JSONObject r2 = io.dcloud.common.util.JSONUtil.combinSitemapHtmlJSONObject(r4, r2)     // Catch:{ Exception -> 0x0211 }
        L_0x01cc:
            boolean r1 = r2.has(r5)     // Catch:{ Exception -> 0x0211 }
            if (r1 != 0) goto L_0x01ee
            boolean r1 = r3.has(r5)     // Catch:{ Exception -> 0x0211 }
            if (r1 == 0) goto L_0x01ee
            boolean r1 = r3.isNull(r5)     // Catch:{ Exception -> 0x0211 }
            if (r1 != 0) goto L_0x01ee
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Exception -> 0x0211 }
            org.json.JSONObject r3 = r3.optJSONObject(r5)     // Catch:{ Exception -> 0x0211 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0211 }
            r1.<init>(r3)     // Catch:{ Exception -> 0x0211 }
            r2.put(r5, r1)     // Catch:{ Exception -> 0x0211 }
        L_0x01ee:
            boolean r1 = r2.has(r5)     // Catch:{ Exception -> 0x0211 }
            if (r1 != 0) goto L_0x0215
            boolean r1 = r0.has(r5)     // Catch:{ Exception -> 0x0211 }
            if (r1 == 0) goto L_0x0215
            boolean r1 = r0.isNull(r5)     // Catch:{ Exception -> 0x0211 }
            if (r1 != 0) goto L_0x0215
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Exception -> 0x0211 }
            org.json.JSONObject r0 = r0.optJSONObject(r5)     // Catch:{ Exception -> 0x0211 }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x0211 }
            r1.<init>(r0)     // Catch:{ Exception -> 0x0211 }
            r2.put(r5, r1)     // Catch:{ Exception -> 0x0211 }
            goto L_0x0215
        L_0x0211:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Exception -> 0x023d }
        L_0x0215:
            return r2
        L_0x0216:
            r11 = r31
            r0 = r18
            r1 = r20
            r7 = r24
            int r15 = r15 + 1
            r11 = r25
            r7 = r28
            r1 = r32
            goto L_0x0074
        L_0x0228:
            r11 = r31
            r28 = r7
            r0 = r18
            r1 = r20
            int r1 = r1 + 1
            r0 = r1
            r11 = r19
            r12 = r21
            r15 = r22
            r1 = r32
            goto L_0x0031
        L_0x023d:
            r0 = move-exception
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "getSitemapParameters e=="
            r1.append(r2)
            java.lang.String r0 = r0.getMessage()
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            java.lang.String r1 = "PdrUtil"
            io.dcloud.common.adapter.util.Logger.e(r1, r0)
        L_0x0258:
            r1 = 0
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.PdrUtil.getSitemapParameters(org.json.JSONObject, java.lang.String, java.lang.String):org.json.JSONObject");
    }

    public static String getUrlPathName(String str) {
        return str != null ? URLUtil.stripAnchor(stripQuery(str)) : str;
    }

    public static boolean hasNavBar(Context context) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("config_showNavigationBar", "bool", WXEnvironment.OS);
        if (identifier == 0) {
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
        boolean z = resources.getBoolean(identifier);
        String navBarOverride = getNavBarOverride();
        if ("1".equals(navBarOverride)) {
            return false;
        }
        if (WXInstanceApm.VALUE_ERROR_CODE_DEFAULT.equals(navBarOverride)) {
            return true;
        }
        return z;
    }

    public static String int2DecimalStr(int i, int i2) {
        return String.valueOf(BigDecimal.valueOf((long) i).divide(BigDecimal.valueOf((long) i2)));
    }

    public static boolean isAllScreenDevice(Activity activity) {
        float f;
        float f2;
        if (mHasCheckAllScreen) {
            return mIsAllScreenDevice;
        }
        mHasCheckAllScreen = true;
        mIsAllScreenDevice = false;
        int i = Build.VERSION.SDK_INT;
        if (i < 21) {
            return false;
        }
        if (i < 28 || activity.getWindow().getDecorView().getRootWindowInsets() == null || activity.getWindow().getDecorView().getRootWindowInsets().getDisplayCutout() == null) {
            WindowManager windowManager = (WindowManager) activity.getSystemService("window");
            if (windowManager != null) {
                Display defaultDisplay = windowManager.getDefaultDisplay();
                Point point = new Point();
                defaultDisplay.getRealSize(point);
                int i2 = point.x;
                int i3 = point.y;
                if (i2 < i3) {
                    f = (float) i2;
                    f2 = (float) i3;
                } else {
                    float f3 = (float) i3;
                    f2 = (float) i2;
                    f = f3;
                }
                if (f2 / f >= 1.97f) {
                    mIsAllScreenDevice = true;
                }
            }
            return mIsAllScreenDevice;
        }
        mIsAllScreenDevice = true;
        return mIsAllScreenDevice;
    }

    public static boolean isBase64ImagePath(String str) {
        return !TextUtils.isEmpty(str) && str.startsWith("data:image");
    }

    public static boolean isContains(String str, String str2) {
        if (!isEmpty(str) && !isEmpty(str2)) {
            return str.contains(str2);
        }
        return false;
    }

    public static boolean isDeviceRootDir(String str) {
        return str.startsWith(DeviceInfo.sDeviceRootDir) || str.startsWith(DeviceInfo.sCacheRootDir) || str.startsWith("/sdcard/") || str.startsWith(DeviceInfo.sDeviceRootDir.substring(1)) || str.startsWith("sdcard/");
    }

    public static boolean isEmpty(Object obj) {
        return obj == null || obj.equals("") || (obj.toString().length() == 4 && obj.toString().toLowerCase(Locale.ENGLISH).equals("null"));
    }

    public static boolean isEquals(String str, String str2) {
        if (str == null || str2 == null) {
            return false;
        }
        return str.equalsIgnoreCase(str2);
    }

    public static boolean isFilePath(String str) {
        return !TextUtils.isEmpty(str) && str.startsWith("file:/");
    }

    public static boolean isFullScreen(Activity activity) {
        return (activity.getWindow().getAttributes().flags & 1024) == 1024;
    }

    public static boolean isLightColor(int i) {
        double red = (double) Color.red(i);
        Double.isNaN(red);
        double green = (double) Color.green(i);
        Double.isNaN(green);
        double d = (red * 0.299d) + (green * 0.587d);
        double blue = (double) Color.blue(i);
        Double.isNaN(blue);
        return ((int) (d + (blue * 0.114d))) >= 192;
    }

    public static boolean isNavigationBarExist(Activity activity) {
        ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();
        if (viewGroup != null) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                viewGroup.getChildAt(i).getContext().getPackageName();
                try {
                    if (viewGroup.getChildAt(i).getId() == -1) {
                        continue;
                    } else if (NAVIGATION.equals(activity.getResources().getResourceEntryName(viewGroup.getChildAt(i).getId())) && viewGroup.getChildAt(i).getVisibility() == 0) {
                        return true;
                    }
                } catch (Exception unused) {
                }
            }
        }
        return false;
    }

    public static boolean isNavigationBarShow(Activity activity) {
        if (Build.VERSION.SDK_INT >= 17) {
            Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
            Point point = new Point();
            Point point2 = new Point();
            defaultDisplay.getSize(point);
            defaultDisplay.getRealSize(point2);
            if (point2.y != point.y) {
                return true;
            }
            return false;
        }
        return !ViewConfiguration.get(activity).hasPermanentMenuKey() && !KeyCharacterMap.deviceHasKey(4);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0043 A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:20:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isNavigationBarShowing(android.content.Context r3) {
        /*
            boolean r0 = hasNavBar(r3)
            r1 = 0
            if (r0 == 0) goto L_0x0044
            int r0 = android.os.Build.VERSION.SDK_INT
            r2 = 17
            if (r0 < r2) goto L_0x0044
            java.lang.String r0 = android.os.Build.BRAND
            java.lang.String r2 = "HUAWEI"
            boolean r2 = r0.equalsIgnoreCase(r2)
            if (r2 == 0) goto L_0x0018
            goto L_0x0037
        L_0x0018:
            java.lang.String r2 = "XIAOMI"
            boolean r2 = r0.equalsIgnoreCase(r2)
            if (r2 == 0) goto L_0x0023
            java.lang.String r0 = "force_fsg_nav_bar"
            goto L_0x0039
        L_0x0023:
            java.lang.String r2 = "VIVO"
            boolean r2 = r0.equalsIgnoreCase(r2)
            if (r2 == 0) goto L_0x002c
            goto L_0x0034
        L_0x002c:
            java.lang.String r2 = "OPPO"
            boolean r0 = r0.equalsIgnoreCase(r2)
            if (r0 == 0) goto L_0x0037
        L_0x0034:
            java.lang.String r0 = "navigation_gesture_on"
            goto L_0x0039
        L_0x0037:
            java.lang.String r0 = "navigationbar_is_min"
        L_0x0039:
            android.content.ContentResolver r3 = r3.getContentResolver()
            int r3 = android.provider.Settings.Global.getInt(r3, r0, r1)
            if (r3 != 0) goto L_0x0044
            r1 = 1
        L_0x0044:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.PdrUtil.isNavigationBarShowing(android.content.Context):boolean");
    }

    public static boolean isNetPath(String str) {
        return str != null && ((str.startsWith(DeviceInfo.HTTP_PROTOCOL) && !str.startsWith("http://localhost")) || ((str.startsWith(DeviceInfo.HTTPS_PROTOCOL) && !str.startsWith("https://localhost")) || ((str.startsWith("rtmp://") && !str.startsWith("rtmp://localhost")) || (str.startsWith("rtsp://") && !str.startsWith("rtsp://localhost")))));
    }

    public static boolean isSafeEntryName(String str) {
        return !str.contains(FILE_PATH_ENTRY_BACK) && !str.contains("%");
    }

    public static boolean isSameDay(long j, long j2) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(j);
        GregorianCalendar gregorianCalendar2 = new GregorianCalendar();
        gregorianCalendar2.setTimeInMillis(j2);
        if (gregorianCalendar.get(1) == gregorianCalendar2.get(1) && gregorianCalendar.get(2) == gregorianCalendar2.get(2) && gregorianCalendar.get(5) == gregorianCalendar2.get(5)) {
            return true;
        }
        return false;
    }

    public static boolean isSupportOaid() {
        String str = Build.BRAND;
        int i = Build.VERSION.SDK_INT;
        if ((str.equalsIgnoreCase("honor") || str.equalsIgnoreCase("huawei")) && i >= 23) {
            return true;
        }
        if (str.equalsIgnoreCase("vivo") && i >= 28) {
            return true;
        }
        if ((!str.equalsIgnoreCase("xiaomi") || i < 28) && i < 29) {
            return false;
        }
        return true;
    }

    public static boolean isUniMPHostForUniApp() {
        return PlatformUtil.checkClass("io.dcloud.feature.sdk.Interface.DCUniAppHost");
    }

    private static void loadProperties2HashMap(HashMap<String, String> hashMap, String str) {
        InputStream resInputStream = PlatformUtil.getResInputStream(str);
        Properties properties = new Properties();
        try {
            properties.load(resInputStream);
            Enumeration<?> propertyNames = properties.propertyNames();
            if (propertyNames != null) {
                while (propertyNames.hasMoreElements()) {
                    String str2 = (String) propertyNames.nextElement();
                    hashMap.put(str2.toLowerCase(Locale.ENGLISH), (String) properties.get(str2));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable th) {
            IOUtil.close(resInputStream);
            throw th;
        }
        IOUtil.close(resInputStream);
    }

    public static String makeQueryStringAllRegExp(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        return str.replace("\\", "\\\\").replace("*", "\\*").replace(Operators.PLUS, "\\+").replace("|", "\\|").replace(Operators.BLOCK_START_STR, "\\{").replace(Operators.BLOCK_END_STR, "\\}").replace(Operators.BRACKET_START_STR, "\\(").replace(Operators.BRACKET_END_STR, "\\)").replace("^", "\\^").replace(Operators.DOLLAR_STR, "\\$").replace(Operators.ARRAY_START_STR, "\\[").replace(Operators.ARRAY_END_STR, "\\]").replace(Operators.CONDITION_IF_STRING, "\\?").replace(",", "\\,").replace(Operators.DOT_STR, "\\.").replace("&", "\\&").replace("'", "\\'");
    }

    private static boolean match(String str, String str2) {
        return Pattern.compile(str).matcher(str2).find();
    }

    private static boolean matchArray(JSONArray jSONArray, String str) {
        if (jSONArray != null) {
            for (int i = 0; i < jSONArray.length(); i++) {
                if (match(jSONArray.optString(i), str)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean navigationGestureEnabled(Context context) {
        int i;
        if (DeviceInfo.sBrand.toLowerCase(Locale.ENGLISH).equals("xiaomi")) {
            i = Build.VERSION.SDK_INT >= 26 ? Settings.Global.getInt(context.getContentResolver(), "force_fsg_nav_bar", 0) : 0;
        } else {
            i = Settings.Secure.getInt(context.getContentResolver(), "navigation_gesture_on", 0);
        }
        if (i != 0) {
            return true;
        }
        return false;
    }

    public static boolean parseBoolean(String str, boolean z, boolean z2) {
        if (isEmpty(str)) {
            return z;
        }
        if (str.equalsIgnoreCase(AbsoluteConst.TRUE)) {
            return (!z2) & true;
        }
        return str.equalsIgnoreCase(AbsoluteConst.FALSE) ? z2 | false : z;
    }

    public static float parseFloat(String str, float f) {
        if (str == null) {
            return f;
        }
        try {
            return Float.parseFloat(str);
        } catch (Exception unused) {
            return f;
        }
    }

    public static int parseInt(String str, int i) {
        if (str == null) {
            return i;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception unused) {
            return i;
        }
    }

    public static long parseLong(String str, long j) {
        try {
            return Long.parseLong(str);
        } catch (Exception unused) {
            return j;
        }
    }

    public static int pxFromDp(float f, DisplayMetrics displayMetrics) {
        return Math.round(TypedValue.applyDimension(1, f, displayMetrics));
    }

    public static int pxFromSp(float f, DisplayMetrics displayMetrics) {
        return Math.round(TypedValue.applyDimension(2, f, displayMetrics));
    }

    public static Bitmap renderCroppedGreyscaleBitmap(byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6) {
        int[] iArr = new int[(i5 * i6)];
        int i7 = (i4 * i) + i3;
        for (int i8 = 0; i8 < i6; i8++) {
            int i9 = i8 * i5;
            for (int i10 = 0; i10 < i5; i10++) {
                iArr[i9 + i10] = ((bArr[i7 + i10] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE) * 65793) | -16777216;
            }
            i7 += i;
        }
        Bitmap createBitmap = Bitmap.createBitmap(i5, i6, Bitmap.Config.ARGB_8888);
        createBitmap.setPixels(iArr, 0, i5, 0, 0, i5, i6);
        return createBitmap;
    }

    public static boolean saveBitmapToFile(Bitmap bitmap, String str) {
        try {
            File file = new File(str);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String standardizedURL(String str, String str2) {
        String stripQuery = stripQuery(stripAnchor(str));
        boolean z = true;
        if (str2.startsWith("./")) {
            str2 = str2.substring(2);
            int lastIndexOf = stripQuery.lastIndexOf(47);
            if (lastIndexOf >= 0) {
                return stripQuery.substring(0, lastIndexOf + 1) + str2;
            }
        }
        int indexOf = str2.indexOf("../");
        int lastIndexOf2 = stripQuery.lastIndexOf(47);
        if (lastIndexOf2 <= -1) {
            z = false;
        }
        if (!z) {
            return str2;
        }
        String substring = stripQuery.substring(0, lastIndexOf2);
        while (indexOf > -1) {
            str2 = str2.substring(3);
            substring = substring.substring(0, substring.lastIndexOf(47));
            indexOf = str2.indexOf("../");
        }
        return substring + '/' + str2;
    }

    public static String[] stringSplit(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        int i = 0;
        StringTokenizer stringTokenizer = new StringTokenizer(str, str2, false);
        String[] strArr = new String[stringTokenizer.countTokens()];
        while (stringTokenizer.hasMoreElements()) {
            strArr[i] = stringTokenizer.nextToken().trim();
            i++;
        }
        return strArr;
    }

    public static int stringToColor(String str) {
        boolean z;
        int i;
        int i2;
        int i3;
        try {
            if (!isEmpty(str)) {
                str = str.trim();
            }
            int length = str.length();
            if (!(length == 4 || length == 7 || length == 3)) {
                if (length != 6) {
                    if (str.startsWith("#") && length > 1 && length < 10) {
                        return Color.parseColor(str);
                    }
                    String[] strArr = null;
                    if (str.startsWith("rgba")) {
                        strArr = stringSplit(str.substring(5, str.length() - 1), ",");
                        z = true;
                    } else {
                        if (str.startsWith("rgb")) {
                            strArr = stringSplit(str.substring(4, str.length() - 1), ",");
                        }
                        z = false;
                    }
                    int i4 = 255;
                    if (strArr != null) {
                        if (!z) {
                            try {
                                i = Integer.parseInt(strArr[0]);
                            } catch (Exception unused) {
                            }
                            try {
                                i2 = Integer.parseInt(strArr[1]);
                            } catch (Exception unused2) {
                            }
                            try {
                                i3 = Integer.parseInt(strArr[2]);
                            } catch (Exception unused3) {
                                i3 = 255;
                            }
                            return (i4 << 24) + (i << 16) + (i2 << 8) + i3;
                        }
                        i = Integer.parseInt(strArr[0]);
                        i2 = Integer.parseInt(strArr[1]);
                        i3 = Integer.parseInt(strArr[2]);
                        try {
                            i4 = (int) (((float) 255) * Float.parseFloat(strArr[3]));
                        } catch (Exception unused4) {
                        }
                        return (i4 << 24) + (i << 16) + (i2 << 8) + i3;
                    }
                    i = 255;
                    i3 = 255;
                    i2 = 255;
                    return (i4 << 24) + (i << 16) + (i2 << 8) + i3;
                }
            }
            if (length == 4 || length == 7) {
                str = str.substring(1);
            }
            if (str.length() == 3) {
                StringBuffer stringBuffer = new StringBuffer();
                for (int i5 = 0; i5 < 3; i5++) {
                    stringBuffer.append(str.charAt(i5));
                    stringBuffer.append(str.charAt(i5));
                }
                str = stringBuffer.toString();
            }
            return Integer.parseInt(str, 16) - 16777216;
        } catch (Exception unused5) {
            return -1;
        }
    }

    public static String stripAnchor(String str) {
        return URLUtil.stripAnchor(str);
    }

    public static String stripQuery(String str) {
        int indexOf = str.indexOf(63);
        return indexOf != -1 ? str.substring(0, indexOf) : str;
    }

    public static String toHexFromColor(int i) {
        StringBuilder sb = new StringBuilder();
        String hexString = Integer.toHexString(Color.red(i));
        String hexString2 = Integer.toHexString(Color.green(i));
        String hexString3 = Integer.toHexString(Color.blue(i));
        if (hexString.length() == 1) {
            hexString = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT + hexString;
        }
        if (hexString2.length() == 1) {
            hexString2 = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT + hexString2;
        }
        if (hexString3.length() == 1) {
            hexString3 = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT + hexString3;
        }
        String upperCase = hexString.toUpperCase();
        String upperCase2 = hexString2.toUpperCase();
        String upperCase3 = hexString3.toUpperCase();
        sb.append("#");
        sb.append(upperCase);
        sb.append(upperCase2);
        sb.append(upperCase3);
        return sb.toString();
    }

    public static void toast(Context context, String str, Bitmap bitmap) {
        ToastCompat makeText = ToastCompat.makeText(context, (CharSequence) str, 1);
        if (bitmap != null) {
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            View view = makeText.getView();
            ImageView imageView = new ImageView(context);
            imageView.setImageBitmap(bitmap);
            ((ViewGroup) view).addView(imageView, 0);
            makeText.setText(str + " w=" + width + ";h=" + height);
        }
        makeText.setDuration(1);
        makeText.show();
    }

    public static int convertToScreenInt(String str, int i, int i2, float f, boolean z) {
        float f2;
        if (str == null) {
            return i2;
        }
        try {
            if (str.endsWith("px")) {
                String substring = str.substring(0, str.length() - 2);
                if (substring == null || !substring.contains(Operators.DOT_STR)) {
                    f2 = (float) Integer.parseInt(substring);
                } else {
                    f2 = Float.parseFloat(substring);
                }
                float f3 = f2 * f;
                return z ? Math.round(f3) : (int) f3;
            } else if (str.endsWith("%")) {
                String substring2 = str.substring(0, str.length() - 1);
                try {
                    if (!substring2.contains(Operators.DOT_STR)) {
                        return (i * Integer.parseInt(substring2)) / 100;
                    }
                    float parseFloat = (((float) i) * Float.parseFloat(substring2)) / 100.0f;
                    return z ? Math.round(parseFloat) : (int) parseFloat;
                } catch (NumberFormatException unused) {
                    return i2;
                }
            } else {
                double parseDouble = Double.parseDouble(str);
                double d = (double) f;
                Double.isNaN(d);
                double d2 = parseDouble * d;
                return z ? (int) Math.round(d2) : (int) d2;
            }
        } catch (Exception unused2) {
            return i2;
        }
    }

    public static float parseFloat(String str, float f, float f2) {
        return parseFloat(str, f, f2, 1.0f);
    }

    public static int parseInt(String str, int i, int i2) {
        if (str == null) {
            return i2;
        }
        try {
            String lowerCase = str.toLowerCase(Locale.ENGLISH);
            if (lowerCase.endsWith("px")) {
                return Integer.parseInt(lowerCase.substring(0, lowerCase.length() - 2));
            }
            if (lowerCase.endsWith("%")) {
                String substring = lowerCase.substring(0, lowerCase.length() - 1);
                try {
                    if (substring.contains(Operators.DOT_STR)) {
                        return (int) ((((float) i) * Float.parseFloat(substring)) / 100.0f);
                    }
                    return (i * Integer.parseInt(substring)) / 100;
                } catch (NumberFormatException unused) {
                    return i2;
                }
            } else {
                if (lowerCase.startsWith("#")) {
                    lowerCase = "0x" + lowerCase.substring(1);
                }
                if (lowerCase.startsWith("0x")) {
                    return Integer.valueOf(lowerCase.substring(2), 16).intValue();
                }
                return Integer.parseInt(lowerCase);
            }
        } catch (Exception unused2) {
            return i2;
        }
    }

    public static float parseFloat(String str, float f, float f2, float f3) {
        if (str == null) {
            return f2;
        }
        String lowerCase = str.toLowerCase(Locale.ENGLISH);
        if (lowerCase.endsWith("px")) {
            lowerCase = lowerCase.substring(0, lowerCase.length() - 2);
        }
        try {
            return Float.parseFloat(lowerCase) * f3;
        } catch (NumberFormatException unused) {
            if (lowerCase.endsWith("%")) {
                try {
                    return (f * Float.parseFloat(lowerCase.substring(0, lowerCase.length() - 1))) / 100.0f;
                } catch (Exception unused2) {
                    return f2;
                }
            }
            return f2;
        }
    }

    public static boolean parseBoolean(String str, boolean z) {
        if (isEmpty(str)) {
            return z;
        }
        return str.equalsIgnoreCase(AbsoluteConst.TRUE);
    }

    public static Object getKeyByValue(HashMap hashMap, Object obj) {
        return getKeyByValue(hashMap, obj, false);
    }

    public static void loadProperties2HashMap(HashMap<String, String> hashMap, HashMap<String, String> hashMap2, HashMap<String, HashMap<String, String>> hashMap3, String str) {
        XmlUtil.DHNode XML_Parser;
        InputStream resInputStream = PlatformUtil.getResInputStream(str);
        if (resInputStream != null && (XML_Parser = XmlUtil.XML_Parser(resInputStream)) != null) {
            ArrayList<XmlUtil.DHNode> elements = XmlUtil.getElements(XmlUtil.getElement(XML_Parser, IApp.ConfigProperty.CONFIG_FEATURES), IApp.ConfigProperty.CONFIG_FEATURE);
            if (elements != null && !elements.isEmpty()) {
                Iterator<XmlUtil.DHNode> it = elements.iterator();
                while (it.hasNext()) {
                    XmlUtil.DHNode next = it.next();
                    String lowerCase = XmlUtil.getAttributeValue(next, "name").toLowerCase(Locale.ENGLISH);
                    String attributeValue = XmlUtil.getAttributeValue(next, "value");
                    if (AbsoluteConst.F_UI.equals(lowerCase)) {
                        hashMap2.put("webview", attributeValue);
                    }
                    hashMap2.put(lowerCase, attributeValue);
                    ArrayList<XmlUtil.DHNode> elements2 = XmlUtil.getElements(next, "module");
                    if (elements2 != null && !elements2.isEmpty()) {
                        HashMap hashMap4 = hashMap3.get(lowerCase);
                        if (hashMap4 == null) {
                            hashMap4 = new LinkedHashMap(2);
                            hashMap3.put(lowerCase, hashMap4);
                        }
                        Iterator<XmlUtil.DHNode> it2 = elements2.iterator();
                        while (it2.hasNext()) {
                            XmlUtil.DHNode next2 = it2.next();
                            hashMap4.put(XmlUtil.getAttributeValue(next2, "name").toLowerCase(Locale.ENGLISH), XmlUtil.getAttributeValue(next2, "value"));
                        }
                    }
                }
            }
            ArrayList<XmlUtil.DHNode> elements3 = XmlUtil.getElements(XmlUtil.getElement(XML_Parser, IApp.ConfigProperty.CONFIG_SERVICES), "service");
            if (elements3 != null && !elements3.isEmpty()) {
                Iterator<XmlUtil.DHNode> it3 = elements3.iterator();
                while (it3.hasNext()) {
                    XmlUtil.DHNode next3 = it3.next();
                    hashMap.put(XmlUtil.getAttributeValue(next3, "name").toLowerCase(Locale.ENGLISH), XmlUtil.getAttributeValue(next3, "value"));
                }
            }
        }
    }
}
