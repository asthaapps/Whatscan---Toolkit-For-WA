package com.whatscan.toolkit.forwa.GetSet;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.whatscan.toolkit.forwa.DataBaseHelper.db.DatabaseTableName;

@Entity(tableName = DatabaseTableName.WHATSAPP_NOTIFICATION_CHAT)
public class WhatsAppChat implements Parcelable {

    public static final Creator<WhatsAppChat> CREATOR = new Creator<WhatsAppChat>() {
        @Override
        public WhatsAppChat createFromParcel(Parcel parcel) {
            return new WhatsAppChat(parcel);
        }

        @Override
        public WhatsAppChat[] newArray(int i) {
            return new WhatsAppChat[i];
        }
    };
    String a;
    String b;
    String c;
    String d;
    String e;
    @PrimaryKey(autoGenerate = true)
    int f;
    int g;
    boolean h;

    protected WhatsAppChat(Parcel parcel) {
        this.a = parcel.readString();
        this.b = parcel.readString();
        this.c = parcel.readString();
        this.d = parcel.readString();
        this.e = parcel.readString();
        this.f = parcel.readInt();
        this.g = parcel.readInt();
        this.h = parcel.readByte() != 0;
    }

    public int describeContents() {
        return 0;
    }

    public String getContactId() {
        return this.e;
    }

    public void setContactId(String str) {
        this.e = str;
    }

    public String getCountryCode() {
        return this.c;
    }

    public void setCountryCode(String str) {
        this.c = str;
    }

    public int getId() {
        return this.f;
    }

    public void setId(int i) {
        this.f = i;
    }

    public int getKey() {
        return this.g;
    }

    public void setKey(int i) {
        this.g = i;
    }

    public String getName() {
        return this.a;
    }

    public void setName(String str) {
        this.a = str;
    }

    public String getPhoneNumber() {
        return this.b;
    }

    public void setPhoneNumber(String str) {
        this.b = str;
    }

    public String getPhotoUri() {
        return this.d;
    }

    public void setPhotoUri(String str) {
        this.d = str;
    }

    public boolean isSelected() {
        return this.h;
    }

    public void setSelected(boolean z) {
        this.h = z;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.a);
        parcel.writeString(this.b);
        parcel.writeString(this.c);
        parcel.writeString(this.d);
        parcel.writeString(this.e);
        parcel.writeInt(this.f);
        parcel.writeInt(this.g);
        parcel.writeByte(this.h ? (byte) 1 : 0);
    }
}