## 1. SQL 类



​	Java 中 根据条件动态拼接SQl 语句

源码

```java
package org.apache.ibatis.jdbc;
public class SQL extends AbstractSQL<SQL> {

  @Override
  public SQL getSelf() {
    return this;
  }

}
```

## 2. ScriptRunner类 执行SQl  脚本文件

```java
public class ScriptRunner {

 //sql 异常是否中断程序
  private boolean stopOnError;
    
  private boolean throwWarning;
    //自动提交
  private boolean autoCommit;
    //批量执行
  private boolean sendFullScript;
  private boolean removeCRs;
  private boolean escapeProcessing = true;

  private PrintWriter logWriter = new PrintWriter(System.out);
  private PrintWriter errorLogWriter = new PrintWriter(System.err);

  private String delimiter = DEFAULT_DELIMITER;
  private boolean fullLineDelimiter;
    
  public void runScript(Reader reader) {
      //根据属性值是否自动提交
    setAutoCommit();

    try {
        //是否批处理
      if (sendFullScript) {
         // 一次性读取脚本所有内容,然后调用JDBC Statement 对象的execute() 方法一次性执行所有脚本语句。
        executeFullScript(reader);
      } else {
          //一行一行读取SQL 脚本文件，以分好为一条SQL结束标志，执行SQL 语句。
        executeLineByLine(reader);
      }
    } finally {
      rollbackConnection();
    }
  }
}

```

```java
 private void executeLineByLine(Reader reader) {
    StringBuilder command = new StringBuilder();
    try {
      BufferedReader lineReader = new BufferedReader(reader);
      String line;
      while ((line = lineReader.readLine()) != null) {
        //调用handleLine 方法处理每行没弄
        handleLine(command, line);
      }
      commitConnection();
      checkForMissingLineTerminator(command);
    } catch (Exception e) {
      String message = "Error executing: " + command + ".  Cause: " + e;
      printlnError(message);
      throw new RuntimeSqlException(message, e);
    }
  }

```

```java
  private void handleLine(StringBuilder command, String line) throws SQLException {
    String trimmedLine = line.trim();
    if (lineIsComment(trimmedLine)) {//1. 判断该行是否为SQl 注释
      Matcher matcher = DELIMITER_PATTERN.matcher(trimmedLine);
      if (matcher.find()) {
        delimiter = matcher.group(5);
      }
      println(trimmedLine);
    } else if (commandReadyToExecute(trimmedLine)) {//判断该行是否包含分号
      command.append(line.substring(0, line.lastIndexOf(delimiter)));
      command.append(LINE_SEPARATOR);
      println(command);
      executeStatement(command.toString());//执行该条完整的Sql语句
      command.setLength(0);
    } else if (trimmedLine.length() > 0) {//4.该行不包含分号，说明这条语句未结束，追加本行内容到之前的读取的内容中去。
      command.append(line);
      command.append(LINE_SEPARATOR);
    }
  }
```

## 3. SqlRunner 类操作数据库

```java
SqlRunner sqlRunner = new SqlRunner(connection);
        String qryUserSql = new SQL() {{
            SELECT("*");
            FROM("user");
            WHERE("id = ?");
        }}.toString();
        Map<String, Object> resultMap = sqlRunner.selectOne(qryUserSql, Integer.valueOf(1));
        System.out.println(JSON.toJSONString(resultMap));
```

## 4.MetaObject 类

Mybatis 中的反射工具类，可以优雅的获取和设置对象属性。

## 5. MetaClass 类

获取类的相关信息

## 6. ObjectFactory

Mybatis 对象工厂，每次创建Mapper 映射结果对象的新实例时，都会使用一个对象工厂（ObjectFactory)实例来完成。

## 7. ProxyFactory 

代理工厂，创建动态的代理对象

CglibProxyFactory

JavaProxyFactory
