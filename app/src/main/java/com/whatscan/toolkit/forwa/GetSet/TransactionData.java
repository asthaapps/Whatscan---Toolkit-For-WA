package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionData {
    @SerializedName("flag")
    @Expose
    private boolean flag;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("Data")
    @Expose
    private List<TransactionSubData> transactionSubList = null;

    public boolean getFlag(){return flag;}

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public List<TransactionSubData> getTransactionSubList() {
        return transactionSubList;
    }

    public void setTransactionSubList(List<TransactionSubData> transactionSubList) {
        this.transactionSubList = transactionSubList;
    }
}
