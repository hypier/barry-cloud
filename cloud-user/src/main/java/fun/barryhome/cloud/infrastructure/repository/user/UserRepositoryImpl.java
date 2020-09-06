package fun.barryhome.cloud.infrastructure.repository.user;

import fun.barryhome.cloud.domain.user.User;
import org.springframework.stereotype.Repository;

/**
 * Created on 2020/8/24 11:01 上午
 *
 * @author barry
 * Description:
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    @Override
    public User findByUserName(String userName) {
        return User.builder()
                .userName("admin")
                .realName("管理员")
                .password("123456")
                .build();
    }
}
