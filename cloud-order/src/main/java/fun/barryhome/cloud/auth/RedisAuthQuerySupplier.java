package fun.barryhome.cloud.auth;

import fun.barryhome.cloud.AuthQuerySupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created on 2020/10/8 11:30 上午
 *
 * @author barry
 * Description:
 */
@Component
public class RedisAuthQuerySupplier implements AuthQuerySupplier {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 查询范围
     *
     * @param key
     * @return
     */
    @Override
    public Set<String> queryScope(String key) {
        String AUTH_USER_KEY = "auth:logic:user:%s";
        String redisKey = String.format(AUTH_USER_KEY, key);

        List<String> range = redisTemplate.opsForList().range(redisKey, 0, -1);

        if (range != null) {
            return new HashSet<>(range);
        } else {
            return null;
        }
    }
}
