package io.dcloud.h.a.e;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class c {
    public static void a(byte[] bArr, int i, String str) {
        File file = new File(str);
        File parentFile = file.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            return;
        }
        if (file.exists()) {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rws");
                randomAccessFile.setLength((long) (bArr.length + i));
                randomAccessFile.seek((long) i);
                randomAccessFile.write(bArr);
                randomAccessFile.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e4) {
                e4.printStackTrace();
            }
            if (fileOutputStream != null) {
                if (bArr != null) {
                    try {
                        fileOutputStream.write(bArr, 0, bArr.length);
                        fileOutputStream.flush();
                    } catch (IOException e5) {
                        e5.printStackTrace();
                        try {
                            fileOutputStream.close();
                            return;
                        } catch (IOException e6) {
                            e6.printStackTrace();
                            return;
                        }
                    } catch (Throwable th) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e7) {
                            e7.printStackTrace();
                        }
                        throw th;
                    }
                }
                fileOutputStream.close();
            }
        }
    }
}
