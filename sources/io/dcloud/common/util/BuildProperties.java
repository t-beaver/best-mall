package io.dcloud.common.util;

import android.os.Environment;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class BuildProperties {
    private static String BUILD_PROPERTIES_FILE_NAME = ".buildPropertiesMD5.data";
    private static String BUILD_PROPERTIES_FILE_PATH = "";
    private static volatile BuildProperties ourInstance;
    private Properties properties;

    private BuildProperties() throws Exception {
        Properties properties2 = new Properties();
        this.properties = properties2;
        properties2.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
    }

    public static BuildProperties getInstance() throws Exception {
        if (ourInstance == null) {
            synchronized (BuildProperties.class) {
                if (ourInstance == null) {
                    ourInstance = new BuildProperties();
                }
            }
        }
        return ourInstance;
    }

    public boolean containsKey(Object obj) {
        return this.properties.containsKey(obj);
    }

    public boolean containsValue(Object obj) {
        return this.properties.containsValue(obj);
    }

    public Set<Map.Entry<Object, Object>> entrySet() {
        return this.properties.entrySet();
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0067 A[Catch:{ Exception -> 0x00a8 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getBuildPropertiesLimit(android.content.Context r7) {
        /*
            r6 = this;
            boolean r0 = io.dcloud.common.adapter.util.DeviceInfo.isSDcardExists()
            java.lang.String r1 = ""
            if (r0 == 0) goto L_0x000d
            java.lang.String r7 = io.dcloud.common.adapter.util.DeviceInfo.sBaseFsCachePath
            BUILD_PROPERTIES_FILE_PATH = r7
            goto L_0x001a
        L_0x000d:
            if (r7 != 0) goto L_0x0010
            return r1
        L_0x0010:
            java.io.File r7 = r7.getFilesDir()
            java.lang.String r7 = r7.getAbsolutePath()
            BUILD_PROPERTIES_FILE_PATH = r7
        L_0x001a:
            java.io.File r7 = new java.io.File     // Catch:{ Exception -> 0x00a8 }
            java.io.File r0 = android.os.Environment.getRootDirectory()     // Catch:{ Exception -> 0x00a8 }
            java.lang.String r2 = "build.prop"
            r7.<init>(r0, r2)     // Catch:{ Exception -> 0x00a8 }
            java.lang.String r7 = io.dcloud.common.util.Md5Utils.md5((java.io.File) r7)     // Catch:{ Exception -> 0x00a8 }
            if (r7 == 0) goto L_0x00ac
            r0 = 0
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00a8 }
            r2.<init>()     // Catch:{ Exception -> 0x00a8 }
            java.lang.String r3 = BUILD_PROPERTIES_FILE_PATH     // Catch:{ Exception -> 0x00a8 }
            r2.append(r3)     // Catch:{ Exception -> 0x00a8 }
            java.lang.String r3 = java.io.File.separator     // Catch:{ Exception -> 0x00a8 }
            r2.append(r3)     // Catch:{ Exception -> 0x00a8 }
            java.lang.String r3 = BUILD_PROPERTIES_FILE_NAME     // Catch:{ Exception -> 0x00a8 }
            r2.append(r3)     // Catch:{ Exception -> 0x00a8 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x00a8 }
            java.io.File r3 = new java.io.File     // Catch:{ Exception -> 0x00a8 }
            r3.<init>(r2)     // Catch:{ Exception -> 0x00a8 }
            boolean r4 = r3.exists()     // Catch:{ Exception -> 0x00a8 }
            r5 = 1
            if (r4 == 0) goto L_0x0061
            java.lang.String r3 = io.dcloud.common.util.IOUtil.readStringFile(r2)     // Catch:{ Exception -> 0x00a8 }
            boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Exception -> 0x00a8 }
            if (r4 != 0) goto L_0x0065
            boolean r3 = r3.equals(r7)     // Catch:{ Exception -> 0x00a8 }
            if (r3 != 0) goto L_0x0065
            goto L_0x0064
        L_0x0061:
            r3.createNewFile()     // Catch:{ Exception -> 0x00a8 }
        L_0x0064:
            r0 = 1
        L_0x0065:
            if (r0 == 0) goto L_0x00ac
            io.dcloud.common.util.IOUtil.writeStringFile(r2, r7)     // Catch:{ Exception -> 0x00a8 }
            org.json.JSONObject r7 = new org.json.JSONObject     // Catch:{ Exception -> 0x00a8 }
            r7.<init>()     // Catch:{ Exception -> 0x00a8 }
            java.util.Properties r0 = r6.properties     // Catch:{ Exception -> 0x00a8 }
            if (r0 == 0) goto L_0x0097
            java.util.Set r0 = r0.entrySet()     // Catch:{ Exception -> 0x00a8 }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ Exception -> 0x00a8 }
        L_0x007b:
            boolean r2 = r0.hasNext()     // Catch:{ Exception -> 0x00a8 }
            if (r2 == 0) goto L_0x0097
            java.lang.Object r2 = r0.next()     // Catch:{ Exception -> 0x00a8 }
            java.util.Map$Entry r2 = (java.util.Map.Entry) r2     // Catch:{ Exception -> 0x00a8 }
            java.lang.Object r3 = r2.getKey()     // Catch:{ Exception -> 0x00a8 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x00a8 }
            java.lang.Object r2 = r2.getValue()     // Catch:{ Exception -> 0x00a8 }
            r7.put(r3, r2)     // Catch:{ Exception -> 0x00a8 }
            goto L_0x007b
        L_0x0097:
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x00a8 }
            if (r7 == 0) goto L_0x00ac
            java.lang.String r0 = r7.trim()     // Catch:{ Exception -> 0x00a8 }
            boolean r0 = r1.equals(r0)     // Catch:{ Exception -> 0x00a8 }
            if (r0 != 0) goto L_0x00ac
            return r7
        L_0x00a8:
            r7 = move-exception
            r7.printStackTrace()
        L_0x00ac:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.BuildProperties.getBuildPropertiesLimit(android.content.Context):java.lang.String");
    }

    public String getProperty(String str) {
        return this.properties.getProperty(str);
    }

    public boolean isEmpty() {
        return this.properties.isEmpty();
    }

    public Set keySet() {
        return this.properties.keySet();
    }

    public Enumeration keys() {
        return this.properties.keys();
    }

    public int size() {
        return this.properties.size();
    }

    public Collection values() {
        return this.properties.values();
    }

    public String getProperty(String str, String str2) {
        return this.properties.getProperty(str, str2);
    }
}
