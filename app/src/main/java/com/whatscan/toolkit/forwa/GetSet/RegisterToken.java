package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterToken {
    @SerializedName("flag")
    @Expose
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
