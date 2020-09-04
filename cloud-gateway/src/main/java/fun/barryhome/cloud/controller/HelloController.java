package fun.barryhome.cloud.controller;


import fun.barryhome.cloud.auth.Session;
import fun.barryhome.cloud.dto.LoginUser;
import fun.barryhome.cloud.provider.permission.PermissionDTO;
import fun.barryhome.cloud.provider.permission.PermissionProvider;
import fun.barryhome.cloud.provider.user.UserDTO;
import fun.barryhome.cloud.provider.user.UserProvider;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    @Reference(loadbalance = "leastactive", retries = 0, filter = "activelimit")
    private UserProvider userProvider;

    @Reference
    private PermissionProvider permissionProvider;


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
     * @param userName
     * @param password
     * @return
     */
    private LoginUser userLogin(String userName, String password){
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
        // 保存用户权限
        session.saveUserPermissions(userName, permissions);

        return loginUser;
    }

}

