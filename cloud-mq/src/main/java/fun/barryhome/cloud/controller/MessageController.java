package fun.barryhome.cloud.controller;

import fun.barryhome.cloud.constant.MessageConstant;
import fun.barryhome.cloud.dto.LoginUser;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public LoginUser SendLoginMessage(){
        LoginUser loginUser = getLoginUser();

        // 发送消息
        rabbitTemplate.convertAndSend(MessageConstant.MESSAGE_EXCHANGE,
                MessageConstant.LOGIN_ROUTING_KEY, loginUser);

        return loginUser;
    }

    /**
     * 获取已登陆用户
     * @return
     */
    private LoginUser getLoginUser() {
        return LoginUser.builder()
                .userName("admin")
                .realName("管理员")
                .userToken(UUID.randomUUID().toString())
                .loginTime(new Date())
                .build();
    }
}
