package fun.barryhome.cloud.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created on 2020/9/22 11:33 上午
 *
 * @author barry
 * Description:
 */
@RestController
public class StreamSender {

    @Autowired
    private Source source;

    @GetMapping("/")
    public String sendSucceed() {
        Date date = new Date();
        source.message().send(MessageBuilder.withPayload("Hello World..." + date)
                .setHeader("routingKey", "login.user.succeed")
                .setHeader("version", "1.0")

                .build());
        return "OK " + date;
    }

    @GetMapping("/v2")
    public String sendHeader() {
        Date date = new Date();
        source.message().send(MessageBuilder.withPayload("Hello World..." + date)
                .setHeader("routingKey", "login.user.succeed")
                .setHeader("version", "2.0")
                .setHeader("x-delay", 5000)
                .build());
        return "OK " + date;
    }

    @GetMapping("/failed")
    public String sendFailed() {
        Date date = new Date();
        source.message().send(MessageBuilder.withPayload("Hello World..." + date)
                .setHeader("routingKey", "login.user.failed")
                .build());
        return "OK " + date;
    }
}
