package com.taobao.weex.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import androidx.collection.LruCache;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXConfigAdapter;
import com.taobao.weex.common.Constants;
import com.taobao.weex.el.parse.Operators;

public class WXUtils {
    private static final int HUNDRED = 100;
    public static final char PERCENT = '%';
    static final LruCache<String, Integer> sCache = new LruCache<>(64);
    private static final long sInterval = (System.currentTimeMillis() - SystemClock.uptimeMillis());

    public static LruCache getCache() {
        return sCache;
    }

    public static boolean isUiThread() {
        return Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId();
    }

    public static boolean isUndefined(float f) {
        return Float.isNaN(f);
    }

    public static float getFloatByViewport(Object obj, float f) {
        return floatByViewport(obj, f);
    }

    public static float getFloatByViewport(Object obj, int i) {
        return floatByViewport(obj, (float) i);
    }

    private static float floatByViewport(Object obj, float f) {
        if (obj == null) {
            return Float.NaN;
        }
        String trim = obj.toString().trim();
        if ("auto".equals(trim) || Constants.Name.UNDEFINED.equals(trim) || TextUtils.isEmpty(trim)) {
            WXLogUtils.e("Argument Warning ! value is " + trim + "And default Value:" + Float.NaN);
            return Float.NaN;
        } else if (trim.endsWith("wx")) {
            try {
                return transferWx(trim, f);
            } catch (NumberFormatException e) {
                WXLogUtils.e("Argument format error! value is " + obj, (Throwable) e);
                return Float.NaN;
            } catch (Exception e2) {
                WXLogUtils.e("Argument error! value is " + obj, (Throwable) e2);
                return Float.NaN;
            }
        } else if (!trim.endsWith("px")) {
            try {
                return Float.parseFloat(trim);
            } catch (NumberFormatException e3) {
                WXLogUtils.e("Argument format error! value is " + obj, (Throwable) e3);
                return Float.NaN;
            } catch (Exception e4) {
                WXLogUtils.e("Argument error! value is " + obj, (Throwable) e4);
                return Float.NaN;
            }
        } else if (trim.length() <= 3 || (!trim.endsWith("rpx") && !trim.endsWith("upx"))) {
            try {
                return Float.parseFloat(trim.substring(0, trim.indexOf("px")));
            } catch (NumberFormatException e5) {
                WXLogUtils.e("Argument format error! value is " + obj, (Throwable) e5);
                return Float.NaN;
            } catch (Exception e6) {
                WXLogUtils.e("Argument error! value is " + obj, (Throwable) e6);
                return Float.NaN;
            }
        } else {
            try {
                return transferRpxAndUpx(trim.substring(0, trim.length() - 3), 750.0f);
            } catch (NumberFormatException e7) {
                WXLogUtils.e("Argument format error! value is " + obj, (Throwable) e7);
                return Float.NaN;
            } catch (Exception e8) {
                WXLogUtils.e("Argument error! value is " + obj, (Throwable) e8);
                return Float.NaN;
            }
        }
    }

    public static float getFloat(Object obj) {
        return getFloat(obj, Float.valueOf(Float.NaN)).floatValue();
    }

    public static Float getFloat(Object obj, Float f) {
        if (obj == null) {
            return f;
        }
        String trim = obj.toString().trim();
        if ("auto".equals(trim) || Constants.Name.UNDEFINED.equals(trim) || TextUtils.isEmpty(trim)) {
            WXLogUtils.e("Argument Warning ! value is " + trim + "And default Value:" + Float.NaN);
            return f;
        } else if (trim.endsWith("wx")) {
            try {
                return Float.valueOf(transferWx(trim, 750.0f));
            } catch (NumberFormatException e) {
                WXLogUtils.e("Argument format error! value is " + obj, (Throwable) e);
                return f;
            } catch (Exception e2) {
                WXLogUtils.e("Argument error! value is " + obj, (Throwable) e2);
                return f;
            }
        } else if (!trim.endsWith("px")) {
            try {
                return Float.valueOf(Float.parseFloat(trim));
            } catch (NumberFormatException e3) {
                WXLogUtils.e("Argument format error! value is " + obj, (Throwable) e3);
                return f;
            } catch (Exception e4) {
                WXLogUtils.e("Argument error! value is " + obj, (Throwable) e4);
                return f;
            }
        } else if (trim.length() <= 3 || (!trim.endsWith("rpx") && !trim.endsWith("upx"))) {
            try {
                return Float.valueOf(Float.parseFloat(trim.substring(0, trim.indexOf("px"))));
            } catch (NumberFormatException e5) {
                WXLogUtils.e("Argument format error! value is " + obj, (Throwable) e5);
                return f;
            } catch (Exception e6) {
                WXLogUtils.e("Argument error! value is " + obj, (Throwable) e6);
                return f;
            }
        } else {
            try {
                return Float.valueOf(transferRpxAndUpx(trim.substring(0, trim.length() - 3), 750.0f));
            } catch (NumberFormatException e7) {
                WXLogUtils.e("Argument format error! value is " + obj, (Throwable) e7);
                return f;
            } catch (Exception e8) {
                WXLogUtils.e("Argument error! value is " + obj, (Throwable) e8);
                return f;
            }
        }
    }

    private static float transferWx(String str, float f) {
        if (str == null) {
            return 0.0f;
        }
        if (str.endsWith("wx")) {
            str = str.substring(0, str.indexOf("wx"));
        }
        return ((WXEnvironment.sApplication.getResources().getDisplayMetrics().density * Float.valueOf(Float.parseFloat(str)).floatValue()) * f) / ((float) WXViewUtils.getScreenWidth());
    }

    private static float transferRpxAndUpx(String str, float f) {
        if (str == null) {
            return 0.0f;
        }
        if (str.endsWith("rpx")) {
            str = str.substring(0, str.indexOf("rpx"));
        } else if (str.endsWith("upx")) {
            str = str.substring(0, str.indexOf("upx"));
        }
        return (WXEnvironment.getViewProt() / f) * Float.valueOf(Float.parseFloat(str)).floatValue();
    }

    public static float fastGetFloat(String str, float f) {
        boolean z;
        int i;
        char charAt;
        char charAt2;
        float f2 = 0.0f;
        if (TextUtils.isEmpty(str)) {
            return 0.0f;
        }
        int i2 = 0;
        if (str.charAt(0) == '-') {
            i = 1;
            z = false;
        } else {
            i = str.charAt(0) == '+' ? 1 : 0;
            z = true;
        }
        while (i < str.length() && (charAt2 = str.charAt(i)) >= '0' && charAt2 <= '9') {
            f2 = ((f2 * 10.0f) + ((float) charAt2)) - 48.0f;
            i++;
        }
        if (i < str.length() && str.charAt(i) == '.') {
            int i3 = i + 1;
            int i4 = 10;
            while (i3 < str.length() && ((float) i2) < f && (charAt = str.charAt(i3)) >= '0' && charAt <= '9') {
                f2 += ((float) (charAt - '0')) / ((float) i4);
                i4 *= 10;
                i3++;
                i2++;
            }
        }
        return !z ? f2 * -1.0f : f2;
    }

    public static float fastGetFloat(String str) {
        return fastGetFloat(str, 2.14748365E9f);
    }

    public static int parseInt(String str) {
        try {
            if (TextUtils.isEmpty(str) || str.contains(Operators.DOT_STR)) {
                return 0;
            }
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            if (!WXEnvironment.isApkDebugable()) {
                return 0;
            }
            WXLogUtils.e(WXLogUtils.getStackTrace(e));
            return 0;
        }
    }

    public static int parseInt(Object obj) {
        return parseInt(String.valueOf(obj));
    }

    public static float parseFloat(Object obj) {
        return parseFloat(String.valueOf(obj));
    }

    public static float parseFloat(String str) {
        try {
            if (!TextUtils.isEmpty(str) && !TextUtils.equals(str, "null")) {
                return Float.parseFloat(str);
            }
            if (!WXEnvironment.isApkDebugable()) {
                return 0.0f;
            }
            WXLogUtils.e("WXUtils parseFloat illegal value is " + str);
            return 0.0f;
        } catch (NumberFormatException e) {
            if (!WXEnvironment.isApkDebugable()) {
                return 0.0f;
            }
            WXLogUtils.e(WXLogUtils.getStackTrace(e));
            return 0.0f;
        }
    }

    public static int getInt(Object obj) {
        return getInteger(obj, 0).intValue();
    }

    public static Integer getInteger(Object obj, Integer num) {
        Integer num2;
        if (obj == null) {
            return num;
        }
        String trim = obj.toString().trim();
        Integer num3 = sCache.get(trim);
        if (num3 != null) {
            return num3;
        }
        String substring = trim.length() >= 2 ? trim.substring(trim.length() - 2, trim.length()) : "";
        if (TextUtils.equals("wx", substring)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.w("the value of " + obj + " use wx unit, which will be not supported soon after.");
            }
            try {
                num2 = Integer.valueOf((int) transferWx(trim, 750.0f));
            } catch (NumberFormatException e) {
                WXLogUtils.e("Argument format error! value is " + obj, (Throwable) e);
            } catch (Exception e2) {
                WXLogUtils.e("Argument error! value is " + obj, (Throwable) e2);
            }
        } else if (!TextUtils.equals("px", substring)) {
            try {
                if (TextUtils.isEmpty(trim)) {
                    if (WXEnvironment.isApkDebugable()) {
                        WXLogUtils.e("Argument value is null, df is" + num);
                    }
                    num2 = num;
                } else if (trim.contains(Operators.DOT_STR)) {
                    num2 = Integer.valueOf((int) parseFloat(trim));
                } else {
                    num2 = Integer.valueOf(Integer.parseInt(trim));
                }
            } catch (NumberFormatException e3) {
                WXLogUtils.e("Argument format error! value is " + obj, (Throwable) e3);
            } catch (Exception e4) {
                WXLogUtils.e("Argument error! value is " + obj, (Throwable) e4);
            }
        } else if (trim.length() <= 3 || (!trim.endsWith("rpx") && !trim.endsWith("upx"))) {
            try {
                String substring2 = trim.substring(0, trim.length() - 2);
                if (TextUtils.isEmpty(substring2) || !substring2.contains(Operators.DOT_STR)) {
                    num2 = Integer.valueOf(Integer.parseInt(substring2));
                } else {
                    num2 = Integer.valueOf((int) parseFloat(substring2));
                }
            } catch (NumberFormatException e5) {
                WXLogUtils.e("Argument format error! value is " + obj, (Throwable) e5);
            } catch (Exception e6) {
                WXLogUtils.e("Argument error! value is " + obj, (Throwable) e6);
            }
        } else {
            try {
                num2 = Integer.valueOf((int) transferRpxAndUpx(trim.substring(0, trim.length() - 3), 750.0f));
            } catch (NumberFormatException e7) {
                WXLogUtils.e("Argument format error! value is " + obj, (Throwable) e7);
            } catch (Exception e8) {
                WXLogUtils.e("Argument error! value is " + obj, (Throwable) e8);
            }
        }
        if (num2 != null && !num2.equals(num)) {
            sCache.put(trim, num2);
        }
        return num2;
    }

    @Deprecated
    public static long getLong(Object obj) {
        if (obj == null) {
            return 0;
        }
        String trim = obj.toString().trim();
        if (trim.endsWith("wx")) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.w("the value of " + obj + " use wx unit, which will be not supported soon after.");
            }
            try {
                return (long) transferWx(trim, 750.0f);
            } catch (NumberFormatException e) {
                WXLogUtils.e("Argument format error! value is " + obj, (Throwable) e);
                return 0;
            } catch (Exception e2) {
                WXLogUtils.e("Argument error! value is " + obj, (Throwable) e2);
                return 0;
            }
        } else if (!trim.endsWith("px")) {
            try {
                return Long.parseLong(trim);
            } catch (NumberFormatException e3) {
                WXLogUtils.e("Argument format error! value is " + obj, (Throwable) e3);
                return 0;
            } catch (Exception e4) {
                WXLogUtils.e("Argument error! value is " + obj, (Throwable) e4);
                return 0;
            }
        } else if (trim.length() <= 3 || (!trim.endsWith("rpx") && !trim.endsWith("upx"))) {
            try {
                return Long.parseLong(trim.substring(0, trim.indexOf("px")));
            } catch (NumberFormatException e5) {
                WXLogUtils.e("Argument format error! value is " + obj, (Throwable) e5);
                return 0;
            } catch (Exception e6) {
                WXLogUtils.e("Argument error! value is " + obj, (Throwable) e6);
                return 0;
            }
        } else {
            try {
                return (long) transferRpxAndUpx(trim.substring(0, trim.length() - 3), 750.0f);
            } catch (NumberFormatException e7) {
                WXLogUtils.e("Argument format error! value is " + obj, (Throwable) e7);
                return 0;
            } catch (Exception e8) {
                WXLogUtils.e("Argument error! value is " + obj, (Throwable) e8);
                return 0;
            }
        }
    }

    @Deprecated
    public static double getDouble(Object obj) {
        if (obj == null) {
            return 0.0d;
        }
        String trim = obj.toString().trim();
        if (trim.endsWith("wx")) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.w("the value of " + obj + " use wx unit, which will be not supported soon after.");
            }
            try {
                return (double) transferWx(trim, 750.0f);
            } catch (NumberFormatException e) {
                WXLogUtils.e("Argument format error! value is " + obj, (Throwable) e);
                return 0.0d;
            } catch (Exception e2) {
                WXLogUtils.e("Argument error! value is " + obj, (Throwable) e2);
                return 0.0d;
            }
        } else if (!trim.endsWith("px")) {
            try {
                return Double.parseDouble(trim);
            } catch (NumberFormatException e3) {
                WXLogUtils.e("Argument format error! value is " + obj, (Throwable) e3);
                return 0.0d;
            } catch (Exception e4) {
                WXLogUtils.e("Argument error! value is " + obj, (Throwable) e4);
                return 0.0d;
            }
        } else if (trim.length() <= 3 || (!trim.endsWith("rpx") && !trim.endsWith("upx"))) {
            try {
                return Double.parseDouble(trim.substring(0, trim.indexOf("px")));
            } catch (NumberFormatException e5) {
                WXLogUtils.e("Argument format error! value is " + obj, (Throwable) e5);
                return 0.0d;
            } catch (Exception e6) {
                WXLogUtils.e("Argument error! value is " + obj, (Throwable) e6);
                return 0.0d;
            }
        } else {
            try {
                return (double) transferRpxAndUpx(trim.substring(0, trim.length() - 3), 750.0f);
            } catch (NumberFormatException e7) {
                WXLogUtils.e("Argument format error! value is " + obj, (Throwable) e7);
                return 0.0d;
            } catch (Exception e8) {
                WXLogUtils.e("Argument error! value is " + obj, (Throwable) e8);
                return 0.0d;
            }
        }
    }

    @Deprecated
    public static boolean isTabletDevice() {
        try {
            return (WXEnvironment.getApplication().getResources().getConfiguration().screenLayout & 15) >= 3;
        } catch (Exception unused) {
            return false;
        }
    }

    public static Boolean getBoolean(Object obj, Boolean bool) {
        if (obj == null) {
            return bool;
        }
        if (TextUtils.equals(AbsoluteConst.FALSE, obj.toString())) {
            return false;
        }
        if (TextUtils.equals(AbsoluteConst.TRUE, obj.toString())) {
            return true;
        }
        return bool;
    }

    public static long getAvailMemory(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(memoryInfo);
        WXLogUtils.w("app AvailMemory ---->>>" + (memoryInfo.availMem / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED));
        return memoryInfo.availMem / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED;
    }

    public static String getString(Object obj, String str) {
        if (obj == null) {
            return str;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        return obj.toString();
    }

    public static int parseUnitOrPercent(String str, int i) {
        int lastIndexOf = str.lastIndexOf(37);
        if (lastIndexOf != -1) {
            return parsePercent(str.substring(0, lastIndexOf), i);
        }
        return parseInt(str);
    }

    private static int parsePercent(String str, int i) {
        return (int) ((Float.parseFloat(str) / 100.0f) * ((float) i));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x000b, code lost:
        r0 = r0 + 3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getBundleBanner(java.lang.String r6) {
        /*
            java.lang.String r0 = "/*!"
            int r0 = r6.indexOf(r0)
            r1 = 0
            r2 = -1
            if (r0 != r2) goto L_0x000b
            return r1
        L_0x000b:
            int r0 = r0 + 3
            int r3 = indexLineBreak(r6, r0)
            if (r3 != r2) goto L_0x0014
            return r1
        L_0x0014:
            java.lang.String r0 = r6.substring(r0, r3)
            int r0 = java.lang.Integer.parseInt(r0)
            int r3 = r3 + 1
            int r0 = r0 + r3
            java.lang.String r6 = r6.substring(r3, r0)
            java.lang.String r0 = "!*/"
            int r0 = r6.lastIndexOf(r0)
            if (r0 != r2) goto L_0x002c
            return r1
        L_0x002c:
            r1 = 0
            java.lang.String r6 = r6.substring(r1, r0)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String[] r6 = splitLineBreak(r6)
            int r2 = r6.length
        L_0x003b:
            if (r1 >= r2) goto L_0x004d
            r3 = r6[r1]
            java.lang.String r4 = "\\*"
            java.lang.String r5 = ""
            java.lang.String r3 = r3.replaceFirst(r4, r5)
            r0.append(r3)
            int r1 = r1 + 1
            goto L_0x003b
        L_0x004d:
            java.lang.String r6 = r0.toString()
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.utils.WXUtils.getBundleBanner(java.lang.String):java.lang.String");
    }

    private static int indexLineBreak(String str, int i) {
        int indexOf = str.indexOf("\r", i);
        if (indexOf == -1) {
            indexOf = str.indexOf("\n", i);
        }
        return indexOf == -1 ? str.indexOf("\r\n", i) : indexOf;
    }

    private static String[] splitLineBreak(String str) {
        String[] split = str.split("\r");
        if (split.length == 1) {
            split = str.split("\n");
        }
        return split.length == 1 ? str.split("\r\n") : split;
    }

    public static int getNumberInt(Object obj, int i) {
        if (obj == null) {
            return i;
        }
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        try {
            String obj2 = obj.toString();
            if (obj2.indexOf(46) >= 0) {
                return (int) Float.parseFloat(obj.toString());
            }
            return Integer.parseInt(obj2);
        } catch (Exception unused) {
            return i;
        }
    }

    public static boolean checkGreyConfig(String str, String str2, String str3) {
        IWXConfigAdapter wxConfigAdapter = WXSDKManager.getInstance().getWxConfigAdapter();
        if (wxConfigAdapter == null) {
            return false;
        }
        double d = 100.0d;
        double random = Math.random() * 100.0d;
        try {
            d = Double.valueOf(wxConfigAdapter.getConfig(str, str2, str3)).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (random < d) {
            return true;
        }
        return false;
    }

    public static long getFixUnixTime() {
        return sInterval + SystemClock.uptimeMillis();
    }
}
