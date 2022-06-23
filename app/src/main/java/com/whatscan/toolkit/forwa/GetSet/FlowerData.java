package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FlowerData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("catagery_title")
    @Expose
    private String catageryTitle;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatageryTitle() {
        return catageryTitle;
    }

    public void setCatageryTitle(String catageryTitle) {
        this.catageryTitle = catageryTitle;
    }

}
