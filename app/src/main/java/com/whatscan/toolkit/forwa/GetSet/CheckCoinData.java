package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckCoinData {
    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("Message")
    @Expose
    private String Message;
    @SerializedName("coins")
    @Expose
    private String coins;
    @SerializedName("u_id")
    @Expose
    private String u_id;
    @SerializedName("firstlogin_status")
    @Expose
    private String firstlogin_status;
    @SerializedName("invitation_status")
    @Expose
    private String invitation_status;
    @SerializedName("tooluse_5min")
    @Expose
    private String tooluse_5min;
    @SerializedName("tooluse_1min")
    @Expose
    private String tooluse_1min;
    @SerializedName("tooluse_10min")
    @Expose
    private String tooluse_10min;
    @SerializedName("tooluse_20min")
    @Expose
    private String tooluse_20min;
    @SerializedName("tooluse_30min")
    @Expose
    private String tooluse_30min;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFirstlogin_status() {
        return firstlogin_status;
    }

    public String getFlag() {
        return flag;
    }

    public String getInvitation_status() {
        return invitation_status;
    }

    public String getTooluse_1min() {
        return tooluse_1min;
    }

    public String getTooluse_5min() {
        return tooluse_5min;
    }

    public String getTooluse_10min() {
        return tooluse_10min;
    }

    public String getTooluse_20min() {
        return tooluse_20min;
    }

    public String getTooluse_30min() {
        return tooluse_30min;
    }

    public void setFirstlogin_status(String firstlogin_status) {
        this.firstlogin_status = firstlogin_status;
    }

    public void setInvitation_status(String invitation_status) {
        this.invitation_status = invitation_status;
    }

    public void setTooluse_1min(String tooluse_1min) {
        this.tooluse_1min = tooluse_1min;
    }

    public void setTooluse_5min(String tooluse_5min) {
        this.tooluse_5min = tooluse_5min;
    }

    public void setTooluse_10min(String tooluse_10min) {
        this.tooluse_10min = tooluse_10min;
    }

    public void setTooluse_20min(String tooluse_20min) {
        this.tooluse_20min = tooluse_20min;
    }

    public void setTooluse_30min(String tooluse_30min) {
        this.tooluse_30min = tooluse_30min;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }
}
