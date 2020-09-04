package fun.barryhome.cloud.provider.user;

/**
 * Created on 2020/8/24 11:03 上午
 *
 * @author barry
 * Description:
 */
public interface UserProvider {

    /**
     * 检查用户
     * @param userName
     * @param password
     * @return
     */
    UserDTO checkUser(String userName, String password);

    /**
     * 查询用户
     * @param userName
     * @return
     */
    UserDTO findByUserName(String userName);
}
