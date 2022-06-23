package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckRedemptionList {
    @SerializedName("flag")
    @Expose
    private Boolean flag;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("u_id")
    @Expose
    private String uId;
    @SerializedName("coupon")
    @Expose
    private String coupon;
    @SerializedName("coin")
    @Expose
    private String coin;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("starting_date")
    @Expose
    private String startingDate;
    @SerializedName("ending_date")
    @Expose
    private String endingDate;
    @SerializedName("expire_day")
    @Expose
    private String expireDay;
    @SerializedName("expire")
    @Expose
    private String expire;

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
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

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
    }

    public String getExpireDay() {
        return expireDay;
    }

    public void setExpireDay(String expireDay) {
        this.expireDay = expireDay;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }
}
