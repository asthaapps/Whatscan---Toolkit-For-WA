package com.whatscan.toolkit.forwa.GetSet;

import android.os.Parcel;
import android.os.Parcelable;

public class QuickReply implements Parcelable {

    public static final Creator<QuickReply> CREATOR = new Creator<QuickReply>() {
        @Override
        public QuickReply createFromParcel(Parcel parcel) {
            return new QuickReply(parcel);
        }

        @Override
        public QuickReply[] newArray(int i) {
            return new QuickReply[i];
        }
    };
    private String contactName;
    private int id;
    private String message;
    private String phoneNumber;

    public QuickReply(int i, String str, String str2) {
        this.id = i;
        this.message = str;
        this.phoneNumber = str2;
    }

    public QuickReply() {
    }

    protected QuickReply(Parcel parcel) {
        this.id = parcel.readInt();
        this.message = parcel.readString();
        this.phoneNumber = parcel.readString();
        this.contactName = parcel.readString();
    }

    public int describeContents() {
        return 0;
    }

    public String getContactName() {
        return this.contactName;
    }

    public void setContactName(String str) {
        this.contactName = str;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String str) {
        this.phoneNumber = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.message);
        parcel.writeString(this.phoneNumber);
        parcel.writeString(this.contactName);
    }
}