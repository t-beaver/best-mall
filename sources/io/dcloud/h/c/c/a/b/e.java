package io.dcloud.h.c.c.a.b;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.facebook.common.callercontext.ContextChain;
import com.taobao.weex.common.WXConfig;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.performance.WXInstanceApm;
import io.dcloud.common.util.JSUtil;
import io.dcloud.h.a.d.b.h;
import io.dcloud.h.c.a;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import org.json.JSONObject;

public final class e {
    static HashMap<String, Object> a(Context context) {
        String str;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(ContextChain.TAG_PRODUCT, "a");
        hashMap.put("appid", a.d().b().getAppId());
        try {
            hashMap.put("pname", context.getApplicationInfo().loadLabel(context.getPackageManager()));
        } catch (Exception unused) {
        }
        hashMap.put("pn", context.getPackageName());
        try {
            str = context.getPackageManager().getPackageInfo(context.getPackageName(), 1).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            str = null;
        }
        hashMap.put("pv", str);
        hashMap.put("imei", io.dcloud.h.c.c.a.e.e.a(context, true, true));
        hashMap.put("md", Build.MODEL);
        hashMap.put("vd", Build.MANUFACTURER);
        hashMap.put(WXConfig.os, Integer.valueOf(Build.VERSION.SDK_INT));
        hashMap.put("vb", io.dcloud.h.c.c.a.e.a.a());
        hashMap.put("net", h.d(context));
        hashMap.put("mc", io.dcloud.h.c.c.a.e.a.a(context));
        hashMap.put("paid", a.d().b().getAdId());
        hashMap.put("dw", Integer.valueOf(context.getResources().getDisplayMetrics().widthPixels));
        hashMap.put("dh", Integer.valueOf(context.getResources().getDisplayMetrics().heightPixels));
        hashMap.put("psap", "dcloud,wanka,youdao,common");
        hashMap.put("psas", "");
        hashMap.put("ps", 0);
        hashMap.put("psd", 0);
        hashMap.put("data", b(context));
        hashMap.put("pap", "1");
        hashMap.put("papi", "1");
        hashMap.put("psp", TextUtils.join(",", io.dcloud.sdk.core.b.a.b().c()));
        hashMap.put("psaf", WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
        hashMap.put("psdk", 0);
        hashMap.put("mpap", "1");
        hashMap.put("dpsp", "1");
        hashMap.put("bm", a());
        hashMap.put("um", b());
        do {
        } while (hashMap.values().remove((Object) null));
        do {
        } while (hashMap.values().remove("null"));
        return hashMap;
    }

    private static String b() {
        try {
            Process exec = Runtime.getRuntime().exec("stat -c \"%x\" /data/data");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            char[] cArr = new char[256];
            while (true) {
                int read = bufferedReader.read(cArr);
                if (read <= 0) {
                    break;
                }
                stringBuffer.append(cArr, 0, read);
            }
            bufferedReader.close();
            exec.waitFor();
            String[] split = stringBuffer.toString().replace(JSUtil.QUOTE, "").split("\\.");
            long time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(split[0]).getTime();
            String str = split[1];
            if (str.contains(Operators.PLUS)) {
                str = str.substring(0, str.indexOf(Operators.PLUS));
            }
            long parseLong = Long.parseLong(str.trim());
            return (time / 1000) + Operators.DOT_STR + parseLong;
        } catch (Exception unused) {
            return Operators.DOT_STR;
        }
    }

    private static String b(Context context) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("net", h.c(context));
            jSONObject.put("device", h.b(context));
            jSONObject.put("gps", io.dcloud.h.a.d.b.e.a(context));
        } catch (Exception unused) {
        }
        return jSONObject.toString();
    }

    private static String a() {
        try {
            File file = new File("/proc/sys/kernel/random/boot_id");
            if (file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] bArr = new byte[37];
                fileInputStream.read(bArr);
                String str = new String(bArr);
                try {
                    fileInputStream.close();
                    return str;
                } catch (Exception unused) {
                    return str;
                }
            }
        } catch (Exception unused2) {
        }
        return "";
    }
}
