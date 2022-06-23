package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmoticonSubData {
    @SerializedName("flag")
    @Expose
    private Boolean flag;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("catagery_array")
    @Expose
    private List<EmoticonSubCatagery> emoticonSubCatagery = null;

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

    public List<EmoticonSubCatagery> getCatageryArray() {
        return emoticonSubCatagery;
    }

    public void setCatageryArray(List<EmoticonSubCatagery> emoticonSubCatagery) {
        this.emoticonSubCatagery = emoticonSubCatagery;
    }

}
