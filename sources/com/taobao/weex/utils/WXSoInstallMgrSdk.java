package com.taobao.weex.utils;

import android.app.Application;
import android.os.Build;
import android.text.TextUtils;
import com.taobao.weex.IWXStatisticsListener;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.adapter.IWXSoLoaderAdapter;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.performance.WXInstanceApm;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.util.Locale;
import java.util.Map;

public class WXSoInstallMgrSdk {
    private static final String ARMEABI = "armeabi";
    static final String LOGTAG = "INIT_SO";
    private static final String MIPS = "mips";
    private static final String STARTUPSO = "/libweexjsb.so";
    private static final String STARTUPSOANDROID15 = "/libweexjst.so";
    private static final String X86 = "x86";
    private static String mAbi;
    static Application mContext;
    private static IWXSoLoaderAdapter mSoLoader;
    private static IWXStatisticsListener mStatisticsListener;

    public static void init(Application application, IWXSoLoaderAdapter iWXSoLoaderAdapter, IWXStatisticsListener iWXStatisticsListener) {
        mContext = application;
        mSoLoader = iWXSoLoaderAdapter;
        mStatisticsListener = iWXStatisticsListener;
    }

    public static boolean isX86() {
        return _cpuType().equalsIgnoreCase("x86");
    }

    public static boolean isCPUSupport() {
        return !_cpuType().equalsIgnoreCase(MIPS);
    }

    public static boolean initSo(String str, int i, IWXUserTrackAdapter iWXUserTrackAdapter) {
        String _cpuType = _cpuType();
        if (_cpuType.equalsIgnoreCase(MIPS)) {
            WXExceptionUtils.commitCriticalExceptionRT((String) null, WXErrorCode.WX_KEY_EXCEPTION_SDK_INIT, "initSo", "[WX_KEY_EXCEPTION_SDK_INIT_CPU_NOT_SUPPORT] for android cpuType is MIPS", (Map<String, String>) null);
            return false;
        }
        if (WXEnvironment.CORE_SO_NAME.equals(str)) {
            copyStartUpSo();
        }
        try {
            IWXSoLoaderAdapter iWXSoLoaderAdapter = mSoLoader;
            if (iWXSoLoaderAdapter != null) {
                iWXSoLoaderAdapter.doLoadLibrary("c++_shared");
            } else {
                System.loadLibrary("c++_shared");
            }
        } catch (Error | Exception e) {
            WXErrorCode wXErrorCode = WXErrorCode.WX_KEY_EXCEPTION_SDK_INIT;
            WXExceptionUtils.commitCriticalExceptionRT((String) null, wXErrorCode, "initSo", "load c++_shared failed Detail Error is: " + e.getMessage(), (Map<String, String>) null);
            if (WXEnvironment.isApkDebugable()) {
                throw e;
            }
        }
        try {
            IWXSoLoaderAdapter iWXSoLoaderAdapter2 = mSoLoader;
            if (iWXSoLoaderAdapter2 != null) {
                iWXSoLoaderAdapter2.doLoadLibrary(str);
            } else {
                System.loadLibrary(str);
            }
            return true;
        } catch (Error | Exception e2) {
            if (_cpuType.contains(ARMEABI) || _cpuType.contains("x86")) {
                WXErrorCode wXErrorCode2 = WXErrorCode.WX_KEY_EXCEPTION_SDK_INIT;
                WXExceptionUtils.commitCriticalExceptionRT((String) null, wXErrorCode2, "initSo", str + "[WX_KEY_EXCEPTION_SDK_INIT_CPU_NOT_SUPPORT] for android cpuType is " + _cpuType + "\n Detail Error is: " + e2.getMessage(), (Map<String, String>) null);
            }
            if (!WXEnvironment.isApkDebugable()) {
                return false;
            }
            throw e2;
        }
    }

    private static File _desSoCopyFile(String str) {
        String _cpuType = _cpuType();
        String copySoDesDir = WXEnvironment.copySoDesDir();
        if (TextUtils.isEmpty(copySoDesDir)) {
            return null;
        }
        return new File(copySoDesDir, str + "/" + _cpuType);
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:30:0x00a5 */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00ab A[Catch:{ all -> 0x00c9 }] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00be A[Catch:{ all -> 0x00c9 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void copyStartUpSo() {
        /*
            java.lang.String r0 = "/cache"
            java.lang.String r1 = "/lib"
            android.app.Application r2 = com.taobao.weex.WXEnvironment.getApplication()     // Catch:{ all -> 0x00c9 }
            java.lang.String r2 = r2.getPackageName()     // Catch:{ all -> 0x00c9 }
            android.app.Application r3 = com.taobao.weex.WXEnvironment.getApplication()     // Catch:{ all -> 0x00c9 }
            android.content.Context r3 = r3.getApplicationContext()     // Catch:{ all -> 0x00c9 }
            java.io.File r3 = r3.getCacheDir()     // Catch:{ all -> 0x00c9 }
            java.lang.String r3 = r3.getPath()     // Catch:{ all -> 0x00c9 }
            r4 = 1
            java.lang.String r5 = "weexjsb"
            int r6 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x00c9 }
            r7 = 16
            java.lang.String r8 = "/libweexjst.so"
            java.lang.String r9 = "/libweexjsb.so"
            if (r6 >= r7) goto L_0x0030
            r4 = 0
            java.lang.String r5 = "weexjst"
            r6 = r8
            goto L_0x0031
        L_0x0030:
            r6 = r9
        L_0x0031:
            java.io.File r7 = _desSoCopyFile(r5)     // Catch:{ all -> 0x00c9 }
            boolean r10 = r7.exists()     // Catch:{ all -> 0x00c9 }
            if (r10 != 0) goto L_0x003e
            r7.mkdirs()     // Catch:{ all -> 0x00c9 }
        L_0x003e:
            java.io.File r10 = new java.io.File     // Catch:{ all -> 0x00c9 }
            r10.<init>(r7, r6)     // Catch:{ all -> 0x00c9 }
            java.lang.String r6 = r10.getAbsolutePath()     // Catch:{ all -> 0x00c9 }
            com.taobao.weex.WXEnvironment.CORE_JSB_SO_PATH = r6     // Catch:{ all -> 0x00c9 }
            java.lang.String r6 = "-1"
            java.lang.String r6 = com.taobao.weex.WXEnvironment.getDefaultSettingValue(r5, r6)     // Catch:{ all -> 0x00c9 }
            boolean r7 = r10.exists()     // Catch:{ all -> 0x00c9 }
            if (r7 == 0) goto L_0x0060
            java.lang.String r7 = com.taobao.weex.WXEnvironment.getAppVersionName()     // Catch:{ all -> 0x00c9 }
            boolean r6 = android.text.TextUtils.equals(r7, r6)     // Catch:{ all -> 0x00c9 }
            if (r6 == 0) goto L_0x0060
            return
        L_0x0060:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x00c9 }
            r6.<init>()     // Catch:{ all -> 0x00c9 }
            java.lang.String r7 = "/data/data/"
            r6.append(r7)     // Catch:{ all -> 0x00c9 }
            r6.append(r2)     // Catch:{ all -> 0x00c9 }
            r6.append(r1)     // Catch:{ all -> 0x00c9 }
            java.lang.String r2 = r6.toString()     // Catch:{ all -> 0x00c9 }
            if (r3 == 0) goto L_0x0080
            int r6 = r3.indexOf(r0)     // Catch:{ all -> 0x00c9 }
            if (r6 <= 0) goto L_0x0080
            java.lang.String r2 = r3.replace(r0, r1)     // Catch:{ all -> 0x00c9 }
        L_0x0080:
            if (r4 == 0) goto L_0x0088
            java.io.File r0 = new java.io.File     // Catch:{ all -> 0x00c9 }
            r0.<init>(r2, r9)     // Catch:{ all -> 0x00c9 }
            goto L_0x008d
        L_0x0088:
            java.io.File r0 = new java.io.File     // Catch:{ all -> 0x00c9 }
            r0.<init>(r2, r8)     // Catch:{ all -> 0x00c9 }
        L_0x008d:
            boolean r1 = r0.exists()     // Catch:{ all -> 0x00c9 }
            if (r1 != 0) goto L_0x00a5
            java.lang.Class<com.taobao.weex.utils.WXSoInstallMgrSdk> r1 = com.taobao.weex.utils.WXSoInstallMgrSdk.class
            java.lang.ClassLoader r1 = r1.getClassLoader()     // Catch:{ all -> 0x00a5 }
            dalvik.system.PathClassLoader r1 = (dalvik.system.PathClassLoader) r1     // Catch:{ all -> 0x00a5 }
            java.lang.String r1 = r1.findLibrary(r5)     // Catch:{ all -> 0x00a5 }
            java.io.File r2 = new java.io.File     // Catch:{ all -> 0x00a5 }
            r2.<init>(r1)     // Catch:{ all -> 0x00a5 }
            r0 = r2
        L_0x00a5:
            boolean r1 = r0.exists()     // Catch:{ all -> 0x00c9 }
            if (r1 != 0) goto L_0x00b8
            com.taobao.weex.WXEnvironment.extractSo()     // Catch:{ all -> 0x00c9 }
            java.lang.String r0 = com.taobao.weex.WXEnvironment.copySoDesDir()     // Catch:{ all -> 0x00c9 }
            java.io.File r1 = new java.io.File     // Catch:{ all -> 0x00c9 }
            r1.<init>(r0, r9)     // Catch:{ all -> 0x00c9 }
            r0 = r1
        L_0x00b8:
            boolean r1 = r0.exists()     // Catch:{ all -> 0x00c9 }
            if (r1 == 0) goto L_0x00c1
            com.taobao.weex.utils.WXFileUtils.copyFile(r0, r10)     // Catch:{ all -> 0x00c9 }
        L_0x00c1:
            java.lang.String r0 = com.taobao.weex.WXEnvironment.getAppVersionName()     // Catch:{ all -> 0x00c9 }
            com.taobao.weex.WXEnvironment.writeDefaultSettingsValue(r5, r0)     // Catch:{ all -> 0x00c9 }
            goto L_0x00cd
        L_0x00c9:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00cd:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.utils.WXSoInstallMgrSdk.copyStartUpSo():void");
    }

    public static void copyJssRuntimeSo() {
        boolean checkGreyConfig = WXUtils.checkGreyConfig("wxapm", "use_runtime_api", WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
        WXLogUtils.e("weex", "tryUseRunTimeApi ? " + checkGreyConfig);
        if (checkGreyConfig) {
            try {
                WXLogUtils.e("weex", "copyJssRuntimeSo: ");
                File _desSoCopyFile = _desSoCopyFile(WXEnvironment.CORE_JSS_SO_NAME);
                if (!_desSoCopyFile.exists()) {
                    _desSoCopyFile.mkdirs();
                }
                File file = new File(_desSoCopyFile, "libweexjss.so");
                String defaultSettingValue = WXEnvironment.getDefaultSettingValue("app_version_code_weex", "-1");
                if (file.exists()) {
                    if (!TextUtils.equals(WXEnvironment.getAppVersionName(), defaultSettingValue)) {
                        file.delete();
                    } else {
                        WXEnvironment.CORE_JSS_RUNTIME_SO_PATH = file.getAbsolutePath();
                        WXEnvironment.sUseRunTimeApi = true;
                        WXLogUtils.e("weex", "copyJssRuntimeSo exist:  return");
                        return;
                    }
                }
                String findLibrary = ((PathClassLoader) WXSoInstallMgrSdk.class.getClassLoader()).findLibrary("weexjssr");
                if (!TextUtils.isEmpty(findLibrary)) {
                    file.createNewFile();
                    WXFileUtils.copyFileWithException(new File(findLibrary), file);
                    WXEnvironment.CORE_JSS_RUNTIME_SO_PATH = file.getAbsolutePath();
                    WXEnvironment.writeDefaultSettingsValue("app_version_code_weex", WXEnvironment.getAppVersionName());
                    WXEnvironment.sUseRunTimeApi = true;
                    WXLogUtils.e("weex", "copyJssRuntimeSo: cp end and return ");
                }
            } catch (Throwable th) {
                th.printStackTrace();
                WXEnvironment.sUseRunTimeApi = false;
                WXLogUtils.e("weex", "copyJssRuntimeSo:  exception" + th);
            }
        }
    }

    private static String _getFieldReflectively(Build build, String str) {
        try {
            return Build.class.getField(str).get(build).toString();
        } catch (Exception unused) {
            return "Unknown";
        }
    }

    private static String _cpuType() {
        if (TextUtils.isEmpty(mAbi)) {
            try {
                mAbi = Build.CPU_ABI;
            } catch (Throwable th) {
                th.printStackTrace();
                mAbi = ARMEABI;
            }
            if (TextUtils.isEmpty(mAbi)) {
                mAbi = ARMEABI;
            }
            mAbi = mAbi.toLowerCase(Locale.ROOT);
        }
        return mAbi;
    }

    static boolean checkSoIsValid(String str, long j) {
        Class<WXSoInstallMgrSdk> cls = WXSoInstallMgrSdk.class;
        if (mContext == null) {
            return false;
        }
        try {
            long currentTimeMillis = System.currentTimeMillis();
            if (cls.getClassLoader() instanceof PathClassLoader) {
                String findLibrary = ((PathClassLoader) cls.getClassLoader()).findLibrary(str);
                if (TextUtils.isEmpty(findLibrary)) {
                    return false;
                }
                File file = new File(findLibrary);
                if (file.exists()) {
                    if (j != file.length()) {
                        return false;
                    }
                }
                WXLogUtils.w("weex so size check path :" + findLibrary + "   " + (System.currentTimeMillis() - currentTimeMillis));
                return true;
            }
        } catch (Throwable th) {
            WXErrorCode wXErrorCode = WXErrorCode.WX_KEY_EXCEPTION_SDK_INIT;
            WXExceptionUtils.commitCriticalExceptionRT((String) null, wXErrorCode, "checkSoIsValid", "[WX_KEY_EXCEPTION_SDK_INIT_CPU_NOT_SUPPORT] for weex so size check fail exception :" + th.getMessage(), (Map<String, String>) null);
            WXLogUtils.e("weex so size check fail exception :" + th.getMessage());
        }
        return true;
    }

    static String _targetSoFile(String str, int i) {
        Application application = mContext;
        if (application == null) {
            return "";
        }
        String str2 = "/data/data/" + application.getPackageName() + "/files";
        File filesDir = application.getFilesDir();
        if (filesDir != null) {
            str2 = filesDir.getPath();
        }
        return str2 + "/lib" + str + "bk" + i + ".so";
    }

    static void removeSoIfExit(String str, int i) {
        File file = new File(_targetSoFile(str, i));
        if (file.exists()) {
            file.delete();
        }
    }

    static boolean isExist(String str, int i) {
        return new File(_targetSoFile(str, i)).exists();
    }

    static boolean _loadUnzipSo(String str, int i, IWXUserTrackAdapter iWXUserTrackAdapter) {
        try {
            if (isExist(str, i)) {
                IWXSoLoaderAdapter iWXSoLoaderAdapter = mSoLoader;
                if (iWXSoLoaderAdapter != null) {
                    iWXSoLoaderAdapter.doLoad(_targetSoFile(str, i));
                } else {
                    System.load(_targetSoFile(str, i));
                }
            }
            return true;
        } catch (Throwable th) {
            WXErrorCode wXErrorCode = WXErrorCode.WX_KEY_EXCEPTION_SDK_INIT_CPU_NOT_SUPPORT;
            WXExceptionUtils.commitCriticalExceptionRT((String) null, wXErrorCode, "_loadUnzipSo", "[WX_KEY_EXCEPTION_SDK_INIT_WX_ERR_COPY_FROM_APK] \n Detail Msg is : " + th.getMessage(), (Map<String, String>) null);
            WXLogUtils.e("", th);
            return false;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v10, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v11, resolved type: java.io.FileOutputStream} */
    /* JADX WARNING: type inference failed for: r1v3, types: [java.io.FileOutputStream] */
    /* JADX WARNING: type inference failed for: r1v9 */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x0110, code lost:
        if (r5 == null) goto L_0x0113;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00c4 A[SYNTHETIC, Splitter:B:63:0x00c4] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x00ce A[SYNTHETIC, Splitter:B:69:0x00ce] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x00d8 A[SYNTHETIC, Splitter:B:75:0x00d8] */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x0116  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static boolean unZipSelectedFiles(java.lang.String r10, int r11, com.taobao.weex.adapter.IWXUserTrackAdapter r12) throws java.util.zip.ZipException, java.io.IOException {
        /*
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "lib/armeabi/lib"
            r0.append(r1)
            r0.append(r10)
            java.lang.String r1 = ".so"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            boolean r2 = io.dcloud.common.util.PdrUtil.isSafeEntryName(r0)
            r3 = 0
            if (r2 != 0) goto L_0x001e
            return r3
        L_0x001e:
            android.app.Application r2 = mContext
            if (r2 != 0) goto L_0x0023
            return r3
        L_0x0023:
            android.content.pm.ApplicationInfo r4 = r2.getApplicationInfo()
            if (r4 == 0) goto L_0x002c
            java.lang.String r4 = r4.sourceDir
            goto L_0x002e
        L_0x002c:
            java.lang.String r4 = ""
        L_0x002e:
            java.util.zip.ZipFile r5 = new java.util.zip.ZipFile
            r5.<init>(r4)
            r4 = 0
            java.util.Enumeration r6 = r5.entries()     // Catch:{ IOException -> 0x00f0 }
        L_0x0038:
            boolean r7 = r6.hasMoreElements()     // Catch:{ IOException -> 0x00f0 }
            if (r7 == 0) goto L_0x00ea
            java.lang.Object r7 = r6.nextElement()     // Catch:{ IOException -> 0x00f0 }
            java.util.zip.ZipEntry r7 = (java.util.zip.ZipEntry) r7     // Catch:{ IOException -> 0x00f0 }
            java.lang.String r8 = r7.getName()     // Catch:{ IOException -> 0x00f0 }
            boolean r8 = r8.startsWith(r0)     // Catch:{ IOException -> 0x00f0 }
            if (r8 == 0) goto L_0x0038
            removeSoIfExit(r10, r11)     // Catch:{ all -> 0x00be }
            java.io.InputStream r0 = r5.getInputStream(r7)     // Catch:{ all -> 0x00be }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x00bb }
            r6.<init>()     // Catch:{ all -> 0x00bb }
            java.lang.String r7 = "lib"
            r6.append(r7)     // Catch:{ all -> 0x00bb }
            r6.append(r10)     // Catch:{ all -> 0x00bb }
            java.lang.String r7 = "bk"
            r6.append(r7)     // Catch:{ all -> 0x00bb }
            r6.append(r11)     // Catch:{ all -> 0x00bb }
            r6.append(r1)     // Catch:{ all -> 0x00bb }
            java.lang.String r1 = r6.toString()     // Catch:{ all -> 0x00bb }
            java.io.FileOutputStream r1 = r2.openFileOutput(r1, r3)     // Catch:{ all -> 0x00bb }
            java.nio.channels.FileChannel r2 = r1.getChannel()     // Catch:{ all -> 0x00b8 }
            r6 = 1024(0x400, float:1.435E-42)
            byte[] r6 = new byte[r6]     // Catch:{ all -> 0x00b6 }
            r7 = 0
        L_0x007e:
            int r8 = r0.read(r6)     // Catch:{ all -> 0x00b6 }
            if (r8 <= 0) goto L_0x008d
            java.nio.ByteBuffer r9 = java.nio.ByteBuffer.wrap(r6, r3, r8)     // Catch:{ all -> 0x00b6 }
            r2.write(r9)     // Catch:{ all -> 0x00b6 }
            int r7 = r7 + r8
            goto L_0x007e
        L_0x008d:
            if (r0 == 0) goto L_0x0097
            r0.close()     // Catch:{ Exception -> 0x0093 }
            goto L_0x0097
        L_0x0093:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ IOException -> 0x00f0 }
        L_0x0097:
            if (r2 == 0) goto L_0x00a1
            r2.close()     // Catch:{ Exception -> 0x009d }
            goto L_0x00a1
        L_0x009d:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ IOException -> 0x00f0 }
        L_0x00a1:
            if (r1 == 0) goto L_0x00ab
            r1.close()     // Catch:{ Exception -> 0x00a7 }
            goto L_0x00ab
        L_0x00a7:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ IOException -> 0x00f0 }
        L_0x00ab:
            r5.close()     // Catch:{ IOException -> 0x00f0 }
            if (r7 <= 0) goto L_0x00b5
            boolean r10 = _loadUnzipSo(r10, r11, r12)     // Catch:{ IOException -> 0x00e7, all -> 0x00e4 }
            return r10
        L_0x00b5:
            return r3
        L_0x00b6:
            r10 = move-exception
            goto L_0x00c2
        L_0x00b8:
            r10 = move-exception
            r2 = r4
            goto L_0x00c2
        L_0x00bb:
            r10 = move-exception
            r1 = r4
            goto L_0x00c1
        L_0x00be:
            r10 = move-exception
            r0 = r4
            r1 = r0
        L_0x00c1:
            r2 = r1
        L_0x00c2:
            if (r0 == 0) goto L_0x00cc
            r0.close()     // Catch:{ Exception -> 0x00c8 }
            goto L_0x00cc
        L_0x00c8:
            r11 = move-exception
            r11.printStackTrace()     // Catch:{ IOException -> 0x00f0 }
        L_0x00cc:
            if (r2 == 0) goto L_0x00d6
            r2.close()     // Catch:{ Exception -> 0x00d2 }
            goto L_0x00d6
        L_0x00d2:
            r11 = move-exception
            r11.printStackTrace()     // Catch:{ IOException -> 0x00f0 }
        L_0x00d6:
            if (r1 == 0) goto L_0x00e0
            r1.close()     // Catch:{ Exception -> 0x00dc }
            goto L_0x00e0
        L_0x00dc:
            r11 = move-exception
            r11.printStackTrace()     // Catch:{ IOException -> 0x00f0 }
        L_0x00e0:
            r5.close()     // Catch:{ IOException -> 0x00f0 }
            throw r10     // Catch:{ IOException -> 0x00e7, all -> 0x00e4 }
        L_0x00e4:
            r10 = move-exception
            r5 = r4
            goto L_0x0114
        L_0x00e7:
            r10 = move-exception
            r5 = r4
            goto L_0x00f1
        L_0x00ea:
            r5.close()
            goto L_0x0113
        L_0x00ee:
            r10 = move-exception
            goto L_0x0114
        L_0x00f0:
            r10 = move-exception
        L_0x00f1:
            r10.printStackTrace()     // Catch:{ all -> 0x00ee }
            com.taobao.weex.common.WXErrorCode r11 = com.taobao.weex.common.WXErrorCode.WX_KEY_EXCEPTION_SDK_INIT_CPU_NOT_SUPPORT     // Catch:{ all -> 0x00ee }
            java.lang.String r12 = "unZipSelectedFiles"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ee }
            r0.<init>()     // Catch:{ all -> 0x00ee }
            java.lang.String r1 = "[WX_KEY_EXCEPTION_SDK_INIT_unZipSelectedFiles] \n Detail msg is: "
            r0.append(r1)     // Catch:{ all -> 0x00ee }
            java.lang.String r10 = r10.getMessage()     // Catch:{ all -> 0x00ee }
            r0.append(r10)     // Catch:{ all -> 0x00ee }
            java.lang.String r10 = r0.toString()     // Catch:{ all -> 0x00ee }
            com.taobao.weex.utils.WXExceptionUtils.commitCriticalExceptionRT(r4, r11, r12, r10, r4)     // Catch:{ all -> 0x00ee }
            if (r5 == 0) goto L_0x0113
            goto L_0x00ea
        L_0x0113:
            return r3
        L_0x0114:
            if (r5 == 0) goto L_0x0119
            r5.close()
        L_0x0119:
            goto L_0x011b
        L_0x011a:
            throw r10
        L_0x011b:
            goto L_0x011a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.utils.WXSoInstallMgrSdk.unZipSelectedFiles(java.lang.String, int, com.taobao.weex.adapter.IWXUserTrackAdapter):boolean");
    }
}
