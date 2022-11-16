# Docker 网络

## 课程简介

Docker 在容器内部运行应用，这些应用之间的交互依赖于大量不同的网络，这意味着 Docker 需要强大的网络功能。

幸运的是，Docker 对于容器之间、容器与外部网络和 VLAN 之间的连接均有相应的解决方案。后者对于那些需要跟外部系统（如虚拟机和物理机）的服务打交道的容器化应用来说至关重要。

Docker 网络架构源自一种叫作容器网络模型（CNM）的方案，该方案是开源的并且支持插接式连接。Libnetwork 是 Docker 对 CNM 的一种实现，提供了 Docker 核心网络架构的全部功能。不同的驱动可以通过插拔的方式接入 Libnetwork 来提供定制化的网络拓扑。

为了实现开箱即用的效果，Docker 封装了一系列本地驱动，覆盖了大部分常见的网络需求。其中包括单机桥接网络（Single-Host Bridge Network）、多机覆盖网络（Multi-Host Overlay），并且支持接入现有 VLAN。Docker 生态系统中的合作伙伴通过提供驱动的方式，进一步拓展了 Docker 的网络功能。

最后要说的是，Libnetwork 提供了本地服务发现和基础的容器负载均衡解决方案。

对于 `Docker` 的镜像仓库来说，国内访问速度较慢，我们添加一个阿里云提供的 `Docker` 镜像加速器。

首先，我们需要编辑 `/etc/docker/daemon.json` 文件：

```bash
$ sudo vim /etc/docker/daemon.json
```

然后加入如下内容：

```bash
{
  "registry-mirrors": ["https://n6syp70m.mirror.aliyuncs.com"]
}
```

修改之后，需要重启 `docker` 服务，让修改生效。使用如下命令：

```bash
$ sudo service docker restart
```

#### 知识点

- Docker 网络理论基础
- Libnetwork 和驱动
- 单机桥接网络
- 多机覆盖网络
- 服务发现
- Ingress 网络

## 基础理论

在顶层设计中，Docker 网络架构由 3 个主要部分构成：CNM、Libnetwork 和驱动。

CNM 是设计标准。在 CNM 中，规定了 Docker 网络架构的基础组成要素。

Libnetwork 是 CNM 的具体实现，并且被 Docker 采用。Libnetwork 通过 Go 语言编写，并实现了 CNM 中列举的核心组件。

驱动通过实现特定网络拓扑的方式来拓展该模型的能力。

下图展示了顶层设计中的每个部分是如何组装在一起的：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577429301627)

接下来具体介绍每部分的细节。

#### CNM

一切都始于设计！

Docker 网络架构的设计规范是 CNM。CNM 中规定了 Docker 网络的基础组成要素，完整内容见 GitHub 的 docker/libnetwork 库。

推荐通篇阅读该规范，不过其实抽象来讲，CNM 定义了 3 个基本要素：沙盒（Sandbox）、终端（Endpoint）和网络（Network）。

**沙盒**是一个独立的网络栈。其中包括以太网接口、端口、路由表以及 DNS 配置。

**终端**就是虚拟网络接口。就像普通网络接口一样，终端主要职责是负责创建连接。在 CNM 中，终端负责将沙盒连接到网络。

**网络**是 802.1d 网桥（类似大家熟知的交换机）的软件实现。因此，网络就是需要交互的终端的集合，并且终端之间相互独立。

下图展示了 3 个组件是如何连接的：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577429485802)

Docker 环境中最小的调度单位就是容器，而 CNM 也恰如其名，负责为容器提供网络功能。下图展示了 CNM 组件是如何与容器进行关联的——沙盒被放置在容器内部，为容器提供网络连接：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577429562203)

容器 A 只有一个接口（终端）并连接到了网络 A。容器 B 有两个接口（终端）并且分别接入了网络 A 和网络 B。容器 A 与 B 之间是可以相互通信的，因为都接入了网络 A。但是，如果没有三层路由器的支持，容器 B 的两个终端之间是不能进行通信的。

需要重点理解的是，终端与常见的网络适配器类似，这意味着终端只能接入某一个网络。因此，如果容器需要接入到多个网络，就需要多个终端。

下图对前面的内容进行拓展，加上了 Docker 主机。虽然容器 A 和容器 B 运行在同一个主机上，但其网络堆栈在操作系统层面是互相独立的，这一点由沙盒机制保证：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577429681459)

#### Libnetwork

CNM 是设计规范文档，Libnetwork 是标准的实现。Libnetwork 是开源的，采用 Go 语言编写，它跨平台（Linux 以及 Windows），并且被 Docker 所使用。

在 Docker 早期阶段，网络部分代码都存在于 daemon 当中。这简直就是噩梦——daemon 变得臃肿，并且不符合 UNIX 工具模块化设计原则，即既能独立工作又易于集成到其它项目。所以，Docker 将该网络部分从 daemon 中拆分，并重构为一个叫作 Libnetwork 的外部类库。现在，Docker 核心网络架构代码都在 Libnetwork 当中。

正如读者期望，Libnetwork 实现了 CNM 中定义的全部 3 个组件。此外它还实现了本地服务发现（Service Discovery）、基于 Ingress 的容器负载均衡，以及网络控制层和管理层功能。

#### 驱动

如果说 Libnetwork 实现了控制层和管理层功能，那么驱动就负责实现数据层。比如，网络连通性和隔离性是由驱动来处理的，驱动层实际创建网络对象也是如此，其关系如下图所示：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577429935198)

Docker 封装了若干内置驱动，通常被称作原生驱动或者本地驱动。在 Linux 上包括 `Bridge`、`Overlay` 以及`Macvlan` ，在 Windows 上包括 `NAT、Overlay`、`Transport` 以及 `L2 Bridge` 。接下来的一节中会介绍如何使用其中部分驱动。

第三方也可以编写 Docker 网络驱动。这些驱动叫作远程驱动，例如 `Calico`、`Contiv`、`Kuryr` 以及 `Weave` 。

每个驱动都负责其上所有网络资源的创建和管理。举例说明，一个叫作“prod-fe-cuda”的覆盖网络由 `Overlay` 驱动所有并管理。这意味着 `Overlay` 驱动会在创建、管理和删除其上网络资源的时候被调用。

为了满足复杂且不固定的环境需求，Libnetwork 支持同时激活多个网络驱动。这意味着 Docker 环境可以支持一个庞大的异构网络。



## 单机桥接网络

最简单的 Docker 网络就是单机桥接网络。

从名称中可以看到两点。

- **单机**意味着该网络只能在单个 Docker 主机上运行，并且只能与所在 Docker 主机上的容器进行连接。
- **桥接**意味着这是 802.1.d 桥接的一种实现（二层交换机）。

Linux Docker 创建单机桥接网络采用内置的桥接驱动，而 Windows Docker 创建时使用内置的 NAT 驱动。实际上，这两种驱动工作起来毫无差异。

下图展示了两个均包含相同本地桥接网络 mynet 的 Docker 主机。虽然网络是相同的，但却是两个独立的网络。这意味着图中容器无法直接进行通信，因为并不在一个网络当中：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577430162159)

每个 Docker 主机都有一个默认的单机桥接网络。在 Linux 上网络名称为 `bridge` ，在 Windows 上叫作 `nat` 。除非读者通过命令行创建容器时指定参数 `--network` ，否则默认情况下，新创建的容器都会连接到该网络。

下面列出了 `docker network ls` 命令在刚完成安装的 Docker 主机上的输出内容。输出内容做了截取处理，只展示了每个主机上的默认网络。注意，网络的名称和创建时使用的驱动名称是一致的——这只是个巧合：

```bash
//Linux
$ docker network ls
NETWORK ID         NAME       DRIVER     SCOPE
333e184cd343       bridge     bridge     local

//Windows
> docker network ls
NETWORK ID         NAME       DRIVER     SCOPE
095d4090fa32       nat        nat        local
```

在实验环境中操作的截图如下：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577430652863)

`docker network inspect` 命令就是一个信息宝藏。如果读者对底层细节的内容感兴趣，强烈推荐仔细阅读该命令的输出内容：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577430839240)

此处推荐大家安装一个查看 JSON 数据的精品小工具 jq ，执行 `sudo apt install -y jq` 安装，然后执行如下命令即可实现语法高亮显示信息：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577431655228)

在 Linux 主机上，Docker 网络由 Bridge 驱动创建，而 Bridge 底层是基于 Linux 内核中久经考验达 15 年之久的 Linux Bridge 技术。这意味着 Bridge 是高性能并且非常稳定的！同时这还表示可以通过标准的 Linux 工具来查看这些网络，代码如下：

```bash
$ ip link show docker0
3: docker0: <NO-CARRIER,BROADCAST,MULTICAST,UP> mtu 1500 qdisc noqueue state DOWN mode DEFAULT group default
    link/ether 02:42:1b:d9:9f:3e brd ff:ff:ff:ff:ff:ff
```

在 Linux Docker 主机之上，默认的 bridge 网络被映射到内核中为 **docker0** 的 Linux 网桥。可以通过 `docker network inspect` 命令观察到 Options 字段的输出内容：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577432251736)

Docker 默认 bridge 网络和 Linux 内核中的 docker0 网桥之间的关系如下图所示：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577430454108)

此图对上面的图片的内容进行了扩展，在顶部补充了接入 bridge 网络的容器。bridge 网络在主机内核中映射到名为 docker0 的 Linux 网桥，该网桥可以通过主机以太网接口的端口映射进行反向关联。

接下来使用 `docker network create` 命令创建新的单机桥接网络，名为 “localnet” ：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577433364816)

新的网络创建成功，并且会出现在 `docker network ls` 命名的输出内容当中。如果读者使用 Linux，那么在主机内核中还会创建一个新的 Linux 网桥。

接下来通过使用 Linux `brctl` 工具来查看系统中的 Linux 网桥。读者可能需要通过命令 `apt-get install bridge-utils` 来安装 `brctl` 二进制包，或者根据所使用的 Linux 发行版选择合适的命令：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577433723246)

输出内容中包含了两个网桥。最下面的是前文提过的 `docker0` 网桥，该网桥对应 Docker 中的默认网络 `bridge` ；第一个网桥 br-9075d 与新建的 “localnet” Docker 桥接网络相对应，这里的 bridge name 即为 localnet 的 NETWORK ID 前面加上 br- 字样。两个网桥目前都没有开启 STP（一种网络通信协议），并且也都没有任何设备接入，对应的 `interfaces` 列为空。

目前，主机上的网桥配置如下图所示：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577434799298)

接下来创建一个新的容器，并接入到新建桥接网络 `localnet` 当中：

```bash
$ docker container run -d --name c1 \
  --network localnet \
  alpine sleep 1d
```

容器现在接入了 `localnet` 网络当中。读者可以通过 `docker network inspect` 命令来确认：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577435117343)

输出内容表明 `c1` 容器已经位于桥接（Bridge/NAT）网络 `localnet` 之上。

如果再次运行 `brctl show` 命令，就能看到 `c1` 的网络接口连接到了 `br-20c2e8ae4bbb` 网桥：

```bash
$ brctl show
bridge name        bridge id           STP enabled    interfaces
br-20c2e8ae4bbb    8000.02429636237c   no             vethe792ac0
docker0            8000.0242aff9eb4f   no
```

下图展示了上述关系：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577437442156)

如果在相同网络中继续接入新的容器，那么在新接入容器中是可以通过 c1 的容器名称来 ping 通的。这是因为新容器都注册到了指定的 Docker DNS 服务，所以相同网络中的容器可以解析其他容器的名称。

注：

> Linux 上默认的 Bridge 网络是不支持通过 Docker DNS 服务进行域名解析的。自定义桥接网络可以！

一起来测试一下：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577438271314)

命令生效了！这是因为 c2 容器运行了一个本地 DNS 解析器，该解析器将请求转发到了 Docker 内部 DNS 服务器当中。DNS 服务器中记录了容器启动时通过 `--name` 或者 `--net-alias` 参数指定的名称与容器之间的映射关系。

如果读者仍处于容器中，可以尝试运行一些网络相关的命令。这是一种很好的了解 Docker 容器网络工作原理的方式。下图所示为 c2 容器中执行 ifconfig 命令的截图：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577438504479)

也可以使用 `docker network inspect localnet` 命令查看到 c2 的 IP 地址的 MAC 地址：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577438711907)

到目前为止，我们提到的桥接网络中的容器只能与位于相同网络中的容器进行通信。但是，可以使用端口映射（Port Mapping）来绕开这个限制。

端口映射允许将某个容器端口映射到 Docker 主机端口上。对于配置中指定的 Docker 主机端口，任何发送到该端口的流量，都会被转发到容器。下图展示了具体流量动向：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577438871548)

如上图所示的容器内部应用开放端口为 80。该端口被映射到了 Docker 主机的 `10.0.0.15` 接口的 5000 端口之上。最终结果就是访问 `10.0.0.15:5000` 的所有流量都被转发到了容器的 80 端口。

接下来通过示例了解将容器上运行着 Web 服务的端口 80，映射到 Docker 主机端口 5000 的过程。示例使用 Linux 的 Nginx。如果使用 Windows，可以将 Nginx 替换为某个 Windows 的 Web 服务镜像。

（1）运行一个新的 Web 服务容器，并将容器 80 端口映射到 Docker 主机的 5000 端口：

```bash
$ docker container run -d --name web \
  --network localnet \
  --publish 5000:80 \
  nginx
```

（2）确认端口映射。

```bash
$ docker port web
80/tcp -> 0.0.0.0:5000
```

这表示容器 80 端口已经映射到 Docker 主机所有接口上的 5000 端口。

前两步操作截图如下：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577439314729)

（3）通过 Web 浏览器访问 Docker 主机 5000 端口，验证配置是否生效。为了完成测试，读者需要知道 Docker 主机的 IP 地址或者 DNS 名称。如果读者使用 Windows 版 Docker 或者 Mac 版 Docker，可以使用 `0.0.0.0` 或者 `127.0.0.1` ：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577439354771)

外部系统现在可以通过 Docker 主机的 TCP 端口 5000，来访问运行在桥接网络上的 Nginx 容器了。

端口映射工作原理大致如此，但这种方式比较笨重并且不能扩展。举个例子，在只有单一容器的情况下，它可以绑定到主机的任意端口。这意味着其他容器就不能再使用已经被 Nginx 容器占用的 5000 端口了。这也是单机桥接网络只适用于本地开发环境以及非常小的应用的原因。

## 多机覆盖网络

后面的实验会有专门的章节去介绍多机覆盖网络，所以本节内容尽量做到简洁。

覆盖网络适用于多机环境。它允许单个网络包含多个主机，这样不同主机上的容器间就可以在链路层实现通信。覆盖网络是理想的容器间通信方式，支持完全容器化的应用，并且具备良好的伸缩性。

Docker 为覆盖网络提供了本地驱动。这使得创建覆盖网络非常简单，只需要在 `docker network create` 命令中添加 `--d overlay` 参数。

## 接入现有网络

能够将容器化应用连接到外部系统以及物理网络的能力是非常必要的。常见的例子是部分容器化的应用——应用中已容器化的部分需要与那些运行在物理网络和 VLAN 上的未容器化部分进行通信。

Docker 内置的 `Macvlan` 驱动（Windows 上是 `Transparent`）就是为此场景而生。通过为容器提供 MAC 和 IP 地址，让容器在物理网络上成为“一等公民”。如下图所示：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577439619922)

Macvlan 的优点是性能优异，因为无须端口映射或者额外桥接，可以直接通过主机接口（或者子接口）访问容器接口。但是，Macvlan 的缺点是需要将主机网卡（NIC）设置为**混杂模式（Promiscuous Mode）**，这在大部分公有云平台上是不允许的。所以 Macvlan 对于公司内部的数据中心网络来说很棒（假设公司网络组能接受 NIC 设置为混杂模式），但是 Macvlan 在公有云上并不可行。

接下来通过图片和一个假想场景加深对 Macvlan 的理解。

假设读者有一个物理网络，其上配置了两个 VLAN——VLAN 100：10.0.0.0/24 和 VLAN 200：192.168.3.0/24，如下图所示：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577439703224)

接下来，添加一个 Docker 主机并连接到该网络，如下图所示：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577439777127)

有一个需求是将容器接入 VLAN 100。为了实现该需求，首先使用 `Macvlan` 驱动创建新的 Docker 网络。但是，`Macvlan` 驱动在连接到目标网络前，需要设置几个参数。比如以下几点。

- 子网信息。
- 网关。
- 可分配给容器的 IP 范围。
- 主机使用的接口或者子接口。

下面的命令会创建一个名为 `macvlan100` 的 Macvlan 网络，该网络会连接到 VLAN 100 ，注意该命令无需在实验环境中执行：

```bash
$ docker network create -d macvlan \
  --subnet=10.0.0.0/24 \
  --ip-range=10.0.00/25 \
  --gateway=10.0.0.1 \
  -o parent=eth0.100 \
  macvlan100
```

该命令会创建 `macvlan100` 网络以及 `eth0.100` 子接口。当前配置如下图所示：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577439877447)

Macvlan 采用标准 Linux 子接口，读者需要为其打上目标 VLAN 网络对应的 ID。在本例中目标网络是 VLAN 100，所以将子接口标记为 `.100`（`etho.100`）。

通过 `--ip-range` 参数告知 Macvlan 网络在子网中有哪些 IP 地址可以分配给容器。这些地址必须被保留，不能用于其他节点或者 DHCP 服务器，因为没有任何管理层功能来检查 IP 区域重合的问题。

`macvlan100` 网络已为容器准备就绪，执行以下命令将容器部署到该网络中，注意该命令无需在实验环境中执行：

```bash
$ docker container run -d --name mactainer1 \
  --network macvlan100 \
  alpine sleep 1d
```

当前配置如图 11.17 所示。但是切记，下层网络（`VLAN 100`）对 Macvlan 的魔法毫不知情，只能看到容器的 MAC 和 IP 地址。在该基础之上，`mactainer1` 容器可以 ping 通任何加入 VLAN 100 的系统，并进行通信。

注：

> 如果上述命令不能执行，可能是因为主机 NIC 不支持混杂模式。切记公有云平台不允许混杂模式。

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577440004514)

目前已经拥有了 Macvlan 网络，并有一台容器通过 Macvlan 接入了现有的 VLAN 当中。但是，这并不是结束。Docker Macvlan 驱动基于稳定可靠的同名 Linux 内核驱动构建而成。因此，Macvlan 也支持 VLAN 的 Trunk 功能。这意味着可以在相同的 Docker 主机上创建多个 Macvlan 网络，并且将容器按照下图的方式连接起来：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577440073999)

以上内容基本能涵盖 Macvlan。Windows 也提供了类似的解决方案 `Transparent` 驱动。



## 用于故障排除的容器和服务日志

在讲服务发现之前，快速了解一下网络连接故障排除相关的内容。

当认为遇到容器间网络连接问题时，检查 daemon 日志以及容器日志（应用日志）是非常有必要的。

在 Windows 上，daemon 日志存储在`∼AppData\Local\Docker`，可以通过 Windows 事件查看器来浏览。在 Linux 上，daemon 日志的存储位置取决于当前系统正在使用的初始化方式。如果是`Systemd`，日志会存储在`Journald`，并且可以通过`journalctl -u docker.service`命令查看；如果不是`Systemd`读者需要查看如下位置。

- Ubuntu 系统：`upstart:/var/log/upstart/docker.log`。
- RHEL 系列：`systems:/var/log/messages`。
- Debian：`/var/log/daemon.log`。
- Mac 版 Docker：`∼/Library/Containers/com.docker.docker/Data/com.` `docker.driver.amd64-linux/console-ring`。

读者还可以设置 daemon 日志的详细程度。可以通过编辑 daemon 配置文件（daemon.json），将 debug 设置为 true，并同时设置 log-level 为下面的某个值。

- `debug`：最详细的日志级别。
- `info`：默认值，次详细日志级别。
- `warn`：第三详细日志级别。
- `error`：第四详细日志级别。
- `fatal`：最粗略的日志级别。

下面的片段摘自`daemon.json`，其中开启了调试模式，并设置日志级别为`debug`。该配置在所有 Docker 平台均有效：

```json
{
  <Snip>
  "debug":true,
  "log-level":"debug",
  <Snip>
}
```

修改配置文件之后，需要重启 Docker 才会生效。

这就是 daemon 日志了。容器日志又是什么？

可以通过`docker container logs`命令查看单独的容器日志，通过`docker service logs`可以查看 Swarm 服务日志。但是，Docker 支持多种日志驱动，并不是每种都能通过`docker logs`命令查看的。

就像引擎日志的驱动和配置一样，每个 Docker 主机也为容器提供了默认的日志驱动以及配置。其中包括`json-file`（默认）、`journald`（只在运行`systemd`的 Linux 主机中生效）、`syslog`、`splunk`和`gelf`。

`json-file`和`journald`可能是较容易配置的，并且均可通过`doker logs`和`docker service logs`命令查看。具体命令格式为`docker logs 和 docker service logs`。

如果采用了其他日志驱动，可以通过第三方平台提供的原生工具进行查看。

下面的片段为`daemon.json`文件的一部分，展示如何配置 Docker 主机使用`syslog`方式。

```json
{
  "log-driver": "syslog"
}
```

读者可以为某个容器或者服务配置单独的日志策略，只需在启动的时候通过`--log- driver`和`--log-opts`指定特定的日志驱动即可。这样会覆盖掉`daemon.json`中的配置。

容器日志生效的前提是应用进程在容器内部 PID 为 1，并且将正常日志输出到`STDOUT`，将异常日志输出到`STDERR`。日志驱动就会将这些“日志”转发到日志驱动配置指定的位置。

如果应用日志是写到某个文件的，可以利用符号链接将日志文件重定向到`STDOUT`和`STDERR`。

下图所示为我们之前运行的 c2 容器的日志信息：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577440875100)

通常是很有可能在 daemon 日志或者容器日志中找到网络连接相关异常的。

## 服务发现

作为核心网络架构，Libnetwork 还提供了一些重要的网络服务。

服务发现 `Service Discovery` 允许容器和 Swarm 服务通过名称互相定位。唯一的要求就是需要处于同一个网络当中。

其底层实现是利用了 Docker 内置的 DNS 服务器，为每个容器提供 DNS 解析功能。下图展示了容器“c1”通过名称 ping 容器“c2”的过程。Swarm 服务原理相同。

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577440975286)

下面逐步分析整个过程。

（1）`ping c2`命令调用本地 DNS 解析器，尝试将“c2”解析为具体 IP 地址。每个 Docker 容器都有本地 DNS 解析器。

（2）如果本地解析器在本地缓存中没有找到“c2”对应的 IP 地址，本地解析器会向 Docker DNS 服务器发起一个递归查询。本地服务解析器是预先配置好并知道 Docker DNS 服务器细节的。

（3）Docker DNS 服务器记录了全部容器名称和 IP 地址的映射关系，其中容器名称是容器在创建时通过`--name`或者`--net-alias`参数设置的。这意味着 Docker DNS 服务器知道容器“c2”的 IP 地址。

（4）DNS 服务返回“c2”对应的 IP 地址到“c1”本地 DNS 解析器。之所以会这样是因为两个容器位于相同的网络当中，如果所处网络不同则该命令不可行。

（5）ping 命令被发往“c2”对应的 IP 地址。

每个启动时使用了`--name`参数的 Swarm 服务或者独立的容器，都会将自己的名称和 IP 地址注册到 Docker DNS 服务。这意味着容器和服务副本可以通过 Docker DNS 服务互相发现。

但是，服务发现是受网络限制的。这意味着名称解析只对位于同一网络中的容器和服务生效。如果两个容器在不同的网络，那么就不能互相解析。

关于服务发现和名称解析最后要说一点。

用户可以为 Swarm 服务和独立容器进行自定义的 DNS 配置。举个例子，`--dns`参数允许读者指定自定义的 DNS 服务列表，以防出现内置的 Docker DNS 服务器解析失败的情况。此外也可以使用`--dns-search`参数指定自定义查询时所使用的域名（例如当查询名称并非完整域名的时候）。

在 Linux 上，上述工作都是通过在容器内部`/etc/resolve.conf`文件内部增加条目来实现的。

下面的例子会启动一个新的容器，并添加声名狼藉的`8.8.8.8` Google DNS 服务器，同时指定`dockercents.com`作为域名添加到非完整查询当中。

```bash
$ docker container run -it --name c1 \
  --dns=8.8.8.8 \
  --dns-search=dockercerts.com \
  alpine sh
```



## Ingress 网络

Swarm 支持两种服务发布模式，两种模式均保证服务从集群外可访问。

- Ingress 模式（默认）。
- Host 模式。

通过 Ingress 模式发布的服务，可以保证从 Swarm 集群内任一节点（即使**没有**运行服务的副本）都能访问该服务；以 Host 模式发布的服务只能通过运行服务副本的节点来访问。图 11.20 展示了两种模式的区别。

Ingress 模式是默认方式，这意味着任何时候通过`-p`或者`--publish`发布服务的时候，默认都是 Ingress 模式；如果需要以 Host 模式发布服务，则读者需要使用`--publish`参数的完整格式，并添加`mode=host`。下面一起来看 Host 模式的例子：

```bash
$ docker service create -d --name svc1 \
  --publish published=5000,target=80,mode=host \
  nginx
```

关于该命令的一些说明。`docker service mode`允许读者使用完整格式语法或者简单格式语法来发布服务。简单格式如`-p 5000:80`，前面已经多次出现。但是，读者不能使用简单格式发布 Host 模式下的服务。

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577441258849)

完整格式如`--publish published=5000,target=80,mode=host`。该方式采用逗号分隔多个参数，并且逗号前后不允许有空格。具体选项说明如下。

- `published=5000` 表示服务通过端口 5000 提供外部服务。
- `target=80`表示发送到 published 端口 5000 的请求，会映射到服务副本的 80 端口之上。
- `mode=host`表示只有外部请求发送到运行了服务副本的节点才可以访问该服务。

通常使用 Ingress 模式。

在底层，Ingress 模式采用名为 Service Mesh 或者 Swarm Mode Service Mesh 的四层路由网络来实现。下图展示了 Ingress 模式下一个外部请求是如何流转，最终访问到服务的。

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577441350007)

简要介绍上图的内容。

- 图中最上方命令部署了一个名为“svc1”的 Swarm 服务。该服务连接到了`overnet`网络，并发布到 5000 端口。
- 按上述方式发布 Swarm 服务（`--publish published=5000,target=80`）会在 Ingress 网络的 5000 端口进行发布。因为 Swarm 全部节点都接入了 Ingress 网络，所以这个端口被发布到了 Swarm 范围内。
- 集群确保到达 Ingress 网络中**任意节点**的 5000 端口的流量，都会被路由到 80 端口的“svc1”服务。
- 当前“svc1”服务只部署了一个副本，集群中有一条映射规则：“所有访问 Ingress 网络 5000 端口的流量都需要路由到运行了“svc1”服务副本的节点之上”。
- 红线展示了访问 Node 的 5000 端口的流量，通过 Ingress 网络，被路由到了 Node2 节点正在运行的服务副本之上。

入站流量可能访问 4 个 Swarm 节点中的任意一个，但是结果都是一样的，了解这一点很重要。这是因为服务通过 Ingress 网络实现了 Swarm 范围内的发布。

此外，还有一点很重要：如果存在多个运行中的副本，流量会平均到每个副本之上，如下图中展示的一样。

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191227-1577441454781)



## 总结

Docker 网络有自己的子命令，主要包括以下几种：

- `docker network ls`用于列出运行在本地 Docker 主机上的全部网络。
- `docker network create`创建新的 Docker 网络。默认情况下，在 Windows 上会采用`NAT`驱动，在 Linux 上会采用`Bridge`驱动。读者可以使用`-d`参数指定驱动（网络类型）。`docker network create -d overlay overnet`会创建一个新的名为 overnet 的覆盖网络，其采用的驱动为`Docker Overlay`。
- `docker network inspect`提供 Docker 网络的详细配置信息。
- `docker network prune`删除 Docker 主机上全部未使用的网络。
- `docker network rm`删除 Docker 主机上指定网络。

容器网络模型（CNM）是 Docker 网络架构的主要设计文档，它定义了 Docker 网络中用到的 3 个主要结构——沙盒、终端以及网络。

Libnetwork 是开源库，采用 Go 编写，实现了 CNM。Docker 使用了该库，并且 Docker 网络架构的核心代码都在该库当中。Libnetwork 同时还提供了 Docker 网络控制层和管理层的功能。

驱动通过实现特定网络类型的方式扩展了 Docker 网络栈（Libnetwork），例如桥接网络和覆盖网络。Docker 内置了几种网络驱动，同时也支持第三方驱动。

单机桥接网络是基本的 Docker 网络类型，对于本地开发和小型应用来说也十分适用。单机桥接网络不可扩展，并且对外发布服务依赖于端口映射。Linux Docker 使用内置的`Bridge`驱动实现单机桥接网络，而 Windows Docker 使用内置的`NAT`驱动来实现。

覆盖网络是当下流行的方式，并且是一种出色的多机容器网络方案。第 12 章会深入介绍覆盖网络。

`Macvlan`驱动（在 Windows 中是`Transparent`）允许容器接入现存物理网络以及 VLAN。通过赋予容器 MAC 和 IP 地址的方式，让容器成为网络中的“一等公民”。不过，该驱动需要主机的 NIC 支持混杂模式，这意味着该驱动在公有云上无法使用。

Docker 使用 Libnetwork 实现了基础服务发现功能，同时还实现了服务网格，支持对入站流量实现容器级别负载均衡。



# 容器隔离网络

每次容器被创建，都会分配一个唯一IP。

默认容器连接桥接网路，除非用户特别指定



```bash
# 滚动鼠标查看容器IP
docker container inspect <Conntainer Name or ID>
# 指定格式查看IP
docker inspect --format='{{.NetworkSettings.IPAddress}}' <Container Name/ID>
```



```bash
# 查看网络list 表
docker network ls
```

none 内部运行容器

host 共享主机IP 和端口范围

bridge: 不指定默认桥接模式，如果指定网络使用 --net 命令



![image-20220908123514464](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220908123514464.png)





## 用户定义桥接网络

![image-20220908125102732](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220908125102732.png)

 

![image-20220908161208063](http://typora-dy.oss-cn-beijing.aliyuncs.com/img/image-20220908161208063.png)

```bash
# 自定义上图网络
$ docker network create --driver bridge mynet

$ docker run -itd --net mynet --name c1 ubuntu bash
$ docker run -itd --net mynet --name c2 ubuntu bash
$ docker run -itd --net mynet --name c3 ubuntu bash
$ docker run -itd --name c4 ubuntu bash
# 打通容器和其他网络的连接
$ docker network connect bridge c3
# 查看容器
$ docker container ls

# 查看mynet 网络详细信息 查看容器IP
$ docker network inspect mynet

# 查看bridge 网络详细信息,查看容器IP
$ docker network inspect bridge
```

连通测试

```bash
[root@root ~]# docker exec -it c3 /bin/bash
root@a0b7f91946c2:/# apt-get update && apt-get install -y iputils-ping
Get:1 http://security.ubuntu.com/ubuntu jammy-security InRelease [110 kB]   
Hit:2 http://archive.ubuntu.com/ubuntu jammy InRelease                               
Hit:3 http://archive.ubuntu.com/ubuntu jammy-updates InRelease
Hit:4 http://archive.ubuntu.com/ubuntu jammy-backports InRelease
Fetched 110 kB in 5s (20.7 kB/s)
Reading package lists... Done
Reading package lists... Done
Building dependency tree... Done
Reading state information... Done
iputils-ping is already the newest version (3:20211215-1).
0 upgraded, 0 newly installed, 0 to remove and 0 not upgraded.
root@a0b7f91946c2:/# ping -c 2 c1
PING c1 (172.18.0.2) 56(84) bytes of data.
64 bytes from c1.mynet (172.18.0.2): icmp_seq=1 ttl=64 time=0.109 ms
64 bytes from c1.mynet (172.18.0.2): icmp_seq=2 ttl=64 time=0.059 ms

--- c1 ping statistics ---
2 packets transmitted, 2 received, 0% packet loss, time 1006ms
rtt min/avg/max/mdev = 0.059/0.084/0.109/0.025 ms
root@a0b7f91946c2:/# ping -c 2 c2
PING c2 (172.18.0.3) 56(84) bytes of data.
64 bytes from c2.mynet (172.18.0.3): icmp_seq=1 ttl=64 time=0.079 ms
64 bytes from c2.mynet (172.18.0.3): icmp_seq=2 ttl=64 time=0.053 ms

--- c2 ping statistics ---
2 packets transmitted, 2 received, 0% packet loss, time 1012ms
rtt min/avg/max/mdev = 0.053/0.066/0.079/0.013 ms
root@a0b7f91946c2:/# ping -c 2 c4							不能直接ping 通
ping: c4: Name or service not known
root@a0b7f91946c2:/# ping -c 2 172.17.0.3					使用C4 ip ping 通
PING 172.17.0.3 (172.17.0.3) 56(84) bytes of data.
64 bytes from 172.17.0.3: icmp_seq=1 ttl=64 time=0.082 ms
64 bytes from 172.17.0.3: icmp_seq=2 ttl=64 time=0.054 ms

--- 172.17.0.3 ping statistics ---
2 packets transmitted, 2 received, 0% packet loss, time 1006ms
rtt min/avg/max/mdev = 0.054/0.068/0.082/0.014 ms
root@a0b7f91946c2:/# 
```

```
[root@root ~]# docker attach c2
root@bc81cf23d7b2:/# apt-get update && apt-get install -y iputils-ping 
Hit:1 http://security.ubuntu.com/ubuntu jammy-security InRelease                                       
Hit:2 http://archive.ubuntu.com/ubuntu jammy InRelease                                                 
Hit:3 http://archive.ubuntu.com/ubuntu jammy-updates InRelease
Hit:4 http://archive.ubuntu.com/ubuntu jammy-backports InRelease
Reading package lists... Done
Reading package lists... Done
Building dependency tree... Done
Reading state information... Done
The following additional packages will be installed:
  libcap2-bin libpam-cap
The following NEW packages will be installed:
  iputils-ping libcap2-bin libpam-cap
0 upgraded, 3 newly installed, 0 to remove and 0 not upgraded.
Need to get 76.8 kB of archives.
After this operation, 280 kB of additional disk space will be used.
Get:1 http://archive.ubuntu.com/ubuntu jammy/main amd64 libcap2-bin amd64 1:2.44-1build3 [26.0 kB]
Get:2 http://archive.ubuntu.com/ubuntu jammy/main amd64 iputils-ping amd64 3:20211215-1 [42.9 kB]
Get:3 http://archive.ubuntu.com/ubuntu jammy/main amd64 libpam-cap amd64 1:2.44-1build3 [7932 B]
Fetched 76.8 kB in 2s (40.9 kB/s)   
debconf: delaying package configuration, since apt-utils is not installed
Selecting previously unselected package libcap2-bin.
(Reading database ... 4395 files and directories currently installed.)
Preparing to unpack .../libcap2-bin_1%3a2.44-1build3_amd64.deb ...
Unpacking libcap2-bin (1:2.44-1build3) ...
Selecting previously unselected package iputils-ping.
Preparing to unpack .../iputils-ping_3%3a20211215-1_amd64.deb ...
Unpacking iputils-ping (3:20211215-1) ...
Selecting previously unselected package libpam-cap:amd64.
Preparing to unpack .../libpam-cap_1%3a2.44-1build3_amd64.deb ...
Unpacking libpam-cap:amd64 (1:2.44-1build3) ...
Setting up libcap2-bin (1:2.44-1build3) ...
Setting up libpam-cap:amd64 (1:2.44-1build3) ...
debconf: unable to initialize frontend: Dialog
debconf: (No usable dialog-like program is installed, so the dialog based frontend cannot be used. at /usr/share/perl5/Debconf/FrontEnd/Dialog.pm line 78.)
debconf: falling back to frontend: Readline
debconf: unable to initialize frontend: Readline
debconf: (Can't locate Term/ReadLine.pm in @INC (you may need to install the Term::ReadLine module) (@INC contains: /etc/perl /usr/local/lib/x86_64-linux-gnu/perl/5.34.0 /usr/local/share/perl/5.34.0 /usr/lib/x86_64-linux-gnu/perl5/5.34 /usr/share/perl5 /usr/lib/x86_64-linux-gnu/perl-base /usr/lib/x86_64-linux-gnu/perl/5.34 /usr/share/perl/5.34 /usr/local/lib/site_perl) at /usr/share/perl5/Debconf/FrontEnd/Readline.pm line 7.)
debconf: falling back to frontend: Teletype
Setting up iputils-ping (3:20211215-1) ...
root@bc81cf23d7b2:/# ping -c 2 c1
PING c1 (172.18.0.2) 56(84) bytes of data.
64 bytes from c1.mynet (172.18.0.2): icmp_seq=1 ttl=64 time=0.085 ms
64 bytes from c1.mynet (172.18.0.2): icmp_seq=2 ttl=64 time=0.057 ms

--- c1 ping statistics ---
2 packets transmitted, 2 received, 0% packet loss, time 1054ms
rtt min/avg/max/mdev = 0.057/0.071/0.085/0.014 ms
root@bc81cf23d7b2:/# ping -c 2 c2
PING c2 (172.18.0.3) 56(84) bytes of data.
64 bytes from bc81cf23d7b2 (172.18.0.3): icmp_seq=1 ttl=64 time=0.019 ms
64 bytes from bc81cf23d7b2 (172.18.0.3): icmp_seq=2 ttl=64 time=0.031 ms

--- c2 ping statistics ---
2 packets transmitted, 2 received, 0% packet loss, time 1014ms
rtt min/avg/max/mdev = 0.019/0.025/0.031/0.006 ms
root@bc81cf23d7b2:/# ping -c 2 c3
PING c3 (172.18.0.4) 56(84) bytes of data.
64 bytes from c3.mynet (172.18.0.4): icmp_seq=1 ttl=64 time=0.061 ms
64 bytes from c3.mynet (172.18.0.4): icmp_seq=2 ttl=64 time=0.056 ms

--- c3 ping statistics ---
2 packets transmitted, 2 received, 0% packet loss, time 1044ms
rtt min/avg/max/mdev = 0.056/0.058/0.061/0.002 ms
root@bc81cf23d7b2:/# ping -c 2 c4
ping: c4: Name or service not known
root@bc81cf23d7b2:/# ping -c 2 172.17.0.3
PING 172.17.0.3 (172.17.0.3) 56(84) bytes of data.

--- 172.17.0.3 ping statistics ---
2 packets transmitted, 0 received, 100% packet loss, time 1003ms

root@bc81cf23d7b2:/#
```



结论：

docker0特点：默认，域名不能访问，--1ink可以打通连接

在同一网络中的容器可以互相ping 通。

不在同一网络的容器不能ping 通。



连通2个不同的网络

```
docker network connnect 
```



## 暴露容器端口

```
# docker 生成一个随机端口访问
docker run -d --name nginx -p 80 nginx
# 查看容器信息
# docker container ls -a  | docker ps
# 查看端口号
docker inspect --format='{{.NetworkSettings.Ports}}' nginx
```

```
# 生成指定端口
docker run -d --name nginx -p 80:80 nginx
# 生成指定主机和端口
docker run -d --name nginx -p 192.168.247.128:80:80 nginx
```

Dockerfile 生成镜像  

```dockerfile
FROM nginx
EXPOSE 80
```

```
docker build -t nginx .
```

上述代码等价于 `docker run -d --name nginx -p 80 nginx`

## **Swarm Networking**

