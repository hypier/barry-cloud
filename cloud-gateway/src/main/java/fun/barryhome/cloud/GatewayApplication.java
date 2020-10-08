package fun.barryhome.cloud;

import fun.barryhome.cloud.auth.Session;
import fun.barryhome.cloud.provider.permission.PermissionProvider;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * Created on 2020/8/15 4:08 下午
 *
 * @author barry
 * Description:
 */
@EnableOpenApi
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Autowired
    private Session session;

    @DubboReference
    private PermissionProvider permissionProvider;

    @Override
    public void run(String... args) throws Exception {
        session.savePermissions(permissionProvider.findAll());
    }
}
