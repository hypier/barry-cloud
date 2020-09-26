package fun.barryhome.cloud;

import fun.barryhome.cloud.stream.Sink;
import fun.barryhome.cloud.stream.Source;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * Created on 2020/9/22 11:30 上午
 *
 * @author barry
 * Description:
 */
@EnableBinding(value = {Source.class, Sink.class})
@EnableDiscoveryClient
@SpringBootApplication
public class RabbitApplication {
    public static void main(String[] args) {
        SpringApplication.run(RabbitApplication.class, args);
    }
}

