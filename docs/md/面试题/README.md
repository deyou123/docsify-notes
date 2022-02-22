# Spring 面试题



1. 谈谈Spring IOC 的理解原理和实现？

控制反转: 理论思想，原理的对象是由使用者进行控制，

有了Spring之后，可以把整个对象交给spring 来帮我进行管理

DI ：把对应属性的值注入到具体的对象中去。 

可以

容器： 存储对象，使用map 结构存储，

在spring中一般存在三级缓存，

singletonObject 存放完整的bean 对象。

整个bean 的声明周期，从创建到销毁的过程都是由容器来管理的。



IOC 容器的创建过程。（beanFacatory,defaultListableBeanFactory） 

2.加载解析bean 对象，xmL 或者使用注解解析



1. 谈一下Spring IOC 的的底层实现？
2. 描述一下bean 的声明周期？
3. spring 如何解决循环依赖问题？
4. Bean Factroy 和 Factory Bean 又什么区别？
5. Spring 中用到的设计模式？
6. Spring AOP 的底层实现原理？
7. Spring 事务是如何回滚的？
8. 谈一下Spring事务传播？

