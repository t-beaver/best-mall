package io.dcloud.common.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.performance.WXInstanceApm;
import io.dcloud.PdrR;
import io.dcloud.base.R;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.ui.Info.AndroidPrivacyResponse;
import io.dcloud.common.ui.a;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.TitleNViewUtil;
import io.dcloud.common.util.language.LanguageUtil;
import io.dcloud.feature.internal.sdk.SDK;
import java.io.File;
import java.io.InputStream;

public class b {
    private static b a;
    /* access modifiers changed from: private */
    public AndroidPrivacyResponse b = new AndroidPrivacyResponse();
    private boolean c = false;
    public boolean d = false;
    private a e;

    /* renamed from: io.dcloud.common.ui.b$b  reason: collision with other inner class name */
    public interface C0023b {
        void a();

        void a(AndroidPrivacyResponse androidPrivacyResponse);

        void a(String str);

        void b(AndroidPrivacyResponse androidPrivacyResponse);
    }

    private b() {
    }

    public static boolean c() {
        Class<?> cls;
        try {
            cls = Class.forName("io.dcloud.common.util.net.http.LocalServer2");
        } catch (ClassNotFoundException e2) {
            e2.printStackTrace();
            cls = null;
        }
        return cls != null;
    }

    private boolean e(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("privacy_config_uni_sp_file", 0);
        String string = sharedPreferences.getString("privacy_config_version_uni_current_key", "emptyPrivacyVersion");
        int i = sharedPreferences.getInt("privacy_config_choose_disagree_uni_current_key", 0);
        if (string.equals(this.b.version) || "emptyPrivacyVersion".equals(string)) {
            if (this.b.prompt.equals("template")) {
                AndroidPrivacyResponse.disagreeModeDTO disagreemodedto = this.b.disagreeMode;
                if (((!disagreemodedto.support && !disagreemodedto.visitorEntry) || disagreemodedto.showAlways || i != 1) && !"1".equals(SP.getBundleData(context, "pdr", "scok"))) {
                    SP.setBundleData(context, "pdr", "scok", WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
                    return true;
                }
            }
            return false;
        }
        sharedPreferences.edit().putInt("privacy_config_choose_disagree_uni_current_key", 0).apply();
        SP.setBundleData(context, "pdr", "scok", "");
        if (!this.b.prompt.equals("template")) {
            return false;
        }
        SP.setBundleData(context, "pdr", "scok", WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
        return true;
    }

    public boolean b() {
        return this.d;
    }

    public void d(Context context) {
        context.getSharedPreferences("privacy_config_uni_sp_file", 0).edit().putString("privacy_config_version_uni_current_key", "").commit();
        SP.setBundleData(context, "pdr", "scok", WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
    }

    public static b a() {
        if (a == null) {
            a = new b();
        }
        return a;
    }

    public void b(Context context, JSONObject jSONObject) {
        if (jSONObject != null) {
            this.c = false;
            a(context, jSONObject);
            return;
        }
        this.c = false;
        a(context, (JSONObject) null);
    }

    public boolean c(Context context) {
        this.c = false;
        a(context);
        String str = this.b.prompt;
        if (SDK.isUniMPSDK() || !"template".equals(str) || context.getSharedPreferences("privacy_config_uni_sp_file", 0).getString("privacy_config_version_uni_current_key", "").equals(this.b.version)) {
            return false;
        }
        return true;
    }

    class a implements a.d {
        final /* synthetic */ Activity a;
        final /* synthetic */ C0023b b;

        a(Activity activity, C0023b bVar) {
            this.a = activity;
            this.b = bVar;
        }

        public void a(String str) {
            this.a.getSharedPreferences("privacy_config_uni_sp_file", 0).edit().putString("privacy_config_version_uni_current_key", str).apply();
            SP.setBundleData(this.a, "pdr", "scok", "1");
            this.b.a();
        }

        public void onCancel() {
            SP.setBundleData(this.a, "pdr", "scok", WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
            this.b.b(b.this.b);
            this.a.getSharedPreferences("privacy_config_uni_sp_file", 0).edit().putInt("privacy_config_choose_disagree_uni_current_key", 1).apply();
        }

        public void a() {
            SP.setBundleData(this.a, "pdr", "scok", WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
            this.b.a(b.this.b);
        }
    }

    public void a(Context context) {
        a(context, (JSONObject) null);
    }

    public void a(Context context, JSONObject jSONObject) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        if (!this.c) {
            String str = "";
            this.d = AppRuntime.isAppResourcesInAssetsPath(context, str);
            String str2 = BaseInfo.sCacheFsAppsPath;
            String str3 = BaseInfo.sDefaultBootApp + "/www/androidPrivacy.json";
            Object obj = null;
            if (jSONObject == null) {
                if (!this.d) {
                    File file = new File(str2 + str3);
                    if (file.exists()) {
                        String a2 = a(file.getPath());
                        try {
                            Object parseObject = JSONObject.parseObject(a2, AndroidPrivacyResponse.class);
                            jSONObject = JSONObject.parseObject(a2);
                            obj = parseObject;
                        } catch (Exception unused) {
                            Log.e("uniapp", "privacy json format error");
                            jSONObject = null;
                        }
                        if (obj instanceof AndroidPrivacyResponse) {
                            this.b = (AndroidPrivacyResponse) obj;
                        } else {
                            this.b = new AndroidPrivacyResponse();
                        }
                    } else {
                        if (!new File(str2 + BaseInfo.sDefaultBootApp).exists()) {
                            String a3 = a(context, "apps/" + str3);
                            if (!TextUtils.isEmpty(a3)) {
                                try {
                                    Object parseObject2 = JSONObject.parseObject(a3, AndroidPrivacyResponse.class);
                                    r10 = JSONObject.parseObject(a3);
                                    obj = parseObject2;
                                } catch (Exception unused2) {
                                    r10 = null;
                                }
                                if (obj instanceof AndroidPrivacyResponse) {
                                    this.b = (AndroidPrivacyResponse) obj;
                                } else {
                                    this.b = new AndroidPrivacyResponse();
                                }
                            }
                        }
                    }
                } else {
                    String a4 = a(context, "apps/" + str3);
                    if (!TextUtils.isEmpty(a4)) {
                        try {
                            Object parseObject3 = JSONObject.parseObject(a4, AndroidPrivacyResponse.class);
                            r10 = JSONObject.parseObject(a4);
                            obj = parseObject3;
                        } catch (Exception unused3) {
                            Log.e("uniapp", "privacy json format error");
                            r10 = null;
                        }
                        if (obj instanceof AndroidPrivacyResponse) {
                            this.b = (AndroidPrivacyResponse) obj;
                        } else {
                            this.b = new AndroidPrivacyResponse();
                        }
                    }
                }
                jSONObject = null;
            }
            if (jSONObject == null) {
                jSONObject = new JSONObject();
            }
            if (this.b == null) {
                this.b = new AndroidPrivacyResponse();
            }
            AndroidPrivacyResponse androidPrivacyResponse = this.b;
            if (androidPrivacyResponse.second == null) {
                androidPrivacyResponse.second = new AndroidPrivacyResponse.SecondDTO();
            }
            AndroidPrivacyResponse androidPrivacyResponse2 = this.b;
            if (androidPrivacyResponse2.styles == null) {
                androidPrivacyResponse2.styles = new AndroidPrivacyResponse.StylesDTO();
            }
            AndroidPrivacyResponse.StylesDTO stylesDTO = this.b.styles;
            if (stylesDTO.title == null) {
                stylesDTO.title = new AndroidPrivacyResponse.StylesDTO.TitleDTO();
            }
            AndroidPrivacyResponse.StylesDTO stylesDTO2 = this.b.styles;
            if (stylesDTO2.buttonRefuse == null) {
                stylesDTO2.buttonRefuse = new AndroidPrivacyResponse.StylesDTO.ButtonRefuseDTO();
            }
            AndroidPrivacyResponse.StylesDTO stylesDTO3 = this.b.styles;
            if (stylesDTO3.buttonAccept == null) {
                stylesDTO3.buttonAccept = new AndroidPrivacyResponse.StylesDTO.ButtonAcceptDTO();
            }
            JSONObject jSONObject2 = jSONObject.getJSONObject("titleLocales");
            if (jSONObject2 != null) {
                AndroidPrivacyResponse androidPrivacyResponse3 = this.b;
                androidPrivacyResponse3.title = LanguageUtil.getString(jSONObject2, androidPrivacyResponse3.title);
            }
            JSONObject jSONObject3 = jSONObject.getJSONObject("messageLocales");
            if (jSONObject3 != null) {
                AndroidPrivacyResponse androidPrivacyResponse4 = this.b;
                androidPrivacyResponse4.message = LanguageUtil.getString(jSONObject3, androidPrivacyResponse4.message);
            }
            JSONObject jSONObject4 = jSONObject.getJSONObject("buttonAcceptLocales");
            if (jSONObject4 != null) {
                AndroidPrivacyResponse androidPrivacyResponse5 = this.b;
                androidPrivacyResponse5.buttonAccept = LanguageUtil.getString(jSONObject4, androidPrivacyResponse5.buttonAccept);
            }
            JSONObject jSONObject5 = jSONObject.getJSONObject("buttonRefuseLocales");
            if (jSONObject5 != null) {
                AndroidPrivacyResponse androidPrivacyResponse6 = this.b;
                androidPrivacyResponse6.buttonRefuse = LanguageUtil.getString(jSONObject5, androidPrivacyResponse6.buttonRefuse);
            }
            JSONObject jSONObject6 = jSONObject.getJSONObject("second");
            if (jSONObject6 != null) {
                JSONObject jSONObject7 = jSONObject6.getJSONObject("titleLocales");
                if (jSONObject7 != null) {
                    AndroidPrivacyResponse.SecondDTO secondDTO = this.b.second;
                    secondDTO.title = LanguageUtil.getString(jSONObject7, secondDTO.title);
                }
                JSONObject jSONObject8 = jSONObject6.getJSONObject("messageLocales");
                if (jSONObject8 != null) {
                    AndroidPrivacyResponse.SecondDTO secondDTO2 = this.b.second;
                    secondDTO2.message = LanguageUtil.getString(jSONObject8, secondDTO2.message);
                }
                JSONObject jSONObject9 = jSONObject6.getJSONObject("buttonAcceptLocales");
                if (jSONObject9 != null) {
                    AndroidPrivacyResponse.SecondDTO secondDTO3 = this.b.second;
                    secondDTO3.buttonAccept = LanguageUtil.getString(jSONObject9, secondDTO3.buttonAccept);
                }
                JSONObject jSONObject10 = jSONObject6.getJSONObject("buttonRefuseLocales");
                if (jSONObject10 != null) {
                    AndroidPrivacyResponse.SecondDTO secondDTO4 = this.b.second;
                    secondDTO4.buttonRefuse = LanguageUtil.getString(jSONObject10, secondDTO4.buttonRefuse);
                }
            }
            if (TextUtils.isEmpty(this.b.version)) {
                this.b.version = "emptyPrivacyVersion";
            }
            if (TextUtils.isEmpty(this.b.prompt)) {
                String metaValue = AndroidResources.getMetaValue("DCLOUD_PRIVACY_PROMPT");
                if (metaValue == null) {
                    metaValue = str;
                }
                this.b.prompt = metaValue;
            }
            if (TextUtils.isEmpty(this.b.title) && (i8 = PdrR.getInt(context, "string", "dcloud_privacy_prompt_title")) != 0) {
                String string = context.getString(i8);
                if (string == null) {
                    string = str;
                }
                this.b.title = string;
            }
            if (TextUtils.isEmpty(this.b.message) && (i7 = PdrR.getInt(context, "string", "dcloud_privacy_prompt_message")) != 0) {
                String string2 = context.getString(i7);
                if (string2 == null) {
                    string2 = str;
                }
                this.b.message = string2;
            }
            if (TextUtils.isEmpty(this.b.buttonAccept) && (i6 = PdrR.getInt(context, "string", "dcloud_privacy_prompt_accept_button_text")) != 0) {
                String string3 = context.getString(i6);
                if (string3 == null) {
                    string3 = str;
                }
                this.b.buttonAccept = string3;
            }
            if (TextUtils.isEmpty(this.b.buttonRefuse) && (i5 = PdrR.getInt(context, "string", "dcloud_privacy_prompt_refuse_button_text")) != 0) {
                String string4 = context.getString(i5);
                if (string4 == null) {
                    string4 = str;
                }
                this.b.buttonRefuse = string4;
            }
            if (TextUtils.isEmpty(this.b.second.message) && (i4 = PdrR.getInt(context, "string", "dcloud_second_privacy_prompt_message")) != 0) {
                String string5 = context.getString(i4);
                if (string5 == null) {
                    string5 = str;
                }
                this.b.second.message = string5;
            }
            if (TextUtils.isEmpty(this.b.second.title) && (i3 = PdrR.getInt(context, "string", "dcloud_second_privacy_prompt_title")) != 0) {
                String string6 = context.getString(i3);
                if (string6 == null) {
                    string6 = str;
                }
                this.b.second.title = string6;
            }
            if (TextUtils.isEmpty(this.b.second.buttonAccept) && (i2 = PdrR.getInt(context, "string", "dcloud_second_privacy_prompt_accept_button_text")) != 0) {
                String string7 = context.getString(i2);
                if (string7 == null) {
                    string7 = str;
                }
                this.b.second.buttonAccept = string7;
            }
            if (TextUtils.isEmpty(this.b.second.buttonRefuse) && (i = PdrR.getInt(context, "string", "dcloud_second_privacy_prompt_refuse_button_text")) != 0) {
                String string8 = context.getString(i);
                if (string8 != null) {
                    str = string8;
                }
                this.b.second.buttonRefuse = str;
            }
            if (TextUtils.isEmpty(this.b.styles.backgroundColor)) {
                this.b.styles.backgroundColor = TitleNViewUtil.TRANSPARENT_BUTTON_TEXT_COLOR;
            }
            if (TextUtils.isEmpty(this.b.styles.borderRadius)) {
                this.b.styles.borderRadius = "10px";
            }
            if (TextUtils.isEmpty(this.b.styles.title.color)) {
                this.b.styles.title.color = "#000000";
            }
            if (TextUtils.isEmpty(this.b.styles.buttonAccept.color)) {
                this.b.styles.buttonAccept.color = "#000000";
            }
            if (TextUtils.isEmpty(this.b.styles.buttonRefuse.color)) {
                this.b.styles.buttonRefuse.color = "#000000";
            }
            if (TextUtils.isEmpty(this.b.buttonAccept)) {
                this.b.styles.buttonAccept.color = "#000000";
            }
            if (TextUtils.isEmpty(this.b.styles.buttonRefuse.color)) {
                this.b.styles.buttonRefuse.color = "#000000";
            }
            this.c = true;
        }
    }

    public boolean b(Context context) {
        if (!this.c) {
            a(context);
        }
        String str = this.b.prompt;
        String bundleData = SP.getBundleData(context, "pdr", "scok");
        return (PdrUtil.isEmpty(bundleData) || !bundleData.equals("1")) && !PdrUtil.isEmpty(str) && str.equalsIgnoreCase("template");
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x003d A[SYNTHETIC, Splitter:B:22:0x003d] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x004c A[SYNTHETIC, Splitter:B:29:0x004c] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String a(java.lang.String r4) {
        /*
            java.io.File r0 = new java.io.File
            r0.<init>(r4)
            java.lang.StringBuffer r4 = new java.lang.StringBuffer
            r4.<init>()
            r1 = 0
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0037 }
            java.io.FileReader r3 = new java.io.FileReader     // Catch:{ IOException -> 0x0037 }
            r3.<init>(r0)     // Catch:{ IOException -> 0x0037 }
            r2.<init>(r3)     // Catch:{ IOException -> 0x0037 }
        L_0x0015:
            java.lang.String r0 = r2.readLine()     // Catch:{ IOException -> 0x0032, all -> 0x002f }
            if (r0 == 0) goto L_0x001f
            r4.append(r0)     // Catch:{ IOException -> 0x0032, all -> 0x002f }
            goto L_0x0015
        L_0x001f:
            r2.close()     // Catch:{ IOException -> 0x0032, all -> 0x002f }
            java.lang.String r4 = r4.toString()     // Catch:{ IOException -> 0x0032, all -> 0x002f }
            r2.close()     // Catch:{ IOException -> 0x002a }
            goto L_0x002e
        L_0x002a:
            r0 = move-exception
            r0.printStackTrace()
        L_0x002e:
            return r4
        L_0x002f:
            r4 = move-exception
            r1 = r2
            goto L_0x004a
        L_0x0032:
            r0 = move-exception
            r1 = r2
            goto L_0x0038
        L_0x0035:
            r4 = move-exception
            goto L_0x004a
        L_0x0037:
            r0 = move-exception
        L_0x0038:
            r0.printStackTrace()     // Catch:{ all -> 0x0035 }
            if (r1 == 0) goto L_0x0045
            r1.close()     // Catch:{ IOException -> 0x0041 }
            goto L_0x0045
        L_0x0041:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0045:
            java.lang.String r4 = r4.toString()
            return r4
        L_0x004a:
            if (r1 == 0) goto L_0x0054
            r1.close()     // Catch:{ IOException -> 0x0050 }
            goto L_0x0054
        L_0x0050:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0054:
            goto L_0x0056
        L_0x0055:
            throw r4
        L_0x0056:
            goto L_0x0055
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.ui.b.a(java.lang.String):java.lang.String");
    }

    private static String a(Context context, String str) {
        try {
            InputStream open = context.getResources().getAssets().open(str);
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            return new String(bArr);
        } catch (Exception unused) {
            return "";
        }
    }

    public void a(Activity activity, C0023b bVar, boolean z, boolean z2) {
        if (!e(activity)) {
            bVar.a(this.b.prompt);
            return;
        }
        a aVar = new a(activity, bVar);
        a aVar2 = this.e;
        if (aVar2 != null) {
            aVar2.a();
            this.e = null;
        }
        if (z) {
            a aVar3 = new a(activity);
            this.e = aVar3;
            aVar3.a(bVar);
            this.e.a(true);
            this.e.a(this.b, true, (a.d) aVar);
            this.e.a(R.layout.dcloud_custom_privacy_second_dialog_layout);
            this.e.e();
            return;
        }
        a aVar4 = new a(activity);
        this.e = aVar4;
        aVar4.a(bVar);
        this.e.a(false);
        this.e.a(R.layout.dcloud_custom_privacy_dialog_layout);
        this.e.a(this.b, false, (a.d) aVar);
        this.e.e();
    }

    public void a(Activity activity) {
        a aVar = this.e;
        if (aVar != null && aVar.d()) {
            C0023b b2 = this.e.b();
            boolean c2 = this.e.c();
            this.e.a();
            this.e = null;
            a(activity, b2, c2, true);
        }
    }

    public void a(Context context, boolean z) {
        SP.setBundleData(context, "pdr", "scok", z ? "1" : WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
    }
}
