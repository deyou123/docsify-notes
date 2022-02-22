# NIO 专题

## channel

### Filechannel

### DatagramChannel

发送

![image-20211209145337447](C:/Users/zhaid/AppData/Roaming/Typora/typora-user-images/image-20211209145337447.png)

接受方法

![image-20211209151416046](C:/Users/zhaid/AppData/Roaming/Typora/typora-user-images/image-20211209151416046.png)

通过read write 做 连接测试。

![image-20211209151448090](C:/Users/zhaid/AppData/Roaming/Typora/typora-user-images/image-20211209151448090.png)

![image-20211209151922714](C:/Users/zhaid/AppData/Roaming/Typora/typora-user-images/image-20211209151922714.png)

![image-20211209151946329](C:/Users/zhaid/AppData/Roaming/Typora/typora-user-images/image-20211209151946329.png)

## Buffer

Java NIO Buffers 和 NIO Channels 用来互动。数据从Channel 读入Buffer,  从Buffer 写入Channel.

Buffer 本事上是一个内存块，你可以写入数据，然后你可以再次读取这些数据。内存块被包装在Java NIO Buffer对象中，	

Buffer 的基本用法

* 数据写入
* 调用buffer.slip()
* 数据读取
* 调用buffer.clear() 和 buffer.compact()

当你写入数据到buffer