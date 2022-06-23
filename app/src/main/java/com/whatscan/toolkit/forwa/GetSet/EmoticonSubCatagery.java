package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmoticonSubCatagery {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("emojicatagery_id")
    @Expose
    private String emojicatageryId;
    @SerializedName("emojicatagery_name")
    @Expose
    private String emojicatageryName;
    @SerializedName("emoji")
    @Expose
    private String emoji;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmojicatageryId() {
        return emojicatageryId;
    }

    public void setEmojicatageryId(String emojicatageryId) {
        this.emojicatageryId = emojicatageryId;
    }

    public String getEmojicatageryName() {
        return emojicatageryName;
    }

    public void setEmojicatageryName(String emojicatageryName) {
        this.emojicatageryName = emojicatageryName;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

}
