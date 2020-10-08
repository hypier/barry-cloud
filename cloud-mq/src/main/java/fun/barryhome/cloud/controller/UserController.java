package fun.barryhome.cloud.controller;

import fun.barryhome.cloud.provider.user.UserDTO;
import fun.barryhome.cloud.provider.user.UserProvider;
import fun.barryhome.cloud.provider.user.UserWebProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created on 2020/9/4 3:47 下午
 *
 * @author barry
 * Description:
 */
@Slf4j
@RestController
public class UserController {

    @DubboReference
    private UserProvider userProvider;

    @Autowired
    private UserWebProvider userWebProvider;

    /**
     * 已登陆用户
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/sessionUser")
    public UserDTO sessionUser(HttpServletRequest request) {

        String userName = request.getHeader("X-User-Name");
        if (Strings.isEmpty(userName)) {
            throw new RuntimeException("没有找到用户");
        }

        return userProvider.findByUserName(userName);
    }

    /**
     * 已登陆用户
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/sessionUserWeb")
    public UserDTO sessionUserWeb(HttpServletRequest request) {

        String userName = request.getHeader("X-User-Name");
        if (Strings.isEmpty(userName)) {
            throw new RuntimeException("没有找到用户");
        }

        return userWebProvider.findByUserName(userName);
    }
}
