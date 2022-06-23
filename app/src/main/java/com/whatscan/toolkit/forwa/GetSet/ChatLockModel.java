package com.whatscan.toolkit.forwa.GetSet;

public class ChatLockModel {
    String AppName;
    int id;
    int isLock;
    int isToCheckLock;
    String username;

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public int getIsLock() {
        return this.isLock;
    }

    public void setIsLock(int i) {
        this.isLock = i;
    }

    public int getIsToCheckLock() {
        return this.isToCheckLock;
    }

    public void setIsToCheckLock(int i) {
        this.isToCheckLock = i;
    }

    public String getAppName() {
        return this.AppName;
    }

    public void setAppName(String str) {
        this.AppName = str;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String str) {
        this.username = str;
    }
}
