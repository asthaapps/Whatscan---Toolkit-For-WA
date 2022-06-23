package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseLogin {
    @SerializedName("flag")
    private String flag;
    @SerializedName("message")
    private String message;
    @SerializedName("tool_id")
    private String tool_id;
    @SerializedName("id")
    private String id;
    @SerializedName("u_id")
    private String u_id;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("type")
    private String type;
    @SerializedName("fcm_id")
    private String fcm_id;
    @SerializedName("android_id")
    private String android_id;
    @SerializedName("google_key")
    private String google_key;
    @SerializedName("facebook_key")
    private String facebook_key;
    @SerializedName("verify_ads")
    private String verify_ads;
    @SerializedName("notification_status")
    private String notification_status;
    @SerializedName("register_token")
    private String register_token;
    @SerializedName("coins")
    private String coins;
    @SerializedName("refered_number")
    private String refered_number;
    @SerializedName("Data")
    @Expose
    private List<PlanCheck> Data = null;

    public String getNotification_status() {
        return notification_status;
    }

    public void setNotification_status(String notification_status) {
        this.notification_status = notification_status;
    }

    public List<PlanCheck> getData() {
        return Data;
    }

    public void setData(List<PlanCheck> data) {
        Data = data;
    }

    public String getTool_id() {
        return tool_id;
    }

    public void setTool_id(String tool_id) {
        this.tool_id = tool_id;
    }

    public String getRegister_token() {
        return register_token;
    }

    public void setRegister_token(String register_token) {
        this.register_token = register_token;
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

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFcm_id() {
        return fcm_id;
    }

    public void setFcm_id(String fcm_id) {
        this.fcm_id = fcm_id;
    }

    public String getAndroid_id() {
        return android_id;
    }

    public void setAndroid_id(String imei2) {
        this.android_id = imei2;
    }

    public String getGoogle_key() {
        return google_key;
    }

    public void setGoogle_key(String google_key) {
        this.google_key = google_key;
    }

    public String getFacebook_key() {
        return facebook_key;
    }

    public void setFacebook_key(String facebook_key) {
        this.facebook_key = facebook_key;
    }

    public String getVerify_ads() {
        return verify_ads;
    }

    public void setVerify_ads(String verify_ads) {
        this.verify_ads = verify_ads;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getRefered_number() {
        return refered_number;
    }

    public void setRefered_number(String refered_number) {
        this.refered_number = refered_number;
    }
}