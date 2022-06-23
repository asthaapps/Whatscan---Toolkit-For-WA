package com.whatscan.toolkit.forwa.GetSet;

public class ChatModel {
    String message;
    String mtime;

    public ChatModel(String str, String str2) {
        this.message = str;
        this.mtime = str2;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public String getMtime() {
        return this.mtime;
    }
}