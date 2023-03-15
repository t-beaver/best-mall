package io.dcloud.common.util.language;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.common.adapter.util.DeviceInfo;
import java.util.Locale;

public class LanguageUtil {
    public static final String LanguageBroadCastIntent = "language_uni_broad_cast_intent";
    public static final String LanguageConfigKey = "language_uni_current_key";
    public static final String LanguageConfigSPFile = "language_uni_sp_file";
    private static String deviceDefCountry = "";
    private static String deviceDefLocalLanguage = "";
    private static String sCurrentLocalLanguage = "";

    public static Locale getCurrentLocal(Context context, boolean z) {
        if (context == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= 24) {
            LocaleList locales = context.getResources().getConfiguration().getLocales();
            if (z) {
                context.getResources();
                locales = Resources.getSystem().getConfiguration().getLocales();
            }
            if (locales == null || locales.size() <= 0) {
                return context.getResources().getConfiguration().locale;
            }
            return locales.get(0);
        } else if (!z) {
            return context.getResources().getConfiguration().locale;
        } else {
            context.getResources();
            return Resources.getSystem().getConfiguration().locale;
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x006b, code lost:
        if (r7.equals("es") == false) goto L_0x0050;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getCurrentLocaleLanguage(android.content.Context r7) {
        /*
            java.lang.String r0 = sCurrentLocalLanguage
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L_0x00a5
            r0 = 1
            java.util.Locale r7 = getCurrentLocal(r7, r0)
            if (r7 != 0) goto L_0x0012
            java.lang.String r7 = deviceDefLocalLanguage
            return r7
        L_0x0012:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = r7.getLanguage()
            r1.append(r2)
            java.lang.String r2 = "-"
            r1.append(r2)
            java.lang.String r2 = r7.getCountry()
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            int r2 = android.os.Build.VERSION.SDK_INT
            r3 = 21
            if (r2 <= r3) goto L_0x0038
            java.lang.String r1 = r7.toLanguageTag()
        L_0x0038:
            java.lang.String r7 = r7.getLanguage()
            r7.hashCode()
            r7.hashCode()
            r2 = -1
            int r3 = r7.hashCode()
            java.lang.String r4 = "fr"
            java.lang.String r5 = "es"
            java.lang.String r6 = "en"
            switch(r3) {
                case 3241: goto L_0x006e;
                case 3246: goto L_0x0067;
                case 3276: goto L_0x005e;
                case 3886: goto L_0x0052;
                default: goto L_0x0050;
            }
        L_0x0050:
            r0 = -1
            goto L_0x0076
        L_0x0052:
            java.lang.String r0 = "zh"
            boolean r7 = r7.equals(r0)
            if (r7 != 0) goto L_0x005c
            goto L_0x0050
        L_0x005c:
            r0 = 3
            goto L_0x0076
        L_0x005e:
            boolean r7 = r7.equals(r4)
            if (r7 != 0) goto L_0x0065
            goto L_0x0050
        L_0x0065:
            r0 = 2
            goto L_0x0076
        L_0x0067:
            boolean r7 = r7.equals(r5)
            if (r7 != 0) goto L_0x0076
            goto L_0x0050
        L_0x006e:
            boolean r7 = r7.equals(r6)
            if (r7 != 0) goto L_0x0075
            goto L_0x0050
        L_0x0075:
            r0 = 0
        L_0x0076:
            switch(r0) {
                case 0: goto L_0x00a4;
                case 1: goto L_0x00a3;
                case 2: goto L_0x00a2;
                case 3: goto L_0x007a;
                default: goto L_0x0079;
            }
        L_0x0079:
            return r1
        L_0x007a:
            java.lang.String r7 = "zh-CN"
            boolean r7 = r1.equalsIgnoreCase(r7)
            if (r7 == 0) goto L_0x0087
            java.lang.String r7 = "zh-Hans"
            return r7
        L_0x0087:
            java.lang.String r7 = "zh-HK"
            boolean r7 = r1.equalsIgnoreCase(r7)
            if (r7 == 0) goto L_0x0094
            java.lang.String r7 = "zh-Hant-HK"
            return r7
        L_0x0094:
            java.lang.String r7 = "zh-TW"
            boolean r7 = r1.equalsIgnoreCase(r7)
            if (r7 == 0) goto L_0x00a1
            java.lang.String r7 = "zh-Hant-TW"
            return r7
        L_0x00a1:
            return r1
        L_0x00a2:
            return r4
        L_0x00a3:
            return r5
        L_0x00a4:
            return r6
        L_0x00a5:
            java.lang.String r7 = sCurrentLocalLanguage
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.language.LanguageUtil.getCurrentLocaleLanguage(android.content.Context):java.lang.String");
    }

    public static String getDeviceDefCountry() {
        return deviceDefCountry;
    }

    public static String getDeviceDefLocalLanguage() {
        return deviceDefLocalLanguage;
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x00b6  */
    /* JADX WARNING: Removed duplicated region for block: B:32:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getString(com.alibaba.fastjson.JSONObject r7, java.lang.String r8) {
        /*
            if (r7 == 0) goto L_0x00ae
            android.content.Context r0 = io.dcloud.common.adapter.util.DeviceInfo.sApplicationContext
            java.lang.String r0 = getCurrentLocaleLanguage(r0)
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 != 0) goto L_0x00ae
            java.lang.String r1 = getString((java.lang.String) r0, (com.alibaba.fastjson.JSONObject) r7)
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 == 0) goto L_0x00af
            java.lang.String r2 = "zh-CN"
            boolean r2 = r0.equalsIgnoreCase(r2)
            if (r2 == 0) goto L_0x0029
            java.lang.String r1 = "zh-Hans"
            java.lang.String r1 = getString((java.lang.String) r1, (com.alibaba.fastjson.JSONObject) r7)
            goto L_0x004a
        L_0x0029:
            java.lang.String r2 = "zh-HK"
            boolean r2 = r0.equalsIgnoreCase(r2)
            if (r2 == 0) goto L_0x003a
            java.lang.String r1 = "zh-Hant-HK"
            java.lang.String r1 = getString((java.lang.String) r1, (com.alibaba.fastjson.JSONObject) r7)
            goto L_0x004a
        L_0x003a:
            java.lang.String r2 = "zh-TW"
            boolean r2 = r0.equalsIgnoreCase(r2)
            if (r2 == 0) goto L_0x004a
            java.lang.String r1 = "zh-Hant-TW"
            java.lang.String r1 = getString((java.lang.String) r1, (com.alibaba.fastjson.JSONObject) r7)
        L_0x004a:
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 == 0) goto L_0x00af
            java.lang.String r2 = "-"
            java.lang.String[] r0 = r0.split(r2)
            int r3 = r0.length
            r4 = 2
            r5 = 0
            if (r3 == r4) goto L_0x00a7
            r6 = 3
            if (r3 == r6) goto L_0x005f
            goto L_0x00af
        L_0x005f:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r3 = r0[r5]
            r1.append(r3)
            r1.append(r2)
            r3 = r0[r4]
            r1.append(r3)
            java.lang.String r1 = r1.toString()
            java.lang.String r1 = getString((java.lang.String) r1, (com.alibaba.fastjson.JSONObject) r7)
            boolean r3 = android.text.TextUtils.isEmpty(r1)
            if (r3 == 0) goto L_0x009a
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r3 = r0[r5]
            r1.append(r3)
            r1.append(r2)
            r2 = 1
            r2 = r0[r2]
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            java.lang.String r1 = getString((java.lang.String) r1, (com.alibaba.fastjson.JSONObject) r7)
        L_0x009a:
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 == 0) goto L_0x00af
            r0 = r0[r5]
            java.lang.String r1 = getString((java.lang.String) r0, (com.alibaba.fastjson.JSONObject) r7)
            goto L_0x00af
        L_0x00a7:
            r0 = r0[r5]
            java.lang.String r1 = getString((java.lang.String) r0, (com.alibaba.fastjson.JSONObject) r7)
            goto L_0x00af
        L_0x00ae:
            r1 = r8
        L_0x00af:
            boolean r7 = io.dcloud.common.util.PdrUtil.isEmpty(r1)
            if (r7 == 0) goto L_0x00b6
            goto L_0x00b7
        L_0x00b6:
            r8 = r1
        L_0x00b7:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.language.LanguageUtil.getString(com.alibaba.fastjson.JSONObject, java.lang.String):java.lang.String");
    }

    public static void initAppLanguageForAppBeforeO(Context context) {
        updateAppBootLanguage(context);
        updateSystemLanguage(context);
    }

    private static void updateAppBootLanguage(Context context) {
        String str;
        String str2 = "";
        String string = context.getSharedPreferences(LanguageConfigSPFile, 0).getString(LanguageConfigKey, str2);
        try {
            str = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.getString("DCLOUD_APP_DEFAULT_LANGUAGE");
        } catch (Exception e) {
            e.printStackTrace();
            str = str2;
        }
        if (TextUtils.isEmpty(string) && !TextUtils.isEmpty(str)) {
            string = str;
        }
        if (!"auto".equalsIgnoreCase(string)) {
            str2 = string;
        }
        sCurrentLocalLanguage = str2;
        updateDeviceDefLocalLanguage(context);
    }

    public static Context updateContextLanguageAfterO(Context context, boolean z) {
        return updateContextLanguageAfterO(context, z, true);
    }

    public static void updateDeviceDefLocalLanguage(Locale locale) {
        if (locale != null) {
            deviceDefLocalLanguage = locale.getLanguage() + Operators.SUB + locale.getCountry();
            deviceDefCountry = locale.getCountry();
            DeviceInfo.sLanguage = deviceDefLocalLanguage;
        }
    }

    public static void updateLanguage(Context context, String str) {
        if (context != null) {
            SharedPreferences.Editor edit = context.getSharedPreferences(LanguageConfigSPFile, 0).edit();
            edit.putString(LanguageConfigKey, str);
            edit.commit();
            sCurrentLocalLanguage = str;
            updateSystemLanguage(context);
        }
    }

    public static void updateSystemLanguage(Context context) {
        if (context != null) {
            String str = sCurrentLocalLanguage;
            if ("zh-Hant-TW".equals(str) || "zh-Hant".equals(str)) {
                str = "zh-TW";
            } else if ("zh-Hant-HK".equals(str)) {
                str = "zh-HK";
            }
            int i = Build.VERSION.SDK_INT;
            if (i >= 24) {
                Resources resources = context.getResources();
                DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                Configuration configuration = resources.getConfiguration();
                if (TextUtils.isEmpty(str)) {
                    str = deviceDefLocalLanguage;
                }
                LocaleList localeList = new LocaleList(new Locale[]{Locale.forLanguageTag(str)});
                LocaleList.setDefault(localeList);
                configuration.setLocales(localeList);
                resources.updateConfiguration(configuration, displayMetrics);
            } else if (i >= 21) {
                Resources resources2 = context.getResources();
                if (TextUtils.isEmpty(str)) {
                    str = deviceDefLocalLanguage;
                }
                Locale forLanguageTag = Locale.forLanguageTag(str);
                Locale.setDefault(forLanguageTag);
                Configuration configuration2 = resources2.getConfiguration();
                configuration2.setLocale(forLanguageTag);
                resources2.updateConfiguration(configuration2, resources2.getDisplayMetrics());
            }
        }
    }

    public static Context wrapContextConfigurationAfterO(Context context, String str) {
        return wrapContextConfigurationAfterO(context, str, true);
    }

    public static Context updateContextLanguageAfterO(Context context, boolean z, boolean z2) {
        if (z) {
            updateAppBootLanguage(context);
        }
        if (TextUtils.isEmpty(sCurrentLocalLanguage)) {
            return context;
        }
        return wrapContextConfigurationAfterO(context, sCurrentLocalLanguage, z2);
    }

    public static Context wrapContextConfigurationAfterO(Context context, String str, boolean z) {
        if (Build.VERSION.SDK_INT < 24) {
            return context;
        }
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        LocaleList localeList = new LocaleList(new Locale[]{Locale.forLanguageTag(str)});
        LocaleList.setDefault(localeList);
        configuration.setLocales(localeList);
        resources.updateConfiguration(configuration, displayMetrics);
        return z ? context.createConfigurationContext(configuration) : context;
    }

    public static void updateDeviceDefLocalLanguage(Context context) {
        updateDeviceDefLocalLanguage(getCurrentLocal(context, true));
    }

    private static String getString(String str, JSONObject jSONObject) {
        if (jSONObject == null || !jSONObject.containsKey(str)) {
            return null;
        }
        return jSONObject.getString(str);
    }
}
