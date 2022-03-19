# spring 学习笔记

Spring boot 2X 基于Spring 5

Spring 项目在整个项目开发中，它的作用主要是用来整合其他框架的

Spring 框架的两大核心机制 Ioc 和AOP

IoC : 控制反转。 inverse  of Controll

控制反转值得是对象的创建方式进行反转。

传统的方式是程序员自己创建对象，

IOC 是指将这一过程进行反转，程序员不需要自己创建对象，而是交给IOC 容器来创建对象，程序员只需要使用这些对象即可。

为什么要使用控制反转？控制反转的好处是什么？

解耦合

Java  Web  部署的两种方式

war: Servlet 完成的，需要手动把工程达成 war ,在部署在 tomcat 中。

jar ： Spring Boot 完成的，内嵌一个 Tomcat ,只需要运行jar 即可。

Java Web 是分层的，将业务功能模块划分为不同的层，将代码分为不同的层，每层都负责各种任务，一起来协同工作完成任务。

MVC 设计模式

Model : 模型层，提供业务数据模型

View:  视图层，负责给用户展示数据结果，JSP，Thymeleaf, HTML

Controller: 控制层，负责整个程序的运行的，接受客户端请求并作出相应。



在不修改Java 代码的情况下，实现业务模块的切换

我们只需要给一个配置文件

在配置文件中进行业务切换。通过反转

完成MVC 分层结构。

IOC 思想。将创建对象的控制权进行了反转。实现解耦合。



---------------------------------------------------------------------------------------

# Spring IOC

spring IOC 是将创建对象的权利进行反转，交给IOC 容器

而不是开发者，开发者直接使用IOC 生成的对象操作即可。

* 基于 XML

  1. 创建一个XML 文件，在文件中将程序需要用到的对象进行配置，
  2. 直接加载IOC 容器，Spring 框架会自动读取XML 文件，根据XML 配置，自动创建各种对象放入缓冲池中。
  3. 开发者只需要在缓冲池中取出对象使用即可。

  创建bean 默认单例模式，在bean标签内 内使用scope.

  默认通过无参构造创建对象。

  也可以通过有参构造创建对象

  给bean 赋值初始值，实体类要使用set方法。

* 基于注解

1. 在需要注入Ioc 的类处添加注解，标记一下，告诉Spring IOC 容器，这个类是需要注入的IOC。

2. 加载到Spring 容器中，Spring 框架会自动读取到所有添加了注解的类，通过反射机制创建bean,注入到Ioc 容器中。需要指定要扫描的包的注解。
3. 基于注解的方式，bean 默认的id 名是类名首字母小写。

完成对象的级联

* @Autowired, 自动装载。根据类名，自动装载，可以分为根据类型装载，根据Id 名进行装载。 

* @Qualifier("根据id名")

# Spring IoC 源码

1.1 基于XML 

## 1.2 基于注解

1、 IoC 容器加载的时候，遍历指定包名下的所有类

2、判断这些类是否加载了@Component ,把所有添加了@ Component 注解的类数据收集（beanid, className）,存入集合中

3、将集合传递给下一个单元，遍历这个集合获取目标数据

4、通过反射机制创建bean

5、将创建的bean 以键值对的方式存入IoC 容器。

# 手写一个基于注解的IOC 容器。

1. 自定义一个myAnnotionConfigApplicationContext.构造器中传入要扫描的包。
2. 获取这个包下的所有类
3. 遍历这些类，找出添加了Component 注解的类，获取响应的beanName. beanClass , 封装成 BeanDefinition 对象，存入set 集合。这些就是Ioc 自动装载的原材料。

4. 遍历set 集合，通过反射机制创建对象。通过反射机制检测属性是否有@Value 注解，有的化就赋值，没有的话就不赋值。
5. 将上面创建的bean 以k-v 的形式存入cach Map。
6. 提供getBean 方法。通过beanName 从缓存去取出对应的bean.

# Spring 面向切面编程

AOP AOP: Aspect Oriented Programming 面向切面编程

OOP： Objec Oriented  Programming 面向对象编程。

程序运行时动态地将非业务代码切入到程序运行的前后。实现代码的解耦合。