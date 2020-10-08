package fun.barryhome.cloud;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created on 2020/10/8 11:30 上午
 *
 * @author barry
 * Description:
 */
@Component
public class DefaultAuthenticationSupplier implements AuthenticationSupplier {
    /**
     * 保存范围
     *
     * @param key
     * @param values
     */
    @Override
    public void importScope(String key, Set<String> values) {

    }

    /**
     * 查询范围
     *
     * @param key
     * @return
     */
    @Override
    public Set<String> findScope(String key) {
        Set<String> list = new HashSet<>();
        list.add("AUTH_ALL");
        return list;
    }
}
