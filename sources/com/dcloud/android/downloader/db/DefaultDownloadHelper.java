package com.dcloud.android.downloader.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.dcloud.android.downloader.config.Config;
import io.dcloud.common.util.StringUtil;

public class DefaultDownloadHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String SQL_CREATE_DOWNLOAD_TABLE = StringUtil.format("CREATE TABLE %s (_id integer PRIMARY KEY NOT NULL,supportRanges integer NOT NULL,createAt long NOT NULL,uri varchar(255) NOT NULL,location varchar(255),path varchar(255) NOT NULL,size long NOT NULL, progress long NOT NULL,status integer NOT NULL, value1 varchar(255), value2 varchar(255));", TABLE_NAME_DOWNLOAD_INFO);
    private static final String SQL_CREATE_DOWNLOAD_THREAD_TABLE = StringUtil.format("CREATE TABLE %s (_id integer PRIMARY KEY NOT NULL,threadId integer NOT NULL,downloadInfoId integer NOT NULL,uri varchar(255) NOT NULL,start long NOT NULL,end long NOT NULL,progress long NOT NULL);", TABLE_NAME_DOWNLOAD_THREAD_INFO);
    public static final String TABLE_NAME_DOWNLOAD_INFO = "download_info";
    public static final String TABLE_NAME_DOWNLOAD_THREAD_INFO = "download_thread_info";

    public DefaultDownloadHelper(Context context, Config config) {
        super(context, config.getDatabaseName(), (SQLiteDatabase.CursorFactory) null, config.getDatabaseVersion());
    }

    private void createTable(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(SQL_CREATE_DOWNLOAD_TABLE);
        sQLiteDatabase.execSQL(SQL_CREATE_DOWNLOAD_THREAD_TABLE);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        createTable(sQLiteDatabase);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }
}
