# HTML 学习笔记

## HTML ：
是超文本标记语言，不用解释，直接可以在浏览器页面显示。

##  第一个HTML 页面

```html
<!DOCTYPE HTML>
<html>
    <head>
        <meta charset = "utf-8">
        <title>Html 简介</title>
    </head>
    <body>
        HTML
    </body>
</html>
```

##  常用标签
```html
<!DOCTYPE HTML>
<html>
    <head>

        <meta charset = "utf-8">
        <title>Html 简介</title>
    </head>
    <body>HTML
    <p>这是一个普通的段落</p>
    <p>I love you</p>
    <a href="https://www.baidu.com">这是一个链接a 标签</a>
<!--常见单标签-->
    <br/> 
    <hr/>
    <meta/>
    <img>
    <!--标题-->
    <h1>标题一<h1>
    <h2>标题二<h2>
    <h3>标题三<h3>
    <h4>标题四<h4>
    <h5>标题五<h5>
    <h6>标题六<h6>
    <!--图片地址-->
    <img src="https://img-pre.ivsky.com/img/tupian/pre/202103/01/sunyunzhu_changxiushan-001.jpg"/>

    <!--块级元素div-->
    <div style="width:200px;height:200px;background:pink">块级元素div</div>
    <!--换行标签-->
    <br>
    <p>这是一段文字      前面有很多空格但是只显示一个</p>
    <p>这是一段文字&nbsp;&nbsp;&nbsp;&nbsp; "&\nbsp;着但是只显示一个</p>
    <br>
    <p>上面是换行符</p>
    <!--水平分割线-->
    <hr/>

    <p>容器标签 div 和 span</p>
    
    </body>
    
</html>
```

## 样式

```html
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8"/>
        <title>样式</title>
    </head>

    <body>
        <p style="background-color:red">这是一个段落，背景颜色为红色</p>

        <p style="font-family:arial;color:blue;font-size:50px;">设置字体，颜色，和尺寸</p>
        <h1 style="text-align:center">设置文本对齐，标题1居中</h1>
    </body>
</html>
```

## 列表

```html
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8"/>
        <title>样式</title>
    </head>

    <body>
        <p style="background-color:red">这是一个段落，背景颜色为红色</p>

        <p style="font-family:arial;color:blue;font-size:50px;">设置字体，颜色，和尺寸</p>
        <h1 style="text-align:center">设置文本对齐，标题1居中</h1>
    </body>
</html>
```

## 块级元素 div  span

```html
<!DOCTYPE HTML>
<html>
    <head>
        <meta charset="UTF-8"/>
        <title>div 标签</title>

        <style>
            #box {
                width: 100px;
                height: 200px;
                background: red;
            }
        </style>
    </head>
    <body>
    
        <div id="box">
            这是一个div 自动换行
        </div>    
        <input type="text"/> <span>这是span,不自动换行</span>
        <input type="text"/> <span>这是span,不自动换行</span>
    </body>
</html>
```

## 列表

```html
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <title>标题</title>
    </head>
    <body>
        <p>无序列表</p>
         <ul >
            <li>默认实心圆类型</li>
            <li>列表选项</li>
            <li>列表选项</li>
            <li>列表选项</li>
        </ul>
        <ul type ="circle">
            <li>circle 空心圆类型</li>
            <li>列表选项</li>
            <li>列表选项</li>
            <li>列表选项</li>
        </ul>
        <ul type ="square">
            <li>square 小方块类型</li>
            <li>列表选项</li>
            <li>列表选项</li>
            <li>列表选项</li>
        </ul>
        <p>有序列表</p>
        <ol >
            <li>有序列表</li>
            <li>有序列表</li>
            <li>有序列表</li>
            <li>有序列表</li>
        </ol>
         <ol type="A">
            <li>有序列表</li>
            <li>有序列表</li>
            <li>有序列表</li>
            <li>有序列表</li>
        </ol>
         <ol type="I">
            <li>有序列表</li>
            <li>有序列表</li>
            <li>有序列表</li>
            <li>有序列表</li>
        </ol>
         <ol type="i">
            <li>有序列表</li>
            <li>有序列表</li>
            <li>有序列表</li>
            <li>有序列表</li>
        </ol>
        <p>自定义列表（dl）</p>
        <dl>
            <dt>名词1</dt>
            <dd>名词1解释</dd>
            ...
            <dt>名词1</dt>
            <dd>名词1解释</dd>
            ...
        </dl>
        <h2>一个自定义列表：</h2>
        <dl>
            <dt>春天</dt>
            <dd>是万物复苏，百花争艳的季节</dd>
            <dt>夏天</dt>
            <dd>是夏日绵绵，烈日炎炎的季节</dd>
        </dl>
    </body>
</html>
```

##  实验挑战1

```html
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8"/>
        <title>练习1</title>
    </head>
    <body>
        <div style="width:500px;height:500px;background:pink">
        <h1 style="text-align:center">试验楼学习教程</h1>
        
        <p style="text-align:center">学习就上<a href="https://www.lanqiao.cn">实验楼</a></p>
        <hr/>
        <span>在实验楼你可以学习的课程有：</span>
        <ul>
            <li>HTML 基础课程</li>
            <li>CSS 基础课程</li>
            <li>Linux 基础课程</li>
            <li>...</li>
        </ul>
        </div>
    </body>
</html>

```

## 表格制作

`border` 单元格间隔

`colspan` 在同一行内合并多列。

`rowspan`  在同一列中合并多行。

`tr` 行标签

`caption` 表的标题

`th`  表头

`td` 内容标间

###  表格跨列 colspan

```html
<!DOCType html>
<html>
    <head>
        <title>表格跨列</title>
    <head>
    <body>
        <table border="1" width="300px" height="150px">
  <h3>单元跨两列</h3>
  <tr>
    <th>姓名</th>
    <th colspan="2">电话</th>
  </tr>
  <tr>
    <td>张三</td>
    <td>344 22 112</td>
    <td>211 32 123</td>
  </tr>
  <tr>
    <td>李四</td>
    <td>433 31 212</td>
    <td>234 21 654</td>
  </tr>
</table>
    </body>
</html>
```

###  表格跨行rowspan

```html
<!DOCType html>
<html>
    <head>
        <title>表格跨行</title>
    <head>
    <body>
        <table border="1" cellspacing="0">
  <h3>单元跨两行</h3>
  <tr>
    <th>姓名</th>
    <td>张三</td>
  </tr>
  <tr>
    <th rowspan="2">电话</th>
    <td>344 22 112</td>
  </tr>
  <tr>
    <td>234 21 654</td>
  </tr>
    </body>
</html>
```



挑战2

```html
<!DOCTYPE html>
<html>
    <head>
        <title>课程表</title>
    </head>

    <body>
        <table border="1" bgcolor="pink" text-align="center" width="60%" cellspacing="">
        <caption>课程表</caption>
        <tr>
            <th colspan="2">时间\日期</th>
            <th>一</th>
            <th>二</th>
            <th>三</th>
            <th>四</th>
            <th>五</th>
        </tr>

         <tr align="center">
        <td rowspan="2">上午</td>
        <td>9:30-10:15</td>
        <td>语文</td>
        <td>数学</td>
        <td>外语</td>
        <td>音乐</td>
        <td>体育</td>
      </tr>

      <tr align="center">
        <td>10:25-11:10</td>
        <td>数学</td>
        <td>数学</td>
        <td>物理</td>
        <td>化学</td>
        <td>生物</td>
      </tr>

      <tr>
        <td colspan="7">&nbsp;</td>
      </tr>

      <tr align="center">
        <td rowspan="2">下午</td>
        <td>14:30-15:15</td>
        <td>体育</td>
        <td>语文</td>
        <td>历史</td>
        <td>政治</td>
        <td>化学</td>
      </tr>

      <tr align="center">
        <td>15:25-16:10</td>
        <td>音乐</td>
        <td>语文</td>
        <td>数学</td>
        <td>美术</td>
        <td>语文</td>
      </tr>
        
          
        </table>
    </body>
</html>
```

## 表单

```html
<form name="表单名字" method="请求方法" action="请求url,没有提供此属性用#代替，返回到本页面"></form>
<input
  type="text"
  name="控件名称"
  value="文字字段的默认取值"
  size="控件的长度"
  maxlength="最长字符数"
/>
```

#### 文字字段



```html
<!DOCTYPE html>
<html>

<body>
<p>表单</p>
    <form name="formBox" method="post" action="#">
        姓名： <input type="text" name = "name " value ="syl" size="20"/><br/>
        年龄： <input type="text" name = "age " value ="10" size="40"  maxlength="3"/>
        密码： <input type="password" name="password" />
    </from>
</body>
</html>
```

#### 单选

```html
<!DOCTYPE html>
<html>

<body>
<p>表单</p>
    <form name="formBox" method="post" action="#">
        <input  type="radio" name="sex" value="male" checked/>男 <br/>
        <input  type="radio" name="sex" value="female" />女 <br/>
    </from>
</body>
</html>
```

#### 多选

```html
<!DOCTYPE html>
<html>

<body>
<p>表单</p>
    <form name="formBox" method="post" action="#">
        <input  type="checkbox" name="music"  checked/>音乐 <br/>
        <input  type="checkbox" name="art"  />美术 <br/>
        <input  type="checkbox" name="math"  />数学 <br/>
    </from>
</body>
</html>
```

#### 表单提交

```html
<!DOCTYPE html>
<html>

<body>
<p>表单</p>
    <form name="formBox" method="post" action="">
        <input type="text" name = "name " value ="输入的内容" size="10"/><br/>
        <button type="submit" >button提交</button>
        <input type="submit" value="input提交信息"/>
    </from>
</body>
</html>
```

#### 表单重置

```html
<!DOCTYPE html>
<html>

<body>
<p>表单</p>
    <form name="formBox" method="post" action="">
        <input type="text" /><br/>
        <button type="reset" >button重置</button>
        <input type="reset" value="input重置"/>
    </from>
</body>
</html>
```

#### 综合案例

```html
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>个人信息提交</title>
    </head>
    <body>
        <form action="#" method="post" name="frombox">
            姓名: <input type="text" name="name" /><br/>
            密码: <input type="password" name="password" /><br/>
            性别: <input type="radio" name="sex" /> 男
            <input type="radio" name="sex" /> 女<br/>
            爱好:<input type="checkbox" name="唱歌" />唱歌
            <input type="checkbox" name="跑步" />跑步
            <input type="checkbox" name="游泳" />游泳<br/>

            照片: <input type="file" name="penson_pic" /><br/>
            <br/>
            个人描述：<textarea cols="40"rows="3"></textarea><br/>
            籍贯: <select name="select" >
            <option value="成都">成都</option>
            <option value="上海">上海</option>
            <option value="四川">四川</option>
            <option value="南昌">南昌</option>
            </select><br/>
            <button type="submit" name="">提交</button>
            <button type="reset">重置</button>
        </form>
    </body>
</html>
```

### HTML 图像与框架

```html
<!DOCType html>
<html>
    <head>
        <title>表格跨列</title>
    <head>
    <body>
        <p>图像为单标签
        src: 引用图片资源地址
        alt: 规定图片未成功显示代替的文本
        title: 图标悬停显示的文本信息
        width: 图片的宽度
        height: 图片的高度
        </P>
        <img src="https://labfile.oss.aliyuncs.com/courses/1236/coder.jpg"
                alt="图片被勾走了"
                title="实验楼log"
                width="50%"
                height="300px"
        />
    </body>
</html>
```

相对路径：

* 位于同一文件夹下，`<img src="syl.png"/>`
* 位于下级文件夹  `<img src="image/a/sy1.png"/>`
* 位于上级文件夹 `<img src="../syl.png"/>`
* 位于上两级的文件夹 `<img src="../../syl.png"`

绝对路径：

指当所有网页引用同一个文件时，所使用的路径都是一样的。

比如`“D:\webStudy\img\syl.png”`或者 `“https://labfile.oss.aliyuncs.com/courses/1236/syl.png ”`。

#### 框架 iframe 

可以在浏览其中显示不止一个页面。

语法：

```html
<iframe src="URL">
  <!-- URL指向不同的页面 -->
</iframe>
```

#### iframe - 设置高度与宽度

属性默认以像素为单位,但是你可以指定其按比例显示 (如："60%")。

```html
<iframe src="https://www.lanqiao.cn/" width="400" height="400"></iframe>
```

![图片描述](https://doc.shiyanlou.com/courses/43/1347963/f4b4fe443850229d31d4712e0d51a119-0)

#### iframe - 移除边框

`frameborder` 属性用于定义 iframe 表示是否显示边框。设置属性值为 "0" 移除 iframe 的边框:

```html
<iframe
  src="https://www.lanqiao.cn/"
  width="400"
  height="400"
  frameborder="0"
></iframe>
```

#### 使用 iframe 来显示目标链接页面

iframe 可以显示一个目标链接的页面，目标链接的属性必须使用 iframe 的属性。

```html
<p><a href="https://www.lanqiao.cn/" target="shiyanlou">实验楼</a></p>
<iframe width="400" height="400" name="shiyanlou"></iframe>
```

注： 因为 `a` 标签的 `target` 属性是名为 `shiyanlou` 的 iframe 框架，所以在点击链接时页面会显示在 iframe 框架中。需要保证 iframe 框架的 `name` 属性的名称与 `a` 标签的 `target` 属性名一致。

# HTML5 学习笔记

```html
<!DOCTYPE html>
<html>
    <head>
        <title>My Page</title>
    </head>
    <body>
    <header>header</header>
    <nav>nav</nav>
    <article>
        <section>section</section>
    </article>
    <aside>aside</aside>
    <footer>footer</footer>
    </body>
</html>
```

`section` 表示文档中的一个区域。

`article` 定义独立的内容

`nav` 定义导航链接部分

`header` 定义文档页眉

`footer` 定义文档页脚

`aside` 侧边栏，放一些广告，友链等。

## 表单元素

###  datalist

 ```html
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>My Page</title>
    </head>
    <body>
        <lable for="myColor">你最喜爱的颜色？</lable>
        <input type="text" name="myColor" id="myColor" list="mySuggestion"/>

        <datalist id="mySuggestion">
            <option>黑</option>
            <option>白</option>
            <option>黄</option>
            <option>红</option>
            <option>蓝</option>
            <option>绿</option>
            <option>紫</option>
            <option>青</option>
            <option>赤</option>
        </datalist>
    </body>
</html>
 ```

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <title></title>
  </head>
  <body>
    <label for="myColor">What's your favorite color?</label>
    <input type="text" name="myColor" id="myColor" list="mySuggestion" />
    <datalist id="mySuggestion">
      <option value="black"> </option>
      <option value="blue"> </option>
      <option value="green"> </option>
      <option value="red"> </option>
      <option value="white"> </option>
      <option value="yellow"> </option>
    </datalist>
  </body>
</html>
```

## 表单属性



### autocomplete

```html
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>My Page</title>
    </head>
    <body>
        <form action="#" method="get" autocomplete="on">
            Name:<input type="text" name="name"/><br/>
            Email:<input type="email" autocomplete="off"/><br/>

            <input type="submit" value="提交"/>
        </from>
    </body>
</html>
```

### autofocus

```html
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>My Page</title>
    </head>
    <body>
    <form action="#" method="post">
        Name： <input type="text" name="name" autofocus="autofocus"/><br/>
        <input type="submit" value="提交" />
    </form>
        
    </body>
</html>
```

### form 属性

```html
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>My Page</title>
    </head>
    <body>
        <from id="user-form" action="#" method="get">
            first Name:<input type="text" name="fname"/>
            <input type="submit" value="提交" />
        </form>
          <p>
    下面的输入域在 form 元素之外，但仍然是表单的一部分， 也就是说提交按钮会把
    first name 和 last name的值都提交。
  </p>
        Last Name :<input type="text" name="lname" form="user_form" />
    </body>
</html>
```

### multiple 属性

```html
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>My Page</title>
    </head>
    <body>
        <!--支持上传多个文件-->
        <from  action="#" method="get">
            选择文件：<input type="file" name="file" multiple="multiple" />
            <input type="submit" value="提交" />
        </from>
    </body>
</html>
```

### novalidate 属性

```html
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>My Page</title>
    </head>
    <body>
        <from  action="#" method="get" novalidate="true">
            选择文件：<input type="file" name="file" multiple="multiple" />
            <input type="submit" value="提交" />
        </from>
    </body>
</html>
```

不验证

### pattern

验证input 域的模式

```html
<body>
  <form action="#" method="get">
    Name:
    <input type="text" name="name" pattern="[A-z]{4}" title="请输入四个字母" />
    <input type="submit" value="提交" />
  </form>
</body>
```

### placeholder 属性

提供输入框默认值

```html
<!DODTYPE html>
<html>
    <head>
        <title>My Page</title>
    </head>
    <body>
        <form action="#" method="get">
            <input type="search" name="user_search" placeholder="佛不渡我"/>
            <input type="submit" value="提交"/>
        </form>
    </body>
</html>
```

### required

提交前输入框不能为空

```html
<body>
  <form action="#" method="get">
    <input type="search" name="user_search" required="required" />
    <input type="submit" value="提交" />
  </form>
</body>
```

## html5 输入类型

### input 类型 email 

```html
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>My Page</title>
    </head>
    <body>
        <form  action="#" method="get">
            email:<input type="email" name="email"/>
            <input type="submit" value="提交" />
        </form>
    </body>
</html>
```

### input 输入类型 url

```html
<body>
  <form action="#" method="get">
    Url: <input type="url" name="user_url" /><br />
    <input type="submit" value="提交" />
  </form>
</body>
```

会自动验证url 域的值

### number 属性

```html
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>My Page</title>
    </head>
    <body>
        <form  action="#" method="get">
            Number:<input type="number" name="points"
            min="0"
            max="10"
            step="2"
            value="2"/>
            <input type="submit" value="提交" />
        </form>
    </body>
</html>
```

### input 类型range

```html
<body>
  <form action="#" method="get">
    range:
    <input
      type="range"
      name="points"
      min="1"
      max="10"
      value="2"
      step="2"
    /><br />
    <input type="submit" value="提交" />
  </form>
</body>
```

### input 类型 Date Pickers 日期选择器

```html
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>My Page</title>
    </head>
    <body>
        <form  action="#" method="get">
            Date <input type="date" name="user_date"/> <br />

            Month <input type="month" name="user_month"/><br />
            week <input type="week" name="user_week"/><br />
            time <input type="time" name="user_time"/><br />
            datetime-local: <input type="datetime-local" name="user_datetime"/><br />
            <input type="submit" value="提交"/>
        </form>
    </body>
</html>
```

### input -color

```html
<body>
  <form action="#" method="get">
    <input type="color" name="colordemo" />
    <input type="submit" value="提交" />
  </form>
</body>
```

##  canvas 绘制图形

知识要点

* Canvas 元素
* 绘制简单图形
* 直线绘制
* 矩形绘制
* 圆和椭圆绘制
* 填充和渐变
* 文字绘制
* 图片绘制

### Canvas 元素

创建一个画布

```html
<body>
  <canvas
    id="myCanvas"
    width="200"
    height="100"
    style="border:2px solid #000000;"
  >
  </canvas>
</body>
```

### 绘制简单的图形

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title></title>
  </head>

  <body>
    <!--添加canvas元素，设置画布的大小-->
    <canvas id="mycanvas" style="width:200;height:100">
      对不起，你的浏览器不支持canvas
    </canvas>

    <script type="text/javascript">
      var myCanvas = document.getElementById('mycanvas');
      var ctx = myCanvas.getContext('2d');
      //设置颜色
      ctx.fillStyle = 'red';
      //设置矩形的位置和尺寸（位置从 左上角原点坐标开始，尺寸为100*100的矩形）
      ctx.fillRect(0, 0, 100, 100);
    </script>
  </body>
</html>
```



### 直线绘制

```html
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>直线绘制</title>
    <head>
    <body>
        <!--添加画布-->
        <canvas id="mycanvas" whidth="520px" height="1314px">
            对不起，您的浏览器不支持canvas
        </canvas>
        <script>
            //获取canvas元素  
            var myCanvas=document.getElementById('mycanvas');
            //获取上下文
            var ctx=myCanvas.getContext('2d');
            //开始路径绘制
            ctx.beginPath();
            //设置路径起始坐标点
            ctx.moveTo(20,20);
            //添加一个新的点
            ctx.lineTo(200,200);
            //设置线宽
            ctx.lineWidth = 2.0;
            //设置线的颜色
            ctx.strokeStyle = '#CC0000';
            //绘制已经定义的路径
            ctx.stroke();
        </script>
    </body>
</html>






```

### 绘制三角型

```html
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>直线绘制</title>
    <head>
    <body>
        <!--添加画布-->
        <canvas id="mycanvas" whidth="520px" height="1314px">
            对不起，您的浏览器不支持canvas
        </canvas>
        <script>
            //获取canvas元素  
            var myCanvas=document.getElementById('mycanvas');
            //获取上下文
            var ctx=myCanvas.getContext('2d');
            //开始路径绘制
            ctx.beginPath();
            //设置路径起始坐标点
            ctx.moveTo(20,20);
            //添加一个新的点
            ctx.lineTo(200,200);
            ctx.lineTo(400,20)
            ctx.closePath();
            //设置线宽
            ctx.lineWidth = 2.0;
            //设置线的颜色
            ctx.strokeStyle = '#CC0000';
            //绘制已经定义的路径
            ctx.stroke();
        </script>
    </body>
</html>
```

### 绘制矩形

```html
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>直线绘制</title>
    <head>
    <body>
        <!--添加画布-->
        <canvas id="mycanvas" whidth="520px" height="1314px">
            对不起，您的浏览器不支持canvas
        </canvas>
        <script>
            //获取canvas元素  
            var myCanvas=document.getElementById('mycanvas');
            //获取上下文
            var ctx=myCanvas.getContext('2d');
            //开始路径绘制
            ctx.beginPath();
            //设置路径起始坐标点
           ctx.rect(10,10,100,200)
            //设置线宽
            ctx.lineWidth = 2.0;
            //设置线的颜色
            ctx.strokeStyle = '#CC0000';
            //绘制已经定义的路径
            ctx.stroke();
        </script>
    </body>
</html>
```

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title></title>
  </head>

  <body>
    <!--添加canvas元素，设置画布的大小-->
    <canvas id="mycanvas" width="520px" height="1314px">
      对不起，你的浏览器不支持canvas
    </canvas>

    <script type="text/javascript">
      //获取canvas元素
      var myCanvas = document.getElementById('mycanvas');
      //获取Context上下文
      var ctx = myCanvas.getContext('2d');
      //绘制矩形
      ctx.strokeRect(10, 10, 100, 200);
    </script>
  </body>
</html>
```

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title></title>
  </head>

  <body>
    <!--添加canvas元素，设置画布的大小-->
    <canvas id="mycanvas" width="520px" height="1314px">
      对不起，你的浏览器不支持canvas
    </canvas>

    <script type="text/javascript">
      //获取canvas元素
      var myCanvas = document.getElementById('mycanvas');
      //获取Context上下文
      var ctx = myCanvas.getContext('2d');
      //设置填充颜色
      ctx.fillStyle='#FF0000';
      //绘制矩形
      ctx.fillRect(10, 10, 100, 200);
    </script>
  </body>
</html>
```

### 清空指定元素的填充

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title></title>
  </head>

  <body>
    <!--添加canvas元素，设置画布的大小-->
    <canvas id="mycanvas" width="520px" height="1314px">
      对不起，你的浏览器不支持canvas
    </canvas>

    <script type="text/javascript">
      //获取canvas元素
      var myCanvas = document.getElementById('mycanvas');
      //获取Context上下文
      var ctx = myCanvas.getContext('2d');
      //绘制矩形
      ctx.fillRect(10, 10, 100, 200);
      //清空指定像素
      ctx.clearRect(20, 20, 50, 50);
    </script>
  </body>
</html>
```

### 绘制圆

使用 `arc()` 方法绘制圆或者椭圆。语法为：

```javascript
ctx.arc(x, y, r, sAngle, eAngle, counterclockwise);
```

参数说明：

- x 表示圆的中心的 x 坐标。
- y 表示圆的中心的 y 坐标。
- r 表示圆的半径。
- sAngle 表示起始角，以弧度计（特别需要注意的是弧的圆形的三点钟位置是 0 度而不是常规以为的 90 度）。
- eAngle 表示结束角，以弧度计。
- counterclockwise 表示绘制圆的方向，值为 false 表示顺时针，为 true 表示逆时针。是一个可选值，默认值是 false。

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title></title>
  </head>

  <body>
    <!--添加canvas元素，设置画布的大小-->
    <canvas id="mycanvas" width="520px" height="1314px">
      对不起，你的浏览器不支持canvas
    </canvas>

    <script type="text/javascript">
      //获取canvas元素
      var myCanvas = document.getElementById('mycanvas');
      //获取Context上下文
      var ctx = myCanvas.getContext('2d');
      //开始绘制路径
      ctx.beginPath();
      //绘制圆
      ctx.arc(100,75,50,0,2*Math.PI);
      //绘制已经定制的路径
     
      ctx.stroke();
    </script>
  </body>
</html>
```

### 绘制扇形

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title></title>
  </head>

  <body>
    <!--添加canvas元素，设置画布的大小-->
    <canvas id="mycanvas" width="520px" height="1314px">
      对不起，你的浏览器不支持canvas
    </canvas>

    <script type="text/javascript">
      //获取canvas元素
      var myCanvas = document.getElementById('mycanvas');
      //获取Context上下文
      var ctx = myCanvas.getContext('2d');
      //开始绘制路径
      ctx.beginPath();
      //绘制圆
      ctx.arc(100,75,50,0,Math.PI/2);
       ctx.moveTo(100,125);
      ctx.lineTo(100,75);
      ctx.lineTo(150,75);
      //绘制已经定制的路径
      ctx.stroke();
    </script>
  </body>
</html>
```

#### 渐变

使用 `createLinearGradient()` 方法创建线性渐变。语法为：

```js
ctx.createLinearGradient(x0, y0, x1, y1);
```

参数说明：

- x0 表示渐变开始点的 x 坐标。
- y0 表示渐变开始点的 y 坐标。
- x1 表示渐变结束点的 x 坐标。
- y1 表示渐变结束点的 y 坐标。

使用 `addColorStop()` 方法规定渐变对象中的颜色和停止位置。语法为：

```js
gradient.addColorStop(stop, color);
```

参数说明：

- stop 表示渐变中开始与结束之间的位置。是介于 `0.0` 与 `1.0` 之间的值。
- color 表示在结束位置显示的 CSS 颜色值。

注：`addColorStop()` 方法与 `createLinearGradient()` 或 `createRadialGradient()` 一起使用。我们可以多次调用 `addColorStop()` 方法来改变渐变。如果我们不对 `gradient` 对象使用该方法，那么渐变将不可见。为了获得可见的渐变，至少需要创建一个色标。

例子：

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title></title>
  </head>

  <body>
    <!--添加canvas元素，设置画布的大小-->
    <canvas id="mycanvas" width="520px" height="1314px">
      对不起，你的浏览器不支持canvas
    </canvas>

    <script type="text/javascript">
      //获取canvas元素
      var myCanvas = document.getElementById('mycanvas');
      //获取Context上下文
      var ctx = myCanvas.getContext('2d');
      //设置渐变色
      var gradient = ctx.createLinearGradient(0, 0, 170, 0);
      gradient.addColorStop(0, 'red');
      gradient.addColorStop('0.2', 'orange');
      gradient.addColorStop('0.5', 'yellow');
      gradient.addColorStop('0.7', 'green');
      gradient.addColorStop(1, 'blue');
      //填充色为渐变色
      ctx.fillStyle = gradient;
      //绘制实心矩形
      ctx.fillRect(10, 10, 100, 200);
    </script>
  </body>
</html>
```

运行效果为：

![此处输入图片的描述](https://doc.shiyanlou.com/document-uid897174labid9222timestamp1550131650892.png)

使用 `createRadialGradient()` 方法创建放射状/环形的渐变。语法为：

```js
ctx.createRadialGradient(x0, y0, r0, x1, y1, r1);
```

参数说明：

- x0 表示渐变的开始圆的 x 坐标。
- y0 表示渐变的开始圆的 y 坐标。
- r0 表示开始圆的半径。
- x1 表示渐变的结束圆的 x 坐标。
- y1 表示渐变的结束圆的 y 坐标。
- r1 表示结束圆的半径。

例子：

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title></title>
  </head>

  <body>
    <!--添加canvas元素，设置画布的大小-->
    <canvas id="mycanvas" width="520px" height="1314px">
      对不起，你的浏览器不支持canvas
    </canvas>

    <script type="text/javascript">
      //获取canvas元素
      var myCanvas = document.getElementById('mycanvas');
      //获取Context上下文
      var ctx = myCanvas.getContext('2d');
      //设置渐变色
      var gradient = ctx.createRadialGradient(75, 50, 5, 90, 60, 100);
      gradient.addColorStop(0, 'red');
      gradient.addColorStop('0.2', 'orange');
      gradient.addColorStop('0.5', 'yellow');
      gradient.addColorStop('0.7', 'green');
      gradient.addColorStop(1, 'blue');
      //填充色为渐变色
      ctx.fillStyle = gradient;
      //绘制实心矩形
      ctx.fillRect(10, 10, 190, 150);
    </script>
  </body>
</html>
```

运行效果为：

![此处输入图片的描述](https://doc.shiyanlou.com/document-uid897174labid9222timestamp1550132664228.png)

#### fill() 方法

使用 `fill()` 方法填充当前的图像（路径）。默认颜色是黑色。填充另一种颜色/渐变使用 `fillStyle` 属性。

语法为：

```js
ctx.fill();
```

注：如果路径未关闭，那么 `fill()` 方法会从路径结束点到开始点之间添加一条线，以关闭该路径，然后填充该路径。

例子：

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title></title>
  </head>

  <body>
    <!--添加canvas元素，设置画布的大小-->
    <canvas id="mycanvas" width="520px" height="1314px">
      对不起，你的浏览器不支持canvas
    </canvas>

    <script type="text/javascript">
      //获取canvas元素
      var myCanvas = document.getElementById('mycanvas');
      //获取Context上下文
      var ctx = myCanvas.getContext('2d');
      //绘制矩形
      ctx.rect(20, 20, 150, 100);
      ctx.fillStyle = 'red';
      ctx.fill();
    </script>
  </body>
</html>
```

运行效果为：

![此处输入图片的描述](https://doc.shiyanlou.com/document-uid897174labid9222timestamp1550133110891.png)

#### fillText() 方法

使用 `fillText()` 方法在在画布上绘制实心文本。语法为：

```js
ctx.fillText(text, x, y, maxWidth);
```

参数说明：

- text 规定在画布上输出的文本。
- x 表示开始绘制文本的 x 坐标位置（相对于画布）。
- y 表示开始绘制文本的 y 坐标位置（相对于画布）。
- maxWidth 表示允许的最大文本宽度，以像素计。可选。

例子：

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title></title>
  </head>

  <body>
    <!--添加canvas元素，设置画布的大小-->
    <canvas id="mycanvas" width="520px" height="1314px">
      对不起，你的浏览器不支持canvas
    </canvas>

    <script type="text/javascript">
      //获取canvas元素
      var myCanvas = document.getElementById('mycanvas');
      //获取Context上下文
      var ctx = myCanvas.getContext('2d');
      //绘制实心文本
      ctx.fillText('Hello Syl!', 10, 50);
    </script>
  </body>
</html>
```

运行效果为：

![此处输入图片的描述](https://doc.shiyanlou.com/document-uid897174labid9222timestamp1550135723679.png)

#### strokeText() 方法

使用 `strokeText()` 方法在画布上绘制空心文本。语法为：

```js
ctx.strokeText(text, x, y, maxWidth);
```

注：参数说明与 `fillText()` 方法一致。

例子：

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title></title>
  </head>

  <body>
    <!--添加canvas元素，设置画布的大小-->
    <canvas id="mycanvas" width="520px" height="1314px">
      对不起，你的浏览器不支持canvas
    </canvas>

    <script type="text/javascript">
      //获取canvas元素
      var myCanvas = document.getElementById('mycanvas');
      //获取Context上下文
      var ctx = myCanvas.getContext('2d');
      //设置字体样式
      ctx.font = '50px Georgia';
      //绘制空心文本
      ctx.strokeText('Hello Syl!', 10, 50);
    </script>
  </body>
</html>
```

运行效果为：

![此处输入图片的描述](https://doc.shiyanlou.com/document-uid897174labid9222timestamp1550136250155.png)

#### font 属性

使用 `font` 属性设置或返回画布上文本内容的当前字体属性。`font` 属性使用的语法与 CSS `font` 属性相同。

#### textAlign 属性

使用 `textAlign` 属性设置或返回文本内容的当前对齐方式。语法为：

```js
ctx.textAlign = 'center|end|left|right|start';
```

参数说明：

- start 默认值，表示文本在指定的位置开始。
- center 表示文本的中心被放置在指定的位置。
- end 表示文本在指定的位置结束。
- left 表示文本左对齐。
- right 表示文本右对齐。

注：使用 `fillText()` 或 `strokeText()` 方法在实际地在画布上绘制并定位文本。

例子：

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title></title>
  </head>

  <body>
    <!--添加canvas元素，设置画布的大小-->
    <canvas id="mycanvas" width="520px" height="1314px">
      对不起，你的浏览器不支持canvas
    </canvas>

    <script type="text/javascript">
      //获取canvas元素
      var myCanvas = document.getElementById('mycanvas');
      //获取Context上下文
      var ctx = myCanvas.getContext('2d');
      //画一条线便于展示
      ctx.strokeStyle = 'blue';
      ctx.moveTo(200, 20);
      ctx.lineTo(200, 180);
      ctx.stroke();
      //设置字体样式
      ctx.font = '20px Georgia';
      //值为start的情况
      ctx.textAlign = 'start';
      ctx.strokeText('Hello Syl!', 200, 20);
      //值为center的情况
      ctx.textAlign = 'center';
      ctx.strokeText('Hello Syl!', 200, 60);
      //值为end的情况
      ctx.textAlign = 'end';
      ctx.strokeText('Hello Syl!', 200, 100);
      //值为left的情况
      ctx.textAlign = 'left';
      ctx.strokeText('Hello Syl!', 200, 140);
      //值为right的情况
      ctx.textAlign = 'right';
      ctx.strokeText('Hello Syl!', 200, 180);
    </script>
  </body>
</html>
```

运行效果为：

![此处输入图片的描述](https://doc.shiyanlou.com/document-uid897174labid9222timestamp1550194687243.png)

#### textBaseline 属性

`textBaseline` 属性设置或返回在绘制文本时的当前文本基线。语法为：

```js
ctx.textBaseline = 'alphabetic|top|hanging|middle|ideographic|bottom';
```

参数说明：

- alphabetic 表示文本基线是普通的字母基线。默认值。
- top 表示文本基线是 `em` 方框的顶端。
- hanging 表示文本基线是悬挂基线。
- middle 表示文本基线是 `em` 方框的正中。
- ideographic 表示文本基线是表意基线。
- bottom 表示文本基线是 `em` 方框的底端。

例子：

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title></title>
  </head>

  <body>
    <!--添加canvas元素，设置画布的大小-->
    <canvas id="mycanvas" width="1314px" height="1314px">
      对不起，你的浏览器不支持canvas
    </canvas>

    <script type="text/javascript">
      //获取canvas元素
      var myCanvas = document.getElementById('mycanvas');
      //获取Context上下文
      var ctx = myCanvas.getContext('2d');
      //画一条线便于展示
      ctx.strokeStyle = 'blue';
      ctx.moveTo(20, 100);
      ctx.lineTo(1314, 100);
      ctx.stroke();
      //设置字体样式
      ctx.font = '30px Georgia';
      //值为alphabetic的情况
      ctx.textBaseline = 'alphabetic';
      ctx.fillText('Hello Syl!', 20, 100);
      //值为top的情况
      ctx.textBaseline = 'top';
      ctx.fillText('Hello Syl!', 220, 100);
      //值为hanging的情况
      ctx.textBaseline = 'hanging';
      ctx.fillText('Hello Syl!', 420, 100);
      //值为middle的情况
      ctx.textBaseline = 'middle';
      ctx.fillText('Hello Syl!', 620, 100);
      //值为ideographic的情况
      ctx.textBaseline = 'ideographic';
      ctx.fillText('Hello Syl!', 820, 100);
      //值为bottom的情况
      ctx.textBaseline = 'bottom';
      ctx.fillText('Hello Syl!', 1020, 100);
    </script>
  </body>
</html>
```

运行效果为：

![此处输入图片的描述](https://doc.shiyanlou.com/document-uid897174labid9222timestamp1550196501383.png)

## 视频video

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <title></title>
  </head>
  <body>
    <video width="320" height="240" controls="controls">
      <source
        src="https://labfile.oss.aliyuncs.com/courses/1248/video.ogg"
        type="video/ogg"
      />
      <source
        src="https://labfile.oss.aliyuncs.com/courses/1248/video.mp4"
        type="video/mp4"
      />
 	不同的浏览器识别格式不同，只读取第一个识别的source.
      你的浏览器不支持video元素
    </video>
  </body>
</html>
```

### 字幕的使用

```html
<body>
  <video
    src="https://labfile.oss.aliyuncs.com/courses/1248/video.ogg"
    width="320"
    height="240"
    controls="controls"
  >
    你的浏览器不支持video元素
    <track
      src="video_ch.vtt"
      srclang="en"
      kind="subtitles"
      label="中文"
      
    />
    <track 
        src="video_en.vtt" 
        srclang="en" 
        kind="subtitles" 
        label="English" default/>
  </video>
</body>
```

字幕文件

video_ch.vtt

```txt
WEBVTT
 
00:00:01.000 --> 00:00:04.000
不准在课堂上打游戏！
00:00:05.000 --> 00:00:09.000
好好学习，天天向上！
```

video_en.vtt

```html
WEBVTT
 
00:00:01.000 --> 00:00:04.000
Don't play games in class！
00:00:05.000 --> 00:00:09.000
Study hard and make progress every day！
```

### 音频 audio

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <title></title>
  </head>
  <body>
  <audio controls="controls">
    <source
      src="https://labfile.oss.aliyuncs.com/courses/1248/video.ogg"
      type="audio/ogg"
    />
    <source
      src="https://labfile.oss.aliyuncs.com/courses/1248/phone.mp3"
      type="audio/mpeg"
    />
    你的浏览器不支持audio元素
  </audio>
</body>
</html>
```

注意：之后的学习会涉及 JavaScript 的基础用法，如果有不明白的，可以到实验楼[JavaScript 基础教程](https://www.lanqiao.cn/courses/1238)进行学习。 先来看个例子：

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title></title>
    <style type="text/css">
      #div1 {
        width: 200px;
        height: 100px;
        padding: 10px;
        border: 1px solid red;
      }
    </style>
    <script type="text/javascript">
      function allowDrop(ev) {
        ev.preventDefault();
      }

      function drag(ev) {
        ev.dataTransfer.setData('Text', ev.target.id);
      }

      function drop(ev) {
        ev.preventDefault();
        var data = ev.dataTransfer.getData('Text');
        ev.target.appendChild(document.getElementById(data));
      }
    </script>
  </head>

  <body>
    <p>请把图片拖放到矩形中：</p>
    <div id="div1" ondrop="drop(event)" ondragover="allowDrop(event)"></div>
    <br />
    <img
      id="drag1"
      src="drag-image.png"
      draggable="true"
      ondragstart="drag(event)"
    />
  </body>
</html>
```

运行效果为：

![此处输入图片的描述](https://doc.shiyanlou.com/document-uid897174labid9222timestamp1550213480826.png)

下面将为大家一一介绍拖放是如何实现的。

#### 确定什么是可拖动的

为了使元素可拖放，首先把 `draggable` 属性设置为 `true`，再加上全局事件处理函数 `ondragstart`，如下所示：

```html
<img draggable="true" ondragstart="drag(event)" />
```

#### 定义拖动数据

每个 `drag event` 都有一个 `dataTransfer` 属性保保存事件的数据。这个属性（ `DataTransfer` 对象）也有管理拖动数据的方法。`setData()` 方法添加一个项目的拖拽数据，如下面的示例代码所示：

```js
function drag(ev) {
  ev.dataTransfer.setData('Text', ev.target.id);
}
```

在这个例子中数据类型是 `"Text"`，值是可拖动元素的 `id ("drag1")`。

#### 定义一个放置区

`ondragover` 事件规定在何处放置被拖动的数据。默认地，无法将数据/元素放置到其他元素中。如果需要设置允许放置，我们必须阻止对元素的默认处理方式,如下所示：

```js
function allowDrop(ev) {
  ev.preventDefault();
}
```

#### 进行放置

当放置被拖数据时，会发生 `drop` 事件。如下所示：

```js
function drop(ev) {
  //调用 preventDefault() 来避免浏览器对数据的默认处理
  ev.preventDefault();
  //通过 dataTransfer.getData("Text") 方法获得被拖的数据。该方法将返回在 setData() 方法中设置为相同类型的任何数据。
  var data = ev.dataTransfer.getData('Text');
  //被拖数据是被拖元素的 id ("drag1"),把被拖元素追加到放置元素（目标元素）中
  ev.target.appendChild(document.getElementById(data));
}
```

上面只是简单的讲解了一个图片拖放的案例，想要了解更多拖放的知识，请访问：[MDN HTML 拖放 API](https://developer.mozilla.org/zh-CN/docs/Web/API/HTML_Drag_and_Drop_API#接口)

# 完结撒花

