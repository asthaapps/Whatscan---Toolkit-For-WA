package com.whatscan.toolkit.forwa.GetSet;

public class StatusData {
    String imageurl;
    String name;
    String profileurl;
    String text;
    String timeanddate;
    String videourl;
    String view;
    int viewedcompleted = 0;

    public StatusData(String str, String str2, String str3, String str4, String str5, String str6, String str7, int i) {
        name = str;
        timeanddate = str2;
        text = str3;
        view = str4;
        profileurl = str5;
        imageurl = str6;
        videourl = str7;
        viewedcompleted = i;
    }

    public int getViewedcompleted() {
        return viewedcompleted;
    }

    public void setViewedcompleted(int i) {
        viewedcompleted = i;
    }

    public String getName() {
        return name;
    }

    public void setName(String str) {
        name = str;
    }

    public String getTimeanddate() {
        return timeanddate;
    }

    public void setTimeanddate(String str) {
        timeanddate = str;
    }

    public String getText() {
        return text;
    }

    public void setText(String str) {
        text = str;
    }

    public String getView() {
        return view;
    }

    public void setView(String str) {
        view = str;
    }

    public String getProfileurl() {
        return profileurl;
    }

    public void setProfileurl(String str) {
        profileurl = str;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String str) {
        imageurl = str;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String str) {
        videourl = str;
    }
}
