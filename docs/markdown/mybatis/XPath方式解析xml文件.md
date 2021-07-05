xml 解析有三种方式：

* DOM
* SAX
* XPath

Mybatis 采用XPath 方式解析

新建一个maven项目

引入依赖

```java
<dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.7</version>
        </dependency>
<!--        <dependency>-->
<!--            <groupId>commons-beanutils</groupId>-->
<!--            <artifactId>commons-beanutils</artifactId>-->
<!--            <version>1.9.3</version>-->
<!--        </dependency>-->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.2</version>

        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.20</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.76</version>
        </dependency>

```

在resources 文件下新建一个users.xml 文件

```java
<?xml version="1.0" encoding="UTF-8" ?>
<users>
    <user id = "1">
        <name>张三</name>
        <createTime>2021-06-06 00:00:00</createTime>
        <passward>admin</passward>
        <phone>180000000</phone>
        <nickName>阿毛</nickName>
    </user>
    <user id = "2">
        <name>李四</name>
        <createTime>2021-06-06 00:00:00</createTime>
        <passward>admin</passward>
        <phone>180000001</phone>
        <nickName>明明</nickName>
    </user>
</users>

```

JDK Xpath 解析

```java
package com.zdy.xpath;

import com.alibaba.fastjson.JSON;
import com.zdy.entity.UserEntity;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.io.Resources;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class XPathExample {

    @Test
    public void testXPathParser() {
        try {
            // 创建DocumentBuilderFactory实例
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // 创建DocumentBuilder实例
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream inputSource = Resources.getResourceAsStream("users.xml");
            Document doc = builder.parse(inputSource);
            // 获取XPath实例
            XPath xpath = XPathFactory.newInstance().newXPath();
            // 执行XPath表达式，获取节点信息
            NodeList nodeList = (NodeList)xpath.evaluate("/users/*", doc, XPathConstants.NODESET);
            List<UserEntity> userList = new ArrayList<>();
            for(int i=1; i < nodeList.getLength() + 1; i++) {
                String path = "/users/user["+i+"]";
                String id = (String)xpath.evaluate(path + "/@id", doc, XPathConstants.STRING);
                String name = (String)xpath.evaluate(path + "/name", doc, XPathConstants.STRING);
                String createTime = (String)xpath.evaluate(path + "/createTime", doc, XPathConstants.STRING);
                String passward = (String)xpath.evaluate(path + "/passward", doc, XPathConstants.STRING);
                String phone = (String)xpath.evaluate(path + "/phone", doc, XPathConstants.STRING);
                String nickName = (String)xpath.evaluate(path + "/nickName", doc, XPathConstants.STRING);
                // 调用buildUserEntity()方法，构建UserEntity对象
                UserEntity userEntity = buildUserEntity(id,name, createTime, passward, phone, nickName);
                userList.add(userEntity);
            }
            System.out.println(JSON.toJSONString(userList));
        } catch (Exception e) {
            throw new BuilderException("Error creating document instance.  Cause: " + e, e);
        }
    }

    private UserEntity buildUserEntity(String id,String name,
                                       String createTime, String passward,
                                       String phone, String nickName)
            throws IllegalAccessException, InvocationTargetException {
        UserEntity userEntity = new UserEntity();
        DateConverter dateConverter = new DateConverter(null);
        dateConverter.setPattern("yyyy-MM-dd HH:mm:ss");
        ConvertUtils.register(dateConverter,Date.class);
        BeanUtils.setProperty(userEntity,"id",id);
        BeanUtils.setProperty(userEntity,"name",name);
        BeanUtils.setProperty(userEntity,"createTime",createTime);
        BeanUtils.setProperty(userEntity,"passward",passward);
        BeanUtils.setProperty(userEntity,"phone",phone);
        BeanUtils.setProperty(userEntity,"nickName",nickName);
        return userEntity;
    }
}

```

Mybatis XPthatParser 解析工具

```java
package com.zdy.xpath;

import com.alibaba.fastjson.JSON;
import com.zdy.entity.UserEntity;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.junit.Test;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class XPathParserExample {

    @Test
    public void testXPathParser() throws Exception {
        Reader resource = Resources.getResourceAsReader("users.xml");
        XPathParser parser = new XPathParser(resource);
        // 注册日期转换器
        DateConverter dateConverter = new DateConverter(null);
        dateConverter.setPattern("yyyy-MM-dd HH:mm:ss");
        ConvertUtils.register(dateConverter, Date.class);
        List<UserEntity> userList = new ArrayList<>();
        // 调用evalNodes（）方法获取XNode列表
        List<XNode> nodes = parser.evalNodes("/users/*");
        // 对XNode对象进行遍历，获取user相关信息
        for (XNode node : nodes) {
            UserEntity userEntity = new UserEntity();
            Long id = node.getLongAttribute("id");
            BeanUtils.setProperty(userEntity, "id", id);
            List<XNode> childNods = node.getChildren();
            for (XNode childNode : childNods) {
                    BeanUtils.setProperty(userEntity, childNode.getName(),
                            childNode.getStringBody());
            }
            userList.add(userEntity);
        }
        System.out.println(JSON.toJSONString(userList));
    }
}

```

## Configuration 创建过程

