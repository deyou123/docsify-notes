# Mysql字符集

Mysql 5.7 为例

查看字符集

SHOW CHARACTER SET;

select * from information_schema.CHARACTER_SETS;

SHOW COLLATION WHERE Charset='utf8';

查看系统字符集

select @@global.character_set_system;



# Mysql 用户管理

## Win10 Mysql 8.0 忘记root 用户密码

1. （以管理员身份）打开命令窗口cmd，输入命令：`net stop mysql`，停止MySQL服务，

2. 开启跳过密码验证登录的MySQL服务

```shell
  输入命令  
  mysqld --console --skip-grant-tables --shared-memory 
```

3. 再打开一个新的cmd(以管理员身份)，无密码登录MySQL，输入登录命令：`mysql -u root -p`

 

4. 密码置为空，命令如下：
  ```sql
  use mysql;
  update user set authentication_string='' where user='root';
  ```

5. 退出mysql，执行命令：`qiut`

6. 关闭以`-console --skip-grant-tables --shared-memory` 启动的MySQL服务。

7. ***\*（以管理员身份）\****打开命令框，输入：`net start mysql` 启动MySQL服务。

8. 步骤4密码已经置空，所以无密码状态登录MySQL，输入登录命令：

   ```sql
   mysql -u root -p
   ```

9.  修改密码

```sql
ALTER USER 'root'@'localhost' IDENTIFIED BY '新密码';
```

10. 验证更改后密码正确登录

输入`quit`，退出当前登录，输入登录命令：**mysql -u root -p**

输入密码，成功登录，到此，重置密码结束。

Liunx 修改用户登录密码

