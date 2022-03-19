# Tymeleaf 学习笔记

# Spring boot 整合 Thymeleaf 

1. 创建springboot 项目

引入依赖

```xml
<dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
<dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

关闭缓存

```yml
spring:
  thymeleaf:
    cache: false
```

thymeleaf是一个模板引擎，缓存的意思是加载一次模板之后便不会在加载了，对于生产环境应该加上缓存，但是在开发过程中如果打开缓存，不方便开发人员调试。试想一下，改一行html，就需要重启服务器，肯定是不方便的。总结一下：本地开发环境下，需要把缓存关闭，否则调试成本太大。其他环境下缓存都需要打开。

2. 建一个测试controller

```java
@Controller
public class ThymeleafController {
    @GetMapping("/thymeleaf")
    public String hello(HttpServletRequest request, @RequestParam(value = "desc",required = false,defaultValue = "Springboot Thymeleaf!") String desc){
        request.setAttribute("desc",desc);
        return "thymeleaf";
    }
}
```

建一个html5页面，在templates 下thymeleaf.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h>desc字段为:</h>
 <h th:text="${desc}">这里显示的是 description 字段内容</h>
</body>
</html>
```

注意要引入 xmlns:th="http://www.thymeleaf.org" 。

启动项目,打开连接进行测试。

http://localhost:8080/thymeleaf

http://localhost:8080/thymeleaf?description=Springboottest

# Thymeleaf 学习

官方网址：https://www.thymeleaf.org/index.html

- **表达式语法**

  - 变量表达式： `${...}`
  - 选择变量表达式： `*{...}`
  - 信息表达式： `#{...}`
  - 链接 URL 表达式： `@{...}`
  - 分段表达式： `~{...}`

- **字面量**

  - 字符串： 'one text', 'Another one!' ...
  - 数字： `0`, `34`, `3.0`, `12.3` ...
  - 布尔值： `true`, `false`
  - Null 值： `null`
  - 字面量标记：`one`, `sometext`, `main` ...

- **文本运算**

  - 字符串拼接： `+`
  - 字面量置换: `|The name is ${name}|`

- **算术运算**

  - 二元运算符： `+`, `-`, `*`, `/`, `%`
  - 负号（一元运算符）： (unary operator): `-`

- **布尔运算**

  - 二元运算符： `and`, `or`
  - 布尔非（一元运算符）： `!`, `not`

- **比较运算**

  - 比较： `>`, `<`, `>=`, `<=` (`gt`, `lt`, `ge`, `le`)
  - 相等运算符： `==`, `!=` (`eq`, `ne`)

  比较运算符也可以使用转义字符，比如大于号，可以使用 Thymeleaf 语法 `gt` 也可以使用转义字符`>`

- **条件运算符**

  - If-then: `(if) ? (then)`
  - If-then-else: `(if) ? (then) : (else)`
  - Default: `(value) ?: (defaultvalue)`

- **特殊语法**

  - 无操作： `_`

常用的

```java
 @GetMapping("/attributes")
    public String attribute(ModelMap map){
        map.put("th_background","#DD00FF");
        map.put("title","Thymeleaf参数测试页");
        // 更改 id、name、value
        map.put("th_id", "thymeleaf-input");
        map.put("th_name", "thymeleaf-input");
        map.put("th_value", "13");
        // 更改 class、href
        map.put("th_class", "thymeleaf-class");
        map.put("th_href", "http://13blog.site");
        return "attributes";
    }
    @GetMapping("/simple")
    public String simple(ModelMap map) {
        map.put("thymeleafText", "shiyanlou");
        map.put("number1", 2019);
        map.put("number2", 3);
        return "simple";
    }

    @GetMapping("/test")
    public String test(ModelMap map) {
        map.put("title", "Thymeleaf 语法测试");
        map.put("testString", "玩转 Spring Boot");
        map.put("bool", true);
        map.put("testArray", new Integer[]{2018,2019,2020,2021});
        map.put("testList", Arrays.asList("Spring","Spring Boot","Thymeleaf","MyBatis","Java"));
        Map testMap = new HashMap();
        testMap.put("platform", "shiyanlou");
        testMap.put("title", "玩转 Spring Boot");
        testMap.put("author", "十三");
        map.put("testMap", testMap);
        map.put("testDate", new Date());
        return "test";
    }
```
attributes.html
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Thymeleaf setting-value-to-specific-attributes</title>
    <meta charset="UTF-8">
</head>
<!-- background 标签-->
<body th:background="${th_background}" background="#FFF">
<!-- text 标签-->
<h1 th:text="${title}">html标签演示</h1>
<div>
    <h5>id、name、value标签:</h5>
    <!-- id、name、value标签-->
    <input id="input1" name="input1" value="1" th:id="${th_id}" th:name="${th_name}" th:value="${th_value}"/>
</div>
<br/>
<div class="div1" th:class="${th_class}">
    <h5>class、href标签:</h5>
    <!-- class、href标签-->
    <a th:href="${th_href}" href="https://gitbook.cn/">链接地址</a>
</div>
</body>
</html>
```

simple.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Thymeleaf simple syntax</title>
    <meta charset="UTF-8">
</head>
<body>
<h1>Thymeleaf简单语法</h1>
<div>
    <h5>基本类型操作(字符串):</h5>
    <p>一个简单的字符串：<span th:text="'thymeleaf text'">default text</span>.</p>
    <p>字符串连接：<span th:text="'thymeleaf text concat,'+${thymeleafText}">default text</span>.</p>
    <p>字符串连接：<span th:text="|thymeleaf text concat,${thymeleafText}|">default text</span>.</p>
</div>
<div>
    <h5>基本类型操作(数字):</h5>
    <p>一个简单的神奇的数字：<span th:text="2019">1000</span>.</p>
    <p>算术运算： 2019+1=<span th:text="${number1}+1">0</span>.</p>
    <p>算术运算： 14-1=<span th:text="14-1">0</span>.</p>
    <p>算术运算： 673 * 3=<span th:text="673*${number2}">0</span>.</p>
    <p>算术运算： 39 ÷ 3=<span th:text="39/3">0</span>.</p>
</div>
<div>
    <h5>基本类型操作(布尔值):</h5>
    <p>一个简单的数字比较：2019 > 2018=<span th:text="2019&gt;2018"> </span>.</p>
    <p>字符串比较：thymeleafText == 'shiyanlou'，结果为<span th:text="${thymeleafText} == 'shiyanlou'">0</span>.</p>
    <p>数字比较：13 == 39/3 结果为： <span th:text="13 == 39/3">0</span>.</p>
</div>
</body>
</html>
```

test.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title th:text="${title}">语法测试</title>
</head>
<body>
<h3>#strings 工具类测试 </h3>
<div th:if="${not #strings.isEmpty(testString)}" >
    <p>testString初始值 : <span th:text="${testString}"/></p>
    <p>toUpperCase : <span th:text="${#strings.toUpperCase(testString)}"/></p>
    <p>toLowerCase : <span th:text="${#strings.toLowerCase(testString)}"/></p>
    <p>equalsIgnoreCase : <span th:text="${#strings.equalsIgnoreCase(testString, '13')}"/></p>
    <p>indexOf : <span th:text="${#strings.indexOf(testString, 'r')}"/></p>
    <p>substring : <span th:text="${#strings.substring(testString, 5, 9)}"/></p>
    <p>startsWith : <span th:text="${#strings.startsWith(testString, 'Spring')}"/></p>
    <p>contains : <span th:text="${#strings.contains(testString, 'Boot')}"/></p>
</div>
<h3>#bools 工具类测试</h3>
<!-- 如果 bool 的值为false的话，该div将不会显示-->
<div th:if="${#bools.isTrue(bool)}">
    <p th:text="${bool}"></p>
</div>
<h3>#arrays 工具类测试</h3>
<div th:if="${not #arrays.isEmpty(testArray)}">
    <p>length : <span th:text="${#arrays.length(testArray)}"/></p>
    <p>contains : <span th:text="${#arrays.contains(testArray, 5)}"/></p>
    <p>containsAll : <span th:text="${#arrays.containsAll(testArray, testArray)}"/></p>
    <p>循环读取 : <span th:each="i:${testArray}" th:text="${i+' '}"/></p>
</div>
<h3>#lists 工具类测试</h3>
<div th:unless="${#lists.isEmpty(testList)}">
    <p>size : <span th:text="${#lists.size(testList)}"/></p>
    <p>contains : <span th:text="${#lists.contains(testList, 0)}"/></p>
    <p>sort : <span th:text="${#lists.sort(testList)}"/></p>
    <p>循环读取 : <span th:each="i:${testList}" th:text="${i+' '}"/></p>
</div>
<h3>#maps 工具类测试</h3>
<div th:if="${not #maps.isEmpty(testMap)}">
    <p>size : <span th:text="${#maps.size(testMap)}"/></p>
    <p>containsKey : <span th:text="${#maps.containsKey(testMap, 'platform')}"/></p>
    <p>containsValue : <span th:text="${#maps.containsValue(testMap, '13')}"/></p>
    <p>读取map中键为title的值 : <span th:if="${#maps.containsKey(testMap,'title')}" th:text="${testMap.get('title')}"/></p>
</div>
<h3>#dates 工具类测试</h3>
<div>
    <p>year : <span th:text="${#dates.year(testDate)}"/></p>
    <p>month : <span th:text="${#dates.month(testDate)}"/></p>
    <p>day : <span th:text="${#dates.day(testDate)}"/></p>
    <p>hour : <span th:text="${#dates.hour(testDate)}"/></p>
    <p>minute : <span th:text="${#dates.minute(testDate)}"/></p>
    <p>second : <span th:text="${#dates.second(testDate)}"/></p>
    <p>格式化: <span th:text="${#dates.format(testDate)}"/></p>
    <p>yyyy-MM-dd HH:mm:ss 格式化: <span th:text="${#dates.format(testDate, 'yyyy-MM-dd HH:mm:ss')}"/></p>
</div>
</body>
</html>
```

# 