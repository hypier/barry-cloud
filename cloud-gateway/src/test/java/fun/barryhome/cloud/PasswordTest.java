package fun.barryhome.cloud;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.UUID;

/**
 * Created on 2020/8/23 7:14 下午
 *
 * @author barry
 * Description:
 */
class PasswordTest {

    @Test
    void hello() {
        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder("secret");

        String password = UUID.randomUUID().toString();
        String pbk1 = pbkdf2PasswordEncoder.encode(password);


        System.out.println(pbk1);
    }
}