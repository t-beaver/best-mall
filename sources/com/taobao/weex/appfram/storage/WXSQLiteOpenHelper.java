package com.taobao.weex.appfram.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.taobao.weex.utils.WXLogUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WXSQLiteOpenHelper extends SQLiteOpenHelper {
    static final String COLUMN_KEY = "key";
    static final String COLUMN_PERSISTENT = "persistent";
    static final String COLUMN_TIMESTAMP = "timestamp";
    static final String COLUMN_VALUE = "value";
    private static final String DATABASE_NAME = "WXStorage";
    private static final int DATABASE_VERSION = 2;
    private static final int SLEEP_TIME_MS = 30;
    private static final String STATEMENT_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS default_wx_storage (key TEXT PRIMARY KEY,value TEXT NOT NULL,timestamp TEXT NOT NULL,persistent INTEGER DEFAULT 0)";
    static final String TABLE_STORAGE = "default_wx_storage";
    static final String TAG_STORAGE = "weex_storage";
    static SimpleDateFormat sDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private Context mContext;
    private SQLiteDatabase mDb;
    private long mMaximumDatabaseSize = 52428800;

    public WXSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 2);
        this.mContext = context;
    }

    public SQLiteDatabase getDatabase() {
        ensureDatabase();
        return this.mDb;
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(STATEMENT_CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        if (i != i2) {
            if (i2 == 2) {
                boolean z = true;
                if (i == 1) {
                    WXLogUtils.d(TAG_STORAGE, "storage is updating from version " + i + " to version " + i2);
                    try {
                        long currentTimeMillis = System.currentTimeMillis();
                        sQLiteDatabase.beginTransaction();
                        WXLogUtils.d(TAG_STORAGE, "exec sql : " + "ALTER TABLE default_wx_storage ADD COLUMN timestamp TEXT;");
                        sQLiteDatabase.execSQL("ALTER TABLE default_wx_storage ADD COLUMN timestamp TEXT;");
                        WXLogUtils.d(TAG_STORAGE, "exec sql : " + "ALTER TABLE default_wx_storage ADD COLUMN persistent INTEGER;");
                        sQLiteDatabase.execSQL("ALTER TABLE default_wx_storage ADD COLUMN persistent INTEGER;");
                        String str = "UPDATE default_wx_storage SET timestamp = '" + sDateFormatter.format(new Date()) + "' , " + COLUMN_PERSISTENT + " = 0";
                        WXLogUtils.d(TAG_STORAGE, "exec sql : " + str);
                        sQLiteDatabase.execSQL(str);
                        sQLiteDatabase.setTransactionSuccessful();
                        WXLogUtils.d(TAG_STORAGE, "storage updated success (" + (System.currentTimeMillis() - currentTimeMillis) + "ms)");
                    } catch (Exception e) {
                        WXLogUtils.d(TAG_STORAGE, "storage updated failed from version " + i + " to version " + i2 + "," + e.getMessage());
                        z = false;
                    } catch (Throwable th) {
                        sQLiteDatabase.endTransaction();
                        throw th;
                    }
                    sQLiteDatabase.endTransaction();
                    if (!z) {
                        WXLogUtils.d(TAG_STORAGE, "storage is rollback,all data will be removed");
                        deleteDB();
                        onCreate(sQLiteDatabase);
                        return;
                    }
                    return;
                }
            }
            deleteDB();
            onCreate(sQLiteDatabase);
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(10:15|16|17|18|19|20|21|22|41|23) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0027 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void ensureDatabase() {
        /*
            r4 = this;
            monitor-enter(r4)
            android.database.sqlite.SQLiteDatabase r0 = r4.mDb     // Catch:{ all -> 0x0063 }
            if (r0 == 0) goto L_0x000d
            boolean r0 = r0.isOpen()     // Catch:{ all -> 0x0063 }
            if (r0 == 0) goto L_0x000d
            monitor-exit(r4)
            return
        L_0x000d:
            r0 = 0
        L_0x000e:
            r1 = 2
            if (r0 >= r1) goto L_0x0031
            if (r0 <= 0) goto L_0x0016
            r4.deleteDB()     // Catch:{ SQLiteException -> 0x001d }
        L_0x0016:
            android.database.sqlite.SQLiteDatabase r1 = r4.getWritableDatabase()     // Catch:{ SQLiteException -> 0x001d }
            r4.mDb = r1     // Catch:{ SQLiteException -> 0x001d }
            goto L_0x0031
        L_0x001d:
            r1 = move-exception
            r1.printStackTrace()     // Catch:{ all -> 0x0042 }
            r1 = 30
            java.lang.Thread.sleep(r1)     // Catch:{ InterruptedException -> 0x0027 }
            goto L_0x002e
        L_0x0027:
            java.lang.Thread r1 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x0042 }
            r1.interrupt()     // Catch:{ all -> 0x0042 }
        L_0x002e:
            int r0 = r0 + 1
            goto L_0x000e
        L_0x0031:
            android.database.sqlite.SQLiteDatabase r0 = r4.mDb     // Catch:{ all -> 0x0042 }
            if (r0 != 0) goto L_0x0037
            monitor-exit(r4)
            return
        L_0x0037:
            r4.createTableIfNotExists(r0)     // Catch:{ all -> 0x0042 }
            android.database.sqlite.SQLiteDatabase r0 = r4.mDb     // Catch:{ all -> 0x0042 }
            long r1 = r4.mMaximumDatabaseSize     // Catch:{ all -> 0x0042 }
            r0.setMaximumSize(r1)     // Catch:{ all -> 0x0042 }
            goto L_0x0061
        L_0x0042:
            r0 = move-exception
            r1 = 0
            r4.mDb = r1     // Catch:{ all -> 0x0063 }
            java.lang.String r1 = "weex_storage"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0063 }
            r2.<init>()     // Catch:{ all -> 0x0063 }
            java.lang.String r3 = "ensureDatabase failed, throwable = "
            r2.append(r3)     // Catch:{ all -> 0x0063 }
            java.lang.String r0 = r0.getMessage()     // Catch:{ all -> 0x0063 }
            r2.append(r0)     // Catch:{ all -> 0x0063 }
            java.lang.String r0 = r2.toString()     // Catch:{ all -> 0x0063 }
            com.taobao.weex.utils.WXLogUtils.d((java.lang.String) r1, (java.lang.String) r0)     // Catch:{ all -> 0x0063 }
        L_0x0061:
            monitor-exit(r4)
            return
        L_0x0063:
            r0 = move-exception
            monitor-exit(r4)
            goto L_0x0067
        L_0x0066:
            throw r0
        L_0x0067:
            goto L_0x0066
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.appfram.storage.WXSQLiteOpenHelper.ensureDatabase():void");
    }

    public synchronized void setMaximumSize(long j) {
        this.mMaximumDatabaseSize = j;
        SQLiteDatabase sQLiteDatabase = this.mDb;
        if (sQLiteDatabase != null) {
            sQLiteDatabase.setMaximumSize(j);
        }
    }

    private boolean deleteDB() {
        closeDatabase();
        return this.mContext.deleteDatabase(DATABASE_NAME);
    }

    public synchronized void closeDatabase() {
        SQLiteDatabase sQLiteDatabase = this.mDb;
        if (sQLiteDatabase != null && sQLiteDatabase.isOpen()) {
            this.mDb.close();
            this.mDb = null;
        }
    }

    private void createTableIfNotExists(SQLiteDatabase sQLiteDatabase) {
        Cursor cursor = null;
        try {
            cursor = sQLiteDatabase.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = 'default_wx_storage'", (String[]) null);
            if (cursor == null || cursor.getCount() <= 0) {
                sQLiteDatabase.execSQL(STATEMENT_CREATE_TABLE);
                if (cursor == null) {
                    return;
                }
                cursor.close();
            } else if (cursor != null) {
                cursor.close();
            }
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
