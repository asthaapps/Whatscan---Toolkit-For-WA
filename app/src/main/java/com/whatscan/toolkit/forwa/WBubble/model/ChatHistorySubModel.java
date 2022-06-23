package com.whatscan.toolkit.forwa.WBubble.model;

import android.graphics.Bitmap;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatHistorySubModel {
    private String DtTm;
    private Bitmap imaBitmap;
    private String name;
    private String txtmsg;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getTxtmsg() {
        return this.txtmsg;
    }

    public void setTxtmsg(String str) {
        this.txtmsg = str;
    }

    public Bitmap getImage() {
        return this.imaBitmap;
    }

    public void setImage(Bitmap bitmap) {
        this.imaBitmap = bitmap;
    }

    public String getDtTm() {
        return this.DtTm;
    }

    public void setDtTm(String str) {
        String substring = new SimpleDateFormat("dd-MM-yy hh:mm a").format((Object) new Date(System.currentTimeMillis())).substring(0, 5);
        String substring2 = str.substring(0, 5);
        String substring3 = str.substring(9, str.length());
        if (substring.equals(substring2)) {
            str = "Today " + substring3;
        }
        this.DtTm = str;
    }
}
