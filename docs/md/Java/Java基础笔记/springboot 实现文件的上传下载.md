# springboot 实现文件的上传下载

引入依赖

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

## 实现上传功能

新建upload-test.html 页面

```html
<form action="/upload" method="post" enctype="multipart/form-data">
    <input type="file" name="file" />
    <input type="submit" value="文件上传" />
</form>
```

controller

```java
package com.lou.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Controller
public class UploadController {
    //需要手动创建目录文件
    private final static String FILE_UPLOAD_PATH = "D:"+ File.separator+"upload"+File.separator;
    //private final static String FILE_UPLOAD_PATH = "/home/project/upload/";
    //http://localhost:8080/upload-test.html
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "上传失败";
        }
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        StringBuilder tempName = new StringBuilder();
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
        String newFileName = tempName.toString();
        try {
            // 保存文件
            byte[] bytes = file.getBytes();
            //创建文件夹
            File path1 = new File(FILE_UPLOAD_PATH);
            if(!path1.exists()) {//如果文件夹不存在
                path1.mkdir();//创建文件夹
            }
            Path path = Paths.get(FILE_UPLOAD_PATH + newFileName);
            Files.write(path, bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传成功，图片地址为：/files/" + newFileName;
    }
}
```

SpringbootWebConfig.java

```java
@Configuration
public class SpringBootWebMvcConfigurer implements WebMvcConfigurer {
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**").addResourceLocations("file:D:\\upload\\");
        //registry.addResourceHandler("/files/**").addResourceLocations("file:/home/project/upload/");
    }
}
```

访问文件测试数据上传。



