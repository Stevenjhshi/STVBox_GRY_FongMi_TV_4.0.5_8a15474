package com.android.cast.dlna.dmr;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Action carrier delivered to the renderer activity, holds the media url set by the DMC.
 */
public class CastAction implements Parcelable {

    private String currentURI;
    private String currentURIMetaData;

    public CastAction() {
    }

    public CastAction(String currentURI, String currentURIMetaData) {
        this.currentURI = currentURI;
        this.currentURIMetaData = currentURIMetaData;
    }

    protected CastAction(Parcel in) {
        currentURI = in.readString();
        currentURIMetaData = in.readString();
    }

    public String getCurrentURI() {
        return currentURI;
    }

    public void setCurrentURI(String currentURI) {
        this.currentURI = currentURI;
    }

    public String getCurrentURIMetaData() {
        return currentURIMetaData;
    }

    public void setCurrentURIMetaData(String currentURIMetaData) {
        this.currentURIMetaData = currentURIMetaData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(currentURI);
        dest.writeString(currentURIMetaData);
    }

    public static final Creator<CastAction> CREATOR = new Creator<CastAction>() {
        @Override
        public CastAction createFromParcel(Parcel in) {
            return new CastAction(in);
        }

        @Override
        public CastAction[] newArray(int size) {
            return new CastAction[size];
        }
    };
}
