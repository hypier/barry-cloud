package fun.barryhome.cloud;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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