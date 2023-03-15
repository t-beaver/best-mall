package io.dcloud.common.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import androidx.core.content.ContextCompat;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.f.a;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class TelephonyUtil {
    private static String AIDKEY = "aid";
    private static String AId = "";
    private static String IMEI1_KEY = "II1";
    private static String IMEI2_KEY = "II2";
    private static String IMSI_KEY = "isi";
    private static String MAC_KEY = "mc";
    private static String[] MultiIMEITemp = null;
    private static String MultiIMEI_KEY = "mmikey";
    private static final String OLD_UUID_FILE_NAME = ".imei.txt";
    public static final String TAG = "TelephonyUtil";
    private static final String UUID_FILE_NAME = ".DC4278477faeb9.txt";
    private static boolean isGetAId = false;
    private static boolean isGetIMSI = false;
    private static boolean isGetMac = false;
    private static boolean isGetMultiIMEI = false;
    private static Boolean isGetRdId = Boolean.FALSE;
    private static String mImei = "";
    private static String muuid = null;
    private static String randomId = null;
    private static String sIMSI = null;
    private static String sImei = "";
    private static String sImeiAndBakInfo;
    private static String sMac = null;
    private static String sOriginalImeiAndBakInfo;

    private static boolean checkPseudoData(String str) {
        if (PdrUtil.isEmpty(str)) {
            return true;
        }
        str.hashCode();
        if (!str.equals("00000000-0000-0000-0000-000000000000")) {
            return false;
        }
        return true;
    }

    private static String createRandomBSFile(Context context, File file, File file2, String str) throws IOException {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        String replace = UUID.randomUUID().toString().replaceAll(Operators.SUB, "").replace("\n", "");
        byte[] bytes = replace.getBytes();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
            if (!FileUtil.needMediaStoreOpenFile(context)) {
                if (file2 != null) {
                    DHFile.copyFile(file.getPath(), file2.getPath());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return replace;
    }

    public static String getAId(Context context) {
        if (!isGetAId) {
            if (ContextCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") != 0) {
                return AId;
            }
            SharedPreferences orCreateBundle = SP.getOrCreateBundle(context, SP.N_DEVICE_INFO);
            if (orCreateBundle.contains(AIDKEY)) {
                String string = orCreateBundle.getString(AIDKEY, (String) null);
                if (!PdrUtil.isEmpty(string)) {
                    AId = Base64.decodeString(string, true, 10);
                }
            } else {
                String string2 = Settings.Secure.getString(context.getContentResolver(), a.a("aWZsemdhbFdhbA=="));
                AId = string2;
                SP.setBundleData(orCreateBundle, AIDKEY, PdrUtil.isEmpty(string2) ? AId : Base64.encodeString(AId, true, 10));
            }
            isGetAId = true;
        }
        return AId;
    }

    private static String getAPSubId(Context context) {
        if (Build.VERSION.SDK_INT < 29) {
            if (!AppRuntime.hasPrivacyForNotShown(context)) {
                return "";
            }
            try {
                Object invokeMethod = ReflectUtils.invokeMethod(context.getSystemService("phone"), a.a("b218W31qe2t6YWptekFs"), new Class[0], new Object[0]);
                if (invokeMethod != null) {
                    return (String) invokeMethod;
                }
            } catch (Exception unused) {
            }
        }
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0034  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x005f A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0060  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getDCloudDeviceID(android.content.Context r4) {
        /*
            java.lang.String r0 = io.dcloud.common.util.BaseInfo.PDR
            java.lang.String r1 = "android_device_dcloud_id"
            java.lang.String r0 = io.dcloud.common.adapter.util.SP.getBundleData((android.content.Context) r4, (java.lang.String) r0, (java.lang.String) r1)
            boolean r2 = android.text.TextUtils.isEmpty(r0)
            if (r2 == 0) goto L_0x0069
            int r0 = android.os.Build.VERSION.SDK_INT
            r2 = 28
            java.lang.String r3 = ""
            if (r0 <= r2) goto L_0x002d
            java.lang.String r0 = io.dcloud.common.adapter.util.DeviceInfo.oaids
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r0)
            if (r0 != 0) goto L_0x002d
            java.lang.String r0 = io.dcloud.common.adapter.util.DeviceInfo.oaids
            java.lang.String r2 = "\\|"
            java.lang.String[] r0 = r0.split(r2)
            int r2 = r0.length
            if (r2 <= 0) goto L_0x002d
            r2 = 0
            r0 = r0[r2]
            goto L_0x002e
        L_0x002d:
            r0 = r3
        L_0x002e:
            boolean r2 = checkPseudoData(r0)
            if (r2 == 0) goto L_0x0059
            java.lang.String[] r0 = MultiIMEITemp
            if (r0 == 0) goto L_0x003f
            java.lang.String r2 = ","
            java.lang.String r0 = android.text.TextUtils.join(r2, r0)
            goto L_0x0040
        L_0x003f:
            r0 = r3
        L_0x0040:
            boolean r2 = checkPseudoData(r0)
            if (r2 == 0) goto L_0x0059
            java.lang.String r0 = sIMSI
            boolean r2 = checkPseudoData(r0)
            if (r2 == 0) goto L_0x0059
            java.lang.String r0 = getRandomId(r4)
            boolean r2 = checkPseudoData(r0)
            if (r2 == 0) goto L_0x0059
            r0 = r3
        L_0x0059:
            boolean r2 = io.dcloud.common.util.PdrUtil.isEmpty(r0)
            if (r2 == 0) goto L_0x0060
            return r3
        L_0x0060:
            java.lang.String r0 = io.dcloud.common.util.Md5Utils.md5LowerCase32Bit(r0)
            java.lang.String r2 = io.dcloud.common.util.BaseInfo.PDR
            io.dcloud.common.adapter.util.SP.setBundleData(r4, r2, r1, r0)
        L_0x0069:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.TelephonyUtil.getDCloudDeviceID(android.content.Context):java.lang.String");
    }

    public static String getIMEI(Context context) {
        return getIMEI(context, true);
    }

    public static String getIMEIS(Context context) {
        try {
            String[] multiIMEI = getMultiIMEI(context);
            if (multiIMEI != null) {
                return TextUtils.join(",", multiIMEI);
            }
        } catch (Exception unused) {
        }
        return "";
    }

    public static String getIMSI(Context context) {
        if (context == null) {
            return "";
        }
        try {
            if (ContextCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") != 0) {
                return "";
            }
            boolean hasPrivacyForNotShown = AppRuntime.hasPrivacyForNotShown(context);
            if (!isGetIMSI) {
                if (!hasPrivacyForNotShown) {
                    String str = sIMSI;
                    if (str != null) {
                        return str;
                    }
                    SharedPreferences orCreateBundle = SP.getOrCreateBundle(context, SP.N_DEVICE_INFO);
                    if (orCreateBundle.contains(IMSI_KEY)) {
                        String string = orCreateBundle.getString(IMSI_KEY, (String) null);
                        if (!PdrUtil.isEmpty(string)) {
                            sIMSI = Base64.decodeString(string, true, 10);
                        }
                    } else {
                        int subId = getSubId(0, context);
                        int subId2 = getSubId(1, context);
                        if (subId == -1 && subId2 == -1) {
                            sIMSI = getAPSubId(context);
                        } else {
                            String str2 = (String) getPhoneInfo(subId, context);
                            String str3 = (String) getPhoneInfo(subId2, context);
                            if (!PdrUtil.isEmpty(str2)) {
                                sIMSI = str2;
                                if (!PdrUtil.isEmpty(str3) && !str2.equals(str3)) {
                                    sIMSI += "," + str3;
                                }
                            } else if (!PdrUtil.isEmpty(str3)) {
                                sIMSI = str3;
                            } else {
                                sIMSI = getAPSubId(context);
                            }
                        }
                        SP.setBundleData(orCreateBundle, IMSI_KEY, PdrUtil.isEmpty(sIMSI) ? sIMSI : Base64.encodeString(sIMSI, true, 10));
                    }
                    isGetIMSI = true;
                    return sIMSI;
                }
            }
            return sIMSI;
        } catch (Exception unused) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:48:0x00ca  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00d4  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String[] getMultiIMEI(android.content.Context r11) {
        /*
            java.lang.String r0 = "getDeviceId"
            java.lang.String r1 = "android.permission.READ_PHONE_STATE"
            int r1 = androidx.core.content.ContextCompat.checkSelfPermission(r11, r1)
            r2 = 0
            if (r1 == 0) goto L_0x000c
            return r2
        L_0x000c:
            boolean r1 = io.dcloud.common.util.AppRuntime.hasPrivacyForNotShown(r11)
            if (r1 == 0) goto L_0x0015
            java.lang.String[] r11 = MultiIMEITemp
            return r11
        L_0x0015:
            boolean r1 = isGetMultiIMEI
            if (r1 == 0) goto L_0x001c
            java.lang.String[] r11 = MultiIMEITemp
            return r11
        L_0x001c:
            java.lang.String r1 = "device_info"
            android.content.SharedPreferences r1 = io.dcloud.common.adapter.util.SP.getOrCreateBundle((android.content.Context) r11, (java.lang.String) r1)
            java.lang.String r3 = MultiIMEI_KEY
            boolean r3 = r1.contains(r3)
            r4 = 10
            r5 = 0
            r6 = 1
            if (r3 == 0) goto L_0x0059
            java.lang.String r11 = MultiIMEI_KEY
            java.lang.String r11 = r1.getString(r11, r2)
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r11)
            if (r0 != 0) goto L_0x0053
            java.lang.String r11 = io.dcloud.common.util.Base64.decodeString(r11, r6, r4)
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0050 }
            r0.<init>(r11)     // Catch:{ JSONException -> 0x0050 }
            java.lang.String r11 = IMEI1_KEY     // Catch:{ JSONException -> 0x0050 }
            java.lang.String r11 = r0.optString(r11)     // Catch:{ JSONException -> 0x0050 }
            java.lang.String r1 = IMEI2_KEY     // Catch:{ JSONException -> 0x0051 }
            java.lang.String r0 = r0.optString(r1)     // Catch:{ JSONException -> 0x0051 }
            goto L_0x0055
        L_0x0050:
            r11 = r2
        L_0x0051:
            r0 = r2
            goto L_0x0055
        L_0x0053:
            r11 = r2
            r0 = r11
        L_0x0055:
            isGetMultiIMEI = r6
            goto L_0x00be
        L_0x0059:
            java.lang.String r3 = "phone"
            java.lang.Object r11 = r11.getSystemService(r3)
            android.telephony.TelephonyManager r11 = (android.telephony.TelephonyManager) r11
            java.lang.Class r3 = r11.getClass()     // Catch:{ Exception -> 0x00bc }
            java.lang.String r3 = r3.getName()     // Catch:{ Exception -> 0x00bc }
            java.lang.Object r3 = io.dcloud.common.adapter.util.PlatformUtil.invokeMethod(r3, r0, r11)     // Catch:{ Exception -> 0x00bc }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Exception -> 0x00bc }
            java.lang.Class r7 = r11.getClass()     // Catch:{ Exception -> 0x00bc }
            java.lang.String r7 = r7.getName()     // Catch:{ Exception -> 0x00bc }
            java.lang.Class[] r8 = new java.lang.Class[r6]     // Catch:{ Exception -> 0x00bc }
            java.lang.Class r9 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x00bc }
            r8[r5] = r9     // Catch:{ Exception -> 0x00bc }
            java.lang.Object[] r9 = new java.lang.Object[r6]     // Catch:{ Exception -> 0x00bc }
            java.lang.Integer r10 = java.lang.Integer.valueOf(r6)     // Catch:{ Exception -> 0x00bc }
            r9[r5] = r10     // Catch:{ Exception -> 0x00bc }
            java.lang.Object r11 = io.dcloud.common.adapter.util.PlatformUtil.invokeMethod(r7, r0, r11, r8, r9)     // Catch:{ Exception -> 0x00bc }
            java.lang.String r11 = (java.lang.String) r11     // Catch:{ Exception -> 0x00bc }
            boolean r0 = isUnValid(r3)     // Catch:{ Exception -> 0x00bc }
            if (r0 != 0) goto L_0x0092
            goto L_0x0093
        L_0x0092:
            r3 = r2
        L_0x0093:
            boolean r0 = isUnValid(r11)     // Catch:{ Exception -> 0x00b9 }
            if (r0 != 0) goto L_0x009b
            r0 = r11
            goto L_0x009c
        L_0x009b:
            r0 = r2
        L_0x009c:
            org.json.JSONObject r11 = new org.json.JSONObject     // Catch:{ Exception -> 0x00ba }
            r11.<init>()     // Catch:{ Exception -> 0x00ba }
            java.lang.String r7 = IMEI1_KEY     // Catch:{ Exception -> 0x00ba }
            r11.put(r7, r3)     // Catch:{ Exception -> 0x00ba }
            java.lang.String r7 = IMEI2_KEY     // Catch:{ Exception -> 0x00ba }
            r11.put(r7, r0)     // Catch:{ Exception -> 0x00ba }
            java.lang.String r11 = r11.toString()     // Catch:{ Exception -> 0x00ba }
            java.lang.String r11 = io.dcloud.common.util.Base64.encodeString(r11, r6, r4)     // Catch:{ Exception -> 0x00ba }
            java.lang.String r4 = MultiIMEI_KEY     // Catch:{ Exception -> 0x00ba }
            io.dcloud.common.adapter.util.SP.setBundleData((android.content.SharedPreferences) r1, (java.lang.String) r4, (java.lang.String) r11)     // Catch:{ Exception -> 0x00ba }
            goto L_0x00ba
        L_0x00b9:
            r0 = r2
        L_0x00ba:
            r11 = r3
            goto L_0x00be
        L_0x00bc:
            r11 = r2
            r0 = r11
        L_0x00be:
            boolean r1 = isUnValid(r11)
            if (r1 != 0) goto L_0x00d4
            boolean r1 = isUnValid(r0)
            if (r1 != 0) goto L_0x00d4
            r1 = 2
            java.lang.String[] r1 = new java.lang.String[r1]
            r1[r5] = r11
            r1[r6] = r0
            MultiIMEITemp = r1
            goto L_0x00f0
        L_0x00d4:
            boolean r1 = isUnValid(r11)
            if (r1 != 0) goto L_0x00e1
            java.lang.String[] r0 = new java.lang.String[r6]
            r0[r5] = r11
            MultiIMEITemp = r0
            goto L_0x00f0
        L_0x00e1:
            boolean r11 = isUnValid(r0)
            if (r11 != 0) goto L_0x00ee
            java.lang.String[] r11 = new java.lang.String[r6]
            r11[r5] = r0
            MultiIMEITemp = r11
            goto L_0x00f0
        L_0x00ee:
            MultiIMEITemp = r2
        L_0x00f0:
            isGetMultiIMEI = r6
            java.lang.String[] r11 = MultiIMEITemp
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.TelephonyUtil.getMultiIMEI(android.content.Context):java.lang.String[]");
    }

    private static Object getPhoneInfo(int i, Context context) {
        try {
            Object systemService = context.getSystemService("phone");
            String a = a.a("b218W31qe2t6YWptekFs");
            int i2 = Build.VERSION.SDK_INT;
            if (i2 > 21) {
                return ReflectUtils.invokeMethod(systemService, a, new Class[]{Integer.TYPE}, new Object[]{Integer.valueOf(i)});
            }
            if (i2 == 21) {
                return ReflectUtils.invokeMethod(systemService, a, new Class[]{Long.TYPE}, new Object[]{Integer.valueOf(i)});
            }
            return null;
        } catch (Exception unused) {
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(9:(3:32|33|(8:35|37|(3:39|(1:41)|42)|43|44|(1:46)|53|54))|36|37|(0)|43|44|(0)|53|54) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x0100 */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00da A[Catch:{ Exception -> 0x0100 }] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0106 A[Catch:{ Exception -> 0x0113 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getRandomId(android.content.Context r11) {
        /*
            java.lang.String r0 = ".imei.txt"
            java.lang.Boolean r1 = isGetRdId
            boolean r1 = r1.booleanValue()
            if (r1 == 0) goto L_0x000d
            java.lang.String r11 = randomId
            return r11
        L_0x000d:
            r1 = 0
            boolean r2 = io.dcloud.common.util.FileUtil.needMediaStoreOpenFile(r11)     // Catch:{ Exception -> 0x0113 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0113 }
            r3.<init>()     // Catch:{ Exception -> 0x0113 }
            java.io.File r4 = r11.getFilesDir()     // Catch:{ Exception -> 0x0113 }
            r3.append(r4)     // Catch:{ Exception -> 0x0113 }
            java.lang.String r4 = java.io.File.separator     // Catch:{ Exception -> 0x0113 }
            r3.append(r4)     // Catch:{ Exception -> 0x0113 }
            r3.append(r0)     // Catch:{ Exception -> 0x0113 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0113 }
            java.io.File r5 = new java.io.File     // Catch:{ Exception -> 0x0113 }
            r5.<init>(r3)     // Catch:{ Exception -> 0x0113 }
            boolean r6 = r5.exists()     // Catch:{ Exception -> 0x0113 }
            java.lang.String r7 = ".DC4278477faeb9.txt"
            if (r6 != 0) goto L_0x0052
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0113 }
            r3.<init>()     // Catch:{ Exception -> 0x0113 }
            java.io.File r5 = r11.getFilesDir()     // Catch:{ Exception -> 0x0113 }
            r3.append(r5)     // Catch:{ Exception -> 0x0113 }
            r3.append(r4)     // Catch:{ Exception -> 0x0113 }
            r3.append(r7)     // Catch:{ Exception -> 0x0113 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0113 }
            java.io.File r5 = new java.io.File     // Catch:{ Exception -> 0x0113 }
            r5.<init>(r3)     // Catch:{ Exception -> 0x0113 }
        L_0x0052:
            java.lang.String r6 = "mounted"
            java.lang.String r8 = android.os.Environment.getExternalStorageState()     // Catch:{ Exception -> 0x0113 }
            boolean r6 = r6.equalsIgnoreCase(r8)     // Catch:{ Exception -> 0x0113 }
            if (r6 == 0) goto L_0x00a5
            if (r2 != 0) goto L_0x00a5
            java.lang.String r6 = "android.permission.WRITE_EXTERNAL_STORAGE"
            int r6 = androidx.core.content.ContextCompat.checkSelfPermission(r11, r6)     // Catch:{ Exception -> 0x0113 }
            if (r6 != 0) goto L_0x00a5
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0113 }
            r6.<init>()     // Catch:{ Exception -> 0x0113 }
            java.io.File r8 = android.os.Environment.getExternalStorageDirectory()     // Catch:{ Exception -> 0x0113 }
            r6.append(r8)     // Catch:{ Exception -> 0x0113 }
            r6.append(r4)     // Catch:{ Exception -> 0x0113 }
            r6.append(r0)     // Catch:{ Exception -> 0x0113 }
            java.lang.String r0 = r6.toString()     // Catch:{ Exception -> 0x0113 }
            java.io.File r6 = new java.io.File     // Catch:{ Exception -> 0x0113 }
            r6.<init>(r0)     // Catch:{ Exception -> 0x0113 }
            boolean r8 = r6.exists()     // Catch:{ Exception -> 0x0113 }
            if (r8 != 0) goto L_0x00a7
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0113 }
            r0.<init>()     // Catch:{ Exception -> 0x0113 }
            java.io.File r6 = android.os.Environment.getExternalStorageDirectory()     // Catch:{ Exception -> 0x0113 }
            r0.append(r6)     // Catch:{ Exception -> 0x0113 }
            r0.append(r4)     // Catch:{ Exception -> 0x0113 }
            r0.append(r7)     // Catch:{ Exception -> 0x0113 }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x0113 }
            java.io.File r6 = new java.io.File     // Catch:{ Exception -> 0x0113 }
            r6.<init>(r0)     // Catch:{ Exception -> 0x0113 }
            goto L_0x00a7
        L_0x00a5:
            r0 = r1
            r6 = r0
        L_0x00a7:
            boolean r4 = r5.isDirectory()     // Catch:{ Exception -> 0x0113 }
            if (r4 == 0) goto L_0x00b0
            r5.delete()     // Catch:{ Exception -> 0x0113 }
        L_0x00b0:
            if (r6 == 0) goto L_0x010b
            boolean r4 = r6.exists()     // Catch:{ Exception -> 0x0113 }
            if (r4 == 0) goto L_0x010b
            long r7 = r6.length()     // Catch:{ Exception -> 0x0113 }
            r9 = 0
            int r4 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r4 <= 0) goto L_0x010b
            if (r2 == 0) goto L_0x00cb
            int r2 = android.os.Build.VERSION.SDK_INT     // Catch:{ Exception -> 0x0100 }
            r4 = 29
            if (r2 < r4) goto L_0x00cb
            goto L_0x00d4
        L_0x00cb:
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0100 }
            r2.<init>(r6)     // Catch:{ Exception -> 0x0100 }
            java.lang.String r1 = io.dcloud.common.util.IOUtil.toString(r2)     // Catch:{ Exception -> 0x0100 }
        L_0x00d4:
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x0100 }
            if (r2 != 0) goto L_0x0100
            java.io.File r2 = r5.getParentFile()     // Catch:{ Exception -> 0x0100 }
            boolean r2 = r2.exists()     // Catch:{ Exception -> 0x0100 }
            if (r2 != 0) goto L_0x00ee
            java.io.File r2 = r5.getParentFile()     // Catch:{ Exception -> 0x0100 }
            r2.mkdirs()     // Catch:{ Exception -> 0x0100 }
            r5.createNewFile()     // Catch:{ Exception -> 0x0100 }
        L_0x00ee:
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0100 }
            r2.<init>(r5)     // Catch:{ Exception -> 0x0100 }
            byte[] r4 = r1.getBytes()     // Catch:{ Exception -> 0x0100 }
            r2.write(r4)     // Catch:{ Exception -> 0x0100 }
            r2.flush()     // Catch:{ Exception -> 0x0100 }
            r2.close()     // Catch:{ Exception -> 0x0100 }
        L_0x0100:
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x0113 }
            if (r2 == 0) goto L_0x0117
            java.lang.String r11 = savePublicFile(r5, r6, r3, r0, r11)     // Catch:{ Exception -> 0x0113 }
            goto L_0x010f
        L_0x010b:
            java.lang.String r11 = savePublicFile(r5, r6, r3, r0, r11)     // Catch:{ Exception -> 0x0113 }
        L_0x010f:
            r1 = r11
            goto L_0x0117
        L_0x0111:
            r11 = move-exception
            goto L_0x011e
        L_0x0113:
            r11 = move-exception
            r11.printStackTrace()     // Catch:{ all -> 0x0111 }
        L_0x0117:
            randomId = r1
            java.lang.Boolean r11 = java.lang.Boolean.TRUE
            isGetRdId = r11
            return r1
        L_0x011e:
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.TelephonyUtil.getRandomId(android.content.Context):java.lang.String");
    }

    public static String getSBBS(Context context, boolean z, boolean z2) {
        return getSBBS(context, z, z2, true);
    }

    public static String getSimOperator(Context context) {
        if (!AppRuntime.hasPrivacyForNotShown(context)) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager.getSimState() == 5) {
                return telephonyManager.getSimOperator();
            }
        }
        return "";
    }

    private static int getSubId(int i, Context context) {
        Uri parse = Uri.parse("content://telephony/siminfo");
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            String[] strArr = {"_id", "sim_id"};
            cursor = contentResolver.query(parse, strArr, "sim_id = ?", new String[]{String.valueOf(i)}, (String) null);
            if (cursor == null || !cursor.moveToFirst()) {
                if (cursor == null) {
                    return -1;
                }
                cursor.close();
                return -1;
            }
            int i2 = cursor.getInt(cursor.getColumnIndex("_id"));
            cursor.close();
            return i2;
        } catch (Exception unused) {
            if (cursor == null) {
                return -1;
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public static String getUUID(Context context) {
        String str = "";
        if (context == null) {
            return str;
        }
        if (!TextUtils.isEmpty(muuid)) {
            return muuid;
        }
        try {
            String string = Settings.System.getString(context.getContentResolver(), a.a("aWZsemdhbFdhbA=="));
            if (string != null) {
                str = string;
            }
            return str;
        } finally {
            muuid = str;
        }
    }

    @Deprecated
    public static String getWifiData(Context context) {
        Object invokeMethod;
        String str = null;
        if (AppRuntime.hasPrivacyForNotShown(context)) {
            return null;
        }
        if (isGetMac) {
            return sMac;
        }
        SharedPreferences orCreateBundle = SP.getOrCreateBundle(context, SP.N_DEVICE_INFO);
        if (orCreateBundle.contains(MAC_KEY)) {
            String string = orCreateBundle.getString(MAC_KEY, (String) null);
            if (!PdrUtil.isEmpty(string)) {
                sMac = Base64.decodeString(string, true, 10);
            }
        } else {
            Object systemService = context.getSystemService(a.a("f2FuYQ=="));
            if (!(systemService == null || (invokeMethod = ReflectUtils.invokeMethod(systemService, a.a("b218S2dmZm1rfGFnZkFmbmc"), new Class[0], new Object[0])) == null)) {
                Object invokeMethod2 = ReflectUtils.invokeMethod(invokeMethod, a.a("b218RWlrSWxsem17ew"), new Class[0], new Object[0]);
                String str2 = invokeMethod2 != null ? (String) invokeMethod2 : null;
                if (!TextUtils.isEmpty(str2)) {
                    str = str2.replace(":", "");
                }
            }
            sMac = str;
            SP.setBundleData(orCreateBundle, MAC_KEY, PdrUtil.isEmpty(str) ? sMac : Base64.encodeString(sMac, true, 10));
        }
        isGetMac = true;
        return sMac;
    }

    private static boolean isUnValid(String str) {
        return TextUtils.isEmpty(str) || str.contains("Unknown") || str.contains("00000000");
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:24:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String savePublicFile(java.io.File r6, java.io.File r7, java.lang.String r8, java.lang.String r9, android.content.Context r10) throws java.io.IOException {
        /*
            boolean r0 = r6.exists()
            java.lang.String r1 = ".DC4278477faeb9.txt"
            if (r0 == 0) goto L_0x0047
            long r2 = r6.length()
            r4 = 0
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 <= 0) goto L_0x0047
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ Exception -> 0x003e }
            r0.<init>(r6)     // Catch:{ Exception -> 0x003e }
            java.lang.String r0 = io.dcloud.common.util.IOUtil.toString(r0)     // Catch:{ Exception -> 0x003e }
            if (r7 == 0) goto L_0x004b
            boolean r2 = io.dcloud.common.util.FileUtil.needMediaStoreOpenFile(r10)     // Catch:{ Exception -> 0x003c }
            if (r2 == 0) goto L_0x0024
            goto L_0x004b
        L_0x0024:
            java.io.File r2 = r7.getParentFile()     // Catch:{ Exception -> 0x003c }
            boolean r2 = r2.exists()     // Catch:{ Exception -> 0x003c }
            if (r2 != 0) goto L_0x0038
            java.io.File r2 = r7.getParentFile()     // Catch:{ Exception -> 0x003c }
            r2.mkdirs()     // Catch:{ Exception -> 0x003c }
            r7.createNewFile()     // Catch:{ Exception -> 0x003c }
        L_0x0038:
            io.dcloud.common.adapter.io.DHFile.copyFile(r8, r9)     // Catch:{ Exception -> 0x003c }
            goto L_0x004b
        L_0x003c:
            goto L_0x0040
        L_0x003e:
            r8 = 0
            r0 = r8
        L_0x0040:
            if (r0 != 0) goto L_0x004b
            java.lang.String r0 = createRandomBSFile(r10, r6, r7, r1)
            goto L_0x004b
        L_0x0047:
            java.lang.String r0 = createRandomBSFile(r10, r6, r7, r1)
        L_0x004b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.TelephonyUtil.savePublicFile(java.io.File, java.io.File, java.lang.String, java.lang.String, android.content.Context):java.lang.String");
    }

    public static String updateIMEI(Context context) {
        if (!PdrUtil.isEmpty(mImei)) {
            return mImei;
        }
        String[] multiIMEI = getMultiIMEI(context);
        if (multiIMEI != null) {
            StringBuilder sb = new StringBuilder();
            for (String append : multiIMEI) {
                sb.append(append);
                sb.append(",");
            }
            if (sb.lastIndexOf(",") >= sb.length() - 1) {
                String sb2 = sb.deleteCharAt(sb.length() - 1).toString();
                mImei = sb2;
                return sb2;
            }
            String sb3 = sb.toString();
            mImei = sb3;
            return sb3;
        }
        mImei = "";
        return "";
    }

    public static String getIMEI(Context context, boolean z) {
        return getIMEI(context, z, false);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:117:0x0187, code lost:
        if (android.text.TextUtils.isEmpty(r7) != false) goto L_0x01a8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:119:0x0192, code lost:
        if (android.text.TextUtils.isEmpty(r1.toString()) != false) goto L_0x01b9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:126:0x01a1, code lost:
        if (android.text.TextUtils.isEmpty(r7) != false) goto L_0x01a8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:127:0x01a4, code lost:
        r3 = r7.replace("\n", r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:128:0x01a8, code lost:
        r1.append(r3);
        r1.append("|");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:130:0x01b7, code lost:
        if (android.text.TextUtils.isEmpty(r1.toString()) != false) goto L_0x01b9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:131:0x01b9, code lost:
        r1.append(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:132:0x01bc, code lost:
        r10 = java.net.URLEncoder.encode(android.util.Base64.encodeToString(io.dcloud.common.util.AESUtil.encrypt(io.dcloud.f.a.c(), io.dcloud.f.a.b(), r1.toString()), 2)) + "&ie=1";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:133:0x01e3, code lost:
        if (r4 != false) goto L_0x01f3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:134:0x01e5, code lost:
        sImeiAndBakInfo = r10;
        r10 = r1.toString();
        sOriginalImeiAndBakInfo = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:135:0x01ed, code lost:
        if (r12 == false) goto L_0x01f0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:136:0x01ef, code lost:
        return r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:138:0x01f2, code lost:
        return sImeiAndBakInfo;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:139:0x01f3, code lost:
        if (r12 == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:151:?, code lost:
        return r1.toString();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:152:?, code lost:
        return r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00d9, code lost:
        if (r11 != false) goto L_0x00fb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x00f9, code lost:
        if (r11 == false) goto L_0x0112;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00fb, code lost:
        r1.append(sImei);
        r1.append("|");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x0139, code lost:
        if (android.text.TextUtils.isEmpty(r8) != false) goto L_0x014b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x0149, code lost:
        if (android.text.TextUtils.isEmpty(r8) != false) goto L_0x014b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x014b, code lost:
        r8 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x014c, code lost:
        r1.append(r8);
        r1.append("|");
     */
    /* JADX WARNING: Removed duplicated region for block: B:116:0x0183  */
    /* JADX WARNING: Removed duplicated region for block: B:118:0x018a  */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x0157  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getSBBS(android.content.Context r10, boolean r11, boolean r12, boolean r13) {
        /*
            java.lang.String r0 = "\n"
            java.lang.StringBuffer r1 = new java.lang.StringBuffer
            r1.<init>()
            java.lang.String r2 = "|"
            if (r11 == 0) goto L_0x000f
            r1.append(r2)
        L_0x000f:
            java.lang.String r3 = ""
            if (r10 != 0) goto L_0x0019
            if (r11 == 0) goto L_0x0018
            java.lang.String r3 = "|||||"
        L_0x0018:
            return r3
        L_0x0019:
            boolean r4 = io.dcloud.common.util.AppRuntime.hasPrivacyForNotShown(r10)
            r5 = 1
            if (r13 != 0) goto L_0x0021
            r4 = 1
        L_0x0021:
            if (r12 == 0) goto L_0x003d
            if (r11 == 0) goto L_0x0030
            java.lang.String r13 = sOriginalImeiAndBakInfo
            boolean r13 = android.text.TextUtils.isEmpty(r13)
            if (r13 != 0) goto L_0x0030
            java.lang.String r10 = sOriginalImeiAndBakInfo
            return r10
        L_0x0030:
            java.lang.String r13 = sImei
            boolean r13 = isUnValid(r13)
            if (r13 != 0) goto L_0x0057
            if (r11 != 0) goto L_0x0057
            java.lang.String r10 = sImei
            return r10
        L_0x003d:
            if (r11 == 0) goto L_0x004a
            java.lang.String r13 = sImeiAndBakInfo
            boolean r13 = android.text.TextUtils.isEmpty(r13)
            if (r13 != 0) goto L_0x004a
            java.lang.String r10 = sImeiAndBakInfo
            return r10
        L_0x004a:
            java.lang.String r13 = sImei
            boolean r13 = isUnValid(r13)
            if (r13 != 0) goto L_0x0057
            if (r11 != 0) goto L_0x0057
            java.lang.String r10 = sImei
            return r10
        L_0x0057:
            java.lang.String r13 = sImei
            boolean r13 = isUnValid(r13)
            if (r13 != 0) goto L_0x0065
            if (r11 == 0) goto L_0x0062
            goto L_0x0065
        L_0x0062:
            java.lang.String r10 = sImei
            return r10
        L_0x0065:
            java.lang.String r13 = "&ie=1"
            r6 = 2
            if (r4 != 0) goto L_0x010f
            java.lang.String r7 = "pdr"
            java.lang.String r8 = "_dpush_uuid_"
            java.lang.String r7 = io.dcloud.common.adapter.util.SP.getBundleData((android.content.Context) r10, (java.lang.String) r7, (java.lang.String) r8)     // Catch:{ Exception -> 0x00de }
            boolean r8 = isUnValid(r7)     // Catch:{ Exception -> 0x00de }
            if (r8 != 0) goto L_0x00b5
            if (r11 == 0) goto L_0x00b5
            if (r12 == 0) goto L_0x0087
            if (r11 == 0) goto L_0x0086
            java.lang.String r10 = sImei
            r1.append(r10)
            r1.append(r2)
        L_0x0086:
            return r7
        L_0x0087:
            java.lang.String r5 = io.dcloud.f.a.c()     // Catch:{ Exception -> 0x00de }
            java.lang.String r8 = io.dcloud.f.a.b()     // Catch:{ Exception -> 0x00de }
            byte[] r5 = io.dcloud.common.util.AESUtil.encrypt((java.lang.String) r5, (java.lang.String) r8, (java.lang.String) r7)     // Catch:{ Exception -> 0x00de }
            java.lang.String r5 = android.util.Base64.encodeToString(r5, r6)     // Catch:{ Exception -> 0x00de }
            java.lang.String r5 = java.net.URLEncoder.encode(r5)     // Catch:{ Exception -> 0x00de }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00de }
            r7.<init>()     // Catch:{ Exception -> 0x00de }
            r7.append(r5)     // Catch:{ Exception -> 0x00de }
            r7.append(r13)     // Catch:{ Exception -> 0x00de }
            java.lang.String r10 = r7.toString()     // Catch:{ Exception -> 0x00de }
            if (r11 == 0) goto L_0x00b4
            java.lang.String r11 = sImei
            r1.append(r11)
            r1.append(r2)
        L_0x00b4:
            return r10
        L_0x00b5:
            java.lang.String[] r7 = getMultiIMEI(r10)     // Catch:{ Exception -> 0x00de }
            if (r7 == 0) goto L_0x00d7
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00de }
            r8.<init>()     // Catch:{ Exception -> 0x00de }
            r9 = 0
            r9 = r7[r9]     // Catch:{ Exception -> 0x00de }
            r8.append(r9)     // Catch:{ Exception -> 0x00de }
            java.lang.String r9 = ","
            r8.append(r9)     // Catch:{ Exception -> 0x00de }
            r5 = r7[r5]     // Catch:{ Exception -> 0x00de }
            r8.append(r5)     // Catch:{ Exception -> 0x00de }
            java.lang.String r5 = r8.toString()     // Catch:{ Exception -> 0x00de }
            sImei = r5     // Catch:{ Exception -> 0x00de }
            goto L_0x00d9
        L_0x00d7:
            sImei = r3     // Catch:{ Exception -> 0x00de }
        L_0x00d9:
            if (r11 == 0) goto L_0x0112
            goto L_0x00fb
        L_0x00dc:
            r10 = move-exception
            goto L_0x0104
        L_0x00de:
            r5 = move-exception
            java.lang.String r7 = TAG     // Catch:{ all -> 0x00dc }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x00dc }
            r8.<init>()     // Catch:{ all -> 0x00dc }
            java.lang.String r9 = "getIMEI exception=="
            r8.append(r9)     // Catch:{ all -> 0x00dc }
            java.lang.String r5 = r5.getMessage()     // Catch:{ all -> 0x00dc }
            r8.append(r5)     // Catch:{ all -> 0x00dc }
            java.lang.String r5 = r8.toString()     // Catch:{ all -> 0x00dc }
            io.dcloud.common.adapter.util.Logger.e(r7, r5)     // Catch:{ all -> 0x00dc }
            if (r11 == 0) goto L_0x0112
        L_0x00fb:
            java.lang.String r5 = sImei
            r1.append(r5)
            r1.append(r2)
            goto L_0x0112
        L_0x0104:
            if (r11 == 0) goto L_0x010e
            java.lang.String r11 = sImei
            r1.append(r11)
            r1.append(r2)
        L_0x010e:
            throw r10
        L_0x010f:
            r1.append(r2)
        L_0x0112:
            if (r11 == 0) goto L_0x0117
            r1.append(r2)
        L_0x0117:
            java.lang.String r5 = sImei
            boolean r5 = isUnValid(r5)
            if (r5 != 0) goto L_0x0125
            if (r11 == 0) goto L_0x0122
            goto L_0x0125
        L_0x0122:
            java.lang.String r10 = sImei
            return r10
        L_0x0125:
            r7 = 0
            if (r4 != 0) goto L_0x0166
            java.lang.String r8 = getAId(r10)     // Catch:{ Exception -> 0x013e, all -> 0x013c }
            if (r5 == 0) goto L_0x0133
            sImei = r8     // Catch:{ Exception -> 0x0131 }
            goto L_0x0133
        L_0x0131:
            r5 = move-exception
            goto L_0x0140
        L_0x0133:
            if (r11 == 0) goto L_0x0169
            boolean r5 = android.text.TextUtils.isEmpty(r8)
            if (r5 == 0) goto L_0x014c
            goto L_0x014b
        L_0x013c:
            r10 = move-exception
            goto L_0x0155
        L_0x013e:
            r5 = move-exception
            r8 = r7
        L_0x0140:
            r5.printStackTrace()     // Catch:{ all -> 0x0153 }
            if (r11 == 0) goto L_0x0169
            boolean r5 = android.text.TextUtils.isEmpty(r8)
            if (r5 == 0) goto L_0x014c
        L_0x014b:
            r8 = r3
        L_0x014c:
            r1.append(r8)
            r1.append(r2)
            goto L_0x0169
        L_0x0153:
            r10 = move-exception
            r7 = r8
        L_0x0155:
            if (r11 == 0) goto L_0x0165
            boolean r11 = android.text.TextUtils.isEmpty(r7)
            if (r11 == 0) goto L_0x015e
            goto L_0x015f
        L_0x015e:
            r3 = r7
        L_0x015f:
            r1.append(r3)
            r1.append(r2)
        L_0x0165:
            throw r10
        L_0x0166:
            r1.append(r2)
        L_0x0169:
            java.lang.String r5 = sImei
            boolean r5 = isUnValid(r5)
            if (r5 != 0) goto L_0x0177
            if (r11 == 0) goto L_0x0174
            goto L_0x0177
        L_0x0174:
            java.lang.String r10 = sImei
            return r10
        L_0x0177:
            java.lang.String r7 = getRandomId(r10)     // Catch:{ Exception -> 0x0197 }
            if (r5 == 0) goto L_0x0181
            if (r4 != 0) goto L_0x0181
            sImei = r7     // Catch:{ Exception -> 0x0197 }
        L_0x0181:
            if (r11 == 0) goto L_0x018a
            boolean r10 = android.text.TextUtils.isEmpty(r7)
            if (r10 == 0) goto L_0x01a4
            goto L_0x01a8
        L_0x018a:
            java.lang.String r10 = r1.toString()
            boolean r10 = android.text.TextUtils.isEmpty(r10)
            if (r10 == 0) goto L_0x01bc
            goto L_0x01b9
        L_0x0195:
            r10 = move-exception
            goto L_0x01fa
        L_0x0197:
            r10 = move-exception
            r10.printStackTrace()     // Catch:{ all -> 0x0195 }
            if (r11 == 0) goto L_0x01af
            boolean r10 = android.text.TextUtils.isEmpty(r7)
            if (r10 == 0) goto L_0x01a4
            goto L_0x01a8
        L_0x01a4:
            java.lang.String r3 = r7.replace(r0, r3)
        L_0x01a8:
            r1.append(r3)
            r1.append(r2)
            goto L_0x01bc
        L_0x01af:
            java.lang.String r10 = r1.toString()
            boolean r10 = android.text.TextUtils.isEmpty(r10)
            if (r10 == 0) goto L_0x01bc
        L_0x01b9:
            r1.append(r7)
        L_0x01bc:
            java.lang.String r10 = io.dcloud.f.a.c()
            java.lang.String r11 = io.dcloud.f.a.b()
            java.lang.String r0 = r1.toString()
            byte[] r10 = io.dcloud.common.util.AESUtil.encrypt((java.lang.String) r10, (java.lang.String) r11, (java.lang.String) r0)
            java.lang.String r10 = android.util.Base64.encodeToString(r10, r6)
            java.lang.String r10 = java.net.URLEncoder.encode(r10)
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            r11.append(r10)
            r11.append(r13)
            java.lang.String r10 = r11.toString()
            if (r4 != 0) goto L_0x01f3
            sImeiAndBakInfo = r10
            java.lang.String r10 = r1.toString()
            sOriginalImeiAndBakInfo = r10
            if (r12 == 0) goto L_0x01f0
            return r10
        L_0x01f0:
            java.lang.String r10 = sImeiAndBakInfo
            return r10
        L_0x01f3:
            if (r12 == 0) goto L_0x01f9
            java.lang.String r10 = r1.toString()
        L_0x01f9:
            return r10
        L_0x01fa:
            if (r11 == 0) goto L_0x020e
            boolean r11 = android.text.TextUtils.isEmpty(r7)
            if (r11 == 0) goto L_0x0203
            goto L_0x0207
        L_0x0203:
            java.lang.String r3 = r7.replace(r0, r3)
        L_0x0207:
            r1.append(r3)
            r1.append(r2)
            goto L_0x021b
        L_0x020e:
            java.lang.String r11 = r1.toString()
            boolean r11 = android.text.TextUtils.isEmpty(r11)
            if (r11 == 0) goto L_0x021b
            r1.append(r7)
        L_0x021b:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.TelephonyUtil.getSBBS(android.content.Context, boolean, boolean, boolean):java.lang.String");
    }

    public static String getIMEI(Context context, boolean z, boolean z2) {
        return getSBBS(context, z, z2);
    }
}
