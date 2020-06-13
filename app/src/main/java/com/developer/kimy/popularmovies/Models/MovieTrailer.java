package com.developer.kimy.popularmovies.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieTrailer implements Parcelable {

    @SerializedName("results")
    private List<Trailer> Trailers;

    public MovieTrailer(List<Trailer> Trailers) {
        this.Trailers = Trailers;
    }

    public MovieTrailer(Parcel parcel) {
        parcel.readTypedList(Trailers, Trailer.CREATOR);
    }

    public List<Trailer> getTrailers() {
        return Trailers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(Trailers);
    }

    public static final Creator<MovieTrailer> CREATOR = new Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel parcel) {
            return new MovieTrailer(parcel);
        }

        @Override
        public MovieTrailer[] newArray(int i) {
            return new MovieTrailer[i];
        }
    };
}