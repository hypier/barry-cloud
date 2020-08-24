package fun.barryhome.cloud.auth;

import com.alibaba.fastjson.JSON;
import fun.barryhome.cloud.domain.Permission;
import fun.barryhome.cloud.dto.LoginUser;
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

    Long expireTime = 10800L;

    /**
     * 保存session
     *
     * @param loginUser
     */
    public void saveSession(LoginUser loginUser) {
        String key = String.format("login:user:%s", loginUser.getUserToken());

        redisTemplate.opsForValue().set(key, JSON.toJSONString(loginUser),
                expireTime, TimeUnit.SECONDS);
    }

    /**
     * 获取session
     *
     * @param token
     * @return
     */
    public LoginUser getSession(String token) {
        String key = String.format("login:user:%s", token);

        String s = redisTemplate.opsForValue().get(key);
        if (Strings.isEmpty(s)) {
            return null;
        }

        return JSON.parseObject(s, LoginUser.class);
    }

    /**
     * 保存权限列表
     *
     * @param permissions
     */
    public void savePermissions(List<Permission> permissions) {
        String key = "login:permission:all";

        saveList(permissions, key);
    }

    /**
     * 保存用户的权限列表
     *
     * @param userName
     * @param permissions
     */
    public void saveUserPermissions(String userName, List<Permission> permissions) {
        String key = String.format("login:permission:%s", userName);

        saveList(permissions, key);
    }

    private void saveList(List<Permission> permissions, String key) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.putAll(key, permissions.stream().collect(
                Collectors.toMap(p -> p.getMethod().concat(":").concat(p.getUri()),
                        Permission::getName, (k1, k2) -> k2)));

        if (expireTime != null) {
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        }
    }

    /**
     * 检查是否有权限
     * @param userName
     * @param uri
     * @param method
     * @return
     */
    public boolean checkPermissions(String userName, String uri, String method) {
        String key = String.format("login:permission:%s", userName);
        String hashKey = String.format("%s:%s", method, uri);

        if (redisTemplate.opsForHash().hasKey(key, hashKey)){
            return  true;
        }

        String allKey = "login:permission:all";
        // 权限列表中没有则通过
        return !redisTemplate.opsForHash().hasKey(allKey, hashKey);
    }
}
