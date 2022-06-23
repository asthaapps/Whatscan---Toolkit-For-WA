package com.whatscan.toolkit.forwa.GetSet;

public class NameModel {
    private String date;
    private String lastmsg;
    private String name;

    public NameModel(String str, String str2, String str3) {
        this.name = str;
        this.date = str2;
        this.lastmsg = str3;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String str) {
        this.date = str;
    }

    public String getLastmsg() {
        return this.lastmsg;
    }

    public void setLastmsg(String str) {
        this.lastmsg = str;
    }
}
