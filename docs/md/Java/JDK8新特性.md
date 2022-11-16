JDK 8  新特性

1.Lambda表达式
2.接口的增强
3.常用的内置函数式接口
4.方法引用
5.集合之Stream流式操作
6.Optional中避免Null检查
7.新的时间和日期API
8.可重复注解
9.并行数组排序

# JDK8新特性(一)之Lambda表达式

Java8发布于2014年03月18日，是目前企业中使用最广泛的一个版本。Java8是一次重大的版本升级，带来了很多的新特性：
Oracle JDK是基于Open JDK源代码的商业版本。如果要学习Java新特性可以去Open JDK官网学习，其官网地址为http://openjdk.java.net/。JDK Enhancement Proposals（JDK增强建议），通俗的讲JEP就是JDK的新特性。

## 一、Lambda表达式的介绍

一般情况下，线程类执行具体任务的时候，需要创建一个实现Runnable接口的匿名内部类，如下所示：

```java
public class Demo {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("新线程执行代码啦!");
            }
        }).start();
    }
}
```


Lambda表达式体现的是函数式编程思想，只需要将要执行的代码放到函数中(函数就是类中的方法)；Lambda就是一个匿名函数，我们只需要将要执行的代码放到Lambda表达式中

```java
public class Demo {
    public static void main(String[] args) {
        new Thread(()->{
            System.out.println("新线程执行代码啦!");
        }).start();
    }
}
```

Lambda表达式的好处：可以简化匿名内部类，让代码更加精简

## 二、Lambda表达式的标准格式

Lambda表达式的标准格式由3个部分组成：

> (参数类型 参数名称)->{
>     代码体;
> }

(参数类型 参数名称)：参数列表

{代码体;}：方法体

->：箭头表示的是分隔参数列表和方法体，没有实际含义，起到连接的作用

Lambda表达式是一个匿名函数，而函数相当于Java中方法

1.练习无参数无返回值的Lambda表达式

```java
public interface Swimmable {
    public abstract void swimming();
}
```

```java
public class Demo {
    public static void main(String[] args) {
        goSwimming(new Swimmable() {
            @Override
            public void swimming() {
                System.out.println("我是匿名内部类的游泳");
            }
        });
    }
 
    public static void goSwimming(Swimmable s){
        s.swimming();
    }
}
```




执行上述代码，其输出结果为：

> 我是匿名内部类的游泳



如果是使用Lambda表达式来对上述代码进行简写，其可以修改为：

```java
public class Demo {
    public static void main(String[] args) {
        goSwimming(() -> {
            System.out.println("我是Lambda表达式中的游泳");
        });
    }
 
    public static void goSwimming(Swimmable s) {
        s.swimming();
    }
}
```

执行上述代码，其输出结果为：

> 我是Lambda表达式中的游泳



小结：以后看到方法的参数是接口就可以考虑使用Lambda表达式，我们可以认为Lambda表达式就是对接口中的抽象方法的重写

2.练习有参数有返回值的Lambda表达式

```java
public interface Smokeable {
    public abstract int smoking(String name);
}
```

```java
public class Demo {
    public static void main(String[] args) {
        goSmoking(new Smokeable() {
            @Override
            public int smoking(String name) {
                System.out.println("抽了"+name+"的烟");
                return 5;
            }
        });
    }
 
    public static void goSmoking(Smokeable s){
        int i=s.smoking("中华");
        System.out.println("返回值："+i);
    }
}
```





执行上述代码，其输出结果为：

> 抽了中华的烟
> 返回值：5



如果是使用Lambda表达式来对上述代码进行简写，其可以修改为：

```java
public class Demo {
 
    public static void main(String[] args) {
        goSmoking((String name)->{
            System.out.println("Lambda抽了"+name+"的烟");
            return 5;
        });
    }
 
    public static void goSmoking(Smokeable s){
        int i=s.smoking("中华");
        System.out.println("返回值："+i);
    }
}
```

执行上述代码，其输出结果为：



> Lambda抽了中华的烟
> 返回值：5

以前对于List列表的排序，我们会实现Comparator接口，并重写里面的compare方法：

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
 
public class Demo {
 
    public static void main(String[] args) {
        ArrayList<Person> persons=new ArrayList<>();
        persons.add(new Person("刘德华",58,174));
        persons.add(new Person("张学友",58,176));
        persons.add(new Person("张国荣",54,171));
        persons.add(new Person("黎明",53,178));
 
        Collections.sort(persons, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o2.getAge()-o1.getAge();
            }
        });
        for (Person person:persons){
            System.out.println(person);
        }
    }
}
```



>Person(name=刘德华, age=58, height=174)
>Person(name=张学友, age=58, height=176)
>Person(name=张国荣, age=54, height=171)
>Person(name=黎明, age=53, height=178)



那么现在使用Lambda表达式来实现的话，则可以修改为如下所示：

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
 
public class Demo {
 
    public static void main(String[] args) {
        ArrayList<Person> persons=new ArrayList<>();
        persons.add(new Person("刘德华",58,174));
        persons.add(new Person("张学友",58,176));
        persons.add(new Person("张国荣",54,171));
        persons.add(new Person("黎明",53,178));
 
        Collections.sort(persons, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o2.getAge()-o1.getAge();
            }
        });
        for (Person person:persons){
            System.out.println(person);
        }
    }
}
```



执行上述代码，其输出结果为：

>Person(name=刘德华, age=58, height=174)
>Person(name=张学友, age=58, height=176)
>Person(name=张国荣, age=54, height=171)
>Person(name=黎明, age=53, height=178)

总结：当看到调用的方法它的参数是接口就可以考虑使用Lambda表达式来替代匿名内部类，不是所有的匿名内部类都能使用Lambda来替换，Lambda表达式相当于对接口的抽象方法的重写。

## 三、Lambda表达式的省略格式

在Lambda标准格式的基础上，使用省略写法的规则为：

1. 小括号内参数的类型可以省略

2. 如果小括号内有且仅有一个参数，则小括号可以省略

3. 如果大括号内有且仅有一个语句，可以同时省略大括号、return关键字及语句分号

```java
(int a)->{
    return new Person();
}
```

省略后

```java
a-> new Person()
```



## 四、Lambda表达式的前提条件

Lambda的语法非常简洁，但是Lambda表达式不是随便使用的，使用时有几个条件要特别注意：

1. 方法的参数或局部变量类型必须为接口

2. 接口中有且仅有一个抽象方法

只有一个抽象方法的接口称为函数式接口，我们就可以使用Lambda表达式

```java
@FunctionalInterface
public interface Flyable {

    //接口中有且仅有一个抽象方法
    public abstract void eat();
    
    //public abstract void eat2();

}
```

@FunctionalInterface注解：作用是检测这个接口是不是只有一个抽象方法，有多个时会报错

## 五、Lambda表达式与匿名内部类区别

1. 所需的类型不一样

匿名内部类需要的类型可以是类，抽象类，接口

Lambda表达式需要的类型必须是接口

2. 抽象方法的数量不一样

匿名内部类所需的接口中抽象方法的数量随意

Lambda表达式所需的接口只能有一个

3. 实现原理不同

匿名内部类是在编译后形成class

Lambda表达式是在程序运行的时候动态生成class


# JDK8新特性(二)之接口新增的两个方法

JDK8以前的接口中只能有静态常量和抽象方法：

```java
interface 接口名{
    静态常量;
    抽象方法;
}
```

JDK8对接口的增强以后，接口中还可以有默认方法和静态方法，即：

```java
interface 接口名{
    静态常量;
    抽象方法;
    
    默认方法;
    静态方法;
}
```

## 一、接口引入默认方法

在JDK8以前接口中只能有抽象方法，存在一个问题：如果给接口新增抽象方法，那么所有实现类都必须重写这个抽象方法，不利于接口的扩展。

JDK8接口中的默认方法实现类不必重写，可以直接使用，实现类也可以根据需要重写，这样就方便接口的扩展。

接口默认方法的定义格式

```java
interface 接口名{
    修饰符 default 返回值类型 方法名(){
        代码;
    }
}
```

接口默认方法的使用

方式一：实现类直接调用接口默认方法

```java
public interface A {
    public default void test01(){
        System.out.println("我是接口A中的默认方法");
    }
}
```

```java
public class B implements A{
    
}
```

```java
public class Demo {

    public static void main(String[] args) {
        B b=new B();
        b.test01();
    }

}
```

执行上述代码，其输出结果为：

> 我是接口A中的默认方法

方式二：实现类重写接口默认方法

```java
public interface A {
    public default void test01(){
        System.out.println("我是接口A中的默认方法");
    }
}
```

```java
public class C implements A{

    @Override
    public void test01() {
        System.out.println("我是C类中重写的默认方法");
    }

}
```

```java
public class Demo {

    public static void main(String[] args) {
        C c=new C();
        c.test01();
    }

}
```

执行上述代码，其输出结果为：

> 我是C类中重写的默认方法

## 二、接口引入静态方法

接口静态方法的定义格式

```java
interface 接口名{
    修饰符 static 返回值类型 方法名(){
        代码;
    }
}
```

接口静态方法的使用

直接使用接口名调用即可：接口名.静态方法名()

```java
public interface A {
    public static void test01() {
        System.out.println("我是接口A中的静态方法");
    }
}
```

```java
public class Demo {

    public static void main(String[] args) {
        // 直接使用接口名调用即可：接口名.静态方法民():
        A.test01();
    }

}
```

执行上述代码，其输出结果为：

> 我是接口A中的静态方法

接口默认方法和静态方法的区别

1. 默认方法通过实例调用，静态方法通过接口名调用

2. 默认方法可以被继承，实现类可以直接使用默认方法，也可以重写接口默认方法

3. 静态方法不能被继承，实现类不能重写接口静态方法，只能使用接口名调用
   

# JDK8新特性(三)之常用内置函数式接口

我们知道Lambda表达式的前提是需要有函数式接口，而Lambda使用时不关心接口名，抽象方法名，只关心抽象方法的参数列表和返回值类型，因此为了让我们使用Lambda方便，JDK8提供了大量常用的函数式接口。常用内置函数式接口主要在java.util.function包中，下面是最常用的几个接口。

## 一、Supplier接口

java.util.function.Supplier<T>接口，它意味着“供给”，对应的Lambda表达式需要“对外提供”一个符合泛型类型的对象数据。

```java
@FunctionalInterface
public interface Supplier<T> {
    T get();
}
```

供给型接口，通过Supplier接口中的get方法可以得到一个值，无参有返回的接口

使用Supplier接口作为方法参数类型，通过Lambda表达式求生int数组中的最大值。提示：接口的泛型使用java.lang.Integer类，基本使用如下：

```java
import java.util.Arrays;
import java.util.function.Supplier;

public class Demo {

    // 使用Lambda表达式返回数组元素的最大值
    public static void main(String[] args) {
        printMax(()->{
            int[] arr={11,99,88,77,22};
            Arrays.sort(arr);
            return arr[arr.length-1];
        });
    }
     
    private static void printMax(Supplier<Integer> supplier){
        int max= supplier.get();
        System.out.println("max = "+max);
    }

}
```



执行上述代码，其输出结果为：

> max = 99

## 二、Consumer接口

java.util.function.Consumer<T>接口则正好相反，它不是生产一个数据，而是消费一个数据，其数据类型由泛型参数决定。

```java
@FunctionalInterface
public interface Consumer<T> {

    public abstract void accept(T t);

}
```

使用Lambda表达式将一个字符串转成大写的字符串

```java
import java.util.function.Consumer;

public class Demo {

    public static void main(String[] args) {
        printHello((String str)->{
            System.out.println(str.toUpperCase());
        });
    }
     
    private static void printHello(Consumer<String> consumer){
        consumer.accept("Hello World!");
    }

}
```

执行上述代码，其输出结果为：



> HELLO WORLD!

使用Lambda表达式先将一个字符串转成小写的字符串，然后再转成大写

```java
import java.util.function.Consumer;

public class Demo {

    public static void main(String[] args) {
        printHello((String str)->{
            System.out.println(str.toLowerCase());
        },(String str)->{
            System.out.println(str.toUpperCase());
        });
    }
     
    private static void printHello(Consumer<String> c1,Consumer<String> c2){
        String str="Hello World!";
        c1.accept(str);
        c2.accept(str);
    }

}
```



执行上述代码，其输出结果为：

> hello world!
> HELLO WORLD!

上述代码可以使用Consumer接口的andThen方法进行简写，其简写后的示例如下：

```java
import java.util.function.Consumer;

public class Demo {

    public static void main(String[] args) {
        printHello((String str)->{
            System.out.println(str.toLowerCase());
        },(String str)->{
            System.out.println(str.toUpperCase());
        });
    }
     
    private static void printHello(Consumer<String> c1,Consumer<String> c2){
        String str="Hello World!";
        c1.andThen(c2).accept(str);
    }

}
```



三、Function接口

java.util.function.Function<T,R>接口用来根据一个类型的数据得到另一个类型的数据，前者称为前置条件，后者称为后置条件，有参数有返回值。

```java
@FunctionalInterface
public interface Function<T, R> {

    public abstract R apply(T t);

}
```

```java
import java.util.function.Function;

public class Demo {

    public static void main(String[] args) {
        getNumber((String str)->{
            int i= Integer.parseInt(str);
            return i;
        });
    }
     
    private static void getNumber(Function<String,Integer> function){
        Integer num = function.apply("10");
        System.out.println("num = "+num);
    }

}
```



执行上述代码，其输出结果为：

num = 10
Function接口的andThen的使用示例： 

```java
import java.util.function.Function;

public class Demo {

    public static void main(String[] args) {
        getNumber((String str)->{
            return Integer.parseInt(str);
        },(Integer i)->{
            return i*5;
        });
    }
     
    private static void getNumber(Function<String,Integer> f1,Function<Integer,Integer> f2{
        //Integer num = f1.apply("6");
        //Integer num2 = f2.apply(num);
        Integer num2 = f1.andThen(f2).apply("6");
        System.out.println("num2 = "+num2);
    }

}
```



四、Predicate接口

有时我们需要对某种类型的数据进行判断，从而得到一个boolean值结果，这时可以使用 java.util.function.Predicate<T>接口

```java
@FunctionalInterface
public interface Predicate<T> {

    boolean test(T t);

}
```

使用Lambda表达式判断一个人姓名是否超过3个字

```java
import java.util.function.Predicate;

public class Demo {

    public static void main(String[] args) {
        isLongName((String name)->{
            return name.length()>3;
        });
    }
     
    private static void isLongName(Predicate<String> predicate){
        boolean isLong =predicate.test("徐凤年");
        System.out.println("名字超出3个字:"+isLong);
    }

}
```



执行上述代码，其输出结果为：

名字超出3个字:false
使用Lambda表达式判断一个字符串中即包含W，又包含H

使用Lambda表达式判断一个字符串中包含W或者包含H

使用Lambda表达式判断一个字符串中不包含W

```java
import java.util.function.Predicate;

public class Demo {

    public static void main(String[] args) {
        test((String str)->{
            return str.contains("H");
        },(String str)->{
            return str.contains("W");
        });
    }
     
    private static void test(Predicate<String> p1,Predicate<String> p2){
        String str="Hello World";
        //boolean b1 =p1.test(str);
        //boolean b2 =p2.test(str);
        //if(b1&b2){
        //    System.out.println("既包含W,也包含H");
        //}
     
        // 使用Lambda表达式判断一个字符串中即包含W，又包含H
        boolean b1 = p1.and(p2).test(str);
        if(b1){
            System.out.println("既包含W,也包含H");
        }
     
        // 使用Lambda表达式判断一个字符串中包含W或者包含H
        boolean b2 = p1.or(p2).test(str);
        if(b2){
            System.out.println("包含W或者包含H");
        }
     
        //使用Lambda表达式判断一个字符串中不包含W
        boolean b3 = p1.negate().test("Hello");
        if(b3){
            System.out.println("不包含W");
        }
    }

}
```

执行上述代码，其输出结果为：

既包含W,也包含H
包含W或者包含H
不包含W

# JDK8新特性(四)之方法引用

## 一、方法引用的介绍

```java
import java.util.function.Consumer;

public class Demo {

    public static void getMax(int[] arr) {
        int sum=0;
        for(int n:arr){
            sum+=n;
        }
        System.out.println(sum);
    }
     
    public static void main(String[] args){
        // 使用Lambda表达式求一个数组的和
        printMax((int[] arr)->{
            getMax(arr);
        });
    }
     
    private static void printMax(Consumer<int[]> consumer){
        int[] arr={11,22,33,44,55};
        consumer.accept(arr);
    }

}
```

上述求和示例中Lambda表达式什么也没有做，仅仅是调用了另外一个方法，这时Lambda表达式也显的有点冗余，因此可以使用方法引用将上述代码进行简化：

```java
import java.util.function.Consumer;

public class Demo {

    public static void getMax(int[] arr) {
        int sum=0;
        for(int n:arr){
            sum+=n;
        }
        System.out.println(sum);
    }
     
    public static void main(String[] args){
        // 使用方法引用求一个数组的和
        printMax(Demo::getMax);
    }
     
    private static void printMax(Consumer<int[]> consumer){
        int[] arr={11,22,33,44,55};
        consumer.accept(arr);
    }

}
```



使用方法引用就是让指定的方法去重写接口的抽象方法，到时调用接口的抽象方法就是调用传递过去的这个方法。

方法引用的格式

符号表示：::

符号说明：双冒号为方法引用运算符，而它所在的表达式被称为方法引用

应用场景：如果Lambda所要实现的抽象方法，已经有其它方法实现，那么则可以使用方法引用

常见引用方式

方法引用在JDK8中使用方式相当灵活，有以下几种形式：

1、instanceName::methodName 对象名::方法名

2、ClassName::staticMethodName 类名::静态方法名

3、ClassName::methodName 类名::普通方法

4、ClassName::new 类名::new 调用的构造器

5、TypeName[]::new String[]::new 调用数组的构造器

## 二、对象名::引用成员方法

这是最常见的一种用法，如果一个类中已经存在了一个成员方法，则可以通过对象名引用成员方法

```java
import java.util.Date;
import java.util.function.Supplier;

public class Demo {

    public static void main(String[] args){
        Date now = new Date();
        //Supplier<Long> sul = () -> {
        //    return now.getTime();
        //};
     
        //使用方法引用
        Supplier<Long> su2 = now::getTime;
        Long aLong = su2.get();
        System.out.println("aLong = " + aLong);
    }

}
```



执行上述代码，其输出结果为：

aLong = 1640490235269
方法引用有两个注意事项：

1.被引用的方法，参数要和接口中抽象方法的参数一样

2.当接口抽象方法有返回值时，被引用的方法也必须有返回值

## 三、类名::引用静态方法

由于在java.lang.System类中已经存在了静态方法currentTimeMillis，所以当我们需要通过Lambda来调用该方法时，可以使用方法引用：

```java
import java.util.function.Supplier;

public class Demo {

    public static void main(String[] args){
        Supplier<Long> su = System::currentTimeMillis;
        Long time = su.get();
        System.out.println("time = " + time);
    }

}
```

执行上述代码，其输出结果为：

time = 1640491482339

## 四、类名::引用实例方法

类名只能调用静态方法，类名引用实例方法是有前提的，实际上是拿第一个参数作为方法的调用

```java
import java.util.function.BiFunction;
import java.util.function.Function;

public class Demo {

    public static void main(String[] args){
        //Function<String,Integer> f1 = (String str)->{
        //    return str.length();
        //};
     
        // 类名::实例方法（注意：“类名::实例方法”实际上会将第一个参数作为方法的调用）
        Function<String,Integer> f1 = String::length;
        
        int length=f1.apply("hello");
        System.out.println("length = "+length);
     
        // BiFunction<String,Integer,String> f2=String::substring;
        // 相当于这样的Lambda表达式
        BiFunction<String,Integer,String> f2=(String str,Integer index)->{
            return str.substring(index);
        };
        String str=f2.apply("helloworld",3);
        System.out.println("str = "+str);
    }

}
```



执行上述代码，其输出结果为：

length = 5
str = loworld

## 五、类名::new引用构造器

```java
import lombok.Data;

@Data
public class Person {

    private String name;
    private int age;
     
    public Person() {
        System.out.println("无参构造");
    }
     
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
        System.out.println("有参构造");
    }

}
```



 方法引用使用类名::new（无参构造）形式

```java
import java.util.function.Supplier;

public class Demo {

    public static void main(String[] args){
        //Supplier<Person> su1=()->{
        //    return new Person();
        //};
        Supplier<Person> su1=Person::new;
        Person person = su1.get();
        System.out.println("person = "+person);
    }

}
```

执行上述代码，其输出结果为：

无参构造
person = Person(name=null, age=0)
方法引用使用类名::new（有参构造）形式

```java
import java.util.function.BiFunction;

public class Demo {

    public static void main(String[] args){
        //BiFunction<String,Integer,Person> bif=(String name,Integer age)->{
        //    return new Person(name,age);
        //};
        BiFunction<String,Integer,Person> bif=Person::new;
        Person person = bif.apply("小仙女",18);
        System.out.println("person = "+person);
    }

}
```

执行上述代码，其输出结果为：

有参构造
person = Person(name=小仙女, age=18)

## 六、类名::new引用数组构造器

数组也是Object的子类对象，所以同样具有构造器

```java
import java.util.Arrays;
import java.util.function.Function;

public class Demo {

    public static void main(String[] args){
        Function<Integer,int[]> f1=(Integer length)->{
            return new int[length];
        };
        int[] arr=f1.apply(10);
        System.out.println(Arrays.toString(arr));
    }

}
```

对应上述代码，如果是使用数组构造器的方法引用：

```java
import java.util.Arrays;
import java.util.function.Function;

public class Demo {

    public static void main(String[] args){
        Function<Integer,int[]> f1=int[]::new;
        int[] arr=f1.apply(10);
        System.out.println(Arrays.toString(arr));
    }

}
```

执行上述代码，其输出结果为：

[0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
总结：方法引用是对Lambda表达式符合特定情况下的一种缩写， 它使得我们的Lambda表达式更加的精简，也可以理解为Lambda表达式的缩写形式，不过要注意的是方法引用只能“引用” 已经存在的方法。

# [JDK8新特性(五)之Stream流介绍和获取方式](https://blog.csdn.net/y_bccl27/article/details/121732962)

# JDK8新特性(十三)之Optional

1.Optional类介绍

Optional是一个没有子类的工具类，Optional是一个可以为null的容器对象。它的作用主要是为了解决避免Null检查，防止NullPointerException。



2.Optional类的创建方式

```java
import java.util.Optional;

public class Demo {

    public static void main(String[] args){
        //of：只能传入一个具体值，不能传入null
        //ofNullable：既可以传入具体值
        //empty：存入的是null
        //Optional<String> op1=Optional.of("王初冬");
        //Optional<String> op1=Optional.of(null);
        //Optional<String> op2=Optional.ofNullable("王初冬");
        //Optional<String> op2=Optional.ofNullable(null);
        Optional<Object> op3=Optional.empty();
    }

}
```



3.Optional类的isPresent方法

```java
import java.util.Optional;

public class Demo {

    public static void main(String[] args){
        Optional<String> op1=Optional.of("王初冬");
        //判断Optional中是否有具体值，有值则返回true，没有值则返回false
        boolean present = op1.isPresent();
        System.out.println("present = "+present);
    }

}
```

执行上述代码，其输出结果为：

present = true
4.Optional类的get方法

```java
import java.util.Optional;

public class Demo {

    public static void main(String[] args){
        Optional<String> op1=Optional.of("王初冬");
        //获取Optional中的值，如果有值则返回具体值，没有值则报错
        System.out.println(op1.get());
    }

}
```

5.Optional中的orElse方法

orElse：如果Optional中有值，就取出这个值；如果没有值，就使用参数指定的值

```java
import java.util.Optional;

public class Demo {

    public static void main(String[] args){
        Optional<String> userName=Optional.of("南宫仆射");
        String name =userName.orElse("王初冬");
        System.out.println("name="+name);
    }

}
```



6.Optional中的ifPresent方法

ifPresent：如果Optional存在值的话，则调用Lambda表达式

```java
import java.util.Optional;

public class Demo {

    public static void main(String[] args){
        Optional<String> userName=Optional.of("王初冬");
        userName.ifPresent(s ->{
            System.out.println("有人："+s);
        });
    }

}
```

7.Optional中的map方法

```java
public class Demo {

    public static void main(String[] args){
        User u = new User("王初冬",18);
        String upperUserName = getUpperUserName(u);
        System.out.println(upperUserName);
    }
     
    // 定义一个方法将User中的用户名转成大小并返回
    public static String getUpperUserName(User u){
        if(u!=null){
            String userName = u.getUserName();
            if(userName!=null){
                return userName.toUpperCase();
            }else {
                return null;
            }
        }else{
            return null;
        }
    }

}
```



上述为传统方式来事项，如果采用Optional，其修改为如下：

```java
import java.util.Optional;

public class Demo {

    public static void main(String[] args){
        User u = new User("王初冬",18);
        Optional<User> op=Optional.of(u);
        System.out.println(getUpperUserName(op));
    }
     
    public static String getUpperUserName(Optional<User> op){
        //String upperUserName=op.map(u->u.getUserName())
        //        .map(s-> s.toUpperCase())
        //        .orElse("null");
        String upperUserName=op.map(User::getUserName).map(String::toUpperCase).orElse("null");
        return upperUserName;
    }

}
```



总结：Optional是一个可以为null的容器对象，orElse、ifPresent、map等方法避免对null的判断，写出更加优雅的代码


# JDK8新特性(十四)之日期时间API



旧版日期时间API存在的问题：

1. 设计很差： 在java.util和java.sql的包中都有日期类，java.util.Date同时包含日期和时间，而java.sql.Date仅包含日期。此外用于格式化和解析的类在java.text包中定义。
2. 非线程安全：java.util.Date 是非线程安全的，所有的日期类都是可变的，这是Java日期类最大的问题之一。
3. 时区处理麻烦：日期类并不提供国际化，没有时区支持，因此Java引入了java.util.Calendar和
java.util.TimeZone类，但他们同样存在上述所有的问题。

JDK8中增加了一套全新的日期时间API，这套API设计合理，是线程安全的。新的日期及时间API位于java.time包中，下面是一些关键类：

LocalDate：表示日期，包含年月日，格式为2019-10-16

LocalTime：表示时间，包含时分秒，格式为16:38:54.158549300

LocalDateTime：表示日期，包含年月日、时分秒，格式为2019-10-16T16:38:54.750

DateTimeFormatter：日期时间格式化类

Instant：时间戳，表示一个特定的时间瞬间

Duration：用于计算2个时间(LocalTime，时分秒)的距离

Period：用于计算2个时间(LocalDate，年月日)的距离

ZoneDateTime：包含时区的时间

一、LocalDate、LocalTime和LocalDateTime的介绍 

LocalDate、LocalTime和LocalDateTime这三个类是JDK1.8出来的新特性，用于时间和日期的支持，这三个类位于java.time包下面。 

LocalDate：LocalDate类的实例是一个不可变对象，它只提供了简单的日期，并不含当天的时间信息。它是日期的描述，常用于生日，不能代表时间线上的即时信息，这个类是不可变的和线程安全的。

LocalTime：LocalTime是一个不可变的时间对象，代表一个时间，通常被看作是小时 - 分 - 秒，时间表示为纳秒精度。它是在挂钟上看到的当地时间的描述， 不能代表时间线上的即时信息，这个类是不可变的和线程安全的。

LocalDateTime：LocalDateTime是一个不可变的日期时间对象，代表日期时间，通常被视为年 - 月 - 日 - 时 - 分 - 秒。这个类是不可变的和线程安全的。

示例代码：

```java
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Tests {
    

    public void main(String[]  args){
        String date = "2019-10-01";
        String time = "06:00:00";
        //字符串转换为LocalDate
        LocalDate localDate = LocalDate.parse(date);
        //字符串转换为LocalTime
        LocalTime localTime = LocalTime.parse(time);
        System.out.println("localDate:"+localDate.toString());
        System.out.println("localTime:"+localTime.toString());
     
        //利用LocalDateTime.of()函数指定LocalDateTime
        LocalDateTime localDateTime1 = LocalDateTime.of(2019, 10, 1, 6, 00, 00, 000);
        System.out.println("localDateTime1:"+localDateTime1.toString());
        LocalDateTime localDateTime2 = LocalDateTime.of(localDate, localTime);
        System.out.println("localDateTime2:"+localDateTime2.toString());
     
        //从LocalDataTime中获取对应的LocalDate和LocalTime
        LocalDate localDate1 = localDateTime1.toLocalDate();
        LocalTime localTime1 = localDateTime1.toLocalTime();
        System.out.println("localDate1:"+localDate1.toString());
        System.out.println("localTime1:"+localTime1.toString());
     
        //LocalDate添加time生成LocalDateTime
        LocalDateTime localDateTime3 = localDate.atTime(6, 0, 0);
        //LocalTime添加Date生成LocalDateTime
        LocalDateTime localDateTime4 = localTime.atDate(localDate);
        System.out.println("localDateTime3:"+localDateTime3.toString());
        System.out.println("localDateTime4:"+localDateTime4.toString());
     
        //获取系统当前时间
        LocalDateTime localDateTime5=LocalDateTime.now();
        System.out.println("localDateTime5:"+localDateTime5.toString());
    }

}
```



执行上述代码，其输出代码：

> localDate:2019-10-01
> localTime:06:00
> localDateTime1:2019-10-01T06:00
> localDateTime2:2019-10-01T06:00
> localDate1:2019-10-01
> localTime1:06:00
> localDateTime3:2019-10-01T06:00
> localDateTime4:2019-10-01T06:00
> localDateTime5:2019-09-11T21:06:35.586



二、时间的格式化与解析

DateTimeFormatter类是JDK1.8出来的新特性，该类的作用是便于日期时间类与String类之间的转换，该类位于java.time包下面。

1.将LocalDateTime转换为指定格式的字符串



`DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())`
2.将字符串转换为指定格式的LocalDateTime

```java
DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
String str="2019-10-1 08:00:00";
LocalDateTime  ldt= LocalDateTime.parse(str,df);
System.out.println("String类型的时间转成LocalDateTime："+ldt);
```





测试时在JDK1.8环境下面会报错（DateTimeParseException could not be parsed at index 8），JDK1.9里面得到了修复，不过本人还没有测试过，仅需了解既可

三、Instant类

```java
import java.time.Instant;

public class Demo {

    public static void main(String[] args){
        // Instant内部保存了秒和纳秒，一般不是给用户使用的，而是方便我们程序做一些统计的
        Instant now=Instant.now();
        System.out.println("now ="+now);
     
        //增加20纳秒
        Instant plus=now.plusNanos(20);
        System.out.println("plus ="+plus);
     
        //减少20秒
        Instant minus=now.minusSeconds(20);
        System.out.println("minus ="+minus);
     
        // 得到秒
        System.out.println(now.getEpochSecond());
        // 得到纳秒
        System.out.println(now.getNano());
    }

}
```



执行上述代码，其输出结果为：

```java
now =2021-12-26T13:09:33.879Z
plus =2021-12-26T13:09:33.879000020Z
minus =2021-12-26T13:09:13.879Z
1640524173
879000000
```

四、计算日期时间差类Duration和Period

```java
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;

public class Demo {

    public static void main(String[] args){
        // Duration计算时间的距离
        LocalTime now = LocalTime.now();
        LocalTime time = LocalTime.of(14,15,20);
        Duration duration = Duration.between(time,now);
        System.out.println("相差的天数："+duration.toDays());
        System.out.println("相差的小时数："+duration.toHours());
        System.out.println("相差的分钟数："+duration.toMinutes());
     
        // Period计算日期的距离
        LocalDate nowDate = LocalDate.now();
        LocalDate date =LocalDate.of(1998,8,8);
        Period period=Period.between(date,nowDate);
        System.out.println("相差的年："+period.getYears());
        System.out.println("相差的月："+period.getMonths());
        System.out.println("相差的天："+period.getDays());
    }

}
```



执行上述代码，其输出结果为：

```java
相差的天数：0
相差的小时数：7
相差的分钟数：447
相差的年：23
相差的月：4
相差的天：18
```

五、时间校正器 TemporalAdjuster

  自定义的时间调整器

```java
import java.time.*;
import java.time.temporal.TemporalAdjuster;

public class Demo {

    public static void main(String[] args){
        LocalDateTime now=LocalDateTime.now();
     
        //将日期调整到“下一个月的第一天”
        TemporalAdjuster firstDayOfNextMonth = temporal -> {
            // temporal要调整的时间
            LocalDateTime dateTime =(LocalDateTime)temporal;
            // 下一个月的第一天
            return dateTime.plusMonths(1).withDayOfMonth(1);
        };
     
        LocalDateTime newDateTime=now.with(firstDayOfNextMonth);
        System.out.println("nowDateTime = "+newDateTime);
    }

}
```



执行上述代码，其输出结果为：

```java
nowDateTime = 2022-01-01T22:01:48.755
```


JDK中自带了很多时间调整器 TemporalAdjusters

```java
import java.time.*;
import java.time.temporal.TemporalAdjusters;

public class Demo {

    public static void main(String[] args){
        LocalDateTime now=LocalDateTime.now();
        // JDK中自带了很多时间调整器
        LocalDateTime newDateTime=now.with(TemporalAdjusters.firstDayOfYear());
        System.out.println("nowDateTime = "+newDateTime);
    }

}
```

执行上述代码，其输出结果为：

```java
nowDateTime = 2021-01-01T22:09:15.309
```

六、设置日期时间的时区

JDK8中加入了对时区的支持，LocalDate、LocalTime、LocalDateTime是不带时区的，带时区的日期时间类分别为：ZonedDate、ZonedTime、ZonedDateTime。其中每个时区都对应着ID，ID的格式为“区域/城市”，例如：Asia/Shanghai。

```java
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Demo {

    public static void main(String[] args){
        //1.获取所有的时区ID
        //ZoneId.getAvailableZoneIds().forEach(System.out::println);
     
        //不带时区，获取计算机的当前时间
        LocalDateTime now=LocalDateTime.now(); //中国使用的是东八区的时区，比标准时间早8个小时
        System.out.println("now = "+now);
     
        // 2.操作带时区的类
        // 创建世界标准时间
        ZonedDateTime bz=ZonedDateTime.now(Clock.systemUTC());
        System.out.println("bz = "+bz);
     
        // now()：使用计算机的默认的时区，创建日期时间
        ZonedDateTime now1 = ZonedDateTime.now();
        System.out.println("now1 = "+now1);
     
        // 使用指定的时区创建日期时间
        ZonedDateTime now2 =ZonedDateTime.now(ZoneId.of("America/Vancouver"));
        System.out.println("now2 = "+now2);
    }

}
```

执行上述代码，其输出结果为：

```java
now = 2021-12-27T22:24:49.405
bz = 2021-12-27T14:24:49.406Z
now1 = 2021-12-27T22:24:49.407+08:00[Asia/Shanghai]
now2 = 2021-12-27T06:24:49.408-08:00[America/Vancouver]

```









# JDK8新特性(十五)之重复注解、类型注解

## 一、重复注解

自从Java 5中引入注解以来，注解开始变得非常流行，并在各个框架和项目中被广泛使用。不过注解有一个很大的限制是：在同一个地方不能多次使用同一个注解。JDK8引入了重复注解的概念，允许在同一个地方多次使用同一个注解。在JDK8中使用@Repeatable注解定义重复注解。

1.定义重复的注解容器注解

```java
//定义重复的注解容器注解
@Retention(RetentionPolicy.RUNTIME)
@interface MyTests { // 这是重复注解的容器
    MyTest[] value();
}
```

2.定义一个可以重复的注解

```java
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// 定义一个可以重复的注解
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MyTests.class)  // 注入可重复注解容器
@interface MyTest {
    String value();
}
```

3.可重复注解的使用

```java
/**
 * 配置可重复注解的使用
 */
@MyTest("ta")
@MyTest("tb")
@MyTest("tc")
public class Demo {
 
    @MyTest("ma")
    @MyTest("mb")
    public void test(){
 
    }
 
    public static void main(String[] args) throws NoSuchMethodException{
        // 解析重复注解
        // 获取类上的重复注解
        // getAnnotationsByType是新增的API用于获取重复的注解
        MyTest[] annotationsByType = Demo.class.getAnnotationsByType(MyTest.class);
        for(MyTest myTest:annotationsByType){
            System.out.println(myTest.value());
        }
 
        // 获取方法上的重复注解
        MyTest[] tests=Demo.class.getMethod("test",null).getAnnotationsByType(MyTest.class);
        for(MyTest test:tests){
            System.out.println(test.value());
        }
    }
}
```

执行上述代码，其输出结果为：

> ta
> tb
> tc
> ma
> mb

## 二、类型注解

JDK8为@Traget元注解新增了两种类型：TYPE_PARAMETER，TYPE_USE

TYPE_PARAMETER：表示该注解能写在类型参数的声明语句中，类型参数声明如：<T>、<T extends Person>

TYPE_USE：表示该注解可以在任何用到类型的地方使用

TYPE_PARAMETER的使用示例：

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public class Demo <@TypeParam T>{

    public <@TypeParam E extends Integer> void test(){
     
    }
     
    public static void main(String[] args){
      
    }

}

@Target(ElementType.TYPE_PARAMETER)
@interface TypeParam{

}
```

TYPE_USE的使用示例：

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public class Demo {

    private @NotNull int a=10;
     
    public void test(@NotNull String str,@NotNull int a) {
        @NotNull double d=10.1;
    }

}

@Target(ElementType.TYPE_USE)
@interface NotNull{

}
```

总结：通过@Repeatable元注解可以定义可重复注解，TYPE_PARAMETER可以让注解放在泛型上，TYPE_USE可以让注解放在类型的前面
