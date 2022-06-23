package com.whatscan.toolkit.forwa.GetSet;

public class Details {

    private String title;
    private String data;
    private int image;
    private int color;
    private String path;

    public Details(String title, String data, String path, int image, int color) {
        this.title = title;
        this.data = data;
        this.image = image;
        this.color = color;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public String getData() {
        return data;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getPath() {
        return path;
    }
}
