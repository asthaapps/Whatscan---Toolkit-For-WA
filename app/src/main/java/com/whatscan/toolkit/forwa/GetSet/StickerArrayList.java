package com.whatscan.toolkit.forwa.GetSet;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class StickerArrayList implements Parcelable, Serializable {

    public static final Parcelable.Creator<StickerArrayList> CREATOR = new Parcelable.Creator<StickerArrayList>() {
        @Override
        public StickerArrayList createFromParcel(Parcel in) {
            return new StickerArrayList(in);
        }

        @Override
        public StickerArrayList[] newArray(int size) {
            return new StickerArrayList[size];
        }
    };
    public String id;
    public String catagery_id;
    public String catagery_name;
    public String title_id;
    public String catagery_title;
    public String sticker_image;

    public StickerArrayList() {
    }

    public StickerArrayList(String catageryName, String stickerImage) {
        this.catagery_name = catageryName;
        this.sticker_image = stickerImage;
    }

    public StickerArrayList(String id, String catageryId, String catageryName, String titleId, String stickerImage) {
        this.id = id;
        this.catagery_id = catageryId;
        this.catagery_name = catageryName;
        this.title_id = titleId;
        this.sticker_image = stickerImage;
    }

    protected StickerArrayList(Parcel in) {
        id = in.readString();
        catagery_id = in.readString();
        catagery_name = in.readString();
        title_id = in.readString();
        sticker_image = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatageryId() {
        return catagery_id;
    }

    public void setCatageryId(String catageryId) {
        this.catagery_id = catageryId;
    }

    public String getCatageryName() {
        return catagery_name;
    }

    public void setCatageryName(String catageryName) {
        this.catagery_name = catageryName;
    }

    public String getTitleId() {
        return title_id;
    }

    public void setTitleId(String titleId) {
        this.title_id = titleId;
    }

    public String getCatageryTitle() {
        return catagery_title;
    }

    public void setCatageryTitle(String catageryTitle) {
        this.catagery_title = catageryTitle;
    }

    public String getStickerImage() {
        return sticker_image;
    }

    public void setStickerImage(String stickerImage) {
        this.sticker_image = stickerImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(catagery_id);
        dest.writeString(catagery_name);
        dest.writeString(title_id);
        dest.writeString(sticker_image);
    }
}