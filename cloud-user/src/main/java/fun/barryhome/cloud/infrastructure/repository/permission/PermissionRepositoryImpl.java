package fun.barryhome.cloud.infrastructure.repository.permission;


import fun.barryhome.cloud.domain.permission.Permission;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2020/8/24 10:11 上午
 *
 * @author barry
 * Description:
 */
@Repository
public class PermissionRepositoryImpl implements PermissionRepository {

    @Override
    public List<Permission> findAll() {
        List<Permission> list = new ArrayList<>();

        list.add(Permission.builder()
                .uri("/user/home")
                .method("GET")
                .name("home")
                .build());
        list.add(Permission.builder()
                .uri("/user/hello")
                .method("GET")
                .name("hello")
                .build());
        list.add(Permission.builder()
                .uri("/order/query")
                .method("GET")
                .name("query")
                .build());
        list.add(Permission.builder()
                .uri("/user/user")
                .method("POST")
                .name("user")
                .build());

        return list;
    }

    @Override
    public List<Permission> findByUserName(String userName) {

        List<Permission> list = new ArrayList<>();

        list.add(Permission.builder()
                .uri("/user/hello")
                .method("GET")
                .name("hello")
                .build());

        list.add(Permission.builder()
                .uri("/user/user")
                .method("POST")
                .name("user")
                .build());
        list.add(Permission.builder()
                .uri("/order/query")
                .method("GET")
                .name("query")
                .build());

        return list;
    }
}
