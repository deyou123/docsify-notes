#### 知识点

- Docker Stack 简介
- 深入分析 Stack 文件
- 搭建应用实验环境

用 Docker Stack 部署应用简介



在笔记本上测试和部署简单应用很容易。但这只能算业余选手。在真实的生产环境进行多服务的应用部署和管理，这才是专业选手的水平！

幸运的是，Stack 正为此而生！Stack 能够在单个声明文件中定义复杂的多服务应用。Stack 还提供了简单的方式来部署应用并管理其完整的生命周期：初始化部署 > 健康检查 > 扩容 > 更新 > 回滚，以及其他功能！

步骤很简单。在 Compose 文件中定义应用，然后通过 `docker stack deploy` 命令完成部署和管理。就是这样！

Compose 文件中包含了构成应用所需的完整服务栈。此外还包括了卷、网络、安全以及应用所需的其他基础架构。

然后基于该文件使用 `docker stack deploy` 命令来部署应用，这很简单。

Stack 是基于 Docker Swarm 之上来完成应用的部署。因此诸如安全等高级特性，其实都是来自 Swarm。

简而言之，Docker 适用于开发和测试。Docker Stack 则适用于大规模场景和生产环境。

如果了解 Docker Compose，就会发现 Docker Stack 非常简单。事实上在许多方面，Stack 一直是期望的 Compose——完全集成到 Docker 中，并能够管理应用的整个生命周期。

从体系结构上来讲，Stack 位于 Docker 应用层级的最顶端。Stack 基于服务进行构建，而服务又基于容器，如下图所示：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191231-1577776467168)

本节实验接下来的内容分为如下几部分。

- 简单应用。
- 深入分析 Stack 文件。
- 部署应用。
- 管理应用。





## 简单应用

本章后续的内容会一直使用示例应用 **AtSea Shop** 。该示例托管在 Github 的 `dockersamples/atsea-sample-shop-app` 库中，基于 Apache 2.0 许可证开源。

使用该应用是因为其复杂度适中，不会因为太复杂而难以完整解释。除此之外，该应用还是个多服务应用，并且利用了认证和安全相关的技术。

如图所示，该应用由 5 个服务、3 个网络、4 个密钥以及 3 组端口映射构成。具体细节将会结合 Stack 文件进行分析：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191231-1577782771643)

注：

> 在本章中用到服务一词时，指的是 Docker 服务（由若干容器组成的集合，作为一个整体进行统一管理，并且在 Docker API 中存在对应的服务对象）。

复制 Github 仓库，以获取全部源代码文件：

```bash
$ git clone https://github.com/dockersamples/atsea-sample-shop-app
正克隆到 'atsea-sample-shop-app'...
remote: Enumerating objects: 30, done.
remote: Counting objects: 100% (30/30), done.
remote: Compressing objects: 100% (30/30), done.
remote: Total 672 (delta 20), reused 0 (delta 0), pack-reused 642
接收对象中: 100% (672/672), 7.29 MiB | 1.15 MiB/s, done.
处理 delta 中: 100% (217/217), done.
检查连接... 完成。
```

该应用的代码由若干目录和源码文件组成。读者可以随意浏览这些文件。但是接下来，重点关注的文件是 `docker-stack.yml` 。该文件通常被称为 Stack 文件，在该文件中定义了应用及其依赖。

在该文件整体结构中，定义了 4 种顶级关键字：

```yaml
version:
services:
networks:
secrets:
```

**version** 代表了 Compose 文件格式的版本号。为了应用于 Stack，需要 3.0 或者更高的版本。**services** 中定义了组成当前应用的服务都有哪些。**networks** 列出了必需的网络。**secrets** 定义了应用用到的密钥。

如果展开顶级的关键字，可以看到类似上图中的结构。Stack 文件由 5 个服务构成，分别为“reverse_proxy”、“database”、“appserver”、“visualizer”、“payment_gateway”。Stack 文件中包含 3 个网络，分别为“front-tier”、“back-tier”、“payment”。最后，Stack 文件中有 4 个密钥，分别为“postgres_password”、“staging_token”、“revprox_key”、“revprox_cert”。

```yaml
version: '3.2'
services:
  reverse_proxy:
  database:
  appserver:
  visualizer:
  payment_gateway:
networks:
  front-tier:
  back-tier:
  payment:
secrets:
  postgres_password:
  staging_token:
  revprox_key:
  revprox_cert:
```

Stack 文件定义了应用的很多依赖要素，理解这一点很重要。因此，Stack 文件是应用的一个自描述文件，并且作为一个很好的工具弥合了开发和运维之间的隔阂。

接下来一起深入分析 Stack 文件的细节。



## 深入分析 Stack 文件

Stack 文件就是 Docker Compose 文件。唯一的要求就是 `version：` 一项需要是“3.0”或者更高的值。具体可以关注 Docker 文档中关于 Compose 文件的最新版本信息。

在 Docker 根据某个 Stack 文件部署应用的时候，首先会检查并创建 `networks` ：关键字对应的网络。如果对应网络不存在，Docker 会进行创建。

一起看一下 Stack 文件中的网络定义。

##### 1．网络

```yaml
networks:
  front-tier:
  back-tier:
  payment:
    driver: overlay
    driver_opts:
      encrypted: 'yes'
```

该文件中定义了 3 个网络：`front-tier`、`back-tier` 以及 `payment` 。默认情况下，这些网络都会采用 `overlay` 驱动，新建对应的覆盖类型的网络。但是 `payment` 网络比较特殊，需要数据层加密。

默认情况下，覆盖网络的所有控制层都是加密的。如果需要加密数据层，有两种选择。

- 在 `docker network create` 命令中指定 `-o encrypted` 参数。
- 在 Stack 文件中的 `driver_opts` 之下指定 `encrypted:'yes'`。

数据层加密会导致额外开销，而影响额外开销大小的因素有很多，比如流量的类型和流量的多少。但是，通常额外开销会在 10% 的范围之内。

正如前面提到的，全部的 3 个网络均会先于密钥和服务被创建。

##### 2．密钥

密钥属于顶级对象，在当前 Stack 文件中定义了 4 个。

```yaml
secrets:
  postgres_password:
    external: true
  staging_token:
    external: true
  revprox_key:
    external: true
  revprox_cert:
    external: true
```

注意，4 个密钥都被定义为 external。这意味着在 Stack 部署之前，这些密钥必须存在。

当然在应用部署时按需创建密钥也是可以的，只需要将 `file:` 替换为 `external: true` 。但该方式生效的前提是，需要在主机文件系统的对应路径下有一个文本文件，其中包含密钥所需的值，并且是未加密的。这种方式存在明显的安全隐患。

稍后会展示在部署的时候究竟是如何创建这些密钥的。现在，读者只需知道应用定义了 4 个密钥，并且需要提前创建即可。

下面对服务逐一进行分析。

##### 3．服务

部署中的主要操作都在服务这个环节。

每个服务都是一个 JSON 集合（字典），其中包含了一系列关键字。我们会依次介绍每个关键字，并解释操作的具体内容。

（1）reverse_proxy 服务

正如读者所见，`reverse_proxy` 服务定义了镜像、端口、密钥以及网络。

```yaml
reverse_proxy:
  image: dockersamples/atseasampleshopapp_reverse_proxy
  ports:
    - '80:80'
    - '443:443'
  secrets:
    - source: revprox_cert
      target: revprox_cert
    - source: revprox_key
      target: revprox_key
networks:
  - front-tier
```

image 关键字是服务对象中唯一的必填项。顾名思义，该关键字定义了将要用于构建服务副本的 Docker 镜像。

Docker 是可选项，除非指定其他值，否则镜像会从 Docker Hub 拉取。读者可以通过在镜像前添加对应第三方镜像仓库服务 API 的 DNS 名称的方式，来指定某个镜像从第三方服务拉取。例如 Google 的容器服务的 DNS 名称为 `gcr.io` 。

Docker Stack 和 Docker Compose 的一个区别是，Stack 不支持**构建**。这意味着在部署 Stack 之前，所有镜像必须提前构建完成。

ports 关键字定义了两个映射。

- `80:80` 将 Swarm 节点的 80 端口映射到每个服务副本的 80 端口。
- `443:443` 将 Swarm 节点的 443 端口映射到每个服务副本的 443 端口。

默认情况下，所有端口映射都采用 Ingress 模式。这意味着 Swarm 集群中每个节点的对应端口都会映射并且是可访问的，即使是那些没有运行副本的节点。另一种方式是 Host 模式，端口只映射到了运行副本的 Swarm 节点上。但是，Host 模式需要使用完整格式的配置。例如，在 Host 模式下将端口映射到 80 端口的语法如下所示。

```yaml
ports:
  - target: 80
    published: 80
    mode: host
```

推荐使用完整语法格式，这样可以提高易读性，并且更灵活（完整语法格式支持 Ingress 模式和 Host 模式）。但是，完整格式要求 Compose 文件格式的版本至少是 3.2。

secret 关键字中定义了两个密钥：`revprox_cert` 以及 `revprox_key` 。这两个密钥必须在顶级关键字 `secrets` 下定义，并且必须在系统上已经存在。

密钥以普通文件的形式被挂载到服务副本当中。文件的名称就是 stack 文件中定义的 `target` 属性的值，其在 Linux 下的路径为 `/run/secrets` ，在 Windows 下的路径为 `C:\ProgramData\Docker\secrets` 。Linux 将 `/run/secrets` 作为内存文件系统挂载，但是 Windows 并不会这样。

本服务密钥中定义的内容会在每个服务副本中被挂载，具体路径为 `/run/secrets/revprox_cert` 和 `/run/secrets/revprox_key` 。若将其中之一挂载为 `/run/secrets/uber_secret` ，需要在 stack 文件中定义如下内容。

```yaml
secrets:
  - source: revprox_cert
    target: uber_secret
```

networks 关键字确保服务所有副本都会连接到 `front-tier` 网络。网络相关定义必须位于顶级关键字 networks 之下，如果定义的网络不存在，Docker 会以 Overlay 网络方式新建一个网络。

（2）database 服务

数据库服务也在 Stack 文件中定义了，包括镜像、网络以及密钥。除上述内容之外，数据库服务还引入了环境变量和部署约束。

```yaml
database:
  image: dockersamples/atsea_db
  environment:
    POSTGRES_USER: gordonuser
    POSTGRES_DB_PASSWORD_FILE: /run/secrets/postgres_password
    POSTGRES_DB: atsea
  networks:
    - back-tier
  secrets:
    - postgres_password
  deploy:
    placement:
      constraints:
        - 'node.role == worker'
```

environment 关键字允许在服务副本中注入环境变量。在该服务中，使用了 3 个环境变量来定义数据库用户、数据库密码的位置（挂载到每个服务副本中的密钥）以及数据库服务的名称。

```yaml
environment:
  POSTGRES_USER: gordonuser
  POSTGRES_DB_PASSWORD_FILE: /run/secrets/postgres_password
  POSTGRES_DB: atsea
```

注：

> 将三者作为密钥传递会更安全，因为这样可以避免将数据库名称和数据库用户以明文变量的方式记录在文件当中。

该服务还在 deploy 关键字下定义了部署约束。这样保证了当前服务只会运行在 Swarm 集群的 worker 节点之上。

```yaml
deploy:
  placement:
    constraints:
      - 'node.role == worker'
```

部署约束是一种拓扑感知定时任务，是一种很好的优化调度选择的方式。Swarm 目前允许通过如下几种方式进行调度。

- 节点 ID，如 `node.id==o2p4kw2uuw2a` 。
- 节点名称，如 `node.hostname==worker12` 。
- 节点角色，如 `node.role!=manager` 。
- 节点引擎标签，如 `engine.labels.operatingsystem==ubuntu16.04` 。
- 节点自定义标签，如 `node.labels.zone==prod1` 。

注意==和！=操作符均支持。

（3）appserver 服务

`appserver` 服务使用了一个镜像，连接到 3 个网络，并且挂载了一个密钥。此外 `appserver` 服务还在 `deploy` 关键字下引入了一些额外的特性。

```yaml
appserver:
  image: dockersamples/atsea_app
  networks:
    - front-tier
    - back-tier
    - payment
  deploy:
    replicas: 2
    update_config:
      parallelism: 2
    failure_action: rollback
    placement:
      constraints:
        - 'node.role == worker'
  restart_policy:
    condition: on-failure
    delay: 5s
    max_attempts: 3
    window: 120s
secrets:
  - postgres_password
```

接下来进一步了解 `deploy` 关键字中新增的内容。

首先，`services.appserver.deploy.replicas = 2` 设置期望服务的副本数量为 2。缺省情况下，默认值为 1。如果服务正在运行，并且需要修改副本数，则读者需要显示声明该值。这意味着需要更新 stack 文件中的 `services.appserver.deploy.replicas` ，设置一个新值，然后重新部署当前 stack。后面会进行具体展示，但是重新部署 stack 并不会影响那些没有改动的服务。

`services.appserver.deploy.update_config` 定义了 Docker 在服务滚动升级的时候具体如何操作。对于当前服务，Docker 每次会更新两个副本（parallelism），并且在升级失败后自动回滚。回滚会基于之前的服务定义启动新的副本。`failure_action` 的默认操作是 `pause` ，会在服务升级失败后阻止其他副本的升级。`failure_action` 还支持 `continue` 。

```yaml
update_config:
  parallelism: 2
  failure_action: rollback
```

`services.appserver.deploy.restart-policy` 定义了 Swarm 针对容器异常退出的重启策略。当前服务的重启策略是，如果某个副本以非 0 返回值退出（`condition: onfailure`），会立即重启当前副本。重启最多重试 3 次，每次都会等待至多 120s 来检测是否启动成功。每次重启的间隔是 5s。

```yaml
restart_policy:
  condition: on-failure
  delay: 5s
  max_attempts: 3
  window: 120s
```

（4）visualizer 服务

visualizer 服务中指定了镜像，定义了端口映射规则、更新配置以及部署约束。此外还挂载了一个指定卷，并且定义了容器的优雅停止方式。

```yaml
visualizer:
  image: dockersamples/visualizer:stable
  ports:
    - '8001:8080'
  stop_grace_period: 1m30s
  volumes:
    - '/var/run/docker.sock:/var/run/docker.sock'
  deploy:
    update_config:
      failure_action: rollback
    placement:
      constraints:
        - 'node.role == manager'
```

当 Docker 停止某个容器的时候，会给容器内部 PID 为 1 的进程发送 `SIGTERM` 信号。容器内 PID 为 1 的进程会有 10s 的优雅停止时间来执行一些清理操作。如果进程没有处理该信号，则 10s 后就会被 `SIGKILL` 信号强制结束。`stop_grace_period` 属性可以调整默认为 10s 的优雅停止时长。

`volumes` 关键字用于挂载提前创建的卷或者主机目录到某个服务副本当中。在本例中，会挂载 Docker 主机的 `/var/run/docker.sock` 目录到每个服务副本的 `/var/run/docker.sock` 路径。这意味着在服务副本中任何对 `/var/run/docker.sock` 的读写操作都会实际指向 Docker 主机的对应目录中。

`/var/run/docker.sock` 恰巧是 Docker 提供的 IPC 套接字，Docker daemon 通过该套接字对其他进程暴露其 API 终端。这意味着如果给某个容器访问该文件的权限，就是允许该容器接收全部的 API 终端，即等价于给予了容器查询和管理 Docker daemon 的能力。在大部分场景下这是决不允许的。但是，这是一个实验室环境中的示例应用。

该服务需要 Docker 套接字访问权限的原因是需要以图形化方式展示当前 Swarm 中服务。为了实现这个目标，当前服务需要能访问管理节点的 Docker daemon。为了确保能访问管理节点 Docker daemon，当前服务通过部署约束的方式，强制服务副本只能部署在管理节点之上，同时将 Docker 套接字绑定挂载到每个服务副本中。绑定挂载如下图所示：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191231-1577784985375)

（5）payment_gateway 服务

`payment_gateway` 服务中指定了镜像，挂载了一个密钥，连接到网络，定义了部分部署策略，并且使用了两个部署约束：

```yaml
payment_gateway:
  image: dockersamples/atseasampleshopapp_payment_gateway
  secrets:
    - source: staging_token
      target: payment_token
  networks:
    - payment
  deploy:
    update_config:
      failure_action: rollback
    placement:
      constraints:
        - 'node.role == worker'
        - 'node.labels.pcidss == yes'
```

除了部署约束 `node.label` 之外，其余配置项在前面都已经出现过了。通过 `docker node update` 命令可以自定义节点标签，并添加到 Swarm 集群的指定节点。因此，`node.label` 配置只适用于 Swarm 集群中指定的节点上（不能用于单独的容器或者不属于 Swarm 集群的容器之上）。

在本例中，`payment_gateway` 服务被要求只能运行在符合 PCI DSS（支付卡行业标准，译者注）标准的节点之上。为了使其生效，读者可以将某个自定义节点标签应用到 Swarm 集群中符合要求的节点之上。本实验在搭建应用部署实验环境的时候完成了该操作。

因为当前服务定义了两个部署约束，所以服务副本只会部署在两个约束条件均满足的节点之上，即具备 `pcidss=yes` 节点标签的 worker 节点。

关于 Stack 文件的分析到这里就结束了，目前对于应用需求应该有了较好的理解。前文中提到，Stack 文件是应用文档化的重要部分之一。读者已经了解该应用包含 5 个服务、3 个网络以及 4 个密钥。此外读者还知道了每个服务都会连接到哪个网络、有哪些端口需要发布、应用会使用到哪些镜像以及哪些服务需要在特定的节点上发布。

下面开始部署。



## 部署应用

在部署应用之前，有几个前置处理需要完成。

- **Swarm 模式**：应用将采用 Docker Stack 部署，而 Stack 依赖 Swarm 模式。
- **标签**：某个 Swarm worker 节点需要自定义标签。
- **密钥**：应用所需的密钥需要在部署前创建完成。

## 搭建应用实验环境

##### 以下操作需购买并创建三个云主机以完成操作

在本节中会完成基于 Linux 的三节点 Swarm 集群搭建，同时能满足上面应用的全部前置依赖。完成之后，实验环境如下图所示：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191231-1577785074868)

接下来内容分为 3 个步骤。

（1）创建新的 Swarm。

（2）添加节点标签。

（3）创建密钥。

首先创建新的三节点 Swarm 集群。

（1）初始化 Swarm。

在读者期望成为 Swarm 管理节点的机器上，运行下面的命令：

```bash
root@manager1:~# docker swarm init
Swarm initialized: current node (qviobhp1xlia4dfotdpwaw0o7) is now a manager.

To add a worker to this swarm, run the following command:

    docker swarm join --token SWMTKN-1-05c4p4kyltyumxkjdp95b7xcgnzantwf1p190nuzufl73as16x-37cx0jhyyc4963x45qmmx1jcz 172.16.109.89:2377

To add a manager to this swarm, run 'docker swarm join-token manager' and follow the instructions.
```

（2）添加工作节点。

复制前面输出中出现的 `docker swarm join` 命令。将复制内容粘贴到工作节点上并运行：

```bash
//Worker 1 (worker1)
root@worker1:~# docker swarm join --token SWMTKN-1-05c4p4kyltyumxkjdp95b7xcgnzantwf1p190nuzufl73as16x-37cx0jhyyc4963x45qmmx1jcz 172.16.109.89:2377
This node joined a swarm as a worker.

//Worker 2 (worker2)
root@worker2:~# docker swarm join --token SWMTKN-1-05c4p4kyltyumxkjdp95b7xcgnzantwf1p190nuzufl73as16x-37cx0jhyyc4963x45qmmx1jcz 172.16.109.89:2377
This node joined a swarm as a worker.
```

（3）确认当前 Swarm 由一个管理节点和两个工作节点构成。在管理节点中运行下面的命令：

```bash
root@manager1:~# docker node ls
ID                            HOSTNAME            STATUS              AVAILABILITY        MANAGER STATUS      ENGINE VERSION
qviobhp1xlia4dfotdpwaw0o7 *   manager1            Ready               Active              Leader              19.03.5
tlswpppdb0rtz83l6x7dubl2j     worker1             Ready               Active                                  19.03.5
gkieede0k0gq664be5gmarirb     worker2             Ready               Active                                  19.03.5
```

Swarm 集群目前就绪。

`payment_gateway` 服务配置了部署约束，限制该服务只能运行在有 `pcidss=yes` 标签的工作节点之上。本步骤中将在 `worker1` 上添加该节点标签。

在现实世界中，添加该标签之前必须将某个 Docker 节点按 PCI 规范进行标准化。但是，这只是一个实验环境，所以就暂且跳过这一过程，直接将标签添加到 `worker1` 节点。

在 Swarm 管理节点运行下面的命令。

（1）添加节点标签到 worker1。

```bash
$ docker node update --label-add pcidss=yes worker1
```

Node 标签只在 Swarm 集群之内生效。

（2）确认节点标签：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191231-1577785389677)

`worker1` 工作节点现在已经配置完成，所以该节点可以运行 `payment_gateway` 服务副本了。

应用定义了 4 个密钥，这些都需要在应用部署前创建。

- `postgress_password`。
- `staging_token`。
- `revprox_cert`。
- `revprox_key`。

在管理节点运行下面的命令，来创建这些密钥。

（1）创建新的键值对。

密钥中有 3 个是需要加密 key 的。在本步骤中会创建加密 key，下一步会将加密 key 放到 Docker 密钥文件当中。

```bash
$ openssl req -newkey rsa:4096 -nodes -sha256 \
  -keyout domain.key -x509 -days 365 -out domain.crt
```

（2）创建 `revprox_cert`、`revprox_key` 以及 `postgress_password` 密钥。

```bash
root@manager1:~# docker secret create revprox_cert domain.crt
tth6t9od8y6sif9d3shlqzd0o

root@manager1:~# docker secret create revprox_key domain.key
4fqrcauqy5sfdci04u562axba

root@manager1:~# docker secret create postgres_password domain.key
j80xjmnoy43jt03dydq5xibpc
```

（3）创建 stage_token 密钥：

```bash
root@manager1:~# echo staging | docker secret create staging_token -
7l1i9w2xjn4b8za8i99qp0qu0
```

（4）列出所有密钥。

```bash
root@manager1:~# docker secret ls
ID                          NAME                DRIVER              CREATED              UPDATED
j80xjmnoy43jt03dydq5xibpc   postgres_password                       48 seconds ago       48 seconds ago
tth6t9od8y6sif9d3shlqzd0o   revprox_cert                            About a minute ago   About a minute ago
4fqrcauqy5sfdci04u562axba   revprox_key                             56 seconds ago       56 seconds ago
7l1i9w2xjn4b8za8i99qp0qu0   staging_token                           18 seconds ago       18 seconds ago
```

上面已经完成了全部的前置准备。是时候开始部署应用了！

## 部署示例应用



如果还没有代码，请先复制应用的 GitHub 仓库到 Swarm 管理节点。

```bash
$ git clone https://github.com/dockersamples/atsea-sample-shop-app.git
Cloning into 'atsea-sample-shop-app'...
remote: Counting objects: 636, done.
Receiving objects: 100% (636/636), 7.23 MiB | 3.30 MiB/s, done. remote:
Total 636 (delta 0), reused 0 (delta 0), pack-reused 636 Resolving
deltas: 100% (197/197), done.
Checking connectivity... done.

$ cd atsea-sample-shop-app
```

现在已经拥有了源码，可以开始部署应用了。

Stack 通过 `docker stack deploy` 命令完成部署。基础格式下，该命令允许传入两个参数。

- Stack 文件的名称。
- Stack 的名称。

应用的 GitHub 仓库中包含一个名为 docker-stack.yml 的 Stack 文件。这里会使用该文件。本实验中为 Stack 起名 seastack，如果读者不喜欢，也可以选择其他名称。

在 Swarm 管理节点的 `atsea-sample-shop-app` 目录下运行下面的命令部署 Stack 应用：

```bash
$ docker stack deploy -c docker-stack.yml seastack
Creating network seastack_default
Creating network seastack_back-tier
Creating network seastack_front-tier
Creating network seastack_payment
Creating service seastack_database
Creating service seastack_appserver
Creating service seastack_visualizer
Creating service seastack_payment_gateway
Creating service seastack_reverse_proxy
```

读者可以运行 `docker network ls` 以及 `docker service ls` 命令来查看应用的网络和服务情况。

下面是命令输出中几个需要注意的地方。

网络是先于服务创建的。这是因为服务依赖于网络，所以网络需要在服务启动前创建。

Docker 将 Stack 名称附加到由他创建的任何资源名称前作为前缀。在本例中，Stack 名为 `seastack` ，所以所有资源名称的格式都如：`seastack_` 。例如，`payment` 网络的名称是 `seastack_payment` 。而在部署之前创建的资源则没有被重命名，比如密钥。

另一个需要注意的点是出现了新的名为 `seastack_default` 的网络。该网络并未在 Stack 文件中定义，那为什么会创建呢？每个服务都需要连接到网络，但是 `visualizer` 服务并没有指定具体的网络。因此，Docker 创建了名为 `seastack_default` 的网络，并将 `visualizer` 连接到该网络。

读者可以通过两个命令来确认当前 Stack 的状态。`docker stack ls` 列出了系统中全部 Stack，包括每个 Stack 下面包含多少服务。`docker stack ps` 针对某个指定 Stack 展示了更详细的信息，例如期望状态以及当前状态。下面一起来了解下这两条命令：

```bash
root@manager1:~/atsea-sample-shop-app# docker stack ls
NAME                SERVICES            ORCHESTRATOR
seastack            5                   Swarm
root@manager1:~/atsea-sample-shop-app# docker stack ps seastack
ID                  NAME                         IMAGE                                                     NODE
DESIRED STATE       CURRENT STATE                    ERROR                       PORTS
sq6z9eaopxa9        seastack_reverse_proxy.1     dockersamples/atseasampleshopapp_reverse_proxy:latest     manager1            Running             Running 5 minutes ago
r9c3vmr5acdb        seastack_database.1          dockersamples/atsea_db:latest                             worker1             Running             Running 4 minutes ago
21quzk4f3esh        seastack_payment_gateway.1   dockersamples/atseasampleshopapp_payment_gateway:latest   worker1             Running             Running 5 minutes ago
vxky2b23aipf        seastack_visualizer.1        dockersamples/visualizer:stable                           manager1            Running             Running 5 minutes ago
ev4bndyjqw4x        seastack_appserver.2         dockersamples/atsea_app:latest                            worker1             Running             Preparing 6 minutes ago
```

在服务启动失败时，`docker stack ps` 命令是首选的问题定位方式。该命令展示了 Stack 中每个服务的概况，包括服务副本所在节点、当前状态、期望状态以及异常信息。从下面的输出信息中能看出 `reverse_proxy` 服务在 `worker2` 节点上两次尝试启动副本失败：

```bash
$ docker stack ps seastack
NAME                NODE      DESIRED    CURRENT ERROR
                              STATE      STATE
reverse_proxy.1     worker2     Shutdown   Failed  "task: non-zero exit (1)"
\_reverse_proxy.1   worker2     Shutdown   Failed  "task: non-zero exit (1)"
```

如果想查看具体某个服务的详细信息，可以使用 `docker service logs` 命令。读者需要将服务名称/ID 或者副本 ID 作为参数传入。如果传入服务名称或 ID，读者可以看到所有服务副本的日志信息。如果传入的是副本 ID，读者只会看到对应副本的日志信息。

下面的 `docker service logs` 命令展示了 `seastack_reverse_proxy` 服务的全部副本日志，其中包含了前面输出中的两次副本启动失败的日志：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191231-1577786769754)

输出内容为了适应页面展示，已经经过裁剪，但是读者还是可以看到全部 3 个服务副本的日志（两个启动失败，1 个正在运行）。每行的开始都是副本的名称，包括服务名称、副本序号、副本 ID 以及副本所在主机的名称。接下来是具体的日志输出。

注：

> 读者可能已经注意到前面日志中全部副本的序号都是 1。这是因为 Docker 每次只创建一个副本，并且只有当前面的副本启动失败时才会创建新的。

因为输出内容经过裁剪，所以具体原因很难明确，但看起来前两次副本启动失败原因是其依赖的某个服务仍然在启动中（一种启动时服务间依赖导致的竞争条件）。

读者可以继续跟踪日志（`--follow`），查看日志尾部内容（`--tail`），或者获取额外的详细信息（`--details`）。

现在 Stack 已经启动并且处于运行中，看一下如何管理 stack。



## 管理应用


管理应用



Stack 是一组相关联的服务和基础设施，需要进行统一的部署和管理。虽然这句话里充斥着术语，但仍提醒我们 Stack 是由普通的 Docker 资源构建而来：网络、卷、密钥、服务等。这意味着可以通过普通的 Docker 命令对其进行查看和重新配置，例如 `docker network`、`docker volume`、`docker secret`、`docker service` 等。

在此前提之下，通过 `docker service` 命令来管理 Stack 中某个服务是可行的。一个简单的例子是通过 `docker service scale` 命令来扩充 `appserver` 服务的副本数。但是，这并不是推荐的方式！

推荐方式是通过声明式方式修改，即将 Stack 文件作为配置的唯一声明。这样，所有 Stack 相关的改动都需要体现在 Stack 文件中，然后更新重新部署应用所需的 Stack 文件。

下面是一个简单例子，阐述了为什么通过命令修改的方式不好（通过 CLI 进行变更）。

> 假设读者已经部署了一个 Stack，采用的 Stack 文件是前面章节中从 GitHub 复制的仓库中的`docker-stack.yml`。这意味着目前 appserver 服务有两个副本。如果通过`docker service scale`命令将副本修改为 4 个，当前运行的集群会有 4 个副本，但是 Stack 文件中仍然是两个。得承认目前看起来还不是特别糟糕。但是，假设读者又通过修改 Stack 文件对 Stack 做了某些改动，然后通过`docker stack deploy`命令进行滚动部署。这会导致`appserver`服务副本数被回滚到两个，因为 Stack 文件就是这么定义的。因此，推荐对 Stack 所有的变更都通过修改 Stack 文件来进行，并且将该文件放到一个合适的版本控制系统当中。

一起来回顾对 Stack 进行两个声明式修改的过程。目标是进行如下改动。

- 增加 `appserver` 副本数，数量为 2 ～ 10。
- 将 `visualizer` 服务的优雅停止时间增加到 2min。

修改 docker-stack.yml 文件，更新两个值：`services.appserver.deploy.replicas=10` 和 `services.visualizer.stop_grace_period=2m` 。

目前，Stack 文件中的内容如下。

```yaml
<Snip>
appserver:
  image: dockersamples/atsea_app
  networks:
    - front-tier
    - back-tier
    - payment
  deploy:
    replicas: 10             <<Updated value
<Snip>
visualizer:
  image: dockersamples/visualizer:stable
  ports:
    - "8001:8080"
stop_grace_period: 2m        <<Updated value
<Snip>
```

保存文件并重新部署应用：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191231-1577786981826)

以上重新部署应用的方式，只会更新存在变更的部分。

运行 `docker stack ps` 命令来确认 `appserver` 副本数量确实增加：

```bash
$ docker stack ps seastack
NAME                    NODE  DESIRED STATE CURRENT STATE
seastack_visualizer.1   manager1 Running       Running 1 second ago
seastack_visualizer.1   manager1 Shutdown      Shutdown 3 seconds ago
seastack_appserver.1    worker2 Running       Running 24 minutes ago
seastack_appserver.2    worker1 Running       Running 24 minutes ago
seastack_appserver.3    worker2 Running       Running 1 second ago
seastack_appserver.4    worker1 Running       Running 1 second ago
seastack_appserver.5    worker2 Running       Running 1 second ago
seastack_appserver.6    worker1 Running       Starting 7 seconds ago
seastack_appserver.7    worker2 Running       Running 1 second ago
seastack_appserver.8    worker1 Running       Starting 7 seconds ago
seastack_appserver.9    worker2 Running       Running 1 second ago
seastack_appserver.10   worker1 Running       Starting 7 seconds ago
```

为了排版效果，输出内容有所裁剪，只展示了受变更影响的服务。

注意关于 `visualizer` 服务有两行内容。其中一行表示某个副本在 3s 前停止，另一行表示新副本已经运行了 1s。这是因为刚才对 `visualizer` 服务作了修改，所以 Swarm 集群终止了正在运行的副本，并且启动了新的副本，新副本中更新了 `stop_grace_period` 的值。

还需要注意的是，`appserver` 服务目前拥有 10 个副本，但不同副本的“CURRENT STATE”一列状态并不相同：有些处于 running 状态，而有些仍在 starting 状态。

经过足够的时间，集群的状态会完成收敛，期望状态和当前状态就会保持一致。在那时，集群中实际部署和观察到的状态，就会跟 Stack 文件中定义的内容完全一致。这真是让人开心的事情。

所有应用/Stack 都应采用该方式进行更新。所有的变更都应该通过 Stack 文件进行声明，然后通过 `docker stack deploy` 进行部署。

正确的删除某个 Stack 方式是通过 `docker stack rm` 命令。一定要谨慎！删除 Stack 不会进行二次确认：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191231-1577787187743)

注意，网络和服务已经删除，但是密钥并没有。这是因为密钥是在 Stack 部署前就创建并存在了。在 Stack 最上层结构中定义的卷同样不会被 `docker stack rm` 命令删除。这是因为卷的设计初衷是保存持久化数据，其生命周期独立于容器、服务以及 Stack 之外。

恭喜！读者现在学会了如何通过 Docker Stack 部署和管理一个多服务应用。



## 总结

- `docker stack deploy` 用于根据 Stack 文件（通常是 `docker-stack.yml`）部署和更新 Stack 服务的命令。
- `docker stack ls` 会列出 Swarm 集群中的全部 Stack，包括每个 Stack 拥有多少服务。
- `docker stack ps` 列出某个已经部署的 Stack 相关详情。该命令支持 Stack 名称作为其主要参数，列举了服务副本在节点的分布情况，以及期望状态和当前状态。
- `docker stack rm` 命令用于从 Swarm 集群中移除 Stack。移除操作执行前并不会进行二次确认。

Stack 是 Docker 原生的部署和管理多服务应用的解决方案。Stack 默认集成在 Docker 引擎中，并且提供了简单的声明式接口对应用进行部署和全生命周期管理。

在本章开始提供了应用代码以及一些基础设施需求，比如网络、端口、卷和密钥。接下来的内容完成了应用的容器化，并且将全部应用服务和基础设施需求集成到一个声明式的 Stack 文件当中。在 Stack 文件中设置了服务副本数、滚动升级以及重启策略。然后通过 `docker stack deploy` 命令基于 Stack 文件完成了应用的部署。

对于已部署应用的更新操作，应当通过修改 Stack 文件完成。首先需要从源码管理系统中检出 Stack 文件，更新该文件，然后重新部署应用，最后将改动后的 Stack 文件重新提交到源码控制系统中。

因为 Stack 文件中定义了像服务副本数这样的内容，所以读者需要自己维护多个 Stack 文件以用于不同的环境，比如 dev、test 以及 prod。



