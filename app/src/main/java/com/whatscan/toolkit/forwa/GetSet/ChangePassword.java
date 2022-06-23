package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChangePassword {
    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("u_id")
    @Expose
    private String uId;
    @SerializedName("tool_id")
    @Expose
    private String toolId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("fcm_id")
    @Expose
    private String fcmId;
    @SerializedName("android_id")
    @Expose
    private String androidId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("facebook_key")
    @Expose
    private String facebookKey;
    @SerializedName("google_key")
    @Expose
    private String googleKey;
    @SerializedName("verify_ads")
    @Expose
    private String verifyAds;
    @SerializedName("plan_active")
    @Expose
    private String planActive;
    @SerializedName("refered_number")
    @Expose
    private String referedNumber;
    @SerializedName("coins")
    @Expose
    private String coins;
    @SerializedName("notification_status")
    @Expose
    private String notificationStatus;
    @SerializedName("register_token")
    @Expose
    private String registerToken;
    @SerializedName("removeads")
    @Expose
    private String removeads;
    @SerializedName("basic")
    @Expose
    private String basic;
    @SerializedName("classic")
    @Expose
    private String classic;
    @SerializedName("primum")
    @Expose
    private String primum;
    @SerializedName("master")
    @Expose
    private String master;
    @SerializedName("Data")
    @Expose
    private List<PlanCheck> Data = null;

    public List<PlanCheck> getData() {
        return Data;
    }

    public void setData(List<PlanCheck> data) {
        Data = data;
    }

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

    public String getToolId() {
        return toolId;
    }

    public void setToolId(String toolId) {
        this.toolId = toolId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFcmId() {
        return fcmId;
    }

    public void setFcmId(String fcmId) {
        this.fcmId = fcmId;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFacebookKey() {
        return facebookKey;
    }

    public void setFacebookKey(String facebookKey) {
        this.facebookKey = facebookKey;
    }

    public String getGoogleKey() {
        return googleKey;
    }

    public void setGoogleKey(String googleKey) {
        this.googleKey = googleKey;
    }

    public String getVerifyAds() {
        return verifyAds;
    }

    public void setVerifyAds(String verifyAds) {
        this.verifyAds = verifyAds;
    }

    public String getPlanActive() {
        return planActive;
    }

    public void setPlanActive(String planActive) {
        this.planActive = planActive;
    }

    public String getReferedNumber() {
        return referedNumber;
    }

    public void setReferedNumber(String referedNumber) {
        this.referedNumber = referedNumber;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(String notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public String getRegisterToken() {
        return registerToken;
    }

    public void setRegisterToken(String registerToken) {
        this.registerToken = registerToken;
    }

    public String getRemoveads() {
        return removeads;
    }

    public void setRemoveads(String removeads) {
        this.removeads = removeads;
    }

    public String getBasic() {
        return basic;
    }

    public void setBasic(String basic) {
        this.basic = basic;
    }

    public String getClassic() {
        return classic;
    }

    public void setClassic(String classic) {
        this.classic = classic;
    }

    public String getPrimum() {
        return primum;
    }

    public void setPrimum(String primum) {
        this.primum = primum;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }
}