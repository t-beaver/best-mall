package com.dmcbig.mediapicker.data;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import com.dmcbig.mediapicker.MediaPickerR;
import com.dmcbig.mediapicker.entity.Folder;
import com.dmcbig.mediapicker.entity.Media;
import java.util.ArrayList;

public class MediaLoader extends LoaderM implements LoaderManager.LoaderCallbacks {
    String[] MEDIA_PROJECTION = {"_data", "_display_name", "date_added", "media_type", "_size", "_id", "parent"};
    Context mContext;
    DataCallback mLoader;

    public MediaLoader(Context context, DataCallback dataCallback) {
        this.mContext = context;
        this.mLoader = dataCallback;
    }

    public Loader onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this.mContext, MediaStore.Files.getContentUri("external"), this.MEDIA_PROJECTION, "media_type=1 OR media_type=3", (String[]) null, "date_added DESC");
    }

    public void onLoadFinished(Loader loader, Object obj) {
        ArrayList arrayList = new ArrayList();
        Folder folder = new Folder(this.mContext.getResources().getString(MediaPickerR.MP_STRING_ALL_DIR_NAME));
        arrayList.add(folder);
        Folder folder2 = new Folder(this.mContext.getResources().getString(MediaPickerR.MP_STRING_VIDEO_DIR_NAME));
        arrayList.add(folder2);
        Cursor cursor = (Cursor) obj;
        while (cursor.moveToNext()) {
            String string = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
            String string2 = cursor.getString(cursor.getColumnIndexOrThrow("_display_name"));
            long j = cursor.getLong(cursor.getColumnIndexOrThrow("date_added"));
            int i = cursor.getInt(cursor.getColumnIndexOrThrow("media_type"));
            long j2 = cursor.getLong(cursor.getColumnIndexOrThrow("_size"));
            int i2 = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            if (j2 >= 1) {
                String parent = getParent(string);
                Cursor cursor2 = cursor;
                Media media = r6;
                String str = parent;
                Media media2 = new Media(string, string2, j, i, j2, i2, parent);
                folder.addMedias(media);
                if (i == 3) {
                    folder2.addMedias(media);
                }
                String str2 = str;
                int hasDir = hasDir(arrayList, str2);
                if (hasDir != -1) {
                    ((Folder) arrayList.get(hasDir)).addMedias(media);
                } else {
                    Folder folder3 = new Folder(str2);
                    folder3.addMedias(media);
                    arrayList.add(folder3);
                }
                cursor = cursor2;
            }
        }
        this.mLoader.onData(arrayList);
        cursor.close();
    }

    public void onLoaderReset(Loader loader) {
    }
}
