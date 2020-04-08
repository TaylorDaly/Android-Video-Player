package com.example.videoplayer.Model;

public class Video {
    private String url = "";
    private String desc = "";

    public Video() {
    }

    public Video(String url, String desc) {
        this.url = url;
        this.desc = desc;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return this.url;
    }

    public String getDesc() {
        return this.desc;
    }
}
