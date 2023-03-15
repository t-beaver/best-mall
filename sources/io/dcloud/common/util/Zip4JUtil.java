package io.dcloud.common.util;

import java.io.File;
import java.nio.charset.Charset;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class Zip4JUtil {
    public static boolean isEncryptedZip(File file) {
        try {
            ZipFile zipFile = new ZipFile(file);
            if (!zipFile.isValidZipFile()) {
                return false;
            }
            return zipFile.isEncrypted();
        } catch (ZipException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void upZipFileWithPassword(File file, String str, String str2) throws ZipException {
        if (!str.endsWith("/")) {
            str = str + File.separatorChar;
        }
        File file2 = new File(str);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        char[] cArr = null;
        if (str2 != null) {
            cArr = str2.toCharArray();
        }
        ZipFile zipFile = new ZipFile(file, cArr);
        zipFile.setCharset(Charset.forName("UTF-8"));
        zipFile.extractAll(file2.getPath());
    }
}
