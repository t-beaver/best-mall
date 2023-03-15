package io.dcloud.sdk.poly.base.config;

import android.os.Parcel;
import android.os.Parcelable;
import com.taobao.weex.el.parse.Operators;

public class ThirdSlotConfig implements Parcelable {
    public static final Parcelable.Creator<ThirdSlotConfig> CREATOR = new a();
    private int a;
    private int b;
    private String c;
    private String d;
    private int e;
    private int f;
    private int g;
    private boolean h;
    private int i;
    private int j;
    private boolean k;

    class a implements Parcelable.Creator<ThirdSlotConfig> {
        a() {
        }

        /* renamed from: a */
        public ThirdSlotConfig createFromParcel(Parcel parcel) {
            return new ThirdSlotConfig(parcel);
        }

        /* renamed from: a */
        public ThirdSlotConfig[] newArray(int i) {
            return new ThirdSlotConfig[i];
        }
    }

    public static final class b {
        /* access modifiers changed from: private */
        public int a;
        /* access modifiers changed from: private */
        public int b;
        /* access modifiers changed from: private */
        public String c;
        /* access modifiers changed from: private */
        public String d;
        /* access modifiers changed from: private */
        public int e;
        /* access modifiers changed from: private */
        public int f;
        /* access modifiers changed from: private */
        public int g;
        /* access modifiers changed from: private */
        public boolean h;
        /* access modifiers changed from: private */
        public int i;
        /* access modifiers changed from: private */
        public int j;

        public b a(String str) {
            this.d = str;
            return this;
        }

        public b b(String str) {
            this.c = str;
            return this;
        }

        public b c(int i2) {
            this.a = i2;
            return this;
        }

        public b d(int i2) {
            this.f = i2;
            return this;
        }

        public b e(int i2) {
            this.b = i2;
            return this;
        }

        public b f(int i2) {
            this.i = i2;
            return this;
        }

        public b g(int i2) {
            this.e = i2;
            return this;
        }

        public b a(boolean z) {
            this.h = z;
            return this;
        }

        public b b(int i2) {
            this.g = i2;
            return this;
        }

        public b a(int i2) {
            this.j = i2;
            return this;
        }

        public ThirdSlotConfig a() {
            return new ThirdSlotConfig(this, (a) null);
        }
    }

    /* synthetic */ ThirdSlotConfig(b bVar, a aVar) {
        this(bVar);
    }

    public void a(int i2) {
        this.b = i2;
    }

    public int b() {
        return this.g;
    }

    public int c() {
        return this.a;
    }

    public int d() {
        return this.f;
    }

    public int describeContents() {
        return 0;
    }

    public String e() {
        return this.d;
    }

    public int f() {
        return this.b;
    }

    public String g() {
        return this.c;
    }

    public int h() {
        return this.i;
    }

    public int i() {
        return this.e;
    }

    public boolean j() {
        return this.k;
    }

    public boolean k() {
        return this.h;
    }

    public String toString() {
        return "cfg{level=" + this.a + ", ss=" + this.b + ", sid='" + this.c + Operators.SINGLE_QUOTE + ", p='" + this.d + Operators.SINGLE_QUOTE + ", w=" + this.e + ", m=" + this.f + ", cpm=" + this.g + ", bdt=" + this.h + ", sto=" + this.i + ", type=" + this.j + Operators.BLOCK_END;
    }

    public void writeToParcel(Parcel parcel, int i2) {
        parcel.writeInt(this.a);
        parcel.writeInt(this.b);
        parcel.writeString(this.c);
        parcel.writeString(this.d);
        parcel.writeInt(this.e);
        parcel.writeInt(this.f);
        parcel.writeInt(this.g);
        parcel.writeByte(this.h ? (byte) 1 : 0);
        parcel.writeInt(this.i);
        parcel.writeInt(this.j);
        parcel.writeByte(this.k ? (byte) 1 : 0);
    }

    private ThirdSlotConfig(b bVar) {
        boolean z = false;
        this.k = false;
        this.a = bVar.a;
        this.b = bVar.b;
        this.c = bVar.c;
        this.d = bVar.d;
        this.e = bVar.e;
        this.f = bVar.f;
        this.g = bVar.g;
        this.h = bVar.h;
        this.i = bVar.i;
        this.j = bVar.j;
        this.k = (this.e > 0 || this.f > 0) ? true : z;
    }

    public int a() {
        return this.j;
    }

    protected ThirdSlotConfig(Parcel parcel) {
        boolean z = false;
        this.k = false;
        this.a = parcel.readInt();
        this.b = parcel.readInt();
        this.c = parcel.readString();
        this.d = parcel.readString();
        this.e = parcel.readInt();
        this.f = parcel.readInt();
        this.g = parcel.readInt();
        this.h = parcel.readByte() != 0;
        this.i = parcel.readInt();
        this.j = parcel.readInt();
        this.k = parcel.readByte() != 0 ? true : z;
    }
}
