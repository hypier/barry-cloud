package fun.barryhome.cloud.domain.user;

/**
 * Created on 2020/8/24 11:03 上午
 *
 * @author barry
 * Description:
 */
public interface UserService {

    /**
     * 检查用户
     * @param userName
     * @param password
     * @return
     */
    User checkUser(String userName, String password);
}
