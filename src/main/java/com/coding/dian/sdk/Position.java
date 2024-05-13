package com.coding.dian.sdk;

// 匹配到视频标签的位置信息
public class Position {
    private int start , end;
    private String video_src; // 视频文件提供src
    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getVideo_src() {
        return video_src;
    }

    public void setVideo_src(String video_src) {
        this.video_src = video_src;
    }
}
