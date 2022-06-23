package com.whatscan.toolkit.forwa.GetSet;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class StickerArray implements  Serializable {
    public String id;
    public String catagery_title;
    public String catagery_image;
    public String publisher_name;
    public List<StickerArrayList> Sticker_array;


    public StickerArray(String id, String catageryTitle, String publisher, String catageryImage) {
        this.id = id;
        this.catagery_title = catageryTitle;
        this.publisher_name = publisher;
        this.catagery_image = catageryImage;
    }

    protected StickerArray(Parcel in) {
        id = in.readString();
        catagery_title = in.readString();
        publisher_name = in.readString();
        catagery_image = in.readString();
        Sticker_array = in.createTypedArrayList(StickerArrayList.CREATOR);
    }


    public List<StickerArrayList> getStickers() {
        return Sticker_array;
    }

    public void setStickers(List<StickerArrayList> stickers) {
        this.Sticker_array = stickers;
    }

    public String getPublisher() {
        return publisher_name;
    }

    public void setPublisher(String publisher) {
        this.publisher_name = publisher;
    }

    public String getCatageryTitle() {
        return catagery_title;
    }

    public void setCatageryTitle(String catageryTitle) {
        this.catagery_title = catageryTitle;
    }

    public String getCatageryImage() {
        return catagery_image;
    }

    public void setCatageryImage(String catageryImage) {
        this.catagery_image = catageryImage;
    }

   /* @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(catagery_title);
        dest.writeString(publisher_name);
        dest.writeString(catagery_image);
        dest.writeTypedList(Sticker_array);
    }*/
}