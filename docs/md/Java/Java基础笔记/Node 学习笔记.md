# Node 学习笔记

# Express 框架

第一个Express 

控制台 npm install express 

```javascript
var express = require('express');

var app = express();

app.get('/',(req,resp)=>{
    resp.send('Hello World!');
});

app.listen(8080,()=>
    console.log('服务器启动了。。。。http://127.0.0.1:8080/'));

```

本地浏览器访问 http://127.0.0.1:8080/  查看效果。