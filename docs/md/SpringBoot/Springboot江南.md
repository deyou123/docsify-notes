## 第二章

## 2.1 banner

## 2.3 Tomcat 

配置内置服务器默认Tomcat 

```
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-web</artifactId>
<!--            <exclusions>
<exclusion>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-tomcat</artifactId>
</exclusion>
</exclusions>-->
</dependency>

<!--        <dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-undertow</artifactId>
</dependency>-->
```

application.properties

```yml
#不启动内嵌的web 服务器
# spring.main.web-application-type=none

#server.port=8888
# 关闭所有的 http 端点
#server.port=-1
# 随机端口
#server.port=0
# 压缩响应数据
server.compression.enabled=true
```



Tomcat 内部日志

```yml
# 生成的访问日志将在该目录下
server.tomcat.basedir=my-tomcat
# 开启访问日志，默认的日志位置在项目运行的临时目录中，默认生成的日志格式 access_log.2020-12-10.log
server.tomcat.accesslog.enabled=true
# 生成日志文件名的前缀，默认是 access_log
server.tomcat.accesslog.prefix=javaboy_log
# 生成的日志文件后缀
server.tomcat.accesslog.suffix=.log
# 日志文件名中的日期格式
server.tomcat.accesslog.file-date-format=.yyyyMMdd

# 生成的日志文件内容格式也是可以调整的
# %h 请求的客户端 IP
# %l 用户的身份
# %u 用户名
# %t 请求时间
# %r 请求地址
# %s 响应的状态码
# %b 响应的大小
server.tomcat.accesslog.pattern=%h %l %u %t \"%r\" %s %b

# 服务器内部日志开启

#logging.level.org.apache.tomcat=debug
#logging.level.org.apache.catalina=debug
```

2.4 Spring boot 日志

之前录过一个视频和大家分享 Spring Boot 日志问题，但是总感觉差点意思，因此松哥打算再通过一篇文章来和大家捋一捋 Java 中的日志问题，顺便我们把 Spring Boot 中的日志问题也说清楚。



# Spring boot 日志

## 1. Java 日志概览

说到 Java 日志，很多初学者可能都比较懵，因为这里涉及到太多东西了：`Apache Commons Logging`、`Slf4j`、`Log4j`、`Log4j2`、`Logback`、`Java Util Logging` 等等，这些框架各自有什么作用？他们之间有什么区别？

### 1.1 总体概览

下面这张图很好的展示了 Java 中的日志体系：

![img](http://img.itboyhub.com/2020/20201213205509.png)

可以看到，Java 中的日志框架主要分为两大类：**日志门面**和**日志实现**。

**日志门面**

日志门面定义了一组日志的接口规范，它并不提供底层具体的实现逻辑。`Apache Commons Logging` 和 `Slf4j` 就属于这一类。

**日志实现**

日志实现则是日志具体的实现，包括日志级别控制、日志打印格式、日志输出形式（输出到数据库、输出到文件、输出到控制台等）。`Log4j`、`Log4j2`、`Logback` 以及 `Java Util Logging` 则属于这一类。

将日志门面和日志实现分离其实是一种典型的门面模式，这种方式可以让具体业务在不同的日志实现框架之间自由切换，而不需要改动任何代码，开发者只需要掌握日志门面的 API 即可。

日志门面是不能单独使用的，它必须和一种具体的日志实现框架相结合使用。

那么日志框架是否可以单独使用呢？

技术上来说当然没问题，但是我们一般不会这样做，因为这样做可维护性很差，而且后期扩展不易。例如 A 开发了一个工具包使用 Log4j 打印日志，B 引用了这个工具包，但是 B 喜欢使用 Logback 打印日志，此时就会出现一个业务使用两个甚至多个日志框架，开发者也需要维护多个日志的配置文件。因此我们都是用日志门面打印日志。

### 1.2 日志级别

使用日志级别的好处在于，调整级别，就可以屏蔽掉很多调试相关的日志输出。不同的日志实现定义的日志级别不太一样，不过也都大同小异。

**Java Util Logging**

`Java Util Logging` 定义了 7 个日志级别，从严重到普通依次是：

- SEVERE
- WARNING
- INFO
- CONFIG
- FINE
- FINER
- FINEST

因为默认级别是 INFO，因此 INFO 级别以下的日志，不会被打印出来。

**Log4j**

`Log4j` 定义了 8 个日志级别（除去 OFF 和 ALL，可以说分为 6 个级别），从严重到普通依次是：

- OFF：最高等级的，用于关闭所有日志记录。
- FATAL：重大错误，这种级别可以直接停止程序了。
- ERROR：打印错误和异常信息，如果不想输出太多的日志，可以使用这个级别。
- WARN：警告提示。
- INFO：用于生产环境中输出程序运行的一些重要信息，不能滥用。
- DEBUG：用于开发过程中打印一些运行信息。
- TRACE
- ALL 最低等级的，用于打开所有日志记录。

**Logback**

`Logback` 日志级别比较简单，从严重到普通依次是：

- ERROR
- WARN
- INFO
- DEBUG
- TRACE

### 1.3 综合对比

`Java Util Logging` 系统在 `JVM` 启动时读取配置文件并完成初始化，一旦应用程序开始运行，就无法修改配置。另外，这种日志实现配置也不太方便，只能在 `JVM` 启动时传递参数，像下面这样：

```java
-Djava.util.logging.config.file=<config-file-name>。
```

Java

Copy

由于这些局限性，导致 `Java Util Logging` 并未广泛使用。

`Log4j` 虽然配置繁琐，但是一旦配置完成，使用起来就非常方便，只需要将相关的配置文件放到 `classpath` 下即可。在很多情况下，`Log4j` 的配置文件我们可以在不同的项目中反复使用。

`Log4j` 可以和 `Apache Commons Logging` 搭配使用，`Apache Commons Logging` 会自动搜索并使用 `Log4j`，如果没有找到 `Log4j`，再使用 `Java Util Logging`。

比 `Log4j` + `Apache Commons Logging` 组合更得人心的是 `Slf4j` + `Logback` 组合。

`Logback` 是 `Slf4j` 的原生实现框架，它也出自 `Log4j` 作者（Ceki Gülcü）之手，但是相比 `Log4j`，它拥有更多的优点、特性以及更强的性能。

### 1.4 最佳实践

- 如果不想添加任何依赖，使用 `Java Util Logging` 或框架容器已经提供的日志接口。
- 如果比较在意性能，推荐：`Slf4j` + `Logback`。
- 如果项目中已经使用了 `Log4j` 且没有发现性能问题，推荐组合为：`Slf4j` + `Log4j2`。

## 2. Spring Boot 日志实现

Spring Boot 使用 `Apache Commons Logging` 作为内部的日志框架门面，它只是一个日志接口，在实际应用中需要为该接口来指定相应的日志实现。

Spring Boot 默认的日志实现是 `Logback`。这个很好查看：随便启动一个 Spring Boot 项目，从控制台找一行日志，例如下面这样：

![img](http://img.itboyhub.com/2020/20201214092816.png)

考虑到最后的 prod 是一个可以变化的字符，我们在项目中全局搜索：`The following profiles are active`，结果如下：

![img](http://img.itboyhub.com/2020/20201214092956.png)

在日志输出的那一行 debug。然后再次启动项目，如下图：

![img](http://img.itboyhub.com/2020/20201214093147.png)

此时我们就可以看到真正的日志实现是 `Logback`。

其他的诸如 `Java Util Logging`、`Log4j` 等框架，Spring Boot 也有很好的支持。

在 Spring Boot 项目中，只要添加了如下 web 依赖，日志依赖就自动添加进来了：

```markup
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

XML

Copy

### 2.1 Spring Boot 日志配置

Spring Boot 的日志系统会自动根据 classpath 下的内容选择合适的日志配置，在这个过程中首选 Logback。

如果开发者需要修改日志级别，只需要在 application.properties 文件中通过 `logging.level 前缀+包名` 的形式进行配置即可，例如下面这样：

```properties
logging.level.org.springframework.web=debug
logging.level.org.hibernate=error
```

.properties

Copy

如果你想将日志输出到文件，可以通过如下配置指定日志文件名：

```properties
logging.file.name=javaboy.log
```

.properties

Copy

logging.file.name 可以只指定日志文件名，也可以指定日志文件全路径，例如下面这样：

```properties
logging.file.name=/Users/sang/Documents/javaboy/javaboy.log
```

.properties

Copy

如果你只是想重新定义输出日志文件的路径，也可以使用 `logging.file.path` 属性，如下：

```properties
logging.file.path=/Users/sang/Documents/javaboy
```

.properties

Copy

如果想对输出到文件中的日志进行精细化管理，还有如下一些属性可以配置：

- logging.logback.rollingpolicy.file-name-pattern：日志归档的文件名，日志文件达到一定大小之后，自动进行压缩归档。
- logging.logback.rollingpolicy.clean-history-on-start：是否在应用启动时进行归档管理。
- logging.logback.rollingpolicy.max-file-size：日志文件大小上限，达到该上限后，会自动压缩。
- logging.logback.rollingpolicy.total-size-cap：日志文件被删除之前，可以容纳的最大大小。
- logging.logback.rollingpolicy.max-history：日志文件保存的天数。

日志文件归档这块，小伙伴们感兴趣可以自己试下，可以首先将 max-file-size 属性调小，这样方便看到效果：

```properties
logging.logback.rollingpolicy.max-file-size=1MB
```

.properties

Copy

然后添加如下接口：

```java
@RestController
public class HelloController {
    private static final Logger logger = getLogger(HelloController.class);
    @GetMapping("/hello")
    public void hello() {
        for (int i = 0; i < 100000; i++) {
            logger.info("hello javaboy");
        }
    }
}
```

Java

Copy

访问该接口，可以看到最终生成的日志文件被自动压缩了：

![img](http://img.itboyhub.com/2020/20201214115027.png)

application.properties 中还可以配置日志分组。

日志分组能够把相关的 logger 放到一个组统一管理。

例如我们可以定义一个 tomcat 组：

```properties
logging.group.tomcat=org.apache.catalina,org.apache.coyote, org.apache.tomcat
```

.properties

Copy

然后统一管理 tomcat 组中的所有 logger：

```properties
logging.level.tomcat=TRACE
```

.properties

Copy

Spring Boot 中还预定义了两个日志分组 web 和 sql，如下：

![img](http://img.itboyhub.com/2020/20201214124953.png)

不过在 application.properties 中只能实现对日志一些非常简单的配置，如果想实现更加细粒度的日志配置，那就需要使用日志实现的原生配置，例如 `Logback` 的 `classpath:logback.xml`，`Log4j` 的 `classpath:log4j.xml` 等。如果这些日志配置文件存在于 classpath 下，那么默认情况下，Spring Boot 就会自动加载这些配置文件。

### 2.2 Logback 配置

#### 2.2.1 基本配置

默认的 `Logback` 配置文件名有两种：

- `logback.xml`：这种配置文件会直接被日志框架加载。
- `logback-spring.xml`：这种配置文件不会被日志框架直接加载，而是由 Spring Boot 去解析日志配置，可以使用 Spring Boot 的高级 Profile 功能。

Spring Boot 中为 `Logback` 提供了四个默认的配置文件，位置在 `org/springframework/boot/logging/logback/`，分别是：

- defaults.xml：提供了公共的日志配置，日志输出规则等。
- console-appender.xml：使用 CONSOLE_LOG_PATTERN 添加一个ConsoleAppender。
- file-appender.xml：添加一个 RollingFileAppender。
- base.xml：为了兼容旧版 Spring Boot 而提供的。

如果需要自定义 `logback.xml` 文件，可以在自定义时使用这些默认的配置文件，也可以不使用。一个典型的 `logback.xml` 文件如下（resources/logback.xml）：

```markup
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="org/springframework/boot/logging/logback/defaults.xml"/>
    <include resource="org/springframework/boot/logging/logback/console-appender.xml" />
    <root level="INFO">
        <appender-ref ref="CONSOLE" />
    </root>
    <logger name="org.springframework.web" level="DEBUG"/>
</configuration>
```

XML

Copy

可以通过 include 引入 Spring Boot 已经提供的配置文件，也可以自定义。

#### 2.2.2 输出到文件

如果想禁止控制台的日志输出，转而将日志内容输出到一个文件，我们可以自定义一个 `logback-spring.xml` 文件，并引入前面所说的 `file-appender.xml` 文件。

像下面这样（`resources/logback-spring.xml`）：

```markup
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="org/springframework/boot/logging/logback/defaults.xml" />
    <property name="LOG_FILE" value="{LOG_FILE:-{LOG_PATH:-{LOG_TEMP:-{java.io.tmpdir:-/tmp}}/}spring.log}"/>
    <include resource="org/springframework/boot/logging/logback/file-appender.xml" />
    <root level="INFO">
        <appender-ref ref="FILE" />
    </root>
</configuration>
```

XML

Copy

### 2.3 Log4j 配置

如果 classpath 下存在 `Log4j2` 的依赖，Spring Boot 会自动进行配置。

默认情况下 classpath 下当然不存在 `Log4j2` 的依赖，如果想使用 `Log4j2`，可以排除已有的 `Logback`，然后再引入 `Log4j2`，如下：

```markup
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
</dependency>
```



`Log4j2` 的配置就比较容易了，在 reources 目录下新建 log4j2.xml 文件，内容如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration status="warn">
    <properties>
        <Property name="app_name">logging</Property>
        <Property name="log_path">logs/${app_name}</Property>
    </properties>
    <appenders>
        <console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="[%d][%t][%p][%l] %m%n" />
        </console>
        <RollingFile name="RollingFileInfo" fileName="${log_path}/info.log"
                     filePattern="${log_path}/$${date:yyyy-MM}/info-%d{yyyy-MM-dd}-%i.log.gz">
            <Filters>
                <ThresholdFilter level="INFO" />
                <ThresholdFilter level="WARN" onMatch="DENY"
                                 onMismatch="NEUTRAL" />
            </Filters>
            <PatternLayout pattern="[%d][%t][%p][%c:%L] %m%n" />
            <Policies>
                <TimeBasedTriggeringPolicy interval="1" modulate="true" />
                <SizeBasedTriggeringPolicy size="2 MB" />
            </Policies>
            <DefaultRolloverStrategy compressionLevel="0" max="10"/>
        </RollingFile>
        <RollingFile name="RollingFileWarn" fileName="${log_path}/warn.log"
                     filePattern="${log_path}/$${date:yyyy-MM}/warn-%d{yyyy-MM-dd}-%i.log.gz">
            <Filters>
                <ThresholdFilter level="WARN" />
                <ThresholdFilter level="ERROR" onMatch="DENY"
                                 onMismatch="NEUTRAL" />
            </Filters>
            <PatternLayout pattern="[%d][%t][%p][%c:%L] %m%n" />
            <Policies>
                <TimeBasedTriggeringPolicy interval="1" modulate="true" />
                <SizeBasedTriggeringPolicy size="2 MB" />
            </Policies>
            <DefaultRolloverStrategy compressionLevel="0" max="10"/>
        </RollingFile>

        <RollingFile name="RollingFileError" fileName="${log_path}/error.log"
                     filePattern="${log_path}/$${date:yyyy-MM}/error-%d{yyyy-MM-dd}-%i.log.gz">
            <ThresholdFilter level="ERROR" />
            <PatternLayout pattern="[%d][%t][%p][%c:%L] %m%n" />
            <Policies>
                <TimeBasedTriggeringPolicy interval="1" modulate="true" />
                <SizeBasedTriggeringPolicy size="2 MB" />
            </Policies>
            <DefaultRolloverStrategy compressionLevel="0" max="10"/>
        </RollingFile>
    </appenders>
    <loggers>
        <root level="info">
            <appender-ref ref="Console" />
            <appender-ref ref="RollingFileInfo" />
            <appender-ref ref="RollingFileWarn" />
            <appender-ref ref="RollingFileError" />
        </root>
    </loggers>
</configuration>
```



首先在 properties 节点中指定了应用名称以及日志文件位置。

然后通过几个不同的 RollingFile 对不同级别的日志分别处理，不同级别的日志将输出到不同的文件，并按照各自的命名方式进行压缩。

这段配置比较程式化，小伙伴们可以保存下来做成 IntelliJ IDEA 模版以便日常使用。



虽然现在流行前后端分离，但是后端模版在一些关键地方还是非常有用的，例如邮件模版、代码模版等。当然也不排除一些古老的项目后端依然使用动态模版。

Thymeleaf 简洁漂亮、容易理解，并且完美支持 HTML5，可以直接打开静态页面，同时不新增标签，只需增强属性，这样也降低了学习成本。

因此松哥今天花点时间和大家仔细分享一下 Thymeleaf。

# Springboot Thymeleaf(一)

## 1. Thymeleaf 简介

Thymeleaf 是新一代 Java 模板引擎，它类似于 Velocity、FreeMarker 等传统 Java 模板引擎，但是与传统 Java 模板引擎不同的是，Thymeleaf 支持 HTML 原型。

它既可以让前端工程师在浏览器中直接打开查看样式，也可以让后端工程师结合真实数据查看显示效果，同时，SpringBoot 提供了 Thymeleaf 自动化配置解决方案，因此在 SpringBoot 中使用 Thymeleaf 非常方便。

事实上， Thymeleaf 除了展示基本的 HTML ，进行页面渲染之外，也可以作为一个 HTML 片段进行渲染，例如我们在做邮件发送时，可以使用 Thymeleaf 作为邮件发送模板。

另外，由于 Thymeleaf 模板后缀为 `.html`，可以直接被浏览器打开，因此，预览时非常方便。

## 2. 整合 Spring Boot

### 2.1 基本用法

Spring Boot 中整合 Thymeleaf 非常容易，只需要创建项目时添加 Thymeleaf 即可：

![img](http://img.itboyhub.com/2020/20201215105627.png)

创建完成后，pom.xml 依赖如下：

```markup
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

XML

Copy

当然，Thymeleaf 不仅仅能在 Spring Boot 中使用，也可以使用在其他地方，只不过 Spring Boot 针对 Thymeleaf 提供了一整套的自动化配置方案，这一套配置类的属性在 `org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties` 中，部分源码如下：

```java
@ConfigurationProperties(prefix = "spring.thymeleaf")
public class ThymeleafProperties {
        private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;
        public static final String DEFAULT_PREFIX = "classpath:/templates/";
        public static final String DEFAULT_SUFFIX = ".html";
        private boolean checkTemplate = true;
        private boolean checkTemplateLocation = true;
        private String prefix = DEFAULT_PREFIX;
        private String suffix = DEFAULT_SUFFIX;
        private String mode = "HTML";
        private Charset encoding = DEFAULT_ENCODING;
        private boolean cache = true;
        //...
}
```

Java

Copy

1. 首先通过 `@ConfigurationProperties` 注解，将 `application.properties` 前缀为 `spring.thymeleaf` 的配置和这个类中的属性绑定。
2. 前三个 `static` 变量定义了默认的编码格式、视图解析器的前缀、后缀等。
3. 从前三行配置中，可以看出来，`Thymeleaf` 模板的默认位置在 `resources/templates` 目录下，默认的后缀是 `html` 。
4. 这些配置，如果开发者不自己提供，则使用 默认的，如果自己提供，则在 `application.properties` 中以 `spring.thymeleaf` 开始相关的配置。

而我们刚刚提到的，Spring Boot 为 Thymeleaf 提供的自动化配置类，则是 `org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration` ，部分源码如下：

```java
@Configuration
@EnableConfigurationProperties(ThymeleafProperties.class)
@ConditionalOnClass({ TemplateMode.class, SpringTemplateEngine.class })
@AutoConfigureAfter({ WebMvcAutoConfiguration.class, WebFluxAutoConfiguration.class })
public class ThymeleafAutoConfiguration {
}
```

Java

Copy

可以看到，在这个自动化配置类中，首先导入 `ThymeleafProperties` ，然后 `@ConditionalOnClass` 注解表示当当前系统中存在 `TemplateMode` 和 `SpringTemplateEngine` 类时，当前的自动化配置类才会生效，即只要项目中引入了 `Thymeleaf` 相关的依赖，这个配置就会生效。

这些默认的配置我们几乎不需要做任何更改就可以直接使用了。如果开发者有特殊需求，则可以在 application.properties 中配置以 spring.thymeleaf 开头的属性即可。

接下来我们就可以创建 Controller 了，实际上引入 Thymeleaf 依赖之后，我们可以不做任何配置。新建的 IndexController 如下：

```java
@Controller
public class IndexController {
    @GetMapping("/index")
    public String index(Model model) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User u = new User();
            u.setId((long) i);
            u.setName("javaboy:" + i);
            u.setAddress("深圳:" + i);
            users.add(u);
        }
        model.addAttribute("users", users);
        return "index";
    }
}
public class User {
    private Long id;
    private String name;
    private String address;
    //省略 getter/setter
}
```

Java

Copy

在 `IndexController` 中返回逻辑视图名+数据，逻辑视图名为 `index` ，意思我们需要在 `resources/templates` 目录下提供一个名为 `index.html` 的 `Thymeleaf` 模板文件。

- 创建 Thymeleaf

```markup
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<table border="1">
    <tr>
        <td>编号</td>
        <td>用户名</td>
        <td>地址</td>
    </tr>
    <tr th:each="user : {users}">
        <td th:text="{user.id}"></td>
        <td th:text="{user.name}"></td>
        <td th:text="{user.address}"></td>
    </tr>
</table>
</body>
</html>
```

HTML

Copy

在 `Thymeleaf` 中，通过 `th:each` 指令来遍历一个集合，数据的展示通过 `th:text` 指令来实现，

注意 `index.html` 最上面引入 `thymeleaf` 名称空间（最新版并无强制要求）。

配置完成后，就可以启动项目了，访问 /index 接口，就能看到集合中的数据了：

![img](http://img.itboyhub.com/2020/20201215105758.png)

### 2.2 手动渲染

前面我们说的是返回一个 Thymeleaf 模板，我们也可以手动渲染 Thymeleaf 模板，这个一般在邮件发送时候有用，例如我在 resources/templates 目录下新建一个邮件模板，如下：

```markup
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<p>hello 欢迎 <span th:text="{username}"></span>加入 XXX 集团，您的入职信息如下：</p>
<table border="1">
    <tr>
        <td>职位</td>
        <td th:text="{position}"></td>
    </tr>
    <tr>
        <td>薪水</td>
        <td th:text="${salary}"></td>
    </tr>
</table>
<img src="http://www.javaboy.org/images/sb/javaboy.jpg" alt="">
</body>
</html>
```

HTML

Copy

这一个 HTML 模板中，有几个变量，我们要将这个 HTML 模板渲染成一个 String 字符串，再把这个字符串通过邮件发送出去，那么如何手动渲染呢？

```java
@Autowired
TemplateEngine templateEngine;
@Test
public void test1() throws MessagingException {
    Context context = new Context();
    context.setVariable("username", "javaboy");
    context.setVariable("position", "Java工程师");
    context.setVariable("salary", 99999);
    String mail = templateEngine.process("mail", context);
    //省略邮件发送
}
```

Java

Copy

1. 渲染时，我们需要首先注入一个 TemplateEngine 对象，这个对象就是在 Thymeleaf 的自动化配置类中配置的（即当我们引入 Thymeleaf 的依赖之后，这个实例就有了）。
2. 然后构造一个 Context 对象用来存放变量。
3. 调用 process 方法进行渲染，该方法的返回值就是渲染后的 HTML 字符串，然后我们将这个字符串发送出去。

## 3. Thymeleaf 细节

前面两个案例让小伙伴们大致上理解了在 Spring Boot 中要如何使用 Thymeleaf，接下来，松哥将详细介绍 Thymeleaf 本身的一些具体用法。

### 3.1 标准表达式语法

#### 3.1.1 简单表达式

**`${...}`**

直接使用 `th:xx = "${}"` 获取对象属性。这个在前面的案例中已经演示过了，不再赘述。

**`\*{...}`**

可以像 `${...}` 一样使用，也可以通过 `th:object` 获取对象，然后使用 `th:xx = "*{}"` 获取对象属性，这种简写风格极为清爽，推荐大家在实际项目中使用。

```markup
<table border="1" th:object="${user}">
<tr>
    <td>用户名</td>
    <td th:text="*{username}"></td>
</tr>
<tr>
    <td>地址</td>
    <td th:text="*{address}"></td>
</tr>
</table>
```

HTML

Copy

**`#{...}`**

通常的国际化属性：`#{...}` 用于获取国际化语言翻译值。

在 resources 目录下新建两个文件：messages.properties 和 messages_zh_CN.properties，内容如下：

messages.properties：

```properties
message = javaboy
```

.properties

Copy

messages_zh_CN.properties：

```properties
message = 江南一点雨
```

.properties

Copy

然后在 thymeleaf 中引用 message，系统会根据浏览器的语言环境显示不同的值：

```markup
<div th:text="#{message}"></div>
```

HTML

Copy

**`@{...}`**

- **引用绝对 URL：**

```markup
<script type="text/javascript" th:src="@{http://localhost:8080/hello.js}"></script>
```

HTML

Copy

等价于：

```markup
<script type="text/javascript" src="http://localhost:8080/hello.js"></script>
```

HTML

Copy

- **上下文相关的 URL：**

首先在 application.properties 中配置 Spring Boot 的上下文，以便于测试：

```properties
server.servlet.context-path=/myapp
```

.properties

Copy

引用路径：

```markup
<script type="text/javascript" th:src="@{/hello.js}"></script>
```

HTML

Copy

等价于：

```markup
<script type="text/javascript" src="/myapp/hello.js"></script>
```

HTML

Copy

- **相对 URL：**

这个相对是指相对于服务器的 URL，例如如下引用：

```markup
<script type="text/javascript" th:src="@{~/hello.js}"></script>
```

HTML

Copy

等价于：

```markup
<script type="text/javascript" src="/hello.js"></script>
```

HTML

Copy

应用程序的上下文 **/myapp** 将被忽略。

- **协议相对 URL：**

```markup
<script type="text/javascript" th:src="@{//localhost:8080/hello.js}"></script>
```

HTML

Copy

等价于：

```markup
<script type="text/javascript" src="//localhost:8080/hello.js"></script>
```

HTML

Copy

- **带参数的 URL：**

```markup
<script type="text/javascript" th:src="@{//localhost:8080/hello.js(name='javaboy',age=99)}"></script>
```

HTML

Copy

等价于：

```markup
<script type="text/javascript" th:src="//localhost:8080/hello.js?name=javaboy&age=99"></script>
```

HTML

Copy

**`~{...}`**

片段表达式是 Thymeleaf 的特色之一，细粒度可以达到标签级别，这是 JSP 无法做到的。片段表达式拥有三种语法：

- `~{ viewName }`：表示引入完整页面
- `~{ viewName ::selector}`：表示在指定页面寻找片段，其中 selector 可为片段名、jquery选择器等
- `~{ ::selector}`： 表示在当前页寻找

举个简单例子。

在 resources/templates 目录下新建 my_fragment.html 文件，内容如下：

```markup
<div th:fragment="javaboy_link"><a href="http://www.javaboy.org">www.javaboy</a></div>
<div th:fragment="itboyhub_link"><a href="http://www.itboyhub.com">www.itboyhub.com</a></div>
```

HTML

Copy

这里有两个 div，通过 th:fragment 来定义片段，两个 div 分别具有不同的名字。

然后在另外一个页面中引用该片段：

```markup
<table border="1" th:object="${user}" th:fragment="aaa">
<tr>
    <td>用户名</td>
    <td th:text="*{username}"></td>
</tr>
<tr>
    <td>地址</td>
    <td th:text="*{address}"></td>
</tr>
</table>
<hr>
<div th:replace="my_fragment.html"></div>
<hr>
<div th:replace="~{my_fragment.html::javaboy_link}"></div>
<hr>
<div th:replace="~{::aaa}"></div>
```

HTML

Copy

通过 th:replace 来引用片段。第一个表示引用完整的 `my_fragment.html` 页面；第二个表示引用 `my_fragment.html` 中的名为 `javaboy_link` 的片段；第三个表示引用当前页面名为 aaa 的片段，也就是上面那个 table。

#### 3.1.2 字面量

这些是一些可以直接写在表达式中的字符，主要有如下几种：

- 文本字面量： ‘one text’, ‘Another one!’,…
- 数字字面量： 0, 34, 3.0, 12.3,…
- 布尔字面量： true, false
- Null字面量： null
- 字面量标记：one, sometext, main,…

案例：

```markup
<div th:text="'这是 文本字面量(有空格)'"></div>
<div th:text="javaboy"></div>
<div th:text="99"></div>
<div th:text="true"></div>
```

HTML

Copy

如果文本是英文，并且不包含空格、逗号等字符，可以不用加单引号。

#### 3.1.3 文本运算

文本可以使用 `+` 进行拼接。

```markup
<div th:text="'hello '+'javaboy'"></div>
<div th:text="'hello '+${user.username}"></div>
```

HTML

Copy

如果字符串中包含变量，也可以使用另一种简单的方式，叫做字面量置换，用 `|` 代替 `'...' + '...'`，如下：

```markup
<div th:text="|hello {user.username}|"></div>
<div th:text="'hello '+{user.username}+' '+|Go ${user.address}|"></div>
```

HTML

Copy

#### 3.1.4 算术运算

算术运算有：`+`, `-`, `*`, `/` 和 `%`。

```markup
<div th:with="age=(99*99/99+99-1)">
    <div th:text="${age}"></div>
</div>
```

HTML

Copy

th:with 定义了一个局部变量 age，在其所在的 div 中可以使用该局部变量。

#### 3.1.5 布尔运算

- 二元运算符：and, or
- 布尔非（一元运算符）：!, not

案例：

```markup
<div th:with="age=(99*99/99+99-1)">
    <div th:text="9 eq 9 or 8 ne 8"></div>
    <div th:text="!(9 eq 9 or 8 ne 8)"></div>
    <div th:text="not(9 eq 9 or 8 ne 8)"></div>
</div>
```

HTML

Copy

#### 3.1.6 比较和相等

表达式里的值可以使用 `>`, `<`, `>=` 和 `<=` 符号比较。`==` 和 `!=` 运算符用于检查相等（或者不相等）。注意 `XML`规定 `<` 和 `>` 标签不能用于属性值，所以应当把它们转义为 `<` 和 `>`。

如果不想转义，也可以使用别名：gt (>)；lt (<)；ge (>=)；le (<=)；not (!)。还有 eq (), neq/ne (!=)。

举例：

```markup
<div th:with="age=(99*99/99+99-1)">
    <div th:text="{age} eq 197"></div>
    <div th:text="{age} ne 197"></div>
    <div th:text="{age} ge 197"></div>
    <div th:text="{age} gt 197"></div>
    <div th:text="{age} le 197"></div>
    <div th:text="{age} lt 197"></div>
</div>
```

HTML

Copy

#### 3.1.7 条件运算符

类似于我们 Java 中的三目运算符。

```markup
<div th:with="age=(99*99/99+99-1)">
    <div th:text="(${age} ne 197)?'yes':'no'"></div>
</div>
```

HTML

Copy

其中，: 后面的部分可以省略，如果省略了，又同时计算结果为 false 时，将返回 null。

#### 3.1.8 内置对象

基本内置对象：

- \#ctx：上下文对象。
- \#vars: 上下文变量。
- \#locale：上下文区域设置。
- \#request：（仅在 Web 上下文中）HttpServletRequest 对象。
- \#response：（仅在 Web 上下文中）HttpServletResponse 对象。
- \#session：（仅在 Web 上下文中）HttpSession 对象。
- \#servletContext：（仅在 Web 上下文中）ServletContext 对象。

在页面可以访问到上面这些内置对象，举个简单例子：

```markup
<div th:text='${#session.getAttribute("name")}'></div>
```

HTML

Copy

实用内置对象：

- \#execInfo：有关正在处理的模板的信息。
- \#messages：在变量表达式中获取外部化消息的方法，与使用＃{…}语法获得的方式相同。
- \#uris：转义URL / URI部分的方法
- \#conversions：执行配置的转换服务（如果有）的方法。
- \#dates：java.util.Date对象的方法：格式化，组件提取等
- \#calendars：类似于#dates但是java.util.Calendar对象。
- \#numbers：用于格式化数字对象的方法。
- \#strings：String对象的方法：contains，startsWith，prepending / appending等
- \#objects：一般对象的方法。
- \#bools：布尔评估的方法。
- \#arrays：数组方法。
- \#lists：列表的方法。
- \#sets：集合的方法。
- \#maps：地图方法。
- \#aggregates：在数组或集合上创建聚合的方法。
- \#ids：处理可能重复的id属性的方法（例如，作为迭代的结果）。

这是一些内置对象以及工具方法，使用方式也都比较容易，如果使用的是 IntelliJ IDEA，都会自动提示对象中的方法，很方便。

举例：

```java
<div th:text="{#execInfo.getProcessedTemplateName()}"></div>
<div th:text="{#arrays.length(#request.getAttribute('names'))}"></div>
```

Java

Copy

### 3.2 设置属性值

这个是给 HTML 元素设置属性值。可以一次设置多个，多个之间用 `,` 分隔开。

例如：

```markup
<img th:attr="src=@{/1.png},title={user.username},alt={user.username}">
```

HTML

Copy

会被渲染成：

```markup
<img src="/myapp/1.png" title="javaboy" alt="javaboy">
```

HTML

Copy

当然这种设置方法不太美观，可读性也不好。Thymeleaf 还支持在每一个原生的 HTML 属性前加上 th: 前缀的方式来使用动态值，像下面这样：

```markup
<img th:src="@{/1.png}" th:alt="{user.username}" th:title="{user.username}">
```

HTML

Copy

这种写法看起来更清晰一些，渲染效果和前面一致。

上面案例中的 alt 和 title 则是两个特殊的属性，可以一次性设置，像下面这样：

```markup
<img th:src="@{/1.png}" th:alt-title="${user.username}">
```

HTML

Copy

这个等价于前文的设置。

### 3.3 遍历

数组/集合/Map/Enumeration/Iterator 等的遍历也算是一个非常常见的需求，Thymeleaf 中通过 th:each 来实现遍历，像下面这样：

```markup
<table border="1">
    <tr th:each="u : {users}">
        <td th:text="{u.username}"></td>
        <td th:text="${u.address}"></td>
    </tr>
</table>
```

HTML

Copy

users 是要遍历的集合/数组，u 则是集合中的单个元素。

遍历的时候，我们可能需要获取遍历的状态，Thymeleaf 也对此提供了支持：

- index：当前的遍历索引，从0开始。
- count：当前的遍历索引，从1开始。
- size：被遍历变量里的元素数量。
- current：每次遍历的遍历变量。
- even/odd：当前的遍历是偶数次还是奇数次。
- first：当前是否为首次遍历。
- last：当前是否为最后一次遍历。

u 后面的 state 表示遍历状态，通过遍历状态可以引用上面的属性。

```markup
<table border="1">
    <tr th:each="u,state : {users}">
        <td th:text="{u.username}"></td>
        <td th:text="{u.address}"></td>
        <td th:text="{state.index}"></td>
        <td th:text="{state.count}"></td>
        <td th:text="{state.size}"></td>
        <td th:text="{state.current}"></td>
        <td th:text="{state.even}"></td>
        <td th:text="{state.odd}"></td>
        <td th:text="{state.first}"></td>
        <td th:text="${state.last}"></td>
    </tr>
</table>
```

HTML

Copy

### 3.4 分支语句

只显示奇数次的遍历，可以使用 th:if，如下：

```markup
<table border="1">
    <tr th:each="u,state : {users}" th:if="{state.odd}">
        <td th:text="{u.username}"></td>
        <td th:text="{u.address}"></td>
        <td th:text="{state.index}"></td>
        <td th:text="{state.count}"></td>
        <td th:text="{state.size}"></td>
        <td th:text="{state.current}"></td>
        <td th:text="{state.even}"></td>
        <td th:text="{state.odd}"></td>
        <td th:text="{state.first}"></td>
        <td th:text="{state.last}"></td>
    </tr>
</table>
```

HTML

Copy

th:if 不仅仅只接受布尔值，也接受其他类型的值，例如如下值都会判定为 true：

- 如果值是布尔值，并且为 true。
- 如果值是数字，并且不为 0。
- 如果值是字符，并且不为 0。
- 如果值是字符串，并且不为 “false”， “off” 或者 “no”。
- 如果值不是布尔值，数字，字符或者字符串。

但是如果值为 null，th:if 会求值为 false。

th:unless 的判定条件则与 th:if 完全相反。

```markup
<table border="1">
    <tr th:each="u,state : {users}" th:unless="{state.odd}">
        <td th:text="{u.username}"></td>
        <td th:text="{u.address}"></td>
        <td th:text="{state.index}"></td>
        <td th:text="{state.count}"></td>
        <td th:text="{state.size}"></td>
        <td th:text="{state.current}"></td>
        <td th:text="{state.even}"></td>
        <td th:text="{state.odd}"></td>
        <td th:text="{state.first}"></td>
        <td th:text="{state.last}"></td>
    </tr>
</table>
```

HTML

Copy

这个显示效果则与上面的完全相反。

当可能性比较多的时候，也可以使用 switch：

```markup
<table border="1">
    <tr th:each="u,state : {users}">
        <td th:text="{u.username}"></td>
        <td th:text="{u.address}"></td>
        <td th:text="{state.index}"></td>
        <td th:text="{state.count}"></td>
        <td th:text="{state.size}"></td>
        <td th:text="{state.current}"></td>
        <td th:text="{state.even}"></td>
        <td th:text="{state.odd}"></td>
        <td th:text="{state.first}"></td>
        <td th:text="{state.last}"></td>
        <td th:switch="{state.odd}">
            <span th:case="true">odd</span>
            <span th:case="*">even</span>
        </td>
    </tr>
</table>
```

HTML

Copy

`th:case="*"` 则表示默认选项。

### 3.5 本地变量

这个我们前面已经涉及到了，使用 th:with 可以定义一个本地变量。

### 3.6 内联

我们可以使用属性将数据放入页面模版中，但是很多时候，内联的方式看起来更加直观一些，像下面这样：

```markup
<div>hello [[${user.username}]]</div>
```

HTML

Copy

用内联的方式去做拼接也显得更加自然。

`[[...]]` 对应于 th:text （结果会是转义的 HTML），`[(...)]`对应于 th:utext，它不会执行任何的 HTML 转义。

像下面这样：

```markup
<div th:with="str='hello <strong>javaboy</strong>'">
    <div>[[{str}]]</div>
    <div>[({str})]</div>
</div>
```

HTML

Copy

最终的显示效果如下：

![img](http://img.itboyhub.com/2020/20201215210604.png)

不过内联方式有一个问题。我们使用 Thymeleaf 的一大优势在于不用动态渲染就可以直接在浏览器中看到显示效果，当我们使用属性配置的时候确实是这样，但是如果我们使用内联的方式，各种表达式就会直接展示在静态网页中。

也可以在 js 或者 css 中使用内联，以 js 为例，使用方式如下：

```markup
<script th:inline="javascript">
    var username=[[${user.username}]]
    console.log(username)
</script>
```

HTML

Copy

js 中需要通过 `th:inline="javascript"` 开启内联。

## 4. 小结

好啦，Thymeleaf 跟大家也介绍的差不多了，应付日常的工作应该是可以了。对 Thymeleaf 感兴趣的小伙伴，也可以看看它的官方文档： [https://www.thymeleaf.org](https://www.thymeleaf.org/)。

# 9. Spring boot 缓存

Spring boot cache 缓存框架的抽象。

 @EnableCahing





