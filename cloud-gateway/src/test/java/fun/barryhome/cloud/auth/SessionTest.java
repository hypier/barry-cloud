package fun.barryhome.cloud.auth;

import fun.barryhome.cloud.api.permission.PermissionApi;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created on 2020/8/24 10:26 上午
 *
 * @author barry
 * Description:
 */

@RunWith(SpringRunner.class)
@SpringBootTest
class SessionTest {

    @Reference
    private PermissionApi permissionApi;
    @Autowired
    private Session session;

    @Test
    void savePermissions() {
        session.savePermissions(permissionApi.findAll());
    }
}