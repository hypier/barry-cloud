package fun.barryhome.cloud.infrastructure.repository.permission;



import fun.barryhome.cloud.domain.permission.Permission;

import java.util.List;

/**
 * Created on 2020/8/24 10:10 上午
 *
 * @author barry
 * Description:
 */
public interface PermissionRepository {

    List<Permission> findAll();

    List<Permission> findByUserName(String userName);

}
