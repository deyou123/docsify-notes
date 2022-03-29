# [Spring Boot + Freemarker](http://itboyhub.com/2021/01/29/spring-boot-freemarker/) 一

## 1. Freemarker 简介

这是一个相当老牌的开源的免费的模版引擎，基于Apache许可证2.0版本发布。

通过 Freemarker 模版，我们可以将数据渲染成 HTML 网页、电子邮件、配置文件以及源代码等。Freemarker 不是面向最终用户的，而是一个 Java 类库，我们可以将之作为一个普通的组件嵌入到我们的产品中。

来看一张来自 Freemarker 官网的图片：

![img](http://img.itboyhub.com/2020/10-1.png)

可以看到，Freemarker 可以将模版和数据渲染成 HTML 。

Freemarker 模版后缀为 `.ftlh`(FreeMarker Template Language)。FTL 是一种简单的、专用的语言，它不是像 Java 那样成熟的编程语言。在模板中，你可以专注于如何展现数据， 而在模板之外可以专注于要展示什么数据。

## 2. 整合 Spring Boot

在 SSM 中整合 Freemarker ，所有的配置文件加起来，前前后后大约在 50 行左右，Spring Boot 中要几行配置呢？ 0 行！

### 2.1 创建工程

首先创建一个 Spring Boot 工程，引入 Freemarker 依赖，如下图：

![img](http://img.itboyhub.com/2020/20201217125112.png)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-freemarker</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```



工程创建完成后，在 `org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration` 类中，可以看到关于 Freemarker 的自动化配置：

```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ freemarker.template.Configuration.class, FreeMarkerConfigurationFactory.class })
@EnableConfigurationProperties(FreeMarkerProperties.class)
@Import({ FreeMarkerServletWebConfiguration.class, FreeMarkerReactiveWebConfiguration.class,
        FreeMarkerNonWebConfiguration.class })
public class FreeMarkerAutoConfiguration {
}
```



从这里可以看出，当 `classpath` 下存在 `freemarker.template.Configuration` 以及 `FreeMarkerConfigurationFactory` 时，配置才会生效，也就是说当我们引入了 `Freemarker` 之后，配置就会生效。但是这里的自动化配置只做了模板位置检查，其他配置则是在导入的 `FreeMarkerServletWebConfiguration` 配置中完成的。那么我们再来看看 `FreeMarkerServletWebConfiguration` 类，部分源码如下：

```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({ Servlet.class, FreeMarkerConfigurer.class })
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
class FreeMarkerServletWebConfiguration extends AbstractFreeMarkerConfiguration {

    protected FreeMarkerServletWebConfiguration(FreeMarkerProperties properties) {
        super(properties);
    }

    @Bean
    @ConditionalOnMissingBean(FreeMarkerConfig.class)
    FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        applyProperties(configurer);
        return configurer;
    }

    @Bean
    freemarker.template.Configuration freeMarkerConfiguration(FreeMarkerConfig configurer) {
        return configurer.getConfiguration();
    }

    @Bean
    @ConditionalOnMissingBean(name = "freeMarkerViewResolver")
    @ConditionalOnProperty(name = "spring.freemarker.enabled", matchIfMissing = true)
    FreeMarkerViewResolver freeMarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        getProperties().applyToMvcViewResolver(resolver);
        return resolver;
    }

    @Bean
    @ConditionalOnEnabledResourceChain
    @ConditionalOnMissingFilterBean(ResourceUrlEncodingFilter.class)
    FilterRegistrationBean<ResourceUrlEncodingFilter> resourceUrlEncodingFilter() {
        FilterRegistrationBean<ResourceUrlEncodingFilter> registration = new FilterRegistrationBean<>(
                new ResourceUrlEncodingFilter());
        registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);
        return registration;
    }

}
```



我们来简单看下这段源码：

1. @ConditionalOnWebApplication 表示当前配置在 web 环境下才会生效。
2. ConditionalOnClass 表示当前配置在存在 Servlet 和 FreeMarkerConfigurer 时才会生效。
3. @AutoConfigureAfter 表示当前自动化配置在 WebMvcAutoConfiguration 之后完成。
4. 代码中，主要提供了 FreeMarkerConfigurer 和 FreeMarkerViewResolver。
5. FreeMarkerConfigurer 是 Freemarker 的一些基本配置，例如 templateLoaderPath、defaultEncoding 等
6. FreeMarkerViewResolver 则是视图解析器的基本配置，包含了viewClass、suffix、allowRequestOverride、allowSessionOverride 等属性。

另外还有一点，在这个类的构造方法中，注入了 FreeMarkerProperties：

```java
@ConfigurationProperties(prefix = "spring.freemarker")
public class FreeMarkerProperties extends AbstractTemplateViewResolverProperties {
        public static final String DEFAULT_TEMPLATE_LOADER_PATH = "classpath:/templates/";
        public static final String DEFAULT_PREFIX = "";
        public static final String DEFAULT_SUFFIX = ".ftlh";
        /**
         * Well-known FreeMarker keys which are passed to FreeMarker's Configuration.
         */
        private Map<String, String> settings = new HashMap<>();
}
```



FreeMarkerProperties 中则配置了 Freemarker 的基本信息，例如模板位置在 `classpath:/templates/` ，再例如模板后缀为 `.ftlh`，那么这些配置我们以后都可以在 application.properties 中进行修改。

如果我们在 SSM 的 XML 文件中自己配置 Freemarker ，也不过就是配置这些东西。现在，这些配置由 FreeMarkerServletWebConfiguration 帮我们完成了。

### 2.2 创建类

首先我们来创建一个 User 类，如下：

```java
public class User {
    private Long id;
    private String username;
    private String address;
    //省略 getter/setter
}
```



再来创建 `UserController`：

```java
@Controller
public class UserController {
    @GetMapping("/index")
    public String index(Model model) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId((long) i);
            user.setUsername("javaboy>>>>" + i);
            user.setAddress("www.javaboy.org>>>>" + i);
            users.add(user);
        }
        model.addAttribute("users", users);
        return "index";
    }
}
```



最后在 freemarker 中渲染数据：

index.ftlh

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<table border="1">
    <tr>
        <td>用户编号</td>
        <td>用户名称</td>
        <td>用户地址</td>
    </tr>
    <#list users as user>
        <tr>
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.address}</td>
        </tr>
    </#list>
</table>
</body>
</html>
```



运行效果如下：

![img](http://img.itboyhub.com/2020/10-3.png)

### 2.3 其他配置

如果我们要修改模版文件位置等，可以在 application.properties 中进行配置：

```properties
spring.freemarker.allow-request-override=false
spring.freemarker.allow-session-override=false
spring.freemarker.cache=false
spring.freemarker.charset=UTF-8
spring.freemarker.check-template-location=true
spring.freemarker.content-type=text/html
spring.freemarker.expose-request-attributes=false
spring.freemarker.expose-session-attributes=false
spring.freemarker.suffix=.ftlh
spring.freemarker.template-loader-path=classpath:/templates/
```



配置文件按照顺序依次解释如下：

1. HttpServletRequest的属性是否可以覆盖controller中model的同名项
2. HttpSession的属性是否可以覆盖controller中model的同名项
3. 是否开启缓存
4. 模板文件编码
5. 是否检查模板位置
6. Content-Type的值
7. 是否将HttpServletRequest中的属性添加到Model中
8. 是否将HttpSession中的属性添加到Model中
9. 模板文件后缀
10. 模板文件位置

好了，整合完成之后，Freemarker 的更多用法，就和在 SSM 中使用 Freemarker 一样了。

## 3. Freemarker 学习

### 3.1 插值与表达式

#### 3.1.1 直接输出值

**字符串**

可以直接输出一个字符串：

```markup
<div>{"hello，我是直接输出的字符串"}</div>
<div>{"我的文件保存在C:\\盘"}</div>
```



\ 需要进行转义。

如果感觉转义太麻烦，可以在目标字符串的引号前增加 r 标记,在 r 标记后的文本内容将会直接输出，像下面这样：

```markup
<div>${r"我的文件保存在C:\盘"}</div>
```



**数字**

在 FreeMarker 中使用数值需要注意以下几点:

1. 数值不能省略小数点前面的0，所以”.5″是错误的写法。
2. 数值 8 , +8 , 8.00 都是相同的。

数字还有一些其他的玩法：

- 将数字以钱的形式展示：

```markup
<#assign num=99>
<div>${num?string.currency}</div>
```



`<#assign num=99>` 表示定义了一个变量 num，值为 99。最终的展示形式是在数字前面出现了一个人民币符号：

![img](http://img.itboyhub.com/2020/20201217155655.png)

- 将数字以百分数的形式展示：

```html
<div>${num?string.percent}</div>
```



展示效果如下：

![img](http://img.itboyhub.com/2020/20201217160444.png)

**布尔**

布尔类型可以直接定义，不需要引号，像下面这样：

```html
<#assign flag=true>
<div>${flag?string("a","b")}</div>
```



首先使用 `<#assign flag=true>` 定义了一个 Boolean 类型的变量，然后在 div 中展示，如果 flag 为 true，则输出 a，否则输出 b。

**集合**

集合也可以现场定义现场输出，例如如下方式定义一个 List 集合并显示出来：

```html
<#list ["星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"] as x>
    ${x}<br/>
</#list>
```



x 代表集合中的每一个元素，最终显示效果如下：

![img](http://img.itboyhub.com/2020/20201217161426.png)

集合中的元素也可以是一个表达式：

```html
<#list [2+2,"javaboy"] as x>
    ${x} <br/>
</#list>
```



集合中的第一个元素就是 2+2 的结果，即 4。

也可以用 `1..5` 表示 1 到 5，`5..1` 表示 5 到 1，例如：

```markup
<#list 5..1 as x>
    ${x} <br/>
</#list>
<#list 1..5 as x>
	${x} <br/>
</#list>
```



也可以定义 Map 集合，Map 集合用一个 `{}` 来描述：

```html
<#list {"name":"javaboy","address":"www.javaboy.org"}?keys as x>
${x}
</#list>
<#list {"name":"javaboy","address":"www.javaboy.org"}?values as x>${x}
</#list>
```



上面两个循环分别表示遍历 Map 中的 key 和 values。

#### 3.1.2 输出变量

创建一个 HelloController，然后添加如下方法：

```java
@Controller
public class HelloController {
    @GetMapping("/hello")
    public String hello(Model model) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User u = new User();
            u.setUsername("javaboy:" + i);
            u.setAddress("www.javaboy.org:" + i);
            users.add(u);
        }
        Map<String, Object> info = new HashMap<>();
        info.put("site", "http://www.itboyhub.com");
        info.put("wechat", "a_java_boy");
        info.put("github", "https://github.com/lenve");
        model.addAttribute("users", users);
        model.addAttribute("info", info);
        model.addAttribute("name", "江南一点雨");
        return "hello";
    }
}
```

接下来我们在模版文件中对这里的普通变量、List 集合以及 Map 分别进行展示。

**普通变量**

普通变量的展示很容易，如下：

```html
<div>${name}</div>
```



**集合**

集合的展示就有很多不同的玩法了。

直接遍历：

```html
<div>
    <table border="1">
        <#list users as u>
            <tr>
                <td>${u.username}</td>
                <td>${u.address}</td>
            </tr>
        </#list>
    </table>
</div>
```



输出集合中第三个元素：

```html
<div>
    ${users[3].username}
</div>
```



输出集合中第 4-6 个元素，即子集合：

```html
<div>
    <table border="1">
        <#list users[3..5] as u>
            <tr>
                <td>${u.username}</td>
                <td>${u.address}</td>
            </tr>
        </#list>
    </table>
</div>
```



遍历时，可以通过 `变量_index` 获取遍历的下标：

```html
<div>
    <table border="1">
        <#list users[3..5] as u>
            <tr>
                <td>${u.username}</td>
                <td>${u.address}</td>
                <td>${u_index}</td>
            </tr>
        </#list>
    </table>
</div>
```



**Map**

直接获取 Map 中的值有不同的写法，如下：

```html
<div>${info.wechat}</div>
<div>${info['site']}</div>
```



获取 Map 中的所有 key，并根据 key 获取 value：

```html
<div>
    <#list info?keys as key>
        <div>${key}--${info[key]}</div>
    </#list>
</div>
```



获取 Map 中的所有 value：

```html
<div>
    <#list info?values as value>
        <div>${value}</div>
    </#list>
</div>
```



#### 3.1.3 字符串操作


字符串的拼接有两种方式：

```html
<div>${"hello${name}"}</div>
<div>${"hello "+ name}</div>
```



也可以从字符串中截取子串：

```html
<div>${name[0]}${name[1]}</div>

<div>${name[0..2]}</div>
```



最终显示效果如下：

![img](http://img.itboyhub.com/2020/20201217200637.png)

#### 3.1.4 集合操作

集合或者 Map 都可以相加。

集合相加：

```html
<div>
    <#list ["星期一","星期二","星期三"] + ["星期四","星期五","星期六","星期天"] as x>
        ${x}
    </#list>
</div>
```



Map 相加：

```markup
<div>
    <#list (info+{"gitee":"https://gitee.com/lenve"})?keys as key>
        <div>${key}</div>
    </#list>
</div>
```



#### 3.1.5 算术运算符

`+`、`—`、`*`、`/`、`%` 运算都是支持的。

```markup
<div>
    <#assign age=99>
    <div>${age*99/99+99-1}</div>
</div>
```



#### 3.1.6 比较运算

比较运算和 Thymeleaf 比较类似:

1. = 或者 判断两个值是否相等。
2. != 判断两个值是否不等。
3. `>` 或者 gt 判断左边值是否大于右边值。
4. `>=` 或者 gte 判断左边值是否大于等于右边值。
5. `<` 或者 lt 判断左边值是否小于右边值。
6. `<=` 或者 lte 判断左边值是否小于等于右边值。

可以看到，带 `<` 或者 `>` 的符号，也都有别名，建议使用别名。

```html
<div>
    <#assign age=99>
    <#if age=99>age=99</#if>
    <#if age gt 99>age gt 99</#if>
    <#if age gte 99>age gte 99</#if>
    <#if age lt 99>age lt 99</#if>
    <#if age lte 99>age lte 99</#if>
    <#if age!=99>age!=99</#if>
    <#if age==99>age==99</#if>
</div>
```



#### 3.1.7 逻辑运算

逻辑运算符有三个:

- 逻辑与 `&&`
- 逻辑或 `||`
- 逻辑非 `!`

逻辑运算符只能作用于布尔值,否则将产生错误。

```html
<div>
    <#assign age=99>
    <#if age=99 && 1==1>age=99 && 1==1</#if>
    <#if age=99 || 1==0>age=99 || 1==0</#if>
    <#if !(age gt 99)>!(age gt 99)</#if>
</div>
```



#### 3.1.8 空值处理

为了处理缺失变量,Freemarker 提供了两个运算符:

1. `!`：指定缺失变量的默认值
2. `??`：判断某个变量是否存在

**! 对象为空**

如果某个变量不存在，则设置其为 javaboy，如下：

```html
<div>${aaa!"javaboy"}</div>
```

如果某个变量不存在，则设置其为空字符串，如下：

即，`!` 后面的东西如果省略了，默认就是空字符串。

```html
<div>${aaa!}</div>
```

**！取反**

```html
<div>
    <#assign age=20>
    <#if !(age gt 99)>年龄不大于99</#if>
</div>
```





**?? 判断对象是否为空**

```html

<div>
    <#assign bb="tom"/>
    <div>
        <#if bb??>${bb}
        </#if>
    </div>
</div>
```

**?判断是否为空**

和？？类似，但要在？后面加上关键字

```html
<div>
    <#assign bb="tom"/>
    <div>
        <#if bb?exists>
            ${bb}
        </#if>
    </div>
</div>
```



### 3.2 内建函数

内建函数涉及到的东西比较多，可以参考官方文档:http://freemarker.foofun.cn/ref_builtins.html。

这里仅说一些比较常用的内建函数。

**cap_first**

使字符串第一个字母大写：

```markup
<div>${"hello"?cap_first}</div>
```



**lower_case**

将字符串转换成小写：

```html
<div>${"HELLO"?lower_case}</div>
```



**upper_case**

将字符串转换成大写：

```html
<div>${"hello"?upper_case}</div>
```



**trim**

去掉字符串前后的空白字符：

```html
<div>${" hello"?trim}</div>
```



**size**

获取序列中元素的个数：

```html
<div>${users?size}</div>
```



**int**

取得数字的整数部分,结果带符号：

```html
<div>${3.14?int}</div>
```



**日期格式化**

```html
<div>${birthday?string("yyyy-MM-dd")}</div>
```



### 3.2 常用指令

#### 3.2.1 if/else

分支控制指令，作用类似于 Java 语言中的 if：

```html
<div>
    <#assign age=23>
    <#if (age>60)>老年人
    <#elseif (age>40)>中年人
    <#elseif (age>20)>青年人
    <#else> 少年人
    </#if>
</div>
```



比较符号中用了 `()`，因此不用转义。

#### 3.2.2 switch

分支指令，类似于 Java 中的 switch：

```html
<div>
    <#assign age=99>
    <#switch age>
        <#case 23>23<#break>
        <#case 24>24<#break>
        <#default>9999
    </#switch>
</div>
```



`<#break>` 是提前退出，也可以用在 `<#list>` 中。

#### 3.2.3 include

include 可以包含一个外部页面进来。

```markup
<#include "./javaboy.ftlh">
```

#### 3.2.4 macro

macro 用来定义一个宏。

我们可以自定义一个名为 book 的宏，并引用它：

```markup
<#macro book>
    三国演义
</#macro>
<@book/>
```



最终页面中会输出宏中所定义的内容。

在定义宏的时候，也可以传入参数，那么引用时，也需要传入参数：

```html
<#macro book bs>
    <table border="1">
        <#list bs as b>
            <tr>
                <td>${b}</td>
            </tr>
        </#list>
    </table>
</#macro>
<@book ["三国演义","水浒传"]/>
```



bs 就是需要传入的参数。可以通过传入多个参数，多个参数跟在 bs 后面即可，中间用空格隔开。

还可以使用 `<#nested>` 引入用户自定义指令的标签体，像下面这样：

```html
<#macro book bs>
    <table border="1">
        <#list bs as b>
            <tr>
                <td>${b}</td>
            </tr>
        </#list>
    </table>
    <#nested>
</#macro>
<@book bs=["三国演义","西游记","红楼梦","水浒传"]>
    <h3>四大名著</h3>
</@book>
```



在宏定义的时候，`<#nested>` 相当于是一个占位符，在调用的时候，`<@book>` 标签中的内容会出现在 `<#nested>` 位置。

前面的案例中，宏都是定义在当前页面中，宏也可以定义在一个专门的页面中。新建 myjavaboy.ftlh 页面，内容如下：

```html
<#macro book bs>
    <table border="1">
        <#list bs as b>
            <tr>
                <td>${b}</td>
            </tr>
        </#list>
    </table>
    <#nested>
</#macro>
```



此时，需要先通过 `<#import>` 标签导入宏，然后才能调用，如下：

```html
<#import "./MyMacro.ftlh" as com>
<@com.book bs=["三国演","西游记","红楼梦","水浒传"]>
    <h3>四大名著</h3>
</@com.book>
<hr>
```



#### 3.2.5 noparse

如果想在页面展示一些 Freemarker 语法而不被渲染，则可以使用 noparse 标签，如下：

```html
<#noparse>
<#import "./myjavaboy.ftlh" as com>
<@com.book bs=["三国演义","水浒传"]>
    <h1>hello javaboy!</h1>
</@com.book>
</#noparse>
```



显示效果如下：

![img](http://img.itboyhub.com/2020/20201217214402.png)

## 4. 自动生成代码实践

松哥录制了一个利用 Freemarker 自动生成代码的视频，感兴趣的小伙伴可以参考：

[代码自动生成很难吗？松哥手把手教大家整一个！](https://mp.weixin.qq.com/s/2Ovqwn1hd40D7FtKE60HKw)

## 5. 项目实践

今年 6 月份我还录制了一个 SSM+Freemarker 的项目，感兴趣的小伙伴可以参考：

[松哥憋了个大招！一套免费的视频教程又出炉啦！](https://mp.weixin.qq.com/s/8N4ZGUwQYo51i1zEQZ6f9Q)

