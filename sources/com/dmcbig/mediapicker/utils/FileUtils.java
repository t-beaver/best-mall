package com.dmcbig.mediapicker.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import com.facebook.common.util.UriUtil;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.utils.tools.TimeCalculator;
import io.dcloud.common.DHInterface.IApp;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Locale;

public class FileUtils {
    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final long MB = 1048576;

    public static File createTmpFile(Context context) throws IOException {
        File file;
        if (TextUtils.equals(Environment.getExternalStorageState(), "mounted")) {
            file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            if (!file.exists()) {
                file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/Camera");
                if (!file.exists()) {
                    file = getCacheDirectory(context, true);
                }
            }
        } else {
            file = getCacheDirectory(context, true);
        }
        return File.createTempFile(JPEG_FILE_PREFIX, JPEG_FILE_SUFFIX, file);
    }

    public static String fileSize(long j) {
        if (j <= 0) {
            return WXInstanceApm.VALUE_ERROR_CODE_DEFAULT;
        }
        double d = (double) j;
        int log10 = (int) (Math.log10(d) / Math.log10(1024.0d));
        StringBuilder sb = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.#");
        double pow = Math.pow(1024.0d, (double) log10);
        Double.isNaN(d);
        sb.append(decimalFormat.format(d / pow));
        sb.append(Operators.SPACE_STR);
        sb.append(new String[]{"B", "kB", "MB", "GB", "TB"}[log10]);
        return sb.toString();
    }

    public static File getCacheDirectory(Context context) {
        return getCacheDirectory(context, true);
    }

    private static File getExternalCacheDir(Context context) {
        File file = new File(new File(new File(new File(Environment.getExternalStorageDirectory(), TimeCalculator.PLATFORM_ANDROID), "data"), context.getPackageName()), IApp.ConfigProperty.CONFIG_CACHE);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return null;
            }
            try {
                new File(file, ".nomedia").createNewFile();
            } catch (IOException unused) {
            }
        }
        return file;
    }

    public static File getIndividualCacheDirectory(Context context, String str) {
        File cacheDirectory = getCacheDirectory(context);
        File file = new File(cacheDirectory, str);
        return (file.exists() || file.mkdir()) ? file : cacheDirectory;
    }

    public static String getMimeType(Context context, Uri uri) {
        if (UriUtil.LOCAL_CONTENT_SCHEME.equals(uri.getScheme())) {
            String extensionFromMimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(context.getContentResolver().getType(uri));
            if (TextUtils.isEmpty(extensionFromMimeType)) {
                return MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
            }
            return extensionFromMimeType;
        }
        String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        return TextUtils.isEmpty(fileExtensionFromUrl) ? MimeTypeMap.getSingleton().getExtensionFromMimeType(context.getContentResolver().getType(uri)) : fileExtensionFromUrl;
    }

    public static String getMimeTypeByFileName(String str) {
        return str.substring(str.lastIndexOf(Operators.DOT_STR), str.length());
    }

    public static String getRealPathFromURI(Context context, Uri uri) {
        Cursor query = context.getContentResolver().query(uri, (String[]) null, (String) null, (String[]) null, (String) null);
        if (query == null) {
            return uri.getPath();
        }
        query.moveToFirst();
        String string = query.getString(query.getColumnIndex("_data"));
        query.close();
        return string;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        return context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION) == 0;
    }

    public String getSizeByUnit(double d) {
        if (d == 0.0d) {
            return "0K";
        }
        if (d >= 1048576.0d) {
            return String.format(Locale.US, "%.1f", new Object[]{Double.valueOf(d / 1048576.0d)}) + "M";
        }
        return String.format(Locale.US, "%.1f", new Object[]{Double.valueOf(d / 1024.0d)}) + "K";
    }

    public static File getCacheDirectory(Context context, boolean z) {
        String str;
        try {
            str = Environment.getExternalStorageState();
        } catch (IncompatibleClassChangeError | NullPointerException unused) {
            str = "";
        }
        File externalCacheDir = (!z || !"mounted".equals(str) || !hasExternalStoragePermission(context)) ? null : getExternalCacheDir(context);
        if (externalCacheDir == null) {
            externalCacheDir = context.getCacheDir();
        }
        if (externalCacheDir != null) {
            return externalCacheDir;
        }
        return new File("/data/data/" + context.getPackageName() + "/cache/");
    }
}
