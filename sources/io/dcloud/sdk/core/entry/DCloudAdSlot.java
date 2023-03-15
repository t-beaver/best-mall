package io.dcloud.sdk.core.entry;

import org.json.JSONObject;

public class DCloudAdSlot {
    private String a;
    private String b;
    private String c;
    private JSONObject d;
    private int e;
    private boolean f;
    private int g;
    private String h;
    private int i;
    private int j;
    private int k;

    public static final class Builder {
        /* access modifiers changed from: private */
        public String a;
        /* access modifiers changed from: private */
        public String b;
        /* access modifiers changed from: private */
        public String c;
        /* access modifiers changed from: private */
        public int d = 3;
        /* access modifiers changed from: private */
        public int e;
        /* access modifiers changed from: private */
        public int f;
        /* access modifiers changed from: private */
        public boolean g = false;
        /* access modifiers changed from: private */
        public int h = 1;
        /* access modifiers changed from: private */
        public String i = "";

        public Builder adpid(String str) {
            this.a = str;
            return this;
        }

        public DCloudAdSlot build() {
            return new DCloudAdSlot(this);
        }

        public Builder count(int i2) {
            this.d = i2;
            return this;
        }

        public Builder extra(String str) {
            this.c = str;
            return this;
        }

        public Builder height(int i2) {
            this.f = i2;
            return this;
        }

        public Builder setEI(String str) {
            this.i = str;
            return this;
        }

        public Builder setVideoSoundEnable(boolean z) {
            this.g = z;
            return this;
        }

        public Builder userId(String str) {
            this.b = str;
            return this;
        }

        public Builder width(int i2) {
            this.e = i2;
            return this;
        }
    }

    public String getAdpid() {
        return this.a;
    }

    public JSONObject getConfig() {
        return this.d;
    }

    public int getCount() {
        return this.e;
    }

    public String getEI() {
        return this.h;
    }

    public String getExtra() {
        return this.c;
    }

    public int getHeight() {
        return this.j;
    }

    public int getOrientation() {
        return this.g;
    }

    public int getType() {
        return this.k;
    }

    public String getUserId() {
        return this.b;
    }

    public int getWidth() {
        return this.i;
    }

    public boolean isVideoSoundEnable() {
        return this.f;
    }

    public void setAdpid(String str) {
        this.a = str;
    }

    public void setConfig(JSONObject jSONObject) {
        this.d = jSONObject;
    }

    public void setType(int i2) {
        this.k = i2;
    }

    private DCloudAdSlot(Builder builder) {
        this.g = 1;
        this.k = -1;
        this.a = builder.a;
        this.b = builder.b;
        this.c = builder.c;
        this.e = Math.min(builder.d, 3);
        this.i = builder.e;
        this.j = builder.f;
        this.g = builder.h;
        this.f = builder.g;
        this.h = builder.i;
    }
}
