# Spring boot 自动编译

引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

设置自动编译：Build->Compiler->Build porject automatically

![image-20220317201945460](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220317201945460.png)

使用<kbd>Ctrl</kbd>+<kbd>Shift</kbd>+<kbd>Alt</kbd>+<kbd>/</kbd>  组合键

![image-20220317202101391](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220317202101391.png)

找到 compioler.automake.allow.when.app.runing  之前的版本

![image-20220317203926539](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220317203926539.png)

在新版本中移到了 Tools->Advanced setting 中，如图

![image-20220317202905247](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220317202905247.png)

设置好，启动项目就可以自动编译了。