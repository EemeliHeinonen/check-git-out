package com.eemeliheinonen.gitcheck.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Commit implements Parcelable {

    @SerializedName("commit")
    private CommitData commitData;

    private Author author;

    public CommitData getCommitData() {
        return commitData;
    }

    public Author getAuthor() {
        return author;
    }


    //// Methods for Parcelable

    protected Commit(Parcel in) {
    }

    public static final Creator<Commit> CREATOR = new Creator<Commit>() {
        @Override
        public Commit createFromParcel(Parcel in) {
            return new Commit(in);
        }

        @Override
        public Commit[] newArray(int size) {
            return new Commit[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
