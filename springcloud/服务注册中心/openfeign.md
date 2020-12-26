# openFeign

* 为什么使用feign，底层默认继承ribbon

  > 1.每次调用服务都需要写这些代码，存在大量代码冗余
  >
  > 2.服务名称如果修改，维护成本高
  >
  > 3.使用不灵活

#  使用

### 1.pom.xml

```xml
<!--openfeign 依赖-->
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
    </dependencies>

```

### 2.springboot

```java
@SpringBootApplication
@EnableDiscoveryClient  //启动服务注册和服务发现功能  默认开启
@EnableFeignClients    //开启支持openfeign
public class OpenFeignApplication {
}
```

### 3.服务调用

```java
@FeignClient("user-service")
public interface UserFeignClient {
    @GetMapping("/user/")
     String index();
}

```

* 使用结果

* ```java
  @RestController
  public class FeignController {
      @Autowired
      UserFeignClient feignClient;
      @GetMapping("/test")
      public String testfeign(){
          return feignClient.index();
      }
      @GetMapping("/testindex")
      public String test(){
          return "123";
      }
  }
  
  ```

  #### 参数传递

  ##### 1.get方式

  feign客户端

  ```java
      /**
       *  get方式传递参数的时候必须在参数前添加@RequestParam("")注解且
       *    参数的名称必须为请求接口接收参数的名称完全相同，（即""中的字段与请求接口的参数相同）
       		param  ==param
       * @param feignParam  feign请求注册中心的服务时传递给请求服务的参数
       * @return
       */
      @GetMapping("/user/getfeign")
      String getFeign(@RequestParam("param") String feignParam); 
  ```

  使用fein进行通信

  > ```java
  >     @GetMapping("/testindex02")
  >     public String test02(String param){
  >         return feignClient.getFeign(param);
  >     }
  > ```

  未添加@RequestParam

  ![image-20201226232253932](C:\Users\13198\AppData\Roaming\Typora\typora-user-images\image-20201226232253932.png)

##### 2.post方式

1.传递单个属性同get方式

2.传递对象类型时：

```java
1.被feign请求的服务参数必须使用@RequestBody --@RequestBody将json字符串转换为对应的对象
2.feign客户端声明时参数也需要添加@RequestBody
3.传递的对象必须是相同的，及类相同，属性相同
```

代码：

```java
User.java
            @PostMapping("postObject")
        	public Map<String,Object> postObject(@RequestBody User user){
            Map<String ,Object> map=new HashMap();
            map.put("name",user.getUsername());
            return map;
```

```java
feign客户端
     /**
     *
     * @param user post 方式传递的对象
     * @return
     */
    @PostMapping("/user/postObject")
    Map<String,Object> postObject(@RequestBody User user);
```

# openFeign超时设置

默认情况下，openfeign在进行服务调用时要求服务提供方处理逻辑时间必须在1s内返回，如果超时openfeign会直接报错，不会等待服务执行，但是往往在处理复杂业务逻辑时可能会超过1s，因此需要修改openfeign的默认服务调用超时时间