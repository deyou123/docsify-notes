# Spring boot 使用mybatis

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
   <groupId>org.projectlombok</groupId>
   <artifactId>lombok</artifactId>
</dependency>

<!-- 引入 MyBatis 场景启动器，包含其自动配置类及 MyBatis 3 相关依赖 -->
<dependency>
   <groupId>org.mybatis.spring.boot</groupId>
   <artifactId>mybatis-spring-boot-starter</artifactId>
   <version>1.3.2</version>
</dependency>

<dependency>
   <groupId>mysql</groupId>
   <artifactId>mysql-connector-java</artifactId>
</dependency>
```



yaml

```yaml
spring.datasource.url=jdbc:mysql://localhost:3306/lou_springboot?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false&serverTimezone=UTC
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=666666

mybatis.mapper-locations=classpath:mapper/*Dao.xml

# mybatis.config-location=classpath:mybatis-config.xml
# mybatis.mapper-locations=classpath:mapper/*Dao.xml
# mybatis.type-aliases-package=com.lou.springboot.entity
```

检测是数据库是否连接成功

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

成功后

1. 创建数据库

```sql
DROP TABLE IF EXISTS `tb_user`;

CREATE TABLE `tb_user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '登录名',
  `password` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```

创建实体类

```java
@Data
public class User {

    private Integer id;
    private String name;
    private String password;


}
```

创建接口

```java
package com.lou.springboot.dao;

import com.lou.springboot.entity.User;

import java.util.List;
import java.util.Map;

/**
 * @author 13
 * MyBatis 测试
 */
public interface UserDao  {
    /**
     * 返回数据列表
     *
     * @return
     */
    List<User> findAllUsers();

    /**
     * 添加
     *
     * @param User
     * @return
     */
    int insertUser(User User);

    /**
     * 修改
     *
     * @param User
     * @return
     */
    int updUser(User User);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    int delUser(Integer id);
}
```

创建映射文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

    <mapper namespace="com.lou.springboot.dao.UserDao">
    <resultMap type="com.lou.springboot.entity.User" id="UserResult">
        <result property="id" column="id"/>
        <result property="name" column="name"/>
        <result property="password" column="password"/>
    </resultMap>

    <select id="findAllUsers" resultMap="UserResult">
        select id,name,password from stb_user
        order by id desc
    </select>


    <insert id="insertUser" parameterType="com.lou.springboot.entity.User">
        insert into tb_user(name,password)
        values(#{name},#{password})
    </insert>

    <update id="updUser" parameterType="com.lou.springboot.entity.User">
        update tb_user
        set
        name=#{name},password=#{password}
        where id=#{id}
    </update>

    <delete id="delUser" parameterType="int">
        delete from tb_user where id=#{id}
    </delete>

</mapper>
```

创建controller

```java
package com.lou.springboot.controller;

import com.lou.springboot.dao.UserDao;
import com.lou.springboot.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class MyBatisController {

    @Resource
    UserDao userDao;
    //http://localhost:8080/users/queryAll
    // 查询所有记录
    @GetMapping("/users/mybatis/queryAll")
    public List<User> queryAll() {
        return userDao.findAllUsers();
    }

    // 新增一条记录
    //http://localhost:8080/users/mybatis/insert?name=mybatis1&password=1233333
    @GetMapping("/users/mybatis/insert")
    public Boolean insert(String name, String password) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
            return false;
        }
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        return userDao.insertUser(user) > 0;
    }

    // 修改一条记录
    //http://localhost:8080/users/mybatis/update?id=3&name=mybatis2&password=1233222
    @GetMapping("/users/mybatis/update")
    public Boolean insert(Integer id, String name, String password) {
        if (id == null || id < 1 || StringUtils.isEmpty(name) || StringUtils.isEmpty(password)) {
            return false;
        }
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setPassword(password);
        return userDao.updUser(user) > 0;
    }

    // 删除一条记录
    //http://localhost:8080/users/mybatis/delete?id=4
    @GetMapping("/users/mybatis/delete")
    public Boolean insert(Integer id) {
        if (id == null || id < 1) {
            return false;
        }
        return userDao.delUser(id) > 0;
    }
}
```

