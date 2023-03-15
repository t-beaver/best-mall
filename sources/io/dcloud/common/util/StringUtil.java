package io.dcloud.common.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import com.taobao.weex.performance.WXInstanceApm;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

public class StringUtil {
    public static String convert(String str) {
        if (str == null) {
            str = "";
        }
        StringBuffer stringBuffer = new StringBuffer(1000);
        stringBuffer.setLength(0);
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            stringBuffer.append("\\u");
            String hexString = Integer.toHexString(charAt >>> 8);
            if (hexString.length() == 1) {
                stringBuffer.append(WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
            }
            stringBuffer.append(hexString);
            String hexString2 = Integer.toHexString(charAt & 255);
            if (hexString2.length() == 1) {
                stringBuffer.append(WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
            }
            stringBuffer.append(hexString2);
        }
        return new String(stringBuffer);
    }

    public static String format(String str, Object... objArr) {
        return String.format(Locale.US, str, objArr);
    }

    public static Double getDouble(String str) {
        try {
            return Double.valueOf(Double.parseDouble(str));
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    public static Integer getInt(String str) {
        try {
            return Integer.valueOf(Integer.parseInt(str));
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    public static String getSCString(String str, String str2) {
        if (str != null) {
            try {
                return new JSONObject(str).optString(str2);
            } catch (JSONException unused) {
            }
        }
        return null;
    }

    public static String revert(String str) {
        int i;
        int i2;
        if (str == null) {
            str = "";
        }
        if (str.indexOf("\\u") == -1) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer(1000);
        int i3 = 0;
        while (i3 < str.length()) {
            if (str.substring(i3).startsWith("\\u")) {
                i = i3 + 6;
                String substring = str.substring(i3, i).substring(2);
                int i4 = 0;
                for (int i5 = 0; i5 < substring.length(); i5++) {
                    char charAt = substring.charAt(i5);
                    switch (charAt) {
                        case 'a':
                            i2 = 10;
                            break;
                        case 'b':
                            i2 = 11;
                            break;
                        case 'c':
                            i2 = 12;
                            break;
                        case 'd':
                            i2 = 13;
                            break;
                        case 'e':
                            i2 = 14;
                            break;
                        case 'f':
                            i2 = 15;
                            break;
                        default:
                            i2 = charAt - '0';
                            break;
                    }
                    i4 += i2 * ((int) Math.pow(16.0d, (double) ((substring.length() - i5) - 1)));
                }
                stringBuffer.append((char) i4);
            } else {
                i = i3 + 1;
                stringBuffer.append(str.charAt(i3));
            }
            i3 = i;
        }
        return stringBuffer.toString();
    }

    public static Bitmap textToBitmap(Activity activity, String str) {
        if (activity == null) {
            return null;
        }
        try {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            int pxFromDp = PdrUtil.pxFromDp(10.0f, activity.getResources().getDisplayMetrics());
            int pxFromDp2 = PdrUtil.pxFromDp(5.0f, activity.getResources().getDisplayMetrics());
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(-16777216);
            textPaint.setAntiAlias(true);
            textPaint.setTextSize((float) PdrUtil.pxFromDp(12.0f, activity.getResources().getDisplayMetrics()));
            StaticLayout staticLayout = new StaticLayout(str, textPaint, 450, Layout.Alignment.ALIGN_NORMAL, 1.3f, 0.0f, true);
            Bitmap createBitmap = Bitmap.createBitmap(staticLayout.getWidth() + pxFromDp, staticLayout.getHeight() + pxFromDp, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            float f = (float) pxFromDp2;
            canvas.translate(f, f);
            canvas.drawColor(-1);
            staticLayout.draw(canvas);
            return createBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String trimString(String str, char c) {
        if (str == null || str.equals("")) {
            return str;
        }
        int i = 0;
        if (str.charAt(0) == c) {
            i = 1;
        }
        int length = str.length();
        int i2 = length - 1;
        if (str.charAt(i2) == c) {
            length = i2;
        }
        return str.substring(i, length);
    }

    public static String[] trimString(String[] strArr, char c) {
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = trimString(strArr[i], c);
        }
        return strArr;
    }
}
