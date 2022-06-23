package com.whatscan.toolkit.forwa.GetSet;

public class WelcomeReplyModel {
    private String date;
    private String id;
    private String incomingmsg;
    private String name;

    public String getIncomingmsg() {
        return this.incomingmsg;
    }

    public void setIncomingmsg(String str) {
        this.incomingmsg = str;
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

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }
}
