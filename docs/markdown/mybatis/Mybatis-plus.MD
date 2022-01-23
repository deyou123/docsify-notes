## Mybatis-plus 主键生成策略



* AUTO: 该类型请确保数据库设置了 ID自增 否则无效

* NONE: 该类型为未设置主键类型(注解里等于跟随全局,全局里约等于 INPUT)

* INPUT:该类型可以通过自己注册自动填充插件进行填充

以下2种类型、只有当插入对象ID 为空，才自动填充。

* ASSIGN_ID:  分配ID (主键类型为number或string）**注意修改实体类和数据库数据类型**

默认实现类 {@link com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator}(雪花算法)

*  ASSIGN_UUID: 分配UUID (主键类型为 string) **注意修改实体类和数据库数据类型。**

默认实现类 {@link com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator}(UUID.replace("-",""))

查看IdType源码

```java
public enum IdType {
    
    AUTO(0),
   
    NONE(1),
    
    INPUT(2),

    ASSIGN_ID(3),
   
    ASSIGN_UUID(4),
    
    private final int key;

    IdType(int key) {
        this.key = key;
    }
}
```

配置方式：

```java
@TableId(type = IdType.ASSIGN_ID)
private Long id;
```
测试代码

```java
@Test
public void insert(){
    User user = new User();
    user.setName("张三");
    user.setAge(20);
    user.setEmail("test@qq.com");
    int insert = userMapper.insert(user);
    System.out.println("======插入"+insert+"条数据");

}
```

查看结果

>  User(id=1481173580543700993, name=张三, age=20, email=test@qq.com)

