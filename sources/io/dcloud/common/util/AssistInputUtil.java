package io.dcloud.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.taobao.weex.common.Constants;
import io.dcloud.common.adapter.ui.RecordView;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class AssistInputUtil {
    private static final String SP_KEY_CURRENT_ADDRESS = "assisiSettingCurrentAddress";
    private static final String SP_KEY_DUTY_PARAGRAPH = "assisiSettingDutyParagraph";
    private static final String SP_KEY_EMAIL_A = "assisiSettingEmailA";
    private static final String SP_KEY_EMAIL_B = "assisiSettingEmailB";
    private static final String SP_KEY_HOME_ADDRESS = "assisiSettingHomeAddress";
    private static final String SP_KEY_ID = "assisiSettingId";
    private static final String SP_KEY_NAME = "assisiSettingName";
    private static final String SP_KEY_NAME_B = "assisiSettingNameB";
    private static final String SP_KEY_PHONE_A = "assisiSettingPhoneA";
    private static final String SP_KEY_PHONE_B = "assisiSettingPhoneB";
    private static final String SP_KEY_WORK_ADDRESS = "assisiSettingWorkAddress";
    private static final String SP_KEY_WORK_NAME = "assisiSettingWorkName";
    private static final String SP_NAME = "assisiSetting";
    private static final int XORNUMBER = 6;

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x006d, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void changeSaveDataToEncrypt(android.content.Context r7) {
        /*
            java.lang.Class<io.dcloud.common.util.AssistInputUtil> r0 = io.dcloud.common.util.AssistInputUtil.class
            monitor-enter(r0)
            if (r7 == 0) goto L_0x006c
            java.lang.String r1 = "assisiSetting"
            r2 = 0
            android.content.SharedPreferences r7 = r7.getSharedPreferences(r1, r2)     // Catch:{ all -> 0x0069 }
            java.util.Map r1 = r7.getAll()     // Catch:{ all -> 0x0069 }
            boolean r1 = r1.isEmpty()     // Catch:{ all -> 0x0069 }
            r2 = 1
            if (r1 == 0) goto L_0x0026
            android.content.SharedPreferences$Editor r7 = r7.edit()     // Catch:{ all -> 0x0069 }
            java.lang.String r1 = "isEncrypt"
            android.content.SharedPreferences$Editor r7 = r7.putInt(r1, r2)     // Catch:{ all -> 0x0069 }
            r7.commit()     // Catch:{ all -> 0x0069 }
            monitor-exit(r0)
            return
        L_0x0026:
            java.lang.String r1 = "isEncrypt"
            boolean r1 = r7.contains(r1)     // Catch:{ all -> 0x0069 }
            if (r1 == 0) goto L_0x0030
            monitor-exit(r0)
            return
        L_0x0030:
            android.content.SharedPreferences$Editor r1 = r7.edit()     // Catch:{ all -> 0x0069 }
            java.util.Map r3 = r7.getAll()     // Catch:{ all -> 0x0069 }
            java.util.Set r3 = r3.keySet()     // Catch:{ all -> 0x0069 }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ all -> 0x0069 }
        L_0x0040:
            boolean r4 = r3.hasNext()     // Catch:{ all -> 0x0069 }
            if (r4 == 0) goto L_0x0060
            java.lang.Object r4 = r3.next()     // Catch:{ all -> 0x0069 }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ all -> 0x0069 }
            java.lang.String r5 = ""
            java.lang.String r5 = r7.getString(r4, r5)     // Catch:{ all -> 0x0069 }
            boolean r6 = android.text.TextUtils.isEmpty(r5)     // Catch:{ all -> 0x0069 }
            if (r6 != 0) goto L_0x0040
            java.lang.String r5 = encrypt(r5)     // Catch:{ all -> 0x0069 }
            r1.putString(r4, r5)     // Catch:{ all -> 0x0069 }
            goto L_0x0040
        L_0x0060:
            java.lang.String r7 = "isEncrypt"
            r1.putInt(r7, r2)     // Catch:{ all -> 0x0069 }
            r1.commit()     // Catch:{ all -> 0x0069 }
            goto L_0x006c
        L_0x0069:
            r7 = move-exception
            monitor-exit(r0)
            throw r7
        L_0x006c:
            monitor-exit(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.AssistInputUtil.changeSaveDataToEncrypt(android.content.Context):void");
    }

    public static void clearData(Context context) {
        if (context != null) {
            SharedPreferences.Editor edit = context.getSharedPreferences(SP_NAME, 0).edit();
            edit.clear();
            edit.commit();
        }
    }

    private static String decrypt(String str) {
        if (str == null || TextUtils.isEmpty(str)) {
            return "";
        }
        String decodeString = Base64.decodeString(str, true, 6);
        if (PdrUtil.isEmpty(decodeString)) {
            return "";
        }
        try {
            return URLDecoder.decode(decodeString, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return decodeString;
        }
    }

    private static String encrypt(String str) {
        if (str == null) {
            return null;
        }
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            str = URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.encodeString(str, true, 6);
    }

    private static String[] getCoreRecordViewSaveData(String str) {
        String[] strArr = null;
        String recordDatas = RecordView.getRecordDatas((String) null, str);
        if (TextUtils.isEmpty(recordDatas)) {
            return null;
        }
        if (recordDatas.contains("&")) {
            try {
                String[] split = recordDatas.split("&");
                if (split.length <= 0) {
                    return split;
                }
                if (1 <= split.length) {
                    split[0] = URLDecoder.decode(split[0], "utf-8");
                }
                if (2 > split.length) {
                    return split;
                }
                split[1] = URLDecoder.decode(split[1], "utf-8");
                return split;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            try {
                strArr = new String[]{URLDecoder.decode(recordDatas, "utf-8")};
                return strArr;
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
                return strArr;
            }
        }
    }

    public static String getCurrentAddress(Context context) {
        return context != null ? decrypt(context.getSharedPreferences(SP_NAME, 0).getString(SP_KEY_CURRENT_ADDRESS, "")) : "";
    }

    public static String getDutyParagraph(Context context) {
        return context != null ? decrypt(context.getSharedPreferences(SP_NAME, 0).getString(SP_KEY_DUTY_PARAGRAPH, "")) : "";
    }

    public static String getEmailA(Context context) {
        return context != null ? decrypt(context.getSharedPreferences(SP_NAME, 0).getString(SP_KEY_EMAIL_A, "")) : "";
    }

    public static String getEmailB(Context context) {
        return context != null ? decrypt(context.getSharedPreferences(SP_NAME, 0).getString(SP_KEY_EMAIL_B, "")) : "";
    }

    public static String getHomeAddress(Context context) {
        return context != null ? decrypt(context.getSharedPreferences(SP_NAME, 0).getString(SP_KEY_HOME_ADDRESS, "")) : "";
    }

    public static String getId(Context context) {
        return context != null ? decrypt(context.getSharedPreferences(SP_NAME, 0).getString(SP_KEY_ID, "")) : "";
    }

    public static String getName(Context context) {
        return context != null ? decrypt(context.getSharedPreferences(SP_NAME, 0).getString(SP_KEY_NAME, "")) : "";
    }

    public static String getNameB(Context context) {
        return context != null ? decrypt(context.getSharedPreferences(SP_NAME, 0).getString(SP_KEY_NAME_B, "")) : "";
    }

    public static String getPhoneA(Context context) {
        return context != null ? decrypt(context.getSharedPreferences(SP_NAME, 0).getString(SP_KEY_PHONE_A, "")) : "";
    }

    public static String getPhoneB(Context context) {
        return context != null ? decrypt(context.getSharedPreferences(SP_NAME, 0).getString(SP_KEY_PHONE_B, "")) : "";
    }

    public static String[] getRecordViewCompany() {
        return getCoreRecordViewSaveData("company");
    }

    public static String[] getRecordViewEmails() {
        return getCoreRecordViewSaveData("email");
    }

    public static String[] getRecordViewId() {
        return getCoreRecordViewSaveData("id");
    }

    public static String[] getRecordViewNames() {
        return getCoreRecordViewSaveData("nick");
    }

    public static String[] getRecordViewPhones() {
        return getCoreRecordViewSaveData(Constants.Value.TEL);
    }

    public static String[] getRecordViewTax() {
        return getCoreRecordViewSaveData("tax");
    }

    public static String getWorkAddress(Context context) {
        return context != null ? decrypt(context.getSharedPreferences(SP_NAME, 0).getString(SP_KEY_WORK_ADDRESS, "")) : "";
    }

    public static String getWorkName(Context context) {
        return context != null ? decrypt(context.getSharedPreferences(SP_NAME, 0).getString(SP_KEY_WORK_NAME, "")) : "";
    }

    public static void saveAll(Context context, String str, String str2, String str3, String str4, String str5, String str6) {
        if (context != null) {
            SharedPreferences.Editor edit = context.getSharedPreferences(SP_NAME, 0).edit();
            if (str != null) {
                edit.putString(SP_KEY_CURRENT_ADDRESS, encrypt(str));
            }
            if (str2 != null) {
                edit.putString(SP_KEY_HOME_ADDRESS, encrypt(str2));
            }
            if (str3 != null) {
                edit.putString(SP_KEY_WORK_ADDRESS, encrypt(str3));
            }
            if (str4 != null) {
                edit.putString(SP_KEY_WORK_NAME, encrypt(str4));
            }
            if (str5 != null) {
                edit.putString(SP_KEY_DUTY_PARAGRAPH, encrypt(str5));
            }
            if (str6 != null) {
                edit.putString(SP_KEY_ID, encrypt(str6));
            }
            edit.putInt("isEncrypt", 1);
            edit.commit();
        }
    }

    public static void saveCompany(Context context, boolean z, String str) {
        String str2;
        if (context != null && z) {
            if (str != null) {
                try {
                    str2 = URLEncoder.encode(str, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                str2 = null;
            }
            if (str2 != null) {
                RecordView.setRcordDatas(context, (String) null, "company", str2);
            }
            if (!TextUtils.isEmpty(str)) {
                SharedPreferences.Editor edit = context.getSharedPreferences(SP_NAME, 0).edit();
                edit.putString(SP_KEY_WORK_NAME, encrypt(str));
                edit.commit();
                RecordView.setAssisBundleData(context, "useAssistSettingCompany", "1");
            } else if (TextUtils.isEmpty(str)) {
                RecordView.setAssisBundleData(context, "useAssistSettingCompany", "");
            }
        }
    }

    public static void saveCurrentAddress(Context context, String str) {
        if (context != null && !TextUtils.isEmpty(str)) {
            context.getSharedPreferences(SP_NAME, 0).edit().putString(SP_KEY_CURRENT_ADDRESS, encrypt(str)).commit();
        }
    }

    public static void saveEmail(Context context, boolean z, String str, String str2) {
        String str3;
        if (context != null && z) {
            if (str != null) {
                try {
                    str3 = URLEncoder.encode(str, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                str3 = null;
            }
            if (str2 != null) {
                String encode = URLEncoder.encode(str2, "utf-8");
                if (TextUtils.isEmpty(str3)) {
                    str3 = encode;
                } else {
                    str3 = str3 + "&" + encode;
                }
            }
            if (str3 != null) {
                RecordView.setRcordDatas(context, (String) null, "email", str3);
            }
            if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
                SharedPreferences.Editor edit = context.getSharedPreferences(SP_NAME, 0).edit();
                edit.putString(SP_KEY_EMAIL_A, encrypt(str));
                edit.putString(SP_KEY_EMAIL_B, encrypt(str2));
                edit.commit();
                RecordView.setAssisBundleData(context, "useAssistSettingEmail", "1");
            } else if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
                RecordView.setAssisBundleData(context, "useAssistSettingEmail", "");
            }
        }
    }

    public static void saveId(Context context, boolean z, String str) {
        String str2;
        if (context != null && z) {
            if (str != null) {
                try {
                    str2 = URLEncoder.encode(str, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                str2 = null;
            }
            if (str2 != null) {
                RecordView.setRcordDatas(context, (String) null, "id", str2);
            }
            if (!TextUtils.isEmpty(str)) {
                SharedPreferences.Editor edit = context.getSharedPreferences(SP_NAME, 0).edit();
                edit.putString(SP_KEY_ID, encrypt(str));
                edit.commit();
                RecordView.setAssisBundleData(context, "useAssistSettingId", "1");
            } else if (TextUtils.isEmpty(str)) {
                RecordView.setAssisBundleData(context, "useAssistSettingId", "");
            }
        }
    }

    public static void saveName(Context context, boolean z, String str, String str2) {
        String str3;
        if (context != null && z) {
            if (str != null) {
                try {
                    str3 = URLEncoder.encode(str, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                str3 = null;
            }
            if (str2 != null) {
                String encode = URLEncoder.encode(str2, "utf-8");
                if (TextUtils.isEmpty(str3)) {
                    str3 = encode;
                } else {
                    str3 = str3 + "&" + encode;
                }
            }
            if (str3 != null) {
                RecordView.setRcordDatas(context, (String) null, "nick", str3);
            }
            if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
                SharedPreferences.Editor edit = context.getSharedPreferences(SP_NAME, 0).edit();
                edit.putString(SP_KEY_NAME, encrypt(str));
                edit.putString(SP_KEY_NAME_B, encrypt(str2));
                edit.commit();
                RecordView.setAssisBundleData(context, "useAssistSettingName", "1");
            } else if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
                RecordView.setAssisBundleData(context, "useAssistSettingName", "");
            }
        }
    }

    public static void savePhone(Context context, boolean z, String str, String str2) {
        String str3;
        if (context != null && z) {
            if (str != null) {
                try {
                    str3 = URLEncoder.encode(str, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                str3 = null;
            }
            if (str2 != null) {
                String encode = URLEncoder.encode(str2, "utf-8");
                if (TextUtils.isEmpty(str3)) {
                    str3 = encode;
                } else {
                    str3 = str3 + "&" + encode;
                }
            }
            if (str3 != null) {
                RecordView.setRcordDatas(context, (String) null, Constants.Value.TEL, str3);
            }
            if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
                SharedPreferences.Editor edit = context.getSharedPreferences(SP_NAME, 0).edit();
                edit.putString(SP_KEY_PHONE_A, encrypt(str));
                edit.putString(SP_KEY_PHONE_B, encrypt(str2));
                edit.commit();
                RecordView.setAssisBundleData(context, "useAssistSettingPhone", "1");
            } else if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
                RecordView.setAssisBundleData(context, "useAssistSettingPhone", "");
            }
        }
    }

    public static void saveTax(Context context, boolean z, String str) {
        String str2;
        if (context != null && z) {
            if (str != null) {
                try {
                    str2 = URLEncoder.encode(str, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                str2 = null;
            }
            if (str2 != null) {
                RecordView.setRcordDatas(context, (String) null, "tax", str2);
            }
            if (!TextUtils.isEmpty(str)) {
                SharedPreferences.Editor edit = context.getSharedPreferences(SP_NAME, 0).edit();
                edit.putString(SP_KEY_DUTY_PARAGRAPH, encrypt(str));
                edit.commit();
                RecordView.setAssisBundleData(context, "useAssistSettingTax", "1");
            } else if (TextUtils.isEmpty(str)) {
                RecordView.setAssisBundleData(context, "useAssistSettingTax", "");
            }
        }
    }

    public static boolean useAssistSettingCompany() {
        return !TextUtils.isEmpty(RecordView.getAssisBundleData("useAssistSettingCompany"));
    }

    public static boolean useAssistSettingEmail() {
        return !TextUtils.isEmpty(RecordView.getAssisBundleData("useAssistSettingEmail"));
    }

    public static boolean useAssistSettingId() {
        return !TextUtils.isEmpty(RecordView.getAssisBundleData("useAssistSettingId"));
    }

    public static boolean useAssistSettingName() {
        return !TextUtils.isEmpty(RecordView.getAssisBundleData("useAssistSettingName"));
    }

    public static boolean useAssistSettingPhone() {
        return !TextUtils.isEmpty(RecordView.getAssisBundleData("useAssistSettingPhone"));
    }

    public static boolean useAssistSettingTax() {
        return !TextUtils.isEmpty(RecordView.getAssisBundleData("useAssistSettingTax"));
    }

    public static void saveAll(Context context, String str, String str2, String str3) {
        if (context != null) {
            SharedPreferences.Editor edit = context.getSharedPreferences(SP_NAME, 0).edit();
            if (str != null) {
                edit.putString(SP_KEY_CURRENT_ADDRESS, encrypt(str));
            }
            if (str2 != null) {
                edit.putString(SP_KEY_HOME_ADDRESS, encrypt(str2));
            }
            if (str3 != null) {
                edit.putString(SP_KEY_WORK_ADDRESS, encrypt(str3));
            }
            edit.putInt("isEncrypt", 1);
            edit.commit();
        }
    }
}
