package fun.barryhome.cloud.convertor;

import fun.barryhome.cloud.domain.permission.Permission;
import fun.barryhome.cloud.provider.permission.PermissionDTO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2020/9/3 5:43 下午
 *
 * @author barry
 * Description:
 */
public class PermissionConvertor {

    public static PermissionDTO toDTO(Permission permission) {
        PermissionDTO permissionDTO = new PermissionDTO();
        BeanUtils.copyProperties(permission, permissionDTO);
        return permissionDTO;
    }

    public static List<PermissionDTO> toDTO(List<Permission> permissions) {
        List<PermissionDTO> list = new ArrayList<>();

        for (Permission p : permissions) {
            list.add(toDTO(p));
        }

        return list;
    }
}
