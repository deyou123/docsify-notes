# Spring MVC 学习笔记

# 1 Spring MVC RestFul 风格

Spring MVC 也支持RESTful 风格的url 参数获取。

具体表现形式。

* 对数据的操作就是增删改查，Restful 对增删改查中分别做出了要求，使用不同的请求类型执行不同的操作

Get： 查询数据

Post : 添加数据

Put： 修改数据

Delete： 删除数据

​	Json {

​	name : "zhangsan"，

​	age ： 20，

}

* 