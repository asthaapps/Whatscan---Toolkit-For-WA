package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckRedemption {
    @SerializedName("flag")
    @Expose
    private Boolean flag;
    @SerializedName("Data")
    @Expose
    private List<CheckRedemptionList> data = null;

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public List<CheckRedemptionList> getData() {
        return data;
    }

    public void setData(List<CheckRedemptionList> data) {
        this.data = data;
    }
}
