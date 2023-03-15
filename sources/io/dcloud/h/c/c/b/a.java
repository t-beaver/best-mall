package io.dcloud.h.c.c.b;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.h.c.c.a.e.d;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

public class a {
    private static a a;
    private final String b = "UNIAPP_HostPicker_0817";
    private final String c = "SP_LAST_SUIT_HOST_NAME_0817";

    public interface b {
        void a(C0057a aVar);

        boolean b(C0057a aVar);

        void onNoOnePicked();
    }

    private a() {
    }

    private void a(Context context, List<C0057a> list, String str) {
        String str2 = "SP_LAST_SUIT_HOST_NAME_0817" + str;
        SharedPreferences sharedPreferences = context.getSharedPreferences("UNIAPP_HostPicker_0817", 0);
        String string = sharedPreferences.getString(str2, "");
        for (C0057a next : list) {
            if (!next.c()) {
                throw new RuntimeException("error format host");
            } else if (!TextUtils.isEmpty(string)) {
                if (string.equals(next.a)) {
                    next.b = C0057a.C0058a.FIRST;
                } else {
                    next.b = C0057a.C0058a.NORMAL;
                }
            }
        }
        sharedPreferences.edit().remove(str2).apply();
    }

    /* renamed from: io.dcloud.h.c.c.b.a$a  reason: collision with other inner class name */
    public static class C0057a implements Comparable<C0057a>, Cloneable {
        String a;
        C0058a b = C0058a.NORMAL;

        /* renamed from: io.dcloud.h.c.c.b.a$a$a  reason: collision with other inner class name */
        public enum C0058a {
            NORMAL(0),
            FIRST(1),
            BACKUP(-1);
            
            int e;

            private C0058a(int i) {
                this.e = 0;
                this.e = i;
            }
        }

        public C0057a(String str, C0058a aVar) {
            this.a = str;
            this.b = aVar;
        }

        /* renamed from: a */
        public int compareTo(C0057a aVar) {
            if (aVar == null) {
                return 1;
            }
            return aVar.b.e - this.b.e;
        }

        public String b() {
            String str = "";
            if (TextUtils.isEmpty(this.a)) {
                return str;
            }
            try {
                str = new String(Base64.decode(this.a.getBytes("UTF-8"), 2), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return d.d(str);
        }

        public boolean c() {
            return !TextUtils.isEmpty(this.a);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof C0057a)) {
                return false;
            }
            C0057a aVar = (C0057a) obj;
            if (TextUtils.isEmpty(aVar.a)) {
                return false;
            }
            return aVar.a.equals(this.a);
        }

        public int hashCode() {
            return this.a.hashCode();
        }

        public String toString() {
            return "Host{hostUrl='" + this.a + Operators.SINGLE_QUOTE + ", priority=" + this.b + Operators.BLOCK_END;
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public C0057a clone() {
            return new C0057a(this.a, this.b);
        }
    }

    public static a a() {
        if (a == null) {
            synchronized (a.class) {
                if (a == null) {
                    a = new a();
                }
            }
        }
        return a;
    }

    public void a(Context context, List<C0057a> list, String str, b bVar) {
        if (list == null || list.isEmpty()) {
            throw new RuntimeException("call initHosts first");
        }
        a(context, list, str);
        Collections.sort(list);
        for (C0057a next : list) {
            if (bVar.b(next)) {
                if (next.b != C0057a.C0058a.BACKUP) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("UNIAPP_HostPicker_0817", 0);
                    sharedPreferences.edit().putString("SP_LAST_SUIT_HOST_NAME_0817" + str, next.a).apply();
                }
                bVar.a(next);
                return;
            }
        }
        bVar.onNoOnePicked();
    }
}
