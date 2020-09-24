package fun.barryhome.cloud;

import fun.barryhome.cloud.stream.Receiver;
import fun.barryhome.cloud.stream.Sender;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * Created on 2020/9/22 11:30 上午
 *
 * @author barry
 * Description:
 */
@EnableBinding(value = {Sender.class, Receiver.class})
@SpringBootApplication
public class RocketApplication {
    public static void main(String[] args) {
        SpringApplication.run(RocketApplication.class, args);
    }
}

