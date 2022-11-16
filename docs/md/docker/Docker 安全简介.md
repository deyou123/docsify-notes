#### 知识点

- Linux 安全技术
- Namespace 命名空间
- Control Group 控制组
- Docker 平台安全技术

## Docker 安全简介

安全本质就是分层！通俗地讲，拥有更多的安全层，就能拥有更多的安全性。而 Docker 提供了很多安全层。下图展示了本章接下来会介绍的一部分安全技术。

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577952517712)

Linux Docker 利用了大部分 Linux 通用的安全技术。这些技术包括了命名空间（Namespace）、控制组（CGroup）、系统权限（Capability），强制访问控制（MAC）系统以及安全计算（Seccomp）。对于上述每种技术，Docker 都设置合理的默认值，实现了流畅并且适度安全的开箱即用体验。同时，Docker 也允许用户根据特定需求自定义调整每项安全配置。

Docker 平台本身也提供了一些非常棒的原生安全技术。并且重要的是，这些技术**使用起来都很简单！**

- **Docker Swarm 模式：**默认是开启安全功能的。无须任何配置，就可以获得加密节点 ID、双向认证、自动化 CA 配置、自动证书更新、加密集群存储、加密网络等安全功能。
- **Docker 内容信任（Docker Content Trust, DCT）：**允许用户对镜像签名，并且对拉取的镜像的完整度和发布者进行验证。
- **Docker 安全扫描（Docker Security Scanning）：**分析 Docker 镜像，检查已知缺陷，并提供对应的详细报告。
- **Docker 密钥：**使安全成为 Docker 生态系统中重要的一环。Docker 密钥存储在加密集群存储中，在容器传输过程中实时解密，使用时保存在内存文件系统，并运行了一个最小权限模型。

重要的是，要知道 Docker 在使用主流 Linux 安全技术的同时，还提供了额外的扩展以及一些新的安全技术。Linux 安全技术看起来可能略为复杂，但是 Docker 平台的安全技术却非常简单。

大家都知道安全是非常重要的。同时，安全又很复杂并且枯燥无味。

在最初决定向平台中添加安全功能时，就选择了简单易用的方式。Docker 知道如果安全相关配置特别复杂，那么就没有人会去使用。所以，Docker 平台提供的绝大部分安全功能使用起来都很简单。并且大部分的安全设置都配有默认值，意味着用户无须任何配置，就能得到一个相当安全的平台。当然，默认配置不一定是最合适的，但至少在最开始能够保障一定的安全性。如果默认配置与用户需求不符，那么用户也可以进行自定义配置。

接下来的内容按照如下结构进行介绍。

- Linux 安全技术。
  - Namespace。
  - Control Group。
  - Capability。
  - MAC。
  - Seccomp。
- Docker 平台安全技术。
  - Swarm 模式。
  - Docker 安全扫描。
  - Docker 内容信任机制。
  - Docker 密钥。



## Linux 安全技术

每个优秀的容器平台都应该使用命名空间和控制组技术来构建容器。最佳的容器平台还会集成其他容器安全技术，例如系统权限、强制访问控制系统（如 SELinux 和 AppArmor）以及安全计算。正如用户所期望的，Docker 中集成了上述全部安全技术！

在本节中会对 Docker 中用到的主要 Linux 技术进行简要介绍。之所以不进行深入介绍，是因为本课程希望将重点放在 Docker 平台技术上。

## Namespace

内核命名空间属于容器中非常核心的一部分！ 该技术能够将操作系统（OS）进行拆分，使一个操作系统看起来像多个互相独立的操作系统一样。这种技术可以用来做一些非常酷的事情，比如在相同的 OS 上运行多个 Web 服务，同时还不存在端口冲突的问题。该技术还允许多个应用运行在相同 OS 上并且不存在竞争，同时还能共享配置文件以及类库。

举两个简单的例子。

- 用户可以在相同的 OS 上运行多个 Web 服务，每个端口都是 443。为了实现该目的，可以将两个 Web 服务应用分别运行在自己的网络命名空间中。这样可以生效的原因是每个网络命名空间都拥有自己的 IP 地址以及对应的全部端口。也可能需要将每个 IP 映射到 Docker 主机的不同端口之上，但是使用 IP 上的哪个端口则无须其他额外配置。
- 用户还可以运行多个应用，应用间共享类库和配置文件，但是版本可能不同。为了实现该目标，需要在自己的挂载命名空间中运用每个应用程序。这样做能生效的原因，是每个挂载命名空间内都有系统上任意目录的独立副本。

下图展示了一个抽象的例子，两个应用运行在相同的主机上，并且同时使用 443 端口。每个 Web 服务应用都运行在自己的网络命名空间之内。

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577953396031)

Linux Docker 现在利用了下列内核命名空间。

- 进程 ID（PID）。
- 网络（NET）。
- 文件系统/挂载（MNT）。
- 进程内通信（IPC）。
- 用户（USER）。
- UTS。

下面会简要介绍每种技术都做了些什么。但重要的是要理解，**Docker 容器是由各种命名空间组合而成的**。再次强调一遍，**Docker 容器本质就是命名空间的有组织集合**。

例如，每个容器都由自己的 PID、NET、MNT、IPC、UTS 构成，还可能包括 USER 命名空间。这些命名空间有机的组合就是所谓的容器。下图展示了两个运行在相同 Linux 主机上的容器。

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577953475431)

接下来简要介绍一下 Docker 是如何使用每个命名空间的。

- 进程 ID 命名空间：Docker 使用 `PID` 命名空间为每个容器提供互相独立的容器树。每个容器都拥有自己的进程树，意味着每个容器都有自己的 PID 为 1 的进程。PID 命名空间也意味着容器不能看到其他容器的进程树，或者其所在主机的进程树。
- 网络命名空间：Docker 使用 `NET` 命名空间为每个容器提供互相隔离的网络栈。网络栈中包括接口、IP 地址、端口地址以及路由表。例如，每个容器都有自己的 eth0 网络接口，并且有自己独立的 IP 和端口地址。
- 挂载点命名空间：每个容器都有互相隔离的根目录 `/` 。这意味着每个容器都有自己的 `/etc`、`/var`、`/dev` 等目录。容器内的进程不能访问 Linux 主机上的目录，或者其他容器的目录，只能访问自己容器的独立挂载命名空间。
- 进程内通信命名空间：Docker 使用 `IPC` 命名空间在容器内提供共享内存。`IPC` 提供的共享内存在不同容器间也是互相独立的。
- 用户命名空间：Docker 允许用户使用 `USER` 命名空间将容器内用户映射到 Linux 主机不同的用户上。常见的例子就是将容器内的 `root` 用户映射到 Linux 主机的非 root 用户上。用户命名空间对于 Docker 来说还属于新生事物且非必选项。该部分内容在未来可能出现改变。
- UTS 命名空间：Docker 使用 `UTS` 命名空间为每个容器提供自己的主机名称。

如下图所示，容器本质就是命名空间的有机组合！

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577953868109)



## Control Group

如果说命名空间用于隔离，那么控制组就是用于限额。

假设容器就是酒店中的房间。每个容器间都是互相独立的，但是每个房间都共享一部分公共资源，比如供应水电、共享游泳池、共享健身、共享早餐餐吧等。CGroup 允许用户设置一些限制（以酒店作为类比）来保证不会存在单一容器占用全部的公共资源，如用光全部水或者吃光早餐餐吧的全部食物。

抛开酒店的例子，在 Docker 的世界中，容器之间是互相隔离的，但却共享 OS 资源，比如 CPU、RAM 以及硬盘 I/O。CGroup 允许用户设置限制，这样单个容器就不能占用主机全部的 CPU、RAM 或者存储 I/O 资源了。

## Capability

以 root 身份运行容器不是什么好主意，root 拥有全部的权限，因此很危险。但是，如果以非 root 身份在后台运行容器的话，非 root 用户缺少权限，处处受限。所以用户需要一种技术，能选择容器运行所需的 root 用户权限。了解一下 Capability！

在底层，Linux root 用户是由许多能力组成的。其中一部分包括以下几点。

- `CAP_CHOWN`：允许用户修改文件所有权。
- `CAP_NET_BIND_SERVICE`：允许用户将 socket 绑定到系统端口号。
- `CAP_SETUID`：允许用户提升进程优先级。
- `CAP_SYS_BOOT`：允许用户重启系统。

Docker 采用 Capability 机制来实现用户在以 root 身份运行容器的同时，还能移除非必须的 root 能力。如果容器运行只需要 root 的绑定系统网络端口号的能力，则用户可以在启动容器的同时移除全部 root 能力，然后再将 CAP_NET_BIND_SERVICE 能力添加回来。



## MAC 和 Seccomp

##### MAC

Docker 采用主流 Linux MAC 技术，例如 AppArmor 以及 SELinux。

基于用户的 Linux 发行版本，Docker 对新容器增加了默认的 AppArmor 配置文件。根据 Docker 文档的描述，默认配置文件提供了“适度的保护，同时还能兼容大部分应用”。

Docker 允许用户在启动容器的时候不设置相应策略，还允许用户根据需求自己配置合适的策略。

##### Seccomp

Docker 使用过滤模式下的 Seccomp 来限制容器对宿主机内核发起的系统调用。

按照 Docker 的安全理念，每个新容器都会设置默认的 Seccomp 配置，文件中设置了合理的默认值。这样做是为了在不影响应用兼容性的前提下，提供适度的安全保障。

用户同样可以自定义 Seccomp 配置，同时也可以通过向 Docker 传递指定参数，使 Docker 启动时不设置任何 Seccomp 配置。



## Linux 安全技术总结

Docker 基本支持所有的 Linux 重要安全技术，同时对其进行封装并赋予合理的默认值，这在保证了安全的同时也避免了过多的限制，如下图所示：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577954330581)

自定义设置某些安全技术会非常复杂，因为这需要用户深入理解安全技术的运作原理，同时还要了解 Linux 内核的工作机制。希望这些技术在未来能够简化配置的过程，但就现阶段而言，使用 Docker 在对安全技术的封装中提供的默认值是很不错的选择。



## Docker 平台安全技术

### Swarm 模式

Swarm 模式是 Docker 未来的趋势。Swarm 模式支持用户集群化管理多个 Docker 主机，同时还能通过声明式的方式部署应用。每个 Swarm 都由管理者和工作者节点构成，节点可以是 Linux 或者 Windows。管理者节点构成了集群中的控制层，并负责集群配置以及工作负载的分配。工作者节点就是运行应用代码的容器。

正如所预期的，Swarm 模式包括很多开箱即用的安全特性，同时还设置了合理的默认值。这些安全特性包括以下几点。

- 加密节点 ID。
- 基于 TLS 的认证机制。
- 安全准入令牌。
- 支持周期性证书自动更新的 CA 配置。
- 加密集群存储（配置 DB）。
- 加密网络。

接下来将详细介绍如何构建安全的 Swarm，以及如何进行安全相关的配置。

为了完成下面的内容，读者需要至少 3 个 Docker 主机，每个都运行 1.13 或者更高版本的 Docker。示例中 3 个 Docker 主机分别叫作 “manager1”、“manager2”、“worker1”。每台主机上都安装 Ubuntu 16.04，其上运行了 Docker 18.01.0-ce。同时还有一个网络负责联通 3 台主机，并且主机之间可以通过名称互相 ping 通。安装完成后如下图所示：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577954648627)

（1）配置安全的 Swarm 集群

读者可以在其 Swarm 集群管理者节点上运行 `docker swarm init` 命令。在本例中，命令运行于“manager1”节点之上：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577955056635)

上面的命令就是配置安全 Swarm 集群所要做的全部工作！

“manager1”被配置为 Swarm 集群中的第一个管理节点，也是根 CA 节点。Swarm 集群已经被赋予了加密 Swarm ID，同时“manager1”节点为自己发布了一个客户端认证信息，标明自己是 Swarm 集群管理者。证书的更新周期默认设置为 90 天，集群配置数据库也已经配置完成并且处于加密状态。安全令牌也已经成功创建，允许新的管理者和工作者节点加入到 Swarm 集群中。以上全部内容都只需要**一条命令**！

实验环境如图所示：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577955117041)

现在将 “manager2” 节点加入到集群中，作为额外的管理者节点。

将新的管理者节点加入到 Swarm 需要两步。第一步，需要提取加入管理者到集群中所需的令牌；第二步，在“manager2”节点上执行 `docker swarm join` 命令。只要将管理者准入令牌作为 `docker swarm join` 命令的一部分，“manager2” 就作为管理者节点加入 Swarm。

在“manager1”上运行下面的命令获取管理者准入令牌：

```bash
root@manager1:~# docker swarm join-token manager
To add a manager to this swarm, run the following command:

    docker swarm join --token SWMTKN-1-0in0hswxqr9s8el5fsyat6kk1s9jwdl6chyckz2cfghmv3r2yj-55fsml7id2ns0w1xrq2n7gt3d 172.16.109.89:2377

root@manager1:~#
```

命令输出内容给出了管理者加入 Swarm 所需运行的准确命令。准入令牌和 IP 地址在读者自己的实验环境中是不一样的。

复制该命令并在“manager2”节点上运行：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577955333035)

可以通过在任意管理者节点上运行 `docker node ls` 命令来确认上述操作：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577955420167)

上述输出内容中显示“manager1”和“manager2”都加入了 Swarm，并且都是 Swarm 管理者。最新的配置如下图所示：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577955488663)

两个管理者这个数量，大概是最糟糕的一种情况了。但是这只是一个实验环境，而不是什么核心业务生产环境，所以糟糕点也无所谓。

向 Swarm 中加入工作者也只需两步。第一步需要获取新工作者的准入令牌，第二步是在工作者节点上运行 `docker swarm join` 命令。

在任意管理者节点上运行下面的命令，获取工作者准入令牌：

```bash
root@manager2:~# docker swarm join-token worker
To add a worker to this swarm, run the following command:

    docker swarm join --token SWMTKN-1-0in0hswxqr9s8el5fsyat6kk1s9jwdl6chyckz2cfghmv3r2yj-08ia4x6ux8uo65qh43cet58nk 172.16.109.90:2377

root@manager2:~#
```

读者可以在指定工作者的节点上运行该命令。准入令牌和 IP 地址会有所不同。

复制如下所示命令到“worker1”上并且运行：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577955735285)

在任意 Swarm 管理者上运行 `docker node ls` 命令：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577955801599)

目前我们已经拥有包含两个管理者和一个工作者的 Swarm 集群。管理者配置为高可用（HA），并且复用集群存储。最新的配置如下图所示：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577955863831)

（2）了解 Swarm 安全背后的原理

到目前为止，读者已经成功搭建了安全的 Swarm 集群。接下来一起花费几分钟了解一下这背后涉及的安全技术。

1）Swarm 准入令牌

向某个现存的 Swarm 中加入管理者和工作者所需的唯一凭证就是准入令牌。因此，保证准入令牌的安全十分关键！不要将其发布到公开的 Github 仓库中。

每个 Swarm 都包含两种不同准入令牌。

- 管理者所需准入令牌。
- 工作者所需准入令牌。

有必要理解 Swarm 准入令牌的格式。每个准入令牌都由 4 个不同的字段构成，中间采用虚线（-）连接。

```tex
PREFIX - VERSION - SWARM ID - TOKEN
```

PREFIX 永远是“SWMTKN”，这样允许读者通过表达式匹配到该令牌，以避免意外将其发布到公共环境当中；VERSION 这一列则展示了 Swarm 的版本信息；SWARM ID 列是 Swarm 认证信息的一个哈希值；TOKEN 这一列的内容决定了该令牌是管理者还是工作者的准入令牌。

如下所示，对于指定 Swarm 的管理者和工作者准入令牌，除了最后 TOKEN 字段的内容之外没有任何区别。

- 管理者：SWMTKN-1-0in...2yj-55fsml7id2ns0w1xrq2n7gt3d。
- 工作者：SWMTKN-1-0in...2yj-08ia4x6ux8uo65qh43cet58nk。

如果用户认为当前准入令牌存在风险，仅用一条命令就可以取消该准入令牌授权，同时发布新的准入令牌。在下面的示例中，执行 `docker swarm join-token --rotate manager` 取消了已经授权的管理者准入令牌，之后又发布了新的令牌：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577956467465)

需要注意的是，新旧令牌只有最后字段存在区别。SWARM ID 还是相同的。此外，该命令不会影响已经加入到 Swarm 中的管理者节点。

准入令牌保存在集群配置的数据库中，默认是加密的。

2）TLS 和双向认证

每个加入 Swarm 的管理者和工作者节点，都需要发布自己的客户端证书。这个证书用于双向认证。证书中定义了节点相关信息，包括从属的 Swarm 集群以及该节点在集群中的身份（管理者还是工作者）。

在 Linux 主机上，读者可以指定使用 `sudo openssl x509 -in /var/lib/docker/swarm/certificates/swarm-node.crt -text` 命令查看指定节点的客户端证书：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577956739135)

上述输出中，`Subject` 中用到了 `O`、`OU` 以及 `CN` 字段分别表示 Swarm ID、节点角色以及节点 ID 信息。

- 组织字段 `O` 保存的是 Swarm ID。
- 组织单元字段 `OU` 保存的是节点在 Swarm 中的角色。
- 规范名称字段 `CN` 保存的是节点的加密 ID。

在 `Validity` 中，还可以直接看到证书的更新周期。

上述信息可以在 `docker system info` 命令的输出中得到验证：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577956976360)

3）配置一些 CA 信息

通过 `docker swarm update` 命令可以配置 Swarm 证书的更新周期。下面的示例中，将 Swarm 的证书更新周期修改为 30 天：

```bash
root@manager1:~# docker swarm update --cert-expiry 720h
Swarm updated.
root@manager1:~#
```

Swarm 允许节点在证书过期前重新创建证书，这样可以保证 Swarm 中全部节点不会在同一时间尝试更新自己的证书信息。

读者可以在创建 Swarm 的时候，通过在 `docker swarm init` 命令中增加 `--external-ca` 参数来指定外部的 CA。

`docker swarm ca` 命令可以用于管理 CA 相关配置。可以在运行该命令时指定 `--help` 来查看命令功能：

```bash
$ docker swarm ca --help

Usage: docker swarm ca [OPTIONS]

Manage root CA

Options:
      --ca-cert pem-file               Path to the PEM-formatted root CA
                                       certificate to use for the new cluster Path
      --ca-key pem-file                to the PEM-formatted root CA
                                       key to use for the new cluster
      --cert-expiry duration           Validity period for node certificates
                                       (ns|us|ms|s|m|h) (default 2160h0m0s)
  -d, --detach                         Exit immediately instead of waiting for the
                                       root rotation to converge Specifications of
      --external-ca external-ca        one or more certificate signing endpoints
                                       Print usage
      --help                           Suppress progress output
  -q, --quiet                          Rotate the swarm CA - if no certificate
      --rotate                         or key are provided, new ones will be gene\
```

4）集群存储

集群存储是 Swarm 的大脑，保存了集群配置和状态数据。

存储目前是基于 `etcd` 的某种实现，并且会在 Swarm 内所有管理者之间自动复制。存储默认也是加密的。

集群存储正逐渐成为很多 Docker 平台的关键技术。例如，Docker 网络和 Docker 密钥都用到了集群存储。Docker 平台的很多部分都已经用到了集群存储，未来对集群存储的利用会更多，而这也是 Swarm 模式在 Docker 规划中占据重要地位的原因之一。这还意味着，如果不使用 Swarm 模式运行 Docker，很多 Docker 特性就无法使用。

集群存储的日常维护由 Docker 自动完成。但是，在生产环境中，需要为集群存储提供完整的备份和恢复方案。

Swarm 模式安全部分的内容到此为止。

### Docker 安全扫描

快速发现代码缺陷的能力至关重要。Docker 安全扫描功能使得对 Docker 镜像中已知缺陷的检测工作变得简单。

注：

> 在本课程编写之时，Docker 安全扫描已经可以用于 Docker Hub 上私有仓库的镜像了。同时该技术还可以作为 Docker 可信服务本地化部署解决方案的一部分。最后，所有官方 Docker 镜像都经过了安全扫描，扫描报告在其仓库中可以查阅。

Docker 安全扫描对 Docker 镜像进行二进制代码级别的扫描，对其中的软件根据已知缺陷数据库（CVE 数据库）进行检查。在扫描执行完成后，会生成一份详细报告。

打开浏览器访问 Docker Hub，并搜索 Alpine 仓库。下图展示了官方 Alpine 仓库的 Tags 标签页：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577957331544)

Alpine 仓库是官方仓库，这意味着该仓库会自动扫描并生成对应报告。可以看到，镜像标签为 `edge`、`latest` 以及 `3.6` 的镜像都通过了已知缺陷的检查。但是 `alpine:3.5` 镜像存在已知缺陷（标红）。

如果打开 `alpine:3.5` 镜像，可以发现下图所示的详细信息：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577957390978)

这是发现自己软件中已知缺陷详情的一种简单方式。

Docker 可信镜像仓库服务（Docker Trusted Registry, DTR），属于 Docker 企业版中本地化镜像仓库服务的一部分内容，提供了相同的 Capability，同时还允许用户自行控制其镜像扫描时机以及扫描方式。例如，DTR 允许用户选择镜像是在推送时自动触发扫描，还是只能手工触发。同时 DTR 还允许用户手动更新 CVE 数据库，这对于 DTL 无法进行联网来自动更新 CVE 数据的场景来说，是一种理想的解决方案。

这就是 Docker 安全扫描，一种深入检测 Docker 镜像是否存在已知安全缺陷的好方式。当然，能力越大责任越大，当用户发现缺陷后，就需要承担解决相应缺陷的责任了。



### Docker 内容信任

Dockr 内容信任（Docker Content Trust，DCT）使得用户很容易就能确认所下载镜像的完整性以及其发布者。在不可信任的网络环境中下载镜像时，这一点很重要。

从更高层面来看，DCT 允许开发者对发布到 Docker Hub 或者 Docker 可信服务的镜像进行签名。当这些镜像被拉取的时候，会自动确认签名状态。下图展示了这一过程。

DCT 还可以提供关键上下文，如镜像是否已被签名从而可用于生产环境，镜像是否被新版本取代而过时等。

截至目前，DTC 提供的上下文还在初期，配置起来相当复杂。

在 Docker 主机上启用 DCT 功能，所要做的只是在环境中将 `DOCKER_CONTENT_TRUST` 变量设置为 1 ：

```bash
$ export DOCKER_CONTENT_TRUST=1
```

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577957475211)

在实际环境中，用户可能希望在系统中默认开启该特性。

如果使用 Docker 统一配置层（Docker 企业版的一部分），需要勾选下图所示 `Run Only Signed Images` 复选项。这样会强制所有在 UCP 集群中的节点只运行已签名镜像：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577957536135)

由上图可知，UCP 在 DCT 的基础上进行进一步封装，提供了已签名镜像的安全首选项信息。例如，用户可能有这样的需求：在生产环境中只能使用由 `secops` 签名的镜像。

一旦 DCT 功能开启，就不能获取并使用未签名镜像了。下图展示了开启 DCT 之后，如果再次尝试通过 Docker CLI 或者 UCP Web UI 界面拉取未签名镜像时所报的错误（两个示例都尝试拉取标签为“unsigned”的镜像）：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577957607452)

下图展示了 DCT 是如何阻止 Docker 客户端拉取一个被篡改的镜像的：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577957667791)

下图展示了 DCT 如何阻止客户端拉取旧镜像：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577957712821)

Docker 内容信任是一种很重要的技术，能帮助用户检查从 Docker 服务中拉取的镜像。该技术的基础模式配置起来非常简单，但是类似上下文等一些高级特性，现阶段配置起来还是非常复杂的。

### Docker 密钥

很多应用都需要密钥。比如密码、TLS 证书、SSH key 等。

在 Docker1.13 版本之前，没有一种标准且安全的方式能让密钥在应用间实现共享。常见的方式是开发人员将密钥以文本的方式写入环境变量（我们都这么做过）。这与理想状态差距甚远。

Docker1.13 引入了 Docker 密钥，将密钥变成 Docker 生态系统中的一等公民。例如，增加了一个新的子命令`docker secret` 来管理密钥。在 Docker 的 UCP 界面中，也有专门的地方来创建和管理密钥。在后台，密钥在创建后以及传输中都是加密的，使用时被挂载到内存文件系统，并且只对那些已经被授权了的服务开放访问。这确实是一种综合性的端到端解决方案。

下图展示了其总体流程：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20200102-1577957777555)

下面依次介绍上图中所示工作流的每一步。

（1）密钥被创建，并且发送到 Swarm。

（2）密钥存放在集群存储当中，并且是加密的（每个管理者节点都能访问集群存储）。

（3）B 服务被创建，并且使用了该密钥。

（4）密钥传输到 B 服务的任务节点（容器）的过程是加密的。

（5）B 服务的容器将密钥解密并挂载到路径 `/run/secrets` 下。这是一个临时的内存文件系统（在 Windows Docker 中该步骤有所不同，因为 Windows 中没有内存文件系统这个概念）。

（6）一旦容器（服务任务）完成，内存文件系统关闭，密钥也随之删除。

（7）A 服务中的容器不能访问该密钥。

用户可以通过 `docker secret` 子命令来管理密钥，可以通过在运行 `docker service create` 命令时附加 `--secret` ，从而为某个服务指定密钥。



## 总结

Docker 可以通过配置变得特别安全。Docker 支持全部的 Linux 主流安全技术，包括 Namespace、Control Group、Capability、MAC 以及 Seccomp。Docker 为这些安全技术设定了合理的默认值，但是用户也可以自行修改配置，或者禁用这些安全技术。

在通用的 Linux 安全技术之上，Docker 平台还引入了大量自有安全技术。Swarm 模式基于 TLS 构建，并且配置上极其简单灵活。安全扫描对镜像进行二进制源码级别扫描，并提供已知缺陷的详细报告。Docker 内容信任允许用户对内容进行签名和认证，密钥目前也是 Docker 中的一等公民。

最终结论就是，无论用户希望 Docker 环境有多安全，Docker 都可以实现。这一切都取决于用户如何配置 Docker。