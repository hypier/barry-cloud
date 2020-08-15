package fun.barryhome.cloud;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 2019-07-29 18:25
 *
 * @author barry
 * Description:
 */
@RestController
public class HomeController {

    @GetMapping(value = "/home")
    public String home() {
        return "User Home !";
    }


}

