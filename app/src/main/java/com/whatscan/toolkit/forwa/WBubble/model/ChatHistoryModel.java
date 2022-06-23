package com.whatscan.toolkit.forwa.WBubble.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatHistoryModel {
    String DtTm;
    String txtmsg;

    public String getTxtmsg() {
        return this.txtmsg;
    }

    public void setTxtmsg(String str) {
        this.txtmsg = str;
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
