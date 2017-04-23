package com.sohamfit.sohamfitapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leonardogedler on 4/21/17.
 */

public class Video implements Parcelable {
    public String objectId;
    public String createdAt;
    public String videoName;
    public String videoDescription;
    public String videoLevel;
    public String videoPosterUrl;
    public String videoMp4Url;
    public String videoDuration;


    public Video(){}

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(objectId);
        out.writeString(createdAt);
        out.writeString(videoName);
        out.writeString(videoDescription);
        out.writeString(videoLevel);
        out.writeString(videoPosterUrl);
        out.writeString(videoMp4Url);
        out.writeString(videoDuration);
    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    private Video(Parcel in) {
        objectId = in.readString();
        createdAt = in.readString();
        videoName = in.readString();
        videoDescription = in.readString();
        videoLevel = in.readString();
        videoPosterUrl = in.readString();
        videoMp4Url = in.readString();
        videoDuration = in.readString();
    }
}
