package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CaptionData {
    @SerializedName("flag")
    @Expose
    private Boolean flag;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Caption_catagery")
    @Expose
    private List<CaptionCatagery> captionCatagery = null;

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

    public List<CaptionCatagery> getCaptionCatagery() {
        return captionCatagery;
    }

    public void setCaptionCatagery(List<CaptionCatagery> captionCatagery) {
        this.captionCatagery = captionCatagery;
    }

}
