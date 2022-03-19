Spring boot 使用 Mybatis-Genrator 自动生成代码

引入依赖和相关插件

```xml
<dependencies>
   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
   </dependency>

   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-jdbc</artifactId>
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

   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
   </dependency>
</dependencies>

<build>
   <plugins>
      <plugin>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
      <plugin>
         <groupId>org.mybatis.generator</groupId>
         <artifactId>mybatis-generator-maven-plugin</artifactId>
         <version>1.3.5</version>
         <dependencies>
            <dependency>
               <groupId> mysql</groupId>
               <artifactId>mysql-connector-java</artifactId>
               <version> 5.1.39</version>
            </dependency>
            <dependency>
               <groupId>org.mybatis.generator</groupId>
               <artifactId>mybatis-generator-core</artifactId>
               <version>1.3.5</version>
            </dependency>
         </dependencies>
         <executions>
            <execution>
               <id>Generate MyBatis Artifacts</id>
               <phase>package</phase>
               <goals>
                  <goal>generate</goal>
               </goals>
            </execution>
         </executions>
         <configuration>
            <verbose>true</verbose>
            <!-- 是否覆盖 -->
            <overwrite>true</overwrite>
            <!-- MybatisGenerator的配置文件位置 -->
            <configurationFile>src/main/resources/mybatisGeneratorConfig.xml</configurationFile>
         </configuration>
      </plugin>
   </plugins>
</build>
```

创建数据库表

新增 MyBatis-Generator 的配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <context id="my-blog-generator-config" targetRuntime="MyBatis3">
        <!-- 生成的Java文件的编码 -->
        <property name="javaFileEncoding" value="utf-8"/>
        <!-- 格式化java代码 -->
        <property name="javaFormatter" value="org.mybatis.generator.api.dom.DefaultJavaFormatter"/>
        <!-- 格式化XML代码 -->
        <property name="xmlFormatter" value="org.mybatis.generator.api.dom.DefaultXmlFormatter"/>
        <plugin type="org.mybatis.generator.plugins.ToStringPlugin"/>
        <!--创建Java类时对注释进行控制-->
        <commentGenerator>
            <property name="suppressDate" value="true"/>
            <!-- 是否去除自动生成的注释 true：是 ： false:否 -->
            <property name="suppressAllComments" value="true"/>
        </commentGenerator>
        <!--数据库地址及登陆账号密码 改成你自己的配置-->
        <jdbcConnection
                driverClass="com.mysql.jdbc.Driver"
                connectionURL="jdbc:mysql://localhost:3306/lou_springboot"
                userId="root"
                password="">
        </jdbcConnection>
        <javaTypeResolver>
            <property name="forceBigDecimals" value="false"/>
        </javaTypeResolver>
        <!--生成实体类设置-->
        <javaModelGenerator targetPackage="com.lou.springboot.entity" targetProject="src/main/java">
            <property name="enableSubPackages" value="true"/>
            <property name="trimStrings" value="true"/>
        </javaModelGenerator>
        <!--生成Mapper文件设置-->
        <sqlMapGenerator targetPackage="mapper" targetProject="src/main/resources">
            <property name="enableSubPackages" value="true"/>
        </sqlMapGenerator>
        <!--生成Dao类设置-->
        <javaClientGenerator type="XMLMAPPER" targetPackage="com.lou.springboot.dao"
                             targetProject="src/main/java">
            <property name="enableSubPackages" value="true"/>
        </javaClientGenerator>
        <!--需要自动生成代码的表及对应的类名设置-->
        <table tableName="generator_test" domainObjectName="GeneratorTest"
               enableCountByExample="false"
               enableUpdateByExample="false"
               enableDeleteByExample="false" enableSelectByExample="false" selectByExampleQueryId="false">
        </table>
    </context>
</generatorConfiguration>
```

