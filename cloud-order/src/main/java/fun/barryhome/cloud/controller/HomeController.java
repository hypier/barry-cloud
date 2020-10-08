package fun.barryhome.cloud.controller;


import fun.barryhome.cloud.annotation.ScopeAuth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Created on 2019-07-29 18:25
 *
 * @author barry
 * Description:
 */
@RestController
public class HomeController {

    @ScopeAuth(scopes = {"#cities"}, token = "#request.getHeader(\"X-User-Name\")")
    @GetMapping(value = "/query")
    public String query(String[] cities, HttpServletRequest request) {
        return Arrays.toString(cities);
    }

}

