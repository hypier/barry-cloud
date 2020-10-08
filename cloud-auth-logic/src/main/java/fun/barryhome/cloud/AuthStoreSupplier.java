package fun.barryhome.cloud;

import java.util.Set;

/**
 * Created on 2020/10/8 11:09 上午
 *
 * @author barry
 * Description: 权限存储器，可使用redis，DB保存
 */
public interface AuthStoreSupplier {
    /**
     * 保存范围
     * @param key
     * @param values
     */
    void storeScope(String key, Set<String> values);

}
