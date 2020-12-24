# 1.eureka服务端

## 1.主类

```
@SpringBootApplication
@EnableEurekaServer//代表它是一个eureka服务端
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class,args);
    }
}
```

## 2.配置

```yaml
server:
  port: 8761
spring:
  application:
    name: myeurekaserver
      # 暴露eureka注册中心的地址，供客户端连接(固定写法)
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
    fetch-registry: false #关闭一启动服务eureka就注册自己，等服务初始化完成后再注册自己
    register-with-eureka: false #关闭eureka客户端，该服务不再是eureka客户端
```

## 3. pom.xml

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```



# 2.eureka客户端

## 1.pom.xml

```xml
<!--    eureka client-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
```

## 2.主类

```java
@SpringBootApplication
@EnableEurekaClient //代表当前服务是个eureka客户端
public class EurekaClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class,args);
    }
}
```

## 3.配置

```yaml
server:
  port: 8888
spring:
  application:
    name: myeurekaclient
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
```