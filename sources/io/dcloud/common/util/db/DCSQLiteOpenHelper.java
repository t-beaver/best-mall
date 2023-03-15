package io.dcloud.common.util.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DCSQLiteOpenHelper extends SQLiteOpenHelper {
    static final String COLUMN_KEY = "key";
    static final String COLUMN_TIMESTAMP = "timestamp";
    static final String COLUMN_VALUE = "value";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS default_wx_storage (key TEXT PRIMARY KEY,value TEXT NOT NULL,timestamp TEXT NOT NULL)";
    private static final String DATABASE_NAME = "DCStorage";
    private static final int DATABASE_VERSION = 1;
    private static final int SLEEP_TIME_MS = 30;
    static final String TABLE_STORAGE = "default_wx_storage";
    static final String TAG_STORAGE = "dc_storage";
    private static DCSQLiteOpenHelper mInstance;
    static SimpleDateFormat sDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private Context mContext;
    private SQLiteDatabase mDb;

    private DCSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
        this.mContext = context;
    }

    private void createTableIfNotExists(SQLiteDatabase sQLiteDatabase) {
        Cursor cursor = null;
        try {
            cursor = sQLiteDatabase.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = 'default_wx_storage'", (String[]) null);
            if (cursor == null || cursor.getCount() <= 0) {
                sQLiteDatabase.execSQL(CREATE_TABLE);
                if (cursor == null) {
                    return;
                }
                cursor.close();
                return;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor == null) {
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    private boolean deleteDB() {
        closeDatabase();
        return this.mContext.deleteDatabase(DATABASE_NAME);
    }

    public static DCSQLiteOpenHelper getSQLiteOpenHelper(Context context) {
        if (mInstance == null) {
            mInstance = new DCSQLiteOpenHelper(context);
        }
        return mInstance;
    }

    public synchronized void closeDatabase() {
        SQLiteDatabase sQLiteDatabase = this.mDb;
        if (sQLiteDatabase != null && sQLiteDatabase.isOpen()) {
            this.mDb.close();
            this.mDb = null;
        }
        mInstance = null;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(10:16|17|18|19|20|21|22|23|45|24) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:22:0x002c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void ensureDatabase(java.lang.String r4) {
        /*
            r3 = this;
            monitor-enter(r3)
            android.database.sqlite.SQLiteDatabase r0 = r3.mDb     // Catch:{ all -> 0x006e }
            if (r0 == 0) goto L_0x0012
            boolean r0 = r0.isOpen()     // Catch:{ all -> 0x006e }
            if (r0 == 0) goto L_0x0012
            android.database.sqlite.SQLiteDatabase r0 = r3.mDb     // Catch:{ all -> 0x006e }
            r3.createTableIfNotExists(r0, r4)     // Catch:{ all -> 0x006e }
            monitor-exit(r3)
            return
        L_0x0012:
            r0 = 0
        L_0x0013:
            r1 = 2
            if (r0 >= r1) goto L_0x0036
            if (r0 <= 0) goto L_0x001b
            r3.deleteDB()     // Catch:{ SQLiteException -> 0x0022 }
        L_0x001b:
            android.database.sqlite.SQLiteDatabase r1 = r3.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0022 }
            r3.mDb = r1     // Catch:{ SQLiteException -> 0x0022 }
            goto L_0x0036
        L_0x0022:
            r1 = move-exception
            r1.printStackTrace()     // Catch:{ all -> 0x004e }
            r1 = 30
            java.lang.Thread.sleep(r1)     // Catch:{ InterruptedException -> 0x002c }
            goto L_0x0033
        L_0x002c:
            java.lang.Thread r1 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x004e }
            r1.interrupt()     // Catch:{ all -> 0x004e }
        L_0x0033:
            int r0 = r0 + 1
            goto L_0x0013
        L_0x0036:
            android.database.sqlite.SQLiteDatabase r0 = r3.mDb     // Catch:{ all -> 0x004e }
            if (r0 != 0) goto L_0x003c
            monitor-exit(r3)
            return
        L_0x003c:
            boolean r0 = android.text.TextUtils.isEmpty(r4)     // Catch:{ all -> 0x004e }
            if (r0 == 0) goto L_0x0048
            android.database.sqlite.SQLiteDatabase r4 = r3.mDb     // Catch:{ all -> 0x004e }
            r3.createTableIfNotExists(r4)     // Catch:{ all -> 0x004e }
            goto L_0x006c
        L_0x0048:
            android.database.sqlite.SQLiteDatabase r0 = r3.mDb     // Catch:{ all -> 0x004e }
            r3.createTableIfNotExists(r0, r4)     // Catch:{ all -> 0x004e }
            goto L_0x006c
        L_0x004e:
            r4 = move-exception
            r0 = 0
            r3.mDb = r0     // Catch:{ all -> 0x006e }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x006e }
            r0.<init>()     // Catch:{ all -> 0x006e }
            java.lang.String r1 = "ensureDatabase failed, throwable = "
            r0.append(r1)     // Catch:{ all -> 0x006e }
            java.lang.String r4 = r4.getMessage()     // Catch:{ all -> 0x006e }
            r0.append(r4)     // Catch:{ all -> 0x006e }
            java.lang.String r4 = r0.toString()     // Catch:{ all -> 0x006e }
            java.lang.String r0 = "dc_storage"
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r0, (java.lang.String) r4)     // Catch:{ all -> 0x006e }
        L_0x006c:
            monitor-exit(r3)
            return
        L_0x006e:
            r4 = move-exception
            monitor-exit(r3)
            goto L_0x0072
        L_0x0071:
            throw r4
        L_0x0072:
            goto L_0x0071
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.db.DCSQLiteOpenHelper.ensureDatabase(java.lang.String):void");
    }

    public SQLiteDatabase getDatabase() {
        ensureDatabase((String) null);
        return this.mDb;
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public SQLiteDatabase getDatabase(String str) {
        ensureDatabase(str);
        return this.mDb;
    }

    private void createTableIfNotExists(SQLiteDatabase sQLiteDatabase, String str) {
        Cursor cursor = null;
        try {
            cursor = sQLiteDatabase.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '" + str + "'", (String[]) null);
            if (cursor == null || cursor.getCount() <= 0) {
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + str + " (" + "key" + " TEXT PRIMARY KEY," + "value" + " TEXT NOT NULL," + COLUMN_TIMESTAMP + " TEXT NOT NULL)");
                if (cursor == null) {
                    return;
                }
                cursor.close();
                return;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor == null) {
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }
}
