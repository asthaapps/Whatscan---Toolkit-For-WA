package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MainStickerCategery implements Serializable {

    @SerializedName("flag")
    @Expose
    private Boolean flag;

    @SerializedName("Message")
    @Expose
    private String message;

    @SerializedName("Sticker_catagery")
    @Expose
    private List<StickerCatagery> stickerCatagery = null;

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

    public List<StickerCatagery> getStickerCatagery() {
        return stickerCatagery;
    }

    public void setStickerCatagery(List<StickerCatagery> stickerCatagery) {
        this.stickerCatagery = stickerCatagery;
    }

}
