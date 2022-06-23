package com.whatscan.toolkit.forwa.GetSet;

import java.util.ArrayList;
import java.util.List;

public class otherStatusData {
    List<StatusData> dataList = new ArrayList();
    String othername;

    public otherStatusData(String str, List<StatusData> list) {
        this.othername = str;
        this.dataList = list;
    }

    public String getOthername() {
        return this.othername;
    }

    public void setOthername(String str) {
        this.othername = str;
    }

    public List<StatusData> getDataList() {
        return this.dataList;
    }

    public void setDataList(List<StatusData> list) {
        this.dataList = list;
    }
}
