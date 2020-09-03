package fun.barryhome.cloud.api;

import fun.barryhome.cloud.api.permission.PermissionApi;
import fun.barryhome.cloud.api.permission.PermissionDTO;
import fun.barryhome.cloud.convertor.PermissionConvertor;
import fun.barryhome.cloud.domain.permission.Permission;
import fun.barryhome.cloud.infrastructure.repository.permission.PermissionRepository;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created on 2020/9/3 5:46 下午
 *
 * @author barry
 * Description:
 */
@Service
public class PermissionApiImpl implements PermissionApi {

    @Autowired
    private PermissionRepository permissionRepository;

    /**
     * 查询所有权限
     *
     * @return
     */
    @Override
    public List<PermissionDTO> findAll() {
        List<Permission> list = permissionRepository.findAll();

        return PermissionConvertor.toDTO(list);
    }

    /**
     * 查询单个用户权限
     *
     * @param userName
     * @return
     */
    @Override
    public List<PermissionDTO> findByUserName(String userName) {
        List<Permission> list = permissionRepository.findByUserName(userName);
        return PermissionConvertor.toDTO(list);
    }
}
