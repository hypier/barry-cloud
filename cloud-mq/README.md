# SpringBoot RabbitMQ消息队列的重试、超时、延时、死信队列

标签（空格分隔）： SpringBoot RabbitMQ 死信

---
[toc]

今天介绍使用SpringBoot实现RabbitMQ消息队列的高级用法。

* MQ安装
* 自动创建
* 消息重试
* 消息超时
* 死信队列
* 延时队列

## 一、RabbitMQ的安装

众所周知，`RabbitMQ`的安装相对复杂，需要先安装**Erlang**，再按着对应版本的**RabbitMQ**的服务端，最后为了方便管理还需要安装**rabbitmq_management**管理端插件，偶尔还会出现一些安装配置问题，故十分复杂。
在开发测试环境下使用`docker`来安装就方便多了，省去了环境和配置的麻烦。

### 1. 拉取官方image
```shell
docker pull rabbitmq:management
```

### 2. 启动RabbitMQ

```shell
docker run -dit --name MyRabbitmq -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin -p 15672:15672 -p 5672:5672 rabbitmq:management
```

>rabbitmq:management: image:tag
--name:指定容器名；
-d:后台运行容器；
-t:在新容器内指定一个伪终端或终端；
-i:允许你对容器内的标准输入 (STDIN) 进行交互；
-p:指定服务运行的端口（5672：应用访问端口；15672：控制台Web端口号）；
-e:指定环境变量；（RABBITMQ_DEFAULT_USER：默认的用户名；RABBITMQ_DEFAULT_PASS：默认用户名的密码）；

至此RabbitMQ就安装启动完成了，可以通过http://localhost:15672 登陆管理后台，用户名密码就是上面配置的**admin/admin**

## 二、使用SpringBoot自动创建队列

### 1. 引入amqp包
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

### 2. MQ配置

**bootstrap.yml** 配置
```yml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    virtual-host: /
    username: admin
    password: admin
    listener:
      simple:
        concurrency: 5
      direct:
        prefetch: 10
```
`concurrency`：每个listener在初始化的时候设置的并发消费者的个数
`prefetch`：每次从一次性从broker里面取的待消费的消息的个数

![](https://oscimg.oschina.net/oscnet/up-1c51bcd95a16cf0489a99013409ff874a11.png)

**rabbitmq-spring.xml**配置
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:rabbit="http://www.springframework.org/schema/rabbit"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/rabbit http://www.springframework.org/schema/rabbit/spring-rabbit.xsd">

    <!--接收消息的队列名-->
    <rabbit:queue name="login-user-logined" />
    <!--声明exchange的名称与类型-->
    <rabbit:topic-exchange name="login_barryhome_fun">
        <rabbit:bindings>
            <!--queue与exchange的绑定和匹配路由-->
            <rabbit:binding queue="login-user-logined" pattern="login.user.logined"/>
        </rabbit:bindings>
    </rabbit:topic-exchange>
</beans>
```

`rabbit:topic-exchange`：声明为**topic**消息类型
`pattern="login.user.logined"`：此处是一个表达式，可使用“*”表示一个词，“#”表示一个或多个词

### 3. 消息生产端
```java
@Autowired
RabbitTemplate rabbitTemplate;

@GetMapping("/send")
public LoginUser SendLoginSucceedMessage(){
    LoginUser loginUser = getLoginUser("succeed");
    // 发送消息
    rabbitTemplate.convertAndSend(MessageConstant.MESSAGE_EXCHANGE,
            MessageConstant.LOGIN_ROUTING_KEY, loginUser);
    return loginUser;
}

@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements Serializable {
    String userName;
    String realName;
    String userToken;
    Date loginTime;
    String status;
}
```

这里需要注意的是默认情况下消息的转换器为`SimpleMessageConverter`只能解析**string**和**byte**，故传递的消息对象必须是可序列化的，实现`Serializable`接口
```java
SimpleMessageConverter only supports String, byte[] and Serializable payloads, received: fun.barryhome.cloud.dto.LoginUser
```

### 4. 消息消费端
```java
@Component
public class ReceiverMessage {

    @RabbitListener(queues = "login-user-logined")
    public void receiveLoginMessage(LoginUser loginUser) {
        System.err.println(loginUser);
    }
}
```

`@RabbitListener(queues = "login-user-logined")`：用于监听名为**login-user-logined** 队列中的消息

### 5. 自动创建Queue
```java
@SpringBootApplication
@ImportResource(value = "classpath:rabbitmq-spring.xml")
public class MQApplication {
    public static void main(String[] args) {
        SpringApplication.run(MQApplication.class, args);
    }
}
```

在没有导入xml且MQ服务器上没有列队的情况下，会导致找不到相关queue的错误
``` java
channel error; protocol method: #method<channel.close>(reply-code=404, reply-text=NOT_FOUND - no queue 'login-user-logined' in vhost '/', class-id=50, method-id=10)
```
而导入之后将`自动创建` **exchange**和**queue**

## 三、消息重试

默认情况下如果有消息消费出错后会一直重试，造成消息堵塞
![](https://oscimg.oschina.net/oscnet/up-4a31d102a2a0d0b363ca4cee719d9d9b0bd.png)
如图可观察**unacked**和**total**一直是1，但**deliver/get**飙升

消息堵塞之后也影响到后续消息的消费，时间越长越来越多的消息将无法及时消费处理。
如果是单条或极少量的消息有问题可通过多开节点`concurrency`将正常的消息消息掉，但如果较多则全部节点都将堵塞。

如果想遇到消息消费报错重试几次就舍弃，从而不影响后续消息的消费，如何实现呢？

```yml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    virtual-host: /
    username: admin
    password: admin
    listener:
      simple:
        concurrency: 5
        prefetch: 10
        retry:
          enabled: true   # 允许消息消费失败的重试
          max-attempts: 3   # 消息最多消费次数3次
          initial-interval: 2000    # 消息多次消费的间隔2秒
```
以上配置允许消息消费失败后重试3次，每次间隔2秒，如果还是失败则直接舍弃掉本条消息。
重试可解决因非消息体本身处理问题产生的临时性的故障，而将处理失败的消息直接舍弃掉只是为其它消息正常处理的权益之计而以，将业务操作降到相对低的影响。

## 四、消息超时
`消息重试`可解决因消息处理报错引起的问题。如果是消息处理过慢导致错过时效，除了可在处理逻辑中进行处理外，也可以通过消息的超时机制来处理，设定超时时间后将消息直接舍弃。

修改**rabbitmq-spring.xml**
```xml
<rabbit:queue name="login-user-logined">
    <rabbit:queue-arguments>
    <entry key="x-message-ttl" value="10000" value-type="java.lang.Long" />
    </rabbit:queue-arguments>
</rabbit:queue>
```
`x-message-ttl`：在消息服务器停留的时间(ms)

![](https://oscimg.oschina.net/oscnet/up-306e9defa5e39da35afa5ceb413c8485414.png)
如果配置前已存在queue将不能被修改，需要删除原有queue后自动创建
创建成功后会在**Features**中有**TTL**标识

## 五、死信队列
死信队列就是当业务队列处理失败后，将消息根据routingKey转投到另一队列，这样的情况有：

* 消息被拒绝 (basic.reject or basic.nack) 且带 requeue=false不重新入队参数或达到的retry重新入队的上限次数
* 消息的TTL(Time To Live)-存活时间已经过期
* 队列长度限制被超越（队列满，queue的"x-max-length"参数）


### 1. 修改**rabbitmq-spring.xml**
```xml
<!--接收消息的队列名-->
<rabbit:queue name="login-user-logined">
    <rabbit:queue-arguments>
        <entry key="x-message-ttl" value="10000" value-type="java.lang.Long"/>
        <!--死信的交换机-->
        <entry key="x-dead-letter-exchange" value="login_barryhome_fun"/>
        <!--死信发送的路由-->
        <entry key="x-dead-letter-routing-key" value="login.user.login.dlq"/>
    </rabbit:queue-arguments>
</rabbit:queue>
<rabbit:queue name="login-user-logined-dlq"/>

<!--申明exchange的名称与类型-->
<rabbit:topic-exchange name="login_barryhome_fun">
    <rabbit:bindings>
        <!--queue与exchange的绑定和匹配路由-->
        <rabbit:binding queue="login-user-logined" pattern="login.user.logined"/>
        <rabbit:binding queue="login-user-logined-dlq" pattern="login.user.login.dlq"/>
    </rabbit:bindings>
</rabbit:topic-exchange>

```

![](https://oscimg.oschina.net/oscnet/up-91625d334c5b7db8a518b8f3e03afad50c1.png)

通过对死信发送的交换机和路由的的设置，可将消息转向具体的queue中。这里交换机可以和原业务队列不是一个。
当`login-user-logined`中的消息处理失败后将直接转投向`login-user-logined-dlq`队列中。
当程序逻辑修复后可再将消息再移回业务队列中`move messages`

### 2. 安装插件

![](https://oscimg.oschina.net/oscnet/up-d648db0191cd9ac1b82ca890f6de8ab17f3.png)
如图提示需要先安装插件

### 3. 移动消息
![](https://oscimg.oschina.net/oscnet/up-f1064bde86da2dd60b6d1724298bd85c480.png)
安装成功后就可以输入业务队列名再转投

## 六、延时队列
延时队列除了可以做一般的延时处理外，还可以当作单个job的定时任务处理，比起一般通过定时器去轮询的方式更优雅。

### 1. 修改rabbitmq-spring.xml
```xml
<rabbit:topic-exchange name="login_barryhome_fun" delayed="true">
```

初次配置时，如果报以下错误，则是服务器不支持此命令，需要安装插件

```java
Channel shutdown: connection error; protocol method: #method<connection.close>(reply-code=503, reply-text=COMMAND_INVALID - unknown exchange type 'x-delayed-message', class-id=40, method-id=10)
```

### 2. 安装插件

1) 下载插件：https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases/tag/v3.8.0

2) 上传插件到docker容器中/plugins
`docker ps` 查询rabbitmq的 **CONTAINER ID**
![](https://oscimg.oschina.net/oscnet/up-057ee4c45e643398009578d3a3341e01875.JPEG)
```shell
docker cp rabbitmq_delayed_message_exchange-3.8.0.ez 2c248563a2b0:/plugins
```

3) 进入docker容器内部
```shell
docker exec -it 2c248563a2b0 /bin/bash
```

4) 安装插件
```shell
cd /plugins
rabbitmq-plugins enable rabbitmq_delayed_message_exchange
```

具体安装教程可参考：https://blog.csdn.net/magic_1024/article/details/103840681

安装成功后重启程序，观察mq管理端的**exchange**可发现
![](https://oscimg.oschina.net/oscnet/up-c1d4d81b838f31b7f842f8b3f80a8180576.png)

### 3. 发送延时消息
``` java
@GetMapping("/sendDelay")
public LoginUser SendDelayLoginSucceedMessage() {
    LoginUser loginUser = getLoginUser("succeed");

    MessagePostProcessor messagePostProcessor = message -> {
        // 延时10s
        message.getMessageProperties().setHeader("x-delay", 10000);
        return message;
    };

    // 发送消息
    rabbitTemplate.convertAndSend(MessageConstant.MESSAGE_EXCHANGE,
            MessageConstant.LOGIN_ROUTING_KEY, loginUser, messagePostProcessor);
    return loginUser;
}
```

>需要注意的是消息的发送是`实时`的，消息服务器接收到消息待延时时间后再投到对应的queue中

## 七、完整代码
[https://gitee.com/hypier/barry-cloud/tree/master/cloud-mq][5]

## 八、请关注我的公众号
 ![请关注我的公众号][6]


  [6]: https://oscimg.oschina.net/oscnet/up-8969dabd3beeba071b59e61139a2bb8b22f.JPEG
  [5]: https://gitee.com/hypier/barry-cloud/tree/master/cloud-mq