package com.whatscan.toolkit.forwa.GetSet;

public class EventModel {
    String date;
    String day;
    String id;
    String message;
    String name;
    String packagename;
    int requestcode;
    String status;
    String time;

    public EventModel(String str, String str2, String str3, String str4, String str5, String str6, int i, String str7, String str8) {
        this.id = str;
        this.name = str2;
        this.day = str3;
        this.date = str4;
        this.time = str5;
        this.message = str6;
        this.requestcode = i;
        this.packagename = str7;
        this.status = str8;
    }

    public EventModel() {
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String str) {
        this.status = str;
    }

    public String getDay() {
        return this.day;
    }

    public void setDay(String str) {
        this.day = str;
    }

    public String getPackagename() {
        return this.packagename;
    }

    public void setPackagename(String str) {
        this.packagename = str;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
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

    public String getTime() {
        return this.time;
    }

    public void setTime(String str) {
        this.time = str;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public int getRequestcode() {
        return this.requestcode;
    }

    public void setRequestcode(int i) {
        this.requestcode = i;
    }
}
