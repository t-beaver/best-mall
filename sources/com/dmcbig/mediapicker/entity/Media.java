package com.dmcbig.mediapicker.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.taobao.weex.el.parse.Operators;

public class Media implements Parcelable {
    public static final Parcelable.Creator<Media> CREATOR = new Parcelable.Creator<Media>() {
        public Media createFromParcel(Parcel parcel) {
            return new Media(parcel);
        }

        public Media[] newArray(int i) {
            return new Media[i];
        }
    };
    public String extension;
    public int id;
    public int mediaType;
    public String name;
    public String parentDir;
    public String path;
    public long size;
    public long time;

    public Media(String str, String str2, long j, int i, long j2, int i2, String str3) {
        this.path = str;
        this.name = str2;
        if (TextUtils.isEmpty(str2) || str2.indexOf(Operators.DOT_STR) == -1) {
            this.extension = "null";
        } else {
            this.extension = str2.substring(str2.lastIndexOf(Operators.DOT_STR), str2.length());
        }
        this.time = j;
        this.mediaType = i;
        this.size = j2;
        this.id = i2;
        this.parentDir = str3;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.path);
        parcel.writeString(this.name);
        parcel.writeString(this.extension);
        parcel.writeLong(this.time);
        parcel.writeInt(this.mediaType);
        parcel.writeLong(this.size);
        parcel.writeInt(this.id);
        parcel.writeString(this.parentDir);
    }

    protected Media(Parcel parcel) {
        this.path = parcel.readString();
        this.name = parcel.readString();
        this.extension = parcel.readString();
        this.time = parcel.readLong();
        this.mediaType = parcel.readInt();
        this.size = parcel.readLong();
        this.id = parcel.readInt();
        this.parentDir = parcel.readString();
    }
}
