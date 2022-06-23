package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class StickerData implements Serializable {
    @SerializedName("flag")
    @Expose
    private String flag;

    @SerializedName("Sticker_data")
    @Expose
    private List<StickerArray> Sticker_data;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public List<StickerArray> getGetStickerArrays() {
        return Sticker_data;
    }

    public void setGetStickerArrays(List<StickerArray> getStickerArrays) {
        this.Sticker_data = getStickerArrays;
    }
}
