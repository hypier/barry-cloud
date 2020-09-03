package fun.barryhome.cloud.application;

import fun.barryhome.cloud.auth.Session;
import fun.barryhome.cloud.domain.Permission;
import fun.barryhome.cloud.dto.LoginUser;
import fun.barryhome.cloud.infrastructure.permission.PermissionRepository;
import fun.barryhome.cloud.api.user.UserApi;
import fun.barryhome.cloud.api.user.UserDTO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created on 2020/8/24 11:09 上午
 *
 * @author barry
 * Description:
 */
@Component
public class UserManager {

    @Reference
    private UserApi userApi;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private Session session;

    /**
     * 用户登陆
     * @param userName
     * @param password
     * @return
     */
    public LoginUser login(String userName, String password){
        // 检查密码
        UserDTO user = userApi.checkUser(userName, password);

        LoginUser loginUser = LoginUser.builder()
                .userName(userName)
                .realName(user.getRealName())
                .userToken(UUID.randomUUID().toString())
                .loginTime(new Date())
                .build();

        // 保存session
        session.saveSession(loginUser);

        // 查询权限
        List<Permission> permissions = permissionRepository.findByUserName(userName);
        // 保存用户权限
        session.saveUserPermissions(userName, permissions);

        return loginUser;
    }
}
