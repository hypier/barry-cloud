package fun.barryhome.cloud.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Created on 2020/8/15 4:45 下午
 *
 * @author barry
 * Description:
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements Serializable {
    String userName;
    String realName;
    String userToken;
    Date loginTime;
    String status;
}
