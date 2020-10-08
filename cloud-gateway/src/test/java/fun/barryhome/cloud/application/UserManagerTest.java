package fun.barryhome.cloud.application;

import fun.barryhome.cloud.provider.user.UserDTO;
import fun.barryhome.cloud.provider.user.UserProvider;
import org.apache.dubbo.config.annotation.DubboReference;
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

    @DubboReference
    private UserProvider userProvider;

    @Test
    void login() {
        UserDTO admin = userProvider.checkUser("admin", "123456");

        System.out.println(admin);
    }
}