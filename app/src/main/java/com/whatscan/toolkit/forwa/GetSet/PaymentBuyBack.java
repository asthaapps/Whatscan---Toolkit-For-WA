package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentBuyBack {
    @SerializedName("flag")
    @Expose
    private String flag;

    @SerializedName("Message")
    @Expose
    private String Message;

    @SerializedName("Data")
    @Expose
    private List<PlanCheck> Data = null;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public List<PlanCheck> getData() {
        return Data;
    }

    public void setData(List<PlanCheck> data) {
        Data = data;
    }
}
