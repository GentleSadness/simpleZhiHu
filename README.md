# simpleZhiHu

https://github.com/izzyleung/ZhihuDailyPurify/wiki/%E7%9F%A5%E4%B9%8E%E6%97%A5%E6%8A%A5-API-%E5%88%86%E6%9E%90


1. 使用css进行图片的自适应

在web前端，也就是HTML中，如果只设置图片的宽度，那么高度会根据图片原本尺寸进行缩放。

如果后台返回的HTML代码中，不包含<head>标签，则可以直接在HTML字符串前加上一下面的代码（如果包含<head>，则在<head>标签内部添加）。代码含义是，不管用户以前设置的图片尺寸是多大，都缩放到宽度为320px大小。


<head><style>img{width:320px !important;}</style></head>

img{
 max-width:80%;
 height:auto;
}


http://blog.csdn.net/cndrip/article/details/7753787

http://blog.csdn.net/ameyume/article/details/6528205

http://blog.csdn.net/wangkuifeng0118/article/details/7648618
