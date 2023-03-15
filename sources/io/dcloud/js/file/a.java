package io.dcloud.js.file;

import io.dcloud.common.adapter.io.DHFile;
import java.io.File;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class a {

    /* renamed from: io.dcloud.js.file.a$a  reason: collision with other inner class name */
    static class C0071a {
        long a;
        long b;
        int c = 0;
        int d = 0;

        C0071a() {
        }

        public JSONObject a() {
            try {
                return new JSONObject(String.format(Locale.ENGLISH, "{lastModifiedDate : %d,size : %d,directoryCount : %d,fileCount : %d}", new Object[]{Long.valueOf(this.a), Long.valueOf(this.b), Integer.valueOf(this.c), Integer.valueOf(this.d)}));
            } catch (JSONException e) {
                e.printStackTrace();
                return new JSONObject();
            }
        }
    }

    protected static JSONObject a(String str, long j, long j2, String str2, String str3) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("lastModifiedDate", j);
            jSONObject.put("type", str);
            jSONObject.put(AbsoluteConst.JSON_KEY_SIZE, j2);
            jSONObject.put("name", str2);
            jSONObject.put("fullPath", str3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    protected static JSONArray b(String str, String str2) {
        JSONArray jSONArray = new JSONArray();
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        String str3 = File.separator;
        sb.append(str.endsWith(str3) ? "" : str3);
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(str2);
        if (str2.endsWith(str3)) {
            str3 = "";
        }
        sb3.append(str3);
        String sb4 = sb3.toString();
        String[] list = new File(sb2).list();
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                String str4 = sb2 + list[i];
                File file = new File(str4);
                JSONObject jSONObject = new JSONObject();
                boolean isDirectory = file.isDirectory();
                String str5 = list[i];
                String str6 = sb4 + str5;
                try {
                    jSONObject.put("isDirectory", isDirectory);
                    jSONObject.put("isFile", !isDirectory);
                    jSONObject.put("name", str5);
                    jSONObject.put("remoteURL", str6);
                    jSONObject.put("fullPath", str4);
                    jSONArray.put(jSONObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return jSONArray;
    }

    protected static JSONObject a(String str, int i, String str2, String str3, String str4) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("name", str);
            jSONObject.put("type", i);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("name", str2);
            jSONObject2.put("fullPath", str3);
            jSONObject2.put("remoteURL", str4);
            jSONObject.put("root", jSONObject2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    protected static JSONObject a(String str, String str2, String str3, boolean z) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("isDirectory", z);
            jSONObject.put("isFile", !z);
            jSONObject.put("name", str);
            jSONObject.put("remoteURL", str3);
            jSONObject.put("fullPath", str2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    protected static JSONObject a(String str, String str2, boolean z, String str3, String str4, int i, JSONObject jSONObject) {
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject2.put("isDirectory", z);
            jSONObject2.put("isFile", !z);
            jSONObject2.put("name", str);
            jSONObject2.put("remoteURL", str3);
            jSONObject2.put("fullPath", str2);
            jSONObject2.put("fsName", str4);
            jSONObject2.put("type", i);
            jSONObject2.put("fsRoot", jSONObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject2;
    }

    public static JSONObject a(String str, String str2) {
        File file = new File(str);
        return a(str2, file.lastModified(), DHFile.getFileSize(file), file.getName(), str);
    }

    public static JSONObject a(String str, boolean z) {
        File file = new File(str);
        if (!file.exists()) {
            return null;
        }
        C0071a aVar = new C0071a();
        aVar.a = file.lastModified();
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (File a : listFiles) {
                    a(a, aVar, z);
                }
            }
        } else {
            aVar.b = file.length();
        }
        return aVar.a();
    }

    public static void a(File file, C0071a aVar, boolean z) {
        File[] listFiles;
        if (file.isDirectory()) {
            if (z && (listFiles = file.listFiles()) != null) {
                for (File a : listFiles) {
                    a(a, aVar, z);
                }
            }
            aVar.c++;
            return;
        }
        aVar.b += file.length();
        aVar.d++;
    }
}
