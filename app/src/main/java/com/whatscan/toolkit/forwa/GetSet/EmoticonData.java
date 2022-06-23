package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmoticonData {

    @SerializedName("flag")
    @Expose
    private Boolean flag;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Sticker_catagery")
    @Expose
    private List<EmoticonCatagery> stickerCatagery = null;

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<EmoticonCatagery> getStickerCatagery() {
        return stickerCatagery;
    }

    public void setStickerCatagery(List<EmoticonCatagery> stickerCatagery) {
        this.stickerCatagery = stickerCatagery;
    }

}