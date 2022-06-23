package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CaptionSubCategory {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("captioncatagery_id")
    @Expose
    private String captioncatageryId;
    @SerializedName("captioncatagery_name")
    @Expose
    private String captioncatageryName;
    @SerializedName("caption")
    @Expose
    private String caption;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaptioncatageryId() {
        return captioncatageryId;
    }

    public void setCaptioncatageryId(String captioncatageryId) {
        this.captioncatageryId = captioncatageryId;
    }

    public String getCaptioncatageryName() {
        return captioncatageryName;
    }

    public void setCaptioncatageryName(String captioncatageryName) {
        this.captioncatageryName = captioncatageryName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

}
