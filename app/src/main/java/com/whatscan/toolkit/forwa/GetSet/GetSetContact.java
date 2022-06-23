package com.whatscan.toolkit.forwa.GetSet;

public class GetSetContact {
    String acType;
    String name;
    String number;

    public GetSetContact(String str, String str2, String str3) {
        this.name = str;
        this.number = str2;
        this.acType = str3;
    }

    public String getAcType() {
        return this.acType;
    }

    public void setAcType(String str) {
        this.acType = str;
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
