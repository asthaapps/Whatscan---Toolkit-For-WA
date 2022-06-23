package com.whatscan.toolkit.forwa.GetSet;

public class SearchModel {

    public String txtName;
    public int fabImage;
    public Class<?> Act = null;

    public SearchModel(String name, int image, Class<?> act) {
        this.txtName = name;
        this.fabImage = image;
        Act = act;
    }

    public String getTxtName() {
        return txtName;
    }

    public void setTxtName(String txtName) {
        this.txtName = txtName;
    }

    public int getFabImage() {
        return fabImage;
    }

    public void setFabImage(int fabImage) {
        this.fabImage = fabImage;
    }
}
