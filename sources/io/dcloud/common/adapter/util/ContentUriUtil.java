package io.dcloud.common.adapter.util;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import com.facebook.common.util.UriUtil;

public class ContentUriUtil {
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0033  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getDataColumn(android.content.Context r8, android.net.Uri r9, java.lang.String r10, java.lang.String[] r11) {
        /*
            java.lang.String r0 = "_data"
            java.lang.String[] r3 = new java.lang.String[]{r0}
            r7 = 0
            android.content.ContentResolver r1 = r8.getContentResolver()     // Catch:{ all -> 0x0030 }
            r6 = 0
            r2 = r9
            r4 = r10
            r5 = r11
            android.database.Cursor r8 = r1.query(r2, r3, r4, r5, r6)     // Catch:{ all -> 0x0030 }
            if (r8 == 0) goto L_0x002a
            boolean r9 = r8.moveToFirst()     // Catch:{ all -> 0x0027 }
            if (r9 == 0) goto L_0x002a
            int r9 = r8.getColumnIndexOrThrow(r0)     // Catch:{ all -> 0x0027 }
            java.lang.String r9 = r8.getString(r9)     // Catch:{ all -> 0x0027 }
            r8.close()
            return r9
        L_0x0027:
            r9 = move-exception
            r7 = r8
            goto L_0x0031
        L_0x002a:
            if (r8 == 0) goto L_0x002f
            r8.close()
        L_0x002f:
            return r7
        L_0x0030:
            r9 = move-exception
        L_0x0031:
            if (r7 == 0) goto L_0x0036
            r7.close()
        L_0x0036:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.util.ContentUriUtil.getDataColumn(android.content.Context, android.net.Uri, java.lang.String, java.lang.String[]):java.lang.String");
    }

    public static String getImageAbsolutePath(Context context, Uri uri) {
        Uri uri2 = null;
        if (!(context == null || uri == null)) {
            if (Build.VERSION.SDK_INT < 19 || !DocumentsContract.isDocumentUri(context, uri)) {
                if (UriUtil.LOCAL_CONTENT_SCHEME.equalsIgnoreCase(uri.getScheme())) {
                    if (isGooglePhotosUri(uri)) {
                        return uri.getLastPathSegment();
                    }
                    return getDataColumn(context, uri, (String) null, (String[]) null);
                } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                    return uri.getPath();
                }
            } else if (isMediaDocument(uri)) {
                String[] split = DocumentsContract.getDocumentId(uri).split(":");
                String str = split[0];
                if ("image".equals(str)) {
                    uri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(str)) {
                    uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(str)) {
                    uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                return getDataColumn(context, uri2, "_id=?", new String[]{split[1]});
            } else if (isExternalStorageDocument(uri)) {
                String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
                if ("primary".equalsIgnoreCase(split2[0])) {
                    return Environment.getExternalStorageDirectory() + "/" + split2[1];
                }
            } else if (isDownloadsDocument(uri)) {
                return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), (String) null, (String[]) null);
            }
        }
        return null;
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
