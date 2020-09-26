# 使用Spring Cloud Stream玩转RabbitMQ，RocketMQ和Kafka

标签（空格分隔）： SpringCloudStream RabbitMQ RocketMQ Kafka

---
[toc]

前一章我们讲了《[SpringBoot RabbitMQ消息队列的重试、超时、延时、死信队列][1]》，从代码层面引用了非常多的rabbit特征代码，如：`rabbitTemplate.convertAndSend()`，` @RabbitListener(queues = "xxx")`等，都是很简单的代码看起来一切都是合理的，但隐约感觉代码遭到了入侵。

业务的发展对MQ的依赖越来越重，地位也越来越高，对它的需求也越来越多。比如顺序消费，事务消息，回溯消费等，性能方面也有更高要求。越来越多的趋势提醒我们有更好MQ方案。

假如我们将“MQ从Rabbit替换成Rocket”的方案提上议程，就会发放这是一个非常浩大的工程。以前好多服务都是用的有`RabbitMQ`的特征代码，如果要替换相当于所有服务的代码都要较大的更新，这带来的运营风险是巨大的，需要非常多的开发测试资源的投入。

那回头来讲，我们最开始使用**rabbitmq**的时候能不能尽量隐藏特征代码吗，为以后的升级替换保留可能性。

这个时候就需要使用`Spring Cloud`的子组件`Spring Cloud Stream`。它是一个构建消息驱动微服务的框架，提供一套消息订阅消费的标准为不同供应商的消息中间件进行集成。目前官方提供`Kafka`和`RabbitMQ`的集成实现，而阿里也实现对`RocketMQ`的集成。

## 一、 Spring Cloud Stream简介

![](https://oscimg.oschina.net/oscnet/up-eee2a08f8cad1ecce185545fc6a46037325.png)

Spring Cloud Stream应用由第三方的中间件组成。应用间的通信通过输入通道（input channel）和输出通道（output channel）完成。这些通道是由Spring Cloud Stream 注入的。而通道与外部的代理的连接又是通过Binder实现的。

## 二、 RabbitMQ集成

### 1. 引入包

``` xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
</dependency>
```

### 2. 设置消息输入输出通道

```java
public interface Source {
    String OUTPUT = "myOutput";

    @Output(OUTPUT)
    MessageChannel message();
}

public interface Sink {
    String INPUT = "myInput";

    @Input(INPUT)
    SubscribableChannel sub1();
}

```

输出通道为消息的**发送方**，输入通道为消息的**接收方**

`myOutput`，`myInput`为通道名，后续通过配置文件进行特性配置，切记两个通道的绑定最好是**分开定义**，不然有可能产生莫名错误

### 3. 消息特性配置

```yml
spring
  cloud:
    stream:
      bindings:
        myOutput:
          destination: login-user
        myInput: # 通道名，对应代码中的消费监听组
          destination: login-user # exchange
          group: logined-member   # 消费组

      rabbit:
        bindings:
          myOutput:
            producer:
              routing-key-expression: headers.routingKey   # 发送端路由key
              delayed-exchange: true    # 开启延时队列

          myInput:
            consumer:
              binding-routing-key: login.user.succeed   # 消费监听路由表达式
              delayed-exchange: true    # 开启延时队列
              auto-bind-dlq: true   # 绑定死信队列
              republish-to-dlq: true  # 重投到死信队列并带有报错信息

```

#### 1) `destination`消息的主题名
在Rabbit中用来定义`exchange`以及成为`queue`的一部分

#### 2) `group`消费组

* 没有定义消费组时，如果启动多实例则一个消息同时都消费
![](https://oscimg.oschina.net/oscnet/up-e4609ffabb421489c4fc7f2afff4ffa6f9a.png)

* 定义了消费组后，多实例共用一个queue，负载消费。从图可以看出**queue**名为`destination.group`组成
![](https://oscimg.oschina.net/oscnet/up-dc5fa9dccc017359347f5bfd607ae34747b.png)

* *binding-routing-key*：消费路由监听表达式
* *delayed-exchange*： 开启延时队列
* *auto-bind-dlq*：开启死信队列
* *republish-to-dlq*：此设置可以让死信消息带报错信息
![](https://oscimg.oschina.net/oscnet/up-ac4afbf12c95e2ace72a6317a2005d07ab9.png)

### 4. 消息的发送接收实现

**发送消息**

```java
@Autowired
private Source source;

@GetMapping("/")
public void sendSucceed() {
    source.message().send(MessageBuilder.withPayload("Hello World...")
            .setHeader("routingKey", "login.user.succeed")
            .setHeader("version", "1.0")
            .setHeader("x-delay", 5000)
            .build());
}
```

这里可以为消息设置不同header，以现实不同的功能，这部分每种MQ有不同的特性，需要视情况而定

**接收消息**

```java
@StreamListener(value = Sink.MY_INPUT_1, condition = "headers['version']=='1.0'")
public void receiveSucceed_v1(@Payload String message) {
    String msg = "StreamReceiver v1: " + message;
    log.error(msg);
}
```

### 5. 绑定消息通道
```java
@EnableBinding(value = {Source.class, Sink.class})
@SpringBootApplication
public class RabbitApplication {
    public static void main(String[] args) {
        SpringApplication.run(RabbitApplication.class, args);
    }
}
```

实现这5步就可以正常发送接收消息了，你会发现除了引入不同的包和消息特性配置外，其它的代码都是抽象代码，没有任何rabbitmq的特征代码

## 三、 RocketMQ集成

根据`RabbitMQ`的相关代码，只需要修改引入包和特片配置就可以替换成`RocketMQ`了（*一些特性功能除外*）

### 1. 引入包

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-stream-rocketmq</artifactId>
</dependency>
```

### 2. 消息特征配置
```yml
spring
  cloud:
    stream:
      bindings:
        myOutput:
          destination: login-user
          content-type: application/json

        myInput: # 通道名，对应代码中的消费监听组
          destination: login-user # exchange
          group: logined-member   # 消费者组, 同组负载消费

      rocketmq:
        binder:
          name-server: 127.0.0.1:9876

```

## 四、 Kafka集成

### 1. 引入包

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-stream-binder-kafka</artifactId>
</dependency>
```

### 2. 消息特征配置
```yml
spring
    cloud:
    stream:
      bindings:
        myOutput:
          destination: login-user
          content-type: application/json

        myInput: # 通道名，对应代码中的消费监听组
          destination: login-user # exchange
          group: logined-member   # 消费者组, 同组负载消费


      kafka:
        binder:
          brokers: localhost:9092         #Kafka的消息中间件服务器
          auto-create-topics: true

```

## 五、 总结

由上面三个简单的例子可以看出，`Spring Cloud Stream`对消息订阅和消费做了高度抽象，用一套代码实现多种消息中间件的支持。同时它也可以非常简单的实现多种消息中间件的混用，大大扩展了消息中间件的玩法。

这里也建议如果没有特殊的特征场景需要实现的话，推荐使用`Spring Cloud Stream`组件来实现消息的订阅与消费，对中间件进行高度接耦。

## 六、源代码
文中代码由于篇幅原因有一定省略并不是完整逻辑，如有兴趣请Fork源代码
[https://gitee.com/hypier/barry-cloud/tree/master/cloud-stream](https://gitee.com/hypier/barry-cloud/tree/master/cloud-stream)

## 七、请关注我的公众号
 ![请关注我的公众号](https://oscimg.oschina.net/oscnet/up-8969dabd3beeba071b59e61139a2bb8b22f.JPEG)


  [1]: https://my.oschina.net/barryhome/blog/4538928