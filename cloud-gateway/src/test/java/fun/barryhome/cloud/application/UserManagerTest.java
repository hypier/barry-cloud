package fun.barryhome.cloud.application;

import fun.barryhome.cloud.api.user.UserDTO;
import fun.barryhome.cloud.api.user.UserLoginApi;
import fun.barryhome.cloud.dto.LoginUser;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created on 2020/8/24 11:29 上午
 *
 * @author barry
 * Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class UserManagerTest {

    @Reference
    private UserLoginApi userLoginApi;

    @Test
    void login() {
        UserDTO admin = userLoginApi.checkUser("admin", "123456");

        System.out.println(admin);
    }
}