参考

尚硅谷全笔记： https://blog.csdn.net/weixin_44751434/article/details/119358203

# 1. 第一个SpringMVC项目

创建一个maven 项目

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>springmvc_01</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.3.16</version>
        </dependency>
    </dependencies>
</project>
```

并添加web 模块

项目目录如下

![image-20220318211911622](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220318211911622.png)

配置

springmvc.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/mvc https://www.springframework.org/schema/mvc/spring-mvc.xsd">
        <context:component-scan base-package="com.example.controller"></context:component-scan>
        <mvc:default-servlet-handler></mvc:default-servlet-handler>
        <mvc:annotation-driven></mvc:annotation-driven>
</beans>
```

配置

web.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    <servlet>
        <servlet-name>springMVC</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:sprngMVC.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>springMVC</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```

配置HelloController

```java
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello(){
        return "Hello Spring MVC";
    }
}
```

配置tomcat 

别忘记设置路径

![image-20220318212127321](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220318212127321.png)

启动tomcat 访问 http://localhost:8080/hello

出现

![image-20220318212326737](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220318212326737.png)

接下来配置视图使用jsp 技术配置视图解析器

引入依赖
```xml
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>4.0.1</version>
    <scope>provided</scope>
</dependency>
<!--jsp依赖 -->
<dependency>
    <groupId>javax.servlet.jsp</groupId>
    <artifactId>jsp-api</artifactId>
    <version>2.1</version>
    <scope>provided</scope>
</dependency>
```

在SpringMVC 中配置视图解析器

```xml
<!--配置视图解析器-->
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/views/"></property>
        <property name="suffix" value=".jsp"></property>
 </bean>
```



修改HelloController 添加test接口

```java
@Controller
public class HelloController {
    @GetMapping("/hello")
    @ResponseBody
    public String hello(){
        return "Hello Spring MVC";
    }

    /*测试视图解析器*/
    @GetMapping("/test")
    public ModelAndView test(){
        System.out.println("配置了jsp 视图解析");
        ModelAndView view = new ModelAndView("test");
        return view;
    }
}
```

添加/webapp/views/路径下新建test.jsp 

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>Test 页面</h1>
</body>
</html>
```

启动项目访问  http://localhost:8080/test

![image-20220318213912834](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220318213912834.png)

# 2. Spring MVC 实现文件上传功能

引入依赖

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>5.3.16</version>
</dependency>
<!-- https://mvnrepository.com/artifact/commons-fileupload/commons-fileupload -->
<dependency>
    <groupId>commons-fileupload</groupId>
    <artifactId>commons-fileupload</artifactId>
    <version>1.4</version>
</dependency>

<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>4.0.1</version>
</dependency>
<dependency>
    <groupId>javax.servlet.jsp</groupId>
    <artifactId>javax.servlet.jsp-api</artifactId>
    <version>2.3.3</version>
</dependency>
```

配置springmvc.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/mvc https://www.springframework.org/schema/mvc/spring-mvc.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
    <context:component-scan base-package="com.zdy.controller"></context:component-scan>

    <mvc:default-servlet-handler></mvc:default-servlet-handler>

    <mvc:annotation-driven></mvc:annotation-driven>

    <!--配置jsp 视图解析-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" id="internalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/jsp/"></property>
        <property name="suffix" value=".jsp"></property>
    </bean>
</beans>
```

配置web

```xml
<!--配置DispatcherServlet-->
<servlet>
    <servlet-name>SpringMVC</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:springmvc.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>SpringMVC</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
```

配置HelloController

```java
@Controller
public class HelloController {
    @GetMapping("/hello")
    @ResponseBody
    public String hello(){
        return "Hello";
    }
    
}
```

配置index.jsp

```

<form method="post" action="/upload" enctype="multipart/form-data"  >
    <input name="file" type="file">
    <input name="上传文件" type="submit">
</form>

```

分别访问 http://localhost/hello 和 http://localhost/

检测SpringMVC 是否配置正确。

### 2.1 使用 CommonsMultipartResolver 依赖 commons-fileupload 

在springmvc 中添加 id 必须为`multipartResolver`

```xml
  <bean class="org.springframework.web.multipart.commons.CommonsMultipartResolver" id="multipartResolver">
        <!--默认的编码-->
        <property name="defaultEncoding" value="UTF-8"/>
        <!--上传的总文件大小-->
        <property name="maxUploadSize" value="1048576"/>
        <!--上传的单个文件大小-->
        <property name="maxUploadSizePerFile" value="1048576"/>
        <!--内存中最大的数据量，超过这个数据量，数据就要开始往硬盘中写了-->
        <property name="maxInMemorySize" value="4096"/>
        <!--临时目录，超过 maxInMemorySize 配置的大小后，数据开始往临时目录写，等全部上传完成后，再将数据合并到正式的文件上传目录-->
        <property name="uploadTempDir" value="file:///E:\\tmp"/>
    </bean>
```

启动项目： 测试单个文件上传。

### 2.2 StandardServletMultipartResolver

替换bean 

```xml
<bean class="org.springframework.web.multipart.support.StandardServletMultipartResolver" id="multipartResolver">
</bean>
```

在web 中配置

```xml
<multipart-config>
    <location>E:\\temp</location>
    <max-file-size>102400000</max-file-size>
    <max-request-size>102400000</max-request-size>
    <!--这个就是内存中保存的文件最大大小-->
    <file-size-threshold>4096</file-size-threshold>
</multipart-config>
```

同样启动项目测试文件上传。

### 2.3 多个文件上传。

页面

```jsp
<form action="/upload3" method="post" enctype="multipart/form-data">
    <input type="file" name="files" multiple>
    <input type="submit" value="上传">
</form>
```

接口：

```java
@RequestMapping("/upload3")
@ResponseBody
public void upload3(MultipartFile[] files, HttpServletRequest req) {
    String format = sdf.format(new Date());
    String realPath = req.getServletContext().getRealPath("/img") + format;
    File folder = new File(realPath);
    if (!folder.exists()) {
        folder.mkdirs();
    }
    try {
        for (MultipartFile file : files) {
            String oldName = file.getOriginalFilename();
            String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
            file.transferTo(new File(folder, newName));
            String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/img" + format + newName;
            System.out.println(url);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

### 2.4  key 不同的文件

key 不同的，一般前端定义如下：

```jsp
<form action="/upload3" method="post" enctype="multipart/form-data">
    <input type="file" name="file1">
    <input type="file" name="file2">
    <input type="submit" value="上传">
</form>
```



这种，在后端用不同的变量来接收就行了：

```java
@RequestMapping("/upload3")
@ResponseBody
public void upload3(MultipartFile file1, MultipartFile file2, HttpServletRequest req) {
    String format = sdf.format(new Date());
    String realPath = req.getServletContext().getRealPath("/img") + format;
    File folder = new File(realPath);
    if (!folder.exists()) {
        folder.mkdirs();
    }
    try {
        String oldName = file1.getOriginalFilename();
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
        file1.transferTo(new File(folder, newName));
        String url1 = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/img" + format + newName;
        System.out.println(url1);
        String oldName2 = file2.getOriginalFilename();
        String newName2 = UUID.randomUUID().toString() + oldName2.substring(oldName2.lastIndexOf("."));
        file2.transferTo(new File(folder, newName2));
        String url2 = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/img" + format + newName2;
        System.out.println(url2);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```