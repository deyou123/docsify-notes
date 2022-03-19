# Springboot 使用JDBC

引入依赖

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>

<dependency>
   <groupId>mysql</groupId>
   <artifactId>mysql-connector-java</artifactId>
</dependency>
```

配置数据源

```properties
# datasource config
spring.datasource.url=jdbc:mysql://localhost:3306/lou_springboot?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false&serverTimezone=UTC
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=666666
```

yaml 

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: 666666
    url: jdbc:mysql://localhost:3306/lou_springboot?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false&serverTimezone=UTC
```

测试数据库连接成功代码

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {
    // 注入数据源对象
    @Autowired
    private DataSource dataSource;

    @Test
    public void datasourceTest() throws SQLException {
        // 获取数据源类型
        System.out.println("默认数据源为：" + dataSource.getClass());
        // 获取数据库连接对象
        Connection connection = dataSource.getConnection();
        // 判断连接对象是否为空
        System.out.println(connection != null);
        connection.close();
    }
}
```

测试项目

```java
@Controller
public class HelloController {

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello,JDBC";
    }
}
```

创建JDBC 控制器

```java
@RestController
public class JdbcController {

    //自动配置，因此可以直接通过 @Autowired 注入进来
    @Autowired
    JdbcTemplate jdbcTemplate;
    //http://localhost:8080/users/queryAll
    // 查询所有记录
    @GetMapping("/users/queryAll")
    public List<Map<String, Object>> queryAll() {
        String sql = "select * from `tb_user`";
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from lou_springboot.tb_user");
        return list;
    }

    // 新增一条记录
    //http://localhost:8080/users/insert?name=shiyanlou1&password=syl123
    @GetMapping("/users/insert")
    public Object insert(String name, String password) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
            return false;
        }
        String sql;
        sql = "insert into lou_springboot.tb_user(`name`,`password`) " +
                "values (\"" + name + "\",\"" + password + "\")";
        jdbcTemplate.execute(sql);
        return true;
    }
}
```

启动项目 访问地址

http://localhost:8080/users/insert?name=shiyanlou1&password=syl123

http://localhost:8080/users/queryAll