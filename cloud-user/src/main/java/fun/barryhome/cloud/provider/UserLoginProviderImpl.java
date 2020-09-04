package fun.barryhome.cloud.provider;

import fun.barryhome.cloud.convertor.UserConvertor;
import fun.barryhome.cloud.domain.user.User;
import fun.barryhome.cloud.domain.user.UserService;
import fun.barryhome.cloud.provider.user.UserLoginProvider;
import fun.barryhome.cloud.provider.user.UserDTO;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created on 2020/9/3 10:38 上午
 *
 * @author barry
 * Description:
 */
@Service
public class UserLoginProviderImpl implements UserLoginProvider {

    @Autowired
    private UserService userService;

    /**
     * 检查用户
     *
     * @param userName
     * @param password
     * @return
     */
    @Override
    public UserDTO checkUser(String userName, String password) {
        User user = userService.checkUser(userName, password);
        return UserConvertor.toDTO(user);
    }
}
