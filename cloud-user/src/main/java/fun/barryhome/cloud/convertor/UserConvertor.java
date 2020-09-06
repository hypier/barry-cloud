package fun.barryhome.cloud.convertor;


import fun.barryhome.cloud.domain.user.User;
import fun.barryhome.cloud.provider.user.UserDTO;
import org.springframework.beans.BeanUtils;

/**
 * Created on 2020/9/3 10:56 上午
 *
 * @author barry
 * Description:
 */
public class UserConvertor {

    /**
     * to dto
     * @param user
     * @return
     */
    public static UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }
}
