package io.dcloud.common.util.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import io.dcloud.application.DCLoudApplicationImpl;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.internal.sdk.SDK;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DCStorage {
    public static final int ERROR_DEF = -1;
    public static final int ERROR_FULL = -2;
    public static final int ERROR_NO_KEY = -3;
    public static final int SUCCESS = 1;
    private static DCStorage mInstance;
    private String DBFILE_NAME = "_dbfile";
    private String DCDBFILE_START = "DCDBFile_";
    private String ERROR_TAG = "__ERROR__";
    private String TABLET_TAG = "_storage";
    /* access modifiers changed from: private */
    public DCSQLiteOpenHelper mDatabaseSupplier;
    /* access modifiers changed from: private */
    public ExecutorService mExecutorService;

    public class StorageInfo {
        public int code;
        public String meg;
        public Object v;

        public StorageInfo() {
        }
    }

    private DCStorage(Context context) {
        this.mDatabaseSupplier = DCSQLiteOpenHelper.getSQLiteOpenHelper(context);
    }

    private void clearDBFile(Context context, String str) {
        File file = new File(getBaseDBFilePath(context, str));
        if (file.exists()) {
            file.delete();
        }
    }

    private String getBaseDBFilePath(Context context, String str) {
        return context.getFilesDir().getAbsolutePath() + "/apps/" + str + "/dbfile/";
    }

    private String getCurrentTableName(String str) {
        return "DC_" + Math.abs(str.hashCode()) + this.TABLET_TAG;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0059, code lost:
        if (r2 != null) goto L_0x005b;
     */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0054 A[Catch:{ FileNotFoundException -> 0x0055, IOException -> 0x004e, all -> 0x004c }] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0061 A[SYNTHETIC, Splitter:B:35:0x0061] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String getDBFileValue(java.lang.String r6) {
        /*
            r5 = this;
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r6)
            java.lang.String r1 = ""
            if (r0 != 0) goto L_0x0065
            java.lang.String r0 = r5.DCDBFILE_START
            boolean r0 = r6.startsWith(r0)
            if (r0 == 0) goto L_0x0016
            r0 = 9
            java.lang.String r6 = r6.substring(r0)
        L_0x0016:
            java.io.File r0 = new java.io.File
            r0.<init>(r6)
            r2 = 0
            boolean r0 = r0.exists()
            if (r0 == 0) goto L_0x0065
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r0.<init>()
            java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch:{ FileNotFoundException -> 0x0055, IOException -> 0x004e }
            java.io.FileReader r4 = new java.io.FileReader     // Catch:{ FileNotFoundException -> 0x0055, IOException -> 0x004e }
            r4.<init>(r6)     // Catch:{ FileNotFoundException -> 0x0055, IOException -> 0x004e }
            r3.<init>(r4)     // Catch:{ FileNotFoundException -> 0x0055, IOException -> 0x004e }
        L_0x0031:
            java.lang.String r6 = r3.readLine()     // Catch:{ FileNotFoundException -> 0x0049, IOException -> 0x0046, all -> 0x0043 }
            if (r6 == 0) goto L_0x003b
            r0.append(r6)     // Catch:{ FileNotFoundException -> 0x0049, IOException -> 0x0046, all -> 0x0043 }
            goto L_0x0031
        L_0x003b:
            java.lang.String r1 = r0.toString()     // Catch:{ FileNotFoundException -> 0x0049, IOException -> 0x0046, all -> 0x0043 }
            r3.close()     // Catch:{ IOException -> 0x0065 }
            goto L_0x0065
        L_0x0043:
            r6 = move-exception
            r2 = r3
            goto L_0x005f
        L_0x0046:
            r6 = move-exception
            r2 = r3
            goto L_0x004f
        L_0x0049:
            r6 = move-exception
            r2 = r3
            goto L_0x0056
        L_0x004c:
            r6 = move-exception
            goto L_0x005f
        L_0x004e:
            r6 = move-exception
        L_0x004f:
            r6.printStackTrace()     // Catch:{ all -> 0x004c }
            if (r2 == 0) goto L_0x0065
            goto L_0x005b
        L_0x0055:
            r6 = move-exception
        L_0x0056:
            r6.printStackTrace()     // Catch:{ all -> 0x004c }
            if (r2 == 0) goto L_0x0065
        L_0x005b:
            r2.close()     // Catch:{ IOException -> 0x0065 }
            goto L_0x0065
        L_0x005f:
            if (r2 == 0) goto L_0x0064
            r2.close()     // Catch:{ IOException -> 0x0064 }
        L_0x0064:
            throw r6
        L_0x0065:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.db.DCStorage.getDBFileValue(java.lang.String):java.lang.String");
    }

    public static DCStorage getDCStorage(Context context) {
        if (mInstance == null && context != null) {
            mInstance = new DCStorage(context);
        }
        return mInstance;
    }

    private String getMsgForCode(Context context, int i) {
        if (i == -3) {
            return context.getString(R.string.dcloud_storage_not_find_error);
        }
        if (i != -2) {
            return i != 1 ? "" : context.getString(R.string.dcloud_storage_success);
        }
        return context.getString(R.string.dcloud_storage_ceiling_error);
    }

    private void removeDBFile(Context context, String str, String str2) {
        removeDBFile(getBaseDBFilePath(context, str) + str2 + this.DBFILE_NAME);
    }

    private String saveDBFileValue(Context context, String str, String str2, String str3) {
        String baseDBFilePath = getBaseDBFilePath(context, str);
        File file = new File(baseDBFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (file.isFile()) {
            file.delete();
            file.mkdirs();
        }
        String str4 = baseDBFilePath + str2.hashCode() + this.DBFILE_NAME;
        File file2 = new File(str4);
        if (file2.exists()) {
            file2.delete();
        }
        try {
            file2.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(str4);
            fileOutputStream.write(str3.getBytes());
            fileOutputStream.close();
            return this.DCDBFILE_START + str4;
        } catch (IOException e) {
            e.printStackTrace();
            return this.ERROR_TAG + e.getMessage() + str4;
        }
    }

    public synchronized void checkSPstorageToDB(Context context, String str) {
        SharedPreferences orCreateBundle = SP.getOrCreateBundle(context, str + "_storages");
        SharedPreferences.Editor edit = orCreateBundle.edit();
        Map<String, ?> all = orCreateBundle.getAll();
        if (all != null && all.size() > 0) {
            for (String next : all.keySet()) {
                if (performSetItem(context, str, next, (String) all.get(next)).code == 1) {
                    edit.remove(next);
                }
            }
        }
        edit.commit();
    }

    public void close() {
        execute(new Runnable() {
            public void run() {
                try {
                    if (DCStorage.this.mDatabaseSupplier != null) {
                        DCStorage.this.mDatabaseSupplier.closeDatabase();
                    }
                    if (DCStorage.this.mExecutorService != null) {
                        DCStorage.this.mExecutorService.shutdown();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ExecutorService unused = DCStorage.this.mExecutorService = null;
            }
        });
    }

    public void execute(Runnable runnable) {
        if (this.mExecutorService == null) {
            this.mExecutorService = Executors.newSingleThreadExecutor();
        }
        if (runnable != null && !this.mExecutorService.isShutdown() && !this.mExecutorService.isTerminated()) {
            this.mExecutorService.execute(runnable);
        }
    }

    public synchronized StorageInfo performClear(Context context, String str) {
        StorageInfo storageInfo;
        SQLiteDatabase database = this.mDatabaseSupplier.getDatabase(getCurrentTableName(str));
        storageInfo = new StorageInfo();
        if (database == null) {
            storageInfo.code = -1;
            storageInfo.meg = context.getString(R.string.dcloud_storage_no_db_error);
            return storageInfo;
        }
        storageInfo.code = 1;
        try {
            database.execSQL("delete from " + getCurrentTableName(str));
            if (!SDK.isUniMPSDK()) {
                this.mDatabaseSupplier.ensureDatabase(getCurrentTableName(str));
            }
            clearDBFile(context, str);
        } catch (Exception e) {
            storageInfo.code = -1;
            storageInfo.meg = context.getString(R.string.dcloud_storage_native_error) + e.getMessage();
        }
        return storageInfo;
    }

    public synchronized StorageInfo performGetAllKeys(String str) {
        SQLiteDatabase database = this.mDatabaseSupplier.getDatabase(getCurrentTableName(str));
        StorageInfo storageInfo = new StorageInfo();
        Context context = DCLoudApplicationImpl.self().getContext();
        if (database == null) {
            storageInfo.code = -1;
            storageInfo.meg = context.getString(R.string.dcloud_storage_no_db_error);
            return storageInfo;
        }
        ArrayList arrayList = new ArrayList();
        Cursor query = database.query(getCurrentTableName(str), new String[]{IApp.ConfigProperty.CONFIG_KEY}, (String) null, (String[]) null, (String) null, (String) null, (String) null);
        while (query.moveToNext()) {
            try {
                arrayList.add(query.getString(query.getColumnIndex(IApp.ConfigProperty.CONFIG_KEY)));
            } catch (Exception e) {
                try {
                    storageInfo.code = -1;
                    storageInfo.meg = context.getString(R.string.dcloud_storage_native_error) + e.getMessage();
                } catch (Throwable th) {
                    query.close();
                    throw th;
                }
            }
        }
        storageInfo.code = 1;
        storageInfo.v = arrayList;
        query.close();
        return storageInfo;
    }

    public synchronized StorageInfo performGetItem(String str, String str2) {
        SQLiteDatabase database = this.mDatabaseSupplier.getDatabase(getCurrentTableName(str));
        StorageInfo storageInfo = new StorageInfo();
        Context context = DCLoudApplicationImpl.self().getContext();
        if (database == null) {
            storageInfo.code = -1;
            storageInfo.meg = context.getString(R.string.dcloud_storage_no_db_error);
            return storageInfo;
        }
        Cursor query = database.query(getCurrentTableName(str), new String[]{"value"}, "key=?", new String[]{str2}, (String) null, (String) null, (String) null);
        try {
            if (query.moveToNext()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("timestamp", DCSQLiteOpenHelper.sDateFormatter.format(new Date()));
                this.mDatabaseSupplier.getDatabase(getCurrentTableName(str)).update(getCurrentTableName(str), contentValues, "key= ?", new String[]{str2});
                String string = query.getString(query.getColumnIndex("value"));
                if (TextUtils.isEmpty(string) || !string.startsWith(this.DCDBFILE_START)) {
                    storageInfo.v = string;
                } else {
                    String dBFileValue = getDBFileValue(string);
                    if (PdrUtil.isEmpty(dBFileValue)) {
                        storageInfo.v = string;
                    } else {
                        storageInfo.v = dBFileValue;
                    }
                }
                storageInfo.code = 1;
                query.close();
                return storageInfo;
            }
            storageInfo.code = -3;
            storageInfo.meg = getMsgForCode(context, -3);
            query.close();
            return storageInfo;
        } catch (Exception e) {
            try {
                storageInfo.code = -1;
                storageInfo.meg = context.getString(R.string.dcloud_storage_native_error) + e.getMessage();
                return storageInfo;
            } finally {
                query.close();
            }
        }
    }

    public synchronized StorageInfo performGetLength(String str) {
        SQLiteDatabase database = this.mDatabaseSupplier.getDatabase(getCurrentTableName(str));
        StorageInfo storageInfo = new StorageInfo();
        Context context = DCLoudApplicationImpl.self().getContext();
        if (database == null) {
            storageInfo.code = -1;
            storageInfo.meg = context.getString(R.string.dcloud_storage_no_db_error);
            return storageInfo;
        }
        SQLiteStatement sQLiteStatement = null;
        try {
            sQLiteStatement = database.compileStatement("SELECT count(key) FROM " + getCurrentTableName(str));
            storageInfo.v = Long.valueOf(sQLiteStatement.simpleQueryForLong());
            storageInfo.code = 1;
            sQLiteStatement.close();
            return storageInfo;
        } catch (Exception e) {
            storageInfo.code = -1;
            storageInfo.meg = context.getString(R.string.dcloud_storage_native_error) + e.getMessage();
            if (sQLiteStatement != null) {
                sQLiteStatement.close();
            }
            return storageInfo;
        } catch (Throwable unused) {
            if (sQLiteStatement != null) {
                sQLiteStatement.close();
            }
            return storageInfo;
        }
    }

    public synchronized StorageInfo performRemoveItem(Context context, String str, String str2) {
        StorageInfo storageInfo;
        SQLiteDatabase database = this.mDatabaseSupplier.getDatabase(getCurrentTableName(str));
        storageInfo = new StorageInfo();
        if (database == null) {
            storageInfo.code = -1;
            storageInfo.meg = context.getString(R.string.dcloud_storage_no_db_error);
            return storageInfo;
        }
        try {
            int delete = database.delete(getCurrentTableName(str), "key=?", new String[]{str2});
            if (delete > 0) {
                removeDBFile(context, str, str2);
            }
            if (delete == 1) {
                storageInfo.code = 1;
            } else {
                storageInfo.code = -3;
                storageInfo.meg = getMsgForCode(context, -3);
            }
        } catch (Exception e) {
            storageInfo.code = -1;
            storageInfo.meg = context.getString(R.string.dcloud_storage_native_error) + e.getMessage();
        }
        return storageInfo;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00ac, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized io.dcloud.common.util.db.DCStorage.StorageInfo performSetItem(android.content.Context r9, java.lang.String r10, java.lang.String r11, java.lang.String r12) {
        /*
            r8 = this;
            monitor-enter(r8)
            io.dcloud.common.util.db.DCSQLiteOpenHelper r0 = r8.mDatabaseSupplier     // Catch:{ all -> 0x010b }
            java.lang.String r1 = r8.getCurrentTableName(r10)     // Catch:{ all -> 0x010b }
            android.database.sqlite.SQLiteDatabase r0 = r0.getDatabase(r1)     // Catch:{ all -> 0x010b }
            io.dcloud.common.util.db.DCStorage$StorageInfo r1 = new io.dcloud.common.util.db.DCStorage$StorageInfo     // Catch:{ all -> 0x010b }
            r1.<init>()     // Catch:{ all -> 0x010b }
            r2 = -1
            if (r0 != 0) goto L_0x001f
            r1.code = r2     // Catch:{ all -> 0x010b }
            int r10 = io.dcloud.base.R.string.dcloud_storage_no_db_error     // Catch:{ all -> 0x010b }
            java.lang.String r9 = r9.getString(r10)     // Catch:{ all -> 0x010b }
            r1.meg = r9     // Catch:{ all -> 0x010b }
            monitor-exit(r8)
            return r1
        L_0x001f:
            boolean r3 = io.dcloud.common.util.PdrUtil.isEmpty(r11)     // Catch:{ all -> 0x010b }
            if (r3 == 0) goto L_0x0031
            r1.code = r2     // Catch:{ all -> 0x010b }
            int r10 = io.dcloud.base.R.string.dcloud_storage_key_error     // Catch:{ all -> 0x010b }
            java.lang.String r9 = r9.getString(r10)     // Catch:{ all -> 0x010b }
            r1.meg = r9     // Catch:{ all -> 0x010b }
            monitor-exit(r8)
            return r1
        L_0x0031:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x010b }
            r3.<init>()     // Catch:{ all -> 0x010b }
            java.lang.String r4 = "INSERT OR REPLACE INTO "
            r3.append(r4)     // Catch:{ all -> 0x010b }
            java.lang.String r4 = r8.getCurrentTableName(r10)     // Catch:{ all -> 0x010b }
            r3.append(r4)     // Catch:{ all -> 0x010b }
            java.lang.String r4 = " VALUES (?,?,?);"
            r3.append(r4)     // Catch:{ all -> 0x010b }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x010b }
            r4 = 0
            java.text.SimpleDateFormat r5 = io.dcloud.common.util.db.DCSQLiteOpenHelper.sDateFormatter     // Catch:{ all -> 0x010b }
            java.util.Date r6 = new java.util.Date     // Catch:{ all -> 0x010b }
            r6.<init>()     // Catch:{ all -> 0x010b }
            java.lang.String r5 = r5.format(r6)     // Catch:{ all -> 0x010b }
            byte[] r6 = r12.getBytes()     // Catch:{ Exception -> 0x00cc }
            int r6 = r6.length     // Catch:{ Exception -> 0x00cc }
            r7 = 1800000(0x1b7740, float:2.522337E-39)
            if (r6 < r7) goto L_0x00ad
            java.lang.String r12 = r8.saveDBFileValue(r9, r10, r11, r12)     // Catch:{ Exception -> 0x00cc }
            boolean r10 = android.text.TextUtils.isEmpty(r12)     // Catch:{ Exception -> 0x00cc }
            if (r10 != 0) goto L_0x0073
            java.lang.String r10 = r8.ERROR_TAG     // Catch:{ Exception -> 0x00cc }
            boolean r10 = r12.startsWith(r10)     // Catch:{ Exception -> 0x00cc }
            if (r10 == 0) goto L_0x00ad
        L_0x0073:
            r1.code = r2     // Catch:{ Exception -> 0x00cc }
            int r10 = io.dcloud.base.R.string.dcloud_storage_write_big_error     // Catch:{ Exception -> 0x00cc }
            java.lang.String r10 = r9.getString(r10)     // Catch:{ Exception -> 0x00cc }
            r1.meg = r10     // Catch:{ Exception -> 0x00cc }
            boolean r10 = android.text.TextUtils.isEmpty(r12)     // Catch:{ Exception -> 0x00cc }
            if (r10 != 0) goto L_0x00ab
            java.lang.String r10 = r8.ERROR_TAG     // Catch:{ Exception -> 0x00cc }
            boolean r10 = r12.startsWith(r10)     // Catch:{ Exception -> 0x00cc }
            if (r10 == 0) goto L_0x00ab
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00cc }
            r10.<init>()     // Catch:{ Exception -> 0x00cc }
            java.lang.String r11 = r1.meg     // Catch:{ Exception -> 0x00cc }
            r10.append(r11)     // Catch:{ Exception -> 0x00cc }
            java.lang.String r11 = " error "
            r10.append(r11)     // Catch:{ Exception -> 0x00cc }
            java.lang.String r11 = r8.ERROR_TAG     // Catch:{ Exception -> 0x00cc }
            java.lang.String r0 = ""
            java.lang.String r11 = r12.replace(r11, r0)     // Catch:{ Exception -> 0x00cc }
            r10.append(r11)     // Catch:{ Exception -> 0x00cc }
            java.lang.String r10 = r10.toString()     // Catch:{ Exception -> 0x00cc }
            r1.meg = r10     // Catch:{ Exception -> 0x00cc }
        L_0x00ab:
            monitor-exit(r8)
            return r1
        L_0x00ad:
            android.database.sqlite.SQLiteStatement r4 = r0.compileStatement(r3)     // Catch:{ Exception -> 0x00cc }
            r4.clearBindings()     // Catch:{ Exception -> 0x00cc }
            r10 = 1
            r4.bindString(r10, r11)     // Catch:{ Exception -> 0x00cc }
            r11 = 2
            r4.bindString(r11, r12)     // Catch:{ Exception -> 0x00cc }
            r11 = 3
            r4.bindString(r11, r5)     // Catch:{ Exception -> 0x00cc }
            r4.execute()     // Catch:{ Exception -> 0x00cc }
            r1.code = r10     // Catch:{ Exception -> 0x00cc }
            r4.close()     // Catch:{ all -> 0x010b }
            monitor-exit(r8)
            return r1
        L_0x00ca:
            r9 = move-exception
            goto L_0x0105
        L_0x00cc:
            r10 = move-exception
            boolean r11 = r10 instanceof android.database.sqlite.SQLiteFullException     // Catch:{ all -> 0x00ca }
            if (r11 == 0) goto L_0x00e1
            r10 = -2
            r1.code = r10     // Catch:{ all -> 0x00ca }
            java.lang.String r9 = r8.getMsgForCode(r9, r10)     // Catch:{ all -> 0x00ca }
            r1.meg = r9     // Catch:{ all -> 0x00ca }
            if (r4 == 0) goto L_0x00df
            r4.close()     // Catch:{ all -> 0x010b }
        L_0x00df:
            monitor-exit(r8)
            return r1
        L_0x00e1:
            r1.code = r2     // Catch:{ all -> 0x00ca }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ca }
            r11.<init>()     // Catch:{ all -> 0x00ca }
            int r12 = io.dcloud.base.R.string.dcloud_storage_native_error     // Catch:{ all -> 0x00ca }
            java.lang.String r9 = r9.getString(r12)     // Catch:{ all -> 0x00ca }
            r11.append(r9)     // Catch:{ all -> 0x00ca }
            java.lang.String r9 = r10.getMessage()     // Catch:{ all -> 0x00ca }
            r11.append(r9)     // Catch:{ all -> 0x00ca }
            java.lang.String r9 = r11.toString()     // Catch:{ all -> 0x00ca }
            r1.meg = r9     // Catch:{ all -> 0x00ca }
            if (r4 == 0) goto L_0x0103
            r4.close()     // Catch:{ all -> 0x010b }
        L_0x0103:
            monitor-exit(r8)
            return r1
        L_0x0105:
            if (r4 == 0) goto L_0x010a
            r4.close()     // Catch:{ all -> 0x010b }
        L_0x010a:
            throw r9     // Catch:{ all -> 0x010b }
        L_0x010b:
            r9 = move-exception
            monitor-exit(r8)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.db.DCStorage.performSetItem(android.content.Context, java.lang.String, java.lang.String, java.lang.String):io.dcloud.common.util.db.DCStorage$StorageInfo");
    }

    private void removeDBFile(String str) {
        if (!PdrUtil.isEmpty(str)) {
            if (str.startsWith(this.DCDBFILE_START)) {
                str = str.substring(9);
            }
            File file = new File(str);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
