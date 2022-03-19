# Ajax 学习笔记

**什么是Ajax？**

Asynchrinous JavaScrit And XML 异步的JS 和xml 

可以在浏览器中发送请求无需刷新页面。

优点： 可以无需刷新页面与服务端进行通信。

允许用户事件更新部分页面

缺点：没有浏览历史。不能回退。







**XML 可扩展标记语言**。

用来传输和存储数据。和HTML 类似。

不同的是HTML　都是预定义标签，

XML　中全是自定义标签。

xml 现在已经被json 字符串代替传输数据问题。

**HTTP (Hypertext Transport Protocol) 超文本传输协义。**

## 

 #  使用node Express  创建一个服务器

**1.先创建一个文件夹，搭建一个项目 ，生成packgage.json文件**

相关命令

创建app 文件夹

在app目录下运行 npm init

2. **安装express**

npm i express

3. **新建一个 app.js 文件**

```javascript

//1. 引入express
const express = require('express');
//2. 创建应用对象

const app = express();

//3. 创建路由规则
app.get('/', (request, response) => {
    response.send("Hello Express");

});

//4. 监听端口启动服务

app.listen(8000, () => {
    console.log("服务已经启动，8080端口监听中....");
});
```

**4.命令行启动 ： node app.js ,然后访问：localhost:8000**

