package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdFreeData {
    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("reedem")
    @Expose
    private Boolean reedem;
    @SerializedName("coin")
    @Expose
    private String coin;
    @SerializedName("status")
    @Expose
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Boolean getReedem() {
        return reedem;
    }

    public void setReedem(Boolean reedem) {
        this.reedem = reedem;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}