package fun.barryhome.cloud.provider.permission;

import java.util.List;

/**
 * Created on 2020/9/3 5:40 下午
 *
 * @author barry
 * Description:
 */
public interface PermissionProvider {
    /**
     * 查询所有权限
     * @return
     */
    List<PermissionDTO> findAll();

    /**
     * 查询单个用户权限
     * @param userName
     * @return
     */
    List<PermissionDTO> findByUserName(String userName);
}
