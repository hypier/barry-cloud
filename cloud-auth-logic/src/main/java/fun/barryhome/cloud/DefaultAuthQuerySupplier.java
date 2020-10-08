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
public class DefaultAuthQuerySupplier implements AuthQuerySupplier {

    /**
     * 查询范围
     *
     * @param key
     * @return
     */
    @Override
    public Set<String> queryScope(String key) {
        Set<String> list = new HashSet<>();
        list.add("abc");
        return list;
    }
}
