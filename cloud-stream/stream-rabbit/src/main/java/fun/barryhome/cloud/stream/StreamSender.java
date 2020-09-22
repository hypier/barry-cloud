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
    private Sender sender;

    @GetMapping("/")
    public String send() {
        Date date = new Date();
        sender.message().send(MessageBuilder.withPayload("Hello World..." + date).build());
        return "OK " + date;
    }

}
