package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CaptionSubData {

    @SerializedName("flag")
    @Expose
    private Boolean flag;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Caption_data")
    @Expose
    private List<CaptionSubCategory> captionData = null;

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

    public List<CaptionSubCategory> getCaptionData() {
        return captionData;
    }

    public void setCaptionData(List<CaptionSubCategory> captionData) {
        this.captionData = captionData;
    }

}
