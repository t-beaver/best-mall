package io.dcloud.h.a;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.taobao.weex.el.parse.Operators;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class a {
    public static int a(Context context) {
        return (int) (((float) context.getResources().getDisplayMetrics().heightPixels) * 0.8f);
    }

    public static int b(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static String a(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str2)) {
            Matcher matcher = Pattern.compile("[\\w\\.]+[\\.](" + "avi|mpeg|3gp|mp3|mp4|wav|jpeg|gif|jpg|png|apk|exe|pdf|rar|zip|docx|doc" + Operators.BRACKET_END_STR).matcher(str);
            if (matcher.find()) {
                return matcher.group();
            }
            if (!TextUtils.isEmpty(str3)) {
                return str3;
            }
            return String.valueOf(System.currentTimeMillis()) + ".dat";
        }
        String lastPathSegment = Uri.parse(str).getLastPathSegment();
        if (lastPathSegment != null && lastPathSegment.contains(str2)) {
            return lastPathSegment;
        }
        if (!TextUtils.isEmpty(str3)) {
            return str3;
        }
        return String.valueOf(System.currentTimeMillis()) + str2;
    }
}
