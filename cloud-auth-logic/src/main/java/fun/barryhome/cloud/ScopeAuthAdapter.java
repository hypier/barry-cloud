package fun.barryhome.cloud;


import java.util.Set;

/**
 * Created on 2020/10/8 11:09 上午
 *
 * @author barry
 * Description:
 */
public class ScopeAuthAdapter {

    private final AuthQuerySupplier supplier;

    public ScopeAuthAdapter(AuthQuerySupplier supplier) {
        this.supplier = supplier;
    }

    /**
     * 验证权限范围
     * @param token
     * @param requestScope
     * @return
     */
    public Set<String> identifyPermissionScope(String token, Set<String> requestScope) {
        Set<String> authorizeScope = supplier.queryScope(token);

        String ALL_SCOPE = "AUTH_ALL";
        String USER_ALL = "USER_ALL";

        if (authorizeScope == null) {
            return null;
        }

        if (authorizeScope.contains(ALL_SCOPE)) {
            // 如果是全开放则返回请求范围
            return requestScope;
        }

        if (requestScope == null) {
            return null;
        }

        if (requestScope.contains(USER_ALL)){
            // 所有授权的范围
            return authorizeScope;
        }

        // 移除不同的元素
        requestScope.retainAll(authorizeScope);

        return requestScope;
    }
}
