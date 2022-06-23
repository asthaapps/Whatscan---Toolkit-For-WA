package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseDecorativeMagic {

    @SerializedName("flag")
    private String flag;

    @SerializedName("Text")
    private List<GetSetMagic> getTextData;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public List<GetSetMagic> getGetTextData() {
        return getTextData;
    }

    public void setGetTextData(List<GetSetMagic> getTextData) {
        this.getTextData = getTextData;
    }
}
