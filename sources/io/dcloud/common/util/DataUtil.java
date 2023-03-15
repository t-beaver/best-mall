package io.dcloud.common.util;

import android.text.TextUtils;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.f.a;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.Character;
import java.util.Locale;

public class DataUtil {
    public static String GBK2Unicode(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (!isNeedConvert(charAt)) {
                stringBuffer.append(charAt);
            } else {
                stringBuffer.append("\\u" + Integer.toHexString(charAt));
            }
        }
        return stringBuffer.toString();
    }

    public static String Unicode2GBK(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        int length = str.length();
        int i = 0;
        while (i < length) {
            if (i < length - 1) {
                int i2 = i + 2;
                if ("\\u".equals(str.substring(i, i2))) {
                    i += 6;
                    stringBuffer.append((char) Integer.parseInt(str.substring(i2, i), 16));
                }
            }
            stringBuffer.append(str.charAt(i));
            i++;
        }
        return stringBuffer.toString();
    }

    public static void datToJsString(final String str, final ICallBack iCallBack) {
        ThreadPool.self().addThreadTask(new Runnable() {
            public void run() {
                String datToJsStringSync = DataUtil.datToJsStringSync(str);
                if (!TextUtils.isEmpty(datToJsStringSync)) {
                    iCallBack.onCallBack(0, datToJsStringSync);
                }
            }
        }, true);
    }

    public static String datToJsStringSync(String str) {
        InputStream inputStream;
        File file = new File(DeviceInfo.sBaseFsRootPath + File.separator + str);
        boolean z = false;
        boolean z2 = true;
        if (file.exists()) {
            try {
                inputStream = new FileInputStream(file);
                z2 = false;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                inputStream = null;
            }
        } else {
            inputStream = PlatformUtil.getResInputStream(str);
        }
        if (!str.equals(BaseInfo.sUniNViewServiceJsPath)) {
            z = z2;
        }
        if (inputStream != null) {
            if (!z) {
                try {
                    return new String(IOUtil.getBytes(inputStream));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } else if (!TextUtils.isEmpty(a.c())) {
                return AESUtil.decrypt(a.c(), a.b(), IOUtil.getBytes(inputStream));
            }
        }
        return null;
    }

    public static boolean isNeedConvert(char c) {
        return (c & 255) != c;
    }

    public static String unicodeToUtf8(String str) {
        if (str == null) {
            return "";
        }
        int length = str.length();
        StringBuffer stringBuffer = new StringBuffer(length);
        int i = 0;
        while (i < length) {
            int i2 = i + 1;
            char charAt = str.charAt(i);
            if (charAt == '\\') {
                i = i2 + 1;
                char charAt2 = str.charAt(i2);
                if (charAt2 == 'u') {
                    int i3 = 0;
                    int i4 = 0;
                    while (i3 < 4) {
                        int i5 = i + 1;
                        char charAt3 = str.charAt(i);
                        switch (charAt3) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                i4 = ((i4 << 4) + charAt3) - 48;
                                break;
                            default:
                                switch (charAt3) {
                                    case 'A':
                                    case 'B':
                                    case 'C':
                                    case 'D':
                                    case 'E':
                                    case 'F':
                                        i4 = (((i4 << 4) + 10) + charAt3) - 65;
                                        break;
                                    default:
                                        switch (charAt3) {
                                            case 'a':
                                            case 'b':
                                            case 'c':
                                            case 'd':
                                            case 'e':
                                            case 'f':
                                                i4 = (((i4 << 4) + 10) + charAt3) - 97;
                                                break;
                                            default:
                                                throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
                                        }
                                }
                        }
                        i3++;
                        i = i5;
                    }
                    stringBuffer.append((char) i4);
                } else {
                    if (charAt2 == 't') {
                        charAt2 = 't';
                    } else if (charAt2 == 'r') {
                        charAt2 = 'r';
                    } else if (charAt2 == 'n') {
                        charAt2 = 'n';
                    } else if (charAt2 == 'f') {
                        charAt2 = 'f';
                    }
                    stringBuffer.append(charAt2);
                }
            } else {
                stringBuffer.append(charAt);
                i = i2;
            }
        }
        return stringBuffer.toString();
    }

    public static String utf8ToUnicode(String str) {
        char[] charArray = str.toCharArray();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            Character.UnicodeBlock of = Character.UnicodeBlock.of(charArray[i]);
            if (of == Character.UnicodeBlock.BASIC_LATIN || of == Character.UnicodeBlock.LATIN_1_SUPPLEMENT) {
                stringBuffer.append(charArray[i]);
            } else if (of == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                stringBuffer.append((char) (charArray[i] - 65248));
            } else {
                String hexString = Integer.toHexString(charArray[i]);
                stringBuffer.append(("\\u" + hexString).toLowerCase(Locale.ENGLISH));
            }
        }
        return stringBuffer.toString();
    }

    public String gbk2utf8(String str) {
        return unicodeToUtf8(GBK2Unicode(str));
    }

    public String utf82gbk(String str) {
        return Unicode2GBK(utf8ToUnicode(str));
    }
}
