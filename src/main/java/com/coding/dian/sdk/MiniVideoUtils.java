package com.coding.dian.sdk;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MiniVideoUtils {

    static String REGEX_IFRAME = "<iframe\\s{1}class=\"ql-video\"\\s{1}frameborder=\"0\"\\s{1}allowfullscreen=\"true\"\\s{1}src=\"[a-zA-Z0-9\\.\\-\\_\\///:]+\"></iframe>";
    static String REGEX_VIDEO_SRC = "src=\"([\\s\\S]*)\"";
    static String REGEX_VIDEO = "<video\\s{1}src=\"([a-zA-Z0-9\\.\\-\\_\\///:]+)\"\\s{1}controls></video>";
    /**
     *
     * @param content: 原始内容
     * @return 转换 <iframe ... src="" ></iframe>
     * <video  :src="video.bucketUrl" controls></video>
     */
    public static List<Video> transTag(String content) {
        List<Video> targets = new ArrayList<>();
        Pattern pattern1 = Pattern.compile(REGEX_IFRAME);
        Matcher matcher1 = pattern1.matcher(content);
        int index = 0;
        while(matcher1.find()){

            String target = matcher1.group();
            //System.out.println("--> start ="+ matcher1.start()+", end ="+matcher1.end());

            //System.out.println("iframe:"+target);
            Video video = new Video();
            video.setOriginContent(target);
            Pattern pattern = Pattern.compile(REGEX_VIDEO_SRC);
            Matcher matcher = pattern.matcher(target);
            if (matcher.find()) {
                target = matcher.group(1);
                System.out.println("video src="+target);
                video.setVideoAddr(target);

                targets.add(video);
            }

        }
        return  targets;

    }

    public static String  replaceVideos(String content) {

        List<Video> videos =  transTag( content);
        if (videos == null || 0 == videos.size()) return content ;
        for(Video video : videos) {
            String newContent = "<video src=\""+video.getVideoAddr()+"\" controls></video>";
            content = content.replace(video.getOriginContent(), newContent);
        }
        return content;

    }


    /**
     *
     * @param
     * @return
     */
    public static  List<Seperator>  getSeperators(String content1) {
        String newContent = replaceVideos(content1);
        List<Seperator> seperators = new ArrayList<>();
        List<Position> posList = new ArrayList<>();
        Pattern pattern = Pattern.compile(REGEX_VIDEO);
        Matcher matcher = pattern.matcher(newContent);
        while (matcher.find()){
            String target = matcher.group();
            String videoSrc = matcher.group(1);
           // System.out.println("-->videoSrc="+videoSrc);
            //System.out.println("target="+target);
            //System.out.println("--> start ="+ matcher.start()+", end ="+matcher.end());
            Position p = new Position();
            p.setStart(matcher.start());
            p.setEnd(matcher.end());
            p.setVideo_src(videoSrc);
            posList.add(p);
        }

        int length = newContent.length(); // 总长度
        if (posList == null || posList.size() == 0) return seperators; // 没有找到视频
        for(int i = 0 ; i < posList.size(); i++) {
            // 每个视频仅处理前面的HTML元素及自身
            Position pos = posList.get(i);// 当前的视频
            //System.out.println("--->当前pos start="+pos.getStart()+",end="+pos.getEnd());
            if ( i == 0 ){
                // 第一个视频元素
                if (3 < pos.getStart() && pos.getStart() < 8 ) { // eg: <p></p>
                    //System.out.println("舍取第一个视频外的HTML元素");
                }else{
                    Seperator  sep = new Seperator();
                    sep.setStart_index(0);
                    sep.setEnd_index( pos.getStart());
                    sep.setType("html");

                    sep.setContent(newContent.substring(sep.getStart_index(),sep.getEnd_index()));
                    //System.out.println("--第1个视频标签前面的HTL元素内容="+sep.getContent()+", start="+sep.getStart_index()+",end="+sep.getEnd_index());
                    seperators.add(sep);
                }
            }else{
                // 不是第一个视频
                Position lastPos = posList.get(i-1);//前一个视频
                if (   pos.getStart() - lastPos.getEnd()  < 8 ) { // eg: <p></p>
                    // 舍取第i个视频外的HTML元素
                }else{
                    Seperator  sep = new Seperator();
                    sep.setStart_index(lastPos.getEnd());
                    sep.setEnd_index( pos.getStart());
                    sep.setType("html");

                    sep.setContent(newContent.substring(sep.getStart_index(),sep.getEnd_index()));
                    //System.out.println("--视频标签前面的HTL元素内容="+sep.getContent()+", start="+sep.getStart_index()+",end="+sep.getEnd_index());
                    seperators.add(sep);
                }
              }
                // 将视频自身加入列表
                Seperator  sep = new Seperator();
                sep.setStart_index(pos.getStart());
                sep.setEnd_index( pos.getEnd());
                sep.setType("video");
                sep.setVideo_src(pos.getVideo_src());
                sep.setContent(newContent.substring(sep.getStart_index(),sep.getEnd_index()));
                //System.out.println("-->第1个视频标签内容="+sep.getContent()+", start="+sep.getStart_index()+",end="+sep.getEnd_index());
                seperators.add(sep);
        } // end of for

        // 处理最后一个视频的后面HTML部分
        Position lastPos = posList.get( posList.size() -1 );
        int gap = length  - lastPos.getEnd();
        if (gap > 8) {

            Seperator sep = new Seperator();
            sep.setStart_index( lastPos.getEnd());
            sep.setEnd_index(length);
            sep.setContent(newContent.substring(sep.getStart_index(), sep.getEnd_index()));
            sep.setType("html");
            seperators.add(sep);
        }

        return seperators;
    }

    public static void main(String[] args) throws Exception{
        String content ="<p>Fomei美暖&nbsp;是联米科技旗下的高端品牌，是中国智能电热围巾，智能电热披肩，智能电热袜等产品的高端品牌。</p><p><br></p><p><img src=\"https://s3.cn-northwest-1.amazonaws.com.cn/coding-2020/media/10000051/360x360/202405012006401fca.jpg\"></p><p><br></p><p>来看一下产品的实际效果：</p><p><br></p><iframe class=\"ql-video\" frameborder=\"0\" allowfullscreen=\"true\" src=\"https://s3.cn-northwest-1.amazonaws.com.cn/coding-2020/10000052/202404270826083cfa.mp4\"></iframe>";
        List<Seperator> seperators = MiniVideoUtils.getSeperators(content); // 视频和HTML对象按顺序输出
        for(Seperator sep : seperators) {

            System.out.println("内容："+sep.getContent()+",类型："+sep.getType());
        }



    }



}


