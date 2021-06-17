# UML图



## 类与类之间的关系

![img](E:\Code\deyou123.github.io\docs\markdown\设计模式\o_200409025533uml.png)

# 依赖

只要类中能用到了对方，他们之间就存在依赖关系

* 类中用到了对象
* 类的成员属性
* 方法的返回值类型
* 方法的接受参数
* 方法中使用到

# 泛化

就是继承

## 实现

就是接口实现

为什么把这三个放在一起呢？

因为三者在代码上的表现相同，只是语意上有所差别。

# 关联



类与类之间的联系，关系具有多重性，如：

```
“1”（表示有且仅有一个）

“0...”（表示0个或者多个）

“0，1”（表示0个或者一个）

“n...m”(表示n到 m个都可以)

“m...*”（表示至少m个）
```

# 聚合：

部分可以离开整体单独存在，举一个例子。学校类中有一个学生类。当我们创建一个学校类的时候，可能因为刚创办，还没有学生。所以学生类是可以不存在的。不影响学校类的创建。他是依赖关系的特例(真子集)。

# 组合：

整体和部分同生共死，部分脱离整体会变得毫无意义，强调同生共死的一致的生命周期。

例如学生类中的身份证证类。每个学生肯定都会有身份证。在学生类被实例化成功以后，身份证类也被实例化成功。学生类是不能脱离身份证类单独存在的。他是依赖关系的特例(真子集)。

```java
//学校
public class School {
    public List<Student> studnets;
}


//学生
public class Student {
   private IdentityCard identityCard = new IdentityCard(); //组合关系，创建student的时候也创建了身份证
}

//身份证
public class IdentityCard {
   private String id = UUID.randomUUID().toString();
}
```

一个学校有很多个学生，一个学生只有一个身份证。所以上面三个类的UML图如下：
![img](https://images.cnblogs.com/cnblogs_com/zhxiansheng/1455495/o_200409025456Association.png)

首先我们来看实线箭头，箭头方向指的是依赖的方向。School箭头指向Student标注1 * 表示：一个学校有多个学生。

Student的实线箭头指向IdentityCard并且标注的是1 1表示：一个学生只有一个学校和一个身份证。

然后我们看菱形图。正常来说，聚合关系应该是空心的菱形图，组合关系才是实心的菱形图，但是IDEA的集成工具将聚合和依赖关系都以实心菱形图来表示。

按照标准的uml图来说，student和school之间应该是空心的菱形图。IdentityCard与Student才是实线的菱形图。