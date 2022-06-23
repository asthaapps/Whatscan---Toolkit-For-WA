package com.whatscan.toolkit.forwa.GetSet;

public class ReplyModel {
    private String id;
    private String incomingmsg;
    private String replymsg;
    private String selectedoption;

    public String getIncomingmsg() {
        return this.incomingmsg;
    }

    public void setIncomingmsg(String str) {
        this.incomingmsg = str;
    }

    public String getReplymsg() {
        return this.replymsg;
    }

    public void setReplymsg(String str) {
        this.replymsg = str;
    }

    public String getSelectedoption() {
        return this.selectedoption;
    }

    public void setSelectedoption(String str) {
        this.selectedoption = str;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }
}
