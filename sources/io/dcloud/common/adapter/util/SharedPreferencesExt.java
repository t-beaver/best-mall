package io.dcloud.common.adapter.util;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class SharedPreferencesExt implements SharedPreferences {
    static final byte BOOLEAN = 2;
    static final byte FLOAT = 3;
    static final byte INT = 1;
    static final byte LONG = 4;
    public static final String N_BASE = "pdr";
    static final byte STRING = 5;
    static final byte STRING_SET = 6;
    private static final HashMap<String, LinkedHashMap> mLinkedHashMapCollenction = new HashMap<>(4);
    private W2AEditor editor;
    private File mFileHandler;
    private long mFileModifyTime;
    /* access modifiers changed from: private */
    public LinkedHashMap<String, Object> mKeyValue;

    public static class W2AEditor implements SharedPreferences.Editor {
        LinkedHashMap<String, Object> mCache;
        SharedPreferencesExt mSP;

        public void apply() {
            this.mSP.mKeyValue.putAll(this.mCache);
            this.mCache.clear();
        }

        public SharedPreferences.Editor clear() {
            this.mCache.clear();
            return this;
        }

        public boolean commit() {
            this.mSP.mKeyValue.putAll(this.mCache);
            boolean unused = this.mSP.saveLocal();
            this.mCache.clear();
            return false;
        }

        public SharedPreferences.Editor putBoolean(String str, boolean z) {
            this.mCache.put(str, Boolean.valueOf(z));
            return this;
        }

        public SharedPreferences.Editor putFloat(String str, float f) {
            this.mCache.put(str, Float.valueOf(f));
            return this;
        }

        public SharedPreferences.Editor putInt(String str, int i) {
            this.mCache.put(str, Integer.valueOf(i));
            return this;
        }

        public SharedPreferences.Editor putLong(String str, long j) {
            this.mCache.put(str, Long.valueOf(j));
            return this;
        }

        public SharedPreferences.Editor putString(String str, String str2) {
            this.mCache.put(str, str2);
            return this;
        }

        public SharedPreferences.Editor putStringSet(String str, Set<String> set) {
            this.mCache.put(str, set);
            return this;
        }

        public SharedPreferences.Editor remove(String str) {
            this.mCache.remove(str);
            return this;
        }

        private W2AEditor(SharedPreferencesExt sharedPreferencesExt) {
            this.mCache = new LinkedHashMap<>(16);
            this.mSP = null;
            this.mSP = sharedPreferencesExt;
        }
    }

    public SharedPreferencesExt(Context context, String str) {
        this(context, str, 0);
    }

    private void checkModify() {
        if (this.mFileModifyTime != this.mFileHandler.lastModified()) {
            reset();
        }
    }

    private void reset() {
        try {
            this.mKeyValue.clear();
            this.mFileModifyTime = this.mFileHandler.lastModified();
            FileInputStream fileInputStream = new FileInputStream(this.mFileHandler);
            byte[] bArr = new byte[4];
            byte[] bArr2 = new byte[1];
            byte[] bArr3 = new byte[256];
            while (fileInputStream.read(bArr) > 0) {
                int i = ByteUtil.toInt(bArr);
                if (i > bArr3.length) {
                    bArr3 = new byte[i];
                }
                String str = new String(bArr3, 0, fileInputStream.read(bArr3, 0, i), "UTF-8");
                fileInputStream.read(bArr2);
                byte b = bArr2[0];
                fileInputStream.read(bArr);
                int i2 = ByteUtil.toInt(bArr);
                if (i2 > bArr3.length) {
                    bArr3 = new byte[i2];
                }
                load(this.mKeyValue, str, bArr3, fileInputStream.read(bArr3, 0, i2), b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public boolean saveLocal() {
        save(this.mKeyValue);
        return false;
    }

    public boolean contains(String str) {
        checkModify();
        return this.mKeyValue.containsKey(str);
    }

    public SharedPreferences.Editor edit() {
        checkModify();
        return this.editor;
    }

    public Map<String, ?> getAll() {
        checkModify();
        return (HashMap) this.mKeyValue.clone();
    }

    public boolean getBoolean(String str, boolean z) {
        try {
            checkModify();
            return this.mKeyValue.containsKey(str) ? ((Boolean) this.mKeyValue.get(str)).booleanValue() : z;
        } catch (Exception e) {
            e.printStackTrace();
            return z;
        }
    }

    public float getFloat(String str, float f) {
        try {
            checkModify();
            return this.mKeyValue.containsKey(str) ? ((Float) this.mKeyValue.get(str)).floatValue() : f;
        } catch (Exception e) {
            e.printStackTrace();
            return f;
        }
    }

    public int getInt(String str, int i) {
        try {
            checkModify();
            return this.mKeyValue.containsKey(str) ? ((Integer) this.mKeyValue.get(str)).intValue() : i;
        } catch (Exception e) {
            e.printStackTrace();
            return i;
        }
    }

    public long getLong(String str, long j) {
        try {
            checkModify();
            return this.mKeyValue.containsKey(str) ? ((Long) this.mKeyValue.get(str)).longValue() : j;
        } catch (Exception e) {
            e.printStackTrace();
            return j;
        }
    }

    public String getString(String str, String str2) {
        try {
            checkModify();
            return this.mKeyValue.containsKey(str) ? (String) this.mKeyValue.get(str) : str2;
        } catch (Exception e) {
            e.printStackTrace();
            return str2;
        }
    }

    public Set<String> getStringSet(String str, Set<String> set) {
        try {
            checkModify();
            return (Set) this.mKeyValue.get(str);
        } catch (Exception e) {
            e.printStackTrace();
            return set;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean hasChaged() {
        return this.mFileHandler.lastModified() != this.mFileModifyTime;
    }

    /* access modifiers changed from: package-private */
    public void load(LinkedHashMap<String, Object> linkedHashMap, String str, byte[] bArr, int i, byte b) {
        boolean z = true;
        int i2 = 0;
        switch (b) {
            case 1:
                linkedHashMap.put(str, Integer.valueOf(ByteUtil.toInt(new byte[]{bArr[0], bArr[1], bArr[2], bArr[3]})));
                return;
            case 2:
                if (bArr[0] != 1) {
                    z = false;
                }
                linkedHashMap.put(str, Boolean.valueOf(z));
                return;
            case 3:
                byte[] bArr2 = new byte[i];
                while (i2 < i && i2 < bArr.length) {
                    bArr2[i2] = bArr[i2];
                    i2++;
                }
                linkedHashMap.put(str, Float.valueOf(ByteUtil.bytesToFloat(bArr2)));
                return;
            case 4:
                byte[] bArr3 = new byte[i];
                while (i2 < i && i2 < bArr.length) {
                    bArr3[i2] = bArr[i2];
                    i2++;
                }
                linkedHashMap.put(str, Long.valueOf(ByteUtil.bytesToLong(bArr3)));
                return;
            case 5:
                try {
                    linkedHashMap.put(str, new String(bArr, 0, i, "UTF-8"));
                    return;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return;
                }
            case 6:
                try {
                    linkedHashMap.put(str, new String(bArr, 0, i, "UTF-8"));
                    return;
                } catch (UnsupportedEncodingException e2) {
                    e2.printStackTrace();
                    return;
                }
            default:
                return;
        }
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
    }

    /* access modifiers changed from: package-private */
    public void save(LinkedHashMap<String, Object> linkedHashMap) {
        byte[] bArr;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(this.mFileHandler, false);
            byte[] bArr2 = new byte[1];
            for (Map.Entry next : linkedHashMap.entrySet()) {
                byte[] bytes = ((String) next.getKey()).getBytes("UTF-8");
                fileOutputStream.write(ByteUtil.toBytes(bytes.length));
                fileOutputStream.write(bytes);
                Object value = next.getValue();
                Class<?> cls = value.getClass();
                if (cls != Integer.TYPE) {
                    if (cls != Integer.class) {
                        if (cls != Boolean.TYPE) {
                            if (cls != Boolean.class) {
                                if (cls == String.class) {
                                    bArr2[0] = 5;
                                    bArr = ((String) value).getBytes("UTF-8");
                                } else {
                                    if (cls != Long.TYPE) {
                                        if (cls != Long.class) {
                                            if (cls != Float.TYPE) {
                                                if (cls != Float.class) {
                                                    if (cls == Set.class) {
                                                        bArr2[0] = 6;
                                                        bArr = value.toString().getBytes("UTF-8");
                                                    }
                                                }
                                            }
                                            bArr2[0] = 3;
                                            bArr = ByteUtil.floatToBytes(((Float) value).floatValue());
                                        }
                                    }
                                    bArr2[0] = 4;
                                    bArr = ByteUtil.longToBytes(((Long) value).longValue());
                                }
                                fileOutputStream.write(bArr2);
                                fileOutputStream.write(ByteUtil.toBytes(bArr.length));
                                fileOutputStream.write(bArr);
                            }
                        }
                        bArr2[0] = 2;
                        bArr = ((Boolean) value).booleanValue() ? new byte[]{1} : new byte[]{0};
                        fileOutputStream.write(bArr2);
                        fileOutputStream.write(ByteUtil.toBytes(bArr.length));
                        fileOutputStream.write(bArr);
                    }
                }
                bArr2[0] = 1;
                bArr = ByteUtil.toBytes(((Integer) value).intValue());
                fileOutputStream.write(bArr2);
                fileOutputStream.write(ByteUtil.toBytes(bArr.length));
                fileOutputStream.write(bArr);
            }
            this.mFileModifyTime = this.mFileHandler.lastModified();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
    }

    public SharedPreferencesExt(Context context, String str, int i) {
        this.editor = null;
        this.mFileHandler = null;
        this.mFileModifyTime = System.currentTimeMillis();
        this.mKeyValue = null;
        this.mKeyValue = mLinkedHashMapCollenction.get(str);
        this.editor = new W2AEditor();
        String str2 = "/data/data/" + context.getPackageName() + "/shared_prefs_ext/" + str;
        if (this.mKeyValue == null) {
            this.mKeyValue = new LinkedHashMap<>(16);
            File file = new File(str2);
            this.mFileHandler = file;
            if (!file.getParentFile().exists()) {
                this.mFileHandler.getParentFile().mkdirs();
            }
            if (!this.mFileHandler.exists()) {
                try {
                    this.mFileHandler.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                reset();
            }
            mLinkedHashMapCollenction.put(str, this.mKeyValue);
            return;
        }
        this.mFileHandler = new File(str2);
    }
}
