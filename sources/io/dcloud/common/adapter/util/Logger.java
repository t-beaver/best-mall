package io.dcloud.common.adapter.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.ThreadPool;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Logger {
    public static final String ANIMATION_TAG = "Animation_Path";
    public static final String AUTO_POP_PUSH_TAG = "Auto_Pop_Push_Path";
    public static final String Android_System_TAG = "Android_System_Path";
    public static final String AppMgr_TAG = "appmgr";
    public static final String AutoGC_TAG = "AutoGC_Path";
    public static final String Capture_TAG = "Capture_Tag";
    protected static String D = "D";
    protected static String E = "E";
    private static final String EXCEPTION_TAG = "DCloud_Exception";
    public static final String Event_TAG = "Event_Tag";
    protected static String I = "I";
    public static final String LAYOUT_TAG = "Layout_Path";
    private static final String LOGTAG = "DCloud_LOG";
    private static String LogPath = "";
    public static final String MAIN_TAG = "Main_Path";
    public static final String MAP_TAG = "Map_Path";
    /* access modifiers changed from: private */
    public static int MAX_CRASH_FILE_COUNT = 3;
    private static final String MSC_TAG = "DCloud_";
    private static final String STREAMSDKLOGTAG = "DCLOUD_STREAMSDK_LOG";
    public static final String StreamApp_TAG = "stream_manager";
    private static long TIMES = 432000000;
    public static final String TIMESTAMP_HH_MM_SS_SSS = "HH:mm:ss.SSS";
    public static final String TIMESTAMP_YYYY_MM_DD = "yyyyMMdd";
    public static final String TIMESTAMP_YYYY_MM_DD_HH_MM_SS_SSS = "yyyyMMdd HH:mm:ss.SSS";
    public static final String VIEW_VISIBLE_TAG = "View_Visible_Path";
    protected static String W = "W";
    private static boolean isHasDevFile = false;
    private static boolean isOpen = true;
    private static boolean isStoreLog = false;
    /* access modifiers changed from: private */
    public static File mLogFile = null;
    private static HashMap<String, SimpleDateFormat> mSimpleDateFormatCache = null;
    private static HandlerThread mWriteLogToSdCardThread = null;
    private static WriteLogToSdCardThreadHandler mWriteLogToSdCardThreadHandler = null;
    static String pkg = "";

    static class WriteLogToSdCardThreadHandler extends Handler {
        public WriteLogToSdCardThreadHandler(Looper looper) {
            super(looper);
        }

        /* JADX WARNING: Removed duplicated region for block: B:28:0x0058 A[SYNTHETIC, Splitter:B:28:0x0058] */
        /* JADX WARNING: Removed duplicated region for block: B:33:0x0063 A[SYNTHETIC, Splitter:B:33:0x0063] */
        /* JADX WARNING: Removed duplicated region for block: B:42:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(android.os.Message r5) {
            /*
                r4 = this;
                super.handleMessage(r5)
                java.lang.Object r5 = r5.obj
                if (r5 == 0) goto L_0x006c
                boolean r0 = r5 instanceof java.lang.String[]
                if (r0 != 0) goto L_0x000c
                goto L_0x006c
            L_0x000c:
                java.lang.String[] r5 = (java.lang.String[]) r5
                int r0 = r5.length
                r1 = 3
                if (r0 >= r1) goto L_0x0013
                return
            L_0x0013:
                r0 = 0
                r0 = r5[r0]
                r1 = 1
                r2 = r5[r1]
                r3 = 2
                r5 = r5[r3]
                java.lang.String r5 = io.dcloud.common.adapter.util.Logger.generateLog(r0, r2, r5)
                java.io.File r0 = io.dcloud.common.adapter.util.Logger.mLogFile
                if (r0 == 0) goto L_0x006c
                java.io.File r0 = io.dcloud.common.adapter.util.Logger.mLogFile
                boolean r0 = r0.exists()
                if (r0 == 0) goto L_0x006c
                if (r5 == 0) goto L_0x006c
                r0 = 0
                java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0052 }
                java.io.File r3 = io.dcloud.common.adapter.util.Logger.mLogFile     // Catch:{ Exception -> 0x0052 }
                r2.<init>(r3, r1)     // Catch:{ Exception -> 0x0052 }
                byte[] r5 = r5.getBytes()     // Catch:{ Exception -> 0x004d, all -> 0x004a }
                r2.write(r5)     // Catch:{ Exception -> 0x004d, all -> 0x004a }
                r2.flush()     // Catch:{ Exception -> 0x004d, all -> 0x004a }
                r2.close()     // Catch:{ IOException -> 0x005c }
                goto L_0x006c
            L_0x004a:
                r5 = move-exception
                r0 = r2
                goto L_0x0061
            L_0x004d:
                r5 = move-exception
                r0 = r2
                goto L_0x0053
            L_0x0050:
                r5 = move-exception
                goto L_0x0061
            L_0x0052:
                r5 = move-exception
            L_0x0053:
                r5.printStackTrace()     // Catch:{ all -> 0x0050 }
                if (r0 == 0) goto L_0x006c
                r0.close()     // Catch:{ IOException -> 0x005c }
                goto L_0x006c
            L_0x005c:
                r5 = move-exception
                r5.printStackTrace()
                goto L_0x006c
            L_0x0061:
                if (r0 == 0) goto L_0x006b
                r0.close()     // Catch:{ IOException -> 0x0067 }
                goto L_0x006b
            L_0x0067:
                r0 = move-exception
                r0.printStackTrace()
            L_0x006b:
                throw r5
            L_0x006c:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.util.Logger.WriteLogToSdCardThreadHandler.handleMessage(android.os.Message):void");
        }
    }

    private static void WriteExceptionToSDcard(String str, String str2, String str3, Throwable th) {
        if (th != null) {
            WriteLogToSDcard(str, str2, generateLog(str, EXCEPTION_TAG, th.getClass().getName()));
            StackTraceElement[] stackTrace = th.getStackTrace();
            if (stackTrace != null) {
                WriteLogToSDcard(str, str2, str3);
                for (StackTraceElement stackTraceElement : stackTrace) {
                    WriteLogToSDcard(str, str2, generateLog(str, EXCEPTION_TAG, "    at " + stackTraceElement.toString()));
                }
            }
        }
    }

    private static void WriteLogToSDcard(String str, String str2, String str3) {
        File file;
        if (isOpen && (file = mLogFile) != null && file.exists()) {
            initWriteLogToSdCardThread();
            Message obtain = Message.obtain();
            obtain.obj = new String[]{str, str2, str3};
            if (!PdrUtil.isEmpty(mWriteLogToSdCardThreadHandler)) {
                mWriteLogToSdCardThreadHandler.sendMessage(obtain);
            }
        }
    }

    public static boolean canStoreLogToSDcard() {
        if (isSDcardExists() && isOpen) {
            File file = new File(LogPath);
            if (file.exists()) {
                deleteOldLog(file);
            }
            if (file.exists() && file.canWrite()) {
                isStoreLog = true;
            }
        }
        return isStoreLog;
    }

    private static String concatString(String str, String str2) {
        if (str == null || str2 == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer(str.length() + str2.length());
        stringBuffer.append(str);
        stringBuffer.append(str2);
        return stringBuffer.toString();
    }

    public static void d(String str, String str2) {
        if (isOpen) {
            if (!BaseInfo.ISDEBUG) {
                Log.i(str, str2);
            } else {
                Log.d(str, str2);
            }
            WriteLogToSDcard(D, str, str2);
        }
    }

    protected static void deleteOldLog(File file) {
        File[] listFiles = file.listFiles();
        Date date = new Date();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                if (!file2.isDirectory()) {
                    try {
                        if (date.getTime() - new SimpleDateFormat(TIMESTAMP_YYYY_MM_DD).parse(file2.getName().substring(0, 8)).getTime() > TIMES) {
                            file2.delete();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void e(String str, String str2) {
        if (isOpen) {
            Log.e(str, str2);
            WriteLogToSDcard(E, str, str2);
        }
    }

    public static void es(String str, String str2) {
        if (isOpen) {
            Log.e(str, str2);
            WriteLogToSDcard(E, str, str2);
        }
    }

    protected static String generateLog(String str, String str2, String str3) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(generateTimeStamp(Boolean.TRUE));
        stringBuffer.append(Operators.SPACE_STR);
        stringBuffer.append(str);
        stringBuffer.append(Operators.SPACE_STR);
        stringBuffer.append(Operators.SUB);
        stringBuffer.append(Operators.SPACE_STR);
        stringBuffer.append(str2);
        stringBuffer.append(Operators.SPACE_STR);
        stringBuffer.append(str3);
        stringBuffer.append("\n");
        return stringBuffer.toString();
    }

    public static String generateTimeStamp(Boolean bool) {
        return generateTimeStamp(bool.booleanValue() ? TIMESTAMP_HH_MM_SS_SSS : TIMESTAMP_YYYY_MM_DD, new Date());
    }

    private static SimpleDateFormat getDateFormatFromCache(String str) {
        if (mSimpleDateFormatCache == null) {
            mSimpleDateFormatCache = new HashMap<>();
        }
        if (mSimpleDateFormatCache.containsKey(str)) {
            return mSimpleDateFormatCache.get(str);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str, Locale.ENGLISH);
        mSimpleDateFormatCache.put(str, simpleDateFormat);
        return simpleDateFormat;
    }

    public static void i(String str, String str2) {
        if (isOpen && str2 != null) {
            Log.i(str, str2);
            WriteLogToSDcard(I, str, str2);
        }
    }

    private static void init(final String str, final String str2) {
        try {
            ThreadPool.self().addThreadTask(new Runnable() {
                public void run() {
                    File[] listFiles = new File(str).listFiles();
                    if (listFiles != null && listFiles.length > Logger.MAX_CRASH_FILE_COUNT) {
                        if (listFiles[0].getName().compareTo(listFiles[1].getName()) < 0) {
                            for (int i = 0; i < listFiles.length - Logger.MAX_CRASH_FILE_COUNT; i++) {
                                if (!TextUtils.equals(str2, listFiles[i].getName())) {
                                    listFiles[i].delete();
                                }
                            }
                            return;
                        }
                        for (int length = listFiles.length - 1; length >= Logger.MAX_CRASH_FILE_COUNT; length--) {
                            if (!TextUtils.equals(str2, listFiles[length].getName())) {
                                listFiles[length].delete();
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initLogger(Context context) {
        pkg = context.getPackageName();
        isHasDevFile = DHFile.hasFile();
        boolean z = BaseInfo.isBase(context) || isHasDevFile;
        isOpen = z;
        isOpen = z | BaseInfo.ISDEBUG;
        if ("mounted".equalsIgnoreCase(Environment.getExternalStorageState())) {
            LogPath = BaseInfo.getCrashLogsPath(context);
        }
        if (isOpen) {
            init(LogPath, IApp.ConfigProperty.CONFIG_CRASH);
            init(LogPath + "crash/", (String) null);
        }
        setOpen(isOpen);
    }

    private static void initWriteLogToSdCardThread() {
        if (mWriteLogToSdCardThread == null) {
            HandlerThread handlerThread = new HandlerThread("WriteLogToSdCardThread");
            mWriteLogToSdCardThread = handlerThread;
            handlerThread.start();
            mWriteLogToSdCardThreadHandler = new WriteLogToSdCardThreadHandler(mWriteLogToSdCardThread.getLooper());
        }
    }

    public static boolean isOpen() {
        return isOpen;
    }

    protected static boolean isSDcardExists() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static boolean isTurnOn() {
        return isStoreLog;
    }

    public static void p(String str, String str2) {
        privacyLog(str, str2);
    }

    private static void privacyLog(String str, String str2) {
        if (isHasDevFile) {
            Log.e(str, str2);
        }
    }

    public static void setOpen(boolean z) {
        isOpen = z;
        if (z) {
            canStoreLogToSDcard();
            storeLogToSDcard();
        }
    }

    public static void stopWriteLogToSdCardThread() {
        if (mWriteLogToSdCardThread != null) {
            WriteLogToSdCardThreadHandler writeLogToSdCardThreadHandler = mWriteLogToSdCardThreadHandler;
            if (writeLogToSdCardThreadHandler != null) {
                writeLogToSdCardThreadHandler.removeCallbacksAndMessages((Object) null);
            }
            mWriteLogToSdCardThreadHandler = null;
            if (Build.VERSION.SDK_INT >= 18) {
                mWriteLogToSdCardThread.quitSafely();
            } else {
                mWriteLogToSdCardThread.quit();
            }
            mWriteLogToSdCardThread = null;
        }
    }

    public static void storeLogToSDcard() {
        if (isStoreLog) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(LogPath);
            stringBuffer.append(generateTimeStamp(Boolean.FALSE) + ".log");
            File file = new File(stringBuffer.toString());
            mLogFile = file;
            if (!file.exists()) {
                try {
                    mLogFile.createNewFile();
                } catch (IOException e) {
                    mLogFile = null;
                    e.printStackTrace();
                }
            }
            WriteLogToSDcard("日志路径:", "Logger", "path=" + stringBuffer.toString());
        }
    }

    public static void turnOff() {
        isStoreLog = false;
    }

    public static void w(String str, Throwable th) {
        if (isOpen) {
            if (th != null) {
                th.printStackTrace();
            }
            Log.w(EXCEPTION_TAG, str, th);
            WriteExceptionToSDcard(W, EXCEPTION_TAG, str, th);
        }
    }

    public static void p(String str) {
        privacyLog(LOGTAG, str);
    }

    public static void e(String str) {
        e(LOGTAG, str);
    }

    public static void es(String str) {
        if (isOpen) {
            es(STREAMSDKLOGTAG, str);
        }
    }

    public static void i(String str) {
        i(LOGTAG, str);
    }

    public static void w(Throwable th) {
        w("", th);
    }

    public static void d(String str, Object... objArr) {
        if (isOpen) {
            StringBuffer stringBuffer = new StringBuffer();
            if (objArr != null) {
                for (Object append : objArr) {
                    stringBuffer.append(append);
                    stringBuffer.append(";");
                }
            }
            if (!BaseInfo.ISDEBUG) {
                Log.i(str, stringBuffer.toString());
            } else {
                Log.d(str, stringBuffer.toString());
            }
            WriteLogToSDcard(D, str, stringBuffer.toString());
        }
    }

    public static String generateTimeStamp(String str, Date date) {
        SimpleDateFormat dateFormatFromCache = getDateFormatFromCache(str);
        dateFormatFromCache.applyPattern(str);
        return dateFormatFromCache.format(date);
    }

    public static void d(String str) {
        d(LOGTAG, str);
    }
}
