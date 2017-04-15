package com.eemeliheinonen.gitcheck.models;


import android.os.Parcel;
import android.os.Parcelable;

public class Repository implements Parcelable {

    private String name;

    public String getName() {
        return name;
    }

    ///Methods for Parcelable

    protected Repository(Parcel in) {
        name = in.readString();
    }

    public static final Creator<Repository> CREATOR = new Creator<Repository>() {
        @Override
        public Repository createFromParcel(Parcel in) {
            return new Repository(in);
        }

        @Override
        public Repository[] newArray(int size) {
            return new Repository[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
    }
}
