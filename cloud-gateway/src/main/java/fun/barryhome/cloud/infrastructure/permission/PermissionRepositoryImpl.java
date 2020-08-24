package fun.barryhome.cloud.infrastructure.permission;

import fun.barryhome.cloud.domain.Permission;
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
                .uri("/hello")
                .method("GET")
                .name("hello")
                .build());

        list.add(Permission.builder()
                .uri("/query")
                .method("GET")
                .name("query")
                .build());

        return list;
    }

    @Override
    public List<Permission> findByUserName(String userName) {

        List<Permission> list = new ArrayList<>();

        list.add(Permission.builder()
                .uri("/hello")
                .method("GET")
                .name("hello")
                .build());

        list.add(Permission.builder()
                .uri("/query")
                .method("GET")
                .name("query")
                .build());

        return list;
    }
}
