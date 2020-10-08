package fun.barryhome.cloud.auth;

import com.alibaba.fastjson.JSON;
import fun.barryhome.cloud.dto.LoginUser;
import fun.barryhome.cloud.provider.permission.PermissionDTO;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created on 2020/8/15 5:24 下午
 *
 * @author barry
 * Description:
 */
@Service
public class Session {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final Long EXPIRE_TIME = 10800L;
    private final String LOGIN_USER_KEY = "login:user:%s";
    private final String LOGIN_USER_PERMISSION_KEY = "login:permission:%s";
    private final String LOGIN_ALL_PERMISSION_KEY = "login:permission:all";

    /**
     * 保存session
     *
     * @param loginUser
     */
    public void saveSession(LoginUser loginUser) {
        String key = String.format(LOGIN_USER_KEY, loginUser.getUserToken());

        redisTemplate.opsForValue().set(key, JSON.toJSONString(loginUser),
                EXPIRE_TIME, TimeUnit.SECONDS);
    }

    /**
     * 获取session
     *
     * @param token
     * @return
     */
    public LoginUser getSession(String token) {
        String key = String.format(LOGIN_USER_KEY, token);

        String s = redisTemplate.opsForValue().get(key);
        if (Strings.isEmpty(s)) {
            return null;
        }

        return JSON.parseObject(s, LoginUser.class);
    }

    /**
     * 保存权限列表
     *
     * @param PermissionDTOs
     */
    public void savePermissions(List<PermissionDTO> PermissionDTOs) {
        saveList(PermissionDTOs, LOGIN_ALL_PERMISSION_KEY);
    }

    /**
     * 保存用户的权限列表
     *
     * @param userName
     * @param PermissionDTOs
     */
    public void saveUserPermissions(String userName, List<PermissionDTO> PermissionDTOs) {
        String key = String.format(LOGIN_USER_PERMISSION_KEY, userName);

        saveList(PermissionDTOs, key);
    }

    private void saveList(List<PermissionDTO> PermissionDTOs, String key) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.putAll(key, PermissionDTOs.stream().collect(
                Collectors.toMap(p -> p.getMethod().concat(":").concat(p.getUri()),
                        PermissionDTO::getName, (k1, k2) -> k2)));

        redisTemplate.expire(key, EXPIRE_TIME, TimeUnit.SECONDS);
    }

    /**
     * 检查是否有权限
     * @param userName
     * @param uri
     * @param method
     * @return
     */
    public boolean checkPermissions(String userName, String uri, String method) {
        String key = String.format(LOGIN_USER_PERMISSION_KEY, userName);
        String hashKey = String.format("%s:%s", method, uri);

        if (redisTemplate.opsForHash().hasKey(key, hashKey)){
            return  true;
        }

        // 权限列表中没有则通过
        return !redisTemplate.opsForHash().hasKey(LOGIN_ALL_PERMISSION_KEY, hashKey);
    }
}
