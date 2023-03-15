package io.dcloud.common.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import androidx.core.content.FileProvider;
import com.facebook.common.util.UriUtil;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.util.DeviceInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;

public class FileUtil {
    public static boolean checkFilePathLegalization(Context context, String str) {
        if (checkPrivatePath(context, str)) {
            return true;
        }
        if (!needMediaStoreOpenFile(context) || TextUtils.isEmpty(getPathForPublicType(str))) {
            return false;
        }
        return true;
    }

    public static boolean checkPathAccord(Context context, String... strArr) {
        boolean z = true;
        if (needMediaStoreOpenFile(context)) {
            if (Build.VERSION.SDK_INT > 29 && Environment.isExternalStorageManager()) {
                return true;
            }
            for (String checkPrivatePath : strArr) {
                if (!checkPrivatePath(context, checkPrivatePath)) {
                    z = false;
                }
            }
        }
        return z;
    }

    public static boolean checkPrivatePath(Context context, String str) {
        if (TextUtils.isEmpty(DeviceInfo.sPrivateDir)) {
            DeviceInfo.initAppDir(context);
        }
        int i = Build.VERSION.SDK_INT;
        if (i > 29 && Environment.isExternalStorageManager()) {
            return true;
        }
        if (str.startsWith(DeviceInfo.FILE_PROTOCOL)) {
            str = str.replace(DeviceInfo.FILE_PROTOCOL, "");
        }
        if (DeviceInfo.sPrivateExternalDir.startsWith("/") && !str.startsWith("/")) {
            str = "/" + str;
        }
        if (str.contains(DeviceInfo.sPrivateDir) || str.contains(DeviceInfo.sPrivateExternalDir) || isAssetFile(str) || i < 29) {
            return true;
        }
        return false;
    }

    private static Uri copyMediaFile(Context context, InputStream inputStream, String str, String str2, String str3, String str4) throws Exception {
        String fileTypeForSuffix = getFileTypeForSuffix(str2);
        if (TextUtils.isEmpty(fileTypeForSuffix)) {
            return null;
        }
        Uri contentUriForSuffix = getContentUriForSuffix(str2);
        File file = new File(DeviceInfo.sPublicDCIMDir + "/" + str4 + str);
        StringBuilder sb = new StringBuilder();
        sb.append(Operators.DOT_STR);
        sb.append(str2);
        String sb2 = sb.toString();
        String replace = str.replace(sb2, "");
        ContentResolver contentResolver = context.getContentResolver();
        int i = 1;
        while (file.exists()) {
            str = replace + Operators.BRACKET_START_STR + i + Operators.BRACKET_END_STR + sb2;
            i++;
            file = new File(DeviceInfo.sPublicDCIMDir + "/" + str4 + str);
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("_display_name", str);
        contentValues.put("mime_type", fileTypeForSuffix);
        contentValues.put(AbsoluteConst.JSON_KEY_TITLE, str);
        contentValues.put("relative_path", str3 + str4);
        Uri insert = contentResolver.insert(contentUriForSuffix, contentValues);
        if (insert == null) {
            return null;
        }
        OutputStream openOutputStream = context.getContentResolver().openOutputStream(insert);
        byte[] bArr = new byte[DHFile.BUF_SIZE];
        if (openOutputStream != null) {
            while (true) {
                int read = inputStream.read(bArr);
                if (read <= 0) {
                    break;
                }
                openOutputStream.write(bArr, 0, read);
                openOutputStream.flush();
            }
            openOutputStream.close();
        }
        inputStream.close();
        return insert;
    }

    public static Uri copyMediaFileToDCIM(Context context, String str) {
        InputStream inputStream;
        String str2;
        String str3;
        if (needMediaStoreOpenFile(context)) {
            try {
                File file = new File(str);
                if (!file.exists()) {
                    return null;
                }
                if (checkPrivatePath(context, str)) {
                    inputStream = new FileInputStream(file);
                } else {
                    inputStream = getFileInputStream(context, file);
                }
                InputStream inputStream2 = inputStream;
                if (inputStream2 == null) {
                    return null;
                }
                String fileNameForPath = getFileNameForPath(str);
                String fileNameWithSuffix = getFileNameWithSuffix(fileNameForPath);
                if (TextUtils.isEmpty(fileNameWithSuffix)) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    if (checkPrivatePath(context, str)) {
                        BitmapFactory.decodeFile(str, options);
                    } else {
                        InputStream fileInputStream = getFileInputStream(context, file);
                        BitmapFactory.decodeStream(fileInputStream, (Rect) null, options);
                        fileInputStream.close();
                    }
                    String str4 = options.outMimeType;
                    if (PdrUtil.isEmpty(str4) || !str4.contains("/")) {
                        str3 = "jpg";
                    } else {
                        String[] split = str4.split("/");
                        str3 = split[split.length - 1];
                    }
                    str2 = str3;
                } else {
                    str2 = fileNameWithSuffix;
                }
                return copyMediaFile(context, inputStream2, fileNameForPath, str2, "DCIM/", "Camera/");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Uri createPublicDocumentsFile(Context context, String str, String str2) {
        if (!needMediaStoreOpenFile(context)) {
            return null;
        }
        Uri contentUri = MediaStore.Files.getContentUri("external");
        File file = new File(DeviceInfo.sPublicDocumentsDir + "/" + str);
        if (file.exists()) {
            return getFileUri(context, file, contentUri);
        }
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put("_display_name", str);
        contentValues.put("mime_type", str2);
        contentValues.put(AbsoluteConst.JSON_KEY_TITLE, str);
        contentValues.put("relative_path", "Documents/");
        try {
            return contentResolver.insert(contentUri, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void deleteFile(Context context, String str) {
        if (context != null) {
            try {
                if (!TextUtils.isEmpty(str)) {
                    File fileStreamPath = context.getFileStreamPath(str);
                    if (fileStreamPath.exists()) {
                        fileStreamPath.delete();
                    }
                }
            } catch (Exception unused) {
            }
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.net.Uri getContentUriForSuffix(java.lang.String r1) {
        /*
            java.lang.String r1 = r1.toUpperCase()
            r1.hashCode()
            int r0 = r1.hashCode()
            switch(r0) {
                case 64547: goto L_0x0159;
                case 64934: goto L_0x014e;
                case 65108: goto L_0x0143;
                case 65204: goto L_0x0138;
                case 65893: goto L_0x012d;
                case 70564: goto L_0x0122;
                case 73665: goto L_0x0117;
                case 75674: goto L_0x010c;
                case 75695: goto L_0x00fe;
                case 76387: goto L_0x00f0;
                case 76408: goto L_0x00e2;
                case 76528: goto L_0x00d4;
                case 76529: goto L_0x00c6;
                case 78191: goto L_0x00b8;
                case 79369: goto L_0x00aa;
                case 85708: goto L_0x009c;
                case 86059: goto L_0x008e;
                case 86080: goto L_0x0080;
                case 1590132: goto L_0x0072;
                case 2160488: goto L_0x0064;
                case 2283624: goto L_0x0056;
                case 2657710: goto L_0x0048;
                case 2660249: goto L_0x003a;
                case 2660252: goto L_0x002c;
                case 49294142: goto L_0x001e;
                case 73545134: goto L_0x0010;
                default: goto L_0x000e;
            }
        L_0x000e:
            goto L_0x0164
        L_0x0010:
            java.lang.String r0 = "MP2TS"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x001a
            goto L_0x0164
        L_0x001a:
            r1 = 25
            goto L_0x0165
        L_0x001e:
            java.lang.String r0 = "3GPP2"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0028
            goto L_0x0164
        L_0x0028:
            r1 = 24
            goto L_0x0165
        L_0x002c:
            java.lang.String r0 = "WEBP"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0036
            goto L_0x0164
        L_0x0036:
            r1 = 23
            goto L_0x0165
        L_0x003a:
            java.lang.String r0 = "WEBM"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0044
            goto L_0x0164
        L_0x0044:
            r1 = 22
            goto L_0x0165
        L_0x0048:
            java.lang.String r0 = "WBMP"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0052
            goto L_0x0164
        L_0x0052:
            r1 = 21
            goto L_0x0165
        L_0x0056:
            java.lang.String r0 = "JPEG"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0060
            goto L_0x0164
        L_0x0060:
            r1 = 20
            goto L_0x0165
        L_0x0064:
            java.lang.String r0 = "FLAC"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x006e
            goto L_0x0164
        L_0x006e:
            r1 = 19
            goto L_0x0165
        L_0x0072:
            java.lang.String r0 = "3GPP"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x007c
            goto L_0x0164
        L_0x007c:
            r1 = 18
            goto L_0x0165
        L_0x0080:
            java.lang.String r0 = "WMV"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x008a
            goto L_0x0164
        L_0x008a:
            r1 = 17
            goto L_0x0165
        L_0x008e:
            java.lang.String r0 = "WMA"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0098
            goto L_0x0164
        L_0x0098:
            r1 = 16
            goto L_0x0165
        L_0x009c:
            java.lang.String r0 = "WAV"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x00a6
            goto L_0x0164
        L_0x00a6:
            r1 = 15
            goto L_0x0165
        L_0x00aa:
            java.lang.String r0 = "PNG"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x00b4
            goto L_0x0164
        L_0x00b4:
            r1 = 14
            goto L_0x0165
        L_0x00b8:
            java.lang.String r0 = "OGG"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x00c2
            goto L_0x0164
        L_0x00c2:
            r1 = 13
            goto L_0x0165
        L_0x00c6:
            java.lang.String r0 = "MP4"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x00d0
            goto L_0x0164
        L_0x00d0:
            r1 = 12
            goto L_0x0165
        L_0x00d4:
            java.lang.String r0 = "MP3"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x00de
            goto L_0x0164
        L_0x00de:
            r1 = 11
            goto L_0x0165
        L_0x00e2:
            java.lang.String r0 = "MKV"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x00ec
            goto L_0x0164
        L_0x00ec:
            r1 = 10
            goto L_0x0165
        L_0x00f0:
            java.lang.String r0 = "MKA"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x00fa
            goto L_0x0164
        L_0x00fa:
            r1 = 9
            goto L_0x0165
        L_0x00fe:
            java.lang.String r0 = "M4V"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0108
            goto L_0x0164
        L_0x0108:
            r1 = 8
            goto L_0x0165
        L_0x010c:
            java.lang.String r0 = "M4A"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0115
            goto L_0x0164
        L_0x0115:
            r1 = 7
            goto L_0x0165
        L_0x0117:
            java.lang.String r0 = "JPG"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0120
            goto L_0x0164
        L_0x0120:
            r1 = 6
            goto L_0x0165
        L_0x0122:
            java.lang.String r0 = "GIF"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x012b
            goto L_0x0164
        L_0x012b:
            r1 = 5
            goto L_0x0165
        L_0x012d:
            java.lang.String r0 = "BMP"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0136
            goto L_0x0164
        L_0x0136:
            r1 = 4
            goto L_0x0165
        L_0x0138:
            java.lang.String r0 = "AVI"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0141
            goto L_0x0164
        L_0x0141:
            r1 = 3
            goto L_0x0165
        L_0x0143:
            java.lang.String r0 = "ASF"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x014c
            goto L_0x0164
        L_0x014c:
            r1 = 2
            goto L_0x0165
        L_0x014e:
            java.lang.String r0 = "AMR"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0157
            goto L_0x0164
        L_0x0157:
            r1 = 1
            goto L_0x0165
        L_0x0159:
            java.lang.String r0 = "AAC"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0162
            goto L_0x0164
        L_0x0162:
            r1 = 0
            goto L_0x0165
        L_0x0164:
            r1 = -1
        L_0x0165:
            switch(r1) {
                case 0: goto L_0x0175;
                case 1: goto L_0x0175;
                case 2: goto L_0x0172;
                case 3: goto L_0x0172;
                case 4: goto L_0x016f;
                case 5: goto L_0x016f;
                case 6: goto L_0x016f;
                case 7: goto L_0x0175;
                case 8: goto L_0x0172;
                case 9: goto L_0x0175;
                case 10: goto L_0x0172;
                case 11: goto L_0x0175;
                case 12: goto L_0x0172;
                case 13: goto L_0x0175;
                case 14: goto L_0x016f;
                case 15: goto L_0x0175;
                case 16: goto L_0x0175;
                case 17: goto L_0x0172;
                case 18: goto L_0x0172;
                case 19: goto L_0x0175;
                case 20: goto L_0x016f;
                case 21: goto L_0x016f;
                case 22: goto L_0x0172;
                case 23: goto L_0x016f;
                case 24: goto L_0x0172;
                case 25: goto L_0x0172;
                default: goto L_0x0168;
            }
        L_0x0168:
            java.lang.String r1 = "external"
            android.net.Uri r1 = android.provider.MediaStore.Files.getContentUri(r1)
            return r1
        L_0x016f:
            android.net.Uri r1 = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            return r1
        L_0x0172:
            android.net.Uri r1 = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            return r1
        L_0x0175:
            android.net.Uri r1 = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.FileUtil.getContentUriForSuffix(java.lang.String):android.net.Uri");
    }

    public static InputStream getFileInputStream(Context context, String str) {
        if (str.startsWith(DeviceInfo.FILE_PROTOCOL)) {
            str = str.replace(DeviceInfo.FILE_PROTOCOL, "");
        }
        return getFileInputStream(context, new File(str));
    }

    public static String getFileNameForPath(String str) {
        int lastIndexOf;
        if (!TextUtils.isEmpty(str) && (lastIndexOf = str.lastIndexOf("/")) != -1) {
            return str.substring(lastIndexOf + 1);
        }
        return null;
    }

    public static String getFileNameWithSuffix(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        int lastIndexOf = str.lastIndexOf(Operators.DOT_STR);
        if (lastIndexOf == -1) {
            return null;
        }
        return str.substring(lastIndexOf + 1);
    }

    public static String getFileProviderUriToPath(Context context, Uri uri) {
        try {
            Method declaredMethod = FileProvider.class.getDeclaredMethod("getPathStrategy", new Class[]{Context.class, String.class});
            declaredMethod.setAccessible(true);
            Object invoke = declaredMethod.invoke((Object) null, new Object[]{context, uri.getAuthority()});
            if (invoke != null) {
                Method declaredMethod2 = Class.forName(FileProvider.class.getName() + "$PathStrategy").getDeclaredMethod("getFileForUri", new Class[]{Uri.class});
                declaredMethod2.setAccessible(true);
                Object invoke2 = declaredMethod2.invoke(invoke, new Object[]{uri});
                if (invoke2 instanceof File) {
                    return ((File) invoke2).getAbsolutePath();
                }
            }
        } catch (Exception unused) {
        }
        return null;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getFileTypeForSuffix(java.lang.String r1) {
        /*
            java.lang.String r1 = r1.toUpperCase()
            r1.hashCode()
            int r0 = r1.hashCode()
            switch(r0) {
                case 64547: goto L_0x0159;
                case 64934: goto L_0x014e;
                case 65108: goto L_0x0143;
                case 65204: goto L_0x0138;
                case 65893: goto L_0x012d;
                case 70564: goto L_0x0122;
                case 73665: goto L_0x0117;
                case 75674: goto L_0x010c;
                case 75695: goto L_0x00fe;
                case 76387: goto L_0x00f0;
                case 76408: goto L_0x00e2;
                case 76528: goto L_0x00d4;
                case 76529: goto L_0x00c6;
                case 78191: goto L_0x00b8;
                case 79369: goto L_0x00aa;
                case 85708: goto L_0x009c;
                case 86059: goto L_0x008e;
                case 86080: goto L_0x0080;
                case 1590132: goto L_0x0072;
                case 2160488: goto L_0x0064;
                case 2283624: goto L_0x0056;
                case 2657710: goto L_0x0048;
                case 2660249: goto L_0x003a;
                case 2660252: goto L_0x002c;
                case 49294142: goto L_0x001e;
                case 73545134: goto L_0x0010;
                default: goto L_0x000e;
            }
        L_0x000e:
            goto L_0x0164
        L_0x0010:
            java.lang.String r0 = "MP2TS"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x001a
            goto L_0x0164
        L_0x001a:
            r1 = 25
            goto L_0x0165
        L_0x001e:
            java.lang.String r0 = "3GPP2"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0028
            goto L_0x0164
        L_0x0028:
            r1 = 24
            goto L_0x0165
        L_0x002c:
            java.lang.String r0 = "WEBP"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0036
            goto L_0x0164
        L_0x0036:
            r1 = 23
            goto L_0x0165
        L_0x003a:
            java.lang.String r0 = "WEBM"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0044
            goto L_0x0164
        L_0x0044:
            r1 = 22
            goto L_0x0165
        L_0x0048:
            java.lang.String r0 = "WBMP"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0052
            goto L_0x0164
        L_0x0052:
            r1 = 21
            goto L_0x0165
        L_0x0056:
            java.lang.String r0 = "JPEG"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0060
            goto L_0x0164
        L_0x0060:
            r1 = 20
            goto L_0x0165
        L_0x0064:
            java.lang.String r0 = "FLAC"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x006e
            goto L_0x0164
        L_0x006e:
            r1 = 19
            goto L_0x0165
        L_0x0072:
            java.lang.String r0 = "3GPP"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x007c
            goto L_0x0164
        L_0x007c:
            r1 = 18
            goto L_0x0165
        L_0x0080:
            java.lang.String r0 = "WMV"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x008a
            goto L_0x0164
        L_0x008a:
            r1 = 17
            goto L_0x0165
        L_0x008e:
            java.lang.String r0 = "WMA"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0098
            goto L_0x0164
        L_0x0098:
            r1 = 16
            goto L_0x0165
        L_0x009c:
            java.lang.String r0 = "WAV"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x00a6
            goto L_0x0164
        L_0x00a6:
            r1 = 15
            goto L_0x0165
        L_0x00aa:
            java.lang.String r0 = "PNG"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x00b4
            goto L_0x0164
        L_0x00b4:
            r1 = 14
            goto L_0x0165
        L_0x00b8:
            java.lang.String r0 = "OGG"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x00c2
            goto L_0x0164
        L_0x00c2:
            r1 = 13
            goto L_0x0165
        L_0x00c6:
            java.lang.String r0 = "MP4"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x00d0
            goto L_0x0164
        L_0x00d0:
            r1 = 12
            goto L_0x0165
        L_0x00d4:
            java.lang.String r0 = "MP3"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x00de
            goto L_0x0164
        L_0x00de:
            r1 = 11
            goto L_0x0165
        L_0x00e2:
            java.lang.String r0 = "MKV"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x00ec
            goto L_0x0164
        L_0x00ec:
            r1 = 10
            goto L_0x0165
        L_0x00f0:
            java.lang.String r0 = "MKA"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x00fa
            goto L_0x0164
        L_0x00fa:
            r1 = 9
            goto L_0x0165
        L_0x00fe:
            java.lang.String r0 = "M4V"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0108
            goto L_0x0164
        L_0x0108:
            r1 = 8
            goto L_0x0165
        L_0x010c:
            java.lang.String r0 = "M4A"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0115
            goto L_0x0164
        L_0x0115:
            r1 = 7
            goto L_0x0165
        L_0x0117:
            java.lang.String r0 = "JPG"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0120
            goto L_0x0164
        L_0x0120:
            r1 = 6
            goto L_0x0165
        L_0x0122:
            java.lang.String r0 = "GIF"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x012b
            goto L_0x0164
        L_0x012b:
            r1 = 5
            goto L_0x0165
        L_0x012d:
            java.lang.String r0 = "BMP"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0136
            goto L_0x0164
        L_0x0136:
            r1 = 4
            goto L_0x0165
        L_0x0138:
            java.lang.String r0 = "AVI"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0141
            goto L_0x0164
        L_0x0141:
            r1 = 3
            goto L_0x0165
        L_0x0143:
            java.lang.String r0 = "ASF"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x014c
            goto L_0x0164
        L_0x014c:
            r1 = 2
            goto L_0x0165
        L_0x014e:
            java.lang.String r0 = "AMR"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0157
            goto L_0x0164
        L_0x0157:
            r1 = 1
            goto L_0x0165
        L_0x0159:
            java.lang.String r0 = "AAC"
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x0162
            goto L_0x0164
        L_0x0162:
            r1 = 0
            goto L_0x0165
        L_0x0164:
            r1 = -1
        L_0x0165:
            switch(r1) {
                case 0: goto L_0x0171;
                case 1: goto L_0x0171;
                case 2: goto L_0x016d;
                case 3: goto L_0x016d;
                case 4: goto L_0x016a;
                case 5: goto L_0x016a;
                case 6: goto L_0x016a;
                case 7: goto L_0x0171;
                case 8: goto L_0x016d;
                case 9: goto L_0x0171;
                case 10: goto L_0x016d;
                case 11: goto L_0x0171;
                case 12: goto L_0x016d;
                case 13: goto L_0x0171;
                case 14: goto L_0x016a;
                case 15: goto L_0x0171;
                case 16: goto L_0x0171;
                case 17: goto L_0x016d;
                case 18: goto L_0x016d;
                case 19: goto L_0x0171;
                case 20: goto L_0x016a;
                case 21: goto L_0x016a;
                case 22: goto L_0x016d;
                case 23: goto L_0x016a;
                case 24: goto L_0x016d;
                case 25: goto L_0x016d;
                default: goto L_0x0168;
            }
        L_0x0168:
            r1 = 0
            return r1
        L_0x016a:
            java.lang.String r1 = "image/*"
            return r1
        L_0x016d:
            java.lang.String r1 = "video/*"
            return r1
        L_0x0171:
            java.lang.String r1 = "audio/*"
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.FileUtil.getFileTypeForSuffix(java.lang.String):java.lang.String");
    }

    public static Uri getFileUri(Context context, File file, Uri uri) {
        Uri uri2 = uri;
        Cursor query = context.getContentResolver().query(uri2, new String[]{"_id"}, "_data=? ", new String[]{file.getAbsolutePath()}, (String) null);
        if (query != null && query.moveToFirst()) {
            int i = query.getInt(query.getColumnIndex("_id"));
            query.close();
            return Uri.withAppendedPath(uri, "" + i);
        } else if (query == null) {
            return null;
        } else {
            query.close();
            return null;
        }
    }

    public static InputStream getImageFileStream(Context context, File file) {
        Uri imageFileUri = getImageFileUri(context, file.getPath());
        if (imageFileUri == null) {
            return null;
        }
        try {
            return context.getContentResolver().openInputStream(imageFileUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Uri getImageFileUri(Context context, String str) {
        return getImageFileUri(context, new File(str));
    }

    public static String getPathForPublicType(String str) {
        if (str.contains(DeviceInfo.sPublicDCIMDir)) {
            return Environment.DIRECTORY_DCIM;
        }
        if (str.contains(DeviceInfo.sPublicDownloadDir)) {
            return Environment.DIRECTORY_DOWNLOADS;
        }
        if (str.contains(DeviceInfo.sPublicMoviesDir)) {
            return Environment.DIRECTORY_MOVIES;
        }
        if (str.contains(DeviceInfo.sPublicMusicDir)) {
            return Environment.DIRECTORY_MUSIC;
        }
        if (str.contains(DeviceInfo.sPublicPicturesDir)) {
            return Environment.DIRECTORY_PICTURES;
        }
        if (str.contains(DeviceInfo.sPublicDocumentsDir)) {
            return Environment.DIRECTORY_DOCUMENTS;
        }
        if (str.contains(DeviceInfo.sPublicRingtonesDir)) {
            return Environment.DIRECTORY_RINGTONES;
        }
        return null;
    }

    public static String getPathFromUri(Context context, Uri uri) {
        Uri uri2;
        if (uri == null) {
            return null;
        }
        if (!(Build.VERSION.SDK_INT >= 19) || !DocumentsContract.isDocumentUri(context, uri)) {
            String scheme = uri.getScheme();
            if (UriUtil.LOCAL_CONTENT_SCHEME.equals(scheme)) {
                return queryAbsolutePath(context, uri);
            }
            if ("file".equals(scheme)) {
                return uri.getPath();
            }
            return null;
        }
        String authority = uri.getAuthority();
        if ("com.android.externalstorage.documents".equals(authority)) {
            String[] split = DocumentsContract.getDocumentId(uri).split(":");
            String str = split[0];
            if ("primary".equals(str)) {
                return Environment.getExternalStorageDirectory().getAbsolutePath().concat("/").concat(split[1]);
            }
            return "/storage/".concat(str).concat("/").concat(split[1]);
        } else if ("com.android.providers.downloads.documents".equals(authority)) {
            String documentId = DocumentsContract.getDocumentId(uri);
            if (documentId.startsWith("raw:")) {
                return documentId.replaceFirst("raw:", "");
            }
            return queryAbsolutePath(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(documentId)));
        } else {
            if ("com.android.providers.media.documents".equals(authority)) {
                String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
                String str2 = split2[0];
                if ("image".equals(str2)) {
                    uri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(str2)) {
                    uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(str2)) {
                    uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                return queryAbsolutePath(context, ContentUris.withAppendedId(uri2, Long.parseLong(split2[1])));
            }
            return null;
        }
    }

    public static DCFileUriData getShareImageUri(Context context, File file, String str, Intent intent) {
        DCFileUriData dCFileUriData = new DCFileUriData();
        String path = file.getPath();
        dCFileUriData.filePath = path;
        if (!isExternalPublicDir(path) && Build.VERSION.SDK_INT >= 29 && getPathForPublicType(path) == null) {
            String str2 = context.getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath() + "/" + str.hashCode() + Operators.DOT_STR + getFileNameWithSuffix(str);
            File file2 = new File(str2);
            File parentFile = file2.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            dCFileUriData.fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".dc.fileprovider", file2);
            dCFileUriData.isReplace = true;
            dCFileUriData.fileReplacePath = str2;
            if (intent != null) {
                intent.addFlags(1);
            }
        } else if (isExternalPublicDir(path)) {
            File parentFile2 = file.getParentFile();
            if (!parentFile2.exists()) {
                parentFile2.mkdirs();
            }
            dCFileUriData.fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".dc.fileprovider", file);
        } else {
            dCFileUriData.fileUri = Uri.fromFile(file);
        }
        return dCFileUriData;
    }

    public static Uri getVideoFileUri(Context context, String str) {
        File file = new File(str);
        if (checkPrivatePath(context, str)) {
            return Uri.fromFile(file);
        }
        return getFileUri(context, file, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
    }

    public static boolean isAssetFile(String str) {
        return str.startsWith("apps/") || str.startsWith("/android_asset/") || str.startsWith("android_asset/");
    }

    private static boolean isExternalPublicDir(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(DeviceInfo.sPrivateExternalDir);
        sb.append("/file/");
        return str.contains(sb.toString()) || str.contains(DeviceInfo.sBaseFsRootPath);
    }

    public static boolean isFilePathForPublic(Context context, String str) {
        if (PdrUtil.isEmpty(str)) {
            return false;
        }
        if (str.startsWith(DeviceInfo.FILE_PROTOCOL)) {
            str = str.replace(DeviceInfo.FILE_PROTOCOL, "");
        }
        File file = new File(str);
        if (!file.exists()) {
            return false;
        }
        if (!needMediaStoreOpenFile(context)) {
            return true;
        }
        String fileNameWithSuffix = getFileNameWithSuffix(str);
        if (PdrUtil.isEmpty(fileNameWithSuffix) || getFileUri(context, file, getContentUriForSuffix(fileNameWithSuffix)) == null) {
            return false;
        }
        return true;
    }

    public static boolean needMediaStoreOpenFile(Context context) {
        int i = Build.VERSION.SDK_INT;
        return (i >= 29 && context.getApplicationInfo().targetSdkVersion >= 29) || i >= 30;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x002c, code lost:
        if (r8 != null) goto L_0x0038;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0036, code lost:
        if (r8 != null) goto L_0x0038;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0038, code lost:
        r8.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003b, code lost:
        return null;
     */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0040  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String queryAbsolutePath(android.content.Context r8, android.net.Uri r9) {
        /*
            java.lang.String r0 = "_data"
            java.lang.String[] r3 = new java.lang.String[]{r0}
            r7 = 0
            android.content.ContentResolver r1 = r8.getContentResolver()     // Catch:{ Exception -> 0x0031, all -> 0x002f }
            r4 = 0
            r5 = 0
            r6 = 0
            r2 = r9
            android.database.Cursor r8 = r1.query(r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x0031, all -> 0x002f }
            if (r8 == 0) goto L_0x002c
            boolean r9 = r8.moveToFirst()     // Catch:{ Exception -> 0x002a }
            if (r9 == 0) goto L_0x002c
            int r9 = r8.getColumnIndexOrThrow(r0)     // Catch:{ Exception -> 0x002a }
            java.lang.String r9 = r8.getString(r9)     // Catch:{ Exception -> 0x002a }
            r8.close()     // Catch:{ Exception -> 0x002a }
            r8.close()
            return r9
        L_0x002a:
            r9 = move-exception
            goto L_0x0033
        L_0x002c:
            if (r8 == 0) goto L_0x003b
            goto L_0x0038
        L_0x002f:
            r9 = move-exception
            goto L_0x003e
        L_0x0031:
            r9 = move-exception
            r8 = r7
        L_0x0033:
            r9.printStackTrace()     // Catch:{ all -> 0x003c }
            if (r8 == 0) goto L_0x003b
        L_0x0038:
            r8.close()
        L_0x003b:
            return r7
        L_0x003c:
            r9 = move-exception
            r7 = r8
        L_0x003e:
            if (r7 == 0) goto L_0x0043
            r7.close()
        L_0x0043:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.FileUtil.queryAbsolutePath(android.content.Context, android.net.Uri):java.lang.String");
    }

    public static Object readData4Disk(Context context, String str) {
        ObjectInputStream objectInputStream;
        ObjectInputStream objectInputStream2 = null;
        if (context != null && !TextUtils.isEmpty(str)) {
            try {
                objectInputStream = new ObjectInputStream(new FileInputStream(context.getFileStreamPath(str)));
                try {
                    Object readObject = objectInputStream.readObject();
                    objectInputStream.close();
                    IOUtil.close((InputStream) objectInputStream);
                    return readObject;
                } catch (Exception unused) {
                    IOUtil.close((InputStream) objectInputStream);
                    return null;
                } catch (Throwable th) {
                    th = th;
                    objectInputStream2 = objectInputStream;
                    IOUtil.close((InputStream) objectInputStream2);
                    throw th;
                }
            } catch (Exception unused2) {
                objectInputStream = null;
                IOUtil.close((InputStream) objectInputStream);
                return null;
            } catch (Throwable th2) {
                th = th2;
                IOUtil.close((InputStream) objectInputStream2);
                throw th;
            }
        }
        return null;
    }

    public static void saveData2Disk(Context context, Object obj, String str) {
        if (context != null && obj != null && (obj instanceof Serializable) && !TextUtils.isEmpty(str)) {
            ObjectOutputStream objectOutputStream = null;
            try {
                File fileStreamPath = context.getFileStreamPath(str);
                if (!fileStreamPath.exists()) {
                    fileStreamPath.createNewFile();
                }
                ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(new FileOutputStream(fileStreamPath));
                try {
                    objectOutputStream2.writeObject(obj);
                    objectOutputStream2.close();
                    IOUtil.close((OutputStream) objectOutputStream2);
                } catch (IOException unused) {
                    objectOutputStream = objectOutputStream2;
                    IOUtil.close((OutputStream) objectOutputStream);
                } catch (Throwable th) {
                    th = th;
                    objectOutputStream = objectOutputStream2;
                    IOUtil.close((OutputStream) objectOutputStream);
                    throw th;
                }
            } catch (IOException unused2) {
                IOUtil.close((OutputStream) objectOutputStream);
            } catch (Throwable th2) {
                th = th2;
                IOUtil.close((OutputStream) objectOutputStream);
                throw th;
            }
        }
    }

    public static void writeStream2File(InputStream inputStream, File file) {
        FileOutputStream fileOutputStream = null;
        if (inputStream != null) {
            try {
                FileOutputStream fileOutputStream2 = new FileOutputStream(file);
                try {
                    byte[] bArr = new byte[1024];
                    while (true) {
                        int read = inputStream.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        fileOutputStream2.write(bArr, 0, read);
                    }
                    fileOutputStream2.close();
                    inputStream.close();
                    fileOutputStream = fileOutputStream2;
                } catch (Exception e) {
                    e = e;
                    fileOutputStream = fileOutputStream2;
                    try {
                        e.printStackTrace();
                        IOUtil.close(inputStream);
                        IOUtil.close((OutputStream) fileOutputStream);
                    } catch (Throwable th) {
                        th = th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    fileOutputStream = fileOutputStream2;
                    IOUtil.close(inputStream);
                    IOUtil.close((OutputStream) fileOutputStream);
                    throw th;
                }
            } catch (Exception e2) {
                e = e2;
                e.printStackTrace();
                IOUtil.close(inputStream);
                IOUtil.close((OutputStream) fileOutputStream);
            }
        }
        IOUtil.close(inputStream);
        IOUtil.close((OutputStream) fileOutputStream);
    }

    public static Uri getImageFileUri(Context context, File file) {
        if (checkPrivatePath(context, file.getPath())) {
            return Uri.fromFile(file);
        }
        return getFileUri(context, file, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.io.InputStream getFileInputStream(android.content.Context r4, java.io.File r5) {
        /*
            r0 = 0
            if (r5 == 0) goto L_0x013d
            java.lang.String r1 = r5.getPath()
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 == 0) goto L_0x000f
            goto L_0x013d
        L_0x000f:
            java.lang.String r1 = r5.getPath()     // Catch:{ FileNotFoundException -> 0x0139 }
            boolean r2 = checkPrivatePath(r4, r1)     // Catch:{ FileNotFoundException -> 0x0139 }
            if (r2 == 0) goto L_0x001f
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x0139 }
            r4.<init>(r5)     // Catch:{ FileNotFoundException -> 0x0139 }
            return r4
        L_0x001f:
            java.lang.String r2 = getFileNameWithSuffix(r1)     // Catch:{ FileNotFoundException -> 0x0139 }
            boolean r3 = needMediaStoreOpenFile(r4)     // Catch:{ FileNotFoundException -> 0x0139 }
            if (r3 == 0) goto L_0x0133
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch:{ FileNotFoundException -> 0x0139 }
            if (r3 != 0) goto L_0x0042
            android.net.Uri r1 = getContentUriForSuffix(r2)     // Catch:{ FileNotFoundException -> 0x0139 }
            android.net.Uri r5 = getFileUri(r4, r5, r1)     // Catch:{ FileNotFoundException -> 0x0139 }
            if (r5 == 0) goto L_0x013d
            android.content.ContentResolver r4 = r4.getContentResolver()     // Catch:{ FileNotFoundException -> 0x0139 }
            java.io.InputStream r4 = r4.openInputStream(r5)     // Catch:{ FileNotFoundException -> 0x0139 }
            return r4
        L_0x0042:
            java.lang.String r1 = getPathForPublicType(r1)     // Catch:{ FileNotFoundException -> 0x0139 }
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ FileNotFoundException -> 0x0139 }
            if (r2 != 0) goto L_0x013d
            r2 = -1
            int r3 = r1.hashCode()     // Catch:{ FileNotFoundException -> 0x0139 }
            switch(r3) {
                case -1984392349: goto L_0x0091;
                case -1970382607: goto L_0x0087;
                case -1347456360: goto L_0x007d;
                case -665475243: goto L_0x0073;
                case 2092515: goto L_0x0069;
                case 74710533: goto L_0x005f;
                case 1492462760: goto L_0x0055;
                default: goto L_0x0054;
            }     // Catch:{ FileNotFoundException -> 0x0139 }
        L_0x0054:
            goto L_0x009a
        L_0x0055:
            java.lang.String r3 = "Download"
            boolean r1 = r1.equals(r3)     // Catch:{ FileNotFoundException -> 0x0139 }
            if (r1 == 0) goto L_0x009a
            r2 = 1
            goto L_0x009a
        L_0x005f:
            java.lang.String r3 = "Music"
            boolean r1 = r1.equals(r3)     // Catch:{ FileNotFoundException -> 0x0139 }
            if (r1 == 0) goto L_0x009a
            r2 = 3
            goto L_0x009a
        L_0x0069:
            java.lang.String r3 = "DCIM"
            boolean r1 = r1.equals(r3)     // Catch:{ FileNotFoundException -> 0x0139 }
            if (r1 == 0) goto L_0x009a
            r2 = 0
            goto L_0x009a
        L_0x0073:
            java.lang.String r3 = "Pictures"
            boolean r1 = r1.equals(r3)     // Catch:{ FileNotFoundException -> 0x0139 }
            if (r1 == 0) goto L_0x009a
            r2 = 4
            goto L_0x009a
        L_0x007d:
            java.lang.String r3 = "Documents"
            boolean r1 = r1.equals(r3)     // Catch:{ FileNotFoundException -> 0x0139 }
            if (r1 == 0) goto L_0x009a
            r2 = 5
            goto L_0x009a
        L_0x0087:
            java.lang.String r3 = "Ringtones"
            boolean r1 = r1.equals(r3)     // Catch:{ FileNotFoundException -> 0x0139 }
            if (r1 == 0) goto L_0x009a
            r2 = 6
            goto L_0x009a
        L_0x0091:
            java.lang.String r3 = "Movies"
            boolean r1 = r1.equals(r3)     // Catch:{ FileNotFoundException -> 0x0139 }
            if (r1 == 0) goto L_0x009a
            r2 = 2
        L_0x009a:
            java.lang.String r1 = "external"
            switch(r2) {
                case 0: goto L_0x010f;
                case 1: goto L_0x00f8;
                case 2: goto L_0x00e7;
                case 3: goto L_0x00d6;
                case 4: goto L_0x00c5;
                case 5: goto L_0x00b2;
                case 6: goto L_0x00a1;
                default: goto L_0x009f;
            }
        L_0x009f:
            goto L_0x0120
        L_0x00a1:
            android.net.Uri r1 = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI     // Catch:{ FileNotFoundException -> 0x0139 }
            android.net.Uri r5 = getFileUri(r4, r5, r1)     // Catch:{ FileNotFoundException -> 0x0139 }
            if (r5 == 0) goto L_0x013d
            android.content.ContentResolver r4 = r4.getContentResolver()     // Catch:{ FileNotFoundException -> 0x0139 }
            java.io.InputStream r4 = r4.openInputStream(r5)     // Catch:{ FileNotFoundException -> 0x0139 }
            return r4
        L_0x00b2:
            android.net.Uri r1 = android.provider.MediaStore.Files.getContentUri(r1)     // Catch:{ FileNotFoundException -> 0x0139 }
            android.net.Uri r5 = getFileUri(r4, r5, r1)     // Catch:{ FileNotFoundException -> 0x0139 }
            if (r5 == 0) goto L_0x013d
            android.content.ContentResolver r4 = r4.getContentResolver()     // Catch:{ FileNotFoundException -> 0x0139 }
            java.io.InputStream r4 = r4.openInputStream(r5)     // Catch:{ FileNotFoundException -> 0x0139 }
            return r4
        L_0x00c5:
            android.net.Uri r1 = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI     // Catch:{ FileNotFoundException -> 0x0139 }
            android.net.Uri r5 = getFileUri(r4, r5, r1)     // Catch:{ FileNotFoundException -> 0x0139 }
            if (r5 == 0) goto L_0x013d
            android.content.ContentResolver r4 = r4.getContentResolver()     // Catch:{ FileNotFoundException -> 0x0139 }
            java.io.InputStream r4 = r4.openInputStream(r5)     // Catch:{ FileNotFoundException -> 0x0139 }
            return r4
        L_0x00d6:
            android.net.Uri r1 = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI     // Catch:{ FileNotFoundException -> 0x0139 }
            android.net.Uri r5 = getFileUri(r4, r5, r1)     // Catch:{ FileNotFoundException -> 0x0139 }
            if (r5 == 0) goto L_0x013d
            android.content.ContentResolver r4 = r4.getContentResolver()     // Catch:{ FileNotFoundException -> 0x0139 }
            java.io.InputStream r4 = r4.openInputStream(r5)     // Catch:{ FileNotFoundException -> 0x0139 }
            return r4
        L_0x00e7:
            android.net.Uri r1 = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI     // Catch:{ FileNotFoundException -> 0x0139 }
            android.net.Uri r5 = getFileUri(r4, r5, r1)     // Catch:{ FileNotFoundException -> 0x0139 }
            if (r5 == 0) goto L_0x013d
            android.content.ContentResolver r4 = r4.getContentResolver()     // Catch:{ FileNotFoundException -> 0x0139 }
            java.io.InputStream r4 = r4.openInputStream(r5)     // Catch:{ FileNotFoundException -> 0x0139 }
            return r4
        L_0x00f8:
            int r1 = android.os.Build.VERSION.SDK_INT     // Catch:{ FileNotFoundException -> 0x0139 }
            r2 = 29
            if (r1 < r2) goto L_0x013d
            android.net.Uri r1 = android.provider.MediaStore.Downloads.EXTERNAL_CONTENT_URI     // Catch:{ FileNotFoundException -> 0x0139 }
            android.net.Uri r5 = getFileUri(r4, r5, r1)     // Catch:{ FileNotFoundException -> 0x0139 }
            if (r5 == 0) goto L_0x013d
            android.content.ContentResolver r4 = r4.getContentResolver()     // Catch:{ FileNotFoundException -> 0x0139 }
            java.io.InputStream r4 = r4.openInputStream(r5)     // Catch:{ FileNotFoundException -> 0x0139 }
            return r4
        L_0x010f:
            android.net.Uri r1 = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI     // Catch:{ FileNotFoundException -> 0x0139 }
            android.net.Uri r5 = getFileUri(r4, r5, r1)     // Catch:{ FileNotFoundException -> 0x0139 }
            if (r5 == 0) goto L_0x013d
            android.content.ContentResolver r4 = r4.getContentResolver()     // Catch:{ FileNotFoundException -> 0x0139 }
            java.io.InputStream r4 = r4.openInputStream(r5)     // Catch:{ FileNotFoundException -> 0x0139 }
            return r4
        L_0x0120:
            android.net.Uri r1 = android.provider.MediaStore.Files.getContentUri(r1)     // Catch:{ FileNotFoundException -> 0x0139 }
            android.net.Uri r5 = getFileUri(r4, r5, r1)     // Catch:{ FileNotFoundException -> 0x0139 }
            if (r5 == 0) goto L_0x013d
            android.content.ContentResolver r4 = r4.getContentResolver()     // Catch:{ FileNotFoundException -> 0x0139 }
            java.io.InputStream r4 = r4.openInputStream(r5)     // Catch:{ FileNotFoundException -> 0x0139 }
            return r4
        L_0x0133:
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x0139 }
            r4.<init>(r5)     // Catch:{ FileNotFoundException -> 0x0139 }
            return r4
        L_0x0139:
            r4 = move-exception
            r4.printStackTrace()
        L_0x013d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.FileUtil.getFileInputStream(android.content.Context, java.io.File):java.io.InputStream");
    }

    public static Uri copyMediaFileToDCIM(Context context, InputStream inputStream, String str) {
        if (!needMediaStoreOpenFile(context) || inputStream == null) {
            return null;
        }
        try {
            String fileNameWithSuffix = getFileNameWithSuffix(str);
            if (TextUtils.isEmpty(getFileTypeForSuffix(fileNameWithSuffix))) {
                return null;
            }
            return copyMediaFile(context, inputStream, str, fileNameWithSuffix, "DCIM/", "Camera/");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
