# 安装Docker

> 环境准备

* 会一点liunx 
* Centos7
* 使用Xshell 远程操作

> 环境查看

```shell
# 系统内核是3.10 以上
[root@Home ~]# uname -r
4.18.0-193.28.1.el8_2.x86_64
```

```shell
[root@localhost ~]# 
NAME="CentOS Linux"
VERSION="7 (Core)"
ID="centos"
ID_LIKE="rhel fedora"
VERSION_ID="7"
PRETTY_NAME="CentOS Linux 7 (Core)"
ANSI_COLOR="0;31"
CPE_NAME="cpe:/o:centos:centos:7"
HOME_URL="https://www.centos.org/"
BUG_REPORT_URL="https://bugs.centos.org/"

CENTOS_MANTISBT_PROJECT="CentOS-7"
CENTOS_MANTISBT_PROJECT_VERSION="7"
REDHAT_SUPPORT_PRODUCT="centos"
REDHAT_SUPPORT_PRODUCT_VERSION="7"

```

> 安装

官网地址： https://docs.docker.com/engine/install/centos/

帮助文档

```shell
# 1. 卸载旧的版本
yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine
 # 2. 需要的安装包
 yum install -y yum-utils
# um -y update 
 # 3. 设置镜像仓库
yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo # 默认国外的

# 推荐使用阿里云的
sudo yum-config-manager \
    --add-repo \
    http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
# 4. 安装docker 相关的 docker-ce 社区  ee 企业
yum install docker-ce docker-ce-cli containerd.io

# centos7 可以使用下面命令安装
# yum list docker-ce --showduplicates | sort -r # 查看版本。
# sudo yum install docker-ce-17.12.0.ce
# 5. 启动docker
systemctl start docker
# 6. 查看版本
docker version
```

> 
>

> 8、卸载docer
>
> ```shell
> yum  remove docker-ce docker-ce-cli containerd.io
> ```
>
> 

> yum -y remove docker-ce docker-ce-cli containerd.io

> rm -rf /var/lib/docker  

> 配置镜像加速（自己安装自己登录阿里云去找，不能使用下面的）

```shell
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://nmgdd19m.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
```





# Dcoker 常用命令

```shell
docker version # 查看版本

[root@root ~]# docker info
Client:
 Context:    default
 Debug Mode: false
 Plugins:
  app: Docker App (Docker Inc., v0.9.1-beta3)
  buildx: Build with BuildKit (Docker Inc., v0.6.1-docker)
  scan: Docker Scan (Docker Inc., v0.8.0)

Server:
 Containers: 4
  Running: 3
  Paused: 0
  Stopped: 1
 Images: 4
 Server Version: 20.10.8
 Storage Driver: overlay2
  Backing Filesystem: xfs
  Supports d_type: true
  Native Overlay Diff: true
  userxattr: false
 Logging Driver: json-file
 Cgroup Driver: cgroupfs
 Cgroup Version: 1
 Plugins:
  Volume: local
  Network: bridge host ipvlan macvlan null overlay
  Log: awslogs fluentd gcplogs gelf journald json-file local logentries splunk syslog
 Swarm: inactive
 Runtimes: io.containerd.runc.v2 io.containerd.runtime.v1.linux runc
 Default Runtime: runc
 Init Binary: docker-init
 containerd version: e25210fe30a0a703442421b0f60afac609f950a3
 runc version: v1.0.1-0-g4144b63
 init version: de40ad0
 Security Options:
  seccomp
   Profile: default
 Kernel Version: 4.18.0-193.28.1.el8_2.x86_64
 Operating System: CentOS Linux 8
 OSType: linux
 Architecture: x86_64
 CPUs: 2
 Total Memory: 1.683GiB
 Name: root
 ID: 2YQF:LXCT:NPXA:2F7Y:TH7V:2W7J:OFFW:XUFV:ABFW:XYWZ:QJHF:MCDW
 Docker Root Dir: /var/lib/docker
 Debug Mode: false
 Registry: https://index.docker.io/v1/
 Labels:
 Experimental: false
 Insecure Registries:
  127.0.0.0/8
 Registry Mirrors:
  https://nmgdd19m.mirror.aliyuncs.com/
 Live Restore Enabled: false

docker --help  # 查看帮助
```

官网查看docker 命令： https://docs.docker.com/engine/reference/commandline

## 镜像命令

```shell
docker images 
# 仓库         版本       镜像id         创建时间        内存大小
REPOSITORY    TAG       IMAGE ID       CREATED       SIZE
hello-world   latest    feb5d9fea6a5   2 days ago    13.3kB
tomcat        latest    bb832de23021   11 days ago   680MB
nginx         latest    ad4c705f24d3   2 weeks ago   133MB
redis         latest    02c7f2054405   3 weeks ago   105MB

docker images -a
docker images -q 
# 官网搜索地址  https://hub.docker.com/
docker search
```

![image-20210926134702460](Docker%E6%89%8B%E5%86%8C.images/image-20210926134702460.png)

##  docker rmi 删除镜像

> ![image-20210926135424884](Docker%E6%89%8B%E5%86%8C.images/image-20210926135424884.png)

## 容器命令

```shell
docker pull centos

```

 新建容器并启动

```
docker run [可算选参数] image

# 参数说明
-d 			后台启动
-it   		使用交互方式运行,
-p			指定容器的端口 -p 8080:8080
  	-p      主机端口：容器端口：容器端口
  	-p      容器端口：容器端口
```

>```shell
>[root@root ~]# docker run -it centos /bin/bash
>[root@44525e9a19ef /]# ls
>bin  dev  etc  home  lib  lib64  lost+found  media  mnt  opt  proc  root  run  sbin  srv  sys  tmp  usr  var
>[root@44525e9a19ef /]# exit
>exit
>[root@root ~]# 
>```
>
>```shell
>docker ps   # 查看程序运行状态
>```
>
>##  删除容器 
>
>```
>docker rm 容器id
>docker rm -f $(docker ps -aq)       # 删除所有容器
>docker ps -a -q | xargs docker rm   # 删除所有容器
>```
>
>## 启动停止容器的操作
>
>```shell
>docker start 容器id
>docker stop 容器id
>docker restart 容器id
>dokcer kill 容器id
>```
>
>## 启动后台应用
>
>```shell
>[root@root ~]# docker run -d centos /bin/sh -c "while true; do echo haha; sleep 1; done"
>[root@root ~]# docker ps
>CONTAINER ID   IMAGE     COMMAND                  CREATED              STATUS              PORTS                                       NAMES
>bb0351ce8813   centos    "/bin/sh -c 'while t…"   About a minute ago   Up About a minute   
>## 查看日志
>-tf 显示日志，加上时间戳
>--tail number 显示日志条数
>[root@root ~]# docker logs -tf --tail 10 bb0351ce8813
>2021-09-26T08:19:42.260610000Z haha
>2021-09-26T08:19:43.262461904Z haha
>2021-09-26T08:19:44.264364803Z haha
>2021-09-26T08:19:45.266086493Z haha
>2021-09-26T08:19:46.267896489Z haha
>2021-09-26T08:19:47.269914203Z haha
>
>
>[root@root ~]# docker top 容器id(bb0351ce8813)
>UID                 PID                 PPID                C                   STIME               TTY                 TIME                CMD
>root                114778              114758              0                   16:17               ?                   00:00:00            /bin/sh -c while true; do echo haha; sleep 1; done
>root                115407              114778              0                   16:26               ?                   00:00:00            /usr/bin/coreutils --coreutils-prog-shebang=sleep /usr/bin/sleep 1
>[root@root ~]# 
>
>## 查看镜像元数据
>[root@root ~]# docker inspect bb0351ce8813
>[
>    {
>        "Id": "bb0351ce88135c80928408dafe852c245af6fd18bd110e801ec16000dc629b6a",
>        "Created": "2021-09-26T08:17:47.798840059Z",
>        "Path": "/bin/sh",
>        "Args": [
>            "-c",
>            "while true; do echo haha; sleep 1; done"
>        ],
>        "State": {
>            "Status": "running",
>            "Running": true,
>            "Paused": false,
>            "Restarting": false,
>            "OOMKilled": false,
>            "Dead": false,
>            "Pid": 114778,
>            "ExitCode": 0,
>            "Error": "",
>            "StartedAt": "2021-09-26T08:17:48.060925989Z",
>            "FinishedAt": "0001-01-01T00:00:00Z"
>        },
>        "Image": "sha256:5d0da3dc976460b72c77d94c8a1ad043720b0416bfc16c52c45d4847e53fadb6",
>        "ResolvConfPath": "/var/lib/docker/containers/bb0351ce88135c80928408dafe852c245af6fd18bd110e801ec16000dc629b6a/resolv.conf",
>        "HostnamePath": "/var/lib/docker/containers/bb0351ce88135c80928408dafe852c245af6fd18bd110e801ec16000dc629b6a/hostname",
>        "HostsPath": "/var/lib/docker/containers/bb0351ce88135c80928408dafe852c245af6fd18bd110e801ec16000dc629b6a/hosts",
>        "LogPath": "/var/lib/docker/containers/bb0351ce88135c80928408dafe852c245af6fd18bd110e801ec16000dc629b6a/bb0351ce88135c80928408dafe852c245af6fd18bd110e801ec16000dc629b6a-json.log",
>        "Name": "/affectionate_chaplygin",
>        "RestartCount": 0,
>        "Driver": "overlay2",
>        "Platform": "linux",
>        "MountLabel": "",
>        "ProcessLabel": "",
>        "AppArmorProfile": "",
>        "ExecIDs": null,
>        "HostConfig": {
>            "Binds": null,
>            "ContainerIDFile": "",
>            "LogConfig": {
>                "Type": "json-file",
>                "Config": {}
>            },
>            "NetworkMode": "default",
>            "PortBindings": {},
>            "RestartPolicy": {
>                "Name": "no",
>                "MaximumRetryCount": 0
>            },
>            "AutoRemove": false,
>            "VolumeDriver": "",
>            "VolumesFrom": null,
>            "CapAdd": null,
>            "CapDrop": null,
>            "CgroupnsMode": "host",
>            "Dns": [],
>            "DnsOptions": [],
>            "DnsSearch": [],
>            "ExtraHosts": null,
>            "GroupAdd": null,
>            "IpcMode": "private",
>            "Cgroup": "",
>            "Links": null,
>            "OomScoreAdj": 0,
>            "PidMode": "",
>            "Privileged": false,
>            "PublishAllPorts": false,
>            "ReadonlyRootfs": false,
>            "SecurityOpt": null,
>            "UTSMode": "",
>            "UsernsMode": "",
>            "ShmSize": 67108864,
>            "Runtime": "runc",
>            "ConsoleSize": [
>                0,
>                0
>            ],
>            "Isolation": "",
>            "CpuShares": 0,
>            "Memory": 0,
>            "NanoCpus": 0,
>            "CgroupParent": "",
>            "BlkioWeight": 0,
>            "BlkioWeightDevice": [],
>            "BlkioDeviceReadBps": null,
>            "BlkioDeviceWriteBps": null,
>            "BlkioDeviceReadIOps": null,
>            "BlkioDeviceWriteIOps": null,
>            "CpuPeriod": 0,
>            "CpuQuota": 0,
>            "CpuRealtimePeriod": 0,
>            "CpuRealtimeRuntime": 0,
>            "CpusetCpus": "",
>            "CpusetMems": "",
>            "Devices": [],
>            "DeviceCgroupRules": null,
>            "DeviceRequests": null,
>            "KernelMemory": 0,
>            "KernelMemoryTCP": 0,
>            "MemoryReservation": 0,
>            "MemorySwap": 0,
>            "MemorySwappiness": null,
>            "OomKillDisable": false,
>            "PidsLimit": null,
>            "Ulimits": null,
>            "CpuCount": 0,
>            "CpuPercent": 0,
>            "IOMaximumIOps": 0,
>            "IOMaximumBandwidth": 0,
>            "MaskedPaths": [
>                "/proc/asound",
>                "/proc/acpi",
>                "/proc/kcore",
>                "/proc/keys",
>                "/proc/latency_stats",
>                "/proc/timer_list",
>                "/proc/timer_stats",
>                "/proc/sched_debug",
>                "/proc/scsi",
>                "/sys/firmware"
>            ],
>            "ReadonlyPaths": [
>                "/proc/bus",
>                "/proc/fs",
>                "/proc/irq",
>                "/proc/sys",
>                "/proc/sysrq-trigger"
>            ]
>        },
>        "GraphDriver": {
>            "Data": {
>                "LowerDir": "/var/lib/docker/overlay2/f2dce31477ba4b2c812566afa012795461b5ad389d6899d9d84a2a445b8855bd-init/diff:/var/lib/docker/overlay2/59cc39093dcf701007bf834a54b6d90c03a310af540f33e7c04d3082be65ac97/diff",
>                "MergedDir": "/var/lib/docker/overlay2/f2dce31477ba4b2c812566afa012795461b5ad389d6899d9d84a2a445b8855bd/merged",
>                "UpperDir": "/var/lib/docker/overlay2/f2dce31477ba4b2c812566afa012795461b5ad389d6899d9d84a2a445b8855bd/diff",
>                "WorkDir": "/var/lib/docker/overlay2/f2dce31477ba4b2c812566afa012795461b5ad389d6899d9d84a2a445b8855bd/work"
>            },
>            "Name": "overlay2"
>        },
>        "Mounts": [],
>        "Config": {
>            "Hostname": "bb0351ce8813",
>            "Domainname": "",
>            "User": "",
>            "AttachStdin": false,
>            "AttachStdout": false,
>            "AttachStderr": false,
>            "Tty": false,
>            "OpenStdin": false,
>            "StdinOnce": false,
>            "Env": [
>                "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"
>            ],
>            "Cmd": [
>                "/bin/sh",
>                "-c",
>                "while true; do echo haha; sleep 1; done"
>            ],
>            "Image": "centos",
>            "Volumes": null,
>            "WorkingDir": "",
>            "Entrypoint": null,
>            "OnBuild": null,
>            "Labels": {
>                "org.label-schema.build-date": "20210915",
>                "org.label-schema.license": "GPLv2",
>                "org.label-schema.name": "CentOS Base Image",
>                "org.label-schema.schema-version": "1.0",
>                "org.label-schema.vendor": "CentOS"
>            }
>        },
>        "NetworkSettings": {
>            "Bridge": "",
>            "SandboxID": "e00925b7ae7297aa4162307e0ab5e2a6fcc68e790dbd4ec0f906e868143b34cf",
>            "HairpinMode": false,
>            "LinkLocalIPv6Address": "",
>            "LinkLocalIPv6PrefixLen": 0,
>            "Ports": {},
>            "SandboxKey": "/var/run/docker/netns/e00925b7ae72",
>            "SecondaryIPAddresses": null,
>            "SecondaryIPv6Addresses": null,
>            "EndpointID": "a5c7824e0724c7fbeb2d23f278dce9b125f5265c8a5a1f53258037b13670028d",
>            "Gateway": "172.17.0.1",
>            "GlobalIPv6Address": "",
>            "GlobalIPv6PrefixLen": 0,
>            "IPAddress": "172.17.0.5",
>            "IPPrefixLen": 16,
>            "IPv6Gateway": "",
>            "MacAddress": "02:42:ac:11:00:05",
>            "Networks": {
>                "bridge": {
>                    "IPAMConfig": null,
>                    "Links": null,
>                    "Aliases": null,
>                    "NetworkID": "c7226b8865fadec55fc07621861743e33d921d260434e154e18e38f5923a8331",
>                    "EndpointID": "a5c7824e0724c7fbeb2d23f278dce9b125f5265c8a5a1f53258037b13670028d",
>                    "Gateway": "172.17.0.1",
>                    "IPAddress": "172.17.0.5",
>                    "IPPrefixLen": 16,
>                    "IPv6Gateway": "",
>                    "GlobalIPv6Address": "",
>                    "GlobalIPv6PrefixLen": 0,
>                    "MacAddress": "02:42:ac:11:00:05",
>                    "DriverOpts": null
>                }
>            }
>        }
>    }
>]
>
>```

```shell
# 进入容器 docker exec -it [容器id|容器名字] /bin/bash
docker exec -it tomcat01 /bin/bash
## 查找容器id 
docker ps
CONTAINER ID   IMAGE     COMMAND                  CREATED          STATUS          PORTS                                       NAMES

0a9b0396785f   tomcat    "catalina.sh run"        3 hours ago      Up 3 hours
## 根据id 进入容器目录
[root@root ~]# docker exec -it 0a9b0396785f /bin/bash
root@0a9b0396785f:/usr/local/tomcat 

[root@root ~]# docker attach 容器ID  ## 进入一个正在执行的终端


```

## 容器内文件复制到容器外

```shell
[root@root ~]#ls

[root@root ~]# docker exec -it redis01 /bin/bash
root@e4e5d5aabc12:/data# echo >>test.txt
root@e4e5d5aabc12:/data# ls
backup.db  dump.rdb  root  test.txt  zzh
root@e4e5d5aabc12:/data#  exit
exit
[root@root ~]# docker cp redis01:/data/test.txt /root
[root@root ~]# ls
test.txt
[root@root ~]# 

```



## 安装tomcat

```shell
# 1. 下载
docker pull tomcat
# 2. 启动运行
docker run -d -p 8080:8080 --name tomcat01 tomcat
# 3. 访问出现404
# 进入容器
docker exec -it tomcat01 /bin/bash
# 4. 发现问题默认最小镜像webapps 内没有内容，赋值webaps.dist 内内容到webapps
cp -r  webapps.dist/* webapps

```

## 安装 nginx

```shell
# 搜索
docker serch nginx
# 2. 下载
docker pull nginx 

# 3.运行
docker run -d --name nginx01 -p 80:80 nginx
# 进入容器
docker exec -it nginx01 /bin/bash
```

停止服务

> docker stop nginx01

# 安装redis

```shell
# 搜索
docker serch redis
# 2. 下载
docker pull redis 

# 3.运行
docker run -d --name redis01 -p 80:80 nginx
# 4.  进入容器
docker exec -it redis01 /bin/bash
# root@e4e5d5aabc12:/data# 
# 5. 开启客户端
redis-cli
127.0.0.1:6379> 
```

停止服务

> docker stop redis01

# mysql 安装

```shell
## 1、下载mysql 镜像
[root@root ~]# docker pull mysql
## 2、查看镜像
[root@root ~]# docker images
REPOSITORY   TAG       IMAGE ID       CREATED       SIZE
mysql        latest    0716d6ebcc1a   3 weeks ago   514MB
## 3、后台运行msyql
[root@root ~]# docker run -p 3306:3306 --name mysql8.0 -e MYSQL_ROOT_PASSWORD=root -d mysql
6c63da935c90ae249569d1048c8628e839233246e5e1a30318e17b30b8b95343
## 4、查看mysql 安装情况
[root@root ~]# docker ps
CONTAINER ID   IMAGE     COMMAND                  CREATED          STATUS          PORTS                                                  NAMES
6c63da935c90   mysql     "docker-entrypoint.s…"   11 seconds ago   Up 11 seconds   0.0.0.0:3306->3306/tcp, :::3306->3306/tcp, 33060/tcp   mysql8.0
## 5、进入msyql 安装目录
[root@root ~]# docker exec -it mysql8.0 /bin/bash
root@6c63da935c90:/# mysql -u root -p
Enter password: 
mysql> mysql8.0
```

停止服务

> docker stop mysql8.0

## 安装mangodb



## 安装 ElasticSearch

> ```shell
> docker stats # 查看cpu 状态
> es
> ```
>
> 

## 定制镜像

```shell
[root@root ~]# docker commit -m="add" -a="deyo" 0a9b0396785f tomcat02:01.1
sha256:66df32ec04978bc7042bea175c001a28d1c46184cbc4cb7bd1d483d4108586c0
[root@root ~]# docker images
REPOSITORY   TAG       IMAGE ID       CREATED          SIZE
tomcat02     01.1      66df32ec0497   14 seconds ago   684MB 
tomcat       latest    bb832de23021   12 days ago      680MB
[root@root ~]# 
```

 下次直接使用自己提交的镜像实现部署就可以了

## 容器数据卷

什么式容器数据卷

docker 的理念回顾

将应用和换将打包成一个镜像。

数据？ 如果数据都在容器中。如果容器删除，数据就会丢失！

容器之间有一个数据共享技术。

容器的持久化，和数据共享。

> 实战操作 使用centos

>```shell
>[root@root ~]# docker run -it -v /root:/home/test centos
>[root@root ~]# docker ps
>CONTAINER ID   IMAGE     COMMAND       CREATED          STATUS          PORTS     NAMES
>27d086e155a0   centos    "/bin/bash"   20 minutes ago   Up 20 minutes             pensive_galois
>[root@root ~]# docker inspect 27d086e155a0
>    ## 查询结果多了这条信息说明挂在成功。
>      "HostConfig": {
>                 "Binds": [
>                     "/root:/home/test"
>                 ],
>     
>      "Mounts": [
>                 {
>                     "Type": "bind",
>                     "Source": "/root",
>                     "Destination": "/home/test",
>                     "Mode": "",
>                     "RW": true,
>                     "Propagation": "rprivate"
>                 }
>             ],
>     ```

  ##  测试使用命令。




> ![](Docker%E6%89%8B%E5%86%8C.images/image-20210927122134273.png)



## 实战安装mysql

![image-20210927124755328](Docker%E6%89%8B%E5%86%8C.images/image-20210927124755328.png)

![image-20210927124839267](Docker%E6%89%8B%E5%86%8C.images/image-20210927124839267.png)

![image-20210927143657121](Docker%E6%89%8B%E5%86%8C.images/image-20210927143657121.png)

![image-20210927143735363](Docker%E6%89%8B%E5%86%8C.images/image-20210927143735363.png)

![image-20210927143816901](Docker%E6%89%8B%E5%86%8C.images/image-20210927143816901.png)

# 发布自己的镜像

# 



## 部署redis 集群

  

# 安装  docker compose

官网安装地址： https://docs.docker.com/compose/install/

## 第一种

1、下载

 ```
 # 官网镜像
  sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
  
  # 国内高速镜像
curl -L https://get.daocloud.io/docker/compose/releases/download/1.29.2/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose


 ```
 2、修改权限。

```

sudo chmod +x /usr/local/bin/docker-compose
```

3、测试安装
```
docker-compose -version
```
4、卸载

```
sudo rm /usr/local/bin/docker-compose
```

## 第二种安装方式
1、 pip 安装
```
sudo pip install docker-compose
```
2、测试安装
```
docker-compose -version
```
3、 卸载

```
sudo pip uninstall docker-compose
```

