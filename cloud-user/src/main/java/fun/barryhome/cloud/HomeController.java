package fun.barryhome.cloud;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created on 2019-07-29 18:25
 *
 * @author barry
 * Description:
 */
@RestController
public class HomeController {

    @GetMapping(value = "/home")
    public String home(HttpServletRequest request) throws UnsupportedEncodingException {
        String userName = request.getHeader("X-User-Name");
        String realName = URLDecoder.decode(request.getHeader("X-Real-Name"), "utf-8");

        return String.format("userName: %s, realName: %s", userName, realName);
    }


    @GetMapping(value = "/hello")
    public String hello() {
        return "User Home !";
    }


    @GetMapping(value = "/user/hello")
    public String userHello() {
        return "User Home !!";
    }

    @PostMapping(value = "/user")
    public String user() {
        return "Post User Home !";
    }

}

