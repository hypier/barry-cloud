# 微服务下的用户鉴权方案

标签（空格分隔）： SpringCloud Gateway

---

上一章讲了微服务下的用户身份认证《[SpringCloud Gateway 身份认证][1]》，这次主要讲如何进行鉴权。
*相对上一章的身份认证代码略有改动*

Java下常用的安全框架主要有`Spring Security`和`shiro`，都可提供非常强大的功能，但学习成本较高。在微服务下鉴权多多少少都会对服务有一定的入侵性。
为了降低依赖，减少入侵，让鉴权功能相对应用服务透明，我们采用网关拦截资源请求的方式进行鉴权。

## 一、整体架构
![整体结构][2]

`用户鉴权`模块位于**API GateWay**服务中，所有的API资源请求都需要从此通过。

1. 做**身份认证**，通过则缓存用户权限数据，不通过返回`401`
2. 做**用户鉴权**，比对当前访问资源（URI和Method）是否在已缓存的用户权限数据中，在则转发请求给对应应用服务，不在则返回`403`

## 二、实现步骤
![登陆鉴权流程][3]

### 1. 用户登陆

```java
public LoginUser login(String userName, String password){
    // 检查密码
    User user = userService.checkUser(userName, password);

    LoginUser loginUser = LoginUser.builder()
            .userName(userName)
            .realName(user.getRealName())
            .userToken(UUID.randomUUID().toString())
            .loginTime(new Date())
            .build();

    // 保存session
    session.saveSession(loginUser);

    // 查询权限
    List<Permission> permissions = permissionRepository.findByUserName(userName);
    // 保存用户权限到缓存中
    session.saveUserPermissions(userName, permissions);

    return loginUser;
}

// ...
// 缓存用户权限到Redis
public void saveUserPermissions(String userName, List<Permission> permissions) {
    String key = String.format("login:permission:%s", userName);

    HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
    hashOperations.putAll(key, permissions.stream().collect(
            Collectors.toMap(p -> p.getMethod().concat(":").concat(p.getUri()),
                    Permission::getName, (k1, k2) -> k2)));

    if (expireTime != null) {
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }
}
```

* 用户验证通过后，下发`userToken`，保存当前登陆信息，缓存用户授权列表
* 缓存授权列表时，为了方便读取使用hash方式保存为**list**，切勿直接将数组对象保存为一个object

### 2. 拦截请求
```java
@Slf4j
@Component
public class AuthorizationFilter extends AbstractGatewayFilterFactory {

    @Autowired
    private Session session;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            String uri = request.getURI().getPath();
            String method = request.getMethodValue();

            // 1.从AuthenticationFilter中获取userName
            String key = "X-User-Name";
            if (!request.getHeaders().containsKey(key)) {
                response.setStatusCode(HttpStatus.FORBIDDEN);
                return response.setComplete();
            }

            String userName = Objects.requireNonNull(request.getHeaders().get(key)).get(0);

            // 2.验证权限
            if (!session.checkPermissions(userName, uri, method)) {
                log.info("用户：{}, 没有权限", userName);
                response.setStatusCode(HttpStatus.FORBIDDEN);
                return response.setComplete();
            }

            return chain.filter(exchange);
        };
    }
}

```

- 第一步从取出`身份认证`模块传递的**X-User-Name**
- 第二步去缓存中检查是否有相应的权限

```java
public boolean checkPermissions(String userName, String uri, String method) {
    String key = String.format("login:permission:%s", userName);
    String hashKey = String.format("%s:%s", method, uri);

    if (redisTemplate.opsForHash().hasKey(key, hashKey)){
        return  true;
    }

    String allKey = "login:permission:all";
    // 权限列表中没有则通过
    return !redisTemplate.opsForHash().hasKey(allKey, hashKey);
}
```

* `权限列表中没有则通过` 主要是放过一些没有必要配置的公共资源，默认都可以访问的资源
* `login:permission:all` 所有配置过的权限列表需要在程序启动时放入缓存，并需要保持数据的更新

### 3. 鉴权Filter配置
``` yml
spring:
  cloud:
    gateway:
      routes:
        - id: cloud-user
          uri: lb://cloud-user  # 后端服务名
          predicates:
            - Path=/user/**   # 路由地址
          filters:
            - name: AuthenticationFilter  # 身份认证
            - name: AuthorizationFilter   # 用户鉴权
            - StripPrefix=1 # 去掉前缀
```

* 特别注意filter的顺序，必须先做身份认证后再进行鉴权
* 如果有较多的路由都需要配置，可使用`default-filters`默认Filter配置

## 三、其它问题
在做单元测试时，如遇到如下错误
```
nested exception is java.lang.NoClassDefFoundError: javax/validation/ValidationException
```
请升级依赖包版本：
``` xml
<!--升级validation-api的版本-->
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>6.0.5.Final</version>
</dependency>
<dependency>
    <groupId>javax.validation</groupId>
    <artifactId>validation-api</artifactId>
    <version>2.0.1.Final</version>
</dependency>
```

## 四、完整代码
[https://gitee.com/hypier/barry-cloud][5]

## 五、请关注我的公众号
 ![请关注我的公众号][6]


  [6]: https://oscimg.oschina.net/oscnet/up-8969dabd3beeba071b59e61139a2bb8b22f.JPEG
  [5]: https://gitee.com/hypier/barry-cloud
  [1]: https://my.oschina.net/barryhome/blog/4512213
  [2]: https://oscimg.oschina.net/oscnet/up-fdc5f3c3c447822b24d36ccc7112abee859.png
  [3]: https://oscimg.oschina.net/oscnet/up-791773e1f595ca7b9eb0eb6ffc45ebc569a.png