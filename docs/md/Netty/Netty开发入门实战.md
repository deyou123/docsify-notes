# Netty 开发入门实战

# 1. IO 基础入门

一、实验简介



在学习网络编程之前，需要先了解下网络 I/O 的基本概念，这节课会介绍 Linux 下常见的几种网络 I/O 模型，以及它们之间的区别，本节内容没有代码，目的是理解网络 I/O 相关概念。

#### 知识点

- Linux I/O 模型介绍
- 阻塞和非阻塞
- 同步和异步

二、Linux 网络 I/O 模型



在介绍 I/O 模型之前，先来说说什么是 I/O。根据维基百科的定义，I/O 指的是输入输出，通常指数据在内部存储器和外部存储器或其他周边设备之间的输入和输出。简而言之，从硬盘中读写数据或者从网络上收发数据，都属于 I/O 行为。

Linux 系统将所有的外部设备都看作一个文件来看待，所有打开的文件都通过文件描述符(简称 fd)来引用。而对一个 socket 的读写也会有相应的描述符，称为 socket 描述符(简称 socketfd)。描述符是一个非负整数，它指向内核中的一个结构体，由系统内核返回给进程。

在 Linux 内核中，I/O 操作通常包含两个阶段：

1. 内核等待数据准备好
2. 从内核复制数据到进程中

根据在这两个阶段的不同处理，Linux 提供了以下 5 种不同的 I/O 模型：

- 阻塞 I/O 模型
- 非阻塞 I/O 模型
- I/O 复用模型
- 信号驱动式 I/O 模型
- 异步 I/O 模型

下面以 linux socket 编程的 recvfrom 函数作为系统调用来说明 I/O 模型。recvfrom 函数类似于标准的 read 函数，它的作用是从指定的套接字中读取数据报。recvfrom 会从应用进程空间运行切换到内核空间中运行，一段时间后会再切换回来。

- 阻塞 I/O 模型：最常用的 I/O 模型是阻塞 I/O 模型，也是最简单的模型，示意图如下：

  ![阻塞 I/O 模型](https://doc.shiyanlou.com/document-uid934942labid9921timestamp1551778170895.png)

  可以看到应用进程调用 recvfrom，会切换到内核空间中运行，直到数据报到达且被复制到应用进程缓冲区中才返回。进程从调用 recvfrom 开始到它返回的整段时间内是被阻塞的，因此被称为阻塞 I/O。

- 非阻塞 I/O 模型：把 socket 设置为非阻塞的话，在调用 recvfrom 时，如果数据没准备就绪，进程并不会阻塞，而是由内核直接返回一个错误，示意图如下：

  ![非阻塞 I/O 模型](https://doc.shiyanlou.com/document-uid934942labid9921timestamp1551778248638.png)

  在非阻塞 I/O 模式下，进程要轮询数据是否准备就绪，而这个轮询操作会消耗大量 cpu 时间。

- I/O 复用模型：Linux 提供 select、poll，进程可以通过将 fd 传递给 select 或 poll 系统调用，阻塞在 select 操作上，这样 select、poll 就可以监听多个 fd 是否处于就绪状态。不过 select 和 poll 存在缺陷，要按顺序扫描 fd 状态，并且监听的 fd 数量有限。不过 linux 还提供了一个 epoll , epoll 使用基于事件驱动的方式代替顺序扫描，且 fd 数量不受限制，在有数据准备就绪时会立即触发一个 callback ，这是目前最流行的 I/O 模型，示意图如下： ![I/O 复用模型](https://doc.shiyanlou.com/document-uid934942labid9921timestamp1551778130046.png)

  当调用 select 函数时，会阻塞在此函数，当有 fd 处于就绪状态时，调用 recvfrom 把数据报复制到应用进程缓冲区。这样看起来好像和阻塞 I/O 模型很像，但是区别在于一个 select 可以监听多个 fd。

- 信号驱动式 I/O 模型：这个模型用的比较少，这里就直接略过。

- 异步 I/O 模型：异步 I/O 的机制是告知内核启动某个操作，由内核来完成操作(包括把内核数据复制到进程缓冲区)，应用进程只需要等待内核通知操作完成即可，示意图如下：

  ![异步 I/O 模型](https://doc.shiyanlou.com/document-uid934942labid9921timestamp1551778149976.png)

  从图片可以看到使用异步 I/O 模型，应用进程不会出现阻塞，全部的操作都交给内核处理。

常用的 I/O 模型就此介绍完了，如果大家感兴趣，想深入网络编程的话，推荐去看看《UNIX 网络编程》，当然对大多数 JAVA 程序员来说，不需要了解的那么深入，只要了解以下这些概念就好。



三、阻塞和非阻塞



讲到网络编程就总会涉及到阻塞和非阻塞、同步和异步这两组概念。这些概念面试也会经常问到，编者也在网上看了很多资料，发现没有几个能清晰地表达出它们之间的区别和联系，甚至有很多不同的观点，这里也来讲下编者的理解：

- 阻塞：进程/线程在调用一个函数时，会阻塞函数直至处理完毕。可以发现上面的阻塞 I/O 模型和 I/O 复用模型其实都是阻塞的模型
- 非阻塞：和阻塞相反，不需要阻塞函数至处理完毕即可往下执行，对应上面的非阻塞 I/O 模型和异步 I/O 模型

四、同步和异步



同步和异步是最容易理解出错的地方，很多人可能会错把同步理解成阻塞，异步理解成非阻塞，其实这两个看似相同但却不能混为一谈。同步和异步关注的是**通知机制**，同步的话就是在发出调用后一直等待结果返回(所以说同步必然是通过阻塞函数来实现)，而异步则是发出调用后等待被调用者通知，这里把网络 I/O 模型的概念套上去就是，进程是调用者而内核是被调用者。

所以到底是同步还是异步需要看具体场景，比如把阻塞 I/O 模型封装，用多线程去实现阻塞 I/O 的异步调用，也可以对外宣称 I/O 框架是异步非阻塞的。当然这是编者的理解，这些概念性的东西也没有一个绝对正确的观点，这里只是抛砖引玉，有兴趣的同学可以自己 google 搜索下相关的资料看看。



五、实验总结



通过本实验的学习，了解了 Linux 上常用的几种 I/O 模型，并且简单讲解了阻塞和非阻塞、同步和异步的概念，这些知识在后续的课程中会用代码进行更详细的讲解，相信大家在这一节的铺垫下,学习后续的内容会有更透彻的理解。

#### 参考资料

- [图解 UNIX 的 I/O 模型](https://blog.csdn.net/lihao21/article/details/51620374)

# 2 Java 网络编程

一、实验简介



上一节实验从底层描述了网络 I/O 的基本概念，这节会讲如何在 JAVA 中使用不同的 I/O 模型进行网络开发。

#### 知识点

- BIO 编程
- NIO 编程
- AIO 编程

二、网络编程概念



网络编程的基本模型就是 C/S (client / server) 模型，一般都是用于不同进程之间的通讯。其中服务器提供 IP+端口，客户端通过服务器的 IP+端口发起连接请求，通过 TCP 协议进行三次握手建立连接，成功之后即可通过 socket (网络套接字)进行通讯。



三、传统的 BIO 编程



在 JDK1.4 之前，JAVA 只存在一种 I/O 类库，就是 BIO，这是最好理解的 I/O 模型（对应阻塞 I/O 模型）。在 BIO 模型下，由于 I/O 操作都是阻塞的，所以每个客户端连接都会在服务器中用一个单独的线程做处理，示意图如下：

![此处输入图片的描述](https://doc.shiyanlou.com/document-uid934942labid9921timestamp1551778306475.png)

当一个客户端连接成功时，服务器就会开启一个线程去处理，该模型的最大问题就是非常消耗 CPU，要知道线程资源是很宝贵的，如果客户端连接一旦多起来，线程数会急剧增加，CPU 上下文切换频繁，可能导致进程卡死或直接宕机，也可能造成 JVM 线程栈内存溢出导致程序崩溃。

接下来以一个时间服务器为例，用代码来熟悉一下 BIO 编程。时间服务器业务是由客户端发出`Get Date`请求，然后服务器返回当前时间，否则返回请求格式有误。



3.1 创建 Maven 项目



在 Web IDE 中调出控制台 ![此处输入图片的描述](https://doc.shiyanlou.com/document-uid934942labid9921timestamp1551778335315.png) 在下面的控制台中输入以下命令构造一个 JAVA 项目。

```shell
mvn archetype:generate -DgroupId=com.shiyanlou -DartifactId=netty-hello2 -Dversion=1.0-SNAPSHOT -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeCatalog=internal
```

参数说明：

- archetypeArtifactId ：使用的 maven 骨架名称；

- groupId : 项目的组织机构，也是包的目录结构，一般都是域名的倒序，比如 `com.shiyanlou`；

- artifactId ：项目实际的名字，这里取名`netty-hello2`；

- version ：项目的版本号；

- archetypeCatalog:catalog选择在哪里取，此处设置internal是避免从远程服务器上取catalog

  > 避免命令行卡在`Generating project in Interactive mode`位置不动

maven 会自动下载相关环境文件，等待一段时间后提示当前创建项目的基本信息，回车确认后结果如下: ![此处输入图片的描述](https://doc.shiyanlou.com/document-uid934942labid9921timestamp1551778359974.png)

看到已经生成了 maven 项目，目录如下: ![此处输入图片的描述](https://doc.shiyanlou.com/document-uid934942labid9921timestamp1551778419146.png)

创建好项目之后，需要将工作空间切换到刚创建的项目中(即 netty-hello 目录)(必须切换到该目录，否则不会有自动提示和导包等功能)，选择 File->Open WorkSpace，找到刚才创建的 netty-hello 项目，点击 open，就可成功切换。

切换成功之后，Web IDE 会自动生成一些配置文件（可能需要一点时间）。最后的文件目录如下： ![此处输入图片的描述](https://doc.shiyanlou.com/document-uid934942labid9921timestamp1551778437700.png)

由于 Web IDE 的限制，这可能和平时开发不太一样，不过没关系，把重点放在代码编写上。在后续的实验中，都会以这种方式初始化。



3.2 修改 pom.xml



打开 pom.xml 文件，在`project`节点上插入以下内容：

```xml
<properties>
  <!-- 使用java8的编译器来进行编译 -->
  <maven.compiler.target>1.8</maven.compiler.target>
  <maven.compiler.source>1.8</maven.compiler.source>
</properties>
```

最终 pom.xml 代码如下：

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.shiyanlou</groupId>
  <artifactId>netty-hello2</artifactId>
  <packaging>jar</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>netty-hello2</name>
  <url>http://maven.apache.org</url>

  <properties>
    <!-- 使用java8的编译器来进行编译 -->
    <maven.compiler.target>1.8</maven.compiler.target>
    <maven.compiler.source>1.8</maven.compiler.source>
  </properties>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>
```

3.3 创建 TimeServer



在刚创建的项目中`com.shiyanlou`包下新建一个`bio` 包，并在新创建的包下创建一个 `TimeServer.java` 文件，代码如下：

```java
package com.shiyanlou.bio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class TimeServer {

  public void bind(int port) {
    try (
        //启动tcp服务器并监听指定端口
        ServerSocket serverSocket = new ServerSocket(port)
    ) {
      System.out.println("服务器启动成功");
      //通过死循环来监听客户端连接
      while (true) {
        System.out.println("等待客户端连接");
        //accept方法会阻塞到有客户端连接进来
        Socket socket = serverSocket.accept();
        System.out.println("客户端连接成功");
        //开启一个线程去处理对应的客户端连接
        new Thread(new TimeServerHandle(socket)).start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  class TimeServerHandle implements Runnable {

    private Socket socket;

    public TimeServerHandle(Socket socket) {
      this.socket = socket;
    }

    public void run() {
      try (
          //拿到客户端的输入、输出流
          BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
      ) {
        String line;
        //循环读取一行字符串，如果非空则一直读
        while ((line = reader.readLine()) != null) {
          System.out.println("接收到客户端数据：" + line);
          //判断客户端输入的字符串是否为Get Date
          if (line.equalsIgnoreCase("Get Date")) {
            //输出服务器时间字符串
            writer.write(new Date().toString());
          } else {
            //提示客户端请求有误
            writer.write("Bad Request");
          }
          writer.newLine();
          //刷新缓冲区
          writer.flush();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) {
    //启动服务器
    new TimeServer().bind(8989);
  }
}
```

上面的代码，主要是以下几步：

1. 通过`ServerSocket`类来指定端口并开启 tcp 服务。
2. 用`accept`方法来接收客户端的连接，该方法在没有客户端连接到服务器时会一直阻塞。
3. 当有客户端连接到服务器时，使用一个线程去处理客户端对应的`socket`，因为是无限循环，这样主线程就又回到了第二步，继续等待新客户端连接。
4. 在客户端连接成功后返回了一个`Socket`对象，此对象有两个非常重要的方法就是`getInputStream`和`getOutputStream`，这两个方法分别可以拿到客户端对应的输入流(InputStream)和输出流(OutputStream)，通过`BufferedReader`和`BufferedWriter`两个类对它们进行包装，然后通过`readLine`方法读取客户端发送过来的数据，该方法也会阻塞，直到客户端发送了一行字符串。
5. 读取到客户端发来的字符串，判断是不是`Get Date`，是的话就通过`BufferedWriter`向客户端输出服务器当前时间，否则输出`Bad Request`，提示客户端输入有误，然后继续等待客户端输入。



3.4 创建 TimeClient



接着是客户端 TimeClient 的实现，客户端相对于服务器就比较简单点，在`com.shiyanlou.bio`包下新建一个 `TimeClient.java` 文件，代码如下：

```java
package com.shiyanlou.bio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class TimeClient {

  public void connect(String ip, int port) {
    try (
        Socket socket = new Socket(ip, port);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
    ) {
      while (true) {
        System.out.print("请输入要发送的数据：");
        String input = new Scanner(System.in).nextLine();
        writer.write(input);
        writer.newLine();
        writer.flush();
        String response = reader.readLine();
        System.out.println("接收到服务器的数据：" + response);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    new TimeClient().connect("127.0.0.1", 8989);
  }
}
```

与服务器稍微不同的是，客户端直接使用`Socket`类，来连接指定的服务器，这里也讲解下客户端的代码，主要是以下几步：

1. 通过`Socket`类的构造方法来连接指定服务器，这里也会阻塞直到连接成功。
2. 连接成功后返回一个`Socket`对象，同样使用`BufferedReader`和`BufferedWriter`两个类对输入输出流进行包装。
3. 通过`Scanner`获取用户输入，并将输入的字符串发送给服务器。
4. 读取服务器发送过来的数据并打印到控制台。

3.5 运行代码



由于 Web IDE 的限制，不能直接通过 IDE 来运行 JAVA 代码，这里用 maven 工具来编译和运行代码。

使用 maven 编译项目,在终端运行以下命令： `mvn clean && mvn compile`

接着启动服务器，运行 TimeServer 的 main 方法，在终端中运行以下命令： `mvn exec:java -Dexec.mainClass="com.shiyanlou.bio.TimeServer"`

等到控制台输出代码里打印的**服务器启动成功**即可。

然后开启一个新的终端，运行 TimeClient： `mvn exec:java -Dexec.mainClass="com.shiyanlou.bio.TimeClient"`

客户端启动会连接到服务器，连接成功后会打印出**请输入要发送的数据：**

这个时候先发送一条错误的数据，看看服务器返回的结果：

![此处输入图片的描述](https://doc.shiyanlou.com/document-uid934942labid9921timestamp1551778477354.png)

再发送正确的请求`Get Date`：

![img](https://doc.shiyanlou.com/document-uid934942labid9921timestamp1551778541207.png)

可以看到和预期的结果一样，通过这一节对 JAVA 网络编程的简单认识，可以看到 BIO 的代码写起来非常简单，但由于客户端连接和服务器线程数是 1:1 的关系，在客户端连接非常大时，服务器很容易就出现 cpu 瓶颈，进而导致服务器卡顿或宕机等问题，接下来看看 JAVA 中怎么使用 NIO 解决这个问题。



四、NIO 编程



NIO 编程是在 JDK1.4 之后引入的，NIO 相对于之前的 I/O 库是新增的，所以官方称它为 New I/O，NIO 对应 I/O 复用模型，来简单回顾下上节实验内容：

> I/O 复用模型：Linux 提供 select、poll,进程可以通过将 fd 传递给 select 或 poll 系统调用，阻塞 select 操作，这样 select、poll 就可以监听多个 fd 是否处于就绪状态。

对此在 JAVA NIO 中引进了一个 `Selector` 的类，负责注册和分发 I/O 事件，一般称之为多路复用器，另外还提供了`Buffer`和`Channel`两个类来配合使用，下面就来简单了解下 JAVA NIO 相关的概念和功能。



4.1 Buffer



Buffer 从名字就可以看出来是缓冲的意思，它支持写入和读取数据,与传统 BIO 不同，BIO 是面向流的 I/O，对数据的读写都是直接通过流(Stream)进行。而在 NIO 中所有数据的读写都是通过缓冲区(Buffer)进行。

JDK 提供了非常多的 Buffer 类型，具体如下：

- ByteBuffer
- CharBuffer
- ShortBuffer
- IntBuffer
- LongBuffer
- FloatBuffer
- DoubleBuffer

可以看到 JAVA 的基本类型除了 boolean ，都有与之对应的 Buffer 类。在网络编程中用到最多的就是 ByteBuffer。



4.2 Channel



Channel 是一个管道，有点类似于传统 BIO 中的流，但是流只能支持一个方向的数据传输(读或者写)，而 Channel 同时支持读和写，Channel 可以分为两大类，一类是用于网络读写的`SelectableChannel`，另一类是用于文件读写的`FileChannel`。在接下来的 NIO 编程中使用到的就是`SelectableChannel`的子类`ServerSocketChannel`和`SocketChannel`。



4.3 Selector



前面说了 Selector 是一个负责 I/O 事件的注册、分发组件，把多个 Channel 的 I/O 事件都注册在 Selector 上，通过 Selector 来分发，这样就可以用一个 Selector 同时处理多个 Channel，这样的话就可以在一个线程上处理成千上万个客户端的 I/O 事件。



4.4 使用 NIO 编写 TimeServer



在刚刚创建的项目中`com.shiyanlou`包下新建一个`nio` 包，并在新创建的包下创建一个 `TimeServer.java` 文件，代码如下：

```java
package com.shiyanlou.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class TimeServer {

  public void bind(int port) {
    try (
        //打开一个ServerSocketChannel
        ServerSocketChannel acceptChannel = ServerSocketChannel.open();
    ) {
      //设置成非阻塞，才能配合Selector使用
      acceptChannel.configureBlocking(false);
      //绑定指定端口
      acceptChannel.bind(new InetSocketAddress(port));
      //创建Selector多路复用器
      Selector selector = Selector.open();
      //注册accept事件到多路复用器上
      acceptChannel.register(selector, SelectionKey.OP_ACCEPT);
      //轮询多路复用器，查看是否有准备就绪的I/O事件
      while (selector.select() > 0) {
        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = keys.iterator();
        while (iterator.hasNext()) {
          //取出key之后从Selector中移除掉
          SelectionKey key = iterator.next();
          iterator.remove();
          if (key.isAcceptable()) {
            //如果是通知accept就绪
            System.out.println("客户端连接成功");
            ServerSocketChannel tempServerChannel = (ServerSocketChannel) key.channel();
            //取出客户端连接的Channel
            SocketChannel socketChannel = tempServerChannel.accept();
            //设置成非阻塞
            socketChannel.configureBlocking(false);
            //注册read事件到多路复用器上
            socketChannel.register(selector, SelectionKey.OP_READ);
          } else if (key.isReadable()) {
            //如果是通知read就绪
            SocketChannel socketChannel = (SocketChannel) key.channel();
            //新建一个ByteBuffer用于读取
            ByteBuffer buffer = ByteBuffer.allocate(8192);
            int size = socketChannel.read(buffer);
            if (size == -1) {
              //客户端关闭了连接
              socketChannel.close();
              return;
            }
            buffer.flip();
            byte[] bts = new byte[buffer.remaining()];
            buffer.get(bts);
            String str = new String(bts);
            System.out.println("接收到客户端数据：" + str);
            buffer.clear();
            //判断客户端输入的字符串是否为Get Date
            if (str.equalsIgnoreCase("Get Date")) {
              //输出服务器时间字符串
              buffer.put(new Date().toString().getBytes());
            } else {
              //提示客户端请求有误
              buffer.put("Bad Request".getBytes());
            }
            buffer.flip();
            socketChannel.write(buffer);
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    new TimeServer().bind(8989);
  }
}
```

来讲解下上面的代码：

1. 通过`ServerSocketChannel.oepn()`方法构造一个`ServerSocketChannel`实例。
2. 使用`configureBlocking(false)`方法把 Channel 设置为非阻塞模式，不然无法配合接下来的`Selector`进行 I/O 事件的分发。
3. 绑定指定的端口号，开启服务器。
4. 通过`Selector.open()`方法构造一个`Selector`实例，将`ServerSocketChannel`中 accpet 的事件注册进去。
5. 通过`selector.select()`方法，来监听是否有准备好的 I/O 事件，该方法会一直阻塞，直到注册的 I/O 事件准备就绪，通过`while(true)`的方式无限循环来读取准备就绪的 I/O 事件。
6. 当有准备好的 I/O 事件时，可以用`selector.selectedKeys()`方法拿到所有相关的 I/O 事件 key，再通过轮询所有的 key 来做对应的处理，取出来之后需要将 key 从`Selector`中移除，避免下次轮询中重复出现。
7. 通过`key.isAcceptable()`方法判断是否为 accpet 就绪，是的话调用`key.channel()`拿到该事件相关的 Channel，这里 accpet 对应的 Channel 为`ServerSocketChannel`所以把它转成对应的类，通过`ServerSocketChannel.accpet()`方法可以拿到客户端连接相关的`SocketChannel`实例。
8. 接着将`SocketChannel`也设置成非阻塞模式，并将 read 事件注册到`Selector`中，这样在客户端发送数据过来时，`Selecotr`就能分发对应的事件过来。
9. 通过`key.isReadable()`方法判断是否为 read 就绪，是的话拿到相关的`Channel`,然后这里就要使用到`ByteBuffer`了，通过`ByteBuffer.allocate(8192)`方法，分配了一个容量为 8192 字节的缓冲区，通过`Channel.read()`方法，将数据读取到 Buffer 中，read()方法会返回读取到的字节数长度，如果为-1 的话表示客户端主动关闭了连接。否则就处理接下来的业务。
10. 使用`buffer.flip()`方法，将 buffer 切换成读模式，之后初始化一个 byte[]读取 buffer 中的数据。
11. 读取到客户端传入的数据之后做对应的处理，这里逻辑和之前 BIO 是一样的，不同的地方就是用了 Buffer 来写入给客户端。通过`Buffer.clear()`方法，清空缓冲区，再写入返回给客户端的 byte[]，通过`Channel.write()`方法写入到客户端，这样一个与客户端的简单通讯就完成了。

可以看到在 NIO 中主要通过`Channel`、`Selector`、`Buffer`配合使用来完成编写，重点就是`Selector`的使用，要理解它就是用来注册 I/O 事件，然后通过 select()方法来通知这些事件准备就绪了，再通过 Channel 和 Buffer 来处理对应的 I/O 事件。



4.5 使用 NIO 编写 TimeClient



接着是的客户端 TimeClient 的实现，客户端相对于服务器就比较简单点了，在`com.shiyanlou.nio`包下新建一个 `TimeClient.java` 文件，代码如下：

```java
package com.shiyanlou.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class TimeClient {

  public void bind(String ip, int port) {
    try (
        //打开一个SocketChannel
        SocketChannel connectChannel = SocketChannel.open();
    ) {
      //设置成非阻塞
      connectChannel.configureBlocking(false);
      //连接指定服务器
      connectChannel.connect(new InetSocketAddress(InetAddress.getByName(ip), port));
      //创建Selector多路复用器
      Selector selector = Selector.open();
      //注册connect事件到多路复用器上
      connectChannel.register(selector, SelectionKey.OP_CONNECT);
      //轮询多路复用器，查看是否有准备就绪的I/O事件
      while (selector.select() > 0) {
        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = keys.iterator();
        while (iterator.hasNext()) {
          //取出key之后从Selector中移除掉
          SelectionKey key = iterator.next();
          iterator.remove();
          if (key.isConnectable()) {
            //如果是通知connect就绪
            SocketChannel socketChannel = (SocketChannel) key.channel();
            socketChannel.configureBlocking(false);
            //判断connect是否完成
            if (socketChannel.finishConnect()) {
              commonWrite(socketChannel);
              //注册read事件到多路复用器上
              socketChannel.register(selector, SelectionKey.OP_READ);
            }
          } else if (key.isReadable()) {
            //新建一个ByteBuffer用于读取
            SocketChannel socketChannel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(8192);
            int size = socketChannel.read(buffer);
            if (size == -1) {
              //客户端关闭了连接
              socketChannel.close();
              return;
            }
            buffer.flip();
            byte[] bts = new byte[buffer.remaining()];
            buffer.get(bts);
            System.out.println(new String(bts));
            commonWrite(socketChannel);
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void commonWrite(SocketChannel socketChannel) throws IOException {
    System.out.print("请输入要发送的数据：");
    String input = new Scanner(System.in).nextLine();
    ByteBuffer buffer = ByteBuffer.allocate(8192);
    buffer.put(input.getBytes());
    buffer.flip();
    socketChannel.write(buffer);
  }

  public static void main(String[] args) {
    new TimeClient().bind("127.0.0.1", 8989);
  }
}
```

客户端的代码其实和服务器很类似，区别在于客户端是通过构造`SocketChannel`并通过`connect`方法来构造连接，接着注册 connect 事件到`Selector`上，当连接就绪时通过 SelectorKey 的`isConnectable()`方法来判断是否为连接就绪事件。这里要注意一点的是要通过`finishConnect()`方法来判断是否连接成功，当这个方法返回 true 的时候才是真正的连接成功。

在连接成功后封装了一个`commonWrite`方法用于客户端数据发送，发送完之后注册一个 read 事件到`Selector`上，用于监听服务器。向客户端写入数据的时候，把读到的数据通过`ByteBuffer`读取出来，转成 String 输出。



4.6 运行代码



按照 3.5 步骤的方式来编译和运行代码，注意相关的类路径的修改。



4.7 NIO 总结



NIO 编程的复杂度是非常的高，而且上面的应用还没考虑到 TCP 的粘包和拆包问题，也没考虑到异常处理和优雅停止服务器。

但是 NIO 编程的性能是非常好，不需要为每个客户端连接分配一个线程去处理，而是通过`Selecotr`解耦了 I/O 事件的分发，使得应用可以在一个线程中处理大量的连接和 I/O 事件。



五、 AIO 编程



在 JDK1.7 中引入了 NIO2.0 类库，也就是对应这节要学习的 AIO 编程，意味着 JAVA 开始支持真正的异步 I/O 模型，AIO 就对应着 异步 I/O 模型，来简单回顾下：

> 异步 I/O 模型：异步 I/O 的机制是告知内核启动某个操作，然后由内核来完成操作(包括把内核数据复制到进程数据)，应用进程只需要等待内核通知操作完成即可

在之前 NIO 编程中，其实不算真正的异步，因为在`Selector.select()`方法的执行时，还是会阻塞住.在 AIO 中就不存在`Selecotr`了，由内核直接通知给进程应用，通过回调的方式来编写。



5.1 AIO 编写 TimeServer



这里直接以代码的方式来展现 AIO 相关类库，在刚刚创建的项目中`com.shiyanlou`包下新建一个`aio` 包，并在新创建的包下创建一个 `TimeServer.java` 文件，代码如下：

```java
package com.shiyanlou.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class TimeServer {

  private CountDownLatch countDownLatch = new CountDownLatch(1);

  public void bind(int port) {
    try (
        //打开一个AsynchronousServerSocketChannel
        AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open();
    ) {
      //绑定指定端口
      serverSocketChannel.bind(new InetSocketAddress(port));
      //监听accept事件
      doAccept(serverSocketChannel);
      try {
        //因为上面的方法都是非阻塞的，这里需要阻塞住主线程不然程序就直接退出了
        countDownLatch.await();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void doAccept(AsynchronousServerSocketChannel serverSocketChannel) {
    //调用accept方法，通过实现一个CompletionHandler接口以回调的方式来进行编码，该方法是非阻塞的
    serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
      @Override
      public void completed(AsynchronousSocketChannel socketChannel, Void attachment) {
        //当accept完成时，aio框架会自动调用此方法，接着再调用一次doAccept方法，因为一次accept的调用只会触发一次
        doAccept(serverSocketChannel);
        //监听read事件
        doRead(socketChannel);
        System.out.println("客户端连接");
      }

      @Override
      public void failed(Throwable exc, Void attachment) {
        exc.printStackTrace();
        countDownLatch.countDown();
      }
    });
  }

  private void doRead(AsynchronousSocketChannel socketChannel) {
    //新建一个ByteBuffer用于读取
    ByteBuffer buffer = ByteBuffer.allocate(8192);
    //调用read方法，也是通过实现一个CompletionHandler接口以回调的方式来进行编码，该方法是非阻塞的
    socketChannel.read(buffer, socketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
      @Override
      public void completed(Integer result, AsynchronousSocketChannel socketChannel) {
        //当read完成时，aio框架会自动调用此方法，接着再调用一次doRead方法，原理同上
        doRead(socketChannel);
        try {
          if (result == -1) {
            //客户端关闭了连接
            socketChannel.close();
          } else {
            buffer.flip();
            byte[] bts = new byte[buffer.remaining()];
            buffer.get(bts);
            String str = new String(bts);
            System.out.println("接收到客户端数据：" + str);
            buffer.clear();
            //判断客户端输入的字符串是否为Get Date
            if (str.equalsIgnoreCase("Get Date")) {
              //输出服务器时间字符串
              buffer.put(new Date().toString().getBytes());
            } else {
              //提示客户端请求有误
              buffer.put("Bad Request".getBytes());
            }
            buffer.flip();
            socketChannel.write(buffer);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void failed(Throwable exc, AsynchronousSocketChannel socketChannel) {
        try {
          socketChannel.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }

  public static void main(String[] args) {
    new TimeServer().bind(8989);
  }
}
```

可以看到在 AIO 中，所有的 I/O 操作都是以回调函数的方式来编写的，懂 js 的同学应该会觉得很熟悉。其中主要就是通过 Channel 配合一个`CompletionHandler`接口来实现异步回调，下面来分析下代码：

1. 使用`AsynchronousServerSocketChannel`来作为 AIO 服务器启动相关的 Channel。
2. 通过调用`accpet()`方法，配合一个实现`CompletionHandler`接口的匿名内部类来编写回调实现，该接口有两个方法一个是`completed`,即 I/O 事件完成之后 AIO 框架会触发该方法，还有一个是`failed`在发生异常时触发。
3. 在 accpet 完成的回调方法里再一次调用了`doAccpet()`，因为每调用一次 accpet 只会产生一次回调，为了处理更多的客户端就要在 accpet 完成之后继续 accpet 以处理后续的客户端连接。
4. 然后同理又封装了一个`doRead()`方法，用来监听客户端的输入，区别就在于`read`需要声明一个`ByteBuffer`去接收客户端的输入，再接收完之后也需要重新调用一遍`doRead()`方法，以处理后续的客户端输入。
5. 读取到客户端传入的数据之后做对应的处理，逻辑还是和以前一样，这里就不再提了。
6. 因为调用 Channel 的`accept()`和`read()`方法都是异步非阻塞的，所以使用了一个`CountDownLatch`对象来阻塞主主线程，防止主线程运行完了程序就直接结束。

5.2 AIO 编写 TimeClient



接着是客户端 TimeClient 的实现，理解了上面服务器的编程方式，客户端实现也差不多是一样的，在`com.shiyanlou.nio`包下新建一个 `TimeClient.java` 文件，代码如下：

```java
package com.shiyanlou.aio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class TimeClient {

  private CountDownLatch countDownLatch = new CountDownLatch(1);

  public void connect(String ip, int port) {
    try (
        //打开一个AsynchronousSocketChannel
        AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
    ) {
      //绑定指定端口
      socketChannel.connect(new InetSocketAddress(InetAddress.getByName(ip), port), null, new CompletionHandler<Void, Object>() {
        @Override
        public void completed(Void result, Object attachment) {
          //当connect完成
          doRead(socketChannel);
          //客户端输入处理
          doWrite(socketChannel);
        }

        @Override
        public void failed(Throwable exc, Object attachment) {
          try {
            //连接失败
            socketChannel.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      });
      try {
        //因为上面的方法都是非阻塞的，这里需要阻塞住主线程不然程序就直接退出了
        countDownLatch.await();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void doRead(AsynchronousSocketChannel socketChannel) {
    //新建一个ByteBuffer用于读取
    ByteBuffer buffer = ByteBuffer.allocate(8192);
    //调用read方法，也是通过实现一个CompletionHandler接口以回调的方式来进行编码，该方法是非阻塞的
    socketChannel.read(buffer, socketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
      @Override
      public void completed(Integer result, AsynchronousSocketChannel socketChannel) {
        //当read就绪时，aio框架会自动调用此方法，接着再调用一次doRead方法，原理同上
        doRead(socketChannel);
        try {
          if (result == -1) {
            //服务器关闭了连接
            socketChannel.close();
          } else {
            buffer.flip();
            byte[] bts = new byte[buffer.remaining()];
            buffer.get(bts);
            System.out.println("接收到服务器的数据：" + new String(bts));
            doWrite(socketChannel);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void failed(Throwable exc, AsynchronousSocketChannel socketChannel) {
        try {
          socketChannel.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }

  private void doWrite(AsynchronousSocketChannel socketChannel) {
    System.out.print("请输入要发送的数据：");
    String input = new Scanner(System.in).nextLine();
    ByteBuffer buffer = ByteBuffer.allocate(8192);
    buffer.put(input.getBytes());
    buffer.flip();
    socketChannel.write(buffer);
  }

  public static void main(String[] args) {
    new TimeClient().connect("127.0.0.1", 8989);
  }
}
```

可以看到 AIO 下的客户端与服务器很类似，区别在于使用的`AsynchronousSocketChannel`来创建一个 AIO 的客户端 Channel，之后通过异步回调的方式调用`connect()`和`read()`方法来监听客户端连接成功和读取数据，这里因为客户端只需要连接一次服务器，所以没有重复调用`doConnect()`方法，然后把客户端输入处理封装了一个`doWrite()`方法用来复用。



5.3 运行代码



按照 3.5 步骤的方式来编译和运行代码，注意将相关的类路径修改。



5.4 AIO 总结



AIO 相对于 NIO 来说更好理解一点，不需要用到`Selecotr`多路复用器，但回调的写法稍微有点复杂。 AIO 是真正对应了操作系统底层的异步 I/O 模型，这可以带来更高的性能提升。



六、实验总结



通过本实验可以看到 jdk 原生的网络编程除了 BIO 之外，NIO 和 AIO 都是比较复杂的，需要熟练掌握`Channel`、`Selecotr`、`Buffer`等类的使用，另外还需要对多线程非常熟悉，不然无法开发出高质量的网络应用，下一节实验开始学习`netty`，看看`netty`是如何解决以上痛点的。



# 3 Netty 入门

二、为什么选择 netty



上节实验讲了用 JAVA 原生 API 进行网络开发，会发现使用原生 API 开发会存在以下几个痛点：

- NIO 和 AIO 的 API 使用复杂

- 性能优化难度高，需要熟练掌握多线程编程和网络编程

- 开发工作量大，在处理常见的网络编程问题时都可能需要重新造轮子，例如：客户端断网自动重连、TCP 半包处理、异常处理等等。

- JDK NIO 的 BUG，例如臭名昭著的 epoll bug，它会导致 Selector 空轮询，最终导致 CPU 100%。官方声称在 JDK1.6 版本的 update18 修复了该问题，但是直到 JDK1.7 版本该问题仍旧存在，只不过该 BUG 发生概率降低了一些而已，并没有被根本解决。

  > 该 BUG 以及相关的问题可以参见以下链接内容。 https://bugs.java.com/bugdatabase/view_bug.do?bug_id=2147719 [https://bugs.java.com/bugdatabase/view_bug.do?bug_id=6403933](https://bugs.java.com/bugdatabase/view_bug.do?bug_id=2147719)

后来 netty 出现了，那么 netty 是什么？netty 本质上只是 Jboss 的一个 jar 包，目的是快速开发和构建高性能的网络服务器和客户端，netty 的出现解决了上面所有的痛点。netty 底层封装了 JAVA NIO 和 Reactor 模式，它的健壮性、功能、性能、易用性、易扩展性都是同类型框架中首屈一指的，netty 是互联网中间件领域使用最广泛最核心的网络通信框架，例如：Dubbo,Hadoop,Spark,RocketMQ。几乎所有主流的中间件或大数据领域都离不开 netty，掌握 netty 是迈向高级工程师的重要技能之一。

这里抛出一个问题：netty 为什么没有使用 AIO 作为底层实现？

可能很多人在上一节课学习到 AIO 是性能最好的编程模型，为什么 netty 用的却是 NIO 呢。这里要说一下，在 netty4.0 早期，作者加入了 AIO 模型，但是后来发现在 Linux 系统下 AIO 并不比 NIO 快，而且还会加入不必要的线程模型，导致代码复杂度提高，所以作者后来放弃了 AIO。

> 相关内容可参考：https://github.com/netty/netty/issues/2515


三、开发环境准备



这一节将搭建一个 netty 开发环境。

3.1 创建 Maven 项目



在 Web IDE 中调出控制台

![此处输入图片的描述](https://doc.shiyanlou.com/document-uid934942labid9921timestamp1551778889623.png)

在下面的控制台中用 maven 骨架构造一个 java 项目。

```bash
mvn archetype:generate -DgroupId=com.shiyanlou -DartifactId=netty-hello3 -Dversion=1.0-SNAPSHOT -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeCatalog=internal
```

参数说明：

- archetypeArtifactId ：使用的 maven 骨架名称；
- groupId : 项目的组织机构，也是包的目录结构，一般都是域名的倒序，比如 `com.shiyanlou`；
- artifactId ：项目实际的名字，这里取名`netty-hello3`；
- version ：项目的版本号；

输入命令后，maven 会自动下载相关环境文件，需要等待一段时间，接着 maven 会提示当前要创建项目的基本信息，然后直接回车确认。 结果如下: ![此处输入图片的描述](https://doc.shiyanlou.com/document-uid934942labid9921timestamp1551778922493.png)

可以看到已经生成了一个 maven 项目，目录如下: ![此处输入图片的描述](https://doc.shiyanlou.com/document-uid934942labid9921timestamp1551778953397.png)

创建好项目后，需要将工作空间切换到创建的项目中(即 netty-hello3 目录)(必须切换到该目录，否则不会有自动提示和导包等功能)，选择 File->Open WorkSpace，找到刚才创建的 netty-hello3 项目，选择它，点击 open，就可以成功切换了。

切换成功后，Web IDE 会自动生成一些配置文件（可能需要一点时间）。最后的文件目录如下： 



![此处输入图片的描述](https://doc.shiyanlou.com/document-uid934942labid9921timestamp1551778972079.png)

3.2 修改 pom.xml 文件



打开 pom.xml 文件，在`project`节点上插入以下内容：

```xml
<properties>
  <!-- 使用java8的编译器来进行编译 -->
  <maven.compiler.target>1.8</maven.compiler.target>
  <maven.compiler.source>1.8</maven.compiler.source>
</properties>
```

在`dependencies`节点中间插入以下内容：

```xml
<!-- 引入netty依赖 -->
<dependency>
  <groupId>io.netty</groupId>
  <artifactId>netty-all</artifactId>
  <version>4.1.31.Final</version>
</dependency>
```

最终代码如下：

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.shiyanlou</groupId>
  <artifactId>netty-hello3</artifactId>
  <packaging>jar</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>netty-hello3</name>
  <url>http://maven.apache.org</url>
  <properties>
    <!-- 使用java8的编译器来进行编译 -->
    <maven.compiler.target>1.8</maven.compiler.target>
    <maven.compiler.source>1.8</maven.compiler.source>
  </properties>
  <dependencies>
    <!-- 引入netty依赖 -->
    <dependency>
      <groupId>io.netty</groupId>
      <artifactId>netty-all</artifactId>
      <version>4.1.31.Final</version>
    </dependency>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>
```

四、开发第一个 netty 应用



接下来开发第一个 netty 应用了，先用 netty 实现一个简单的 Echo Server，把从客户端接收到的数据原样返回给客户端。(在此之前也可以思考下用 JAVA NIO 怎么实现)

4.1 创建 EchoServer.java



在 shiyanlou 下新建一个 EchoServer.java 文件，把以下代码复制进去。

```java
package com.shiyanlou;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {

  public static void main(String[] args) {
    //处理客户端连接(accept)的线程池
    NioEventLoopGroup boosGroup = new NioEventLoopGroup();
    //处理IO事件(read、write)的线程池
    NioEventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      //netty服务器启动辅助类
      ServerBootstrap bootstrap = new ServerBootstrap()
          .group(boosGroup, workerGroup)  //传入之前的两个NioEventLoopGroup实例
          .channel(NioServerSocketChannel.class)  //使用NioServerSocketChannel作为底层实现
          .childHandler(new ChannelInitializer() {

            protected void initChannel(Channel channel) throws Exception {

              //自定义handle处理数据
              channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {

                @Override
                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                  //当读取到客户端数据时，直接原样写入到客户端
                  ctx.write(msg);
                  //刷新缓冲区，将数据立即发送给客户端
                  ctx.flush();
                  //或者直接使用 ctx.writeAndFlush(msg);
                }
              });
            }
          });
      //绑定9999端口并开始接收客户端连接
      ChannelFuture f = bootstrap.bind(9999).sync();
      System.out.println("服务器启动成功");
      //阻塞当前线程，直到服务器关闭
      f.channel().closeFuture().sync();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      //优雅关闭服务器
      boosGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }
}
```

重点关注`new ChannelInboundHandlerAdapter(){}`这个匿名内部类，通过重写此类中`channelRead`方法，可以在服务器读取客户端消息时进行对应的处理，其中有两个参数(ChannelHandlerContext ctx, Object msg)：

1. **ctx**:可以用它提供的方法写入消息到客户端。
2. **msg**:就是接收到的客户端消息，默认是`ByteBuf`类型,`ByteBuf`是 netty 提供的类似于 JDK 中 java.nio.ByteBuffer 接口，但它提供了更强大更灵活的功能，是 netty 最原始的通讯单位。

4.2 运行 EchoServer.java



在终端中使用 maven 运行：

```bash
mvn clean && mvn compile && mvn exec:java  -Dexec.mainClass="com.shiyanlou.EchoServer"
```

首次启动时，maven 会自动下载相关环境文件，等待一段时间后控制台输出代码里打印的**服务器启动成功**



4.3 使用 telnet 工具测试



telnet 是一个 TCP 客户端工具，可以很方便用它测试。不过由于 Web Ide 中未安装此工具，需要手动安装，打开一个新的终端窗口，输入以下命令：

```bash
sudo apt-get update && sudo apt-get install telnet
```

安装成功后，用 telnet 就能连上服务器，发送数据测试：

```bash
telnet 127.0.0.1 9999
```

telnet 连接指定服务器 IP 和端口号就能发送 tcp 请求，连接成功后结果如下：

![此处输入图片的描述](https://doc.shiyanlou.com/document-uid934942labid9921timestamp1551779015767.png)

接着输入任意字符后按回车发送给服务器，可以看到服务器原样返回了发送的字符串：



4.4 第一个 netty 应用总结



到这里就编写好了一个简单的 Echo Server，可以看到使用 netty 来写网络应用，只需很少的代码就可实现 NIO 服务器的开发，并且还使用了 Reactor 线程模型，相比传统的 NIO 开发，代码量大大减少，开发难度也降低了很多。



五、实现一个 Calc Server 和 Calc Client



这一节用 netty 实现一个简单的计算器服务器和客户端，目标是服务器可以接收客户端发出的简单计算公式(两个整数之间的加减乘除运算)，并返回计算结果给客户端，大概流程如下：

![此处输入图片的描述](https://doc.shiyanlou.com/document-uid934942labid9921timestamp1551779060124.png)

5.1 Calc Server 开发



通过上一节知道，netty 实现业务需要继承一个`ChannelInboundHandlerAdapter`类，通过`channel.pipeline().addLast()`方法添加到 handler 列表中，之前是直接以匿名内部类的方式创建的对象，这次新建一个`CalcServerHandler`类来继承它，以此解耦业务代码。



章节

步骤

报告

讨论



5.2 创建 CalcServer.java



在刚创建的项目中`com.shiyanlou`包下面新建一个`server`包，创建一个`CalcServer.java`文件，代码如下：

```java
package com.shiyanlou.server;

import com.shiyanlou.server.handler.CalcServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class CalcServer {

  public void bind(int port) {
    NioEventLoopGroup boosGroup = new NioEventLoopGroup();
    NioEventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap bootstrap = new ServerBootstrap()
          .group(boosGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .childHandler(new ChannelInitializer() {

            protected void initChannel(Channel channel) throws Exception {

              //自定义计算器handler类
              channel.pipeline().addLast(new CalcServerHandler());
            }
          });
      ChannelFuture f = bootstrap.bind(port).sync();
      System.out.println("服务器启动成功");
      f.channel().closeFuture().sync();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      boosGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }

  public static void main(String[] args) {
    new CalcServer().bind(9999);
  }
}
```

代码和上一节的区别，把`handler`解耦出来了，业务处理的核心也在这个`handler`类里

5.3 创建 CalcServerHandler.java



接下来重点是创建一个处理计算请求的 handler 类，在刚刚创建的`server`包下创建一个`handler`包，并创建一个`CalcServerHandler.java`文件，代码如下：

```java
package com.shiyanlou.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalcServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    System.out.println("有新的客户端连接至服务器");
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    //拿到客户端传来的消息
    ByteBuf byteBuf = (ByteBuf) msg;
    //根据读到的消息长度创建字节数组
    byte[] data = new byte[byteBuf.readableBytes()];
    //把消息读到字节数组中
    byteBuf.readBytes(data);
    //将读到的消息转成字符串
    String body = new String(data, "utf-8");
    System.out.println("接收到计算公式：" + body);
    //分配一个ByteBuf对象
    ByteBuf response = Unpooled.buffer();
    //使用正则表达式匹配出两个数字和计算符号
    Pattern pattern = Pattern.compile("^(\\d+)([+\\-*/])(\\d+)$");
    Matcher matcher = pattern.matcher(body);
    double result = 0;
    if (matcher.find()) {
      double num1 = Double.valueOf(matcher.group(1));
      double num2 = Double.valueOf(matcher.group(3));
      String symbol = matcher.group(2);
      //根据符号进行相应的计算
      switch (symbol) {
        case "+":
          result = num1 + num2;
          break;
        case "-":
          result = num1 - num2;
          break;
        case "*":
          result = num1 * num2;
          break;
        case "/":
          result = num1 / num2;
          break;
      }
    } else {
      System.out.println("计算公式不正确");
    }
    //将计算结果写入到ByteBuf中
    response.writeBytes((result + "").getBytes());
    //把消息返回到客户端
    ctx.writeAndFlush(response);
  }
}
```

在上面的 java 类里，重写了两个方法，一个是`channelActive`，该方法在有客户端连接到服务器时触发，这里就做个简单的打印提示。另一个是`channelRead`这个方法上节已经讲过了，讲讲里面的业务代码：

1. 首先协议规定的是客户端传一个计算公式，所以把计算公式读取出来，通过`byteBuf.readableBytes()`方法，取到 ByteBuf 可读的字节长度，通过这个长度声明了一个`byte[]`将 ByteBuf 可读的字节读到字节数组中，（_是不是发现和 JDK ByteBuffer 操作非常相似_），然后通过`new String()`方法得到客户端传输的计算公式
2. 拿到计算公式之后，需要将两个数字和计算符号取出来，这里用了一个正则表达式去做匹配，如果计算公式不符合规定，就直接返回结果 0。否则就进行相应的计算。
3. 通过`Unpooled.buffer()`分配一个 ByteBuf 对象，将计算的结果通过 ByteBuf 写入到客户端(*关于 netty ByteBuf 在后续的实验里会有详细讲解*)

5.4 创建 CalcClient.java



在前一节直接使用的`telnet`来测试`Echo Server`，这一节就来用`netty`来实现客户端程序，netty 的客户端开发相对于服务器开发要简单的多，在刚刚创建的项目中`com.shiyanlou`包下面新建一个`client`包，创建一个`CalcClient.java`文件，代码如下：

```java
package com.shiyanlou.client;

import com.shiyanlou.client.handler.CalcClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class CalcClient {

  public void connect(String host, int port) {
    NioEventLoopGroup loopGroup = new NioEventLoopGroup();
    try {
      Bootstrap bootstrap = new Bootstrap().group(loopGroup)
          .channel(NioSocketChannel.class)
          .handler(new ChannelInitializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
              ch.pipeline().addLast(new CalcClientHandler());
            }
          });

      //指定服务器IP和端口发起连接
      ChannelFuture f = bootstrap.connect(host, port).sync();
      //阻塞当前线程，直到客户端关闭
      f.channel().closeFuture().sync();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      //客户端优雅关闭
      loopGroup.shutdownGracefully();
    }
  }

  public static void main(String[] args) {
    new CalcClient().connect("127.0.0.1", 9999);
  }
}
```

客户端代码与服务器的区别在于，客户端只需要一个处理 IO 事件的线程池，使用`Bootstarp`+`NioSocketChannel`来构造客户端程序，处理业务逻辑也是一样通过添加自定义`handler`来做处理，最后调用`connect`方法来连接指定的 IP 和端口。

5.5 创建 CalcClientHandler.java



接下来创建客户端的业务处理`handler`类，客户端业务流程图如下： ![此处输入图片的描述](https://doc.shiyanlou.com/document-uid934942labid9921timestamp1551779114353.png)

然后在刚刚创建的`client`包下新建`handler`包，并创建一个`CalcClientHandler.java`文件，代码如下：

```java
package com.shiyanlou.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.Scanner;

public class CalcClientHandler extends ChannelInboundHandlerAdapter {

  //连接服务器成功时的回调方法
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    System.out.print("请输入计算公式：");
    String body = new Scanner(System.in).next();
    //使用ByteBuf把消息写入到服务器
    ByteBuf byteBuf = Unpooled.buffer();
    byteBuf.writeBytes(body.getBytes());
    ctx.channel().writeAndFlush(byteBuf);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    //拿到服务器传来的消息
    ByteBuf byteBuf = (ByteBuf) msg;
    //根据读到的消息长度创建字节数组
    byte[] data = new byte[byteBuf.readableBytes()];
    //把消息读到字节数组中
    byteBuf.readBytes(data);
    //将读到的消息转成字符串
    String body = new String(data, "utf-8");
    System.out.println("接收到计算结果：" + body);
    System.out.print("是否需要继续计算(Y/N)：");
    String flag = new Scanner(System.in).next();
    //判断是否要继续计算
    if ("Y".equalsIgnoreCase(flag)) {
      channelActive(ctx);
    } else {
      System.out.println("客户端关闭中...");
      //关闭客户端
      ctx.close();
    }
  }
}
```

这里还是继承的`ChannelInboundHandlerAdapter`类，重写了`channelActive`和`channelRead`方法，跟着上面流程图的步骤来讲讲上面的代码：

1. `channelActive`方法表示客户端成功连接到服务器，提示输入计算公式，然后依旧是使用`ByteBuf`把消息写入到服务器。
2. 在服务器接收到客户端消息并计算结果返回后，会触发`channelRead`方法，其中 msg 参数是服务器返回的结果，然后把`ByteBuf`转成字符串打印出来。
3. 接着询问是否要继续计算，是的话就复用`channelActive`方法，否则提示客户端关闭，调用`ctx.close()`方法来关闭此连接。

5.6 运行代码



最终的代码结构入下图所示： ![此处输入图片的描述](https://doc.shiyanlou.com/document-uid934942labid9921timestamp1551779151555.png)

首先用 maven 编译好 class 文件，在终端中运行以下命令：

```bash
mvn clean && mvn compile
```

接着启动服务器，在终端中运行以下命令：

```bash
mvn exec:java -Dexec.mainClass="com.shiyanlou.server.CalcServer"
```

等到控制台输出代码里打印的**服务器启动成功**，如图所示：

![此处输入图片的描述](https://doc.shiyanlou.com/document-uid934942labid9921timestamp1551779174785.png)

接着启动客户端，新开一个终端窗口，运行以下命令：

```bash
mvn exec:java -Dexec.mainClass="com.shiyanlou.client.CalcClient"
```

客户端启动会连接到服务器，连接成功后会打印出**请输入计算公式：**

![此处输入图片的描述](https://doc.shiyanlou.com/document-uid934942labid9921timestamp1551779196749.png)

这个时候随便输入一个计算公式，然后看看服务器的控制台打印情况：

![此处输入图片的描述](https://doc.shiyanlou.com/document-uid934942labid9923timestamp1551858482597.png)

上图表示服务器已经接收到了客户端输入的公式，然后计算返回给客户端：

按 Y 再来计算一次：

![此处输入图片的描述](https://doc.shiyanlou.com/document-uid934942labid9923timestamp1551858500545.png)

![此处输入图片的描述](https://doc.shiyanlou.com/document-uid934942labid9923timestamp1551858515340.png)

可以看到第二次输入的`88/6`也被服务器接收并返回结果给客户端了，现在输入 N 关闭客户端：



六、实验总结



通过本实验可以看到，使用 netty 开发一个 NIO 服务器或客户端是非常简单的，不需要考虑线程的使用，也不需要熟悉 JDK NIO 类库的使用，就可以用几十行代码完成一个网络应用的开发。当然这只是 netty 最基本的用法，后面会通过更多的实例来了解 netty 的强大之处。

#### 参考资料

- [netty 官网](https://netty.io/)
- [netty github](https://github.com/netty/netty)
- 《netty 权威指南》- 李林峰