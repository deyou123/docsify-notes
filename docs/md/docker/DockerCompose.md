# Docker compose

Docker Compose 并不是通过脚本和各种冗长的 docker 命令将应用组件组织起来，通过一个声明式的配置文件描述整个应用，从而使用一条命令完成部署。应用部署成功后，还可以通过一系列简单的命令实现对其完整声明周期的管理。甚至，配置文件还可以置于版本控制系统中进行存储和管理。这是显著的进步！

Docker compose 安装

Docker Compose 可用于多种平台。本节将介绍在 Windows、Mac 以及 Linux 上的几种安装方法。当然还有其它的安装方法，不过以下几种足够帮助读者入门。

##### 在 Windows 10 上安装 Docker Compose

在 Windows 10 上运行 Docker 的推荐工具是 Windows 版 Docker（Docker for Windows, DfW)。Docker Compose 会包含在标准 DfW 安装包中。所以，安装 DfW 之后就已经有 Docker Compose 工具了。

在 PowerShell 或 CMD 终端中使用如下命令可以检查 Docker Compose 是否安装成功。

```bash
> docker-compose --version
docker-compose version 1.18.0, build 8dd22a96
```

##### 在 Mac 上安装 Docker Compose

与 Windows 10 一样，Docker Compose 也作为 Mac 版 Docker（Docker for Mac, DfM）的一部分进行安装，所以一旦安装了 DfM，也就安装了 Docker Compose。

在终端中运行如下命令检查 Docker Compose 是否安装：

```bash
$ docker-compose --version
docker-compose version 1.18.0, build 8dd22a96
```

关于安装 Mac 版 Docker 的更多内容请见第 3 章。

##### 在 Windows Server 上安装 Docker Compose

Docker Compose 在 Windows Server 上是作为一个单独的二进制文件安装的。因此，使用它的前提是确保在 Windows Server 上已经正确安装了 Docker。

在 PowerShell 终端中输入如下命令来安装 Docker Compose。为了便于阅读，下面的命令使用反引号（`）来对换行进行转义，从而将多行命令合并。

下面的命令安装的是 `1.18.0` 版本的 Docker Compose，读者请自行选择版本号：[https://github](https://github/). com/docker/compose/releases。只需要将 URL 中的 `1.18.0` 替换为你希望安装的版本即可：

```bash
> Invoke-WebRequest ` "https://github.com/docker/compose/releases/download/1\
.18.0/docker-compose-Windows-x86_64.exe" `
-UseBasicParsing `
-OutFile $Env:ProgramFiles\docker\docker-compose.exe

Writing web request
Writing request stream... (Number of bytes written: 5260755)
```

使用 `docker-compose --version` 命令查看安装情况：

```bash
> docker-compose --version
docker-compose version 1.18.0, build 8dd22a96
```

Docker Compose 安装好了，只要 Windows Server 上安装有 Docker 引擎即可使用。

##### 在 Linux 上安装 Docker Compose

在 Linux 上安装 Docker Compose 分为两步。首先使用 `curl` 命令下载二进制文件，然后使用 `chmod` 命令为其增加可执行权限。

Docker Compose 在 Linux 上的使用，同样需要先安装有 Docker 引擎。当然在我们的实验环境中已经安装了。Compose 需要单独安装，我们需要从 github 上下载 Docker Compose 二进制文件。

但是官网提供的从 github 上下载的链接速度十分缓慢，在实验环境中，我们已提供该文件，直接使用以下命令进行下载：

```bash
$ wget https://labfile.oss-cn-hangzhou.aliyuncs.com/courses/980/software/docker-compose-Linux-x86_64
```

下载成功后，为了能够直接使用该可执行文件执行命令，一般将其放入 `$PATH` 的环境变量支持的路径中，并添加可执行权限，使用的命令如下：

```bash
$ sudo mv docker-compose-Linux-x86_64 /usr/local/bin/docker-compose

$ sudo chmod +x /usr/local/bin/docker-compose
```

执行完成后，就能够在终端下直接使用 `docker-compose` 命令了：现在就可以在 Linux 上使用 Docker Compose 了：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191222-1577006364639)

此外，也可以使用 `pip` 来安装 Docker Compose 的 Python 包。

## Docker Compose 部署应用



接下来将实际部署前文介绍的 Compose 文件中定义的应用。执行如下命令克隆仓库 counter-app 到本地，其中包含我们所需要的文件：

```bash
$ cd ~/Code
$ git clone https://github.com/nigelpoulton/counter-app
$ cd counter-app
$ ls
app.py  docker-compose.yml  Dockerfile  README.md  requirements.txt
```

切换到 counter-app 目录中。该目录包含所需的全部文件，可以作为构建上下文。Docker Compose 会使用目录名 `counter-app` 作为项目名称，这一点在后续的操作中会看到，Docker Compose 会将所有的资源名称中加上前缀 `counter-app_` 。

简要介绍 counter-app 仓库中的这几个文件：

- `app.py` 是应用程序代码（一个 Python Flask 应用）。
- `docker-compose.yml` 是 Compose 文件，其中定义了 Docker 如何部署应用。
- `Dockerfile` 定义了如何构建 `web-fe` 服务所使用的镜像。
- `requirements.txt` 列出了应用所依赖的 Python 包。

请根据需要自行查看文件内容。

`app.py` 显然是应用的核心文件，而 `docker-compose.yml` 文件将应用的所有组件组织起来。

下面使用 Docker Compose 将应用启动起来。终端执行 `docker-compose up &` 命令，执行截图如下所示：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191224-1577176224048)

如上图所示第三个命令，其实是打印信息，因为使用 & 的缘故。

命令执行过程中，会启动 Flask 应用，截图如下所示：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191224-1577176368807)

命令执行将花费大约两分钟时间，其输出非常详尽。关于启动过程会在稍后介绍，我们首先讨论一下 `docker-compose` 命令。

通过 Compose 文件定义的多容器应用称为 “Compose 应用”。常用的启动一个 Compose 应用的方式就是 `docker-compose up` 命令。它会构建所需的镜像，创建网络和卷，并启动容器。

默认情况下，`docker-compose up` 会查找名为 `docker-compose.yml` 或 `docker-compose.yaml` 的 Compose 文件。如果 Compose 文件是其他文件名，则需要通过 `-f` 参数来指定。举个例子，如下命令会基于名为 `prod-equus-bass.yml` 的 Compose 文件部署应用：

```bash
$ docker-compose -f prod-equus-bass.yml up
```

使用 `-d` 参数在后台启动应用也是常见的用法，代码如下：

```bash
$ docker-compose up -d

--OR--

$ docker-compose -f prod-equus-bass.yml up -d
```

前面的示例命令在前台启动应用，没有使用 `-d` 参数，但是使用了 `&` 将终端窗口返回，它的作用是将执行命令的进程隐藏在后台运行。这种用法不太正规，所有的日志还是会直接输出到我们后续可能会用的终端窗口上。

这样应用就构建并启动起来了，可以直接使用 `docker` 命令来查看 Docker Compose 创建的镜像、容器、网络和卷：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191224-1577179107045)

如上图所示，有 4 个镜像，其中最下面的 ubuntu 是启动实验环境时自带的，前 3 个镜像是在部署过程中构建或拉取得到的。

`counterapp_web-fe:latest` 镜像源自 `docker-compose.yml` 文件中的 `build: .` 指令。该指令让 Docker 基于当前目录下的 Dockerfile 来构建一个新的镜像。该镜像基于 `python:3.4-alpine` 构建，其中包含 Python Flask Web 应用的程序代码。更多信息可以通过查看 `Dockerfile` 的内容进行了解：

```yaml
FROM python:3.4-alpine                  # 基础镜像
ADD . /code                             # 将 app 复制到镜像中
WORKDIR /code                           # 设置工作目录
RUN pip install -r requirements.txt     # 安装依赖
CMD ["python", "app.py"]                # 设置默认启动命令
```

为了方便理解，每一行都添加了注释。部署时要删除掉。

请注意，Docker Compose 会将项目名称 `counter-app` 和 Compose 文件中定义的资源名称 `web-fe` 连起来，作为新构建的镜像的名称。Docker Compose 部署的所有资源的名称都会遵循这一规范。

由于 Compose 文件的 `.Services.redis` 项中指定了 `image: "redis:alpine"` ，因此会从 Docker Hub 拉取 `redis:alpine` 镜像。

如下命令列出了两个容器。每个容器的名称都以项目名称（所在目录名称）为前缀。此外，它们还都以一个数字为后缀用于标识容器实例序号——因为 Docker Compose 允许扩缩容：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191224-1577179833952)

`counterapp_web-fe_1` 容器中运行的是应用的 Web 前端。其中执行的是 `app.py` ，并且被映射到了 Docker 主机的 5000 端口，稍后会进行连接。

如下的网络和卷列表显示了名为 counterapp_counter-net 的网络和名为 counterapp_counter-vol 的卷：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191224-1577180783109)

应用部署成功后，读者可以用 Docker 主机的浏览器连接 5000 端口来查看应用的运行效果，如下图所示：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191224-1577185176956)

单击浏览器的刷新按钮，计数会增加。感兴趣的读者可以查看 `app.py` 是如何在 Redis 中存储计数的。

如果使用 `&` 启动应用，那么可以在终端窗口中看到包含 HTTP 响应码 200 的日志。这表明请求收到了正确的响应，每次加载页面都会有日志打印出来：

![图片描述](https://doc.shiyanlou.com/courses/uid310176-20191224-1577181136146)

恭喜！到此为止，多容器的应用已经借助 Docker Compose 成功部署了。

#### Docker Compose 部署和管理命令小结

- `docker-compose up` 命令用于部署一个 Compose 应用。默认情况下该命令会读取名为 `docker-compose.yml` 或 `docker-compose.yaml` 的文件，当然用户也可以使用 `-f` 指定其他文件名。通常情况下，会使用-d 参数令应用在后台启动。
- `docker-compose stop` 命令会停止 Compose 应用相关的所有容器，但不会删除它们。被停止的应用可以很容易地通过 `docker-compose restart` 命令重新启动。
- `docker-compose rm` 命令用于删除已停止的 Compose 应用。它会删除容器和网络，但是不会删除卷和镜像。
- `docker-compose restart` 命令会重启已停止的 Compose 应用。如果用户在停止该应用后对其进行了变更，那么变更的内容不会反映在重启后的应用中，这时需要重新部署应用使变更生效。
- `docker-compose ps` 命令用于列出 Compose 应用中的各个容器。输出内容包括当前状态、容器运行的命令以及网络端口。
- `docker-compose down` 会停止并删除运行中的 Compose 应用。它会删除容器和网络，但是不会删除卷和镜像。



