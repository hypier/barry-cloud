package fun.barryhome.cloud.controller;


import fun.barryhome.cloud.auth.Session;
import fun.barryhome.cloud.dto.LoginUser;
import fun.barryhome.cloud.provider.permission.PermissionDTO;
import fun.barryhome.cloud.provider.permission.PermissionProvider;
import fun.barryhome.cloud.provider.user.UserDTO;
import fun.barryhome.cloud.provider.user.UserProvider;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created on 2019-07-29 18:25
 *
 * @author barry
 * Description:
 */
@RestController
public class HelloController {

    @Autowired
    private Session session;

    @DubboReference
    private UserProvider userProvider;

    @DubboReference
    private PermissionProvider permissionProvider;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @GetMapping(value = "/hello")
    public String hello() {
        return "Demo project for Spring Boot !";
    }

    @GetMapping(value = "/cachePermissions")
    public void cachePermissions() {
        session.savePermissions(permissionProvider.findAll());
    }

    @PostMapping(path = "/login")
    public LoginUser login(@RequestParam String userName, @RequestParam String password) {
        return userLogin(userName, password);
    }

    /**
     * 用户登陆
     *
     * @param userName
     * @param password
     * @return
     */
    private LoginUser userLogin(String userName, String password) {
        // 检查密码
        UserDTO user = userProvider.checkUser(userName, password);

        LoginUser loginUser = LoginUser.builder()
                .userName(userName)
                .realName(user.getRealName())
                .userToken(UUID.randomUUID().toString())
                .loginTime(new Date())
                .build();

        // 保存session
        session.saveSession(loginUser);

        // 查询权限
        List<PermissionDTO> permissions = permissionProvider.findByUserName(userName);
        // 保存用户资源访问权限
        session.saveUserPermissions(userName, permissions);

        // 保存用户数据权限
        redisAuthStore(userName);

        return loginUser;
    }

    /**
     * 构造数据权限样例数据
     * @param userName
     */
    private void redisAuthStore(String userName){
        Set<String> list = new HashSet<>();
        list.add("cq");
        list.add("cd");
        list.add("cs");

        String AUTH_USER_KEY = "auth:logic:user:%s";
        String redisKey = String.format(AUTH_USER_KEY, userName);

        redisTemplate.opsForList().leftPushAll(redisKey, list);
    }

}

