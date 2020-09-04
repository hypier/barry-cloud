package fun.barryhome.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ImportResource;

/**
 * Created on 2020/8/15 4:08 下午
 *
 * @author barry
 * Description:
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@ImportResource(value = "classpath:rabbitmq-spring.xml")
public class MQApplication {

    public static void main(String[] args) {
        SpringApplication.run(MQApplication.class, args);
    }


}
