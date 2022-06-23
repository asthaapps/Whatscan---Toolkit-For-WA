package com.whatscan.toolkit.forwa.BulkSender.helper;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.whatscan.toolkit.forwa.DataBaseHelper.db.DatabaseTableName;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = DatabaseTableName.PHONE_CONTACT_TABLE)
public class ContactModel implements Parcelable {

    public static final Creator<ContactModel> CREATOR = new Creator<ContactModel>() {
        @Override
        public ContactModel createFromParcel(Parcel parcel) {
            return new ContactModel(parcel);
        }

        @Override
        public ContactModel[] newArray(int i) {
            return new ContactModel[i];
        }
    };

    public String name;
    public String phoneNumber;
    public String countryCode;
    public String photoUri;
    public String contactId;
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int key;
    public boolean isSelected;

    protected ContactModel(Parcel parcel) {
        this.name = parcel.readString();
        this.phoneNumber = parcel.readString();
        this.countryCode = parcel.readString();
        this.photoUri = parcel.readString();
        this.contactId = parcel.readString();
        this.id = parcel.readInt();
        this.key = parcel.readInt();
        this.isSelected = parcel.readByte() != 0;
    }

    public ContactModel() {
    }

    public ContactModel(@NotNull String str, @NotNull String str2, @NotNull String str3) {
        this.countryCode = str;
        this.phoneNumber = str2;
        this.name = str3;
    }

    public int describeContents() {
        return 0;
    }

    public String getContactId() {
        return this.contactId;
    }

    public void setContactId(String str) {
        this.contactId = str;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String str) {
        this.countryCode = str;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int i) {
        this.key = i;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String str) {
        this.phoneNumber = str;
    }

    public String getPhotoUri() {
        return this.photoUri;
    }

    public void setPhotoUri(String str) {
        this.photoUri = str;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.phoneNumber);
        parcel.writeString(this.countryCode);
        parcel.writeString(this.photoUri);
        parcel.writeString(this.contactId);
        parcel.writeInt(this.id);
        parcel.writeInt(this.key);
        parcel.writeByte(this.isSelected ? (byte) 1 : 0);
    }
}