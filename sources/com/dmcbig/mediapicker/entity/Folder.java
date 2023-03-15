package com.dmcbig.mediapicker.entity;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class Folder implements Parcelable {
    public static final Parcelable.Creator<Folder> CREATOR = new Parcelable.Creator<Folder>() {
        public Folder createFromParcel(Parcel parcel) {
            return new Folder(parcel);
        }

        public Folder[] newArray(int i) {
            return new Folder[i];
        }
    };
    public int count;
    ArrayList<Media> medias = new ArrayList<>();
    public String name;

    public Folder(String str) {
        this.name = str;
    }

    public void addMedias(Media media) {
        this.medias.add(media);
    }

    public int describeContents() {
        return 0;
    }

    public ArrayList<Media> getMedias() {
        return this.medias;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeInt(this.count);
        parcel.writeTypedList(this.medias);
    }

    protected Folder(Parcel parcel) {
        this.name = parcel.readString();
        this.count = parcel.readInt();
        this.medias = parcel.createTypedArrayList(Media.CREATOR);
    }
}
