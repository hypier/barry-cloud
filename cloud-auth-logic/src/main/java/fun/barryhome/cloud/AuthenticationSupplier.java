package fun.barryhome.cloud;

import java.util.Set;

/**
 * Created on 2020/10/8 11:09 上午
 *
 * @author barry
 * Description: 权限存储器，可使用redis，DB保存
 */
public interface AuthenticationSupplier {
    /**
     * 保存范围
     * @param key
     * @param values
     */
    void importScope(String key, Set<String> values);

    /**
     * 查询范围
     * @param key
     * @return
     */
    Set<String> findScope(String key);
}
