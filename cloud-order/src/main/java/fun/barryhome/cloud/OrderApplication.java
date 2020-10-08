package fun.barryhome.cloud;

import fun.barryhome.cloud.annotation.EnableScopeAuth;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * Created on 2020/8/15 8:29 下午
 *
 * @author barry
 * Description:
 */
@EnableOpenApi
@EnableScopeAuth
@EnableDiscoveryClient
@SpringBootApplication
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
