package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.SerializedName;

public class verifyEmail {

    @SerializedName("u_id")
    public String u_id;
    @SerializedName("flag")
    private String flag;
    @SerializedName("email")
    private String email;
    @SerializedName("message")
    private String message;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String uId) {
        this.u_id = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
