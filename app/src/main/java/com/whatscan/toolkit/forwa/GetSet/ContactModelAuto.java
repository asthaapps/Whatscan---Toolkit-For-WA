package com.whatscan.toolkit.forwa.GetSet;

public class ContactModelAuto {
    public String id;
    public String name;
    public String number;

    public ContactModelAuto() {
    }

    public ContactModelAuto(String str, String str2) {
        this.name = str;
        this.number = str2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String str) {
        this.number = str;
    }
}
