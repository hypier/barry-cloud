package fun.barryhome.cloud.provider;

import fun.barryhome.cloud.convertor.UserConvertor;
import fun.barryhome.cloud.domain.user.User;
import fun.barryhome.cloud.domain.user.UserService;
import fun.barryhome.cloud.infrastructure.repository.user.UserRepository;
import fun.barryhome.cloud.provider.user.UserDTO;
import fun.barryhome.cloud.provider.user.UserProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created on 2020/9/3 10:38 上午
 *
 * @author barry
 * Description:
 */
@Slf4j
@Service
public class UserProviderImpl implements UserProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

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

    /**
     * 查询用户
     *
     * @param userName
     * @return
     */
    @Override
    public UserDTO findByUserName(String userName) {

        User user = userRepository.findByUserName(userName);
        return UserConvertor.toDTO(user);
    }
}
