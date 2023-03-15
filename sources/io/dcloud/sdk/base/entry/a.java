package io.dcloud.sdk.base.entry;

import android.content.Context;
import io.dcloud.h.a.d.b.g;
import io.dcloud.sdk.base.entry.AdData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

public class a {
    private long a;
    private String b;
    private AdData c;

    public a(String str) {
        this.b = str;
    }

    public void a(String str) {
        try {
            Date parse = new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(new JSONObject(str).optString("expires"));
            if (parse != null) {
                this.a = parse.getTime();
            }
        } catch (ParseException | JSONException unused) {
        }
    }

    public boolean b() {
        return System.currentTimeMillis() > this.a;
    }

    public void a(Context context, AdData adData) {
        if (adData != null) {
            adData.c(context);
            String a2 = g.a().a(context, this.b);
            if (a2 != null) {
                try {
                    adData.a(new JSONObject(a2), (AdData.e) null, false);
                } catch (JSONException unused) {
                }
            }
        }
    }

    public void a(AdData adData) {
        this.c = adData;
    }

    public String a() {
        return this.b;
    }
}
