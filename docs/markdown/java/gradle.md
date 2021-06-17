# gradle

# 安装

官网地址https://gradle.org/releases



下载后配置环境变量。和java 配置环境变量一样。

# 构建 build.gradle 脚本

```groovy
task firstgvy{
	doLast{
		println 'hello world'
	}
}
```

# 

```groovy
task maxuan{
	doLast{
		println "Java course"
	}
}

task maxuanNB(dependsOn:maxuan){
	doLast{
		println "NB"
	}
}
```

# 依赖关系

```groovy
task taskX{
	doLast{
		println "taskX"
	}
}

task taskY{
	doLast{
		println "taskY"
	}
}
taskY.dependsOn taskX
```

> gradle -q taskY

# 跳过任务

```groovy
task compile{
    doLast{
        println "compile"
    }
}
compile.doFirst{
  
    if(true){
        throw new StopExecutionException()
    }
}
task myTask(dependsOn:'compile'){
    doLast{
        println "mytask"
    }
}
```

> gradle -q myTask

# gradle 依赖管理

```groovy
apply plugin: 'java'
```

# 脚本插件

other.gradle

```groovy
ext{
    version='1.0'
    url='http:maxuan.cn'
}
```

```groovy

apply from: 'other.gradle'
task test{
    doLast{
        println "version:$version,url:$url"
    }
}
```

> gradle -q test

# 二进制插件（对象插件）

1. 内部插件

	​	

```groovy
apply plugin: JavaPlugin
```

```groovy
plugins{
    id 'java'
}
```

# 自定义插件

```groovy
apply plugin: HelloPlugin

class HelloPlugin implements Plugin<Project>{
    void apply(Project project){
        project.task('hello');
        doLast{
            println "Hello plugins!"
        }
        
    }
}
```

> gradle -q task 
