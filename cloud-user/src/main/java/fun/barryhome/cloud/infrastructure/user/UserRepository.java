package fun.barryhome.cloud.infrastructure.user;

import fun.barryhome.cloud.domain.user.User;

/**
 * Created on 2020/8/24 11:01 上午
 *
 * @author barry
 * Description:
 */
public interface UserRepository {

    User findByUserName(String userName);
}
