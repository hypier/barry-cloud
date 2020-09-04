package fun.barryhome.cloud.provider.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created on 2020/9/4 5:31 下午
 *
 * @author barry
 * Description:
 */
@FeignClient(name = "cloud-user")
public interface UserWebProvider {

    @GetMapping(value = "/queryUser")
    UserDTO findByUserName(@RequestParam("userName") String userName);
}
