# Spring boot 整合Mybatis 

引入依赖

```java
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.1.4</version>
</dependency>

<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```

数据库

```sql
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

User 

```java
@Data
public class User {
    private Long id;
    private String username;
    private String address;

}
```

UserMapper

```java
public interface UserMapper {
    @Select("select * from user where id=#{id}")
    User getUserById(Long id);

    @Results({
            @Result(property = "address",column = "address1")
    })
    @Select("select * from user")
    List<User> getAllUsers();

    @Insert("insert into user (username,address1) values (#{username},#{address})")
    @SelectKey(statement = "select last_insert_id()",keyProperty = "id",before = false,resultType = Long.class)
    Integer addUser(User user);

    @Delete("delete from user where id=#{id}")
    Integer deleteById(Long id);

    @Update("update user set username=#{username} where id=#{id}")
    Integer updateById(String username, Long id);
}
```

测试

```java
@Autowired
UserMapper userMapper;

@Test
void contextLoads() {
    User user = userMapper.getUserById(9L);
    System.out.println(user);
}

@Test
void tes1() {
    List<User> users = userMapper.getAllUsers();
    System.out.println(users);
}

@Test
void test2() {
    User user = new User();
    user.setUsername("zhangsan");
    user.setAddress("shenzhen");
    userMapper.addUser(user);
    System.out.println("user.getId() = " + user.getId());
}

@Test
void test3() {
    userMapper.deleteById(12L);
    userMapper.updateById("123", 11L);
}
```

Xml 方式

```java
public interface UserMapper2 {
    User getUserById(Long id);
    List<User> getAllUsers();
    Integer addUser(User user);
    Integer deleteById(Long id);
    Integer updateById(String username, Long id);
}
```

```xml
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.javaboy.mybatis.mapper.UserMapper2">

    <resultMap id="UserMap" type="org.javaboy.mybatis.model.User">
        <id property="id" column="id"/>
        <result property="username" column="username"/>
        <result property="address" column="address1"/>
    </resultMap>

    <select id="getUserById" resultMap="UserMap">
        select * from user where id=#{id};
    </select>

    <select id="getAllUsers" resultMap="UserMap">
        select * from user ;
    </select>

    <insert id="addUser" parameterType="org.javaboy.mybatis.model.User" useGeneratedKeys="true" keyProperty="id">
        insert into user (username,address1) values (#{username},#{address});
    </insert>

    <delete id="deleteById">
        delete from user where id=#{id}
    </delete>
    <update id="updateById">
        update user set username = #{username} where id=#{id};
    </update>
</mapper>
```

测试

```java
@Autowired
UserMapper2 userMapper2;
@Test
void test4() {
    User user = userMapper2.getUserById(9L);
    System.out.println("user = " + user);
    List<User> allUsers = userMapper2.getAllUsers();
    System.out.println("allUsers = " + allUsers);
    User u = new User();
    u.setUsername("lisi");
    u.setAddress("guangzhou");
    userMapper2.addUser(u);
    System.out.println(u.getId());
    userMapper2.deleteById(9L);
    userMapper2.updateById("zhangsan", 4L);
}
```



# Spring boot 整合 mybatis 配置多数据源

项目结构如图

![image-20220328155852179](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220328155852179.png)

application.properties

```
spring.datasource.one.jdbcUrl=jdbc:mysql:///db_01?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
spring.datasource.one.username=root
spring.datasource.one.password=666666

spring.datasource.two.jdbcUrl=jdbc:mysql:///db_02?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
spring.datasource.two.username=root
spring.datasource.two.password=666666
```

User

```java
@Data
public class User {
    private Long id;
    private String username;
    private String address;
}
```

UserMapper1

```java
public interface UserMapper1 {
    List<User> getAllUsers();
}
```

UserMapper1.xml

```xml
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.javaboy.mybatismulti.mapper1.UserMapper1">

    <select id="getAllUsers" resultType="org.javaboy.mybatismulti.model.User">
        select * from user;
    </select>

</mapper>
```

UserMapper2

```
public interface UserMapper2 {
    List<User> getAllUsers();
}
```

UserMapper2.xml

```xml
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.javaboy.mybatismulti.mapper2.UserMapper2">

    <select id="getAllUsers" resultType="org.javaboy.mybatismulti.model.User">
        select * from user;
    </select>

</mapper>
```

数据源配置

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

配置数据库1

```java
@Configuration
@MapperScan(basePackages = "org.javaboy.mybatismulti.mapper1",
        sqlSessionFactoryRef = "sqlSessionFactory1",
        sqlSessionTemplateRef = "sqlSessionTemplate1")
public class MyBatisConfigOne {
    @Autowired
    @Qualifier("dsOne")
    DataSource ds;

    @Bean
    SqlSessionFactory sqlSessionFactory1() {
        SqlSessionFactory sqlSessionFactory = null;
        try {
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(ds);
            sqlSessionFactory = bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlSessionFactory;
    }

    @Bean
    SqlSessionTemplate sqlSessionTemplate1() {
        return new SqlSessionTemplate(sqlSessionFactory1());
    }
}
```

配置数据2

```java
@Configuration
@MapperScan(basePackages = "org.javaboy.mybatismulti.mapper2",sqlSessionFactoryRef = "sqlSessionFactory2",sqlSessionTemplateRef = "sqlSessionTemplate2")
public class MyBatisConfigTwo {
    @Autowired
    @Qualifier("dsTwo")
    DataSource ds;

    @Bean
    SqlSessionFactory sqlSessionFactory2() {
        SqlSessionFactory sqlSessionFactory = null;
        try {
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(ds);
            sqlSessionFactory = bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlSessionFactory;
    }

    @Bean
    SqlSessionTemplate sqlSessionTemplate2() {
        return new SqlSessionTemplate(sqlSessionFactory2());
    }
}
```

测试

```java
@SpringBootTest
class MybatismultiApplicationTests {

    @Autowired
    UserMapper1 userMapper1;
    @Autowired
    UserMapper2 userMapper2;
    @Test
    void contextLoads() {
        System.out.println("userMapper1.getAllUsers() = " + userMapper1.getAllUsers());
        System.out.println("userMapper2.getAllUsers() = " + userMapper2.getAllUsers());
    }

}
```