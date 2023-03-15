package io.dcloud.feature.nativeObj.data;

import android.os.Parcel;
import android.os.Parcelable;
import io.dcloud.common.util.PdrUtil;

public class NativeImageDataItem implements Parcelable {
    public static final Parcelable.Creator<NativeImageDataItem> CREATOR = new Parcelable.Creator<NativeImageDataItem>() {
        public NativeImageDataItem createFromParcel(Parcel parcel) {
            return new NativeImageDataItem(parcel);
        }

        public NativeImageDataItem[] newArray(int i) {
            return new NativeImageDataItem[i];
        }
    };
    public String align = "center";
    public String height = "auto";
    String url = "";
    public String verticalAlign = "middle";
    public String width = "auto";

    public NativeImageDataItem() {
    }

    public int describeContents() {
        return 0;
    }

    public int getHeight(int i, float f) {
        if (!this.height.equals("auto")) {
            return PdrUtil.convertToScreenInt(this.height, i, i, f);
        }
        return -100;
    }

    public String getUrl() {
        return this.url;
    }

    public int getWidth(int i, float f) {
        if (!this.width.equals("auto")) {
            return PdrUtil.convertToScreenInt(this.width, i, i, f);
        }
        return -100;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.url);
        parcel.writeString(this.align);
        parcel.writeString(this.verticalAlign);
        parcel.writeString(this.height);
        parcel.writeString(this.width);
    }

    protected NativeImageDataItem(Parcel parcel) {
        this.url = parcel.readString();
        this.align = parcel.readString();
        this.verticalAlign = parcel.readString();
        this.height = parcel.readString();
        this.width = parcel.readString();
    }
}
