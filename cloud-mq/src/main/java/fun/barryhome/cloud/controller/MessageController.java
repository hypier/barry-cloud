package fun.barryhome.cloud.controller;

import fun.barryhome.cloud.constant.MessageConstant;
import fun.barryhome.cloud.dto.LoginUser;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * Created on 2020/8/29 6:02 下午
 *
 * @author barry
 * Description:
 */
@RestController
public class MessageController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/send")
    public LoginUser SendLoginSucceedMessage() {
        LoginUser loginUser = getLoginUser("succeed");
        // 发送消息
        rabbitTemplate.convertAndSend(MessageConstant.MESSAGE_EXCHANGE,
                MessageConstant.LOGIN_ROUTING_KEY, loginUser);
        return loginUser;
    }

    @GetMapping("/sendDelay")
    public LoginUser SendDelayLoginSucceedMessage() {
        LoginUser loginUser = getLoginUser("succeed");

        MessagePostProcessor messagePostProcessor = message -> {
            // 延时10s
            message.getMessageProperties().setHeader("x-delay", 10000);
            return message;
        };

        // 发送消息
        rabbitTemplate.convertAndSend(MessageConstant.MESSAGE_EXCHANGE,
                MessageConstant.LOGIN_ROUTING_KEY, loginUser, messagePostProcessor);
        return loginUser;
    }

    @GetMapping("/sendFailed")
    public LoginUser SendLoginFailedMessage() {
        LoginUser loginUser = getLoginUser("failed");

        // 发送消息
        rabbitTemplate.convertAndSend(MessageConstant.MESSAGE_EXCHANGE,
                MessageConstant.LOGIN_ROUTING_KEY, loginUser);

        return loginUser;
    }

    /**
     * 获取已登陆用户
     *
     * @return
     */
    private LoginUser getLoginUser(String status) {
        return LoginUser.builder()
                .userName("admin")
                .realName("管理员")
                .userToken(UUID.randomUUID().toString())
                .loginTime(new Date())
                .status(status)
                .build();
    }
}
