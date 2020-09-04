package fun.barryhome.cloud.controller;

import fun.barryhome.cloud.provider.user.UserDTO;
import fun.barryhome.cloud.provider.user.UserLoginProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.logging.log4j.util.Strings;
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

    @Reference(loadbalance = "leastactive", filter = "activelimit")
    private UserLoginProvider userLoginProvider;

    /**
     * 已登陆用户
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/sessionUser")
    public UserDTO sessionUser(HttpServletRequest request) {
        log.error("cloud-mq:{}", "sessionUser");

        String userName = request.getHeader("X-User-Name");
        if (Strings.isEmpty(userName)) {
            throw new RuntimeException("没有找到用户");
        }

        return userLoginProvider.findByUserName(userName);
    }
}
