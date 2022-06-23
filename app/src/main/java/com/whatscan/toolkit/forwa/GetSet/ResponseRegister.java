package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.SerializedName;

public class ResponseRegister {
    @SerializedName("flag")
    private String flag;
    @SerializedName("message")
    private String message;
    @SerializedName("u_id")
    private String u_id;
    @SerializedName("email")
    private String email;


    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}