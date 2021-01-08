package fun.barryhome.cloud.controller;

import fun.barryhome.cloud.annotation.ScopeAuth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;

/**
 * Created on 2019-07-29 18:25
 *
 * @author barry
 * Description:
 */
@RestController
public class HomeController {

    @ScopeAuth(scopes = {"#orderDTO.cities"}, token = "#request.getHeader(\"X-User-Name\")")
    @PostMapping(value = "/query")
    public String query(@RequestBody OrderDTO orderDTO, HttpServletRequest request) {
        return Arrays.toString(orderDTO.getCities());
    }

    @GetMapping(value = "/home")
    public String home(HttpServletRequest request) throws UnsupportedEncodingException {
        String userName = request.getHeader("X-User-Name");
        String realName = URLDecoder.decode(request.getHeader("X-Real-Name"), "utf-8");

        return String.format("userName: %s, realName: %s", userName, realName);
    }

}

