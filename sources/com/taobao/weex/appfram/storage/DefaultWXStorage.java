package com.taobao.weex.appfram.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteFullException;
import android.database.sqlite.SQLiteStatement;
import com.taobao.weex.appfram.storage.IWXStorageAdapter;
import com.taobao.weex.common.WXThread;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.ui.component.WXImage;
import com.taobao.weex.utils.WXLogUtils;
import io.dcloud.common.DHInterface.IApp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultWXStorage implements IWXStorageAdapter {
    /* access modifiers changed from: private */
    public WXSQLiteOpenHelper mDatabaseSupplier;
    private ExecutorService mExecutorService;

    private void execute(Runnable runnable) {
        if (this.mExecutorService == null) {
            this.mExecutorService = Executors.newSingleThreadExecutor();
        }
        if (runnable != null && !this.mExecutorService.isShutdown() && !this.mExecutorService.isTerminated()) {
            this.mExecutorService.execute(WXThread.secure(runnable));
        }
    }

    public DefaultWXStorage(Context context) {
        this.mDatabaseSupplier = new WXSQLiteOpenHelper(context);
    }

    public void setItem(final String str, final String str2, final IWXStorageAdapter.OnResultReceivedListener onResultReceivedListener) {
        execute(new Runnable() {
            public void run() {
                Map<String, Object> itemResult = StorageResultHandler.setItemResult(DefaultWXStorage.this.performSetItem(str, str2, false, true));
                IWXStorageAdapter.OnResultReceivedListener onResultReceivedListener = onResultReceivedListener;
                if (onResultReceivedListener != null) {
                    onResultReceivedListener.onReceived(itemResult);
                }
            }
        });
    }

    public void getItem(final String str, final IWXStorageAdapter.OnResultReceivedListener onResultReceivedListener) {
        execute(new Runnable() {
            public void run() {
                Map<String, Object> itemResult = StorageResultHandler.getItemResult(DefaultWXStorage.this.performGetItem(str));
                IWXStorageAdapter.OnResultReceivedListener onResultReceivedListener = onResultReceivedListener;
                if (onResultReceivedListener != null) {
                    onResultReceivedListener.onReceived(itemResult);
                }
            }
        });
    }

    public void removeItem(final String str, final IWXStorageAdapter.OnResultReceivedListener onResultReceivedListener) {
        execute(new Runnable() {
            public void run() {
                Map<String, Object> removeItemResult = StorageResultHandler.removeItemResult(DefaultWXStorage.this.performRemoveItem(str));
                IWXStorageAdapter.OnResultReceivedListener onResultReceivedListener = onResultReceivedListener;
                if (onResultReceivedListener != null) {
                    onResultReceivedListener.onReceived(removeItemResult);
                }
            }
        });
    }

    public void length(final IWXStorageAdapter.OnResultReceivedListener onResultReceivedListener) {
        execute(new Runnable() {
            public void run() {
                Map<String, Object> lengthResult = StorageResultHandler.getLengthResult(DefaultWXStorage.this.performGetLength());
                IWXStorageAdapter.OnResultReceivedListener onResultReceivedListener = onResultReceivedListener;
                if (onResultReceivedListener != null) {
                    onResultReceivedListener.onReceived(lengthResult);
                }
            }
        });
    }

    public void getAllKeys(final IWXStorageAdapter.OnResultReceivedListener onResultReceivedListener) {
        execute(new Runnable() {
            public void run() {
                Map<String, Object> allkeysResult = StorageResultHandler.getAllkeysResult(DefaultWXStorage.this.performGetAllKeys());
                IWXStorageAdapter.OnResultReceivedListener onResultReceivedListener = onResultReceivedListener;
                if (onResultReceivedListener != null) {
                    onResultReceivedListener.onReceived(allkeysResult);
                }
            }
        });
    }

    public void setItemPersistent(final String str, final String str2, final IWXStorageAdapter.OnResultReceivedListener onResultReceivedListener) {
        execute(new Runnable() {
            public void run() {
                Map<String, Object> itemResult = StorageResultHandler.setItemResult(DefaultWXStorage.this.performSetItem(str, str2, true, true));
                IWXStorageAdapter.OnResultReceivedListener onResultReceivedListener = onResultReceivedListener;
                if (onResultReceivedListener != null) {
                    onResultReceivedListener.onReceived(itemResult);
                }
            }
        });
    }

    public void close() {
        final ExecutorService executorService = this.mExecutorService;
        execute(new Runnable() {
            public void run() {
                try {
                    DefaultWXStorage.this.mDatabaseSupplier.closeDatabase();
                    ExecutorService executorService = executorService;
                    if (executorService != null) {
                        executorService.shutdown();
                    }
                } catch (Exception e) {
                    WXLogUtils.e("weex_storage", e.getMessage());
                }
            }
        });
        this.mExecutorService = null;
    }

    /* access modifiers changed from: private */
    public boolean performSetItem(String str, String str2, boolean z, boolean z2) {
        SQLiteDatabase database = this.mDatabaseSupplier.getDatabase();
        if (database == null) {
            return false;
        }
        WXLogUtils.d("weex_storage", "set k-v to storage(key:" + str + ",value:" + str2 + ",isPersistent:" + z + ",allowRetry:" + z2 + Operators.BRACKET_END_STR);
        SQLiteStatement sQLiteStatement = null;
        String format = WXSQLiteOpenHelper.sDateFormatter.format(new Date());
        try {
            SQLiteStatement compileStatement = database.compileStatement("INSERT OR REPLACE INTO default_wx_storage VALUES (?,?,?,?);");
            compileStatement.clearBindings();
            compileStatement.bindString(1, str);
            compileStatement.bindString(2, str2);
            compileStatement.bindString(3, format);
            compileStatement.bindLong(4, z ? 1 : 0);
            compileStatement.execute();
            if (compileStatement != null) {
                compileStatement.close();
            }
            return true;
        } catch (Exception e) {
            WXLogUtils.e("weex_storage", "DefaultWXStorage occurred an exception when execute setItem :" + e.getMessage());
            if (!(e instanceof SQLiteFullException) || !z2 || !trimToSize()) {
                if (sQLiteStatement != null) {
                    sQLiteStatement.close();
                }
                return false;
            }
            WXLogUtils.d("weex_storage", "retry set k-v to storage(key:" + str + ",value:" + str2 + Operators.BRACKET_END_STR);
            boolean performSetItem = performSetItem(str, str2, z, false);
            if (sQLiteStatement != null) {
                sQLiteStatement.close();
            }
            return performSetItem;
        } catch (Throwable th) {
            if (sQLiteStatement != null) {
                sQLiteStatement.close();
            }
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0079 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x007a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean trimToSize() {
        /*
            r13 = this;
            java.lang.String r0 = "weex_storage"
            com.taobao.weex.appfram.storage.WXSQLiteOpenHelper r1 = r13.mDatabaseSupplier
            android.database.sqlite.SQLiteDatabase r2 = r1.getDatabase()
            r1 = 0
            if (r2 != 0) goto L_0x000d
            return r1
        L_0x000d:
            java.util.ArrayList r10 = new java.util.ArrayList
            r10.<init>()
            java.lang.String r11 = "key"
            java.lang.String r12 = "persistent"
            java.lang.String[] r4 = new java.lang.String[]{r11, r12}
            r5 = 0
            r6 = 0
            r7 = 0
            r8 = 0
            java.lang.String r3 = "default_wx_storage"
            java.lang.String r9 = "timestamp ASC"
            android.database.Cursor r2 = r2.query(r3, r4, r5, r6, r7, r8, r9)
            r3 = 1
            int r4 = r2.getCount()     // Catch:{ Exception -> 0x005c }
            int r4 = r4 / 10
            r5 = 0
        L_0x002e:
            boolean r6 = r2.moveToNext()     // Catch:{ Exception -> 0x0058 }
            if (r6 == 0) goto L_0x0054
            int r6 = r2.getColumnIndex(r11)     // Catch:{ Exception -> 0x0058 }
            java.lang.String r6 = r2.getString(r6)     // Catch:{ Exception -> 0x0058 }
            int r7 = r2.getColumnIndex(r12)     // Catch:{ Exception -> 0x0058 }
            int r7 = r2.getInt(r7)     // Catch:{ Exception -> 0x0058 }
            if (r7 != r3) goto L_0x0048
            r7 = 1
            goto L_0x0049
        L_0x0048:
            r7 = 0
        L_0x0049:
            if (r7 != 0) goto L_0x002e
            if (r6 == 0) goto L_0x002e
            int r5 = r5 + 1
            r10.add(r6)     // Catch:{ Exception -> 0x0058 }
            if (r5 != r4) goto L_0x002e
        L_0x0054:
            r2.close()
            goto L_0x0077
        L_0x0058:
            r4 = move-exception
            goto L_0x005e
        L_0x005a:
            r0 = move-exception
            goto L_0x00a8
        L_0x005c:
            r4 = move-exception
            r5 = 0
        L_0x005e:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x005a }
            r6.<init>()     // Catch:{ all -> 0x005a }
            java.lang.String r7 = "DefaultWXStorage occurred an exception when execute trimToSize:"
            r6.append(r7)     // Catch:{ all -> 0x005a }
            java.lang.String r4 = r4.getMessage()     // Catch:{ all -> 0x005a }
            r6.append(r4)     // Catch:{ all -> 0x005a }
            java.lang.String r4 = r6.toString()     // Catch:{ all -> 0x005a }
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r0, (java.lang.String) r4)     // Catch:{ all -> 0x005a }
            goto L_0x0054
        L_0x0077:
            if (r5 > 0) goto L_0x007a
            return r1
        L_0x007a:
            java.util.Iterator r1 = r10.iterator()
        L_0x007e:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x008e
            java.lang.Object r2 = r1.next()
            java.lang.String r2 = (java.lang.String) r2
            r13.performRemoveItem(r2)
            goto L_0x007e
        L_0x008e:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "remove "
            r1.append(r2)
            r1.append(r5)
            java.lang.String r2 = " items by lru"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r0, (java.lang.String) r1)
            return r3
        L_0x00a8:
            r2.close()
            goto L_0x00ad
        L_0x00ac:
            throw r0
        L_0x00ad:
            goto L_0x00ac
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.appfram.storage.DefaultWXStorage.trimToSize():boolean");
    }

    /* access modifiers changed from: private */
    public String performGetItem(String str) {
        SQLiteDatabase database = this.mDatabaseSupplier.getDatabase();
        if (database == null) {
            return null;
        }
        Cursor query = database.query("default_wx_storage", new String[]{"value"}, "key=?", new String[]{str}, (String) null, (String) null, (String) null);
        try {
            if (query.moveToNext()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("timestamp", WXSQLiteOpenHelper.sDateFormatter.format(new Date()));
                int update = this.mDatabaseSupplier.getDatabase().update("default_wx_storage", contentValues, "key= ?", new String[]{str});
                StringBuilder sb = new StringBuilder();
                sb.append("update timestamp ");
                sb.append(update == 1 ? WXImage.SUCCEED : AbsoluteConst.EVENTS_FAILED);
                sb.append(" for operation [getItem(key = ");
                sb.append(str);
                sb.append(")]");
                WXLogUtils.d("weex_storage", sb.toString());
                return query.getString(query.getColumnIndex("value"));
            }
            query.close();
            return null;
        } catch (Exception e) {
            WXLogUtils.e("weex_storage", "DefaultWXStorage occurred an exception when execute getItem:" + e.getMessage());
            return null;
        } finally {
            query.close();
        }
    }

    /* access modifiers changed from: private */
    public boolean performRemoveItem(String str) {
        SQLiteDatabase database = this.mDatabaseSupplier.getDatabase();
        if (database == null) {
            return false;
        }
        try {
            if (database.delete("default_wx_storage", "key=?", new String[]{str}) == 1) {
                return true;
            }
            return false;
        } catch (Exception e) {
            WXLogUtils.e("weex_storage", "DefaultWXStorage occurred an exception when execute removeItem:" + e.getMessage());
            return false;
        }
    }

    /* access modifiers changed from: private */
    public long performGetLength() {
        SQLiteDatabase database = this.mDatabaseSupplier.getDatabase();
        if (database == null) {
            return 0;
        }
        SQLiteStatement sQLiteStatement = null;
        try {
            SQLiteStatement compileStatement = database.compileStatement("SELECT count(key) FROM default_wx_storage");
            long simpleQueryForLong = compileStatement.simpleQueryForLong();
            if (compileStatement != null) {
                compileStatement.close();
            }
            return simpleQueryForLong;
        } catch (Exception e) {
            WXLogUtils.e("weex_storage", "DefaultWXStorage occurred an exception when execute getLength:" + e.getMessage());
            if (sQLiteStatement != null) {
                sQLiteStatement.close();
            }
            return 0;
        } catch (Throwable th) {
            if (sQLiteStatement != null) {
                sQLiteStatement.close();
            }
            throw th;
        }
    }

    /* access modifiers changed from: private */
    public List<String> performGetAllKeys() {
        SQLiteDatabase database = this.mDatabaseSupplier.getDatabase();
        if (database == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        Cursor query = database.query("default_wx_storage", new String[]{IApp.ConfigProperty.CONFIG_KEY}, (String) null, (String[]) null, (String) null, (String) null, (String) null);
        while (query.moveToNext()) {
            try {
                arrayList.add(query.getString(query.getColumnIndex(IApp.ConfigProperty.CONFIG_KEY)));
            } catch (Exception e) {
                WXLogUtils.e("weex_storage", "DefaultWXStorage occurred an exception when execute getAllKeys:" + e.getMessage());
                return arrayList;
            } finally {
                query.close();
            }
        }
        return arrayList;
    }
}
