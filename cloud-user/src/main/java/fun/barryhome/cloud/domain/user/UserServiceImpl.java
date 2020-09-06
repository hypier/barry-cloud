package fun.barryhome.cloud.domain.user;


import fun.barryhome.cloud.infrastructure.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created on 2020/8/24 11:05 上午
 *
 * @author barry
 * Description:
 */
@Service
public class UserServiceImpl implements UserService{
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
    public User checkUser(String userName, String password) {
        User dbUser = userRepository.findByUserName(userName);

        if (dbUser == null){
            throw new RuntimeException("没有此用户");
        }

        if (!password.equals(dbUser.password)){
            throw new RuntimeException("密码错误");
        }

        return dbUser;
    }
}
