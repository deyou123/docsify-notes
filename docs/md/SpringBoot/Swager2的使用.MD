1、添加依赖

不同版本可能会引发程序运行异常

```java
<dependency>
   <groupId>io.springfox</groupId>
   <artifactId>springfox-swagger2</artifactId>
   <version>2.9.2</version>
</dependency>
<dependency>
   <groupId>io.springfox</groupId>
   <artifactId>springfox-swagger-ui</artifactId>
   <version>2.9.2</version>
</dependency>
```

2、Spring boot 中 Swagger2配置类

```java
@Configuration
@EnableSwagger2 //开启Swagger
public class Swagger2Config {
    //http://localhost:8080/swagger-ui.html#
    @Bean
    public Docket createSocket(){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().build();
    }
}
```

3、页面接口测试

启动应用，在浏览器中输出 http://localhost:8080/swagger-ui.html# 进行接口测试。

参考文档：

官方网站： [https://swagger.io/](https://swagger.io/)

官方文档:	[https://swagger.io/docs/)](https://swagger.io/docs/)

