# SSM 框架整合

三者的分工

1. spring MVC 负责客户端和Java 后台的交互。
2. Mybatis 负责Java 后台与数据库的交互。
3. Spring  负责Spring MVC 和Mybatis 的交互。



## 1. pom.xml

创建maven-web 工程。

```xml
  <dependencies>
    <!-- SpringMVC -->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>5.3.4</version>
    </dependency>

    <!-- Spring JDBC -->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-jdbc</artifactId>
      <version>5.3.4</version>
    </dependency>

    <!-- Spring AOP -->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-aop</artifactId>
      <version>5.3.4</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-aspects</artifactId>
      <version>5.3.4</version>
    </dependency>

    <!-- MyBatis -->
    <!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis</artifactId>
      <version>3.5.6</version>
    </dependency>


    <!-- MyBatis 整合 Spring -->
    <!-- https://mvnrepository.com/artifact/org.mybatis/mybatis-spring -->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis-spring</artifactId>
      <version>2.0.6</version>
    </dependency>

    <!-- MySQL 驱动 -->
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>8.0.22</version>
    </dependency>

    <!-- C3P0 -->
    <!-- https://mvnrepository.com/artifact/com.mchange/c3p0 -->
    <dependency>
      <groupId>com.mchange</groupId>
      <artifactId>c3p0</artifactId>
      <version>0.9.5.5</version>
    </dependency>


    <!-- JSTL -->
    <dependency>
      <groupId>jstl</groupId>
      <artifactId>jstl</artifactId>
      <version>1.2</version>
    </dependency>

    <!-- ServletAPI -->
    <!-- https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api -->
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>javax.servlet-api</artifactId>
      <version>4.0.1</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <version>1.18.16</version>
    </dependency>
  </dependencies>
```



## 2. 配置 web.xml

```xml
<web-app>
  <display-name>Archetype Created Web Application</display-name>
  <!-- 启动 Spring -->
  <context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:applicationContext.xml</param-value>
  </context-param>
  <listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>

  <!-- Spring MVC 的前端控制器，拦截所有请求 -->
  <servlet>
    <servlet-name>mvc-dispatcher</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>classpath:springmvc.xml</param-value>
    </init-param>
  </servlet>

  <servlet-mapping>
    <servlet-name>mvc-dispatcher</servlet-name>
    <url-pattern>/</url-pattern>
  </servlet-mapping>

  <!-- 字符编码过滤器，一定要放在所有过滤器之前 -->
  <filter>
    <filter-name>CharacterEncodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
      <param-name>encoding</param-name>
      <param-value>utf-8</param-value>
    </init-param>
    <init-param>
      <param-name>forceRequestEncoding</param-name>
      <param-value>true</param-value>
    </init-param>
    <init-param>
      <param-name>forceResponseEncoding</param-name>
      <param-value>true</param-value>
    </init-param>
  </filter>
  <filter-mapping>
    <filter-name>CharacterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>

  <!-- 加载静态资源 -->
  <servlet-mapping>
    <servlet-name>default</servlet-name>
    <url-pattern>*.js</url-pattern>
  </servlet-mapping>

  <servlet-mapping>
    <servlet-name>default</servlet-name>
    <url-pattern>*.css</url-pattern>
  </servlet-mapping>
</web-app>




```

## 3. 配置applicationContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:aop="http://www.springframework.org/schema/aop"
    xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.3.xsd
		http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
		http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.3.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.3.xsd">
	
	<!-- 加载外部文件 -->
	<context:property-placeholder location="classpath:dbconfig.properties"/>
	
	<!-- 配置 C3P0 数据源 -->
	<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
		<property name="user" value="${jdbc.user}"></property>
		<property name="password" value="${jdbc.password}"></property>
		<property name="driverClass" value="${jdbc.driverClass}"></property>
		<property name="jdbcUrl" value="${jdbc.jdbcUrl}"></property>
		<property name="initialPoolSize" value="5"></property>
		<property name="maxPoolSize" value="10"></property>
	</bean>
	
	<!-- 配置MyBatis SqlSessionFactory -->
	<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		<!-- 指定MyBatis 数据源 -->
		<property name="dataSource" ref="dataSource"/>
		<!-- 指定MyBatis mapper文件的位置 -->
		<property name="mapperLocations" value="classpath:com/zdy/mapper/*.xml"/>
		<!-- 指定MyBatis全局配置文件的位置 -->
		<property name="configLocation" value="classpath:mybatis-config.xml"></property>
	</bean>
	
	<!-- 扫描MyBatis的mapper接口 -->
	<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
		<!--扫描所有Repository接口的实现，加入到IoC容器中 -->
		<property name="basePackage" value="com.zdy.mapper"/>
	</bean>
	
	<!-- 配置事务管理器 -->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <!-- 控制住数据源 -->
        <property name="dataSource" ref="dataSource"></property>
    </bean>
    
    <!-- 配置事务增强，事务如何切入  -->
    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <tx:attributes>
            <!-- 所有方法都是事务方法 -->
            <tx:method name="*"/>
            <!-- 以get开始的所有方法  -->
            <tx:method name="get*" read-only="true"/>
        </tx:attributes>
    </tx:advice>
	
	<!-- 开启基于注解的事务  -->
    <aop:config>
        <!-- 切入点表达式 -->
        <aop:pointcut expression="execution(* com.zdy.service.impl.*.*(..))" id="txPoint"/>
        <!-- 配置事务增强 -->
        <aop:advisor advice-ref="txAdvice" pointcut-ref="txPoint"/>
    </aop:config>
</beans>
```

## 4. 配置数据库

```xml
jdbc.jdbcUrl=jdbc:mysql://localhost:3306/ssm?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
jdbc.driverClass=com.mysql.cj.jdbc.Driver
jdbc.user=root
jdbc.password=666666

```

## 5. 配置springmvc.xml

```html
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xmlns:mvc="http://www.springframework.org/schema/mvc"
    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
        http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd
        http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc-3.2.xsd">
	
	<!-- 告知spring，启用springmvc的注解驱动 -->
	<mvc:annotation-driven />
	
	<!-- 扫描业务代码 -->
    <context:component-scan base-package="com.zdy"></context:component-scan>
    
    <!-- 配置视图解析器 -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<property name="prefix" value="/"></property>
		<property name="suffix" value=".jsp"></property>
	</bean>
    
</beans>

```

## 6. 配置mybtsi-config.xml

```html
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xmlns:mvc="http://www.springframework.org/schema/mvc"
    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
        http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd
        http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc-3.2.xsd">
	
	<!-- 告知spring，启用springmvc的注解驱动 -->
	<mvc:annotation-driven />
	
	<!-- 扫描业务代码 -->
    <context:component-scan base-package="com.zdy"></context:component-scan>
    
    <!-- 配置视图解析器 -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<property name="prefix" value="/"></property>
		<property name="suffix" value=".jsp"></property>
	</bean>
    
</beans>

```

## 7. 准备数据库

```sql
create database ssm;
use ssm;
```

```sql
CREATE TABLE `department` (
  `d_id` int(11) NOT NULL AUTO_INCREMENT,
  `d_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`d_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

INSERT INTO `department` VALUES ('1', '研发部');
INSERT INTO `department` VALUES ('2', '销售部');
INSERT INTO `department` VALUES ('3', '行政部');
    
```

```sql
CREATE TABLE `department` (
  `d_id` int(11) NOT NULL AUTO_INCREMENT,
  `d_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`d_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

INSERT INTO `department` VALUES ('1', '研发部');
INSERT INTO `department` VALUES ('2', '销售部');
INSERT INTO `department` VALUES ('3', '行政部');

    
```

## 8 . 创建 Department.java

```java
@Data
public class Department {
    private int id;
    private String name;
}

```

## 9. 创建 DepartmentMapper 接口 和 DepartmentMapper.xml

```java
public interface DepartmentMapper {
    public List<Department> findAll();
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org/DTD Mapper 3.0" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.zdy.mapper.DepartmentMapper">
<resultMap id="departmentMap" type="com.zdy.entity.Department">
    <id property="id" column="d_id"></id>
    <result property="name" column="d_name"></result>
</resultMap>
    <select id="findAll" resultMap="departmentMap">
    select * from department
</select>

</mapper>
```

## 10.  创建 DepartmentService 和 DepartmentServieImpl 类

```java
public interface DepartmentService {
    public List<Department> findAll();
}
```

```java
@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;
    @Override
    public List<Department> findAll() {
        return departmentMapper.findAll();
    }
}
```

## 11. 创建 DepartmentController

```java
package com.zdy.controller;

import com.zdy.entity.Department;
import com.zdy.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @Author: Deyou
 * @Date: 2021/4/3
 */
@Controller
@RequestMapping("/d")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;
    @GetMapping("/findAll")
    public ModelAndView findAll(){
        List<Department> list = departmentService.findAll();
        for ( int  i =  0 ;i < list.size();i++){
            System.out.println(list.get(i).getId());
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("list",list);
        return modelAndView;
    }
}

```

## 12 .创建视图层index.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Title</title>
    </head>
    <body>
        <h1>department</h1>
        <c:forEach items="${list}" var="department">
            ${department.id}----${department.name}<br/>
        </c:forEach>
    </body>
</html>

```

注意mapper.xml 在Java 文件包下，程序运行无法找到，解决办法

pom.xml 中添加如下代码

```xml
<build>
    <resources>
        <resource>
            <directory>src/main/java</directory>
            <includes>
                <include>**/*.xml</include>
            </includes>
        </resource>
    </resources>
</build>
```

配置 tomcat 启动项目。访问 http://locahost:8080/d/findAll. 结果如下

![image-20210403161419637](C:\Users\zhaid\AppData\Roaming\Typora\typora-user-images\image-20210403161419637.png)

到这里一个简单的ssm 框架搭建完成。

------------------------------------------------------------------------------------------------------------------------------------------------

# 接下来使用boostrap  实现employee 的查询功能。

从上述第8步开始

## 1. 创建 Employee类

```html
@Data
public class Employee {
    private Integer id;
    private String name;
    private String gender;
    private String email;
    private String tel;
    private Department department;
}
```

## 2. 创建 Mapper 接口和配置文件。

```java
public interface EmployeeMapper {
    public List<Employee> queryAll();
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org/DTD Mapper 3.0" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.zdy.mapper.EmployeeMapper">
    <resultMap id="employeeMap" type="com.zdy.entity.Employee">
        <id property="id" column="d_id"></id>
        <result property="name" column="d_name"></result>
        <result property="gender" column="e_gender"></result>
        <result property="email" column="e_email"></result>
        <result property="tel" column="e_tel"></result>

        <association property="department" javaType="com.zdy.entity.Department">
            <id property="id" column="d_id"></id>
            <result property="name" column="d_name"></result>
        </association>
    </resultMap>
    <select id="queryAll" resultMap="employeeMap">
        select * from employee e, department d where e.d_id = d.d_id
    </select>

</mapper>
```

## 3. 创建service 层

```java
public interface EmployeeService {
    public List<Employee> queryAll();
}
```

```java
@Service
public class EmployeeServiceImpl implements EmployeeService {
   @Autowired
    private EmployeeMapper employeeMapper;
    @Override
    public List<Employee> queryAll() {
        return this.employeeMapper.queryAll();
    }
}

```

## 4. 创建controller 层

```java
@Controller
@RequestMapping("/e")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @GetMapping("/queryAll")
    public ModelAndView queryAll(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("employee");
        modelAndView.addObject("employees",this.employeeService.queryAll());
        return modelAndView;
    }
}

```

## 5. 创建视图

```jsp
<html>
    <head>
        <meta />
        <title>Employee  </title>
        <link rel="stylesheet" type="text/css" href="/static/bootstrap/css/bootstrap.min.css"/>
        <script src="/static/js/jquery-3.6.0.js"></script>
        <script src="/static/bootstrap/js/bootstrap.js"></script>
    </head>

    <body>
        <div class="container">
            <!-- 标题 -->
            <div class="row">
                <div class="col-md-12">
                    <h1>SSM-员工管理</h1>
                </div>
            </div>
            <!-- 显示表格数据 -->
            <div class="row">
                <div class="col-md-12">
                    <table class="table table-hover" id="emps_table">
                        <thead>
                            <tr>
                                <th>
                                    <input type="checkbox" id="check_all"/>
                                </th>
                                <th>编号</th>
                                <th>姓名</th>
                                <th>性别</th>
                                <th>电子邮箱</th>
                                <th>联系电话</th>
                                <th>部门</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${employees }" var="employee">
                                <tr>
                                    <td><input type='checkbox' class='check_item'/></td>
                                    <td>${employee.id }</td>
                                    <td>${employee.name }</td>
                                    <td>${employee.gender }</td>
                                    <td>${employee.email }</td>
                                    <td>${employee.tel }</td>
                                    <td>${employee.department.name }</td>
                                    <td>
                                        <button class="btn btn-primary btn-sm edit_btn">
                                            <span class="glyphicon glyphicon-pencil">编辑</span>
                                        </button>&nbsp;&nbsp;
                                        <button class="btn btn-danger btn-sm delete_btn">
                                            <span class="glyphicon glyphicon-trash">删除</span>
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>

</html>

```

## 6. 启动项目

访问http://localhost:8080/e/queryAll .结果如下图

![image-20210403162316898](C:\Users\zhaid\AppData\Roaming\Typora\typora-user-images\image-20210403162316898.png)

到这里项目简单的ssm 项目搭建完毕。有兴趣可以自行添加增删改功能。

完结撒花！