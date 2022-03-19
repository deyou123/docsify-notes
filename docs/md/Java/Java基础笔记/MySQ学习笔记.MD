# 查询数据

## 1. 基本语法

```sql
select
	{*|<字段列表>}
	[
        from <表1>,<表2>...
        [where <表达式>]
        [group by <字段>]
        [order by asc|desc]
        [limit <offset>,<row count>]
    ]
```

创建数据

```sql
create table fruits(
    f_id char(10) not null,
    s_id int not null,
    f_name char(255) not null,
    f_price decimal(8,2) not null,
    primary key(f_id)
);
INSERT INTO fruits (f_id, s_id, f_name, f_price)
     VALUES('a1', 101,'apple',5.2),
     ('b1',101,'blackberry', 10.2),
     ('bs1',102,'orange', 11.2),
     ('bs2',105,'melon',8.2),
     ('t1',102,'banana', 10.3),
     ('t2',102,'grape', 5.3),
     ('o2',103,'coconut', 9.2),
     ('c0',101,'cherry', 3.2),
     ('a2',103, 'apricot',2.2),
     ('l2',104,'lemon', 6.4),
     ('b2',104,'berry', 7.6),
     ('m1',106,'mango', 15.6),
     ('m2',105,'xbabay', 2.6),
     ('t4',107,'xbababa', 3.6),
     ('m3',105,'xxtt', 11.6),
     ('b5',107,'xxxx', 3.6);
```

```sql
使用SELECT语句查询f_id字段的数据。
 SELECT f_id, f_name FROM fruits;
```

## 2. 单表查询

```sql
# 1. 查询所有字段
select * from fruits;

# 2. 指定字段查询
select f_id,s_id,f_name from fruits;

# 3. 条件查询
select f_name,f_price
from fruits
where f_price = 10.2;

# 4. 查询名为apple 的价格

select f_name, f_price
from fruits
where f_name = 'apple';
# 4. 带In 关键字查询
# 查询s_id 为101 和102 的记录
select s_id, f_name,f_price
from fruits
where s_id in(101,102)
order by f_name;
# 5. Not In 关键字查询

select s_id, f_name,f_price
from fruits
where s_id not in(101,102)
order by f_name;

# 6. between and 范围查询
select f_name,f_price
from fruits
where f_price between 2.00 and 10.2;

# 7. 根据价格排序
select f_name,f_price
from fruits
where f_price between 2.00 and 10.2
order by f_price; # 默认增序asc, 降序desc.
# 8. Like 字符匹配查询，首字母为a 的字段

select f_id, f_name
from fruits
where f_name like 'a%';

# 9. 中间位置为g 的字段
select f_id, f_name
from fruits
where f_name like '%g%';

# 10. b 开头y 结尾的字段
select f_id, f_name
from fruits
where f_name like 'b%y';

# 11. 下划线“_” 通配符号，一个占位一个字符。
select f_id, f_name
from fruits
where f_name like '____y';

# 12 查询空值，需要创建一个测试表
CREATE TABLE customers
(
  c_id      int       NOT NULL AUTO_INCREMENT,
  c_name    char(50)  NOT NULL,
  c_address char(50)  NULL,
  c_city    char(50)  NULL,
  c_zip     char(10)  NULL,
  c_contact char(50)  NULL,
  c_email   char(255) NULL,
  PRIMARY KEY (c_id)
);

# 插入4 条 数据
INSERT INTO customers(c_id, c_name, c_address, c_city,
c_zip,  c_contact, c_email)
VALUES(10001, 'RedHook', '200 Street ', 'Tianjin',
 '300000',  'LiMing', 'LMing@163.com'),
(10002, 'Stars', '333 Fromage Lane',
 'Dalian', '116000',  'Zhangbo','Jerry@hotmail.com'),
(10003, 'Netbhood', '1 Sunny Place', 'Qingdao',  '266000',
 'LuoCong', NULL),
(10004, 'JOTO', '829 Riverside Drive', 'Haikou',
 '570000',  'YangShan', 'sam@hotmail.com');
## 表创建成功

# 查询空值

select c_id,c_name,c_email
from customers
where c_email is null;

# 13. count(字段) 查询记录
SELECT COUNT(*) AS cust_num  FROM customers;

# 14. 查询非空

select c_id,c_name,c_email
from customers
where c_email is not null;

# 15 And 多条件查询
select f_id, f_name,f_price
from fruits
where s_id = '101' and f_price >= 5;

# 16. 多个and 语句

select f_id, f_name,f_price
from fruits
where s_id in('101', '102')
    and f_price >= 5;

select f_id, f_name,f_price
from fruits
where s_id in('101', '102')
    and f_name = 'apple';
# 17. 带or 的多条件查询

select f_id, f_name,f_price
from fruits
where s_id = 101 or s_id = 102;
# 也可使用In 语句查询
select f_id, f_name,f_price
from fruits
where s_id in(101,102);
# in 比 or 更快， in 可以指执行复杂的嵌套
# or 和 and 一起使用时，先使用and。 and 优先级要高于or

# 18. distinct 查询不重复的结果
# s_id 有重复数据
select s_id from fruits;

# s_id 无重复数据
select distinct s_id from fruits;
# 19. 排序

# 单列排序
select f_name from fruits
order by f_name;

# 多列排序,在前面的优先级高
select f_name, f_price from fruits
order by f_name, f_price;

# 指定排序方向
select f_name, f_price from fruits
order by  f_price desc ;

select f_name, f_price from fruits
order by f_name asc ,
         f_price desc ;

# 20. 分组查询
# group by

# 根据s_id 对 fruits 中数据分组排序
select s_id, COUNT(*)
from fruits
group by s_id;

# 21. 对s_id 进行分组，并查出组内水果种类
select s_id, GROUP_CONCAT(f_name) as names
from fruits
group by s_id;
# 22. 使用having 过滤 分组大于1的组
select s_id, GROUP_CONCAT(f_name) as names
from fruits
group by s_id having count(f_name) >1;
# having 和 where 的区别，where 过滤分组前的数据，having 过滤分组后的数据。
# where 排除的记录不在包括在分组中。

# 23.  使用with rollup 计算总和

select s_id, count(f_id) as names
from fruits
group by s_id with rollup ;

# 24. 多字段分组
select * from fruits
group by s_id,f_name;

# 25. group by 和 order by 一起使用
# 演示数据
CREATE TABLE orderitems
(
    o_num      int          NOT NULL,
    o_item     int          NOT NULL,
    f_id       char(10)     NOT NULL,
    quantity   int          NOT NULL,
    item_price decimal(8,2) NOT NULL,
    PRIMARY KEY (o_num,o_item)
) ;
INSERT INTO orderitems(o_num, o_item, f_id, quantity, item_price)
VALUES(30001, 1, 'a1', 10, 5.2),
      (30001, 2, 'b2', 3, 7.6),
      (30001, 3, 'bs1', 5, 11.2),
      (30001, 4, 'bs2', 15, 9.2),
      (30002, 1, 'b3', 2, 20.0),
      (30003, 1, 'c0', 100, 10),
      (30004, 1, 'o2', 50, 2.50),
      (30005, 1, 'c0', 5, 10),
      (30005, 2, 'b1', 10, 8.99),
      (30005, 3, 'a2', 10, 2.2),
      (30005, 4, 'm1', 5, 14.99);
# 查询按照o_num 分组， 总价和》 200 的数据
select o_num,sum(quantity * orderitems.item_price) as orderTotal
from orderitems
group by o_num
having orderTotal > 200;

## 对上语句进行排序
select o_num,sum(quantity * orderitems.item_price) as orderTotal
from orderitems
group by o_num
having orderTotal > 200
order by orderTotal;

# 注意，当使用rollup 时 不能使用 order by 会互斥。

# 26. 私用LIMIT 限制查询结果。
# 查询前四条数据
select * from fruits limit 4;
# 8.0
select * from fruits limit 3 offset 1;
# offset 偏移量，从0 开始。
select * from fruits limit 1,3;
```

## 3. 聚合函数

1. count 函数

```sqlite
select count(*) as cost_num from customers;

select COUNT(c_email) as email_num from customers;# 3条记录
# * 号对null 字段计算
select COUNT(*) as email_num from customers;# 4条记录

# 先按照订单号分组，再计算分组中的总记录数。
select o_num,count(f_id) from orderitems
group by o_num;
```

2. sum 函数

```sql
# 先分组，后统计
select sum(quantity) as items_total
from orderitems
group by o_num;
统计o_num = 3005 的记录
select sum(quantity) as items_total
from orderitems
where o_num = 30005;
```

3. avg 函数

```sql
# 查询每个供应商水果的平均值
select s_id,avg(f_price) as avg_price 
from fruits
group by s_id;
```

4. MAX()函数

```sql
select max(f_price) as max_price
from fruits;
# 查找不同供应商的最高价
select s_id, max(f_price) as max_price
from fruits
group by s_id;

#查找字符类型的最大值
select max(f_name) from fruits;
```

5. min 函数

```sql
select min(f_price) as min_price
from fruits;

select s_id, min(f_price) as min_price
from fruits
group by s_id;

select min(f_name) from fruits;
```

# 多表连接查询

## 1. 内连接查询

内连接查询，使用比较运算符进行表间的列数据比较操作组合成新的记录。在内连接插叙中，只有满足条件的记录从才能出现在结果里。

```sql
# 为了演示的需要，首先创建数据表suppliers，SQL语句如下：
CREATE TABLE suppliers
(
  s_id      int      NOT NULL AUTO_INCREMENT,
  s_name    char(50) NOT NULL,
  s_city    char(50) NULL,
  s_zip     char(10) NULL,
  s_call    CHAR(50) NOT NULL,
  PRIMARY KEY (s_id)
) ;
# 插入需要演示的数据，SQL语句如下：
INSERT INTO suppliers(s_id, s_name,s_city,  s_zip, s_call)
VALUES(101,'FastFruit Inc.','Tianjin','300000','48075'),
(102,'LT Supplies','Chongqing','400000','44333'),
(103,'ACME','Shanghai','200000','90046'),
(104,'FNK Inc.','Zhongshan','528437','11111'),
(105,'Good Set','Taiyuang','030000', '22222'),
(106,'Just Eat Ours','Beijing','010', '45678'),
(107,'DK Inc.','Zhengzhou','450000', '33332');

create table fruits(
    f_id char(10) not null,
    s_id int not null,
    f_name char(255) not null,
    f_price decimal(8,2) not null,
    primary key(f_id)
);
INSERT INTO fruits (f_id, s_id, f_name, f_price)
     VALUES('a1', 101,'apple',5.2),
     ('b1',101,'blackberry', 10.2),
     ('bs1',102,'orange', 11.2),
     ('bs2',105,'melon',8.2),
     ('t1',102,'banana', 10.3),
     ('t2',102,'grape', 5.3),
     ('o2',103,'coconut', 9.2),
     ('c0',101,'cherry', 3.2),
     ('a2',103, 'apricot',2.2),
     ('l2',104,'lemon', 6.4),
     ('b2',104,'berry', 7.6),
     ('m1',106,'mango', 15.6),
     ('m2',105,'xbabay', 2.6),
     ('t4',107,'xbababa', 3.6),
     ('m3',105,'xxtt', 11.6),
     ('b5',107,'xxxx', 3.6);
```

####  查看表结构

desc fruits;

desc suppliers;

1. **内连接** 通过s_Id 字段建立两个表的连接

```sql
select * from fruits,suppliers where fruits.s_id = suppliers.s_id;

```

2. **自连接**。供应f_id= ‘a1’的水果供应商提供的其他水果种类

```sql
 SELECT f1.f_id, f1.f_name
      FROM fruits AS f1, fruits AS f2
      WHERE f1.s_id = f2.s_id AND f2.f_id = 'a1';
```

## 2. 外连接

创建数据

```sql
CREATE TABLE orders
(
    o_num  int      NOT NULL AUTO_INCREMENT,
    o_date datetime NOT NULL,
    c_id   int      NOT NULL,
    PRIMARY KEY (o_num)
);

INSERT INTO orders(o_num, o_date, c_id)
VALUES(30001, '2008-09-01', 10001),
      (30002, '2008-09-12', 10003),
      (30003, '2008-09-30', 10004),
      (30004, '2008-10-03', 10005),
      (30005, '2008-10-08', 10001);
```



- 140.82.112.3  github.com
- 199.232.69.194  github.global.ssl.fastly.net

