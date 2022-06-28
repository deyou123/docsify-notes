# ORACLE 笔记

### 

# 学习地址

https://blog.csdn.net/langfeiyes/article/details/123597992

## ORACLE 安装

安装帖子：

https://blog.csdn.net/qq_35048277/article/details/120304061

下载
1.百度网盘链接，里面包含了oracle19c，以及SQL Developer：链接：https://pan.baidu.com/s/1DNPXvorqiCT7PgI-oz8_EA 提取码：lu66

密码123456

地址：https://localhost:5500/em

Wind10 安装oracle19c

监听配置文件目录：

D:\app\Oracle\WINDOWS.X64_193000_db_home\network\admin

### Docker 安装 oracle19c 

```shell
docker run -d  -p 1524:1521 -p 5502:5500  -e ORACLE_SID=ORCLCDB  -e ORACLE_PDB=ORCLPDB1  -e ORACLE_PWD=123456 -e ORACLE_EDITION=standard -e ORACLE_CHARACTERSET=AL32UTF8 -v /mydata/oracle/oradata:/opt/oracle/oradata --name orcl19c_03 registry.cn-hangzhou.aliyuncs.com/zhuyijun/oracle:19c
```



## Oracle 卸载

卸载注册表

计算机\HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services

计算机\HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Application

计算机\HKEY_LOCAL_MACHINE\SOFTWARE\ORACLE

计算机\HKEY_LOCAL_MACHINE\SYSTEM\ControlSet001\services

删除文件

C:\Program Files

C:\Users\lenovo

C:\ProgramData

C:\ProgramData\Microsoft\Windows\Start Menu\Programs\Oracle - OraDB19Home1

## Ooracle 忘记密码：

1. 问题:忘记oracle的sys和system的密码
2. 解决:
2.1. 找到oracle的安装目录: 找到此路径;       D:\tools\WINDOWS.X64_193000_db_home\bin,通过sqlplus.exe执行操作命令.(如果提示sqlplus /nolog不是内部命令, 可以先用此方法.然后在配置环境变量);

问题 : sqlplus不是内部命令.  ①确保oracle安装成功--> ②环境变量的配置(问题所在)     

解决:找到此路径oracle的安装目录:  D:\app\Administrator\product\11.2.0\dbhome_1\BIN ,将此路径配置到环境变量path中即可,位置没有要求. -->③成功.

 

2.2. 通过cmd打开命令提示符,  安装目录下  D:\tools\WINDOWS.X64_193000_db_home\bin

> sqlplus /nolog



 2.3. 输入conn /as sysdba

> conn /as sysdba

 2.4. 输入alter user sytem identified by 新密码; 

> alter user  sys identified by 123456;

 



可插拔数据库PDB 打开

> sqlplus
>
> sys as sysdba
>
> 123456



> set linesize 300;
>
> select name,open_mode from v$pdbs;
>
> alter session set container=orclpdb;
>
> alter database open;

数据库重启后，需要可插拔数据库是挂载的，需要打开。

