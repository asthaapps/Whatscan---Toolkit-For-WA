package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VeryfyPlan {

    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Data")
    @Expose
    private List<VeryfyData> data = null;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<VeryfyData> getData() {
        return data;
    }

    public void setData(List<VeryfyData> data) {
        this.data = data;
    }

}
