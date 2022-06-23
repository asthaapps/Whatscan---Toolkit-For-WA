package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyAdds {
    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("status")
    @Expose
    private String status;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
