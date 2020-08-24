package fun.barryhome.cloud.application;

import fun.barryhome.cloud.dto.LoginUser;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created on 2020/8/24 11:29 上午
 *
 * @author barry
 * Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class UserManagerTest {

    @Autowired
    private UserManager userManager;

    @Test
    void login() {
        LoginUser admin = userManager.login("admin", "123456");

        System.out.println(admin);
    }
}