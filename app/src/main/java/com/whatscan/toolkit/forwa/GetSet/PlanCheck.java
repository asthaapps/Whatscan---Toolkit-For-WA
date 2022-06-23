package com.whatscan.toolkit.forwa.GetSet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlanCheck {

    @SerializedName("u_id")
    @Expose
    private String u_id;

    @SerializedName("plan_active")
    @Expose
    private String plan_active;

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getPlan_active() {
        return plan_active;
    }

    public void setPlan_active(String plan_active) {
        this.plan_active = plan_active;
    }
}
