## 1. 环境准备

Elasticsearch是使用java开发的，且7.8版本的ES需要JDK版本8以上，默认安装

安装之前访问

https://www.elastic.co/cn/support/matrix#matrix_jvm

查看对应的系统和jdk 环境

包带有idk环境，如果系统配置JAVA_HOME，那么使用系统默认的JDK，如果没有配
置使用自带的JDK，一般建议使用系统配置的JDK。

## Docker 快速安装 ElasticSearch 

查看要安装的 es 和系统还有jdk 版本是否匹配。

```
    docker rm -f $(docker ps -aq)
```

访问网站

https://www.elastic.co/guide/en/elasticsearch/reference/7.8/docker.html

```shell
docker run -d --name es01 -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.9.3
```

访问 http://192.168.247.128:9200/

http://192.168.247.131:9200/

http://39.103.170.254:9200/



![image-20220907173115708](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220907173115708.png)

## 安装kibana

```console
docker run -d --name kibana  -p 5601:5601   kibana:7.9.3
docker exec  -it kibana /bin/bash
vi config/kibana.yml

docker restart kibana
```

修改配置文件

查看es ip 地址

```bash
docker inspect es01
```

![image-20220909094824324](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220909094824324.png)



```bash
docker exec  -it kibana /bin/bash
vi config/kibana.yml
```

![image-20220909094347914](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220909094347914.png)

```bash
exit
docker restart kibana
```

访问 虚拟机

http://192.168.247.128:5601/

http://192.168.247.131:5601/



## 安装中文分词器

```bash
$ docker exec -it es01 /bin/bash

$ ./bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.9.3/elasticsearch-analysis-ik-7.9.3.zip
```

# 安装 ES 8.0.0

```
# 拉取镜像
docker pull docker.elastic.co/elasticsearch/elasticsearch:8.0.0
docker pull docker.elastic.co/kibana/kibana:8.0.0
# 启动 Elastic Search 8.0.0
docker run -it \
--name elasticsearch \
--restart=always \
-p 9200:9200 \
-p 9300:9300 \
-e "discovery.type=single-node" \
docker.elastic.co/elasticsearch/elasticsearch:8.0.0

启动后记住如下信息
#####
---------------------------------------------------------------------------------------------------------------------------------------
-> Elasticsearch security features have been automatically configured!
-> Authentication is enabled and cluster connections are encrypted.

->  Password for the elastic user (reset with `bin/elasticsearch-reset-password -u elastic`): 
  caJhf4PQ2rq-1ljWdhB5						####   	3.用户名elastic 登录密码

->  HTTP CA certificate SHA-256 fingerprint:    #####     1. 打开浏览器输入
  d6fd02f747488d8fa2e2f60154e68491c6edcc9bdca38c50b4b5ae8ff6db9191

->  Configure Kibana to use this cluster:       #### 	2.kibana 连接配置输入
* Run Kibana and click the configuration link in the terminal when Kibana starts.
* Copy the following enrollment token and paste it into Kibana in your browser (valid for the next 30 minutes):
  eyJ2ZXIiOiI4LjAuMCIsImFkciI6WyIxNzIuMTcuMC4yOjkyMDAiXSwiZmdyIjoiZDZmZDAyZjc0NzQ4OGQ4ZmEyZTJmNjAxNTRlNjg0OTFjNmVkY2M5YmRjYTM4YzUwYjRiNWFlOGZmNmRiOTE5MSIsImtleSI6IjZCZzFKWU1CNVNBUXN0QW9wZVl5OkJ2bnM1c3c4VHJDWmFkTXQwSjJaM3cifQ==

-> Configure other nodes to join this cluster:
* Copy the following enrollment token and start new Elasticsearch nodes with `bin/elasticsearch --enrollment-token <token>` (valid for the next 30 minutes):
  eyJ2ZXIiOiI4LjAuMCIsImFkciI6WyIxNzIuMTcuMC4yOjkyMDAiXSwiZmdyIjoiZDZmZDAyZjc0NzQ4OGQ4ZmEyZTJmNjAxNTRlNjg0OTFjNmVkY2M5YmRjYTM4YzUwYjRiNWFlOGZmNmRiOTE5MSIsImtleSI6IjV4ZzFKWU1CNVNBUXN0QW9wZVl2Om5UTEZzaFZlU1F1XzVvRzNVV2RCLVEifQ==

  If you're running in Docker, copy the enrollment token and run:
  `docker run -e "ENROLLMENT_TOKEN=<token>" docker.elastic.co/elasticsearch/elasticsearch:8.0.0`

###

# 第一次的日志中会打印出默认用户elastic的初始密码，以及用于Kibana启动的enrollment token（半小时有效）注意保存
 
# 启动 Kibana 8.0.0
docker run \
--name kibana \
--net elastic \
-p 5601:5601 \
docker.elastic.co/kibana/kibana:8.0.0

docker run \
--name kibana \
-p 5601:5601 \
docker.elastic.co/kibana/kibana:8.0.0
# 第一次的日志中会打印出启动配置网址，在浏览器打开并输入enrollment token，等待完成配置
# 使用用户名elastic和之前保存的密码登录
 
# 安装ik分词器 方式一

# 下载 https://github.com/medcl/elasticsearch-analysis-ik/releases
docker cp elasticsearch-analysis-ik-8.0.0.zip elasticsearch:/usr/share/elasticsearch/plugins
# 进入elasticsearch命令行
cd plugins/
mkdir ik
mv elasticsearch-analysis-ik-8.0.0.zip ik/
cd ik/
unzip elasticsearch-analysis-ik-8.0.0.zip
rm elasticsearch-analysis-ik-8.0.0.zip

安装ik分词器  方式二
# 
docker exec -it elasticsearch /bin/bash
./bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v8.4.1/elasticsearch-analysis-ik-8.4.1.zip

# 重启es和kibana
docker restart elasticsearch
docker restart kibana
 
# 在kibana中测试ik分词器
GET _analyze
{
  "text" : "中华人民共和国国歌",
  "analyzer": "ik_max_word"
}
```

```bash
密码重置
docker exec -it elasticsearch /bin/bash
bin/elasticsearch-reset-password -u elastic
```



---------------------------------------------------------------------------------------------------------------------------------------

```
sysctl -w vm.max_map_count=262144
sysctl -a|grep vm.max_map_count
# 执行如下步骤
vim /etc/sysctl.conf
	vm.max_map_count=262144
sysctl -p
```



curl --cacert http_ca.crt -u elastic https://192.168.247.128:9200



```
docker run -e ENROLLMENT_TOKEN=eyJ2ZXIiOiI4LjQuMSIsImFkciI6WyIxNzIuMTcuMC4yOjkyMDAiXSwiZmdyIjoiZmNmZGZiZTk1N2RhYTE4YWJiMDIzNTE2NTUwZjNmYjQ3Njk0OWExNmFjNDk1YTJkZjRlOGZmODRiMDZkMzYyYiIsImtleSI6Ik5LeTBKWU1CYTNYYmlBSjZkcUdtOmN0LXotbXRZUk51Ny1PNmpXOEdxeXcifQ==  \
--name es02 \
--restart=always \
-p 9002:9200 \
-p 9302:9300 \
-e "discovery.type=single-node" \
docker.elastic.co/elasticsearch/elasticsearch:8.4.1
```

