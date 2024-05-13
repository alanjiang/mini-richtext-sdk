# 工具介绍

微信小程序富文本 mp-html 的替代者。

## 背景：



 使用富文本编辑工具（如： vue-editor )在后台编辑含图片、视频、文字的富文本内容时，在小程序端显示却发现视频加载不出来。微信的 rich-text 组件不支持视频文件的渲染。迫使前端使用开源 mp-html (见：https://github.com/jin-yufeng/mp-html)， 但对前端的组件有要求。作者在使用 taro nutUI （小程序跨多端开发框架）时，发现引入mp-html 不生效。于是决定自研SDK工具将富文本中的内容按不含视频标签的普通HTML和视频内容提取出来，这样一来， 一个复杂的富文本内容就标记成一组 “不含视频标签的普通HTML”和“视频内容”的有序集合。



## 工具效果：



###  后台编辑效果：



如：在后端使用 vue-editor 编辑富文本内容：



![](https://coding-2020.s3.cn-northwest-1.amazonaws.com.cn/assets/rich_back.jpg)





富文本（含图片和视频）在数据库中存储的富文本原始格式为：



```
<p>     公司自住研发的智能语音助手无需联网，即插即用，可以使用语音控制家电。因质量可靠，性价比高成为消费者的新宠。</p><p><br></p><p><img src="https://s3.cn-northwest-1.amazonaws.com.cn/coding-2020/media/10000051/360x360/202405012006401fca.jpg"></p><p><br></p><p>来看一下产品的实际效果：</p><p><br></p><iframe class="ql-video" frameborder="0" allowfullscreen="true" src="https://s3.cn-northwest-1.amazonaws.com.cn/coding-2020/10000052/202404270826083cfa.mp4"></iframe><p><br></p>
```



其中： <iframe ... > 为视频标签。（注： 不同富文本编辑器视频标签或许有差别，但不影响工具的扩展性）



我们的SDK算法就是要将“富文本原始格式”转换成一组 Seperator 有序集合。

![](https://coding-2020.s3.cn-northwest-1.amazonaws.com.cn/assets/sdk.svg)





开箱即用，示例如下：

```
public static void main(String[] args) throws Exception{
        String content ="<p>Fomei美暖&nbsp;是联米科技旗下的高端品牌，是中国智能电热围巾，智能电热披肩，智能电热袜等产品的高端品牌。</p><p><br></p><p><img src=\"https://s3.cn-northwest-1.amazonaws.com.cn/coding-2020/media/10000051/360x360/202405012006401fca.jpg\"></p><p><br></p><p>来看一下产品的实际效果：</p><p><br></p><iframe class=\"ql-video\" frameborder=\"0\" allowfullscreen=\"true\" src=\"https://s3.cn-northwest-1.amazonaws.com.cn/coding-2020/10000052/202404270826083cfa.mp4\"></iframe>";
        List<Seperator> seperators = MiniVideoUtils.getSeperators(content); // 视频和HTML对象按顺序输出
        for(Seperator sep : seperators) {

            System.out.println("内容："+sep.getContent()+",类型："+sep.getType());
        }


    }
```



示例调用打印如下：



```
内容：<p>Fomei美暖&nbsp;是联米科技旗下的高端品牌，是中国智能电热围巾，智能电热披肩，智能电热袜等产品的高端品牌。</p><p><br></p><p><img src="https://s3.cn-northwest-1.amazonaws.com.cn/coding-2020/media/10000051/360x360/202405012006401fca.jpg"></p><p><br></p><p>来看一下产品的实际效果：</p><p><br></p>,类型：html
内容：<video src="https://s3.cn-northwest-1.amazonaws.com.cn/coding-2020/10000052/202404270826083cfa.mp4" controls></video>,类型：video

```

说明已依据不同的媒体类型进行了切割。 



### 小程序显示效果



![](https://coding-2020.s3.cn-northwest-1.amazonaws.com.cn/assets/rich_mini.jpg)





从效果上看，进行了正确的文本类型是切割，小程序也正确地渲染出了视频。



### 小程序端使用示范：

其中： seperators 是后端服务返回的Seperator 有序集合， 小程序根据 type == 'html' 是普通的HTML富文本，使用微信小程序  <rich-text ..> 即可渲染内容，对于视频内容，使用 <video ..>标签即可渲染视频。



```
<nut-row v-for="sep in seperators">
         <nut-col :span="24" v-if="sep.type == 'html'">
           <rich-text :nodes="sep.content" style="font-size: 13px; text-indent：2em;padding: 3px;margin: 3px;" />
         </nut-col>
         <nut-col :span="24" v-else>
            <video :src="sep.video_src"
                 initial-time="0"
                :controls="true"
                :autoplay="false"
                :loop="false"
                :muted="false"
             />
          </nut-col>
        </nut-row>
```





## 工具优点：



 由服务端处理，更灵活，效率更高；

 前端不需要集成 mp-html， 减少小程序包体积，提高用户体验。





## 交流方式

 

![](https://coding-2020.s3.cn-northwest-1.amazonaws.com.cn/assets/gzh.jpg)