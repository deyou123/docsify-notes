# Junit5 单元测试

## 1. JUNiT 官网

 官网教程  [junit-team/junit5](https://github.com/junit-team/junit5/)

API : https://junit.org/junit5/docs/current/api/

用户指南： https://junit.org/junit5/docs/current/user-guide/

与以前的 JUnit 版本不同，JUnit 5 由来自三个不同子项目的几个不同模块组成。

**JUnit 5 = JUnit Platform + JUnit Jupiter+ JUnit Vintage**

## 2.Junit 5测试

### 2.1 引入依赖

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <version>5.8.2</version>
    <scope>test</scope>
</dependency>
```

如果想兼容Junit 4,引入junit-vintage-engine，该依赖自动引入junit4.

```xml
<dependency>
    <groupId>org.junit.vintage</groupId>
    <artifactId>junit-vintage-engine</artifactId>
    <version>5.8.2</version>
    <scope>test</scope>
</dependency>
```



不同注解的区别如下：

| **特性**                                                     | **Junit 4**  | **Junit 5** |
| ------------------------------------------------------------ | ------------ | ----------- |
| 在当前类的所有测试方法之前执行。注解在静态方法上。此方法可以包含一些初始化代码。 | @BeforeClass | @BeforeAll  |
| 在当前类中的所有测试方法之后执行。注解在静态方法上。此方法可以包含一些清理代码。 | @AfterClass  | @AfterAll   |
| 在每个测试方法之前执行。注解在非静态方法上。可以重新初始化测试方法所需要使用的类的某些属性。 | @Before      | @BeforeEach |
| 在每个测试方法之后执行。注解在非静态方法上。可以回滚测试方法引起的数据库修改。 | @After       | @AfterEach  |

### 2.2 第一个测试用例

引入JUnit 5，我们可以先快速编写一个简单的测试用例，从这个测试用例来认识初步下 JUnit 5：

```java
@DisplayName("我的第一个测试用例")
public class MyFirstTestCaseTest {

    @BeforeAll
    public static void init() {
        System.out.println("初始化数据");
    }

    @AfterAll
    public static void cleanup() {
        System.out.println("清理数据");
    }

    @BeforeEach
    public void tearup() {
        System.out.println("当前测试方法开始");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("当前测试方法结束");
    }

    @DisplayName("我的第一个测试")
    @Test
    void testFirstTest() {
        System.out.println("我的第一个测试开始测试");
    }

    @DisplayName("我的第二个测试")
    @Test
    void testSecondTest() {
        System.out.println("我的第二个测试开始测试");
    }
}
```

直接运行这个测试用例，可以看到控制台日志如下：![image-20220228145433426](images/image-20220228145433426.png)

可以看到左边一栏的结果里显示测试项名称就是我们在测试类和方法上使用 **@DisplayName** 设置的名称，这个注解就是 JUnit 5 引入，用来定义一个测试类并指定用例在测试报告中的展示名称，这个注解可以使用在类上和方法上，在类上使用它就表示该类为测试类，在方法上使用则表示该方法为测试方法。

```java
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@API(status = STABLE, since = "5.0")
public @interface DisplayName {
	String value();
}
```

再来看下示例代码中使用到的一对注解 **@BeforeAll **和 **@AfterAll \**，它们定义了整个测试类在开始前以及结束时的操作，只能修饰静态方法，主要用于在测试过程中所需要的全局数据和外部资源的初始化和清理。与它们不同，\**@BeforeEach** 和 **@AfterEach** 所标注的方法会在每个测试用例方法开始前和结束时执行，主要是负责该测试用例所需要的运行环境的准备和销毁。

在测试过程中除了这些基本的注解，还有更多丰富强大的注解，接下来就我们一一学习下吧。

### 2.3 禁用执行测试：@Disabled

当我们希望在运行测试类时，跳过某个测试方法，正常运行其他测试用例时，我们就可以用上 @Disabled 注解，表明该测试方法处于不可用，执行测试类的测试方法时不会被 JUnit 执行。

下面看下使用 @Disbaled 之后的运行效果，在原来测试类中添加如下代码：

```java
@DisplayName("我的第三个测试")
@Disabled
@Test
void testThirdTest() {
	System.out.println("我的第三个测试开始测试");
}
```

运行后看到控制台日志如下，用 @Disabled 标记的方法不会执行，只有单独的方法信息打印：

![image-20220228145334764](images/image-20220228145334764.png)

@Disabled 也可以使用在类上，用于标记类下所有的测试方法不被执行，一般使用对多个测试类组合测试的时候。

### 2.4 内嵌测试类：@Nested

当我们编写的类和代码逐渐增多，随之而来的需要测试的对应测试类也会越来越多。为了解决测试类数量爆炸的问题，JUnit 5提供了@Nested 注解，能够以静态内部成员类的形式对测试用例类进行逻辑分组。 并且每个静态内部类都可以有自己的生命周期方法， 这些方法将按从外到内层次顺序执行。 此外，嵌套的类也可以用@DisplayName 标记，这样我们就可以使用正确的测试名称。下面看下简单的用法：

```java
@DisplayName("内嵌测试类")
public class NestUnitTest {
    @BeforeEach
    void init() {
        System.out.println("测试方法执行前准备");
    }

    @Nested
    @DisplayName("第一个内嵌测试类")
    class FirstNestTest {
        @Test
        void test() {
            System.out.println("第一个内嵌测试类执行测试");
        }
    }

    @Nested
    @DisplayName("第二个内嵌测试类")
    class SecondNestTest {
        @Test
        void test() {
            System.out.println("第二个内嵌测试类执行测试");
        }
    }
}
```

运行所有测试用例后，在控制台能看到如下结果：

![image-20220228145506999](images/image-20220228145506999.png)

### 2.5 重复性测试：@RepeatedTest

在 JUnit 5 里新增了对测试方法设置运行次数的支持，允许让测试方法进行重复运行。当要运行一个测试方法 N次时，可以使用 @RepeatedTest 标记它，如下面的代码所示：

```java
@DisplayName("重复测试")
@RepeatedTest(value = 5)
public void i_am_a_repeated_test() {
	System.out.println("执行测试");
}
```

运行后测试方法会执行5次，在 IDEA 的运行效果如下图所示：

![image-20220228145547033](images/image-20220228145547033.png)

这是基本的用法，我们还可以对重复运行的测试方法名称进行修改，利用 @RepeatedTest 提供的内置变量，以占位符方式在其 `name` 属性上使用，下面先看下使用方式和效果：

```java
@DisplayName("自定义名称重复测试")
@RepeatedTest(value = 3, name = "{displayName} 第 {currentRepetition} 次")
public void i_am_a_repeated_test_2() {
	System.out.println("执行测试");
}
```

![image-20220228145609582](images/image-20220228145609582.png)

@RepeatedTest 注解内用 `currentRepetition` 变量表示已经重复的次数，`totalRepetitions` 变量表示总共要重复的次数，`displayName` 变量表示测试方法显示名称，我们直接就可以使用这些内置的变量来重新定义测试方法重复运行时的名称。

### 2.6 新的断言

在断言 API 设计上，JUnit 5 进行显著地改进，并且充分利用 Java 8 的新特性，特别是 Lambda 表达式，最终提供了新的断言类: **org.junit.jupiter.api.Assertions** 。许多断言方法接受 Lambda 表达式参数，在断言消息使用 Lambda 表达式的一个优点就是它是延迟计算的，如果消息构造开销很大，这样做一定程度上可以节省时间和资源。

现在还可以将一个方法内的多个断言进行分组，使用 assertAll 方法如下示例代码：

```java
@Test
void testGroupAssertions() {
    int[] numbers = {0, 1, 2, 3, 4};
    Assertions.assertAll("numbers",
            () -> Assertions.assertEquals(numbers[1], 1),
            () -> Assertions.assertEquals(numbers[3], 3),
            () -> Assertions.assertEquals(numbers[4], 4)
    );
}
```

如果分组断言中任一个断言的失败，都会将以 MultipleFailuresError 错误进行抛出提示。

### 2.7超时操作的测试：assertTimeoutPreemptively

当我们希望测试耗时方法的执行时间，并不想让测试方法无限地等待时，就可以对测试方法进行超时测试，JUnit 5 对此推出了断言方法 `assertTimeout`，提供了对超时的广泛支持。

假设我们希望测试代码在一秒内执行完毕，可以写如下测试用例：

```java
@Test
@DisplayName("超时方法测试")
void test_should_complete_in_one_second() {
  Assertions.assertTimeoutPreemptively(Duration.of(1, ChronoUnit.SECONDS), () -> Thread.sleep(2000));
}
```

这个测试运行失败，因为代码执行将休眠两秒钟，而我们期望测试用例在一秒钟之内成功。但是如果我们把休眠时间设置一秒钟，测试仍然会出现偶尔失败的情况，这是因为测试方法执行过程中除了目标代码还有额外的代码和指令执行会耗时，所以在超时限制上无法做到对时间参数的完全精确匹配。

![image-20220228145654481](images/image-20220228145654481.png)

修改线程休眠时间500ms,测试通过

![image-20220228145747868](images/image-20220228145747868.png)

### 2.8 异常测试：assertThrows

我们代码中对于带有异常的方法通常都是使用 try-catch 方式捕获处理，针对测试这样带有异常抛出的代码，而 JUnit 5 提供方法 `Assertions#assertThrows(Class<T>, Executable)` 来进行测试，第一个参数为异常类型，第二个为函数式接口参数，跟 Runnable 接口相似，不需要参数，也没有返回，并且支持 Lambda表达式方式使用，具体使用方式可参考下方代码：

```java
@Test
@DisplayName("测试捕获的异常")
void assertThrowsException() {
  String str = null;
  Assertions.assertThrows(IllegalArgumentException.class, () -> {
    Integer.valueOf(str);
  });
}
```

当Lambda表达式中代码出现的异常会跟首个参数的异常类型进行比较，如果不属于同一类异常，就会控制台输出如下类似的提示：`org.opentest4j.AssertionFailedError: Unexpected exception type thrown ==> expected: <IllegalArgumentException> but was: <...Exception>`

## 3. JUnit 5 参数化测试

要使用 JUnit 5 进行参数化测试，除了 junit-jupiter-engine 基础依赖之外，还需要另个模块依赖：**junit-jupiter-params**，其主要就是提供了编写参数化测试 API。同样方式，把相同版本的对应依赖引入 Maven 工程中：

```xml
<dependency>
  <groupId>org.junit.jupiter</groupId>
  <artifactId>junit-jupiter-params</artifactId>
  <version>5.5.2</version>
  <scope>test</scope>
</dependency>
```

### 3.1 基本数据源测试： @ValueSource

@ValueSource 是 JUnit 5 提供的最简单的数据参数源，支持 Java 的八大基本类型和字符串，Class，使用时赋值给注解上对应类型属性，以数组方式传递，示例代码如下：

```java
public class ParameterizedUnitTest {
    @ParameterizedTest
    @ValueSource(ints = {2, 4, 8})
    void testNumberShouldBeEven(int num) {
        Assertions.assertEquals(0, num % 2);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Effective Java", "Code Complete", "Clean Code"})
    void testPrintTitle(String title) {
        System.out.println(title);
    }
}
```

> @ParameterizedTest 作为参数化测试的必要注解，替代了 @Test 注解。任何一个参数化测试方法都需要标记上该注解。

运行测试，结果如下图所示，针对 @ValueSource 里每个参数都会运行目标方法，一旦哪个参数运行测试失败，就意味着该测试方法不通过。

![image-20220228145130837](images/image-20220228145130837.png)

### 3.2 CSV 数据源测试：@CsvSource

通过 @CsvSource 可以注入指定 CSV 格式 (comma-separated-values) 的一组数据，用每个逗号分隔的值来匹配一个测试方法对应的参数，下面是使用示例：

```java
@ParameterizedTest
@CsvSource({"1,One", "2,Two", "3,Three"})
void testDataFromCsv(long id, String name) {
	System.out.printf("id: %d, name: %s", id, name);
}
```

运行结果如图所示，除了用逗号分隔参数外，@CsvSource 还支持自定义符号，只要修改它的 `delimiter` 即可，默认为 `，`。

![image-20220228145055538](images/image-20220228145055538.png)

JUnit 还提供了读取外部 CSV 格式文件数据的方式作为数据源的实现，我们只要用 @CsvFileSource 指定资源文件路径即可，使用起来跟 @CsvSource 一样简单这里就不再重复演示了。

> @CsvFileSource 指定的资源文件路径时要以 `/` 开始，寻找当前测试资源目录下文件。

除了上面提到的三种数据源方式外，JUnit 还提供了以下三种数据源：

- **@EnumSource**：允许我们通过参数值，给指定 Enum 枚举类型传入，构造出枚举类型中特定的值。
- **@MethodSource**：指定一个返回的 Stream / Array / 可迭代对象 的方法作为数据源。 需要注意的是该方法必须是静态的，并且不能接受任何参数。
- **@ArgumentSource**：通过实现 ArgumentsProvider 接口的参数类来作为数据源，重写它的 `provideArguments` 方法可以返回自定义类型的 Stream<Arguments> ，作为测试方法所需要的数据使用。

对上面三种数据源注解感兴趣的同学可以参考示例工程的 ParameterizedUnitTest 类，这里就不一一再介绍了。

## 结语

到这里，想必你对 JUnit 5 也有了基本的了解和掌握，都说单元测试是提升软件质量，提升研发效率的必备环节，从会用 JUnit 5 写单元测试开始，培养写测试代码的习惯，在不断实践中提升自身的开发效率，让写出来的代码有更质量的保证。