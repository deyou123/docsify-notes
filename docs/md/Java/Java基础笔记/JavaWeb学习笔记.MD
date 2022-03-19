# 实验一： 使用Servlet 和 JSP 模拟用户登录功能

新建一个web 项目，记得添加jsp 和 Servlet jar 包到工程中。

1. 用户名admin 密码 admin, 输入错误，重定向到登录页面。
2. 输出正确进入到欢迎页面。

项目文件路径

![image-20210202015557719](C:\Users\zhaid\AppData\Roaming\Typora\typora-user-images\image-20210202015557719.png)

login.jsp

```html
<%--
  Created by IntelliJ IDEA.
  User: zhaid
  Date: 2021/2/2
  Time: 1:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="login" method="post">
    <input type="text" name="username"/>
    <input type="password" name="password"/>
    <input type="submit" name="提交" value="登录"/>
</form>
</body>
</html>

```

创建欢迎页面

welcome.jsp

```java
<%--
  Created by IntelliJ IDEA.
  User: zhaid
  Date: 2021/2/2
  Time: 1:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    String name = (String) request.getAttribute("name");
%>

你好<span> <%=name%></span>,欢迎回来

</body>
</html>

```

创建LoginServlet.java

```java
package com.zdy;



import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("执行doPost");
        String username = (String) req.getParameter("username");
        String password = (String) req.getParameter("password");
        if("admin".equals(username) && "admin".equals(password)){
            req.setAttribute("name",username);
            //转发
            req.getRequestDispatcher("welcome.jsp").forward(req,resp);
        }else{
            //重定向
            resp.sendRedirect("login.jsp");
        }
    }
}

```

启动项目

访问 http://localhost:8080/login.jsp 进行测试。