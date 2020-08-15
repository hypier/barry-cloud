package fun.barryhome.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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

    @GetMapping(value = "/hello")
    public String hello() {
        return "Demo project for Spring Boot !";
    }

    @GetMapping(value = "/query")
    public LoginUser token(@RequestParam String token) {
        return session.getSession(token);
    }

    @PostMapping(path = "/login")
    public LoginUser login(@RequestParam String userName, @RequestParam String password) {
        return checkUser(userName, password);
    }

    /**
     * 验证
     *
     * @param userName
     * @param password
     * @return
     */
    private LoginUser checkUser(String userName, String password) {
        if (!"admin".equals(userName) && !"123456".equals(password)) {
            throw new RuntimeException("用户名或密码错误");
        }

        String token = UUID.randomUUID().toString();
        LoginUser loginUser = new LoginUser();
        loginUser.setUserName("admin");
        loginUser.setRealName("管理员");
        loginUser.setLoginTime(new Date());
        loginUser.setUserToken(token);

        session.saveSession(loginUser);
        return loginUser;
    }


}

