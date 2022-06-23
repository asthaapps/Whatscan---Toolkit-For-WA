package com.whatscan.toolkit.forwa.GetSet;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable {
    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel parcel) {
            return new Contact(parcel);
        }

        @Override
        public Contact[] newArray(int i) {
            return new Contact[i];
        }
    };

    public String id;
    public String label;
    public String name;
    public String phone;

    public Contact(String str, String str2, String str3, String str4) {
        this.id = str;
        this.name = str2;
        this.phone = str3;
        this.label = str4;
    }

    protected Contact(Parcel parcel) {
        this.id = parcel.readString();
        this.name = parcel.readString();
        this.phone = parcel.readString();
        this.label = parcel.readString();
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return this.name + " : " + this.phone;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.phone);
        parcel.writeString(this.label);
    }
}