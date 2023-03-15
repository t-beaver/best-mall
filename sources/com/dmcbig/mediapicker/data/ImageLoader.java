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

public class ImageLoader extends LoaderM implements LoaderManager.LoaderCallbacks {
    String[] IMAGE_PROJECTION = {"_data", "_display_name", "date_added", "mime_type", "_size", "_id"};
    Context mContext;
    DataCallback mLoader;

    public ImageLoader(Context context, DataCallback dataCallback) {
        this.mContext = context;
        this.mLoader = dataCallback;
    }

    public Loader onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this.mContext, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, this.IMAGE_PROJECTION, (String) null, (String[]) null, "date_added DESC");
    }

    public void onLoadFinished(Loader loader, Object obj) {
        ArrayList arrayList = new ArrayList();
        Folder folder = new Folder(this.mContext.getResources().getString(MediaPickerR.MP_STRING_ALL_IMAGE));
        arrayList.add(folder);
        Cursor cursor = (Cursor) obj;
        while (cursor.moveToNext()) {
            String string = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
            String string2 = cursor.getString(cursor.getColumnIndexOrThrow("_display_name"));
            long j = cursor.getLong(cursor.getColumnIndexOrThrow("date_added"));
            int i = cursor.getInt(cursor.getColumnIndexOrThrow("mime_type"));
            long j2 = cursor.getLong(cursor.getColumnIndexOrThrow("_size"));
            int i2 = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            if (j2 >= 1) {
                String parent = getParent(string);
                Media media = new Media(string, string2, j, i, j2, i2, parent);
                folder.addMedias(media);
                int hasDir = hasDir(arrayList, parent);
                if (hasDir != -1) {
                    ((Folder) arrayList.get(hasDir)).addMedias(media);
                } else {
                    Folder folder2 = new Folder(parent);
                    folder2.addMedias(media);
                    arrayList.add(folder2);
                }
            }
        }
        this.mLoader.onData(arrayList);
        cursor.close();
    }

    public void onLoaderReset(Loader loader) {
    }
}
