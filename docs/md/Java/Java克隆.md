# 对象的克隆

## 1.1  变量的  Copy

```java
@Data
class Person implements Cloneable{
    private String name;
    private int age;
   
    public String info(){
        return  this.getName()+"\t"+this.age;
    }
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```



```java
Person person = new Person("tom",20);
Person copyPerson = person;
copyPerson.setAge(22);
System.out.println(person.info());
System.out.println(copyPerson.info());
```

输出结果：

tom	22
tom	22

修改复制对象的年龄时，原始对象的年龄也发生变化。

原始变量和副本都是指向同一个对象的引用，任何一个变量的改变都会影响另一个变量的。



## 1.2  对象的浅克隆

Object 的clone方法是protected 的，要想实现克隆，类必须实现Cloneable 接口

```java
@HotSpotIntrinsicCandidate
protected native Object clone() throws CloneNotSupportedException;
```

示例代码：

```java
@Setter
@AllArgsConstructor
class Person implements Cloneable{
    private String name;
    private int age;

    public String info(){
        return  this.name+"\t"+this.age ;
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
```
测试代码
```java
Person person = new Person("tom",20);
Person clone = (Person) person.clone();

System.out.println(person);
System.out.println(person.info());
System.out.println(clone);
System.out.println(clone.info());

//修改复制对象的名字
clone.setName("tiger"); // String 是final 的
clone.setAge(29);

System.out.println(person);
System.out.println(person.info());

System.out.println(clone);
System.out.println(clone.info());
```

输出结果：
>com.zdy.Person@17d0685f
>tom	20
>com.zdy.Person@3891771e
>tom	20
>com.zdy.Person@17d0685f
>tom	20
>com.zdy.Person@3891771e
>tiger	29
>
## 1.3 对象的深克隆

**当对象中有字段为引用对象时，使用浅克隆就会出现问题**

如下代码
```java
@Setter
@Getter
class Person implements Cloneable{
    private String name;
    private int age;
    private Address address;

    public String info(){
        return  this.getName()+"\t"+this.age + "\t" + this.address;
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
@Data
@AllArgsConstructor
class Address{
    private String provices;
    private String City;
}
```
测试代码
```java
Person person = new Person("tom",20);
Person clone = (Person) person.clone();

System.out.println(person);
System.out.println(person.info());
System.out.println(clone);
System.out.println(clone.info());

//修改复制对象的名字
clone.setName("tiger"); // String 是final 
clone.setAge(29);      // 修改年龄

System.out.println(person);
System.out.println(person.info());

System.out.println(clone);
System.out.println(clone.info());
```

输出结果：
>com.zdy.clone.Person@3891771e
>tom	20	Address(provices=河南省, City=南阳市)
>com.zdy.clone.Person@78ac1102
>tom	20	Address(provices=河南省, City=南阳市)
>com.zdy.clone.Person@3891771e
>tom	20	Address(provices=北京市, City=海淀区)
>com.zdy.clone.Person@78ac1102
>tiger	23	Address(provices=北京市, City=海淀区)




**修改名字和年龄都没问题。当修改了克隆对象的地址信息时，原始对象地址改变。说明克隆对象地址和原始对象是共享的。违背了克隆的初衷。**

为Adress类添加 Cloneable 接口

重写clone 方法

```java
@Override
public Object clone() throws CloneNotSupportedException {
    return super.clone();
}
```

```java
@Override
protected Person clone() throws CloneNotSupportedException {
    Person person = (Person)super.clone();
    person.setAddress((Adreess)getAddress().clone());
    return person;
}
```

再次执行测试

>com.zdy.clone.Person@3891771e
>tom	20	Address(provices=河南省, City=南阳市)
>com.zdy.clone.Person@78ac1102
>tom	20	Address(provices=河南省, City=南阳市)
>com.zdy.clone.Person@3891771e
>tom	20	Address(provices=河南省, City=南阳市)
>com.zdy.clone.Person@78ac1102
>tiger	23	Address(provices=北京市, City=海淀区)

从而实现了深克隆