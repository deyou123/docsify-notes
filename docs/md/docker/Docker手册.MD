# 安装Docker

## Centos安装

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

> //设置docker 开机自启动
>
> systemctl enable docker

> 8、卸载docer
>
> ```shell
> yum  remove docker-ce docker-ce-cli containerd.io
> ```
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

## Ubunt安装docker

https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-on-ubuntu-20-04

Ubuntu 官方存储库中提供的 Docker 安装包可能不是最新版本。为确保我们获得最新版本，我们将从官方 Docker 存储库安装 Docker。为此，我们将添加一个新的包源，从 Docker 添加 GPG 密钥以确保下载有效，然后安装包。

首先，更新您现有的软件包列表：

```bash
sudo apt update
```



接下来，安装一些允许`apt`通过 HTTPS 使用包的必备包：

```bash
sudo apt install apt-transport-https ca-certificates curl software-properties-common
```



然后将官方 Docker 存储库的 GPG 密钥添加到您的系统：

```bash
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
```



将 Docker 存储库添加到 APT 源：

```bash
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable"
```



这也将使用新添加的 repo 中的 Docker 包更新我们的包数据库。

确保您要从 Docker 存储库而不是默认的 Ubuntu 存储库进行安装：

```bash
apt-cache policy docker-ce
```



你会看到这样的输出，尽管 Docker 的版本号可能不同：

apt-cache 策略 docker-ce 的输出

```
docker-ce:
  Installed: (none)
  Candidate: 5:19.03.9~3-0~ubuntu-focal
  Version table:
     5:19.03.9~3-0~ubuntu-focal 500
        500 https://download.docker.com/linux/ubuntu focal/stable amd64 Packages
```

请注意，`docker-ce`它没有安装，但安装的候选者来自 Ubuntu 20.04 ( `focal`) 的 Docker 存储库。

最后，安装 Docker：

```bash
sudo apt install docker-ce
```



现在应该安装 Docker，启动守护程序，并且启用该进程以在启动时启动。检查它是否正在运行：

```bash
sudo systemctl status docker
```



输出应类似于以下内容，表明该服务处于活动状态且正在运行：

```
Output● docker.service - Docker Application Container Engine
     Loaded: loaded (/lib/systemd/system/docker.service; enabled; vendor preset: enabled)
     Active: active (running) since Tue 2020-05-19 17:00:41 UTC; 17s ago
TriggeredBy: ● docker.socket
       Docs: https://docs.docker.com
   Main PID: 24321 (dockerd)
      Tasks: 8
     Memory: 46.4M
     CGroup: /system.slice/docker.service
             └─24321 /usr/bin/dockerd -H fd:// --containerd=/run/containerd/containerd.sock
```

现在安装 Docker 不仅为您提供了 Docker 服务（守护程序），还为您提供了`docker`命令行实用程序或 Docker 客户端。

查看docker 版本

```
docker version
```

配置阿里云镜像加速器

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

详解（可以不看直接使用上述代码）

首先，我们需要编辑 `/etc/docker/daemon.json` 文件：

```bash
$ sudo vim /etc/docker/daemon.json
```

然后加入如下内容：

```bash
{
  "registry-mirrors": ["https://nmgdd19m.mirror.aliyuncs.com"]
}
```

修改之后，需要重启 `docker` 服务，让修改生效。使用如下命令：

```bash
$ sudo service docker restart
```

修改存储引擎（可选）

- Red Hat Enterprise Linux：4.x 版本内核或更高版本 + Docker 17.06 版本或更高版本，建议使用 Overlay2。
- Red Hat Enterprise Linux：低版本内核或低版本的 Docker，建议使用 Device Mapper。
- Ubuntu Linux：4.x 版本内核或更高版本，建议使用 Overlay2。
- Ubuntu Linux：更早的版本建议使用 AUFS。
- SUSE Linux Enterprise Server：Btrfs。

在 Linux 上，大家可以通过修改 `/etc/docker/daemon.json` 

```json
{
  "registry-mirrors": ["https://nmgdd19m.mirror.aliyuncs.com"],
  "storage-driver": "overlay2"
}
```

```
docker info  
```

![image-20220830150550518](images/image-20220830150550518.png)

再次强调，上面的清单内容只是一个参考建议。用户需要时刻关注 Docker 文档中关于存储驱动的最新支持和版本兼容列表。尤其当大家正在使用 Docker 企业版（EE），并且有售后支持合同的情况下，更有必要查阅最新文档。



# Dcoker 常用命令

## 

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

## 镜像

镜像可以看做一个类，可以启动一个或者多个容器

镜像由多个层组成，每层叠加之后，从外部看来就如一个独立的对象。镜像内部是一个精简的操作系统（OS），同时还包含应用运行所必须的文件和依赖包。因为容器的设计初衷就是快速和小巧，所以镜像通常都比较小。

![image-20220830185040444](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220830185040444.png)



从某个镜像启动一个或多个容器。一旦容器从镜像启动后，二者之间就变成了互相依赖的关系，并且在镜像上启动的容器全部停止之前，镜像是无法被删除的。尝试删除镜像而不停止或销毁使用它的容器，会导致下面的错误。

```shell
root@ubuntu:~# docker run -d redis
ee0911d3a0e03f2ef52676c2e15e6985dee98a2d15285b728ded9e8da6f1f729
root@ubuntu:~# docker rmi redis:latest 
Error response from daemon: conflict: unable to remove repository reference "redis:latest" (must force) - container ee0911d3a0e0 is using its referenced image 7614ae9453d1
root@ubuntu:~# 
```

Linux Docker 主机本地镜像仓库通常位于 `/var/lib/docker/` ，

Windows Docker 主机则是 `C:\ProgramData\docker\windowsfilter` 。

docker images 查看镜像



### 获取镜像



```dockerfile
docker search mysql									# 搜索镜像
docker search redis --filter "is-official=true"   	# 搜索官方镜像
docker search redis --limit 100						# 默认显示25行，最多指定显示100行
docker pull ubuntu:18.04							# 拉取镜像
docker image ls 或者 docker images				   # 查看镜像
```

### 查看镜像

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



###  删除镜像

>  删除一个 可以通过名称 也可以指定id -f表示强制 
>
>  `docker rmi -f 9cfcce23593a`
>
>  删除多个 用空格分隔id 
>
>  `docker rmi -f 镜像id 镜像id 镜像id `
>
>  删除所有  
>
>  `docker rmi -f $(docker images -aq) `
>
>   images -aq就是查所有镜像id，从而递归删除



### docker commit 修改镜像

提交操作过的镜像

```bash
docker commit -m "提交信息描述" -a "作者" 容器id  目标镜像:tag
```

```bash
root@ubuntu:~# docker run -d -it --name c1 centos
0dadb85cd70b1ca8eccb6b7175ee0a953d130324302c98cb20104f6680b2621b
root@ubuntu:~# docker exec -it c1 /bin/bash

[root@0dadb85cd70b /]# cd /root
[root@0dadb85cd70b ~]# ls
anaconda-ks.cfg  anaconda-post.log  original-ks.cfg
[root@0dadb85cd70b ~]# touch a.txt
[root@0dadb85cd70b ~]# ls
a.txt  anaconda-ks.cfg	anaconda-post.log  original-ks.cfg
[root@0dadb85cd70b ~]# exit
exit

root@ubuntu:~# docker ps
CONTAINER ID   IMAGE     COMMAND       CREATED              STATUS              PORTS     NAMES
0dadb85cd70b   centos    "/bin/bash"   About a minute ago   Up About a minute             c1

root@ubuntu:~# docker commit -m 'add a.txt in /root/' -a 'deyou' 0dadb85cd70 mycentos:1.0
sha256:6fc1b2f4f209bbdf87834f3cf24bb3cb7a5305fc2c46c9fc4f74a0e18bf4ece4
root@ubuntu:~# docker images
REPOSITORY                                      TAG       IMAGE ID       CREATED          SIZE
mycentos                                        1.0       6fc1b2f4f209   21 seconds ago   231MB

```



## 容器

容器是镜像的运行时实例。正如从虚拟机模板上启动 VM 一样，用户也同样可以从单个镜像上启动一个或多个容器。虚拟机和容器最大的区别是容器更快并且更轻量级——与虚拟机运行在完整的操作系统之上相比，容器会共享其所在主机的操作系统/内核。

从更高层面上来讲，Hypervisor 是**硬件虚拟化（Hardware Virtualization）**——Hypervisor 将硬件物理资源划分为虚拟资源；另外，容器是**操作系统虚拟化（OS Virtualization）**——容器将系统资源划分为虚拟资源。



器共享一个操作系统/内核。这意味着只有一个操作系统消耗 CPU、RAM 和存储资源，只有一个操作系统需要授权，只有一个操作系统需要升级和打补丁。同时，只有一个操作系统面临被攻击的风险。简言之，就是只有一份 OS 损耗！

```shell
docker pull redis
```

###  新建容器并启动

```
docker run -it redis /bin/bash  # 或
docker run -it redis bash

docker run -d redis 			# -d  后台启动
docker run --name Myredis redis # --name 给容器起别名
docker run -p 6379:6379			# -p 指定端口号

```

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
>docker ps   # 查看正在运行的容器
>docker ps -a # 查看所有容器，包括已经停止的
>docke ps -l   # 查看最后一个创建的容器
### 删除容器

```
docker rm 容器id
docker rm -f  容器id					# 强制删除容器 -f 
docker rm -f $(docker ps -aq)       # 删除所有容器
docker ps -a -q | xargs docker rm   # 删除所有容器
```



**删除容器的最佳方式还是分两步，先停止容器然后删除。这样可以给容器中运行的应用/进程一个停止运行并清理残留数据的机会：**

> docker stop 容器id
>
> docker rm 容器id



### 启动停止容器的操作

>
>```shell
>docker start 容器id
>docker stop 容器id
>docker restart 容器id
>dokcer kill 容器id
>```
>
### 重启策略进行容器的自我修复

重启策略

#### allsays

容器支持的重启策略包括 `always`、`unless-stopped` 和 `on-failed` 。

```shell
docker run -d --name myredis1 --restart=always redis
```

```
docker inspect myredis1   # 查看指定容器详细状态
```

![image-20220902074113035](images/image-20220902074113035.png)

![image-20220902074153472](images/image-20220902074153472.png)

```
docker stop myredis1
docker start myredis1
docker restart myredis1
```

杀死设置了always 正在运行的容器进程

```
docker inspect -f '{{.State.Pid}}' myredis1
kill -9 查询出的pid
```

再次查看状态

```docker
docker inspect myredis1
```

依旧是运行状态

![image-20220902080031706](images/image-20220902080031706.png)

如上图 

**命令让容器正常重启，不累加重启次数**

```
docker stop myredis1
docker start myredis1
docker restart myredis1
docker kill myredis1
```

`docker inspect myredis1` 查看容器详细状态 " RestartCount" 参数值 不变

**干掉进程，容器会重启，累加次数**

```
docker inspect -f '{{.State.Pid}}' myredis1
kill -9 查询出的pid
```

`docker inspect myredis1` 查看容器详细状态 " RestartCount" 参数值 +1

#### On failue

指定重启次数超过10次不成功就放弃

```
docker run -d --name myredis2 --restart=on-failure:10 redis
```

我们发现容器也可以被重启，说明干掉进程仍归类为异常退出，只是没记记录ExitCode字段而已。不记录重启次数。

```shell
#查看容器重启次数* 
docker inspect -f "{{ .RestartCount }}" myresdis2
#查看容器最后一次的启动时间
docker inspect -f "{{ .State.StartedAt }}" myresdis2
```

#### unless-stopped

容器退出时总是重启，但不考虑Docker守护进程启动时就已经停止的容器Stopped (Exited)

```
docker run -d --name myredis3 --restart=unless-stopped redis
docker stop myredis3
service docker restart
```

指定了处于Stop 状态的容器，不会重启。

![image-20220902084119287](images/image-20220902084119287.png)



**注意：**

**Docker Compose 或者 Docker Stack，可以在 service 对象中配置重启策略**

```shell
version: "3.5"
services:
  myservice:
    <Snip>
    restart_policy:
      condition: always | unless-stopped | on-failure
```

#### Web 容器示例

```shell
# 杀死80端口占用
sudo fuser -k 80/tcp
# 快速开启一个web 容器示例
docker  run -d --name webserver -p 80:8080 nigelpoulton/pluralsight-docker-ci
```

```shell
# 会在前台启动一个程序，并进入shell
docker  run -it redis /bin/bash 
```



### 启动后台应用

```
docker run -d redis     # -d 后台启动程序
docker ps  				# 查看正在运行的程序
```



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

## 应用容器化

### 在原有的基础上修改

> root@ubuntu:~# `docker pull ubuntu:18.04`
> root@ubuntu:~# `docker run --name MyUbuntu01 -it ubuntu:18.04 bash`
> root@a1155be8b52a:/# `pwd `
> /
> root@a1155be8b52a:/# `cd ~`
> root@a1155be8b52a:~# `touch test1 test2`
> root@a1155be8b52a:~# `exit`
> exit
> root@ubuntu:~# `docker commit MyUbuntu01 ubuntu:18.04.test`
> sha256:d320d1aa2a787d9a0457f8575eddec3bbd1d656fcdbaeabd0c7c60e8c6ab67ab
>
> root@ubuntu:~# `docker images`
> REPOSITORY   TAG          IMAGE ID       CREATED          SIZE
> ubuntu       18.04.test   d320d1aa2a78   26 seconds ago   63.1MB
> tomcat       latest       fb5657adc892   8 months ago     680MB
> redis        latest       7614ae9453d1   8 months ago     113MB
> ubuntu       18.04        5a214d77f5d7   11 months ago    63.1MB
> centos       latest       5d0da3dc9764   11 months ago    231MB
> root@ubuntu:~#
>
> 

![image-20220830165739912](images/image-20220830165739912-16621174785901.png)



### Dockerfile 文件创建新镜像（重要）

完整的应用容器化过程主要分为以下几个步骤。

（1）编写应用代码。

（2）创建一个 Dockerfile，其中包括当前应用的描述、依赖以及该如何运行这个应用。

（3）对该 Dockerfile 执行 `docker image build` 命令。

（4）等待 Docker 将应用程序构建到 Docker 镜像中。

一旦应用容器化完成（即应用被打包为一个 Docker 镜像），就能以镜像的形式交付并以容器的方式运行了。

![image-20220902192047008](images/image-20220902192047008.png)

具体的过程：

（1）获取应用代码。

（2）分析 Dockerfile。

（3）构建应用镜像。

（4）运行该应用。

（5）测试应用。

（6）容器应用化细节。

（7）生产环境中的多阶段构建。

（8）最佳实践。

具体分析一下每一步的作用。

```
git clone https://github.com/nigelpoulton/psweb
```

除注释以外，每个 Dockerfile 文件第一行都是 `FROM` 指令。`FROM` 指令指定的镜像，会作为当前镜像的一个基础镜像层，当前应用的剩余内容会作为新增镜像层添加到基础镜像层之上。本例中的应用基于 Linux 操作系统，所以在 `FROM` 指令当中所引用的也是一个 Linux 基础镜像；如果要容器化的应用是一个基于 Windows 操作系统的应用，就需要指定一个像 `microsoft/aspnetcore-build` 这样的 Windows 基础镜像了。

截至目前，基础镜像的结构如下图所示：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191220-1576820793679)

接下来，Dockerfile 中通过标签（LABLE）方式指定了当前镜像的维护者为 “nigelpoulton@hotmail. com”。每个标签其实是一个键值对（Key-Value），在一个镜像当中可以通过增加标签的方式来为镜像添加自定义元数据。备注维护者信息有助于为该镜像的潜在使用者提供沟通途径，这是一种值得提倡的做法。

第 6 行 `RUN apk add --update nodejs nodejs-npm` 指令使用 `alpine` 的 `apk` 包管理器将 `nodejs` 和 `nodejs-npm` 安装到当前镜像之中。`RUN` 指令会在 `FROM` 指定的 `alpine` 基础镜像之上，新建一个镜像层来存储这些安装内容。当前镜像的结构如下图所示：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191220-1576821115455)

第 8 行 `COPY . /src` 指令将应用相关文件从构建上下文复制到了当前镜像中，并且新建一个镜像层来存储。`COPY`执行结束之后，当前镜像共包含 3 层，如下图所示：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191220-1576821200153)

下一步，Dockerfile 通过 `WORKDIR` 指令，为 Dockerfile 中尚未执行的指令设置工作目录。该目录与镜像相关，并且会作为元数据记录到镜像配置中，但不会创建新的镜像层。

然后，`RUN npm install` 指令会根据 `package.json` 中的配置信息，使用 `npm` 来安装当前应用的相关依赖包。`npm` 命令会在前文设置的工作目录中执行，并且在镜像中新建镜像层来保存相应的依赖文件。目前镜像一共包含 4 层，如下图所示：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191220-1576821472108)

因为当前应用需要通过 TCP 端口 8080 对外提供一个 Web 服务，所以在 Dockerfile 中通过 `EXPOSE 8080` 指令来完成相应端口的设置。这个配置信息会作为镜像的元数据被保存下来，并不会产生新的镜像层。

最终，通过 `ENTRYPOINT` 指令来指定当前镜像的入口程序。`ENTRYPOINT` 指定的配置信息也是通过镜像元数据的形式保存下来，而不是新增镜像层。

到目前为止，大家应该已经了解基本的原理和流程，接下来是时候尝试构建自己的镜像了！

在开始之前，我们需要对 `Dockerfile` 文件做个小调整。在安装 nodejs 命令的上一行新增下面命令，用来将 alpinelinux 调整为清华源，这样可以提高运行的速度。

```
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.tuna.tsinghua.edu.cn/g' /etc/apk/repositories
```

我们通过 `vim Dockerfile` 打开文件，按 `i` 进入编辑模式，将下面的内容按照截图的位置写入。然后按 ESC 退出编辑模式，按 `:wq` 保存退出。

![图片描述](https://dn-simplecloud.shiyanlou.com/questions/uid810810-20220621-1655802596253)

修改之后，可以通过 `nl Dockerfile` 查看是否修改成功。

![图片描述](https://dn-simplecloud.shiyanlou.com/questions/uid810810-20220621-1655803200104)

下面的命令会构建并生成一个名为 `web:latest` 的镜像。命令最后的点（.）表示 Docker 在进行构建的时候，使用当前目录作为构建上下文。

一定要在命令最后包含这个点，并且在执行命令前，要确认当前目录是 psweb ：

```bash
$ pwd
/home/shiyanlou/Code/psweb
$ ls -l | grep Docker
-rw-rw-r-- 1 shiyanlou shiyanlou  338 12月 20 13:29 Dockerfile
$ docker image build -t web:latest .
Sending build context to Docker daemon  74.75kB
Step 1/8 : FROM alpine
 ---> c85b8f829d1f
Step 2/8 : LABEL maintainer "nigelpoulton@hotmail.com"
 ---> Running in 57c5bb103a6d
 ---> a9a4cfc6ac1d
Removing intermediate container 57c5bb103a6d
Step 3/8 : RUN apk add --update nodejs nodejs-npm
 ---> Running in 5d0dae7a94b6

... ...

Step 8/8 : ENTRYPOINT node ./app.js
 ---> Running in dea66e8891d2
 ---> c156c5c8ddf8
Removing intermediate container dea66e8891d2
Successfully built c156c5c8ddf8
Successfully tagged web:latest
```

如上代码块所示，倒数第 2 行成功创建了 ID 为 c156 的镜像。执行 image ls 命令查看镜像库中的镜像列表：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191220-1576823191369)

恭喜，应用容器化已经成功了！

大家可以通过 `docker inspect web:latest` 来确认刚刚构建的镜像配置是否正确。这个命令会列出 Dockerfile 中设置的所有配置项。



#### Dockerfile 文件创建新镜像（重要）

```shell


root@ubuntu:~# cd docker/
root@ubuntu:~/docker# vim Dockerfile

root@ubuntu:~/docker# docker build .

root@ubuntu:~/docker# docker images
REPOSITORY   TAG          IMAGE ID       CREATED             SIZE
<none>       <none>       fff59ccc92b5   30 seconds ago      141MB

root@ubuntu:~/docker# docker tag fff59ccc92b5 my_nginx
root@ubuntu:~/docker# docker images
REPOSITORY   TAG          IMAGE ID       CREATED              SIZE
my_nginx     latest       fff59ccc92b5   About a minute ago   141MB

root@ubuntu:~/docker# cd ~
root@ubuntu:~# docker images
REPOSITORY   TAG          IMAGE ID       CREATED             SIZE
my_nginx     latest       fff59ccc92b5   2 minutes ago       141MB

root@ubuntu:~# docker run -d -p 80:80 --name nginx01 my_nginx
9ea4031cfedab1915c0772de2ca34e5171152cbea9e38c32f58971bd7a263ae6
```

也可以在创建完Dockerfile 后，回到上级目录

![image-20220830183232528](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220830183232528.png)

## 文件复制

### 容器内到容器外

新建一个redis 容器，并将容器内的一个文件复制到容器外

```shell
root@ubuntu:~# docker ps
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
root@ubuntu:~# ps -ef | grep redis
root       14462   11936  0 02:21 pts/1    00:00:00 grep --color=auto redis
root@ubuntu:~# docker run -d --name redis01 -p 6379:6379 redis
92ff7e5d57d229c0c5f8449c0749c29dbaecd72a0a36bdc872e44d62789295f8
root@ubuntu:~# ps -ef | grep redis
systemd+   14555   14533  1 02:21 ?        00:00:00 redis-server *:6379
root       14597   11936  0 02:21 pts/1    00:00:00 grep --color=auto redis
root@ubuntu:~# docker exec -it redis01 /bin/bash
root@92ff7e5d57d2:/data# ls
root@92ff7e5d57d2:/data# echo >>test.txt
root@92ff7e5d57d2:/data# ls
test.txt
root@92ff7e5d57d2:/data# exit
exit
root@ubuntu:~# docker cp redis01:/data/test.txt /root
root@ubuntu:~# pwd
/root
root@ubuntu:~# ls
a.txt  hello.py  snap  test.txt  text.txt
root@ubuntu:~# 
```

### 容器外到容器内

```shell
root@ubuntu:~# echo >>hello.java
root@ubuntu:~# docker cp  /root/hello.java  redis01:/data/
root@ubuntu:~# docker exec -it redis01 /bin/bash
root@92ff7e5d57d2:/data# ls
hello.java  test.txt
```

# Docker 引擎

Linux 上可用的存储引擎有 `AUFS`、`Overlay2`、`Device Mapper`、`Btrfs` 以及 `ZFS` 。

Docker 在 Windows 上仅支持 `windowsfilter` 一种存储引擎，该引擎基于 NTFS 文件系统之上实现了分层和 CoW 。

Docker 引擎由如下主要的组件构成：Docker 客户端（Docker Client）、Docker 守护进程（Docker daemon）、containerd 以及 runc 。它们共同负责容器的创建和运行。

![image-20220830184035125](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220830184035125.png)



# Docker 镜像仓库

镜像仓库分为官方和非官方的

官方

只需要给出镜像的名字和标签，就能在官方仓库中定位一个镜像（采用“:”分隔）

```
docker image pull ubuntu:18.04
```

非官方

非官方仓库拉取镜像也是类似的，读者只需要在仓库名称面前加上 Docker Hub 的用户名或者组织名称。

```shell
$ docker image pull nigelpoulton/tu-demo:v2
//该命令会从 nigelpoulton 账号为命名空间的 tu-demo 库中下载标签为 v2 的镜像
//其中 nigelpoulton 是在 Docker Hub 注册的账号的用户名
```



从第三方镜像仓库服务获取镜像（非 Docker Hub），则需要在镜像仓库名称前加上第三方镜像仓库服务的 DNS 名称。假设上面的示例中的镜像位于 Google 容器镜像仓库服务（GCR）中，则需要在仓库名称前面加上 `gcr.io` ，如 

`docker pull gcr.io/nigelpoulton/tu-demo:v2` 。

此外，可能需要操作者拥有第三方镜像仓库服务的账户，并在拉取镜像前完成登录。

`latest` 是一个非强制标签，不保证指向仓库中最新的镜像,如下图

![image-20220830191057686](images/image-20220830191057686.png)

```shell
docker images --format "{{.Repository}}: {{.Tag}}: {{.Size}}"
```

![image-20220830191550075](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220830191550075.png)





# Docker安装常用软件

## 开启防火墙端口

```bash
# 查看防火墙状态
firewall-cmd --state  
# 如果防火墙关闭，就开启，如果开启直接执行下一条命令
systemctl start firewalld.service
# 开启对应的端口
firewall-cmd --zone=public --add-port=9200/tcp --permanent
# 重启防火墙
systemctl restart firewalld.service
# 重新载入配置
firewall-cmd --reload

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
docker run --name nginx -p 80:80 -d nginx
# 进入容器
docker exec -it nginx /bin/bash
```

停止服务

> docker stop nginx01

## 安装redis

```shell
# 搜索
docker serch redis
# 2. 下载
docker pull redis 

# 3.运行
docker run -p 6379:6379 --name redis01 -v /usr/local/docker/redis.conf:/etc/redis/redis.conf -v /usr/local/docker/data:/data -d redis redis-server /etc/redis/redis.conf --appendonly yes
# 4.  进入容器
docker exec -it redis01 /bin/bash
# root@e4e5d5aabc12:/data# 
# 5. 开启客户端
redis-cli
127.0.0.1:6379> 
```

停止服务

> docker stop redis01

删除redis

> docker rm redis01

## mysql8.0 安装

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

> ```shell
> docker run --name mysql8  -p 3306:3306 -e MYSQL_ROOT_PASSWORD=666666 -d mysql:latest --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
> ```
>
> # 一条命令安装mysql 最新版本
>
> 删除 msyql
>
> > docker rm mysql8

## mysql 5.7

> docker run --name mysql5.7 -p 3307:3306 -e MYSQL_ROOT_PASSWORD=666666 -d mysql:5.7 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci

docker kill mysql8

docker start mysql8

停止服务

> docker stop mysql8.0

## 安装MONGDB 

### 安装命令

> docker run --name mongodb  -v /my/custom:/etc/mongo -p 27017:27017 -d mongo --auth

> docker exec -it mongodb mongo admin

![image-20220415144656652](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220415144656652.png)

创建用户

> use admin

> db.createUser({user:"root",pwd:"root",roles:[{role:"userAdminAnyDatabase",db:"admin"}]})

> exit

### 远程访问

> docker exec -it mongodb /bin/bash
>
> cd /etc
>
> cat mongod.conf.orig

复制mongod.conf.orig 中的内容

```
# mongod.conf

# for documentation of all options, see:
#   http://docs.mongodb.org/manual/reference/configuration-options/

# Where and how to store data.
storage:
  dbPath: /var/lib/mongodb
  journal:
    enabled: true
#  engine:
#  wiredTiger:

# where to write logging data.
systemLog:
  destination: file
  logAppend: true
  path: /var/log/mongodb/mongod.log

# network interfaces
net:
  port: 27017
  bindIp: 0.0.0.0


# how the process runs
processManagement:
  timeZoneInfo: /usr/share/zoneinfo

#security:

#operationProfiling:

#replication:

#sharding:

## Enterprise-Only Options:

#auditLog:

#snmp:

```



> exit

> vim mongod.conf.orig

粘贴内容

修改

`bindIP: 0.0.0.0`

![image-20220415152134275](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220415152134275.png)

复制修改后的内容到mongdb 容器

> docker cp ./mongod.conf.orig  mongodb:/etc/mongod.conf.orig

> docker restart mongodb

阿里ECS服务器开放安全组

![image-20220415152955338](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220415152955338.png)

本地系统

如果使用虚拟机则要开放防火墙

> firewall-cmd --permanent --add-port=27017/tcp
>
> firewall-cmd --reload

查看端口是否开放

> firewall-cmd --query-port=27017/tcp

Mongdb Compass 连接数据库

连接语法：

mongodb://username:paswd@host:port

![image-20220415160155291](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220415160155291.png)



## 安装Vim

>  apt-get update

> apt-get install vim





## 安装 ElasticSearch

> 
> 
> 

## docker 安装 RabbitMQ



> 

安装命令

> docker run -d --hostname my-rabbit --name rabbit -p 15672:15672 -p 5672:5672 rabbitmq



通过`docker ps -a`查看部署的mq容器id，在通过 `docker exec -it rabbit /bin/bash` 进入容器内部在
运行：`rabbitmq-plugins enable rabbitmq_management`

## Docker 安装rocketmq

> docker pull rocketmqinc/rocketmq
>


>
> mkdir -p /docker/rocketmq/data/namesrv/logs /docker/rocketmq/data/namesrv/store
>

>
> docker run -d --restart=always --name rmqnamesrv --privileged=true -p 9876:9876  -v /docker/rocketmq/data/namesrv/logs:/root/logs -v /docker/rocketmq/data/namesrv/store:/root/store -e "MAX_POSSIBLE_HEAP=100000000" rocketmqinc/rocketmq sh mqnamesrv

> mkdir -p /docker/rocketmq/conf

> vi /docker/rocketmq/conf/broker.conf 

```shell
# 所属集群名称，如果节点较多可以配置多个
brokerClusterName = DefaultCluster 
#broker名称，master和slave使用相同的名称，表明他们的主从关系 
brokerName = broker-a 
#0表示Master，大于0表示不同的
slave brokerId = 0 
#表示几点做消息删除动作，默认是凌晨4点 
deleteWhen = 04 
#在磁盘上保留消息的时长，单位是小时 
fileReservedTime = 48 
#有三个值：SYNC_MASTER，ASYNC_MASTER，SLAVE；同步和异步表示Master和Slave之间同步数据的机 制；
brokerRole = ASYNC_MASTER 
#刷盘策略，取值为：ASYNC_FLUSH，SYNC_FLUSH表示同步刷盘和异步刷盘；SYNC_FLUSH消息写入磁盘后 才返回成功状态，ASYNC_FLUSH不需要；
flushDiskType = ASYNC_FLUSH 
# 设置broker节点所在服务器的ip地址 
brokerIP1 = 192.168.247.128
#剩余磁盘比例 
diskMaxUsedSpaceRatio=99

```

```shell
docker run -d --restart=always --name rmqbroker --link rmqnamesrv:namesrv -p 10911:10911 -p 10909:10909 --privileged=true -v /docker/rocketmq/data/broker/logs:/root/logs -v /docker/rocketmq/data/broker/store:/root/store -v /docker/rocketmq/conf/broker.conf:/opt/rocketmq-4.4.0/conf/broker.conf -e "NAMESRV_ADDR=namesrv:9876" -e "MAX_POSSIBLE_HEAP=200000000" rocketmqinc/rocketmq sh mqbroker -c /opt/rocketmq-4.4.0/conf/broker.conf

```

```java
docker pull pangliang/rocketmq-console-ng
```



记得修改Ip 地址

```shell
docker run -d --restart=always --name rmqadmin -e "JAVA_OPTS=-Drocketmq.namesrv.addr=192.168.247.128:9876 -Dcom.rocketmq.sendMessageWithVIPChannel=false" -p 8080:8080 pangliang/rocketmq-console-ng

```

#关闭防火墙 

> systemctl stop firewalld.service 
> #禁止开机启动 
> systemctl disable firewalld.service



RocketMQ提供了UI管理工具，名为rocketmq-console，我们选择docker安装

通过浏览器进行访问：[http://192.168.247.128:8080/](http://192.168.247.128:8080/)



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

# 容器数据卷

什么式容器数据卷

docker 的理念回顾

将应用和换将打包成一个镜像。

数据？ 如果数据都在容器中。如果容器删除，数据就会丢失！

容器之间有一个数据共享技术。

容器的持久化，和数据共享。

实战操作 使用centos

>```shell
>[root@root ~]# docker run -it -v /root:/home/test centos
>[root@root ~]# docker ps
>CONTAINER ID   IMAGE     COMMAND       CREATED          STATUS          PORTS     NAMES
>27d086e155a0   centos    "/bin/bash"   20 minutes ago   Up 20 minutes             pensive_galois
>[root@root ~]# docker inspect 27d086e155a0
>## 查询结果多了这条信息说明挂在成功。
> "HostConfig": {
>            "Binds": [
>                "/root:/home/test"
>            ],
>
> "Mounts": [
>            {
>                "Type": "bind",
>                "Source": "/root",
>                "Destination": "/home/test",
>                "Mode": "",
>                "RW": true,
>                "Propagation": "rprivate"
>            }
>        ],
>```

扩展：

指定容器内只读

```
-v /宿主机路径:容器内路径`ro`   # 容器只读，只能通过宿主机操作，容器无法操作
-v /宿主机路径：容器内路径：rw   # 默认模式
```

## 数据卷的相关命令

![image-20220904231131588](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220904231131588.png)

```
# 创建卷
docker volume create [卷名]
# 查看docker卷
docker volume ls
# 查看卷的详细信息
docker volume inspect [卷名]

# 删除卷  会删除未装入到某个容器或者服务的**所有卷**，所以**谨慎使用**
docker volume prune
# 删除指定卷
docker volume rm  [卷名]
```

```shell
root@ubuntu:~# docker volume create V1
V1
root@ubuntu:~# docker volume ls
DRIVER    VOLUME NAME
local     V1
root@ubuntu:~# ocker volume inspect V1
[
    {
        "CreatedAt": "2022-09-04T06:22:34-07:00",
        "Driver": "local",
        "Labels": {},
        "Mountpoint": "/var/lib/docker/volumes/V1/_data",
        "Name": "V1",
        "Options": {},
        "Scope": "local"
    }
]
root@ubuntu:~# docker volume prune
WARNING! This will remove all local volumes not used by at least one container.
Are you sure you want to continue? [y/N] y
Deleted Volumes:
V1

Total reclaimed space: 0B
root@ubuntu:~# 
```

### 匿名挂载

```
-v 容器内路径
```

```
docker run -d -it  --name centos1 centos
```



### 具名挂载

```
-V 卷名:容器内路径
```

```
docker run -d -it -v V2:/tmp/parent --name centos2 centos
docker attach centos2
cd /tmp/parent/
touch a.txt
```

![image-20220904231902756](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220904231902756.png)

### 指定路径挂载

```
-v /宿主机路径:容器内路径
```

 `--privileged=true`解决没有操作文件权限问题

示例

```
docker run -it --privileged=true -v /tmp/host_data:/tmp/docker_data --name u1 ubuntu
```

示例二 

```shell
docker run -p 6379:6379 --name redis01 -v /usr/local/docker/redis.conf:/etc/redis/redis.conf -v /usr/local/docker/data:/data -d redis redis-server /etc/redis/redis.conf --appendonly yes
```






## 数据卷容器

命名的容器挂载数据卷，其他的容器通过挂载这个父容器实现数据共享，挂载数据卷的容器，我们称为数据卷容器。

````
docker run -d -it --name -v /docker_data/:/tmp/parent parentContainer [镜像名]（可以填上面通过dockerFile建立的镜像，里面有挂载容器卷）
````

```
docker run -d -it --name sonContainer1 --volumes-from parentContainer [镜像名]

docker run -d -it --name sonContainer2 --volumes-from parentContainer [镜像名]
```

这样2个子容器和父容器数据共享。

```
docker run -d -it -v /docker_data/:/tmp/parent --name parentContainer centos
docker run -d -it --name sonContainer1 --volumes-from parentContainer  centos
docker run -d -it --name sonContainer2 --volumes-from parentContainer  centos

docker exec -it parentContainer /bin/bash
docker exec -it sonContainer1 /bin/bash
docker exec -it sonContainer2 /bin/bash

# 进入父容器创建文件
docker attach parentContainer
cd /tmp/parent
# 进入子容器创建文件，并查看
# docker attach sonContainer1
cd /tmp/parent

# 进入子容器创建文件，并查看
# docker attach sonContainer2
cd /tmp/parent
```

结论实现了容器的文件共享



# 安装Docker-compose

## 1 国内镜像秒下载

https://get.daocloud.io/#install-compose

```
curl -L https://get.daocloud.io/docker/compose/releases/download/v2.10.2/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose

```

- 如果遇到版本更新，把版本数字更改一下就可以了。

## 2 修改文件执行权限

```
chmod +x /usr/local/bin/docker-compose
```

## 3 测试是否安装成功

```bash
docker-compose --version
cker-compose version 1.24.1, build 4667896b
```



初体验

https://docs.docker.com/compose/gettingstarted/

# Docker 保存镜像



```bash
# 镜像打包
docker save -o es8.0.0.tar docker.elastic.co/elasticsearch/elasticsearch
# 镜像解压
docker load --input es8.0.0.tar
```

