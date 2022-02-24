# Effective 实验楼课件

------

[TOC]



------



# 第一章 创建和销毁对象

# 实验1 用静态工厂替换构造器

一个类除了可以提供一个公有的构造器让客户端获取它自身的实例之外，也可以提供一个公有的静态工厂方法，本次实验中将详细介绍静态工厂方法的优缺点。

##### 知识点

- 简介
- 静态工厂方法的优势
- 静态工厂方法的劣势



- 简介

对于类而言，为了让客户端获取它自身的一个实例，最传统的方法就是提供一个公有的构造器。还有一种方法，也应该在每个程序员的工具箱中占 有一席之地。类可以提供一个公有的静态工厂方法（static factory method），它只是一个返回类的实例的静态方法。下面是一个来自 Boolean（基本类型 boolean 的装箱类）的简单示例。这个方法将 boolean 基本类型值转换成了一个 Boolean 对象引用：

```java
public static Boolean valueOf(boolean b){
    return b ? Boolean.TRUE ：Boolean.FALSE;
}
```

注意，静态工厂方法与设计模式中的工厂方法（Factory Method）模式不同。本实验中所指的静态工厂方法并不直接对应于设计模式（Design Pattern）中的工厂方法。

如果不通过公有的构造器，或者说除了公有的构造器之外，类还可以给它的客户端提供静态工厂方法。提供静态工厂方法而不是公有的构造器，这样做既有优势，也有劣势。

静态工厂方法的优势



静态工厂方法与构造器不同的第一大优势在于，它们有名称

如果构造器的参数本身没有确切地描述正被返回的对象，那么具有适当名称的静态工厂会更容易使用，产生的客户端代码也更易于阅读。例如，构造器 BigInteger(int, int, Random) 返回的 BigInteger 可能为素数，如果用名为 BigInteger.probablePrime 的静态工厂方法来表示，显然更为清楚。（Java 4 版本中增加了这个方法。）

一个类只能有一个带有指定签名的构造器。编程人员通常知道如何避开这一限制：通过提供两个构造器，它们的参数列表只在参数类型的顺序上有所不同。实际上这并不是个好主意。面对这样的 API，用户永远也记不住该用哪个构造器，结果常常会调用错误的构造器。并且在读到使用了这些构造器的代码时，如果没有参考类的文档，往往不知所云。

由于静态工厂方法有名称，所以它们不受上述限制。当一个类需要多个带有相同签名的构造器时，就用静态工厂方法代替构造器，并且仔细地选择名称以便突出静态工厂方法之间的区别。

静态工厂方法与构造器不同的第二大优势在于，不必在每次调用它们的时候都创建一个新对象

这使得不可变类（详见实验 17）可以使用预先构建好的实例，或者将构建好的实例缓存起来，进行重复利用，从而避免创建不必要的重复对象。Boolean.valueOf(boolean)方法说明了这项技术：它从来不创建对象。这种方法类似于享元（Flyweight）模式。如果程序经常请求创建相同的对象，并且创建对象的代价很高，则这项技术可以极大地提升性能。

静态工厂方法能够为重复的调用返回相同对象，这样有助于类总能严格控制在某个时刻哪些实例应该存在。这种类被称作实例受控的类（instance-controlled）。编写实例受控的类有几个原因。实例受控使得类可以确保它是一个 Singleton（详见实验 3）或者是不可实例化 的（详见实验 4）。它还使得不可变的值类（详见实验 17）可以确保不会存在两个相等的实例，即当且仅当 a==b 时，a.equals(b) 才为 true。这是享元模式的基础。枚举（enum）类型（详见实验 34）保证了这一点。

静态工厂方法与构造器不同的第三大优势在于，它们可以返回原返回类型的任何子类型的对象

这样我们在选择返回对象的类时就有了更大的灵活性。这种灵活性的一种应用是，API 可以返回对象，同时又不会使对象的类变成公有的。以这种方式隐藏实现类会使 API 变得非常简洁。这项技术适用于基于接口的框架（interfacebased framework）（详见实验 20），因为在这种框架中，接口为静态工厂方法提供了自然返回类型。

在 Java 8 之前，接口不能有静态方法，因此按照惯例，接口 Type 的静态工厂方法被放在一个名为 Types 的不可实例化的伴生类（详见实验 4）中。例如，Java Collections Framework 的集合接口有 45 个工具实现，分别提供了不可修改的集合、同步集合，等等。几乎所有这些实现都通过静态工厂方法在一个不可实例化的类（java.util.Collections）中导出。所有返回对象的类都是非公有的。

现在的 Collections Framework API 比导出 45 个独立公有类的那种实现方式要小得多，每种便利实现都对应一个类。这不仅仅是指 API 数量上的减少，也是概念意义上的减少：为了使用这个 API，用户必须掌握的概念在数量和难度上都减少了。程序员知道，被返回的对象是由相关的接口精确指定的，所以他们不需要阅读有关的类文档。此外，使用这种静态工厂方法时，甚至要求客户端通过接口来引用被返回的对象，而不是通过它的实现类来引用被返回的对象，这是一种良好的习惯（详见实验 64）。

从 Java 8 版本开始，接口中不能包含静态方法的这一限制成为历史，因此一般没有任何理由给接口提供一个不可实例化的伴生类。已经被放在这种类中的许多公有的静态成员，应该被放到接口中去。但是要注意，仍然有必要将这些静态方法背后的大部分实现代码，单独放进一个包级私有的类中。这是因为在 Java 8 中仍要求接口的所有静态成员都必须是公有的。在 Java 9 中允许接口有私有的静态方法，但是静态域和静态成员类仍然需要是公有的。

静态工厂的第四大优势在于，所返回的对象的类可以随着每次调用而发生变化，这取决于静态工厂方法的参数值

只要是已声明的返回类型的子类型，都是允许的。返回对象的类也可能随着发行版本的不同而不同。EnumSet（详见实验 36）没有公有的构造器，只有静态工厂方法。在 OpenJDK 实现中，它们返回两种子类之一的一个实例，具体则取决于底层枚举类型的大小：如果它的元素有 64 个或者更少，就像大多数枚举类型一样，静态工厂方法就会返回一个 RegularEnumSet 实例，用单个 long 进行支持；如果枚举类型有 65 个或者更多元素，工厂就返回 JumboEnumSet 实例，用一个 long 数组进行支持。

这两个实现类的存在对于客户端来说是不可见的。如果 RegularEnumSet 不能再给小的枚举类型提供性能优势，就可能从未来的发行版本中将它删除，不会造成任何负面的影响。同样地，如果事实证明对性能有好处，也可能在未来的发行版本中添加第三甚至第四个 EnumSet 实现。客户端永远不知道也不关心它们从工厂方法中得到的对象的类，它们只关心它是 EnumSet 的某个子类。

静态工厂的第五大优势在于，方法返回的对象所属的类，在编写包含该静态工厂方法的类时可以不存在

这种灵活的静态工厂方法构成了服务提供者框架（Service Provider Framework）的基础，例如 JDBC（Java 数据库连接）API。服务提供者框架是指这样一个系统：多个服务提供者实现一个服务，系统为服务提供者的客户端提供多个实现，并把它们从多个实现中解耦出来。

服务提供者框架中有三个重要的组件：服务接口（Service Interface），这是提供者实现的；提供者注册 API（Provider Registration API），这是提供者用来注册实现的；服务访问 API（Service Access API），这是客户端用来获取服务的实例。服务访问 API 是客户端用来指定某种选择实现的条件。如果没有这样的规定，API 就会返回默认实现的一个实例，或者允许客户端遍历所有可用的实现。服务访问 API 是“灵活的静态工厂”，它构成了服务提供者框架的基础。

服务提供者框架的第四个组件服务提供者接口（Service Provider Interface）是可选的，它表示产生服务接口之实例的工厂对象。如果没有服务提供者接口，实现就通过反射方式进行实例化（详见实验 65）。对于 JDBC 来说，Connection 就是其服务接口的一部分，DriverManager.registerDriver 是提供者注册 API，DriverManager.getConnection 是服务访问 API，Driver 是服务提供者接口。

服务提供者框架模式有着无数种变体。例如，服务访问 API 可以返回比提供者需要的更丰富的服务接口。这就是桥接（Bridge）模式。依赖注入框架（详见实验 5）可以被看作是一个强大的服务提供者。从 Java 6 版本开始，Java 平台就提供了一个通用的服务提供者框架 java.util.ServiceLoader，因此你不需要（一般来说也不应该）再自己编写了（详见实验 59）。JDBC 不用 ServiceLoader，因为前者出现得比后者早。

静态工厂方法的劣势



静态工厂方法的主要缺点在于，类如果不含公有的或者受保护的构造器，就不能被子类化

例如，要想将 Collections Framework 中的任何便利的实现类子类化，这是不可能的。但是这样也许会因祸得福，因为它鼓励程序员使用复合（composition），而不是继承（详见实验 18），这正是不可变类型所需要的（详见实验 17）。

静态工厂方法的第二个缺点在于，程序员很难发现它们

在 API 文档中，它们没有像构造器那样在 API 文档中明确标识出来，因此，对于提供了静态工厂方法而不是构造器的类来说，要想查明如何实例化一个类是非常困难的。Javadoc 工具总有一天会注意到静态工厂方 法。同时，通过在类或者接口注释中关注静态工厂，并遵守标准的命名习惯，也可以弥补这一劣势。下面是静态工厂方法的一些惯用名称。这里只列出了其中的一小部分：

- from：类型转换方法，它只有单个参数，返回该类型的一个相对应的实例，例如：

  ```java
  Date d = Date.from(instant);
  ```

- of:聚合方法，带有多个参数，返回该类型的一个实例，把它们合并起来，例如：

  ```java
  Set<Rank> faceCards=EnumSet.of(JACK,QUEEN,KING);
  ```

- valueOf:比 from 和 of 更烦琐的一种替代方法，例如：

  ```java
  BigInteger prime = BigInteger.valueOf(Integer.MAX_VALUE);
  ```

- instance 或者 getInstance:返回的实例是通过方法的（如有）参数来描述的，但是不能说与参数具有同样的值，例如：

  ```java
  StackWalker luke = StackWalker.getInstance(options);
  ```

- create 或者 newInstance:像 instance 或者 getInstance 一样，但 create 或者 newInstance 能够确保每次调用都返回一个新的实例，例如：

  ```java
  Object newArray = Array.newInstance(classObject,arrayLen);
  ```

- getType:像 getInstance 一样，但是在工厂方法处于不同的类中的时候使用。Type 表示工厂方法所返回的对象类型，例如：

  ```java
  FileStore fs = Files.getFileStore(path);
  ```

- newType:像 newInstance 一样，但是在工厂方法处于不同的类中的时候使用。Type 表示工厂方法所返回的对象类型，例如：

  ```java
  BufferedReader br = Files.newBufferedReader(path);
  ```

- type:getType 和 newType 的简版，例如：

  ```java
  List<Complaint> litany = Collections.list(legacyLitany);
  ```



实验总结



简而言之，静态工厂方法和公有构造器都各有用处，我们需要理解它们各自的长处。静态工厂经常更加合适，因此切忌第一反应就是提供公有的构造器，而不先考虑静态工厂。

# 实验2 遇到多个构造参数是考虑使用构造器

实验介绍

本此实验将详细介绍当类的构造器或者静态工厂中需要多个可选参数时，我们可以分别使用重叠构造器模式、JavaBeans 模式、建造者（Builder）模式等三种模式来编写这个类。

#### 知识点

- 重叠构造器模式
- JavaBeans 模式
- 建造者（Builder）模式

重叠构造器模式

静态工厂和构造器有个共同的局限性：它们都不能很好地扩展到大量的可选参数。比如用一个类表示包装食品外面显示的营养成分标签。这些标签中有几个域是必需的：每份的含量、每罐的含量以及每份的卡路里。还有超过 20 个的可选域：总脂肪量、饱和脂肪量、转化脂肪、胆固醇、钠，等等。大多数产品在某几个可选域中都会有非零的值。

对于这样的类，应该用哪种构造器或者静态工厂来编写呢？程序员一向习惯采用重叠构造器（telescoping constructor）模式，在这种模式下，提供的第一个构造器只有必要的参数，第二个构造器有一个可选参数，第三个构造器有两个可选参数，依此类推，最后一个构造器包含所有可选的参数。下面有个示例，为了简单起见，它只显示四个可选域：

```java
//Telescoping constructor pattern - does not scale well!
public class NutritionFacts{
    private final int servingSize; //(mL)         required
    private final int servings; //(per container) required
    private final int calories; //(per serving)    optional
    private final int fat;   //(g/serving)        optional
    private final int sodium; //(mg/serving)     optional
    private final int carbohydrate; //(g/serving)  optional
    public NutritionFacts(int servingSize,int servings){
        this(servingSize,servings,0);
    }
    public NutritionFacts(int servingSize,int servings, int calories){
        this(servingSize,servings,calories，0);
    }
    public NutritionFacts(int servingSize,int servings,int calories,int fat){
        this(servingSize,servings,calories,fat,0);
    }
    public NutritionFacts(int servingSize,int servings,
          int calories,int fat,int sodium){
        this(servingSize,servings,calories,fat，sodium,0);
    }
    public NutritionFacts(int servingSize,int servings,
            int calories,int fat,int sodium,int carbohydrate){
        this.servingSize=servingSize;
        this.servings=servings;
        this.calories=calories;
        this.fat=fat;
        this.sodium=sodium;
        this.carbohydrate=carbohydrate;
    }
}
```

当你想要创建实例的时候，就利用参数列表最短的构造器，但该列表中包含了要设置的所有参数：

```java
    NutritionFacts cocaCola = new NutritionFacts(240,8,100,0,35,27);
```

这个构造器调用通常需要许多你本不想设置的参数，但还是不得不为它们传递值。在这个例子中，我们给 fat 传递了一个值为 0。如果“仅仅”是这 6 个参数，看起来还不算太糟糕，问题是随着参数数目的增加，它很快就失去了控制。

简而言之，重叠构造器模式可行，但是当有许多参数的时候，客户端代码会很难编写，并且仍然较难以阅读。如果读者想知道那些值是什么意思，必须很仔细地数着这些参数来探个究竟。一长串类型相同的参数会导致一些微妙的错误。如果客户端不小心颠倒了其中两个参数的顺序，编译器也不会出错，但是程序在运行时会出现错误的行为（详见实验 51）。



JavaBeans 模式



遇到许多可选的构造器参数的时候，还有第二种代替办法，即 JavaBeans 模式，在这种模式下，先调用一个无参构造器来创建对象，然后再调用 setter 方法来设置每个必要的参数，以及每个相关的可选参数：

```java
//JavaBeans Pattern - allows inconsistency，mandates mutability
public class NutritionFacts{
    //Parameters initialized to default values (if any)
    private int servingSize = -1;//Required;no default value
    private int servings = -1;//Required;no default value
    private int calories=0;
    private int fat=0;
    private int sodium=0;
    private int carbohydrate=0;
    public NutritionFacts(){}
    //Setters
    public void setServingSize(int val){servingSize=val;}
    public void setServings(int val){servings=val;}
    public void setCalories(int val){calories=val;}
    public void setFat(int val){fat=val;}
    public void setSodium(int val){sodium=val;}
    public void setCarbohydrate(int val){carbohydrate=val;}
}
```

这种模式弥补了重叠构造器模式的不足。说得明白一点，就是创建实例很容易，这样产生的代码读起来也很容易：

```java
NutritionFacts cocaCola=new NutritionFacts();
cocaCola.setServingSize(240);
cocaCola.setServings(8);
cocaCola.setCalories(100);
cocaCola.setSodium(35);
cocaCola.setCarbohydrate(27);
```

遗憾的是，JavaBeans 模式自身有着很严重的缺点。因为构造过程被分到了几个调用中，在构造过程中 JavaBean 可能处于不一致的状态。类无法仅仅通过检验构造器参数的有效性来保证一致性。试图使用处于不一致状态的对象将会导致失败，这种失败与包含错误的代码大相径庭，因此调试起来十分困难。与此相关的另一点不足在于，JavaBeans 模式使得把类做成不可变的可能性不复存在（详见实验 17），这就需要程序员付出额外的努力来确保它的线程安全。

当对象的构造完成，并且不允许在冻结之前使用时，通过手工“冻结”对象可以弥补这些不足，但是这种方式十分笨拙，在实践中很少使用。此外，它甚至会在运行时导致错误，因为编译器无法确保程序员会在使用之前先调用对象上的 freeze 方法进行冻结。



建造者（Builder）模式



第三种替代方法，它既能保证像重叠构造器模式那样的安全性，也能保证像 JavaBeans 模式那么好的可读性。这就是建造者（Builder）模式的一种形式。它不直接生成想要的对象，而是让客户端利用所有必要的参数调用构造器（或者静态工厂），得到一个 builder 对象。然后客户端在 builder 对象上调用类似于 setter 的方法，来设置每个相关的可选参数。最后，客户端调用无参的 build 方法来生成通常是不可变的对象。这个 builder 通常是它构建的类的静态成员类（详见实验 24）。下面就是它的示例：

```java
//Builder Pattern
public class NutritionFacts{
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public static class Buider{
        //Required parameters
        private final int servingSize;
        private final int servings;
        // Optional parameters - initialized to default values
        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;
        private int carbohydrate = 0;
        public Builder(int servingSize,int servings){
            this.servingSize=servingSize;
            this.servings=servings;
        }
        public Builder calories(int val)
            {calories=val;return this;}
        public Builder fat(int val)
            {fat=val;return this;}
        public Builder sodium(int val)
            {sodium=val;return this;}
        public Builder carbohydrate(int val)
            {carbohydrate= val;return this;}

        public NutritionFacts build(){
            return new NutritionFacts(this);
        }
    }
    private NutritionFacts(Builder builder){
        servingSize=builder.servingSize;
        servings=builder.servings;
        calories=builder.calories;
        fat=builder.fat;
        sodium=builder.sodium;
        carbohydrate=builder.carbohydrate;
    }

}
```

注意 NutritionFacts 是不可变的，所有的默认参数值都单独放在一个地方。builder 的设值方法返回 builder 本身，以便把调用链接起来，得到一个流式的 API。下面就是其客户端代码：

```java
NutritionFacts cocaCola=new NutritionFacts.Builder(240，8).calories(100).sodium(35).carbohydrate(27).build();
```

这样的客户端代码很容易编写，更为重要的是易于阅读。Builder 模式模拟了具名的可选参数，就像 Python 和 Scala 编程语言中的一样。

为了简洁起见，示例中省略了有效性检查。要想尽快侦测到无效的参数，可以在 builder 的构造器和方法中检查参数的有效性。查看不可变量，包括 build 方法调用的构造器中的多个参数。为了确保这些不变量免受攻击，从 builder 复制完参数之后，要检查对象域（详见实验 50）。如果检查失败，就抛出 IllegalArgumentException（详见实验 72），其中的详细 信息会说明哪些参数是无效的（详见实验 75）。

Builder 模式也适用于类层次结构。使用平行层次结构的 builder 时，各自嵌套在相应的类中。抽象类有抽象的 builder，具体类有具体的 builder。假设用类层次根部的一个抽象类表示各式各样的比萨：

```java
//Builder pattern for class hierarchies
public abstract class Pizza{
    public enum Topping{HAM,MUSHROOM,ONION,PEPPER,SAUSAGE}
    final Set<Topping> toppings;
    abstract static class Builder<T extends Builder<T>>{
      EnumSet<Topping> toppings=EnumSet .none0f(Topping.class);
      public T addTopping(Topping topping){
          toppings.add(Objects.requireNonNull(topping));
            return self();
        }
        abstract Pizza build();
        //Subclasses must override this method to return "this" protected abstract T self();
    }
    Pizza(Builder<?> builder){
        toppings=builder.toppings.clone();//See Item 50
    }
}
```

注意，Pizza.Builder 的类型是泛型（generic type），带有一个递归类型参数（recursive type parameter），详见实验 30。它和抽象的 self 方法一样，允许在子类中适当地进行方法链接，不需要转换类型。这个针对 Java 缺乏 self 类型的解决方案，被称作模拟的 self 类型 （simulated self-type）。

这里有两个具体的 Pizza 子类，其中一个表示经典纽约风味的比萨，另一个表示馅料内置的半月型（calzone）比萨。前者需要一个尺寸参数，后者则要你指定酱汁应该内置还是外置：

```java
public class NyPizza extends Pizza{
    public enum Size {SMALL,MEDIUM,LARGE}
    private final Size size;
    public static class Builder extends Pizza.Builder<Builder>{
        private final Size size;
        public Builder(Size size){
            this.size=Objects.repuireNonNull(size);
        }
        @Override
        public NyPizza build(){
            return new NyPizza(this);
        }

        @Override
        protected Builder self(){return this;}
    }
    private NyPizza(Builder builder){
        super(builder);
        size=builder.size;
    }
}
public class Calzone extends Pizza{
    private final boolean sauceInside;
    public static class Builder extends Pizza.Builder<Builder>{
        private boolean sauceInside=false;//Default
        public Builder sauceInside(){
            sauceInside=true;
            return this;
        }
        @Override public Calzone build(){
            return new Calzone(this);
        }
        @Override protected Builder self(){return this;}
    }
    private Calzone(Builder builder){
        super(builder);
        sauceInside=builder.sauceInside;
    }
}
```

注意，每个子类的构建器中的 build 方法，都声明返回正确的子类：NyPizza.Builder 的 build 方法返回 NyPizza，而 Calzone.Builder 中的则返回 Calzone。在该方法中，子类方法声明返回超级类中声明的返回类型的子类型，这被称作协变返回类型（covariant return type）。它允许客户端无须转换类型就能使用这些构建器。

这些“层次化构建器”的客户端代码本质上与简单的 NutritionFacts 构建器一样。为了简洁起见，下列客户端代码示例假设是在枚举常量上静态导入：

```java
NyPizza pizza=new NyPizza .Builder(SMALL).addTopping(SAUSAGE).addTopping(ONION).build();
Calzone calzone=new Calzone .Builder().addTopping(HAM).sauceInside().build();
```

与构造器相比，builder 的微略优势在于，它可以有多个可变（varargs）参数。因为 builder 是利用单独的方法来设置每一个参数。此外，构建器还可以将多次调用某一个方法而传入的参数集中到一个域中，如前面的调用了两次 addTopping 方法的代码所示。

Builder 模式十分灵活，可以利用单个 builder 构建多个对象。builder 的参数可以在调用 build 方法来创建对象期间进行调整，也可以随着不同的对象而改变。builder 可以自动填充某些域，例如每次创建对象时自动增加序列号。

Builder 模式的确也有它自身的不足。为了创建对象，必须先创建它的构建器。虽然创建这个构建器的开销在实践中可能不那么明显，但是在某些十分注重性能的情况下，可能就成问题了。Builder 模式还比重叠构造器模式更加冗长，因此它只在有很多参数的时候才使用，比如 4 个或者更多个参数。但是记住，将来你可能需要添加参数。如果一开始就使用构造器或者静态工厂，等到类需要多个参数时才添加构造器，就会无法控制，那些过时的构造器或者静态工厂显得十分不协调。因此，通常最好一开始就使用构建器。

简而言之，如果类的构造器或者静态工厂中具有多个参数，设计这种类时，Builder 模式就是一种不错的选择，特别是当大多数参数都是可选或者类型相同的时候。与使用重叠构造器模式相比，使用 Builder 模式的客户端代码将更易于阅读和编写，构建器也比 JavaBeans 更加安全。



实验总结



本次实验详细介绍了当一个类的构造器或者静态工厂中具有多个参数时，可以使用重叠构造器模式、JavaBeans 模式、建造者（Builder）模式等三种模式来设计这种类，以及这三种模式的优缺点。

# 实验3	用私有构造器或者枚举类型强化 Singleton 属性

实验介绍



在本此实验中讲详细介绍使用私有构造器实现 Singleton 和使用枚举类型实现 Singleton，以及这两种实现类型的优势。

#### 知识点

- 私有构造器实现 Singleton
- 使用枚举类型实现 Singleton

私有构造器实现 Singleton



Singleton 是指仅仅被实例化一次的类。Singleton 通常被用来代表一个无状态的对象，如函数（详见实验 24），或者那些本质上唯一的系统组件。使类成为 Singleton 会使它的客户端测试变得十分困难，因为不可能给 Singleton 替换模拟实现，除非实现一个充当其类型的接口。

实现 Singleton 有两种常见的方法。这两种方法都要保持构造器为私有的，并导出公有的静态成员，以便允许客户端能够访问该类的唯一实例。

在第一种方法中，公有静态成员是个 final 域：

```java
//Singleton with public final field
public class Elvis{
    public static final Elvis INSTANCE=new Elvis();
    private Elvis(){ ... }
    public void leaveTheBu们ding(){ ... }
}
```

私有构造器仅被调用一次，用来实例化公有的静态 final 域 Elvis.INSTANCE。由于缺少公有的或者受保护的构造器，所以保证了 Elvis 的全局唯一性：一旦 Elvis 类被实例化，将只会存在一个 Elvis 实例，不多也不少。客户端的任何行为都不会改变这一点，但要提醒一点：享有特权的客户端可以借助 AccessibleObject.setAccessible 方法，通过反射机制（详见实验 65）调用私有构造器。如果需要抵御这种攻击，可以修改构造器，让它在被要求创建第二个实例的时候抛出异常。

在第二种方法中，公有的成员是个静态工厂方法：

```java
//Singleton with static factory
public class Elvis{
    private static final Elvis INSTANCE=new Elvis();
    private Elvis(){...}
    public static Elvis getInstance(){return INSTANCE;}
    public void leaveTheBuilding(){...}
}
```

对于静态方法 Elvis.getInstance 的所有调用，都会返回同一个对象引用，所以，永远不会创建其他的 Elvis 实例（上述提醒依然适用）。

公有域方法的主要优势在于，API 很清楚地表明了这个类是一个 Singleton ：公有的静态域是 final 的，所以该域总是包含相同的对象引用。第二个优势在于它更简单。

静态工厂方法的优势之一在于，它提供了灵活性：在不改变其 API 的前提下，我们可以改变该类是否应该为 Singleton 的想法。工厂方法返回该类的唯一实例，但是，它很容易被修改，比如改成为每个调用该方法的线程返回一个唯一的实例。第二个优势是，如果应用程序需要，可以编写一个泛型 Singleton 工厂（generic singleton factory）（详见实验 30）。使用静态工厂的最后一个优势是，可以通过方法引用（method reference）作为提供者，比如 Elvis::instance 就是一个 Supplier。除非满足以上任意一种优势，否则还是优先考虑公有域（public-field）的方法。

为了将利用上述方法实现的 Singleton 类变成是可序列化的（Serializable）（详见实验 12），仅仅在声明中加上 implements Serializable 是不够的。为了维护并保证 Singleton，必须声明所有实例域都是瞬时（transient）的，并提供一个 readResolve 方法（详见实验 89）。否则，每次反序列化一个序列化的实例时，都会创建一个新的实例，比如，在我们的例子中，会导致“假冒的 Elvis”。为了防止发生这种情况，要在 Elvis 类中加入如下 readResolve 方法：

```java
//readResolve method to preserve singleton property
private Object readResolve(){
      //Return the one true Elvis and let the garbage collector
      //take care of the Elvis impersonator.
      return INSTANCE;
}
```



使用枚举类型实现 Singleton



实现 Singleton 的第三种方法是声明一个包含单个元素的枚举类型：

```java
//Enum singleton-the preferred approach
public enum Elvis{
      INSTANCE;
public void leaveTheBuilding(){...}
}
```

这种方法在功能上与公有域方法相似，但更加简洁，无偿地提供了序列化机制，绝对防止多次实例化，即使是在面对复杂的序列化或者反射攻击的时候。虽然这种方法还没有广泛采用，但是单元素的枚举类型经常成为实现 Singleton 的最佳方法。注意，如果 Singleton 必须扩展一个超类，而不是扩展 Enum 的时候，则不宜使用这个方法（虽然可以声明枚举去实现接口）。



实验总结



本次实验主要讲解了三种实现 Singleton 的方法，并且介绍了这三种方法各自的优势。



# 实验4 通过私有构造器强化不可实例化的能力

实验介绍



在本次实验中将介绍在一个类中使用私有构造器的方式让其不能被实例化。

#### 知识点

- 通过私有构造器使类不能被实例化

通过私有构造器使类不能被实例化



有时可能需要编写只包含静态方法和静态域的类。这些类的名声很不好，因为有些人在面向对象的语言中滥用这样的类来编写过程化的程序，但它们也确实有特别的用处。我们可以利用这种类，以 java.lang.Math 或者 java.util.Arrays 的方式，把基本类型的值或者数组类型上的相关方法组织起来。我们也可以通过 java.util.Collections 的方式，把实现特定接口的对象上的静态方法，包括工厂方法（详见实验 1）组织起来。（从 Java 8 开始，也可以把这些方法放进接口中，假定这是你自己编写的接口可以进行修改。）最后，还可以利用这种类把 final 类上的方法组织起来，因为不能把它们放在子类中。

这样的工具类（utility class）不希望被实例化，因为实例化对它没有任何意义。然而，在缺少显式构造器的情况下，编译器会自动提供一个公有的、无参的缺省构造器（default constructor）。对于用户而言，这个构造器与其他的构造器没有任何区别。在已发行的 API 中常常可以看到一些被无意识地实例化的类。

企图通过将类做成抽象类来强制该类不可被实例化是行不通的。该类可以被子类化，并且该子类也可以被实例化。这样做甚至会误导用户，以为这种类是专门为了继承而设计的（详见实验 19）。然而，有一些简单的习惯用法可以确保类不可被实例化。由于只有当类不包含显式的构造器时，编译器才会生成缺省的构造器，因此只要让这个类包含一个私有构造器，它就不能被实例化：

```java
//Noninstantiable utility class
public class UtilityClass{
    //Suppress default constructor for noninstantiability
    private UtilityClass(){
        throw new AssertionError();
    }
    ... //Remainder omitted
}
```

由于显式的构造器是私有的，所以不可以在该类的外部访问它。AssertionError 不是必需的，但是它可以避免不小心在类的内部调用构造器。它保证该类在任何情况下都不会被实例化。这种习惯用法有点违背直觉，好像构造器就是专门设计成不能被调用一样。因此，明智的做法就是在代码中增加一条注释，如上所示。

这种习惯用法也有副作用，它使得一个类不能被子类化。所有的构造器都必须显式或隐式地调用超类（superclass）构造器，在这种情形下，子类就没有可访问的超类构造器可调用了。



实验总结



本次实验介绍了一种使用私有构造器的方式让一个类不能实例化的方法，及其副作用。

# 实验5	优先考虑依赖注入来引用资源

实验介绍



在本次实验中将介绍使用依赖注入来引用资源的方法，以及使用这种方式与其他方式有哪些优势。

#### 知识点

- 优先考虑依赖注入来引用资源

优先考虑依赖注入来引用资源



有许多类会依赖一个或多个底层的资源。例如，拼写检查器需要依赖词典。因此，像下面这样把类实现为静态工具类的做法并不少见（详见实验 4）：

```java
//Inappropriate use of static utility inflexible & untestable!
public class SpellChecker{
    private static final Lexicon dictionary=...;
    private SpellChecker(){}//Noninstantiable
    public static boolean isValid(String word){...}
    public static List<String> suggestions(String typo){...}
}
```

同样地，将这些类实现为 Singleton 的做法也并不少见（详见实验 3）：

```java
//Inappropriate use of singleton-inflexible&untestable!
public class SpellChecker{
    private final Lexicon dictionary=...;
    private SpellChecker(...){}
    public static INSTANCE=new SpellChecker(...);
    public boolean isValid(String word){...}
    public List<String> suggestions(String typo){...}
}
```

以上两种方法都不理想，因为它们都是假定只有一本词典可用。实际上，每一种语言都有自己的词典，特殊词汇还要使用特殊的词典。此外，可能还需要用特殊的词典进行测试。因此假定用一本词典就能满足所有需求，这简直是痴心妄想。

建议尝试用 SpellChecker 来支持多词典，即在现有的拼写检查器中，设 dictionary 域为 nonfinal，并添加一个方法用它来修改词典，但是这样的设置会显得很笨拙、容易出错，并且无法并行工作。静态工具类和 Singleton 类不适合于需要引用底层资源的类。

这里需要的是能够支持类的多个实例（在本例中是指 SpellChecker），每一个实例都使用客户端指定的资源（在本例中是指词典）。满足该需求的最简单的模式是，当创建一个新的实例时，就将该资源传到构造器中。这是依赖注入（dependency injection）的一种形式：词典（dictionary）是拼写检查器的一个依赖（dependency），在创建拼写检查器时就将词典注入 （injected）其中。

```java
//Dependency injection provides flexibility and testability
public class SpellChecker{
    private final Lexicon dictionary;
    public SpellChecker(Lexicon dictionary){
          this .dictionary=Objects.requireNonNull(dictionary);
      }
    public boolean isValid(String word){...}
    public List<String> suggestions(String typo){...}
}
```

依赖注入模式就是这么简单，因此许多程序员使用多年，却不知道它还有名字呢。虽然这个拼写检查器的范例中只有一个资源（词典），但是依赖注入却适用于任意数量的资源，以及任意的依赖形式。依赖注入的对象资源具有不可变性（详见实验 17），因此多个客户端可以共享依赖对象（假设客户端们想要的是同一个底层资源）。依赖注入也同样适用于构造器、静态工厂（详见实验 1）和构建器（详见实验 2）。

这个程序模式的另一种有用的变体是，将资源工厂（factory）传给构造器。工厂是可以被重复调用来创建类型实例的一个对象。这类工厂具体表现为工厂方法（Factory Method）模式。在 Java 8 中增加的接口 Supplier，最适合用于表示工厂。带有 Supplier 的方法，通常应该限制输入工厂的类型参数使用有限制的通配符类型（bounded wildcard type），详见实验 31，以便客户端能够传入一个工厂，来创建指定类型的任意子类型。例如，下面是一个生产马赛克的方法，它利用客户端提供的工厂来生产每一片马赛克：

```java
Mosaic create(Supplier<? extends Tile> tileFactory){...}
```

虽然依赖注入极大地提升了灵活性和可测试性，但它会导致大型项目凌乱不堪，因为它通常包含上千个依赖。不过这种凌乱用一个依赖注入框架（dependency injection framework）便可以终结，如 Dagger [Dagger]、Guice [Guice] 或者 Spring [Spring]。这些框架的用法超出了本课程的讨论范畴，但是，请注意：设计成手动依赖注入的 API，一般都适用于这些框架。



实验总结



总而言之，不要用 Singleton 和静态工具类来实现依赖一个或多个底层资源的类，且该资源的行为会影响到该类的行为；也不要直接用这个类来创建这些资源。而应该将这些资源或者工厂传给构造器（或者静态工厂，或者构建器），通过它们来创建类。这个实践就被称作依赖注入，它极大地提升了类的灵活性、可重用性和可测试性。



# 实验6	避免创建不必要的对象

实验介绍



本次实验将介绍几种平常我们会出现创建了多余的对象的情况，以及我们应该怎样避免这些情况的发生。

#### 知识点

- 避免创建不必要的对象

避免创建不必要的对象



一般来说，最好能重用单个对象，而不是在每次需要的时候就创建一个相同功能的新对象。重用方式既快速，又流行。如果对象是不可变的（immutable）（详见实验 17），它就始终可以被重用。

作为一个极端的反面例子，看看下面的语句：

```java
String s = new String("bikini"); //DON'T DO THIS!
```

该语句每次被执行的时候都创建一个新的 String 实例，但是这些创建对象的动作全都是不必要的。传递给 String 构造器的参数（"bikini"）本身就是一个 String 实例，功能方面等同于构造器创建的所有对象。如果这种用法是在一个循环中，或者是在一个被频繁调用的方法中，就会创建出成千上万不必要的 String 实例。

改进后的版本如下所示：

```java
String s = "bikini";
```

这个版本只用了一个 String 实例，而不是每次执行的时候都创建一个新的实例。而且，它可以保证，对于所有在同一台虚拟机中运行的代码，只要它们包含相同的字符串字面常量，该对象就会被重用。

对于同时提供了静态工厂方法（static factory method）（详见实验 1）和构造器的不可变类，通常优先使用静态工厂方法而不是构造器，以避免创建不必要的对象。例如，静态工厂方法 Boolean.valueOf (String) 几乎总是优先于构造器 Boolean(String)，注意构造器 Boolean(String) 在 Java 9 中已经被废弃了。构造器在每次被调用的时候都会创建一个新的对象，而静态工厂方法则从来不要求这样做，实际上也不会这样做。除了重用不可变的对象之外，也可以重用那些已知不会被修改的可变对象。

有些对象创建的成本比其他对象要高得多。如果重复地需要这类“昂贵的对象”，建议将它缓存下来重用。遗憾的是，在创建这种对象的时候，并非总是那么显而易见。假设想要编写一个方法，用它确定一个字符串是否为一个有效的罗马数字。下面介绍一种最容易的方法，使用一个正则表达式：

```java
//Performance can be greatly improved!
static boolean isRomanNumeral(String s){
    return s.matches("A(?=.)M *(C [MD]|D?C{0，3})"
        +"(X[CL]|L?X{0，3})(I[XV]|V?I{0，3})$”);
}
```

这个实现的问题在于它依赖 String.matches 方法。虽然 String.matches 方法最易于查看一个字符串是否与正则表达式相匹配，但并不适合在注重性能的情形中重复使用。问题在于，它在内部为正则表达式创建了一个 Pattern 实例，却只用了一次，之后就可以进行垃圾回收了。创建 Pattern 实例的成本很高，因为需要将正则表达式编译成一个有限状态机（finite state machine）。

为了提升性能，应该显式地将正则表达式编译成一个 Pattern 实例（不可变），让它成为类初始化的一部分，并将它缓存起来，每当调用 isRomanNumeral 方法的时候就重用同一个实例：

```java
//Reusing expensive object for improved performance
public class RomanNumerals{
    private static final Pattern ROMAN=Pattern.compile(
        "^(?=.)M * (C [MD]|D?C{0，3})"
        +"(X [CL]|L?X{0，3})(I[XV]|V?I{0，3})$");

    static boolean isRomanNumeral(String s){
        return ROMAN.matcher(s).matches();
    }
}
```

改进后的 isRomanNumeral 方法如果被频繁地调用，会显示出明显的性能优势。在我的机器上，原来的版本在一个 8 字符的输入字符串上花了 1.1μs，而改进后的版本只花了 0.17μs，速度快了 6.5 倍。除了提高性能之外，可以说代码也更清晰了。将不可见的 Pattern 实例做成 final 静态域时，可以给它起个名字，这样会比正则表达式本身更有可读性。

如果包含改进后的 isRomanNumeral 方法的类被初始化了，但是该方没有被调用，那就没必要初始化 ROMAN 域。通过在 isRomanNumeral 方法第一次被调用的时候延迟初始化（lazily initializing）（详见 s 实验 83）这个域，有可能消除这个不必要的初始化工作，但是不建议这样做。正如延迟初始化中常见的情况一样，这样做会使方法的实现更加复杂，从而无法将性能显著提高到超过已经达到的水平（详见实验 67）。

如果一个对象是不变的，那么它显然能够被安全地重用，但其他有些情形则并不总是这么明显。考虑适配器（adapter）的情形，有时也叫作视图（view）。适配器是指这样一个对象：它把功能委托给一个后备对象（backing object），从而为后备对象提供一个可以替代的接口。由于适配器除了后备对象之外，没有其他的状态信息，所以针对某个给定对象 的特定适配器而言，它不需要创建多个适配器实例。

例如，Map 接口的 keySet 方法返回该 Map 对象的 Set 视图，其中包含该 Map 中所有的键（key）。乍看之下，好像每次调用 keySet 都应该创建一个新的 Set 实例，但是，对于一个给定的 Map 对象，实际上每次调用 keySet 都返回同样的 Set 实例。虽然被返回的 Set 实例一般是可改变的，但是所有返回的对象在功能上是等同的：当其中一个返回对象发生变化的时候，所有其他的返回对象也要发生变化，因为它们是由同一个 Map 实例支撑的。虽然创建 keySet 视图对象的多个实例并无害处，却是没有必要，也没有好处的。

另一种创建多余对象的方法，称作自动装箱（autoboxing），它允许程序员将基本类型和装箱基本类型（Boxed Primitive Type）混用，按需要自动装箱和拆箱。自动装箱使得基本类型和装箱基本类型之间的差别变得模糊起来，但是并没有完全消除。它们在语义上还有着微妙的差别，在性能上也有着比较明显的差别（详见实验 61）。请看下面的程序，它计算所有 int 正整数值的总和。为此，程序必须使用 long 算法，因为 int 不够大，无法容纳所有 int 正整数值的总和：

```java
//Hideously slow! Can you spot the object creation?
private static long sum(){
    Long sum = 0L;
    for(long i =0;i<=Integer.MAX_VALUE;i++)
        sum +=i;
    return sum;

}
```

这段程序算出的答案是正确的，但是比实际情况要更慢一些，只因为打错了一个字符。变量 sum 被声明成 Long 而不是 long，意味着程序构造了大约 231 个多余的 Long 实例（大约每次往 Long sum 中增加 long 时构造一个实例）。将 sum 的声明从 Long 改成 long，在我的 机器上使运行时间从 6.3 秒减少到了 0.59 秒。结论很明显：要优先使用基本类型而不是装箱基本类型，要当心无意识的自动装箱。

不要错误地认为本实验所介绍的内容暗示着“创建对象的代价非常昂贵，我们应该要尽可能地避免创建对象”。相反，由于小对象的构造器只做很少量的显式工作，所以小对象的创建和回收动作是非常廉价的，特别是在现代的 JVM 实现上更是如此。通过创建附加的对象，提升程序的清晰性、简洁性和功能性，这通常是件好事。

反之，通过维护自己的对象池（object pool）来避免创建对象并不是一种好的做法，除非池中的对象是非常重量级的。正确使用对象池的典型对象示例就是数据库连接池。建立数据库连接的代价是非常昂贵的，因此重用这些对象非常有意义。而且，数据库的许可可能限制你只能使用一定数量的连接。但是，一般而言，维护自己的对象池必定会把代码弄得很乱，同时增加内存占用（footprint），并且还会损害性能。现代的 JVM 实现具有高度优化的垃圾回收器，其性能很容易就会超过轻量级对象池的性能。

与本实验对应的是实验 50 中有关“保护性拷贝”（defensive copying）的内容。本实验提及“当你应该重用现有对象的时候，请不要创建新的对象”，而在实验 50 中则说“当你应该创建新对象的时候，请不要重用现有的对象”。注意，在提倡使用保护性拷贝的时候，因重用对象而付出的代价要远远大于因创建重复对象而付出的代价。必要时如果没能实施保护性拷贝，将会导致潜在的 Bug 和安全漏洞；而不必要地创建对象则只会影响程序的风格和性能。



实验总结



本次实验介绍了几种我们在编程时会遇到的创建了多余的对象的情况，也指出了其缺点，但是并不是说任何时候我们都应该重用对象，有些情况下，重用对象付出的代价要远远大于因创建重复对象而付出的代价，所以是否重用应视情况而定。



# 实验7	消除过期的对象引用

实验介绍



虽然 Java 语言具有垃圾回收功能，但是它并不会处理那些被无意识的保留起来的对象引用以及被这个对象所引用的所有其他对象，就会出现内存泄漏的情况。在本次实验中将为同学们介绍内存泄漏的几种常见来源，以及怎样解决这些情况下的内存泄露问题。

#### 知识点

- 内存泄漏的常见来源
- 内存泄漏的处理方法

消除过期的对象引用



当你从手工管理内存的语言（比如 C 或 C++）转换到具有垃圾回收功能的比如 Java 语言时，程序员的工作会变得更加容易，因为当你用完了对象之后，它们会被自动回收。当你第一次经历对象回收功能的时候，会觉得这简直有点不可思议。它很容易给你留下这样的印象，认为自己不再需要考虑内存管理的事情了，其实不然。

请看下面这个简单的栈实现的例子：

```java
// Can you spot the "memory leak"?
public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        return elements[--size];
    }

    /**
     * Ensure space for at least one more element, roughly
     * doubling the capacity each time the array needs to grow.
     */
    private void ensureCapacity() {
        if (elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }
}
```

这段程序（它的泛型版本请见实验 29）中并没有很明显的错误。无论如何测试，它都会成功地通过每一项测试，但是这个程序中隐藏着一个问题。不严格地讲，这段程序有一个“内存泄漏”，随着垃圾回收器活动的增加，或者由于内存占用的不断增加，程序性能的降低会逐渐表现出来。在极端的情况下，这种内存泄漏会导致磁盘交换（Disk Paging），甚至导致程序失败（OutOfMemoryError 错误），但是这种失败情形相对比较少见。

那么，程序中哪里发生了内存泄漏呢？如果一个栈先是增长，然后再收缩，那么，从栈中弹出来的对象将不会被当作垃圾回收，即使使用栈的程序不再引用这些对象，它们也不会被回收。这是因为栈内部维护着对这些对象的过期引用（obsolete reference）。所谓的过期引用，是指永远也不会再被解除的引用。在本例中，凡是在 elements 数组的“活动部分”（active portion）之外的任何引用都是过期的。活动部分是指 elements 中下标小于 size 的那些元素。

在支持垃圾回收的语言中，内存泄漏是很隐蔽的（称这类内存泄漏为“无意识的对象保持”（unintentional object retention）更为恰当）。如果一个对象引用被无意识地保留起来了，那么垃圾回收机制不仅不会处理这个对象，而且也不会处理被这个对象所引用的所有其他对象。即使只有少量的几个对象引用被无意识地保留下来，也会有许许多多的对象被排除在垃圾回 收机制之外，从而对性能造成潜在的重大影响。

这类问题的修复方法很简单：一旦对象引用已经过期，只需清空这些引用即可。对于上述例子中的 Stack 类而言，只要一个单元被弹出栈，指向它的引用就过期了。pop 方法的修订版本如下所示：

```java
public Object pop(){
    if (size==0)
          throw new EmptyStackException();
    Object result=elements[--size];
    elements[size]=null;//Eliminate obsolete reference
      return result;
}
```

清空过期引用的另一个好处是，如果它们以后又被错误地解除引用，程序就会立即抛出 NullPointerException 异常，而不是悄悄地错误运行下去。尽快地检测出程序中的错误总是有益的。

当程序员第一次被类似这样的问题困扰的时候，他们往往会过分小心：对于每一个对象引用，一旦程序不再用到它，就把它清空。其实这样做既没必要，也不是我们所期望的，因为这样做会把程序代码弄得很乱。清空对象引用应该是一种例外，而不是一种规范行为。消除过期引用最好的方法是让包含该引用的变量结束其生命周期。如果你是在最紧凑的作用域范围内定义每一个变量（详见实验 57），这种情形就会自然而然地发生。

那么，何时应该清空引用呢？ Stack 类的哪方面特性使它易于遭受内存泄漏的影响呢？简而言之，问题在于，Stack 类自己管理内存。存储池（storage pool）包含了 elements 数组（对象引用单元，而不是对象本身）的元素。数组活动区域（同前面的定义）中的元素是已分配的（allocated），而数组其余部分的元素则是自由的（free）。但是垃圾回收器并不知道这一点；对于垃圾回收器而言，elements 数组中的所有对象引用都同等有效。只有程序员知道数组的非活动部分是不重要的。程序员可以把这个情况告知垃圾回收器，做法很简单：一旦数组元素变成了非活动部分的一部分，程序员就手工清空这些数组元素。

一般来说，只要类是自己管理内存，程序员就应该警惕内存泄漏问题。一旦元素被释放掉，则该元素中包含的任何对象引用都应该被清空。

内存泄漏的另一个常见来源是缓存。一旦你把对象引用放到缓存中，它就很容易被遗忘掉，从而使得它不再有用之后很长一段时间内仍然留在缓存中。对于这个问题，有几种可能的解决方案。如果你正好要实现这样的缓存：只要在缓存之外存在对某个项的键的引用，该项就有意义，那么就可以用 WeakHashMap 代表缓存；当缓存中的项过期之后，它们就会自动被删除。记住只有当所要的缓存项的生命周期是由该键的外部引用而不是由值决定时，WeakHashMap 才有用处。

更为常见的情形则是，“缓存项的生命周期是否有意义”并不是很容易确定，随着时间的推移，其中的项会变得越来越没有价值。在这种情况下，缓存应该时不时地清除掉没用的项。这项清除工作可以由一个后台线程（可能是 ScheduledThreadPoolExecutor）来完成，或者也可以在给缓存添加新条目的时候顺便进行清理。LinkedHashMap 类利用它 removeEldestEntry 方法可以很容易地实现后一种方案。对于更加复杂的缓存，必须直接使 用 java.lang.ref。

内存泄漏的第三个常见来源是监听器和其他回调。如果你实现了一个 API，客户端在这个 API 中注册回调，却没有显式地取消注册，那么除非你采取某些动作，否则它们就会不断地堆积起来。确保回调立即被当作垃圾回收的最佳方法是只保存它们的弱引用（weak reference），例如，只将它们保存成 WeakHashMap 中的键。

由于内存泄漏通常不会表现成明显的失败，所以它们可以在一个系统中存在很多年。往往只有通过仔细检查代码，或者借助于 Heap 剖析工具（Heap Prof iler）才能发现内存泄漏问题。因此，如果能够在内存泄漏发生之前就知道如何预测此类问题，并阻止它们发生，那是最好不过的了。



实验总结



本次实验介绍了 Java 语言中几种内存泄漏的常见来源，以及怎样处理这些情况下的内存泄漏。



# 实验8	避免使用终结方法和清除方法

实验介绍



在本次实验中将详细分析使用终结方法和清除方法的优势与劣势，并通过一个案例来介绍使用清除方法的技巧。

#### 知识点

- 终结方法和清除方法的缺点
- 终结方法和清除方法的优势

终结方法和清除方法的缺点



终结方法（finalizer）通常是不可预测的，也是很危险的，一般情况下是不必要的。使用终结方法会导致行为不稳定、性能降低，以及可移植性问题。当然，终结方法也有其可用之处，我们将在本实验的最后再做介绍；但是根据经验，应该避免使用终结方法。在 Java 9 中用清除方（cleaner）代替了终结方法。清除方法没有终结方法那么危险，但仍然是不可预测、运行缓慢，一般情况下也是不必要的。

C++ 的程序员被告知“不要把终结方法当作是 C++ 中析构器（destructors）的对应物”。在 C++ 中，析构器是回收一个对象所占用资源的常规方法，是构造器所必需的对应物。在 Java 中，当一个对象变得不可到达的时候，垃圾回收器会回收与该对象相关联的存储空间，并不需要程序员做专门的工作。C++ 的析构器也可以被用来回收其他的非内存资源。而在 Java 中，一般用 try-finally 块来完成类似的工作（详见实验 9）。

终结方法和清除方法的缺点在于不能保证会被及时执行。从一个对象变得不可到达开始，到它的终结方法被执行，所花费的这段时间是任意长的。这意味着，注重时间（time-critical）的任务不应该由终结方法或者清除方法来完成。例如，用终结方法或者清除方法来关闭已经打开的文件，就是一个严重的错误，因为打开文件的描述符是一种很有限的资源。如果系统无法及时运行终结方法或者清除方法就会导致大量的文件仍然保留在打开状态，于是当一个程序再也不能打开文件的时候，它可能会运行失败。

及时地执行终结方法和清除方法正是垃圾回收算法的一个主要功能，这种算法在不同的 JVM 实现中会大相径庭。如果程序依赖于终结方法或者清除方法被执行的时间点，那么这个程序的行为在不同的 JVM 中运行的表现可能就会截然不同。一个程序在你测试用的 JVM 平台上运行得非常好，而在你最重要顾客的 JVM 平台上却根本无法运行，这是完全有可能的。

延迟终结过程并不只是一个理论问题。在很少见的情况下，为类提供终结方法，可能会随意地延迟其实例的回收过程。一位同事最近在调试一个长期运行的 GUI 应用程序的时候，该应用程序莫名其妙地出现 OutOfMemoryError 错误而死掉。分析表明，该应用程序死掉的时候，其终结方法队列中有数千个图形对象正在等待被终结和回收。遗憾的是，终结方法线程的优先级比该应用程序的其他线程的优先级要低得多，所以，图形对象的终结速度达不到它们进入队列的速度。Java 语言规范并不保证哪个线程将会执行终结方法，所以，除了不使用终结方法之外，并没有很轻便的办法能够避免这样的问题。在这方面，清除方法比终结方法稍好一些，因为类的设计者可以控制自己的清除线程，但清除方法仍然在后台运行，处于垃圾回收器的控制之下，因此不能确保及时清除。

Java 语言规范不仅不保证终结方法或者清除方法会被及时地执行，而且根本就不保证它们会被执行。当一个程序终止的时候，某些已经无法访问的对象上的终结方法却根本没有被执行，这是完全有可能的。结论是：永远不应该依赖终结方法或者清除方法来更新重要的持久状态。例如，依赖终结方法或者清除方法来释放共享资源（比如数据库）上的永久锁，这很容易让整个分布式系统垮掉。

不要被 System.gc 和 System.runFinalization 这两个方法所诱惑，它们确实增加了终结方法或者清除方法被执行的机会，但是它们并不保证终结方法或者清除方法一定会被执行。唯一声称保证它们会被执行的两个方法是 System.runFinalizersOnExit，及其臭名昭著的孪生兄弟 Runtime.runFinalizersOnExit。这两个方法都有致命的缺陷，并且已经被废弃很久了。

使用终结方法的另一个问题是：如果忽略在终结过程中被抛出来的未被捕获的异常，该对象的终结过程也会终止。未被捕获的异常会使对象处于破坏的状态（corrupt state），如果另一个线程企图使用这种被破坏的对象，则可能发生任何不确定的行为。正常情况下，未被捕获的异常将会使线程终止，并打印出栈轨迹（Stack Trace），但是，如果异常发生在终结方法之中，则不会如此，甚至连警告都不会打印出来。清除方法没有这个问题，因 为使用清除方法的一个类库在控制它的线程。

使用终结方法和清除方法有一个非常严重的性能损失。在我的机器上，创建一个简单的 AutoCloseable 对象，用 try-with-resources 将它关闭，再让垃圾回收器将它回收，完成这些工作花费的时间大约为 12ns。增加一个终结方法使时间增加到了 550ns。换句话说，用终结方法创建和销毁对象慢了大约 50 倍。这主要是因为终结方法阻止了有效的垃圾回收。如果用清除方法来清除类的所有实例，它的速度比终结方法会稍微快一些（在我的机器上大约是每个实例花 500ns），但如果只是把清除方法作为一道安全网（safety net），下面将会介绍，那么清除方法的速度还会更快一些。在这种情况下，创建、清除和销毁对象，在我的机器上花了大约 66ns，这意味着，如果没有使用它，为了确保安全网多花了 5 倍（而不是 50 倍）的代价。

终结方法有一个严重的安全问题：它们为终结方法攻击（finalizer attack）打开了类的大门。终结方法攻击背后的思想很简单：如果从构造器或者它的序列化对等体（readObject 和 readResolve 方法，详见实验 12）抛出异常，恶意子类的终结方法就可以在构造了一部分的应该已经半途夭折的对象上运行。这个终结方法会将对该对象的引用记录在一个静态域中，阻止它被垃圾回收。一旦记录到异常的对象，就可以轻松地在这个对象上调用任何原本永远不允许在这里出现的方法。从构造器抛出的异常，应该足以防止对象继续存在；有了终结方法的存在，这一点就做不到了。这种攻击可能造成致命的后果。final 类不会受到终结方法攻击，因为没有人能够编写出 final 类的恶意子类。为了防止非 final 类受到终结方法攻击，要编写一个空的 final 的 finalize 方法。

那么，如果类的对象中封装的资源（例如文件或者线程）确实需要终止，应该怎么做才能不用编写终结方法或者清除方法呢？只需让类实现 AutoCloseable，并要求其客户端在每个实例不再需要的时候调用 close 方法，一般是利用 try-with-resources 确保终止，即使遇到异常也是如此（详见实验 9）。值得提及的一个细节是，该实例必须记录下自己是否已经被关闭了：close 方法必须在一个私有域中记录下“该对象已经不再有效”。如果这些方法是在对象已经终止之后被调用，其他的方法就必须检查这个域，并抛出 IllegalStateException 异常。



终结方法和清除方法的优势



它们有两种合法用途。第一种用途是，当资源的所有者忘记调用它的 close 方法时，终结方法或者清除方法可以充当“安全网”。虽然这样做并不能保证终结方法或者清除方法会被及时地运行，但是在客户端无法正常结束操作的情况下，迟一点释放资源总比永远不释放要好。如果考虑编写这样的安全网终结方法，就要认真考虑清楚，这种保护是否值得付出这样的代价。有些 Java 类（如 FileInputStream、FileOutputStream、ThreadPoolExecutor 和 java.sql.Connection）都具有能充当安全网的终结方法。

清除方法的第二种合理用途与对象的本地对等体（native peer）有关。本地对等体是一个本地（非 Java 的）对象（native object），普通对象通过本地方法（native method）委托给一个本地对象。因为本地对等体不是一个普通对象，所以垃圾回收器不会知道它，当它的 Java 对等体被回收的时候，它不会被回收。如果本地对等体没有关键资源，并且性能也可以接受的话，那么清除方法或者终结方法正是执行这项任务最合适的工具。如果本地对等体拥有必须被及时终止的资源，或者性能无法接受，那么该类就应该具有一个 close 方法，如前所述。

清除方法的使用有一定的技巧。下面以一个简单的 Room 类为例。假设房间在收回之前必须进行清除。Room 类实现了 AutoCloseable ；它利用清除方法自动清除安全网的过程只不过是一个实现细节。与终结方法不同的是，清除方法不会污染类的公有 API：

```java
import java.lang.ref.Cleaner;
// An autocloseable class using a cleaner as a safety net
public class Room implements AutoCloseable {
    private static final Cleaner cleaner = Cleaner.create();

    // Resource that requires cleaning. Must not refer to Room!
    private static class State implements Runnable {
        int numJunkPiles; // Number of junk piles in this room

        State(int numJunkPiles) {
            this.numJunkPiles = numJunkPiles;
        }

        // Invoked by close method or cleaner
        @Override public void run() {
            System.out.println("Cleaning room");
            numJunkPiles = 0;
        }
    }

    // The state of this room, shared with our cleanable
    private final State state;

    // Our cleanable. Cleans the room when it’s eligible for gc
    private final Cleaner.Cleanable cleanable;

    public Room(int numJunkPiles) {
        state = new State(numJunkPiles);
        cleanable = cleaner.register(this, state);
    }

    @Override public void close() {
        cleanable.clean();
    }
}
```

内嵌的静态类 State 保存清除方法清除房间所需的资源。在这个例子中，就是 numJunkPiles 域，表示房间的杂乱度。更现实地说，它可以是 f inal 的 long，包含一个指向本地对等体的指针。State 实现了 Runnable 接口，它的 run 方法最多被 Cleanable 调用一次，后者是我们在 Room 构造器中用清除器注册 State 实例时获得的。以下两种情况之一会触发 run 方法的调用：通常是通过调用 Room 的 close 方法触发的，后者又调用了 Cleanable 的清除方法。如果到了 Room 实例应该被垃圾回收时，客户端还没有调用 close 方法，清除方法就会（希望如此）调用 State 的 run 方法。

关键是 State 实例没有引用它的 Room 实例。如果它引用了，会造成循环，阻止 Room 实例被垃圾回收（以及防止被自动清除）。因此 State 必须是一个静态的嵌套类，因为非静态的嵌套类包含了对其外围实例的引用（详见实验 24）。同样地，也不建议使用 lambda，因为它们很容易捕捉到对外围对象的引用。

如前所述，Room 的清除方法只用作安全网。如果客户端将所有的 Room 实例化都包在 try-with-resource 块中，将永远不会请求到自动清除。用下面这个表现良好的客户端代码示范一下：

```java
// Well-behaved client of resource with cleaner safety-net
public class Adult {
    public static void main(String[] args) {
        try (Room myRoom = new Room(7)) {
            System.out.println("Goodbye");
        }
    }
}
```

![图片描述](https://doc.shiyanlou.com/courses/3483/1533757/2f75556d5acce3d9549e92cd2bffcba8-0)

正如所期待的一样，运行 Adult 程序会打印出 Goodbye，接着是 Cleaning room。但是下面这个表现糟糕的程序又如何呢？哪一个将永远不会清除它的房间？

```java
public class Teenager {
    public static void main(String[] args) {
        new Room(99);
        System.out.println("Peace out");

    }
}
```

![图片描述](https://doc.shiyanlou.com/courses/3483/1533757/cccfb274f5d4751ba7a99a4643d6e2fa-0)

你可能期望打印出 Peace out，然后是 Cleaning room，但是程序并没有打印出 Cleaning room，就退出程序了。这就是我们之前提到过的不可预见性。Cleaner 规范指出：“清除方法在 System.exit 期间的行为是与实现相关的。不确保清除动作是否会被调用。”虽然规范没有指明，其实对于正常的程序退出也是如此。在我的机器上，只要在 Teenager 的 main 方法上添加代码行 System.gc()，就足以让它在退出之前打印出 Cleaning room，但是不能保证在你的机器上也能看到相同的行为。

实验总结



在本次实验中将详细分析使用终结方法和清除方法的优势与劣势，并通过一个案例介绍了使用清除方法的技巧。但是除非是作为安全网，或者是为了终止非关键的本地资源，否则请不要使用清除方法，对于在 Java 9 之前的发行版本，则尽量不要使用终结方法。若使用了终结方法或者清除方法，则要注意它的不确定性和性能后果。



# 实验9	try-with-resources 优先于 try-finally

在本次实验中将介绍两种关闭资源的方法，try-with-resources 和 try-finally，并对比这两种方式的性能。

#### 知识点

- try-with-resources 和 try-finally

try-with-resources 和 try-finally



Java 类库中包括许多必须通过调用 close 方法来手工关闭的资源。例如 InputStream、OutputStream 和 java.sql.Connection。客户端经常会忽略资源的关闭，造成严重的性能后果也就可想而知了。虽然这其中的许多资源都是用终结方法作为安全网，但是效果并不理想（详见实验 8）。

根据经验，try-finally 语句是确保资源会被适时关闭的最佳方法，就算发生异常或者返回也一样：

```java
// try-finally - No longer the best way to close resources!
    static String firstLineOfFile(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        try {
            return br.readLine();
        } finally {
            br.close();
        }
    }
```

这看起来好像也不算太坏，但是如果再添加第二个资源，就会一团糟了：

```java
// try-finally is ugly when used with more than one resource!
    static void copy(String src, String dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                byte[] buf = new byte[BUFFER_SIZE];
                int n;
                while ((n = in.read(buf)) >= 0)
                    out.write(buf, 0, n);
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }
```

这可能令人有点难以置信，不过就算优秀的程序员也会经常犯这样的错误。在 2007 年，close 方法在 Java 类库中有 2/3 都用错了。即便用 try-finally 语句正确地关闭了资源，如前两段代码范例所示，它也存在着些许不足。因为在 try 块和 finally 块中的代码，都会抛出异常。例如，在 firstLineOfFile 方法中，如果底层的物理设备异常，那么调用 readLine 就会抛出异常，基于同样的原因，调用 close 也会出现异常。在这种情况下，第二个异常完全抹除了第一个异常。在异常堆栈轨迹中，完全没有关于第一个异常的记录，这在现实的系统中会导致调试变得非常复杂，因为通常需要看到第一个异常才能诊断出问题何在。虽然可以通过编写代码来禁止第二个异常，保留第一个异常，但事实上没有人会这么做，因为实现起来太烦琐了。

当 Java 7 引入 try-with-resources 语句时，所有这些问题一下子就全部解决了。要使用这个构造的资源，必须先实现 AutoCloseable 接口，其中包含了单个返回 void 的 close 方法。Java 类库与第三方类库中的许多类和接口，现在都实现或扩展了 AutoCloseable 接口。如果编写了一个类，它代表的是必须被关闭的资源，那么这个类也应该实现 AutoCloseable。

以下就是使用 try-with-resources 的第一个范例：

```java
// try-with-resources - the the best way to close resources!
    static String firstLineOfFile(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new FileReader(path))) {
            return br.readLine();
        }
    }
```

以下是使用 try-with-resources 的第二个范例：

```java
// try-with-resources on multiple resources - short and sweet
    static void copy(String src, String dst) throws IOException {
        try (InputStream   in = new FileInputStream(src);
             OutputStream out = new FileOutputStream(dst)) {
            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while ((n = in.read(buf)) >= 0)
                out.write(buf, 0, n);
        }
    }
```

使用 try-with-resources 不仅使代码变得更简洁易懂，也更容易进行诊断。以 firstLineOfFile 方法为例，如果调用 readLine 和（不可见的）close 方法都抛出异常，后一个异常就会被禁止，以保留第一个异常。事实上，为了保留你想要看到的那个异常，即便多个异常都可以被禁止。这些被禁止的异常并不是简单地被抛弃了，而是会被打印在堆栈轨迹中，并注明它们是被禁止的异常。通过编程调用 getSuppressed 方法还可以访问到它们，getSuppressed 方法也已经添加在 Java 7 的 Throwable 中了。

在 try-with-resources 语句中还可以使用 catch 子句，就像在平时的 try-finally 语句中一样。这样既可以处理异常，又不需要再套用一层代码。下面举一个稍费了点心思的范例，这个 firstLineOfFile 方法没有抛出异常，但是如果它无法打开文件，或者无法从中读取，就会返回一个默认值：

```java
 // try-with-resources with a catch clause
    static String firstLineOfFile(String path, String defaultVal) {
        try (BufferedReader br = new BufferedReader(
                new FileReader(path))) {
            return br.readLine();
        } catch (IOException e) {
            return defaultVal;
        }
    }
```

结论很明显：在处理必须关闭的资源时，始终要优先考虑用 try-with-resources，而不是用 try-finally。这样得到的代码将更加简洁、清晰，产生的异常也更有价值。有了 try-with-resources 语句，在使用必须关闭的资源时，就能更轻松地正确编写代码了。实践证明，这个用 try-finally 是不可能做到的。



实验总结



本次实验介绍了使用 try-with-resources 和 try-finally 两种关闭资源的方法，通过对比其性能，应优先选择 try-with-resources。



# 第二章 对于所有对象都通用的方法

# 实验10	覆盖 equals 时请遵守通用约定

实验介绍



本次实验将详细介绍我们在覆盖 equals 方法时必须要遵守自反性、对称性、一致性、传递性等通用的规定，以及我们使用 equals 方法时的一些技巧和告诫。

#### 知识点

- 覆盖 equals 方法的通用约定
- 使用 equals 方法的诀窍

简介



覆盖 equals 方法看起来似乎很简单，但是有许多覆盖方式会导致错误，并且后果非常严重。最容易避免这类问题的办法就是不覆盖 equals 方法，在这种情况下，类的每个实例都只与它自身相等。如果满足了以下任何一个条件，这就正是所期望的结果：

- 类的每个实例本质上都是唯一的。对于代表活动实体而不是值（value）的类来说确实如此，例如 Thread。Object 提供的 equals 实现对于这些类来说正是正确的行为。

- 类没有必要提供“逻辑相等”（logical equality）的测试功能。例如，java.util.regex.Pattern 可以覆盖 equals，以检查两个 Pattern 实例是否代表同一个正则表达式，但是设计者并不认为客户需要或者期望这样的功能。在这类情况之下，从 Object 继承得到的 equals 实现已经足够了。

- 超类已经覆盖了 equals，超类的行为对于这个类也是合适的。例如，大多数的 Set 实现都从 AbstractSet 继承 equals 实现，List 实现从 AbstractList 继承 equals 实现，Map 实现从 AbstractMap 继承 equals 实现。

- 类是私有的，或者是包级私有的，可以确定它的 equals 方法永远不会被调用。如果你非常想要规避风险，可以覆盖 equals 方法，以确保它不会被意外调用：

  ```java
  @Override public boolean equals(Object o){
      throw new AssertionError();//Method is never called
  }
  ```

那么，什么时候应该覆盖 equals 方法呢？如果类具有自己特有的“逻辑相等”（logical equality）概念（不同于对象等同的概念），而且超类还没有覆盖 equals。这通常属于“值类”（value class）的情形。值类仅仅是一个表示值的类，例如 Integer 或者 String。程序员在利用 equals 方法来比较值对象的引用时，希望知道它们在逻辑上是否相等，而不是想了解它们是否指向同一个对象。为了满足程序员的要求，不仅必须覆盖 equals 方法，而且这样做也使得这个类的实例可以被用作映射表（map）的键（key），或者集合（set）的元素，使映射或者集合表现出预期的行为。

有一种“值类”不需要覆盖 equals 方法，即用实例受控（详见实验 1）确保“每个值至多只存在一个对象”的类。枚举类型（详见实验 34）就属于这种类。对于这样的类而言，逻辑相同与对象等同是一回事，因此 Object 的 equals 方法等同于逻辑意义上的 equals 方法。



覆盖 equals 方法的通用约定



在覆盖 equals 方法的时候，必须要遵守它的通用约定。下面是约定的内容，来自 Object 的规范。

equals 方法实现了等价关系（equivalence relation），其属性如下：

- 自反性（ref lexive）：对于任何非 null 的引用值 x，x.equals(x) 必须返回 true。
- 对称性（symmetric）：对于任何非 null 的引用值 x 和 y，当且仅当 y.equals(x) 返回 true 时，x.equals(y) 必须返回 true。
- 传递性（transitive）：对于任何非 null 的引用值 x、y 和 z，如果 x.equals(y) 返回 true，并且 y.equals(z) 也返回 true，那么 x.equals(z) 也必须返回 true。
- 一致性（consistent）：对于任何非 null 的引用值 x 和 y，只要 equals 的比较操作在对象中所用的信息没有被修改，多次调用 x.equals(y) 就会一致地返回 true，或者一致地返回 false。
- 对于任何非 null 的引用值 x，x.equals(null) 必须返回 false。

除非你对数学特别感兴趣，否则这些规定看起来可能有点让人感到恐惧，但是绝对不要忽视这些规定！如果违反了，就会发现程序将会表现得不正常，甚至崩溃，而且很难找到失败的根源。用 John Donne 的话说，没有哪个类是孤立的。一个类的实例通常会被频繁地传递给另一个类的实例。有许多类，包括所有的集合类（collection class）在内，都依赖于传递给它们的对象是否遵守了 equals 约定。

现在你已经知道了违反 equals 约定有多么可怕，下面将更细致地讨论这些约定。值得欣慰的是，这些约定虽然看起来很吓人，实际上并不十分复杂。一旦理解了这些约定，要遵守它们并不困难。

那么什么是等价关系呢？不严格地说，它是一个操作符，将一组元素划分到其元素与另一个元素等价的分组中。这些分组被称作等价类（equivalence class）。从用户的角度来看，对于有用的 equals 方法，每个等价类中的所有元素都必须是可交换的。现在我们按照顺序逐一查看以下 5 个要求。



自反性（Ref lexivity）



第一个要求仅仅说明对象必须等于其自身。很难想象会无意识地违反这一条。假如违背了这一条，然后把该类的实例添加到集合中，该集合的 contains 方法将果断地告诉你，该集合不包含你刚刚添加的实例。



对称性（Symmetry）



第二个要求是说，任何两个对象对于“它们是否相等”的问题都必须保持一致。与第一个要求不同，若无意中违反这一条，这种情形倒是不难想象。例如下面的类，它实现了一个不区分大小写的字符串。字符串由 toString 保存，但在 equals 操作中被忽略。

```java
// Broken - violates symmetry!
public final class CaseInsensitiveString {
    private final String s;

    public CaseInsensitiveString(String s) {
        this.s = Objects.requireNonNull(s);
    }

    // Broken - violates symmetry!
    @Override public boolean equals(Object o) {
        if (o instanceof CaseInsensitiveString)
            return s.equalsIgnoreCase(
                    ((CaseInsensitiveString) o).s);
        if (o instanceof String)  // One-way interoperability!
            return s.equalsIgnoreCase((String) o);
        return false;
    }
    ... //Remainder omitted
}
```

在这个类中，equals 方法的意图非常好，它企图与普通的字符串对象进行互操作。假设我们有一个不区分大小写的字符串和一个普通的字符串：

```java
CaseInsensitiveString cis=new CaseInsensitiveString("Polish");
String s="polish";
```

不出所料，cis.equals(s) 返回 true。问题在于，虽然 CaseInsensitiveString 类中的 equals 方法知道普通的字符串对象，但是，String 类中的 equals 方法却并不知道不区分大小写的字符串。因此，s.equals(cis) 返回 false，显然违反了对称性。假设你把不区分大小写的字符串对象放到一个集合中：

```java
List<CaseInsensitiveString> list=new ArrayList<>();
list .add(cis);
```

此时 list.contains(s) 会返回什么结果呢？没人知道。在当前的 OpenJDK 实现中，它碰巧返回 false，但这只是这个特定实现得出的结果而已。在其他的实现中，它有可能返回 true，或者抛出一个运行时异常。一旦违反了 equals 约定，当其他对象面对你的对象时，你完全不知道这些对象的行为会怎么样。

为了解决这个问题，只需把企图与 String 互操作的这段代码从 equals 方法中去掉就可以了。这样做之后，就可以重构该方法，使它变成一条单独的返回语句：

```java
@Override public boolean equals(Object o){
    return o instanceof CaseInsensitiveString&&
          ((CaseInsensitiveString) o).s.equalsIgnoreCase(s);
}
```



传递性（Transitivity）



equals 约定的第三个要求是，如果一个对象等于第二个对象，而第二个对象又等于第三个对象，则第一个对象一定等于第三个对象。同样地，无意识地违反这条规则的情形也不难想象。用子类举个例子。假设它将一个新的值组件（value component）添加到了超类中。换句话说，子类增加的信息会影响 equals 的比较结果。我们首先以一个简单的不可变的二维整数型 `Point` 类作为开始：

```java
// Simple immutable two-dimensional integer point class
public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override public boolean equals(Object o) {
        if (!(o instanceof Point))
            return false;
        Point p = (Point)o;
        return p.x == x && p.y == y;
    }
    ... //Remainder omitted
}
```

假设你想要扩展这个类，为一个点添加颜色信息：

```java
public class ColorPoint extends Point {
    private final Color color;

    public ColorPoint(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }
    ... //Remainder omitted
}
```

equals 方法会是什么样的呢？如果完全不提供 equals 方法，而是直接从 Point 继承过来，在 equals 做比较的时候颜色信息就被忽略掉了。虽然这样做不会违反 equals 约定，但很明显这是无法接受的。假设编写了一个 equals 方法，只有当它的参数是另一个有色点，并且具有同样的位置和颜色时，它才会返回 true：

```java
//Broken-violates symmetry!
@Override public boolean equals(Object o){
      if(!(o instanceof ColorPoint))
        return false;
    return super .equals(o)&&((ColorPoint) o).color==color;
}
```

这个方法的问题在于，在比较普通点和有色点，以及相反的情形时，可能会得到不同的结果。前一种比较忽略了颜色信息，而后一种比较则总是返回 false，因为参数的类型不正确。为了直观地说明问题所在，我们创建一个普通点和一个有色点：

```java
Point p=new Point(1，2);
ColorPoint cp =new ColorPoint(1，2，Color.RED);
```

然后，`p.equals(cp)` 返回 true，`cp.equals(p)` 则返回 false。你可以做这样的尝试来修正这个问题，让 ColorPoint.equals 在进行“混合比较”时忽略颜色信息：

```java
//Broken-violates transitivity!
@Override public boolean equals(Object o){
    if(!(o instanceof Point))
          return false;
//If o is a normal Point，do a color-blind comparison
if(!(o instanceof ColorPoint))
    return o.equals(this);
//o is a ColorPoint;do a full comparison
return super.equals(o)&&((ColorPoint) o).color==color;
}
```

这种方法确实提供了对称性，但是却牺牲了传递性：

```java
ColorPoint p1= new ColorPoint(1，2，Color.RED);
Point p2=new Point(1，2);
ColorPoint p3=new ColorPoint(1，2，Color.BLUE);
```

此时，p1.equals(p2) 和 p2.equals(p3) 都返回 true，但是 p1.equals(p3) 则返回 false，很显然这违反了传递性。前两种比较不考虑颜色信息（“色盲”），而第三种比较则考虑了颜色信息。

此外，这种方法还可能导致无限递归问题：假设 Point 有两个子类，如 ColorPoint 和 SmellPoint，它们各自都带有这种 equals 方法。那么对 myColorPoint.equals(mySmellPoint) 的调用将会抛出 StackOverflowError 异常。

那该怎么解决呢？事实上，这是面向对象语言中关于等价关系的一个基本问题。我们无法在扩展可实例化的类的同时，既增加新的值组件，同时又保留 equals 约定，除非愿意放弃面向对象的抽象所带来的优势。

你可能听说过，在 equals 方法中用 getClass 测试代替 instanceof 测试，可以扩展可实例化的类和增加新的值组件，同时保留 equals 约定：

```java
//Broken一violates Liskov substitution principle
@Override public boolean equals(Object o){
    if (o== null || o .getClass()!= getClass())
          return false;
    Point p=(Point) o;
    return p.x==x&&p.Y==Y;
}
```

这段程序只有当对象具有相同的实现类时，才能使对象等同。虽然这样也不算太糟糕，但结果却是无法接受的：Point 子类的实例仍然是一个 Point，它仍然需要发挥作用，但是如果采用了这种方法，它就无法完成任务！假设我们要编写一个方法，以检验某个点是否处在单位圆中。下面是可以采用的其中一种方法：

```java
//Initialize unitCircle to contain all Points on the unit circle
private static final Set<Point> unitCircle=Set.of(
          new Point(1,0),new Point(0,1)，
          new Point(-1,0),new Point( 0,-1) );

public static boolean onUnitCircle(Point p){
    return unitCircle.contains(p);
}
```

虽然这可能不是实现这种功能的最快方式，不过它的效果很好。但是假设你通过某种不添加值组件的方式扩展了 Point，例如让它的构造器记录创建了多少个实例：

```java
// Trivial subclass of Point - doesn't add a value component
public class CounterPoint extends Point {
    private static final AtomicInteger counter =
            new AtomicInteger();

    public CounterPoint(int x, int y) {
        super(x, y);
        counter.incrementAndGet();
    }
    public static int numberCreated() { return counter.get(); }
}
```

里氏替换原则（Liskov substitution principle）认为，一个类型的任何重要属性也将适用于它的子类型，因此为该类型编写的任何方法，在它的子类型上也应该同样运行得很好。针对上述 Point 的子类（如 CounterPoint）仍然是 Point，并且必须发挥作用的例子，这个就是它的正式语句。但是假设我们将 CounterPoint 实例传给了 onUnitCircle 方法。如果 Point 类使用了基于 getClass 的 equals 方法，无论 CounterPoint 实例的 x 和 y 值是什么，onUnitCircle 方法都会返回 false。这是因为像 onUnitCircle 方法所用的 HashSet 这样的集合，利用 equals 方法检验包含条件，没有任何 CounterPoint 实例与任何 Point 对应。但是，如果在 Point 上使用适当的基于 instanceof 的 equals 方法，当遇到 CounterPoint 时，相同的 onUnitCircle 方法就会工作得很好。

虽然没有一种令人满意的办法可以既扩展不可实例化的类，又增加值组件，但还是有一种不错的权宜之计：遵从第 18 条“复合优先于继承”的建议。我们不再让 ColorPoint 扩展 Point，而是在 ColorPoint 中加入一个私有的 Point 域，以及一个公有的视图（view）方法（详见实验 6），此方法返回一个与该有色点处在相同位置的普通 Point 对象：

```java
// Adds a value component without violating the equals contract
public class ColorPoint {
    private final Point point;
    private final Color color;

    public ColorPoint(int x, int y, Color color) {
        point = new Point(x, y);
        this.color = Objects.requireNonNull(color);
    }

    /**
     * Returns the point-view of this color point.
     */
    public Point asPoint() {
        return point;
    }

    @Override public boolean equals(Object o) {
        if (!(o instanceof ColorPoint))
            return false;
        ColorPoint cp = (ColorPoint) o;
        return cp.point.equals(point) && cp.color.equals(color);
    }

    ... //Remaineder omitted
}
```

在 Java 平台类库中，有一些类扩展了可实例化的类，并添加了新的值组件。例如，java.sql.Timestamp 对 java.util.Date 进行了扩展，并增加了 nanoseconds 域。Timestamp 的 equals 实现确实违反了对称性，如果 Timestamp 和 Date 对象用于同一个集合中，或者以其他方式被混合在一起，则会引起不正确的行为。Timestamp 类有一个免责声明，告诫程序 员不要混合使用 Date 和 Timestamp 对象。只要你不把它们混合在一起，就不会有麻烦，除此之外没有其他的措施可以防止你这么做，而且结果导致的错误将很难调试。Timestamp 类的这种行为是个错误，不值得仿效。

注意，你可以在一个抽象（abstract）类的子类中增加新的值组件且不违反 equals 约定。对于根据第 23 条的建议而得到的那种类层次结构来说，这一点非常重要。例如，你可能有一个抽象的 Shape 类，它没有任何值组件，Circle 子类添加了一个 radius 域，Rectangle 子类添加了 length 和 width 域。只要不可能直接创建超类的实例，前面所述的种种问题就都不会发生。



一致性（Consistency）



equals 约定的第四个要求是，如果两个对象相等，它们就必须始终保持相等，除非它们中有一个对象（或者两个都）被修改了。换句话说，可变对象在不同的时候可以与不同的对象相等，而不可变对象则不会这样。当你在写一个类的时候，应该仔细考虑它是否应该是不可变的（详见实验 17）。如果认为它应该是不可变的，就必须保证 equals 方法满足这样的限制条件：相等的对象永远相等，不相等的对象永远不相等。

无论类是否是不可变的，都不要使 equals 方法依赖于不可靠的资源。如果违反了这条禁令，要想满足一致性的要求就十分困难了。例如，java.net.URL 的 equals 方法依赖于对 URL 中主机 IP 地址的比较。将一个主机名转变成 IP 地址可能需要访问网络，随着时间的推移，就不能确保会产生相同的结果，即有可能 IP 地址发生了改变。这样会导致 URL equals 方法违反 equals 约定，在实践中有可能引发一些问题。URL equals 方法的行为是一个大错误并且不应被模仿。遗憾的是，因为兼容性的要求，这一行为无法被改变。为了避免发生这种问题，equals 方法应该对驻留在内存中的对象执行确定性的计算。



非空性（Non-nullity）



最后一个要求没有正式名称，我姑且称它为“非空性”，意思是指所有的对象都不能等于 null。尽管很难想象在什么情况下 o.equals（null）调用会意外地返回 true，但是意外抛出 NullPointerException 异常的情形却不难想象。通用约定不允许抛出 NullPointerException 异常。许多类的 equals 方法都通过一个显式的 null 测试来防止这种情况：

```java
@Override public boolean equals(Object o){
    if(o==null)
    return false;
    ...
}
```

这项测试是不必要的。为了测试其参数的等同性，equals 方法必须先把参数转换成适当的类型，以便可以调用它的访问方法，或者访问它的域。在进行转换之前，equals 方法必须使用 instanceof 操作符，检查其参数的类型是否正确：

```java
@Override public boolean equals(Object o){
    if(!(o instanceof MyType))
        return false;
    MyType mt = (MyType) o ;
    ...
}
```

如果漏掉了这一步的类型检查，并且传递给 equals 方法的参数又是错误的类型，那么 equals 方法将会抛出 ClassCastException 异常，这就违反了 equals 约定。但是，如果 instanceof 的第一个操作数为 null，那么，不管第二个操作数是哪种类型，instanceof 操作符都指定应该返回 false。因此，如果把 null 传给 equals 方法，类型检查就会返回 false，所以不需要显式的 null 检查。



使用 equals 方法的诀窍



结合所有这些要求，得出了以下实现高质量 equals 方法的诀窍：

1. 使用 == 操作符检查“参数是否为这个对象的引用”。如果是，则返回 true。这只不过是一种性能优化，如果比较操作有可能很昂贵，就值得这么做。
2. 使用 instanceof 操作符检查“参数是否为正确的类型”。如果不是，则返回 false。一般说来，所谓“正确的类型”是指 equals 方法所在的那个类。某些情况下，是指该类所实现的某个接口。如果类实现的接口改进了 equals 约定，允许在实现了该接口的类之间进行比较，那么就使用接口。集合接口如 Set、List、Map 和 Map.Entry 具有这样的特性。
3. 把参数转换成正确的类型。因为转换之前进行过 instanceof 测试，所以确保会成功。
4. 对于该类中的每个“关键”（significant）域，检查参数中的域是否与该对象中对应的域相匹配。如果这些测试全部成功，则返回 true ；否则返回 false。如果第 2 步中的类型是个接口，就必须通过接口方法访问参数中的域；如果该类型是个类，也许就能够直接访问参数中的域，这要取决于它们的可访问性。

对于既不是 float 也不是 double 类型的基本类型域，可以使用 == 操作符进行比较；对于对象引用域，可以递归地调用 equals 方法；对于 float 域，可以使用静态 Float.compare（float,float）方法；对于 double 域，则使用 Double.compare（double,double）。对 float 和 double 域进行特殊的处理是有必要的，因为存在着 Float.NaN、-0.0f 以及类似的 double 常量；详细信息请参考 JLS 15.21.1 或者 Float.equals 的文档。虽然可以用静态方法 Float.equals 和 Double.equals 对 float 和 double 域进行比较，但是每次比较都要进行自动装箱，这会导致性能下降。对于数组域，则要把以上这些指导原则应用到每一 个元素上。如果数组域中的每个元素都很重要，就可以使用其中一个 Arrays.equals 方法。

有些对象引用域包含 null 可能是合法的，所以，为了避免可能导致 NullPointerException 异常，则使用静态方法 Objects.equals(Object,Object) 来检查这类域的等同性。

对于有些类，比如前面提到的 CaseInsensitiveString 类，域的比较要比简单的等同性测试复杂得多。如果是这种情况，可能希望保存该域的一个“范式”（canonical form），这样 equals 方法就可以根据这些范式进行低开销的精确比较，而不是高开销的非精确比较。这种方法对于不可变类（详见实验 17）是最为合适的；如果对象可能发生变化，就必须使其范式保持最新。

域的比较顺序可能会影响 equals 方法的性能。为了获得最佳的性能，应该最先比较最有可能不一致的域，或者是开销最低的域，最理想的情况是两个条件同时满足的域。不应该比较那些不属于对象逻辑状态的域，例如用于同步操作的 Lock 域。也不需要比较衍生域（derived f ield），因为这些域可以由“关键域”（signif icant f ield）计算获得，但是这样做有可能提高 equals 方法的性能。如果衍生域代表了整个对象的综合描述，比较这个域可以节省在比较失败时去比较实际数据所需要的开销。例如，假设有一个 Polygon 类，并缓存了该面积。如果两个多边形有着不同的面积，就没有必要去比较它们的边和顶点。

在编写完 equals 方法之后，应该问自己三个问题：它是否是对称的、传递的、一致的？并且不要只是自问，还要编写单元测试来检验这些特性，除非用 AutoValue（后面会讲到）生成 equals 方法，在这种情况下就可以放心地省略测试。如果答案是否定的，就要找出原因，再相应地修改 equals 方法的代码。当然，equals 方法也必须满足其他两个特性（自反性和非空性），但是这两种特性通常会自动满足。根据上面的诀窍构建 equals 方法的具体例子，请看下面这个简单的 PhoneNumber 类：

```java
// Class with a typical equals method
public final class PhoneNumber {
    private final short areaCode, prefix, lineNum;

    public PhoneNumber(int areaCode, int prefix, int lineNum) {
        this.areaCode = rangeCheck(areaCode, 999, "area code");
        this.prefix   = rangeCheck(prefix,   999, "prefix");
        this.lineNum  = rangeCheck(lineNum, 9999, "line num");
    }

    private static short rangeCheck(int val, int max, String arg) {
        if (val < 0 || val > max)
            throw new IllegalArgumentException(arg + ": " + val);
        return (short) val;
    }

    @Override public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PhoneNumber))
            return false;
        PhoneNumber pn = (PhoneNumber)o;
        return pn.lineNum == lineNum && pn.prefix == prefix
                && pn.areaCode == areaCode;
    }

    ... // Remainder omitted - note that hashCode is REQUIRED (Item 11)!
}
```

下面是最后的一些告诫：

- 覆盖 equals 时总要覆盖 hashCode（详见实验 11）。
- 不要企图让 equals 方法过于智能。如果只是简单地测试域中的值是否相等，则不难做到遵守 equals 约定。如果想过度地去寻求各种等价关系，则很容易陷入麻烦之中。把任何一种别名形式考虑到等价的范围内，往往不会是个好主意。例如，File 类不应该试图把指向同一个文件的符号链接（symbolic link）当作相等的对象来看待。所幸 File 类没有这样做。
- 不要将 equals 声明中的 Object 对象替换为其他的类型。程序员编写出下面这样的 equals 方法并不鲜见，这会使程序员花上数个小时都搞不清为什么它不能正常工作：

```java
//Broken - paramater type must be Object!
public boolean equals(MyClass o){
    ...
}
```

问题在于，这个方法并没有覆盖（override）Object.equals，因为它的参数应该是 Object 类型，相反，它重载（overload）了 Object.equals（详见实验 52）。在正常 equals 方法的基础上，再提供一个“强类型”（strongly typed）的 equals 方法，这是无法接受的，因为会导致子类中的 Override 注解产生错误的正值，带来错误的安全感。

@Override 注解的用法一致，就如本条目中所示，可以防止犯这种错误（详见实验 40）。这个 equals 方法不能编译，错误消息会告诉你到底哪里出了问题：

```java
// Still broken,but won't compile
@Override public boolean equals(MyClass o){
    ...
}
```

编写和测试 equals（及 hashCode）方法都是十分烦琐的，得到的代码也很琐碎。代替手工编写和测试这些方法的最佳途径，是使用 Google 开源的 AutoValue 框架，它会自动替你生成这些方法，通过类中的单个注解就能触发。在大多数情况下，AutoValue 生成的方法本质上与你亲自编写的方法是一样的。

IDE 也有工具可以生成 equals 和 hashCode 方法，但得到的源代码比使用 Auto-Value 的更加冗长，可读性也更差，它无法自动追踪类中的变化，因此需要进行测试。也就是说，让 IDE 生成 equals（及 hashCode）方法，通常优于手工实现它们，因为 IDE 不会犯粗心的错误，但是程序员会犯错。



实验总结



总而言之，不要轻易覆盖 equals 方法，除非迫不得已。因为在许多情况下，从 Object 处继承的实现正是你想要的。如果覆盖 equals，一定要比较这个类的所有关键域，并且查看它们是否遵守 equals 合约的所有五个条款。



# 实验11 	覆盖 equals 时总要覆盖 hashCode



实验介绍



本次实验将介绍 hashCode 的通用约定并通过计算散列码来具体阐述某些约定。

#### 知识点

- hashCode 的通用约定
- 散列码

hashCode 的通用约定



在每个覆盖了 equals 方法的类中，都必须覆盖 hashCode 方法。如果不这样做的话，就会违反 hashCode 的通用约定，从而导致该类无法结合所有基于散列的集合一起正常运作，这类集合包括 HashMap 和 HashSet。下面是约定的内容，摘自 Object 规范：

- 在应用程序的执行期间，只要对象的 equals 方法的比较操作所用到的信息没有被修改，那么对同一个对象的多次调用，hashCode 方法都必须始终返回同一个值。在一个应用程序与另一个程序的执行过程中，执行 hashCode 方法所返回的值可以不一致。
- 如果两个对象根据 equals(Object) 方法比较是相等的，那么调用这两个对象中的 hashCode 方法都必须产生同样的整数结果。
- 如果两个对象根据 equals(Object) 方法比较是不相等的，那么调用这两个对象中的 hashCode 方法，则不一定要求 hashCode 方法必须产生不同的结果。但是程序员应该知道，给不相等的对象产生截然不同的整数结果，有可能提高散列表（hash table）的性能。

因没有覆盖 hashCode 而违反的关键约定是第二条：相等的对象必须具有相等的散列码（hash code）。根据类的 equals 方法，两个截然不同的实例在逻辑上有可能是相等的，但是根据 Object 类的 hashCode 方法，它们仅仅是两个没有任何共同之处的对象。因此，对象的 hashCode 方法返回两个看起来是随机的整数，而不是根据第二个约定所要求的那样，返回两个相等的整数。



散列码



假设在 HashMap 中用实验 10 中出现过的 PhoneNumber 类的实例作为键：

```java
Map<PhoneNumber,String> m = new HashMap<>();
m.put(new PhoneNumber(707,867,5309),"Jenny");
```

此时，你可能期望 `m.get(new PhoneNumber(707, 867, 5309))` 会返回 "Jenny"， 但它实际上返回的是 null。注意，这里涉及两个 PhoneNumber 实例：第一个被插入 HashMap 中，第二个实例与第一个相等，用于从 Map 中根据 PhoneNumber 去获取用户名字。由于 PhoneNumber 类没有覆盖 hashCode 方法，从而导致两个相等的实例具有不相等的散列码，违反了 hashCode 的约定。因此，put 方法把电话号码对象存放在一个散列桶（hash bucket）中，get 方法却在另一个散列桶中查找这个电话号码。即使这两个实例正好被放到同一个散列桶中，get 方法也必定会返回 null，因为 HashMap 有一项优化，可以将与每个项相关联 的散列码缓存起来，如果散列码不匹配，也就不再去检验对象的等同性。

修正这个问题非常简单，只需为 PhoneNumber 类提供一个适当的 hashCode 方法即可。那么，hashCode 方法应该是什么样的呢？编写一个合法但并不好用的 hashCode 方法没有任何价值。例如，下面这个方法总是合法的，但是它永远都不应该被正式使用：

```java
//The worst possible legal hashCode implementation-never use!
@Override public int hashCode(){return 42;}
```

上面这个 hashCode 方法是合法的，因为它确保了相等的对象总是具有同样的散列码。但它也极为恶劣，因为它使得每个对象都具有同样的散列码。因此，每个对象都被映射到同一个散列桶中，使散列表退化为链表（linked list）。它使得本该线性时间运行的程序变成了以平方级时间在运行。对于规模很大的散列表而言，这会关系到散列表能否正常工作。

一个好的散列函数通常倾向于“为不相等的对象产生不相等的散列码”。这正是 hashCode 约定中第三条的含义。理想情况下，散列函数应该把集合中不相等的实例均匀地分布到所有可能的 int 值上。要想完全达到这种理想的情形是非常困难的。幸运的是，相对接近这种理想情形则并不太困难。下面给出一种简单的解决办法：

1. 声明一个 int 变量并命名为 result，将它初始化为对象中第一个关键域的散列码 c，如步骤 2.a 中计算所示（如实验 10 所述，关键域是指影响 equals 比较的域）。

2. 对象中剩下的每一个关键域 f 都完成以下步骤：

   **a.** 为该域计算 int 类型的散列码 c：

- 如果该域是基本类型，则计算 Type.hashCode(f)，这里的 Type 是装箱基本类型的类，与 f 的类型相对应。

- 如果该域是一个对象引用，并且该类的 equals 方法通过递归地调用 equals 的方式来比较这个域，则同样为这个域递归地调用 hashCode。如果需要更复杂的比较，则为这个域计算一个“范式”（canonical representation），然后针对这个范式调用 hashCode。如果这个域的值为 null，则返回 0（或者其他某个常数，但通常是 0）。

- 如果该域是一个数组，则要把每一个元素当作单独的域来处理。也就是说，递归地应用上述规则，对每个重要的元素计算一个散列码，然后根据步骤 2.b 中的做法把这些散列值组合起来。如果数组域中没有重要的元素，可以使用一个常量，但最好不要用 0。如果数组域中的所有元素都很重要，可以使用 Arrays.hashCode 方法。

  **b.** 按照下面的公式，把步骤 2.a 中计算得到的散列码 c 合并到 result 中：

  ```java
  result=31 * result + c;
  ```

1. 返回 result。 写完了 hashCode 方法之后，问问自己“相等的实例是否都具有相等的散列码”。要编写单元测试来验证你的推断（除非利用 AutoValue 生成 equals 和 hashCode 方法，这样你就可以放心地省略这些测试）。如果相等的实例有着不相等的散列码，则要找出原因，并修正错误。

在散列码的计算过程中，可以把衍生域（derived field）排除在外。换句话说，如果一个域的值可以根据参与计算的其他域值计算出来，则可以把这样的域排除在外。必须排除 equals 比较计算中没有用到的任何域，否则很有可能违反 hashCode 约定的第二条。

步骤 2.b 中的乘法部分使得散列值依赖于域的顺序，如果一个类包含多个相似的域，这样的乘法运算就会产生一个更好的散列函数。例如，如果 String 散列函数省略了这个乘法部分，那么只是字母顺序不同的所有字符串将都会有相同的散列码。之所以选择 31，是因为它是一个奇素数。如果乘数是偶数，并且乘法溢出的话，信息就会丢失，因为与 2 相乘等价于移位运算。使用素数的好处并不很明显，但是习惯上都使用素数来计算散列结果。31 有个很好的特性，即用移位和减法来代替乘法，可以得到更好的性能：31 * i == (i << 5) - i。现代的虚拟机可以自动完成这种优化。

现在我们要把上述解决办法用到 PhoneNumber 类中：

```java
//Typical hashCode method
@Override public int hashCode(){
      int result=Short.hashCode(areaCode);
      result=31*result+Short.hashCode(prefix);
      result=31*result+Short.hashCode(lineNum);
      return result;
}
```

因为这个方法返回的结果是一个简单、确定的计算结果，它的输入只是 PhoneNumber 实例中的三个关键域，因此相等的 PhoneNumber 实例显然都会有相等的散列码。实际上，对 PhoneNumber 的 hashCode 实现而言，上面这个方法是非常合理的，相当于 Java 平台类库中的实现。它的做法非常简单，也相当快捷，恰当地把不相等的电话号码分散到不同的散列桶中。

虽然本条目中前面给出的 hashCode 实现方法能够获得相当好的散列函数，但它们并不是最先进的。它们的质量堪比 Java 平台类库的值类型中提供的散列函数，这些方法对于绝大多数应用程序而言已经足够了。如果执意想让散列函数尽可能地不会造成冲突，请参阅 Guava’s com.google.common.hash.Hashing [Guava]。

Objects 类有一个静态方法，它带有任意数量的对象，并为它们返回一个散列码。这个方法名为 hash，是让你只需要编写一行代码的 hashCode 方法，与根据本条目前面介绍过的解决方案编写出来的相比，它的质量是与之相当的。遗憾的是，运行速度更慢一些，因为它们会引发数组的创建，以便传入数目可变的参数，如果参数中有基本类型，还需要装箱和拆箱。建议只将这类散列函数用于不太注重性能的情况。下面就是用这种方法为 PhoneNumber 编写的散列函数：

```java
//One-line hashCode method-mediocre performance
@Override public int hashCode(){
    return Objects.hash(lineNum，prefix，areaCode);
}
```

如果一个类是不可变的，并且计算散列码的开销也比较大，就应该考虑把散列码缓存在对象内部，而不是每次请求的时候都重新计算散列码。如果你觉得这种类型的大多数对象会被用作散列键（hash keys），就应该在创建实例的时候计算散列码。否则，可以选择“延迟初始化”（lazily initialize）散列码，即一直到 hashCode 被第一次调用的时候才初始化（见实验 83）。虽然我们的 PhoneNumber 类不值得这样处理，但是可以通过它来说明这种方法该如何实现。注意 hashCode 域的初始值（在本例中是 0）一般不能成为创建的实例的散列码：

```java
//hashCode method with lazily initialized cached hash code
private int hashCode;//Automatically initialized to 0
@Override public int hashCode(){
    int result=hashCode;
    if(result==0){
          result=Short.hashCode(areaCode);
          result=31*result+Short.hashCode(prefix);
          result=31*result+Short.hashCode(lineNum);
          hashCode=result;
      }
      return result;
}
```

不要试图从散列码计算中排除掉一个对象的关键域来提高性能。虽然这样得到的散列函数运行起来可能更快，但是它的效果不见得会好，可能会导致散列表慢到根本无法使用。特别是在实践中，散列函数可能面临大量的实例，在你选择忽略的区域之中，这些实例仍然区别非常大。如果是这样，散列函数就会把所有这些实例映射到极少数的散列码上，原本应该以线性级时间运行的程序，将会以平方级的时间运行。

这不只是一个理论问题。在 Java 2 发行版本之前，一个 String 散列函数最多只能使用 16 个字符，若长度少于 16 个字符就计算所有的字符，否则就从第一个字符开始，在整个字符串中间隔均匀地选取样本进行计算。对于像 URL 这种层次状名称的大型集合，该散列函数正好表现出了这里所提到的病态行为。

不要对 hashCode 方法的返回值做出具体的规定，因此客户端无法理所当然地依赖它；这样可以为修改提供灵活性。Java 类库中的许多类，比如 String 和 Integer，都可以把它们的 hashCode 方法返回的确切值规定为该实例值的一个函数。一般来说，这并不是个好主意，因为这样做严格地限制了在未来的版本中改进散列函数的能力。如果没有规定散列函数的细 节，那么当你发现了它的内部缺陷时，或者发现了更好的散列函数时，就可以在后面的发行版本中修正它。



实验总结



总而言之，每当覆盖 equals 方法时都必须覆盖 hashCode，否则程序将无法正确运行。hashCode 方法必须遵守 Object 规定的通用约定，并且必须完成一定的工作，将不相等的散列码分配给不相等的实例。这个很容易实现，但是如果不想那么费力，也可以使用前文建议的解决方法。如实验 10 所述，AutoValue 框架提供了很好的替代方法，可以不必手工编写 equals 和 hashCode 方法，并且现在的集成开发环境 IDE 也提供了类似的部分功能。



# 实验12	始终要覆盖 toString

实验介绍



本次实验将介绍 toString 的通用约定，以及覆盖 toString 方法的优势和注意点。

#### 知识点

- toString 的通用约定
- toString 方法返回格式

toString



虽然 Object 提供了 toString 方法的一个实现，但它返回的字符串通常并不是类的用户所期望看到的。它包含类的名称，以及一个“@”符号，接着是散列码的无符号十六进制表示法，例如 PhoneNumber@163b91。toString 的通用约定指出，被返回的字符串应该是一个“简洁的但信息丰富，并且易于阅读的表达形式”。尽管有人认为 PhoneNumber@163b91 算得上是简洁和易于阅读了，但是与 707-867-5309 比较起来，它还算不上是信息丰富的。toString 约定进一步指出，“建议所有的子类都覆盖这个方法。”这是一个很好的建议，真的！

遵守 toString 约定并不像遵守 equals 和 hashCode 的约定那么重要，但是，提供好的 toString 实现可以使类用起来更加舒适，使用了这个类的系统也更易于调试。当对象被传递给 println、printf、字符串联操作符（+）以及 assert，或者被调试器打印出来时，toString 方法会被自动调用。即使你永远不调用对象的 toString 方法，但是其他人也许可能需要。例如，带有对象引用的一个组件，在它记录的错误消息中，可能包含该对象的字符串表示法。如果你没有覆盖 toString，这条消息可能就毫无用处。

如果为 PhoneNumber 提供了好的 toString 方法，那么要产生有用的诊断消息会非常容易：

```java
System.out.print("Failed to connect to " + phoneNumber);
```

不管是否覆盖了 toString 方法，程序员都将以这种方式来产生诊断消息，但是如果没有覆盖 toString 方法，产生的消息将难以理解。提供好的 toString 方法，不仅有益于这个类的实例，同样也有益于那些包含这些实例的引用的对象，特别是集合对象。打印 Map 时会看到消息 {Jenny = PhoneNumber@163b91} 或 {Jenny = 707-867-5309}，你更愿意看到哪 一个？

在实际应用中，toString 方法应该返回对象中包含的所有值得关注的信息，例如上述电话号码例子那样。如果对象太大，或者对象中包含的状态信息难以用字符串来表达，这样做就有点不切实际。在这种情况下 toString 应该返回一个摘要信息，例如“ Manhattan residential phone directory (1487536 listings)”或者“ Thread[main, 5, main]”。理想情况下，字符串应该是自描述的（self-explanatory）。（Thread 例子不满足这样的要求。）如果对象的字符串表示法中没有包含对象的所有必要信息，测试失败时得到的报告将会像下面这样：

```java
Assertion failure:expected {abc，123}，but was {abc，123}.
```

在实现 toString 的时候，必须要做出一个很重要的决定：是否在文档中指定返回值的格式。对于值类（value class），比如电话号码类、矩阵类，建议这么做。指定格式的好处是，它可以被用作一种标准的、明确的、适合人阅读的对象表示法。这种表示法可以用于输入和输出，以及用在永久适合人类阅读的数据对象中，例如 CSV 文档。如果你指定了格式，通常最好再提供一个相匹配的静态工厂或者构造器，以便程序员可以很容易地在对象及其字符串表示法之间来回转换。Java 平台类库中的许多值类都采用了这种做法，包括 BigInteger、BigDecimal 和绝大多数的基本类型包装类（boxed primitive class）。

指定 toString 返回值的格式也有不足之处：如果这个类已经被广泛使用，一旦指定格式，就必须始终如一地坚持这种格式。程序员将会编写出相应的代码来解析这种字符串表示法、产生字符串表示法，以及把字符串表示法嵌入持久的数据中。如果将来的发行版本中改变了这种表示法，就会破坏他们的代码和数据，他们当然会抱怨。如果不指定格式，就可以保留灵活性，便于在将来的发行版本中增加信息，或者改进格式。

无论是否决定指定格式，都应该在文档中明确地表明你的意图。如果要指定格式，则应该严格地这样去做。例如，下面是实验 11 中 `PhoneNumber` 类的 `toString` 方法：

```java
/**
*Returns the string representation of this phone number.
*The string consists of twelve characters whose format is
*"XXX-YYY-ZZZZ"，where XXX is the area code，YYY is the
*prefix，and ZZZZ is the line number.Each of the capital
*letters represents a single decimal digit.

*If any of the three parts of this phone number is too small
*to fill up its field，the field is padded with leading zeros.
*For example，if the value of the line number is 123，the last
*four characters of the string representation will be "0123”.
*/
@Override public String toString(){
    return String.format("%03d-%03d-%04d",
              areaCode，prefix，lineNum);
}
```

对于那些依赖于格式的细节进行编程或者产生永久数据的程序员，在读到这段注释之后，一旦格式被改变，则只能自己承担后果。

无论是否指定格式，都为 toString 返回值中包含的所有信息提供一种可以通过编程访问的途径。例如，PhoneNumber 类应该包含针对 area code、prefix 和 line number 的访问方法。如果不这么做，就会迫使需要这些信息的程序员不得不自己去解析这些字符串。除了降低了程序的性能，使得程序员们去做这些不必要的工作之外，这个解析过程也很容易出错，会导致系统不稳定，如果格式发生变化，还会导致系统崩溃。如果没有提供这些访问方法，即使你已经指明了字符串的格式是会变化的，这个字符串格式也成了事实上的 API。

在静态工具类中编写 toString 方法是没有意义的。也不要在大多数枚举类型（详见实验 34）中编写 toString 方法，因为 Java 已经为你提供了非常完美的方法。但是，在所有其子类共享通用字符串表示法的抽象类中，一定要编写一个 toString 方法。

例如，大多数集合实现中的 toString 方法都是继承自抽象的集合类。在实验 10 中讨论过的 Google 公司开源的 AutoValue 工具，会替你生成 toString 方法，大多数集成开发环境 IDE 也有这样的功能。这些方法都能很好地告诉你每个域的内容，但是并不特定于该类的意义（meaning）。因此，比如对于上述 PhoneNumber 类就不适合用自动生成的 toString 方法（因为电话号码有标准的字符串表示法），但是我们的 Potion 类就非常适合。也就是说，自动生成的 toString 方法要远远优先于继承自 Object 的方法，因为它无法告诉你任何关于对象值的信息。



实验总结



总而言之，要在你编写的每一个可实例化的类中覆盖 Object 的 toString 实现，除非已经在超类中这么做了。这样会使类使用起来更加舒适，也更易于调试。toString 方法应该以美观的格式返回一个关于对象的简洁、有用的描述。



# 实验13	谨慎地覆盖 clone

实验介绍



本实验将介绍如何实现一个行为良好的 clone 方法，并讨论何时适合这样做，同时也简单地讨论了其他的可替代做法。

#### 知识点

- Cloneable 接口
- clone() 方法

clone() 方法



Cloneable 接口的目的是作为对象的一个 mixin 接口（mixin interface）（详见实验 20），表明这样的对象允许克隆（clone）。遗憾的是，它并没有成功地达到这个目的。它的主要缺陷在于缺少一个 clone 方法，而 Object 的 clone 方法是受保护的。如果不借助于反射（reflection）（详见实验 65），就不能仅仅因为一个对象实现了 Cloneable，就调用 clone 方法。即使是反射调用也可能会失败，因为不能保证该对象一定具有可访问的 clone 方法。尽管存在这样或那样的缺陷，这项设施仍然被广泛使用，因此值得我们进一步了解。

既然 Cloneable 接口并没有包含任何方法，那么它到底有什么作用呢？它决定了 Object 中受保护的 clone 方法实现的行为：如果一个类实现了 Cloneable，Object 的 clone 方法就返回该对象的逐域拷贝，否则就会抛出 CloneNotSupportedException 异常。这是接口的一种极端非典型的用法，也不值得仿效。通常情况下，实现接口是为了表明类可以为它的客户做些什么。然而，对于 Cloneable 接口，它改变了超类中受保护的方法的行为。

虽然规范中没有明确指出，事实上，实现 Cloneable 接口的类是为了提供一个功能适当的公有的 clone 方法。为了达到这个目的，类及其所有超类都必须遵守一个相当复杂的、不可实施的，并且基本上没有文档说明的协议。由此得到一种语言之外的（extralinguistic）机制：它无须调用构造器就可以创建对象。

clone 方法的通用约定是非常弱的，下面是来自 Object 规范中的约定内容：

创建和返回该对象的一个拷贝。这个“拷贝”的精确含义取决于该对象的类。一般的含义是，对于任何对象 x，表达式

```java
x.clone() != x
```

将会返回结果 true,并且表达式

```java
x.clone().getClass() == x.getClass()
```

将会返回结果 true，但这些都不是绝对的要求，虽然通常情况下，表达式

```java
x.clone().equals(x)
```

将会返回结果 true，但是，这也不是一个绝对的要求。 按照约定，这个方法返回的对象应该通过调用 super.clone 获得。如果类及超类（Object 除外）遵守这一约定，那么：

```java
x.clone().getClass()==x.getClass()
```

按照约定，返回的对象应该不依赖于被克隆的对象。为了成功地实现这种独立性，可能需要在 super.clone 返回对象之前，修改对象地一个或更多个域。

这种机制大体上类似于自动的构造器调用链，只不过它不是强制要求的：如果类的 clone 方法返回的实例不是通过调用 super.clone 方法获得，而是通过调用构造器获得，编译器就不会发出警告，但是该类的子类调用了 super.clone 方法，得到的对象就会拥有错误的类，并阻止了 clone 方法的子类正常工作。如果 final 类覆盖了 clone 方法，那么这个约定可以被安全地忽略，因为没有子类需要担心它。如果 final 类的 clone 方法没有调用 super.clone 方法，这个类就没有理由去实现 Cloneable 接口了，因为它不依赖于 Object 克隆实现的行为。

假设你希望在一个类中实现 Cloneable 接口，并且它的超类都提供了行为良好的 clone 方法。首先，调用 super.clone 方法。由此得到的对象将是原始对象功能完整的克隆（clone）。在这个类中声明的域将等同于被克隆对象中相应的域。如果每个域包含一个基本类型的值，或者包含一个指向不可变对象的引用，那么被返回的对象则可能正是你所需要的对象，在这种情况下不需要再做进一步处理。例如，实验 11 中的 PhoneNumber 类正是如此，但要注意，不可变的类永远都不应该提供 clone 方法，因为它只会激发不必要的克隆。因此，PhoneNumber 的 clone 方法应该是这样的：

```java
//Clone method for class with no references to mutable state
@Override public PhoneNumber clone(){
    try{
        return (PhoneNumber) super.clone();
    }catch (C1oneNotSupportedException e){
        throw new AssertionError();//Can’t happen
    }
}
```

为了让这个方法生效，应该修改 PhoneNumber 的类声明为实现 Cloneable 接口。虽然 Object 的 clone 方法返回的是 Object，但这个 clone 方法返回的却是 PhoneNumber。这么做是合法的，也是我们所期望的，因为 Java 支持协变返回类型（covariant return type）。换句话说，目前覆盖方法的返回类型可以是被覆盖方法的返回类型的子类了。这样在客户端中就不必进行转换了。我们必须在返回结果之前，先将 super.clone 从 Object 转换成 PhoneNumber，当然这种转换是一定会成功的。

对 super.clone 方法的调用应当包含在一个 try-catch 块中。这是因为 Object 声明其 clone 方法抛出 CloneNotSupportedException，这是一个受检异常（checked exception）。由于 PhoneNumber 实现了 Cloneable 接口，我们知道调用 super.clone 方法一定会成功。对于这个样板代码的需求表明，CloneNotSupportedException 应该还没有被检查到（详见实验 71）。



clone 复杂对象



如果对象中包含的域引用了可变的对象，使用上述这种简单的 clone 实现可能会导致灾难性的后果。例如实验 7 中的 Stack 类：

```java
public class Stack{
private Obj ect[]elements;
private int size=0;
private static final int DEFAULT_INITIAL_CAPACITY=16
public Stack(){
    this .elements=new Object[DEFAULT_INITIAL_CAPACITY];
}
public void push(Object e){
    ensureCapacity();
    elements[size++]=e;
}
public Object pop(){
    if  (size==0)
          throw new EmptyStackException();
    Object result=elements[--size];
    elements[size]=null;//Eliminate obsolete reference
    return result;
}
//Ensure space for at least one more element.
private void ensureCapacity(){
    if (elements .length==size)
          elements=Arrays .copy0f(elements,2*size+1);
}
}
```

假设你希望把这个类做成可克隆的（cloneable）。如果它的 clone 方法仅仅返回 `super.clone()`，这样得到的 Stack 实例，在其 size 域中具有正确的值，但是它的 elements 域将引用与原始 Stack 实例相同的数组。修改原始的实例会破坏被克隆对象中的约束条件，反之亦然。很快你就会发现，这个程序将产生毫无意义的结果，或者抛出 NullPointerException 异常。

如果调用 Stack 类中唯一的构造器，这种情况就永远不会发生。实际上，clone 方法就是另一个构造器；必须确保它不会伤害到原始的对象，并确保正确地创建被克隆对象中的约束条件（invariant）。为了使 Stack 类中的 clone 方法正常工作，它必须要拷贝栈的内部信息。最容易的做法是，在 elements 数组中递归地调用 clone：

```java
//Clone method for class with references to mutable state

@Override public Stack clone(){
    try{
        Stack result=(Stack) super.clone();
        result.elements=elements .clone();
        return result;
    }catch (C1oneNotSupportedException e){
        throw new AssertionError();
    }
}
```

注意，我们不一定要将 elements.clone() 的结果转换成 Object[]。在数组上调用 clone 返回的数组，其编译时的类型与被克隆数组的类型相同。这是复制数组的最佳习惯做法。事实上，数组是 clone 方法唯一吸引人的用法。

还要注意如果 elements 域是 final 的，上述方案就不能正常工作，因为 clone 方法是被禁止给 final 域赋新值的。这是个根本的问题：就像序列化一样，Cloneable 架构与引用可变对象的 final 域的正常用法是不相兼容的，除非在原始对象和克隆对象之间可以安全地共享此可变对象。为了使类成为可克隆的，可能有必要从某些域中去掉 final 修饰符。

递归地调用 clone 有时还不够。例如，假设你正在为一个散列表编写 clone 方法，它的内部数据包含一个散列桶数组，每个散列桶都指向“键 - 值”对链表的第一项。出于性能方面的考虑，该类实现了它自己的轻量级单向链表，而没有使用 Java 内部的 java.util.LinkedList：

```java
public class HashTable implements Cloneable{
    private Entry[]buckets=...;
    private static class Entry{
        final Object key;
        Object value;
        Entry  next;
        Entry(Object key, Object value,Entry next){
            this.key=key;
            this.value=vaule;
            this.next=next;

        }
    }
    ...//Remainder omitted

}
```

假设你仅仅递归地克隆这个散列桶数组，就像我们对 Stack 类所做的那样：

```java
//Broken clone method-results in shared mutable state!
@Override public HashTable clone(){
    try{
    HashTable result=(HashTable) super.clone();
      result .buckets=buckets .clone();
      return result;
    }catch (C1oneNotSupportedException e){
        throw new AssertionError();
    }
}
```

虽然被克隆对象有它自己的散列桶数组，但是，这个数组引用的链表与原始对象是一样的，从而很容易引起克隆对象和原始对象中不确定的行为。为了修正这个问题，必须单独地拷贝并组成每个桶的链表。下面是一种常见的做法：

```java
//Recursive clone method for class with complex mutable state
public class HashTable implements Cloneable{
    private Entry[] buckets=...;
    private static class Entry{
        final Object key;
        Object value;
        Entry  next;
        Entry(Object key，Object value，Entry next){
            this .key=key:
            this .value=value;
            this .next=next;
        }
        //Recursively copy the linked list headed by this Entry
        Entry deepCopy(){
            return new Entry(key,value,
                next==null?null:next.deepCopy());
        }
    }
    @Override public HashTable clone(){
        try{
            HashTable result=(HashTable) super.clone();
            result.buckets=new Entry[buckets.length];
            for  (i nt i=0;i<buckets.length;i++)
                    if(buckets[i]!=null)
                    result .buckets[i〕=buckets[i].deepCopy();
            return result;
        }catch (CloneNotSupportedException e){
            throw new AssertionError();
        }
    }
    ...//Remainder omitted
}
```

私有类 HashTable.Entry 被加强了，它支持一个“深度拷贝”（deep copy）方法。HashTable 上的 clone 方法分配了一个大小适中的、新的 buckets 数组，并且遍历原始的 buckets 数组，对每一个非空散列桶进行深度拷贝。Entry 类中的深度拷贝方法递归地调用它自身，以便拷贝整个链表（它是链表的头节点）。虽然这种方法很灵活，如果散列桶不是很长，也会工作得很好，但是，这样克隆一个链表并不是一种好办法，因为针对列表中的每个元素，它都要消耗一段栈空间。如果链表比较长，这很容易导致栈溢出。为了避免发生这种情况，你可以在 deepCopy 方法中用迭代（iteration）代替递归（recursion）：

```java
//Iteratively copy the linked list headed by this Entry
Entry deepCopy(){
    Entry result=new Entry(key，value，next);
    for (Entry p=result;p.next!=null;p=p.next)
        p.next=new Entry(p.next.key,p.next.value,p.next.next);
    return result;
}
```

克隆复杂对象的最后一种办法是，先调用 super.clone 方法，然后把结果对象中的所有域都设置成它们的初始状态（initial state），然后调用高层（higher-level）的方法来重新产生对象的状态。在我们的 HashTable 例子中，buckets 域将被初始化为一个新的散列桶数组，然后，对于正在被克隆的散列表中的每一个键 - 值映射，都调用 put（key, value）方法（上面没有给出其代码）。这种做法往往会产生一个简单、合理且相当优美的 clone 方法，但是它运行起来通常没有“直接操作对象及其克隆对象的内部状态的 clone 方法”快。虽然这种方法干脆利落，但它与整个 Cloneable 架构是对立的，因为它完全抛弃了 Cloneable 架构基础的逐域对象复制的机制。

像构造器一样，clone 方法也不应该在构造的过程中，调用可以覆盖的方法（详见实验 19）。如果 clone 调用了一个在子类中被覆盖的方法，那么在该方法所在的子类有机会修正它在克隆对象中的状态之前，该方法就会先被执行，这样很有可能会导致克隆对象和原始对象之间的不一致。因此，上一段中讨论到的 put(key, value) 方法要么应是 final 的，要么应是私有的。（如果是私有的，它应该算是非 final 公有方法的“辅助方法”。）

Object 的 clone 方法被声明为可抛出 CloneNotSupportedException 异常，但是，覆盖版本的 clone 方法可以忽略这个声明。公有的 clone 方法应该省略 throws 声明，因为不会抛出受检异常的方法使用起来更加轻松（详见实验 71）。

为继承（详见实验 19）设计类有两种选择，但是无论选择其中的哪一种方法，这个类都不应该实现 Cloneable 接口。你可以选择模拟 Object 的行为：实现一个功能适当的受保护的 clone 方法，它应该被声明抛出 CloneNotSupportedException 异常。这样可以使子类具有实现或不实现 Cloneable 接口的自由，就仿佛它们直接扩展了 Object 一样。或者，也可以选择不去实现一个有效的 clone 方法，并防止子类去实现它，只需要提供下列退化了的 clone 实现即可：

```java
//clone method for extendable class not supporting Cloneable
@Override
protected final Object clone()throws C1oneNotSupportedException{
    throw new C1oneNotSupportedException();
}
```

还有一点值得注意。如果你编写线程安全的类准备实现 Cloneable 接口，要记住它的 clone 方法必须得到严格的同步，就像任何其他方法一样（详见实验 78）。Object 的 clone 方法没有同步，即使很满意可能也必须编写同步的 clone 方法来调用 `super.clone()`，即实现 `synchronized clone()` 方法。

简而言之，所有实现了 Cloneable 接口的类都应该覆盖 clone 方法，并且是公有的方法，它的返回类型为类本身。该方法应该先调用 super.clone 方法，然后修正任何需要修正的域。一般情况下，这意味着要拷贝任何包含内部“深层结构”的可变对象，并用指向新对象的引用代替原来指向这些对象的引用。虽然，这些内部拷贝操作往往可以通过递归地调用 clone 来完成，但这通常并不是最佳方法。如果该类只包含基本类型的域，或者指向不可变对象的引用，那么多半的情况是没有域需要修正。这条规则也有例外。例如，代表序列号或其他唯一 ID 值的域，不管这些域是基本类型还是不可变的，它们也都需要被修正。



代替 clone 的方法



如果你扩展一个实现了 Cloneable 接口的类，那么你除了实现一个行为良好的 clone 方法外，没有别的选择。否则，最好提供某些其他的途径来代替对象拷贝。对象拷贝的更好的办法是提供一个拷贝构造器（copy constructor）或拷贝工厂（copy factory）。拷贝构造器只是一个构造器，它唯一的参数类型是包含该构造器的类，例如：

```java
//Copy constructor
public Yum(Yum yum){ ...};
```

拷贝工厂是类似于拷贝构造器的静态工厂（详见实验 1）：

```java
//Copy factory
public static Yum newInstance(Yum yum){...};
```

拷贝构造器的做法，及其静态工厂方法的变形，都比 Cloneable/clone 方法具有更多的优势：它们不依赖于某一种很有风险的、语言之外的对象创建机制；它们不要求遵守尚未制定好文档的规范；它们不会与 final 域的正常使用发生冲突；它们不会抛出不必要的受检异常；它们不需要进行类型转换。

甚至，拷贝构造器或者拷贝工厂可以带一个参数，参数类型是该类所实现的接口。例如，按照惯例所有通用集合实现都提供了一个拷贝构造器，其参数类型为 Collection 或者 Map 接口。基于接口的拷贝构造器和拷贝工厂（更准确的叫法应该是转换构造器（conversion constructor）和转换工厂（conversion factory）），允许客户选择拷贝的实现类型，而不是强迫客户接受原始的实现类型。例如，假设你有一个 HashSet:s，并且希望把它拷贝成一个 TreeSet。clone 方法无法提供这样的功能，但是用转换构造器很容易实现：new TreeSet<>(s)。

既然所有的问题都与 Cloneable 接口有关，新的接口就不应该扩展这个接口，新的可扩展的类也不应该实现这个接口。虽然 final 类实现 Cloneable 接口没有太大的危害，这个应该被视同性能优化，留到少数必要的情况下才使用（详见实验 67）。总之，复制功能最好由构造器或者工厂提供。这条规则最绝对的例外是数组，最好利用 clone 方法复制数组。



实验总结



本次实验介绍了一些使用 clone()方法的情况，以及在这些情况下使用 clone()带来的问题，也介绍了其他复制功能的实现方法。



# 实验14 	考虑实现 Comparable 接口


实验介绍



本次实验将介绍 Comparable 接口和 Comparable 接口中唯一的方法 compareTo 方法，并详细阐述 compareTo 方法的使用。

#### 知识点

- Comparable 接口
- compareTo 方法

compareTo 方法简介



与本章中讨论的其他方法不同，compareTo 方法并没有在 Object 类中声明。相反，它是 Comparable 接口中唯一的方法。compareTo 方法不但允许进行简单的等同性比较，而且允许执行顺序比较，除此之外，它与 Object 的 equals 方法具有相似的特征，它还是个泛型（generic）。类实现了 Comparable 接口，就表明它的实例具有内在的排序关系（natural ordering）。为实现 Comparable 接口的对象数组进行排序就这么简单：

```java
Arrays.sort(a);
```

对存储在集合中的 Comparable 对象进行搜索、计算极限值以及自动维护也同样简单。例如，下面的程序依赖于实现了 Comparable 接口的 String 类，它去掉了命令行参数列表中的重复参数，并按字母顺序打印出来：

```java
public class Wordlist{
    public static void main(String[] args){
    Set<String>  s=new TreeSet<>();
    Collections.addAll(s,args);
    System.out.println(s);
    }
}
```

一旦类实现了 Comparable 接口，它就可以跟许多泛型算法（generic algorithm）以及依赖于该接口的集合实现（collection implementation）进行协作。你付出很小的努力就可以获得非常强大的功能。事实上，Java 平台类库中的所有值类（value classes），以及所有的枚举类型（详见实验 34）都实现了 Comparable 接口。如果你正在编写一个值类，它具有非常明显的内在排序关系，比如按字母顺序、按数值顺序或者按年代顺序，那你就应该坚决考虑实现 Comparable 接口：

```java
public interface Comparable<T>{
    int compareTo(T t);
}
```

compareTo 方法的通用约定与 equals 方法的约定相似：

将这个对象与指定的对象进行比较。当该对象小于、等于或大于指定对象的时候，分别返回一个负整数、零或者正整数。如果由于指定对象的类型而无法与该对象进行比较，则抛出 ClassCastException 异常。

在下面的说明中，符号 sgn(expression) 表示数学中的 signum 函数，它根据表达式（expression）的值为负值、零和正值，分别返回 -1、0 或 1。

- 实现者必须确保所有的 x 和 y 都满足 sgn(x.compareTo(y)) == -sgn (y.compareTo(x))。（这也暗示着，当且仅当 y.compareTo(x) 抛出异常时，x.compareTo(y) 才必须抛出异常。）
- 实现者还必须确保这个比较关系是可传递的：(x.compareTo(y) > 0 && y.compareTo(z)> 0) 暗示着 x.compareTo(z) > 0。
- 最后， 实现者必须确保 x.compareTo(y) == 0 暗示着所有的 z 都满足 sgn(x.compareTo(z))== sgn(y.compareTo(z))。
- 强烈建议 (x.compareTo(y) == 0) == (x.equals(y))，但这并非绝对必要。一般说来，任何实现了 Comparable 接口的类，若违反了这个条件，都应该明确予以说明。推荐使用这样的说法：“注意：该类具有内在的排序功能，但是与 equals 不一致。”

千万不要被上述约定中的数学关系所迷惑。如同 equals 约定（详见实验 10）一样，compareTo 约定并没有看起来那么复杂。与 equals 方法不同的是，它对所有的对象强行施加了一种通用的等同关系，compareTo 不能跨越不同类型的对象进行比较：在比较不同类型的对象时，compareTo 可以抛出 ClassCastException 异常。通常，这正是 compareTo 在这种情况下应该做的事情。合约确实允许进行跨类型之间的比较，这一般是在被比较对象实现的接口中进行定义。

就好像违反了 hashCode 约定的类会破坏其他依赖于散列的类一样，违反 compareTo 约定的类也会破坏其他依赖于比较关系的类。依赖于比较关系的类包括有序集合类 TreeSet 和 TreeMap，以及工具类 Collections 和 Arrays，它们内部包含有搜索和排序算法。



compareTo 方法的约定详解



现在我们来回顾一下 compareTo 约定中的条款。第一条指出，如果颠倒了两个对象引用之间的比较方向，就会发生下面的情况：如果第一个对象小于第二个对象，则第二个对象一定大于第一个对象；如果第一个对象等于第二个对象，则第二个对象一定等于第一个对象；如果第一个对象大于第二个对象，则第二个对象一定小于第一个对象。第二条指出，如果一个对象大于第二个对象，并且第二个对象又大于第三个对象，那么第一个对象一定大于第三个对象。最后一条指出，在比较时被认为相等的所有对象，它们跟别的对象做比较时一定会产生同样的结果。

这三个条款的一个直接结果是，由 compareTo 方法施加的等同性测试，也必须遵守相同于 equals 约定所施加的限制条件：自反性、对称性和传递性。因此，下面的告诫也同样适用：无法在用新的值组件扩展可实例化的类时，同时保持 compareTo 约定，除非愿意放弃面向对象的抽象优势（详见实验 10）。针对 equals 的权宜之计也同样适用于 compareTo 方法。如果你想为一个实现了 Comparable 接口的类增加值组件，请不要扩展这个类；而是要编写一个不相关的类，其中包含第一个类的一个实例。然后提供一个“视图”（view）方法返回这个实例。这样既可以让你自由地在第二个类上实现 compareTo 方法，同时也允许它的客户端在必要的时候，把第二个类的实例视同第一个类的实例。

compareTo 约定的最后一段是一条强烈的建议，而不是真正的规则，它只是说明了 compareTo 方法施加的等同性测试，在通常情况下应该返回与 equals 方法同样的结果。如果遵守了这一条，那么由 compareTo 方法所施加的顺序关系就被认为与 equals 一致。如果违反了这条规则，顺序关系就被认为与 equals 不一致。如果一个类的 compareTo 方法施加了一个与 equals 方法不一致的顺序关系，它仍然能够正常工作，但是如果一个有序集合（sorted collection）包含了该类的元素，这个集合就可能无法遵守相应集合接口（Collection、Set 或 Map）的通用约定。因为对于这些接口的通用约定是按照 equals 方法来定义的，但是有序集合使用了由 compareTo 方法而不是 equals 方法所施加的等同性测试。尽管出现这种情况不会造成灾难性的后果，但是应该有所了解。

例如，以 BigDecimal 类为例，它的 compareTo 方法与 equals 不一致。如果你创建了一个空的 HashSet 实例，并且添加 `new BigDecimal("1.0")` 和 `new BigDecimal("1.00")`，这个集合就将包含两个元素，因为新增到集合中的两个 BigDecimal 实例，通过 equals 方法来比较时是不相等的。然而，如果你使用 TreeSet 而不是 HashSet 来执行同样的过程，集合中将只包含一个元素，因为这两个 BigDecimal 实例在通过 compareTo 方法进行比较时是相等的。

编写 compareTo 方法与编写 equals 方法非常相似，但也存在几处重大的差别。因为 Comparable 接口是参数化的，而且 comparable 方法是静态的类型，因此不必进行类型检查，也不必对它的参数进行类型转换。如果参数的类型不合适，这个调用甚至无法编译。如果参数为 null，这个调用应该抛出 NullPointerException 异常，并且一旦该方法试图访问它的成员时就应该抛出异常。

CompareTo 方法中域的比较是顺序的比较，而不是等同性的比较。比较对象引用域可以通过递归地调用 compareTo 方法来实现。如果一个域并没有实现 Comparable 接口，或者你需要使用一个非标准的排序关系，就可以使用一个显式的 Comparator 来代替。或者编写自己的比较器，或者使用已有的比较器，例如针对实验 10 中的 CaseInsensitiveString 类的 这个 compareTo 方法使用一个已有的比较器：

```java
//Single一field Comparable with object reference field
public final class CaseInsensitiveString
          implements Comparable<CaseInsensitiveString>{
    public int compareTo(CaseInsensitiveString cis){
        return String.CASE_INSENSITIVE_ORDER.compares(s,cis.s)

    }
    ...//Remainder omitted

}
```

注意 CaseInsensitiveString 类实现了 Comparable 接口。这意味着 CaseInsensitiveString 引用只能与另一个 CaseInsensitiveString 引用进行比较。在声明类去实现 Comparable 接口时，这是常用的模式。

在这之前建议 compareTo 方法可以利用关系操作符 < 和 > 去比较整数型基本类型的域，用静态方法 Double.compare 和 Float.compare 去比较浮点基本类型域。在 Java 7 版本中，已经在 Java 的所有装箱基本类型的类中增加了静态的 compare 方法。在 compareTo 方法中使用关系操作符 < 和 > 是非常烦琐的，并且容易出错，因此不再建议使用。

如果一个类有多个关键域，那么，按什么样的顺序来比较这些域是非常关键的。你必须从最关键的域开始，逐步进行到所有的重要域。如果某个域的比较产生了非零的结果（零代表相等），则整个比较操作结束，并返回该结果。如果最关键的域是相等的，则进一步比较次关键的域，以此类推。如果所有的域都是相等的，则对象就是相等的，并返回零。下面通过实验 11 中的 PhoneNumber 类的 compareTo 方法来说明这种方法：

```java
//Multiple-field Comparable with primitive fields
public int compareTo(PhoneNumber pn){
    int result=Short.compare(areaCode,pn.areaCode);
    if(result==0){
          result=Short.compare(prefix,pn.prefix);
          if(result==0)
              result=Short .compare(lineNum,pn.lineNum);
      }
      return result;
}
```



Comparator 接口的比较器构造方法



Java 8 中，Comparator 接口配置了一组比较器构造方法（comparator construction methods），使得比较器的构造工作变得非常流畅。之后，按照 Comparable 接口的要求，这些比较器可以用来实现一个 compareTo 方法。许多程序员都喜欢这种方法的简洁性，虽然它要付出一定的性能成本：在我的机器上，PhoneNumber 实例的数组排序的速度慢了大约 10%。在使用这个方法时，为了简洁起见，可以考虑使用 Java 的静态导入（static import）设施，通过静态比较器构造方法的简单的名称就可以对它们进行引用。下面是使用这个方法之后 PhoneNumber 的 compareTo 方法：

```java
//Comparable with comparator construction methods
private static final Comparator<PhoneNumber> COMPARATOR =
          comparingInt((PhoneNumber pn)->pn .areaCode)
            .thenComparingInt(pn->pn.prefix)
            .thenComparingInt(pn->pn.lineNum);
public int compareTo(PhoneNumber pn){
    return COMPARATOR .compare(this，pn);
}
```

这个实现利用两个比较构造方法，在初始化类的时候构建了一个比较器。第一个是 comparingInt。这是一个静态方法，带有一个键提取器函数（key extractor function），它将一个对象引用映射到一个类型为 int 的键上，并根据这个键返回一个对实例进行排序的比较器。在上一个例子中，comparingInt 带有一个 lambda()，它从 PhoneNumber 提取区号，并 返回一个按区号对电话号码进行排序的 Comparator。注意，lambda 显式定义了其输入参数（PhoneNumber pn）的类型。事实证明，在这种情况下，Java 的类型推导还没有强大到足以为自己找出类型，因此我们不得不帮助它直接进行指定，以使程序能够成功地进行编译。

如果两个电话号码的区号相同，就需要进一步细化比较，这正是第二个比较器构造方法 thenComparingInt 要完成的任务。这是 Comparator 上的一个实例方法，带有一个类型为 int 的键提取器函数，它会返回一个最先运用原始比较器的比较器，然后利用提取到的键继续比较。还可以随意地叠加多个 thenComparingInt 调用，并按词典顺序进行排序。在上述例子中，叠加了两个 thenComparingInt 调用，按照第二个键为前缀且第三个键为行数的顺序进行排序。注意，并不一定要指定传入 thenComparingInt 调用的键提取器函数的参数类型：Java 的类型推导十分智能，它足以为自己找出正确的类型。

Comparator 类具备全套的构造方法。对于基本类型 long 和 double 都有对应的 comparingInt 和 thenComparingInt。int 版本也可以用于更狭义的整数型类型，如 PhoneNumber 例子中的 short。double 版本也可以用于 float。这样便涵盖了所有的 Java 数字型基本类型。

对象引用类型也有比较器构造方法。静态方法 comparing 有两个重载。一个带有键提取器，使用键的内在排序关系。第二个既带有键提取器，还带有要用在被提取的键上的比较器。这个名为 thenComparing 的实例方法有三个重载。一个重载只带一个比较器，并用它提供次级顺序。第二个重载只带一个键提取器，并利用键的内在排序关系作为次级顺序。最后一个重载既带有键提取器，又带有要在被提取的键上使用的比较器。

compareTo 或者 compare 方法偶尔也会依赖于两个值之间的区别，即如果第一个值小于第二个值，则为负；如果两个值相等，则为零；如果第一个值大于第二个值，则为正。下面举个例子：

```java
//BROKEN difference-based comparator-violates transitive
static Comparator<Object> hashCodeOrder =new Comparator<>(){
public int compare(Object of,Object o2){
    return o1.hashCode()-o2.hashCode();
    }
};
```

千万不要使用这个方法。它很容易造成整数溢出，同时违反 IEEE 754 浮点算术标准。甚至，与利用本条目讲到的方法编写的那些方法相比，最终得到的方法并没有明显变快。因此，要么使用一个静态方法 compare：

```java
//Comparator based on static compare method
static Comparator<Object> hashCodeOrder = new Comparator<>(){
    public int compare(Object o1,Object o2){
        return Integer.compare(ol.hashCode(),o2.hashCode());

    }
};
```

要么使用一个比较器构造方法：

```java
//Comparator based on Comparator construction method
static Comparator<Object> hashCodeOrder=
    Comparator.comparingInt(o->o.hashCode());
```



实验总结



总而言之，每当实现一个对排序敏感的类时，都应该让这个类实现 Comparable 接口，以便其实例可以轻松地被分类、搜索，以及用在基于比较的集合中。每当在 compareTo 方法的实现中比较域值时，都要避免使用 < 和 > 操作符，而应该在装箱基本类型的类中使用静态的 compare 方法，或者在 Comparator 接口中使用比较器构造方法。

# 第三章	类和接口

# 实验15 	使类和成员的可访问性最小化

实验介绍



本次实验将为大家介绍不同访问权限的相关知识，主要讲解私有的访问级别，并拓展了 Java 9 新增的两种隐式访问级别。

#### 知识点

- 访问级别

访问控制



区分一个组件设计得好不好，唯一重要的因素在于，它对于外部的其他组件而言，是否隐藏了其内部数据和其他实现细节。设计良好的组件会隐藏所有的实现细节，把 API 与实现清晰地隔离开来。然后，组件之间只通过 API 进行通信，一个模块不需要知道其他模块的内部工作情况。这个概念被称为信息隐藏（information hiding）或封装（encapsulation），是软件设计的基本原则之一。

信息隐藏之所以非常重要有许多原因，其中大多是因为：它可以有效地解除组成系统的各组件之间的耦合关系，即解耦（decouple），使得这些组件可以独立地开发、测试、优化、使用、理解和修改。因为这些组件可以并行开发，所以加快了系统开发的速度。同时减轻了维护的负担，程序员可以更快地理解这些组件，并且在调试它们的时候不影响其他的组件。虽然信息隐藏本身无论是对内还是对外都不会带来更好的性能，但是可以有效地调节性能：一旦完成一个系统，并通过剖析确定了哪些组件影响了系统的性能（详见实验 67），那些组件就可以被进一步优化，而不会影响到其他组件的正确性。信息隐藏提高了软件的可重用性，因为组件之间并不紧密相连，除了开发这些模块所使用的环境之外，它们在其他的环境中往往也很有用。最后，信息隐藏也降低了构建大型系统的风险，因为即使整个系统不可用，这些独立的组件仍有可能是可用的。

Java 提供了许多机制（facility）来协助信息隐藏。访问控制（access control）机制决定了类、接口和成员的可访问性（accessibility）。实体的可访问性是由该实体声明所在的位置，以及该实体声明中所出现的访问修饰符（private、protected 和 public）共同决定的。正确地使用这些修饰符对于实现信息隐藏是非常关键的。

规则很简单：尽可能地使每个类或者成员不被外界访问。换句话说，应该使用与你正在编写的软件的对应功能相一致的、尽可能最小的访问级别。

对于顶层的（非嵌套的）类和接口，只有两种可能的访问级别：包级私有的（packageprivate）和公有的（public）。如果你用 public 修饰符声明了顶层类或者接口，那它就是公有的；否则，它将是包级私有的。如果类或者接口能够被做成包级私有的，它就应该被做成包级私有。通过把类或者接口做成包级私有，它实际上成了这个包的实现的一部分，而不是该包导出的 API 的一部分，在以后的发行版本中，可以对它进行修改、替换或者删除，而无须担心会影响到现有的客户端程序。如果把它做成公有的，你就有责任永远支持它，以保持它们的兼容性。

如果一个包级私有的顶层类（或者接口）只是在某一个类的内部被用到，就应该考虑使它成为唯一使用它的那个类的私有嵌套类（详见实验 24）。这样可以将它的可访问范围从包中的所有类缩小到使用它的那个类。然而，降低不必要公有类的可访问性，比降低包级私有的顶层类的可访问性重要得多：因为公有类是包的 API 的一部分，而包级私有的顶层类则已经是这个包的实现的一部分。

对于成员（域、方法、嵌套类和嵌套接口）有四种可能的访问级别，下面按照可访问性的递增顺序罗列出来：

- 私有的（private）—只有在声明该成员的顶层类内部才可以访问这个成员。
- 包级私有的（package-private）—声明该成员的包内部的任何类都可以访问这个成员。从技术上讲，它被称为“缺省”（default）访问级别，如果没有为成员指定访问修饰符，就采用这个访问级别（当然，接口成员除外，它们默认的访问级别是公有的）。
- 受保护的（protected）—声明该成员的类的子类可以访问这个成员（但有一些限制），并且声明该成员的包内部的任何类也可以访问这个成员。
- 公有的（public）—在任何地方都可以访问该成员。

当你仔细地设计了类的公有 API 之后，可能觉得应该把所有其他的成员都变成私有的。其实，只有当同一个包内的另一个类真正需要访问一个成员的时候，你才应该删除 private 修饰符，使该成员变成包级私有的。如果你发现自己经常要做这样的事情，就应该重新检查系统设计，看看是否另一种分解方案所得到的类，与其他类之间的耦合度会更小。也就是说，私有成员和包级私有成员都是一个类的实现中的一部分，一般不会影响导出的 API。然而，如果这个类实现了 Serializable 接口（详见实验 86 和实验 87），这些域就有可能会被“泄漏”（leak）到导出的 API 中。

对于公有类的成员，当访问级别从包级私有变成保护级别时，会大大增强可访问性。受保护的成员是类的导出的 API 的一部分，必须永远得到支持。导出的类的受保护成员也代表了该类对于某个实现细节的公开承诺（详见实验 19）。应该尽量少用受保护的成员。

有一条规则限制了降低方法的可访问性的能力。如果方法覆盖了超类中的一个方法，子类中的访问级别就不允许低于超类中的访问级别。这样可以确保任何可使用超类的实例的地方也都可以使用子类的实例（里氏替换原则，详见实验 10）。如果违反了这条规则，那么当你试图编译该子类的时候，编译器就会产生一条错误消息。这条规则有一个特例：如果一个类实现了一个接口，那么接口中所有的方法在这个类中也都必须被声明为公有的。

为了便于测试代码，你可以试着使类、接口或者成员变得更容易访问。这么做在一定程度上来说是好的。为了测试而将一个公有类的私有成员变成包级私有的，这还可以接受，但是要将访问级别提高到超过它，这就无法接受了。换句话说，不能为了测试，而将类、接口或者成员变成包的导出的 API 的一部分。幸运的是，也没有必要这么做，因为可以让测试作为被测试的包的一部分来运行，从而能够访问它的包级私有的元素。

公有类的实例域决不能是公有的（详见实验 16）。如果实例域是非 final 的，或者是一个指向可变对象的 final 引用，那么一旦使这个域成为公有的，就等于放弃了对存储在这个域中的值进行限制的能力；这意味着，你也放弃了强制这个域不可变的能力。同时，当这个域被修改的时候，你也失去了对它采取任何行动的能力。因此，包含公有可变域的类通常并不是线程安全的。即使域是 final 的，并且引用不可变的对象，但当把这个域变成公有的时候，也就放弃了“切换到一种新的内部数据表示法”的灵活性。

这条建议也同样适用于静态域，只是有一种情况例外。假设常量构成了类提供的整个抽象中的一部分，可以通过公有的静态 final 域来暴露这些常量。按惯例，这种域的名称由大写字母组成，单词之间用下划线隔开（详见实验 68）。很重要的一点是，这些域要么包含基本类型的值，要么包含指向不可变对象的引用（详见实验 17）。如果 final 域包含可变对象的引 用，它便具有非 final 域的所有缺点。虽然引用本身不能被修改，但是它所引用的对象却可以被修改，这会导致灾难性的后果。

注意，长度非零的数组总是可变的，所以让类具有公有的静态 final 数组域，或者返回这种域的访问方法，这是错误的。如果类具有这样的域或者访问方法，客户端将能够修改数组中的内容。这是安全漏洞的一个常见根源：

```java
//Potenial security hole
public static final Thing[] VALUES = {...};
```

要注意，许多 IDE 产生的访问方法会返回指向私有数组域的引用，正好导致了这个问题。修正这个问题有两种方法。可以使公有数组变成私有的，并增加一个公有的不可变列表：

```java
private static final Thing[] PRIVATE_VALUES={...};
public static final List<Thing> VALUES=
    Collections.unmodifiableList(Arrays.asList(PRIVATE_VALUES));
```

另一种方法是，也可以使数组变成私有的，并添加一个公有方法，它返回私有数组的一个拷贝：

```java
private static final Thing[]PRIVATE_VALUES={…}
public static final Thing[]values(){
      return PRIVATE_VALUES.clone();
}
```

要在这两种方法之间做出选择，得考虑客户端可能怎么处理这个结果。哪种返回类型会更加方便？哪种会得到更好的性能？

从 Java 9 开始，又新增了两种隐式访问级别，作为模块系统（module system）的一部分。一个模块就是一组包，就像一个包就是一组类一样。模块可以通过其模块声明（module declaration）中的导出声明（export declaration）显式地导出它的一部分包（按照惯例，这包含在名为 module-info.java 的源文件中）。模块中未被导出的包在模块之外是不可访问的；在模块内部，可访问性不受导出声明的影响。使用模块系统可以在模块内部的包之间共享类，不用让它们对全世界都可见。未导出的包中公有类的公有成员和受保护的成员都提高了两个隐式访问级别，这是正常的公有和受保护级别在模块内部的对等体（intramodular analogues）。对于这种共享的需求相对罕见，经常通过在包内部重新安排类来解决。

与四个主访问级别不同的是，这两个基于模块的级别主要提供咨询。如果把模块的 JAR 文件放在应用程序的类路径下，而不是放在模块路径下，模块中的包就会恢复其非模块的行为：无论包是否通过模块导出，这些包中公有类的所有公有的和受保护的成员将都有正常的可访问性。严格执行新引入的访问级别的一个示例是 JDK 本身：Java 类库中未导出的包在其模块之外确实是不可访问的。

对于传统的 Java 程序员来说，不仅由受限工具的模块提供了访问保护，而且在本质上主要也是提供咨询。为了利用模块的这一特性，必须将包集中到模块中，并在模块声明中显式地表明其所有的依赖关系，重新安排代码结构树，从模块内部采取特殊的动作调解对于非模块化的包的任何访问 。现在说模块将在 JDK 本身之外获得广泛的使用，还为时过早。同时，似乎最好不用它们，除非你的需求非常迫切。



实验总结



总而言之，应该始终尽可能（合理）地降低程序元素的可访问性。在仔细地设计了一个最小的公有 API 之后，应该防止把任何散乱的类、接口或者成员变成 API 的一部分。除了公有静态 final 域的特殊情形之外（此时它们充当常量），公有类都不应该包含公有域，并且要确保公有静态 final 域所引用的对象都是不可变的。



# 实验16	要在公有类中使用访问方法而非公有域

实验介绍



本次实验将为大家介绍我们需要在公有类中使用访问方法而不是在公有域中使用的相关知识点。

#### 知识点

- 在公有类中使用访问方法而非公有域

在公有类中使用访问方法



有时候，可能需要编写一些退化类，它们没有什么作用，只是用来集中实例域：

```java
//Degenerate classes like this should not be public!
class Point{
    public double x;
    public double y;
}
```

由于这种类的数据域是可以被直接访问的，这些类没有提供封装（encapsulation）的功能（详见实验 15）。如果不改变 API，就无法改变它的数据表示法，也无法强加任何约束条件；当域被访问的时候，无法采取任何辅助的行动。坚持面向对象编程的程序员对这种类深恶痛绝，认为应该用包含私有域和公有访问方法（getter）的类代替。对于可变的类来说，应该用公有设值方法（setter）的类代替：

```java
//Encapsulation of data by accessor methods and mutators
class Point{
    private double x;
    private double y;
    public Point(double x，double y){
      this.x=x;
      this.y=y;
}
public double getX(){return x;}
public double getY(){return y;}

public void setX(double x) {this.x=x;}
public void setY(double y) {this.y=y;}

}
```

毫无疑问，说到公有类的时候，坚持面向对象编程思想的看法是正确的：如果类可以在它所在的包之外进行访问，就提供访问方法，以保留将来改变该类的内部表示法的灵活性。如果公有类暴露了它的数据域，要想在将来改变其内部表示法是不可能的，因为公有类的客户端代码已经遍布各处了。

然而，如果类是包级私有的，或者是私有的嵌套类，直接暴露它的数据域并没有本质的错误—假设这些数据域确实描述了该类所提供的抽象。无论是在类定义中，还是在使用该类的客户端代码中，这种方法比访问方法的做法更不容易产生视觉混乱。虽然客户端代码与该类的内部表示法紧密相连，但是这些代码被限定在包含该类的包中。如有必要，也可以不改变包之外的任何代码，而只改变内部数据表示法。在私有嵌套类的情况下，改变的作用范围被进一步限制在外围类中。

Java 平台类库中有几个类违反了“公有类不应该直接暴露数据域”的告诫。显著的例子包括 `java.awt` 包中的 `Point` 类和 `Dimension` 类。它们是不值得仿效的例子，相反，这些类应该被当作反面的警告示例。正如第 67 条所述，决定暴露 `Dimension` 类的内部数据造成了严重的性能问题，而且这个问题至今依然存在。

让公有类直接暴露域虽然从来都不是种好办法，但是如果域是不可变的，这种做法的危害就比较小一些。如果不改变类的 API，就无法改变这种类的表示法，当域被读取的时候，你也无法采取辅助的行动，但是可以强加约束条件。例如，这个类确保了每个实例都表示一个有效的时间：

```java
//Public class with exposed immutable fields-questionable
public final class Time{
    private static final int HOURS_PER_DAY=24;
    private static final int MINUTES_PER_HOUR=60;

    public final int hour;
    public final int minute;

    public Time(int hour，int minute){
    if (hour<0||hour>=HOURS_PER_DAY)
        throw new IllegalArgumentException("Hour:"+hour);
    if (minute<0||minute>=MINUTES_PER_HOUR)
        throw new IllegalArgumentException("Min:"+minute);
    this.hour=hour;
    this.minute=minute;
    }
    ... //Remainder omitted

}
```



实验总结



简而言之，公有类永远都不应该暴露可变的域。虽然还是有问题，但是让公有类暴露不可变的域，其危害相对来说比较小。但有时候会需要用包级私有的或者私有的嵌套类来暴露域，无论这个类是可变的还是不可变的。



# 实验17	使可变性最小化


实验介绍



本次实验将为大家介绍让一个类成为不可变类必须要遵守的规则以及如何构建不可变的类，并且了解了不可变性的优点和缺点

#### 知识点

- 不可变类简介
- 不可变类的优缺点
- 设计不可变类

不可变类简介



不可变类是指其实例不能被修改的类。每个实例中包含的所有信息都必须在创建该实例的时候就提供，并在对象的整个生命周期（lifetime）内固定不变。Java 平台类库中包含许多不可变的类，其中有 String、基本类型的包装类、BigInteger 和 BigDecimal。存在不可变的类有许多理由：不可变的类比可变类更加易于设计、实现和使用。它们不容易出错，且更加安全。

为了使类成为不可变，要遵循下面五条规则：

1. 不要提供任何会修改对象状态的方法（也称为设值方法）。
2. 保证类不会被扩展。这样可以防止粗心或者恶意的子类假装对象的状态已经改变，从而破坏该类的不可变行为。为了防止子类化，一般做法是声明这个类成为 final 的，但是后面我们还会讨论到其他的做法。
3. 声明所有的域都是 final 的。通过系统的强制方式可以清楚地表明你的意图。而且，如果一个指向新创建实例的引用在缺乏同步机制的情况下，从一个线程被传递到另一个线程，就必须确保正确的行为，正如内存模型（memory model）中所述。
4. 声明所有的域都为私有的。这样可以防止客户端获得访问被域引用的可变对象的权限，并防止客户端直接修改这些对象。虽然从技术上讲，允许不可变的类具有公有的 final 域，只要这些域包含基本类型的值或者指向不可变对象的引用，但是不建议这样做，因为这样会使得在以后的版本中无法再改变内部的表示法（详见实验 15 和 16）。
5. 确保对于任何可变组件的互斥访问。如果类具有指向可变对象的域，则必须确保该类的客户端无法获得指向这些对象的引用。并且，永远不要用客户端提供的对象引用来初始化这样的域，也不要从任何访问方法（accessor）中返回该对象引用。在构造器、访问方法和 readObject 方法（详见实验 88）中请使用保护性拷贝（defensive copy）技术（详见实验 50）。

前面实验中的许多例子都是不可变的，其中一个例子是实验 11 中的 PhoneNumber，它针对每个属性都有访问方法（accessor），但是没有对应的设值方法（mutator）。下面是个稍微复杂一点的例子：

```java
// Immutable complex number class (Pages 81-82)
public final class Complex {
    private final double re;
    private final double im;

    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE  = new Complex(1, 0);
    public static final Complex I    = new Complex(0, 1);

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double realPart()      { return re; }
    public double imaginaryPart() { return im; }

    public Complex plus(Complex c) {
        return new Complex(re + c.re, im + c.im);
    }

    // Static factory, used in conjunction with private constructor (Page 85)
    public static Complex valueOf(double re, double im) {
        return new Complex(re, im);
    }

    public Complex minus(Complex c) {
        return new Complex(re - c.re, im - c.im);
    }

    public Complex times(Complex c) {
        return new Complex(re * c.re - im * c.im,
                re * c.im + im * c.re);
    }

    public Complex dividedBy(Complex c) {
        double tmp = c.re * c.re + c.im * c.im;
        return new Complex((re * c.re + im * c.im) / tmp,
                (im * c.re - re * c.im) / tmp);
    }

    @Override public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Complex))
            return false;
        Complex c = (Complex) o;

        // See page 47 to find out why we use compare instead of ==
        return Double.compare(c.re, re) == 0
                && Double.compare(c.im, im) == 0;
    }
    @Override public int hashCode() {
        return 31 * Double.hashCode(re) + Double.hashCode(im);
    }

    @Override public String toString() {
        return "(" + re + " + " + im + "i)";
    }
}
```

这个类表示一个复数（complex number，具有实部和虚部）。除了标准的 Object 方法之外，它还提供了针对实部和虚部的访问方法，以及 4 种基本的算术运算：加法、减法、乘法和除法。注意这些算术运算如何创建并返回新的 Complex 实例，而不是修改这个实例。大多数重要的不可变类都使用了这种模式。它被称为函数的（functional）方法，因为这些方法返回了一个函数的结果，这些函数对操作数进行运算但并不修改它。与之相对应的更常见的是过程的（procedural）或者命令式的（imperative）方法，使用这些方法时，将一个过程作用在它们的操作数上，会导致它的状态发生改变。注意，这些方法名称都是介词（如 plus），而不是动词（如 add）。这是为了强调该方法不会改变对象的值。BigInteger 类和 BigDecimal 类由于没有遵守这一命名习惯，就导致了许多用法错误。



不可变类的优缺点



如果你对函数方式的做法还不太熟悉，可能会觉得它显得不太自然，但是它带来了不可变性，具有许多优点。不可变对象比较简单。不可变对象可以只有一种状态，即被创建时的状态。如果你能够确保所有的构造器都建立了这个类的约束关系，就可以确保这些约束关系在整个生命周期内永远不再发生变化，你和使用这个类的程序员都无须再做额外的工作来维护这些约束关系。另一方面，可变的对象可以有任意复杂的状态空间。如果文档中没有为设值方法所执行的状态转换提供精确的描述，要可靠地使用可变类是非常困难的，甚至是不可能的。

不可变对象本质上是线程安全的，它们不要求同步。当多个线程并发访问这样的对象时，它们不会遭到破坏。这无疑是获得线程安全最容易的办法。实际上，没有任何线程会注意到其他线程对于不可变对象的影响。所以，不可变对象可以被自由地共享。不可变类应该充分利用这种优势，鼓励客户端尽可能地重用现有的实例。要做到这一点，一个很简便的办法就是：对于频繁用到的值，为它们提供公有的静态 final 常量。例如，Complex 类有可能会提供下面的常量：

```java
public static final Complex ZERO = new Complex(0,0);
public static final Complex ONE = new Complex(1,0);
public static final Complex I = new Complex(0,1);
```

这种方法可以被进一步扩展。不可变的类可以提供一些静态工厂（详见实验 1），它们把频繁被请求的实例缓存起来，从而当现有实例可以符合请求的时候，就不必创建新的实例。所有基本类型的包装类和 BigInteger 都有这样的静态工厂。使用这样的静态工厂也使得客户端之间可以共享现有的实例，而不用创建新的实例，从而降低内存占用和垃圾回收的成本。在设计新的类时，选择用静态工厂代替公有的构造器可以让你以后有添加缓存的灵活性，而不必影响客户端。

“不可变对象可以被自由地共享”导致的结果是，永远也不需要进行保护性拷贝（defensive copy）（详见实验 50）。实际上，你根本无须做任何拷贝，因为这些拷贝始终等于原始的对象。因此，你不需要，也不应该为不可变的类提供 clone 方法或者拷贝构造器（详见实验 13）。这一点在 Java 平台的早期并不好理解，所以 String 类仍然具有拷贝构造器，但是应该尽量少用它（详见实验 6）。

不仅可以共享不可变对象，甚至也可以共享它们的内部信息。例如，BigInteger 类内部使用了符号数值表示法。符号用一个 int 类型的值来表示，数值则用一个 int 数组表示。negate 方法产生一个新的 BigInteger，其中数值是一样的，符号则是相反的。它并不需要拷贝数组，新建的 BigInteger 也指向原始实例中的同一个内部数组。

不可变对象为其他对象提供了大量的构件，无论是可变的还是不可变的对象。如果知道一个复杂对象内部的组件对象不会改变，要维护它的不变性约束是比较容易的。这条原则的一种特例在于，不可变对象构成了大量的映射键（map key）和集合元素（set element）；一旦不可变对象进入到映射（map）或者集合（set）中，尽管这破坏了映射或者集合的不变性约束，但是也不用担心它们的值会发生变化。

不可变对象无偿地提供了失败的原子性（详见实验 76 ）。它们的状态永远不变，因此不存在临时不一致的可能性。

不可变类真正唯一的缺点是，对于每个不同的值都需要一个单独的对象。创建这些对象的代价可能很高，特别是大型的对象。例如，假设你有一个上百万位的 BigInteger，想要改变它的低位：

```java
BigInteger moby = ...;
moby=moby.flipBit(0);
```

flipBit 方法创建了一个新的 BigInteger 实例，也有上百万位长，它与原来的对象只差一位不同。这项操作所消耗的时间和空间与 BigInteger 的成正比。我们拿它与 java.util.BitSet 进行比较。与 BigInteger 类似，BitSet 代表一个任意长度的位序列，但是与 BigInteger 不同的是，BitSet 是可变的。BitSet 类提供了一个方法，允许在固定时间（constant time）内改变此“百万位”实例中单个位的状态：

```java
BitSet moby = ...;
moby.flip(0);
```

如果你执行一个多步骤的操作，并且每个步骤都会产生一个新的对象，除了最后的结果之外，其他的对象最终都会被丢弃，此时性能问题就会显露出来。处理这种问题有两种办法。第一种办法，先猜测一下经常会用到哪些多步骤的操作，然后将它们作为基本类型提供。如果某个多步骤操作已经作为基本类型提供，不可变的类就无须在每个步骤单独创建一个对象。不可变的类在内部可以更加灵活。例如，BigInteger 有一个包级私有的可变“配套类”（companing class），它的用途是加速诸如“模指数”（modular exponentiation）这样的多步骤操作。由于前面提到的诸多原因，使用可变的配套类比使用 BigInteger 要困难得多，但幸运的是，你并不需要这样做。因为 BigInteger 的实现者已经替你完成了所有的困难工作。

如果能够精确地预测出客户端将要在不可变的类上执行哪些复杂的多阶段操作，这种包级私有的可变配套类的方法就可以工作得很好。如果无法预测，最好的办法是提供一个公有的可变配套类。在 Java 平台类库中，这种方法的主要例子是 String 类，它的可变配套类是 StringBuilder（及其已经被废弃的祖先 StringBuffer）。



私有构造器设计不可变类



现在你已经知道了如何构建不可变的类，并且了解了不可变性的优点和缺点，现在我们来讨论其他的一些设计方案。前面提到过，为了确保不可变性，类绝对不允许自身被子类化。除了“使类成为 final 的”这种方法之外，还有另外一种更加灵活的办法可以做到这一点。不可变的类变成 final 的另一种办法就是，让类的所有构造器都变成私有的或者包级私有的，并添加公有的静态工厂（static factory）来代替公有的构造器（详见实验 1）。为了具体说明这种方法，下面以 Complex 为例，看看如何使用这种方法：

```java
//Immutable class with static factories instead of constructors
public class Complex{
    private final double re;
    private final double im;

    private Complex(double re，double im){
        this.re = re;
        this.im = im;
    }
    public static Complex value0f(double re，double im){
        return new Complex(re，im);
    }
    …//Remainder unchanged
}
```

这种方法虽然并不常用，但它通常是最好的替代方法。它最灵活，因为它允许使用多个包级私有的实现类。对于处在包外部的客户端而言，不可变的类实际上是 final 的，因为不可能对来自另一个包的类、缺少公有的或受保护的构造器的类进行扩展。除了允许多个实现类的灵活性之外，这种方法还使得有可能通过改善静态工厂的对象缓存能力，在后续的发行版本中改进该类的性能。

当 BigInteger 和 BigDecimal 刚被编写出来的时候，对于“不可变的类必须为 final”的说法还没有得到广泛的理解，所以它们的所有方法都有可能被覆盖。遗憾的是，为了保持向后兼容，这个问题一直无法得以修正。如果你在编写一个类，它的安全性依赖于来自不可信客户端的 BigInteger 或者 BigDecimal 参数的不可变性，就必须进行检查，以确定这个参数是否为“真正的”BigInteger 或者 BigDecimal，而不是不可信任子类的实例。如果是后者，就必须在假设它可能是可变的前提下对它进行保护性拷贝（详见实验 50）：

```java
public static BigInteger safeInstance(BigInteger val){
    return val .getClass()==BigInteger.class?
        val:new BigInteger(val .toByteArray());
}
```

本实验开头关于不可变类的诸多规则指出，没有方法会修改对象，并且它的所有域都必须是 final 的。实际上，这些规则比真正的要求更强硬了一点，为了提高性能可以有所放松。事实上应该是这样：没有一个方法能够对对象的状态产生外部可见（externally visible）的改变。然而，许多不可变的类拥有一个或者多个非 final 的域，它们在第一次被请求执行这些计算的时候，把一些开销昂贵的计算结果缓存在这些域中。如果将来再次请求同样的计算，就直接返回这些缓存的值，从而节约了重新计算所需要的开销。这种技巧可以很好地工作，因为对象是不可变的，它的不可变性保证了这些计算如果被再次执行，就会产生同样的结果。

例如，PhoneNumber 类的 hashCode 方法（详见实验 11）在第一次被调用的时候，计算出散列码，然后把它缓存起来，以备将来被再次调用时使用。这种方法是延迟初始化（lazy initialization）（详见实验 83）的一个例子，String 类也用到了。

有关序列化功能的一条告诫有必要在这里提出来。如果你选择让自己的不可变类实现 Serializable 接口，并且它包含一个或者多个指向可变对象的域，就必须提供一个显式的 read-Object 或者 readResolve 方法，或者使用 ObjectOutputStream.writeUnshared 和 ObjectInputStream.readUnshared 方法，即便默认的序列化形式是可以接受的，也是如此。否则，攻击者可能从不可变的类创建可变的实例。关于这个话题的详情请参见实验 88。

对于某些类而言，其不可变性是不切实际的。如果类不能被做成不可变的，仍然应该尽可能地限制它的可变性。降低对象可以存在的状态数，可以更容易地分析该对象的行为，同时降低出错的可能性。因此，除非有令人信服的理由使域变成非 final 的，否则让每个域都是 final 的。结合本实验的建议和实验 15 的建议，你自然倾向于：除非有令人信服的理由要使域变成是非 final 的，否则要使每个域都是 private final 的。

构造器应该创建完全初始化的对象，并建立起所有的约束关系。不要在构造器或者静态工厂之外再提供公有的初始化方法，除非有令人信服的理由必须这么做。同样地，也不应该提供“重新初始化”方法（它使得对象可以被重用，就好像这个对象是由另一不同的初始状态构造出来的一样）。与所增加的复杂性相比，“重新初始化”方法通常并没有带来太多的性能优势。

通过 CountDownLatch 类的例子可以说明这些原则。它是可变的，但是它的状态空间被有意地设计得非常小。比如创建一个实例，只使用一次，它的任务就完成了：一旦定时器的计数达到零，就不能重用了。

最后值得注意的一点与本实验中的 Complex 类有关。这个例子只是被用来演示不可变性的，它不是一个工业强度的复数实现。它对复数乘法和除法使用标准的计算公式，会进行不正确的四舍五入，并且对复数 NaN 和无穷大也没有提供很好的语义。



实验总结



总之，坚决不要为每个 get 方法编写一个相应的 set 方法。除非有很好的理由要让类成为可变的类，否则它就应该是不可变的。不可变的类有许多优点，唯一的缺点是在特定的情况下存在潜在的性能问题。你应该总是使一些小的值对象，成为不可变的。（在 Java 平台类库中，有几个类如 java.util.Date 和 java.awt.Point，它们本应该是不可变的，但实际上却不是。）你也应该认真考虑把一些较大的值对象做成不可变的，例如 String 和 BigInteger。只有当你确认有必要实现令人满意的性能时（详见实验 67），才应该为不可变的类提供公有的可变配套类。



# 实验18	复合优先于继承

实验介绍



本次实验将介绍实现代码重用的两种工具：继承和复合，并分析们的优劣点。

#### 知识点

- 继承
- 复合

继承



继承（inheritance）是实现代码重用的有力手段，但它并非永远是完成这项工作的最佳工具。使用不当会导致软件变得很脆弱。在包的内部使用继承是非常安全的，在那里子类和超类的实现都处在同一个程序员的控制之下。对于专门为了继承而设计并且具有很好的文档说明的类来说（详见实验 19 ），使用继承也是非常安全的。然而，对普通的具体类（concrete class）进行跨越包边界的继承，则是非常危险的。提示一下，这里使用“继承”一词，含义是实现继承（当一个类扩展另一个类的时候）。本实验中讨论的问题并不适用于接口继承（当一个类实现一个接口的时候，或者当一个接口扩展另一个接口的时候）。

与方法调用不同的是，继承打破了封装性。换句话说，子类依赖于其超类中特定功能的实现细节。超类的实现有可能会随着发行版本的不同而有所变化，如果真的发生了变化，子类可能会遭到破坏，即使它的代码完全没有改变。因而，子类必须要跟着其超类的更新而演变，除非超类是专门为了扩展而设计的，并且具有很好的文档说明。

为了说明得更加具体一点，我们假设有一个程序使用了 HashSet。为了调优该程序的性能，需要查询 HashSet，看一看自从它被创建以来添加了多少个元素（不要与它当前的元素数目混淆起来，它会随着元素的删除而递减）。为了提供这种功能，我们得编写一个 HashSet 变体，定义记录试图插入的元素的数量 addCount，并针对该计数值导出一个访问方法。 `HashSet` 类包含两个可以增加元素的方法：`add` 和 `addAll`，因此这两个方法都要被覆盖：

```java
import java.util.*;

// Broken - Inappropriate use of inheritance! (Page 87)
public class InstrumentedHashSet<E> extends HashSet<E> {
    // The number of attempted element insertions
    private int addCount = 0;

    public InstrumentedHashSet() {
    }

    public InstrumentedHashSet(int initCap, float loadFactor) {
        super(initCap, loadFactor);
    }

    @Override public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    @Override public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }


}
```

这个类看起来非常合理，但是它并不能正常工作。假设我们创建了一个实例，并利用 `addAll` 方法添加了三个元素。顺便提一句，注意我们利用静态工厂方法 `List.of` 创建了一个列表，该方法是在 Java 9 中增加的。如果使用较早的版本，则用 Arrays.asList 代替：

```java
InstrumentedHashSet<String> s = new InstrumentedHashSet<>();
        s.addAll(List.of("Snap", "Crackle", "Pop"));
```

此时我们期望 getAddCount 方法能返回 3，但是实际上它返回的是 6。哪里出错了呢？在 HashSet 的内部，addAll 方法是基于它的 add 方法来实现的，即使 HashSet 的文档中并没有说明这样的实现细节，这也是合理的。InstrumentedHashSet 中的 addAll 方法首先给 addCount 增加 3，然后利用 supper.addAll 来调用 HashSet 的 addAll 实现。然后又依次调用到被 InstrumentedHashSet 覆盖了的 add 方法，每个元素调用一次。这三次调用又分别给 addCount 加了 1，所以总共增加了 6：通过 addAll 方法增加的每个元素都被计算了两次。

我们只要去掉被覆盖的 addAll 方法，就可以“修正”这个子类。虽然这样得到的类可以正常工作，但是它的功能正确性则需要依赖于这样的事实：HashSet 的 addAll 方法是在它的 add 方法上实现的。这种“自用性”（self-use）是实现细节，不是承诺，不能保证在 Java 平台的所有实现中都保持不变，不能保证随着发行版本的不同而不发生变化。因此，这样得到的 InstrumentedHashSet 类将是非常脆弱的。

稍微好一点的做法是，覆盖 addAll 方法来遍历指定的集合，为每个元素调用一次 add 方法。这样做可以保证得到正确的结果，不管 HashSet 的 addAll 方法是否在 add 方法的基础上实现，因为 HashSet 的 addAll 实现将不会再被调用到。然而，这项技术并没有解决所有的问题，它相当于重新实现了超类的方法，这些超类的方法可能是自用的，也可能不是，这种方法很困难，也非常耗时，容易出错，并且可能降低性能。此外，这样做并不总是可行的，因为无法访问对于子类来说是私有的域，所以有些方法就无法实现。

导致子类脆弱的一个相关的原因是，它们的超类在后续的发行版本中可以获得新的方法。假设一个程序的安全性依赖于这样的事实：所有被插入某个集合中的元素都满足某个先决条件。下面的做法就可以确保这一点：对集合进行子类化，并覆盖所有能够添加元素的方法，以便确保在加入每个元素之前它是满足这个先决条件的。如果在后续的发行版本中，超类中没有增加能插入元素的新方法，这种做法就可以正常工作。然而，一旦超类增加了这样的新方法，则很可能仅仅由于调用了这个未被子类覆盖的新方法，而将“非法的”元素添加到子类的实例中。这不是一个纯粹的理论问题。在把 Hashtable 和 Vector 加入到 Collections Framework 中的时候，就修正了几个这类性质的安全漏洞。

上面这两个问题都来源于覆盖（overriding）方法。你可能会认为在扩展一个类的时候，仅仅增加新的方法，而不覆盖现有的方法是安全的。虽然这种扩展方式比较安全一些，但是也并非完全没有风险。如果超类在后续的发行版本中获得了一个新的方法，并且不幸的是，你给子类提供了一个签名相同但返回类型不同的方法，那么这样的子类将无法通过编译。如果给子类提供的方法带有与新的超类方法完全相同的签名和返回类型，实际上就覆盖了超类中的方法，因此又回到了上述两个问题。此外，你的方法是否能够遵守新的超类方法的约定，这也是很值得怀疑的，因为当你在编写子类方法的时候，这个约定压根还没有面世。



复合



幸运的是，有一种办法可以避免前面提到的所有问题。即不扩展现有的类，而是在新的类中增加一个私有域，它引用现有类的一个实例。这种设计被称为“复合”（composition），因为现有的类变成了新类的一个组件。新类中的每个实例方法都可以调用被包含的现有类实例中对应的方法，并返回它的结果。这被称为转发（forwarding），新类中的方法被称为转发方法（forwarding method）。这样得到的类将会非常稳固，它不依赖于现有类的实现细节。即使现有的类添加了新的方法，也不会影响新的类。为了进行更具体的说明，请看下面的例子，它用复合 / 转发的方法来代替 InstrumentedHashSet 类。注意这个实现分为两部分：类本身和可重用的转发类（forwarding class），其中包含了所有的转发方法，没有任何其他的方法：

```java
import java.util.*;

// Wrapper class - uses composition in place of inheritance
public class InstrumentedSet<E> extends ForwardingSet<E> {
    private int addCount = 0;

    public InstrumentedSet(Set<E> s) {
        super(s);
    }

    @Override public boolean add(E e) {
        addCount++;
        return super.add(e);
    }
    @Override public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }
    public int getAddCount() {
        return addCount;
    }

}

// Reusable forwarding class
public class ForwardingSet<E> implements Set<E> {
    private final Set<E> s;
    public ForwardingSet(Set<E> s) { this.s = s; }

    public void clear()               { s.clear();            }
    public boolean contains(Object o) { return s.contains(o); }
    public boolean isEmpty()          { return s.isEmpty();   }
    public int size()                 { return s.size();      }
    public Iterator<E> iterator()     { return s.iterator();  }
    public boolean add(E e)           { return s.add(e);      }
    public boolean remove(Object o)   { return s.remove(o);   }
    public boolean containsAll(Collection<?> c)
                                   { return s.containsAll(c); }
    public boolean addAll(Collection<? extends E> c)
                                   { return s.addAll(c);      }
    public boolean removeAll(Collection<?> c)
                                   { return s.removeAll(c);   }
    public boolean retainAll(Collection<?> c)
                                   { return s.retainAll(c);   }
    public Object[] toArray()          { return s.toArray();  }
    public <T> T[] toArray(T[] a)      { return s.toArray(a); }
    @Override public boolean equals(Object o)
                                       { return s.equals(o);  }
    @Override public int hashCode()    { return s.hashCode(); }
    @Override public String toString() { return s.toString(); }
}
```

Set 接口的存在使得 InstrumentedSet 类的设计成为可能，因为 Set 接口保存了 HashSet 类的功能特性。除了获得健壮性之外，这种设计也带来了更多的灵活性。InstrumentedSet 类实现了 Set 接口，并且拥有单个构造器，它的参数也是 Set 类型。从本质上讲，这个类把一个 Set 转变成了另一个 Set，同时增加了计数的功能。前面提到的基于继承的方法只适用于单个具体的类，并且对于超类中所支持的每个构造器都要求有一个单独的构造器，与此不同的是，这里的包装类（wrapper class）可以被用来包装任何 Set 实现，并且可以结合任何先前存在的构造器一起工作：

```java
Set<Instant> times=new InstrumentedSet<>(new TreeSet<>(cmp));
Set<E> s=new InstrumentedSet<>(new HashSet<>(INIT_CAPACITY));
```

InstrumentedSet 类甚至也可以用来临时替换一个原本没有计数特性的 Set 实例

```java
static void walk(Set<Dog> dogs){
    InstrumentedSet<Dog> iDogs = new InstrumentedSet<>(dogs);
      …//Within this method use iDogs instead of dogs
}
```

因为每一个 InstrumentedSet 实例都把另一个 Set 实例包装起来了，所以 InstrumentedSet 类被称为包装类（wrapper class）。这也正是 Decorator（修饰者）模式，因为 InstrumentedSet 类对一个集合进行了修饰，为它增加了计数特性。有时复合和转发的结合也被宽松地称为“委托”（delegation）。从技术的角度而言，这不是委托，除非包装对象把自身传递给被包装的对象。

包装类几乎没有什么缺点。需要注意的一点是，包装类不适合用于回调框架（callback framework）；在回调框架中，对象把自身的引用传递给其他的对象，用于后续的调用（“回调”）。因为被包装起来的对象并不知道它外面的包装对象，所以它传递一个指向自身的引用（this），回调时避开了外面的包装对象。这被称为 SELF 问题。有些人担心转发方法调用所带来的性能影响，或者包装对象导致的内存占用。在实践中，这两者都不会造成很大的影响。编写转发方法倒是有点琐碎，但是只需要给每个接口编写一次构造器，转发类则可以通过包含接口的包提供。例如，Guava 就为所有的集合接口提供了转发类。

只有当子类真正是超类的子类型（subtype）时，才适合用继承。换句话说，对于两个类 A 和 B，只有当两者之间确实存在“ is-a”关系的时候，类 B 才应该扩展类 A。如果你打算让类 B 扩展类 A，就应该问问自己：每个 B 确实也是 A 吗？如果你不能够确定这个问题的答案是肯定的，那么 B 就不应该扩展 A。如果答案是否定的，通常情况下，B 应该包含 A 的一个私有实例，并且暴露一个较小的、较简单的 API ：A 本质上不是 B 的一部分，只是它的实现细节而已。

在 Java 平台类库中，有许多明显违反这条原则的地方。例如，栈（stack）并不是向量（vector），所以 Stack 不应该扩展 Vector。同样地，属性列表也不是散列表，所以 Properties 不应该扩展 Hashtable。在这两种情况下，复合模式才是恰当的。

如果在适合使用复合的地方使用了继承，则会不必要地暴露实现细节。这样得到的 API 会把你限制在原始的实现上，永远限定了类的性能。更为严重的是，由于暴露了内部的细节，客户端就有可能直接访问这些内部细节。这样至少会导致语义上的混淆。例如，如果 p 指向 Properties 实例，那么 p.getProperty(key) 就有可能产生与 p.get(key) 不同的结果：前一个方法考虑了默认的属性表，而后一个方法则继承自 Hashtable，没有考虑默认的属性列表。最严重的是，客户有可能直接修改超类，从而破坏子类的约束条件。在 Properties 的情形中，设计者的目标是只允许字符串作为键（key）和值（value），但是直接访问底层的 Hashtable 就允许违反这种约束条件。一旦违反了约束条件，就不可能再使用 Properties API 的其他部分（load 和 store）了。等到发现这个问题时，要改正它已经太晚了，因为客户端依赖于使用非字符串的键和值了。

在决定使用继承而不是复合之前，还应该问自己最后一组问题。对于你正试图扩展的类，它的 API 中有没有缺陷呢？如果有，你是否愿意把那些缺陷传播到类的 API 中？继承机制会把超类 API 中的所有缺陷传播到子类中，而复合则允许设计新的 API 来隐藏这些缺陷。



实验总结



简而言之，继承的功能非常强大，但是也存在诸多问题，因为它违背了封装原则。只有当子类和超类之间确实存在子类型关系时，使用继承才是恰当的。即便如此，如果子类和超类处在不同的包中，并且超类并不是为了继承而设计的，那么继承将会导致脆弱性（fragility）。为了避免这种脆弱性，可以用复合和转发机制来代替继承，尤其是当存在适当的接口可以实现包装类的时候。包装类不仅比子类更加健壮，而且功能也更加强大。



# 实验19	要么设计继承并提供文档说明，要么禁止继承

实验介绍



本次实验将详细阐述设计继承类应该提供的文档说明要求以及意义，并阐述了为什么需要通过将类声明为 final，或者确保没有可访问的构造器来禁止类被继承的原因。

#### 知识点

- 类的说明文档
- 禁止继承

说明文档



实验 18 提醒过我们：对于不是为了继承而设计并且没有文档说明的“外来”类进行子类化是多么危险。那么对于专门为了继承而设计并且具有良好文档说明的类而言，这又意味着什么呢？

首先，该类的文档必须精确地描述覆盖每个方法所带来的影响。换句话说，该类必须有文档说明它可覆盖（overridable）的方法的自用性（self-use）。对于每个公有的或受保护的方法或者构造器，它的文档必须指明该方法或者构造器调用了哪些可覆盖的方法，是以什么顺序调用的，每个调用的结果又是如何影响后续处理过程的（所谓可覆盖（overridable）的方法，是指非 final 的、公有的或受保护的）。更广义地说，即类必须在文档中说明，在哪些情况下它会调用可覆盖的方法。例如，后台的线程或者静态的初始化器（initializer）可能会调用这样的方法。

如果方法调用到了可覆盖的方法，在它的文档注释的末尾应该包含关于这些调用的描述信息。这段描述信息是规范的一个特殊部分，写着：`Implementation Requirements`（实现要求……），它由 Javadoc 标签 @implSpec 生成。这段话描述了该方法的内部工作情况。下面举个例子，摘自 `java.util.AbstractCollection` 的规范：

```java
public boolean remove(Object o)
```

（如果这个集合中存在指定的元素，就从中删除该指定元素中的单个实例（这是项可选的操作）。更广义地说，即如果集合中包含一个或者多个这样的元素 e，就从中删除掉一个，如 `Objects.equals(o,e)`。如果集合中包含指定的元素，就返回 true（如果调用的结果改变了集合，也是一样）。

实现要求：该实现遍历整个集合来查找指定的元素。如果它找到该元素，将会利用迭代器的 `remove` 方法将之从集合中删除。注意，如果由该集合的 `iterator` 方法返回的迭代器没有实现 `remove` 方法，该实现就会抛出 `UnsupportedOperationException`。）

这份文档清楚地说明了，覆盖 iterator 方法将会影响 remove 方法的行为。而且，它确切地描述了 iterator 方法返回的 Iterator 的行为将会怎样影响 remove 方法的行为。与此相反的是，在 实验 18 的情形中，程序员在子类化 HashSet 的时候，无法说明覆盖 add 方法是否会影响 addAll 方法的行为。

关于程序文档有句格言：好的 API 文档应该描述一个给定的方法做了什么工作，而不是描述它是如何做到的。那么，上面这种做法是否违背了这句格言呢？是的，它确实违背了！这正是继承破坏了封装性所带来的不幸后果。所以，为了设计一个类的文档，以便它能够被安全地子类化，你必须描述清楚那些有可能未定义的实现细节。

@implSpec 标签是在 Java 8 中增加的，在 Java 9 中得到了广泛应用。这个标签应该是默认可用的，但是到 Java 9，Javadoc 工具仍然把它忽略，除非传入命令行参数：`-tag "apiNote:a:API Note:"`。为了继承而进行的设计不仅仅涉及自用模式的文档设计。为了使程序员能够编写出更加有效的子类，而无须承受不必要的痛苦，类必须以精心挑选的受保护的（protected）方法的形式，提供适当的钩子（hook），以便进入其内部工作中。这种形式也可以是罕见的实例，或者受保护的域。例如，以 `java.util.AbstractList` 中的 `removeRange` 方法为例：

```java
protected void removeRange(int fromIndex, int toIndex)
```

（从列表中删除所有索引处于 fromIndex（含）和 toIndex（不含）之间的元素。将所有符合条件的元素移到左边（减小它们索引）。这一调用将从 ArrayList 中删除从 toIndex 到 fromIndex 之间的元素。（如果 toIndex == fromIndex，这项操作就无效。）

这个方法是通过 clear 操作在这个列表及其子列表中调用的。覆盖这个方法来利用列表实现的内部信息，可以充分地改善这个列表及其子列表中的 clear 操作的性能。

实现要求：这项实现获得了一个处在 fromIndex 之前的列表迭代器，并依次地重复调用 `ListIterator.next` 和 `ListIterator.remove`，直到整个范围都被移除为止。注意：如果 ListIterator.remove 需要线性的时间，该实现就需要平方级的时间。

参数：

- fromIndex 要移除的第一个元素的索引。
- toIndex 要移除的最后一个元素之后的索引。

这个方法对于 List 实现的最终用户并没有意义。提供该方法的唯一目的在于，使子类更易于提供针对子列表（sublist）的快速 clear 方法。如果没有 removeRange 方法，当在子列表（sublist）上调用 clear 方法时，子类将不得不用平方级的时间来完成它的工作。否则，就得重新编写整个 subList 机制—这可不是一件容易的事情！

因此，当你为了继承而设计类的时候，如何决定应该暴露哪些受保护的方法或者域呢？遗憾的是，并没有什么神奇的法则可供你使用。你所能做到的最佳途径就是努力思考，发挥最好的想象，然后编写一些子类进行测试。你应该尽可能地少暴露受保护的成员，因为每个方法或者域都代表了一项关于实现细节的承诺。另一方面，你又不能暴露得太少，因为漏掉的受保护方法可能会导致这个类无法被真正用于继承。

对于为了继承而设计的类，唯一的测试方法就是编写子类。如果遗漏了关键的受保护成员，尝试编写子类就会使遗漏所带来的痛苦变得更加明显。相反，如果编写了多个子类，并且无一使用受保护的成员，或许就应该把它做成私有的。经验表明，3 个子类通常就足以测试一个可扩展的类。除了超类的程序设计者之外，都需要编写一个或者多个这种子类。

在为了继承而设计有可能被广泛使用的类时，必须要意识到，对于文档中所说明的自用模式（self-use pattern），以及对于其受保护方法和域中所隐含的实现策略，你实际上已经做出了永久的承诺。这些承诺使得你在后续的版本中提高这个类的性能或者增加新功能都变得非常困难，甚至不可能。因此，必须在发布类之前先编写子类对类进行测试。

还要注意，因继承而需要的特殊文档会打乱正常的文档信息，正常的文档信息是被设计用来让程序员可以创建该类的实例，并调用类中的方法。在编写本书之时，几乎还没有适当的工具或者注释规范，能够把“普通的 API 文档”与“专门针对实现子类的程序员的信息”区分开。



禁止继承



为了允许继承，类还必须遵守其他一些约束。构造器决不能调用可被覆盖的方法，无论是直接调用还是间接调用。如果违反了这条规则，很有可能导致程序失败。超类的构造器在子类的构造器之前运行，所以，子类中覆盖版本的方法将会在子类的构造器运行之前先被调用。如果该覆盖版本的方法依赖于子类构造器所执行的任何初始化工作，该方法将不会如预期般执行。为了更加直观地说明这一点，下面举个例子，其中有个类违反了这条规则：

```java
public class Super{
    //Broken-constructor invokes an overridable method
    public Super(){
          overrideMe();
      }
    public void overrideMe(){
      }
}
```

下面的子类覆盖了方法 overrideMe，Super 唯一的构造器就错误地调用了这个方法：

```java
public final class
    //Blank final，set by constructor
    private final Instant instant;

    Sub(){
        instant=Instant.now();
    }
    //Overriding method invoked by superclass constructor
    @Override public void overrideMe(){
        System .out .println(instant);
    }
    public static void main (String[] args){
        Sub sub=new Sub();
        sub.overrideMe();
    }
}
```

你可能会期待这个程序会打印两次日期，但是它第一次打印出的是 null，因为 overrideMe 方法被 Super 构造器调用的时候，构造器 Sub 还没有机会初始化 instant 域。注意，这个程序观察到的 final 域处于两种不同的状态。还要注意，如果 overrideMe 已经调用了 instant 中的任何方法，当 Super 构造器调用 overrideMe 的时候，调用就会抛出 NullPointerException 异常。如果该程序没有抛出 NullPointerException 异常，唯一的原因就在于 println 方法可以容忍 null 参数。

注意，通过构造器调用私有的方法、final 方法和静态方法是安全的，这些都不是可以被覆盖的方法。

在为了继承而设计类的时候，Cloneable 和 Serializable 接口出现了特殊的困难。如果类是为了继承而设计的，无论实现这其中的哪个接口通常都不是个好主意，因为它们把一些实质性的负担转嫁到了扩展这个类的程序员身上。然而，你还是可以采取一些特殊的手段，允许子类实现这些接口，无须强迫子类的程序员去承受这些负担。实验 13 和实验 86 中会讲解这些特殊的手段。

如果你决定在一个为了继承而设计的类中实现 Cloneable 或者 Serializable 接口，就应该意识到，因为 clone 和 readObject 方法在行为上非常类似于构造器，所以类似的限制规则也是适用的：无论是 clone 还是 readObject，都不可以调用可覆盖的方法，不管是以直接还是间接的方式。对于 readObject 方法，覆盖的方法将在子类的状态被反序列化（deserialized）之前先被运行；而对于 clone 方法，覆盖的方法则是在子类的 clone 方法有机会修正被克隆对象的状态之前先被运行。无论哪种情形，都不可避免地将导致程序失败。在 clone 方法的情形中，这种失败可能会同时损害到原始的对象以及被克隆的对象本身。例如，如果覆盖版本的方法假设它正在修改对象深层结构的克隆对象的备份，就会发生这种情况，但是该备份还没有完成。

最后，如果你决定在一个为了继承而设计的类中实现 Serializable 接口，并且该类有一个 readResolve 或者 writeReplace 方法，就必须使 readResolve 或者 writeReplace 成为受保护的方法，而不是私有的方法。如果这些方法是私有的，那么子类将会不声不响地忽略掉这两个方法。这正是“为了允许继承，而把实现细节变成一个类的 API 的一部分”的 另一种情形。

到现在为止，结论应该很明显了：为了继承而设计类，对这个类会有一些实质性的限制。这并不是很轻松就可以承诺的决定。在某些情况下，这样的决定很明显是正确的，比如抽象类，包括接口的骨架实现（skeletal implementation）（详见实验 20）。但是，在另外一些情况下，这样的决定却很明显是错误的，比如不可变类（详见实验 17）。

但是，对于普通的具体类应该怎么办呢？它们既不是 final 的，也不是为了子类化而设计和编写文档的，所以这种状况很危险。每次对这种类进行修改，从这个类扩展得到的客户类就有可能遭到破坏。这不仅仅是个理论问题。对于一个并非为了继承而设计的非 final 具体类，在修改了它的内部实现之后，接收到与子类化相关的错误报告也并不少见。

这个问题的最佳解决方案是，对于那些并非为了安全地进行子类化而设计和编写文档的类，要禁止子类化。有两种办法可以禁止子类化。比较容易的办法是把这个类声明为 final 的。另一种办法是把所有的构造器都变成私有的，或者包级私有的，并增加一些公有的静态工厂来替代构造器。后一种办法在实验 7 中讨论过，它为内部使用子类提供了灵活性。这两种办法都是可以接受的。

这条建议可能会引来争议，因为许多程序员已经习惯于对普通的具体类进行子类化，以便增加新的功能设施，比如仪表功能（如计数显示等）、通知机制或者同步功能，或者为了限制原有类中的功能。如果类实现了某个能够反映其本质的接口，比如 Set、List 或者 Map，就不应该为了禁止子类化而感到后悔。实验 18 中介绍的包装类（wrapper class）模式还提供了另一种更好的办法，让继承机制实现更多的功能。

如果具体的类没有实现标准的接口，那么禁止继承可能会给某些程序员带来不便。如果你认为必须允许从这样的类继承，一种合理的办法是确保这个类永远不会调用它的任何可覆盖的方法，并在文档中说明这一点。换句话说，完全消除这个类中可覆盖方法的自用特性。这样做之后，就可以创建“能够安全地进行子类化”的类。覆盖方法将永远不会影响其他任何方法的行为。

你可以机械地消除类中可覆盖方法的自用特性，而不改变它的行为。将每个可覆盖方法的代码体移到一个私有的“辅助方法”（helper method）中，并且让每个可覆盖的方法调用它的私有辅助方法。然后用“直接调用可覆盖方法的私有辅助方法”来代替“可覆盖方法的每个自用调用”。



实验总结



简而言之，专门为了继承而设计类是一件很辛苦的工作。你必须建立文档说明其所有的自用模式，并且一旦建立了文档，在这个类的整个生命周期中都必须遵守。如果没有做到，子类就会依赖超类的实现细节，如果超类的实现发生了变化，它就有可能遭到破坏。为了允许其他人能编写出高效的子类，你还必须导出一个或者多个受保护的方法。除非知道真正需要子类，否则最好通过将类声明为 final，或者确保没有可访问的构造器来禁止类被继承。



# 实验20 接口优于抽象类

实验介绍



本次实验主要介绍在定义允许多个实现的类型时选用接口的优势以及骨架实现类的使用。

#### 知识点

- 接口
- 骨架实现类

接口



Java 提供了两种机制，可以用来定义允许多个实现的类型：接口和抽象类。自从 Java 8 为继承引入了缺省方法（default method），这两种机制都允许为某些实例方法提供实现。主要的区别在于，为了实现由抽象类定义的类型，类必须成为抽象类的一个子类。因为 Java 只允许单继承，所以用抽象类作为类型定义受到了限制。任何定义了所有必要的方法并遵守通用约定的类，都允许实现一个接口，无论这个类是处在类层次结构中的什么位置。

现有的类可以很容易被更新，以实现新的接口。如果这些方法尚不存在，你所需要做的就只是增加必要的方法，然后在类的声明中增加一个 implements 子句。例如，当 Comparable、Iterable 和 Autocloseable 接口被引入 Java 平台时，更新了许多现有的类，以实现这些接口。一般来说，无法更新现有的类来扩展新的抽象类。如果你希望两个类扩展同一个抽象类，就必须把抽象类放到类型层次（type hierarchy）的高处，这样它就成了那两个类的一个祖先。遗憾的是，这样做会间接地伤害到类层次，迫使这个公共祖先的所有后代类都扩展这个新的抽象类，无论它对于这些后代类是否合适。

接口是定义 mixin（混合类型）的理想选择。不严格地讲，mixin 类型是指：类除了实现它的“基本类型”之外，还可以实现这个 mixin 类型，以表明它提供了某些可供选择的行为。例如，Comparable 是一个 mixin 接口，它允许类表明它的实例可以与其他的可相互比较的对象进行排序。这样的接口之所以被称为 mixin，是因为它允许任选的功能可被混合到类型的主要功能中。抽象类不能被用于定义 mixin，同样也是因为它们不能被更新到现有的类中：类不可能有一个以上的父类，类层次结构中也没有适当的地方来插入 mixin。

接口允许构造非层次结构的类型框架。类型层次对于组织某些事物是非常合适的，但是其他事物并不能被整齐地组织成一个严格的层次结构。例如，假设我们有一个接口代表一个 singer（歌唱家），另一个接口代表一个 songwriter（作曲家）：

```java
public interface Singer{
    AudioClip sing(Song s)
}
public interface Songwriter{
    Song compose(int chartPosition);
}
```

在现实生活中，有些歌唱家本身也是作曲家。因为我们使用了接口而不是抽象类来定义这些类型，所以对于单个类而言，它同时实现 Singer 和 Songwriter 是完全允许的。实际上，我们可以定义第三个接口，它同时扩展 Singer 和 Songwriter，并添加一些适合于这种组合的新方法：

```java
public interface SingerSongwriter extends Singer,Songwriter{
    AudioClip strum();
    void actSensitive();
}
```

也许并非总是需要这种灵活性，但是一旦这样做了，接口可就成了救世主。另外一种做法是编写一个臃肿（bloated）的类层次，对于每一种要被支持的属性组合，都包含一个单独的类。如果在整个类型系统中有 n 个属性，那么就必须支持 2n 种可能的组合。这种现象被称为“组合爆炸”（combinatorial explosion）。类层次臃肿会导致类也臃肿，这些类包含许多方法，并且这些方法只是在参数的类型上有所不同而已，因为类层次中没有任何类型体现了公共的行为特征。

通过实验 18 中介绍的包装类（wrapper class）模式，接口使得安全地增强类的功能成为可能。如果使用抽象类来定义类型，那么程序员除了使用继承的手段来增加功能，再没有其他的选择了。这样得到的类与包装类相比，功能更差，也更加脆弱。

当一个接口方法根据其他接口方法有了明显的实现时，可以考虑以缺省方法的形式为程序员提供实现协助。关于这种方法的范例，请参考实验 21 中的 removeIf 方法。如果提供了缺省方法，要确保利用 Javadoc 标签 @implSpec 建立文档（详见实验 19）。



骨架实现类



通过缺省方法可以提供的实现协助是有限的。虽然许多接口都定义了 Object 方法的行为，如 equals 和 hashCode，但是不允许给它们提供缺省方法。而且接口中不允许包含实例域或者非公有的静态成员（私有的静态方法除外）。最后一点，无法给不受你控制的接口添加缺省方法。但是，通过对接口提供一个抽象的骨架实现（skeletal implementation）类，可以把接口和抽象类的优点结合起来。接口负责定义类型，或许还提供一些缺省方法，而骨架实现类则负责实现除基本类型接口方法之外，剩下的非基本类型接口方法。扩展骨架实现占了实现接口之外的大部分工作。这就是模板方法（Template Method）模式。

按照惯例，骨架实现类被称为 AbstractInterface，这里的 Interface 是指所实现的接口的名字。例如，Collections Framework 为每个重要的集合接口都提供了一个骨架实现，包括 AbstractCollection、AbstractSet、AbstractList 和 AbstractMap。将它们称作 SkeletalCollection、SkeletalSet、SkeletalList 和 SkeletalMap 也是有道理的，但是现在 Abstract 的用法已经根深蒂固。如果设计得当，骨架实现（无论是单独一个抽象类，还是接口中唯一包含的缺省方法）可以使程序员非常容易地提供他们自己的接口实现。例如，下面是一个静态工厂方法，除 AbstractList 之外，它还包含了一个完整的、功能全面的 List 实现：

```java
//Concrete implementation built atop skeletal implementation
static List<Integer> intArrayAsList(int[〕a){
    Objects .requireNonNull(a);
//The diamond operator is only legal here in java 9 and later
//If you’re using an earlier release，specify <Integer>
return new AbstractList<>(){
    @Override public Integer get(int i){
          return a[i];//Autoboxing (Item 6)
        }
    @Override public Integer set(int i，Integer val){
        int oldVal=a[i];
        a[i]=val;//Auto一unboxing
        return o1dVal;//Autoboxing
        }
    @Override public int size(){
            return a.length;
        }
    };
}
```

如果想知道一个 List 实现应该为你完成哪些工作，这个例子就充分演示了骨架实现的强大功能。顺便提一下，这个例子是个 Adapter，它允许将 int 数组看作 Integer 实例的列表。由于在 int 值和 Integer 实例之间来回转换需要开销，它的性能不会很好。注意，这个实现采用了匿名类（anonymous class）的形式（详见实验 24）。

骨架实现类的美妙之处在于，它们为抽象类提供了实现上的帮助，但又不强加“抽象类被用作类型定义时”所特有的严格限制。对于接口的大多数实现来讲，扩展骨架实现类是个很显然的选择，但并不是必须的。如果预置的类无法扩展骨架实现类，这个类始终能手工实现这个接口。同时，这个类本身仍然受益于接口中出现的任何缺省方法。此外，骨架实现类仍然有助于接口的实现。实现了这个接口的类可以把对于接口方法的调用转发到一个内部私有类的实例上，这个内部私有类扩展了骨架实现类。这种方法被称作模拟多重继承（simulated multiple inheritance），它与实验 18 中讨论过的包装类模式密切相关。这项技术具有多重继承的绝大多数优点，同时又避免了相应的缺陷。

编写骨架实现类相对比较简单，只是过程有点乏味。首先，必须认真研究接口，并确定哪些方法是最为基本的，其他的方法则可以根据它们来实现。这些基本方法将成为骨架实现类中的抽象方法。接下来，在接口中为所有可以在基本方法之上直接实现的方法提供缺省方法，但要记住，不能为 Object 方法（如 equals 和 hashCode）提供缺省方法。如果基本方法和缺省方法覆盖了接口，你的任务就完成了，不需要骨架实现类了。否则，就要编写一个类，声明实现接口，并实现所有剩下的接口方法。这个类中可以包含任何非公有的域，以及适合该任务的任何方法。

以 Map.Entry 接口为例，举个简单的例子。明显的基本方法是 getKey、getValue 和（可选的）setValue。接口定义了 equals 和 hashCode 的行为，并且有一个明显的 toString 实现。由于不允许给 Object 方法提供缺省实现，因此所有实现都放在骨架实现类中：

```java
//Skeletal implementation class
public abstract class AbstractMapEntry<K，V>
          implements Map.Entry<K，V>{
    //Entries in a modifiable map must override this method
    @Override public V setValue(V value){
          throw new UnsupportedOperationException();
      }
    //Implements the general contract of Map.Entry.equals
    @Override public boolean equals(Object o){
          if (o==this)
                return true;
          if(!(o instanceof Map.Entry))
                return false;
          Map.Entry<?，?>e=(Map.Entry)  o;
        return Objects.equals(e.getKey()，getKey())
              &&Objects.equals(e.getValue()，getValue());
      }
    //Implements the general contract of Map.Entry.hashCode
    @Override public int hashCode(){
        return Objects.hashCode(getKey())
             ^ Objects.hashCode(getValue());
    }
    @Override public String toString(){
            return getKey()+"="+getValue();
    }

}
```

注意，这个骨架实现不能在 Map.Entry 接口中实现，也不能作为子接口，因为不允许缺省方法覆盖 Object 方法，如 equals、hashCode 和 toString。

因为骨架实现类是为了继承的目的而设计的，所以应该遵从第 19 条中介绍的所有关于设计和文档的指导原则。为了简洁起见，上面例子中的文档注释部分被省略掉了，但是对于骨架实现类而言，好的文档绝对是非常必要的，无论它是否在接口或者单独的抽象类中包含了缺省方法。

骨架实现上有个小小的不同，就是简单实现（simple implementation），AbstractMap.SimpleEntry 就是个例子。简单实现就像骨架实现一样，这是因为它实现了接口，并且是为了继承而设计的，但是区别在于它不是抽象的：它是最简单的可能的有效实现。你可以原封不动地使用，也可以看情况将它子类化。



实验总结



总而言之，接口通常是定义允许多个实现的类型的最佳途径。如果你导出了一个重要的接口，就应该坚决考虑同时提供骨架实现类。而且，还应该尽可能地通过缺省方法在接口中提供骨架实现，以便接口的所有实现类都能使用。也就是说，对于接口的限制，通常也限制了骨架实现会采用的抽象类的形式。



# 实验21 	为后代设计接口

实验介绍



本节实验将分析使用缺省方法来实现给现有的接口添加方法的优劣势。

#### 知识点

- 接口
- 缺省方法



缺省方法



在 Java 8 发行之前，如果不破坏现有的实现，是不可能给接口添加方法的。如果给某个接口添加了一个新的方法，一般来说，现有的实现中是没有这个方法的，因此就会导致编译错误。在 Java 8 中，增加了缺省方法（default method）构造，目的就是允许给现有的接口添加方法。但是给现有接口添加新方法还是充满风险的。

缺省方法的声明中包括一个缺省实现（default implementation），这是给实现了该接口但没有实现默认方法的所有类使用的。虽然 Java 中增加了缺省方法之后，可以给现有接口添加方法了，但是并不能确保这些方法在之前存在的实现中都能良好运行。因为这些默认的方法是被“注入”到现有实现中的，它们的实现者并不知道，也没有许可。在 Java 8 之前，编写这些实现时，是默认它们的接口永远不需要任何新方法的。

Java 8 在核心集合接口中增加了许多新的缺省方法，主要是为了便于使用 lambda。Java 类库的缺省方法是高品质的通用实现，它们在大多数情况下都能正常使用。但是，并非每一个可能的实现的所有变体，始终都可以编写出一个缺省方法。比如，以 removeIf 方法为例，它在 Java 8 中被添加到了 Collection 接口。这个方法用来移除所有元素，并用一个 boolean 函数（或者断言）返回 true。缺省实现指定用其迭代器来遍历集合，在每个元素上调用断言（predicate），并利用迭代器的 remove 方法移除断言返回值为 true 的元素。其声明大致如下：

```java
//Default method added to the Collection interface in Java 8
default boolean removeIf(Predicate<? super E> filter){
    Objects.requireNonNull(filter):
    boolean result = false;
    for(Iterator<E> it = iterator();it.hasNext();){
        if(filter.test(it.next())){
            it.remove();
            result = true;
        }
    }
    return result;
}
```

这是适用于 removeIf 方法的最佳通用实现，但遗憾的是，它在某些现实的 Collection 实现中会出错。比如，以 org.apache.commons.collections4.Collection.SynchronizedCollection 为例，这个类来自 Apache Commons 类库，类似于 java.util 中的静态工厂 Collections.synchronizedCollection。Apache 版本额外提供了利用客户端提供的对象（而不是用集合）进行锁定的功能。换句话说，它是一个包装类（详见实验 18），它的所有方法在委托给包装集合之前，都在锁定对象上进行了同步。

Apache 版本的 SynchronizedCollection 类依然有人维护，但是到编写本课程之时，它也没有取代 removeIf 方法。如果这个类与 Java 8 结合使用，将会继承 removeIf 的缺省实现，它不会（实际上也无法）保持这个类的基本承诺：围绕着每一个方法调用执行自动同步。缺省实现压根不知道同步这回事，也无权访问包含该锁定对象的域。如果客户在 SynchronizedCollection 实例上调用 removeIf 方法，同时另一个线程对该集合进行修改，就会导致 ConcurrentModificationException 或者其他异常行为。

为了避免在类似的 Java 平台类库实现中发生这种异常，如 Collections.synchronizedCollection 返回的包私有类，JDK 维护人员必须覆盖默认的 removeIf 实现，以及像它一样的其他方法，以便在调用缺省实现之前执行必要的同步。不属于 Java 平台组成部分的预先存在的集合实现，过去无法做出与接口变化相类似的改变，现在有些已经可以做到了。

有了缺省方法，接口的现有实现就不会出现编译时没有报错或警告，运行时却失败的情况。这个问题虽然并非普遍，但也不是孤立的意外事件。Java 8 在集合接口中添加的许多方法是极易受影响的，有些现有实现已知将会受到影响。

建议尽量避免利用缺省方法在现有接口上添加新的方法，除非有特殊需要，但就算在那样的情况下也应该慎重考虑：缺省的方法实现是否会破坏现有的接口实现。然而，在创建接口的时候，用缺省方法提供标准的方法实现是非常方便的，它简化了实现接口的任务（详见实验 20）。

还要注意的是，缺省方法不支持从接口中删除方法，也不支持修改现有方法的签名。对接口进行这些修改肯定会破坏现有的客户端代码。

因此，在发布程序之前，测试每一个新的接口就显得尤其重要。程序员应该以不同的方法实现每一个接口。最起码不应少于三种实现。编写多个客户端程序，利用每个新接口的实例来执行不同的任务，这一点也同样重要。这些步骤对确保每个接口都能满足其既定的所有用途起到了很大的帮助。它还有助于在接口发布之前及时发现其中的缺陷，使你依然能够轻松地把它们纠正过来。或许接口程序发布之后也能纠正，但是千万别指望它啦！



实验总结



总而言之，尽管缺省方法现在已经是 Java 平台的组成部分，但谨慎设计接口仍然是至关重要的。虽然缺省方法可以在现有接口上添加方法，但这么做还是存在着很大的风险。就算接口中只有细微的缺陷都可能永远给用户带来不愉快；假如接口有严重的缺陷，则可能摧毁包含它的 API。



# 实验接口22 只用于定义类型

实验介绍



本节实验将介绍使用接口只用于定义类型，并通过常量接口案例来进行相关论证。

#### 知识点

- 常量接口

常量接口



当类实现接口时，接口就充当可以引用这个类的实例的类型（type）。因此，类实现了接口，就表明客户端可以对这个类的实例实施某些动作。为了任何其他目的而定义接口是不恰当的。

有一种接口被称为常量接口（constant interface），它不满足上面的条件。这种接口不包含任何方法，它只包含静态的 final 域，每个域都导出一个常量。使用这些常量的类实现这个接口，以避免用类名来修饰常量名。下面举个例子：

```java
//Constant interface antipattern - do not use!
public interface PhysicalConStants{
    //Avogadro's number(1/mol)
    static final double AVOGADROS_NUMBER = 6.022_140_857e23;
    //Boltzmann constant (J/K)
    static final double BOLTZMANN_CONSTANT=1.380_648_52e-23;
    //Moss of the electron(kg)
    static final double ELECTRON_MASS=9.109_383_56e-31;
}
```

常量接口模式是对接口的不良使用。类在内部使用某些常量，这纯粹是实现细节。实现常量接口会导致把这样的实现细节泄露到该类的导出 API 中。类实现常量接口对于该类的用户而言并没有什么价值。实际上，这样做反而会使他们更加糊涂。更糟糕的是，它代表了一种承诺：如果在将来的发行版本中，这个类被修改了，它不再需要使用这些常量了，它依然必须实现这个接口，以确保二进制兼容性。如果非 final 类实现了常量接口，它的所有子类的命名空间也会被接口中的常量所“污染”。

在 Java 平台类库中有几个常量接口，例如 java.io.ObjectStreamConstants。这些接口应该被认为是反面的典型，不值得效仿。

如果要导出常量，可以有几种合理的选择方案。如果这些常量与某个现有的类或者接口紧密相关，就应该把这些常量添加到这个类或者接口中。例如，在 Java 平台类库中所有的数值包装类，如 Integer 和 Double，都导出了 MIN_VALUE 和 MAX_VALUE 常量。如果这些常量最好被看作枚举类型的成员，就应该用枚举类型（enum type）（详见实验 34）来导出这些常量。否则，应该使用不可实例化的工具类（utility class）（详见实验 4）来导出这些常量。下面的例子是前面的 PhysicalConstants 例子的工具类翻版：

```java
//Constant utility class
package com.effectivejava.science;
public class PhysicalConstants{
    private PhysicalConStants() { }

    public static final double AVOGADROS_NUMBER = 6.022_140_857e23;
    public static final double BOLTZMANN_CONSTANT=1.380_648_52e-23;
    public static final double ELECTRON_MASS=9.109_383_56e-31;
}
```

注意，有时候会在数字的字面量中使用下划线（_）。从 Java 7 开始，下划线的使用已经合法了，它对数字字面量的值没有影响，如果使用得当，还可以极大地提升它们的可读性。如果其中包含五个或五个以上连续的数字，无论是浮点还是定点，都要考虑在数字的字面量中添加下划线。对于基数为 10 的字面量，无论是整数还是浮点，都应该用下划线把数字隔成每三位一组，表示一千的正负倍数。

工具类通常要求客户端要用类名来修饰这些常量名，例如 PhysicalConstants.AVOGADROS_NUMBER。如果大量利用工具类导出的常量，可以通过利用静态导入（static import）机制，避免用类名来修饰常量名：

```java
//Use of static import to avoid qualifying constants
import static com.effectivejava.science.PhysicalConstants.*;
public class Test{
    double atoms(double mols){
        return AVOGADROS_NUMBER * mols;
    }
    ...
    //Many more uses of PhysicalConstants justify static import
}
```



实验总结



简而言之，接口应该只被用来定义类型，它们不应该被用来导出常量。

# 实验23 类层次优先于标签类

实验介绍



本节实验将介绍标签类和类层次的相关知识点，并通过案例来对比两种方式的优劣势。

#### 知识点

- 标签类
- 类层次

标签类与类层次



有时可能会遇到带有两种甚至更多种风格的实例的类，并包含表示实例风格的标签（tag）域。例如，以下面这个类为例，它能够表示圆形或者矩形：

```java
//Tagged class - vastly inferior to a class hierarchy!
class Figure{
    enum shape{ RECTANGLE,CIRCLE};
    //Tag filed - the shape of this figure
    final Shape shape;

    // These fields are used only if shape is RECTANGLE
    double length;
    double width;
    //This field is used only if shape is CIRCLE
    double radius;
    //Constuctor for circle
    Figure( double radius){
        shape = Shape.CIRCLE;
        this.radius = radius;
    }
    //Constuctor for rectangle
    Figure(double length,double width){
        shape=Shape.RECTANGLE;
        this.length=length;
        this.widh=width;

    }
    double area(){
        switch(shape){
            case RECTANGLE:
                return length * width;
            case CIRCLE:
                return Math.PI * (radius * radius);
            default:
                throw new AssertionError(shape);
        }
    }
}
```

这种标签类（tagged class）有许多缺点。它们中充斥着样板代码，包括枚举声明、标签域以及条件语句。由于多个实现乱七八糟地挤在单个类中，破坏了可读性。由于实例承担着属于其他风格的不相关的域，因此内存占用也增加了。域不能做成 final 的，除非构造器初始化了不相关的域，产生了更多的样板代码。构造器必须不借助编译器来设置标签域，并初始化正确的数据域：如果初始化了错误的域，程序就会在运行时失败。无法给标签类添加风格，除非可以修改它的源文件。如果一定要添加风格，就必须记得给每个条件语句都添加一个条件，否则类就会在运行时失败。最后，实例的数据类型没有提供任何关于其风格的线索。一句话，标签类过于冗长、容易出错，并且效率低下。

幸运的是，面向对象的语言（如 Java）提供了其他更好的方法来定义能表示多种风格对象的单个数据类型：子类型化（subtyping）。标签类正是对类层次的一种简单的仿效。

为了将标签类转变成类层次，首先要为标签类中的每个方法都定义一个包含抽象方法的抽象类，标签类的行为依赖于标签值。在 Figure 类中，只有一个这样的方法：area。这个抽象类是类层次的根（root）。如果还有其他的方法其行为不依赖于标签的值，就把这样的方法放在这个类中。同样地，如果所有的方法都用到了某些数据域，就应该把它们放在这个类中。在 Figure 类中，不存在这种类型独立的方法或者数据域。

接下来，为每种原始标签类都定义根类的具体子类。在前面的例子中，这样的类型有两个：圆形（circle）和矩形（rectangle）。在每个子类中都包含特定于该类型的数据域。在我们的示例中，radius 是特定于圆形的，length 和 width 是特定于矩形的。同时在每个子类中还包括针对根类中每个抽象方法的相应实现。以下是与原始的 Figure 类相对应的类层次：

```java
//Class hierarchy replacement for a tagged class
abstract class Figure{
      abstract double area();
}
class Circle extends Figure{
    final double radius;
    Circle(double radius){this.radius=radius;}
    @Override double area(){return Math.PI*(radius*radius);}
    }
class Rectangle extends Figure{
    final double length;
    final double width;
    Rectangle(double length，double width){
        this.length=length;
        this.width=width;
    }
    @Override double area(){return length*width;}
}
```

这个类层次纠正了前面提到过的标签类的所有缺点。这段代码简单且清楚，不包含在原来的版本中见到的所有样板代码。每个类型的实现都配有自己的类，这些类都没有受到不相关数据域的拖累。所有的域都是 final 的。编译器确保每个类的构造器都初始化它的数据域，对于根类中声明的每个抽象方法都确保有一个实现。这样就杜绝了由于遗漏 switch case 而导致运行时失败的可能性。多名程序员可以独立地扩展层次结构，并且不用访问根类的源代码就能相互操作。每种类型都有一种相关的独立的数据类型，允许程序员指明变量的类型，限制变量，并将参数输入到特殊的类型。

类层次的另一个好处在于，它们可以用来反映类型之间本质上的层次关系，有助于增强灵活性，并有助于更好地进行编译时类型检查。假设上述例子中的标签类也允许表达正方形。类层次可以反映出正方形是一种特殊的矩形这一事实（假设两者都是不可变的）：

```java
class Square extends Rectangle{
    Square (double side){
          super(side，side);
      }
}
```

注意，上述层次中的域被直接访问，而不是通过访问方法访问。这是为了简洁起见，如果层次结构是公有的（详见实验 16），则不允许这样做。



实验总结



简而言之，标签类很少有适用的时候。当你想要编写一个包含显式标签域的类时，应该考虑一下，这个标签是否可以取消，这个类是否可以用类层次来代替。当你遇到一个包含标签域的现有类时，就要考虑将它重构到一个层次结构中去。



# 实验24	静态成员类优于非静态成员类

实验介绍



本次实验将介绍静态成员类、非静态成员类、匿名类、局部类等四种嵌套类，并介绍什么时候使用哪种嵌套类，以及这样做的原因。

#### 知识点

- 静态成员类
- 非静态成员类
- 匿名类
- 局部类

嵌套类



嵌套类（nested class）是指定义在另一个类内部的类。嵌套类存在的目的应该只是为它的外围类（enclosing class）提供服务。如果嵌套类将来可能会用于其他的某个环境中，它就应该是顶层类（top-level class）。嵌套类有四种：静态成员类（static member class）、非静态成员类（nonstatic member class）、匿名类（anonymous class）和局部类（local class）。除了第一种之外，其他三种都称为内部类（inner class）。

静态成员类和非静态成员类



静态成员类是最简单的一种嵌套类。最好把它看作是普通的类，只是碰巧被声明在另一个类的内部而已，它可以访问外围类的所有成员，包括那些声明为私有的成员。静态成员类是外围类的一个静态成员，与其他的静态成员一样，也遵守同样的可访问性规则。如果它被声明为私有的，它就只能在外围类的内部才可以被访问，等等。

静态成员类的一种常见用法是作为公有的辅助类，只有与它的外部类一起使用才有意义。例如，以枚举为例，它描述了计算器支持的各种操作（详见实验 34）。Operation 枚举应该是 Calculator 类的公有静态成员类，之后 Calculator 类的客户端就可以用诸如 Calculator.Operation.PLUS 和 Calculator.Operation.MINUS 这样的名称来引用这些操作。

从语法上讲，静态成员类和非静态成员类之间唯一的区别是，静态成员类的声明中包含修饰符 static。尽管它们的语法非常相似，但是这两种嵌套类有很大的不同。非静态成员类的每个实例都隐含地与外围类的一个外围实例（enclosing instance）相关联。在非静态成员类的实例方法内部，可以调用外围实例上的方法，或者利用修饰过的 this（qualified this）构造获得外围实例的引用。如果嵌套类的实例可以在它外围类的实例之外独立存在，这个嵌套类就必须是静态成员类：在没有外围实例的情况下，要想创建非静态成员类的实例是不可能的。

当非静态成员类的实例被创建的时候，它和外围实例之间的关联关系也随之被建立起来；而且，这种关联关系以后不能被修改。通常情况下，当在外围类的某个实例方法的内部调用非静态成员类的构造器时，这种关联关系被自动建立起来。使用表达式 enclosingInstance.new MemberClas (args) 来手工建立这种关联关系也是有可能的，但是很少使用。正如你所预料的那样，这种关联关系需要消耗非静态成员类实例的空间，并且会增加构造的时间开销。

非静态成员类的一种常见用法是定义一个 Adapter，它允许外部类的实例被看作是另一个不相关的类的实例。例如，Map 接口的实现往往使用非静态成员类来实现它们的集合视图（collection view），这些集合视图是由 Map 的 keySet、entrySet 和 values 方法返回的。同样地，诸如 Set 和 List 这种集合接口的实现往往也使用非静态成员类来实现它们的迭代器（iterator）：

```java
//Typical use of a nonstatic mebner classs
public class MySet<E> extends AbstractSet<E>{
    ... //Bulk of the class omitted
    @Override public Iterator<E> iterator(){
        return new MyIterator();
    }
    private class MyIterator implements Iterator<E>{
        ...
    }
}
```

如果声明成员类不要求访问外围实例，就要始终把修饰符 static 放在它的声明中，使它成为静态成员类，而不是非静态成员类。如果省略了 static 修饰符，则每个实例都将包含一个额外的指向外围对象的引用。如前所述，保存这份引用要消耗时间和空间，并且会导致外围实例在符合垃圾回收（详见实验 7）时却仍然得以保留。由此造成的内存泄漏可能是灾难性的。但是常常难以发现，因为这个引用是不可见的。

私有静态成员类的一种常见用法是代表外围类所代表的对象的组件。以 Map 实例为例，它把键（key）和值（value）关联起来。许多 Map 实现的内部都有一个 Entry 对象，对应于 Map 中的每个键 - 值对。虽然每个 entry 都与一个 Map 关联，但是 entry 上的方法（getKey、getValue 和 setValue）并不需要访问该 Map。因此，使用非静态成员类来表示 entry 是很浪费的：私有的静态成员类是最佳的选择。如果不小心漏掉了 entry 声明中的 static 修饰符，该 Map 仍然可以工作，但是每个 entry 中将会包含一个指向该 Map 的引用，这样就浪费了空间和时间。

如果相关的类是导出类的公有或受保护的成员，毫无疑问，在静态和非静态成员类之间做出正确的选择是非常重要的。在这种情况下，该成员类就是导出的 API 元素，在后续的发行版本中，如果不违反向后兼容性，就无法从非静态成员类变为静态成员类。



匿名类和局部类



顾名思义，匿名类是没有名字的。它不是外围类的一个成员。它并不与其他的成员一起被声明，而是在使用的同时被声明和实例化。匿名类可以出现在代码中任何允许存在表达式的地方。当且仅当匿名类出现在非静态的环境中时，它才有外围实例。但是即使它们出现在静态的环境中，也不可能拥有任何静态成员，而是拥有常数变量（constant variable），常数变量是 final 基本类型，或者被初始化成常量表达式的字符串域。

匿名类的运用受到诸多的限制。除了在它们被声明的时候之外，是无法将它们实例化的。不能执行 instanceof 测试，或者做任何需要命名类的其他事情。无法声明一个匿名类来实现多个接口，或者扩展一个类，并同时扩展类和实现接口。除了从超类型中继承得到之外，匿名类的客户端无法调用任何成员。由于匿名类出现在表达式中，它们必须保持简短（大约 10 行或者更少），否则会影响程序的可读性。

在 Java 中增加 lambda 之前，匿名类是动态地创建小型函数对象（function object）和过程对象（process object）的最佳方式，但是现在会优先选择 lambda（详见实验 42）。匿名类的另一种常见用法是在静态工厂方法的内部（参见实验 20 中的 intArrayAsList 方法）。

局部类是四种嵌套类中使用最少的类。在任何“可以声明局部变量”的地方，都可以声明局部类，并且局部类也遵守同样的作用域规则。局部类与其他三种嵌套类中的每一种都有一些共同的属性。与成员类一样，局部类有名字，可以被重复使用。与匿名类一样，只有当局部类是在非静态环境中定义的时候，才有外围实例，它们也不能包含静态成员。与匿名类一样，它们必须非常简短，以便不会影响可读性。



实验总结



总而言之，共有四种不同的嵌套类，每一种都有自己的用途。如果一个嵌套类需要在单个方法之外仍然是可见的，或者它太长了，不适合放在方法内部，就应该使用成员类。如果成员类的每个实例都需要一个指向其外围实例的引用，就要把成员类做成非静态的；否则，就做成静态的。假设这个嵌套类属于一个方法的内部，如果你只需要在一个地方创建实例，并且已经有了一个预置的类型可以说明这个类的特征，就要把它做成匿名类；否则，就做成局部类。



# 实验25	限制源文件为单个顶级类

实验介绍



本节实验将为大家介绍在编写类时需要限制源文件为单个顶级类以及这样做的原因。

#### 知识点

- 限制源文件为单个顶级类

限制源文件为单个顶级类



虽然 Java 编译器允许在一个源文件中定义多个顶级类，但这么做并没有什么好处，只会带来巨大的风险。因为在一个源文件中定义多个顶级类，可能导致给一个类提供多个定义。哪一个定义会被用到，取决于源文件被传给编译器的顺序。

为了更具体地说明，下面举个例子，这个源文件中只包含一个 Main 类，它将引用另外两个顶级类（Utensil 和 Dessert）的成员：

```java
public class Main{
    public static viod main(String[] args){
        System.out.println(Utensil.NAME + Dessert.NAME);
    }
}
```

现在假设你在一个名为 `Utensil.java` 的源文件中同时定义了 Utensil 和 Dessert：

```java
//Two classes defined in one file.Don't ever do this!
class Utensil{
    static final String NAME = "pan";
}
class Dessert{
    static final String NAME="cake";
}
```

当然，主程序会打印出`pancake`。

现在假设你不小心在另一个名为 `Dessert.java` 的源文件中也定义了同样的两个类：

```java
//Two classes defined in one file.Don't ever do this!
class Utensil{
    static final String NAME = "pot";
}
class Dessert{
    static final String NAME="pie";
}
```

如果你侥幸是用命令 `javac Main.java Dessert.java` 来编译程序，那么编译就会失败，此时编译器会提醒你定义了多个 Utensil 和 Dessert 类。这是因为编译器会先编译 `Main.java`，当它看到 Utensil 的引用（在 Dessert 引用之前），就会在 `Utensil.java` 中查看这个类，结果找到 Utensil 和 Dessert 这两个类。当编译器在命令行遇到 `Dessert.java` 时，也会去查找该文件，结果会遇到 Utensil 和 Dessert 这两个定义。

如果用命令 `javac Main.java` 或者 `javac Main.java Utensil.java` 编译程序， 结果将如同你还没有编写 `Dessert.java` 文件一样，输出 `pancake`。但如果是用命令 `javac Dessert.java Main.java` 编译程序，就会输出 `potpie`。程序的行为受源文件被传给编译器的顺序影响，这显然是让人无法接受的。

这个问题的修正方法很简单，只要把顶级类（在本例中是指 Utensil 和 Dessert）分别放入独立的源文件即可。如果一定要把多个顶级类放进一个源文件中，就要考虑使用静态成员类（详见实验 24），以此代替将这两个类分到独立源文件中去。如果这些类服从于另一个类，那么将它们做成静态成员类通常比较好，因为这样增强了代码的可读性，如果将这些类声明为私有的（详见实验 15），还可以使它们减少被读取的概率。以下就是做成静态成员类的范例：

```java
//Static member classes instead of multiple top-level classes
public class Test{
    public static void main(String[] args){
        System.out.println(Utensil.NAME + Dessert.NAME);
    }
    private static class Utensil{
        static final String NAME = "pain";
    }
    private static class Dessert{
        static final String Name = "cake";
    }
}
```



实验总结



结论显而易见：永远不要把多个顶级类或者接口放在一个源文件中。遵循这个规则可以确保编译时一个类不会有多个定义。这么做反过来也能确保编译产生的类文件，以及程序结果的行为，都不会受到源文件被传给编译器时的顺序的影响。

# 第四章	泛型

# 实验26	请不要使用原生态类型

