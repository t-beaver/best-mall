package com.bun.miitmdid.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.zip.CRC32;

public class Utils {
    public static final String CPU_ABI_X86 = "x86";

    private static String CPUABI() {
        try {
            if (new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop ro.product.cpu.abi").getInputStream())).readLine().contains(CPU_ABI_X86)) {
                return CPU_ABI_X86;
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return "arm";
    }

    public static void PrintClassMethod(Class<?> cls) {
        for (Method name : cls.getMethods()) {
            System.out.println(name.getName());
        }
    }

    public static void PrintObjectType(Class<?> cls) {
        String name = cls.getName();
        PrintStream printStream = System.out;
        printStream.println("PrintObjectType:" + name);
    }

    public static void PrintObjectType(Object obj) {
        String name = obj.getClass().getName();
        PrintStream printStream = System.out;
        printStream.println("PrintObjectType:" + name);
    }

    public static long getFileCRC(String str) {
        try {
            File file = new File(str);
            if (!file.exists()) {
                return -1;
            }
            int length = (int) file.length();
            FileInputStream fileInputStream = new FileInputStream(str);
            CRC32 crc32 = new CRC32();
            byte[] bArr = new byte[length];
            for (int i = 0; i < length; i += fileInputStream.read(bArr, i, length - i)) {
            }
            crc32.update(length);
            return crc32.getValue();
        } catch (IOException unused) {
        }
    }

    public static void getFileListame(String str) {
        File[] listFiles = new File(str).listFiles();
        if (listFiles != null) {
            for (int i = 0; i < listFiles.length; i++) {
                Log.i("Utils", listFiles[i].getName());
                if (listFiles[i].isDirectory()) {
                    getFileListame(listFiles[i].getAbsolutePath());
                    Log.i("Utils", String.valueOf(listFiles[i].getAbsolutePath()) + listFiles[i].getName());
                }
            }
        }
    }

    public static String getLibraryDir(Context context) {
        return context.getApplicationInfo().nativeLibraryDir;
    }

    public static String getUserDir(Context context) {
        return context.getFilesDir().getParent();
    }

    public static String getXdataDir(Context context, String str) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(String.valueOf(getUserDir(context)) + "/" + JLibrary.xdata + "/" + str);
        return stringBuffer.toString();
    }

    public static String getYdataDir(Context context, String str) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(String.valueOf(getUserDir(context)) + "/" + JLibrary.ydata + "/" + str);
        return stringBuffer.toString();
    }

    public static boolean isX86() {
        return Build.CPU_ABI.equals(CPU_ABI_X86) || CPUABI().equals(CPU_ABI_X86);
    }

    public static boolean update(Context context) throws Exception {
        long zipCrc = ZipUtils.getZipCrc(new File(context.getApplicationInfo().sourceDir));
        boolean z = false;
        SharedPreferences sharedPreferences = context.getSharedPreferences("update", 0);
        if (zipCrc != sharedPreferences.getLong("crc", 0)) {
            z = true;
        }
        sharedPreferences.edit().putLong("crc", zipCrc).commit();
        return z;
    }

    public static void x0xooXdata(InputStream inputStream, String str, Context context) throws Exception {
        try {
            File file = new File(str);
            byte[] bArr = new byte[65536];
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            while (true) {
                int read = bufferedInputStream.read(bArr);
                if (read <= 0) {
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                    bufferedInputStream.close();
                    return;
                }
                bufferedOutputStream.write(bArr, 0, read);
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
