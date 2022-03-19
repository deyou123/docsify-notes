# 创建第一个Spring MVC 项目

1. 使用 maven 创建一个web 项目。

![image-20210309184338681](C:\Users\zhaid\AppData\Roaming\Typora\typora-user-images\image-20210309184338681.png)

2. 引入依赖

```xml
<dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>5.3.4</version>
    </dependency>
```

3. 配置web.xml

```xml
<!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd" >

<web-app>
  <display-name>Archetype Created Web Application</display-name>
  <servlet>
    <servlet-name>dispatcherServlet</servlet-name>
    <servlet-class >org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>classpath:spring-mvc.xml</param-value>
    </init-param>
  </servlet>
  <servlet-mapping>
    <servlet-name>dispatcherServlet</servlet-name>
    <url-pattern>/</url-pattern>
  </servlet-mapping>
</web-app>

```

4. 配置spring-mvc.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
    <!--扫描组件-->
    <context:component-scan base-package="com.zdy"/>
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/"/>
        <property name="suffix" value=".jsp"/>
    </bean>
</beans>
```

5. 配置controller

```java
package com.zdy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author: Deyou
 * @Date: 2021/3/9
 */
@Controller
public class HellController {
    @GetMapping("/index")
    public ModelAndView hello(ModelAndView modelAndView){
        modelAndView.addObject("mess","Hello SrpingMVC");
        modelAndView.setViewName("index");
        return modelAndView;
    }
}

```

6. 在WEB-INF/下新建index.jsp

```html
<%--
  Created by IntelliJ IDEA.
  User: zhaid
  Date: 2021/3/9
  Time: 18:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<p>${mess}</p>
</body>
</html>

```

配置tomcat，启动项目

访问 http://localhost:8080/index

输出 Hello SrpingMVC，项目完成。



