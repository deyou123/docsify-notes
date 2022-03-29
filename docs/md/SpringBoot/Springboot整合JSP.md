# Spring boot 整合 JSP

## 1. Spring boot 整合 JSP

创建Springboot 项目

### 1.1 导入必须的依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.apache.tomcat.embed</groupId>
    <artifactId>tomcat-embed-jasper</artifactId>
    <scope>provided</scope>
</dependency>
<!--使用jstl 表达公式-->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>jstl</artifactId>
</dependency>
```

### 1.2 配置web 目录

在 main 目录下配置 webapp/WEB-INF/jsp 目录用于存放jsp 页面。并配置webapp 为web 目录。

![image-20220322112110598](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220322112110598.png)

### 1.3 配置视图解析

方式一、在application.properties 中配置

```properties
spring.mvc.view.prefix=WEB-INF/jsp/
spring.mvc.view.suffix=.jsp
```

方式二、创建配置类(实现`WebConfigurer`)

```java
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
       registry.jsp("WEB-INF/jsp/",".jsp");
    }
}
```

### 1.4 配置controller

```java
@Controller
public class HelloController {
    @GetMapping("/hello")
    public String hello(Model model){
        model.addAttribute( "msg","Hello Spring boot + jsp" );
        return "hello";
    }
}
```

## 1.5 配置页面

hello.jsp

```html
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Jsp</title>
</head>
<body>
<h1>01.jsp</h1>
<h1>${msg}</h1>
</body>
</html>
```

### 1.6 测试

使用1.3 中配置视图解析器的方式，选择其中一种。测试

访问	http://localhost:8080/hello

![image-20220322112607323](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220322112607323.png)

