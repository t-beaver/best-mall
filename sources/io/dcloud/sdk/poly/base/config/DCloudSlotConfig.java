package io.dcloud.sdk.poly.base.config;

import android.os.Parcel;
import android.os.Parcelable;
import androidtranscoder.format.MediaFormatExtraConstants;
import com.facebook.common.callercontext.ContextChain;
import com.nostra13.dcloudimageloader.core.download.BaseImageDownloader;
import com.taobao.weex.ui.component.WXComponent;
import io.dcloud.sdk.poly.base.config.ThirdSlotConfig;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class DCloudSlotConfig implements Parcelable {
    public static final Parcelable.Creator<DCloudSlotConfig> CREATOR = new a();
    private String a;
    private int b;
    private int c;
    private int d;
    private List<ThirdSlotConfig> e;
    private int f = 0;
    private boolean g = false;
    private boolean h = false;
    private final List<String> i = new ArrayList();

    class a implements Parcelable.Creator<DCloudSlotConfig> {
        a() {
        }

        /* renamed from: a */
        public DCloudSlotConfig createFromParcel(Parcel parcel) {
            return new DCloudSlotConfig(parcel);
        }

        /* renamed from: a */
        public DCloudSlotConfig[] newArray(int i) {
            return new DCloudSlotConfig[i];
        }
    }

    public DCloudSlotConfig() {
    }

    public DCloudSlotConfig a(JSONObject jSONObject) {
        this.a = jSONObject.optString("adpid");
        this.b = jSONObject.optInt("type", -1);
        int optInt = jSONObject.optInt("tto", 18000);
        this.c = optInt;
        if (optInt < 1000) {
            this.c = 18000;
        }
        int optInt2 = jSONObject.optInt("dsto", BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT);
        this.d = optInt2;
        if (optInt2 < 1000) {
            this.d = BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
        }
        this.f = jSONObject.optInt("sr", 0);
        this.g = jSONObject.optInt("ord", 0) == 1;
        JSONArray optJSONArray = jSONObject.optJSONArray("cfgs");
        if (optJSONArray != null && optJSONArray.length() > 0) {
            if (this.e == null) {
                this.e = new ArrayList();
            }
            for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                JSONObject optJSONObject = optJSONArray.optJSONObject(i2);
                if (optJSONObject != null) {
                    int optInt3 = optJSONObject.has("sto") ? optJSONObject.optInt("sto", BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT) : this.d;
                    if (optInt3 < 1000) {
                        optInt3 = BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
                    }
                    ThirdSlotConfig a2 = new ThirdSlotConfig.b().b(optJSONObject.optString("sid")).b(optJSONObject.optInt("cpm", -1)).a(optJSONObject.optInt("bdt", 0) == 1).c(optJSONObject.optInt(MediaFormatExtraConstants.KEY_LEVEL, -1)).d(optJSONObject.optInt(WXComponent.PROP_FS_MATCH_PARENT, -1)).a(optJSONObject.optString(ContextChain.TAG_PRODUCT)).e(optJSONObject.optInt("ss", -1)).f(optInt3).g(optJSONObject.optInt(WXComponent.PROP_FS_WRAP_CONTENT, -1)).a(this.b).a();
                    if (!this.h) {
                        this.h = a2.j();
                    }
                    this.i.add(a2.e());
                    this.e.add(a2);
                }
            }
        }
        return this;
    }

    public List<String> b() {
        return this.i;
    }

    public int c() {
        return this.f;
    }

    public List<ThirdSlotConfig> d() {
        return this.e;
    }

    public int describeContents() {
        return 0;
    }

    public int e() {
        return this.c;
    }

    public int f() {
        return this.b;
    }

    public boolean g() {
        return this.g;
    }

    public boolean h() {
        return this.h;
    }

    public void writeToParcel(Parcel parcel, int i2) {
        parcel.writeString(this.a);
        parcel.writeInt(this.b);
        parcel.writeInt(this.c);
        parcel.writeInt(this.d);
        parcel.writeTypedList(this.e);
        parcel.writeInt(this.f);
    }

    protected DCloudSlotConfig(Parcel parcel) {
        this.a = parcel.readString();
        this.b = parcel.readInt();
        this.c = parcel.readInt();
        this.d = parcel.readInt();
        this.e = parcel.createTypedArrayList(ThirdSlotConfig.CREATOR);
        this.f = parcel.readInt();
    }

    public String a() {
        return this.a;
    }
}
