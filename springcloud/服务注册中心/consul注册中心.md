# consul

* consul启动就是服务注册中心，我们开发的springboot应用程序就是客户端，直接向consul注册

# 1.使用consul

* 1.下载
* 2.设置环境变量 exe的上级目录添加到path

* 使用命令consul agent -dev启动consul  
* 访问localhost:8500

# 1.创建项目demo

## 1.java

```java
@SpringBootApplication
@EnableDiscoveryClient
public class ConsulClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsulClientApplication.class,args);
    }
}
```

##  2.application.yml

```yaml
server:
  port: 8889
spring:
  application:
    name: myconsulclient
  cloud:
    consul:
      host: localhost   # 注册consul服务的主机
      port: 8500  # 注册consul服务的端口号
      discovery:
        register-health-check: true   # 关闭consul服务的健康检查【不推荐 false】
        service-name: ${spring.application.name}-own  # 指定注册的服务名称
```



3.pom.xml

* 开启consul健康监控

* 默认情况下consul的健康监控是开启的，但是必须依赖健康监控依赖才能正确的监控健康状态，所以直接启动会出现下列情况

* 引入健康监控之后服务正常

  ![](E:\笔记\springcloud\服务注册中心\img\consul健康状态.png)

```xml
<!--    consul 客户端-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-consul-discovery</artifactId>
    </dependency>
```

> 必须依赖的健康检查

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```

## cap定理及区别

![](E:\笔记\springcloud\服务注册中心\img\区别.jpg)