# Java 基础笔记

# day17



# 子类访问权限

子类可以通过 super 关键字访问父类中的资源，但并不是所有的父类资源都可以被子类访问。

访问权限修饰符

public、private、protected、缺省默认（不写）

|           | 同一个类 | 同一个包中 | 不同包     | 子类（同包）            | 子类（不同包）              |
| --------- | -------- | ---------- | ---------- | ----------------------- | --------------------------- |
| public    | 可以访问 | 可以访问   | 可以访问   | 可以访问                | 可以访问                    |
| protected | 可以访问 | 可以访问   | 不可以访问 | 可以访问，super可以访问 | 不可以访问，super可以访问   |
| 缺省默认  | 可以访问 | 可以访问   | 不可以访问 | 可以访问，super可以访问 | 不可以访问，super不可以访问 |
| private   | 可以访问 | 不可以访问 | 不可以访问 | 不可以访问              | 不可以访问                  |

包（package）是 Java 管理文件的一种方式，一个工程可能会出现重名的类，通过包来解决冲突问题。

包的命名方式一般采用公司/组织的倒置域名

default 关键字是用来表示接口中的默认方法的，和访问权限修饰符的缺省默认值没有关系。



# 方法重写

子类在继承父类方法的基础上，对父类方法重新定义并覆盖的操作叫做方法重写。

```java
package com.java.test;

public class People {
    public void show(){
        System.out.println("输出人员信息");
    }
}

package com.java.test;

public class Student extends People {

    @Override
    public void show() {
        System.out.println("这是一个学生");
    }
}
```

构造方法不能被重写，构造方法不能被继承。

方法重写的规则：

1、父子类的方法名必须相同。

2、父子类的方法参数列表必须相同。

3、子类方法返回值与父类方法返回值相同，或者是其子类。

4、子类方法的访问权限不能小于父类，至少和父类一样，或者大于父类。

public > protected > 默认 > private

父类 public，子类 public

父类 protected，子类 public、protected

父类默认，子类 public、protected、默认

父类 private，子类无法重写父类的方法，**重写需要建立在继承的基础上**，必须先有继承，才能有重写，父类的 private 修饰的方法是无法被子类继承的，所以无法重写。

## 方法重写和方法重载

|          | 所在位置         | 方法名 | 参数列表 | 返回值         | 访问权限     |
| -------- | ---------------- | ------ | -------- | -------------- | ------------ |
| 方法重写 | 子类重写父类方法 | 相同   | 相同     | 相同或是其子类 | 不能小于父类 |
| 方法重载 | 同一个类中       | 相同   | 不相同   | 没有要求       | 没有要求     |



# 多态

一个事物可以有多种表现形态，一个类本质是抽象的，实际使用的时候可以有多种不同的表现形态。

Java 程序中，定义一个抽象的方法，只有方法的声明，但是没有实现。在具体的生成环境中根据不同的需求呈现出不同的业务逻辑，很灵活，扩展性更好，实现解耦合。

Java 程序中，定义一个抽象方法，只有方法的声明，但是没有实现。具体的生成环境中根据不同的需求呈现出不同的业务逻辑，很灵活，扩展性很好，实现解耦合。



# day18



# 注解

@Override 它告诉编译器这个方法是用来覆盖父类方法的。

@WebServlet Java Web 开发中表示某个类是一个 Servlet，Web 容器就会识别该类，从而加载运行该类，替代传统的 XML 开发，让程序开发更简洁。

注解和注释

注释完全不影响程序的运行，注解会影响程序的运行。

注释是给开发者看的，注解是给程序看的，编译器、框架、Web 容器。

元注解：用来描述注解的注解，JDK 提供默认的注解，用这些默认注解帮助

开发者自定义需要的注解。

@Target 指定该注解使用的地方（类/方法）

ElementType.TYPE   给类/接口使用

ElementType.METHOD 给方法使用

ElementType.FIELD 给属性使用

ElementType.PARAMETER 给方法参数使用

@Retention 指定注解的保留域

RetentionPolicy.SOURCE	编译器处理，处理之后就不再保留

RetentionPolicy.CLASS	class文件处理，类对应的class文件

RetentionPolicy.RUNTIME	JVM 读取，程序运行时使用

自定义注解

```java
package com.java.servlet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InitMethod {
}
```

注解的使用

```java
package com.java.servlet;

public class InitDemo {

    @InitMethod
    public void init(){
        System.out.println("init...");
    }

    @InitMethod
    public void test(){

    }
}
```

注解的解析

```java
package com.java.servlet;

import java.lang.reflect.Method;

public class Test {
    public static void main(String[] args) throws Exception {
        //获取使用了注解的目标类
        Class clazz = Class.forName("com.java.servlet.InitDemo");
        //检查目标类中哪些方法使用注解
        Method[] methods = clazz.getMethods();
        if(methods!=null){
            for (Method method : methods) {
               boolean isInitMethod = method.isAnnotationPresent(InitMethod.class);
               if(isInitMethod){
                   System.out.println(method.getName() + "使用了InitMethod注解");
               }
            }
        }
    }
}
```

# 多态的使用

**父类引用指向子类对象**，就是多态的一种表现形式。

多态主要有两种表现形式：

1、方法定义时，形参为父类，调用方法时传入的参数是子类对象

```java
public void settlement(Member member){
    member.buyBook();
}
```

```java
OrdinaryMember ordinaryMember = new OrdinaryMember();
cashier.settlement(ordinaryMember);
```

2、方法定义时，返回值为父类，调用方法时返回的是子类对象

```java
public Member getMember(String name){
    if("ordinaryMember".equals(name)){
        return new OrdinaryMember();
    }else if("superMember".equals(name)){
        return new SuperMember();
    }else{
        System.out.println("参数有误");
        return null;
    }
}
```

```java
System.out.println(cashier.getMember("ordinaryMember"));
System.out.println(cashier.getMember("superMember"));
```

父子类之间的类型转换分为两种：向上转型、向下转型

向上转型就是将子类对象转为父类类型，自动完成类型转换

```java
Member member = new OrdinaryMember();
```

向下转型就是将父类对象转为子类类型，无法完成自动转型，需要手动进行强制转换

```java
OrdinaryMember ordinaryMember = (OrdinaryMember) new Member();
```

父类动物 子类猫

猫是动物 

动物是猫

# 抽象方法和抽象类

抽象方法：一个方法只有定义，没有具体实现。

抽象类：一个类中一旦出现抽象方法，该类就必须被定义抽象类。

为什么要有抽象方法？

多态的一种应用，多态场景下，需要定义一个父类，对父类进行编程，具体的实现交给子类完成，子类就需要对父类方法进行重写，也就是说父类方法无论怎么定义，最终都会被子类覆盖，那么父类的方法就不需要定义了，所以就需要将该方法定义为抽象的。

为什么一旦有了抽象方法，类就必须是抽象的？

抽象方法只是一种声明，没有具体实现，意味着方法所在的类不能被实例化，因为一旦实例化该类，意味着可以直接调用方法，但是方法是没有实现，所以不能调用。

**抽象方法所在的类不能被实例化**

将类定义成抽象类，抽象类的特点是不能被实例化。

 

抽象类/抽象方法如何使用

通过继承来实现多态，定义一个子类，继承抽象类，子类中实现抽象方法，写程序的时候操作子类即可。

如果子类也不想实现抽象方法，只需要将子类也定义成抽象类即可，相当于将实现抽象方法的任务再交给它的子类了。



# day19

# Object

Java 类分三种：

1、JDK 提供的官方类库

2、第三方提供的类库（工具、框架）

3、开发自定义的类

Object 是 JDK 中的一个类，java.lang 包中，该类是所有类的直接父类或间接父类。

Java 程序中的每个类都是 Object 的后代。

```java
package com.java.test;

public class Test {
    public void test() throws Exception{
        hashCode();
        getClass();
        equals(null);
        clone();
        toString();
        notify();
        notifyAll();
        wait();
        wait(100L);
        wait(100L, 100);
    }
}
```

## 重写 Object 类的方法

| 方法                              | 描述                         |
| --------------------------------- | ---------------------------- |
| public String toString()          | 以字符串的形式返回对象的信息 |
| public boolean equals(Object obj) | 判断两个对象是否相等         |
| public native int hashCode()      | 返回对象的hash值             |

**== 比较的永远是变量的值**

变量的值就是变量中存储的数据

hashCode 方法用来获取对象的散列值，散列值是根据对象的内存地址和对象的信息映射出来的一个数值。

任何两个对象的内存地址肯定不一样，但是实际开发中如果两个对象的属性值完全一致，就认为是同一个对象，那么它们的散列值也是相等的。



eqauls 方法用来判断两个对象是否相等，hashCode 方法是返回对象的哈希值，实际开发中真正判断两个对象是否相等，需要结合 eqauls 和 hashCode 方法一同来判断。

为什么？为了提高效率。

判断两个对象是否相等

1、先判断两个对象的 hashCode 是否相等，如果不相等，则两个对象一定不是同一个对象，但是如果相等，则不能确定两个对象一定相等。

```
2*6 = 3*4
```

2、再通过 equals 方法进行进一步的判断，最终得出两个对象是否相等的结论。

hashCode 执行效率高于 equals，优先使用 hashCode 进行判断，如果 hashCode 能确定结果，则不需要执行效率低的 equals，除非 hashCode 无法确定结果，不得不执行 equals 的时候再去执行 equals 得出结果。

表达式1 & 表达式2 两个表达式都要执行

表达式1 && 表达式2 表达式1 = false，表达式2 就不需要执行了，从而提升效率

String 的比较



# 包装类

包装类是 Java 提供的一组类，专门用来创建基本数据类型对应的对象，一共有 8 个，都保存在 java.lang 包中。

| 基本数据类型 | 包装类    |
| ------------ | --------- |
| byte         | Byte      |
| short        | Short     |
| int          | Integer   |
| long         | Long      |
| float        | Float     |
| double       | Double    |
| char         | Character |
| boolean      | Boolean   |

包装类的体系结构

![image-20210122135349946](C:\Users\zhaid\AppData\Roaming\Typora\typora-user-images\image-20210122135349946.png)

# day20

# 包装类

## 装箱和拆箱

装箱：基本数据类型-》包装类

public Type(type value) 

public Type(String value)

以上两个方法不推荐使用

public static valueOf(type value) 静态工厂方法

静态工厂方法是专门用来创建对象的一个静态方法，功能上与构造器一样，都是用来创建对象的。

获取一个类的实例化对象，有两条路可以选择

1、构造器

2、静态工厂方法

区别：

1、静态工厂方法可以有自定义的名字。

2、静态工厂方法在需要的情况下可以保证单例模式。



拆箱：包装类-》基本数据类型

*Value()

parse*(String value)



# 接口

接口是由抽象类衍生出来的概念，抽象类是指类中至少存在一个抽象方法，抽象类是由抽象方法和非抽象方法组成的。

接口就是极度抽象的抽象类，类中所有方法都是抽象方法，没有非抽象方法。

接口主要用来实现解耦合。

面向接口编程的优点：

1、能够最大限度的解耦合，降低程序的耦合度。

2、使程序易于扩展。

3、有利于程序的后期维护。

## 如何使用接口

接口在 Java 中是独立存在的一种结构，和类相似。

```
public interface 接口名{
	方法;
}
```

接口中的方法全部都是抽象方法，同时接口中允许存在成员变量，有要求：

1、不能定义 private 和 protected 修饰的成员变量，只能定义 public 和默认成员变量。

2、接口中的成员变量在定义时必须初始化。

3、接口中的成员变量都是静态常量，可以通过接口之间访问，同时值不能修改(使用final 修饰)。

使用接口，需要有实现类，实现类来实现接口中的抽象方法，把抽象的内容具体化。

1、用普通Java类实现接口，重写（实现）接口中的抽象方法。

2、如果实现类不希望重写方法，将实现类定义成抽象类即可。

3、接口不能实现接口。

4、接口可以继承接口。

接口其实是对继承的一种扩展，都是多态的体现。

扩展什么呢？

继承的原则是单继承，一个类只能有一个父类，但是接口是没有这个限制，一个实现类可以同时实现多个接口。



## 面向接口编程的实际应用

面向对象编程 OOP 是一种思想，面向接口编程是一种具体的开发方式。

工厂生产产品A，需要用设备A来生产

Factory、EquipmentA

```java
package com.southwind.com.southwind.test3;

public class EquipmentA {
    public void work(){
        System.out.println("设备A运行，生产产品A...");
    }
}
```

```java
package com.southwind.com.southwind.test3;

public class Factory {
    private EquipmentA equipmentA;

    public EquipmentA getEquipmentA() {
        return equipmentA;
    }

    public void setEquipmentA(EquipmentA equipmentA) {
        this.equipmentA = equipmentA;
    }

    public void produce(){
        System.out.println("开始生产...");
        this.equipmentA.work();
    }
}
```

```java
package com.southwind.com.southwind.test3;

public class Test {
    public static void main(String[] args) {
        EquipmentA equipmentA = new EquipmentA();
        Factory factory = new Factory();
        factory.setEquipmentA(equipmentA);
        factory.produce();
    }
}
```

工厂接了新订单，生产产品B，必须由设备B来生产，工厂中只能有一台设备。

```java
package com.southwind.com.southwind.test3;

public interface Equiment {
    public void work();
}
```

```java
package com.southwind.com.southwind.test3;

public class Factory {
    private Equiment equiment;

    public Equiment getEquiment() {
        return equiment;
    }

    public void setEquiment(Equiment equiment) {
        this.equiment = equiment;
    }

    public void produce(){
        System.out.println("开始生产...");
        this.equiment.work();
    }
}
```

```java
package com.southwind.com.southwind.test3;

public class Test {
    public static void main(String[] args) {
        EquipmentA equipmentA = new EquipmentA();
        EquipmentB equipmentB = new EquipmentB();
        EquipmentC equipmentC = new EquipmentC();
        Factory factory = new Factory();
        factory.setEquiment(equipmentC);
        factory.produce();
    }
}
```

# day 线程安全的单例模式

```java
public class SingleTon{
    private static SingleTon  instance;
    private SingleTon(){
        
    }
    
    public static SingleTon getInsttance(){
        if(instance == null){
            instance = new Stance;
        }
        return instance;
    }
}
```

线程死锁是一种互斥状态。



# day 42

# Servlet 

Servlet 是一个单例模式

当第一次创建Servlet 实例，

Sevlet 接口



# 集合框架笔记

Collection 接口

List 接口

Set 接口

ArraryList x