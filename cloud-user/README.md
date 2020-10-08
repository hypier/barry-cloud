# SpringCloud轻松集成Dubbo实现RPC调用

标签（空格分隔）： SpringCloud RPC Dubbo

---

很久之前在做微服务架构选型的时候就听说阿里的微服务RPC框架`dubbo`，当时与`Spring Cloud`以http协议调用的架构做对比。发现`dubbo`的rpc框架学习成本较高，代码入侵性强，本身生态不完整，需要整合多个外部组件，故选择了相对性能弱一点的`Spring Cloud`全家桶。

直到Spring Cloud Alibaba的出现，使用`Nacos`作为服务发现与注册，同时兼容使用`Feign`的`http`方式和使用`Dubbo`的`rpc`方式调用。

**Spring Cloud 为什么需要RPC**
>在Spring Cloud构建的微服务系统中，大多数的开发者使用都是官方提供的Feign组件来进行内部服务通信，这种声明式的HTTP客户端使用起来非常的简洁、方便、优雅，并且和开发平台、语言无关，但是通常情况下，HTTP并不会开启KeepAlive功能，即当前连接为短连接，短连接的缺点是每次请求都需要建立TCP连接，这使得其效率变的相当低下。

>对外部提供REST API服务是一件非常好的事情，但是如果内部调用也是使用HTTP调用方式，就会显得显得性能低下，Spring Cloud默认使用的Feign组件进行内部服务调用就是使用的HTTP协议进行调用，这时，我们如果内部服务使用RPC调用，对外使用REST API，将会是一个非常不错的选择。

*引用至：[Dubbo 与 Spring Cloud 完美结合](https://www.cnblogs.com/babycomeon/p/11546737.html)*

使用Dubbo Spring Cloud使用内部的RPC协议调用几乎是零成本的改造。

## 一、系统结构

![](https://oscimg.oschina.net/oscnet/up-3322c7976c7f276a1351a8f8ee35d2b4e37.png)

* `cloud-gateway` 作为cloud集群的网关，外部的路由转发使用**http**协议，内部的服务调用使用**dubbo**协议
* `cloud-user`和`cloud-mq`之间的远程调用使用**dubbo**协议
* 使用`Nacos`作为**服务注册与发现**和**配置中心**的服务
* 使用`Sentinel`作为服务间**http**和**dubbo**调用的流量控制服务

**目录结构**

```
├── cloud-admin         # 服务监控
├── cloud-gateway       # 服务网关
├── cloud-mq            # mq服务
├── cloud-provider      # 服务接口
└── cloud-user          # user服务
```

## 二、服务接口提供方实现

### 1. 服务接口定义
```java
public interface UserProvider {
    UserDTO checkUser(String userName, String password);
    UserDTO findByUserName(String userName);
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    String userName;
    String realName;
    String password;
}

```

* 服务接口是服务提供方和消费方的契约，包含服务的方法传输对象DTO。由于涉及多个应用服务的引入，最好是将其独立成**Module**
* DTO对象必须实现`Serializable`接口

### 2. 引入`dubbo`包

**POM**
```xml
 <dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-dubbo</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

`spring-boot-starter-actuator` 也是必须的


### 3. 服务接口实现
```java
import org.apache.dubbo.config.annotation.Service;

@Service
public class UserProviderImpl implements UserProvider {

    @Autowired
    private UserService userService;

    @Override
    public UserDTO checkUser(String userName, String password) {

        User user = userService.checkUser(userName, password);
        return UserConvertor.toDTO(user);
    }

}

```

`@Service`必须是**org.apache.dubbo.config.annotation.Service**

### 4. 配置Dubbo服务相关的信息
```yml
spring:
  main:
    allow-bean-definition-overriding: true
    
dubbo:
  scan:
    base-packages: fun.barryhome.cloud.provider   #指定 Dubbo 服务实现类的扫描基准包
  protocols:
    dubbo:
      name: dubbo   # Dubbo 的协议名称
      port: -1      # port 为协议端口（ -1 表示自增端口，从 20880 开始）

  registry:
    address: spring-cloud://localhost       # 挂载到 Spring Cloud 注册中心
```

启动后有可能出现连接失败，不影响使用
``` java
java.net.ConnectException: Connection refused (Connection refused)
	at java.net.PlainSocketImpl.socketConnect(Native Method) ~[na:1.8.0_111]
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:350) ~[na:1.8.0_111]
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:206) ~[na:1.8.0_111]

```

## 三、服务调用方实现

### 1. 引入依赖包
```xml
<!--服务接口-->
<dependency>
    <groupId>fun.barryhome</groupId>
    <artifactId>cloud-provider</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-dubbo</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### 2. 调用服务接口
```java
import org.apache.dubbo.config.annotation.Reference;

public class UserController {

    @DubboReference
    private UserProvider userProvider;

    @GetMapping(value = "/sessionUser")
    public UserDTO sessionUser(HttpServletRequest request) {

        String userName = request.getHeader("X-User-Name");
        if (Strings.isEmpty(userName)) {
            throw new RuntimeException("没有找到用户");
        }

        return userProvider.findByUserName(userName);
    }
}

```

### 3. 配置Dubbo服务相关的信息

```yml
dubbo:
  cloud:
    subscribed-services: cloud-user   # 服务提供方的服务名
  consumer:
    check: false
    loadbalance: "leastactive"    # 最小活跃数负载均衡

  registry:
    # 挂载到 Spring Cloud 注册中心
    address: spring-cloud://localhost
```

`dubbo.consumer.check`：用于启动时是否检查服务提供方是否运行正常，如果不正常将不能启动调用方

`dubbo.consumer.loadbalance`：负载均衡策略

* RandomLoadBalance：随机，按权重设置随机概率
* ConsistentHashLoadBalance：一致性哈希算法
* LeastActiveLoadBalance：最小活跃数负载均衡
* RoundRobinLoadBalance：根据权重进轮训

## 四、总结

1. 服务间使用了长连接，在正常运行时，提供方某个节点断掉后会需要一段时间来切换，可使用`sentinel`来控制快速切换可用节点
2. 使用dubbo进行远程调用，内部调用性能上有所提供，调用方式上也相对简单
3. 与`sentinel`配合，合理使用负载策略，可实现更多功能，如灰度发布，版本控制等
4. 性能的提升让调用链增加成为可能性，可实现更小粒度的微服务拆分与组合

## 五、源代码
[https://gitee.com/hypier/barry-cloud](https://gitee.com/hypier/barry-cloud)

## 六、请关注我的公众号
 ![请关注我的公众号][6]


[6]: https://oscimg.oschina.net/oscnet/up-8969dabd3beeba071b59e61139a2bb8b22f.JPEG