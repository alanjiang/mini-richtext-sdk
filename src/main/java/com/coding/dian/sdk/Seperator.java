package com.coding.dian.sdk;

public class Seperator {
    private String type ; // video|html
    private int start_index ;
    private int end_index ;
    private String content;
    private String video_src;

    public String getType() {
        return type;
    }

    public int getStart_index() {
        return start_index;
    }

    public int getEnd_index() {
        return end_index;
    }

    public String getContent() {
        return content;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStart_index(int start_index) {
        this.start_index = start_index;
    }

    public void setEnd_index(int end_index) {
        this.end_index = end_index;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVideo_src() {
        return video_src;
    }

    public void setVideo_src(String video_src) {
        this.video_src = video_src;
    }
}
