package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionSubData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("u_id")
    @Expose
    private String u_id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("coin")
    @Expose
    private String coin;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("t_name")
    @Expose
    private String t_name;

    @SerializedName("current_month")
    @Expose
    private String current_month;

    public String getT_name() {
        return t_name;
    }

    public void setT_name(String t_name) {
        this.t_name = t_name;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getU_id() {
        return u_id;
    }

    public String getName() {
        return name;
    }

    public String getCoin() {
        return coin;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getCurrent_month() {
        return current_month;
    }

    public void setCurrent_month(String current_month) {
        this.current_month = current_month;
    }
}
