package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ToolUseCoin {
    @SerializedName("flag")
    @Expose
    private String flag;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("coins")
    @Expose
    private String coins;

    @SerializedName("status")
    @Expose
    private String status;

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

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
