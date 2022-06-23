package com.whatscan.toolkit.forwa.GetSet;

import java.io.Serializable;

public class StickerCatagery implements Serializable {

    private String id;
    private String catagery_name;
    private String catagery_json;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatageryName() {
        return catagery_name;
    }

    public void setCatageryName(String catageryName) {
        this.catagery_name = catageryName;
    }

    public String getCatageryJson() {
        return catagery_json;
    }

    public void setCatageryJson(String catageryJson) {
        this.catagery_json = catageryJson;
    }
}
