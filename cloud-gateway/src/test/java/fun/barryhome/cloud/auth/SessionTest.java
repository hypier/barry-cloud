package fun.barryhome.cloud.auth;

import fun.barryhome.cloud.provider.permission.PermissionProvider;
import org.apache.dubbo.config.annotation.DubboReference;
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

    @DubboReference
    private PermissionProvider permissionProvider;
    @Autowired
    private Session session;

    @Test
    void savePermissions() {
        session.savePermissions(permissionProvider.findAll());
    }
}