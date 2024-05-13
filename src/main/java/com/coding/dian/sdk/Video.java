package com.coding.dian.sdk;

public class Video {
    private String originContent; // 原始内容  <iframe ... src= ></iframe>
    private String videoAddr ; // 视频文件地址

    public String getOriginContent() {
        return originContent;
    }

    public String getVideoAddr() {
        return videoAddr;
    }

    public void setVideoAddr(String videoAddr) {
        this.videoAddr = videoAddr;
    }

    public void setOriginContent(String originContent) {
        this.originContent = originContent;
    }
}
