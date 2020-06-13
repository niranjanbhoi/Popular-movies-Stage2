package com.developer.kimy.popularmovies.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Trailer implements Parcelable {

    @SerializedName("key")
    private String key;
    @SerializedName("site")
    private String site;
    @SerializedName("name")
    private String title;

    public Trailer(String key, String site, String title) {
        this.key = key;
        this.site = site;
        this.title = title;
    }

    public Trailer(Parcel parcel) {
        key = parcel.readString();
        site = parcel.readString();
        title = parcel.readString();
    }

    public String getKey() {
        return key;
    }

    public String getSite() {
        return site;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(site);
        parcel.writeString(title);
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel parcel) {
            return new Trailer(parcel);
        }

        @Override
        public Trailer[] newArray(int i) {
            return new Trailer[i];
        }
    };

}
