package fun.barryhome.cloud.application;

import fun.barryhome.cloud.dto.LoginUser;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created on 2020/8/29 6:24 下午
 *
 * @author barry
 * Description:
 */
@Component
public class ReceiverMessage {

    @RabbitListener(queues = "login-user-logined")
    public void receiveLoginMessage(LoginUser loginUser) {
        if ("failed".equals(loginUser.getStatus())) {
            throw new RuntimeException("error");
        }

        System.err.println(loginUser);
    }
}
