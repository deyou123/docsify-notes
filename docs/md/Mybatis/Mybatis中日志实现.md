## Java 日志体系

* Log4j
* Log4j2
* Commos-loggging
* logback
* JUL

java 日志门面

* JCL
* SLF4j

##  Mybatis 日志实现

mybatis 中的日志接口

```java
public interface Log {

  boolean isDebugEnabled();

  boolean isTraceEnabled();

  void error(String s, Throwable e);

  void error(String s);

  void debug(String s);

  void trace(String s);

  void warn(String s);

}
```

针对日志接口，mybatis 提供了7中不同的日志实现。

* Apache Commons Loggging : JCL 日志输出
* Log4j2 使用Log4j2 框架输入日志
* Java Util Logging 使用JDK 内置模块输出日志
* No Loggging 不输出任何日志
* SLFJ : 使用SLF4J 输出日志
* Stdut: 将日志输出到标准设备（例如控制台）

## mybatis log 实例采用工厂模式创建。

* 可以在配置文件中指定日志框架

```xml
  <setting name="logImpl" value="STDOUT_LOGGING" />
```

可以指定的值有： **SLF4J|COMMONS_LOGGING|LOG4J|LOG4j2|JDK_LOGGING|STDOUT_LOGGING|NO_LOGGING**

* 未指定日志框架时：

Mybatis 能自动从classpath 中发现日志框架

**SLF4j-->JCL -->Log4J2-->log4J-->JUL-->No Logging**

