## Spring boot 整合JDBC

引入依赖

```java
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

创建数据库

```sql
use db_01;
create table user
(
    id       int auto_increment
        primary key,
    username varchar(255) null,
    address  varchar(255) null
);

INSERT INTO db_01.user (id, username, address) VALUES (1, 'javaboy', 'www.javaboy.org');
INSERT INTO db_01.user (id, username, address) VALUES (2, 'itboyhub', 'www.itboyhub.com');
```

配置数据库

```java
spring.datasource.one.jdbcUrl=jdbc:mysql:///db_01?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
spring.datasource.one.username=root
spring.datasource.one.password=666666
```

创建User

```user
@Data
public class User {
    private Long id;
    private String username;
    private String address;
}
```

创建UserService

```java
@Service
public class UserService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public int addUser(User user) {
        int result = jdbcTemplate.update("insert into user (username,address) values(?,?)", user.getUsername(), user.getAddress());
        return result;
    }

    public int addUser2(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int result = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement("insert into user (username,address) values(?,?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getAddress());
                return ps;
            }
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return result;
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("delete from user where id=?", id);
    }

    public int updateById(Long id,String username) {
        return jdbcTemplate.update("update user set username=? where id=?", username, id);
    }

    public List<User> getAllUsers() {
        List<User> list = jdbcTemplate.query("select * from user", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                String username = resultSet.getString("username");
                String address = resultSet.getString("address");
                long id = resultSet.getLong("id");
                User user = new User();
                user.setId(id);
                user.setUsername(username);
                user.setAddress(address);
                return user;
            }
        });
        return list;
    }
    public List<User> getAllUsers2() {
        List<User> list = jdbcTemplate.query("select * from user", new BeanPropertyRowMapper<>(User.class));
        return list;
    }
```



测试

```java
@SpringBootTest
class JdbctemplateApplicationTests {

    @Autowired
    UserService userService;
    @Test

    void contextLoads() {
        User user = new User();
        user.setUsername("javaboy");
        user.setAddress("www.javaboy.org");
        int i = userService.addUser(user);
        System.out.println(i);
    }

    @Test
    void test1() {
        User u = new User();
        u.setUsername("itboyhub");
        u.setAddress("www.itboyhub.com");
        int i = userService.addUser2(u);
        System.out.println("i = " + i);
        System.out.println("u.getId() = " + u.getId());
    }

    @Test
    void test2() {
        userService.deleteById(10L);
        userService.updateById(9L, "itboyhub123");
    }
    @Test
    void test3() {
        List<User> allUsers = userService.getAllUsers();
        for (User allUser : allUsers) {
            System.out.println(allUser);
        }
    }

    @Test
    void test4() {
        List<User> allUsers = userService.getAllUsers2();
        for (User allUser : allUsers) {
            System.out.println(allUser);
        }
    }
}
```

## Springboot 使用jdbc 配置多数据源

配置文件

```properties
spring.datasource.one.jdbcUrl=jdbc:mysql:///db_01?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
spring.datasource.one.username=root
spring.datasource.one.password=666666

spring.datasource.two.jdbcUrl=jdbc:mysql:///db_02?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
spring.datasource.two.username=root
spring.datasource.two.password=666666
```

DataSource 配置

```java
@Configuration
public class DataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.one")
    DataSource dsOne() {
        return new HikariDataSource();
    }
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.two")
    DataSource dsTwo() {
        return new HikariDataSource();
    }
}
```

Jdbc 模板配置

```java
@Configuration
public class JdbcTemplateConfig {

    @Bean
    JdbcTemplate jdbcTemplateOne(@Qualifier("dsOne") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @Bean
    JdbcTemplate jdbcTemplateTwo(@Qualifier("dsTwo") DataSource ds) {
        return new JdbcTemplate(ds);
    }
}
```



创建db_01，db_02，添加User表

```sql
use db_01;
create table user
(
    id       int auto_increment
        primary key,
    username varchar(255) null,
    address  varchar(255) null
);

INSERT INTO db_01.user (id, username, address) VALUES (1, 'javaboy', 'www.javaboy.org');
INSERT INTO db_01.user (id, username, address) VALUES (2, 'itboyhub', 'www.itboyhub.com');
```

```sql
use db_02;
create table user
(
    id       int auto_increment
        primary key,
    username varchar(255) null,
    address  varchar(255) null
);

INSERT INTO db_01.user (id, username, address) VALUES (1, 'javaboy', 'www.javaboy.org');
INSERT INTO db_01.user (id, username, address) VALUES (2, 'itboyhub', 'www.itboyhub.com');
```

创建User

```java
@Data
public class User {
    private Long id;
    private String username;
    private String address;
}
```

测试：

```java
@SpringBootTest
class JdbctemplatemultiApplicationTests {

    @Autowired
    @Qualifier("jdbcTemplateOne")
    JdbcTemplate jdbcTemplateOne;

    @Resource(name = "jdbcTemplateTwo")
    JdbcTemplate jdbcTemplateTwo;
    @Test
    void contextLoads() {
        List<User> list1 = jdbcTemplateOne.query("select * from user", new BeanPropertyRowMapper<>(User.class));
        List<User> list2 = jdbcTemplateTwo.query("select * from user", new BeanPropertyRowMapper<>(User.class));
        System.out.println("list1 = " + list1);
        System.out.println("list2 = " + list2);
    }

}
```