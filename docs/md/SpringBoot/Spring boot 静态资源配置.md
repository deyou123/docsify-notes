# 1. Spring boot 静态资源配置



默认位置：WebProperties 中可以看到。

```
private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/META-INF/resources/",
      "classpath:/resources/", "classpath:/static/", "classpath:/public/" };
```

还有一个位置再webapp 路径下，优先级最低。

方法一：Spring boot 中如何自定义静态资源位置；

```properties
# 静态资源路径
spring.web.resources.static-locations=classpath:/mystatics/
## 静态资源访问路径配置(浏览器输入)
spring.mvc.static-path-pattern=mystatics
```

方法二 也可以在配置文件中配置

```java
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry //静态资源访问
                .addResourceHandler( "/mystatic" )
                // 静态资源
                .addResourceLocations( "classpath:/mystatics/" );
    }
}
```

#  Sprin boot 文件上传

## 1.1 Springboot 文件单文件上传

创建项目:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

在static 文件内新建一个01.html 页面

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<form action="/upload" method="post" enctype="multipart/form-data">
    <input type="file" name="file">
    <input type="submit" value="上传">
</form>
</body>
</html>
```

接口

```java
@RestController
public class FileUploadController {
    SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");
    @PostMapping("/upload")
    public String upload(MultipartFile file, HttpServletRequest req) {
        String realPath = req.getServletContext().getRealPath("/");
        String format = sdf.format(new Date());
        String path = realPath + format;
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
        try {
            file.transferTo(new File(folder, newName));
            String s = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + format + newName;
            return s;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
```

## 1.2 Sprinboot 多文件上传

```html
<form action="/upload2" method="post" enctype="multipart/form-data">
    <input type="file" name="files" multiple>
    <input type="submit" value="上传">
</form>
```

接口：

```java
@RestController
public class FileUploadController2 {
    SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");

    @PostMapping("/upload2")
    public void upload(MultipartFile[] files, HttpServletRequest req) {
        String realPath = req.getServletContext().getRealPath("/");
        String format = sdf.format(new Date());
        String path = realPath + format;
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        try {
            for (MultipartFile file : files) {
                String oldName = file.getOriginalFilename();
                String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
                file.transferTo(new File(folder, newName));
                String s = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + format + newName;
                System.out.println("s = " + s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

多选上传

```html
<form action="/upload3" method="post" enctype="multipart/form-data">
    <input type="file" name="file1">
    <input type="file" name="file2">
    <input type="submit" value="上传">
</form>
```

```java
@RestController
public class FileUploadController3 {
    SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");

    @PostMapping("/upload3")
    public void upload(MultipartFile file1, MultipartFile file2, HttpServletRequest req) {
        String realPath = req.getServletContext().getRealPath("/");
        String format = sdf.format(new Date());
        String path = realPath + format;
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        try {
            String oldName1 = file1.getOriginalFilename();
            String newName1 = UUID.randomUUID().toString() + oldName1.substring(oldName1.lastIndexOf("."));
            file1.transferTo(new File(folder, newName1));
            String s1 = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + format + newName1;
            System.out.println("s1 = " + s1);

            String oldName2 = file2.getOriginalFilename();
            String newName2 = UUID.randomUUID().toString() + oldName2.substring(oldName2.lastIndexOf("."));
            file2.transferTo(new File(folder, newName2));
            String s2 = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + format + newName2;
            System.out.println("s2 = " + s2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

## 1.3 Sprinboot ajax 上传

使用上面的upload 接口

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="https://code.jquery.com/jquery-3.5.1.js" integrity="sha256-QWo7LDvxbWT2tbbQ97B53yJnYU3WhH/C8ycbRAkjPDc=" crossorigin="anonymous"></script>
</head>
<body>
<div id="result"></div>
<input type="file" id="file">
<input type="button" value="上传" onclick="uploadFile()">
<script>
    function uploadFile() {
        var file = $("#file")[0].files[0];
        var formData = new FormData();
        formData.append("file", file);
        formData.append("username", "javaboy");
        $.ajax({
            type:'post',
            url:'/upload',
            processData:false,
            contentType:false,
            data:formData,
            success:function (msg) {
                $("#result").html(msg);
            }
        })
    }
</script>
</body>
</html>
```

## @ControllerAdivce 和RestControllerAdivce 



