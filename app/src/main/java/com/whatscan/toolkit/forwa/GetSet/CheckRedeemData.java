package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckRedeemData {
    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("u_id")
    @Expose
    private String uId;
    @SerializedName("ads_date")
    @Expose
    private String adsDate;
    @SerializedName("ads_1day")
    @Expose
    private String ads1day;
    @SerializedName("ads_7day")
    @Expose
    private String ads7day;
    @SerializedName("ads_3days")
    @Expose
    private String ads3days;
    @SerializedName("ads_30days")
    @Expose
    private String ads30days;
    @SerializedName("f_date")
    @Expose
    private String fDate;
    @SerializedName("bulksender_1day")
    @Expose
    private String bulksender1day;
    @SerializedName("bulksender_7day")
    @Expose
    private String bulksender7day;
    @SerializedName("autoresponce_1day")
    @Expose
    private String autoresponce1day;
    @SerializedName("autoresponce_7day")
    @Expose
    private String autoresponce7day;
    @SerializedName("importexcel_1day")
    @Expose
    private String importexcel1day;
    @SerializedName("importexcel_7day")
    @Expose
    private String importexcel7day;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getAdsDate() {
        return adsDate;
    }

    public void setAdsDate(String adsDate) {
        this.adsDate = adsDate;
    }

    public String getAds1day() {
        return ads1day;
    }

    public void setAds1day(String ads1day) {
        this.ads1day = ads1day;
    }

    public String getAds7day() {
        return ads7day;
    }

    public void setAds7day(String ads7day) {
        this.ads7day = ads7day;
    }

    public String getAds3days() {
        return ads3days;
    }

    public void setAds3days(String ads3days) {
        this.ads3days = ads3days;
    }

    public String getAds30days() {
        return ads30days;
    }

    public void setAds30days(String ads30days) {
        this.ads30days = ads30days;
    }

    public String getfDate() {
        return fDate;
    }

    public void setfDate(String fDate) {
        this.fDate = fDate;
    }

    public String getBulksender1day() {
        return bulksender1day;
    }

    public void setBulksender1day(String bulksender1day) {
        this.bulksender1day = bulksender1day;
    }

    public String getBulksender7day() {
        return bulksender7day;
    }

    public void setBulksender7day(String bulksender7day) {
        this.bulksender7day = bulksender7day;
    }

    public String getAutoresponce1day() {
        return autoresponce1day;
    }

    public void setAutoresponce1day(String autoresponce1day) {
        this.autoresponce1day = autoresponce1day;
    }

    public String getAutoresponce7day() {
        return autoresponce7day;
    }

    public void setAutoresponce7day(String autoresponce7day) {
        this.autoresponce7day = autoresponce7day;
    }

    public String getImportexcel1day() {
        return importexcel1day;
    }

    public void setImportexcel1day(String importexcel1day) {
        this.importexcel1day = importexcel1day;
    }

    public String getImportexcel7day() {
        return importexcel7day;
    }

    public void setImportexcel7day(String importexcel7day) {
        this.importexcel7day = importexcel7day;
    }
}
