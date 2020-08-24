package fun.barryhome.cloud.controller;

import fun.barryhome.cloud.application.UserManager;
import fun.barryhome.cloud.auth.Session;
import fun.barryhome.cloud.dto.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 2019-07-29 18:25
 *
 * @author barry
 * Description:
 */
@RestController
public class HelloController {

    @Autowired
    private UserManager userManager;
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
        return userManager.login(userName, password);
    }


}

