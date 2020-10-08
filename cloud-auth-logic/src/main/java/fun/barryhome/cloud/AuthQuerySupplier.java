package fun.barryhome.cloud;

import java.util.Set;

/**
 * Created on 2020/10/8 11:09 上午
 *
 * @author barry
 * Description: 权限查询器，可使用redis，DB保存
 */
public interface AuthQuerySupplier {

    /**
     * 查询范围
     * @param key
     * @return
     */
    Set<String> queryScope(String key);
}
