# Spring boot Freemarker + Thymeleaf + Jsp多视图解析

## 1. Springboot FreeMarker +Thymeleaf 

创建Springboot 工程。

### 1.1引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-freemarker</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### 1.2 配置controller

```java
@Controller
public class HelloController {
    @GetMapping("/{path}")
    public String hello(@PathVariable(value = "path") String path, Model model){
        model.addAttribute( "hello","Hello Views" );
        return path;
    }

}
```

### 1.3 配置页面

Thymeleaf 页面在Resources/templates/路径下

01.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h1>01.Thymelef 模板页面</h1>
<div th:text="${hello}"></div>
</body>
</html>
```

Freemarker 页面在Resources/templates/路径下

02.ftlh

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Freemarker</title>
</head>
<body>
<h1>02Freenarker 页面</h1>
<div>${hello}</div>
</body>
</html>
```

### 1.4 applications.properties 配置文件：

下面配置全部使用默认值。也可不配置。

```yml
# 应用名称
spring.application.name=springboot-views
# 应用服务 WEB 访问端口
server.port=8080
#指定freemarker的模板路径和模板的后缀 使用默认的 classpath:/templates/
spring.freemarker.template-loader-path=classpath:/templates/
spring.freemarker.suffix=.ftlh
# 指定字符集
spring.freemarker.charset=utf-8
# 指定是否要启用缓存
spring.freemarker.cache=false
#指定是否要暴露请求和会话属性
spring.freemarker.expose-request-attributes=true
spring.freemarker.expose-session-attributes=true
# spring 静态资源扫描路径
spring.resources.static-locations=classpath:/static/# THYMELEAF (ThymeleafAutoConfiguration)
# 开启模板缓存（默认值： true ）
spring.thymeleaf.cache=true
# 检查模板是否存在，然后再呈现
spring.thymeleaf.check-template=true
# 检查模板位置是否正确（默认值 :true ）
spring.thymeleaf.check-template-location=true
#Content-Type 的值（默认值： text/html ）
spring.thymeleaf.content-type=text/html
# 开启 MVC Thymeleaf 视图解析（默认值： true ）
spring.thymeleaf.enabled=true
# 模板编码
spring.thymeleaf.encoding=UTF-8
# 要被排除在解析之外的视图名称列表，⽤逗号分隔
#spring.thymeleaf.excluded-view-names=
# 要运⽤于模板之上的模板模式。另⻅ StandardTemplate-ModeHandlers( 默认值： HTML5)
spring.thymeleaf.mode=HTML5
# 在构建 URL 时添加到视图名称前的前缀（默认值： classpath:/templates/ ）
spring.thymeleaf.prefix=classpath:/templates/
# 在构建 URL 时添加到视图名称后的后缀（默认值： .html ）
spring.thymeleaf.suffix=.html
```

### 1.5 测试

| http://localhost:8080/01                                     | http://localhost:8080/02                                     |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![image-20220322150910391](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220322150910391.png) | ![image-20220322150941396](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220322150941396.png) |

## 2 Spring boot + FreeMarker + Jsp

### 2.1 引入依赖

```xml
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-freemarker</artifactId>
 </dependency>
<dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-thymeleaf</artifactId>
 </dependency>
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

### 2.2 配置jsp 视图

webapp/03.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>03 Jsp 页面</h1>
<h1>${hello}</h1>
</body>
</html>
```

### 2.3 配置jsp 视图解析器

```java
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/",".jsp");
    }
}
```

启动项目： 分别访问三个页面

| http://localhost:8080/01 | http://localhost:8080/02 | http://localhost:8080/03 |
| ------------------------ | ------------------------ | ------------------------ |
| 访问成功                 | 访问成功                 | 访问失败                 |

发现0.3 jsp 页面无法访问。

在依赖中去掉 thymeleaf

```xml
<dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-thymeleaf</artifactId>
 </dependency>
```

Sprin boot + Freemarker + jsp 访问成功

-------------------------------------------------------------------------------------------------------------

分析：

Jsp 是视图解析器优先级最低。那么要想实现同时访问三种视图呢？



Jsp 视图解析器中checkResource 总是返回true. Jsp 视图解析优先级低于Freemarker 

试着调高jsp 视图解析优先级

```java
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/",".jsp");
        //把jsp 优先级调高
        registry.order( 1 );
    }
}
```

测试后发现之后jsp 页面能访问 Freemarker 页面也无法访问了。

下面来看怎养解决

## 3 Spring boot + thymeleaf + jsp 

### 3.1 修改依赖

```html
<dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-thymeleaf</artifactId>
 </dependency>
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

### 3.2 配置 checkResource()

```java
public class HandleResourceViewExists  extends InternalResourceView {
    @Override
    public boolean checkResource(Locale locale) throws Exception {
        File file = new File( this.getServletContext().getRealPath( "/" ) + getUrl() );
        return file.exists();
    }
}
```
3.3 配置JSP 视图解析

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/",".jsp").viewClass( HandleResourceViewExists.class );
        //FreeMarker 优先级
        registry.order( 1 );
    }
}

```

这样就可以Thymeleaf 和 JSP 同时访问。

## 4. Spring boot + Thymeleaf + Freemarker + jsp

将上述代码中添加

```xml
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-freemarker</artifactId>
 </dependency>
```

添加 Freemarker 依赖后，就实现了均可访问。