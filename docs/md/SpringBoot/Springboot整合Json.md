# Spring boot 整合 Json

序列化 对象->json

反序列化 json->对象



HttpMessageConvert 转换器



SpringMVC 框架 jackson 和 Gson 的HttpMessageConvert 已经配置好了。FastJson需要开发者手动配置。

## 1. JackJson

jackson 配置有2种思路

1. 在对象上配

1. 全局配置

实例展示

### 1.1 Web 模块引入依赖(内嵌了Jackjson)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### 1.2 创建User

```java
@JsonIgnoreProperties({"name","birthday","address"})
public class User {
    // 默认值，序列化别名  index 序列化优先级，默认-1，数据越大优先级越低
    @JsonProperty(defaultValue = "user_id",index = 99)
    private int id;

    @JsonIgnore //存在JsonProperty 该字段忽略会失效
    //@JsonProperty(index = 98)
    private String name;
    //该字段不序列化
    @JsonIgnore
    private int age;
    //时间格式化，在实体类局部设置
   // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date birthday;
    @JsonProperty("地址")
    //@JsonPropertyOrder //类似于@JsonProperty 中的index
    private String address;
}
```

### 1.3 创建UserContoroller

```java
@RestController
public class UserController {
    @GetMapping("/getUserInfo")
    public User getUserById(){
        User user = new User();
        user.setId( 1 );
        user.setName( "Tomcat" );
        user.setAge( 20);
        user.setAdrees( "北京" );
        user.setBirthday( new Date() );
        return user;
    }
}
```

启动项目测试  http://localhost:8080/getUserInfo





### 1.4 日志格式化全局配置

```java
@Configuration
public class WebConfig {
    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper om = new ObjectMapper();
        om.setDateFormat( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") );
        return om;

    }
}
```

这里就不用再加时区了。默认系统时区。

## 2 Gson

### 2.1 引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-json</artifactId>
        </exclusion>
    </exclusions>
</dependency>


<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
</dependency>
```

### 2.2 user

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String name;
    private Date birthday;
}
```

### 2.3 controller

```java
@RestController
public class UserController {
    @GetMapping("/getUserInfo")
    public User getUser(){
         return new User( 1, "tomcat", new Date() );
    }
}
```

### 2.4 配置日期格式化

* 方法一

```properties
spring.gson.date-format=yyyy-MM-dd HH:mm:ss
```

* 方法二

```java
@Configuration
public class WebConfig {
    @Bean
    public GsonBuilder gsonBuilder(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat( "yyyy-MM-dd HH:mm:ss" );
        return gsonBuilder;
    }
}
```

## 3. Fastson

有坑，Spring boot 版本和fast json 版本有坑，注意下

### 3.1 这里使用Springboot 2.4.1 和  fastjson 1.2.75



```xml
<dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
      <exclusions>
          <exclusion>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-json</artifactId>
          </exclusion>
      </exclusions>
  </dependency>

<!--需要写版本号-->
  <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>fastjson</artifactId>
      <version>1.2.75</version>
  </dependency>
```

### 3.2 配置 user

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String name;
    private Date birthday;
}
```

### 3.3 配置

```java
@RestController
public class UserController {
    @GetMapping("/getUserInfo")
    public User getUser(){
         return new User( 1, "我是中国人", new Date() );
    }
}
```

启动项目： http://localhost:8080/getUserInfo

出现异常：org.springframework.http.converter.HttpMessageNotWritableException: No converter found for return value of type: class com.example.fastjson.User

这里要手动配置 converter 

### 3.4 添加消息转换器

* 方式一

```java
@Configuration
public class WebConfig {

    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter(){
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setDateFormat( "yyyy-MM-dd HH:mm:ss" );
        converter.setFastJsonConfig( fastJsonConfig );
        converter.setDefaultCharset( StandardCharsets.UTF_8 );
        return converter;
    }
}
```

* 方式二重写 

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setDateFormat( "yyyy-MM-dd HH:mm:ss" );
        converter.setFastJsonConfig( fastJsonConfig );
        converter.setDefaultCharset( StandardCharsets.UTF_8 );
        //fastjson converter 加入converters 集合
        converters.add( converter );
    }
}
```

### 3.5 测试

启动项目：  http://localhost:8080/getUserInfo 测试结果如下

![image-20220323111451410](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220323111451410.png)

