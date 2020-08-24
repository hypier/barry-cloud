package fun.barryhome.cloud.auth;

import fun.barryhome.cloud.GatewayApplication;
import fun.barryhome.cloud.infrastructure.permission.PermissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created on 2020/8/24 10:26 上午
 *
 * @author barry
 * Description:
 */

@RunWith(SpringRunner.class)
@SpringBootTest
class SessionTest {

    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private Session session;

    @Test
    void savePermissions() {
        session.savePermissions(permissionRepository.findAll());
    }
}