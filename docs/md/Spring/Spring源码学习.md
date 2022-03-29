# 1. 准备工作

* 源码下载 ` git clone git@gitee.com:deyou123/spring-framework.git`

​		Spring 源码 clone 下来之后，大家在 `spring-framework/gradle/wrapper/gradle-wrapper.properties` 文件中，我们可以看到当前源码使用的 Gradle 版本：

```properties
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-6.5.1-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```

* 安装https://gradle.org/  Spring 源码使用Gradle 6.5.1 版本

* 修改下载源

  默认路径下载依赖构建项目比较慢，所以源码下载下来之后，我们首先进入到 build.gradle 文件中，添加下载源（原有的保留，添加 Alibaba 下载源）：

  ```gradle
  repositories {
      maven{ url 'http://maven.aliyun.com/nexus/content/groups/public/'}
  }
  ```

* 导入idea 项目

