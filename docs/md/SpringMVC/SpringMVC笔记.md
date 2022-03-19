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